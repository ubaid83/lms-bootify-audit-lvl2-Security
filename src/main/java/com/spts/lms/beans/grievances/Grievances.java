package com.spts.lms.beans.grievances;

import java.util.Date;

import com.spts.lms.beans.BaseBean;

@SuppressWarnings("serial")
public class Grievances extends BaseBean {

	public String username;
	public String acadMonth;
	public String acadYear;
	public String typeOfGrievance;
	public String description;
	public String subject;
	public String grievanceCase;
	public String grievanceResponse;
	public String grievanceStatus;
	public String grievanceReason;
	public String studentName;

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getGrievanceReason() {
		return grievanceReason;
	}

	public void setGrievanceReason(String grievanceReason) {
		this.grievanceReason = grievanceReason;
	}

	public String getGrievanceStatus() {
		return grievanceStatus;
	}

	public void setGrievanceStatus(String grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}

	public String getGrievanceResponse() {
		return grievanceResponse;
	}

	public void setGrievanceResponse(String grievanceResponse) {
		this.grievanceResponse = grievanceResponse;
	}

	public Date getGrievanceResponseTimeStamp() {
		return grievanceResponseTimeStamp;
	}

	public void setGrievanceResponseTimeStamp(Date grievanceResponseTimeStamp) {
		this.grievanceResponseTimeStamp = grievanceResponseTimeStamp;
	}

	public Date grievanceResponseTimeStamp;

	public String getGrievanceCase() {
		return grievanceCase;
	}

	public void setGrievanceCase(String grievanceCase) {
		this.grievanceCase = grievanceCase;
	}

	public String getTypeOfGrievance() {
		return typeOfGrievance;
	}

	public void setTypeOfGrievance(String typeOfGrievance) {
		this.typeOfGrievance = typeOfGrievance;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public String getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}

	@Override
	public String toString() {
		return "Grievances [username=" + username + ", acadMonth=" + acadMonth
				+ ", acadYear=" + acadYear + ", typeOfGrievance="
				+ typeOfGrievance + ", description=" + description
				+ ", subject=" + subject + ", grievanceCase=" + grievanceCase
				+ ", grievanceResponse=" + grievanceResponse
				+ ", grievanceResponseTimeStamp=" + grievanceResponseTimeStamp
				+ "]";
	}

}