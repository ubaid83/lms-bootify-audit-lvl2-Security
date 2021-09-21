package com.spts.lms.services.assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.assignment.StudentAssignmentDAO;
import com.spts.lms.daos.groups.GroupDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("studentAssignmentService")
@Transactional
public class StudentAssignmentService extends BaseService<StudentAssignment> {
	@Autowired
	private GroupDAO groupsDAO;
	@Autowired
	private StudentAssignmentDAO studentAssignmentDAO;
	@Autowired
	private AssignmentDAO assignmentDAO;

	@Override
	public BaseDAO<StudentAssignment> getDAO() {
		return studentAssignmentDAO;
	}

	public List<StudentAssignment> getStudentUsername(Long assignmentId, Long courseId) {
		return studentAssignmentDAO.getStudentUsername(assignmentId, courseId);

	}

	public List<String> findStudentUsernames(Long assignmentId) {
		return studentAssignmentDAO.findStudentUsernames(assignmentId);
	}

	public List<StudentAssignment> getAssignmentsByStudent(Long courseId, String username) {
		return studentAssignmentDAO.getAssignmentsByStudent(courseId, username);
	}

	public int chkStartandEndDateOfAssignment(String username, Long id) {
		return studentAssignmentDAO.chkStartandEndDateOfAssignment(username, id);
	}
	
	public int chkStartandEndDtOfAssignment(String endDate, String username, Long id) {
		return studentAssignmentDAO.chkStartandEndDtOfAssignment(endDate,username, id);
	}

	public int chkStartandEndDtOfAssignment(String username, Long id) {
		return studentAssignmentDAO.chkStartandEndDtOfAssignment(username, id);
	}

	public void setStudentInActive(Long groupId, String username) {
		studentAssignmentDAO.setStudentInActive(groupId, username);
	}

	public void deleteStudent(Long groupId, String username) {
		studentAssignmentDAO.deleteStudent(groupId, username);
	}

	public List<StudentAssignment> getStudentsForAssignmentWithScores(Long assignmentId, Long courseId) {
		return studentAssignmentDAO.getStudentsForAssignmentWithScores(assignmentId, courseId);
	}

	public List<StudentAssignment> getLateSubmittedStudents(StudentAssignment assignment, Long assignmentId) {
		return studentAssignmentDAO.getLateSubmittedStudents(assignment, assignmentId);
	}

	public int getNoOfGroupsAllocated(Long id) {
		return studentAssignmentDAO.getNoOfGroupsAllocated(id);
	}

	public void setInActive(Long groupId) {
		studentAssignmentDAO.setInActive(groupId);
	}

	public List<StudentAssignment> findStudentsByGroupId(Long groupId) {
		return studentAssignmentDAO.findStudentsByGroupId(groupId);
	}

	public List<StudentAssignment> findAssignmentEvaluationForNonSunmittedStudents(String facultyId) {
		return studentAssignmentDAO.findAssignmentEvaluationForNonSunmittedStudents(facultyId);
	}

	public int getNoOfStudentsAllocated(Long id) {
		return studentAssignmentDAO.getNoOfStudentsAllocated(id);
	}

	public List<StudentAssignment> getStduentFiles(Long assignmentId) {
		return studentAssignmentDAO.getStudentFiles(assignmentId);
	}

	public List<StudentAssignment> getStduentFilesForReport(Long assignmentId) {
		return studentAssignmentDAO.getStudentFilesForReport(assignmentId);
	}

	public List<StudentAssignment> getNonSubmittedStudents(StudentAssignment assignment, Long assignmentId) {
		return studentAssignmentDAO.getNonSubmittedStudents(assignment, assignmentId);
	}

	public void saveApprovalStatus(String status, Long pk, String userName) {
		studentAssignmentDAO.saveApprovalStatus(status, pk, userName);
	}

	public String findByGroupAndAssignmentId(Long groupId, Long assignmentId) {
		return studentAssignmentDAO.findByGroupAndAssignmentId(groupId, assignmentId);
	}

