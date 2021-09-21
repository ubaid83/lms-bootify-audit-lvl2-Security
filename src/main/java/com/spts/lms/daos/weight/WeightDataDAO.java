package com.spts.lms.daos.weight;


import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.beans.course.CourseSyllabus;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.weight.Weight;
import com.spts.lms.beans.weight.WeightData;
import com.spts.lms.daos.BaseDAO;

@Repository("weightDataDAO")
public class WeightDataDAO extends BaseDAO<WeightData> {

	@Override
	protected String getTableName() {

		return "weightdata";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " (weightType, courseId, "
				+ " weightAssigned , createdBy , createdDate , lastModifiedBy , lastModifiedDate ) values"
				+ "(:weightType, :courseId, :weightAssigned , :createdBy , :createdDate ,:lastModifiedBy , :lastModifiedDate)";
		return sql;
	}

	@Override
	protected String getUpdateSql() {

		String sql = "";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	/*
	 * public List<WeightData> findByWeightId(Long weightId) { String sql =
	 * "select wd.* from weight w inner join weightdata wd on wd.weightId = w.id where w.id = ?"
	 * ; return findAllSQL(sql, new Object[] { weightId }); }
	 */

	public List<WeightData> findByCourseId(Long courseId) {
		String sql = "select w.*, c.*, w.id as id from weightdata w "
				+ " inner join course c on c.id = w.courseId where w.courseId = ? ";
		return findAllSQL(sql, new Object[] { courseId });
	}

	public WeightData findByWeightTypeAndCourseId(String weightType,
			Long courseId) {
		String sql = "select * from weightdata w where weightType = ? and courseId = ?";
		return findOneSQL(sql, new Object[] { weightType, courseId });
	}
	

}
