package com.spts.lms.services.FacultyLectureRescheduleServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spts.lms.beans.FacultyLectureReschedule.FacultyLectureReschedule;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.FacultyLectureReschedule.FacultyLectureRescheduleDao;
import com.spts.lms.services.BaseService;

@Service("facultyLectureRescheduleService")
public class FacultyLectureRescheduleService extends BaseService<FacultyLectureReschedule>{

	@Autowired
	FacultyLectureRescheduleDao facultyLectureRescheduleDao;
	
	@Override
	protected BaseDAO<FacultyLectureReschedule> getDAO() {
		// TODO Auto-generated method stub
		return facultyLectureRescheduleDao;
	}
	
	public String getPlayerIdForFaculty(String username) {
		return facultyLectureRescheduleDao.getPlayerIdForFaculty(username);
	}

}
