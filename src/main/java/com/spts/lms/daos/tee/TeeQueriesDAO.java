package com.spts.lms.daos.tee;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.tee.TeeQueries;
import com.spts.lms.daos.BaseDAO;

@Repository("teeQueriesDAO")
public class TeeQueriesDAO extends BaseDAO<TeeQueries>{

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "tee_queries";
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
		String sql = "Insert into "
				+ getTableName()
				+ " (teeId,filePath,isApproved,isReEvaluated,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ " (:teeId,:filePath,:isApproved,:isReEvaluated,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) "

				+ "  ON DUPLICATE KEY UPDATE "
				
				+ " filePath=:filePath,isApproved=:isApproved,isReEvaluated=:isReEvaluated,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}

	public TeeQueries findByTeeId(Long teeId){
		
		String sql = " select * from "+getTableName()+" where teeId = ? ";
		
		return findOneSQL(sql, new Object[]{teeId});
	}
	
	public void updateReEvaluated(Long teeId) {
		String sql = "update " + getTableName() + " set isReEvaluated = 'Y' where teeId = ?";

		getJdbcTemplate().update(sql, new Object[] { teeId });
	}
}
