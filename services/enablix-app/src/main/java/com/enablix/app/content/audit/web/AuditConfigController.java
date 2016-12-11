package com.enablix.app.content.audit.web;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;

@RestController
@RequestMapping("getAuditConfiguration")
public class AuditConfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditConfigController.class);

	@RequestMapping(value = "/getAuditActivityTypes", method = RequestMethod.GET, produces = "application/json")
	public HashMap<String, String> getAuditActivityTypes() {
		LOGGER.debug("Fetching getAuditActivityTypes");
		HashMap<String, String> contentActivityTypes = new HashMap<String, String>();
		for(ContentActivityType contentActivity : ContentActivityType.values()) {
	        contentActivityTypes.put(contentActivity.name(), contentActivity.getValue());
	    }
		return contentActivityTypes;	
	}	
}