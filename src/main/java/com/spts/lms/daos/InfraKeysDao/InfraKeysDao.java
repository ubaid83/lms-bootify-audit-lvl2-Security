package com.spts.lms.daos.InfraKeysDao;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.InfraKeys.InfraKeys;
import com.spts.lms.daos.BaseDAO;

@Repository("infraKeysDao")
public class InfraKeysDao extends BaseDAO<InfraKeys> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "infra_keys";
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
	
	public InfraKeys findKeys() {
		final String sql = "Select secret_token, private_key, public_key from "
				+ getTableName() + " limit 1";
		return findOneSQL(sql, new Object[] {  });
	}
	
	public String findSlugFromAbbr(String abbr) {
		String sql = "SELECT slug from infra_school_map where abbr = ? limit 1 ";
		return getJdbcTemplate().queryForObject(sql, new Object[] { abbr },
				String.class);
	}

}
