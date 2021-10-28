package com.spts.lms.services.course;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.user.UserCourseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("userCourseService")
@Transactional
public class UserCourseService extends BaseService<UserCourse> {

	@Autowired
	private UserCourseDAO userCourseDAO;

	@Override
	public BaseDAO<UserCourse> getDAO() {
		return userCourseDAO;
	}

	public List<UserCourse> findFacultyByCourseID(Long courseId) {
		return userCourseDAO.findFacultyByCourseID(courseId);
	}

	public List<UserCourse> findByUsername(String uname) {
		return userCourseDAO.findByUsername(uname);
	}

	public List<UserCourse> findFacultyByCourseID(Long courseId, String acadMonth, Integer acadYear) {
		return userCourseDAO.findFacultyByCourseID(courseId, acadMonth, acadYear);
	}

	public List<UserCourse> findStudentsForFaculty(Long courseId) {
		return userCourseDAO.findStudentsForFaculty(courseId);
	}

	public List<Map<String, Object>> getNumberOfStudentsPerCourse() {
		return userCourseDAO.getNumberOfStudentsPerCourse();
	}

	public List<UserCourse> getUserbasedOnMultipleCourse(String role, List<String> courseId) {
		return userCourseDAO.getUserbasedOnMultipleCourse(role, courseId);
	}

	public void makeInActive(Long courseId, String username) {
		userCourseDAO.makeInActive(courseId, username);
	}

	public List<UserCourse> findFacultyByAcadMonthAndAcadYear(String acadMonth, Integer acadYear) {
		UserCourse userCourse = new UserCourse();
		userCourse.setAcadMonth(acadMonth);
		userCourse.setAcadYear(acadYear);
		userCourse.setRole(Role.ROLE_FACULTY);
		return userCourseDAO.searchByExactMatch(userCourse, 1, 0).getPageItems();
	}

	public List<UserCourse> findAllFacultyWithCourseId(Long courseId) {
		return userCourseDAO.findAllFacultyWithCourseId(courseId);
	}

	public List<UserCourse> findAllFacultyWithCourseId(Long courseId, String acadMonth, Integer acadYear) {
		return userCourseDAO.findAllFacultyWithCourseId(courseId, acadMonth, acadYear);
	}

	public List<UserCourse> findAllAcadSessionCourse() {
		return userCourseDAO.findAllAcadSessionCourse();
	}

	public List<UserCourse> getUsersBasedOnCourse(Long courseId, String role) {
		return userCourseDAO.getUsersBasedOnCourse(courseId, role);
	}

	public List<UserCourse> findAllAcadSessionsWithAcadYear(Long acadYear) {
		return userCourseDAO.findAllAcadSessionsWithAcadYear(acadYear);
	}

	public List<UserCourse> findAllAcadSessionsWithAcadYear(Long acadYear, String programId) {
		return userCourseDAO.findAllAcadSessionsWithAcadYear(acadYear, programId);
	}

	public List<UserCourse> getStudentCourseList() {
		return userCourseDAO.getStudentCourseList();
	}

	public List<String> findAllAcadSessionsWithProgramId(String programId) {
		return userCourseDAO.findAllAcadSessionsForProgramId(programId);
	}

	public List<UserCourse> getStudentsByProgram(String programId) {
		return userCourseDAO.getStudentsByProgram(programId);
	}

	public List<UserCourse> noofStudentsInCourseList(List<Long> CourseIdList) {
		return userCourseDAO.noofStudentsInCourseList(CourseIdList);
	}

	public List<UserCourse> getStudentsByCourseIds(List<String> courseIds) {
		return userCourseDAO.getStudentsByCourseIds(courseIds);
	}

	public List<UserCourse> getFacultiesByCourseIds(List<String> courseIds) {
		return userCourseDAO.getFacultiesByCourseIds(courseIds);
	}

	public List<UserCourse> findStudentsForFacultyWithCampusId(Long courseId, Long campusId) {
		return userCourseDAO.findStudentsForFacultyWithCampusId(courseId, campusId);
	}

	public List<String> findStudentUsernamesForFaculty(Long courseId) {
		return userCourseDAO.findStudentUsernamesForFaculty(courseId);
	}

	public List<UserCourse> noofStudentsInCourseList(Long courseId) {
		return userCourseDAO.noofStudentsInCourseList(courseId);
	}

	public List<UserCourse> noofStudentsInCourseList(String campus, Long courseId) {
		return userCourseDAO.noofStudentsInCourseList(campus, courseId);
	}

	public List<UserCourse> getUserbasedByAcadSessionAndYear(String role, List<String> acadSession, String acadYear) {
		return userCourseDAO.getUserbasedByAcadSessionAndYear(role, acadSession, acadYear);
	}

