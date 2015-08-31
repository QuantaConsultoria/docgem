package com.quantaconsultoria.docgem.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

import com.quantaconsultoria.docgem.DocumentationConfiguration;

public class ImageUtil {
	
	public static void highlightElement(File image, WebElement elemento, Dimension dimension) throws IOException {
		BufferedImage imagem = ImageIO.read(image);
		Graphics2D g = imagem.createGraphics();
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.RED);
		g.drawRoundRect(elemento.getLocation().x-1, 
				elemento.getLocation().y-2, 
				elemento.getSize().width+2, 
				elemento.getSize().height+4, 5, 5);
		
		ImageIO.write(imagem.getSubimage(0, 0, dimension.getWidth(), dimension.getHeight()), "PNG", image);
	}
	
	public static String saveScreenshot(File srcImage, DocumentationConfiguration configuration) throws IOException {
		Long id =  (long) (Math.random()*1000000000);
		String destFilePath = "images/"+id+".png"; 
		File destFile = new File(configuration.getTarget(), destFilePath);
		
		if (!destFile.exists()) {
			FileUtils.copyFile(srcImage, destFile);
			return destFilePath;
		} else {
			return saveScreenshot(srcImage, configuration);
		}
	}

}
