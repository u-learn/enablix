package com.enablix.app.mail.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.commons.xsdtopojo.ContainerType;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.mail.BasicEmailVelocityInput;

public class UserContentAccessActivityMailInput extends BasicEmailVelocityInput {

	private UserProfile actor;
	
	private Map<String, ContentAccessBucket> contentAccess;
	
	public UserProfile getActor() {
		return actor;
	}

	public void setActor(UserProfile actor) {
		this.actor = actor;
	}

	public UserContentAccessActivityMailInput() {
		this.contentAccess = new HashMap<>();
	}
	
	public Map<String, ContentAccessBucket> getContentAccess() {
		return contentAccess;
	}

	public void setContentAccess(Map<String, ContentAccessBucket> contentAccess) {
		this.contentAccess = contentAccess;
	}
	
	public void addContentAccess(ContentDataRef content, TemplateFacade template) {
		
		String containerQId = content.getContainerQId();
		
		ContentAccessBucket bucket = contentAccess.get(containerQId);
		
		if (bucket == null) {
			
			ContainerType containerDef = template.getContainerDefinition(containerQId);

			if (containerDef != null) {
				
				bucket = new ContentAccessBucket();
				bucket.setContentQId(containerQId);
				bucket.setContentLabel(containerDef.getLabel());
				
				contentAccess.put(containerQId, bucket);
			}
			
		}
		
		if (bucket != null) {
			bucket.getContentList().add(content);
		}
	}

	public static class ContentAccessBucket {
		
		private String contentQId;
		
		private String contentLabel;
		
		private List<ContentDataRef> contentList;
		
		public ContentAccessBucket() {
			super();
			this.contentList = new ArrayList<>();
		}

		public String getContentQId() {
			return contentQId;
		}

		public void setContentQId(String contentQId) {
			this.contentQId = contentQId;
		}

		public String getContentLabel() {
			return contentLabel;
		}

		public void setContentLabel(String contentLabel) {
			this.contentLabel = contentLabel;
		}

		public List<ContentDataRef> getContentList() {
			return contentList;
		}

		public void setContentList(List<ContentDataRef> contentList) {
			this.contentList = contentList;
		}

	}

}
