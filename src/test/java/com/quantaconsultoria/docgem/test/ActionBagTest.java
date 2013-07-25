package com.quantaconsultoria.docgem.test;

import org.junit.Assert;
import org.junit.Test;

import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.SectionBag;

public class ActionBagTest {

	@Test
	public void getAndSetMustWorks() {
		ActionBag actionBag = new ActionBag();
		actionBag.setImageFile("imagem.png");
		actionBag.setSection(new SectionBag());
		actionBag.setText("Texto");
		
		Assert.assertEquals(actionBag.getImageFile(),"imagem.png");
		Assert.assertEquals(actionBag.getText(),"Texto");
		Assert.assertNotNull(actionBag.getSection());
		
	}
}
