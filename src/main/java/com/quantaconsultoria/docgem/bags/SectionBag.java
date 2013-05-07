package com.quantaconsultoria.docgem.bags;

import java.util.List;

public class SectionBag {
	
	private String id;
	private String text;
	private transient CharpterBag charpter;
	private List<ActionBag> actions;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CharpterBag getCharpter() {
		return charpter;
	}
	public void setCharpter(CharpterBag charpter) {
		this.charpter = charpter;
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
