package com.spts.lms.daos.StudentService;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.StudentService.StudentServiceBean;
import com.spts.lms.daos.BaseDAO;

@Repository
public class StudentServiceDAO extends BaseDAO<StudentServiceBean> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "service";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ "(name,amount,level1,level2,level3,createdBy,createdDate,lastModifiedBy,lastModifiedDate,payment,active,makeAvail,duration,mapping)"

				+ " VALUES(:name,:amount,:level1,:level2,:level3,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:payment,:active,:makeAvail,:duration,:mapping)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "level1 = :level1 , " + "level2 = :level2 ,"
				+ "level3 = :level3 ," + "level4 = :level4 ,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "makeAvail = :makeAvail ," + "duration = :duration ,"
				+ "location = :location ," + "totalSeats = :totalSeats ,"
				+ "lastModifiedDate = :lastModifiedDate " + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<StudentServiceBean> findAllAvail() {
		String sql = "select * from " + getTableName()
				+ " where active = 'Y' and makeAvail = 'Y'";
		return findAllSQL(sql, new Object[] {});
	}

	public List<StudentServiceBean> findServiceByName(String name) {
		String sql = "select * from " + getTableName()
				+ " where active = 'Y' and makeAvail = 'Y' and name = ?";
		return findAllSQL(sql, new Object[] { name });
	}
}
