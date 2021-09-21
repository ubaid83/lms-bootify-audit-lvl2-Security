package com.spts.lms.daos.attendance;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.attendance.Attendance;
import com.spts.lms.daos.BaseDAO;

@Repository("attendanceDAO")
public class AttendanceDAO extends BaseDAO<Attendance>{

	@Override
	protected String getTableName() {
		return "student_attendance";
	}


	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO student_attendance(username,courseId,facultyId,startDate,endDate) "
				+ "VALUES (:username,:courseId,:facultyId,:startDate,:endDate)";
		return sql;
	}
	@Override
	protected String getUpdateSql() {
		final String sql = "Update student_attendance set " + "username = :username,"
				  +"courseId=:courseId ,facultyId=:facultyId ,startDate=:startDate,endDate=:endDate "	
				 + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	public List<Attendance> findByUser(String username) {
        final String sql = "SELECT a.* FROM student_attendance a "
             
              
                    + " where a.username = ? "
                    + " order by startDate desc";
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