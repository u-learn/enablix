package com.enablix.analytics.search;

import org.springframework.data.domain.Page;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.api.SearchResult;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;

public interface SearchClient {

	SearchResult<ContentDataRef> search(String text, SearchInput scope, TemplateFacade template, int pageSize, int pageNum, DataView dataView);

	SearchResult<ContentDataRecord> searchAndGetRecords(String text, SearchInput scope, TemplateFacade template, int pageSize, int pageNum, DataView dataView);
	
	SearchResult<ContentDataRecord> searchBizContentRecords(SearchInput input, TemplateFacade template, DataView dataView);

	SearchResult<ContentDataRecord> searchAsYouTypeBizContentRecords(SearchInput input, TemplateFacade template, DataView dataView);

	Page<?> searchTypeRecords(String containerQId, TemplateFacade template, SearchRequest searchRequest, DataView userView);

}
