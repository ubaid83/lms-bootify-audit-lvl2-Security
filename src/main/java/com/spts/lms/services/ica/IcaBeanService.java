package com.spts.lms.services.ica;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaBeanDAO;
import com.spts.lms.services.BaseService;

@Service("icaBeanService")
@Transactional
public class IcaBeanService extends BaseService<IcaBean> {

	@Autowired
	IcaBeanDAO icaBeanDAO;

	@Override
	protected BaseDAO<IcaBean> getDAO() {

		return icaBeanDAO;
	}

	public List<IcaBean> findIcaListByProgram(String programId, String role, String username) {

		return icaBeanDAO.findIcaListByProgram(programId, role, username);
	}

	public int updateIca(IcaBean ica) {
		return icaBeanDAO.updateIca(ica);
	}

	public IcaBean checkAlreadyExistICA(String moduleId, String acadYear, String campusId, String acadSession) {

		return icaBeanDAO.checkAlreadyExistICA(moduleId, acadYear, campusId, acadSession);
	}

	public List<IcaBean> checkAlreadyExistICAList(String moduleId, String acadYear, String campusId,
			String acadSession) {

		return icaBeanDAO.checkAlreadyExistICAList(moduleId, acadYear, campusId, acadSession);
	}

	public List<IcaBean> getIcaComponents(Long icaId) {

		return icaBeanDAO.getIcaComponents(icaId);
	}

	public List<IcaBean> getComponentsForIca(Long icaId) {

		return icaBeanDAO.getComponentsForIca(icaId);
	}

	public IcaBean getComponentsByCompIdForIca(String icaId, String componentId) {

		return icaBeanDAO.getComponentsByCompIdForIca(icaId, componentId);
	}

	public int updateIcaToSubmitted(String icaId, Date lastModifiedDate) {

		return icaBeanDAO.updateIcaToSubmitted(icaId, lastModifiedDate);
	}

	public List<IcaBean> getSubmittedIca(String username) {
		return icaBeanDAO.getSubmittedIca(username);
	}

	public List<IcaBean> getSubmittedIcaForNonEvent(String username) {
		return icaBeanDAO.getSubmittedIcaForNonEvent(username);
	}

	public List<IcaBean> getfacultyNameMap(String username) {
		return icaBeanDAO.getfacultyNameMap(username);
	}

	public List<IcaBean> getIcaQueries(String username) {
		return icaBeanDAO.getIcaQueries(username);
	}

	public List<IcaBean> getIcaQueriesForNonEvent(String username) {
		return icaBeanDAO.getIcaQueriesForNonEvent(username);
	}

	public List<String> getSubmittedIcaIds(String username) {
		return icaBeanDAO.getSubmittedIcaIds(username);
	}

	public int updateIcaToPublished(String icaId, Date lastModifiedDate, String publishedDate) {
		return icaBeanDAO.updateIcaToPublished(icaId, lastModifiedDate, publishedDate);
	}

	public int updateMultipleIcaToPublished(List<String> icaId, Date lastModifiedDate, String publishedDate) {
		return icaBeanDAO.updateMultipleIcaToPublished(icaId, lastModifiedDate, publishedDate);
	}

	public List<IcaBean> findIcaListByFacultyId(String role, String username) {
		return icaBeanDAO.findIcaListByFacultyId(role, username);
	}

	public List<IcaBean> findDivisionWiseIcaListByParentIca(String parentIcaId) {

		return icaBeanDAO.findDivisionWiseIcaListByParentIca(parentIcaId);
	}

	public List<IcaBean> getIcaIdsByParentIcaIds(Long icaId) {
		return icaBeanDAO.getIcaIdsByParentIcaIds(icaId);
	}

	public List<IcaBean> getSubmittedIcaIdsByParentIcaIds(Long icaId) {
		return icaBeanDAO.getSubmittedIcaIdsByParentIcaIds(icaId);
	}

	public List<IcaBean> icaListByParent(String parentIcaId) {

		return icaBeanDAO.icaListByParent(parentIcaId);
	}

	public List<IcaBean> findIcaStatusListByProgram(String programId, String role, String username) {
		return icaBeanDAO.findIcaStatusListByProgram(programId, role, username);
	}

	public List<IcaBean> findIcaStatusListForAll() {
		return icaBeanDAO.findIcaStatusListForAll();
	}

	public List<IcaBean> findIcaListByProgramForNonEventModule(String programId, String role, String username) {
		return icaBeanDAO.findIcaListByProgramForNonEventModule(programId, role, username);
	}

	public void updateIcaDate(String endDate, String publishedDate, Long icaId) {
		icaBeanDAO.updateIcaDate(endDate, publishedDate, icaId);
	}

