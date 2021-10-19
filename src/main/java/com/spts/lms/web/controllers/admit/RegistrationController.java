package com.spts.lms.web.controllers.admit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
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
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.glassfish.jersey.client.ClientConfig;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.user.FacultyDetails;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.TrainingProgram;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.helpers.excel.FacultyExcelHelper;
import com.spts.lms.helpers.excel.StudentExcelHelper;
/*import com.spts.lms.sap.profile.ZCHANGESTMOBILEEMAILWSSEP;
import com.spts.lms.sap.profile.ZmessageLogLt;
import com.spts.lms.sap.profile.ZmessageLogTt;*/
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.user.FacultyDetailsService;
import com.spts.lms.services.user.TrainingProgramService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.user.WsdlLogService;
import com.spts.lms.studentms.sap.ZCHANGESTMOBILEEMAILWSD;
import com.spts.lms.studentms.sap.ZCHANGESTMOBILEEMAILWSDFEQ;
import com.spts.lms.studentms.sap.ZmessageLogTt;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.controllers.BaseController;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Controller
public class RegistrationController extends BaseController {

	private static final Logger logger = Logger.getLogger(RegistrationController.class);

	@Autowired
	ApplicationContext act;

	@Autowired
	HttpSession httpSession;

	@Autowired
	HttpSession session;

	@Autowired
	private UserService userService;

	@Autowired
	TrainingProgramService trainingProgramService;

	@Autowired
	TrainingProgram trainingProgram;

	@Autowired

	ProgramCampusService programCampusService;

	@Autowired
	private ProgramService programService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserCourseService userCourseService;
	@Autowired
	private FacultyDetailsService facultyDetailsService;
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private WsdlLogService wsdlLogService;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Value("${app}")
	private String app;

	@Value("${practicalExamUrl}")
	private String practicalExamUrl;

	@Value("${lms.enrollment.years}")
	private String[] enrollmentYears;
	@Value("#{'${acadSessionList}'.split(',')}")
	private List<String> acadSessionList;
	@Value("${lms.enrollment.months}")
	private String[] enrollmentMonths;
	@Value("${lms.enrollment.end.years}")
	private String[] enrollmentEndYears;
	@Value("${lms.enrollment.end.months}")
	private String[] enrollmentEndMonths;

	/*
	 * @Value("#{'${conversionUrl:http://192.168.2.116:8080}'}") String
	 * conversionUrl;
	 */

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Value("${imgPath}")
	private String saveimagePath;
	/*
	 * @Value("#{'${userRoleMgmtCrudUrl:http://13.76.166.129:8080/usermgmtcrud}'}" )
	 * String userRoleMgmtCrudUrl;
	 */

	Client client = ClientBuilder.newClient();

	@ModelAttribute("programs")
	public List<Program> getPrograms() {
		return programService.findAll();
	}

	@ModelAttribute("yearList")
	public String[] getYearList() {
		return enrollmentYears;
	}

	@ModelAttribute("endYearList")
	public String[] getEndYearList() {
		return enrollmentEndYears;
	}

	private StudentExcelHelper getStudentExcelHelper() {
		return (StudentExcelHelper) act.getBean("studentExcelHelper");
	}

