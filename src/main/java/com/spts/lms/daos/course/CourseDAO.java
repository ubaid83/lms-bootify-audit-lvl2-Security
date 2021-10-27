package com.spts.lms.daos.course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("courseDAO")
public class CourseDAO extends BaseDAO<Course> {

	@Override
	protected String getTableName() {

		return "course";
	}

	@Override
	protected String getInsertSql() {

		/*
		 * String sql =
		 * "Insert into course (id,acadYear,acadMonth,eventName,programId,abbr, courseName, property1, property2, property3, "
		 * +
		 * " createdDate, lastModifiedDate, createdBy, lastModifiedBy,acadSession,dept) values"
		 * +
		 * "(:id,:acadYear,:acadMonth,:eventName,:programId,:abbr, :courseName, :property1, :property2, :property3,"
		 * +
		 * " :createdDate, :lastModifiedDate, :createdBy, :lastModifiedBy,:acadSession,:dept)"
		 * ;
		 */

		String sql = "Insert into course (id,acadYear,acadMonth,eventName,programId,abbr, courseName, property1, property2, property3, "
				+ " createdDate, lastModifiedDate, createdBy, lastModifiedBy,acadSession,dept,moduleId,moduleName,campusId,moduleAbbr,deptCode,moduleCategoryCode,moduleCategoryName,acadYearCode) values"
				+ "(:id,:acadYear,:acadMonth,:eventName,:programId,:abbr, :courseName, :property1, :property2, :property3,"
				+ " :createdDate, :lastModifiedDate, :createdBy, :lastModifiedBy,:acadSession,:dept,:moduleId,:moduleName,:campusId,:moduleAbbr,:deptCode,:moduleCategoryCode,:moduleCategoryName,:acadYearCode)";

		return sql;
		
	}

	@Override
	protected String getUpdateSql() {

		String sql = "update course  "
				+ " set abbr=:abbr, "
				+ " courseName=:courseName, "
				+ " property1=:property1, "
				+ " property2=:property2, "
				+ " dept=:dept, "
				+ " property3=:property3,programId = :programId, "
				+ " lastModifiedDate=:lastModifiedDate,acadSession=:acadSession, "
				+ " lastModifiedBy=:lastModifiedBy " + " where id=:id";
		return sql;
		
		
	}

	Calendar now = Calendar.getInstance();

	public List<Course> getAllCourses(String courseId) {

		String sql = "SELECT distinct  c.id, c.abbr, c.courseName, c.sessionType, c.durationInMonths, c.maxDurationInMonths, "
				+ " c.revisedFromMonth, c.revisedFromYear, "
				+ " c.createdDate, c.lastModifiedDate, c.createdBy, c.lastModifiedBy "
				+ " FROM course c   " + " WHERE c.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<String> getAllAcadSession() {

		String sql = "select distinct acadSession from course where acadSession is not null order by acadSession  asc";
		return listOfStringParameter(sql, new Object[] {});
	}

	public List<Course> findByProgramIdAcadSession(String programId,
			List<String> acadSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);

