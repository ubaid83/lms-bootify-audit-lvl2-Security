package com.spts.lms.beans;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseBean implements Serializable {

	private static final long serialVersionUID = 6355333012082656202L;

	private Long id;

	@JsonIgnore
	private Date createdDate;
	@JsonIgnore
	private Date lastModifiedDate;

	private String createdBy;

	private String lastModifiedBy;

	// Non persistent fields
	// For excel data check
	private Boolean isErrorRecord = false;
	// For excel validation message
	private String errorMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Boolean isErrorRecord() {
		return isErrorRecord;
	}

	public void setErrorRecord(Boolean isErrorRecord) {
		this.isErrorRecord = isErrorRecord;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	protected String checkNullAndSet(String value, String defaultValue) {
		return null == value ? defaultValue : value;
	}

	protected String compareStringAndSet(String value, String compare,
			String defaultValue) {
		return compare.equalsIgnoreCase(value) ? compare : defaultValue;
	}

	protected String checkYElseSetN(String value) {
		return compareStringAndSet(value, "Y", "N");
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
