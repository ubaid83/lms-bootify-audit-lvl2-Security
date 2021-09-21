package com.spts.lms.services.instituteCycle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.instituteCycle.InstituteCycle;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.instituteCycle.InstituteCycleDAO;
import com.spts.lms.services.BaseService;

@Service("instituteCycleService")
@Transactional
public class InstituteCycleService extends BaseService<InstituteCycle> {
	
	@Autowired
	private InstituteCycleDAO instituteCycleDAO;
	
		
	@Override
	public BaseDAO<InstituteCycle> getDAO() {
		return instituteCycleDAO;
	}

	public List<InstituteCycle> findIfCycleExists(InstituteCycle instituteCycle) {
		String sql = "Select * from instituteCycle where cycleType = ? and month = ? and year = ? ";
		return findAllSQL(sql, new Object[]{
				instituteCycle.getCycleType(),
				instituteCycle.getMonth(),
				instituteCycle.getYear()
		});
	}


	public void makeCyclesInactive(InstituteCycle instituteCycle) {
		String sql = "Update instituteCycle set live = 'N' where cycleType = :cycleType and live = 'Y' ";
		getDAO().updateSQL(instituteCycle, sql);
	}


	public void updateInstituteCycle(InstituteCycle instituteCycleDb) {
		instituteCycleDAO.update(instituteCycleDb);
		
	}

}
