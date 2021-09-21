package com.spts.lms.beans.program;

import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class ProgramSessionCourse extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2375032896642772659L;
	private Long programSessionId;
	private Long courseId;
	private String type;
	
	/**
	 * Non persistent fields
	 */
	private Long programId;
	private List<Long> courseIds = new ArrayList<Long>();
	private String courseName;
	/*
	 * 
	 */

	public Long getProgramSessionId() {
		return programSessionId;
	}

	public void setProgramSessionId(Long programSessionId) {
		this.programSessionId = programSessionId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "ProgramSessionCourse [programSessionId=" + programSessionId
				+ ", courseId=" + courseId + ", type=" + type + ", getId()="
				+ getId() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getLastModifiedDate()=" + getLastModifiedDate()
				+ ", getCreatedBy()=" + getCreatedBy()
				+ ", getLastModifiedBy()=" + getLastModifiedBy() + "]";
	}

	public List<Long> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(List<Long> courseIds) {
		this.courseIds = courseIds;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

}
