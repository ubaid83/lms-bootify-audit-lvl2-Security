package com.spts.lms.web.controllers.studentConfirmation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationPeriodService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.studentms.sap.ZCHANGESTMOBILEEMAILWSD;
import com.spts.lms.studentms.sap.ZCHANGESTMOBILEEMAILWSDFEQ;
import com.spts.lms.studentms.sap.ZmessageLogTt;
import com.spts.lms.web.controllers.BaseController;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

import sun.misc.BASE64Decoder;


@Controller
public class studentDetailConfirmationController extends BaseController {
	private JdbcTemplate jdbcTemplate;
	@Autowired
	studentDetailConfirmationService studentDetailConfirmationService;

	@Autowired
	studentDetailConfirmationPeriodService studentDetailConfirmationPeriodService;

	@Value("${imgPath}")
	private String saveimagePath;

	@Autowired
	Notifier notifier;

	@Autowired
	private UserService userService;
	
	@Autowired
	ProgramService programService;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;
	
	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	Client client = ClientBuilder.newClient();

	private static final Logger logger = Logger
			.getLogger(studentDetailConfirmationController.class);
	private static final String FILE_SEPARATOR = "/";

	// // 1 MB
	@RequestMapping(value = "/updateUserProfileForMaster", method = RequestMethod.POST)
	public String updateUserProfileForMaster(
			@ModelAttribute("userprofile") User user, Model m,
			Principal principal, RedirectAttributes r,
			@RequestParam("file") MultipartFile file) throws IOException,
			URISyntaxException {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		logger.info("GET FILE SIZE--------------->" + file.getSize());

		logger.info("UserNAme ----------->" + username);
		logger.info("Details----------------->" + user);

		logger.info("User Email Master------------->" + user.getEmail());
		logger.info("User Phone Master------------->" + user.getMobile());

		logger.info("User Sec Question Master------------->"
				+ user.getSecquestion());
		logger.info("User Sec Answer Master------------->"
				+ user.getSecAnswer());
		logger.info("file1----------------->" + file.getName());
		Response response = null;
		String jsonResponse = null;
		try {
			HtmlValidation.validateHtml(user, new ArrayList<>());
			BusinessBypassRule.validateEmail(user.getEmail());
			BusinessBypassRule.validateNumeric(user.getMobile());
			BusinessBypassRule.validateAlphaNumeric(user.getSecAnswer());
			
			String json = "{\"secQuestion\":\""+ user.getSecquestion() +"\"}";
			
			WebTarget webTarget3 = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/checkIfSecurityQuestionExists"));
			Invocation.Builder invocationBuilder3 = webTarget3.request(MediaType.APPLICATION_JSON);
			Response response1 = invocationBuilder3.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));
			jsonResponse = response1.readEntity(String.class);
			logger.info("jsonResponse is " + jsonResponse);
			if(jsonResponse.equals("false")) {
				throw new ValidationException("Invalid Security Question");				
			}
			
		} catch (ValidationException ve) {
			ve.printStackTrace();
			setError(r, ve.getMessage());
			return "redirect:/homepage";
		}
		
		
		
		
		String originalfileName = file.getOriginalFilename();
		//Audit change start
		Tika tika = new Tika();
		  String detectedType = tika.detect(file.getBytes());
		if (originalfileName.contains(".")) {
			Long count = originalfileName.chars().filter(c -> c == ('.')).count();
			logger.info("length--->"+count);
			if (count > 1 || count == 0) {
				m.addAttribute("fileuploaderror", "File uploaded is invalid!");
				setError(r, "File uploaded is invalid!");
				return "redirect:/homepage";
			}else {
				String extension = FilenameUtils.getExtension(originalfileName);
				logger.info("extension--->"+extension);
				if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
						|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
					m.addAttribute("fileuploaderror", "File uploaded is invalid!");
					setError(r, "File uploaded is invalid!");
					return "redirect:/homepage";
				}else {
					byte [] byteArr=file.getBytes();
					if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
														(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) ||
														("text/plain").equals(detectedType)) {
						logger.info("File is valid--->");
					}else {
						m.addAttribute("fileuploaderror", "File uploaded is invalid!");
						setError(r, "File uploaded is invalid!");
						return "redirect:/homepage";
					}
				}
			}
		}else {
			m.addAttribute("fileuploaderror", "File uploaded is invalid!");
			setError(r, "File uploaded is invalid!");
			return "redirect:/homepage";
		}
		//Audit change end
		File convFile = new File(originalfileName);
		File saveimage = new File(saveimagePath);

		logger.info("Correct display File Path------------->" + saveimage);
		logger.info("originalfileName11------------->" + originalfileName);
		/* m.addAttribute("email",user.getEmail()); */
		double kilobyteSize = file.getSize();
		double ogfilesize = 200;// in kb

		logger.info("kilobyteSize=========>>>"
				+ Utils.round((kilobyteSize / 1024), 2) + "kb");

		double kilo = Utils.round((kilobyteSize / 1024), 2);

		logger.info("kilobyteSize11=========>>>" + kilo + "kb " + ogfilesize
				+ "kb");

		String encodedString = null;

		String uploadimagepath = saveimagePath;
		String realstorpath = saveimagePath.substring(7,
				uploadimagepath.length() - 1);

		logger.info("uploadimagepath-------------->" + uploadimagepath);
		logger.info("realstorpath-------------->" + realstorpath);

		if (kilo <= ogfilesize && kilo > 0) {
			if (originalfileName.contains(".")) {
				String name = FilenameUtils.getBaseName(originalfileName);
				String repo = name.replace(name, username);
				logger.info("RepoName------------------->" + repo);
				// Get the file and save it somewhere
				byte[] bytes = file.getBytes();

				// Convert File Int Bytes

				encodedString = Base64.getEncoder().encodeToString(bytes);
				logger.info("bytes-------------><>" + bytes);
				logger.info("encodedString------------->" + encodedString);

				// Convert File Int Bytes
				logger.info("bImage data---------------------->" + bytes);

				/*
				 * Path path = Paths.get(saveimagePath +
				 * file.getOriginalFilename());
				 */
				/*
				 * URL url = getClass().getResource(uploadimgPath); Path dest =
				 * Paths.get(url.toURI());
				 */
				
				 BASE64Decoder decoder = new BASE64Decoder();
				 byte[] decodedBytes = decoder.decodeBuffer(encodedString);
				

				File of = new File(realstorpath + "/" +username+".JPG");
				FileOutputStream osf = new FileOutputStream(of);
				osf.write(decodedBytes);
				osf.close();
				
				
//				Path path = Paths.get(realstorpath + File.separator + repo
//						+ ".JPG");
//
//				Files.write(path, bytes);
//
//				String newfilePath = realstorpath + File.separator + repo
//						+ ".JPG";

			}
			logger.info("originalfileName------------->" + originalfileName);
			logger.info("Save Image Path_______________>" + realstorpath);
		} else {
			String fileuploaderror = "File size must not be greater 200kb";
			m.addAttribute("fileuploaderror", fileuploaderror);
			setError(r, "File size must not be greater 200kb");
			// file size is greater than 200kb
		}

		User userDB = userService.findByUserName(username);
		userDB.setLastModifiedBy(username);
		userDB.setEmail(user.getEmail());
		userDB.setMobile(user.getMobile());
		userDB.setAddress(user.getAddress());
		// student email,phone,image send to SAP.

		String studentObjId = "";
		String wsdlresponse = "";

		User userBean2 = userService.findStudentObjectId(username);
