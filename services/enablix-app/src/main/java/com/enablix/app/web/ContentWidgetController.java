package com.enablix.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.app.content.ui.DisplayContext;
import com.enablix.app.content.ui.DisplayableContentService;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.activity.audit.ActivityTrackingContext;
import com.enablix.core.api.ContentDataRef;
import com.enablix.core.domain.activity.ActivityChannel.Channel;
import com.enablix.core.domain.activity.ContentActivity.ContainerType;
import com.enablix.core.ui.DisplayableContent;
import com.enablix.data.view.DataView;
import com.enablix.services.util.ActivityLogger;
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
		
		String sharedWith = ProcessContext.get().getUserId();
		
		dsplyContentService.postProcessDisplayableContent(dispRecord, sharedWith, ctx);
		dsplyContentService.populatePreviewInfo(dispRecord, ctx);
		
		// audit content widget view
		ContentDataRef contentDataRef = ContentDataRef.createContentRef(ProcessContext.get().getTemplateId(), 
				dispRecord.getContainerQId(), dispRecord.getRecordIdentity(), dispRecord.getTitle());
		
		Channel channel = ActivityTrackingContext.get().getActivityChannel(Channel.CONTENTWIDGET);
		ActivityLogger.auditContentEmbedView(contentDataRef, ContainerType.CONTENT, channel);
		
		return dispRecord;
		
	}
	
	private DisplayContext widgetDisplayContext() {
		
		DisplayContext ctx = new DisplayContext();
		ctx.setDisplayChannel(Channel.CONTENTWIDGET);
		
		ctx.setTrackingParams(ActivityTrackingContext.get().getAuditContextParams());
		
		return ctx;
	}
	
}
