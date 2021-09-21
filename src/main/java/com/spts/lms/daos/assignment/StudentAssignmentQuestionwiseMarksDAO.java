package com.spts.lms.daos.assignment;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.StudentAssignmentQuestionwiseMarks;
import com.spts.lms.daos.BaseDAO;

@Repository("studentAssignmentQuestionwiseMarksDAO")
public class StudentAssignmentQuestionwiseMarksDAO extends BaseDAO<StudentAssignmentQuestionwiseMarks>{

	@Override
	protected String getTableName() {
		return "student_assignment_questionwise_marks";
	}

	@Override
	protected String getInsertSql() {
		
		return null;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "INSERT INTO "+getTableName()+" (username, assignmentId, assignConfigId, marks, active, createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ "VALUES (:username, :assignmentId, :assignConfigId, :marks, :active, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate)"
				+ " ON DUPLICATE KEY UPDATE "
				+ " marks=:marks, lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate ";
		return sql;
	}
	
	public List<StudentAssignmentQuestionwiseMarks> getStudentsMarksQuestionwise(long assignmentId){
		String sql="select * from "+getTableName()+" where assignmentId = ? and active= 'Y'";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	
}
