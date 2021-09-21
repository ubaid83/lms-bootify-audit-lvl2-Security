package com.spts.lms.daos.test;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.test.StudentOfflineTest;
import com.spts.lms.daos.BaseDAO;

@Repository("studnetOfflineTestDao")
public class StudentOfflineTestDAO extends BaseDAO<StudentOfflineTest>{
	
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "student_offline_test";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (offlineTestId,username,score,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:offlineTestId,:username,:score,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " score = :score," + " username =:username,"
				
				+ " offlineTestId = :offlineTestId,"
				+ " lastModifiedDate =:lastModifiedDate,"
				+ " lastModifiedBy =:lastModifiedBy"

			

				+ " where offlineTestId = :offlineTestId and username = :username  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<StudentOfflineTest> findByOfflineTestId(String offlineTestId){
		
		String sql = " select * from "+ getTableName()+" where offlineTestId = ? " ;
		
		return findAllSQL(sql, new Object[]{offlineTestId});
		
		
	}
}
