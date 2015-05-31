package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface ContentLabelResolver {

	String findContentLabel(Map<String, Object> record, ContentTemplate template, String qId);
	
}
