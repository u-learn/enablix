package com.enablix.wordpress.info.mail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolver;
import com.enablix.core.mail.velocity.VelocityTemplateInputResolverFactory;
import com.enablix.data.view.DataView;

@Component
public class WPImportMailInputBuilder {

	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	@Autowired
	private VelocityTemplateInputResolverFactory resolverFactory;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	public WordpressImportMailInput build(List<ContentDataRef> contentRefs, DataView view) {
		
		WordpressImportMailInput input = new WordpressImportMailInput();
		
		List<NavigableContent> navContents = new ArrayList<>();
		
		contentRefs.forEach((contentRef) -> {
			NavigableContent navContent = navContentBuilder.build(contentRef, labelResolver);
			if (navContent != null) {
				navContents.add(navContent);
			}
		});

		input.setImportedContent(navContents);
		
		Collection<VelocityTemplateInputResolver<WordpressImportMailInput>> resolvers = 
				resolverFactory.getResolvers(input);
		
		for (VelocityTemplateInputResolver<WordpressImportMailInput> resolver : resolvers) {
			resolver.work(input, view);
		}
		
		return input;
		
	}
	
}