package com.spts.lms.beans.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.AutoPopulatingList;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

/**
 * The persistent class for the test database table.
 * 
 */
public class Test extends BaseBean {
	private static final long serialVersionUID = 1L;

	private Long score;
	private Long wieghtageassigned;
	private String rollNo;
	private String randomQuestion;
	private String campusName;
	private Long campusId;
	private String sendSmsAlertToParents;
	private String sendEmailAlertToParents;

	private String isPasswordForTest;
	private String passwordForTest;
	private String autoAllocateToStudents;

	private String maxDesQueToShow;
	private String maxMcqQueToShow;
	private String maxRngQueToShow;
	private String maxImgQueToShow;

	private String sameMarksQue;
	private double marksPerQue;
	private String specifyMaxQues;
	private String programId;
	private String studentFilePath;
	private String courseIdToExport;
	private String idForTest;

	private String dbName;
	private String url;
	private String dbUsername;
	private String password;

	private String noOfQuestion;

	private String dbPort;
	private String schoolName;

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getIsCreatedByAdmin() {
		return isCreatedByAdmin;
	}

	public void setIsCreatedByAdmin(String isCreatedByAdmin) {
		this.isCreatedByAdmin = isCreatedByAdmin;
	}

	public String getAssignedFaculty() {
		return assignedFaculty;
	}

	public void setAssignedFaculty(String assignedFaculty) {
		this.assignedFaculty = assignedFaculty;
	}

	private String moduleId;
	private String isCreatedByAdmin;
	private String assignedFaculty;

	private String studentCount;

	private String isPeerFacultyForDemo;
	private String peerFacultiesForDemo;
	private String showReportsToStudents;

	private String encrypted_key;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEncrypted_key() {
		return encrypted_key;
	}

	public void setEncrypted_key(String encrypted_key) {
		this.encrypted_key = encrypted_key;
	}

	public String getShowReportsToStudents() {
		return showReportsToStudents;
	}

	public void setShowReportsToStudents(String showReportsToStudents) {
		this.showReportsToStudents = checkYElseSetN(showReportsToStudents);
	}

	public String getIsPeerFacultyForDemo() {
		return isPeerFacultyForDemo;
	}

	public void setIsPeerFacultyForDemo(String isPeerFacultyForDemo) {
		this.isPeerFacultyForDemo = isPeerFacultyForDemo != null ? isPeerFacultyForDemo : "N";
	}

	public String getPeerFacultiesForDemo() {
		return peerFacultiesForDemo;
	}

	public void setPeerFacultiesForDemo(String peerFacultiesForDemo) {
		this.peerFacultiesForDemo = peerFacultiesForDemo;
	}

