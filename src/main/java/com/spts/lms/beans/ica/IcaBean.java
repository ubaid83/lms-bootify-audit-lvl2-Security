package com.spts.lms.beans.ica;

import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.UserCourse;

public class IcaBean extends BaseBean{
	
	
	private String moduleId;
	private String acadYear;
	private String campusId;
	private String programId;
	private String componentName;
	private String componentId;
	private String icaCompId;
	private String icaCompMarks;
	private String publishedDate;
	//private String programName;
	private String icaQueryId;
	
	//private String isApproved;
	//private String icaQueryId;
	
	private String isApproved;
	//New Field
	
	private String flagTcs;
	
	private String eventId;
	private String isIcaDivisionWise;
	private String parentIcaId;
	private String courseName;
	private String facultyName;
	private String isNonEventModule;
	private String status;
	private String isCourseraIca;
	
	//new fields
	private String testIdsForIca;
	private String modeOfAddingTestMarks;
	private int bestOf;
	private List<String> testIds;
	private String courseId;
	private String sequenceNo;
	private String showTestMarksIcon; 
	
	//field added on 26-07-2021
	private String isPublishCompWise;
	
	private String submittedComps;
	private String publishedComps;
	
	//Peter 25/11/2021
	private String saveAsDraft;
	
	public String getSaveAsDraft() {
		return saveAsDraft;
	}

	public void setSaveAsDraft(String saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}	

	public String getSubmittedComps() {
		return submittedComps;
	}

	public void setSubmittedComps(String submittedComps) {
		this.submittedComps = submittedComps;
	}

	public String getPublishedComps() {
		return publishedComps;
	}

	public void setPublishedComps(String publishedComps) {
		this.publishedComps = publishedComps;
	}

	public String getIsPublishCompWise() {
		return isPublishCompWise;
	}

	public void setIsPublishCompWise(String isPublishCompWise) {
		this.isPublishCompWise = isPublishCompWise;
	}

	public String getFlagTcs() {
		return flagTcs;
	}

	public void setFlagTcs(String flagTcs) {
		this.flagTcs = flagTcs;
	}

	public String getShowTestMarksIcon() {
		return showTestMarksIcon;
	}

	public void setShowTestMarksIcon(String showTestMarksIcon) {
		this.showTestMarksIcon = showTestMarksIcon;
	}

	public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTestIdsForIca() {
		return testIdsForIca;
	}

	public void setTestIdsForIca(String testIdsForIca) {
		this.testIdsForIca = testIdsForIca;
	}

	public String getModeOfAddingTestMarks() {
		return modeOfAddingTestMarks;
	}

	public void setModeOfAddingTestMarks(String modeOfAddingTestMarks) {
		this.modeOfAddingTestMarks = modeOfAddingTestMarks;
	}

	public int getBestOf() {
		return bestOf;
	}

	public void setBestOf(int bestOf) {
		this.bestOf = bestOf;
	}

	public List<String> getTestIds() {
		return testIds;
	}

	public void setTestIds(List<String> testIds) {
		this.testIds = testIds;
	}

	public String getIsCourseraIca() {
		return isCourseraIca;
	}