//		ZCHANGESTMOBILEEMAILWSDFEQ ws = new ZCHANGESTMOBILEEMAILWSDFEQ();

//		Holder<ZmessageLogTt> lst = new Holder<ZmessageLogTt>();

//		ZCHANGESTMOBILEEMAILWSD ws1 = ws.getZCHANGESTMOBILEEMAILBINDFEQ();
//		logger.info("ws.getZCHANGESTMOBILEEMAILBIND()--->"+ws.getZCHANGESTMOBILEEMAILBINDFEQ());
//		BindingProvider prov = (BindingProvider)ws1;
		
//		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "basissp");
//		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "india@123");
//		logger.info("ws2--->"+prov.getRequestContext());
//		
//		logger.info("ref--->"+prov.getEndpointReference());
//		try {  //try was here
//			logger.info("studentObjId--->"+studentObjId);
//			String sha256hex = DigestUtils.sha256Hex(studentObjId);
//			logger.info("sha256hex--->"+sha256hex);
//			if (userBean2 == null) {
//				WebTarget webTarget12 = client.target(URIUtil
//						.encodeQuery(userRoleMgmtCrudUrl
//								+ "/getStudentObjIdByUsername?username="
//								+ username));
//				Invocation.Builder invocationBuilder12 = webTarget12
//						.request(MediaType.APPLICATION_JSON);
//				studentObjId = invocationBuilder12.get(String.class);
//
//				// Code for inserting student in student_mapping table
//				// if("")
//				User newUserForScoolMap = new User();
//				newUserForScoolMap.setUsername(username);
//				newUserForScoolMap.setStudentObjectId(studentObjId);
//				if (!studentObjId.equals("null")) {
//					userService.insertStudentMapping(newUserForScoolMap);
//				}
//
//			} else {
//				studentObjId = userBean2.getStudentObjectId();
//			}
//
//			if (!studentObjId.equals("null")) {
//
////				wsdlresponse = ws1.zchangeStMobileEmail(user.getEmail(), 
////						user.getMobile(), encodedString, username, sha256hex, lst);
//				logger.info("resp----------<><>< " + wsdlresponse);
//
////				logger.info("ZmessageProfileStudMSLog----->" + lst.value);
//			}else{
//				setError(r, "Student id is not found in SAP");
//				return "redirect:/homepage";
//			}
//
//		} catch (Exception ex) {
//
//			logger.error("Exception while calling a webservice", ex);
//		}

