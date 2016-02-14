package com.enablix.core.security.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserRole;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserRoleRepository;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;

@Component
public class EnablixUserService implements UserService, UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

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
		
		LOGGER.debug("loadUserByUsername initiated");
		User user = userRepo.findByUserId(userName);
		
		if (user == null) {
			LOGGER.debug("UserName not found " +  userName);
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
	/**
	 * 
	 * @author ganesh
	 * @return boolean
	 *  
	 */
	
	@Override
	public Boolean checkUserbyUserName(String userName) {
		LOGGER.debug("CheckUserByUserName is initiated" + userName);
		User user=userRepo.findByUserId(userName);
		if(user==null)
		{
			LOGGER.debug("User Not found by UserName"+userName);
			return false;		  
		}else {
			LOGGER.debug("User found by UserName"+userName);
			return true;
		}
	};	
	 
	/**
	 * @author ganesh 
	 */
	
	@Override
	public User addUser(User user) {
		LOGGER.debug("Add User initiated"+user.toString());
		User newuser=userRepo.save(user);
		newuser.getUserRole().setUserIdentity(newuser.getUserId());
		userRoleRepo.save(newuser.getUserRole());
		LOGGER.debug("User Added successfully" + user.toString());
		return newuser;
	}
	
	
	@Override
	public List<User> getAllUsers() {
		 List<User> userList=userRepo.findAll();
		 List<User> newUserList=new ArrayList<>();
		 for (User user : userList)
		 {				
			 UserRole userRole=userRoleRepo.findByUserIdentity(user.getUserId());
			 user.setUserRole(userRole);
			newUserList.add(user);
			 
		 };
		return newUserList;	
	};
	
	@Override
	public Boolean deleteUser(User user) {
		LOGGER.debug("Delete User initiated");
		try{		
			userRepo.delete(user);
			userRoleRepo.delete(user.getUserRole());
			LOGGER.debug("User deleted successfully");
			return true;
		}catch(Exception e)
		{
		   LOGGER.debug("Exception occured while deleting User "+e.getMessage());	
			return false;
		}
	};
	
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
	public List<Role> getRoleS() {
		return roleRepo.findAll();
	};

}
