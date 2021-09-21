package com.spts.lms.daos.user;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.user.TrainingProgram;
import com.spts.lms.daos.BaseDAO;

@Repository("trainingProgramDAO")
public class TrainingProgramDAO extends BaseDAO<TrainingProgram> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "trainingprogram";
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub

		/*
		 * String sql =
		 * "Insert into program (abbr, programName, sessionType, durationInMonths, maxDurationInMonths, revisedFromMonth, revisedFromYear, "
		 * + " createdDate, lastModifiedDate, createdBy, lastModifiedBy) values"
		 * +
		 * "(:abbr, :programName, :sessionType, :durationInMonths, :maxDurationInMonths, :revisedFromMonth, :revisedFromYear, "
		 * + " :createdDate, :lastModifiedDate, :createdBy, :lastModifiedBy)";
		 */
		// String sql=
		// "Insert into trainingProgram(id,trainingTitle,date,startTime,endTime,userType,ConductedBy,school,location) values(:id,:trainingTitle,:date,:startTime,:endTime,:userType,:ConductedBy,:school,:location)";
		String sql = "Insert into trainingprogram(id,trainingTitle,date,startTime,endTime,userType,ConductedBy, "
				+ " school,programId,campusId,createdBy,createdDate,lastModifiedBy,lastModifiedDate) values(:id,:trainingTitle,:date,:startTime,:endTime,:userType,:ConductedBy,:school,:programId,:campusId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate)";

		return sql;

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

	/*
	 * public int save(TrainingProgram p){ String sql=
	 * "insert into trainingProgram(id,traningTitle,date,startTime,endTime) values(:id,:traningTitle,:date,:startTime,:endTime)"
	 * ; return template.update(sql); }
	 */

	public List<TrainingProgram> getOnGoingTraining() {
		String sql = " select * from " + getTableName()
				+ " where startTime <= NOW() and endTime >= NOW() ";

		return findAllSQL(sql, new Object[] {});
	}

	public TrainingProgram getMarkedTrainingAttendance(String username,
			Long trainingProgramId) {
		String sql = " select * from training_attendance where username = ? and trainingProgramId = ? ";

		return findOneSQL(sql, new Object[] { username, trainingProgramId });
	}

	public void insertTrainingAttendance(String username,
			String trainingProgramId) {
		String sql = " INSERT INTO training_attendance (username, trainingProgramId, createdBy, createdDate, lastModifiedBy, lastModifiedDate) "
				+ "VALUES (?, ?, ?, now(), ?, now())  ";

		getJdbcTemplate()
				.update(sql,
						new Object[] { username, trainingProgramId, username,
								username });
	}

	public List<TrainingProgram> findTrainingProgramList() {
		String sql = " select  * from " + getTableName() + "";

		return findAllSQL(sql, new Object[] {});
	}
}
