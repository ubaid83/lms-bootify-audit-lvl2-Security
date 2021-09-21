package com.spts.lms.daos.grievances;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("grievancesDAO")
public class GrievancesDAO extends BaseDAO<Grievances> {

	@Override
	protected String getTableName() {
		return "grievances";
	}

	@Override
	protected String getInsertSql() {
		final String sql = " INSERT INTO grievances " + " ( " + " username, "
				+ " acadMonth, " + " acadYear, " + " typeOfGrievance, "
				+ " description, " + " grievanceCase," + "grievanceResponse,"
				+ "lastModifiedDate," + "grievanceResponseTimeStamp,"
				+ "grievanceStatus," + "grievanceReason" + " )" + " VALUES "
				+ " ( "

				+ " :username, " + " :acadMonth, " + " :acadYear, "
				+ " :typeOfGrievance, " + " :description, " + ":grievanceCase,"
				+ ":grievanceResponse," + ":lastModifiedDate,"
				+ ":grievanceResponseTimeStamp," + ":grievanceStatus,"
				+ ":grievanceReason" + ")";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = " UPDATE grievances " + " SET "
				+ " grievanceResponse = :grievanceResponse, "
				+ " grievanceResponseTimeStamp = :grievanceResponseTimeStamp, "
				+ " lastModifiedDate = :lastModifiedDate ,"
				+ " grievanceStatus= :grievanceStatus,"
				+ " grievanceReason= :grievanceReason" + " WHERE id = :id ";

		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Grievances> getGrievancesListByUser(Long id) {
		String sql = " select  lgg.* , lur.* from grievances lgg , user_roles lur  where "
				+ "  lgg.username=lur.username and lgg.id=? "
				+ "  GROUP BY lgg.id ";
		return findAllSQL(sql, new Object[] { id, Role.ROLE_ADMIN.name() });

	}

	public List<Grievances> getAllGrievances(){
		String sql = " select g.*,concat(u.firstName, ' ',u.lastName) as studentName from grievances g , "
				+" users u where g.username=u.username order by lastModifiedDate";
		return findAllSQL(sql, new Object[]{});
	}
	public void updateGrievance(Long id, String grievanceResponse) {
		String sql = " update grievances set grievanceResponse = ?   where id = ? ";
		executeUpdateSql(sql, new Object[] { grievanceResponse, id });
	}

	public List<Grievances> findByUserGrievances(String username) {
		final String sql = "select * " + " from grievances  "
				+ " where username= ? ";
		return findAllSQL(sql, new Object[] { username });
	}
	
    public String  findSrbPath(String id)
    {
          String sql="select filePath from srb where id = ? ";
          return getJdbcTemplate().queryForObject(sql, new Object[] {id},
                      String.class);
    }

		public String findSrbPathCheck() {
		
		 String sql="select filePath from srb where id = 1 limit 1";
         return getJdbcTemplate().queryForObject(sql, new Object[] {},
                     String.class);
	}

				public List<String> findPortalManualPath(List<String> authority) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("authority",authority);
		
			String sql="select filePath from portal_manuals where role in (:authority) and active='Y'";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
			
		}

	
}
