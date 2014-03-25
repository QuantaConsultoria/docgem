package com.quantaconsultoria.docgem.test;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.repository.InformationRepository;
import com.quantaconsultoria.docgem.repository.impl.InformationRepositoryDefault;

public class InformationRepositoryDefaultTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
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
		InformationRepository repository = new InformationRepositoryDefault(config);
		repository.readSortedChapters();
	}

}
