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
import com.enablix.app.content.bulkimport.ImportManager;
import com.enablix.app.content.delete.DeleteContentRequest;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.commons.exception.ValidationException;
import com.enablix.core.domain.content.ImportRequest;

@RestController
@RequestMapping("content")
public class ContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentDataController.class);
	
	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private ImportManager importMgr;
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/update/t/{templateId}/c/{contentQId}/r/{parentIdentity}", 
			consumes = "application/json", produces = "application/json")
	public UpdateContentResponse updateData(@PathVariable String templateId, @PathVariable String contentQId, 
			@PathVariable String parentIdentity, @RequestBody String jsonData) {
		
		LOGGER.debug("Updating content data");
		return dataMgr.saveData(new UpdateContentRequest(templateId, parentIdentity, contentQId, jsonData));
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/update/t/{templateId}/c/{contentQId}", 
			consumes = "application/json", produces = "application/json")
	public UpdateContentResponse insertData(@PathVariable String templateId, @PathVariable String contentQId, 
			@RequestBody String jsonData) {
		
		LOGGER.debug("Updating content data");
		return dataMgr.saveData(new UpdateContentRequest(templateId, null, contentQId, jsonData));
	}

	@RequestMapping(method = RequestMethod.POST, 
			value="/delete/t/{templateId}/c/{contentQId}/r/{recordIdentity}", 
			consumes = "application/json", produces = "application/json")
	public String deleteData(@PathVariable String templateId, 
							 @PathVariable String contentQId, 
							 @PathVariable String recordIdentity) {
		
		LOGGER.debug("Deleting content data");
		dataMgr.deleteData(new DeleteContentRequest(templateId, contentQId, recordIdentity));
		return "{\"status\": \"success\"}";
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			value="/import/request", 
			consumes = "application/json", produces = "application/json")
	public void importRecords(@RequestBody ImportRequest request) throws ValidationException {
		importMgr.acceptRequest(request);
	}
	
}