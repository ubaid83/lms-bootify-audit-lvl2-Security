package com.spts.lms.services.StudentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.StudentService.StudentServiceBean;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.StudentServiceDAO;
import com.spts.lms.services.BaseService;

@Service("studentService")
public class StudentService extends BaseService<StudentServiceBean> {

	@Autowired
	StudentServiceDAO studentServiceDAO;

	@Override
	protected BaseDAO<StudentServiceBean> getDAO() {
		// TODO Auto-generated method stub
		return studentServiceDAO;
	}

	public List<StudentServiceBean> findAllAvail() {
		return studentServiceDAO.findAllAvail();
	}

	public List<StudentServiceBean> findServiceByName(String name) {
		return studentServiceDAO.findServiceByName(name);
	}

}
