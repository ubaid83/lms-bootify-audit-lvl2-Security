package com.spts.lms.beans.weight;

import java.io.Serializable;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

public class Weight extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acadSession;
	private String acadYear;
	private String courseName;
	private Long courseId;
	private String wieghtagetype;
	private String wieghtageassigned;
	private String internal;
	private List<Course> allCourses;
	private String passInternal;
	private String total;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPassInternal() {
		return passInternal;
	}

	public void setPassInternal(String passInternal) {
		this.passInternal = passInternal;
	}

	public List<Course> getAllCourses() {
		return allCourses;
	}

	public void setAllCourses(List<Course> allCourses) {
		this.allCourses = allCourses;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String external;

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

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getWieghtagetype() {
		return wieghtagetype;
	}

	public void setWieghtagetype(String wieghtagetype) {
		this.wieghtagetype = wieghtagetype;
	}

	public String getWieghtageassigned() {
		return wieghtageassigned;
	}

	public void setWieghtageassigned(String wieghtageassigned) {
		this.wieghtageassigned = wieghtageassigned;
	}

	@Override
	public String toString() {
		return "Weight [acadSession=" + acadSession + ", acadYear=" + acadYear
				+ ", courseName=" + courseName + ", courseId=" + courseId
				+ ", wieghtagetype=" + wieghtagetype + ", wieghtageassigned="
				+ wieghtageassigned + ", internal=" + internal
				+ ", allCourses=" + allCourses + ", passInternal="
				+ passInternal + ", total=" + total + ", external=" + external
				+ "]";
	}

	

}
