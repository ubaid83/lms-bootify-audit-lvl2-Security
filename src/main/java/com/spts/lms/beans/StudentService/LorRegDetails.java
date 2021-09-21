package com.spts.lms.beans.StudentService;

import com.spts.lms.beans.BaseBean;

public class LorRegDetails extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String name;
	private String email;
	private String mobile;
	private String programEnrolledId;
	
	
	private String toeflOrIelts;
	private double toeflScore;
	private double ieltsReadingScore;
	private double ieltsWritingScore;
	private double ieltsSpeakingScore;
	private double ieltsListeningScore;
	
	
	private String lorDocsFilePath;
	private String active;
	private String rollNo;
	private String programName;
	private String acadYear;
	private String expectedDate;
	private String appApproval;
	private String docApproval;
	private String lorApproval;
	private String finalLorFilePath;
	private long lorRegId;
	private long lorRegStaffId;
	
	//New
	private String countryForHigherStudy;
	private String universityName;
	private String programToEnroll;
	private String tentativeDOJ;
	private String competitiveExam;
	private String examScore;
	private String isNmimsPartnerUniversity;
	private String nmimsPartnerUniversity;
	private String examMarksheet;
	private String appRejectionReason;
	private String docRejectionReason;
	private String lorRejectionReason;
	private String lorFormatFilePath;
	private String noOfCopies;
	private String department;
	private String toeflOrIeltsMarksheet;
	
	
	public String getToeflOrIeltsMarksheet() {
		return toeflOrIeltsMarksheet;
	}
	public void setToeflOrIeltsMarksheet(String toeflOrIeltsMarksheet) {
		this.toeflOrIeltsMarksheet = toeflOrIeltsMarksheet;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(String noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public String getLorFormatFilePath() {
		return lorFormatFilePath;
	}
	public void setLorFormatFilePath(String lorFormatFilePath) {
		this.lorFormatFilePath = lorFormatFilePath;
	}
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
	public String getExamMarksheet() {
		return examMarksheet;
	}
	public void setExamMarksheet(String examMarksheet) {
		this.examMarksheet = examMarksheet;
	}
	public String getCountryForHigherStudy() {
		return countryForHigherStudy;
	}
	public void setCountryForHigherStudy(String countryForHigherStudy) {
		this.countryForHigherStudy = countryForHigherStudy;
	}
	public String getUniversityName() {
		return universityName;
	}
	public void setUniversityName(String universityName) {
		this.universityName = universityName;
	}
	public String getProgramToEnroll() {
		return programToEnroll;
	}
	public void setProgramToEnroll(String programToEnroll) {
		this.programToEnroll = programToEnroll;
	}
	public String getTentativeDOJ() {
		return tentativeDOJ;
	}
	public void setTentativeDOJ(String tentativeDOJ) {
		this.tentativeDOJ = tentativeDOJ;
	}
	public String getCompetitiveExam() {
		return competitiveExam;
	}
	public void setCompetitiveExam(String competitiveExam) {
		this.competitiveExam = competitiveExam;
	}
	public String getExamScore() {
		return examScore;
	}
	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}
	public String getIsNmimsPartnerUniversity() {
		return isNmimsPartnerUniversity;
	}
	public void setIsNmimsPartnerUniversity(String isNmimsPartnerUniversity) {
		this.isNmimsPartnerUniversity = isNmimsPartnerUniversity;
	}
	public String getNmimsPartnerUniversity() {
		return nmimsPartnerUniversity;
	}
	public void setNmimsPartnerUniversity(String nmimsPartnerUniversity) {
		this.nmimsPartnerUniversity = nmimsPartnerUniversity;
	}
	public long getLorRegStaffId() {
		return lorRegStaffId;
	}
	public void setLorRegStaffId(long lorRegStaffId) {
		this.lorRegStaffId = lorRegStaffId;
	}
	public long getLorRegId() {
		return lorRegId;
	}
	public void setLorRegId(long lorRegId) {
		this.lorRegId = lorRegId;
	}
	public String getFinalLorFilePath() {
		return finalLorFilePath;
	}
	public void setFinalLorFilePath(String finalLorFilePath) {
		this.finalLorFilePath = finalLorFilePath;
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
	public String getLorApproval() {
		return lorApproval;
	}
	public void setLorApproval(String lorApproval) {
		this.lorApproval = lorApproval;
	}
	public String getRollNo() {
		return rollNo;
	}
	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public String getAcadYear() {
		return acadYear;
	}
	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}
	public String getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(String expectedDate) {
		this.expectedDate = expectedDate;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getProgramEnrolledId() {
		return programEnrolledId;
	}
	public void setProgramEnrolledId(String programEnrolledId) {
		this.programEnrolledId = programEnrolledId;
	}
	
	public String getToeflOrIelts() {
		return toeflOrIelts;
	}
	public void setToeflOrIelts(String toeflOrIelts) {
		this.toeflOrIelts = toeflOrIelts;
	}
	
	
	public double getToeflScore() {
		return toeflScore;
	}
	public void setToeflScore(double toeflScore) {
		this.toeflScore = toeflScore;
	}
	public double getIeltsReadingScore() {
		return ieltsReadingScore;
	}
	public void setIeltsReadingScore(double ieltsReadingScore) {
		this.ieltsReadingScore = ieltsReadingScore;
	}
	public double getIeltsWritingScore() {
		return ieltsWritingScore;
	}
	public void setIeltsWritingScore(double ieltsWritingScore) {
		this.ieltsWritingScore = ieltsWritingScore;
	}
	public double getIeltsSpeakingScore() {
		return ieltsSpeakingScore;
	}
	public void setIeltsSpeakingScore(double ieltsSpeakingScore) {
		this.ieltsSpeakingScore = ieltsSpeakingScore;
	}
	public double getIeltsListeningScore() {
		return ieltsListeningScore;
	}
	public void setIeltsListeningScore(double ieltsListeningScore) {
		this.ieltsListeningScore = ieltsListeningScore;
	}
	public String getLorDocsFilePath() {
		return lorDocsFilePath;
	}
	public void setLorDocsFilePath(String lorDocsFilePath) {
		this.lorDocsFilePath = lorDocsFilePath;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	
	@Override
	public String toString() {
		return "LorRegDetails [username=" + username + ", name=" + name + ", email=" + email + ", mobile=" + mobile
				+ ", programEnrolledId=" + programEnrolledId + ", toeflOrIelts=" + toeflOrIelts + ", toeflScore="
				+ toeflScore + ", ieltsReadingScore=" + ieltsReadingScore + ", ieltsWritingScore=" + ieltsWritingScore
				+ ", ieltsSpeakingScore=" + ieltsSpeakingScore + ", ieltsListeningScore=" + ieltsListeningScore
				+ ", lorDocsFilePath=" + lorDocsFilePath + ", active=" + active + ", rollNo=" + rollNo
				+ ", programName=" + programName + ", acadYear=" + acadYear + ", expectedDate=" + expectedDate
				+ ", appApproval=" + appApproval + ", docApproval=" + docApproval + ", lorApproval=" + lorApproval
				+ ", finalLorFilePath=" + finalLorFilePath + ", lorRegId=" + lorRegId + ", lorRegStaffId="
				+ lorRegStaffId + ", countryForHigherStudy=" + countryForHigherStudy + ", universityName="
				+ universityName + ", programToEnroll=" + programToEnroll + ", tentativeDOJ=" + tentativeDOJ
				+ ", competitiveExam=" + competitiveExam + ", examScore=" + examScore + ", isNmimsPartnerUniversity="
				+ isNmimsPartnerUniversity + ", nmimsPartnerUniversity=" + nmimsPartnerUniversity + ", examMarksheet="
				+ examMarksheet + ", appRejectionReason=" + appRejectionReason + ", docRejectionReason="
				+ docRejectionReason + ", lorRejectionReason=" + lorRejectionReason + ", lorFormatFilePath="
				+ lorFormatFilePath + ", noOfCopies=" + noOfCopies + ", department=" + department
				+ ", toeflOrIeltsMarksheet=" + toeflOrIeltsMarksheet + "]";
	}
	
}
