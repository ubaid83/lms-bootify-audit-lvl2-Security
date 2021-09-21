package com.spts.lms.services.tee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.tee.TeeQueries;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.tee.TeeQueriesDAO;
import com.spts.lms.services.BaseService;

@Service("teeQueriesService")
@Transactional
public class TeeQueriesService extends BaseService<TeeQueries>{
	
	@Autowired
	TeeQueriesDAO teeQueriesDAO;
	

	@Override
	protected BaseDAO<TeeQueries> getDAO() {
		// TODO Auto-generated method stub
		return teeQueriesDAO;
	}
	
	public TeeQueries findByTeeId(Long teeId){
		return teeQueriesDAO.findByTeeId(teeId);
	}
	
	
	public void updateReEvaluated(Long teeId) {
		 teeQueriesDAO.updateReEvaluated(teeId);
	}

}
