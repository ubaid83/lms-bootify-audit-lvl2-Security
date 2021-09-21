package com.spts.lms.beans.program;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spts.lms.beans.BaseBean;

/**
 * The persistent class for the program database table.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Program extends BaseBean {
	private static final long serialVersionUID = 1L;

	public enum SessionType {
		ANNUAL(12), SEMESTER(6), TRIMESTER(4);

		private int numOfMonths;

		SessionType(int numOfMonths) {
			this.setNumOfMonths(numOfMonths);
		}

		public int getNumOfMonths() {
			return numOfMonths;
		}

		public void setNumOfMonths(int numOfMonths) {
			this.numOfMonths = numOfMonths;
		}
	}

	private String abbr;

	private String active;

	private String programName;

	private short maxDurationInMonths;

	private String revisedFromMonth;

	private String revisedFromYear;

	private SessionType sessionType;

	private short durationInMonths;
	
	private String username;
	private String url;
	private String password;
	private String dbName;
	
	private String schoolObjId;
	
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSchoolObjId() {
		return schoolObjId;
	}

	public void setSchoolObjId(String schoolObjId) {
		this.schoolObjId = schoolObjId;
	}

	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}

	public Program() {
	}

	public String getAbbr() {
		return this.abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public short getMaxDurationInMonths() {
		return this.maxDurationInMonths;
	}

	public void setMaxDurationInMonths(short maxDurationInMonths) {
		this.maxDurationInMonths = maxDurationInMonths;
	}

	public String getRevisedFromMonth() {
		return revisedFromMonth;
	}

	public void setRevisedFromMonth(String revisedFromMonth) {
		this.revisedFromMonth = revisedFromMonth;
	}

	public String getRevisedFromYear() {
		return revisedFromYear;
	}

	public void setRevisedFromYear(String revisedFromYear) {
		this.revisedFromYear = revisedFromYear;
	}

	public String getSessionType() {
		return this.sessionType == null ? null : this.sessionType.toString();
	}

	public void setSessionType(String sessionType) {
		if (!StringUtils.isEmpty(sessionType)) {
			this.sessionType = SessionType.valueOf(sessionType);
		}
	}

	public short getDurationInMonths() {
		return durationInMonths;
	}

	public void setDurationInMonths(short durationInMonths) {
		this.durationInMonths = durationInMonths;
	}

	@Override
	public String toString() {
		return "Program [abbr=" + abbr + ", active=" + active
				+ ", programName=" + programName + ", maxDurationInMonths="
				+ maxDurationInMonths + ", revisedFromMonth="
				+ revisedFromMonth + ", revisedFromYear=" + revisedFromYear
				+ ", sessionType=" + sessionType + ", durationInMonths="
				+ durationInMonths + ", programSessions=" + ", getId()="
				+ getId() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getLastModifiedDate()=" + getLastModifiedDate()
				+ ", getCreatedBy()=" + getCreatedBy()
				+ ", getLastModifiedBy()=" + getLastModifiedBy()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}