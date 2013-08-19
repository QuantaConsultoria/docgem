package com.quantaconsultoria.docgem.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junitx.framework.FileAssert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
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

@Chapter(id = "Capitulo 1")
public class DocumentationTest {

	private RemoteWebDriver driver;
	private Documentation documentation;
	private DocumentationConfiguration config;

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
	@Section(id = "Sessão 2")
	public void binaryFileMustBewrtted() throws IOException {
		driver.get("file://"
				+ DocumentationTest.class.getResource(
						"/examples/basci_structure.html").getPath());

		WebElement login = driver.findElement(By.id("login"));
		login.sendKeys("Admin");
		documentation.addAction("Informe o nome do usuário", login);
		
		List<String> lines = FileUtils.readLines(new File(config.getActionsFile()));
		
		String[] parts = lines.get(lines.size()-1).split("\\;");
		
		Assert.assertEquals("Capitulo 1",parts[0]);
		Assert.assertEquals("Sessão 2",parts[1]);
		Assert.assertEquals("Informe o nome do usuário",parts[2]);
		Assert.assertNotNull(parts[3]);
		
		documentation.makeIt();
	}

	@Test
	@Section(id = "Sessão 1")
	public void writeSection() throws IOException {
		driver.get("file://"
				+ DocumentationTest.class.getResource(
						"/examples/basci_structure.html").getPath());

		WebElement login = driver.findElement(By.id("login"));
		login.sendKeys("Admin");
		documentation.addAction("Informe o nome do usuário", login);

		WebElement senha = driver.findElement(By.id("password"));
		senha.sendKeys("Admin");
		documentation.addAction("Informe a senha do usuário", senha);

		WebElement entrar = driver.findElement(By.id("submit"));
		documentation.addAction("Click no botão Submit", entrar);

		documentation.makeIt();

		checkBuildFiles();
	}

	private void checkBuildFiles() throws IOException {
		checkWithExpected("/templates/index.html", "target/site/docgem/index.html");
		checkWithExpected("/templates/style.css", "target/site/docgem/style.css");
		checkWithExpected("/templates/docgem.js", "target/site/docgem/docgem.js");
		checkWithExpected("/templates/jquery.min.js", "target/site/docgem/jquery.min.js");
		checkWithExpected("/templates/jquery.tmpl.js", "target/site/docgem/jquery.tmpl.js");
		
		checkJsonFile();
	}

	private void checkJsonFile() throws IOException {
		Assert.assertTrue(new File("target/site/docgem/data.js").exists());
		
		String jsonObjet = FileUtils.readFileToString(new File("target/site/docgem/data.js"));
		
		Assert.assertTrue(jsonObjet.contains("\"sections\":"));
		Assert.assertTrue(jsonObjet.contains("\"actions\":"));
		
	}

	private void checkWithExpected(String resource, String target) {
		File expexted = new File(this.getClass().getResource(resource)
				.getPath());
		File actual = new File(target);
		FileAssert.assertBinaryEquals(expexted, actual);
	}

}
