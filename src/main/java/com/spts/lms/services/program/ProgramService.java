package com.spts.lms.services.program;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.program.ProgramDAO;
import com.spts.lms.helpers.ApplicationMailer;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.BaseService;

@Service("programService")
@Transactional
public class ProgramService extends BaseService<Program> {
	@Autowired
	private ApplicationMailer mailService;

	@Autowired
	private ProgramDAO programDAO;

	@Override
	public BaseDAO<Program> getDAO() {
		return programDAO;
	}

	public Page<Program> searchProgramWithSession(int pageNo, int pageSize) {
		return programDAO.getAllProgramsWithSession(pageNo, pageSize);

	}

	public int deleteSessionsNotIn(Program program) {
		return programDAO.deleteSessionsNotIn(program);

	}

	public List<Program> findAllProgramsWithAcadSession(List<String> acadSession) {
		return programDAO.findAllProgramsWithAcadSession(acadSession);
	}

	public List<String> findProgramIdByProgramName(String programName) {
		return programDAO.findProgramIdByProgramName(programName);
	}

	public List<Program> getProgramByName(String progName) {
		return programDAO.getProgramByName(progName);
	}

	public List<Program> findAppsByUsername(String username) {
		return programDAO.findAppsByUsername(username);
	}

	public List<Program> findAllProgramsWithAcadSessionForFaculty(List<String> acadSession, String facultyId) {
		return programDAO.findAllProgramsWithAcadSessionForFaculty(acadSession, facultyId);
	}

	public List<String> findAllProgramsForFaculty(String facultyId) {
		return programDAO.findAllProgramsForFaculty(facultyId);
	}

	public List<Program> findAllProgramsByFacultyId(String acadYear, String facultyId, List<String> acadSessions) {
		return programDAO.findAllProgramsByFacultyId(acadYear, facultyId, acadSessions);
	}

	public List<Program> getProgramListByIds(String programIds) {
		return programDAO.getProgramListByIds(programIds);
	}

	public String getProgramNamesForIca(String programIds) {
		return programDAO.getProgramNamesForIca(programIds);
	}

	public String getCollegeName(String programId, String campusId) {
		return programDAO.getCollegeName(programId, campusId);
	}

	public List<Program> findAllProgramsWithAcadSessionYearModule(String acadSession, String acadYear,
			String moduleId) {

		return programDAO.findAllProgramsWithAcadSessionYearModule(acadSession, acadYear, moduleId);
	}

	public String getCollgeName(String programId, String campusId) {

		return programDAO.getCollegeName(programId, campusId);
	}

	public List<Program> findAllProgramsWithAcadSessionYearModuleForModule(String acadSession, String acadYear,
			String moduleId) {
		return programDAO.findAllProgramsWithAcadSessionYearModuleForModule(acadSession, acadYear, moduleId);
	}

	public List<Program> findPrograms() {

		return programDAO.findPrograms();
	}

	public List<Program> findAllProgramsWithAcadSessionYearModuleCE(String acadYear, String moduleId) {

		return programDAO.findAllProgramsWithAcadSessionYearModuleCE(acadYear, moduleId);
	}

	public List<Program> findAllProgramForFeedbackSupportAdmin() {
// TODO Auto-generated method stub
		return programDAO.findAllProgramForFeedbackSupportAdmin();
	}

	public String getProgramName(String programId) {
		return programDAO.getProgramName(programId);
	}

	// amey 14-10-2020
	public List<String> findAllProgramsForFacultyByType(String facultyId, String feedbackType) {
		return programDAO.findAllProgramsForFacultyByType(facultyId, feedbackType);
	}

	public List<Program> getPrograms() {

		return programDAO.getPrograms();
	}
}
