package com.spts.lms.beans.selectives;

import com.spts.lms.beans.BaseBean;

public class SelectiveCourses extends BaseBean {

	private String selective_id;
	private String course_name;
	private String active;
	
	public String getSelective_id() {
		return selective_id;
	}
	public void setSelective_id(String selective_id) {
		this.selective_id = selective_id;
	}

	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "SelectiveCourses [selective_id=" + selective_id + ", course_name=" + course_name + ", active=" + active
				+ "]";
	}
	
}
