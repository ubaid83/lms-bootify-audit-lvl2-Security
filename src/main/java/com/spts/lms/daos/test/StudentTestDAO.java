package com.spts.lms.daos.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.PendingTest;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;

@Repository("studentTestDAO")
public class StudentTestDAO extends BaseDAO<StudentTest> {

	@Override
	protected String getTableName() {
		return "student_test";
	}

	/*
	 * @Override protected String getInsertSql() { return
	 * "INSERT INTO student_test(username,testId,attempt,testStartTime,testEndTime,testCompleted,score,"
	 * + "createdBy,createdDate,lastModifiedBy,lastModifiedDate,courseId)" +
	 * " VALUES(:username,:testId,0,:testStartTime,:testEndTime,:testCompleted,"
	 * +
	 * ":score,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:courseId) "
	 * + " ON DUPLICATE KEY UPDATE " +
	 * " testStartTime = COALESCE(testStartTime, :testStartTime), testEndTime = COALESCE(testEndTime, :testEndTime),"
	 * +
	 * "testCompleted = COALESCE(testCompleted, :testCompleted), score = COALESCE(score, :score), courseId=:courseId,attempt = COALESCE(attempt,0) + 1, lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate"
	 * ; }
	 */

	@Override
	protected String getInsertSql() {
		return "INSERT INTO student_test(username,testId,attempt,testStartTime,testEndTime,testCompleted,score,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,courseId)"
				+ " VALUES(:username,:testId,0,:testStartTime,:testEndTime,:testCompleted,"
				+ ":score,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:courseId) "
				+ " ON DUPLICATE KEY UPDATE "
				+ "score=:score, testStartTime = :testStartTime, testEndTime = :testEndTime,"
				+ "testCompleted = :testCompleted ,  courseId=:courseId, attempt=:attempt, lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate";
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public Page<StudentTest> getStudentForTest(Long testId, String facultyId,
			Long courseId, String acadMonth, String acadYear, int pageNo,
			int pageSize) {
		final String sql = "select uc.username, u.firstname, u.lastname, st.testId, st.id "
				+ " from user_course uc inner join users u on uc.username = u.username "
				+ " left outer join student_test st on st.courseId = uc.courseId  and st.username = u.username   and st.testId = ? "
				+ " left outer join test t on t.id = st.testId and t.acadMonth = uc.acadMonth and t.acadYear = uc.acadYear and t.facultyId = ? "
				+ "  where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by st.id asc ";

		final String countsql = "select count(*) "
				+ " from user_course uc inner join users u on uc.username = u.username "
				+ " left outer join student_test st on st.courseId = uc.courseId  and st.username = u.username   and st.testId = ? "
				+ " left outer join test t on t.id = st.testId and t.acadMonth = uc.acadMonth and t.acadYear = uc.acadYear and t.facultyId = ? "
				+ "  where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by st.id asc ";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { testId, facultyId, Role.ROLE_STUDENT.name(),
						courseId, acadMonth, acadYear }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentTest.class));
	}

	public Page<StudentTest> getStudentForTest(Long testId, String facultyId,
			Long courseId, int pageNo, int pageSize) {
		final String sql = "select uc.username, u.firstname, u.lastname, st.testId, st.id "
				+ " from user_course uc inner join users u on uc.username = u.username "
				+ " left outer join student_test st on st.courseId = uc.courseId  and st.username = u.username   and st.testId = ? "
				+ " left outer join test t on t.id = st.testId and t.acadMonth = uc.acadMonth and t.acadYear = uc.acadYear and t.facultyId = ? "
				+ "  where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ?  order by st.id asc ";

		final String countsql = "select count(*) "
				+ " from user_course uc inner join users u on uc.username = u.username "
				+ " left outer join student_test st on st.courseId = uc.courseId  and st.username = u.username   and st.testId = ? "
				+ " left outer join test t on t.id = st.testId and t.acadMonth = uc.acadMonth and t.acadYear = uc.acadYear and t.facultyId = ? "
				+ "  where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ?  order by st.id asc ";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { testId, facultyId, Role.ROLE_STUDENT.name(),
						courseId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentTest.class));
	}

	public StudentTest findBytestIDAndUsernameAndTestCompleted(Long testId,
			String username) {
		final String sql = "Select * from " + getTableName()
				+ " where testId = ? and username = ? and testCompleted ='Y' ";
		return findOneSQL(sql, new Object[] { testId, username });
	}

	public List<StudentTest> getStudentUsername(Long testId, Long courseId) {

		String sql = "select st.username from student_test st "
				+ "where st.testId = ? and st.courseId = ? ";

		return findAllSQL(sql, new Object[] { testId, courseId });

	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	public StudentTest findStudentTestByStudentTestID(Long studentTestId) {
		final String sql = "Select * from " + getTableName() + " where id = ? ";
		return findOneSQL(sql, new Object[] { studentTestId });
	}

	public List<StudentTest> findByUsername(String username) {
		String sql = " select * from student_test where username = ? and active = 'Y' ";
		return findAllSQL(sql, new Object[] { username });
	}

	public StudentTest findBytestIDAndUsername(Long testId, String username) {
		final String sql = "Select * from " + getTableName()
				+ " where testId = ? and username = ?";
		return findOneSQL(sql, new Object[] { testId, username });
	}

	public int getNoOfStudentsAllocated(Long id) {
		String sql = " select count(*) from student_test st,user_course uc,test t where "
				+ " st.username=uc.username and st.active = 'Y' and uc.role = 'ROLE_STUDENT' "
				+ "  and st.courseId=uc.courseId and st.testId=t.id and t.id = ? ";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	public List<StudentTest> getStudentForTestList(Long testId,
			String facultyId, Long courseId) {
		final String sql = "select uc.username, u.firstname, u.lastname, st.testId, st.id "
				+ " from user_course uc inner join users u on uc.username = u.username "
				+ " left outer join student_test st on st.courseId = uc.courseId  and st.username = u.username   and st.testId = ? "
				+ " left outer join test t on t.id = st.testId and t.acadMonth = uc.acadMonth and t.acadYear = uc.acadYear and t.facultyId = ? "
				+ "  where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ?  order by st.lastModifiedDate desc ";

		return findAllSQL(sql, new Object[] { testId, facultyId,
				Role.ROLE_STUDENT.name(), courseId });
	}

	public List<StudentTest> getStudentForTestWithScores(Long testId,
			Long courseId) {

		final String sql = "select uc.username, u.firstname, u.lastname, st.testId, st.id,st.score,t.passScore "
				+ " from user_course uc "
				+ "	inner join users u on uc.username = u.username "
				+ "	inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "	left outer join student_test st on st.username = u.username and st.testId = t.id "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and uc.role = ? and uc.courseId = ? and t.showResultsToStudents = 'Y'  order by st.lastModifiedDate desc";

		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId });

	}

	public List<StudentTest> getTestsByStudent(Long courseId, String username) {
		String sql = "select st.score, t.maxScore, t.testName,t.id from test t "
				+ " inner join student_test st on st.testId = t.id "
				+ " where st.courseId = ? and st.username = ? and t.showResultsToStudents = 'Y' order by t.id";
		return findAllSQL(sql, new Object[] { courseId, username });

	}

	public int calculateMarks(StudentTest studentTest) {
		final String sql = "Update student_question_response sqr inner join test_question tq on tq.id = sqr.questionId and tq.correctOption = sqr.answer "
				+ " set sqr.marks = tq.marks"
				+ " where sqr.studentTestId = :id and sqr.username = :username and tq.active = 'Y' ";
		return updateSQL(studentTest, sql);
	}

	public int chkStartandEndDateOfTest(String username, Long id) {
		final String sql = " select count(*) from test t,student_test st where startDate <= sysdate() and endDate >= sysdate() and st.testId=t.id and st.username= ? "
				+ " and t.id= ? and st.testCompleted='Y'";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { username, id });
	}

	public List<StudentTest> searchAllTest(Long courseId, String status) {

		String sql = "select st.*, t.* from test t"
				+ " inner join student_test st on st.testId = t.id "
				+ " where st.courseId = ? and st.testCompleted = ? and t.active = 'Y'";
		return findAllSQL(sql, new Object[] { courseId, status });
	}

	public int chkStartandEndDateOfTests(String username, Long id) {
		final String sql = " select count(*) from test t,student_test st where startDate <= sysdate() and endDate >= sysdate() and st.testId=t.id and st.username= ? "
				+ " and t.id= ? ";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { username, id });
	}

	public List<StudentTest> findOneTest(Long testId) {
		String sql = " select st.*,t.* from test t "
				+ " inner join student_test st on st.testId = t.id "
				+ " inner join user_course uc on uc.username=st.username and t.courseId=uc.courseId and uc.role='ROLE_STUDENT' "
				+ "where st.testId = ?";
		return findAllSQL(sql, new Object[] { testId });
	}

	public List<StudentTest> findAllTest(String acadMonth, String acadYear,
			String facultyId) {
		String sql = " select t.*, st.* from test t "
				+ " inner join student_test st on st.testId = t.id "
				+ " where t.acadMonth = ? and t.acadYear = ?  and st.username = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, facultyId });

	}

	/*
	 * public int calculateMarks(StudentTest studentTest) { final String sql =
	 * "Update student_question_response sqr inner join test_question tq on tq.id = sqr.questionId  "
	 * +
	 * " set sqr.marks = CASE WHEN tq.correctOption = sqr.answer THEN tq.marks ELSE 0"
	 * +
	 * " where sqr.studentTestId = :id and sqr.username = :username and tq.active = 'Y' "
	 * ; return updateSQL(studentTest, sql); }
	 */

	public int calculateScore(StudentTest studentTest) {
		final String sql = "Update "
				+ getTableName()
				+ " st "
				+ " set st.score = (select sum(marks) from student_question_response sqr where sqr.studentTestId = :id)"
				+ " where st.id = :id and st.username = :username ";
		return updateSQL(studentTest, sql);
	}

	public List<StudentTest> getStudenForTest(Long testId, Long courseId,
			String acadMonth, String acadYear) {
		final String sql = "select uc.username, p.programName, u.firstname, u.lastname, st.testId, st.id "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "		left outer join student_test st on st.username = u.username and st.testId = t.id "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by st.id asc ";
		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId, acadMonth, acadYear });
	}

	public List<StudentTest> getStudentForTest(Long testId, Long courseId,
			String acadMonth, String acadYear) {

		final String sql = "select uc.username, u.firstname, u.lastname, st.testId, st.id "
				+ " from user_course uc "
				+ "	inner join users u on uc.username = u.username "
				+ "	inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "	left outer join student_test st on st.username = u.username and st.testId = t.id "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by st.id asc";

		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId, acadMonth, acadYear });

	}

	public List<StudentTest> getStudentForTest(Long testId, Long courseId) {

		final String sql = "select uc.username, u.firstname,u.rollNo, u.lastname, st.testId, st.id ,u.campusId, u.campusName "
				+ " from user_course uc "
				+ "	inner join users u on uc.username = u.username "
				+ "	inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "	left outer join student_test st on st.username = u.username and st.testId = t.id and st.active = 'Y' "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and uc.role = ? and uc.courseId = ?  "
				+ " order by st.lastModifiedDate desc,u.rollNo asc";

		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId });

	}

	public List<StudentTest> getStudentForTestAndCampusId(Long testId,
			Long courseId, Long campusId) {

		final String sql = "select uc.username, u.firstname, u.lastname, st.testId, st.id , u.campusName "
				+ " from user_course uc "
				+ "   inner join users u on uc.username = u.username "
				+ "   inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "   left outer join student_test st on st.username = u.username and st.testId = t.id "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and uc.role = ? and uc.courseId = ? and u.campusId= ? order by st.lastModifiedDate desc";

		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId, campusId });

	}

	public List<PendingTest> getPendingTest(String username, String acadMonth,
			String acadYear) {
		final String sql = "select t.testName,t.id as testId,t.endDate from test t , student_test st where  t.id =st.testId  and st.username =?   and ( st.testCompleted <> 'Y' or st.testCompleted is null)  and t.acadYear = ? and t.acadMonth=? ";
		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(PendingTest.class),
				new Object[] { username, acadYear, acadMonth });
	}

	public List<PendingTest> getPendingTest(String username) {
		final String sql = "select t.testName,t.id as testId,t.startDate,t.endDate,t.testType,t.isPasswordForTest,t.courseId from test t inner join course c on c.id=t.courseId , student_test st where  t.id =st.testId  and st.username =?   and ( st.testCompleted <> 'Y' or st.testCompleted is null or t.allowAfterEndDate='Y')   and t.active='Y' and st.active='Y' and c.active = 'Y' and t.endDate >= sysdate()";
		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(PendingTest.class),
				new Object[] { username });
	}

	public List<PendingTest> getPendingTestByCourse(String username,
			String acadMonth, String acadYear, String courseId) {
		final String sql = "select t.testName,t.id as testId,t.endDate from test t , student_test st where  t.id =st.testId "
				+ " and st.username =?   and ( st.testCompleted <> 'Y' or st.testCompleted is null)  and t.acadYear = ? and t.acadMonth=? and t.courseId = ?";
		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(PendingTest.class),
				new Object[] { username, acadYear, acadMonth, courseId });
	}

	public List<PendingTest> getPendingTestByCourse(String username,
			String courseId) {
		final String sql = "select t.testName,t.id as testId,t.endDate,t.startDate,t.testType from test t , student_test st where  t.id =st.testId "
				+ " and st.username =?   and ( st.testCompleted <> 'Y' or st.testCompleted is null)  and t.courseId = ? and t.active='Y' and st.active = 'Y' and t.endDate >= sysdate()";
		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(PendingTest.class),
				new Object[] { username, courseId });
	}

	public List<PendingTest> getPendingTestCountDashboard(String username,
			String acadMonth, String acadYear) {

		String sql;

		sql = "select t.courseId as courseId,count(*) as count from test t , student_test st "
				+ " where  t.id =st.testId  and st.username = ? and "
				+ " ( st.testCompleted <> 'Y' or st.testCompleted is null)  and "
				+ " t.acadYear = ? and t.acadMonth= ? group by t.courseId ";

		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(PendingTest.class),
				new Object[] { username, acadYear, acadMonth });
	}

	public List<PendingTest> getPendingTestCountDashboard(String username) {

		String sql;

		sql = "select t.courseId as courseId,count(*) as count from test t , student_test st "
				+ " where  t.id =st.testId  and st.username = ? and st.active = 'Y' and "
				+ " ( st.testCompleted <> 'Y' or st.testCompleted is null)  "
				+ "  group by t.courseId ";

		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(PendingTest.class),
				new Object[] { username });
	}

	public List<StudentTest> getStudentsForTest(Long testId, Long courseId,
			String acadMonth, String acadYear) {
		final String sql = "select uc.username, p.programName, u.firstname, u.lastname, st.testId, st.id "
				+ " from user_course uc"
				+ "                          inner join users u on uc.username = u.username"
				+ "                          inner join program p on u.programId = p.id"
				+ "                          inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "                          left outer join student_test st on st.username = u.username and st.testId = t.id "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and "
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by st.id asc ";
		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId, acadMonth, acadYear });
	}

	public void saveStartDate(String startDate, Long pk, String userName) {
		StudentTest test = new StudentTest();
		test.setStartDate(startDate);
		test.setId(pk);
		test.setCreatedBy(userName);
		test.setLastModifiedBy(userName);

		String sql = "update student_test set " + " startDate = :startDate, "
				+ " evaluatedBy = :evaluatedBy, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(test, sql);
	}

	public void saveEndDate(String endDate, Long pk, String userName) {
		StudentTest test = new StudentTest();
		test.setEndDate(endDate);
		test.setId(pk);
		test.setCreatedBy(userName);
		test.setLastModifiedBy(userName);

		String sql = "update test set " + " endDate = :endDate, "
				+ " evaluatedBy = :evaluatedBy, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(test, sql);
	}

	public void savePassScore(Integer passScore, Long pk, String userName) {
		StudentTest test = new StudentTest();
		test.setPassScore(passScore);
		test.setId(pk);
		test.setCreatedBy(userName);
		test.setLastModifiedBy(userName);

		String sql = "update student_test set " + " passScore = :passScore, "
				+ " evaluatedBy = :evaluatedBy, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(test, sql);

	}

	public Page<StudentTest> getStudentsBasedOnTest(StudentTest test,
			int pageNo, int pageSize, Long testId) {
		String sql = "select st.* , t.* from student_test st"

				+ "                          inner join course c on c.id=st.courseId"
				+ "                          inner join test t on t.id=st.testId"
				+ " where" + " t.id = ? order by st.id asc  ";

		String countSql = "Select count(*) from student_test st"
				+ " inner join course c on c.id=st.courseId"
				+ " inner join test t on t.id=st.testId " + " where"
				+ " t.id = ? order by st.id asc  ";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(testId);

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public List<StudentTest> getStudentToEvaluate(Long testId, Long courseId) {

		final String sql = "select uc.username, u.firstname,u.rollNo, u.lastname, st.testId, st.id ,st.testCompleted "
				+ "   from user_course uc "
				+ "	inner join users u on uc.username = u.username"
				+ "	inner join test t on t.courseId = uc.courseId and t.id = ?"
				+ "	inner join student_test st on st.username = u.username and st.testId = t.id "
				+ "   where uc.role = ? and uc.courseId = ?  order by u.rollNo asc";

		return findAllSQL(sql, new Object[] { testId, Role.ROLE_STUDENT.name(),
				courseId });

	}

	public void saveMaxScore(Integer maxScore, Long pk, String userName) {
		StudentTest test = new StudentTest();
		test.setMaxScore(maxScore);
		test.setId(pk);
		test.setCreatedBy(userName);
		test.setLastModifiedBy(userName);

		String sql = "update student_test set " + " maxScore = :maxScore, "
				+ " evaluatedBy = :evaluatedBy, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(test, sql);
	}

	public List<StudentTest> findTestByStudent(String username) {

		String sql = "select st.* from student_test st where st.username  = ?";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<StudentTest> searchAllTestByCourseId(Long courseId) {

		String sql = "select st.*, t.* from test t"
				+ " inner join student_test st on st.testId = t.id "
				+ " where st.courseId = ? and t.active = 'Y'";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<StudentTest> searchAllTestByStatus(String status) {

		String sql = "select st.*, t.* from test t"
				+ " inner join student_test st on st.testId = t.id "
				+ " where st.testCompleted = ? and t.active = 'Y'";
		return findAllSQL(sql, new Object[] { status });
	}

	public List<StudentTest> findTestByStudent(String username, String courseId) {

		String sql = "select st.* from student_test st where st.username  = ? and st.courseId = ? ";

		return findAllSQL(sql, new Object[] { username, courseId });

	}

	public List<StudentTest> getNoOfStudentSubmissionStats(String campusId,
			String fromDate, String toDate) {

		String typeCondition = "";
		if (campusId != null) {
			typeCondition = " c.campusId = ? ";
		} else {
			typeCondition = " c.campusId is ? ";
		}

		String sql = "select st.*, sqr.* from student_test st ,student_question_response sqr, course c, users u where st.id =sqr.studentTestId and st.courseId = c.id and st.username = u.username and u.active = 'Y' and "
				+ typeCondition
				+ " and sqr.lastModifiedDate"
				+ " BETWEEN  ? and ? group by sqr.studentTestId";
		return findAllSQL(sql, new Object[] { campusId, fromDate, toDate });

	}

	public void removeStudent_Test(String username, String id) {

		String sql = " UPDATE student_test SET active = 'N' where username = ? and testId = ? ";

		executeUpdateSql(sql, new Object[] { username, id });

	}

	public List<StudentTest> findStudentTest(Long testId) {

		String sql = " select st.*,sum(marks) as updatedScore from "
				+ getTableName()
				+ " st,student_question_response sqr "
				+ " where sqr.studentTestId=st.id and st.testId= ?  and st.active = 'Y' group by st.username,st.testId ";

		return findAllSQL(sql, new Object[] { testId });
	}

	public void updateStudentTestDuration(String studentTestId,
			String durationInMinute) {

		String sql = " update student_test set durationCompleted = ? where id = ? ";

		getJdbcTemplate().update(sql,
				new Object[] { durationInMinute, studentTestId });
	}

	public List<StudentTest> findAllTestOfCurrentDateByStudent(String username,
			String date) {

		String sql = " select t.testName,c.courseName,st.* from student_test st,test t,course c where c.id=t.courseId "
				+ " and t.startDate like ? and st.testId=t.id and st.username = ? and (st.testCompleted IS NULL or st.testCompleted = 'N')"
				+ " and st.active = 'Y' ";

		return findAllSQL(sql, new Object[] { date + "%", username });
	}

	public StudentTest getTestSummaryById(String username) {
		final String sql = " select Count(case when (st.testCompleted ='Y' and st.score >= t.passScore and t.showResultsToStudents = 'Y')  then 1 end) as passed, "
				+ " Count(case when (st.testCompleted ='Y' and st.score < t.passScore and t.showResultsToStudents = 'Y')  then 1 end) as failed, "
				+ " Count(case when (st.testCompleted ='N' or st.testCompleted is null)  then 1 end) as pending "
				+ " from student_test st, test t, course c, users u where st.testId = t.id and st.username = u.username and t.courseId = c.id and "
				+ " c.acadSession = u.acadSession and st.username = ? and st.active = 'Y' and t.active = 'Y'"
				+ " group by st.username ";
		return findOneSQL(sql, new Object[] { username });
	}

	public StudentTest getTestSummaryByIdAndSem(String username,
			String acadSession) {
		final String sql = " select Count(case when (st.testCompleted ='Y' and st.score >= t.passScore and t.showResultsToStudents = 'Y')  then 1 end) as passed, "
				+ " Count(case when (st.testCompleted ='Y' and st.score < t.passScore and t.showResultsToStudents = 'Y')  then 1 end) as failed, "
				+ " Count(case when (st.testCompleted ='N' or st.testCompleted is null)  then 1 end) as pending "
				+ " from student_test st, test t, course c where st.testId = t.id and t.courseId = c.id "
				+ " and st.username = ? and c.acadSession = ? and st.active = 'Y' and t.active = 'Y'"
				+ " group by st.username ";
		return findOneSQL(sql, new Object[] { username, acadSession });
	}

	public List<StudentTest> getTestsForGradeCenter(String username,
			Long courseId) {
		String sql = " select distinct concat(u.firstname,' ', u.lastname) as name, a.testName,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC' "
				+ " when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as stringscore, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test a "
				+ "  on a.courseId = uc.courseId   inner join course c on a.courseId = c.id "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
				+ " where uc.courseId = ?  and asub.username = ? and c.active='Y' and a.active='Y' ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId, username });
	}
	
	public List<StudentTest> getTestsForGradeCenterCreatedByAdminForTest(String username,
			String moduleId) {
		String sql = " select distinct concat(u.firstname,' ', u.lastname) as name, a.testName,( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null  then 'NC' "
				+ " when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as stringscore, "
				+ " ( case when  asub.testCompleted  <> 'Y' or asub.testCompleted is null then 'NC' when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else "
				+ " (case when asub.score>= a.passScore then 'P' else 'F' end) end) as status "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = ? "
				+ " inner join test a "
				+ "  inner join course c on a.moduleId = c.moduleId "
				+ " left outer join student_test asub on asub.testId = a.id and asub.username = u.username "
				+ " where c.moduleId=? and asub.username = ? and c.active='Y' and a.active='Y' ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				moduleId, username });
	}

	public List<StudentTest> getTestsForParentReport(String username,
			Long courseId) {
		String sql = " select t.testName,t.id as testId,t.endDate,t.startDate,t.testType, st.score as stringscore, t.maxScore, st.score-t.maxScore as unscored from test t , student_test st where  t.id =st.testId "
				+ " and st.username = ? and st.testCompleted  = 'Y'  and t.courseId = ? and t.active='Y' and st.active = 'Y' and t.showResultsToStudents = 'Y' ";

		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public StudentTest getTestSummaryByIdAndSemAndCourse(String username,
			String acadSession, String courseId) {
		final String sql = " select Count(case when (st.testCompleted ='Y' and st.score >= t.passScore and t.showResultsToStudents = 'Y')  then 1 end) as passed, "
				+ " Count(case when (st.testCompleted ='Y' and st.score < t.passScore and t.showResultsToStudents = 'Y')  then 1 end) as failed, "
				+ " Count(case when (st.testCompleted ='N' or st.testCompleted is null)  then 1 end) as pending "
				+ " from student_test st, test t, course c where st.testId = t.id and t.courseId = c.id "
				+ " and st.username = ? and c.acadSession = ? and c.id = ? and st.active = 'Y' and t.active = 'Y' "
				+ " group by st.username  ";
		return findOneSQL(sql, new Object[] { username, acadSession, courseId });
	}

	public StudentTest callCompleteStudentTest(String studentTestId,
			String testType) {

		String sql = " call complete_student_test(?, ?) ";
		return findOneSQL(sql, new Object[] { studentTestId, testType });

	}

	// query added on 11-10-2019

	public void updateStudentTestResponseFilePath(String studentQRespFilePath,
			Long studentTestId) {

		String sql = " update student_test set studentQRespFilePath = ? where id = ? ";

		getJdbcTemplate().update(sql,
				new Object[] { studentQRespFilePath, studentTestId });
	}
	
	public List<StudentTest> getIncompleteStudentTest(String testId){
		
		String sql =" select * from "+getTableName()+" where testId = ?  and (testCompleted <> 'Y' or testCompleted is null)";
		
		return findAllSQL(sql, new Object[]{testId});
	}
	
	public StudentTest allocateTestQuestionsForAllStudent(long testId, String filePath){
		
		String sql = " call allocateTestQuestionsForAllStudents(?, ?) ";
		return findOneSQL(sql, new Object[] { testId, filePath });
		
	}
	
	public List<StudentTest> findByTestId(Long testId) {
		final String sql = "Select * from " + getTableName()
				+ " where testId = ?";
		return findAllSQL(sql, new Object[] { testId });
	}
	
	public void updateStudentTestResponseFilePathByTestId(Long testId) {

		String sql = " update student_test set studentQRespFilePath = null where testId = ? ";

		getJdbcTemplate().update(sql,
				new Object[] { testId });
	}
	
	public void deleteStudentTestResponseByTestId(Long testId) {

		String sql = " delete from  student_question_response "
				+ " where studentTestId In (select id from student_test where testId = ?) ";

		getJdbcTemplate().update(sql,
				new Object[] { testId });
	}
	
	public void removeStudentQRespFile(Long testId){
        String sql ="update student_test SET studentQRespFilePath = NULL,testStartTime = NULL,testEndTime = NULL where testId = ?";
        executeUpdateSql(sql, new Object[] { testId });
        
        /*String sql1 ="delete from student_question_response where studentTestId = ?";
        executeUpdateSql(sql1, new Object[] { testId });*/
  }
  
  public void removeStudentQueResp(Long testId){
        String sql ="select distinct st.id from student_test st,student_question_response sqr where sqr.studentTestId=st.id and  testId=?";
        List<String> stIds= getJdbcTemplate().queryForList(sql, String.class,
                    new Object[] { testId });
        
        for(String sid:stIds){
              String sql1 ="delete from student_question_response where studentTestId = ?";
              executeUpdateSql(sql1, new Object[] { sid });
        }
  }
  
  public List<StudentTest> getStudentTestNotSubmitted(String testId){
		
		String sql =" select * from "+getTableName()+" where testId = ? and testStartTime is not null  and (testCompleted <> 'Y' or testCompleted is null)";
		
		return findAllSQL(sql, new Object[]{testId});
	}

	/*
	 * public List<StudentTest> studentTestDetails(Long testId) { String sql =
	 * "SELECT st.id, st.username,st.testStartTime,st.testEndTime,st.testCompleted,st.score,st.durationCompleted,st.lastModifiedBy "
	 * + " FROM student_test st WHERE st.testId=? and st.active='Y'";
	 * 
	 * return findAllSQL(sql, new Object[] {testId }); }
	 */

  
  public List<StudentTest> studentTestDetails(Long testId) {
		String sql = "SELECT st.id,t.testType, st.username,st.testStartTime,st.testEndTime,st.testCompleted,st.score,st.durationCompleted,st.lastModifiedBy " + 
				      " FROM student_test st,test t WHERE st.testId=? and st.active='Y' and  st.testId= t.id";

		return findAllSQL(sql, new Object[] {testId });
	}
  
  
  public List<StudentTest> getStudentForTestByAdmin(Long testId, List<String> courseIds) {

		Map<String,Object> mapper = new HashMap<>();
		
		mapper.put("testId", testId);
		mapper.put("courseIds", courseIds);
		mapper.put("role", Role.ROLE_STUDENT.name());
		final String sql = "select distinct uc.username, u.firstname,u.rollNo, u.lastname, st.testId, st.id ,u.campusId, u.campusName "
				+ " from user_course uc "
				+ "	inner join users u on uc.username = u.username "
				+ "	inner join test t on t.id = :testId"
				+ "	left outer join student_test st on st.username = u.username and st.testId = t.id and st.active = 'Y' "
				+ " where uc.active = 'Y' and u.active = 'Y' and u.enabled = 1 and uc.role = :role and uc.courseId in(:courseIds)  "
				+ " order by st.lastModifiedDate desc,u.rollNo asc";

		return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(StudentTest.class));

	}
  
  public List<StudentTest> findStudentTestForDelete(Long testId) {
		String sql = " select st.* from test t "
				+ " inner join student_test st on st.testId = t.id "
				+ " inner join user_course uc on uc.username=st.username and t.courseId=uc.courseId and uc.role='ROLE_STUDENT' "
				+ "where st.testId = ?";
		return findAllSQL(sql, new Object[] { testId });
	}
  
  public List<StudentTest> getStudetTestMarksByTestList(List<String> testId,List<String> usernames) {
	  Map<String,Object> mapper = new HashMap<>();
	  mapper.put("testId", testId);
	  mapper.put("usernames", usernames);
	  
	  String sql =" select st.score,st.username from "+getTableName()+" st where st.testId in(:testId) and st.username in(:usernames)"
	  		+ " and st.active ='Y' and st.testCompleted='Y' and st.score is not null ";
	  
	  return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(StudentTest.class));
  }
  
 public List<StudentTest> getStudetTestMarksByTestList(List<String> testId) {
	  Map<String,Object> mapper = new HashMap<>();
	  mapper.put("testId", testId);
	  String sql =" select st.score,st.username from "+getTableName()+" st where st.testId in(:testId) "
	  		+ " and st.active ='Y' and st.testCompleted='Y' and st.score is not null ";
	  
	  return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(StudentTest.class));
  }

 	//Hiren 02-02-2021
	public void removeFaultyDemoTestTime(Long testId){
	     String sql ="update student_test SET testStartTime = NULL,testEndTime = NULL where testId = ?";
	     executeUpdateSql(sql, new Object[] { testId });
	}	
}
