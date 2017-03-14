package com.enablix.app.content.ui.search;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.api.SearchResult;
import com.enablix.core.ui.DisplayableContent;

public interface SearchService {

	SearchResult<NavigableContent> search(String searchText, int pageSize, int pageNum);

	SearchResult<DisplayableContent> searchAndGetResultAsDisplayContent(String searchText, int pageSize, int pageNum);
	
}
