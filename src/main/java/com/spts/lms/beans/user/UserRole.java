package com.spts.lms.beans.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;

/**
 * The persistent class for the user_roles database table.
 * 
 */
public class UserRole extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Role role;

	private String username;
	private String operation;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	// bi-directional many-to-one association to User
	private User user;

	public UserRole() {
	}

	public String getUsername() {
		if (null != user)
			return user.getUsername();
		return username;
	}

	public void setUsername(String username) {
		if (null == user)
			user = new User();
		user.setUsername(username);
		this.username = username;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setRole(String role) {
		this.role = Role.valueOf(role);
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String getCreatedBy() {
		return user.getCreatedBy();
	}

	@Override
	public String getLastModifiedBy() {
		return user.getLastModifiedBy();
	}

	@Override
	public String toString() {
		return "UserRole [role=" + role + ", username=" + getUsername() + "]";
	}

}