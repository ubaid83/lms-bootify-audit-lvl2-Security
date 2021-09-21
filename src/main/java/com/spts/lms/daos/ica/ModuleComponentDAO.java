package com.spts.lms.daos.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.ModuleComponent;
import com.spts.lms.daos.BaseDAO;

@Repository("teeComponentDAO")
public class ModuleComponentDAO extends BaseDAO<ModuleComponent> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "module_components";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " ( componentName,"
				+ " componentDesc,moduleId,acadYear,programId,campusId,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ "(:componentName,:componentDesc,:moduleId,:acadYear,:programId,:campusId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " update "
				+ getTableName()
				+ " set componentName=:componentName , componentDesc=:componentDesc,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate "
				+ " where id=:id  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " (  componentName,"
				+ " componentDesc,moduleId,acadYear,programId,campusId,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ "(:componentName,:componentDesc,:moduleId,:acadYear,:programId,:campusId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)"
				+ "  ON DUPLICATE KEY UPDATE "
				+ "  componentName=:componentName , componentDesc=:componentDesc,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}

	public List<ModuleComponent> moduleComponentListByModuleId(String moduleId,
			String programId, String acadYear, String campusId) {

		if (campusId != null) {
			String sql = " select mc.* from "
					+ getTableName()
					+ " mc where mc.programId=? and mc.acadYear = ? and mc.campusId=?  "
					+ " and mc.moduleId=? " + " and mc.active = 'Y'  ";

			return findAllSQL(sql, new Object[] { programId, acadYear,
					campusId, moduleId });
		} else {
			String sql = " select mc.* from " + getTableName()
					+ " mc where mc.programId=? and mc.acadYear = ?  "
					+ " and mc.moduleId=? " + " and mc.active = 'Y'  ";
			return findAllSQL(sql,
					new Object[] { programId, acadYear, moduleId });
		}

	}

	public List<ModuleComponent> moduleComponentListByAcadYearAndCampus(
			String programId, String acadYear, String campusId) {

		if (campusId != null) {
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where p.id=? and c.acadYear = ? and c.campusId=?   "
					+ " and c.programId=p.id and c.active = 'Y' and c.campusId is not null "
					+ " and p.id=pc.programId and pc.campusId=c.campusId ";

			return findAllSQL(sql,
					new Object[] { programId, acadYear, campusId });
		} else {
			String sql = " select distinct c.moduleId,c.moduleName,c.moduleAbbr,c.acadSession,p.programName,pc.campusName "
					+ " from course c,program p,program_campus pc where p.id=? and c.acadYear = ?    "
					+ " and c.programId=p.id and c.active = 'Y'  "
					+ " and p.id=pc.programId and pc.campusId=c.campusId ";

			return findAllSQL(sql, new Object[] { programId, acadYear });
		}

	}
}
