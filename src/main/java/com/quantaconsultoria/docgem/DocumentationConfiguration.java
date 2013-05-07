package com.quantaconsultoria.docgem;

import java.net.URL;

public class DocumentationConfiguration {

	private String target;
	private String packagePrefix;
	private String charptersXmlPath;

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

	public void setCharptersXmlPath(String path) {
		this.charptersXmlPath = path;		
	}

	public String getCharptersXmlPath() {
		return charptersXmlPath;
	}
}
