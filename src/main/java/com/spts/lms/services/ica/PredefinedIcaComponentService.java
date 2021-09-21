package com.spts.lms.services.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.PredefinedIcaComponent;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.PredefinedIcaComponentDAO;
import com.spts.lms.services.BaseService;
@Service("predefinedIcaComponentService")
@Transactional
public class PredefinedIcaComponentService extends BaseService<PredefinedIcaComponent>{

	@Autowired 
	
	PredefinedIcaComponentDAO predefinedIcaComponentDAO;
	
	@Override
	protected BaseDAO<PredefinedIcaComponent> getDAO() {
		// TODO Auto-generated method stub
		return predefinedIcaComponentDAO;
	}
	
	public PredefinedIcaComponent findByComponentName(String comName)
	{
		return predefinedIcaComponentDAO.findByComponentName(comName);
	}



}
