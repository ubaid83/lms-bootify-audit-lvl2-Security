package com.spts.lms.beans.test;

import java.util.Date;
import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

/**
 * The persistent class for the student_test database table.
 * 
 */
public class StudentTest extends BaseBean {
	private static final long serialVersionUID = 1L;
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";

	private Integer attempt;

	private String testCompleted;

	private Date testEndTime;

	private Date testStartTime;

	private Double score;

	private Long testId;

	private String testName;

	private String groupId;

	private String rollNo;

	private String sendSmsAlertToParents;
	private String sendEmailAlertToParents;
	
	private int totalQuestionAttempted;
	private int totalQuestions;
	private String durationCompleted;
	
	private String studentQRespFilePath;
	
	private String stringscore;
	private String unscored;
	
	private String 	testType;
	
	
	
	
	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getStudentQRespFilePath() {
		return studentQRespFilePath;
	}

	public void setStudentQRespFilePath(String studentQRespFilePath) {
		this.studentQRespFilePath = studentQRespFilePath;
	}

	public String getStringscore() {
		return stringscore;
	}

	public void setStringscore(String stringscore) {
		this.stringscore = stringscore;
	}

	public String getUnscored() {
		return unscored;
	}

	public void setUnscored(String unscored) {
		this.unscored = unscored;
	}

	public String getDurationCompleted() {
		return durationCompleted;
	}

	public void setDurationCompleted(String durationCompleted) {
		this.durationCompleted = durationCompleted;
	}

	private Double updatedScore;
	
	
	
	
	
	public Double getUpdatedScore() {
		return updatedScore;
	}

	public void setUpdatedScore(Double updatedScore) {
		this.updatedScore = updatedScore;
	}

	public int getTotalQuestionAttempted() {
		return totalQuestionAttempted;
	}

	public void setTotalQuestionAttempted(int totalQuestionAttempted) {
		this.totalQuestionAttempted = totalQuestionAttempted;
	}

	public int getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public String getSendSmsAlertToParents() {
		return sendSmsAlertToParents;
	}

	public void setSendSmsAlertToParents(String sendSmsAlertToParents) {
		this.sendSmsAlertToParents = sendSmsAlertToParents;
	}

	public String getSendEmailAlertToParents() {
		return sendEmailAlertToParents;
	}

	public void setSendEmailAlertToParents(String sendEmailAlertToParents) {
		this.sendEmailAlertToParents = sendEmailAlertToParents;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getPass() {
		return PASS;
	}

	public static String getFail() {
		return FAIL;
	}

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

	private String courseId;
	private String status;
	String courseName;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
	public String toString() {
		return "StudentTest [attempt=" + attempt + ", testCompleted=" + testCompleted + ", testEndTime=" + testEndTime
				+ ", testStartTime=" + testStartTime + ", score=" + score + ", testId=" + testId + ", testName="
				+ testName + ", groupId=" + groupId + ", rollNo=" + rollNo + ", sendSmsAlertToParents="
				+ sendSmsAlertToParents + ", sendEmailAlertToParents=" + sendEmailAlertToParents
				+ ", totalQuestionAttempted=" + totalQuestionAttempted + ", totalQuestions=" + totalQuestions
				+ ", durationCompleted=" + durationCompleted + ", studentQRespFilePath=" + studentQRespFilePath
				+ ", stringscore=" + stringscore + ", unscored=" + unscored + ", testType=" + testType
				+ ", updatedScore=" + updatedScore + ", campusName=" + campusName + ", campusId=" + campusId
				+ ", courseId=" + courseId + ", status=" + status + ", courseName=" + courseName + ", username="
				+ username + ", startDate=" + startDate + ", endDate=" + endDate + ", passScore=" + passScore
				+ ", maxScore=" + maxScore + ", studentQuestionResponses=" + studentQuestionResponses + ", user=" + user
				+ ", course=" + course + ", program=" + program + ", passed=" + passed + ", failed=" + failed
				+ ", pending=" + pending + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	private String username;

	private String startDate;

	private String endDate;

	private Integer passScore;

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

	public Integer getPassScore() {
		return passScore;
	}

	public void setPassScore(Integer passScore) {
		this.passScore = passScore;
	}

	public Integer getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Integer maxScore) {
		this.maxScore = maxScore;
	}

	private Integer maxScore;

	/**
	 * Non persistent fields
	 */
	private List<StudentQuestionResponse> studentQuestionResponses = new AutoPopulatingList<StudentQuestionResponse>(
			StudentQuestionResponse.class);

	@JsonIgnore
	private User user = new User();

	@JsonIgnore
	private Course course = new Course();

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@JsonIgnore
	private Program program = new Program();

	/**
	 * 
	 */

	public StudentTest() {
	}

	public Integer getAttempt() {
		return attempt;
	}

	public void setAttempt(Integer attempt) {
		this.attempt = attempt;
	}

	public String getTestCompleted() {
		return testCompleted;
	}

	public void setTestCompleted(String testCompleted) {
		this.testCompleted = testCompleted;
	}

	public Date getTestEndTime() {
		return testEndTime;
	}

	public void setTestEndTime(Date testEndTime) {
		this.testEndTime = testEndTime;
	}

	public Date getTestStartTime() {
		return testStartTime;
	}

	public void setTestStartTime(Date testStartTime) {

		this.testStartTime = testStartTime;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double totalScore) {
		this.score = totalScore;
	}

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<StudentQuestionResponse> getStudentQuestionResponses() {
		return studentQuestionResponses;
	}

	public void setStudentQuestionResponses(
			List<StudentQuestionResponse> studentQuestionResponses) {
		this.studentQuestionResponses = studentQuestionResponses;
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
		return "Y".equals(testCompleted);
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
	
	private int passed;
	private int failed;
	private int pending;




	public int getPassed() {
		return passed;
	}

	public void setPassed(int passed) {
		this.passed = passed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public int getPending() {
		return pending;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}
	
}