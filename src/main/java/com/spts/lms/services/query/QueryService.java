package com.spts.lms.services.query;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spts.lms.beans.grievances.Grievances;
import com.spts.lms.beans.message.Message;
import com.spts.lms.beans.query.Query;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.message.MessageDAO;
import com.spts.lms.daos.query.QueryDAO;
import com.spts.lms.services.BaseService;


@Service("queryService")
public class QueryService extends BaseService<Query>{
	
@Autowired 
private QueryDAO queryDAO;

@Override
public BaseDAO<Query> getDAO() {
	return queryDAO;
}

public List<Query> findAllQueries(String username){
	return queryDAO.findAllQueries(username);
}


public List<Query> findAllQueries(){
	return queryDAO.findAllQueries();
}

public List<Query> getQueryStats(String campus, String fromDate ,String toDate) {
	return queryDAO.getQueryStats(campus, fromDate, toDate);
}

}
