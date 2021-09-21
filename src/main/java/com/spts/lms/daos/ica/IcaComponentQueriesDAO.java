package com.spts.lms.daos.ica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaComponentQueries;
import com.spts.lms.daos.BaseDAO;

@Repository
public class IcaComponentQueriesDAO extends BaseDAO<IcaComponentQueries>{
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "ica_component_queries";

	}

	@Override
	protected String getInsertSql() {

		final String sql ="INSERT INTO "
				+ getTableName()
				+ " (icaId,componentId,username,query,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:icaId,:componentId,:username,:query,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " query = :query," 
				
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate"

			

				+ " where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	

}
