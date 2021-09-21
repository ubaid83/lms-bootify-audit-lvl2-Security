package com.spts.lms.daos.attendance;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.attendance.Attendance;
import com.spts.lms.beans.attendance.RtasAttendance;
import com.spts.lms.daos.BaseDAO;

@Repository("rtasAttendanceDAO")
public class RtasAttendanceDAO extends BaseDAO<RtasAttendance>{

	@Override
	protected String getTableName() {
		return "rtas_attendance.attendance";
	}


	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO "+ getTableName() +"(studentNumber,location,date,termNoIn,termNoOut,inTime,outTime) "
				+ "VALUES (:studentNumber,:location,:date,:termNoIn,:termNoOut,:inTime,:outTime)";
		return sql;
	}
	@Override
	protected String getUpdateSql() {
		final String sql = "Update "+ getTableName() +" set " + "studentNumber = :studentNumber,"
				  +"location=:location ,inTime=:inTime ,outTime=:outTime"	
				 + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	public List<RtasAttendance> findByUser(String username) {
        final String sql = "SELECT a.* FROM "+ getTableName() +" a "
                    + " where a.studentNumber = ? "
                    + " order by inTime desc";
        return findAllSQL(sql, new Object[] { username});
  }
	
/*
	public List<Attendance> getStudentsForAttendance(Long courseId,String facultyId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sm.messageId, sm.id "
				+ " from lms.user_course uc"
				+ "                          inner join lms.users u on uc.username = u.username"
				+ "                          inner join lms.program p on u.programId = p.id"
				+ "                          inner  join lms.student_message sm on sm.username = u.username and sm.courseId = uc.courseId and sm.messageId = ? "
				+ " where"
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by sm.id asc ";
		return findAllSQL(sql,
				new Object[] { courseId, Role.ROLE_ADMIN.name(), facultyId});

	}*/
	
	
	
}