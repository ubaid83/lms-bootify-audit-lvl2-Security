package com.spts.lms.services.selectives;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spts.lms.beans.selectives.SelectiveStudents;
import com.spts.lms.beans.selectives.SelectiveUserCourse;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.selectives.SelectiveStudentsDAO;
import com.spts.lms.daos.selectives.SelectiveUserCourseDAO;
import com.spts.lms.services.BaseService;

@Service("selectiveUserCourseService")
public class SelectiveUserCourseService extends BaseService<SelectiveUserCourse> {

	@Autowired
	SelectiveUserCourseDAO selectiveUserCourseDAO;

	@Override
	protected BaseDAO<SelectiveUserCourse> getDAO() {
		// TODO Auto-generated method stub
		return selectiveUserCourseDAO;
	}

	public List<SelectiveUserCourse> getStudentsList(String eventId, String username) {

		return selectiveUserCourseDAO.getStudentsList(eventId, username);

	}
}
