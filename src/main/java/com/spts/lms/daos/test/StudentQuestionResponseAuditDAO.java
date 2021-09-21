package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.beans.test.StudentQuestionResponseAudit;
import com.spts.lms.daos.BaseDAO;

@Repository("studentQuestionResponseAuditDAO")
public class StudentQuestionResponseAuditDAO extends BaseDAO<StudentQuestionResponseAudit> {

	@Override
	protected String getTableName() {
		return "student_question_response_audit";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO student_question_response_audit(username,studentTestId,questionId,answer,marks,attempts,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:username,:studentTestId,:questionId,:answer,:marks,:attempts,:createdBy,:createdDate,"
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
	
	public StudentQuestionResponseAudit findByStudentUsernameAndTestQuestnId(String username,String questnId){
		String sql =" select * from student_question_response_audit where username=? and questionId=?";
		return findOneSQL(sql, new Object[]{username,questnId});
	}
	
	public List<StudentQuestionResponseAudit> findByStudentTestId(String studTestId) {
		String sql = "select * from "+ getTableName() +" where studentTestId=?";
		return findAllSQL(sql, new Object[]{studTestId});
	}
	
	public int deleteFacultyTestResponseAudit(String testId,String username) {
		String sql = " delete from "+getTableName()+"  where questionId in(select id from test_question " 
				+" where testId = ?  ) and username = ? ";
		//SqlParameterSource parameterSource = getParameterSource(bean);
		int updated = getJdbcTemplate().update(sql,
				new Object[]{testId,username});
		return updated;
	}
	//17-09-2020 Hiren
	public StudentQuestionResponseAudit findByStudentUsernameAndTestQuestnIdAndAttempts(long studentTestId,String username,long questnId,int attempts){
		String sql =" select * from student_question_response_audit where studentTestId = ? and username= ? and questionId= ? and attempts = ? ";
		return findOneSQL(sql, new Object[]{studentTestId,username,questnId,attempts});
	}
	
	public void updateMarksForQuestionResponseAudit(double marks,long id) {
		String sql ="update student_question_response_audit set marks = ? where id = ?";
		getJdbcTemplate().update(sql, new Object[]{marks,id});
	}
	
	public List<StudentQuestionResponseAudit> findByQuesId(long qId) {
		String sql = "select sqr.* from " + getTableName() + " sqr, student_test st where sqr.studentTestId = st.id and sqr.questionId=? and st.testCompleted = 'Y' ";
		return findAllSQL(sql, new Object[] { qId });
	}
}
