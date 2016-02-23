package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.List;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.domain.links.QuickLinkCategory;

public class QuickLinks {

	private List<QuickLinkSection> sections = new ArrayList<>();
	
	public QuickLinks() {
		
	}

	public List<QuickLinkSection> getSections() {
		return sections;
	}

	public void addLink(Link link) {
		QuickLinkSection quickLinkSection = findQuickLinkSection(link.getCategoryIdentity());
		if (quickLinkSection != null) {
			quickLinkSection.getLinks().add(link.getData());
		}
	}
	
	private QuickLinkSection findQuickLinkSection(String categoryIdentity) {
		
		for (QuickLinkSection section : sections) {
			if (section.getSectionIdentity().equals(categoryIdentity)) {
				return section;
			}
		}
		
		return null;
	}
	
	public void addQuickLinkSection(QuickLinkCategory category) {
		QuickLinkSection linkSection = findQuickLinkSection(category.getIdentity());
		if (linkSection == null) {
			sections.add(new QuickLinkSection(category.getName(), category.getIdentity()));
		}
	}
	
	public static class Link {
		
		private String categoryIdentity;
		private NavigableContent data;
		
		public String getCategoryIdentity() {
			return categoryIdentity;
		}
		
		public NavigableContent getData() {
			return data;
		}
		
		public void setCategoryIdentity(String sectionName) {
			this.categoryIdentity = sectionName;
		}
		
		public void setData(NavigableContent data) {
			this.data = data;
		}
	}
	
}
