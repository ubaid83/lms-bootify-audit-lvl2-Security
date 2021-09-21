package com.spts.lms.daos.selectives;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.selectives.SelectiveStudents;
import com.spts.lms.daos.BaseDAO;

@Repository("selectiveStudentsDAO")
public class SelectiveStudentsDAO extends BaseDAO<SelectiveStudents>{
	
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "selective_students";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (selective_id,username,active,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:selective_id,:username,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " active = :active," 
				
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate"

			

				+ " where selective_id = :selective_id and username=:username  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
