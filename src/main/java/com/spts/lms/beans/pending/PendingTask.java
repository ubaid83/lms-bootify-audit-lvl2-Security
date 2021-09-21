package com.spts.lms.beans.pending;

import java.util.Date;

public class PendingTask {

	Date startDate;
	Date endDate;

	String testType;
	
	String isPasswordForTest;
	String courseId;
	
	
	
	public String getIsPasswordForTest() {
		return isPasswordForTest;
	}

	public void setIsPasswordForTest(String isPasswordForTest) {
		this.isPasswordForTest = isPasswordForTest;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
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
	
	

	public void setUrl(String url) {
		this.url = url;
	}

	String taskName;
	String type;
	String dueDate;
	String level;
	String id;
	String url;
	

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
