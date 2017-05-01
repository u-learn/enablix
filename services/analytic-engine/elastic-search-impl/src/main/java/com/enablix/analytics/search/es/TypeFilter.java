package com.enablix.analytics.search.es;

public interface TypeFilter {

	boolean searchInType(String esType);
	
	TypeFilter ALL_TYPES = new TypeFilter() {
		
		@Override
		public boolean searchInType(String esType) {
			return true;
		}
		
	};
	
}
