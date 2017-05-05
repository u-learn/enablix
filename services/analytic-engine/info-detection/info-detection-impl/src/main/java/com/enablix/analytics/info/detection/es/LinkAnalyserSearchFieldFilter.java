package com.enablix.analytics.info.detection.es;

import java.util.Set;

import com.enablix.analytics.search.es.SearchFieldFilter;
import com.enablix.core.commons.xsdtopojo.ContentItemClassType;
import com.enablix.core.commons.xsdtopojo.ContentItemType;

public class LinkAnalyserSearchFieldFilter implements SearchFieldFilter {

	private Set<String> searchInTypes;
	
	public LinkAnalyserSearchFieldFilter(Set<String> searchInTypes) {
		this.searchInTypes = searchInTypes;
	}
	
	@Override
	public boolean searchIn(String containerQId, ContentItemType field) {
		return searchInTypes.contains(containerQId) && field.getType() == ContentItemClassType.TEXT;
	}

	@Override
	public boolean searchIn(String fieldId) {
		return false;
	}

}
