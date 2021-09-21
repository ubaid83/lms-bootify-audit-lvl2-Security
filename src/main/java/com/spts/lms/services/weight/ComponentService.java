package com.spts.lms.services.weight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.weight.Component;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.weight.ComponentDAO;
import com.spts.lms.services.BaseService;

@Service("componentService")
public class ComponentService extends BaseService<Component> {
	@Autowired
	ComponentDAO componentDAO;

	@Override
	protected BaseDAO<Component> getDAO() {
		// TODO Auto-generated method stub
		return componentDAO;
	}

	public List<Component> findStudentsForFaculty(Long courseId) {
		return componentDAO.findStudentsForFaculty(courseId);
	}

	public List<String> findAllStudentUsernames(Long courseId) {
		return componentDAO.findAllStudentUsernames(courseId);
	}

	public List<Component> findByStudent(String username) {
		return componentDAO.findByStudent(username);
	}

	public Component findByCourseAndStudentAndComp(String courseId,
			String username, String compName) {
		return componentDAO.findByCourseAndStudentAndComp(courseId, username,
				compName);
	}

	public List<Component> findStudentsForCompnent(Long courseId,
			String compName) {
		return componentDAO.findStudentsForCompnent(courseId, compName);
	}

	public List<String> findCompNamesByCourseId(String courseId) {
		return componentDAO.findCompNamesByCourseId(courseId);
	}

	public void deleteComponent(String courseId, String compName) {
		componentDAO.deleteComponent(courseId, compName);
	}

	public List<Component> findByStudent(String username, String courseId) {
		return componentDAO.findByStudent(username, courseId);
	}

}
