package com.spts.lms.beans.assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class GroupAssignment extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String acadMonth;

	private Integer acadYear;

	private Long courseId;

	private String studentFilePath;

	private String filePreviewPath;

	private String startDate;

	/*private String dueDate;*/
	public String groupName;

	private String endDate;
	
	private String campusName;
	private Long campusId;

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

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
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
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
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

	public String getAttempts() {
		return attempts;
	}

	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	public String getEvaluationStatus() {
		return evaluationStatus;
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
	}

	public void setSubmissionStatus(String submissionStatus) {
		this.submissionStatus = submissionStatus;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
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

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getLowScoreReason() {
		return lowScoreReason;
	}

	public void setLowScoreReason(String lowScoreReason) {
		this.lowScoreReason = lowScoreReason;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String attempts;

	private String evaluationStatus;

	private String evaluatedBy;

	private String submissionStatus;

	private Integer score;

	private String remarks;

	private Date submissionDate;

	private Long assignmentId;

	private String lowScoreReason;

	private Long groupId;

	List<String> groupNames = new ArrayList<String>();

	public List<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}

	private User user = new User();

	private Program program = new Program();
	private Assignment assignment = new Assignment();
	private Course course = new Course();

	@Override
	public String toString() {
		return "GroupAssignment [username=" + username + ", acadMonth="
				+ acadMonth + ", acadYear=" + acadYear + ", courseId="
				+ courseId + ", studentFilePath=" + studentFilePath
				+ ", filePreviewPath=" + filePreviewPath + ", startDate="
				+ startDate + ", groupName=" + groupName + ", endDate="
				+ endDate + ", campusName=" + campusName + ", campusId="
				+ campusId + ", attempts=" + attempts + ", evaluationStatus="
				+ evaluationStatus + ", evaluatedBy=" + evaluatedBy
				+ ", submissionStatus=" + submissionStatus + ", score=" + score
				+ ", remarks=" + remarks + ", submissionDate=" + submissionDate
				+ ", assignmentId=" + assignmentId + ", lowScoreReason="
				+ lowScoreReason + ", groupId=" + groupId + ", groupNames="
				+ groupNames + ", user=" + user + ", program=" + program
				+ ", assignment=" + assignment + ", course=" + course + "]";
	}

	
	

}
