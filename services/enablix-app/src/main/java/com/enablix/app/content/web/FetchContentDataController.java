package com.enablix.app.content.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.template.web.TemplateController;

@RestController
@RequestMapping("data")
public class FetchContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

	@Autowired
	private ContentDataManager dataMgr;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}", 
			produces = "application/json")
	public Object fetchRootData(@PathVariable String templateId, @PathVariable String contentQId) {
		LOGGER.debug("Fetch root content data");
		return fetchData(new FetchContentRequest(templateId, contentQId, null, null));
	}

	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}/p/{parentIdentity}", 
			produces = "application/json")
	public Object fetchChildData(@PathVariable String templateId, 
			@PathVariable String contentQId, 
			@PathVariable String parentIdentity) {
		
		LOGGER.debug("Fetch child content data");
		return fetchData(new FetchContentRequest(templateId, contentQId, parentIdentity, null));
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}/d/{dataIdentity}", 
			produces = "application/json")
	public Object fetchRecordData(@PathVariable String templateId, 
			@PathVariable String contentQId, 
			@PathVariable String dataIdentity) {
		
		LOGGER.debug("Fetch record content data");
		return fetchData(new FetchContentRequest(templateId, contentQId, null, dataIdentity));
	}
	
	private Object fetchData(FetchContentRequest request) {
		return dataMgr.fetchDataJson(request);
	}

}