	public List<StudentAssignment> getGroupsBasedOnCourseAndAssignment(StudentAssignment assignment, Long courseId,
			Long assignmentId) {
		return studentAssignmentDAO.getGroupsBasedOnCourseAndAssignment(assignment, courseId, assignmentId);
	}

	public List<StudentAssignment> serachAllAssignments(Long courseId, String status) {
		return studentAssignmentDAO.searchAllAssignment(courseId, status);
	}

	public List<StudentAssignment> getStudentsForAssignment(Long assignmentId, Long courseId, String acadMonth,
			Integer acadYear) {
		return studentAssignmentDAO.getStudentsForAssignment(assignmentId, courseId, acadMonth, acadYear);
	}

	public List<StudentAssignment> getStudentsForAssignment(Long assignmentId, Long courseId) {
		return studentAssignmentDAO.getStudentsForAssignment(assignmentId, courseId);
	}

	public void saveAssigmentFromStudent(StudentAssignment bean) {
		studentAssignmentDAO.saveAssigmentFromStudent(bean);
	}

	/*
	 * public List<StudentAssignment> getGroupsForAssignment(Long groupId, Long
	 * courseId, String acadMonth, Integer acadYear) { return
	 * studentAssignmentDAO.getGroupsForAssignment(groupId, courseId, acadMonth,
	 * acadYear); }
	 */

	public StudentAssignment findAssignmentSubmission(String userName, Long assignmentId) {
		return studentAssignmentDAO.findAssignmentSubmission(userName, assignmentId);
	}

	public List<StudentAssignment> findAssignmentSubmissionLst(String userName, Long assignmentId) {
		return studentAssignmentDAO.findAssignmentSubmissionLst(userName, assignmentId);
	}

	public void saveAssignmentScore(String score, Long pk, String userName) {
		studentAssignmentDAO.saveAssignmentScore(score, pk, userName);
	}

	public void saveAssignmentRemarks(String remarks, Long pk, String userName) {
		studentAssignmentDAO.saveAssignmentRemarks(remarks, pk, userName);
	}

	public List<StudentAssignment> findAssignmentForEvaluation(StudentAssignment assignment, String facultyId) {
		return studentAssignmentDAO.findAssignmentForEvaluation(assignment, facultyId);
	}

	public void saveLowScoreReason(String lowScoreReason, Long pk, String userName) {
		studentAssignmentDAO.saveLowScoreReason(lowScoreReason, pk, userName);

	}

	public List<StudentAssignment> getGroupsForAssignment(Long assignmentId, Long courseId, String acadMonth,
			String acadYear) {
		return studentAssignmentDAO.getGroupsForAssignment(assignmentId, courseId, acadMonth, acadYear);
	}

	public List<StudentAssignment> getGroupsForAssignment(Long assignmentId, Long courseId) {
		return studentAssignmentDAO.getGroupsForAssignment(assignmentId, courseId);
	}

	public List<StudentAssignment> getStudentsBasedOnCourse(Long courseId) {
		return studentAssignmentDAO.getStudentsBasedOnCourse(courseId);
	}

	public void insertAssigments(List<StudentAssignment> sas) {
		studentAssignmentDAO.insertData(sas);
	}

	/*
	 * public Page<StudentAssignment> getStudentsBasedOnAssignment(
	 * StudentAssignment assignment, int pageNo, int pageSize, Long assignmentId) {
	 * return studentAssignmentDAO.getStudentsBasedOnAssignment(assignment, pageNo,
	 * pageSize, assignmentId); }
	 */

	public List<StudentAssignment> getStudentsBasedOnAssignment(StudentAssignment assignment, Long assignmentId) {
		return studentAssignmentDAO.getStudentsBasedOnAssignment(assignment, assignmentId);
	}

	public List<StudentAssignment> getAssignmentBasedOnGroups(StudentAssignment studentassignment, Long groupId) {
		return studentAssignmentDAO.getAssignmentBasedOnGroups(studentassignment, groupId);
	}

