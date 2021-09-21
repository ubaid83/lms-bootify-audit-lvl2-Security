package com.spts.lms.daos.assignment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.web.utils.Utils;

@Repository("assignmentDAO")
public class AssignmentDAO extends BaseDAO<Assignment> {

	@Override
	protected String getTableName() {
		return "assignment";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO assignment(assignmentName,acadYear,acadMonth,rightGrant,courseId,startDate,"
				+ "endDate,filePath,assignmentText,filePreviewPath,assignmentType,showResultsToStudents,active,facultyId,allowAfterEndDate,threshold,plagscanRequired,runPlagiarism,"
				+ "sendEmailAlert,sendSmsAlertToParents,sendEmailAlertToParents,sendSmsAlert,maxScore,createdBy,createdDate,lastModifiedBy,lastModifiedDate,groupId,submitByOneInGroup,allocateToStudents,moduleId,parentModuleId,createdByAdmin,isCheckSumReq,isQuesConfigFromPool,maxQuestnToShow,randomQuestion) "
				+ "VALUES (:assignmentName,:acadYear,:acadMonth,:rightGrant,:courseId,:startDate,:endDate,:filePath,:assignmentText,:filePreviewPath,:assignmentType,"
				+ ":showResultsToStudents,:active,:facultyId,:allowAfterEndDate,:threshold,:plagscanRequired,:runPlagiarism,"
				+ ":sendEmailAlert,:sendSmsAlertToParents,:sendEmailAlertToParents,:sendSmsAlert,:maxScore,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:groupId,:submitByOneInGroup,:allocateToStudents,:moduleId,:parentModuleId,:createdByAdmin,:isCheckSumReq,:isQuesConfigFromPool,:maxQuestnToShow,:randomQuestion)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update assignment set "
				+ "assignmentName = :assignmentName,"
				+ "acadYear = :acadYear ,"
				+ "acadMonth = :acadMonth ,"
				+ "rightGrant = :rightGrant ,"
				+ "startDate = :startDate ,"
				+ "endDate = :endDate ,"
				+ "filePath = :filePath,"
				+ "submitByOneInGroup=:submitByOneInGroup,"
				+ "assignmentText = :assignmentText,"
				+ "filePreviewPath = :filePreviewPath, assignmentType=:assignmentType,"
				+ "showResultsToStudents = :showResultsToStudents ,"
				+ "active = :active,"
				+ "allowAfterEndDate = :allowAfterEndDate ,"
				+ "sendEmailAlertToParents = :sendEmailAlertToParents ,"
				+ "sendSmsAlertToParents = :sendSmsAlertToParents ,"
				+ "sendEmailAlert = :sendEmailAlert ,"
				+ "sendSmsAlert = :sendSmsAlert ," + "maxScore = :maxScore ,"
				+ "threshold = :threshold , "
				+ "plagscanRequired = :plagscanRequired , "
				+ "runPlagiarism = :runPlagiarism , "
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "allocateToStudents = :allocateToStudents,"
				+ "moduleId = :moduleId, "
				+ " createdByAdmin=:createdByAdmin, isCheckSumReq=:isCheckSumReq, isQuesConfigFromPool=:isQuesConfigFromPool, "  
				+ "maxQuestnToShow=:maxQuestnToShow, randomQuestion=:randomQuestion, "
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<Assignment> findByUser(String username, String acadMonth,
			String acadYear) {
		Date dt = Utils.getInIST();
		final String sql = "SELECT a.*, c.courseName FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? " + " and sa.acadMonth = ? "
				+ " and sa.acadYear = ? "
				+ " and a.startDate <= ? and a.active = 'Y' "
				+ " order by endDate desc";
		return findAllSQL(sql,
				new Object[] { username, acadMonth, acadYear, dt });
	}

	public void updateFacultyAssigned(String facultyId, Long assignmentId) {
		executeUpdateSql(
				"Update assignment a set a.facultyId = ? where a.id=?",
				new Object[] { facultyId, assignmentId });

	}

	public List<Assignment> findAllAssignmentForHOD(Long programId) {
		String sql = "select a.* from assignment a,course c , program p where a.courseId= c.id and c.programId = p.id and "
				+ " p.id=? and a.active ='Y' order by a.createdDate desc";
		return findAllSQL(sql, new Object[] { programId });
	}

	public List<Assignment> findByFacultyAndCourseActive_new(String username,
			Long courseId) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId in (select uc.username from user_course uc where uc.courseId= ? ) "
				+ " and a.courseId = ? " + " and a.active ='Y' "
				+ " and (a.rightGrant ='Y' " + " or a.facultyId = ?) and parentModuleId is null"
				+ " order by endDate desc";

		return findAllSQL(sql, new Object[] { courseId, courseId, username });
	}

	/*public List<Assignment> findByUser(String username) {
		
		 * final String sql = "SELECT a.*, c.courseName FROM assignment a " +
		 * " inner join student_assignment sa on sa.assignmentid = a.id " +
		 * " inner join course c on a.courseId = c.id " +
		 * " where sa.username = ? " + " and a.startDate <= ? " +
		 * "and a.active='Y'" + "and c.active='Y'"
		 * 
		 * + " order by endDate desc";
		 
		final String sql = "SELECT a.*, c.courseName, sa.submissionStatus,sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? "
				+ " and a.startDate <= ? and a.endDate IS NOT NULL "
				+ " and a.active='Y'" + "and c.active='Y' "
				+ " order by endDate desc ";

		Date dt = Utils.getInIST();
		return findAllSQL(sql, new Object[] { username, dt });
	}*/
	
	/*
	 * public List<Assignment> findByUser(String username) { Calendar c =
	 * Calendar.getInstance(); c.setTime(Utils.getInIST()); int month =
	 * c.get(Calendar.MONTH) + 1; int year = c.get(Calendar.YEAR) - 1; int
	 * currentYear = c.get(Calendar.YEAR);
	 * 
	 * String sql = "";
	 * 
	 * if(month > 6) { sql =
	 * "SELECT a.*, c.courseName, sa.submissionStatus,sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
	 * + "  inner join student_assignment sa on sa.assignmentid = a.id " +
	 * "  inner join course c on a.courseId = c.id " + "  where sa.username = ? " +
	 * "  and (a.startDate <= ? or " + "  a.endDate >= ?)and (c.acadYear = '" +
	 * currentYear + "') " +
	 * "  and a.active='Y' and c.active='Y' and a.endDate IS NOT NULL " +
	 * "  order by endDate desc"; } else { sql =
	 * "SELECT a.*, c.courseName, sa.submissionStatus,sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
	 * + "  inner join student_assignment sa on sa.assignmentid = a.id " +
	 * "  inner join course c on a.courseId = c.id " + "  where sa.username = ? " +
	 * "  and (a.startDate <= ? or " + "  a.endDate >= ?)and (c.acadYear = '" +
	 * currentYear + "' or c.acadYear = '" + year + "%') " +
	 * "  and a.active='Y' and c.active='Y' and a.endDate IS NOT NULL " +
	 * "  order by endDate desc"; }
	 * 
	 * 
	 * 
	 * Date dt = Utils.getInIST(); return findAllSQL(sql, new Object[] { username,
	 * dt, dt }); }
	 */
	
	public List<Assignment> findByUser(String username)
	{
		 Calendar c = Calendar.getInstance();
	        c.setTime(Utils.getInIST());
	        int month = c.get(Calendar.MONTH) + 1;
	        int year = c.get(Calendar.YEAR) - 1;
	        int currentYear = c.get(Calendar.YEAR);
	        
	        String sql = "";
	        
	        if(month > 6)
	        {
	        	sql = "SELECT a.*, c.courseName, sa.submissionStatus, sa.approvalStatus, sa.evaluationStatus, sa.submissionDate ,sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
	        			+ "  inner join student_assignment sa on sa.assignmentid = a.id "
	    			    + "  inner join course c on a.courseId = c.id "
	    				+ "  where sa.username = ? "
	    				+ "  and (a.startDate <= ? or "
	    				+ "  a.endDate >= ?)and (c.acadYear = '"+ currentYear +"') "
	    				+ "  and a.active='Y' and c.active='Y' and a.endDate IS NOT NULL "
	    				+ "  order by a.endDate desc";
	        }
	        else
	        {
	        	sql = "SELECT a.*, c.courseName, sa.submissionStatus, sa.approvalStatus, sa.evaluationStatus , sa.submissionDate, sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
	        			+ "  inner join student_assignment sa on sa.assignmentid = a.id "
	    			    + "  inner join course c on a.courseId = c.id "
	    				+ "  where sa.username = ? "
	    				+ "  and (a.startDate <= ? or "
	    				+ "  a.endDate >= ?)and (c.acadYear = '"+ currentYear +"' or c.acadYear = '"+ year +"') "
	    				+ "  and a.active='Y' and c.active='Y' and a.endDate IS NOT NULL "
	    				+ "  order by a.endDate desc";
	        }
	        
		  

		Date dt = Utils.getInIST();
		return findAllSQL(sql, new Object[] { username, dt, dt });
	}
	
	public List<Assignment> findByUserNew(String username) {
		Calendar c = Calendar.getInstance();
		c.setTime(Utils.getInIST());
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR) - 1;
		int currentYear = c.get(Calendar.YEAR);

		String sql = "";

		if (month > 6) {
			sql = "SELECT a.*, c.courseName, sa.submissionStatus, sa.approvalStatus, sa.evaluationStatus, sa.submissionDate ,sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
					+ "  inner join student_assignment sa on sa.assignmentid = a.id "
					+ "  inner join course c on a.courseId = c.id " + "  where sa.username = ? "
					+ "  and (a.startDate <= ? )and (c.acadYear = '" + currentYear + "') "
					+ "  and a.active='Y' and c.active='Y' and a.endDate IS NOT NULL " + "  order by endDate desc";
		} else {
			sql = "SELECT a.*, c.courseName, sa.submissionStatus, sa.approvalStatus, sa.evaluationStatus , sa.submissionDate, sa.id as studentAssignmentId, sa.studentFilePath,sa.isSubmitterInGroup,sa.evaluationStatus FROM assignment a "
					+ "  inner join student_assignment sa on sa.assignmentid = a.id "
					+ "  inner join course c on a.courseId = c.id " + "  where sa.username = ? "
					+ "  and (a.startDate <= ? )and (c.acadYear = '" + currentYear
					+ "' or c.acadYear = '" + year + "') "
					+ "  and a.active='Y' and c.active='Y' and a.endDate IS NOT NULL " + "  order by a.endDate desc";
		}

		Date dt = Utils.getInIST();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(dt);
		return findAllSQL(sql, new Object[] { username, currentTime });
	}


