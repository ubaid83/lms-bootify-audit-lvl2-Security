package com.spts.lms.services.course;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.course.CourseDAO;
import com.spts.lms.services.BaseService;

@Service("courseService")
@Transactional
public class CourseService extends BaseService<Course> {
	@Autowired
	private CourseDAO courseDAO;

	@Override
	public BaseDAO<Course> getDAO() {
		return courseDAO;
	}

	public List<Course> findByCoursesBasedOnProgramName(String progName) {
		return courseDAO.findByCoursesBasedOnProgramName(progName);
	}

	public List<Course> findByCoursesBasedOnProgramNameAndYear(String progName,
			Integer acadYear) {
		return courseDAO.findByCoursesBasedOnProgramNameAndYear(progName,
				acadYear);
	}

	public List<Course> findByProgramIdSessionId(Integer programId,
			Integer sessionId) {
		return courseDAO.findByProgramIdSessionId(programId, sessionId);
	}

	public List<Course> findByCourseName(String courseName) {
		return courseDAO.findByCourseName(courseName);

	}

	public List<Course> findByUser(String username, String acadMonth,
			String acadYear) {
		return courseDAO.findByUser(username, acadMonth, acadYear);
	}

	public List<String> getAllAcadSession() {
		return courseDAO.getAllAcadSession();
	}

	public List<Course> findByProgramIdAcadSession(String programId,
			List<String> acadSession) {
		return courseDAO.findByProgramIdAcadSession(programId, acadSession);

	}

	public List<String> findDistinctDepartment(List<String> acadSession) {
		return courseDAO.findDistinctDepartment(acadSession);

	}

	public List<String> getAcadSessionForActiveFeedback() {
		return courseDAO.getAcadSessionForActiveFeedback();
	}

	public List<Course> findByUser(String username, Long programId) {
		return courseDAO.findByUser(username, programId);
	}

	public List<Course> findByUser(String username) {
		return courseDAO.findByUser(username);
	}

	public List<Course> findByAcadSession(List<String> acadSession) {
		return courseDAO.findByAcadSession(acadSession);

	}

	public List<Course> findByUserActiveInterdesciplinary(String username) {
		return courseDAO.findByUserActiveInterdesciplinary(username);
	}

	/*
	 * public List<Course> findByUserActive(String username,Long programId) {
	 * return courseDAO.findByUserActive(username,programId); }
	 */
	// added on 21/11/2017
	public List<Course> findByUserActive(String username, String programName) {
		return courseDAO.findByUserActive(username, programName);
	}

	// ends
	public List<Course> findByUserActive(String username) {
		return courseDAO.findByUserActive(username);
	}

	public List<String> findFacultiesByProgram(String programId) {
		return courseDAO.getFacultiesByProgram(programId);
	}

	public void makeInActive(Long id) {
		courseDAO.makeInActive(id);
	}

	public Course findByIDAndFaculty(Long id, String facultyId) {
		return courseDAO.findByIDAndFaculty(id, facultyId);
	}

	public String getTerms(String courseId) {
		return courseDAO.findTerms(courseId);
	}

	public List<String> getAllAcadYear() {
		return courseDAO.getAllAcadYear();
	}

	public List<String> getAllAcadMonth() {
		return courseDAO.getAllAcadMonth();
	}

	public HashMap<Long, String> getCourseIdNameMap() {
		List<Course> courses = courseDAO.findAllActive();
		HashMap<Long, String> courseIdNameMap = new HashMap<Long, String>();
		for (Course course : courses) {
			courseIdNameMap.put(course.getId(), course.getCourseName());
		}

		return courseIdNameMap;
	}

	public List<Course> findNotAssignedCourseProgramwise(String ProgramId) {
		return courseDAO.findNotAssignedCourseProgramwise(ProgramId);
	}

	public List<Course> findByProgramIdAcadSessionAcadYear(Long programId,
			String acadYear, String acadSession) {
		return courseDAO.findByProgramIdAcadSessionAcadYear(programId,
				acadYear, acadSession);
	}

	public List<Course> findByAcadSessionAndYear(List<String> acadSession,
			String acadYear) {
		return courseDAO.findByAcadSessionAndYear(acadSession, acadYear);

	}

	public List<Course> findByAcadSessionAndYearAndCampusList(
			List<String> acadSession, String acadYear, String campusId) {
		return courseDAO.findByAcadSessionAndYearAndCampus(acadSession,
				acadYear, campusId);

	}

	public List<String> getAcadSessionForActiveFeedbackForFaculty(
			String facultyId) {
		return courseDAO.getAcadSessionForActiveFeedbackForFaculty(facultyId);
	}

	public List<Course> getUserCourseByUsername(String username) {
		return courseDAO.getUserCourseByUsername(username);
	}

	public void deleteSoftById(String courseId) {
		courseDAO.deleteSoftById(courseId);
	}

	public List<Course> getModulesByFaculty(String facultyId,String acadYear,List<String> acadSessions) {

		return courseDAO.getModulesByFaculty(facultyId,acadYear,acadSessions);
	}
	
	public List<Course> findCoursesByUserForApp(String username) {
		return courseDAO.findCoursesByUserForApp(username);
	}
	
