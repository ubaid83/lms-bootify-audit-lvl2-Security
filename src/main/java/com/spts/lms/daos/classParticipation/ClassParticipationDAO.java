package com.spts.lms.daos.classParticipation;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;

@Repository("classParticipationDAO")
public class ClassParticipationDAO extends BaseDAO<ClassParticipation> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "classparticipation";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (username,score,remarks,facultyId,courseId,createdBy,createdDate,acadMonth,acadYear,"
				+ "lastModifiedBy,lastModifiedDate,active)"

				+ " VALUES(:username,:score,:remarks,:facultyId,:courseId,:createdBy,:createdDate,:acadMonth,:acadYear,:lastModifiedBy,:lastModifiedDate,'Y')";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " score=:score," + " remarks =:remarks,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate "

				+ " where username = :username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ClassParticipation> findStudentsForFaculty(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, cp.score, cp.remarks , u.campusId, u.campusName"
				+ "  from user_course uc "
				+ "  inner join users u on uc.username = u.username"
				+ "  inner join program p on u.programId = p.id"
				+ " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ "  where" + "  uc.role = ? and uc.active = 'Y' and uc.courseId = ?   ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId });
	}

	public List<String> findAllStudentUsernames(Long courseId) {
		String sql = "select username from classparticipation where courseId = ?";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { courseId });
	}

	public List<ClassParticipation> findByStudent(String username) {
		String sql = "select * from classparticipation where username = ?";
		return findAllSQL(sql, new Object[] { username });
	}

	public List<ClassParticipation> findByStudent(String username,
			String courseId) {
		String sql = "select * from classparticipation where username = ? and courseId = ? ";
		return findAllSQL(sql, new Object[] { username, courseId });
	}
	
	public List<ClassParticipation> getCPForGradeCenter(String username,
			String courseId) {
		String sql = " select distinct 'Class Participation' as nameToShow,(case when cp.score is null then 'NE' else cp.score end) as score, cp.remarks "
				+ " from users u inner join user_course uc on uc.username = u.username and uc.role = 'ROLE_STUDENT' "
				+ " left outer join classparticipation cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ " where uc.courseId=? and cp.username=? ";
		return findAllSQL(sql, new Object[] { courseId, username });
	}

}
