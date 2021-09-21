package com.spts.lms.beans.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;

public class UserLoginOutTime extends BaseBean {

	private String username;
	@JsonIgnore
    private Date logInTime;
    
    @JsonIgnore
	private Date logOutTime;
    
    
	private String logInTimeText;
	private String logOutTimeText;
	private String isLoggedIn;
	
	
	
	public String getIsLoggedIn() {
		return isLoggedIn;
	}
	public void setIsLoggedIn(String isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getLogInTime() {
		return logInTime;
	}
	public void setLogInTime(Date logInTime) {
		this.logInTime = logInTime;
	}
	public Date getLogOutTime() {
		return logOutTime;
	}
	public void setLogOutTime(Date logOutTime) {
		this.logOutTime = logOutTime;
	}
	public String getLogInTimeText() {
		return logInTimeText;
	}
	public void setLogInTimeText(String logInTimeText) {
		this.logInTimeText = logInTimeText;
	}
	public String getLogOutTimeText() {
		return logOutTimeText;
	}
	public void setLogOutTimeText(String logOutTimeText) {
		this.logOutTimeText = logOutTimeText;
	}
	@Override
	public String toString() {
		return "UserLoginOutTime [username=" + username + ", logInTime="
				+ logInTime + ", logOutTime=" + logOutTime + ", logInTimeText="
				+ logInTimeText + ", logOutTimeText=" + logOutTimeText + "]";
	}
	
	
}
