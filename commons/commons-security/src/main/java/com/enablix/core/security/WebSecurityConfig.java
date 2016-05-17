package com.enablix.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

import com.enablix.core.security.service.EnablixUserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private EnablixUserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http = http.addFilterAfter(new ProcessContextInitFilter(), SwitchUserFilter.class);
		
		http
			.authorizeRequests()
				.antMatchers("/resetpassword", "/", "/health", "/site-doc/**/*", "/terms", "/privacy", "/**/*.html", "/**/*.js", "/**/*.json", "/**/*.css", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/*.svg", "/**/*.ttf", "/**/*.woff", "/**/*.woff2", "/**/*.eot").permitAll()
				.anyRequest().authenticated()
			//.and().addFilter(new ProcessContextInitFilter()).exceptionHandling()
			.and().httpBasic()
			.and().logout().logoutSuccessUrl("/").permitAll()
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

}
