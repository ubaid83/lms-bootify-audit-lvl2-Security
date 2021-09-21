package com.spts.lms.beans.test;

import com.spts.lms.beans.BaseBean;

public class TestConfiguration extends BaseBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long testId;
	private double marks;
	private int noOfQuestion;
	
	
	
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
	
	
	
}
