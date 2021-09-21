package com.spts.lms.daos.selectives;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.selectives.SelectiveEvents;
import com.spts.lms.daos.BaseDAO;

@Repository("selectiveEventsDAO")
public class SelectiveEventsDAO extends BaseDAO<SelectiveEvents> {

	@Override
	protected String getTableName() {
		return "selective_events";
	}

	@Override
	protected String getInsertSql() {

		String str = " INSERT INTO " + getTableName() + " (title, selective_type, active, "
				+ " createdBy, createdDate, lastModifiedBy, lastModifiedDate) " + " VALUES "
				+ " (:title, :selective_type, 'Y', "
				+ " :createdBy, :createdDate, :lastModifiedBy, :lastModifiedDate) ";
		return str;
	}

	@Override
	protected String getUpdateSql() {

		String str = " UPDATE " + getTableName() + " SET "
				+ " title=:title, selective_type=:selective_type, active=:active, "
				+ " createdBy=:createdBy, createdDate=:createdDate, "
				+ " lastModifiedBy=:lastModifiedBy, lastModifiedDate=:lastModifiedDate " + " WHERE id=:id ";
		return str;
	}

	@Override
	protected String getUpsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<SelectiveEvents> getSelectiveEventsList(String role,String username) {
		if (role.equals("ROLE_ADMIN")) {
			String sql = " SELECT * FROM selective_events WHERE (createdBy =? or lastModifiedBy = ?) AND active = 'Y' ";
			
			return findAllSQL(sql, new Object[] {role,role});
		} else {
			String sql  =" SELECT se.* FROM selective_events se,selective_students ss WHERE se.id=ss.selective_id " + 
					" AND ss.username = ? AND ss.active ='Y' AND se.active='Y' and se.startDate<=sysdate() " ;
			
			return findAllSQL(sql, new Object[] {username});
		}

	}

}
