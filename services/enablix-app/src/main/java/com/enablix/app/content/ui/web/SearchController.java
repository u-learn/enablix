package com.enablix.app.content.ui.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.search.SearchService;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.SearchResult;
import com.enablix.core.domain.activity.SearchActivity;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.services.util.ActivityLogger;

@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method = RequestMethod.GET, value="/t/{searchText}/")
	public SearchResult<NavigableContent> searchResults(@PathVariable String searchText,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size) {
		
		int pageInt = 0;
		int sizeInt = AppConstants.DEFAULT_PAGE_SIZE;
		
		if (!StringUtil.isEmpty(page)) {
			pageInt = Integer.parseInt(page);
			sizeInt = StringUtil.isEmpty(size) ? AppConstants.DEFAULT_PAGE_SIZE : Integer.parseInt(size);
		}
		
		SearchResult<NavigableContent> searchResult = searchService.search(searchText, sizeInt, pageInt);
		
		ActivityLogger.auditActivity(new SearchActivity(searchText));
		return searchResult;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/d/t/{searchText}/")
	public SearchResult<DisplayableContent> displayableSearchResults(@PathVariable String searchText,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size) {
		
		int pageInt = 0;
		int sizeInt = AppConstants.DEFAULT_PAGE_SIZE;
		
		if (!StringUtil.isEmpty(page)) {
			pageInt = Integer.parseInt(page);
			sizeInt = StringUtil.isEmpty(size) ? AppConstants.DEFAULT_PAGE_SIZE : Integer.parseInt(size);
		}
		
		SearchResult<DisplayableContent> searchResult = searchService.searchAndGetResultAsDisplayContent(searchText, sizeInt, pageInt);
		
		ActivityLogger.auditActivity(new SearchActivity(searchText));
		return searchResult;
	}
	
}
