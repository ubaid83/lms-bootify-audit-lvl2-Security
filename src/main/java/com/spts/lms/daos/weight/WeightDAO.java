package com.spts.lms.daos.weight;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.weight.Weight;
import com.spts.lms.daos.BaseDAO;

@Repository("weightDAO")
public class WeightDAO extends BaseDAO<Weight> {

	@Override
	protected String getTableName() {

		return "weight";
	}

	@Override
	protected String getInsertSql() {

		String sql = "Insert into "
				+ getTableName()
				+ " (courseId, internal, external, passInternal, total, createdBy , createdDate , lastModifiedBy , lastModifiedDate ) values"
				+ "(:courseId, :internal, :external, :passInternal, :total, :createdBy , :createdDate ,:lastModifiedBy , :lastModifiedDate)";
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

	/*public List<Weight> findByCourseId(Long courseId) {
		String sql = "select w.*, c.*, w.id as id from weight w "
				+ " inner join course c on c.id = w.courseId where w.courseId = ? ";
		return findAllSQL(sql, new Object[] { courseId });
	}*/

	/*
	 * public List<Weight> findByCourseName(String courseName){ String sql =
	 * "select w.* from weight w inner join course c on c.id = w.courseId where c.courseName = ?"
	 * ; return findAllSQL(sql, new Object[]{courseName}); }
	 */
	public Weight findByCourseName(String courseName) {
		String sql = "select w.* from weight w inner join course c on c.id = w.courseId where c.courseName = ?";
		return findOneSQL(sql, new Object[] { courseName });
	}

	public List<String> findWeightType() {
		String sql = "select * from weighttype";
		return getJdbcTemplate().queryForList(sql, String.class,
				new Object[] {});
	}

}
