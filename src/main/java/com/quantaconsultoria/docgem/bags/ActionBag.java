package com.quantaconsultoria.docgem.bags;

import java.io.Serializable;

public class ActionBag  implements Serializable {

	private static final long serialVersionUID = -835582459201994081L;
	
	private transient SectionBag section;
	
	private String text;
	
	private String imageFile;
	
	public ActionBag(String text, String imageFile) {
		super();
		this.text = text;
		this.imageFile = imageFile;
	}
	
	public ActionBag() {
		
	}

	public SectionBag getSection() {
		return section;
	}

	public void setSection(SectionBag section) {
		this.section = section;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}
	
}
