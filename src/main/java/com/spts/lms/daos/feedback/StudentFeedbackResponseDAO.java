package com.spts.lms.daos.feedback;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.daos.BaseDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("studentFeedbackResponseDAO")
public class StudentFeedbackResponseDAO extends
		BaseDAO<StudentFeedbackResponse> {

	@Override
	protected String getTableName() {
		return "student_feedback_response";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO student_feedback_response(username,studentFeedbackId,feedbackQuestionId,answer,courseId,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:username,:studentFeedbackId,:feedbackQuestionId,:answer,:courseId,:createdBy,:createdDate,"
				+ ":lastModifiedBy,:lastModifiedDate) "
				+ " ON DUPLICATE KEY UPDATE "
				+ " answer = :answer, lastModifiedBy = :lastModifiedBy ,lastModifiedDate = :lastModifiedDate";
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	public List<StudentFeedbackResponse> findStudentFeedbackResponse(
			Long feedbackId) {
		String sql = " select * from student_feedback_response where studentFeedbackId in ( select studentFeedbackId from student_Feedback where "
				+ "feedbackId= ? AND feedbackCompleted = 'Y' ) ";
		return findAllSQL(sql, new Object[] { feedbackId });
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<StudentFeedbackResponse> findStudentFeedbackResponseByUsername(
			Long feedbackId, String username) {
		String sql = "select * from student_feedback_response where studentFeedbackId = ? and username = ?";
		return findAllSQL(sql, new Object[] { feedbackId, username });
	}

	public List<Map<String, Object>> getFacultyAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId,
			String facultyId, List<String> usernameList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackQuestionId", feedbackQuestionIds);
		params.put("courseId", courseId);
		params.put("facultyId", facultyId);
		params.put("usernames", usernameList);
		String sql = " select feedbackQuestionId,avg(answer) as average from student_feedback_response sfr , student_feedback sf "
				+ " where feedbackQuestionId in (:feedbackQuestionId) and sf.courseId= :courseId and sf.facultyId=:facultyId "
				+ " and sf.id= sfr.studentFeedbackId and sf.courseId=sfr.courseId and sfr.username in(:usernames) group by feedbackQuestionId ";
		return getNamedParameterJdbcTemplate().queryForList(sql, params);

	}

	public String getAvgAnswer(List<String> courseId, String facultyId,
			List<String> feedbackQuestId) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);
		params.put("feedbackQuestIds", feedbackQuestId);

		String sql = " select avg(sfr.answer) from student_feedback_response sfr , student_feedback sf where sf.id = sfr.studentFeedbackId "
				+ " and sfr.courseId in (:courseIds) and sf.facultyId = :facultyIds and feedbackQuestionId in(:feedbackQuestIds) ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				String.class);

	}

	public List<Map<String, Object>> getCourseAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId,
			List<String> usernameList) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackQuestionId", feedbackQuestionIds);
		params.put("courseId", courseId);
		params.put("usernames", usernameList);
		String sql = " select feedbackQuestionId as feedbackQuestionId,avg(answer) as average from student_feedback_response "
				+ " where feedbackQuestionId in (:feedbackQuestionId) and courseId=:courseId and username in(:usernames) group by feedbackQuestionId ";
		return getNamedParameterJdbcTemplate().queryForList(sql, params);

	}

	public List<Map<String, Object>> getCourseAverageScoreByFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackQuestionId", feedbackQuestionIds);
		params.put("courseId", courseId);
		String sql = " select feedbackQuestionId as feedbackQuestionId,avg(answer) as average from student_feedback_response "
				+ " where feedbackQuestionId in (:feedbackQuestionId) and courseId=:courseId group by feedbackQuestionId ";
		return getNamedParameterJdbcTemplate().queryForList(sql, params);

	}

	public List<Map<String, Object>> getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId, String facultyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackQuestionId", feedbackQuestionIds);
		params.put("courseId", courseId);
		params.put("facultyId", facultyId);
		String sql = " select feedbackQuestionId,avg(answer) as average from student_feedback_response sfr , student_feedback sf "
				+ " where feedbackQuestionId in (:feedbackQuestionId) and sf.courseId= :courseId and sf.facultyId=:facultyId "
				+ " and sf.id= sfr.studentFeedbackId and sf.courseId=sfr.courseId group by feedbackQuestionId ";
		return getNamedParameterJdbcTemplate().queryForList(sql, params);

	}

	/*public String getAvgAnswerforActiveFeedbackByFaculty(List<String> courseId,
			String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);

		String sql = "  select avg(answer) from student_feedback_response sfr , student_feedback sf , feedback f  where  sfr.studentFeedbackId =sf.id and f.id = sf.feedbackId and  sfr.courseId in (:courseIds) "
				+ " and sf.facultyId =:facultyIds and f.active = 'Y' ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				String.class);

	}*/
	
	public String getAvgAnswerforActiveFeedbackByFaculty(List<String> courseId,
			String facultyId, String username) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);
		params.put("username", username);

		String sql = "  select avg(answer) from student_feedback_response sfr , student_feedback sf , feedback f  where  sfr.studentFeedbackId =sf.id and f.id = sf.feedbackId and  sfr.courseId in (:courseIds) "
				+ " and sf.facultyId =:facultyIds and f.active = 'Y' and f.createdBy = :username and sf.createdBy = :username ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				String.class);

	}

	/*public String getAvgAnswerforActiveFeedback(List<String> courseId) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);

		String sql = "  select avg(answer) from student_feedback_response sfr , student_feedback sf , feedback f  where  sfr.studentFeedbackId =sf.id and f.id = sf.feedbackId and  sfr.courseId in (:courseIds) "
				+ " and f.active = 'Y' ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				String.class);

	}*/
	
	public String getAvgAnswerforActiveFeedback(List<String> courseId, String username) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("username", username);
		String sql = "  select avg(answer) from student_feedback_response sfr , student_feedback sf , feedback f  where  sfr.studentFeedbackId =sf.id and f.id = sf.feedbackId and  sfr.courseId in (:courseIds) "
				+ " and f.active = 'Y' and f.createdBy = :username and sf.createdBy = :username ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				String.class);

	}

	public Map<String, Object> getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
			Long feedbackQuestionId, String courseId, String facultyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackQuestionId", feedbackQuestionId);
		params.put("courseId", courseId);
		params.put("facultyId", facultyId);
		String sql = " select feedbackQuestionId,avg(answer) as average from student_feedback_response sfr , student_feedback sf "
				+ " where feedbackQuestionId =:feedbackQuestionId and sf.courseId= :courseId and sf.facultyId=:facultyId "
				+ " and sf.id= sfr.studentFeedbackId and sf.courseId=sfr.courseId group by feedbackQuestionId ";
		try {
			return getNamedParameterJdbcTemplate().queryForMap(sql, params);
		} catch (Exception ex) {
			return null;
		}
	}

	public int getGrandTotal(List<String> studentFeedbackId) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("ids", studentFeedbackId);

		String sql = " select sum(answer) from student_feedback_response where studentFeedbackId in(:ids) ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,

		Integer.class);

	}

	public int checkFeedbackCompletionStatusByCourse(Long feedbackId,
			String studentFeedbackId, String username) {
		String sql = "SELECT CASE WHEN (select count(*)  FROM  feedback_question where feedbackId =? )="
				+ "(select count(*)  from student_feedback_response where studentFeedbackId=? and username=? )"
				+ "THEN 1 ELSE 0 END AS RowCountResult";
		return getJdbcTemplate().queryForObject(sql,
				new Object[] { feedbackId, studentFeedbackId, username },
				Integer.class);

	}

	public List<StudentFeedbackResponse> getstudentFeedbackResponseListByStudentFeedbackId(
			List<String> studentFeedbackId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", studentFeedbackId);
		String sql = " select * from student_feedback_response where studentFeedbackId in(:ids) ";
		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));

	}

	public List<StudentFeedbackResponse> getAverageByQuestionId(
			List<String> feedbackQuestionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", feedbackQuestionId);
		String sql = " select avg(answer) as average,feedbackQuestionId from student_feedback_response where feedbackQuestionId in (:ids) ";
		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));

	}

	public List<StudentFeedbackResponse> findAverageForFeedback(Long courseId,
			List<Long> questionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("ids", questionId);

		String sql = " select avg(answer) as 'courseAverage' from student_feedback_response where courseId =:courseId and feedbackQuestionId in (:ids) ";

		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));
	}

	public List<StudentFeedbackResponse> findFeedbackResponseByStudent(
			String username) {

		String sql = "select sfr.* from student_feedback_response sfr where sfr.username  = ?";

		return findAllSQL(sql, new Object[] { username });

	}

	public Map<String, Object> getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
			List<String> feedbackQuestionIdList, String courseId,
			String facultyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackQuestionIdList", feedbackQuestionIdList);
		params.put("courseId", courseId);
		params.put("facultyId", facultyId);
		String sql = " select avg(answer) as average from student_feedback_response sfr , student_feedback sf "
				+ " where feedbackQuestionId in(:feedbackQuestionIdList) and sf.courseId= :courseId and sf.facultyId=:facultyId "
				+ " and sf.id= sfr.studentFeedbackId and sf.courseId=sfr.courseId  ";
		try {

			return getNamedParameterJdbcTemplate().queryForMap(sql, params);
		} catch (Exception ex) {
			return null;
		}
	}

	public List<StudentFeedbackResponse> findAnswersByFacultyAndProgramAndYear(
			String acadYear, String facultyId, String programId,
			List<String> acadSessions) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadYear", acadYear);
		params.put("programId", programId);
		params.put("facultyId", facultyId);
		params.put("acadSessions", acadSessions);

		String sql = " select min(answer) as lowestFeedback, max(answer) as highestFeedback , round(avg(answer),2) as averageFeedback,"
				+ " c.moduleId,c.moduleName,p.programName "
				+ " from student_feedback_response sfr,course c,student_feedback sf,program p "
				+ " where sf.courseId=c.id and sfr.studentFeedbackId=sf.id and sf.acadYear=:acadYear  "
				+ " and sf.facultyId = :facultyId and c.programId = :programId  and c.programId=p.id "
				+ " and c.acadSession in(:acadSessions) "
				+ " group by c.moduleId ";

		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));
	}

	public List<StudentFeedbackResponse> findAnswersByDept(String acadYear,
			String facultyId, List<String> acadSessions) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadYear", acadYear);
		params.put("facultyId", facultyId);
		params.put("acadSessions", acadSessions);

		String sql = " select min(answer) as lowestFeedback, max(answer) as highestFeedback , round(avg(answer),2) as averageFeedback,"
				+ " c.moduleId,c.moduleName,p.programName,c.dept "
				+ " from student_feedback_response sfr,course c,student_feedback sf,program p "
				+ " where sf.courseId=c.id and sfr.studentFeedbackId=sf.id and sf.acadYear=:acadYear  "
				+ " and sf.facultyId = :facultyId  and c.programId=p.id and (dept is not null and dept <> '') "
				+ " and c.acadSession in(:acadSessions) " + " group by c.dept ";

		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));
	}
	
	
	/*public List<String> getAvgAnswer1(List<String> courseId,
			List<String> facultyId, List<String> feedbackQuestId) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);
		params.put("feedbackQuestIds", feedbackQuestId);

		String sql = " select avg(sfr.answer) from student_feedback_response sfr , student_feedback sf where sf.id = sfr.studentFeedbackId "
				+ " and sfr.courseId in (:courseIds) and sf.facultyId in (:facultyIds) and feedbackQuestionId in(:feedbackQuestIds) "
				+ " group by sf.facultyId";

		return getNamedParameterJdbcTemplate().queryForList(sql, params,
				String.class);

	}*/
	
	public List<String> getAvgAnswer1(List<String> courseId,
			List<String> facultyId, List<String> feedbackQuestId, String username) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);
		params.put("feedbackQuestIds", feedbackQuestId);
		params.put("username", username);
		String sql = " select avg(sfr.answer) from student_feedback_response sfr , student_feedback sf where sf.id = sfr.studentFeedbackId "
				+ " and sfr.courseId in (:courseIds) and sf.facultyId in (:facultyIds) and feedbackQuestionId in(:feedbackQuestIds) and sf.createdBy = :username "
				+ " group by sf.facultyId";

		return getNamedParameterJdbcTemplate().queryForList(sql, params,
				String.class);

	}
	
	/*public List<StudentFeedbackResponse> getAvgAnswersByFacultyId(List<String> courseId, List<String> facultyId,
			List<String> feedbackQuestId){
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);
		params.put("feedbackQuestIds", feedbackQuestId);
		
		String sql = " select avg(sfr.answer) as average,sf.facultyId,concat(u.firstName,' ',u.lastName) as facultyName,count(distinct(sf.username)) as noOfStudentCompleted "
				+ " from student_feedback_response sfr , student_feedback sf,users u where sf.id = sfr.studentFeedbackId and sf.facultyId=u.username "
				+ " and sfr.courseId in (:courseIds) "
				 + " and sf.facultyId in(:facultyIds) and "
				 + " feedbackQuestionId in(:feedbackQuestIds) "
				 + " and sf.feedbackCompleted = 'Y' "
				 + " group by sf.facultyId order by average desc,noOfStudentCompleted desc " ;
		
		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));
	}*/
	
	public List<StudentFeedbackResponse> getAvgAnswersByFacultyId(List<String> courseId, List<String> facultyId,
			List<String> feedbackQuestId, String username){
		
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("courseIds", courseId);
		params.put("facultyIds", facultyId);
		params.put("feedbackQuestIds", feedbackQuestId);
		params.put("username", username);
		String sql = " select avg(sfr.answer) as average,sf.facultyId,concat(u.firstName,' ',u.lastName) as facultyName,count(distinct(sf.username)) as noOfStudentCompleted "
				+ " from student_feedback_response sfr , student_feedback sf,users u where sf.id = sfr.studentFeedbackId and sf.facultyId=u.username "
				+ " and sfr.courseId in (:courseIds) "
				 + " and sf.facultyId in(:facultyIds) and "
				 + " feedbackQuestionId in(:feedbackQuestIds) "
				 + " and sf.feedbackCompleted = 'Y' and sf.createdBy = :username "
				 + " group by sf.facultyId order by average desc,noOfStudentCompleted desc " ;
		
		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentFeedbackResponse.class));
	}
	
	
	
	public List<StudentFeedbackResponse> findAnswersByFacultyAndModuleAndYear(
			String acadYear, String facultyId, String moduleId,
			List<String> acadSessions) throws ArrayIndexOutOfBoundsException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadYear", acadYear);
		params.put("moduleId", moduleId);
		params.put("facultyId", facultyId);
		params.put("acadSessions", acadSessions);

		String sql = " SELECT  ROUND(AVG(answer),2) AS averageFeedback,"
				+ " c.moduleId,c.moduleName,c.courseName FROM student_feedback_response sfr,course c,student_feedback sf,module m "
				+ " WHERE sf.courseId=c.id AND sfr.studentFeedbackId=sf.id AND sf.acadYear= :acadYear "
				+ " AND sf.facultyId = :facultyId AND c.moduleId = :moduleId  AND c.moduleId=m.module_id "
				+ " AND c.acadSession IN(:acadSessions) "
				+ " GROUP BY c.moduleId";

		return getNamedParameterJdbcTemplate().query(sql,params,BeanPropertyRowMapper.newInstance(StudentFeedbackResponse.class));
	
	}
	
	
	
	
	

}
