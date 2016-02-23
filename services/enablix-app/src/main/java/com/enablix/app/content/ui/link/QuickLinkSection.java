package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.Collection;

import com.enablix.app.content.ui.NavigableContent;

public class QuickLinkSection {

	private String sectionName;
	
	private String sectionIdentity;
	
	private Collection<NavigableContent> links;

	public QuickLinkSection(String sectionName, String sectionIdentity) {
		this.sectionName = sectionName;
		this.sectionIdentity = sectionIdentity;
		this.links = new ArrayList<>();
	}
	
	public String getSectionName() {
		return sectionName;
	}

	public String getSectionIdentity() {
		return sectionIdentity;
	}

	public Collection<NavigableContent> getLinks() {
		return links;
	}

}
