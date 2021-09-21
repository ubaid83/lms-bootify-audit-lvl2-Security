package com.spts.lms.beans;

public class MenuLoadParams {
	
	private String abbr;
	private String username;
	
	public String getAbbr() {
		return abbr;
	}
	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String toString() {
		return "MenuLoadParams [abbr=" + abbr + ", username=" + username + "]";
	}
	
	
	
	

}
