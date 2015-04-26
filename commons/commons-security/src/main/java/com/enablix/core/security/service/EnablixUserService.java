package com.enablix.core.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.user.User;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;

@Component
public class EnablixUserService implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TenantRepository tenantRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userRepo.findByUserId(userName);
		
		if (user == null) {
			throw new UsernameNotFoundException("[" + userName + "] not found");
		}
		
		Tenant tenant = tenantRepo.findByTenantId(user.getTenantId());
		
		return new LoggedInUser(user, tenant == null ? "" : tenant.getDefaultTemplateId());
	}
	
	public static class LoggedInUser implements UserDetails {

		private static final long serialVersionUID = 1L;
		
		private User user;
		private String templateId;
		
		private LoggedInUser(User user, String templateId) {
			this.user = user;
			this.templateId = templateId;
		}
		
		public User getUser() {
			return user;
		}

		public String getTemplateId() {
			return templateId;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Collection<GrantedAuthority> auths = new ArrayList<>();
			auths.add(new SimpleGrantedAuthority("ROLE_USER"));
			return auths;
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		@Override
		public String getUsername() {
			return user.getUserId();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
		
	}

}
