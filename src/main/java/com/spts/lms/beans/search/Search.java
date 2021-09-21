package com.spts.lms.beans.search;

public class Search {
	
	String searchType;
	
	String status;
	
	String course;
	
	Long courseId;
	
	String courseName;
	
	

	@Override
	public String toString() {
		return "Search [searchType=" + searchType + ", status=" + status
				+ ", course=" + course + ", courseId=" + courseId
				+ ", courseName=" + courseName + "]";
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	
	
	
	
	
	
	

}
