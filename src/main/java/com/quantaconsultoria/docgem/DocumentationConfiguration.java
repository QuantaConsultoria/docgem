package com.quantaconsultoria.docgem;



public class DocumentationConfiguration {

	private String target;
	private String packagePrefix;
	private String chaptersXmlPath;
	private String actionsFile;
	private String encoding;

	public DocumentationConfiguration() {
		target = "target/docgem/";
		encoding = "UTF8";
	}

	public DocumentationConfiguration(String target, String packagePrefix,
			String chaptersXmlPath, String actionsFile, String encoding) {
		super();
		this.target = target;
		this.packagePrefix = packagePrefix;
		this.chaptersXmlPath = chaptersXmlPath;
		this.actionsFile = actionsFile;
		this.encoding = encoding;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getTarget() {
		return target;
	}

	public String getPackagePrefix() {
		return this.packagePrefix;
	}
	
	public void setPackagePrefix(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}

	public void setChaptersXmlPath(String path) {
		this.chaptersXmlPath = path;		
	}

	public String getChaptersXmlPath() {
		return chaptersXmlPath;
	}

	public void setActionsFile(String actionsFile) {
		this.actionsFile=actionsFile;
	}
	
	public String getActionsFile() {
		return actionsFile;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
