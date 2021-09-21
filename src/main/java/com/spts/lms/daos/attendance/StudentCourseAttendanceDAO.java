package com.spts.lms.daos.attendance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.attendance.StudentCourseAttendance;
import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("studentCourseAttendanceDAO")
public class StudentCourseAttendanceDAO extends
		BaseDAO<StudentCourseAttendance> {

	private static final Logger logger = Logger
			.getLogger(StudentCourseAttendanceDAO.class);

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "student_course_attendance";
	}

	@Override
	protected String getInsertSql() {
        
        final String sql = "INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
                    + "facultyId,active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession,presentFacultyId) "
                    + "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession,:presentFacultyId)";
        return sql;

  }


	@Override
	protected String getUpdateSql() {
        final String sql = "Update student_course_attendance set "
                    + "rollNo = :rollNo ,"
                    + "status = :status ," + "facultyId = :facultyId , presentFacultyId=:presentFacultyId, " 
                    + "noOfLec = :noOfLec ,"
                    + "active = :active ," + "lastModifiedBy = :lastModifiedBy ,"
                    + "lastModifiedDate = :lastModifiedDate " + "where startTime = :startTime and endTime = :endTime and"
                                + " username = :username and courseId = :courseId";
        return sql;
  }


	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		String sql ="INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
       		 + "facultyId,active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession,presentFacultyId) "
       		 + "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession,:presentFacultyId)"
       		 + "ON DUPLICATE KEY UPDATE status=:status, lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate";
		
		return sql;
	}

	public List<StudentCourseAttendance> findStudentsForAttendance(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, a.status, a.reason "
				+ "  from user_course uc "
				+ "  inner join users u on uc.username = u.username"
				+ "  inner join program p on u.programId = p.id"
				+ " left outer join student_course_attendance a on a.username = u.username and a.courseId = uc.courseId "
				+ "  where" + "  uc.role = ? and uc.courseId = ?   ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId });
	}

	public List<StudentCourseAttendance> findByCourseId(Long courseId) {
		String sql = "select * from " + getTableName() + " where courseId=?";
		return findAllSQL(sql, new Object[] { courseId });

	}

	public StudentCourseAttendance findByCourseIdAndStudent(Long courseId,
			String username) {
		String sql = "select * from " + getTableName()
				+ " where courseId = ? and username = ?";
		return findOneSQL(sql, new Object[] { courseId, username });
	}

	/*
	 * public List<StudentCourseAttendance> findByCourseIdAndDateTime( String
	 * courseId, String startTime, String endTime, String facultyId) { String sql =
	 * "select sca.courseId, sca.username, sca.rollNo, sca.status, u.firstname, u.lastname"
	 * + " from student_course_attendance sca, users u" +
	 * " where sca.username = u.username and sca.courseId = ? and" +
	 * " sca.startTime = ? and sca.endTime = ? and sca.facultyId = ? and sca.active='Y' and sca.active='Y' and sca.active='Y' order by sca.rollNo"
	 * ; return findAllSQL(sql, new Object[] { courseId, startTime, endTime,
	 * facultyId }); }
	 */

	public List<StudentCourseAttendance> getAttendanceStatByUsernameAndCourseId(
			String username, String courseId) {
		String sql = " SELECT Year(startTime) as attdYear, Month(startTime) as attdMonth, "
				+ " Count(case when status ='Absent'  then 1 end) as absent_count, "
				+ " Count(case when status ='Present'  then 1 end) as present_count, "
				+ " count(*) as total_count FROM student_course_attendance "
				+ " WHERE username = ? and courseId = ? "
				+ " and ( startTime >= now() - INTERVAL 1 YEAR ) "
				+ " GROUP BY Year(startTime),Month(startTime) ";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<StudentCourseAttendance> getAttendanceStatByUsernameAndCourseId(
			String username, String startDate, String endDate) {
		String sql = " SELECT sca.courseId,c.courseName, Count(case when sca.status ='Absent'  then 1 end) as absent_count, "
				+ " Count(case when sca.status ='Present'  then 1 end) as present_count, "
				+ " count(*) as total_count FROM student_course_attendance sca, course c "
				+ " WHERE sca.courseId=c.id and sca.username = ? "
				+ " and ( sca.startTime >= ? and sca.startTime <= ? ) "
				+ " GROUP BY sca.courseId ";
		return findAllSQL(sql, new Object[] { username, startDate, endDate });
	}

	public StudentCourseAttendance getAllPresentRecord(String courseId,
			String startDate, String endDate, String facultyId) {
		String sql = "select * from student_course_attendance sca "
				+ "where sca.courseId = ? and "
				+ " sca.startTime = ? and sca.endTime = ? and sca.facultyId = ? and sca.username is null";
		return findOneSQL(sql, new Object[] { courseId, startDate, endDate,
				facultyId });
	}

	public void updateDelFlag(String value, long id) {
		executeUpdateSql(
				" update student_course_attendance set delFlag = ? where id = ? ",
				new Object[] { value, id });
	}

	public List<StudentCourseAttendance> getAbsentRecords() {
		String sql = " select * from student_course_attendance sca "
				+ "where sca.status = 'Absent' and DATE(sca.endTime) = CURDATE() "
				+ "and DATE_ADD(sca.endTime, INTERVAL 2 HOUR) < NOW() and (sca.sapOperation is null or sca.sapOperation = 'F') ";
		return findAllSQL(sql, new Object[] {});
	}

	public List<StudentCourseAttendance> getPresentRecords() {
		String sql = " select * from student_course_attendance where username is null and (sapOperation is null or sapOperation = 'F') and delFlag = 'N' ";
		return findAllSQL(sql, new Object[] {});
	}
	
	/*public List<StudentCourseAttendance> getLastAbsentRecords() {
		String sql = " select * from student_course_attendance sca "
				+ " where sca.status = 'Absent' and DATE(sca.endTime) = ( CURDATE() - INTERVAL 1 DAY ) "
				+ " and (sca.sapOperation is null or sca.sapOperation = 'F') ";
		return findAllSQL(sql, new Object[] {});
	}*/
	
	public List<StudentCourseAttendance> getLastAbsentRecords() {
		String sql = " select * from student_course_attendance sca "
				+ " where sca.status = 'Absent' "
				+ " and (sca.sapOperation is null or sca.sapOperation = 'F') ";
		return findAllSQL(sql, new Object[] {});
	}

	public void updateSapOperation(String value, String ids) {
		executeUpdateSql(
				" update student_course_attendance set sapOperation = ?, lastModifiedDate = now() where id IN("
						+ ids + ") ", new Object[] { value });
	}
	
	public List<StudentCourseAttendance> getAbsentRecordsByDate(String date) {
        String sql = " select * from student_course_attendance where startTime like ? and (sapOperation is null or sapOperation = 'F') and status = 'Absent' ";
        return findAllSQL(sql, new Object[] { date });
  }
  
  public List<StudentCourseAttendance> getPresentRecordsByDate(String date) {
        String sql = " select * from student_course_attendance where startTime like ? and (sapOperation is null or sapOperation = 'F') and status = 'Present' "
                    + " and username is null and delFlag = 'N' ";
        return findAllSQL(sql, new Object[] { date });
  }
  
  public List<StudentCourseAttendance> getAbsentRecordsByDateForApp(String date, String dateLike) {
      String sql = " select * from student_course_attendance where startTime < ? and startTime like ? and (sapOperation is null or sapOperation = 'F') and status = 'Absent' ";
      return findAllSQL(sql, new Object[] { date, dateLike });
  }
	
	
	 public List<StudentCourseAttendance> getPresentRecordsByDateForApp(String date, String dateLike) {
    String sql = " select * from student_course_attendance where startTime < ? and startTime like ? and (sapOperation is null or sapOperation = 'F') and status = 'Present' "
                + " and username is null and delFlag = 'N' ";
    return findAllSQL(sql, new Object[] { date, dateLike });
}

  
  public List<StudentCourseAttendance> getStudentCourseAttendanceForReport(String dateString, List<String> facultyList) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateString", dateString);
		params.put("facultyList", facultyList);
		
		String sql = " select distinct(facultyId) as facultyId, count(distinct startTime) as attendanceTakenCount from student_course_attendance where startTime like :dateString and facultyId in (:facultyList) group by facultyId ";
			
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentCourseAttendance.class));
	}

  public List<StudentCourseAttendance> getMarkedAttendaceCourseId(){
		
		String sql = "select distinct courseId from student_course_attendance sca "
				+ "where (sca.status = 'Absent' or (sca.status = 'Present' and sca.username is null and sca.delFlag = 'N' )) and "
				+ "DATE(sca.endTime) = CURDATE() and (sca.sapOperation is null or sca.sapOperation = 'F')";
		
	return findAllSQL(sql, new Object[] { });
	}
	public List<StudentCourseAttendance> getAbsentRecordsByCourseId(String courseId) {
		String sql = " select * from student_course_attendance sca "
				+ "where (sca.status = 'Absent' or (sca.status = 'Present' and sca.username is null and sca.delFlag = 'N' )) and DATE(sca.endTime) = CURDATE() "
				+ "and DATE_ADD(sca.endTime, INTERVAL 2 HOUR) < NOW()"
				+ "and (sca.sapOperation is null or sca.sapOperation = 'F') and sca.courseId = ?";
		return findAllSQL(sql, new Object[] {courseId});
	}
	public List<StudentCourseAttendance> findByCourseIdAndDateTime(
			List<Long> courseId, String startTime, String endTime, String facultyId) {
	  
	  Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("facultyId", facultyId);
		
		String sql = "select sca.courseId, sca.username, sca.rollNo, sca.status,sca.presentFacultyId, u.firstname, u.lastname, sca.facultyId"
				+ " from student_course_attendance sca, users u"
				+ " where sca.username = u.username and sca.courseId in (:courseId) and"
				+ " sca.startTime = (:startTime) and sca.endTime = (:endTime) and sca.facultyId = (:facultyId) and sca.active='Y' order by sca.rollNo";
		
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentCourseAttendance.class));
	}
	 public List<StudentCourseAttendance> getAllPresentRecord(List<Long> courseId,
			String startDate, String endDate, String facultyId) {
	  
	  Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("facultyId", facultyId);
		
		String sql = "select * from student_course_attendance sca "
				+ "where sca.courseId in (:courseId) and "
				+ " sca.startTime = :startDate and sca.endTime = :endDate and sca.facultyId = :facultyId and sca.username is null";
		
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentCourseAttendance.class));
	}
	 
	     public List<StudentCourseAttendance> getDistinctEndTime(String startTime, String status)
	    {
		  String sql ="";
		  if(status.equals("Present"))
		  {
			 sql = "select distinct endTime from student_course_attendance where startTime like ? and status = 'Present' and (sapOperation is NULL or sapOperation = 'F') and username is NULL and delFlag = 'N'"; 
		  }
		  else if(status.equals("Absent"))
		  {
			  sql = "select distinct endTime from student_course_attendance where startTime like ? and status = 'Absent' and (sapOperation is NULL or sapOperation = 'F')" ;
		  }
		  
	      return findAllSQL(sql, new Object[] { startTime+"%"});
	   }
	     
	     
	     public void upsertAttendance(List<StudentCourseAttendance> studentCourseAttendance){
             String sql ="INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
            		 + "facultyId,active,createdBy,createdDate,lastModifiedBy,lastModifiedDate,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession) "
            		 + "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession)"
            		 + "ON DUPLICATE KEY UPDATE status=:status, lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate";
             
             SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(studentCourseAttendance
                                                          .toArray());
                          int[] updateCounts = getNamedParameterJdbcTemplate().batchUpdate(sql,
                                                          batch);
}
	     
	/*
	 * public Integer getStudentCountByCourseIdStartEndTime(List<Long> courseId,
	 * String startTime, String endTime) { Map<String, Object> params = new
	 * HashMap<String, Object>(); params.put("courseId", courseId);
	 * params.put("startTime", startTime); params.put("endTime", endTime); String
	 * sql =
	 * " select count(*) as count from student_course_attendance where courseId in (:courseId) and startTime = (:startTime) and endTime = (:endTime)"
	 * ; return getNamedParameterJdbcTemplate().queryForObject(sql, params,
	 * Integer.class); }
	 */
	     
	     public Integer getStudentCountByCourseIdStartEndTime(List<Long> courseId,
		 			String startTime, String endTime, String facultyId) {
		 		Map<String, Object> params = new HashMap<String, Object>();
		 		params.put("courseId", courseId);
		 		params.put("startTime", startTime);
		 		params.put("endTime", endTime);
		 		params.put("facultyId", facultyId);
		 		String sql = " select count(*) as count from student_course_attendance where courseId in (:courseId) and startTime = (:startTime) and endTime = (:endTime) and facultyId = (:facultyId)";
		 		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
		 				Integer.class);
		 	}

	     public List<StudentCourseAttendance> findByCourseIdAndDate(List<Long> courseId, String startTime, String facultyId) {
	   	  
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("courseId", courseId);
	    		params.put("startTime", startTime+"%");
	    		//params.put("endTime", endTime+"%");
	    		params.put("facultyId", facultyId);

	    		/*String sql = "select sca.courseId, sca.username, sca.rollNo, sca.status, u.firstname, u.lastname"
	    				+ " from student_course_attendance sca, users u"
	    				+ " where sca.username = u.username and sca.courseId in (:courseId) and"
	    				+ " sca.startTime like (:startTime) and sca.facultyId = (:facultyId) and sca.active='Y' group by sca.username  order by sca.rollNo";*/

	    		String sql ="select sca.courseId, sca.username, sca.rollNo, sca.status,sca.presentFacultyId, u.firstname, u.lastname, sca.startTime "
	    				+ "from student_course_attendance sca, users u "
	    				+ "where sca.username = u.username and sca.courseId in (:courseId) and "
	    				+ "sca.startTime = "
	    				+ "(select MAX(sca1.startTime) from student_course_attendance sca1 where sca1.startTime like (:startTime) "
	    				+ "and sca1.courseId in (:courseId) and sca1.facultyId = (:facultyId) and sca1.active='Y') "
	    				+ "and sca.facultyId = (:facultyId) and sca.active='Y' group by sca.username order by sca.rollNo";

	    		return getNamedParameterJdbcTemplate().query(sql, params,
	    				BeanPropertyRowMapper.newInstance(StudentCourseAttendance.class));
	    	}
	     
	     public String getMarkedAttendanceMaxEndTime(List<Long> courseId, String startDate, String facultyId){
	    	 
	    	 Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("courseId", courseId);
	    		params.put("startDate", startDate+"%");
	    		//params.put("endTime", endTime+"%");
	    		params.put("facultyId", facultyId);
	    		
	     	String sql ="select MAX(sca1.endTime) from student_course_attendance sca1 where sca1.startTime like (:startDate) "
	     			+ "and sca1.courseId in (:courseId) and sca1.facultyId = (:facultyId) and sca1.active='Y'";
	     	return getNamedParameterJdbcTemplate().queryForObject(sql, params, String.class);
	     }
	     
	     // 08-05-2020 shubham
	     
	     
	     public String getPlayerIdForFaculty(String username) {

		 		String sql = " select playerId from user_playerid where username = ? ";
		 		return getJdbcTemplate().queryForObject(sql, new Object[] { username },
		 				String.class);
		 	 }
			 
			  public String getCourseNameFromCourseId(String id) {
			String sql = " select courseName from course where id = ? ";
			return getJdbcTemplate().queryForObject(sql, new Object[] { id },
					String.class);
		  }
		  
	/*
	 * public int[] upsertBatchByApp( final List<StudentCourseAttendance>
	 * studentCourseAttdList) {
	 * 
	 * String sql =
	 * "INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
	 * +
	 * "facultyId,active,createdBy,createdDateApp,lastModifiedBy,lastModifiedDateApp,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession) "
	 * +
	 * "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:active,:createdBy,now(),:lastModifiedBy,now(),:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession) "
	 * +
	 * "ON DUPLICATE KEY UPDATE status=:status, lastModifiedBy=:lastModifiedBy, lastModifiedDate=now()"
	 * ; List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>(
	 * studentCourseAttdList.size()); for (StudentCourseAttendance bean :
	 * studentCourseAttdList) {
	 * 
	 * parameters.add(getParameterSource(bean)); } SqlParameterSource[] param =
	 * parameters .toArray(new SqlParameterSource[parameters.size()]);
	 * 
	 * return getNamedParameterJdbcTemplate().batchUpdate(sql, param); }
	 */
			  
			  public int[] upsertBatchByApp(
						final List<StudentCourseAttendance> studentCourseAttdList) {

					String sql = "INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
							+ "facultyId,presentFacultyId,active,createdBy,createdDateApp,lastModifiedBy,lastModifiedDateApp,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession) "
							+ "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:presentFacultyId,:active,:createdBy,now(),:lastModifiedBy,now(),:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession) "
					        + "ON DUPLICATE KEY UPDATE status=:status, lastModifiedBy=:lastModifiedBy, lastModifiedDate=now()";
					List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>(
							studentCourseAttdList.size());
					for (StudentCourseAttendance bean : studentCourseAttdList) {

						parameters.add(getParameterSource(bean));
					}
					SqlParameterSource[] param = parameters
							.toArray(new SqlParameterSource[parameters.size()]);

					return getNamedParameterJdbcTemplate().batchUpdate(sql, param);
				}
			
			public Integer getStudentDataCountSentToSap(String courseId,
				String startTime, String endTime, String facultyId) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courseId", courseId);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("courseId", courseId);
			params.put("facultyId", facultyId);
			String sql = " select count(*) from student_course_attendance where sapOperation ='S' and startTime = (:startTime) and endTime = (:endTime) and "
					+ " courseId = (:courseId) and facultyId = (:facultyId) ";
			return getNamedParameterJdbcTemplate().queryForObject(sql, params,
					Integer.class);
		}
		
	/*
	 * public int insertApp(StudentCourseAttendance bean) { String sql =
	 * "INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
	 * +
	 * "facultyId,active,createdBy,createdDateApp,lastModifiedBy,lastModifiedDateApp,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession) "
	 * +
	 * "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:active,:createdBy,now(),:lastModifiedBy,now(),:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession)"
	 * ; SqlParameterSource parameterSource = getParameterSource(bean); int updated
	 * = getNamedParameterJdbcTemplate().update(sql, parameterSource); return
	 * updated; }
	 */
			
			public int insertApp(StudentCourseAttendance bean)
			{
		        String sql = "INSERT INTO student_course_attendance(courseId,startTime,endTime,username,rollNo,status,reason,"
						+ "facultyId,presentFacultyId,active,createdBy,createdDateApp,lastModifiedBy,lastModifiedDateApp,noOfLec,flag,sapOperation,delFlag,acadYear,acadSession) "
						+ "VALUES (:courseId,:startTime,:endTime,:username,:rollNo,:status,:reason,:facultyId,:presentFacultyId,:active,:createdBy,now(),:lastModifiedBy,now(),:noOfLec,:flag,:sapOperation,:delFlag,:acadYear,:acadSession)";
				SqlParameterSource parameterSource = getParameterSource(bean);
				int updated = getNamedParameterJdbcTemplate().update(sql,
						parameterSource);
				return updated;
			}
		
	/*
	 * public int[] updateBatchByApp(final List<StudentCourseAttendance>
	 * studentCourseAttdList) {
	 * 
	 * String sql = "Update student_course_attendance set " + "rollNo = :rollNo ," +
	 * "status = :status ," + "facultyId = :facultyId ," + "noOfLec = :noOfLec ," +
	 * "active = :active ," + "lastModifiedBy = :lastModifiedBy ," +
	 * "lastModifiedDateApp = now() " +
	 * "where startTime = :startTime and endTime = :endTime and" +
	 * " username = :username and courseId = :courseId";
	 * 
	 * List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>(
	 * studentCourseAttdList.size()); for (StudentCourseAttendance bean :
	 * studentCourseAttdList) {
	 * 
	 * parameters.add(getParameterSource(bean)); } SqlParameterSource[] param =
	 * parameters .toArray(new SqlParameterSource[parameters.size()]);
	 * 
	 * return getNamedParameterJdbcTemplate().batchUpdate(sql, param); }
	 */
			
			
			
			public int[] updateBatchByApp(final List<StudentCourseAttendance> studentCourseAttdList) {

				String sql = "Update student_course_attendance set "
						+ "rollNo = :rollNo ," + "status = :status ,"
						+ "facultyId = :facultyId ," + "presentFacultyId = :presentFacultyId ," + "noOfLec = :noOfLec ,"
						+ "active = :active ," + "lastModifiedBy = :lastModifiedBy ,"
						+ "lastModifiedDateApp = now() "
						+ "where startTime = :startTime and endTime = :endTime and"
						+ " username = :username and courseId = :courseId";

				List<SqlParameterSource> parameters = new ArrayList<SqlParameterSource>(
						studentCourseAttdList.size());
				for (StudentCourseAttendance bean : studentCourseAttdList) {

					parameters.add(getParameterSource(bean));
				}
				SqlParameterSource[] param = parameters
						.toArray(new SqlParameterSource[parameters.size()]);

				return getNamedParameterJdbcTemplate().batchUpdate(sql, param);
			}
			
		
	/*
	 * public List<StudentCourseAttendance> findByCourseIdAndDateTime( String
	 * courseId, String startTime, String endTime, String facultyId) { String sql =
	 * "select sca.courseId, sca.createdDateApp, sca.lastModifiedDateApp, sca.username, sca.rollNo, sca.status, u.firstname, u.lastname"
	 * + " from student_course_attendance sca, users u" +
	 * " where sca.username = u.username and sca.courseId = ? and" +
	 * " sca.startTime = ? and sca.endTime = ? and sca.facultyId = ? and sca.active='Y' and sca.active='Y' and sca.active='Y' order by sca.rollNo"
	 * ; return findAllSQL(sql, new Object[] { courseId, startTime, endTime,
	 * facultyId }); }
	 */
		
		public List<StudentCourseAttendance> findByCourseIdAndDateTime(
				String courseId, String startTime, String endTime, String facultyId) {
			String sql = "select sca.courseId, sca.createdDateApp, sca.lastModifiedDateApp, sca.username, sca.rollNo, sca.status, sca.presentFacultyId, u.firstname, u.lastname"
					+ " from student_course_attendance sca, users u"
					+ " where sca.username = u.username and sca.courseId = ? and"
					+ " sca.startTime = ? and sca.endTime = ? and sca.facultyId = ? and sca.active='Y' and sca.active='Y' and sca.active='Y' order by sca.rollNo";
			return findAllSQL(sql, new Object[] { courseId, startTime, endTime,
					facultyId });
		}
		
		
	    //17-06-2020 
		public void updateDelFlag(String value,String presentFacultyId, long id) {
			executeUpdateSql(
					" update student_course_attendance set delFlag = ?,presentFacultyId=? where id = ? ",
					new Object[] { value, presentFacultyId, id });
		}
	     
		public List<StudentCourseAttendance> findByCourseIdAndDateTimeAndPresentFacultyId(
				List<Long> courseId, String startTime, String endTime, String facultyId) {
		  
		  Map<String, Object> params = new HashMap<String, Object>();
			params.put("courseId", courseId);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("facultyId", facultyId);
			
			String sql = "select sca.courseId, sca.username, sca.rollNo, sca.status,sca.presentFacultyId,sca.facultyId, u.firstname, u.lastname"
					+ " from student_course_attendance sca, users u"
					+ " where sca.username = u.username and sca.courseId in (:courseId) and"
					+ " sca.startTime = (:startTime) and sca.endTime = (:endTime) and FIND_IN_SET(:facultyId, REPLACE(presentFacultyId,' ',''))  and sca.active='Y' order by sca.rollNo";
			
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentCourseAttendance.class));
		}
		public List<StudentCourseAttendance> findByCourseIdAndDateTimeForAbsentFaculty(
				List<Long> courseId, String startTime, String endTime, String facultyId,List<String> facultyIds) {
		  
		  Map<String, Object> params = new HashMap<String, Object>();
			params.put("courseId", courseId);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("facultyId", facultyId);
			params.put("facultyIds", facultyIds);
			
			String sql = "select sca.courseId, sca.username, sca.rollNo, sca.status,sca.presentFacultyId,sca.facultyId, u.firstname, u.lastname"
					+ " from student_course_attendance sca, users u"
					+ " where sca.username = u.username and sca.courseId in (:courseId) and"
					+ " sca.startTime = (:startTime) and sca.endTime = (:endTime) and sca.facultyId in (:facultyIds) and sca.active='Y' order by sca.rollNo";
			
			return getNamedParameterJdbcTemplate().query(sql, params,
					BeanPropertyRowMapper.newInstance(StudentCourseAttendance.class));
		}
		
		
		
		
		
		
		
		
		
		public Integer getDataCountByOneFacultyId(String courseId,
				String startTime, String endTime, String facultyId) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courseId", courseId);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			params.put("courseId", courseId);
			params.put("facultyId", facultyId);
			String sql = " select count(*) from student_course_attendance where startTime = (:startTime) and endTime = (:endTime) and "
					+ " courseId = (:courseId) and facultyId = (:facultyId) ";
			return getNamedParameterJdbcTemplate().queryForObject(sql, params,
					Integer.class);
		}
		
		
	public Integer getDataCountByAllFacultyId(String courseId,
			String startTime, String endTime, List<Long> facultyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("courseId", courseId);
		params.put("facultyId", facultyId);
		String sql = " select count(*) from student_course_attendance where startTime = (:startTime) and endTime = (:endTime) and "
				+ " courseId = (:courseId) and facultyId in (:facultyId) ";
		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				Integer.class);
	}	
	
	public List<StudentCourseAttendance> getDataForAllFacultyId(List<Long> courseId,
			String startTime, String endTime, List<Long> facultyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("courseId", courseId);
		params.put("facultyId", facultyId);
		String sql = " select * from student_course_attendance where startTime = (:startTime) and endTime = (:endTime) and "
				+ " courseId in (:courseId) and facultyId in (:facultyId) ";
		
		return getNamedParameterJdbcTemplate().query(
				sql,
				params,
				BeanPropertyRowMapper
						.newInstance(StudentCourseAttendance.class));
	}
	     
	     
}
