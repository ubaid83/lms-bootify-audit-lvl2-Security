package com.spts.lms.beans.portalFeedback;

import java.util.List;

import org.springframework.util.AutoPopulatingList;

import com.spts.lms.beans.BaseBean;

public class PortalFeedback extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String userRole;
	private Long pfId;
	private Long pfrId;
	private String answer;
	private List<PortalFeedbackQuery> queryList;
	private String firstname;
	private String lastname;
	private String collegeName;
	private String schoolObjId;
	private Integer feedbackCount;
	private String dbName;
	private String url;
	private String dbUsername;
	private List<PortalFeedbackQuery> portalFeedbackQueryList;
	private String repliedByAdmin;
	private List<PortalFeedback> feedbackList;
	private String createdOn;
	private String email;
	private String mobile;

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<PortalFeedback> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<PortalFeedback> feedbackList) {
		this.feedbackList = feedbackList;
	}

	public String getRepliedByAdmin() {
		return repliedByAdmin;
	}

	public void setRepliedByAdmin(String repliedByAdmin) {
		this.repliedByAdmin = repliedByAdmin;
	}

	public List<PortalFeedbackQuery> getPortalFeedbackQueryList() {
		return portalFeedbackQueryList;
	}

	public void setPortalFeedbackQueryList(
			List<PortalFeedbackQuery> portalFeedbackQueryList) {
		this.portalFeedbackQueryList = portalFeedbackQueryList;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getSchoolObjId() {
		return schoolObjId;
	}

	public void setSchoolObjId(String schoolObjId) {
		this.schoolObjId = schoolObjId;
	}

	public Integer getFeedbackCount() {
		return feedbackCount;
	}

	public void setFeedbackCount(Integer feedbackCount) {
		this.feedbackCount = feedbackCount;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Long getPfId() {
		return pfId;
	}

	public void setPfId(Long pfId) {
		this.pfId = pfId;
	}

	public Long getPfrId() {
		return pfrId;
	}

	public void setPfrId(Long pfrId) {
		this.pfrId = pfrId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public List<PortalFeedbackQuery> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<PortalFeedbackQuery> queryList) {
		this.queryList = queryList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private List<PortalFeedbackQuestion> yesNoQuestionList = new AutoPopulatingList<PortalFeedbackQuestion>(
			PortalFeedbackQuestion.class);
	private List<PortalFeedbackQuestion> ratingQuestionList = new AutoPopulatingList<PortalFeedbackQuestion>(
			PortalFeedbackQuestion.class);
	private List<PortalFeedbackQuestion> commentQuestionList = new AutoPopulatingList<PortalFeedbackQuestion>(
			PortalFeedbackQuestion.class);

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public List<PortalFeedbackQuestion> getYesNoQuestionList() {
		return yesNoQuestionList;
	}

	public void setYesNoQuestionList(
			List<PortalFeedbackQuestion> yesNoQuestionList) {
		this.yesNoQuestionList = yesNoQuestionList;
	}

	public List<PortalFeedbackQuestion> getRatingQuestionList() {
		return ratingQuestionList;
	}

	public void setRatingQuestionList(
			List<PortalFeedbackQuestion> ratingQuestionList) {
		this.ratingQuestionList = ratingQuestionList;
	}

	public List<PortalFeedbackQuestion> getCommentQuestionList() {
		return commentQuestionList;
	}

	public void setCommentQuestionList(
			List<PortalFeedbackQuestion> commentQuestionList) {
		this.commentQuestionList = commentQuestionList;
	}

	@Override
	public String toString() {
		return "PortalFeedback [username=" + username + ", userRole="
				+ userRole + ", pfId=" + pfId + ", pfrId=" + pfrId
				+ ", answer=" + answer + ", queryList=" + queryList
				+ ", firstname=" + firstname + ", lastname=" + lastname
				+ ", collegeName=" + collegeName + ", schoolObjId="
				+ schoolObjId + ", feedbackCount=" + feedbackCount
				+ ", dbName=" + dbName + ", url=" + url + ", dbUsername="
				+ dbUsername + ", portalFeedbackQueryList="
				+ portalFeedbackQueryList + ", repliedByAdmin="
				+ repliedByAdmin + ", feedbackList=" + feedbackList
				+ ", createdOn=" + createdOn + ", email=" + email + ", mobile="
				+ mobile + ", password=" + password + ", yesNoQuestionList="
				+ yesNoQuestionList + ", ratingQuestionList="
				+ ratingQuestionList + ", commentQuestionList="
				+ commentQuestionList + "]";
	}

}
