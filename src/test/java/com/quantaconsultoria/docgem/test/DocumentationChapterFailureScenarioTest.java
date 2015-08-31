package com.quantaconsultoria.docgem.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.quantaconsultoria.docgem.Documentation;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.factory.Factory;
import com.quantaconsultoria.docgem.factory.impl.FactoryDefault;

public class DocumentationChapterFailureScenarioTest {

	private RemoteWebDriver driver;
	private Documentation documentation;
	private DocumentationConfiguration config;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void startTestStuffs() {
		driver = new FirefoxDriver();
		driver.manage().window().setSize(new Dimension(800, 600));
		config = new DocumentationConfiguration();
		config.setTarget("target/site/docgem/");
		config.setActionsFile("target/site/docgem/action.csv");
		config.setDocumentationFile(this.getClass()
				.getResource("/examples/chapters.xml").getPath());
		config.setPackagePrefix("com.quantaconsultoria.docgem");
		Factory factory = new FactoryDefault(config);
		documentation = new Documentation(driver, factory);
	}

	@After
	public void closeDriver() {
		driver.close();
		driver.quit();
	}

	@Test
	@Section(id="fail test")
	public void mustThrowExceptionWhenChapterNotFound() {
		exception.expect(RuntimeException.class);
	    exception.expectMessage("Don't exist a Chapter on stack");
	    
		driver.get("file://"
				+ DocumentationTest.class.getResource(
						"/examples/basci_structure.html").getPath());

		WebElement login = driver.findElement(By.id("login"));
		login.sendKeys("Admin");
		documentation.addAction("Informe o nome do usuário", login);
	}
}
