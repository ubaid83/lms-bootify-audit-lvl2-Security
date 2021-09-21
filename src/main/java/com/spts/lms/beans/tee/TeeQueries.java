package com.spts.lms.beans.tee;

import com.spts.lms.beans.BaseBean;

public class TeeQueries extends BaseBean{
	
	private String teeId;
	private String filePath;
	private String isApproved;
	private String isReEvaluated;
	public String getTeeId() {
		return teeId;
	}
	public void setTeeId(String teeId) {
		this.teeId = teeId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
	public String getIsReEvaluated() {
		return isReEvaluated;
	}
	public void setIsReEvaluated(String isReEvaluated) {
		this.isReEvaluated = isReEvaluated;
	}
	
	

}
