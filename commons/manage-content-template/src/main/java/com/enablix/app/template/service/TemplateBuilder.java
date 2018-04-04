package com.enablix.app.template.service;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.exception.AppError;
import com.enablix.commons.exception.ErrorCodes;
import com.enablix.commons.exception.ValidationException;
import com.enablix.commons.util.QIdUtil;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.commons.xsdtopojo.ContainerPortalConfigType;
import com.enablix.core.commons.xsdtopojo.ContainerPortalConfigType.HeadingContentItem;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContainerUIDefType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.ContentUIDefType;
import com.enablix.core.commons.xsdtopojo.RefContentItemType;
import com.enablix.core.commons.xsdtopojo.TextAutoPopulateType;
import com.enablix.core.commons.xsdtopojo.TextUIClassType;
import com.enablix.core.commons.xsdtopojo.TextUIDefType;
import com.enablix.services.util.TemplateUtil;
import com.enablix.services.util.template.walker.TemplateContainerWalker;

public class TemplateBuilder {

	private static final int NAME_SEARCH_BOOST = 7;

	private static final String NAME_ATTR_ID = "name";
	private static final String NAME_ATTR_LBL = "Name";

	private static final String SHORT_NAME_ATTR_ID = "shortName";
	private static final String SHORT_NAME_ATTR_LBL = "Short Name";
	
	public static boolean updateContainerAndCheckLabelUpdate(ContentTemplate template, ContainerType container) throws Exception {
		
		ContainerType existContainer = TemplateUtil.findContainer(template.getDataDefinition(), container.getQualifiedId());
		
		if (existContainer == null) {
			throw new ValidationException(Collections.singletonList(
					new AppError(ErrorCodes.INVALID_CONTAINER_QID, "Invalid container [" + container.getQualifiedId() + "]")));
		}
		
		boolean labelUpdated = !container.getLabel().equals(existContainer.getLabel())
								|| (container.getSingularLabel() != null 
								&& !container.getSingularLabel().equals(existContainer.getSingularLabel()));
		
		BeanUtils.copyProperties(container, existContainer);
		
		TemplateContainerWalker templateWalker = new TemplateContainerWalker(template, 
				(visitContainer) -> !visitContainer.getQualifiedId().equals(container.getQualifiedId()));
		
		templateWalker.walk((visitContainer) -> {
			
			// update label in linked containers
			visitContainer.getContainer().forEach((subContainer) -> {
				if (TemplateUtil.isLinkedContainer(subContainer)
						&& subContainer.getLinkContainerQId().equals(container.getQualifiedId())) {
					subContainer.setLabel(container.getLabel());
				}
			});
			
			// update label in content items
			visitContainer.getContentItem().forEach((ci) -> {
				
				if (ci.getBounded() != null && ci.getBounded().getRefList() != null
						&& ci.getBounded().getRefList().getDatastore() != null
						&& ci.getBounded().getRefList().getDatastore().getStoreId().equals(container.getQualifiedId())) {
					
					ci.setLabel(container.getLabel());
				}
				
			});
		});
		
		return labelUpdated;
	}
	
	public static void addContainerToTemplate(ContentTemplate template, ContainerType container) throws Exception {
		
		template.getDataDefinition().getContainer().add(container);
		
		if (container.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_DIMENSION) {
			
			addTextItem(NAME_ATTR_ID, NAME_ATTR_LBL, container);
			addTextItem(SHORT_NAME_ATTR_ID, SHORT_NAME_ATTR_LBL, container);

			addUILabelConfig(template, container, SHORT_NAME_ATTR_ID);
			
			addShortNameUIConfig(template, container);
			
			TemplateContainerWalker walker = new TemplateContainerWalker(template, 
					(cntnrType) -> cntnrType.getBusinessCategory() == ContainerBusinessCategoryType.BUSINESS_CONTENT);
			
			walker.walk((bizContentContainer) -> {
				
				addLinkedContainer(container, SHORT_NAME_ATTR_ID, bizContentContainer);
				
			});
		}
		
	}

	private static void addLinkedContainer(ContainerType inContainer, 
			String inContainerLabelAttrId, ContainerType linkContainer) {
		
		String linkContentItemId = linkContainer.getId() + inContainer.getId();
		String linkContainerId = inContainer.getId() + linkContainer.getId();
		
		// Add linked content item in biz container
		ContentItemType linkingItem = new ContentItemType();
		linkingItem.setId(linkContentItemId);
		linkingItem.setQualifiedId(QIdUtil.createQualifiedId(linkContainer.getQualifiedId(), linkContentItemId));
		linkingItem.setLabel(inContainer.getLabel());
		linkingItem.setType(ContentItemClassType.BOUNDED);
		
		BoundedType boundedType = new BoundedType();
		boundedType.setMultivalued(true);
		BoundedRefListType refListType = new BoundedRefListType();
		
		BoundedListDatastoreType ds = new BoundedListDatastoreType();
		ds.setHyperlink(true);
		ds.setStoreId(inContainer.getQualifiedId());
		ds.setDataId(ContentDataConstants.IDENTITY_KEY);
		ds.setDataLabel(inContainerLabelAttrId);
		refListType.setDatastore(ds );
		
		boundedType.setRefList(refListType );
		
		linkingItem.setBounded(boundedType);
		
		linkContainer.getContentItem().add(linkingItem);
		
		ContainerType linkedContentType = new ContainerType();
		linkedContentType.setLabel(inContainer.getLabel());
		linkedContentType.setId(linkContainerId);
		linkedContentType.setQualifiedId(QIdUtil.createQualifiedId(inContainer.getId(), linkContainerId));
		linkedContentType.setLinkContainerQId(linkContainer.getQualifiedId());
		linkedContentType.setLinkContentItemId(linkContentItemId);
		
		inContainer.getContainer().add(linkedContentType);
	}

