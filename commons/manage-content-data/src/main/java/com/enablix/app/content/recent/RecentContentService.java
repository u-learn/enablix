package com.enablix.app.content.recent;

import java.util.List;

import org.springframework.data.domain.Page;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.mongo.search.SearchCriteria;
import com.enablix.core.mongo.search.service.SearchRequest;

public interface RecentContentService {

	//@PreAuthorize("hasAuthority('VIEW_RECENT_CONTENT')")
	List<NavigableContent> getRecentContent(WebContentRequest request);

	List<NavigableContent> getRecentContentByCriteria(SearchCriteria criteria);

	Page<RecentUpdateVO> getRecentContentByRequest(SearchRequest request);
	
}
