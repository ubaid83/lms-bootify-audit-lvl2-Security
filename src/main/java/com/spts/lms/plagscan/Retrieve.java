//PlagScan API usage example in JAVA
// REQUIRES httpcomponents-client-4.5.3 lib AND java-json lib
//Make sure to change access_token variable
//METHOD RETRIEVE
package com.spts.lms.plagscan;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSON;

public class Retrieve {

	private static final Logger logger = Logger.getLogger(Retrieve.class);
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static String pid;

	public String getPlagLevelForFile(String pid, String plagscanKey, String url)
			throws IOException, URISyntaxException, JSONException {
		logger.info("get pid " + pid);
		return this.retrievePlagLevel(pid, plagscanKey, url);

	}

	public String retrievePlagLevel(String pid, String plagscanKey, String url)
			throws IOException, URISyntaxException, JSONException {
		String plagLevel = "0";
		/*
		 * / CHANGE THE access_token AND $docID VARIABLE WITH YOUR OWN TO GET
		 * ACCESS TO YOUR DATA
		 */

		/* String access_token = "MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3"; */

		logger.info("DOC ID " + pid);
		// Create the client to make the request
		CloseableHttpClient client = HttpClients.createDefault();
		// Create the request method with the url
		/*
		 * ***** MUST ADD THE ID OF THE DOCUMENT TO THE URL ******
		 */
		HttpGet request = new HttpGet(url + pid);

		// Adding to the URL the parameters (because it is a GET request)
		URI uri = new URIBuilder(request.getURI()).addParameter("access_token",
				"MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3").build();
		((HttpRequestBase) request).setURI(uri);

		// Execute the request and get response
		CloseableHttpResponse response = client.execute(request);

		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String strResponse = EntityUtils.toString(entity);
				// We can show the response string directly
				logger.info(strResponse);

				// Also we can get the JSON object and for example show the
				// PlagLevel

				JSONObject json = new JSONObject(strResponse);
				/* JSONArray json = new JSONArray(strResponse); */

				logger.info("PlagLevel: " + json.getJSONObject("plagLevel"));
				plagLevel = JSON.toString(json.getJSONObject("plagLevel"));

			}
		}

		catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			if (response != null)
				response.close();
		}
		return plagLevel;

	}

}
