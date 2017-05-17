package com.enablix.doc.converter.ppt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;

public class PptToImage {
   
   public static void main(String args[]) throws IOException{
      
	   String outFolder = "C:\\Users\\dluthra\\Downloads\\preview\\";
	   String filepath = "C:\\Users\\dluthra\\Downloads\\slf4j-in-10-slides.ppt";
	   FileInputStream is = new FileInputStream(filepath);
	   String imageFilePrefix = "slf4j-in-10-slides";
	   
       try (HSLFSlideShow ppt = new HSLFSlideShow(is)) {
	       
    	   is.close();
	
	       double zoom = 1; // magnify it by 2
	       AffineTransform at = new AffineTransform();
	       at.setToScale(zoom, zoom);
	
	       Dimension pgsize = ppt.getPageSize();
	
	       List<HSLFSlide> slides = ppt.getSlides();
	       int slideIndx = 0;
	       
	       for (HSLFSlide slide : slides) {
	           BufferedImage img = new BufferedImage((int)Math.ceil(pgsize.width*zoom), (int)Math.ceil(pgsize.height*zoom), BufferedImage.TYPE_INT_RGB);
	           Graphics2D graphics = img.createGraphics();
	           //graphics.setTransform(at);
	
	           graphics.setPaint(Color.white);
	           graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
	           slide.draw(graphics);
	           FileOutputStream out = new FileOutputStream(outFolder + imageFilePrefix + "-" + (++slideIndx) + ".png");
	           ImageIO.write(img, "png", out);
	           out.close();
	       }
	       
       }
   }
}