	public void showResults(Long id) {
		executeUpdateSql(
				"update assignment set showResultsToStudents='Y' where id= ? ",
				new Object[] { id });
	}

	public List<Map<String, Object>> getAssignmentsWithAdvanceSearchByFaculty(
			String username) {
		String sql = " Select sa.username as sapId,a.facultyId as facultyId ,c.courseName as courseName,a.assignmentName as assignmentName,sa.evaluationStatus as evaluationStatus,sa.submissionStatus as submissionStatus,sa.score as score,sa.remarks as remarks,sa.lowScoreReason as lowScoreReason from assignment a "
				+ " inner join student_assignment sa on a.id = sa.assignmentId "
				+ " inner join course c on a.courseId = c.id "
				+ " where  c.active ='Y'   and a.active='Y' "
				+ " and  sa.createdBy = ?";
		return getJdbcTemplate().queryForList(sql, new Object[] { username });
	}

	public List<Assignment> findByUserPendingForParents(String username,
			String acadMonth, String acadYear) {
		String sql = "SELECT a.*, c.courseName FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? " + " and sa.acadMonth = ? "
				+ " and sa.acadYear = ? " // + " and a.startDate <= ? "
				+ " and " + "sa.submissionStatus='N'";
		sql = sql + " order by endDate desc";

		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });

	}

	public List<Assignment> searchAssignmentsForFaculty(String username,
			Long programId, Long courseId) {
		String sql = " select a.* from "
				+ getTableName()
				+ " a,course c , user_course uc ,program p where a.courseId= c.id and c.programId = p.id and "
				+ "  uc.courseId = c.id and uc.username=? and "
				+ " p.id= ? and a.courseId=? and a.active ='Y' and a.parentModuleId is null and a.moduleId is null group by a.id order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { username, programId, courseId });
	}

	public List<Assignment> searchAssignmentForHOD(Long programId) {

		String sql = " select a.* from "
				+ getTableName()
				+ " a,course c , program p where a.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and a.active ='Y' and c.active ='Y' order by a.createdDate desc";
		return findAllSQL(sql, new Object[] { programId });

	}

	public List<Assignment> searchAssignmentsForFaculty(String username,
			Long programId) {
		String sql = " select a.* from "
				+ " assignment a,course c , user_course uc ,program p where a.courseId= c.id and c.programId = p.id and "
				+ "  uc.courseId = c.id and uc.username=? and "
				+ " p.id= ? and a.active ='Y' and c.active ='Y' and a.parentModuleId is null and a.moduleId is null group by a.id order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { username, programId });
	}

	public List<Assignment> findAllAssigment(String acadMonth, String acadYear) {
		String sql = "select a.* from assignment a "
				+ " where a.acadMonth = ? and a.acadYear = ? ";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear });

	}

	/*public List<Assignment> findAllAssigmentForFaculty(String acadMonth,
			String acadYear, String facultyId) {
		String sql = "select a.* from assignment a "
				+ " where a.acadMonth = ? and a.acadYear = ? and a.facultyId = ?";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, facultyId });

	}*/

	public List<Assignment> findByUserPendingForParents(String username) {
		String sql = "SELECT a.*, c.courseName FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? " // + " and a.startDate <= ? "
				+ "and a.active='Y'"
				+ "and c.active='Y'"
				+ " and "
				+ "(sa.submissionStatus='N' or a.allowAfterEndDate='Y' )  and c.active='Y'";
		sql = sql + " order by endDate desc";

		return findAllSQL(sql, new Object[] { username });

	}

	public List<Assignment> findByUserPending(String username,
			String acadMonth, String acadYear, boolean isFaculty) {
		String sql = "";
		if (!isFaculty) {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where sa.username = ? " + " and sa.acadMonth = ? "
					+ " and sa.acadYear = ? " // + " and a.startDate <= ? "
					+ " and " + "sa.submissionStatus='N'";
		} else {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where a.facultyId = ? " + " and sa.acadMonth = ? "
					+ " and sa.acadYear = ? " // + " and a.startDate <= ? "
					+ " and " + "sa.evaluationStatus='N'";
		}

		sql = sql + " order by endDate desc";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
		// , new Date() });
	}

	/*
	 * public List<Assignment> findByUserPending(String username, boolean
	 * isFaculty) { String sql = ""; if (!isFaculty) { sql =
	 * "SELECT a.*, c.courseName FROM assignment a " +
	 * " inner join student_assignment sa on sa.assignmentid = a.id " +
	 * " inner join course c on a.courseId = c.id " + " where sa.username = ? "
	 * + "" // + // " and a.startDate <= ? " + "and a.active='Y'" +
	 * "and c.active='Y'" + " and " +
	 * "(sa.submissionStatus='N' or sa.submissionStatus is null or a.allowAfterEndDate='Y')"
	 * ; } else { sql = "SELECT a.*, c.courseName FROM assignment a " +
	 * " inner join student_assignment sa on sa.assignmentid = a.id " +
	 * " inner join course c on a.courseId = c.id " + " where a.facultyId = ? "
	 * + " " // + // " and a.startDate <= ? " + "and a.active='Y'" +
	 * "and c.active='Y'" + " and " +
	 * "(sa.evaluationStatus='N' or sa.evaluationStatus is null)"; }
	 * 
	 * sql = sql + " order by endDate desc"; return findAllSQL(sql, new Object[]
	 * { username }); // , new Date() }); }
	 */

	public List<Assignment> findByUserPending(String username, boolean isFaculty) {
		String sql = "";
		if (!isFaculty) {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where sa.username = ? "
					+ "" // +
							// " and a.startDate <= ? "
					+ " and a.active='Y'"
					+ " and c.active='Y'"
					+ " and "
					+ " (sa.submissionStatus='N' or sa.submissionStatus is null or a.allowAfterEndDate='Y') ";
		} else {
			sql = "SELECT a.*, c.courseName FROM user_course uc , assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where a.facultyId = ? "
					+ " " // +
							// " and a.startDate <= ? "
					+ " and a.active='Y' "
					+ " and c.active='Y' "
					+ " and "
					+ " (sa.evaluationStatus='N' or sa.evaluationStatus is null) and uc.courseId =a.courseId and uc.username =a.facultyId  group by a.id";
		}

		sql = sql + " order by createdDate desc";
		return findAllSQL(sql, new Object[] { username });
		// , new Date() });
	}

	public List<Assignment> findByUserPending(String username,
			boolean isFaculty, Long programId) {
		String sql = "";
		if (!isFaculty) {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where sa.username = ? "
					+ "" // +
							// " and a.startDate <= ? "
					+ " and a.active='Y'"
					+ " and c.active='Y'"
					+ " and "
					+ " (sa.submissionStatus='N' or sa.submissionStatus is null) and c.programId =? ";
		} else {
			sql = "SELECT a.*, c.courseName FROM user_course uc , assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where a.facultyId = ? "
					+ " " // +
							// " and a.startDate <= ? "
					+ " and a.active='Y' "
					+ " and c.active='Y' "
					+ " and "
					+ " (sa.evaluationStatus='N' or sa.evaluationStatus is null) and uc.courseId =a.courseId and uc.username =a.facultyId and c.programId =? group by a.id";
		}

		sql = sql + " order by createdDate desc";
		return findAllSQL(sql, new Object[] { username, programId });
		// , new Date() });
	}

	public List<Assignment> findPendingAssignmentCount(String username,
			String acadMonth, String acadYear) {

		String sql;

		sql = "SELECT c.id as courseId,  count(*) as count FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username =?  and sa.acadMonth = ? "
				+ " and sa.acadYear = ?  and a.startDate <= sysdate() and a.active = 'Y'	"
				+ "  and   sa.submissionStatus='N' group by c.courseName ";

		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public List<Assignment> findPendingAssignmentCount(String username) {

		String sql;

		sql = "SELECT c.id as courseId,  count(*) as count FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username =?   and a.startDate <= sysdate()	"
				+ "and a.active='Y'"
				+ "and c.active='Y'"
				+ "  and   sa.submissionStatus='N' and c.active='Y' group by c.courseName ";

		return findAllSQL(sql, new Object[] { username });
	}

	public List<Assignment> findByUserPendingViaCourse(String username,
			String acadMonth, String acadYear, boolean isFaculty,
			String courseId) {
		String sql = "";
		if (!isFaculty) {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id  "
					+ " where  a.courseId = ?  and sa.username = ? "
					+ " and sa.acadMonth = ? " + " and sa.acadYear = ? " // +
																			// " and a.startDate <= ? "
					+ " and " + "sa.submissionStatus='N'";
		} else {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where  a.courseId = ? and  a.facultyId = ? "
					+ " and sa.acadMonth = ? " + " and sa.acadYear = ? " // +
																			// " and a.startDate <= ? "
					+ " and " + "sa.evaluationStatus='N'";
		}

		sql = sql + " order by endDate desc";
		return findAllSQL(sql, new Object[] { courseId, username, acadMonth,
				acadYear });
		// , new Date() });
	}

	public List<Assignment> findByUserPendingViaCourse(String username,
			boolean isFaculty, String courseId) {
		String sql = "";
		if (!isFaculty) {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id  "
					+ " where  a.courseId = ?  and sa.username = ? "
					+ "  " // +
							// " and a.startDate <= ? "
					+ "and a.active='Y'" + "and c.active='Y'" + " and "
					+ "sa.submissionStatus='N' and c.active='Y'";
		} else {
			sql = "SELECT a.*, c.courseName FROM assignment a "
					+ " inner join student_assignment sa on sa.assignmentid = a.id "
					+ " inner join course c on a.courseId = c.id "
					+ " where  a.courseId = ? and  a.facultyId = ? "
					+ "  " // +
							// " and a.startDate <= ? "
					+ "and a.active='Y'" + "and c.active='Y'" + " and "
					+ "sa.evaluationStatus='N' and c.active='Y' ";
		}

		sql = sql + " order by endDate desc";
		return findAllSQL(sql, new Object[] { courseId, username });
		// , new Date() });
	}

	public List<Assignment> findByUserAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		Date dt = Utils.getInIST();
		final String sql = "SELECT distinct a.*,sa.submissionStatus, c.courseName FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? "
				+ " and a.courseId = ? "
				+ " and sa.acadMonth = ? "
				+ " and sa.acadYear = ? "
				+ " and a.startDate <= ? " + " order by endDate desc";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth,
				acadYear, dt });
	}

	public List<Assignment> findByUserAndCourseActive(String username,
			Long courseId) {
		Date dt = Utils.getInIST();
		final String sql = "SELECT distinct a.*,sa.submissionStatus, c.courseName, sa.approvalStatus, sa.attempts, sa.submissionDate, sa.isSubmitterInGroup, sa.evaluationStatus, sa.id as studentAssignmentId,sa.isAcceptDisclaimer,sa.remarks,sa.lowScoreReason "
				+ " FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? "
				+ " and a.courseId = ? and c.active='Y' "
				+ "and a.active='Y'"
				+ " order by endDate ";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<Assignment> findByCourse(Long courseId, String acadMonth,
			String acadYear) {
		final String sql = "SELECT a.*, c.courseName FROM assignment a "
				+ " inner join course c on a.courseId = c.id "
				+ " where a.courseId = ? " + " and a.acadMonth = ? "
				+ " and a.acadYear = ? " + " order by assignmentName";
		return findAllSQL(sql, new Object[] { courseId, acadMonth, acadYear });
	}

	public List<Assignment> findByCourse(Long courseId) {
		final String sql = "SELECT a.*, c.courseName FROM assignment a "
				+ " inner join course c on a.courseId = c.id "
				+ " where a.courseId = ? " + "and a.active='Y'"
				+ " and c.active='Y' "

				+ " " + " order by assignmentName";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<Assignment> findByFaculty(String username, String acadMonth,
			String acadYear) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId = ? " + " and a.acadMonth = ? "
				+ " and a.acadYear = ? " + " order by endDate";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public List<Assignment> findByFaculty(String username) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId = ? " + " and a.active = 'Y'  "
				+ " order by endDate";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<Assignment> findByFacultyAndCourse(String username,
			Long courseId, String acadMonth, String acadYear) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId = ? " + " and a.courseId = ? "
				+ " and a.acadMonth = ? " + " and a.acadYear = ? "
				+ " order by endDate desc";
		return findAllSQL(sql, new Object[] { username, courseId, acadMonth,
				acadYear });
	}

	public List<Assignment> findByFacultyAndCourseActive(String username,
			Long courseId) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId = ? " + " and a.courseId = ? "
				+ " and a.active ='Y' " + " order by endDate desc";
		return findAllSQL(sql, new Object[] { username, courseId });
	}

	public List<Assignment> findAllAssignmentsByFaculty(String username,
			String acadMonth, String acadYear) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId = ? "
				+ " and a.acadMonth = ? and a.active ='Y' "
				+ " and a.acadYear = ? " + " order by endDate desc";
		return findAllSQL(sql, new Object[] { username, acadMonth, acadYear });
	}

	public List<Assignment> findAllAssignmentsByFaculty(String username) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId = ?  and  a.active='Y' "
				+ " order by endDate desc";
		return findAllSQL(sql, new Object[] { username });
	}

	public Page<Assignment> getAssignmentBasedOnGroups(Assignment assignment,
			int pageNo, int pageSize, Long groupId) {
		String sql = " select * from assignment la,groups g where "
				+ "g.courseId=la.courseId and g.id=? and g.active='Y' and la.active='Y'";
		// + " la.id = lsa.assignmentId and lsa.groupId = ? ";

		String countSql = " select count(*) from assignment la,groups g where "
				+ "g.courseId=la.courseId and g.id=? and g.active='Y' and la.active='Y'";

		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(groupId);

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);
	}

	public List<Assignment> getGroupsForAssignment(Long assignmentId,
			Long courseId, String acadMonth, String acadYear) {
		String sql = "select distinct g.id as groupId, g.groupName, sa.assignmentId, sa.id "
				+ " from groups g"
				+ "		inner join assignment a on a.courseId = g.courseId"
				+ "		left outer join student_assignment sa on sa.groupId = g.id and sa.courseId = g.courseId and sa.assignmentId = ? "
				+ " where"
				+ " g.courseId = ? and g.acadMonth = ? and g.acadYear = ? and g.active='Y' and a.active='Y' order by sa.id asc ";
		return findAllSQL(sql, new Object[] { assignmentId, courseId,
				acadMonth, acadYear });

	}

	public void softDeleteAssignment(Integer id) {
		final String sql = "update assignment set active = 'N' " + " where id = ? ";
		getJdbcTemplate().update(sql, new Object[] { id });

	}

	public List<Assignment> getAssignmentBasedOnCourse(Long courseId) {
		String sql = "select * from assignment a where a.courseId= ? and a.active='Y' ";
		return findAllSQL(sql, new Object[] { courseId });

	}

	public List<Assignment> findAllFacultyByAssignment(Long assignmentId) {
		final String sql = "select * from assignment a where a.id= ? and a.active='Y' ";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

	public void updateFacultyAssigned(String createdBy, String facultyId) {
		executeUpdateSql(
				"Update assignment a set a.facultyId = ? where a.createdBy=? and a.active='Y'  ",
				new Object[] { createdBy, facultyId });

	}

	public void updateInactiveAssignment(Long id) {
		executeUpdateSql("Update assignment set active = 'N' where id= ?",
				new Object[] { id });
	}

	public void updateInactiveAssignmentByCourseId(Long courseId) {
		executeUpdateSql(
				"Update assignment set active = 'N' where courseId= ?",
				new Object[] { courseId });
	}

	public List<Assignment> getAllAssignments(Long courseId, String username) {
		final String sql = " select a.maxScore , a.assignmentName , a.assignmentType , sa.score , wd.wieghtageassigned "
				+ " from assignment a , student_assignment sa , wieghtagedata wd"
				+ " where a.courseId=? and sa.username = ? and a.id=sa.assignmentId and wd.wieghtagetype=a.assignmentType and a.active='Y'";
		return findAllSQL(sql, new Object[] { courseId, username });
	}

	public Page<Assignment> searchActiveByExactMatchReplacement(Long programId,
			int pageNo, int pageSize) {

		String sql = " select a.* from "
				+ getTableName()
				+ " a,course c , program p where a.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and a.active ='Y' order by a.createdDate desc";

		String sql2 = " select count(*) from "
				+ getTableName()
				+ " a,course c , program p where a.courseId= c.id and c.programId = p.id and "
				+ " p.id= ? and a.active ='Y' ";

		return paginationHelper.fetchPage(getJdbcTemplate(), sql2, sql,
				new Object[] { programId }, pageNo, pageSize,
				BeanPropertyRowMapper.newInstance(genericType));

	}

	public List<Assignment> findAssignmentsBySessionAndYearForProgram(

	String acadSession, Integer acadYear, Long programId) {

		String sql = "select a.* from assignment a "

				+ " inner join course c on c.id = a.courseId "

				+ " inner join program p on p.id = c.programId "

				+ " where c.acadSession = ? and c.acadYear = ? and programId = ?  and a.active ='Y' ";

		return findAllSQL(sql,

		new Object[] { acadSession, acadYear, programId });

	}

	public List<Assignment> findAssignmentsBySessionAndYearForCollege(

	String acadSession, Integer acadYear) {

		String sql = "select a.* from assignment a "

		+ " inner join course c on c.id = a.courseId "

		+ " inner join program p on p.id = c.programId "

		+ " where c.acadSession = ? and c.acadYear = ? and a.active ='Y'  ";

		return findAllSQL(sql, new Object[] { acadSession, acadYear });

	}

	public Assignment getAssignmentSummaryById(String username) {
		final String sql = " SELECT Count(case when sa.submissionStatus = 'Y' then 1 end) as completed, "
				+ " Count(case when (sa.submissionStatus = 'N' and (sa.approvalStatus IS NULL OR sa.approvalStatus <> 'Reject') and sa.attempts = 0) then 1 end) as pending, "
				+ " Count(case when sa.submissionDate > sa.endDate then 1 end) as lateSubmitted, "
				+ " Count(case when sa.approvalStatus = 'Reject' then 1 end) as rejected FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " inner join users u on sa.username = u.username and c.acadSession = u.acadSession "
				+ " where sa.username = ? and a.startDate <= ? "
				+ " and a.active='Y' and c.active='Y' "
				+ " group by sa.username ";

		Date dt = Utils.getInIST();
		return findOneSQL(sql, new Object[] { username, dt });
	}

	public Assignment getAssignmentSummaryByIdAndSem(String username,
			String acadSession) {
		final String sql = " SELECT Count(case when sa.submissionStatus = 'Y' then 1 end) as completed, "
				+ " Count(case when (sa.submissionStatus = 'N' and (sa.approvalStatus IS NULL OR sa.approvalStatus <> 'Reject') and sa.attempts = 0) then 1 end) as pending, "
				+ " Count(case when sa.submissionDate > sa.endDate then 1 end) as lateSubmitted, "
				+ " Count(case when sa.approvalStatus = 'Reject' then 1 end) as rejected FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? and c.acadSession = ? and a.startDate <= ? "
				+ " and a.active='Y' and c.active='Y' "
				+ " group by sa.username ";

		Date dt = Utils.getInIST();
		return findOneSQL(sql, new Object[] { username, acadSession, dt });
	}

	public Assignment getAssignmentSummaryByIdAndSemAndCourse(String username,
			String acadSession, String cid) {
		final String sql = " SELECT Count(case when sa.submissionStatus = 'Y' then 1 end) as completed, "
				+ " Count(case when (sa.submissionStatus = 'N' and (sa.approvalStatus IS NULL OR sa.approvalStatus <> 'Reject') and sa.attempts = 0) then 1 end) as pending, "
				+ " Count(case when sa.submissionDate > sa.endDate then 1 end) as lateSubmitted, "
				+ " Count(case when sa.approvalStatus = 'Reject' then 1 end) as rejected FROM assignment a "
				+ " inner join student_assignment sa on sa.assignmentid = a.id "
				+ " inner join course c on a.courseId = c.id "
				+ " where sa.username = ? and c.acadSession = ? and a.startDate <= ? and c.id = ? "
				+ " and a.active='Y' and c.active='Y' "
				+ " group by sa.username ";

		Date dt = Utils.getInIST();
		return findOneSQL(sql, new Object[] { username, acadSession, dt, cid });
	}
	
	public List<Long> getIdByParentModuleId(Long parentModuleId){
		String sql = "select id from assignment where parentModuleId = ?";
		return getJdbcTemplate().queryForList(sql, new Object[] { parentModuleId },
				Long.class); 
	}
	
public Assignment findIdByCourseIdAndParentModuleId(String courseId, String parentModuleId){
		String sql = "select * from assignment where courseId = ? and parentModuleId = ?";
		return findOneSQL(sql, new Object[]{courseId, parentModuleId});
	}	
	
	public List<Assignment> getChildListByParentModuleId(Long id){
		String sql = "select * from assignment where parentModuleId = ?";
		
		return findAllSQL(sql, new Object[] { id });
	}
	
	public List<Assignment> searchAssignmentsForFacultyByModule(String username,
			Long programId) {
		String sql = " select a.* from "
				+ " assignment a,course c , user_course uc ,program p where a.moduleId= c.moduleId and c.programId = p.id and uc.username = a.facultyId and "
				+ " uc.username=? and a.parentModuleId is null and "
				+ " p.id= ? and a.active ='Y' and c.active ='Y' group by a.id order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { username, programId });
	}
	public List<Assignment> searchAssignmentsForFacultyByModule(String username,
			Long programId, Long moduleId) {
		String sql = " select a.* from "
				+ " assignment a,course c , user_course uc ,program p where a.moduleId= c.moduleId and c.programId = p.id and uc.username = a.facultyId and "
				+ " uc.username=? and "
				+ " p.id= ? and a.active ='Y' and c.active ='Y' and a.moduleId = ? and a.courseId is null group by a.id order by a.createdDate desc";

		return findAllSQL(sql, new Object[] { username, programId, moduleId });
	}

	public void softDeleteAssignmentForModule(Long id) {
		final String sql = "update assignment set active='N' where id=? ";
		getJdbcTemplate().update(sql, new Object[] { id });

	}
	public List<Assignment> findAllAssigmentForFaculty(String acadMonth,
			String acadYear, String facultyId) {
		String sql = "select a.* from assignment a "
				+ " where a.acadMonth = ? and a.acadYear = ? and a.facultyId = ? and parentModuleId is null";
		return findAllSQL(sql, new Object[] { acadMonth, acadYear, facultyId });

	}
	public List<Assignment> findByFacultyAndModuleActive_new(String username,
			Long moduleId) {
		final String sql = "SELECT a.* FROM assignment a "
				+ " where a.facultyId in (select uc.username from user_course uc,course c where uc.courseId= c.id and c.moduleId = ?)"
				+ " and a.moduleId = ?  and a.active ='Y'"
				+ " and (a.rightGrant ='Y' or a.facultyId = ?) and parentModuleId is null"
				+ " order by endDate desc";

		return findAllSQL(sql, new Object[] { moduleId, moduleId, username });
	}
	
	public List<Assignment> searchAssignmentsForAdmin(Long programId) {
		String sql = " select a.* from assignment a,course c , program p "
				+ " where a.courseId= c.id and c.programId = p.id and p.id= ? "
				+ " and a.active ='Y' and c.active ='Y' and a.createdByAdmin = 'Y' "
				+ " and a.parentModuleId is null and a.moduleId is null group by a.id "
				+ " order by a.createdDate desc ";

		return findAllSQL(sql, new Object[] { programId });
	}
	
	public List<Assignment> searchAssignmentsForAdmin(Long programId, Long courseId) {
		String sql = " select a.* from assignment a,course c , program p "
				+ " where a.courseId= c.id and c.programId = p.id and  p.id= ? and "
				+ " a.courseId= ? and a.active ='Y' and a.createdByAdmin = 'Y' and "
				+ " a.parentModuleId is null and a.moduleId is null group by a.id "
				+ " order by a.createdDate desc ";

		return findAllSQL(sql, new Object[] { programId, courseId });
	}

	public List<Assignment> getAllAssignmentCount(String startDate, String endDate) {
		if(startDate.isEmpty() && endDate.isEmpty())
		{
			String sql=" SELECT  a.*,  CONCAT(u.username,'(',u.firstname,' ',u.lastname,')') AS facultyName,c.courseName, " + 
					" (SELECT COUNT(sa.id) from assignment aa, " + 
					"  student_assignment sa, users u" + 
					" WHERE  aa.id = a.id" + 
					" AND aa.id =sa.assignmentId AND aa.active = 'Y' and sa.active = 'Y' and u.username = sa.username and u.enabled = 1 )  AS countOverall," + 
					" ( SELECT COUNT(sa.id) from assignment ab," + 
					" student_assignment sa, users u" + 
					" WHERE ab.id = a.id " + 
					" AND ab.id =sa.assignmentId AND ab.active = 'Y' and sa.active = 'Y' and sa.submissionStatus = 'Y' and u.username = sa.username and u.enabled = 1 ) AS completedCount, " + 
					" ( SELECT COUNT(sa.id) from assignment ac, " + 
					" student_assignment sa, users u " + 
					" WHERE  ac.id = a.id " + 
					" AND ac.id =sa.assignmentId AND ac.active = 'Y' and sa.active = 'Y' AND "  + 
					" sa.submissionStatus = 'N' AND submissionDate IS NOT NULL  and u.username = sa.username and u.enabled = 1) AS lateSubmittedCount " + 
					" from assignment a, users u, course c " + 
					" WHERE a.facultyId = u.username AND a.courseId = c.id AND a.active = 'Y' " +
					" and  a.startDate <= NOW() AND a.endDate>= NOW()";
			return findAllSQL(sql, new Object[] { });
		}else if(!startDate.isEmpty() && !endDate.isEmpty()) {
		String sql=" SELECT  a.*,  CONCAT(u.username,'(',u.firstname,' ',u.lastname,')') AS facultyName,c.courseName, " + 
				" (SELECT COUNT(sa.id) from assignment aa, " + 
				"  student_assignment sa, users u " + 
				" WHERE  aa.id = a.id" + 
				" AND aa.id =sa.assignmentId AND aa.active = 'Y' and sa.active = 'Y'  and u.username = sa.username and u.enabled = 1 )  AS countOverall," + 
				" ( SELECT COUNT(sa.id) from assignment ab," + 
				" student_assignment sa, users u " + 
				" WHERE ab.id = a.id " + 
				" AND ab.id =sa.assignmentId AND ab.active = 'Y' and sa.active = 'Y' and sa.submissionStatus = 'Y'  and u.username = sa.username and u.enabled = 1 ) AS completedCount, " + 
				" ( SELECT COUNT(sa.id) from assignment ac, " + 
				" student_assignment sa , users u " + 
				" WHERE  ac.id = a.id " + 
				" AND ac.id =sa.assignmentId AND ac.active = 'Y' and sa.active = 'Y' AND "  + 
				" sa.submissionStatus = 'N' AND submissionDate IS NOT NULL  and u.username = sa.username and u.enabled = 1) AS lateSubmittedCount " + 
				" from assignment a, users u, course c " + 
				" WHERE a.facultyId = u.username AND a.courseId = c.id AND a.active = 'Y' "
				+ " and a.startDate between ? AND ?";
		return findAllSQL(sql, new Object[] { startDate,endDate });	
		} else {
			return null;
		}
		}
	public List<Assignment> getCurrentAllAssignmentCount() {
		String sql=" SELECT  a.*,  CONCAT(u.username,'(',u.firstname,' ',u.lastname,')') AS facultyName,c.courseName, " + 
				" (SELECT COUNT(sa.id) from assignment aa, " + 
				"  student_assignment sa, users u" + 
				" WHERE  aa.id = a.id" + 
				" AND aa.id =sa.assignmentId AND aa.active = 'Y' and sa.active = 'Y'  and u.username = sa.username and u.enabled = 1 )  AS countOverall," + 
				" ( SELECT COUNT(sa.id) from assignment ab," + 
				" student_assignment sa, users u" + 
				" WHERE ab.id = a.id " + 
				" AND ab.id =sa.assignmentId AND ab.active = 'Y' and sa.active = 'Y' and sa.submissionStatus = 'Y'  and u.username = sa.username and u.enabled = 1) AS completedCount, " + 
				" ( SELECT COUNT(sa.id) from assignment ac, " + 
				" student_assignment sa , users u" + 
				" WHERE  ac.id = a.id " + 
				" AND ac.id =sa.assignmentId AND ac.active = 'Y' and sa.active = 'Y' AND "  + 
				" sa.submissionStatus = 'N' AND submissionDate IS NOT NULL and u.username = sa.username and u.enabled = 1) AS lateSubmittedCount " + 
				" from assignment a, users u, course c " + 
				" WHERE a.facultyId = u.username AND a.courseId = c.id AND a.active = 'Y' " +
				" and  a.startDate <= NOW() AND a.endDate>= NOW() ";
			
		return findAllSQL(sql, new Object[] { });
	}
	
	public List<Assignment> getLateSubmittedStudentsByAssignId(String id){
//		String sqla=" select distinct u.username, concat(u.firstname, ' ', u.lastname) as studentName, u.rollNo, sa.submissionDate "
	//			+ " from student_assignment sa, users u where sa.username = u.username and sa.active = 'Y' and sa.assignmentId = ? ";

		String sql="SELECT distinct u.username, concat(u.firstname, ' ', u.lastname) as studentName, u.rollNo, sa.submissionDate  , sa.* from assignment ac, " + 
				"	student_assignment sa,users u   " + 
				"	WHERE  u.username=sa.username and ac.id = ? " + 
				"	AND ac.id =sa.assignmentId AND ac.active = 'Y' and sa.active = 'Y' AND   " + 
				"	sa.submissionStatus = 'N' AND sa.submissionDate IS NOT NULL and u.enabled =1";
		
		
		return findAllSQL(sql, new Object[] { id });
	}

	/* For Assignment Pool Start */
	public int chkStartDateForUpdate(Long assignmentId) {
		final String sql = "select count(*) from " + getTableName()
				+ " where startDate>=sysdate() and id = ?";
		return getJdbcTemplate().queryForObject(sql, Integer.class,
				new Object[] { assignmentId });
	}
	/* For Assignment Pool End */
	
	
	public List<Assignment> getAssignmentsForTeeModule(String facultyId,String moduleId, String acadYear, String courseId) {
		if (courseId != null) {
			String sql = "select a.*,c.courseName,c.id as courseId FROM assignment a,course c where a.courseId=c.id and a.facultyId =? "
					+ " and c.moduleId=? and c.acadYearCode=? and createdByAdmin='Y' and c.id =? and a.active='Y' and c.active='Y' "
					+ " group by a.id ";
			return findAllSQL(sql, new Object[] { facultyId,moduleId, acadYear, courseId });
		} else {
			String sql = "select a.*,c.courseName,c.id as courseId FROM assignment a,course c where a.courseId=c.id and a.facultyId =? "
					 + " and c.moduleId=? and c.acadYearCode=? and  createdByAdmin='Y' and a.active='Y' and c.active='Y' "
					+ " group by a.id ";
			return findAllSQL(sql, new Object[] {facultyId, moduleId, acadYear });
		}

	}
public List<Assignment> getCoursesForAssignmentTee(String moduleId, String acadYear, String facultyId,String courseIdDivWise) {

		if(courseIdDivWise==null) {
		String sql = "select  c.id as courseId,c.courseName FROM assignment a,course c where a.courseId=c.id "
				+ " and c.moduleId=? and c.acadYearCode=? and a.facultyId=? and createdByAdmin='Y' and a.active='Y' and c.active='Y' "
				+ " group by a.courseId ";
		return findAllSQL(sql, new Object[] { moduleId, acadYear, facultyId });
		}else {
			String sql = "select  c.id as courseId,c.courseName FROM assignment a,course c where a.courseId=c.id "
					+ " and c.moduleId=? and c.acadYearCode=? and a.facultyId=? and createdByAdmin='Y'"
					+ "  and c.id=? and a.active='Y' and c.active='Y' "
					+ " group by a.courseId ";
			return findAllSQL(sql, new Object[] { moduleId, acadYear, facultyId,courseIdDivWise });
		}

	}
public List<Assignment> getAssignmentsByIds(List<String> assignmentIds) {
	Map<String, Object> mapper = new HashMap<>();
	mapper.put("assignmentIds", assignmentIds);
	String sql = " select distinct a.maxScore from assignment a where a.id in(:assignmentIds) " + " and a.active='Y'  ";

	return getNamedParameterJdbcTemplate().query(sql, mapper, BeanPropertyRowMapper.newInstance(Assignment.class));
}

	public List<Assignment> findAllAssignmentWithCourseIdsForAdmin(
			List<String> courseIds) {
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseIds", courseIds);
	
		final String sql = " select a.* from assignment a, assignment_configuration ac where a.id = ac.assignmentId and a.courseId IN (:courseIds)"
				+ " and a.active = 'Y' and ac.active = 'Y' group by a.id ";
		return getNamedParameterJdbcTemplate().query(sql, params,
				BeanPropertyRowMapper.newInstance(Assignment.class));
	}
	
	public List<Assignment> findAssignmentReportListWithAssignmentIdForAdmin(
			String assignmentId) {
	
		String sql = " select a.id, a.assignmentName, c.programId, p.programName, a.courseId, c.courseName, c.acadSession, "
				+ " concat(c.moduleAbbr, '[', c.moduleName, ']') as moduleName, "
				+ " concat(u1.firstname, ' ', u1.lastname) as studentName, "
				+ " concat(u2.firstname, ' ', u2.lastname) as facultyName, "
				+ " a.acadYear, a.acadMonth, a.facultyId, a.maxScore, a.createdByAdmin, ac.questionNumber, "
				+ " ac.marks as totalMarksPerQues, sa.username, sam.marks, sa.evaluationStatus, sa.score "
				+ " from assignment a, student_assignment sa, assignment_configuration ac, "
				+ " student_assignment_questionwise_marks sam, course c, program p, users u1, users u2 "
				+ " where a.id = sa.assignmentId and sa.assignmentId = ac.assignmentId and a.id = sam.assignmentId "
				+ " and sa.username = sam.username and sa.username = u1.username and a.facultyId = u2.username "
				+ " and ac.id = sam.assignConfigId and a.courseId = c.id and c.programId = p.id and "
				+ " a.id = ? and a.active = 'Y' and ac.active = 'Y' and sa.active = 'Y' "
				+ " order by sa.username, ac.id ";
		return findAllSQL(sql, new Object[] { assignmentId });
	}

}
