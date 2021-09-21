package com.spts.lms.beans.portalFeedback;

import com.spts.lms.beans.BaseBean;

public class PortalFeedbackResponse extends BaseBean {

	@Override
	public String toString() {
		return "PortalFeedbackResponse [portalFeedbackId=" + portalFeedbackId
				+ ", portalFeedbackQuestionId=" + portalFeedbackQuestionId
				+ ", answer=" + answer + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private long portalFeedbackId;
	private long portalFeedbackQuestionId;
	private String answer;
	
	
	public long getPortalFeedbackId() {
		return portalFeedbackId;
	}
	public void setPortalFeedbackId(long portalFeedbackId) {
		this.portalFeedbackId = portalFeedbackId;
	}
	public long getPortalFeedbackQuestionId() {
		return portalFeedbackQuestionId;
	}
	public void setPortalFeedbackQuestionId(long portalFeedbackQuestionId) {
		this.portalFeedbackQuestionId = portalFeedbackQuestionId;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}
