package com.enablix.core.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

//@Configuration
//@EnableAuthorizationServer
// Require this class when we move to OAuth2 completely
public class AuthServerOAuth2Config extends AuthorizationServerConfigurerAdapter {
  
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
 
    @Override
    public void configure(
      AuthorizationServerSecurityConfigurer oauthServer) 
      throws Exception {
        oauthServer
          .tokenKeyAccess("permitAll()")
          .checkTokenAccess("isAuthenticated()");
    }
 
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) 
      throws Exception {
        clients.inMemory()
          .withClient("clientIdPassword")
          .secret("secret")
          .authorizedGrantTypes(
            "password","authorization_code", "refresh_token")
          .scopes("read");
    }
 
    @Override
    public void configure(
      AuthorizationServerEndpointsConfigurer endpoints) 
      throws Exception {
  
        endpoints
          .tokenStore(tokenStore())
          .authenticationManager(authenticationManager);
    }
 
    @Bean
    public TokenStore tokenStore() {
        return new OAuth2RepositoryTokenStore();
    }
}
