package com.spts.lms.daos.feedback;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.web.utils.Utils;

@Repository("feedbackDAO")
public class FeedbackDAO extends BaseDAO<Feedback> {

	@Override
	protected String getTableName() {
		return "feedback";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO feedback(active,"
				+ "feedbackName,feedbackType,createdBy,createdDate,lastModifiedBy,lastModifiedDate)"
				+ " VALUES('Y',:feedbackName,:feedbackType,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update feedback set active=COALESCE(:active, active),"
				+ "feedbackName=:feedbackName,feedbackType=:feedbackType,createdBy=:createdBy,createdDate=:createdDate,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate where id=:id "
				+ "and not exists (select id from student_feedback sfb where sfb.feedbackId = feedback.id)";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Feedback> findAllActiveWithCourse() {
		final String sql = "SELECT fb.*, c.courseName FROM  "
				+ getTableName()
				+ " fb INNER JOIN course c ON c.id = fb.courseId where active = 'Y' ";
		return findAllSQL(sql, new Object[] {});
	}

	public List<Feedback> findByUser(String username, String acadMonth,
			String acadYear) {

		Date dt = Utils.getInIST();
		final String sql = "SELECT fb.* FROM "
				+ getTableName()
				+ " fb INNER JOIN student_feedback sfb ON sfb.feedbackId = fb.id "
				+ " where sfb.username = ? " + " and sfb.acadMonth = ? "
				+ " and sfb.acadYear = ? " + " and sfb.startDate <= ? "
				+ " and sfb.endDate >= ? " + " and fb.active = 'Y' ";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear,
				dt, dt });
	}

