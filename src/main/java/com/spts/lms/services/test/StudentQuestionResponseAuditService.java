package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.beans.test.StudentQuestionResponseAudit;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.StudentQuestionResponseAuditDAO;
import com.spts.lms.daos.test.StudentQuestionResponseDAO;
import com.spts.lms.services.BaseService;

@Service("studentQuestionResponseAuditService")
@Transactional
public class StudentQuestionResponseAuditService extends BaseService<StudentQuestionResponseAudit> {
	
	@Autowired
	private StudentQuestionResponseAuditDAO studentQuestionResponseAuditDAO;

	@Override
	protected BaseDAO<StudentQuestionResponseAudit> getDAO() {
		return studentQuestionResponseAuditDAO;
	}
	
	public int deleteFacultyTestResponseAudit(String testId,String username) {
		return studentQuestionResponseAuditDAO.deleteFacultyTestResponseAudit(testId, username);
	}
	//17-09-2020 Hiren
	public StudentQuestionResponseAudit findByStudentUsernameAndTestQuestnIdAndAttempts(long studentTestId,String username,long questnId,int attempts){
		return studentQuestionResponseAuditDAO.findByStudentUsernameAndTestQuestnIdAndAttempts(studentTestId,username,questnId,attempts);
	}
	
	public void updateMarksForQuestionResponseAudit(double marks,long id) {
		studentQuestionResponseAuditDAO.updateMarksForQuestionResponseAudit(marks,id);
	}
	
	public List<StudentQuestionResponseAudit> findByQuesId(long qId) {
		return studentQuestionResponseAuditDAO.findByQuesId(qId);
	}
	
}
