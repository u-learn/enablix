package com.enablix.app.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.search.RefListItemCount;
import com.enablix.app.content.ui.search.UIFilterService;
import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.TemplateFacade;
import com.enablix.core.mongo.dao.GenericDao;
import com.enablix.core.mongo.search.service.SearchRequest;
import com.enablix.core.mongo.view.MongoDataView;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@RestController
@RequestMapping("/data/search")
public class GenericDataFetchController {

	@Autowired
	private GenericDao dao;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@Autowired
	private TemplateManager templateManager;
	
	@Autowired
	private UIFilterService uiFilterService;
	
	@RequestMapping(method = RequestMethod.POST, value="/c/{collectionName}/t/{className}",
			consumes = "application/json", produces = "application/json")
	public Page<?> filterData(@RequestBody SearchRequest searchRequest,
			@PathVariable String collectionName, @PathVariable String className) throws ClassNotFoundException {
		
		Class<?> findType = Class.forName(className);
		
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		MongoDataView view = DataViewUtil.getMongoDataView(userView);
		
		return dao.findByQuery(searchRequest, collectionName, findType, view);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/t/{className}",
			consumes = "application/json", produces = "application/json")
	public Page<?> filterData(@RequestBody SearchRequest searchRequest,
			@PathVariable String className) throws ClassNotFoundException {
		
		Class<?> findType = Class.forName(className);
		
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		MongoDataView view = DataViewUtil.getMongoDataView(userView);
		
		return dao.findByQuery(searchRequest, findType, view);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cq/{containerQId}/",
			consumes = "application/json", produces = "application/json")
	public Page<?> filterContainerData(@RequestBody SearchRequest searchRequest,
			@PathVariable String containerQId) throws ClassNotFoundException {
		
		String templateId = ProcessContext.get().getTemplateId();
		TemplateFacade template = templateManager.getTemplateFacade(templateId);
		String collectionName = template.getCollectionName(containerQId);
		
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		MongoDataView view = DataViewUtil.getMongoDataView(userView);
		
		return dao.findByQuery(searchRequest, collectionName, Map.class, view);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/cfc/{containerQId}/",
			consumes = "application/json", produces = "application/json")
	public Map<String, List<RefListItemCount>> findContainerItemCountByRefListValue(
			@RequestBody SearchRequest searchRequest,
			@PathVariable String containerQId) throws ClassNotFoundException {
		
		DataView userView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		
		return uiFilterService.findRecordCountByRefListItem(containerQId, searchRequest, userView);
	}
	
	
}
