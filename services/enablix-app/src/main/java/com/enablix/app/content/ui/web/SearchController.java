package com.enablix.app.content.ui.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.search.SearchService;
import com.enablix.core.domain.activity.SearchActivity;
import com.enablix.services.util.ActivityLogger;

@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method = RequestMethod.GET, value="/t/{searchText}/")
	public List<NavigableContent> containerRecommendedContent(@PathVariable String searchText) {
		List<NavigableContent> searchResult = searchService.search(searchText);
		ActivityLogger.auditActivity(new SearchActivity(searchText));
		return searchResult;
	}
	
}
