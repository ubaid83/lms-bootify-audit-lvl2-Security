package com.spts.lms.beans.instituteCycle;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;

/**
 * The persistent class for the InstituteCycle database table.
 * 
 */
public class InstituteCycle extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer year;
	private String month;
	private String live;
	private InstituteCycleType cycleType;
	
	public enum InstituteCycleType {
		ACADS, EXAMS;
	}

	public InstituteCycle() {
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getLive() {
		return live;
	}

	public void setLive(String live) {
		this.live = live;
	}

	public String getCycleType() {
		return this.cycleType == null ? null : this.cycleType.toString();
	}

	public void setCycleType(String cycleType) {
		if(!StringUtils.isEmpty(cycleType)) {
			this.cycleType = InstituteCycleType.valueOf(cycleType);
		}
	}

	public void setCycleType(InstituteCycleType cycleType) {
		this.cycleType = cycleType;
	}
	

	
	
}