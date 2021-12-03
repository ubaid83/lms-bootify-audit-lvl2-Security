package com.spts.lms.web.controllers.studentConfirmation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.client.ClientConfig;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.StudentService.BonafideForm;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.PredefinedIcaComponent;
import com.spts.lms.beans.program.AdminProgram;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmationPeriod;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.CourseExcelHelper;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.helpers.excel.StudentMasterExcelHelper;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationPeriodService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.controllers.BaseController;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

import java.io.*;

@Controller
public class studentDetailConfirmationPeriodController extends BaseController {

	@Autowired
	studentDetailConfirmationPeriodService studentDetailConfirmationPeriodService;

	@Autowired
	studentDetailConfirmationService studentDetailConfirmationService;

	@Value("${app}")
	private String app;

	@Autowired
	ApplicationContext act;

	
	@Autowired
	CourseService courseService;

	@Autowired
	ProgramService programService;
	
	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	Notifier notifier;

	@Autowired
	UserService userService;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	Client client = ClientBuilder.newClient();
	private static final Logger logger = Logger
			.getLogger(studentDetailConfirmationPeriodController.class);

	// addStudentDetailConfirmationForm

	@RequestMapping(value = "/createStudentDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createStudentDetails(Model m, Principal principal) {
		String username = principal.getName();
		new studentDetailConfirmationPeriod();

		List<studentDetailConfirmationPeriod> validateDate = studentDetailConfirmationPeriodService
				.validationgStudentEndDate();

		logger.info("Validate ------------------>" + validateDate);

		List<studentDetailConfirmationPeriod> allData = studentDetailConfirmationPeriodService
				.findAllActiveDates(username);
		
		
		List<Course> acadSessionList = courseService.findAcadSessionForStudentMs();
		List<ProgramCampus> campusList = programCampusService.getCampusForSMaster();

		
		// String diableadminbtn=
		// studentDetailConfirmationPeriodService.findAllActiveDatesForDisableBtn();

	

		try {
			ObjectMapper mapper = new ObjectMapper();
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getAdminAssignMappingCourses?username="
							+ username));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			ObjectMapper objMapper = new ObjectMapper();
			List<AdminProgram> AdminProgramList = objMapper.readValue(resp,
					new TypeReference<List<AdminProgram>>() {
					});

			mapper.writeValueAsString(username);

