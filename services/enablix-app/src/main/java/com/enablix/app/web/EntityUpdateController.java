package com.enablix.app.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.id.IdentityUtil;

@RestController
@RequestMapping("/services/entity")
public class EntityUpdateController {

	@RequestMapping(method = RequestMethod.POST, value="/{transformer}/c/{containerQId}/",
			produces = "application/json")
	public Map<String, Object> createOrUpdateEntity(@RequestBody Map<String, Object> data,
			@PathVariable String transformer, @PathVariable String containerQId) {
		// TODO:
		Map<String, Object> returnData = new HashMap<String, Object>();
		returnData.put(ContentDataConstants.IDENTITY_KEY, IdentityUtil.generateIdentity(returnData));
		return returnData;
	}
	
}
