package com.spts.lms.beans.attendance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class Attendance extends BaseBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String courseName;
	private String courseId;
	private String facultyId;
	private String startDate;
	private String endDate;
	private List<String> students = new ArrayList<String>();

	public List<String> getStudents() {
		return students;
	}
	
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getFacultyId() {
		return facultyId;
	}
	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}
	
	public String getStartDate() {
		return formatDate(startDate);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return formatDate(endDate);
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "Attendance [username=" + username + ", courseName="
				+ courseName + ", courseId=" + courseId + ", facultyId="
				+ facultyId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", students=" + students + "]";
	}


}
