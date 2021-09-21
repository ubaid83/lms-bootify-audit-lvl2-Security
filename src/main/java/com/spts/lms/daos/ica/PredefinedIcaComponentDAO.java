package com.spts.lms.daos.ica;

import java.util.List;

import org.springframework.stereotype.Repository;


import com.spts.lms.beans.ica.PredefinedIcaComponent;
import com.spts.lms.daos.BaseDAO;

@Repository("predefinedIcaComponentDAO")
public class PredefinedIcaComponentDAO extends BaseDAO<PredefinedIcaComponent> {

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return "pre_def_ica_components";
	}

	@Override
	protected String getInsertSql() {
		String sql = " Insert into pre_def_ica_components (id,componentName) values (:id,:componentName) " ;

		return sql;
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
	
	public PredefinedIcaComponent findByComponentName(String componentName)
	{
		String sql="select id,componentName from pre_def_ica_components where componentName = ? and active = 'Y' ";
		
		return findOneSQL(sql, new Object[]{componentName});
				
		
		//return findAllSQL(sql, new Object[]{});
	}

}
