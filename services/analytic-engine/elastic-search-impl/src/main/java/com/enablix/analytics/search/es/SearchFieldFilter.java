package com.enablix.analytics.search.es;

public interface SearchFieldFilter {

	boolean searchIn(String containerQId, String attributeId);
	
	SearchFieldFilter ALL_FIELD_FILTER = new SearchFieldFilter() {
		
		@Override
		public boolean searchIn(String containerQId, String attributeId) {
			return true;
		}
	};
	
}
