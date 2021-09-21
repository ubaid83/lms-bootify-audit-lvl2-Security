package com.spts.lms.services.portalFeedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.portalFeedback.PortalFeedback;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.portalFeedback.PortalFeedbackDAO;
import com.spts.lms.services.BaseService;

@Service("portalFeedbackService")
@Transactional
public class PortalFeedbackService extends BaseService<PortalFeedback> {

	@Autowired
	private PortalFeedbackDAO portalFeedbackDAO;

	@Override
	protected BaseDAO<PortalFeedback> getDAO() {

		return portalFeedbackDAO;
	}

	public PortalFeedback findUserByUsername(String username) {
		return portalFeedbackDAO.findUserByUsername(username);
	}

	public PortalFeedback findByUsername(String username) {
		return portalFeedbackDAO.findByUsername(username);
	}

}