	public String getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(String studentCount) {
		this.studentCount = studentCount;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNoOfQuestion() {
		return noOfQuestion;
	}

	public void setNoOfQuestion(String noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getIdForTest() {
		return idForTest;
	}

	public void setIdForTest(String idForTest) {
		this.idForTest = idForTest;
	}

	public String getStudentFilePath() {
		return studentFilePath;
	}

	public void setStudentFilePath(String studentFilePath) {
		this.studentFilePath = studentFilePath;
	}

	public String getCourseIdToExport() {
		return courseIdToExport;
	}

	public void setCourseIdToExport(String courseIdToExport) {
		this.courseIdToExport = courseIdToExport;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getSameMarksQue() {
		return sameMarksQue;
	}

	public void setSameMarksQue(String sameMarksQue) {
		// this.sameMarksQue = sameMarksQue;
		this.sameMarksQue = sameMarksQue != null ? sameMarksQue : "N";
	}

	public double getMarksPerQue() {
		return marksPerQue;
	}

	public void setMarksPerQue(double marksPerQue) {
		this.marksPerQue = marksPerQue;
	}

	public String getSpecifyMaxQues() {
		return specifyMaxQues;
	}

	public void setSpecifyMaxQues(String specifyMaxQues) {
		this.specifyMaxQues = specifyMaxQues;
	}

	public String getMaxDesQueToShow() {
		return maxDesQueToShow;
	}

	public void setMaxDesQueToShow(String maxDesQueToShow) {
		this.maxDesQueToShow = maxDesQueToShow;
	}

	public String getMaxMcqQueToShow() {
		return maxMcqQueToShow;
	}

	public void setMaxMcqQueToShow(String maxMcqQueToShow) {
		this.maxMcqQueToShow = maxMcqQueToShow;
	}

	public String getMaxRngQueToShow() {
		return maxRngQueToShow;
	}

	public void setMaxRngQueToShow(String maxRngQueToShow) {
		this.maxRngQueToShow = maxRngQueToShow;
	}

	public String getMaxImgQueToShow() {
		return maxImgQueToShow;
	}

	public void setMaxImgQueToShow(String maxImgQueToShow) {
		this.maxImgQueToShow = maxImgQueToShow;
	}

	public String getSendSmsAlertToParents() {
		return sendSmsAlertToParents;
	}

	public void setSendSmsAlertToParents(String sendSmsAlertToParents) {
		this.sendSmsAlertToParents = sendSmsAlertToParents != null ? sendSmsAlertToParents : "N";
	}

	public String getSendEmailAlertToParents() {
		return sendEmailAlertToParents;
	}

	public void setSendEmailAlertToParents(String sendEmailAlertToParents) {
		// this.sendEmailAlertToParents = sendEmailAlertToParents;
		this.sendEmailAlertToParents = sendEmailAlertToParents != null ? sendEmailAlertToParents : "N";
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

	public String getRandomQuestion() {
		return randomQuestion;
	}

	/*
	 * public void setRandomQuestion(String randomQuestion) { this.randomQuestion =
	 * randomQuestion; }
	 */

	public void setRandomQuestion(String randomQuestion) {
		this.randomQuestion = randomQuestion != null ? randomQuestion : "N";
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getWieghtageassigned() {
		return wieghtageassigned;
	}

	public void setWieghtageassigned(Long wieghtageassigned) {
		this.wieghtageassigned = wieghtageassigned;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	private String acadMonth;

	private Integer acadYear;

	private String active;

	private String allowAfterEndDate;

	/* private String dueDate; */

	private String endDate;

	private Integer maxAttempt = 1;

	private Integer attempt;

	// Changes By Akshay From Integer To Double

	private double maxScore;

	// End

	private Integer duration;

	private Integer passScore;

	private String testName;

	private String sendEmailAlert;

	private String sendSmsAlert;

	private String showResultsToStudents;

	private String startDate;

	private Long courseId;

	private String facultyId;

	private String maxQuestnToShow;

	String idOfCourse;

	private String groupId;

	private String testCompleted;

	private String completionTime;

	private String testType;

	private String testDescription;

	private static final Logger logger = Logger.getLogger(Test.class);

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Date completionTime, int duration) {

		DateFormat date = new SimpleDateFormat("yyyy-mm-dd");
		int hour = completionTime.getHours();
		int minutes = completionTime.getMinutes();

		String time = hour + ":" + minutes;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date d;
		String newTime = null;
		try {
			d = df.parse(time);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, duration);
			newTime = date.format(completionTime) + " " + df.format(cal.getTime());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

		this.completionTime = newTime;

	}

	public String getMaxQuestnToShow() {
		return maxQuestnToShow;
	}

	public void setMaxQuestnToShow(String maxQuestnToShow) {
		this.maxQuestnToShow = maxQuestnToShow;
	}

	public Integer getAttempt() {
		return attempt;
	}

	public void setAttempt(Integer attempt) {
		this.attempt = attempt;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public String getTestCompleted() {
		return testCompleted;
	}

	public void setTestCompleted(String testCompleted) {
		this.testCompleted = testCompleted;
	}

	public String getIdOfCourse() {
		return idOfCourse;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setIdOfCourse(String idOfCourse) {
		this.idOfCourse = idOfCourse;
	}

	/**
	 * Non persistent fields
	 */
	private Course course = new Course();

	private List<TestQuestion> testQuestions = new AutoPopulatingList<TestQuestion>(TestQuestion.class);

	private StudentTest studentTest = new StudentTest();

	private List<StudentTest> studentTests = new ArrayList<StudentTest>();

	private List<String> students = new ArrayList<String>();

	/**
	 * 
	 */

	private List<String> deallocatedStudents = new ArrayList<String>();

	public Test() {
	}

	public List<String> getDeallocatedStudents() {
		return deallocatedStudents;
	}

	public void setDeallocatedStudents(List<String> deallocatedStudents) {
		this.deallocatedStudents = deallocatedStudents;
		for (String username : deallocatedStudents) {
			StudentTest studentTest = new StudentTest();
			studentTest.setUsername(username);
			studentTest.setTestId(getId());
			studentTests.add(studentTest);
		}
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getAllowAfterEndDate() {
		return allowAfterEndDate;
	}

	public void setAllowAfterEndDate(String allowAfterEndDate) {
		this.allowAfterEndDate = checkYElseSetN(allowAfterEndDate);
		// this.allowAfterEndDate = allowAfterEndDate != null ? allowAfterEndDate : "N";
	}

	/*
	 * public String getDueDate() { return formatDate(dueDate); }
	 * 
	 * public void setDueDate(String dueDate) { this.dueDate = dueDate; }
	 */
	public String getEndDate() {
		return formatDate(endDate);
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getMaxAttempt() {
		return maxAttempt;
	}

	public void setMaxAttempt(Integer maxAttempt) {
		this.maxAttempt = maxAttempt;
	}

	public double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getPassScore() {
		return passScore;
	}

	public void setPassScore(Integer passScore) {
		this.passScore = passScore;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getSendEmailAlert() {
		return sendEmailAlert;
	}

	public void setSendEmailAlert(String sendEmailAlert) {
		this.sendEmailAlert = checkYElseSetN(sendEmailAlert);
		// this.sendEmailAlert = sendEmailAlert != null ? sendEmailAlert : "N";
	}

	public String getSendSmsAlert() {
		return sendSmsAlert;
	}

	public void setSendSmsAlert(String sendSmsAlert) {
		this.sendSmsAlert = checkYElseSetN(sendSmsAlert);
		// this.sendSmsAlert = sendSmsAlert != null ? sendSmsAlert : "N";

	}

	public String getShowResultsToStudents() {
		return showResultsToStudents;
	}

	public void setShowResultsToStudents(String showResultsToStudents) {
		this.showResultsToStudents = checkYElseSetN(showResultsToStudents);
		// this.showResultsToStudents = showResultsToStudents != null ?
		// showResultsToStudents : "N";
	}

	public String getStartDate() {
		return formatDate(startDate);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
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

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
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

	public List<TestQuestion> getTestQuestions() {
		return testQuestions;
	}

	public void setTestQuestions(List<TestQuestion> testQuestions) {
		this.testQuestions = testQuestions;
	}

	public StudentTest getStudentTest() {
		return studentTest;
	}

	public void setStudentTest(StudentTest studentTest) {
		this.studentTest = studentTest;
	}

	public List<StudentTest> getStudentTests() {
		return studentTests;
	}

	public void setStudentTests(List<StudentTest> studentTests) {
		this.studentTests = studentTests;
	}

	public List<String> getStudents() {
		return students;
	}

	public String getIsPasswordForTest() {
		return isPasswordForTest;
	}

	public void setIsPasswordForTest(String isPasswordForTest) {
		this.isPasswordForTest = isPasswordForTest != null ? isPasswordForTest : "N";
	}

	public String getPasswordForTest() {
		return passwordForTest;
	}

	public void setPasswordForTest(String passwordForTest) {
		this.passwordForTest = passwordForTest;
	}

	public String getAutoAllocateToStudents() {
		return autoAllocateToStudents;
	}

	public void setAutoAllocateToStudents(String autoAllocateToStudents) {
		// this.autoAllocateToStudents = autoAllocateToStudents;

		this.autoAllocateToStudents = checkYElseSetN(autoAllocateToStudents);
		// this.autoAllocateToStudents = autoAllocateToStudents != null ?
		// autoAllocateToStudents : "N";
	}

	public void setStudents(List<String> students) {
		this.students = students;
		for (String username : students) {
			StudentTest studentTest = new StudentTest();
			studentTest.setUsername(username);
			studentTest.setTestId(getId());
			studentTests.add(studentTest);
		}
	}

	public Integer getDurationLeft() {
		if (null == duration) {
			logger.info("whfirst----------second----12313123------>>>>");
			return duration;
		}
		if (null == studentTest.getTestStartTime()) {
			logger.info("whent test is not ended and the time reset to 59 minutes--------------------->>>>");
			return duration * 60;
		} else {
			Integer durationLeft = (int) (duration * 60
					- (System.currentTimeMillis() - studentTest.getTestStartTime().getTime()) / 1000);
			logger.info(
					"whent test is not ended and the time reset to 59 minutes-----------second----12313123------>>>>");
			return durationLeft > 0 ? durationLeft : 2;
		}
	}

	@Override
	public String toString() {
		return "Test [score=" + score + ", wieghtageassigned=" + wieghtageassigned + ", rollNo=" + rollNo
				+ ", randomQuestion=" + randomQuestion + ", campusName=" + campusName + ", campusId=" + campusId
				+ ", sendSmsAlertToParents=" + sendSmsAlertToParents + ", sendEmailAlertToParents="
				+ sendEmailAlertToParents + ", isPasswordForTest=" + isPasswordForTest + ", passwordForTest="
				+ passwordForTest + ", autoAllocateToStudents=" + autoAllocateToStudents + ", maxDesQueToShow="
				+ maxDesQueToShow + ", maxMcqQueToShow=" + maxMcqQueToShow + ", maxRngQueToShow=" + maxRngQueToShow
				+ ", maxImgQueToShow=" + maxImgQueToShow + ", sameMarksQue=" + sameMarksQue + ", marksPerQue="
				+ marksPerQue + ", specifyMaxQues=" + specifyMaxQues + ", programId=" + programId + ", studentFilePath="
				+ studentFilePath + ", courseIdToExport=" + courseIdToExport + ", idForTest=" + idForTest + ", dbName="
				+ dbName + ", url=" + url + ", dbUsername=" + dbUsername + ", password=" + password + ", noOfQuestion="
				+ noOfQuestion + ", dbPort=" + dbPort + ", schoolName=" + schoolName + ", moduleId=" + moduleId
				+ ", isCreatedByAdmin=" + isCreatedByAdmin + ", assignedFaculty=" + assignedFaculty + ", studentCount="
				+ studentCount + ", isPeerFacultyForDemo=" + isPeerFacultyForDemo + ", peerFacultiesForDemo="
				+ peerFacultiesForDemo + ", showReportsToStudents=" + showReportsToStudents + ", encrypted_key="
				+ encrypted_key + ", username=" + username + ", acadMonth=" + acadMonth + ", acadYear=" + acadYear
				+ ", active=" + active + ", allowAfterEndDate=" + allowAfterEndDate + ", endDate=" + endDate
				+ ", maxAttempt=" + maxAttempt + ", attempt=" + attempt + ", maxScore=" + maxScore + ", duration="
				+ duration + ", passScore=" + passScore + ", testName=" + testName + ", sendEmailAlert="
				+ sendEmailAlert + ", sendSmsAlert=" + sendSmsAlert + ", showResultsToStudents=" + showResultsToStudents
				+ ", startDate=" + startDate + ", courseId=" + courseId + ", facultyId=" + facultyId
				+ ", maxQuestnToShow=" + maxQuestnToShow + ", idOfCourse=" + idOfCourse + ", groupId=" + groupId
				+ ", testCompleted=" + testCompleted + ", completionTime=" + completionTime + ", testType=" + testType
				+ ", testDescription=" + testDescription + ", course=" + course + ", testQuestions=" + testQuestions
				+ ", studentTest=" + studentTest + ", studentTests=" + studentTests + ", students=" + students
				+ ", deallocatedStudents=" + deallocatedStudents + "]";
	}

}