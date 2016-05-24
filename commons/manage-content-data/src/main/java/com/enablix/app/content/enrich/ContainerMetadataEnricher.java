package com.enablix.app.content.enrich;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.TemplateUtil;

@Component
public class ContainerMetadataEnricher implements ContentEnricher {

	@Override
	public void enrich(ContentUpdateContext updateCtx, 
			Map<String, Object> content, ContentTemplate contentTemplate) {
		
		// add container name as a metadata in content
		ContainerType containerType = TemplateUtil.findContainer(
				contentTemplate.getDataDefinition(), updateCtx.contentQId());
		
		content.put(ContentDataConstants.CONTAINER_NAME_METADATA_FLD, containerType.getLabel());
		
	}

}
