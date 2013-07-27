package com.quantaconsultoria.docgem.test;

import org.junit.Assert;
import org.junit.Test;

import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

public class SectionTest {
	
	@Test
	public void getAndSetMustWorks() {
		
		SectionBag sectionBag = new SectionBag();
		sectionBag.setId("id");
		sectionBag.setText("Text");
		sectionBag.setChapter(new ChapterBag());
		
		Assert.assertEquals(sectionBag.getId(),"id");
		Assert.assertEquals(sectionBag.getText(),"Text");
		Assert.assertNotNull(sectionBag.getChapter());
	}

}
