package com.spts.lms.services.course;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.course.CourseDAO;
import com.spts.lms.daos.course.CourseOverviewDAO;
import com.spts.lms.services.BaseService;

@Service("courseOverviewService")
@Transactional
	
public class CourseOverviewService extends BaseService<CourseOverview> {
	@Autowired
	private CourseDAO courseDAO;
	
	@Autowired
	private CourseOverviewDAO courseOverviewDAO;
	
	@Override
	public BaseDAO<CourseOverview> getDAO() {
		return courseOverviewDAO;
	}


	

}
