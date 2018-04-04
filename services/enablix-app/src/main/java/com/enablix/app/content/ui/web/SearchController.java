package com.enablix.app.content.ui.web;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.app.content.ui.search.SearchService;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.SearchResult;
import com.enablix.core.domain.activity.SearchActivity;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ActivityLogger;

@RestController
@RequestMapping("search")
public class SearchController {

	@Autowired
	private SearchService searchService;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
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
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		SearchResult<NavigableContent> searchResult = searchService.search(searchText, sizeInt, pageInt, dataView);
		
		ActivityLogger.auditActivity(new SearchActivity(searchText, 
				searchResult.getCurrentPage(), searchResult.getNumberOfElements()));
		return searchResult;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/bizcontent/")
	public SearchResult<ContentDataRecord> searchBizContentResults(@RequestBody SearchRequest request) {
		
		int pageInt = 0;
		int sizeInt = AppConstants.DEFAULT_PAGE_SIZE;
		
		if (request.getPage() != null) {
			pageInt = request.getPage();
			sizeInt = request.getSize() == null ? AppConstants.DEFAULT_PAGE_SIZE : request.getSize();
		}
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		SearchResult<ContentDataRecord> searchResult = searchService.searchBizContentRecords(request.getText(), sizeInt, pageInt, dataView);
		
		ActivityLogger.auditActivity(new SearchActivity(request.getText(), 
				searchResult.getCurrentPage(), searchResult.getNumberOfElements()));
		return searchResult;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/ta/bizcontent/")
	public SearchResult<ContentDataRecord> searchAsYouTypeBizContentResults(@RequestBody SearchRequest request) {
		
		int pageInt = 0;
		int sizeInt = 5;
		
		if (request.getPage() != null) {
			pageInt = request.getPage();
			sizeInt = request.getSize() == null ? sizeInt : request.getSize();
		}
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return searchService.searchAsYouTypeBizContentRecords(request.getText(), sizeInt, pageInt, dataView);
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
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		DisplayContext ctx = new DisplayContext();
		SearchResult<DisplayableContent> searchResult = 
				searchService.searchAndGetResultAsDisplayContent(searchText, sizeInt, pageInt, dataView, ctx);
		
		ActivityLogger.auditActivity(new SearchActivity(searchText, 
				searchResult.getCurrentPage(), searchResult.getNumberOfElements()));
		return searchResult;
	}
	
	public static class SearchRequest {
		
		private String text;
		private Integer page;
		private Integer size;
		
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Integer getPage() {
			return page;
		}
		public void setPage(Integer page) {
			this.page = page;
		}
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
		
	}
	
}
