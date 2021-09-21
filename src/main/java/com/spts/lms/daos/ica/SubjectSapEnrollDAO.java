package com.spts.lms.daos.ica;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.ica.SubjectSapEnroll;
import com.spts.lms.daos.BaseDAO;

@Repository("subjectSapEnrollDAO")
public class SubjectSapEnrollDAO extends BaseDAO<SubjectSapEnroll> {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "subject_sap_enroll";
	}

	@Override
	protected String getInsertSql() {
		// TODO Auto-generated method stub
		return null;
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

	public List<SubjectSapEnroll> getNSStudent(String subjectCode,
			String acadYearCode, String programId, String acadSession,
			String campusId) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("subjectCode", subjectCode);
		params.put("acadYearCode", acadYearCode);
		params.put("programId", programId);
		params.put("acadSession", acadSession);
		params.put("campusId", campusId);
		String sql;

		if (campusId != null && !campusId.isEmpty()) {
			sql = "select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " ss.studentNumber from users u, subject_sap_enroll ss where "
					+ "	u.username =ss.studentNumber and ss.subjectCode=:subjectCode and ss.enrollDeEnroll='Y' and "
					+ " ss.acadYearCode=:acadYearCode and ss.programId in(:programIds) and ss.acadSession=:acadSession and ss.campusId=:campusId";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(SubjectSapEnroll.class));
		}else{
			sql = "select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
					+ " ss.studentNumber from users u, subject_sap_enroll ss where "
					+ "	u.username =ss.studentNumber and ss.subjectCode=:subjectCode and ss.enrollDeEnroll='Y' and "
					+ " ss.acadYearCode=:acadYearCode and ss.programId in(:programIds) and ss.acadSession=:acadSession";
			return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(SubjectSapEnroll.class));
		}
		/*	if (campusId != null && !campusId.isEmpty()) {
				sql = "select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " ss.studentNumber from users u, subject_sap_enroll ss where "
						+ "	u.username =ss.studentNumber and ss.subjectCode=? and ss.enrollDeEnroll='Y' and "
						+ " ss.acadYearCode=? and ss.programId=? and ss.acadSession=? and ss.campusId=?";
				return findAllSQL(sql, new Object[] { subjectCode,
						acadYearCode, programId, acadSession, campusId });
			} else {
				sql = "select distinct u.rollNo as rollNo,concat(u.firstName,' ',u.lastName) as studentName,"
						+ " ss.studentNumber from users u, subject_sap_enroll ss where "
						+ "u.username =ss.studentNumber and ss.subjectCode=? and ss.enrollDeEnroll='Y' and "
						+ " ss.acadYearCode=? and ss.programId=? and ss.acadSession=? ";
				return findAllSQL(sql, new Object[] { subjectCode,
						acadYearCode, programId, acadSession });
			}*/
		

	}

	public List<String> getDistinctNSStudentList(String subjectCode,
			String acadYearCode, String programId, String acadSession,
			String campusId) {
/*
		String sql = "";
		if (campusId != null && !campusId.isEmpty()) {
			sql = "select distinct ss.studentNumber from users u, subject_sap_enroll ss where "
					+ "	u.username =ss.studentNumber and ss.subjectCode=? and ss.enrollDeEnroll='Y' and "
					+ " ss.acadYearCode=? and ss.programId=? and ss.acadSession=? and ss.campusId=?";
			return getJdbcTemplate().queryForList(
					sql,
					new Object[] { subjectCode, acadYearCode, programId,
							acadSession, campusId }, String.class);
		} else {
			sql = "select distinct ss.studentNumber from users u, subject_sap_enroll ss where "
					+ "u.username =ss.studentNumber and ss.subjectCode=? and ss.enrollDeEnroll='Y' and "
					+ " ss.acadYearCode=? and ss.programId=? and ss.acadSession=? ";
			return getJdbcTemplate().queryForList(
					sql,
					new Object[] { subjectCode, acadYearCode, programId,
							acadSession }, String.class);
		}*/
		Map<String, Object> params = new HashMap<String, Object>();
		if (programId.contains(",")) {
			List<String> programList = Arrays.asList(programId.split(","));
			params.put("programIds", programList);
		} else {
			params.put("programIds", programId);
		}
		params.put("subjectCode", subjectCode);
		params.put("acadYearCode", acadYearCode);
		params.put("programId", programId);
		params.put("acadSession", acadSession);
		params.put("campusId", campusId);
		String sql;

		if (campusId != null && !campusId.isEmpty()) {
			sql = "select distinct ss.studentNumber from users u, subject_sap_enroll ss where "
					+ "	u.username =ss.studentNumber and ss.subjectCode=:subjectCode and ss.enrollDeEnroll='Y' and "
					+ " ss.acadYearCode=:acadYearCode and ss.programId in(:programIds) and ss.acadSession=:acadSession and ss.campusId=:campusId";
			return getNamedParameterJdbcTemplate().queryForList(sql, params,
					String.class);
		}else{
			sql = "select distinct ss.studentNumber from users u, subject_sap_enroll ss where "
					+ "	u.username =ss.studentNumber and ss.subjectCode=:subjectCode and ss.enrollDeEnroll='Y' and "
					+ " ss.acadYearCode=:acadYearCode and ss.programId in(:programIds) and ss.acadSession=:acadSession";
			return getNamedParameterJdbcTemplate().queryForList(sql, params, String.class);
		}
	}

}
