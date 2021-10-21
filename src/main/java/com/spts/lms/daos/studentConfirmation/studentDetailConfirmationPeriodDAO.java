package com.spts.lms.daos.studentConfirmation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.PredefinedIcaComponent;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmationPeriod;
import com.spts.lms.daos.BaseDAO;

@Repository("studentDetailConfirmationPeriodDAO")
public class studentDetailConfirmationPeriodDAO extends
		BaseDAO<studentDetailConfirmationPeriod> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "student_detail_confirmation_period";
	}

	@Override
	protected String getInsertSql() {

		final String sql = "INSERT INTO "
				+ getTableName()
				+ " (endDate,programId,active,createdBy,"
				+ "createdDate,lastModifiedBy,lastModifiedDate,sendEmailAlert,acadSession,campusId) VALUES (:endDate,:programId,:active,:createdBy,"
				+ ":createdDate,:lastModifiedBy,:lastModifiedDate,:sendEmailAlert,:acadSession,:campusId)";

		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql = "update student_detail_confirmation_period  "
				+ " set enddate=:endate, " + " programId=:programId, "
				+ " active=:active, " + " createdBy=:createdBy, "
				+ " createdDate=:createdDate, "
				+ " lastModifiedBy=:lastModifiedBy, "
				+ " lastModifiedDate=:lastModifiedDate " + " where id=:id";
		return sql;

	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<studentDetailConfirmationPeriod> studentDetailConfirmationPeriod(String username) {
		// String
		// sql="select * from student_detail_confirmation_period  WHERE  active ='Y' ORDER BY id DESC";

		/*
		 * String sql =
		 * "SELECT sdcp.id,sdcp.endDate,p.programName,sdcp.active,sdcp.createdby,sdcp.acadSession FROM student_detail_confirmation_period sdcp LEFT OUTER JOIN program p ON p.id=sdcp.programId WHERE sdcp.active='Y' and sdcp.createdBy =? ORDER BY sdcp.id DESC"
		 * ;
		 */
		
		String sql = " SELECT  sdcp.id,sdcp.endDate,p.programName,sdcp.active,sdcp.createdby,sdcp.acadSession,pc.campusName "
				+ " FROM student_detail_confirmation_period sdcp  "
				+ " LEFT OUTER JOIN program_campus pc ON pc.campusId=sdcp.campusId "
				+ " INNER JOIN program p ON p.id=sdcp.programId "
				+ " WHERE sdcp.active='Y' AND sdcp.createdBy = ?  GROUP BY sdcp.id ORDER BY sdcp.id DESC ";

		return findAllSQL(sql, new Object[] {username});
	}

	public void inactiveEndDate(String id) {
		String sql = "update student_detail_confirmation_period set active='N' where id = ? ";
		executeUpdateSql(sql, new Object[] { id });
	}

	public studentDetailConfirmationPeriod findByEndDate() {

		// String
		// sql="select endDate from student_detail_confirmation_period where endDate <= ? and programId IS NULL and active = 'Y' ";
		// String
		// sql="select endDate from student_detail_confirmation_period where endDate <= ?  and active = 'Y' ";
		String sql = "select MAX(endDate) from student_detail_confirmation_period where   active = 'Y' ";
		// String
		// sql="select endDate from student_detail_confirmation_period where  active = 'Y' ";
		return findOneSQL(sql, new Object[] {});
	}

	/*
	 * public studentDetailConfirmationPeriod findByEndDateForSchool(String
	 * endDate,String programId) { String sql=
	 * "select endDate,programId from student_detail_confirmation_period where endDate = ? and programId = ? and active = 'Y' "
	 * ; return findOneSQL(sql, new Object[]{endDate,programId}); }
	 */

	public List<studentDetailConfirmationPeriod> validationgStudentEndDate() {

		String sql = "SELECT MAX(endDate) FROM student_detail_confirmation_period where active ='Y'";
		return findAllSQL(sql, new Object[] {});
		// return findOneSQL(sql, new Object[]{});
	}

	public String findDateDifference() {
		// String
		// sql=" SELECT enddate FROM student_detail_confirmation_period WHERE  active='Y' ORDER BY id DESC ";

		String sql = " SELECT max(enddate) FROM student_detail_confirmation_period WHERE  active='Y' ";
		// String
		// sql="SELECT * FROM student_detail_confirmation_period WHERE AND CAST(ENDDATE AS DATE) active='Y'";
		// String
		// sql="SELECT * FROM student_detail_confirmation_period WHERE CURRENT_TIMESTAMP <= enddate AND active = 'Y' ORDER BY id DESC LIMIT 1";

		/*
		 * return getJdbcTemplate().queryForObject(sql, new Object[] { },
		 * String.class);
		 */
		return getJdbcTemplate().queryForObject(sql, new Object[] {},
				String.class);
		// return findOneSQL(sql, new Object[]{});
	}

	public List<String> findProgramForValidation() {
		String sql = " SELECT distinct programId FROM student_detail_confirmation_period WHERE active='Y' and (programId is not null or programId <>'' ) ";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] {});

	}

	/*
	 * public int findDateDiffrenceCount() { String sql=
	 * "SELECT COUNT(*) FROM student_detail_confirmation_period WHERE active='Y'"
	 * ; return getJdbcTemplate().queryForObject(sql, new
	 * Object[]{},Integer.class); }
	 */

	/*
	 * public String findAllActiveDatesForDisableBtn(){ String sql =
	 * " SELECT enddate FROM student_detail_confirmation_period WHERE active = 'Y' ORDER BY id DESC LIMIT 1 "
	 * ;
	 * 
	 * return getJdbcTemplate().queryForObject(sql, new Object[] { },
	 * String.class); }
	 */

	public String findMaximumDate() {
		String sql = "SELECT MAX(endDate) FROM student_detail_confirmation_period where active ='Y'";
		return getJdbcTemplate().queryForObject(sql, new Object[] {},
				String.class);
	}

	public String findProgramidForSchool(String username) {
		String sql = "SELECT max(endDate) FROM student_detail_confirmation_period where active ='Y' and createdBy =?";

		// return findAllSQL(sql, new Object[]{programId});

		return getJdbcTemplate().queryForObject(sql, new Object[] {username},
				String.class);
	}

	/*
	 * public String findDateDifferenceForAll(){ String sql="";
	 * 
	 * return getJdbcTemplate().queryForObject(sql, new Object[]{},
	 * String.class); }
	 */

	public List<String> findIfValueIsExist() {
		String sql = " SELECT  programId FROM student_detail_confirmation_period WHERE active='Y' and (programId is not null or programId <>'' ) ";

		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] {});
	}
	
	
	public String showMastervalidationAlert(String username) {
		String sql = " SELECT remarks FROM student_detail_confirmation WHERE  username=?";
		return getJdbcTemplate().queryForObject(sql, new Object[] {username},
				String.class);

	}


	
	