	public List<UserCourse> getUserbasedByAcadSessionAndYearAndCampus(String role, List<String> acadSession,
			String acadYear, String campusId) {
		return userCourseDAO.getUserbasedByAcadSessionAndYearAndCampus(role, acadSession, acadYear, campusId);
	}

	public List<String> findUserCourseList(String username) {
		return userCourseDAO.findUserCourseList(username);
	}

	public List<UserCourse> getStudentsByCourseId(String courseId) {
		return userCourseDAO.getStudentsByCourseId(courseId);
	}

	public boolean isNullOrEmpty(String str) {
		if (str != null && !str.isEmpty())
			return true;
		return false;
	}

	/*
	 * public Page<UserCourse> searchUserCourse(UserCourse uCourse, int pageNo,
	 * 
	 * int pageSize) {
	 * 
	 * String sql = "Select * from user_course  where 1 = 1 and active='Y' ";
	 * 
	 * String countSql =
	 * "Select count(*) from user_course where 1 = 1 and active='Y' ";
	 * 
	 * 
	 * 
	 * ArrayList<Object> parameters = new ArrayList<Object>();
	 * 
	 * 
	 * 
	 * if (uCourse.getCourseId() != null) {
	 * 
	 * sql = sql + " and courseId = ? ";
	 * 
	 * countSql = countSql + " and courseId = ? ";
	 * 
	 * parameters.add(uCourse.getCourseId());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * if (isNullOrEmpty(uCourse.getUsername())) {
	 * 
	 * sql = sql + " and username = ? ";
	 * 
	 * countSql = countSql + " and username = ? ";
	 * 
	 * parameters.add(uCourse.getUsername());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * if (isNullOrEmpty(uCourse.getAcadMonth())) {
	 * 
	 * sql = sql + " and acadMonth = ? ";
	 * 
	 * countSql = countSql + " and acadMonth = ? ";
	 * 
	 * parameters.add(uCourse.getAcadMonth());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * if (uCourse.getAcadYear() != null) {
	 * 
	 * sql = sql + " and acadYear = ? ";
	 * 
	 * countSql = countSql + " and acadYear = ? ";
	 * 
	 * parameters.add(uCourse.getAcadYear());
	 * 
	 * }
	 * 
	 * 
	 * 
	 * // sql = sql + " order by a.id desc ";
	 * 
	 * 
	 * 
	 * Object[] args = parameters.toArray();
	 * 
	 * 
	 * 
	 * return findAllSQL(sql, countSql, args, pageNo, pageSize);
	 * 
	 * }
	 */

	public Page<UserCourse> searchUserCourse(String programId, UserCourse uCourse, int pageNo,

			int pageSize) {

		String sql = "Select uc.*,c.courseName from user_course  uc,course c where   uc.active='Y' and uc.courseId=c.id and c.active='Y' ";

		String countSql = "Select count(*) from user_course uc,course c where uc.active='Y' and uc.courseId=c.id and c.active='Y' ";

		ArrayList<Object> parameters = new ArrayList<Object>();

		if (programId != null) {

			sql = sql + " and c.programId = ? ";

			countSql = countSql + " and c.programId = ? ";

			parameters.add(programId);

		}

		if (uCourse.getCourseId() != null) {

			sql = sql + " and uc.courseId = ? ";

			countSql = countSql + " and uc.courseId = ? ";

			parameters.add(uCourse.getCourseId());

		}

		if (uCourse.getAcadSession() != null) {

			sql = sql + " and uc.acadSession = ? ";

			countSql = countSql + " and uc.acadSession = ? ";

			parameters.add(uCourse.getAcadSession());

		}

		if (isNullOrEmpty(uCourse.getUsername())) {

			sql = sql + " and uc.username = ? ";

			countSql = countSql + " and uc.username = ? ";

			parameters.add(uCourse.getUsername());

		}

		if (isNullOrEmpty(uCourse.getAcadMonth())) {

			sql = sql + " and uc.acadMonth = ? ";

			countSql = countSql + " and uc.acadMonth = ? ";

			parameters.add(uCourse.getAcadMonth());

		}

		if (uCourse.getAcadYear() != null) {

			sql = sql + " and uc.acadYear = ? ";

			countSql = countSql + " and uc.acadYear = ? ";

			parameters.add(uCourse.getAcadYear());

		}

		// sql = sql + " order by a.id desc ";

		Object[] args = parameters.toArray();

		return findAllSQL(sql, countSql, args, pageNo, pageSize);

	}

	public void makeUserInActiveByUsername(String username) {

		userCourseDAO.makeUserInActiveByUsername(username);
	}

	public List<String> findAcadSessionList(String programId) {
		return userCourseDAO.findAcadSessionList(programId);
	}

	public void makeCourseActive(String courseId) {
		userCourseDAO.makeCourseActive(courseId);
	}

