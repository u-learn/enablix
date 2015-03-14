package com.enablix.app.content;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class ContentDataManagerImpl implements ContentDataManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentDataManagerImpl.class);
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private UpdateContentRequestValidator updateValidator;

	@Autowired
	private ContentCrudService crud;
	
	/*
	 * Save algorithm:
	 * 1. Find the collection in which the data needs to be stored
	 * 2. If node is referenceable content, then find the record that needs to be updated
	 * 3. If not, then find the container record that needs to be updated
	 * 4. Update the record
	 */
	@Override
	public void saveData(UpdateContentRequest request) {
		
		Collection<Error> errors = updateValidator.validate(request);
		if (errors != null & !errors.isEmpty()) {
			LOGGER.error("Update request validation error: {}", errors);
			throw new IllegalArgumentException("Update request validation error: " + errors);
		}
		
		ContentTemplate template = templateMgr.getTemplate(request.getTemplateId());
		
		if (request.isNewRecord()) {
			String collectionName = TemplateUtil.resolveCollectionName(template, request.getContentQId());
			crud.insert(collectionName, request.getJsonData());
		} else {
			
		}
		
	}

	@Override
	public void deleteData(String templateId, String containerQId, String dataIdentity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String fetchDataJson(FetchContentRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
