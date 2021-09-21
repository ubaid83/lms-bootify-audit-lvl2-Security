package com.spts.lms.daos.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.NsBean;
import com.spts.lms.beans.ica.StudentNcIca;
import com.spts.lms.daos.BaseDAO;


@Repository("studentNcIcaDAO")
public class StudentNcIcaDAO extends BaseDAO< StudentNcIca>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	protected String getTableName() {
		
		return "student_nc_ica";
	}

	@Override
	protected String getInsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " ( icaId,"
				+ " username,grade,active,finalSubmit,createdBy,createdDate,lastModifiedBy,"
				+ " lastModifiedDate) values"
				+ " (:icaId,"
				+ " :username,:grade,:active,:finalSubmit,:createdBy,:createdDate,:lastModifiedBy,"
				+ " :lastModifiedDate) ";
		return sql;
		
	}

	@Override
	protected String getUpdateSql() {
		
		return null;
	}

	@Override
	protected String getUpsertSql() {
		
		String sql = "Insert into "
				+ getTableName()
				+ " (icaId,username,grade,finalSubmit,createdBy,"
				+ " createdDate,lastModifiedBy,lastModifiedDate) values"
				+ " (:icaId,:username,:grade,:finalSubmit,:createdBy,"
				+ " :createdDate,:lastModifiedBy,:lastModifiedDate) "

				+ "  ON DUPLICATE KEY UPDATE "
				+ " icaId=:icaId,username=:username,"
				+ " grade=:grade,"
				+ " finalSubmit=:finalSubmit,createdBy=:createdBy,createdDate=:createdDate,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}
	
	
	public List<StudentNcIca> getUpdatedGrade(String icaId) {

		String sql = "select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
				+ "ss.username,ss.grade from users u, student_nc_ica ss where u.username =ss.username and ss.icaId=?";
		
		return findAllSQL(sql, new Object[]{icaId});
	}

}
