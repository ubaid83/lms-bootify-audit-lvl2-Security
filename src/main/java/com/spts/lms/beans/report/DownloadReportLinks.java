package com.spts.lms.beans.report;

import com.spts.lms.beans.BaseBean;

public class DownloadReportLinks extends BaseBean{
	
	private String reportName;
	private String reportLink;
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportLink() {
		return reportLink;
	}
	public void setReportLink(String reportLink) {
		this.reportLink = reportLink;
	}

}
