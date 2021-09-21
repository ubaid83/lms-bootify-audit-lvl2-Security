package com.spts.lms.services.studentParent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.BaseBean;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.studentParent.StudentParentDAO;
import com.spts.lms.services.BaseService;

@Service
public class StudentParentService extends BaseService<StudentParent> {

	@Autowired
	private StudentParentDAO studentParentDAO;

	@Override
	public BaseDAO<StudentParent> getDAO() {
		return studentParentDAO;
	}

	public List<StudentParent> findStudentsByParentUname(String uname) {
		return studentParentDAO.findStudentsByParentUname(uname);
	}

	public StudentParent findParentByStudent(String uname) {

		return studentParentDAO.findParentByStudent(uname);
	}

}
