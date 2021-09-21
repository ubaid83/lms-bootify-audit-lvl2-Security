package com.spts.lms.beans.user;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class WsdlLog extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String studentObjectid;
	private String username;
	private String mobile;
	private String email;
	private String failedReason;
	
	public String getStudentObjectid() {
		return studentObjectid;
	}
	public void setStudentObjectid(String studentObjectid) {
		this.studentObjectid = studentObjectid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFailedReason() {
		return failedReason;
	}
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
	}
	@Override
	public String toString() {
		return "WsdlLog [studentObjectid=" + studentObjectid + ", username="
				+ username + ", mobile=" + mobile + ", email=" + email
				+ ", failedReason=" + failedReason + "]";
	}
	
	
	
	
	
	
}
