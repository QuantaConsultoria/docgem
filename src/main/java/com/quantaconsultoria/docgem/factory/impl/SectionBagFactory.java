package com.quantaconsultoria.docgem.factory.impl;

import java.util.ArrayList;

import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

public class SectionBagFactory {
	
	private static final int INDEX_ID = 1;
	
	public static SectionBag createSectionFromArray(String[] parts) {
		SectionBag sectionBag = new SectionBag();
		sectionBag.setId(parts[INDEX_ID]);
		sectionBag.setActions(new ArrayList<ActionBag>());
		sectionBag.getActions().add(ActionBagFactory.createActionFomArray(parts));
		
		return sectionBag;
	}

}
