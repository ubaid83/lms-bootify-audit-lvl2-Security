package com.spts.lms.beans.feedback;

public class StudentFeedbackRecord {
	private String programId;
	private String trimester;
	private String facultyName;
	private String coreVisitingFaculty;
	private String area;
	private String courseName;
	private String totalNoOfStudents;
	private String totalNoOfStudentGaveFeedback;
	private String course;
	private String average;
	private String term;
	private String grandAverage;
	private String rollNo;
	private String campusName;
	private Long campusId;

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public Long getCampusId() {
		return campusId;
	}

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getTrimester() {
		return trimester;
	}
	public void setTrimester(String trimester) {
		this.trimester = trimester;
	}
	public String getFacultyName() {
		return facultyName;
	}
	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}
	public String getCoreVisitingFaculty() {
		return coreVisitingFaculty;
	}
	public void setCoreVisitingFaculty(String coreVisitingFaculty) {
		this.coreVisitingFaculty = coreVisitingFaculty;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTotalNoOfStudents() {
		return totalNoOfStudents;
	}
	public void setTotalNoOfStudents(String totalNoOfStudents) {
		this.totalNoOfStudents = totalNoOfStudents;
	}
	public String getTotalNoOfStudentGaveFeedback() {
		return totalNoOfStudentGaveFeedback;
	}
	public void setTotalNoOfStudentGaveFeedback(String totalNoOfStudentGaveFeedback) {
		this.totalNoOfStudentGaveFeedback = totalNoOfStudentGaveFeedback;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getAverage() {
		return average;
	}
	public void setAverage(String average) {
		this.average = average;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getGrandAverage() {
		return grandAverage;
	}
	public void setGrandAverage(String grandAverage) {
		this.grandAverage = grandAverage;
	}
	
	
	

}
