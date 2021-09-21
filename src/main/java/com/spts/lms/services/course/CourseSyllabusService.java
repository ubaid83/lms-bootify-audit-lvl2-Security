package com.spts.lms.services.course;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.beans.course.CourseSyllabus;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.course.CourseDAO;
import com.spts.lms.daos.course.CourseOverviewDAO;
import com.spts.lms.daos.course.CourseSyllabusDAO;
import com.spts.lms.services.BaseService;

@Service("courseSyllabusService")
@Transactional
	
public class CourseSyllabusService extends BaseService<CourseSyllabus> {
	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private CourseOverviewDAO courseOverviewDAO;
	
	@Autowired
	private CourseSyllabusDAO courseSyllabusDAO;
	
	@Override
	public BaseDAO<CourseSyllabus> getDAO() {
		return courseSyllabusDAO;
	}


	

}
