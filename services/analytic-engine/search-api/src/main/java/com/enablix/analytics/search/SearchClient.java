package com.enablix.analytics.search;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.services.util.template.TemplateWrapper;

public interface SearchClient {

	SearchResult<ContentDataRef> search(String text, TemplateWrapper template, int pageSize, int pageNum);

	SearchResult<ContentDataRecord> searchAndGetRecords(String text, TemplateWrapper template, int pageSize, int pageNum);
	
}
