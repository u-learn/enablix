package com.enablix.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentService;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;
import com.enablix.services.util.DataViewUtil;

@RestController
@RequestMapping("cwdg")
public class ContentWidgetController {

	@Autowired
	private DisplayableContentService dsplyContentService;
	
	@RequestMapping(method = RequestMethod.GET, value="/dc/{contentQId}/{recordIdentity}/", produces = "application/json")
	public DisplayableContent getWidgetContent(@PathVariable String contentQId, @PathVariable String recordIdentity) {
		
		DataView view = DataViewUtil.allDataView();
		DisplayContext ctx = widgetDisplayContext();
		
		DisplayableContent dispRecord = dsplyContentService.getDisplayableContent(
				contentQId, recordIdentity, view, ctx);
		
		dsplyContentService.postProcessDisplayableContent(dispRecord, "content-widget", ctx);
		dsplyContentService.populatePreviewInfo(dispRecord, ctx);
		
		return dispRecord;
		
	}
	
	private DisplayContext widgetDisplayContext() {
		
		DisplayContext ctx = new DisplayContext();
		ctx.setDisplayChannel(Channel.CONTENTWIDGET);
		
		return ctx;
	}
	
}