//		if (wsdlresponse.equalsIgnoreCase("SUCCESS")) {
			if(userDB.getEmail() != null && userDB.getMobile() != null){
				userService.updateProfile(userDB);
//			}
			

			User userDBSec = userService.findByUserName(username);

			try {
				// Master Validation update user profile
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(userDB);

				user.setUsername(userDB.getUsername());
				user.setUserImage(encodedString);
				logger.info("Get Religopn-------------------->" + user.getNad()
						+ " + + " + user.getUserImage());
				logger.info("GET NAGDDDDD" + user.getNad());
				/*
				 * if (user.getNad() != null) { user.setNad("Y"); } else { user.setNad("N"); }
				 */
				
				String json1= "";
				if(user.getUsername() != null && user.getSecquestion() != null && user.getSecAnswer() != null && user.getUserImage() != null && user.getNad() != null && user.getMobile() != null)
				{
				ObjectMapper mapper1 = new ObjectMapper();
				 json1 = mapper1.writeValueAsString(user);

				logger.info("json1 value for usermgmt------------------------------->"
						+ json1);
			

				// For updating student email and mobile number in usermgmtcrud

				WebTarget webTarget1 = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/addOrUpdateUserForMastervalidation?json="
								+ json));
				Invocation.Builder invocationBuilder1 = webTarget1
						.request(MediaType.APPLICATION_JSON);
				String resp1 = invocationBuilder1.get(String.class);

				// Inserting Master validation data in table with image and
				// other fields
				WebTarget webTarget2 = client
						.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl
								+ "/insertSecurityQuestionForMastervalidation"));

				

				Invocation.Builder invocationBuilder2 = webTarget2.request();
