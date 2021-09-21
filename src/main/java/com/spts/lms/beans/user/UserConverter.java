package com.spts.lms.beans.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spts.lms.web.utils.Utils;
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserConverter {

	private String username;
	private String password;
	private Long programId;
	private String instituteFlag;
	private String collegeName;
	Map<String, Set<String>> menuRolesApp = new HashMap<>();
	//Map<String, Map<String, Set<String>>> menuRoles = new HashMap<>();
	Map<String, List<String>> appRoles;
	// Map<String, Set<String>> roles;
	

	List<String> roles;
	String reqApp;

	
	/*public Map<String, Map<String, Set<String>>> getMenuRoles() {
		return menuRoles;
	}

	public void setMenuRoles(Map<String, Map<String, Set<String>>> menuRoles) {
		this.menuRoles = menuRoles;
	}*/

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getProgramId() {
		return programId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

	public String getInstituteFlag() {
		return instituteFlag;
	}

	public void setInstituteFlag(String instituteFlag) {
		this.instituteFlag = instituteFlag;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public Map<String, Set<String>> getMenuRolesApp() {
		return menuRolesApp;
	}

	public void setMenuRolesApp(Map<String, Set<String>> menuRolesApp) {
		this.menuRolesApp = menuRolesApp;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getReqApp() {
		return reqApp;
	}

	public void setReqApp(String reqApp) {
		this.reqApp = reqApp;
	}

	public void convertRoles(String app, String role) {
		List<String> roles = new ArrayList<>();
		roles.add(role);
		roles.add("ROLE_USER");
		appRoles = new HashMap<String, List<String>>();

		appRoles.put(app, roles);

	}

	public Map<String, List<String>> getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(Map<String, List<String>> appRoles) {
		this.appRoles = appRoles;
	}

	public void convertRolesForIntd(String role) {
		appRoles = new HashMap<String, List<String>>();

		List<String> roles = new ArrayList<>();
		roles.add(role);
		roles.add("ROLE_USER");
		String value = "INTDR";
		appRoles.put(value, roles);

	}

	@Override
	public String toString() {
		return "UserConverter [username=" + username + ", password=" + password
				+ ", programId=" + programId + ", instituteFlag="
				+ instituteFlag + ", collegeName=" + collegeName
				+ ", menuRolesApp=" + menuRolesApp + ", appRoles=" + appRoles
				+ ", roles=" + roles + ", reqApp=" + reqApp + "]";
	}

}
