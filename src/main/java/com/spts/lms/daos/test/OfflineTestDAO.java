package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.OfflineTest;
import com.spts.lms.daos.BaseDAO;


@Repository("offlineTestDao")
public class OfflineTestDAO extends BaseDAO<OfflineTest>{

	
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "offline_test";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (testName,courseId,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:testName,:courseId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " testName = :testName," 
				
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate"

			

				+ " where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public List<OfflineTest> findAllOfflineTestsByUsernameAndCourseId(String username,
			String courseId) {

		String sql = " select ot.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession from "
				+ getTableName()
				+ " ot,course c where ot.courseId=c.id and ot.active = 'Y' and ot.createdBy = ? and ot.courseId = ? ";

		return findAllSQL(sql, new Object[] { username, courseId });
	}
	
	
		public List<OfflineTest> findAllOfflineTestsByUsername(String username) {

		String sql = " select ot.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession from "
				+ getTableName()
				+ " ot,course c where ot.courseId=c.id and ot.active = 'Y' and ot.createdBy = ?";

		return findAllSQL(sql, new Object[] { username });
	}
	
}
