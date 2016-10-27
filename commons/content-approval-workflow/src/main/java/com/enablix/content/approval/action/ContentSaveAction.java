package com.enablix.content.approval.action;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.template.service.TemplateManager;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.core.commons.xsdtopojo.ContentTemplate;
import com.enablix.services.util.ContentDataUtil;
import com.enablix.state.change.model.GenericActionResult;

public abstract class ContentSaveAction extends BaseContentAction<ContentDetail, Boolean> {

	@Autowired
	private TemplateManager templateMgr;
	
	public ContentSaveAction() {
		super(ContentDetail.class);
	}
	
	protected GenericActionResult<ContentDetail, Boolean> copyInputData(ContentDetail actionData,
			ContentDetail objectRef) {
		
		// evaluate title of the content
		ContentTemplate template = templateMgr.getTemplate(ProcessContext.get().getTemplateId());
		String title = ContentDataUtil.findPortalLabelValue(actionData.getData(), template, actionData.getContentQId());
		actionData.setContentTitle(title);
		
		BeanUtils.copyProperties(actionData, objectRef);
		
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
