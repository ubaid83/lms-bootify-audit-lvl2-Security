package com.spts.lms.beans.ica;

import com.spts.lms.beans.BaseBean;

public class ModuleComponent extends BaseBean{
	
	
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
	
	
	
	
	
	private String active;
	
	

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
		return "TeeComponent [componentName=" + componentName
				+ ", componentDesc=" + componentDesc + ", moduleId=" + moduleId
				+ ", acadYear=" + acadYear + ", programId=" + programId
				+ ", campusId=" + campusId + ", active=" + active + "]";
	}
	
	
	
}
