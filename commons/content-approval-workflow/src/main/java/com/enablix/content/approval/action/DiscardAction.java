package com.enablix.content.approval.action;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.ContentDataManager;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.StateChangeConstants;
import com.enablix.state.change.model.GenericActionResult;
import com.enablix.state.change.model.SimpleActionInput;
import com.enablix.state.change.model.StateChangeRecording;

public class DiscardAction extends BaseContentAction<SimpleActionInput, Boolean> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DiscardAction.class);
	
	@Autowired
	private ContentDataManager dataMgr;
	
	public DiscardAction() {
		super(SimpleActionInput.class);
	}
	
	@Override
	public String getActionName() {
		return StateChangeConstants.ACTION_DISCARD;
	}

	@Override
	public GenericActionResult<ContentDetail, Boolean> execute(
				SimpleActionInput actionData, ContentDetail objectRef, 
				StateChangeRecording<ContentDetail> recording)
			throws ActionException {
		
		try {
			
			dataMgr.deleteRecordDocuments(objectRef.getData(), objectRef.getContentQId());
			
		} catch (IOException e) {
			LOGGER.error("Error deleting documents", e);
			throw new ActionException("Error deleting documents", e);
		}
		
		return new GenericActionResult<ContentDetail, Boolean>(objectRef, Boolean.TRUE);
	}

}
