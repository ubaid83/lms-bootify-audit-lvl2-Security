package com.spts.lms.beans.charts;


import java.io.Serializable;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.Role;

public class Charts extends BaseBean implements Serializable {

	private Long courseId;
	
	private Course course;
	
	List<Course> allCourse;

	public List<Course> getAllCourse() {
		return allCourse;
	}

	public void setAllCourse(List<Course> allCourse) {
		this.allCourse = allCourse;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	private String assignmentChart;

	private String testChart;

	private Integer acadYear;

	private String acadSession;

	private String searchType;

	private String username;

	private String firstname;

	private String lastname;

	private Role role;

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
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

	public String getAssignmentChart() {
		return assignmentChart;
	}

	public void setAssignmentChart(String assignmentChart) {
		this.assignmentChart = assignmentChart;
	}

	public String getTestChart() {
		return testChart;
	}

	public void setTestChart(String testChart) {
		this.testChart = testChart;
	}

	@Override
	public String toString() {
		return "Charts [courseId=" + courseId + ", course=" + course
				+ ", allCourse=" + allCourse + ", assignmentChart="
				+ assignmentChart + ", testChart=" + testChart + ", acadYear="
				+ acadYear + ", acadSession=" + acadSession + ", searchType="
				+ searchType + ", username=" + username + ", firstname="
				+ firstname + ", lastname=" + lastname + ", role=" + role + "]";
	}

	

	

}
