package com.quantaconsultoria.docgem.bags;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class DocumentationBag implements Serializable{

	private static final long serialVersionUID = -363433881112177448L;
	
	private Date dateRevision;
	
	private String version;
	
	private String title;
	
	private List<ChapterBag> chapters;
	

	public Date getDateRevision() {
		return dateRevision;
	}

	public void setDateRevision(Date dateRevision) {
		this.dateRevision = dateRevision;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String titel) {
		this.title = titel;
	}

	public List<ChapterBag> getChapters() {
		return chapters;
	}

	public void setChapters(List<ChapterBag> chapters) {
		this.chapters = chapters;
	}

}
