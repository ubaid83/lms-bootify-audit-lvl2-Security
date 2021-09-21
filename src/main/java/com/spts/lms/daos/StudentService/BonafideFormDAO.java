package com.spts.lms.daos.StudentService;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.StudentService.BonafideForm;
import com.spts.lms.daos.BaseDAO;

@Repository
public class BonafideFormDAO extends BaseDAO<BonafideForm> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "bonafide_form";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ "(username,firstname,lastname,fatherName,motherName,rollNo,acadYear,programName,studyClass,division,reason,serviceId,level1,level2,level3,flag1,flag2,flag3,createdBy,createdDate,lastModifiedBy,lastModifiedDate,payment,isSubmitted,active)"

				+ " VALUES(:username,:firstname,:lastname,:fatherName,:motherName,:rollNo,:acadYear,:programName,:studyClass,:division,:reason,:serviceId,:level1,:level2,:level3,:flag1,:flag2,:flag3,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:payment,:isSubmitted,:active)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "firstname = :firstname , " + "lastname = :lastname ,"
				+ "rollNo = :rollNo ," + "acadYear = :acadYear ,"
				+ "programName = :programName ," + "username = :username ,"
				+ "studyClass = :studyClass ," + "division = :division ,"
				+ "reason = :reason ," + "serviceId = :serviceId ,"
				+ "level1 = :level1 ," + "level2 = :level2 ,"
				+ "level3 = :level3 ," + "flag1 = :flag1 ,"
				+ "flag2 = :flag2 ," + "flag3 = :flag3 ,"
				+ "fatherName = :fatherName ," + "motherName = :motherName ,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate ,"
				+ "payment = :payment ," + "active = :active "
				+ "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BonafideForm> findAllSubmittedStudentsByServiceId(Long serviceId) {
		String sql = "select * from " + getTableName()
				+ " where serviceId = ? and active = 'Y' and isSubmitted = 'Y'";
		return findAllSQL(sql, new Object[] { serviceId });
	}

	public List<BonafideForm> findAllSubmittedStudentsByServiceIdFlag1(
			Long serviceId) {
		String sql = "select * from "
				+ getTableName()
				+ " where serviceId = ? and active = 'Y' and isSubmitted = 'Y' and flag1 = 'PENDING'";
		return findAllSQL(sql, new Object[] { serviceId });
	}

	public List<BonafideForm> findAllSubmittedStudentsByServiceIdFlag2(
			Long serviceId) {
		String sql = "select * from "
				+ getTableName()
				+ " where serviceId = ? and active = 'Y' and isSubmitted = 'Y' and flag2 = 'PENDING' and flag1='APPROVED'";
		return findAllSQL(sql, new Object[] { serviceId });
	}

	public void updateFlags(Integer flagNo, String status, Long id) {
		String sql = "update " + getTableName() + " set flag" + flagNo
				+ " = ? where id = ?";
		executeUpdateSql(sql, new Object[] { status, id });
	}

	public void updateFlagsAndRemarks(String flagNo, String status,
			String remark, String id) {
		String sql = "update " + getTableName() + " set flag" + flagNo
				+ " = ? , remark" + flagNo + " = ? where id = ?";
		executeUpdateSql(sql, new Object[] { status, remark, id });
	}

	public BonafideForm findByUsernameAndSubmitted(String username) {
		String sql = "select * from " + getTableName()
				+ " where username = ? and active = 'Y' and isSubmitted = 'Y'";
		return findOneSQL(sql, new Object[] { username });
	}
	
	public BonafideForm findByUsernameAndSaved(String username) {
		String sql = "select * from " + getTableName()
				+ " where username = ? and active = 'Y' and isSaveAsDraft = 'Y'";
		return findOneSQL(sql, new Object[] { username });
	}


	public BonafideForm getSubmittedStudentById(Long id) {
		String sql = "select * from " + getTableName()
				+ " where id = ? and active = 'Y' and isSubmitted = 'Y'";
		return findOneSQL(sql, new Object[] { id });
	}

	public void updateSubmit(Long id) {
		String sql = "update " + getTableName()
				+ " set active = 'N' where id = ?";
		executeUpdateSql(sql, new Object[] { id });
	}

	public void saveBonafideRemarks(String remarkValue, String remarkCol,
			Long pk) {
		BonafideForm bonafideForm = new BonafideForm();
		// bonafideForm.setRemark1(remarkValue);
		bonafideForm.setId(pk);

		String sql = "update " + getTableName() + " set " + remarkCol + " = '"
				+ remarkValue + "'," + " lastModifiedDate = :lastModifiedDate "
				+ " where id = :id";

		updateSQL(bonafideForm, sql);

	}

	public void saveBonafideStatus(String flagValue, String flagCol, Long pk) {
		BonafideForm bonafideForm = new BonafideForm();

		bonafideForm.setId(pk);

		String sql = "update " + getTableName() + " set " + flagCol + " = '"
				+ flagValue + "'," + " lastModifiedDate = :lastModifiedDate "
				+ " where id = :id";

		updateSQL(bonafideForm, sql);

	}

	public BonafideForm findStudentBonafide(String username, Long serviceId) {
		String sql = "select * from "
				+ getTableName()
				+ " where username = ? and serviceId = ? and isSubmitted = 'Y' and active = 'Y'";
		return findOneSQL(sql, new Object[] { username, serviceId });
	}

	public List<BonafideForm> findAllPendingBonafides(){
		String sql="select bf.*, u.email from " + getTableName() + " bf, service s, users u"
				+ " where bf.serviceId = s.id and bf.level3 = u.username and TIMEDIFF(SYSDATE(),bf.createdDate) > TIME(s.duration) "
				+ "and bf.flag1 = 'APPROVED' and bf.flag2 = 'PENDING' and bf.isSubmitted = 'Y' and bf.active = 'Y' and bf.flag3 = '0' ";
		return findAllSQL(sql, new Object[]{});
	}
	
	public List<BonafideForm> findAllPendingBonafidesForLevel3(String username){
		String sql="select bf.* from " + getTableName() + " bf, service s"
				+ " where bf.serviceId = s.id and TIMEDIFF(SYSDATE(),bf.createdDate) > TIME(s.duration) "
				+ "and bf.flag1 <> 'REJECTED' and bf.flag2 = 'PENDING' and bf.isSubmitted = 'Y' and bf.active = 'Y' and bf.level3 = ? ";
		return findAllSQL(sql, new Object[]{username});
	}
	
	public void saveBonafideStatusForLevel3(String flagValue,String flagCol, String pk) {
		 BonafideForm bonafideForm = new BonafideForm();
		
		 bonafideForm.setId(Long.valueOf(pk));
		 

		String sql = "update " + getTableName() + " set " + flagCol + " = '"+ flagValue+"',"
				+ " lastModifiedDate = :lastModifiedDate where id = :id";

		updateSQL(bonafideForm, sql);

	}
	
	public void saveBonafideRemarksForLevel3(String remarkValue,String remarkCol, String pk) {
		 BonafideForm bonafideForm = new BonafideForm();
		 //bonafideForm.setRemark1(remarkValue);
		 bonafideForm.setId(Long.valueOf(pk));
		 

		String sql = "update " + getTableName() + " set " + remarkCol + " = '"+ remarkValue+"',"
				+ " lastModifiedDate = :lastModifiedDate where id = :id";

		updateSQL(bonafideForm, sql);

	}
}