	public List<UserCourse> findFacultyByUsingProgramId(String ProgramId) {
		return userCourseDAO.findFacultyByUsingProgramId(ProgramId);
	}

	public List<UserCourse> findStudentsByUsingProgramId(String ProgramId) {
		return userCourseDAO.findStudentsByUsingProgramId(ProgramId);
	}

	public List<UserCourse> findAllFacultyWithModuleIdICA(String moduleId, String acadYear) {
		return userCourseDAO.findAllFacultyWithModuleIdICA(moduleId, acadYear);
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYear(String moduleId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId,String isEvalCompWise,String compId) {
		return userCourseDAO.findStudentByModuleIdAndAcadYear(moduleId, acadYear, acadSession, programId, icaId,
				campusId,isEvalCompWise,compId);
	}

	public List<UserCourse> findAllAcadSessionsWithProgramIds(List<String> programList) {
		return userCourseDAO.findAllAcadSessionsWithProgramIds(programList);
	}

	public List<UserCourse> getAllFacultiesDivisionWise(String acadYear, String acadSession, String moduleId,
			String programId, String campusId) {
		return userCourseDAO.getAllFacultiesDivisionWise(acadYear, acadSession, moduleId, programId, campusId);
	}

	public List<UserCourse> findStudentByEventIdAndAcadYear(String eventId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId,String isEvalCompWise,String compId) {
		return userCourseDAO.findStudentByEventIdAndAcadYear(eventId, acadYear, acadSession, programId, icaId,
				campusId,isEvalCompWise,compId);
	}

	public String getFacultiesByParam(String acadYear, String session, String moduleId, String programId) {

		return userCourseDAO.getFacultiesByParam(acadYear, session, moduleId, programId);
	}

	public String getFacultiesByParamForReport(String acadYear) {

		return userCourseDAO.getFacultiesByParamForReport(acadYear);
	}

	public UserCourse getMappingByUsernameAndCourse(String username, String courseId) {
		return userCourseDAO.getMappingByUsernameAndCourse(username, courseId);
	}
	
	public UserCourse getMappingByUsernameAndModule(String username, String moduleId) {
		return userCourseDAO.getMappingByUsernameAndModule(username, moduleId);
	}

	public List<String> findDistinctStudentByModuleIdAndAcadYear(String moduleId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId) {
		return userCourseDAO.findDistinctStudentByModuleIdAndAcadYear(moduleId, acadYear, acadSession, programId, icaId,
				campusId);
	}

	public List<String> findDistinctStudentByEventIdAndAcadYear(String courseId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId) {
		return userCourseDAO.findDistinctStudentByEventIdAndAcadYear(courseId, acadYear, acadSession, programId, icaId,
				campusId);
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForNonEventModule(String moduleId, String acadYear,
			String acadSession, String programId, Long icaId, String campusId,String componentId) {
		return userCourseDAO.findStudentByModuleIdAndAcadYearForNonEventModule(moduleId, acadYear, acadSession,
				programId, icaId, campusId,componentId);
	}

	public List<String> findDistinctStudentByModuleIdAndAcadYearForNonEventModule(String moduleId, String acadYear,
			String acadSession, String programId, Long icaId, String campusId) {
		return userCourseDAO.findDistinctStudentByModuleIdAndAcadYearForNonEventModule(moduleId, acadYear, acadSession,
				programId, icaId, campusId);
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForNCAEvents(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {
		return userCourseDAO.findStudentByModuleIdAndAcadYearForNCAEvents(moduleId, acadYear, acadSession, programId,
				campusId);
	}

	public List<String> findStudentNoByModuleIdAndAcadYearForNCAEvents(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {
		return userCourseDAO.findStudentNoByModuleIdAndAcadYearForNCAEvents(moduleId, acadYear, acadSession, programId,
				campusId);
	}

	public List<UserCourse> findStudentByStudentListAndIcaId(List<String> usernames, Long icaId,String isEvalCompWise,String compId) {
		return userCourseDAO.findStudentByStudentListAndIcaId(usernames, icaId,isEvalCompWise,compId);
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForBatch(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {
		return userCourseDAO.findStudentByModuleIdAndAcadYearForBatch(moduleId, acadYear, acadSession, programId,
				campusId);
	}

	public List<UserCourse> findFacultyNamesByIdsForBatch(List<String> facultyIds) {
		return userCourseDAO.findFacultyNamesByIdsForBatch(facultyIds);
	}

	public List<UserCourse> findAllFacultyByFacultyIds(String facultyIds) {
		return userCourseDAO.findAllFacultyByFacultyIds(facultyIds);
	}

	public String getFacultyByCourseIdForAssignment(long courseId) {
		return userCourseDAO.getFacultyByCourseIdForAssignment(courseId);
	}

	public List<UserCourse> findStudentsByYearMonthCourse(String acadYear, String acadMonth, String courseId) {
		return userCourseDAO.findStudentsByYearMonthCourse(acadYear, acadMonth, courseId);
	}

	public List<UserCourse> findFacultiesByYearMonthCourse(String acadYear, String acadMonth, String courseId) {
		return userCourseDAO.findFacultiesByYearMonthCourse(acadYear, acadMonth, courseId);
	}
	public List<UserCourse> findStudentsByYearMonthCourseNew(String acadYear, String acadMonth, String courseId) {
		return userCourseDAO.findStudentsByYearMonthCourseNew(acadYear, acadMonth, courseId);
	}

	public List<UserCourse> findFacultiesByYearMonthCourseNew(String acadYear, String acadMonth, String courseId) {
		return userCourseDAO.findFacultiesByYearMonthCourseNew(acadYear, acadMonth, courseId);
	}

	// New Queries for TEE

	public List<UserCourse> findStudentByModuleIdAndAcadYearForTEE(String moduleId, String acadYear, String acadSession,
			String programId, Long teeId, String campusId) {

		return userCourseDAO.findStudentByModuleIdAndAcadYearForTEE(moduleId, acadYear, acadSession, programId, teeId,
				campusId);
	}

	public List<String> findDistinctStudentByModuleIdAndAcadYearForTEE(String moduleId, String acadYear,
			String acadSession, String programId, Long teeId, String campusId) {

		return userCourseDAO.findDistinctStudentByModuleIdAndAcadYearForTEE(moduleId, acadYear, acadSession, programId,
				teeId, campusId);
	}

	public List<UserCourse> findStudentByStudentListAndTeeId(List<String> usernames, Long teeId) {
		return userCourseDAO.findStudentByStudentListAndTeeId(usernames, teeId);
	}

	public List<UserCourse> findStudentByEventIdAndAcadYearForTEE(String courseId, String acadYear, String acadSession,
			String programId, Long teeId, String campusId) {

		return userCourseDAO.findStudentByEventIdAndAcadYearForTEE(courseId, acadYear, acadSession, programId, teeId,
				campusId);
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearForBatchCE(String moduleId, String acadYear,
			String acadSession, String programId, String campusId) {
		return userCourseDAO.findStudentByModuleIdAndAcadYearForBatchCE(moduleId, acadYear, acadSession, programId,
				campusId);
	}

	public List<UserCourse> findStudentByModuleIdAndAcadYearCE(String moduleId, String acadYear, String acadSession,
			String programId, Long icaId, String campusId,String isEvalCompWise,String compId) {

		return userCourseDAO.findStudentByModuleIdAndAcadYearCE(moduleId, acadYear, acadSession, programId, icaId,
				campusId,isEvalCompWise,compId);
	}

	public List<UserCourse> findStudentByStudentListAndIcaIdCE(List<String> studentsBatchWise, Long icaId,
			String acadSession,String isEvalCompWise,String compId) {

		return userCourseDAO.findStudentByStudentListAndIcaIdCE(studentsBatchWise, icaId, acadSession,isEvalCompWise,compId);
	}

	public List<String> findAcadSessionListSupportAdmin() {
		return userCourseDAO.findAcadSessionListSupportAdmin();
	}

	public int makeInActiveBySupportAdmin(List<String> courseIdList, List<String> usernameList) {
		return userCourseDAO.makeInActiveBySupportAdmin(courseIdList, usernameList);

	}

	public List<String> findCoursesByParam(String moduleId, String programId, String acadYear, String username) {
		return userCourseDAO.findCoursesByParam(moduleId, programId, acadYear, username);
	}

	public List<UserCourse> getStudentsByModuleId(String moduleId, String acadYear) {
		return userCourseDAO.getStudentsByModuleId(moduleId, acadYear);
	}

	public List<UserCourse> findAllFacultyWithModuleIdTest(String moduleId, String acadYear, String campusId,
			String programId) {
		return userCourseDAO.findAllFacultyWithModuleIdTest(moduleId, acadYear, campusId, programId);
	}

	public List<UserCourse> findAllFacultyWithModuleAndProgram(String moduleId, String acadYear, String programId,
			String campusId) {
		return userCourseDAO.findAllFacultyWithModuleAndProgram(moduleId, acadYear, programId, campusId);
	}
	
	public String getCourseIdByModuleAndAcadYear(String username, String moduleId, String acadYear){
		return userCourseDAO.getCourseIdByModuleAndAcadYear(username,moduleId,acadYear);
	}
	
	public List<String> getFacultyByCourse(String courseId) {
		return userCourseDAO.getFacultyByCourse(courseId);
	}

	//Peter 22/10/2021
	public UserCourse getFacultyCourseId(String assignedFaculty, String moduleId) {
		return userCourseDAO.getFacultyCourseId(assignedFaculty,moduleId);
	}
}
