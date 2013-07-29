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
import java.util.List;
import java.util.Map;

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

	public void makeIt() throws IOException {
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
		Map<String, ChapterBag> chapters = getCapterFromActionsFile();
		
		for(ChapterBag xmlChapter : finalChapters) {
			ChapterBag docChapter = chapters.get(xmlChapter.getId());
			if (docChapter!=null) {
				chapters.remove(xmlChapter.getId());
				for(SectionBag docSection : docChapter.getSections()) {
					SectionBag xmlSection = xmlChapter.getSection(docSection.getId());
					if (xmlSection!=null) {
						xmlSection.setActions(docSection.getActions());
					} else {
						xmlChapter.getSections().add(docSection);
					}
				}
			} 
		}
		finalChapters.addAll(chapters.values());
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
		SectionBag sectionBag = chapterBag.getSection(parts[1]); 
		if(sectionBag != null){
			sectionBag.getActions().add(createActionFomArrayInfo(parts));
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

	private List<ChapterBag> getChaptersFromXml() throws IOException {
		InputStream xmlChapters = null;
		try {
			XStream xstream = new XStream();
			xstream.alias("chapters", ArrayList.class);
			xstream.alias("chapter", ChapterBag.class);
			xstream.alias("section", SectionBag.class);
			xstream.alias("action", ActionBag.class);
			
			xmlChapters = new FileInputStream(new File(configuration.getChaptersXmlPath()));
			List<ChapterBag> lista = (List<ChapterBag>)xstream.fromXML(xmlChapters);
			return lista;
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} finally {
			close(xmlChapters);
		}
	}

	private void copyResources() throws IOException {
		File targetDir = new File(configuration.getTarget());
		InputStream index = null;
		InputStream style = null;
		InputStream docgem_js = null;
		
		try {
			index = this.getClass().getResourceAsStream("/templates/index.html");
			style = this.getClass().getResourceAsStream("/templates/style.css");
			docgem_js = this.getClass().getResourceAsStream("/templates/docgem.js");
			
			FileUtils.copyInputStreamToFile(index, new File(targetDir,"index.html"));
			FileUtils.copyInputStreamToFile(style, new File(targetDir, "style.css"));
			FileUtils.copyInputStreamToFile(docgem_js, new File(targetDir, "docgem.js"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(index);
			close(style);
			close(docgem_js);
		}
	}

	public void addAction(String text, WebElement element) {
		try {
			File imageFile = null;
			
			if(driver instanceof TakesScreenshot) {
				imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			} else {
				throw new RuntimeException("Can't take a screenshot.");
			}
			
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
		if (!file.exists() && !file.createNewFile()) {
			throw new RuntimeException("Can't create the actions file.");
		}
		FileLock lock = null;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file,true);
			lock = out.getChannel().lock();
			
			String line = String.format("%s;%s;%s;%s%n", chapter.id(),section.id(),action.getText(),action.getImageFile());
			
			out.write(line.getBytes(configuration.getEncoding()));
			
		} finally {
			close(lock);
			close(out);
		}		
		
	}

	private void close(FileOutputStream out) throws IOException {
		if(out != null) {
			out.close();
		}
	}

	private void close(FileLock lock) throws IOException {
		if(lock != null) {
			lock.release();
			lock.close();
		}
	}
	
	private void close(InputStream stream) throws IOException {
		if(stream != null) {
			stream.close();
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