	public void saveScore(String score, Long pk, String userName) {
		studentAssignmentDAO.saveScore(score, pk, userName);
	}

	public List<StudentAssignment> findAllAssignment(String acadMonth, String acadYear, String username) {
		return studentAssignmentDAO.findAllAssigment(acadMonth, acadYear, username);
	}

	public List<StudentAssignment> findOneAssignment(Long assignmentId) {
		return studentAssignmentDAO.findOneAssignment(assignmentId);
	}

	public List<StudentAssignment> getGroupsBasedOnCourseAndAssignment(Long courseId, Long assignmentId) {
		return studentAssignmentDAO.getGroupsBasedOnCourseAndAssignment(courseId, assignmentId);
	}

	public void saveGroupScore(String score, Long pk, String userName) {
		studentAssignmentDAO.saveGroupAssignmentScore(score, pk, userName);
	}

	public void saveGroupLowScoreReason(String lowScoreReason, Long pk, String userName) {
		studentAssignmentDAO.saveGroupLowScoreReason(lowScoreReason, pk, userName);

	}

	public void saveGroupAssignmentRemarks(String remarks, String userName, Long groupId) {
		studentAssignmentDAO.saveGroupAssignmentRemarks(remarks, userName, groupId);
	}

	public List<StudentAssignment> findAssignmentByStudent(String username) {

		return studentAssignmentDAO.findAssignmentByStudent(username);

	}

	public List<StudentAssignment> findAssignmentsByCourseId(Long courseId) {
		return studentAssignmentDAO.findAssignmentsByCourseId(courseId);
	}

	public List<StudentAssignment> findAssignmentsByStatus(String status) {
		return studentAssignmentDAO.findAssignmentByStatus(status);
	}

	public List<StudentAssignment> findStudentsForPlagiarismCheck(Long assignmentId) {
		return studentAssignmentDAO.findStudentsForPlagiarismCheck(assignmentId);
	}

	public List<StudentAssignment> findAssignmentByStudent(String username, String courseId) {

		return studentAssignmentDAO.findAssignmentByStudent(username, courseId);

	}

	public List<StudentAssignment> getStudentsForAssignmentOnCampusId(Long assignmentId, Long courseId, Long campusId) {
		return studentAssignmentDAO.getStudentsForAssignmentOnCampusId(assignmentId, courseId, campusId);
	}

	public List<StudentAssignment> getGroupsBasedOnCourseAndAssignmentWithGroupCourse(StudentAssignment assignment,
			Long courseId, Long assignmentId) {
		return studentAssignmentDAO.getGroupsBasedOnCourseAndAssignmentWithGroupCourse(assignment, courseId,
				assignmentId);
	}

	public List<StudentAssignment> getStudentAssignmentSubmissionStats(String campusId, String fromDate,
			String toDate) {
		return studentAssignmentDAO.getStudentAssignmentSubmissionStats(campusId, fromDate, toDate);

	}

	public void makeAllInactiveFirst(Long assignmentId, Long groupId) {
		studentAssignmentDAO.makeAllInactiveFirst(assignmentId, groupId);
	}

	public void updateSubmitter(String studentAssignmentId) {
		studentAssignmentDAO.updateSubmitter(studentAssignmentId);
	}

	public List<StudentAssignment> getStudentsByGroup(Long assignmentId) {

		return studentAssignmentDAO.getStudentsByGroup(assignmentId);
	}

	public List<StudentAssignment> findOneAssignmentByGroupId(Long assignmentId, Long groupId) {
		return studentAssignmentDAO.findOneAssignmentByGroupId(assignmentId, groupId);
	}

	public StudentAssignment getSubmitterForAssignment(Long assignmentId, Long groupId) {

		return studentAssignmentDAO.getSubmitterForAssignment(assignmentId, groupId);
	}

