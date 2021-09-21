package com.spts.lms.beans.course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;

public class UserCourse extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5541433915016984117L;

	private String acadMonth;

	private Integer acadYear;

	private Long courseId;

	private String username;

	private String acadSession;

	private String rollNo;

	private String email;

	private String mobile;

	private String programName;
	
	private String campusName;
	private Long campusId;
	
	private String isAbsent;
	
	private String isQueryRaise;
	
	private String studentName;
	private String isQueryApproved;
	
	private String eventId;
	
	//New Variables For Tee
	private String teeTotalMarks;
	private String remarks;
	private String internalMarks;
	private String externalMarks;
	private String query;
	private String teeScaledMarks;
	private String acadYearCode;
	

	public String getAcadYearCode() {
		return acadYearCode;
	}

	public void setAcadYearCode(String acadYearCode) {
		this.acadYearCode = acadYearCode;
	}

	public String getTeeTotalMarks() {
		return teeTotalMarks;
	}

	public void setTeeTotalMarks(String teeTotalMarks) {
		this.teeTotalMarks = teeTotalMarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInternalMarks() {
		return internalMarks;
	}

	public void setInternalMarks(String internalMarks) {
		this.internalMarks = internalMarks;
	}

	public String getExternalMarks() {
		return externalMarks;
	}

	public void setExternalMarks(String externalMarks) {
		this.externalMarks = externalMarks;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getTeeScaledMarks() {
		return teeScaledMarks;
	}

	public void setTeeScaledMarks(String teeScaledMarks) {
		this.teeScaledMarks = teeScaledMarks;
	}

	public String getIsQueryApproved() {
		return isQueryApproved;
	}

	public void setIsQueryApproved(String isQueryApproved) {
		this.isQueryApproved = isQueryApproved;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getIsAbsent() {
		return isAbsent;
	}

	public void setIsAbsent(String isAbsent) {
		this.isAbsent = isAbsent;
	}

	public String getIsQueryRaise() {
		return isQueryRaise;
	}

	public void setIsQueryRaise(String isQueryRaise) {
		this.isQueryRaise = isQueryRaise;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public Long getCampusId() {
		return campusId;
	}
	
	private Integer NoOfFacultyInCourse;

	public Integer getNoOfFacultyInCourse() {
		return NoOfFacultyInCourse;
	}

	public void setNoOfFacultyInCourse(Integer noOfFacultyInCourse) {
		NoOfFacultyInCourse = noOfFacultyInCourse;
	}

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	private Role role;
	private String operation;
	private Integer noOfStudentInCourse;
	private String facultyName;

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public Integer getNoOfStudentInCourse() {
		return noOfStudentInCourse;
	}

	public void setNoOfStudentInCourse(Integer noOfStudentInCourse) {
		this.noOfStudentInCourse = noOfStudentInCourse;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Non persistent
	 */
	private List<Long> courseIds = new ArrayList<Long>();
	@JsonIgnore
	private User user = new User();
	private String firstname;
	private String lastname;
	private String courseName;
	/*
	 * 
	 */
	private String eventName;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	private List<String> students;

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public UserCourse() {

	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		user.setUsername(username);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public void setRole(String role) {
		this.role = Role.valueOf(role);
	}

	public List<Long> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(List<Long> courseIds) {
		this.courseIds = courseIds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
		user.setFirstname(firstname);
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
		user.setLastname(lastname);
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
	public String toString() {
		return "UserCourse [acadMonth=" + acadMonth + ", acadYear=" + acadYear + ", courseId=" + courseId
				+ ", username=" + username + ", acadSession=" + acadSession + ", rollNo=" + rollNo + ", email=" + email
				+ ", mobile=" + mobile + ", programName=" + programName + ", campusName=" + campusName + ", campusId="
				+ campusId + ", isAbsent=" + isAbsent + ", isQueryRaise=" + isQueryRaise + ", studentName="
				+ studentName + ", isQueryApproved=" + isQueryApproved + ", eventId=" + eventId + ", teeTotalMarks="
				+ teeTotalMarks + ", remarks=" + remarks + ", internalMarks=" + internalMarks + ", externalMarks="
				+ externalMarks + ", query=" + query + ", teeScaledMarks=" + teeScaledMarks + ", acadYearCode="
				+ acadYearCode + ", NoOfFacultyInCourse=" + NoOfFacultyInCourse + ", role=" + role + ", operation="
				+ operation + ", noOfStudentInCourse=" + noOfStudentInCourse + ", facultyName=" + facultyName
				+ ", courseIds=" + courseIds + ", user=" + user + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", courseName=" + courseName + ", eventName=" + eventName + ", students=" + students + "]";
	}





}
