package com.enablix.core.security.oauth2;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.enablix.core.domain.BaseEntity;

@Document(collection = "ebx_oauth2_access_token")
public class OAuth2AuthAccessToken extends BaseEntity {
	
	@Id
	private String id;
	
	private String tokenId;
	
    private OAuth2AccessToken oAuth2AccessToken;
    
    private String authenticationId;
    
    private String userName;
    
    private String clientId;
    
    private OAuth2Authentication authentication;
    
    private String refreshToken;
    
    private boolean alwaysPersist;
	
    public OAuth2AuthAccessToken() {
    }

    public OAuth2AuthAccessToken(final OAuth2AccessToken oAuth2AccessToken, 
    		final OAuth2Authentication authentication, final String authenticationId) {
    	
        this.tokenId = oAuth2AccessToken.getValue();
        this.oAuth2AccessToken = oAuth2AccessToken;
        this.authenticationId = authenticationId;
        this.userName = authentication.getName();
        this.clientId = authentication.getOAuth2Request().getClientId();
        this.authentication = authentication;
        this.refreshToken = oAuth2AccessToken.getRefreshToken().getValue();
        this.alwaysPersist = false;
    }

    public String getTokenId() {
        return tokenId;
    }

    public OAuth2AccessToken getoAuth2AccessToken() {
        return oAuth2AccessToken;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public String getUserName() {
        return userName;
    }

    public String getClientId() {
        return clientId;
    }

    public OAuth2Authentication getAuthentication() {
        return authentication;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

	public boolean isAlwaysPersist() {
		return alwaysPersist;
	}
}
