package com.enablix.services.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enablix.commons.util.QIdUtil;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.core.commons.xsdtopojo.DataDefinitionType;

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
	
}
