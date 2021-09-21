package com.spts.lms.services.assignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.assignment.AssignmentQuestion;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentQuestionDAO;
import com.spts.lms.services.BaseService;

@Service("assignmentQuestionService")
public class AssignmentQuestionService extends BaseService<AssignmentQuestion>{

	@Autowired
	AssignmentQuestionDAO assignmentQuestionDAO;
	
	
	@Override
	protected BaseDAO<AssignmentQuestion> getDAO() {
		// TODO Auto-generated method stub
		return assignmentQuestionDAO;
	}
	
	public List<AssignmentQuestion> getAssgnQuestionsByAssignmentId(Long assignmentId,String questnType){
		return assignmentQuestionDAO.getAssgnQuestionsByAssignmentId(assignmentId,questnType);
	}
	
	public List<AssignmentQuestion> getTestPoolsByAssignmentId(Long assignmentId){
		return assignmentQuestionDAO.getTestPoolsByAssignmentId(assignmentId);
	}
	
	public List<AssignmentQuestion> getAssgnQuestionToValidate(Long assignmentId,String questionType){
		return assignmentQuestionDAO.getAssgnQuestionToValidate(assignmentId, questionType);
	}
	
	public List<AssignmentQuestion> findByAssignmentId(Long assignmentId) {
		return assignmentQuestionDAO.findByAssignmentId(assignmentId);
	}
	
	public void deleteAlocatedQuestion(long assignmentId) {
		assignmentQuestionDAO.deleteAlocatedQuestion(assignmentId);
	}

}
