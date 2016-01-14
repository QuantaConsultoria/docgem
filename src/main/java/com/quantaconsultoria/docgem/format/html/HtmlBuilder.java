package com.quantaconsultoria.docgem.format.html;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.markdown4j.Markdown4jProcessor;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.quantaconsultoria.docgem.Builder;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.bags.DocumentationBag;


public class HtmlBuilder implements Builder {
	
	private static final String INDEX_HTML = "index.html";
	private static final String STYLE_CSS = "stylesheets/style.css";
	private static final String MAIN_CSS = "stylesheets/main.css";
	private static final String BOOTSTRAP_CSS = "stylesheets/bootstrap.min.css";
	private static final String ANGULAR_RESIZE_CSS = "stylesheets/angular-resizable.min.css";
	private static final String FONTE_300 = "stylesheets/fonts/opensans/300.woff";
	private static final String FONTE_400 = "stylesheets/fonts/opensans/400.woff";
	private static final String FONTE_700 = "stylesheets/fonts/opensans/700.woff";
	private static final String FONTAWESOME = "stylesheets/fonts/fontawesome/fontawesome-webfont.woff";
	private static final String GLYPHICOM_FONT_TTF = "fonts/glyphicons-halflings-regular.ttf";
	private static final String GLYPHICOM_FONT_WOFF = "fonts/glyphicons-halflings-regular.woff";
	private static final String GLYPHICOM_FONT_WOFF2 = "fonts/glyphicons-halflings-regular.woff2";
	private static final String DOCGEM_JS = "docgem.js";
	private static final String JQUERY_JS = "jquery.js";
	private static final String BOOTSTRAP_JS = "bootstrap.min.js";	
	private static final String TREEVIEW_JS = "treeview.js";
	private static final String AUTOCOMPLETE_JS = "bootstrap3-typeahead.min.js";
	private static final String HANDLEBARS_JS = "handlebars.runtime-v4.0.5.js";
	private static final String BLOODHOUND_JS = "bloodhound.js";
	private static final String UNDERSCORE_JS = "underscore-min.js";
	private static final String ANGULARJS = "angularjs/angular.js";
	private static final String ANGULARJS_ROUTE = "angularjs/angular-route.js";
	private static final String ANGULARJS_RESOURCES= "angularjs/angular-resource.js";
	private static final String ANGULARJS_SANITIZE= "angularjs/angular-sanitize.js";
	private static final String ANGULARJS_RESIZABLE= "angularjs/angular-resizable.min.js";
	private static final String NG_INSPECTOR= "ng-inspector.js";
	private static final String ICONE= "images/help_256.png";
	private static final String[] ARQUIVOS = { INDEX_HTML, STYLE_CSS, MAIN_CSS, BOOTSTRAP_CSS,
			FONTE_300, FONTE_400, FONTE_700, GLYPHICOM_FONT_TTF, GLYPHICOM_FONT_WOFF, GLYPHICOM_FONT_WOFF2, FONTAWESOME, DOCGEM_JS, ANGULARJS,
			ANGULARJS_ROUTE, ANGULARJS_RESOURCES, ANGULARJS_SANITIZE, ANGULARJS_RESIZABLE,UNDERSCORE_JS, ANGULAR_RESIZE_CSS,
			ANGULARJS_SANITIZE, NG_INSPECTOR, ICONE, JQUERY_JS, BOOTSTRAP_JS ,TREEVIEW_JS, AUTOCOMPLETE_JS, HANDLEBARS_JS, BLOODHOUND_JS };
	private DocumentationConfiguration configuration;
	private Markdown4jProcessor markdownProcessor;
	
	public HtmlBuilder(DocumentationConfiguration configuration) {
		this.configuration = configuration;
		this.markdownProcessor = new Markdown4jProcessor();
	}

	@Override
	public void saveDocumentationInfo(DocumentationBag documentation) throws IOException {
		Gson gson = new Gson();			
		String json = gson.toJson(documentation);
		File dataJson = new File(configuration.getTarget(),"data.js");		
		System.out.println("Generating data.js file to ".concat(dataJson.getAbsolutePath()));
		FileUtils.writeStringToFile(dataJson, json, configuration.getEncoding());
	}

	@Override
	public void copyResources() {
		File targetDir = new File(configuration.getTarget());
		String templateDir = "/templates/";
		for(String arquivo : ARQUIVOS) {
			try {
				File directoryTemplate = new File(templateDir);
				
				if(!targetDir.exists())
					targetDir.mkdirs();
				
				if(!directoryTemplate.exists())
					directoryTemplate.mkdirs();
				
				FileUtils.copyURLToFile(this.getClass().getResource(templateDir + arquivo), 
										new File(targetDir + File.separator + arquivo));
			} catch (IOException e) {
				throw new RuntimeException(String.format("Can't copy resource file %s.", arquivo),e);
			}
			
		}
		
		File mdDirectory = new File(configuration.getSource());
		if(mdDirectory.exists() && mdDirectory.isDirectory()){
			
			try { FileUtils.copyDirectoryToDirectory(mdDirectory, targetDir); }
			catch(IOException e){ throw new RuntimeException(e); }
		}		

	}

	@Override
	public String generateFileDescription(String path, DocumentationConfiguration configuration) throws IOException {
		if(path != null && !path.isEmpty()) {
			String markdown = Files.toString(new File(configuration.getTarget() + path), Charsets.UTF_8);
			
			String html = markdownProcessor.process(markdown);
			File generatedHtmlFile = new File(String.format("%s/%s.html", configuration.getTarget(), path));
			
			Files.write(html, generatedHtmlFile, Charsets.UTF_8);
			
			return markdown;
		}
		
		return null;
	}

}
