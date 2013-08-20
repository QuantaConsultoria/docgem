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
import com.quantaconsultoria.docgem.annotations.Chapter;

@Chapter(id = "Capitulo falha")
public class DocumentationFailureScenarioTest {

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
		config.setChaptersXmlPath(this.getClass()
				.getResource("/examples/chapters.xml").getPath());
		config.setPackagePrefix("com.quantaconsultoria.docgem");
		documentation = new Documentation(driver, config);
	}

	@After
	public void closeDriver() {
		driver.close();
		driver.quit();
	}

	@Test
	public void mustThrowExceptionWhenSectionNotFound() {
		exception.expect(RuntimeException.class);
	    exception.expectMessage("Don't exist a Section on stack");
	    
		WebElement login = action();
		documentation.addAction("Informe o nome do usuário", login);
	}

	private WebElement action() {
		driver.get("file://"
				+ DocumentationTest.class.getResource(
						"/examples/basci_structure.html").getPath());

		WebElement login = driver.findElement(By.id("login"));
		login.sendKeys("Admin");
		return login;
	}
}
