package com.enablix.app.content.event.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.event.ContentDataDelEvent;
import com.enablix.app.content.event.ContentDataEventListener;
import com.enablix.app.content.event.ContentDataSaveEvent;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.DatastoreUtil;
import com.enablix.services.util.TemplateUtil;

/**
 * If a container is used in the dropdown value in another container, then this listener
 * will update the label value in the other container record based on the label set in
 * the updated record.
 * 
 * @author dikshit.luthra
 *
 */

@Component
public class UpdateReferencedLabelListener implements ContentDataEventListener {

	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private ContentCrudService crudService;
	
	@Override
	public void onContentDataSave(ContentDataSaveEvent event) {
		
		if (!event.isNewRecord()) {
		
			ContentTemplate template = templateMgr.getTemplate(event.getTemplateId());
			
			// get containers where the updated record is used as a dropdown value
			List<ContainerType> assocContainers = TemplateUtil.findContainersWithDatastoreAssociation(
													template, event.getContainerType().getQualifiedId());
			
			String forContainerId = event.getContainerType().getId();
			
			for (ContainerType assocCntnr : assocContainers) {
				
				// find the content item in the container where the updated record's container is used
				for (ContentItemType contentItem : assocCntnr.getContentItem()) {
					
					if (contentItem.getType() == ContentItemClassType.BOUNDED) {
						
						BoundedType boundedItem = contentItem.getBounded();
						if (boundedItem != null) {
						
							BoundedRefListType boundedRefList = boundedItem.getRefList();
							if (boundedRefList != null) {
				
								if (boundedRefList.getDatastore().getStoreId().equals(forContainerId)) {
									
									// update the label of the existing records as per the value in the updated record
									String boundedLabelAttr = contentItem.getBounded().getRefList().getDatastore().getDataLabel();
									
									// check if the change has occured on the label attribute
									if (event.getChangeDelta().hasAttribute(boundedLabelAttr)) {
										
										Object labelValue = event.getDataAsMap().get(boundedLabelAttr);
										String labelValueStr = labelValue == null ? null : String.valueOf(labelValue);
										
										String recordIdentity = (String) event.getDataAsMap().get(ContentDataConstants.IDENTITY_KEY);
										
										String collectionName = DatastoreUtil.getCollectionName(event.getTemplateId(), assocCntnr);
										crudService.updateBoundedLabel(collectionName, contentItem.getId(), recordIdentity, labelValueStr);
									}
								}
							}
							
						}
					}
				}
				
			}
			
		}
		
	}

	@Override
	public void onContentDataDelete(ContentDataDelEvent event) {
			
		ContentTemplate template = templateMgr.getTemplate(event.getTemplateId());
		
		ContainerType delContainerType = TemplateUtil.findContainer(template.getDataDefinition(), event.getContainerQId());
		
		// get containers where the delete record is used as a dropdown value
		List<ContainerType> assocContainers = TemplateUtil.findContainersWithDatastoreAssociation(
												template, delContainerType.getQualifiedId());
		
		String forContainerId = delContainerType.getId();
		
		for (ContainerType assocCntnr : assocContainers) {
			
			// find the content item in the container where the delete record's container is used
			for (ContentItemType contentItem : assocCntnr.getContentItem()) {
				
				if (contentItem.getType() == ContentItemClassType.BOUNDED) {
					
					BoundedType boundedItem = contentItem.getBounded();
					if (boundedItem != null) {
					
						BoundedRefListType boundedRefList = boundedItem.getRefList();
						if (boundedRefList != null) {
			
							if (boundedRefList.getDatastore().getStoreId().equals(forContainerId)) {
								
								// delete the bounded value corresponding to the deleted record
								String recordIdentity = (String) event.getContentIdentity();
								
								String collectionName = DatastoreUtil.getCollectionName(event.getTemplateId(), assocCntnr);
								crudService.deleteBoundedItem(collectionName, contentItem.getId(), recordIdentity);
							}
						}
					}
				}
			}
		}
	}

}
