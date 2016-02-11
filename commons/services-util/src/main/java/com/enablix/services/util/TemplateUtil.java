package com.enablix.services.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.util.QIdUtil;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.commons.xsdtopojo.ContainerPortalConfigType;
import com.enablix.core.commons.xsdtopojo.ContainerPortalConfigType.HeadingContentItem;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContainerUIDefType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.ContentUIDefType;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;
import com.enablix.core.commons.xsdtopojo.UiDefinitionType;

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
	
	public static List<String> getChildContainerIds(ContentTemplate template, String parentContainerQId) {
		
		ContainerType container = findContainer(template.getDataDefinition(), parentContainerQId);
		
		if (container == null) {
			LOGGER.error("Invalid containerQId [{}] for template [{}]", parentContainerQId, template.getId());
			throw new IllegalArgumentException("Invalid containerQId [" + parentContainerQId + "] for template ["
					+ template.getId() + "]");
		}
		
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
	
	private static ContainerType findReferenceableParentContainer(
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
	
	public static boolean hasOwnCollection(ContentTemplate template, String containerQId) {
		
		ContainerType container = findContainer(template.getDataDefinition(), containerQId);
		
		if (container == null) {
			LOGGER.error("Invalid containerQId [{}] for template [{}]", containerQId, template.getId());
			throw new IllegalArgumentException("Invalid containerQId [" + containerQId + "] for template ["
					+ template.getId() + "]");
		}
		
		return container.getQualifiedId().equals(containerQId) && container.isReferenceable();
	}
	
	public static String findParentCollectionName(ContentTemplate template, String containerQId) {
		String[] idHierarchy = QIdUtil.splitQId(containerQId);
		ContainerType parentContainer = findReferenceableParentContainer(
				template.getDataDefinition(), 
				Arrays.copyOfRange(idHierarchy, 0, idHierarchy.length - 1));
		return DatastoreUtil.getCollectionName(template.getId(), parentContainer);
	}
	
	public static String getQIdRelativeToParentContainer(ContentTemplate template, String contentQId) {
		ContainerType holdingContainer = findReferenceableParentContainer(
				template.getDataDefinition(), contentQId);
		
		if (holdingContainer == null) {
			LOGGER.error("No holding container found in template [{}] for content [{}]", 
					template.getId(), contentQId);
			throw new IllegalArgumentException("No holding container found in template [" 
					+ template.getId() + "] for content [" + contentQId + "]");
		}

		String holdingContainerQId = holdingContainer.getQualifiedId();
		if (!contentQId.startsWith(holdingContainerQId)) {
			LOGGER.error("Incorrect contentQId [{}] for parent QId [{}]", contentQId, holdingContainerQId);
			throw new IllegalStateException("Incorrect contentQId [" + contentQId 
					+ "] for parent QId [" + holdingContainerQId + "]");
		}
		
		return holdingContainerQId.equals(contentQId) ? "" :
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
		
		return cntUIDef;
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
	
	public static ContainerType findContainerForCollection(String collectionName, ContentTemplate template) {
		return findContainerForCollectionName(collectionName, 
				template.getDataDefinition().getContainer(), template.getId());
	}


	private static ContainerType findContainerForCollectionName(String collectionName, 
			List<ContainerType> containers, String templateId) {
		
		for (ContainerType container : containers) {
			
			String cntnrCollName = DatastoreUtil.getCollectionName(templateId, container);
			
			if (cntnrCollName.equals(collectionName)) {
				return container;
			}
		}
		
		for (ContainerType container : containers) {
			
			ContainerType matchedContainer = findContainerForCollectionName(
					collectionName, container.getContainer(), templateId);
			
			if (matchedContainer != null) {
				return matchedContainer;
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
	
}
