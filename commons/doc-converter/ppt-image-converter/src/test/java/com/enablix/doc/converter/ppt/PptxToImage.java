package com.enablix.doc.converter.ppt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

public class PptxToImage {
   
   public static void main(String args[]) throws IOException{
      
	   String outFolder = "C:\\Users\\dluthra\\Downloads\\preview\\";
	   String filePath = "C:\\Users\\dluthra\\Downloads\\Haystax Presentation Template.pptx";
	   String imageFilePrefix = "Haystax";
	   FileInputStream is = new FileInputStream(filePath);
	   
       try (XMLSlideShow ppt = new XMLSlideShow(is)) {
	       
    	   is.close();
	
	       double zoom = 2; // magnify it by 2
	       AffineTransform at = new AffineTransform();
	       at.setToScale(zoom, zoom);
	
	       Dimension pgsize = ppt.getPageSize();
	
	       List<XSLFSlide> slides = ppt.getSlides();
	       int slideIndx = 0;
	       
	       for (XSLFSlide slide : slides) {
	           
	    	   BufferedImage img = new BufferedImage((int)Math.ceil(pgsize.width*zoom), (int)Math.ceil(pgsize.height*zoom), BufferedImage.TYPE_INT_RGB);
	           Graphics2D graphics = img.createGraphics();
	           
	           graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	           graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	           graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	           graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	           
	           graphics.setTransform(at);
	
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