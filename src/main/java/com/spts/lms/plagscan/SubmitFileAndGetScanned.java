package com.spts.lms.plagscan;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service

public class SubmitFileAndGetScanned {

	static private String url = "https://api.plagscan.com/v3/token";
	
	@Value("${plagscanUrl:''}")
	private String plagscanUrl;
	@Value("${client_id:''}")
	private String client_id;
	@Value("${client_secret:''}")
	private String client_secret;
	/**
	 * @param args
	 *            the command line arguments
	 *            
	 */
	private static final Logger logger = Logger
			.getLogger(SubmitFileAndGetScanned.class);
	public  String getAccessToken() {
		/*
		 * / CHANGE THE client_id AND client_secret VARIABLE WITH YOUR OWN TO
		 * GET ACCESS TO YOUR DATA
		 */
		// Create the client to make the request
		CloseableHttpClient client = HttpClients.createDefault();
		// Create the request method with the url
		HttpPost request = new HttpPost(plagscanUrl);
		String access_token = "";
		// Adding the parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		logger.info("client_id "+client_id);
		params.add(new BasicNameValuePair("client_id", client_id));
		params.add(new BasicNameValuePair("client_secret", client_secret));
		CloseableHttpResponse response = null;
		try {
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			// Execute the request and get response
			response = client.execute(request);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String strResponse = EntityUtils.toString(entity);
				// We can show the response string directly
				logger.info(strResponse);
				// Also we can get the JSON object and save the access token
				JSONObject json = new JSONObject(strResponse);
				logger.info("json "+json);
				access_token = json.getString("access_token");
				logger.info(access_token);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception",e);
		}

		finally {
			if (response != null)
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("exception",e);
				}
		}

		return access_token;

	}

	public  String getPid(String filePath) {
		/*
		 * / CHANGE THE access_token VARIABLE WITH YOUR OWN TO GET ACCESS TO
		 * YOUR DATA
		 */
		String access_token = getAccessToken();
		String pid = "";

		// Create the client to make the request
		CloseableHttpClient client = HttpClients.createDefault();
		// Create the request method with the url
		HttpPost request = new HttpPost(plagscanUrl);
		CloseableHttpResponse response = null;
		try{
			HttpEntity reqEntity = MultipartEntityBuilder.create().addBinaryBody("fileUpload", new File(filePath)).build();

			request.setEntity(reqEntity);

			// Adding to the URL the parameters (because it is a GET request)
			URI uri = new URIBuilder(request.getURI()).addParameter(
					"access_token", access_token).build();
			((HttpRequestBase) request).setURI(uri);

			// Execute the request and get response
			response = client.execute(request);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String strResponse = EntityUtils.toString(entity);
				// We can show the response string directly
				System.out.println(strResponse);
				// Also we can get the JSON object
				JSONObject json = new JSONObject(strResponse);
				JSONObject data = (JSONObject) json.get("data");
				pid = data.get("docID").toString();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null)
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Exception",e);
				}
		}
		return pid;

	}

	
	
public String getPlagLevel(String filepath) throws IOException, URISyntaxException, JSONException {
    /*
    / CHANGE THE access_token AND $docID VARIABLE WITH YOUR OWN TO GET ACCESS TO YOUR DATA
    */
 
	String plagLevel=null;
    //Create the client to make the request
	  CloseableHttpResponse response=null;
    try{
    CloseableHttpClient client = HttpClients.createDefault();
    //Create the request method with the url
    /*
    ****** MUST ADD THE ID OF THE DOCUMENT TO THE URL ******
    */
  
    HttpGet request = new HttpGet(plagscanUrl+getPid(filepath));

    //Adding to the URL the parameters (because it is a GET request)
    URI uri = new URIBuilder(request.getURI())
            .addParameter("access_token", getAccessToken())
            .build();
    ((HttpRequestBase ) request).setURI(uri);

    //Execute the request and get response
   response = client.execute(request);

      HttpEntity entity = response.getEntity();
      if(entity != null){
        String strResponse = EntityUtils.toString(entity);
        //We can show the response string directly
        System.out.println(strResponse);

        //Also we can get the JSON object and for example show the PlagLevel
        JSONObject json = new JSONObject(strResponse);
        logger.info("JSON   "+json);
        if(json.has("data"))
        {
        JSONObject data = (JSONObject) json.get("data");
        plagLevel = data.get("plagLevel").toString();
      }}
    }
    catch(Exception e)
    {
    	logger.error("Exception",e);
    }
    finally{
    	if(response!=null)
      response.close();
    }
	return plagLevel;

}
}
