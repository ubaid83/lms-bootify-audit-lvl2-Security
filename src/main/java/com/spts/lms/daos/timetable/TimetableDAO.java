package com.spts.lms.daos.timetable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.timetable.Timetable;
import com.spts.lms.daos.BaseDAO;

@Repository("timetableDAO")
public class TimetableDAO extends BaseDAO<Timetable> {

	@Override
	protected String getTableName() {

		return "timetable";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO timetable (eventId, programId, facultyId, input_json, created_by,"
				+ " created_on, last_updated_by, last_updated_on)"
				+ " VALUES (:eventId, :programId, :facultyId, :input_json, :created_by, :created_on,"
				+ " :last_updated_by, :last_updated_on) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	/*public List<Timetable> getTimetableByCourse(Long programId, Long eventId,
			Long username, String curDate) {
		String sql = " select eventId, programId, facultyId, "
				+ " REPLACE(json_extract(input_json, '$.flag'),'\"','') as flag, "
				+ " REPLACE(json_extract(input_json, '$.class_date'),'\"','') as class_date, "
				+ " REPLACE(json_extract(input_json, '$.start_time'),'\"','') as start_time, "
				+ " REPLACE(json_extract(input_json, '$.end_time'),'\"','') as end_time "
				+ " from timetable where eventId = ? and ? in (programId) "
				+ " and ? in (facultyId) and "
				+ " json_extract(input_json, '$.class_date') like ? ";
		return findAllSQL(sql, new Object[] { eventId, programId, username,
				curDate });
	}*/
	
	/*public List<Timetable> getTimetableByCourse(Long programId, Long eventId,
			Long username, String curDate) {
		String sql = " select eventId, programId, facultyId, "
				+ " REPLACE(json_extract(input_json, '$.flag'),'\"','') as flag, "
				+ " REPLACE(json_extract(input_json, '$.class_date'),'\"','') as class_date, "
				+ " REPLACE(json_extract(input_json, '$.start_time'),'\"','') as start_time, "
				+ " REPLACE(json_extract(input_json, '$.end_time'),'\"','') as end_time "
				+ " from timetable where eventId = ? and ? in (programId) "
				+ " and ? in (facultyId) and "
				+ " json_extract(input_json, '$.class_date') like ? ";
		return findAllSQL(sql, new Object[] { eventId, programId, username,
				curDate });
	}*/
	public List<Timetable> getTimetableByCourse(Long programId, Long eventId,
			  String username, String curDate) {
			 
			 if(username.contains("_")){
			  username = username.substring(0,username.indexOf("_"));
			 }
			 String sql = " select eventId, programId, facultyId, "
			   + " REPLACE(json_extract(input_json, '$.flag'),'\"','') as flag, "
			   + " REPLACE(json_extract(input_json, '$.class_date'),'\"','') as class_date, "
			   + " REPLACE(json_extract(input_json, '$.start_time'),'\"','') as start_time, "
			   + " REPLACE(json_extract(input_json, '$.end_time'),'\"','') as end_time "
			   + " from timetable where eventId = ? and FIND_IN_SET(?, REPLACE(programId,' ','')) "
			   + " and FIND_IN_SET(?, REPLACE(facultyId,' ','')) and "
			   + " json_extract(input_json, '$.class_date') like ? ";
			 return findAllSQL(sql, new Object[] { eventId, programId, username,
			   curDate });
			}
	public List<Timetable> getTimetableByTime() {
		String sql = " select *, "
				+ " STR_TO_DATE(REPLACE(json_extract(input_json, '$.start_time'),'\"',''), '%d-%m-%Y %H.%i.%S') as start_time "
				+ " from timetable "
				+ " where STR_TO_DATE(REPLACE(json_extract(input_json, '$.start_time'),'\"',''), '%d-%m-%Y %H.%i.%S') BETWEEN NOW() and (NOW() + INTERVAL 15 MINUTE); ";
		return findAllSQL(sql, new Object[] {});
	}
	
	/*public List<Timetable> getTimetableByCourseForApp(Long username, String curDate) {
		String sql = " select eventId, programId, facultyId, "
				+ " REPLACE(json_extract(input_json, '$.flag'),'\"','') as flag, "
				+ " REPLACE(json_extract(input_json, '$.class_date'),'\"','') as class_date, "
				+ " REPLACE(json_extract(input_json, '$.start_time'),'\"','') as start_time, "
				+ " REPLACE(json_extract(input_json, '$.end_time'),'\"','') as end_time "
				+ " from timetable where "
				+ " ? in (facultyId) and "
				+ " json_extract(input_json, '$.class_date') like ? and programId <> '' and programId is not null order by start_time";
		return findAllSQL(sql, new Object[] {username,
				curDate });
	}*/
	public List<Timetable> getTimetableByCourseForApp(String username, String curDate) {
		 if(username.contains("_")){
		  username = username.substring(0,username.indexOf("_"));
		 }
		 String sql = " select eventId, programId, facultyId, "
		   + " REPLACE(json_extract(input_json, '$.flag'),'\"','') as flag, "
		   + " REPLACE(json_extract(input_json, '$.class_date'),'\"','') as class_date, "
		   + " REPLACE(json_extract(input_json, '$.start_time'),'\"','') as start_time, "
		   + " REPLACE(json_extract(input_json, '$.end_time'),'\"','') as end_time "
		   + " from timetable where "
		   + " FIND_IN_SET(?, REPLACE(facultyId,' ','')) and "
		   + " json_extract(input_json, '$.class_date') like ? and programId <> '' and programId is not null order by start_time";
		 return findAllSQL(sql, new Object[] {username,
		   curDate });
		}
	
	public List<Timetable> getTimetableDataByFaculties(String dateString,List<String> facultyList) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dateString", dateString);
		params.put("facultyList", facultyList);
		
		String sql = "select * from timetable where json_extract(input_json, '$.class_date') like :dateString and facultyId in (:facultyList)";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Timetable.class));
	}
	
	public Timetable getTimeToSend(String eventId, String programId ,String currDate){
		String sql ="select eventId,programId,MAX(JSON_EXTRACT(input_json,'$.end_time')) as end_time "
				+ "from timetable where eventId = ? and FIND_IN_SET(?, REPLACE(programId,' ','')) and JSON_EXTRACT(input_json,'$.class_date') like ?";
		
		return findOneSQL(sql, new Object[]{eventId,programId,currDate});
	}
	
	public List<Timetable> getTimetableByUsernameAndDate(String username, String curDate) {
		 if(username.contains("_")){
		  username = username.substring(0,username.indexOf("_"));
		 }
		 String sql = " select eventId, programId, facultyId, "
		   + " REPLACE(json_extract(input_json, '$.flag'),'\"','') as flag, "
		   + " REPLACE(json_extract(input_json, '$.class_date'),'\"','') as class_date, "
		   + " REPLACE(json_extract(input_json, '$.start_time'),'\"','') as start_time, "
		   + " REPLACE(json_extract(input_json, '$.end_time'),'\"','') as end_time, "
		   + " REPLACE(json_extract(input_json, '$.type'),'\"','') as type "
		   + " from timetable where "
		   + " FIND_IN_SET(?, REPLACE(facultyId,' ','')) and "
		   + " json_extract(input_json, '$.class_date') like ? and programId <> '' and programId is not null order by start_time";
		 return findAllSQL(sql, new Object[] {username,
		   curDate });
		}
	
	public String getAcadYearFromSapMaster(String eventId,String programId){
        
        String sql ="select distinct(REPLACE(JSON_EXTRACT(input_json,'$.acad_year'),'\"','')) as acadYear from student_event where eventId= ? and JSON_EXTRACT(input_json,'$.program_id')= ? order by last_updated_on desc limit 1;";
        
        return getJdbcTemplate().queryForObject(sql, new Object[] { eventId,programId },
                    String.class);
  }

}
