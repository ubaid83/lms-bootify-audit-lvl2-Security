package com.spts.lms.services.StudentService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.StudentService.LorRegDetails;
import com.spts.lms.beans.StudentService.LorRegStaff;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.LorRegStaffDAO;
import com.spts.lms.services.BaseService;

@Service("lorRegStaffService")
@Transactional
public class LorRegStaffService extends BaseService<LorRegStaff> {

	@Autowired
	LorRegStaffDAO lorRegStaffDAO;

	@Override
	protected BaseDAO<LorRegStaff> getDAO() {

		return lorRegStaffDAO;
	}

	public void saveApplicationApprovalStatus(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.saveApplicationApprovalStatus(lorRegStaff);
	}

	public void saveDocumentApprovalStatus(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.saveDocumentApprovalStatus(lorRegStaff);
	}

	public void saveLORApprovalStatus(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.saveLORApprovalStatus(lorRegStaff);
	}

	public void saveLorFormatFilePath(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.saveLorFormatFilePath(lorRegStaff);
	}

	public void saveLorFinalFilePath(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.saveLorFinalFilePath(lorRegStaff);
	}

	public void saveExpectedDate(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.saveExpectedDate(lorRegStaff);
	}

	public LorRegStaff findByLorRegIdAndStaffId(String lorRegId, String staffId) {
		return lorRegStaffDAO.findByLorRegIdAndStaffId(lorRegId, staffId);
	}

	public List<String> getAlldept() {
		return lorRegStaffDAO.getAllDept();
	}
	
	public List<String> getAllCountryList() {
		return lorRegStaffDAO.getAllCountryList();
	}
    
	public LorRegStaff findByDepartment(final String department) {
		return lorRegStaffDAO.findByDepartment(department);
		
	}
	
	
	public List<LorRegStaff> getfaculty(String department) {
		return lorRegStaffDAO.getfaculty(department);
	}

	public List<LorRegStaff> getAllApplicationByStudent(String username) {
		return lorRegStaffDAO.getAllApplicationByStudent(username);
	}

	public List<LorRegStaff> getPendingAppicationForApprovalByAdmin(String username) {
		return lorRegStaffDAO.getPendingAppicationForApprovalByAdmin(username);
	}

	public void updateStudentDocumentStatus(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.updateStudentDocumentStatus(lorRegStaff);

	}

	public void updateLORDocumentStatus(LorRegStaff lorRegStaff) {
		lorRegStaffDAO.updateLORDocumentStatus(lorRegStaff);
	}

	public int getIsLorStaff(String facultyId) {
		return lorRegStaffDAO.getIsLorStaff(facultyId);
	}

	public int AddLorDepartMentFaculty(LorRegStaff lorRegStaff)
	{
		return lorRegStaffDAO.AddLorDepartMentFaculty(lorRegStaff);
	}

	public List<LorRegStaff> getPendingAppicationForApprovalByDepartment(String username) {
		return lorRegStaffDAO.getPendingAppicationForApprovalByDepartment(username);
	}

	public LorRegStaff getDepartmentAssistent(String department) {
		return lorRegStaffDAO.getDepartmentAssistent(department);
	}

	public LorRegStaff findByUserName(String staffUsername) {
	
		return lorRegStaffDAO.findByUserName(staffUsername);
	}

}
