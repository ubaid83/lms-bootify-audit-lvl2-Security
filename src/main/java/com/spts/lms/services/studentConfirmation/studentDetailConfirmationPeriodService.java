package com.spts.lms.services.studentConfirmation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.ica.PredefinedIcaComponent;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmationPeriod;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.studentConfirmation.studentDetailConfirmationPeriodDAO;
import com.spts.lms.services.BaseService;

@Service("studentDetailConfirmationPeriodService")
public class studentDetailConfirmationPeriodService extends
		BaseService<studentDetailConfirmationPeriod> {

	@Autowired
	studentDetailConfirmationPeriodDAO studentDetailConfirmationPeriodDAO;

	@Override
	protected BaseDAO<studentDetailConfirmationPeriod> getDAO() {
		// TODO Auto-generated method stub
		return studentDetailConfirmationPeriodDAO;
	}

	public List<studentDetailConfirmationPeriod> findAllActiveDates(String username) {
		return studentDetailConfirmationPeriodDAO
				.studentDetailConfirmationPeriod(username);
	}

	public void inactiveEndDate(String id) {
		studentDetailConfirmationPeriodDAO.inactiveEndDate(id);
	}

/*	public studentDetailConfirmationPeriod findByEndDate() {
		return studentDetailConfirmationPeriodDAO.findByEndDate();
	}
*/
/*	public studentDetailConfirmationPeriod findByEndDateForSchool(
			String endDate, String programId) {
		return studentDetailConfirmationPeriodDAO.findByEndDateForSchool(
				endDate, programId);
	}*/

	public List<studentDetailConfirmationPeriod> validationgStudentEndDate() {
		return studentDetailConfirmationPeriodDAO.validationgStudentEndDate();
	}

	public String findDateDifference() {
		return studentDetailConfirmationPeriodDAO.findDateDifference();
	}
	
	public String showMastervalidationAlert(String username) {
		return studentDetailConfirmationPeriodDAO.showMastervalidationAlert(username);
	}
	
	public List<String> findProgramForValidation()
	{
		return studentDetailConfirmationPeriodDAO.findProgramForValidation();
	}
	
	
/*	public int findDateDiffrenceCount()
	{
		return studentDetailConfirmationPeriodDAO.findDateDiffrenceCount();
	}*/
	/*
	 * public String findAllActiveDatesForDisableBtn(){ return
	 * studentDetailConfirmationPeriodDAO.findAllActiveDatesForDisableBtn(); }
	 */
	public String findMaximumDate(){
		return studentDetailConfirmationPeriodDAO.findMaximumDate();
	}
	
	public String findProgramidForSchool(String username)
	{
		return studentDetailConfirmationPeriodDAO.findProgramidForSchool(username);
	}
	
/*	public String  findDateDifferenceForAll()
	{
		return studentDetailConfirmationPeriodDAO.findDateDifferenceForAll();
	}*/
	
public List<String> findIfValueIsExist(){
	return studentDetailConfirmationPeriodDAO.findIfValueIsExist();
}

public List<studentDetailConfirmationPeriod> getAcadSessionByProgramId(
		String masterProgramId) {
	return studentDetailConfirmationPeriodDAO.getAcadSessionByProgramId(masterProgramId);
}



	public studentDetailConfirmationPeriod findbyProgramId(String programId,String usersession,String campusId) {
	
		return studentDetailConfirmationPeriodDAO.findbyProgramId(programId,usersession,campusId);
	}
	
	public List<studentDetailConfirmationPeriod> fingAllActiveProgramWithSemester() {
		// TODO Auto-generated method stub
		return studentDetailConfirmationPeriodDAO.fingAllActiveProgramWithSemester();
	}
	


	
}
