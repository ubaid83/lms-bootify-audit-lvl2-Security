package com.spts.lms.beans.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class Message extends BaseBean {
	private static final long serialVersionUID = 1L;
	private Long courseId;
	private String facultyId;
	private Integer acadYear;
	private String acadMonth;
	private String noOfStudents;
	private String subject;
	private String description;
	private String idForCourse;
	private String messageReply;
	private String questionId;
	private String forumReply;
	private String campusName;
	private Long campusId;

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

	private String replyId;
	private String programName;
	private String rollNo;
	private String firstname;
	private String lastname;

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

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public String getForumReply() {
		return forumReply;
	}

	public void setForumReply(String forumReply) {
		this.forumReply = forumReply;
	}

	

    public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getMessageReply() {
		return messageReply;
	}

	public void setMessageReply(String messageReply) {
		this.messageReply = messageReply;
	}

	public Date getMessageReplyTimeStamp() {
                    return messageReplyTimeStamp;
    }

    public void setMessageReplyTimeStamp(Date messageReplyTimeStamp) {
                    this.messageReplyTimeStamp = messageReplyTimeStamp;
    }

    public Date messageReplyTimeStamp;
	

	private List<String> students = new ArrayList<String>();
	
	public List<String> getStudents() {
		return students;
	}

	

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getIdForCourse() {
		return idForCourse;
	}

	public void setIdForCourse(String idForCourse) {
		this.idForCourse = idForCourse;
	}

	private Course course = new Course();

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public Integer getAcadYear() {
		return acadYear;
	}

	public void setAcadYear(Integer acadYear) {
		this.acadYear = acadYear;
	}

	public String getAcadMonth() {
		return acadMonth;
	}

	public void setAcadMonth(String acadMonth) {
		this.acadMonth = acadMonth;
	}

	public String getNoOfStudents() {
		return noOfStudents;
	}

	public void setNoOfStudents(String noOfStudents) {
		this.noOfStudents = noOfStudents;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Message [courseId=" + courseId + ", facultyId=" + facultyId
				+ ", acadYear=" + acadYear + ", acadMonth=" + acadMonth
				+ ", noOfStudents=" + noOfStudents + ", subject=" + subject
				+ ", description=" + description + ", idForCourse="
				+ idForCourse + ", messageReply=" + messageReply
				+ ", questionId=" + questionId + ", forumReply=" + forumReply
				+ ", campusName=" + campusName + ", campusId=" + campusId
				+ ", replyId=" + replyId + ", programName=" + programName
				+ ", rollNo=" + rollNo + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", messageReplyTimeStamp="
				+ messageReplyTimeStamp + ", students=" + students
				+ ", course=" + course + "]";
	}


	

	

	

	


}
