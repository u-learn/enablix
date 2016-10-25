package com.enablix.doc.state.change.repo;

import com.enablix.doc.state.change.model.DocInfo;
import com.enablix.doc.state.change.model.DocPublishing;
import com.enablix.state.change.repo.StateChangeRecordingRepository;

public interface DocPublishingRepository extends StateChangeRecordingRepository<DocInfo, DocPublishing> {

}
