package com.spts.lms.daos.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.daos.BaseDAO;

@Repository("studentContentDAO")
public class StudentContentDAO extends BaseDAO<StudentContent> {

	@Override
	protected String getTableName() {
		return "student_content";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO student_content(acadMonth,acadYear,username,courseId,contentId,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate) VALUES "
				+ "(:acadMonth,:acadYear,:username,:courseId,:contentId,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
		return sql;
	}

	public int getNoOfStudentsAllocated(Long id) {
		String sql = " select count(*) from student_content where contentId = ?";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public void keepCount(Long id, String username) {
		logger.info("Update calling");
		executeUpdateSql(
				"update student_content set count = count+1 where contentId = ? and username = ?",
				new Object[] { id, username });
	}

	/*public void deleteByContentId(Long contentId) {
		String sql = " delete from " + getTableName() + " where contentId = ?";
		executeUpdateSql(sql, new Object[] { contentId });
	}*/
	
	public void deleteByContentId(Long contentId) {
		String sql = " update " + getTableName() + " set active = 'N' where contentId = ?";
		executeUpdateSql(sql, new Object[] { contentId });
	}
	
	public List<StudentContent> getStudentUsername(Long contentId, Long courseId) {

		String sql = "select sc.username from student_content sc "
				+ "where sc.contentId = ? and sc.courseId = ?";

		return findAllSQL(sql, new Object[] { contentId, courseId });

	}

	public List<StudentContent> findContentByStudent(String username) {

		String sql = "select sc.* from student_content sc where sc.username  = ? ";

		return findAllSQL(sql, new Object[] { username });

	}
	
	public StudentContent findStudentContent(String userName,
			String contentId) {
		
		String sql = "Select * from student_content sc where sc.username = ? and sc.contentId = ? ";
		return findOneSQL(sql, new Object[] { userName, contentId });
	}

	public List<StudentContent> getStudentContentStats(String campusId, String fromDate , String toDate ) {
		
		String typeCondition ="";
		if(campusId !=null){
			typeCondition =" c.campusId = ? ";
		}else{
			typeCondition =" c.campusId is ? " ;
		}
		
		String sql = "select sc.* from student_content sc, course c, users u where sc.courseId = c.id and sc.username = u.username and u.active = 'Y' and "+ typeCondition +" and sc.count != 0 and sc.lastModifiedDate BETWEEN  ? and ? ";

		return findAllSQL(sql, new Object[] { campusId, fromDate ,toDate });

	}
	
/*	public List<StudentContent> getStudentsContentForModule(List<Long> contentId,String moduleId, Integer acadYear) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", contentId);
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		
		String sql = "select uc.username,c.id as courseId, p.programName, u.firstname, u.lastname,u.rollNo as rollNo, sc.contentId, sc.id, u.campusId, u.campusName "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		INNER JOIN course c ON c.id = uc.courseId"
				+ "		left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId IN (:contentId) "
				+ " where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and"
				+ " uc.role = 'ROLE_STUDENT' and c.moduleId = :moduleId and c.acadYear = :acadYear  order by sc.lastModifiedDate desc ";

		 return getNamedParameterJdbcTemplate().query(sql, params,
				   BeanPropertyRowMapper.newInstance(StudentContent.class));
	}*/
	//New Content Query
	public List<StudentContent> getStudentsContentForModule(List<Long> contentId,String moduleId, Integer acadYear,String facultyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", contentId);
		params.put("moduleId", moduleId);
		params.put("acadYear", acadYear);
		params.put("facultyId", facultyId);
		
		
		String sql = "select uc.username,c.id as courseId, p.programName, u.firstname, u.lastname,u.rollNo as rollNo, sc.contentId, sc.id, u.campusId, u.campusName "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		INNER JOIN course c ON c.id = uc.courseId"
				+ "		left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId IN (:contentId) "
				+ " where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and"
				+ " uc.role = 'ROLE_STUDENT' and c.id in (select courseId from user_course uc1, course c1 where uc1.courseId=c1.id and uc1.username = :facultyId "
				+ "and c1.moduleId = :moduleId and uc1.active='Y' and c1.active='Y' and c1.acadYear=:acadYear) and c.acadYear = :acadYear  order by sc.lastModifiedDate desc ";

		 return getNamedParameterJdbcTemplate().query(sql, params,
				   BeanPropertyRowMapper.newInstance(StudentContent.class));
	}
	
/*	public List<StudentContent> getStudentsForContentAndCampusIdbyModuleId(List<Long> contentId,String moduleId, Long campusId,Integer acadYear) {
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", contentId);
		params.put("moduleId", moduleId);
		params.put("campusId", campusId);
		params.put("acadYear", acadYear);
		String sql = "select uc.username, p.programName, u.firstname, u.lastname,u.rollNo as rollNo, sc.contentId, sc.id, u.campusName "
				+ " from user_course uc"
				+ "         inner join users u on uc.username = u.username"
				+ "         inner join program p on u.programId = p.id"
				+ "         inner join course c ON c.id = uc.courseId"
				+ "         left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId IN (:contentId) "
				+ " where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and"
				+ " uc.role = 'ROLE_STUDENT' and c.moduleId = :moduleId and u.campusId = :campusId and c.acadYear = :acadYear order by sc.lastModifiedDate desc ";
		 return getNamedParameterJdbcTemplate().query(sql, params,
		   BeanPropertyRowMapper.newInstance(StudentContent.class));
		}*/
	
	
	public List<StudentContent> getStudentsForContentAndCampusIdbyModuleId(List<Long> contentId,String moduleId, Long campusId,Integer acadYear) {
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", contentId);
		params.put("moduleId", moduleId);
		params.put("campusId", campusId);
		params.put("acadYear", acadYear);
		String sql = "select uc.username, p.programName, u.firstname, u.lastname,u.rollNo as rollNo, sc.contentId, sc.id, u.campusName "
				+ " from user_course uc"
				+ "         inner join users u on uc.username = u.username"
				+ "         inner join program p on u.programId = p.id"
				+ "         inner join course c ON c.id = uc.courseId"
				+ "         left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId IN (:contentId) "
				+ " where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and"
				+ " uc.role = 'ROLE_STUDENT' and c.moduleId = :moduleId and u.campusId = :campusId and c.acadYear = :acadYear order by sc.lastModifiedDate desc ";
		 return getNamedParameterJdbcTemplate().query(sql, params,
		   BeanPropertyRowMapper.newInstance(StudentContent.class));
		}
	
	public StudentContent getNoOfStudentsAllocatedForModule(List<Long> Id) {
		 
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Id", Id);
		 String sql = " select count(*) as count from student_content where contentId IN (:Id)" ;
		 return getNamedParameterJdbcTemplate().queryForObject(sql, params,
		   BeanPropertyRowMapper.newInstance(StudentContent.class));
	}
	
	public void setActiveByContentId(Long contentId){
		String sql = " update " + getTableName() + " set active = 'Y' where contentId = ?";
		executeUpdateSql(sql, new Object[] { contentId });
	}
	
	public StudentContent getStudentViewCount(List<Long> parentModuleId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contentId", parentModuleId);
		String sql = "select count(distinct username) as count from student_content where contentId IN (:contentId) and count <> '0'";
		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				   BeanPropertyRowMapper.newInstance(StudentContent.class));
	}
	
}
