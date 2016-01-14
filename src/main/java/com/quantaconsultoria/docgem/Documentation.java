package com.quantaconsultoria.docgem;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.*;
import com.quantaconsultoria.docgem.factory.Factory;
import com.quantaconsultoria.docgem.reflections.ReflectionsUtil;
import com.quantaconsultoria.docgem.repository.InformationRepository;
import com.quantaconsultoria.docgem.util.ImageUtil;

public class Documentation {

	private RemoteWebDriver driver;
	private DocumentationScanner scanner;
	private Builder builder;
	private InformationRepository repository;
	private Factory factory;

	
	protected Documentation() {		
	}
	
	public Documentation(RemoteWebDriver driver, Factory factory) {
		this();
		this.driver = driver;
		this.scanner = new DocumentationScanner(factory.getConfiguration());
		this.builder = factory.getBuilder();
		this.repository = factory.getRepository();
		this.factory = factory;
		
		setupDirectory();
		scanner.scan();
		cleanUpActionsLog();
	}

	public void makeIt() throws IOException {
		builder.copyResources();
		buildJson();
	}

	private void buildJson() {
		try { 
			DocumentationBag documentation = mergeLogActionsWithDocumentationInfo();
			builder.saveDocumentationInfo(documentation);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private DocumentationBag mergeLogActionsWithDocumentationInfo() throws IOException {
		DocumentationBag documentation = repository.readDocumentationInfo();
		Map<String, ChapterBag> chapters = repository.readActions();
		
		for(ChapterBag chapterFromFile : documentation.getChapters()) {
			ChapterBag chapterLog = chapters.get(chapterFromFile.getId());
			if (chapterLog!=null) {
				chapters.remove(chapterFromFile.getId());
				for(SectionBag sectionFromFile : chapterLog.getSections()) {
					SectionBag jsonSection = chapterFromFile.getSection(sectionFromFile.getId());
					if (jsonSection!=null) {
						jsonSection.setActions(sectionFromFile.getActions());
					} else {
						chapterFromFile.getSections().add(sectionFromFile);
					}
				}
			} 
		}
		
		documentation.getChapters().addAll(chapters.values());
		documentation.setDateRevision(new Date());
		loadChaptersInfo(documentation);
		return documentation;
	}

	private void loadChaptersInfo(DocumentationBag documentation) throws IOException {
		for(ChapterBag chapterBag : documentation.getChapters()) {
			String content = builder.generateFileDescription(chapterBag.getPath(), factory.getConfiguration());
			chapterBag.setContent(content);
			loadSectionInfo(chapterBag.getSections(), String.valueOf(chapterBag.getIndice()));
		}
	}

	private void loadSectionInfo(List<SectionBag> sections, String indice) throws IOException {
		int i = 1;
		if(sections != null) {
			for(SectionBag sectionBag : sections) {
				String content = builder.generateFileDescription(sectionBag.getPath(), factory.getConfiguration());
				sectionBag.setContent(content);
				loadSectionInfo(sectionBag.getSections(), sectionBag.getIndice());
			}
		}		
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
			
			ImageUtil.highlightElement(imageFile, element, driver.manage().window().getSize());
			String imageFinalFile = ImageUtil.saveScreenshot(imageFile, factory.getConfiguration());
			ActionBag action = new ActionBag();
			action.setText(text + getCurrentActionText());
			action.setImageFile(imageFinalFile);
			
			repository.saveInfoAction(currentChapter, currentSection, action);
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
	
	private void cleanUpActionsLog() {		
		try{ 
			File actionFile = new File(factory.getConfiguration().getActionsFile());
			if(!actionFile.exists())
				actionFile.createNewFile();
		} catch(IOException exception) { throw new RuntimeException(exception); }
	}
	
	private void setupDirectory(){
		File path = new File(factory.getConfiguration().getTarget());
		if(!path.exists())
			path.mkdirs();		
	}	

}
