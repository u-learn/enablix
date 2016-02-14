package com.enablix.core.security.service;

import java.util.List;

import com.enablix.core.domain.security.authorization.Role;
import com.enablix.core.domain.user.User;

public interface UserService {
	
	public Boolean checkUserbyUserName(String userName);
	public User addUser(User user);
	public List<User> getAllUsers();
	public Boolean deleteUser(User user);
	public List<Role> getRoleS();

}
