package com.quantaconsultoria.docgem.test;

import java.io.File;

import junitx.framework.FileAssert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.quantaconsultoria.docgem.Documentation;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;

@Chapter(id="Capitulo 1")
public class DocumentationTest {
	
	RemoteWebDriver driver;
	Documentation documentation;
	
	@Before
	public void startTestStuffs(){
		driver = new FirefoxDriver();
		driver.manage().window().setSize(new Dimension(800, 600));
		DocumentationConfiguration config = new DocumentationConfiguration();
		config.setTarget("target/site/docgem/");
		config.setChaptersXmlPath(this.getClass().getResource("/examples/chapters.xml").getPath());
		config.setPackagePrefix("com.quantaconsultoria.docgem");
		documentation = new Documentation(driver, config);
	}
	
	@After
	public void closeDriver() {
		driver.close();
		driver.quit();
	}
	
	@Test
	@Section(id="Sessão 1")
	public void writeSection() {
		driver.get("file://"+DocumentationTest.class.getResource("/examples/basci_structure.html").getPath());
		WebElement name = driver.findElement(By.id("name"));
		documentation.addAction("Passo 1", name);
		documentation.makeIt();
		
		checkBuildFiles();
	}

	private void checkBuildFiles() {
		checkWithExpected("/templates/index.html","target/site/docgem/index.html");
		checkWithExpected("/templates/style.css","target/site/docgem/style.css");
		checkWithExpected("/templates/docgem.js","target/site/docgem/docgem.js");
		checkWithExpected("/examples/expected_json.js","target/site/docgem/data.js");
	}

	private void checkWithExpected(String resource, String target) {
		File expexted = new File(this.getClass().getResource(resource).getPath());
		File actual = new File(target);
		FileAssert.assertBinaryEquals(expexted, actual);
	}
	
}
