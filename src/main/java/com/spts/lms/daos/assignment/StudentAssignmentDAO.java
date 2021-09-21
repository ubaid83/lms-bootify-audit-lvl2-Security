package com.spts.lms.daos.assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.web.utils.Utils;

@Repository("studentAssignmentDAO")
public class StudentAssignmentDAO extends BaseDAO<StudentAssignment> {

	@Override
	protected String getTableName() {
		return "student_assignment";
	}

	@Override
	protected String getInsertSql() {

		final String sql = "INSERT INTO student_assignment(acadMonth,acadYear,username,courseId,assignmentId,evaluatedBy,groupId,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,startDate,endDate,threshold,url,isSubmitterInGroup)"

				+ "VALUES (:acadMonth,:acadYear,:username,:courseId,:assignmentId,:evaluatedBy,:groupId,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:startDate,:endDate,:threshold,:url,:isSubmitterInGroup)";

		/*
		 * final String sql =
		 * "INSERT INTO student_assignment(acadMonth,acadYear,username,courseId,filePath,"
		 * +
		 * "filePreviewPath,submissionDate,attempts,createdBy,createdDate,lastModifiedBy,lastModifiedDate,"
		 * + "submissionStatus,assignmentId) VALUES " +
		 * "(:acadMonth,:acadYear,:username,:courseId,:filePath,:filePreviewPath,:submissionDate,:attempts,"
		 * +
		 * ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:submissionStatus,:assignmentId)"
		 * ;
		 */

		return sql;
	}

	public void insertData(List<StudentAssignment> sas) {

		String sql = "INSERT INTO student_assignment(acadMonth,acadYear,username,courseId,assignmentId,evaluatedBy,groupId,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate,startDate,endDate,active,score,submissionStatus,evaluationStatus,filePreviewPath,studentFilePath)"

				+ "VALUES (:acadMonth,:acadYear,:username,:courseId,:assignmentId,:evaluatedBy,:groupId,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:startDate,:endDate,:active,:score,:submissionStatus,:evaluationStatus,:filePreviewPath,:studentFilePath)";
		SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(sas
				.toArray());
		int[] updateCounts = getNamedParameterJdbcTemplate().batchUpdate(sql,
				batch);

	}

