package com.spts.lms.services.calender;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.calender.StudentEvent;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.calender.StudentEventDAO;
import com.spts.lms.services.BaseService;

@Service("studentEventService")
@Transactional
public class StudentEventService extends BaseService<StudentEvent> {
	
	@Autowired
	private StudentEventDAO studentEventDAO;

	
	@Autowired
	@Override
	protected BaseDAO<StudentEvent> getDAO() {
		return studentEventDAO;
	}
	
	public StudentEvent findByEventIdAndUsername(String testId,String username) {
		return studentEventDAO.findByEventIdAndUsername(testId,username);
	}
	
	public List<StudentEvent> getStudentForEvents(Long eventId, Long courseId) {
		return studentEventDAO.getStudentForEvents(eventId, courseId);
	}
	
	public List<StudentEvent> getStudentsByEventId(Long eventId) {
		return studentEventDAO.getStudentsByEventId(eventId);
	}
	

}
