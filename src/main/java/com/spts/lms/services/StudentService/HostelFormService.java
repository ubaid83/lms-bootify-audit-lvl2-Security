package com.spts.lms.services.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.StudentService.HostelForm;
import com.spts.lms.beans.StudentService.HostelForm;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.BonafideFormDAO;
import com.spts.lms.daos.StudentService.HostelFormDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.services.notification.Notifier;

@Service("hostelFormService")
public class HostelFormService extends BaseService<HostelForm> {

	@Autowired
	Notifier notifier;

	@Autowired
	HostelFormDAO hostelFormDAO;

	@Override
	protected BaseDAO<HostelForm> getDAO() {
		// TODO Auto-generated method stub
		return hostelFormDAO;
	}

	public List<HostelForm> findAllSubmittedStudentsByServiceId(Long serviceId) {
		return hostelFormDAO.findAllSubmittedStudentsByServiceId(serviceId);
	}

	public List<HostelForm> findAllSubmittedStudentsByServiceIdFlag1(
			Long serviceId) {
		return hostelFormDAO
				.findAllSubmittedStudentsByServiceIdFlag1(serviceId);
	}

	public List<HostelForm> findAllSubmittedStudentsByServiceIdFlag2(
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

	public HostelForm findByUsernameAndSubmitted(String username) {
		return hostelFormDAO.findByUsernameAndSubmitted(username);
	}

	public HostelForm findByUsernameAndSaved(String username) {
		return hostelFormDAO.findByUsernameAndSaved(username);
	}

	public HostelForm getSubmittedStudentById(Long id) {
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

	public HostelForm findStudentHostel(String username, Long serviceId) {
		return hostelFormDAO.findStudentHostel(username, serviceId);
	}

	public List<HostelForm> findAllPendingHostels() {
		return hostelFormDAO.findAllPendingHostels();
	}

	public List<HostelForm> findAllPendingHostelsForLevel3(String username) {
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

	/*
	 * public void pendingHostelEmail(){ List<HostelForm> bf =
	 * findAllPendingHostels(); logger.info("Pending list" + bf); if (bf.size()
	 * > 0 ) { // notifier String email = bf.get(0).getEmail(); Map<String,
	 * String> map = new HashMap(); Map<String, String> mobile = new HashMap();
	 * map.put(email, email); logger.info("Email" + email); String subject =
	 * "Reminder for Bonafide"; StringBuilder sb = new StringBuilder();
	 * sb.append("<html><body><table style='border: solid;'>");
	 * sb.append("<thead><tr>"); sb.append(
	 * "<th>FormId</th><th>Username</th><th>First Name</th><th>Last Name</th><th>Roll No</th>"
	 * ); sb.append("</tr></thead>"); sb.append("<tbody><tr>"); for (HostelForm
	 * b : bf) { sb.append("<td>" + b.getId() + "</td>"); sb.append("<td>" +
	 * b.getUsername() + "</td>"); sb.append("<td>" + b.getFirstname() +
	 * "</td>"); sb.append("<td>" + b.getLastname() + "</td>"); sb.append("<td>"
	 * + b.getRollNo() + "</td>");
	 * 
	 * } sb.append("</tr></tbody></table></body></html>"); logger.info("table:"
	 * + sb); String msg = "Pending Bonafide Requets:";
	 * 
	 * msg = msg + sb.toString(); logger.info("Message:" + msg);
	 * 
	 * Boolean success = notifier.sendEmail(map, mobile, subject, msg);
	 * if(success){ for (HostelForm b : bf) { b.setFlag3("1"); }
	 * hostelFormDAO.updateBatch(bf); } } }
	 */

	public List<String> findHostelNamesByLocation(String location) {
		return hostelFormDAO.findHostelNamesByLocation(location);
	}
	
	public int saveHostelDetails(HostelForm hostelForm, String dbCol, String value) {
		return hostelFormDAO.saveHostelDetails(hostelForm, dbCol, value);
	}
	
	
	
	public void updateByLocation(String username, String year, String location){
		 hostelFormDAO.updateByLocation(username, year, location);
	}
	
	public List<HostelForm> findByLocation(String location){
		return hostelFormDAO.findByLocation(location);
	}
}
