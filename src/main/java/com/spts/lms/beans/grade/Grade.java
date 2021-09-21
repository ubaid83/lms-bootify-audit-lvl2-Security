package com.spts.lms.beans.grade;

import java.util.HashMap;
import java.util.Map;

public class Grade {

	public Grade() {
		super();
		assigmentToScore = new HashMap<String, String>();
		testToScore = new HashMap<String, String>();
		cpToScore = new HashMap<String, String>(); 
		statusMap = new HashMap<String,String>();
		offlineToScore = new HashMap<String,String>();
	}

	public Map<String, String> getCpToScore() {
		return cpToScore;
	}

	public void setCpToScore(Map<String, String> cpToScore) {
		this.cpToScore = cpToScore;
	}

	String name;
	Map<String, String> assigmentToScore;
	Map<String, String> testToScore;
	Map<String, String> cpToScore;
	Map<String,String> statusMap;
	
	Map<String, String> offlineToScore;
	public Map<String, String> getOfflineToScore() {
			return offlineToScore;
		}

		public void setOfflineToScore(Map<String, String> offlineToScore) {
			this.offlineToScore = offlineToScore;
		}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAssigmentToScore() {
		return assigmentToScore;
	}

	public void setAssigmentToScore(Map<String, String> assigmentToScore) {
		this.assigmentToScore = assigmentToScore;
	}

	public Map<String, String> getTestToScore() {
		return testToScore;
	}

	public void setTestToScore(Map<String, String> testToScore) {
		this.testToScore = testToScore;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}
	

}
