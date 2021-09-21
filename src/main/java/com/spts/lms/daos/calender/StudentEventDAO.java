package com.spts.lms.daos.calender;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.calender.StudentEvent;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
@Repository("studentEventDAO")
public class StudentEventDAO extends BaseDAO<StudentEvent> {
	
	@Override
	protected String getTableName() {
		return "student_events";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO student_events(eventId,username,user_email,courseId,"
				+ "createdBy,createdDate,lastModifiedBy,lastModifiedDate)"
				+ " VALUES(:eventId,:username,:user_email,:courseId,"
				+ ":createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate) ";
				
	}
	
	public StudentEvent findByEventIdAndUsername(String eventId,String username) {
		String sql = " select * from student_events where eventId = ? and username = ?";
		
		return findOneSQL(sql, new Object[]{eventId,username});
		
	}
	
	
	public List<StudentEvent> getStudentForEvents(Long eventId, Long courseId) {

		final String sql = "select uc.username, u.firstname, u.lastname, u.email, se.eventId, se.id "
				+ " from user_course uc "
				+ "	inner join users u on uc.username = u.username "
				+ "	inner join calender c on c.courseId = uc.courseId and c.id = ?"
				+ "	left outer join student_events se on se.username = u.username and se.eventId = c.id "
				+ " where uc.role = ? and uc.courseId = ? order by se.id asc";

		return findAllSQL(sql, new Object[] { eventId, Role.ROLE_STUDENT.name(),
				courseId });

	}
	
	public List<StudentEvent> getStudentsByEventId(Long eventId) {
		final String sql =" select * from student_events where eventId = ?";
		
		return findAllSQL(sql,new Object[]{eventId});
	}
	
	
	@Override
	protected String getUpdateSql() {
		return null;
	}
	
	@Override
	protected String getUpsertSql() {
		return null;
	}


}
