package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.daos.BaseDAO;

@Repository("studentTestAuditDAO")
public class StudentTestAuditDAO extends BaseDAO<StudentTest> {

	@Override
	protected String getTableName() {
		return "student_test_audit";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO  "+ getTableName() +"(username,testId,attempt,testStartTime,testEndTime,testCompleted,score,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,courseId)"
				+ " VALUES(:username,:testId,0,:testStartTime,:testEndTime,:testCompleted,"
				+ ":score,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:courseId) "
				+ " ON DUPLICATE KEY UPDATE "
				+ " score=:score,testStartTime = :testStartTime, testEndTime = :testEndTime,"
				+ "testCompleted = :testCompleted ,  courseId=:courseId, attempt=:attempt, lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate";
	}
	
	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	@Override
	protected String getUpdateSql() {
		final String sql = "Update "+ getTableName() +" set "
				+ "score=:score,testStartTime = :testStartTime, testEndTime = :testEndTime,"
				+ "testCompleted = :testCompleted ,  courseId=:courseId, attempt=:attempt, "
				+ "lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate " 
				+ " where testId = :testId and username =:username order by createdDate desc limit 1 ";
		return sql;
	}
	
	public int insertStudentTest(StudentTest st){
		SqlParameterSource parameterSource = getParameterSource(st);
		final String sql ="INSERT INTO "+ getTableName() +"(username,testId,attempt,testStartTime,testEndTime,testCompleted,score,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,courseId)"
				+ " VALUES(:username,:testId,:attempt,:testStartTime,:testEndTime,:testCompleted,"
				+ ":score,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:courseId)";
		
		return getNamedParameterJdbcTemplate().update(sql, parameterSource);
		
	}
	
	
public List<StudentTest> findByTestIdAndUsername(Long testId,String username){
		
		
		
		String sql =" select * from "+getTableName()+" where testId = ? and username = ? ";
		
		return findAllSQL(sql, new Object[]{testId,username});
	}
	
}
