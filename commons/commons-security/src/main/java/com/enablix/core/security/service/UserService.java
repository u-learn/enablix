package com.enablix.core.security.service;

import com.enablix.core.domain.user.User;

public interface UserService {
	
	public Boolean checkUserbyUserName(String userName);
	public User addUser(User user);
	public User updateUser(User user);

}
