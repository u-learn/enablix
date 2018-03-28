package com.enablix.core.mongo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.enablix.core.domain.content.UserContentRelevance;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserContentRelevanceRepository extends BaseMongoRepository<UserContentRelevance> {

	Page<UserContentRelevance> findByUserIdAndLatestTrue(String userId, Pageable pageable);

	Long deleteByContentIdentity(String contentIdentity);
	
}
