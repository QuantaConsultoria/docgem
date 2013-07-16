package com.quantaconsultoria.docgem.bags;

import java.util.ArrayList;
import java.util.List;

import com.quantaconsultoria.docgem.annotations.Section;

public class ChapterBag {
	
	private String id;
	private String text;
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
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public SectionBag getSectionBag(Section currentSection) {
		for(SectionBag section: sections) {
			if (section.getId().equals(currentSection.id())) {
				return section;
			}
		}
		SectionBag section = new SectionBag();
		section.setChapter(this);
		section.setId(currentSection.id());
		section.setActions(new ArrayList<ActionBag>());
		sections.add(section);
		return section;
	}
	public SectionBag getSection(String id) {
		for(SectionBag section: sections) {
			if (section.getId().equals(id)) {
				return section;
			}
		}
		return null;
	}
	
	
}
