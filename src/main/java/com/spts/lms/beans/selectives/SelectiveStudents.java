package com.spts.lms.beans.selectives;

import com.spts.lms.beans.BaseBean;

public class SelectiveStudents extends BaseBean{
	
	
	
	private String username;
	private String active;
	private String selective_id;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getSelective_id() {
		return selective_id;
	}
	public void setSelective_id(String selective_id) {
		this.selective_id = selective_id;
	}
	@Override
	public String toString() {
		return "SelectiveStudents [username=" + username + ", active=" + active + ", selective_id=" + selective_id
				+ "]";
	}
	
	

}
