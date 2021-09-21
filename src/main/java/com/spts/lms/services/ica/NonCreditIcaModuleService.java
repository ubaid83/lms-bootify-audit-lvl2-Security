package com.spts.lms.services.ica;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.NonCreditIcaModule;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.NonCreditIcaModuleDAO;
import com.spts.lms.services.BaseService;

@Service("nonCreditIcaModuleService")
@Transactional
public class NonCreditIcaModuleService extends BaseService<NonCreditIcaModule> {
	
	@Autowired
	private NonCreditIcaModuleDAO nonCreditIcaModuleDAO;
	
	@Override
	protected BaseDAO<NonCreditIcaModule> getDAO() {
		
		return nonCreditIcaModuleDAO;
	}
	
	
	public List<NonCreditIcaModule> getModuleIdByProgramId(String programId, String acadYear) {
		return nonCreditIcaModuleDAO.getModuleIdByProgramId(programId, acadYear);
	}
	
}
