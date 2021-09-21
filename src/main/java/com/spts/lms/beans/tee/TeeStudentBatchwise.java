package com.spts.lms.beans.tee;

import com.spts.lms.beans.BaseBean;

public class TeeStudentBatchwise extends BaseBean{

	private String teeId;
	private String facultyId;
	private String username;
	private String active;
	public String getTeeId() {
		return teeId;
	}
	public void setTeeId(String teeId) {
		this.teeId = teeId;
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
		return "TeeStudentBatchWise [teeId=" + teeId + ", facultyId=" + facultyId + ", username=" + username
				+ ", active=" + active + "]";
	}
	
	
}
