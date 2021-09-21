package com.spts.lms.daos.groups;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.group.StudentGroup;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("studentgroupDAO")
public class StudentGroupDao extends BaseDAO<StudentGroup> {

	@Override
	protected String getTableName() {
		return "student_group";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO student_group(username,courseId,groupId,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,acadMonth,acadYear) VALUES "
				+ "(:username,:courseId,:groupId,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:acadMonth,:acadYear)";

		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "update student_group set "

				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate,"

				+ " where  groupId = :groupId and courseId = :courseId and username = :username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public int getNoOfStudentsAllocated(Long id) {
		String sql = " select count(*) from student_group where groupId = ?";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	public List<StudentGroup> getStudentsForGroupForAllocation(Long groupId,
			Long courseId, String acadMonth, String acadYear) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sg.groupId, sg.id "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          left outer  join student_group sg on sg.username = u.username and sg.courseId = uc.courseId and sg.groupId = ? "
				+ " where"
				+ " uc.role = ? and uc.active = 'Y' and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by sg.id asc ";
		return findAllSQL(sql, new Object[] { groupId,
				Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear });

	}

	public List<StudentGroup> getStudentsForGroupForAllocation(Long groupId,
			Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sg.groupId, sg.id , u.campusId, u.campusName "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          left outer  join student_group sg on sg.username = u.username and sg.courseId = uc.courseId "
				
				+ " where"
				+ " uc.role = ? and uc.active = 'Y' and uc.courseId = ?  order by sg.lastModifiedDate desc ";
		return findAllSQL(sql, new Object[] { 
				Role.ROLE_STUDENT.name(), courseId });

	}

	public List<StudentGroup> getStudentsBasedOnGroups(Long groupId) {
		String sql = "select u.firstname, u.lastname , g.* ,sg.*  from student_group sg"
				+ "  inner join users u on u.username = sg.username"
				+ "         inner join course c on c.id=sg.courseId"
				+ "         inner join groups g on g.id=sg.groupId"
				+ " where"
				+ " g.id = ? and c.active='Y' and g.active = 'Y' and u.enabled = 1 and u.active = 'Y' and sg.active = 'Y' order by sg.id asc  ";

		return findAllSQL(sql, new Object[] { groupId });
	}

	public List<StudentGroup> findStudentsByGroupId(Long groupId) {
		String sql = "select * from student_group where groupId = ?";
		return findAllSQL(sql, new Object[] { groupId });
	}

	public List<StudentGroup> getStudentsForGroup(Long groupId, Long courseId,
			String acadMonth, Integer acadYear) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sg.groupId, sg.id "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          inner  join student_group sg on sg.username = u.username and sg.courseId = uc.courseId and sg.groupId = ? "
				+ " where"
				+ " uc.role = ? and uc.active = 'Y' and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by sg.id asc ";
		return findAllSQL(sql, new Object[] { groupId,
				Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear });

	}

	public List<StudentGroup> getStudentsForGroup(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname"
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ " where"
				+ " uc.role = ? and uc.active = 'Y' and uc.courseId = ?  order by u.username ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId });

	}

	public void removeStudentsFromGroup(Long id) {
		executeUpdateSql("delete from student_group  where id=?",
				new Object[] { id });

	}

	public List<StudentGroup> getAllGroups(Long courseId) {
		String sql = "select distinct g.id as groupId, g.groupName , CASE when g.id IS NULL    THEN 'N'  ELSE 'Y'  END as 'allocated' "
				+ "  from groups g " + "  where " + "  g.courseId = ? ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<StudentGroup> getStudentsForGroupForAllocationAndCourseId(
			 Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sg.groupId, sg.id, u.campusName "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          left outer  join (select * from student_group where groupId in (select groupId from group_course)) sg on sg.username = u.username and sg.courseId = uc.courseId "
				
				+ " where"
				+ " uc.role = ? and uc.active = 'Y' and u.enabled=1 and uc.courseId = ?  order by sg.lastModifiedDate desc ";
		return findAllSQL(sql, new Object[] { 
				Role.ROLE_STUDENT.name(), courseId });

	}
	
	public List<StudentGroup> getStudentsForGroupForAllocationAndCampusId(
			 Long courseId, Long campusId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sg.groupId, sg.id, u.campusName "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          left outer  join (select * from student_group where groupId in (select groupId from group_course)) sg on sg.username = u.username and sg.courseId = uc.courseId "
				
				+ " where"
				+ " uc.role = ? and uc.active = 'Y' and uc.courseId = ? and u.enabled=1 and u.campusId = ? order by sg.lastModifiedDate desc ";
		return findAllSQL(sql, new Object[] { 
				Role.ROLE_STUDENT.name(), courseId, campusId });

	}

}