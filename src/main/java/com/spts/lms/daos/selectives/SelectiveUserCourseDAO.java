package com.spts.lms.daos.selectives;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.selectives.SelectiveUserCourse;
import com.spts.lms.daos.BaseDAO;

@Repository("selectiveUserCourseDAO")
public class SelectiveUserCourseDAO extends BaseDAO<SelectiveUserCourse>{
	
	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "selective_user_courses";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (username,course_id,active,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate)"

				+ " VALUES(:username,:course_id,:active,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " active = :active," 
				
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate"

			

				+ " where username = :username and course_id=:course_id  ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<SelectiveUserCourse> getStudentsList(String eventId,String username){
		
		String sql =" SELECT sc.*,c.course_name " + 
				" from selective_user_courses sc,selective_students se,selective_courses c " + 
				" where sc.course_id=c.id  and " + 
				" c.selective_id=se.selective_id  and sc.course_id=c.id " + 
				" and sc.active = 'Y' and se.active = 'Y' and c.active = 'Y' and sc.username=? and se.selective_id=? ";
		
		return findAllSQL(sql, new Object[] {username,eventId});
		
	}

}
