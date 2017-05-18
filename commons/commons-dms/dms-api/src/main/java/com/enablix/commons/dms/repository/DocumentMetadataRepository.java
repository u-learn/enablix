package com.enablix.commons.dms.repository;

import java.util.Collection;

import com.enablix.commons.dms.api.DocumentMetadata;
import com.enablix.commons.dms.api.DocumentMetadata.PreviewStatus;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface DocumentMetadataRepository extends BaseMongoRepository<DocumentMetadata> {

	Collection<DocumentMetadata> findByPreviewStatusExistsOrPreviewStatus(boolean exists, PreviewStatus pending);

}
