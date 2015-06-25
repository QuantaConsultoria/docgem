package com.quantaconsultoria.docgem.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.format.html.HtmlBuilder;

@Chapter(id = "Capitulo falha")
@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class, FileUtils.class, FileInputStream.class})
public class HtmlBuilderTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private HtmlBuilder builder;
	
	@Before
	public void init() {
		DocumentationConfiguration config = new DocumentationConfiguration();
		builder = new HtmlBuilder(config);
	}
	
	@Test
	public void copyResourcesFail() throws IOException {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can't copy resources files.");
		
		PowerMockito.mockStatic(FileUtils.class);
		PowerMockito.doThrow(new IOException("Can't copy resources files.")).when(FileUtils.class);
		FileUtils.copyInputStreamToFile(Matchers.any(InputStream.class), Matchers.any(File.class));
		
		
		builder.copyResources();
	}
	
	@Test
	public void mustConvertLineTerminatorInTagBr() {
		String text = "Texto com \n quebra de linha";
		String expectedText = "Texto com <br /> quebra de linha";
		
		Assert.assertEquals(expectedText, builder.convertLineTerminator(text));
	}
	
}
