package com.enablix.app.content.task.coverage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.template.TemplateWrapper;

@Component
public class LinkedContainerCoverageResolver {

	@Autowired
	private ContentCrudService crud;
	
	public long getCount(ContainerType linkedContainer, ContentDataRecord record, TemplateWrapper template) {
		
		String linkContainerQId = linkedContainer.getLinkContainerQId();
		String recordIdentity = (String) record.getRecord().get(ContentDataConstants.IDENTITY_KEY);
		
		String collectionName = template.getCollectionName(linkContainerQId);
		
		return crud.findRecordCountWithLinkContainerId(collectionName, linkedContainer.getLinkContentItemId(), recordIdentity);
	}
	
}
