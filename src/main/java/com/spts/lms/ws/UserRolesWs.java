package com.spts.lms.ws;

import java.util.Date;
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
import com.spts.lms.beans.Status;
import com.spts.lms.beans.StatusType;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;

@RestController
public class UserRolesWs {

	@Autowired
	UserRoleService userRoleService;
	
	@Autowired
	UserService userService;

	private static final Logger logger = Logger.getLogger(UserRolesWs.class);

	@RequestMapping(value = "/ws/userRolesAddOrUpdate", method = { RequestMethod.POST })
	public @ResponseBody Status userCourseAddOrUpdate(@RequestBody String json) {

		ObjectMapper mapper = new ObjectMapper();
		Status status = new Status();
		try {
			UserRole userRole = mapper.readValue(json, UserRole.class);
		/*	User user = userService.findByUserName(userRole.getUsername());
			userRole.setUser(user);
			userRole.setCreatedBy("CA");
			userRole.setLastModifiedBy("CA");
			userRole.setLastModifiedDate(new Date());*/
			userRoleService.upsert(userRole);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
			logger.error(e);

		}

		return status;

	}
	
	@RequestMapping(value = "/ws/userRolesAddOrUpdates", method = { RequestMethod.POST })
	public @ResponseBody Status userCourseAddOrUpdates(@RequestBody String json) {

		ObjectMapper mapper = new ObjectMapper();
		Status status = new Status();
		try {
			List<UserRole> userRoles = mapper.readValue(json,
					new TypeReference<List<UserRole>>() {
					});
			
		/*	User user = userService.findByUserName(userRole.getUsername());
			userRole.setUser(user);
			userRole.setCreatedBy("CA");
			userRole.setLastModifiedBy("CA");
			userRole.setLastModifiedDate(new Date());*/
			userRoleService.upsertBatch(userRoles);
			status.setStatus(StatusType.SUCCESS);

		} catch (Exception e) {
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
			logger.error(e);

		}

		return status;

	}

	@RequestMapping(value = "/ws/userRolesDelete", method = { RequestMethod.POST })
	public @ResponseBody Status userRolesDelete(@RequestBody String json) {

		ObjectMapper mapper = new ObjectMapper();
		Status status = new Status();
		try {
			UserRole userRole = mapper.readValue(json, UserRole.class);
			userRoleService.softDeleteByUsername(userRole.getUsername());
			status.setStatus(StatusType.SUCCESS);
		} catch (Exception e) {
			status.setStatus(StatusType.ERROR);
			status.setErrorDescription(e.toString());
			logger.error(e);

		}

		return status;
	}

}
