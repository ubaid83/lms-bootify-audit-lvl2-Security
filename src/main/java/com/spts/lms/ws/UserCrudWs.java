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
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.user.UserService;

@RestController
public class UserCrudWs {
	private static final Logger logger = Logger.getLogger(UserCrudWs.class);

	@Autowired
	UserService userService;

	@RequestMapping(value = "/ws/userAddOrUpdate", method = { RequestMethod.POST })
	public @ResponseBody Status userAdd(@RequestBody String json) {
		Status status = new Status();

		ObjectMapper mapper = new ObjectMapper();
		try {
			User user = mapper.readValue(json, User.class);
			userService.upsert(user);
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception e) {
			logger.error("Exception", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}

		return status;

	}
	
	@RequestMapping(value = "/ws/userAddOrUpdates", method = { RequestMethod.POST })
	public @ResponseBody Status userAdds(@RequestBody String json) {
		Status status = new Status();

		ObjectMapper mapper = new ObjectMapper();
		try {
			List<User> users = mapper.readValue(json,
					new TypeReference<List<User>>() {
					});

		
			userService.upsertBatch(users);
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception e) {
			logger.error("Exception", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}

		return status;

	}
	
	@RequestMapping(value = "/userDelete", method = { RequestMethod.POST })
	public @ResponseBody Status userDe(@RequestBody String json) {
		Status status = new Status();

		ObjectMapper mapper = new ObjectMapper();
		try {
			User user = mapper.readValue(json, User.class);
			userService.delete(user);
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception e) {
			logger.error("Exception", e);
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
		}

		return status;

	}

}
