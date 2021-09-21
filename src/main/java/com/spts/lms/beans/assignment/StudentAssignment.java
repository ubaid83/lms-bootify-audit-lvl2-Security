package com.spts.lms.beans.assignment;

import java.io.Serializable;
import java.util.Date;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class StudentAssignment extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String active;

	private String studentId;
	private String showFileDownload;
	private String showStudentFileDownload;
	private String url;
	private String plagiarismChecked;
	private String campusName;
	private Long campusId;
	
	private String isSubmitterInGroup;
	private String studentName;
	
	private String assignmentNote;
	private String assignmentError;
	private String assignmentSuccess;
	
	private String unscored;
	private int count;
	private String moduleId;
	
	private String facultyId;
	
	
	 private String assignmentStatus;
	 private String studentPlayerId;	
	 private String assignmentCompleted;
	 
	 //new fields
	 
	 private String lateSubmRemark;
		
	 private String isHashKeyLateSubmitted;
	 private String hashKey;
	 private Date keyGenerationTime;
		 
	 private String isAcceptDisclaimer;
	 private String acceptDisclaimerDate;
	 
	 private String studentAssignmentId;
	 
	 
	
	public String getStudentAssignmentId() {
		return studentAssignmentId;
	}

	public void setStudentAssignmentId(String studentAssignmentId) {
		this.studentAssignmentId = studentAssignmentId;
	}

	public String getAcceptDisclaimerDate() {
		return acceptDisclaimerDate;
	}

	public void setAcceptDisclaimerDate(String acceptDisclaimerDate) {
		this.acceptDisclaimerDate = acceptDisclaimerDate;
	}

	public String getIsAcceptDisclaimer() {
		return isAcceptDisclaimer;
	}

	public void setIsAcceptDisclaimer(String isAcceptDisclaimer) {
		this.isAcceptDisclaimer = isAcceptDisclaimer;
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

	public String getAssignmentStatus() {
		return assignmentStatus;
	}

	public void setAssignmentStatus(String assignmentStatus) {
		this.assignmentStatus = assignmentStatus;
	}

	public String getStudentPlayerId() {
		return studentPlayerId;
	}

	public void setStudentPlayerId(String studentPlayerId) {
		this.studentPlayerId = studentPlayerId;
	}

	public String getAssignmentCompleted() {
		return assignmentCompleted;
	}

	public void setAssignmentCompleted(String assignmentCompleted) {
		this.assignmentCompleted = assignmentCompleted;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUnscored() {
		return unscored;
	}

	public void setUnscored(String unscored) {
		this.unscored = unscored;
	}

	public String getAssignmentNote() {
		return assignmentNote;
	}

	public void setAssignmentNote(String assignmentNote) {
		this.assignmentNote = assignmentNote;
	}

	public String getAssignmentError() {
		return assignmentError;
	}

	public void setAssignmentError(String assignmentError) {
		this.assignmentError = assignmentError;
	}

	public String getAssignmentSuccess() {
		return assignmentSuccess;
	}

	public void setAssignmentSuccess(String assignmentSuccess) {
		this.assignmentSuccess = assignmentSuccess;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getIsSubmitterInGroup() {
			return isSubmitterInGroup;
		}

		public void setIsSubmitterInGroup(String isSubmitterInGroup) {
			this.isSubmitterInGroup = isSubmitterInGroup != null ? isSubmitterInGroup
					: "N";
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPlagiarismChecked() {
		return plagiarismChecked;
	}

	public void setPlagiarismChecked(String plagiarismChecked) {
		this.plagiarismChecked = plagiarismChecked;
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

	public void setShowStudentFileDownload(String showStudentFileDownload) {
		this.showStudentFileDownload = showStudentFileDownload;
	}

	private String rollNo;
	private Integer threshold;
	private double minMatchPercent;

	public double getMinMatchPercent() {
		return minMatchPercent;
	}

	public void setMinMatchPercent(double minMatchPercent) {
		this.minMatchPercent = minMatchPercent;
	}

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

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	private Integer maxScore;

	public Integer getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	private String username;

	private String allocated;

	public String getAllocated() {
		return allocated;
	}

	public void setAllocated(String allocated) {
		this.allocated = allocated;
	}

	private String approvalStatus;

	private String acadMonth;

	private Integer acadYear;

	private Long courseId;

	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	private String studentFilePath;

	private String filePreviewPath;

	private int attempts;

	private String evaluationStatus;

	private String evaluatedBy;

	private String submissionStatus;

	private String score;

	private String remarks;

	private Date submissionDate;

	private Long assignmentId;

	private String lowScoreReason;

	private String startDate;

	/* private String dueDate; */

	private String endDate;

	String groupName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	/**
	 * Non persistent fields
	 */
	private User user = new User();

	private Program program = new Program();
	private Assignment assignment = new Assignment();
	private Course course = new Course();

	/**
	 * 
	 */

	public Long getAssignmentId() {
		return assignmentId;
	}

	public String getLowScoreReason() {
		return lowScoreReason;
	}

	public void setLowScoreReason(String lowScoreReason) {
		this.lowScoreReason = lowScoreReason;
	}

	public String getAssignmentName() {
		return assignment.getAssignmentName();
	}

	public void setAssignmentName(String assignmentName) {
		assignment.setAssignmentName(assignmentName);
		;
	}

	public String getAssignmentText() {
		return assignment.getAssignmentText();
	}

	public void setAssignmentText(String assignmentText) {
		assignment.setAssignmentText(assignmentText);
	}

	public String getFilePath() {
		return assignment.getFilePath();
	}

	public void setFilePath(String filePath) {
		assignment.setFilePath(filePath);
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		user.setUsername(username);
		this.username = username;
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
		return course.getCourseName();
	}

	public void setCourseName(String courseName) {
		course.setCourseName(courseName);
	}

	public String getStudentFilePath() {
		return studentFilePath;
	}

	public void setStudentFilePath(String studentFilePath) {
		this.studentFilePath = studentFilePath;
	}

	public String getFilePreviewPath() {
		return filePreviewPath;
	}

	public void setFilePreviewPath(String filePreviewPath) {
		this.filePreviewPath = filePreviewPath;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public String getEvaluationStatus() {
		return evaluationStatus;
		// return checkYElseSetN(evaluationStatus);
	}

	public void setEvaluationStatus(String evaluationStatus) {
		this.evaluationStatus = evaluationStatus;
	}

	public String getEvaluatedBy() {
		return evaluatedBy;
	}

	public void setEvaluatedBy(String evaluatedBy) {
		this.evaluatedBy = evaluatedBy;
	}

	public String getSubmissionStatus() {
		return submissionStatus;
		// return checkYElseSetN(submissionStatus);
	}

	public void setSubmissionStatus(String submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public String getFirstname() {
		return user.getFirstname();
	}

	public void setFirstname(String firstname) {
		user.setFirstname(firstname);
	}

	public String getLastname() {
		return user.getLastname();
	}

	public void setLastname(String lastname) {
		user.setLastname(lastname);
	}

	public String getProgramName() {
		return program.getProgramName();
	}

	public void setProgramName(String programName) {
		program.setProgramName(programName);
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	@Override
	public String toString() {
		return "StudentAssignment [active=" + active + ", studentId=" + studentId + ", showFileDownload="
				+ showFileDownload + ", showStudentFileDownload=" + showStudentFileDownload + ", url=" + url
				+ ", plagiarismChecked=" + plagiarismChecked + ", campusName=" + campusName + ", campusId=" + campusId
				+ ", isSubmitterInGroup=" + isSubmitterInGroup + ", studentName=" + studentName + ", assignmentNote="
				+ assignmentNote + ", assignmentError=" + assignmentError + ", assignmentSuccess=" + assignmentSuccess
				+ ", unscored=" + unscored + ", count=" + count + ", moduleId=" + moduleId + ", facultyId=" + facultyId
				+ ", assignmentStatus=" + assignmentStatus + ", studentPlayerId=" + studentPlayerId
				+ ", assignmentCompleted=" + assignmentCompleted + ", lateSubmRemark=" + lateSubmRemark
				+ ", isHashKeyLateSubmitted=" + isHashKeyLateSubmitted + ", hashKey=" + hashKey + ", keyGenerationTime="
				+ keyGenerationTime + ", isAcceptDisclaimer=" + isAcceptDisclaimer + ", acceptDisclaimerDate="
				+ acceptDisclaimerDate + ", studentAssignmentId=" + studentAssignmentId + ", rollNo=" + rollNo
				+ ", threshold=" + threshold + ", minMatchPercent=" + minMatchPercent + ", maxScore=" + maxScore
				+ ", username=" + username + ", allocated=" + allocated + ", approvalStatus=" + approvalStatus
				+ ", acadMonth=" + acadMonth + ", acadYear=" + acadYear + ", courseId=" + courseId + ", groupId="
				+ groupId + ", studentFilePath=" + studentFilePath + ", filePreviewPath=" + filePreviewPath
				+ ", attempts=" + attempts + ", evaluationStatus=" + evaluationStatus + ", evaluatedBy=" + evaluatedBy
				+ ", submissionStatus=" + submissionStatus + ", score=" + score + ", remarks=" + remarks
				+ ", submissionDate=" + submissionDate + ", assignmentId=" + assignmentId + ", lowScoreReason="
				+ lowScoreReason + ", startDate=" + startDate + ", endDate=" + endDate + ", groupName=" + groupName
				+ ", user=" + user + ", program=" + program + ", assignment=" + assignment + ", course=" + course + "]";
	}


	

}
