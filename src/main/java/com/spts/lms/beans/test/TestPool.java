package com.spts.lms.beans.test;

import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class TestPool extends BaseBean{
	
	private String testPoolName;
	private String courseId;
	private String active;
	
	private String courseName;
	private String acadMonth;
	private String acadYear;
	private String acadSession;
	private String testId;
	
	private String programId;
	
	private String courseIdToExport;
	private String marks;
	
	private String moduleId;
	private String isCreatedByAdmin;
	
	private long noOfQuestion;
	private long assignmentId;
	
	public long getNoOfQuestion() {
		return noOfQuestion;
	}
	public void setNoOfQuestion(long noOfQuestion) {
		this.noOfQuestion = noOfQuestion;
	}
	public long getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}
	
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
	public String getMarks() {
		return marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getCourseIdToExport() {
		return courseIdToExport;
	}
	public void setCourseIdToExport(String courseIdToExport) {
		this.courseIdToExport = courseIdToExport;
	}
	private List<String> testQuestionsPoolIds = new ArrayList<String>();
	private List<TestQuestionPools> testQuestionPools = new ArrayList<TestQuestionPools>();
	
	
	
	public List<String> getTestQuestionsPoolIds() {
		return testQuestionsPoolIds;
	}
	public void setTestQuestionsPoolIds(List<String> testQuestionsPoolIds) {
		this.testQuestionsPoolIds = testQuestionsPoolIds;
	}
	public List<TestQuestionPools> getTestQuestionPools() {
		return testQuestionPools;
	}
	public void setTestQuestionPools(List<TestQuestionPools> testQuestionPools) {
		this.testQuestionPools = testQuestionPools;
	}
	public List<String> getTestQuestions() {
		return testQuestionsPoolIds;
	}
	public void setTestQuestions(List<String> testQuestionsPoolIds) {
		this.testQuestionsPoolIds = testQuestionsPoolIds;
		
		for (String Id : testQuestionsPoolIds) {
			TestQuestionPools testQuestion = new TestQuestionPools();
			
			testQuestion.setId(Long.valueOf(Id));
			testQuestionPools.add(testQuestion);
		}
	}
	public String getTestId() {
		return testId;
	}
	public void setTestId(String testId) {
		this.testId = testId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getAcadMonth() {
		return acadMonth;
	}
	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}
	public String getAcadYear() {
		return acadYear;
	}
	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}
	public String getAcadSession() {
		return acadSession;
	}
	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}
	public String getTestPoolName() {
		return testPoolName;
	}
	public void setTestPoolName(String testPoolName) {
		this.testPoolName = testPoolName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "TestPool [testPoolName=" + testPoolName + ", courseId=" + courseId + ", active=" + active
				+ ", courseName=" + courseName + ", acadMonth=" + acadMonth + ", acadYear=" + acadYear
				+ ", acadSession=" + acadSession + ", testId=" + testId + ", programId=" + programId
				+ ", courseIdToExport=" + courseIdToExport + ", marks=" + marks + ", moduleId=" + moduleId
				+ ", isCreatedByAdmin=" + isCreatedByAdmin + ", testQuestionsPoolIds=" + testQuestionsPoolIds
				+ ", testQuestionPools=" + testQuestionPools + "]";
	}
	
	

}
