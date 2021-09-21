package com.spts.lms.beans.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spts.lms.web.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTo {

	@Override
	public String toString() {
		return "UserTo [username=" + username + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", password=" + password
				+ ", enabled=" + enabled + ", enrollmentYear=" + enrollmentYear
				+ ", enrollmentMonth=" + enrollmentMonth + ", validityEndYear="
				+ validityEndYear + ", validityEndMonth=" + validityEndMonth
				+ ", fatherName=" + fatherName + ", motherName=" + motherName
				+ ", programId=" + programId + ", email=" + email + ", mobile="
				+ mobile + ", attempts=" + attempts + ", roles=" + roles
				+ ", rollNo=" + rollNo + ", librarianRoles=" + librarianRoles
				+ ", examRoles=" + examRoles + ", ITRoles=" + ITRoles
				+ ", counselorRoles=" + counselorRoles + ", supportAdminRoles="
				+ supportAdminRoles + ", staffRoles=" + staffRoles
				+ ", instituteFlag=" + instituteFlag + ", appRoles=" + appRoles
				+ ", reqApp=" + reqApp + ", menuRoles=" + menuRoles
				+ ", studentRoles=" + studentRoles + "]";
	}

	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private boolean enabled;
	private String enrollmentYear;
	private String enrollmentMonth;
	private String validityEndYear;
	private String validityEndMonth;
	private String fatherName;
	private String motherName;
	private Long programId;
	private String email;
	private String mobile;
	private Integer attempts;
	private String roles;
	private String rollNo;
	Map<String, Set<String>> librarianRoles;
	Map<String, Set<String>> examRoles;
	Map<String, Set<String>> ITRoles;
	private String collegeName;
	
	
	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public Map<String, Set<String>> getITRoles() {
		return ITRoles;
	}

	public void setITRoles(Map<String, Set<String>> iTRoles) {
		ITRoles = iTRoles;
	}

	Map<String, Set<String>> counselorRoles;
	Map<String, Set<String>> supportAdminRoles;
	Map<String, Set<String>> staffRoles;
	public Map<String, Set<String>> getStaffRoles() {
		return staffRoles;
	}

	public void setStaffRoles(Map<String, Set<String>> staffRoles) {
		this.staffRoles = staffRoles;
	}

	public Map<String, Set<String>> getSupportAdminRoles() {
		return supportAdminRoles;
	}

	public void setSupportAdminRoles(Map<String, Set<String>> supportAdminRoles) {
		this.supportAdminRoles = supportAdminRoles;
	}

	private String instituteFlag;
	
	
	
	public Map<String, Set<String>> getCounselorRoles() {
		return counselorRoles;
	}

	public void setCounselorRoles(Map<String, Set<String>> counselorRoles) {
		this.counselorRoles = counselorRoles;
	}

	public Map<String, Set<String>> getLibrarianRoles() {
		return librarianRoles;
	}

	public void setLibrarianRoles(Map<String, Set<String>> librarianRoles) {
		this.librarianRoles = librarianRoles;
	}

	public Map<String, Set<String>> getExamRoles() {
		return examRoles;
	}

	public void setExamRoles(Map<String, Set<String>> examRoles) {
		this.examRoles = examRoles;
	}

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	Map<String, List<String>> appRoles;
	String reqApp;

	public String getReqApp() {
		return reqApp;
	}

	public void setReqApp(String reqApp) {
		this.reqApp = reqApp;
	}

	Map<String, Map<String, Set<String>>> menuRoles;
	Map<String, Set<String>> studentRoles;

	public Map<String, Set<String>> getStudentRoles() {
		return studentRoles;
	}

	public void setStudentRoles(Map<String, Set<String>> studentRoles) {
		this.studentRoles = studentRoles;
	}

	public Map<String, Map<String, Set<String>>> getMenuRoles() {
		return menuRoles;
	}

	public void setMenuRoles(Map<String, Map<String, Set<String>>> menuRoles) {
		this.menuRoles = menuRoles;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getEnrollmentYear() {
		return enrollmentYear;
	}

	public void setEnrollmentYear(String enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}

	public String getEnrollmentMonth() {
		return enrollmentMonth;
	}

	public void setEnrollmentMonth(String enrollmentMonth) {
		this.enrollmentMonth = enrollmentMonth;
	}

	public String getValidityEndYear() {
		return validityEndYear;
	}

	public void setValidityEndYear(String validityEndYear) {
		this.validityEndYear = validityEndYear;
	}

	public String getValidityEndMonth() {
		return validityEndMonth;
	}

	public void setValidityEndMonth(String validityEndMonth) {
		this.validityEndMonth = validityEndMonth;
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

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public Map<String, List<String>> getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(Map<String, List<String>> appRoles) {
		this.appRoles = appRoles;
	}

	public void convert(User bean) {
		setUsername(bean.getUsername());
		setFirstname(bean.getFirstname());
		setLastname(bean.getLastname());
		setMobile(bean.getMobile());
		setFatherName(bean.getFatherName());
		setMotherName(bean.getMotherName());
		setEmail(bean.getEmail());
		setEnrollmentMonth(bean.getEnrollmentMonth());
		setEnrollmentYear(bean.getEnrollmentYear());
		setValidityEndMonth(bean.getValidityEndMonth());
		setValidityEndYear(bean.getValidityEndYear());
		setEnabled(bean.getEnabled());

	}

	public void convertRoles(List<Map<String, Object>> roles) {
		appRoles = new HashMap<String, List<String>>();
		for (Map<String, Object> rm : roles) {

			String value = Utils.getBlankIfNull(rm.get("appName"));
			appRoles.putIfAbsent(value, new ArrayList<String>());
			appRoles.get(value).add(Utils.getBlankIfNull(rm.get("role")));
		}

	}

	public String getInstituteFlag() {
		return instituteFlag;
	}

	public void setInstituteFlag(String instituteFlag) {
		this.instituteFlag = instituteFlag;
	}
	
	
}
