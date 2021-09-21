package com.spts.lms.beans.ica;

import com.spts.lms.beans.BaseBean;

public class IcaQueries extends BaseBean{

	
	private String icaId;
	private String filePath;
	private String isApproved;
	private String isReEvaluated;
	private String componentId;
	
	
	
	@Override
	public String toString() {
		return "IcaQueries [icaId=" + icaId + ", filePath=" + filePath + ", isApproved=" + isApproved
				+ ", isReEvaluated=" + isReEvaluated + ", componentId=" + componentId + "]";
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getIcaId() {
		return icaId;
	}
	public void setIcaId(String icaId) {
		this.icaId = icaId;
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
