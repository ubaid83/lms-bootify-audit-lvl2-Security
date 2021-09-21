package com.spts.lms.daos.weight;

import java.util.List;

import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.weight.Component;
import com.spts.lms.daos.BaseDAO;

@Repository("componentDAO")
public class ComponentDAO extends BaseDAO<Component> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "component";
	}

	@Override
	protected String getInsertSql() {
		return "INSERT INTO "
				+ getTableName()
				+ " (compName,username,score,remarks,facultyId,courseId,createdBy,createdDate,"
				+ "lastModifiedBy,lastModifiedDate,active)"

				+ " VALUES(:compName,:username,:score,:remarks,:facultyId,:courseId,:createdBy,:createdDate,:lastModifiedBy,:lastModifiedDate,'Y')";
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update " + getTableName() + " set "
				+ " score=:score," + " remarks =:remarks,"
				+ " lastModifiedBy = :lastModifiedBy,"
				+ " lastModifiedDate =:lastModifiedDate, "
				+ " compName =compName " + " where username = :username ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Component> findStudentsForFaculty(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, cp.score, cp.remarks,cp.compName "
				+ "  from user_course uc "
				+ "  inner join users u on uc.username = u.username"
				+ "  inner join program p on u.programId = p.id"
				+ " left outer join "
				+ getTableName()
				+ " cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ "  where" + "  uc.role = ? and uc.courseId = ?   ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId });
	}

	public List<Component> findStudentsForCompnent(Long courseId,
			String compName) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, cp.score, cp.remarks,cp.compName "
				+ "  from user_course uc "
				+ "  inner join users u on uc.username = u.username"
				+ "  inner join program p on u.programId = p.id"
				+ " left outer join "
				+ getTableName()
				+ " cp on cp.username = u.username and cp.courseId = uc.courseId "
				+ "  where"
				+ "  uc.role = ? and uc.courseId = ?  and cp.compName = ? ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId, compName });
	}

	public List<String> findAllStudentUsernames(Long courseId) {
		String sql = "select username from " + getTableName()
				+ " where courseId = ?";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { courseId });
	}

	public List<Component> findByStudent(String username) {
		String sql = "select * from " + getTableName() + " where username = ?";
		return findAllSQL(sql, new Object[] { username });
	}
	
	public List<Component> findByStudent(String username, String courseId) {
		String sql = "select * from " + getTableName() + " where username = ? and courseId = ?";
		return findAllSQL(sql, new Object[] { username,courseId });
	}

	public Component findByCourseAndStudentAndComp(String courseId,
			String username, String compName) {
		String sql = "select * from " + getTableName()
				+ " where courseId = ? and username = ? and compName = ?";
		return findOneSQL(sql, new Object[] { courseId, username, compName });
	}

	public List<String> findCompNamesByCourseId(String courseId) {
		String sql = "select distinct compName from component where courseId = ? ";
		return getJdbcTemplate().queryForList(sql, new Object[] { courseId },
				String.class);
	}

	public void deleteComponent(String courseId, String compName) {
		executeUpdateSql(
				"delete from component where courseId = ? and compName = ?",
				new Object[] { courseId, compName });
	}

}
