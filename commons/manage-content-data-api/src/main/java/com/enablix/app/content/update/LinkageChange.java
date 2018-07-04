package com.enablix.app.content.update;

import java.util.List;

import com.enablix.core.api.ContentStackItem;

public class LinkageChange {

	private String linkContainerQId;
	private List<ContentStackItem> newLinks;
	private List<ContentStackItem> removedLinks;
	
	public String getLinkContainerQId() {
		return linkContainerQId;
	}
	
	public void setLinkContainerQId(String linkContainerQId) {
		this.linkContainerQId = linkContainerQId;
	}
	
	public List<ContentStackItem> getNewLinks() {
		return newLinks;
	}
	
	public void setNewLinks(List<ContentStackItem> newLinks) {
		this.newLinks = newLinks;
	}
	
	public List<ContentStackItem> getRemovedLinks() {
		return removedLinks;
	}
	
	public void setRemovedLinks(List<ContentStackItem> removedLinks) {
		this.removedLinks = removedLinks;
	}
	
	
}
