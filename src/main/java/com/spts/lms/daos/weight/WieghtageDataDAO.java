/*package com.spts.lms.daos.wieghtage;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.beans.course.CourseSyllabus;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.wieghtage.WieghtageData;
import com.spts.lms.daos.BaseDAO;

@Repository("wieghtageDataDAO")
public class WieghtageDataDAO extends BaseDAO<WieghtageData> {

	@Override
	protected String getTableName() {

		return "wieghtagedata";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " (courseId, wieghtagetype,  "
				+ " wieghtageassigned , createdBy , createdDate , lastModifiedBy , lastModifiedDate ) values"
				+ "(:courseId, :wieghtagetype, :wieghtageassigned , :createdBy , :createdDate ,:lastModifiedBy , :lastModifiedDate)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {

		String sql = "update  " + getTableName() + " set courseId=:courseId, "

		+ " wieghtagetype=:wieghtagetype, "
				+ " wieghtageassigned=:wieghtageassigned, "

				+ " where id=:id";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<WieghtageData> findWieghtageByCourse(Long courseId) {
		final String sql = "select * from wieghtagedata w where w.courseId= ? ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public String showWieghtage(Long courseId) {
		final String sql = " Select wd.wieghtageassigned from wieghtagedata wd where wd.courseId= ? and wd.wieghtagetype='test' ";
		return returnSingleColumn(sql, new Object[] { courseId });
	}

	public List<WieghtageData> showWieghtageForAssignment(Long courseId) {
		final String sql = "select wd.wieghtagetype , wd.wieghtageassigned from wieghtagedata wd where (wd.wieghtagetype='Viva' or wd.wieghtagetype='Presentation' or wd.wieghtagetype='WrittenAssignment' or wd.wieghtagetype='ReportWriting') and (wd.courseId= ? ) ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public List<String> showWieghtageForCP(Long courseId) {
		final String sql = "select wd.wieghtageassigned from wieghtagedata wd  where wd.wieghtagetype='ClassParticipation' and wd.courseId= ?  ";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] { courseId });
	}

	public List<WieghtageData> findAddedWeightageByCourseIdAndWeightageType(
			Long courseId, String weightageType) {
		String sql = "select * from wieghtagedata where courseId = ? and wieghtagetype = ?";
		return findAllSQL(sql, new Object[] { courseId, weightageType });
	}
}
*/