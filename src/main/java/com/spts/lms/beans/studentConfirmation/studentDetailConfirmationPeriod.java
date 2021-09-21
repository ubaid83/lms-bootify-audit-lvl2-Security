package com.spts.lms.beans.studentConfirmation;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.validator.constraints.Range;
import org.joda.time.DateTime;

import com.spts.lms.beans.BaseBean;

public class studentDetailConfirmationPeriod extends BaseBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String endDate;
	private String active;
	private String programId;
	private String programName;
	private String sendEmailAlert;
	private String Description= " Kindly Fill the Master Data Validation ";
	private String acadSession;
	private String acadYear;
	private String campusId;
	private String campusName;

	

	
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getAcadYear() {
		return acadYear;
	}
	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}
	public String getAcadSession() {
		return acadSession;
	}
	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getSendEmailAlert() {
		return sendEmailAlert;
	}
	public void setSendEmailAlert(String sendEmailAlert) {
		this.sendEmailAlert = sendEmailAlert != null ? sendEmailAlert : "N";
		//this.sendEmailAlert = sendEmailAlert;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	@Override
	public String toString() {
		return "studentDetailConfirmationPeriod [endDate=" + endDate + ", active=" + active + ", programId=" + programId
				+ ", programName=" + programName + ", sendEmailAlert=" + sendEmailAlert + ", Description=" + Description
				+ ", acadSession=" + acadSession + ", acadYear=" + acadYear + ", campusId=" + campusId + ", campusName="
				+ campusName + "]";
	}
	
	
	
	
	
}
