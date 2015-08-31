package com.quantaconsultoria.docgem.bags;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.quantaconsultoria.docgem.annotations.Section;

public class ChapterBag  implements Serializable{
	
	private static final long serialVersionUID = 18348148544099670L;
	
	private String id;
	
	private String title;
	
	private String path;
	
	private String indice;	
	
	private List<SectionBag> sections;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIndice() {
		return indice;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public List<SectionBag> getSections() {
		return sections;
	}

	public void setSections(List<SectionBag> sections) {
		this.sections = sections;
	}

	public SectionBag createSectionBag(Section currentSection) {
		SectionBag section = this.getSection(currentSection.id());
		
		if (section!=null) {
			return section;
		} else {
			section = new SectionBag();
			section.setChapter(this);
			section.setId(currentSection.id());
			section.setActions(new ArrayList<ActionBag>());
			sections.add(section);
			return section;
		}
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
