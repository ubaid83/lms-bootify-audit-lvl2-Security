package com.spts.lms.beans;



public class UsermgmtEvent extends BaseBean {
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	String event_id;
	String course_id;
	String faculty_type;
	String event_desc;
	String event_name;
	String faculty_id;
	String program_id;
	String description;
	String course_name;
	String startDate;
	String endDate;
	String courseId;
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getFaculty_type() {
		return faculty_type;
	}
	public void setFaculty_type(String faculty_type) {
		this.faculty_type = faculty_type;
	}
	public String getEvent_desc() {
		return event_desc;
	}
	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getFaculty_id() {
		return faculty_id;
	}
	public void setFaculty_id(String faculty_id) {
		this.faculty_id = faculty_id;
	}
	public String getProgram_id() {
		return program_id;
	}
	public void setProgram_id(String program_id) {
		this.program_id = program_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}
	@Override
	public String toString() {
		return "UsermgmtEvent [event_id=" + event_id + ", course_id="
				+ course_id + ", faculty_type=" + faculty_type
				+ ", event_desc=" + event_desc + ", event_name=" + event_name
				+ ", faculty_id=" + faculty_id + ", program_id=" + program_id
				+ ", description=" + description + ", course_name="
				+ course_name + ", startDate=" + startDate + ", endDate="
				+ endDate + ", courseId=" + courseId + "]";
	}
	
	
	
}
