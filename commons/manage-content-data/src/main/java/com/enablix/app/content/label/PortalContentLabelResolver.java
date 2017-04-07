package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.core.api.TemplateFacade;
import com.enablix.services.util.ContentDataUtil;

public class PortalContentLabelResolver implements ContentLabelResolver {

	@Override
	public String findContentLabel(Map<String, Object> record, TemplateFacade template, String qId) {
		return ContentDataUtil.findPortalLabelValue(record, template, qId);
	}
	
}
