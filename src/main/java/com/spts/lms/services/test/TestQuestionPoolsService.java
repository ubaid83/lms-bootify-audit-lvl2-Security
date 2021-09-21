package com.spts.lms.services.test;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.test.TestQuestionPools;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.TestQuestionPoolsDAO;
import com.spts.lms.services.BaseService;
@Service("testQuestionPoolsService")
public class TestQuestionPoolsService extends BaseService<TestQuestionPools>{
	@Autowired
	TestQuestionPoolsDAO testQuestionPoolsDAO;
	
	@Override
	protected BaseDAO<TestQuestionPools> getDAO() {
		// TODO Auto-generated method stub
		return testQuestionPoolsDAO;
	}

	public List<TestQuestionPools> findByTestPoolId(String testPoolId){
		
		return testQuestionPoolsDAO.findByTestPoolId(testPoolId);
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestType(String testId,String testPoolId,String testType){
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndTestType(testId,testPoolId,testType);
	}
	
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolId(String testPoolId){
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolId(testPoolId);
	}
	
	
	public List<TestQuestionPools> findAllByTestPoolIdAndQuestionType(String testPoolId,String questionType){
		return testQuestionPoolsDAO.findAllByTestPoolIdAndQuestionType(testPoolId,questionType);
	}
	
	public List<Map<String, Object>> findByListOfIds(List<String> listOfIds){
		return testQuestionPoolsDAO.findByListOfIds(listOfIds);
		
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolId(
			String testId, String testPoolId) {
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolId(testId, testPoolId);
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndMarks(
			String testId, String testPoolId, double marks) {
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndMarks(testId, testPoolId, marks);
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestTypeAndMarks(
			String testId, String testPoolId, String testType, double marks) {
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarks(testId, testPoolId, testType, marks);
	}
	
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndMarksList(
			String testId, String testPoolId, String marks) {


		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndMarksList(testId, testPoolId, marks);
	}
	
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestTypeAndMarksList(
			String testId, String testPoolId, String testType, String marks) {
		
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarksList(testId, testPoolId, testType, marks);
	}

	/* For Assignment Pool Start */
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestTypeForAssignment(
			String assignmentId, String testPoolId, String questionType) {
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndTestTypeForAssignment(assignmentId, testPoolId, questionType);
	}
	//New Pool Changes
	public List<TestQuestionPools> findAllTestQuestionsByTestPoolIdAndTestPoolConfigurationAndTestType(
			String testId, String testPoolId, String testType) {
		
		return testQuestionPoolsDAO.findAllTestQuestionsByTestPoolIdAndTestPoolConfigurationAndTestType(testId, testPoolId, testType);
	}
	//New Pool Changes
}