/*	public List<studentDetailConfirmationPeriod> getAcadSessionByProgramId(
			String masterProgramId) {
		
		String sql=" select distinct programId,acadSession,acadYear from course where programId = ? and active='Y' "
				+ " order by acadYear desc ";
		
		return findAllSQL(sql, new Object[] {masterProgramId});
	}
	*/
	
	public List<studentDetailConfirmationPeriod> getAcadSessionByProgramId(
			String masterProgramId) {
		List<studentDetailConfirmationPeriod> returnList = new ArrayList<studentDetailConfirmationPeriod>();
		String sql=" select distinct programId,acadSession,acadYear from course where programId = ? and active='Y' "
				+ " order by acadYear desc ";
		
		String sql1 ="select distinct programId,acadSession,enrollmentYear as acadYear from users where programId = ? and enrollMentYear >=2017 order by enrollmentYear desc";
		returnList = findAllSQL(sql1, new Object[] {masterProgramId});
		
		 returnList.addAll(findAllSQL(sql, new Object[] {masterProgramId}));
		List<studentDetailConfirmationPeriod> finalCList = returnList.stream().distinct().collect(Collectors.toList());
		 return finalCList;
	}
	

	//30-09-2021 New Changes
	public studentDetailConfirmationPeriod findbyProgramId(String programId,String usersession,String campusId) {
		if(campusId!= null && !campusId.equals("null") && !campusId.isEmpty() && !campusId.equals("0") && !campusId.equals("") && !campusId.equals("00000000")) {
	
			String sql = " SELECT acadSession, enddate FROM student_detail_confirmation_period  WHERE programId= ? "
					+ " AND active='Y' AND FIND_IN_SET(?,acadSession) AND campusId= ? AND enddate>= CURDATE()  GROUP BY programid ORDER BY endDate DESC";
			 return findOneSQL(sql, new Object[]{programId,usersession,campusId});
		}
		else
		{
			String sql = " SELECT acadSession, enddate FROM student_detail_confirmation_period  WHERE programId= ? "
					+ " AND active='Y' AND FIND_IN_SET(?,acadSession)  AND enddate>= CURDATE() GROUP BY programid ORDER BY endDate DESC";
		 return findOneSQL(sql, new Object[]{programId,usersession});
		}
	}
	
	public List<studentDetailConfirmationPeriod> fingAllActiveProgramWithSemester() {

		//String sql="SELECT sdp.endDate,p.programName,sdp.acadSession,sdp.createdBy,sdp.sendEmailAlert FROM student_detail_confirmation_period sdp,program p WHERE sdp.active='Y' AND sdp.programId=p.id ";
		String sql="SELECT sdp.endDate,p.programName,sdp.acadSession,CONCAT(u.firstname,' ',u.lastname,' (',sdp.createdBy,')') as createdBy,sdp.sendEmailAlert,sdp.campusId FROM student_detail_confirmation_period sdp,program p,users u WHERE sdp.active='Y' AND sdp.programId=p.id AND sdp.createdBy=u.username ";
		
		return findAllSQL(sql, new Object[] {});
		
	}
	

}
