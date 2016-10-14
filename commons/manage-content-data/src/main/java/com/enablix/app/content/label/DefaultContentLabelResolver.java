package com.enablix.app.content.label;

import java.util.Map;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.ContentDataUtil;

public class DefaultContentLabelResolver implements ContentLabelResolver {

	@Override
	public String findContentLabel(Map<String, Object> record, ContentTemplate template, String qId) {
		return ContentDataUtil.findStudioLabelValue(record, template, qId);
	}
	
}
