package com.spts.lms.daos.tee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.IcaStudentBatchwise;
import com.spts.lms.beans.tee.TeeStudentBatchwise;
import com.spts.lms.daos.BaseDAO;

@Repository("teeStudentBatchwiseDAO")
public class TeeStudentBatchwiseDAO extends BaseDAO<TeeStudentBatchwise>{
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	protected String getTableName() {
		
		return "tee_student_batchwise";
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
				+ " ( username,teeId,facultyId,active,createdBy,"
				+ " createdDate,lastModifiedBy,lastModifiedDate) values"
				+ " (:username,:teeId,:facultyId,:active,"
				+ " :createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) "

				+ " ON DUPLICATE KEY UPDATE "
				+ " active=:active,"
				+ " lastModifiedBy=:lastModifiedBy,lastModifiedDate=:lastModifiedDate ";

		return sql;
	}
	
	public List<TeeStudentBatchwise> getAllByActiveTeeId(String teeId){
		String sql = " select * from "+getTableName()+" where active = 'Y' and teeId = ? ";
		return findAllSQL(sql, new Object[]{ teeId });
	}
	
	public List<String> getDistinctUsernamesByActiveTeeIdAndFaculty(Long teeId, String facultyId){
		String sql = " select distinct username from "+getTableName()+" where active = 'Y' and teeId = ? and facultyId = ? ";
		return getJdbcTemplate().queryForList(sql, String.class, new Object[]{ teeId, facultyId });
	}
	public void deleteAllActiveByTeeId(String teeId){
		String sql = " delete from "+getTableName()+" where active = 'Y' and teeId = ? ";
		getJdbcTemplate().update(sql, new Object[]{ teeId });
	}
	public void deleteSoftByTeeId(String teeId){
		String sql = " update "+getTableName()+" set active = 'N' where teeId = ? ";
		getJdbcTemplate().update(sql, new Object[]{ teeId });
	}
	
}
