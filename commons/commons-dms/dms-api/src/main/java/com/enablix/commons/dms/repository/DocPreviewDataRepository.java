package com.enablix.commons.dms.repository;

import com.enablix.commons.dms.api.DocPreviewData;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface DocPreviewDataRepository extends BaseMongoRepository<DocPreviewData> {

	DocPreviewData findByDocIdentity(String docIdentity);
	
}
