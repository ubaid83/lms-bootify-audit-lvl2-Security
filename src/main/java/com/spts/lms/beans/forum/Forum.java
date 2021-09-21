package com.spts.lms.beans.forum;

import java.io.Serializable;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

public class Forum extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer replyCount;
	
	private List<String> students;
	
	private Integer studentsInvolved;
	
	private String rollNo;
	
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

	public Integer getStudentsInvolved() {
		return studentsInvolved;
	}

	public void setStudentsInvolved(Integer studentsInvolved) {
		this.studentsInvolved = studentsInvolved;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	private String firstname;
	
	private String dateCreated;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String lastname;

	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	private String question;

	private Long courseId;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Long getCourseId() {
		if (null != course)
			return course.getId();
		return courseId;
	}

	public void setCourseId(Long courseId) {
		if (null != course)
			course.setId(courseId);
		this.courseId = courseId;
	}

	Course course = new Course();

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "Forum [replyCount=" + replyCount + ", students=" + students
				+ ", studentsInvolved=" + studentsInvolved + ", rollNo="
				+ rollNo + ", campusName=" + campusName + ", campusId="
				+ campusId + ", firstname=" + firstname + ", dateCreated="
				+ dateCreated + ", lastname=" + lastname + ", topic=" + topic
				+ ", question=" + question + ", courseId=" + courseId
				+ ", course=" + course + "]";
	}

	



	
	

	

}
