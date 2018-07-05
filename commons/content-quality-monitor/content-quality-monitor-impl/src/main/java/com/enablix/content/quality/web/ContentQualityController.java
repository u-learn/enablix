package com.enablix.content.quality.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.SearchRequest;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.domain.content.quality.ContentQualityAlert;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.services.util.DataViewUtil;

@RestController
@RequestMapping("/cq")
public class ContentQualityController {

	@Autowired
	private GenericDao dao;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private ContentDataManager dataManager;
	
	@RequestMapping(method = RequestMethod.POST, value="/fetch",
			consumes = "application/json", produces = "application/json")
	public Page<?> filterData(@RequestBody SearchRequest searchRequest) throws ClassNotFoundException {
		
		TemplateFacade template = templateManager.getTemplateFacade(ProcessContext.get().getTemplateId());
		
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		MongoDataView view = DataViewUtil.getMongoDataView(userView);
		
		Page<ContentQualityAlert> dataPage = dao.findByQuery(searchRequest, ContentQualityAlert.class, view);
		
		Map<String, List<String>> groupedContent = groupByContentQId(dataPage);
		
		Map<String, Map<String, Object>> recordsById = new HashMap<>();
		
		for (Map.Entry<String, List<String>> entry : groupedContent.entrySet()) {
			
			List<Map<String, Object>> contentRecords = dataManager.getContentRecords(
					entry.getKey(), entry.getValue(), template, userView);
			
			for (Map<String, Object> rec : contentRecords) {
				recordsById.put(ContentDataUtil.getRecordIdentity(rec), rec);
			}
		}
		
		List<ContentQualityAlertResponse> responseList = new ArrayList<>();
		
		for (ContentQualityAlert alert : dataPage) {
			
			ContentQualityAlertResponse res = new ContentQualityAlertResponse();
			res.setContentQualityAlert(alert);
			res.setRecord(recordsById.get(alert.getRecordIdentity()));
			
			responseList.add(res);
		}
		
		return new PageImpl<>(responseList, new PageRequest(
				dataPage.getNumber(), dataPage.getSize(), dataPage.getSort()), 
				dataPage.getTotalElements());
	}
	
	private Map<String, List<String>> groupByContentQId(Page<ContentQualityAlert> dataPage) {
		
		Map<String, List<String>> recIdsByQId = new HashMap<>();
		
		for (ContentQualityAlert alert : dataPage) {
			CollectionUtil.addToMappedListValue(alert.getContentQId(), 
				alert.getRecordIdentity(), recIdsByQId, () -> new ArrayList<>());
		}
		
		return recIdsByQId;
	}

	private static class ContentQualityAlertResponse {
		
		private ContentQualityAlert contentQualityAlert;
		
		private Map<String, Object> record;

		@SuppressWarnings("unused")
		public ContentQualityAlert getContentQualityAlert() {
			return contentQualityAlert;
		}

		public void setContentQualityAlert(ContentQualityAlert contentQualityAlert) {
			this.contentQualityAlert = contentQualityAlert;
		}

		@SuppressWarnings("unused")
		public Map<String, Object> getRecord() {
			return record;
		}

		public void setRecord(Map<String, Object> record) {
			this.record = record;
		}
		
	}
	
}
