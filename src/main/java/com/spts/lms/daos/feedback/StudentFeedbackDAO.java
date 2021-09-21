package com.spts.lms.daos.feedback;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.web.utils.Utils;

@Repository("studentFeedbackDAO")
public class StudentFeedbackDAO extends BaseDAO<StudentFeedback> {

	@Override
	protected String getTableName() {
		return "student_feedback";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO student_feedback(acadYear,acadMonth,courseId,startDate,endDate,"
				+ "allowAfterEndDate,mandatory,username,feedbackId,facultyId,feedbackCompleted,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,comments)"
				+ " VALUES(:acadYear,:acadMonth,"
				+ ":courseId,:startDate,:endDate,COALESCE(:allowAfterEndDate,'N'),COALESCE(:mandatory, 'N'),:username,:feedbackId,:facultyId,COALESCE(:feedbackCompleted,'N'),"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:comments) "
				+ " ON DUPLICATE KEY UPDATE "
				+ "feedbackCompleted = COALESCE(:feedbackCompleted, feedbackCompleted),lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate,comments =:comments";
	}

	@Override
	protected String getUpsertSql() {
		return "INSERT INTO student_feedback(id, acadYear,acadMonth,courseId,startDate,endDate,"
				+ "allowAfterEndDate,mandatory,username,feedbackId,facultyId,feedbackCompleted,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,comments)"
				+ " VALUES(:id, :acadYear,:acadMonth,"
				+ ":courseId,:startDate,:endDate,COALESCE(:allowAfterEndDate,'N'),COALESCE(:mandatory, 'N'),:username,:feedbackId,:facultyId,COALESCE(:feedbackCompleted,'N'),"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,comments) "
				+ " ON DUPLICATE KEY UPDATE "
				+ "feedbackCompleted = COALESCE(:feedbackCompleted, feedbackCompleted),lastModifiedBy = :lastModifiedBy, lastModifiedDate = :lastModifiedDate, comments =:comments";
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	public List<StudentFeedback> findByfeedbackIDAndUsername(Long id,
			String username) {
		final String sql = "Select * from " + getTableName()
				+ " where feedbackId = ? and username = ?";
		return findAllSQL(sql, new Object[] { id, username });
	}

	public List<StudentFeedback> getstudentFeedbackCompleteListByInputParam(
			String fromDate, String toDate, String facultyId) {

		String sql = " select distinct sf.*,concat(u.firstname,' ',u.lastname) as facultyName,(case when  u.type = 'H' then 'Visiting' "
				+ "  else 'Core' end) as type, "
				+ " p.programName as programName,c.acadYear as acadYear,c.acadSession as acadSession,c.courseName as courseNameforFeedback "
				+ " from student_feedback sf,users u,course c,program p  "
				+ " where sf.endDate between ? and ? "
				+ " and sf.facultyId=? and sf.feedbackCompleted='Y' "
				+ "  and sf.feedbackCompleted='Y' and sf.courseId=c.id and "
				+ " c.programId=p.id and sf.facultyId=u.username and u.active='Y' and u.enabled = 1 and c.active='Y' group by sf.courseId,sf.username";

		return findAllSQL(sql, new Object[] { fromDate, toDate, facultyId });
	}

	public List<StudentFeedback> getStudentFeedbackAllocationStatus(
			String feedbackId) {
		String sql = "select distinct feedbackId from " + getTableName()
				+ " where feedbackId = ?";
		return findAllSQL(sql, new Object[] { feedbackId });
	}

	public List<StudentFeedback> findStudentFeedbacksByCourseAndFaculty(
			String courseId, String facultyId) {

		String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
				+ getTableName()
				+ " sf, users u ,course c , program p   where sf.courseId = ? and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? and u.active ='Y' and u.enabled = 1 and c.active ='Y' and sf.facultyId=u.username ";
		return findAllSQL(sql, new Object[] { courseId, facultyId });
	}

	public void removeStudent_Feedback(String id) {
		executeUpdateSql(
				"update student_feedback set active = 'N'  where id=?",
				new Object[] { id });

	}

