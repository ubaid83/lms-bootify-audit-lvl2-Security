package com.spts.lms.beans.timetable;

import java.util.Date;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.attendance.StudentCourseAttendance;
import com.spts.lms.beans.course.Course;

public class Timetable extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long eventId;
	private String programId;
	private String facultyId;
	private String input_json;
	private String created_by;
	private Date created_on;
	private String last_updated_by;
	private Date last_updated_on;
	private String flag;
	private String class_date;
	private String start_time;
	private String end_time;
	private String courseId;
	private String courseName;
	private String programName;
    private String type;
    private String maxEndTimeForCourse;
    private String allottedLecture;
    private String conductedLecture;
    private String remainingLecture;
    private List<Course>courseList;
    private List<StudentCourseAttendance> studentCourseAttendanceList;

    private String isContinueLecture;
    private String endTime;
    
    private String isAttendanceMarked;
    
    

	public String getIsAttendanceMarked() {
		return isAttendanceMarked;
	}
	public void setIsAttendanceMarked(String isAttendanceMarked) {
		this.isAttendanceMarked = isAttendanceMarked;
	}
	public String getIsContinueLecture() {
		return isContinueLecture;
	}
	public void setIsContinueLecture(String isContinueLecture) {
		this.isContinueLecture = isContinueLecture;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public List<StudentCourseAttendance> getStudentCourseAttendanceList() {
		return studentCourseAttendanceList;
	}
	public void setStudentCourseAttendanceList(
			List<StudentCourseAttendance> studentCourseAttendanceList) {
		this.studentCourseAttendanceList = studentCourseAttendanceList;
	}
	public List<Course> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
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

	private List<String> multipleCourseId;

     


	
	public List<String> getMultipleCourseId() {
		return multipleCourseId;
	}
	public void setMultipleCourseId(List<String> multipleCourseId) {
		this.multipleCourseId = multipleCourseId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMaxEndTimeForCourse() {
		return maxEndTimeForCourse;
	}
	public void setMaxEndTimeForCourse(String maxEndTimeForCourse) {
		this.maxEndTimeForCourse = maxEndTimeForCourse;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getClass_date() {
		return class_date;
	}
	public void setClass_date(String class_date) {
		this.class_date = class_date;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public String getProgramId() {
		return programId;
	}
	public void setProgramId(String programId) {
		this.programId = programId;
	}
	public String getFacultyId() {
		return facultyId;
	}
	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}
	public String getInput_json() {
		return input_json;
	}
	public void setInput_json(String input_json) {
		this.input_json = input_json;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public Date getCreated_on() {
		return created_on;
	}
	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}
	public String getLast_updated_by() {
		return last_updated_by;
	}
	public void setLast_updated_by(String last_updated_by) {
		this.last_updated_by = last_updated_by;
	}
	public Date getLast_updated_on() {
		return last_updated_on;
	}
	public void setLast_updated_on(Date last_updated_on) {
		this.last_updated_on = last_updated_on;
	}
	
	@Override
	public String toString() {
		return "Timetable [eventId=" + eventId + ", programId=" + programId
				+ ", facultyId=" + facultyId + ", input_json=" + input_json
				+ ", created_by=" + created_by + ", created_on=" + created_on
				+ ", last_updated_by=" + last_updated_by + ", last_updated_on="
				+ last_updated_on + ", flag=" + flag + ", class_date="
				+ class_date + ", start_time=" + start_time + ", end_time="
				+ end_time + ", courseId=" + courseId + ", courseName="
				+ courseName + ", programName=" + programName + ", type="
				+ type + ", maxEndTimeForCourse=" + maxEndTimeForCourse
				+ ", allottedLecture=" + allottedLecture
				+ ", conductedLecture=" + conductedLecture
				+ ", remainingLecture=" + remainingLecture + ", courseList="
				+ courseList + ", studentCourseAttendanceList="
				+ studentCourseAttendanceList + ", isContinueLecture="
				+ isContinueLecture + ", endTime=" + endTime
				+ ", multipleCourseId=" + multipleCourseId + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((class_date == null) ? 0 : class_date.hashCode());
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((end_time == null) ? 0 : end_time.hashCode());
		result = prime * result + ((facultyId == null) ? 0 : facultyId.hashCode());
		result = prime * result + ((start_time == null) ? 0 : start_time.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timetable other = (Timetable) obj;
		if (class_date == null) {
			if (other.class_date != null)
				return false;
		} else if (!class_date.equals(other.class_date))
			return false;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (end_time == null) {
			if (other.end_time != null)
				return false;
		} else if (!end_time.equals(other.end_time))
			return false;
		if (facultyId == null) {
			if (other.facultyId != null)
				return false;
		} else if (!facultyId.equals(other.facultyId))
			return false;
		if (start_time == null) {
			if (other.start_time != null)
				return false;
		} else if (!start_time.equals(other.start_time))
			return false;
		return true;
	}
	
}
