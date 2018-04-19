package com.enablix.core.mongo.content.repo;

import com.enablix.core.domain.content.ImportRequest;
import com.enablix.core.domain.content.ImportStatus;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ImportRequestRepository extends BaseMongoRepository<ImportRequest> {

	ImportRequest findByIdentityAndStatus(String requestIdentity, ImportStatus status);
	
}