		String sql = "  select * from course where programId = :programIds and active='Y' and acadSession in (:acadSessions)";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));
	}

	/*
	 * public List<String> findDistinctDepartment(List<String> acadSession) {
	 * 
	 * Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("acadSessions", acadSession);
	 * 
	 * final String sql =
	 * "select distinct dept from course where acadsession in (:acadSessions) ";
	 * 
	 * 
	 * return getNamedParameterJdbcTemplate().queryForList(sql,
	 * params,String.class); //query(sql, params,
	 * //BeanPropertyRowMapper.newInstance(String.class)); }
	 */

	public List<String> findDistinctDepartment(List<String> acadSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);

		final String sql = "select distinct dept from course where dept is not null and acadsession in (:acadSessions)";

		return getNamedParameterJdbcTemplate().queryForList(sql, params,
				String.class);
		// query(sql, params,
		// BeanPropertyRowMapper.newInstance(String.class));
	}

	public List<String> getAcadSessionForActiveFeedback() {

		String sql = "select distinct acadSession from course c ,student_feedback sf where c.id = sf.courseId and  c.acadSession is not null  order by acadSession  asc";
		return listOfStringParameter(sql, new Object[] {});
	}

	/*
	 * public List<Course> findByCoursesBasedOnProgramName(String progName) {
	 * 
	 * String sql =
	 * "select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y'"
	 * ;
	 * 
	 * return findAllSQL(sql, new Object[] { progName }); }
	 */
	
	/*public List<Course> findByCoursesBasedOnProgramName(String progName) {

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = "select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y' "
					+ " and c.acadYear = " + currentYear;
		} else {
			sql = " select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y'"
					+ " and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year + ") ";
		}

		return findAllSQL(sql, new Object[] { progName });
	}*/
	
	public List<Course> findByCoursesBasedOnProgramName(String progName) {

        Calendar c = Calendar.getInstance();
        c.setTime(Utils.getInIST());
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR) - 1;
        int currentYear = c.get(Calendar.YEAR);
        String sql = "";

        if (month > 6) {
                        sql = "select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y' "
                                                        + " and (c.acadYear = " + currentYear +  " or c.programId = 50003116) ";
        } else {
                        sql = " select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y'"
                                                        + " and (c.acadYear = "
                                                        + currentYear
                                                        + " or c.acadYear = "
                                                        + year +  " or c.programId = 50003116) ";
        }

        return findAllSQL(sql, new Object[] { progName });
}


	public List<Course> findByCoursesBasedOnProgramNameAndYear(String progName,
			Integer acadYear) {

		String sql = "select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y' and c.acadYear = ?";

		return findAllSQL(sql, new Object[] { progName, acadYear });
	}

	public List<Course> getAllCoursesActive() {

		String sql = "SELECT distinct  c.id, c.abbr, c.courseName, c.durationInMonths, c.maxDurationInMonths, "
				+ " c.revisedFromMonth, c.revisedFromYear, "
				+ " c.createdDate, c.lastModifiedDate, c.createdBy, c.lastModifiedBy "
				+ " FROM course c   " + " WHERE c.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<Course> findByUserActiveInterdesciplinary(String username) {
		String sql = " SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ?  "
				+ " AND c.active = 'Y' and uc.active='Y' ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Course> findByCourseName(String courseName) {
		String sql = "select * from course where courseName = ?";
		return findAllSQL(sql, new Object[] { courseName });
	}

	public List<Course> findByAcadSession(List<String> acadSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);

		final String sql = " select * from course where acadsession in (:acadSessions)";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));

	}

	public List<Course> findByProgramIdSessionId(Integer programId,
			Integer sessionId) {
		String sql = "SELECT c.* FROM course c INNER JOIN program_session_course psc on psc.courseId = c.id "
				+ " INNER JOIN program_session ps on ps.id = psc.programSessionId where ps.programId = ? and ps.sessionNumber = ? "
				+ " AND c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { programId, sessionId });
	}

	public List<String> getFacultiesByProgram(String programId) {
		String sql = " select distinct uc.username from user_course uc, course c where c.programId = ? and uc.role = 'ROLE_FACULTY'";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { programId });
	}

	@Cacheable("user_courses")
	public List<Course> findByUser(String username, String acadMonth,
			String acadYear) {
		String sql = "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? "
				+ " and uc.acadMonth = ? "
				+ " and uc.acadYear = ? " + " AND c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	@Cacheable("user_courses")
	public List<Course> findByUser(String username, Long programId) {
		String sql = "SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? and c.programId=? "
				+ " AND c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { username, programId });
	}

	public List<Course> findByUser(String username) {
		String sql = "SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? " + " AND c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { username });
	}

	public void makeInActive(Long id) {
		executeUpdateSql("update course set active='N' where id= ? ",
				new Object[] { id });
	}

	/*
	 * public List<Course> findByUserActive(String username, Long programId) {
	 * String sql =
	 * "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
	 * + "  where uc.username = ?  and c.programId =? " +
	 * " AND c.active = 'Y' ";
	 * 
	 * return findAllSQL(sql, new Object[] { username, programId }); }
	 */

	// added on 21/11/2017
	/*
	 * public List<Course> findByUserActive(String username, String programName)
	 * { String sql =
	 * " SELECT c.* FROM course c  INNER JOIN user_course uc on uc.courseId = c.id  "
	 * + " INNER JOIN program p on p.id= c.programId " +
	 * " where uc.username = ?  and  p.programName= ? " +
	 * " AND c.active = 'Y' and uc.active = 'Y' ";
	 * 
	 * return findAllSQL(sql, new Object[] { username, programName }); }
	 * 
	 * // ends public List<Course> findByUserActive(String username) { String
	 * sql =
	 * "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
	 * + "  where uc.username = ? " + " AND c.active = 'Y' ";
	 * 
	 * return findAllSQL(sql, new Object[] { username }); }
	 */

	public List<Course> findByUserActive(String username, String programName) {

		/*
		 * String sql =
		 * " SELECT c.* FROM course c  INNER JOIN user_course uc on uc.courseId = c.id  "
		 * + " INNER JOIN program p on p.id= c.programId " +
		 * " where uc.username = ?  and  p.programName= ? " +
		 * " AND c.active = 'Y' and (c.acadYear = " + now.get(Calendar.YEAR) +
		 * " or c.programId = 50003116)  "; return findAllSQL(sql, new Object[]
		 * { username, programName });
		 */

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		System.out.println("month --->" + month);
		if (month > 6) {
			sql = " SELECT c.* FROM course c  INNER JOIN user_course uc on uc.courseId = c.id  "
					+ " INNER JOIN program p on p.id= c.programId "
					+ " where uc.username = ? and uc.active='Y' and  p.programName= ? "
					+ " AND c.active = 'Y' and (c.acadYear = "
					+ currentYear
					+ " or c.programId = 50003116)  ";
		} else {
			sql = " SELECT c.* FROM course c  INNER JOIN user_course uc on uc.courseId = c.id  "
					+ " INNER JOIN program p on p.id= c.programId "
					+ " where uc.username = ? and uc.active='Y' and  p.programName= ? "
					+ " AND c.active = 'Y' and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year
					+ " or c.programId = 50003116)  ";
		}
		return findAllSQL(sql, new Object[] { username, programName });
	}

	// ends
	public List<Course> findByUserActive(String username) {
		/*
		 * String sql =
		 * "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
		 * + "  where uc.username = ?  AND c.active = 'Y'  and (c.acadYear = " +
		 * now.get(Calendar.YEAR) + " or c.programId = 50003116)  ";
		 * 
		 * return findAllSQL(sql, new Object[] { username });
		 */

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.username = ? and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
					+ currentYear + " or c.programId = 50003116)  ";
		} else {
			sql = "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.username = ? and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year
					+ " or c.programId = 50003116)  ";
		}

		return findAllSQL(sql, new Object[] { username });
	}

	public String findTerms(String courseId) {
		String sql = " select distinct uc.acadSession user_course uc where "
				+ "  uc.courseId= ? and uc.role='ROLE_STUDENT' ";

		return getJdbcTemplate().queryForObject(sql, new Object[] { courseId },
				String.class);

	}

	public Course findByIDAndFaculty(Long id, String facultyId) {
		final String sql = "SELECT c.* FROM "
				+ getTableName()
				+ " c INNER JOIN user_course uc ON uc.courseId = c.id and uc.role = ? where c.id = ? and uc.username = ? and c.active = 'Y' ";
		return findOneSQL(sql, new Object[] { Role.ROLE_FACULTY.name(), id,
				facultyId });
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<String> getAllAcadYear() {

		String sql = "select distinct acadYear from course where acadYear is not null";
		return listOfStringParameter(sql, new Object[] {});
	}

	public List<String> getAllAcadMonth() {

		String sql = "select distinct acadMonth from course where acadMonth is not null";
		return listOfStringParameter(sql, new Object[] {});
	}

	public void updateCourseToMakeInactive(Long id) {
		executeUpdateSql("Update course set active = 'N' where id= ?",
				new Object[] { id });
	}

	public List<Course> findNotAssignedCourseProgramwise(String ProgramId) {
		/*
		 * String sql =
		 * " Select distinct c.id,c.courseName from course c,wieghtagedata wd, program p where c.id != wd.courseId and  "
		 * + " c.programId = p.id and p.id=? and c.active='Y'  ";
		 */
		String sql = "Select distinct c.id,c.courseName from course c,program p where c.id not in (select w.courseId from  wieghtagedata w ) and c.programId = p.id and p.id=? and c.active='Y' ";

		return findAllSQL(sql, new Object[] { ProgramId });
	}

	public List<Course> findByProgramIdAcadSessionAcadYear(Long programId,
			String acadYear, String acadSession) {
		String sql = "select * from course c where c.programId = ? and c.acadYear = ? and c.acadSession = ? order by courseName";
		return findAllSQL(sql,
				new Object[] { programId, acadYear, acadSession });
	}

	public List<Course> findByAcadSessionAndYear(List<String> acadSession,
			String acadYear) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);

		final String sql = " select * from course where acadsession in (:acadSessions)"
				+ " and acadYear = :acadYear and active = 'Y' ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));

	}

	public List<Course> findByAcadSessionAndYearAndCampus(
			List<String> acadSession, String acadYear, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);

		final String sql = " select * from course where acadsession in (:acadSessions)"
				+ " and acadYear = :acadYear and campusId = :campusId and active = 'Y' ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));

	}

	public List<String> getAcadSessionForActiveFeedbackForFaculty(
			String facultyId) {

		String sql = "select distinct acadSession from course c ,student_feedback sf , feedback f, program p "
				+ " where c.id = sf.courseId and  c.acadSession is not null "
				+ " and sf.facultyId=? and f.id=sf.feedbackId and f.isPublished='Y' "
				+ " and c.programId=p.id and c.id=sf.courseId  order by acadSession  asc";

		return listOfStringParameter(sql, new Object[] { facultyId });
	}

	public List<Course> getUserCourseByUsername(String username) {
		/*
		 * String sql =
		 * "SELECT c.*, p.programName FROM course c, user_course uc, program p where uc.courseId = c.id AND c.programId = p.id "
		 * +
		 * "AND uc.username = ?  AND c.active = 'Y'  and (c.acadYear =  '2018' or c.programId = 50003116)"
		 * ; return findAllSQL(sql, new Object[] { username });
		 */

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = "SELECT c.*, p.programName FROM course c, user_course uc, program p where uc.courseId = c.id AND c.programId = p.id "
					+ " AND uc.username = ? and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
					+ currentYear + " or c.programId = 50003116) ";
		} else {
			sql = "SELECT c.*, p.programName FROM course c, user_course uc, program p where uc.courseId = c.id AND c.programId = p.id "
					+ "AND uc.username = ? and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year
					+ " or c.programId = 50003116)  ";
		}
		return findAllSQL(sql, new Object[] { username });
	}

	public List<Course> findByUserByOtherDB(String username, Long programId) {

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = "SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.username = ? and c.programId=? and c.acadYear = "
					+ currentYear + " AND c.active = 'Y' AND uc.active = 'Y' ";
		} else {
			sql = "SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.username = ? and c.programId=? and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year
					+ " )AND c.active = 'Y' AND uc.active = 'Y' ";
		}

		return getJdbcTemplate().query(sql,
				BeanPropertyRowMapper.newInstance(Course.class),
				new Object[] { username, programId });
	}

	public void deleteSoftById(final String courseId) {
		final String sql = "Update " + getTableName()
				+ " set active = 'N' WHERE ID = ?";

		logger.info("SQl 1---------->" + sql);

		getJdbcTemplate().update(sql, new Object[] { courseId });

	}

	public List<Course> getModulesByFaculty(String facultyId, String acadYear,
			List<String> acadSessions) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadYear", acadYear);
		params.put("facultyId", facultyId);
		params.put("acadSessions", acadSessions);

		String sql = " select distinct c.moduleName,SUBSTRING_INDEX(p.programName,'-',-1) as programName from course c,user_course uc,program p "
				+ " where c.id=uc.courseId and uc.username = :facultyId and uc.acadYear=:acadYear and uc.active = 'Y' and c.programId = p.id "
				+ " and (c.moduleName is not null or c.moduleName <> '') and c.acadSession in(:acadSessions) ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));
	}

	public List<Course> findCoursesByUserForApp(String username) {
		String sql = "SELECT distinct c.id, c.courseName, c.programId, c.acadSession FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? " + " AND c.active = 'Y' ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Course> findCoursesByUserAndProgramIdForApp(String username,
			long programId) {
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";
		if (month > 6) {
			sql = "SELECT distinct c.id, c.courseName, c.programId, c.acadSession FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.username = ?  and c.programId = ? "
					+ " AND c.active = 'Y' and c.acadYear = "
					+ currentYear
					+ " and uc.active = 'Y' ";
		} else {
			sql = "SELECT distinct c.id, c.courseName, c.programId, c.acadSession FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.username = ?  and c.programId = ? "
					+ " AND c.active = 'Y' and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = " + year + " ) and uc.active = 'Y' ";
		}
		return findAllSQL(sql, new Object[] { username, programId });
	}

	public List<Course> findStudentsByCourseIdForApp(long courseId) {
		String sql = "SELECT distinct uc.username, u.firstname, u.lastname, u.rollNo FROM course c , user_course uc, users u "
				+ "where uc.courseId = c.id and uc.username = u.username and c.id = ? and uc.role = 'ROLE_STUDENT' AND c.active = 'Y' and uc.active = 'Y' and u.enabled = 1 and u.active = 'Y' order by u.rollNo";

		return findAllSQL(sql, new Object[] { courseId });
	}

	/*
	 * public List<Course> findProgramsByUserForApp(String username) { String
	 * sql =
	 * " SELECT distinct c.programId, p.programName FROM course c INNER JOIN user_course uc on uc.courseId = c.id, program p "
	 * + " where c.programId = p.id and uc.username = ?  AND c.active = 'Y' ";
	 * 
	 * return findAllSQL(sql, new Object[] { username }); }
	 */

	public List<Course> findProgramsByUserForApp(String username) {

		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = " SELECT distinct c.programId, p.programName FROM course c INNER JOIN user_course uc on uc.courseId = c.id, program p "
					+ " where c.programId = p.id and uc.username = ? and c.acadYear = "
					+ currentYear + "  AND c.active = 'Y' ";
		} else {
			sql = " SELECT distinct c.programId, p.programName FROM course c INNER JOIN user_course uc on uc.courseId = c.id, program p "
					+ " where c.programId = p.id and uc.username = ? and (c.acadYear = "
					+ year
					+ " or c.acadYear = "
					+ currentYear
					+ ") AND c.active = 'Y' ";
		}

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Course> findByAcadSession(List<String> acadSession,
			String acadYear1, String acadYear2, String campusId) {

		if (!campusId.equals("null")) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("acadSessions", acadSession);
			params.put("acadYear1", acadYear1);
			params.put("acadYear2", acadYear2);
			params.put("campusId", campusId);

			final String sql = " select * from course c where c.acadsession in (:acadSessions) "
					+ " and (c.acadYear = :acadYear1 or c.acadYear = :acadYear2) and c.campusId = :campusId";

			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(Course.class));
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("acadSessions", acadSession);
			params.put("acadYear1", acadYear1);
			params.put("acadYear2", acadYear2);

			final String sql = " select * from course c where c.acadsession in (:acadSessions) "
					+ " and (c.acadYear = :acadYear1 or c.acadYear = :acadYear2) ";

			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(Course.class));
		}

	}

