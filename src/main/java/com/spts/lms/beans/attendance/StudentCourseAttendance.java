package com.spts.lms.beans.attendance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

public class StudentCourseAttendance extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String courseId;
	private String startTime;
	private String endTime;
	private String username;
	private String rollNo;
	private String status;
	private String facultyId;
	private String courseName;
	private String firstname;
	private String lastname;
	private Course course;
	private String reason;
	private String listofAbsStud;
	private String noOfLec;
	private String attdDate;
	private String flag;
	private String organization;
	private String studentObjId;
	private String acadYear;
	private String acadSession;
	private String programId;
	private String eventId;
	private String attdYear;
	private String attdMonth;
	private String absent_count;
	private String present_count;
	private String total_count;
    private String lecture;
    private String msg;
    private String sapOperation;
    private String delFlag;
    private String attendanceTakenCount;
    
    private String campusId;
    private String campusName;
    
    private String courseStudentListMap;
    private String actualEndTime;
    private String cids;
    private String actualStartTime;
    private String classDate;
    
    private String allottedLecture;
    private String conductedLecture;
    private String remainingLecture;
    private String isContinuousLecture;
    private String isAttendanceAllowed;
    
    
    
    private String createdDateApp;
    private String lastModifiedDateApp;
    private String lastModifiedDateSap;
    
    private String presentFacultyId;
    private String allFacultyId;
    private String failStatus;
    
    
    
    
    

    
    public String getAllFacultyId() {
		return allFacultyId;
	}

	public void setAllFacultyId(String allFacultyId) {
		this.allFacultyId = allFacultyId;
	}

	public String getFailStatus() {
		return failStatus;
	}

	public void setFailStatus(String failStatus) {
		this.failStatus = failStatus;
	}

	public String getPresentFacultyId() {
		return presentFacultyId;
	}

	public void setPresentFacultyId(String presentFacultyId) {
		this.presentFacultyId = presentFacultyId;
	}

	public String getCreatedDateApp() {
		return createdDateApp;
	}

	public void setCreatedDateApp(String createdDateApp) {
		this.createdDateApp = createdDateApp;
	}

	public String getLastModifiedDateApp() {
		return lastModifiedDateApp;
	}

	public void setLastModifiedDateApp(String lastModifiedDateApp) {
		this.lastModifiedDateApp = lastModifiedDateApp;
	}

	public String getLastModifiedDateSap() {
		return lastModifiedDateSap;
	}

	public void setLastModifiedDateSap(String lastModifiedDateSap) {
		this.lastModifiedDateSap = lastModifiedDateSap;
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

	private String isAbsentAll;
	
	
	public String getIsAbsentAll() {
		return isAbsentAll;
	}

	public void setIsAbsentAll(String isAbsentAll) {
		this.isAbsentAll = isAbsentAll;
	}

	public String getAllottedLecture() {
		return allottedLecture;
	}

	public void setAllottedLecture(String allottedLecture) {
		this.allottedLecture = allottedLecture;
	}

	public String getConductedLecture() {
		return conductedLecture;
	}

	public void setConductedLecture(String conductedLecture) {
		this.conductedLecture = conductedLecture;
	}

	public String getRemainingLecture() {
		return remainingLecture;
	}

	public void setRemainingLecture(String remainingLecture) {
		this.remainingLecture = remainingLecture;
	}

	public String getClassDate() {
		return classDate;
	}

	public void setClassDate(String classDate) {
		this.classDate = classDate;
	}

	public String getCourseStudentListMap() {
		return courseStudentListMap;
	}

	public void setCourseStudentListMap(String courseStudentListMap) {
		this.courseStudentListMap = courseStudentListMap;
	}

	public String getActualEndTime() {
		return actualEndTime;
	}

	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	public String getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(String actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getAttendanceTakenCount() {
		return attendanceTakenCount;
	}

	public void setAttendanceTakenCount(String attendanceTakenCount) {
		this.attendanceTakenCount = attendanceTakenCount;
	}

	public String getLecture() {
		return lecture;
	}

	public void setLecture(String lecture) {
		this.lecture = lecture;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSapOperation() {
		return sapOperation;
	}

	public void setSapOperation(String sapOperation) {
		this.sapOperation = sapOperation;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getAttdYear() {
		return attdYear;
	}

	public void setAttdYear(String attdYear) {
		this.attdYear = attdYear;
	}

	public String getAttdMonth() {
		return attdMonth;
	}

	public void setAttdMonth(String attdMonth) {
		this.attdMonth = attdMonth;
	}

	public String getAbsent_count() {
		return absent_count;
	}

	public void setAbsent_count(String absent_count) {
		this.absent_count = absent_count;
	}

	public String getPresent_count() {
		return present_count;
	}

	public void setPresent_count(String present_count) {
		this.present_count = present_count;
	}

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getStudentObjId() {
		return studentObjId;
	}

	public void setStudentObjId(String studentObjId) {
		this.studentObjId = studentObjId;
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

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getListofAbsStud() {
		return listofAbsStud;
	}

	public void setListofAbsStud(String listofAbsStud) {
		this.listofAbsStud = listofAbsStud;
	}

	public String getNoOfLec() {
		return noOfLec;
	}

	public void setNoOfLec(String noOfLec) {
		this.noOfLec = noOfLec;
	}

	public String getAttdDate() {
		return attdDate;
	}

	public void setAttdDate(String attdDate) {
		this.attdDate = attdDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	private List<String> students = new ArrayList<String>();
	private List<String> statusList = new ArrayList<String>();

	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
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

	@Override
	public String toString() {
		return "StudentCourseAttendance [courseId=" + courseId + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", username=" + username + ", rollNo=" + rollNo + ", status=" + status + ", facultyId=" + facultyId
				+ ", courseName=" + courseName + ", firstname=" + firstname + ", lastname=" + lastname + ", course="
				+ course + ", reason=" + reason + ", listofAbsStud=" + listofAbsStud + ", noOfLec=" + noOfLec
				+ ", attdDate=" + attdDate + ", flag=" + flag + ", organization=" + organization + ", studentObjId="
				+ studentObjId + ", acadYear=" + acadYear + ", acadSession=" + acadSession + ", programId=" + programId
				+ ", eventId=" + eventId + ", attdYear=" + attdYear + ", attdMonth=" + attdMonth + ", absent_count="
				+ absent_count + ", present_count=" + present_count + ", total_count=" + total_count + ", lecture="
				+ lecture + ", msg=" + msg + ", sapOperation=" + sapOperation + ", delFlag=" + delFlag
				+ ", attendanceTakenCount=" + attendanceTakenCount + ", campusId=" + campusId + ", campusName="
				+ campusName + ", courseStudentListMap=" + courseStudentListMap + ", actualEndTime=" + actualEndTime
				+ ", cids=" + cids + ", actualStartTime=" + actualStartTime + ", classDate=" + classDate
				+ ", allottedLecture=" + allottedLecture + ", conductedLecture=" + conductedLecture
				+ ", remainingLecture=" + remainingLecture + ", isContinuousLecture=" + isContinuousLecture
				+ ", isAttendanceAllowed=" + isAttendanceAllowed + ", createdDateApp=" + createdDateApp
				+ ", lastModifiedDateApp=" + lastModifiedDateApp + ", lastModifiedDateSap=" + lastModifiedDateSap
				+ ", presentFacultyId=" + presentFacultyId + ", allFacultyId=" + allFacultyId + ", failStatus="
				+ failStatus + ", isAbsentAll=" + isAbsentAll + ", students=" + students + ", statusList=" + statusList
				+ ", active=" + active + "]";
	}

	

}