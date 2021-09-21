package com.spts.lms.services.notification;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.spts.lms.web.controllers.AssignmentController;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Component
public class Notifier {

	@Value("${notifierUrl}")
	private String notifierUrl;

	private static final Logger logger = Logger
			.getLogger(AssignmentController.class);

	Gson gson = new Gson();

	Client client = Client.create();

	public boolean sendEmail(Map<String, String> email,
			Map<String, String> mobile, String subject, String msg) {

		WebResource emailResource = client.resource(notifierUrl
				+ "/smtp-email-endpoint/sendPlainEmail");

		WebResource smsResource = client.resource(notifierUrl
				+ "/sms-endpoint/sendSMS");

		String emailStr = gson.toJson(email);
		String mobileStr = gson.toJson(mobile);

		String input = "{\"userIdToEmails\":" + emailStr + ",\"subject\":\""
				+ subject + "\",\"message\":\"" + msg
				+ "\",\"templateBody\":null,\"parameters\":null}";

		String ip = "{\"createdBy\":null,\"createdDate\":null,\"lastModifiedDate\":null,\"lastModifiedBy\":null,\"applicationName\":null,"
				+ "\"userToNumbers\":"
				+ mobileStr
				+ ",\"message\":\""
				+ msg
				+ "\",\"templateBody\":\""
				+ msg
				+ "\",\"parameters\":"
				+ gson.toJson(new HashMap()) + "}";
		logger.info("ip" + ip);
		logger.info("input" + input);
		Boolean isSuccess = false;
		try {
			if (!email.isEmpty()) {
				ClientResponse response = emailResource
						.type("application/json").post(ClientResponse.class,
								input);
				logger.info("Email response" + response.getStatus());
				isSuccess = (response.getStatus() == 200);
			}
			if (!mobile.isEmpty()) {

				ClientResponse smsResponse = smsResource.type(
						"application/json").post(ClientResponse.class, ip);
				isSuccess = (smsResponse.getStatus() == 200);
				logger.info("Sms response" + smsResponse.getStatus());
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		return isSuccess;

	}
}
