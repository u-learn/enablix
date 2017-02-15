package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.template.TemplateWrapper;

public class DefaultContentLabelResolver implements ContentLabelResolver {

	@Override
	public String findContentLabel(Map<String, Object> record, TemplateWrapper template, String qId) {
		return ContentDataUtil.findStudioLabelValue(record, template, qId);
	}
	
}
