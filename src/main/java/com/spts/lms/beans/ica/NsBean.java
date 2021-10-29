package com.spts.lms.beans.ica;

import java.util.Date;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class NsBean extends BaseBean{

	private Long id;
	private String acadYear;
	private String programId;
	private String moduleId;
	private String icaName;
	private String acadSession;
	private String campusId;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String active;
	private String programName;
	private String moduleDescription;
	private String campusName;
	private String isSubmitted;
	private String isPublished;
	private String showToStudents;
	private String grade;
	private List<String> programIds;
	
	
	
	public List<String> getProgramIds() {
		return programIds;
	}
	public void setProgramIds(List<String> programIds) {
		this.programIds = programIds;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getShowToStudents() {
		return showToStudents;
	}
	public void setShowToStudents(String showToStudents) {
		this.showToStudents = showToStudents != null ? showToStudents : "N";
	}
	public String getIsSubmitted() {
		return isSubmitted;
	}
	public void setIsSubmitted(String isSubmitted) {
		this.isSubmitted = isSubmitted;
	}
	public String getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}
	public String getModuleDescription() {
		return moduleDescription;
	}
	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAcadYear() {
		return acadYear;
	}
	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getIcaName() {
		return icaName;
	}
	public void setIcaName(String icaName) {
		this.icaName = icaName;
	}
	public String getAcadSession() {
		return acadSession;
	}
	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}
	
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
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
		return "NsBean [id=" + id + ", acadYear=" + acadYear + ", programId="
				+ programId + ", moduleId=" + moduleId + ", icaName=" + icaName
				+ ", acadSession=" + acadSession + ", campusId=" + campusId
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate="
				+ lastModifiedDate + ", active=" + active + ", programName="
				+ programName + ", moduleDescription=" + moduleDescription
				+ ", campusName=" + campusName + ", isSubmitted=" + isSubmitted
				+ ", isPublished=" + isPublished + ", showToStudents="
				+ showToStudents + ", grade=" + grade + ", programIds="
				+ programIds + "]";
	}
		
	
}
