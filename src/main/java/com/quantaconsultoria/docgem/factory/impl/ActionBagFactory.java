package com.quantaconsultoria.docgem.factory.impl;

import com.quantaconsultoria.docgem.bags.ActionBag;

public class ActionBagFactory {
	
	private static final int INDEX_TEXT = 2;
	private static final int INDEX_IMAGE_LOCATION = 3;
	
	public static ActionBag createActionFomArray(String[] parts) {
		return new ActionBag(parts[INDEX_TEXT], parts[INDEX_IMAGE_LOCATION]);
	}

}
