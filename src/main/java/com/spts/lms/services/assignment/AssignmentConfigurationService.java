package com.spts.lms.services.assignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.assignment.AssignmentConfiguration;
import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentConfigurationDAO;
import com.spts.lms.services.BaseService;

@Service("assignmentConfigurationService")
@Transactional
public class AssignmentConfigurationService extends BaseService<AssignmentConfiguration>{

	
	@Autowired
	AssignmentConfigurationDAO assignmentConfigurationDao;
	
	@Override
	protected BaseDAO<AssignmentConfiguration> getDAO() {
		// TODO Auto-generated method stub
		return assignmentConfigurationDao;
	}

	public List<AssignmentConfiguration> findAllByAssignmentId(Long assignmentId) {
		
		return assignmentConfigurationDao.findAllByAssignmentId(assignmentId);
	}
	
	public int deleteByAssignmentId(String assignmentId) {
		
		return assignmentConfigurationDao.deleteByAssignmentId(assignmentId);
	}
	
	//For Assignment Pool Start
	public int insertAssignmentPoolConfiguration(AssignmentConfiguration ac) {
		return assignmentConfigurationDao.insertAssignmentPoolConfiguration(ac);
	}
	public int deleteByAssignmentPoolConfigurationAssignmentId(String assignmentId) {
		return assignmentConfigurationDao.deleteByAssignmentPoolConfigurationAssignmentId(assignmentId);
	}
	
	public List<AssignmentConfiguration> getAllPoolConfigByAssignmentId(long assignmentId){
		return assignmentConfigurationDao.getAllPoolConfigByAssignmentId(assignmentId);
	}

	public List<AssignmentConfiguration> findPoolByAssignmentId(Long assignmentId) {
		return assignmentConfigurationDao.findPoolByAssignmentId(assignmentId);
	}
}
