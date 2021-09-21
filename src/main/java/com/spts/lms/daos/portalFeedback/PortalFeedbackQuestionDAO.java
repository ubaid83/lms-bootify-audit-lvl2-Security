package com.spts.lms.daos.portalFeedback;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.portalFeedback.PortalFeedbackQuestion;
import com.spts.lms.daos.BaseDAO;


@Repository("portalFeedbackQuestionDAO")
public class PortalFeedbackQuestionDAO extends BaseDAO<PortalFeedbackQuestion> {

	@Override
	protected String getTableName() {
		
		return "portal_feedback_question";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO "+ getTableName() +" (portalFeedbackId, question, option1, option2, option3,"
				+ " option4, option5, type, questionRole, active, createdBy, createdDate, lastModifiedBy, lastModifiedDate)"
				+ " VALUES (:portalFeedbackId, :question, :option1, :option2, :option3, :option4, :option5,"
				+ " :type, :questionRole, 'Y', :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE "+ getTableName() +" SET"
				+ " portalFeedbackId = :portalFeedbackId,"
				+ " question = :question,"
				+ " option1 = :option1,"
				+ " option2 = :option2,"
				+ " option3 = :option3,"
				+ " option4 = :option4,"
				+ " option5 = :option5,"
				+ " type = :type,"
				+ " questionRole = :questionRole,"
				+ " active = :active,"
				+ " createdBy = :createdBy,"
				+ " createdDate = :createdDate,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate = :lastModifiedDate"
				+ " WHERE id = :id ";
		return str;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

}
