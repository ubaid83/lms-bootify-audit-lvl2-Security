package com.spts.lms.daos.groups;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.group.Groups;
import com.spts.lms.daos.BaseDAO;

@Repository("groupDAO")
public class GroupDAO extends BaseDAO<Groups> {

	@Override
	protected String getTableName() {
		return "groups";
	}

	@Override
	protected String getInsertSql() {

		String sql = "insert into "
				+ getTableName()
				+ "(groupName, courseId, facultyId, acadYear, acadMonth, "
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate, active, noOfStudents, group_details) values "
				+ "(:groupName, :courseId, :facultyId, :acadYear, :acadMonth,"
				+ " :createdBy, :createdDate,  :lastModifiedBy, :lastModifiedDate, :active, :noOfStudents, :group_details ) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update groups set " + "groupName = :groupName,"
				+ "acadYear = :acadYear ," + "noOfStudents = :noOfStudents,"
				+ "acadMonth = :acadMonth ,"
				+ "group_details = :group_details," + "active = :active,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Groups> findByUser(String username, String acadMonth,
			String acadYear) {
		final String sql = "SELECT g.*, c.courseName FROM groups g "
				+ " inner join student_group sg on sg.groupId = g.id "
				+ " inner join course c on g.courseId = c.id "
				+ " where sg.username = ? " + " and sg.acadMonth = ? "
				+ " and sg.acadYear = ? ";

		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public List<Groups> findByUser(String username) {
		final String sql = "SELECT g.*, c.courseName FROM groups g "
				+ " inner join student_group sg on sg.groupId = g.id "
				+ " inner join course c on g.courseId = c.id "
				+ " where sg.username = ? and c.active='Y' ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Groups> findByUserAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		final String sql = "SELECT g.*, c.courseName FROM groups g "
				+ " inner join student_group sg on sg.groupId = g.id "
				+ " inner join course c on g.courseId = c.id "
				+ " where sg.username = ? "
				+ " and g.courseId = ? and sg.acadMonth = ? "
				+ " and sg.acadYear = ? ";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth,
				acadYear });
	}

	public List<Groups> findByUserAndCourse(String username, Long courseId) {
		final String sql = "SELECT g.*, c.courseName FROM groups g "
				+ " inner join student_group sg on sg.groupId = g.id "
				+ " inner join course c on g.courseId = c.id "
				+ " where sg.username = ? "
				+ " and g.courseId = ?  and c.active='Y' and g.active='Y' ";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<Groups> findByCourse(Long courseId) {
		final String sql = "SELECT g.*, c.courseName FROM groups g "
				+ " inner join course c on g.courseId = c.id "
				+ " where g.courseId = ? " + " and g.acadMonth = ? "
				+ " and g.acadYear = ? " + " order by groupName";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<Groups> findByFaculty(String username, String acadMonth,
			String acadYear) {
		final String sql = "SELECT g.* FROM groups g "
				+ " where g.facultyId = ? " + " and g.acadMonth = ? "
				+ " and g.acadYear = ? ";
		// + " order by dueDate";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public void updateFacultyAssigned(String facultyId, Long groupId) {
		executeUpdateSql("Update groups g set g.facultyId = ? where g.id=?",
				new Object[] { facultyId, groupId });

	}

	public List<Groups> findByFaculty(String username) {
		final String sql = "SELECT g.* FROM groups g "
				+ " where g.facultyId = ? and g.active='Y' ";
		// + " order by dueDate";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<Groups> findByFacultyAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		final String sql = "SELECT g.* FROM groups g "
				+ " where g.facultyId = ? " + " and g.courseId = ? "
				+ " and g.acadMonth = ? " + " and g.acadYear = ? ";
		// + " order by dueDate desc";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth,
				acadYear });
	}

	/*
	 * public List<Groups> findByFacultyAndCourse(String username, Long
	 * courseId) { final String sql = "SELECT g.* FROM groups g " +
	 * " where g.facultyId = ? " + " and g.courseId = ? " +
	 * " and g.active='Y' "; // + " order by dueDate desc"; return
	 * findAllSQL(sql, new Object[] { username, courseId }); }
	 */

	public List<Groups> findByFacultyAndCourseNonEmpty(String username,
			Long courseId, String acadMonth, String acadYear) {
		final String sql = "SELECT  distinct g.* FROM groups g , student_group sg"
				+ " where sg.groupId = g.id and g.facultyId = ? "
				+ " and g.courseId = ? "
				+ " and g.acadMonth = ? "
				+ " and g.acadYear = ? ";
		// + " order by dueDate desc";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth,
				acadYear });
	}

	public List<Groups> findByFacultyAndCourseActiveWithNonEmptyGroups(
			String username, Long courseId) {
		final String sql = "SELECT  distinct g.* FROM groups g , student_group sg"
				+ " where sg.groupId = g.id and g.facultyId = ? "
				+ " and g.courseId = ? " + " and g.active='Y' ";
		// + " order by dueDate desc";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<Groups> findAllGroupsByFaculty(String username,
			String acadMonth, String acadYear) {
		final String sql = "SELECT g.* FROM groups g "
				+ " where g.facultyId = ? " + " and g.acadMonth = ? "
				+ " and g.acadYear = ? ";
		// + " order by dueDate desc";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	/*
	 * public List<Groups> findAllGroupsByFaculty(String username) { final
	 * String sql = "SELECT g.* FROM groups g " +
	 * " where g.facultyId = ? and g.active='Y' "; // +
	 * " order by dueDate desc"; return findAllSQL(sql, new Object[] { username
	 * }); }
	 */

	/*
	 * public List<Groups> findAllGroupsByFaculty(String username, Long
	 * programId) { final String sql =
	 * "SELECT g.* FROM groups g ,course c , program p where g.facultyId = ? " +
	 * " and g.courseId=c.id and  c.programId =p.id and  p.id=? and " +
	 * " g.active='Y' order by g.id desc "; // + " order by dueDate desc";
	 * return findAllSQL(sql, new Object[] { username, programId }); }
	 */
	public List<Groups> findAllGroupsByFaculty(String username, Long programId) {
		final String sql = "SELECT distinct(g.id), g.groupName,g.facultyId,g.acadYear,g.acadMonth,g.createdBy,g.createdDate,g.noOfStudents,"
				+ " g.group_details, g.courseId FROM groups g ,course c , program p, group_course gc where g.facultyId = ? and g.id = gc.groupId "
				+ " and gc.courseId=c.id and  c.programId =p.id and  p.id=? and  g.active='Y' order by g.id desc";
		// + " order by dueDate desc";
		return findAllSQL(sql, new Object[] { username, programId });
	}

	public List<Groups> findByFacultyAndCourse(String username, Long courseId) {
		final String sql = "SELECT g.* FROM groups g "
				+ " where g.facultyId = ? " + " and g.courseId = ? "
				+ " and g.active='Y' order by g.id desc";
		// + " order by dueDate desc";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<Groups> findByFacultyAndCourseWithGroupCourse(String username,
			String courseId) {
		final String sql = "SELECT g.*, gc.courseId as courseId FROM groups g , group_course gc "
				+ " where g.facultyId = ? and g.id=gc.groupId  and gc.courseId = ? "
				+ " and g.active='Y' and gc.active='Y' order by g.id desc	";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<Groups> findByFacultyAndCourseWithGroupCourseNew(String username, String courseId) {
		final String sql = "SELECT g.*, gc.courseId as courseId FROM groups g , group_course gc "
				+ " where g.facultyId = ? and g.id=gc.groupId and gc.courseId = ? "
				+ " and g.active='Y' and gc.active='Y' order by g.id desc	";
		return findAllSQL(sql, new Object[] {username, courseId });
	}

}