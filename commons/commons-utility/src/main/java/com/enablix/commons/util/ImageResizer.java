package com.enablix.commons.util;
 
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
 
public class ImageResizer {
 
    public static BufferedImage resize(InputStream inputImageStream, 
    		String formatName, int scaledWidth) throws IOException {
    	
    	// reads input image
        BufferedImage inputImage = ImageIO.read(inputImageStream);
        
    	double percent = ((double) scaledWidth) / inputImage.getWidth();
        int scaledHeight = (int) (inputImage.getHeight() * percent);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 
        return outputImage;
    }
 
}