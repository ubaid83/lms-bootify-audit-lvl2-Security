package com.spts.lms.beans.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentDetails {
	
	private String username;
	private String programId;
	
	private String context;
	private String rollNo;
	private String fullName;
	private String programName;
	private String acadSession;
	
	
	
	private Map<String,Map<String,String>> mapOfSAPEventToLMSCourse= new HashMap<>();
	
	private Map<String,String> mapOfSAPEventToEventName = new HashMap<>();

	
	
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
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

	public Map<String, Map<String, String>> getMapOfSAPEventToLMSCourse() {
		return mapOfSAPEventToLMSCourse;
	}

	public void setMapOfSAPEventToLMSCourse(
			Map<String, Map<String, String>> mapOfSAPEventToLMSCourse) {
		this.mapOfSAPEventToLMSCourse = mapOfSAPEventToLMSCourse;
	}

	public Map<String, String> getMapOfSAPEventToEventName() {
		return mapOfSAPEventToEventName;
	}

	public void setMapOfSAPEventToEventName(
			Map<String, String> mapOfSAPEventToEventName) {
		this.mapOfSAPEventToEventName = mapOfSAPEventToEventName;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "StudentDetails [username=" + username + ", programId="
				+ programId + ", context=" + context
				+ ", mapOfSAPEventToLMSCourse=" + mapOfSAPEventToLMSCourse
				+ ", mapOfSAPEventToEventName=" + mapOfSAPEventToEventName
				+ "]";
	}
	
	
	

}
