package com.enablix.app.content.ui.link;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.content.ui.link.repo.QuickLinkContentRepository;
import com.enablix.core.domain.links.QuickLinkContent;

@Component
public class QuickLinksServiceImpl implements QuickLinksService {

	@Autowired
	private QuickLinkContentRepository repo;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public QuickLinks getQuickLinks() {
		
		List<QuickLinkContent> allContent = repo.findAll();
		
		QuickLinks quickLinks = new QuickLinks();
		
		for (QuickLinkContent linkContent : allContent) {
			
			NavigableContent navContent = navContentBuilder.build(linkContent.getData(), labelResolver);
			
			QuickLinks.Link link = new QuickLinks.Link();
			link.setSectionName(linkContent.getSectionName());
			link.setData(navContent);
			
			quickLinks.addLink(link);
		}
		
		return quickLinks;
	}

}