	public void setIsCourseraIca(String isCourseraIca) {
		this.isCourseraIca = isCourseraIca;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsNonEventModule() {
		return isNonEventModule;
	}

	public void setIsNonEventModule(String isNonEventModule) {
		this.isNonEventModule = isNonEventModule;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getIsIcaDivisionWise() {
		return isIcaDivisionWise;
	}

	public void setIsIcaDivisionWise(String isIcaDivisionWise) {
		this.isIcaDivisionWise = isIcaDivisionWise;
	}

	public String getParentIcaId() {
		return parentIcaId;
	}

	public void setParentIcaId(String parentIcaId) {
		this.parentIcaId = parentIcaId;
	}

	public String getIcaQueryId() {
		return icaQueryId;
	}

	public void setIcaQueryId(String icaQueryId) {
		this.icaQueryId = icaQueryId;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	List<String> programIds;
	
	public List<String> getProgramIds() {
		return programIds;
	}

	public void setProgramIds(List<String> programIds) {
		this.programIds = programIds;
	}

	private String isPublished;
	private String isSubmitted;
	
	
	
	
	public String getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}

	public String getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(String isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	private List<String> components = new ArrayList<String>();
	
	public List<UserCourse> userCourseList = new ArrayList<>();
	public List<IcaComponent> componentList =  new ArrayList<>();
	
	
	public List<IcaComponent> getComponentList() {
		return componentList;
	}

	public List<UserCourse> getUserCourseList() {
		return userCourseList;
	}

	public void setUserCourseList(List<UserCourse> userCourseList) {
		this.userCourseList = userCourseList;
	}

	public void setComponentList(List<IcaComponent> componentList) {
		this.componentList = componentList;
	}

	public String getIcaCompId() {
		return icaCompId;
	}

	public void setIcaCompId(String icaCompId) {
		this.icaCompId = icaCompId;
	}

	public String getIcaCompMarks() {
		return icaCompMarks;
	}

	public void setIcaCompMarks(String icaCompMarks) {
		this.icaCompMarks = icaCompMarks;
	}

	private String icaName;
	
	private String icaDesc;
	
	private String internalMarks;
	private String externalMarks;
	
	private String totalMarks;
	private String programName;
	private String campusName;
	private String moduleName;
	private String acadSession;
	private String scaleUpOrDown;
	
	private String scaledMarks;
	
	private String assignedFaculty;
	private String internalPassMarks;
	private String externalPassMarks;
	
	private String startDate;
	private String endDate;
	
	private String scaledReq;
	
	
	
	public List<String> getComponents() {
		return components;
	}

	public void setComponents(List<String> components) {
		this.components = components;
	}

	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getScaledReq() {
		return scaledReq;
	}

	public void setScaledReq(String scaledReq) {
		this.scaledReq = scaledReq;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getInternalPassMarks() {
		return internalPassMarks;
	}

	public void setInternalPassMarks(String internalPassMarks) {
		this.internalPassMarks = internalPassMarks;
	}

	public String getExternalPassMarks() {
		return externalPassMarks;
	}

	public void setExternalPassMarks(String externalPassMarks) {
		this.externalPassMarks = externalPassMarks;
	}

	private String active;
	
	
	

	public String getScaleUpOrDown() {
		return scaleUpOrDown;
	}

	public void setScaleUpOrDown(String scaleUpOrDown) {
		this.scaleUpOrDown = scaleUpOrDown;
	}

	public String getScaledMarks() {
		return scaledMarks;
	}

	public void setScaledMarks(String scaledMarks) {
		this.scaledMarks = scaledMarks;
	}

	public String getAssignedFaculty() {
		return assignedFaculty;
	}

	public void setAssignedFaculty(String assignedFaculty) {
		this.assignedFaculty = assignedFaculty;
	}

	public String getModuleId() {
		return moduleId;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getIcaName() {
		return icaName;
	}

	public void setIcaName(String icaName) {
		this.icaName = icaName;
	}

	public String getIcaDesc() {
		return icaDesc;
	}

	public void setIcaDesc(String icaDesc) {
		this.icaDesc = icaDesc;
	}

	public String getInternalMarks() {
		return internalMarks;
	}

	public void setInternalMarks(String internalMarks) {
		this.internalMarks = internalMarks;
	}

	public String getExternalMarks() {
		return externalMarks;
	}

	public void setExternalMarks(String externalMarks) {
		this.externalMarks = externalMarks;
	}

	public String getTotalMarks() {
		return totalMarks;
	}

	public void setTotalMarks(String totalMarks) {
		this.totalMarks = totalMarks;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "IcaBean [moduleId=" + moduleId + ", acadYear=" + acadYear + ", campusId=" + campusId + ", programId="
				+ programId + ", componentName=" + componentName + ", componentId=" + componentId + ", icaCompId="
				+ icaCompId + ", icaCompMarks=" + icaCompMarks + ", publishedDate=" + publishedDate + ", icaQueryId="
				+ icaQueryId + ", isApproved=" + isApproved + ", flagTcs=" + flagTcs + ", eventId=" + eventId
				+ ", isIcaDivisionWise=" + isIcaDivisionWise + ", parentIcaId=" + parentIcaId + ", courseName="
				+ courseName + ", facultyName=" + facultyName + ", isNonEventModule=" + isNonEventModule + ", status="
				+ status + ", isCourseraIca=" + isCourseraIca + ", testIdsForIca=" + testIdsForIca
				+ ", modeOfAddingTestMarks=" + modeOfAddingTestMarks + ", bestOf=" + bestOf + ", testIds=" + testIds
				+ ", courseId=" + courseId + ", sequenceNo=" + sequenceNo + ", showTestMarksIcon=" + showTestMarksIcon
				+ ", isPublishCompWise=" + isPublishCompWise + ", submittedComps=" + submittedComps
				+ ", publishedComps=" + publishedComps + ", programIds=" + programIds + ", isPublished=" + isPublished
				+ ", isSubmitted=" + isSubmitted + ", components=" + components + ", userCourseList=" + userCourseList
				+ ", componentList=" + componentList + ", icaName=" + icaName + ", icaDesc=" + icaDesc
				+ ", internalMarks=" + internalMarks + ", externalMarks=" + externalMarks + ", totalMarks=" + totalMarks
				+ ", programName=" + programName + ", campusName=" + campusName + ", moduleName=" + moduleName
				+ ", acadSession=" + acadSession + ", scaleUpOrDown=" + scaleUpOrDown + ", scaledMarks=" + scaledMarks
				+ ", assignedFaculty=" + assignedFaculty + ", internalPassMarks=" + internalPassMarks
				+ ", externalPassMarks=" + externalPassMarks + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", scaledReq=" + scaledReq + ", active=" + active + "]";
	}
	
	
	
}
