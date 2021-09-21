package com.spts.lms.beans.course;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.mortbay.util.ajax.JSON;

import com.google.api.client.json.Json;
import com.spts.lms.beans.BaseBean;

public class CourseOverview extends BaseBean implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	public String programId;

	public Long courseId;
	
	public String lecture;

	public String practical;

	public String tutorial;

	public String credit;

	public String ica;

	public String tee;

	public String preRequisite;

	public String objectives;

	public String outcomes;

	public InstituteSessionType sessionType;
	
	public String code;
	


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public enum InstituteSessionType {
		TRIMESTER, SEMESTER;
	}
	
	public String getSessionType() {
		return this.sessionType == null ? null : this.sessionType.toString();
	}

	public void setSessionType(String sessionType) {
		if(!StringUtils.isEmpty(sessionType)) {
			this.sessionType = InstituteSessionType.valueOf(sessionType);
		}
	}

	public void setSessionType(InstituteSessionType sessionType) {
		this.sessionType = sessionType;
	}

public String getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(String preRequisite) {
		this.preRequisite = preRequisite;
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(String outcomes) {
		this.outcomes = outcomes;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getLecture() {
		return lecture;
	}

	public void setLecture(String lecture) {
		this.lecture = lecture;
	}

	public String getPractical() {
		return practical;
	}

	public void setPractical(String practical) {
		this.practical = practical;
	}

	public String getTutorial() {
		return tutorial;
	}

	public void setTutorial(String tutorial) {
		this.tutorial = tutorial;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "CourseOverview [programId=" + programId + ", courseId="
				+ courseId + ", lecture=" + lecture + ", practical="
				+ practical + ", tutorial=" + tutorial + ", credit=" + credit
				+ ", ica=" + ica + ", tee=" + tee + ", preRequisite="
				+ preRequisite + ", objectives=" + objectives + ", outcomes="
				+ outcomes + ", sessionType=" + sessionType + ", code=" + code
				+ "]";
	}

	

}
