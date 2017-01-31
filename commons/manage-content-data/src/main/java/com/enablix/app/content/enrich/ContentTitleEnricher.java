package com.enablix.app.content.enrich;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.ContentDataUtil;

@Component
public class ContentTitleEnricher implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, ContentTemplate contentTemplate) {
		String title = ContentDataUtil.findPortalLabelValue(content, contentTemplate, updateCtx.contentQId());
		content.put(ContentDataConstants.CONTENT_TITLE_KEY, title);
	}

}
