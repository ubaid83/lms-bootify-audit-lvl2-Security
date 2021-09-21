package com.spts.lms.daos.program;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.program.Program;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;

@Repository("programDAO")
public class ProgramDAO extends BaseDAO<Program> {

	@Override
	protected String getTableName() {

		return "program";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into program (abbr, programName, sessionType, durationInMonths, maxDurationInMonths, revisedFromMonth, revisedFromYear, "
				+ " createdDate, lastModifiedDate, createdBy, lastModifiedBy) values"
				+ "(:abbr, :programName, :sessionType, :durationInMonths, :maxDurationInMonths, :revisedFromMonth, :revisedFromYear, "
				+ " :createdDate, :lastModifiedDate, :createdBy, :lastModifiedBy)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {

		String sql = "update program  set abbr=:abbr, programName=:programName, sessionType=:sessionType, durationInMonths=:durationInMonths, maxDurationInMonths=:maxDurationInMonths, revisedFromMonth=:revisedFromMonth,"
				+ " revisedFromYear=:revisedFromYear, lastModifiedDate=:lastModifiedDate, lastModifiedBy=:lastModifiedBy where id=:id";
		return sql;
	}

	@Override
	@Cacheable("programs")
	public Program findOne(Long id) {
		return super.findOne(id);
	}

	@Override
	@CacheEvict(value = "programs", key = "#bean.id")
	public int update(Program bean) {
		return super.update(bean);
	}

	public List<Program> getAllPrograms(String programId) {

		String sql = "SELECT p.id, p.abbr, p.programName, p.sessionType, p.durationInMonths, p.maxDurationInMonths, "
				+ " p.revisedFromMonth, p.revisedFromYear, "
				+ " p.createdDate, p.lastModifiedDate, p.createdBy, p.lastModifiedBy, p.programSessions "

				+ " FROM program p   "

				+ " WHERE p.active = 'Y' ";

		return findAllSQL(sql, new Object[] {});
	}

	public List<Program> findAllProgramsWithAcadSession(List<String> acadSession) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);

