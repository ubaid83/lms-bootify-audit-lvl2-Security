package com.spts.lms.services.calender;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.auth.oauth2.Credential;
import com.spts.lms.beans.calender.Calender;
import com.spts.lms.daos.BaseDAO;
import com.spts.lms.daos.calender.CalenderDAO;
import com.spts.lms.services.BaseService;

@Service("calenderService")
@Transactional
public class CalenderService extends BaseService<Calender> {

	@Autowired
	private CalenderDAO calenderDAO;

	@Override
	public BaseDAO<Calender> getDAO() {
		return calenderDAO;
	}

	public List<Calender> listOfEventsForUser(String userName) {
		return calenderDAO.listOfEventsForUser(userName);
	}

	public List<Calender> listOfEventsForUserByCourse(String userName,
			Set<Long> courseIdList) {
		return calenderDAO.calendarEventsByCoursesList(userName, courseIdList);
	}

	public List<Calender> getAllEvents(String userName, Long programId) {

		return calenderDAO.getAllEvents(userName, programId);
	}

	public List<Calender> getAllEventsCourseWise(String userName,
			String courseId) {
		return calenderDAO.getAllEventsByCourseId(userName, courseId);
	}

	public List<Calender> getAllEventsForAdmin(String userName, Long programId) {

		return calenderDAO.getAllEventsForAdmin(userName, programId);

	}

}
