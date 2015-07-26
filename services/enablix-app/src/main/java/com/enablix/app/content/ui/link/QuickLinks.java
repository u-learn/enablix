package com.enablix.app.content.ui.link;

import java.util.ArrayList;
import java.util.List;

import com.enablix.app.content.ui.NavigableContent;

public class QuickLinks {

	private List<QuickLinkSection> sections = new ArrayList<>();
	
	public QuickLinks() {
		
	}

	public List<QuickLinkSection> getSections() {
		return sections;
	}

	public void addLink(Link link) {
		QuickLinkSection quickLinkSection = findQuickLinkSection(link.getSectionName());
		quickLinkSection.getLinks().add(link.getData());
	}
	
	private QuickLinkSection findQuickLinkSection(String sectionName) {
		
		for (QuickLinkSection section : sections) {
			if (section.getSectionName().equals(sectionName)) {
				return section;
			}
		}
		
		QuickLinkSection section = new QuickLinkSection(sectionName);
		sections.add(section);
		
		return section;
	}
	
	public static class Link {
		
		private String sectionName;
		private NavigableContent data;
		
		public String getSectionName() {
			return sectionName;
		}
		
		public NavigableContent getData() {
			return data;
		}
		
		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}
		
		public void setData(NavigableContent data) {
			this.data = data;
		}
	}
	
}
