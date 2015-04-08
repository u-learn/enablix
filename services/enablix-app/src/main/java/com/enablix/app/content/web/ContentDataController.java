package com.enablix.app.content.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.template.web.TemplateController;

@RestController
@RequestMapping("content")
public class ContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/update/t/{templateId}/c/{contentQId}/r/{parentIdentity}", 
			consumes = "application/json")
	public String updateData(@PathVariable String templateId, @PathVariable String contentQId, 
			@PathVariable String parentIdentity, @RequestBody String jsonData) {
		LOGGER.debug("Updating content data");
		dataMgr.saveData(new UpdateContentRequest(templateId, parentIdentity, contentQId, jsonData));
		return "{ \"result\" : \"success\"}";
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/update/t/{templateId}/c/{contentQId}", 
			consumes = "application/json")
	public String insertData(@PathVariable String templateId, @PathVariable String contentQId, 
			@RequestBody String jsonData) {
		LOGGER.debug("Updating content data");
		dataMgr.saveData(new UpdateContentRequest(templateId, null, contentQId, jsonData));
		return "{ \"result\" : \"success\"}";
	}
	
}