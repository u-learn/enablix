package com.enablix.core.security.oauth2.repo;

import java.util.List;

import com.enablix.core.mongo.repository.BaseMongoRepository;
import com.enablix.core.security.oauth2.OAuth2AuthAccessToken;

public interface OAuth2AccessTokenRepository extends BaseMongoRepository<OAuth2AuthAccessToken> {

	public OAuth2AuthAccessToken findByTokenId(String tokenId);

    public OAuth2AuthAccessToken findByRefreshToken(String refreshToken);

    public OAuth2AuthAccessToken findByAuthenticationId(String authenticationId);

    public List<OAuth2AuthAccessToken> findByClientIdAndUserName(String clientId, String userName);

    public List<OAuth2AuthAccessToken> findByClientId(String clientId);
	
}
