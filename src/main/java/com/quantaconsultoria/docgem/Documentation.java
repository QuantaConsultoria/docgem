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
import com.quantaconsultoria.docgem.annotations.Charpter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.CharpterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;
import com.quantaconsultoria.docgem.reflections.ReflectionsUtil;
import com.thoughtworks.xstream.XStream;

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
			List<CharpterBag> sortedCharpters = mergeWithXmlCharptersBag();
			Gson gson = new Gson();			
			String json = gson.toJson(sortedCharpters);
			json = "var charpters = "+json+";";			
			File data_json = new File(configuration.getTarget(),"data.js");			
			FileUtils.writeStringToFile(data_json, json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<CharpterBag> mergeWithXmlCharptersBag() {
		List<CharpterBag> finalCharpters = getCharptersFromXml();
		
		Set<CharpterBag> notUsedCharpters = new HashSet<CharpterBag>();		
		
		for(CharpterBag xmlCharpter : finalCharpters) {
			CharpterBag docCharpter = charpters.get(xmlCharpter.getId());
			if (docCharpter!=null) {
				for(SectionBag docSection : docCharpter.getSections()) {
					SectionBag xmlSection = xmlCharpter.getSection(docSection.getId());
					if (xmlSection!=null) {
						xmlSection.setActions(docSection.getActions());
					} else {
						xmlCharpter.getSections().add(docSection);
					}
				}
			} else {
				notUsedCharpters.add(docCharpter);				
			}
		}
		finalCharpters.addAll(notUsedCharpters);
		return finalCharpters;
	}

	private List<CharpterBag> getCharptersFromXml() {
		try {
			XStream xstream = new XStream();
			xstream.alias("charpters", ArrayList.class);
			xstream.alias("charpter", CharpterBag.class);
			xstream.alias("section", SectionBag.class);
			xstream.alias("action", ActionBag.class);
			
			List<CharpterBag> lista = (List<CharpterBag>)xstream.fromXML(new FileInputStream(new File(configuration.getCharptersXmlPath())));
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
			
			CharpterBag charpter = this.getCurrentChapter();
			SectionBag section = charpter.getSectionBag(this.getCurrentSection());
			section.getActions().add(action);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Section getCurrentSection() {
		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (StackTraceElement element : stackTrace) {
				if (scanner.existCharpter(element)) {
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

	private CharpterBag getCurrentChapter() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement element : stackTrace) {
			if (scanner.existCharpter(element)) {
				Charpter charpter = scanner.getCharpter(element);
				if (!charpters.containsKey(charpter.id())) {
					CharpterBag bag = new CharpterBag();
					bag.setId(charpter.id());
					bag.setSections(new ArrayList<SectionBag>());
					charpters.put(charpter.id(), bag);
				}
				return charpters.get(charpter.id());
			}
		}
		throw new RuntimeException("Don't exist a Charpter on stack");
	}
	

}
