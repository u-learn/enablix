package com.enablix.app.content.update;

import java.util.Map;

import com.enablix.core.api.TemplateFacade;


public interface ContentUpdateHandler {

	
	/**
	 * Updates the content in the system
	 * 
	 * Returns the existing record before update. Return <code>null</code> if a new
	 * records is being created.
	 * 
	 * @param template The template for the content
	 * @param recordId The parent id of the content
	 * @param contentQId The qualified id for the content
	 * @param contentDataMap The content data
	 * 
	 * @return Existing record before update
	 */
	Map<String, Object> updateContent(TemplateFacade template, String recordId, 
			String contentQId, Map<String, Object> contentDataMap);
	
}
