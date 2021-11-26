package com.spts.lms.services.StudentService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.StudentService.LorRegDetails;
import com.spts.lms.beans.StudentService.LorRegStaff;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.StudentService.LorRegDetailsDAO;
import com.spts.lms.services.BaseService;

@Service("lorRegDetailsService")
@Transactional
public class LorRegDetailsService extends BaseService<LorRegDetails> {

	@Autowired
	LorRegDetailsDAO lorRegDetailsDAO;
	
	@Override
	protected BaseDAO<LorRegDetails> getDAO() {
		
		return lorRegDetailsDAO;
	}
	public List<LorRegDetails> getPendingAppicationForApproval(String staffId){
		return lorRegDetailsDAO.getPendingAppicationForApproval(staffId);
	}
//	
//	public Long insertandreturnId(LorRegDetails lorRegDetails) {
//		// TODO Auto-generated method stub
//		return lorRegDetailsDAO.insertandreturnId(lorRegDetails);
//	}
	
	public LorRegDetails getLorDetailsById(String lorRegId,String staffId){
		return lorRegDetailsDAO.getLorDetailsById(lorRegId,staffId);
	}
	
	public  LorRegDetails findByCountryName(final String country_name) {
		return lorRegDetailsDAO.findByCountryName(country_name);
		
	}
	
	

	
}
