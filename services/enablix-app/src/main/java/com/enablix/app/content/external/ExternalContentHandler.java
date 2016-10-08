package com.enablix.app.content.external;

import com.enablix.content.mapper.ExternalContent;

public interface ExternalContentHandler {

	/**
	 * Stores the external content after mapping it to the internal containers
	 * 
	 * @param content
	 * @return String - identity of the record added/updated
	 */
	String storeContent(ExternalContent content);
	
}
