package com.spts.lms.services.feedback;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.feedback.StudentFeedbackResponseDAO;
import com.spts.lms.services.BaseService;

@Service("studentFeedbackResponseService")
@Transactional
public class StudentFeedbackResponseService extends
		BaseService<StudentFeedbackResponse> {

	@Autowired
	private StudentFeedbackResponseDAO studentFeedbackResponseDAO;

	@Override
	protected BaseDAO<StudentFeedbackResponse> getDAO() {
		return studentFeedbackResponseDAO;
	}

	public List<StudentFeedbackResponse> findStudentFeedbackResponse(
			Long feedbackId) {

		return studentFeedbackResponseDAO
				.findStudentFeedbackResponse(feedbackId);
	}

	public List<StudentFeedbackResponse> findStudentFeedbackResponseByUsername(
			Long feedbackId, String username) {
		return studentFeedbackResponseDAO
				.findStudentFeedbackResponseByUsername(feedbackId, username);
	}

	public int checkFeedbackCompletionStatusByCourse(Long feedbackId,
			String studentFeedbackId, String username) {
		return studentFeedbackResponseDAO
				.checkFeedbackCompletionStatusByCourse(feedbackId,
						studentFeedbackId, username);
	}

	public List<StudentFeedbackResponse> getAverageByQuestionId(
			List<String> feedbackQuestionIds) {
		return studentFeedbackResponseDAO
				.getAverageByQuestionId(feedbackQuestionIds);
	}

	public String getAvgAnswer(List<String> courseId, String facultyId,
			List<String> feedbackQuestId) {

		return studentFeedbackResponseDAO.getAvgAnswer(courseId, facultyId,
				feedbackQuestId);

	}

	public int getGrandTotal(List<String> studentFeedbackId) {

		return studentFeedbackResponseDAO.getGrandTotal(studentFeedbackId);

	}

	public List<Map<String, Object>> getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId) {
		return studentFeedbackResponseDAO
				.getCourseAverageScoreByFeedbackQuestionIdAndCourseId(
						feedbackQuestionIds, courseId);

	}

	/*public String getAvgAnswerforActiveFeedbackByFaculty(List<String> courseId,
			String facultyId) {

		return studentFeedbackResponseDAO
				.getAvgAnswerforActiveFeedbackByFaculty(courseId, facultyId);

	}*/
	
	public String getAvgAnswerforActiveFeedbackByFaculty(List<String> courseId,
			String facultyId, String username) {

		return studentFeedbackResponseDAO
				.getAvgAnswerforActiveFeedbackByFaculty(courseId, facultyId, username);

	}

	/*public String getAvgAnswerforActiveFeedback(List<String> courseId) {

		return studentFeedbackResponseDAO
				.getAvgAnswerforActiveFeedback(courseId);

	}*/
	
	public String getAvgAnswerforActiveFeedback(List<String> courseId, String username) {

		return studentFeedbackResponseDAO
				.getAvgAnswerforActiveFeedback(courseId, username);

	}

	public List<Map<String, Object>> getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId, String facultyId) {
		return studentFeedbackResponseDAO
				.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
						feedbackQuestionIds, courseId, facultyId);

	}

	public Map<String, Object> getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
			Long feedbackQuestionId, String courseId, String facultyId) {
		return studentFeedbackResponseDAO
				.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
						feedbackQuestionId, courseId, facultyId);

	}

	public List<StudentFeedbackResponse> getstudentFeedbackResponseListByStudentFeedbackId(
			List<String> studentFeedbackId) {
		return studentFeedbackResponseDAO
				.getstudentFeedbackResponseListByStudentFeedbackId(studentFeedbackId);

	}

	public List<Map<String, Object>> getFacultyAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId,
			String facultyId, List<String> usernameList) {
		return studentFeedbackResponseDAO
				.getFacultyAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
						feedbackQuestionIds, courseId, facultyId, usernameList);

	}

	public List<Map<String, Object>> getCourseAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
			List<String> feedbackQuestionIds, String courseId,
			List<String> usernameList) {
		return studentFeedbackResponseDAO
				.getCourseAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
						feedbackQuestionIds, courseId, usernameList);

	}

	public List<StudentFeedbackResponse> findAverageForFeedback(Long courseId,
			List<Long> questionId) {
		return studentFeedbackResponseDAO.findAverageForFeedback(courseId,
				questionId);
	}

	public List<StudentFeedbackResponse> findFeedbackResponseByStudent(

	String username) {

		return studentFeedbackResponseDAO

		.findFeedbackResponseByStudent(username);

	}

	public Map<String, Object> getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
			List<String> feedbackQuestionIdList, String courseId,
			String facultyId) {
		return studentFeedbackResponseDAO
				.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
						feedbackQuestionIdList, courseId, facultyId);
	}

	public List<StudentFeedbackResponse> findAnswersByFacultyAndProgramAndYear(
			String acadYear, String facultyId, String programId,
			List<String> acadSessions) {

		return studentFeedbackResponseDAO
				.findAnswersByFacultyAndProgramAndYear(acadYear, facultyId,
						programId, acadSessions);
	}

	public List<StudentFeedbackResponse> findAnswersByDept(String acadYear,
			String facultyId, List<String> acadSessions) {

		return studentFeedbackResponseDAO.findAnswersByDept(acadYear,
				facultyId, acadSessions);
	}
	
	/*public List<String> getAvgAnswer1(List<String> courseId,
			List<String> facultyId, List<String> feedbackQuestId) {
		
		return studentFeedbackResponseDAO.getAvgAnswer1(courseId,facultyId,feedbackQuestId);
		
	}*/
	
	public List<String> getAvgAnswer1(List<String> courseId,
			List<String> facultyId, List<String> feedbackQuestId, String username) {
		
		return studentFeedbackResponseDAO.getAvgAnswer1(courseId,facultyId,feedbackQuestId, username);
		
	}
	
	/*public List<StudentFeedbackResponse> getAvgAnswersByFacultyId(List<String> courseId, List<String> facultyId,
			List<String> feedbackQuestId){
		return studentFeedbackResponseDAO.getAvgAnswersByFacultyId(courseId,facultyId,feedbackQuestId);
	}*/
	
	public List<StudentFeedbackResponse> getAvgAnswersByFacultyId(List<String> courseId, List<String> facultyId,
			List<String> feedbackQuestId, String username){
		return studentFeedbackResponseDAO.getAvgAnswersByFacultyId(courseId,facultyId,feedbackQuestId, username);
	}
	
	
	
	public List<StudentFeedbackResponse> findAnswersByFacultyAndModuleAndYear(
			String acadYear, String facultyId, String moduleId,
			List<String> acadSessions) {
		
		return studentFeedbackResponseDAO.findAnswersByFacultyAndModuleAndYear(acadYear,facultyId,moduleId,acadSessions);
	}

}
