package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.app.content.ContentDataUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public class PortalContentLabelResolver implements ContentLabelResolver {

	@Override
	public String findContentLabel(Map<String, Object> record, ContentTemplate template, String qId) {
		return ContentDataUtil.findPortalLabelValue(record, template, qId);
	}
	
}
