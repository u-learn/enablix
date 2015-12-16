package com.enablix.app.content.ui.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.search.SearchService;

@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@RequestMapping(method = RequestMethod.GET, value="/t/{searchText}/")
	public List<NavigableContent> containerRecommendedContent(@PathVariable String searchText) {
		return searchService.search(searchText);
	}
	
}
