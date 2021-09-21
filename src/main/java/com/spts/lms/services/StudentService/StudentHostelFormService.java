package com.spts.lms.services.StudentService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.StudentService.StudentHostelForm;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.StudentHostelFormDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.services.notification.Notifier;

@Service("studentHostelFormService")
public class StudentHostelFormService extends BaseService<StudentHostelForm> {

	@Autowired
	Notifier notifier;

	@Autowired
	StudentHostelFormDAO hostelFormDAO;

	@Override
	protected BaseDAO<StudentHostelForm> getDAO() {
		// TODO Auto-generated method stub
		return hostelFormDAO;
	}

	public List<StudentHostelForm> findAllSubmittedStudentsByServiceId(
			Long serviceId) {
		return hostelFormDAO.findAllSubmittedStudentsByServiceId(serviceId);
	}

	public List<StudentHostelForm> findAllSubmittedStudentsByServiceIdFlag1(
			Long serviceId) {
		return hostelFormDAO
				.findAllSubmittedStudentsByServiceIdFlag1(serviceId);
	}

	public List<StudentHostelForm> findAllSubmittedStudentsByServiceIdFlag2(
			Long serviceId) {
		return hostelFormDAO
				.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
	}

	public void updateFlags(Integer flagNo, String status, Long id) {
		hostelFormDAO.updateFlags(flagNo, status, id);
	}

	public void updateFlagsAndRemarks(String flagNo, String status,
			String remark, String id) {
		hostelFormDAO.updateFlagsAndRemarks(flagNo, status, remark, id);
	}

	public StudentHostelForm findByUsernameAndSubmitted(String username) {
		return hostelFormDAO.findByUsernameAndSubmitted(username);
	}

	public StudentHostelForm findByUsernameAndSaved(String username) {
		return hostelFormDAO.findByUsernameAndSaved(username);
	}

	public StudentHostelForm getSubmittedStudentById(Long id) {
		return hostelFormDAO.getSubmittedStudentById(id);
	}

	public void updateSubmit(Long id) {
		hostelFormDAO.updateSubmit(id);
	}

	public void saveHostelRemarks(String remarkValue, String remarkCol, Long pk) {
		hostelFormDAO.saveHostelRemarks(remarkValue, remarkCol, pk);
	}

	public void saveHostelStatus(String flagValue, String flagCol, Long pk) {
		hostelFormDAO.saveHostelStatus(flagValue, flagCol, pk);
	}

	public StudentHostelForm findStudentHostel(String username, Long serviceId) {
		return hostelFormDAO.findStudentHostel(username, serviceId);
	}

	public List<StudentHostelForm> findAllPendingHostels() {
		return hostelFormDAO.findAllPendingHostels();
	}

	public List<StudentHostelForm> findAllPendingHostelsForLevel3(
			String username) {
		return hostelFormDAO.findAllPendingHostelsForLevel3(username);
	}

	public void saveHostelStatusForLevel3(String flagValue, String flagCol,
			String pk) {
		hostelFormDAO.saveHostelStatusForLevel3(flagValue, flagCol, pk);
	}

	public void saveHostelRemarksForLevel3(String remarkValue,
			String remarkCol, String pk) {
		hostelFormDAO.saveHostelRemarksForLevel3(remarkValue, remarkCol, pk);
	}

}
