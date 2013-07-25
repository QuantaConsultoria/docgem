package com.quantaconsultoria.docgem.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

public class ChapterBagTest {
	
	@Test
	public void mustReturnNullIfEmpity() {
		ChapterBag bag = new ChapterBag();
		bag.setSections(new ArrayList<SectionBag>());
		Assert.assertNull(bag.getSection(""));
	}

}
