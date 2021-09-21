package com.spts.lms.beans.assignment;

import com.spts.lms.beans.BaseBean;

public class AssignmentQuestion extends BaseBean{
	
	private String assignmentId;
	private String description;
	private String marks;
	private String questionType;
	private String testQuestionPoolId;
	private String active;
	private String testPoolId;
	private String noOfQuestion;
	
	
	
	public String getNoOfQuestion() {
		return noOfQuestion;
	}
	public void setNoOfQuestion(String noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}
	public String getTestPoolId() {
		return testPoolId;
	}
	public void setTestPoolId(String testPoolId) {
		this.testPoolId = testPoolId;
	}
	public String getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(String assignmentId) {
		this.assignmentId = assignmentId;
	}
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
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "AssignmentQuestion [assignmentId=" + assignmentId + ", description=" + description + ", marks=" + marks
				+ ", questionType=" + questionType + ", testQuestionPoolId=" + testQuestionPoolId + ", active=" + active
				+ ", testPoolId=" + testPoolId + ", noOfQuestion=" + noOfQuestion + "]";
	}
	
	
	
	

}
