package com.spts.lms.daos.ica;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaComponentMarks;
import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;

@Repository("icaComponentMarksDAO")
public class IcaComponentMarksDAO extends BaseDAO<IcaComponentMarks> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "ica_component_marks";

	}

	@Override
	protected String getInsertSql() {

		return null;
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " ( username,icaId,componentId,marks,saveAsDraft,finalSubmit,remarks,isAbsent,"
				+ " active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,isQueryApproved) values"
				+ " (:username,:icaId,:componentId,:marks,:saveAsDraft,:finalSubmit,:remarks,:isAbsent,"
				+ " :active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:isQueryApproved) "

				+ "  ON DUPLICATE KEY UPDATE "
				+ "  marks=:marks,active=:active,saveAsDraft=:saveAsDraft,finalSubmit=:finalSubmit,remarks=:remarks,isAbsent=:isAbsent,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate,isQueryApproved=:isQueryApproved ";

		return sql;
	}

	public List<IcaComponentMarks> icaComponentMarksByIcaId(Long icaId) {
		String sql = " select * from " + getTableName() + " where icaId = ? ";
		return findAllSQL(sql, new Object[] { icaId });
	}
	
	public List<IcaComponentMarks> icaComponentMarksByIcaId(Long icaId,String componentId) {
		String sql = " select * from " + getTableName() + " where icaId = ? and componentId =? ";
		return findAllSQL(sql, new Object[] { icaId,componentId });
	}
	

	public List<IcaComponentMarks> getIcaComponentMarksByUser(String usrename) {
		String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName "
				+ " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd "
				+ " where i.id=icm.icaId and icm.username = ? and i.active = 'Y' "
				+ " and i.moduleId=c.moduleId and icm.componentId=pd.id "
				+ " AND icm.active= 'Y' and i.isPublished = 'Y' "
				+ " AND (i.isNonEventModule <> 'Y'  OR i.isNonEventModule IS NULL)  group by icm.componentId,icm.icaId ";

		return findAllSQL(sql, new Object[] { usrename });
	}
	
	public List<IcaComponentMarks> getIcaPublishedCompMarks(String usrename) {
		String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName "
				+ " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd,ica_components ic "
				+ " where i.id=icm.icaId and icm.username = ? and i.active = 'Y' "
				+ " and i.moduleId=c.moduleId and icm.componentId=pd.id and icm.icaId = ic.icaId "
				+ " and ic.componentId =icm.componentId and pd.id =ic.componentId "
				+ " AND icm.active= 'Y' and ic.isPublished = 'Y' and "
				+ " (i.isSubmitted <> 'Y'  OR i.isSubmitted IS NULL) "
				+ " AND (i.isNonEventModule <> 'Y'  OR i.isNonEventModule IS NULL) "
				+ " group by icm.componentId,icm.icaId ";

		return findAllSQL(sql, new Object[] { usrename });
	}
	
	public List<IcaComponentMarks> getIcaComponentMarksByUserForNonEvent(String usrename) {
		String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName "
				+ " from ica i,ica_component_marks icm,module m,pre_def_ica_components pd "
				+ " where i.id=icm.icaId and icm.username = ? and i.active = 'Y' "
				+ " and i.moduleId=m.module_id and icm.componentId=pd.id "
				+ " AND icm.active= 'Y' and i.isPublished = 'Y' and i.isNonEventModule = 'Y'  group by icm.componentId,icm.icaId ";

		return findAllSQL(sql, new Object[] { usrename });
	}

	public List<IcaComponentMarks> getIcaPublishedComponentMarksByUserForNonEvent(String usrename) {
		String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName "
				+ " from ica i,ica_component_marks icm,module m,pre_def_ica_components pd,ica_components ic "
				+ " where i.id=icm.icaId and icm.username = ? and i.active = 'Y' "
				+ " and ic.componentId =icm.componentId and pd.id =ic.componentId and icm.icaId = ic.icaId "
				+ " and i.moduleId=m.module_id and icm.componentId=pd.id "
				+ " AND icm.active= 'Y' and ic.isPublished = 'Y' and "
				+ " (i.isSubmitted <> 'Y'  OR i.isSubmitted IS NULL) "
				+ " and i.isNonEventModule = 'Y'  group by icm.componentId,icm.icaId ";

		return findAllSQL(sql, new Object[] { usrename });
	}
	
	public List<IcaComponentMarks> getIcaComponentMarksByIcaId(String icaId) {
		String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName ,ic.sequenceNo  "
				+ " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd , ica_components ic  "
				+ " where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' "
				+ " and i.moduleId=c.moduleId and icm.componentId=pd.id "
				+ " and icm.active= 'Y' and ic.icaId=i.id and icm.componentId = ic.componentId group by icm.componentId,icm.icaId,username  order by ic.sequenceNo  ";

		return findAllSQL(sql, new Object[] { icaId });
	}
	
	
	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdForNonEvent(String icaId) {
		String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName,ic.sequenceNo "
				+ " from ica i,ica_component_marks icm,module m,pre_def_ica_components pd , ica_components ic "
				+ " where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' "
				+ " and i.moduleId=m.module_id and icm.componentId=pd.id "
				+ " and icm.active= 'Y' and ic.icaId=i.id and icm.componentId = ic.componentId group by icm.componentId,icm.icaId,username order by ic.sequenceNo ";

		return findAllSQL(sql, new Object[] { icaId });
	}

	public String checkWhetherGradingStartOrNotP(String icaId) {

		String sql = " select (case when (count(distinct(icm.id))) > 0 then 't' else 'f' end) as checkGrade from "
				+ " ica_component_marks icm,ica a where "
				+ " icm.icaId=a.id and a.parentIcaId = ? ";

		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] { icaId });
	}

	public String checkWhetherGradingStartOrNot(String icaId) {

		String sql = " select (case when (count(distinct(icm.id))) > 0 then 't' else 'f' end) as checkGrade from "
				+ " ica_component_marks icm,ica a where "
				+ " icm.icaId=a.id and a.id = ? ";

		return getJdbcTemplate().queryForObject(sql, String.class,
				new Object[] { icaId });
	}

	public List<IcaComponentMarks> getIcaComponentMarksByParam(String acadYear,
			String acadSession, String programId, String campusId) {
		
		String programIdL = "%" + programId + "%";
		if (campusId != null) {
			String sql = " select distinct icm.*, round(icm.marks,2) as marks ,i.moduleId,pd.componentName from ica_component_marks icm,ica i,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.acadSession= ? and icm.componentId=pd.id "
					+ " and i.programId like ? and i.campusId=? and i.active='Y' and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and icm.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					programIdL, campusId });
		} else {
			String sql = " select distinct icm.*, round(icm.marks,2) as marks,i.moduleId,pd.componentName from ica_component_marks icm,ica i,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.acadSession= ? and icm.componentId=pd.id "
					+ " and i.programId like ?  and i.active='Y' and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and icm.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					programIdL });
		}
	}
	
	//26-04-2021
	public List<IcaComponentMarks> getIcaComponentMarksByParamMultiSessionCoursera(String acadYear,
			String acadSession, String programId, String campusId) {
		if (campusId != null) {
			String sql = " select distinct icm.*, round(icm.marks,2) as marks ,i.moduleId,pd.componentName from ica_component_marks icm,ica i,course c ,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.acadSession= ? and icm.componentId=pd.id and  i.moduleId=c.moduleId "
					+ " and i.programId like ? and i.campusId=? and i.active='Y' and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and icm.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,"%"+programId+"%",campusId });
		} else {
			String sql = " select distinct icm.*, round(icm.marks,2) as marks,i.moduleId,pd.componentName from ica_component_marks icm,ica i,course c,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.acadSession =? and icm.componentId=pd.id and  i.moduleId=c.moduleId "
					+ " and i.programId =?  and i.active='Y' and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and icm.active = 'Y' ";
			return  findAllSQL(sql, new Object[] { acadYear, acadSession,"%"+programId+"%"});
		}
	}
	public List<IcaComponentMarks> getIcaComponentMarksByParamMultiSession(String acadYear,
			String acadSession, String programId, String campusId) {
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		  if (programId.contains(",")) { List<String> programidsList =
		  Arrays.asList(programId.split(",")); params.put("programids",
		  programidsList); } else { params.put("programids",programId);
		  
		  
		  
		  }
		  
		  if (acadSession.contains(",")) { List<String> acadSessionList =
		  Arrays.asList(acadSession.split(",")); params.put("acadSession",
		  acadSessionList); } else { params.put("acadSession", acadSession);
		  
		  }

		params.put("acadYear",acadYear);
		params.put("campusId",campusId);
		
		
		
		if (campusId != null) {
			String sql = " select distinct icm.*, round(icm.marks,2) as marks ,i.moduleId,pd.componentName from ica_component_marks icm,ica i,course c ,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=:acadYear and c.acadSession in(:acadSession) and icm.componentId=pd.id and  i.moduleId=c.moduleId "
					+ " and c.programId in(:programids) and c.campusId=:campusId and i.active='Y' and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and icm.active = 'Y' ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(IcaComponentMarks.class));
		} else {
			String sql = " select distinct icm.*, round(icm.marks,2) as marks,i.moduleId,pd.componentName from ica_component_marks icm,ica i,course c,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=:acadYear and c.acadSession in(:acadSession) and icm.componentId=pd.id and  i.moduleId=c.moduleId "
					+ " and c.programId in(:programids)  and i.active='Y' and i.isSubmitted='Y' and icm.finalSubmit = 'Y' and icm.active = 'Y' ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(IcaComponentMarks.class));
		}
	}
	
	
	public List<IcaComponentMarks> getIcaComponentMarksByParamDraft(String acadYear,
			String acadSession, String programId, String campusId) {
		
		String programIdL = "%" + programId + "%";
		if (campusId != null) {
			String sql = " select icm.*, round(icm.marks,2) as marks ,i.moduleId,pd.componentName from ica_component_marks icm,ica i,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.acadSession= ? and icm.componentId=pd.id "
					+ " and i.programId like ? and i.campusId=? and i.active='Y' and icm.saveAsDraft = 'Y' and icm.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					programIdL, campusId });
		} else {
			String sql = " select icm.*, round(icm.marks,2) as marks,i.moduleId,pd.componentName from ica_component_marks icm,ica i,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.acadSession= ? and icm.componentId=pd.id "
					+ " and i.programId like ?  and i.active='Y' and icm.saveAsDraft = 'Y' icm.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					programIdL });
		}
	}
	
	public List<IcaComponentMarks> getIcaComponentMarksByUserById(String usrename, String icaId) {
        String sql = " select icm.*,pd.componentName "
                                        + " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd "
                                        + " where i.id=icm.icaId and icm.username = ? and i.active = 'Y' "
                                        + " and i.moduleId=c.moduleId and icm.componentId=pd.id and i.id = ?"
                                        + " AND icm.active= 'Y' and i.isPublished = 'Y' group by icm.componentId,icm.icaId ";

        return findAllSQL(sql, new Object[] { usrename , icaId});
	}
	
	public List<IcaComponentMarks> icaComponentMarksByParentIcaId(Long icaId) {
        String sql = " select icm.* from " + getTableName() + " icm,ica i"
                                        + "  where i.parentIcaId = ? and i.id=icm.icaId ";
        return findAllSQL(sql, new Object[] { icaId });
	}
	/*public List<IcaComponentMarks> getIcaComponentMarksByParamFaculty(String acadYear ,String username) {
		
		
		
			String sql = " select icm.*, round(icm.marks,2) as marks,i.moduleId,pd.componentName from ica_component_marks icm,ica i,pre_def_ica_components pd "
					+ " where i.id=icm.icaId and i.acadYear=? and i.assignedFaculty=? and icm.componentId=pd.id "
					+ "  and i.active='Y' and icm.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, username });
		
	}*/
	
	public List<IcaComponentMarks> getIcaComponentMarksByParamFaculty(String acadYear ,String username) {
        
		String sql = " select icm.*, round(icm.marks,2) as marks,i.moduleId,pd.componentName from ica_component_marks icm,ica i,pre_def_ica_components pd "
				+ " where i.id=icm.icaId and i.acadYear=? and FIND_IN_SET(?,i.assignedFaculty) and icm.componentId=pd.id "
				+ "  and i.active='Y' and icm.active = 'Y' ";
        return findAllSQL(sql, new Object[] { acadYear, username });

}

	
	public List<String> getDistinctUsernamesByActiveIcaId(String icaId){
		String sql = " select distinct username from "+getTableName()+" where active = 'Y' and icaId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ icaId });
	}
	
	public IcaComponentMarks getIcaCompMarksByUsername(String icaId,String componentId,String username){
		String sql = " select * from "+getTableName()+" where active = 'Y' and icaId = ? and componentId =? "
				+ " and username =? ";
		return findOneSQL(sql, new Object[] {icaId,componentId,username});
	}
	
	public List<String> getDistinctUsernamesByActiveIcaId(String icaId,String componentId){
		String sql = " select distinct username from "+getTableName()+" where active = 'Y' and icaId = ? and componentId =? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ icaId,componentId });
	}
	
	public void updateSaveAsDraftOrFinalSubmit(String flag, String icaId,String value) {
		if(flag.equalsIgnoreCase("DRAFT")){
			String sql = "update " + getTableName() + " set saveAsDraft = ? where icaId = ? and finalSubmit is null ";
			executeUpdateSql(sql, new Object[] { value, icaId });
		}else{
			if(value.equalsIgnoreCase("Y")){
				String sql = "update " + getTableName() + " set finalSubmit = ?, saveAsDraft = NULL where icaId = ?";
				executeUpdateSql(sql, new Object[] { value, icaId });
			}else{
				String sql = "update " + getTableName() + " set finalSubmit = ? where icaId = ?";
				executeUpdateSql(sql, new Object[] { value, icaId });
			}
			
		}
	}
	//componentId
	
	public void updateSaveAsDraftOrFinalSubmit(String flag, String icaId,String value,String componentId) {
		if(flag.equalsIgnoreCase("DRAFT")){
			String sql = "update " + getTableName() + " set saveAsDraft = ? where icaId = ? and componentId=? ";
			executeUpdateSql(sql, new Object[] { value, icaId,componentId });
		}else{
			if(value.equalsIgnoreCase("Y")){
				String sql = "update " + getTableName() + " set finalSubmit = ?, saveAsDraft = NULL where icaId = ?"
						+ " and componentId=? ";
				executeUpdateSql(sql, new Object[] { value, icaId,componentId });
			}else{
				String sql = "update " + getTableName() + " set finalSubmit = ? where icaId = ?"
						+ " and componentId=? ";
				executeUpdateSql(sql, new Object[] { value, icaId,componentId });
			}
			
		}
	}
	
	public List<String> getFinalSubmitByIcaIdAndUserList(String icaId, List<String> usernames) {
        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("icaId", icaId);
                        params.put("usernames", usernames);
        

        String sql = " select distinct finalSubmit from  " + getTableName()
                                        + " where active = 'Y' and icaId = :icaId and username in (:usernames) ";

        return getNamedParameterJdbcTemplate().queryForList(sql, params,
                                        String.class);

	}
	
	public List<String> getFinalSubmitByIcaIdAndUserList(String icaId, List<String> usernames,String componentId) {
        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("icaId", icaId);
                        params.put("usernames", usernames);
                        params.put("componentId", componentId);

        String sql = " select distinct finalSubmit from  " + getTableName()
                                        + " where active = 'Y' and icaId = :icaId and username in (:usernames) "
                                        + " and componentId =:componentId ";

        return getNamedParameterJdbcTemplate().queryForList(sql, params,
                                        String.class);

	}
	
	public void updateFinalSubmitByUserList(List<String> usernames, String icaId) {
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("icaId", icaId);
	        params.put("usernames", usernames);
	        
	        String sql = "update " + getTableName() + " set finalSubmit = 'Y', saveAsDraft = NULL where icaId = :icaId "
	                                        + " and username in (:usernames) ";
	        getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	public void updateFinalSubmitByUserList(List<String> usernames, String icaId,String componentId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("icaId", icaId);
        params.put("usernames", usernames);
        params.put("componentId", componentId);
        
        String sql = "update " + getTableName() + " set finalSubmit = 'Y', saveAsDraft = NULL where icaId = :icaId "
                                        + " and username in (:usernames) and componentId =:componentId ";
        getNamedParameterJdbcTemplate().update(sql, params);
}
	
	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdBatchWise(String icaId, String username) {
        String sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName ,ic.sequenceNo "
                                        + " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd, ica_student_batchwise isb, ica_components ic  "
                                        + " where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' and isb.icaId = i.id "
                                        + " and isb.username = icm.username and isb.facultyId = ? "
                                        + " and i.moduleId=c.moduleId and icm.componentId=pd.id "
                                        + " and icm.active= 'Y' and ic.icaId=i.id and icm.componentId = ic.componentId  group by icm.componentId,icm.icaId,username  order by ic.sequenceNo ";

        return findAllSQL(sql, new Object[] { icaId, username });
	}

	
	
	
	
	public int deleteIcaTotalMarksByStudents(String icaId, List<String> studList) {

		Map<String, Object> mapper = new HashMap<>();

		mapper.put("icaId", icaId);
		mapper.put("studList", studList);

		String sql = " delete from " + getTableName() + " where icaId =:icaId and username in(:studList) ";
		return getNamedParameterJdbcTemplate().update(sql, mapper);
	}
	
	public List<IcaComponentMarks> getIcaMarks(String icaId){
		
		String sql =" select distinct username,SUM(marks) as marks FROM ica_component_marks WHERE icaId = ? " + 
				 " group by username";
		return findAllSQL(sql, new Object[] {icaId});
	}
	
	public int updateRaiseQuery(String icaId, String username, String query,String compId) {

		String sql = " update " + getTableName()
		+ " set isQueryRaise = 'Y',lastModifiedBy = ?,lastModifiedDate=?,query=? "
		+ "  where icaId = ? and username =? and componentId =? ";

		return getJdbcTemplate().update(sql, new Object[] { username, Utils.getInIST(), query, icaId, username,compId });
	}
	
	public List<IcaComponentMarks> getIsReevalIcaUsername(String icaId,String compId) {

		String sql = "select itm.username,itm.isQueryApproved,itm.remarks from ica_component_marks itm , ica_queries iq where itm.icaId=iq.icaId "
				+ "and itm.icaId=? and itm.isQueryRaise='Y' and iq.isApproved='Y' and (iq.isReEvaluated <> 'Y' or iq.isReEvaluated is null)"
				+ " and iq.componentId=? ";
		return findAllSQL(sql, new Object[] { icaId,compId });
	}
// new change for sequence 09-01
	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdWithSeqNo(String parenticaId,String seqFlag,String icaId) {
		String sql="";
		if(seqFlag.equals("I"))
		{
			
			 sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName ,ic.sequenceNo  "
					+ " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd , ica_components ic  "
					+ " where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' "
					+ " and i.moduleId=c.moduleId and icm.componentId=pd.id "
					+ " and icm.active= 'Y' and ic.icaId=i.id and icm.componentId = ic.componentId group by icm.componentId,icm.icaId,username  order by ic.sequenceNo  ";
			 return findAllSQL(sql, new Object[] { icaId });
		}else 
		{
		
		sql = " select icm.*,round(icm.marks,2) as marks ,pd.componentName ,ic.sequenceNo   from ica i,ica_component_marks icm,course c,pre_def_ica_components pd , "
				+ "ica_components ic   where i.id=icm.icaId and icm.icaId=? and i.parentIcaId = ? and i.active = 'Y'  and i.moduleId=c.moduleId and "
				+ "icm.componentId=pd.id  and icm.active= 'Y' and ic.icaId=i.parentIcaId and ic.componentId=icm.componentId group by icm.componentId,icm.icaId,"
				+ "username  order by ic.sequenceNo";
		return findAllSQL(sql, new Object[] { icaId,parenticaId});
		}
		
		
	}
	
	//new change for sequence 09-01
	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdBatchWiseforSeq(String tempIcaId, String seqFlag,String username,String icaId) {
        String sql=""; 
        if(seqFlag.equals("I")) {
        	
        sql= "select icm.*,round(icm.marks,2) as marks ,pd.componentName ,ic.sequenceNo "
                                        + " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd, ica_student_batchwise isb, ica_components ic  "
                                        + " where i.id=icm.icaId and icm.icaId = ? and i.active = 'Y' and isb.icaId = i.id "
                                        + " and isb.username = icm.username and isb.facultyId = ? "
                                        + " and i.moduleId=c.moduleId and icm.componentId=pd.id "
                                        + " and icm.active= 'Y' and ic.icaId=i.id and icm.componentId = ic.componentId  group by icm.componentId,icm.icaId,username  order by ic.sequenceNo ";

        return findAllSQL(sql, new Object[] { tempIcaId, username });
        }
        else {
        	sql= "select icm.*,round(icm.marks,2) as marks ,pd.componentName ,ic.sequenceNo "
                    + " from ica i,ica_component_marks icm,course c,pre_def_ica_components pd, ica_student_batchwise isb, ica_components ic  "
                    + " where i.id=icm.icaId and icm.icaId = ?  and i.parentIcaId=? and i.active = 'Y' and isb.icaId = i.id "
                    + " and isb.username = icm.username and isb.facultyId = ? "
                    + " and i.moduleId=c.moduleId and icm.componentId=pd.id  and "
                    + " and icm.active= 'Y' and ic.icaId=i.parentIcaId and icm.componentId = ic.componentId  group by icm.componentId,icm.icaId,username  order by ic.sequenceNo ";
        	return findAllSQL(sql, new Object[] { icaId,tempIcaId, username });
        }
	
	}
	
	
	
	
	
	
	

}
