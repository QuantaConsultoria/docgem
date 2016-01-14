package com.quantaconsultoria.docgem;



public class DocumentationConfiguration {

	private String target;
	private String source;
	private String packagePrefix;
	private String path;
	private String actionsFile;
	private String encoding;

	public DocumentationConfiguration() {
		target = "target/docgem/";
		encoding = "UTF-8";
		source = ".";
	}

	public DocumentationConfiguration(String target, String packagePrefix,
			String path, String actionsFile, String encoding, String source) {
		super();
		this.target = target;
		this.packagePrefix = packagePrefix;
		this.path = path;
		this.actionsFile = actionsFile;
		this.encoding = encoding;
		this.source = source;
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

	public void setPath(String path) {
		this.path = path;		
	}

	public String getPath() {
		return path;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
