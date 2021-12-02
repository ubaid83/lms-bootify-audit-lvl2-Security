package com.spts.lms.services.tee;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.beans.tee.TeeTotalMarks;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.tee.TeeTotalMarksDAO;
import com.spts.lms.services.BaseService;
@Service("teeTotalMarksService")
@Transactional
public class TeeTotalMarksService extends BaseService<TeeTotalMarks>{
	
	@Autowired
	TeeTotalMarksDAO teeTotalMarksDAO;
	
	@Override
	protected BaseDAO<TeeTotalMarks> getDAO() {
		// TODO Auto-generated method stub
		return teeTotalMarksDAO;
	}

//	public List<TeeTotalMarks> getTeeTotalMarksByUsername(String username) {
//		return teeTotalMarksDAO.getTeeTotalMarksByUsername(username);
//	}
//	public String checkWhetherGradingStartOrNotP(String teeId) {
//		return teeTotalMarksDAO.checkWhetherGradingStartOrNotP(teeId);
//	}
//	public String checkWhetherGradingStartOrNot(String teeId) {
//		return teeTotalMarksDAO.checkWhetherGradingStartOrNot(teeId);
//	}
//	public List<TeeTotalMarks> getAllTeeTotalMarksByTeeId(long teeId) {
//		return teeTotalMarksDAO.getAllTeeTotalMarksByTeeId(teeId);
//	}
//	public List<TeeTotalMarks> getTeeTotalMarksByTeeIdBatchwise(String teeId,String username) {
//		return teeTotalMarksDAO.getTeeTotalMarksByTeeIdBatchwise(teeId,username);
//	}
	public int updateRaiseQuery(String icaId, String username, String query) {
		return teeTotalMarksDAO.updateRaiseQuery(icaId, username, query);
	}
	public List<TeeTotalMarks> getTeeTotalMarksByParamForFaculty(String acadYear ,  String username) {
		return teeTotalMarksDAO.getTeeTotalMarksByParamForFaculty(acadYear,username);
	}
	
	
	
	//10-04-2020
	
	public List<TeeTotalMarks> getTeeTotalMarksByUsername(String username) {
		return teeTotalMarksDAO.getTeeTotalMarksByUsername(username);
	}
	public String checkWhetherGradingStartOrNotP(String teeId) {
		return teeTotalMarksDAO.checkWhetherGradingStartOrNotP(teeId);
	}
	public String checkWhetherGradingStartOrNot(String teeId) {
		return teeTotalMarksDAO.checkWhetherGradingStartOrNot(teeId);
	}
	public List<TeeTotalMarks> getAllTeeTotalMarksByTeeId(long teeId) {
		return teeTotalMarksDAO.getAllTeeTotalMarksByTeeId(teeId);
	}
	public List<TeeTotalMarks> getTeeTotalMarksByTeeIdBatchwise(String teeId,String username) {
		return teeTotalMarksDAO.getTeeTotalMarksByTeeIdBatchwise(teeId,username);
	}
	
	public List<String> getDistinctUsernamesByActiveTeeId(String teeId){
		
		return teeTotalMarksDAO.getDistinctUsernamesByActiveTeeId(teeId);
	}
	public List<TeeTotalMarks> getIsReevalTeeUsername(String teeId) {
		return teeTotalMarksDAO.getIsReevalTeeUsername(teeId);
	}
	public List<String> getDistinctSubmittedUsernamesByActiveTeeId(String teeId){
		return teeTotalMarksDAO.getDistinctSubmittedUsernamesByActiveTeeId(teeId);
	}
	
	public List<TeeTotalMarks> getTeeTotalMarksByParam(String acadYear,
			String acadSession, String programId, String campusId) {
		return teeTotalMarksDAO.getTeeTotalMarksByParam(acadYear,acadSession,programId,campusId);
	}
	
	public List<TeeTotalMarks> getRaiseQueriesForTee(String acadYear,
			Collection<GrantedAuthority> auth, String username) {
		return teeTotalMarksDAO.getRaiseQueriesForTee(acadYear,auth,username);
	}
	public int updateMarksFromSupportAdmin(TeeTotalMarks ttm) {
		return teeTotalMarksDAO.updateMarksFromSupportAdmin(ttm);
	}
	public int deleteTeeTotalMarksByStudents(String teeId, List<String> studList) {

		return teeTotalMarksDAO.deleteTeeTotalMarksByStudents(teeId, studList);
	}
	
	public int getNoOfStudentsForTee(Long teeId) {
		return teeTotalMarksDAO.getNoOfStudentsForTee(teeId);
	}
	public int getCountOfTcsFlagSentForTee(Long teeId) {
		return teeTotalMarksDAO.getCountOfTcsFlagSentForTee(teeId);
	}
	
	public List<TeeTotalMarks> getFacultyEvaluationStatus(String teeId){
		return teeTotalMarksDAO.getFacultyEvaluationStatus(teeId);
	}

	public int checkIfSavedAsDraft(Long teeId) {
		return teeTotalMarksDAO.checkIfSavedAsDraft(teeId);
	}
}
