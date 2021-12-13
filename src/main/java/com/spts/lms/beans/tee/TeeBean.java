package com.spts.lms.beans.tee;

import java.util.List;

import com.spts.lms.beans.BaseBean;

public class TeeBean extends BaseBean {

	private String moduleId;
	private String acadYear;
	private String campusId;
	private String programId;
	private String acadSession;
	private String eventId;
	private String teeName;
	private String teeDesc;
	private String assignedFaculty;
	private String scaledReq;
	private String scaledMarks;
	private String parentTeeId;
	private String internalMarks;
	private String internalPassMarks;
	private String externalPassMarks;
	private String externalMarks;
	private String totalMarks;
	private String active;
	private String isSubmitted;
	private String isPublished;
	private String isTeeDivisionWise;
	private String isNonEventModule;
	private String isCourseraTee;
	private String publishedDate;
	private String startDate;
	private String endDate;
	private String courseName;
	private String programName;
	private String campusName;
	private String moduleName;
	private String facultyName;
	private String teeQueryId;
	private String isApproved;
	private String status;

	// New Fields

	private String teeId;
	private String assignmentIdsForTee;
	private String modeOfAddingAssignmentMarks;
	private int bestOf;
	private String marksAdded;

	private String courseId;

	private List<String> assignmentIds;

	private String autoAssignMarks;
	
	private String flagTcs;
	
	//Peter 25/11/2021
	private String saveAsDraft;
	
	public String getSaveAsDraft() {
		return saveAsDraft;
	}

	public void setSaveAsDraft(String saveAsDraft) {
		this.saveAsDraft = saveAsDraft;
	}

	public String getFlagTcs() {
		return flagTcs;
	}

	public void setFlagTcs(String flagTcs) {
		this.flagTcs = flagTcs;
	}

	public String getTeeId() {
		return teeId;
	}

	public void setTeeId(String teeId) {
		this.teeId = teeId;
	}

	public String getAssignmentIdsForTee() {
		return assignmentIdsForTee;
	}

	public void setAssignmentIdsForTee(String assignmentIdsForTee) {
		this.assignmentIdsForTee = assignmentIdsForTee;
	}

	public String getModeOfAddingAssignmentMarks() {
		return modeOfAddingAssignmentMarks;
	}

	public void setModeOfAddingAssignmentMarks(String modeOfAddingAssignmentMarks) {
		this.modeOfAddingAssignmentMarks = modeOfAddingAssignmentMarks;
	}

	public int getBestOf() {
		return bestOf;
	}

	public void setBestOf(int bestOf) {
		this.bestOf = bestOf;
	}

	public String getMarksAdded() {
		return marksAdded;
	}

	public void setMarksAdded(String marksAdded) {
		this.marksAdded = marksAdded;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public List<String> getAssignmentIds() {
		return assignmentIds;
	}

	public void setAssignmentIds(List<String> assignmentIds) {
		this.assignmentIds = assignmentIds;
	}

	public String getAutoAssignMarks() {
		return autoAssignMarks;
	}

	public void setAutoAssignMarks(String autoAssignMarks) {
		this.autoAssignMarks = autoAssignMarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

	public String getTeeQueryId() {
		return teeQueryId;
	}

	public void setTeeQueryId(String teeQueryId) {
		this.teeQueryId = teeQueryId;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
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

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getTeeName() {
		return teeName;
	}

	public void setTeeName(String teeName) {
		this.teeName = teeName;
	}

	public String getTeeDesc() {
		return teeDesc;
	}

	public void setTeeDesc(String teeDesc) {
		this.teeDesc = teeDesc;
	}

	public String getAssignedFaculty() {
		return assignedFaculty;
	}

	public void setAssignedFaculty(String assignedFaculty) {
		this.assignedFaculty = assignedFaculty;
	}

	public String getScaledReq() {
		return scaledReq;
	}

	public void setScaledReq(String scaledReq) {
		this.scaledReq = scaledReq;
	}

	public String getScaledMarks() {
		return scaledMarks;
	}

	public void setScaledMarks(String scaledMarks) {
		this.scaledMarks = scaledMarks;
	}

	public String getParentTeeId() {
		return parentTeeId;
	}

	public void setParentTeeId(String parentTeeId) {
		this.parentTeeId = parentTeeId;
	}

	public String getInternalMarks() {
		return internalMarks;
	}

	public void setInternalMarks(String internalMarks) {
		this.internalMarks = internalMarks;
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

	public String getIsTeeDivisionWise() {
		return isTeeDivisionWise;
	}

	public void setIsTeeDivisionWise(String isTeeDivisionWise) {
		this.isTeeDivisionWise = isTeeDivisionWise;
	}

	public String getIsNonEventModule() {
		return isNonEventModule;
	}

	public void setIsNonEventModule(String isNonEventModule) {
		this.isNonEventModule = isNonEventModule;
	}

	public String getIsCourseraTee() {
		return isCourseraTee;
	}

	public void setIsCourseraTee(String isCourseraTee) {
		this.isCourseraTee = isCourseraTee;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
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

	@Override
	public String toString() {
		return "TeeBean [moduleId=" + moduleId + ", acadYear=" + acadYear + ", campusId=" + campusId + ", programId="
				+ programId + ", acadSession=" + acadSession + ", eventId=" + eventId + ", teeName=" + teeName
				+ ", teeDesc=" + teeDesc + ", assignedFaculty=" + assignedFaculty + ", scaledReq=" + scaledReq
				+ ", scaledMarks=" + scaledMarks + ", parentTeeId=" + parentTeeId + ", internalMarks=" + internalMarks
				+ ", internalPassMarks=" + internalPassMarks + ", externalPassMarks=" + externalPassMarks
				+ ", externalMarks=" + externalMarks + ", totalMarks=" + totalMarks + ", active=" + active
				+ ", isSubmitted=" + isSubmitted + ", isPublished=" + isPublished + ", isTeeDivisionWise="
				+ isTeeDivisionWise + ", isNonEventModule=" + isNonEventModule + ", isCourseraTee=" + isCourseraTee
				+ ", publishedDate=" + publishedDate + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", courseName=" + courseName + ", programName=" + programName + ", campusName=" + campusName
				+ ", moduleName=" + moduleName + ", facultyName=" + facultyName + ", teeQueryId=" + teeQueryId
				+ ", isApproved=" + isApproved + ", status=" + status + ", teeId=" + teeId + ", assignmentIdsForTee="
				+ assignmentIdsForTee + ", modeOfAddingAssignmentMarks=" + modeOfAddingAssignmentMarks + ", bestOf="
				+ bestOf + ", marksAdded=" + marksAdded + ", courseId=" + courseId + ", autoAssignMarks="
				+ autoAssignMarks + "]";
	}

}
