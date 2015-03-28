package com.enablix.app.content.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.template.web.TemplateController;

@RestController
@RequestMapping("data")
public class FetchContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/{templateId}/{contentQId}", 
			produces = "application/json")
	public String updateData(@PathVariable String templateId, @PathVariable String contentQId) {
		LOGGER.debug("Updating content data");
		return "";
	}
	
}
