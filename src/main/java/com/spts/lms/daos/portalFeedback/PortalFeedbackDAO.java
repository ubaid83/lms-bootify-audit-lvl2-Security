package com.spts.lms.daos.portalFeedback;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.portalFeedback.PortalFeedback;
import com.spts.lms.daos.BaseDAO;

@Repository("portalFeedbackDAO")
public class PortalFeedbackDAO extends BaseDAO<PortalFeedback> {

	@Override
	protected String getTableName() {
		return "portal_feedback";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO "
				+ getTableName()
				+ " (username, userRole, active, createdBy, createdDate,"
				+ " lastModifiedBy, lastModifiedDate) VALUES (:username, :userRole, 'Y', :createdBy,"
				+ " :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE " + getTableName() + " SET"
				+ " username = :username," + " userRole = :userRole,"
				+ " active = :active," + " createdBy = :createdBy,"
				+ " createdDate = :createdDate,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate" + " WHERE id = :id ";
		return str;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public PortalFeedback findUserByUsername(String username) {

		String sql = "SELECT * FROM " + getTableName() + " WHERE username = ?";

		return findOneSQL(sql, new Object[] { username });
	}

	public PortalFeedback findByUsername(String username) {

		String sql = "select * from " + getTableName() + " where username = ?";
		return findOneSQL(sql, new Object[] { username });
	}

}
