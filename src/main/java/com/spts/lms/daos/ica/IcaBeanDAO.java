package com.spts.lms.daos.ica;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("icaBeanDAO")
public class IcaBeanDAO extends BaseDAO<IcaBean> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "ica";

	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into " + getTableName() + " ( icaName,"
				+ " icaDesc,moduleId,acadYear,programId,acadSession,campusId,internalMarks,externalMarks,totalMarks,"
				+ " assignedFaculty,eventId,isIcaDivisionWise,parentIcaId, "
				+ " scaledReq,scaledMarks,internalPassMarks,externalPassMarks,startDate,endDate,"
				+ " active,createdBy,createdDate,"
				+ " lastModifiedBy,lastModifiedDate, isNonEventModule,isCourseraIca,isPublishCompWise) values"
				+ " (:icaName,:icaDesc,:moduleId,:acadYear,:programId,:acadSession,:campusId,"
				+ " :internalMarks,:externalMarks,:totalMarks,"
				+ " :assignedFaculty,:eventId,:isIcaDivisionWise,:parentIcaId, "
				+ " :scaledReq,:scaledMarks,:internalPassMarks,:externalPassMarks,:startDate,:endDate,"
				+ " :active,:createdBy,"
				+ " :createdDate,:lastModifiedBy,:lastModifiedDate, :isNonEventModule,:isCourseraIca,:isPublishCompWise) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " update " + getTableName()
				+ " set icaName=:icaName , icaDesc=:icaDesc,internalMarks=:internalMarks,"
				+ " externalMarks=:externalMarks,assignedFaculty=:assignedFaculty,scaledReq=:scaledReq,"
				+ " scaledMarks=:scaledMarks,internalPassMarks=:internalPassMarks,"
				+ " externalPassMarks=:externalPassMarks,startDate=:startDate,endDate=:endDate,"
				+ " totalMarks=:totalMarks,lastModifiedBy=:lastModifiedBy,"
				+ " lastModifiedDate=:lastModifiedDate,isPublishCompWise=:isPublishCompWise " + " where id=:id  ";
		return sql;
	}

	public int updateIca(IcaBean ica) {
		SqlParameterSource parameterSource = getParameterSource(ica);
		String sql = " update " + getTableName()
				+ " set icaName=:icaName , icaDesc=:icaDesc,assignedFaculty=:assignedFaculty,endDate=:endDate,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return getNamedParameterJdbcTemplate().update(sql, parameterSource);
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into " + getTableName() + " ( icaName,"
				+ " icaDesc,moduleId,acadYear,programId,acadSession,campusId,internalMarks,externalMarks,totalMarks,assignedFaculty, "
				+ " scaledReq,scaledMarks,internalPassMarks,externalPassMarks,startDate,endDate,"
				+ " active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,isPublishCompWise) values"
				+ " (:icaName,:icaDesc,:moduleId,:acadYear,:programId,:acadSession,:campusId,"
				+ " :internalMarks,:externalMarks,:totalMarks,:assignedFaculty, "
				+ " :scaledReq,:scaledMarks,:internalPassMarks,:externalPassMarks,:startDate,:endDate,:active,:createdBy,"
				+ " :createdDate,:lastModifiedBy,:lastModifiedDate,:isPublishCompWise) "

				+ "  ON DUPLICATE KEY UPDATE "
				+ "  icaName=:icaName , icaDesc=:icaDesc,internalMarks=:internalMarks,externalMarks=:externalMarks,"
				+ " assignedFaculty=:assignedFaculty,scaledReq=:scaledReq,"
				+ " scaledMarks=:scaledMarks,internalPassMarks=:internalPassMarks,externalPassMarks=:externalPassMarks,"
				+ " startDate=:startDate,endDate=:endDate,totalMarks=:totalMarks,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate,isPublishCompWise=:isPublishCompWise ";

		return sql;
	}

	public List<IcaBean> findIcaListByProgram(String programId, String role, String username) {

		if (role.equals(Role.ROLE_ADMIN.name())) {
			/*
			 * String sql =
			 * " select distinct a.*,c.moduleName,c.acadSession,pc.campusName from course c,program p,ica a "
			 * + " left outer join program_campus pc on a.campusId=pc.campusId " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and p.id=? and a.active= 'Y' "
			 * ;
			 */
			/*
			 * String sql =
			 * " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear," +
			 * " concat(u.firstName,' ',u.lastName) as facultyName," +
			 * " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
			 * + " from course c,program p,ica a " +
			 * " left outer join ica_queries iq on a.id=iq.icaId  " +
			 * " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
			 * + " left outer join course c1 on a.eventId=c1.id " +
			 * " left outer join users u on a.assignedFaculty=u.username " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
			 * + " and a.acadYear=c.acadYear "
			 * 
			 * + " and a.parentIcaId is null "
			 * 
			 * +
			 * " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id"
			 * ;
			 */
			// acadYearCode
			String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
					+ " concat(u.firstName,' ',u.lastName) as facultyName,"
					+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
					+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
					+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
					+ " left outer join course c1 on a.eventId=c1.id "
					+ " left outer join users u on a.assignedFaculty=u.username "
					+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
					+ " and a.acadYear=c.acadYearCode "

					+ " and a.parentIcaId is null "

					+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id";

			return findAllSQL(sql, new Object[] { username, username });
		} else {
			/*
			 * String sql =
			 * " select distinct a.*,c.moduleName,c.acadSession,pc.campusName from course c,program p,ica a "
			 * + " left outer join program_campus pc on a.campusId=pc.campusId " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and a.assignedFaculty=? and a.active= 'Y' "
			 * ;
			 */
			String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,a.acadSession,c1.courseName,"
					+ " pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
					+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
					+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId "
					+ " left outer join course c1 on a.eventId=c1.id "
					+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
					+ " and a.acadYear = c.acadYearCode "

					+ " and a.active= 'Y' and (a.assignedFaculty = ?) group by a.id";

			return findAllSQL(sql, new Object[] { username });

		}
	}

	public IcaBean checkAlreadyExistICA(String moduleId, String acadYear, String campusId, String acadSession) {

		if (campusId != null) {
			String sql = " select * from " + getTableName()
					+ " where moduleId = ?  and acadYear =? and campusId = ? and acadSession = ? and active = 'Y' ";
			return findOneSQL(sql, new Object[] { moduleId, acadYear, campusId, acadSession });
		} else {
			String sql = " select * from " + getTableName()
					+ " where moduleId = ?  and acadYear =?  and acadSession = ? and active = 'Y' ";
			return findOneSQL(sql, new Object[] { moduleId, acadYear, acadSession });
		}
	}

	public List<IcaBean> checkAlreadyExistICAList(String moduleId, String acadYear, String campusId,
			String acadSession) {

		if (campusId != null) {
			String sql = " select * from " + getTableName()
					+ " where moduleId = ?  and acadYear =? and campusId = ? and acadSession = ? and active = 'Y' ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, campusId, acadSession });
		} else {
			String sql = " select * from " + getTableName()
					+ " where moduleId = ?  and acadYear =?  and acadSession = ? and active = 'Y' ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, acadSession });
		}
	}

	public List<IcaBean> getIcaComponents(Long icaId) {

		// String sql =
		// " select *,id as componentId from pre_def_ica_components where active = 'Y'
		// ";

		String sql = " select pd.componentName,pd.id as componentId,i.componentId as icaCompId,i.marks as icaCompMarks,i.sequenceNo  "
				+ " from pre_def_ica_components pd left outer join ica_components i on pd.id=i.componentId and i.icaId=? AND i.active='Y' "
				+ " WHERE pd.active = 'Y' ";

		return findAllSQL(sql, new Object[] { icaId });
	}

	public List<IcaBean> getComponentsForIca(Long icaId) {
		String sql = " select pd.componentName,pd.id as componentId,i.componentId as icaCompId,i.marks as icaCompMarks "
				+ " from pre_def_ica_components pd inner join ica_components i on pd.id=i.componentId and i.icaId=? AND i.active='Y' "
				+ " WHERE pd.active = 'Y' ";

		return findAllSQL(sql, new Object[] { icaId });
	}

	public IcaBean getComponentsByCompIdForIca(String icaId, String componentId) {
		String sql = " select pd.componentName,pd.id as componentId,i.componentId as icaCompId,i.marks as icaCompMarks "
				+ " from pre_def_ica_components pd inner join ica_components i on pd.id=i.componentId and i.icaId=? and i.componentId =?"
				+ " AND i.active='Y' " + " WHERE pd.active = 'Y' ";

		return findOneSQL(sql, new Object[] { icaId, componentId });
	}

	public int updateIcaToSubmitted(String icaId, Date lastModifiedDate) {

		String sql = " update " + getTableName() + " set isSubmitted = 'Y',lastModifiedDate = ?  where id = ? ";

		return getJdbcTemplate().update(sql, new Object[] { lastModifiedDate, icaId });

	}

	public List<IcaBean> getSubmittedIca(String username) {

		/*
		 * String sql =
		 * " select distinct a.*,c.moduleName as moduleName,c.acadYear,a.acadSession  "
		 * + " from course c,program p,ica a " +
		 * " left outer join program_campus pc on a.campusId=pc.campusId " +
		 * " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
		 * + " and a.isSubmitted='Y' " +
		 * " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) ";
		 */
		String sql = " select distinct a.*,c.moduleName as moduleName,a.acadYear,a.acadSession,"
				+ " c1.courseName,concat(u.firstname,' ',u.lastname) as facultyName "
				+ " from course c,program p,ica a " + " left outer join program_campus pc on a.campusId=pc.campusId "
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on u.username=a.assignedFaculty "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
				+ " and a.isSubmitted='Y' " + " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) ";

		return findAllSQL(sql, new Object[] { username, username });
	}

	public List<IcaBean> getSubmittedIcaForNonEvent(String username) {

//		String sql = " select distinct a.*,m.module_description as moduleName,a.acadYear,a.acadSession,"
//				+ " concat(u.firstname,' ',u.lastname) as facultyName "
//				+ " from module m,session_master sm,program p,ica a "
//				+ " left outer join program_campus pc on a.campusId=pc.campusId "
//				+ " left outer join users u on u.username=a.assignedFaculty "
//				+ " where m.module_id=a.moduleId and m.session_code = sm.sapSessionCode and "
//				+ " a.programId=p.id and sm.sapSessionText=a.acadSession and a.acadYear=m.acadyear "
//				+ " and a.isSubmitted='Y' and a.isNonEventModule = 'Y' "
//				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) ";

		String sql = " select distinct a.*,m.module_description as moduleName,a.acadYear,a.acadSession "
				+ " from module m,session_master sm,program p,ica a "
				+ " left outer join program_campus pc on a.campusId=pc.campusId "
				+ " where m.module_id=a.moduleId and m.session_code = sm.sapSessionCode and "
				+ " a.programId=p.id and sm.sapSessionText=a.acadSession and a.acadYear=m.acadyear "
				+ " and a.isSubmitted='Y' and a.isNonEventModule = 'Y' "
				+ " and (a.isPublishCompWise<> 'Y' or a.isPublishCompWise is null) "
				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) ";

		return findAllSQL(sql, new Object[] { username, username });
	}

	public List<IcaBean> getfacultyNameMap(String username) {

		String sql = " SELECT distinct i.assignedFaculty, GROUP_CONCAT(DISTINCT u.firstname,' ',u.lastname) as facultyName "
				+ " FROM users u,ica i WHERE FIND_IN_SET(u.username,i.assignedFaculty) and "
				+ " (i.createdBy = ? or i.lastModifiedBy = ?)  GROUP BY i.assignedFaculty ";
		return findAllSQL(sql, new Object[] { username, username });

	}

	public List<IcaBean> getIcaQueries(String username) {

		/*
		 * String sql = " select distinct a.*,c.moduleName as moduleName,c.acadYear," +
		 * " a.acadSession,iq.isApproved,iq.id as icaQueryId  from course c,program p,ica a "
		 * + " left outer join program_campus pc on a.campusId=pc.campusId " +
		 * " inner join ica_queries iq on a.id=iq.icaId " +
		 * " where c.moduleId=a.moduleId and a.programId=p.id " +
		 * " and c.acadSession=a.acadSession " + " and a.isSubmitted='Y' " +
		 * " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id"
		 * ;
		 */
		String sql = " select distinct a.*,c.moduleName as moduleName,a.acadYear,"
				+ " a.acadSession,iq.isApproved,iq.id as icaQueryId,iq.componentId,a.acadSession," + " c1.courseName"
				+ " from course c,program p,ica a " + " left outer join program_campus pc on a.campusId=pc.campusId "
				+ "  left outer join course c1 on a.eventId=c1.id " + " inner join ica_queries iq on a.id=iq.icaId "
				+ " where c.moduleId=a.moduleId and a.programId=p.id "
				+ " and (a.isCourseraIca ='Y' or c.acadSession=a.acadSession )" 
				+ " and (a.isSubmitted='Y' or a.isPublishCompWise='Y') "
				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) and "
				+ " a.isNonEventModule <> 'Y' group by a.id,iq.componentId";

		return findAllSQL(sql, new Object[] { username, username });
	}

	public List<IcaBean> getIcaQueriesForNonEvent(String username) {

		String sql = " select distinct a.*, m.module_description as moduleName,a.acadYear, a.acadSession,iq.isApproved, "
				+ " iq.id as icaQueryId,iq.componentId,a.acadSession from module m, session_master sm,program p,ica a "
				+ " left outer join program_campus pc on a.campusId=pc.campusId "
				+ " inner join ica_queries iq on a.id=iq.icaId "
				+ " where m.module_id=a.moduleId and a.programId=p.id and m.session_code = sm.sapSessionCode and "
				+ " sm.sapSessionText=a.acadSession and a.isSubmitted='Y' and a.active= 'Y' and a.isNonEventModule = 'Y' "
				+ " and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id,iq.componentId";

		return findAllSQL(sql, new Object[] { username, username });
	}

	public List<String> getSubmittedIcaIds(String username) {

		String sql = " select distinct a.id  from course c,program p,ica a "
				+ " left outer join program_campus pc on a.campusId=pc.campusId "
				+ " where c.moduleId=a.moduleId and a.programId=p.id "
				+ " and a.isSubmitted='Y' and a.isPublished <> 'Y' "
				+ " and (a.isPublishCompWise<> 'Y' or a.isPublishCompWise is null) "
				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) ";

		return getJdbcTemplate().queryForList(sql, String.class, new Object[] { username, username });
	}

	public int updateIcaToPublished(String icaId, Date lastModifiedDate, String publishedDate) {

		String sql = " update " + getTableName()
				+ " set isPublished = 'Y',lastModifiedDate = ?,publishedDate=?  where id = ? ";

		return getJdbcTemplate().update(sql, new Object[] { lastModifiedDate, publishedDate, icaId });

	}

	public int updateMultipleIcaToPublished(List<String> icaId, Date lastModifiedDate, String publishedDate) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("icaId", icaId);
		params.put("lastModifiedDate", lastModifiedDate);
		params.put("publishedDate", publishedDate);

		String sql = " update " + getTableName() + " set isPublished = 'Y',lastModifiedDate = :lastModifiedDate,"
				+ " publishedDate=:publishedDate  where id in(:icaId) ";

		return getNamedParameterJdbcTemplate().update(sql, params);

	}

	public List<String> getProgramList(String acadYear, String acadSession, String programId, String campusId) {

		programId = "%" + programId + "%";
		String sql = " select distinct i.programId from " + getTableName()
				+ " where i.acadYear=? and i.acadSession=? and i.programId like ? and i.campusId = ? ";

		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { acadYear, acadSession, programId, campusId });

	}

	public List<IcaBean> findIcaListByFacultyId(String role, String username) {

		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,a.acadSession,"
				+ " pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  " + " from course c,program p,ica a "
				+ " left outer join ica_queries iq on a.id=iq.icaId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "

				+ " and a.active= 'Y' and (a.assignedFaculty = ?) and a.isSubmitted<>'Y' group by a.id";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<IcaBean> findDivisionWiseIcaListByParentIca(String parentIcaId) {

		/*
		 * String sql =
		 * " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear," +
		 * " concat(u.firstName,' ',u.lastName) as facultyName," +
		 * " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
		 * + " from course c,program p,ica a " +
		 * " left outer join ica_queries iq on a.id=iq.icaId  " +
		 * " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
		 * + " left outer join course c1 on a.eventId=c1.id " +
		 * " left outer join users u on a.assignedFaculty=u.username " +
		 * " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
		 * + " and a.acadYear=c.acadYear"
		 * 
		 * + " and a.active= 'Y' and (a.parentIcaId = ?) group by a.id";
		 */

//AcadYear Code
		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
				+ " concat(u.firstName,' ',u.lastName) as facultyName,"
				+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
				+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
				+ " and a.acadYear=c.acadYearCode"

				+ " and a.active= 'Y' and (a.parentIcaId = ?) group by a.id";

		return findAllSQL(sql, new Object[] { parentIcaId });
	}

	public List<IcaBean> getIcaIdsByParentIcaIds(Long icaId) {

		String sql = " select * from " + getTableName() + " where parentIcaId = ? and active= 'Y' ";

		return findAllSQL(sql, new Object[] { icaId });
	}

	public List<IcaBean> getSubmittedIcaIdsByParentIcaIds(Long icaId) {

		String sql = " select * from " + getTableName()
				+ " where parentIcaId = ? and isSubmitted= 'Y' and active= 'Y' ";

		return findAllSQL(sql, new Object[] { icaId });
	}

	public List<IcaBean> icaListByParent(String parentIcaId) {

		String sql = " select * from " + getTableName() + " where parentIcaId = ? and active = 'Y' ";

		return findAllSQL(sql, new Object[] { parentIcaId });
	}

	public List<IcaBean> findIcaStatusListForAll() {

		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,c.acadYear,"
				+ " concat(u.firstName,' ',u.lastName) as facultyName,"
				+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
				+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "

				+ " and a.active= 'Y'  group by a.id";

		return findAllSQL(sql, new Object[] {});

	}

	public List<IcaBean> findIcaStatusListByProgram(String programId, String role, String username) {

		if (role.equals(Role.ROLE_ADMIN.name())) {
			/*
			 * String sql =
			 * " select distinct a.*,c.moduleName,c.acadSession,pc.campusName from course c,program p,ica a "
			 * + " left outer join program_campus pc on a.campusId=pc.campusId " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and p.id=? and a.active= 'Y' "
			 * ;
			 */
			String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,c.acadYear,"
					+ " concat(u.firstName,' ',u.lastName) as facultyName,"
					+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
					+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
					+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
					+ " left outer join course c1 on a.eventId=c1.id "
					+ " left outer join users u on a.assignedFaculty=u.username "
					+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "

					+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id";

			return findAllSQL(sql, new Object[] { username, username });
		} else {
			/*
			 * String sql =
			 * " select distinct a.*,c.moduleName,c.acadSession,pc.campusName from course c,program p,ica a "
			 * + " left outer join program_campus pc on a.campusId=pc.campusId " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and a.assignedFaculty=? and a.active= 'Y' "
			 * ;
			 */
			String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,c.acadYear,a.acadSession,c1.courseName,"
					+ " pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
					+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
					+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId "
					+ " left outer join course c1 on a.eventId=c1.id "
					+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "

					+ " and a.active= 'Y' and (a.assignedFaculty = ?) group by a.id";

			return findAllSQL(sql, new Object[] { username });

		}
	}

	public List<IcaBean> findIcaListByProgramForNonEventModule(String programId, String role, String username) {

		String sql = " select distinct a.*,p.programName,m.module_description as moduleName,a.acadYear, "
				+ " concat(u.firstName,' ',u.lastName) as facultyName, "
				+ " a.acadSession,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved "
				+ " from module m, session_master sm,program p,ica a "
				+ " left outer join ica_queries iq on a.id=iq.icaId "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where m.module_id=a.moduleId and a.programId=p.id and m.session_code = sm.sapSessionCode and sm.sapSessionText=a.acadSession "
				+ " and a.acadYear=m.acadyear " + " and a.parentIcaId is null "
				+ " and a.active= 'Y' and a.isNonEventModule = 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id order by a.id desc";

		return findAllSQL(sql, new Object[] { username, username });

	}

	public void updateIcaDate(String endDate, String publishedDate, Long icaId) {
		if ((endDate != null || !endDate.equals("")) && (publishedDate == null || publishedDate.equals(""))) {
			String sql = " update " + getTableName()
					+ " set isSubmitted=NULL, endDate=?, lastModifiedDate= SYSDATE() where id = ? ";

			getJdbcTemplate().update(sql, new Object[] { endDate, icaId });
		} else if ((publishedDate != null || !publishedDate.equals("")) && (endDate == null || endDate.equals(""))) {
			String sql = " update " + getTableName()
					+ " set isPublished=NULL, publishedDate=?, lastModifiedDate= SYSDATE() where id=? ";

			getJdbcTemplate().update(sql, new Object[] { publishedDate, icaId });
		} else if ((endDate != null || !endDate.equals("")) && (publishedDate != null || !publishedDate.equals(""))) {
			String sql = " update " + getTableName()
					+ " set isSubmitted=NULL, endDate=?, isPublished=NULL, publishedDate=?,"
					+ " lastModifiedDate= SYSDATE() where id=? ";

			getJdbcTemplate().update(sql, new Object[] { endDate, publishedDate, icaId });
		}
	}

	public void updateIcaDateWithoutSubmit(String endDate, String publishedDate, Long icaId) {
		if ((endDate != null || !endDate.equals("")) && (publishedDate == null || publishedDate.equals(""))) {
			String sql = " update " + getTableName() + " set  endDate=?, lastModifiedDate= SYSDATE() where id = ? ";

			getJdbcTemplate().update(sql, new Object[] { endDate, icaId });
		} else if ((publishedDate != null || !publishedDate.equals("")) && (endDate == null || endDate.equals(""))) {
			String sql = " update " + getTableName()
					+ " set  publishedDate=?, lastModifiedDate= SYSDATE() where isPublished = 'Y' and id=? ";

			getJdbcTemplate().update(sql, new Object[] { publishedDate, icaId });
		} else if ((endDate != null || !endDate.equals("")) && (publishedDate != null || !publishedDate.equals(""))) {
			String sql = " update " + getTableName() + " set  endDate=?, publishedDate=?,"
					+ " lastModifiedDate= SYSDATE() where id=? ";

			getJdbcTemplate().update(sql, new Object[] { endDate, publishedDate, icaId });
		}
	}

	/*
	 * public void updateIcaDateForDivision(String endDate, String publishedDate,
	 * Long parentIcaId, Long parentIcaId2) { logger.info("endDate" + endDate);
	 * 
	 * if ((endDate != null || !endDate.equals("")) && (publishedDate == null ||
	 * publishedDate.equals(""))) { String sql = " update " + getTableName() +
	 * " set isSubmitted=NULL, endDate=?, lastModifiedDate= SYSDATE() where isIcaDivisionWise='Y' and parentIcaId IN(NULL,?) or id = ? "
	 * ;
	 * 
	 * getJdbcTemplate().update(sql, new Object[] { endDate, parentIcaId,
	 * parentIcaId2 }); } else if ((publishedDate != null ||
	 * !publishedDate.equals("")) && (endDate == null || endDate.equals(""))) {
	 * String sql = " update " + getTableName() +
	 * " set isPublished=NULL, publishedDate=?, lastModifiedDate= SYSDATE() where isIcaDivisionWise='Y' and parentIcaId IN(NULL,?) or id = ? "
	 * ;
	 * 
	 * getJdbcTemplate().update(sql, new Object[] { publishedDate, parentIcaId,
	 * parentIcaId2 }); } else if ((endDate != null || !endDate.equals("")) &&
	 * (publishedDate != null || !publishedDate.equals(""))) { String sql =
	 * " update " + getTableName() +
	 * " set isSubmitted=NULL, endDate=?, isPublished=NULL, publishedDate=?," +
	 * " lastModifiedDate= SYSDATE() where isIcaDivisionWise='Y' and parentIcaId IN(NULL,?) or id = ? "
	 * ;
	 * 
	 * getJdbcTemplate().update( sql, new Object[] { endDate, publishedDate,
	 * parentIcaId, parentIcaId2 }); } }
	 */

	public void updateIcaDateForDivision(String endDate, String publishedDate, List<Long> icaIds) {
		logger.info("endDate" + endDate);

		if ((endDate != null || !endDate.equals("")) && (publishedDate == null || publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("endDate", endDate);
			params.put("icaIds", icaIds);
			String sql = " update " + getTableName()
					+ " set isSubmitted=NULL, endDate = :endDate , lastModifiedDate= SYSDATE() where  id IN (:icaIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((publishedDate != null || !publishedDate.equals("")) && (endDate == null || endDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDate", publishedDate);
			params.put("icaIds", icaIds);
			String sql = " update " + getTableName()
					+ " set isPublished=NULL, publishedDate = :publishedDate , lastModifiedDate= SYSDATE() where id IN (:icaIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((endDate != null || !endDate.equals("")) && (publishedDate != null || !publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDate", publishedDate);
			params.put("endDate", endDate);
			params.put("icaIds", icaIds);
			String sql = " update " + getTableName()
					+ " set isSubmitted=NULL, endDate = :endDate , isPublished=NULL, publishedDate = :publishedDate , "
					+ " lastModifiedDate= SYSDATE() where id IN (:icaIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		}
	}

	public void updateIcaDateForDivisionWithoutSubmit(String endDate, String publishedDate, List<Long> icaIds) {
		logger.info("My new Changes " + endDate + "" + publishedDate);

		if ((endDate != null || !endDate.equals("")) && (publishedDate == null || publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("endDate", endDate);
			params.put("icaIds", icaIds);
			String sql = " update " + getTableName()
					+ " set  endDate = :endDate , lastModifiedDate= SYSDATE() where  id IN (:icaIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((publishedDate != null || !publishedDate.equals("")) && (endDate == null || endDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDateS", publishedDate);
			params.put("icaIds", icaIds);
			String sql = " update " + getTableName()
					+ " set  publishedDate = :publishedDateS , lastModifiedDate= SYSDATE() where id IN (:icaIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((endDate != null || !endDate.equals("")) && (publishedDate != null || !publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDateS", publishedDate);
			params.put("endDate", endDate);
			params.put("icaIds", icaIds);
			String sql = " update " + getTableName() + " set  endDate = :endDate, publishedDate = :publishedDateS , "
					+ " lastModifiedDate= SYSDATE() where id IN (:icaIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		}
	}

	public List<IcaBean> findIcaListByProgramForSupportAdmin() {

		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
				+ " concat(u.firstName,' ',u.lastName) as facultyName,"
				+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
				+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
				+ " and a.acadYear=c.acadYearCode "

				+ " and a.parentIcaId is null "

				+ " and a.active= 'Y' and c.active = 'Y' group by a.id";

		return findAllSQL(sql, new Object[] {});

	}

	public List<IcaBean> findIcaListByProgramForNonEventModuleForSupportAdmin() {

		String sql = " select distinct a.*,p.programName,m.module_description as moduleName,a.acadYear, "
				+ " concat(u.firstName,' ',u.lastName) as facultyName, "
				+ " a.acadSession,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved "
				+ " from module m, session_master sm,program p,ica a "
				+ " left outer join ica_queries iq on a.id=iq.icaId "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where m.module_id=a.moduleId and a.programId=p.id and m.session_code = sm.sapSessionCode and sm.sapSessionText=a.acadSession "
				+ " and a.acadYear=m.acadyear " + " and a.parentIcaId is null "
				+ " and a.active= 'Y' and a.isNonEventModule = 'Y' group by a.id ";

		return findAllSQL(sql, new Object[] {});

	}

	public List<IcaBean> findDivisionWiseIcaListByParentIcaForSupportAdmin(String parentIcaId) {

		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
				+ " concat(u.firstName,' ',u.lastName) as facultyName,"
				+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
				+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
				+ " and a.acadYear=c.acadYearCode  "

				+ " and a.active= 'Y' and (a.parentIcaId = ?) group by a.id";

		return findAllSQL(sql, new Object[] { parentIcaId });
	}

	public List<String> getIcaStatusesForDivisionIca(Long icaId) {
		String sql = " select distinct isSubmitted from " + getTableName() + " where parentIcaId = ? and active = 'Y' ";
		return getJdbcTemplate().queryForList(sql, new Object[] { icaId }, String.class);
	}

	public List<IcaBean> findIcaListByProgramForBatchWise(String programId, String role, String username) {

		if (role.equals(Role.ROLE_ADMIN.name())) {

			/*
			 * String sql =
			 * " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear," +
			 * " GROUP_CONCAT(DISTINCT  concat(u.firstName,' ',u.lastName)) as facultyName,"
			 * +
			 * " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
			 * + " from course c,program p,ica a " +
			 * " left outer join ica_queries iq on a.id=iq.icaId  " +
			 * " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
			 * + " left outer join course c1 on a.eventId=c1.id " +
			 * " left outer join users u on FIND_IN_SET(u.username,a.assignedFaculty) " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
			 * + " and a.acadYear=c.acadYearCode "
			 * 
			 * + " and a.parentIcaId is null "
			 * 
			 * +
			 * " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id"
			 * ;
			 */

			String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
					+ " GROUP_CONCAT(DISTINCT  concat(u.firstName,' ',u.lastName)) as facultyName,"
					+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
					+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
					+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
					+ " left outer join course c1 on a.eventId=c1.id "
					+ " left outer join users u on FIND_IN_SET(u.username,a.assignedFaculty) "
					+ " where c.moduleId=a.moduleId and a.programId=p.id and (c.acadSession=a.acadSession or a.isCourseraIca ='Y') "
					+ " and a.acadYear=c.acadYearCode "

					+ " and a.parentIcaId is null "

					+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) and c.active = 'Y' group by a.id order by a.id desc";

			return findAllSQL(sql, new Object[] { username, username });
		} else {

			/*
			 * String sql =
			 * " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,a.acadSession,c1.courseName,"
			 * + " pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  " +
			 * " from course c,program p,ica a " +
			 * " left outer join ica_queries iq on a.id=iq.icaId  " +
			 * " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId "
			 * + " left outer join course c1 on a.eventId=c1.id " +
			 * " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
			 * + " and a.acadYear = c.acadYearCode "
			 * 
			 * + " and a.active= 'Y' and (a.assignedFaculty like ?) group by a.id";
			 */

			String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,a.acadSession,c1.courseName,"
					+ " pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
					+ " from course c,program p,ica a " + " left outer join ica_queries iq on a.id=iq.icaId  "
					+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId "
					+ " left outer join course c1 on a.eventId=c1.id "
					+ " where c.moduleId=a.moduleId and a.programId=p.id and (c.acadSession=a.acadSession or a.isCourseraIca ='Y') "
					+ " and a.acadYear = c.acadYearCode "

					+ " and a.active= 'Y' and (a.assignedFaculty like ?) and c.active = 'Y' group by a.id order by a.id desc";

			return findAllSQL(sql, new Object[] { "%" + username + "%" });

		}
	}

	public List<IcaBean> getSubmittedIcaForBatchWise(String username) {

//        String sql="select distinct a.*,c.moduleName as moduleName,a.acadYear,a.acadSession," + 
//				" c1.courseName,GROUP_CONCAT(DISTINCT  concat(u.firstname,' ',u.lastname)) as facultyName" + 
//				" from course c,program p,ica a" + 
//				" left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId" + 
//				" left outer join course c1 on a.eventId=c1.id " + 
//				" left outer join users u on FIND_IN_SET(u.username,a.assignedFaculty) " + 
//				" where c.moduleId=a.moduleId and a.programId=p.id and (c.acadSession=a.acadSession or a.isCourseraIca ='Y') " + 
//				" and a.isSubmitted='Y' " + 
//				" and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id";

		String sql = "select distinct a.*,c.moduleName as moduleName,a.acadYear,a.acadSession," + " c1.courseName"
				+ " from course c,program p,ica a"
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and (c.acadSession=a.acadSession or a.isCourseraIca ='Y') "
				+ " and a.isSubmitted='Y' and (a.isPublishCompWise<> 'Y' or a.isPublishCompWise is null) "
				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) group by a.id";

		return findAllSQL(sql, new Object[] { username, username });
	}

	public List<IcaBean> getIcaQueriesForApproveAll(String username) {

		/*
		 * String sql = " select distinct a.*,c.moduleName as moduleName,a.acadYear," +
		 * " a.acadSession,iq.isApproved,iq.id as icaQueryId,a.acadSession," +
		 * " c1.courseName,concat(u.firstname,' ',u.lastname) as facultyName " +
		 * " from course c,program p,ica a " +
		 * " left outer join program_campus pc on a.campusId=pc.campusId " +
		 * "  left outer join course c1 on a.eventId=c1.id " +
		 * "  left outer join users u on u.username=a.assignedFaculty " +
		 * " inner join ica_queries iq on a.id=iq.icaId " +
		 * " where c.moduleId=a.moduleId and a.programId=p.id " +
		 * " and c.acadSession=a.acadSession " + " and a.isSubmitted='Y' " +
		 * " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?)" +
		 * "and a.endDate > now() and (iq.isApproved is null or iq.isApproved = 'N') group by a.id"
		 * ;
		 */

		String sql = " select distinct a.*,c.moduleName as moduleName,a.acadYear,"
				+ " a.acadSession,iq.isApproved,iq.componentId,iq.id as icaQueryId,a.acadSession,"
				+ " c1.courseName,concat(u.firstname,' ',u.lastname) as facultyName "
				+ " from course c,program p,ica a " + " left outer join program_campus pc on a.campusId=pc.campusId "
				+ "  left outer join course c1 on a.eventId=c1.id "
				+ "  left outer join users u on u.username=a.assignedFaculty "
				+ " inner join ica_queries iq on a.id=iq.icaId " + " where c.moduleId=a.moduleId and a.programId=p.id "
				+ " and (a.isCourseraIca ='Y' or c.acadSession=a.acadSession ) " + " and a.isSubmitted='Y' "
				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?)"
				+ "and a.endDate > now() and (iq.isApproved is null or iq.isApproved = 'N') group by a.id";

		return findAllSQL(sql, new Object[] { username, username });
	}

