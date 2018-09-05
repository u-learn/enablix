package com.enablix.app.content.pack.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.pack.impl.SelectedContentPackManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.api.SelectedContentPack;
import com.enablix.core.domain.content.pack.ContentPack;
import com.enablix.data.segment.DataSegmentService;
import com.enablix.data.view.DataView;

@RestController
@RequestMapping("contentpack")
public class ContentPackController {

	@Autowired
	private SelectedContentPackManager selContentPackMgr;
	
	@Autowired
	private DataSegmentService dataSegmentService;
	
	@RequestMapping(method = RequestMethod.POST, value="/selcontent/update", produces = "application/json")
	public ContentPack getWidgetContent(@RequestBody SelectedContentPack request) {
		
		DataView dataView = dataSegmentService.getDataViewForUserId(ProcessContext.get().getUserId());
		
		return selContentPackMgr.saveOrUpdate(request, dataView);
	}
	
}
