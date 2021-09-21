package com.spts.lms.beans.classParticipation;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import com.spts.lms.beans.BaseBean;

public class ClassParticipation extends BaseBean implements Serializable {

	private String username;
	private Integer score;
	private String remarks;
	private String facultyId;
	private String courseId;
	private String acadMonth;
	private String firstname;
	private String lastname;
	private String courseName;
	private String rollNo;
	private String campusName;
	private Long campusId;
	private String nameToShow;
private MultipartFile file;
	

	

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	public String getNameToShow() {
		return nameToShow;
	}

	public void setNameToShow(String nameToShow) {
		this.nameToShow = nameToShow;
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

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
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

	private Integer acadYear;

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

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}



	@Override
	public String toString() {
		return "ClassParticipation [username=" + username + ", score=" + score
				+ ", remarks=" + remarks + ", facultyId=" + facultyId
				+ ", courseId=" + courseId + ", acadMonth=" + acadMonth
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", courseName=" + courseName + ", rollNo=" + rollNo
				+ ", campusName=" + campusName + ", campusId=" + campusId
				+ ", nameToShow=" + nameToShow + ", acadYear=" + acadYear + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
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

}
