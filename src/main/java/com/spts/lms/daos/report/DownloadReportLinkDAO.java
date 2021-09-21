package com.spts.lms.daos.report;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.report.DownloadReportLinks;
import com.spts.lms.daos.BaseDAO;

@Repository("downloadReportLinkDAO")
public class DownloadReportLinkDAO extends BaseDAO<DownloadReportLinks> {

	@Override
	protected String getTableName() {

		return "download_report_links";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "+ getTableName()+" (reportName,reportLink) values"
				+ "(:reportName , :reportLink)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {

		String sql = "update   "+ getTableName()
				+ " set reportName=:reportName, "
				+ " reportLink=:reportLink, "
				 + " where reportName=:reportName and reportLink=:reportLink";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	public List<DownloadReportLinks> findAllForAdmin(){
		String sql = " select * from "+getTableName()+" where reportName like '%ADMIN%' ";
		return findAllSQL(sql, new Object[]{});
	}
	
	public List<DownloadReportLinks> findAllForFaculty(){
		String sql = " select * from "+getTableName()+" where reportName not like '%ADMIN%' ";
		return findAllSQL(sql, new Object[]{});
	}

}
