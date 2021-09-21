package com.spts.lms.beans.forum;

import java.io.Serializable;
import java.util.List;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.course.Course;

public class ForumReply extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer vote;

	private Integer likes;

	private Integer disLikes;

	private String firstname;

	private String lastname;

	private String repliedDate;

	private String showReportAbuse;

	private String showLike;

	private String showDislike;
	
	private List<String> students;
	
	private String answer;
	
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	private String counterReply;
	public String getCounterReply() {
		return counterReply;
	}

	public void setCounterReply(String counterReply) {
		this.counterReply = counterReply;
	}

	private List<ForumReply> counterReplyList;
	
	private String replyId;
	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public List<ForumReply> getCounterReplyList() {
		return counterReplyList;
	}

	public void setCounterReplyList(List<ForumReply> counterReplyList) {
		this.counterReplyList = counterReplyList;
	}

	private String rollNo;

	public String getRollNo() {
		return rollNo;
	}

	public void setRollNo(String rollNo) {
		this.rollNo = rollNo;
	}

	public List<String> getStudents() {
		return students;
	}

	public void setStudents(List<String> students) {
		this.students = students;
	}

	public String getShowReportAbuse() {
		return showReportAbuse;
	}

	public void setShowReportAbuse(String showReportAbuse) {
		this.showReportAbuse = showReportAbuse;
	}

	public String getShowLike() {
		return showLike;
	}

	public void setShowLike(String showLike) {
		this.showLike = showLike;
	}

	public String getShowDislike() {
		return showDislike;
	}

	public void setShowDislike(String showDislike) {
		this.showDislike = showDislike;
	}

	public String getRepliedDate() {
		return repliedDate;
	}

	public void setRepliedDate(String repliedDate) {
		this.repliedDate = repliedDate;
	}

	public Integer getLikes() {
		return likes;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDisLikes() {
		return disLikes;
	}

	public void setDisLikes(Integer disLikes) {
		this.disLikes = disLikes;
	}

	private String active = "Y";

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	private Integer reportAbuse;

	public Integer getReportAbuse() {
		return reportAbuse;
	}

	public void setReportAbuse(Integer reportAbuse) {
		this.reportAbuse = reportAbuse;
	}

	public Integer getVote() {
		return vote;
	}

	public void setVote(Integer vote) {
		this.vote = vote;
	}

	private Long questionId;

	private Long courseId;

	private String topic;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	private String reply;

	private String question;

	private Forum forum = new Forum();

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Forum getForum() {
		return forum;
	}

	public void setForum(Forum forum) {
		this.forum = forum;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	@Override
	public String toString() {
		return "ForumReply [vote=" + vote + ", likes=" + likes + ", disLikes="
				+ disLikes + ", firstname=" + firstname + ", lastname="
				+ lastname + ", repliedDate=" + repliedDate
				+ ", showReportAbuse=" + showReportAbuse + ", showLike="
				+ showLike + ", showDislike=" + showDislike + ", students="
				+ students + ", answer=" + answer + ", counterReply="
				+ counterReply + ", counterReplyList=" + counterReplyList
				+ ", replyId=" + replyId + ", rollNo=" + rollNo + ", active="
				+ active + ", reportAbuse=" + reportAbuse + ", questionId="
				+ questionId + ", courseId=" + courseId + ", topic=" + topic
				+ ", reply=" + reply + ", question=" + question + ", forum="
				+ forum + ", course=" + course + "]";
	}

	
	

}