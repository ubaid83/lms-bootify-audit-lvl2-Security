package com.spts.lms.beans.StudentService;


import java.io.Serializable;
import java.util.Date;

import com.spts.lms.beans.BaseBean;



public class StudentHostelForm extends BaseBean implements Serializable{
	private String username;
	private String firstname;
	private String lastname;
	private String fatherName;
	private String motherName;
	private String rollNo;
	private String acadYear;
	private String programName;
	private String studyClass;
	private String division;
	private String reason;
	private Long serviceId;
	private String level1;
	private String level3;
	private String level2;
	private String flag1;
	private String flag2;
	private String flag3;
	private String allowLevel2;
	private String allowLevel3;
	private String approvedLevel1;
	private String approvedLevel2;
	private String approvedLevel3;
	private String approvedLevel;
	private String remark1;
	private String remark2;
	private String remark3;
	private String allowRemark2;
	private String allowRemark3;
	private String user1;
	private String user2;
	private String user3;
	private String email;
	private String isSaveAsDraft;
	private String address;
	private String mobile;
	private String dob;
	private String nationality;
	private String parentName;
	private String relationship;
	private String occupation;
	private String designation;
	private String officeAddress;
	private String parentMobile;
	private String localName1;
	private String localAddress1;
	private String localMobile1;
	private String localName2;
	private String localAddress2;
	private String localMobile2;
	private Date date;
	private String campus;
	private String parentEmail;
	private String telephone;
	
	
	
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getParentEmail() {
		return parentEmail;
	}
	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getParentMobile() {
		return parentMobile;
	}
	public void setParentMobile(String parentMobile) {
		this.parentMobile = parentMobile;
	}
	public String getLocalName1() {
		return localName1;
	}
	public void setLocalName1(String localName1) {
		this.localName1 = localName1;
	}
	public String getLocalAddress1() {
		return localAddress1;
	}
	public void setLocalAddress1(String localAddress1) {
		this.localAddress1 = localAddress1;
	}
	public String getLocalMobile1() {
		return localMobile1;
	}
	public void setLocalMobile1(String localMobile1) {
		this.localMobile1 = localMobile1;
	}
	public String getLocalName2() {
		return localName2;
	}
	public void setLocalName2(String localName2) {
		this.localName2 = localName2;
	}
	public String getLocalAddress2() {
		return localAddress2;
	}
	public void setLocalAddress2(String localAddress2) {
		this.localAddress2 = localAddress2;
	}
	public String getLocalMobile2() {
		return localMobile2;
	}
	public void setLocalMobile2(String localMobile2) {
		this.localMobile2 = localMobile2;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getIsSaveAsDraft() {
		return isSaveAsDraft;
	}
	public void setIsSaveAsDraft(String isSaveAsDraft) {
		this.isSaveAsDraft = isSaveAsDraft;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUser1() {
		return user1;
	}
	public void setUser1(String user1) {
		this.user1 = user1;
	}
	public String getUser2() {
		return user2;
	}
	public void setUser2(String user2) {
		this.user2 = user2;
	}
	public String getUser3() {
		return user3;
	}
	public void setUser3(String user3) {
		this.user3 = user3;
	}
	public String getAllowRemark2() {
		return allowRemark2;
	}
	public void setAllowRemark2(String allowRemark2) {
		this.allowRemark2 = allowRemark2;
	}
	public String getAllowRemark3() {
		return allowRemark3;
	}
	public void setAllowRemark3(String allowRemark3) {
		this.allowRemark3 = allowRemark3;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	private String payment;
	private String isSubmitted;
	private String active;
	
	
	
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getIsSubmitted() {
		return isSubmitted;
	}
	public void setIsSubmitted(String isSubmitted) {
		this.isSubmitted = isSubmitted;
	}
	public String getApprovedLevel() {
		return approvedLevel;
	}
	public void setApprovedLevel(String approvedLevel) {
		this.approvedLevel = approvedLevel;
	}
	public String getApprovedLevel1() {
		return approvedLevel1;
	}
	public void setApprovedLevel1(String approvedLevel1) {
		this.approvedLevel1 = approvedLevel1;
	}
	public String getApprovedLevel2() {
		return approvedLevel2;
	}
	public void setApprovedLevel2(String approvedLevel2) {
		this.approvedLevel2 = approvedLevel2;
	}
	public String getApprovedLevel3() {
		return approvedLevel3;
	}
	public void setApprovedLevel3(String approvedLevel3) {
		this.approvedLevel3 = approvedLevel3;
	}
	public String getAllowLevel2() {
		return allowLevel2;
	}
	public void setAllowLevel2(String allowLevel2) {
		this.allowLevel2 = allowLevel2;
	}
	public String getAllowLevel3() {
		return allowLevel3;
	}
	public void setAllowLevel3(String allowLevel3) {
		this.allowLevel3 = allowLevel3;
	}
	@Override
	public String toString() {
		return "HostelForm [username=" + username + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", fatherName=" + fatherName
				+ ", motherName=" + motherName + ", rollNo=" + rollNo
				+ ", acadYear=" + acadYear + ", programName=" + programName
				+ ", studyClass=" + studyClass + ", division=" + division
				+ ", reason=" + reason + ", serviceId=" + serviceId
				+ ", level1=" + level1 + ", level3=" + level3 + ", level2="
				+ level2 + ", flag1=" + flag1 + ", flag2=" + flag2 + ", flag3="
				+ flag3 + ", allowLevel2=" + allowLevel2 + ", allowLevel3="
				+ allowLevel3 + ", approvedLevel1=" + approvedLevel1
				+ ", approvedLevel2=" + approvedLevel2 + ", approvedLevel3="
				+ approvedLevel3 + ", approvedLevel=" + approvedLevel
				+ ", remark1=" + remark1 + ", remark2=" + remark2
				+ ", remark3=" + remark3 + ", allowRemark2=" + allowRemark2
				+ ", allowRemark3=" + allowRemark3 + ", user1=" + user1
				+ ", user2=" + user2 + ", user3=" + user3 + ", email=" + email
				+ ", isSaveAsDraft=" + isSaveAsDraft + ", address=" + address
				+ ", mobile=" + mobile + ", dob=" + dob + ", nationality="
				+ nationality + ", parentName=" + parentName
				+ ", relationship=" + relationship + ", occupation="
				+ occupation + ", designation=" + designation
				+ ", officeAddress=" + officeAddress + ", parentMobile="
				+ parentMobile + ", localName1=" + localName1
				+ ", localAddress1=" + localAddress1 + ", localMobile1="
				+ localMobile1 + ", localName2=" + localName2
				+ ", localAddress2=" + localAddress2 + ", localMobile2="
				+ localMobile2 + ", date=" + date + ", campus=" + campus
				+ ", parentEmail=" + parentEmail + ", telephone=" + telephone
				+ ", payment=" + payment + ", isSubmitted=" + isSubmitted
				+ ", active=" + active + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getAcadYear() {
		return acadYear;
	}
	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getStudyClass() {
		return studyClass;
	}
	public void setStudyClass(String studyClass) {
		this.studyClass = studyClass;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getFlag1() {
		return flag1;
	}
	public void setFlag1(String flag1) {
		this.flag1 = flag1;
	}
	public String getFlag2() {
		return flag2;
	}
	public void setFlag2(String flag2) {
		this.flag2 = flag2;
	}
	public String getFlag3() {
		return flag3;
	}
	public void setFlag3(String flag3) {
		this.flag3 = flag3;
	}
	
	
	

}
