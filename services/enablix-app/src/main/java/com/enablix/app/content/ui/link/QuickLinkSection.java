package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.Collection;

import com.enablix.app.content.ui.link.QuickLinks.Link;

public class QuickLinkSection {

	private String sectionName;
	
	private String sectionIdentity;
	
	private String clientId;
	
	private String clientName;
	
	private Collection<Link> links;
	
	private int order;

	public QuickLinkSection(String sectionName, String sectionIdentity) {
		this.sectionName = sectionName;
		this.sectionIdentity = sectionIdentity;
		this.links = new ArrayList<>();
		this.order = 0;
	}
	
	public String getSectionName() {
		return sectionName;
	}

	public String getSectionIdentity() {
		return sectionIdentity;
	}

	public Collection<Link> getLinks() {
		return links;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
