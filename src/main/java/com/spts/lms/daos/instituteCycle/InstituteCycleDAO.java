package com.spts.lms.daos.instituteCycle;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import com.spts.lms.beans.instituteCycle.InstituteCycle;
import com.spts.lms.daos.BaseDAO;

@Repository("instituteCycleDAO")
public class InstituteCycleDAO extends BaseDAO<InstituteCycle> {

	@Override
	protected String getTableName() {

		return "instituteCycle";
	}

	@Override
	protected String getInsertSql() {

		String sql = " INSERT INTO institutecycle "
				+ " ( "
				+ " year, "
				+ " month, "
				+ " cycleType, "
				+ " live, "
				+ " createdBy, "
				+ " createdDate, "
				+ " lastModifiedBy, "
				+ " lastModifiedDate) "
				+ " VALUES "
				+ " ( "
				+ " :year, "
				+ " :month, "
				+ " :cycleType, "
				+ " :live, "
				+ " :createdBy, "
				+ " :createdDate, "
				+ " :lastModifiedBy, "
				+ " :lastModifiedDate) ";
		
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		
		String sql = " UPDATE institutecycle "
				+ " SET "
				+ " year = :year, "
				+ " month = :month, "
				+ " cycleType = :cycleType, "
				+ " live = :live, "
				+ " lastModifiedBy = :lastModifiedBy, "
				+ " lastModifiedDate = :lastModifiedDate "
				+ " WHERE id = :id ";
		
		return sql;
	}

	

	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	@Override
	@Caching(evict = {
			@CacheEvict(value="acad_cycle", allEntries = true),
			@CacheEvict(value="user_courses", allEntries = true)
	})
	public int update(InstituteCycle bean) {
		return super.update(bean);
	}
	
	
}
