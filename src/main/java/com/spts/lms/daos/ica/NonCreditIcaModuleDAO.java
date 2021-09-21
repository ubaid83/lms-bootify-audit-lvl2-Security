package com.spts.lms.daos.ica;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.ica.NonCreditIcaModule;
import com.spts.lms.daos.BaseDAO;

@Repository("nonCreditIcaModuleDAO")
public class NonCreditIcaModuleDAO extends BaseDAO<NonCreditIcaModule>{

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return null;
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
	
	public List<NonCreditIcaModule> getModuleIdByProgramId(String programId, String acadYear) {
		String sql = " select * from module  where program_id=?"
				+ " AND module_credit like '0.000%' and acadYear=?";

		return findAllSQL(sql, new Object[] { programId,acadYear });
	}

}
