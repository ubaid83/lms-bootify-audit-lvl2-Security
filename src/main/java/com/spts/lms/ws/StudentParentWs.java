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
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.studentParent.StudentParentService;

@RestController
public class StudentParentWs {

	private static final Logger logger = Logger
			.getLogger(StudentParentWs.class);

	@Autowired
	StudentParentService studentParentService;

	@RequestMapping(value = "/ws/addOrUpdateStudentParent", method = { RequestMethod.POST })
	public @ResponseBody Status addOrUpdateStudentParent(
			@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status obStatus = new Status();
		try {
			StudentParent studentParent = objMapper.readValue(json,
					StudentParent.class);
			studentParentService.upsert(studentParent);
			obStatus.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			obStatus.setStatus(StatusType.ERROR);
			obStatus.setErrorDescription(e.toString());
		}
		return obStatus;
	}

	@RequestMapping(value = "/ws/addOrUpdateStudentParents", method = { RequestMethod.POST })
	public @ResponseBody Status addOrUpdateStudentParents(
			@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status obStatus = new Status();
		try {

			List<StudentParent> stuParents = objMapper.readValue(json,
					new TypeReference<List<StudentParent>>() {
					});

			studentParentService.upsertBatch(stuParents);
			obStatus.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			obStatus.setStatus(StatusType.ERROR);
			obStatus.setErrorDescription(e.toString());
		}
		return obStatus;
	}

	@RequestMapping(value = "/ws/deleteStudentParent", method = { RequestMethod.POST })
	public @ResponseBody Status deleteStudentParent(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status obStatus = new Status();
		try {
			StudentParent studentParent = objMapper.readValue(json,
					StudentParent.class);
			studentParentService.delete(studentParent);
			obStatus.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			obStatus.setStatus(StatusType.ERROR);
			obStatus.setErrorDescription(e.toString());
		}
		return obStatus;
	}

}
