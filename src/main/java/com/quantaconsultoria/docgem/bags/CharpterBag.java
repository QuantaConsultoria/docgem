package com.quantaconsultoria.docgem.bags;

import java.util.List;

public class CharpterBag {
	private String id;
	private List<SectionBag> sections;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<SectionBag> getSections() {
		return sections;
	}
	public void setSections(List<SectionBag> sections) {
		this.sections = sections;
	}
	
	
}
