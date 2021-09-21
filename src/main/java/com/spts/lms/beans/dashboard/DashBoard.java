package com.spts.lms.beans.dashboard;

import com.spts.lms.beans.course.Course;

public class DashBoard {
	Course course;
	int pendingAssigmentCount;
	int pendingTestCount;
	int announcementCount;
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public int getPendingAssigmentCount() {
		return pendingAssigmentCount;
	}
	public void setPendingAssigmentCount(int pendingAssigmentCount) {
		this.pendingAssigmentCount = pendingAssigmentCount;
	}
	public int getPendingTestCount() {
		return pendingTestCount;
	}
	public void setPendingTestCount(int pendingTestCount) {
		this.pendingTestCount = pendingTestCount;
	}
	public int getAnnouncementCount() {
		return announcementCount;
	}
	public void setAnnouncementCount(int announcementCount) {
		this.announcementCount = announcementCount;
	}
	

}
