package com.spts.lms.daos.StudentService;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.StudentService.LorRegDetails;
import com.spts.lms.beans.StudentService.LorRegStaff;
import com.spts.lms.daos.BaseDAO;

@Repository
public class LorRegStaffDAO extends BaseDAO<LorRegStaff> {

	@Override
	protected String getTableName() {
		return "lor_reg_staff";
	}

	@Override
	protected String getInsertSql() {

		String sql = " INSERT INTO " + getTableName()
				+ " (lorRegId, username, department, staffId, noOfCopies, appApproval, "
				+ " docApproval, lorFormatFilePath, finalLorUploadedBy, finalLorFilePath, LorApproval, "
				+ " active, createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ " VALUES (:lorRegId, :username, :department, :staffId, :noOfCopies, :appApproval, :docApproval, "
				+ " :lorFormatFilePath, :finalLorUploadedBy, :finalLorFilePath, :LorApproval, 'Y', :createdBy, "
				+ " :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {

		String sql = " UPDATE " + getTableName() + " SET "
				+ " lorRegId=:lorRegId, username=:username, department=:department, staffId=:staffId,noOfCopies=:noOfCopies, "
				+ " appApproval=:appApproval, docApproval=:docApproval, lorFormatFilePath=:lorFormatFilePath, "
				+ " finalLorUploadedBy=:finalLorUploadedBy, finalLorFilePath=:finalLorFilePath, "
				+ " LorApproval=:LorApproval, active=:active, createdBy=:createdBy, createdDate=:createdDate, "
				+ " lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate " + " WHERE id=:id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveApplicationApprovalStatus(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set "
				+ " appApproval = :appApproval, appRejectionReason=:appRejectionReason, "
				+ " lastModifiedBy = :lastModifiedBy, " + " lastModifiedDate = :lastModifiedDate " + " where id=:id";

		updateSQL(lorRegStaff, sql);

	}

	public void saveDocumentApprovalStatus(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set "
				+ " docApproval = :docApproval, docRejectionReason=:docRejectionReason, "
				+ " lastModifiedBy = :lastModifiedBy, " + " lastModifiedDate = :lastModifiedDate " + " where id=:id";

		updateSQL(lorRegStaff, sql);

	}

	public void saveLORApprovalStatus(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set "
				+ " lorApproval = :lorApproval, lorRejectionReason=:lorRejectionReason, "
				+ " finalLorFilePath = :finalLorFilePath, " + " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id=:id";

		updateSQL(lorRegStaff, sql);

	}

	public void saveLorFormatFilePath(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set " + " lorFormatFilePath = :lorFormatFilePath, "
				+ " lastModifiedBy = :lastModifiedBy, " + " lastModifiedDate = :lastModifiedDate " + " where id=:id";

		updateSQL(lorRegStaff, sql);
	}

	public void saveLorFinalFilePath(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set " + " finalLorFilePath = :finalLorFilePath, "
				+ " LorApproval = NULL, " + " finalLorUploadedBy = :finalLorUploadedBy, "
				+ " lastModifiedBy = :lastModifiedBy, " + " lastModifiedDate = :lastModifiedDate " + " where id=:id";

		updateSQL(lorRegStaff, sql);
	}

	public void saveExpectedDate(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set " + " expectedDate = :expectedDate, "
				+ " lastModifiedBy = :lastModifiedBy, " + " lastModifiedDate = :lastModifiedDate " + " where id=:id";
		updateSQL(lorRegStaff, sql);
	}

	public LorRegStaff findByLorRegIdAndStaffId(String lorRegId, String staffId) {
		String sql = "Select * from " + getTableName() + " where lorRegId = ? and staffId = ?";
		return findOneSQL(sql, new Object[] { lorRegId, staffId });
	}

	public List<String> getAllDept() {

		String sql = "select distinct department from  lor_department_faculty";
		return listOfStringParameter(sql, new Object[] {});
	}

	public List<LorRegStaff> getfaculty(String department) {
		String sql = "select distinct ldept.facultyId as username,CONCAT(u.firstname,' ',u.lastname) as name, ldept.department  from lor_department_faculty ldept LEFT JOIN users u ON u.username = ldept.facultyId where ldept.department=? ";
		return findAllSQL(sql, new Object[] { department });
	}

	public List<LorRegStaff> getAllApplicationByStudent(String username) {
		String sql = "select concat(u.firstname,' ',u.lastname) as name,lrs.* from lor_reg_staff lrs ,users u,lor_reg_details lrd where lrs.username=? and (u.username =lrs.staffId or u.username = REPLACE(lrs.staffId,'_STAFF','')) and lrd.id=lrs.lorRegId group by id";
		return findAllSQL(sql, new Object[] { username });

	}

	public List<LorRegStaff> getPendingAppicationForApprovalByAdmin(String username) {
		String sql = "select lgs.*,concat (u.firstname,' ',u.lastname) as fullName from lor_reg_staff lgs,users u where lgs.appApproval='Approve' and lgs.docApproval='Approve' and lgs.lorFormatfilePath IS NOT NULL and (lgs.finalLorFilePath IS NULL or lgs.lorApproval = 'Reject' or lgs.lorApproval IS NULL) and lgs.staffId=u.username  ";
		return findAllSQL(sql, new Object[] {});

	}

	public void updateStudentDocumentStatus(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set " + " lorDocsFilePath = :lorDocsFilePath, "
				+ " docApproval = :docApproval, docRejectionReason=:docRejectionReason, "
				+ " lastModifiedBy = :lastModifiedBy, " + " lastModifiedDate = :lastModifiedDate " + " where id=:id";
		updateSQL(lorRegStaff, sql);
	}

	public void updateLORDocumentStatus(LorRegStaff lorRegStaff) {
		String sql = "update " + getTableName() + " set "
				+ " lorApproval = :lorApproval, lorRejectionReason=:lorRejectionReason, "
				+ " finalLorUploadedBy = :finalLorUploadedBy, "
				+ " finalLorFilePath = :finalLorFilePath, " + " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate " + " where id=:id";

		updateSQL(lorRegStaff, sql);

	}

	public int getIsLorStaff(String facultyId) {
		String sql = " select count(distinct facultyId) from lor_department_faculty where facultyId = ?";

		return getJdbcTemplate().queryForObject(sql, Integer.class, new Object[] { facultyId });
	}

	public int AddLorDepartMentFaculty(LorRegStaff lorRegStaff) {
		String sql = "INSERT INTO lor_department_faculty (department,facultyId) VALUES (?,?);";
		return getJdbcTemplate().update(sql, new Object[] { lorRegStaff.getDepartment(), lorRegStaff.getUsername() });
	}

	public List<LorRegStaff> getPendingAppicationForApprovalByDepartment(String username) {
		String sql = "select lgs.*,concat (u.firstname,' ',u.lastname) as fullName from lor_reg_staff lgs,users u,lor_department_faculty ldf where lgs.appApproval='Approve' and lgs.docApproval='Approve' and lgs.lorFormatfilePath IS NOT NULL and (lgs.finalLorFilePath IS NULL or lgs.lorApproval = 'Reject' or lgs.lorApproval IS NULL) and lgs.staffId=u.username and ldf.department=lgs.department and ldf.facultyId=?";
		return findAllSQL(sql, new Object[] { username });
	}

	public LorRegStaff getDepartmentAssistent(String department) {
		String sql = "select  facultyId as username from lor_department_facuty where facultyId like '%_Staff%' and department=?  ";
		
		return findOneSQL(sql, new Object[] { department });
	}
	
}
