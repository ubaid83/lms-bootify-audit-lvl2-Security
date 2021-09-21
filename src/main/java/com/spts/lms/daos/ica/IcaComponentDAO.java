package com.spts.lms.daos.ica;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.IcaComponent;
import com.spts.lms.daos.BaseDAO;

@Repository("icaComponentDAO")
public class IcaComponentDAO extends BaseDAO<IcaComponent> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "ica_components";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " ( componentId,icaId,marks,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ "(:componentId,:icaId,:marks,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " update "
				+ getTableName()
				+ " set active =:active, "
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate "
				+ " where id=:id  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " (  componentId,"
				+ " icaId,marks,active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,sequenceNo) values"
				+ "(:componentId,:icaId,:marks,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:sequenceNo)"
				+ "  ON DUPLICATE KEY UPDATE "
				+ "  active=:active,marks=:marks,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ,sequenceNo=:sequenceNo";
		
		
		

		return sql;
	}

	public List<IcaComponent> icaComponentListByIcaId(String icaId,
			String campusId) {

		if (campusId != null) {
			String sql = " select distinct ic.*,c.moduleName,p.programName,i.acadYear,pc.campusName "
					+ " from ica_components ic,ica i,course c,program_campus pc,program p "
					+ " where ic.icaId=i.id and c.moduleId=i.moduleId "
					+ " and c.acadYearCode=i.acadYear and i.active='Y' and ic.active='Y' "
					+ " and c.programId=i.programId and pc.programId=i.programId and p.id=pc.programId "
					+ " and i.campusId=pc.campusId and i.id = ? ";

			return findAllSQL(sql, new Object[] { icaId });
		} else {
			String sql = " select distinct ic.*,c.moduleName,p.programName,i.acadYear "
					+ " from ica_components ic,ica i,course c,program p "
					+ " where ic.icaId=i.id and c.moduleId=i.moduleId "
					+ " and c.acadYearCode=i.acadYear and i.active='Y' and ic.active='Y' "
					+ " and c.programId=i.programId  " + " and i.id = ? ";
			return findAllSQL(sql, new Object[] { icaId });
		}
	}

	public List<IcaComponent> icaComponentListByIcaId(Long icaId) {

		String sql = " select ic.*,pic.componentName from "
				+ getTableName()
				+ " ic,pre_def_ica_components pic "
				+ " where ic.icaId=? and ic.active ='Y' and ic.componentId=pic.id  order by ic.sequenceNo";

		return findAllSQL(sql, new Object[] { icaId });
	}

	
	public List<IcaComponent> icaComponentListByParentIcaId(Long icaId) {

		String sql = " select ic.*,pic.componentName from "
				+ getTableName()
				+ " ic,pre_def_ica_components pic,ica i "
				+ " where   ic.active ='Y' "
				+ " and i.active ='Y' and i.id=ic.icaId and i.parentIcaId =?	 "
				+ " and ic.componentId=pic.id  order by ic.sequenceNo";

		return findAllSQL(sql, new Object[] { icaId });
	}
	
	public IcaComponent icaComponentByIcaId(Long icaId,String raiseQComp) {
		String sql ="";
		if(raiseQComp.isEmpty()) {
		 sql = " select ic.*,pic.componentName from "
				+ getTableName()
				+ " ic,pre_def_ica_components pic "
				+ " where ic.icaId=? and ic.active ='Y' and ic.componentId=pic.id  "
				+ " and ic.isSubmitted <> 'Y' "
				+ " order by ic.sequenceNo limit 1";
		 return findOneSQL(sql, new Object[] { icaId });
		}else {
			 sql = " select ic.*,pic.componentName from "
					+ getTableName()
					+ " ic,pre_def_ica_components pic "
					+ " where ic.icaId=? and ic.active ='Y' and ic.componentId=pic.id  "
					
					+ " and ic.componentId=? ";
			 return findOneSQL(sql, new Object[] { icaId,raiseQComp });
		}

		
	}
	
	public int icaTotalComponentMarks(Long icaId) {

		String sql = " select sum(ic.marks) from ica_components ic,pre_def_ica_components pic "
				+ "  where ic.icaId=? and ic.active ='Y' and ic.componentId=pic.id ";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { icaId });
	}

	
	public List<IcaComponent> getComponentsByParam(String acadYear,
			String acadSession, String programId, String campusId) {
		
		String programIdL = "%" + programId + "%";
		if (campusId != null) {
			String sql = " select ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i "
					+ "          where ic.icaId=i.id and pd.id=ic.componentId and "
					+ "          i.acadYear=? and i.acadSession= ? and i.campusId = ? "
					+ "          and i.programId like ? and i.active = 'Y' and ic.active = 'Y' order by ic.sequenceNo ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					campusId, programIdL });

		} else {

			String sql = " select ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i "
					+ "          where ic.icaId=i.id and pd.id=ic.componentId and "
					+ "          i.acadYear=? and i.acadSession= ? "
					+ "          and i.programId like ? and i.active = 'Y' and ic.active = 'Y' order by ic.sequenceNo ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					programIdL });
		}
	}
	
	public List<IcaComponent> getComponentsByParamDraft(String acadYear,
			String acadSession, String programId, String campusId) {
		
		String programIdL = "%" + programId + "%";
		if (campusId != null) {
			String sql = " select ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i "
					+ "          where ic.icaId=i.id and pd.id=ic.componentId and "
					+ "          i.acadYear=? and i.acadSession= ? and i.campusId = ? "
					+ "          and i.programId like ? and i.active = 'Y' and ic.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					campusId, programIdL });

		} else {

			String sql = " select ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i "
					+ "          where ic.icaId=i.id and pd.id=ic.componentId and "
					+ "          i.acadYear=? and i.acadSession= ? "
					+ "          and i.programId like ? and i.active = 'Y' and ic.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear, acadSession,
					programIdL });
		}
	}

	/*public List<IcaComponent> getComponentsByParamFaculty(String acadYear, String username) {
		
	
		
			String sql = " select ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i "
					+ "          where (ic.icaId=i.id or ic.icaId=i.parentIcaId) and pd.id=ic.componentId and "
					+ "          i.acadYear=? and i.assignedFaculty=?"
					+ "           and i.active = 'Y' ";
			return findAllSQL(sql, new Object[] { acadYear ,username });
		
	}*/
	
	public List<IcaComponent> getComponentsByParamFaculty(String acadYear, String username) {
        
        
        
        String sql = " select ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i "
				+ "          where (ic.icaId=i.id or ic.icaId=i.parentIcaId) and pd.id=ic.componentId and "
				+ "          i.acadYear=? and FIND_IN_SET(?,i.assignedFaculty)"
				+ "           and i.active = 'Y' and ic.active = 'Y' order by sequenceNo";
        return findAllSQL(sql, new Object[] { acadYear ,username });

	}
	//26-04-2021
	public List<IcaComponent> getComponentsByParamMultiSessionCoursera(String acadYear, String acadSession, String programId,
			String campusId) {
		
		
		if(campusId!=null) {
		String sql =" select distinct ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i  "
				+ " where ic.icaId=i.id and pd.id=ic.componentId"
				+ " and i.acadSession = ? and i.active='Y' and ic.active='Y' "
				+ " and i.programId like ? and i.acadYear=? and i.campusId=? ";
		return findAllSQL(sql, new Object[] { acadSession ,"%"+programId+"%",acadYear,campusId });
		}else {
			String sql=" select distinct ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i  "
					+ " where ic.icaId=i.id and pd.id=ic.componentId"
					+ " and i.acadSession = ? and i.active='Y' and ic.active='Y' "
					+ " and i.programId like ? and i.acadYear=? ";
			return findAllSQL(sql, new Object[] { acadSession ,"%"+programId+"%",acadYear });
		}
			
	}
	public List<IcaComponent> getComponentsByParamMultiSession(String acadYear, String acadSession, String programId,
			String campusId) {
	
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
			String sql = " select distinct ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i,course c " + 
					" where ic.icaId=i.id and pd.id=ic.componentId AND i.moduleId=c.moduleId and " + 
					" i.acadYear=:acadYear and c.acadSession IN(:acadSession) and c.campusId = :campusId " + 
					" and c.programId in(:programids) and i.active = 'Y' order by ic.sequenceNo ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(IcaComponent.class));
		} else {
			String sql = " select distinct ic.*,i.moduleId,pd.componentName from ica_components ic,pre_def_ica_components pd,ica i,course c " + 
					" where ic.icaId=i.id and pd.id=ic.componentId AND i.moduleId=c.moduleId and " + 
					" i.acadYear=:acadYear and c.acadSession IN(:acadSession) " + 
					" and c.programId in(:programids) and i.active = 'Y' order by ic.sequenceNo ";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(IcaComponent.class));
		}
	}
	
	
	
	public double getCompMarks(Long icaId,Long componentId) {
		String sql =" select marks from "+getTableName()+" where icaId =? and componentId=? and active='Y'";
		
		return getJdbcTemplate().queryForObject(sql, Double.class, new Object[] {icaId,componentId});
	}
	
	public IcaComponent getCompBean(Long icaId,Long componentId) {
		String sql =" select * from "+getTableName()+" where icaId =? and componentId=? and active='Y'";
		
		return findOneSQL(sql, new Object[] {icaId,componentId});
	}
	
	public void updateIcaCompToSubmitted(String icaId,String componentId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("icaId", icaId);
        
        params.put("componentId", componentId);
        
        String sql = "update " + getTableName() + " set isSubmitted = 'Y' where icaId = :icaId "
                                        + " and componentId =:componentId ";
        getNamedParameterJdbcTemplate().update(sql, params);
} 

	
	public int getTotalComponents(String icaId) {
		
		String sql =" select count(*) from "+getTableName()+" where icaId =? and active ='Y' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}
	
	public int getSubmittedComponents(String icaId) {
		
		String sql =" select count(*) from "+getTableName()+" where icaId =? and isSubmitted='Y' and active ='Y' ";
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
	}
	
	public int updateIcaCompsToPublished(String icaId,String compId, Date lastModifiedDate, String publishedDate) {

		String sql = " update " + getTableName()
				+ " set isPublished = 'Y',lastModifiedDate = ?,publishedDate=?  where icaId = ? and componentId =? ";

		return getJdbcTemplate().update(sql, new Object[] { lastModifiedDate, publishedDate, icaId,compId });

	}
	
	public int getMaxSeqNo(Long icaId) {
		
		String sql =" select max(sequenceNo) from "+getTableName()+" where icaId =? and active='Y' ";
		try {
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] {icaId});
		}catch(Exception ex) {
			return 0;
		}
	}
	public IcaComponent getSubmittedIcaComponent(Long icaId) {
		
		String sql1 = " select id from "+getTableName()+" where icaId =? and isSubmitted='Y' order by "
				+ " sequenceNo desc limit 1 ";
		String maxId="";
		try {
			 maxId = getJdbcTemplate().queryForObject(sql1, String.class, new Object[] {icaId});
		}catch(Exception ex){
			return null;
		}
		
		if(maxId!=null) {
		return findOne(Long.valueOf(maxId));
		}else {
			return null;
		}
		
		
	}
	
	public int getSubmittedSeqNo(Long icaId) {
		String sql1 = " SELECT MAX(sequenceNo)  FROM ica_components WHERE icaId =? AND isSubmitted='Y' ";
		
		try {
			return getJdbcTemplate().queryForObject(sql1, Integer.class, new Object[] {icaId});
			 
		}catch(Exception ex){
			return 0;
		}
		
		
	}
	
	public List<IcaComponent> icaSubmittedListByIcaId(String icaId) {

		String sql = " select * from " + getTableName() + " where icaId = ? and isSubmitted='Y' "
				+ " and active = 'Y' ";

		return findAllSQL(sql, new Object[] { icaId });
	}
}
