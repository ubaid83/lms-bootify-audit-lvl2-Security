package com.spts.lms.daos.groups;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.group.GroupCourse;
import com.spts.lms.daos.BaseDAO;

@Repository("groupCourseDAO")
public class GroupCourseDAO extends BaseDAO<GroupCourse> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "group_course";
	}

	@Override
	protected String getInsertSql() {
		String sql = "insert into "
				+ getTableName()
				+ "(groupId, courseId, createdBy, createdDate, lastModifiedBy, lastModifiedDate, active) values "
				+ "(:groupId, :courseId, :createdBy, :createdDate,  :lastModifiedBy, :lastModifiedDate, :active ) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "courseId = :courseId ,"

				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<GroupCourse> findbyGroupId(Long groupId) {
		String sql = "select * from " + getTableName() + " where groupId = ?";
		return findAllSQL(sql, new Object[] { groupId });
	}

	public GroupCourse findbyGroupIdAndCourseId(Long groupId, String courseId) {
		String sql = "select * from " + getTableName()
				+ " where groupId = ? and courseId = ?";
		return findOneSQL(sql, new Object[] { groupId, courseId });
	}

}
