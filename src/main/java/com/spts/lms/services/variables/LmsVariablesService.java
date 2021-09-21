package com.spts.lms.services.variables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.variables.LmsVariablesDAO;
import com.spts.lms.services.BaseService;

@Service("lmsVariableServices")
@Transactional
public class LmsVariablesService extends BaseService<LmsVariables>{
	
	@Autowired
	LmsVariablesDAO lmsVariablesDAO;

	@Override
	protected BaseDAO<LmsVariables> getDAO() {
		return lmsVariablesDAO;
	}

	public LmsVariables getLmsVariableBykeyword(String keyword) {
		return lmsVariablesDAO.getLmsVariableBykeyword(keyword);
	}
	
	public int updateLmsVariable(Long id, String value, String active) {
		return lmsVariablesDAO.updateLmsVariable(id, value, active);
		
	}
}
