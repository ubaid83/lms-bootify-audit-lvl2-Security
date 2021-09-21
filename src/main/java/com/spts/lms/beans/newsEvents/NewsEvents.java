package com.spts.lms.beans.newsEvents;

import java.util.Date;

import com.spts.lms.beans.BaseBean;

public class NewsEvents extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String subject;
	private String description;
	private String type;
	private Date startTime;
	private Date endTime;
	private String active;
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "NewsEvents [subject=" + subject + ", description="
				+ description + ", type=" + type + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", active=" + active + "]";
	}
	
}
