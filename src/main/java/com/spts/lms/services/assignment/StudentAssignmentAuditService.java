package com.spts.lms.services.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.daos.BaseDAO;

import com.spts.lms.daos.assignment.StudentAssignmentAuditDAO;

import com.spts.lms.services.BaseService;

@Service("studentAssignmentAuditService")
@Transactional
public class StudentAssignmentAuditService extends BaseService<StudentAssignment> {


	@Autowired
	private StudentAssignmentAuditDAO studentAssignmentAuditDAO;
	

	@Override
	public BaseDAO<StudentAssignment> getDAO() {
		return studentAssignmentAuditDAO;
	}
	
	public StudentAssignment findAssignmentSubmission(String userName,
			Long assignmentId) {
		return studentAssignmentAuditDAO.findAssignmentSubmission(userName,
				assignmentId);
	}
	
}
