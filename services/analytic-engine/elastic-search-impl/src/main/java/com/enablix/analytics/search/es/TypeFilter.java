package com.enablix.analytics.search.es;

public interface TypeFilter {

	boolean searchIn(String contentQId);
	
	TypeFilter ALL_TYPES = new TypeFilter() {
		
		@Override
		public boolean searchIn(String contentQId) {
			return true;
		}
	};
	
}
