package com.spts.lms.daos.report;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.report.UtilityReport;
import com.spts.lms.daos.BaseDAO;

@Repository("utilityReportDAO")
public class UtilityReportDAO extends BaseDAO<UtilityReport> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<UtilityReport> getDetailedUtilityReport(String campusId,
			String fromDate, String toDate) {

		String sql = " call  detailed_utility_report(?,?,?); ";
		return findAllSQL(sql, new Object[] { campusId, fromDate, toDate });

	}

	public List<UtilityReport> getSummaryUtilityReport(String campusId,
			String fromDate, String toDate) {

		if ("null".equals(campusId)) {
			String sql = " call  summary_utility_report( null,?,?); ";
			return findAllSQL(sql, new Object[] { fromDate, toDate });
		} else {
			String sql = " call  summary_utility_report(?,?,?); ";
			return findAllSQL(sql, new Object[] { campusId, fromDate, toDate });
		}

	}

	public List<Map<String, Object>> getSummaryUtilityReportMap(
			String campusId, String startDate, String endDate) {

		String sql = " call summary_utility_report(?,?,?) ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { campusId, startDate, endDate });
	}

	public List<Map<String, Object>> getDetailedReport1(String campusId,
			String startDate, String endDate) {

		String sql = " call detailed_utility_report_alt1(?,?,?) ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { campusId, startDate, endDate });
	}

	public List<Map<String, Object>> getDetailedReport2(String campusId,
			String startDate, String endDate) {

		String sql = " call detailed_utility_report_alt(?,?,?) ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { campusId, startDate, endDate });
	}
}
