package com.spts.lms.daos.studentParent;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.user.User;
import com.spts.lms.daos.BaseDAO;

@Repository
public class StudentParentDAO extends BaseDAO<StudentParent> {

	@Override
	protected String getTableName() {
		return "student_parent";
	}

	@Override
	protected String getInsertSql() {
		String sql = "INSERT INTO "
				+ getTableName()
				+ "(stud_username,parent_username,parent_firstname,parent_lastname,parent_mobile,parent_email,parent_type"
				+ "createdDate,createdBy,lastModifiedDate,lastModifiedBy)"
				+ "VALUES(:stud_username,:parent_username,:parent_firstname,:parent_lastname,:parent_mobile,:parent_email,:parent_type,"
				+ ":createdDate,:createdBy,:lastModifiedDate,"
				+ ":lastModifiedBy)";
		return sql;
	}

	public List<StudentParent> findStudentsByParentUname(String uname) {
		String sql = "select * from student_parent where parent_username = ?";
		return findAllSQL(sql, new Object[] { uname });
	}

	public StudentParent findParentByStudent(String uname) {
		String sql = "select * from student_parent where stud_username = ?";
		return findOneSQL(sql, new Object[] { uname });
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update student_parent set "
				+ "stud_username = :stud_username,"
				+ "parent_username = :parent_username ,"
				+ "parent_firstname = :parent_firstname ,"
				+ "parent_lastname = :parent_lastname ,"
				+ "parent_mobile = :parent_mobile ,"
				+ "parent_email = :parent_email ,"
				+ "parent_type = :parent_type,"

				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate "
				+ "where stud_username = :stud_username and"
				+ " parent_username=:parent_username";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		/*
		 * String sql = "INSERT INTO " + getTableName() +
		 * "(stud_username,parent_username,parent_firstname,parent_lastname,parent_mobile,parent_email,parent_type,"
		 * + "createdDate,createdBy,lastModifiedDate,lastModifiedBy)"
		 * 
		 * + "VALUES"
		 * 
		 * +
		 * "(:stud_username,:parent_username,:parent_firstname,:parent_lastname,:parent_mobile,:parent_email,:parent_type,"
		 * + "createdDate,:createdBy,:lastModifiedDate," + ":lastModifiedBy)"
		 * 
		 * + " ON DUPLICATE KEY UPDATE "
		 * 
		 * + " stud_username = :stud_username," +
		 * " parent_username = :parent_username," +
		 * " parent_firstname = :parent_firstname," +
		 * " parent_lastname = :parent_lastname," +
		 * " parent_mobile = :parent_mobile," + " parent_email = :parent_email,"
		 * + " parent_type = :parent_type,"
		 * 
		 * + " lastModifiedDate = :lastModifiedDate," +
		 * " lastModifiedBy = :lastModifiedBy"; return sql;
		 */

		return null;
	}
}
