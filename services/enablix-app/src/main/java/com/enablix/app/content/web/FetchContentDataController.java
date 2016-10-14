package com.enablix.app.content.web;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.app.template.web.TemplateController;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;

@RestController
@RequestMapping("data")
public class FetchContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}", 
			produces = "application/json")
	public Object fetchRootData(@PathVariable String templateId, @PathVariable String contentQId,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size) {
		
		LOGGER.debug("Fetch root content data");
		
		Pageable pageable = createPaginationInfo(page, size);
		return fetchData(new FetchContentRequest(templateId, contentQId, null, null, pageable));
	}
	
	private Pageable createPaginationInfo(String page, String size) {
		
		Pageable pageable = null;
		
		if (!StringUtil.isEmpty(page)) {
			int pageInt = Integer.parseInt(page);
			int sizeInt = StringUtil.isEmpty(size) ? AppConstants.DEFAULT_PAGE_SIZE : Integer.parseInt(size);
			pageable = new PageRequest(pageInt, sizeInt);
		}
		
		return pageable;
	}

	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}/p/{parentIdentity}", 
			produces = "application/json")
	public Object fetchChildData(@PathVariable String templateId, 
			@PathVariable String contentQId, 
			@PathVariable String parentIdentity,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size) {
		
		LOGGER.debug("Fetch child content data");
		Pageable pageable = createPaginationInfo(page, size);
		return fetchData(new FetchContentRequest(templateId, contentQId, parentIdentity, null, pageable));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}/d/{dataIdentity}", 
			produces = "application/json")
	public Object fetchRecordData(@PathVariable String templateId, 
			@PathVariable String contentQId, 
			@PathVariable String dataIdentity,
			@RequestHeader(required=false) String atChannel, // activity tracking channel
			@RequestHeader(required=false) String atContext, // activity tracking context e.g. weekly sharing
			@RequestHeader(required=false) String atContextId, // activity tracking context id e.g. weekly sharing id
			@RequestHeader(required=false) String atContextTerm // activity tracking context term e.g. search term
			) { 
		
		LOGGER.debug("Fetch record content data");
		
		Object data =  fetchData(new FetchContentRequest(templateId, contentQId, null, dataIdentity));
		
		// Audit access activity
		Channel channel = Channel.parse(atChannel);
		if (channel != null) {
			
			String contentTitle = null;
			if (data instanceof Map) {
				// single result 
				Map record = (Map) data;
				contentTitle = ContentDataUtil.findPortalLabelValue(record, 
						templateMgr.getTemplate(templateId), contentQId);
				
			}
			ActivityLogger.auditContentAccess(
				new ContentDataRef(templateId, contentQId, dataIdentity, contentTitle), 
				ContainerType.CONTENT, channel, atContext, atContextId, atContextTerm);
		}
		
		return data;
	}
	
	private Object fetchData(FetchContentRequest request) {
		return dataMgr.fetchDataJson(request);
	}

}
