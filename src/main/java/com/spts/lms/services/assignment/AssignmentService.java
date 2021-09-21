package com.spts.lms.services.assignment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.assignment.StudentAssignmentDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.utils.Utils;

@Service("assignmentService")
@Transactional
public class AssignmentService extends BaseService<Assignment> {

	private static final Logger logger = Logger
			.getLogger(AssignmentService.class);
	
	@Autowired
	private AssignmentDAO assignmentDAO;

	@Autowired
	StudentAssignmentDAO studentAssigmentDAO;
	
	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Override
	public BaseDAO<Assignment> getDAO() {
		return assignmentDAO;
	}

	// need to add logic for current running cycle
	public List<Assignment> findAllAssignments(String userName) {
		String sql = "SELECT sa.*, a.*, c.courseName FROM student_assignment sa "
				+ " inner join assignment a on sa.assignmentid = a.id " + " inner join course c on sa.courseId = c.id "
				+ " where sa.username = ? ";

		return findAllSQL(sql, new Object[] { userName });
	}

	public List<Assignment> findAllAssignmentForHOD(Long programId) {
		return assignmentDAO.findAllAssignmentForHOD(programId);
	}

	public List<Assignment> findByFacultyAndCourseActive_new(String username, Long courseId) {
		return assignmentDAO.findByFacultyAndCourseActive_new(username, courseId);
	}

	public List<Assignment> findByUser(String username, String acadMonth, String acadYear) {
		return assignmentDAO.findByUser(username, acadMonth, acadYear);
	}

	public List<Assignment> findByUserPending(String username, boolean isFaculty) {
		return assignmentDAO.findByUserPending(username, isFaculty);
	}

	public List<Assignment> findByUser(String username) {
		return assignmentDAO.findByUser(username);
	}

	public List<Assignment> findByUserNew(String username) {
		return assignmentDAO.findByUserNew(username);
	}

	public void updateFacultyAssigned(String facultyId, Long assignmentId) {
		assignmentDAO.updateFacultyAssigned(facultyId, assignmentId);
	}

	public List<Assignment> findByUserPendingForParents(String username, String acadMonth, String acadYear) {
		return assignmentDAO.findByUserPendingForParents(username, acadMonth, acadYear);
	}

	public List<Assignment> searchAssignmentsForFaculty(String username, Long programId) {
		return assignmentDAO.searchAssignmentsForFaculty(username, programId);
	}

	public List<Assignment> searchAssignmentForHOD(Long programId) {
		return assignmentDAO.searchAssignmentForHOD(programId);
	}

	public List<Assignment> searchAssignmentsForFaculty(String username, Long programId, Long courseId) {
		return assignmentDAO.searchAssignmentsForFaculty(username, programId, courseId);
	}

	public List<Map<String, Object>> getAssignmentsWithAdvanceSearchByFaculty(String username) {
		return assignmentDAO.getAssignmentsWithAdvanceSearchByFaculty(username);
	}

	public void showResults(Long id) {
		assignmentDAO.showResults(id);
	}

	public List<Assignment> findAllAssignmentForFaculty(String acadMonth, String acadYear, String facultyId) {
		return assignmentDAO.findAllAssigmentForFaculty(acadMonth, acadYear, facultyId);
	}

	public List<Assignment> findAllAssignment(String acadMonth, String acadYear) {
		return assignmentDAO.findAllAssigment(acadMonth, acadYear);
	}

	public List<Assignment> findByUserPendingForParents(String username) {
		return assignmentDAO.findByUserPendingForParents(username);
	}

	public List<Assignment> findByUserPending(String username, boolean isFaculty, String acadMonth, String acadYear) {
		return assignmentDAO.findByUserPending(username, acadMonth, acadYear, isFaculty);
	}

	public List<Assignment> findByUserPending(String username, boolean isFaculty, Long programId) {
		return assignmentDAO.findByUserPending(username, isFaculty, programId);
	}

	public List<Assignment> findPendingAssignmentCount(String username, String acadMonth, String acadYear) {

		return assignmentDAO.findPendingAssignmentCount(username, acadMonth, acadYear);

	}

	public List<Assignment> findPendingAssignmentCount(String username) {

		return assignmentDAO.findPendingAssignmentCount(username);

	}

	public List<Assignment> findByUserPendingByCourse(String username, boolean isFaculty, String courseId,
			String acadMonth, String acadYear) {
		return assignmentDAO.findByUserPendingViaCourse(username, acadMonth, acadYear, isFaculty, courseId);
	}

	public List<Assignment> findByUserPendingByCourse(String username, boolean isFaculty, String courseId) {
		return assignmentDAO.findByUserPendingViaCourse(username, isFaculty, courseId);
	}

	public List<Assignment> findByUserAndCourse(String username, Long courseId, String acadMonth, String acadYear) {
		return assignmentDAO.findByUserAndCourse(username, courseId, acadMonth, acadYear);
	}

