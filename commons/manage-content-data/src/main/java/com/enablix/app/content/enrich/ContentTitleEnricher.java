package com.enablix.app.content.enrich;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class ContentTitleEnricher implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateWrapper contentTemplate) {
		String title = ContentDataUtil.findPortalLabelValue(content, contentTemplate, updateCtx.contentQId());
		content.put(ContentDataConstants.CONTENT_TITLE_KEY, title);
	}

}
