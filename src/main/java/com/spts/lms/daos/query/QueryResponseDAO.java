package com.spts.lms.daos.query;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.beans.query.Query;
import com.spts.lms.beans.query.QueryResponse;
import com.spts.lms.daos.BaseDAO;

@Repository("queryResponseDAO")
public class QueryResponseDAO extends BaseDAO<QueryResponse> {

	@Override
	protected String getTableName() {
		return "queryresponse";
	}

	@Override
	protected String getInsertSql() {

		String sql = "insert into "
				+ getTableName()
				+ "(queryId, username, queryDesc, queryResponse , queryRespondedBy , queryRespondedTime) values "
				+ "(:queryId, :username, :queryDesc,  :queryResponse , :queryRespondedBy , :queryRespondedTime) ";
		return sql;
	}

	@Override
	protected String getUpdateSql() {
		final String sql = "Update queryresponse set "
				+ "courseId = :courseId," + "where id = :id ";
		return sql;
	}

	@Override
	protected String getUpsertSql() {
		return null;
	}

	public List<QueryResponse> findMyQuery(String username) {
		final String sql = "select * from query q , queryresponse qr where q.queryDesc=qr.queryDesc and q.username = ? ";
		return findAllSQL(sql, new Object[] { username });
	}

	public QueryResponse findByQueryId(Long queryId) {
		String sql = "select * from queryresponse where queryId = ?";
		return findOneSQL(sql, new Object[] { queryId });
	}
}