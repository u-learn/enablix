package com.enablix.services.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.core.api.TemplateFacade;
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
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.QualityRuleConfigType;
import com.enablix.core.commons.xsdtopojo.QualityRulesType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;
import com.enablix.core.commons.xsdtopojo.UserProfileRefType;

public class TemplateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateUtil.class);
	
	public static boolean isReferenceable(DataDefinitionType dataDef, String containerQId) {
		ContainerType container = findContainer(dataDef, containerQId);
		return container != null ? container.isReferenceable() : false;
	}
	
	
	public static ContainerType findContainer(DataDefinitionType dataDef, String containerQId) {
		String[] idHierarchy = QIdUtil.splitQId(containerQId);
		
		List<ContainerType> containers = dataDef.getContainer();
		ContainerType container = null;
		
		for (String id : idHierarchy) {
			
			container = findContainer(containers, id);
			
			if (container == null) {
				break;
			}
			
			containers = container.getContainer();
		}
		
		return container;
	}
	

	public static List<String> getChildContainerIds(ContainerType container) {
		List<String> childContainerIds = new ArrayList<>();
		for (ContainerType child : container.getContainer()) {
			childContainerIds.add(child.getId());
		}
		
		return childContainerIds;
	}
	
	private static ContainerType findContainer(List<ContainerType> containers, String containerId) {
		
		if (containers != null) {
		
			for (ContainerType cntnr : containers) {
				if (cntnr.getId().equals(containerId)) {
					return cntnr;
				}
			}
		}
		
		return null;
	}
	
	public static ContainerType findReferenceableParentContainer(
			DataDefinitionType dataDef, String containerQId) {
		
		String[] idHierarchy = QIdUtil.splitQId(containerQId);
		return findReferenceableParentContainer(dataDef, idHierarchy);
	}
	
	private static ContainerType findReferenceableParentContainer(
			DataDefinitionType dataDef, String[] idHierarchy) {
		
		List<ContainerType> containers = dataDef.getContainer();
		ContainerType refContainer = null;
		ContainerType container = null;
		
		for (String id : idHierarchy) {

			container = findContainer(containers, id);
			
			if (container == null) {
				break;
			}
			
			if (container.isReferenceable()) {
				refContainer = container;
			}
			
			containers = container.getContainer();
		}
		
		if (refContainer != null && !StringUtil.isEmpty(refContainer.getLinkContainerQId())) {
			refContainer = findReferenceableParentContainer(dataDef, refContainer.getLinkContainerQId());
		}
		
		return refContainer;		
	}
	
	public static String resolveCollectionName(ContentTemplate template, String containerQId) {
		ContainerType holdingContainer = findReferenceableParentContainer(
											template.getDataDefinition(), containerQId);
		return DatastoreUtil.getCollectionName(template.getId(), holdingContainer);
	}
	
	public static boolean isRootContainer(ContentTemplate template, String containerQId) {
		for (ContainerType container : template.getDataDefinition().getContainer()) {
			if (container.getQualifiedId().equals(containerQId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @deprecated use {@link #hasOwnCollection(ContainerType)} instead
	 */
	public static boolean hasOwnCollection(ContentTemplate template, String containerQId) {
		
		ContainerType container = findContainer(template.getDataDefinition(), containerQId);
		
		if (container == null) {
			LOGGER.error("Invalid containerQId [{}] for template [{}]", containerQId, template.getId());
			throw new IllegalArgumentException("Invalid containerQId [" + containerQId + "] for template ["
					+ template.getId() + "]");
		}
		
		return container.getQualifiedId().equals(containerQId) && container.isReferenceable();
	}
	
	public static boolean hasOwnCollection(ContainerType container) {
		Assert.notNull(container, "Container object in null");
		return container.isReferenceable();
	}
	
	public static String findParentCollectionName(TemplateFacade template, String containerQId) {
		String parentQId = QIdUtil.getParentQId(containerQId);
		return template.getCollectionName(parentQId);
	}
	
	public static String getQIdRelativeToParentContainer(ContentTemplate template, String contentQId) {
		
		String holdingContainerQId = contentQId;
		
		if (QIdUtil.hasParent(contentQId)) {
			
			ContainerType holdingContainer = findReferenceableParentContainer(
					template.getDataDefinition(), contentQId);
			
			if (holdingContainer == null) {
				LOGGER.error("No holding container found in template [{}] for content [{}]", 
						template.getId(), contentQId);
				throw new IllegalArgumentException("No holding container found in template [" 
						+ template.getId() + "] for content [" + contentQId + "]");
			}
			
			holdingContainerQId = holdingContainer.getQualifiedId();
			
		}
		
		return holdingContainerQId.equals(contentQId) || !contentQId.startsWith(holdingContainerQId) ? "" :
				// substring - +1 for excluding '.' after holding container QId
				contentQId.substring(holdingContainerQId.length() + 1, contentQId.length());
		
	}


	public static String getPortalLabelAttributeId(ContentTemplate template, String qId) {
		
		ContentUIDefType contentUIDef = getContentUIDef(template, qId);
		
		if (contentUIDef != null) {
			
			ContainerUIDefType containerUIDef = contentUIDef.getContainer();
			if (containerUIDef != null) {
			
				ContainerPortalConfigType portalConfig = containerUIDef.getPortalConfig();
				if (portalConfig != null) {
				
					HeadingContentItem headingContentItem = portalConfig.getHeadingContentItem();
					if (headingContentItem != null) {
						return headingContentItem.getId();
					}
				}
				
				// check container label instead
				String labelQualifiedId = containerUIDef.getLabelQualifiedId();
				if (!StringUtil.isEmpty(labelQualifiedId)) {
					return QIdUtil.getElementId(labelQualifiedId);
				}
				
			}
		}
		
		return null;
	}
	
	public static ContentUIDefType getContentUIDef(ContentTemplate template, String qId) {
		
		ContentUIDefType cntUIDef = null;
		UiDefinitionType uiDefinition = template.getUiDefinition();
		
		for (ContentUIDefType contentUIDef : uiDefinition.getContentUIDef()) {
			if (contentUIDef.getQualifiedId().equals(qId)) {
				cntUIDef = contentUIDef;
				break;
			}
		}
		
		// check if this is a linked container, then look for linked container's UI def
		if (cntUIDef == null) {
			ContainerType containerDef = findContainer(template.getDataDefinition(), qId);
			if (containerDef == null) {
				LOGGER.warn("Qualified Id [{}] used in template but definition not found", qId);
			} else if (isLinkedContainer(containerDef)) {
				cntUIDef = getContentUIDef(template, containerDef.getLinkContainerQId());
			}
		}
		
		return cntUIDef;
	}
	
	public static boolean isLinkedContainer(ContainerType containerDef) {
		return !StringUtil.isEmpty(containerDef.getLinkContainerQId());
	}


	public static String getStudioLabelAttributeId(ContentTemplate template, String qId) {
		
		ContentUIDefType contentUIDef = getContentUIDef(template, qId);
		
		if (contentUIDef != null) {
			
			ContainerUIDefType containerUIDef = contentUIDef.getContainer();
			if (containerUIDef != null) {
			
				// check container label instead
				String labelQualifiedId = containerUIDef.getLabelQualifiedId();
				if (!StringUtil.isEmpty(labelQualifiedId)) {
					return QIdUtil.getElementId(labelQualifiedId);
				}
				
			}
		}
		
		return null;
	}
	
	public static List<String> findAllCollectionNames(ContentTemplate template) {
		return findCollectionNames(template.getDataDefinition().getContainer(), template.getId(), true, true);
	}
	
	private static List<String> findCollectionNames(List<ContainerType> containers, String templateId,
			boolean includeRefDataColl, boolean includeContentDataColl) {
		
		List<String> collectionNames = new ArrayList<>();
		
		for (ContainerType container : containers) {
			
			if ((includeRefDataColl && container.isRefData())
					|| (includeContentDataColl && !container.isRefData())) {
			
				collectionNames.add(DatastoreUtil.getCollectionName(templateId, container));
				collectionNames.addAll(findCollectionNames(container.getContainer(), templateId, 
					includeRefDataColl, includeContentDataColl));
			}
		}
		
		return collectionNames;
	}
	
	public static List<String> findContentCollectionNames(ContentTemplate template) {
		return findCollectionNames(template.getDataDefinition().getContainer(), template.getId(), false, true);
	}

	public static String checkAndGetLinkedContainerQId(ContentTemplate template, String containerQId) {
		
		String linkedContainerQId = null;
		
		ContainerType container = findContainer(template.getDataDefinition(), containerQId);
		
		if (container != null) {
			linkedContainerQId = container.getLinkContainerQId();
		}
		
		return linkedContainerQId;
	}
	
	public static List<ContainerType> findContainersWithDatastoreAssociation(
					ContentTemplate inTemplate, String forContainerQId) {
		
		List<ContainerType> containers = new ArrayList<>();
		
		List<ContainerType> allContainers = inTemplate.getDataDefinition().getContainer();
		containers.addAll(findContainersWithDatastoreAssociation(
							allContainers, QIdUtil.getElementId(forContainerQId)));
		
		return containers;
	}
	
	public static ContainerBusinessCategoryType getContainerBusinessCategory(ContentTemplate template, String containerQId) {
		ContainerType containerDef = findContainer(template.getDataDefinition(), containerQId);
		return containerDef == null ? null : containerDef.getBusinessCategory();
	}
	
	private static List<ContainerType> findContainersWithDatastoreAssociation(
			List<ContainerType> inContainers, String forContainerId) {
		
		List<ContainerType> containers = new ArrayList<>();
		
		for (ContainerType cntnr : inContainers) {
			
			for (ContentItemType contentItem : cntnr.getContentItem()) {
			
				if (contentItem.getType() == ContentItemClassType.BOUNDED) {
				
					BoundedType boundedItem = contentItem.getBounded();
					if (boundedItem != null) {
					
						BoundedRefListType boundedRefList = boundedItem.getRefList();
						if (boundedRefList != null) {
							if (boundedRefList.getDatastore().getStoreId().equals(forContainerId)) {
								containers.add(cntnr);
							}
						}
					}
				}
			}
			
			containers.addAll(findContainersWithDatastoreAssociation(cntnr.getContainer(), forContainerId));
		}
		
		return containers;
		
	}

	public static String getUserContainerQId(ContentTemplate template) {
		
		UserProfileRefType userProfileRef = template.getDataDefinition().getUserProfileRef();
		
		if (userProfileRef == null) {
			return ContentDataConstants.DEFAULT_USER_CONTAINER_QID;
		}
		
		return userProfileRef.getContainerQId();
	}
	
	public static String getUserContainerEmailAttrId(ContentTemplate template) {
		
		UserProfileRefType userProfileRef = template.getDataDefinition().getUserProfileRef();
		
		if (userProfileRef == null) {
			return ContentDataConstants.DEFAULT_USER_CONTAINER_EMAIL_ATTR_ID;
		}
		
		return userProfileRef.getEmailAttributeId();
	}
	
	public static ContentItemType findContentItem(ContainerType container, String contentItemId) {
		for (ContentItemType item : container.getContentItem()) {
			if (item.getId().equals(contentItemId)) {
				return item;
			}
		}
		return null;
	}


	public static boolean hasContentStackItem(ContainerType containerType) {
		for (ContentItemType itemType : containerType.getContentItem()) {
			if (itemType.getType() == ContentItemClassType.CONTENT_STACK) {
				return true;
			}
		}
		return false;
	}


	public static List<ContentItemType> getContentStackItems(ContainerType containerType) {
		List<ContentItemType> contentStackItems = new ArrayList<>();
		for (ContentItemType itemType : containerType.getContentItem()) {
			if (itemType.getType() == ContentItemClassType.CONTENT_STACK) {
				contentStackItems.add(itemType);
			}
		}
		return contentStackItems;
	}


	public static ContentItemType findMatchingContentItem(ContainerType containerDef, ContentItemType dsAttr) {
		
		ContentItemType matchedContentItem = null;
		
		if (containerDef != null) {
			
			BoundedListDatastoreType segmentAttrDS = checkAndGetBoundedRefListDatastore(dsAttr);
			
			for (ContentItemType contentItem : containerDef.getContentItem()) {

				if (segmentAttrDS != null) {
					
					BoundedListDatastoreType ciDatastore = checkAndGetBoundedRefListDatastore(contentItem);
					
					if (ciDatastore != null && segmentAttrDS.getStoreId().equals(ciDatastore.getStoreId())
							&& segmentAttrDS.getLocation() == ciDatastore.getLocation()) {
						
						matchedContentItem = contentItem;
					}
					
				}

				if (matchedContentItem == null && contentItem.getId().equals(dsAttr.getId())) {
					matchedContentItem = contentItem;
				}
			}
		}
		
		return matchedContentItem;
		
	}


	public static BoundedListDatastoreType checkAndGetBoundedRefListDatastore(ContentItemType contentItem) {
		
		if (contentItem.getType() == ContentItemClassType.BOUNDED) {
		
			BoundedType bounded = contentItem.getBounded();
			
			if (bounded.getRefList() != null) {
			
				return bounded.getRefList().getDatastore();
				
			}
		}
		
		return null;
	}


	public static boolean hasQualityCheckRules(ContentTemplate template) {
		
		QualityRuleConfigType qualityRuleConfig = template.getQualityRuleConfig();
		
		if (qualityRuleConfig != null) {
		
			QualityRulesType qualityRules = qualityRuleConfig.getQualityRules();
			if (qualityRules != null) {
				return CollectionUtil.isNotEmpty(qualityRules.getRule());
			}
		}
		
		return false;
	}
	
}
