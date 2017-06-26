package com.enablix.app.content.ui.search;

import java.util.List;
import java.util.Map;

import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.data.view.DataView;

public interface UIFilterService {

	Map<String, List<RefListItemCount>> findRecordCountByRefListItem(
			String containerQId, SearchRequest searchRequest, DataView userView);
	
}
