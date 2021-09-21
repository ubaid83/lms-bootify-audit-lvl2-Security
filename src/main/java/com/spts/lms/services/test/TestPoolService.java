package com.spts.lms.services.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.test.TestPool;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.test.TestPoolDAO;
import com.spts.lms.services.BaseService;
@Service("testPoolService")
public class TestPoolService extends BaseService<TestPool>{
	@Autowired
	TestPoolDAO testPoolDAO;
	
	@Override
	protected BaseDAO<TestPool> getDAO() {
		// TODO Auto-generated method stub
		return testPoolDAO;
	}
	
	public List<TestPool> findAllTestPoolsByUser(String username,String role){
		
		
		
		return testPoolDAO.findAllTestPoolsByUser(username,role);
	}
	
	public List<TestPool> findAllTestPoolsByUserAndCourse(String username,String courseId){
		
		
		
		return testPoolDAO.findAllTestPoolsByUserAndCourse(username,courseId);
	}

	public List<TestPool> findAllTestPoolsByUserAndSameMarks(String username, double marks,String role) {
	
		return testPoolDAO.findAllTestPoolsByUserAndSameMarks(username, marks,role);
	}
	
	public List<TestPool> findAllTestPoolsByUserAndDiffMarks(String username, String marks,String role) {
		
		return testPoolDAO.findAllTestPoolsByUserAndDiffMarks(username, marks,role);
	}

	public List<TestPool> findAllTestPoolsByUserForAdmin(String username) {
		return testPoolDAO.findAllTestPoolsByUserForAdmin(username);
	}

	/* For Assignment Pool Start */
	public List<TestPool> findAllTestPoolsByUserAdminForAssignment(long assignmentId, String username) {
		return testPoolDAO.findAllTestPoolsByUserAdminForAssignment(assignmentId,username);
	}
	
	public List<TestPool> findAllTestPoolsByAssignmentId(String assignmentId, String username) {
		return testPoolDAO.findAllTestPoolsByAssignmentId(assignmentId, username);
	}
	/* For Assignment Pool Start */
	
	//New Pool Changes
	public List<TestPool> findAllTestPoolsByUserAndTestPoolConfig(String username) {
		
		return testPoolDAO.findAllTestPoolsByUserAndTestPoolConfig(username);
	}
	//New Pool Changes
}
