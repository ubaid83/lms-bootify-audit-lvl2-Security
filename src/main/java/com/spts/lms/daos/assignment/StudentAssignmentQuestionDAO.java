package com.spts.lms.daos.assignment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.StudentAssignmentQuestion;
import com.spts.lms.daos.BaseDAO;

@Repository("studentAssignmentQuestionDAO")
public class StudentAssignmentQuestionDAO extends BaseDAO<StudentAssignmentQuestion> {

	@Override
	protected String getTableName() {
		return "student_assignment_question";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO student_assignment_question(username,studentAssignmentId,questionId,createdBy,createdDate,lastModifiedBy,lastModifiedDate) "
				+ "VALUES (:username,:studentAssignmentId,:questionId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<StudentAssignmentQuestion> getAllByAssignIdAndUsername(String id, String username){
		String sql = " SELECT aq.*,saq.username,apc.id FROM student_assignment_question saq, assignment_question aq,"
				+ "assignment_pool_configuration apc,test_question_pools tqp WHERE saq.questionId = aq.id and"
				+ " aq.assignmentId=apc.assignmentId and tqp.id=aq.testQuestionPoolId and tqp.testPoolId=apc.testPoolId"
				+ " and aq.assignmentId = ? AND saq.username = ? group by aq.id order by apc.id ";
		return findAllSQL(sql, new Object[] { id, username });
	}

	public void deleteAssignQuesByAssignment(Long studentAssignmentId) {
		executeUpdateSql("delete from student_assignment_question where studentAssignmentId = ?",
			new Object[] { studentAssignmentId });
	}
	
	public List<StudentAssignmentQuestion> getAllByAssignIdAndUsernameForNonRandom(String id, String username){
		String sql = " SELECT aq.*,saq.username FROM student_assignment_question saq, assignment_question aq WHERE saq.questionId = aq.id " +
		" and aq.assignmentId = ? AND saq.username = ? order by aq.id ";
		return findAllSQL(sql, new Object[] { id, username });
	}
}
