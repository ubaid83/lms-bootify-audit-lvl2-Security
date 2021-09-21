package com.spts.lms.beans.message;



import java.util.Date;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.group.Groups;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;

public class StudentMessage extends BaseBean {

	/**
	 * The persistent class for the program database table.
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private Long messageId;
	private String facultyId;
	private String username;
	private Long courseId;
	private String subject;
	private String description;
	private Integer acadYear;
	private String acadMonth;
	private String messageReply;
	private String messageRepliedBy;
	public Date messageRepliedDate;
	private String forumReply;
	private Long replyId;
	private String rollNo;
	private String firstname;
	private String lastname;
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
	

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}
	
	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

	public String getForumReply() {
		return forumReply;
	}

	public void setForumReply(String forumReply) {
		this.forumReply = forumReply;
	}
	
	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}

	public String getMessageReply() {
		return messageReply;
	}

	public void setMessageReply(String messageReply) {
		this.messageReply = messageReply;
	}

	public String getMessageRepliedBy() {
		return messageRepliedBy;
	}

	public void setMessageRepliedBy(String messageRepliedBy) {
		this.messageRepliedBy = messageRepliedBy;
	}

	

	public Date getMessageRepliedDate() {
		return messageRepliedDate;
	}

	public void setMessageRepliedDate(Date messageRepliedDate) {
		this.messageRepliedDate = messageRepliedDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private User user = new User();
	
	public String getCourseName() {
		return course.getCourseName();
	}

	public void setCourseName(String courseName) {
		course.setCourseName(courseName);
	}

	public String getFirstname() {
		return user.getFirstname();
	}

	public void setFirstname(String firstname) {
		user.setFirstname(firstname);
	}

	public String getLastname() {
		return user.getLastname();
	}

	public void setLastname(String lastname) {
		user.setLastname(lastname);
	}
	public String getProgramName() {
		return program.getProgramName();
	}

	public void setProgramName(String programName) {
		program.setProgramName(programName);
	}
	
	private Message message = new Message();
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Groups getGroups() {
		return groups;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	private Course course  = new Course();
	 private Groups groups = new Groups();
	private Program program = new Program();

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

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "StudentMessage [messageId=" + messageId + ", facultyId="
				+ facultyId + ", username=" + username + ", courseId="
				+ courseId + ", subject=" + subject + ", description="
				+ description + ", acadYear=" + acadYear + ", acadMonth="
				+ acadMonth + ", messageReply=" + messageReply
				+ ", messageRepliedBy=" + messageRepliedBy
				+ ", messageRepliedDate=" + messageRepliedDate
				+ ", forumReply=" + forumReply + ", replyId=" + replyId
				+ ", rollNo=" + rollNo + ", firstname=" + firstname
				+ ", lastname=" + lastname + ", campusName=" + campusName
				+ ", campusId=" + campusId + ", user=" + user + ", message="
				+ message + ", course=" + course + ", groups=" + groups
				+ ", program=" + program + "]";
	}

	

	

	
	
	

	

}
