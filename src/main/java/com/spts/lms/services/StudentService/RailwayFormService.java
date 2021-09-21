package com.spts.lms.services.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.StudentService.RailwayForm;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.RailwayFormDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.services.notification.Notifier;

@Service("railwayFormService")
public class RailwayFormService extends BaseService<RailwayForm> {
	@Autowired
	RailwayFormDAO railwayFormDAO;
	
	@Autowired
	Notifier notifier;

	@Override
	protected BaseDAO<RailwayForm> getDAO() {
		// TODO Auto-generated method stub
		return railwayFormDAO;
	}

	public List<RailwayForm> findAllSubmittedStudentsByServiceId(Long serviceId) {
		return railwayFormDAO.findAllSubmittedStudentsByServiceId(serviceId);
	}

	public List<RailwayForm> findAllSubmittedStudentsByServiceIdFlag1(
			Long serviceId) {
		return railwayFormDAO
				.findAllSubmittedStudentsByServiceIdFlag1(serviceId);
	}

	public List<RailwayForm> findAllSubmittedStudentsByServiceIdFlag2(
			Long serviceId) {
		return railwayFormDAO
				.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
	}

	public void updateFlags(Integer flagNo, String status, Long id) {
		railwayFormDAO.updateFlags(flagNo, status, id);
	}

	public void updateFlagsAndRemarks(String flagNo, String status,
			String remark, String id) {
		railwayFormDAO.updateFlagsAndRemarks(flagNo, status, remark, id);
	}

	public RailwayForm findByUsernameAndSubmitted(String username) {
		return railwayFormDAO.findByUsernameAndSubmitted(username);
	}
	
	public RailwayForm findByUsernameAndSaved(String username) {
		return railwayFormDAO.findByUsernameAndSaved(username);
	}

	public RailwayForm getSubmittedStudentById(Long id) {
		return railwayFormDAO.getSubmittedStudentById(id);
	}

	public void updateSubmit(Long id) {
		railwayFormDAO.updateSubmit(id);
	}

	public void saveRCRemarks(String remarkValue, String remarkCol,
			Long pk) {
		railwayFormDAO.saveRCRemarks(remarkValue, remarkCol, pk);
	}

	public void saveRCStatus(String flagValue, String flagCol, Long pk) {
		railwayFormDAO.saveRCStatus(flagValue, flagCol, pk);
	}

	public RailwayForm findStudentRC(String username, Long serviceId) {
		return railwayFormDAO.findStudentRC(username, serviceId);
	}
	
	public List<RailwayForm> findAllPendingRC(){
		return railwayFormDAO.findAllPendingRC();
	}
	public List<RailwayForm> findAllPendingRCForLevel3(String username){
		return railwayFormDAO.findAllPendingRCForLevel3(username);
	}
	
	public void saveRCStatusForLevel3(String flagValue,String flagCol, String pk) {
		 railwayFormDAO.saveRCStatusForLevel3(flagValue, flagCol, pk);
	}
	
	public void saveRCRemarksForLevel3(String remarkValue,String remarkCol, String pk) {
		railwayFormDAO.saveRCRemarksForLevel3(remarkValue, remarkCol, pk);
	}
	
	public void pendingRCEmail(){
		List<RailwayForm> rf = findAllPendingRC();
		/*logger.info("Pending list" + rf);*/
		if (rf.size() > 0 ) {
			// notifier
			String email = rf.get(0).getEmail();
			Map<String, String> map = new HashMap();
			Map<String, String> mobile = new HashMap();
			map.put(email, email);
			/*logger.info("Email" + email);*/
			String subject = "Reminder for Railway Concession";
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body><table style='border: solid;'>");
			sb.append("<thead><tr>");
			sb.append("<th>FormId</th><th>Username</th><th>First Name</th><th>Last Name</th>");
			sb.append("</tr></thead>");
			sb.append("<tbody><tr>");
			for (RailwayForm r : rf) {
				sb.append("<td>" + r.getId() + "</td>");
				sb.append("<td>" + r.getUsername() + "</td>");
				sb.append("<td>" + r.getFirstname() + "</td>");
				sb.append("<td>" + r.getLastname() + "</td>");
				

			}
			sb.append("</tr></tbody></table></body></html>");
			/*logger.info("table:" + sb);*/
			String msg = "Pending Railway Concession Requets:";
			
			msg = msg + sb.toString();
			/*logger.info("Message:" + msg);*/

			Boolean success = notifier.sendEmail(map, mobile, subject, msg);
			if(success){
				for (RailwayForm r : rf) {
					r.setFlag3("1");
				}
				railwayFormDAO.updateBatch(rf);
			}
		}
	}
}
