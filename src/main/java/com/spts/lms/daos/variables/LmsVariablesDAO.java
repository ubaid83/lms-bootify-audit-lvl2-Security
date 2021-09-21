package com.spts.lms.daos.variables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.BaseDAO;

@Repository("lmsVariablesDAO")
public class LmsVariablesDAO extends BaseDAO<LmsVariables>{
	
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
	
	public LmsVariables getLmsVariableBykeyword(String keyword) {
		String sql = "select * from lms_variables where keyword = ?";
		return findOneSQL(sql, new Object[] { keyword });
	}
	public int updateLmsVariable(Long id, String value, String active) {
		String sql = "update "
				+ getTableName()
				+ " set value=?, lastModifiedDate= SYSDATE(), active=? where id=?;";
		return getJdbcTemplate().update(sql, new Object[] {value,active,id});
	}

}
