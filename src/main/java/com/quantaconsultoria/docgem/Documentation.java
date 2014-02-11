package com.quantaconsultoria.docgem;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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

public class Documentation {

	private RemoteWebDriver driver;
	private DocumentationConfiguration configuration;
	private DocumentationScanner scanner;
	private FileManager fileManager;
	
	
	protected Documentation() {
		
	}
	
	public Documentation(RemoteWebDriver driver, DocumentationConfiguration config) {
		this();
		scanner = new DocumentationScanner(config);
		scanner.scan();
		this.driver = driver;
		this.configuration = config;
		fileManager = new FileManager(configuration);
	}

	public void makeIt() throws IOException {
		fileManager.copyResources();
		buildJson();
	}

	private void buildJson() {
		try { 
			List<ChapterBag> sortedChapters = mergeWithXmlChaptersBag();
			Gson gson = new Gson();			
			String json = gson.toJson(sortedChapters);
			json = "var chapters = "+json+";";			
			fileManager.saveJson(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<ChapterBag> mergeWithXmlChaptersBag() throws IOException {
		List<ChapterBag> finalChapters = fileManager.readChapterssXml();
		Map<String, ChapterBag> chapters = fileManager.readActionsFile();
		
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

	public void addAction(String text, WebElement element) {
		try {
			Chapter currentChapter = this.getCurrentChapter();
			Section currentSection = this.getCurrentSection();
			
			File imageFile = null;
			
			if(driver instanceof TakesScreenshot) {
				imageFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			} else {
				throw new RuntimeException("Can't take a screenshot.");
			}
			
			ImageUtil.circulateElement(imageFile, element);
			String imageFinalFile = fileManager.saveScreenshot(imageFile);
			ActionBag action = new ActionBag();
			action.setText(text + getCurrentActionText());
			action.setImageFile(imageFinalFile);
			
			fileManager.writeInfoAction(currentChapter, currentSection, action);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String getCurrentActionText() {
		String actionText = "";
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (StackTraceElement element : stackTrace) {
				if (scanner.existChapter(element)) {
					Method method = ReflectionsUtil.getMethod(element);
					if (scanner.existAction(method)) {
						actionText = " " + scanner.getAction(method).text();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return actionText;
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
