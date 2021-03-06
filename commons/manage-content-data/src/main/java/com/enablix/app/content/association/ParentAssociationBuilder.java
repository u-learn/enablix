package com.enablix.app.content.association;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.update.ContentUpdateContext;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.content.ContentAssociation;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.services.util.TemplateUtil;

@Component
public class ParentAssociationBuilder implements ContentAssociationBuilder {

	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public Collection<ContentAssociation> buildAssociations(
			TemplateFacade template, ContentUpdateContext request, Map<String, Object> dataAsMap) {
		
		Collection<ContentAssociation> associations = new ArrayList<ContentAssociation>();
		
		// if content is a root element i.e. it has a separate collection
		// then add an association to the parent collection record 
		if (!StringUtil.isEmpty(request.parentIdentity())
				&& !TemplateUtil.isRootContainer(template.getTemplate(), request.contentQId()) 
				&& TemplateUtil.hasOwnCollection(template.getContainerDefinition(request.contentQId()))) {
			
			String parentCollection = TemplateUtil.findParentCollectionName(template, request.contentQId());
			
			Map<String, Object> parent = crudService.findRecord(
					parentCollection, request.parentIdentity(), MongoDataView.ALL_DATA);
			
			String parentIdentity = (String) parent.get(ContentDataConstants.IDENTITY_KEY);
			String parentId = String.valueOf(parent.get(ContentDataConstants.RECORD_ID_KEY));
			
			associations.add(new ContentAssociation(ContentDataConstants.PARENT_ASSOCIATION, 
									parentCollection, parentId, parentIdentity));
		}

		return associations;
	}

	@Override
	public boolean replaceExisting() {
		return true;
	}

}
