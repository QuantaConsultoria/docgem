package com.quantaconsultoria.docgem.repository.impl;

import static com.quantaconsultoria.docgem.util.FileUtil.close;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;
import com.quantaconsultoria.docgem.factory.impl.ChapterBagFactory;
import com.quantaconsultoria.docgem.repository.InformationRepository;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
	@SuppressWarnings("unchecked")
	public List<ChapterBag> readSortedChapters() throws IOException {
		InputStream xmlChapters = null;
		try {
			XStream xstream = new XStream(new DomDriver(configuration.getEncoding()));
			xstream.alias("chapters", ArrayList.class);
			xstream.alias("chapter", ChapterBag.class);
			xstream.alias("section", SectionBag.class);
			xstream.alias("action", ActionBag.class);
			
			xmlChapters = new FileInputStream(new File(configuration.getChaptersXmlPath()));
			List<ChapterBag> lista = (List<ChapterBag>)xstream.fromXML(xmlChapters);
			return lista;
			
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Can't read XML file.",e);
		} finally {
			close(xmlChapters);
		}
	}

}
