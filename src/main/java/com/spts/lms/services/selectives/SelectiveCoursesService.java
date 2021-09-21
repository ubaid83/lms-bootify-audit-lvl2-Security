package com.spts.lms.services.selectives;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.selectives.SelectiveCourses;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.selectives.SelectiveCoursesDAO;
import com.spts.lms.services.BaseService;

@Service("selectiveCoursesService")
@Transactional
public class SelectiveCoursesService extends BaseService<SelectiveCourses> {

	@Autowired
	SelectiveCoursesDAO selectiveCoursesDAO;
	
	@Override
	protected BaseDAO<SelectiveCourses> getDAO() {
		
		return selectiveCoursesDAO;
	}
	
	public List<SelectiveCourses> getCoursesByEventId(String eventId) {
		
		return selectiveCoursesDAO.getCoursesByEventId(eventId);
	}
	
	
}