	public List<Course> findCoursesByUserAndProgramIdForApp(String username, long programId) {
		return courseDAO.findCoursesByUserAndProgramIdForApp(username, programId);
	}
	
	public List<Course> findStudentsByCourseIdForApp(long courseId) {
		return courseDAO.findStudentsByCourseIdForApp(courseId);
	}
	
	public List<Course> findProgramsByUserForApp(String username) {
		return courseDAO.findProgramsByUserForApp(username);
	}
	
	public List<Course> findByAcadSession(List<String> acadSession,String acadYear1,String acadYear2,String campusId) {
		return courseDAO.findByAcadSession(acadSession,acadYear1,acadYear2,campusId);
	}
	
	public String getModuleName(String moduleId){
		return courseDAO.getModuleName(moduleId);
	}
	public String getModuleAbbr(String moduleId){
		return courseDAO.getModuleAbbr(moduleId);
	}
	
	public List<Course> moduleListByAcadYearAndCampus(String acadSession,
			String acadYear, String campusId) {
		return courseDAO.moduleListByAcadYearAndCampus(acadSession,acadYear,campusId);
	}
	
	public List<Course> acadSessionListByAcadYearAndCampus(
			String acadYear, String campusId) {
		return courseDAO.acadSessionListByAcadYearAndCampus(acadYear, campusId);
	}
	
	public List<Course> acadSessionListByAcadYearAndCampusForIca(String acadYear,
			String campusId) {
		return courseDAO.acadSessionListByAcadYearAndCampusForIca(acadYear, campusId);
	}
	public List<Course> acadSessionListByAcadYearAndCampusForTee(String acadYear,
			String campusId) {
		return courseDAO.acadSessionListByAcadYearAndCampusForTee(acadYear, campusId);
	}
	public List<Course> acadSessionListByAcadYearAndCampusForModule(
			String acadYear, String campusId) {
		return courseDAO.acadSessionListByAcadYearAndCampusForModule(acadYear, campusId);
	}
	
	public List<Course> moduleListByAcadYearAndCampusForModule(String acadSession,
			String acadYear, String campusId) {
		return courseDAO.moduleListByAcadYearAndCampusForModule(acadSession, acadYear, campusId);
	}
	
	public String getModuleNameForNonEvent(String moduleId){
		return courseDAO.getModuleNameForNonEvent(moduleId);
	}
	
	public List<Course> findStudentsByCourseIdForAndroidApp(long courseId) 
	{
		return courseDAO.findStudentsByCourseIdForAndroidApp(courseId);
	}
	public List<Course> findStudentsByMultipleCourseId(List<Long> courseIds){
		return courseDAO.findStudentsByMultipleCourseId(courseIds);
	}
	public List<Course> findStudentCountCourseWise(List<Long> courseIds){
		return courseDAO.findStudentCountCourseWise(courseIds);
	}
	public Course findByModuleIdAndAcadYear(String moduleId,String acadYear){
		return courseDAO.findByModuleIdAndAcadYear(moduleId, acadYear);
	}
	public Course findByModuleIdAndAcadYearCode(String moduleId,String acadYear){
		return courseDAO.findByModuleIdAndAcadYearCode(moduleId, acadYear);
	}
	
	public List<Course> findModulesByUsername(String username,Long programId) {
			return courseDAO.findModulesByUsername(username,programId);
		}
		
	public List<Course> findCoursesByModuleId(Long moduleId, String username, Integer acadYear) {
			return courseDAO.findCoursesByModuleId(moduleId, username,acadYear);
		}

public List<Course> findCoursesByModuleIdAndCampusId(Long moduleId, String username, Long campusId) {
		return courseDAO.findCoursesByModuleIdAndCampusId(moduleId, username, campusId);
	}
	
	public List<Course> getAcadYearByModuleId(String moduleId,String username) {
		return courseDAO.getAcadYearByModuleId(moduleId,username);
	}
	
	public List<Course> findActiveModulesByUsername(String username, long programId) {
		return courseDAO.findActiveModulesByUsername(username, programId);
	}
	

	public List<Course> findCoursesByModuleId(Long moduleId, String username, String acadYear) {
		return courseDAO.findCoursesByModuleId(moduleId, username, acadYear);
	}
	//forSearch
	public List<Course> findCoursesByModuleId(Long moduleId, String username, String acadYear,String programId) {
		return courseDAO.findCoursesByModuleId(moduleId, username, acadYear,programId);
	}
	public List<Course> findModulesByUsername(String username,String acadYear, Long programId) {
		return courseDAO.findModulesByUsername(username,acadYear,programId);
	}
	

	public String getAcadMonthByModuleIdAndAcadYear(String moduleId, String acadYear)
	{
		return courseDAO.getAcadMonthByModuleIdAndAcadYear(moduleId,acadYear);
	}

	public List<Course> findCoursesByUsernameAndProgramId(String username, Long programId){
		return courseDAO.findCoursesByUsernameAndProgramId(username,programId);
	}
	public List<Course> findCoursesByAcadYear(String username, Long programId, String acadYear){
        return courseDAO.findCoursesByAcadYear(username, programId, acadYear);
	}
	
