package com.enablix.core.security.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping(method = RequestMethod.GET,	value="/checkusername/{userName}", 
			produces = "application/json")
	public Boolean checkUserbyUserName(@PathVariable String userName) {
		return userService.checkUserbyUserName(userName);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/adduser", 
			produces = "application/json")
	public User addUser(@RequestBody User user) {
		return userService.addUser(user);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/updateuser", 
			produces = "application/json")
	public User updateUser(@RequestBody User user) {
		return userService.updateUser(user);
	}

}
