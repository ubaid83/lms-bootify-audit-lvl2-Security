package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.Tika;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.Status;
import com.spts.lms.beans.StudentService.LorRegDetails;
import com.spts.lms.beans.StudentService.LorRegStaff;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.beans.user.UserTo;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.StudentService.LorRegDetailsService;
import com.spts.lms.services.StudentService.LorRegStaffService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

@Controller
public class LorController extends BaseController {

	@Value("${lorFolder}")
	private String lorFolderPath;

	@Value("${lorAdminFolderPath}")
	private String lorAdminFolderPath;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${lorStudentFolderPath}")
	private String lorStudentFolderPath;

	@Autowired
	UserService userService;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	LorRegDetailsService lorRegDetailsService;

	@Autowired
	LorRegStaffService lorRegStaffService;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Autowired
	ProgramService programService;

	@Value("${app}")
	private String app;

	@Autowired
	Notifier notifier;
	
	@Autowired
	BusinessBypassRule businessBypassRule;
	
	@Autowired
	Utils Utils;

	Client client = ClientBuilder.newClient();
	private static final Logger logger = Logger.getLogger(LorController.class);

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF" })
	@RequestMapping(value = "/viewAppliedApplicationStudentsForStaff", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewAppliedApplicationStudentsForStaff(Model m, Principal principal) {
		String username = principal.getName();

		List<LorRegDetails> lorRegDetailsList = lorRegDetailsService.getPendingAppicationForApproval(username);
		logger.info("lorRegDetailsList--->" + lorRegDetailsList);

		Map<String, String> programMap = new HashMap<String, String>();
		List<Program> programList = programService.findPrograms();
		for (Program p : programList) {
			programMap.put(String.valueOf(p.getId()), p.getProgramName());
		}
		LorRegStaff lorRegStaff = new LorRegStaff();
		m.addAttribute("lorRegStaff", lorRegStaff);
		m.addAttribute("lorRegDetailsList", lorRegDetailsList);
		m.addAttribute("programMap", programMap);
		return "lor/viewLorStudentsApplicationForStaff";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF" })
	@RequestMapping(value = "/uploadLORFormatFile", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadLORFormatFile(@RequestParam long id, @RequestParam("file") List<MultipartFile> files, Model m,
			Principal principal, RedirectAttributes ra) {
		String staffUsername = principal.getName();
		Token userdetails = (Token) principal;
		try {
			String errorMessage = "";
			String filepath = "";

			if (files.size() <= 0) {
				setError(ra, "No File selected.");
				return "redirect:/viewAppliedApplicationStudentsForStaff";
			}
			for (MultipartFile file : files) {
				if (null == file || file.isEmpty()) {
					setError(ra, "Selected File is empty!");
					return "redirect:/lorApplicationForm";
				}
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(ra, "File uploaded is invalid!");
						return "redirect:/viewAppliedApplicationStudentsForStaff";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(ra, "File uploaded is invalid!");
							return "redirect:/viewAppliedApplicationStudentsForStaff";
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
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B)) {
							errorMessage = uploadFileForS3(file, userdetails);
							} else {
								setError(ra, "File uploaded is invalid!");
								return "redirect:/viewAppliedApplicationStudentsForStaff";
							}
						}
					}
				}else {
					setError(ra, "File uploaded is invalid!");
					return "redirect:/viewAppliedApplicationStudentsForStaff";
				}
				
				if (!errorMessage.contains("Error in uploading file")) {
					if (filepath.isEmpty() || filepath == null) {
						filepath = errorMessage;
					} else {
						filepath = filepath + "," + errorMessage;
					}
				} else {
					setError(ra, "Error in uploading file");
					return "redirect:/viewAppliedApplicationStudentsForStaff";
				}
			}
			LorRegStaff lorRegStaff = new LorRegStaff();
			if (!errorMessage.contains("Error in uploading file")) {
				lorRegStaff.setLorFormatFilePath(filepath);
				lorRegStaff.setId(id);
				lorRegStaffService.saveLorFormatFilePath(lorRegStaff);
				lorRegStaff = lorRegStaffService.findByID(Long.valueOf(id));
				lorRegStaff = lorRegStaffService.findByID(Long.valueOf(id));

				LorRegDetails 	LorRegDetails =lorRegDetailsService.findByID(lorRegStaff.getLorRegId());			
				LorRegStaff deptAssistent = lorRegStaffService.getDepartmentAssistent(lorRegStaff.getDepartment());
				User user=userService.findByUserName(deptAssistent.getUsername());
				User userfaculty=userService.findByUserName(lorRegStaff.getStaffId());
			 
				String subject = "LOR APPLICATION";
				
				
				User ss= new User();
				
//				LorRegDetails 	LorRegDetails =lorRegDetailsService.findByID(lorRegStaff.getLorRegId());	
//				User userfaculty=userService.findByUserName(staffUsername);
//				
				
				
				
				
				/*changes for email 16-09-2021
				 * String notificationEmailMessage = "<html><body>" +
				 * "<b>LOR APPLICATION </b><br>" + "<br>  LOR Format uploaded by faculty " +
				 * facultyData.getFirstname() + facultyData.getLastname() + ".<br>" +
				 * "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
				 * + "This is auto-generated email, do not reply on this.</body></html>";
				 */
				logger.info("new---1"+userfaculty.getFirstname());
				StringBuffer notificationEmailMessage = new StringBuffer(" ");
				
				
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b><br> <br>  LOR Format uploaded by faculty  ");
				notificationEmailMessage.append(userfaculty.getFirstname());
				notificationEmailMessage.append("  ");
				notificationEmailMessage.append( userfaculty.getLastname());
				notificationEmailMessage.append( " of Department ");
				
				notificationEmailMessage.append(lorRegStaff.getDepartment());
				notificationEmailMessage.append( " for Student ");
				notificationEmailMessage.append( LorRegDetails.getName());
				notificationEmailMessage.append( " SAP ID ");
				notificationEmailMessage.append( LorRegDetails.getUsername());
				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");

				
				
				Map<String, Map<String, String>> result = null;
				
				Map<String, String> email = new HashMap();
				email.put(user.getUsername(), user.getEmail());

				Map<String, String> mobiles = new HashMap();
				
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
				
				
				setSuccess(ra, "LOR format uploaded.");
			}
		} catch (Exception e) {
			logger.error("Error---->" + e);
			setError(ra, "Error in uploading file");
		}
		return "redirect:/viewAppliedApplicationStudentsForStaff";
	}

	@Secured({ "ROLE_INTL","ROLE_STAFF" })
	@RequestMapping(value = "/uploadLORFinalFile", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadLORFinalFile(@RequestParam Long id, @RequestParam("file") List<MultipartFile> files, Model m,
			Principal principal, RedirectAttributes ra) {
		String userIntl = principal.getName();
		Token userdetails = (Token) principal;
		try {
			String errorMessage = "";
			String filepath = "";

			if (files.size() <= 0) {
				setError(ra, "No File selected.");
//				return "redirect:/viewAppliedApplicationStudentsForStaff";
				if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
					return "redirect:/viewAppliedApplicationStudentsForDepartment";
				}else {
					return "redirect:/viewAppliedApplicationStudentsForAdmin";
				}
			}
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					logger.info("file---->");
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(ra, "File uploaded is invalid!");
							if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
								return "redirect:/viewAppliedApplicationStudentsForDepartment";
							}else {
								return "redirect:/viewAppliedApplicationStudentsForAdmin";
							}
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(ra, "File uploaded is invalid!");
								if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
									return "redirect:/viewAppliedApplicationStudentsForDepartment";
								}else {
									return "redirect:/viewAppliedApplicationStudentsForAdmin";
								}
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
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B)) {
								errorMessage = uploadFileForS3(file, userdetails);
								} else {
									setError(ra, "File uploaded is invalid!");
									if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
										return "redirect:/viewAppliedApplicationStudentsForDepartment";
									}else {
										return "redirect:/viewAppliedApplicationStudentsForAdmin";
									}
								}
							}
						}
					}else {
						setError(ra, "File uploaded is invalid!");
						if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
							return "redirect:/viewAppliedApplicationStudentsForDepartment";
						}else {
							return "redirect:/viewAppliedApplicationStudentsForAdmin";
						}
					}
					
					if (!errorMessage.contains("Error in uploading file")) {
						if (filepath.isEmpty() || filepath == null) {
							filepath = errorMessage;
						} else {
							filepath = filepath + "," + errorMessage;
						}
					} else {
						setError(ra, "Error in uploading file");
//						return "redirect:/viewAppliedApplicationStudentsForStaff";
						if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
							return "redirect:/viewAppliedApplicationStudentsForDepartment";
						}else {
							return "redirect:/viewAppliedApplicationStudentsForAdmin";
						}
					}
				}
			}
			logger.info("errorMessage=--------" + errorMessage);
			LorRegStaff lorRegStaff = new LorRegStaff();
			lorRegStaff.setFinalLorFilePath(filepath);
			lorRegStaff.setLorApproval(null);
			lorRegStaff.setLorRejectionReason(null);
			lorRegStaff.setId(id);
			lorRegStaff.setFinalLorUploadedBy(userIntl);
			lorRegStaff.setLastModifiedBy(userIntl);
			lorRegStaffService.updateLORDocumentStatus(lorRegStaff);
			setSuccess(ra, "LOR format uploaded.");
			LorRegStaff lorRegStaffDB = lorRegStaffService.findByID(id);
			User user = userService.findByUserName(userIntl);
			User userStaff = userService.findByUserNameLike(lorRegStaffDB.getStaffId());
			LorRegDetails 	LorRegDetails =lorRegDetailsService.findByID(lorRegStaffDB.getLorRegId());
			
			String subject = " LOR APPLICATION ";
			
			StringBuffer notificationEmailMessage = new StringBuffer("");
			
			notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b><br> <br> FINAL LOR  uploaded by Department assistant  ");
			notificationEmailMessage.append(user.getFirstname());
			notificationEmailMessage.append(" ");
			notificationEmailMessage.append( user.getLastname());
			notificationEmailMessage.append( "for LOR application of  department ");
			notificationEmailMessage.append(lorRegStaffDB.getDepartment());
			notificationEmailMessage.append( "for Student ");
			notificationEmailMessage.append( LorRegDetails.getName());
			notificationEmailMessage.append( "SAP ID ");
			notificationEmailMessage.append( LorRegDetails.getUsername());

			notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
			
			
			
			
			
