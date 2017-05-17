package com.enablix.doc.converter.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.enablix.core.api.DocInfo;
import com.enablix.core.api.DocumentFormat;
import com.enablix.doc.converter.DocFormatResolver;

@Component
@ConfigurationProperties(prefix="doc.format")
public class DocFormatResolverImpl implements DocFormatResolver {

	private Map<String, DocumentFormat> contentTypeMapping = new HashMap<>();
	
	private Map<String, DocumentFormat> extMapping = new HashMap<>();

	public Map<String, DocumentFormat> getContentTypeMapping() {
		return contentTypeMapping;
	}

	public Map<String, DocumentFormat> getExtMapping() {
		return extMapping;
	}

	@Override
	public DocumentFormat resolve(DocInfo docInfo) {
		
		DocumentFormat docFormat = contentTypeMapping.get(docInfo.getContentType());
		
		if (docFormat == null) {
			docFormat = extMapping.get(getFileExtension(docInfo.getName()));
		}
		
		return docFormat == null ? DocumentFormat.OTHER : docFormat;
	}

	private String getFileExtension(String name) {
		
		String ext = null;
		
		int lastIndexOfDot = name.lastIndexOf('.');
		if (lastIndexOfDot > 0 && lastIndexOfDot < (name.length() - 1)) {
			ext = name.substring(lastIndexOfDot + 1);
		}
		
		return ext;
	}
	
	
}
