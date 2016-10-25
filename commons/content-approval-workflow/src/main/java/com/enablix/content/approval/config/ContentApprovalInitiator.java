package com.enablix.content.approval.config;

import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.definition.RecordingInstantiator;

public class ContentApprovalInitiator implements RecordingInstantiator<ContentDetail, ContentApproval> {

	@Override
	public ContentApproval newInstance() {
		return new ContentApproval();
	}

}
