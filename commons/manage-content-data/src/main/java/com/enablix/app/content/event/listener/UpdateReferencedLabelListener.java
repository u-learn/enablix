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
import com.enablix.core.mongo.content.ContentCrudService;
import com.enablix.services.util.DatastoreUtil;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.TemplateWrapper;
import com.enablix.services.util.template.walker.ContainerFilter;
import com.enablix.services.util.template.walker.ContainerVisitor;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

/**
 * If a container is used in the dropdown or content stack value in another container, 
 * then this listener will update the label value in the other container record based 
 * on the label set in the updated record.
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
			
			TemplateWrapper template = templateMgr.getTemplateWrapper(event.getTemplateId());
			
			final ContainerType eventContainer = event.getContainerType();
			final String labelAttr = template.getStudioLabelAttributeId(eventContainer.getQualifiedId());
			
			Object labelValue = event.getDataAsMap().get(labelAttr);
			final String labelValueStr = labelValue == null ? null : String.valueOf(labelValue);
			final String recordIdentity = (String) event.getDataAsMap().get(ContentDataConstants.IDENTITY_KEY);
			
			if (event.getChangeDelta().hasAttribute(labelAttr)) {
				
				updateLabelInBoundedItems(event, template);
				
				updateContentStack(template, new ContentStackAction() {
					
					@Override
					public void execute(ContainerType containerToUpdate,
							ContentItemType itemOfUpdateContainer, TemplateWrapper template) {
						String collectionName = template.getCollectionName(containerToUpdate.getQualifiedId());
						crudService.updateBoundedLabel(collectionName, itemOfUpdateContainer.getId(), recordIdentity, labelValueStr);
					}
				});
				
			}
		}
		
	}

	private void updateContentStack(final TemplateWrapper template, final ContentStackAction action) {
		
		TemplateContainerWalker walker = new TemplateContainerWalker(template.getTemplate(), 
				new ContainerFilter() {
					
					@Override
					public boolean accept(ContainerType container) {
						return !container.isRefData() && !TemplateUtil.isLinkedContainer(container);
					}
				});

		walker.walk(new ContainerVisitor() {
			
			@Override
			public void visit(ContainerType container) {
				
				List<ContentItemType> contentStackItems = TemplateUtil.getContentStackItems(container);
				
				for (ContentItemType contentItem : contentStackItems) {
					action.execute(container, contentItem, template);
				}
			}
		});
	}

	private void updateLabelInBoundedItems(ContentDataSaveEvent event, TemplateWrapper template) {
		
		// get containers where the updated record is used as a dropdown value
		List<ContainerType> assocContainers = 
				TemplateUtil.findContainersWithDatastoreAssociation(
								template.getTemplate(), event.getContainerType().getQualifiedId());
		
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

	@Override
	public void onContentDataDelete(final ContentDataDelEvent event) {
			
		TemplateWrapper template = templateMgr.getTemplateWrapper(event.getTemplateId());
		
		final String recordIdentity = event.getContentIdentity();
		
		removeFromBoundedItems(event, template);
		
		updateContentStack(template, new ContentStackAction() {
			
			@Override
			public void execute(ContainerType containerToUpdate,
					ContentItemType itemOfUpdateContainer, TemplateWrapper template) {
				
				String collectionName = template.getCollectionName(containerToUpdate.getQualifiedId());
				crudService.deleteContentStackItem(collectionName, itemOfUpdateContainer.getId(), recordIdentity);

			}
		});
	}

	private void removeFromBoundedItems(ContentDataDelEvent event, TemplateWrapper template) {
		ContainerType delContainerType = event.getContainerType();
		
		// get containers where the delete record is used as a dropdown value
		List<ContainerType> assocContainers = TemplateUtil.findContainersWithDatastoreAssociation(
												template.getTemplate(), delContainerType.getQualifiedId());
		
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

	private static interface ContentStackAction {
		
		void execute(ContainerType containerToUpdate, 
				ContentItemType itemOfUpdateContainer, TemplateWrapper template);
	}
	
}
