package com.spts.lms.beans.variables;

import com.spts.lms.beans.BaseBean;

public class LmsVariables extends BaseBean{

	private String keyword;
	private String value;
	private String active;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "LmsVariables [keyword=" + keyword + ", value=" + value + ", active=" + active + "]";
	}
	
	
}
