package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.StudentTestAuditDAO;
import com.spts.lms.services.BaseService;

@Service("studentTestAuditService")
@Transactional
public class StudentTestAuditService  extends BaseService<StudentTest> {

	@Autowired
	private StudentTestAuditDAO studentTestAuditDAO;
	
	@Autowired
	@Override
	protected BaseDAO<StudentTest> getDAO() {
		return studentTestAuditDAO;
	}
	
	public int insertStudentTest(StudentTest st){
		return studentTestAuditDAO.insertStudentTest(st);
	}
	
	public int upsert(StudentTest bean) {
		return super.insert(bean);
	}
	

	public List<StudentTest> findByTestIdAndUsername(Long id,String username){
		
		return studentTestAuditDAO.findByTestIdAndUsername(id,username);
	}
	
}
