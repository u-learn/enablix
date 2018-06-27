package com.enablix.app.content.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.fetch.FetchContentRequest;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.constants.AppConstants;
import com.enablix.commons.constants.ContentDataConstants;
import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.api.ContentDataRecord;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.api.ContentRecordGroup;
import com.enablix.core.api.ContentStackItem;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ActivityLogger;
import com.enablix.services.util.ContentDataUtil;

@RestController
@RequestMapping("data")
public class FetchContentDataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FetchContentDataController.class);

	@Autowired
	private ContentDataManager dataMgr;
	
	@Autowired
	private TemplateManager templateMgr;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}", 
			produces = "application/json")
	public Object fetchRootData(@PathVariable String templateId, @PathVariable String contentQId,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size,
			@RequestParam(required=false) String sortProp,
			@RequestParam(required=false) Sort.Direction sortDir) {
		
		LOGGER.debug("Fetch root content data");
		
		Pageable pageable = createPaginationInfo(page, size, sortProp, sortDir);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return fetchData(new FetchContentRequest(templateId, contentQId, null, null, pageable), userDataView);
	}
	
	private Pageable createPaginationInfo(String page, String size) {
		return createPaginationInfo(page, size, null, null);
	}
	
	private Pageable createPaginationInfo(String page, String size, String sortProp, Direction sortDir) {
		
		Pageable pageable = null;
		
		if (!StringUtil.isEmpty(page)) {
			
			int pageInt = Integer.parseInt(page);
			int sizeInt = StringUtil.isEmpty(size) ? AppConstants.DEFAULT_PAGE_SIZE : Integer.parseInt(size);
			
			if (StringUtil.hasText(sortProp)) {
				pageable = new PageRequest(pageInt, sizeInt, sortDir == null ? Direction.ASC : sortDir, sortProp);
			} else {
				pageable = new PageRequest(pageInt, sizeInt);
			}
		}
		
		return pageable;
	}

	@RequestMapping(method = RequestMethod.POST, value="/fetchcs", produces = "application/json")
	public List<ContentDataRecord> getContentStack(@RequestBody List<ContentStackItem> contentStackItems) {
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return dataMgr.getContentStackRecords(contentStackItems, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/fetchcsgrp", produces = "application/json")
	public List<ContentRecordGroup> getContentStackGroup(@RequestBody List<Map<String, String>> contentStackItems,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size,
			@RequestParam(required=false) String sortProp,
			@RequestParam(required=false) Sort.Direction sortDir) {
		
		Pageable pageable = createPaginationInfo(page, size, sortProp, sortDir);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return dataMgr.fetchStackDetails(contentStackItems, pageable, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/fetchcs/c/{containerQId}/r/{instanceIdentity}/{itemQId}/", 
			produces = "application/json")
	public List<ContentRecordGroup> fetchRecordContentStackItem(@PathVariable String containerQId, 
			@PathVariable String instanceIdentity, @PathVariable String itemQId,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size,
			@RequestParam(required=false) String sortProp,
			@RequestParam(required=false) Sort.Direction sortDir) {
		
		LOGGER.debug("Fetch content stack data");
		Pageable pageable = createPaginationInfo(page, size, sortProp, sortDir);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return dataMgr.getContentStackItemForContentRecord(containerQId, instanceIdentity, itemQId, pageable, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/fetchcs/c/{containerQId}/r/{instanceIdentity}", 
			produces = "application/json")
	public List<ContentRecordGroup> fetchRecordContentStack(@PathVariable String containerQId, 
			@PathVariable String instanceIdentity,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size,
			@RequestParam(required=false) String sortProp,
			@RequestParam(required=false) Sort.Direction sortDir) {
		
		LOGGER.debug("Fetch content stack data");
		Pageable pageable = createPaginationInfo(page, size, sortProp, sortDir);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return dataMgr.getContentStackForContentRecord(containerQId, instanceIdentity, pageable, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/t/{templateId}/c/{contentQId}/p/{parentIdentity}", 
			produces = "application/json")
	public Object fetchChildData(@PathVariable String templateId, 
			@PathVariable String contentQId, 
			@PathVariable String parentIdentity,
			@RequestParam(required=false) String page, 
			@RequestParam(required=false) String size,
			@RequestParam(required=false) String sortProp,
			@RequestParam(required=false) Sort.Direction sortDir) {
		
		LOGGER.debug("Fetch child content data");
		Pageable pageable = createPaginationInfo(page, size, sortProp, sortDir);
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return fetchData(new FetchContentRequest(templateId, contentQId, parentIdentity, null, pageable), userDataView);
	}
	
	@RequestMapping(method = RequestMethod.GET, 
			value="/rnc/c/{contentQId}/r/{contentIdentity}", 
			produces = "application/json")
	public Object fetchRecordAndAllChildrenData(
			@PathVariable String contentQId, 
			@PathVariable String contentIdentity,
			@RequestParam(required=false) String size) {
		
		LOGGER.debug("Fetch record and all children content data");
		
		Pageable pageable = null;
		if (StringUtil.hasText(size)) {
			pageable = createPaginationInfo("0", size);
		}
		
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		List<ContentRecordGroup> recordAndChildren = dataMgr.fetchRecordAndChildData(
										contentQId, contentIdentity, pageable, userDataView);
		
		// Audit Access activity
		if (CollectionUtil.isNotEmpty(recordAndChildren)) {

			ActivityTrackingContext atCtx = ActivityTrackingContext.get();
			Channel channel = atCtx.getActivityChannel();
			
			if (channel != null) {
			
				for (ContentRecordGroup recGrp : recordAndChildren) {
				
					if (recGrp.getContentQId().equals(contentQId)) {
					
						for (Map<String, Object> rec : recGrp.getRecords()) {
						
							String recIdentity = (String) rec.get(ContentDataConstants.IDENTITY_KEY);
							
							if (recIdentity.equals(contentIdentity)) {
							
								String recTitle = (String) rec.get(ContentDataConstants.CONTENT_TITLE_KEY);
								String templateId = ProcessContext.get().getTemplateId();
								
								ActivityLogger.auditContentAccess(
										ContentDataRef.createContentRef(templateId, contentQId, recIdentity, recTitle), 
										ContainerType.CONTENT, channel, atCtx.getActivityContextName(), 
											atCtx.getActivityContextId(), atCtx.getActivityContextTerm());
							}
						}
					}
				}
			}
		}
		
		return recordAndChildren;
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
		
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		Object data =  fetchData(new FetchContentRequest(templateId, contentQId, null, dataIdentity), userDataView);
		
		if (data != null) {
			// Audit access activity
			Channel channel = Channel.parse(atChannel);
			if (channel != null && !"SEARCH".equals(atContext)) {
				
				String contentTitle = null;
				if (data instanceof Map) {
					// single result 
					Map record = (Map) data;
					contentTitle = ContentDataUtil.findPortalLabelValue(record, 
							templateMgr.getTemplateFacade(templateId), contentQId);
					
				}
				ActivityLogger.auditContentAccess(
					ContentDataRef.createContentRef(templateId, contentQId, dataIdentity, contentTitle), 
					ContainerType.CONTENT, channel, atContext, atContextId, atContextTerm);
			}
			
		}
		
		return data == null ? new HashMap<>() : data;
	}
	
	private Object fetchData(FetchContentRequest request, DataView view) {
		return dataMgr.fetchDataJson(request, view);
	}

}
