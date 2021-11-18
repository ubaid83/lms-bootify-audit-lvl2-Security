package com.spts.lms.daos.StudentService;

import java.util.List;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.StudentService.LorRegDetails;
import com.spts.lms.beans.StudentService.LorRegStaff;
import com.spts.lms.daos.BaseDAO;

@Repository
public class LorRegDetailsDAO extends BaseDAO<LorRegDetails> {

	@Override
	protected String getTableName() {
		return "lor_reg_details";
	}

	@Override
	protected String getInsertSql() {
		
		String sql = " INSERT INTO "+ getTableName() +" (username, name, email, mobile, programEnrolledId, "
				+ " countryForHigherStudy, universityName, programToEnroll, tentativeDOJ, competitiveExam, examMarksheet, "
				+ " toeflOrIelts, toeflOrIeltsMarksheet, isNmimsPartnerUniversity, nmimsPartnerUniversity,  active, "
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ " VALUES (:username, :name, :email, :mobile, :programEnrolledId, :countryForHigherStudy, "
				+ " :universityName, :programToEnroll, :tentativeDOJ, :competitiveExam, :examMarksheet, :toeflOrIelts, "
				+ " :toeflOrIeltsMarksheet, "
				+ " :isNmimsPartnerUniversity, :nmimsPartnerUniversity, 'Y', "
				+ " :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		return null;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public List<LorRegDetails> getPendingAppicationForApproval(String staffId){
	
		String sql="select lrd.*,u.rollNo,u.programId,u.enrollmentYear as acadYear,lrs.id as lorRegStaffId,lrs.department,lrs.expectedDate,lrs.noOfCopies,"
				+ "lrs.appApproval,lrs.lorDocsFilePath,lrs.docApproval,lrs.lorApproval,"
				+ "lrs.appRejectionReason,lrs.docRejectionReason,lrs.lorRejectionReason,lrs.lorFormatFilePath,lrs.finalLorFilePath "
				+ " from users u, "+ getTableName() +" lrd, lor_reg_staff lrs where u.username=lrd.username and lrd.id=lrs.lorRegId and (lrs.appApproval ='Approve' or lrs.appApproval is Null) and (lrs.LorApproval ='Reject' or lrs.LorApproval is Null) and lrs.staffId like ?";
		//String sql="select *,u.rollNo,u.programId,u.enrollmentYear as acadYear,lrs.id as lorRegStaffId from users u, lor_reg_details lrd, lor_reg_staff lrs where u.username=lrd.username and lrd.id=lrs.lorRegId and  lrs.staffId like ?";
		return findAllSQL(sql, new Object[] { staffId+"%" });
	}

	

	public LorRegDetails getLorDetailsById(String lorRegId,String staffId){
		
		String sql="select lrd.*,lrs.id as lorRegStaffId,lrs.appApproval,lrs.lorDocsFilePath,lrs.docApproval,lrs.lorApproval,"
				+ "lrs.appRejectionReason,lrs.docRejectionReason,lrs.lorRejectionReason,lrs.lorFormatFilePath,"
				+ "lrs.finalLorFilePath from "+ getTableName() +" lrd, lor_reg_staff lrs where lrd.id=lrs.lorRegId "
				+ "and lrs.lorRegId=? and lrs.staffId like ?";
		
		return findOneSQL(sql, new Object[] {lorRegId, staffId+"%" });
	}

	public LorRegDetails findByCountryName(final String country_name) {
		
		 String sql = "Select * from country where countryForHigherStudy = ?";
			return findOneSQL(sql, new Object[] { country_name});
			
		
	}

}