	public int chkStartandEndDateOfAssignment(String username, Long id) {
		final String sql = " select count(*) from assignment a,student_assignment sa where a.startDate <= ?  and sa.assignmentId=a.id and sa.username= ? "
				+ " and a.id= ?";
		Date dt = Utils.getInIST();
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { dt, username, id });
	}
	
	public int chkStartandEndDtOfAssignment(String endDate, String username, Long id) {
		final String sql = " select count(*) from assignment a,student_assignment sa where ? >= ?  and sa.assignmentId=a.id and sa.username= ? "
				+ " and a.id= ? ";
		Date dt = Utils.getInIST();
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { endDate, dt, username, id });
	}

	public int chkStartandEndDtOfAssignment(String username, Long id) {
		final String sql = " select count(*) from assignment a,student_assignment sa where   a.endDate >= ?  and sa.assignmentId=a.id and sa.username= ? "
				+ " and a.id= ? ";
		Date dt = Utils.getInIST();
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { dt, username, id });
	}

	public List<String> findStudentUsernames(Long assignmentId) {
		String sql = "select username from student_assignment where assignmentId = ?";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { assignmentId });
	}

	public List<StudentAssignment> getStudentsForAssignmentWithScores(
			Long assignmentId, Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id,sa.score "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		left outer join student_assignment sa on sa.username = u.username and sa.courseId = uc.courseId and sa.assignmentId = ? "
				+ " where"
				+ " uc.role = ? "
				+ "and uc.active='Y'"
				+ "and u.active='Y' and u.enabled = 1 " + "and p.active='Y'"

				+ "and uc.courseId = ?    order by sa.lastModifiedDate desc ";
		return findAllSQL(
				sql,
				new Object[] { assignmentId, Role.ROLE_STUDENT.name(), courseId });

	}

	public List<StudentAssignment> getAssignmentsByStudent(Long courseId,
			String username) {
		String sql = "select sa.score, a.maxScore, a.assignmentName,a.id from assignment a "
				+ " inner join student_assignment sa on sa.assignmentId = a.id "
				+ " where sa.courseId = ? and sa.username = ? and a.active = 'Y' order by a.id";
		return findAllSQL(sql, new Object[] { courseId, username });

	}

	public void setStudentInActive(Long groupId, String username) {
		executeUpdateSql(
				"update student_assignment set active='N' where groupId= ? and username=? ",
				new Object[] { groupId, username });
	}

	public void deleteStudent(Long groupId, String username) {
		executeUpdateSql(
				"delete from student_assignment where groupId= ? and username=? ",
				new Object[] { groupId, username });
	}

	public List<StudentAssignment> getLateSubmittedStudents(
			StudentAssignment assignment, Long assignmentId) {
		String sql = "select  a.* ,sa.*  from student_assignment sa"
				+ "         inner join course c on c.id=sa.courseId"
				+ "         inner join assignment a on a.id=sa.assignmentId"
				+ " where"
				+ " a.id = ? "
				+ "and a.active='Y'"
				+ " and sa.submissionDate > sa.endDate and sa.submissionStatus='N' and c.active='Y' order by sa.id asc  ";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(assignmentId);

		/*
		 * if (assignment.getCourseId() != null) { sql = sql +
		 * " and sa.courseId = ? "; countSql = countSql +
		 * " and sa.courseId = ? "; parameters.add(assignment.getCourseId()); }
		 */

		Object[] args = parameters.toArray();

		return findAllSQL(sql, args);
	}

	public List<StudentAssignment> getNonSubmittedStudents(
			StudentAssignment assignment, Long assignmentId) {
		String sql = "select  a.* ,sa.*  from student_assignment sa"
				+ "         inner join course c on c.id=sa.courseId"
				+ "         inner join assignment a on a.id=sa.assignmentId"
				+ " where"
				+ " a.id = ? "
				+ "and a.active='Y'"

				+ " and studentFilePath is NULL"
				+ " and a.endDate < sysdate() and sa.submissionStatus='N' and c.active='Y'  order by sa.id asc";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(assignmentId);

		/*
		 * if (assignment.getCourseId() != null) { sql = sql +
		 * " and sa.courseId = ? "; countSql = countSql +
		 * " and sa.courseId = ? "; parameters.add(assignment.getCourseId()); }
		 */

		Object[] args = parameters.toArray();
		return findAllSQL(sql, args);
	}

	public List<StudentAssignment> searchAllAssignment(Long courseId,
			String status) {
		String sql = "select sa.*, a.*,sa.id as id from assignment a"
				+ " inner join student_assignment sa on sa.assignmentId = a.id "
				+ " where sa.courseId = ? and sa.submissionStatus = ? and a.active = 'Y'";
		return findAllSQL(sql, new Object[] { courseId, status });
	}

	public int getNoOfStudentsAllocated(Long id) {
		String sql = " select count(*) from student_assignment sa ,users u where sa.assignmentId=? and u.username=sa.username and u.enabled=1 and sa.active='Y'";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	public List<StudentAssignment> findAssignmentEvaluationForNonSunmittedStudents(
			String facultyId) {
		String sql = " Select a.*, sa.*, c.courseName from assignment a "
				+ " inner join student_assignment sa on a.id = sa.assignmentId "
				+ " inner join course c on a.courseId = c.id "
				+ " where  c.active ='Y' "
				+ " and a.active='Y' and sa.submissionStatus='N'"
				+ "and  sa.createdBy = ? ";
		return findAllSQL(sql, new Object[] { facultyId });
	}

	public int getNoOfGroupsAllocated(Long id) {
		String sql = " select count(distinct groupId) from student_assignment where assignmentId = ?";

		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { id });
	}

	public List<StudentAssignment> findStudentsByGroupId(Long groupId) {
		String sql = "select * from student_assignment where groupId = ?";
		return findAllSQL(sql, new Object[] { groupId });
	}

	public void setInActive(Long groupId) {
		executeUpdateSql(
				"update student_assignment set active='N' where groupId= ? ",
				new Object[] { groupId });
	}

	public List<StudentAssignment> getGroupsBasedOnCourseAndAssignment(
			StudentAssignment sa, Long courseId, Long assignmentId) {
		String sql = "select a.* , sa.* , g.* from student_assignment sa,"
				+ " assignment a, groups g where a.courseId = sa.courseId and "
				+ " sa.assignmentId = a.id  and "
				+ "g.id = sa.groupId and a.courseId = ? and a.id = ? ";

		String countSql = "Select count(*) from assignment a"
				+ " assignment a, groups g where a.courseId = sa.courseId and "
				+ " sa.assignmentId = a.id "
				+ "g.id = sa.groupId and a.courseId = ? and a.id = ? ";
		StringBuffer sqlBuffer = new StringBuffer(sql);
		StringBuffer countBuffer = new StringBuffer(countSql);

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(courseId);
		parameters.add(assignmentId);

		if (!StringUtils.isEmpty(sa.getSubmissionStatus())) {

			String sStatus = sa.getSubmissionStatus();
			switch (sStatus) {
			case "Y":
				sqlBuffer.append(" and sa.submissionStatus = 'Y' ");
				countBuffer.append(" and sa.submissionStatus = 'Y' ");
				break;
			case "N":
				sqlBuffer.append(" and sa.submissionStatus = 'N' ");
				countBuffer.append(" and sa.submissionStatus = 'N' ");
				break;

			}

		} else {
		}
		sqlBuffer.append(" GROUP BY g.groupName ");
		countBuffer.append(" GROUP BY g.groupName ");
		Object[] args = parameters.toArray();

		return findAllSQL(sqlBuffer.toString(), args);

	}

	public String findByGroupAndAssignmentId(Long groupId, Long assignmentId) {
		String sql = "select count(DISTINCT sa.submissionStatus) from student_assignment sa where sa.groupId = ? and sa.assignmentId = ?";
		return returnSingleColumn(sql, new Object[] { groupId, assignmentId });
	}

	public void saveApprovalStatus(String approvalStatus, Long pk,
			String userName) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setApprovalStatus(approvalStatus);
		assignment.setId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set "
				+ " approvalStatus = :approvalStatus, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y',"
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(assignment, sql);

	}

	@Override
	protected String getUpdateSql() {
		final String sql = "update student_assignment "
				+ " set studentFilePath = :studentFilePath,"
				+ " filePreviewPath = :filePreviewPath ,"
				+ " submissionDate = :submissionDate ,"
				+ " attempts = attempts + 1,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate,"
				+ " submissionStatus = :submissionStatus, "
				+ "startDate = :startDate ,"
				+ "isSubmitterInGroup=:isSubmitterInGroup,"
				+ "threshold = :threshold ,"
				+ "url = :url ,"
				+ "endDate = :endDate, "
				+ " active =:active"
				+ " where  assignmentId = :assignmentId and courseId = :courseId and username = :username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	/*
	 * public Page<StudentAssignment> getStudentsBasedOnAssignment(
	 * StudentAssignment assignment, int pageNo, int pageSize, Long
	 * assignmentId) { String sql =
	 * "select  a.* ,sa.* from student_assignment sa"
	 * 
	 * + "         inner join course c on c.id=sa.courseId" +
	 * "         inner join assignment a on a.id=sa.assignmentId" + " where" +
	 * " a.id = ? order by sa.id asc  ";
	 * 
	 * String countSql = "Select count(*) from student_assignment sa" +
	 * " inner join course c on c.id=sa.courseId" +
	 * " inner join assignment a on a.id=sa.assignmentId " + " where" +
	 * " a.id = ? order by sa.id asc  ";
	 * 
	 * ArrayList<Object> parameters = new ArrayList<Object>();
	 * parameters.add(assignmentId);
	 * 
	 * 
	 * if (assignment.getCourseId() != null) { sql = sql +
	 * " and sa.courseId = ? "; countSql = countSql + " and sa.courseId = ? ";
	 * parameters.add(assignment.getCourseId()); }
	 * 
	 * 
	 * Object[] args = parameters.toArray();
	 * 
	 * return findAllSQL(sql, countSql, args, pageNo, pageSize); }
	 */
	public List<StudentAssignment> getStudentsBasedOnAssignment(
			StudentAssignment assignment, Long assignmentId) {
		/*
		 * String sql = "select  a.* ,sa.*  from student_assignment sa" +
		 * "         inner join course c on c.id=sa.courseId" +
		 * "         inner join assignment a on a.id=sa.assignmentId" + " where"
		 * + " a.id = ?" + "and a.active='Y'" + ""
		 * 
		 * + " and c.active='Y' order by sa.id asc  ";
		 */

		String sql = "select  a.* ,sa.*  from student_assignment sa"
				+ "  inner join course c on c.id=sa.courseId"
				+ "  inner join assignment a on a.id=sa.assignmentId"
				+ " where"
				+ " a.id = ? and c.active='Y' and a.active='Y' and sa.submissionStatus ='Y' order by sa.id asc  ";

		//ArrayList<Object> parameters = new ArrayList<Object>();
		//parameters.add(assignmentId);

		/*
		 * if (assignment.getCourseId() != null) { sql = sql +
		 * " and sa.courseId = ? "; countSql = countSql +
		 * " and sa.courseId = ? "; parameters.add(assignment.getCourseId()); }
		 */

		//Object[] args = parameters.toArray();

		//return findAllSQL(sql, args);
		return findAllSQL(sql, new Object[] {assignmentId});
	}

	public Page<StudentAssignment> getStudentsBasedOnGroups(
			StudentAssignment assignment, int pageNo, int pageSize,
			Long assignmentId) {
		String sql = "select sa.* , a.* from student_assignment sa"

		+ "         inner join course c on c.id=sa.courseId"
				+ "         inner join assignment a on a.id=sa.assignmentId"
				+ " where" + " a.active='Y'" + "and "
				+ " a.id = ? and c.active='Y' order by sa.id asc  ";

		String countSql = "Select count(*) from student_assignment sa"
				+ " inner join course c on c.id=sa.courseId"
				+ " inner join assignment a on a.id=sa.assignmentId "
				+ " where" + " a.active='Y'" + "and "
				+ " a.id = ? and c.active='Y'  order by sa.id asc  ";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(assignmentId);

		/*
		 * if (assignment.getCourseId() != null) { sql = sql +
		 * " and sa.courseId = ? "; countSql = countSql +
		 * " and sa.courseId = ? "; parameters.add(assignment.getCourseId()); }
		 */

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public List<StudentAssignment> findAssignmentForEvaluation(
			StudentAssignment assignment, String facultyId) {
		String sql = " Select a.*, sa.*, c.courseName from assignment a "
				+ " inner join student_assignment sa on a.id = sa.assignmentId "
				+ " inner join course c on a.courseId = c.id "
				+ " where  c.active ='Y' " + " and a.active='Y'"
				+ "and  sa.createdBy = ? and a.moduleId is null";

		String countSql = "Select count(*) from assignment a"
				+ " inner join student_assignment sa on a.id = sa.assignmentId "
				+ " inner join course c on a.courseId = c.id "
				+ " where c.active='Y' " + "and a.active='Y'"
				+ "and  sa.createdBy = ? and a.moduleId is null";
		StringBuffer sqlBuffer = new StringBuffer(sql);
		StringBuffer countBuffer = new StringBuffer(countSql);

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(facultyId);

		if (assignment.getUsername() != null
				&& !assignment.getUsername().isEmpty()) {
			sqlBuffer.append(" and sa.username = ? ");
			countBuffer.append(" and sa.username = ? ");
			parameters.add(assignment.getUsername());
		}

		if (assignment.getEvaluationStatus() != null
				&& !assignment.getEvaluationStatus().isEmpty()) {
			sqlBuffer.append(" and sa.evaluationStatus = ? ");
			countBuffer.append(" and sa.evaluationStatus = ? ");
			parameters.add(assignment.getEvaluationStatus());
		}

		if (assignment.getLowScoreReason() != null
				&& !assignment.getLowScoreReason().isEmpty()) {
			sqlBuffer.append(" and sa.lowScoreReason = ? ");
			countBuffer.append(" and sa.lowScoreReason = ? ");
			parameters.add(assignment.getLowScoreReason());
		}

		if (assignment.getCourseId() != null) {
			sqlBuffer.append(" and sa.courseId = ? ");
			countBuffer.append(" and sa.courseId = ? ");
			parameters.add(assignment.getCourseId());
		}

		if (!StringUtils.isEmpty(assignment.getSubmissionStatus())) {

			String sStatus = assignment.getSubmissionStatus();
			switch (sStatus) {
			case "Y":
				sqlBuffer.append(" and sa.submissionStatus = 'Y' ");
				countBuffer.append(" and sa.submissionStatus = 'Y' ");
				break;
			case "N":
				sqlBuffer.append(" and sa.submissionStatus = 'N' ");
				countBuffer.append(" and sa.submissionStatus = 'N' ");
				break;

			}

		} else {
		}

		Object[] args = parameters.toArray();

		return findAllSQL(sqlBuffer.toString(), args);

	}

	public void saveAssignmentRemarks(String remarks, Long pk, String userName) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setRemarks(remarks);
		assignment.setId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set " + " remarks = :remarks, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(assignment, sql);

	}

	public void saveAssignmentScore(String score, Long pk, String userName) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setScore(score);
		assignment.setId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set " + " score = :score, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(assignment, sql);
	}

	public void saveAssigmentFromStudent(StudentAssignment inputBean) {

		String sql = "update student_assignment "
				+ " set studentFilePath = :studentFilePath,"
				+ " submissionDate = :submissionDate ,"
				+ " attempts = attempts + 1,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate,"
				+ " submissionStatus = :submissionStatus  "
				+ " where  assignmentId = :assignmentId and courseId = :courseId and username = :username ";

		updateSQL(inputBean, sql);
	}

	public List<StudentAssignment> getStudentsForAssignment(Long assignmentId,
			Long courseId, String acadMonth, Integer acadYear) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id, u.campusId, u.campusName "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		left outer join student_assignment sa on sa.username = u.username and sa.courseId = uc.courseId and sa.assignmentId = ? "
				+ " where"
				+ " uc.role = ? and uc.courseId = ? "
				+ "and uc.active='Y'"
				+ "and u.active='Y' and u.enabled = 1 "
				+ "and p.active='Y'"
				+ "and uc.acadMonth = ? and uc.acadYear = ? order by sa.id asc ";
		return findAllSQL(sql,
				new Object[] { assignmentId, Role.ROLE_STUDENT.name(),
						courseId, acadMonth, acadYear });

	}

	public List<StudentAssignment> getStudentsForAssignment(Long assignmentId,
			Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id ,u.campusId, u.campusName"
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		left outer join student_assignment sa on sa.username = u.username and sa.courseId = uc.courseId and sa.assignmentId = ? "
				+ " where"
				+ " uc.role = ? "
				+ "and uc.active='Y'"
				+ "and u.active='Y' and u.enabled = 1 " + "and p.active='Y'"

				+ "and uc.courseId = ?    order by sa.lastModifiedDate desc ";
		return findAllSQL(
				sql,
				new Object[] { assignmentId, Role.ROLE_STUDENT.name(), courseId });

	}

	public List<StudentAssignment> getStudentUsername(Long assignmentId,
			Long courseId) {

		String sql = "select sa.username from student_assignment sa "
				+ "where sa.assignmentId = ? and sa.courseId = ? ";

		return findAllSQL(sql, new Object[] { assignmentId, courseId });

	}

	public List<StudentAssignment> getStudentFiles(Long assignmentId) {
		String sql = "select sa.studentFilePath, sa.username from student_assignment sa,assignment a where sa.assignmentId=a.id and sa.assignmentId=? and sa.studentFilePath is NOT NULL and sa.submissionStatus = 'Y'";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public List<StudentAssignment> getStudentFilesForReport(Long assignmentId) {
		String sql = "select sa.studentFilePath, sa.username from student_assignment sa,assignment a where sa.assignmentId=a.id and sa.assignmentId=? and sa.studentFilePath is NOT NULL and sa.submissionStatus = 'Y'";
		return findAllSQL(sql, new Object[] { assignmentId });
	}
	public StudentAssignment findAssignmentSubmission(String userName,
			Long assignmentId) {
		String sql = "Select * from student_assignment sa where sa.username = ? and sa.assignmentid = ?  ";
		return findOneSQL(sql, new Object[] { userName, assignmentId });
	}

	public List<StudentAssignment> findAssignmentSubmissionLst(String userName,
			Long assignmentId) {
		String sql = "Select * from student_assignment sa where sa.username = ? and sa.assignmentid = ?  ";
		return findAllSQL(sql, new Object[] { userName, assignmentId });
	}

	public void saveLowScoreReason(String lowScoreReason, Long pk,
			String userName) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setLowScoreReason(lowScoreReason);
		assignment.setId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set "
				+ " lowScoreReason = :lowScoreReason, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(assignment, sql);

	}

	public List<StudentAssignment> getGroupsForAssignment(Long assignmentId,
			Long courseId, String acadMonth, String acadYear) {
		String sql = "select distinct g.id as groupId, g.groupName, sa.assignmentId,CASE when sa.id IS NULL    THEN 'N'  ELSE 'Y'  END as 'allocated' "
				+ " from groups g"
				+ "		inner join assignment a on a.courseId = g.courseId"
				+ "		left outer join student_assignment sa on sa.groupId = g.id and sa.courseId = g.courseId and sa.assignmentId = ? "
				+ " where"
				+ " g.courseId = ? and g.acadMonth = ? "
				+ "and g.active='Y'"
				+ "and a.active='Y'"
				+ "and g.acadYear = ? order by sa.id asc ";
		return findAllSQL(sql, new Object[] { assignmentId, courseId,
				acadMonth, acadYear });

	}

	public List<StudentAssignment> getStudentsBasedOnCourse(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		left outer join student_assignment sa on sa.username = u.username and sa.courseId = uc.courseId  "
				+ " where"
				+ " uc.role = ? and uc.courseId = ? "
				+ "and uc.active='Y'"
				+ "and u.active='Y' and u.enabled = 1 "
				+ "and p.active='Y'" + " order by sa.id asc ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId });

	}

	/*
	 * public List<StudentAssignment> getGroupsForAssignment(Long assignmentId,
	 * Long courseId) { String sql =
	 * "select distinct g.id as groupId, g.groupName, sa.assignmentId,CASE when sa.id IS NULL    THEN 'N'  ELSE 'Y'  END as 'allocated' "
	 * + " from groups g" +
	 * "		inner join assignment a on a.courseId = g.courseId" +
	 * "		left outer join student_assignment sa on sa.groupId = g.id and sa.courseId = g.courseId and sa.assignmentId = ? "
	 * + " where" + " a.active='Y'"
	 * 
	 * +
	 * " and g.courseId = ? and g.active='Y' order by sa.lastModifiedDate desc "
	 * ; return findAllSQL(sql, new Object[] { assignmentId, courseId });
	 * 
	 * }
	 */

	public List<StudentAssignment> getGroupsForAssignment(Long assignmentId,
			Long courseId) {
		String sql = " select distinct g.id as groupId, g.groupName, sa.assignmentId, " + 
				" CASE when sa.id IS NULL THEN 'N' ELSE 'Y' END as 'allocated'  " + 
				" from groups g " + 
				" inner join assignment a on a.courseId = g.courseId " + 
				" left outer join student_assignment sa on sa.groupId = g.id and  " + 
				" sa.courseId = g.courseId " + 
				" where a.active='Y' and sa.assignmentId  = ? " + 
				" and g.courseId = ? and g.active='Y' order by sa.lastModifiedDate desc ";
		return findAllSQL(sql, new Object[] { assignmentId, courseId });

	}

	public ArrayList<StudentAssignment> getGroupListAssociatedToFaculty(
			String userName) {
		String sql = " select * from student_assignment sa"
				+ " where sa.username = ? ";
		return (ArrayList<StudentAssignment>) findAllSQL(sql,
				new Object[] { userName });
	}

	public void deleteStudentAssigment(Integer assigmentId) {
		String sql = " update student_assignment set active = 'N' where assignmentId = ?";
		getJdbcTemplate().update(sql, new Object[] { assigmentId });

	}

	public List<StudentAssignment> getAssignmentBasedOnGroups(
			StudentAssignment studentassignment, Long groupId) {
		String sql = " select lsa.* , lg.* , la.* from student_assignment lsa , groups lg , assignment la where "
				+ "  lg.id=lsa.groupId and la.id=lsa.assignmentId and lg.id=? "
				+ "and lg.active='Y'"
				+ "and la.active='Y'"
				+ "  GROUP BY lsa.groupId ";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(groupId);

		Object[] args = parameters.toArray();

		return findAllSQL(sql, args);
	}

	public void saveScore(String saveScore, Long pk, String userName) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setScore(saveScore);
		assignment.setId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set "
				+ " saveScore = :saveScore, " + " evaluatedBy = :evaluatedBy, "
				+ " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id = :id";

		updateSQL(assignment, sql);

	}

	public List<StudentAssignment> findAllAssigment(String acadMonth,
			String acadYear, String username) {
		String sql = "select a.*,sa.* from assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " where sa.acadMonth = ? and sa.acadYear = ?  and sa.username = ? and a.active = 'Y' ";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, username });

	}

	public List<StudentAssignment> findOneAssignment(Long assignmentId) {
		String sql = "select a.*,sa.* from assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " where sa.assignmentId = ?";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public List<StudentAssignment> getGroupsBasedOnCourseAndAssignment(
			Long courseId, Long assignmentId) {
		String sql = "select a.* , sa.* , g.* from student_assignment sa,"
				+ " assignment a, groups g where a.courseId = sa.courseId and "
				+ " sa.assignmentId = a.id and sa.submissionStatus = 'Y' and "
				+ "g.id = sa.groupId and a.courseId = ? and a.id = ? GROUP BY g.groupName";
		return findAllSQL(sql, new Object[] { courseId, assignmentId });

	}

	public void saveGroupAssignmentRemarks(String remarks, String userName,
			Long pk) {
		StudentAssignment assignment = new StudentAssignment();
		logger.info("Remarks" + remarks);
		logger.info("groups Id" + pk);
		assignment.setRemarks(remarks);
		// assignment.setId(pk);
		assignment.setGroupId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set " + " remarks = :remarks, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate "
				+ " where groupId = :groupId ";

		updateSQL(assignment, sql);

	}

	public void saveGroupAssignmentScore(String score, Long pk, String userName) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setScore(score);
		// assignment.setId(pk);
		assignment.setGroupId(pk);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set " + " score = :score, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate "
				+ " where groupId = :groupId";

		updateSQL(assignment, sql);
	}

	public void saveGroupLowScoreReason(String lowScoreReason, Long groupId,
			String userName) {
		StudentAssignment assignment = new StudentAssignment();
		logger.info("value" + lowScoreReason);
		logger.info("pk " + groupId);
		assignment.setLowScoreReason(lowScoreReason);
		// assignment.setId(pk);
		assignment.setGroupId(groupId);
		assignment.setEvaluatedBy(userName);
		assignment.setLastModifiedBy(userName);

		String sql = "update student_assignment set "
				+ " lowScoreReason = :lowScoreReason, "
				+ " evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate "
				+ " where groupId = :groupId ";

		updateSQL(assignment, sql);

	}

	public List<StudentAssignment> findAssignmentByStudent(String username) {

		String sql = "select sa.* from student_assignment sa where sa.username  = ? and sa.active = 'Y'";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<StudentAssignment> findAssignmentsByCourseId(Long courseId) {
		String sql = "select sa.*, a.*,sa.id as id from assignment a"
				+ " inner join student_assignment sa on sa.assignmentId = a.id "
				+ " where sa.courseId = ?  and a.active = 'Y'";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<StudentAssignment> findAssignmentByStatus(String status) {
		String sql = "select sa.*, a.*,sa.id as id from assignment a"
				+ " inner join student_assignment sa on sa.assignmentId = a.id "
				+ " where sa.submissionStatus = ?  and a.active = 'Y'";
		return findAllSQL(sql, new Object[] { status });
	}

	public List<StudentAssignment> findStudentsForPlagiarismCheck(
			Long assignmentId) {
		String sql = "select sa.* from student_assignment sa where sa.assignmentId=? and sa.studentFilePath is NOT NULL";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public List<StudentAssignment> findAssignmentByStudent(String username,
			String courseId) {

		String sql = "select sa.* from student_assignment sa where sa.username  = ? and sa.courseId = ? and sa.active = 'Y'";

		return findAllSQL(sql, new Object[] { username, courseId });

	}

	public List<StudentAssignment> getStudentsForAssignmentOnCampusId(
			Long assignmentId, Long courseId, Long campusId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id, u.campusId, u.campusName "
				+ " from user_course uc"
				+ "         inner join users u on uc.username = u.username"
				+ "         inner join program p on u.programId = p.id"
				+ "         left outer join student_assignment sa on sa.username = u.username and sa.courseId = uc.courseId and sa.assignmentId = ? "
				+ " where"
				+ " uc.role = ? "
				+ "and uc.active='Y'  "
				+ "and u.active='Y' and u.enabled = 1 "
				+ "and p.active='Y'"

				+ "and uc.courseId = ?  and u.campusId= ?  order by sa.lastModifiedDate desc ";
		return findAllSQL(sql,
				new Object[] { assignmentId, Role.ROLE_STUDENT.name(),
						courseId, campusId });

	}

	public List<StudentAssignment> getGroupsBasedOnCourseAndAssignmentWithGroupCourse(
			StudentAssignment sa, Long courseId, Long assignmentId) {
		String sql = "select a.id, a.assignmentName, sa.* ,g.id, g.groupName "
				+ " from assignment a, groups g, student_assignment sa, group_course gc "
				+ " where a.id=sa.assignmentId and a.courseId=gc.courseId and g.id=gc.groupId "
				+ " and a.courseId=?  and a.id=? and sa.groupId=g.id and a.active = 'Y' and sa.active='Y' and g.active='Y' and gc.active='Y' ";

		String countSql = "select a.id, a.assignmentName, sa.* ,g.id, g.groupName "
				+ " from assignment a, groups g, student_assignment sa, group_course gc "
				+ " where a.id=sa.assignmentId and a.courseId=gc.courseId and g.id=gc.groupId "
				+ " and a.courseId=?  and a.id=? and sa.groupId=g.id and a.active = 'Y' and sa.active='Y' and g.active='Y' and gc.active='Y' ";
		StringBuffer sqlBuffer = new StringBuffer(sql);
		StringBuffer countBuffer = new StringBuffer(countSql);

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(courseId);
		parameters.add(assignmentId);

		if (!StringUtils.isEmpty(sa.getSubmissionStatus())) {

			String sStatus = sa.getSubmissionStatus();
			switch (sStatus) {
			case "Y":
				sqlBuffer.append(" and sa.submissionStatus = 'Y' ");
				countBuffer.append(" and sa.submissionStatus = 'Y' ");
				break;
			case "N":
				sqlBuffer.append(" and sa.submissionStatus = 'N' ");
				countBuffer.append(" and sa.submissionStatus = 'N' ");
				break;

			}

		} else {
		}
		sqlBuffer.append(" GROUP BY g.groupName ");
		countBuffer.append(" GROUP BY g.groupName ");
		Object[] args = parameters.toArray();

		return findAllSQL(sqlBuffer.toString(), args);

	}

	public List<StudentAssignment> getStudentAssignmentSubmissionStats(
			String campusId, String fromDate, String toDate) {

		String typeCondition = "";
		if (campusId != null) {
			typeCondition = " c.campusId = ? ";
		} else {
			typeCondition = " c.campusId is ? ";
		}

		String sql = "select sa.* from student_assignment sa, course c, users u  where sa.courseId = c.id and sa.username = u.username and u.active = 'Y' and "
				+ typeCondition
				+ " and sa.studentFilePath is not null and sa.lastModifiedDate BETWEEN  ? and ? ";
		return findAllSQL(sql, new Object[] { campusId, fromDate, toDate });
	}

	public void makeAllInactiveFirst(Long assignmentId, Long groupId) {
		StudentAssignment assignment = new StudentAssignment();
		// assignment.setId(Long.valueOf(studentAssignmentId));
		assignment.setAssignmentId(Long.valueOf(assignmentId));
		assignment.setGroupId(Long.valueOf(groupId));

		String sql = "update student_assignment set "
				+ " isSubmitterInGroup = 'N' "
				+ " where assignmentId = :assignmentId and groupId =:groupId ";

		updateSQL(assignment, sql);
	}

	public void updateSubmitter(String studentAssignmentId) {
		StudentAssignment assignment = new StudentAssignment();
		assignment.setId(Long.valueOf(studentAssignmentId));

		String sql = "update student_assignment set "
				+ " isSubmitterInGroup = 'Y' " + " where id = :id";

		updateSQL(assignment, sql);
	}

	public List<StudentAssignment> getStudentsByGroup(Long assignmentId) {

		String sql = " select sa.id,sa.isSubmitterInGroup,u.username,concat(u.firstName,' ',u.lastName) as studentName,u.rollNo,sa.groupId "
				+ " from student_assignment sa,users u,assignment a where sa.username=u.username "
				+ " and sa.assignmentId=a.id and a.id = ? ";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public List<StudentAssignment> findOneAssignmentByGroupId(
			Long assignmentId, Long groupId) {
		String sql = "select a.*,sa.*,concat(u.firstName,' ',u.lastName) as studentName from assignment a "

				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join users u on sa.username = u.username "
				+ " where sa.assignmentId = ? and sa.groupId = ? ";
		return findAllSQL(sql, new Object[] { assignmentId, groupId });
	}

	public StudentAssignment getSubmitterForAssignment(Long assignmentId,
			Long groupId) {

		String sql = " select sa.*,concat(u.firstName,' ',u.lastName) as studentName from "
				+ getTableName()
				+ ""
				+ " sa,users u where sa.assignmentId = ?  and sa.groupId = ? and sa.isSubmitterInGroup = 'Y' "
				+ " and sa.username = u.username ";

		return findOneSQL(sql, new Object[] { assignmentId, groupId });
	}

	public List<String> getAllSapIdsOfStudentFromAssignment(Long assignmentId,
			Long groupId) {

		String sql = " select distinct username from " + getTableName()
				+ " where assignmentId = ?  and groupId = ? and active = 'Y' ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { assignmentId, groupId }, String.class);
	}
	
	public List<StudentAssignment> getAssignmentsForGradeCenter(
			String username, Long courseId) {
		String sql = " select distinct concat(u.firstname,' ', u.lastname) as name,asub.startDate, asub.endDate, a.assignmentName,( case when  asub.submissionStatus <> 'Y'  or  asub.submissionStatus is null "
				+ " then 'NC' when asub.evaluationStatus <> 'Y' then 'NE'when asub.id is null then 'NA' when a.showResultsToStudents='N' then 'ND' else asub.score end) as score"
				+ " from users u inner join user_course uc on uc.username = u.username and  uc.role = ? inner join assignment a on a.courseId = uc.courseId "
				+ " inner join course c on a.courseId = c.id "
				+ " left outer join student_assignment asub on asub.assignmentId = a.id and asub.username = u.username "
				+ "  where uc.courseId = ? and c.active= 'Y' and asub.username = ? and asub.active='Y' ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId, username });
	}
	
	public List<StudentAssignment> getAssignmentsForParentReport(String username, Long courseId) {
		
		String sql = " select sa.score-a.maxScore as unscored , sa.score, a.maxScore, a.assignmentName, sa.submissionDate, a.id from assignment a "
				+ " inner join student_assignment sa on sa.assignmentId = a.id "
				+ " where sa.courseId = ? and sa.username = ? and sa.submissionStatus = 'Y' "
				+ " and sa.evaluationStatus = 'Y' and a.showResultsToStudents = 'Y' and a.active = 'Y' order by sa.id ";
		
		return findAllSQL(sql, new Object[]{ courseId, username });		
	}
	
	
	public List<StudentAssignment> getStudentsForAssignmentModule(List<Long> assignmentId,
			List<String> courseId, String acadYear) {

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		params.put("courseId", courseId);
		params.put("acadYear", acadYear);
		
		String sql = "select uc.username,c.id as courseId, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id ,u.campusId, u.campusName "
				+ "from user_course uc "
				+ "inner join users u on uc.username = u.username "
				+ "inner join program p on u.programId = p.id "
				+ "inner join course c on c.id = uc.courseId "
				+ "left outer join student_assignment sa on sa.username = u.username and sa.assignmentId in (:assignmentId) "
				+ "where "
				+ "uc.role = 'ROLE_STUDENT' "
				+ "and uc.active='Y' "
				+ "and u.active='Y' and u.enabled = 1 and p.active='Y' "
				+ "and c.id in (:courseId) and c.acadYear = :acadYear group by u.username order by sa.lastModifiedDate desc";
		
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));

	}
	
	
public List<StudentAssignment> getStudentsForAssignmentForModuleOnCampusId(
			List<Long> assignmentId, List<String> courseId,String acadYear, Long campusId) {
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		params.put("courseId", courseId);
		params.put("acadYear", acadYear);
		params.put("campusId", campusId);
		
		String sql = "select uc.username,c.id, p.programName, u.firstname, u.lastname, sa.assignmentId, sa.id, u.campusId, u.campusName "
				+ " from user_course uc"
				+ "         inner join users u on uc.username = u.username"
				+ "         inner join program p on u.programId = p.id"
				+ " inner join course c on c.id = uc.courseId "
				+ "         left outer join student_assignment sa on sa.username = u.username and sa.assignmentId in (:assignmentId) "
				+ " where"
				+ " uc.role = 'ROLE_STUDENT' "
				+ "and uc.active='Y'  "
				+ "and u.active='Y' and u.enabled = 1 "
				+ "and p.active='Y'"

				+ "and c.id in (:courseId) and c.acadYear = :acadYear and u.campusId= :campusId group by u.username order by sa.lastModifiedDate desc ";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));

	}
	
	public StudentAssignment getNoOfStudentsAllocated(List<Long> assignmentId) {
		
	Map<String, Object> params = new HashMap<String, Object>();
	params.put("assignmentId", assignmentId);

	
		String sql = " select count(*) as count from student_assignment where assignmentId in (:assignmentId)";

		return getNamedParameterJdbcTemplate().queryForObject(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	}
	
	public void deleteStudentAssigmentForModule(Long assigmentId) {
		String sql = " update student_assignment set active = 'N' where assignmentId = ?";
		getJdbcTemplate().update(sql, new Object[] { assigmentId });

		
	}
	
	public List<StudentAssignment> getStudentsBasedOnAssignmentForModule(
			StudentAssignment assignment, List<Long> assignmentId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		
		String sql = "select  a.* ,sa.*  from student_assignment sa"
				+ "         inner join course c on c.id=sa.courseId"
				+ "         inner join assignment a on a.id=sa.assignmentId"
				+ " where"
				+ " a.id in (:assignmentId) and c.active='Y' and a.active='Y' and sa.submissionStatus='y' order by sa.id asc  ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	}
	public List<StudentAssignment> getStudentFilesForModule(List<Long> assignmentId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		
		String sql = "select sa.studentFilePath, sa.username from student_assignment sa where sa.assignmentId in (:assignmentId) and sa.studentFilePath is NOT NULL";
		
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	}

	public List<StudentAssignment> findStudentsForPlagiarismCheckForModule(
			List<Long> assignmentId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		
		String sql = "select sa.* from student_assignment sa where sa.assignmentId in (:assignmentId) and sa.studentFilePath is NOT NULL";


		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	}
	public List<StudentAssignment> findOneAssignment(List<Long> assignmentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		
		String sql = "select a.*,sa.* from assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " where sa.assignmentId in (:assignmentId)";
		
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	}

	//Hiren 19-02-2020
	public StudentAssignment getAllGroupAssignemnt(String id){
		String sql ="select groupId,assignmentId from student_assignment where id =?";
		return findOneSQL(sql, new Object[] { id });
	}
	public List<String> getGroupAssignmentStudentFiles(Long groupId,Long assignmentId){
		String sql ="select studentFilePath from student_assignment where groupId=? and assignmentId= ? and studentFilePath is not null";
		return getJdbcTemplate().queryForList(sql,new Object[] { groupId, assignmentId }, String.class);
	}
	
	public List<StudentAssignment> getStudentFilesForlateSubmitted(Long assignmentId) {
		String sql = "select sa.studentFilePath, sa.username from student_assignment sa,assignment a where sa.assignmentId=a.id and sa.assignmentId=? and sa.studentFilePath is NOT NULL and sa.submissionDate > a.endDate";
		return findAllSQL(sql, new Object[] { assignmentId });
	}
	
	public List<StudentAssignment> getStudentsAssignmentReport(Long assignmentId) {

		String sql = "select  u.rollNo,a.assignmentName,a.facultyId,sa.* from student_assignment sa"
				+ "  inner join course c on c.id=sa.courseId"
				+ "  inner join assignment a on a.id=sa.assignmentId"
				+ " left outer join users u on sa.username = u.username"
				+ " where"
				+ " a.id = ? and c.active='Y' and a.active='Y' and sa.submissionStatus ='Y' order by u.rollNo asc  ";

		return findAllSQL(sql, new Object[] {assignmentId});
	}
	
	
	//05/05/2020 shubham
	
	public int checkAssignmentEvaluationStatus(String username, Long id) {
		final String sql = " select count(*) from assignment a,student_assignment sa where a.startDate <= ?  and sa.assignmentId=a.id and sa.username= ? "
				+ " and a.id= ? and sa.evaluationStatus = 'Y' and ( sa.approvalStatus is null or sa.approvalStatus = 'Approved' ) ";
		Date dt = Utils.getInIST();
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { dt, username, id });
	}
	
	public String getApprovalStatusForAssignment(String username, Long id) {
		String sql = " Select sa.approvalStatus from assignment a, student_assignment sa where  sa.assignmentId=a.id and sa.username= ? "
		+ " and a.id = ?";
		return returnSingleColumn(sql, new Object[] { username, id });
	}
	
	public void setSubmissionDate(String submissionDate, Long id, String username) {
		executeUpdateSql(
				"update student_assignment set submissionDate= ? where assignmentId= ? and username=? ",
				new Object[] {submissionDate, id, username });
	}
	
	public void setSubmissionStatusForGroupAssignment(Long assignmentId)
	{
		executeUpdateSql(
				"update student_assignment set submissionStatus = 'Y' where assignmentId= ? ",
				new Object[] {assignmentId});
	}
	
	public List<String> getAllSapIdsOfStudentFromAssignmentForRemark(Long assignmentId) {

		String sql = " select distinct username from " + getTableName()+ " where assignmentId = ? and submissionStatus='Y' and active = 'Y' ";

		return getJdbcTemplate().queryForList(sql,
				new Object[] { assignmentId }, String.class);
	}

	public List<StudentAssignment> getStudentsAssignmentReportForModule(List<Long> assignmentId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("assignmentId", assignmentId);
		
		String sql = "select  u.rollNo,a.assignmentName,a.facultyId,sa.* from student_assignment sa"
				+ "  inner join course c on c.id=sa.courseId"
				+ "  inner join assignment a on a.id=sa.assignmentId"
				+ " left outer join users u on sa.username = u.username"
				+ " where"
				+ " a.id in (:assignmentId) and c.active='Y' and a.active='Y' and sa.submissionStatus ='Y' order by u.rollNo asc  ";

		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	}
	

public void updateAssignmentsEvaluationMarks(StudentAssignment sa,List<Long> assignmentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("assignmentId", assignmentId);
		params.put("score",sa.getScore());
		params.put("remarks", sa.getRemarks());
		params.put("lowScoreReason",sa.getLowScoreReason());
		params.put("evaluatedBy",sa.getEvaluatedBy());
		params.put("evaluationStatus",sa.getEvaluationStatus());
		params.put("lastModifiedBy",sa.getLastModifiedBy());
		params.put("username",sa.getUsername());
		
		String sql = "update student_assignment set " + " score = :score, "
				+ "remarks=:remarks, lowScoreReason=:lowScoreReason, evaluatedBy = :evaluatedBy, " + " evaluationStatus = 'Y', "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = now() " + " where username = :username and assignmentId in (:assignmentId)";
//		updateSQL(sa, sql);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	//Hiren 29-08-2020
	public List<StudentAssignment> getAllStudentToEvaluateAssignment(Long assignmentId){
		String sql="select CONCAT(u.firstname, ' ',u.lastname) as studentName,u.rollNo,u.username,sa.* from student_assignment sa, users u where sa.username=u.username and sa.assignmentId=? order by rollNo";
		return findAllSQL(sql, new Object[] { assignmentId });
	}
	public List<String> getAllStudentSAPIdToEvaluateAssignment(Long assignmentId){
		String sql="select distinct sa.username from student_assignment sa where sa.assignmentId=? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ assignmentId });
	}
	public void updateStudentAssignmentTotalScore(StudentAssignment sa) {
		Map<String, Object> params = new HashMap<String, Object>();
	
		params.put("assignmentId", sa.getAssignmentId());
		params.put("score",sa.getScore());
		params.put("evaluatedBy", sa.getEvaluatedBy());
		params.put("username", sa.getUsername());
		String sql="update student_assignment set score =:score, evaluationStatus = 'Y',evaluatedBy = :evaluatedBy,lastModifiedBy = :evaluatedBy,lastModifiedDate = now() where username =:username and assignmentId = :assignmentId";
		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	public void updateDisclaimer(String assignmentId,String username) {
		String sql = "update student_assignment set acceptDisclaimerDate = now(), isAcceptDisclaimer = 'Y' where assignmentId =? and username =?";
		executeUpdateSql(sql,new Object[] {assignmentId, username });
	}
	
	
	//new queries added by akshay
	
	public StudentAssignment getStudentAssignmentHashKey(String username,Long assignmentId) {
		String sql =" select * from student_assignment_hashkeys where active ='Y' and username =? and assignmentId =? ";
		return findOneSQL(sql, new Object[] {username,assignmentId});
	}
	public int insertStudentAssignmentHashKey(StudentAssignment sa) {

		SqlParameterSource parameterSource = getParameterSource(sa);
		final String sql = "INSERT INTO student_assignment_hashkeys(username,assignmentId,hashKey,keyGenerationTime,isHashKeyLateSubmitted,lateSubmRemark,"
				+ " active,"
				+ " createdBy,createdDate,lastModifiedBy,lastModifiedDate) "
				+ "VALUES (:username,:assignmentId,:hashKey,:keyGenerationTime,:isHashKeyLateSubmitted,:lateSubmRemark,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
		return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	}
	
	public List<StudentAssignment> studentAssignmentHashKeys(Long assignmentId){
		
		String sql =" select u.firstName,u.lastName,u.rollNo,sh.username,sh.hashKey,sh.keyGenerationTime,sh.lateSubmRemark from users u,student_assignment_hashkeys sh"
				+ " where u.username = sh.username and sh.active ='Y' and sh.assignmentId=? order by u.rollNo";
		
		return findAllSQL(sql, new Object[] {assignmentId});
	}

	public int updateStudentAssignmentHashKey(StudentAssignment sa) {

		SqlParameterSource parameterSource = getParameterSource(sa);
		final String sql = "update student_assignment_hashkeys set hashKey=:hashKey ,keyGenerationTime=:keyGenerationTime,isHashKeyLateSubmitted=:isHashKeyLateSubmitted,lateSubmRemark=:lateSubmRemark,"
				+ " lastModifiedBy=:lastModifiedBy ,lastModifiedDate=:lastModifiedDate where id =:id ";
		return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	}
	
	public int softDelStudentAssignmentHashKey(StudentAssignment sa) {

		SqlParameterSource parameterSource = getParameterSource(sa);
		sa.setLastModifiedDate(Utils.getInIST());
		final String sql = "update student_assignment_hashkeys set active=:active, "
				+ " lastModifiedDate=:lastModifiedDate where id =:id ";
		return getNamedParameterJdbcTemplate().update(sql, parameterSource);

	}
	
	public int chkStartandEndDateOfAssignment(String username, Long id,Date keyGenDate) {
		final String sql = " select count(*) from assignment a,student_assignment sa where a.startDate <= ?  and sa.assignmentId=a.id and sa.username= ? "
				+ " and a.id= ?";
		
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { keyGenDate, username, id });
	}

	public int chkStartandEndDtOfAssignment(String username, Long id,Date keyGenDate) {
		final String sql = " select count(*) from assignment a,student_assignment sa where   a.endDate >= ?  and sa.assignmentId=a.id and sa.username= ? "
				+ " and a.id= ? ";
		
		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { keyGenDate, username, id });
	}
	
	public List<StudentAssignment> getStudentDetails(Long id) {
		
		
		String sql="select sa.id, CONCAT(firstname, ' ', lastname) as studentName ,sa.assignmentId,sa.username,sa.submissionStatus,sa.submissionDate,sa.studentFilePath,sa.isAcceptDisclaimer,"
				+ "sa.acceptDisclaimerDate,sah.hashKey,sah.lateSubmRemark, sah.keyGenerationTime,sah.isHashKeyLateSubmitted "
				+ "from users u,student_assignment sa left outer  join student_assignment_hashkeys sah on  sa.assignmentId = sah.assignmentId "
				+ "and sa.username= sah.username and sah.active='Y' where sa.assignmentid=? and  sa.active='Y'  and u.username= sa.username";
		return findAllSQL(sql, new Object[] {id});
		}

	/* For Assignment Pool Start */
	public List<StudentAssignment> getStudentsForAssignent(Long assignmentId) {
		String sql = "select sa.*,saq.studentAssignmentId from "+getTableName()
				+ "  sa"
				+ " left outer join student_assignment_question saq on sa.id=saq.studentAssignmentId   "
				+ " where sa.assignmentId = ? and sa.active='Y' ";
		return findAllSQL(sql, new Object[] { assignmentId });
	}
	
	public void deleteStudentByAssignment(Long assignmentId) {
		executeUpdateSql("delete from student_assignment where assignmentId = ?",
			new Object[] { assignmentId });
	}
	public List<StudentAssignment> getStudentsForAssignentById(Long assignmentId) {
		String sql = "select sa.* from assignment a, student_assignment sa where sa.assignmentid = a.id and sa.assignmentId = ? and sa.active = 'Y' and a.active = 'Y' ";
		return findAllSQL(sql, new Object[] { assignmentId });
	}
	/* For Assignment Pool End */
	
	public List<StudentAssignment> getStudentAssignmentMarksByAssignmentIds(List<String> assignmentIds) {
		  Map<String,Object> mapper = new HashMap<>();
		  mapper.put("assignmentIds", assignmentIds);
		  String sql =" select sa.score,sa.username from "+getTableName()+" sa where sa.assignmentId in(:assignmentIds) "
		  		+ " and sa.active ='Y' and sa.submissionStatus='Y' and sa.score is not null ";
		  
		  return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	  }
	  
	  public List<StudentAssignment> getStudentAssignmentMarksByAssignmentIds(List<String> assignmentIds,List<String> usernames) {
		  Map<String,Object> mapper = new HashMap<>();
		  mapper.put("assignmentIds", assignmentIds);
		  mapper.put("usernames", usernames);
		  
		  String sql =" select sa.score,sa.username from "+getTableName()+" sa where sa.assignmentId in(:assignmentIds) and st.username in(:usernames)"
		  		+ " and sa.active ='Y' and st.submissionStatus='Y' and sa.score is not null ";
		  
		  return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(StudentAssignment.class));
	  }
	  
	  
	  //PETER 15/07/2021
	  public int updateAssignmentApprovalStatus(List<String> usernames, Long assignmentId) {
		String sql = "UPDATE student_assignment \n"
				+ "SET approvalStatus='Approve',submissionStatus='Y', lastModifiedDate = now() \n"
				+ "WHERE username \n"
				+ "	IN (:usernames)	AND assignmentId=:assignmentId";
		Map<String, Object> mapper = new HashMap<>();
		mapper.put("usernames", usernames);
		mapper.put("assignmentId", assignmentId);
		return getNamedParameterJdbcTemplate().update(sql, mapper);
	}
	  
		//Peter 04/08/2021
		public int updateFilePath(Long studentAssignmentId, String newFilePath) {
			String sql = "UPDATE student_assignment\n"
					+ "SET studentFilePath=:newFilePath,filePreviewPath=:newFilePath\n"
					+ "WHERE id=:studentAssignmentId;";
			Map<String, Object> mapper = new HashMap<>();
			mapper.put("studentAssignmentId", studentAssignmentId);
			mapper.put("newFilePath", newFilePath);
			return getNamedParameterJdbcTemplate().update(sql, mapper);
		}
}