			logger.info("Rest---------------------------->"
					+ AdminProgramList.get(0).getProgramName());
			logger.info("Rest---------------------------->" + resp);
			logger.info("Web Target-------------------->" + webTarget);
			m.addAttribute("AdminProgramList", AdminProgramList);
			m.addAttribute("appName", app);
			m.addAttribute("campusList", campusList);

		} catch (Exception e) {

		}

		LocalDate localdate = LocalDate.now();
		logger.info("LocalDate ------------->" + localdate);

		logger.info("All Dattaa---------------->" + allData);
		// m.addAttribute("disablebtn",diableadminbtn);
		m.addAttribute("allData", allData);
		m.addAttribute("acadSessionList", acadSessionList);

		return "studentConfirmation/addStudentDetailConfirmation";
	}

	@RequestMapping(value = "/downloadStudentDetailConfirmation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadStudentDetailConfirmation(Model m, Principal principal) {
		principal.getName();
		new studentDetailConfirmationPeriod();
		return "studentConfirmation/downloadConfirmationReport";
	}

	@RequestMapping(value = "/createDetailConfirmation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createDetailConfirmation(
			Model m,
			Principal principal,
			@RequestParam("endDate") String endDate,
			@RequestParam(required = false, name = "programId") String programId,
			@RequestParam(required = false, name = "sendEmailAlert") String sendEmailAlert,
			@RequestParam(required = false, name = "acadSession") String acadSession,
			@RequestParam(required = false, name = "campusId") String campusId,
			RedirectAttributes r) throws ParseException {
		String username = principal.getName();
		logger.info("EndDate---------->" + endDate);
		logger.info("programName---------->" + programId);

		logger.info("sendEmailAlert---------->" + sendEmailAlert);
		logger.info("semester---------->" + acadSession);
		
		
		try {
		Utils.validateDate(endDate + " 00:00:00");
		Course checkIfProgramExistsInDB = courseService.checkIfExistsInDB("programId", programId);
		if(checkIfProgramExistsInDB==null) {
			throw new ValidationException("Invalid Program");
		}
		if(sendEmailAlert==null) {
			sendEmailAlert="N";
		}
		BusinessBypassRule.validateYesOrNo(sendEmailAlert);
		if(campusId != null){
			Course checkIfCampusExistsInDB = courseService.checkIfExistsInDB("campusId", campusId);
			if(checkIfCampusExistsInDB==null) {
				throw new ValidationException("Invalid Campus");
			}
		}
		Course checkIfAcadSessionExistsInDB = courseService.checkIfExistsInDB("acadSession", acadSession);
		if(checkIfAcadSessionExistsInDB==null) {
			throw new ValidationException("Invalid Semester");
		}
		
	
		
		//String split comma seperated
		
	
			 String elm = acadSession;
			 String[] elements = elm.split("\\s*[,]\\s*");
			 List<String>  semMailList = Arrays.asList(elements);
		
			 logger.info("semMailList---------->" + semMailList);
			 
		
		// String splitedacadsession = acadSession.replaceAll("^|$", "'").replaceAll(",", "','"); 
		    //first replaceAll, adds ' to start and end and second replace change , to ','
		    
		  
		studentDetailConfirmationPeriod studentdetails = new studentDetailConfirmationPeriod();
		studentdetails.setActive("Y");
		
		if(campusId !=null &&  !campusId.trim().isEmpty())
		{
			studentdetails.setCampusId(campusId);
		}
		
		
		if (("Yes").equalsIgnoreCase(sendEmailAlert)) {
			studentdetails.setSendEmailAlert("Y");
		} else {
			studentdetails.setSendEmailAlert("N");
		}
		studentdetails.setEndDate(endDate);

		String syscurrentdate = Utils
				.formatDate("yyyy-MM-dd", Utils.getInIST());
		logger.info("syscurrentdate-------->" + syscurrentdate);
		// studentDetailConfirmationPeriod compdata =
		// studentDetailConfirmationPeriodService.findByEndDate();

		logger.info("Case 0----------->" + 0);

		if (programId != null) {
			logger.info("Case 1----------->" + 1);

			String maxEndDateForProgram = "";
			maxEndDateForProgram = studentDetailConfirmationPeriodService
					.findProgramidForSchool(username);
			logger.info("maxEndDateForProgram----------->"
					+ maxEndDateForProgram);
			logger.info("Case 2----------->" + 2);
			studentdetails.setProgramId(programId);
			if (null != maxEndDateForProgram) {
				if (maxEndDateForProgram.compareTo(syscurrentdate) < 0) {
					logger.info("Case 3----------->" + 3);

					if (studentdetails.getProgramId().isEmpty()) {
						studentdetails.setProgramId(null);
					}
					studentdetails.setCreatedBy(username);
					studentdetails.setLastModifiedBy(username);
					studentdetails.setAcadSession(acadSession);
					studentDetailConfirmationPeriodService
							.insert(studentdetails);
					setSuccess(r, "added successfully");

				} else {
					logger.info("Case 4----------->" + 4);

					setError(r, " Date Range Already Exist ");

					logger.info("Case 5----------->" + 5);
				}
			} else {

				
				logger.info("Case 5----------->" + 6);
				if (studentdetails.getProgramId().isEmpty()) {
					studentdetails.setProgramId(null);
				}
				studentdetails.setCreatedBy(username);
				studentdetails.setLastModifiedBy(username);
				studentdetails.setAcadSession(acadSession);
				studentDetailConfirmationPeriodService.insert(studentdetails);
				setSuccess(r, "added successfully");

				logger.info("Case 7----------->" + 7);
			}
		} else {
			logger.info("Case 8----------->" + 8);
			String maxDateRange = studentDetailConfirmationPeriodService
					.findMaximumDate();
			logger.info("MaxDateRange-------->" + maxDateRange);
			if (null != maxDateRange) {
			
				if (maxDateRange.compareTo(endDate) < 0) {
					logger.info(maxDateRange.compareTo(endDate) < 0);
					

					logger.info("Case 9----------->" + 9);
					studentdetails.setCreatedBy(username);
					studentdetails.setLastModifiedBy(username);
					studentDetailConfirmationPeriodService
							.insert(studentdetails);
					setSuccess(r, "added successfully");

				}
				else {
					logger.info("Case 10----------->" + 10);

					setError(r, " Date Range Already Exist " + maxDateRange);

				}
			} else {
				logger.info("Case 11----------->" + 11);
			

				studentdetails.setCreatedBy(username);
				studentdetails.setLastModifiedBy(username);
				studentdetails.setAcadSession(acadSession);
				studentDetailConfirmationPeriodService.insert(studentdetails);
				setSuccess(r, "added successfully");

				logger.info("Case 12----------->" + 12);
			}
		}

		logger.info("Email Testing ---------------->1"
				+ studentdetails.getProgramId());

	
		String defaultMsg = "<h3>\\n\\r\\n<span style='color:red'>Note:<span><br> This Announcement is created by : ?? \\r\\n <br>To view any attached files to this mail kindly login to \\r\\nUrl: https://portal.svkm.ac.in/usermgmt/login</h3> ";

		logger.info("Default MSG---->" + defaultMsg);

		
		
		User u1 = userService.findByUserName(username);
		List<String> userList = new ArrayList<String>();
		Map<String, Map<String, String>> result = null;
		
		// Email For Program wise

		 //try was here

			// For Date Wise And Program Wise
		
		

			logger.info("If EndDate And programid is not null---->"
					+ studentdetails.getEndDate() + ""
					+ studentdetails.getProgramId());

			if ("Y".equals(studentdetails.getSendEmailAlert())) {
				Program p = programService.findByID(Long.valueOf(studentdetails
						.getProgramId()));

				String subject = "New Announcement:"
						+ studentdetails.getDescription() + "  for Program "
						+ p.getProgramName() + " End Date "
						+ studentdetails.getEndDate();
				StringBuilder sb = new StringBuilder();
				sb.append("<html><body>");
				sb.append("<h3 style='border-style: groove;'>Kindly Fill the Master Data Validation:</h3>");
				sb.append("<h3>This Announcement is created by :"
						+ u1.getFirstname() + " " + u1.getLastname() + "</h3>");
				sb.append("<ul style='padding:10px'><u style='font-weight: bold;'>Photograph Specification:</u><br><li>Height 25 mm and Width 20 mm (file size not to exceed 150 kb).</li>"
						+ "<li>A very clear colour image.</li>"
						+ "<li>Taken within the last 6 months to reflect current appearance.</li>"
						+ "<li>Taken in front of a plain white or off-white background.</li>"
						+ "<li>Taken in full-face view directly facing the camera (No side view please).</li>"
						+ "<li>With a neutral facial expression and both eyes open.</li>"
						+ "<li>Taken in formal wear (avoid T shirts etc.)</li>"
						+ "<li>Photo taken in a closed environment (not in open area).</li>"
						+ "<li>Do not wear a hat or anything that covers head, unless worn daily for religious purpose.</li>"
						+ "Your seeing this screen to enable your new photo.Please note that the photograph which will be uploaded on degree certificate"
						+ "</ul>");
				sb.append("<h3 style='border-style: groove;'>To view any attached files to this mail kindly login to Url:https://portal.svkm.ac.in/usermgmt/login</h3>");

				List<User> userEmailList = userService
						.findUserByProgramIdForMasterEmail(studentdetails
								.getProgramId(), semMailList,campusId);
				logger.info("programList--------------------->"
						+ userEmailList.size());

				for (User u : userEmailList) {
					userList.add(u.getUsername());
					if (!userList.isEmpty()) {

						sb.append("</body></html>");
						String msg = "";
						msg = msg + sb.toString();
						logger.info("USER LIST TESTNG---------"
								+ userList.size());
						if ("Y".equals(studentdetails.getSendEmailAlert())) {

							result = userService.findEmailByUserName(userList);
							Map<String, String> email = result.get("emails");
							logger.info("SENDING EMAIL PROCESS---------------->"
									+ email);
							Map<String, String> mobiles = new HashMap();
							notifier.sendEmail(email, mobiles, subject, msg);
						}
					}
				}
			}

		}
		catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(r, ve.getMessage());
			return "redirect:/createStudentDetails";
			
		}
		
		catch (Exception e) {
			logger.error("Exception", e);
		}

		// END EMAIL ALERT
		// logger.info("Comdate --------->" + compdata);

		return "redirect:/createStudentDetails";
	}

	@RequestMapping(value = "/viewStudentDetailConfirmation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewStudentDetailConfirmation(Model m, Principal principal) {
		principal.getName();
		new studentDetailConfirmationPeriod();
		return "studentConfirmation/downloadConfirmationReport";
	}

	@RequestMapping(value = "deleteStudentDetailConfirmation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteStudentDetailConfirmation(Model m,
			@RequestParam String id, Principal p, RedirectAttributes r) {
		try {
			new studentDetailConfirmationPeriod();
			setSuccess(r, "Deleted successfully");
			logger.info("Delete End ------------>" + id);
			studentDetailConfirmationPeriodService.inactiveEndDate(id);

		} catch (Exception e) {
			setError(r, "Error while deleting.");
		}
		return "redirect:/createStudentDetails";
	}

	@RequestMapping(value = "/downloadMasterReport", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadMasterReport(Model m, Principal p,
			HttpServletResponse response) throws URIException {
		List<studentDetailConfirmation> stdList = new ArrayList<>();
		String username = p.getName();
		stdList = studentDetailConfirmationService.findStatusListForAll(username);
		
		List<String> fornad = new ArrayList<>();
		fornad = studentDetailConfirmationService.findAllActiveStudent();

		logger.info("fornad------------------>"+fornad);
		
		List<String> validateHeaders = new ArrayList<String>(Arrays.asList(
				"SapId", "FirstName", "FatherName", "MotherName", "NAD", "Address"));

		new ExcelCreater();
		File file = null;
		InputStream is = null;
		try {
			String json = new Gson().toJson(fornad);
			logger.info("size"+fornad.size()+"ffor"+fornad);
			logger.info("json------------>" + json);

			// Code For Adding Nad In download Excel

			WebTarget webTarget3 = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getNADListForMaster?json=" + json));
			Invocation.Builder invocationBuilder = webTarget3.request();
			Response response1 = invocationBuilder.post(Entity.entity(json,
					MediaType.APPLICATION_JSON));
			ObjectMapper objMapper = new ObjectMapper();
			logger.info("response---->11" + response1);

			String resp = response1.readEntity(String.class);
			logger.info("resp---->11" + resp);
			List<studentDetailConfirmation> getNad = objMapper.readValue(resp,
					new TypeReference<List<studentDetailConfirmation>>() {
					});
		

			Map<String, Object> mapOfQueries1 = new HashMap<>();

			for (studentDetailConfirmation std : getNad) {

						mapOfQueries1.put(std.getUsername(), std.getNad());
						
			}

			List<Map<String, Object>> ListOfvalidastiudent = new ArrayList<>();
			
			String filePath = downloadAllFolder + File.separator
					+ "MasterReport.xlsx";

			for (studentDetailConfirmation itm : stdList) {
				logger.info("ITM=======>" + itm.getNad() +""+mapOfQueries1.get(itm.getNad()));
				Map<String, Object> mapOfQueries = new HashMap<>();
				mapOfQueries.put("SapId", itm.getUsername());
				
				mapOfQueries.put("NAD", mapOfQueries1.get(itm.getUsername()));
				

				if (itm.getFirstname() == null || itm.getFirstname().isEmpty()
						|| itm.getFirstname().equals("")) {
					mapOfQueries.put("FirstName", "Agree");
				} else {
					mapOfQueries.put("FirstName", itm.getFirstname());
				}
				if (itm.getFathername() == null
						|| itm.getFathername().isEmpty()
						|| itm.getFathername().equals("")) {
					mapOfQueries.put("FatherName", "Agree");
				} else {
					mapOfQueries.put("FatherName", itm.getFathername());
				}

				if (itm.getMothername() == null
						|| itm.getMothername().isEmpty()
						|| itm.getMothername().equals("")) {
					mapOfQueries.put("MotherName", "Agree");
				} else {
					mapOfQueries.put("MotherName", itm.getMothername());
				}
				mapOfQueries.put("Address", itm.getAddress());
				/*
				 * if(itm.getNad()==null || itm.getNad().isEmpty() ||
				 * itm.getNad().equals("")) { mapOfQueries.put("NAD",
				 * itm.getNad()); } else { mapOfQueries.put("NAD",
				 * itm.getNad()); }
				 */

				ListOfvalidastiudent.add(mapOfQueries);

			}
			logger.info("ListOfvalidastiudent----------<"
					+ ListOfvalidastiudent);
			ExcelCreater.CreateExcelFile(ListOfvalidastiudent, validateHeaders,
					filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=");

			file = new File(filePath);
			is = new FileInputStream(filePath);
			logger.info("ISIS1------->" + file);
			logger.info("ISIS------->" + is);
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

	// Upload Excel File

	private StudentMasterExcelHelper getStudentMasterExcelHelper() {
		return (StudentMasterExcelHelper) act
				.getBean("StudentMasterExcelHelper");
	}

	@RequestMapping(value = "/uploadMasterStatusReport", method = RequestMethod.POST)
	public String uploadMasterStatusReport(
			@ModelAttribute studentDetailConfirmation student,
			Principal principal,
			@RequestParam("excelFileRead") MultipartFile file,
			RedirectAttributes redirectAttrs, Model m) {

		StudentMasterExcelHelper StudentMasterExcelHelper = getStudentMasterExcelHelper();

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		userdetails1.getProgramName();
		userService.findByUserName(username);

		try {
			if (!file.isEmpty()) {
				StudentMasterExcelHelper.initHelper(student);
				StudentMasterExcelHelper.readExcel(file);

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs,
					"Error in uploading File: " + e.getMessage());
			return "redirect:/createStudentDetails";
		}
		setSuccess(redirectAttrs, "uploaded Successfully");
		return "redirect:/createStudentDetails";

	}

	/*
	 * @RequestMapping(value = "/downloadMasterSampleTemplet", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * downloadMasterSampleTemplet(Model m, Principal p, HttpServletResponse
	 * response) throws URIException { Token userdetails1 = (Token) p;
	 * List<studentDetailConfirmation> stdList = new ArrayList<>(); stdList =
	 * studentDetailConfirmationService.findStatusListForAll();
	 * logger.info("Data------>" + stdList); List<String> validateHeaders = new
	 * ArrayList<String>(Arrays.asList("UserName", "Status")); ExcelCreater
	 * excelCreater = new ExcelCreater(); File file = null; InputStream is =
	 * null; try { List<Map<String, Object>> ListOfvalidastiudent = new
	 * ArrayList<>(); XSSFWorkbook workbook = new XSSFWorkbook(); XSSFSheet
	 * sheet = workbook.createSheet("Student Master Report"); String filePath =
	 * downloadAllFolder + File.separator + "MasterSamppReport.xlsx";
	 * logger.info("stdList------------->" + stdList); for
	 * (studentDetailConfirmation itm : stdList) { logger.info("ITM=======>" +
	 * itm); Map<String, Object> mapOfQueries = new HashMap<>();
	 * mapOfQueries.put("UserName", itm.getUsername());
	 * mapOfQueries.put("Status", itm.getStatus());
	 * ListOfvalidastiudent.add(mapOfQueries); }
	 * ExcelCreater.CreateExcelFile(ListOfvalidastiudent,
	 * validateHeaders,filePath); XSSFDataValidationHelper dvHelper = new
	 * XSSFDataValidationHelper(sheet); response.setContentType(
	 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	 * response.setHeader("Content-Disposition", "attachment; filename="); file
	 * = new File(filePath); is = new FileInputStream(filePath);
	 * logger.info("ISIS1------->" + file); logger.info("ISIS------->" + is);
	 * org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	 * response.flushBuffer(); response.getOutputStream().flush();
	 * response.getOutputStream().close(); } catch (Exception ex) {
	 * logger.error("Exception", ex); } finally { if (is != null) {
	 * IOUtils.closeQuietly(is); FileUtils.deleteQuietly(file); } } return null;
	 * }
	 */

	// Download Sample Template for Master Validation

	@RequestMapping(value = "/downloadMasterSampleTemplet", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void downloadMasterSampleTemplet(HttpServletResponse response,
			Model m, Principal p) {
		InputStream is = null;
		String filePath = "";
		try {
			
			String username = p.getName();

			List<studentDetailConfirmation> stdList = new ArrayList<>();
			stdList = studentDetailConfirmationService
					.findStatusListForAllStudent(username);

			filePath = getMasterTemplateSample(stdList);

			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			// copy it to response's OutputStream
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			response.flushBuffer();
		} catch (Exception ex) {
			logger.info(
					"Error writing file to output stream. Filename was '{}'",
					ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}
			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	public String getMasterTemplateSample(List<studentDetailConfirmation> bList) {

		logger.info("BLIST------------------>" + bList);

		String filePath = downloadAllFolder + File.separator
				+ "downloadMasterSampleTemplet.xlsx";
		String h[] = { "SapId", "Status", "Remarks" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook
				.createSheet("Student Master Validation Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;
		for (studentDetailConfirmation bf : bList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(bf.getUsername());
			row.createCell(1).setCellValue(bf.getStatus());
			row.createCell(2).setCellValue("");
			rowNum++;
		}
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		String[] actions = new String[] { "Y,N" };
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(actions);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 1,
				1);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper
				.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		try {
			logger.info("filePathtry--->" + filePath);

			System.out.println(System.getProperty("dir"));
			FileOutputStream outputStream = new FileOutputStream(filePath);

			workbook.write(outputStream);

			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;

	}

	// Student Master Support Admin working code

	/*
	 * @RequestMapping(value = "/StudentMasterSupportAdmin", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * StudentMasterSupportAdmin(Model m, Principal principal) { String username
	 * = principal.getName(); try { ObjectMapper mapper = new ObjectMapper();
	 * WebTarget webTarget = client.target(URIUtil
	 * .encodeQuery(userRoleMgmtCrudUrl +
	 * "/getRegisterdStudentForMasterValidation")); Invocation.Builder
	 * invocationBuilder = webTarget .request(MediaType.APPLICATION_JSON);
	 * String resp = invocationBuilder.get(String.class); ObjectMapper objMapper
	 * = new ObjectMapper(); List<studentDetailConfirmation>
	 * StudentMastreStudentList = objMapper .readValue( resp, new
	 * TypeReference<List<studentDetailConfirmation>>() { });
	 * 
	 * mapper.writeValueAsString(username);
	 * 
	 * logger.info("Rest Student Side---------------------------->" +
	 * StudentMastreStudentList);
	 * logger.info("Rest StudentSide---------------------------->" + resp);
	 * logger.info("Web Target-------------------->" + webTarget);
	 * m.addAttribute("StudentMastreStudentList", StudentMastreStudentList); }
	 * catch (Exception e) { } return "studentConfirmation/supportadmin"; }
	 */

	// Till Here

	/*
	 * @RequestMapping(value = "/addSchoolAbbrForMaster", method =
	 * RequestMethod.POST) public String addSchoolAbbrForMaster( Model m,
	 * 
	 * @RequestParam(name = "schoolabbr", required = true) String schoolabbr) {
	 * 
	 * System.err.println("Mapping Data Recieving form Page ---------------->" +
	 * schoolabbr); m.addAttribute("schoolabbr", schoolabbr); return
	 * "studentConfirmation/supportadmin"; }
	 */

	// view student photo mapping admin side

	
	//Lms Admin Student View Mapping view 
	
/*	@RequestMapping(value = "/viewstudents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewstudents(Model m, Principal principal) {
		String username = principal.getName();
		new studentDetailConfirmationPeriod();
		// Student Detail lms
		try {
			ObjectMapper mapper = new ObjectMapper();
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl+ "/getRegisterdStudentForMasterValidation"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			ObjectMapper objMapper = new ObjectMapper();
			List<studentDetailConfirmation> StudentMastreStudentList = objMapper
				.readValue(resp,new TypeReference<List<studentDetailConfirmation>>() {});
			mapper.writeValueAsString(username);
			logger.info("Rest Student Side---------------------------->"
					+ StudentMastreStudentList);
			logger.info("Rest StudentSide---------------------------->" + resp);
			logger.info("Web Target-------------------->" + webTarget);
			m.addAttribute("StudentMastreStudentList", StudentMastreStudentList);
		} catch (Exception e) {
			logger.info("Error while getting response ---->" + e.getMessage());
		}
		return "studentConfirmation/viewstudents";
	}
*/
	
	
	// Student Master Support Admin working code

	@RequestMapping(value = "/StudentMasterSupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String StudentMasterSupportAdmin(Model m, Principal principal) {
	
		try {
			/*
			 * ObjectMapper mapper = new ObjectMapper(); WebTarget webTarget = client
			 * .target(URIUtil.encodeQuery(userRoleMgmtCrudUrl +
			 * "/getRegisterdStudentForMasterValidation")); Invocation.Builder
			 * invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON); String
			 * resp = invocationBuilder.get(String.class); ObjectMapper objMapper = new
			 * ObjectMapper(); List<studentDetailConfirmation> StudentMastreStudentList =
			 * objMapper.readValue(resp, new
			 * TypeReference<List<studentDetailConfirmation>>() { });
			 */
			

			List<studentDetailConfirmationPeriod> activeprogramList = studentDetailConfirmationPeriodService
					.fingAllActiveProgramWithSemester();
			
			List<studentDetailConfirmation> studentList = studentDetailConfirmationService.findActiveStudentForSupportAdmin();
			

			logger.info("Rest Student Side---------------------------->" + activeprogramList);
	
			m.addAttribute("activeprogramList", activeprogramList);
			m.addAttribute("studentList", studentList);
		} catch (Exception e) {
		}
		return "studentConfirmation/supportadmin";
	}
	
	
	@RequestMapping(value = "/studentMasterNamechange", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteIca(Model m, Principal principal, @RequestParam(name = "username") String username,
			RedirectAttributes redirectAttrs) {
		try {
				logger.info("id-------------------->"+username);

		if(username!=null)
		{
			studentDetailConfirmationService.softDeleteById(username);
		}
		setSuccess(redirectAttrs, "Status Chnage Sucessully,Student able to update information");
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting");
			logger.error("Exception", ex);
		}

		return "redirect:/StudentMasterSupportAdmin";
	}
	
	@RequestMapping(value = "/studentMasterReuploadPhoto", method = RequestMethod.GET)
	public String studentMasterReuploadPhoto(Model m, Principal principal, @RequestParam(name = "username") String username,
			RedirectAttributes redirectAttrs) {
		try {
				logger.info("username-------------------->"+username);

				
				if (username != null) {
	
					
					WebTarget webTarget3 = client.target(URIUtil
							.encodeQuery(userRoleMgmtCrudUrl
									+ "/studentMasterReuploadPhotoUsermgmt?username=" + username));
					Invocation.Builder invocationBuilder3 = webTarget3
							.request(MediaType.APPLICATION_JSON);
					String resp3 = invocationBuilder3.get(String.class);
					
					logger.info("resp3resp3-------------------->"+resp3);
				}
				 
		setSuccess(redirectAttrs, "Status Chnage Sucessully,Student re-upload photo");
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting");
			logger.error("Exception", ex);
		}

		return "redirect:/StudentMasterSupportAdmin";
	}
	
	
	
	
	
	
	
	
/*	@RequestMapping(value = "/getacadSessionByProgramId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseBySemester(
			@RequestParam(name = "masterProgramId") String masterProgramId,
			Principal principal) {
		String json = "";
		
		List<studentDetailConfirmationPeriod> courseList = null;
		logger.info("masterProgramId------------ " + masterProgramId);
		
				courseList = studentDetailConfirmationPeriodService.getAcadSessionByProgramId(masterProgramId);

			List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		
		if(courseList.size()>0){
		for (studentDetailConfirmationPeriod ass : courseList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(ass.getAcadSession(),ass.getAcadSession()+"-"+ass.getAcadYear());
			res.add(returnMap);
		}
		}else{
			Map<String, String> returnMap = new HashMap();
			returnMap.put("NA","No Acad Session Available");
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		logger.info("json" + json);
		return json;

	}*/
	
	
	
	@RequestMapping(value = "/getacadSessionByProgramId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseBySemester(
			@RequestParam(name = "masterProgramId") String masterProgramId,
			Principal principal) {
		String json = "";
		
		List<studentDetailConfirmationPeriod> courseList = null;
		logger.info("masterProgramId------------ " + masterProgramId);
		
		Map<String,String> map = new HashMap<>();
		map.put("2017", "2017-18");
		map.put("2018", "2018-19");
		map.put("2019", "2019-20");
		map.put("2020", "2020-21");
		map.put("2021", "2021-22");
		map.put("2022", "2022-23");
		map.put("2023", "2023-24");

			courseList = studentDetailConfirmationPeriodService.getAcadSessionByProgramId(masterProgramId);

		

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		
		if(courseList.size()>0){
		for (studentDetailConfirmationPeriod ass : courseList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(ass.getAcadSession(),ass.getAcadSession()+"("+map.get(ass.getAcadYear())+")");
			res.add(returnMap);
		}
		}else{
			Map<String, String> returnMap = new HashMap();
			returnMap.put("NA","No Acad Session Available");
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		logger.info("json" + json);
		return json;

	}
	
	
	
	
	
	
	
	
	
	
}
