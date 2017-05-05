package com.enablix.analytics.search.es;

import com.enablix.core.commons.xsdtopojo.ContentItemType;

public interface SearchFieldFilter {

	boolean searchIn(String containerQId, ContentItemType field);
	
	boolean searchIn(String fieldId);
	
	SearchFieldFilter ALL_FIELD_FILTER = new SearchFieldFilter() {

		@Override
		public boolean searchIn(String fieldId) {
			return true;
		}

		@Override
		public boolean searchIn(String containerQId, ContentItemType field) {
			return true;
		}
	};
	
}
