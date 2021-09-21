package com.spts.lms.beans.course;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.mortbay.util.ajax.JSON;

import com.google.api.client.json.Json;
import com.spts.lms.beans.BaseBean;

public class CourseSyllabus extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long courseId;

	private String unit;
	
	private String description;
	
	private String duration;
	
	private String textbooks;
  	
	private String referbooks;
	





	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getTextbooks() {
		return textbooks;
	}

	public void setTextbooks(String textbooks) {
		this.textbooks = textbooks;
	}

	public String getReferbooks() {
		return referbooks;
	}

	public void setReferbooks(String referbooks) {
		this.referbooks = referbooks;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public String toString() {
		return "CourseSyllabus [ courseId=" + courseId
				+ ", unit=" + unit + ", description=" + description
				+ ", duration=" + duration + ", textbooks=" + textbooks
				+ ", referbooks=" + referbooks + "]";
	}
	
	
	

	

	
	


	

}
