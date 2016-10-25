package com.enablix.content.approval.repo;

import com.enablix.content.approval.model.ContentApproval;
import com.enablix.content.approval.model.ContentDetail;
import com.enablix.state.change.repo.StateChangeRecordingRepository;

public interface ContentApprovalRepository extends StateChangeRecordingRepository<ContentDetail, ContentApproval> {

}
