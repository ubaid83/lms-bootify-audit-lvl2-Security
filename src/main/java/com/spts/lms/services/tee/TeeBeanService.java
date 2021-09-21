package com.spts.lms.services.tee;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.tee.TeeBean;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.tee.TeeBeanDAO;
import com.spts.lms.services.BaseService;

@Service("teeBeanService")
@Transactional
public class TeeBeanService extends BaseService<TeeBean>{

	@Autowired
	TeeBeanDAO teeBeanDAO;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	protected BaseDAO<TeeBean> getDAO() {
		// TODO Auto-generated method stub
		return teeBeanDAO;
	}

	public List<TeeBean> checkAlreadyExistICAList(String moduleId,
			String acadYear, String campusId, String acadSession) {

		return teeBeanDAO.checkAlreadyExistICAList(moduleId, acadYear,
				campusId, acadSession);
	}
	
	public List<TeeBean> getAllTeeByUsername(String username){
		return teeBeanDAO.getAllTeeByUsername(username);
	}
	
	public List<TeeBean> getAllSubmittedTee(String username) {
		return teeBeanDAO.getAllSubmittedTee(username);
	}
	
	public int updateTeeToPublished(String teeId, Date lastModifiedDate,
			String publishedDate) {
		return teeBeanDAO.updateTeeToPublished(teeId,lastModifiedDate,publishedDate);
	}
	
	public int updateTeeToSubmitted(String teeId, Date lastModifiedDate) {
		return teeBeanDAO.updateTeeToSubmitted(teeId, lastModifiedDate);
	}
	
	public List<TeeBean> findDivisionWiseTeeListByParentTee(String parentTeeId) {
		return teeBeanDAO.findDivisionWiseTeeListByParentTee(parentTeeId);
	}
	public List<TeeBean> findTeeListByProgramAndUsername(String programId, String role,String username) {
		return teeBeanDAO.findTeeListByProgramAndUsername(programId,role,username);
	}
	public List<String> getTeeStatusesForDivisionTee(Long teeId){
        return teeBeanDAO.getTeeStatusesForDivisionTee(teeId);
	}
	public List<TeeBean> getTeeQueries(String username) {
		return teeBeanDAO.getTeeQueries(username);
	}
	
	public List<TeeBean> getTeeQueriesForApproveAll(String username) {
		return teeBeanDAO.getTeeQueriesForApproveAll(username);
	}
	
	public List<TeeBean> checkAlreadyExistTEEAList(String moduleId, String acadYear, String campusId,
			String acadSession) {
		return teeBeanDAO.checkAlreadyExistTEEAList(moduleId, acadYear, campusId, acadSession);
	}
	
	public List<String> getSubmittedTeeIds(String username) {
		return teeBeanDAO.getSubmittedTeeIds(username);
	}
	
	public int updateMultipleTeeToPublished(List<String> teeId,
			Date lastModifiedDate, String publishedDate) {
		return teeBeanDAO.updateMultipleTeeToPublished(teeId, lastModifiedDate,
				publishedDate);
	}
	public List<TeeBean> getTeeIdsByParentTeeIds(Long teeId) {
		return teeBeanDAO.getTeeIdsByParentTeeIds(teeId);
	}

	public List<TeeBean> getSubmittedTeeIdsByParentTeeIds(Long teeId) {
		return teeBeanDAO.getSubmittedTeeIdsByParentTeeIds(teeId);
	}
	public List<TeeBean> teeListByParent(String parentTeeId) {

		return teeBeanDAO.teeListByParent(parentTeeId);
	}
	
	public String getFacultyNameByUsername(String assignedFaculty) {
		return teeBeanDAO.getFacultyNameByUsername(assignedFaculty);
	}
	
	
	//Tee Suport Admin
	
public List<TeeBean> findTeeListByProgramForSupportAdmin() {
		
		return teeBeanDAO.findTeeListByProgramForSupportAdmin();
	}

	public List<TeeBean> findDivisionWiseTeeListByParentTeeForSupportAdmin(String teeId) {
		
		return  teeBeanDAO.findDivisionWiseTeeListByParentTeeForSupportAdmin(teeId);
	}

	public void updateTeeDate(String endDate, String publishedDate, Long teeid) {
		
		teeBeanDAO.updateTeeDate(endDate,publishedDate,teeid);
	}

	public void updateTeeDateForDivision(String endDate, String publishedDate, List<Long> teeIds) {
		
		teeBeanDAO.updateTeeDateForDivision(endDate,publishedDate,teeIds);
	}

	public void updateTeeDateWithoutSubmit(String endDate, String publishedDate, Long teeId) {
	
		teeBeanDAO.updateTeeDateWithoutSubmit(endDate,publishedDate,teeId);
	}

	public void updateTeeDateForDivisionWithoutSubmit(String endDate, String publishedDate, List<Long> teeIds) {
		teeBeanDAO.updateTeeDateForDivisionWithoutSubmit(endDate,publishedDate,teeIds);
		
	}
	
	
	public int insertInTeeAssignments(TeeBean i) {
		return teeBeanDAO.insertInTeeAssignments(i);
	}

	public int updateInTeeAssignments(TeeBean i) {

		return teeBeanDAO.updateInTeeAssignments(i);

	}

	public int clearTeeAssignments(TeeBean i) {

		return teeBeanDAO.clearTeeAssignments(i);

	}
	
	public TeeBean getTeeAssignments(Long teeId,Long courseId){
		return teeBeanDAO.getTeeAssignments(teeId, courseId);
	}
	public TeeBean getAllTeeAssignments(Long teeId,Long courseId){
		return teeBeanDAO.getAllTeeAssignments(teeId, courseId);
	}
	public int getTeeAssignmentNC(Long teeId){
		return teeBeanDAO.getTeeAssignmentNC(teeId);
	}
	
	public int getTeeAssignmentCompleted(Long teeId){
		return teeBeanDAO.getTeeAssignmentCompleted(teeId);
	}

	public List<TeeBean> teeSubmittedListByParent(String parentId) {
		return teeBeanDAO.teeSubmittedListByParent(parentId);
	}
	
}
