package com.spts.lms.ws;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.beans.course.Course;
import com.spts.lms.services.course.CourseService;

@RestController
public class CourseWs {
	private static final Logger logger = Logger.getLogger(CourseWs.class);

	@Autowired
	CourseService courseService;

	@RequestMapping(value = "/ws/addOrUpdateCourse", method = { RequestMethod.POST })
	public @ResponseBody Status addOrUpdateProgram(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status status = new Status();
		try {
			Course course = objMapper.readValue(json, Course.class);
			courseService.upsert(course);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}
		return status;
	}

	@RequestMapping(value = "/ws/addOrUpdateCourses", method = { RequestMethod.POST })
	public @ResponseBody Status addOrUpdateCourses(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status status = new Status();
		try {

			List<Course> courses = objMapper.readValue(json,
					new TypeReference<List<Course>>() {
					});

			courseService.upsertBatch(courses);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}
		return status;
	}

	@RequestMapping(value = "/ws/deleteCourse", method = { RequestMethod.POST })
	public @ResponseBody Status deleteCourse(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status status = new Status();
		try {
			Course course = objMapper.readValue(json, Course.class);
			courseService.delete(course);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}
		return status;
	}
}
