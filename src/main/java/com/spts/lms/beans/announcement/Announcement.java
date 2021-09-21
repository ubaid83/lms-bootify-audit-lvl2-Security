package com.spts.lms.beans.announcement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;

/**
 * The persistent class for the program database table.
 * 
 */
public class Announcement extends BaseBean {
	private static final long serialVersionUID = 1L;

	public enum AnnouncementType {
		COURSE, PROGRAM, INSTITUTE, LIBRARY, COUNSELOR,NOTIFICATION,TIMETABLE;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String accessType;						
						

	private String courseName;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	private String fullName;
	private Long courseId;
	private Long programId;
	private AnnouncementType announcementType;
	private String subject;
	private String description;
	private String startDate;
	private String endDate;
	private String sendEmailAlert;
	private String sendSmsAlert;
	private Integer count;
	private String programName;
	private String announcementSubType;
	private String filePath;
	private String acadSession;
	private String acadMonth;
	private String campusName;
	private Long campusId;
	private String sendSmsAlertToParents;
	private String sendEmailAlertToParents;
	
	private List<String> programIds;
	private Long announcement_id;
	private String username;
	
	private String examMonth;
	private String examYear;
	private String examType;
	private String announcementTitle;
	private String announcementDesc;
	
	
	
	

	public String getAnnouncementDesc() {
		return announcementDesc;
	}

	public void setAnnouncementDesc(String announcementDesc) {
		this.announcementDesc = announcementDesc;
	}

	public String getAnnouncementTitle() {
		return announcementTitle;
	}

	public void setAnnouncementTitle(String announcementTitle) {
		this.announcementTitle = announcementTitle;
	}

	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getExamMonth() {
		return examMonth;
	}

	public void setExamMonth(String examMonth) {
		this.examMonth = examMonth;
	}

	public String getExamYear() {
		return examYear;
	}

	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}

	public Long getAnnouncement_id() {
		return announcement_id;
	}

	public void setAnnouncement_id(Long announcement_id) {
		this.announcement_id = announcement_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getProgramIds() {
		return programIds;
	}

	public void setProgramIds(List<String> programIds) {
		this.programIds = programIds;
	}

	public String getSendSmsAlertToParents() {
		return sendSmsAlertToParents;
	}

	public void setSendSmsAlertToParents(String sendSmsAlertToParents) {
		this.sendSmsAlertToParents = sendSmsAlertToParents;
	}

	public String getSendEmailAlertToParents() {
		return sendEmailAlertToParents;
	}

	public void setSendEmailAlertToParents(String sendEmailAlertToParents) {
		this.sendEmailAlertToParents = sendEmailAlertToParents;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public Long getCampusId() {
		return campusId;
	}

	public void setCampusId(Long campusId) {
		this.campusId = campusId;
	}

	private Integer acadYear;

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	private List<String> semesters;

	public List<String> getSemesters() {
		return semesters;
	}

	public void setSemesters(List<String> semesters) {
		this.semesters = semesters;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	private String filePreviewPath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePreviewPath() {
		return filePreviewPath;
	}

	public void setFilePreviewPath(String filePreviewPath) {
		this.filePreviewPath = filePreviewPath;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFullName() {
		return fullName;
	}

	public String getAnnouncementSubType() {
		return announcementSubType;
	}

	public void setAnnouncementSubType(String announcementSubType) {
		this.announcementSubType = announcementSubType;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Non persistent Fields
	 */

	public Announcement() {
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return formatDate(startDate);
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return formatDate(endDate);
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSendEmailAlert() {
		return sendEmailAlert;
	}

	public void setSendEmailAlert(String sendEmailAlert) {
		this.sendEmailAlert = sendEmailAlert != null ? sendEmailAlert : "N";
	}

	public String getSendSmsAlert() {
		return sendSmsAlert;
	}

	public void setSendSmsAlert(String sendSmsAlert) {
		this.sendSmsAlert = sendSmsAlert != null ? sendSmsAlert : "N";
	}

	public String getAnnouncementType() {
		return this.announcementType == null ? null : this.announcementType
				.toString();
	}

	public void setAnnouncementType(AnnouncementType announcementType) {
		this.announcementType = announcementType;
	}

	public void setAnnouncementType(String announcementType) {
		if (!StringUtils.isEmpty(announcementType)) {
			this.announcementType = AnnouncementType.valueOf(announcementType);
		}
	}

	@Override
	public String toString() {
		return "Announcement [accessType=" + accessType + ", courseName="
				+ courseName + ", fullName=" + fullName + ", courseId="
				+ courseId + ", programId=" + programId + ", announcementType="
				+ announcementType + ", subject=" + subject + ", description="
				+ description + ", startDate=" + startDate + ", endDate="
				+ endDate + ", sendEmailAlert=" + sendEmailAlert
				+ ", sendSmsAlert=" + sendSmsAlert + ", count=" + count
				+ ", programName=" + programName + ", announcementSubType="
				+ announcementSubType + ", filePath=" + filePath
				+ ", acadSession=" + acadSession + ", acadMonth=" + acadMonth
				+ ", campusName=" + campusName + ", campusId=" + campusId
				+ ", sendSmsAlertToParents=" + sendSmsAlertToParents
				+ ", sendEmailAlertToParents=" + sendEmailAlertToParents
				+ ", programIds=" + programIds + ", announcement_id="
				+ announcement_id + ", username=" + username + ", examMonth="
				+ examMonth + ", examYear=" + examYear + ", examType="
				+ examType + ", announcementTitle=" + announcementTitle
				+ ", announcementDesc=" + announcementDesc + ", acadYear="
				+ acadYear + ", semesters=" + semesters + ", filePreviewPath="
				+ filePreviewPath + "]";
	}

	

}