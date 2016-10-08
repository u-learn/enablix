package com.enablix.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import com.enablix.core.security.service.EnablixUserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String PERSISTENT_REMEMBER_ME_KEY = "3nabl1xr0cks";
	
	@Autowired
	private EnablixUserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String[] systemUserRequestPatterns = {"/site/*", "site-doc/**/*"};
		http = http.addFilterAfter(new ProcessContextInitFilter(systemUserRequestPatterns), SwitchUserFilter.class);
		
		// Add guest login filter ahead of username/password filter. Also, read the following:
		// http://mtyurt.net/2015/07/15/spring-how-to-insert-a-filter-before-springsecurityfilterchain/
		http = http.addFilterBefore(guestLoginFilter(), FilterSecurityInterceptor.class);
		
		http.headers().frameOptions().sameOrigin();
		
		http
			.authorizeRequests()
				.antMatchers("/resetpassword", "/", "/health", "/site-doc/**/*", "/site/*", "/terms", "/privacy", 
						"/**/*.html", "/**/*.js", "/**/*.json", "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", 
						"/**/*.gif", "/**/*.svg", "/**/*.ttf", "/**/*.woff", "/**/*.woff2", "/**/*.eot").permitAll()
				.anyRequest().authenticated()
			//.and().addFilter(new ProcessContextInitFilter()).exceptionHandling()
			.and().httpBasic()
			
			// http://docs.spring.io/spring-security/site/docs/3.0.x/reference/remember-me.html#remember-me-persistent-token
			//.and().rememberMe().rememberMeServices(rememberMeServices()).key(PERSISTENT_REMEMBER_ME_KEY)
			.and().logout().logoutSuccessHandler(logoutSuccessHandler()).permitAll()
			.and().csrf().disable();
			
			//.and().csrf().csrfTokenRepository(csrfTokenRepository());
			// logout not working and hence disabling it for the time being
			// {"timestamp":1429957911657,"status":403,"error":"Forbidden","message":"Expected CSRF token not found. Has your session expired?","path":"/logout"}
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}
	 
/*	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}*/

	@Bean
	public GuestUserLoginFilter guestLoginFilter() {
		return new GuestUserLoginFilter();
	}
	
	@Bean
	public RememberMeServices rememberMeServices() {
		return new BasicAuthPersistedRememberMeServices(PERSISTENT_REMEMBER_ME_KEY, 
					userService, mongoPersistentTokenRepository());
	}
	
	@Bean
	public MongoPersistentTokenRepository mongoPersistentTokenRepository() {
		return new MongoPersistentTokenRepository();
	}
	
	public CustomLogoutSuccessHandler logoutSuccessHandler() {
		CustomLogoutSuccessHandler handler = new CustomLogoutSuccessHandler();
		handler.setDefaultTargetUrl("/login.html#/login");
		return handler;
	}
	
}
