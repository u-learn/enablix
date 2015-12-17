package com.enablix.analytics.search.es;

import org.elasticsearch.search.SearchHit;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface SearchHitToContentDataRefTransformer {

	ContentDataRef transform(SearchHit searchHit, ContentTemplate template);
	
}
