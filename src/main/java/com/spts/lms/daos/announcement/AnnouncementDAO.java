package com.spts.lms.daos.announcement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;

@Repository("announcementDAO")
public class AnnouncementDAO extends BaseDAO<Announcement> {

	@Override
	protected String getTableName() {
		return "announcement";
	}

	@Override
	protected String getInsertSql() {
		final String sql = " INSERT INTO announcement "
				+ " ( "
				+ " courseId, "
				+ " programId, "
				+ " announcementType, "
				+ " subject, "
				+ " filePath, "
				+ " filePreviewPath, "
				+ " description, "
				+ " startDate, "
				+ " endDate, "
				+ " sendEmailAlert, "
				+ " campusId, "
				+ " sendEmailAlertToParents, "
				+ " sendSmsAlertToParents, "
				+ " sendSmsAlert, "
				+ " createdBy, "
				+ " createdDate, "
				+ " lastModifiedBy, "
				+ " lastModifiedDate,announcementSubType,acadSession,acadYear,examMonth,examYear) "
				+ " VALUES "
				+ " ( "
				+ " :courseId, "
				+ " :programId, "
				+ " :announcementType, "
				+ " :subject, "
				+ " :filePath, "
				+ " :filePreviewPath, "
				+ " :description, "
				+ " :startDate, "
				+ " :endDate, "
				+ " :sendEmailAlert, "
				+ " :campusId, "
				+ " :sendEmailAlertToParents, "
				+ " :sendSmsAlertToParents, "
				+ " :sendSmsAlert, "
				+ " :createdBy, "
				+ " :createdDate, "
				+ " :lastModifiedBy, "
				+ " :lastModifiedDate,:announcementSubType,:acadSession,:acadYear,:examMonth,:examYear) ";
		return sql;
	}

	public Announcement getFacultyNotificationAnnouncement() {
		String sql = "select subject,description from "
				+ getTableName()
				+ " where announcementType = 'NOTIFICATION' and active = 'Y' and accessType = 'FACULTY'";

		logger.info("sql---------------------------> " + sql);
		return findOneSQL(sql, new Object[] {});
	}

	public Announcement getStudentNotificationAnnouncement() {
		String sql = "select subject,description from "
				+ getTableName()
				+ " where announcementType = 'NOTIFICATION' and active = 'Y' and accessType = 'STUDENT'";

		logger.info("sql---------------------------> " + sql);
		return findOneSQL(sql, new Object[] {});
	}

	public Announcement getAdminNotificationAnnouncement() {
		String sql = "select subject,description from "
				+ getTableName()
				+ " where announcementType = 'NOTIFICATION' and active = 'Y' and accessType = 'ADMIN'";

		logger.info("sql---------------------------> " + sql);
		return findOneSQL(sql, new Object[] {});
	}

