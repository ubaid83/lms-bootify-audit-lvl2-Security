package com.spts.lms.services.ica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.IcaComponentMarks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaComponentMarksDAO;
import com.spts.lms.services.BaseService;

@Service("icaComponentMarksService")
@Transactional
public class IcaComponentMarksService extends BaseService<IcaComponentMarks> {

	@Autowired
	IcaComponentMarksDAO icaComponentMarksDAO;

	@Override
	protected BaseDAO<IcaComponentMarks> getDAO() {

		return icaComponentMarksDAO;
	}

	public List<IcaComponentMarks> icaComponentMarksByIcaId(Long icaId) {

		return icaComponentMarksDAO.icaComponentMarksByIcaId(icaId);
	}

	public List<IcaComponentMarks> icaComponentMarksByIcaId(Long icaId, String componentId) {

		return icaComponentMarksDAO.icaComponentMarksByIcaId(icaId, componentId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByUser(String username) {
		return icaComponentMarksDAO.getIcaComponentMarksByUser(username);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByUserForNonEvent(String usrename) {
		return icaComponentMarksDAO.getIcaComponentMarksByUserForNonEvent(usrename);
	}
	
	public List<IcaComponentMarks> getIcaPublishedComponentMarksByUserForNonEvent(String usrename) {
		return icaComponentMarksDAO.getIcaPublishedComponentMarksByUserForNonEvent(usrename);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByIcaId(String icaId) {
		return icaComponentMarksDAO.getIcaComponentMarksByIcaId(icaId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdForNonEvent(String icaId) {
		return icaComponentMarksDAO.getIcaComponentMarksByIcaIdForNonEvent(icaId);
	}

	public String checkWhetherGradingStartOrNotP(String icaId) {

		return icaComponentMarksDAO.checkWhetherGradingStartOrNotP(icaId);
	}

	public String checkWhetherGradingStartOrNot(String icaId) {

		return icaComponentMarksDAO.checkWhetherGradingStartOrNot(icaId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByParam(String acadYear, String acadSession, String programId,
			String campusId) {
		return icaComponentMarksDAO.getIcaComponentMarksByParam(acadYear, acadSession, programId, campusId);

	}

	// 26-04-2021
	public List<IcaComponentMarks> getIcaComponentMarksByParamMultiSessionCoursera(String acadYear, String acadSession,
			String programId, String campusId) {
		return icaComponentMarksDAO.getIcaComponentMarksByParamMultiSessionCoursera(acadYear, acadSession, programId,
				campusId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByParamMultiSession(String acadYear, String acadSession,
			String programId, String campusId) {
		return icaComponentMarksDAO.getIcaComponentMarksByParamMultiSession(acadYear, acadSession, programId, campusId);

	}

	public List<IcaComponentMarks> getIcaComponentMarksByParamDraft(String acadYear, String acadSession,
			String programId, String campusId) {
		return icaComponentMarksDAO.getIcaComponentMarksByParamDraft(acadYear, acadSession, programId, campusId);

	}

	public List<IcaComponentMarks> getIcaComponentMarksByParamFaculty(String acadYear, String username) {
		return icaComponentMarksDAO.getIcaComponentMarksByParamFaculty(acadYear, username);

	}

	public List<IcaComponentMarks> getIcaComponentMarksByUserById(String username, String icaId) {
		return icaComponentMarksDAO.getIcaComponentMarksByUserById(username, icaId);
	}

	public List<IcaComponentMarks> icaComponentMarksByParentIcaId(Long icaId) {

		return icaComponentMarksDAO.icaComponentMarksByParentIcaId(icaId);
	}

	public List<String> getDistinctUsernamesByActiveIcaId(String icaId) {
		return icaComponentMarksDAO.getDistinctUsernamesByActiveIcaId(icaId);
	}

	public List<String> getDistinctUsernamesByActiveIcaId(String icaId, String componentId) {
		return icaComponentMarksDAO.getDistinctUsernamesByActiveIcaId(icaId, componentId);
	}
	
	public IcaComponentMarks getIcaCompMarksByUsername(String icaId, String componentId,String username) {
		return icaComponentMarksDAO.getIcaCompMarksByUsername(icaId, componentId,username);
	}

	public void updateSaveAsDraftOrFinalSubmit(String flag, String icaId, String value) {
		icaComponentMarksDAO.updateSaveAsDraftOrFinalSubmit(flag, icaId, value);
	}

	public void updateSaveAsDraftOrFinalSubmit(String flag, String icaId, String value, String componentId) {
		icaComponentMarksDAO.updateSaveAsDraftOrFinalSubmit(flag, icaId, value, componentId);
	}

	public List<String> getFinalSubmitByIcaIdAndUserList(String icaId, List<String> usernames) {
		return icaComponentMarksDAO.getFinalSubmitByIcaIdAndUserList(icaId, usernames);
	}

	public List<String> getFinalSubmitByIcaIdAndUserList(String icaId, List<String> usernames, String componentId) {
		return icaComponentMarksDAO.getFinalSubmitByIcaIdAndUserList(icaId, usernames, componentId);
	}

	public void updateFinalSubmitByUserList(List<String> usernames, String icaId) {
		icaComponentMarksDAO.updateFinalSubmitByUserList(usernames, icaId);
	}

	public void updateFinalSubmitByUserList(List<String> usernames, String icaId, String componentId) {
		icaComponentMarksDAO.updateFinalSubmitByUserList(usernames, icaId, componentId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdBatchWise(String icaId, String username) {
		return icaComponentMarksDAO.getIcaComponentMarksByIcaIdBatchWise(icaId, username);
	}

	public int deleteIcaTotalMarksByStudents(String icaId, List<String> studList) {

		return icaComponentMarksDAO.deleteIcaTotalMarksByStudents(icaId, studList);
	}

	public List<IcaComponentMarks> getIcaMarks(String icaId) {
		return icaComponentMarksDAO.getIcaMarks(icaId);
	}

	public List<IcaComponentMarks> getIcaPublishedCompMarks(String usrename) {
		return icaComponentMarksDAO.getIcaPublishedCompMarks(usrename);
	}
	
	public int updateRaiseQuery(String icaId, String username, String query,String componentId) {

		return icaComponentMarksDAO.updateRaiseQuery(icaId, username, query,componentId);
	}
	
	public List<IcaComponentMarks> getIsReevalIcaUsername(String icaId,String compId) {
		return icaComponentMarksDAO.getIsReevalIcaUsername(icaId, compId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdWithSeqNo(String tempIcaId, String seqFlag,String icaId) {
		return icaComponentMarksDAO.getIcaComponentMarksByIcaIdWithSeqNo(tempIcaId,seqFlag,icaId);
	}

	public List<IcaComponentMarks> getIcaComponentMarksByIcaIdBatchWiseforSeq(String tempIcaId, String seqFlag,	String username,String icaId) {
		return icaComponentMarksDAO.getIcaComponentMarksByIcaIdBatchWiseforSeq(tempIcaId,seqFlag,username,icaId);
	}
}
