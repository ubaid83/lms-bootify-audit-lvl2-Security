package com.spts.lms.plagscan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spts.lms.web.controllers.StudentAssignmentController;

import copyleaks.sdk.api.CopyleaksCloud;
import copyleaks.sdk.api.CopyleaksProcess;
import copyleaks.sdk.api.eProduct;
import copyleaks.sdk.api.models.ProcessOptions;
import copyleaks.sdk.api.models.ResultRecord;
@Service("copyLeaks")
public class CopyLeaks {
	
	private static final Logger logger = Logger
			.getLogger(CopyLeaks.class);
	
	@Value("${app}")
	private String app;
	
	Integer avg;
	Integer sum = 0;
	public File multipartToFile(MultipartFile multipart)
			throws IllegalStateException, IOException {
		File convFile = new File(multipart.getOriginalFilename());
		multipart.transferTo(convFile);
		return convFile;
	}

	public Map<String, Integer> scan(String email, String key, File file) {
		CopyleaksCloud copyleaks = new CopyleaksCloud(eProduct.Academic);
		
		Map<String, Integer> resultMap = new HashMap<String, Integer>();

		double max = 0;
		String url = "";
		try {
			logger.info("Login to Copyleaks cloud...");
			logger.info("Email" + email);
			logger.info("Key" + key);

			copyleaks.Login(email, key);
			logger.info("Done!");
			logger.info("Checking account balance...");
			int creditsBalance = copyleaks.getCredits();
			logger.info("Done (" + creditsBalance + " credits)!");
			if (creditsBalance == 0) {
				logger.info("ERROR: You do not have enough credits left in your account to proceed with this scan! 							(current credit balance = "
								+ creditsBalance + ")");
				return resultMap;

			}

			ProcessOptions scanOptions = new ProcessOptions();
			ResultRecord[] results;
			CopyleaksProcess createdProcess;
			createdProcess = copyleaks.CreateByFile(file, scanOptions);
			logger.info("Scanning...");
			int percents = 0;

			while (percents != 100
					&& (percents = createdProcess.getCurrentProgress()) <= 100) {
				logger.info(percents + "%");

				if (percents != 100)
					Thread.sleep(4000);
			}
			
			logger.info("createdProcess------>"+createdProcess);

			results = createdProcess.GetResults();
			
			logger.info("results------>"+results);

			Integer temp1[] = new Integer[results.length];
			
			if (results.length == 0) {
				logger.info("No results.");
				
				resultMap.put("Not Copied From Anywhere", 0);
			} else {

				for (int i = 0, k = 1; i < results.length - 1; k = i + 1, i++) {

					logger.info(String.format("Result %1$s:", i + 1));
					if (results[i].getURL() != null) {
						logger.info(String.format("Url: %1$s",
								results[i].getURL()));
					}
					logger.info(String.format(
							"Information: %1$s copied words (%2$s%%)",
							results[i].getNumberOfCopiedWords(),
							results[i].getPercents()));

					temp1[k] = results[i].getPercents();
					if (temp1[k] > max) {
						max = temp1[k];
						url = results[i].getURL();
					}
					logger.info("max------>"+max);
					sum = sum + temp1[k];
					
					logger.info(String.format("Comparison Report: %1$s",
							results[i].getComparisonReport()));
					logger.info(String.format("Title: %1$s",
							results[i].getTitle()));
					logger.info(String.format("Introduction: %1$s",
							results[i].getIntroduction()));
					logger.info(String.format(
							"Embeded Comparison: %1$s",
							results[i].getEmbededComparison()));
				}
				avg = sum / (results.length);
				logger.info("TEMP1 length-------------" + temp1.length);
				logger.info("SUM-------------" + sum);
				logger.info("AVERAGE--------------" + avg);
				Integer plagValue = (int) max;
				logger.info("plagValue---->"+plagValue);
				resultMap.put(url, plagValue);
				
				
			}
			int creditsBalanceAfterScan = copyleaks.getCredits();
			
			logger.info("Done (" + (creditsBalance - creditsBalanceAfterScan) + " credits used)!");
			
			resultMap.put("creditUsed", (creditsBalance - creditsBalanceAfterScan));

		}

		catch (copyleaks.sdk.api.exceptions.CommandFailedException copyleaksException) {
			logger.error("Failed!");
			logger.error("*** Error (%d):\n" +
					copyleaksException.getCopyleaksErrorCode());
			logger.error(copyleaksException.getMessage());
			logger.error("Exception", copyleaksException);

		} catch (Exception ex) {

			ex.printStackTrace();
			logger.error("Failed!");
			logger.error("Unhandled Exception");
			logger.error("Exception", ex);
		}
		logger.info("resultMap--->"+resultMap);
		return resultMap;
	}
	
	
	//V3
	
