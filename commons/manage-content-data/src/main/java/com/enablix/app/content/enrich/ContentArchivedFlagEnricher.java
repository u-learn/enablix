package com.enablix.app.content.enrich;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;

@Component
public class ContentArchivedFlagEnricher implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, Map<String, Object> content, TemplateFacade contentTemplate) {
		if (!content.containsKey(ContentDataConstants.ARCHIVED_KEY)) {
			content.put(ContentDataConstants.ARCHIVED_KEY, false);
		}
	}

}
