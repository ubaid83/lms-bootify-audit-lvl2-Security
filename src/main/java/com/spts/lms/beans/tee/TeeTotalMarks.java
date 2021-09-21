package com.spts.lms.beans.tee;

import com.spts.lms.beans.BaseBean;

public class TeeTotalMarks extends BaseBean{
	
	private Long teeId;
	private String username;
	private String teeTotalMarks;
	private String active;
	private String teeScaledMarks;
	private String saveAsDraft;
	private String finalSubmit;
	private String isQueryRaise;
	private String isAbsent;
	private String remarks;
	private String query;
	private String flagTcs;
	private String isQueryApproved;
	
	private String passFailStatus;
	private String externalMarks;
	private String publishedDate;
	private String moduleName;
	private String acadYear;
	private String acadSession;
	private String moduleId;
	private String checkGrade;
	private String studentName;
	private String rollNo;
	private String dueDate;
	private String teeIdStr;
	private String moduleAbbr;
	private String teeName;
	private String programName;
	private String assignedFaculty;
	private String email;
	private String raiseQDate;
	
	
	public String getTeeName() {
		return teeName;
	}
	public void setTeeName(String teeName) {
		this.teeName = teeName;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getAssignedFaculty() {
		return assignedFaculty;
	}
	public void setAssignedFaculty(String assignedFaculty) {
		this.assignedFaculty = assignedFaculty;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRaiseQDate() {
		return raiseQDate;
	}
	public void setRaiseQDate(String raiseQDate) {
		this.raiseQDate = raiseQDate;
	}
	public String getModuleAbbr() {
		return moduleAbbr;
	}
	public void setModuleAbbr(String moduleAbbr) {
		this.moduleAbbr = moduleAbbr;
	}
	public String getTeeIdStr() {
		return teeIdStr;
	}
	public void setTeeIdStr(String teeIdStr) {
		this.teeIdStr = teeIdStr;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getCheckGrade() {
		return checkGrade;
	}
	public void setCheckGrade(String checkGrade) {
		this.checkGrade = checkGrade;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Long getTeeId() {
		return teeId;
	}
	public void setTeeId(Long teeId) {
		this.teeId = teeId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTeeTotalMarks() {
		return teeTotalMarks;
	}
	public void setTeeTotalMarks(String teeTotalMarks) {
		this.teeTotalMarks = teeTotalMarks;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getTeeScaledMarks() {
		return teeScaledMarks;
	}
	public void setTeeScaledMarks(String teeScaledMarks) {
		this.teeScaledMarks = teeScaledMarks;
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
	public String getIsQueryRaise() {
		return isQueryRaise;
	}
	public void setIsQueryRaise(String isQueryRaise) {
		this.isQueryRaise = isQueryRaise;
	}
	public String getIsAbsent() {
		return isAbsent;
	}
	public void setIsAbsent(String isAbsent) {
		this.isAbsent = isAbsent;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getFlagTcs() {
		return flagTcs;
	}
	public void setFlagTcs(String flagTcs) {
		this.flagTcs = flagTcs;
	}
	public String getIsQueryApproved() {
		return isQueryApproved;
	}
	public void setIsQueryApproved(String isQueryApproved) {
		this.isQueryApproved = isQueryApproved;
	}
	public String getPassFailStatus() {
		return passFailStatus;
	}
	public void setPassFailStatus(String passFailStatus) {
		this.passFailStatus = passFailStatus;
	}
	public String getExternalMarks() {
		return externalMarks;
	}
	public void setExternalMarks(String externalMarks) {
		this.externalMarks = externalMarks;
	}
	public String getPublishedDate() {
		return publishedDate;
	}
	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	@Override
	public String toString() {
		return "TeeTotalMarks [teeId=" + teeId + ", username=" + username + ", teeTotalMarks=" + teeTotalMarks
				+ ", active=" + active + ", teeScaledMarks=" + teeScaledMarks + ", saveAsDraft=" + saveAsDraft
				+ ", finalSubmit=" + finalSubmit + ", isQueryRaise=" + isQueryRaise + ", isAbsent=" + isAbsent
				+ ", remarks=" + remarks + ", query=" + query + ", flagTcs=" + flagTcs + ", isQueryApproved="
				+ isQueryApproved + ", passFailStatus=" + passFailStatus + ", externalMarks=" + externalMarks
				+ ", publishedDate=" + publishedDate + ", moduleName=" + moduleName + ", acadYear=" + acadYear
				+ ", acadSession=" + acadSession + ", moduleId=" + moduleId + ", checkGrade=" + checkGrade
				+ ", studentName=" + studentName + ", rollNo=" + rollNo + ", dueDate=" + dueDate + ", teeIdStr="
				+ teeIdStr + ", moduleAbbr=" + moduleAbbr + ", teeName=" + teeName + ", programName=" + programName
				+ ", assignedFaculty=" + assignedFaculty + ", email=" + email + ", raiseQDate=" + raiseQDate + "]";
	}
	
	

}
