package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.Collection;

import com.enablix.app.content.ui.NavigableContent;

public class QuickLinkSection {

	private String sectionName;
	
	private Collection<NavigableContent> links;

	public QuickLinkSection(String sectionName) {
		this.sectionName = sectionName;
		this.links = new ArrayList<>();
	}
	
	public String getSectionName() {
		return sectionName;
	}

	public Collection<NavigableContent> getLinks() {
		return links;
	}

}
