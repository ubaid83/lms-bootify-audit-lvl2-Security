package com.spts.lms.beans.ica;

import com.spts.lms.beans.BaseBean;

public class IcaComponentMarks extends BaseBean{
	
	
	private String username;
	private String icaId;
	
	private String componentId;
	
	private String marks;
	private String active;
	
	
	private String saveAsDraft;
	private String finalSubmit;
	private String sequenceNo;
	
	
	
	private String componentName;
	private String moduleName;
	private String acadYear;
	
	private String acadSession;
	
	private String moduleId;
	
	private String isQueryRaise;
	private String isQueryApproved;
	private String remarks;
	private String query;
	private String isAbsent;
	
	
	
	public String getSequenceNo() {
		return sequenceNo;
	}
	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	};
	public String getIsAbsent() {
		return isAbsent;
	}
	public void setIsAbsent(String isAbsent) {
		this.isAbsent = isAbsent;
	}
	
	public String getIsQueryRaise() {
		return isQueryRaise;
	}
	public void setIsQueryRaise(String isQueryRaise) {
		this.isQueryRaise = isQueryRaise;
	}
	public String getIsQueryApproved() {
		return isQueryApproved;
	}
	public void setIsQueryApproved(String isQueryApproved) {
		this.isQueryApproved = isQueryApproved;
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
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIcaId() {
		return icaId;
	}
	public void setIcaId(String icaId) {
		this.icaId = icaId;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
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
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "IcaComponentMarks [username=" + username + ", icaId=" + icaId + ", componentId=" + componentId
				+ ", marks=" + marks + ", active=" + active + ", saveAsDraft=" + saveAsDraft + ", finalSubmit="
				+ finalSubmit + ", sequenceNo=" + sequenceNo + ", componentName=" + componentName + ", moduleName="
				+ moduleName + ", acadYear=" + acadYear + ", acadSession=" + acadSession + ", moduleId=" + moduleId
				+ ", isQueryRaise=" + isQueryRaise + ", isQueryApproved=" + isQueryApproved + ", remarks=" + remarks
				+ ", query=" + query + ", isAbsent=" + isAbsent + ", getSequenceNo()=" + getSequenceNo()
				+ ", getIsAbsent()=" + getIsAbsent() + ", getIsQueryRaise()=" + getIsQueryRaise()
				+ ", getIsQueryApproved()=" + getIsQueryApproved() + ", getRemarks()=" + getRemarks() + ", getQuery()="
				+ getQuery() + ", getAcadSession()=" + getAcadSession() + ", getModuleId()=" + getModuleId()
				+ ", getComponentName()=" + getComponentName() + ", getModuleName()=" + getModuleName()
				+ ", getAcadYear()=" + getAcadYear() + ", getUsername()=" + getUsername() + ", getIcaId()=" + getIcaId()
				+ ", getComponentId()=" + getComponentId() + ", getMarks()=" + getMarks() + ", getSaveAsDraft()="
				+ getSaveAsDraft() + ", getFinalSubmit()=" + getFinalSubmit() + ", getActive()=" + getActive()
				+ ", getId()=" + getId() + ", getCreatedDate()=" + getCreatedDate() + ", getLastModifiedDate()="
				+ getLastModifiedDate() + ", getCreatedBy()=" + getCreatedBy() + ", getLastModifiedBy()="
				+ getLastModifiedBy() + ", isErrorRecord()=" + isErrorRecord() + ", getErrorMessage()="
				+ getErrorMessage() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
	 

}
