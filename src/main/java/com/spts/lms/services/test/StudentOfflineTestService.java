package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.test.StudentOfflineTest;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.StudentOfflineTestDAO;
import com.spts.lms.services.BaseService;

@Service("studentOfflineTestService")
public class StudentOfflineTestService extends BaseService<StudentOfflineTest>{
	
	@Autowired
	StudentOfflineTestDAO studentOfflineTestDao;
	
	@Override
	protected BaseDAO<StudentOfflineTest> getDAO() {
		// TODO Auto-generated method stub
		return studentOfflineTestDao;
	}

	public List<StudentOfflineTest> findByOfflineTestId(String offlineTestId){
		
		return studentOfflineTestDao.findByOfflineTestId(offlineTestId);
		
	}
}
