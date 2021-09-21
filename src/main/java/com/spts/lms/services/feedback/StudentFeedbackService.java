package com.spts.lms.services.feedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.feedback.StudentFeedbackDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("studentFeedbackService")
@Transactional
public class StudentFeedbackService extends BaseService<StudentFeedback> {

	@Autowired
	private StudentFeedbackDAO studentFeedbackDAO;

	@Override
	protected BaseDAO<StudentFeedback> getDAO() {
		return studentFeedbackDAO;
	}

	public void removeStudent_Feedback(String id) {
		studentFeedbackDAO.removeStudent_Feedback(id);
	}

	public List<StudentFeedback> findByfeedbackIDAndUsername(Long id,
			String username) {
		return studentFeedbackDAO.findByfeedbackIDAndUsername(id, username);
	}

	public List<StudentFeedback> getStudentsForFeedback(String facultyId,
			Long courseId, String acadMonth, Integer acadYear) {
		return studentFeedbackDAO.getStudentsForFeedback(facultyId, courseId,
				acadMonth, acadYear);
	}

	public List<StudentFeedback> getStudentFeedbackAllocationStatus(
			String feedbackId) {
		return studentFeedbackDAO
				.getStudentFeedbackAllocationStatus(feedbackId);
	}

	public List<StudentFeedback> getstudentFeedbackListUserWise(
			String feedbackId, List<String> username) {
		return studentFeedbackDAO.getstudentFeedbackListUserWise(feedbackId,
				username);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgram(
			String acadSession, List<String> programId) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgram(acadSession,
						programId);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgram(
			String acadSession, List<String> programId, String acadYear) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgram(acadSession,
						programId, acadYear);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
			String acadSession, List<String> programId, String acadYear,
			String campusId) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
						acadSession, programId, acadYear, campusId);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYear(
			String acadSession) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYear(acadSession);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWise(
			List<String> programId, List<String> acadSession) {
		return studentFeedbackDAO.getstudentFeedbackListCourseWise(programId,
				acadSession);
	}

	public List<StudentFeedback> findStudentFeedbacksByCourseAndFaculty(
			String courseId, String facultyId) {

		return studentFeedbackDAO.findStudentFeedbacksByCourseAndFaculty(
				courseId, facultyId);
	}

	public List<StudentFeedback> getstudentFeedbackCompleteListByInputParam(

	String fromDate, String toDate) {

		return studentFeedbackDAO.getstudentFeedbackCompleteListByInputParam(

		fromDate, toDate);

	}

	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyAndStudentList(
			String courseId, String facultyId, List<String> usernames) {

		return studentFeedbackDAO
				.findStudentFeedbacksByCourseAndFacultyAndStudentList(courseId,
						facultyId, usernames);
	}

	public List<StudentFeedback> getstudentFeedbackCompleteListByInputParam(
			String fromDate, String toDate, String facultyId) {

		return studentFeedbackDAO.getstudentFeedbackCompleteListByInputParam(
				fromDate, toDate, facultyId);
	}

	public List<StudentFeedback> getAllocatedStudentFeedback(String feedbackId) {
		return studentFeedbackDAO.getAllocatedStudentFeedback(feedbackId);
	}

	public List<StudentFeedback> getStudentFeedbackResponseList(
			String feedbackId) {
		return studentFeedbackDAO.getStudentFeedbackResponseList(feedbackId);
	}

	public List<StudentFeedback> findByfeedbackAllocatedWithCourseName(Long id,
			String username) {
		return studentFeedbackDAO.findByfeedbackAllocatedWithCourseName(id,
				username);
	}

	public List<StudentFeedback> getStudentsByProgram(String programId,
			Integer acadYear,String acadMonth) {

		return studentFeedbackDAO.getStudentsByProgram(programId, acadYear,acadMonth);
	}

	public List<StudentFeedback> getStudentsByProgramAndCampus(
			String programId, String campusId, Integer acadYear,String acadMonth) {

		return studentFeedbackDAO.getStudentsByProgramAndCampus(programId,
				campusId, acadYear,acadMonth);
	}

	public List<StudentFeedback> findStudentFeedbackByFaculty(String facultyId) {
		return studentFeedbackDAO.findStudentFeedbackByFaculty(facultyId);
	}

	public List<StudentFeedback> searchAllFeedback(Long courseId, String status) {
		return studentFeedbackDAO.searchAllFeedback(courseId, status);
	}

	public List<StudentFeedback> getPendingFeedbackByStudent(String username) {
		return studentFeedbackDAO.getPendingFeedbackByStudent(username);
	}

	public Page<StudentFeedback> getStudentForFeedback(String feedbackId,
			String facultyId, Long courseId, String acadMonth,
			Integer acadYear, int pageNo, int pageSize) {
		return studentFeedbackDAO.getStudentForFeedback(feedbackId, facultyId,
				courseId, acadMonth, acadYear, pageNo, pageSize);
	}

	public Page<StudentFeedback> getStudentForFeedback(String feedbackId,
			String facultyId, Long courseId, int pageNo, int pageSize) {
		return studentFeedbackDAO.getStudentForFeedback(feedbackId, facultyId,
				courseId, pageNo, pageSize);
	}

	public List<StudentFeedback> getNoOfStudentAllocatedFeedback(
			String courseId, String feedbackId) {
		return studentFeedbackDAO.getNoOfStudentAllocatedFeedback(courseId,
				feedbackId);
	}

	public List<StudentFeedback> getStudentGaveFeedback(String courseId,
			String feedbackId) {
		return studentFeedbackDAO.getStudentGaveFeedback(courseId, feedbackId);
	}

	public List<StudentFeedback> checkFeedbackValidity(Long feedbackId,
			String username) {

		return studentFeedbackDAO.checkFeedbackValidity(feedbackId, username);

	}

	public Page<StudentFeedback> getStudentFeedbackResponse(String feedbackId,
			int pageNo, int pageSize) {
		return studentFeedbackDAO.getStudentFeedbackResponse(feedbackId,
				pageNo, pageSize);
	}

	public Page<StudentFeedback> viewStudentFeedback(String feedbackId,
			String acadMonth, Integer acadYear, int pageNo, int pageSize) {
		return studentFeedbackDAO.viewStudentFeedback(feedbackId, acadMonth,
				acadYear, pageNo, pageSize);
	}

	public Page<StudentFeedback> getStudentFeedbackResponseByUsername(
			String username, int pageNo, int pageSize) {
		return studentFeedbackDAO.getStudentFeedbackResponseByUsername(
				username, pageNo, pageSize);
	}

	public List<StudentFeedback> findFeedbackAllocatedToCourse(Long courseId) {

		return studentFeedbackDAO.findFeedbackAllocatedToCourse(courseId);
	}

	public List<StudentFeedback> getStudentFeedbackStatus(String feedbackId,
			String username) {
		return studentFeedbackDAO
				.getStudentFeedbackStatus(feedbackId, username);
	}

	public List<StudentFeedback> findFeedbackCompletedStatus(String username,
			String feedbackId) {
		return studentFeedbackDAO.findFeedbackCompletedStatus(username,
				feedbackId);
	}

	public StudentFeedback findnoofStudentsWhoTookFeedbackList(Long courseId) {

		return studentFeedbackDAO.findnoofStudentsWhoTookFeedbackList(courseId);
	}

	public StudentFeedback findByCourseIdAndFeedback(String feedbackId,
			String username, String courseId, String studentFeedbackId) {
		return studentFeedbackDAO.findByCourseIdAndFeedback(feedbackId,
				username, courseId, studentFeedbackId);
	}

	public List<StudentFeedback> getStudentsByAcadSession(String acadSession) {

		return studentFeedbackDAO.getStudentsByAcadSession(acadSession);
	}

	public List<StudentFeedback> getStudentsByAcadSession(String programId,
			String acadSession, Integer acadYear,String acadMonth) {

		return studentFeedbackDAO.getStudentsByAcadSession(programId,
				acadSession, acadYear,acadMonth);
	}

	public List<StudentFeedback> findFeedbacksBySessionAndYearForProgram(

	String acadSession, Integer acadYear, Long programId) {

		return studentFeedbackDAO.findFeedbaclsBySessionAndYearForProgram(

		acadSession, acadYear, programId);

	}

	public List<StudentFeedback> findFeedbacksBySessionAndYearForCollege(

	String acadSession, Integer acadYear) {

		return studentFeedbackDAO.findFeedbaclsBySessionAndYearForCollege(

		acadSession, acadYear);

	}

	public List<StudentFeedback> findAllFeedbackForProgram(Long programId) {

		return studentFeedbackDAO.findAllFeedbackForProgram(programId);

	}

	public List<StudentFeedback> getStudentFeedbacksByCourse(String programId,
			String feedbackId, String acadYear, String acadSession,String acadMonth) {
		return studentFeedbackDAO.getStudentFeedbacksByCourse(programId,
				feedbackId, acadYear, acadSession,acadMonth);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearForFaculty(
			String acadSession, String facultyId) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearForFaculty(acadSession,
						facultyId);
	}

	public List<StudentFeedback> getstudentFeedbackListForFacultyAndForAllPrograms(
			String facultyId) {
		return studentFeedbackDAO
				.getstudentFeedbackListForFacultyAndForAllPrograms(facultyId);

	}

	public List<StudentFeedback> getstudentFeedbackListForAllPrograms() {
		return studentFeedbackDAO.getstudentFeedbackListForAllPrograms();

	}

	public List<StudentFeedback> checkStudentFeedbackDuplicates(
			String feedbackId) {
		return studentFeedbackDAO.checkStudentFeedbackDuplicates(feedbackId);
	}

	public void deleteDuplicateStudentFeedback(String feedbackId) {
		studentFeedbackDAO.deleteDuplicateStudentFeedback(feedbackId);
	}

	public List<StudentFeedback> getStudentsByAcadSessionAndCampus(
			String programId, String acadSession, Integer acadYear,String acadMonth,
			String campusId) {

		return studentFeedbackDAO.getStudentsByAcadSessionAndCampus(programId,
				acadSession, acadYear,acadMonth, campusId);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFaculty(
			String acadSession, List<String> programId, String acadYear,
			String campusId, String username) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFaculty(
						acadSession, programId, acadYear, campusId, username);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramByFaculty(
			String acadSession, List<String> programId, String acadYear,
			String username) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramByFaculty(
						acadSession, programId, acadYear, username);
	}

	public List<StudentFeedback> getStudentFeedbackStats(String campusId,
			String fromDate, String toDate) {
		return studentFeedbackDAO.getStudentFeedbackStats(campusId, fromDate,
				toDate);
	}

	public int updateDates(StudentFeedback feedback) {
		return studentFeedbackDAO.updateDates(feedback);
	}

	public StudentFeedback getStudentFeedbackResponseByQuestionId(
			String feedbackId, String username, String courseId,
			String questionId, String facultyId) {
		return studentFeedbackDAO.getStudentFeedbackResponseByQuestionId(
				feedbackId, username, courseId, questionId, facultyId);
	}

	public int makeFacultyCourseInactiveForFeedback(StudentFeedback feedback,
			String role) {
		return studentFeedbackDAO.makeFacultyCourseInactiveForFeedback(
				feedback, role);
	}

	public void makeUserCourseInactiveForFeedback(String username, String role) {

		studentFeedbackDAO.makeUserCourseInactiveForFeedback(username, role);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusWithoutSession(
			List<String> programId, String acadYear, String campusId) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndCampusWithoutSession(
						programId, acadYear, campusId);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramWithoutSession(
			List<String> programId, String acadYear) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramWithoutSession(
						programId, acadYear);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFacultyWithoutSession(
			List<String> programId, String acadYear, String campusId,
			String username) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFacultyWithoutSession(
						programId, acadYear, campusId, username);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramByFacultyWithoutSession(
			List<String> programId, String acadYear, String username) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramByFacultyWithoutSession(
						programId, acadYear, username);
	}

	public List<String> getAllFacultiesForFeedback(String acadYear,
			String campusId, String username, List<String> acadSessions) {

		return studentFeedbackDAO.getAllFacultiesForFeedback(acadYear,
				campusId, username, acadSessions);
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseNew(
			List<String> programId, List<String> acadSession, String username) {
		return studentFeedbackDAO.getstudentFeedbackListCourseWiseNew(programId, acadSession, username);
	}

	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyNew(
			String courseId, String facultyId, String username) {
		return studentFeedbackDAO.findStudentFeedbacksByCourseAndFacultyNew(courseId, facultyId, username);
	}

	
	public List<StudentFeedback> getFacultyByFeedbackId(Long feedbackId){
		return studentFeedbackDAO.getFacultyByFeedbackId(feedbackId);
	}
	public void removeFacultyFromFeedback(String feedbackId,String facultyId,String courseId){
		studentFeedbackDAO.removeFacultyFromFeedback(feedbackId,facultyId,courseId);
	}


	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
			String acadSession, List<String> programId, String acadYear,
			String campusId, String username) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
						acadSession, programId, acadYear, campusId, username);
	}

	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgram(
			String acadSession, List<String> programId, String acadYear, String username) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgram(acadSession,
						programId, acadYear, username);
	}

	public List<String> getStudentFeedbackByFeedbackId(String feedbackId){
		return studentFeedbackDAO.getStudentFeedbackByFeedbackId(feedbackId);
	}
	
	public void removeStudent_FeedbackUsingExcel(String feedbackId,String username) {
		studentFeedbackDAO.removeStudent_FeedbackUsingExcel(feedbackId,username);
	}
	public List<StudentFeedback> findStartedFeedbackByUsername(String username){
		return studentFeedbackDAO.findStartedFeedbackByUsername(username);
	}

	
	public List<String> getCreatedbyListForSupportAdmin() {
		// TODO Auto-generated method stub
		return studentFeedbackDAO.findStartedFeedbackByUsername();
	}

	public int makeMultipleUserCourseInactiveForFeedbackSupportAdmin(List<String> usernameList, List<String> roleList,
			List<String> courseIdList) {
		return studentFeedbackDAO.makeMultipleUserCourseInactiveForFeedbackSupportAdmin(usernameList,roleList,courseIdList);
		
	}
	
	//amey 14-10-2020
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseByFeedbackType(
			List<String> programId, List<String> acadSession, String username, String feedbackType) {
		return studentFeedbackDAO.getstudentFeedbackListCourseWiseByFeedbackType(
				programId, acadSession, username, feedbackType);
	}
	
	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyAndFeedbackType(
			String courseId, String facultyId, String username, String feedbackType) {
		return studentFeedbackDAO.findStudentFeedbacksByCourseAndFacultyAndFeedbackType(courseId, 
				facultyId, username, feedbackType);
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusandType(
			String acadSession, List<String> programId, String acadYear,
			String campusId, String username, String feedbackType) {
		return studentFeedbackDAO.getstudentFeedbackListCourseWiseAndYearProgramAndCampusandType(
				acadSession, programId, acadYear, campusId, username, feedbackType);
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndType(
			String acadSession, List<String> programId, String acadYear, String username, String feedbackType) {
		return studentFeedbackDAO.getstudentFeedbackListCourseWiseAndYearProgramAndType(
				acadSession, programId, acadYear, username, feedbackType);
	}
	
	public List<StudentFeedback> findStudentFeedbacksByCourseAndFacultyAndType(
			String courseId, String facultyId, String feedbackType) {
		return studentFeedbackDAO.findStudentFeedbacksByCourseAndFacultyAndType(courseId, 
				facultyId, feedbackType);
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndCampusAndTypeByFacultyWithoutSession(
			List<String> programId, String acadYear, String campusId,
			String facultyId, String feedbackType) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndCampusAndTypeByFacultyWithoutSession(
						programId, acadYear, campusId, facultyId, feedbackType);
	}
	
	public List<StudentFeedback> getstudentFeedbackListCourseWiseAndYearProgramAndTypeByFacultyWithoutSession(
			List<String> programId, String acadYear, String facultyId, String feedbackType) {
		return studentFeedbackDAO
				.getstudentFeedbackListCourseWiseAndYearProgramAndTypeByFacultyWithoutSession(
						programId, acadYear, facultyId, feedbackType);
	}
	
}
