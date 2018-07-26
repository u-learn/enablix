package com.enablix.core.security.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.commons.util.process.ProcessContext;
import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.user.User;
import com.enablix.core.mail.service.MailService;
import com.enablix.core.security.SecurityUtil;
import com.enablix.core.security.auth.repo.RoleRepository;
import com.enablix.core.security.service.EnablixUserService.LoggedInUser;
import com.enablix.core.security.service.UserService;

@RestController
public class SecurityController {

	@Autowired
	private UserService userService;

	@Autowired
	MailService mailService;

	@Autowired
	private RoleRepository roleRepo;

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	@RequestMapping(method = RequestMethod.GET,	value="/checkusername", 
			produces = "application/json")
	public Boolean checkUserbyUserName(@RequestParam String userName) {
		return userService.checkUserByUserId(userName);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/checkuserexist", 
			produces = "application/json")
	public String checkUserExist(@RequestBody CheckUsernameRequest request) {
		boolean exist = userService.checkUserByUserId(request.getUsername());
		return exist ? "{ \"exist\": true }" : "{ \"exist\": false }";
	}

	@RequestMapping(method = RequestMethod.POST, value="/systemuser", 
			produces = "application/json")
	public User addUser(@RequestBody String userJSON) {
		return userService.addUser(userJSON);
	}

	@RequestMapping(method = RequestMethod.POST, value="/systemuseredit", 
			produces = "application/json")
	public User editUser(@RequestBody String userJSON) {
		return userService.editUser(userJSON);
	}

	@RequestMapping(method = RequestMethod.POST, value="/deletesystemuser", 
			produces = "application/json")
	public Boolean deleteUser(@RequestBody String identity) {
		return userService.deleteUser(identity);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/deletesystemusers", 
			produces = "application/json")
	public Boolean deleteUsers(@RequestBody UserProfileIdentities identities) {
		return userService.deleteUsers(identities.getIdentities());
	}


	@RequestMapping(method = RequestMethod.GET, value="/systemuser", 
			produces = "application/json")
	public List<UserProfile> getAllUsers() {
		return userService.getAllUsers(ProcessContext.get().getTenantId());
	}

	@RequestMapping(method = RequestMethod.GET, value="/d/user/{userIdentity}/", 
			produces = "application/json")
	public UserProfile getUser(@PathVariable String userIdentity) {
		return userService.getUserByIdentity(userIdentity);
	}

	@RequestMapping(method = RequestMethod.GET, value="/roles", 
			produces = "application/json")
	public List<Role> getAllRoles() {
		return roleRepo.findAll();
	}

	@RequestMapping(method = RequestMethod.POST,	value="/resetpassword", 
			produces = "application/json")
	public Boolean resetPassword(@RequestBody String userid ) {
		if(userService.checkUserByUserId(userid)){
			userService.resetPassword(userid);
			return true;
		}
		else
			return false;
	}

	@RequestMapping(method = RequestMethod.POST,	value="/systemuserchangepwd", 
			produces = "application/json")
	public User changePassword(@RequestBody User usr ) {
		return userService.changePassword(usr);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/member/add", 
			produces = "application/json")
	public UserProfile addMember(@RequestBody UserProfile up) {
		return userService.addOrUpdateUser(up);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/member/update", 
			produces = "application/json")
	public UserProfile updateMember(@RequestBody UserProfile up) {
		
		UserProfile updateUser = userService.addOrUpdateUser(up);
		
		// update profile in session
		UserDetails userDetails = SecurityUtil.currentUser();
		if (userDetails instanceof LoggedInUser) {
			LoggedInUser user = (LoggedInUser) userDetails;
			userService.checkAndUpdateUserProfile(user, updateUser);
		}
		
		return updateUser;
	}

	/*	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "SUCCESS";
	    //You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}
	 */
	
	private static class CheckUsernameRequest {
		private String username;

		public String getUsername() {
			return username;
		}

		@SuppressWarnings("unused")
		public void setUsername(String username) {
			this.username = username;
		}
		
	}
	
	private static class UserProfileIdentities {
		private List<String> identities;

		public List<String> getIdentities() {
			return identities;
		}

		@SuppressWarnings("unused")
		public void setIdentities(List<String> identities) {
			this.identities = identities;
		}
		
	}
}