	public List<Assignment> findByUserAndCourse(String username, Long courseId) {
		return assignmentDAO.findByUserAndCourseActive(username, courseId);
	}

	public List<Assignment> findByCourse(Long courseId, String acadMonth, String acadYear) {
		return assignmentDAO.findByCourse(courseId, acadMonth, acadYear);
	}

	public List<Assignment> findByCourse(Long courseId) {
		return assignmentDAO.findByCourse(courseId);
	}

	@Async
	public Future<List<Assignment>> findByCourseAsync(Long courseId, String acadMonth, String acadYear) {
		return new AsyncResult<List<Assignment>>(assignmentDAO.findByCourse(courseId, acadMonth, acadYear));
	}

	public List<Assignment> findByFaculty(String username, String acadMonth, String acadYear) {
		return assignmentDAO.findByFaculty(username, acadMonth, acadYear);
	}

	public List<Assignment> findByFaculty(String username) {
		return assignmentDAO.findByFaculty(username);
	}

	public List<Assignment> findByFacultyAndCourse(String username, Long courseId, String acadMonth, String acadYear) {
		return assignmentDAO.findByFacultyAndCourse(username, courseId, acadMonth, acadYear);
	}

	public List<Assignment> findByFacultyAndCourseActive(String username, Long courseId) {
		return assignmentDAO.findByFacultyAndCourseActive(username, courseId);
	}

	public List<Assignment> findAllAssignmentsByFaculty(String username, String acadMonth, String acadYear) {
		return assignmentDAO.findAllAssignmentsByFaculty(username, acadMonth, acadYear);
	}

	public List<Assignment> findAllAssignmentsByFaculty(String username) {
		return assignmentDAO.findAllAssignmentsByFaculty(username);
	}

	public void softDeleteAssignment(Integer id) {
		studentAssigmentDAO.deleteStudentAssigment(id);
		assignmentDAO.softDeleteAssignment(id);

	}

	public Page<Assignment> getAssignmentBasedOnGroups(Assignment assignment, int pageNo, int pageSize, Long groupId) {
		return assignmentDAO.getAssignmentBasedOnGroups(assignment, pageNo, pageSize, groupId);
	}

	public List<Assignment> getAssignmentBasedOnCourse(Long courseId) {
		return assignmentDAO.getAssignmentBasedOnCourse(courseId);
	}

	public List<Assignment> findAllFacultyByAssignment(Long assignmentId) {
		return assignmentDAO.findAllFacultyByAssignment(assignmentId);
	}

	public List<Assignment> getAllAssignments(Long courseId, String username) {
		return assignmentDAO.getAllAssignments(courseId, username);
	}

	public Page<Assignment> searchActiveByExactMatchReplacement(Long programId, int pageNo, int pageSize) {
		return assignmentDAO.searchActiveByExactMatchReplacement(programId, pageNo, pageSize);
	}

	public List<Assignment> findAssignmentsBySessionAndYearForProgram(

			String acadSession, Integer acadYear, Long programId) {

		return assignmentDAO.findAssignmentsBySessionAndYearForProgram(

				acadSession, acadYear, programId);

	}

	public List<Assignment> findAssignmentsBySessionAndYearForCollege(

			String acadSession, Integer acadYear) {

		return assignmentDAO.findAssignmentsBySessionAndYearForCollege(

				acadSession, acadYear);

	}

	public Assignment getAssignmentSummaryById(String username) {
		return assignmentDAO.getAssignmentSummaryById(username);
	}

	public Assignment getAssignmentSummaryByIdAndSem(String username, String acadSession) {
		return assignmentDAO.getAssignmentSummaryByIdAndSem(username, acadSession);
	}

	public Assignment getAssignmentSummaryByIdAndSemAndCourse(String username, String acadSession, String cid) {
		return assignmentDAO.getAssignmentSummaryByIdAndSemAndCourse(username, acadSession, cid);
	}

	public List<Long> getIdByParentModuleId(Long id) {
		return assignmentDAO.getIdByParentModuleId(id);
	}

	public Assignment findIdByCourseIdAndParentModuleId(String courseId, String parentModuleId) {
		return assignmentDAO.findIdByCourseIdAndParentModuleId(courseId, parentModuleId);
	}

	public List<Assignment> getChildListByParentModuleId(Long id) {
		return assignmentDAO.getChildListByParentModuleId(id);
	}

	public List<Assignment> searchAssignmentsForFacultyByModule(String username, Long programId) {
		return assignmentDAO.searchAssignmentsForFacultyByModule(username, programId);
	}

	public List<Assignment> searchAssignmentsForFacultyByModule(String username, Long programId, Long moduleId) {
		return assignmentDAO.searchAssignmentsForFacultyByModule(username, programId, moduleId);
	}

