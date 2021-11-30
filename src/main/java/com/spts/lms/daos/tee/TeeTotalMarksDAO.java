package com.spts.lms.daos.tee;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.tee.TeeTotalMarks;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("teeTotalMarksDAO")
public class TeeTotalMarksDAO extends BaseDAO<TeeTotalMarks> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "tee_total_marks";
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		String sql = "Insert into " + getTableName()
				+ " ( username,teeId,teeTotalMarks,teeScaledMarks,saveAsDraft,finalSubmit,"
				+ " active,remarks,isAbsent,createdBy,createdDate,lastModifiedBy,lastModifiedDate,isQueryApproved) values"
				+ " (:username,:teeId,:teeTotalMarks,:teeScaledMarks,:saveAsDraft,:finalSubmit,"
				+ " :active,:remarks,:isAbsent,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:isQueryApproved) "

				+ "  ON DUPLICATE KEY UPDATE " + "  teeTotalMarks=:teeTotalMarks,teeScaledMarks=:teeScaledMarks,"
				+ " saveAsDraft=:saveAsDraft,finalSubmit=:finalSubmit,"
				+ " active=:active,remarks=:remarks,isAbsent=:isAbsent,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate,isQueryApproved=:isQueryApproved ";

		return sql;
	}

//	public List<TeeTotalMarks> getTeeTotalMarksByUsername(String username) {
//
//		String sql = "select icm.`*`,icm.teeId as teeIdStr,t.publishedDate,t.externalMarks,CEIL(icm.teeTotalMarks) as teeTotalMarks,"
//				+ " (case when CEIL(icm.teeTotalMarks) >= CONVERT(t.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) as passFailStatus,"
//				+ " c.moduleName,t.acadYear,t.acadSession,t.moduleId from tee t,tee_total_marks icm,course c where t.id=icm.teeId and icm.username = ?"
//				+ " and t.active = 'Y' and t.moduleId=c.moduleId and icm.finalSubmit = 'Y' AND icm.active= 'Y' and t.isPublished = 'Y' group by t.id ";
//
//		return findAllSQL(sql, new Object[] { username });
//	}
//	
//	public String checkWhetherGradingStartOrNotP(String teeId) {
//
//		String sql = " select (case when (count(distinct(ttm.id))) > 0 then 't' else 'f' end) as checkGrade from "
//				+ " tee_total_marks ttm,tee t where "
//				+ " ttm.teeId=t.id and t.parentTeeId = ? ";
//
//		return getJdbcTemplate().queryForObject(sql, String.class,
//				new Object[] { teeId });
//	}
//
//	public String checkWhetherGradingStartOrNot(String teeId) {
//
//		String sql = " select (case when (count(distinct(ttm.id))) > 0 then 't' else 'f' end) as checkGrade from "
//				+ " tee_total_marks ttm,ttm t where "
//				+ " ttm.teeId=t.id and t.id = ? ";
//
//		return getJdbcTemplate().queryForObject(sql, String.class,
//				new Object[] { teeId });
//	}
//	
//	public List<TeeTotalMarks> getAllTeeTotalMarksByTeeId(long teeId) {
//		String sql = "select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, " + 
//				"icm.`*`,i.publishedDate,(case when icm.teeScaledMarks is not null then i.scaledMarks  " + 
//				"else i.externalMarks  end) " + 
//				"as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  " + 
//				"else CEIL(icm.teeTotalMarks)  end) " + 
//				"as teeTotalMarks, " + 
//				"(case when icm.teeScaledMarks is not null then( " + 
//				"case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) " + 
//				"else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) " + 
//				"end) as passFailStatus, " + 
//				"c.moduleName,i.acadYear,i.acadSession,i.moduleId from tee i,tee_total_marks icm,course c,users u " + 
//				"where i.id=icm.teeId and icm.teeId = 3 and i.active = 'Y' and i.moduleId=c.moduleId and u.username=icm.username " + 
//				"and icm.active= 'Y' group by i.id,icm.username ";
//		return findAllSQL(sql, new Object[] { teeId });
//	}
//	
//	public List<TeeTotalMarks> getTeeTotalMarksByTeeIdBatchwise(String teeId,String username) {
//		
//		String sql="select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, " + 
//				"icm.`*`,i.publishedDate,(case when icm.teeScaledMarks is not null then i.scaledMarks  " + 
//				"else i.externalMarks  end) " + 
//				"as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  " + 
//				"else CEIL(icm.teeTotalMarks)  end) " + 
//				"as teeTotalMarks, " + 
//				"(case when icm.teeScaledMarks is not null then( " + 
//				"case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) " + 
//				"else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) " + 
//				"end) as passFailStatus,  " + 
//				"c.moduleName,i.acadYear,i.acadSession,i.moduleId from tee i,tee_total_marks icm,course c,users u, tee_student_batchwise isb " + 
//				"where i.id=icm.teeId and icm.teeId = ? and i.active = 'Y' and i.moduleId=c.moduleId and u.username=icm.username and isb.teeId = i.id " + 
//				"and isb.username = icm.username and isb.facultyId = ? " + 
//				"and icm.active= 'Y' group by i.id,icm.username";
//		return findAllSQL(sql, new Object[] { teeId,username });
//	}

	public int updateRaiseQuery(String icaId, String username, String query) {

		String sql = " update " + getTableName()
				+ " set isQueryRaise = 'Y',lastModifiedBy = ?,lastModifiedDate=?,query=? "
				+ "  where teeId = ? and username =? ";

		return getJdbcTemplate().update(sql, new Object[] { username, Utils.getInIST(), query, icaId, username });
	}

	public List<TeeTotalMarks> getTeeTotalMarksByParamForFaculty(String acadYear, String username) {
		String sql = "select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
				+ "(case when icm.teeScaledMarks is not null then i.scaledMarks  else i.externalMarks  end) "
				+ "as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  "
				+ "else CEIL(icm.teeTotalMarks)  end) as teeTotalMarks, "
				+ "(case when icm.teeScaledMarks is not null then( case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "end) as passFailStatus,  "
				+ "c.moduleName,c.acadYearCode,c.acadSession,c.moduleId,c.moduleAbbr from tee i,tee_total_marks icm,course c,users u "
				+ "where i.id=icm.teeId  and i.active = 'Y' and i.moduleId=c.moduleId and  "
				+ "i.acadYear=? and FIND_IN_SET(?,i.assignedFaculty) and u.username=icm.username "
				+ "AND i.acadYear=c.acadYearCode  " + "and i.moduleId=c.moduleId and c.active = 'Y' "
				+ "AND icm.active= 'Y' group by i.id,icm.username  ";

		return findAllSQL(sql, new Object[] { acadYear, username });
	}

	// 10-04-2020

	public List<TeeTotalMarks> getTeeTotalMarksByUsername(String username) {

		String sql = "select icm.*,i.publishedDate,(case when icm.teeScaledMarks is not null then i.scaledMarks  else i.externalMarks  end) "
				+ "as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  else CEIL(icm.teeTotalMarks)  end) "
				+ "as teeTotalMarks, " + "(case when icm.teeScaledMarks is not null then( "
				+ "case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "end) as passFailStatus,  "
				+ "c.moduleName,i.acadYear,i.acadSession,i.moduleId,icm.teeId as teeIdStr from tee i,tee_total_marks icm,course c "
				+ "where i.id=icm.teeId and icm.username = ? and i.active = 'Y' and i.moduleId=c.moduleId and icm.finalSubmit = 'Y' "
				+ "AND icm.active= 'Y' and i.isPublished = 'Y' and (i.isNonEventModule <> 'Y'  OR i.isNonEventModule is null) group by i.id  ";

		return findAllSQL(sql, new Object[] { username });
	}

	public String checkWhetherGradingStartOrNotP(String teeId) {

		String sql = " select (case when (count(distinct(ttm.id))) > 0 then 't' else 'f' end) as checkGrade from "
				+ " tee_total_marks ttm,tee t where " + " ttm.teeId=t.id and t.parentTeeId = ? ";

		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { teeId });
	}

	public String checkWhetherGradingStartOrNot(String teeId) {

		String sql = " select (case when (count(distinct(ttm.id))) > 0 then 't' else 'f' end) as checkGrade from "
				+ " tee_total_marks ttm,tee t where " + " ttm.teeId=t.id and t.id = ? ";

		return getJdbcTemplate().queryForObject(sql, String.class, new Object[] { teeId });
	}

	public List<TeeTotalMarks> getAllTeeTotalMarksByTeeId(long teeId) {
		String sql = "select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, "
				+ "icm.*,i.publishedDate,(case when icm.teeScaledMarks is not null then i.scaledMarks  "
				+ "else i.externalMarks  end) "
				+ "as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  "
				+ "else CEIL(icm.teeTotalMarks)  end) " + "as teeTotalMarks, "
				+ "(case when icm.teeScaledMarks is not null then( "
				+ "case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "end) as passFailStatus, "
				+ "c.moduleName,i.acadYear,i.acadSession,i.moduleId from tee i,tee_total_marks icm,course c,users u "
				+ "where i.id=icm.teeId and icm.teeId = ? and i.active = 'Y' and i.moduleId=c.moduleId and u.username=icm.username "
				+ "and icm.active= 'Y' group by i.id,icm.username ";
		return findAllSQL(sql, new Object[] { teeId });
	}

	public List<TeeTotalMarks> getTeeTotalMarksByTeeIdBatchwise(String teeId, String username) {

		String sql = "select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo, "
				+ "icm.*,i.publishedDate,(case when icm.teeScaledMarks is not null then i.scaledMarks  "
				+ "else i.externalMarks  end) "
				+ "as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  "
				+ "else CEIL(icm.teeTotalMarks)  end) " + "as teeTotalMarks, "
				+ "(case when icm.teeScaledMarks is not null then( "
				+ "case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
				+ "end) as passFailStatus,  "
				+ "c.moduleName,i.acadYear,i.acadSession,i.moduleId from tee i,tee_total_marks icm,course c,users u, tee_student_batchwise isb "
				+ "where i.id=icm.teeId and icm.teeId = ? and i.active = 'Y' and i.moduleId=c.moduleId and u.username=icm.username and isb.teeId = i.id "
				+ "and isb.username = icm.username and isb.facultyId = ? "
				+ "and icm.active= 'Y' group by i.id,icm.username";
		return findAllSQL(sql, new Object[] { teeId, username });
	}

	public List<String> getDistinctUsernamesByActiveTeeId(String teeId) {
		String sql = " select distinct username from " + getTableName() + " where active = 'Y' and teeId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { teeId });
	}

	public List<String> getDistinctSubmittedUsernamesByActiveTeeId(String teeId) {
		String sql = " select distinct username from " + getTableName()
				+ " where active = 'Y' and teeId = ? and finalSubmit = 'Y' ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { teeId });
	}

	public List<TeeTotalMarks> getIsReevalTeeUsername(String teeId) {

		String sql = "select itm.username,itm.isQueryApproved,itm.remarks from tee_total_marks itm , tee_queries iq where itm.teeId=iq.teeId "
				+ "and itm.teeId=? and itm.isQueryRaise='Y' and iq.isApproved='Y' and (iq.isReEvaluated <> 'Y' or iq.isReEvaluated is null)";
		return findAllSQL(sql, new Object[] { teeId });
	}

	// round off using ceil function
	public List<TeeTotalMarks> getTeeTotalMarksByParam(String acadYear, String acadSession, String programId,
			String campusId) {

		String programIdL = "%" + programId + "%";
		if (campusId != null) {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate, "
					+ "(case when icm.teeScaledMarks is not null then i.scaledMarks  else i.externalMarks  end) "
					+ "as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)  "
					+ "else CEIL(icm.teeTotalMarks)  end) " + "as teeTotalMarks, "
					+ "(case when icm.teeScaledMarks is not null then( case when CEIL(icm.teeScaledMarks) >= CONVERT(i.internalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end) "
					+ "end) as passFailStatus, "
					+ "c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,c.moduleAbbr from tee i,tee_total_marks icm,course c,users u "
					+ "where i.id=icm.teeId  and i.active = 'Y' and i.moduleId=c.moduleId and i.programId like ? "
					+ "and i.acadSession=?   and i.acadYear=? and i.campusId=? and i.isSubmitted='Y' "
					+ "and icm.finalSubmit = 'Y' and u.username=icm.username "
					+ "and c.programId=? AND i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
					+ "and i.moduleId=c.moduleId and i.campusId=c.campusId and c.active = 'Y' "
					+ "AND icm.active= 'Y' group by i.id,icm.username";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, campusId, programId });
		} else {

			String sql = " select concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,icm.*,i.publishedDate,  "
					+ "(case when icm.teeScaledMarks is not null then i.scaledMarks  else i.externalMarks  end)  "
					+ "as externalMarks, (case when icm.teeScaledMarks is not null then CEIL(icm.teeScaledMarks)   "
					+ "else CEIL(icm.teeTotalMarks)  end)  " + "as teeTotalMarks,  "
					+ "(case when icm.teeScaledMarks is not null then( case when CEIL(icm.teeScaledMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end)  "
					+ "else (case when CEIL(icm.teeTotalMarks) >= CONVERT(i.externalPassMarks,decimal) then 'PASS' else 'FAIL' end)  "
					+ "end) as passFailStatus,   "
					+ "c.moduleName,c.acadYearCode as acadYear,c.acadSession,c.moduleId,c.moduleAbbr from tee i,tee_total_marks icm,course c,users u  "
					+ "where i.id=icm.teeId  and i.active = 'Y' and i.moduleId=c.moduleId and i.programId like ?  "
					+ "and i.acadSession=?   and i.acadYear=?  and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and u.username=icm.username  "
					+ "and c.programId=? AND i.acadYear=c.acadYearCode and i.acadSession=c.acadSession   "
					+ "and i.moduleId=c.moduleId and c.active = 'Y'  "
					+ "AND icm.active= 'Y' group by i.id,icm.username  ";

			return findAllSQL(sql, new Object[] { programIdL, acadSession, acadYear, programId });
		}
	}

	public List<TeeTotalMarks> getRaiseQueriesForTee(String acadYear, Collection<GrantedAuthority> auth,
			String username) {

		if (acadYear != null) {
			if (auth.contains(Role.ROLE_ADMIN)) {

				String sql = " select i.teeName,i.id as teeId,(case when itm.teeScaledMarks is not null then itm.teeScaledMarks "
						+ " else itm.teeTotalMarks  end) as teeTotalMarks, "
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,tee_total_marks itm,users u,course c,users u1, tee i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join tee_queries iq on iq.teeId=i.id "
						+ " where itm.teeId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
						+ " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?) and i.acadYear = ? AND i.active = 'Y' "
						+ " group by itm.username,itm.teeId ";

				return findAllSQL(sql, new Object[] { username, username, acadYear });

			} else if (auth.contains(Role.ROLE_FACULTY)) {

				String sql = " select i.teeName,i.id as teeId,(case when itm.teeScaledMarks is not null then itm.teeScaledMarks "
						+ " else itm.teeTotalMarks  end) as teeTotalMarks, "
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,tee_total_marks itm,users u,course c,users u1, tee i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join tee_queries iq on iq.teeId=i.id "
						+ " where itm.teeId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
						+ " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  "
						+ " and (FIND_IN_SET(?,i.assignedFaculty)) and i.acadYear = ? AND i.active = 'Y' "
						+ " group by itm.username,itm.teeId ";

				return findAllSQL(sql, new Object[] { username, acadYear });

			} else {
				return null;
			}
		} else {

			if (auth.contains(Role.ROLE_ADMIN)) {

				String sql = " select i.teeName,i.id as teeId,(case when itm.teeScaledMarks is not null then itm.teeScaledMarks "
						+ " else itm.teeTotalMarks  end) as teeTotalMarks, "
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,tee_total_marks itm,users u,course c,users u1, tee i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join tee_queries iq on iq.teeId=i.id "
						+ " where itm.teeId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
						+ " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  "
						+ " and (i.createdBy=? or i.lastModifiedBy= ?)  AND i.active = 'Y' "
						+ " group by itm.username,itm.teeId ";

				return findAllSQL(sql, new Object[] { username, username });
			}

			else if (auth.contains(Role.ROLE_FACULTY)) {

				String sql = " select i.teeName,i.id as teeId,(case when itm.teeScaledMarks is not null then itm.teeScaledMarks "
						+ " else itm.teeTotalMarks  end) as teeTotalMarks, "
						+ " i.acadYear,i.acadSession,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,u.email, p.programName, "
						+ " concat(c.moduleName,'(',c.moduleAbbr,')') as moduleName,itm.`query`,iq.isApproved,DATE_FORMAT(iq.createdDate, \"%M %e %Y\") as raiseQDate, "
						+ " itm.username,c1.courseName as division, GROUP_CONCAT(DISTINCT concat(u1.firstName,' ',u1.lastName,'(',u1.username,')')) "
						+ " as assignedFaculty "
						+ " from program p,tee_total_marks itm,users u,course c,users u1, tee i  "
						+ " left outer join course c1 on c1.id=i.eventId "
						+ " inner join tee_queries iq on iq.teeId=i.id "
						+ " where itm.teeId=i.id and itm.username=u.username and FIND_IN_SET(u1.username,i.assignedFaculty) "
						+ " and i.moduleId=c.moduleId and i.acadYear=c.acadYearCode and i.acadSession=c.acadSession "
						+ " and u.programId=p.id " + " and itm.isQueryRaise= 'Y'  "
						+ " and (FIND_IN_SET(?,i.assignedFaculty))  AND i.active = 'Y' "
						+ " group by itm.username,itm.teeId ";
				return findAllSQL(sql, new Object[] { username });

			} else {
				return null;
			}
		}

	}

	public int updateMarksFromSupportAdmin(TeeTotalMarks ttm) {
		if (null != ttm.getTeeScaledMarks()) {
			String sql = "update " + getTableName()
					+ " set teeTotalMarks = ?,teeScaledMarks = ?, remarks = ? where username = ? and teeId = ?";
			return getJdbcTemplate().update(sql, new Object[] { ttm.getTeeTotalMarks(), ttm.getTeeScaledMarks(),
					ttm.getRemarks(), ttm.getUsername(), ttm.getTeeId() });
		} else {
			String sql = "update " + getTableName()
					+ " set teeTotalMarks = ?, remarks = ? where username = ? and teeId = ?";
			return getJdbcTemplate().update(sql,
					new Object[] { ttm.getTeeTotalMarks(), ttm.getRemarks(), ttm.getUsername(), ttm.getTeeId() });
		}

	}

	public int deleteTeeTotalMarksByStudents(String teeId, List<String> studList) {

		Map<String, Object> mapper = new HashMap<>();

		mapper.put("teeId", teeId);
		mapper.put("studList", studList);

		String sql = " delete from " + getTableName() + " where teeId =:teeId and username in(:studList) ";
		return getNamedParameterJdbcTemplate().update(sql, mapper);
	}
	
	public int getNoOfStudentsForTee(Long icaId) {
		String sql =" select count(*) from "+getTableName()+" where teeId = ? and active ='Y' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}
	public int getCountOfTcsFlagSentForTee(Long icaId) {
		String sql =" select count(*) from "+getTableName()+" where teeId = ? and active ='Y' and flagTcs='S' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}

	public int checkIfSavedAsDraft(Long teeId) {
		String sql = "select count(*) from tee_total_marks where teeId=? and saveAsDraft='Y'";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {teeId});
	}
}
