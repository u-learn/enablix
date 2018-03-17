package com.enablix.core.domain.content;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.enablix.core.commons.xsdtopojo.ContainerBusinessCategoryType;
import com.enablix.core.domain.BaseDocumentEntity;

@Document(collection = "ebx_user_content_relevance")
public class UserContentRelevance extends BaseDocumentEntity {
	
	private String userId;
	
	private String contentQId;
	
	private String containerLabel;
	
	private String contentIdentity;
	
	private String contentTitle;
	
	private ContainerBusinessCategoryType bizCategory;
	
	private float relevance;
	
	private Map<String, NodeRelevance> nodeRelevance;
	
	private boolean latest;
	
	private Date asOfDate;

	public UserContentRelevance(String userId, String contentQId, String containerLabel, 
			String contentIdentity, String contentTitle, ContainerBusinessCategoryType bizCategory, 
			float relevance, Date asOfDate) {
		
		super();
		
		this.userId = userId;
		this.contentQId = contentQId;
		this.containerLabel = containerLabel;
		this.contentIdentity = contentIdentity;
		this.contentTitle = contentTitle;
		this.bizCategory = bizCategory;
		this.relevance = relevance;
		this.asOfDate = asOfDate;
		this.latest = true;
		this.nodeRelevance = new HashMap<>();
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContentQId() {
		return contentQId;
	}

	public void setContentQId(String contentQId) {
		this.contentQId = contentQId;
	}
	
	public String getContainerLabel() {
		return containerLabel;
	}

	public void setContainerLabel(String containerLabel) {
		this.containerLabel = containerLabel;
	}

	public String getContentIdentity() {
		return contentIdentity;
	}

	public void setContentIdentity(String contentIdentity) {
		this.contentIdentity = contentIdentity;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	
	public ContainerBusinessCategoryType getBizCategory() {
		return bizCategory;
	}

	public void setBizCategory(ContainerBusinessCategoryType type) {
		this.bizCategory = type;
	}

	public float getRelevance() {
		return relevance;
	}

	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}

	public Map<String, NodeRelevance> getNodeRelevance() {
		return nodeRelevance;
	}

	public void setNodeRelevance(Map<String, NodeRelevance> nodeRelevance) {
		this.nodeRelevance = nodeRelevance;
	}

	public void addNodeRelevance(String nodeName, String state, float nodeRelevanceValue) {
		this.nodeRelevance.put(nodeStateKey(nodeName, state), new NodeRelevance(nodeName, state, nodeRelevanceValue));
	}
	
	public NodeRelevance getNodeRelevance(String nodeName, String state) {
		return this.nodeRelevance.get(nodeStateKey(nodeName, state));
	}

	public boolean isLatest() {
		return latest;
	}

	public void setLatest(boolean latest) {
		this.latest = latest;
	}
	
	public Date getAsOfDate() {
		return asOfDate;
	}

	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	@Override
	public String toString() {
		return "UserContentRelevance [userId=" + userId + ", contentQId=" + contentQId + ", containerLabel="
				+ containerLabel + ", contentIdentity=" + contentIdentity + ", contentTitle=" + contentTitle
				+ ", type=" + bizCategory + ", relevance=" + relevance + ", nodeRelevance=" + nodeRelevance + "]";
	}
	
	public static class NodeRelevance {
		
		private String nodeName;
		
		private String state;
		
		private float relevance;
		
		public NodeRelevance() {}
		
		public NodeRelevance(String nodeName, String state, float relevance) {
			super();
			this.nodeName = nodeName;
			this.state = state;
			this.relevance = relevance;
		}

		public String getNodeName() {
			return nodeName;
		}

		public void setNodeName(String nodeName) {
			this.nodeName = nodeName;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public float getRelevance() {
			return relevance;
		}

		public void setRelevance(float relevance) {
			this.relevance = relevance;
		}

		@Override
		public String toString() {
			return "NodeRelevance [nodeName=" + nodeName + ", state=" + state + ", relevance=" + relevance + "]";
		}
		
	}
	
	public static String nodeStateKey(String nodeName, String state) {
		return nodeName + "-" + state;
	}

}