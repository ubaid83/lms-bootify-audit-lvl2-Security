package com.spts.lms.beans.test;

import com.spts.lms.beans.BaseBean;

public class TestPoolConfiguration extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long testPoolId;
	private long testId;
	private double marks;
	private int noOfQuestion;
	
	
	public long getTestPoolId() {
		return testPoolId;
	}
	public void setTestPoolId(long testPoolId) {
		this.testPoolId = testPoolId;
	}
	public long getTestId() {
		return testId;
	}
	public void setTestId(long testId) {
		this.testId = testId;
	}
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	public int getNoOfQuestion() {
		return noOfQuestion;
	}
	public void setNoOfQuestion(int noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}
	@Override
	public String toString() {
		return "TestPoolConfiguration [testPoolId=" + testPoolId + ", testId="
				+ testId + ", marks=" + marks + ", noOfQuestion="
				+ noOfQuestion + "]";
	}
	
	
}
