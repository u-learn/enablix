package com.enablix.analytics.search.es;

import org.elasticsearch.common.unit.Fuzziness;

public interface FuzzyMatchOption {

	Fuzziness fuzziness(String searchText);
	
	FuzzyMatchOption DEFAULT_FUZZY_MATCH = new FuzzyMatchOption() {
		
		@Override
		public Fuzziness fuzziness(String searchText) {
			
			if (ESPropertiesUtil.getProperties().isFuzzyMatchOn() 
					&& searchText.length() >= ESPropertiesUtil.getProperties().getFuzzyMatchMinChars()) {
			
				return Fuzziness.AUTO;
			}
			
			return null;
		}
		
	};
	
}
