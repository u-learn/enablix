package com.enablix.analytics.info.detection;

import java.util.HashSet;
import java.util.Set;

import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.BoundedListDatastoreType;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;
import com.enablix.core.commons.xsdtopojo.DatastoreLocationType;
import com.enablix.services.util.TemplateUtil;

public class InfoDetectorHelper {
	
	public static LookupCollectionAndContainers getLinkedContentCollections(Assessment assessment, TemplateFacade templateFacade) {
		
		LookupCollectionAndContainers lookups = new LookupCollectionAndContainers();
		
		for (TypeOpinion typeOp : assessment.getTypeOpinions()) {
			
			ContainerType container = templateFacade.getContainerDefinition(typeOp.getContainerQId());
			
			if (container != null) {
			
				for (ContentItemType contentItem : container.getContentItem()) {
				
					BoundedListDatastoreType refListDS = TemplateUtil.checkAndGetBoundedRefListDatastore(contentItem);
					
					if (refListDS != null && refListDS.getLocation() == DatastoreLocationType.CONTENT) {
					
						String collectionName = templateFacade.getCollectionName(refListDS.getStoreId());
						if (StringUtil.hasText(collectionName)) {
							lookups.entries.add(new ContainerAndCollection(collectionName, refListDS.getStoreId()));
						}
					}
				}
			}
		}
		
		return lookups;
	}
	
	public static class LookupCollectionAndContainers {
		
		private Set<ContainerAndCollection> entries = new HashSet<>();

		public Set<ContainerAndCollection> getEntries() {
			return entries;
		}
		
	}
	
	public static class ContainerAndCollection {
		
		private String collection;
		private String containerQId;
		
		ContainerAndCollection(String collection, String containerQId) {
			super();
			this.collection = collection;
			this.containerQId = containerQId;
		}

		public String getCollection() {
			return collection;
		}

		public String getContainerQId() {
			return containerQId;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((containerQId == null) ? 0 : containerQId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ContainerAndCollection other = (ContainerAndCollection) obj;
			if (containerQId == null) {
				if (other.containerQId != null)
					return false;
			} else if (!containerQId.equals(other.containerQId))
				return false;
			return true;
		}
		
	}
}
