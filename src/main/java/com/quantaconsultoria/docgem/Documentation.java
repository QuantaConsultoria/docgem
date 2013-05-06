package com.quantaconsultoria.docgem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.quantaconsultoria.docgem.annotations.Action;
import com.quantaconsultoria.docgem.annotations.Charpter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.CharpterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Documentation {

	private RemoteWebDriver driver;
	private DocumentationConfiguration configuration;
	private long imageCount = 1;
	private DocumentationScanner scanner;
	
	private Map<String, CharpterBag> charpters;
	private Map<String, SectionBag> sections;
	private Map<String, ActionBag> actions;
	
	protected Documentation() {
		charpters = new HashMap<String, CharpterBag>();
		sections = new HashMap<String, SectionBag>();
		actions = new HashMap<String, ActionBag>();
		scanner = new DocumentationScanner();
		scanner.scan();
	}
	
	public Documentation(RemoteWebDriver driver, DocumentationConfiguration config) {
		this();
		this.driver = driver;
		this.configuration = config;
	}

	public Section createSection(String id) {
		throw new NotImplementedException();
	}

	public Charpter createCharpter(String id) {
		throw new NotImplementedException();
	}

	public void loadConfiguration(InputStream file) {
		throw new NotImplementedException();
	}

	public void writeText(String text) {
		throw new NotImplementedException();
	}

	public void makeIt() {
		copyResources();
	}

	private void copyResources() {
		File targetDir = new File(configuration.getTarget());
		File index = new File(this.getClass().getResource("/templates/index.html").getPath());
		File style = new File(this.getClass().getResource("/templates/style.css").getPath());
		
		try {
			FileUtils.copyFileToDirectory(index, targetDir);
			FileUtils.copyFileToDirectory(style, targetDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void takeScreenshot(WebElement element) {
		throw new NotImplementedException();
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
			
			Charpter charpter = this.getCurrentChapter();
			Section section = this.getCurrentSection(charpter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Section getCurrentSection(Charpter charpter) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			if (scanner.existSection(element,charpter)) {
				return scanner.getSection(element,charpter);
			}
		}
		throw new RuntimeException("Don't exist a Charpter on stack");
	}

	private Charpter getCurrentChapter() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			if (scanner.existCharpter(element)) {
				return scanner.getCharpter(element);
			}
		}
		throw new RuntimeException("Don't exist a Charpter on stack");
	}
	

}
