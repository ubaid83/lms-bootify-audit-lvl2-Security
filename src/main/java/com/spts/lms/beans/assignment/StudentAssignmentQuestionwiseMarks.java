package com.spts.lms.beans.assignment;

import com.spts.lms.beans.BaseBean;

public class StudentAssignmentQuestionwiseMarks extends BaseBean{

	private static final long serialVersionUID = 1L;
	
	public String username;
	public long assignmentId;
	public long assignConfigId;
	public double marks;
	public String active;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}
	public long getAssignConfigId() {
		return assignConfigId;
	}
	public void setAssignConfigId(long assignConfigId) {
		this.assignConfigId = assignConfigId;
	}
	public double getMarks() {
		return marks;
	}
	public void setMarks(double marks) {
		this.marks = marks;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "StudentAssignmentQuestionwiseMarks [username=" + username + ", assignmentId=" + assignmentId
				+ ", assignConfigId=" + assignConfigId + ", marks=" + marks + ", active=" + active + "]";
	}
	
	

}
