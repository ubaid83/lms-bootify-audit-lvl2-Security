package com.spts.lms.daos.selectives;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.selectives.SelectiveCourses;
import com.spts.lms.daos.BaseDAO;

@Repository("selectiveCoursesDAO")
public class SelectiveCoursesDAO extends BaseDAO<SelectiveCourses> {

	@Override
	protected String getTableName() {
		return "selective_courses";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO " + getTableName() + " (selective_id, course_name, active, "
				+ " createdBy, lastModifiedBy, createdDate, lastModifiedDate) " + " VALUES "
				+ " (:selective_id, :course_name, 'Y', "
				+ " :createdBy, :lastModifiedBy, :createdDate, :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE " + getTableName() + " SET "
				+ " selective_id=:selective_id, course_name=:course_name, active=:active, "
				+ " createdBy=:createdBy, lastModifiedBy=:lastModifiedBy, "
				+ " createdDate=:createdDate, lastModifiedDate=:lastModifiedDate " + " WHERE id=:id ";
		return str;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SelectiveCourses> getCoursesByEventId(String eventId) {

		String sql = " select c.* from " + getTableName() + " c where active  = 'Y' and selective_id = ? ";

		return findAllSQL(sql, new Object[] { eventId });
	}

}
