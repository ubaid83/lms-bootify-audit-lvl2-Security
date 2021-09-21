/*package com.spts.lms.daos.wieghtage;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.wieghtage.Wieghtage;
import com.spts.lms.daos.BaseDAO;



@Repository("wieghtageDAO")
public class WieghtageDAO extends BaseDAO<Wieghtage> {

	@Override
	protected String getTableName() {

		return "wieghtage";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into " + getTableName() + " (id,programId "
				+ " courseId) values"
				+ "(:id, :programId ,"
				+ " :courseId)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		
		String sql = "update  "
				+  getTableName() 
				+ " set programId=:programId, "
				+ " courseId=:courseId, "
				+ " where id=:id";
		return sql;
	}

	
	@Override
	protected String getUpsertSql() {
		return null;
	}
	
}
*/