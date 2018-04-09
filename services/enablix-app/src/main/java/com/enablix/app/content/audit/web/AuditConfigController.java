package com.enablix.app.content.audit.web;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.activity.Activity.ActivityType;
import com.enablix.core.domain.activity.Activity.Category;

@RestController
@RequestMapping("getAuditConfiguration")
public class AuditConfigController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuditConfigController.class);

	@RequestMapping(value = "/getAuditActivityTypes", method = RequestMethod.GET, produces = "application/json")
	public HashMap<String, String> getAuditActivityTypes() {
		
		LOGGER.debug("Fetching getAuditActivityTypes");
		
		HashMap<String, String> contentActivityTypes = new HashMap<String, String>();
		
		for (ActivityType contentActivity : ActivityType.values()) {
			if (contentActivity.getCategory() == Category.CONTENT) {
				addActivityType(contentActivityTypes, contentActivity);
			}
	    }
		
		addActivityType(contentActivityTypes, ActivityType.SEARCH_FREE_TEXT);
		addActivityType(contentActivityTypes, ActivityType.LOGIN);
		addActivityType(contentActivityTypes, ActivityType.LOGOUT);
		addActivityType(contentActivityTypes, ActivityType.NAV_EXTERNAL_LINK);
		
		return contentActivityTypes;	
	}

	private void addActivityType(HashMap<String, String> contentActivityTypes, ActivityType contentActivity) {
		contentActivityTypes.put(contentActivity.name(), contentActivity.getValue());
	}	
	
	
}