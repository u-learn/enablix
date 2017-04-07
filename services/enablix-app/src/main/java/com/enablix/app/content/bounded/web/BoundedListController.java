package com.enablix.app.content.bounded.web;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.bounded.BoundedListManager;
import com.enablix.app.content.bounded.DataItem;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.commons.xsdtopojo.BoundedType;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;

@RestController
@RequestMapping("bounded")
public class BoundedListController {

	@Autowired
	private BoundedListManager manager;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.GET, value="/list/{templateId}/{contentQId}", produces = "application/json")
	public Collection<DataItem> fetchBoundedListValues(@PathVariable String templateId, 
			@PathVariable String contentQId) {
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return manager.getBoundedList(templateId, contentQId, userDataView);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/d/list/", produces = "application/json")
	public Collection<DataItem> fetchBoundedDefListValues(@RequestBody BoundedType boundedType) {
		DataView userDataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		return manager.getBoundedList(boundedType, userDataView);
	}
	
}
