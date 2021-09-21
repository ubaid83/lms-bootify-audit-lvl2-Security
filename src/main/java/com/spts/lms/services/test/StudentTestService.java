package com.spts.lms.services.test;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.PendingTest;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.test.StudentTestDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("studentTestService")
@Transactional
public class StudentTestService extends BaseService<StudentTest> {

	@Autowired
	private StudentTestDAO studentTestDAO;

	@Autowired
	private AssignmentDAO assignmentDAO;
	private static final Logger logger = Logger
			.getLogger(StudentTestService.class);

	@Autowired
	@Override
	protected BaseDAO<StudentTest> getDAO() {
		return studentTestDAO;
	}

	public StudentTest findBytestIDAndUsername(Long testId, String username) {
		return studentTestDAO.findBytestIDAndUsername(testId, username);
	}

	public StudentTest findBytestIDAndUsernameAndTestCompleted(Long testId,
			String username) {
		return studentTestDAO.findBytestIDAndUsernameAndTestCompleted(testId,
				username);
	}

	public List<StudentTest> findByUsername(String username) {
		return studentTestDAO.findByUsername(username);
	}

	public List<StudentTest> searchAllTest(Long courseId, String status) {
		return studentTestDAO.searchAllTest(courseId, status);
	}

	public int chkStartandEndDateOfTest(String username, Long id) {
		return studentTestDAO.chkStartandEndDateOfTest(username, id);
	}

	public List<StudentTest> getStudentForTestList(Long testId,
			String facultyId, Long courseId) {
		return studentTestDAO
				.getStudentForTestList(testId, facultyId, courseId);
	}

	public List<StudentTest> getStudentForTestWithScores(Long testId,
			Long courseId) {
		return studentTestDAO.getStudentForTestWithScores(testId, courseId);
	}

	public List<StudentTest> getTestsByStudent(Long courseId, String username) {
		return studentTestDAO.getTestsByStudent(courseId, username);
	}

	public StudentTest findStudentTestByStudentTestID(Long studentTestId) {
		return studentTestDAO.findStudentTestByStudentTestID(studentTestId);
	}

	public int getNoOfStudentsAllocated(Long id) {
		return studentTestDAO.getNoOfStudentsAllocated(id);
	}

	public List<StudentTest> getStudentUsername(Long testId, Long courseId) {
		return studentTestDAO.getStudentUsername(testId, courseId);

	}

	public List<StudentTest> findAllTest(String acadMonth, String acadYear,
			String facultyId) {
		return studentTestDAO.findAllTest(acadMonth, acadYear, facultyId);
	}

	public List<StudentTest> findOneTest(Long testId) {
		return studentTestDAO.findOneTest(testId);
	}

	public int chkStartandEndDateOfTests(String username, Long id) {
		return studentTestDAO.chkStartandEndDateOfTests(username, id);
	}

	public Page<StudentTest> getStudentForTest(Long testId, String facultyId,
			Long courseId, String acadMonth, String acadYear, int pageNo,
			int pageSize) {
		return studentTestDAO.getStudentForTest(testId, facultyId, courseId,
				acadMonth, acadYear.toString(), pageNo, pageSize);
	}

	public Page<StudentTest> getStudentForTest(Long testId, String facultyId,
			Long courseId, int pageNo, int pageSize) {
		return studentTestDAO.getStudentForTest(testId, facultyId, courseId,
				pageNo, pageSize);
	}

	public int upsert(StudentTest bean) {
		return super.insert(bean);
	}

	public List<StudentTest> getStudentToEvaluate(Long testId, Long courseId) {
		return studentTestDAO.getStudentToEvaluate(testId, courseId);
	}

	public int calculateMarks(StudentTest studentTest) {
		int num = studentTestDAO.calculateMarks(studentTest);
		logger.info("student marks ------------->"
				+ studentTestDAO.calculateMarks(studentTest));
		return studentTestDAO.calculateScore(studentTest);
	}

	public List<StudentTest> getStudentForTest(Long testId, Long courseId,
			String acadMonth, String acadYear) {
		return studentTestDAO.getStudentForTest(testId, courseId, acadMonth,
				acadYear);
	}

	public List<StudentTest> getStudentForTest(Long testId, Long courseId) {
		return studentTestDAO.getStudentForTest(testId, courseId);
	}

	public List<StudentTest> getStudentForTestAndCampusId(Long testId,
			Long courseId, Long campusId) {
		return studentTestDAO.getStudentForTestAndCampusId(testId, courseId,
				campusId);
	}

	public List<PendingTest> getPendingTest(String username, String acadMonth,
			String acadYear) {
		return studentTestDAO.getPendingTest(username, acadMonth, acadYear);
	}

	public List<PendingTest> getPendingTest(String username) {
		return studentTestDAO.getPendingTest(username);
	}

	public List<PendingTest> getPendingTestByCourse(String username,
			String courseId, String acadMonth, String acadYear) {
		return studentTestDAO.getPendingTestByCourse(username, acadMonth,
				acadYear, courseId);
	}

	public List<PendingTest> getPendingTestByCourse(String username,
			String courseId) {
		return studentTestDAO.getPendingTestByCourse(username, courseId);
	}

	public List<PendingTest> getPendingTestCountDashboard(String username,
			String acadMonth, String acadYear) {

		return studentTestDAO.getPendingTestCountDashboard(username, acadMonth,
				acadYear);
	}

	public List<PendingTest> getPendingTestCountDashboard(String username) {

		return studentTestDAO.getPendingTestCountDashboard(username);
	}

