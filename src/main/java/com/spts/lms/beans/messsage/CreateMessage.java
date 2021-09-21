package com.spts.lms.beans.messsage;

import org.apache.commons.lang3.StringUtils;

import com.spts.lms.beans.BaseBean;

public class CreateMessage extends BaseBean {

	/**
	 * The persistent class for the program database table.
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private Long inbox_id;
	private String from_username;
	private String to_username;
	private String created_on;
	private String subject;
	private String body;
	private String is_read;
	private String is_trash;
	private String is_star;
	private String is_draft;
	private String full_user_name;
	private String from_email_id;
	private String to_email_id;

	@Override
	public String toString() {
		return "CreateMessage [inbox_id=" + inbox_id + ", from_username="
				+ from_username + ", to_username=" + to_username
				+ ", created_on=" + created_on + ", subject=" + subject
				+ ", body=" + body + ", is_read=" + is_read + ", is_trash="
				+ is_trash + ", is_star=" + is_star + ", is_draft=" + is_draft
				
				+ ", from_email_id=" + from_email_id + ", to_email_id="
				+ to_email_id + "]";
	}

	public String getFull_user_name() {
		return full_user_name;
	}

	public void setFull_user_name(String full_user_name) {
		this.full_user_name = full_user_name;
	}

	public Long getInbox_id() {
		return inbox_id;
	}

	public void setInbox_id(Long inbox_id) {
		this.inbox_id = inbox_id;
	}

	public String getFrom_username() {
		return from_username;
	}

	public void setFrom_username(String from_username) {
		this.from_username = from_username;
	}

	public String getTo_username() {
		return to_username;
	}

	public void setTo_username(String to_username) {
		this.to_username = to_username;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getIs_read() {
		return is_read;
	}

	public void setIs_read(String is_read) {
		this.is_read = is_read;
	}

	public String getIs_trash() {
		return is_trash;
	}

	public void setIs_trash(String is_trash) {
		this.is_trash = is_trash;
	}

	public String getIs_star() {
		return is_star;
	}

	public void setIs_star(String is_star) {
		this.is_star = is_star;
	}

	public String getIs_draft() {
		return is_draft;
	}

	public void setIs_draft(String is_draft) {
		this.is_draft = is_draft;
	}

	public String getFrom_email_id() {
		return from_email_id;
	}

	public void setFrom_email_id(String from_email_id) {
		this.from_email_id = from_email_id;
	}

	public String getTo_email_id() {
		return to_email_id;
	}

	public void setTo_email_id(String to_email_id) {
		this.to_email_id = to_email_id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
