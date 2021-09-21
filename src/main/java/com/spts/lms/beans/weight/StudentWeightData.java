package com.spts.lms.beans.weight;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class StudentWeightData extends BaseBean implements Serializable {

	private String username;
	private String firstname;
	private String lastname;
	private String rollNo;
	private String courseId;

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	private String icaTotal;
	private String icaScored;
	private String tee;
	private String total;

	private boolean icaPassed;
	private String icaPass;
	private String gradingType;

	public String getGradingType() {
		return gradingType;
	}

	public void setGradingType(String gradingType) {
		this.gradingType = gradingType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIcaTotal() {
		return icaTotal;
	}

	public void setIcaTotal(String icaTotal) {
		this.icaTotal = icaTotal;
	}

	public String getIcaScored() {
		return icaScored;
	}

	public void setIcaScored(String icaScored) {
		this.icaScored = icaScored;
	}

	public String getTee() {
		return tee;
	}

	public void setTee(String tee) {
		this.tee = tee;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	

	public String getIcaPass() {
		return icaPass;
	}

	public void setIcaPass(String icaPass) {
		this.icaPass = icaPass;
	}

	public boolean isIcaPassed() {
		return icaPassed;
	}

	public void setIcaPassed(boolean icaPassed) {
		this.icaPassed = icaPassed;
	}

	@Override
	public String toString() {
		return "StudentWeightData [username=" + username + ", firstname="
				+ firstname + ", lastname=" + lastname + ", rollNo=" + rollNo
				+ ", courseId=" + courseId + ", icaTotal=" + icaTotal
				+ ", icaScored=" + icaScored + ", tee=" + tee + ", total="
				+ total + ", icaPassed=" + icaPassed + ", icaPass=" + icaPass
				+ ", gradingType=" + gradingType + "]";
	}

	
	

}
