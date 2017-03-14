package com.enablix.analytics.search.es;

import org.elasticsearch.search.SearchHit;

import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.services.util.template.TemplateWrapper;

public interface SearchHitTransformer {

	ContentDataRef toContentDataRef(SearchHit searchHit, TemplateWrapper template);
	
	ContentDataRecord toContentDataRecord(SearchHit searchHit, TemplateWrapper template);
	
}
