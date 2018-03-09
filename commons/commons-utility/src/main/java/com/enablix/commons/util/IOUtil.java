package com.enablix.commons.util;

import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
}
