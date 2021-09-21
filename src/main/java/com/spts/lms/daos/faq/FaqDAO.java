package com.spts.lms.daos.faq;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.faq.Faq;
import com.spts.lms.daos.BaseDAO;

@Repository("faqDAO")
public class FaqDAO extends BaseDAO<Faq> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "faq";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (question,answer,questionType,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:question,:answer,:questionType,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " question = :question," + " answer =:answer,"
				+ " createdBy =:createdBy," + " questionType =:questionType,"
				+ " createdDate = :createdDate ,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate,"

			

				+ " where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Faq> getAdmissionsFAQs(){
		String sql = "select * from faq where faq.questionType like '%Admission%'";
		return findAllSQL(sql, new Object[]{});
	}
	public List<Faq> getAcademicsFAQs(){
		String sql = "select * from faq where faq.questionType like '%Academics%'";
		return findAllSQL(sql, new Object[]{});
	}
	public List<Faq> getExamsFAQs(){
		String sql = "select * from faq where faq.questionType like '%Exams%'";
		return findAllSQL(sql, new Object[]{});
	}
	public List<Faq> getOthersFAQs(){
		String sql = "select * from faq where faq.questionType like '%Others%'";
		return findAllSQL(sql, new Object[]{});
	}
	public List<Faq> getSupportFAQs(){
		String sql = "select * from faq where faq.questionType like '%Student Support%'";
		return findAllSQL(sql, new Object[]{});
	}

}
