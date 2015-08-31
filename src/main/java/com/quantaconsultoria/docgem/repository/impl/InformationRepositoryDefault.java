package com.quantaconsultoria.docgem.repository.impl;

import static com.quantaconsultoria.docgem.util.FileUtil.close;

import java.io.*;
import java.nio.channels.FileLock;
import java.util.*;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.*;
import com.quantaconsultoria.docgem.factory.impl.ChapterBagFactory;
import com.quantaconsultoria.docgem.repository.InformationRepository;

public class InformationRepositoryDefault implements InformationRepository {
	
	private DocumentationConfiguration configuration;
	
	
	public InformationRepositoryDefault(DocumentationConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void saveInfoAction(Chapter chapter, Section section, ActionBag action) throws IOException {
		File file = new File(configuration.getActionsFile());
		
		if (!file.exists() && !file.createNewFile()) {
			throw new RuntimeException("Can't create the actions file.");
		}
		FileLock lock = null;
		FileOutputStream out = null;
		try {
			
			out = new FileOutputStream(file,true);
			lock = out.getChannel().lock();
			
			String line = String.format("%s;%s;%s;%s%n", chapter.id(),section.id(),action.getText(),action.getImageFile());
			
			out.write(line.getBytes(configuration.getEncoding()));
			
		} finally {
			close(lock);
			close(out);
		}		
		
	}
	
	@Override
	public Map<String, ChapterBag> readActions() throws IOException {
		Map<String, ChapterBag> chapters= new HashMap<>();
		
		List<String> lines = FileUtils.readLines(new File(configuration.getActionsFile()), configuration.getEncoding());
		for(String line : lines) {
			String[] parts = line.split("\\;");
			
			if(chapters.containsKey(parts[0])) {
				ChapterBag chapterBag = chapters.get(parts[0]);
				ChapterBagFactory.updateChapterInfo(chapterBag,parts);
			} else {
				ChapterBag chapterBag = ChapterBagFactory.createChapterFromStringArray(parts);
				chapters.put(chapterBag.getId(), chapterBag);
			}
		}
		
		return chapters;
	}
	
	@Override
	public DocumentationBag readDocumentationInfo() throws IOException {
		BufferedReader bufferedReader = null;
		FileReader fielFileReader = null;
		Gson gson = new Gson(); 
		try {
			fielFileReader =  new FileReader(configuration.getDocumentationFile());
			bufferedReader = new BufferedReader(fielFileReader);
			return gson.fromJson(bufferedReader, DocumentationBag.class);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Can't read documentation JSON file.",e);
		} finally {
			close(bufferedReader);
			close(fielFileReader);
		}
	}

}
