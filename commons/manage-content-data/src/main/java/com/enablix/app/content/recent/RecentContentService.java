package com.enablix.app.content.recent;

import java.util.List;

import org.springframework.data.domain.Page;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.mongo.search.SearchCriteria;
import com.enablix.data.view.DataView;

public interface RecentContentService {

	//@PreAuthorize("hasAuthority('VIEW_RECENT_CONTENT')")
	List<NavigableContent> getRecentContent(WebContentRequest request, DataView view);

	List<NavigableContent> getRecentContentByCriteria(SearchCriteria criteria, DataView view);

	Page<RecentUpdateVO> getRecentContentByRequest(SearchRequest request, DataView userDataView);
	
}
