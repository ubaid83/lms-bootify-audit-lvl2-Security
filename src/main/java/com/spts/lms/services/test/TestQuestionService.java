package com.spts.lms.services.test;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.TestQuestion;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.TestQuestionDAO;
import com.spts.lms.services.BaseService;

@Service("testQuestionService")
@Transactional
public class TestQuestionService extends BaseService<TestQuestion> {

	@Autowired
	private TestQuestionDAO testQuestionDAO;

	@Override
	protected BaseDAO<TestQuestion> getDAO() {
		return testQuestionDAO;
	}

	@Override
	public int insertWithIdReturn(TestQuestion bean) {
		validateForInsert(bean);
		return super.insertWithIdReturn(bean);
	}

	public int getSumOfTestQuestnMarksByTestId(Long id) {
		return testQuestionDAO.getSumOfTestQuestnMarksByTestId(id);
	}
	
	public double getSumOfTestQuestionMarksByTestId(Long id) {
		return testQuestionDAO.getSumOfTestQuestionMarksByTestId(id);
	}

	private void validateForInsert(TestQuestion bean) {
		// Check if correct option is chosen
		if (bean.getCorrectOption() == null
				|| bean.getCorrectOption().trim().isEmpty()) {
			throw new ValidationException(
					"Must select atleast one answer as correct answer");
		}

	}

	public int findTestIdByTestQuestionId(String testQuestionId) {
		return testQuestionDAO.findTestIdByTestQuestionId(testQuestionId);
	}

	public List<TestQuestion> findByTestId(Long testId) {
		return testQuestionDAO.findByTestId(testId);
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsername(
			Long testId, String username) {
		return testQuestionDAO.findStudentResponseByTestIdAndUsername(testId,
				username);
	}

	public List<TestQuestion> findResponseByTestIdAndUsernameToEvaluate(
			Long testId, String username) {
		return testQuestionDAO.findResponseByTestIdAndUsernameToEvaluate(
				testId, username);
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsernameOnlyAttemptedQuestions(
			Long testId, String username) {
		return testQuestionDAO
				.findStudentResponseByTestIdAndUsernameOnlyAttemptedQuestions(
						testId, username);
	}

	public int deleteByTestId(String testId) {
		return testQuestionDAO.deleteByTestId(testId);
	}

	public List<TestQuestion> findTestDetailsQuestionWise(Long testId) {
		return testQuestionDAO.findTestDetailsQuestionWise(testId);
	}

	public List<TestQuestion> findTestDetailsQuestionWiseByUsername(
			Long testId, String username) {
		return testQuestionDAO.findTestDetailsQuestionWiseByUsername(testId,
				username);
	}

	public List<TestQuestion> findStudentResponseByTestIdAndUsernameForIncompleteTest(
			Long testId, String username) {
		return testQuestionDAO
				.findStudentResponseByTestIdAndUsernameForIncompleteTest(
						testId, username);
	}

	public int updateTestQuestionsAfterTest(TestQuestion testQuestion) {
		return testQuestionDAO.updateTestQuestionsAfterTest(testQuestion);
	}
	
	
	public List<TestQuestion> findTestDetailsQuestionWiseByUsernameAttemptWise( Long testId, String username ) {
		return testQuestionDAO.findTestDetailsQuestionWiseByUsernameAttemptWise(testId, username);
	}
	
	public List<TestQuestion> findTestDetailsQuestionWiseAndAttemptWise( Long testId ) {
		return testQuestionDAO.findTestDetailsQuestionWiseAndAttemptWise(testId);
	}
	
	public List<TestQuestion> findResponseByTestIdAndUsernameToEvaluateForMix(
			Long testId, String username) {
		return testQuestionDAO.findResponseByTestIdAndUsernameToEvaluateForMix(testId, username);
	}
	
	public int updateStudentMarks(TestQuestion testQuestion) {
		return testQuestionDAO.updateStudentMarks(testQuestion);
	}
	
	
	
	public int updateStudentMarksAudit(TestQuestion testQuestion) {
		
		return testQuestionDAO.updateStudentMarksAudit(testQuestion);
	}
	
	
	public int updateStudentMarksToZero(TestQuestion testQuestion) {
		return testQuestionDAO.updateStudentMarksToZero(testQuestion);
	}
	
	
	public int updateStudentMarksToZeroAudit(TestQuestion testQuestion) {
		return testQuestionDAO.updateStudentMarksToZeroAudit(testQuestion);
	}
	
	public List<TestQuestion> findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(
			Long testId, String username) {
		return testQuestionDAO
				.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(
						testId, username);
	}
	
	public List<TestQuestion> findTestCreatedByAdminQuestionWise(String testId,List<String> courseIds) {
		return testQuestionDAO.findTestCreatedByAdminQuestionWise(testId,courseIds);
	}

	public List<TestQuestion> findTestByAdminDetailsQuestionWiseAndAttemptWise( String testId,List<String> courseIds ) {
		return testQuestionDAO.findTestByAdminDetailsQuestionWiseAndAttemptWise(testId,courseIds);
	}
	//New Pool Changes
	public List<TestQuestion> getQuestionsByPoolConfiguration(long testId,long testPoolId,double marks){
		return testQuestionDAO.getQuestionsByPoolConfiguration(testId,testPoolId,marks);
	}
}
