package com.spts.lms.beans.copyleaksAudit;

import com.spts.lms.beans.BaseBean;

public class CopyleaksAudit  extends BaseBean  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
	private long assignmentId;
	private long count;
	private long creditUsed;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getCreditUsed() {
		return creditUsed;
	}
	public void setCreditUsed(long creditUsed) {
		this.creditUsed = creditUsed;
	}
	
	

}
