package com.enablix.services.util.template.walker;

import org.springframework.util.Assert;

import com.enablix.core.commons.xsdtopojo.BoundedRefListType;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

/**
 * This container filter looks for container which have a bounded reference list attribute where
 * the store id of the reference list points to a specific container
 * 
 * @author dikshit.luthra
 *
 */
public class BoundedListFilter implements ContainerFilter {

	// The reference container that is used as a data store in the container to be filtered
	private ContainerType boundedListRefContainer;
	
	public BoundedListFilter(ContainerType boundedListRefContainer) {
		Assert.notNull(boundedListRefContainer, "Container Type cannot be null");
		this.boundedListRefContainer = boundedListRefContainer;
	}
	
	@Override
	public boolean accept(ContainerType container) {
		
		String boundedRefListStoreId = boundedListRefContainer.getId();
		
		// find the content item in the container where the updated record's container is used
		for (ContentItemType contentItem : container.getContentItem()) {
			
			if (contentItem.getType() == ContentItemClassType.BOUNDED) {
				
				BoundedType boundedItem = contentItem.getBounded();
				if (boundedItem != null) {
				
					BoundedRefListType boundedRefList = boundedItem.getRefList();
					if (boundedRefList != null && boundedRefList.getDatastore().getStoreId().equals(boundedRefListStoreId)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
