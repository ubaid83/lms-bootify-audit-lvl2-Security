package com.spts.lms.services.ica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.ica.IcaComponentQueries;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaComponentQueriesDAO;
import com.spts.lms.services.BaseService;

@Service
public class IcaComponentQueriesService extends BaseService<IcaComponentQueries>{
	
	@Autowired
	IcaComponentQueriesDAO icaComponentQueriesDAO;

	@Override
	protected BaseDAO<IcaComponentQueries> getDAO() {
		// TODO Auto-generated method stub
		return icaComponentQueriesDAO;
	}
	
	

}
