package com.enablix.analytics.search;

import java.util.List;

import com.enablix.core.api.ContentDataRef;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;

public interface SearchClient {

	List<ContentDataRef> search(String text, ContentTemplate template);
	
}
