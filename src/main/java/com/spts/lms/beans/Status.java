package com.spts.lms.beans;

import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserTo;

public class Status {

	public UserTo getUser() {
		return user;
	}

	public void setUser(UserTo user) {
		this.user = user;
	}

	UserTo user;
	//List<User> userList;
	List<UserTo> userList;
	public List<UserTo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserTo> userList) {
		this.userList = userList;
	}

	public int errorCount;
	public int successCount;
	public String errorDescription;
	public String successDescription;
	

	StatusType status;
	List<String> ignoredId = new ArrayList<String>();

	List<String> succesId = new ArrayList<String>();

	List<String> failedJson = new ArrayList<>();


	public StatusType getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Status [user=" + user + ", userList=" + userList
				+ ", errorCount=" + errorCount + ", successCount="
				+ successCount + ", errorDescription=" + errorDescription
				+ ", successDescription=" + successDescription + ", status="
				+ status + ", ignoredId=" + ignoredId + ", succesId="
				+ succesId + ", failedJson=" + failedJson + "]";
	}

	public int getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public String getSuccessDescription() {
		return successDescription;
	}

	public void setSuccessDescription(String successDescription) {
		this.successDescription = successDescription;
	}

	public List<String> getIgnoredId() {
		return ignoredId;
	}

	public void setIgnoredId(List<String> ignoredId) {
		this.ignoredId = ignoredId;
	}

	public List<String> getSuccesId() {
		return succesId;
	}

	public void setSuccesId(List<String> succesId) {
		this.succesId = succesId;
	}

	public List<String> getFailedJson() {
		return failedJson;
	}

	public void setFailedJson(List<String> failedJson) {
		this.failedJson = failedJson;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
}
