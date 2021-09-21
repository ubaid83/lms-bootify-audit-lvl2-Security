package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.TestConfigurationDAO;
import com.spts.lms.services.BaseService;

@Service("testConfigurationService")
@Transactional
public class TestConfigurationService extends BaseService<TestConfiguration> {

	@Autowired
	TestConfigurationDAO testConfigurationDAO;
	
	@Override
	protected BaseDAO<TestConfiguration> getDAO() {
		
		return testConfigurationDAO;
	}
	
	public List<TestConfiguration> findAllByTestId(Long testId) {
		
		return testConfigurationDAO.findAllByTestId(testId);
	}
	
	public int deleteByTestId(String testId) {
		
		return testConfigurationDAO.deleteByTestId(testId);
	}

}
