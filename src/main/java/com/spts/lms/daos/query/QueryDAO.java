package com.spts.lms.daos.query;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.beans.query.Query;
import com.spts.lms.daos.BaseDAO;

@Repository("queryDAO")
public class QueryDAO extends BaseDAO<Query> {

	@Override
	protected String getTableName() {
		return "query";
	}

	@Override
	protected String getInsertSql() {

		String sql = "insert into "
				+ getTableName()
				+ "(username, courseId, queryDesc, queryResponse , queryResponseTime,  queryCreatedTime) values "
				+ "(:username, :courseId, :queryDesc, :queryResponse,:queryResponseTime ,:queryCreatedTime) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update query set " + "courseId = :courseId,"
				 + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}
	
	public List<Query> findAllQueries(String username){
		final String sql=" select distinct * from query q  where q.username = ? order by q.queryCreatedTime "  ;
		return findAllSQL(sql, new Object[] {username});
	}
	
	public List<Query> findAllQueries(){
		final String sql=" select distinct * from query q  order by q.queryCreatedTime "  ;
		return findAllSQL(sql, new Object[] {});
	}
	
	public List<Query> getQueryStats(String campus, String fromDate ,String toDate) {
		
		String typeCondition ="";
		if(campus !=null){
			typeCondition =" u.campusId = ? ";
		}else{
			typeCondition =" u.campusId is ? " ;
		}
		
		String sql = "select u.*,t.* from query t , user_roles ur , users u where t.username = ur.username"
				+ " and u.username = ur.username and u.`type`is null and ur.role='ROLE_STUDENT' and t.active ='Y' and u.active = 'Y' and "+ typeCondition +" "
				+ " and   t.queryCreatedTime BETWEEN  ? and ? ";
		return findAllSQL(sql, new Object[] { campus, fromDate, toDate });
	}

}