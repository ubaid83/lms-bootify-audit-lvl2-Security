package com.spts.lms.services.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaStudentBatchwise;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaStudentBatchwiseDAO;
import com.spts.lms.services.BaseService;

@Service("icaStudentBatchwiseService")
@Transactional
public class IcaStudentBatchwiseService extends BaseService<IcaStudentBatchwise>{
	
	@Autowired
	IcaStudentBatchwiseDAO icaStudentBatchwiseDAO;

	@Override
	protected BaseDAO<IcaStudentBatchwise> getDAO() {
		
		return icaStudentBatchwiseDAO;
	}
	
	public List<IcaStudentBatchwise> getAllByActiveIcaId(String icaId){
		return icaStudentBatchwiseDAO.getAllByActiveIcaId(icaId);
	}
	
	public List<IcaStudentBatchwise> getAllByActiveIcaIdAndFaculty(Long icaId, String facultyId){
		return icaStudentBatchwiseDAO.getAllByActiveIcaIdAndFaculty(icaId, facultyId);
	}
	
	public List<String> getDistinctUsernamesByActiveIcaIdAndFaculty(Long icaId, String facultyId){
		return icaStudentBatchwiseDAO.getDistinctUsernamesByActiveIcaIdAndFaculty(icaId, facultyId);
	}
	
	public List<String> getDistinctUsernamesByActiveIcaId(String icaId){
		return icaStudentBatchwiseDAO.getDistinctUsernamesByActiveIcaId(icaId);
	}
	
	public void deleteAllActiveByIcaId(String icaId){
		icaStudentBatchwiseDAO.deleteAllActiveByIcaId(icaId);
	}
	
	public void deleteSoftByIcaId(String icaId){
		icaStudentBatchwiseDAO.deleteSoftByIcaId(icaId);
	}
	
	public List<String> getDistinctActiveUsernamesOfRemainingFaculty(Long icaId, String facultyId){
        return icaStudentBatchwiseDAO.getDistinctActiveUsernamesOfRemainingFaculty(icaId, facultyId);
	}
	
	public String getFacultyIdByusernameAndIcaId(long icaId,String username) {
		 return icaStudentBatchwiseDAO.getFacultyIdByusernameAndIcaId(icaId,username);
	}

}
