package com.spts.lms.services.studentConfirmation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmationPeriod;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.studentConfirmation.studentDetailConfirmationDAO;
import com.spts.lms.services.BaseService;

@Service("studentDetailConfirmationService")
public class studentDetailConfirmationService extends BaseService<studentDetailConfirmation>{

	@Autowired
	studentDetailConfirmationDAO studentDetailConfirmationDAO;
	
	@Override
	protected BaseDAO<studentDetailConfirmation> getDAO() {
		// TODO Auto-generated method stub
		return studentDetailConfirmationDAO;
	}
	
	public studentDetailConfirmation findByUserNamevalidate(String username)
	{
		return studentDetailConfirmationDAO.findByUserNamevalidate(username);
	}
	
	public List<studentDetailConfirmation> findStatusListForAll(String username){
		 return studentDetailConfirmationDAO.findStatusListForAll(username);
	}
	
	public List<studentDetailConfirmation> findStatusListForAllStudent(String username){
		 return studentDetailConfirmationDAO.findStatusListForAllStudent(username);
	}

	public void updateMasterStatus(String username,String status,String remarks) {
		  studentDetailConfirmationDAO.updateMasterStatus(username,status,remarks);
		
	}

	public studentDetailConfirmation findAllUserName(String username) {
		
		return studentDetailConfirmationDAO.findAllUserName(username);
	}

	public List<String> findAllActiveStudent() {
		return studentDetailConfirmationDAO.findAllActiveStudent();
	}


public List<studentDetailConfirmation> findActiveStudentForSupportAdmin() {
		
		return studentDetailConfirmationDAO.findActiveStudentForSupportAdmin();
	}

	public void softDeleteById(String username) {
	
		studentDetailConfirmationDAO.softDeleteById(username);
	}


	
/*	public List<studentDetailConfirmation> findAllSecurityQuestion()
	{
		return studentDetailConfirmationDAO.findAllSecurityQuestion();
	}
	*/


}