	public List<String> getAllSapIdsOfStudentFromAssignment(Long assignmentId, Long groupId) {

		return studentAssignmentDAO.getAllSapIdsOfStudentFromAssignment(assignmentId, groupId);
	}

	public List<StudentAssignment> getAssignmentsForGradeCenter(String username, Long courseId) {
		return studentAssignmentDAO.getAssignmentsForGradeCenter(username, courseId);
	}

	public List<StudentAssignment> getAssignmentsForParentReport(String username, Long courseId) {
		return studentAssignmentDAO.getAssignmentsForParentReport(username, courseId);
	}

	public List<StudentAssignment> getStudentsForAssignmentModule(List<Long> assignmentId, List<String> courseId,
			String acadYear) {
		return studentAssignmentDAO.getStudentsForAssignmentModule(assignmentId, courseId, acadYear);
	}

	public List<StudentAssignment> getStudentsForAssignmentForModuleOnCampusId(List<Long> assignmentId,
			List<String> courseId, String acadYear, Long campusId) {
		return studentAssignmentDAO.getStudentsForAssignmentForModuleOnCampusId(assignmentId, courseId, acadYear,
				campusId);
	}

	public StudentAssignment getNoOfStudentsAllocated(List<Long> assignmentId) {
		return studentAssignmentDAO.getNoOfStudentsAllocated(assignmentId);
	}

	public List<StudentAssignment> getStudentsBasedOnAssignmentForModule(StudentAssignment assignment,
			List<Long> assignmentId) {
		return studentAssignmentDAO.getStudentsBasedOnAssignmentForModule(assignment, assignmentId);
	}

	public List<StudentAssignment> getStudentFilesForModule(List<Long> assignmentId) {
		return studentAssignmentDAO.getStudentFilesForModule(assignmentId);
	}

	public List<StudentAssignment> findStudentsForPlagiarismCheckForModule(List<Long> assignmentId) {
		return studentAssignmentDAO.findStudentsForPlagiarismCheckForModule(assignmentId);
	}

	public List<StudentAssignment> findOneAssignment(List<Long> assignmentId) {
		return studentAssignmentDAO.findOneAssignment(assignmentId);
	}

	// Hiren 19-02-2020
	public StudentAssignment getAllGroupAssignemnt(String id) {
		return studentAssignmentDAO.getAllGroupAssignemnt(id);
	}

	public List<String> getGroupAssignmentStudentFiles(Long groupId, Long assignmentId) {
		return studentAssignmentDAO.getGroupAssignmentStudentFiles(groupId, assignmentId);
	}

	public List<StudentAssignment> getStudentFilesForlateSubmitted(Long assignmentId) {
		return studentAssignmentDAO.getStudentFilesForlateSubmitted(assignmentId);
	}

	public List<StudentAssignment> getStudentsAssignmentReport(Long assignmentId) {
		return studentAssignmentDAO.getStudentsAssignmentReport(assignmentId);
	}

	public int checkAssignmentEvaluationStatus(String username, Long id) {
		return studentAssignmentDAO.checkAssignmentEvaluationStatus(username, id);
	}

	public String getApprovalStatusForAssignment(String username, Long id) {
		return studentAssignmentDAO.getApprovalStatusForAssignment(username, id);
	}

	public void setSubmissionDate(String submissionDate, Long id, String username) {
		studentAssignmentDAO.setSubmissionDate(submissionDate, id, username);
	}

	public void setSubmissionStatusForGroupAssignment(Long assignmentId) {
		studentAssignmentDAO.setSubmissionStatusForGroupAssignment(assignmentId);
	}

	public List<String> getAllSapIdsOfStudentFromAssignmentForRemark(Long assignmentId) {
		return studentAssignmentDAO.getAllSapIdsOfStudentFromAssignmentForRemark(assignmentId);
	}

	public List<StudentAssignment> getStudentsAssignmentReportForModule(List<Long> assignmentId) {
		return studentAssignmentDAO.getStudentsAssignmentReportForModule(assignmentId);
	}

