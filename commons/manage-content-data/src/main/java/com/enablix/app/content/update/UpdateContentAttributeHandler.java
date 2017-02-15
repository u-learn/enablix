package com.enablix.app.content.update;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class UpdateContentAttributeHandler implements ContentUpdateHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateContentAttributeHandler.class);
	
	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public Map<String, Object> updateContent(TemplateWrapper template, String recordId, String contentQId,
			Map<String, Object> contentDataMap) {
		
		LOGGER.debug("Updating content attributes for templateId: {}, recordId: {}, "
				+ "contentQId: {}, data: {}", template.getId(), recordId, contentQId, contentDataMap);
		
		String collectionName = template.getCollectionName(contentQId);
		String relativeContentQId = TemplateUtil.getQIdRelativeToParentContainer(template.getTemplate(), contentQId);

		String contentIdentity = (String) contentDataMap.get(ContentDataConstants.IDENTITY_KEY);
		if (StringUtil.isEmpty(contentIdentity)) {
			LOGGER.error("Content identity is null or empty - data: {}", contentDataMap);
			throw new IllegalArgumentException("Content identity is null or empty");
		}
		
		Map<String, Object> existRecord = crudService.findRecord(collectionName, contentIdentity);
		
		crudService.updateAttributes(collectionName, relativeContentQId, 
				contentIdentity, contentDataMap);
		
		return existRecord;
	}


}