	@Override
	protected String getUpdateSql() {
		final String sql = " UPDATE announcement "
				+ " SET "
				+ " courseId = :courseId, "
				+ " programId = :programId, "
				+ " announcementType = :announcementType, "
				+ " subject = :subject, "
				+ " filePath = :filePath, "
				+ " filePreviewPath = :filePreviewPath, "
				+ " description = :description, "
				+ " startDate = :startDate, "
				+ " endDate = :endDate, "
				+ " sendEmailAlert = :sendEmailAlert, "
				+ " sendSmsAlert = :sendSmsAlert, "
				+ " sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ " sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ " campusId = :campusId ,"
				+ " acadYear = :acadYear ,"
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate, announcementSubType=:announcementSubType,acadSession=:acadSession, "
				+ " examMonth=:examMonth,examYear=:examYear "
				+ " WHERE id = :id ";

		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Announcement> getAnnouncementsByCourses(List<Long> cId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", cId);
		List<Announcement> employees = getNamedParameterJdbcTemplate()
				.query("SELECT * FROM announcement where courseId IN (:ids) or announcementType = 'INSTITUTE' and active = 'Y'",
						params,
						BeanPropertyRowMapper.newInstance(Announcement.class));

		return employees;
	}

	public Page<Announcement> getLibraryAnnouncement(int pageNo, int pageSize) {

		String sql = " select * from announcement a where announcementType = 'LIBRARY' "
				+ " and a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";
		String countSql = " select count(*) from announcement where announcementType = 'LIBRARY' "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";

		return findAllSQL(sql, countSql, new Object[] {}, pageNo, pageSize);
	}

	public Page<Announcement> getCounselorAnnouncement(int pageNo, int pageSize) {

		String sql = " select * from announcement a where announcementType = 'Counselor' "
				+ " and a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";
		String countSql = " select count(*) from announcement where announcementType = 'Counselor' "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";

		return findAllSQL(sql, countSql, new Object[] {}, pageNo, pageSize);
	}

	public Page<Announcement> getAnnouncementsByStudentCoursesAndSubtype(
			List<Long> cId, int pageNo, int pageSize, String announcementSubType) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", cId);
		params.put("announcementSubType", announcementSubType);

		String sql = " SELECT * from announcement a where a.courseId in (:ids) and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' and announcementSubType = (:announcementSubType) order by a.endDate";

		String countsql = " SELECT count(*) from announcement a where a.courseId in (:ids) and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' and announcementSubType = (:announcementSubType) order by a.endDate";

		return paginationHelper.fetchPages(getNamedParameterJdbcTemplate(),
				countsql, sql, params, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Announcement> getAnnouncementsByAcadSessionYear(Long programId,
			String acadSession) {

		String sql = " SELECT * from announcement a where a.programId = ? and a.acadSession = ?  and a.active='Y' order by a.createdDate desc ";

		return findAllSQL(sql, new Object[] { programId, acadSession });
	}

	public Page<Announcement> findAnnouncementsReplacement(Long programId,
			Long courseId, int pageNo, int pageSize) {

		String sql = "Select * from "
				+ getTableName()
				+ "   where  (programId =? and announcementType='Course' and courseId=? ) and active ='Y' order by id desc";

		String sql2 = "Select count(*) from "
				+ getTableName()
				+ "   where  (programId =? and announcementType='Course' and courseId=? ) and active ='Y' order by id desc";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql,
				new Object[] { programId, courseId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public Page<Announcement> getInstitutionalAnnouncement(int pageNo,
			int pageSize) {

		String sql = " select * from announcement a where announcementType = 'INSTITUTE' "
				+ " and a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";
		String countSql = " select count(*) from announcement where announcementType = 'INSTITUTE' "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";

		return findAllSQL(sql, countSql, new Object[] {}, pageNo, pageSize);
	}

	public Page<Announcement> getInstitutionalAnnouncementBySubtype(int pageNo,
			int pageSize, String subtype) {
		String sql = " select * from announcement a where announcementType = 'INSTITUTE' "
				+ " and a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' and announcementSubType = ?";
		String countSql = " select count(*) from announcement where announcementType = 'INSTITUTE' "
				+ " and a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' and announcementSubType = ? order by a.endDate";

		return findAllSQL(sql, countSql, new Object[] { subtype }, pageNo,
				pageSize);

	}

	public Page<Announcement> getAnnouncementsByStudentCourses(List<Long> cId,
			int pageNo, int pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", cId);

		String sql = " SELECT * from announcement a where a.courseId in (:ids) and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";

		String countsql = " SELECT count(*) from announcement a where a.courseId in (:ids) and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' order by a.endDate";

		return paginationHelper.fetchPages(getNamedParameterJdbcTemplate(),
				countsql, sql, params, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Announcement> findAnnouncmentsByProgramId(Long programId) {
		String sql = "select a.* from announcement a, program p where a.programId=p.id and "
				+ "p.id=? and a.active ='Y' order by a.createdDate desc";
		return findAllSQL(sql, new Object[] { programId });
	}

	public List<Announcement> getAnnouncementsByCoursesLIMIT(List<Long> cId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", cId);
		String sql = "SELECT * FROM announcement where (courseId IN (:ids) or announcementType='INSTITUTE')and ((SYSDATE() > startDate and SYSDATE() < endDate ) or (SYSDATE() = startDate or SYSDATE() = endDate )) and active ='Y' ORDER By endDate LIMIT 6";

		List<Announcement> employees = getNamedParameterJdbcTemplate().query(
				sql, params,
				BeanPropertyRowMapper.newInstance(Announcement.class));

		return employees;
	}

	public List<Announcement> getAnnouncementCountDashboard(String username,
			String acadMonth, String acadYear) {

		String sql = " SELECT courseId ,count(*) as count from announcement where courseId in (SELECT c.id FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ " where uc.username = ?  and uc.acadMonth = ? "
				+ " and uc.acadYear = ? AND c.active = 'Y'  and uc.active = 'Y' ) and "
				+ " startDate  <= sysdate() and endDate  >= sysdate()  and"
				+ " active='Y' group by courseId  ";

		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public List<Announcement> getAnnouncementCountDashboard(String username) {

		String sql = " SELECT courseId ,count(*) as count from announcement where courseId in (SELECT c.id FROM course c INNER JOIN user_course uc on uc.courseId = c.id "
				+ " where uc.username = ?  "
				+ " AND c.active = 'Y' and uc.active = 'Y' ) and "
				+ " startDate  <= sysdate() and endDate  >= sysdate()  and"
				+ " active='Y' group by courseId  ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Announcement> getAnnouncementsByCoursesAndInstitute(
			Set<Long> courseIdList, String userName, String programId) {
		String sql = "select *,CONCAT(u.firstname,' ',u.lastname) as 'fullName', a.createdDate as createdDate from "
				+ " announcement a,user_course uc,users u where   uc.username = u.username   "
				+ " and sysdate() >= a.startDate  and sysdate() <= a.endDate  and uc.username=:userName and a.active='Y' and ( (uc.courseId in ( :courseIdList) and a.courseId = uc.courseId )  "
				+ " OR( a.announcementType='Institute'  and a.active='Y' ) "
				+ " OR( a.announcementType='PROGRAM'  and a.active='Y' and a.programId = :programId ))  "
				+ " group by a.id " + " order by a.createdDate desc";

		MapSqlParameterSource mapSource = new MapSqlParameterSource();
		mapSource.addValue("courseIdList", courseIdList);
		mapSource.addValue("userName", userName);
		mapSource.addValue("programId", programId);
		return queryInClause(sql, mapSource);
	}

	public List<Announcement> getAnnouncementsByCoursesAndAcadSessionAndInstitute(
			Set<Long> courseIdList, String userName, String programId,
			String acadSession) {
		String sql = "select *,CONCAT(u.firstname,' ',u.lastname) as 'fullName', a.createdDate as createdDate , a.campusId as campusId from "
				+ " announcement a,user_course uc,users u where   uc.username = u.username   "
				+ " and sysdate() >= a.startDate  and sysdate() <= a.endDate  and uc.username=:userName and a.active='Y' and ( (uc.courseId in ( :courseIdList) and a.courseId = uc.courseId )  "
				+ " OR( a.announcementType='Institute'  and a.active='Y' ) "
				+ " OR( a.announcementType='TIMETABLE'  and a.active='Y' and a.programId = :programId and a.acadSession=:acadSession ) "
				+ " OR( a.announcementType='PROGRAM'  and a.active='Y' and a.programId = :programId and a.acadSession=:acadSession))  "
				+ " group by a.id " + " order by a.createdDate desc";

		MapSqlParameterSource mapSource = new MapSqlParameterSource();
		mapSource.addValue("courseIdList", courseIdList);
		mapSource.addValue("userName", userName);
		mapSource.addValue("programId", programId);
		mapSource.addValue("acadSession", acadSession);
		return queryInClause(sql, mapSource);
	}

	public List<Announcement> announcementsByCoursesList(
			Set<Long> courseIdList, String userName) {
		String sql = " select *,CONCAT(u.firstname,' ',u.lastname) as 'fullName', a.createdDate as createdDate from "
				+ " announcement a,user_course uc,users u where   uc.username = u.username   "
				+ " and sysdate() >= a.startDate  and sysdate() <= a.endDate  and uc.username=:userName and a.active='Y' and  (uc.courseId in ( :courseIdList) and a.courseId = uc.courseId ) and uc.active='Y'  "
				+ " group by a.id " + " order by a.createdDate desc";

		MapSqlParameterSource mapSource = new MapSqlParameterSource();
		mapSource.addValue("courseIdList", courseIdList);
		mapSource.addValue("userName", userName);
		return queryInClause(sql, mapSource);
	}

	/*
	 * public Page<Announcement> getAnnouncementsByCoursesP(List<Long> cId, int
	 * pageNo, int pageSize) {
	 * 
	 * Map<String, Object> params = new HashMap<String, Object>();
	 * params.put("ids", cId);
	 * 
	 * String sql =
	 * " SELECT * from announcement a where a.courseId in (:ids) and " +
	 * " a.startDate  <= sysdate() " +
	 * " and a.endDate  >= sysdate()  and a.active='Y' "
	 * 
	 * + " or ( a.announcementType='Institute' and " +
	 * " a.startDate  <= sysdate() " +
	 * " and a.endDate  >= sysdate()  and a.active='Y'  ) order by a.createdDate desc "
	 * ;
	 * 
	 * String countsql =
	 * " SELECT count(*) from announcement a where a.courseId in (:ids) and " +
	 * " a.startDate  <= sysdate() " +
	 * " and a.endDate  >= sysdate()  and a.active='Y' "
	 * 
	 * + " or ( a.announcementType='Institute' and " +
	 * " a.startDate  <= sysdate() " +
	 * " and a.endDate  >= sysdate()  and a.active='Y' ) order by a.createdDate desc "
	 * ;
	 * 
	 * return paginationHelper.fetchPages(getNamedParameterJdbcTemplate(),
	 * countsql, sql, params, pageNo, pageSize,
	 * BeanPropertyRowMapper.newInstance(genericType)); }
	 */

	public Page<Announcement> getAnnouncementsByCoursesP(List<Long> cId,
			int pageNo, int pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", cId);

		String sql = " SELECT * from announcement a where a.courseId in (:ids) and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' "

				+ " or ( a.announcementType='Institute' and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y'  ) "
				+ " or ( a.announcementType='LIBRARY' and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y'  ) "
				+ " or ( a.announcementType='COUNSELOR' and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y'  ) order by a.createdDate desc ";

		String countsql = " SELECT count(*) from announcement a where a.courseId in (:ids) and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' "

				+ " or ( a.announcementType='Institute' and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' ) "

				+ " or ( a.announcementType='LIBRARY' and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' ) "

				+ " or ( a.announcementType='COUNSELOR' and "
				+ " a.startDate  <= sysdate() "
				+ " and a.endDate  >= sysdate()  and a.active='Y' ) order by a.createdDate desc ";

		return paginationHelper.fetchPages(getNamedParameterJdbcTemplate(),
				countsql, sql, params, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Announcement> findByCourse(Long courseId) {
		final String sql = "SELECT a.* FROM " + getTableName() + " a"
				+ " where a.courseId = ? " + " and a.active = 'Y' "
				+ " order by id";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public void updateAnnouncementToMakeInactive(Integer id) {
		executeUpdateSql("Update announcement set active = 'N' where id= ?",
				new Object[] { id });
	}

	public void updateInactiveAnnouncementByCourseId(Long courseId) {
		executeUpdateSql(
				" Update announcement set active = 'N' where courseId= ? ",
				new Object[] { courseId });
	}

	/*
	 * public Page<Announcement> findAnnouncementsReplacement(Long programId,
	 * int pageNo, int pageSize) {
	 * 
	 * String sql = "Select * from " + getTableName() +
	 * "   where  (programId =? or announcementType='Institute') and active ='Y' order by id desc"
	 * ;
	 * 
	 * String sql2 = "Select count(*) from " + getTableName() +
	 * "   where  (programId =? or announcementType='Institute') and active ='Y' order by id desc"
	 * ;
	 * 
	 * return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new
	 * Object[] { programId }, pageNo, pageSize,
	 * BeanPropertyRowMapper.newInstance(genericType)); }
	 */

	public Page<Announcement> findAnnouncementsReplacement(Long programId,
			int pageNo, int pageSize) {

		String sql = "Select * from "
				+ getTableName()
				+ "   where  (programId =? or announcementType='Institute' or announcementType='LIBRARY' OR announcementType = 'COUNSELOR') and active ='Y' order by id desc";

		String sql2 = "Select count(*) from "
				+ getTableName()
				+ "   where  (programId =? or announcementType='Institute' or announcementType='LIBRARY' OR announcementType = 'COUNSELOR') and active ='Y' order by id desc";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql,
				new Object[] { programId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Announcement> getNoOfAnnouncementStats(String fromDate,
			String toDate) {

		String sql = " select * from announcement a where a.lastModifiedDate  BETWEEN  ? and ? and a.active='Y' ";

		return findAllSQL(sql, new Object[] { fromDate, toDate });
	}
	
	public Page<Announcement> getAnnouncementsByCoursesAndAcadSessionAndInstitute(
			List<String> courseIdList, String userName, String programId,
			int pageNo, int pageSize) {
		logger.info("courseIdList ---- " + courseIdList);
		String sql = "select a.* from "
				+ " announcement a,user_course uc,users u where   uc.username = u.username   "
				+ " and sysdate() >= a.startDate  and sysdate() <= a.endDate  and uc.username = :userName and a.active='Y' and ( (uc.courseId in ( :courseIdList) and a.courseId = uc.courseId )  "
				+ " OR( a.announcementType='Institute'  and a.active='Y' ) "
				+ " OR( a.announcementType='PROGRAM'  and a.active='Y' and a.programId = :programId ))  "
				+ " group by a.id " + " order by a.createdDate desc";
		String sql2 = "select count(*) from announcement where id IN  (select a.id from "
				+ " announcement a,user_course uc,users u where   uc.username = u.username   "
				+ " and sysdate() >= a.startDate  and sysdate() <= a.endDate  and uc.username = :userName and a.active='Y' and ( (uc.courseId in ( :courseIdList) and a.courseId = uc.courseId )  "
				+ " OR( a.announcementType='Institute'  and a.active='Y' ) "
				+ " OR( a.announcementType='PROGRAM'  and a.active='Y' and a.programId = :programId ))  "
				+ " group by a.id " + " order by a.createdDate desc )";

		MapSqlParameterSource mapSource = new MapSqlParameterSource();
		Map<String, Object> param = new HashMap<String, Object>();
		mapSource.addValue("courseIdList", courseIdList);
		mapSource.addValue("userName", userName);
		mapSource.addValue("programId", programId);
		
		param.put("courseIdList", courseIdList);
		param.put("userName", userName);
		param.put("programId", programId);
		logger.info("sql1 --- " + sql);
		logger.info("sql2 ---- " + sql2);
		/*
		 * return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql, new
		 * Object[] {}, pageNo, pageSize,
		 * BeanPropertyRowMapper.newInstance(genericType));
		 */

		return paginationHelper.fetchPages(getNamedParameterJdbcTemplate(),
				sql2, sql, param, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	public List<Announcement> getAnnouncementByUsernameForApp(String username) {

		/*
		 * String sql =
		 * " select a.id, a.courseId, a.programId, a.announcementType, a.subject, "
		 * + " a.description, a.startDate, a.endDate, a.active, a.createdDate "
		 * +
		 * " from announcement a inner join users u on (a.programId = u.programId "
		 * +
		 * " or a.programId is null)  and u.username = ? where a.endDate > now() "
		 * +
		 * " and a.announcementType In ('COURSE', 'PROGRAM', 'INSTITUTE', 'LIBRARY') "
		 * ;
		 */

		String sql = " select a.id, a.courseId, a.programId, a.announcementType, a.subject, "
				+ " a.description, a.startDate, a.endDate, a.active, a.createdDate, a.filePath "
				+ " from announcement a inner join users u on (a.programId = u.programId "
				+ " or a.programId is null)  and u.username = ? where a.endDate >= CURDATE() and a.startDate <= CURDATE()"
				+ " and a.announcementType In ('COURSE', 'PROGRAM', 'INSTITUTE', 'LIBRARY') order by a.createdDate desc ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Announcement> getInstituteAnnouncement() {

		String sql = " select * from announcement a where a.announcementType = 'INSTITUTE' "
				+ "and a.startDate  <= CURDATE()  and a.endDate  >= CURDATE() and a.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<Announcement> getLibraryAnnouncement() {

		String sql = " select * from announcement a where a.announcementType = 'LIBRARY' "
				+ "and a.startDate  <= CURDATE()  and a.endDate  >= CURDATE() and a.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<Announcement> getCounselorAnnouncement() {

		String sql = " select * from announcement a where a.announcementType = 'COUNSELOR' "
				+ "and a.startDate  <= CURDATE()  and a.endDate  >= CURDATE() and a.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<Announcement> getProgramAnnouncementByProgramId(String programId) {

		String sql = " select * from announcement a where a.announcementType = 'PROGRAM' "
				+ "and a.startDate  <= CURDATE()  and a.endDate  >= CURDATE() and a.active = 'Y' and a.programId = ? ";

		return findAllSQL(sql, new Object[] { programId });
	}
	public List<Announcement> getTimeTableByProgramId(String acadSession,String programId  ) {

		String sql = " select * from announcement a where a.announcementType = 'TIMETABLE' "
				+ "and a.startDate  <= CURDATE()  and a.endDate  >= CURDATE() and a.active = 'Y' and a.acadSession=? and a.programId = ? ";

		return findAllSQL(sql, new Object[] { acadSession,programId });
	}

	public List<Announcement> getCourseAnnouncementByCourseIds(List<Long> cId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", cId);
		List<Announcement> employees = getNamedParameterJdbcTemplate()
				.query("select * from announcement a where a.announcementType = 'COURSE' and a.startDate  <= CURDATE()  and a.endDate  >= CURDATE() and  a.active = 'Y' and a.courseId in (:ids)",
						params,
						BeanPropertyRowMapper.newInstance(Announcement.class));

		return employees;
	}

	public int insertTimeTableStudent(String username,String announcement_id  ) {

		String sql = "insert into timetable_result(username,announcement_id) values (?,?)";

		return getJdbcTemplate().update(sql, new Object[]{username,announcement_id});
	}
	
	public Announcement getTimeTableStudent(String username,String announcement_id) {

		String sql = " select * from timetable_result  where username=? and announcement_id=?";

		return findOneSQL(sql, new Object[] { username, announcement_id });
	}
	
	public List<Announcement> getStudentReport(Long id) {
		
		String sql = "select tr.username, a.subject from timetable_result tr,announcement a where tr.announcement_id=a.id and a.id=? ";
		return findAllSQL(sql, new Object[] {id});
		
	}
	
	public List<Announcement> getExamTimetableForApp(String username){
		String sql = "select a.* from announcement a, users u where a.announcementType = 'TIMETABLE' and a.startDate  <= CURDATE() "
				+ "and a.endDate  >= CURDATE() and a.active = 'Y' "
				+ "and username = ? and a.programId = u.programId and u.acadSession = a.acadSession ";
		return findAllSQL(sql, new Object[] {username});
	}
	
	public int addNotification(final Announcement announcement) {
		String sql = "INSERT INTO notification_support_admin (announcementTitle, startDate, endDate,announcementDesc, "
				+ "  active,createdDate,createdBy,lastModifiedDate, lastModifiedBy) "
				+ " VALUES (:announcementTitle,:startDate,:endDate,:announcementDesc, 'Y', now(), 'CA', now(),'CA')  ";
		return updateSQL(announcement, sql);
	}
	
	public List<Announcement> getAnnouncementBySupportAdmin() {


		String sql = "select n.announcementTitle,n.startDate,n.endDate,n.announcementDesc from notification_support_admin n "
				+ "where n.startDate<=SYSDATE() and n.endDate>= SYSDATE() and n.active='Y'";
		
			return findAllSQL(sql, new Object[] {  });
	}

}
