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
			quickLinkSection.getLinks().add(link);
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
	
	public void addQuickLinkSection(QuickLinkCategory category, String clientName) {
		
		QuickLinkSection linkSection = findQuickLinkSection(category.getIdentity());
		
		if (linkSection == null) {
			
			QuickLinkSection qlSection = 
					new QuickLinkSection(category.getName(), category.getIdentity());
			qlSection.setClientId(category.getClientId());
			qlSection.setClientName(clientName);
			qlSection.setOrder(category.getOrder());
		
			sections.add(qlSection);
		}
	}
	
	public static class Link {
		
		private String categoryIdentity;
		private String quickLinkIdentity;
		private int order;
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

		public String getQuickLinkIdentity() {
			return quickLinkIdentity;
		}

		public void setQuickLinkIdentity(String quickLinkIdentity) {
			this.quickLinkIdentity = quickLinkIdentity;
		}

		public int getOrder() {
			return order;
		}

		public void setOrder(int order) {
			this.order = order;
		}
	}
	
}
