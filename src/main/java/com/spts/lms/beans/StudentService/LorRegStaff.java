package com.spts.lms.beans.StudentService;

import java.util.Date;

import com.spts.lms.beans.BaseBean;

public class LorRegStaff extends BaseBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long lorRegId;
	private String username;
	private String department;
	private String staffId;
	private String	lorRegStaffId;
	private Long	Id;
	
	private String Role;


	private String fullName;
	private String appApproval;
	private String docApproval;
	private String lorFormatFilePath;
	private String finalLorUploadedBy;
	private String finalLorFilePath;
	private String lorApproval;
	private String active;
	private Date expectedDate;
	private String lorDocsFilePath;
	
	
	//New
	private String noOfCopies;
	private String appRejectionReason;
	private String docRejectionReason;
	private String lorRejectionReason;
	
	
	
	public String getAppRejectionReason() {
		return appRejectionReason;
	}
	public void setAppRejectionReason(String appRejectionReason) {
		this.appRejectionReason = appRejectionReason;
	}
	public String getDocRejectionReason() {
		return docRejectionReason;
	}
	public void setDocRejectionReason(String docRejectionReason) {
		this.docRejectionReason = docRejectionReason;
	}
	public String getLorRejectionReason() {
		return lorRejectionReason;
	}
	public void setLorRejectionReason(String lorRejectionReason) {
		this.lorRejectionReason = lorRejectionReason;
	}
	public String getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(String noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getLorDocsFilePath() {
		return lorDocsFilePath;
	}
	public void setLorDocsFilePath(String lorDocsFilePath) {
		this.lorDocsFilePath = lorDocsFilePath;
	}

	public String getLorRegStaffId() {
		return lorRegStaffId;
	}
	public void setLorRegStaffId(String lorRegStaffId) {
		this.lorRegStaffId = lorRegStaffId;
	}

	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}

	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}
	public long getLorRegId() {
		return lorRegId;
	}
	public void setLorRegId(long lorRegId) {
		this.lorRegId = lorRegId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getAppApproval() {
		return appApproval;
	}
	public void setAppApproval(String appApproval) {
		this.appApproval = appApproval;
	}
	public String getDocApproval() {
		return docApproval;
	}
	public void setDocApproval(String docApproval) {
		this.docApproval = docApproval;
	}
	public String getLorFormatFilePath() {
		return lorFormatFilePath;
	}
	public void setLorFormatFilePath(String lorFormatFilePath) {
		this.lorFormatFilePath = lorFormatFilePath;
	}
	public String getFinalLorUploadedBy() {
		return finalLorUploadedBy;
	}
	public void setFinalLorUploadedBy(String finalLorUploadedBy) {
		this.finalLorUploadedBy = finalLorUploadedBy;
	}
	public String getFinalLorFilePath() {
		return finalLorFilePath;
	}
	public void setFinalLorFilePath(String finalLorFilePath) {
		this.finalLorFilePath = finalLorFilePath;
	}
	
	public String getLorApproval() {
		return lorApproval;
	}
	public void setLorApproval(String lorApproval) {
		this.lorApproval = lorApproval;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	
	@Override
	public String toString() {
		return "LorRegStaff [lorRegId=" + lorRegId + ", username=" + username + ", department=" + department
				+ ", staffId=" + staffId + ", lorRegStaffId=" + lorRegStaffId + ", Id=" + Id + ", fullName=" + fullName
				+ ", appApproval=" + appApproval + ", docApproval=" + docApproval + ", lorFormatFilePath="
				+ lorFormatFilePath + ", finalLorUploadedBy=" + finalLorUploadedBy + ", finalLorFilePath="
				+ finalLorFilePath + ", lorApproval=" + lorApproval + ", active=" + active + ", expectedDate="
				+ expectedDate + ", lorDocsFilePath=" + lorDocsFilePath + ", noOfCopies=" + noOfCopies
				+ ", appRejectionReason=" + appRejectionReason + ", docRejectionReason=" + docRejectionReason
				+ ", lorRejectionReason=" + lorRejectionReason + ", name=" + name + "]";
	}
	
}
