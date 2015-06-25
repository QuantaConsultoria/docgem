package com.quantaconsultoria.docgem.format.html;

import static com.quantaconsultoria.docgem.util.FileUtil.close;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.quantaconsultoria.docgem.Builder;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;


public class HtmlBuilder implements Builder {

	private static final String INDEX_HTML = "index.html";
	private static final String STYLE_CSS = "style.css";
	private static final String DOCGEM_JS = "docgem.js";
	private static final String JQUERY_JS = "jquery.min.js";
	private static final String JQUERY_TEMPLATES= "jquery.template.js";
	private static final String BOOTSTRAP_JS= "bootstrap/js/bootstrap.min.js";
	private static final String BOOTSTRAP_CSS= "bootstrap/css/bootstrap.min.css";
	private static final String ICONE= "bootstrap/images/help_256.png";
	private DocumentationConfiguration configuration;


	public HtmlBuilder(DocumentationConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void saveDocumentationInfo(List<ChapterBag> chapters) throws IOException {
		convertText(chapters);
		Gson gson = new Gson();			
		String json = gson.toJson(chapters);
		json = "var chapters = "+json+";";	
		File data_json = new File(configuration.getTarget(),"data.js");			
		FileUtils.writeStringToFile(data_json, json, configuration.getEncoding());
	}

	public void convertText(List<ChapterBag> chapters) {
		for(ChapterBag chapter : chapters) {
			chapter.setText(convertLineTerminator(chapter.getText()));
			if(chapter.getSections() != null) {
				for(SectionBag section : chapter.getSections()) {
					section.setText(convertLineTerminator(section.getText()));
				}
			}
		}
		
	}

	public String convertLineTerminator(String text) {
		if(text != null) {
			return text.replaceAll("(\\r\\n|\\n)", "<br />");
		}
		return null;
	}

	@Override
	public void copyResources() {
		File targetDir = new File(configuration.getTarget());
		InputStream index = null;
		InputStream style = null;
		InputStream docgem_js = null;
		InputStream jquery = null;
		InputStream jqueryTmpl = null;
		InputStream bootstrapJs = null;
		InputStream bootstrapCss = null;
		InputStream icone = null;
		String templateDir = "/templates/";

		try {
			index = this.getClass().getResourceAsStream(templateDir+INDEX_HTML);
			style = this.getClass().getResourceAsStream(templateDir+STYLE_CSS);
			docgem_js = this.getClass().getResourceAsStream(templateDir+DOCGEM_JS);
			jquery = this.getClass().getResourceAsStream(templateDir+JQUERY_JS);
			jqueryTmpl = this.getClass().getResourceAsStream(templateDir+JQUERY_TEMPLATES);
			bootstrapJs = this.getClass().getResourceAsStream(templateDir+BOOTSTRAP_JS);
			bootstrapCss = this.getClass().getResourceAsStream(templateDir+BOOTSTRAP_CSS);
			icone = this.getClass().getResourceAsStream(templateDir+ICONE);


			FileUtils.copyInputStreamToFile(index, new File(targetDir,INDEX_HTML));
			FileUtils.copyInputStreamToFile(style, new File(targetDir, STYLE_CSS));
			FileUtils.copyInputStreamToFile(docgem_js, new File(targetDir, DOCGEM_JS));
			FileUtils.copyInputStreamToFile(jquery, new File(targetDir, JQUERY_JS));
			FileUtils.copyInputStreamToFile(jqueryTmpl, new File(targetDir, JQUERY_TEMPLATES));
			FileUtils.copyInputStreamToFile(bootstrapJs, new File(targetDir, BOOTSTRAP_JS));
			FileUtils.copyInputStreamToFile(bootstrapCss, new File(targetDir, BOOTSTRAP_CSS));
			FileUtils.copyInputStreamToFile(icone, new File(targetDir, ICONE));
		} catch (IOException e) {
			throw new RuntimeException("Can't copy resources files.",e);
		} finally {
			close(index);
			close(style);
			close(docgem_js);
			close(jquery);
			close(jqueryTmpl);
			close(icone);
		}
	}

}
