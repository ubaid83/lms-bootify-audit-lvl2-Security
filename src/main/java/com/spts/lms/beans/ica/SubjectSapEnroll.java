package com.spts.lms.beans.ica;

import java.util.Date;

import com.spts.lms.beans.BaseBean;

public class SubjectSapEnroll extends BaseBean {

	private Long id;
	private String studentNumber;
	private String subjectCode;
	private String creditPoint;
	private String enrollDeEnroll;
	private String enrollType;
	private String flag;
	private String acadYear;
	private String acadSession;
	private String operation;
	private String companyCode;
	private String acadYearCode;
	private String acadSessionCode;
	private String programId;
	private String campusId;
	private String schoolObj;
	private String flagTcs;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	private String rollno;
	private String studentName;
	

	public String getRollno() {
		return rollno;
	}

	public void setRollno(String rollno) {
		this.rollno = rollno;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getCreditPoint() {
		return creditPoint;
	}

	public void setCreditPoint(String creditPoint) {
		this.creditPoint = creditPoint;
	}

	public String getEnrollDeEnroll() {
		return enrollDeEnroll;
	}

	public void setEnrollDeEnroll(String enrollDeEnroll) {
		this.enrollDeEnroll = enrollDeEnroll;
	}

	public String getEnrollType() {
		return enrollType;
	}

	public void setEnrollType(String enrollType) {
		this.enrollType = enrollType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getAcadYearCode() {
		return acadYearCode;
	}

	public void setAcadYearCode(String acadYearCode) {
		this.acadYearCode = acadYearCode;
	}

	public String getAcadSessionCode() {
		return acadSessionCode;
	}

	public void setAcadSessionCode(String acadSessionCode) {
		this.acadSessionCode = acadSessionCode;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getSchoolObj() {
		return schoolObj;
	}

	public void setSchoolObj(String schoolObj) {
		this.schoolObj = schoolObj;
	}

	public String getFlagTcs() {
		return flagTcs;
	}

	public void setFlagTcs(String flagTcs) {
		this.flagTcs = flagTcs;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public String toString() {
		return "SubjectSapEnroll [id=" + id + ", studentNumber="
				+ studentNumber + ", subjectCode=" + subjectCode
				+ ", creditPoint=" + creditPoint + ", enrollDeEnroll="
				+ enrollDeEnroll + ", enrollType=" + enrollType + ", flag="
				+ flag + ", acadYear=" + acadYear + ", acadSession="
				+ acadSession + ", operation=" + operation + ", companyCode="
				+ companyCode + ", acadYearCode=" + acadYearCode
				+ ", acadSessionCode=" + acadSessionCode + ", programId="
				+ programId + ", campusId=" + campusId + ", schoolObj="
				+ schoolObj + ", flagTcs=" + flagTcs + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy
				+ ", lastModifiedDate=" + lastModifiedDate
				+ ", lastModifiedBy=" + lastModifiedBy + ", rollno=" + rollno
				+ ", studentName=" + studentName + "]";
	}

	

}
