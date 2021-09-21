package com.spts.lms.daos.user;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.LmsDb;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.utils.PasswordGenerator;

@Repository
public class LmsUserDAO extends BaseDAO<User> {

	private static String tableName;

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}

	public void getLmsDb(LmsDb bean) {
		tableName = bean.getLmsDb() + ".users";
		System.out.println("table name is" + tableName);
	}

	@Override
	protected String getInsertSql() {
		String sql = "INSERT INTO "
				+ getTableName()
				+ "(username,firstname,lastname,password,enrollmentYear,enrollmentMonth,validityEndYear,validityEndMonth,fatherName,"
				+ "motherName,programId,email,mobile,enabled,attempts,createdDate,createdBy,lastModifiedDate,lastModifiedBy,acadSession,type)"
				+ "VALUES(:username,:firstname,:lastname,:password,:enrollmentYear,:enrollmentMonth,:validityEndYear,:validityEndMonth,"
				+ ":fatherName,:motherName,:programId,:email,:mobile,:enabled,:attempts,:createdDate,:createdBy,:lastModifiedDate,"
				+ ":lastModifiedBy,:acadSession,:type)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "INSERT INTO "
				+ getTableName()
				+ "(username,firstname,lastname,password,enrollmentYear,enrollmentMonth,validityEndYear,validityEndMonth,fatherName,"
				+ "motherName,programId,email,mobile,enabled,createdDate,createdBy,lastModifiedDate,lastModifiedBy,acadSession,type)"

				+ "VALUES"

				+ "(:username,:firstname,:lastname,:password,:enrollmentYear,:enrollmentMonth,:validityEndYear,:validityEndMonth,"
				+ ":fatherName,:motherName,:programId,:email,:mobile,:enabled,:createdDate,:createdBy,:lastModifiedDate,"
				+ ":lastModifiedBy,:acadSession,:type)"

				+ " ON DUPLICATE KEY UPDATE "

				+ " firstname = :firstname," + " lastname = :lastname,"
				+ " password = :password,"
				+ " enrollmentYear = :enrollmentYear,"
				+ " enrollmentMonth = :enrollmentMonth,"
				+ " validityEndYear = :validityEndYear,"
				+ " validityEndMonth = :validityEndMonth,"
				+ " fatherName = :fatherName," + " motherName = :motherName,"
				+ " programId = :programId," + " email = :email,"
				+ " mobile = :mobile," + " acadSession = :acadSession,"
				+ " type = :type," + " lastModifiedDate = :lastModifiedDate,"
				+ " lastModifiedBy = :lastModifiedBy";
		return sql;
	}

	public User findByEmail(final String email) {
		final String sql = "Select firstname, lastname, username, enabled, email, createdDate, lastModifiedDate, createdBy, lastModifiedBy from "
				+ getTableName() + " where email = ?";
		return findOneSQL(sql, new Object[] { email });
	}

	public User findCredsByUserName(final String username) {
		final String sql = "Select u.firstname, u.lastname, u.username, u.password, u.enabled, u.email, u.mobile, u.programId, p.programName, u.attempts, u.createdDate, u.lastModifiedDate, u.createdBy, u.lastModifiedBy from "
				+ getTableName()
				+ " u left outer join program p on p.id = u.programId where username = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	public User findStudentObjectId(final String username) {
		final String sql = "Select * from  student_mapping"
				+ " where student_no = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	// @Cacheable("users")
	public User findByUserName(final String username) {
		final String sql = "Select * from " + getTableName()
				+ " where username = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	public int makeUserInactive(User user) {

		final String sql = " update " + getTableName()
				+ " set enabled = 0 where username = ? ";

		return getJdbcTemplate().update(sql,
				new Object[] { user.getUsername() });
	}

	public List<User> findUsers(List<String> userList) {
		String sql = " select * from " + getTableName()
				+ " where username in (:userList)";
		Map<String, Object> params = Collections.singletonMap("userList",
				userList);
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public String resetPassword(final User user) {
		String newPassword = generateNewPassword(user);
		String sql = "Update "
				+ getTableName()
				+ " set password = :password, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username";
		updateSQL(user, sql);

		logger.info("New Password = " + newPassword);
		return newPassword;
	}

	public List<User> findAllFacultiesByDept(String department) {
		final String sql = " select u.* from course c, user_course uc , Users u where c.id = uc.courseId and u.username = uc.username "
				+ " and  c.dept =? and uc.role = ? group by u.username ";
		return findAllSQL(sql,
				new Object[] { department, Role.ROLE_FACULTY.name() });
	}

	public String findFacultyType(String username) {
		String sql = " select type from " + getTableName()
				+ " where username= ?";
		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] { username });
	}

	public String generateNewPassword(final User user) {
		String newPassword = RandomStringUtils.randomAlphanumeric(8);
		user.setPassword(PasswordGenerator.generatePassword(newPassword));
		return newPassword;
	}

	public int changePassword(final User user) {
		String sql = "Update "
				+ getTableName()
				+ " set password = :password, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		return updateSQL(user, sql);
	}

	public List<User> findCampus() {
		String sql = "select distinct u.campusId,u.campusName from users u where u.campusName <> ''";
		return findAllSQL(sql, new Object[] {});
	}

	public String findMyChild(String username) {
		// final String sql =
		// "select sp.stud_username from student_parent sp where sp.parent_username = ?";
		// return findOneSQL(sql, new Object[] { username });
		// returnSingleColumn(sql, new Object[]{username});

		return returnSingleColumn(
				"select sp.stud_username from student_parent sp where sp.parent_username = ?",
				new Object[] { username });
	}

	public int resetFailAttempts(final String username) {
		String sql = "Update "
				+ getTableName()
				+ " set attempts = 0, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		final User user = new User();
		user.setUsername(username);
		user.setLastModifiedBy(username);
		return updateSQL(user, sql);
	}

	public int updateFailAttempts(final String username) {
		String sql = "Update "
				+ getTableName()
				+ " set attempts = attempts + 1, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		final User user = new User();
		user.setUsername(username);
		user.setLastModifiedBy(username);
		return updateSQL(user, sql);
	}

	public User findUserAndProgramByUserName(String username) {
		final String sql = "Select u.*, p.abbr as abbr from users u inner join program p on p.id = u.programId where username = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	public int updateProfile(User user) {
		String sql = "Update "
				+ getTableName()
				+ " set email = :email, mobile = :mobile , lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		return updateSQL(user, sql);

	}

	public List<User> findAllTestScoreByCourse(Long courseId, String acadMonth,
			String acadYear) {
		final String sql = "select u.firstname, u.lastname, group_concat(IF(st.id is null, 'NA', coalesce(st.score, 'NC')) order by testName) as studentTestScore "
				+ " from users u "
				+ " inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test t on t.courseId = uc.courseId and t.acadMonth = t.acadMonth and t.acadYear = uc.acadYear "
				+ " left outer join student_test st on st.testId = t.id and st.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? "
				+ " group by firstname, lastname";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId, acadMonth, acadYear });
	}

	public List<User> findAllAssignmentScoreByCourse(Long courseId,
			String acadMonth, String acadYear) {
		final String sql = "select distinct u.firstname, u.lastname, a.assignmentName,( case when  asub.submissionStatus <> 'Y' then 'NC' "
				+ " when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as studentAssignmentScore  "
				+ " from users u "
				+ " inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join assignment a on a.courseId = uc.courseId and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? "
				+ " group by firstname, lastname";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId, acadMonth, acadYear });
	}

	public List<User> findByRole(Role role) {
		final String sql = "select u.firstname, u.lastname, u.username "
				+ " from users u "
				+ " inner join user_roles ur on ur.username = u.username and ur.role = ? ";
		return findAllSQL(sql, new Object[] { role.name() });
	}

	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseList(
			Long courseId, String acadMonth, String acadYear) {
		final String sql = "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ " then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
				+ "from users u inner join user_course uc on uc.username = u.username and uc.role = ? 	inner join assignment a on a.courseId = uc.courseId "

				+ "left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "	where uc.courseId = ? "
				+ "group by firstname, lastname				"
				+ "union "
				+ "select distinct concat(u.firstname, u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ "when asub.id is null then 'NA' else asub.score end) as score  ,'TEST' as typ  "
				+ "from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ "inner join test a"
				+ " on a.courseId = uc.courseId"
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
				+ " where uc.courseId = ?" + " group by firstname, lastname";

		return getJdbcTemplate().queryForList(
				sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth,
						acadYear, Role.ROLE_STUDENT.name(), courseId,
						acadMonth, acadYear });

	}

	/*
	 * public List<Map<String, Object>>
	 * findAllTestScoreAndAssigmentByCourseList( Long courseId) { final String
	 * sql =
	 * "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
	 * +
	 * " then 'NC'         when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
	 * +
	 * "from users u inner join user_course uc on uc.username = u.username and uc.role = ?    inner join assignment a on uc.courseId = uc.courseId  inner join course c on a.courseId = c.id"
	 * +
	 * " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
	 * + "     where uc.courseId = ? and c.active='Y'   " +
	 * "group by firstname, lastname                                  " +
	 * "union " +
	 * "select distinct concat(u.firstname, u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
	 * +
	 * "when asub.id is null then 'NA' else asub.score end) as score  ,'TEST' as typ  "
	 * +
	 * "from users u    inner join user_course uc on uc.username = u.username and uc.role = ? inner join course c on uc.courseId = c.id "
	 * + "inner join test a" + " on a.courseId = uc.courseId " +
	 * " left outer join student_test asub on asub.testId = a.id and asub.username = u.username          "
	 * + " where uc.courseId = ? and c.active='Y' " +
	 * " group by firstname, lastname";
	 * 
	 * return getJdbcTemplate().queryForList( sql, new Object[] {
	 * Role.ROLE_STUDENT.name(), courseId, Role.ROLE_STUDENT.name(), courseId
	 * });
	 */
	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseList(
			Long courseId) {
		final String sql = "(select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ " then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'NA' as status,'ASSIGNEMENT' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? 	inner join assignment a on a.courseId = uc.courseId  inner join course c on a.courseId = c.id"
				+ "  "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "	where uc.courseId = ? and c.active='Y' and asub.active='Y')"

				+ " union "
				+ " (select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ " when asub.id is null then 'NA' else asub.score end ) as score, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ"

				+ " from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? inner join course c on uc.courseId = c.id "
				+ " inner join test a"
				+ " on a.courseId = uc.courseId "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
				+ " where uc.courseId = ? and c.active='Y' and a.active='Y') "
				+ "union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,'Class Participation' as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'CP' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ " where uc.courseId=? order by NAME asc";

		return getJdbcTemplate().queryForList(
				sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId,
						Role.ROLE_STUDENT.name(), courseId,
						Role.ROLE_STUDENT.name(), courseId });

	}

	public List<Map<String, Object>> findTestScoreAndAssigmentByCourseListByUsername(
			String username, Long courseId, String acadMonth, String acadYear) {
		/*
		 * final String sql =
		 * "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
		 * +
		 * " then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
		 * +
		 * "from users u inner join user_course uc on uc.username = u.username and  uc.role = ? 	inner join assignment a on a.courseId = uc.courseId "
		 * + "and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear  " +
		 * "left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username and asub.username = ?"
		 * + "	where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? "
		 * + "group by firstname, lastname				" + "union " +
		 * "select distinct concat(u.firstname, u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
		 * +
		 * "when asub.id is null then 'NA' else asub.score end) as score  ,'TEST' as typ  "
		 * +
		 * "from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
		 * + "inner join test a" +
		 * " on a.courseId = uc.courseId and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear"
		 * +
		 * " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
		 * +
		 * " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? and asub.username = ?"
		 * + " group by firstname, lastname";
		 */

		final String sql = "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ "	  then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
				+ "	 from users u inner join user_course uc on uc.username = u.username and  uc.role = ?	inner join assignment a on a.courseId = uc.courseId "
				+ " "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "	 	where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? and asub.username = ? "
				+ " group by firstname, lastname "
				+ "union "
				+ " select distinct concat(u.firstname, u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC' "
				+ " when asub.id is null then 'NA' else asub.score end) as score  ,'TEST' as typ  "
				+ " from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test a "
				+ "  on a.courseId = uc.courseId  "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? and asub.username = ? "
				+ " group by firstname, lastname";
		logger.info("called---------->");
		return getJdbcTemplate().queryForList(
				sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth,
						acadYear, username, Role.ROLE_STUDENT.name(), courseId,
						acadMonth, acadYear, username });

	}

	public List<Map<String, Object>> findTestScoreAndAssigmentByCourseListByUsername(
			String username, Long courseId) {
		final String sql = "select distinct concat(u.firstname,' ', u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ "	  then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as score,'NA' as status,'ASSIGNEMENT' as typ "
				+ "	 from users u inner join user_course uc on uc.username = u.username and  uc.role = ?	inner join assignment a on a.courseId = uc.courseId "
				+ "	 inner join course c on a.courseId = c.id "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "	 	where uc.courseId = ? and c.active='Y' and asub.username = ? and asub.active='Y' "

				+ " union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC' "
				+ " when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as score, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ"

				+ " from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test a "
				+ "  on a.courseId = uc.courseId   inner join course c on a.courseId = c.id "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
				+ " where uc.courseId = ?  and asub.username = ? and c.active='Y' and a.active='Y' "
				+ "union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name, 'Class Participation' as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'CP' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ " where uc.courseId=? and cp.username=? ";

		logger.info("called---------->");
		return getJdbcTemplate().queryForList(
				sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId, username,
						Role.ROLE_STUDENT.name(), courseId, username,
						Role.ROLE_STUDENT.name(), courseId, username });

	}

	public List<Map<String, Object>> findAllAssignmentScoreByCourseList(
			Long courseId, String acadMonth, String acadYear) {
		final String sql = "select distinct u.firstname, u.lastname, a.assignmentName,( case when  asub.submissionStatus <> 'Y' then 'NC' "
				+ " when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as studentAssignmentScore  "
				+ " from users u "
				+ " inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join assignment a on a.courseId = uc.courseId and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? "
				+ " group by firstname, lastname";
		return getJdbcTemplate().queryForList(
				sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth,
						acadYear });
	}

	public List<User> findFacultyByCourse(String courseId) {
		final String sql = "select distinct uc.username from user_course uc where uc.courseId= ? and uc.role='ROLE_FACULTY' ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<User> getUserDetails(String username) {
		final String sql = "select * from users u where u.username=? ";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<User> findAllStudents(Long programId) {
		String sql = "select u.*, ur.* from users u"
				+ " inner join user_roles ur on ur.username = u.username"
				+ " where u.programId = ? and ur.role = ? and u.enabled = 1";
		return findAllSQL(sql,
				new Object[] { programId, Role.ROLE_STUDENT.name() });
	}

	public String getProgramName(String username) {
		final String sql = "select p.programName  from program p,users u where p.id = u.programId and u.username=? ";
		return returnSingleColumn(sql, new Object[] { username });
	}

	public void updateStudentToMakeInactive(String username) {
		executeUpdateSql(
				"Update users u , user_roles ur set u.active = 'N' where u.username=? and ur.role='ROLE_STUDENT'",
				new Object[] { username });
	}

	public String getFacultyType(String username) {
		String sql = "select uu.`type` from usermanagement.uusers uu , users u where u.username=uu.username and u.username=? ";
		return returnSingleColumn(sql, new Object[] { username });

	}

	public List<User> findAllFacultyByProgramId(Long programId) {
		String sql = "select distinct u.* from users u, course c ,user_course uc, program p where u.username = uc.username and  uc.role ='ROLE_FACULTY' and c.id = uc.courseId and c.programId = p.id and p.id = ?";
		return findAllSQL(sql, new Object[] { programId });
	}

	public List<User> findAllUsersByProgramId(String role, Long programId) {

		String sql = "select distinct u.* from users u, course c ,user_course uc, program p where u.username = uc.username and  uc.role =? and c.id = uc.courseId and c.programId = p.id and p.id = ?";

		return findAllSQL(sql, new Object[] { role, programId });

	}

	public String findLibraryName() {
		String sql = "select libraryName from library_name";
		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] {});
	}

	public String findCampusName(Long campusId) {
		String sql = "select distinct campusName from " + getTableName()
				+ " where campusId = ?";
		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] { campusId });
	}

	public List<User> findAllActiveUsers() {
		String sql = " select distinct u.* from users u,user_course uc,course c,program p "
				+ " where u.username=uc.username and c.id =uc.courseId and c.programId=p.id "
				+ " and u.enabled=1 and uc.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public Map<String, Map<String, String>> findEmailByUserName(
			List<String> username) {
		Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
		List<User> userList = findUsers(username);
		Map<String, String> emails = new HashMap<String, String>();
		Map<String, String> mobiles = new HashMap<String, String>();
		for (User u : userList) {
			String email = u.getEmail();
			String mobile = u.getMobile();
			if (email != null && !email.isEmpty()) {
				email = email.trim();
				if (!StringUtils.isEmpty(email)
						&& !StringUtils.isWhitespace(email))
					emails.put(u.getUsername(), email);
			}
			if (mobile != null && !mobile.isEmpty()) {
				mobile = mobile.trim();
				if (!StringUtils.isEmpty(mobile)
						&& !StringUtils.isWhitespace(mobile))
					mobiles.put(u.getUsername(), mobile);
			}
		}

		result.put("emails", emails);
		result.put("mobiles", mobiles);
		return result;

	}

}
