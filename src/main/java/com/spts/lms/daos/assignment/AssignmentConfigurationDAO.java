package com.spts.lms.daos.assignment;

import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.AssignmentConfiguration;
import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.daos.BaseDAO;

@Repository("assignmentConfiguartionDAO")
public class AssignmentConfigurationDAO extends BaseDAO<AssignmentConfiguration>{

	@Override
	protected String getTableName() {
		return "assignment_configuration";
		
	}

	@Override
	protected String getInsertSql() {
		String str = " INSERT INTO "+ getTableName() +" (assignmentId, marks, questionNumber, createdBy, createdDate,"
				+ " lastModifiedBy, lastModifiedDate)"
				+ " VALUES (:assignmentId, :marks, :questionNumber, :createdBy, :createdDate, :lastModifiedBy,"
				+ " :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE "+ getTableName() +" SET "
				+ " assignmentId=:assignmentId, marks=:marks, questionNumber=:questionNumber, createdBy=:createdBy,"
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

	public List<AssignmentConfiguration> findAllByAssignmentId(Long assignmentId) {

		String sql = "select * from "+ getTableName() +" where assignmentId = ? and active='Y'";

		return findAllSQL(sql, new Object[] { assignmentId });

	}
	
	public int deleteByAssignmentId(String assignmentId) {
		final String sql = "Delete from " + getTableName()
				+ " where assignmentId = ? ";

		return getJdbcTemplate().update(sql, new Object[] { assignmentId });
	}
	
	
	//For Assignment Pool Start
	public int insertAssignmentPoolConfiguration(AssignmentConfiguration ac) {

		SqlParameterSource parameterSource = getParameterSource(ac);
		final String sql = "INSERT INTO assignment_pool_configuration(assignmentId,testPoolId,noOfQuestion,active,"
				+ " createdBy,createdDate,lastModifiedBy,lastModifiedDate) "
				+ "VALUES (:assignmentId,:testPoolId,:noOfQuestion,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
		return getNamedParameterJdbcTemplate().update(sql, parameterSource);
	}
	
	public int deleteByAssignmentPoolConfigurationAssignmentId(String assignmentId) {
		final String sql = "Delete from assignment_pool_configuration where assignmentId = ? ";
		return getJdbcTemplate().update(sql, new Object[] { assignmentId });
	}
	
	public List<AssignmentConfiguration> getAllPoolConfigByAssignmentId(long assignmentId){
		String sql = "select * from assignment_pool_configuration where assignmentId = ? and active = 'Y'";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public List<AssignmentConfiguration> findPoolByAssignmentId(Long assignmentId) {
		String sql = "select * from assignment_pool_configuration where assignmentId = ? and active='Y'";

		return findAllSQL(sql, new Object[] { assignmentId });
	}
}
