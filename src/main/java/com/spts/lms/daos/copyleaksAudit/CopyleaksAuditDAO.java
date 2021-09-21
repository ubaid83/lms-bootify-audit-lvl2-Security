package com.spts.lms.daos.copyleaksAudit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.copyleaksAudit.CopyleaksAudit;
import com.spts.lms.daos.BaseDAO;


@Repository("copyleaksAuditDAO")
public class CopyleaksAuditDAO extends BaseDAO<CopyleaksAudit> {

	@Override
	protected String getTableName() {
		return "copyleaks_audit";
	}

	@Override
	protected String getInsertSql() {
		String sql = " INSERT INTO "+ getTableName() +" (username, assignmentId, count, creditUsed, createdBy,"
				+ " createdDate, lastModifiedBy, lastModifiedDate)"
				+ " VALUES (:username, :assignmentId, :count, :creditUsed, :createdBy, :createdDate,"
				+ " :lastModifiedBy, :lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " UPDATE "+ getTableName() +" SET "
				+ " username = :username, assignmentId = :assignmentId, count = :count,"
				+ " creditUsed = :creditUsed, createdBy = :createdBy, createdDate = :createdDate,"
				+ " lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate "
				+ " WHERE id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public CopyleaksAudit getRecordByUsername(String username, long assignmentId) {

		String sql = " select * from "+ getTableName() +" where username = ? and assignmentId = ? ";

		return findOneSQL(sql, new Object[] { username, assignmentId });
	}

public CopyleaksAudit getRecordByUsernameForModule(String username, List<Long> assignmentId) {

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		
		String sql = " select * from "+ getTableName() +" where username = ? and assignmentId in (:assignmentId) ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				BeanPropertyRowMapper.newInstance(CopyleaksAudit.class));
	}

	public List<CopyleaksAudit> getRecordsByUsername(String username) {
		String sql = " select * from "+ getTableName() +" where username = ? ";
		return findAllSQL(sql, new Object[] { username });
	}
	
	public void deleteRecordById(String id) {
		String sql = " delete from "+ getTableName() +" where id = ? ";
		getJdbcTemplate().update(sql, new Object[] { id });
	}
}
