package com.enablix.analytics.search;

import java.util.List;

import com.enablix.core.api.ContentDataRef;

public interface SearchClient {

	List<ContentDataRef> search(String text, String templateId);
	
}
