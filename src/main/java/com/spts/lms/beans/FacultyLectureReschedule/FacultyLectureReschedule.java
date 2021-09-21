package com.spts.lms.beans.FacultyLectureReschedule;

import java.io.Serializable;
import com.spts.lms.beans.BaseBean;

public class FacultyLectureReschedule extends BaseBean implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String facultyId;
	private String details;
	private String msg;
	
	public String getFacultyId() {
		return facultyId;
	}
	
	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}
	
	public String getDetails() {
		return details;
	}
	
	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "FacultyLectureReschedule [facultyId=" + facultyId + ", details=" + details + ", msg=" + msg + "]";
	}
}
