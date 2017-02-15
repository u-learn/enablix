package com.enablix.core.domain.content.kit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_content_kit")
public class ContentKit extends BaseDocumentEntity {

	private String name;
	
	private List<ContentDataRef> contentList;
	
	// identity of the linked kits
	private List<String> linkedKits;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ContentDataRef> getContentList() {
		return contentList;
	}

	public void setContentList(List<ContentDataRef> contentList) {
		this.contentList = contentList;
	}

	public List<String> getLinkedKits() {
		return linkedKits;
	}

	public void setLinkedKits(List<String> linkedKits) {
		this.linkedKits = linkedKits;
	}
	
	public ContentKitSummary toSummary() {
		ContentKitSummary contentKitSummary = new ContentKitSummary();
		contentKitSummary.setIdentity(getIdentity());
		contentKitSummary.setCreatedAt(getCreatedAt());
		contentKitSummary.setCreatedBy(getCreatedBy());
		contentKitSummary.setCreatedByName(getCreatedByName());
		contentKitSummary.setModifiedAt(getModifiedAt());
		contentKitSummary.setModifiedBy(getModifiedBy());
		contentKitSummary.setModifiedByName(getModifiedByName());
		contentKitSummary.setName(getName());
		return contentKitSummary;
	}
	
	public ContentKitRef toRef() {
		ContentKitRef contentKitRef = new ContentKitRef();
		contentKitRef.setIdentity(getIdentity());
		contentKitRef.setName(getName());
		return contentKitRef;
	}


	public static class DisplaySetting {
		
		private Map<String, Integer> contentTypeDisplayOrder;
		
		public DisplaySetting() {
			this.contentTypeDisplayOrder = new HashMap<>();
		}

		public Map<String, Integer> getContentTypeDisplayOrder() {
			return contentTypeDisplayOrder;
		}

		public void setContentTypeDisplayOrder(Map<String, Integer> contentTypeDisplayOrder) {
			this.contentTypeDisplayOrder = contentTypeDisplayOrder;
		}
		
	}
	
}
