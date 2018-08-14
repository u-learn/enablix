package com.enablix.commons.util;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coobird.thumbnailator.Thumbnails;

public class IOUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(IOUtil.class);
	
	public static void closeStream(Closeable stream) {
		
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				LOGGER.error("Error closing stream", e);
			}
		}
		
	}
	
	public static void deleteFile(File file) {
		if (file != null) {
			file.delete();
		}
	}
	
	public static Map<String, Object> getImageProperties(BufferedImage bim) {
		return getImageProperties(bim.getWidth(), bim.getHeight());
	}
	
	public static Map<String, Object> getImageProperties(int width, int height) {

		Map<String, Object> imageProp = new HashMap<>();
		imageProp.put("width", width);
		imageProp.put("height", height);
		
		return imageProp;
	}
	
	public static Map<String, Object> resizeImage(InputStream imageStream, int maxWidth, 
			String outputFormat, OutputStream imageOutStream) throws IOException {
		
		BufferedImage inImage = ImageIO.read(imageStream);
		
		int imgWidth = inImage.getWidth();
		int imgHeight = inImage.getHeight();
		
		if (imgWidth > maxWidth) {
			
			int scaledWidth = maxWidth;
	    	double percent = ((double) scaledWidth) / imgWidth;
	        int scaledHeight = (int) (inImage.getHeight() * percent);

	        Thumbnails.of(inImage).outputFormat(outputFormat)
				.size(scaledWidth, scaledHeight).toOutputStream(imageOutStream);
	        
	        imgWidth = scaledWidth;
	        imgHeight = scaledHeight;
	        
		} else {
			ImageIO.write(inImage, outputFormat, imageOutStream);
		}
		
        return getImageProperties(imgWidth, imgHeight);
	}
	
}
