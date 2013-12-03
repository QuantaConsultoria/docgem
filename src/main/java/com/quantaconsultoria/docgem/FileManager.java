package com.quantaconsultoria.docgem;

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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class FileManager {
	
	private static final Logger LOG = Logger.getLogger(FileManager.class.getName());
	private static final String INDEX_HTML = "index.html";
	private static final String STYLE_CSS = "style.css";
	private static final String DOCGEM_JS = "docgem.js";
	private static final String JQUERY_JS = "jquery.min.js";
	private static final String JQUERY_TEMPLATES= "jquery.template.js";
	private static final String BOOTSTRAP_JS= "bootstrap/js/bootstrap.min.js";
	private static final String BOOTSTRAP_CSS= "bootstrap/css/bootstrap.min.css";
	private DocumentationConfiguration configuration;
	
	
	
	public FileManager(DocumentationConfiguration configuration) {
		this.configuration = configuration;
	}

	public String saveScreenshot(File srcImage) throws IOException {
		Long id =  (long) (Math.random()*1000000000);
		String destFilePath = "images/"+id+".png"; 
		File destFile = new File(configuration.getTarget(), destFilePath);
		
		if (!destFile.exists()) {
			FileUtils.copyFile(srcImage, destFile);
			return destFilePath;
		} else {
			return saveScreenshot(srcImage);
		}
	}
	
	public void saveJson(String json) throws IOException {
		File data_json = new File(configuration.getTarget(),"data.js");			
		FileUtils.writeStringToFile(data_json, json, configuration.getEncoding());
	}
	
	public void copyResources() {
		File targetDir = new File(configuration.getTarget());
		InputStream index = null;
		InputStream style = null;
		InputStream docgem_js = null;
		InputStream jquery = null;
		InputStream jqueryTmpl = null;
		InputStream bootstrapJs = null;
		InputStream bootstrapCss = null;
		String templateDir = "/templates/";
		
		try {
			index = this.getClass().getResourceAsStream(templateDir+INDEX_HTML);
			style = this.getClass().getResourceAsStream(templateDir+STYLE_CSS);
			docgem_js = this.getClass().getResourceAsStream(templateDir+DOCGEM_JS);
			jquery = this.getClass().getResourceAsStream(templateDir+JQUERY_JS);
			jqueryTmpl = this.getClass().getResourceAsStream(templateDir+JQUERY_TEMPLATES);
			bootstrapJs = this.getClass().getResourceAsStream(templateDir+BOOTSTRAP_JS);
			bootstrapCss = this.getClass().getResourceAsStream(templateDir+BOOTSTRAP_CSS);
			
			
			FileUtils.copyInputStreamToFile(index, new File(targetDir,INDEX_HTML));
			FileUtils.copyInputStreamToFile(style, new File(targetDir, STYLE_CSS));
			FileUtils.copyInputStreamToFile(docgem_js, new File(targetDir, DOCGEM_JS));
			FileUtils.copyInputStreamToFile(jquery, new File(targetDir, JQUERY_JS));
			FileUtils.copyInputStreamToFile(jqueryTmpl, new File(targetDir, JQUERY_TEMPLATES));
			FileUtils.copyInputStreamToFile(bootstrapJs, new File(targetDir, BOOTSTRAP_JS));
			FileUtils.copyInputStreamToFile(bootstrapCss, new File(targetDir, BOOTSTRAP_CSS));
		} catch (IOException e) {
			throw new RuntimeException("Can't copy resources files.",e);
		} finally {
			close(index);
			close(style);
			close(docgem_js);
			close(jquery);
			close(jqueryTmpl);
		}
	}
	
	public void close(FileOutputStream out) throws IOException {
		if(out != null) {
			out.close();
		}
	}

	public void close(FileLock lock) throws IOException {
		if(lock != null) {
			lock.release();
			lock.close();
		}
	}
	
	public void close(InputStream stream) {
		try {
			if(stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Can't close the InputStream.");
		}
	}
	
	public void writeInfoAction(Chapter chapter, Section section, ActionBag action) throws IOException {
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
	
	public List<ChapterBag> readChapterssXml() throws IOException {
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
	
	public Map<String, ChapterBag> readActionsFile() throws IOException {
		Map<String, ChapterBag> chapters= new HashMap<>();
		
		List<String> lines = FileUtils.readLines(new File(configuration.getActionsFile()), configuration.getEncoding());
		for(String line : lines) {
			String[] parts = line.split("\\;");
			
			if(chapters.containsKey(parts[0])) {
				ChapterBag chapterBag = chapters.get(parts[0]);
				updateChapterInfo(chapterBag,parts);
			} else {
				ChapterBag chapterBag = createChapterFromStringArray(parts);
				chapters.put(chapterBag.getId(), chapterBag);
			}
		}
		
		return chapters;
	}
	
	private void updateChapterInfo(ChapterBag chapterBag, String[] parts) {
		SectionBag sectionBag = chapterBag.getSection(parts[1]); 
		if(sectionBag != null){
			sectionBag.getActions().add(createActionFomArrayInfo(parts));
		} else {
			chapterBag.getSections().add(createSectionFromArrayInfo(parts));
		}
	}
	
	private SectionBag createSectionFromArrayInfo(String[] parts) {
		SectionBag sectionBag = new SectionBag();
		sectionBag.setId(parts[1]);
		sectionBag.setActions(new ArrayList<ActionBag>());
		sectionBag.getActions().add(createActionFomArrayInfo(parts));
		
		return sectionBag;
	}
	
	private ActionBag createActionFomArrayInfo(String[] parts) {
		return new ActionBag(parts[2], parts[3]);
	}

	private ChapterBag createChapterFromStringArray(String[] parts) {
		ChapterBag chapterBag = new ChapterBag();
		chapterBag.setId(parts[0]);
		chapterBag.setSections(new ArrayList<SectionBag>());
		
		SectionBag sectionBag = createSectionFromArrayInfo(parts);
		chapterBag.getSections().add(sectionBag);
		
		return chapterBag;
	}

}
