package com.enablix.core.security.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.user.User;
import com.enablix.core.security.service.UserService;

@RestController
public class SecurityController {

	@Autowired
	UserService userService;
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
	@RequestMapping(method = RequestMethod.GET,	value="/checkusername", produces = "application/json")
	public Boolean checkUserbyUserName(@RequestParam String userName) {
		return userService.checkUserbyUserName(userName);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/systemuser", produces = "application/json")
	public User addUser(@RequestBody User user) {
		return userService.addUser(user);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/deletesystemuser", produces = "application/json")
	public Boolean deleteUser(@RequestBody User user) {
		 return userService.deleteUser(user);
	}
	
		
	@RequestMapping(method = RequestMethod.GET, value="/systemuser", produces = "application/json")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/getroles", produces = "application/json")
	public List<Role> getAllRoles() {
		return userService.getRoleS();
	};

}
