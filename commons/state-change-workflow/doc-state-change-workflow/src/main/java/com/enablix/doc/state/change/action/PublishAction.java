package com.enablix.doc.state.change.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.enablix.app.content.doc.DocumentManager;
import com.enablix.commons.util.StringUtil;
import com.enablix.doc.state.change.DocStateChangeConstants;
import com.enablix.doc.state.change.model.DocActionResult;
import com.enablix.doc.state.change.model.DocInfo;
import com.enablix.doc.state.change.model.PublishInfo;
import com.enablix.state.change.ActionException;
import com.enablix.state.change.StateChangeAction;
import com.enablix.state.change.model.StateChangeRecording;

public class PublishAction implements StateChangeAction<DocInfo, PublishInfo, Boolean, DocActionResult<Boolean>> {

	@Autowired
	private DocumentManager docManager;
	
	@Override
	public String getActionName() {
		return DocStateChangeConstants.ACTION_PUBLISH;
	}

	@Override
	public DocActionResult<Boolean> execute(PublishInfo pubInfo, DocInfo objectRef, 
			StateChangeRecording<DocInfo> recording) throws ActionException {
		
		try {
		
			if (!StringUtil.isEmpty(pubInfo.getParentIdentity())) {
				
				docManager.attachUsingParentInfo(objectRef.getMetadata(), 
						pubInfo.getDocIdentity(), pubInfo.getParentIdentity());
				
			} else {
				
				docManager.attachUsingContainerInfo(objectRef.getMetadata(), 
						pubInfo.getDocQId(), pubInfo.getDocContainerIdentity());
			}
		
		} catch (Throwable t) {
			throw new ActionException("Error in publish action", t);
		}
		
		return new DocActionResult<Boolean>(objectRef, Boolean.TRUE);
	}

	@Override
	public Class<PublishInfo> getInputType() {
		return PublishInfo.class;
	}

}
