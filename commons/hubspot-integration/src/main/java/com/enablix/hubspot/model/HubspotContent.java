package com.enablix.hubspot.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class HubspotContent {
	
	private long objectId;
	
	private String title;
	
	private String link;
	
	private String contentType;
	
	private Link downloadLink;
	
	private Link extUrlLink;
	
	private List<HubspotAction> actions;
	
	public HubspotContent() {
		this.actions = new ArrayList<>();
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Link getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(Link downloadLink) {
		this.downloadLink = downloadLink;
	}

	public Link getExtUrlLink() {
		return extUrlLink;
	}

	public void setExtUrlLink(Link extUrlLink) {
		this.extUrlLink = extUrlLink;
	}

	public List<HubspotAction> getActions() {
		return actions;
	}

	public void setActions(List<HubspotAction> actions) {
		this.actions = actions;
	}
	
	public static class Link {
		
		private String value;
		
		private String linkLabel;
		
		public Link(String value, String linkLabel) {
			super();
			this.value = value;
			this.linkLabel = linkLabel;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getLinkLabel() {
			return linkLabel;
		}

		public void setLinkLabel(String linkLabel) {
			this.linkLabel = linkLabel;
		}
		
	}
	
}