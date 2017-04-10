package com.enablix.data.segment.repo;

import com.enablix.core.domain.segment.DataSegmentSpec;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface DataSegmentSpecRepository extends BaseMongoRepository<DataSegmentSpec> {

	DataSegmentSpec findByTemplateId(String templateId);
	
}
