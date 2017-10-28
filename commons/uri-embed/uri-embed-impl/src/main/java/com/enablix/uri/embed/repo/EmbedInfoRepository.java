package com.enablix.uri.embed.repo;

import com.enablix.core.domain.uri.embed.EmbedInfo;
import com.enablix.core.mongo.repository.BaseMongoRepository;

public interface EmbedInfoRepository extends BaseMongoRepository<EmbedInfo> {

	EmbedInfo findByUrl(String url);
	
}
