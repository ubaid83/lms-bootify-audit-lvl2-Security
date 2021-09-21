package com.spts.lms.beans.attendance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class RtasAttendance extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String studentNumber;
	private String location;
	private String date;
	private String termNoIn;
	private String termNoOut;
	private String inTime;
	private String outTime;
	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTermNoIn() {
		return termNoIn;
	}
	public void setTermNoIn(String termNoIn) {
		this.termNoIn = termNoIn;
	}
	public String getTermNoOut() {
		return termNoOut;
	}
	public void setTermNoOut(String termNoOut) {
		this.termNoOut = termNoOut;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "RtasAttendance [studentNumber=" + studentNumber + ", location="
				+ location + ", date=" + date + ", termNoIn=" + termNoIn
				+ ", termNoOut=" + termNoOut + ", inTime=" + inTime
				+ ", outTime=" + outTime + "]";
	}
	



}
