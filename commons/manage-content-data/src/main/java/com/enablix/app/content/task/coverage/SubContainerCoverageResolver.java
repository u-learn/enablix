package com.enablix.app.content.task.coverage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.TemplateUtil;

@Component
public class SubContainerCoverageResolver {

	@Autowired
	private ContentCrudService crud;
	
	public long getCount(ContainerType subContainer, ContentDataRecord record, TemplateFacade template) {

		long count = 0;
		
		String contentQId = subContainer.getQualifiedId();
		String collName = template.getCollectionName(contentQId);
		String recordIdentity = (String) record.getRecord().get(ContentDataConstants.IDENTITY_KEY);
		
		// Fetch all child containers
		if (TemplateUtil.hasOwnCollection(template.getContainerDefinition(contentQId ))) {
		
			// content is in its own collection, hence query with parent id
			count = crud.findRecordCountWithParentId(collName, recordIdentity, MongoDataView.ALL_DATA);
			
		} else {
			// content is a child array in parents collection, hence retrieve child elements
			String qIdRelativeToParent = TemplateUtil.getQIdRelativeToParentContainer(template.getTemplate(), contentQId);
			count  = crud.findChildElementsCount(collName, qIdRelativeToParent, recordIdentity, MongoDataView.ALL_DATA);
		}
		
		return count;
	}
	
}
