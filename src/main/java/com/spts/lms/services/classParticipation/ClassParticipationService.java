package com.spts.lms.services.classParticipation;

import java.util.List;

import org.eclipse.jdt.internal.compiler.batch.FileSystem.Classpath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.faq.Faq;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.classParticipation.ClassParticipationDAO;
import com.spts.lms.daos.faq.FaqDAO;
import com.spts.lms.services.BaseService;

@Service("classParticipationService")
public class ClassParticipationService extends BaseService<ClassParticipation> {
	@Autowired
	ClassParticipationDAO classParticipationDAO;

	@Override
	protected BaseDAO<ClassParticipation> getDAO() {
		// TODO Auto-generated method stub
		return classParticipationDAO;
	}

	public List<ClassParticipation> findStudentsForFaculty(Long courseId) {
		return classParticipationDAO.findStudentsForFaculty(courseId);
	}

	public List<String> findAllStudentUsernames(Long courseId) {
		return classParticipationDAO.findAllStudentUsernames(courseId);
	}

	public List<ClassParticipation> findByStudent(String username) {
		return classParticipationDAO.findByStudent(username);
	}

	public List<ClassParticipation> findByStudent(String username,
			String courseId) {
		return classParticipationDAO.findByStudent(username, courseId);
	}
	
	public List<ClassParticipation> getCPForGradeCenter(String username,
			String courseId) {
		return classParticipationDAO.getCPForGradeCenter(username, courseId);
	}

}
