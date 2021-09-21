package com.spts.lms.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.MultipleDBReference;

@Repository("multipleDBReferenceDAO")
public class MultipleDBReferenceDAO extends BaseDAO<MultipleDBReference> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "multiple_db_references";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into " + getTableName() + " ( url,username,"
				+ " password,dbName) values"
				+ "(:url,:username,:password, :dbName)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " update "
				+ getTableName()
				+ " set url=:url, username=:username , password=:password, dbName=:dbName "
				+ " where url=:url and username=:username and password=:password and dbName=:dbName ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	
	public List<MultipleDBReference> findByLMSDb(){
		
		String sql = " select * from "+ getTableName() +" where dbName like 'lms_%'";
		return findAllSQL(sql, new Object[]{});
	}
	
	public MultipleDBReference findByDBName(String dbName){
		
		String sql = " select * from "+ getTableName() +" where dbName = ? ";
		return findOneSQL(sql, new Object[]{dbName});
	}
}
