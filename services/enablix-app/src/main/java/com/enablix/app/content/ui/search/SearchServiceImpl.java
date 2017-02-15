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
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;

@Component
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchClient searchClient;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	@Autowired
	private TemplateManager templateMgr;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public SearchResult<NavigableContent> search(String searchText, int pageSize, int pageNum) {
		
		String templateId = ProcessContext.get().getTemplateId();
		SearchResult<ContentDataRef> searchResult = searchClient.search(
				searchText, templateMgr.getTemplateWrapper(templateId), pageSize, pageNum);
		
		List<NavigableContent> content = new ArrayList<>();
		
		if (searchResult != null) {
			for (ContentDataRef dataRef : searchResult.getContent()) {
				NavigableContent resultItem = navContentBuilder.build(dataRef, labelResolver);
				if (resultItem != null) {
					content.add(resultItem);
				}
			}
		}
		
		SearchResult<NavigableContent> result = new SearchResult<NavigableContent>();
		result.setContent(content);
		result.setNumberOfElements(searchResult.getNumberOfElements());
		result.setPageSize(searchResult.getPageSize());
		result.setCurrentPage(pageNum);
		result.setTotalPages(searchResult.getTotalPages());
		
		return result;
	}

}
