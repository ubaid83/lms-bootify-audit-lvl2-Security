package com.spts.lms.daos.feedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.feedback.FeedbackQuestion;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;

@Repository("feedbackQuestionDAO")
public class FeedbackQuestionDAO extends BaseDAO<FeedbackQuestion> {

	@Autowired
	 private JdbcTemplate jdbcTemplate;
	
	
	@Override
	protected String getTableName() {
		return "feedback_question";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO feedback_question(feedbackId,type,description,option1,option2,option3,option4,option5,option6,option7,option8,"
				+ " active,createdBy,createdDate,lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:feedbackId,:type,:description,:option1,:option2,:option3,:option4,:option5,:option6,:option7,:option8,"
				+ " 'Y',:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update "
				+ getTableName()
				+ " set description = :description,option1 = :option1,option2 = :option2,option3 = :option3,"
				+ "option4 = :option4,option5 = :option5,option6 = :option6,option7 = :option7,option8 = :option8,"
				+ "type= :type, lastModifiedBy = :lastModifiedBy,lastModifiedDate = :lastModifiedDate where id = :id "
				+ "and not exists (select id from student_feedback sfb where sfb.feedbackId = feedback_question.feedbackId)";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<FeedbackQuestion> findByFeedbackId(Long feedbackId) {
		final String sql = "Select *  from " + getTableName()
				+ " where feedbackId = ? and active='Y'";
		return findAllSQL(sql, new Object[] { feedbackId });
	}

	public List<FeedbackQuestion> findStudentResponseByFeedbackIdAndUsername(
			Long feedbackId, String username) {
		final String sql = "Select fbq.*, sfbr.answer, sfbr.username from feedback_question fbq"
				+ "	left outer join student_feedback_response sfbr on sfbr.feedbackQuestionId = fbq.id and sfbr.username = ? "
				+ "  where fbq.feedbackId = ? and fbq.active='Y'";
		return findAllSQL(sql, new Object[] { username, feedbackId });
	}

	public List<FeedbackQuestion> getFeedbackQuestionListByCourseForfaculty(
			String courseId) {

		String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
				+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y'"
				
				+ " group by fq.id ";
		return findAllSQL(sql, new Object[] { courseId });
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourse(
			String courseId, String username) {

		String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
				+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y' "
				+ " and f.createdBy = ? and sf.createdBy = ? "
				+ " group by fq.id ";
		return findAllSQL(sql, new Object[] { courseId, username, username });
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemester(
			String acadSession) {
		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' and sf.active='Y' "
				+ " and c.acadSession = ? GROUP BY  fq.description order by fq.id";
		return findAllSQL(sql, new Object[] { acadSession });
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemester(
			String acadSession, String acadYear) {
		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadSession = ? and c.acadYear = ? GROUP BY  fq.description order by fq.id";
		return findAllSQL(sql, new Object[] { acadSession, acadYear });
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterAndCampus(
			String acadSession, String acadYear, String campusId) {
		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadSession = ? and c.acadYear = ? and c.campusId = ? GROUP BY  fq.description order by fq.id";
		return findAllSQL(sql, new Object[] { acadSession, acadYear, campusId });
	}

	public FeedbackQuestion getQuestionForReferenceTemplate(String feedbackId,
			String username, String courseId, String questionId,
			String facultyId) {
		/*
		 * final String sql =
		 * "  select distinct fq.*, sf.* ,sfr.*,c.courseName as courseName,concat(u.firstname,' ',u.lastname) as facultyName,sf.courseId as courseId ,sf.id as stuFeedbackId, "
		 * +
		 * " fq.id as id from feedback_question fq,feedback f ,course c ,users u ,student_feedback sf"
		 * +
		 * " LEFT OUTER JOIN student_feedback_response sfr ON sfr.studentfeedbackId= sf.id and sfr.courseId=sf.courseId"
		 * +
		 * " where  sf.feedbackId =f.id and fq.feedbackId =sf.feedbackId and c.id=sf.courseId and u.username = sf.facultyId  and "
		 * +
		 * " f.id=? and sf.username=? and sf.courseId=? group by fq.description"
		 * ;
		 */

		final String sql = "select distinct fq.*, sf.* ,sfr.*,c.courseName as courseName,concat(u.firstname,' ',u.lastname) as facultyName,sf.courseId as courseId ,sf.id as stuFeedbackId, "
				+ " fq.id as id from feedback f ,course c ,users u ,student_feedback sf,feedback_question fq "

				+ " LEFT OUTER JOIN student_feedback_response sfr ON sfr.feedbackQuestionId= fq.id and sfr.studentFeedbackId in(select id from student_feedback sf where sf.feedbackId=? and sf.username=? and  sf.courseId=? and sf.facultyId=?  ) "
				+ " where  sf.feedbackId =f.id and fq.feedbackId =sf.feedbackId and c.id=sf.courseId and u.username = sf.facultyId  and "
				+ "  f.id=?  and sf.username=? and sf.courseId=? and fq.id=?  and sf.facultyId= ? group by fq.description ";

		return findOneSQL(sql, new Object[] { feedbackId, username, courseId,
				facultyId, feedbackId, username, courseId, questionId,
				facultyId });
	}

	public Page<FeedbackQuestion> getStudentFeedbackResponsePage(
			Long feedbackId, String username, int pageNo, int pageSize) {
		final String sql = "Select u.firstname,u.lastname,fbq.*, sfbr.answer as answer,c.courseName as courseName, sfbr.username from course c  ,feedback_question fbq"
				+ " left outer join student_feedback_response sfbr "
				+ " on sfbr.feedbackQuestionId = fbq.id and sfbr.username =? "
				+ "inner join users u on sfbr.username= u.username where sfbr.courseId= c.id and fbq.feedbackId = ? and fbq.active='Y'";
		/*
		 * "Select u.firstname,u.lastname,fbq.*,sfbr.answer , sfbr.username from feedback_question fbq"
		 * +" left outer join student_feedback_response sfbr "
		 * +"  on sfbr.feedbackQuestionId = fbq.id and sfbr.username = ? "
		 * +"  inner join users u on sfbr.username= u.username "
		 * +"   where fbq.feedbackId = ? and fbq.active='Y'";
		 */

		final String countsql = "Select count(*) from feedback_question fbq"
				+ " left outer join student_feedback_response sfbr "
				+ "  on sfbr.feedbackQuestionId = fbq.id and sfbr.username = ? "
				+ "  inner join users u on sfbr.username= u.username "
				+ "   where fbq.feedbackId = ? and fbq.active='Y'";

		return paginationHelper.fetchPage(getJdbcTemplate(), countsql, sql,
				new Object[] { username, feedbackId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}

	public List<Long> findQuestionByFeedbackId(Long feedbackId) {
		final String sql = "Select id  from " + getTableName()
				+ " where feedbackId = ? and active='Y'";
		return getJdbcTemplate().queryForList(sql, Long.class,
				new Object[] { feedbackId });
	}

	public List<FeedbackQuestion> getStudentFeedbackResponseList(
			Long feedbackId, String username) {
		final String sql = "Select u.firstname,u.lastname,fbq.*, sfbr.answer as answer,c.courseName as courseName, sfbr.username from course c  ,feedback_question fbq"
				+ " left outer join student_feedback_response sfbr "
				+ " on sfbr.feedbackQuestionId = fbq.id and sfbr.username =? "
				+ " inner join users u on sfbr.username= u.username where sfbr.courseId= c.id and fbq.feedbackId = ? and fbq.active='Y' order by c.courseName";

		return findAllSQL(sql, new Object[] { username, feedbackId });

	}

	public List<FeedbackQuestion> findFeedbackQuestions() {
		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ "  GROUP BY  fq.description order by fq.id";
		return findAllSQL(sql, new Object[] {});
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgram(
			String acadSession, String acadYear, List<String> programId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);

		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.programId in (:programIds) GROUP BY  fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampus(
			String acadSession, String acadYear, String campusId,
			List<String> programId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);

		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.campusId = :campusId and c.programId in (:programIds) GROUP BY  fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(
			String acadYear, String campusId, List<String> programId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);

		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ "  and c.acadYear = :acadYear and c.campusId = :campusId and c.programId in (:programIds) GROUP BY  fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramWithoutSession(
			String acadYear, List<String> programId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadYear", acadYear);

		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadYear = :acadYear and c.programId in (:programIds) GROUP BY  fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}
	
	
	public List<FeedbackQuestion> findAllQuestionsByFeedbackId(List<String> feedbackIds) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("feedbackIds", feedbackIds);
		

		final String sql = " select * from "+getTableName()+" where feedbackId in (:feedbackIds)  ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));

	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseNew(
			String courseId, String username) {

		String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
				+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y'"
				+ " and f.createdBy = ? and sf.createdBy = ? group by fq.id ";
		return findAllSQL(sql, new Object[] { courseId, username, username });
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampus(
			String acadSession, String acadYear, String campusId,
			List<String> programId, String username) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		params.put("username", username);
		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.campusId = :campusId and c.programId in (:programIds) "
				+ " and f.createdBy =:username and sf.createdBy =:username GROUP BY  fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}

public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgram(
			String acadSession, String acadYear, List<String> programId, String username) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("programIds", programId);
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("username", username);
		String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
				+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
				+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.programId in (:programIds) "
				+ " and f.createdBy =:username and sf.createdBy =:username GROUP BY  fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}

	public List<FeedbackQuestion> getFeedbackQuestionListByCourseForWs(
		String courseId) {

	String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
			+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y' "
			+ " group by fq.id ";
	return findAllSQL(sql, new Object[] { courseId });
	}
	
	public List<FeedbackQuestion> getAllActiveFeedbackTemplateQuestions() {
        
        String sql = " select * from feedback_question_template where active = 'Y' ";
        return findAllSQL(sql, new Object[] {});
	}
	//SuportAdmin	

		public int updateFeedbackQuestion(String description, String id) {
			String query = "UPDATE feedback_question SET description = ?  WHERE id= ? ";
			return jdbcTemplate.update(query,description,id);
			
		}
		



	public FeedbackQuestion getFeefbackForSupportDropBean(String feedbackId) {
	    
	    String sql = " SELECT DISTINCT c.acadSession,c.acadYear,p.programName,p.id as programId,f.createdBy,c.campusId FROM "
	    		+ " student_feedback sf,feedback f,program p,course c WHERE sf.feedbackId = ? AND sf.courseId=c.id AND sf.feedbackId=f.id"
	    		+ " AND c.programId=p.id GROUP BY c.acadsession,c.acadYear,p.programName,f.createdBy,c.campusId ";
	   return findOneSQL(sql, new Object[] {feedbackId});
	}
	/*public List<FeedbackQuestion> findFeedbackQuestionByCourseList(String username,List<Long> courseIds) {
		Map<String,Object> mapper = new HashMap<>();
		mapper.put("username", username);
		mapper.put("courseIds", courseIds);
		String sql ="  select fq.* from student_feedback sf,feedback_question fq where sf.feedbackId=fq.feedbackId and sf.facultyId =:username "
		+ " and sf.courseId in(:courseIds) group by fq.description";
		return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}*/
	public List<FeedbackQuestion> findFeedbackQuestionByCourseList(String username,Set<Long> courseIds) {
		Map<String,Object> mapper = new HashMap<>();
		mapper.put("username", username);
		mapper.put("courseIds", courseIds);
		String sql ="  select fq.* from student_feedback sf,feedback_question fq,feedback f"
				+ "  where sf.feedbackId=fq.feedbackId and sf.facultyId =:username and f.id=sf.feedbackId and "
				+ " f.id=fq.feedbackId "
				+ " and sf.courseId in(:courseIds) "
				+ " and f.isPublished='Y' group by fq.description order by fq.id";
		return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
	}
	
	//amey 14-10-2020
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseAndFeedbackType(
			String courseId, String username, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
					+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y'"
					+ " and f.createdBy = ? and f.feedbackType is null and sf.createdBy = ? group by fq.id ";
			return findAllSQL(sql, new Object[] { courseId, username, username });
		}else {
			String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
					+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y'"
					+ " and f.createdBy = ? and f.feedbackType = ? and sf.createdBy = ? group by fq.id ";
			return findAllSQL(sql, new Object[] { courseId, username, feedbackType, username });
		}
		
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampusAndType(
			String acadSession, String acadYear, String campusId,
			List<String> programId, String username, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("campusId", campusId);
			params.put("username", username);
			String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
					+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' and f.feedbackType is null "
					+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.campusId = :campusId and c.programId in (:programIds) "
					+ " and f.createdBy =:username and sf.createdBy =:username GROUP BY  fq.description order by fq.id";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("campusId", campusId);
			params.put("username", username);
			params.put("feedbackType", feedbackType);
			String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
					+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' and f.feedbackType = :feedbackType "
					+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.campusId = :campusId and c.programId in (:programIds) "
					+ " and f.createdBy =:username and sf.createdBy =:username GROUP BY  fq.description order by fq.id";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
		}
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndType(
			String acadSession, String acadYear, List<String> programId, String username, String feedbackType) {
	
		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("username", username);
			String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
					+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
					+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.programId in (:programIds) "
					+ " and f.createdBy =:username and f.feedbackType is null and sf.createdBy =:username GROUP BY  fq.description order by fq.id";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("programIds", programId);
			params.put("acadSessions", acadSession);
			params.put("acadYear", acadYear);
			params.put("username", username);
			params.put("feedbackType", feedbackType);
			String sql = " select fq.* from feedback_question fq,feedback f,student_feedback sf,course c "
					+ " where fq.feedbackId = f.id and f.id=sf.feedbackId and sf.courseId =c.id and f.active = 'Y' "
					+ " and c.acadSession = :acadSessions and c.acadYear = :acadYear and c.programId in (:programIds) "
					+ " and f.createdBy =:username and f.feedbackType=:feedbackType and sf.createdBy =:username GROUP BY  fq.description order by fq.id";
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
		}
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseAndType(
			String courseId, String username, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
					+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y' "
					+ " and f.createdBy = ? and f.feedbackType is null and sf.createdBy = ? "
					+ " group by fq.id ";
			return findAllSQL(sql, new Object[] { courseId, username, username });
		}else {
			String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
					+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y' "
					+ " and f.createdBy = ? and f.feedbackType = ? and sf.createdBy = ? "
					+ " group by fq.id ";
			return findAllSQL(sql, new Object[] { courseId, username, feedbackType, username });
		}
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseAndTypeForfaculty(
			String courseId, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
					+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y' and f.feedbackType is null "
					
					+ " group by fq.id ";
			return findAllSQL(sql, new Object[] { courseId });
		}else {
			String sql = " select fq.* from feedback_question fq , feedback f, course c, student_feedback sf "
					+ " where fq.feedbackId=f.id and sf.courseId=? and c.id=sf.courseId and sf.feedbackId=f.id and f.active='Y' and f.feedbackType=? "
					
					+ " group by fq.id ";
			return findAllSQL(sql, new Object[] { courseId, feedbackType });
		}
	}
	
public List<FeedbackQuestion> findFeedbackQuestionByCourseListAndType(String username,Set<Long> courseIds, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String,Object> mapper = new HashMap<>();
			mapper.put("username", username);
			mapper.put("courseIds", courseIds);
			String sql ="  select fq.* from student_feedback sf,feedback_question fq,feedback f"
					+ "  where sf.feedbackId=fq.feedbackId and sf.facultyId =:username and f.id=sf.feedbackId and "
					+ " f.id=fq.feedbackId "
					+ " and sf.courseId in(:courseIds) "
					+ " and f.isPublished='Y' and f.feedbackType is null group by fq.description order by fq.id";
			return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
		}else {
			Map<String,Object> mapper = new HashMap<>();
			mapper.put("username", username);
			mapper.put("courseIds", courseIds);
			mapper.put("feedbackType", feedbackType);
			String sql ="  select fq.* from student_feedback sf,feedback_question fq,feedback f"
					+ "  where sf.feedbackId=fq.feedbackId and sf.facultyId =:username and f.id=sf.feedbackId and "
					+ " f.id=fq.feedbackId "
					+ " and sf.courseId in(:courseIds) "
					+ " and f.isPublished='Y' and f.feedbackType=:feedbackType group by fq.description order by fq.id";
			return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(FeedbackQuestion.class));
		}
	}
	
}