/*	public String getModuleName(String moduleId) {
		String sql = " select distinct moduleName from course where moduleId = ? limit 1";

		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] { moduleId });
	}*/
	public String getModuleName(String moduleId){
		String sql = " select distinct moduleName from course where moduleId = ? and moduleId is not null limit 1 ";
		
		return getJdbcTemplate().queryForObject(sql, String.class,new Object[]{moduleId});
	}
	public String getModuleAbbr(String moduleId){
		String sql = " select distinct moduleAbbr from course where moduleId = ? and moduleId is not null limit 1 ";
		
		return getJdbcTemplate().queryForObject(sql, String.class,new Object[]{moduleId});
	}

	public List<Course> moduleListByAcadYearAndCampus(String acadSession,
			String acadYear, String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			/*String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where c.acadSession=? and c.acadYear = ? and c.campusId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.moduleId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId group by c.moduleId";
			*/
			
			// acad year code
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where c.acadSession=? and c.acadYearCode = ? and c.campusId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.moduleId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId group by c.moduleId";

			return findAllSQL(sql, new Object[] { acadSession, acadYear,
					campusId });
		} else {
		/*	String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName"
					+ " from course c,program p where c.acadSession=? and c.acadYear = ?    "
					+ " and c.programId=p.id and c.active = 'Y'  and c.moduleId is not null group by c.moduleId";

*/
			//acad Year Code
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName"
					+ " from course c,program p where c.acadSession=? and c.acadYearCode = ?    "
					+ " and c.programId=p.id and c.active = 'Y'  and c.moduleId is not null group by c.moduleId";

			
			
			return findAllSQL(sql, new Object[] { acadSession, acadYear });
		}

	}

	public List<Course> acadSessionListByAcadYearAndCampus(String acadYear,
			String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			String sql = " select distinct acadSession " + " from "
					+ getTableName()
					+ " where acadYearCode = ? and campusId = ? and active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear, campusId });
		} else {
			String sql = " select distinct acadSession " + " from "
					+ getTableName() + " where acadYearCode = ? and active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear });
		}

	}
	
	public List<Course> acadSessionListByAcadYearAndCampusForIca(String acadYear,
			String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			String sql = " select distinct acadSession " + " from ica where acadYear = ? and campusId = ? and active = 'Y' and acadSession IS NOT NULL ";

			return findAllSQL(sql, new Object[] { acadYear, campusId });
		} else {
			String sql = " select distinct acadSession " + " from ica where acadYear = ? and active = 'Y' and acadSession IS NOT NULL ";

			return findAllSQL(sql, new Object[] { acadYear });
		}

	}
	public List<Course> acadSessionListByAcadYearAndCampusForTee(String acadYear,
			String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			String sql = " select distinct acadSession " + " from tee where acadYear = ? and campusId = ? and active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear, campusId });
		} else {
			String sql = " select distinct acadSession " + " from tee where acadYear = ? and active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear });
		}

	}

	public List<Course> acadSessionListByAcadYearAndCampusForModule(
			String acadYear, String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			String sql = " select distinct sm.sapSessionText as acadSession "
					+ " from module m, session_master sm, program_campus pc "
					+ " where m.session_code = sm.sapSessionCode and m.program_id = pc.programId "
					+ " and  m.acadYear = ? and pc.campusId = ? and sm.active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear, campusId });
		} else {
			String sql = " select distinct sm.sapSessionText as acadSession "
					+ " from module m, session_master sm "
					+ " where m.session_code = sm.sapSessionCode and  "
					+ " m.acadYear = ? and sm.active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear });
		}

	}

	public List<Course> moduleListByAcadYearAndCampusForModule(
			String acadSession, String acadYear, String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			String sql = " select distinct m.module_id as moduleId, m.module_description as moduleName, m.module_abbr as moduleAbbr, sm.sapSessionText as acadSession, p.programName, pc.campusName "
					+ " from module m, session_master sm, program p, program_campus pc "
					+ " where m.session_code = sm.sapSessionCode and m.program_id = p.id and p.id = pc.programId "
					+ " and sm.sapSessionText = ? and  m.acadYear = ? and pc.campusId = ? and sm.active = 'Y' group by m.module_id ";

			return findAllSQL(sql, new Object[] { acadSession, acadYear,
					campusId });
		} else {
			String sql = " select distinct m.module_id as moduleId, m.module_description as moduleName, m.module_abbr as moduleAbbr, sm.sapSessionText as acadSession, p.programName "
					+ " from module m, session_master sm, program p "
					+ " where m.session_code = sm.sapSessionCode and  m.program_id = p.id "
					+ " and sm.sapSessionText = ? and m.acadYear = ? and sm.active = 'Y' group by m.module_id ";

			return findAllSQL(sql, new Object[] { acadSession, acadYear });
		}

	}

	public String getModuleNameForNonEvent(String moduleId) {
		String sql = " select distinct module_description as moduleName from module where module_id = ? limit 1";

		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] { moduleId });
	}
	
	public List<Course> findStudentsByCourseIdForAndroidApp(long courseId) 
	{
		String sql = "SELECT distinct uc.username, c.id, u.firstname, u.lastname, u.rollNo FROM course c , user_course uc, users u "
				+ "where uc.courseId = c.id and uc.username = u.username and c.id = ? and uc.role = 'ROLE_STUDENT' AND c.active = 'Y' and uc.active = 'Y' and u.enabled = 1 and u.active = 'Y' order by u.rollNo";

		return findAllSQL(sql, new Object[] { courseId });
	}
	
	public List<Course> findStudentsByMultipleCourseId(List<Long> courseIds){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseIds", courseIds);
		

		String sql = "SELECT distinct uc.username,c.id, u.firstname, u.lastname, u.rollNo FROM course c , user_course uc, users u "
				+ "where uc.courseId = c.id and uc.username = u.username and c.id in (:courseIds) and uc.role = 'ROLE_STUDENT' AND c.active = 'Y' and uc.active = 'Y' and u.enabled = 1 and u.active = 'Y' order by u.rollNo";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));
	}
	
	public List<Course> findStudentCountCourseWise(List<Long> courseIds){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseIds", courseIds);
		
		String sql = "SELECT count(distinct uc.username) as count, c.id FROM course c , user_course uc, users u "
				+ "where uc.courseId = c.id and uc.username = u.username and c.id in (:courseIds) and uc.role = 'ROLE_STUDENT' AND c.active = 'Y' and uc.active = 'Y' "
				+ "and u.enabled = 1 and u.active = 'Y' group by c.id;";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Course.class));
	}
	
	public List<Course> findCoursesByModuleId(Long moduleId, String username, String acadYear) {
		String sql = " select c.id, c.courseName from course c, user_course uc  "
				+ " where c.id = uc.courseId and c.moduleId = ? and uc.username = ? and uc.acadYear = ? and c.active='Y'";

		return findAllSQL(sql, new Object[] { moduleId, username, acadYear });
	}
	//forSearch
	public List<Course> findCoursesByModuleId(Long moduleId, String username, String acadYear,String programId) {
		String sql = " select c.id, c.courseName from course c, user_course uc  "
				+ " where c.id = uc.courseId and c.moduleId = ? and uc.username = ? and uc.acadYear = ? and programId = ? and c.active='Y' and uc.active ='Y'";

		return findAllSQL(sql, new Object[] { moduleId, username, acadYear, programId});
	}
	
	public List<Course> findModulesByUsername(String username, String acadYear, Long programId) {
		
		String sql = "";
		
		sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc "
				+ " where c.id = uc.courseId and uc.username = ? and c.moduleId is not NULL and uc.acadYear = ? and programId = ?";
		
		return findAllSQL(sql, new Object[] { username,acadYear, programId });
	}
	

	

	
	  public Course findAcadMonthByModuleId(String moduleId)

	    {
	          String sql="SELECT DISTINCT acadMonth FROM course WHERE moduleId = ? AND acadMonth IS NOT NULL";
	          //String sql="SELECT DISTINCT acadMonth FROM course WHERE moduleId = ? ";
	          return findOneSQL(sql, new Object[]{moduleId});

	    }
	
	
	public Course findByModuleIdAndAcadYear(String moduleId, String acadYear){
		String sql = "select distinct moduleId, moduleName,moduleAbbr from course where moduleId = ? and acadYear = ?";
		
		return findOneSQL(sql, new Object[]{moduleId,acadYear});
		
	}
	
	public Course findByModuleIdAndAcadYearCode(String moduleId, String acadYear){
		String sql = "select distinct moduleId, moduleName,moduleAbbr from course where moduleId = ? and acadYearCode = ?";
		
		return findOneSQL(sql, new Object[]{moduleId,acadYear});
		
	}
	
    public String getAcadMonthByModuleIdAndAcadYear(String moduleId, String acadYear)
	{
		String sql="SELECT DISTINCT acadMonth FROM course WHERE moduleId = ? AND acadYear = ? and acadMonth is not null";
		
		 //findOneSQL(sql, new Object[]{moduleid, acadYear});
		 
		 return getJdbcTemplate().queryForObject(sql, String.class,new Object[]{moduleId,acadYear});
	}
    
    public List<Course> findCoursesByUsernameAndProgramId(String username, Long programId) {
    	Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";
		
		if (month > 6) {
			sql = " SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? and c.programId=? and c.acadYear = " + currentYear + "  AND c.active = 'Y' ";
		}else{
			sql = " SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? and c.programId=?  and (c.acadYear = "+ year +" or c.acadYear = "+ currentYear +") AND c.active = 'Y' ";
		}
		/*String sql = "SELECT distinct c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ "  where uc.username = ? and c.programId=? "
				+ " AND c.active = 'Y' ";*/

		return findAllSQL(sql, new Object[] { username, programId });
	}
    
  
    public List<Course> getAcadYearByModuleId(String moduleId) {
		String sql = " select distinct(acadYear) from course  where moduleId=?";

		return findAllSQL(sql, new Object[] { moduleId });
	}
    
   /* public List<Course> findActiveModulesByUsername(String username, long programId) {
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";
		
		if (month > 6) {
			sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL and c.acadYear = " + currentYear + "";
		}else{
			sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL and (c.acadYear = "+ year +" or c.acadYear = "+ currentYear +") ";
		}
		String sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL ";

		return findAllSQL(sql, new Object[] { username,programId });
	}
    */
    public Course findByModuleIdAndAcadYearForModule(String moduleId, String acadYear){
		String sql = "select distinct moduleId, moduleName from course where moduleId = ? and acadYear = ?";
		
		return findOneSQL(sql, new Object[]{moduleId,acadYear});
		
	}
    


	
	
	
	public List<Course> findModulesByUsername(String username, long programId) {
		String sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL ";

		return findAllSQL(sql, new Object[] { username,programId });
	}

