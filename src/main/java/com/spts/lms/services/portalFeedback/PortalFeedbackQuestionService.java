package com.spts.lms.services.portalFeedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.portalFeedback.PortalFeedbackQuestion;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.portalFeedback.PortalFeedbackQuestionDAO;
import com.spts.lms.services.BaseService;


@Service("portalFeedbackQuestionService")
@Transactional
public class PortalFeedbackQuestionService extends BaseService<PortalFeedbackQuestion> {

	@Autowired
	private PortalFeedbackQuestionDAO portalFeedbackQuestionDAO;
	
	@Override
	protected BaseDAO<PortalFeedbackQuestion> getDAO() {
		
		return portalFeedbackQuestionDAO;
	}

}
