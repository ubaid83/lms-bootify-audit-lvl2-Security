package com.spts.lms.services.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.beans.user.WsdlLog;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.feedback.FeedbackDAO;
import com.spts.lms.daos.user.WsdlLogDAO;
import com.spts.lms.services.BaseService;

@Service("WsdlLogService")
@Transactional
public class WsdlLogService extends BaseService<WsdlLog>{

	@Autowired
	private WsdlLogDAO wsdlLogDAO;

	@Override
	protected BaseDAO<WsdlLog> getDAO() {
		return wsdlLogDAO;
	}
	
	
	
	
	
	
}
