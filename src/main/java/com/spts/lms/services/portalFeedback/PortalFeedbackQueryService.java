package com.spts.lms.services.portalFeedback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.portalFeedback.PortalFeedbackQuery;
import com.spts.lms.beans.portalFeedback.PortalFeedbackResponse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.portalFeedback.PortalFeedbackQueryDAO;
import com.spts.lms.daos.portalFeedback.PortalFeedbackResponseDAO;
import com.spts.lms.services.BaseService;

@Service("portalFeedbackQueryService")
@Transactional
public class PortalFeedbackQueryService extends
		BaseService<PortalFeedbackQuery> {

	@Autowired
	private PortalFeedbackResponseDAO portalFeedbackResponseDAO;

	@Autowired
	private PortalFeedbackQueryDAO portalFeedbackQueryDAO;

	@Override
	protected BaseDAO<PortalFeedbackQuery> getDAO() {

		return portalFeedbackQueryDAO;
	}

	public List<PortalFeedbackQuery> findQueryList(Long portalFeedbackResponseId) {
		return portalFeedbackQueryDAO.findQueryList(portalFeedbackResponseId);
	}

	public PortalFeedbackQuery findQuery(Long portalFeedbackResponseId) {
		return portalFeedbackQueryDAO.findQuery(portalFeedbackResponseId);
	}

	public List<String> findSupportAdminUsernames() {

		return portalFeedbackQueryDAO.findSupportAdminUsernames();
	}

	public PortalFeedbackQuery findQueryById(Long id) {
		return portalFeedbackQueryDAO.findQueryById(id);
	}

	public List<PortalFeedbackQuery> findQueryByPortalFeedbackId(
			Long portalFeedbackId) {

		return portalFeedbackQueryDAO
				.findQueryByPortalFeedbackId(portalFeedbackId);
	}

}
