package com.spts.lms.daos.FacultyLectureReschedule;

import org.springframework.stereotype.Repository;
import com.spts.lms.beans.FacultyLectureReschedule.FacultyLectureReschedule;
import com.spts.lms.daos.BaseDAO;

@Repository("facultyLectureRescheduleDao")
public class FacultyLectureRescheduleDao extends BaseDAO<FacultyLectureReschedule> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "faculty_lecture_reschedule_infra";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO " + getTableName() + " (facultyId,details,msg,createdDate,createdBy)"
				+ " VALUES(:facultyId,:details,:msg,:createdDate,:createdBy)";
	}

	@Override
	protected String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlayerIdForFaculty(String username) {
		String sql = " select playerId from user_playerid where username = ? ";
		return getJdbcTemplate().queryForObject(sql, new Object[] { username }, String.class);
	}
}
