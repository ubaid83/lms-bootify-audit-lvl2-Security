package com.spts.lms.daos.assignment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.GroupAssignment;
import com.spts.lms.daos.BaseDAO;

@Repository("groupAssignmentDAO")
public class GroupAssignmentDAO extends BaseDAO<GroupAssignment> {

	@Override
	protected String getTableName() {
		return "group_assignment";
	}

	@Override
	protected String getInsertSql() {
		/*
		 * final String sql =
		 * "INSERT INTO group_assignment(groupId,assignmentId,username,groupName,acadMonth,acadYear,courseId,evaluatedBy,"
		 * +
		 * "createdBy,createdDate,lastModifiedBy,lastModifiedDate,submissionDate,attempts,submissionStatus,score,remarks,evaluationStatus,studentFilePath,filePreviewPath) VALUES "
		 * +
		 * "(:groupId,:assignmentId,:username,:groupName,:acadMonth,:acadYear,:courseId,:evaluatedBy,"
		 * +
		 * ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:submissionDate,:attempts,:submissionStatus,:score,:remarks,:evaluationStatus,:studentFilePath,:filePreviewPath)"
		 * ;
		 */
		final String sql = "INSERT INTO group_assignment(groupId,groupName,acadMonth,acadYear,username,courseId,assignmentId,evaluatedBy,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate) VALUES "
				+ "(:groupId,:groupName,:acadMonth,:acadYear,:username,:courseId,:assignmentId,:evaluatedBy,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";

		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "update group_assignment "
				+ " set studentFilePath = :studentFilePath,"
				+ " filePreviewPath = :filePreviewPath ,"
				+ " submissionDate = :submissionDate ,"
				+ " attempts = attempts + 1,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate,"
				+ " submissionStatus = :submissionStatus "
				+ " groupId = :groupId"
				+ " groupName = :groupName"
				+ " where  assignmentId = :assignmentId and courseId = :courseId and username = :username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<GroupAssignment> getGroupsForAssignment(Long assignmentId,
			Long courseId, String acadMonth, String acadYear) {
		String sql = "select distinct g.id as groupId, g.groupName, ga.assignmentId, ga.id "
				+ " from groups g"
				+ "		inner join assignment a on a.courseId = g.courseId"
				+ "		left outer join group_assignment ga on ga.groupId = g.id and ga.courseId = g.courseId and ga.assignmentId = ? "
				+ " where g.active = 'Y' "
				+ " g.courseId = ? and g.acadMonth = ? and g.acadYear = ? order by ga.id asc ";
		return findAllSQL(sql, new Object[] { assignmentId, courseId,
				acadMonth, acadYear });

	}

	public ArrayList<GroupAssignment> getGroupListAssociatedToFaculty(
			String userName) {
		String sql = " select * from group_assignment g"
				+ " where username = ? "
				+"and g.active='Y'";
		return (ArrayList<GroupAssignment>)findAllSQL(sql, new Object[] { userName });
	}
}
