package com.quantaconsultoria.docgem.bags;

import java.util.List;

public class SectionBag {
	
	private String id;
	private String text;
	private transient ChapterBag chapter;
	private List<ActionBag> actions;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ChapterBag getChapter() {
		return chapter;
	}
	public void setChapter(ChapterBag chapter) {
		this.chapter = chapter;
	}
	public List<ActionBag> getActions() {
		return actions;
	}
	public void setActions(List<ActionBag> actions) {
		this.actions = actions;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
