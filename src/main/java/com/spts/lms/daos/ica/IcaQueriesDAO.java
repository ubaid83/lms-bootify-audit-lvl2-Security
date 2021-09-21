package com.spts.lms.daos.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaQueries;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.web.utils.Utils;
@Repository("icaQueriesDAO")
public class IcaQueriesDAO extends BaseDAO<IcaQueries>{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "ica_queries";

	}

	@Override
	protected String getInsertSql() {

		return null;
	}

	@Override
	protected String getUpdateSql() {
		String sql =" update "+getTableName()+" set filePath=:filePath,isApproved=:isApproved,isReEvaluated=:isReEvaluated, "
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate "
				+ " where icaId =:icaId and componentId=:componentId ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " (icaId,filePath,isApproved,isReEvaluated,createdBy,createdDate,lastModifiedBy,lastModifiedDate,componentId) values"
				+ " (:icaId,:filePath,:isApproved,:isReEvaluated,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:componentId) "

				+ "  ON DUPLICATE KEY UPDATE "
				
				+ " filePath=:filePath,isApproved=:isApproved,isReEvaluated=:isReEvaluated,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}
	
	
	public IcaQueries findByIcaId(Long icaId){
		
		String sql = " select * from "+getTableName()+" where icaId = ? limit 1 ";
		
		return findOneSQL(sql, new Object[]{icaId});
	}
	
	public IcaQueries findByIcaIdAndCompId(String icaId,String compId){
		
		String sql = " select * from "+getTableName()+" where icaId = ? and componentId=? ";
		
		return findOneSQL(sql, new Object[]{icaId,compId});
	}
	
	public List<IcaQueries> findAllQueriesByIcaId(Long icaId){
		
		String sql = " select * from "+getTableName()+" where icaId = ? ";
		
		return findAllSQL(sql, new Object[] {icaId });
	}
	
	public void updateReEvaluated(Long icaId){
		String sql = "update "+getTableName()+" set isReEvaluated = 'Y' where icaId = ?";
		
		getJdbcTemplate().update(sql, new Object[] { icaId});
	}
	
	public void updateIsApproved(String icaId,String username,String filePath,String componentId){
		if(componentId!=null) {
		String sql = "update "+getTableName()+" set isApproved = 'Y' ,filePath=?, lastModifiedBy =? ,lastModifiedDate =? "
				+ " where icaId = ? and componentId =? ";
		
		getJdbcTemplate().update(sql, new Object[] { filePath,username,Utils.getInIST(),icaId,componentId});
		
		}else {
			String sql = "update "+getTableName()+" set isApproved = 'Y' ,filePath=?, lastModifiedBy =? ,lastModifiedDate =? "
					+ " where icaId = ? ";
			
			getJdbcTemplate().update(sql, new Object[] { filePath,username,Utils.getInIST(),icaId});
			
		}
	}
	
	public void updateReEvaluated(Long icaId,String componentId){
		String sql = "update "+getTableName()+" set isReEvaluated = 'Y' where icaId = ? and componentId=? ";
		
		getJdbcTemplate().update(sql, new Object[] { icaId,componentId});
	}

	public IcaQueries findByIcaIdIsApproved(String icaId) {
		String sql="select * from "+getTableName()+"  where icaid=? and isApproved ='Y' limit 1";
		 return findOneSQL(sql, new Object[]{icaId});
		
	}
}
