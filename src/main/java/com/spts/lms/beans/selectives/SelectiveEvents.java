package com.spts.lms.beans.selectives;

import com.spts.lms.beans.BaseBean;

public class SelectiveEvents extends BaseBean {

	private String title;
	private String selective_type;
	private String active;
	
	private String startDate;
	private String endDate;
	
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSelective_type() {
		return selective_type;
	}
	public void setSelective_type(String selective_type) {
		this.selective_type = selective_type;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "SelectiveEvents [title=" + title + ", selective_type=" + selective_type + ", active=" + active + "]";
	}
	
}
