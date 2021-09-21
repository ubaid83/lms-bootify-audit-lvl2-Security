package com.spts.lms.beans.weight;

import java.io.Serializable;
import java.util.List;

import com.spts.lms.beans.BaseBean;

public class WeightData extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String acadSession;
	private String acadYear;
	private String courseName;
	private Long courseId;
	private String weightType;
	private String weightAssigned;
	private String semiFinalScore;
	private String count;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSemiFinalScore() {
		return semiFinalScore;
	}

	public void setSemiFinalScore(String semiFinalScore) {
		this.semiFinalScore = semiFinalScore;
	}

	private String component1;
	private String component2;
	private String component3;
	private String component4;
	private String component5;
	private String ica;
	private String tee;
	private String passIca;
	private String total;
	private String studentUsername;

	public String getStudentUsername() {
		return studentUsername;
	}

	public void setStudentUsername(String studentUsername) {
		this.studentUsername = studentUsername;
	}

	public String getComponent1() {
		return component1;
	}

	public void setComponent1(String component1) {
		this.component1 = component1;
	}

	public String getComponent2() {
		return component2;
	}

	public void setComponent2(String component2) {
		this.component2 = component2;
	}

	public String getComponent3() {
		return component3;
	}

	public void setComponent3(String component3) {
		this.component3 = component3;
	}

	public String getComponent4() {
		return component4;
	}

	public void setComponent4(String component4) {
		this.component4 = component4;
	}

	public String getComponent5() {
		return component5;
	}

	public void setComponent5(String component5) {
		this.component5 = component5;
	}

	public String getIca() {
		return ica;
	}

	public void setIca(String ica) {
		this.ica = ica;
	}

	public String getTee() {
		return tee;
	}

	public void setTee(String tee) {
		this.tee = tee;
	}

	public String getPassIca() {
		return passIca;
	}

	public void setPassIca(String passIca) {
		this.passIca = passIca;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	public String getWeightAssigned() {
		return weightAssigned;
	}

	public void setWeightAssigned(String weightAssigned) {
		this.weightAssigned = weightAssigned;
	}

	private String internal;
	private Long weightId;

	public Long getWeightId() {
		return weightId;
	}

	public void setWeightId(Long weightId) {
		this.weightId = weightId;
	}

	public String getInternal() {
		return internal;
	}

	public void setInternal(String internal) {
		this.internal = internal;
	}

	public String getExternal() {
		return external;
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String external;

	public String getAcadSession() {
		return acadSession;
	}

	public void setAcadSession(String acadSession) {
		this.acadSession = acadSession;
	}

	public String getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(String acadYear) {
		this.acadYear = acadYear;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "WeightData [acadSession=" + acadSession + ", acadYear="
				+ acadYear + ", courseName=" + courseName + ", courseId="
				+ courseId + ", weightType=" + weightType + ", weightAssigned="
				+ weightAssigned + ", semiFinalScore=" + semiFinalScore
				+ ", count=" + count + ", component1=" + component1
				+ ", component2=" + component2 + ", component3=" + component3
				+ ", component4=" + component4 + ", component5=" + component5
				+ ", ica=" + ica + ", tee=" + tee + ", passIca=" + passIca
				+ ", total=" + total + ", studentUsername=" + studentUsername
				+ ", internal=" + internal + ", weightId=" + weightId
				+ ", external=" + external + "]";
	}

}
