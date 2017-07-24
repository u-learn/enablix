package com.enablix.core.security.oauth2;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.enablix.core.domain.BaseEntity;

@Document(collection = "ebx_oauth2_refresh_token")
public class OAuth2AuthRefreshToken extends BaseEntity {

	@Id
	private String id;
	
	private String tokenId;
    
	private OAuth2RefreshToken oAuth2RefreshToken;
    
	private OAuth2Authentication authentication;

    public OAuth2AuthRefreshToken() {
    	// for ORM
    }
    
    public OAuth2AuthRefreshToken(OAuth2RefreshToken oAuth2RefreshToken, OAuth2Authentication authentication) {
        this.oAuth2RefreshToken = oAuth2RefreshToken;
        this.authentication = authentication;
        this.tokenId = oAuth2RefreshToken.getValue();
    }

    public String getTokenId() {
        return tokenId;
    }

    public OAuth2RefreshToken getoAuth2RefreshToken() {
        return oAuth2RefreshToken;
    }

    public OAuth2Authentication getAuthentication() {
        return authentication;
    }
	
}
