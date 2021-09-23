package com.spts.lms.beans.user;

import java.io.Serializable;
import java.sql.Date;

import org.springframework.stereotype.Component;

import com.spts.lms.beans.BaseBean;
@Component
public class TrainingProgram extends BaseBean implements Serializable{

private String trainingTitle;
private String date;
private String startTime;
private String endTime;
private String userType;
private String ConductedBy;
private String location;
private String school;
private String programName;
private String campusAbbr;
private String programId;
private String campusId;
private String username;
private String trainingProgramId;




private String encrypted_key;



public String getEncrypted_key() {
	return encrypted_key;
}

public void setEncrypted_key(String encrypted_key) {
	this.encrypted_key = encrypted_key;
}
public String getProgramName() {
	return programName;
}
public void setProgramName(String programName) {
	this.programName = programName;
}
public String getCampusAbbr() {
	return campusAbbr;
}
public void setCampusAbbr(String campusAbbr) {
	this.campusAbbr = campusAbbr;
}
public String getProgramId() {
	return programId;
}
public void setProgramId(String programId) {
	this.programId = programId;
}
public String getCampusId() {
	return campusId;
}
public void setCampusId(String campusId) {
	this.campusId = campusId;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getTrainingProgramId() {
	return trainingProgramId;
}
public void setTrainingProgramId(String trainingProgramId) {
	this.trainingProgramId = trainingProgramId;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public String getSchool() {
	return school;
}
public void setSchool(String school) {
	this.school = school;
}
public String getConductedBy() {
	return ConductedBy;
}
public void setConductedBy(String conductedBy) {
	ConductedBy = conductedBy;
}
public String getTrainingTitle() {
	return trainingTitle;
}
public void setTrainingTitle(String trainingTitle) {
	this.trainingTitle = trainingTitle;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getStartTime() {
	return startTime;
}
public void setStartTime(String startTime) {
	this.startTime = startTime;
}
public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getUserType() {
	return userType;
}
public void setUserType(String userType) {
	this.userType = userType;
}
@Override
public String toString() {
	return "TrainingProgram [trainingTitle=" + trainingTitle + ", date=" + date + ", startTime=" + startTime
			+ ", endTime=" + endTime + ", userType=" + userType + ", ConductedBy=" + ConductedBy + ", location="
			+ location + ", school=" + school + ", programName=" + programName + ", campusAbbr=" + campusAbbr
			+ ", programId=" + programId + ", campusId=" + campusId + ", username=" + username + ", trainingProgramId="
			+ trainingProgramId + ", encrypted_key=" + encrypted_key + "]";
}




}
