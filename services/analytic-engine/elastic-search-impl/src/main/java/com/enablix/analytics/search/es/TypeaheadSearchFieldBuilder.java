package com.enablix.analytics.search.es;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.core.api.TemplateFacade;

public class TypeaheadSearchFieldBuilder implements SearchFieldBuilder {

	private String[] fields = { ContentDataConstants.CONTENT_TITLE_KEY };
	
	@Override
	public String[] getContentSearchFields(SearchFieldFilter filter, TemplateFacade template) {
		return fields;
	}
	
	public String[] getFields() {
		return fields;
	}

}
