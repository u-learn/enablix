package com.enablix.analytics.search.es;

import org.elasticsearch.search.SearchHit;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.TemplateFacade;

public interface SearchHitTransformer {

	ContentDataRef toContentDataRef(SearchHit searchHit, TemplateFacade template);
	
	ContentDataRecord toContentDataRecord(SearchHit searchHit, TemplateFacade template);
	
}