	public List<StudentFeedback> getAllocatedStudentFeedback(String feedbackId) {
		String sql = "select  * from " + getTableName()
				+ " where feedbackId =? ";
		return findAllSQL(sql, new Object[] { feedbackId });
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWise(
			List<String> programId, List<String> acadSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);

		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and p.id in(:programIds) and c.acadSession in(:acadSessions) ";
		// return findAllSQL(sql, new Object[] {});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	/*
	 * public List<StudentFeedback>
	 * getstudentFeedbackListCourseWise(List<String> programId) {
	 * 
	 * Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("programIds", programId);
	 * 
	 * final String sql =
	 * " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
	 * +
	 * "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
	 * + "  from student_feedback sf, feedback f, users u , course c,program p "
	 * + " where courseId in (select distinct id from course " +
	 * "	where programId in( select distinct id from program where active='Y') AND active='Y') "
	 * +
	 * " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and p.id in(:programIds) "
	 * ; //return findAllSQL(sql, new Object[] {}); return
	 * getNamedParameterJdbcTemplate().query(sql, params,
	 * BeanPropertyRowMapper.newInstance(StudentFeedback.class)); }
	 */

	/*
	 * public List<StudentFeedback> getstudentFeedbackListCourseWise() {
	 * 
	 * final String sql =
	 * " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
	 * +
	 * "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
	 * + "  from student_feedback sf, feedback f, users u , course c,program p "
	 * + " where courseId in (select distinct id from course " +
	 * "	where programId in( select distinct id from program where active='Y') AND active='Y') "
	 * +
	 * " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
	 * ; return findAllSQL(sql, new Object[] {});
	 * 
	 * }
	 */

	/*
	 * public List<StudentFeedback> getStudentFeedbackResponseList( String
	 * feedbackId) {
	 * 
	 * 
	 * final String sql =
	 * " select sf.*,c.courseName,c.acadYear,concat(u.firstname,' ',u.lastname) as 'studentName'  from  course c,  student_feedback sf   "
	 * + " inner join users u on sf.username = u.username " +
	 * "	 where sf.feedbackId=? and c.id=sf.courseId and u.username=sf.username group by sf.feedbackId,sf.username "
	 * ;
	 * 
	 * return findAllSQL(sql, new Object[] { feedbackId }); }
	 */

	public List<StudentFeedback> checkFeedbackValidity(Long feedbackId,
			String username) {

		Date dt = Utils.getInIST();

		final String sql = " select * from student_feedback where startDate < ? and  endDate > ? and feedbackId =? and username =? ";

		return findAllSQL(sql, new Object[] { dt, dt, feedbackId, username });

	}

	public List<StudentFeedback> getstudentFeedbackCompleteListByInputParam(

	String fromDate, String toDate) {

		String sql = " select distinct sf.*,concat(u.firstname,' ',u.lastname) as facultyName,(case when  u.type = 'H' then 'Visiting' "

				+ "  else 'Core' end) as type, "

				+ " p.programName as programName,c.acadYear as acadYear,c.acadSession as acadSession,c.courseName as courseNameforFeedback "

				+ " from student_feedback sf,users u,course c,program p  "

				+ " where sf.endDate between ? and ? "

				+ "  and sf.feedbackCompleted='Y' "

				+ "  and sf.feedbackCompleted='Y' and sf.courseId=c.id and "

				+ " c.programId=p.id and sf.facultyId=u.username and u.active='Y' and u.enabled = 1 group by sf.courseId,sf.username";

		return findAllSQL(sql, new Object[] { fromDate, toDate });

	}

	public List<StudentFeedback> getstudentFeedbackListUserWise(
			String feedbackId, List<String> username) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", feedbackId);
		params.put("usernames", username);

		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ " (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty ,c.acadSession as acadSession "
				+ " from student_feedback sf, feedback f, users u , course c,program p "
				+ " where sf.username in (:usernames) "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and u.active ='Y' and u.enabled = 1 and sf.feedbackId=:id ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgram(
			String acadSession, List<String> programId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and c.acadSession = :acadSessions and p.id in(:programIds) ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgram(
			String acadSession, List<String> programId, String acadYear) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
			String acadSession, List<String> programId, String acadYear,
			String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYear(
			String acadSession) {

		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active = 'Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and c.active ='Y' and u.active='Y' and u.enabled = 1 and c.acadSession = ? ";
		return findAllSQL(sql, new Object[] { acadSession });

	}

	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyAndStudentList(
			String courseId, String facultyId, List<String> usernameList) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", courseId);
		params.put("facultyId", facultyId);
		params.put("usernames", usernameList);

		String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
				+ getTableName()
				+ " sf, users u ,course c , program p   where sf.courseId = :id and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = :facultyId and sf.facultyId=u.username "
				+ " and c.active='Y' and u.active='Y' and u.enabled = 1 and sf.username in(:usernames)";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	/*
	 * public List<StudentFeedback> getStudentFeedbackResponseList(
	 * 
	 * String feedbackId) {
	 * 
	 * 
	 * final String sql = "select * from student_feedback sf "
	 * 
	 * + " inner join users u on sf.username = u.username"
	 * 
	 * + " where sf.feedbackId=? group by sf.feedbackId,sf.username";
	 * 
	 * 
	 * final String sql =
	 * "select sf.*,c.courseName,c.acadYear,u.email,u.mobile,c.acadSession,concat(u.firstname,' ',u.lastname) as 'studentName'  from  course c,  student_feedback sf   "
	 * 
	 * + " inner join users u on sf.username = u.username "
	 * 
	 * +
	 * "          where sf.feedbackId=? and sf.active='Y' and c.id=sf.courseId and u.username=sf.username group by sf.feedbackId,sf.username,sf.courseId,sf.facultyId"
	 * ;
	 * 
	 * return findAllSQL(sql, new Object[] { feedbackId });
	 * 
	 * }
	 */

	public List<StudentFeedback> getStudentFeedbackResponseList(

	String feedbackId) {

		/*
		 * final String sql =
		 * "select sf.*,c.courseName,c.acadYear,u.email,u.mobile,c.acadSession,concat(u.firstname,' ',u.lastname) as 'studentName'  from  course c,  student_feedback sf   "
		 * 
		 * + " inner join users u on sf.username = u.username "
		 * 
		 * +
		 * "          where sf.feedbackId=? and sf.active='Y' and c.id=sf.courseId and u.username=sf.username group by sf.feedbackId,sf.username,sf.courseId,sf.facultyId"
		 * ;
		 */
		final String sql = " select sf.*,c.courseName,c.acadYear,u.email,u.mobile,c.acadSession, "
				+ " concat(u.firstname,' ',u.lastname) as 'studentName' "
				+ " from  users u, course c, student_feedback sf, user_course uc "
				+ " where sf.username = u.username and sf.username = uc.username "
				+ " and sf.courseId = uc.courseId and sf.feedbackId=? and sf.active='Y' "
				+ " and c.id=sf.courseId  and uc.active = 'Y' "
				+ " order by sf.feedbackId,sf.username,sf.courseId,sf.facultyId ";
		return findAllSQL(sql, new Object[] { feedbackId });

	}

	public Page<StudentFeedback> getStudentForFeedback(String feedbackId,
			String facultyId, Long courseId, String acadMonth,
			Integer acadYear, int pageNo, int pageSize) {
		final String sql = "select uc.username, u.firstname, u.lastname, sfb.feedbackId, sfb.id "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		left outer join student_feedback sfb on sfb.courseId = uc.courseId and sfb.acadMonth = uc.acadMonth and sfb.acadYear = uc.acadYear and sfb.username = u.username and sfb.facultyId = ? and sfb.feedbackId = ? "

				+ " where"
				+ "  uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and u.active ='Y' and u.enabled = 1 and uc.active ='Y' and uc.acadYear = ? order by sfb.id asc ";

		final String countsql = " select count(*) "
				+ " from user_course uc "
				+ "		inner join users u on uc.username = u.username"
				+ "		left outer join student_feedback sfb on sfb.courseId = uc.courseId and sfb.acadMonth = uc.acadMonth and sfb.acadYear = uc.acadYear and sfb.username = u.username and sfb.facultyId = ? and sfb.feedbackId = ? "

				+ " where"
				+ " f.id = ? uc.role = ? and uc.courseId = ? and uc.acadMonth = ?  and u.active ='Y' and u.enabled = 1 and uc.active ='Y' and uc.acadYear = ? order by sfb.id asc ";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { facultyId, feedbackId, Role.ROLE_STUDENT.name(),
						courseId, acadMonth, acadYear }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	public List<StudentFeedback> findFeedbackCompletedStatus(String username,
			String feedbackId) {
		final String sql = "select distinct feedbackCompleted from "
				+ getTableName()
				+ " where username=? and feedbackId=?  and active = 'Y' group by feedbackId ,username,feedbackCompleted";
		return findAllSQL(sql, new Object[] { username, feedbackId });
	}

	public StudentFeedback findnoofStudentsWhoTookFeedbackList(Long cousreId) {

		String sql = "select count(distinct(sf.username)) as noOfStudentsFeedback , c.id as courseId from student_feedback sf,course c where "
				+ "c.id = ? and sf.courseId=c.id and c.active='Y' and sf.feedbackCompleted='Y' ";

		return findOneSQL(sql, new Object[] { cousreId });

	}

	public List<StudentFeedback> findByfeedbackAllocatedWithCourseName(Long id,
			String username) {
//		final String sql = " Select CONCAT(COALESCE(u.firstname,' '), ' ' ,COALESCE(u.lastname,' ')) as 'facultyName' ,sf.*,c.courseName "
//				+ " from student_feedback sf,course c , users u where  c.id=sf.courseId and  sf.feedbackId = ? and "
//				+ " sf.username =? and sf.active='Y' and u.active='Y' and u.enabled = 1 and c.active ='Y' and u.username = sf.facultyId ";
//		
		final String sql = " Select CONCAT(COALESCE(u.firstname,' '), ' ' ,COALESCE(u.lastname,' ')) as 'facultyName' ,sf.*,c.courseName "
				+ " from student_feedback sf,course c , users u where  c.id=sf.courseId and  sf.feedbackId = ? and "
				+ " sf.username =? and sf.active='Y'  and c.active ='Y' and u.username = sf.facultyId ";
		
		return findAllSQL(sql, new Object[] { id, username });

	}

	/*
	 * public List<StudentFeedback> getStudentsByProgram(String programId,
	 * Integer acadYear) { final String sql =
	 * "select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
	 * +
	 * " from course c ,user_course uc ,user_course auc, users u, program p where c.id = uc.courseId and uc.username = u.username and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT' and auc.role='ROLE_FACULTY' and auc.active='Y' and uc.active='Y' and u.active='Y' and u.enabled = 1 and  c.programId= p.id and p.id=? and c.acadYear = ?  "
	 * ;
	 * 
	 * return findAllSQL(sql, new Object[] { programId, acadYear }); }
	 */

	public List<StudentFeedback> getStudentsByProgram(String programId,
			Integer acadYear, String acadMonth) {
		final String sql = "select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
				+ " from course c ,user_course uc ,user_course auc, users u, program p where c.id = uc.courseId "
				+ " and uc.username = u.username and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT'"
				+ "  and auc.role='ROLE_FACULTY' and auc.active='Y' and uc.active='Y' and u.active='Y' "
				+ " and u.enabled = 1 and  c.programId= p.id and p.id=? and c.acadYear = ? and c.acadMonth = ?  ";

		return findAllSQL(sql, new Object[] { programId, acadYear, acadMonth });
	}

	/*
	 * public List<StudentFeedback> getStudentsByProgramAndCampus( String
	 * programId, String campusId, Integer acadYear) { final String sql =
	 * "select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
	 * +
	 * " from course c ,user_course uc ,user_course auc, users u, program p where c.id = uc.courseId and uc.username = u.username and auc.courseId =uc.courseId  "
	 * +
	 * " and uc.role ='ROLE_STUDENT' and auc.role='ROLE_FACULTY' and auc.active='Y' and uc.active='Y' and u.active='Y' and u.enabled = 1 and  "
	 * + " c.programId= p.id and p.id=? and u.campusId = ? and c.acadYear = ? ";
	 * 
	 * return findAllSQL(sql, new Object[] { programId, campusId, acadYear }); }
	 */

	public List<StudentFeedback> getStudentsByProgramAndCampus(
			String programId, String campusId, Integer acadYear,
			String acadMonth) {
		final String sql = "select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
				+ " from course c ,user_course uc ,user_course auc, users u, program p where c.id = uc.courseId and uc.username = u.username and auc.courseId =uc.courseId  "
				+ " and uc.role ='ROLE_STUDENT' and auc.role='ROLE_FACULTY' and auc.active='Y' and uc.active='Y' and u.active='Y' and u.enabled = 1 and  "
				+ " c.programId= p.id and p.id=? and u.campusId = ? and c.acadYear = ? and c.acadMonth = ? ";

		return findAllSQL(sql, new Object[] { programId, campusId, acadYear,
				acadMonth });
	}

	public Page<StudentFeedback> getStudentForFeedback(String feedbackId,
			String facultyId, Long courseId, int pageNo, int pageSize) {
		final String sql = "select uc.username, u.firstname, u.lastname, sfb.feedbackId, sfb.id "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username inner join course c on uc.courseId = c.id"
				+ "		left outer join student_feedback sfb on sfb.courseId = uc.courseId and sfb.acadMonth = uc.acadMonth and sfb.acadYear = uc.acadYear and sfb.username = u.username and sfb.facultyId = ? and sfb.feedbackId = ? "

				+ " where"
				+ "  uc.role = ? and uc.courseId = ? and c.active='Y' and u.active='Y' and u.enabled = 1 and uc.active='Y' order by sfb.id asc ";

		final String countsql = " select count(*) "
				+ " from user_course uc "
				+ "		inner join users u on uc.username = u.username inner join course c on uc.courseId = c.id "
				+ "		left outer join student_feedback sfb on sfb.courseId = uc.courseId and sfb.acadMonth = uc.acadMonth and sfb.acadYear = uc.acadYear and sfb.username = u.username and sfb.facultyId = ? and sfb.feedbackId = ? "
				+ " where"
				+ "uc.role = ? and uc.courseId = ? and c.active='Y' and u.active='Y' and u.enabled = 1 and uc.active='Y' order by sfb.id asc ";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { facultyId, feedbackId, Role.ROLE_STUDENT.name(),
						courseId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	public Page<StudentFeedback> getStudentFeedbackResponse(String feedbackId,
			int pageNo, int pageSize) {
		final String sql = "select * from student_feedback sf "
				+ " inner join users u on sf.username = u.username"
				+ " where sf.feedbackId=? and u.active='Y' and u.enabled = 1 group by sf.feedbackId,sf.username";

		final String countsql = "select count(*) from student_feedback sf "
				+ " inner join users u on sf.username = u.username "
				+ " where sf.feedbackId=? and u.active='Y' and u.enabled = 1 ";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { feedbackId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	public Page<StudentFeedback> getStudentFeedbackResponseByUsername(
			String username, int pageNo, int pageSize) {
		final String sql = "select * from student_feedback sf "
				+ " inner join users u on sf.username = u.username "
				+ " inner join feedback f on sf.feedbackId = f.id "
				+ " where sf.username=? and f.active ='Y' and  u.active='Y' and u.enabled = 1 and sf.active='Y' group by feedbackID order by f.createdDate desc  ";

		final String countsql = "select count(distinct(sf.feedbackId)) from student_feedback sf "
				+ " inner join users u on sf.username = u.username "
				+ " inner join feedback f on sf.feedbackId = f.id "
				+ " where sf.username=? and f.active ='Y' and sf.active='Y'  and u.active ='Y' and u.enabled = 1";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { username }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	public List<StudentFeedback> getNoOfStudentAllocatedFeedback(
			String courseId, String feedbackId) {
		String sql = " select * from student_feedback where courseId = ? and feedbackId = ?";
		return findAllSQL(sql, new Object[] { courseId, feedbackId });
	}

	public List<StudentFeedback> findStudentFeedbackByFaculty(String facultyId) {
		// String sql =
		// " select distinct courseId,username,feedbackId from student_feedback where facultyId=? and feedbackCompleted='Y' order by courseId desc ";
		String sql = " select distinct courseId,feedbackId from student_feedback where facultyId =? and feedbackCompleted='Y' "
				+ " group by courseId,feedbackId order by courseId desc ";

		return findAllSQL(sql, new Object[] { facultyId });
	}

	public List<StudentFeedback> getStudentGaveFeedback(String courseId,
			String feedbackId) {
		String sql = "select * from student_feedback where feedbackCompleted='Y' and courseId = ? and feedbackId = ?";
		return findAllSQL(sql, new Object[] { courseId, feedbackId });
	}

	public List<StudentFeedback> findFeedbackAllocatedToCourse(Long courseId) {
		String sql = " select distinct feedbackId from student_feedback where courseId = ? group by courseId,feedbackId ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public Page<StudentFeedback> viewStudentFeedback(String feedbackId,
			String acadMonth, Integer acadYear, int pageNo, int pageSize) {
		final String sql = "select * from student_feedback sf  "
				+ " inner join user_course uc on sf.username = uc.username "
				+ " where uc.courseId in(select courseId from student_feedback where feedbackId =?) and uc.acadMonth=? and uc.acadYear=? and sf.feedbackId=? and sf.feedbackCompleted='Y'  and uc.active='Y' ";

		final String countsql = "select count(*) from student_feedback sf  "
				+ " inner join user_course uc on sf.username = uc.username "
				+ " inner join users u on uc.username = u.username "
				+ " where uc.courseId in(select courseId from student_feedback where feedbackId =?) and uc.acadMonth=? and uc.acadYear=? and sf.feedbackId=? and sf.feedbackCompleted='Y'  and uc.active='Y' ";
		;

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { feedbackId, acadMonth, acadYear, feedbackId },
				pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

	public List<StudentFeedback> getStudentsForFeedback(String facultyId,
			Long courseId, String acadMonth, Integer acadYear) {
		final String sql = "select uc.username, u.firstname, u.lastname, sfb.feedbackId, sfb.id "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ " inner join users u on uc.username = u.username "
				+ "		left outer join student_feedback sfb on sfb.courseId = uc.courseId and sfb.acadMonth = uc.acadMonth and sfb.acadYear = uc.acadYear and sfb.username = u.username and sfb.facultyId = ? "
				+ " where"
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and u.active ='Y' and u.enabled = 1 and uc.active ='Y' and uc.acadYear = ? order by sfb.id asc ";
		return findAllSQL(sql,
				new Object[] { facultyId, Role.ROLE_STUDENT.name(), courseId,
						acadMonth, acadYear });
	}

	public List<StudentFeedback> getPendingFeedbackByStudent(String username) {
		Date dt = Utils.getInIST();
		final String sql = "select sfb.*, c.courseName, u.firstname, u.lastname from "
				+ getTableName()
				+ " sfb inner join course c on c.id = sfb.courseId inner join users u on u.username = sfb.facultyId "
				+ "where sfb.username = ? and sfb.feedbackCompleted = 'N' and sfb.mandatory = 'Y' and u.active ='Y' and u.enabled = 1 and sfb.startDate <= ? and sfb.endDate >= ?";
		return findAllSQL(sql, new Object[] { username, dt, dt });
	}

	public List<StudentFeedback> getStudentFeedbackStatus(String feedbackId,
			String username) {
		final String sql = "select feedbackCompleted from student_feedback where username=? and feedBackId=?";
		return findAllSQL(sql, new Object[] { username, feedbackId });
	}

	public List<StudentFeedback> searchAllFeedback(Long courseId, String status) {

		String sql = "select sf.*, f.* from feedback f"
				+ " inner join student_feedback sf on sf.feedbackId = f.id "
				+ " where sf.courseId = ? and sf.feedbackCompleted = ? and f.active = 'Y'";
		return findAllSQL(sql, new Object[] { courseId, status });
	}

	public StudentFeedback findByCourseIdAndFeedback(String feedbackId,
			String username, String courseId, String studentFeedbackId) {
		final String sql = "select *  from student_feedback where username=? and feedBackId=? and courseId=? and id = ?";
		return findOneSQL(sql, new Object[] { username, feedbackId, courseId,
				studentFeedbackId });
	}

	public List<StudentFeedback> getStudentsByAcadSession(String acadSession) {
		final String sql = " select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
				+ " from course c ,user_course uc ,user_course auc, users u where c.id = uc.courseId and uc.username = u.username "
				+ " and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT' and auc.role='ROLE_FACULTY' and uc.active ='Y' and u.active ='Y' and u.enabled = 1 and uc.acadSession= ? ";
		return findAllSQL(sql, new Object[] { acadSession });
	}

	/*
	 * public List<StudentFeedback> getStudentsByAcadSession(String programId,
	 * String acadSession, Integer acadYear) { final String sql =
	 * " select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
	 * +
	 * " from course c ,user_course uc ,user_course auc, users u,program p where c.id = uc.courseId and uc.username = u.username "
	 * +
	 * " and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT' and  c.acadYear=uc.acadYear and c.acadMonth=uc.acadMonth and  "
	 * +
	 * " c.programId=p.id and p.id= ? and auc.role='ROLE_FACULTY' AND auc.active = 'Y' and uc.active ='Y' and u.active ='Y' and u.enabled = 1 and uc.acadSession= ? and uc.acadYear = ?"
	 * ; return findAllSQL(sql, new Object[] { programId, acadSession, acadYear
	 * }); }
	 */

	public List<StudentFeedback> getStudentsByAcadSession(String programId,
			String acadSession, Integer acadYear, String acadMonth) {
		final String sql = " select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
				+ " from course c ,user_course uc ,user_course auc, users u,program p where c.id = uc.courseId and uc.username = u.username "
				+ " and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT' and  c.acadYear=uc.acadYear and c.acadMonth=uc.acadMonth and  "
				+ " c.programId=p.id and p.id= ? and auc.role='ROLE_FACULTY' AND auc.active = 'Y' and uc.active ='Y' and u.active ='Y' and u.enabled = 1 and uc.acadSession= ? "
				+ " and uc.acadYear = ? and uc.acadMonth = ? and (c.moduleCategoryName <> 'Coursera' or c.moduleCategoryName is NULL) ";
		return findAllSQL(sql, new Object[] { programId, acadSession, acadYear,
				acadMonth });
	}

	public List<StudentFeedback> findFeedbaclsBySessionAndYearForProgram(

	String acadSession, Integer acadYear, Long programId) {

		String sql = "select a.* from student_feedback a "

		+ " inner join course c on c.id = a.courseId "

		+ " inner join program p on p.id = c.programId "

		+ " where c.acadSession = ? and c.acadYear = ? and programId = ? ";

		return findAllSQL(sql,

		new Object[] { acadSession, acadYear, programId });

	}

	public List<StudentFeedback> findFeedbaclsBySessionAndYearForCollege(

	String acadSession, Integer acadYear) {

		String sql = "select a.* from student_feedback a "

		+ " inner join course c on c.id = a.courseId "

		+ " inner join program p on p.id = c.programId "

		+ " where c.acadSession = ? and c.acadYear = ? ";

		return findAllSQL(sql, new Object[] { acadSession, acadYear });

	}

	public List<StudentFeedback> findAllFeedbackForProgram(Long programId) {

		String sql = "select a.* from student_feedback a,course c , program p where a.courseId= c.id and c.programId = p.id and "

				+ " p.id=? order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { programId });

	}

	public List<StudentFeedback> getStudentFeedbacksByCourse(String programId,
			String feedbackId, String acadYear, String acadSession,
			String acadMonth) {
		String sql = "select   c.id as courseId, c.courseName,  sf.feedbackId "
				+ " from course c "

				+ " inner join program p on c.programId=p.id and p.id = ? "
				+ " inner join user_course uc on c.id=uc.courseId and uc.role='ROLE_FACULTY' "
				+ "	left outer join student_feedback sf on sf.courseId = c.id and sf.feedbackId =? "
				+ " WHERE  c.active ='Y' and c.programId=p.id  and c.acadYear = ? and c.acadSession = ? "
				+ " and c.acadMonth = ? and (c.moduleCategoryName <> 'Coursera' or c.moduleCategoryName is NULL) "
				+ " group by c.id,sf.feedbackId order by feedbackId desc";

		return findAllSQL(sql, new Object[] { programId, feedbackId, acadYear,
				acadSession, acadMonth });
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearForFaculty(
			String acadSession, String facultyId) {

		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active = 'Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and c.acadSession = ? and sf.facultyId = ?";
		return findAllSQL(sql, new Object[] { acadSession, facultyId });

	}

	public List<StudentFeedback> getstudentFeedbackListForFacultyAndForAllPrograms(
			String facultyId) {

		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active = 'Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? ";
		return findAllSQL(sql, new Object[] { facultyId });

	}

	public List<StudentFeedback> getstudentFeedbackListForAllPrograms() {

		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active = 'Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id  ";
		return findAllSQL(sql, new Object[] {});

	}

	public List<StudentFeedback> checkStudentFeedbackDuplicates(
			String feedbackId) {

		String sql = " select username,feedbackId,courseId,facultyId,count(*) as num from student_feedback where feedbackId =?  group by username,feedbackId,courseId,facultyId having num >1 ";

		return findAllSQL(sql, new Object[] { feedbackId });
	}

	public void deleteDuplicateStudentFeedback(String feedbackId) {

		String sql = " DELETE FROM student_feedback where id IN (SELECT * FROM (select  id from student_feedback where "
				+ "  feedbackId= ? group by username,feedbackId,courseId,facultyId) AS X) ";

		getJdbcTemplate().update(sql, new Object[] { feedbackId });
	}

	/*
	 * public List<StudentFeedback> getStudentsByAcadSessionAndCampus( String
	 * programId, String acadSession, Integer acadYear,String acadMonth, String
	 * campusId) { final String sql =
	 * " select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
	 * +
	 * " from course c ,user_course uc ,user_course auc, users u,program p where c.id = uc.courseId and uc.username = u.username "
	 * +
	 * " and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT' and  c.acadYear=uc.acadYear and c.acadMonth=uc.acadMonth and  "
	 * +
	 * " c.programId=p.id and p.id= ? and auc.role='ROLE_FACULTY' AND auc.active = 'Y' and uc.active ='Y' "
	 * +
	 * " and u.active ='Y' and u.enabled = 1 and uc.acadSession= ? and uc.acadYear = ? and uc.acadMonth = ? "
	 * + " and u.campusId = ? "; return findAllSQL(sql, new Object[] {
	 * programId, acadSession, acadYear,acadMonth, campusId }); }
	 */

	public List<StudentFeedback> getStudentsByAcadSessionAndCampus(
			String programId, String acadSession, Integer acadYear,
			String acadMonth, String campusId) {
		final String sql = " select uc.username, u.firstname, u.lastname,uc.acadMonth,uc.acadYear,uc.courseId,auc.username as facultyId "
				+ " from course c ,user_course uc ,user_course auc, users u,program p where c.id = uc.courseId and uc.username = u.username "
				+ " and auc.courseId =uc.courseId  and uc.role ='ROLE_STUDENT' and  c.acadYear=uc.acadYear and c.acadMonth=uc.acadMonth and  "
				+ " c.programId=p.id and p.id= ? and auc.role='ROLE_FACULTY' AND auc.active = 'Y' and uc.active ='Y' and u.active ='Y' "
				+ " and u.enabled = 1 and uc.acadSession= ? and uc.acadYear = ? and uc.acadMonth = ? "
				+ " and u.campusId = ? and (c.moduleCategoryName <> 'Coursera' or c.moduleCategoryName is NULL) ";
		return findAllSQL(sql, new Object[] { programId, acadSession, acadYear,
				acadMonth, campusId });
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFaculty(
			String acadSession, List<String> programId, String acadYear,
			String campusId, String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		params.put("facultyId", facultyId);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId and sf.facultyId=:facultyId";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramByFaculty(
			String acadSession, List<String> programId, String acadYear,
			String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("facultyId", facultyId);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId "
				+ " and c.programId=p.id and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear=:acadYear and sf.facultyId=:facultyId ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getStudentFeedbackStats(String campusId,
			String fromDate, String toDate) {

		String typeCondition = "";
		if (campusId != null) {
			typeCondition = " c.campusId = ? ";
		} else {
			typeCondition = " c.campusId is ? ";
		}

		String sql = "select sf.*, sfr.* from  student_feedback sf , student_feedback_response sfr, course c, users u where sf.id =sfr.studentFeedbackId and sf.courseId = c.id and sf.username = u.username and u.active = 'Y' and "
				+ typeCondition
				+ " and sfr.lastModifiedDate "
				+ " BETWEEN  ? and ? group by sfr.studentFeedbackId; ";
		return findAllSQL(sql, new Object[] { campusId, fromDate, toDate });
	}

	/*
	 * public void updateDates(StudentFeedback feedback) { executeUpdateSql(
	 * "Update " + getTableName() +
	 * " set startDate = :startDate, endDate = :endDate, lastModifiedBy = :lastModifiedBy "
	 * + " where feedbackId = :feedbackId ", new Object[] { feedback }); }
	 */

	public int updateDates(StudentFeedback feedback) {
		String sql = "Update "
				+ getTableName()
				+ " set startDate = :startDate, endDate = :endDate, lastModifiedBy = :lastModifiedBy "
				+ " where feedbackId = :feedbackId and feedbackCompleted = 'N' ";
		return updateSQL(feedback, sql);
	}

	public StudentFeedback getStudentFeedbackResponseByQuestionId(
			String feedbackId, String username, String courseId,
			String questionId, String facultyId) {

		String sql = " select distinct  sf.* ,sfr.answer,c.courseName as courseName,concat(u.firstname,' ',u.lastname) as facultyName,"
				+ " sf.courseId as courseId ,sf.id as stuFeedbackId from feedback f ,course c ,users u ,student_feedback sf,"
				+ " feedback_question fq LEFT OUTER JOIN student_feedback_response sfr ON sfr.feedbackQuestionId= fq.id "
				+ " and sfr.studentFeedbackId in(select id from student_feedback sf where sf.feedbackId=? and sf.username=? "
				+ " and  sf.courseId=? and sf.facultyId=?  ) "
				+ "  where  sf.feedbackId =f.id and fq.feedbackId =sf.feedbackId and c.id=sf.courseId and u.username = sf.facultyId  and "
				+ " f.id=?  and sf.username=? and sf.courseId=? and fq.id=?  and sf.facultyId= ? "
				+ " group by fq.description ";

		return findOneSQL(sql, new Object[] { feedbackId, username, courseId,
				facultyId, feedbackId, username, courseId, questionId,
				facultyId });
	}

	public int makeFacultyCourseInactiveForFeedback(StudentFeedback feedback,
			String role) {

		String sql = "";

		if ("ROLE_FACULTY".equals(role)) {
			sql = "Update " + getTableName() + " set active=:active "
					+ " where facultyId = :facultyId and courseId=:courseId ";
		} else if ("ROLE_STUDENT".equals(role)) {
			sql = "Update " + getTableName() + " set active=:active "
					+ " where username = :username and courseId=:courseId ";
		} else {
			return 0;

		}
		return updateSQL(feedback, sql);
	}

	public void makeUserCourseInactiveForFeedback(String username, String role) {

		String sql = "";

		if ("ROLE_FACULTY".equals(role)) {

			sql = "Update "

			+ getTableName()

			+ " set active= 'N' "

			+ " where facultyId = ? ";

		}

		else if ("ROLE_STUDENT".equals(role)) {

			sql = "Update "

			+ getTableName()

			+ " set active= 'N' "

			+ " where username = ? ";

		}

		getJdbcTemplate().update(sql, new Object[] { username });

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusWithoutSession(
			List<String> programId, String acadYear, String campusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		// params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ "  and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramWithoutSession(
			List<String> programId, String acadYear) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		// params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ "  and p.id in(:programIds) and c.acadYear = :acadYear ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFacultyWithoutSession(
			List<String> programId, String acadYear, String campusId,
			String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		params.put("facultyId", facultyId);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ "  and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId "
				+ " and sf.facultyId=:facultyId and f.isPublished='Y' ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramByFacultyWithoutSession(
			List<String> programId, String acadYear, String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadYear", acadYear);
		params.put("facultyId", facultyId);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId "
				+ " and c.programId=p.id  and p.id in(:programIds) and c.acadYear=:acadYear "
				+ " and sf.facultyId=:facultyId and f.isPublished='Y' ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<String> getAllFacultiesForFeedback(String acadYear,
			String campusId, String username, List<String> acadSessions) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		params.put("username", username);
		params.put("acadSessions", acadSessions);

		String sql = "";
		if (campusId != null) {
			sql = " select distinct facultyId from student_feedback sf,course c,feedback f "
					+ " where sf.acadYear=:acadYear and sf.courseId=c.id and c.campusId = :campusId "
					+ " and c.acadSession in(:acadSessions) "
					+ " and f.id=sf.feedbackId and f.createdBy = :username ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params,
					String.class);
		} else {
			sql = " select distinct facultyId from student_feedback sf,course c,feedback f "
					+ " where sf.acadYear=:acadYear and sf.courseId=c.id  and c.acadSession in(:acadSessions) "
					+ " and f.id=sf.feedbackId and f.createdBy = :username ";
			return getNamedParameterJdbcTemplate().queryForList(sql, params,
					String.class);
		}

	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseNew(
			List<String> programId, List<String> acadSession, String username) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("username", username);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ " and p.id in(:programIds) and c.acadSession in(:acadSessions) and f.createdBy = :username and sf.createdBy = :username ";
		// return findAllSQL(sql, new Object[] {});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));
	}

public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyNew(
			String courseId, String facultyId, String username) {

		String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
				+ getTableName()
				+ " sf, users u ,course c , program p   where sf.courseId = ? and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? and u.active ='Y' and u.enabled = 1 and c.active ='Y' and sf.facultyId=u.username"
				+ " and sf.createdBy = ? ";
		return findAllSQL(sql, new Object[] { courseId, facultyId, username });
	}

public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
			String acadSession, List<String> programId, String acadYear,
			String campusId, String username) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		params.put("username", username);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId and f.createdBy =:username and sf.createdBy =:username ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgram(
			String acadSession, List<String> programId, String acadYear, String username) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("username", username);
		final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
				+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
				+ "  from student_feedback sf, feedback f, users u , course c,program p "
				+ " where courseId in (select distinct id from course "
				+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
				+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
				+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and f.createdBy =:username and sf.createdBy =:username ";
		// return findAllSQL(sql, new Object[] {acadSession});
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentFeedback.class));

	}

	public List<String> getStudentFeedbackByFeedbackId(String feedbackId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackId", feedbackId);
		params.put("feedbackId", feedbackId);
		
		String sql = "select distinct username from student_feedback where feedbackId =:feedbackId and feedbackCompleted='N' and "
				+ "username not in (select distinct username from student_feedback where feedbackId =:feedbackId and feedbackCompleted='Y')";
		
		return getNamedParameterJdbcTemplate().queryForList(sql, params,
				String.class);
	}
	
	public void removeStudent_FeedbackUsingExcel(String feedbackId,String username) {
		executeUpdateSql(
				"update student_feedback set active = 'N'  where feedbackId=? and username=?",
				new Object[] { feedbackId,username });
	
	}

	public List<StudentFeedback> findStartedFeedbackByUsername(String username){
		String sql="select sf.* from student_feedback sf, feedback f where sf.feedbackId=f.id and sf.username=? and (sf.feedbackCompleted='N' or sf.feedbackCompleted is NULL) "
				+ "and sf.startDate <= now() and sf.endDate >= now() and sf.active ='Y' and f.active='Y'";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<StudentFeedback> getFacultyByFeedbackId(Long feedbackId){
		String sql ="select sf.id,sf.acadYear,sf.courseId,u.enabled,sf.facultyId,c.courseName,CONCAT(u.firstname,' ',u.lastname) as facultyName from student_feedback sf,course c,users u where"
				+ " c.id = sf.courseId and sf.facultyId = u.username and sf.feedbackId = ? and sf.active = 'Y' group by sf.courseId,sf.facultyId;";
		return findAllSQL(sql, new Object[] { feedbackId });
	}
	
	public void removeFacultyFromFeedback(String feedbackId,String facultyId,String courseId){
		executeUpdateSql(
				"update student_feedback set active = 'N' where feedbackId = ? and facultyId = ? and courseId = ?",
				new Object[] { feedbackId,facultyId,courseId });
	}

	
	public List<String> findStartedFeedbackByUsername() {
		String sql=" SELECT DISTINCT createdBy FROM feedback_question ";
		return listOfStringParameter(sql, new Object[] {});
	}

	public int makeMultipleUserCourseInactiveForFeedbackSupportAdmin(List<String> usernameList,
			List<String> roleList, List<String> courseIdList) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username",usernameList);
		params.put("role",roleList);
		params.put("courseId",courseIdList);
		String 	sql ="";

		if(roleList.contains("ROLE_STUDENT"))
		{
			
			sql = "Update " + getTableName() + " set active= 'N' "
					+ " where username  in(:username) and courseId in(:courseId) ";
		}
		else if(roleList.contains("ROLE_FACULTY"))
		{
			
			sql = "Update " + getTableName() + " set active='N' "
					+ " where facultyId in(:username) and courseId in(:courseId) ";
		}
		
		return getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	//amey 14-10-2020
	public List<StudentFeedback> getstudentFeedbackListCourseWiseByFeedbackType(
			List<String> programId, List<String> acadSession, String username, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("username", username);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ " and p.id in(:programIds) and c.acadSession in(:acadSessions) and f.createdBy = :username and sf.createdBy = :username and f.feedbackType is null ";
			
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("username", username);
			params.put("feedbackType", feedbackType);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ " and p.id in(:programIds) and c.acadSession in(:acadSessions) and f.createdBy = :username and sf.createdBy = :username and f.feedbackType = :feedbackType ";
			
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}
		
	}
	
	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyAndFeedbackType(
			String courseId, String facultyId, String username, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
					+ getTableName()
					+ " sf, feedback f, users u ,course c , program p   where sf.courseId = ? and sf.feedbackId = f.id and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? and u.active ='Y' and u.enabled = 1 and c.active ='Y' and sf.facultyId=u.username"
					+ " and sf.createdBy = ? and f.active = 'Y' and f.feedbackType is null ";
			return findAllSQL(sql, new Object[] { courseId, facultyId, username });
		}else {
			String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
					+ getTableName()
					+ " sf, feedback f, users u ,course c , program p   where sf.courseId = ? and sf.feedbackId = f.id and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? and u.active ='Y' and u.enabled = 1 and c.active ='Y' and sf.facultyId=u.username"
					+ " and sf.createdBy = ? and f.active = 'Y' and f.feedbackType = ? ";
			return findAllSQL(sql, new Object[] { courseId, facultyId, username, feedbackType });
		}
		
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusandType(
			String acadSession, List<String> programId, String acadYear,
			String campusId, String username, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("campusId", campusId);
			params.put("username", username);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and f.feedbackType is null and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId and f.createdBy =:username and sf.createdBy =:username ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("campusId", campusId);
			params.put("username", username);
			params.put("feedbackType", feedbackType);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and f.feedbackType=:feedbackType and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId and f.createdBy =:username and sf.createdBy =:username ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}

	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndType(
			String acadSession, List<String> programId, String acadYear, String username, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("username", username);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and f.feedbackType is null and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and f.createdBy =:username and sf.createdBy =:username ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("username", username);
			params.put("feedbackType", feedbackType);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and f.feedbackType = :feedbackType and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ " and c.acadSession = :acadSessions and p.id in(:programIds) and c.acadYear = :acadYear and f.createdBy =:username and sf.createdBy =:username ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}

	}
	
	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyAndType(
			String courseId, String facultyId, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
					+ getTableName()
					+ " sf, feedback f, users u ,course c , program p   where sf.courseId = ? and sf.feedbackId = f.id and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? and f.feedbackType is null and f.active = 'Y' and u.active ='Y' and u.enabled = 1 and c.active ='Y' and sf.facultyId=u.username ";
			return findAllSQL(sql, new Object[] { courseId, facultyId });
		}else {
			String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback from "
					+ getTableName()
					+ " sf, feedback f, users u ,course c , program p   where sf.courseId = ? and sf.feedbackId = f.id and  c.id=sf.courseId and c.programId=p.id and sf.facultyId = ? and f.feedbackType = ? and f.active = 'Y' and u.active ='Y' and u.enabled = 1 and c.active ='Y' and sf.facultyId=u.username ";
			return findAllSQL(sql, new Object[] { courseId, facultyId, feedbackType });
		}
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusAndTypeByFacultyWithoutSession(
			List<String> programId, String acadYear, String campusId,
			String facultyId, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadYear", acadYear);
			params.put("campusId", campusId);
			params.put("facultyId", facultyId);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ "  and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId "
					+ " and sf.facultyId=:facultyId and f.isPublished='Y' and f.feedbackType is null ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadYear", acadYear);
			params.put("campusId", campusId);
			params.put("facultyId", facultyId);
			params.put("feedbackType", feedbackType);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId and c.programId=p.id "
					+ "  and p.id in(:programIds) and c.acadYear = :acadYear and c.campusId=:campusId "
					+ " and sf.facultyId=:facultyId and f.isPublished='Y' and f.feedbackType=:feedbackType ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}

	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndTypeByFacultyWithoutSession(
			List<String> programId, String acadYear, String facultyId, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadYear", acadYear);
			params.put("facultyId", facultyId);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId "
					+ " and c.programId=p.id  and p.id in(:programIds) and c.acadYear=:acadYear "
					+ " and sf.facultyId=:facultyId and f.isPublished='Y' and f.feedbackType is null ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadYear", acadYear);
			params.put("facultyId", facultyId);
			params.put("feedbackType", feedbackType);
			final String sql = " select sf.*,concat(u.firstName,' ',u.lastName) as facultyName,c.courseName as courseNameforFeedback,p.programName as programNameForFeedback, "
					+ "  (case when u.`type`='P' THEN 'Core' else 'Visiting' end) as typeOfFaculty,c.acadSession as acadSession "
					+ "  from student_feedback sf, feedback f, users u , course c,program p "
					+ " where courseId in (select distinct id from course "
					+ "	where programId in( select distinct id from program where active='Y') AND active='Y') "
					+ " and f.id=sf.feedbackId and f.active='Y' and sf.active='Y' and sf.facultyId=u.username and c.id=sf.courseId "
					+ " and c.programId=p.id  and p.id in(:programIds) and c.acadYear=:acadYear "
					+ " and sf.facultyId=:facultyId and f.isPublished='Y' and f.feedbackType=:feedbackType ";
			// return findAllSQL(sql, new Object[] {acadSession});
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentFeedback.class));
		}

	}

}
