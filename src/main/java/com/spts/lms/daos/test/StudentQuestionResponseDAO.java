package com.spts.lms.daos.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("studentQuestionResponseDAO")
public class StudentQuestionResponseDAO extends
		BaseDAO<StudentQuestionResponse> {

	@Override
	protected String getTableName() {
		return "student_question_response";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO student_question_response(username,studentTestId,questionId,answer,marks,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:username,:studentTestId,:questionId,:answer,:marks,:createdBy,:createdDate,"
				+ ":lastModifiedBy,:lastModifiedDate) "
				+ " ON DUPLICATE KEY UPDATE "
				+ " marks= :marks , answer = :answer, lastModifiedBy = :lastModifiedBy ,lastModifiedDate = :lastModifiedDate";
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public StudentQuestionResponse findByStudentUsernameAndTestQuestnId(
			String username, String questnId) {
		String sql = " select * from student_question_response where username=? and questionId=?";
		return findOneSQL(sql, new Object[] { username, questnId });
	}

	public List<StudentQuestionResponse> findByStudentTestId(String studTestId) {
		String sql = "select * from " + getTableName()
				+ " where studentTestId=?";
		return findAllSQL(sql, new Object[] { studTestId });
	}

	public List<StudentQuestionResponse> findByQId(String qId) {
		String sql = "select * from " + getTableName() + " where questionId=?";
		return findAllSQL(sql, new Object[] { qId });
	}

	public StudentQuestionResponse findTotalAnsweredQueByStudentTestId(
			String studTestId) {
		String sql = "select count(*) as totalQuestionAttempted from student_question_response where studentTestId = ? and (answer is not null and answer <> '') ";
		return findOneSQL(sql, new Object[] { studTestId });
	}
	
	public StudentQuestionResponse findTotalAnsweredTempQueByStudentTestId(
			String studTestId) {
		String sql = "select count(*) as totalQuestionAttempted from student_question_response_temp where studentTestId = ? and (answer is not null and answer <> '') ";
		return findOneSQL(sql, new Object[] { studTestId });
	}
	
	

	public int deleteFacultyTestResponse(String testId, String username) {
		String sql = " delete from " + getTableName()
				+ "  where questionId in(select id from test_question "
				+ " where testId = ?  ) and username = ? ";
		// SqlParameterSource parameterSource = getParameterSource(bean);
		int updated = getJdbcTemplate().update(sql,
				new Object[] { testId, username });
		return updated;
	}

	public int insertIntoResponseTemp(StudentQuestionResponse bean) {

		bean.setCreatedDate(Utils.getInIST());
		bean.setLastModifiedDate(Utils.getInIST());
		String sql = "INSERT INTO student_question_response_temp(username,studentTestId,questionId,answer,marks,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:username,:studentTestId,:questionId,:answer,:marks,:createdBy,:createdDate,"
				+ ":lastModifiedBy,:lastModifiedDate) "
				+ " ON DUPLICATE KEY UPDATE "
				+ " marks= :marks , answer = :answer, lastModifiedBy = :lastModifiedBy ,lastModifiedDate = :lastModifiedDate";

		SqlParameterSource parameterSource = getParameterSource(bean);
		int updated = getNamedParameterJdbcTemplate().update(sql,
				parameterSource);
		return updated;
	}

	public int[] insertBatchIntoTemp(final List<StudentQuestionResponse> beans)

	{
		String sql = "INSERT INTO student_question_response_temp(username,studentTestId,questionId,answer,marks,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:username,:studentTestId,:questionId,:answer,:marks,:createdBy,:createdDate,"
				+ ":lastModifiedBy,:lastModifiedDate) "
				+ " ON DUPLICATE KEY UPDATE "
				+ " marks= :marks , answer = :answer, lastModifiedBy = :lastModifiedBy ,lastModifiedDate = :lastModifiedDate";
		return executeSQLBatch(beans, sql, true);
	}
	
	public int[] deleteResponseTempBatch(final List<StudentQuestionResponse> beans) {
		final String sql = "delete from student_question_response_temp where id = :id";
		return executeSQLBatch(beans, sql,false);
	}
	
	private int[] executeSQLBatch(final List<StudentQuestionResponse> beans, final String sql,
			final boolean isInsert) {
		List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>(
				beans.size());
		for (StudentQuestionResponse bean : beans) {
			bean.setCreatedDate(Utils.getInIST());
			bean.setLastModifiedDate(Utils.getInIST());
			parameters.add(getParameterSource(bean));
		}
		SqlParameterSource[] param = parameters
				.toArray(new SqlParameterSource[parameters.size()]);

		return getNamedParameterJdbcTemplate().batchUpdate(sql, param);
	}
	
	
	
	public List<StudentQuestionResponse> findByStudentTestIdTemp(String studTestId) {
        
        String sql = "select * from student_question_response_temp"
                                        + " where studentTestId=?";
        return findAllSQL(sql, new Object[] { studTestId });
	}
	
	public List<StudentQuestionResponse> findByQIdNew(String qId) {
		String sql = "select sqr.* from " + getTableName() + " sqr, student_test st where sqr.studentTestId = st.id and sqr.questionId=? and st.testCompleted = 'Y' ";
		return findAllSQL(sql, new Object[] { qId });
	}
	
	public void updateStudentQuesRespMarks(Double marks,Long id){
        String sql = "update student_question_response set marks =? where id = ?";
        getJdbcTemplate().update(sql,
                    new Object[] { marks, id });
  }


}
