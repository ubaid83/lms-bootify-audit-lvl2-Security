package com.spts.lms.services.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.student.StudentProgramSession;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.student.StudentProgramSessionDAO;
import com.spts.lms.services.BaseService;

@Service("studentProgramSessionService")
public class StudentProgramSessionService extends BaseService<StudentProgramSession> {
	
	@Autowired
	StudentProgramSessionDAO studentProgramSessionDAO;

	@Override
	protected BaseDAO<StudentProgramSession> getDAO() {
		return studentProgramSessionDAO;
	}

}
