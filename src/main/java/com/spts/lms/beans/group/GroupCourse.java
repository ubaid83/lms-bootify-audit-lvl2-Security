package com.spts.lms.beans.group;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class GroupCourse extends BaseBean implements Serializable{

	private Long groupId;
	private String courseId;
	private String active;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getCourseId() {
		return courseId;
	}
	@Override
	public String toString() {
		return "GroupCourse [groupId=" + groupId + ", courseId=" + courseId
				+ ", active=" + active + "]";
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
}
