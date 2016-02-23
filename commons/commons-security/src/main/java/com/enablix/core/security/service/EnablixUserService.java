package com.enablix.core.security.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserRole;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserRoleRepository;
import com.enablix.core.security.web.UserAndRolesVO;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;

@Component
public class EnablixUserService implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TenantRepository tenantRepo;
	
	@Autowired
	private UserRoleRepository userRoleRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		User user = userRepo.findByUserId(userName);
		
		if (user == null) {
			throw new UsernameNotFoundException("[" + userName + "] not found");
		}
		
		Tenant tenant = tenantRepo.findByTenantId(user.getTenantId());
		
		String templateId = tenant == null ? "" : tenant.getDefaultTemplateId();
		
		// set up process context to fetch user roles from tenant specific database
		ProcessContext.initialize(user.getUserId(), user.getTenantId(), templateId);
		
		UserRole userRole = null;
		
		try {
			
			 userRole = userRoleRepo.findByUserIdentity(user.getIdentity());
			
		} finally {
			ProcessContext.clear();
		}
		
		
		return new LoggedInUser(user, templateId, userRole);
	}
	
	@Override
	public Boolean checkUserByUserId(String userId) {
		User user = userRepo.findByUserId(userId);
		return user != null;
	}	
	 
	@Override
	public User addUser(UserAndRolesVO userVO) {
		
		String password =UUID.randomUUID().toString().substring(0,8);//system generated default password
		
		if (userVO.getUser().getPassword()==null)
			userVO.getUser().setPassword(password);
		User newuser = userRepo.save(userVO.getUser());
		
		if (!userVO.getRoles().isEmpty()) {
			
			List<Role> roles = roleRepo.findByIdentityIn(userVO.getRoles());
			
			if (!CollectionUtil.isEmpty(roles)) {
				
				UserRole userRole = userRoleRepo.findByUserIdentity(newuser.getIdentity());
				
				if (userRole == null) {
					userRole = new UserRole();
					userRole.setUserIdentity(newuser.getIdentity());
				}
				
				userRole.setRoles(roles);
				userRoleRepo.save(userRole);
			}
		}
		
		return newuser;
	}
	
	@Override
	public List<User> getAllUsers(String tenantId) {
		return userRepo.findByTenantId(tenantId);
	}
	
	@Override
	public Boolean deleteUser(User user) {
		try {
			userRepo.delete(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static class LoggedInUser implements UserDetails {

		private static final long serialVersionUID = 1L;
		
		private User user;
		private String templateId;
		Collection<GrantedAuthority> auths;
		
		private LoggedInUser(User user, String templateId, UserRole userRole) {
			this.user = user;
			this.templateId = templateId;
			initAuthorities(userRole);
		}
		
		private void initAuthorities(UserRole userRole) {
			
			auths = new HashSet<>();
			
			if (userRole != null && userRole.getRoles() != null) {
			
				for (Role role : userRole.getRoles()) {
				
					if (role.getPermissions() != null) {
					
						for (String permission : role.getPermissions()) {
							auths.add(new SimpleGrantedAuthority(permission));
						}
					}
				}
			}
		}
		
		public User getUser() {
			return user;
		}

		public String getTemplateId() {
			return templateId;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
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

	@Override
	public UserAndRolesVO getUserByIdentity(String userIdentity, String tenantId) {
		
		UserAndRolesVO userVO = new UserAndRolesVO();
		
		User user = userRepo.findByIdentityAndTenantId(userIdentity, tenantId);
		
		if (user != null) {
		
			userVO.setUser(user);
			
			UserRole userRole = userRoleRepo.findByUserIdentity(userIdentity);
			
			if (userRole != null) {
				
				for (Role role : userRole.getRoles()) {
					userVO.addRole(role.getIdentity());
				}
			}
		}
		
		return userVO;
	}

}
