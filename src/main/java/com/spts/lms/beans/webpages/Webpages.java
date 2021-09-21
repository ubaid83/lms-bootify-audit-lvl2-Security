package com.spts.lms.beans.webpages;

import java.io.Serializable;

import java.util.List;

import com.spts.lms.beans.BaseBean;

public class Webpages extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String content;
	private String startDate;
	private String endDate;
	private String filePath;
	private String makeAvailable;
	private String active;
	private String type;
	private String description;
	private String firstname;
	private String showArticle;
	private String schoolName;
	private String campusName;
	private String campusId;
	private String abbr;
	private String collegeName;
	private String schoolObjId;
	private List<String> dbList;
	private String dbListName;

	public String getDbListName() {
		return dbListName;
	}

	public void setDbListName(String dbListName) {
		this.dbListName = dbListName;
	}

	public List<String> getDbList() {
		return dbList;
	}

	public void setDbList(List<String> dbList) {
		this.dbList = dbList;
	}

	public String getSchoolObjId() {
		return schoolObjId;
	}

	public void setSchoolObjId(String schoolObjId) {
		this.schoolObjId = schoolObjId;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getShowArticle() {
		return showArticle;
	}

	public void setShowArticle(String showArticle) {
		this.showArticle = showArticle;
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

	private String lastname;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public enum type {
		LIBRARY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getMakeAvailable() {
		return makeAvailable;
	}

	public void setMakeAvailable(String makeAvailable) {
		this.makeAvailable = makeAvailable;
	}

	@Override
	public String toString() {
		return "Webpages [name=" + name + ", content=" + content
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", filePath=" + filePath + ", makeAvailable=" + makeAvailable
				+ ", active=" + active + ", type=" + type + ", description="
				+ description + ", firstname=" + firstname + ", showArticle="
				+ showArticle + ", schoolName=" + schoolName + ", campusName="
				+ campusName + ", campusId=" + campusId + ", abbr=" + abbr
				+ ", collegeName=" + collegeName + ", schoolObjId="
				+ schoolObjId + ", dbList=" + dbList + ", dbListName="
				+ dbListName + ", lastname=" + lastname + "]";
	}

}
