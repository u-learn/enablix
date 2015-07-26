package com.enablix.app.content.ui.recent;

import java.util.List;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;

public interface RecentContentService {

	List<NavigableContent> getRecentContent(WebContentRequest request);
	
}
