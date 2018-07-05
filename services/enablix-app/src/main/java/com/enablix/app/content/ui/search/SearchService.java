package com.enablix.app.content.ui.search;

import com.enablix.analytics.search.SearchInput;
import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.NavigableContent;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.SearchResult;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;

public interface SearchService {

	SearchResult<NavigableContent> search(String searchText, SearchInput scope, int pageSize, int pageNum, DataView dataView);

	SearchResult<DisplayableContent> searchAndGetResultAsDisplayContent(
			String searchText, SearchInput scope, int pageSize, int pageNum, DataView dataView, DisplayContext ctx);
	
	SearchResult<ContentDataRecord> searchBizContentRecords(SearchInput input, DataView dataView);

	SearchResult<ContentDataRecord> searchAsYouTypeBizContentRecords(SearchInput input, DataView dataView);

}
