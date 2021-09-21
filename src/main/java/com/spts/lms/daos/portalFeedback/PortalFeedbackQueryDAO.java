package com.spts.lms.daos.portalFeedback;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.portalFeedback.PortalFeedbackQuery;
import com.spts.lms.beans.portalFeedback.PortalFeedbackResponse;
import com.spts.lms.daos.BaseDAO;

@Repository("portalFeedbackQueryDAO")
public class PortalFeedbackQueryDAO extends BaseDAO<PortalFeedbackQuery> {

	@Override
	protected String getTableName() {

		return "portal_feedback_query";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO "
				+ getTableName()
				+ " (portalFeedbackId, answer, active,"
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate,parentId)"
				+ " VALUES (:portalFeedbackId, :answer, 'Y', :createdBy, :createdDate,"
				+ " :lastModifiedBy, :lastModifiedDate,:parentId) ";
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

	public List<PortalFeedbackQuery> findQueryList(Long portalFeedbackResponseId) {
		String sql = "select pfq.id, pfq.answer,pfq.parentId,pfq.createdBy as createdBy, pfq.createdDate from "
				+ getTableName() + " pfq where pfq.parentId=?";
		return findAllSQL(sql, new Object[] { portalFeedbackResponseId });
	}

	public PortalFeedbackQuery findQuery(Long portalFeedbackResponseId) {
		String sql = "select pfq.id, pfq.answer,pfq.parentId,pfq.createdBy as createdBy, pfq.createdDate from "
				+ getTableName() + " pfq where pfq.parentId=?";
		return findOneSQL(sql, new Object[] { portalFeedbackResponseId });
	}

	public List<String> findSupportAdminUsernames() {
		String sql = "select distinct (u.username) from users u, user_roles ur where u.username=ur.username and u.enabled = 1 and ur.role = 'ROLE_SUPPORT_ADMIN' and u.active = 'Y' and ur.active = 'Y' ";

		return getJdbcTemplate().queryForList(sql, String.class);
	}

	public PortalFeedbackQuery findQueryById(Long id) {
		String sql = "select id,answer,active,createdBy,createdDate,parentId from "
				+ getTableName() + " where id = ?";
		return findOneSQL(sql, new Object[] { id });
	}

	public List<PortalFeedbackQuery> findQueryByPortalFeedbackId(
			Long portalFeedbackId) {
		String sql = "select id,portalFeedbackId,answer,createdBy,createdDate,parentId from "
				+ getTableName() + " where portalFeedbackId = ?";
		return findAllSQL(sql, new Object[] { portalFeedbackId });
	}

}
