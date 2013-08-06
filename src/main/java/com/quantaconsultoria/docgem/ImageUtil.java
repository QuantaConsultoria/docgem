package com.quantaconsultoria.docgem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebElement;

public class ImageUtil {
	

	public static void circulateElement(File image, WebElement elemento) throws IOException {
		BufferedImage imagem = ImageIO.read(image);
		Graphics2D g = imagem.createGraphics();
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.RED);
		g.drawRoundRect(elemento.getLocation().x-1, 
				elemento.getLocation().y-2, 
				elemento.getSize().width+2, 
				elemento.getSize().height+4, 5, 5);
		
		ImageIO.write(imagem, "PNG", image);
	}

}
