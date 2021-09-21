package com.spts.lms.beans.forum;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class ForumReplyLike extends BaseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long replyId;
	
	private String username;
	 
	private ForumReply forumReply =  new ForumReply();
	
	private String rollNo;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ForumReply getForumReply() {
		return forumReply;
	}

	public void setForumReply(ForumReply forumReply) {
		this.forumReply = forumReply;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ForumReplyLike [replyId=" + replyId + ", username=" + username
				+ ", forumReply=" + forumReply + ", rollNo=" + rollNo + "]";
	}



}