	public void updateIcaDateForDivision(String endDate, String publishedDate, List<Long> icaIds) {
		icaBeanDAO.updateIcaDateForDivision(endDate, publishedDate, icaIds);
	}

	public List<IcaBean> findIcaListByProgramForSupportAdmin() {
		return icaBeanDAO.findIcaListByProgramForSupportAdmin();
	}

	public List<IcaBean> findIcaListByProgramForNonEventModuleForSupportAdmin() {
		return icaBeanDAO.findIcaListByProgramForNonEventModuleForSupportAdmin();
	}

	public List<IcaBean> findDivisionWiseIcaListByParentIcaForSupportAdmin(String parentIcaId) {
		return icaBeanDAO.findDivisionWiseIcaListByParentIcaForSupportAdmin(parentIcaId);
	}

	public List<String> getIcaStatusesForDivisionIca(Long icaId) {
		return icaBeanDAO.getIcaStatusesForDivisionIca(icaId);
	}

	public List<IcaBean> findIcaListByProgramForBatchWise(String programId, String role, String username) {
		return icaBeanDAO.findIcaListByProgramForBatchWise(programId, role, username);
	}

	public List<IcaBean> getSubmittedIcaForBatchWise(String username) {
		return icaBeanDAO.getSubmittedIcaForBatchWise(username);
	}

	public List<IcaBean> getIcaQueriesForApproveAll(String username) {
		return icaBeanDAO.getIcaQueriesForApproveAll(username);
	}

	public void updateIcaDateWithoutSubmit(String endDate, String publishedDate, Long icaId) {
		icaBeanDAO.updateIcaDateWithoutSubmit(endDate, publishedDate, icaId);
	}

	public void updateIcaDateForDivisionWithoutSubmit(String endDate, String publishedDate, List<Long> icaIds) {
		icaBeanDAO.updateIcaDateForDivisionWithoutSubmit(endDate, publishedDate, icaIds);
	}

	// Coursera Chnages

	public List<IcaBean> checkAlreadyExistICAListCE(String moduleId, String acadYear, String campusId,
			List<String> acadSession) {
		return icaBeanDAO.checkAlreadyExistICAListCE(moduleId, acadYear, campusId, acadSession);
	}

	public IcaBean findByCampusid(String campusId) {
		// TODO Auto-generated method stub
		return icaBeanDAO.findByCampusid(campusId);
	}

	public void updateIcaInternalPassMarks(String internalPassMarks, Long icaId) {
		icaBeanDAO.updateIcaInternalPassMarks(internalPassMarks, icaId);
	}

	public IcaBean presentIcaInternalPassMarks(String icaId) {
		return icaBeanDAO.presentupdateIcaInternalPassMarks(icaId);
	}

	public void updateParentIcaIdInternalPassMarks(String internalPassMarks, Long ParentIcaId) {
		icaBeanDAO.updateParentIcaIdInternalPassMarks(internalPassMarks, ParentIcaId);

	}

	// new queries
	public int insertInIcaTests(IcaBean i) {
		return icaBeanDAO.insertInIcaTests(i);
	}

	public IcaBean getIcaTests(Long icaId, Long courseId) {

		return icaBeanDAO.getIcaTests(icaId, courseId);
	}

	public IcaBean getAllIcaTests(Long icaId, Long courseId) {

		return icaBeanDAO.getAllIcaTests(icaId, courseId);
	}

	public int updateInIcaTests(IcaBean i) {
		return icaBeanDAO.updateInIcaTests(i);
	}

	public int getIcaTestNC(Long icaId) {
		return icaBeanDAO.getIcaTestNC(icaId);
	}

	public int getIcaTestCompleted(Long icaId) {
		return icaBeanDAO.getIcaTestCompleted(icaId);
	}

	public int clearIcaTests(IcaBean i) {
		return icaBeanDAO.clearIcaTests(i);
	}

	public List<IcaBean> icaSubmittedListByParent(String parentIcaId) {

		return icaBeanDAO.icaSubmittedListByParent(parentIcaId);
	}

	public List<IcaBean> getSubmittedIcaComps(String username) {
		return icaBeanDAO.getSubmittedIcaComps(username);
	}
	public List<IcaBean> getSubmittedIcaCompsNE(String username) {
		return icaBeanDAO.getSubmittedIcaCompsNE(username);
	}

	//Peter 25/10/2021
	public IcaBean checkIfComponentIdExists(String componentId) {
		return icaBeanDAO.checkIfComponentIdExists(componentId);
	}

	//Peter 04/12/2021
	public IcaBean checkIfExistsInDB(String columnName, String value) {
		return icaBeanDAO.checkIfExistsInDB(columnName,value);
	}
}