		final String sql = " select p.* from program p,course c where p.id =c.programId and c.acadSession in(:acadSessions) group by p.id ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));
	}

	public List<String> findProgramIdByProgramName(String programName) {
		String sql = "select distinct id from " + getTableName()
				+ " where programName = ?";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { programName });
	}

	public List<Program> getProgramByName(String progName) {

		String sql = "select * from program where  programName = ? ";
		return findAllSQL(sql, new Object[] { StringUtils.trim(progName) });
	}

	/*
	 * public List<Program> getAllProgramsWithSession(List<Long> programIds, int
	 * pageNo, int pageSize) { final ProgramRowMapper programRowMapper = new
	 * ProgramRowMapper(); final ProgramSessionRowMapper programSessionRowMapper
	 * = new ProgramSessionRowMapper();
	 * 
	 * return getJdbcTemplate().query(
	 * "SELECT * FROM program prg LEFT OUTER JOIN program_session session " +
	 * " on prg.id = session.programId ", new
	 * ResultSetExtractor<List<Program>>() {
	 * 
	 * public List<Program> extractData(ResultSet rs) throws SQLException {
	 * Map<Long,Program> programIdProgramMap = new HashMap<Long,Program>();
	 * while(rs.next()) {
	 * 
	 * Long programId = rs.getLong("programId"); Program program =
	 * programIdProgramMap.get(programId); if (program == null) { program =
	 * programRowMapper.mapRow(rs,0);
	 * programIdProgramMap.put(programId,program); } ProgramSession session =
	 * programSessionRowMapper.mapRow(rs, 0);
	 * program.addProgramSession(session); } return new
	 * ArrayList<Program>(programIdProgramMap.values()); }
	 * 
	 * 
	 * }); }
	 */

	public Page<Program> getAllProgramsWithSession(int pageNo, int pageSize) {
		final String sql = "Select p.*, group_concat(ps.sessionNumber separator ',') as programSession from "
				+ getTableName()
				+ " p left outer join "
				+ " program_session ps on ps.programId = p.id where p.active='Y' group by p.id";
		return paginationHelper.fetchPage(getJdbcTemplate(),
				"SELECT COUNT(*) FROM " + getTableName() + " where active='Y'",
				sql, new Object[] {}, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));
	}

	/*
	 * public class ProgramRowMapper implements RowMapper<Program>{ public
	 * Program mapRow(ResultSet rs, int rowNum) throws SQLException { Program
	 * program = new Program(); program.setId(rs.getLong("ID"));
	 * program.setName(rs.getString("ProgramName"));
	 * program.setAbbr(rs.getString("Abbr"));
	 * program.setSessionType(rs.getString("sessionType"));
	 * program.setDurationInMonths(rs.getShort("durationInMonths"));
	 * 
	 * return program; } }
	 * 
	 * public class ProgramSessionRowMapper implements
	 * RowMapper<ProgramSession>{ public ProgramSession mapRow(ResultSet rs, int
	 * rowNum) throws SQLException {
	 * 
	 * ProgramSession programSessin = new ProgramSession();
	 * programSessin.setId(rs.getLong("Id"));
	 * programSessin.setSessionNumber(rs.getShort("sessionNumber"));
	 * 
	 * return programSessin; } }
	 * 
	 * public class ProgramMapper implements RowMapper<Program> { private
	 * Program program;
	 * 
	 * public Program mapRow(ResultSet rs, int rowNum) throws SQLException { if
	 * (program == null) { this.program = new Program ();
	 * program.setId(rs.getLong("Id"));
	 * program.setName(rs.getString("ProgramName"));
	 * program.setAbbr(rs.getString("Abbr"));
	 * program.setSessionType(rs.getString("sessionType"));
	 * program.setDurationInMonths(rs.getShort("durationInMonths"));
	 * 
	 * } ProgramSession programSession = new ProgramSession ();
	 * programSession.setId(0l); this.program.addProgramSession(programSession);
	 * return null; }
	 * 
	 * public final Program getProgram() { return program; }
	 * 
	 * }
	 */

	public int deleteSessionsNotIn(Program program) {
		final String sql = "DELETE FROM program_session where programId = :id and sessionNumber not in (:programSessionArray)";
		return deleteSQL(program, sql);
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Program> findAppsByUsername(String username) {
		final String sql = "select distinct p.programName,p.id as id "
				+ "from user_course uc , course c,  program p, user_roles ur "
				+ " where uc.courseId = c.id and c.programId = p.id and c.active= 'Y' and uc.username = ur.username and ur.username = ?";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<Program> findAllProgramsWithAcadSessionForFaculty(
			List<String> acadSession, String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);
		params.put("facultyId", facultyId);

		final String sql = "select distinct p.* from course c ,student_feedback sf , feedback f, program p "
				+ " where c.id = sf.courseId and  c.acadSession is not null "
				+ " and sf.facultyId=:facultyId and f.id=sf.feedbackId and f.isPublished='Y' "
				+ " and c.programId=p.id and c.id=sf.courseId and c.acadSession in(:acadSessions)  order by acadSession  asc";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));
	}

	public List<String> findAllProgramsForFaculty(String facultyId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("facultyId", facultyId);

		final String sql = "select distinct p.id from course c ,student_feedback sf , feedback f, program p "
				+ " where c.id = sf.courseId and  c.acadSession is not null "
				+ " and sf.facultyId=:facultyId and f.id=sf.feedbackId and f.isPublished='Y' "
				+ " and c.programId=p.id and c.id=sf.courseId order by acadSession  asc";
		/*
		 * return getNamedParameterJdbcTemplate().query(sql, params,
		 * BeanPropertyRowMapper.newInstance(Stirng.class));
		 */

		return getNamedParameterJdbcTemplate().queryForList(sql, params,
				String.class);
	}

	public List<Program> findAllProgramsByFacultyId(String acadYear,
			String facultyId, List<String> acadSessions) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadYear", acadYear);
		params.put("facultyId", facultyId);
		params.put("acadSessions", acadSessions);

		final String sql = "select distinct p.* from course c ,student_feedback sf , feedback f, program p "
				+ " where c.id = sf.courseId and  c.acadSession is not null "
				+ " and sf.facultyId=:facultyId and sf.acadYear = :acadYear and f.id=sf.feedbackId  and f.active = 'Y' "
				+ " and c.programId=p.id and c.id=sf.courseId and c.acadSession in(:acadSessions)"
				+ " order by acadSession  asc";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));

	}

	public List<Program> getProgramListByIds(String programIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (programIds.contains(",")) {
			List<String> programList = Arrays.asList(programIds.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programIds);
		}

		String sql = " select * from  " + getTableName()
				+ " where id in(:programIds) ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));

	}

	public String getProgramNamesForIca(String programIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (programIds.contains(",")) {
			List<String> programList = Arrays.asList(programIds.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programIds);
		}

		String sql = " select group_concat(programName) from  "
				+ getTableName() + " where id in(:programIds) ";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				String.class);
	}

	public String getCollegeName(String programId, String campusId) {

		String sql = " select campusName from program_campus "
				+ " where programId = ? and campusId = ? ";

		try {
			return getJdbcTemplate().queryForObject(sql,
					new Object[] { programId, campusId }, String.class);
		} catch (Exception ex) {
			return null;
		}
	}

	public List<Program> findAllProgramsWithAcadSessionYearModule(
			String acadSession, String acadYear, String moduleId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("moduleId", moduleId);

		final String sql = " select p.* from program p,course c where p.id =c.programId and c.acadSession =:acadSessions  "
				+ " and c.acadYearCode=:acadYear and c.moduleId=:moduleId group by p.id,c.moduleId ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));
	}

	public List<Program> findAllProgramsWithAcadSessionYearModuleForModule(
			String acadSession, String acadYear, String moduleId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acadSessions", acadSession);
		params.put("acadYear", acadYear);
		params.put("moduleId", moduleId);

		final String sql = " select p.* from program p, module m, session_master sm where p.id = m.program_id and m.session_code = sm.sapSessionCode and sm.sapSessionText =:acadSessions "
				 + " and m.acadYear=:acadYear and m.module_id =:moduleId group by p.id, m.module_id  ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));
	}
	public List<Program> findPrograms(){
        String sql = "select distinct id,programName from program";
        return findAllSQL(sql, new Object[] {  });
  }
	//Coursera Chnage
	
	public List<Program> findAllProgramsWithAcadSessionYearModuleCE(
			String acadYear, String moduleId) {

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("acadYear", acadYear);
		params.put("moduleId", moduleId);

		final String sql = " select p.* from program p,course c where p.id =c.programId   "
				+ " and c.acadYearCode=:acadYear and c.moduleId=:moduleId group by p.id,c.moduleId ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Program.class));
	}	
	
	
	
	
	
	
	

	public List<Program> findAllProgramForFeedbackSupportAdmin() {
	String sql=" SELECT DISTINCT p.programName, p.id FROM course c,program p WHERE c.programId=p.id ";
	return findAllSQL(sql, new Object[] {});
}	
	
	public String getProgramName(String programId) {

		String sql = " select programName from "+ getTableName()
				+ " where id = ? ";

		try {
			return getJdbcTemplate().queryForObject(sql,
					new Object[] { programId }, String.class);
		} catch (Exception ex) {
			return null;
		}
	}
	
	//amey 14-10-2020
	public List<String> findAllProgramsForFacultyByType(String facultyId, String feedbackType) {

		if(feedbackType == null || "".equals(feedbackType)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("facultyId", facultyId);
	
			final String sql = "select distinct p.id from course c ,student_feedback sf , feedback f, program p "
					+ " where c.id = sf.courseId and  c.acadSession is not null "
					+ " and sf.facultyId=:facultyId and f.id=sf.feedbackId and f.isPublished='Y' and f.feedbackType is null "
					+ " and c.programId=p.id and c.id=sf.courseId order by acadSession  asc";
			
			return getNamedParameterJdbcTemplate().queryForList(sql, params,
					String.class);
		}else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("facultyId", facultyId);
			params.put("feedbackType", feedbackType);
	
			final String sql = "select distinct p.id from course c ,student_feedback sf , feedback f, program p "
					+ " where c.id = sf.courseId and  c.acadSession is not null "
					+ " and sf.facultyId=:facultyId and f.id=sf.feedbackId and f.isPublished='Y' and f.feedbackType = :feedbackType "
					+ " and c.programId=p.id and c.id=sf.courseId order by acadSession  asc";
			
			return getNamedParameterJdbcTemplate().queryForList(sql, params,
					String.class);
		}
	}
	

	public List<Program> getPrograms() {
		
		String sql =" select * from "+getTableName()+" where active ='Y' order by programName ";
		return findAllSQL(sql, new Object[] {});
	}
}
