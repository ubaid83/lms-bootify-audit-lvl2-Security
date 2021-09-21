package com.spts.lms.daos.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.web.utils.Utils;

@Repository("testDAO")
public class TestDAO extends BaseDAO<Test> {

	@Override
	protected String getTableName() {
		return "test";
	}

	public List<Test> findAllValidTest() {
		final String sql = "select distinct (t.id),t.testName from  " + getTableName() + " "
				+ " t inner join test_question tq on t.id = tq.testId where t.active = 'Y' ";
		return findAllSQL(sql, new Object[] {});
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO test(acadYear,acadMonth,courseId,testDescription,startDate,endDate,"
				+ "showResultsToStudents,active,facultyId,maxAttempt,allowAfterEndDate,sendEmailAlert,sendSmsAlert,sendEmailAlertToParents,sendSmsAlertToParents,"
				+ "testName,maxScore,duration,passScore,testType,randomQuestion,createdBy,createdDate,"
				+ " lastModifiedBy,lastModifiedDate,maxQuestnToShow,isPasswordForTest,passwordForTest,autoAllocateToStudents,maxDesQueToShow,maxMcqQueToShow,maxRngQueToShow,maxImgQueToShow, "
				+ " sameMarksQue, marksPerQue, specifyMaxQues,moduleId,isCreatedByAdmin,campusId)"
				+ " VALUES(:acadYear,:acadMonth,"
				+ ":courseId,:testDescription,:startDate,:endDate,:showResultsToStudents,'Y',:facultyId,:maxAttempt,:allowAfterEndDate,"
				+ ":sendEmailAlert,:sendSmsAlert,:sendEmailAlertToParents,:sendSmsAlertToParents,:testName,:maxScore,:duration,:passScore,"
				+ ":testType,:randomQuestion,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:maxQuestnToShow,:isPasswordForTest,"
				+ ":passwordForTest,:autoAllocateToStudents,:maxDesQueToShow,:maxMcqQueToShow,:maxRngQueToShow,:maxImgQueToShow, "
				+ " :sameMarksQue, :marksPerQue, :specifyMaxQues,:moduleId,:isCreatedByAdmin,:campusId)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = " Update test set acadYear=:acadYear,acadMonth=:acadMonth,courseId=:courseId,testDescription=:testDescription,startDate=:startDate,endDate=:endDate,"
				+ " showResultsToStudents=:showResultsToStudents,active=COALESCE(:active, active),facultyId=:facultyId,maxAttempt=:maxAttempt,"
				+ " allowAfterEndDate=:allowAfterEndDate,sendEmailAlert=:sendEmailAlert,sendSmsAlert=:sendSmsAlert,"
				+ " sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ " sendSmsAlertToParents = :sendSmsAlertToParents ,autoAllocateToStudents=:autoAllocateToStudents,"
				+ " maxDesQueToShow=:maxDesQueToShow,maxMcqQueToShow=:maxMcqQueToShow,maxRngQueToShow=:maxRngQueToShow,maxImgQueToShow=:maxImgQueToShow, "
				+ " sameMarksQue=:sameMarksQue, marksPerQue=:marksPerQue, specifyMaxQues=:specifyMaxQues, "
				+ " isPasswordForTest=:isPasswordForTest,passwordForTest=:passwordForTest, "
				+ " testName=:testName,maxScore=:maxScore,duration=:duration,passScore=:passScore,"
				+ " testType=:testType,randomQuestion =:randomQuestion,createdBy=:createdBy,"
				+ " createdDate=:createdDate,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate,maxQuestnToShow=:maxQuestnToShow where id=:id "
				+ " and not exists (select id from student_test st where st.testId = test.id and st.testStartTime is not null)";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	@Override
	public Test findOne(Long id) {
		final String sql = "SELECT t.*, c.courseName FROM " + getTableName()
				+ " t inner join course c on c.id = t.courseId " + " WHERE t.ID = ?";
		return findOneSQL(sql, new Object[] { id });
	}

	public int chkStartDateForUpdate(Long testId) {
		final String sql = "select count(*) from " + getTableName() + " where startDate>=sysdate() and id = ?";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { testId });
	}

	public List<Test> findtestByFacultyAndCourseAndType(String username, String courseId, String type) {
		String sql = " select * from " + getTableName()
				+ " where facultyId = ? and courseId=? and testType=? and startDate > sysdate()";
		return findAllSQL(sql, new Object[] { username, courseId, type });
	}

	public void updateTestDetailsBasedOnAttribute(Long testId, String value, String attr, String userName) {
		String sql = "";
		switch (attr) {
		case "passScore":
			sql = " update " + getTableName()
					+ " set passScore = ?, createdBy = ?, lastModifiedBy = ? , createdDate = sysdate() , lastModifiedDate = sysdate() where id= ? ";
			break;
		case "maxScore":
			sql = " update " + getTableName()
					+ " set maxScore = ?, createdBy = ?, lastModifiedBy = ? , createdDate = sysdate() , lastModifiedDate = sysdate() where id= ? ";
			break;

		case "duration":
			sql = " update " + getTableName()
					+ " set duration = ?, createdBy = ?, lastModifiedBy = ? , createdDate = sysdate() , lastModifiedDate = sysdate() where id= ? ";
			break;

		case "attempts":
			sql = " update " + getTableName()
					+ " set maxAttempts = ?, createdBy = ?, lastModifiedBy = ? , createdDate = sysdate() , lastModifiedDate = sysdate() where id= ? ";
			break;

		}
		executeUpdateSql(sql, new Object[] { value, userName, userName, testId });
	}

	@Override
	protected Page<Test> searchByExactMatch(Test bean, int pageNo, int pageSize, boolean includeActive) {
		ArrayList<Object> parameters = buildParameters(bean, includeActive);
		// The first object will always be the criteria String
		StringBuilder criteria = (StringBuilder) parameters.remove(0);

		final String sql = " FROM " + getTableName() + " inner join course c on c.id = " + getTableName()
				+ ".courseId ";

		return paginationHelper.fetchPage(getJdbcTemplate(),
				"SELECT COUNT(" + getTableName() + ".id) " + sql + criteria.toString(),
				"SELECT " + getTableName() + ".*,c.courseName " + sql + criteria.toString(), parameters.toArray(),
				pageNo, pageSize, BeanPropertyRowMapper.newInstance(Test.class));
	}

	public List<Test> findAllActiveWithCourse() {
		final String sql = "SELECT t.*, c.courseName FROM " + getTableName()
				+ " t INNER JOIN course c ON c.id = t.courseId where active = 'Y' ";
		return findAllSQL(sql, new Object[] {});
	}

	public void updateInactiveTestByCourseId(Long courseId) {
		executeUpdateSql("Update test set active = 'N' where courseId= ?", new Object[] { courseId });
	}

	public Test findByIDAndFaculty(Long id, String facultyId, String role) {
		if (role.equals("ROLE_ADMIN")) {
			final String sql = "SELECT t.* FROM " + getTableName()
					+ " t INNER JOIN user_roles ur ON ur.username = t.facultyId and ur.role = ? where t.id = ? and t.facultyId = ? and t.active = 'Y' ";
			return findOneSQL(sql, new Object[] { Role.ROLE_ADMIN.name(), id, facultyId });
		} else {
			final String sql = "SELECT t.* FROM " + getTableName()
					+ " t INNER JOIN user_roles ur ON ur.username = t.facultyId and ur.role = ? where t.id = ? and t.facultyId = ? and t.active = 'Y' ";
			return findOneSQL(sql, new Object[] { Role.ROLE_FACULTY.name(), id, facultyId });
		}
	}

	public Test authenticateUserPrivilages(int id, String username) {
		final String sql = "select * from " + getTableName()
				+ " where id=? and (createdBy =? or facultyId=?) and (startDate > sysdate() or endDate < sysdate()) ";
		return findOneSQL(sql, new Object[] { id, username, username });
	}

	public Page<Test> findByCourse(Long courseId, int pageNo, int pageSize) {
		final String sql = "SELECT t.*, c.courseName FROM " + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " and t.courseId = ? " + " and c.active='Y'  "
				+ " and t.active = 'Y' " + "  order by t.createdDate desc";
		final String countSql = "SELECT count(*) FROM " + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " and t.courseId = ? " + " and c.active='Y'  "
				+ " and t.active = 'Y' " + "  order by t.createdDate desc";
		return findAllSQL(sql, countSql, new Object[] { courseId }, pageNo, pageSize);

	}

	public Page<Test> searchActiveByExactMatchReplacementForHOD(Long programId, int pageNo, int pageSize) {

		String sql = " select t.* from " + getTableName()
				+ " t,course c , program p where t.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and t.active ='Y' and c.active ='Y' order by t.createdDate desc ";

		String sql2 = " select count(*) from " + getTableName()
				+ " t,course c , program p where t.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and t.active ='Y' and c.active ='Y' ";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { programId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));

	}

