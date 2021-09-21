package com.spts.lms.daos.studentConfirmation;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmationPeriod;
import com.spts.lms.daos.BaseDAO;
@Repository("studentDetailConfirmationDAO")
public class studentDetailConfirmationDAO extends BaseDAO<studentDetailConfirmation>{

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "student_detail_confirmation";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO student_detail_confirmation (username,firstname,email,mobile,photo,mothername,fathername,active,createdBy,"
				+ "createdDate,lastModifiedBy,lastModifiedDate,secquestion,secAnswer,address) VALUES (:username,:firstname,:email,:mobile,:photo,:mothername,:fathername,:active,:createdBy,"
				+ ":createdDate,:lastModifiedBy,:lastModifiedDate,:secquestion,:secAnswer,:address)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		String sql=" update "
				+ getTableName()
				+ " set firstname=:firstname,mothername=:mothername,fathername=:fathername,active=:active where username=:username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public studentDetailConfirmation findByUserNamevalidate(String username)
	{
		String sql="select * from student_detail_confirmation where  active = 'Y' and username = ?";
		
		return findOneSQL(sql, new Object[] {username});
	}
	
	
	public  List<studentDetailConfirmation> findStatusListForAll(String username) {

      //  String sql = " SELECT username,firstname,fathername,mothername,email,mobile,photo,secquestion,secAnswer,status FROM student_detail_confirmation ";
        
//		String sql=" SELECT distinct sd.username,sd.firstname,sd.fathername,sd.mothername FROM student_detail_confirmation sd,student_detail_confirmation_period s,"
//				+ " users u WHERE sd.username=u.username AND s.programId=u.programId AND sd.active='Y' AND s.active='Y' AND s.createdBy= ? "; 
		String dateRangeSql = "SELECT value FROM lms_variables WHERE keyword='studentDetailsConfirmationReportRange'";
		int dateRange = getJdbcTemplate().queryForObject(dateRangeSql, Integer.class, new Object[] {});
		
		String sql=" SELECT distinct sd.username,sd.firstname,sd.fathername,sd.mothername,sd.address FROM student_detail_confirmation sd,student_detail_confirmation_period s,"
				+ " users u WHERE sd.username=u.username AND s.programId=u.programId AND (u.campusId=s.campusId OR s.campusId IS NULL  OR u.campusId='')  AND sd.active='Y' AND s.active='Y' AND s.createdBy= ? AND sd.createdDate >= DATE_SUB(now(), INTERVAL ? MONTH)";
		
        return findAllSQL(sql, new Object[]{username, dateRange});

  }
	
	public  List<studentDetailConfirmation> findStatusListForAllStudent(String username) {

       // String sql = " select * from student_detail_confirmation where active ='Y' and status ='N'";
		 String sql=" SELECT sd.* FROM student_detail_confirmation sd,student_detail_confirmation_period s,"
	        + " users u WHERE sd.username=u.username AND s.programId=u.programId AND sd.active='Y' AND s.active='Y' AND sd.status='N' AND s.createdBy = ?";
        
        return findAllSQL(sql, new Object[]{username});

  }

	public void updateMasterStatus(String username,String status,String remarks)
	{	 		
		
		String sql = " update student_detail_confirmation set status = ?,remarks= ? where username =?";
	             
		executeUpdateSql(sql, new Object[]{status,remarks,username});
		  
	}

	public studentDetailConfirmation findAllUserName(String username) {
		String sql="select username from student_detail_confirmation where active='Y' and username = ?";
		
		return findOneSQL(sql, new Object[] {username});
	}

	public List<String> findAllActiveStudent() {
		String sql="select username from student_detail_confirmation where active='Y' ";
		/*String sql="SELECT sd.* FROM student_detail_confirmation sd,student_detail_confirmation_period s,users u WHERE "
				+ " sd.username=u.username AND s.programId=u.programId AND s.createdBy = ? AND s.active = 'Y'";*/
		//return getJdbcTemplate().query(sql,new Object[] {username}, BeanPropertyRowMapper.newInstance(String.class));
		
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] {});
	}
	

	public List<studentDetailConfirmation> findActiveStudentForSupportAdmin() {
	
		String sql=" SELECT sdc.id,sdc.username,sdc.firstname,sdc.fathername,sdc.mothername,u.acadSession,p.programName FROM student_detail_confirmation sdc,users u,program p WHERE sdc.username=u.username AND p.id=u.programId and sdc.active='Y'";
		
	    return findAllSQL(sql, new Object[]{});
	}

	public void softDeleteById(String username) {
		
		String sql="update student_detail_confirmation set active='N' where username = ? ";
		
		executeUpdateSql(sql, new Object[]{username});
	}
	
	
}