public List<Course> findCoursesByModuleId(Long moduleId, String username,Integer acadYear) {
		String sql = " select c.id, c.courseName from course c, user_course uc  "
				+ " where c.id = uc.courseId and c.moduleId = ? and uc.username = ? and c.acadYear=? and c.active = 'Y' and uc.active='Y'" ;

		return findAllSQL(sql, new Object[] { moduleId, username,acadYear });
	}
	
public List<Course> findCoursesByModuleIdAndCampusId(Long moduleId, String username, Long campusId) {
		String sql = " select c.id, c.courseName from course c, user_course uc  "
				+ " where c.id = uc.courseId and c.moduleId = ? and uc.username = ? and campusId = ? and c.active = 'Y' and uc.active='Y'";

		return findAllSQL(sql, new Object[] { moduleId, username, campusId });
	}
	
	public List<Course> getAcadYearByModuleId(String moduleId, String username) {
		String sql = " select distinct(c.acadYear) from course c, user_course uc where moduleId=? and c.id = uc.courseId and uc.username = ?;";

		return findAllSQL(sql, new Object[] { moduleId , username});
	}
	
	public List<Course> findActiveModulesByUsername(String username, long programId) {
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";
		
		if (month > 6) {
			sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL and c.acadYear = " + currentYear + "";
		}else{
			sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL and (c.acadYear = "+ year +" or c.acadYear = "+ currentYear +") ";
		}
		/*String sql = " select distinct(c.moduleId), c.moduleName from course c, user_course uc, program p "
				+ "where  p.id=c.programId and c.id = uc.courseId and uc.username = ? and c.programId=? and c.moduleId is not NULL ";*/

		return findAllSQL(sql, new Object[] { username,programId });
	}
	public List<Course> findCoursesByAcadYear(String username, long programId, String acadYear){
        String sql = "SELECT distinct c.id, c.courseName, c.programId, c.acadSession FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
                                        + "  where uc.username = ?  and c.programId = ? "
                                        + " AND c.active = 'Y' and c.acadYear = ?"
                                        + " and uc.active = 'Y' ";
        return findAllSQL(sql, new Object[] { username,programId,acadYear });
        }
	
	public List<Course> findcourseDetailsByCourseIds(Set<String> courseIds) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("courseIds", courseIds);

        final String sql = "select distinct c.* from course c where c.id in (:courseIds)";

        return getNamedParameterJdbcTemplate().query(sql, params,
                    BeanPropertyRowMapper.newInstance(Course.class));
  }
	
  public List<Course> findCourseStatiticsByUsernameForApp(String username) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";
		
		if (month > 6) {
			sql = " SELECT distinct c.*, p.programName FROM course c INNER JOIN user_course uc on uc.courseId = c.id, program p "
					+ " where c.programId = p.id and uc.username = ? and c.acadYear = " + currentYear + "  AND c.active = 'Y' ";
		}else{
			sql = " SELECT distinct c.*, p.programName FROM course c INNER JOIN user_course uc on uc.courseId = c.id, program p "
					+ " where c.programId = p.id and uc.username = ? and (c.acadYear = "+ year +" or c.acadYear = "+ currentYear +") AND c.active = 'Y' ";
		}

		return findAllSQL(sql, new Object[] { username });
	}


	
	public List<Course> findCoursesByProgramIdAndAcadYear(Integer acadYear,Long programId){
	    String sql = "select distinct id,courseName from course where acadYear = ? and programId = ?";
	    return findAllSQL(sql, new Object[] { acadYear,programId });
	}
	
	
	public List<Course> findCoursesByProgramIdAndAcadYear(Integer acadYear,Long programId,Long campusId){
	    String sql = "select distinct id,courseName from course where acadYear = ? and programId = ? and campusId= ?";
	    return findAllSQL(sql, new Object[] { acadYear,programId, campusId });
	}

	public List<String> findAcadYearCode() {
		String sql="SELECT distinct acadYearCode FROM course WHERE active ='Y' and acadYearCode is not null";
		return listOfStringParameter(sql, new Object[] {});
	}
	
	public List<String> findAcadYearCodeForNS() {
		String sql="SELECT distinct acadYear FROM module WHERE  acadYear is not null";
		return listOfStringParameter(sql, new Object[] {});
	}
	
	public List<Course> findCoursesByProgramIdAndAcadYear(String acadYear,String programId,String campusId){
		ArrayList<Object> parameters = new ArrayList<Object>();
	    String sql = "select distinct id,courseName from course where acadYear = ? and programId = ?";
	    parameters.add(acadYear);
	    parameters.add(programId);
	    if(!campusId.equals("null")) {
	    	sql = sql + " and campusId = ?";
	    	  parameters.add(campusId);
	    }
	    return findAllSQL(sql, parameters.toArray());
//	    return findAllSQL(sql, new Object[] { acadYear,programId });
	}
	
	public List<Course> findAcadSessionForStudentMs() {
		
		String sql="select distinct acadSession,acadYear from course where active ='Y' ";
		
		 return findAllSQL(sql, new Object[] {});
	}

