package com.spts.lms.beans.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.spts.lms.beans.BaseBean;

public class Feedback extends BaseBean {

	private static final long serialVersionUID = 5764815969076655532L;
	private String active;
	private String feedbackName;

	/**
	 * Non persistent fields
	 */
	private Integer acadYear;
	private String acadMonth;
	private Long courseId;
	private String startDate;
	/* private String dueDate ; */
	private String endDate;
	private String mandatory;
	private String allowAfterEndDate;
	private String facultyId;
	private String acadSession;
	private String rollNo;
	List<String> courseIds;
	private String campusName;
	private Long campusId;
	private String createdOn;
	private String isPublished;

	private String courseName;

	private String facultyName;
	private String programName;
	
	private String programId;
	private String feedbackType;
	
	
	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(String isPublished) {
		this.isPublished = isPublished;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	@Override
	public String toString() {
		return "Feedback [active=" + active + ", feedbackName=" + feedbackName
				+ ", acadYear=" + acadYear + ", acadMonth=" + acadMonth
				+ ", courseId=" + courseId + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", mandatory=" + mandatory
				+ ", allowAfterEndDate=" + allowAfterEndDate + ", facultyId="
				+ facultyId + ", acadSession=" + acadSession + ", rollNo="
				+ rollNo + ", courseIds=" + courseIds + ", campusName="
				+ campusName + ", campusId=" + campusId + ", createdOn="
				+ createdOn + ", isPublished=" + isPublished
				+ ", feedbackQuestions=" + feedbackQuestions
				+ ", studentFeedback=" + studentFeedback
				+ ", studentFeedbacks=" + studentFeedbacks + ", students="
				+ students + "]";
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public List<String> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(List<String> courseIds) {
		this.courseIds = courseIds;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	private List<FeedbackQuestion> feedbackQuestions = new AutoPopulatingList<FeedbackQuestion>(
			FeedbackQuestion.class);

	private StudentFeedback studentFeedback = new StudentFeedback();

	private List<StudentFeedback> studentFeedbacks = new AutoPopulatingList<StudentFeedback>(
			StudentFeedback.class);

	private List<String> students = new ArrayList<String>();

	/**
	 * 
	 */

	public Feedback() {

	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getFeedbackName() {
		return feedbackName;
	}

	public void setFeedbackName(String feedbackName) {
		this.feedbackName = feedbackName;
	}

	public List<FeedbackQuestion> getFeedbackQuestions() {
		return feedbackQuestions;
	}

	public void setFeedbackQuestions(List<FeedbackQuestion> feedbackQuestions) {
		this.feedbackQuestions = feedbackQuestions;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public StudentFeedback getStudentFeedback() {
		return studentFeedback;
	}

	public void setStudentFeedback(StudentFeedback studentFeedback) {
		this.studentFeedback = studentFeedback;
	}

	public List<StudentFeedback> getStudentFeedbacks() {
		return studentFeedbacks;
	}

	public void setStudentFeedbacks(List<StudentFeedback> studentFeedbacks) {
		this.studentFeedbacks = studentFeedbacks;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
		for (String username : students) {
			StudentFeedback studentFeedback = new StudentFeedback();
			studentFeedback.setUsername(username);
			studentFeedback.setFeedbackId(getId());
			studentFeedback.setAcadMonth(acadMonth);
			studentFeedback.setAcadYear(acadYear);
			studentFeedback.setAllowAfterEndDate(allowAfterEndDate);
			studentFeedback.setCourseId(courseId);
			studentFeedback.setStartDate(startDate);

			studentFeedback.setEndDate(endDate);
			studentFeedback.setFacultyId(facultyId);
			studentFeedback.setMandatory(mandatory);
			studentFeedbacks.add(studentFeedback);
		}
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getAllowAfterEndDate() {
		return allowAfterEndDate;
	}

	public void setAllowAfterEndDate(String allowAfterEndDate) {
		this.allowAfterEndDate = allowAfterEndDate;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

}
