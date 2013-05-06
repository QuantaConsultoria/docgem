package com.quantaconsultoria.docgem;

public class DocumentationConfiguration {

	private String target;

	public DocumentationConfiguration() {
		target="target/docgem/";
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getTarget() {
		return target;
	}

}
