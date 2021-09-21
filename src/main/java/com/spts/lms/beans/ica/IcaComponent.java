package com.spts.lms.beans.ica;

import com.spts.lms.beans.BaseBean;

public class IcaComponent extends BaseBean{
	
	
	private String componentName;
	private String componentDesc;
	private String moduleId;
	private String acadYear;
	private String programId;
	private String campusId;
	private String moduleName;
	private String moduleAbbr;
	private String acadSession;
	private String programName;
	private String campusName;
	private String icaId;
	private String componentId;
	
	private String marks;
	private String sequenceNo;
	
	
	//new field added on 26-07-21
	private String isSubmitted;
	private String isPublished;
	private String publishedDate;
	
	
	
	
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

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	private String active;
	
	

	public String getIcaId() {
		return icaId;
	}

	public void setIcaId(String icaId) {
		this.icaId = icaId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleAbbr() {
		return moduleAbbr;
	}

	public void setModuleAbbr(String moduleAbbr) {
		this.moduleAbbr = moduleAbbr;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentDesc() {
		return componentDesc;
	}

	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
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

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "IcaComponent [componentName=" + componentName + ", componentDesc=" + componentDesc + ", moduleId="
				+ moduleId + ", acadYear=" + acadYear + ", programId=" + programId + ", campusId=" + campusId
				+ ", moduleName=" + moduleName + ", moduleAbbr=" + moduleAbbr + ", acadSession=" + acadSession
				+ ", programName=" + programName + ", campusName=" + campusName + ", icaId=" + icaId + ", componentId="
				+ componentId + ", marks=" + marks + ", sequenceNo=" + sequenceNo + ", isSubmitted=" + isSubmitted
				+ ", isPublished=" + isPublished + ", publishedDate=" + publishedDate + ", active=" + active + "]";
	}
	
	
	
}
