package com.spts.lms.daos.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.TestQuestionPools;
import com.spts.lms.daos.BaseDAO;

@Repository("testQuestionPoolsDAO")
public class TestQuestionPoolsDAO extends BaseDAO<TestQuestionPools> {

	@Override
	protected String getTableName() {
		return "test_question_pools";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO test_question_pools(testPoolId,description,type,testType,marks,option1,option2,option3,option4,option5,option6,option7,option8,"
				+ "correctOption,active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,optionShuffle,questionType,answerRangeFrom,answerRangeTo)"
				+ " VALUES(:testPoolId,:description,:type,:testType,:marks,:option1,:option2,:option3,:option4,:option5,:option6,:option7,:option8,"
				+ ":correctOption,'Y',:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:optionShuffle,:questionType,:answerRangeFrom,:answerRangeTo)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update "
				+ getTableName()
				+ " set description = :description,marks = :marks,option1 = :option1,option2 = :option2,option3 = :option3,"
				+ " option4 = :option4,option5 = :option5,option6 = :option6,option7 = :option7,option8 = :option8,"
				+ " optionShuffle=:optionShuffle,questionType=:questionType,answerRangeFrom=:answerRangeFrom,answerRangeTo=:answerRangeTo, "
				+ " correctOption = :correctOption, type= :type,testType=:testType, lastModifiedBy = :lastModifiedBy,lastModifiedDate = :lastModifiedDate where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<TestQuestionPools> findByTestPoolId(String testPoolId) {

		String sql = " select * from " + getTableName()
				+ " where testPoolId = ? and active = 'Y' ";

		return findAllSQL(sql, new Object[] { testPoolId });
	}

	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestType(
			String testId, String testPoolId, String testType) {

		/*
		 * String sql = " select * from " + getTableName() +
		 * " where testPoolId = ? and testType = ? ";
		 */

		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp "

				+ " left outer join test_question tq on tqp.id=tq.testPoolId "
				+ " and tq.testId=? "
				+ " where tqp.testPoolId = ? and tqp.testType = ? and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { testId, testPoolId, testType });
	}

	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolId(String testPoolId){
		String sql = " select tqp.* from test_question_pools tqp "

				
				+ " where tqp.testPoolId = ? and tqp.active = 'Y' ";
		
		return findAllSQL(sql, new Object[] {  testPoolId });
		
	}
	public List<TestQuestionPools> findAllByTestPoolIdAndQuestionType(String testPoolId,String questionType){
		
		
		
		String sql = " select * from "+ getTableName()+" where testPoolId = ?  and questionType = ? and active = 'Y' " ;
		
		
		return findAllSQL(sql, new Object[]{testPoolId,questionType});
	}
	
	public List<Map<String, Object>> findByListOfIds(List<String> listOfIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("listOfIds", listOfIds);
		String sql = " select * from " + getTableName()
				+ " where id in(:listOfIds)";

		try {

			return getNamedParameterJdbcTemplate().queryForList(sql, params);
		} catch (Exception ex) {
			return null;
		}

	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolId(
			String testId, String testPoolId) {

		/*
		 * String sql = " select * from " + getTableName() +
		 * " where testPoolId = ? and testType = ? ";
		 */

		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp "

				+ " left outer join test_question tq on tqp.id=tq.testPoolId "
				+ " and tq.testId=? "
				+ " where tqp.testPoolId = ? and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { testId, testPoolId });
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndMarks(
			String testId, String testPoolId, double marks) {


		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp "

				+ " left outer join test_question tq on tqp.id=tq.testPoolId "
				+ " and tq.testId=? "
				+ " where tqp.testPoolId = ? and tqp.marks = ? and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { testId, testPoolId, marks });
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestTypeAndMarks(
			String testId, String testPoolId, String testType, double marks) {

		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp "

				+ " left outer join test_question tq on tqp.id=tq.testPoolId "
				+ " and tq.testId=? "
				+ " where tqp.testPoolId = ? and tqp.testType = ? and tqp.marks = ? and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { testId, testPoolId, testType, marks });
	}

	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndMarksList(
			String testId, String testPoolId, String marks) {


		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp "

				+ " left outer join test_question tq on tqp.id=tq.testPoolId "
				+ " and tq.testId=? "
				+ " where tqp.testPoolId = ? and tqp.marks in ("+ marks +") and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { testId, testPoolId });
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestTypeAndMarksList(
			String testId, String testPoolId, String testType, String marks) {

		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp "

				+ " left outer join test_question tq on tqp.id=tq.testPoolId "
				+ " and tq.testId=? "
				+ " where tqp.testPoolId = ? and tqp.testType = ? and tqp.marks  in ("+ marks +") and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { testId, testPoolId, testType });
	}

	/* For Assignment Pool Start */
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestTypeForAssignment(
			String assignmentId, String testPoolId, String questionType) {

		String sql = " select tqp.*,aq.id as assignmentQuestionId from test_question_pools tqp "

				+ " left outer join assignment_question aq on tqp.id=aq.testQuestionPoolId "
				+ " and aq.assignmentId=? "
				+ " where tqp.testPoolId = ? and tqp.questionType = ? and tqp.active = 'Y' ";

		return findAllSQL(sql, new Object[] { assignmentId, testPoolId, questionType });
	}
	
	//New Pool Changes
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestPoolConfigurationAndTestType(
			String testId, String testPoolId, String testType) {

		String sql = " select tqp.*,tq.id as testQuestionId from test_question_pools tqp left outer join test_question tq on tqp.id=tq.testPoolId "
				+ "and tq.testId=? left outer join test_pool_configuration tpc on tpc.testPoolId = tqp.testPoolId "
				+ "where tqp.testPoolId = ? and tqp.testType = ? and tqp.marks = tpc.marks and tqp.active = 'Y' group by id,testQuestionId";

		return findAllSQL(sql, new Object[] { testId, testPoolId, testType });
	}
	//New Pool Changes

}
