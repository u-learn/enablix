package com.enablix.commons.util;

import java.io.Closeable;
import java.io.File;

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
	
}
