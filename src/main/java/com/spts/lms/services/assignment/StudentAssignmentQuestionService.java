package com.spts.lms.services.assignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.spts.lms.beans.assignment.StudentAssignmentQuestion;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.StudentAssignmentQuestionDAO;
import com.spts.lms.services.BaseService;

@Service("studentAssignmentQuestionService")
@Transactional
public class StudentAssignmentQuestionService extends BaseService<StudentAssignmentQuestion> {
	
	@Autowired
	private StudentAssignmentQuestionDAO studentAssignmentQuestionDAO;

	@Override
	protected BaseDAO<StudentAssignmentQuestion> getDAO() {
		return studentAssignmentQuestionDAO;
	}
	
	public List<StudentAssignmentQuestion> getAllByAssignIdAndUsername(String id, String username){
		return studentAssignmentQuestionDAO.getAllByAssignIdAndUsername(id, username);
	}
	
	public void deleteAssignQuesByAssignment(Long studentAssignmentId) {
		studentAssignmentQuestionDAO.deleteAssignQuesByAssignment(studentAssignmentId);
	}
	
	public List<StudentAssignmentQuestion> getAllByAssignIdAndUsernameForNonRandom(String id, String username){
		return studentAssignmentQuestionDAO.getAllByAssignIdAndUsernameForNonRandom(id, username);
	}

}