//				Response 
				response = invocationBuilder2.post(Entity.entity(
						json1.toString(), MediaType.APPLICATION_JSON));

				logger.info("response---->" + response);
				String resp = response.readEntity(String.class);

}
				
				else{
					setError(r, "Please enter valid values");
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			setNote(r, "unable to process request");
		}

		return "redirect:/homepage";
	}

	@RequestMapping(value = "/addstudentdetails", method = RequestMethod.POST)
	public String addstudentdetails(
			@ModelAttribute("studentdetails") studentDetailConfirmation studentdetails,
			Model m, Principal principal, RedirectAttributes r
	// @RequestParam(required = false, value = "agree") String agreevalue
	) throws IOException {

		// m.addAttribute("studentdetails" ,new studentDetailConfirmation());

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		logger.info("UserNAme ----------->" + username);
		logger.info("Details----------------->" + studentdetails);

		logger.info("AGREEE VALUE PRINTING 1------------------>"
				+ studentdetails.getMothernamedisagr());
		
		try {
			HtmlValidation.validateHtml(studentdetails, new ArrayList<>());
			if(studentdetails.getFirstname() != null && !studentdetails.getFirstname().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(studentdetails.getFirstname());
			}
			if(studentdetails.getFathername() != null && !studentdetails.getFathername().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(studentdetails.getFathername());
			}
			if(studentdetails.getMothername() != null && !studentdetails.getMothername().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(studentdetails.getMothername());
			}
			
		} catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(r, ve.getMessage());
			return "redirect:/homepage";
		}

		if (studentdetails.getFirstnamedisagr() == null
				|| !studentdetails.getFirstnamedisagr().equalsIgnoreCase(
						"disagree")) {
			studentdetails.setFirstname(null);
		} else {
			if (studentdetails.getFirstname() == null
					|| studentdetails.getFirstname().isEmpty()) {
				setError(r, "Error");
			}
		}

		if (studentdetails.getEmaildisagr() == null
				|| !studentdetails.getEmaildisagr()
						.equalsIgnoreCase("disagree")) {
			studentdetails.setEmail(null);
		} else {
			if (studentdetails.getEmail() == null
					|| studentdetails.getEmail().isEmpty()) {
				setError(r, "Error");
			}
		}

		if (studentdetails.getMobiledisagr() == null
				|| !studentdetails.getMobiledisagr().equalsIgnoreCase(
						"disagree")) {
			studentdetails.setMobile(null);
		} else {
			if (studentdetails.getMobile() == null
					|| studentdetails.getMobile().isEmpty()) {
				setError(r, "Error");
			}
		}

		if (studentdetails.getFathernamedisagr() == null
				|| !studentdetails.getFathernamedisagr().equalsIgnoreCase(
						"disagree")) {
			studentdetails.setFathername(null);
		} else {
			if (studentdetails.getFathername() == null
					|| studentdetails.getFathername().isEmpty()) {
				setError(r, "Error");
			}
		}

		if (studentdetails.getMothernamedisagr() == null
				|| !studentdetails.getMothernamedisagr().equalsIgnoreCase(
						"disagree")) {
			studentdetails.setMothername(null);

		} else {
			if (studentdetails.getMothername() == null
					|| studentdetails.getMothername().isEmpty()) {
				setError(r, "Error");
			}
		}
		if (studentdetails.getPhotodisagr() == null
				|| studentdetails.getPhoto().isEmpty()) {
			studentdetails.setPhotodisagr(null);
		}
		/*
		 * logger.info("User Photo Here------------->" + studentdetails.getUserphoto());
		 */

		// SEND EMAIL TO STUDENT IF ALL VALUE IS AGREE

		/*
		 * User u1 = userService.findByUserName(username); List<String> userList
		 * = new ArrayList<String>(); Map<String, Map<String, String>> result =
		 * null;
		 * logger.info("AGREEE VALUE PRINTING ------------------>"+studentdetails
		 * .getMothernamedisagr()); try {
		 * logger.info("AGREEE VALUE INSITE TRY BLOCK MAIL ------------------>"
		 * +studentdetails.getMothernamedisagr()); if
		 * (studentdetails.getMothernamedisagr().equalsIgnoreCase("agree") ||
		 * studentdetails.getFirstnamedisagr().equalsIgnoreCase("agree") ||
		 * studentdetails.getFathernamedisagr().equalsIgnoreCase("agree")) {
		 * String subject = "Dear" + u1.getFirstname() + "\n"; StringBuilder sb
		 * = new StringBuilder();
		 * sb.append("<html><body><table style='border: solid;'>");
		 * sb.append("<thead><tr>");
		 * sb.append("<th>First Name</th><th>Father Name</th><th>Mother Name</th>"
		 * ); sb.append("</tr></thead>"); sb.append("<tbody><tr>");
		 * sb.append("<td>" + u1.getFirstname() + " <br> Agree</td>");
		 * sb.append("<td>" + u1.getFatherName() + " <br> Agree</td>");
		 * sb.append("<td>" + u1.getMotherName() + "<br> Agree</td>");
		 * 
		 * if(studentdetails.getFirstnamedisagr().equalsIgnoreCase("agree")){
		 * sb.append("<td>" + u1.getFirstname() + " <br> Agree</td>"); }else {
		 * sb.append("<td>" + studentdetails.getFirstname() + " Disagree</td>");
		 * } if(studentdetails.getFathernamedisagr().equalsIgnoreCase("agree")){
		 * sb.append("<td>" + u1.getFatherName() + " <br> Agree</td>"); }else {
		 * sb.append("<td>" + studentdetails.getFathername() +
		 * " Disagree</td>"); }
		 * if(studentdetails.getMothernamedisagr().equalsIgnoreCase("agree")){
		 * sb.append("<td>" + u1.getMotherName() + "<br> Agree</td>"); }else{
		 * sb.append("<td>" + studentdetails.getMothername() +
		 * " Disagree</td>"); }
		 * 
		 * sb.append("</tr></tbody></table></body></html>");
		 * logger.info("AGREEE VALUE INSITE TRY BLOCK MAIL IF------------------>"
		 * +studentdetails.getMothernamedisagr());
		 * 
		 * 
		 * userList.add(username);
		 * logger.info("USER LIST TESTNG DATA COMMING---------"
		 * +userList.size()); if (!userList.isEmpty()){ logger.info(
		 * "AGREEE VALUE INSITE TRY BLOCK MAIL IF EMPTY------------------>"
		 * +studentdetails.getMothernamedisagr()); String msg =
		 * "Here is your application details"; msg = msg + sb.toString();
		 * logger.info("USER LIST TESTNG---------"+userList.size());
		 * 
		 * result = userService.findEmailByUserName(userList); Map<String,
		 * String> email = result.get("emails");
		 * logger.info("SENDING EMAIL PROCESS MAP---------------->"+email);
		 * logger.info("SENDING EMAIL RESULT---------------->"+result);
		 * Map<String, String> mobiles = new HashMap(); Boolean success =
		 * notifier.sendEmail(email,mobiles,subject,msg);
		 * logger.info("success--------->"+success); }
		 * 
		 * }
		 * 
		 * } catch (Exception e) { logger.error("Exception", e); }
		 */

		if (studentdetails.getMothernamedisagr().equalsIgnoreCase("agree")
				|| studentdetails.getFirstnamedisagr()
						.equalsIgnoreCase("agree")
				|| studentdetails.getFathernamedisagr().equalsIgnoreCase(
						"agree")
				|| studentdetails.getMothernamedisagr().equalsIgnoreCase(
						"disagree")
				|| studentdetails.getFirstnamedisagr().equalsIgnoreCase(
						"disagree")
				|| studentdetails.getFathernamedisagr().equalsIgnoreCase(
						"disagree")) {
			List<User> bf = userService.findUserForMasterEmail(username);
			logger.info("Pending list" + bf);
			if (bf.size() > 0) {
				// notifier
				String email = bf.get(0).getEmail();
				Map<String, String> map = new HashMap();
				Map<String, String> mobile = new HashMap();
				map.put(email, email);
				logger.info("Email" + email);
				String subject = "Submission of sample result";
				StringBuilder sb = new StringBuilder();
				sb.append("<html><body><table style='border: solid;'>");
				sb.append("<thead><tr>");
				sb.append("<th>First Name</th><th>Father Name</th><th>Mother Name</th>");
				sb.append("</tr></thead>");
				sb.append("<tbody><tr>");
				for (User b : bf) {0

					/*
					 * sb.append("<td>" + b.getFirstname() +" "+b.getLastname()+
					 * "</td>"); sb.append("<td>" + b.getFatherName() +
					 * "</td>"); sb.append("<td>" + b.getMotherName() +
					 * "</td>");
					 */
					if (studentdetails.getFirstnamedisagr().equalsIgnoreCase(
							"agree")) {
						sb.append("<td>" + b.getFirstname()
								+ " <br> Agree</td>");
					} else {
						sb.append("<td>" + studentdetails.getFirstname()
								+ " Disagree</td>");
					}
					if (studentdetails.getFathernamedisagr().equalsIgnoreCase(
							"agree")) {
						sb.append("<td>" + b.getFatherName()
								+ " <br> Agree</td>");
					} else {
						sb.append("<td>" + studentdetails.getFathername()
								+ " Disagree</td>");
					}
					if (studentdetails.getMothernamedisagr().equalsIgnoreCase(
							"agree")) {
						sb.append("<td>" + b.getMotherName()
								+ "<br> Agree</td>");
					} else {
						sb.append("<td>" + studentdetails.getMothername()
								+ " Disagree</td>");
					}

				}
				sb.append("</tr></tbody></table></body></html>");
				logger.info("table:" + sb);
				String msg = "Master data submitted entry:";

				msg = msg + sb.toString();
				logger.info("Message:" + msg);

//				Boolean success = notifier.sendEmail(map, mobile, subject, msg);
//				logger.info("success------------>" + success);
			}
		}

		// EMAIL SENDING CODE END HERE

		studentdetails.setUsername(username);
		studentdetails.setActive("Y");
		studentdetails.setCreatedBy(username);
		studentdetails.setLastModifiedBy(username);
		
		
	/*	User userBean = userService.findByUserName(username);
		if(userBean.getUsername()!= null)
		{
			logger.info("Update Existing Data----------->");
			studentDetailConfirmationService.update(studentdetails);
		}else
		{*/
			studentDetailConfirmationService.insert(studentdetails);
		//}

		

		return "redirect:/homepage";
	}
	
	@RequestMapping(value = "/reportForStudentMasterValidationProgramWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String reportForStudentMasterValidationProgramWise(
			Model m, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCampuses", userService.findCampus());
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			m.addAttribute("allPrograms",
					programService.findPrograms());
			m.addAttribute("studentDetailConfirmation", new studentDetailConfirmation());
		}

		return "studentConfirmation/reportForStudentMaster";
	}
	
	@RequestMapping(value = "/generateReportForPhotoSubmittedProgramWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String generateReportForPhotoSubmittedProgramWise(@ModelAttribute studentDetailConfirmation studentDetailConfirmation,
			Model m, Principal principal,HttpServletResponse response) {

		String username = principal.getName();
		logger.info("programId----->"+studentDetailConfirmation.getProgramId());
		List<String> validateHeaders = new ArrayList<String>(Arrays.asList(
				"SapId", "FirstName", "LastName"));

		new ExcelCreater();
		File file = null;
		InputStream is = null;
		try {
			
			WebTarget webTarget3 = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getStudentsPhotoSubmittedProgramwise?programId=" + studentDetailConfirmation.getProgramId()));
			Invocation.Builder invocationBuilder3 = webTarget3.request(MediaType.APPLICATION_JSON);
			String response1 = invocationBuilder3.get(String.class);
			ObjectMapper objMapper = new ObjectMapper();
			logger.info("response---->11" + response1);

			List<studentDetailConfirmation> stdList = objMapper.readValue(response1,
					new TypeReference<List<studentDetailConfirmation>>() {
			});
			List<Map<String, Object>> ListOfvalidastiudent = new ArrayList<>();
			
			String filePath = downloadAllFolder + File.separator
					+ "MasterReportProgramWise.xlsx";

			for (studentDetailConfirmation itm : stdList) {
				Map<String, Object> mapOfQueries = new HashMap<>();
				mapOfQueries.put("SapId", itm.getUsername());
				
				

					mapOfQueries.put("FirstName", itm.getFirstname());
					mapOfQueries.put("LastName", itm.getLastname());

				ListOfvalidastiudent.add(mapOfQueries);

			}
			logger.info("ListOfvalidastiudent----------<"
					+ ListOfvalidastiudent.size());
			ExcelCreater.CreateExcelFile(ListOfvalidastiudent, validateHeaders,
					filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=");

			file = new File(filePath);
			is = new FileInputStream(filePath);
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception ex) {
			logger.error("Exception", ex);
		} finally {
			if (is != null) {
				IOUtils.closeQuietly(is);
				FileUtils.deleteQuietly(file);
			}
		}
		return null;
	}

}
