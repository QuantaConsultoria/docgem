package com.quantaconsultoria.docgem.test;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.quantaconsultoria.docgem.Action;
import com.quantaconsultoria.docgem.Charpter;
import com.quantaconsultoria.docgem.Documentation;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.Section;

@Charpter(id="Captulo 1")
public class DocumentationTest {
	
	RemoteWebDriver driver;
	Documentation documentation;
	
	@Before
	public void prepararTest(){
		driver = new FirefoxDriver();
		DocumentationConfiguration config = new DocumentationConfiguration();
		documentation = new Documentation(driver, config);
	}
	
	@Test
	@Section(id="Sessão 1")
	public void escreverSessao() {
		driver.get(DocumentationTest.class.getResource("/templates/basci_structure.html").getPath());
		WebElement name = driver.findElement(By.id("name"));
		documentation.addAction("Passo 1", name);
		documentation.makeIt();
		
		
	}
	
}
