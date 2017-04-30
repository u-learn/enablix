package com.enablix.analytics.search.es;

import com.enablix.core.api.TemplateFacade;

public interface SearchFieldBuilder {

	String[] getContentSearchFields(SearchFieldFilter filter, TemplateFacade template);
	
}
