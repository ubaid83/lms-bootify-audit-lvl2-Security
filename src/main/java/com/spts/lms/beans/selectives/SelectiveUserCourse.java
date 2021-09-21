package com.spts.lms.beans.selectives;

import com.spts.lms.beans.BaseBean;

public class SelectiveUserCourse extends BaseBean{
	
	private String username;
	private String course_id;
	private String active;
	private String eventId;
	
	private String course_name;
	
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "SelectiveUserCourse [username=" + username + ", course_id=" + course_id + ", active=" + active
				+ ", eventId=" + eventId + ", course_name=" + course_name + "]";
	}
	
	

}
