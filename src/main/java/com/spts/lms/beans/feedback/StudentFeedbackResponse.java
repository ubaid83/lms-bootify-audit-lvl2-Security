package com.spts.lms.beans.feedback;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;

public class StudentFeedbackResponse extends BaseBean {

	@Override
	public String toString() {
		return "StudentFeedbackResponse [username=" + username
				+ ", studentFeedbackId=" + studentFeedbackId
				+ ", feedbackQuestionId=" + feedbackQuestionId + ", answer="
				+ answer + ", rollNo=" + rollNo + ", average=" + average
				+ ", campusName=" + campusName + ", campusId=" + campusId
				+ ", dept=" + dept + ", lowestFeedback=" + lowestFeedback
				+ ", highestFeedback=" + highestFeedback + ", averageFeedback="
				+ averageFeedback + ", moduleName=" + moduleName
				+ ", facultyId=" + facultyId + ", moduleId=" + moduleId
				+ ", programName=" + programName + ", facultyName="
				+ facultyName + ", noOfStudentCompleted="
				+ noOfStudentCompleted + ", answers="
				+ Arrays.toString(answers) + ", courseId=" + courseId
				+ ", courseAverage=" + courseAverage + ", comments=" + comments
				+ "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429409931926762849L;
	private String username;
	private String studentFeedbackId;
	private Long feedbackQuestionId;
	private String answer;
	private String rollNo;
	private String average;
	private String campusName;
	private Long campusId;

	private String dept;

	private double lowestFeedback;
	private double highestFeedback;

	private double averageFeedback;
	private String moduleName;

	private String facultyId;
	private String moduleId;
	private String programName;
	
	private String facultyName;
	private String noOfStudentCompleted;
	
	
	
	
	
	

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getNoOfStudentCompleted() {
		return noOfStudentCompleted;
	}

	public void setNoOfStudentCompleted(String noOfStudentCompleted) {
		this.noOfStudentCompleted = noOfStudentCompleted;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public double getLowestFeedback() {
		return lowestFeedback;
	}

	public void setLowestFeedback(double lowestFeedback) {
		this.lowestFeedback = lowestFeedback;
	}

	public double getHighestFeedback() {
		return highestFeedback;
	}

	public void setHighestFeedback(double highestFeedback) {
		this.highestFeedback = highestFeedback;
	}

	public double getAverageFeedback() {
		return averageFeedback;
	}

	public void setAverageFeedback(double averageFeedback) {
		this.averageFeedback = averageFeedback;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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

	public Long getCampusId() {
		return campusId;
	}

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	/**
	 * Non persistent fields
	 */
	private String[] answers;
	/**
	 * 
	 */

	private String courseId;

	private String courseAverage;

	private String comments;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getCourseAverage() {
		return courseAverage;
	}

	public void setCourseAverage(String courseAverage) {
		this.courseAverage = courseAverage;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public StudentFeedbackResponse() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStudentFeedbackId() {
		return studentFeedbackId;
	}

	public void setStudentFeedbackId(String studentFeedbackId) {
		this.studentFeedbackId = studentFeedbackId;
	}

	public Long getFeedbackQuestionId() {
		return feedbackQuestionId;
	}

	public void setFeedbackQuestionId(Long feedbackQuestionId) {
		this.feedbackQuestionId = feedbackQuestionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
		if (null != answer)
			answers = answer.split(",");
	}

	public String[] getAnswers() {
		return answers;
	}

	public void setAnswers(String[] answers) {
		this.answers = answers;
		if (null != answers)
			answer = StringUtils.join(answers, ',');
	}
}
