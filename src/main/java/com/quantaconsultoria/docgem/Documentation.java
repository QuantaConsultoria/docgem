package com.quantaconsultoria.docgem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.channels.FileLock;
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
	private DocumentationScanner scanner;
	
	
	protected Documentation() {
	}
	
	public Documentation(RemoteWebDriver driver, DocumentationConfiguration config) {
		this();
		scanner = new DocumentationScanner(config);
		scanner.scan();
		this.driver = driver;
		this.configuration = config;
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

	private List<ChapterBag> mergeWithXmlChaptersBag() throws IOException {
		List<ChapterBag> finalChapters = getChaptersFromXml();
		Set<ChapterBag> notUsedChapters = new HashSet<ChapterBag>();		
		Map<String, ChapterBag> chapters = getCapterFromActionsFile();
		
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

	private Map<String, ChapterBag> getCapterFromActionsFile() throws IOException {
		Map<String, ChapterBag> chapters= new HashMap<>();
		
		List<String> lines = FileUtils.readLines(new File(configuration.getActionsFile()));
		for(String line : lines) {
			String[] parts = line.split("\\;");
			
			if(chapters.containsKey(parts[0])) {
				ChapterBag chapterBag = chapters.get(parts[0]);
				updateChapterInfo(chapterBag,parts);
			} else {
				ChapterBag chapterBag = createChapterFromStringArray(parts);
				chapters.put(chapterBag.getId(), chapterBag);
			}
		}
		
		return chapters;
	}

	private void updateChapterInfo(ChapterBag chapterBag, String[] parts) {
		if(chapterBag.getSections().contains(parts[1])){
			chapterBag.getSection(parts[1]).getActions().add(createActionFomArrayInfo(parts));
		} else {
			chapterBag.getSections().add(createSectionFromArrayInfo(parts));
		}
	}

	private SectionBag createSectionFromArrayInfo(String[] parts) {
		SectionBag sectionBag = new SectionBag();
		sectionBag.setId(parts[1]);
		sectionBag.setActions(new ArrayList<ActionBag>());
		sectionBag.getActions().add(createActionFomArrayInfo(parts));
		
		return sectionBag;
	}

	private ActionBag createActionFomArrayInfo(String[] parts) {
		return new ActionBag(parts[2], parts[3]);
	}

	private ChapterBag createChapterFromStringArray(String[] parts) {
		ChapterBag chapterBag = new ChapterBag();
		chapterBag.setId(parts[0]);
		chapterBag.setSections(new ArrayList<SectionBag>());
		
		SectionBag sectionBag = createSectionFromArrayInfo(parts);
		chapterBag.getSections().add(sectionBag);
		
		return chapterBag;
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
		InputStream index = this.getClass().getResourceAsStream("/templates/index.html");
		InputStream style = this.getClass().getResourceAsStream("/templates/style.css");
		InputStream docgem_js = this.getClass().getResourceAsStream("/templates/docgem.js");
		
		try {
			FileUtils.copyInputStreamToFile(index, new File(targetDir,"index.html"));
			FileUtils.copyInputStreamToFile(style, new File(targetDir, "style.css"));
			FileUtils.copyInputStreamToFile(docgem_js, new File(targetDir, "docgem.js"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void addAction(String text, WebElement element) {
		try {
			File imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String imageFinalFile = gravarImagem(imageFile);
			ActionBag action = new ActionBag();
			action.setText(text);
			action.setImageFile(imageFinalFile);
			
			writeInfoAction(this.getCurrentChapter(), this.getCurrentSection(), action);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String gravarImagem(File imagem) throws IOException {
		
		Long id =  (long) (Math.random()*1000000000);
		
		String destFilePath = "images/"+id+".png"; 
		
		File destFile = new File(configuration.getTarget(), destFilePath);
		
		if (!destFile.exists()) {
			FileUtils.copyFile(imagem, destFile);
			return destFilePath;
		} else {
			return gravarImagem(imagem);
		}
		
	}
	
	private void writeInfoAction(Chapter chapter, Section section, ActionBag action) throws IOException {
		File file = new File(configuration.getActionsFile());
		if (!file.exists()) {
			file.createNewFile();
		}
		FileLock lock = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file,true);
			lock = out.getChannel().lock();
			
			String line = String.format("%s;%s;%s;%s\n", chapter.id(),section.id(),action.getText(),action.getImageFile());
			
			out.write(line.getBytes());
			
		} finally {
			lock.release();
			out.close();
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

	private Chapter getCurrentChapter() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			if (scanner.existChapter(element)) {
				return scanner.getChapter(element);
			}
		}
		throw new RuntimeException("Don't exist a Chapter on stack");
	}

}