	public List<Course> findcourseDetailsByCourseIds(Set<String> courseIds) {
        return courseDAO.findcourseDetailsByCourseIds(courseIds);
  }

	 public List<Course> findCourseStatiticsByUsernameForApp(String username) {
			return courseDAO.findCourseStatiticsByUsernameForApp(username);
		}

	public List<String> findAcadYearCode() {
		
		return courseDAO.findAcadYearCode();
	}
	
	public List<String> findAcadYearCodeForNS() {
		
		return courseDAO.findAcadYearCodeForNS();
	}

    public List<Course> findCoursesByProgramIdAndAcadYear(Integer acadYear,Long programId){
        return courseDAO.findCoursesByProgramIdAndAcadYear(acadYear,programId);
    }

	
	public List<Course> findCoursesByProgramIdAndAcadYear(String acadYear,String programId, String campusId){
        return courseDAO.findCoursesByProgramIdAndAcadYear(acadYear,programId, campusId);
	}

	public List<Course> findAcadSessionForStudentMs() {
		
		return courseDAO.findAcadSessionForStudentMs();
	}


    public List<Course> findCoursesByProgramIdAndAcadYear(Integer acadYear,Long programId, Long campusId){
        return courseDAO.findCoursesByProgramIdAndAcadYear(acadYear,programId,campusId);
	}
    
    public List<Course> findByAdminActive(String programName) {
    	return courseDAO.findByAdminActive(programName);
    }
    
    public List<Course> findByAdminActive() {
    	return courseDAO.findByAdminActive();
    }
    
    public List<Course> findCoursesByProgramId(Long programId) {
    	return courseDAO.findCoursesByProgramId(programId);
    }
    
    public List<Course> moduleListByAcadYearAndCampusCE(
			String acadYear, String campusId) {
		return courseDAO.moduleListByAcadYearAndCampusCE(acadYear,campusId);
	}

	public List<Course> acadSessionListByAcadYearAndCampusCE(
			String acadYear, String campusId,String moduleId) {
		return courseDAO.acadSessionListByAcadYearAndCampusCE(acadYear, campusId,moduleId);
	}
	
	//29-05-2020
	public List<Course> findByCoursesBasedOnProgramNameAndYearAndcreatedBy(String progName,
			Integer acadYear,String createdBy) {
		return courseDAO.findByCoursesBasedOnProgramNameAndYearAndcreatedBy(progName,acadYear,createdBy);
	}

	public List<Course> findByCoursesBasedOnProgramNameSupportAdmin() {
		return courseDAO.findByCoursesBasedOnProgramNameSupportAdmin();
	}
	
	public List<String> courseListByParams(String moduleId,String acadYear,String programId) {
		return courseDAO.courseListByParams(moduleId, acadYear, programId);
	}
	
	public List<String> courseListByParams(String moduleId,String acadYear,String programId,String username) {
		return courseDAO.courseListByParams(moduleId, acadYear, programId,username);
	}
	
	public List<Course> moduleListByAcadYearAndCampusForTest(
			String acadYear, String campusId,String programId) {
		return courseDAO.moduleListByAcadYearAndCampusForTest(acadYear,campusId,programId);
	}
	
	public List<Course> findCoursesByModuleIdAcadYearAndProgram(Long moduleId, String acadYear,String programId) {
		return courseDAO.findCoursesByModuleIdAcadYearAndProgram(moduleId, acadYear, programId);
	}
public String getAcadMonthByModuleIdAndAcadYearAndProgram(String moduleId, String acadYear,String programId)
	{
		return courseDAO.getAcadMonthByModuleIdAndAcadYearAndProgram(moduleId,acadYear,programId);
	}

	public List<Course> getModulesForTest(String username,String programId){
		
		return courseDAO.getModulesForTest(username, programId);
	}
	 
	public List<Course> getCourseByProgramId(String programIds) {
		return courseDAO.getCourseByProgramId(programIds);
	}
	
	public List<Course> findByCoursesBasedOnYearAndcreatedBy(
			Integer acadYear,String createdBy) {
		return courseDAO.findByCoursesBasedOnYearAndcreatedBy(acadYear,createdBy);
	}
	public List<Course> findCoursesForAttd(String username) {
		return courseDAO.findCoursesForAttd(username);
	}

	//Peter 22/10/2021
	public Course findAcadYear(String acadYear) {
		return courseDAO.findAcadYear(acadYear);
	}

	//Peter 22/10/2021
	public Course checkIfExistsInDB(String columnName, String value) {
		return courseDAO.checkIfExistsInDB(columnName,value);
	}

	//Peter 20/10/2021
	public Course checkIfAcadSessionExists(String acadSession) {
		return courseDAO.checkIfAcadSessionExists(acadSession);
	}

	//Peter 20/10/2021
	public Course checkIfModuleExists(String moduleId) {
		return courseDAO.checkIfModuleExists(moduleId);
	}

	//Peter 20/10/2021
	public Course checkIfCampusExists(String campusId) {
		return courseDAO.checkIfCampusExists(campusId);
	}
}
