package com.enablix.app.content.update;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.TemplateUtil;

@Component
public class InsertChildContainerHandler implements ContentUpdateHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertChildContainerHandler.class);
	
	
	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public Map<String, Object> updateContent(TemplateFacade template, String recordId, String contentQId,
			Map<String, Object> contentDataMap) {
		
		LOGGER.debug("Insert child - templateId: {}, recordId: {}, contentQId: {}, data: {}",
				template.getId(), recordId, contentQId, contentDataMap);
		
		String collectionName = template.getCollectionName(contentQId);
		ContainerType container = template.getContainerDefinition(contentQId);
		
		if (TemplateUtil.hasOwnCollection(container)) {
			
			LOGGER.debug("Inserting new record in collection [{}]", collectionName);
			crudService.insert(collectionName, contentDataMap);
			
		} else {
			
			String relativeChildQId = TemplateUtil.getQIdRelativeToParentContainer(template.getTemplate(), contentQId);

			if (StringUtil.isEmpty(relativeChildQId)) {
				LOGGER.error("Relative Child QId is null or empty");
				throw new IllegalArgumentException("Relative Child QId is null or empty");
			}

			LOGGER.debug("Inserting child record in collection [{}]", collectionName);
			crudService.insertChildContainer(collectionName, recordId, relativeChildQId, contentDataMap);
		}
		
		return null;
		
	}

}
