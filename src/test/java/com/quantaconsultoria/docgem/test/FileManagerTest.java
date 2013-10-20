package com.quantaconsultoria.docgem.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.FileManager;
import com.quantaconsultoria.docgem.annotations.Chapter;

@Chapter(id = "Capitulo falha")
@RunWith(PowerMockRunner.class)
@PrepareForTest({File.class, FileUtils.class, FileInputStream.class})
public class FileManagerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void copyResourcesFail() throws IOException {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can't copy resources files.");
		
		PowerMockito.mockStatic(FileUtils.class);
		PowerMockito.doThrow(new IOException("Can't copy resources files.")).when(FileUtils.class);
		FileUtils.copyInputStreamToFile(Matchers.any(InputStream.class), Matchers.any(File.class));
		
		DocumentationConfiguration config = new DocumentationConfiguration();
		FileManager fileManager = new FileManager(config);
		fileManager.copyResources();
	}
	
	@Test
	public void readChapterssXmlFail() throws Exception {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can't read XML file.");
		
		File file = Mockito.mock(File.class);
		PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
		
		FileInputStream inputStream = Mockito.mock(FileInputStream.class);
		PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStream);
		
		DocumentationConfiguration config = new DocumentationConfiguration();
		config.setChaptersXmlPath("");
		FileManager fileManager = new FileManager(config);
		fileManager.readChapterssXml();
	}
}