public List<Course> findByAdminActive(String programName) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		System.out.println("month --->" + month);
		if (month > 6) {
			sql = " SELECT distinct(c.id), c.* FROM course c  INNER JOIN user_course uc on uc.courseId = c.id  "
					+ " INNER JOIN program p on p.id= c.programId "
					+ " where uc.role = 'ROLE_FACULTY' and uc.active='Y' and  p.programName= ? "
					+ " AND c.active = 'Y' and (c.acadYear = "
					+ currentYear
					+ " or c.programId = 50003116) group by c.id ";
		} else {
			sql = " SELECT distinct(c.id), c.* FROM course c  INNER JOIN user_course uc on uc.courseId = c.id  "
					+ " INNER JOIN program p on p.id= c.programId "
					+ " where uc.role = 'ROLE_FACULTY' and uc.active='Y' and  p.programName= ? "
					+ " AND c.active = 'Y' and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year
					+ " or c.programId = 50003116) group by c.id  ";
		}
		return findAllSQL(sql, new Object[] { programName });
	}
	
	public List<Course> findByAdminActive() {
		
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		if (month > 6) {
			sql = "SELECT distinct(c.id), c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.role = 'ROLE_FACULTY' and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
					+ currentYear + " or c.programId = 50003116) group by c.id  ";
		} else {
			sql = "SELECT distinct(c.id), c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
					+ "  where uc.role = 'ROLE_FACULTY' and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
					+ currentYear
					+ " or c.acadYear = "
					+ year
					+ " or c.programId = 50003116) group by c.id  ";
		}

		return findAllSQL(sql, new Object[] { });
	}
	
	public List<Course> findCoursesByProgramId(Long programId) {
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";
		if (month > 6) {
			sql = "SELECT  c.* FROM course c  where c.programId=? and c.acadYear = " + currentYear + " AND c.active = 'Y' order by courseName";
		}else{
			sql = " SELECT  c.* FROM course c  where c.programId=? and (c.acadYear = "+ year +" or c.acadYear = "+ currentYear +") AND c.active = 'Y' order by courseName";
		}
		return findAllSQL(sql, new Object[] { programId });
		
	}
	
	
	
	// Coursera
	
	public List<Course> moduleListByAcadYearAndCampusCE(
			String acadYear, String campusId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			/*String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where c.acadSession=? and c.acadYear = ? and c.campusId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.moduleId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId group by c.moduleId";
			*/
			
			// acad year code
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where  c.acadYearCode = ? and c.campusId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.moduleId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId and (c.moduleCategoryName ='Coursera' or c.moduleCategoryCode='IBM') group by c.moduleId";

			return findAllSQL(sql, new Object[] {  acadYear,
					campusId });
		} else {
		/*	String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName"
					+ " from course c,program p where c.acadSession=? and c.acadYear = ?    "
					+ " and c.programId=p.id and c.active = 'Y'  and c.moduleId is not null group by c.moduleId";

*/
			//acad Year Code
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName"
					+ " from course c,program p where  c.acadYearCode = ?    "
					+ " and c.programId=p.id and c.active = 'Y' and (c.moduleCategoryName ='Coursera' or c.moduleCategoryCode='IBM') and c.moduleId is not null group by c.moduleId";

			
			return findAllSQL(sql, new Object[] {  acadYear });
		}

	}

