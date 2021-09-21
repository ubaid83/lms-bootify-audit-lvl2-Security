package com.spts.lms.services.test;

import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.Test;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.TestDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("testService")
@Transactional
public class TestService extends BaseService<Test> {

	@Autowired
	private TestDAO testDAO;

	@Override
	protected BaseDAO<Test> getDAO() {
		return testDAO;
	}

	public List<Test> findAllActiveWithCourse() {
		return testDAO.findAllActiveWithCourse();
	}

	public List<Test> findAllValidTest() {
		return testDAO.findAllValidTest();
	}

	public Test findByIDAndFaculty(Long id, String facultyId,String role) {
		return testDAO.findByIDAndFaculty(id, facultyId,role);
	}

	public Page<Test> searchActiveByExactMatchReplacementForHOD(Long programId,
			int pageNo, int pageSize) {
		return testDAO.searchActiveByExactMatchReplacementForHOD(programId,
				pageNo, pageSize);
	}

	public Page<Test> findByCourse(Long courseId, int pageNo, int pageSize) {
		return testDAO.findByCourse(courseId, pageNo, pageSize);
	}

	public Test authenticateUserPrivilages(int id, String username) {
		return testDAO.authenticateUserPrivilages(id, username);
	}

	public void updateFacultyAssigned(String facultyId, Long testId) {
		testDAO.updateFacultyAssigned(facultyId, testId);
	}

	public List<Test> findByUser(String username, String acadMonth,
			String acadYear) {
		return testDAO.findByUser(username, acadMonth, acadYear);
	}

	public void showResults(Long id) {
		testDAO.showResults(id);
	}

	public List<Test> findtestByFacultyAndCourseAndType(String username,
			String courseId, String type) {
		return testDAO.findtestByFacultyAndCourseAndType(username, courseId,
				type);
	}

	public Page<Test> findTestAllocated(String username, int pageNo,
			int pageSize) {
		return testDAO.findTestAllocated(username, pageNo, pageSize);
	}

	public int chkStartDateForUpdate(Long testId) {
		return testDAO.chkStartDateForUpdate(testId);
	}

	public Page<Test> findTestAllocatedbyCourseId(String username,
			Long courseId, int pageNo, int pageSize) {
		return testDAO.findTestAllocatedbyCourseId(username, courseId, pageNo,
				pageSize);
	}

