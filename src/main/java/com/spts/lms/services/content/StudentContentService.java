package com.spts.lms.services.content;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.beans.user.Role;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.content.StudentContentDAO;
import com.spts.lms.services.BaseService;

@Service("studentContentService")
@Transactional
public class StudentContentService extends BaseService<StudentContent> {

	@Autowired
	private StudentContentDAO studentContentDAO;

	@Override
	public BaseDAO<StudentContent> getDAO() {
		return studentContentDAO;
	}

	public List<StudentContent> getStudentsForContent(Long contentId,
			Long courseId, String acadMonth, Integer acadYear) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sc.contentId, sc.id "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId = ? "
				+ " where"
				+ " uc.role = ? and uc.courseId = ? and uc.acadMonth = ? and uc.acadYear = ? order by sc.id asc ";

		return findAllSQL(sql,
				new Object[] { contentId, Role.ROLE_STUDENT.name(), courseId,
						acadMonth, acadYear });
	}

	public int getNoOfStudentsAllocated(Long id) {
		return studentContentDAO.getNoOfStudentsAllocated(id);
	}

	public List<StudentContent> getStudentsForContent(Long contentId,
			Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sc.contentId, sc.id, u.campusId, u.campusName "
				+ " from user_course uc"
				+ "		inner join users u on uc.username = u.username"
				+ "		inner join program p on u.programId = p.id"
				+ "		left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId = ? "
				+ " where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and"
				+ " uc.role = ? and uc.courseId = ?  order by sc.lastModifiedDate desc ";

		return findAllSQL(sql,
				new Object[] { contentId, Role.ROLE_STUDENT.name(), courseId });
	}

	public List<StudentContent> getStudentsForContentAndCampusId(
			Long contentId, Long courseId, Long campusId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname, sc.contentId, sc.id, u.campusName "
				+ " from user_course uc"
				+ "         inner join users u on uc.username = u.username"
				+ "         inner join program p on u.programId = p.id"
				+ "         left outer join student_content sc on sc.username = u.username and sc.courseId = uc.courseId and sc.contentId = ? "
				+ " where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and"
				+ " uc.role = ? and uc.courseId = ? and u.campusId= ? order by sc.lastModifiedDate desc ";

		return findAllSQL(sql,
				new Object[] { contentId, Role.ROLE_STUDENT.name(), courseId,
						campusId });
	}

	public void keepCount(Long id, String username) {
		studentContentDAO.keepCount(id, username);
	}

	public void deleteByContentId(Long contentId) {
		studentContentDAO.deleteByContentId(contentId);
	}

	public List<StudentContent> getStudentsForContentCoursewise(Long courseId) {
		String sql = "select uc.username, p.programName, u.firstname, u.lastname "
				+ " from user_course uc "
				+ " inner join users u on uc.username = u.username "
				+ " inner join program p on u.programId = p.id  where u.active = 'Y' and u.enabled = 1 and uc.active = 'Y' and "
				+ " uc.role = ? and uc.courseId = ? ";
		return findAllSQL(sql, new Object[] { Role.ROLE_STUDENT.name(),
				courseId });
	}

	public List<StudentContent> getStudentUsername(Long contentId, Long courseId) {
		return studentContentDAO.getStudentUsername(contentId, courseId);

	}

	public List<StudentContent> findContentByStudent(String username) {

		return studentContentDAO.findContentByStudent(username);

	}

	public StudentContent findStudentContent(String userName, String contentId) {

		return studentContentDAO.findStudentContent(userName, contentId);
	}

	public List<StudentContent> getStudentContentStats(String campusId, String fromDate,String toDate) {

		return studentContentDAO.getStudentContentStats(campusId, fromDate,toDate);

	}
	
	public List<StudentContent> getStudentsContentForModule(List<Long> contentId,String moduleId, Integer acadYear,String facultyId) {
	return studentContentDAO.getStudentsContentForModule(contentId,moduleId,acadYear,facultyId);
}
	
	public List<StudentContent> getStudentsForContentAndCampusIdbyModuleId(List<Long> contentId,String moduleId, Long campusId,Integer acadYear) {
	return studentContentDAO.getStudentsForContentAndCampusIdbyModuleId(contentId,moduleId,campusId,acadYear);
}
	public StudentContent getNoOfStudentsAllocatedForModule(List<Long> id) {
		return studentContentDAO.getNoOfStudentsAllocatedForModule(id);
	}
	
	public void setActiveByContentId(Long contentId){
		studentContentDAO.setActiveByContentId(contentId);
	}
	
	public void setActiveByContentIdForModule(Long contentId){
		studentContentDAO.setActiveByContentId(contentId);
	}
	public StudentContent getStudentViewCount(List<Long> parentModuleId){
		return studentContentDAO.getStudentViewCount(parentModuleId);
	}
	
	
}
