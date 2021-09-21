package com.spts.lms.daos.tee;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.tee.TeeBean;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("teeBeanDAO")
public class TeeBeanDAO extends BaseDAO<TeeBean> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "tee";
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		String sql = "Insert into " + getTableName()
				+ " (moduleId,acadYear,campusId,programId,acadSession,eventId,teeName,teeDesc,"
				+ "assignedFaculty,scaledReq,scaledMarks,parentTeeId,internalMarks,internalPassMarks,"
				+ "externalPassMarks,externalMarks,totalMarks,active,isSubmitted,isPublished,isTeeDivisionWise,"
				+ "isNonEventModule,isCourseraTee,publishedDate,startDate,endDate,createdBy,createdDate,lastModifiedBy,lastModifiedDate,autoAssignMarks) values"
				+ " (:moduleId,:acadYear,:campusId,:programId,:acadSession,:eventId,:teeName,:teeDesc,"
				+ ":assignedFaculty,:scaledReq,:scaledMarks,:parentTeeId,:internalMarks,:internalPassMarks,"
				+ ":externalPassMarks,:externalMarks,:totalMarks,:active,:isSubmitted,:isPublished,:isTeeDivisionWise,"
				+ ":isNonEventModule,:isCourseraTee,:publishedDate,:startDate,:endDate,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:autoAssignMarks)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " update " + getTableName()
				+ " set teeName=:teeName , teeDesc=:teeDesc,internalMarks=:internalMarks,assignedFaculty=:assignedFaculty,scaledReq=:scaledReq,scaledMarks=:scaledMarks,"
				+ " externalMarks=:externalMarks," + " internalPassMarks=:internalPassMarks,"
				+ " externalPassMarks=:externalPassMarks,startDate=:startDate,endDate=:endDate,"
				+ " totalMarks=:totalMarks,lastModifiedBy=:lastModifiedBy," + " lastModifiedDate=:lastModifiedDate,autoAssignMarks=:autoAssignMarks "
				+ " where id=:id  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TeeBean> checkAlreadyExistICAList(String moduleId, String acadYear, String campusId,
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

	public List<TeeBean> getAllTeeByUsername(String username) {

		String sql = "Select t.*,c.moduleName as moduleName,p.programName,pc.campusName as campusName,"
				+ " GROUP_CONCAT(DISTINCT  concat(u.firstName,' ',u.lastName)) as facultyName "
				+ " from course c,program p,tee t "
				+ " left outer join program_campus pc on t.campusId=pc.campusId and t.programId=pc.programId "
				+ " left outer join users u on FIND_IN_SET(u.username,t.assignedFaculty) "
				+ " where c.moduleId = t.moduleId and t.createdBy = ? and t.active = 'Y' and t.programId=p.id group by t.id";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<TeeBean> getAllSubmittedTee(String username) {

		String sql = "Select t.*,c.moduleName as moduleName,p.programName from tee t,course c,program p where c.moduleId = t.moduleId and t.programId=p.id and "
				+ " c.acadSession=t.acadSession and t.createdBy = ? and t.active = 'Y' and t.isSubmitted = 'Y' group by t.id";
		return findAllSQL(sql, new Object[] { username });
	}

	public int updateTeeToPublished(String teeId, Date lastModifiedDate, String publishedDate) {

		String sql = " update " + getTableName()
				+ " set isPublished = 'Y',lastModifiedDate = ?,publishedDate=?  where id = ? ";

		return getJdbcTemplate().update(sql, new Object[] { lastModifiedDate, publishedDate, teeId });

	}

	public int updateTeeToSubmitted(String teeId, Date lastModifiedDate) {

		String sql = " update " + getTableName() + " set isSubmitted = 'Y',lastModifiedDate = ?  where id = ? ";

		return getJdbcTemplate().update(sql, new Object[] { lastModifiedDate, teeId });

	}
	
	public List<TeeBean> findDivisionWiseTeeListByParentTee(String parentTeeId) {
	
			String sql = " select distinct t.*,p.programName,c.moduleName as moduleName,t.acadYear, " + 
					" concat(u.firstName,' ',u.lastName) as facultyName, " + 
					" t.acadSession,c1.courseName,pc.campusName,p.programName,tq.id as icaQueryId,tq.isApproved " + 
					" from course c,program p,tee t " + 
					" left outer join tee_queries tq on t.id=tq.teeId " + 
					" left outer join program_campus pc on t.campusId=pc.campusId and t.programId =pc.programId " + 
					" left outer join course c1 on t.eventId=c1.id " + 
					" left outer join users u on t.assignedFaculty=u.username " + 
					" where c.moduleId=t.moduleId and t.programId=p.id and c.acadSession=t.acadSession " + 
					" and t.acadYear=c.acadYearCode " + 
					" and t.active= 'Y' and (t.parentTeeId = ?) group by t.id";

			return findAllSQL(sql, new Object[] { parentTeeId });
		}
	
	public List<TeeBean> findTeeListByProgramAndUsername(String programId, String role,String username) {

		if (role.equals(Role.ROLE_ADMIN.name())) {
			
			String sql = " select distinct t.*,p.programName,c.moduleName as moduleName,t.acadYear, " + 
					" GROUP_CONCAT(DISTINCT  concat(u.firstName,' ',u.lastName)) as facultyName, " + 
					" t.acadSession,c1.courseName,pc.campusName,p.programName,tq.id as teeQueryId,tq.isApproved  " + 
					" from course c,program p,tee t " + 
					" left outer join tee_queries tq on t.id=tq.teeId  " + 
					" left outer join program_campus pc on t.campusId=pc.campusId and t.programId =pc.programId " + 
					" left outer join course c1 on t.eventId=c1.id " + 
					" left outer join users u on FIND_IN_SET(u.username,t.assignedFaculty) " + 
					" where c.moduleId=t.moduleId and t.programId=p.id and c.acadSession=t.acadSession " + 
					" and t.acadYear=c.acadYearCode " + 
					" and t.parentTeeId is null " + 
					" and t.active= 'Y' and (t.createdBy = ? or t.lastModifiedBy = ?) group by t.id order by id desc";

			return findAllSQL(sql, new Object[] { username, username });
		} else {
			
			String sql = " select distinct t.*,p.programName,c.moduleName as moduleName,t.acadYear,t.acadSession,c1.courseName, " + 
					"pc.campusName,p.programName,tq.id as teeQueryId,tq.isApproved " + 
					"from course c,program p,tee t " + 
					"left outer join tee_queries tq on t.id=tq.teeId  " + 
					"left outer join program_campus pc on t.campusId=pc.campusId and t.programId =pc.programId " + 
					"left outer join course c1 on t.eventId=c1.id " + 
					"where c.moduleId=t.moduleId and t.programId=p.id and c.acadSession=t.acadSession " + 
					"and t.acadYear = c.acadYearCode " + 
					"and t.active= 'Y' and (t.assignedFaculty like ?) group by t.id order by id desc";

			return findAllSQL(sql, new Object[] { "%"+username+"%" });

		}
	}
	
	public List<String> getTeeStatusesForDivisionTee(Long teeId){
        String sql = " select distinct isSubmitted from "+getTableName()+" where parentTeeId = ? and active = 'Y' ";
        return getJdbcTemplate().queryForList(sql, new Object[]{teeId}, String.class);
	}
	
	public List<TeeBean> getTeeQueries(String username) {

		String sql = " select distinct t.*,c.moduleName as moduleName,t.acadYear, " + 
				"t.acadSession,tq.isApproved,tq.id as teeQueryId,t.acadSession, " + 
				"c1.courseName,concat(u.firstname,' ',u.lastname) as facultyName " + 
				"from course c,program p,tee t " + 
				"left outer join program_campus pc on t.campusId=pc.campusId " + 
				"left outer join course c1 on t.eventId=c1.id " + 
				"left outer join users u on u.username=t.assignedFaculty " + 
				"inner join tee_queries tq on t.id=tq.teeId " + 
				"where c.moduleId=t.moduleId and t.programId=p.id " + 
				"and c.acadSession=t.acadSession " + 
				"and t.isSubmitted='Y' " + 
				"and t.active= 'Y' and (t.createdBy = ? or t.lastModifiedBy = ?) group by t.id";

		return findAllSQL(sql, new Object[] { username, username });
	}
	public List<TeeBean> getTeeQueriesForApproveAll(String username) {

		String sql = " select distinct t.*,c.moduleName as moduleName,t.acadYear, " + 
				"t.acadSession,tq.isApproved,tq.id as icaQueryId,t.acadSession, " + 
				"c1.courseName,concat(u.firstname,' ',u.lastname) as facultyName " + 
				"from course c,program p,tee t " + 
				"left outer join program_campus pc on t.campusId=pc.campusId " + 
				"left outer join course c1 on t.eventId=c1.id  " + 
				"left outer join users u on u.username=t.assignedFaculty " + 
				"inner join tee_queries tq on t.id=tq.teeId " + 
				"where c.moduleId=t.moduleId and t.programId=p.id " + 
				"and c.acadSession=t.acadSession " + 
				"and t.isSubmitted='Y' " + 
				"and t.active= 'Y' and (t.createdBy = ? or t.lastModifiedBy = ?) " + 
				"and t.endDate > now() and (tq.isApproved is null or tq.isApproved = 'N') group by t.id";

		return findAllSQL(sql, new Object[] { username, username });
	}
	
	public List<TeeBean> checkAlreadyExistTEEAList(String moduleId, String acadYear, String campusId,
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
	
	public List<String> getSubmittedTeeIds(String username) {

		String sql = " select distinct a.id  from course c,program p,tee a "
				+ " left outer join program_campus pc on a.campusId=pc.campusId "
				+ " where c.moduleId=a.moduleId and a.programId=p.id "
				+ " and a.isSubmitted='Y' and a.isPublished <> 'Y' "
				+ " and a.active= 'Y' and (a.createdBy = ? or a.lastModifiedBy = ?) ";

		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { username, username });
	}

	public int updateMultipleTeeToPublished(List<String> teeId,
			Date lastModifiedDate, String publishedDate) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("teeId", teeId);
		params.put("lastModifiedDate", lastModifiedDate);
		params.put("publishedDate", publishedDate);

		String sql = " update "
				+ getTableName()
				+ " set isPublished = 'Y',lastModifiedDate = :lastModifiedDate,"
				+ " publishedDate=:publishedDate  where id in(:teeId) ";

		return getNamedParameterJdbcTemplate().update(sql, params);

	}
	
	public List<TeeBean> getTeeIdsByParentTeeIds(Long teeId) {

		String sql = " select * from " + getTableName()
				+ " where parentTeeId = ? and active= 'Y' ";

		return findAllSQL(sql, new Object[] { teeId });
	}


	public List<TeeBean> getSubmittedTeeIdsByParentTeeIds(Long teeId) {

		String sql = " select * from "
				+ getTableName()
				+ " where parentTeeId = ? and isSubmitted= 'Y' and active= 'Y' ";

		return findAllSQL(sql, new Object[] { teeId });
	}
	public List<TeeBean> teeListByParent(String parentTeeId){
        
        String sql = " select * from "+getTableName()+" where parentTeeId = ? and active = 'Y' ";
        
        return findAllSQL(sql, new Object[]{parentTeeId});
  }
	
	public String getFacultyNameByUsername(String assignedFaculty) {
		
		String sql = "select GROUP_CONCAT(DISTINCT  concat(u.firstname,' ',u.lastname)) as facultyName from users u where FIND_IN_SET(u.username,?)"; 
		
		return getJdbcTemplate().queryForObject(sql, String.class,new Object[] { assignedFaculty });
	}
	
	
	
	// Tee Suport Admin
	public List<TeeBean> findTeeListByProgramForSupportAdmin() {
		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
				+ " GROUP_CONCAT(DISTINCT  CONCAT(u.firstName,' ',u.lastName)) as facultyName,"
				+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as teeQueryId,iq.isApproved  "
				+ " from course c,program p,tee a "
				+ " left outer join tee_queries iq on a.id=iq.teeId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on FIND_IN_SET(u.username,a.assignedFaculty)"
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
				+ " and a.acadYear=c.acadYearCode "

				+ " and a.parentTeeId is null "

				+ " and a.active= 'Y' group by a.id";

		return findAllSQL(sql, new Object[] {});
	}

	public List<TeeBean> findDivisionWiseTeeListByParentTeeForSupportAdmin(String teeId) {
	
		String sql = " select distinct a.*,p.programName,c.moduleName as moduleName,a.acadYear,"
				+ " concat(u.firstName,' ',u.lastName) as facultyName,"
				+ " a.acadSession,c1.courseName,pc.campusName,p.programName,iq.id as icaQueryId,iq.isApproved  "
				+ " from course c,program p,tee a "
				+ " left outer join tee_queries iq on a.id=iq.teeId  "
				+ " left outer join program_campus pc on a.campusId=pc.campusId and a.programId =pc.programId"
				+ " left outer join course c1 on a.eventId=c1.id "
				+ " left outer join users u on a.assignedFaculty=u.username "
				+ " where c.moduleId=a.moduleId and a.programId=p.id and c.acadSession=a.acadSession "
				+ " and a.acadYear=c.acadYearCode  "

				+ " and a.active= 'Y' and (a.parentTeeId = ?) group by a.id";

		return findAllSQL(sql, new Object[] { teeId });
	}

	public void updateTeeDate(String endDate, String publishedDate, Long teeid) {
		if ((endDate != null || !endDate.equals(""))
				&& (publishedDate == null || publishedDate.equals(""))) {
			String sql = " update "
					+ getTableName()
					+ " set isSubmitted=NULL, endDate=?, lastModifiedDate= SYSDATE() where id = ? ";

			getJdbcTemplate().update(sql, new Object[] { endDate, teeid });
		} else if ((publishedDate != null || !publishedDate.equals(""))
				&& (endDate == null || endDate.equals(""))) {
			String sql = " update "
					+ getTableName()
					+ " set isPublished=NULL, publishedDate=?, lastModifiedDate= SYSDATE() where id=? ";

			getJdbcTemplate()
					.update(sql, new Object[] { publishedDate, teeid });
		} else if ((endDate != null || !endDate.equals(""))
				&& (publishedDate != null || !publishedDate.equals(""))) {
			String sql = " update "
					+ getTableName()
					+ " set isSubmitted=NULL, endDate=?, isPublished=NULL, publishedDate=?,"
					+ " lastModifiedDate= SYSDATE() where id=? ";

			getJdbcTemplate().update(sql,
					new Object[] { endDate, publishedDate, teeid });
		}
		
	}

	public void updateTeeDateForDivision(String endDate, String publishedDate, List<Long> teeIds) {
		logger.info("endDate" + endDate);

		if ((endDate != null || !endDate.equals(""))
				&& (publishedDate == null || publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("endDate", endDate);
			params.put("teeIds", teeIds);
			String sql = " update "
					+ getTableName()
					+ " set isSubmitted=NULL, endDate = :endDate , lastModifiedDate= SYSDATE() where  id IN (:teeIds) ";
			
			 getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((publishedDate != null || !publishedDate.equals(""))
				&& (endDate == null || endDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDate", publishedDate);
			params.put("teeIds", teeIds);
			String sql = " update "
					+ getTableName()
					+ " set isPublished=NULL, publishedDate = :publishedDate , lastModifiedDate= SYSDATE() where id IN (:teeIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((endDate != null || !endDate.equals(""))
				&& (publishedDate != null || !publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDate", publishedDate);
			params.put("endDate", endDate);
			params.put("teeIds", teeIds);
			String sql = " update "
					+ getTableName()
					+ " set isSubmitted=NULL, endDate = :endDate , isPublished=NULL, publishedDate = :publishedDate , "
					+ " lastModifiedDate= SYSDATE() where id IN (:teeIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		}
		
	}

	public void updateTeeDateWithoutSubmit(String endDate, String publishedDate, Long teeId) {
		if ((endDate != null || !endDate.equals(""))
				&& (publishedDate == null || publishedDate.equals(""))) {
			String sql = " update "
					+ getTableName()
					+ " set  endDate=?, lastModifiedDate= SYSDATE() where id = ? ";

			getJdbcTemplate().update(sql, new Object[] { endDate, teeId });
		} else if ((publishedDate != null || !publishedDate.equals(""))
				&& (endDate == null || endDate.equals(""))) {
			String sql = " update "
					+ getTableName()
					+ " set  publishedDate=?, lastModifiedDate= SYSDATE() where isPublished = 'Y' and id=? ";

			getJdbcTemplate()
					.update(sql, new Object[] { publishedDate, teeId });
		} else if ((endDate != null || !endDate.equals(""))
				&& (publishedDate != null || !publishedDate.equals(""))) {
			String sql = " update "
					+ getTableName()
					+ " set  endDate=?, publishedDate=?,"
					+ " lastModifiedDate= SYSDATE() where id=? ";

			getJdbcTemplate().update(sql,
					new Object[] { endDate, publishedDate, teeId });
		}
		
	}

	
	public void updateTeeDateForDivisionWithoutSubmit(String endDate, String publishedDate, List<Long> teeIds) {
		logger.info("My new Changes " + endDate +"" + publishedDate );

		if ((endDate != null || !endDate.equals(""))
				&& (publishedDate == null || publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("endDate", endDate);
			params.put("teeIds", teeIds);
			String sql = " update "
					+ getTableName()
					+ " set  endDate = :endDate , lastModifiedDate= SYSDATE() where  id IN (:teeIds) ";
			
			 getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((publishedDate != null || !publishedDate.equals(""))
				&& (endDate == null || endDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDateS", publishedDate);
			params.put("teeIds", teeIds);
			String sql = " update "
					+ getTableName()
					+ " set  publishedDate = :publishedDateS , lastModifiedDate= SYSDATE() where id IN (:teeIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		} else if ((endDate != null || !endDate.equals(""))
				&& (publishedDate != null || !publishedDate.equals(""))) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("publishedDateS", publishedDate);
			params.put("endDate", endDate);
			params.put("teeIds", teeIds);
			String sql = " update "
					+ getTableName()
					+ " set  endDate = :endDate, publishedDate = :publishedDateS , "
					+ " lastModifiedDate= SYSDATE() where id IN (:teeIds) ";

			getNamedParameterJdbcTemplate().update(sql, params);
		}
		
	}
	
	
	public TeeBean getTeeAssignments(Long teeId,Long courseId){
		String sql =" select teeId,courseId,assignmentIdsForTee from assignments_for_tee where teeId =? and courseId=? ";
		return findOneSQL(sql, new Object[] {teeId,courseId});
	}
	public TeeBean getAllTeeAssignments(Long teeId,Long courseId){
		String sql =" select * from assignments_for_tee where teeId =? and courseId=? ";
		return findOneSQL(sql, new Object[] {teeId,courseId});
	}
	public int getTeeAssignmentNC(Long teeId){
		String sql =" select count(*) from assignments_for_tee where teeId =? and (marksAdded='N' or marksAdded is  null)";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {teeId});
	}
	
	public int getTeeAssignmentCompleted(Long teeId){
		String sql =" select count(*) from assignments_for_tee where teeId =? ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {teeId});
	}
	public int insertInTeeAssignments(TeeBean i) {
		  
			SqlParameterSource parameterSource = getParameterSource(i);
		        final String sql = "INSERT INTO assignments_for_tee(teeId,courseId) "
		                    + "VALUES (:id,:courseId)";
		        return getNamedParameterJdbcTemplate().update(sql, parameterSource);

		  }
	
	public int updateInTeeAssignments(TeeBean i) {
		  
			SqlParameterSource parameterSource = getParameterSource(i);
	        final String sql = " update assignments_for_tee set assignmentIdsForTee =:assignmentIdsForTee,"
	        		+ " marksAdded='Y',modeOfAddingAssignmentMarks=:modeOfAddingAssignmentMarks,"
	        		+ " bestOf=:bestOf "
	        		+ " where teeId=:id and courseId=:courseId ";
	                 
	        return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	  }
	
	public int clearTeeAssignments(TeeBean i) {
		  
		SqlParameterSource parameterSource = getParameterSource(i);
        final String sql = " update assignments_for_tee set assignmentIdsForTee =null,modeOfAddingAssignmentMarks=null,bestOf=null,marksAdded=null "
        		+ " where teeId=:id and courseId=:courseId ";
                 
        return getNamedParameterJdbcTemplate().update(sql, parameterSource);

  }
	public List<TeeBean> teeSubmittedListByParent(String teeparentId) {
		 String sql = " select * from "+getTableName()+" where parentTeeId = ? and isSubmitted='Y' "
			        + " and active = 'Y' ";
		 return findAllSQL(sql, new Object[]{teeparentId});
	}
	
}
