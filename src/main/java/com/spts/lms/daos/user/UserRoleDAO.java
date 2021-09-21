package com.spts.lms.daos.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.daos.BaseDAO;

@Repository
public class UserRoleDAO extends BaseDAO<UserRole> {

	@Override
	protected String getTableName() {
		return "user_roles";
	}
	
	@Override
	protected BeanPropertySqlParameterSource getParameterSource(UserRole bean) {
		BeanPropertySqlParameterSource sqlParameterSource = super.getParameterSource(bean);
		sqlParameterSource.registerSqlType("role", Types.VARCHAR);
		return sqlParameterSource;
	}

	@Override
	protected String getInsertSql() {
		return "Insert into "+getTableName()+"(username, role, createdDate, createdBy, "
				+ "lastModifiedDate, lastModifiedBy) values(:username, :role, :createdDate, :createdBy, "
				+ ":lastModifiedDate, :lastModifiedBy)";
	}
	
	public void softDeleteByUsername(String username) {
		
		 executeUpdateSql("update user_roles set active='N' where username= ? ", new Object[]{username});
		}
	

	@Override
	protected String getUpdateSql() {
		return null;
	}
	
	public UserRole findRoleByUsername(final String username){
		String sql =" select * from user_roles where username= ? and role<>'ROLE_USER' ";
		return findOneSQL(sql, new Object[]{username});
	}
	
	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "+getTableName()
				+"(username, role, createdDate, createdBy, lastModifiedDate, lastModifiedBy) "
				
				+ "values "
				
				+ "(:username, :role, :createdDate, :createdBy, :lastModifiedDate, :lastModifiedBy)"

				+ " ON DUPLICATE KEY UPDATE "
				
				+ " lastModifiedDate = :lastModifiedDate,"
				+ " lastModifiedBy = :lastModifiedBy";
		return sql;
	}
	public UserRole findAdmin(){
		 String sql = "select username from user_roles where role =  'ROLE_ADMIN' ";
				
		//return findOneSQL(sql, new Object[]{Role.ROLE_ADMIN.name()});
		return findOneSQL(sql, new Object[]{});
		
	}
	
	public List<Role> findRolesByUsername(final String username) {
		final String sql = "Select role from "+getTableName()+" where username = ?";
		return getJdbcTemplate().query(sql, new Object[] {username}, new RowMapper<Role>(){

			public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				return Role.valueOf(resultSet.getString("role"));
			}
			
		});
	}
	public List<UserRole> findRoles(){
		String sql = "Select distinct role from "+getTableName() +" where role <> 'ROLE_USER'";
		return findAllSQL(sql, new Object[]{});
	}
	
	public List<UserRole> findUsersByRole(String role){
		String sql = "select distinct * from "+ getTableName() + " where role = ? and active = 'Y'";
		return findAllSQL(sql, new Object[]{role});
	}


}