
package com.spts.lms.daos.user;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;

@Repository("userCourseDAO")
public class UserCourseDAO extends BaseDAO<UserCourse> {

	@Override
	protected String getTableName() {
		return "user_course";
	}

	@Override
	protected BeanPropertySqlParameterSource getParameterSource(UserCourse bean) {
		BeanPropertySqlParameterSource sqlParameterSource = super.getParameterSource(bean);
		sqlParameterSource.registerSqlType("role", Types.VARCHAR);
		return sqlParameterSource;
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO user_course(username,courseId,acadMonth,acadYear,role,createdBy,createdDate,lastModifiedBy,lastModifiedDate,acadSession,acadYearCode)"
				+ " VALUES(:username,:courseId,:acadMonth,:acadYear,:role,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:acadSession,:acadYearCode) "
				+ " ON DUPLICATE KEY UPDATE "
				+ " role = :role, lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<UserCourse> findByUsername(String uname) {
		String sql = "select * from user_course where username = ? and active = 'Y' ";
		return findAllSQL(sql, new Object[] { uname });
	}

	@Override
	protected Page<UserCourse> searchByExactMatch(UserCourse bean, int pageNo, int pageSize, boolean includeActive) {
		ArrayList<Object> parameters = buildParameters(bean, includeActive);
		// The first object will always be the criteria String
		StringBuilder criteria = (StringBuilder) parameters.remove(0);

		final String sql = "SELECT " + getTableName() + ".*,u.firstname,u.lastname,c.courseName " + " FROM "
				+ getTableName() + " inner join users u on u.username = " + getTableName() + ".username "
				+ " inner join course c on c.id = " + getTableName() + ".courseId ";

		return paginationHelper.fetchPage(getJdbcTemplate(),
				"SELECT COUNT(*) FROM " + getTableName() + criteria.toString(), sql + criteria.toString(),
				parameters.toArray(), pageNo, pageSize, BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<UserCourse> getStudentsByProgram(String programId) {

		final String sql = "select u.username from users u inner join user_roles ur on u.username=ur.username  where u.programId=? and ur.role='ROLE_STUDENT' "
				+ " and u.active = 'Y' and u.enabled = 1";

		return findAllSQL(sql, new Object[] { programId });
	}

	public List<UserCourse> getUserbasedOnMultipleCourse(String role, List<String> courseId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roles", role);
		params.put("courseIds", courseId);

		final String sql = " select * from user_course where role=:roles and courseId in (:courseIds) ";

		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));

	}

	public List<Map<String, Object>> getNumberOfStudentsPerCourse() {

		String sql = " select count(*) as numOfStudentsInTheCourse ,courseId as cId from user_course "
				+ " where role='ROLE_STUDENT' and active = 'Y' group by courseId ";
		return getJdbcTemplate().queryForList(sql, new Object[] {});
	}

	public List<UserCourse> findFacultyByCourseID(Long courseId, String acadMonth, Integer acadYear) {
		final String sql = "Select uc.*, u.firstname, u.lastname from " + getTableName()
				+ " uc inner join users u on u.username = uc.username where courseId = ? and acadMonth = ? and acadYear = ? and u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and role = ?";
		return findAllSQL(sql, new Object[] { courseId, acadMonth, acadYear, Role.ROLE_FACULTY.name() });
	}

	public List<UserCourse> findStudentsForFaculty(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, u.campusId, u.campusName "
				+ "  from user_course uc " + "  inner join users u on uc.username = u.username  "
				+ "  inner join program p on u.programId = p.id" + "  where"
				+ "  uc.role = ? and uc.courseId = ?  and uc.active = 'Y' and u.enabled = 1 and u.active = 'Y' ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(), courseId });
	}

	public List<UserCourse> getStudentCourseList() {

		final String sql = " select uc.* from " + getTableName()
				+ " uc, users u where uc.role='ROLE_STUDENT' and uc.username=u.username " + " and u.enabled=1 "
				+ " and uc.active = 'Y' ";
		return findAllSQL(sql, new Object[] {});
	}

	public List<UserCourse> findFacultyByCourseID(Long courseId) {
		final String sql = "Select uc.*, u.firstname, u.lastname from " + getTableName()
				+ " uc inner join users u on u.username = uc.username where courseId = ? and u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and  role = ?";
		return findAllSQL(sql, new Object[] { courseId, Role.ROLE_FACULTY.name() });
	}

	public void makeInActive(Long courseId, String username) {
		executeUpdateSql("update user_course set active='N' where courseId= ? and username=? ",
				new Object[] { courseId, username });
	}

