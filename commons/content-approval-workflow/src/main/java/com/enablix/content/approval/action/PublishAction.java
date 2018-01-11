package com.enablix.content.approval.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.model.GenericActionResult;
import com.enablix.state.change.model.StateChangeRecording;

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
	public GenericActionResult<ContentDetail, Boolean> execute(
				ContentDetail actionData, ContentDetail objectRef,
				StateChangeRecording<ContentDetail> recording)
			throws ActionException {
		
		copyInputData(actionData, objectRef);
		
		String templateId = ProcessContext.get().getTemplateId();
		
		UpdateContentResponse updatedData = dataMgr.saveData(new UpdateContentRequest(templateId, 
				objectRef.getParentIdentity(), objectRef.getContentQId(), objectRef.getData()));
		
		// TODO: handle quality alerts
		objectRef.setData(updatedData.getContentRecord());
		
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
