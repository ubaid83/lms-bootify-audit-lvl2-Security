package com.spts.lms.beans.ica;

import com.spts.lms.beans.BaseBean;

public class IcaStudentBatchwise extends BaseBean{
	
	private String icaId;
	private String facultyId;
	private String username;
	private String active;
	
	public String getIcaId() {
		return icaId;
	}
	public void setIcaId(String icaId) {
		this.icaId = icaId;
	}
	public String getFacultyId() {
		return facultyId;
	}
	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}
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
	@Override
	public String toString() {
		return "IcaStudentBatchwise [icaId=" + icaId + ", facultyId="
				+ facultyId + ", username=" + username + ", active=" + active
				+ "]";
	}

	
}
