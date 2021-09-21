package com.spts.lms.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;

public class Token extends UsernamePasswordAuthenticationToken {

	private static Logger logger = LoggerFactory.getLogger(Token.class);

	Map<String, Set<String>> menuRights;
	
	//added 21/11/2017
	List<Course> courseList;
	private String instituteFlag;

	public List<Course> getCourseList() {
		return courseList;
	}
	
	private String collegeName;

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}
	private List<UserCourse> userCourseList;
	public List<UserCourse> getUserCourseList() {
	return userCourseList;
	}
	 
	public void setUserCourseList(List<UserCourse> userCourseList) {
	this.userCourseList = userCourseList;
	}
	//ends
	String programName;
	public String getProgramName() {
		return programName;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	String programId;
	String acadSession;
	
	

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	String acadMonth;
	String acadYear;

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public String getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}

	public Map<String, Set<String>> getMenuRights() {
		return menuRights;
	}

	public void setMenuRights(Map<String, Set<String>> menuRights) {
		this.menuRights = menuRights;
	}

	public Token(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);

	}

	public Token(Object principal, Object credentials) {
		super(principal, credentials);

	}

	public Map<String, List<String>> getAppRoles() {
		return appRoles;
	}

	public void setAppRoles(Map<String, List<String>> appRoles) {
		this.appRoles = appRoles;
	}

	public static Collection<? extends GrantedAuthority> setAppRolesEnum(
			String appName, Map<String, List<String>> appRoleMap) {
		List<String> roles = appRoleMap.get(appName);
		Collection<Role> roleList = new ArrayList<Role>();
		if(roles!=null)
		for (String role : roles) {
			Role r = Role.valueOf(role);
			logger.info("Role" + r);
			if (r != null)
				roleList.add(r);

		}
		return roleList;

	}
	
	// added for new login
    public static Collection<? extends GrantedAuthority> setAppRoleListEnum(
            String appName, List<String> roles) {
      //List<String> roles = appRoleMap.get(appName);
      Collection<Role> roleList = new ArrayList<Role>();
      if(roles!=null)
      for (String role : roles) {
            Role r = Role.valueOf(role);
            logger.info("Role" + r);
            if (r != null)
                  roleList.add(r);

      }
      return roleList;

}


	Map<String, List<String>> appRoles;

	public String getInstituteFlag() {
		return instituteFlag;
	}

	public void setInstituteFlag(String instituteFlag) {
		this.instituteFlag = instituteFlag;
	}
	
	

}
