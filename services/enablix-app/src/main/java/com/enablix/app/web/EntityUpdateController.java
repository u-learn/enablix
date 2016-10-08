package com.enablix.app.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.external.ExternalContentHandler;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.mapper.ContentSource;
import com.enablix.content.mapper.ExternalContent;

@RestController
@RequestMapping("/services/entity")
public class EntityUpdateController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityUpdateController.class);

	@Autowired
	private ExternalContentHandler extContentHandler;
	
	@RequestMapping(method = RequestMethod.POST, value="/{transformer}/c/{containerQId}/",
			produces = "application/json")
	public Map<String, Object> createOrUpdateEntity(@RequestBody Map<String, Object> data,
			@PathVariable String transformer, @PathVariable String containerQId) {

		LOGGER.debug("Entity Create/Update request: {}", data);
		
		String tenantId = ProcessContext.get().getTenantId();
		
		String recordIdentity = extContentHandler.storeContent(
				new ExternalContent(new ContentSource(transformer, tenantId), containerQId, data));
		
		Map<String, Object> returnData = new HashMap<>();
		returnData.put(ContentDataConstants.IDENTITY_KEY, recordIdentity);
		
		return returnData;
	}
	
}
