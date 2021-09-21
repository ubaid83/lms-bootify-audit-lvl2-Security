package com.spts.lms.services.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.group.StudentGroup;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.message.StudentMessageDAO;
import com.spts.lms.services.BaseService;

@Service("studentMessageService")
public class StudentMessageService extends BaseService<StudentMessage> {

	@Autowired
	private StudentMessageDAO studentMessageDAO;

	@Autowired
	private AssignmentDAO assignmentDAO;

	@Override
	public BaseDAO<StudentMessage> getDAO() {
		return studentMessageDAO;
	}

	public int getNoOfStudentsAllocated(Long id) {
		return studentMessageDAO.getNoOfStudentsAllocated(id);
	}

	public List<StudentMessage> getStudentsForMessage(Long messageId,
			Long courseId) {
		return studentMessageDAO.getStudentsForMessageForAllocation(messageId,
				courseId);
	}

	public List<StudentMessage> getUsersForMessage(Long messageId) {
		return studentMessageDAO.getUsersForMessage(messageId);
	}

	public List<StudentMessage> getUsersForMessageByCourse(
			Long courseId) {
		return studentMessageDAO
				.getUsersForMessageByCourse( courseId);
	}

	public List<StudentMessage> findByUserMessage(String username) {
		return studentMessageDAO.findByUserMessage(username);
	}

	public List<StudentMessage> findByCreatedBy(String username,
			String messageRepliedBy) {
		return studentMessageDAO.findByCreatedBy(username, messageRepliedBy);
	}

}
