package com.quantaconsultoria.docgem.format.html;

import static com.quantaconsultoria.docgem.util.FileUtil.close;

import java.io.*;

import org.apache.commons.io.FileUtils;
import org.pegdown.PegDownProcessor;

import com.google.gson.Gson;
import com.quantaconsultoria.docgem.Builder;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.bags.DocumentationBag;


public class HtmlBuilder implements Builder {

	private static final String INDEX_HTML = "index.html";
	private static final String STYLE_CSS = "stylesheets/style.css";
	private static final String MAIN_CSS = "stylesheets/main.css";
	private static final String FONTE_300 = "stylesheets/opensans/300.woff";
	private static final String FONTE_400 = "stylesheets/opensans/400.woff";
	private static final String FONTE_700 = "stylesheets/opensans/700.woff";
	private static final String FONTAWESOME = "stylesheets/fontawesome/fontawesome-webfont.woff";
	private static final String DOCGEM_JS = "docgem.js";
	private static final String ANGULARJS = "angularjs/angular.js";
	private static final String ANGULARJS_ROUTE = "angularjs/angular-route.js";
	private static final String ANGULARJS_RESOURCES= "angularjs/angular-resource.js";
	private static final String ANGULARJS_SANITIZE= "angularjs/angular-sanitize.js";
	private static final String NG_INSPECTOR= "ng-inspector.js";
	private static final String ICONE= "images/help_256.png";
	private static final String[] ARQUIVOS = { INDEX_HTML, STYLE_CSS, MAIN_CSS,
			FONTE_300, FONTE_400, FONTE_700, FONTAWESOME, DOCGEM_JS, ANGULARJS,
			ANGULARJS_ROUTE, ANGULARJS_RESOURCES, ANGULARJS_SANITIZE,
			ANGULARJS_SANITIZE, NG_INSPECTOR, ICONE };
	private DocumentationConfiguration configuration;
	private PegDownProcessor pegDownProcessor;


	public HtmlBuilder(DocumentationConfiguration configuration) {
		this.configuration = configuration;
		pegDownProcessor = new PegDownProcessor();
	}

	@Override
	public void saveDocumentationInfo(DocumentationBag documentation) throws IOException {
		Gson gson = new Gson();			
		String json = gson.toJson(documentation);
		File data_json = new File(configuration.getTarget(),"data.js");			
		FileUtils.writeStringToFile(data_json, json, configuration.getEncoding());
	}

	@Override
	public void copyResources() {
		File targetDir = new File(configuration.getTarget());
		String templateDir = "/templates/";
		for(String arquivo : ARQUIVOS) {
			InputStream inputStream = null;
			try {
				inputStream = this.getClass().getResourceAsStream(templateDir + arquivo);
				FileUtils.copyInputStreamToFile(inputStream, new File(targetDir, arquivo));
			} catch (IOException e) {
				throw new RuntimeException(String.format("Can't copy resource file %s.", arquivo),e);
			} finally {
				close(inputStream);
			}
			
		}
		
		/*InputStream index = null;
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
		}*/
	}

	@Override
	public void generateFileDescription(String path, DocumentationConfiguration configuration) {
		if(path != null && !path.isEmpty()) {
			char[] markdown = org.parboiled.common.FileUtils.readAllCharsFromResource(String.format("%s/%.md", configuration.getTarget(), path));
			String html = pegDownProcessor.markdownToHtml(markdown);
			PrintWriter out = null;
			try {
				out = new PrintWriter(String.format("%s/%s.html", configuration.getTarget(), path));
				out.write(html);
			} catch (FileNotFoundException e) {
				close(out);
			}	
		}
	}

}