	public void updateAssignmentsEvaluationMarks(StudentAssignment sa, List<Long> assignmentId) {
		studentAssignmentDAO.updateAssignmentsEvaluationMarks(sa, assignmentId);
	}

	// Hiren 29-08-2020

	public List<StudentAssignment> getAllStudentToEvaluateAssignment(Long assignmentId) {
		return studentAssignmentDAO.getAllStudentToEvaluateAssignment(assignmentId);
	}

	public List<String> getAllStudentSAPIdToEvaluateAssignment(Long assignmentId) {
		return studentAssignmentDAO.getAllStudentSAPIdToEvaluateAssignment(assignmentId);
	}

	public void updateStudentAssignmentTotalScore(StudentAssignment sa) {
		studentAssignmentDAO.updateStudentAssignmentTotalScore(sa);
	}

	public void updateDisclaimer(String assignmentId, String username) {
		studentAssignmentDAO.updateDisclaimer(assignmentId, username);
	}

	// added by akshay

	public int insertStudentAssignmenteHashKey(StudentAssignment sa) {

		return studentAssignmentDAO.insertStudentAssignmentHashKey(sa);

	}

	public int updateStudentAssignmentHashKey(StudentAssignment sa) {

		return studentAssignmentDAO.updateStudentAssignmentHashKey(sa);

	}

	public StudentAssignment getStudentAssignmentHashKey(String username, Long assignmentId) {
		return studentAssignmentDAO.getStudentAssignmentHashKey(username, assignmentId);
	}

	public int chkStartandEndDateOfAssignment(String username, Long id, Date keyGenDate) {
		return studentAssignmentDAO.chkStartandEndDateOfAssignment(username, id, keyGenDate);
	}

	public int chkStartandEndDtOfAssignment(String username, Long id, Date keyGenDate) {
		return studentAssignmentDAO.chkStartandEndDtOfAssignment(username, id, keyGenDate);
	}

	public int softDelStudentAssignmentHashKey(StudentAssignment sa) {
		return studentAssignmentDAO.softDelStudentAssignmentHashKey(sa);
	}

	public List<StudentAssignment> studentAssignmentHashKeys(Long assignmentId) {
		return studentAssignmentDAO.studentAssignmentHashKeys(assignmentId);
	}
	public List<StudentAssignment> getStudentDetails(Long id) {
		return studentAssignmentDAO.getStudentDetails(id);
	}

	/* For Assignment Pool Start */
	public List<StudentAssignment> getStudentsForAssignent(Long assignmentId) {
		return studentAssignmentDAO.getStudentsForAssignent(assignmentId);
	}
	
	public void deleteStudentByAssignment(Long assignmentId) {
		studentAssignmentDAO.deleteStudentByAssignment(assignmentId);
	}
	
	public List<StudentAssignment> getStudentsForAssignentById(Long assignmentId) {
		return studentAssignmentDAO.getStudentsForAssignentById(assignmentId);
	}
	/* For Assignment Pool End */
	public List<StudentAssignment> getStudentAssignmentMarksByAssignmentIds(List<String> assignmentIds) {
		return studentAssignmentDAO.getStudentAssignmentMarksByAssignmentIds(assignmentIds);
	}

	public List<StudentAssignment> getStudentAssignmentMarksByAssignmentIds(List<String> assignmentIds,
			List<String> usernames) {
		return studentAssignmentDAO.getStudentAssignmentMarksByAssignmentIds(assignmentIds, usernames);
	}
	
	//PETER 15/07/2021
	public int updateAssignmentApprovalStatus(List<String> usernames, Long assignmentId) {
		return studentAssignmentDAO.updateAssignmentApprovalStatus(usernames, assignmentId);
	}
	
	//Peter 04/08/2021
	public int updateFilePath(Long studentAssignmentId, String newFilePath) {
		return studentAssignmentDAO.updateFilePath(studentAssignmentId,newFilePath);
	}
}
