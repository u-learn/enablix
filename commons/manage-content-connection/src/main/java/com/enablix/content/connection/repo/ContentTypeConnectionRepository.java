package com.enablix.content.connection.repo;

import java.util.List;

import com.enablix.core.domain.content.connection.ContentTypeConnection;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ContentTypeConnectionRepository extends BaseMongoRepository<ContentTypeConnection> {

	List<ContentTypeConnection> findByHoldingContainers(String containerQId);

	List<ContentTypeConnection> findByContentQId(String contentQId);
	
}