	private static void addShortNameUIConfig(ContentTemplate template, ContainerType container) {
		
		ContentUIDefType shortNameConfig = new ContentUIDefType();
		shortNameConfig.setQualifiedId(QIdUtil.createQualifiedId(container.getQualifiedId(), SHORT_NAME_ATTR_ID));
		
		TextUIDefType snTextUIDef = new TextUIDefType();
		snTextUIDef.setType(TextUIClassType.SHORT);
		
		TextAutoPopulateType snAutoPopulate = new TextAutoPopulateType();
		RefContentItemType nameRefItem = new RefContentItemType();
		nameRefItem.setItemId(NAME_ATTR_ID);
		snAutoPopulate.setRefContentItem(nameRefItem );
		
		snTextUIDef.setAutoPopulate(snAutoPopulate );
		shortNameConfig.setText(snTextUIDef );
		
		template.getUiDefinition().getContentUIDef().add(shortNameConfig);
	}

	private static void addUILabelConfig(ContentTemplate template, ContainerType container, String lblAttrId) {
		
		ContentUIDefType contUIDef = new ContentUIDefType();
		contUIDef.setQualifiedId(container.getQualifiedId());
		
		ContainerUIDefType cntnrUIDef = new ContainerUIDefType();
		String shortNameQId = QIdUtil.createQualifiedId(container.getQualifiedId(), lblAttrId);
		cntnrUIDef.setLabelQualifiedId(shortNameQId);
		contUIDef.setContainer(cntnrUIDef );
		
		ContainerPortalConfigType cntnrPortalConfig = new ContainerPortalConfigType();
		HeadingContentItem headingItem = new HeadingContentItem();
		headingItem.setId(lblAttrId);
		
		cntnrPortalConfig.setHeadingContentItem(headingItem);
		
		cntnrUIDef.setPortalConfig(cntnrPortalConfig);
		
		template.getUiDefinition().getContentUIDef().add(contUIDef);
	}

	private static void addTextItem(String nameAttrId, String nameAttrLbl, ContainerType container) {
		ContentItemType nameItem = new ContentItemType();
		nameItem.setId(nameAttrId);
		nameItem.setLabel(nameAttrLbl);
		nameItem.setType(ContentItemClassType.TEXT);
		nameItem.setSearchBoost(BigInteger.valueOf(NAME_SEARCH_BOOST));
		
		container.getContentItem().add(nameItem);
	}
	
	private static ContainerType removeContainerFromList(String containerQId, List<ContainerType> list) {
		
		ContainerType removedCntnr = null;
		
		for (Iterator<ContainerType> itr = list.iterator(); itr.hasNext(); ) {
			
			ContainerType cont = itr.next();
			
			if (cont.getQualifiedId().equals(containerQId)) {

				removedCntnr = cont;
				itr.remove();
				
			} else {
				removedCntnr = removeContainerFromList(containerQId, cont.getContainer());
				
			}
			
			if (removedCntnr != null) {
				break;
			}
		}
		
		return removedCntnr;
	}
	
	public static ContainerType removeContainer(ContentTemplate template, String containerQId) {
		ContainerType container = removeContainerFromList(containerQId, template.getDataDefinition().getContainer());
		removeContainerReferences(template, container);
		return container;
	}

	private static void removeContainerReferences(ContentTemplate template, ContainerType container) {
		
		if (container != null) {

			String containerQId = container.getQualifiedId();
			
			TemplateContainerWalker templateWalker = new TemplateContainerWalker(template, 
					(cnt) -> !TemplateUtil.isLinkedContainer(cnt));
			
			templateWalker.walk((visitContainer) -> {

				// remove where this container is a linked container
				for (Iterator<ContainerType> itr = visitContainer.getContainer().iterator(); itr.hasNext(); ) {
					
					ContainerType subContainer = itr.next();
					
					if (TemplateUtil.isLinkedContainer(subContainer)
							&& subContainer.getLinkContainerQId().equals(container.getQualifiedId())) {
						itr.remove();
					}
				}
				
				// remove the content item where this container is the ref-list store
				for (Iterator<ContentItemType> itr = visitContainer.getContentItem().iterator(); itr.hasNext(); ) {

					ContentItemType ci = itr.next();
					
					if (ci.getBounded() != null && ci.getBounded().getRefList() != null
							&& ci.getBounded().getRefList().getDatastore() != null
							&& ci.getBounded().getRefList().getDatastore().getStoreId().equals(container.getQualifiedId())) {
						itr.remove();
					}
				}
				
				// remove UI Definition
				for (Iterator<ContentUIDefType> itr = template.getUiDefinition().getContentUIDef().iterator(); itr.hasNext(); ) {
					
					ContentUIDefType uiDef = itr.next();
					
					if (uiDef.getQualifiedId().equals(containerQId)
							|| uiDef.getQualifiedId().startsWith(containerQId + ".")) {
						itr.remove();
					}
				}
				
				// remove sub-container references
				for (ContainerType subCntnr : container.getContainer()) {
					if (!TemplateUtil.isLinkedContainer(subCntnr)) {
						removeContainerReferences(template, subCntnr);
					}
				}
				
			});
			
		}
	}
	
}
