package com.spts.lms.beans.test;

import com.spts.lms.beans.BaseBean;

public class StudentOfflineTest extends BaseBean{
	
	private static final long serialVersionUID = 1067122965896106688L;
	
	private String offlineTestId;
	private String username;
	private Double score;
	public String getOfflineTestId() {
		return offlineTestId;
	}
	public void setOfflineTestId(String offlineTestId) {
		this.offlineTestId = offlineTestId;
	}
		public String getUsername() {
			return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "StudentOfflineTest [offlineTestId=" + offlineTestId
				+ ", username=" + username + ", score=" + score + "]";
	}
	
	
	

}
