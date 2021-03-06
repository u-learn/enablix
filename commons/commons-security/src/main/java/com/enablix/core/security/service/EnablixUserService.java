package com.enablix.core.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.enablix.commons.util.StringUtil;
import com.enablix.commons.util.collection.CollectionUtil;
import com.enablix.commons.util.concurrent.Events;
import com.enablix.commons.util.id.IdentityUtil;
import com.enablix.commons.util.json.JsonUtil;
import com.enablix.commons.util.process.ProcessContext;
import com.enablix.commons.util.process.RequestContext;
import com.enablix.core.api.WebPortal;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserBusinessProfile;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.security.authorization.UserSystemProfile;
import com.enablix.core.domain.tenant.Tenant;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.entities.MailEvent;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.mail.utility.MailConstants;
import com.enablix.core.mq.Event;
import com.enablix.core.mq.util.EventUtil;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.auth.repo.UserProfileRepository;
import com.enablix.core.system.repo.TenantRepository;
import com.enablix.core.system.repo.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
@Component
public class EnablixUserService implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private UserProfileRepository userProfileRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private GuestUserProviderFactory guestUserProviderFactory;

	@Autowired
	private MailService mailService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		String tenantId = null;
		RequestContext requestContext = RequestContext.get();
		if (requestContext != null) {
			tenantId = requestContext.getTenantId();
		}
		
		User user = !StringUtil.hasText(tenantId) ? userRepo.findByUserId(userName.toLowerCase())
						: userRepo.findByUserIdAndTenantId(userName.toLowerCase(), tenantId);
		
		UserProfile userProfile = null;
		if (user == null) {
			throw new UsernameNotFoundException("[" + userName + "] not found");
		}

		Tenant tenant = tenantRepo.findByTenantId(user.getTenantId());

		String templateId = tenant == null ? "" : tenant.getDefaultTemplateId();
		String portal = tenant == null ? WebPortal.defaultPortal().getName() : tenant.getPortal();
		
		try {
			// set up process context to fetch user roles from tenant specific database
			//Setting the name also as the user id
			ProcessContext.initialize(user.getUserId(), user.getUserId(), user.getTenantId(), templateId, null);

			userProfile = userProfileRepo.findByUserIdentity(user.getIdentity());

		} finally {
			ProcessContext.clear();
		}

		return new LoggedInUser(user, templateId, userProfile.getSystemProfile().getRoles(), userProfile, portal);
	}

	@Override
	public Boolean checkUserByUserId(String userId) {
		User user = userRepo.findByUserId(userId.toLowerCase());
		return user != null;
	}	

	@Override
	public void resetPassword(String userid){

		User user = userRepo.findByUserId(userid.toLowerCase());

		String password =UUID.randomUUID().toString().substring(0,8);//system generated default password
		user.setPassword(password);
		user.setIsPasswordSet(false);
		
		try {
			
			ProcessContext.clear();
			ProcessContext.initialize(userid.toLowerCase(), userid.toLowerCase(), user.getTenantId(), null, null);
			userRepo.save(user);
			mailService.sendHtmlEmail(user, userid, MailConstants.SCENARIO_RESET_PASSWORD);
			
		} finally{
			ProcessContext.clear();
		}
	}	

	@Override
	public User changePassword(User user){

		User modUser = userRepo.findOne(user.getId());

		modUser.setPassword(user.getPassword());
		modUser.setIsPasswordSet(true);
		
		try {
			
			//Setting the name also as the user id
			ProcessContext.clear();
			ProcessContext.initialize(modUser.getUserId(), modUser.getUserId(), modUser.getTenantId(), null, null);
			userRepo.save(modUser);		

			mailService.sendHtmlEmail(modUser,modUser.getUserId(),"passwordconfirmation");
			
		} finally{
			ProcessContext.clear();
		}
		
		return modUser;
	}	

	@Override
	public User addUser(String userDataJSON) {

		JsonNode userData = JsonUtil.getJSONNode(userDataJSON);

		User user = JsonUtil.<User>jsonToJavaPojo(userData.get("user"), User.class);

		UserSystemProfile userSystemProfile = getSystemProfile(userData.get("userProfile").get("systemProfile"));

		UserProfile userProfile = JsonUtil.<UserProfile>jsonToJavaPojo(userData.get("userProfile"), UserProfile.class);

		UserBusinessProfile usrBusinessProfile = getBusinessProfile(userData.get("userProfile").get("businessProfile"));

		if (user.getPassword() == null) {
			String password =UUID.randomUUID().toString().substring(0,8);//system generated default password
			user.setPassword(password);
		}

		User newuser = userRepo.save(user);
		
		userProfile.setIdentity(IdentityUtil.generateIdentity(newuser));
		userProfile.setUserIdentity(newuser.getIdentity());
		userProfile.setId(newuser.getId());
		userProfile.setSystemProfile(userSystemProfile);
		userProfile.setBusinessProfile(usrBusinessProfile);
		
		userProfile = userProfileRepo.save(userProfile);

		mailService.sendHtmlEmail(newuser, userProfile.getEmail(), "setpassword");
		
		EventUtil.publishEvent(new Event<UserProfile>(Events.USER_ADDED, userProfile));

		return newuser;
	}
	
	@Override
	public UserProfile addOrUpdateUser(UserProfile up) {
		return addOrUpdateUser(up, null, "setpassword");
	}	
	
	public UserProfile signupUser(UserProfile up, String password) {
		return addOrUpdateUser(up, password, "usersignup");
	}
	
	public UserProfile addOrUpdateUser(UserProfile up, String password, String mailScenario) {
		
		boolean addUser = false;
		User user = null;
		String userIdentity = up.getUserIdentity();
		
		if (!StringUtil.isEmpty(userIdentity)) {
			// editing 
			user = userRepo.findByIdentity(userIdentity);
			
		} else {
			// add new user
			addUser = true;
			user = new User();
			user.setIdentity(up.getEmail());
			user.setUserId(up.getEmail());

			boolean passwordSet = false;
			
			if (StringUtil.hasText(password)) {
				passwordSet = true; 
			} else {
				password = UUID.randomUUID().toString().substring(0,8); //system generated default password
			}
			
			user.setPassword(password);
			user.setTenantId(ProcessContext.get().getTenantId());
			user.setIsPasswordSet(passwordSet);
			user.setSystem(Boolean.FALSE);
			
			user = userRepo.save(user);
		}
		
		UserProfile modUP = null;
		String upIdentity = up.getIdentity();
		
		if (StringUtil.isEmpty(upIdentity)) {
			modUP = up;
		} else {
			modUP = userProfileRepo.findByIdentity(upIdentity);
		}
		
		modUP.setUserIdentity(user.getIdentity());
		modUP.setName(up.getName());
		modUP.setSystemProfile(createSystemProfile(up.getSystemProfile()));
		modUP.setBusinessProfile(up.getBusinessProfile());
		
		modUP = userProfileRepo.save(modUP);
		
		if (addUser) {
			//mailService.sendHtmlEmail(user, modUP.getEmail(), mailScenario);
			EventUtil.publishEvent(new Event<MailEvent>(Events.SEND_EMAIL, new MailEvent(user, modUP.getEmail(), mailScenario)));
			EventUtil.publishEvent(new Event<UserProfile>(Events.USER_ADDED, modUP));
		} else {
			EventUtil.publishEvent(new Event<UserProfile>(Events.USER_UPDATED, modUP));
		}
		
		return modUP;
	}

	@Override
	public User editUser(String userDataJSON) {

		JsonNode userData = JsonUtil.getJSONNode(userDataJSON);

		User user = JsonUtil.<User>jsonToJavaPojo(userData.get("user"), User.class);

		UserSystemProfile userSystemProfile = getSystemProfile(userData.get("userProfile").get("systemProfile"));

		UserProfile userProfile = JsonUtil.<UserProfile>jsonToJavaPojo(userData.get("userProfile"), UserProfile.class);

		UserBusinessProfile usrBusinessProfile = getBusinessProfile(userData.get("userProfile").get("businessProfile"));

		if (user.getPassword() == null) {
			String password = UUID.randomUUID().toString().substring(0,8);//system generated default password
			user.setPassword(password);
		}
		
		User modUser  = userRepo.findByUserId(user.getUserId());
		if (modUser == null) {
			modUser = user;
		}
		modUser.setUserId(user.getUserId());
		userRepo.save(modUser);

		UserProfile modUserProfile = userProfileRepo.findByUserIdentity(modUser.getIdentity());
		if (modUserProfile == null) {
			modUserProfile = userProfile;
			modUserProfile.setUserIdentity(modUser.getIdentity());
		}
		modUserProfile.setId(userProfile.getId());
		modUserProfile.setName(userProfile.getName());
		modUserProfile.setSystemProfile(userSystemProfile);
		modUserProfile.setBusinessProfile(usrBusinessProfile);
		userProfileRepo.save(modUserProfile);

		return modUser;
	}

	private UserBusinessProfile getBusinessProfile(JsonNode jsonNode) {
		
		UserBusinessProfile usrBusinessProfile = new UserBusinessProfile();
		
		Map<String, Object> userBusinessProfileMap = JsonUtil.jsonToMap(jsonNode.toString());
		usrBusinessProfile.setAttributes(userBusinessProfileMap);
		
		return usrBusinessProfile;
	}

	private UserSystemProfile getSystemProfile(JsonNode systemProfile) {
		
		JsonNode roleArr = systemProfile.get("systemRoles");
		
		List<String> roleArrLst = new ArrayList<String>();
		if (roleArr.isArray()) {
			for (final JsonNode objNode : roleArr) {
				roleArrLst.add(objNode.asText());
			}
		}

		UserSystemProfile userSystemProfile = JsonUtil.<UserSystemProfile>jsonToJavaPojo(systemProfile, UserSystemProfile.class);

		List<Role> roles = roleRepo.findByIdentityIn(roleArrLst);

		if (!CollectionUtil.isEmpty(roles)) {
			userSystemProfile.setRoles(roles);
		}
		
		return userSystemProfile;
	}
	
	private UserSystemProfile createSystemProfile(UserSystemProfile systemProfile) {
		
		List<Role> roles = new ArrayList<>();
		
		List<Role> inRoles = systemProfile.getRoles();

		if (CollectionUtil.isNotEmpty(inRoles)) {
		
			List<String> roleArrLst = new ArrayList<>();
			inRoles.forEach((role) -> roleArrLst.add(role.getIdentity()));
			
			roles = roleRepo.findByIdentityIn(roleArrLst);
		}
		
		systemProfile.setRoles(roles);
		
		return systemProfile;
	}

	@Override
	public List<UserProfile> getAllUsers(String tenantId) {
		return userProfileRepo.findAllByOrderByNameAsc();
	}

	@Override
	public Boolean deleteUser(String identity) {
		
		try {
			
			UserProfile usrProfile = userProfileRepo.findByIdentity(identity);
			userProfileRepo.deleteByIdentity(usrProfile.getIdentity());
			userRepo.deleteByIdentity(usrProfile.getUserIdentity());
			
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void checkAndUpdateUserProfile(LoggedInUser user, UserProfile newProfile) {
		if (newProfile != null) {
			if (user.getUserProfile().getId().equals(newProfile.getId())) {
				user.userProfile = newProfile;
			}
		}
	}

	public static class LoggedInUser implements UserDetails {

		private static final long serialVersionUID = 1L;
		private UserProfile userProfile;
		private User user;
		private String templateId;
		private String portal;
		private Collection<GrantedAuthority> auths;

		@SuppressWarnings("unused")
		private LoggedInUser() {
			// for ORM
		}
		
		public LoggedInUser(User user, String templateId, List<Role> roles, UserProfile userProfile, String portal) {
			this.user = user;
			this.templateId = templateId;
			this.userProfile = userProfile;
			this.portal = portal;
			initAuthorities(roles);
		}

		private void initAuthorities(List<Role> roles) {

			auths = new HashSet<>();

			if (roles != null) {

				for (Role role : roles) {

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

		public String getDisplayName() {
			return userProfile.getName();
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

		public UserProfile getUserProfile() {
			return userProfile;
		}

		public String getPortal() {
			return portal;
		}

	}

	@Override
	public UserProfile getUserByIdentity(String userIdentity) {
		UserProfile userProfile = userProfileRepo.findByIdentity(userIdentity);
		return userProfile;
	}

	@Override
	public UserDetails getGuestUser(HttpServletRequest request) {

		UserDetails user = null;
		GuestUserProvider provider = guestUserProviderFactory.getProvider(request);

		if (provider != null) {
			user = provider.getGuestUser(request);
		}

		return user;
	}

	@Override
	public Boolean deleteUsers(List<String> userIdentities) {
		userIdentities.forEach((userIdentity) -> {
			deleteUser(userIdentity);
		});
		return Boolean.TRUE;
	}

	

}
