package com.spts.lms.daos.student;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.student.StudentProgramSession;
import com.spts.lms.daos.BaseDAO;

@Repository("studentProgramSessionDAO")
public class StudentProgramSessionDAO extends BaseDAO<StudentProgramSession> {

	@Override
	protected String getTableName() {
		return "student_program_session";
	}

	@Override
	protected String getInsertSql() {
		final String sql = "INSERT INTO "+getTableName()+" (username,programId,session,acadMonth,acadYear,createdBy,"
				+ "createdDate,lastModifiedBy,lastModifiedDate) VALUES (:username,:programId,:session,:acadMonth,:acadYear,:createdBy,"
				+ ":createdDate,:lastModifiedBy,:lastModifiedDate)";
		return sql;
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
