package com.enablix.app.content.recent;

import java.util.List;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.mongo.search.SearchCriteria;

public interface RecentContentService {

	//@PreAuthorize("hasAuthority('VIEW_RECENT_CONTENT')")
	List<NavigableContent> getRecentContent(WebContentRequest request);

	List<NavigableContent> getRecentContentByCriteria(SearchCriteria criteria);
	
}
