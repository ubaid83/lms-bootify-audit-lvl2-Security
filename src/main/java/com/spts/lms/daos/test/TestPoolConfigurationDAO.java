package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.beans.test.TestPoolConfiguration;
import com.spts.lms.daos.BaseDAO;

@Repository("testPoolConfiguartionDAO")
public class TestPoolConfigurationDAO extends BaseDAO<TestPoolConfiguration>{

	
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "test_pool_configuration";
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		String str = " INSERT INTO "+ getTableName() +" (testId, testPoolId, marks, noOfQuestion, createdBy, createdDate,"
				+ " lastModifiedBy, lastModifiedDate)"
				+ " VALUES (:testId, :testPoolId, :marks, :noOfQuestion, :createdBy, :createdDate, :lastModifiedBy,"
				+ " :lastModifiedDate) ";
		return str;
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
	public int deleteByTestId(String testId) {
		final String sql = "Delete from " + getTableName()
				+ " where testId = ? ";

		return getJdbcTemplate().update(sql, new Object[] { testId });
	}
	public List<TestPoolConfiguration> findAllByTestId(Long testId) {

		String sql = "select * from "+ getTableName() +" where testId = ? ";

		return findAllSQL(sql, new Object[] { testId });

	}
}