	private FacultyExcelHelper getFacultyExcelHelper() {
		return (FacultyExcelHelper) act.getBean("facultyExcelHelper");
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addUserForm", method = RequestMethod.GET)
	public String addUserForm(@ModelAttribute("user") User user, Model m) {
		m.addAttribute("webPage", new WebPage("register", "Register User", true, true, true, true, false));
		m.addAttribute("acadSessionList", acadSessionList);
		return "user/register";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT" })
	@RequestMapping(value = "/downloadAttendence_Report", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadAttendence_Report() {
		ModelAndView mav = new ModelAndView("homepage/downloadAttendence");
		return mav;
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addFacultyDetailsForm", method = { RequestMethod.POST, RequestMethod.GET })
	public String addFacultyDetailsForm(@ModelAttribute FacultyDetails facultyDetails, Model m, Principal p,
			@RequestParam(required = false) Long id) {
		String username1 = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username1);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("register", "Add Faculty Detaiils", true, true, true, true, false));
		if (id != null) {
			facultyDetails = facultyDetailsService.findByID(id);
			m.addAttribute("edit", "true");

		}
		Collection<String> similar = new HashSet<String>();
		Collection<String> different = new HashSet<String>();
		List<User> allFaculties = userService.findAllFacultyByProgramId(Long.valueOf(userdetails1.getProgramId()));
		List<FacultyDetails> addedFaculties = facultyDetailsService.findAll();
		for (User u : allFaculties) {
			String username = u.getUsername();
			// Collection<String> listOne = Arrays.asList(u.getUsername());
			Collection<String> listOne = Arrays.asList(u.getUsername());
			different.addAll(listOne);

		}

		for (FacultyDetails f : addedFaculties) {
			String username = f.getUsername();
			Collection<String> listTwo = Arrays.asList(username);

			similar = new HashSet<String>(listTwo);
			different.removeAll(similar);

		}
		List<String> finalList = new ArrayList<String>();
		for (String s : different) {

			User u = userService.findByUserName(s);

			s = u.getUsername() + " " + u.getFirstname() + " " + u.getLastname();
			finalList.add(s);
		}
		m.addAttribute("finalList", finalList);
		m.addAttribute("allCourses", courseService.findAll());
		m.addAttribute("different", different);
		m.addAttribute("facultyList", userService.findAllFaculty());
		m.addAttribute("facultyDetails", facultyDetails);
		return "user/addFacultyDetails";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addFacultyDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String addFacultyDetails(RedirectAttributes redirectAttrs, Principal principal, Model m,
			@ModelAttribute FacultyDetails facultyDetails) {

		m.addAttribute("webPage", new WebPage("register", "Add Faculty Details", true, false, true, true, false));
		String username = principal.getName();
		try {
			// if (!file.isEmpty()) {
			// String errorMessage = uploadFacultyImage(facultyDetails, file);
			// if (errorMessage == null) {
			facultyDetails
					.setUsername(facultyDetails.getUsername().substring(0, facultyDetails.getUsername().indexOf(" ")));
			facultyDetails.setCreatedBy(username);
			facultyDetails.setLastModifiedBy(username);
			facultyDetailsService.insertWithIdReturn(facultyDetails);
			setSuccess(redirectAttrs, "Details added successfully");
			// }
			// }
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in adding details");
			m.addAttribute("webPage", new WebPage("register", "Add Faculty Details", true, false, true, true, false));
			return "user/addFacultyDetails";
		}

		return "redirect:/viewFacultyDetails";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadFacultyDetailsForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadFacultyDetailsForm(Model m, Principal p) {
		Token userdetails1 = (Token) p;
		String username = p.getName();

		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("groups", "Upload FAQs", false, false));

		FacultyDetails facultyDetails = new FacultyDetails();
		m.addAttribute("facultyDetails", facultyDetails);
		return "user/uploadFacultyDetails";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadFacultyDetails", method = { RequestMethod.POST })
	public String uploadFacultyDetails(@ModelAttribute FacultyDetails faq, @RequestParam("file") MultipartFile file,
			Model m, RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("test", "Upload FAQs", true, false));
		List<String> validateHeaders = null;
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		validateHeaders = new ArrayList<String>(
				Arrays.asList("username", "experience", "overview", "designation", "dob", "mobile", "email"));

		ExcelReader excelReader = new ExcelReader();

		try {
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {

				Map<String, Object> map = maps.get(0);
				/*
				 * int firstQuestionanswer = Integer.parseInt((String) map .get("answer"));
				 */

				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {

						setNote(m, "Error  " + mapper.get("Error"));
					} else {
						FacultyDetails faq1 = new FacultyDetails();
						faq1.setCreatedBy(username);
						faq1.setLastModifiedBy(username);
						mapper.put("createdBy", faq1.getCreatedBy());
						mapper.put("lastModifiedBy", faq1.getLastModifiedBy());
						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());
						mapper.put("age", null);
						if (mapper.get("experience").equals("")) {
							mapper.put("experience", null);
						}
						if (mapper.get("overview").equals("")) {
							mapper.put("overview", null);
						}
						if (mapper.get("designation").equals("")) {
							mapper.put("designation", null);
						}
						if (mapper.get("dob").equals("")) {
							mapper.put("dob", null);
						}
						if (mapper.get("mobile").equals("")) {
							mapper.put("mobile", null);
						}
						if (mapper.get("email").equals("")) {
							mapper.put("email", null);
						}

						// mapper.put("courseId", null);
						facultyDetailsService.insertUsingMap(mapper);
						// setSuccess(m, "FAQ file uploaded successfully");
						setSuccess(redirectAttributes, "FAQ file uploaded successfully");

					}
				}

			}
			m.addAttribute("faq", faq);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "redirect:/viewFacultyDetails";
	}

	/*
	 * @RequestMapping(value = "/searchUser", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String searchUser(@RequestParam(required =
	 * false) Integer pageNo, Model m, @ModelAttribute User user) {
	 * logger.debug("searchUserCourse called." + user); m.addAttribute("webPage",
	 * new WebPage("courseList", "View Course Enrollments", false, false));
	 * 
	 * try { Page<User> page = userService.searchByExactMatch(user, pageNo == null
	 * || pageNo == 0 ? 1 : pageNo, pageSize);
	 * 
	 * List<User> users = page.getPageItems();
	 * 
	 * for (User u : users) {
	 * 
	 * UserRole ur = userRoleService.findRoleByUsername(u .getUsername());
	 * u.setRole(String.valueOf(ur.getRole())); }
	 * 
	 * m.addAttribute("page", page); m.addAttribute("q", getQueryString(users));
	 * 
	 * if (users == null || users.size() == 0) { setNote(m, "No Users found"); }
	 * 
	 * } catch (Exception e) { e.printStackTrace(); logger.error(e.getMessage(), e);
	 * setError(m, "Error in getting userCourseList List"); } return
	 * "user/searchUser"; }
	 */

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/searchUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchUser(@RequestParam(required = false) Integer pageNo, Model m, @ModelAttribute User user) {

		m.addAttribute("webPage", new WebPage("courseList", "View Course Enrollments", false, false));

		try {
			Page<User> page = userService.findUsers(user, pageNo == null || pageNo == 0 ? 1 : pageNo, pageSize);

			List<User> users = page.getPageItems();

			for (User u : users) {

				UserRole ur = userRoleService.findRoleByUsername(u.getUsername());
				u.setRole(String.valueOf(ur.getRole()));
			}

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(users));

			if (users == null || users.size() == 0) {
				setNote(m, "No Users found");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting userCourseList List");
		}
		return "user/searchUser";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/deleteUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteUser(@RequestParam String username, RedirectAttributes redirectAttrs) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			User user = userService.findByUserName(username);
			userService.makeUserIncactive(user);
			String json = mapper.writeValueAsString(user);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/makeUserInactive?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "User is inactive now");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Exam Center.");
		}

		return "redirect:/searchUser";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/deleteUserFromAll", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteUserFromAll(@RequestParam String username, RedirectAttributes redirectAttrs) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			User user = userService.findByUserName(username);
			UserRole uRole = userRoleService.findRoleByUsername(username);

			userService.makeUserIncactive(user);
			studentFeedbackService.makeUserCourseInactiveForFeedback(username, uRole.getRole().getAuthority());
			userCourseService.makeUserInActiveByUsername(username);

			String json = mapper.writeValueAsString(user);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/makeUserInactiveFromAll?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "User is inactive now");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Exam Center.");
		}

		return "redirect:/searchUser";
	}

	/*
	 * private String uploadFacultyImage(FacultyDetails bean, MultipartFile file) {
	 * 
	 * String errorMessage = null; InputStream inputStream = null; OutputStream
	 * outputStream = null;
	 * 
	 * // CommonsMultipartFile file = bean.getFileData(); String fileName =
	 * file.getOriginalFilename();
	 * 
	 * // Replace special characters in file fileName = fileName.replaceAll("'",
	 * "_"); fileName = fileName.replaceAll(",", "_"); fileName =
	 * fileName.replaceAll("&", "and"); fileName = fileName.replaceAll(" ", "_");
	 * 
	 * fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" +
	 * RandomStringUtils.randomAlphanumeric(10) +
	 * fileName.substring(fileName.lastIndexOf("."), fileName.length());
	 * 
	 * logger.info("fileName-->" + fileName);
	 * 
	 * try { inputStream = file.getInputStream(); String imagePath = projectFolder +
	 * File.separator + fileName; logger.info("projectFolder--------------" +
	 * projectFolder); logger.info("imagePath-----------------------" + imagePath);
	 * bean.setImagePath(imagePath); bean.setImagePreviewPath(imagePath);
	 * logger.info("imagePath-->" + imagePath); File folderPath = new
	 * File(projectFolder); if (!folderPath.exists()) { folderPath.mkdirs(); } File
	 * newFile = new File(imagePath); outputStream = new FileOutputStream(newFile);
	 * IOUtils.copy(inputStream, outputStream);
	 * 
	 * } catch (IOException e) { errorMessage = "Error in uploading Image  : " +
	 * e.getMessage(); logger.error("Exception" + errorMessage, e); } finally {
	 * 
	 * if (inputStream != null) IOUtils.closeQuietly(inputStream);
	 * 
	 * if (outputStream != null) IOUtils.closeQuietly(outputStream);
	 * 
	 * }
	 * 
	 * return errorMessage; }
	 */
	@Secured({ "ROLE_STUDENT","ROLE_ADMIN" })
	@RequestMapping(value = "/viewFacultyDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewFacultyDetails(Model m, @ModelAttribute FacultyDetails facultyDetails) {
		m.addAttribute("webPage", new WebPage("faculty", "View Faculty Details", true, true, true, true, false));
		List<FacultyDetails> facultyList = facultyDetailsService.findAll();
		m.addAttribute("facultyDetails", facultyDetails);
		m.addAttribute("facultyList", facultyList);
		return "user/viewAllFaculties";
	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/knowMyFaculty", method = { RequestMethod.GET, RequestMethod.POST })
	public String knowMyFaculty(Model m, Principal principal, @ModelAttribute FacultyDetails facultyDetails,
			@RequestParam Long courseId) {
		m.addAttribute("webPage", new WebPage("facultys", "View Faculty Details", true, true, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		ObjectMapper objMapper = new ObjectMapper();
		String acadSession = u1.getAcadSession();

		List<Course> courseList = userdetails1.getCourseList();
		m.addAttribute("courseList", courseList);
		m.addAttribute("courseId", courseId);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<FacultyDetails> facultyList = new ArrayList<>();
		try {
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getFacultyProfile?courseId=" + courseId));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			facultyList = objMapper.readValue(resp, new TypeReference<List<FacultyDetails>>() {
			});
		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		if (facultyList == null || facultyList.size() == 0) {

			if (courseId != null) {
				setNote(m, "No Records found");
			}

			m.addAttribute("showDetails", false);
		} else {
			m.addAttribute("showDetails", true);
		}

		for (FacultyDetails fd : facultyList) {
			// facultyDetails.setImagePath(fd.getImagePath());
			facultyDetails.setUsername(fd.getUsername());
			facultyDetails.setCourseId(courseId);
			facultyDetails.setFirstName(fd.getFirstName());
			facultyDetails.setLastName(fd.getLastName());
			facultyDetails.setEmail(fd.getEmail());
			facultyDetails.setDesignation(fd.getPosition());
			facultyDetails.setDob(fd.getDob());
			facultyDetails.setAge(fd.getAge());
			facultyDetails.setMobile(fd.getMobile());
			facultyDetails.setOverview(fd.getOverview());
			facultyDetails.setExperience(fd.getExperience());
			Course course = courseService.findByID(courseId);
			String courseName = course.getCourseName();
			facultyDetails.setCourseName(courseName);
			/*
			 * String path = fd.getImagePath();
			 * 
			 * Integer pathx = path.lastIndexOf("resources"); String imagePath =
			 * path.substring(pathx); logger.info("imagePath------------------" +
			 * imagePath); m.addAttribute("imagePath", imagePath);
			 */
		}
		m.addAttribute("facultyDetails", facultyDetails);
		m.addAttribute("facultyList", facultyList);
		return "user/knowMyFaculty";
	}

	
	/*
	 * @RequestMapping(value = "/updateFacultyDetails", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String updateFacultyDetails(
	 * 
	 * @ModelAttribute FacultyDetails facultyDetails, Model m, Principal principal,
	 * RedirectAttributes redirectAttrs) {
	 * 
	 * m.addAttribute("webPage", new WebPage("register", "Faculty Details", false,
	 * false)); try { String username = principal.getName(); FacultyDetails
	 * facultyDb = facultyDetailsService .findByID(facultyDetails.getId());
	 * facultyDb = LMSHelper.copyNonNullFields(facultyDb, facultyDetails);
	 * facultyDb.setLastModifiedBy(username);
	 * 
	 * facultyDetailsService.update(facultyDb);
	 * 
	 * setSuccess(redirectAttrs, "Details updated successfully");
	 * m.addAttribute("facultyDetails", facultyDb);
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error(e.getMessage(), e); setError(m, "Error in updating details");
	 * return "redirect:/addFacultyDetailsForm"; } return
	 * "redirect:/viewFacultyDetails"; }
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/updateFacultyDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateFacultyDetails(@ModelAttribute FacultyDetails facultyDetails, Model m,
			@RequestParam("file") MultipartFile file, Principal principal, RedirectAttributes redirectAttrs) {

		m.addAttribute("webPage", new WebPage("register", "Faculty Details", false, false));
		try {
			// String errorMessage = null;
			String username = principal.getName();
			FacultyDetails facultyDb = facultyDetailsService.findByID(facultyDetails.getId());
			// if (file != null && !file.isEmpty()) {
			// errorMessage = uploadFacultyImage(facultyDetails, file);
			// } else {
			// If User has not added a new file then take the path from
			// existing assignment to use generic update query

			facultyDetails.setImagePath(facultyDb.getImagePath());
			facultyDetails.setImagePreviewPath(facultyDb.getImagePreviewPath());
			// }
			// if (errorMessage == null) {
			facultyDb = LMSHelper.copyNonNullFields(facultyDb, facultyDetails);
			facultyDb.setLastModifiedBy(username);

			facultyDetailsService.update(facultyDb);

			setSuccess(redirectAttrs, "Details updated successfully");
			m.addAttribute("facultyDetails", facultyDb);
			/*
			 * } else { setError(m, errorMessage); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating details");
			return "redirect:/addFacultyDetailsForm";
		}
		return "redirect:/viewFacultyDetails";
	}

	/*
	 * @Secured({ "ROLE_USER" })
	 * 
	 * @RequestMapping(value = "/addUser", method = RequestMethod.POST) public
	 * String addUser(@ModelAttribute("user") User user, Model m, Principal
	 * principal) { String username = principal.getName();
	 * user.setCreatedBy(username); user.setLastModifiedBy(username);
	 * logger.info("Adding user " + username); m.addAttribute("webPage", new
	 * WebPage("register", "Register User", true, true, true, true, false)); try {
	 * userService.registerUser(user); setSuccess(m, "User added successfully."); }
	 * catch (ValidationException ex) { setError(m, ""); } catch
	 * (DuplicateKeyException ex) { setError(m, ""); } return "user/register"; }
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadStudentForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentForm(Model m) {
		m.addAttribute("webPage", new WebPage("uploadStudent", "Upload Students", false, false, true, true, false));
		m.addAttribute("user", new User());
		return "user/uploadStudent";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String addUser(@ModelAttribute("user") User user, Model m, Principal principal) {
		String username = principal.getName();
		user.setCreatedBy(username);
		user.setLastModifiedBy(username);

		m.addAttribute("webPage", new WebPage("register", "Register User", true, true, true, true, false));
		try {

			List<String> userRoleList = new ArrayList<>();
			for (UserRole ur : user.getUserRoles()) {
				userRoleList.add(ur.getRole().toString());
			}

			if (userRoleList.contains("ROLE_HOD")) {

				String hodUsername = user.getUsername() + "_HOD";
				user.setUsername(hodUsername);
			}
			userService.registerUser(user);
			user.setOperation("INSERT");
			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(user);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateUser?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			for (UserRole ur : user.getUserRoles()) {
				ur.setCreatedBy(username);
				ur.setLastModifiedBy(username);
				ur.setOperation("INSERT");
				String jsonUserRole = mapper.writeValueAsString(ur);
				WebTarget webTarget1 = client
						.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateUserRole?json=" + jsonUserRole));
				Invocation.Builder invocationBuilder1 = webTarget1.request(MediaType.APPLICATION_JSON);
				String resp1 = invocationBuilder1.get(String.class);

			}
			/*
			 * MenuLoadParams menuLoadParams = new MenuLoadParams();
			 * menuLoadParams.setAbbr("BSSA-NM-M");
			 * menuLoadParams.setUsername(user.getUsername()); String jsonMenu =
			 * mapper.writeValueAsString(menuLoadParams);
			 * 
			 * if (getRole.contains("ROLE_ADMIN") || getRole.contains("ROLE_FACULTY")) {
			 * logger.info("user role got -------" + getRole.get(1)); if
			 * (getRole.get(1).contains("ROLE_ADMIN")) {
			 * logger.info("user role is admin -------"); WebTarget webTargetMenu =
			 * client.target(URIUtil .encodeQuery(conversionUrl +
			 * "/fullLoadMenuRightsForAdminByAbbr?json=" + jsonMenu)); Invocation.Builder
			 * invocationBuilderMenu = webTargetMenu .request(MediaType.APPLICATION_JSON);
			 * String respMenu = invocationBuilderMenu.get(String.class); logger.info("resp"
			 * + respMenu); } else { logger.info("user role is faculty-------"); WebTarget
			 * webTargetMenu = client .target(URIUtil .encodeQuery(conversionUrl +
			 * "/fullLoadMenuRightsForFacultyByAbbr?json=" + jsonMenu)); Invocation.Builder
			 * invocationBuilderMenu = webTargetMenu .request(MediaType.APPLICATION_JSON);
			 * String respMenu = invocationBuilderMenu.get(String.class); logger.info("resp"
			 * + respMenu); } }
			 */

			setSuccess(m, "User added successfully.");
			m.addAttribute("acadSessionList", acadSessionList);
		} catch (ValidationException ex) {
			setError(m, "");
		} catch (DuplicateKeyException ex) {
			setError(m, "");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "user/register";
	}

	/*
	 * @RequestMapping(value = "/uploadStudent", method = RequestMethod.POST) public
	 * String uploadStudent(@ModelAttribute User user, Principal principal,
	 * 
	 * @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
	 * StudentExcelHelper studentExcelHelper = getStudentExcelHelper(); String
	 * username = principal.getName(); try { if (!file.isEmpty()) {
	 * user.setEnrollmentMonths(new ArrayList<String>(Arrays
	 * .asList(enrollmentMonths))); user.setEnrollmentEndMonths(new
	 * ArrayList<String>(Arrays .asList(enrollmentEndMonths)));
	 * user.setCreatedBy(username); user.setLastModifiedBy(username);
	 * studentExcelHelper.initHelper(user);
	 * studentExcelHelper.readExcel((MultipartFile) file); }
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); setError(redirectAttrs,
	 * "Error in uploading File: " + e.getMessage()); return
	 * "redirect:/uploadStudentForm"; }
	 * 
	 * if (studentExcelHelper.getErrorList().isEmpty()) { setSuccess(redirectAttrs,
	 * "File uploaded successfully"); return "redirect:/uploadStudentForm"; } else {
	 * setErrorList(redirectAttrs,
	 * "Errors encountered uploading file: No Records Added",
	 * studentExcelHelper.getErrorList()); return "redirect:/uploadStudentForm"; }
	 * 
	 * }
	 */

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadStudent", method = RequestMethod.POST)
	public String uploadStudent(@ModelAttribute User user, Principal principal,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
		StudentExcelHelper studentExcelHelper = getStudentExcelHelper();
		String username = principal.getName();
		try {
			if (!file.isEmpty()) {
				user.setEnrollmentMonths(new ArrayList<String>(Arrays.asList(enrollmentMonths)));
				user.setEnrollmentEndMonths(new ArrayList<String>(Arrays.asList(enrollmentEndMonths)));
				user.setCreatedBy(username);
				user.setLastModifiedBy(username);
				studentExcelHelper.initHelper(user);
				studentExcelHelper.readExcel((MultipartFile) file);
				List<User> userList = studentExcelHelper.getSuccessList();
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(studentExcelHelper.getSuccessList());

				/*
				 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				 * logger.info(" json--->" + (json));
				 */
				/*
				 * WebTarget webTarget = client.target(URIUtil .encodeQuery(userRoleMgmtCrudUrl
				 * + "/addOrUpdateUserBulk?json=" + json)); Invocation.Builder invocationBuilder
				 * = webTarget .request(MediaType.APPLICATION_JSON); String resp =
				 * invocationBuilder.get(String.class);
				 */

				ClientConfig clientConfig = null;
				Client clientWS = null;
				WebTarget webTarget = null;
				Invocation.Builder invocationBuilder = null;
				Response response = null;
				clientConfig = new ClientConfig();

				clientWS = ClientBuilder.newClient(clientConfig);
				webTarget = clientWS.target(userRoleMgmtCrudUrl + "/addOrUpdateUserBulk");
				// .target(LMSURL+"/MPSTME-NM-M/createAssignmentFromJson/");

				// set file upload values
				invocationBuilder = webTarget.request();

				response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

				/*
				 * response = webTarget.request(MediaType.APPLICATION_JSON).post
				 * (Entity.json(json), Response.class);
				 */

				// get response code
				int responseCode = response.getStatus();

				for (User ur : userList) {

					String jsonUserRole = mapper.writeValueAsString(ur.getUserRoles());

					/*
					 * logger.info("encoded json--->" + URIUtil.encodeQuery(jsonUserRole));
					 */
					WebTarget webTarget1 = client.target(
							URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateUserRoleBulk?json=" + jsonUserRole));
					Invocation.Builder invocationBuilder1 = webTarget1.request(MediaType.APPLICATION_JSON);
					String resp1 = invocationBuilder1.get(String.class);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			setError(redirectAttrs, "Error in uploading File: " + e.getMessage());
			return "redirect:/uploadStudentForm";
		}

		if (studentExcelHelper.getErrorList().isEmpty()) {
			setSuccess(redirectAttrs, "File uploaded successfully");
			return "redirect:/uploadStudentForm";
		} else {
			setErrorList(redirectAttrs, "Errors encountered uploading file: No Records Added",
					studentExcelHelper.getErrorList());
			return "redirect:/uploadStudentForm";
		}

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadFacultyForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadFacultyForm(Model m) {
		m.addAttribute("webPage", new WebPage("uploadFaculty", "Upload Faculties", false, false, true, true, false));
		m.addAttribute("user", new User());
		return "user/uploadFaculty";
	}

	/*
	 * @RequestMapping(value = "/uploadFaculty", method = RequestMethod.POST) public
	 * String uploadFaculty(@ModelAttribute User user, Principal principal,
	 * 
	 * @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
	 * FacultyExcelHelper facultyExcelHelper = getFacultyExcelHelper(); String
	 * username = principal.getName(); try { if (!file.isEmpty()) {
	 * user.setCreatedBy(username); user.setLastModifiedBy(username);
	 * facultyExcelHelper.initHelper(user);
	 * facultyExcelHelper.readExcel((MultipartFile) file); }
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); setError(redirectAttrs,
	 * "Error in uploading File: " + e.getMessage()); return
	 * "redirect:/uploadFacultyForm"; }
	 * 
	 * if (facultyExcelHelper.getErrorList().isEmpty()) { setSuccess(redirectAttrs,
	 * "File uploaded successfully"); return "redirect:/uploadFacultyForm"; } else {
	 * setErrorList(redirectAttrs,
	 * "Errors encountered uploading file: No Records Added",
	 * facultyExcelHelper.getErrorList()); return "redirect:/uploadFacultyForm"; }
	 * 
	 * }
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadFaculty", method = RequestMethod.POST)
	public String uploadFaculty(@ModelAttribute User user, Principal principal,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs) {
		FacultyExcelHelper facultyExcelHelper = getFacultyExcelHelper();
		String username = principal.getName();
		try {
			if (!file.isEmpty()) {
				user.setCreatedBy(username);
				user.setLastModifiedBy(username);
				facultyExcelHelper.initHelper(user);
				facultyExcelHelper.readExcel((MultipartFile) file);
				List<User> userList = facultyExcelHelper.getSuccessList();
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(facultyExcelHelper.getSuccessList());

				/*
				 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				 * logger.info(" json--->" + (json));
				 */
				/*
				 * WebTarget webTarget = client.target(URIUtil .encodeQuery(userRoleMgmtCrudUrl
				 * + "/addOrUpdateUserBulk?json=" + json)); Invocation.Builder invocationBuilder
				 * = webTarget .request(MediaType.APPLICATION_JSON); String resp =
				 * invocationBuilder.get(String.class);
				 */

				WebTarget webTarget = client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateUserBulk"));

				Invocation.Builder invocationBuilder = webTarget.request();

				Response resp = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

				logger.info("reponse is--->" + resp.getStatus());

				for (User ur : userList) {

					String jsonUserRole = mapper.writeValueAsString(ur.getUserRoles());
					/*
					 * logger.info("encoded json--->" + URIUtil.encodeQuery(jsonUserRole));
					 */
					WebTarget webTarget1 = client.target(
							URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateUserRoleBulk?json=" + jsonUserRole));
					Invocation.Builder invocationBuilder1 = webTarget1.request(MediaType.APPLICATION_JSON);
					String resp1 = invocationBuilder1.get(String.class);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			setError(redirectAttrs, "Error in uploading File: " + e.getMessage());
			return "redirect:/uploadFacultyForm";
		}

		if (facultyExcelHelper.getErrorList().isEmpty()) {
			setSuccess(redirectAttrs, "File uploaded successfully");
			return "redirect:/uploadFacultyForm";
		} else {
			setErrorList(redirectAttrs, "Errors encountered uploading file: No Records Added",
					facultyExcelHelper.getErrorList());
			return "redirect:/uploadFacultyForm";
		}

	}

	// 02-07-2020

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/updateProfileForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateProfileForm(Model m, Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("updateProfile", "Update Profile", false, false, true, true, false));
		m.addAttribute("user", userService.findByUserName(username));
		return "user/newUpdateProfile";
	}

	/*
	 * //25-01-2021
	 * 
	 * @RequestMapping(value = "/updateProfile", method = RequestMethod.POST) public
	 * String updateProfile(Model m, @ModelAttribute User user, RedirectAttributes
	 * redirectAttrs, Principal principal) { try {
	 * 
	 * String username = principal.getName(); User userDB =
	 * userService.findByUserName(username); userDB.setUsername(username);
	 * userDB.setLastModifiedBy(username); userDB.setEmail(user.getEmail());
	 * userDB.setMobile(user.getMobile()); userDB.setOperation("UPDATE");
	 * 
	 * User userBean = new User(); User userBean2 =
	 * userService.findStudentObjectId(username);
	 * 
	 * WsdlLog wsLog = new WsdlLog();
	 * 
	 * userBean = userService.findByUserName(username);
	 * 
	 * m.addAttribute("userBean", userBean); session.setAttribute("userBean",
	 * userBean);
	 * 
	 * String studentObjId = ""; if (userBean2 == null) {
	 * 
	 * WebTarget webTarget12 = client.target(URIUtil
	 * .encodeQuery(userRoleMgmtCrudUrl + "/getStudentObjIdByUsername?username=" +
	 * username)); Invocation.Builder invocationBuilder12 = webTarget12
	 * .request(MediaType.APPLICATION_JSON); studentObjId =
	 * invocationBuilder12.get(String.class);
	 * 
	 * User newUserForScoolMap = new User();
	 * 
	 * newUserForScoolMap.setUsername(username);
	 * newUserForScoolMap.setStudentObjectId(studentObjId);
	 * 
	 * userService.insertStudentMapping(newUserForScoolMap);
	 * 
	 * } else { studentObjId = userBean2.getStudentObjectId(); }
	 * 
	 * if (!studentObjId.equals("null")) { ZCHANGESTMOBILEEMAILWSSEP ws = new
	 * ZCHANGESTMOBILEEMAILWSSEP(); Holder<ZmessageLogTt> lst = new
	 * Holder<ZmessageLogTt>();
	 * 
	 * try {
	 * 
	 * String resp = ws.getZCHANGESTMOBILEEMAILBINDSEP()
	 * .zchangeStMobileEmail(user.getEmail(), user.getMobile(), "", studentObjId,
	 * lst); logger.info("ZmessageProfileLog----->" + lst.value); } catch (Exception
	 * e) {
	 * 
	 * logger.error("Exception while calling a webservice", e);
	 * wsLog.setStudentObjectid(studentObjId); wsLog.setUsername(username);
	 * wsLog.setMobile(user.getMobile()); wsLog.setEmail(user.getEmail());
	 * wsLog.setFailedReason(e.toString()); wsLog.setCreatedBy(username);
	 * wsLog.setLastModifiedBy(username); try { wsdlLogService.insert(wsLog); }
	 * catch (Exception ee) { logger.error("Exception while logging error", ee); }
	 * 
	 * }
	 * 
	 * userService.updateProfile(userDB);
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); String json =
	 * mapper.writeValueAsString(userDB);
	 * 
	 * WebTarget webTarget1 = client.target(URIUtil .encodeQuery(userRoleMgmtCrudUrl
	 * + "/addOrUpdateUser?json=" + json)); Invocation.Builder invocationBuilder1 =
	 * webTarget1 .request(MediaType.APPLICATION_JSON); String resp1 =
	 * invocationBuilder1.get(String.class);
	 * 
	 * setSuccess(redirectAttrs, "Profile updated successfully"); } else {
	 * 
	 * setError(redirectAttrs, "Entry not found in SAP"); return
	 * "redirect:/updateProfileForm"; }
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); setError(redirectAttrs,
	 * "Error in updating Profile");
	 * 
	 * } return "redirect:/updateProfileForm"; }
	 */

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
	public String updateProfile(Model m, @ModelAttribute User user, RedirectAttributes redirectAttrs,
			Principal principal, @RequestParam("file") MultipartFile file) {
		try {

			String username = principal.getName();
			User userDB = userService.findByUserName(username);
			userDB.setUsername(username);
			userDB.setLastModifiedBy(username);
			userDB.setEmail(user.getEmail());
			userDB.setMobile(user.getMobile());
			userDB.setAddress(user.getAddress());
			userDB.setOperation("UPDATE");
			user.setUsername(username);
			m.addAttribute("fileuploaderror", "");

			String encodedString = null;

			if (!file.isEmpty()) {
				String originalfileName = file.getOriginalFilename();
				//Audit change start
				Tika tika = new Tika();
				String detectedType = tika.detect(file.getBytes());
				if (originalfileName.contains(".")) {
					Long count = originalfileName.chars().filter(c -> c == ('.')).count();
					logger.info("length--->" + count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/updateProfileForm";
					} else {
						String extension = FilenameUtils.getExtension(originalfileName);
						logger.info("extension--->" + extension);
						if (extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/updateProfileForm";
						} else {
							//Audit change end
							File convFile = new File(originalfileName);
							File saveimage = new File(saveimagePath);

							double kilobyteSize = file.getSize();
							double ogfilesize = 200;// in kb

							double kilo = Utils.round((kilobyteSize / 1024), 2);

							String uploadimagepath = saveimagePath;
							String realstorpath = saveimagePath.substring(7, uploadimagepath.length() - 1);

							if (kilo <= ogfilesize && kilo > 0) {
								if (originalfileName.contains(".")) {
									String name = FilenameUtils.getBaseName(originalfileName);
									String repo = name.replace(name, username);
									logger.info("RepoName------------------->" + repo);
									// Get the file and save it somewhere
									byte[] bytes = file.getBytes();

									// Convert File Int Bytes

									encodedString = Base64.getEncoder().encodeToString(bytes);
									user.setUserImage(encodedString);

									/*
									 * BASE64Decoder decoder = new BASE64Decoder(); byte[] decodedBytes =
									 * decoder.decodeBuffer(encodedString);
									 */

									File of = new File(realstorpath + "/" + username + ".JPG");
									FileOutputStream osf = new FileOutputStream(of);
									osf.write(bytes);
									osf.close();

								}
							} else {
								String fileuploaderror = "File size must not be greater 200kb";
								m.addAttribute("fileuploaderror", fileuploaderror);
								setError(redirectAttrs, "File size must not be greater 200kb");
								return "redirect:/updateProfileForm";
							}
							//Audit change start
						}
					}
				} else {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/updateProfileForm";
				}
				//Audit change end
			}

			User userBean = new User();
			User userBean2 = userService.findStudentObjectId(username);

			userBean = userService.findByUserName(username);

			m.addAttribute("userBean", userBean);
			session.setAttribute("userBean", userBean);

			String studentObjId = "";
			if (userBean2 == null) {
				try {
					WebTarget webTarget12 = client.target(URIUtil
							.encodeQuery(userRoleMgmtCrudUrl + "/getStudentObjIdByUsername?username=" + username));
					Invocation.Builder invocationBuilder12 = webTarget12.request(MediaType.APPLICATION_JSON);
					studentObjId = invocationBuilder12.get(String.class);

					User newUserForScoolMap = new User();

					newUserForScoolMap.setUsername(username);
					newUserForScoolMap.setStudentObjectId(studentObjId);

					userService.insertStudentMapping(newUserForScoolMap);
				} catch (Exception e) {
					logger.error("Exception while getting student Id or inserting data", e);
				}
			} else {
				studentObjId = userBean2.getStudentObjectId();
			}

			if (!studentObjId.equals("null")) {
				String wsdlresponse = "";

				ZCHANGESTMOBILEEMAILWSDFEQ ws = new ZCHANGESTMOBILEEMAILWSDFEQ();
				Holder<ZmessageLogTt> lst = new Holder<ZmessageLogTt>();
				
				ZCHANGESTMOBILEEMAILWSD ws1 = ws.getZCHANGESTMOBILEEMAILBINDFEQ();
				logger.info("ws.getZCHANGESTMOBILEEMAILBIND()--->"+ws.getZCHANGESTMOBILEEMAILBINDFEQ());
				BindingProvider prov = (BindingProvider)ws1;
				
				prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "basissp");
				prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "india@123");
				logger.info("ws2--->"+prov.getRequestContext());
				
				logger.info("ref--->"+prov.getEndpointReference());
				try {
					logger.info("studentObjId--->"+studentObjId);
					String sha256hex = DigestUtils.sha256Hex(studentObjId);
					logger.info("sha256hex--->"+sha256hex);
					if (!file.isEmpty()) {
						wsdlresponse = ws1.zchangeStMobileEmail(user.getEmail(), 
								user.getMobile(), encodedString, username, sha256hex, lst);
					} else {
						wsdlresponse = ws1.zchangeStMobileEmail(user.getEmail(), 
								user.getMobile(), "", username, sha256hex, lst);
					}

					logger.info("resp----------<><>< " + wsdlresponse);
					logger.info("ZmessageProfileLog----->" + lst.value);
				} catch (Exception e) {
					logger.error("Exception while calling a webservice", e);
				}

				if (wsdlresponse.equalsIgnoreCase("SUCCESS")) {
					userService.updateProfile(userDB);

					ObjectMapper mapper = new ObjectMapper();
					String json = mapper.writeValueAsString(userDB);

					WebTarget webTarget1 = client
							.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateUser?json=" + json));
					Invocation.Builder invocationBuilder1 = webTarget1.request(MediaType.APPLICATION_JSON);
					String resp1 = invocationBuilder1.get(String.class);
					if (!file.isEmpty()) {
						ObjectMapper mapper1 = new ObjectMapper();
						String json1 = mapper1.writeValueAsString(user);

						logger.info("json1 value for usermgmt------------------------------->" + json1);
						WebTarget webTarget2 = client
								.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/insertUpdateImageForStudent"));

						Invocation.Builder invocationBuilder2 = webTarget2.request();
						Response response = invocationBuilder2
								.post(Entity.entity(json1.toString(), MediaType.APPLICATION_JSON));

						logger.info("response---->" + response);
						String resp = response.readEntity(String.class);
					}
					setSuccess(redirectAttrs, "Profile updated successfully");
				} else {
					setError(redirectAttrs, "Error occurred while updating profile!");
				}

			} else {

				setError(redirectAttrs, "Student Id entry not found in SAP");
				return "redirect:/updateProfileForm";
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in updating Profile");

		}
		return "redirect:/updateProfileForm";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addNewAdminSchoolWiseForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addNewAdminSchoolWiseForm(Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Admin", true, true, true, true, false));
		m.addAttribute("user", new User());
		return "user/addNewAdminSchoolWise";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addNewAdminSchoolWise", method = { RequestMethod.GET, RequestMethod.POST })
	public String addNewAdminSchoolWise(@ModelAttribute User user, Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Admin", true, true, true, true, false));

		try {
			if (user != null) {

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(user);
				/*
				 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				 * logger.info(" json--->" + (json));
				 */
				WebTarget webTarget = client
						.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addAdminUsersSchoolWise?json=" + json));
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);

				setSuccess(m, "Admin Added Successfully!");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in uploading File");
			return "user/addNewAdminSchoolWise";
		}
		return "user/addNewAdminSchoolWise";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addAdminProgramForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addAdminProgramForm(Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Admin", true, true, true, true, false));
		m.addAttribute("user", new User());
		return "user/addAdminProgram";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "getProgramByObjectId", method = { RequestMethod.POST })
	public @ResponseBody Object selectCompanyMapping(Model m, @RequestParam(name = "schoolObjId") String schoolobj)
			throws JsonParseException, JsonMappingException, IOException {

		logger.info("school Object: Hitting=================> " + schoolobj);

		WebTarget webTarget = client.target(
				URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getAdminProgramMappingbyObjectId?schoolobj=" + schoolobj));
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		String resp = invocationBuilder.get(String.class);

		logger.info("school Object: Hitting resp=================> " + resp);

		ObjectMapper mapper = new ObjectMapper();

		List<Program> secquestionList = mapper.readValue(resp, new TypeReference<List<Program>>() {
		});

		logger.info("secquestionList------------>>" + secquestionList);

		// Converting the Object to JSONString
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(secquestionList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("******JsonString*****" + jsonString);
		return jsonString;
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addAdminProgram", method = { RequestMethod.GET, RequestMethod.POST })
	public String addAdminProgram(@ModelAttribute User user, Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Admin", true, true, true, true, false));

		try {
			if (user != null) {

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(user);
				/*
				 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				 * logger.info(" json--->" + (json));
				 */
				WebTarget webTarget = client
						.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addAdminProgramMapping?json=" + json));
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);

				setSuccess(m, "Admin Prgram Mapping Added Successfully!");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in uploading File");
			return "user/addAdminProgram";
		}
		return "user/addAdminProgram";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addAdminMenuRightsForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addAdminMenuRightsForm(Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Admin", true, true, true, true, false));
		m.addAttribute("user", new User());
		return "user/addAdminMenuRights";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addAdminMenuRights", method = { RequestMethod.GET, RequestMethod.POST })
	public String addAdminMenuRights(@ModelAttribute User user, Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Admin", true, true, true, true, false));

		try {
			if (user != null) {

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(user);
				/*
				 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				 * logger.info(" json--->" + (json));
				 */
				WebTarget webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl + "/fullLoadMenuRightsForAdminByAbbrNonJson?json=" + json));
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);

				setSuccess(m, "Admin Menu Rights Added Successfully!");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in uploading File");
			return "user/addAdminMenuRights";
		}
		return "user/addAdminMenuRights";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addOtherUserForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addOtherUserForm(Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new Librarian", true, true, true, true, false));
		m.addAttribute("user", new User());
		return "user/addOtherUser";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/addOtherUser", method = { RequestMethod.GET, RequestMethod.POST })
	public String addOtherUser(@ModelAttribute User user, Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("addNewAdminSchoolWiseForm", "Add new User", true, true, true, true, false));

		try {
			if (user != null) {

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(user);
				/*
				 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
				 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
				 * logger.info(" json--->" + (json));
				 */
				WebTarget webTarget = client
						.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOtherUser?json=" + json));
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);

				setSuccess(m, "New User Added Successfully!");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating user");
			return "user/addOtherUser";
		}
		return "user/addOtherUser";
	}

	@Secured({ "ROLE_STUDENT", "ROLE_EXAM", "ROLE_EXAM_ADMIN", "ROLE_IT", "ROLE_FACULTY" })
	@RequestMapping(value = "/visitExamApp", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView visitExamApp(Model m, Principal principal, RedirectAttributes rm) throws ParseException {
		String json = "";
		String projectUrl = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			/*
			 * m.addAttribute("webPage", new WebPage("assignment", "View Attendnace", true,
			 * false));
			 */

			String username = principal.getName();
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

			String acadSession = u.getAcadSession();
			// session.setAttribute("userdetails1", userdetails1);

			if (userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)) {
				projectUrl = practicalExamUrl + "/studentUserExam";
				RedirectView view = new RedirectView(projectUrl);
				User newUser = userService.findByUserName(username);
				newUser.setSchoolAbbr(app);
				newUser.setRole("ROLE_STUDENT");
				json = mapper.writeValueAsString(newUser);
				logger.info("Json" + json);
				rm.addAttribute("json", json);
				return new ModelAndView(view);

			} else if (userdetails1.getAuthorities().contains(Role.ROLE_EXAM)) {
				projectUrl = practicalExamUrl + "/examUserExam";
				RedirectView view = new RedirectView(projectUrl);
				User newUser = userService.findByUserName(username);
				newUser.setSchoolAbbr(app);
				newUser.setRole("ROLE_EXAM");
				json = mapper.writeValueAsString(newUser);
				logger.info("Json" + json);
				rm.addAttribute("json", json);
				return new ModelAndView(view);

			} else if (userdetails1.getAuthorities().contains(Role.ROLE_EXAM_ADMIN)) {
				projectUrl = practicalExamUrl + "/superAdminExam";
				RedirectView view = new RedirectView(projectUrl);
				User newUser = userService.findByUserName(username);
				newUser.setSchoolAbbr(app);
				newUser.setRole("ROLE_EXAM_ADMIN");
				json = mapper.writeValueAsString(newUser);
				logger.info("Json" + json);
				rm.addAttribute("json", json);
				return new ModelAndView(view);

			} else if (userdetails1.getAuthorities().contains(Role.ROLE_IT)) {
				projectUrl = practicalExamUrl + "/ITUserExam";
				RedirectView view = new RedirectView(projectUrl);
				User newUser = userService.findByUserName(username);
				newUser.setSchoolAbbr(app);
				newUser.setRole("ROLE_IT");
				json = mapper.writeValueAsString(newUser);
				logger.info("Json" + json);
				rm.addAttribute("json", json);
				return new ModelAndView(view);

			} else if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				projectUrl = practicalExamUrl + "/facultyUserExam";
				RedirectView view = new RedirectView(projectUrl);
				User newUser = userService.findByUserName(username);
				newUser.setSchoolAbbr(app);
				newUser.setRole("ROLE_FACULTY");
				json = mapper.writeValueAsString(newUser);
				logger.info("Json" + json);
				rm.addAttribute("json", json);
				return new ModelAndView(view);

			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return new ModelAndView("/");
	}

	/*
	 * @RequestMapping(value = "/createTrainingProgram", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * createTrainingProgram(Principal principal, Model m) {
	 * m.addAttribute("webPage", new WebPage("createTrainingProgram",
	 * "Add new Librarian", true, true, true, true, false));
	 * m.addAttribute("TrainingProgram", new TrainingProgram()); return
	 * "homepage/createTrainingProgram"; }
	 */

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/createTrainingProgram", method = { RequestMethod.GET, RequestMethod.POST })
	public String createTrainingProgram(Principal principal, Model m,

			@ModelAttribute TrainingProgram TrainingProgram, RedirectAttributes redirectAttrs) throws IOException {

		ProgramCampus CampAbbr = programCampusService.getCampusNameByCampusId(TrainingProgram.getCampusId());

		TrainingProgram.setSchool(CampAbbr.getCampusAbbr());
		String username = principal.getName();
		TrainingProgram.setCreatedBy(username);
		TrainingProgram.setLastModifiedBy(username);
		logger.info("TrainingProgram--->" + TrainingProgram.getTrainingTitle());
		trainingProgramService.insertWithIdReturn(TrainingProgram);
		setSuccess(redirectAttrs, "Program added successfully");
		return "redirect:/createTraingProgramForm";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/createTraingProgramForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String createTraingProgramForm(Principal principal, Model m, @ModelAttribute TrainingProgram TrainingProgram,
			RedirectAttributes redirectAttrs) throws IOException {
		logger.info("TrainingProgram--->" + TrainingProgram);
		trainingProgramService.insertWithIdReturn(TrainingProgram);
		setSuccess(redirectAttrs, "Program added successfully");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(TrainingProgram);
		WebTarget webTarget = client.target(
				URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/selectCampusLocationAndAbbr?json=" + TrainingProgram));
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		String resp = invocationBuilder.get(String.class);
		m.addAttribute("resp", resp);
		logger.info("resp========================~~~~~~>" + resp);
		return "homepage/createTrainingProgram";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/viewscheduledprogram", method = {

			RequestMethod.GET, RequestMethod.POST })

	public String viewscheduledprogram(Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("createTrainingProgram", "Add new Librarian", true, true, true, true, false));
		m.addAttribute("TrainingProgram", new TrainingProgram());

		List<TrainingProgram> listofprogram = trainingProgramService.findTrainingProgramList();
		m.addAttribute("listofprogram", listofprogram);
		List<ProgramCampus> programCampus = programCampusService.getProgramCampusName();
		logger.info("programCampus--->" + programCampus);
		m.addAttribute("programName", programCampus);
		logger.info("Listofprogram  -- > " + listofprogram);
		return "homepage/viewscheduledprogram";

	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/getCampuseNameForTraining", method = {

			RequestMethod.GET, RequestMethod.POST })

	public @ResponseBody String getCampuseNameForTraining(

			@RequestParam(name = "campusName") String campusName, @RequestParam(name = "programId") String programId,
			Model m) {
		String json = "";

		// logger.info("campusName " + campusid);
		List<ProgramCampus> campusNameDrop = programCampusService.getCampusNameDropDown(campusName);

		logger.info("campusNameDrop======>" + campusNameDrop);
		// List<ProgramCampus> programCampus =
		// programCampusService.getProgramCampusNameList(campusid);

		List<ProgramCampus> campusNameDropid = programCampusService.getCampusNameDropDownId(programId);

		logger.info("GetProgramCampusNameList------------------------> " + campusNameDropid);

		// m.addAttribute("programName",programCampus);

		logger.info("programCampus--->" + campusNameDropid);
		// m.addAttribute("programName",programCampus);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (ProgramCampus ass : campusNameDrop) {

			Map<String, String> returnMap = new HashMap<String, String>();

			returnMap.put(String.valueOf(ass.getCampusName()), ass.getProgramId());

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
