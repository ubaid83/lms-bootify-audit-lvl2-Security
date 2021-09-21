package com.spts.lms.beans.ica;

import java.util.Date;

import com.spts.lms.beans.BaseBean;

public class StudentNcIca extends BaseBean {

	private Long id;
	private String icaId;
	private String username;
	private String grade;
	private String active;
	private String saveAsDraft;
	private String finalSubmit;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String rollno;
	private String studentName;
	
	
	

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getRollno() {
		return rollno;
	}

	public void setRollno(String rollno) {
		this.rollno = rollno;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getIcaId() {
		return icaId;
	}

	public void setIcaId(String icaId) {
		this.icaId = icaId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	

	public String getSaveAsDraft() {
		return saveAsDraft;
	}

	public void setSaveAsDraft(String saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

	public String getFinalSubmit() {
		return finalSubmit;
	}

	public void setFinalSubmit(String finalSubmit) {
		this.finalSubmit = finalSubmit;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public String toString() {
		return "StudentNcIca [id=" + id + ", icaId=" + icaId + ", username="
				+ username + ", grade=" + grade + ", active=" + active
				+ ", saveAsDraft=" + saveAsDraft + ", finalSubmit="
				+ finalSubmit + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModifiedDate=" + lastModifiedDate + ", rollno="
				+ rollno + ", studentName=" + studentName + "]";
	}

}
