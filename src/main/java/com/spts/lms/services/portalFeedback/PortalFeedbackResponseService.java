package com.spts.lms.services.portalFeedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.portalFeedback.PortalFeedbackResponse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.portalFeedback.PortalFeedbackResponseDAO;
import com.spts.lms.services.BaseService;

@Service("portalFeedbackResponseService")
@Transactional
public class PortalFeedbackResponseService extends
		BaseService<PortalFeedbackResponse> {

	@Autowired
	private PortalFeedbackResponseDAO portalFeedbackResponseDAO;

	@Override
	protected BaseDAO<PortalFeedbackResponse> getDAO() {

		return portalFeedbackResponseDAO;
	}

	public List<PortalFeedbackResponse> findByPortalFeedbackId(Long id) {
		return portalFeedbackResponseDAO.findByPortalFeedbackId(id);
	}
}
