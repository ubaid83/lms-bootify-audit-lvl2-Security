package com.spts.lms.beans.feedback;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.user.User;

public class FeedbackQuestion extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5336696966997014348L;
	private Long feedbackId ;
    private String description ;
    private String type ;
    private String option1 ;
    private String option2 ;
    private String option3 ;
    private String option4 ;
    private String option5 ;
    private String option6 ;
    private String option7 ;
    private String option8 ;
    private String option9 ;
    private String rollNo;
    private String campusName;
	private Long campusId;
	
	
	private String acadSession;
	private String acadYear;
	private String programId;
	
	

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
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

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
    public List<String> getCourseIds() {
		return courseIds;
	}
	public void setCourseIds(List<String> courseIds) {
		this.courseIds = courseIds;
	}

	private String option10 ;
    private String active ;
    private Long stuFeedbackId;
    private List<String> courseIds;

    

	@Override
	public String toString() {
		return "FeedbackQuestion [feedbackId=" + feedbackId + ", description=" + description + ", type=" + type
				+ ", option1=" + option1 + ", option2=" + option2 + ", option3=" + option3 + ", option4=" + option4
				+ ", option5=" + option5 + ", option6=" + option6 + ", option7=" + option7 + ", option8=" + option8
				+ ", option9=" + option9 + ", rollNo=" + rollNo + ", campusName=" + campusName + ", campusId="
				+ campusId + ", acadSession=" + acadSession + ", acadYear=" + acadYear + ", programId=" + programId
				+ ", option10=" + option10 + ", active=" + active + ", stuFeedbackId=" + stuFeedbackId + ", courseIds="
				+ courseIds + ", studentFeedbackResponse=" + studentFeedbackResponse + ", studentFeedback="
				+ studentFeedback + ", studentFeedbackResponses=" + studentFeedbackResponses + ", courseName="
				+ courseName + ", facultyName=" + facultyName + ", user=" + user + ", courseId=" + courseId + "]";
	}

	/**
	 * Non persistent fields
	 */
    @JsonIgnore
	private StudentFeedbackResponse studentFeedbackResponse = new StudentFeedbackResponse();
    
    @JsonIgnore
   	private StudentFeedback studentFeedback = new StudentFeedback();
    
    private List<StudentFeedbackResponse> studentFeedbackResponses = new ArrayList<>();
    
    private String courseName;
    
    private String facultyName;
    
	
	public String getFacultyName() {
		return facultyName;
	}
	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}
	public Long getStuFeedbackId() {
		return stuFeedbackId;
	}
	public void setStuFeedbackId(Long stuFeedbackId) {
		this.stuFeedbackId = stuFeedbackId;
	}
	public StudentFeedback getStudentFeedback() {
		return studentFeedback;
	}
	public void setStudentFeedback(StudentFeedback studentFeedback) {
		this.studentFeedback = studentFeedback;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public void setStudentFeedbackResponse(
			StudentFeedbackResponse studentFeedbackResponse) {
		this.studentFeedbackResponse = studentFeedbackResponse;
	}
	public List<StudentFeedbackResponse> getStudentFeedbackResponses() {
		return studentFeedbackResponses;
	}
	public void setStudentFeedbackResponses(
			List<StudentFeedbackResponse> studentFeedbackResponses) {
		this.studentFeedbackResponses = studentFeedbackResponses;
	}

	@JsonIgnore
	private User user = new User();
	/**
	 * 
	 */
    
	private Long courseId;

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
    public FeedbackQuestion() {
    	
    }
	public Long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public String getOption4() {
		return option4;
	}
	public void setOption4(String option4) {
		this.option4 = option4;
	}
	public String getOption5() {
		return option5;
	}
	public void setOption5(String option5) {
		this.option5 = option5;
	}
	public String getOption6() {
		return option6;
	}
	public void setOption6(String option6) {
		this.option6 = option6;
	}
	public String getOption7() {
		return option7;
	}
	public void setOption7(String option7) {
		this.option7 = option7;
	}
	public String getOption8() {
		return option8;
	}
	public void setOption8(String option8) {
		this.option8 = option8;
	}
	public String getOption9() {
		return option9;
	}
	public void setOption9(String option9) {
		this.option9 = option9;
	}
	public String getOption10() {
		return option10;
	}
	public void setOption10(String option10) {
		this.option10= option10;
	}
	public String getActive() {
		return active;
	}
	
	public void setActive(String active) {
		this.active = active;
	}
	
	public StudentFeedbackResponse getStudentFeedbackResponse() {
		return studentFeedbackResponse;
	}

	public void setStudentFeedbacknResponse(StudentFeedbackResponse studentFeedbackResponse) {
		this.studentFeedbackResponse = studentFeedbackResponse;
		if(null != studentFeedbackResponse)
			this.studentFeedbackResponse.setFeedbackQuestionId(getId());
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getUsername() {
		return studentFeedbackResponse.getUsername();
	}
	
	public void setUsername(String username) {
		studentFeedbackResponse.setUsername(username);
	}

	public String getAnswer() {
		return studentFeedbackResponse.getAnswer();
	}

	public void setAnswer(String answer) {
		studentFeedbackResponse.setAnswer(answer);
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
	
	public String[] getAnswers() {
		return studentFeedbackResponse.getAnswers();
	}

	public void setAnswers(String[] answers) {
		studentFeedbackResponse.setAnswers(answers);
	}

	public String getStudentFeedbackId() {
		return studentFeedbackResponse.getStudentFeedbackId();
	}

	public void setStudentFeedbackId(String studentFeedbackId) {
		studentFeedbackResponse.setStudentFeedbackId(studentFeedbackId);
	}
}
