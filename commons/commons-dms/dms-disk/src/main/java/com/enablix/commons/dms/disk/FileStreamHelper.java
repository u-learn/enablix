package com.enablix.commons.dms.disk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.DocInfo;

public class FileStreamHelper {

	public static InputStream getFileInputStream(DocInfo docInfo) {
		
		InputStream is = null;
		
		if (!StringUtil.isEmpty(docInfo.getLocation())) {
			try {
				is = new FileInputStream(docInfo.getLocation());
			} catch (FileNotFoundException e) {
				throw new IllegalArgumentException("Unable to open file", e);
			}
		}
		
		return is;
	}
	
}
