package com.enablix.ui.widget.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.UIWidget;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;
import com.enablix.ui.widget.UIWidgetDataRequest;
import com.enablix.ui.widget.UIWidgetService;

@RestController
@RequestMapping("uiwdg")
public class UIWidgetController {

	@Autowired
	private UIWidgetService uiWidgetService;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.GET, value="/data/{widgetIdentity}/", produces = "application/json")
	public UIWidget getWidgetContent(@PathVariable String widgetIdentity,
			@RequestParam(name = "pageNo", defaultValue="0") int pageNo,
			@RequestParam(name = "pageSize", defaultValue="5") int pageSize) {
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		
		UIWidgetDataRequest dataRequest = new UIWidgetDataRequest();
		dataRequest.setPageNo(pageNo);
		dataRequest.setPageSize(pageSize);
		
		return uiWidgetService.getWidget(widgetIdentity, dataView, dataRequest);
		
	}
	
}
