package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.beans.test.TestPoolConfiguration;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.TestPoolConfigurationDAO;
import com.spts.lms.services.BaseService;

@Service("testPoolConfigurationService")
@Transactional
public class TestPoolConfigurationService extends BaseService<TestPoolConfiguration> {

	
	@Autowired
	TestPoolConfigurationDAO testPoolConfigurationDAO;
	
	@Override
	protected BaseDAO<TestPoolConfiguration> getDAO() {
		// TODO Auto-generated method stub
		return testPoolConfigurationDAO;
	}
	
	public int deleteByTestId(String testId) {
		
		return testPoolConfigurationDAO.deleteByTestId(testId);
	}
	public List<TestPoolConfiguration> findAllByTestId(Long testId) {
		
		return testPoolConfigurationDAO.findAllByTestId(testId);
	}

}
