package com.spts.lms.beans.content;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class StudentContent extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String acadMonth;

	private Integer acadYear;

	private Long courseId;

	private Long contentId;

	private int count;

	private String rollNo;
	
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Non persistent fields
	 */
	private User user = new User();

	private Program program = new Program();

	/**
	 * 
	 */

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		user.setUsername(username);
		this.username = username;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public String getFirstname() {
		return user.getFirstname();
	}

	public void setFirstname(String firstname) {
		user.setFirstname(firstname);
	}

	public String getLastname() {
		return user.getLastname();
	}

	public void setLastname(String lastname) {
		user.setLastname(lastname);
	}

	public String getProgramName() {
		return program.getProgramName();
	}

	public void setProgramName(String programName) {
		program.setProgramName(programName);
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	@Override
	public String toString() {
		return "StudentContent [username=" + username + ", acadMonth="
				+ acadMonth + ", acadYear=" + acadYear + ", courseId="
				+ courseId + ", contentId=" + contentId + ", count=" + count
				+ ", rollNo=" + rollNo + ", campusName=" + campusName
				+ ", campusId=" + campusId + ", user=" + user + ", program="
				+ program + "]";
	}


}
