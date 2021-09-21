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
import com.spts.lms.beans.program.Program;
import com.spts.lms.services.program.ProgramService;

@RestController
public class ProgramWs {
	private static final Logger logger = Logger.getLogger(ProgramWs.class);

	@Autowired
	ProgramService programService;

	@RequestMapping(value = "/ws/addOrUpdateProgram", method = { RequestMethod.POST })
	public @ResponseBody Status addOrUpdateProgram(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status status = new Status();
		try {
			Program program = objMapper.readValue(json, Program.class);
			programService.upsert(program);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}
		return status;
	}

	@RequestMapping(value = "/ws/addOrUpdatePrograms", method = { RequestMethod.POST })
	public @ResponseBody Status addOrUpdatePrograms(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status status = new Status();
		try {
			List<Program> programs = objMapper.readValue(json,
					new TypeReference<List<Program>>() {
					});

			programService.upsertBatch(programs);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}
		return status;
	}

	@RequestMapping(value = "/ws/deleteProgram", method = { RequestMethod.POST })
	public @ResponseBody Status deleteProgram(@RequestBody String json) {
		ObjectMapper objMapper = new ObjectMapper();
		Status status = new Status();
		try {
			Program program = objMapper.readValue(json, Program.class);
			programService.delete(program);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			logger.error("Error ", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}
		return status;
	}
}
