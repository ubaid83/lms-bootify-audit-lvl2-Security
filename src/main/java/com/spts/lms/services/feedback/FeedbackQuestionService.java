package com.spts.lms.services.feedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.feedback.FeedbackQuestion;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.feedback.FeedbackQuestionDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("feedbackQuestionService")
@Transactional
public class FeedbackQuestionService extends BaseService<FeedbackQuestion> {

	@Autowired
	private FeedbackQuestionDAO feedbackQuestionDAO;

	@Override
	protected BaseDAO<FeedbackQuestion> getDAO() {
		return feedbackQuestionDAO;
	}

	public List<FeedbackQuestion> findByFeedbackId(Long feedbackId) {
		return feedbackQuestionDAO.findByFeedbackId(feedbackId);
	}

	public List<FeedbackQuestion> findStudentResponseByFeedbackIdAndUsername(
			Long feedbackId, String username) {
		return feedbackQuestionDAO.findStudentResponseByFeedbackIdAndUsername(
				feedbackId, username);
	}

	public Page<FeedbackQuestion> getStudentFeedbackResponsePage(
			Long feedbackId, String username, int pageNo, int pageSize) {
		return feedbackQuestionDAO.getStudentFeedbackResponsePage(feedbackId,
				username, pageNo, pageSize);
	}

	public FeedbackQuestion getQuestionForReferenceTemplate(String feedbackId,
			String username, String courseId, String questionId,
			String facultyId) {
		return feedbackQuestionDAO.getQuestionForReferenceTemplate(feedbackId,
				username, courseId, questionId, facultyId);
	}

	public List<FeedbackQuestion> getStudentFeedbackResponseList(
			Long feedbackId, String username) {
		return feedbackQuestionDAO.getStudentFeedbackResponseList(feedbackId,
				username);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemester(
			String acadSession) {
		return feedbackQuestionDAO.findFeedbackQuestionBySemester(acadSession);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemester(
			String acadSession, String acadYear) {
		return feedbackQuestionDAO.findFeedbackQuestionBySemester(acadSession,
				acadYear);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterAndCampus(
			String acadSession, String acadYear, String campusId) {
		return feedbackQuestionDAO.findFeedbackQuestionBySemesterAndCampus(
				acadSession, acadYear, campusId);
	}

	public List<FeedbackQuestion> getFeedbackQuestionListByCourse(
			String courseId, String username) {

		return feedbackQuestionDAO.getFeedbackQuestionListByCourse(courseId, username);
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseForfaculty(
			String courseId) {

		return feedbackQuestionDAO.getFeedbackQuestionListByCourseForfaculty(courseId);
	}

	public List<Long> findQuestionByFeedbackId(Long feedbackId) {
		return feedbackQuestionDAO.findQuestionByFeedbackId(feedbackId);
	}

	public List<FeedbackQuestion> findFeedbackQuestions() {
		return feedbackQuestionDAO.findFeedbackQuestions();
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgram(
			String acadSession, String acadYear, List<String> programIds) {
		return feedbackQuestionDAO.findFeedbackQuestionBySemesterProgram(
				acadSession, acadYear, programIds);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampus(
			String acadSession, String acadYear, String campusId,
			List<String> programIds) {
		return feedbackQuestionDAO
				.findFeedbackQuestionBySemesterProgramAndCampus(acadSession,
						acadYear, campusId, programIds);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(
			String acadYear, String campusId, List<String> programIds) {
		return feedbackQuestionDAO
				.findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(
						acadYear, campusId, programIds);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramWithoutSession(
			String acadYear, List<String> programIds) {
		return feedbackQuestionDAO
				.findFeedbackQuestionBySemesterProgramWithoutSession(acadYear,
						programIds);
	}

	public List<FeedbackQuestion> findAllQuestionsByFeedbackId(
			List<String> feedbackIds) {
		return feedbackQuestionDAO.findAllQuestionsByFeedbackId(feedbackIds);
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseNew(
			String courseId, String username) {
		return feedbackQuestionDAO.getFeedbackQuestionListByCourseNew(courseId, username);
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampus(
			String acadSession, String acadYear, String campusId,
			List<String> programIds, String username) {
		return feedbackQuestionDAO
				.findFeedbackQuestionBySemesterProgramAndCampus(acadSession,
						acadYear, campusId, programIds, username);
	}

	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgram(
			String acadSession, String acadYear, List<String> programIds, String username) {
		return feedbackQuestionDAO.findFeedbackQuestionBySemesterProgram(
				acadSession, acadYear, programIds, username);
	}

	public List<FeedbackQuestion> getFeedbackQuestionListByCourseForWs(
		String courseId) {
	return feedbackQuestionDAO.getFeedbackQuestionListByCourseForWs(courseId);
	}
	
	public List<FeedbackQuestion> getAllActiveFeedbackTemplateQuestions() {
        return feedbackQuestionDAO.getAllActiveFeedbackTemplateQuestions();
	}

	
	//Support Admin

		public int updateFeedbackQuestion(String description, String id) {
			return feedbackQuestionDAO.updateFeedbackQuestion(description,id);
			
		}

		
		public FeedbackQuestion getFeefbackForSupportDropBean(String feedbackId) {
	        return feedbackQuestionDAO.getFeefbackForSupportDropBean(feedbackId);
		}
		
	public List<FeedbackQuestion> findFeedbackQuestionByCourseList(String username,Set<Long> courseId){
		return feedbackQuestionDAO.findFeedbackQuestionByCourseList(username,courseId);
	}
	
	//amey 14-10-2020
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseAndFeedbackType(
			String courseId, String username, String feedbackType) {
		return feedbackQuestionDAO.getFeedbackQuestionListByCourseAndFeedbackType(
				courseId, username, feedbackType);
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndCampusAndType(
			String acadSession, String acadYear, String campusId,
			List<String> programId, String username, String feedbackType) {
		return feedbackQuestionDAO
				.findFeedbackQuestionBySemesterProgramAndCampusAndType(acadSession, acadYear, 
						campusId, programId, username, feedbackType);
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionBySemesterProgramAndType(
			String acadSession, String acadYear, List<String> programId, String username, String feedbackType) {
		return feedbackQuestionDAO.findFeedbackQuestionBySemesterProgramAndType(
				acadSession, acadYear, programId, username, feedbackType);
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseAndType(
			String courseId, String username, String feedbackType) {
		return feedbackQuestionDAO.getFeedbackQuestionListByCourseAndType(
				courseId, username, feedbackType);
	}
	
	public List<FeedbackQuestion> getFeedbackQuestionListByCourseAndTypeForfaculty(
			String courseId, String feedbackType) {
		return feedbackQuestionDAO.getFeedbackQuestionListByCourseAndTypeForfaculty(courseId, 
				feedbackType);
	}
	
	public List<FeedbackQuestion> findFeedbackQuestionByCourseListAndType(String username,Set<Long> courseIds, String feedbackType) {
		return feedbackQuestionDAO.findFeedbackQuestionByCourseListAndType(username, courseIds, 
				feedbackType);
	}

}