//			String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//					+ "<br> Final LOR uploaded by " + user.getFirstname() + " " + user.getLastname()
//					+ " for LOR application of department " + lorRegStaffDB.getDepartment() + " And Faculty/Staff "
//					+ userStaff.getFirstname() + " " + userStaff.getLastname() + ".<br>"
//					+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//					+ "This is auto-generated email, do not reply on this.</body></html>";

			Map<String, Map<String, String>> result = null;
			// result = userService.findEmailByUserName(userList);
			Map<String, String> email = new HashMap();
			email.put(userStaff.getUsername(), userStaff.getEmail());

			Map<String, String> mobiles = new HashMap();
			// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
			notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());

		} catch (Exception e) {
			logger.error("Error---->" + e);
			setError(ra, "Error in uploading file");
		}
//		return "redirect:/viewAppliedApplicationStudentsForAdmin";
		if(userdetails.getAuthorities().contains(Role.ROLE_STAFF)) {
			return "redirect:/viewAppliedApplicationStudentsForDepartment";
		}else {
			return "redirect:/viewAppliedApplicationStudentsForAdmin";
		}
	}

	private String uploadFileForS3(MultipartFile file,Token userdetails) {

		String errorMessage = null;
		String filePath = null;

		try {
			Principal p;

//			UserRole u = userRoleService.findRoleByUsername(staffUsername);
//			logger.info("u-------" + u.getRole());
//			if (u.getRole().toString().contains("ROLE_STAFF") || u.getRole().toString().contains("ROLE_FACULTY")) {
//				filePath = lorFolderPath;
//			} else if (u.getRole().toString().contains(("ROLE_INTL"))) {
//				filePath = lorAdminFolderPath;
//			}
			if (userdetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				filePath = lorFolderPath;
			} else if (userdetails.getAuthorities().contains(Role.ROLE_STAFF) || userdetails.getAuthorities().contains(Role.ROLE_INTL)) {
				filePath = lorAdminFolderPath;
			}
			logger.info("filePath---" + filePath);

			Map<String, String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file,
					filePath);
			logger.info("map--->" + s3FileNameMap);
			if (s3FileNameMap.containsKey("SUCCESS")) {
				String s3FileName = s3FileNameMap.get("SUCCESS");
				filePath = filePath + "/" + s3FileName;
				errorMessage = filePath;
			} else {
				throw new Exception("Error in uploading file");
			}
		} catch (Exception e) {
			errorMessage = "Error in uploading file : " + e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF" })
	@RequestMapping(value = "/saveExpectedDate", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveExpectedDate(@RequestParam String expectedDate, @RequestParam String id,
			Principal principal) {
		String username = principal.getName();
		try {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = dateFormat.parse(expectedDate);
			LorRegStaff lorRegStaff = new LorRegStaff();
			lorRegStaff.setExpectedDate(date);
			lorRegStaff.setLastModifiedBy(username);
			lorRegStaff.setId(Long.valueOf(id));
			logger.info("LorRegStaff--->" + lorRegStaff);
			lorRegStaffService.saveExpectedDate(lorRegStaff);
			return "{\"status\": \"success\", \"msg\": \"Date updated!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in updating Date!\"}";
		}
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF", "ROLE_INTL" })
	@RequestMapping(value = "/getStudentLorDetailsByLorRegId", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getStudentLorDetailsByLorRegId(@RequestParam String lorRegId, Principal principal) {
		logger.info("lorRegId--->" + lorRegId);
		String username = principal.getName();

		LorRegDetails lorRegDetails = lorRegDetailsService.getLorDetailsById(lorRegId, username);
		lorRegDetails.setProgramName(programService.getProgramName(lorRegDetails.getProgramEnrolledId()));
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(lorRegDetails);
			logger.info("json---->" + json);
		} catch (Exception e) {
			logger.error("Exception--->", e);
			return "{\"status\": \"error\", \"msg\": \"Error in updating Date!\"}";
		}
		return json;
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF", "ROLE_INTL", "ROLE_STUDENT" })
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "/downloadLorFiles", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadLorFiles(
			@RequestParam(required = false, name = "id", defaultValue = "") String id,
			@RequestParam(required = false, name = "fileType", defaultValue = "") String fileType,
			@RequestParam(required = false, name = "staffId", defaultValue = "") String staffId,
			HttpServletRequest request, HttpServletResponse response, Principal p) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		String projectUrl = "";
		String username = p.getName();

		Token userDetails = (Token) p;
		logger.info("userDetails-------" + userDetails.getAuthorities());

		String filePath = "";
		try {

			if (!StringUtils.isEmpty(id)) {
				LorRegStaff lorRegStaff = new LorRegStaff();
				lorRegStaff = lorRegStaffService.findByID(Long.valueOf(id));
				logger.info("lorRegStaff--->" + lorRegStaff);
				LorRegDetails lorRegDetails = new LorRegDetails();
				lorRegDetails = lorRegDetailsService.findByID(lorRegStaff.getLorRegId());
				if (!StringUtils.isEmpty(fileType)) {
					if (fileType.equals("lorDocsFilePath")) {
						filePath = lorRegStaff.getLorDocsFilePath();
					} else if (fileType.equals("examMarksheet")) {
						filePath = lorRegDetails.getExamMarksheet();
					} else if (fileType.equals("toeflOrIeltsMarksheet")) {
						filePath = lorRegDetails.getToeflOrIeltsMarksheet();
					} else if (fileType.equals("lorFormatFilePath")) {
						filePath = lorRegStaff.getLorFormatFilePath();
					} else if (fileType.equals("finalLorFilePath")) {
						filePath = lorRegStaff.getFinalLorFilePath();
					}
				}
			}
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			if (filePath.contains(",")) {

				File folderPath = new File(workDir + File.separator + "lorAllFiles");
				List<String> files = Arrays.asList(filePath.split(","));
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}

				for (String file : files) {
					if (file.startsWith("/")) {
						file = StringUtils.substring(file, 1);
					}
					file = file.replace("/\\", "/");
					file = file.replace("\\\\", "/");
					file = file.replace("\\", "/");
					file = file.replace("//", "/");
					File fileNew = new File(file);
					logger.info(folderPath.getAbsolutePath());
					logger.info("asignQuesFile---->" + file);
					InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
					File dest = new File(folderPath.getAbsolutePath() + File.separator + fileNew.getName());
					FileUtils.copyInputStreamToFile(inpStream, dest);

				}
				String filename = "Lorfile.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
				projectUrl = "/" + "workDir" + "/" + folderPath.getName() + ".zip";
				pack(folderPath.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				if (StringUtils.isEmpty(filePath)) {
					request.setAttribute("error", "true");
					request.setAttribute("errorMessage", "Error in downloading file.");
				}
				File downloadFile = new File(filePath);
				String path = "";
				if (filePath.startsWith("/")) {
					path = StringUtils.substring(filePath, 1);
				} else {
					path = filePath;
				}
				path = path.replace("/\\", "/");
				path = path.replace("\\\\", "/");
				path = path.replace("\\", "/");
				path = path.replace("//", "/");
				byte[] data = amazonS3ClientService.getFile(path);
				logger.info("data" + data.length);
				ByteArrayResource resource = new ByteArrayResource(data);

				return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
						.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
						.body(resource);

			}

		} catch (Exception e) {
			logger.error("Exception", e);
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage", "Error in downloading file.");
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	public static void pack(String sourceDirPath, ServletOutputStream out) throws IOException {

		try (ZipOutputStream zs = new ZipOutputStream(new BufferedOutputStream(out))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
				try {
					zs.putNextEntry(zipEntry);
					zs.write(Files.readAllBytes(path));
					zs.closeEntry();
				} catch (Exception e) {
					logger.error("Ëxception", e);
				}
			});
		}
	}

	@RequestMapping(value = "/getfacultyByDept", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getfacultyByDept(Model m, @RequestParam String department) throws JSONException {
		// logger.info("department1----------------" + department);
		JSONArray jsonarray = new JSONArray();
		List<LorRegStaff> facultyList = lorRegStaffService.getfaculty(department);

		for (LorRegStaff p : facultyList) {
			if(!p.getUsername().contains("STAFF")) {
				JSONObject obj = new JSONObject();
				obj.put("value", p.getUsername());
				obj.put("text", p.getName());
				jsonarray.put(obj);
			}
		}
		return jsonarray.toString();
	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/uploadStudentDocuments", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentDocuments(@RequestParam Long lorRegId, @RequestParam Long id,
			@RequestParam String staffId, @RequestParam("file") List<MultipartFile> files, Model m, Principal principal,
			RedirectAttributes redirect) {

		String username = principal.getName();
		try {
			String errorMessage = "";
			String filepath = "";
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirect, "File uploaded is invalid!");
							return"redirect:/viewLor";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(redirect, "File uploaded is invalid!");
								return"redirect:/viewLor";
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
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B)) {
								errorMessage = uploadStudentFileForS3(file);
								if (!errorMessage.contains("Error in uploading file")) {
									if (filepath.isEmpty() || filepath == null) {
										filepath = errorMessage;
									} else {
										filepath = filepath + "," + errorMessage;
									}
								} else {
									setError(redirect, "Error in uploading file");
									return "redirect:/viewLor";
								}
								logger.info("filepath-----"+filepath);
								} else {
									setError(redirect, "File uploaded is invalid!");
									return"redirect:/viewLor";
								}
							}
						}
					}else {
						setError(redirect, "File uploaded is invalid!");
						return"redirect:/viewLor";
					}
					
					
				}
			}
			LorRegStaff lorRegStaff = new LorRegStaff();
			// logger.info("lorRegId-----"+lorRegId+" id---->"+id+" staffId--->"+staffId);
			if (!errorMessage.contains("Error in uploading file")) {
				logger.info("errorMessage" + errorMessage);
				lorRegStaff.setLorDocsFilePath(filepath);
				lorRegStaff.setDocApproval(null);
				lorRegStaff.setDocRejectionReason(null);
				lorRegStaff.setId(id);
				lorRegStaff.setLastModifiedBy(username);
				lorRegStaffService.updateStudentDocumentStatus(lorRegStaff);
				LorRegStaff lorRegStaffDB = lorRegStaffService.findByID(id);
				setSuccess(redirect, "Your Documents Submitted successfully");

				User user = userService.findByUserName(username);
				User userStaff = userService.findByUserNameLike(lorRegStaffDB.getStaffId());
				String subject = " LOR APPLICATION ";
				
				StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				notificationEmailMessage.append(user.getFirstname());
				notificationEmailMessage.append(" ");
				notificationEmailMessage.append( user.getLastname());
				notificationEmailMessage.append(" (Sap Id");
				notificationEmailMessage.append(user.getUsername());
				notificationEmailMessage.append(" )");
				notificationEmailMessage.append("  has uploaded documents for LOR application of department  ");

				notificationEmailMessage.append(lorRegStaffDB.getDepartment());
				notificationEmailMessage.append( " And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());
				notificationEmailMessage.append( " ");
				notificationEmailMessage.append( userStaff.getLastname());

				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
				
				
				
				
				
				
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>" + "<br> Student "
//						+ user.getFirstname() + " " + user.getLastname() + " ( SAPID: " + username + ")"
//						+ " has uploaded documents for LOR application of department " + lorRegStaffDB.getDepartment()
//						+ " And Faculty/Staff " + userStaff.getFirstname() + " " + userStaff.getLastname() + ".<br>"
//						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";

				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(userStaff.getUsername(), userStaff.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
				return "redirect:/viewLorApplication";
			}
		} catch (Exception e) {
			logger.error("Error---->" + e);
			setError(redirect, "Error in uploading file");
			return "redirect:/viewLor";
		}

		setSuccess(redirect, "Your Documents Submitted successfully");
		return "redirect:/viewLor";

	}

	private String uploadStudentFileForS3(MultipartFile file) {

		String errorMessage = null;
		String filePath = null;

		try {
			filePath = lorStudentFolderPath;
			Map<String, String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file,
					filePath);
			logger.info("map--->" + s3FileNameMap);
			if (s3FileNameMap.containsKey("SUCCESS")) {
				String s3FileName = s3FileNameMap.get("SUCCESS");
				filePath = filePath + "/" + s3FileName;
				errorMessage = filePath;
			} else {
				throw new Exception("Error in uploading file");
			}
		} catch (Exception e) {
			errorMessage = "Error in uploading file : " + e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}

	@Secured({ "ROLE_INTL" })
	@RequestMapping(value = "/viewAppliedApplicationStudentsForAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewAppliedApplicationStudentsForAdmin(Model m, Principal principal) {
		String username = principal.getName();

		List<LorRegStaff> lorApplicationList = lorRegStaffService.getPendingAppicationForApprovalByAdmin(username);
		logger.info("lorApplicationList--->" + lorApplicationList);
		LorRegStaff lorRegStaff = new LorRegStaff();
		m.addAttribute("lorRegStaff", lorRegStaff);
		m.addAttribute("lorApplicationList", lorApplicationList);
		// m.addAttribute("programMap", programMap);
		return "lor/viewLorStudentsApplicationForAdmin";
	}

	// Hiren
	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/lorApplicationForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String lorApplicationForm(@ModelAttribute LorRegDetails lorRegDetails,
			@ModelAttribute LorRegStaff lorRegStaff, Model m, Principal principal) {
            System.out.println("INSIDE LOR APPLICATION>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");	
         String username = principal.getName();

		User u = userService.findByUserName(username);
		m.addAttribute("userdetails", u);

		List<String> deptlist = lorRegStaffService.getAlldept();
		m.addAttribute("deptlist", deptlist);

		Program programName = programService.findByID(u.getProgramId());
		m.addAttribute("programName", programName);

		m.addAttribute("lorRegDetails", new LorRegDetails());
		m.addAttribute("lorRegStaff", new LorRegStaff());
		return "lor/lorApplicationForm";

	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/saveLorApplicationDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveLorApplicationDetails(Model m, @ModelAttribute LorRegDetails lorRegDetails,
			@ModelAttribute LorRegStaff lorRegStaff, @RequestParam(name = "file1") MultipartFile examMarksheet,
			@RequestParam(name = "file2") MultipartFile toeflOrIeltsMarksheet, RedirectAttributes redirect,
			Principal principal) {
		 String username = principal.getName();
		try {
			
			logger.info("lorRegDetails--->" + lorRegDetails);
			logger.info("lorRegStaff--->" + lorRegStaff);
		    
			if(lorRegDetails.getUsername() == null ||lorRegDetails.getUsername().isEmpty() )
		    {
				 throw new ValidationException("Input field cannot be empty");	
			}
	    	//  BusinessBypassRule.validateNumeric(lorRegDetails.getUsername());
	    	//  BusinessBypassRule.validateString(lorRegDetails.getName());
	    	//  BusinessBypassRule.validateEmail(lorRegDetails.getEmail());
	    	//  BusinessBypassRule.validateNumeric(lorRegDetails.getMobile());
			
			 User u = userService.findByUserName(username);
			 
			 System.out.println("Users>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:U:"+u);
			 lorRegDetails.setUsername(username);
			 lorRegDetails.setEmail(u.getEmail());
			 lorRegDetails.setMobile(u.getMobile());
			 lorRegDetails.setName(u.getFirstname()+" "+u.getLastname());
		      BusinessBypassRule.validateNumeric(lorRegDetails.getProgramEnrolledId());
		      
	    	  BusinessBypassRule.validateString(lorRegDetails.getCountryForHigherStudy());
	    	  BusinessBypassRule.validateString(lorRegDetails.getUniversityName());
	    	  System.out.println("Program :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+lorRegDetails.getProgramToEnroll());
	          BusinessBypassRule.validateAlphaNumeric(lorRegDetails.getProgramToEnroll());
	          
	          System.out.println("Tentative Date Of Joining :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+lorRegDetails.getTentativeDOJ());
	          Utils.validateDate(lorRegDetails.getTentativeDOJ());
	          BusinessBypassRule.validateYesOrNo(lorRegDetails.getIsNmimsPartnerUniversity());
	    	  
	    	
			List<String> userList = new ArrayList<String>();
			String uname = principal.getName();    //rename username to uname 
			String errorMessage = "";
			String examMarksheetFilePath = "";
			String toeflOrIeltsMarksheetFilePath = "";
			if (!lorRegDetails.getCompetitiveExam().equals("Not Taken")) {
				if (null == examMarksheet || examMarksheet.isEmpty()) {
					setError(redirect, "Selected File is empty!");
					return "redirect:/lorApplicationForm";
				}

				errorMessage = uploadStudentFileForS3(examMarksheet); // returns file path or error message
				if (!errorMessage.contains("Error in uploading file")) {
					if (examMarksheetFilePath.isEmpty() || examMarksheetFilePath == null) {
						examMarksheetFilePath = errorMessage;
					} else {
						examMarksheetFilePath = examMarksheetFilePath + "," + errorMessage;
					}
				} else {
					setError(redirect, "Error in uploading file");
					return "redirect:/viewAppliedApplicationStudentsForStaff";
				}
			}
			if (!lorRegDetails.getToeflOrIelts().equals("Not Taken")) {
				if (null == toeflOrIeltsMarksheet || toeflOrIeltsMarksheet.isEmpty()) {
					setError(redirect, "Selected File is empty!");
					return "redirect:/lorApplicationForm";
				}

				errorMessage = uploadStudentFileForS3(toeflOrIeltsMarksheet); // returns file path or error message
				if (!errorMessage.contains("Error in uploading file")) {
					if (toeflOrIeltsMarksheetFilePath.isEmpty() || toeflOrIeltsMarksheetFilePath == null) {
						toeflOrIeltsMarksheetFilePath = errorMessage;
					} else {
						toeflOrIeltsMarksheetFilePath = toeflOrIeltsMarksheetFilePath + "," + errorMessage;
					}
				} else {
					setError(redirect, "Error in uploading file");
					return "redirect:/viewAppliedApplicationStudentsForStaff";
				}
			}
         
			lorRegDetails.setExamMarksheet(examMarksheetFilePath);
			lorRegDetails.setToeflOrIeltsMarksheet(toeflOrIeltsMarksheetFilePath);
			lorRegDetails.setCreatedBy(uname);
			lorRegDetails.setLastModifiedBy(uname);//here 
            System.out.println("LORREGISTRATIONDETAILS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+lorRegDetails);
			lorRegDetailsService.insertWithIdReturn(lorRegDetails);
			Long lorId = lorRegDetails.getId();
			String[] staffIds = lorRegStaff.getStaffId().split(",");
			List<String> staffId = Arrays.asList(staffIds);
			for (int i = 0; i <= staffId.size() - 1; i++) {
				LorRegStaff lorRegStaffs = new LorRegStaff();
				String staffIdStr = "";
				if (!staffId.get(i).split("-")[0].endsWith("_STAFF")) {
					staffIdStr = staffId.get(i).split("-")[0] + "_STAFF";
				} else {
					staffIdStr = staffId.get(i).split("-")[0];
				}
				lorRegStaffs.setNoOfCopies(staffId.get(i).split("-")[1]);
				lorRegStaffs.setUsername(lorRegStaff.getUsername());
				lorRegStaffs.setDepartment(lorRegStaff.getDepartment());
				lorRegStaffs.setLorRegId(lorId);
				lorRegStaffs.setStaffId(staffIdStr);
				lorRegStaffs.setCreatedBy(uname);
				lorRegStaffs.setLastModifiedBy(uname);
				lorRegStaffService.insert(lorRegStaffs);//here
				userList.add(staffIdStr.replace("_STAFF", ""));
			}
		//	User user = userService.findByUserName(username);
			User user = userService.findByUserName(uname);
			String subject = " LOR APPLICATION ";
			StringBuffer notificationEmailMessage = new StringBuffer("");

			notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>    The Student ");
			notificationEmailMessage.append(user.getFirstname());
			notificationEmailMessage.append(" ");
			notificationEmailMessage.append( user.getLastname());
			notificationEmailMessage.append(" (Sap Id");
			notificationEmailMessage.append(user.getUsername());
			notificationEmailMessage.append(" )");
			notificationEmailMessage.append("  has applied for LOR application of department  ");

			notificationEmailMessage.append(lorRegStaff.getDepartment());
			notificationEmailMessage.append( ".");
			

			notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
			
			
//			String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>" + "<br> The Student "
//					+ u.getFirstname() + " " + u.getLastname() + " ( SAPID: " + username + ")"
//					+ " has applied for LOR.<br>"
//					+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//					+ "This is auto-generated email, do not reply on this.</body></html>";
			for (String s : userList) {
				User userStaff = userService.findByUserNameLike(s);
				if (null != userStaff) {
					Map<String, Map<String, String>> result = null;
					// result = userService.findEmailByUserName(userList);
					Map<String, String> email = new HashMap();
					email.put(userStaff.getUsername(), userStaff.getEmail());

					Map<String, String> mobiles = new HashMap();
					// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				    notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
				}
			}

		} 
		catch (ValidationException e) {
			logger.error(e);
			setError(redirect,e.getMessage() );
			return "redirect:/lorApplicationForm";
		}
		
		catch (Exception e) {
			logger.error(e);
			setError(redirect, "Error in Submitting form");
			return "redirect:/lorApplicationForm";
		}
		setSuccess(redirect, "Your application Submitted successfully");
		setNote(redirect, "Wait for faculty approval and further instruction to complete the Lor application.");
		return "redirect:/viewLorApplication";

	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/viewLorApplication", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewLorApplication(Model m, Principal principal) {

		String username = principal.getName();

		User u = userService.findByUserName(username);

		List<LorRegStaff> lorRegStaff = lorRegStaffService.getAllApplicationByStudent(username);
		m.addAttribute("lorRegStaff", lorRegStaff);
		return "lor/viewLor";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF" })
	@RequestMapping(value = "/saveApplicationApprovalStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveApplicationApprovalStatus(@RequestParam String json, Principal principal) {
		String username = principal.getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			LorRegStaff lorRegStaff = mapper.readValue(json, new TypeReference<LorRegStaff>() {
			});
			LorRegStaff lorRegStaffDB = lorRegStaffService.findByID(lorRegStaff.getId());
			lorRegStaffDB.setAppApproval(lorRegStaff.getAppApproval());
			lorRegStaffDB.setAppRejectionReason(lorRegStaff.getAppRejectionReason());
			lorRegStaffDB.setLastModifiedBy(username);
			logger.info("LorRegStaff--->" + lorRegStaff);
			lorRegStaffService.saveApplicationApprovalStatus(lorRegStaffDB);
			if (lorRegStaffDB.getAppApproval().equals("Approve")) {
				User user = userService.findByUserName(lorRegStaffDB.getUsername());
				User userStaff = userService.findByUserNameLike(username);
				String subject = " LOR APPLICATION ";
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//						+ "<br> Your LOR application is <b>Approved</b> of department " + lorRegStaffDB.getDepartment()
//						+ " And Faculty/Staff " + userStaff.getFirstname() + " " + userStaff.getLastname() + ".<br>"
//						+ " Now You Can Upload Your Documents.<br>"
//						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";

				StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				
				notificationEmailMessage.append("  Your LOR application is <b>Approved</b> of department  ");

				notificationEmailMessage.append(lorRegStaffDB.getDepartment());
				notificationEmailMessage.append( "And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());
				notificationEmailMessage.append( " ");
				notificationEmailMessage.append( userStaff.getLastname());

				notificationEmailMessage.append( ".<br>");
				notificationEmailMessage.append( "Now You Can Upload Your Documents.");


				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
				
				
				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(user.getUsername(), user.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
			} else if (lorRegStaffDB.getAppApproval().equals("Reject")) {
				User user = userService.findByUserName(lorRegStaffDB.getUsername());
				User userStaff = userService.findByUserNameLike(username);
				String subject = " LOR APPLICATION ";
				
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//						+ "<br> Your LOR application is <b>Rejected</b> of department " + lorRegStaffDB.getDepartment()
//						+ " And Faculty/Staff " + userStaff.getFirstname() + " " + userStaff.getLastname() + ".<br>"
//						+ " Reason: " + lorRegStaffDB.getAppRejectionReason() + "<br>"
//						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";
				
				
				StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				
				notificationEmailMessage.append("  Your LOR application is <b>Rejected</b> of department  ");

				notificationEmailMessage.append(lorRegStaff.getDepartment());
				notificationEmailMessage.append( "  And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());

				notificationEmailMessage.append( userStaff.getLastname());
				notificationEmailMessage.append( " .<br>");
				notificationEmailMessage.append( " Rejected reason ");
				notificationEmailMessage.append(lorRegStaffDB.getAppRejectionReason());
				notificationEmailMessage.append( ".<br>");



				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
				
				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(user.getUsername(), user.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
			}
			return "{\"status\": \"success\", \"msg\": \"Application status updated successfully!\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in updating Application status!\"}";
		}
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF" })
	@RequestMapping(value = "/saveDocumentApprovalStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveDocumentApprovalStatus(@RequestParam String json, Principal principal) {
		String username = principal.getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			LorRegStaff lorRegStaff = mapper.readValue(json, new TypeReference<LorRegStaff>() {
			});
			LorRegStaff lorRegStaffDB = lorRegStaffService.findByID(lorRegStaff.getId());
			lorRegStaffDB.setDocApproval(lorRegStaff.getDocApproval());
			lorRegStaffDB.setDocRejectionReason(lorRegStaff.getDocRejectionReason());
			lorRegStaffDB.setLastModifiedBy(username);
			if (lorRegStaffDB.getDocApproval().equals("Reject")) {
				lorRegStaffDB.setLorDocsFilePath(null);
				lorRegStaffService.updateStudentDocumentStatus(lorRegStaffDB);
			} else {
				lorRegStaffService.updateStudentDocumentStatus(lorRegStaffDB);
			}
			if (lorRegStaffDB.getDocApproval().equals("Approve")) {
				User user = userService.findByUserName(lorRegStaffDB.getUsername());
				User userStaff = userService.findByUserNameLike(username);
				String subject = " LOR APPLICATION ";
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//						+ "<br> Your Uploaded Documents of LOR application is <b>Approved</b> of department "
//						+ lorRegStaffDB.getDepartment() + " And Faculty/Staff " + userStaff.getFirstname() + " "
//						+ userStaff.getLastname() + ".<br>"
//						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";
				
				StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				
				notificationEmailMessage.append("  Your Uploaded Documents of LOR application is <b>Approved</b> of department  ");
			
				notificationEmailMessage.append(lorRegStaffDB.getDepartment());
				notificationEmailMessage.append( "And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());
			
				notificationEmailMessage.append( userStaff.getLastname());
				notificationEmailMessage.append( " .<br>");
						
			
				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
				

				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(user.getUsername(), user.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
			} else if (lorRegStaffDB.getDocApproval().equals("Reject")) {
				User user = userService.findByUserName(lorRegStaffDB.getUsername());
				User userStaff = userService.findByUserNameLike(username);
				String subject = " LOR APPLICATION ";
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//						+ "<br> Your Uploaded Documents of LOR application is <b>Rejected</b> of department "
//						+ lorRegStaffDB.getDepartment() + " And Faculty/Staff " + userStaff.getFirstname() + " "
//						+ userStaff.getLastname() + ".<br>" + " Reason: " + lorRegStaffDB.getDocRejectionReason()
//						+ "<br>" + "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";
				
				
	StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				
				notificationEmailMessage.append("  Your Uploaded Documents of LOR application is <b>Rejected</b> of department  ");
			
				
				notificationEmailMessage.append(lorRegStaffDB.getDepartment());
				notificationEmailMessage.append( "And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());
			
				notificationEmailMessage.append( userStaff.getLastname());
				notificationEmailMessage.append( " .<br>");
				notificationEmailMessage.append( " Rejected reason ");
				notificationEmailMessage.append(lorRegStaffDB.getDocRejectionReason());
				notificationEmailMessage.append( ".<br>");
				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
			


				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(user.getUsername(), user.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
			}
			return "{\"status\": \"success\", \"msg\": \"Document status updated successfully!\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in updating Document Status!\"}";
		}
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STAFF" })
	@RequestMapping(value = "/saveLorApprovalStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String saveLorApprovalStatus(@RequestParam String json, Principal principal) {
		String username = principal.getName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			LorRegStaff lorRegStaff = mapper.readValue(json, new TypeReference<LorRegStaff>() {
			});
			LorRegStaff lorRegStaffDB = lorRegStaffService.findByID(lorRegStaff.getId());
			lorRegStaffDB.setLorApproval(lorRegStaff.getLorApproval());
			lorRegStaffDB.setLorRejectionReason(lorRegStaff.getLorRejectionReason());
			lorRegStaffDB.setLastModifiedBy(username);
			logger.info("LorRegStaff--->" + lorRegStaff);
			if (lorRegStaffDB.getLorApproval().equals("Reject")) {
				lorRegStaffDB.setFinalLorFilePath(null);
				lorRegStaffService.updateLORDocumentStatus(lorRegStaffDB);
			} else {
				lorRegStaffService.updateLORDocumentStatus(lorRegStaffDB);
			}
			if (lorRegStaffDB.getLorApproval().equals("Approve")) {
				User user = userService.findByUserName(lorRegStaffDB.getUsername());
				User userStaff = userService.findByUserNameLike(username);
				String subject = " LOR APPLICATION ";
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//						+ "<br> Your LOR application is ready and now downloadable of department "
//						+ lorRegStaffDB.getDepartment() + " And Faculty/Staff " + userStaff.getFirstname() + " "
//						+ userStaff.getLastname() + ".<br>"
//						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";
				
				StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				
				notificationEmailMessage.append("  Your LOR application is ready and now downloadable of department  ");
			
				notificationEmailMessage.append(lorRegStaffDB.getDepartment());
				notificationEmailMessage.append( "And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());
			
				notificationEmailMessage.append( userStaff.getLastname());
				notificationEmailMessage.append( " .<br>");

				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
			
				

				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(user.getUsername(), user.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
			} else if (lorRegStaffDB.getLorApproval().equals("Reject")) {
				User user = userService.findByUserName(lorRegStaffDB.getUsername());
				User userStaff = userService.findByUserNameLike(username);
				User userIntl = userService.findByUserName(lorRegStaffDB.getFinalLorUploadedBy());
				String subject = " LOR APPLICATION ";
//				String notificationEmailMessage = "<html><body>" + "<b>LOR APPLICATION </b><br>"
//						+ "<br> Your uploaded LOR Rejected of Student " + user.getFirstname() + " " + user.getLastname()
//						+ " from department " + lorRegStaffDB.getDepartment() + " And Faculty/Staff "
//						+ userStaff.getFirstname() + " " + userStaff.getLastname() + ".<br>"
//						+ " You need to upload it again.<br>" + " Reason: " + lorRegStaffDB.getLorRejectionReason()
//						+ "<br>" + "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//						+ "This is auto-generated email, do not reply on this.</body></html>";
				
				StringBuffer notificationEmailMessage = new StringBuffer("");
				
				notificationEmailMessage.append("<html><body> <b>LOR APPLICATION </b> <br>   ");
				
				notificationEmailMessage.append("  Your uploaded LOR Rejected of Student   ");
			
				notificationEmailMessage.append( user.getFirstname());
				notificationEmailMessage.append(" ");
				notificationEmailMessage.append( user.getLastname());
				notificationEmailMessage.append(" from department ");
				notificationEmailMessage.append(lorRegStaffDB.getDepartment());
				notificationEmailMessage.append( "And Faculty/Staff");
				notificationEmailMessage.append( userStaff.getFirstname());
			
				notificationEmailMessage.append( userStaff.getLastname());
				notificationEmailMessage.append( " .<br>");
				notificationEmailMessage.append( " You need to upload it again.<br>Reject Reason");
				notificationEmailMessage.append(lorRegStaffDB.getLorRejectionReason());

				notificationEmailMessage.append(".<br> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br> This is auto-generated email, do not reply on this.</body></html>");
				

				Map<String, Map<String, String>> result = null;
				// result = userService.findEmailByUserName(userList);
				Map<String, String> email = new HashMap();
				email.put(userIntl.getUsername(), userIntl.getEmail());

				Map<String, String> mobiles = new HashMap();
				// logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage.toString());
			}
			return "{\"status\": \"success\", \"msg\": \"LOR approval updated successfully!\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in updating LOR Status!\"}";
		}
	}

	@RequestMapping(value = "/AddLorUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String AddLorUser(Model m, Long id, Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		return "lor/AddUsersForLorForm";

	}

	@RequestMapping(value = "/downloadUserUploadtemplate", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadStudentAssignmentThresholdReport(HttpServletRequest request, HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;

		try {

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Lor Users");

			Row r = sheet.createRow(0);

			r.createCell(0).setCellValue("UserName");
			r.createCell(1).setCellValue("Department");
			r.createCell(2).setCellValue("Role");

			int i = 1;

			Row row = sheet.createRow(i);
			row.createCell(0).setCellValue("01234567");
			row.createCell(1).setCellValue("Department");
			row.createCell(2).setCellValue("ROLE_INTL");

			XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
			String[] actions = new String[] { "ROLE_STAFF,ROLE_INTL" };
			XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
					.createExplicitListConstraint(actions);
			CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 2, 2);
			XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
			validation.setShowErrorBox(true);
			sheet.addValidationData(validation);

			// String folderPath = baseDir + "/" + app + "/" + "evaluateAssignmentTemp";
			String folderPath = downloadAllFolder;
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			String filePath = folderP.getAbsolutePath() + File.separator + "upload_Users_For_Lor.xlsx";
			File f = new File(filePath);
			if (!f.exists()) {
				FileOutputStream fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();
			} else {
				FileUtils.deleteQuietly(f);
				FileOutputStream fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();
				logger.info("File already exist");
			}

			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);

			if (!downloadFile.exists()) {
				return "Error: Dowmload file is not found";
			}
			inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			logger.info("MIME type: " + mimeType);

			// set content attributes for the response
			/* response.setContentType(mimeType); */
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);

		}
		return null;

	}

	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = "/uploadusersForLor", method = { RequestMethod.POST })
	public String uploadusersForLor(@RequestParam("file") MultipartFile file, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		try {
			String username = principal.getName();

			List<String> validateHeaders = new ArrayList<String>(Arrays.asList("UserName", "Department", "Role"));

			logger.info("headers are--->" + validateHeaders);
			ExcelReader excelReader = new ExcelReader();

			// List<Map<String, Object>> maps =
			// excelReader.readIcaExcelFileUsingColumnHeader(file, headers);
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);
			logger.info("maps received-----" + maps);

			List<Map<String, Object>> copy = new ArrayList<>();
			for (Map<String, Object> mapper : maps) {
				logger.info(mapper);
				if (mapper.get("Error") != null) {

					logger.info("elas" + mapper.get("Error"));
					setError(redirectAttributes, "Error--->" + mapper.get("Error"));

					return "redirect:/AddLorUser";

				} else {

				}
			}

			for (Map<String, Object> mappers : maps) {

				LorRegStaff sa = new LorRegStaff();

				sa.setUsername(mappers.get("UserName").toString());
				sa.setDepartment(mappers.get("Department").toString());
				sa.setRole(mappers.get("Role").toString());

				UserRole userRoles = userRoleService.findRoleByUsername(mappers.get("UserName").toString());

				System.out.println(null != userRoles);
				/* START */
				if (null != userRoles) {

					if (userRoles.getRole().equals("ROLE_FACULTY")
							&& mappers.get("Role").toString().equals("ROLE_STAFF")) {

						lorRegStaffService.AddLorDepartMentFaculty(sa);

					} else {

						List<User> userList = new ArrayList<User>();
						String level2 = null;
						User user2 = new User();

						logger.info("get mapper .get useranme" + mappers.get("UserName").toString());

						user2.setUsername(mappers.get("UserName").toString());

						if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
							user2.setRole("ROLE_STAFF");
						} else if (mappers.get("Role").toString().equals("ROLE_INTL")) {
							user2.setRole("ROLE_INTL");
						}

						user2.setSchoolAbbr(app);
						userList.add(user2);

						List<UserRole> userRoleList = new ArrayList<UserRole>();
						ObjectMapper mapper = new ObjectMapper();

						if (userList.size() != 0) {
							for (User uzr : userList) {
								logger.info("uzr--------->" + uzr);
								String json = mapper.writeValueAsString(uzr);

								logger.info(" json----------------------->" + (json));
								WebTarget webTarget = client.target(URIUtil
										.encodeQuery(userRoleMgmtCrudUrl + "/addOtherUserStaffService?json=" + json));
								Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
								String resp = invocationBuilder.get(String.class);

								logger.info("response--------->" + resp);
								ObjectMapper objMapper = new ObjectMapper();
								Status status = objMapper.readValue(resp, Status.class);
								logger.info("Status---->" + status.getStatus());
								if (status.getStatus().SUCCESS != null) {

									lorRegStaffService.AddLorDepartMentFaculty(sa);
									UserTo userTo;

									User user = new User();
									User newUser = userService.findByUserName(uzr.getUsername());
									if (newUser != null) {

										user = newUser;

										if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
											user.setUsername(uzr.getUsername() + "_STAFF");
										} else {
											user.setUsername(uzr.getUsername() + "_INTL");
										}
									} else {

										if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
											user.setUsername(uzr.getUsername() + "_STAFF");
										} else {
											user.setUsername(uzr.getUsername() + "_INTL");
										}

										if (status.getUser() != null) {

											userTo = status.getUser();
											user.convert(userTo);

										}
										user.setPassword(
												"d97086919b6522e13ba9b46c04902c38372102218a4b3ef2f45ac2a80e9fd240");
										user.setCreatedBy("CA");
										user.setLastModifiedBy("CA");
										user.setEnabled(true);
										user.setAttempts(0);
										user.setAcadSession(null);
										user.setRollNo(null);
										user.setCampusId(null);
										user.setCampusName(null);
									}
									UserRole userRole = new UserRole();
									userRole.setUsername(user.getUsername());
									if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
										userRole.setRole(Role.ROLE_STAFF);
									} else {
										userRole.setRole(Role.ROLE_INTL);
									}

									userRole.setCreatedBy("CA");
									userRole.setLastModifiedBy("CA");
									userRole.setUser(user);

									UserRole userRole1 = new UserRole();
									userRole1.setUsername(user.getUsername());
									userRole1.setRole(Role.ROLE_USER);
									userRole1.setCreatedBy("CA");
									userRole1.setLastModifiedBy("CA");
									userRole1.setUser(user);
									userRoleList.add(userRole);
									userRoleList.add(userRole1);

									userService.upsert(user);
									userRoleService.upsertBatch(userRoleList);

								}
							}
						}
					}

				}

				else {

					List<User> userList = new ArrayList<User>();
					String level2 = null;
					User user2 = new User();

					logger.info("get mapper .get useranme" + mappers.get("UserName").toString());

					user2.setUsername(mappers.get("UserName").toString());
					System.out.println("----------------" + mappers.get("Role").toString());
					if (mappers.get("Role").toString().equals("ROLE_STAFF")) {

						user2.setRole("ROLE_STAFF");
					} else if (mappers.get("Role").toString().equals("ROLE_INTL")) {
						user2.setRole("ROLE_INTL");
					}

					user2.setSchoolAbbr(app);
					userList.add(user2);

					List<UserRole> userRoleList = new ArrayList<UserRole>();
					ObjectMapper mapper = new ObjectMapper();

					if (userList.size() != 0) {
						for (User uzr : userList) {
							logger.info("uzr--------->" + uzr);
							String json = mapper.writeValueAsString(uzr);

							logger.info(" json----------------------->" + (json));
							WebTarget webTarget = client.target(URIUtil
									.encodeQuery(userRoleMgmtCrudUrl + "/addOtherUserStaffService?json=" + json));
							Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
							String resp = invocationBuilder.get(String.class);

							logger.info("response--------->" + resp);
							ObjectMapper objMapper = new ObjectMapper();
							Status status = objMapper.readValue(resp, Status.class);
							logger.info("Status---->" + status.getStatus());
							if (status.getStatus().SUCCESS != null) {

								UserTo userTo;

								User user = new User();
								User newUser = userService.findByUserName(uzr.getUsername());
								if (newUser != null) {

									user = newUser;

									if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
										user.setUsername(uzr.getUsername() + "_STAFF");
									} else {
										user.setUsername(uzr.getUsername() + "_INTL");
									}
								} else {

									if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
										user.setUsername(uzr.getUsername() + "_STAFF");
									} else {
										user.setUsername(uzr.getUsername() + "_INTL");
									}

									if (status.getUser() != null) {

										userTo = status.getUser();
										user.convert(userTo);

									}
									user.setPassword("d97086919b6522e13ba9b46c04902c38372102218a4b3ef2f45ac2a80e9fd240");
									user.setCreatedBy("CA");
									user.setLastModifiedBy("CA");
									user.setEnabled(true);
									user.setAttempts(0);
									user.setAcadSession(null);
									user.setRollNo(null);
									user.setCampusId(null);
									user.setCampusName(null);
								}
								UserRole userRole = new UserRole();
//							userRole.setUsername(user.getUsername());
								if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
									userRole.setUsername(user.getUsername() + "_STAFF");
									userRole.setRole(Role.ROLE_STAFF);
								} else {
									userRole.setRole(Role.ROLE_INTL);
									userRole.setUsername(user.getUsername() + "_INTL");
								}

								userRole.setCreatedBy("CA");
								userRole.setLastModifiedBy("CA");
								userRole.setUser(user);

								UserRole userRole1 = new UserRole();
								userRole1.setUsername(user.getUsername());
								userRole1.setRole(Role.ROLE_USER);
								userRole1.setCreatedBy("CA");
								userRole1.setLastModifiedBy("CA");
								userRole1.setUser(user);
								userRoleList.add(userRole);
								userRoleList.add(userRole1);

								userService.upsert(user);
								userRoleService.upsertBatch(userRoleList);
								if (mappers.get("Role").toString().equals("ROLE_STAFF")) {
									sa.setUsername(sa.getUsername() + "_STAFF");
									lorRegStaffService.AddLorDepartMentFaculty(sa);
								}

							}
						}
					}

				}

			}

		} catch (Exception ex) {

			setError(redirectAttributes, "Error in Adding Users");

			logger.error("Exception", ex);

		}
		setSuccess(redirectAttributes, "User Added Successfully ");

		return "redirect:/AddLorUser";

	}

	@Secured({"ROLE_STAFF"})
	@RequestMapping(value = "/viewAppliedApplicationStudentsForDepartment", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewAppliedApplicationStudentsForDepartment(Model m, Principal principal) {
		String username = principal.getName();

		List<LorRegStaff> lorApplicationList = lorRegStaffService.getPendingAppicationForApprovalByDepartment(username);
		logger.info("lorApplicationList--->" + lorApplicationList);
		LorRegStaff lorRegStaff = new LorRegStaff();
		m.addAttribute("lorRegStaff", lorRegStaff);
		m.addAttribute("lorApplicationList", lorApplicationList);
		// m.addAttribute("programMap", programMap);
		return "lor/viewLorStudentsApplicationForDepartment";
	}
	
}
