package com.spts.lms.beans.assignment;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class StudentAssignmentQuestion extends BaseBean implements Serializable {
	
	private static final long serialVersionUID = 2748251831795725278L;
	
	private String username;
	private String studentAssignmentId;
	private String questionId;
	private String description;
	private String marks;
	private String questionType;
	private String testQuestionPoolId;
	private String assignmentId;
	private String active;
	
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getTestQuestionPoolId() {
		return testQuestionPoolId;
	}
	public void setTestQuestionPoolId(String testQuestionPoolId) {
		this.testQuestionPoolId = testQuestionPoolId;
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStudentAssignmentId() {
		return studentAssignmentId;
	}
	public void setStudentAssignmentId(String studentAssignmentId) {
		this.studentAssignmentId = studentAssignmentId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	@Override
	public String toString() {
		return "StudentAssignmentQuestion [username=" + username + ", studentAssignmentId=" + studentAssignmentId
				+ ", questionId=" + questionId + ", description=" + description + ", marks=" + marks + ", questionType="
				+ questionType + ", testQuestionPoolId=" + testQuestionPoolId + ", assignmentId=" + assignmentId
				+ ", active=" + active + "]";
	}
	
	

}
