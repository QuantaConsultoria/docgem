package com.quantaconsultoria.docgem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.google.gson.Gson;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;
import com.quantaconsultoria.docgem.reflections.ReflectionsUtil;
import com.thoughtworks.xstream.XStream;

public class Documentation {

	private RemoteWebDriver driver;
	private DocumentationConfiguration configuration;
	private long imageCount = 1;
	private DocumentationScanner scanner;
	
	private Map<String, ChapterBag> chapters;
	private Map<String, SectionBag> sections;
	private Map<String, ActionBag> actions;
	
	protected Documentation() {
		chapters = new HashMap<String, ChapterBag>();
		sections = new HashMap<String, SectionBag>();
		actions = new HashMap<String, ActionBag>();
	}
	
	public Documentation(RemoteWebDriver driver, DocumentationConfiguration config) {
		this();
		scanner = new DocumentationScanner(config);
		scanner.scan();
		this.driver = driver;
		this.configuration = config;
	}

	public void writeText(String text) {
		throw new NotImplementedException();
	}

	public void makeIt() {
		copyResources();
		buildJson();
	}

	private void buildJson() {
		try { 
			List<ChapterBag> sortedChapters = mergeWithXmlChaptersBag();
			Gson gson = new Gson();			
			String json = gson.toJson(sortedChapters);
			json = "var chapters = "+json+";";			
			File data_json = new File(configuration.getTarget(),"data.js");			
			FileUtils.writeStringToFile(data_json, json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<ChapterBag> mergeWithXmlChaptersBag() {
		List<ChapterBag> finalChapters = getChaptersFromXml();
		
		Set<ChapterBag> notUsedChapters = new HashSet<ChapterBag>();		
		
		for(ChapterBag xmlChapter : finalChapters) {
			ChapterBag docChapter = chapters.get(xmlChapter.getId());
			if (docChapter!=null) {
				for(SectionBag docSection : docChapter.getSections()) {
					SectionBag xmlSection = xmlChapter.getSection(docSection.getId());
					if (xmlSection!=null) {
						xmlSection.setActions(docSection.getActions());
					} else {
						xmlChapter.getSections().add(docSection);
					}
				}
			} else {
				notUsedChapters.add(docChapter);				
			}
		}
		finalChapters.addAll(notUsedChapters);
		return finalChapters;
	}

	private List<ChapterBag> getChaptersFromXml() {
		try {
			XStream xstream = new XStream();
			xstream.alias("chapters", ArrayList.class);
			xstream.alias("chapter", ChapterBag.class);
			xstream.alias("section", SectionBag.class);
			xstream.alias("action", ActionBag.class);
			
			List<ChapterBag> lista = (List<ChapterBag>)xstream.fromXML(new FileInputStream(new File(configuration.getChaptersXmlPath())));
			return lista;
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private void copyResources() {
		File targetDir = new File(configuration.getTarget());
		File index = new File(this.getClass().getResource("/templates/index.html").getPath());
		File style = new File(this.getClass().getResource("/templates/style.css").getPath());
		File docgem_js = new File(this.getClass().getResource("/templates/docgem.js").getPath());
		
		try {
			FileUtils.copyFileToDirectory(index, targetDir);
			FileUtils.copyFileToDirectory(style, targetDir);
			FileUtils.copyFileToDirectory(docgem_js, targetDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addAction(String text, WebElement element) {
		try {
			File imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			long imageIndex = imageCount++;
			String destFilePath = "images/"+imageIndex+".png"; 
			
			File destFile = new File(configuration.getTarget(), destFilePath);
			FileUtils.copyFile(imageFile, destFile);
			ActionBag action = new ActionBag();
			action.setText(text);
			action.setImageFile(destFilePath);
			
			ChapterBag chapter = this.getCurrentChapter();
			SectionBag section = chapter.getSectionBag(this.getCurrentSection());
			section.getActions().add(action);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Section getCurrentSection() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (StackTraceElement element : stackTrace) {
				if (scanner.existChapter(element)) {
					Method method = ReflectionsUtil.getMethod(element);
					if (scanner.existSection(method)) {
						return scanner.getSection(method);
					}					
				}
			}
			throw new RuntimeException("Don't exist a Section on stack");			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ChapterBag getCurrentChapter() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			if (scanner.existChapter(element)) {
				Chapter chapter = scanner.getChapter(element);
				if (!chapters.containsKey(chapter.id())) {
					ChapterBag bag = new ChapterBag();
					bag.setId(chapter.id());
					bag.setSections(new ArrayList<SectionBag>());
					chapters.put(chapter.id(), bag);
				}
				return chapters.get(chapter.id());
			}
		}
		throw new RuntimeException("Don't exist a Chapter on stack");
	}
	

}
