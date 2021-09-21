package com.spts.lms.beans.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class StudentFeedback extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7958101032182716140L;
	private Integer acadYear;
	private String acadMonth;
	private Long courseId;
	private String startDate;
	/* private String dueDate ; */
	private String endDate;
	private String mandatory;
	private String allowAfterEndDate;
	private String username;
	private String facultyId;
	private Long feedbackId;
	private String feedbackCompleted;
	private String feedbackName;
	private String facultyName;
	private String eachfeedbackCompletionStatus;
	private String courseNameforFeedback;
	private String studentName;
	private Integer noOfStudentsFeedback;
	private String acadSession;
	private String email;
	private String mobile;
	private String rollNo;
	private String type;
	private String programNameForFeedback;
	private String typeOfFaculty;
	private String courseQuestion;
	private String campusName;
	private Long campusId;
	
	private String answer;
	
	private String active;
	private String enabled;
	
	List<String> multipleAnswers = new ArrayList<>();
	
	
	

	public List<String> getMultipleAnswers() {
		return multipleAnswers;
	}

	public void setMultipleAnswers(List<String> multipleAnswers) {
		this.multipleAnswers = multipleAnswers;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
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

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	/**
	 * Non persistent fields
	 */
	@JsonIgnore
	private List<StudentFeedbackResponse> studentFeedbackResponses = new AutoPopulatingList<StudentFeedbackResponse>(
			StudentFeedbackResponse.class);
	@JsonIgnore
	private User user = new User();
	@JsonIgnore
	private Program program = new Program();
	@JsonIgnore
	private Course course = new Course();
	/**
	 * 
	 */

	private String comments;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public StudentFeedback() {

	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getNoOfStudentsFeedback() {
		return noOfStudentsFeedback;
	}

	public void setNoOfStudentsFeedback(Integer noOfStudentsFeedback) {
		this.noOfStudentsFeedback = noOfStudentsFeedback;
	}

	public String getCourseNameforFeedback() {
		return courseNameforFeedback;
	}

	public void setCourseNameforFeedback(String courseNameforFeedback) {
		this.courseNameforFeedback = courseNameforFeedback;
	}

	public String getEachfeedbackCompletionStatus() {
		return eachfeedbackCompletionStatus;
	}

	public void setEachfeedbackCompletionStatus(
			String eachfeedbackCompletionStatus) {
		this.eachfeedbackCompletionStatus = eachfeedbackCompletionStatus;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getStudentName() {
		return studentName;
	}

	
	public String getProgramNameForFeedback() {
		return programNameForFeedback;
	}

	public void setProgramNameForFeedback(String programNameForFeedback) {
		this.programNameForFeedback = programNameForFeedback;
	}

	public String getTypeOfFaculty() {
		return typeOfFaculty;
	}

	public void setTypeOfFaculty(String typeOfFaculty) {
		this.typeOfFaculty = typeOfFaculty;
	}

	public String getCourseQuestion() {
		return courseQuestion;
	}

	public void setCourseQuestion(String courseQuestion) {
		this.courseQuestion = courseQuestion;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getFeedbackName() {
		return feedbackName;
	}

	public void setFeedbackName(String feedbackName) {
		this.feedbackName = feedbackName;
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
		if (null != course)
			return course.getId();
		return courseId;
	}

	public void setCourseId(Long courseId) {
		if (null != course)
			course.setId(courseId);
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public Long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getFeedbackCompleted() {
		return feedbackCompleted;
	}

	public void setFeedbackCompleted(String feedbackCompleted) {
		this.feedbackCompleted = feedbackCompleted;
	}

	public List<StudentFeedbackResponse> getStudentFeedbackResponses() {
		return studentFeedbackResponses;
	}

	public void setStudentFeedbackResponses(
			List<StudentFeedbackResponse> studentFeedbackResponses) {
		this.studentFeedbackResponses = studentFeedbackResponses;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public boolean isCompleted() {
		return "Y".equals(feedbackCompleted);
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

	public String getProgramName() {
		return program.getProgramName();
	}

	public void setProgramName(String programName) {
		program.setProgramName(programName);
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getCourseName() {
		return course.getCourseName();
	}

	public void setCourseName(String courseName) {
		course.setCourseName(courseName);
	}

	@Override
	public String toString() {
		return "StudentFeedback [acadYear=" + acadYear + ", acadMonth=" + acadMonth + ", courseId=" + courseId
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", mandatory=" + mandatory
				+ ", allowAfterEndDate=" + allowAfterEndDate + ", username=" + username + ", facultyId=" + facultyId
				+ ", feedbackId=" + feedbackId + ", feedbackCompleted=" + feedbackCompleted + ", feedbackName="
				+ feedbackName + ", facultyName=" + facultyName + ", eachfeedbackCompletionStatus="
				+ eachfeedbackCompletionStatus + ", courseNameforFeedback=" + courseNameforFeedback + ", studentName="
				+ studentName + ", noOfStudentsFeedback=" + noOfStudentsFeedback + ", acadSession=" + acadSession
				+ ", email=" + email + ", mobile=" + mobile + ", rollNo=" + rollNo + ", type=" + type
				+ ", programNameForFeedback=" + programNameForFeedback + ", typeOfFaculty=" + typeOfFaculty
				+ ", courseQuestion=" + courseQuestion + ", campusName=" + campusName + ", campusId=" + campusId
				+ ", answer=" + answer + ", active=" + active + ", enabled=" + enabled + ", multipleAnswers="
				+ multipleAnswers + ", studentFeedbackResponses=" + studentFeedbackResponses + ", user=" + user
				+ ", program=" + program + ", course=" + course + ", comments=" + comments + "]";
	}



}
