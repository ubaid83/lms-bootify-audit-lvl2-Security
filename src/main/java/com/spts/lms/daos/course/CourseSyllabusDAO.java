package com.spts.lms.daos.course;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.beans.course.CourseSyllabus;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("courseSyllabusDAO")
public class CourseSyllabusDAO extends BaseDAO<CourseSyllabus> {

	@Override
	protected String getTableName() {

		return "course_syllabus";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into " + getTableName() + " (courseId, unit, description, duration, "
				+ " createdBy,createdDate,lastModifiedBy,lastModifiedDate,textbooks,referbooks) values"
				+ "(:courseId, :unit, :description, :duration, :createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,:textbooks,:referbooks)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		
		String sql = "update  "
				+  getTableName() 
				+ " set courseId=:courseId, "

				+ " unit=:unit, "
				+ " description=:description, "
				+ " duration=:duration, "
				+ " lastModifiedBy=:lastModifiedBy, "
				+ " lastModifiedDate=:lastModifiedDate, "
				+ " textbooks=:textbooks,"
				+ " referbooks = :referbooks"
			
				

				
				+ " where id=:id";
		return sql;
	}

	
	@Override
	protected String getUpsertSql() {
		return null;
	}
	
}
