package com.quantaconsultoria.docgem.factory.impl;

import java.util.ArrayList;

import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

public class ChapterBagFactory {
	
	private static final int INDEX_ID = 0;
	
	public static ChapterBag createChapterFromStringArray(String[] parts) {
		ChapterBag chapterBag = new ChapterBag();
		chapterBag.setId(parts[INDEX_ID]);
		chapterBag.setSections(new ArrayList<SectionBag>());
		
		SectionBag sectionBag = SectionBagFactory.createSectionFromArray(parts);
		chapterBag.getSections().add(sectionBag);
		
		return chapterBag;
	}
	
	public static void updateChapterInfo(ChapterBag chapterBag, String[] parts) {
		SectionBag sectionBag = chapterBag.getSection(parts[1]); 
		if(sectionBag != null){
			sectionBag.getActions().add(ActionBagFactory.createActionFomArray(parts));
		} else {
			chapterBag.getSections().add(SectionBagFactory.createSectionFromArray(parts));
		}
	}

}
