package com.spts.lms.beans.weight;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class Component extends BaseBean implements Serializable {

	private String username;
	private String score;
	private String remarks;
	private String facultyId;
	private String courseId;
	private String acadMonth;
	private String firstname;
	private String lastname;
	private String courseName;
	private String rollNo;
	private String compName;
	private String scores;
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getScores() {
		return scores;
	}

	public void setScores(String scores) {
		this.scores = scores;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
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

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	@Override
	public String toString() {
		return "Component [username=" + username + ", score=" + score
				+ ", remarks=" + remarks + ", facultyId=" + facultyId
				+ ", courseId=" + courseId + ", acadMonth=" + acadMonth
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", courseName=" + courseName + ", rollNo=" + rollNo
				+ ", compName=" + compName + ", scores=" + scores + ", active="
				+ active + "]";
	}

}
