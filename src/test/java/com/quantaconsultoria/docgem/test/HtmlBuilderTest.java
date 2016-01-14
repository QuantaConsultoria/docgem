package com.quantaconsultoria.docgem.test;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.format.html.HtmlBuilder;

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
	@Ignore
	public void copyResourcesFail() throws IOException {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can't copy resource file index.html");
		
		PowerMockito.mockStatic(FileUtils.class);
		PowerMockito.doThrow(new IOException("Can't copy resources files.")).when(FileUtils.class);
		FileUtils.copyInputStreamToFile(Matchers.any(InputStream.class), Matchers.any(File.class));
		
		
		builder.copyResources();
	}
	
}
