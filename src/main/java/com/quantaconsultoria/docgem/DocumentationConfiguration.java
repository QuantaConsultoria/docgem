package com.quantaconsultoria.docgem;



public class DocumentationConfiguration {

	private String target;
	private String packagePrefix;
	private String chaptersXmlPath;
	private String actionsFile;

	public DocumentationConfiguration() {
		target="target/docgem/";
	}
	
	public DocumentationConfiguration(String target, String packagePrefix,
			String chaptersXmlPath, String actionsFile) {
		super();
		this.target = target;
		this.packagePrefix = packagePrefix;
		this.chaptersXmlPath = chaptersXmlPath;
		this.actionsFile = actionsFile;
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
}