	public void softDeleteAssignmentForModule(Long id) {
		studentAssigmentDAO.deleteStudentAssigmentForModule(id);
		assignmentDAO.softDeleteAssignmentForModule(id);

	}

	public List<Assignment> findByFacultyAndModuleActive_new(String username, Long moduleId) {
		return assignmentDAO.findByFacultyAndModuleActive_new(username, moduleId);
	}

	public List<Assignment> searchAssignmentsForAdmin(Long programId) {
		return assignmentDAO.searchAssignmentsForAdmin(programId);
	}

	public List<Assignment> searchAssignmentsForAdmin(Long programId, Long courseId) {
		return assignmentDAO.searchAssignmentsForAdmin(programId, courseId);
	}

	public List<Assignment> getAllAssignmentCount(String startDate, String endDate) {
		return assignmentDAO.getAllAssignmentCount(startDate, endDate);
	}

	public List<Assignment> getCurrentAllAssignmentCount() {

		return assignmentDAO.getCurrentAllAssignmentCount();
	}

	public List<Assignment> getLateSubmittedStudentsByAssignId(String id) {
		return assignmentDAO.getLateSubmittedStudentsByAssignId(id);
	}

	/* For Assignment Pool Start */
	public int chkStartDateForUpdate(Long assignmentId) {
		return assignmentDAO.chkStartDateForUpdate(assignmentId);
	}

	// new queries on 19-09-2020
	public List<Assignment> getAssignmentsForTeeModule(String facultyId, String moduleId, String acadYear,
			String courseId) {
		return assignmentDAO.getAssignmentsForTeeModule(facultyId, moduleId, acadYear, courseId);
	}

	public List<Assignment> getCoursesForAssignmentTee(String moduleId, String acadYear, String facultyId,
			String courseIdDivWise) {
		return assignmentDAO.getCoursesForAssignmentTee(moduleId, acadYear, facultyId, courseIdDivWise);
	}
	public List<Assignment> getAssignmentsByIds(List<String> assignmentIds) {
		return assignmentDAO.getAssignmentsByIds(assignmentIds);
	}
	
	public List<Assignment> findAllAssignmentWithCourseIdsForAdmin(
			List<String> courseIds) {
		return assignmentDAO.findAllAssignmentWithCourseIdsForAdmin(courseIds);
	}
	
	public List<Assignment> findAssignmentReportListWithAssignmentIdForAdmin(
			String assignmentId) {
		return assignmentDAO.findAssignmentReportListWithAssignmentIdForAdmin(assignmentId);
	}
	
	public String getXlsxforAssignmentReport(List<Assignment> assignmentReportList) {
		Date date = new Date();
		File file = new File(downloadAllFolder + File.separator
				+ "AssignmentReport"
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
				+ ".xlsx");
		try {
			file.createNewFile();
			
			
			List<Assignment> admStatus = assignmentReportList;

			Map<String, List<Map<String, Object>>> lstExcelData = new HashMap<String, List<Map<String, Object>>>();
			
			
			List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
					"Program", "Session", "Course Name", "Subject Name", "Student SAP Id", "Student Name", 
					"Faculty SAP Id", "Faculty Name", "Question Number", "Score"));
			
			
			int count = 1;
			for (Assignment ad : assignmentReportList) {
				

				Map<String, Object> map = new HashMap();
				
				
				map.put("Sr.No", count);
				map.put("Program", ad.getProgramName() == null ? "-" : ad.getProgramName());
				map.put("Session", ad.getAcadSession() == null ? "-" : ad.getAcadSession());
				map.put("Course Name", ad.getCourseName() == null ? "-" : ad.getCourseName());
				map.put("Subject Name", ad.getModuleName() == null ? "-" : ad.getModuleName());
				map.put("Student SAP Id", ad.getUsername() == null ? "-" : ad.getUsername());
				map.put("Student Name", ad.getStudentName() == null ? "-" : ad.getStudentName());
				map.put("Faculty SAP Id", ad.getFacultyId() == null ? "-" : ad.getFacultyId());
				map.put("Faculty Name", ad.getFacultyName() == null ? "-" : ad.getFacultyName());
				map.put("Question Number", ad.getQuestionNumber() == null ? "-" : ad.getQuestionNumber());
				map.put("Score", ad.getMarks() == null ? "-" : ad.getMarks());
				

				if (lstExcelData.containsKey("AssignmentReport")) {
					List<Map<String, Object>> lst = lstExcelData
							.get("AssignmentReport");
					lst.add(map);

				} else {
					List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
					lst.add(map);

					lstExcelData.put("AssignmentReport", lst);
				}
				
				count++;

			}

			ExcelCreater.createExcelFile(lstExcelData, headers,
					file.getAbsolutePath());

		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return file.getAbsolutePath();
	}
}
