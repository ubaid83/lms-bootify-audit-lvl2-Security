package com.spts.lms.services.ica;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.ica.IcaTotalMarksDAO;
import com.spts.lms.services.BaseService;

@Service("icaTotalMarksService")
@Transactional
public class IcaTotalMarksService extends BaseService<IcaTotalMarks> {

	@Autowired
	IcaTotalMarksDAO icaTotalMarksDAO;

	@Override
	protected BaseDAO<IcaTotalMarks> getDAO() {

		return icaTotalMarksDAO;
	}

	public List<IcaTotalMarks> icaTotalMarksByIcaId(Long icaId) {

		return icaTotalMarksDAO.icaTotalMarksByIcaId(icaId);
	}

	public List<IcaTotalMarks> getIcaTotalMarksByUser(String username) {

		return icaTotalMarksDAO.getIcaTotalMarksByUser(username);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByUserForNonEvent(String username) {
		return icaTotalMarksDAO.getIcaTotalMarksByUserForNonEvent(username);
	}

	public List<IcaTotalMarks> getIcaTotalMarksByIca(String icaId) {

		return icaTotalMarksDAO.getIcaTotalMarksByIca(icaId);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByIcaForNonEvent(String icaId) {

		return icaTotalMarksDAO.getIcaTotalMarksByIcaForNonEvent(icaId);
	}

	public int updateRaiseQuery(String icaId, String username, String query) {

		return icaTotalMarksDAO.updateRaiseQuery(icaId, username, query);
	}
	//26-04-2021
	public List<IcaTotalMarks> getIcaTotalMarksByParamCoursera(String acadYear, String acadSession, String programId,
			String campusId) {
		return icaTotalMarksDAO.getIcaTotalMarksByParamCoursera(acadYear,acadSession,programId,campusId);
	}
	public List<IcaTotalMarks> getIcaTotalMarksByParam(String acadYear,
			String acadSession, String programId, String campusId) {
		return icaTotalMarksDAO.getIcaTotalMarksByParam(acadYear, acadSession,
				programId, campusId);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByParamDraft(String acadYear,
			String acadSession, String programId, String campusId) {
		return icaTotalMarksDAO.getIcaTotalMarksByParamDraft(acadYear, acadSession,
				programId, campusId);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByParamForNonEvent(String acadYear,
			String acadSession, String programId, String campusId) {
		return icaTotalMarksDAO.getIcaTotalMarksByParamForNonEvent(acadYear, acadSession, programId, campusId);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByParamForNonEventDraft(String acadYear,
			String acadSession, String programId, String campusId) {
		return icaTotalMarksDAO.getIcaTotalMarksByParamForNonEventDraft(acadYear, acadSession, programId, campusId);
	}

	public List<IcaTotalMarks> getRaiseQueries(String acadYear,
			Collection<GrantedAuthority> auth, String username) {

		return icaTotalMarksDAO.getRaiseQueries(acadYear, auth, username);
	}
	public List<IcaTotalMarks> getIcaTotalMarksByParamForFaculty(String acadYear ,  String username) {
		return icaTotalMarksDAO.getIcaTotalMarksByParamForFaculty(acadYear,username);
	}
	
	public List<IcaTotalMarks> getIsReevalIcaUsername(String icaId) {
		return icaTotalMarksDAO.getIsReevalIcaUsername(icaId);
	}
	
	
	
	public List<String> getDistinctUsernamesByActiveIcaId(String icaId){
		return icaTotalMarksDAO.getDistinctUsernamesByActiveIcaId(icaId);
	}
	
	public void updateSaveAsDraftOrFinalSubmit(String flag, String icaId, String value) {
		icaTotalMarksDAO.updateSaveAsDraftOrFinalSubmit(flag, icaId, value);
	}
	
	public List<String> getFinalSubmitByIcaIdAndUserList(String icaId, List<String> usernames) {
        return icaTotalMarksDAO.getFinalSubmitByIcaIdAndUserList(icaId, usernames);
	}
	
	public void updateFinalSubmitByUserList(List<String> usernames, String icaId) {
	        icaTotalMarksDAO.updateFinalSubmitByUserList(usernames, icaId);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByIcaBatchWise(String icaId,String username) {
        return icaTotalMarksDAO.getIcaTotalMarksByIcaBatchWise(icaId, username);
	}
	
	public List<IcaTotalMarks> getIcaTotalMarksByUserCoursera(String username) {

		return icaTotalMarksDAO.getIcaTotalMarksByUserCoursera(username);
	}

	public List<IcaTotalMarks> getNonEventRaiseQueries(String acadYear,
			Collection<GrantedAuthority> auth, String username) {
		return icaTotalMarksDAO.getNonEventRaiseQueries(acadYear, auth, username);
	}

	public int deleteIcaTotalMarksByStudents(String icaId, List<String> studList) {

		return icaTotalMarksDAO.deleteIcaTotalMarksByStudents(icaId, studList);
	}
	
	public int getNoOfStudentsForIca(Long icaId) {
		return icaTotalMarksDAO.getNoOfStudentsForIca(icaId);
	}
	public int getCountOfTcsFlagSentForIca(Long icaId) {
		return icaTotalMarksDAO.getCountOfTcsFlagSentForIca(icaId);
	}
	
	public List<IcaTotalMarks> getCompQueries(String acadYear,
			Collection<GrantedAuthority> auth, String username){
		return icaTotalMarksDAO.getComponentQueries(acadYear, auth, username);
	}
	
	public List<IcaTotalMarks> getCompQueriesNE(String acadYear,
			Collection<GrantedAuthority> auth, String username){
		return icaTotalMarksDAO.getComponentQueriesNE(acadYear, auth, username);
	}
}
