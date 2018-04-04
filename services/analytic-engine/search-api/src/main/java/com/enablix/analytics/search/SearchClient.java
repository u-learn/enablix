package com.enablix.analytics.search;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.SearchResult;
import com.enablix.core.api.TemplateFacade;
import com.enablix.data.view.DataView;

public interface SearchClient {

	SearchResult<ContentDataRef> search(String text, TemplateFacade template, int pageSize, int pageNum, DataView dataView);

	SearchResult<ContentDataRecord> searchAndGetRecords(String text, TemplateFacade template, int pageSize, int pageNum, DataView dataView);
	
	SearchResult<ContentDataRecord> searchBizContentRecords(String text, TemplateFacade template, int pageSize, int pageNum, DataView dataView);

	SearchResult<ContentDataRecord> searchAsYouTypeBizContentRecords(String text, TemplateFacade template, int pageSize,
			int pageNum, DataView dataView);
}
