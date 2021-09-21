package com.spts.lms.beans.ica;

import com.spts.lms.beans.BaseBean;

public class IcaComponentQueries extends BaseBean{
	
	private String icaId;
	private String componentId;
	private String username;
	private String query;
	public String getIcaId() {
		return icaId;
	}
	public void setIcaId(String icaId) {
		this.icaId = icaId;
	}
	public String getComponentId() {
		return componentId;
	}
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	@Override
	public String toString() {
		return "IcaComponentQueries [icaId=" + icaId + ", componentId=" + componentId + ", username=" + username
				+ ", query=" + query + "]";
	}
	
	

}
