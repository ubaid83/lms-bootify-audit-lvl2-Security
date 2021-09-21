package com.spts.lms.daos.assignment;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.daos.BaseDAO;

@Repository("StudentAssignmentAuditDAO")
public class StudentAssignmentAuditDAO  extends BaseDAO<StudentAssignment> {

	
	@Override
	protected String getTableName() {
		return "student_assignment_audit";
	}

	@Override
	protected String getInsertSql() {

		final String sql = "INSERT INTO "+ getTableName() +"(acadMonth,acadYear,username,courseId,studentFilePath,filePreviewPath,submissionDate,attempts,assignmentId,evaluatedBy,groupId,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,startDate,endDate,active)"

				+ "VALUES (:acadMonth,:acadYear,:username,:courseId,:studentFilePath,:filePreviewPath,:submissionDate,:attempts,:assignmentId,:evaluatedBy,:groupId,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:startDate,:endDate,:active)";

		/*
		 * final String sql =
		 * "INSERT INTO student_assignment(acadMonth,acadYear,username,courseId,filePath,"
		 * +
		 * "filePreviewPath,submissionDate,attempts,createdBy,createdDate,lastModifiedBy,lastModifiedDate,"
		 * + "submissionStatus,assignmentId) VALUES " +
		 * "(:acadMonth,:acadYear,:username,:courseId,:filePath,:filePreviewPath,:submissionDate,:attempts,"
		 * +
		 * ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:submissionStatus,:assignmentId)"
		 * ;
		 */

		return sql;
	}
	
	@Override
	protected String getUpdateSql() {
		final String sql = "update  "+ getTableName()
				+ " set studentFilePath = :studentFilePath,"
				+ " filePreviewPath = :filePreviewPath ,"
				+ " submissionDate = :submissionDate ,"
				+ " attempts = attempts + 1,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate,"
				+ " submissionStatus = :submissionStatus, "
				+ "startDate = :startDate ,"
				+ "endDate = :endDate, "
				+ "threshold = :threshold, "
				+ "url = :url, "
				+ " active =:active"
				+ " where  assignmentId = :assignmentId and courseId = :courseId and username = :username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	public StudentAssignment findAssignmentSubmission(String userName,
			Long assignmentId) {
		String sql = "Select * from student_assignment_audit sa where sa.username = ? and sa.assignmentid = ? order by id desc limit 1";
		return findOneSQL(sql, new Object[] { userName, assignmentId });
	}
}
