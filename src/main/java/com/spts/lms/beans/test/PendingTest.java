package com.spts.lms.beans.test;

import java.util.Date;

public class PendingTest {

	String testName;
	String testId;
	/*String dueDate;*/
	Long courseId;
	Date startDate;
	Date endDate;
	String testType;
	String isPasswordForTest;

	
	
	public String getIsPasswordForTest() {
		return isPasswordForTest;
	}

	public void setIsPasswordForTest(String isPasswordForTest) {
		this.isPasswordForTest = isPasswordForTest;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	Integer count;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}



}
