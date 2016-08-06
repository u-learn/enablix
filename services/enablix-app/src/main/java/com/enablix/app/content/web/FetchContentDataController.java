package com.enablix.app.content.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.template.web.TemplateController;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.domain.activity.ContentActivity.ContentActivityType;
import com.enablix.services.util.ActivityLogger;

@RestController
@RequestMapping("data")
public class FetchContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

	@Autowired
	private ContentDataManager dataMgr;
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}", 
			produces = "application/json")
	public Object fetchRootData(@PathVariable String templateId, @PathVariable String contentQId,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size) {
		LOGGER.debug("Fetch root content data");
		
		Pageable pageable = null;
		
		if (!StringUtil.isEmpty(page)) {
			int pageInt = Integer.parseInt(page);
			int sizeInt = StringUtil.isEmpty(size) ? DEFAULT_PAGE_SIZE : Integer.parseInt(size);
			pageable = new PageRequest(pageInt, sizeInt);
		}
		
		return fetchData(new FetchContentRequest(templateId, contentQId, null, null, pageable));
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
			@PathVariable String dataIdentity,
			@RequestParam(required=false) String atChannel) { // activity tracking channel
		
		LOGGER.debug("Fetch record content data");
		
		Object data =  fetchData(new FetchContentRequest(templateId, contentQId, null, dataIdentity));
		
		// Audit access activity
		Channel channel = Channel.parse(atChannel);
		if (channel != null) {
			ActivityLogger.auditContentActivity(ContentActivityType.CONTENT_ACCESS, 
					new ContentDataRef(contentQId, contentQId, dataIdentity), ContainerType.CONTENT, channel);
		}
		
		return data;
	}
	
	private Object fetchData(FetchContentRequest request) {
		return dataMgr.fetchDataJson(request);
	}

}