//Coursera Chnages

	public List<IcaBean> checkAlreadyExistICAListCE(String moduleId, String acadYear, String campusId,
			List<String> acadSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSession", acadSession);
		params.put("moduleId", moduleId);
		params.put("campusId", campusId);
		params.put("acadYear", acadYear);

		if (campusId != null) {
			String sql = " select * from " + getTableName()
					+ " where moduleId = :moduleId  and acadYear = :acadYear and campusId = :campusId and acadSession in(:acadSession) and active = 'Y' ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(IcaBean.class));
		} else {
			String sql = " select * from " + getTableName()
					+ " where moduleId = :moduleId  and acadYear = :acadYear  and acadSession in(:acadSession) and active = 'Y' ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(IcaBean.class));
		}
	}

	public IcaBean findByCampusid(String campusId) {

		String sql = "SELECT  distinct pc.campusName  FROM program_campus pc, ica i "
				+ " WHERE pc.campusId=i.campusId AND pc.campusId = ? limit 1";
		return findOneSQL(sql, new Object[] { campusId });
	}

	public void updateIcaInternalPassMarks(String internalPassMarks, Long icaId) {
		String sql = " update ica set internalPassMarks=?, lastModifiedDate= SYSDATE() where id=? ";
		getJdbcTemplate().update(sql, new Object[] { internalPassMarks, icaId });

	}

	public void updateParentIcaIdInternalPassMarks(String internalPassMarks, Long ParentIcaId) {
		String sql = " update ica set internalPassMarks=?, lastModifiedDate= SYSDATE() where parentIcaId = ? ";
		getJdbcTemplate().update(sql, new Object[] { internalPassMarks, ParentIcaId });

	}

	public IcaBean presentupdateIcaInternalPassMarks(String icaId) {
		String sql = " SELECT internalPassMarks FROM ica WHERE id = ?";
		return findOneSQL(sql, new Object[] { icaId });
	}

	// new queries

	public IcaBean getIcaTests(Long icaId, Long courseId) {
		String sql = " select icaId,courseId,testIdsForIca from tests_for_ica where icaId =? and courseId=? ";
		return findOneSQL(sql, new Object[] { icaId, courseId });
	}

	public IcaBean getAllIcaTests(Long icaId, Long courseId) {
		String sql = " select * from tests_for_ica where icaId =? and courseId=? ";
		return findOneSQL(sql, new Object[] { icaId, courseId });
	}

	public int getIcaTestNC(Long icaId) {
		String sql = " select count(*) from tests_for_ica where icaId =? and (marksAdded='N' or marksAdded is  null)";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { icaId });
	}

	public int getIcaTestCompleted(Long icaId) {
		String sql = " select count(*) from tests_for_ica where icaId =? ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { icaId });
	}

	public int insertInIcaTests(IcaBean i) {

		SqlParameterSource parameterSource = getParameterSource(i);
		final String sql = "INSERT INTO tests_for_ica(icaId,courseId) " + "VALUES (:id,:courseId)";
		return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	}

	public int updateInIcaTests(IcaBean i) {

		SqlParameterSource parameterSource = getParameterSource(i);
		final String sql = " update tests_for_ica set testIdsForIca =:testIdsForIca,marksAdded='Y',modeOfAddingTestMarks=:modeOfAddingTestMarks,"
				+ " bestOf=:bestOf " + " where icaId=:id and courseId=:courseId ";

		return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	}

	public int clearIcaTests(IcaBean i) {

		SqlParameterSource parameterSource = getParameterSource(i);
		final String sql = " update tests_for_ica set testIdsForIca =null,modeOfAddingTestMarks=null,bestOf=null,marksAdded=null "
				+ " where icaId=:id and courseId=:courseId ";

		return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	}

	public List<IcaBean> icaSubmittedListByParent(String parentIcaId) {

		String sql = " select * from " + getTableName() + " where parentIcaId = ? and isSubmitted='Y' "
				+ " and active = 'Y' ";

		return findAllSQL(sql, new Object[] { parentIcaId });
	}

	public List<IcaBean> getSubmittedIcaComps(String username) {

		String sql = " SELECT a.id,a.icaName,a.acadSession,a.acadYear,a.programId,a.assignedFaculty,a.moduleId"
				+ "  FROM ica a,ica_components ic "
				+ " WHERE a.id=ic.icaId  AND (ic.isSubmitted='Y' and ic.isPublished<>'Y') "
				+ " AND a.active='Y' AND ic.active='Y' AND (a.createdBy=? OR a.lastModifiedBy=?)"
				+ " group by a.id ";

		return findAllSQL(sql, new Object[] { username,username });
	}

	public List<IcaBean> getSubmittedIcaCompsNE(String username) {
		//and a.isNonEventModule='Y'
		String sql = " SELECT a.id,a.icaName,a.acadSession,a.acadYear,a.programId,a.assignedFaculty,a.moduleId"
				+ "  FROM ica a,ica_components ic "
				+ " WHERE a.id=ic.icaId  AND (ic.isSubmitted='Y' and ic.isPublished<>'Y') and a.isNonEventModule='Y' "
				+ " AND a.active='Y' AND ic.active='Y' AND (a.createdBy=? OR a.lastModifiedBy=?) "
				+ " group by a.id ";

		return findAllSQL(sql, new Object[] { username,username });
	}

	//Peter 25/10/2021
	public IcaBean checkIfComponentIdExists(String componentId) {
		String sql = " SELECT id from pre_def_ica_components where id=?";
		return findOneSQL(sql, new Object[] { componentId });
	}

	

}