	public List<Test> findByUserAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		return testDAO.findByUserAndCourse(username, courseId, acadMonth,
				acadYear);
	}

	public List<Test> findByUserAndCourse(String username, Long courseId) {
		return testDAO.findByUserAndCourse(username, courseId);
	}

	public Page<Test> findAllByUser(String username, int pageNo, int pageSize,
			String acadMonth, String acadYear) {
		return testDAO.findAllByUser(username, acadMonth, acadYear, pageNo,
				pageSize);
	}

	public Page<Test> findAllByUserAndCourse(String username, Long courseId,
			int pageNo, int pageSize, String acadMonth, String acadYear) {
		return testDAO.findAllByUserAndCourse(username, courseId, acadMonth,
				acadYear, pageNo, pageSize);
	}

	public List<Test> findByCourse(Long courseId, String acadMonth,
			String acadYear) {
		return testDAO.findByCourse(courseId, acadMonth, acadYear);
	}

	public List<Test> findByCourse(Long courseId) {
		return testDAO.findByCourse(courseId);
	}

	@Async
	public Future<List<Test>> findByCourseAsync(Long courseId,
			String acadMonth, String acadYear) {
		return new AsyncResult<List<Test>>(testDAO.findByCourse(courseId,
				acadMonth, acadYear));
	}

	public List<Test> findByFaculty(String username, String acadMonth,
			String acadYear) {
		return testDAO.findByFaculty(username, acadMonth, acadYear);
	}

	public List<Test> findByFaculty(String username) {
		return testDAO.findByFaculty(username);
	}

	public Page<Test> findAllByFaculty(String username, int pageNo,
			int pageSize, String acadMonth, String acadYear) {
		return testDAO.findAllByFaculty(username, acadMonth, acadYear, pageNo,
				pageSize);
	}

	public List<Test> findAllTest(String acadMonth, String acadYear) {
		return testDAO.findAllTest(acadMonth, acadYear);
	}

	public List<Test> findAllTestForFaculty(String acadMonth, String acadYear,
			String facultyId) {
		return testDAO.findAllTestForFaculty(acadMonth, acadYear, facultyId);
	}

	public Page<Test> findAllByFacultyAndCourse(String username, Long courseId,
			int pageNo, int pageSize, String acadMonth, String acadYear) {
		return testDAO.findAllByFacultyAndCourse(username, courseId, acadMonth,
				acadYear, pageNo, pageSize);
	}

	public List<Test> findByFacultyAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		return testDAO.findByFacultyAndCourse(username, courseId, acadMonth,
				acadYear);
	}

	public Page<Test> findByFacultyAndCourse(String username, Long courseId,
			int pageNo, int pageSize) {
		return testDAO.findByFacultyAndCourse(username, courseId, pageNo,
				pageSize);
	}

	public List<Test> findByFacultyAndCourse(String username, Long courseId) {
		return testDAO.findByFacultyAndCourse(username, courseId);
	}

	public void updateTestDetails(Long testId, String value, String attribute,
			String userName) {
		testDAO.updateTestDetailsBasedOnAttribute(testId, value, attribute,
				userName);
	}

	public void softDeleteTest(Integer id) {
		testDAO.softDeleteTest(id);
	}

	public List<Test> getAllStudentsAssociatedWithGroup(String groupId) {
		return testDAO
				.findAllSQL(
						" select st.*,lt.*,CONCAT(lu.firstName,' ',lu.lastName) as studentFullName from lms.student_test st,lms.test lt,lms.users lu where "
								+ " st.testId = lt.id and st.username = lu.username and st.groupId = ? and st.testStartTime  is null ",
						new Object[] { groupId });
	}

	public List<Test> getAllTests(Long courseId, String username) {
		return testDAO.getAllTests(courseId, username);
	}

	public Page<Test> searchActiveByExactMatchReplacement(String username,
			Long programId, int pageNo, int pageSize) {
		return testDAO.searchActiveByExactMatchReplacement(username, programId,
				pageNo, pageSize);
	}

	public List<Test> findTestsBySessionAndYearForProgram(String acadSession,

	Integer acadYear, Long programId) {

		return testDAO.findTestsBySessionAndYearForProgram(acadSession,

		acadYear, programId);

	}

	public List<Test> findTestsBySessionAndYearForCollege(String acadSession,

	Integer acadYear) {

		return testDAO.findTestsBySessionAndYearForCollege(acadSession,

		acadYear);

	}

	public List<Test> findAllTestsByFaculty(String username) {

		return testDAO.findAllTestsByFaculty(username);

	}

	public List<Test> findAllTestForProgram(Long programId) {

		return testDAO.findAllTestForProgram(programId);

	}

	public int checkDatesForUpdate(Long testId) {
		return testDAO.checkDatesForUpdate(testId);
	}

	public List<Test> findTestAllocated(String username) {
		return testDAO.findTestAllocated(username);
	}

	public void hideResults(Long id) {
		testDAO.hideResults(id);
	}
	
	public List<Test> findtestByStartDate(String startDate) {
		return testDAO.findtestByStartDate(startDate);
	}
	
	public int getStudentCountByTestId(Long id){
		return testDAO.getStudentCountByTestId(id);
	}
	public List<Test> findTestAllocatedForContent(String username) {
		return testDAO.findTestAllocatedForContent(username);
	}

	//10-07-2020
	public Page<Test> findByFacultyAndCourseForDemo(String username, Long courseId,
			int pageNo, int pageSize) {
		return testDAO.findByFacultyAndCourseForDemo(username, courseId, pageNo,
				pageSize);
	}
	public Page<Test> searchActiveByExactMatchReplacementForDemo(String username,
			Long programId, int pageNo, int pageSize) {
		return testDAO.searchActiveByExactMatchReplacementForDemo(username, programId,
				pageNo, pageSize);
	}
	//27-07-2020

	public void updateHideOrShowReports(Long id, String showReportsToStudents) {
		testDAO.updateHideOrShowReports(id,showReportsToStudents);
	}
	
	public Page<Test> searchTestCreatedByAdminForFaculty(String username,
			Long programId,String moduleId,String acadYear, int pageNo, int pageSize) {
		
		return testDAO.searchTestCreatedByAdminForFaculty(username, programId, moduleId, acadYear, pageNo, pageSize);
	}
	

	public Page<Test> searchActiveByExactMatchReplacementForAdmin(String username,
			Long programId, int pageNo, int pageSize) {
		
		return testDAO.searchActiveByExactMatchReplacementForAdmin(username, programId, pageNo, pageSize);
	}
	
	
	//new queries
	
	public List<Test> getTestForIcaModue(String moduleId,String acadYear,String facultyId,String courseId){
		return testDAO.getTestForIcaModue(moduleId, acadYear, facultyId,courseId);
	}
public List<Test> getCoursesForTestIca(String moduleId,String acadYear,String facultyId,String courseIdDivWise){
		return testDAO.getCoursesForTestIca(moduleId, acadYear, facultyId,courseIdDivWise);
	}
	
public List<Test> getTestsByIds(List<String> testId) {
		return testDAO.getTestsByIds(testId);
	}
}
