package com.spts.lms.beans.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AutoPopulatingList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;

/**
 * The persistent class for the users database table.
 * 
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseBean implements Serializable, UserDetails {
	private static final long serialVersionUID = 1L;
	private static final Integer MAX_ATTEMPTS = 3;

	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private String rollNo;
	private String studentObjectId;
	private List<String> rolesForUser;
	private String campusName;
	private Long campusId;
	private String schoolAbbr;
    private String schoolObjId;
    private String programName;
    private String religion;
    private String isLoggedIn;
    private String userImage;
    private String startDate;
    private String endDate;
    private String nad;
    private String enableS;
    private String address;
    
    
	//private String log_in_time;
	
	public String getAddress() {
		return address;
	}






	public void setAddress(String address) {
		this.address = address;
	}

	@JsonIgnore
	private Date logInTime;
	
	public String getReligion() {
		return religion;
	}






	public String getEnableS() {
		return enableS;
	}






	public void setEnableS(String enableS) {
		this.enableS = enableS;
	}






	public String getUserImage() {
		return userImage;
	}


	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}


	public void setReligion(String religion) {
		this.religion = religion;
	}

	@JsonIgnore
	private Date logOutTime;
	private String logInTimeText;
	private String logOutTimeText;
	
	private String newPasswordMob;
	private String reenterPasswordMob;

	private String icaId;

	private String secquestion;
	private String secAnswer;
	
    public String getSecquestion() {
		return secquestion;
	}


	public void setSecquestion(String secquestion) {
		this.secquestion = secquestion;
	}


	public String getSecAnswer() {
		return secAnswer;
	}


	public void setSecAnswer(String secAnswer) {
		this.secAnswer = secAnswer;
	}

	
	private String deRegStatus;
	
	private String deRegReason;
	
	
	
    
    public String getDeRegReason() {
		return deRegReason;
	}


	public void setDeRegReason(String deRegReason) {
		this.deRegReason = deRegReason;
	}


	public String getDeRegStatus() {
		return deRegStatus;
	}


	public void setDeRegStatus(String deRegStatus) {
		this.deRegStatus = deRegStatus;

	}


	public String getNewPasswordMob() {
		return newPasswordMob;
	}


	public void setNewPasswordMob(String newPasswordMob) {
		this.newPasswordMob = newPasswordMob;
	}


	public String getReenterPasswordMob() {
		return reenterPasswordMob;
	}


	public void setReenterPasswordMob(String reenterPasswordMob) {
		this.reenterPasswordMob = reenterPasswordMob;
	}


	public String getIcaId() {
		return icaId;
	}


	public void setIcaId(String icaId) {
		this.icaId = icaId;
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


	public String getIsLoggedIn() {
		return isLoggedIn;
	}


	public void setIsLoggedIn(String isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}


	public Date getLogInTime() {
		return logInTime;
	}


	public void setLogInTime(Date logInTime) {
		this.logInTime = logInTime;
	}


	public Date getLogOutTime() {
		return logOutTime;
	}


	public void setLogOutTime(Date logOutTime) {
		this.logOutTime = logOutTime;
	}


	public String getLogInTimeText() {
		return logInTimeText;
	}


	public void setLogInTimeText(String logInTimeText) {
		this.logInTimeText = logInTimeText;
	}


	public String getLogOutTimeText() {
		return logOutTimeText;
	}


	public void setLogOutTimeText(String logOutTimeText) {
		this.logOutTimeText = logOutTimeText;
	}


	public void convert(UserTo bean) {
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
        setEnabled(bean.isEnabled());

  }


	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getSchoolAbbr() {
		return schoolAbbr;
	}

	public void setSchoolAbbr(String schoolAbbr) {
		this.schoolAbbr = schoolAbbr;
	}

	public String getSchoolObjId() {
		return schoolObjId;
	}

	public void setSchoolObjId(String schoolObjId) {
		this.schoolObjId = schoolObjId;
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

	public List<String> getRolesForUser() {
		return rolesForUser;
	}

	public void setRolesForUser(List<String> rolesForUser) {
		this.rolesForUser = rolesForUser;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Integer getMaxAttempts() {
		return MAX_ATTEMPTS;
	}

	@JsonIgnore
	private String reenterPassword;

	@JsonIgnore
	private String newPassword;

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getReenterPassword() {
		return reenterPassword;
	}

	public void setReenterPassword(String reenterPassword) {
		this.reenterPassword = reenterPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getStudentObjectId() {
		return studentObjectId;
	}

	public void setStudentObjectId(String studentObjectId) {
		this.studentObjectId = studentObjectId;
	}

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
	private String corseName;
	private String operation;
	private String role;
	private String acadSession;

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@JsonIgnore
	Course course = new Course();

	public String getCorseName() {
		return corseName;
	}

	public void setCorseName(String corseName) {
		this.corseName = corseName;
	}

	@JsonIgnore
	private List<Role> roles = new AutoPopulatingList<Role>(Role.class);
	// bi-directional many-to-one association to UserRole
	@JsonIgnore
	private List<UserRole> userRoles = new ArrayList<UserRole>();

	private Program program;

	@JsonIgnore
	private List<String> studentTestScores;
	@JsonIgnore
	private List<String> studentAssignmentScores;

	/**
	 * Non persistent attributes
	 */
	@JsonIgnore
	private String oldPassword;
	@JsonIgnore
	private String abbr;
	@JsonIgnore
	private boolean upsert;
	@JsonIgnore
	private ArrayList<String> enrollmentMonths = new ArrayList<String>();
	@JsonIgnore
	private ArrayList<String> enrollmentEndMonths = new ArrayList<String>();

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public ArrayList<String> getEnrollmentMonths() {
		return enrollmentMonths;
	}

	public void setEnrollmentMonths(ArrayList<String> enrollmentMonths) {
		this.enrollmentMonths = enrollmentMonths;
	}

	public ArrayList<String> getEnrollmentEndMonths() {
		return enrollmentEndMonths;
	}

	public void setEnrollmentEndMonths(ArrayList<String> enrollmentEndMonths) {
		this.enrollmentEndMonths = enrollmentEndMonths;
	}

	/**
	 * End of Non persistent attributes
	 */

	public User() {
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

	public boolean getEnabled() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public boolean isUpsert() {
		return upsert;
	}

	public void setUpsert(boolean upsert) {
		this.upsert = upsert;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Program getProgram() {
		return program;
	}

	@JsonIgnore
	public void setProgram(Program program) {
		this.program = program;
	}

	public Long getProgramId() {
		if (null != program) {
			return program.getId();
		}
		return programId;
	}

	public void setProgramId(Long programId) {
		if (null == program) {
			program = new Program();
		}
		program.setId(programId);
		this.programId = programId;
	}

	public List<UserRole> getUserRoles() {
		return this.userRoles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public boolean hasRole(Role role) {
		return roles.contains(role);
	}

	public void setUserRoles(List<UserRole> userRoles) {
		for (UserRole userRole : userRoles) {
			addUserRole(userRole);
		}
	}

	public void updateUserInUserRoles() {
		for (UserRole userRole : userRoles) {
			userRole.setUser(this);
		}
	}

	public UserRole addUserRole(UserRole userRole) {
		getUserRoles().add(userRole);
		userRole.setUser(this);

		return userRole;
	}

	public UserRole removeUserRole(UserRole userRole) {
		getUserRoles().remove(userRole);
		userRole.setUser(null);

		return userRole;
	}

	





	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	public boolean isAccountNonLocked() {
		return attempts < MAX_ATTEMPTS;
	}

	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	public boolean isEnabled() {
		return enabled;
	}

	public Integer getAttempts() {
		return attempts;
	}

	public void setAttempts(Integer attempts) {
		this.attempts = attempts;
	}

	public void addStudentRoles() {
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");

		addUserRole(role);

		role = new UserRole();
		role.setRole("ROLE_STUDENT");

		addUserRole(role);

	}

	public void addFacultyRoles() {
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");

		addUserRole(role);

		role = new UserRole();
		role.setRole("ROLE_FACULTY");

		addUserRole(role);

	}

	public List<String> getStudentTestScores() {
		return studentTestScores;
	}

	public void setStudentTestScores(List<String> studentTestScores) {
		this.studentTestScores = studentTestScores;
	}

	public void setStudentTestScore(String studentTestScore) {
		if (null != studentTestScore && !studentTestScore.isEmpty())
			this.studentTestScores = Arrays.asList(studentTestScore.split(","));
	}

	public List<String> getStudentAssignmentScores() {
		return studentAssignmentScores;
	}

	public void setStudentAssignmentScores(List<String> studentAssignmentScores) {
		this.studentAssignmentScores = studentAssignmentScores;
	}

	public void setStudentAssignmentScore(String studentAssignmentScore) {
		if (null != studentAssignmentScore && !studentAssignmentScore.isEmpty())
			this.studentAssignmentScores = Arrays.asList(studentAssignmentScore
					.split(","));
		;
	}

	@JsonIgnore
	public String getFullname() {
		return firstname + " " + lastname;
	}
	
	private String dept;
	private String acadYear;
	private String courseId;
	private String courseName;
	private Integer count;

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
	private String playerId;

	public String getPlayerId() {
		return playerId;
	}


	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}






	public String getNad() {
		return nad;
	}






	public void setNad(String nad) {
		this.nad = nad;
	}






	@Override
	public String toString() {
		return "User [username=" + username + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", password=" + password
				+ ", rollNo=" + rollNo + ", studentObjectId=" + studentObjectId
				+ ", rolesForUser=" + rolesForUser + ", campusName="
				+ campusName + ", campusId=" + campusId + ", schoolAbbr="
				+ schoolAbbr + ", schoolObjId=" + schoolObjId
				+ ", programName=" + programName + ", religion=" + religion
				+ ", isLoggedIn=" + isLoggedIn + ", userImage=" + userImage
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", nad=" + nad + ", enableS=" + enableS + ", address="
				+ address + ", logInTime=" + logInTime + ", logOutTime="
				+ logOutTime + ", logInTimeText=" + logInTimeText
				+ ", logOutTimeText=" + logOutTimeText + ", newPasswordMob="
				+ newPasswordMob + ", reenterPasswordMob=" + reenterPasswordMob
				+ ", icaId=" + icaId + ", secquestion=" + secquestion
				+ ", secAnswer=" + secAnswer + ", deRegStatus=" + deRegStatus
				+ ", deRegReason=" + deRegReason + ", reenterPassword="
				+ reenterPassword + ", newPassword=" + newPassword
				+ ", enabled=" + enabled + ", enrollmentYear=" + enrollmentYear
				+ ", enrollmentMonth=" + enrollmentMonth + ", validityEndYear="
				+ validityEndYear + ", validityEndMonth=" + validityEndMonth
				+ ", fatherName=" + fatherName + ", motherName=" + motherName
				+ ", programId=" + programId + ", email=" + email + ", mobile="
				+ mobile + ", attempts=" + attempts + ", corseName="
				+ corseName + ", operation=" + operation + ", role=" + role
				+ ", acadSession=" + acadSession + ", type=" + type
				+ ", course=" + course + ", roles=" + roles + ", userRoles="
				+ userRoles + ", program=" + program + ", studentTestScores="
				+ studentTestScores + ", studentAssignmentScores="
				+ studentAssignmentScores + ", oldPassword=" + oldPassword
				+ ", abbr=" + abbr + ", upsert=" + upsert
				+ ", enrollmentMonths=" + enrollmentMonths
				+ ", enrollmentEndMonths=" + enrollmentEndMonths + ", dept="
				+ dept + ", acadYear=" + acadYear + ", courseId=" + courseId
				+ ", courseName=" + courseName + ", count=" + count
				+ ", playerId=" + playerId + "]";
	}
	
	
	
	
}