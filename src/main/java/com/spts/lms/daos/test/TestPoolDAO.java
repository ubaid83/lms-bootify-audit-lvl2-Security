package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.TestPool;
import com.spts.lms.daos.BaseDAO;

@Repository("testPoolDAO")
public class TestPoolDAO extends BaseDAO<TestPool> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "test_pool";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO " + getTableName() + " (testPoolName,courseId,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate,moduleId,isCreatedByAdmin)"

				+ " VALUES(:testPoolName,:courseId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:moduleId,:isCreatedByAdmin)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set " + " testPoolName = :testPoolName,"
				+ " courseId =:courseId,"

				+ " lastModifiedBy = :lastModifiedBy," + " lastModifiedDate =:lastModifiedDate "

				+ " where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TestPool> findAllTestPoolsByUser(String username,String role) {

		if ("ROLE_FACULTY".equals(role)) {
			String sql = " select tp.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession from " + getTableName()
					+ " tp,course c where tp.courseId=c.id and tp.active = 'Y' and tp.createdBy = ?";

			return findAllSQL(sql, new Object[] { username });
		} else {
			String sql = " select tp.*,c.moduleName as courseName,c.acadMonth,c.acadYear,c.acadSession from "
					+ getTableName()
					+ " tp,course c where tp.moduleId=c.moduleId and tp.active = 'Y' and tp.createdBy = ? group by tp.id";
			return findAllSQL(sql, new Object[] { username });
		}
	}

	public List<TestPool> findAllTestPoolsByUserAndCourse(String username, String courseId) {

		String sql = " select tp.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession from " + getTableName()
				+ " tp,course c where tp.courseId=c.id and tp.active = 'Y' and tp.createdBy = ? and tp.courseId = ? ";

		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<TestPool> findAllTestPoolsByUserAndSameMarks(String username, double marks,String role) {

		if("ROLE_FACULTY".equals(role)) {
		String sql = " select tp.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession from "+ getTableName() +" tp,course c,"
				+ " test_question_pools tqp where tp.courseId=c.id and tp.id = tqp.testPoolId and tp.active = 'Y' and tqp.active = 'Y' and tqp.marks = ? and tp.createdBy = ? group by tp.id";
		
		return findAllSQL(sql, new Object[]{marks, username});
		
		}else {
			String sql = " select tp.*,c.moduleName as courseName,c.acadMonth,c.acadYear,c.acadSession from "+ getTableName() +" tp,course c,"
					+ " test_question_pools tqp where tp.moduleId=c.moduleId and tp.id = tqp.testPoolId and tp.active = 'Y' and tqp.active = 'Y' and tqp.marks = ? and tp.createdBy = ? group by tp.id";
			
			return findAllSQL(sql, new Object[]{marks, username});
		}
	}

	public List<TestPool> findAllTestPoolsByUserAndDiffMarks(String username, String marks,String role) {

		if("ROLE_FACULTY".equals(role)) {
			String sql = " select tp.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession from "+ getTableName() +" tp,course c,"
					+ " test_question_pools tqp where tp.courseId=c.id and tp.id = tqp.testPoolId and tp.active = 'Y' and tqp.active = 'Y' and tqp.marks in ( "+ marks +" ) and tp.createdBy = ? group by tp.id";
			
			return findAllSQL(sql, new Object[]{ username });
			}else {
				String sql = " select tp.*,c.moduleName as courseName,c.acadMonth,c.acadYear,c.acadSession from "+ getTableName() +" tp,course c,"
						+ " test_question_pools tqp where tp.moduleId=c.moduleId and tp.id = tqp.testPoolId and tp.active = 'Y' and tqp.active = 'Y' and tqp.marks in ( "+ marks +" ) and tp.createdBy = ? group by tp.id";
				
				return findAllSQL(sql, new Object[]{ username });
			}
	}

	public List<TestPool> findAllTestPoolsByUserForAdmin(String username) {

		String sql = " select tp.*,c.moduleName as courseName,c.acadMonth,c.acadYear,c.acadSession from "
				+ getTableName()
				+ " tp,course c where tp.moduleId=c.moduleId and tp.active = 'Y' and tp.createdBy = ? group by tp.id ";

		return findAllSQL(sql, new Object[] { username });
	}

	/* For Assignment Pool Start */
	public List<TestPool> findAllTestPoolsByUserAdminForAssignment(long assignmentId, String username) {
		String sql = " select tp.* from "+getTableName()+" tp,course c,assignment a where a.courseId=c.id and tp.moduleId = c.moduleId and a.id = ? and tp.createdBy = ? ";
		return findAllSQL(sql, new Object[] {assignmentId, username });
	}
	
	public List<TestPool> findAllTestPoolsByAssignmentId(String assignmentId, String username) {
		String sql = " select tp.*,c.moduleName as courseName,c.acadMonth,c.acadYear,c.acadSession from "+ getTableName() +" tp,course c,"
				+ " assignment_pool_configuration apc where tp.moduleId=c.moduleId and tp.id = apc.testPoolId and tp.active = 'Y' and apc.active = 'Y' and apc.assignmentId = ? and tp.createdBy = ? group by tp.id";
		return findAllSQL(sql, new Object[]{ assignmentId, username });
	}
	/* For Assignment Pool End */
	
	//New Pool Changes
	public List<TestPool> findAllTestPoolsByUserAndTestPoolConfig(String username) {
		
		String sql = " select tp.*,c.courseName,c.acadMonth,c.acadYear,c.acadSession,tqp.marks,count(tqp.marks) as countl,tpc.noOfQuestion as noq from test_question_pools tqp,test_pool tp,test_pool_configuration tpc,course c "
				+ "where tp.courseId=c.id and tp.id = tqp.testPoolId and tp.id = tpc.testPoolId and tqp.marks=tpc.marks "
				+ "and tp.createdBy = ? and tp.active = 'Y' and tqp.active = 'Y' group by tp.id,tqp.marks having countl > noq";
		
		return findAllSQL(sql, new Object[]{ username });
	}
	//New Pool Changes
}
