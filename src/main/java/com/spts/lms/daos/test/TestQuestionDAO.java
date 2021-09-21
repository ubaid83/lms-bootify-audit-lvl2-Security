package com.spts.lms.daos.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.TestQuestion;
import com.spts.lms.daos.BaseDAO;

@Repository("testQuestionDAO")
public class TestQuestionDAO extends BaseDAO<TestQuestion> {

	@Override
	protected String getTableName() {
		return "test_question";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO test_question(testId,description,type,marks,option1,option2,option3,option4,option5,option6,option7,option8,"
				+ "correctOption,active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,optionShuffle,questionType,answerRangeFrom,answerRangeTo,testPoolId)"
				+ " VALUES(:testId,:description,:type,:marks,:option1,:option2,:option3,:option4,:option5,:option6,:option7,:option8,"
				+ ":correctOption,'Y',:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:optionShuffle,:questionType,:answerRangeFrom,:answerRangeTo,:testPoolId)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update "
				+ getTableName()
				+ " set description = :description,marks = :marks,option1 = :option1,option2 = :option2,option3 = :option3,"
				+ " option4 = :option4,option5 = :option5,option6 = :option6,option7 = :option7,option8 = :option8,"
				+ " optionShuffle=:optionShuffle,testPoolId=:testPoolId,questionType=:questionType,answerRangeFrom=:answerRangeFrom,answerRangeTo=:answerRangeTo,testPoolId=:testPoolId, "
				+ " correctOption = :correctOption, type= :type, lastModifiedBy = :lastModifiedBy,lastModifiedDate = :lastModifiedDate where id = :id "
				+ " and not exists (select id from student_test st where st.testId = test_question.testId and st.testStartTime is not null)";
		return sql;
	}

	public int getSumOfTestQuestnMarksByTestId(Long id) {
		String sql = " select sum(marks) from test_question where testId=? ";
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	public double getSumOfTestQuestionMarksByTestId(Long id) {
		String sql = " select sum(marks) from test_question where testId=? ";
		return getJdbcTemplate().queryForObject(sql, Double.class,
				new Object[] { id });
	}

	public int findTestIdByTestQuestionId(String testQuestionId) {
		String sql = "select testId from test_question where id=?";
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { testQuestionId });
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<TestQuestion> findByTestId(Long testId) {
		final String sql = "Select *  from " + getTableName()
				+ " where testId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { testId });
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsername(
			Long testId, String username) {
		/*
		 * final String sql =
		 * "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
		 * + getTableName() + " tq , student_question_response sqr " +
		 * " where  sqr.username = ? and testId = ? and sqr.questionId = tq.id  and active='Y'"
		 * ;
		 */
		final String sql = "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
				+ getTableName()
				+ " tq left outer join student_question_response sqr "
				+ " on sqr.questionId = tq.id and sqr.username = ? where testId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { username, testId });
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsernameOnlyAttemptedQuestions(
			Long testId, String username) {
		final String sql = "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
				+ getTableName()
				+ " tq inner join student_question_response sqr "
				+ " on sqr.questionId = tq.id and sqr.username = ? where testId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { username, testId });
	}

	public List<TestQuestion> findResponseByTestIdAndUsernameToEvaluate(
			Long testId, String username) {
		final String sql = "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
				+ getTableName()
				+ " tq , student_question_response sqr "
				+ " where  sqr.username = ? and testId = ? and sqr.questionId = tq.id  and active='Y'";

		return findAllSQL(sql, new Object[] { username, testId });
	}

	public int deleteByTestId(String testId) {
		final String sql = "Delete from " + getTableName()
				+ " where testId = ? ";

		return getJdbcTemplate().update(sql, new Object[] { testId });
	}

	public List<TestQuestion> findTestDetailsQuestionWise(Long testId) {
		final String sql = "select sqr.username,sqr.answer,sqr.marks as studentMarks,tq.description,tq.answerRangeFrom,tq.answerRangeTo,"
				+ " tq.correctOption,sqr.questionId,tq.option1,tq.option2,tq.option3,tq.option4,tq.option5,tq.option6,tq.option7,tq.option8"
				+ " from student_question_response sqr,test_question tq,test t,student_test st, user_roles ur "
				+ " where tq.id=sqr.questionId and t.id=st.testId and sqr.studentTestId=st.id and st.username = ur.username "
				+ " and ur.role = 'ROLE_STUDENT' and t.id= ?  ";

		return findAllSQL(sql, new Object[] { testId });
	}

	public List<TestQuestion> findTestDetailsQuestionWiseByUsername(
			Long testId, String username) {
		final String sql = "select sqr.username,sqr.answer,sqr.marks as studentMarks,tq.description,tq.answerRangeFrom,tq.answerRangeTo,"
				+ " tq.correctOption,sqr.questionId,tq.option1,tq.option2,tq.option3,tq.option4,tq.option5,tq.option6,tq.option7,tq.option8,tq.questionType,tq.type"
				+ " from student_question_response sqr,test_question tq,test t,student_test st"
				+ " where tq.id=sqr.questionId and t.id=st.testId and sqr.studentTestId=st.id and t.id= ? and sqr.username = ? ";

		return findAllSQL(sql, new Object[] { testId, username });
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsernameForIncompleteTest(
			Long testId, String username) {
		/*
		 * final String sql =
		 * "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
		 * + getTableName() + " tq , student_question_response sqr " +
		 * " where  sqr.username = ? and testId = ? and sqr.questionId = tq.id  and active='Y'"
		 * ;
		 */
		final String sql = "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
				+ getTableName()
				+ " tq inner join student_question_response sqr "
				+ " on sqr.questionId = tq.id and sqr.username = ? where testId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { username, testId });
	}

	public int updateTestQuestionsAfterTest(TestQuestion testQuestion) {
		final String sql = "Update "
				+ getTableName()
				+ " set description = :description,marks = :marks,option1 = :option1,option2 = :option2,option3 = :option3,"
				+ " option4 = :option4,option5 = :option5,option6 = :option6,option7 = :option7,option8 = :option8,"
				+ " optionShuffle=:optionShuffle,answerRangeFrom=:answerRangeFrom,answerRangeTo=:answerRangeTo, "
				+ " correctOption = :correctOption, type= :type, lastModifiedBy = :lastModifiedBy,lastModifiedDate = :lastModifiedDate where id = :id ";
		// return executeUpdateSql(sql, new Object[] { testQuestion });
		return updateSQL(testQuestion, sql);
	}

	public List<TestQuestion> findTestDetailsQuestionWiseByUsernameAttemptWise(
			Long testId, String username) {
		final String sql = "select sqr.username,sqr.attempts as attempts,sqr.answer,sqr.marks as studentMarks,tq.description,tq.answerRangeFrom,tq.answerRangeTo,"
				+ " tq.correctOption,sqr.questionId,tq.option1,tq.option2,tq.option3,tq.option4,tq.option5,tq.option6,tq.option7,tq.option8"
				+ " from student_question_response_audit sqr,test_question tq,test t,student_test st"
				+ " where tq.id=sqr.questionId and t.id=st.testId and sqr.studentTestId=st.id and t.id= ? and sqr.username = ? ";

		return findAllSQL(sql, new Object[] { testId, username });
	}

	public List<TestQuestion> findTestDetailsQuestionWiseAndAttemptWise(
			Long testId) {
		final String sql = "select sqr.username,sqr.attempts as attempts,sqr.answer,sqr.marks as studentMarks,tq.description,tq.answerRangeFrom,tq.answerRangeTo,"
				+ " tq.correctOption,sqr.questionId,tq.option1,tq.option2,tq.option3,tq.option4,tq.option5,tq.option6,tq.option7,tq.option8"
				+ " from student_question_response_audit sqr,test_question tq,test t,student_test st, user_roles ur"
				+ " where tq.id=sqr.questionId and t.id=st.testId and sqr.studentTestId=st.id and st.username = ur.username "
				+ " and ur.role = 'ROLE_STUDENT' and t.id= ? ";

		return findAllSQL(sql, new Object[] { testId });
	}

	public List<TestQuestion> findResponseByTestIdAndUsernameToEvaluateForMix(
			Long testId, String username) {
		final String sql = "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
				+ getTableName()
				+ " tq , student_question_response sqr "
				+ " where  sqr.username = ? and testId = ? and sqr.questionId = tq.id and tq.questionType = 'Descriptive' and active='Y'";

		return findAllSQL(sql, new Object[] { username, testId });
	}

	public int updateStudentMarks(TestQuestion testQuestion) {

		double answerRangeF = Double.parseDouble(testQuestion
				.getAnswerRangeFrom());
		double answerRangeT = Double.parseDouble(testQuestion
				.getAnswerRangeTo());
		final String sql = " Update student_question_response "

		+ " set marks = :marks where questionId = :id "
				+ " and (answer between " + answerRangeF + " and "
				+ answerRangeT + ") ";
		// return executeUpdateSql(sql, new Object[] { testQuestion });
		return updateSQL(testQuestion, sql);
	}

	public int updateStudentMarksAudit(TestQuestion testQuestion) {

		double answerRangeF = Double.parseDouble(testQuestion
				.getAnswerRangeFrom());
		double answerRangeT = Double.parseDouble(testQuestion
				.getAnswerRangeTo());

		final String sql = " Update student_question_response_audit "

		+ " set marks = :marks where questionId = :id "
				+ " and (answer between " + answerRangeF + " and "
				+ answerRangeT + ") ";
		// return executeUpdateSql(sql, new Object[] { testQuestion });
		return updateSQL(testQuestion, sql);
	}

	public int updateStudentMarksToZero(TestQuestion testQuestion) {

		double answerRangeF = Double.parseDouble(testQuestion
				.getAnswerRangeFrom());
		double answerRangeT = Double.parseDouble(testQuestion
				.getAnswerRangeTo());

		final String sql = " Update student_question_response "

		+ " set marks = 0 where questionId = :id "
				+ " and (answer not between " + answerRangeF + " and "
				+ answerRangeT + ") ";
		// return executeUpdateSql(sql, new Object[] { testQuestion });
		return updateSQL(testQuestion, sql);
	}

	public int updateStudentMarksToZeroAudit(TestQuestion testQuestion) {

		double answerRangeF = Double.parseDouble(testQuestion
				.getAnswerRangeFrom());
		double answerRangeT = Double.parseDouble(testQuestion
				.getAnswerRangeTo());

		final String sql = " Update student_question_response_audit "

		+ " set marks = 0 where questionId = :id "
				+ " and (answer not between " + answerRangeF + " and "
				+ answerRangeT + ") ";
		// return executeUpdateSql(sql, new Object[] { testQuestion });
		return updateSQL(testQuestion, sql);
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(
			Long testId, String username) {
		/*
		 * final String sql =
		 * "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
		 * + getTableName() + " tq , student_question_response sqr " +
		 * " where  sqr.username = ? and testId = ? and sqr.questionId = tq.id  and active='Y'"
		 * ;
		 */
		final String sql = "Select tq.*, sqr.answer, sqr.username, sqr.marks as studentMarks from "
				+ getTableName()
				+ " tq inner join student_question_response_temp sqr "
				+ " on sqr.questionId = tq.id and sqr.username = ? where testId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { username, testId });
	}
	
	public List<TestQuestion> getQuestionCount( Long testId) {
		 String sql = "select * from test_question where testId = ? and active = 'Y'";
		return findAllSQL(sql, new Object[] {testId });
}

	public List<TestQuestion> findTestCreatedByAdminQuestionWise(String testId,List<String> courseIds) {
		Map<String,Object> mapper = new HashMap<>();
		
		mapper.put("testId",testId);
		mapper.put("courseIds",courseIds);
		
		final String sql = "select sqr.username,sqr.answer,sqr.marks as studentMarks,tq.description,tq.answerRangeFrom,tq.answerRangeTo,"
				+ " tq.correctOption,sqr.questionId,tq.option1,tq.option2,tq.option3,tq.option4,tq.option5,tq.option6,tq.option7,tq.option8"
				+ " from student_question_response sqr,test_question tq,test t,student_test st, user_roles ur "
				+ " where tq.id=sqr.questionId and t.id=st.testId and sqr.studentTestId=st.id and st.username = ur.username "
				+ " and ur.role = 'ROLE_STUDENT' and t.id= :testId and st.courseId in(:courseIds)  ";

		return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(TestQuestion.class));
	}
	public List<TestQuestion> findTestByAdminDetailsQuestionWiseAndAttemptWise(
			
			String testId,List<String> courseIds) {
		Map<String,Object> mapper = new HashMap<>();
		
		mapper.put("testId",testId);
		mapper.put("courseIds",courseIds);
		final String sql = "select sqr.username,sqr.attempts as attempts,sqr.answer,sqr.marks as studentMarks,tq.description,tq.answerRangeFrom,tq.answerRangeTo,"
				+ " tq.correctOption,sqr.questionId,tq.option1,tq.option2,tq.option3,tq.option4,tq.option5,tq.option6,tq.option7,tq.option8"
				+ " from student_question_response_audit sqr,test_question tq,test t,student_test st, user_roles ur"
				+ " where tq.id=sqr.questionId and t.id=st.testId and sqr.studentTestId=st.id and st.username = ur.username "
				+ " and ur.role = 'ROLE_STUDENT' and t.id= :testId and st.courseId in(:courseIds) ";

		return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(TestQuestion.class));
	}
	//New Pool Changes
	public List<TestQuestion> getQuestionsByPoolConfiguration(long testId,long testPoolId,double marks){
		String sql = "select tq.* from test_question tq,test_question_pools tqp,test_pool_configuration tpc where "
				+ "tq.testId = ? and tq.testPoolId = tqp.id and tqp.testPoolId = ? and tqp.testPoolId = tpc.testPoolId "
				+ "and tq.marks = ? group by tq.id";
		return findAllSQL(sql, new Object[] { testId, testPoolId, marks});
	}
}
