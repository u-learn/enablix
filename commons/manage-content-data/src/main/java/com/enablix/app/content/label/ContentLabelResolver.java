package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.services.util.template.TemplateWrapper;

public interface ContentLabelResolver {

	String findContentLabel(Map<String, Object> record, TemplateWrapper template, String qId);
	
}
