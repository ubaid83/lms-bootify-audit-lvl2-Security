package com.spts.lms.beans.portalFeedback;

import com.spts.lms.beans.BaseBean;

public class PortalFeedbackQuestion extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private long portalFeedbackId;
	private String question;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String option5;
	private String Type;
	private String questionRole;
	private PortalFeedbackResponse portalFeedbackResponse;
	
	
	public long getPortalFeedbackId() {
		return portalFeedbackId;
	}
	public void setPortalFeedbackId(long portalFeedbackId) {
		this.portalFeedbackId = portalFeedbackId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getOption1() {
		return option1;
	}
	public void setOption1(String option1) {
		this.option1 = option1;
	}
	public String getOption2() {
		return option2;
	}
	public void setOption2(String option2) {
		this.option2 = option2;
	}
	public String getOption3() {
		return option3;
	}
	public void setOption3(String option3) {
		this.option3 = option3;
	}
	public String getOption4() {
		return option4;
	}
	public void setOption4(String option4) {
		this.option4 = option4;
	}
	public String getOption5() {
		return option5;
	}
	public void setOption5(String option5) {
		this.option5 = option5;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getQuestionRole() {
		return questionRole;
	}
	public void setQuestionRole(String questionRole) {
		this.questionRole = questionRole;
	}
	public PortalFeedbackResponse getPortalFeedbackResponse() {
		return portalFeedbackResponse;
	}
	public void setPortalFeedbackResponse(
			PortalFeedbackResponse portalFeedbackResponse) {
		this.portalFeedbackResponse = portalFeedbackResponse;
	}
	
	
}
