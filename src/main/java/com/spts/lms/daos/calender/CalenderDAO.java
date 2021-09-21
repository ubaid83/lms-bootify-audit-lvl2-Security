package com.spts.lms.daos.calender;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.calender.Calender;
import com.spts.lms.daos.BaseDAO;

@Repository("calenderDAO")
public class CalenderDAO extends BaseDAO<Calender> {

	@Override
	protected String getTableName() {
		return "calender";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO calender ( username, event, startDate, endDate, description, showStatus, courseId, "
				+ "active, createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ "VALUES ( :username, :event, :startDate, :endDate, :description,:showStatus , :courseId, "
				+ ":active, :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ "username = :username," + "event = :event ,"
				+ "startDate = :startDate ," + "endDate = :endDate ,"
				+ "description = :description ," + "showStatus = :showStatus ,"
				+ "courseId = :courseId ," + "active = :active,"
				+ "createdBy = :createdBy," + "createdDate = :createdDate,"
				+ "lastModifiedBy = :lastModifiedBy ,"
				+ "lastModifiedDate = :lastModifiedDate " + " where id=:id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Calender> listOfEventsForUser(String userName) {
		return findAllSQL(" select * from calender where username = ? ",
				new Object[] { userName });
	}

	public List<Calender> calendarEventsByCoursesList(String userName,
			Set<Long> courseIdList) {
		String sql = "select * from calender where username = :userName and courseId in(:courseIdList)";
		MapSqlParameterSource mapSource = new MapSqlParameterSource();
		mapSource.addValue("userName", userName);
		mapSource.addValue("courseIdList", courseIdList);
		return queryInClause(sql, mapSource);
	}

	public List<Calender> getAllEventsByCourseId(String userName,
			String courseId) {
		return findAllSQL(" select id,courseId,username,description,showStatus,event as event,event as title,startDate,endDate  from calender  where (username=? or showStatus='Y') and active='Y' and courseId =? ",
				new Object[] { userName, Long.valueOf(courseId) });
	}

	public List<Calender> getAllEvents(String userName, Long programId) {
		return findAllSQL(
				" select ca.id,ca.courseId,ca.username,ca.description,ca.showStatus,event as event,event as title,startDate,endDate "
						+ " from calender ca ,course c, program p ,user_course uc  where (ca.username=? or ca.showStatus='Y')  and uc.courseId = c.id and uc.username = ? and ca.courseId=c.id and c.programId =p.id and p.id = ? and  ca.active='Y' "

				, new Object[] { userName, userName, programId });
	}

	public List<Calender> getAllEventsForAdmin(String userName, Long programId) {

		return findAllSQL(
				"select distinct (ca.id) ,ca.courseId,ca.username,ca.description,ca.showStatus,event as event,event as title,startDate,endDate "

						+ " from calender ca ,course c, program p ,user_course uc  where (ca.username=? or ca.showStatus='Y')   and ca.courseId=c.id and c.programId =p.id and p.id = ? and  ca.active='Y'",

				new Object[] { userName, programId });

	}

	public void updateCalendarEvent(Map<String, Object> updateInputs) {

		updateUsingMaps(getUpdateSql(), updateInputs);
	}

}
