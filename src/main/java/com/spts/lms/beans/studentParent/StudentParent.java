package com.spts.lms.beans.studentParent;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class StudentParent extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String stud_username;
	private String parent_username;
	private String parent_firstname;
	private String parent_lastname;
	private String parent_mobile;
	private String parent_email;
	private String parent_type;
	private String rollNo;

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	@Override
	public String toString() {
		return "StudentParent [stud_username=" + stud_username
				+ ", parent_username=" + parent_username
				+ ", parent_firstname=" + parent_firstname
				+ ", parent_lastname=" + parent_lastname + ", parent_mobile="
				+ parent_mobile + ", parent_email=" + parent_email
				+ ", parent_type=" + parent_type + ", rollNo=" + rollNo + "]";
	}

	public String getStud_username() {
		return stud_username;
	}

	public void setStud_username(String stud_username) {
		this.stud_username = stud_username;
	}

	public String getParent_username() {
		return parent_username;
	}

	public void setParent_username(String parent_username) {
		this.parent_username = parent_username;
	}

	public String getParent_firstname() {
		return parent_firstname;
	}

	public void setParent_firstname(String parent_firstname) {
		this.parent_firstname = parent_firstname;
	}

	public String getParent_lastname() {
		return parent_lastname;
	}

	public void setParent_lastname(String parent_lastname) {
		this.parent_lastname = parent_lastname;
	}

	public String getParent_mobile() {
		return parent_mobile;
	}

	public void setParent_mobile(String parent_mobile) {
		this.parent_mobile = parent_mobile;
	}

	public String getParent_email() {
		return parent_email;
	}

	public void setParent_email(String parent_email) {
		this.parent_email = parent_email;
	}

	public String getParent_type() {
		return parent_type;
	}

	public void setParent_type(String parent_type) {
		this.parent_type = parent_type;
	}

}
