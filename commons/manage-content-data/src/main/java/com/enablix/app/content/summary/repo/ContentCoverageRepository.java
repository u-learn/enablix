package com.enablix.app.content.summary.repo;

import java.util.Collection;
import java.util.List;

import com.enablix.core.domain.content.summary.ContentCoverage;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface ContentCoverageRepository extends BaseMongoRepository<ContentCoverage> {

	List<ContentCoverage> findByIdentityInAndLatestTrue(Collection<String> identity);
	
}
