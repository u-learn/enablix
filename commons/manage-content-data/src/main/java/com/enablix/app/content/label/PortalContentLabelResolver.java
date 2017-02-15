package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.template.TemplateWrapper;

public class PortalContentLabelResolver implements ContentLabelResolver {

	@Override
	public String findContentLabel(Map<String, Object> record, TemplateWrapper template, String qId) {
		return ContentDataUtil.findPortalLabelValue(record, template, qId);
	}
	
}
