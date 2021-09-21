package com.spts.lms.beans.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.group.Groups;

public class Assignment extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2748251831795725278L;

	/**
	 * 
	 */
	StudentAssignment studentAssignment;
	private String showFileDownload;
	private String showStudentFileDownload;
	private String runPlagiarism;
	private String plagscanRequired;
	private String rightGrant;
	private String campusName;
	private Long campusId;
	private String sendSmsAlertToParents;
	private String sendEmailAlertToParents;
	private String submissionDate;
	private String studentFilePath;
	private String submitByOneInGroup;

	private String isAcceptDisclaimer;

	private String completed;
	private String pending;
	private String rejected;
	private String lateSubmitted;

	private String approvalStatus;
	private String attempts;

	private String studentAssignmentId;
	private String isSubmitterInGroup;

	private Long moduleId;
	private String allocateToStudents;
	private String parentModuleId;
	private String moduleName;

	private String programId;
	private String courseIdToExport;

	private String createdByAdmin;

	private String completedCount;
	private String lateSubmittedCount;
	private String countOverall;
	private String facultyName;
	private String courseName;
	private String studentName;
	private String username;
	private String assignmentStatus;

	private boolean assignmentRemarkFile;

	// new fields

	private String lateSubmRemark;

	private String isHashKeyLateSubmitted;
	private String hashKey;
	private Date keyGenerationTime;
	private String showGenHashKey;
	
	private String studentHashKey;
	
	private String isCheckSumReq;
	
	private String isQuesConfigFromPool;
	private String maxQuestnToShow;
	private String randomQuestion;
	
	
	private String remarks;
	private String lowScoreReason;
	
	private String acadSession;
	private String questionNumber;
	private String marks;
	private String programName;
	
	
	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}

	public String getMarks() {
		return marks;
	}

	public void setMarks(String marks) {
		this.marks = marks;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLowScoreReason() {
		return lowScoreReason;
	}

	public void setLowScoreReason(String lowScoreReason) {
		this.lowScoreReason = lowScoreReason;
	}
	
	public String getIsQuesConfigFromPool() {
		return isQuesConfigFromPool;
	}

	public void setIsQuesConfigFromPool(String isQuesConfigFromPool) {
		
		this.isQuesConfigFromPool = isQuesConfigFromPool != null ? isQuesConfigFromPool : "N";
	}

	public String getMaxQuestnToShow() {
		return maxQuestnToShow;
	}

	public void setMaxQuestnToShow(String maxQuestnToShow) {
		this.maxQuestnToShow = maxQuestnToShow;
	}

	public String getRandomQuestion() {
		return randomQuestion;
	}

	public void setRandomQuestion(String randomQuestion) {
		
		this.randomQuestion = randomQuestion != null ? randomQuestion : "N";
	}
	

	public String getIsCheckSumReq() {
		return isCheckSumReq;
	}

	public void setIsCheckSumReq(String isCheckSumReq) {
		this.isCheckSumReq = isCheckSumReq;
	}

	public String getStudentHashKey() {
		return studentHashKey;
	}

	public void setStudentHashKey(String studentHashKey) {
		this.studentHashKey = studentHashKey;
	}

	public String getShowGenHashKey() {
		return showGenHashKey;
	}

	public void setShowGenHashKey(String showGenHashKey) {
		this.showGenHashKey = showGenHashKey;
	}

	public String getLateSubmRemark() {
		return lateSubmRemark;
	}

	public void setLateSubmRemark(String lateSubmRemark) {
		this.lateSubmRemark = lateSubmRemark;
	}

	public String getIsHashKeyLateSubmitted() {
		return isHashKeyLateSubmitted;
	}

	public void setIsHashKeyLateSubmitted(String isHashKeyLateSubmitted) {
		this.isHashKeyLateSubmitted = isHashKeyLateSubmitted;
	}

	public String getHashKey() {
		return hashKey;
	}

	public void setHashKey(String hashKey) {
		this.hashKey = hashKey;
	}

	public Date getKeyGenerationTime() {
		return keyGenerationTime;
	}

	public void setKeyGenerationTime(Date keyGenerationTime) {
		this.keyGenerationTime = keyGenerationTime;
	}

	public boolean isAssignmentRemarkFile() {
		return assignmentRemarkFile;
	}

	public void setAssignmentRemarkFile(boolean assignmentRemarkFile) {
		this.assignmentRemarkFile = assignmentRemarkFile;
	}

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(String completedCount) {
		this.completedCount = completedCount;
	}

	public String getLateSubmittedCount() {
		return lateSubmittedCount;
	}

	public void setLateSubmittedCount(String lateSubmittedCount) {
		this.lateSubmittedCount = lateSubmittedCount;
	}

	public String getCountOverall() {
		return countOverall;
	}

	public void setCountOverall(String countOverall) {
		this.countOverall = countOverall;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getCourseIdToExport() {
		return courseIdToExport;
	}

	public void setCourseIdToExport(String courseIdToExport) {
		this.courseIdToExport = courseIdToExport;
	}

	public String getCreatedByAdmin() {
		return createdByAdmin;
	}

	public void setCreatedByAdmin(String createdByAdmin) {
		this.createdByAdmin = createdByAdmin;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public String getAllocateToStudents() {
		return allocateToStudents;
	}

	public void setAllocateToStudents(String allocateToStudents) {
		this.allocateToStudents = allocateToStudents != null ? allocateToStudents : "N";
	}

	public String getParentModuleId() {
		return parentModuleId;
	}

	public void setParentModuleId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getStudentFilePath() {
		return studentFilePath;
	}

	public void setStudentFilePath(String studentFilePath) {
		this.studentFilePath = studentFilePath;
	}

	public String getIsSubmitterInGroup() {
		return isSubmitterInGroup;
	}

	public void setIsSubmitterInGroup(String isSubmitterInGroup) {
		this.isSubmitterInGroup = isSubmitterInGroup;
	}

	public String getStudentAssignmentId() {
		return studentAssignmentId;
	}

	public void setStudentAssignmentId(String studentAssignmentId) {
		this.studentAssignmentId = studentAssignmentId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getLateSubmitted() {
		return lateSubmitted;
	}

	public void setLateSubmitted(String lateSubmitted) {
		this.lateSubmitted = lateSubmitted;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getPending() {
		return pending;
	}

	public void setPending(String pending) {
		this.pending = pending;
	}

	public String getRejected() {
		return rejected;
	}

	public void setRejected(String rejected) {
		this.rejected = rejected;
	}

	public String getSubmitByOneInGroup() {
		return submitByOneInGroup;
	}

	public void setSubmitByOneInGroup(String submitByOneInGroup) {
		this.submitByOneInGroup = submitByOneInGroup != null ? submitByOneInGroup : "N";
	}

	public String getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(String submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getSendSmsAlertToParents() {
		return sendSmsAlertToParents;
	}

	public void setSendSmsAlertToParents(String sendSmsAlertToParents) {
		this.sendSmsAlertToParents = sendSmsAlertToParents != null ? sendSmsAlertToParents : "N";
	}

	public String getSendEmailAlertToParents() {
		return sendEmailAlertToParents;
	}

	public void setSendEmailAlertToParents(String sendEmailAlertToParents) {
		this.sendEmailAlertToParents = sendEmailAlertToParents != null ? sendEmailAlertToParents : "N";
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public Long getCampusId() {
		return campusId;
	}

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}

	public String getRunPlagiarism() {
		return runPlagiarism;
	}

	public void setRunPlagiarism(String runPlagiarism) {
		this.runPlagiarism = runPlagiarism;
	}

	public String getPlagscanRequired() {
		return plagscanRequired;
	}

	public void setPlagscanRequired(String plagscanRequired) {
		if (plagscanRequired == null) {
			this.plagscanRequired = "No";
		} else {
			this.plagscanRequired = plagscanRequired;
		}
	}

	public String getShowFileDownload() {
		return showFileDownload;
	}

	public void setShowFileDownload(String showFileDownload) {
		this.showFileDownload = showFileDownload;
	}

	public String getShowStudentFileDownload() {
		return showStudentFileDownload;
	}

	public String getRightGrant() {
		return rightGrant;
	}

	public void setRightGrant(String rightGrant) {
		this.rightGrant = rightGrant != null ? rightGrant : "N";

	}

	public void setShowStudentFileDownload(String showStudentFileDownload) {
		this.showStudentFileDownload = showStudentFileDownload;
	}

	private Long score;
	private Long wieghtageassigned;
	private String rollNo;
	private Integer threshold;

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public StudentAssignment getStudentAssignment() {
		return studentAssignment;
	}

	private String studentId;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public void setStudentAssignment(StudentAssignment studentAssignment) {
		this.studentAssignment = studentAssignment;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getWieghtageassigned() {
		return wieghtageassigned;
	}

	public void setWieghtageassigned(Long wieghtageassigned) {
		this.wieghtageassigned = wieghtageassigned;
	}

	private String assignmentName;
	private Long assignedId;
	String evaluationStatus;

	private String assignmentType;

	public String getEvaluationStatus() {
		return evaluationStatus;
	}

	public void setEvaluationStatus(String evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}

	private Integer count;

	public String getAssignmentType() {
		return assignmentType;
	}

	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getAssignedId() {
		return assignedId;
	}

	public void setAssignedId(Long assignedId) {
		this.assignedId = assignedId;
	}

	private String acadMonth;

	private Integer acadYear;

	private Long courseId;

	private String startDate;

	/* private String dueDate; */

	private String endDate;

	private String filePath;

	private String filePreviewPath;

	private String showResultsToStudents;

	private String active = "Y";

	private String facultyId;
	List<String> grps1 = new ArrayList<String>();
	List<String> grps2 = new ArrayList<String>();
	List<String> grps3 = new ArrayList<String>();
	List<String> grps4 = new ArrayList<String>();
	List<String> grps5 = new ArrayList<String>();

	public List<String> getGrps1() {
		return grps1;
	}

	public void setGrps1(List<String> grps1) {
		this.grps1 = grps1;
	}

	public List<String> getGrps2() {
		return grps2;
	}

	public void setGrps2(List<String> grps2) {
		this.grps2 = grps2;
	}

	public List<String> getGrps3() {
		return grps3;
	}

	public void setGrps3(List<String> grps3) {
		this.grps3 = grps3;
	}

	public List<String> getGrps4() {
		return grps4;
	}

	public void setGrps4(List<String> grps4) {
		this.grps4 = grps4;
	}

	public List<String> getGrps5() {
		return grps5;
	}

	public void setGrps5(List<String> grps5) {
		this.grps5 = grps5;
	}

	private String allowAfterEndDate;

	private String sendEmailAlert;

	private String sendSmsAlert;

	private String maxScore;

	private String assignmentText;

	private String idForCourse;

	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getIdForCourse() {
		return idForCourse;
	}

	public void setIdForCourse(String idForCourse) {
		this.idForCourse = idForCourse;
	}

	/**
	 * Non Persistent Attributes
	 *
	 **/

	public String getSelectedGrps() {
		return selectedGrps;
	}

	String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<Groups> getGroups() {
		return groups;
	}

	public void setGroups(List<Groups> groups) {
		this.groups = groups;
	}

	public void setSelectedGrps(String selectedGrps) {
		this.selectedGrps = selectedGrps;
	}

	String selectedGrps;
	private List<String> students = new ArrayList<String>();
	List<Groups> groups;

	List<String> grps = new ArrayList<String>();

	public List<String> getGrps() {
		return grps;
	}

	public void setGrps(List<String> grps) {
		this.grps = grps;
	}

	List<StudentAssignment> groupAssigment = new ArrayList<StudentAssignment>();

	public List<StudentAssignment> getGroupAssigment() {
		return groupAssigment;
	}

	public void setGroupAssigment(List<StudentAssignment> groupAssigment) {
		this.groupAssigment = groupAssigment;
	}

	private String submissionStatus;
	private Course course = new Course();

	public String getSubmissionStatus() {
		return submissionStatus;
	}

	public void setSubmissionStatus(String submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStartDate() {
		return formatDate(startDate);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/*
	 * public String getDueDate() { return formatDate(dueDate); }
	 * 
	 * public void setDueDate(String dueDate) { this.dueDate = dueDate; }
	 */

	public String getEndDate() {
		return formatDate(endDate);
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePreviewPath() {
		return filePreviewPath;
	}

	public void setFilePreviewPath(String filePreviewPath) {
		this.filePreviewPath = filePreviewPath;
	}

	public String getShowResultsToStudents() {
		return showResultsToStudents;
	}

	public void setShowResultsToStudents(String showResultsToStudents) {
		this.showResultsToStudents = showResultsToStudents != null ? showResultsToStudents : "N";
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getAllowAfterEndDate() {
		return allowAfterEndDate;
	}

	public void setAllowAfterEndDate(String allowAfterEndDate) {
		this.allowAfterEndDate = allowAfterEndDate != null ? allowAfterEndDate : "N";
	}

	public String getSendEmailAlert() {
		return sendEmailAlert;
	}

	public void setSendEmailAlert(String sendEmailAlert) {
		this.sendEmailAlert = sendEmailAlert != null ? sendEmailAlert : "N";
	}

	public String getSendSmsAlert() {
		return sendSmsAlert;
	}

	public void setSendSmsAlert(String sendSmsAlert) {
		this.sendSmsAlert = sendSmsAlert != null ? sendSmsAlert : "N";
	}

	public Assignment() {

	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	public Long getCourseId() {
		if (null != course)
			return course.getId();
		return courseId;
	}

	public void setCourseId(Long courseId) {
		if (null != course)
			course.setId(courseId);
		this.courseId = courseId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getCourseName() {
		// return course.getCourseName();
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
		// course.setCourseName(courseName);
	}

	public String getAssignmentText() {
		return assignmentText;
	}

	public void setAssignmentText(String assignmentText) {
		this.assignmentText = assignmentText;
	}

	public String getIsAcceptDisclaimer() {
		return isAcceptDisclaimer;
	}

	public void setIsAcceptDisclaimer(String isAcceptDisclaimer) {
		this.isAcceptDisclaimer = isAcceptDisclaimer;
	}

	@Override
	public String toString() {
		return "Assignment [studentAssignment=" + studentAssignment + ", showFileDownload=" + showFileDownload
				+ ", showStudentFileDownload=" + showStudentFileDownload + ", runPlagiarism=" + runPlagiarism
				+ ", plagscanRequired=" + plagscanRequired + ", rightGrant=" + rightGrant + ", campusName=" + campusName
				+ ", campusId=" + campusId + ", sendSmsAlertToParents=" + sendSmsAlertToParents
				+ ", sendEmailAlertToParents=" + sendEmailAlertToParents + ", submissionDate=" + submissionDate
				+ ", studentFilePath=" + studentFilePath + ", submitByOneInGroup=" + submitByOneInGroup
				+ ", isAcceptDisclaimer=" + isAcceptDisclaimer + ", completed=" + completed + ", pending=" + pending
				+ ", rejected=" + rejected + ", lateSubmitted=" + lateSubmitted + ", approvalStatus=" + approvalStatus
				+ ", attempts=" + attempts + ", studentAssignmentId=" + studentAssignmentId + ", isSubmitterInGroup="
				+ isSubmitterInGroup + ", moduleId=" + moduleId + ", allocateToStudents=" + allocateToStudents
				+ ", parentModuleId=" + parentModuleId + ", moduleName=" + moduleName + ", programId=" + programId
				+ ", courseIdToExport=" + courseIdToExport + ", createdByAdmin=" + createdByAdmin + ", completedCount="
				+ completedCount + ", lateSubmittedCount=" + lateSubmittedCount + ", countOverall=" + countOverall
				+ ", facultyName=" + facultyName + ", courseName=" + courseName + ", studentName=" + studentName
				+ ", username=" + username + ", assignmentStatus=" + assignmentStatus + ", assignmentRemarkFile="
				+ assignmentRemarkFile + ", lateSubmRemark=" + lateSubmRemark + ", isHashKeyLateSubmitted="
				+ isHashKeyLateSubmitted + ", hashKey=" + hashKey + ", keyGenerationTime=" + keyGenerationTime
				+ ", showGenHashKey=" + showGenHashKey + ", studentHashKey=" + studentHashKey + ", isCheckSumReq="
				+ isCheckSumReq + ", isQuesConfigFromPool=" + isQuesConfigFromPool + ", maxQuestnToShow="
				+ maxQuestnToShow + ", randomQuestion=" + randomQuestion + ", remarks=" + remarks + ", lowScoreReason="
				+ lowScoreReason + ", score=" + score + ", wieghtageassigned=" + wieghtageassigned + ", rollNo="
				+ rollNo + ", threshold=" + threshold + ", studentId=" + studentId + ", assignmentName="
				+ assignmentName + ", assignedId=" + assignedId + ", evaluationStatus=" + evaluationStatus
				+ ", assignmentType=" + assignmentType + ", count=" + count + ", acadMonth=" + acadMonth + ", acadYear="
				+ acadYear + ", courseId=" + courseId + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", filePath=" + filePath + ", filePreviewPath=" + filePreviewPath + ", showResultsToStudents="
				+ showResultsToStudents + ", active=" + active + ", facultyId=" + facultyId + ", grps1=" + grps1
				+ ", grps2=" + grps2 + ", grps3=" + grps3 + ", grps4=" + grps4 + ", grps5=" + grps5
				+ ", allowAfterEndDate=" + allowAfterEndDate + ", sendEmailAlert=" + sendEmailAlert + ", sendSmsAlert="
				+ sendSmsAlert + ", maxScore=" + maxScore + ", assignmentText=" + assignmentText + ", idForCourse="
				+ idForCourse + ", groupId=" + groupId + ", groupName=" + groupName + ", selectedGrps=" + selectedGrps
				+ ", students=" + students + ", groups=" + groups + ", grps=" + grps + ", groupAssigment="
				+ groupAssigment + ", submissionStatus=" + submissionStatus + ", course=" + course + "]";
	}

}
