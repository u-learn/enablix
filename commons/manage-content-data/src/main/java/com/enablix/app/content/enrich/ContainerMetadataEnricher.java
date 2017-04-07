package com.enablix.app.content.enrich;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;

@Component
public class ContainerMetadataEnricher implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, TemplateFacade contentTemplate) {
		
		// add container name as a metadata in content
		ContainerType containerType = contentTemplate.getContainerDefinition(updateCtx.contentQId());
		
		content.put(ContentDataConstants.CONTAINER_NAME_METADATA_FLD, containerType.getLabel());
		
	}

}
