package com.enablix.analytics.search;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface SearchClient {

	SearchResult<ContentDataRef> search(String text, ContentTemplate template, int pageSize, int pageNum);
	
}
