package com.spts.lms.daos.StudentService;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.StudentService.StudentHostelForm;
import com.spts.lms.daos.BaseDAO;

@Repository
public class StudentHostelFormDAO extends BaseDAO<StudentHostelForm> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "student_hostel_form";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ "(username,firstname,lastname,serviceId,acadYear,address,mobile,dob,nationality,parentName,relationship,occupation,designation,officeAddress,email,localName1,localAddress1,localMobile1,localName2,localAddress2,localMobile2,date,telephone,parentMobile,campus,level1,level2,level3,flag1,flag2,flag3,createdBy,createdDate,lastModifiedBy,lastModifiedDate,payment,isSubmitted,active)"

				+ " VALUES(:username,:firstname,:lastname,:serviceId,:acadYear,:address,:mobile,:dob,:nationality,:parentName,:relationship,:occupation,:designation,:officeAddress,:email,:localName1,:localAddress1,:localMobile1,:localName2,:localAddress2,:localMobile2,:date,:telephone,:parentMobile,:campus,:level1,:level2,:level3,:flag1,:flag2,:flag3,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:payment,:isSubmitted,:active)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "firstname = :firstname , " + "lastname = :lastname ,"
				+ "address = :address ," + "acadYear = :acadYear ,"
				+ "mobile = :mobile ," + "username = :username ,"
				+ "dob = :dob ," + "nationality = :nationality ,"
				+ "parentName = :parentName ," + "relationship = :relationship ,"
				+ "occupation = :occupation ,"+ "designation = :designation ,"
				+ "officeAddress = :officeAddress ,"+ "email = :email ,"
				+ "parentMobile = :parentMobile ,"+ "localName1 = :localName1 ,"
				+ "localAddress1 = :localAddress1 ,"+ "localMobile1 = :localMobile1 ,"
				+ "localName2 = :localName2 ,"+ "localAddress2 = :localAddress2 ,"
				+ "localMobile3 = :localMobile3 ,"+ "date = :date ,"
				+ "level1 = :level1 ," + "level2 = :level2 ,"
				+ "level3 = :level3 ," + "flag1 = :flag1 ,"
				+ "flag2 = :flag2 ," + "flag3 = :flag3 ,"
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

	public List<StudentHostelForm> findAllSubmittedStudentsByServiceId(Long serviceId) {
		String sql = "select * from " + getTableName()
				+ " where serviceId = ? and active = 'Y' and isSubmitted = 'Y'";
		return findAllSQL(sql, new Object[] { serviceId });
	}

	public List<StudentHostelForm> findAllSubmittedStudentsByServiceIdFlag1(
			Long serviceId) {
		String sql = "select * from "
				+ getTableName()
				+ " where serviceId = ? and active = 'Y' and isSubmitted = 'Y' and flag1 = 'PENDING'";
		return findAllSQL(sql, new Object[] { serviceId });
	}

	public List<StudentHostelForm> findAllSubmittedStudentsByServiceIdFlag2(
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

	public StudentHostelForm findByUsernameAndSubmitted(String username) {
		String sql = "select * from " + getTableName()
				+ " where username = ? and active = 'Y' and isSubmitted = 'Y'";
		return findOneSQL(sql, new Object[] { username });
	}
	
	public StudentHostelForm findByUsernameAndSaved(String username) {
		String sql = "select * from " + getTableName()
				+ " where username = ? and active = 'Y' and isSaveAsDraft = 'Y'";
		return findOneSQL(sql, new Object[] { username });
	}


	public StudentHostelForm getSubmittedStudentById(Long id) {
		String sql = "select * from " + getTableName()
				+ " where id = ? and active = 'Y' and isSubmitted = 'Y'";
		return findOneSQL(sql, new Object[] { id });
	}

	public void updateSubmit(Long id) {
		String sql = "update " + getTableName()
				+ " set active = 'N' where id = ?";
		executeUpdateSql(sql, new Object[] { id });
	}

	public void saveHostelRemarks(String remarkValue, String remarkCol,
			Long pk) {
		StudentHostelForm bonafideForm = new StudentHostelForm();
		// bonafideForm.setRemark1(remarkValue);
		bonafideForm.setId(pk);

		String sql = "update " + getTableName() + " set " + remarkCol + " = '"
				+ remarkValue + "'," + " lastModifiedDate = :lastModifiedDate "
				+ " where id = :id";

		updateSQL(bonafideForm, sql);

	}

	public void saveHostelStatus(String flagValue, String flagCol, Long pk) {
		StudentHostelForm bonafideForm = new StudentHostelForm();

		bonafideForm.setId(pk);

		String sql = "update " + getTableName() + " set " + flagCol + " = '"
				+ flagValue + "'," + " lastModifiedDate = :lastModifiedDate "
				+ " where id = :id";

		updateSQL(bonafideForm, sql);

	}

	public StudentHostelForm findStudentHostel(String username, Long serviceId) {
		String sql = "select * from "
				+ getTableName()
				+ " where username = ? and serviceId = ? and isSubmitted = 'Y' and active = 'Y'";
		return findOneSQL(sql, new Object[] { username, serviceId });
	}

	public List<StudentHostelForm> findAllPendingHostels(){
		String sql="select bf.*, u.email from " + getTableName() + " bf, service s, users u"
				+ " where bf.serviceId = s.id and bf.level3 = u.username and TIMEDIFF(SYSDATE(),bf.createdDate) > TIME(s.duration) "
				+ "and bf.flag1 = 'APPROVED' and bf.flag2 = 'PENDING' and bf.isSubmitted = 'Y' and bf.active = 'Y' and bf.flag3 = '0' ";
		return findAllSQL(sql, new Object[]{});
	}
	
	public List<StudentHostelForm> findAllPendingHostelsForLevel3(String username){
		String sql="select bf.* from " + getTableName() + " bf, service s"
				+ " where bf.serviceId = s.id and TIMEDIFF(SYSDATE(),bf.createdDate) > TIME(s.duration) "
				+ "and bf.flag1 <> 'REJECTED' and bf.flag2 = 'PENDING' and bf.isSubmitted = 'Y' and bf.active = 'Y' and bf.level3 = ? ";
		return findAllSQL(sql, new Object[]{username});
	}
	
	public void saveHostelStatusForLevel3(String flagValue,String flagCol, String pk) {
		 StudentHostelForm bonafideForm = new StudentHostelForm();
		
		 bonafideForm.setId(Long.valueOf(pk));
		 

		String sql = "update " + getTableName() + " set " + flagCol + " = '"+ flagValue+"',"
				+ " lastModifiedDate = :lastModifiedDate where id = :id";

		updateSQL(bonafideForm, sql);

	}
	
	public void saveHostelRemarksForLevel3(String remarkValue,String remarkCol, String pk) {
		 StudentHostelForm bonafideForm = new StudentHostelForm();
		 //bonafideForm.setRemark1(remarkValue);
		 bonafideForm.setId(Long.valueOf(pk));
		 

		String sql = "update " + getTableName() + " set " + remarkCol + " = '"+ remarkValue+"',"
				+ " lastModifiedDate = :lastModifiedDate where id = :id";

		updateSQL(bonafideForm, sql);

	}
}
