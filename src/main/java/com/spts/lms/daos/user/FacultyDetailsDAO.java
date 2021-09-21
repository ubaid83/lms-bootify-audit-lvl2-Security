package com.spts.lms.daos.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.user.FacultyDetails;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("facultyDetailsDAO")
public class FacultyDetailsDAO extends BaseDAO<FacultyDetails> {

	@Override
	protected String getTableName() {
		return "faculty_details";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "Insert into faculty_details (username, experience, overview,age,designation,"
				+ " createdBy, createdDate,lastModifiedBy,lastModifiedDate,dob,mobile,email) values"
				+ " (:username, :experience ,:overview ,:age, :designation,"
				+ ":createdBy, :createdDate, :lastModifiedBy ,:lastModifiedDate ,:dob , :mobile ,:email)";

		return sql;

	}

	@Override
	protected String getUpdateSql() {

		String sql = "update faculty_details  "
				+ " set experience=:experience, " + " overview=:overview, "
				+ " age=:age, " + " designation=:designation, "
				+ " lastModifiedDate=:lastModifiedDate, "
				+ " lastModifiedBy=:lastModifiedBy, " + " dob=:dob,"
				+ "mobile=:mobile," + "email=:email" + " where id=:id";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FacultyDetails> findMyFacultyByCourse(Long courseId) {
		String sql = "SELECT u.* , fd.* FROM users u "
				+ " inner join user_course uc on uc.username = u.username "
                + "	inner join faculty_details fd on fd.username = u.username "
				+ "	  where  uc.courseId = ? and uc.role = ? ";

		return findAllSQL(sql, new Object[]{courseId, Role.ROLE_FACULTY.name(),});
	}

}
