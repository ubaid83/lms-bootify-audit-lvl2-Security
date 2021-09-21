package com.spts.lms.beans.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

@Component
public class QueryResponse extends BaseBean {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long queryId;
	private String username;
	private String queryDesc;
	private String queryResponse;
	private String queryRespondedBy;
	private Date queryRespondedTime;
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

	public Long getQueryId() {
		return queryId;
	}

	public void setQueryId(Long queryId) {
		this.queryId = queryId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getQueryDesc() {
		return queryDesc;
	}

	public void setQueryDesc(String queryDesc) {
		this.queryDesc = queryDesc;
	}

	public String getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(String queryResponse) {
		this.queryResponse = queryResponse;
	}

	public String getQueryRespondedBy() {
		return queryRespondedBy;
	}

	public void setQueryRespondedBy(String queryRespondedBy) {
		this.queryRespondedBy = queryRespondedBy;
	}

	public Date getQueryRespondedTime() {
		return queryRespondedTime;
	}

	public void setQueryRespondedTime(Date queryRespondedTime) {
		this.queryRespondedTime = queryRespondedTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "QueryResponse [id=" + id + ", queryId=" + queryId
				+ ", username=" + username + ", queryDesc=" + queryDesc
				+ ", queryResponse=" + queryResponse + ", queryRespondedBy="
				+ queryRespondedBy + ", queryRespondedTime="
				+ queryRespondedTime + ", rollNo=" + rollNo + "]";
	}

	
}
