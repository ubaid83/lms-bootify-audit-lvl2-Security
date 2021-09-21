package com.spts.lms.beans.user;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.spts.lms.beans.BaseBean;

@Component
public class UploadTimeLimitSession extends BaseBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String session;
	private Date startDate;
	private Date endDate;
	private String active;
	
	
	public String getActive() {
		return active;
	}



	public void setActive(String active) {
		this.active = active;
	}



	public String getSession() {
		return session;
	}



	public void setSession(String session) {
		this.session = session;
	}



	public Date getStartDate() {
		return startDate;
	}



	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public Date getEndDate() {
		return endDate;
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	@Override
	public String toString() {
		return "UploadTimeLimitSession [session=" + session + ", startDate="
				+ startDate + ", endDate=" + endDate + ", active=" + active
				+ "]";
	}



	protected String formatDate(String date) {
		if (null == date)
			return date;
		if (date.length() > 19) {
			return date.substring(0, 19).replace(' ', 'T');
		} else {
			return date.replace(' ', 'T');
		}
	}
	


	
	
}