	public void updateFacultyAssigned(String facultyId, Long testId) {
		executeUpdateSql("Update test t set t.facultyId = ? where t.id=?", new Object[] { facultyId, testId });

	}

	public List<Test> findByUser(String username, String acadMonth, String acadYear) {
		final String sql = "SELECT t.* FROM " + getTableName() + " t INNER JOIN student_test st ON st.testId = t.id "
				+ " where st.username = ? " + " and t.acadMonth = ? " + " and t.acadYear = ? "
				+ " and t.startDate <= ? " + " and t.endDate >= ? " + " and t.active = 'Y' " + " order by endDate";
		Date dt = Utils.getInIST();
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear, dt, dt });
	}

	public List<Test> findByUserAndCourse(String username, Long courseId, String acadMonth, String acadYear) {
		final String sql = "SELECT t.* ,st.testCompleted,st.attempt FROM " + getTableName()
				+ " t INNER JOIN student_test st ON st.testId = t.id " + " where st.username = ? "
				+ " and t.courseId = ? " + " and t.acadMonth = ? " + " and t.acadYear = ? " + " and t.startDate <= ? "
				+ " and t.endDate >= ? " + " and t.active = 'Y' " + " order by endDate";
		Date dt = Utils.getInIST();
		/*
		 * Calendar cal = Calendar.getInstance(TimeZone .getTimeZone("Asia/Calcutta"));
		 * 
		 * cal.getTime()
		 */
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth, acadYear, dt, dt });
	}

	public List<Test> findByUserAndCourse(String username, Long courseId) {
		final String sql = "SELECT t.* FROM " + getTableName() + " t INNER JOIN student_test st ON st.testId = t.id "
				+ " where st.username = ? " + " and t.courseId = ? " + "  " + " and t.endDate >= ? "
				+ " and t.active = 'Y' and st.active = 'Y' " + " order by endDate";
		/*
		 * Calendar cal = Calendar.getInstance(TimeZone .getTimeZone("Asia/Calcutta"));
		 */
		Date dt = Utils.getInIST();
		return findAllSQL(sql, new Object[] { username, courseId, dt });
	}

	public Page<Test> findAllByUser(String username, String acadMonth, String acadYear, int pageNo, int pageSize) {
		final String sql = " FROM " + getTableName() + " t INNER JOIN student_test st ON st.testId = t.id "
				+ " inner join course c on c.id = t.courseId " + " where st.username = ? " + " and t.acadMonth = ? "
				+ " and t.acadYear = ? " + " and t.startDate <= ? " + " and t.active = 'Y' " + " order by endDate";
		Date dt = Utils.getInIST();
		return paginationHelper.fetchPage(getJdbcTemplate(),

				"SELECT COUNT(t.id) " + sql, "SELECT t.*, c.courseName " + sql,
				new Object[] { username, acadMonth, acadYear, dt }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<Test> findTestAllocated(String username, int pageNo, int pageSize) {
		String sql = "select *,t.id from " + getTableName()
				+ " t,student_test st,course c where  st.testId = t.id and st.username=? and c.id= st.courseId and t.active ='Y' and st.active = 'Y' and c.active ='Y'";

		String sql2 = "select count(*) from " + getTableName()
				+ " t,student_test st,course c where  st.testId = t.id and st.username=? and c.id= st.courseId and t.active ='Y' and st.active = 'Y' and c.active ='Y'";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { username }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<Test> findTestAllocatedbyCourseId(String username, Long courseId, int pageNo, int pageSize) {
		String sql = "select *,t.id from " + getTableName()
				+ " t,student_test st,course c where  st.testId = t.id and st.username=? and c.id= st.courseId and st.courseId=? and t.active ='Y' "
				+ " and st.active ='Y' order by t.endDate";

		String sql2 = "select count(*) from " + getTableName()
				+ " t,student_test st,course c where  st.testId = t.id and st.username=? and c.id= st.courseId and st.courseId=? "
				+ " and t.active ='Y' and st.active ='Y' order by t.endDate";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { username, courseId }, pageNo,
				pageSize, BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<Test> findAllByUserAndCourse(String username, Long courseId, String acadMonth, String acadYear,
			int pageNo, int pageSize) {
		final String sql = " FROM " + getTableName() + " t INNER JOIN student_test st ON st.testId = t.id "
				+ " inner join course c on c.id = t.courseId " + " where st.username = ? " + " and t.courseId = ? "
				+ " and t.acadMonth = ? " + " and t.acadYear = ? " + " and t.startDate <= ? " + " and t.active = 'Y' "
				+ " order by endDate";
		Date dt = Utils.getInIST();
		return paginationHelper.fetchPage(getJdbcTemplate(), "SELECT COUNT(t.id) " + sql,
				"SELECT t.*, c.courseName " + sql, new Object[] { username, courseId, acadMonth, acadYear, dt }, pageNo,
				pageSize, BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Test> findByCourse(Long courseId, String acadMonth, String acadYear) {
		final String sql = "SELECT t.* FROM " + getTableName() + " t " + " where t.courseId = ? "
				+ " and t.acadMonth = ? " + " and t.acadYear = ? " + " and t.active = 'Y' " + " order by testName";
		return findAllSQL(sql, new Object[] { courseId, acadMonth, acadYear });
	}

	public List<Test> findByCourse(Long courseId) {
		final String sql = "SELECT t.* FROM " + getTableName() + " t " + " where t.courseId = ? "
				+ " and t.active = 'Y' " + " order by testName";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<Test> findByFaculty(String username, String acadMonth, String acadYear) {
		final String sql = "SELECT t.* FROM " + getTableName() + " t " + " where t.facultyId = ? "
				+ " and t.acadMonth = ? " + " and t.acadYear = ? " + " and t.active = 'Y' " + " order by endDate";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public List<Test> findByFaculty(String username) {
		final String sql = "SELECT t.* FROM " + getTableName() + " t " + " where t.facultyId = ? "
				+ " and t.active = 'Y' " + " order by endDate";
		return findAllSQL(sql, new Object[] { username });
	}

	public Page<Test> findAllByFaculty(String username, String acadMonth, String acadYear, int pageNo, int pageSize) {
		final String sql = " FROM " + getTableName() + " t " + " inner join course c on c.id = t.courseId "
				+ " where t.facultyId = ? " + " and t.acadMonth = ? " + " and t.acadYear = ? " + " and t.active = 'Y' "
				+ " order by endDate";
		return paginationHelper.fetchPage(getJdbcTemplate(), "SELECT COUNT(t.id) " + sql,
				"SELECT t.*, c.courseName " + sql, new Object[] { username, acadMonth, acadYear }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<Test> findAllByFacultyAndCourse(String username, Long courseId, String acadMonth, String acadYear,
			int pageNo, int pageSize) {
		final String sql = " FROM " + getTableName() + " t " + " inner join course c on c.id = t.courseId "
				+ " where t.facultyId = ? " + " and t.courseId = ? " + " and t.acadMonth = ? " + " and t.acadYear = ? "
				+ " and t.active = 'Y' " + " order by endDate";
		return paginationHelper.fetchPage(getJdbcTemplate(), "SELECT COUNT(t.id) " + sql,
				"SELECT t.*, c.courseName " + sql, new Object[] { username, courseId, acadMonth, acadYear }, pageNo,
				pageSize, BeanPropertyRowMapper.newInstance(genericType));

	}

	public List<Test> findByFacultyAndCourse(String username, Long courseId, String acadMonth, String acadYear) {
		final String sql = "SELECT t.*, c.courseName FROM " + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " where t.facultyId = ? " + " and t.courseId = ? "
				+ " and t.acadMonth = ? " + " and t.acadYear = ? " + " and t.active = 'Y' " + " order by endDate";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth, acadYear });
	}

	public List<Test> findByFacultyAndCourse(String username, Long courseId) {
		final String sql = "SELECT t.*, c.courseName FROM " + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " where t.facultyId = ? " + " and t.courseId = ? "
				+ " and c.active='Y'  " + " and t.active = 'Y' " + " order by endDate desc";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public Page<Test> findByFacultyAndCourse(String username, Long courseId, int pageNo, int pageSize) {
		final String sql = "SELECT t.*, c.courseName FROM " + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " where t.facultyId = ? " + " and t.courseId = ? "
				+ " and c.active='Y'  " + " and t.active = 'Y' " + "  order by t.createdDate desc";
		final String countSql = "SELECT count(*) FROM " + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " where t.facultyId = ? " + " and t.courseId = ? "
				+ " and c.active='Y'  " + " and t.active = 'Y' " + "  order by t.createdDate desc";
		return findAllSQL(sql, countSql, new Object[] { username, courseId }, pageNo, pageSize);

	}

	public void softDeleteTest(Integer id) {
		final String sql = "delete from test" + " where id = ? ";
		getJdbcTemplate().update(sql, new Object[] { id });

	}

	public List<Test> findAllTest(String acadMonth, String acadYear) {
		String sql = " select t.* from test t " + " where t.acadMonth = ? and t.acadYear = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear });

	}

	public List<Test> findAllTestForFaculty(String acadMonth, String acadYear, String facultyId) {
		String sql = " select t.* from test t " + " where t.acadMonth = ? and t.acadYear = ? and t.facultyId = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, facultyId });

	}

	public List<Test> getAllTests(Long courseId, String username) {
		final String sql = " select t.maxScore , t.testName , t.testType , st.score , wd.wieghtageassigned "
				+ " from test t , student_test st , wieghtagedata wd"
				+ " where t.courseId=? and st.username = ? and t.id=st.testId and wd.wieghtagetype=t.testType ";
		return findAllSQL(sql, new Object[] { courseId, username });
	}

	public void showResults(Long id) {
		executeUpdateSql("update test set showResultsToStudents='Y' where id= ? ", new Object[] { id });
	}

	public Page<Test> searchActiveByExactMatchReplacement(String username, Long programId, int pageNo, int pageSize) {

		String sql = " select t.*,c.courseName as courseName from " + getTableName()
				+ " t,course c , program p where t.facultyId=? and t.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and t.active ='Y' and c.active ='Y' order by t.createdDate desc ";

		String sql2 = " select count(*) from " + getTableName()
				+ " t,course c , program p where t.facultyId=? and t.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and t.active ='Y' and c.active ='Y' ";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { username, programId }, pageNo,
				pageSize, BeanPropertyRowMapper.newInstance(genericType));

	}

	public List<Test> findTestsBySessionAndYearForProgram(String acadSession,

			Integer acadYear, Long programId) {

		String sql = "select t.* from test t "

				+ " inner join course c on c.id = t.courseId "

				+ " inner join program p on p.id = c.programId "

				+ " where c.acadSession = ? and c.acadYear = ? and programId = ? ";

		return findAllSQL(sql,

				new Object[] { acadSession, acadYear, programId });

	}

	public List<Test> findTestsBySessionAndYearForCollege(String acadSession,

			Integer acadYear) {

		String sql = "select t.* from test t "

				+ " inner join course c on c.id = t.courseId "

				+ " inner join program p on p.id = c.programId "

				+ " where c.acadSession = ? and c.acadYear = ? ";

		return findAllSQL(sql, new Object[] { acadSession, acadYear });

	}

	public List<Test> findAllTestsByFaculty(String username) {

		final String sql = "SELECT t.* FROM test t "

				+ " where t.facultyId = ?  and  t.active='Y' "

				+ " order by t.endDate desc";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<Test> findAllTestForProgram(Long programId) {

		String sql = "select a.* from test a,course c , program p where a.courseId= c.id and c.programId = p.id and "

				+ " p.id=? and a.active ='Y' order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { programId });

	}

	public int checkDatesForUpdate(Long testId) {
		final String sql = "select count(*) from test where startDate<=sysdate() and endDate>=SYSDATE() and  id = ?";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { testId });
	}

	public List<Test> findTestAllocated(String username) {
		String sql = "select *,t.id from " + getTableName()
				+ " t,student_test st,course c where  st.testId = t.id and st.username=? and c.id= st.courseId and t.active ='Y' and c.active ='Y' and t.startDate <= SYSDATE() and t.endDate >=SYSDATE() ";

		return findAllSQL(sql, new Object[] { username });
	}

	public void hideResults(Long id) {
		executeUpdateSql("update test set showResultsToStudents='N' where id= ? ", new Object[] { id });
	}

	public List<Test> findtestByStartDate(String startDate) {
		String sql = " select * from " + getTableName() + " where startDate like ? ";
		return findAllSQL(sql, new Object[] { startDate });
	}

	public int getStudentCountByTestId(Long id) {
		final String sql = "select count(*) from student_test where testId = ? and active = 'Y' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { id });
	}

	public List<Test> findTestAllocatedForContent(String username) {
		String sql = "select *,t.id from " + getTableName()
				+ " t,student_test st,course c where  st.testId = t.id and st.username=? and c.id= st.courseId and t.active ='Y' and c.active ='Y' and st.active='Y' and st.testCompleted='Y' and t.maxAttempt = st.attempt and t.startDate <= SYSDATE() and t.endDate >=SYSDATE() ";

		return findAllSQL(sql, new Object[] { username });
	}

//	public List<Test> findTestDetails(String startDate, String endDate) {
//		String sql = "SELECT t.* ,"
//				+ " count(distinct st.username) as studentCount FROM test t, student_test st "
//				+ " WHERE t.active='Y' AND st.active='Y' AND t.startDate >= ? AND t.endDate <= ? "
//				+ " AND st.testId=t.id   GROUP BY st.testId";
//
//		return findAllSQL(sql, new Object[] { startDate , endDate });
//	}
	public List<Test> findTestDetails(String startDate, String endDate) {

		if (!startDate.equals("") && !endDate.equals("")) {
			String sql = "SELECT t.* ," + " count(distinct st.username) as studentCount FROM test t, student_test st "
					+ " WHERE t.active='Y' AND st.active='Y' AND st.testId=t.id and t.startDate between ? AND  ? "
					+ " GROUP BY st.testId";

			return findAllSQL(sql, new Object[] { startDate, endDate });
		} else {
			String sql = "SELECT t.* ," + " count(distinct st.username) as studentCount FROM test t, student_test st "
					+ " WHERE t.active='Y' AND st.active='Y' AND st.testId=t.id and t.startDate <= now() AND t.endDate >= now() "
					+ " GROUP BY st.testId";

			return findAllSQL(sql, new Object[] {});
		}
	}

	// 10-07-2020
	public Page<Test> findByFacultyAndCourseForDemo(String username, Long courseId, int pageNo, int pageSize) {
		final String sql = "SELECT t.*, c.courseName FROM student_test st," + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " where t.id = st.testId and t.courseId = ? "
				+ " and st.username = ? and c.active='Y'  " + " and t.active = 'Y' " + "  order by t.createdDate desc";
		final String countSql = "SELECT count(*) FROM student_test st," + getTableName() + " t "
				+ " inner join course c on c.id = t.courseId " + " where t.id = st.testId and t.courseId = ? "
				+ " and st.username = ? and c.active='Y'  " + " and t.active = 'Y' " + "  order by t.createdDate desc";
		return findAllSQL(sql, countSql, new Object[] { courseId, username }, pageNo, pageSize);

	}

	public Page<Test> searchActiveByExactMatchReplacementForDemo(String username, Long programId, int pageNo,
			int pageSize) {

		String sql = " select t.*,c.courseName as courseName from " + getTableName()
				+ " t,course c , program p, student_test st where st.username=? and t.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and t.id=st.testId and t.active ='Y' and c.active ='Y'  order by t.createdDate desc ";

		String sql2 = " select count(*) from " + getTableName()
				+ " t,course c , program p, student_test st where st.username=? and t.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and t.id=st.testId and t.active ='Y' and c.active ='Y' ";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { username, programId }, pageNo,
				pageSize, BeanPropertyRowMapper.newInstance(genericType));

	}

	// 27-07-2020

	public void updateHideOrShowReports(Long id, String showReportsToStudents) {
		executeUpdateSql("update test set showReportsToStudents= ? where id= ? ",
				new Object[] { showReportsToStudents, id });
	}

	public Page<Test> searchTestCreatedByAdminForFaculty(String username, Long programId, String moduleId,
			String acadYear, int pageNo, int pageSize) {

		if (moduleId != null && acadYear != null) {
			String sql = " SELECT t.*,c.moduleName AS courseName FROM test t,student_test st,course c WHERE "
					+ " t.id=st.testId AND st.username=? and c.programId =? and c.moduleId=? and t.acadYear =? "
					+ " AND t.isCreatedByAdmin='Y' AND t.active = 'Y' AND t.moduleId=c.moduleId and t.acadYear=c.acadYear "
					+ " AND st.active='Y' GROUP BY t.id ";

			String sql2 = " SELECT count(distinct t.id) FROM test t,student_test st,course c WHERE "
					+ " t.id=st.testId AND st.username=? and c.programId =? and c.moduleId=? and t.acadYear =? "
					+ " AND t.isCreatedByAdmin='Y' AND t.active = 'Y' AND t.moduleId=c.moduleId and t.acadYear=c.acadYear "
					+ " AND st.active='Y' GROUP BY t.id ";
			return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql,
					new Object[] { username, programId, moduleId, acadYear }, pageNo, pageSize,
					BeanPropertyRowMapper.newInstance(genericType));
		} else {
			String sql = " SELECT t.*,c.moduleName AS courseName FROM test t,student_test st,course c WHERE "
					+ " t.id=st.testId AND st.username=? and c.programId =?  "
					+ " AND t.isCreatedByAdmin='Y' AND t.active = 'Y' AND t.moduleId=c.moduleId  "
					+ " AND st.active='Y' GROUP BY t.id ";

			String sql2 = " SELECT count(distinct t.id) FROM test t,student_test st,course c WHERE "
					+ " t.id=st.testId AND st.username=? and c.programId =?  "
					+ " AND t.isCreatedByAdmin='Y' AND t.active = 'Y' AND t.moduleId=c.moduleId  "
					+ " AND st.active='Y' GROUP BY t.id ";
			return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { username, programId },
					pageNo, pageSize, BeanPropertyRowMapper.newInstance(genericType));
		}

	}

	public Page<Test> searchActiveByExactMatchReplacementForAdmin(String username, Long programId, int pageNo,
			int pageSize) {

		String sql = " select t.*,c.moduleName as courseName from " + getTableName()
				+ " t,course c , program p where t.facultyId=? and t.moduleId= c.moduleId and c.programId = p.id and "
				+ " p.id= ? and t.active ='Y' and c.active ='Y'  group by t.id order by t.createdDate desc ";

		String sql2 = " select count(distinct t.id) from " + getTableName()
				+ " t,course c , program p where t.facultyId=? and t.moduleId= c.moduleId and c.programId = p.id and "
				+ " p.id= ? and t.active ='Y' and c.active ='Y' ";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new Object[] { username, programId }, pageNo,
				pageSize, BeanPropertyRowMapper.newInstance(genericType));

	}

	// new queries
	public List<Test> getTestForIcaModue(String moduleId, String acadYear, String facultyId, String courseId) {
		if (courseId != null) {
			String sql = "select t.*,c.courseName,c.id as courseId FROM test t,course c where t.courseId=c.id "
					+ " and c.moduleId=? and c.acadYearCode=? and t.facultyId=? and c.id =? and t.active='Y' and c.active='Y' "
					+ " group by t.id ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, facultyId, courseId });
		} else {
			String sql = "select t.*,c.courseName,c.id as courseId FROM test t,course c where t.courseId=c.id "
					+ " and c.moduleId=? and c.acadYearCode=? and t.facultyId=? and t.active='Y' and c.active='Y' "
					+ " group by t.id ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, facultyId });
		}

	}

	public List<Test> getCoursesForTestIca(String moduleId, String acadYear, String facultyId, String courseIdDivWise) {

		if (courseIdDivWise == null) {
			String sql = "select  c.id as courseId,c.courseName FROM test t,course c where t.courseId=c.id "
					+ " and c.moduleId=? and c.acadYearCode=? and t.facultyId=? and t.active='Y' and c.active='Y' "
					+ " group by t.courseId ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, facultyId });
		} else {
			String sql = "select  c.id as courseId,c.courseName FROM test t,course c where t.courseId=c.id "
					+ " and c.moduleId=? and c.acadYearCode=? and t.facultyId=? and c.id=? and t.active='Y' and c.active='Y' "
					+ " group by t.courseId ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, facultyId, courseIdDivWise });
		}

	}
	public List<Test> getTestsByIds(List<String> testId) {
		Map<String, Object> mapper = new HashMap<>();
		mapper.put("testId", testId);
		String sql = " select distinct t.maxScore from test t where t.id in(:testId) " + " and t.active='Y'  ";

		return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(Test.class));
	}
}
