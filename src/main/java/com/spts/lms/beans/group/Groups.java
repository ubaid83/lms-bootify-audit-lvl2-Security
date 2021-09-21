package com.spts.lms.beans.group;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

public class Groups extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long myId;
	
	private String rollNo;
	private String courseIDS;
	
	public String getCourseIDS() {
		return courseIDS;
	}

	public void setCourseIDS(String courseIDS) {
		this.courseIDS = courseIDS;
	}
	private String campusName;
	private Long campusId;

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
	public Long getMyId() {
		return myId;
	}

	public void setMyId(Long myId) {
		this.myId = myId;
	}
	private String courseName;
  
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	private String groupName;

	private Long courseId;
	private String facultyId;

	private Integer acadYear;

	private String acadMonth;

	private String active = "Y";

	private String sendEmailAlert;

	private String sendSmsAlert;

	private String noOfStudents;

	private String group_details;

	private String idForCourse;

	public String getIdForCourse() {
		return idForCourse;
	}

	public void setIdForCourse(String idForCourse) {
		this.idForCourse = idForCourse;
	}

	


	@Override
	public String toString() {
		return "Groups [myId=" + myId + ", rollNo=" + rollNo + ", courseIDS="
				+ courseIDS + ", campusName=" + campusName + ", campusId="
				+ campusId + ", courseName=" + courseName + ", groupName="
				+ groupName + ", courseId=" + courseId + ", facultyId="
				+ facultyId + ", acadYear=" + acadYear + ", acadMonth="
				+ acadMonth + ", active=" + active + ", sendEmailAlert="
				+ sendEmailAlert + ", sendSmsAlert=" + sendSmsAlert
				+ ", noOfStudents=" + noOfStudents + ", group_details="
				+ group_details + ", idForCourse=" + idForCourse
				+ ", students=" + students + ", course=" + course + "]";
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getSendEmailAlert() {
		return sendEmailAlert;
	}

	public void setSendEmailAlert(String sendEmailAlert) {
		this.sendEmailAlert = sendEmailAlert;
	}

	public String getSendSmsAlert() {
		return sendSmsAlert;
	}

	public void setSendSmsAlert(String sendSmsAlert) {
		this.sendSmsAlert = sendSmsAlert;
	}

	public String getNoOfStudents() {
		return noOfStudents;
	}

	public void setNoOfStudents(String noOfStudents) {
		this.noOfStudents = noOfStudents;
	}

	public String getGroup_details() {
		return group_details;
	}

	public void setGroup_details(String group_details) {
		this.group_details = group_details;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private List<String> students = new ArrayList<String>();
	private Course course = new Course();

	public String size() {
		return null;
	}

	public String count() {
		return null;
	}

}
