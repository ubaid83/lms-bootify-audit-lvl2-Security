package com.spts.lms.services.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.group.StudentGroup;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.message.StudentMessage;
import com.spts.lms.beans.query.QueryResponse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.message.StudentMessageDAO;
import com.spts.lms.daos.query.QueryResponseDAO;
import com.spts.lms.services.BaseService;

@Service("queryResponseService")
public class QueryResponseService extends BaseService<QueryResponse> {

	@Autowired
	private QueryResponseDAO queryResponseDAO;

	@Override
	public BaseDAO<QueryResponse> getDAO() {
		return queryResponseDAO;
	}

	public List<QueryResponse> findMyQuery(String username) {
		return queryResponseDAO.findMyQuery(username);
	}

	public QueryResponse findByQueryId(Long queryId) {
		return queryResponseDAO.findByQueryId(queryId);
	}

}
