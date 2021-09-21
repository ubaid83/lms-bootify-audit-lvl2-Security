package com.spts.lms.beans.programCampus;

import com.spts.lms.beans.BaseBean;



public class ProgramCampus extends BaseBean{
	
	private String programId;
	private String campusId;
	private String campusAbbr;
	private String campusName;
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampusAbbr() {
		return campusAbbr;
	}
	public void setCampusAbbr(String campusAbbr) {
		this.campusAbbr = campusAbbr;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	@Override
	public String toString() {
		return "ProgramCampus [programId=" + programId + ", campusId="
				+ campusId + ", campusAbbr=" + campusAbbr + ", campusName="
				+ campusName + "]";
	}
	
	
	

}
