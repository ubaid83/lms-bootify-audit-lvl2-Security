package com.spts.lms.daos.course;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("courseOverviewDAO")
public class CourseOverviewDAO extends BaseDAO<CourseOverview> {

	@Override
	protected String getTableName() {

		return "course_overview";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into " + getTableName() + " (id ,programId, code, sessionType, lecture, "
				+ " practical,tutorial,credit,ica,tee,preRequisite,objectives, outcomes,  createdBy, createdDate, lastModifiedBy, lastModifiedDate) values"
				+ "(:id, :programId, :code, :sessionType, :lecture,:practical,:tutorial,:credit,:ica,:tee,:preRequisite,"
				+ " :objectives, :outcomes,  :createdBy, :createdDate, :lastModifiedBy ,:lastModifiedDate)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		
		String sql = "update  "
				+  getTableName() 
				+ " set programId=:programId, "
				+ " code=:code, "
				+ " sessionType=:sessionType, "
				+ " lecture=:lecture, "
				+ " practical=:practical, "
				+ " tutorial=:tutorial, "
				+ " credit=:credit,"
				+ " ica=:ica,"
				+ " tee=:tee,"
				+ " preRequisite=:preRequisite,"
				+ " objectives=:objectives,"
				+ " outcomes=:outcomes,"
			
				+ " createdBy=:createdBy,"
				+ " createdDate=:createdDate,"
				+ " lastModifiedBy=:lastModifiedBy,"
				+ " lastModifiedDate=:lastModifiedDate"
				

				
				+ " where id=:id";
		return sql;
	}

	
	@Override
	protected String getUpsertSql() {
		return null;
	}
	
}
