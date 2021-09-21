package com.spts.lms.beans.calender;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.User;

public class StudentEvent extends BaseBean {
	

	
	
	private static final long serialVersionUID = 1L;
	private String eventId;
	private String username;
	private String user_email;
	private String courseId;
	
	private User user = new User();
	private Course course = new Course();
	
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getFirstname() {
		return user.getFirstname();
	}

	public void setFirstname(String firstname) {
		user.setFirstname(firstname);
	}

	public String getLastname() {
		return user.getLastname();
	}

	public void setLastname(String lastname) {
		user.setLastname(lastname);
	}
	
	public String getEmail() {
		return user.getEmail();
	}

	public void setEmail(String email) {
		user.setEmail(email);
	}
	
	
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	@Override
	public String toString() {
		return "StudentEvent [eventId=" + eventId + ", username=" + username
				+ ", user_email=" + user_email + ", courseId=" + courseId + "]";
	}
	
	
	

}