	public Map<String,String> scanFileForCopyleaks(String email, String key,File file, String scanId) {
		Map<String,String> scanStatus = new HashMap<String, String>();
		try {	
			
			//CopyleaksV3 cp = new CopyleaksV3();
			Client clientWS = null;
			ClientConfig clientConfig = null;
			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder = null;
			Response response = null;
			
			String accessToken = "";
			long credits = 0;
			
			int responseCode;
			clientConfig = new ClientConfig();
			clientWS = ClientBuilder.newClient(clientConfig);
			
			//1. login and get access token
			String loginJson = "{\"email\": \""+email+"\",\"key\": \""+key+"\"}";
			webTarget = clientWS.target("https://id.copyleaks.com/v3/account/login/api");
			invocationBuilder = webTarget.request();
			response = invocationBuilder.post(Entity.entity(loginJson.toString(), MediaType.APPLICATION_JSON));
			responseCode = response.getStatus();
			logger.info("response--->" + response);
			if(responseCode == 200) {
				String loginJsonResponse = response.readEntity(String.class);
				logger.info("loginJsonResponse--->" + loginJsonResponse);
				JSONObject loginJsonObject = new JSONObject(loginJsonResponse);
				accessToken = loginJsonObject.getString("access_token");
				//cp.setAccess_token(loginJsonObject.getString("access_token"));
			}else {
				scanStatus.put("Error", "Error while scanning file.");
				return scanStatus;
			}
			
			
			//2. Check credits
			webTarget = clientWS.target("https://api.copyleaks.com/v3/education/credits");
			invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,"Bearer "+ accessToken);
			response = invocationBuilder.get();
			logger.info("response--->" + response);
			responseCode = response.getStatus();
			if(responseCode == 200) {
				String creditsResponse = response.readEntity(String.class);
				logger.info("creditsResponse--->" + creditsResponse);
				JSONObject creditJsonObject = new JSONObject(creditsResponse);
				credits = creditJsonObject.getLong("Amount");
				if (credits == 0) {
					logger.info("ERROR: You do not have enough credits left in your account to proceed with this scan! + credits + ");
					scanStatus.put("Error", "You do not have enough credits left in your account to proceed with this scan!");
					return scanStatus;
				}
				//cp.setCreditAmount(creditJsonObject.getLong("Amount"));
			}
			
			
			//3. Submit file for scan
			String base64File = encoder(file); 
			//File file = new File(filePath);
			String fileName = file.getName();
			
			String submitFileJson ="";
			//sandbox true for testing
			submitFileJson = "{" + 
					"\"base64\": \""+ base64File +"\"," + 
					"\"filename\": \""+fileName+"\"," + 
					"\"properties\": {"+
					//"\"sandbox\": true," + 
					"\"webhooks\": {" + 
					"\"status\": \"https://portal.svkm.ac.in/"+app+"/api/webhook/{STATUS}/"+scanId+"\"" + 
					"}" + 
					"}" + 
					"}";
			//logger.info("submitFileJson---->"+submitFileJson);
			webTarget = clientWS.target("https://api.copyleaks.com/v3/education/submit/file/"+scanId);
			invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,"Bearer "+ accessToken);
			response = invocationBuilder.put(Entity.entity(submitFileJson.toString(), MediaType.APPLICATION_JSON));
			logger.info("response--->" + response);
			responseCode = response.getStatus();
			
			if(responseCode == 201) {
				String submitFileResponse = response.readEntity(String.class);
				logger.info("submitFileResponse--->"+submitFileResponse);
				scanStatus.put("Success","Scan Created");
			}else if(responseCode == 400) {
				String submitFileResponse = response.readEntity(String.class);
				logger.info("submitFileResponse--->"+submitFileResponse);
				scanStatus.put("Error","A scan with the same Id already exists in the system.");
				//logger.error("A scan with the same Id already exists in the system.");
			}else if(responseCode == 401) {
				scanStatus.put("Error","Authorization has been denied for this request.");
				//logger.error("Authorization has been denied for this request.");
			} else if(responseCode == 409) {
				String submitFileResponse = response.readEntity(String.class);
				logger.info("submitFileResponse--->"+submitFileResponse);
				scanStatus.put("Error","A scan with the same Id already exists in the system.");
				//logger.error("A scan with the same Id already exists in the system.");
			}
			
		}catch(Exception e) {
			scanStatus.put("Error",e.getMessage());
			logger.error("Error in scanFileForCopyleaks--->",e);
		}
		return scanStatus;
	}
	
	public static String encoder(File file) {
        String base64File = "";
        //File file = new File(filePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.getEncoder().encodeToString(fileData);
        } catch (FileNotFoundException e) {
        	logger.error("File not found" , e);
        } catch (IOException ioe) {
        	logger.error("Exception while reading the file " , ioe);
        }
        return base64File;
    }

}
