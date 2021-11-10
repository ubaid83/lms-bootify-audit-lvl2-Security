package com.spts.lms.daos.programCampus;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.daos.BaseDAO;

@Repository("programCampusDAO")
public class ProgramCampusDAO extends BaseDAO<ProgramCampus> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {

		return "program_campus";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " ( programId,"
				+ " campusId,campusAbbr,campusName,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ "(:programId,:campusId,:campusAbbr,:campusName,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = " update "
				+ getTableName()
				+ " set campusAbbr=:campusAbbr , campusName=:campusName,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate "
				+ " where programId =:programId , campusId=:campusId  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " ( programId,campusId,"
				+ " campusAbbr,campusName,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values"
				+ "(:programId,:campusId,:campusAbbr,:campusName,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)"
				+ "  ON DUPLICATE KEY UPDATE "
				+ "  campusAbbr=:campusAbbr, campusName=:campusName,lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}

	public List<ProgramCampus> getProgramCampusListFromDate(String schoolObjId,
			String maxLastUpdatedDate) {

		String sql = " select distinct p.* from program_campus p,programs pr where p.programId=pr.id and pr.schoolObjId = ? "
				+ "  and p.lastModifiedDate > ? ";

		return findAllSQL(sql, new Object[] { schoolObjId, maxLastUpdatedDate });
	}

	public List<ProgramCampus> getProgramCampusListBySchoolObjId(
			String schoolObjId) {

		String sql = " select distinct p.* from program_campus p,programs pr where p.programId=pr.id and pr.schoolObjId = ? ";

		return findAllSQL(sql, new Object[] { schoolObjId });
	}

	public List<ProgramCampus> getCampusListByProgram(String programId) {

		String sql = " select distinct campusId,campusName from program_campus where programId = ? ";

		return findAllSQL(sql, new Object[] { programId });
	}

	public List<ProgramCampus> getCampusList() {

		String sql = " select distinct campusId,campusName from program_campus  ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<String> getProgramsByCampusId(String campusId) {

		String sql = "select programId from program_campus where campusId = ?;";

		return getJdbcTemplate().queryForList(sql, new Object[] { campusId },
				String.class);
	}

	public List<ProgramCampus> getCampusNameDropDown(String campusName) {
		String sql = " SELECT DISTINCT  p.programName,pc.programId FROM program_campus pc, program p WHERE pc.campusName = ? AND p.id = pc.programId AND p.active='Y' ";
		return findAllSQL(sql, new Object[] { campusName });

	}

	public List<ProgramCampus> getCampusNameDropDownId(String programId) {
		String sql = " SELECT DISTINCT  pc.campusId,pc.campusAbbr FROM program_campus pc, program p WHERE pc.programId = ? AND p.id = pc.programId AND p.active='Y' ";

		return findAllSQL(sql, new Object[] { programId });

	}

	/*
	 * public ProgramCampus getCampusNameByCampusId(String campusid) { String
	 * sql=" select distinct campusAbbr from program_campus where campusId= ? ";
	 * return findOneSQL(sql, new Object[]{campusid}); }
	 */

	public ProgramCampus getCampusNameByCampusId(String campusid) {
		String sql = " select distinct campusAbbr from program_campus where campusId= ? or campusId is not null limit 1";
		return findOneSQL(sql, new Object[] { campusid });
	}

	public List<ProgramCampus> getProgramCampusName() {
		String sql = " SELECT * FROM program  where active ='Y' ";
		// String
		// sql=" SELECT DISTINCT  p.programName,pc.programId FROM program_campus pc, program p WHERE pc.campusName = 'MPSTME (MUMBAI)' AND p.id = pc.programId AND p.active='Y' ";
		return findAllSQL(sql, new Object[] {});
	}

	public List<ProgramCampus> getCampusesForSchool() {

		String sql = " select distinct campusId,campusAbbr from program_campus group by campusId ";

		return findAllSQL(sql, new Object[] {});
	}
	
	public String getCampusByCampusId(String campusId) {

		String sql = " select distinct campusAbbr from program_campus where campusId = ?  limit 1";

		try {
			return getJdbcTemplate().queryForObject(sql, String.class,
					new Object[] { campusId });
		} catch (Exception ex) {
			return "NA";
		}
	}

	
	public List<ProgramCampus> getCampusForSMaster() {
		String sql = " SELECT DISTINCT campusId,campusName FROM program_campus ";

		return findAllSQL(sql, new Object[] {});
	}
	
	
	

	public List<ProgramCampus> getCampusNameSupportAdmin() {
		String sql = " SELECT DISTINCT campusId,campusName FROM program_campus ";

		return findAllSQL(sql, new Object[] {});
	}

	public ProgramCampus checkIfCampusExists(Long campusId) {
		String sql = " SELECT distinct campusId from program_campus where campusId=?";
		return findOneSQL(sql, new Object[] {campusId});
	}
	
}