	public Page<StudentTest> getStudentsBasedOnTest(StudentTest test,
			int pageNo, int pageSize, Long testId) {
		return studentTestDAO.getStudentsBasedOnTest(test, pageNo, pageSize,
				testId);
	}

	public void saveMaxScore(Integer maxScore, Long pk, String userName) {
		studentTestDAO.saveMaxScore(maxScore, pk, userName);
	}

	public void savePassScore(Integer passScore, Long pk, String userName) {
		studentTestDAO.savePassScore(passScore, pk, userName);
	}

	public List<StudentTest> getStudentsForTest(Long testId, Long courseId,
			String acadMonth, String acadYear) {
		return studentTestDAO.getStudentsForTest(testId, courseId, acadMonth,
				acadYear);
	}

	public List<StudentTest> findTestByStudent(String username) {

		return studentTestDAO.findTestByStudent(username);

	}

	public List<StudentTest> searchAllTestByCourseId(Long courseId) {
		return studentTestDAO.searchAllTestByCourseId(courseId);
	}

	public List<StudentTest> searchAllTestByStatus(String status) {
		return studentTestDAO.searchAllTestByStatus(status);
	}

	public List<StudentTest> findTestByStudent(String username, String courseId) {
		return studentTestDAO.findTestByStudent(username, courseId);
	}

	public List<StudentTest> getNoOfStudentSubmissionStats(String campusId,
			String fromDate, String toDate) {
		return studentTestDAO.getNoOfStudentSubmissionStats(campusId, fromDate,
				toDate);
	}

	public void removeStudent_Test(String username, String id) {

		studentTestDAO.removeStudent_Test(username, id);
	}

	public List<StudentTest> findStudentTest(Long testId) {
		return studentTestDAO.findStudentTest(testId);
	}

	public void updateStudentTestDuration(String studentTestId,
			String durationInMinute) {
		studentTestDAO.updateStudentTestDuration(studentTestId,
				durationInMinute);
	}

	public List<StudentTest> findAllTestOfCurrentDateByStudent(String username,
			String date) {
		return studentTestDAO.findAllTestOfCurrentDateByStudent(username, date);
	}

	public StudentTest getTestSummaryById(String username) {
		return studentTestDAO.getTestSummaryById(username);
	}

	public StudentTest getTestSummaryByIdAndSem(String username,
			String acadSession) {
		return studentTestDAO.getTestSummaryByIdAndSem(username, acadSession);
	}

	public StudentTest getTestSummaryByIdAndSemAndCourse(String username,
			String acadSession, String courseId) {
		return studentTestDAO.getTestSummaryByIdAndSemAndCourse(username,
				acadSession, courseId);
	}

	public List<StudentTest> getTestsForGradeCenter(String username,
			Long courseId) {
		return studentTestDAO.getTestsForGradeCenter(username, courseId);
	}
	
	public List<StudentTest> getTestsForGradeCenterCreatedByAdminForTest(String username,
			String moduleId) {
		return studentTestDAO.getTestsForGradeCenterCreatedByAdminForTest(username, moduleId);
	}

	public List<StudentTest> getTestsForParentReport(String username,
			Long courseId) {
		return studentTestDAO.getTestsForParentReport(username, courseId);
	}
	
	public StudentTest callCompleteStudentTest(String studentTestId, String testType) {
		return studentTestDAO.callCompleteStudentTest(studentTestId, testType);
	}
	
	public void updateStudentTestResponseFilePath(String studentQRespFilePath,
			Long studentTestId) {
		studentTestDAO.updateStudentTestResponseFilePath(studentQRespFilePath, studentTestId);
	}
	
	public List<StudentTest> getIncompleteStudentTest(String testId){
		
		return studentTestDAO.getIncompleteStudentTest(testId);
	}
	
	public StudentTest allocateTestQuestionsForAllStudent(long testId, String filePath){
		
		return studentTestDAO.allocateTestQuestionsForAllStudent(testId, filePath);
	}
	
	public List<StudentTest> findByTestId(Long testId) {
		
		return studentTestDAO.findByTestId(testId);
	}
	
	public void updateStudentTestResponseFilePathByTestId(Long testId) {
		
		studentTestDAO.updateStudentTestResponseFilePathByTestId(testId);
	}
	
	public void deleteStudentTestResponseByTestId(Long testId) {
		
		studentTestDAO.deleteStudentTestResponseByTestId(testId);
	}

	public void removeStudentQRespFile(Long testId){
        studentTestDAO.removeStudentQRespFile(testId);
	}
	public void removeStudentQueResp(Long testId){
        studentTestDAO.removeStudentQueResp(testId);
	}
	
	public List<StudentTest> getStudentTestNotSubmitted(String testId){
		return studentTestDAO.getStudentTestNotSubmitted(testId);
	}
	
	public List<StudentTest> getStudentForTestByAdmin(Long testId, List<String> courseIds) {
		return studentTestDAO.getStudentForTestByAdmin(testId, courseIds);
	}
	
	public List<StudentTest> findStudentTestForDelete(Long testId) {
		return studentTestDAO.findStudentTestForDelete(testId);
	}
	public List<StudentTest> getStudetTestMarksByTestList(List<String> testId,List<String> username) {
		return studentTestDAO.getStudetTestMarksByTestList(testId,username);
	}
public List<StudentTest> getStudetTestMarksByTestList(List<String> testId) {
		return studentTestDAO.getStudetTestMarksByTestList(testId);
	}
	
	//Hiren 02-02-2021
	public void removeFaultyDemoTestTime(Long testId){
		studentTestDAO.removeFaultyDemoTestTime(testId);
	}
}
