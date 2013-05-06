package com.quantaconsultoria.docgem;

import java.io.InputStream;
import java.lang.reflect.Proxy;

import javax.xml.ws.ServiceMode;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Documentation {

	private RemoteWebDriver driver;
	private DocumentationConfiguration configuration;
	
	public Documentation(RemoteWebDriver driver, DocumentationConfiguration config) {
		super();
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
		throw new NotImplementedException();
	}

	public void takeScreenshot(WebElement element) {
		// TODO Auto-generated method stub

	}

	public void addAction(String string, WebElement element) {
		// TODO Auto-generated method stub

	}

}
