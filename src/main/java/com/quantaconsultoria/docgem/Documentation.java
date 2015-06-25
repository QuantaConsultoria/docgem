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

import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;
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
		scanner = new DocumentationScanner(factory.getConfiguration());
		scanner.scan();
		builder = factory.getBuilder();
		repository = factory.getRepository();
		this.factory = factory;
	}

	public void makeIt() throws IOException {
		builder.copyResources();
		buildJson();
	}

	private void buildJson() {
		try { 
			List<ChapterBag> sortedChapters = mergeWithXmlChaptersBag();
			builder.saveDocumentationInfo(sortedChapters);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<ChapterBag> mergeWithXmlChaptersBag() throws IOException {
		List<ChapterBag> finalChapters = repository.readSortedChapters();
		Map<String, ChapterBag> chapters = repository.readActions();
		
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
			
			ImageUtil.highlightElement(imageFile, element);
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

}
