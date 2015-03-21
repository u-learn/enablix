package com.enablix.app.content.update;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.TemplateUtil;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class UpdateContentAttributeHandler implements ContentUpdateHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateContentAttributeHandler.class);
	
	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public void updateContent(ContentTemplate template, String recordId, String contentQId,
			Map<String, Object> contentDataMap) {
		
		LOGGER.debug("Updating content attributes for templateId: {}, recordId: {}, "
				+ "contentQId: {}, data: {}", template.getId(), recordId, contentQId, contentDataMap);
		
		String collectionName = TemplateUtil.resolveCollectionName(template, contentQId);
		String relativeContentQId = TemplateUtil.getQIdRelativeToParentContainer(template, contentQId);

		String contentIdentity = (String) contentDataMap.get(ContentDataConstants.IDENTITY_KEY);
		if (StringUtil.isEmpty(contentIdentity)) {
			LOGGER.error("Content identity is null or empty - data: {}", contentDataMap);
			throw new IllegalArgumentException("Content identity is null or empty");
		}
		
		crudService.updateAttributes(collectionName, recordId, 
				relativeContentQId, contentIdentity, contentDataMap);
	}


}