	public Page<Feedback> searchStudentFeedback(String username, int pageNo,
			int pageSize) {
		// ArrayList<Object> parameters = buildParameters(username);
		// The first object will always be the criteria String
		// StringBuilder criteria = (StringBuilder) parameters.remove(0);

		return paginationHelper
				.fetchPage(
						getJdbcTemplate(),
						"SELECT COUNT(*) FROM "
								+ getTableName()
								+ " where id in ( select feedbackId from student_feedback where student_feedback.username= ?  ) ",
						"SELECT * FROM "
								+ getTableName()
								+ " where id in ( select feedbackId from student_feedback where student_feedback.username= ?  ) ",
						new Object[] { username }, pageNo, pageSize,
						BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Feedback> findAllValidFeedback(String username) {
		final String sql = "select distinct (f.id),f.feedbackName from "
				+ getTableName()
				+ " f inner join feedback_question fq on f.id = fq.feedbackId where f.active = 'Y' and f.createdBy=?  order by f.createdDate desc ";
		return findAllSQL(sql, new Object[] {username});
	}

	public List<Feedback> findByUserAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		Date dt = Utils.getInIST();
		final String sql = "SELECT fb.* FROM "
				+ getTableName()
				+ " fb INNER JOIN student_feedback sfb ON sfb.feedbackId = fb.id "
				+ " where sfb.username = ? " + " and fb.courseId = ? "
				+ " and sfb.acadMonth = ? " + " and sfb.acadYear = ? "
				+ " and sfb.startDate <= ? " + " and sfb.endDate >= ? "
				+ " and fb.active = 'Y' ";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth,
				acadYear, dt, dt });
	}

	/*
	 * public List<Feedback> findfeedbackNotAssigned() { final String sql =
	 * "select f.* from feedback f where f.id NOT IN ( select sf.feedbackId from student_feedback sf) and f.active='Y' "
	 * + " group by f.id order by f.createdDate desc";
	 * 
	 * return findAllSQL(sql, new Object[] {}); }
	 * 
	 * public List<Feedback> findAllocatedfeedbackByProgramId(Long programId) {
	 * final String sql =
	 * "select f.* from feedback f, student_feedback sf,course c , program p " +
	 * "where  sf.feedbackId=f.id  and c.id =  sf.courseId and c.programId=p.id and p.id=? and f.active='Y'"
	 * + "  group by f.id order by f.createdDate desc";
	 * 
	 * return findAllSQL(sql, new Object[] { programId }); }
	 */

	public List<Feedback> findfeedbackNotAssigned(String username) {
		final String sql = "select f.* from feedback f where f.id NOT IN ( select sf.feedbackId from student_feedback sf) and f.active='Y' "
				+ " and (f.lastModifiedBy = ? OR f.createdBy = ?) group by f.id order by f.createdDate desc";

		return findAllSQL(sql, new Object[] { username, username });
	}

	public List<Feedback> findAllocatedfeedbackByProgramId(Long programId,
			String username) {
		final String sql = " select f.* from feedback f, student_feedback sf,course c , program p "
				+ " where  sf.feedbackId=f.id  and c.id =  sf.courseId and c.programId=p.id and p.id=? and f.active='Y' "
				+ "  and (f.lastModifiedBy = ? OR f.createdBy = ?) group by f.id order by f.createdDate desc";

		return findAllSQL(sql, new Object[] { programId, username, username });
	}

	public Feedback getDatesForFeedback(Long feedbackId) {
		String sql = "select distinct(sf.startDate), sf.endDate from student_feedback sf where sf.active='Y' and sf.feedbackId=? and sf.feedbackCompleted = 'N' limit 1";
		return findOneSQL(sql, new Object[] { feedbackId });
	}

	public Feedback getDatesForFeedbackForCompleted(Long feedbackId) {
		String sql = "select distinct(sf.startDate), sf.endDate from student_feedback sf where sf.active='Y' and sf.feedbackId=? "
				+ " and sf.feedbackCompleted = 'Y' limit 1";
		return findOneSQL(sql, new Object[] { feedbackId });
	}

	/*public List<Feedback> findAllFeedbacksByAcadSesionAndYear(
			List<String> acadSessionList, String acadYear) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("acadSession", acadSessionList);
		params.put("acadYear", acadYear);

		final String sql = " select f.* from feedback f,student_feedback sf,course c "
				+ " where sf.courseId=c.id and c.acadSession in (:acadSession) and f.id=sf.feedbackId "
				+ " and c.acadYear =:acadYear and c.active = 'Y' AND f.active = 'Y' group by f.id ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Feedback.class));
	}*/
	
	public List<Feedback> findAllFeedbacksByAcadSesionAndYear(
			List<String> acadSessionList, String acadYear, String username) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("acadSession", acadSessionList);
		params.put("acadYear", acadYear);
		params.put("username", username);
		final String sql = " select f.* from feedback f,student_feedback sf,course c "
				+ " where sf.courseId=c.id and c.acadSession in (:acadSession) and f.id=sf.feedbackId "
				+ " and c.acadYear =:acadYear and c.active = 'Y' AND f.active = 'Y' and f.createdBy =:username and sf.createdBy =:username group by f.id ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Feedback.class));
	}

	public void publishFeedback(Long feedbackId) {
		String sql = "update feedback set isPublished = 'Y' where id = ? ";
		executeUpdateSql(sql, new Object[] { feedbackId });
	}

	public List<Feedback> getAllAllocatedFacultiesByFeedbackId(String feedbackId) {

		String sql = " select distinct c.id as courseId,sf.facultyId,c.courseName,concat(u.firstName,' ',u.lastName) as facultyName,"
				+ " c.acadSession,p.programName "
				+ " from course c,users u,program p,student_feedback sf,feedback f "
				+ " where f.id=sf.feedbackId and sf.courseId=c.id and sf.facultyId=u.username "
				+ " and c.programId=p.id and f.id =? and sf.active = 'Y'";

		return findAllSQL(sql, new Object[] { feedbackId });

	}

	/*public List<Feedback> findAllActiveByUsername(String campusId,
			String acadYear1, String acadYear2) {

		if (!campusId.equals("null")) {
			String sql = "  select f.* from "
					+ getTableName()
					+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id and c.campusId = ? "
					+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?)  group by f.id ";
			return findAllSQL(sql, new Object[] { campusId, acadYear1,
					acadYear2 });
		} else {
			String sql = "  select f.* from "
					+ getTableName()
					+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id  "
					+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?)  group by f.id ";
			return findAllSQL(sql, new Object[] {  acadYear1,
					acadYear2 });
		}
	}*/
	
	public List<Feedback> findAllActiveByUsername(String campusId,
			String acadYear1, String acadYear2, String username) {

		if (!campusId.equals("null")) {
			String sql = "  select f.* from "
					+ getTableName()
					+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id and c.campusId = ? "
					+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?) and f.createdBy = ? and sf.createdBy = ?  group by f.id ";
			return findAllSQL(sql, new Object[] { campusId, acadYear1,
					acadYear2, username, username });
		} else {
			String sql = "  select f.* from "
					+ getTableName()
					+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id  "
					+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?) and f.createdBy = ? and sf.createdBy = ? group by f.id ";
			return findAllSQL(sql, new Object[] {  acadYear1,
					acadYear2, username, username });
		}
	}
	
	public List<Feedback> findAllActiveByCreatedBy(String username) {

        String sql = "  select f.* from "
                                        + getTableName()
                                        + " f where f.active = 'Y' and f.createdBy = ? ";
        return findAllSQL(sql, new Object[] { username });

	}
	
	//amey 14-10-2020
	public List<Feedback> findAllActiveByCreatedByAndType(String username, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			String sql = " select f.* from "
			                    + getTableName()
			                    + " f where f.active = 'Y' and feedbackType is null and f.createdBy = ? ";
			return findAllSQL(sql, new Object[] { username });
		}else {
	        String sql = " select f.* from "
                                + getTableName()
                                + " f where f.active = 'Y' and feedbackType = ? and f.createdBy = ? ";
	        return findAllSQL(sql, new Object[] { feedbackType, username });
		}

	}
	
	public List<Feedback> findAllActiveByUsernameAndFeedbackType(String campusId,
			String acadYear1, String acadYear2, String username, String feedbackType) {
		
		if(feedbackType == null || "".equals(feedbackType)) {
			if (!campusId.equals("null")) {
				String sql = "  select f.* from "
						+ getTableName()
						+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id and c.campusId = ? "
						+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?) and f.createdBy = ? and sf.createdBy = ? and f.feedbackType is null  group by f.id ";
				return findAllSQL(sql, new Object[] { campusId, acadYear1,
						acadYear2, username, username });
			} else {
				String sql = "  select f.* from "
						+ getTableName()
						+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id  "
						+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?) and f.createdBy = ? and sf.createdBy = ? and f.feedbackType is null group by f.id ";
				return findAllSQL(sql, new Object[] {  acadYear1,
						acadYear2, username, username });
			}
		}else {
			if (!campusId.equals("null")) {
				String sql = "  select f.* from "
						+ getTableName()
						+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id and c.campusId = ? "
						+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?) and f.createdBy = ? and sf.createdBy = ? and f.feedbackType = ? group by f.id ";
				return findAllSQL(sql, new Object[] { campusId, acadYear1,
						acadYear2, username, username, feedbackType });
			} else {
				String sql = "  select f.* from "
						+ getTableName()
						+ " f,student_feedback sf,course c where f.active = 'Y' and sf.courseId =c.id  "
						+ " and f.id = sf.feedbackId and (sf.acadYear = ? or sf.acadYear = ?) and f.createdBy = ? and sf.createdBy = ? and f.feedbackType = ? group by f.id ";
				return findAllSQL(sql, new Object[] {  acadYear1,
						acadYear2, username, username, feedbackType });
			}
		}
		
	}

	//Peter 28/10/2021
	public Feedback checkIfFeedbackExists(String username, Long feedbackId) {
		String sql = "select id from feedback f where f.id=? and f.active = 'Y' and f.createdBy=?";
		return findOneSQL(sql,new Object[] {feedbackId, username});
	}

	public Feedback getFeedbackType(String feedbackId) {
		String sql = "select feedbackType from feedback where id=?";
		return findOneSQL(sql,new Object[] {feedbackId});
	}

}
