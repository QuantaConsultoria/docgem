package com.quantaconsultoria.docgem.repository.impl;

import static com.quantaconsultoria.docgem.util.FileUtil.close;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.DocumentationBag;
import com.quantaconsultoria.docgem.bags.SectionBag;
import com.quantaconsultoria.docgem.factory.impl.ChapterBagFactory;
import com.quantaconsultoria.docgem.repository.InformationRepository;

public class InformationRepositoryDefault implements InformationRepository {
	
	private DocumentationConfiguration configuration;
	private final FilenameFilter jsFileFilter = new FilenameFilter() {			
		@Override
		public boolean accept(File dir, String name) {
			if(name.lastIndexOf('.')>0) {
				int lastIndex = name.lastIndexOf('.');
            	String extension = name.substring(lastIndex);
            	return extension.equals(".js");
            }
			
			return false;
		}
	};
	
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
		Map<String, ChapterBag> chapters = new HashMap<>();
		Map<String, SectionBag> sections = new HashMap<>();
		
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
		FileReader fileReader = null;
		Gson gson = new Gson(); 
		try {			
			File path = new File(configuration.getPath());
			if(path.isDirectory())
				return readFromDirectory(path, gson);
			else
				return readFromSingleFile(path, fileReader, bufferedReader, gson);					
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Can't read documentation JSON file.",e);
		} finally {
			close(bufferedReader);
			close(fileReader);
		}
	}
	
	private DocumentationBag readFromSingleFile(File file, FileReader fileReader, BufferedReader bufferedReader, Gson gson) 
			throws IOException{
		DocumentationBag documentationBag = new DocumentationBag();
		
		String json = Files.toString(new File(configuration.getPath()), Charsets.UTF_8);
		ChapterBag chapter = gson.fromJson(json, ChapterBag.class);
		List<ChapterBag> chapters = new ArrayList<ChapterBag>();
		chapters.add(chapter);		
		documentationBag.setChapters(chapters);
		
		return documentationBag;
	}
	
	private DocumentationBag readFromDirectory(File directory, Gson gson){
		FileReader fileReader = null;
		BufferedReader bufferedReader = null; 
		
		List<ChapterBag> chapters = new ArrayList<ChapterBag>();		
		File[] jsons = directory.listFiles(jsFileFilter);
		for(File json:jsons){
			try{
				fileReader = new FileReader(json);
				bufferedReader = new BufferedReader(fileReader);
				ChapterBag chapter = gson.fromJson(bufferedReader, ChapterBag.class);
				if(chapter != null)
					chapters.add(chapter);
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Can't read documentation JSON directory.",e);
			} finally {
				close(bufferedReader);
				close(fileReader);
			}
		}
		
		DocumentationBag documentation = new DocumentationBag();
		documentation.setChapters(chapters);		
		return documentation;
	}

}
