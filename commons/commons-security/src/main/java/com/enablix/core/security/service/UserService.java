package com.enablix.core.security.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;

import com.enablix.core.domain.security.authorization.UserProfile;
import com.enablix.core.domain.user.User;

public interface UserService {
	
	public Boolean checkUserByUserId(String userName);
	public User addUser(String userDataJSON);
	public User editUser(String userDataJSON);
	public List<UserProfile> getAllUsers(String tenantId);
	public Boolean deleteUser(String identity);
	public UserProfile getUserByIdentity(String userIdentity);
	public void resetPassword(String userid);
	public User changePassword(User user);
	public UserDetails getGuestUser(HttpServletRequest request);
	
}
