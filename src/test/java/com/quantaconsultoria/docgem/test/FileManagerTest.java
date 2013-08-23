package com.quantaconsultoria.docgem.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
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
import com.quantaconsultoria.docgem.FileManager;
import com.quantaconsultoria.docgem.annotations.Chapter;

@Chapter(id = "Capitulo falha")
@RunWith(PowerMockRunner.class)
@PrepareForTest(FileUtils.class)
public class FileManagerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void startTestStuffs() throws IOException {
		PowerMockito.mockStatic(FileUtils.class);
		PowerMockito.doThrow(new IOException("Can't copy resources files.")).when(FileUtils.class);
		FileUtils.copyInputStreamToFile(Matchers.any(InputStream.class), Matchers.any(File.class));
	}
	
	@Test
	public void copyResourcesFail() {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can't copy resources files.");
		
		DocumentationConfiguration config = new DocumentationConfiguration();
		FileManager fileManager = new FileManager(config);
		fileManager.copyResources();
	}
}
