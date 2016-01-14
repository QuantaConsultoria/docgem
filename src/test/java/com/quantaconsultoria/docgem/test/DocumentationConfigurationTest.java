package com.quantaconsultoria.docgem.test;

import org.junit.Assert;
import org.junit.Test;

import com.quantaconsultoria.docgem.DocumentationConfiguration;

public class DocumentationConfigurationTest {
	
	@Test
	public void getAndSetMustWorks() {
		
		DocumentationConfiguration configuration = new DocumentationConfiguration();
		configuration.setEncoding("UTF-8");
		
		Assert.assertEquals("UTF-8", configuration.getEncoding());
		Assert.assertEquals("target/docgem/", configuration.getTarget());
		Assert.assertEquals(".", configuration.getSource());
		
		DocumentationConfiguration configuration2 = new DocumentationConfiguration("/target", "br.com", "/chapters.xml", "/actions.csv", "UTF-8", "src/main/resources");
		
		Assert.assertEquals("/target", configuration2.getTarget());
		Assert.assertEquals("br.com", configuration2.getPackagePrefix());
		Assert.assertEquals("/chapters.xml", configuration2.getPath());
		Assert.assertEquals("/actions.csv", configuration2.getActionsFile());
		Assert.assertEquals("UTF-8", configuration2.getEncoding());
		Assert.assertEquals("src/main/resources", configuration2.getSource());
	}

}
