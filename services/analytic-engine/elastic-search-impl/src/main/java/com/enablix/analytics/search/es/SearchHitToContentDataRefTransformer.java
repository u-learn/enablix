package com.enablix.analytics.search.es;

import org.elasticsearch.search.SearchHit;

import com.enablix.core.api.ContentDataRef;
import com.enablix.services.util.template.TemplateWrapper;

public interface SearchHitToContentDataRefTransformer {

	ContentDataRef transform(SearchHit searchHit, TemplateWrapper template);
	
}
