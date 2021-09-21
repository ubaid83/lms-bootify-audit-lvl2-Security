package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.daos.BaseDAO;

@Repository("testConfiguartionDAO")
public class TestConfigurationDAO extends BaseDAO<TestConfiguration> {

	@Override
	protected String getTableName() {
		
		return "test_configuration";
	}

	@Override
	protected String getInsertSql() {
		String str = " INSERT INTO "+ getTableName() +" (testId, marks, noOfQuestion, createdBy, createdDate,"
				+ " lastModifiedBy, lastModifiedDate)"
				+ " VALUES (:testId, :marks, :noOfQuestion, :createdBy, :createdDate, :lastModifiedBy,"
				+ " :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE "+ getTableName() +" SET "
				+ " testId=:testId, marks=:marks, noOfQuestion=:noOfQuestion, createdBy=:createdBy,"
				+ " createdDate=:createdDate, lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate,"
				+ " active=:active "
				+ " WHERE id=:id ";
		return str;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TestConfiguration> findAllByTestId(Long testId) {

		String sql = "select * from "+ getTableName() +" where testId = ? ";

		return findAllSQL(sql, new Object[] { testId });

	}
	
	public int deleteByTestId(String testId) {
		final String sql = "Delete from " + getTableName()
				+ " where testId = ? ";

		return getJdbcTemplate().update(sql, new Object[] { testId });
	}
	
}
