package com.spts.lms.beans.report;

import com.spts.lms.beans.BaseBean;

public class UtilityReport extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String campus;
	private String campusId;
	private String dept;
	private String acadYear;
	private String username;
	private String fullname;
	private String userType;
	private String facultyType;
	private String program;
	private String programName;
	private String acadSession;
	private String courseId;
	private String courseName;
	private String moduleId;
	private String moduleName;
	private String features;
	private String totalUse;

	private String srno;
	private String studentsHit;
	private String students;
	private String totalStudent;
	private String studentUsagePerc;
	private String adminsHit;
	private String admins;
	private String totalAdmin;
	private String adminUsagePerc;
	private String vFacultiesHit;
	private String vFaculties;
	private String totalVFaculty;
	private String vFacultyUsagePerc;
	private String cFacultiesHit;
	private String cFaculties;
	private String totalCFaculty;
	private String cFacultyUsagePerc;

	// new fields

	private int noOfUsersByRole;
	private int noOfUsedByRole;
	private double usagePercentage;

	private int noOfHitByStudent;
	private int noOfHitByFaculty;
	private int noOfHitByPFaculty;
	private int noOfHitByVFaculty;
	
	private String name;
	private String featuresUsed;
	
	private int noOfHitByAdmin;
	

	public int getNoOfHitByAdmin() {
		return noOfHitByAdmin;
	}

	public void setNoOfHitByAdmin(int noOfHitByAdmin) {
		this.noOfHitByAdmin = noOfHitByAdmin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFeaturesUsed() {
		return featuresUsed;
	}

	public void setFeaturesUsed(String featuresUsed) {
		this.featuresUsed = featuresUsed;
	}

	@Override
	public String toString() {
		return "UtilityReport [campus=" + campus + ", campusId=" + campusId
				+ ", dept=" + dept + ", acadYear=" + acadYear + ", username="
				+ username + ", fullname=" + fullname + ", userType="
				+ userType + ", facultyType=" + facultyType + ", program="
				+ program + ", programName=" + programName + ", acadSession="
				+ acadSession + ", courseId=" + courseId + ", courseName="
				+ courseName + ", moduleId=" + moduleId + ", moduleName="
				+ moduleName + ", features=" + features + ", totalUse="
				+ totalUse + ", srno=" + srno + ", studentsHit=" + studentsHit
				+ ", students=" + students + ", totalStudent=" + totalStudent
				+ ", studentUsagePerc=" + studentUsagePerc + ", adminsHit="
				+ adminsHit + ", admins=" + admins + ", totalAdmin="
				+ totalAdmin + ", adminUsagePerc=" + adminUsagePerc
				+ ", vFacultiesHit=" + vFacultiesHit + ", vFaculties="
				+ vFaculties + ", totalVFaculty=" + totalVFaculty
				+ ", vFacultyUsagePerc=" + vFacultyUsagePerc
				+ ", cFacultiesHit=" + cFacultiesHit + ", cFaculties="
				+ cFaculties + ", totalCFaculty=" + totalCFaculty
				+ ", cFacultyUsagePerc=" + cFacultyUsagePerc
				+ ", noOfUsersByRole=" + noOfUsersByRole + ", noOfUsedByRole="
				+ noOfUsedByRole + ", usagePercentage=" + usagePercentage
				+ ", noOfHitByStudent=" + noOfHitByStudent
				+ ", noOfHitByFaculty=" + noOfHitByFaculty
				+ ", noOfHitByPFaculty=" + noOfHitByPFaculty
				+ ", noOfHitByVFaculty=" + noOfHitByVFaculty + ", name=" + name
				+ ", featuresUsed=" + featuresUsed + ", noOfHitByAdmin="
				+ noOfHitByAdmin + "]";
	}

	public int getNoOfUsersByRole() {
		return noOfUsersByRole;
	}

	public void setNoOfUsersByRole(int noOfUsersByRole) {
		this.noOfUsersByRole = noOfUsersByRole;
	}

	public int getNoOfUsedByRole() {
		return noOfUsedByRole;
	}

	public void setNoOfUsedByRole(int noOfUsedByRole) {
		this.noOfUsedByRole = noOfUsedByRole;
	}

	public double getUsagePercentage() {
		return usagePercentage;
	}

	public void setUsagePercentage(double usagePercentage) {
		this.usagePercentage = usagePercentage;
	}

	public int getNoOfHitByStudent() {
		return noOfHitByStudent;
	}

	public void setNoOfHitByStudent(int noOfHitByStudent) {
		this.noOfHitByStudent = noOfHitByStudent;
	}

	public int getNoOfHitByFaculty() {
		return noOfHitByFaculty;
	}

	public void setNoOfHitByFaculty(int noOfHitByFaculty) {
		this.noOfHitByFaculty = noOfHitByFaculty;
	}

	public int getNoOfHitByPFaculty() {
		return noOfHitByPFaculty;
	}

	public void setNoOfHitByPFaculty(int noOfHitByPFaculty) {
		this.noOfHitByPFaculty = noOfHitByPFaculty;
	}

	public int getNoOfHitByVFaculty() {
		return noOfHitByVFaculty;
	}

	public void setNoOfHitByVFaculty(int noOfHitByVFaculty) {
		this.noOfHitByVFaculty = noOfHitByVFaculty;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getFacultyType() {
		return facultyType;
	}

	public void setFacultyType(String facultyType) {
		this.facultyType = facultyType;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
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

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getTotalUse() {
		return totalUse;
	}

	public void setTotalUse(String totalUse) {
		this.totalUse = totalUse;
	}

	public String getSrno() {
		return srno;
	}

	public void setSrno(String srno) {
		this.srno = srno;
	}

	public String getStudentsHit() {
		return studentsHit;
	}

	public void setStudentsHit(String studentsHit) {
		this.studentsHit = studentsHit;
	}

	public String getStudents() {
		return students;
	}

	public void setStudents(String students) {
		this.students = students;
	}

	public String getTotalStudent() {
		return totalStudent;
	}

	public void setTotalStudent(String totalStudent) {
		this.totalStudent = totalStudent;
	}

	public String getStudentUsagePerc() {
		return studentUsagePerc;
	}

	public void setStudentUsagePerc(String studentUsagePerc) {
		this.studentUsagePerc = studentUsagePerc;
	}

	public String getAdminsHit() {
		return adminsHit;
	}

	public void setAdminsHit(String adminsHit) {
		this.adminsHit = adminsHit;
	}

	public String getAdmins() {
		return admins;
	}

	public void setAdmins(String admins) {
		this.admins = admins;
	}

	public String getTotalAdmin() {
		return totalAdmin;
	}

	public void setTotalAdmin(String totalAdmin) {
		this.totalAdmin = totalAdmin;
	}

	public String getAdminUsagePerc() {
		return adminUsagePerc;
	}

	public void setAdminUsagePerc(String adminUsagePerc) {
		this.adminUsagePerc = adminUsagePerc;
	}

	public String getvFacultiesHit() {
		return vFacultiesHit;
	}

	public void setvFacultiesHit(String vFacultiesHit) {
		this.vFacultiesHit = vFacultiesHit;
	}

	public String getvFaculties() {
		return vFaculties;
	}

	public void setvFaculties(String vFaculties) {
		this.vFaculties = vFaculties;
	}

	public String getTotalVFaculty() {
		return totalVFaculty;
	}

	public void setTotalVFaculty(String totalVFaculty) {
		this.totalVFaculty = totalVFaculty;
	}

	public String getvFacultyUsagePerc() {
		return vFacultyUsagePerc;
	}

	public void setvFacultyUsagePerc(String vFacultyUsagePerc) {
		this.vFacultyUsagePerc = vFacultyUsagePerc;
	}

	public String getcFacultiesHit() {
		return cFacultiesHit;
	}

	public void setcFacultiesHit(String cFacultiesHit) {
		this.cFacultiesHit = cFacultiesHit;
	}

	public String getcFaculties() {
		return cFaculties;
	}

	public void setcFaculties(String cFaculties) {
		this.cFaculties = cFaculties;
	}

	public String getTotalCFaculty() {
		return totalCFaculty;
	}

	public void setTotalCFaculty(String totalCFaculty) {
		this.totalCFaculty = totalCFaculty;
	}

	public String getcFacultyUsagePerc() {
		return cFacultyUsagePerc;
	}

	public void setcFacultyUsagePerc(String cFacultyUsagePerc) {
		this.cFacultyUsagePerc = cFacultyUsagePerc;
	}

}
