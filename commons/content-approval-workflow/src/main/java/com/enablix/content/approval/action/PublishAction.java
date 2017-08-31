package com.enablix.content.approval.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.model.GenericActionResult;

public class PublishAction extends ContentSaveAction {

	@Autowired
	private ContentDataManager dataMgr;
	
	public PublishAction() {
		super();
	}
	
	@Override
	public String getActionName() {
		return ContentApprovalConstants.ACTION_PUBLISH;
	}

	@Override
	public GenericActionResult<ContentDetail, Boolean> execute(ContentDetail actionData, ContentDetail objectRef)
			throws ActionException {
		
		copyInputData(actionData, objectRef);
		
		String templateId = ProcessContext.get().getTemplateId();
		
		Map<String, Object> updatedData = dataMgr.saveData(new UpdateContentRequest(templateId, 
				objectRef.getParentIdentity(), objectRef.getContentQId(), objectRef.getData()));
		objectRef.setData(updatedData);
		
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
