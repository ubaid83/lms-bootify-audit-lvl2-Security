package com.spts.lms.beans.assignment;

import com.spts.lms.beans.BaseBean;

public class AssignmentConfiguration extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	private long assignmentId;
	private double marks;
	private String questionNumber;
	private String active;
	
	//assignmentPoolConfiguration
	private long testPoolId;
	private long noOfQuestion;
	
		
	public long getTestPoolId() {
		return testPoolId;
	}
	public void setTestPoolId(long testPoolId) {
		this.testPoolId = testPoolId;
	}
	public long getNoOfQuestion() {
		return noOfQuestion;
	}
	public void setNoOfQuestion(long noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}
	public long getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	public String getQuestionNumber() {
		return questionNumber;
	}
	public void setQuestionNumber(String questionNumber) {
		this.questionNumber = questionNumber;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "AssignmentConfiguration [assignmentId=" + assignmentId + ", marks=" + marks + ", questionNumber="
				+ questionNumber + ", active=" + active + ", testPoolId=" + testPoolId + ", noOfQuestion="
				+ noOfQuestion + "]";
	}
	
	

}