public List<Course> acadSessionListByAcadYearAndCampusCE(String acadYear,
			String campusId,String moduleId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			String sql = " select distinct acadSession " + " from "
					+ getTableName()
					+ " where acadYearCode = ? and campusId = ? "
					+ " and moduleId = ? and active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear, campusId,moduleId });
		} else {
			String sql = " select distinct acadSession " + " from "
					+ getTableName() + " where acadYearCode = ? and moduleId = ? and active = 'Y' ";

			return findAllSQL(sql, new Object[] { acadYear,moduleId });
		}

	}
	
	//29-05-2020
	
	public List<Course> findByCoursesBasedOnProgramNameAndYearAndcreatedBy(String progName,
			Integer acadYear,String createdBy) {
	
		String sql = "select c.* from course c, program p where c.programId = p.id and p.programName= ? and c.active='Y' and (c.acadYear = ? or c.acadYearCode = ?) and (c.createdBy = ? or c.createdBy = 'CA')";
	
		return findAllSQL(sql, new Object[] { progName, acadYear , acadYear, createdBy });
	}

	public List<Course> findByCoursesBasedOnProgramNameSupportAdmin() {
		
		String sql = " SELECT DISTINCT c.courseName,c.id FROM course c, program p WHERE c.programId = p.id AND c.active='Y' ";
		
		return findAllSQL(sql, new Object[] {});
	}

	public List<String> courseListByParams(String moduleId,String acadYear,String programId) {
		String sql = " select distinct id from "+getTableName()+" where acadYear=? and moduleId=? and programId=? and active = 'Y' ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] {acadYear,moduleId,programId});
	}
	
	public List<String> courseListByParams(String moduleId,String acadYear,String programId,String username) {
		String sql = " select distinct c.id from "+getTableName()+" c,user_course uc where c.id=uc.courseId "
				+ "  and c.acadYear=? and c.moduleId=? and c.programId=? and uc.username=? and uc.active = 'Y' and c.active ='Y' ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] {acadYear,moduleId,programId,username});
	}
	//acadYear changes by Hiren 02-02-2021
	public List<Course> moduleListByAcadYearAndCampusForTest(
			String acadYear, String campusId,String programId) {

		if (!"null".equalsIgnoreCase(campusId) && !"undefined".equals(campusId)) {
			/*String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where c.acadSession=? and c.acadYear = ? and c.campusId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.moduleId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId group by c.moduleId";
			*/
			
			// acad year code
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where  c.acadYear = ? and c.campusId=? and c.programId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.moduleId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId group by c.moduleId";

			return findAllSQL(sql, new Object[] {  acadYear,
					campusId,programId });
		} else {
		/*	String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName"
					+ " from course c,program p where c.acadSession=? and c.acadYear = ?    "
					+ " and c.programId=p.id and c.active = 'Y'  and c.moduleId is not null group by c.moduleId";

*/
			//acad Year Code
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName"
					+ " from course c,program p where  c.acadYear = ?  and c.programId=?  "
					+ " and c.programId=p.id and c.active = 'Y'  and c.moduleId is not null group by c.moduleId";

			
			
			return findAllSQL(sql, new Object[] { acadYear,programId });
		}

	}
	//acadYear change by Hiren 02-02-2021
	public List<Course> findCoursesByModuleIdAcadYearAndProgram(Long moduleId, String acadYear,String programId) {
		String sql = " select c.id, c.courseName from course c  "
				+ " where   c.moduleId = ?  and c.acadYear = ? and c.programId = ? and c.active='Y'";

		return findAllSQL(sql, new Object[] { moduleId, acadYear, programId});
	}
	//acadYear change by Hiren 02-02-2021
   public String getAcadMonthByModuleIdAndAcadYearAndProgram(String moduleId, String acadYear,String programId)
   	{
   		String sql=" SELECT DISTINCT acadMonth FROM course WHERE moduleId = ? AND acadYear = ? and programId =? "
   				+ " and acadMonth is not null limit 1";
   		
   		 //findOneSQL(sql, new Object[]{moduleid, acadYear});
   		
   		 return getJdbcTemplate().queryForObject(sql, String.class,new Object[]{moduleId,acadYear,programId});
   		}

   public List<Course> getModulesForTest(String username,String programId){
	   
	   String sql =" select c.* from course c,test t where c.moduleId=t.moduleId and c.programId =? "
	   		+ " and t.facultyId = ? and c.active='Y' and t.active ='Y' group by c.moduleId,c.acadYear ";
	   
	   return findAllSQL(sql, new Object[] {programId,username});
   }

	public List<Course> getCourseByProgramId(String programIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> programIdList = Arrays.asList(programIds.split(","));
		if(programIds.contains(",")) {
			params.put("programIds",programIdList);
		}else
		{
			params.put("programIds",programIds);
		}
		String sql=" SELECT DISTINCT  id, courseName, acadYearCode FROM course WHERE programId in(:programIds) ";
		return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(Course.class));
	}
	
	
	public List<Course> findByCoursesBasedOnYearAndcreatedBy(
			Integer acadYear,String createdBy) {
	
		String sql = "select c.* from course c, program p where c.programId = p.id "
				+ " and c.active='Y' and (c.acadYear = ? or c.acadYearCode = ?) and (c.createdBy = ? or c.createdBy = 'CA')";
	
		return findAllSQL(sql, new Object[] { acadYear , acadYear, createdBy });
	}
	public List<Course> findCoursesForAttd(String username) {
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);
		String sql = "";

		sql = "SELECT c.* FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
		+ "  where uc.username = ? and uc.active='Y' AND c.active = 'Y'  and (c.acadYear = "
		+ currentYear
		+ " or c.acadYear = "
		+ year
		+ " or c.programId = 50003116)  ";

		return findAllSQL(sql, new Object[] { username });
	}

	public Course findAcadYear(String acadYear) {
		String sql = "select acadYear from course where acadYear=? limit 1";
//		return getJdbcTemplate().queryForObject(sql, new Object[] {acadYear}, String.class);
		return findOneSQL(sql, new Object[] {acadYear});
	}
	
	public Course checkIfExistsInDB(String columnName, String value) {
		String sql = "";
		if(columnName.equals("acadYear")) {
			sql = "select * from course where acadYear=? limit 1";
		} else 
		if(columnName.equals("campusId")) {
			sql = "select * from course where campusId=? limit 1";
		} else
		if(columnName.equals("acadSession")) {
			sql = "select * from course where acadSession=? limit 1";
		} else
		if(columnName.equals("moduleId")) {
			sql = "select * from course where moduleId=? limit 1";
		} else
		if(columnName.equals("programId")) {
			sql = "select * from course where programId=? limit 1";
		}
		return findOneSQL(sql, new Object[] {value});
	}

	//Peter 25/10/2021
	public Course checkIfAcadSessionExists(String acadSession) {
		String sql="SELECT sapSessionText FROM session_master WHERE sapSessionText=?";
		return findOneSQL(sql, new Object[] {acadSession});
	}

	//Peter 25/10/2021
	public Course checkIfModuleExists(String moduleId) {
		String sql="SELECT module_id FROM module WHERE module_id=?";
		return findOneSQL(sql, new Object[] {moduleId});
	}

	//Peter 25/10/2021
	public Course checkIfCampusExists(String campusId) {
		String sql="SELECT campusId FROM program_campus WHERE campusId=?";
		return findOneSQL(sql, new Object[] {campusId});
	}
}


