package com.spts.lms.daos.portalFeedback;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.portalFeedback.PortalFeedbackResponse;
import com.spts.lms.daos.BaseDAO;

@Repository("portalFeedbackResponseDAO")
public class PortalFeedbackResponseDAO extends BaseDAO<PortalFeedbackResponse> {

	@Override
	protected String getTableName() {

		return "portal_feedback_response";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO "
				+ getTableName()
				+ " (portalFeedbackId, portalFeedbackQuestionId, answer, active,"
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate)"
				+ " VALUES (:portalFeedbackId, :portalFeedbackQuestionId, :answer, 'Y', :createdBy, :createdDate,"
				+ " :lastModifiedBy, :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE " + getTableName() + " SET"
				+ " portalFeedbackId = :portalFeedbackId,"
				+ " portalFeedbackQuestionId = :portalFeedbackQuestionId,"
				+ " answer = :answer," + " active = :active,"
				+ " createdBy = :createdBy," + " createdDate = :createdDate,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate" + " WHERE id = :id ";
		return str;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<PortalFeedbackResponse> findByPortalFeedbackId(Long id) {
		String sql = "select pfr.* from portal_feedback_response pfr,portal_feedback_question pfq "
				+ " where pfr.portalFeedbackQuestionId=pfq.id and  pfr.portalFeedbackId=? and pfq.`type`='Comment'";
		return findAllSQL(sql, new Object[] { id });
	}

}
