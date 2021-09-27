package com.spts.lms.daos.user;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.utils.PasswordGenerator;
import com.spts.lms.web.utils.Utils;

@Repository
public class UserDAO extends BaseDAO<User> {

	@Override
	protected String getTableName() {
		return "users";
	}

	@Override
	protected String getInsertSql() {
		String sql = "INSERT INTO " + getTableName()
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
		String sql = "INSERT INTO " + getTableName()
				+ "(username,firstname,lastname,password,enrollmentYear,enrollmentMonth,validityEndYear,validityEndMonth,fatherName,"
				+ "motherName,programId,email,mobile,enabled,createdDate,createdBy,lastModifiedDate,lastModifiedBy,acadSession,type)"

				+ "VALUES"

				+ "(:username,:firstname,:lastname,:password,:enrollmentYear,:enrollmentMonth,:validityEndYear,:validityEndMonth,"
				+ ":fatherName,:motherName,:programId,:email,:mobile,:enabled,:createdDate,:createdBy,:lastModifiedDate,"
				+ ":lastModifiedBy,:acadSession,:type)"

				+ " ON DUPLICATE KEY UPDATE "

				+ " firstname = :firstname," + " lastname = :lastname," + " password = :password,"
				+ " enrollmentYear = :enrollmentYear," + " enrollmentMonth = :enrollmentMonth,"
				+ " validityEndYear = :validityEndYear," + " validityEndMonth = :validityEndMonth,"
				+ " fatherName = :fatherName," + " motherName = :motherName," + " programId = :programId,"
				+ " email = :email," + " mobile = :mobile," + " acadSession = :acadSession," + " type = :type,"
				+ " lastModifiedDate = :lastModifiedDate," + " lastModifiedBy = :lastModifiedBy";
		return sql;
	}

	public User findByEmail(final String email) {
		final String sql = "Select firstname, lastname, username, enabled, email, createdDate, lastModifiedDate, createdBy, lastModifiedBy from "
				+ getTableName() + " where email = ?";
		return findOneSQL(sql, new Object[] { email });
	}

	public User findCredsByUserName(final String username) {
		final String sql = "Select u.firstname, u.lastname, u.username, u.password, u.enabled, u.email, u.mobile, u.programId, p.programName, u.attempts, u.createdDate, u.lastModifiedDate, u.createdBy, u.lastModifiedBy from "
				+ getTableName() + " u left outer join program p on p.id = u.programId where username = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	public User findStudentObjectId(final String username) {
		final String sql = "Select * from  student_mapping" + " where student_no = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	// @Cacheable("users")
	public User findByUserName(final String username) {
		final String sql = "Select * from " + getTableName() + " where username = ?";
		return findOneSQL(sql, new Object[] { username });
	}

	public User findDetailsUserName(final String username) {
		final String sql = "Select u.*,p.programName from " + getTableName()
				+ " u,program p where u.username = ? and u.programId=p.id";
		return findOneSQL(sql, new Object[] { username });
	}

	public int makeUserInactive(User user) {

		final String sql = " update " + getTableName() + " set enabled = 0 where username = ? ";

		return getJdbcTemplate().update(sql, new Object[] { user.getUsername() });
	}

	public List<User> findUsers(List<String> userList) {
		String sql = " select * from " + getTableName() + " where username in (:userList)";
		Map<String, Object> params = Collections.singletonMap("userList", userList);
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(genericType));
	}

	public String resetPassword(final User user) {
		String newPassword = generateNewPassword(user);
		String sql = "Update " + getTableName()
				+ " set password = :password, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username";
		updateSQL(user, sql);

		logger.info("New Password = " + newPassword);
		return newPassword;
	}

	public List<User> findAllFacultiesByDept(String department) {
		final String sql = " select u.* from course c, user_course uc , users u where c.id = uc.courseId and u.username = uc.username "
				+ " and  c.dept =? and uc.role = ? group by u.username ";
		return findAllSQL(sql, new Object[] { department, Role.ROLE_FACULTY.name() });
	}

