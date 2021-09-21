package com.spts.lms.beans.course;

import com.spts.lms.beans.BaseBean;

public class MyCourseStudentBean extends BaseBean {

	private String sapId;
	private String acadSession;
	private String fullName;
	private String courseName;
	private Integer acadYear;
	private String acadMonth;
	private String rollNo;
	private String mobile;
	private String email;

	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
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

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "MyCourseStudentBean [sapId=" + sapId + ", acadSession="
				+ acadSession + ", fullName=" + fullName + ", courseName="
				+ courseName + ", acadYear=" + acadYear + ", acadMonth="
				+ acadMonth + ", rollNo=" + rollNo + ", mobile=" + mobile
				+ ", email=" + email + "]";
	}

}
