package com.enablix.app.content.recent;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.enablix.analytics.web.request.WebContentRequest;
import com.enablix.app.content.ui.NavigableContent;

public interface RecentContentService {

	//@PreAuthorize("hasAuthority('VIEW_RECENT_CONTENT')")
	List<NavigableContent> getRecentContent(WebContentRequest request);
	
}
