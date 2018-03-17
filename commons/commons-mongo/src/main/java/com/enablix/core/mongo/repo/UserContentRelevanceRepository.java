package com.enablix.core.mongo.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.enablix.core.domain.content.UserContentRelevance;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface UserContentRelevanceRepository extends BaseMongoRepository<UserContentRelevance> {

	List<UserContentRelevance> findByUserIdAndLatestTrue(String userId, Pageable pageable);
	
}
