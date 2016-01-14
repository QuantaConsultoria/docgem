package com.quantaconsultoria.docgem.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.bags.DocumentationBag;
import com.quantaconsultoria.docgem.repository.InformationRepository;
import com.quantaconsultoria.docgem.repository.impl.InformationRepositoryDefault;
import com.sun.jna.platform.win32.WinNT.FILE_NOTIFY_INFORMATION;


public class InformationRepositoryDefaultTest {
	
	private final String EXAMPLE_PATH = this.getClass()
											.getResource(File.separator + "examples" + 
														 File.separator).getPath();
	private final Integer JSON_FILES_LENGTH = 2;
	private final String JSON_PATH = EXAMPLE_PATH + "chapters" + File.separator;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void readChaptersJsonFail() throws Exception {
		exception.expect(RuntimeException.class);
		exception.expectMessage("Can't read documentation JSON file.");
		
		File file = Mockito.mock(File.class);
		PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
		
		FileInputStream inputStream = Mockito.mock(FileInputStream.class);
		PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStream);
		
		DocumentationConfiguration config = new DocumentationConfiguration();
		config.setPath("");
		InformationRepository repository = new InformationRepositoryDefault(config);
		repository.readDocumentationInfo();
		
	}
	
	@Test
	public void shouldReadFromASingleFile() throws Exception{
		File file = Mockito.mock(File.class);
		PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
		
		FileInputStream inputStream = Mockito.mock(FileInputStream.class);
		PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStream);
		DocumentationConfiguration config = new DocumentationConfiguration();
		config.setPath(EXAMPLE_PATH + "2_Monitoramento.js");
		
		InformationRepository repository = new InformationRepositoryDefault(config);
		DocumentationBag documentationBag = repository.readDocumentationInfo();
		
		assertThat(documentationBag.getChapters().size(), is(1));
		assertThat(documentationBag.getChapters().get(0).getId(), is("monitoramento"));		
	}
	
	@Test
	public void shouldReadFromDirectory() throws Exception{
		File file = Mockito.mock(File.class);
		PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(file);
		
		FileInputStream inputStream = Mockito.mock(FileInputStream.class);
		PowerMockito.whenNew(FileInputStream.class).withAnyArguments().thenReturn(inputStream);
		DocumentationConfiguration config = new DocumentationConfiguration();
		config.setPath(JSON_PATH);
		
		InformationRepository repository = new InformationRepositoryDefault(config);
		DocumentationBag documentationBag = repository.readDocumentationInfo();
			
		assertThat(documentationBag.getChapters().size(), is(JSON_FILES_LENGTH));
	}
	
	

}
