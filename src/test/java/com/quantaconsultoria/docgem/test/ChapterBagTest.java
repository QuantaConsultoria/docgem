package com.quantaconsultoria.docgem.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ChapterBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

public class ChapterBagTest {
	
	@Test
	public void mustReturnNullIfEmpity() throws NoSuchMethodException, SecurityException {
		ChapterBag bag = new ChapterBag();
		bag.setTitle("Chapter");
		bag.setSections(new ArrayList<SectionBag>());
		SectionBag sectionBag = new SectionBag();
		sectionBag.setId("Section test");
		bag.getSections().add(sectionBag);
		
		Section section = this.getClass().getMethod("generateSection", null).getAnnotation(Section.class);
		Section newSection = this.getClass().getMethod("generateNewSection", null).getAnnotation(Section.class);
		
		Assert.assertNull(bag.getSection(""));
		Assert.assertEquals(bag.getTitle(), "Chapter");
		Assert.assertEquals(bag.createSectionBag(section).getId(), "Section test");
		Assert.assertEquals(bag.createSectionBag(newSection).getId(), "New Section");
	}
	
	@Section(id="Section test")
	public void generateSection(){
		
	}
	
	@Section(id="New Section")
	public void generateNewSection(){
		
	}

}
