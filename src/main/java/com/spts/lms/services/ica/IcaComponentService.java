package com.spts.lms.services.ica;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaComponent;
import com.spts.lms.beans.ica.IcaComponentMarks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaComponentDAO;
import com.spts.lms.services.BaseService;

@Service("icaComponentService")
@Transactional
public class IcaComponentService extends BaseService<IcaComponent> {

	@Autowired
	private IcaComponentDAO icaComponentDAO;

	@Override
	protected BaseDAO<IcaComponent> getDAO() {

		return icaComponentDAO;
	}

	public List<IcaComponent> icaComponentListByIcaId(String icaId,
			String campusId) {

		return icaComponentDAO.icaComponentListByIcaId(icaId, campusId);
	}

	public List<IcaComponent> icaComponentListByIcaId(Long icaId) {

		return icaComponentDAO.icaComponentListByIcaId(icaId);
	}
	
	public List<IcaComponent> icaComponentListByParentIcaId(Long icaId) {

		return icaComponentDAO.icaComponentListByParentIcaId(icaId);
	}
	
	public IcaComponent icaComponentByIcaId(Long icaId,String raiseQComp) {

		return icaComponentDAO.icaComponentByIcaId(icaId,raiseQComp);
	}
	
	
	
	public int icaTotalComponentMarks(Long icaId){
		return icaComponentDAO.icaTotalComponentMarks(icaId);
	}
	public List<IcaComponent> getComponentsByParam(String acadYear,
			String acadSession, String programId, String campusId) {
		
		return icaComponentDAO.getComponentsByParam(acadYear, acadSession, programId, campusId);
	}
	//26-04-2021
	public List<IcaComponent> getComponentsByParamMultiSessionCoursera(String acadYear, String acadSession, String programId,
			String campusId) {
		return icaComponentDAO.getComponentsByParamMultiSessionCoursera(acadYear,acadSession,programId,campusId);
	}
	public List<IcaComponent> getComponentsByParamMultiSession(String acadYear, String acadSession, String programId,
			String campusId) {
		
		return icaComponentDAO.getComponentsByParamMultiSession(acadYear,acadSession,programId,campusId);
	}
	
	public List<IcaComponent> getComponentsByParamDraft(String acadYear,
			String acadSession, String programId, String campusId) {
		
		return icaComponentDAO.getComponentsByParamDraft(acadYear, acadSession, programId, campusId);
	}
	public List<IcaComponent> getComponentsByParamFaculty(String acadYear, String username) {
		
		return icaComponentDAO.getComponentsByParamFaculty(acadYear ,username);
	}
	
	public Double getCompMarks(Long icaId,Long componentId) {
		return icaComponentDAO.getCompMarks(icaId,componentId);
	}
	public IcaComponent getCompBean(Long icaId,Long componentId) {
		return icaComponentDAO.getCompBean(icaId, componentId);
	}
	
	public void updateIcaCompToSubmitted(String icaId,String componentId) {
		
		icaComponentDAO.updateIcaCompToSubmitted(icaId, componentId);
	}
	
	public int getTotalComponents(String icaId) {
		
		return icaComponentDAO.getTotalComponents(icaId);
	}
	
	public int getSubmittedComponents(String icaId) {
		
		return icaComponentDAO.getSubmittedComponents(icaId);
	}
	
	public int updateIcaCompsToPublished(String icaId,String compId, Date lastModifiedDate, String publishedDate) {
		return icaComponentDAO.updateIcaCompsToPublished(icaId,compId, lastModifiedDate, publishedDate);
	}
	
	public int getMaxSeqNo(Long icaId) {
		return icaComponentDAO.getMaxSeqNo(icaId);
	}
	public IcaComponent getSubmittedIcaComponent(Long icaId) {
		
		return icaComponentDAO.getSubmittedIcaComponent(icaId);
	}
	public int getSubmittedSeqNo(Long icaId) {
		return icaComponentDAO.getSubmittedSeqNo(icaId);
	}
	
	public List<IcaComponent> icaSubmittedListByIcaId(String icaId) {
		return icaComponentDAO.icaSubmittedListByIcaId(icaId);
	}
}
