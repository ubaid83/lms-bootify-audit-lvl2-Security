package com.spts.lms.beans.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class Library extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String FOLDER = "Folder";
	public static final String FILE = "File";
	public static final String LINK = "Link";
	public static final String ZIP = "Zip";

	public static String getZip() {
		return ZIP;
	}

	public static final String YOUTUBE_VIDEO = "Youtube Video";

	public static final String ACCESS_TYPE_PUBLIC = "Public";
	public static final String ACCESS_TYPE_PRIVATE = "Private";
	public static final String ACCESS_TYPE_SHARED = "Shared";

	/**
	 * Persistent Attributes
	 */
	
	private String oldLibraryPath;
	private String oldLibraryFolderPath;
	private String contentType;
	private String folderPath;
	private String contentDescription;
	private String contentName;
	private String filePath;
	private String linkUrl;
	private String libraryName;
	private String abbr;
	private String active;
	private String campusName;
	private Long campusId;
	private String fileName;
	private String copyPath;
	private String action;
	private String username;
	private String firstname;
	private String lastname;
	private String libraryId;
	//private String viewOnly;
	private String createOnly;
	private String editOnly;
	private List<Library> librarianList;
	private String shareToSchools;
	private String shareFromSchools;
	private Library newLibraryItem;
	
	

	public String getOldLibraryPath() {
		return oldLibraryPath;
	}

	public void setOldLibraryPath(String oldLibraryPath) {
		this.oldLibraryPath = oldLibraryPath;
	}

	public String getOldLibraryFolderPath() {
		return oldLibraryFolderPath;
	}

	public void setOldLibraryFolderPath(String oldLibraryFolderPath) {
		this.oldLibraryFolderPath = oldLibraryFolderPath;
	}

	public String getShareFromSchools() {
		return shareFromSchools;
	}

	public void setShareFromSchools(String shareFromSchools) {
		this.shareFromSchools = shareFromSchools;
	}

	public Library getNewLibraryItem() {
		return newLibraryItem;
	}

	public void setNewLibraryItem(Library newLibraryItem) {
		this.newLibraryItem = newLibraryItem;
	}

	public String getShareToSchools() {
		return shareToSchools;
	}

	public void setShareToSchools(String shareToSchools) {
		this.shareToSchools = shareToSchools;
	}

	public List<Library> getLibrarianList() {
		return librarianList;
	}

	public void setLibrarianList(List<Library> librarianList) {
		this.librarianList = librarianList;
	}

	public String getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/*public String getViewOnly() {
		return viewOnly;
	}

	public void setViewOnly(String viewOnly) {
		this.viewOnly = viewOnly;
	}*/

	public String getCreateOnly() {
		return createOnly;
	}

	public void setCreateOnly(String createOnly) {
		this.createOnly = createOnly;
	}

	public String getEditOnly() {
		return editOnly;
	}

	public void setEditOnly(String editOnly) {
		this.editOnly = editOnly;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCopyPath() {
		return copyPath;
	}

	public void setCopyPath(String copyPath) {
		this.copyPath = copyPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
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

	public static String getAccessTypeShared() {
		return ACCESS_TYPE_SHARED;
	}

	private Long parentId;
	private ArrayList<Library> childrenList = new ArrayList<Library>();

	public ArrayList<Library> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(ArrayList<Library> childrenList) {
		this.childrenList = childrenList;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
		return "Library [oldLibraryPath=" + oldLibraryPath + ", oldLibraryFolderPath=" + oldLibraryFolderPath
				+ ", contentType=" + contentType + ", folderPath=" + folderPath + ", contentDescription="
				+ contentDescription + ", contentName=" + contentName + ", filePath=" + filePath + ", linkUrl="
				+ linkUrl + ", libraryName=" + libraryName + ", abbr=" + abbr + ", active=" + active + ", campusName="
				+ campusName + ", campusId=" + campusId + ", fileName=" + fileName + ", copyPath=" + copyPath
				+ ", action=" + action + ", username=" + username + ", firstname=" + firstname + ", lastname="
				+ lastname + ", libraryId=" + libraryId + ", createOnly=" + createOnly + ", editOnly=" + editOnly
				+ ", librarianList=" + librarianList + ", shareToSchools=" + shareToSchools + ", shareFromSchools="
				+ shareFromSchools + ", newLibraryItem=" + newLibraryItem + ", parentId=" + parentId + ", childrenList="
				+ childrenList + "]";
	}



}
