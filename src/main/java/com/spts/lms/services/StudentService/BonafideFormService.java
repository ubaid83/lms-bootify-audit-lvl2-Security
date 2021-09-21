package com.spts.lms.services.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.StudentService.BonafideForm;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.BonafideFormDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.services.notification.Notifier;

@Service("bonafideFormService")
public class BonafideFormService extends BaseService<BonafideForm> {
	@Autowired
	BonafideFormDAO bonafideFormDAO;
	
	@Autowired
	Notifier notifier;

	@Override
	protected BaseDAO<BonafideForm> getDAO() {
		// TODO Auto-generated method stub
		return bonafideFormDAO;
	}

	public List<BonafideForm> findAllSubmittedStudentsByServiceId(Long serviceId) {
		return bonafideFormDAO.findAllSubmittedStudentsByServiceId(serviceId);
	}

	public List<BonafideForm> findAllSubmittedStudentsByServiceIdFlag1(
			Long serviceId) {
		return bonafideFormDAO
				.findAllSubmittedStudentsByServiceIdFlag1(serviceId);
	}

	public List<BonafideForm> findAllSubmittedStudentsByServiceIdFlag2(
			Long serviceId) {
		return bonafideFormDAO
				.findAllSubmittedStudentsByServiceIdFlag2(serviceId);
	}

	public void updateFlags(Integer flagNo, String status, Long id) {
		bonafideFormDAO.updateFlags(flagNo, status, id);
	}

	public void updateFlagsAndRemarks(String flagNo, String status,
			String remark, String id) {
		bonafideFormDAO.updateFlagsAndRemarks(flagNo, status, remark, id);
	}

	public BonafideForm findByUsernameAndSubmitted(String username) {
		return bonafideFormDAO.findByUsernameAndSubmitted(username);
	}
	
	public BonafideForm findByUsernameAndSaved(String username) {
		return bonafideFormDAO.findByUsernameAndSaved(username);
	}

	public BonafideForm getSubmittedStudentById(Long id) {
		return bonafideFormDAO.getSubmittedStudentById(id);
	}

	public void updateSubmit(Long id) {
		bonafideFormDAO.updateSubmit(id);
	}

	public void saveBonafideRemarks(String remarkValue, String remarkCol,
			Long pk) {
		bonafideFormDAO.saveBonafideRemarks(remarkValue, remarkCol, pk);
	}

	public void saveBonafideStatus(String flagValue, String flagCol, Long pk) {
		bonafideFormDAO.saveBonafideStatus(flagValue, flagCol, pk);
	}

	public BonafideForm findStudentBonafide(String username, Long serviceId) {
		return bonafideFormDAO.findStudentBonafide(username, serviceId);
	}
	
	public List<BonafideForm> findAllPendingBonafides(){
		return bonafideFormDAO.findAllPendingBonafides();
	}
	public List<BonafideForm> findAllPendingBonafidesForLevel3(String username){
		return bonafideFormDAO.findAllPendingBonafidesForLevel3(username);
	}
	
	public void saveBonafideStatusForLevel3(String flagValue,String flagCol, String pk) {
		 bonafideFormDAO.saveBonafideStatusForLevel3(flagValue, flagCol, pk);
	}
	
	public void saveBonafideRemarksForLevel3(String remarkValue,String remarkCol, String pk) {
		bonafideFormDAO.saveBonafideRemarksForLevel3(remarkValue, remarkCol, pk);
	}
	
	public void pendingBonafideEmail(){
		List<BonafideForm> bf = findAllPendingBonafides();
		/*logger.info("Pending list" + bf);*/
		if (bf.size() > 0 ) {
			// notifier
			String email = bf.get(0).getEmail();
			Map<String, String> map = new HashMap();
			Map<String, String> mobile = new HashMap();
			map.put(email, email);
			/*logger.info("Email" + email);*/
			String subject = "Reminder for Bonafide";
			StringBuilder sb = new StringBuilder();
			sb.append("<html><body><table style='border: solid;'>");
			sb.append("<thead><tr>");
			sb.append("<th>FormId</th><th>Username</th><th>First Name</th><th>Last Name</th><th>Roll No</th>");
			sb.append("</tr></thead>");
			sb.append("<tbody><tr>");
			for (BonafideForm b : bf) {
				sb.append("<td>" + b.getId() + "</td>");
				sb.append("<td>" + b.getUsername() + "</td>");
				sb.append("<td>" + b.getFirstname() + "</td>");
				sb.append("<td>" + b.getLastname() + "</td>");
				sb.append("<td>" + b.getRollNo() + "</td>");

			}
			sb.append("</tr></tbody></table></body></html>");
			/*logger.info("table:" + sb);*/
			String msg = "Pending Bonafide Requets:";
			
			msg = msg + sb.toString();
			/*logger.info("Message:" + msg);*/

			Boolean success = notifier.sendEmail(map, mobile, subject, msg);
			if(success){
				for (BonafideForm b : bf) {
					b.setFlag3("1");
				}
				bonafideFormDAO.updateBatch(bf);
			}
		}
	}
}
