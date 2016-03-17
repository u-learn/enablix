package com.enablix.core.security.service;

import java.util.List;

import com.enablix.core.domain.user.User;
import com.enablix.core.security.web.UserAndRolesVO;

public interface UserService {
	
	public Boolean checkUserByUserId(String userName);
	public User addUser(UserAndRolesVO user);
	public List<User> getAllUsers(String tenantId);
	public Boolean deleteUser(User user);
	public UserAndRolesVO getUserByIdentity(String userIdentity, String tenantId);
	public User resetPassword(String userid);
}
