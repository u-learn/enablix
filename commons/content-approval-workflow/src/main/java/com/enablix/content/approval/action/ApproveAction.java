package com.enablix.content.approval.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ContentDataManager;
import com.enablix.app.content.update.UpdateContentRequest;
import com.enablix.app.content.update.UpdateContentResponse;
import com.enablix.commons.util.SudoExecutor;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.content.approval.ContentApprovalConstants;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.model.GenericActionResult;
import com.enablix.state.change.model.SimpleActionInput;
import com.enablix.state.change.model.StateChangeRecording;

public class ApproveAction extends BaseContentAction<SimpleActionInput, Boolean> {

	@Autowired
	private ContentDataManager dataMgr;
	
	public ApproveAction() {
		super(SimpleActionInput.class);
	}
	
	@Override
	public String getActionName() {
		return ContentApprovalConstants.ACTION_APPROVE;
	}

	@Override
	public GenericActionResult<ContentDetail, Boolean> execute(
				SimpleActionInput actionData, ContentDetail objectRef, 
				StateChangeRecording<ContentDetail> recording)
			throws ActionException {
		
		String templateId = ProcessContext.get().getTemplateId();
		
		Map<String, Object> record = objectRef.getData();

		SudoExecutor.runAsUser(recording.getCreatedBy(), recording.getCreatedByName(), () -> {

			UpdateContentResponse updatedData = dataMgr.saveData(new UpdateContentRequest(templateId, 
					objectRef.getParentIdentity(), objectRef.getContentQId(), record));

			// TODO: handle quality alerts
			objectRef.setData(updatedData.getContentRecord());

		});
		
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
