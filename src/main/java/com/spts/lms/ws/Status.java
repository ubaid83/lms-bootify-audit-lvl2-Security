package com.spts.lms.ws;

import com.spts.lms.beans.user.UserTo;

public class Status {

	StatusType status;
	String errorDescription;

	public StatusType getStatus() {
		return status;
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
