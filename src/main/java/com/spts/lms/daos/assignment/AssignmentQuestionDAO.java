package com.spts.lms.daos.assignment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.AssignmentQuestion;
import com.spts.lms.daos.BaseDAO;

@Repository
public class AssignmentQuestionDAO extends BaseDAO<AssignmentQuestion>{

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "assignment_question";
		
		
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		return "INSERT INTO "
		+ getTableName()
		+ " (assignmentId,description,marks,questionType,testQuestionPoolId,createdBy,createdDate,"
		+ "lastModifiedBy,lastModifiedDate)"

		+ " VALUES(:assignmentId,:description,:marks,:questionType,:testQuestionPoolId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
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
	
	public List<AssignmentQuestion> getAssgnQuestionsByAssignmentId(Long assignmentId,String questionType){
		
		String sql =" SELECT aq.*,tp.id AS testPoolId FROM assignment_question aq,test_pool tp,test_question_pools tqp " + 
				" where tqp.testPoolId=tp.id and aq.testQuestionPoolId=tqp.id and aq.assignmentId=? and tqp.questionType=? " + 
				" and tp.active='Y' and tqp.active='Y' and aq.active='Y' " + 
				" order by id DESC ";
		return findAllSQL(sql, new Object[] {assignmentId,questionType});
	}
	
//	public List<AssignmentQuestion> getAssgnQuestionToValidate(Long assignmentId,String questionType){
//		String sql =" SELECT  aq.marks,testPoolId,COUNT(*) as noOfQuestion  FROM assignment_question aq,test_pool tp,test_question_pools tqp" + 
//				" WHERE  tqp.testPoolId=tp.id AND aq.testQuestionPoolId=tqp.id and aq.assignmentId =? and tqp.questionType=? " + 
//				" AND tp.active='Y' AND tqp.active='Y' AND aq.active='Y'" + 
//				" GROUP BY aq.marks ";
//		return findAllSQL(sql, new Object[] {assignmentId,questionType});
//	}
	public List<AssignmentQuestion> getAssgnQuestionToValidate(Long assignmentId,String questionType){
		String sql =" SELECT  aq.marks,testPoolId,COUNT(*) as noOfQuestion  FROM assignment_question aq,test_pool tp,test_question_pools tqp" + 
		" WHERE  tqp.testPoolId=tp.id AND aq.testQuestionPoolId=tqp.id and aq.assignmentId =? and tqp.questionType=? " + 
		" AND tp.active='Y' AND tqp.active='Y' AND aq.active='Y'" + 
		" GROUP BY testPoolId ";
	return findAllSQL(sql, new Object[] {assignmentId,questionType});
	}
	public List<AssignmentQuestion> getTestPoolsByAssignmentId(Long assignmentId) {
		
		String sql =" select testPoolId,noOfQuestion from assignment_pool_configuration where assignmentId =? and active ='Y' ";
		
		return findAllSQL(sql, new Object[] {assignmentId});
	}
	
	public List<AssignmentQuestion> findByAssignmentId(Long assignmentId) {
		final String sql = "Select *  from " + getTableName()
				+ " where assignmentId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public void deleteAlocatedQuestion(long assignmentId) {
		executeUpdateSql("delete from assignment_question where assignmentId = ?",
				new Object[] { assignmentId });
	}
}
