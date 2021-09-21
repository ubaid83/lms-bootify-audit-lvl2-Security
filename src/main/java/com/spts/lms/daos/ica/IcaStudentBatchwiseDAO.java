package com.spts.lms.daos.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaStudentBatchwise;
import com.spts.lms.daos.BaseDAO;

@Repository("icaStudentBatchwiseDAO")
public class IcaStudentBatchwiseDAO extends BaseDAO<IcaStudentBatchwise>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	@Override
	protected String getTableName() {
		
		return "ica_student_batchwise";
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		String sql = "Insert into "
				+ getTableName()
				+ " ( username,icaId,facultyId,active,createdBy,"
				+ " createdDate,lastModifiedBy,lastModifiedDate) values"
				+ " (:username,:icaId,:facultyId,:active,"
				+ " :createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) "

				+ " ON DUPLICATE KEY UPDATE "
				+ " active=:active,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}
	
	public List<IcaStudentBatchwise> getAllByActiveIcaId(String icaId){
		String sql = " select * from "+getTableName()+" where active = 'Y' and icaId = ? ";
		return findAllSQL(sql, new Object[]{ icaId });
	}
	
	public List<IcaStudentBatchwise> getAllByActiveIcaIdAndFaculty(Long icaId, String facultyId){
		String sql = " select * from "+getTableName()+" where active = 'Y' and icaId = ? and facultyId = ? ";
		return findAllSQL(sql, new Object[]{ icaId, facultyId });
	}
	
	public List<String> getDistinctUsernamesByActiveIcaIdAndFaculty(Long icaId, String facultyId){
		String sql = " select distinct username from "+getTableName()+" where active = 'Y' and icaId = ? and facultyId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ icaId, facultyId });
	}
	
	public List<String> getDistinctUsernamesByActiveIcaId(String icaId){
		String sql = " select distinct username from "+getTableName()+" where active = 'Y' and icaId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ icaId });
	}
	
	public void deleteAllActiveByIcaId(String icaId){
		String sql = " delete from "+getTableName()+" where active = 'Y' and icaId = ? ";
		getJdbcTemplate().update(sql, new Object[]{ icaId });
	}
	
	public void deleteSoftByIcaId(String icaId){
		String sql = " update "+getTableName()+" set active = 'N' where icaId = ? ";
		getJdbcTemplate().update(sql, new Object[]{ icaId });
	}
	
	public List<String> getDistinctActiveUsernamesOfRemainingFaculty(Long icaId, String facultyId){
        String sql = " select distinct username from "+getTableName()+" where active = 'Y' and icaId = ? and facultyId <> ? ";
        return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ icaId, facultyId });
	}
	
	public String getFacultyIdByusernameAndIcaId(long icaId,String username) {
		try {
		String sql="Select facultyId from "+getTableName()+" where icaId = ? and username = ? and active = 'Y'";
		return getJdbcTemplate().queryForObject(sql, new Object[] { icaId,username },
				String.class);
		}catch(Exception e) {
			return null;
		}
	}

}
