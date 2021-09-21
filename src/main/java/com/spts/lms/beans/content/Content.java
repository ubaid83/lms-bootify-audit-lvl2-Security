package com.spts.lms.beans.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class Content extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FOLDER = "Folder";
	public static final String FILE = "File";
	public static final String MULTIPLE_FILE = "Multiple_File";
	public static final String LINK = "Link";
	public static final String YOUTUBE_VIDEO = "Youtube Video";

	public static final String ACCESS_TYPE_PUBLIC = "Public";
	public static final String ACCESS_TYPE_PRIVATE = "Private";
	public static final String ACCESS_TYPE_EVERYONE = "Everyone";
	private int count;
	private String courseName;
	private String rollNo;
	private String campusName;
	private Long campusId;
	private String sendSmsAlertToParents;
	private String sendEmailAlertToParents;
	private Long programId;
	private String allocateToStudents;
	private String courseIdToExport;
	private String examViewType; 
	private String moduleId;
	private String moduleName;
	private String parentModuleId;
	private String idForModule;
	private String courseIdForSearch;
	private String contentFor;
	private String acadYearToExport;
	private String schoolToExport;
	
	private String facultyName;
	
	
	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getSchoolToExport() {
		return schoolToExport;
	}

	public void setSchoolToExport(String schoolToExport) {
		this.schoolToExport = schoolToExport;
	}

	public String getAcadYearToExport() {
		return acadYearToExport;
	}

	public void setAcadYearToExport(String acadYearToExport) {
		this.acadYearToExport = acadYearToExport;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getParentModuleId() {
		return parentModuleId;
	}

	public void setParentModuleId(String parentModuleId) {
		this.parentModuleId = parentModuleId;
	}

	public String getIdForModule() {
		return idForModule;
	}

	public void setIdForModule(String idForModule) {
		this.idForModule = idForModule;
	}

	public String getCourseIdForSearch() {
		return courseIdForSearch;
	}

	public void setCourseIdForSearch(String courseIdForSearch) {
		this.courseIdForSearch = courseIdForSearch;
	}

	public String getContentFor() {
		return contentFor;
	}

	public void setContentFor(String contentFor) {
		this.contentFor = contentFor;
	}

	public String getExamViewType() {
		return examViewType;
	}

	public void setExamViewType(String examViewType) {
		//this.examViewType = examViewType;
		this.examViewType = examViewType != null ? examViewType : "N";
	}

	public static String getMultipleFile() {
		return MULTIPLE_FILE;
	}

	public static String getAccessTypeEveryone() {
		return ACCESS_TYPE_EVERYONE;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getAllocateToStudents() {
		return allocateToStudents;
	}

	public void setAllocateToStudents(String allocateToStudents) {
		this.allocateToStudents = allocateToStudents;
	}

	public String getCourseIdToExport() {
		return courseIdToExport;
	}

	public void setCourseIdToExport(String courseIdToExport) {
		this.courseIdToExport = courseIdToExport;
	}

	public String getSendSmsAlertToParents() {
		return sendSmsAlertToParents;
	}

	public void setSendSmsAlertToParents(String sendSmsAlertToParents) {
		//this.sendSmsAlertToParents = sendSmsAlertToParents;
		this.sendSmsAlertToParents = sendSmsAlertToParents != null ? sendSmsAlertToParents : "N";
	}

	public String getSendEmailAlertToParents() {
		return sendEmailAlertToParents;
	}

	public void setSendEmailAlertToParents(String sendEmailAlertToParents) {
		//this.sendEmailAlertToParents = sendEmailAlertToParents;
		this.sendEmailAlertToParents = sendEmailAlertToParents != null ? sendEmailAlertToParents : "N";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static String getFolder() {
		return FOLDER;
	}

	public static String getFile() {
		return FILE;
	}

	public static String getLink() {
		return LINK;
	}

	public static String getYoutubeVideo() {
		return YOUTUBE_VIDEO;
	}

	public static String getAccessTypePublic() {
		return ACCESS_TYPE_PUBLIC;
	}

	public static String getAccessTypePrivate() {
		return ACCESS_TYPE_PRIVATE;
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

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private String idForCourse;

	// public static final String ACCESS_TYPE_SHARED = "Shared";

	public String getIdForCourse() {
		return idForCourse;
	}

	public void setIdForCourse(String idForCourse) {
		this.idForCourse = idForCourse;
	}

	String contentFileName;

	public String getContentFileName() {
		return contentFileName;
	}

	public void setContentFileName(String contentFileName) {
		this.contentFileName = contentFileName;
	}

	private String acadMonth;
	private Integer acadYear;
	private Long courseId;
	private String contentType;
	private String accessType;
	private String startDate;
	private String endDate;
	private String folderPath;
	private String isCourseRootFolder;
	private String facultyId;
	private String sendEmailAlert;
	private String sendSmsAlert;
	private String contentDescription;
	private String contentName;
	private String filePath;
	private String linkUrl;
	private Long parentContentId;
	private ArrayList<Content> childrenList = new ArrayList<Content>();

	private List<String> students = new ArrayList<String>();

	public ArrayList<Content> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(ArrayList<Content> childrenList) {
		this.childrenList = childrenList;
	}

	public Long getParentContentId() {
		return parentContentId;
	}

	public void setParentContentId(Long parentContentId) {
		this.parentContentId = parentContentId;
	}

	public String getContentName() {
		return contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	public String getContentDescription() {
		return contentDescription;
	}

	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getSendEmailAlert() {
		return sendEmailAlert;
	}

	public void setSendEmailAlert(String sendEmailAlert) {
		//this.sendEmailAlert = sendEmailAlert;
		this.sendEmailAlert = sendEmailAlert != null ? sendEmailAlert : "N";
	}

	public String getSendSmsAlert() {
		return sendSmsAlert;
	}

	public void setSendSmsAlert(String sendSmsAlert) {
		//this.sendSmsAlert = sendSmsAlert;
		this.sendSmsAlert = sendSmsAlert != null ? sendSmsAlert : "N";
	}

	public String getIsCourseRootFolder() {
		return isCourseRootFolder;
	}

	public void setIsCourseRootFolder(String isCourseRootFolder) {
		this.isCourseRootFolder = isCourseRootFolder;
	}

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

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
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

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getFontAwesomeClass() {
		if (filePath != null) {
			if (filePath.toUpperCase().endsWith(".DOCX")
					|| filePath.toUpperCase().endsWith(".DOC")) {
				return "fa-file-word-o";
			} else if (filePath.toUpperCase().endsWith(".XLSX")
					|| filePath.toUpperCase().endsWith(".XLS")) {
				return "fa-file-excel-o";
			} else if (filePath.toUpperCase().endsWith(".PPTX")
					|| filePath.toUpperCase().endsWith(".PPT")) {
				return "fa-file-powerpoint-o";
			} else if (filePath.toUpperCase().endsWith(".PDF")) {
				return "fa-file-pdf-o";
			} else if (filePath.toUpperCase().endsWith(".TXT")
					|| filePath.toUpperCase().endsWith(".RTF")) {
				return "fa-file-text-o";
			} else if (filePath.toUpperCase().endsWith(".JPG")
					|| filePath.toUpperCase().endsWith(".JPEG")
					|| filePath.toUpperCase().endsWith(".BMP")
					|| filePath.toUpperCase().endsWith(".GIF")
					|| filePath.toUpperCase().endsWith(".PNG")) {
				return "fa-file-image-o";
			} else if (filePath.toUpperCase().endsWith(".MP4")
					|| filePath.toUpperCase().endsWith(".AVI")
					|| filePath.toUpperCase().endsWith(".MPG")
					|| filePath.toUpperCase().endsWith(".MPEG")
					|| filePath.toUpperCase().endsWith(".WMV")) {
				return "fa-file-video-o";
			} else if (filePath.toUpperCase().endsWith(".MP3")
					|| filePath.toUpperCase().endsWith(".wav")) {
				return "fa-file-audio-o";
			}
		}
		return "fa-file-text-o";
	}

	public void setFontAwesomeClass(String fontAwesomeClass) {
	}

	@Override
	public String toString() {
		return "Content [count=" + count + ", courseName=" + courseName
				+ ", rollNo=" + rollNo + ", campusName=" + campusName
				+ ", campusId=" + campusId + ", sendSmsAlertToParents="
				+ sendSmsAlertToParents + ", sendEmailAlertToParents="
				+ sendEmailAlertToParents + ", programId=" + programId
				+ ", allocateToStudents=" + allocateToStudents
				+ ", courseIdToExport=" + courseIdToExport + ", examViewType="
				+ examViewType + ", moduleId=" + moduleId + ", moduleName="
				+ moduleName + ", parentModuleId=" + parentModuleId
				+ ", idForModule=" + idForModule + ", courseIdForSearch="
				+ courseIdForSearch + ", contentFor=" + contentFor
				+ ", acadYearToExport=" + acadYearToExport + ", idForCourse="
				+ idForCourse + ", contentFileName=" + contentFileName
				+ ", acadMonth=" + acadMonth + ", acadYear=" + acadYear
				+ ", courseId=" + courseId + ", contentType=" + contentType
				+ ", accessType=" + accessType + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", folderPath=" + folderPath
				+ ", isCourseRootFolder=" + isCourseRootFolder + ", facultyId="
				+ facultyId + ", sendEmailAlert=" + sendEmailAlert
				+ ", sendSmsAlert=" + sendSmsAlert + ", contentDescription="
				+ contentDescription + ", contentName=" + contentName
				+ ", filePath=" + filePath + ", linkUrl=" + linkUrl
				+ ", parentContentId=" + parentContentId + ", childrenList="
				+ childrenList + ", students=" + students + "]";
	}


}
