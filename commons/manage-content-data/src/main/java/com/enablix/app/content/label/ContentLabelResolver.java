package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.core.api.TemplateFacade;

public interface ContentLabelResolver {

	String findContentLabel(Map<String, Object> record, TemplateFacade template, String qId);
	
}
