package com.enablix.app.content.update;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;

@Component
public class UpdateContentAttributesHandler implements ContentUpdateHandler {

	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public void updateContent(ContentTemplate template, String recordId, String contentQId,
			Map<String, Object> contentDataMap) {
		// TODO Auto-generated method stub
		
	}


}
