package com.spts.lms.daos.user;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.WsdlLog;
import com.spts.lms.daos.BaseDAO;

@Repository
public class WsdlLogDAO extends BaseDAO<WsdlLog>{

	
	
	@Override
	protected String getTableName() {
		return "wsdlservicelog";
	}

	@Override
	protected String getInsertSql() {
		String sql = "INSERT INTO "
				+ getTableName()
				+ "(studentObjectid,username,mobile,email,failedReason,createdDate,createdBy,lastModifiedDate,lastModifiedBy)"
				+ "VALUES(:studentObjectid,:username,:mobile,:email,:failedReason,:createdDate,:createdBy,:lastModifiedDate,"
				+ ":lastModifiedBy)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		/*String sql = "INSERT INTO "
				+ getTableName()
				+ "(studentObjectid,username,mobile,email,failedReason,createdDate,createdBy,lastModifiedDate,lastModifiedBy)"

				+ "VALUES"

				+ "(:studentObjectid,:username,:mobile,:email,:failedReason,:createdDate,:createdBy,:lastModifiedDate,"
				+ ":lastModifiedBy)"

				+ " ON DUPLICATE KEY UPDATE "

				+ " firstname = :firstname," + " lastname = :lastname,"
				+ " password = :password,"
				+ " enrollmentYear = :enrollmentYear,"
				+ " enrollmentMonth = :enrollmentMonth,"
				+ " validityEndYear = :validityEndYear,"
				+ " validityEndMonth = :validityEndMonth,"
				+ " fatherName = :fatherName," + " motherName = :motherName,"
				+ " programId = :programId," + " email = :email,"
				+ " mobile = :mobile," + " acadSession = :acadSession,"
				+ " type = :type," + " lastModifiedDate = :lastModifiedDate,"
				+ " lastModifiedBy = :lastModifiedBy";*/
		return null;
	}
	
}
