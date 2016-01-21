package com.enablix.app.content.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentUpdateHandlerFactory {

	@Autowired
	private InsertChildContainerHandler insertChild;
	
	@Autowired
	private InsertRootContainerHandler insertRoot;
	
	@Autowired
	private UpdateContentAttributeHandler updateAttribs;
	
	public ContentUpdateHandler getHandler(UpdateContentRequest request) {
		
		if (request.isUpdateAttribRequest()) {
			return updateAttribs;
		}
		
		if (request.isInsertRootRequest()) {
			return insertRoot;
		} 
		
		if (request.isInsertChildRequest()) {
			return insertChild; 
		}
		
		throw new IllegalStateException("No handler found for request: " + request);
	}
	
}
