package com.spts.lms.web.utils;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyClient {

	static Logger logger = LoggerFactory.getLogger(JerseyClient.class);

	public String postAndReturnResponseUsingParams(String url, String applicationType, Map<String, String> queryMapper) {
		try {
			Client client = ClientBuilder.newClient();
		    WebTarget webTarget = client.target(url);
		    for (Map.Entry<String, String> entry : queryMapper.entrySet()) {
		    	webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
		    }
			Invocation.Builder invocationBuilder = webTarget.request(applicationType);
			Response response = invocationBuilder.get();
			return response.readEntity(String.class);
		} catch (Exception e) {
			logger.error("Error");
			return "ERROR";
		}

	}

	public String postAndJsonReturnResponse(String url, String applicationType, String json) {
		try {
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(applicationType);
			Response response = invocationBuilder.post(Entity.json(json));
			return response.readEntity(String.class);
		} catch (Exception e) {
			logger.error("Error");
			return "ERROR";
		}

	}

	public static void main(String[] args) {
		JerseyClient jer = new JerseyClient();
		String url = "http://localhost:8086//salesforce-endPoint/pullShippingAddressSalesforce";
		Map<String,String> mapOfInputTest = new HashMap<>();
		mapOfInputTest.put("sapid","77117102117");
		mapOfInputTest.put("applicationName","Certificate");
		String response = jer.postAndReturnResponseUsingParams(url, MediaType.APPLICATION_JSON,mapOfInputTest);
		logger.info(response);
	}
}
