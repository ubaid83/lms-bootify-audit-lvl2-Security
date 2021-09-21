package com.spts.lms.beans.portalFeedback;

import java.io.Serializable;
import java.util.ArrayList;

import com.spts.lms.beans.BaseBean;


public class PortalFeedbackQuery extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String userRole;
	private String firstname;
	private String lastname;
	private String collegeName;
	private String schoolObjId;
	private Integer feedbackCount;
	private String dbName;
	private String url;
	private String dbUsername;
	private String password;
	private String createdOn;
	

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private ArrayList<PortalFeedbackQuery> childrenList = new ArrayList<PortalFeedbackQuery>();

	public ArrayList<PortalFeedbackQuery> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(ArrayList<PortalFeedbackQuery> childrenList) {
		this.childrenList = childrenList;
	}

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

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	private long portalFeedbackId;
	private long portalFeedbackQuestionId;
	private String answer;
	private long parentId;
	private String active;


	@Override
	public String toString() {
		return "PortalFeedbackQuery [username=" + username + ", userRole="
				+ userRole + ", firstname=" + firstname + ", lastname="
				+ lastname + ", collegeName=" + collegeName + ", schoolObjId="
				+ schoolObjId + ", feedbackCount=" + feedbackCount
				+ ", dbName=" + dbName + ", url=" + url + ", dbUsername="
				+ dbUsername + ", password=" + password + ", createdOn="
				+ createdOn + ", childrenList=" + childrenList
				+ ", portalFeedbackId=" + portalFeedbackId
				+ ", portalFeedbackQuestionId=" + portalFeedbackQuestionId
				+ ", answer=" + answer + ", parentId=" + parentId + ", active="
				+ active + "]";
	}


	

	
}
