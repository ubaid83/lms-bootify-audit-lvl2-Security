package com.spts.lms.auth;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.beans.Status;
import com.spts.lms.beans.user.UserTo;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.daos.user.UserRoleDAO;
import com.spts.lms.web.controllers.BaseController;

@Component
public class CustomAuthenticationProvider extends BaseController implements
		AuthenticationProvider {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserRoleDAO roleDao;

	@Autowired
	UserDAO userDao;

	@Value("#{'${userManagementUrl:http://192.168.2.116:8080/usermgmt/login}'}")
	String userManagementUrl;
	
	@Value("#{'${userRoleMgmtCrudUrl:http://localhost:8080}'}")
	String userRoleMgmtCrudUrl;

	@Value("#{'${appName:nmims}'}")
	String appName;

	Client client = ClientBuilder.newClient();

	ObjectMapper mapper = new ObjectMapper();

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {

		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		
		
		Token token = null;

		WebTarget webTarget = client.target(userManagementUrl
				+ "/user-details/details?username=" + name);
		Invocation.Builder invocationBuilder = webTarget
				.request(MediaType.APPLICATION_JSON);

		String resp = invocationBuilder.get(String.class);
		logger.info("resp" + resp);
		UserTo response;
		try {
			Status status = mapper.readValue(resp, Status.class);

			response = status.getUser();
			if (response != null) {
				Map<String, List<String>> appRoles = response.getAppRoles();

				token = new Token(name, password, token.setAppRolesEnum(
						appName, appRoles));
				Map<String, Map<String, Set<String>>> menuRoles = response
						.getMenuRoles();
				token.setMenuRights(menuRoles.get(appName));
				token.setAppRoles(appRoles);

			}
		} catch (IOException e) {
			logger.error("exception", e);
		}

		return token;

		/*
		 * User user = userDao.findCredsByUserName(name); if (user != null) { if
		 * (PasswordGenerator.matches(password, user.getPassword())) { token =
		 * new Token(name, password, roleDao.findRolesByUsername(name));
		 * 
		 * logger.info("Name = " + name);
		 * 
		 * }
		 * 
		 * return token; } else return null;
		 */
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
