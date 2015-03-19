package com.enablix.app.content.update;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enablix.app.content.ContentDataConstants;
import com.enablix.commons.util.json.JsonUtil;

@Component
public class ContentUpdateHandlerFactory {

	@Autowired
	private InsertChildContentHandler insertChild;
	
	@Autowired
	private InsertRootContentHandler insertRoot;
	
	@Autowired
	private UpdateContentAttributesHandler updateAttribs;
	
	public ContentUpdateHandler getHandler(UpdateContentRequest request) {
		
		Map<String, Object> mapContent = JsonUtil.jsonToMap(request.getJsonData());
		
		if (request.isNewRecord()) {
			return insertRoot;
		} 
		
		if (mapContent.containsKey(ContentDataConstants.IDENTITY_KEY)) {
			return updateAttribs;
		}
		
		return insertChild; 
	}
	
}
