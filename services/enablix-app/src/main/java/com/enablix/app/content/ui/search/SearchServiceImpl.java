package com.enablix.app.content.ui.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.search.SearchClient;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;

@Component
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchClient searchClient;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public List<NavigableContent> search(String searchText) {
		
		List<ContentDataRef> searchResult = searchClient.search(
				searchText, ProcessContext.get().getTemplateId());
		
		List<NavigableContent> result = new ArrayList<>();
		
		if (searchResult != null) {
			for (ContentDataRef dataRef : searchResult) {
				result.add(navContentBuilder.build(dataRef, labelResolver));
			}
		}
		
		return result;
	}

}
