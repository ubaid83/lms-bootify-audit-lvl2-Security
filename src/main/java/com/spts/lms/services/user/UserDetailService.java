package com.spts.lms.services.user;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.user.User;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.daos.user.UserRoleDAO;

@Component("userDetailsService")
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UserRoleDAO userRoleDAO;

	public UserRoleDAO getUserRoleDAO() {
		return userRoleDAO;
	}

	@Autowired
	private UserDAO userDAO;

	public void setUserRoleDAO(UserRoleDAO userRoleDAO) {
		this.userRoleDAO = userRoleDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
		User user = userDAO.findCredsByUserName(username);
		user.setRoles(userRoleDAO.findRolesByUsername(username));
		return user;
	}

}
