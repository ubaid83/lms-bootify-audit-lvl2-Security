package com.spts.lms.beans.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class Query extends BaseBean {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private Long courseId;
	private String queryDesc;
	private String queryResponse;
	private Date queryResponseTime;
	private Date queryCreatedTime;
	private String rollNo;

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getQueryDesc() {
		return queryDesc;
	}

	public void setQueryDesc(String queryDesc) {
		this.queryDesc = queryDesc;
	}

	public Date getQueryCreatedTime() {
		return queryCreatedTime;
	}

	public void setQueryCreatedTime(Date queryCreatedTime) {
		this.queryCreatedTime = queryCreatedTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(String queryResponse) {
		this.queryResponse = queryResponse;
	}

	public Date getQueryResponseTime() {
		return queryResponseTime;
	}

	public void setQueryResponseTime(Date queryResponseTime) {
		this.queryResponseTime = queryResponseTime;
	}

	@Override
	public String toString() {
		return "Query [id=" + id + ", username=" + username + ", courseId="
				+ courseId + ", queryDesc=" + queryDesc + ", queryResponse="
				+ queryResponse + ", queryResponseTime=" + queryResponseTime
				+ ", queryCreatedTime=" + queryCreatedTime + ", rollNo="
				+ rollNo + "]";
	}

	

}
