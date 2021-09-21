package com.spts.lms.beans.program;

import com.spts.lms.beans.BaseBean;

public class AdminProgram extends BaseBean{
	private static final long serialVersionUID = 1L;
	String username;
	String programId;
	String active;
	String programName;
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}

	@Override
	public String toString() {
		return "AdminProgram [username=" + username + ", programId="
				+ programId + ", active=" + active + ", programName="
				+ programName + "]";
	}
	
	
	
	
}
