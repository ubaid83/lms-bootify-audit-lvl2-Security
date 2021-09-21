package com.spts.lms.daos.portalFeedback;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.LmsDb;
import com.spts.lms.beans.portalFeedback.PortalFeedback;
import com.spts.lms.daos.BaseDAO;

@Repository("LmsportalFeedbackDAO")
public class LmsPortalFeedbackDAO extends BaseDAO<PortalFeedback> {

	private static String tableName;
	private static String lmsDB;

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return tableName;
	}

	public void getLmsDb(LmsDb bean) {
		lmsDB = bean.getLmsDb();
		tableName = bean.getLmsDb() + ".portal_feedback";
		System.out.println("table name is" + tableName);
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

	/*
	 * public List<PortalFeedback> findAllFeedbacks() { String sql =
	 * " select pf.id,pf.username,pf.userRole,pf.createdBy,pf.createdDate,u.firstname,u.lastname "
	 * + " from " + getTableName() + " pf, " + lmsDB + ".users u, " + lmsDB +
	 * ".portal_feedback_response pfr " +
	 * " where pf.username=u.username and  pf.id=pfr.portalFeedbackId " +
	 * " and pfr.answer <> '' and pfr.portalFeedbackQuestionId=3 and pf.active='Y' and u.enabled='1' and u.active='Y'  order by pf.createdDate desc"
	 * ; return findAllSQL(sql, new Object[] {}); }
	 */

	public List<PortalFeedback> findAllFeedbacks() {
		String sql = " select pf.id,pf.username,pf.userRole,pf.createdBy,pf.createdDate,u.firstname,u.lastname ,u.email, u.mobile, pfr.answer, pfr.createdDate "
				+ " from "
				+ getTableName()
				+ " pf, "
				+ lmsDB
				+ ".users u, "
				+ lmsDB
				+ ".portal_feedback_response pfr "
				+ " where pf.username=u.username and  pf.id=pfr.portalFeedbackId "
				+ " and pfr.answer <> '' and pfr.portalFeedbackQuestionId=3 and pf.active='Y' and u.enabled='1' and u.active='Y'  order by pf.createdDate desc";
		return findAllSQL(sql, new Object[] {});
	}

}
