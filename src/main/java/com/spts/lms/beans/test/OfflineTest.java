package com.spts.lms.beans.test;

import com.spts.lms.beans.BaseBean;

public class OfflineTest extends BaseBean{

	private static final long serialVersionUID = 1067122965896106688L;
	
	private String testName;
	
	private String courseId;
	
	private String courseName;
	private String acadMonth;
	private String acadYear;
	private String acadSession;
	
	

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
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

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "OfflineTest [testName=" + testName + ", courseId=" + courseId
				+ "]";
	}
	
	
}
