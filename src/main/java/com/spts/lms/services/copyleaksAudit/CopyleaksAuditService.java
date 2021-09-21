package com.spts.lms.services.copyleaksAudit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.copyleaksAudit.CopyleaksAudit;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.copyleaksAudit.CopyleaksAuditDAO;
import com.spts.lms.services.BaseService;


@Service("copyleaksAuditService")
@Transactional
public class CopyleaksAuditService extends BaseService<CopyleaksAudit> {

	@Autowired
	CopyleaksAuditDAO copyleaksAuditDAO; 
	
	@Override
	protected BaseDAO<CopyleaksAudit> getDAO() {
		
		return copyleaksAuditDAO;
	}
	
	public CopyleaksAudit getRecordByUsername(String username, long assignmentId) {
		
		return copyleaksAuditDAO.getRecordByUsername(username, assignmentId);
	}
public CopyleaksAudit getRecordByUsernameForModule(String username, List<Long> assignmentId) {
		
		return copyleaksAuditDAO.getRecordByUsernameForModule(username, assignmentId);
	}

	public List<CopyleaksAudit> getRecordsByUsername(String username) {	
		return copyleaksAuditDAO.getRecordsByUsername(username);
	}
	
	public void deleteRecordById(String id) {
		copyleaksAuditDAO.deleteRecordById(id);
	}

}
