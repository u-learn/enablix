package com.enablix.core.security.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.enablix.core.security.AuditTrackingContextInitFilter;
import com.enablix.core.security.ProcessContextInitFilter;
import com.enablix.core.security.RequestContextFilter;

@Configuration
@EnableResourceServer
@Order(4)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		String[] systemUserRequestPatterns = {"/site/*", "site-doc/**/*"};
		
		http = http.addFilterAfter(new ProcessContextInitFilter(systemUserRequestPatterns), SwitchUserFilter.class);
		http = http.addFilterAfter(new AuditTrackingContextInitFilter(), SwitchUserFilter.class);
		http = http.addFilterBefore(new RequestContextFilter(), ProcessContextInitFilter.class);
		
		// only enable oauth resource server for request containing bearer token
		http.requestMatcher(new BearerRequestMatcher()) 
			.authorizeRequests().anyRequest().authenticated();
	}
	
	private static class BearerRequestMatcher implements RequestMatcher {

		@Override
		public boolean matches(HttpServletRequest request) {
			String auth = request.getHeader("Authorization");
			return (auth != null && auth.startsWith("Bearer"));
		}
		
	}
	
	@Bean
	public TokenStore defaultTokenStore() {
		return new OAuth2RepositoryTokenStore();
	}
	
}
