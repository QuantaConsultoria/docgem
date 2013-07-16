package com.quantaconsultoria.docgem;


public class DocumentationConfiguration {

	private String target;
	private String packagePrefix;
	private String chaptersXmlPath;

	public DocumentationConfiguration() {
		target="target/docgem/";
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
}
