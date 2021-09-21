package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.faq.Faq;
import com.spts.lms.beans.test.OfflineTest;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.OfflineTestDAO;
import com.spts.lms.services.BaseService;

@Service("offlineTestService")
public class OfflineTestService extends BaseService<OfflineTest>{
	
	@Autowired
	OfflineTestDAO offlineTestDao;
	
	@Override
	protected BaseDAO<OfflineTest> getDAO() {
		// TODO Auto-generated method stub
		return offlineTestDao;
	}
	
	public List<OfflineTest> findAllOfflineTestsByUsernameAndCourseId(String username,
			String courseId) {
		
		return offlineTestDao.findAllOfflineTestsByUsernameAndCourseId(username,courseId);
	}
	
	public List<OfflineTest> findAllOfflineTestsByUsername(String username) {
		return offlineTestDao.findAllOfflineTestsByUsername(username);
	}
	
	

}