	public List<UserCourse> findAllFacultyWithCourseId(Long courseId) {
		final String sql = " SELECT uc.* FROM user_course uc "
				+ " where  uc.courseId = ? and role='ROLE_FACULTY' and uc.active = 'Y'"
				+ " order by uc.createdDate desc ";

		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<UserCourse> findAllFacultyWithCourseId(Long courseId, String acadMonth, Integer acadYear) {
		final String sql = " SELECT uc.* FROM user_course uc "
				+ " where  uc.courseId = ? and uc.acadMonth= ?  and uc.acadYear = ? and uc.active = 'Y' and uc.role='ROLE_FACULTY' "
				+ " order by uc.createdDate desc";

		return findAllSQL(sql, new Object[] { courseId, acadMonth, acadYear });
	}

	public List<UserCourse> getUsersBasedOnCourse(Long courseId, String role) {
		final String sql = "Select uc.*, u.firstname, u.lastname from " + getTableName()
				+ " uc,users u where  u.username = uc.username and  uc.courseId = ? and uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and uc.role = ?";
		return findAllSQL(sql, new Object[] { courseId, role });
	}

	public List<UserCourse> findAllAcadSessionsWithAcadYear(Long acadYear) {
		final String sql = "select uc.* from user_course uc where uc.acadYear=?"
				+ " and uc.role='ROLE_STUDENT' and uc.active = 'Y' group by acadSession";

		return findAllSQL(sql, new Object[] { acadYear });
	}

	public List<UserCourse> findAllAcadSessionCourse() {
		final String sql = "select uc.courseId ,uc.acadSession ,c.courseName,CONCAT(COALESCE(u.firstname,' '), ' ' ,COALESCE(u.lastname,' ')) as 'facultyName' ,uc.acadYear  from user_course uc ,user_course uc1 , course c,users u ,student_feedback sf where sf.courseId = c.id and uc.role='ROLE_STUDENT' and c.id = uc.courseId and uc1.role='ROLE_FACULTY' and uc1.courseId=uc.courseId and uc1.username =u.username and   uc.acadSession is not null  group by uc.acadSession,uc.courseId";
		// return getJdbcTemplate().queryForList(sql, new Object[] { });
		return findAllSQL(sql, new Object[] {});
	}

	public List<UserCourse> findAllAcadSessionsWithAcadYear(Long acadYear, String programId) {
		final String sql = "select uc.* from user_course uc,users u where uc.acadYear=? and uc.active='Y' and u.active='Y' and u.enabled = 1 "
				+ " and uc.role='ROLE_STUDENT' and uc.username=u.username  and u.programId= ? "
				+ " group by acadSession";

		return findAllSQL(sql, new Object[] { acadYear, programId });
	}

	public List<String> findAllAcadSessionsForProgramId(String programId) {
		final String sql = "select distinct u.acadSession from users u where "
				+ " u.active = 'Y' and u.enabled = 1 and (u.acadSession is not null or u.acadSession <> '') and u.programId= "
				+ programId + " group by u.acadSession";
		List<String> data = getJdbcTemplate().queryForList(sql, String.class);
		return data;

	}

	public List<UserCourse> noofStudentsInCourseList(List<Long> courseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", courseId);
		String sql = " select count(uc.username) as 'noOfStudentInCourse' , uc.courseId  from " + getTableName()
				+ " uc where uc.courseId in (:ids) and uc.role='ROLE_STUDENT' and uc.active='Y' group by uc.courseId";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));

	}

	public void updateUserCourseToMakeInactive(Long courseId) {
		executeUpdateSql("Update user_course set active = 'N' where courseId= ?", new Object[] { courseId });
	}

	public List<UserCourse> getFacultiesByCourseIds(List<String> courseIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", courseIds);
		String sql = " select distinct uc.* from " + getTableName() + " uc,course c where uc.courseId in(:ids) "
				+ " and uc.active = 'Y' and c.id =uc.courseId and uc.role='ROLE_FACULTY' and c.moduleCategoryName <> 'Coursera' ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
	}

	public List<UserCourse> getStudentsByCourseIds(List<String> courseIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", courseIds);
		String sql = " select distinct uc.* from " + getTableName() + " uc,course c where uc.courseId in(:ids) "
				+ " and uc.active = 'Y' and c.id =uc.courseId and uc.role='ROLE_STUDENT' and c.moduleCategoryName <> 'Coursera' ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
	}

	public List<UserCourse> findStudentsForFacultyWithCampusId(Long courseId, Long campusId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, u.campusName "
				+ "  from user_course uc " + "  inner join users u on uc.username = u.username  "
				+ "  inner join program p on u.programId = p.id" + "  where"
				+ "  uc.role = ? and uc.courseId = ? and u.campusId = ? and uc.active = 'Y' and u.enabled = 1 ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(), courseId, campusId });
	}

	public List<String> findStudentUsernamesForFaculty(Long courseId) {
		String sql = "select uc.username " + "  from user_course uc "
				+ "  inner join users u on uc.username = u.username" + "  inner join program p on u.programId = p.id"
				+ "  where"
				+ "  uc.role = ? and uc.courseId = ?  and uc.active = 'Y' and u.enabled = 1 and u.active = 'Y' ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { Role.ROLE_STUDENT.name(), courseId });
	}

	public List<UserCourse> noofStudentsInCourseList(Long courseId) {

		String sql = " select count(uc.username) as 'noOfStudentInCourse' , uc.courseId  from " + getTableName()
				+ " uc where uc.courseId = ? and uc.role='ROLE_STUDENT' and uc.active='Y' ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<UserCourse> noofStudentsInCourseList(String campus, Long courseId) {

		String typeCondition = "";
		if (campus != null) {
			typeCondition = " c.campusId = ? ";
		} else {
			typeCondition = " c.campusId is ? ";
		}

		String sql = " select count(uc.username) as 'noOfStudentInCourse' , uc.courseId  from " + getTableName()
				+ " uc, course c, users u where uc.courseId = c.id and uc.username = u.username and u.active = 'Y' and "
				+ typeCondition + " and uc.courseId = ? and uc.role='ROLE_STUDENT' and uc.active='Y' ";
		return findAllSQL(sql, new Object[] { campus, courseId });
	}

	public List<UserCourse> getUserbasedByAcadSessionAndYear(String role, List<String> acadSession, String acadYear) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roles", role);
		params.put("acadSession", acadSession);
		params.put("acadYear", acadYear);

		final String sql = " select uc.*,concat(u.firstName,' ',u.lastName) as facultyName from  " + getTableName()
				+ " uc,course c,users u where uc.courseId=c.id and c.acadSession in(:acadSession) and c.acadYear = :acadYear "
				+ " and uc.username = u.username" + " and c.active = 'Y' and uc.active = 'Y' AND uc.role = :roles ";

		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));

	}

	public List<UserCourse> getUserbasedByAcadSessionAndYearAndCampus(String role, List<String> acadSession,
			String acadYear, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roles", role);
		params.put("acadSession", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);

		final String sql = " select uc.*,concat(u.firstName,' ',u.lastName) as facultyName from  " + getTableName()
				+ " uc,course c,users u where uc.courseId=c.id and c.acadSession in(:acadSession) and c.acadYear = :acadYear "
				+ " and c.campusId=:campusId " + " and uc.username = u.username"
				+ " and c.active = 'Y' and uc.active = 'Y' AND uc.role = :roles ";

		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));

	}

	public List<String> findUserCourseList(String username) {
		String sql = " select distinct uc.courseId from user_course uc where uc.username=? and uc.active='Y'";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { username });
	}

	public List<UserCourse> getStudentsByCourseId(String courseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		String sql = " select distinct uc.* from " + getTableName() + " uc,course c where uc.courseId = :courseId "
				+ " and uc.active = 'Y' and c.id =uc.courseId and uc.role='ROLE_STUDENT' ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
	}

	public void makeUserInActiveByUsername(String username) {
		executeUpdateSql("update user_course set active='N' where username=? ", new Object[] { username });
	}

	public List<String> findAcadSessionList(String programId) {
		String sql = " select distinct uc.acadSession from user_course uc,course c where uc.courseId=c.id and c.active='Y' and c.programId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { programId });
	}

	public void makeCourseActive(String courseId) {
		final String sql = "Update " + getTableName() + " set active = 'N' WHERE courseId = ?";

		getJdbcTemplate().update(sql, new Object[] { courseId });

	}

	public List<UserCourse> findFacultyByUsingProgramId(String ProgramId) {
		/*
		 * final String sql = "Select count(*) from " + getTableName() +
		 * " uc where courseId = ? and uc.active = 'Y' and  role = ?";
		 */

		final String sql = "select distinct courseId,count(*) as noOfFacultyInCourse  from " + getTableName()
				+ " uc,course c, users u where c.id=uc.courseId and u.username=uc.username and uc.role= ? and  c.programId = ? "
				+ " and c.active='Y' and uc.active='Y' and u.enabled = '1' group by uc.courseId";

		logger.info("SQL is---------------->" + sql);
		return findAllSQL(sql, new Object[] { Role.ROLE_FACULTY.name(), ProgramId });
	}

	public List<UserCourse> findStudentsByUsingProgramId(String ProgramId) {
		/*
		 * final String sql = "Select count(*) from " + getTableName() +
		 * " uc where courseId = ? and uc.active = 'Y' and  role = ?";
		 */

		final String sql = "select distinct courseId,count(*) as noOfStudentInCourse  from " + getTableName()
				+ " uc,course c, users u where c.id=uc.courseId and u.username=uc.username and uc.role= ? and  c.programId = ? "
				+ " and c.active='Y' and uc.active='Y' and u.enabled = '1' group by uc.courseId";

		logger.info("SQL is---------------->" + sql);
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(), ProgramId });
	}

	public List<UserCourse> findAllFacultyWithModuleIdICA(String moduleId, String acadYear) {
		/*
		 * final String sql =
		 * " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
		 * + " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' " +
		 * " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? " +
		 * " and u.username=uc.username " + " group by uc.username";
		 */
		// acad year Code
		final String sql = " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
				+ " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' "
				+ " and uc.courseId = c.id and c.moduleId = ? and c.acadYearCode = ? " + " and u.username=uc.username "
				+ " group by uc.username";

		return findAllSQL(sql, new Object[] { moduleId, acadYear });
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYear(String moduleId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId, String isEvalCompWise, String compId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}

		if (acadSession.contains(",")) {
			List<String> acadSessionList = Arrays.asList(acadSession.split(","));
			params.put("acadSession", acadSessionList);
		} else {
			params.put("acadSession", acadSession);
		}

		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		// params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);
		params.put("compId", compId);

		if (!"Y".equals(isEvalCompWise)) {
			if (campusId != null) {
				/*
				 * String sql =
				 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				 * +
				 * " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				 * + "	 from user_course uc,course c,users u " +
				 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				 * + " where "
				 * 
				 * +
				 * "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
				 * + " and c.programId in(:programIds) and c.campusId=:campusId " +
				 * "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 " +
				 * "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
				 */

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) "
						+ " and c.programId in(:programIds) and c.campusId=:campusId "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {
				/*
				 * String sql =
				 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				 * +
				 * " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				 * + "	 from user_course uc,course c,users u " +
				 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				 * + " where "
				 * 
				 * +
				 * "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
				 * + "  and c.id=uc.courseId and u.username=uc.username " +
				 * "	and u.enabled=1 " +
				 * "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
				 */

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) and c.programId in(:programIds) "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
		} else {

			if (campusId != null) {
				/*
				 * String sql =
				 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				 * +
				 * " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				 * + "	 from user_course uc,course c,users u " +
				 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				 * + " where "
				 * 
				 * +
				 * "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
				 * + " and c.programId in(:programIds) and c.campusId=:campusId " +
				 * "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 " +
				 * "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
				 */

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " and icm.componentId =:compId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) "
						+ " and c.programId in(:programIds) and c.campusId=:campusId "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {
				/*
				 * String sql =
				 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				 * +
				 * " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				 * + "	 from user_course uc,course c,users u " +
				 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				 * + " where "
				 * 
				 * +
				 * "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
				 * + "  and c.id=uc.courseId and u.username=uc.username " +
				 * "	and u.enabled=1 " +
				 * "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
				 */

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " and icm.componentId=:compId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) and c.programId in(:programIds) "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	public List<UserCourse> findAllAcadSessionsWithProgramIds(List<String> programList) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programList", programList);

		final String sql = "select distinct u.acadSession from users u where "
				+ " u.active = 'Y' and u.enabled = 1 and (u.acadSession is not null or u.acadSession <> '') and u.programId in(:programList) "
				+ " group by u.acadSession";

		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));

	}

	public List<UserCourse> getAllFacultiesDivisionWise(String acadYear, String acadSession, String moduleId,
			String programId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct c.id as eventId ,c.courseName, concat(u.firstName,' ',u.lastName) as facultyName,u.username,"
					+ " p.programName from users u,user_course uc,course c,program p "
					+ " where uc.courseId=c.id and uc.username=u.username and c.acadYearCode=:acadYear and c.moduleId=:moduleId "
					+ " and c.campusId=:campusId " + " and c.programId=p.id and c.active='Y' and uc.active='Y' "
					+ " and c.acadSession = :acadSession and c.programId = :programIds and uc.role = 'ROLE_FACULTY' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct c.id as eventId ,c.courseName, concat(u.firstName,' ',u.lastName) as facultyName,u.username,"
					+ " p.programName from users u,user_course uc,course c,program p "
					+ " where uc.courseId=c.id and uc.username=u.username and c.acadYearCode=:acadYear and c.moduleId=:moduleId "
					+ " and c.programId=p.id and c.active='Y' and uc.active='Y' "
					+ " and c.acadSession = :acadSession and c.programId = :programIds and uc.role = 'ROLE_FACULTY' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}

	}

	public List<UserCourse> findStudentByEventIdAndAcadYear(String courseId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId, String isEvalCompWise, String compId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}

		if (programId.contains(",")) {
			List<String> acadSessionList = Arrays.asList(acadSession.split(","));
			params.put("acadSession", acadSessionList);
		} else {
			params.put("acadSession", acadSession);

		}
		params.put("courseId", courseId);
		params.put("acadYear", acadYear);
		// params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);
		params.put("compId", compId);

		if (!"Y".equals(isEvalCompWise)) {
			if (campusId != null) {
				/*
				 * String sql =
				 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				 * +
				 * " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				 * + "	 from user_course uc,course c,users u " +
				 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				 * + " where "
				 * 
				 * +
				 * "	 c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
				 * + " and c.programId in(:programIds) and c.campusId=:campusId " +
				 * "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 " +
				 * "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
				 */

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " where "

						+ "	 c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession in (:acadSession) "
						+ " and c.programId in (:programIds) and c.campusId=:campusId "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	from user_course uc,course c,users u "
						+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " where "

						+ "	c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession in (:acadSession) and c.programId in (:programIds) "
						+ " and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
		} else {

			if (campusId != null) {
				/*
				 * String sql =
				 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				 * +
				 * " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				 * + "	 from user_course uc,course c,users u " +
				 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				 * + " where "
				 * 
				 * +
				 * "	 c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
				 * + " and c.programId in(:programIds) and c.campusId=:campusId " +
				 * "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 " +
				 * "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
				 */

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " and icm.componentId=:compId " + " where "

						+ "	 c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession in (:acadSession) "
						+ " and c.programId in (:programIds) and c.campusId=:campusId "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {

				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	from user_course uc,course c,users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " and icm.componentId=:compId " + " where "

						+ "	c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession in (:acadSession) and c.programId in (:programIds) "
						+ " and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";

				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	/*
	 * public String getFacultiesByParam(String acadYear,String session,String
	 * moduleId,String programId){
	 * 
	 * 
	 * String sql =
	 * " select  group_concat(u.firstName,' ',u.lastName,'(',u.username,')') as assignedFaculty from users u where "
	 * +" username in(select distinct username "
	 * +"    from user_course uc,course c where uc.active='Y'  "
	 * +"    and c.active='Y' and uc.courseId=c.id "
	 * +"    and c.acadYear=? and c.acadSession=? and c.moduleId=? and uc.role='ROLE_FACULTY' "
	 * +"    AND c.programId=?) ";
	 * 
	 * return getJdbcTemplate().queryForObject(sql, String.class, new
	 * Object[]{acadYear,session,moduleId,programId}); }
	 */

	public String getFacultiesByParam(String acadYear, String session, String moduleId, String programId) {

		if (session.contains(",")) {
			List<String> sessionList = Arrays.asList(session.split(","));
			String sqlReturn = "";

			for (String s : sessionList) {
				String sql = " select  group_concat(u.firstName,' ',u.lastName,'(',u.username,')') as assignedFaculty from users u where "
						+ " username in(select distinct username "
						+ "    from user_course uc,course c where uc.active='Y'  "
						+ "    and c.active='Y' and uc.courseId=c.id "
						+ "    and c.acadYearCode=? and c.acadSession=? and c.moduleId=? and uc.role='ROLE_FACULTY' "
						+ "    AND c.programId=?) ";
				String result = getJdbcTemplate().queryForObject(sql, String.class,
						new Object[] { acadYear, s, moduleId, programId });
				sqlReturn = sqlReturn + result + ",";
			}
			return sqlReturn;

		}

		else {
			String sql = " select  group_concat(u.firstName,' ',u.lastName,'(',u.username,')') as assignedFaculty from users u where "
					+ " username in(select distinct username "
					+ "    from user_course uc,course c where uc.active='Y'  "
					+ "    and c.active='Y' and uc.courseId=c.id "
					+ "    and c.acadYearCode=? and c.acadSession=? and c.moduleId=? and uc.role='ROLE_FACULTY' "
					+ "    AND c.programId=?) ";
			return getJdbcTemplate().queryForObject(sql, String.class,
					new Object[] { acadYear, session, moduleId, programId });
		}

	}

	public String getFacultiesByParamForReport(String acadYear) {

		String sql = " select  group_concat(u.firstName,' ',u.lastName,'(',u.username,')') as assignedFaculty from users u where "
				+ " username in(select distinct username " + "    from user_course uc,course c where uc.active='Y'  "
				+ "    and c.active='Y' and uc.courseId=c.id "
				+ "    and c.acadYearCode=?  and uc.role='ROLE_FACULTY') ";

		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { acadYear });
	}

	public UserCourse getMappingByUsernameAndCourse(String username, String courseId) {
		String sql = " select * from user_course where username = ? and courseId = ? ";
		return findOneSQL(sql, new Object[] { username, courseId });
	}
	
	public UserCourse getMappingByUsernameAndModule(String username, String moduleId) {
		String sql = " select uc.* from user_course uc,course c "
				+ "  where uc.courseId=c.id and uc.username = ? "
				+ " and c.moduleId = ? group by uc.username ";
		return findOneSQL(sql, new Object[] { username, moduleId });
	}

	public List<String> findDistinctStudentByModuleIdAndAcadYear(String moduleId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct uc.username" + " from user_course uc,course c,users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "

					+ "  c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + " and u.enabled=1 "
					+ " and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		} else {
			String sql = " select distinct uc.username" + "  from user_course uc,course c,users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "

					+ "  c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + " and u.enabled=1 "
					+ " and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		}

	}

	public List<String> findDistinctStudentByEventIdAndAcadYear(String courseId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("courseId", courseId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct uc.username  " + "  from user_course uc,course c,users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "

					+ "  c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + " and u.enabled=1 "
					+ " and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		} else {
			String sql = " select distinct uc.username " + "  from user_course uc,course c,users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "

					+ "  c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + " and u.enabled=1 "
					+ " and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		}
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForNonEventModule(String moduleId, String acadYear,
			String acadSession, String programId, Long icaId, String campusId,String componentId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);
		params.put("compId", componentId);

		if(componentId==null) {
		if (campusId != null) {
			String sql = " select distinct u.rollNo as rollNo, concat(u.firstName,' ',u.lastName) as studentName, "
					+ " sse.studentNumber as username, sse.acadSession, icm.isQueryRaise, icm.isAbsent "
					+ " from subject_sap_enroll sse, module m, session_master sm,  users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "
					+ " m.module_id = :moduleId and m.acadyear=:acadYear and sse.acadYearCode=:acadYear and sm.sapSessionText=:acadSession "
					+ " and m.program_id in(:programIds) and sse.campusId=:campusId "
					+ " and m.module_id=sse.subjectCode and u.username=sse.studentNumber "
					+ " and sse.enrollDeEnroll = 'Y' and sm.sapSessionText=sse.acadSession and m.program_id = sse.programId ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct u.rollNo as rollNo, concat(u.firstName,' ',u.lastName) as studentName, "
					+ " sse.studentNumber as username, sse.acadSession, icm.isQueryRaise, icm.isAbsent "
					+ " from subject_sap_enroll sse, module m, session_master sm,  users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "
					+ " m.module_id = :moduleId and m.acadyear=:acadYear and sse.acadYearCode=:acadYear and sm.sapSessionText=:acadSession and m.program_id in(:programIds) "
					+ " and m.module_id=sse.subjectCode and u.username=sse.studentNumber "
					+ " and sse.enrollDeEnroll = 'Y' and sm.sapSessionText=sse.acadSession and m.program_id = sse.programId ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
		}else {
			
			if (campusId != null) {
				String sql = " select distinct u.rollNo as rollNo, concat(u.firstName,' ',u.lastName) as studentName, "
						+ " sse.studentNumber as username, sse.acadSession, icm.isQueryRaise, icm.isAbsent "
						+ " from subject_sap_enroll sse, module m, session_master sm,  users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId and icm.componentId=:compId "
						+ " where "
						+ " m.module_id = :moduleId and m.acadyear=:acadYear and sse.acadYearCode=:acadYear and sm.sapSessionText=:acadSession "
						+ " and m.program_id in(:programIds) and sse.campusId=:campusId "
						+ " and m.module_id=sse.subjectCode and u.username=sse.studentNumber "
						+ " and sse.enrollDeEnroll = 'Y' and sm.sapSessionText=sse.acadSession and m.program_id = sse.programId ";
				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {
				String sql = " select distinct u.rollNo as rollNo, concat(u.firstName,' ',u.lastName) as studentName, "
						+ " sse.studentNumber as username, sse.acadSession, icm.isQueryRaise, icm.isAbsent "
						+ " from subject_sap_enroll sse, module m, session_master sm,  users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId and icm.componentId=:compId"
						+ " where "
						+ " m.module_id = :moduleId and m.acadyear=:acadYear and sse.acadYearCode=:acadYear and sm.sapSessionText=:acadSession and m.program_id in(:programIds) "
						+ " and m.module_id=sse.subjectCode and u.username=sse.studentNumber "
						+ " and sse.enrollDeEnroll = 'Y' and sm.sapSessionText=sse.acadSession and m.program_id = sse.programId ";
				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	public List<String> findDistinctStudentByModuleIdAndAcadYearForNonEventModule(String moduleId, String acadYear,
			String acadSession, String programId, Long icaId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct sse.studentNumber "
					+ " from subject_sap_enroll sse, module m, session_master sm,  users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "
					+ " m.module_id = :moduleId and m.acadyear=:acadYear and sse.acadYearCode=:acadYear and sm.sapSessionText=:acadSession "
					+ " and m.program_id in(:programIds) and sse.campusId=:campusId "
					+ " and m.module_id=sse.subjectCode and u.username=sse.studentNumber "
					+ " and sse.enrollDeEnroll = 'Y' and sm.sapSessionText=sse.acadSession and m.program_id = sse.programId ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		} else {
			String sql = " select distinct sse.studentNumber "
					+ " from subject_sap_enroll sse, module m, session_master sm,  users u "
					+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " where "
					+ " m.module_id = :moduleId and m.acadyear=:acadYear and sse.acadYearCode=:acadYear and sm.sapSessionText=:acadSession and m.program_id in(:programIds) "
					+ " and m.module_id=sse.subjectCode and u.username=sse.studentNumber "
					+ " and sse.enrollDeEnroll = 'Y' and sm.sapSessionText=sse.acadSession and m.program_id = sse.programId ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForNCAEvents(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);

		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " uc.username,uc.acadSession  " + "	 from user_course uc,course c,users u "

					+ " where "

					+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " uc.username,uc.acadSession  " + "	 from user_course uc,course c,users u "

					+ " where "

					+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	public List<String> findStudentNoByModuleIdAndAcadYearForNCAEvents(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);

		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct " + " uc.username " + "	 from user_course uc,course c,users u "

					+ " where "

					+ "	 c.moduleId = :moduleId and c.acadYear=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		} else {
			String sql = " select distinct " + " uc.username  " + "	 from user_course uc,course c,users u "

					+ " where "

					+ "	 c.moduleId = :moduleId and c.acadYear=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	/*
	 * public List<UserCourse> findStudentByStudentListAndIcaId(List<String>
	 * usernames,Long icaId) {
	 * 
	 * Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("usernames", usernames); params.put("icaId", icaId);
	 * 
	 * String sql =
	 * " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
	 * + " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent  " +
	 * "          from user_course uc, ica i,users u " +
	 * " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
	 * +
	 * " where i.id = :icaId and i.acadSession = uc.acadSession and u.username=uc.username and uc.username in(:usernames) "
	 * + "          and u.enabled=1 and i.acadYear = uc.acadYear " +
	 * "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' "; return
	 * getNamedParameterJdbcTemplate().query(sql, params,
	 * BeanPropertyRowMapper.newInstance(UserCourse.class)); }
	 */

	public List<UserCourse> findStudentByStudentListAndIcaId(List<String> usernames, Long icaId,String isEvalCompWise,String compId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usernames", usernames);
		params.put("icaId", icaId);
		params.put("compId", compId);

		if(!"Y".equals(isEvalCompWise)) {
		String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
				+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				+ "          from user_course uc, ica i,users u "
				+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				+ " where i.id = :icaId and i.acadSession = uc.acadSession and u.username=uc.username and uc.username in(:usernames) "
				+ "          and u.enabled=1 and i.acadYear = uc.acadYearCode "
				+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
		}else {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
					+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
					+ "          from user_course uc, ica i,users u "
					+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " and icm.componentId=:compId "
					+ " where i.id = :icaId and i.acadSession = uc.acadSession and u.username=uc.username and uc.username in(:usernames) "
					+ "          and u.enabled=1 and i.acadYear = uc.acadYearCode "
					+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
	}

	/*
	 * public List<UserCourse> findStudentByModuleIdAndAcadYearForBatch(String
	 * moduleId, String acadYear,String acadSession, String programId,String
	 * campusId) {
	 * 
	 * Map<String, Object> params = new HashMap<String, Object>(); if
	 * (programId.contains(",")) { List<String> programList =
	 * Arrays.asList(programId.split(",")); params.put("programIds", programList); }
	 * else { params.put("programIds", programId); } params.put("moduleId",
	 * moduleId); params.put("acadYear", acadYear); params.put("acadSession",
	 * acadSession); params.put("campusId", campusId);
	 * 
	 * if(campusId!=null){ String sql =
	 * " select distinct uc.username, CONCAT(u.firstname, ' ', u.lastname) as studentName  "
	 * + "          from user_course uc,course c,users u " + " where " +
	 * "          c.moduleId = :moduleId and c.acadYear=:acadYear and c.acadSession=:acadSession "
	 * + " and c.programId in(:programIds) and c.campusId=:campusId " +
	 * "  and c.id=uc.courseId and u.username=uc.username " +
	 * "          and u.enabled=1 " +
	 * "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' "; return
	 * getNamedParameterJdbcTemplate().query(sql, params,
	 * BeanPropertyRowMapper.newInstance(UserCourse.class)); }else{ String sql =
	 * " select distinct uc.username, CONCAT(u.firstname, ' ', u.lastname) as studentName  "
	 * + "          from user_course uc,course c,users u " + " where " +
	 * "          c.moduleId = :moduleId and c.acadYear=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
	 * + "  and c.id=uc.courseId and u.username=uc.username " +
	 * "          and u.enabled=1 " +
	 * "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' "; return
	 * getNamedParameterJdbcTemplate().query(sql, params,
	 * BeanPropertyRowMapper.newInstance(UserCourse.class)); } }
	 */

	public List<UserCourse> findStudentByModuleIdAndAcadYearForBatch(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct uc.username, CONCAT(u.firstname, ' ', u.lastname) as studentName, u.rollNo  "
					+ "          from user_course uc,course c,users u " + " where "
					+ "          c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "          and u.enabled=1 "
					+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct uc.username, CONCAT(u.firstname, ' ', u.lastname) as studentName, u.rollNo  "
					+ "          from user_course uc,course c,users u " + " where "
					+ "          c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "          and u.enabled=1 "
					+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId
		// });
	}

	public List<UserCourse> findFacultyNamesByIdsForBatch(List<String> facultyIds) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("facultyIds", facultyIds);

		String sql = " select distinct u.username, CONCAT(u.firstname, ' ', u.lastname) as facultyName  "
				+ "          from users u " + " where  u.username in (:facultyIds) ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	public List<UserCourse> findAllFacultyByFacultyIds(String facultyIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (facultyIds.contains(",")) {
			List<String> facultyList = Arrays.asList(facultyIds.split(","));
			params.put("facultyIds", facultyList);
		} else {
			params.put("facultyIds", facultyIds);
		}

		String sql = " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,users u "
				+ " where uc.role='ROLE_FACULTY' and uc.active = 'Y' "
				+ " and u.username=uc.username and uc.username in (:facultyIds) " + " group by uc.username ";

		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
	}

	public String getFacultyByCourseIdForAssignment(long courseId) {

		String sql = " select username from user_course "
				+ " where role = 'ROLE_FACULTY' and courseId = ? and active = 'Y' limit 1 ";

		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { courseId });
	}

	public List<UserCourse> findStudentsByYearMonthCourse(String acadYear, String acadMonth, String courseId) {
		String sql = " SELECT uc.* FROM user_course uc, course c WHERE c.id = uc.courseId AND c.acadMonth = uc.acadMonth "
				+ " AND c.acadYear = uc.acadYear AND c.acadSession = uc.acadSession and c.active = 'Y' and uc.active = 'Y' "
				+ " AND c.acadYear = ? AND c.acadMonth = ? AND c.id = ? AND uc.role = 'ROLE_STUDENT' ";
		return findAllSQL(sql, new Object[] { acadYear, acadMonth, courseId });
	}

	public List<UserCourse> findFacultiesByYearMonthCourse(String acadYear, String acadMonth, String courseId) {
		String sql = " SELECT uc.* FROM user_course uc, course c WHERE c.id = uc.courseId AND c.acadMonth = uc.acadMonth "
				+ " AND c.acadYear = uc.acadYear AND c.acadSession = uc.acadSession and c.active = 'Y' and uc.active = 'Y' "
				+ " AND c.acadYear = ? AND c.acadMonth = ? AND c.id = ? AND uc.role = 'ROLE_FACULTY' ";
		return findAllSQL(sql, new Object[] { acadYear, acadMonth, courseId });
	}

	public List<UserCourse> findStudentsByYearMonthCourseNew(String acadYear, String acadMonth, String courseId) {
		String sql = " SELECT uc.* FROM user_course uc, course c,users u WHERE c.id = uc.courseId AND c.acadMonth = uc.acadMonth "
				+ " AND c.acadYear = uc.acadYear AND c.acadSession = uc.acadSession and c.active = 'Y' and uc.active = 'Y' "
				+ " AND c.acadYear = ? AND c.acadMonth = ? AND c.id = ? AND uc.role = 'ROLE_STUDENT' and u.username = uc.username and u.enabled = 1";
		return findAllSQL(sql, new Object[] { acadYear, acadMonth, courseId });
	}

	public List<UserCourse> findFacultiesByYearMonthCourseNew(String acadYear, String acadMonth, String courseId) {
		String sql = " SELECT uc.* FROM user_course uc, course c,users u WHERE c.id = uc.courseId AND c.acadMonth = uc.acadMonth "
				+ " AND c.acadYear = uc.acadYear AND c.acadSession = uc.acadSession and c.active = 'Y' and uc.active = 'Y' "
				+ " AND c.acadYear = ? AND c.acadMonth = ? AND c.id = ? AND uc.role = 'ROLE_FACULTY' and u.username = uc.username and u.enabled = 1";
		return findAllSQL(sql, new Object[] { acadYear, acadMonth, courseId });
	}

	// NEW Queries FOR TEE

	public List<String> findDistinctStudentByModuleIdAndAcadYearForTEE(String moduleId, String acadYear,
			String acadSession, String programId, Long teeId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("teeId", teeId);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct uc.username" + " from user_course uc,course c,users u "
					+ " left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
					+ " where "

					+ "  c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + " and u.enabled=1 "
					+ " and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		} else {
			String sql = " select distinct uc.username" + "  from user_course uc,course c,users u "
					+ " left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
					+ " where "

					+ "  c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + " and u.enabled=1 "
					+ " and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		}

	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForTEE(String moduleId, String acadYear, String acadSession,
			String programId, Long teeId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("teeId", teeId);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " uc.username,uc.acadSession,icm.isAbsent,icm.teeTotalMarks,icm.remarks,icm.query,icm.teeScaledMarks,icm.isQueryRaise,icm.isQueryApproved  "
					+ "	 from user_course uc,course c,users u "
					+ " left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
					+ " where "

					+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " uc.username,uc.acadSession,icm.isAbsent,icm.teeTotalMarks,icm.remarks,icm.query,icm.teeScaledMarks,icm.isQueryRaise,icm.isQueryApproved  "
					+ "	 from user_course uc,course c,users u "
					+ " left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
					+ " where "

					+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
	}

	public List<UserCourse> findStudentByStudentListAndTeeId(List<String> usernames, Long teeId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usernames", usernames);
		params.put("teeId", teeId);

		String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
				+ "uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved,icm.teeTotalMarks,icm.remarks,icm.query,icm.teeScaledMarks  "
				+ "from user_course uc, tee t,users u "
				+ "left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
				+ "where t.id = :teeId and t.acadSession = uc.acadSession and u.username=uc.username and uc.username in(:usernames) "
				+ "and u.enabled=1 and t.acadYear = uc.acadYearCode "
				+ "and uc.active = 'Y' and uc.role = 'ROLE_STUDENT'  ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
	}

	public List<UserCourse> findStudentByEventIdAndAcadYearForTEE(String courseId, String acadYear, String acadSession,
			String programId, Long teeId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("courseId", courseId);
		params.put("acadYear", acadYear);
		params.put("acadSession", acadSession);
		params.put("teeId", teeId);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved,icm.teeTotalMarks,"
					+ " icm.remarks,icm.query,icm.teeScaledMarks  " + "	 from user_course uc,course c,users u "
					+ " left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
					+ " where "

					+ "	 c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession=:acadSession "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved,"
					+ " icm.teeTotalMarks,icm.remarks,icm.query,icm.teeScaledMarks  "
					+ "	 from user_course uc,course c,users u "
					+ " left outer join tee_total_marks icm on u.username=icm.username and icm.teeId=:teeId "
					+ " where "

					+ "	 c.id = :courseId and c.acadYearCode=:acadYear and c.acadSession=:acadSession and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
					+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
		// return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });

	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForBatchCE(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}

		if (acadSession.contains(",")) {
			List<String> acadSessionList = Arrays.asList(acadSession.split(","));
			params.put("acadSession", acadSessionList);
		} else {
			params.put("acadSession", acadSession);
		}
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		// params.put("acadSession", acadSession);
		params.put("campusId", campusId);

		if (campusId != null) {
			String sql = " select distinct uc.username, CONCAT(u.firstname, ' ', u.lastname) as studentName  "
					+ "          from user_course uc,course c,users u " + " where "
					+ "          c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) "
					+ " and c.programId in(:programIds) and c.campusId=:campusId "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "          and u.enabled=1 "
					+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		} else {
			String sql = " select distinct uc.username, CONCAT(u.firstname, ' ', u.lastname) as studentName  "
					+ "          from user_course uc,course c,users u " + " where "
					+ "          c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) and c.programId in(:programIds) "
					+ "  and c.id=uc.courseId and u.username=uc.username " + "          and u.enabled=1 "
					+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(UserCourse.class));
		}
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearCE(String moduleId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId,String isEvalCompWise,String compId) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}

		if (acadSession.contains(",")) {
			List<String> acadSessionList = Arrays.asList(acadSession.split(","));
			params.put("acadSession", acadSessionList);
		} else {
			params.put("acadSession", acadSession);
		}

		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		// params.put("acadSession", acadSession);
		params.put("icaId", icaId);
		params.put("campusId", campusId);
		params.put("compId", compId);

		if(!"Y".equals(isEvalCompWise)) {
			if (campusId != null) {
				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) "
						+ " and c.programId in(:programIds) and c.campusId=:campusId "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' group by u.username,icm.icaId";
				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {
				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) and c.programId in(:programIds) "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' group by u.username,icm.icaId";
				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
		}else {
			if (campusId != null) {
				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " and icm.componentId=:compId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) "
						+ " and c.programId in(:programIds) and c.campusId=:campusId "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' group by u.username,icm.icaId";
				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			} else {
				String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
						+ "	 from user_course uc,course c,users u "
						+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
						+ " and icm.componentId=:compId "
						+ " where "

						+ "	 c.moduleId = :moduleId and c.acadYearCode=:acadYear and c.acadSession in(:acadSession) and c.programId in(:programIds) "
						+ "  and c.id=uc.courseId and u.username=uc.username " + "	and u.enabled=1 "
						+ "	and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' group by u.username,icm.icaId";
				return getNamedParameterJdbcTemplate().query(sql, params,
						BeanPropertyRowMapper.newInstance(UserCourse.class));
			}
			
		}
	}

	public List<UserCourse> findStudentByStudentListAndIcaIdCE(List<String> usernames, Long icaId, String acadSession,
			String isEvalCompWise,String compId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usernames", usernames);
		params.put("icaId", icaId);
		if (acadSession.contains(",")) {
			params.put("acadSession", Arrays.asList(acadSession.split(",")));
		} else {
			params.put("acadSession", acadSession);
		}
		params.put("compId", compId);
		if(!"Y".equals(isEvalCompWise)) {
		String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
				+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
				+ "          from user_course uc, ica i,users u "
				+ " left outer join ica_total_marks icm on u.username=icm.username and icm.icaId=:icaId "
				+ " where i.id = :icaId  and u.username=uc.username and uc.username in(:usernames) and uc.acadSession in(:acadSession)"
				+ "          and u.enabled=1 and i.acadYear = uc.acadYearCode "
				+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
		}else {
			String sql = " select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName, "
					+ " uc.username,uc.acadSession,icm.isQueryRaise,icm.isAbsent,icm.isQueryApproved  "
					+ "          from user_course uc, ica i,users u "
					+ " left outer join ica_component_marks icm on u.username=icm.username and icm.icaId=:icaId "
					+ " and icm.componentId =:compId "
					+ " where i.id = :icaId  and u.username=uc.username and uc.username in(:usernames) and uc.acadSession in(:acadSession)"
					+ "          and u.enabled=1 and i.acadYear = uc.acadYearCode "
					+ "          and uc.active = 'Y' and uc.role = 'ROLE_STUDENT' ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
			
		}
	}

	public List<String> findAcadSessionListSupportAdmin() {
		String sql = " select distinct uc.acadSession from user_course uc,course c where uc.courseId=c.id and c.active='Y' ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] {});
	}

	public int makeInActiveBySupportAdmin(List<String> courseIdList, List<String> usernameList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseIdList", courseIdList);
		params.put("usernameList", usernameList);

		String sql = " update user_course set active='N' where courseId in(:courseIdList) and username in(:usernameList) ";
		return getNamedParameterJdbcTemplate().update(sql, params);

	}

	public List<String> findCoursesByParam(String moduleId, String programId, String acadYear, String username) {
		String sql = " select distinct c.id from  " + getTableName() + " uc,course uc "
				+ " where c.id=uc.courseId and c.moduleId =? and c.programId=? and c.acadYear=? "
				+ " and uc.username =? and c.active ='Y' ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { programId });
	}

	public List<UserCourse> getStudentsByModuleId(String moduleId, String acadYear) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		String sql = " select distinct uc.* from " + getTableName()
				+ " uc,course c where c.moduleId = :moduleId and c.acadYear=:acadYear "
				+ " and uc.active = 'Y' and c.id =uc.courseId and c.acadYear=uc.acadYear"
				+ "  and uc.role='ROLE_STUDENT' ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(UserCourse.class));
	}

	// acadYear change by Hiren 02-02-2021
	public List<UserCourse> findAllFacultyWithModuleIdTest(String moduleId, String acadYear, String campusId,
			String programId) {
		/*
		 * final String sql =
		 * " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
		 * + " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' " +
		 * " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? " +
		 * " and u.username=uc.username " + " group by uc.username";
		 */
		// acad year Code
		if (!"undefined".equals(campusId) && !"null".equals(campusId)) {
			final String sql = " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
					+ " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' "
					+ " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? and c.campusId =? and c.programId=? "
					+ " and u.username=uc.username " + " group by uc.username";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, campusId, programId });
		} else {
			final String sql = " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
					+ " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' "
					+ " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? and c.programId=? "
					+ " and u.username=uc.username " + " group by uc.username";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
		}

	}

	// acadYear change by Hiren 02-02-2021
	public List<UserCourse> findAllFacultyWithModuleAndProgram(String moduleId, String acadYear, String programId,
			String campusId) {
		/*
		 * final String sql =
		 * " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
		 * + " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' " +
		 * " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? " +
		 * " and u.username=uc.username " + " group by uc.username";
		 */
		// acad year Code
		if (campusId != null && !"null".equals(campusId)) {
			final String sql = " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
					+ " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' "
					+ " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? and c.programId=? and c.campusId=? "
					+ " and u.username=uc.username " + " group by uc.username";

			return findAllSQL(sql, new Object[] { moduleId, acadYear, programId, campusId });
		} else {
			final String sql = " SELECT uc.*,CONCAT(u.firstName,' ',u.lastName) as facultyName  FROM user_course uc,course c,users u "
					+ " where   role='ROLE_FACULTY' and uc.active = 'Y' and c.active = 'Y' "
					+ " and uc.courseId = c.id and c.moduleId = ? and c.acadYear = ? and c.programId=? "
					+ " and u.username=uc.username " + " group by uc.username";

			return findAllSQL(sql, new Object[] { moduleId, acadYear, programId });
		}
	}

	public String getCourseIdByModuleAndAcadYear(String username, String moduleId, String acadYear) {

		String sql = "SELECT c.id from course c, user_course uc where uc.username = ? and c.id = uc.courseId "
				+ "and c.moduleId = ? and c.acadYear = ? and uc.active = 'Y' and uc.active='Y' group by moduleId";
		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { username, moduleId, acadYear });
	}

	public List<String> getFacultyByCourse(String courseId) {
		String sql = "select distinct username from user_course where courseId = ? and role = 'ROLE_FACULTY' and active = 'Y' ";
		return listOfStringParameter(sql, new Object[] { courseId });

	}

	//Peter 22/10/2021
	public UserCourse getFacultyCourseId(String assignedFaculty, String moduleId) {
		String sql = "select courseId from course c join user_course uc on c.id=uc.courseId where uc.username=? and c.moduleId=? limit 1";
		return findOneSQL(sql,new Object[] { assignedFaculty, moduleId });
	}
}
