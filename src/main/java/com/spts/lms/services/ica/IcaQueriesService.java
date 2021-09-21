package com.spts.lms.services.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaQueries;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaQueriesDAO;
import com.spts.lms.services.BaseService;
import com.spts.lms.web.utils.Utils;
@Service("icaQueriesService")
@Transactional
public class IcaQueriesService extends BaseService<IcaQueries>{

	
	@Autowired
	IcaQueriesDAO icaQueriesDAO;
	
	
	@Override
	protected BaseDAO<IcaQueries> getDAO() {
		// TODO Auto-generated method stub
		return icaQueriesDAO;
	}
	
	public IcaQueries findByIcaId(Long icaId){
		
		
		return icaQueriesDAO.findByIcaId(icaId);
	}
	
	
	public IcaQueries findByIcaIdAndCompId(String icaId,String compId){
		
		
		return icaQueriesDAO.findByIcaIdAndCompId(icaId,compId);
	}	
	public List<IcaQueries> findAllQueriesByIcaId(Long icaId){
		return icaQueriesDAO.findAllQueriesByIcaId(icaId);
	}
	
	public void updateReEvaluated(Long icaId){
		icaQueriesDAO.updateReEvaluated(icaId);
	}
	
	public void updateReEvaluated(Long icaId,String componentId){
		icaQueriesDAO.updateReEvaluated(icaId,componentId);
	}
	
	public void updateIsApproved(String icaId,String username,String filePath,String componentId){
		 icaQueriesDAO.updateIsApproved(icaId, username, filePath, componentId);
	}

	public IcaQueries findByIcaIdIsApproved(String icaId) {
		return  icaQueriesDAO.findByIcaIdIsApproved(icaId);
	}
}
