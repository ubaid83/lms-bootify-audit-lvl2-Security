package com.spts.lms.beans.course;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.test.Test;

/**
 * The persistent class for the course database table.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String abbr;
	private String active;
	private String courseName;
	private String property1;
	private String property2;
	private String property3;
	private String dept;
	String acadMonth;
	String acadYear;
	private String acadYearCode;
	Long programId;
	private String acadSession;
	private Test test;
	private Assignment assignment;
	private Announcement announcement;
	private String testName;
	private String startDate;
	private String endDate;
	private String assignmentName;
	private String subject;
	private String internal;
	private String external;
	private String campusName;
	private Long campusId;

	private String programName;
	private String moduleCategoryName;
	
	private String moduleName;
	
	private String classDate;
	
	private String moduleId;
	private String moduleAbbr;
	private String deptCode;
	private String moduleCategoryCode;
	private String count;
	private String isContinuousLecture;
    private String isAttendanceAllowed;
    private String courseId;
	
	private String programIds;
	
	
	
	
	public String getProgramIds() {
		return programIds;
	}

	public void setProgramIds(String programIds) {
		this.programIds = programIds;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getIsContinuousLecture() {
		return isContinuousLecture;
	}

	public void setIsContinuousLecture(String isContinuousLecture) {
		this.isContinuousLecture = isContinuousLecture;
	}

	public String getIsAttendanceAllowed() {
		return isAttendanceAllowed;
	}

	public void setIsAttendanceAllowed(String isAttendanceAllowed) {
		this.isAttendanceAllowed = isAttendanceAllowed;
	}

	public String getAcadYearCode() {
		return acadYearCode;
	}

	public void setAcadYearCode(String acadYearCode) {
		this.acadYearCode = acadYearCode;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getModuleCategoryCode() {
		return moduleCategoryCode;
	}

	public void setModuleCategoryCode(String moduleCategoryCode) {
		this.moduleCategoryCode = moduleCategoryCode;
	}

	public String getModuleAbbr() {
		return moduleAbbr;
	}

	public void setModuleAbbr(String moduleAbbr) {
		this.moduleAbbr = moduleAbbr;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getClassDate() {
		return classDate;
	}

	public void setClassDate(String classDate) {
		this.classDate = classDate;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleCategoryName() {
		return moduleCategoryName;
	}

	public void setModuleCategoryName(String moduleCategoryName) {
		this.moduleCategoryName = moduleCategoryName;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
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

	
	
	
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getInternal() {
		return internal;
	}

	public void setInternal(String internal) {
		this.internal = internal;
	}

	public String getExternal() {
		return external;
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public Assignment getAssignment() {
		return assignment;
	}

	public void setAssignment(Assignment assignment) {
		this.assignment = assignment;
	}

	public Announcement getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(Announcement announcement) {
		this.announcement = announcement;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
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

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	private String operation;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	private String eventName;

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
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

	@Override
	public String toString() {
		return "Course [abbr=" + abbr + ", active=" + active + ", courseName=" + courseName + ", property1=" + property1
				+ ", property2=" + property2 + ", property3=" + property3 + ", dept=" + dept + ", acadMonth="
				+ acadMonth + ", acadYear=" + acadYear + ", acadYearCode=" + acadYearCode + ", programId=" + programId
				+ ", acadSession=" + acadSession + ", test=" + test + ", assignment=" + assignment + ", announcement="
				+ announcement + ", testName=" + testName + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", assignmentName=" + assignmentName + ", subject=" + subject + ", internal=" + internal
				+ ", external=" + external + ", campusName=" + campusName + ", campusId=" + campusId + ", programName="
				+ programName + ", moduleCategoryName=" + moduleCategoryName + ", moduleName=" + moduleName
				+ ", classDate=" + classDate + ", moduleId=" + moduleId + ", moduleAbbr=" + moduleAbbr + ", deptCode="
				+ deptCode + ", moduleCategoryCode=" + moduleCategoryCode + ", count=" + count
				+ ", isContinuousLecture=" + isContinuousLecture + ", isAttendanceAllowed=" + isAttendanceAllowed
				+ ", courseId=" + courseId + ", programIds=" + programIds + ", operation=" + operation + ", eventName="
				+ eventName + ", username=" + username + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", rollNo=" + rollNo + "]";
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public Course() {
	}

	public String getAbbr() {
		return this.abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getProperty1() {
		return property1;
	}

	public void setProperty1(String property1) {
		this.property1 = property1;
	}

	public String getProperty2() {
		return property2;
	}

	public void setProperty2(String property2) {
		this.property2 = property2;
	}

	public String getProperty3() {
		return property3;
	}

	public void setProperty3(String property3) {
		this.property3 = property3;
	}
	
	private String username;
	private String firstname;
	private String lastname;
	private String rollNo;



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	
	

}