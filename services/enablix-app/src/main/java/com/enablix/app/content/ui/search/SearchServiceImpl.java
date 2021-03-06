package com.enablix.app.content.ui.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.analytics.search.SearchClient;
import com.enablix.analytics.search.SearchInput;
import com.enablix.app.content.label.ContentLabelResolver;
import com.enablix.app.content.label.PortalContentLabelResolver;
import com.enablix.app.content.share.DocUnsecureAccessUrlPopulator;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentBuilder;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.NavigableContentBuilder;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;

@Component
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchClient searchClient;
	
	@Autowired
	private NavigableContentBuilder navContentBuilder;
	
	@Autowired
	private DisplayableContentBuilder dsContentBuilder;
	
	@Autowired
	private DocUnsecureAccessUrlPopulator docUrlPopulator;
	
	@Autowired
	private TemplateManager templateMgr;
	
	private ContentLabelResolver labelResolver = new PortalContentLabelResolver();
	
	@Override
	public SearchResult<NavigableContent> search(
			String searchText, SearchInput scope, int pageSize, int pageNum, DataView dataView) {
		
		String templateId = ProcessContext.get().getTemplateId();
		SearchResult<ContentDataRef> searchResult = searchClient.search(searchText, scope,
				templateMgr.getTemplateFacade(templateId), pageSize, pageNum, dataView);
		
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
	
	@Override
	public SearchResult<DisplayableContent> searchAndGetResultAsDisplayContent(String searchText, 
			SearchInput scope, int pageSize, int pageNum, DataView dataView, DisplayContext ctx) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		SearchResult<ContentDataRecord> searchResult = searchClient.searchAndGetRecords(
				searchText, scope, template, pageSize, pageNum, dataView);
		
		List<DisplayableContent> content = new ArrayList<>();
		
		if (searchResult != null) {
			
			ctx = ctx == null ? new DisplayContext() : ctx;
			
			for (ContentDataRecord dataRec : searchResult.getContent()) {
				
				DisplayableContent resultItem = dsContentBuilder.build(template, dataRec, ctx);
				
				if (resultItem != null) {
					docUrlPopulator.populateSecureUrl(resultItem, ctx);
					content.add(resultItem);
				}
			}
		}
		
		SearchResult<DisplayableContent> result = new SearchResult<>();
		result.setContent(content);
		result.setNumberOfElements(searchResult.getNumberOfElements());
		result.setPageSize(searchResult.getPageSize());
		result.setCurrentPage(pageNum);
		result.setTotalPages(searchResult.getTotalPages());
		
		return result;
	}

	@Override
	public SearchResult<ContentDataRecord> searchBizContentRecords(SearchInput input, DataView dataView) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		return searchClient.searchBizContentRecords(input, template, dataView);
	}
	
	@Override
	public SearchResult<ContentDataRecord> searchAsYouTypeBizContentRecords(SearchInput input, DataView dataView) {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateMgr.getTemplateFacade(templateId);
		
		return searchClient.searchAsYouTypeBizContentRecords(input, template, dataView);
	}

}
