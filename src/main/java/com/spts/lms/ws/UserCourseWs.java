package com.spts.lms.ws;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.beans.Status;
import com.spts.lms.beans.StatusType;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.services.course.UserCourseService;

@RestController
public class UserCourseWs {

	@Autowired
	UserCourseService userCourseService;

	private static final Logger logger = Logger.getLogger(UserCourseWs.class);

	@RequestMapping(value = "/ws/userCourseAddOrUpdate", method = { RequestMethod.POST })
	public @ResponseBody Status userCourseAddOrUpdate(@RequestBody String json) {
		ObjectMapper mapper = new ObjectMapper();

		Status status = new Status();
		try {

			UserCourse userCourse = mapper.readValue(json, UserCourse.class);
			userCourseService.upsert(userCourse);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
			logger.error("Exception", e);

		}

		return status;

	}

	@RequestMapping(value = "/ws/userCourseAddOrUpdates", method = { RequestMethod.POST })
	public @ResponseBody Status userCourseAddOrUpdates(@RequestBody String json) {
		ObjectMapper mapper = new ObjectMapper();

		Status status = new Status();
		try {
			List<UserCourse> userCourses = mapper.readValue(json,
					new TypeReference<List<UserCourse>>() {
					});

			userCourseService.upsertBatch(userCourses);

			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
			logger.error("Exception", e);

		}

		return status;

	}

	@RequestMapping(value = "/ws/userCoursedelete", method = { RequestMethod.POST })
	public @ResponseBody Status userCoursedelete(@RequestBody String json) {
		ObjectMapper mapper = new ObjectMapper();
		Status status = new Status();

		try {

			UserCourse userCourse = mapper.readValue(json, UserCourse.class);
			userCourseService.delete(userCourse);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
			logger.error(e);

		}

		return status;
	}
}
