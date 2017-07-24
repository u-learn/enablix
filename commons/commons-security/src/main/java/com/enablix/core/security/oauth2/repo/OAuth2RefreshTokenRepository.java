package com.enablix.core.security.oauth2.repo;

import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.core.security.oauth2.OAuth2AuthRefreshToken;

public interface OAuth2RefreshTokenRepository extends BaseMongoRepository<OAuth2AuthRefreshToken> {

	 public OAuth2AuthRefreshToken findByTokenId(String tokenId);
	
}
