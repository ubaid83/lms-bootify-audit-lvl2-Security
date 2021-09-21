package com.spts.lms.services.ica;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.beans.ica.NsBean;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.NsDAO;
import com.spts.lms.services.BaseService;

@Service("nsService")
@Transactional
public class NsService extends BaseService<NsBean> {

	@Autowired
	NsDAO nsDAO;

	@Override
	protected BaseDAO<NsBean> getDAO() {

		return nsDAO;
	}

	public List<NsBean> findNsListByProgram(String username) {
		return nsDAO.findNsListByProgram(username);
	}

	public int updateNCIcaToPublished(String icaId, Date lastModifiedDate) {

		return nsDAO.updateNCIcaToPublished(icaId, lastModifiedDate);
	}

	public NsBean checkAlreadyExistNS(String moduleId, String acadYear,String programId,
			String campusId, String acadSession) {

		return nsDAO.checkAlreadyExistNS(moduleId, acadYear, programId,campusId,
				acadSession);
	}
	
	public NsBean actionNS(Long id) {
		return nsDAO.actionNS(id);
	}
	
 public List<NsBean> getNcGradedByUser(String username) {
		
		return nsDAO.getNcGradedByUser(username);
	}
	
}
