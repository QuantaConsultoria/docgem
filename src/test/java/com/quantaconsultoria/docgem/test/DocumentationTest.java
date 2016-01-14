package com.quantaconsultoria.docgem.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junitx.framework.FileAssert;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.quantaconsultoria.docgem.Documentation;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.factory.Factory;
import com.quantaconsultoria.docgem.factory.impl.FactoryDefault;

@Chapter(id = "programa")
public class DocumentationTest {
	
	
	private RemoteWebDriver driver;
	private Documentation documentation;
	private DocumentationConfiguration config;
	private final String EXAMPLE_PATH = this.getClass()
											.getResource(File.separator + "examples" + 
														 File.separator).getPath();
	
	@Before
	public void startTestStuffs() throws IOException {
		driver = new FirefoxDriver();
		driver.manage().window().setSize(new Dimension(800, 600));
		config = new DocumentationConfiguration();
		config.setTarget(EXAMPLE_PATH);
		config.setActionsFile("action.csv");
		config.setPath(this.getClass()
				.getResource("/examples/test.js").getPath());
		config.setPackagePrefix("com.quantaconsultoria.docgem");
		
		Factory factory = new FactoryDefault(config);		
		documentation = new Documentation(driver, factory);
	}

	@After
	public void closeDriver() {
		driver.close();
		driver.quit();
	}

	@Ignore
	@Test
	@Section(id = "criando_categoria_de_investimento_filha")
	public void binaryFileMustBewrtted() throws IOException {
		driver.get("file://"
				+ DocumentationTest.class.getResource(
						"/examples/basic_structure.html").getPath());

		WebElement login = driver.findElement(By.id("login"));
		login.sendKeys("Admin");
		documentation.addAction("Informe o nome do usuário", login);
		
		List<String> lines = FileUtils.readLines(new File(config.getActionsFile()),"UTF8");
		
		String[] parts = lines.get(lines.size()-1).split("\\;");
		
		Assert.assertEquals("programa",parts[0]);
		Assert.assertEquals("criando_categoria_de_investimento_filha",parts[1]);
		Assert.assertEquals("Informe o nome do usuário",parts[2]);
		Assert.assertNotNull(parts[3]);
		
		documentation.makeIt();
	}

	@Ignore
	@Test
	@Section(id = "criando_categoria_de_investimento_pai")
	public void writeSection() throws IOException {
		driver.get("file://"
				+ DocumentationTest.class.getResource(
						"/examples/basic_structure.html").getPath());

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

	@Test
	public void shouldGenerateDocumentation(){
		
	}
	
	private void checkBuildFiles() throws IOException {
		checkWithExpected("/templates/index.html", EXAMPLE_PATH + "index.html");
		checkWithExpected("/templates/stylesheets/style.css", EXAMPLE_PATH + "stylesheets/style.css");
		checkWithExpected("/templates/stylesheets/main.css", EXAMPLE_PATH + "stylesheets/main.css");
		checkWithExpected("/templates/stylesheets/fonts/opensans/300.woff", EXAMPLE_PATH + "stylesheets/fonts/opensans/300.woff");
		checkWithExpected("/templates/stylesheets/fonts/opensans/400.woff", EXAMPLE_PATH + "stylesheets/fonts/opensans/400.woff");
		checkWithExpected("/templates/stylesheets/fonts/opensans/700.woff", EXAMPLE_PATH + "stylesheets/fonts/opensans/700.woff");
		checkWithExpected("/templates/stylesheets/fonts/fontawesome/fontawesome-webfont.woff", EXAMPLE_PATH + "stylesheets/fonts/fontawesome/fontawesome-webfont.woff");
		checkWithExpected("/templates/docgem.js", EXAMPLE_PATH + "docgem.js");
		checkWithExpected("/templates/angularjs/angular.js", EXAMPLE_PATH + "angularjs/angular.js");
		checkWithExpected("/templates/angularjs/angular-route.js", EXAMPLE_PATH + "angularjs/angular-route.js");
		checkWithExpected("/templates/angularjs/angular-resource.js", EXAMPLE_PATH + "angularjs/angular-resource.js");
		checkWithExpected("/templates/angularjs/angular-sanitize.js", EXAMPLE_PATH + "angularjs/angular-sanitize.js");
		checkWithExpected("/templates/ng-inspector.js", EXAMPLE_PATH + "ng-inspector.js");
		checkWithExpected("/templates/images/help_256.png", EXAMPLE_PATH + "images/help_256.png");
		
		//checkJsonFile();
	}

	private void checkJsonFile() throws IOException {
		Assert.assertTrue(new File("data.js").exists());
		
		String jsonObject = FileUtils.readFileToString(new File("data.js"));
		
		Assert.assertTrue(jsonObject.contains("\"sections\":"));
		Assert.assertTrue(jsonObject.contains("\"actions\":"));
		
	}

	private void checkWithExpected(String resource, String target) {
		File expexted = new File(this.getClass().getResource(resource).getPath());
		File actual = new File(target);
		FileAssert.assertBinaryEquals(expexted, actual);
	}
	
}
