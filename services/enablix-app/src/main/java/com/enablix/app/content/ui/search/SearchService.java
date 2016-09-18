package com.enablix.app.content.ui.search;

import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.api.SearchResult;

public interface SearchService {

	SearchResult<NavigableContent> search(String searchText, int pageSize, int pageNum);
	
}