	public String findFacultyType(String username) {
		String sql = " select type from " + getTableName() + " where username= ?";
		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { username });
	}

	public String generateNewPassword(final User user) {
		String newPassword = RandomStringUtils.randomAlphanumeric(8);
		user.setPassword(PasswordGenerator.generatePassword(newPassword));
		return newPassword;
	}

	public int changePassword(final User user) {
		String sql = "Update " + getTableName()
				+ " set password = :password, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		return updateSQL(user, sql);
	}

	public List<User> findCampus() {
		String sql = "select distinct(p.campusId), p.campusName  from program_campus p ";
		return findAllSQL(sql, new Object[] {});
	}

	public String findMyChild(String username) {
		// final String sql =
		// "select sp.stud_username from student_parent sp where sp.parent_username =
		// ?";
		// return findOneSQL(sql, new Object[] { username });
		// returnSingleColumn(sql, new Object[]{username});

		return returnSingleColumn("select sp.stud_username from student_parent sp where sp.parent_username = ?",
				new Object[] { username });
	}

	public int resetFailAttempts(final String username) {
		String sql = "Update " + getTableName()
				+ " set attempts = 0, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		final User user = new User();
		user.setUsername(username);
		user.setLastModifiedBy(username);
		return updateSQL(user, sql);
	}

	public int updateFailAttempts(final String username) {
		String sql = "Update " + getTableName()
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
		String sql = "Update " + getTableName()
				+ " set email = :email, mobile = :mobile, address = :address, lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		return updateSQL(user, sql);

	}

	public List<User> findAllTestScoreByCourse(Long courseId, String acadMonth, String acadYear) {
		final String sql = "select u.firstname, u.lastname, group_concat(IF(st.id is null, 'NA', coalesce(st.score, 'NC')) order by testName) as studentTestScore "
				+ " from users u " + " inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test t on t.courseId = uc.courseId and t.acadMonth = t.acadMonth and t.acadYear = uc.acadYear "
				+ " left outer join student_test st on st.testId = t.id and st.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? " + " group by firstname, lastname";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear });
	}

	public List<User> findAllAssignmentScoreByCourse(Long courseId, String acadMonth, String acadYear) {
		final String sql = "select distinct u.firstname, u.lastname, a.assignmentName,( case when  asub.submissionStatus <> 'Y' then 'NC' "
				+ " when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as studentAssignmentScore  "
				+ " from users u " + " inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join assignment a on a.courseId = uc.courseId and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? " + " group by firstname, lastname";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear });
	}

	public List<User> findByRole(Role role) {
		final String sql = "select u.firstname, u.lastname, u.username " + " from users u "
				+ " inner join user_roles ur on ur.username = u.username and ur.role = ? where u.active = 'Y' ";
		return findAllSQL(sql, new Object[] { role.name() });
	}

	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseList(Long courseId, String acadMonth,
			String acadYear) {
		final String sql = "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ " then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
				+ "from users u inner join user_course uc on uc.username = u.username and uc.role = ? 	inner join assignment a on a.courseId = uc.courseId "

				+ "left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "	where uc.courseId = ? " + "group by firstname, lastname				" + "union "
				+ "select distinct concat(u.firstname, u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ "when asub.id is null then 'NA' else asub.score end) as score  ,'TEST' as typ  "
				+ "from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ "inner join test a" + " on a.courseId = uc.courseId"
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
				+ " where uc.courseId = ?" + " group by firstname, lastname";

		return getJdbcTemplate().queryForList(sql, new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth,
				acadYear, Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear });

	}

	/*
	 * public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseList(
	 * Long courseId) { final String sql =
	 * "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
	 * +
	 * " then 'NC'         when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
	 * +
	 * "from users u inner join user_course uc on uc.username = u.username and uc.role = ?    inner join assignment a on uc.courseId = uc.courseId  inner join course c on a.courseId = c.id"
	 * +
	 * " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
	 * + "     where uc.courseId = ? and c.active='Y'   " +
	 * "group by firstname, lastname                                  " + "union " +
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
	 * Role.ROLE_STUDENT.name(), courseId, Role.ROLE_STUDENT.name(), courseId });
	 */
	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseList(
			Long courseId) {
		final String sql = "select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ " then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'NA' as status,'ASSIGNEMENT' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? 	inner join assignment a on a.courseId = uc.courseId  inner join course c on a.courseId = c.id"
				+ "  "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "	where uc.courseId = ? and c.active='Y' and asub.active='Y' and uc.active = 'Y' and u.enabled ='1' "

				+ " union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ " when asub.id is null then 'NA' else asub.score end ) as score, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ"

				+ " from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? inner join course c on uc.courseId = c.id "
				+ " inner join test a"
				+ " on a.courseId = uc.courseId "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
				+ " where uc.courseId = ? and c.active='Y' and a.active='Y' and uc.active = 'Y' and u.enabled ='1' "
				+ "union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,'Class Participation' as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'CP' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ " where uc.courseId=? and uc.active = 'Y' and u.enabled ='1' "
				+ " union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,ot.testName "
				+ " as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'Offline-Test' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join offline_test ot on uc.courseId=ot.courseId and ot.active = 'Y' "
				+ " left outer join student_offline_test cp on cp.username = u.username "
				+ " where uc.courseId= ? and uc.active = 'Y' and u.enabled ='1' order by rollNo asc ";

		return getJdbcTemplate().queryForList(
				sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId,
						Role.ROLE_STUDENT.name(), courseId,
						Role.ROLE_STUDENT.name(), courseId,
						Role.ROLE_STUDENT.name(), courseId});

	}

	
	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseListAdmin(String moduleId) {
		final String sql = " (select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,"
				+ " a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ " when asub.id is null then 'NA' else asub.score end ) as score, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ" + ""
				+ " from users u  	inner join user_course uc on uc.username = u.username "
				+ " and uc.role = ? inner join course c on uc.courseId = c.id " + " inner join test a"
				+ " on a.moduleId = c.moduleId "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
				+ " WHERE c.moduleId=? and c.active='Y' and a.active='Y') ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { Role.ROLE_STUDENT.name(),moduleId });

	}
	
	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseListCreatedByAdminForStudent(String username,String moduleId) {
		final String sql = " (select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,"
				+ " a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ " when asub.id is null then 'NA' else asub.score end ) as score, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ" + ""
				+ " from users u  	inner join user_course uc on uc.username = u.username "
				+ " and uc.role = ? inner join course c on uc.courseId = c.id " + " inner join test a"
				+ " on a.moduleId = c.moduleId "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username and asub.username=?	"
				+ " WHERE c.moduleId=? and c.active='Y' and a.active='Y') ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { Role.ROLE_STUDENT.name(),username,moduleId });

	}
	
	public List<Map<String, Object>> findAllTestScoreAndAssigmentByCourseListCreatedByAdminForFaculty(List<String> courseIds) {
		Map<String,Object> mapper = new HashMap<>();
		mapper.put("role", Role.ROLE_STUDENT.name());
		mapper.put("courseIds",courseIds);
	
		
		
		final String sql = " (select distinct concat(u.firstname,' ', u.lastname) as name,u.username as sapid,u.rollNo as rollNo,"
				+ " a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC'  "
				+ " when asub.id is null then 'NA' else asub.score end ) as score, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ" + ""
				+ " from users u  	inner join user_course uc on uc.username = u.username "
				+ " and uc.role =:role inner join course c on uc.courseId = c.id " + " inner join test a"
				+ " on a.moduleId = c.moduleId "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username 	"
				+ " WHERE uc.courseId in(:courseIds) and c.active='Y' and a.active='Y'  and uc.active = 'Y' and u.enabled ='1') ";

		return getNamedParameterJdbcTemplate().queryForList(sql, mapper);

	}

	public List<Map<String, Object>> findTestScoreAndAssigmentByCourseListByUsername(String username, Long courseId,
			String acadMonth, String acadYear) {
		/*
		 * final String sql =
		 * "select distinct concat(u.firstname, u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
		 * +
		 * " then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as score,'ASSIGNEMENT' as typ "
		 * +
		 * "from users u inner join user_course uc on uc.username = u.username and  uc.role = ? 	inner join assignment a on a.courseId = uc.courseId "
		 * + "and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear  " +
		 * "left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username and asub.username = ?"
		 * + "	where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? " +
		 * "group by firstname, lastname				" + "union " +
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
				+ " group by firstname, lastname " + "union "
				+ " select distinct concat(u.firstname, u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC' "
				+ " when asub.id is null then 'NA' else asub.score end) as score  ,'TEST' as typ  "
				+ " from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test a " + "  on a.courseId = uc.courseId  "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? and asub.username = ? "
				+ " group by firstname, lastname";
		logger.info("called---------->");
		return getJdbcTemplate().queryForList(sql, new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth,
				acadYear, username, Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear, username });

	}

	/*
	 * public List<Map<String, Object>>
	 * findTestScoreAndAssigmentByCourseListByUsername( String username, Long
	 * courseId) { final String sql =
	 * "select distinct concat(u.firstname,' ', u.lastname) as name, a.assignmentName as nameToShow,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
	 * +
	 * "	  then 'NC' 	when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as score,'NA' as status,'ASSIGNEMENT' as typ "
	 * +
	 * "	 from users u inner join user_course uc on uc.username = u.username and  uc.role = ?	inner join assignment a on a.courseId = uc.courseId "
	 * + "	 inner join course c on a.courseId = c.id " +
	 * " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
	 * +
	 * "	 	where uc.courseId = ? and c.active='Y' and asub.username = ? and asub.active='Y' "
	 * 
	 * + " union " +
	 * " select distinct concat(u.firstname,' ', u.lastname) as name, a.testName as nameToShow,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC' "
	 * +
	 * " when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as score, "
	 * +
	 * " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' else "
	 * +
	 * " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status,'TEST' as typ"
	 * 
	 * +
	 * " from users u  	inner join user_course uc on uc.username = u.username and uc.role = ? "
	 * + " inner join test a " +
	 * "  on a.courseId = uc.courseId   inner join course c on a.courseId = c.id " +
	 * " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
	 * +
	 * " where uc.courseId = ?  and asub.username = ? and c.active='Y' and a.active='Y' "
	 * + "union " +
	 * " select distinct concat(u.firstname,' ', u.lastname) as name, 'Class Participation' as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'CP' as typ "
	 * +
	 * " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
	 * +
	 * " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
	 * 
	 * + " union " +
	 * " select distinct concat(u.firstname,' ', u.lastname) as name,ot.testName " +
	 * " as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'Offline-Test' as typ "
	 * +
	 * " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
	 * +
	 * " inner join offline_test ot on uc.courseId=ot.courseId and ot.active = 'Y'"
	 * + " left outer join student_offline_test cp on cp.username = u.username "
	 * 
	 * + " where uc.courseId=? and cp.username=? ";
	 * 
	 * logger.info("called---------->"); return getJdbcTemplate().queryForList( sql,
	 * new Object[] { Role.ROLE_STUDENT.name(), courseId, username,
	 * Role.ROLE_STUDENT.name(), courseId, username, Role.ROLE_STUDENT.name(),
	 * courseId, username, Role.ROLE_STUDENT.name(), courseId, username});
	 * 
	 * }
	 */

	public List<Map<String, Object>> findTestScoreAndAssigmentByCourseListByUsername(String username, Long courseId) {
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
				+ " inner join test a " + "  on a.courseId = uc.courseId   inner join course c on a.courseId = c.id "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
				+ " where uc.courseId = ?  and asub.username = ? and c.active='Y' and a.active='Y' " + "union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name, 'Class Participation' as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'CP' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ " where uc.courseId=? and cp.username=? " + " union "
				+ " select distinct concat(u.firstname,' ', u.lastname) as name,ot.testName "
				+ " as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score,'NA' as status,'Offline-Test' as typ "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join offline_test ot on uc.courseId=ot.courseId and ot.active = 'Y' "
				+ " left outer join student_offline_test cp on cp.username = u.username "
				+ " where uc.courseId= ? and cp.username = ?  ";

		// logger.info("called---------->");
		return getJdbcTemplate().queryForList(sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId, username, Role.ROLE_STUDENT.name(), courseId,
						username, Role.ROLE_STUDENT.name(), courseId, username, Role.ROLE_STUDENT.name(), courseId,
						username });

	}

	public List<Map<String, Object>> findAllAssignmentScoreByCourseList(Long courseId, String acadMonth,
			String acadYear) {
		final String sql = "select distinct u.firstname, u.lastname, a.assignmentName,( case when  asub.submissionStatus <> 'Y' then 'NC' "
				+ " when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' else asub.score end) as studentAssignmentScore  "
				+ " from users u " + " inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join assignment a on a.courseId = uc.courseId and a.acadMonth = uc.acadMonth and a.acadYear = uc.acadYear "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ " where uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? " + " group by firstname, lastname";
		return getJdbcTemplate().queryForList(sql,
				new Object[] { Role.ROLE_STUDENT.name(), courseId, acadMonth, acadYear });
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
		String sql = "select u.*, ur.* from users u" + " inner join user_roles ur on ur.username = u.username"
				+ " where u.programId = ? and ur.role = ? and u.enabled = 1";
		return findAllSQL(sql, new Object[] { programId, Role.ROLE_STUDENT.name() });
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
		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] {});
	}

	public String findCampusName(Long campusId) {
		String sql = "select distinct campusName from program_campus where campusId =? ";
		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { campusId });
	}

	public List<User> findAllActiveUsers(String campusId) {
		/*
		 * String sql =
		 * " select distinct u.* from users u,user_course uc,course c,program p " +
		 * " where u.username=uc.username and c.id =uc.courseId and c.programId=p.id " +
		 * " and u.enabled=1 and uc.active = 'Y' ";
		 * 
		 * return findAllSQL(sql, new Object[] {});
		 */

		String sql = "";
		if (!campusId.equals("null")) {
			sql = " select distinct u.* from users u,user_course uc,course c,program p "
					+ " where u.username=uc.username and c.id =uc.courseId and c.programId=p.id "
					+ " and u.enabled=1 and uc.active = 'Y' and c.campusId = ? ";
			return findAllSQL(sql, new Object[] { campusId });
		} else {
			sql = " select distinct u.* from users u,user_course uc,course c,program p "
					+ " where u.username=uc.username and c.id =uc.courseId and c.programId=p.id "
					+ " and u.enabled=1 and uc.active = 'Y' ";
			return findAllSQL(sql, new Object[] {});
		}
	}

	public List<User> checkCreatedByRoleStats(String campus, String tableName, Role role, String fromDate,
			String toDate, String type) {
		String typeCondition = "";
		if (type != null) {
			typeCondition = " u.`type` = ? ";
		} else {
			typeCondition = " u.`type`is ? ";
		}

		String campusCondition = "";
		if (campus != null) {
			campusCondition = " c.campusId = ? ";
		} else {
			campusCondition = " c.campusId is ? ";
		}

		final String sql = " select u.*,t.*,c.* from " + tableName
				+ " t , user_roles ur , users u ,course c where t.createdBy = ur.username and u.username = ur.username and c.id = t.courseId and "
				+ campusCondition + " and u.active = 'Y' and " + typeCondition
				+ " and ur.role=? and t.active ='Y' and   t.lastModifiedDate BETWEEN  ? and ? ";
		return findAllSQL(sql, new Object[] { campus, type, role.name(), fromDate, toDate });
		/*
		 * return getJdbcTemplate().queryForObject(sql, Long.class, new Object[] { type
		 * , role , fromDate , toDate });
		 */
	}

	public List<User> checkCreatedByRoleStatsNonCourse(String tableName, Role role, String fromDate, String toDate,
			String type) {
		String typeCondition = "";
		if (type != null) {
			typeCondition = " u.`type` = ? ";
		} else {
			typeCondition = " u.`type`is ? ";
		}
		String sql = "";
		if (tableName.equals("feedback")) {
			sql = " select u.*,t.* from " + tableName
					+ " t , user_roles ur , users u where t.createdBy = ur.username and u.username = ur.username and "
					+ typeCondition + " and ur.role=? and t.active ='Y' and   t.lastModifiedDate BETWEEN  ? and ? ";
		} else if (tableName.equals("queryresponse")) {
			sql = " select u.*,t.* from " + tableName
					+ " t,`query` q , user_roles ur , users u where t.queryRespondedBy = ur.username and q.id = t.queryId and u.username = ur.username and "
					+ typeCondition + " and ur.role= ? and t.active ='Y' and   q.queryCreatedTime BETWEEN  ? and ? ";
		} else {
			sql = " select u.*,t.*,c.* from " + tableName
					+ " t , user_roles ur , users u ,course c where t.username = ur.username and u.username = ur.username and c.id = t.courseId and "
					+ typeCondition + " and ur.role=? and t.active ='Y' and   t.lastModifiedDate BETWEEN  ? and ? ";
		}
		return findAllSQL(sql, new Object[] { type, role.name(), fromDate, toDate });
	}

	public List<User> getTotalUserStats(String campus, Role role, String type) {
		String typeCondition = "";
		if (type != null && Role.ROLE_FACULTY.equals(role)) {
			logger.info("------------called inside------------");
			typeCondition = " u.`type` = ? and u.active ='Y' ";
		} else {
			logger.info("------------called null--------------" + role);
			typeCondition = " u.`type`is ?  and u.enabled = 1 ";
		}

		String campusCondition = "";
		if (campus != null) {
			campusCondition = " c.campusId = ? ";
		} else {
			campusCondition = " c.campusId is ? ";
		}

		final String sql = " select u.* from user_roles ur , users u,user_course uc, course c where u.username = ur.username and u.username=uc.username and uc.courseId = c.id and "
				+ campusCondition + "  and ur.role =? and " + typeCondition + " " + " group by u.username ";
		return findAllSQL(sql, new Object[] { campus, role.name(), type });
	}

	public List<User> findActiveUsersByRole(Role role) {
		final String sql = "select u.* " + " from users u "
				+ " inner join user_roles ur on ur.username = u.username and ur.role = ? and u.enabled = 1 ";
		return findAllSQL(sql, new Object[] { role.name() });
	}

	public List<User> searchEvauatedAssignmentFacultyStat(String type, String fromDate, String toDate) {
		String sql = "select u.* from student_assignment st ,assignment a ,users u where a.id =st.assignmentId and u.username = a.facultyId"
				+ " and st.evaluationStatus='Y' and u.`type`=? and u.enabled =1 and st.lastModifiedDate BETWEEN  ? and ? group by a.facultyId ";
		return findAllSQL(sql, new Object[] { type, fromDate, toDate });
	}

	public List<User> checkRoleStats(String tableName, Role role, String usernameField, String fromDate, String toDate,
			String type) {
		String typeCondition = "";
		String StatusCondition = "";

		if (type != null) {
			typeCondition = " u.`type` = ? ";
		} else {
			typeCondition = " u.`type`is ? ";
		}

		if (role.equals(Role.ROLE_STUDENT)) {
			if (tableName == "student_assignment") {
				StatusCondition = " and t.submissionStatus = 'Y' ";
			} else if (tableName == "student_test") {
				StatusCondition = " and t.testCompleted = 'Y' ";
			} else if (tableName == "student_feedback") {
				StatusCondition = " and t.feedbackCompleted = 'Y' ";
			}
		}

		String sql = "";
		if (tableName.equalsIgnoreCase("announcement")) {
			sql = "select t." + usernameField
					+ " as username, c.campusId, ur.role, c.dept, u.`type`, c.programId, t.courseId, c.courseName, c.acadYear, c.acadSession, count(t.id) as count from "
					+ tableName + " t left outer join course c on t.courseId = c.id, user_roles ur , users u where t."
					+ usernameField + " = u.username and u.username = ur.username and " + typeCondition
					+ " and ur.role=? and t.active ='Y' and t.lastModifiedDate BETWEEN  ? and ? group by c.programId, t.courseId, t."
					+ usernameField + " ";
		} else {
			sql = " select t." + usernameField
					+ " as username, c.campusId, ur.role, c.dept, u.`type`, c.programId, t.courseId, c.courseName, c.acadYear, c.acadSession, count(t.id) as count from "
					+ tableName + " t , user_roles ur , users u ,course c where t." + usernameField
					+ " = u.username and u.username = ur.username and c.id = t.courseId and " + typeCondition
					+ " and ur.role=? and t.active ='Y' " + StatusCondition
					+ " and   t.lastModifiedDate BETWEEN  ? and ?  group by c.programId, t.courseId, t." + usernameField
					+ " ";
		}

		return findAllSQL(sql, new Object[] { type, role.name(), fromDate, toDate });
	}

	public List<User> checkCreatedByRoleStatsForEvalution(String tableName, Role role, String usernameField,
			String fromDate, String toDate, String type) {

		String typeCondition = "";
		if (type != null) {
			typeCondition = " u.`type` = ? ";
		} else {
			typeCondition = " u.`type`is ? ";
		}

		final String sql = " select sa.createdBy as username, c.campusId, ur.role, c.dept, u.`type`, c.programId, sa.courseId,"
				+ " c.courseName, c.acadYear, c.acadSession, count(distinct sa.assignmentId) as count"
				+ " from student_assignment sa, course c, users u, user_roles ur where sa.courseId = c.id and"
				+ " sa.createdBy = u.username and u.username = ur.username and " + typeCondition
				+ " and ur.role = ? and sa.evaluationStatus = 'Y'and sa.active ='Y' and sa.lastModifiedDate BETWEEN  ? and ?  group by c.programId, sa.courseId, sa.createdBy ";
		return findAllSQL(sql, new Object[] { type, role.name(), fromDate, toDate });

	}

	public List<User> checkAdminRoleStats(String tableName, Role role, String usernameField, String fromDate,
			String toDate, String type) {
		String typeCondition = "";
		String modifiedDateCondition = "";

		if (type != null) {
			typeCondition = " u.`type` = ? ";
		} else {
			typeCondition = " u.`type`is ? ";
		}

		if (tableName == "query") {
			modifiedDateCondition = "queryCreatedTime";
		} else if (tableName == "queryresponse") {
			modifiedDateCondition = "queryRespondedTime";
		} else {
			modifiedDateCondition = "lastModifiedDate";
		}

		final String sql = "select t." + usernameField
				+ " as username, u.campusId, ur.role, u.`type`, u.programId, u.acadSession, count(t.id) as count from "
				+ tableName + " t, users u, user_roles ur  where t." + usernameField
				+ " = u.username and u.username = ur.username and " + typeCondition
				+ " and ur.role=? and t.active ='Y' and   t." + modifiedDateCondition + " BETWEEN  ? and ?  group by t."
				+ usernameField + " ";
		return findAllSQL(sql, new Object[] { type, role.name(), fromDate, toDate });
	}

	public List<User> getUserByUsername(String username) {
		String sql = "select u.username, u.firstname, u.lastname, u.enabled, u.programId, u.email, u.mobile, u.acadSession, u.campusId, u.campusName"
				+ " from " + getTableName() + " u where  u.active = 'Y' and u.username = ?";
		return findAllSQL(sql, new Object[] { username });
	}

	public User findUserProgramAcadYearByusername(String username) {
		String sql = "select distinct u.username,u.firstname,u.lastname,u.fatherName, u.motherName, u.rollNo, uc.acadYear, p.programName, u.acadSession"
				+ " from users u, user_course uc, program p where u.programId = p.id and u.username = uc.username and u.active = 'Y'"
				+ " and u.username = ? order by uc.acadYear desc limit 1";
		return findOneSQL(sql, new Object[] { username });
	}

	public int insertStudentMapping(User bean) {
		SqlParameterSource parameterSource = getParameterSource(bean);
		String sql = " insert into student_mapping (student_no,studentObjectId) values (:username,:studentObjectId)";

		int updated = getNamedParameterJdbcTemplate().update(sql, parameterSource);
		return updated;
	}

	public User findPlayerIdByUserName(String username) {
		final String sql = " select id, username, playerId " + " from user_playerid " + " where username = ? and active = 'Y'";
		return findOneSQL(sql, new Object[] { username });
	}

	public User findActivePlayerIdByUserName(String username) {
		final String sql = " select id, username, playerId " + " from user_playerid "
				+ " where username = ? and active = 'Y' ";
		return findOneSQL(sql, new Object[] { username });
	}

	public void insertUserPlayerId(String username, String playerId) {
		final String sql = " INSERT INTO user_playerid (username, playerId, active, "
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ " VALUES (?, ?, 'Y', 'CA', now(), 'CA', now()) ";

		getJdbcTemplate().update(sql, new Object[] { username, playerId });

	}

	public void updateUserPlayerId(String username, String playerId) {
		final String sql = " UPDATE user_playerid set playerId = ?, "
				+ " lastModifiedDate = now(), active = 'Y' WHERE username = ?";

		getJdbcTemplate().update(sql, new Object[] { playerId, username });

	}

	public void deleteUserPlayerId(String username) {
		final String sql = " UPDATE user_playerid set" + " active = 'N', lastModifiedDate = now() WHERE username = ?";

		getJdbcTemplate().update(sql, new Object[] { username });

	}

	public List<String> findFacultyUsernamesForAttendanceReport() {

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = "select distinct u.username from users u, user_course uc where u.username = uc.username and u.enabled = 1 "
					+ " and uc.role = 'ROLE_FACULTY' and uc.acadYear = " + currentYear + " ";
		} else {
			sql = "select distinct u.username from users u, user_course uc where u.username = uc.username and u.enabled = 1 "
					+ " and uc.role = 'ROLE_FACULTY' and (uc.acadYear = " + currentYear + " or uc.acadyear = " + year
					+ ") ";
		}

		return getJdbcTemplate().queryForList(sql, String.class, new Object[] {});
	}

	public User findUserWithCampusByUserName(final String username) {

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = " select u.*, c.campusId, pc.campusName "
					+ " from users u, user_course uc, course c left outer join program_campus pc "
					+ " on c.campusId = pc.campusId "
					+ " where u.username = uc.username and uc.courseId = c.id and uc.username = ? "
					+ " and uc.acadYear = " + currentYear + " limit 1; ";
		} else {
			sql = " select u.*, c.campusId, pc.campusName "
					+ " from users u, user_course uc, course c left outer join program_campus pc "
					+ " on c.campusId = pc.campusId "
					+ " where u.username = uc.username and uc.courseId = c.id and uc.username = ? "
					+ " and (uc.acadYear = " + currentYear + " or uc.acadyear = " + year + ") limit 1; ";
		}
		return findOneSQL(sql, new Object[] { username });
	}

	public List<String> getUserByProgramsAndRole(List<String> programIds, String userType) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programIds);
		params.put("userType", userType);

		String sql = "select distinct uc.username from user_course uc,course c,users u where uc.courseId = c.id and c.programId in (:programIds) "
				+ "and uc.role=:userType and c.active = 'Y' and uc.active = 'Y' and u.enabled = '1' and uc.username = u.username ";
		return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
	}

	public List<String> getUserByProgramAndRole(String programId, String userType) {

		String sql = "select distinct uc.username from user_course uc,course c,users u where uc.courseId = c.id and c.programId = ? "
				+ "and uc.role= ? and c.active = 'Y' and uc.active = 'Y' and u.enabled = '1' and uc.username = u.username ";

		return getJdbcTemplate().queryForList(sql, new Object[] { programId, userType }, String.class);
	}

	public List<String> getSessionMasterListTest() {

		String sql = " SELECT sapSessionText FROM session_master ";

		return getJdbcTemplate().queryForList(sql, String.class, new Object[] {});

	}

	public int changePasswordBySupportAdmin(final User user) {
		String sql = "Update " + getTableName()
				+ " set password ='d97086919b6522e13ba9b46c04902c38372102218a4b3ef2f45ac2a80e9fd240', lastModifiedDate = :lastModifiedDate, lastModifiedBy = :lastModifiedBy "
				+ " where username = :username ";
		return updateSQL(user, sql);
	}

	public List<User> findUsername(final User user) {

		String sql = " select * from users where username =? ";

		return findAllSQL(sql, new Object[] { user.getUsername() });

	}

	public List<String> findActivePlayerIdForActiveFaculties() {
		final String sql = " select up.playerId as playerId from user_playerid up, users u, user_roles ur where up.username=u.username "
				+ " and u.username=ur.username and ur.role='ROLE_FACULTY' and up.active='Y' and ur.active='Y' ";

		return getJdbcTemplate().queryForList(sql, new Object[] {}, String.class);
	}

	public void insertStudentObjIdToStudentMapping(User bean) {

		SqlParameterSource parameterSource = getParameterSource(bean);

		String sql = "INSERT INTO student_mapping (student_no, studentObjectId) VALUES (:username, :studentObjectId) "
				+ "ON DUPLICATE KEY UPDATE student_no=:username";

		int updated = getNamedParameterJdbcTemplate().update(sql, parameterSource);
		// return updated;
	}

	public User getStudentObjIdFromStudentMapping(String username) {
		// Map<String, Object> params = new HashMap<String, Object>();
		// .put("username", username);
		String sql = "select studentObjectId from student_mapping where student_no = ? ";

		return findOneSQL(sql, new Object[] { username });
	}

	public List<User> findUserByProgramIdForMasterEmail(String programId, List<String> semMailList, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("semMailList", semMailList);
		params.put("programId", programId);
		params.put("campusId", campusId);

		/*
		 * String
		 * sql="SELECT DISTINCT u.username,u.email ,u.acadSession FROM users u WHERE u.enabled=1 AND u.active = 'Y' "
		 * + " AND u.programId = :programId AND u.acadSession IN(:semMailList)";
		 * 
		 * return getNamedParameterJdbcTemplate().query(sql, params,
		 * BeanPropertyRowMapper.newInstance(User.class));
		 */

		String sql = "";

		if (campusId != null && !campusId.trim().isEmpty()) {
			sql = "SELECT DISTINCT u.username,u.email ,u.acadSession FROM users u WHERE u.enabled=1 AND u.active = 'Y' "
					+ " AND u.programId = :programId AND u.acadSession IN(:semMailList) and campusId = :campusId ";
		} else

		{
			sql = "SELECT DISTINCT u.username,u.email ,u.acadSession FROM users u WHERE u.enabled=1 AND u.active = 'Y' "
					+ " AND u.programId = :programId AND u.acadSession IN(:semMailList) ";
		}

		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(User.class));
	}

	// Coursera Chnages

	public List<String> findstudentForCoursera(Long programId, String acadSession) {

		String sql = "SELECT DISTINCT u.username FROM users u WHERE u.programId = ? AND u.acadSession= ? AND u.enabled=1 AND username NOT LIKE '00000%' ";

		return getJdbcTemplate().queryForList(sql, new Object[] { programId, acadSession }, String.class);
	}

	// 02-07-2020

	public List<User> findAllFaculty() {
		String sql = "select u.* from users u" + " inner join user_roles ur on ur.username = u.username"
				+ " where   ur.role = ? group by u.username";
		return findAllSQL(sql, new Object[] { Role.ROLE_FACULTY.name() });
	}
	
	public List<User> findNameOfFaculty(List<String> username) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		final String sql = "select CONCAT(username,'(',firstname,'_',lastname,')')  as username from users where username in (:username) and active = 'Y'";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(User.class));
	}
	public User findByUserNameLike(String username) {
		username = username.replace("_STAFF", "");
		final String sql = "select distinct firstname,lastname,email,mobile from "+getTableName()+" where username like ? limit 1";
		return findOneSQL(sql, new Object[] { username +"%" });
	}
	
	public List<User> getFacultyByCourse(Long courseId) {
		String sql = "select * from users u, user_course uc where u.username = uc.username and uc.active = 'Y' and u.enabled=1 and uc.courseId = ? and uc.role = 'ROLE_FACULTY'";
		return findAllSQL(sql, new Object[] { courseId });
	}
	
	public void changeEmailMobileByApp(String email,String mobile,String username) {
		executeUpdateSql("Update users set email = ?, mobile = ? where username= ?",
				new Object[] { email, mobile, username });
	}
}
