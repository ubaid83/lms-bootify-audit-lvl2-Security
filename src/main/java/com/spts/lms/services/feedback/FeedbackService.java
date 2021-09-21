package com.spts.lms.services.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.feedback.FeedbackDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("feedbackService")
@Transactional
public class FeedbackService extends BaseService<Feedback> {

	@Autowired
	private FeedbackDAO feedbackDAO;

	@Override
	protected BaseDAO<Feedback> getDAO() {
		return feedbackDAO;
	}

	public List<Feedback> findAllActiveWithCourse() {
		return feedbackDAO.findAllActiveWithCourse();
	}

	public List<Feedback> findAllValidFeedback() {
		return feedbackDAO.findAllValidFeedback();
	}

	public List<Feedback> findByUser(String username, String acadMonth,
			String acadYear) {
		return feedbackDAO.findByUser(username, acadMonth, acadYear);
	}

	public List<Feedback> findByUserAndCourse(String username, Long courseId,
			String acadMonth, String acadYear) {
		return feedbackDAO.findByUserAndCourse(username, courseId, acadMonth,
				acadYear);
	}

	public Page<Feedback> searchStudentFeedback(String username, int pageNo,
			int pageSize) {
		return feedbackDAO.searchStudentFeedback(username, pageNo, pageSize);
	}

	public List<Feedback> findfeedbackByProgramId(Long programId,
			String username) {

		List<Feedback> finalResult = new ArrayList<Feedback>();

		finalResult.addAll(feedbackDAO.findfeedbackNotAssigned(username));
		finalResult.addAll(feedbackDAO.findAllocatedfeedbackByProgramId(
				programId, username));

		return finalResult;
	}

	public Feedback getDatesForFeedback(Long feedbackId) {

		return feedbackDAO.getDatesForFeedback(feedbackId);
	}

	/*public List<Feedback> findAllFeedbacksByAcadSesionAndYear(
			List<String> acadSessionList, String acadYear) {
		return feedbackDAO.findAllFeedbacksByAcadSesionAndYear(acadSessionList,
				acadYear);
	}*/
	
	public List<Feedback> findAllFeedbacksByAcadSesionAndYear(
			List<String> acadSessionList, String acadYear, String username) {
		return feedbackDAO.findAllFeedbacksByAcadSesionAndYear(acadSessionList,
				acadYear, username);
	}

	public void publishFeedback(Long feedbackId) {
		feedbackDAO.publishFeedback(feedbackId);
	}

	public List<Feedback> getAllAllocatedFacultiesByFeedbackId(String feedbackId) {

		return feedbackDAO.getAllAllocatedFacultiesByFeedbackId(feedbackId);

	}

	
	public Feedback getDatesForFeedbackForCompleted(Long feedbackId) {
		return feedbackDAO.getDatesForFeedbackForCompleted(feedbackId);
	}
	
	/*public List<Feedback> findAllActiveByUsername(String campusId,
			String acadYear1, String acadYear2) {
		return feedbackDAO.findAllActiveByUsername(campusId, acadYear1, acadYear2);
	}*/
	
	public List<Feedback> findAllActiveByUsername(String campusId,
			String acadYear1, String acadYear2, String username) {
		return feedbackDAO.findAllActiveByUsername(campusId, acadYear1, acadYear2, username);
	}
	
	public List<Feedback> findAllActiveByCreatedBy(String username) {
        return feedbackDAO.findAllActiveByCreatedBy(username);
	}
	
	//amey 14-10-2020
	public List<Feedback> findAllActiveByCreatedByAndType(String username, String feedbackType) {
		return feedbackDAO.findAllActiveByCreatedByAndType(username, feedbackType);
	}
	
	public List<Feedback> findAllActiveByUsernameAndFeedbackType(String campusId,
			String acadYear1, String acadYear2, String username, String feedbackType) {
		return feedbackDAO.findAllActiveByUsernameAndFeedbackType(campusId, acadYear1, acadYear2, 
				username, feedbackType);
	}

}
