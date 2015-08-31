package com.quantaconsultoria.docgem.bags;

import java.io.Serializable;
import java.util.List;

public class SectionBag implements Serializable {
	
	private static final long serialVersionUID = -5735965484244231966L;

	private String id;
	
	private String title;
	
	private String path;
	
	private String indice;	
	
	private transient ChapterBag chapter;
	
	private List<SectionBag> sections;
	
	private List<ActionBag> actions;
	

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

	public ChapterBag getChapter() {
		return chapter;
	}

	public void setChapter(ChapterBag chapter) {
		this.chapter = chapter;
	}

	public List<SectionBag> getSections() {
		return sections;
	}

	public void setSections(List<SectionBag> sections) {
		this.sections = sections;
	}

	public List<ActionBag> getActions() {
		return actions;
	}

	public void setActions(List<ActionBag> actions) {
		this.actions = actions;
	}
	
}
