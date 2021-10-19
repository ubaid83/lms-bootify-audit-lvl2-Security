package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Holder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.glassfish.jersey.client.ClientConfig;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.parse.BasicParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.onelogin.saml2.util.Constants;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.attendance.Attendance;
import com.spts.lms.beans.attendance.StudentCourseAttendance;
import com.spts.lms.beans.calender.Calender;
import com.spts.lms.beans.copyleaksAudit.CopyleaksAudit;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.dashboard.DashBoard;
import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.hostelUser.HostelUser;
import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.IcaComponentMarks;
import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.beans.library.Library;
import com.spts.lms.beans.newsEvents.NewsEvents;
import com.spts.lms.beans.pending.PendingTask;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmation;
import com.spts.lms.beans.studentConfirmation.studentDetailConfirmationPeriod;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.timetable.Timetable;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.TrainingProgram;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserConverter;
import com.spts.lms.beans.user.UserLoginOutTime;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.beans.user.WsdlLog;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.beans.webpages.Webpages;
import com.spts.lms.daos.timetable.TimetableDAO;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.plagscan.CopyLeaks;
import com.spts.lms.plagscan.PlagScanner;
import com.spts.lms.services.StudentService.LorRegStaffService;
/*import com.spts.lms.sap.profile.ZCHANGESTMOBILEEMAILWSSEP;
import com.spts.lms.sap.profile.ZmessageLogTt;*/
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentAuditService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.attendance.AttendanceService;
import com.spts.lms.services.attendance.StudentCourseAttendanceService;
import com.spts.lms.services.copyleaksAudit.CopyleaksAuditService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.dashboard.DashboardService;
import com.spts.lms.services.feedback.FeedbackQuestionService;
import com.spts.lms.services.feedback.FeedbackService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.ica.IcaBeanService;
import com.spts.lms.services.ica.IcaComponentMarksService;
import com.spts.lms.services.ica.IcaTotalMarksService;
import com.spts.lms.services.library.LibraryService;
import com.spts.lms.services.newsEvents.NewsEventsService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.redis.RedisService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationPeriodService;
import com.spts.lms.services.studentConfirmation.studentDetailConfirmationService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.TrainingProgramService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.user.WsdlLogService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.services.webpages.WebpagesService;
import com.spts.lms.studentms.sap.ZCHANGESTMOBILEEMAILWSSEP;
import com.spts.lms.studentms.sap.ZmessageLogTt;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.AESEncryption;
import com.spts.lms.web.utils.Utils;

@Controller
public class LoginController extends BaseController {

	private static final Logger logger = Logger.getLogger(LoginController.class);

	@Autowired
	HttpSession httpSession;

	@Autowired
	private UserService userService;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Autowired
	private AssignmentService assignmentService;

	@Value("${lms.assignment.downloadAllFolder:''}")
	private String downloadAllFolderTemp;

	@Value("${file.base.directory}")
	private String downloadResultPath;
	
	@Autowired
	RedisService service;

	@Autowired
	studentDetailConfirmationService studentDetailConfirmationService;

	@Autowired
	studentDetailConfirmationPeriodService studentDetailConfirmationPeriodService;

	@Autowired
	private AnnouncementService announcemnetService;

	@Autowired
	ProgramService programService;

	@Autowired
	private TestService testService;

	@Autowired
	HttpSession session;

	@Autowired
	Notifier notifier;

	@Autowired
	private CourseService courseService;

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private WebpagesService webpagesService;

	@Autowired
	private FeedbackQuestionService feedbackQuestionService;

	@Autowired
	private StudentTestService studentTestService;

	@Autowired
	IcaBeanService icaBeanService;

	@Autowired
	private StudentFeedbackService studentFeedbackService;

	@Autowired
	private AnnouncementService announcementService;

	@Autowired
	private StudentParentService studentParentService;

	@Autowired
	private DashboardService dashBoardService;

	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private StudentCourseAttendanceService studentCourseAttendanceService;

	@Autowired
	UserDAO userDAO;

	@Autowired
	TimetableDAO timetableDAO;

	@Autowired
	NewsEventsService newsEventsService;

	@Autowired
	LibraryService libraryService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Value("${plagiarismCheck:''}")
	private String plagiarismCheck;

	@Value("${copyleaksId:''}")
	private String copyleaksId;

	@Value("${copyleaskKey:''}")
	private String copyleaskKey;
	
	@Value("#{'${aes.secretKey}'}")
	String secretKey;

	@Value("#{'${aes.saltKey}'}")
	String salt;
	
	@Value("${userMgmtUrlApp}")
	private String userMgmtUrlApp;

	@Autowired
	StudentAssignmentController studentAssignmentController;

	@Autowired
	CopyleaksAuditService copyleaksAuditService;

	@Autowired
	PlagScanner submit;

	@Autowired
	CopyLeaks copyLeaks;

	@Autowired
	StudentAssignmentAuditService studentAssignmentAuditService;

	@Autowired
	IcaTotalMarksService icaTotalMarksService;

	@Autowired
	IcaComponentMarksService icaComponentMarksService;

	@Autowired
	private TrainingProgramService trainingProgramService;

	@Autowired
	private ProgramCampusService programCampusService;

	@Autowired
	private WsdlLogService wsdlLogService;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	LmsVariablesService lmsVariablesService;

	@Autowired
	StudentAttendanceController studentAttendanceController;
	
	
	@Autowired
	LorRegStaffService lorRegStaffService;

	ObjectMapper mapper = new ObjectMapper();

	@Value("#{'${appName:nmims}'}")
	String appName;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Value("#{'${logoutUrl:http://localhost:8080/loggedout}'}")
	String logoutUrl;

	@Value("#{'${PlagScanURL:http://3.7.84.108:8009/checkFileForm}'}")
	String PlagScanURL;

	@Value("${app}")
	private String app;

	@Value("${spring.datasource.url}")
	String defaultUrl;

	@Value("${database.username}")
	String defaultUsername;

	@Value("${database.password}")
	String defaultPassword;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Value("${lms.assignment.submissionFolderS3}")
	private String submissionFolder;

	@Value("${userMgmtCrudUrl}")
	private String userMgmtCrudUrlNew;

	private String res_Id;
	private String res_issuer;
	private String res_issueInstant;
	private String res_timeout;
	private String res_assertionId;
	private String res_nameId;
	private String res_notonorafter;
	private String res_notbefore;
	private String res_authnInstant; // Can be equal to issueInstance

	private String strResponseXML;
	private String strFinalResponse; // The final signed SAML Response.
	private Response samlResponseObject;
	private String tcsURL = "https://g21.tcsion.com/LX/INDEXES/ConsumeSAMLToken";
	private String miclURL = "https://qahf-micl.com/LX/INDEXES/ConsumeSAMLToken";

	private String tcsEntity = "https://www.tcsion.com/LX";
	private String miclEntity = "https://qahf-micl.com/LX";

	/*
	 * @Value("#{'${lms.libraryName}'}") String libraryName;
	 */
	Client client = ClientBuilder.newClient();

	@ModelAttribute("libraryName")
	public String getLibraryName(Principal principal) {
		String libraryName = userDAO.findLibraryName();
		httpSession.setAttribute("libraryName", libraryName);
		return libraryName;
	}

	@ModelAttribute("feedback")
	public Feedback getPendingStudentFeedback(Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;

			List<StudentFeedback> studentFeedbacks = studentFeedbackService.getPendingFeedbackByStudent(username);
			if (!studentFeedbacks.isEmpty()) {
				Feedback feedback = feedbackService.findByID(studentFeedbacks.get(0).getFeedbackId());
				feedback.setStudentFeedback(studentFeedbacks.get(0));
				feedback.setFeedbackQuestions(feedbackQuestionService.findByFeedbackId(feedback.getId()));
				return feedback;
			}
		}
		return null;

	}

	@RequestMapping("/login")
	public String login(Model m) {
		m.addAttribute("webPage", new WebPage("login", "Login", true, true, true, true, false));
		return "login";
	}

	@RequestMapping("/PlagScanURL")
	public ModelAndView PlagScanURL(HttpServletRequest request, Model m) {

		request.getSession().invalidate();

		SecurityContextHolder.clearContext();
		RedirectView view = new RedirectView(PlagScanURL);
		return new ModelAndView(view);
	}

	@RequestMapping("/loggedout")
	public ModelAndView logout(HttpServletRequest request, Model m, Principal p) {

		request.getSession().invalidate();

		if (p != null) {
			UserLoginOutTime userLoginOutDetails = new UserLoginOutTime();

			String outTimeText = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			userLoginOutDetails.setUsername(p.getName());
			userLoginOutDetails.setLogOutTimeText(outTimeText);
			userLoginOutDetails.setIsLoggedIn("N");
			updateUsersLogoutTime(userLoginOutDetails);
		}

		SecurityContextHolder.clearContext();
		RedirectView view = new RedirectView(logoutUrl);
		return new ModelAndView(view);
	}

	public void updateUsersLogoutTime(UserLoginOutTime user) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(user);
			// logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			// logger.info(" json--->" + (json));
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/updateUserLogoutTime?json=" + json));
			// logger.info("webTarget" + webTarget);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			// logger.info("resp" + resp);
		} catch (Exception ex) {

			logger.error("Exception", ex);
		}

	}

	/*
	 * @RequestMapping("/handShake") public String handShake(@RequestParam(name =
	 * "json") String json, HttpServletRequest request) { try { UserTo userTo =
	 * mapper.readValue(json, UserTo.class); Map<String, List<String>> appRoles =
	 * userTo.getAppRoles(); Token auth = new Token(userTo.getUsername(),
	 * userTo.getPassword(), Token.setAppRolesEnum(userTo.getReqApp(), appRoles));
	 * auth.setProgramName(userTo.getReqApp());
	 * 
	 * String progName = userTo.getReqApp(); List<Program> programs
	 * =programService.getProgramByName(progName); String programId = "";
	 * if(programs!=null && !programs.isEmpty()){ programId =
	 * programs.get(0).getId()+"";
	 * 
	 * auth.setProgramId(programId); }
	 * 
	 * logger.info("user auth in 8084 logincontroller " + auth.getAuthorities()); if
	 * (auth.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * logger.info(userTo.getUsername() + " is set student");
	 * auth.setMenuRights(userTo.getStudentRoles()); } else if
	 * (userTo.getMenuRoles() != null) { auth.setMenuRights(userTo.getMenuRoles()
	 * .get(userTo.getReqApp())); logger.info(userTo.getUsername() +
	 * " is menu roles"); }
	 * SecurityContextHolder.getContext().setAuthentication(auth); } catch
	 * (IOException e) { logger.info("Exception", e); return "error"; }
	 * 
	 * return "redirect:/homepage"; }
	 */

	// added 21/11/2017

	public User getUserLoginOutTime(String username) {
		WebTarget webTarget = null;
		logger.info("URL HIT ---->" + userRoleMgmtCrudUrl + "/getUserLogInOutTime?username=" + username);
		Invocation.Builder invocationBuilder = null;
		String resp = "";
		try {
			webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getUserLogInOutTime?username=" + username));
			invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			resp = invocationBuilder.get(String.class);

			if (!"null".equals(resp)) {
				User userTo = mapper.readValue(resp, User.class);

				return userTo;
			}
			// logger.info("resp from uname" + resp);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return null;
	}

	/*
	 * @RequestMapping("/handShake") public String handShake(@RequestParam(name =
	 * "json") String json, HttpServletRequest request) { try {
	 * 
	 * logger.info("JSON------------>" + json);
	 * 
	 * UserTo userTo = mapper.readValue(json, UserTo.class);
	 * 
	 * User getUserLogInOutTime = getUserLoginOutTime(userTo.getUsername());
	 * 
	 * if (getUserLogInOutTime == null) { return "redirect:/loggedout"; } else { if
	 * (getUserLogInOutTime.getIsLoggedIn() != null) {
	 * 
	 * if (getUserLogInOutTime.getIsLoggedIn().equals("Y")) { return
	 * "redirect:/loggedout";
	 * 
	 * } } if (getUserLogInOutTime.getLogOutTimeText() != null) { return
	 * "redirect:/loggedout"; } }
	 * 
	 * Map<String, List<String>> appRoles = userTo.getAppRoles(); Token auth = new
	 * Token(userTo.getUsername(), userTo.getPassword(),
	 * Token.setAppRolesEnum(userTo.getReqApp(), appRoles));
	 * auth.setProgramName(userTo.getReqApp());
	 * auth.setInstituteFlag(userTo.getInstituteFlag());
	 * auth.setCollegeName(userTo.getCollegeName());
	 * 
	 * if (userTo.getReqApp().equalsIgnoreCase("INTDR")) {
	 * 
	 * if (auth.getAuthorities().contains(Role.ROLE_ADMIN)) {
	 * 
	 * List<Course> allCourseList = courseService.findAllActive();
	 * auth.setCourseList(allCourseList); } else { List<Course>
	 * courseListByUserNameAndApp = courseService
	 * .findByUserActiveInterdesciplinary(userTo .getUsername());
	 * auth.setCourseList(courseListByUserNameAndApp); } } else {
	 * 
	 * if (auth.getAuthorities().contains(Role.ROLE_LIBRARIAN) ||
	 * auth.getAuthorities().contains(Role.ROLE_EXAM) ||
	 * auth.getAuthorities().contains(Role.ROLE_COUNSELOR) ||
	 * auth.getAuthorities().contains( Role.ROLE_SUPPORT_ADMIN) ||
	 * auth.getAuthorities().contains(Role.ROLE_STAFF) ||
	 * auth.getAuthorities().contains(Role.ROLE_EXAM_ADMIN) ||
	 * auth.getAuthorities().contains(Role.ROLE_IT)) {
	 * 
	 * auth.setCourseList(null); }
	 * 
	 * List<Course> courseListByUserNameAndApp = courseService
	 * .findByUserActive(userTo.getUsername(), auth.getProgramName());
	 * auth.setCourseList(courseListByUserNameAndApp);
	 * 
	 * String progName = userTo.getReqApp();
	 * 
	 * if (!auth.getAuthorities().contains(Role.ROLE_LIBRARIAN) ||
	 * !auth.getAuthorities().contains(Role.ROLE_EXAM) ||
	 * !auth.getAuthorities().contains(Role.ROLE_COUNSELOR) ||
	 * !auth.getAuthorities().contains( Role.ROLE_SUPPORT_ADMIN) ||
	 * !auth.getAuthorities().contains(Role.ROLE_STAFF) || !auth.getAuthorities()
	 * .contains(Role.ROLE_EXAM_ADMIN) ||
	 * !auth.getAuthorities().contains(Role.ROLE_IT)) { List<Program> programs =
	 * programService .getProgramByName(progName); String programId = ""; if
	 * (programs != null && !programs.isEmpty()) { programId =
	 * programs.get(0).getId() + "";
	 * 
	 * auth.setProgramId(programId); } } }
	 * 
	 * if (auth.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * 
	 * // auth.setCourseList(courseListByUserNameAndApp);
	 * auth.setMenuRights(userTo.getStudentRoles()); } else if
	 * (auth.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {
	 * 
	 * auth.setCourseList(null); auth.setMenuRights(userTo.getLibrarianRoles()); //
	 * auth.setMenuRights(userTo.getStudentRoles()); //
	 * logger.info("Student Roles-----------"+auth.getMenuRights()); } else if
	 * (auth.getAuthorities().contains(Role.ROLE_EXAM) ||
	 * auth.getAuthorities().contains(Role.ROLE_EXAM_ADMIN)) {
	 * 
	 * auth.setCourseList(null); auth.setMenuRights(userTo.getLibrarianRoles()); //
	 * auth.setMenuRights(userTo.getStudentRoles()); //
	 * logger.info("Student Roles-----------"+auth.getMenuRights()); } else if
	 * (auth.getAuthorities().contains(Role.ROLE_IT)) {
	 * 
	 * auth.setCourseList(null); auth.setMenuRights(userTo.getLibrarianRoles()); //
	 * auth.setMenuRights(userTo.getStudentRoles()); //
	 * logger.info("Student Roles-----------"+auth.getMenuRights()); } else if
	 * (auth.getAuthorities().contains(Role.ROLE_COUNSELOR)) {
	 * 
	 * auth.setCourseList(null); auth.setMenuRights(userTo.getLibrarianRoles()); //
	 * auth.setMenuRights(userTo.getStudentRoles()); //
	 * logger.info("Student Roles-----------"+auth.getMenuRights()); } else if
	 * (auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)) {
	 * 
	 * auth.setCourseList(null); auth.setMenuRights(userTo.getLibrarianRoles()); //
	 * auth.setMenuRights(userTo.getStudentRoles()); //
	 * logger.info("Student Roles-----------"+auth.getMenuRights()); } else if
	 * (auth.getAuthorities().contains(Role.ROLE_STAFF)) {
	 * 
	 * auth.setCourseList(null); auth.setMenuRights(userTo.getLibrarianRoles()); //
	 * auth.setMenuRights(userTo.getStudentRoles()); //
	 * logger.info("Student Roles-----------"+auth.getMenuRights()); } else if
	 * (userTo.getMenuRoles() != null) { auth.setMenuRights(userTo.getMenuRoles()
	 * .get(userTo.getReqApp()));
	 * 
	 * }
	 * 
	 * UserLoginOutTime userLoginOutDetails = new UserLoginOutTime();
	 * 
	 * userLoginOutDetails.setUsername(userTo.getUsername());
	 * userLoginOutDetails.setLogOutTimeText(null);
	 * userLoginOutDetails.setIsLoggedIn("Y");
	 * updateUsersLogoutTime(userLoginOutDetails);
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(auth); } catch
	 * (IOException e) { logger.info("Exception", e); return "error"; }
	 * 
	 * return "redirect:/homepage"; }
	 */

	// ends

	// new handshake
	@RequestMapping("/handShake")
	public String handShake(
				//@RequestParam(name = "json") String json,
				@RequestParam(name = "username") String username,
			@RequestParam(name="progId") String progId,
			HttpServletRequest request) {
		try {
			//Old
			/*
			logger.info("hand shake called");
			UserConverter userTo = mapper.readValue(json, UserConverter.class);
			logger.info("userTo" + userTo);
			User getUserLogInOutTime = getUserLoginOutTime(userTo.getUsername());
			*/
			//New Chnages
			
			logger.info("hand shake mapping called");
			List<String> keys = service.getKeys(username + progId+"*");
			String json = String.valueOf(service.getValue(keys.get(0)));
			logger.info("hand shake called");
			logger.info("Json is:"+json);
			UserConverter userTo = mapper.readValue(json, UserConverter.class);
			logger.info("userTo" + userTo);
			User getUserLogInOutTime = getUserLoginOutTime(userTo.getUsername());
			
	
			if (getUserLogInOutTime == null) {
				return "redirect:/loggedout";
			} else {
				if (getUserLogInOutTime.getIsLoggedIn() != null) {

					if (getUserLogInOutTime.getIsLoggedIn().equals("Y")) {
						return "redirect:/loggedout";

					}
				}
				if (getUserLogInOutTime.getLogOutTimeText() != null) {
					return "redirect:/loggedout";
				}
			}

			// Map<String, List<String>> appRoles = userTo.getAppRoles();
			List<String> roles = userTo.getRoles();
			Token auth = new Token(userTo.getUsername(), userTo.getPassword(),
					Token.setAppRoleListEnum(userTo.getReqApp(), userTo.getAppRoles().get(userTo.getReqApp())));
			auth.setProgramName(userTo.getReqApp());
			auth.setCollegeName(userTo.getCollegeName());
			auth.setInstituteFlag(userTo.getInstituteFlag());

			if (userTo.getReqApp().equals("TCSION")) {
				auth.setProgramId(String.valueOf(userTo.getProgramId()));
			}

			if (userTo.getReqApp().equalsIgnoreCase("INTDR")) {

				if (auth.getAuthorities().contains(Role.ROLE_ADMIN)) {

					List<Course> allCourseList = courseService.findAllActive();
					auth.setCourseList(allCourseList);
				} else {
					List<Course> courseListByUserNameAndApp = courseService
							.findByUserActiveInterdesciplinary(userTo.getUsername());
					auth.setCourseList(courseListByUserNameAndApp);
				}
			} else {

				if (auth.getAuthorities().contains(Role.ROLE_LIBRARIAN)
						|| auth.getAuthorities().contains(Role.ROLE_EXAM)
						|| auth.getAuthorities().contains(Role.ROLE_COUNSELOR)
						|| auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)
						|| auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN_REPORT)
						|| auth.getAuthorities().contains(Role.ROLE_STAFF)
						|| auth.getAuthorities().contains(Role.ROLE_INTL)
						|| auth.getAuthorities().contains(Role.ROLE_EXAM_ADMIN)
						|| auth.getAuthorities().contains(Role.ROLE_IT)) {

					auth.setCourseList(null);
				}

				List<Course> courseListByUserNameAndApp = courseService.findByUserActive(userTo.getUsername(),
						auth.getProgramName());
				auth.setCourseList(courseListByUserNameAndApp);

				String progName = userTo.getReqApp();

				if (!auth.getAuthorities().contains(Role.ROLE_LIBRARIAN)
						|| !auth.getAuthorities().contains(Role.ROLE_EXAM)
						|| !auth.getAuthorities().contains(Role.ROLE_COUNSELOR)
						|| !auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)
						|| !auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN_REPORT)
						|| !auth.getAuthorities().contains(Role.ROLE_STAFF)
						|| auth.getAuthorities().contains(Role.ROLE_INTL)
						|| !auth.getAuthorities().contains(Role.ROLE_EXAM_ADMIN)
						|| !auth.getAuthorities().contains(Role.ROLE_IT)) {
					List<Program> programs = programService.getProgramByName(progName);
					String programId = "";
					if (programs != null && !programs.isEmpty()) {
						programId = programs.get(0).getId() + "";

						auth.setProgramId(programId);
					}
				}
			}

			if (auth.getAuthorities().contains(Role.ROLE_STUDENT)) {

				// auth.setCourseList(courseListByUserNameAndApp);
				// auth.setMenuRights(userTo.getStudentRoles());
			} else if (auth.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_EXAM)
					|| auth.getAuthorities().contains(Role.ROLE_EXAM_ADMIN)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_IT)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_COUNSELOR)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN_REPORT)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_STAFF)) {

				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			} else if (auth.getAuthorities().contains(Role.ROLE_INTL)) {
				logger.info("null intl set");
				auth.setCourseList(null);
				// auth.setMenuRights(userTo.getLibrarianRoles());
				// auth.setMenuRights(userTo.getStudentRoles());
				// logger.info("Student Roles-----------"+auth.getMenuRights());
			}else if (userTo.getMenuRolesApp() != null) {
				auth.setMenuRights(userTo.getMenuRolesApp());

			}

			UserLoginOutTime userLoginOutDetails = new UserLoginOutTime();

			userLoginOutDetails.setUsername(userTo.getUsername());
			userLoginOutDetails.setLogOutTimeText(null);
			userLoginOutDetails.setIsLoggedIn("Y");
			updateUsersLogoutTime(userLoginOutDetails);

			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (IOException e) {
			logger.info("Exception", e);
			return "error";
		}

		return "redirect:/homepage";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/myCourseForm", method = RequestMethod.GET)
	public String myCourseForm(@ModelAttribute("user") User user, Model m, Principal principal) {

		String username = principal.getName();

		User u = userService.findByUserName(username);
		Token userDetails = (Token) principal;

		logger.info("courseList---->" + userDetails.getCourseList());

		HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();

		for (Course c : userDetails.getCourseList()) {

			if (!sessionWiseCourseList.containsKey(c.getAcadSession())) {
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			} else {
				List<Course> courseLst = sessionWiseCourseList.get(c.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}

		}

		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBean", u);

		return "course/myCourse";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping("/resetPasswordForm")
	public String resetPasswordForm(Model m) {
		m.addAttribute("webPage", new WebPage("forgot", "Forgot", true, true, true, true, false));
		return "forgot";
	}

	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String resetPassword(@RequestParam("userName") String userName, HttpServletRequest request, Model m)
			throws UnknownHostException {

		try {
			User user = userService.forgotPassword(userName);

			setSuccess(request, "Password reset successfully. A mail has been sent to " + user.getEmail()
					+ " containing the new password details.");
		} catch (ValidationException ex) {
			setError(request, ex.getMessage());
			m.addAttribute("webPage", new WebPage("forgot", "Forgot", true, true, true, true, false));
			return "forgot";
		}
		m.addAttribute("webPage", new WebPage("login", "Login", true, true, true, true, false));
		return "login";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/changePasswordFormStudentByAdmin", method = RequestMethod.GET)
	public String changePasswordFormStudentByAdmin(@ModelAttribute("user") User user, Model m) {
		m.addAttribute("webPage", new WebPage("changePassword", "Change Password", true, false));
		return "user/changePasswordForStudentByAdmin";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/changePasswordStudentByAdmin", method = RequestMethod.POST)
	public String changePasswordStudentByAdmin(@ModelAttribute("user") User user, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();

		User userFromUsermgmt = new User();

		try {

			WebTarget webTarget = client.target(URIUtil.encodeQuery(
					userRoleMgmtCrudUrl + "/getUserBeanFromUsermgmtForPassword?username=" + user.getUsername()));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			userFromUsermgmt = new Gson().fromJson(resp, User.class);

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		user.setUsername(user.getUsername());
		user.setLastModifiedBy(user.getUsername());
		user.setLastModifiedDate(Utils.getInIST());
		// logger.info("user old password --->" +
		// userFromUsermgmt.getPassword());
		user.setPassword(userFromUsermgmt.getPassword());

		// logger.info("Changing password " + user.getUsername());
		try {
			String regex = "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";
					   
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(user.getNewPassword());
			boolean isPassPerfect = m.matches();
			//boolean isPassPerfect = true;
			if(isPassPerfect == true) {
				userService.changePasswordForStudentByAdmin(user);
			} else {
				throw new ValidationException(
						"Unable to change the password. Password should have atleast 1 digit, 1 upper case alphabet, 1 lower case alphabet, 1 special character & atleast 8 characters and at most 20 characters!");
			}
		} catch (ValidationException ex) {
			setError(redirectAttrs, ex.getMessage());
			return "redirect:/changePasswordFormStudentByAdmin";
		}

		String json = new Gson().toJson(user);

		// logger.info("passed json--->" + json);
		try {
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/changePasswordForUser?json=" + json));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "Password changed successfully.");
			Boolean sucessStatus = false;
			Map<String, String> email = new HashMap<>();
			Map<String, String> mobile = new HashMap<>();
			if (userFromUsermgmt.getEmail() != null) {

				email.put(user.getUsername(), userFromUsermgmt.getEmail());

			}
			if (userFromUsermgmt.getMobile() != null) {
				mobile.put(user.getUsername(), userFromUsermgmt.getMobile());

			}
			sucessStatus = notifier.sendEmail(email, mobile,
					"New Password Set By Admin for You is: " + user.getNewPassword(),
					"New Password Set By Admin for You is: " + user.getNewPassword());

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}
		// setSuccess(redirectAttrs, "Password changed successfully.");
		return "redirect:/changePasswordFormStudentByAdmin";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/changePasswordForm", method = RequestMethod.GET)
	public String changePasswordForm(@ModelAttribute("user") User user, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("changePassword", "Change Password", true, false));

		Token userdetails1 = (Token) principal;

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "user/changePasswordAdmin";
		} else if (userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)
				|| userdetails1.getAuthorities().contains(Role.ROLE_COUNSELOR)
				|| userdetails1.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN_REPORT)) {
			return "user/changePasswordOld";
		}

		else {
			return "user/changePassword";
		}
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(@ModelAttribute("user") User user, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();

		User userFromUsermgmt = new User();
		try {

			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl + "/getUserBeanFromUsermgmtForPassword?username=" + username));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			userFromUsermgmt = new Gson().fromJson(resp, User.class);

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		user.setUsername(username);
		user.setLastModifiedBy(username);
		user.setLastModifiedDate(Utils.getInIST());
		user.setPassword(userFromUsermgmt.getPassword());

		/*
		 * try { String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" +
		 * "(?=.*[@#$%^&+=])" + "(?=\\S+$).{8,20}$";
		 * 
		 * Pattern p = Pattern.compile(regex); Matcher m =
		 * p.matcher(user.getNewPassword()); boolean isPassPerfect = m.matches();
		 * //boolean isPassPerfect=true; if(isPassPerfect == true) {
		 * userService.changePassword(user); } else { throw new ValidationException(
		 * "Unable to change the password. Password should have atleast 1 digit, 1 upper case alphabet, 1 lower case alphabet, 1 special character & atleast 8 characters and at most 20 characters!"
		 * ); }
		 * 
		 * } catch (ValidationException ex) { setError(redirectAttrs, ex.getMessage());
		 * return "redirect:/changePasswordForm"; }
		 */
		userService.changePassword(user); 
		String json = new Gson().toJson(user);

		// logger.info("passed json--->" + json);
		try {
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/changePasswordForUser?json=" + json));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "Password changed successfully.");

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}
		// setSuccess(redirectAttrs, "Password changed successfully.");
		return "redirect:/changePasswordForm";
	}

	// This is for login//
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = { "/", "/homepage" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String homepage(Model m, Principal principal) {
		String username = principal.getName();
		Token userDetails = (Token) principal;
		User u = userService.findByUserName(username);

		session.setAttribute("instiFlag", userDetails.getInstituteFlag());
		String acadSession = u.getAcadSession();
		httpSession.setAttribute("menuRights", userDetails.getMenuRights());

		List<DashBoard> listOfDashBoardElements = new ArrayList<DashBoard>();
		List<DashBoard> listOfDashBoardForCord = new ArrayList<DashBoard>();
		List<DashBoard> listOfDashBoardForAreaIncharge = new ArrayList<DashBoard>();
		List<DashBoard> listOfDashBoardForAR = new ArrayList<DashBoard>();
		List<DashBoard> listOfDashBoardForDean = new ArrayList<DashBoard>();
		User userBean = new User();
		User userBeanParent = new User();

		m.addAttribute("loggeedInApp", app);

		

		// Strt Server Notification
		List<Announcement> a = announcementService.getAnnouncementBySupportAdmin();

		if (a == null || a.equals("") || a.isEmpty()) {

			m.addAttribute("showModalS", "N");
		} else {

			m.addAttribute("showModalS", "Y");
			m.addAttribute("announcement", a.get(0));
		}
		// End

		/* Enable Tee */
		session.setAttribute("appNameForTee", app);

		/* Role of Faculty */

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			m.addAttribute("webPage", new WebPage("facultyHomePage", "Faculty HomePage", false, false));
//			m.addAttribute("courses", courseService.findByUserActive(username, userDetails.getProgramName()));
			// Course Details
			List<Course> courseDetails = courseService.findByUserActive(username, userDetails.getProgramName());
			m.addAttribute("courses", courseDetails);
			// ICA Details
			List<IcaBean> icaAllocatedByFaculty = icaBeanService.findIcaListByFacultyId(Role.ROLE_FACULTY.name(), principal.getName());
			m.addAttribute("icaListDashboard", icaAllocatedByFaculty);
			
			Map<String, List<DashBoard>> sessionWiseCourseListMap = new HashMap<>();
			Set<String> acadSessionList = new HashSet<>();
			for (Course c : courseDetails) {
				acadSessionList.add(c.getAcadSession() + "-" + c.getAcadYear());
			}

//			m.addAttribute("assignments", assignmentService.findByFaculty(username));
//			m.addAttribute("tests", testService.findByFaculty(username));
//			m.addAttribute("announcements", announcementService.findAnnouncementsByUser(username));
			listOfDashBoardElements = dashBoardService.listOfDashBoardElements(username, userDetails.getProgramName(), userDetails.getCourseList());

			for (String acadSesion : acadSessionList) {
				List<DashBoard> dashboardList = new ArrayList<>();
				for (DashBoard c : listOfDashBoardElements) {
					if (c.getCourse().getAcadSession() != null && c.getCourse().getAcadYear() != null) {
						String key = c.getCourse().getAcadSession() + "-" + c.getCourse().getAcadYear();
						if (key.equals(acadSesion)) {
							dashboardList.add(c);
						}
					}
				}
				sessionWiseCourseListMap.put(acadSesion, dashboardList);
			}

//			m.addAttribute("receivedMessage", dashBoardService.getReceivedMessage(username));
			m.addAttribute("sessionWiseCourseListMap", sessionWiseCourseListMap);

			userBean = userService.findByUserName(username);
			session.setAttribute("facultyType", userBean.getType());
			if (!userDetails.getProgramName().equalsIgnoreCase("INTDR")) {
				session.setAttribute("announcmentList", dashBoardService.listOfAnnouncementsForCourseList(username,
						null, acadSession, Long.valueOf(userDetails.getProgramId())));
			}
			session.setAttribute("userBean", userBean);
			session.setAttribute("announcementForTopMenu", dashBoardService.listOfAnnouncementsForCourseListMenu(username, null));
			session.setAttribute("courseDetailList", listOfDashBoardElements);
			session.setAttribute("sessionWiseCourseListMap", sessionWiseCourseListMap);

			if (userDetails.getProgramName().equalsIgnoreCase("INTDR")) {
				m.addAttribute("toDoDaily", dashBoardService.getToDoEveryday(username, Role.ROLE_FACULTY.name()));
			} else {
				m.addAttribute("toDoDaily", dashBoardService.getToDoEveryday(username, Role.ROLE_FACULTY.name(),
						Long.parseLong(userDetails.getProgramId())));
			}

			m.addAttribute("toDoList", dashBoardService.getToDoListByCourse(username, null));
			// Timetable todo List
			List<Timetable> timetableToDoList = studentAttendanceController.getPendingLecturesList(username);
			// m.addAttribute("timetableToDoList", timetableToDoList);
			session.setAttribute("timetableToDoList", timetableToDoList);
			// TImetable todo List
//			Announcement announcement = announcementService.getFacultyNotificationAnnouncement();
//
//			if (announcement != null) {
//				String notificationFaculty = announcement.getDescription();
//
//				String subjectFaculty = announcement.getSubject();
//
//				m.addAttribute("subjectFaculty", subjectFaculty);
//				m.addAttribute("notificationFaculty", notificationFaculty);
//
//			}
			
			//changed by hiren 07-05-2021
			int lorStaff = lorRegStaffService.getIsLorStaff(username);
			if(lorStaff > 0) {
				session.setAttribute("lorStaff", true);
			}else {
				session.setAttribute("lorStaff", false);
			}

			return "homepage/facultyHomePage";

		} else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

			m.addAttribute("webPage", new WebPage("adminHomePage", "Admin HomePage", false, false));
			m.addAttribute("courses", courseService.findAllActive());
			m.addAttribute("announcements", announcementService.findAll());
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);

			Announcement announcement = announcementService.getAdminNotificationAnnouncement();

			if (announcement != null) {
				String notificationAdmin = announcement.getDescription();

				String subjectAdmin = announcement.getSubject();

				m.addAttribute("subjectAdmin", subjectAdmin);
				m.addAttribute("notificationAdmin", notificationAdmin);

			}

			// Get CUSTOME SCHOOL LIST FOR MASTER VALIDATION FROM USERMGMT DB
			try {
				WebTarget webTarget4 = client
						.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getSchoolListForMasterValidation"));
				Invocation.Builder invocationBuilder4 = webTarget4.request(MediaType.APPLICATION_JSON);
				String resp4 = invocationBuilder4.get(String.class);
				logger.info("Response Comming from usermgmt schoolListMaster---------------------------->" + resp4);
				ObjectMapper objMapper = new ObjectMapper();
				List<String> schoolListMaster = objMapper.readValue(resp4, new TypeReference<List<String>>() {
				});
				logger.info("schoolListMaster  ---   11 ------------>" + schoolListMaster);
				// m.addAttribute("appName", app);
				// m.addAttribute("schoolListMaster",schoolListMaster);
				session.setAttribute("schoolListMaster", schoolListMaster);
				session.setAttribute("appName", app.trim());
			} catch (Exception e) {
				logger.info("Error while getting response from usermgmt");
				logger.error("Exception", e);
			}

			return "homepage/adminHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {

			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);

			m.addAttribute("webPage", new WebPage("adminHomePage", "Librarian HomePage", false, false));
			m.addAttribute("courses", courseService.findAllActive());
			m.addAttribute("announcements", announcementService.findAll());
			return "homepage/librarianHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)
				|| userDetails.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN_REPORT)) {
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			m.addAttribute("webPage", new WebPage("adminHomePage", "Support Admin HomePage", false, false));
			return "homepage/supportAdminHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STAFF)) {
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			m.addAttribute("webPage", new WebPage("adminHomePage", "Staff HomePage", false, false));
			return "homepage/lorStaffHomepage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_INTL)) {
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			m.addAttribute("webPage", new WebPage("adminHomePage", "Staff HomePage", false, false));
			return "homepage/intlAdminHomePage";
		}
		
		
		else  if (userDetails.getAuthorities().contains(Role.ROLE_HOD)) {
			m.addAttribute("webPage", new WebPage("facultyHomePage", "HOD HomePage", false, false));
			m.addAttribute("courses", courseService.findByUserActive(username, userDetails.getProgramName()));
			m.addAttribute("assignments",
					assignmentService.findAllAssignmentForHOD(Long.parseLong(userDetails.getProgramId())));
			m.addAttribute("tests", testService.findByFaculty(username));
			m.addAttribute("announcements",
					announcementService.findAnnouncmentsByProgramId(Long.parseLong(userDetails.getProgramId())));

			listOfDashBoardElements = dashBoardService.listOfDashBoardElements(username, userDetails.getProgramName(),
					userDetails.getCourseList());
			m.addAttribute("receivedMessage", dashBoardService.getReceivedMessage(username));
			userBean = userService.findByUserName(username);
			/*
			 * session.setAttribute("facultyType", userDAO.getFacultyType(username));
			 */
			session.setAttribute("announcmentList", dashBoardService.listOfAnnouncementsForCourseList(username, null,
					acadSession, Long.valueOf(userDetails.getProgramId())));
			session.setAttribute("userBean", userBean);
			session.setAttribute("announcementForTopMenu",
					dashBoardService.listOfAnnouncementsForCourseListMenu(username, null));
			session.setAttribute("courseDetailList", listOfDashBoardElements);
			m.addAttribute("toDoDaily", dashBoardService.getToDoEveryday(username, Role.ROLE_HOD.name(),
					Long.parseLong(userDetails.getProgramId())));
			m.addAttribute("toDoList", dashBoardService.getToDoListByCourse(username, null));

			return "homepage/hodHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_CORD)) {
			m.addAttribute("webPage", new WebPage("cordHomePage", "Co-ordinator HomePage", true, false));
			listOfDashBoardForCord = dashBoardService.listOfDashBoard();
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			session.setAttribute("courseDetailList", listOfDashBoardForCord);
			// return "homepage/cordHomePage";
			return "homepage/adminHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_AREA_INCHARGE)) {
			m.addAttribute("webPage", new WebPage("areaInchargeHomePage", "Area-Incharge HomePage", true, false));
			listOfDashBoardForAreaIncharge = dashBoardService.listOfDashBoard();
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			session.setAttribute("courseDetailList", listOfDashBoardForAreaIncharge);
			return "homepage/areaInchargeHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_AR)) {
			m.addAttribute("webPage", new WebPage("arHomePage", "AR HomePage", true, false));
			listOfDashBoardForAR = dashBoardService.listOfDashBoard();
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			session.setAttribute("courseDetailList", listOfDashBoardForAR);
			return "homepage/arHomePage";
		}

		else if (userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
			m.addAttribute("webPage", new WebPage("deanHomePage", "Dean HomePage", true, false));
			listOfDashBoardForDean = dashBoardService.listOfDashBoardElements(username);
			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);
			session.setAttribute("courseDetailList", listOfDashBoardForDean);
			return "homepage/deanHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_EXAM)
				|| userDetails.getAuthorities().contains(Role.ROLE_EXAM_ADMIN)) {

			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);

			m.addAttribute("webPage", new WebPage("adminHomePage", "Exam HomePage", false, false));
			m.addAttribute("courses", courseService.findAllActive());
			m.addAttribute("announcements", announcementService.findAll());
			return "homepage/examHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_IT)) {

			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);

			m.addAttribute("webPage", new WebPage("adminHomePage", "IT HomePage", false, false));
			m.addAttribute("courses", courseService.findAllActive());
			m.addAttribute("announcements", announcementService.findAll());
			return "homepage/ITHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_COUNSELOR)) {

			userBean = userService.findByUserName(username);
			session.setAttribute("userBean", userBean);

			m.addAttribute("webPage", new WebPage("adminHomePage", "Counselor HomePage", false, false));
			m.addAttribute("courses", courseService.findAllActive());
			m.addAttribute("announcements", announcementService.findAll());
			return "homepage/counselorHomePage";
		} else 
//		} /*
//			 * else if (userDetails.getAuthorities().contains(Role.ROLE_STAFF)) {
//			 * 
//			 * userBean = userService.findByUserName(username);
//			 * session.setAttribute("userBean", userBean);
//			 * 
//			 * m.addAttribute("webPage", new WebPage("adminHomePage", "Staff HomePage",
//			 * false, false)); // m.addAttribute("courses", courseService.findAllActive());
//			 * // m.addAttribute("announcements", announcementService.findAll()); return
//			 * "homepage/staffHomePage"; }
//			 */
		
		//22-01-2021
		
			if (userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
			m.addAttribute("webPage", new WebPage("adminHomePage", "Dean HomePage", false, false));
			m.addAttribute("courses", courseService.findAllActive());
			m.addAttribute("announcements", announcementService.findAll());
			return "homepage/deanHomePage";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_PARENT)) {
			m.addAttribute("webPage", new WebPage("parentHomePage", "Parents Home Page", false, false));
			List<StudentParent> studentParent = studentParentService.findStudentsByParentUname(username);

			m.addAttribute("studentsList", studentParent);
			if (studentParent != null && !studentParent.isEmpty()) {
				String studUsername = studentParent.get(0).getStud_username();
				userBean = userService.findByUserName(username);
				userBeanParent = userService.findByUserName(studUsername);
				logger.info("acadSession----->P" + userBeanParent.getAcadSession());
				session.setAttribute("announcmentList", dashBoardService.listOfAnnouncementsForCourseList(studUsername,
						null, userBeanParent.getAcadSession(), Long.valueOf(userDetails.getProgramId())));
				session.setAttribute("announcementForTopMenu", dashBoardService.listOfAnnouncementsForCourseList(
						studUsername, null, userBeanParent.getAcadSession(), Long.valueOf(userDetails.getProgramId())));

				m.addAttribute("toDoDaily", dashBoardService.getToDoEveryday(studUsername, Role.ROLE_STUDENT.name(),
						Long.parseLong(userDetails.getProgramId())));
				session.setAttribute("toDoDaily", dashBoardService.getToDoEveryday(studUsername,
						Role.ROLE_STUDENT.name(), Long.parseLong(userDetails.getProgramId())));
				// m.addAttribute("toDoList",
				// dashBoardService.getToDoList(studUsername));

				m.addAttribute("toDoList", dashBoardService.getToDoListByCourse(studUsername, null));
				session.setAttribute("toDoList", dashBoardService.getToDoListByCourse(studUsername, null));
				m.addAttribute("receivedMessage", dashBoardService.getReceivedMessage(studUsername));
				logger.info("usernameStud----->" + studUsername);
				List<Course> courseList = courseService.findByUser(studUsername);
				logger.info("courses----->" + courseList.get(0).getCourseName());
				/*
				 * m.addAttribute("courseList", courseList); m.addAttribute("userCourse", new
				 * UserCourse());
				 */
				listOfDashBoardElements = dashBoardService.listOfDashBoardElements(studUsername,
						userDetails.getProgramName(), courseList);

				Map<String, List<DashBoard>> sessionWiseCourseListMap = new HashMap<>();
				Set<String> acadSessionList = new HashSet<>();
				List<String> acadSessLst = new ArrayList();
				for (Course c : courseList) {
					acadSessionList.add(c.getAcadSession() + "-" + c.getAcadYear());
					if (!acadSessLst.contains(c.getAcadSession())) {
						acadSessLst.add(c.getAcadSession());
					}
				}

				for (String acadSesion : acadSessionList) {
					List<DashBoard> dashboardList = new ArrayList<>();
					for (DashBoard c : listOfDashBoardElements) {

						if (c.getCourse().getAcadSession() != null) {
							String key = c.getCourse().getAcadSession() + "-" + c.getCourse().getAcadYear();
							if (key.equals(acadSesion)) {

								dashboardList.add(c);

							}
						}
					}

					sessionWiseCourseListMap.put(acadSesion, dashboardList);
				}

				m.addAttribute("courseDetailList", listOfDashBoardElements);

				m.addAttribute("userBean", userBean);
				m.addAttribute("userBeanParent", userBeanParent);
				session.setAttribute("courseDetailList", listOfDashBoardElements);
				session.setAttribute("sessionWiseCourseListMap", sessionWiseCourseListMap);
				logger.info("acadSessLst-->" + acadSessLst);
				logger.info("parentRole-->" + userDetails.getAuthorities());
				m.addAttribute("acadSessLst", acadSessLst);
				session.setAttribute("userBean", userBean);

			}

			return "homepage/dashboard";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			userBean = userService.findByUserName(username);
			// Show Evaluation Report Button
			File files = new File(downloadResultPath + "/" + "Fyjc" + "/" + username + ".PDF");
			if (files.exists()) {
				m.addAttribute("downloadAvailable", "yes");
			} else {
				m.addAttribute("downloadAvailable", "no");
			}
			// Show Evaluation Report Button
			
			// TCS ION DEFAULT HOMEPAGE

			if ("TCSION".equals(userDetails.getProgramName())) {
				logger.info("userBean-->" + userBean);
				m.addAttribute("userBean", userBean);
				session.setAttribute("userBean", userBean);

				m.addAttribute("app", app);
				String app1 = app.trim();
				try {
					WebTarget webTarget = null;
					Invocation.Builder invocationBuilder;
					String resp = null;
					ObjectMapper mapper = new ObjectMapper();
					if (app1 != null) {

						// String json = mapper.writeValueAsString(app1);
						webTarget = client
								.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getOrgIdOfTcs?school=" + app1));
					}

					invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
					resp = invocationBuilder.get(String.class);
					String res = mapper.readValue(resp, String.class);
					String orgId = res;
					if (orgId != null) {
						if (app1.contains("-")) {
							app1 = app1.replace("-", "");
						}
						m.addAttribute("tcsLink", true);
						m.addAttribute("orgId", orgId);
						m.addAttribute("appName", app1);
					} else {
						m.addAttribute("tcsLink", false);
					}
				} catch (Exception e) {
					logger.error("Exception", e);
				}

				Announcement announcement = announcementService.getStudentNotificationAnnouncement();

				if (announcement != null) {

					String notificationStudent = announcement.getDescription();

					String subjectStudent = announcement.getSubject();

					m.addAttribute("subjectStudent", subjectStudent);
					m.addAttribute("notificationStudent", notificationStudent);

				}
				
				logger.info("TCS ION ENTERED");
				return "homepage/failStudentHomepage";
			}

			// Announcement
			Announcement announcement = announcementService.getStudentNotificationAnnouncement();

			if (announcement != null) {

				String notificationStudent = announcement.getDescription();

				String subjectStudent = announcement.getSubject();

				m.addAttribute("subjectStudent", subjectStudent);
				m.addAttribute("notificationStudent", notificationStudent);

			}
			// Announcement

			listOfDashBoardElements = dashBoardService.listOfDashBoardElements(username, userDetails.getProgramName(),
					userDetails.getCourseList());

			Map<String, List<DashBoard>> sessionWiseCourseListMap = new HashMap<>();
			Set<String> acadSessionList = new HashSet<>();
			List<String> acadSessLst = new ArrayList();
			for (Course c : userDetails.getCourseList()) {
				acadSessionList.add(c.getAcadSession() + "-" + c.getAcadYear());
				if (!acadSessLst.contains(c.getAcadSession())) {
					acadSessLst.add(c.getAcadSession());
				}
			}

			for (String acadSesion : acadSessionList) {
				List<DashBoard> dashboardList = new ArrayList<>();
				for (DashBoard c : listOfDashBoardElements) {

					if (c.getCourse().getAcadSession() != null) {
						String key = c.getCourse().getAcadSession() + "-" + c.getCourse().getAcadYear();
						if (key.equals(acadSesion)) {

							dashboardList.add(c);

						}
					}
				}

				sessionWiseCourseListMap.put(acadSesion, dashboardList);
			}

			m.addAttribute("courseDetailList", listOfDashBoardElements);
			if (!userDetails.getProgramName().equals("INTDR")) {
				session.setAttribute("announcmentList", dashBoardService.listOfAnnouncementsForCourseList(username,
						null, acadSession, Long.valueOf(userDetails.getProgramId())));
			}
			
			isMasterDataValidationEnabled(username, m);
			
			m.addAttribute("userBean", userBean);
			session.setAttribute("courseDetailList", listOfDashBoardElements);
			session.setAttribute("sessionWiseCourseListMap", sessionWiseCourseListMap);
			m.addAttribute("acadSessLst", acadSessLst);
			session.setAttribute("userBean", userBean);
			m.addAttribute("receivedMessage", dashBoardService.getReceivedMessage(username));

			if (userDetails.getProgramName().equals("INTDR")) {
				List<PendingTask> toDoDaily = dashBoardService.getToDoEveryday(username, Role.ROLE_STUDENT.name());
				m.addAttribute("toDoDaily", toDoDaily);
				session.setAttribute("toDoDaily", toDoDaily);
			} else {
				List<PendingTask> toDoDaily = dashBoardService.getToDoEveryday(username, Role.ROLE_STUDENT.name(),
						Long.parseLong(userDetails.getProgramId()));
				m.addAttribute("toDoDaily", toDoDaily);
				session.setAttribute("toDoDaily", toDoDaily);
			}
			List<Calender> toDoList = dashBoardService.getToDoListByCourse(username, null);
			m.addAttribute("toDoList", toDoList);
			session.setAttribute("toDoList", toDoList);

			// new method
			if (!userDetails.getProgramName().equalsIgnoreCase("INTDR")) {
				session.setAttribute("announcementForTopMenu", dashBoardService.listOfAnnouncementsForCourseList(
						username, null, acadSession, Long.valueOf(userDetails.getProgramId())));
			}
			List<Webpages> articlesList = webpagesService.findAvailArticles();

			m.addAttribute("articlesList", articlesList);
			m.addAttribute("app", app);
			String app1 = app.trim();
			try {
				WebTarget webTarget = null;
				Invocation.Builder invocationBuilder;
				String resp = null;
				ObjectMapper mapper = new ObjectMapper();
				if (app1 != null) {

					// String json = mapper.writeValueAsString(app1);
					webTarget = client
							.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getOrgIdOfTcs?school=" + app1));
				}

				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				resp = invocationBuilder.get(String.class);
				String res = mapper.readValue(resp, String.class);
				String orgId = res;
				if (orgId != null) {
					if (app1.contains("-")) {
						app1 = app1.replace("-", "");
					}
					m.addAttribute("tcsLink", true);
					m.addAttribute("orgId", orgId);
					m.addAttribute("appName", app1);
				} else {
					m.addAttribute("tcsLink", false);
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}
			session.setAttribute("studentFeedbackActive", "N");
			LmsVariables lmsVar = lmsVariablesService.getLmsVariableBykeyword("compulsoryFeedback");

			if (null != lmsVar) {
				if ("Y".equals(lmsVar.getValue())) {
					List<StudentFeedback> sfList = studentFeedbackService.findStartedFeedbackByUsername(username);
					if (sfList.size() > 0) {
						session.setAttribute("studentFeedbackActive", "Y");
						return "redirect:/viewFeedbackDetails";
					} else {
						session.setAttribute("studentFeedbackActive", "N");
					}
				}
			}

			if ("ASMSOC".equals(app)) {

				String outTimeText = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());

				List<StudentTest> studentTestOfCurrentDate = studentTestService
						.findAllTestOfCurrentDateByStudent(username, outTimeText);

				if (studentTestOfCurrentDate.size() > 0) {
					return "redirect:/viewTestFinal";
				} else {
					return "homepage/dashboard";
				}
			} else {
				return "homepage/dashboard";
			}

		}

		return "homepage/studentHomePage";
	}

	/* FOR USER PROFILE DATA */
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/profileDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String profileDetails(Model m, @ModelAttribute User user, Principal principal) {
		m.addAttribute("webPage", new WebPage("user", "View Profile Details", true, true, true, true, false));
		String username = principal.getName();
		Token userDetails = (Token) principal;
		List<User> pdetails = userService.getUserDetails(username);
		for (User ud : pdetails) {
			m.addAttribute("programName", userDetails.getProgramName());
			user.setUsername(username);
			user.setFirstname(ud.getFirstname());
			user.setLastname(ud.getLastname());
			user.setMobile(ud.getMobile());
			user.setMotherName(ud.getMotherName());
			user.setProgramId(ud.getProgramId());
			user.setFatherName(ud.getFatherName());
			user.setEmail(ud.getEmail());
		}
		m.addAttribute("pdetails", pdetails);
		m.addAttribute("user", user);

		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "user/newAdminProfileDetails";
		} else if (userDetails.getAuthorities().contains(Role.ROLE_LIBRARIAN)
				|| userDetails.getAuthorities().contains(Role.ROLE_COUNSELOR)
				|| userDetails.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN_REPORT)) {
			return "user/oldProfile";
		}

		else {
			return "user/newProfileDetails";
		}
	}

	@RequestMapping(value = { "/getCourseByUsernameForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByUsernameForApp(@RequestBody User users, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {

			boolean auth = isUserAuthorized(headers.get("token"), users.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);
				json = encryptResponseBody(json);
				return json;
			}
			String username = users.getUsername();
			long programId = users.getProgramId();
			User user = userService.findByUserName(username);
			if (user != null) {
				UserRole roles = userRoleService.findRoleByUsername(username);
				List<Role> role = new ArrayList<>();
				role.add(roles.getRole());
				user.setRoles(role);
				List<Course> courseList = courseService.findCoursesByUserForApp(username);
				String json = new Gson().toJson(courseList);
				// logger.info("Course List ------->" + json);
				return json;
			} else {
				return "{}";
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{}";
		}
	}

	@RequestMapping(value = { "/getProgramsByUsernameForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getProgramsByUsernameForApp(@RequestBody User users, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {
			boolean auth = isUserAuthorized(headers.get("token"), users.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);
				json = encryptResponseBody(json);
				return json;
			}
			String username = users.getUsername();
			User user = userService.findByUserName(username);
			if (user != null) {
				UserRole roles = userRoleService.findRoleByUsername(username);
				List<Role> role = new ArrayList<>();
				role.add(roles.getRole());
				user.setRoles(role);
				List<Course> programList = courseService.findProgramsByUserForApp(username);
				String json = new Gson().toJson(programList);
				return json;
			} else {
				return "{}";
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{}";
		}
	}

	@RequestMapping(value = { "/getCourseByUsernameAndProgramForApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getCourseByUsernameAndProgramForApp(@RequestBody User users, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {
			boolean auth = isUserAuthorized(headers.get("token"), users.getUsername());
			logger.info("HttpHeadersToken ==> " + users.getUsername());
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);
				json = encryptResponseBody(json);
				return json;
			}
			String username = users.getUsername();
			long programId = users.getProgramId();
			User user = userService.findByUserName(username);
			if (user != null) {
				UserRole roles = userRoleService.findRoleByUsername(username);
				List<Role> role = new ArrayList<>();
				role.add(roles.getRole());
				user.setRoles(role);
				List<Course> courseList = courseService.findCoursesByUserAndProgramIdForApp(username, programId);
				String json = new Gson().toJson(courseList);
				return json;
			} else {
				return "{}";
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{}";
		}
	}

	@RequestMapping(value = { "/getStudentsByCourseForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getStudentsByCourseForApp(@RequestBody Course course, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {
			boolean auth = isUserAuthorized(headers.get("token"), course.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);
				json = encryptResponseBody(json);
				return json;
			}
			long courseId = course.getId();
			Course courseDetails = courseService.findByID(courseId);
			if (courseDetails != null) {
				List<Course> courseList = courseService.findStudentsByCourseIdForApp(courseId);
				String json = new Gson().toJson(courseList);
				return json;
			} else {
				return "{}";
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{}";
		}
	}

	@RequestMapping(value = { "/insertStudentAttendanceForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String insertStudentAttendanceForApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {
			boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			List<Course> StudentList = courseService
					.findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttendance.getCourseId()));
			logger.info("flag--------->" + studentCourseAttendance.getFlag());
			String courseId = studentCourseAttendance.getCourseId();
			String eventId = courseId.substring(0, 8);
			String programId = courseId.substring(8);
			String username = "", acadSession = "", acadYear = "", startTime = "", endTime = "";
			List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();

			logger.info("----->" + studentCourseAttendance.getListofAbsStud());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = Utils.getInIST();

			startTime = dateFormat.format(date) + " " + studentCourseAttendance.getStartTime();
			endTime = dateFormat.format(date) + " " + studentCourseAttendance.getEndTime();

			for (Course cr : StudentList) {
				User u = userService.findByUserName(cr.getUsername());

				StudentCourseAttendance sca = new StudentCourseAttendance();

				sca.setCourseId(studentCourseAttendance.getCourseId());
				sca.setEventId(eventId);
				sca.setProgramId(programId);

				username = u.getUsername();
				sca.setUsername(u.getUsername());
				sca.setRollNo(u.getRollNo());
				sca.setFacultyId(studentCourseAttendance.getFacultyId());
				sca.setCreatedBy(studentCourseAttendance.getFacultyId());
				sca.setLastModifiedBy(studentCourseAttendance.getFacultyId());
				sca.setNoOfLec(studentCourseAttendance.getNoOfLec());
				sca.setFlag(studentCourseAttendance.getFlag());

				acadSession = u.getAcadSession();
				sca.setAcadSession(u.getAcadSession());
				sca.setActive("Y");
				acadYear = u.getEnrollmentYear();
				sca.setAcadYear(u.getEnrollmentYear());
				sca.setOrganization(app);

				sca.setStartTime(startTime);
				sca.setEndTime(endTime);

				logger.info("----->" + studentCourseAttendance.getListofAbsStud());

				if (null != studentCourseAttendance.getListofAbsStud()
						|| !studentCourseAttendance.getListofAbsStud().equals("")) {
					List<String> absentUsers = Arrays
							.asList(studentCourseAttendance.getListofAbsStud().split("\\s*,\\s*"));

					logger.info("----->" + absentUsers);

					if (absentUsers.contains(u.getUsername())) {

						sca.setStatus("Absent");

					} else {
						sca.setStatus("Present");
					}
				} else {
					sca.setStatus("Present");
				}
				studentCourseAttdList.add(sca);

			}

			if (studentCourseAttendance.getListofAbsStud().equals(null)
					|| studentCourseAttendance.getListofAbsStud().equals("")) {

				StudentCourseAttendance sca1 = new StudentCourseAttendance();

				sca1.setCourseId(studentCourseAttendance.getCourseId());
				sca1.setEventId(eventId);
				sca1.setProgramId(programId);

				sca1.setFacultyId(studentCourseAttendance.getFacultyId());
				sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
				sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
				sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
				sca1.setFlag(studentCourseAttendance.getFlag());

				sca1.setAcadSession(acadSession);
				sca1.setAcadYear(acadYear);
				sca1.setOrganization(app);
				sca1.setStartTime(startTime);
				sca1.setEndTime(endTime);
				sca1.setActive("Y");
				sca1.setDelFlag("N");
				sca1.setStatus("Present");

				studentCourseAttdList.add(sca1);

			}

			studentCourseAttendanceService.insertBatch(studentCourseAttdList);

			return "{\"Status\":\"Success\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";

		}

	}

	@RequestMapping(value = { "/updateStudentAttendanceForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateStudentAttendanceForApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {
			boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			List<Course> StudentList = courseService
					.findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttendance.getCourseId()));
			logger.info("flag--------->" + studentCourseAttendance.getFlag());
			String courseId = studentCourseAttendance.getCourseId();
			String eventId = courseId.substring(0, 8);
			String programId = courseId.substring(8);
			String username = "", acadSession = "", acadYear = "", startTime = "", endTime = "";
			List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();

			logger.info("----->" + studentCourseAttendance.getListofAbsStud());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = Utils.getInIST();

			startTime = dateFormat.format(date) + " " + studentCourseAttendance.getStartTime();
			endTime = dateFormat.format(date) + " " + studentCourseAttendance.getEndTime();

			for (Course cr : StudentList) {
				User u = userService.findByUserName(cr.getUsername());

				StudentCourseAttendance sca = new StudentCourseAttendance();

				sca.setCourseId(studentCourseAttendance.getCourseId());
				sca.setEventId(eventId);
				sca.setProgramId(programId);

				username = u.getUsername();
				sca.setUsername(u.getUsername());
				sca.setRollNo(u.getRollNo());
				sca.setFacultyId(studentCourseAttendance.getFacultyId());
				sca.setCreatedBy(studentCourseAttendance.getFacultyId());
				sca.setLastModifiedBy(studentCourseAttendance.getFacultyId());
				sca.setNoOfLec(studentCourseAttendance.getNoOfLec());
				sca.setFlag(studentCourseAttendance.getFlag());

				acadSession = u.getAcadSession();
				sca.setAcadSession(u.getAcadSession());
				sca.setActive("Y");
				acadYear = u.getEnrollmentYear();
				sca.setAcadYear(u.getEnrollmentYear());
				sca.setOrganization(app);

				sca.setStartTime(startTime);
				sca.setEndTime(endTime);

				logger.info("----->" + studentCourseAttendance.getListofAbsStud());

				if (null != studentCourseAttendance.getListofAbsStud()
						|| !studentCourseAttendance.getListofAbsStud().equals("")) {
					List<String> absentUsers = Arrays
							.asList(studentCourseAttendance.getListofAbsStud().split("\\s*,\\s*"));

					logger.info("----->" + absentUsers);

					if (absentUsers.contains(u.getUsername())) {

						sca.setStatus("Absent");

					} else {
						sca.setStatus("Present");
					}
				} else {
					sca.setStatus("Present");
				}
				studentCourseAttdList.add(sca);

			}

			studentCourseAttendanceService.updateBatch(studentCourseAttdList);

			StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(courseId, startTime,
					endTime, studentCourseAttendance.getFacultyId());

			if (studentCourseAttendance.getListofAbsStud().equals(null)
					|| studentCourseAttendance.getListofAbsStud().equals("")) {

				if (stca != null) {

					// update
					studentCourseAttendanceService.updateDelFlag("N", stca.getId());

				} else {

					// insert

					StudentCourseAttendance sca1 = new StudentCourseAttendance();

					sca1.setCourseId(studentCourseAttendance.getCourseId());
					sca1.setEventId(eventId);
					sca1.setProgramId(programId);

					sca1.setFacultyId(studentCourseAttendance.getFacultyId());
					sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
					sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
					sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
					sca1.setFlag(studentCourseAttendance.getFlag());

					sca1.setAcadSession(acadSession);
					sca1.setAcadYear(acadYear);
					sca1.setOrganization(app);
					sca1.setStartTime(startTime);
					sca1.setEndTime(endTime);
					sca1.setActive("Y");
					sca1.setDelFlag("N");
					sca1.setStatus("Present");

					studentCourseAttendanceService.insert(sca1);

				}

			} else {

				if (stca != null) {

					// update
					studentCourseAttendanceService.updateDelFlag("Y", stca.getId());

				}

			}

			return "{\"Status\":\"Success\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";

		}

	}

	/*
	 * @RequestMapping(value = { "/insertStudentAttendanceForApp" }, method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * insertStudentAttendanceForApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { try {
	 * 
	 * List<Course> StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(studentCourseAttd.getCourseId())); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId =
	 * studentCourseAttd.getCourseId(); String eventId = courseId.substring(0, 8);
	 * String programId = courseId.substring(8); String username = "", acadSession =
	 * "", acadYear = "", startTime = "", endTime = "";
	 * List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * ZATTSTPORTALUPDATEWS ws = new ZATTSTPORTALUPDATEWS(); ZattStPortalTt lstLT =
	 * new ZattStPortalTt();
	 * 
	 * SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * startTime = dateFormat.format(date) + " " + studentCourseAttd.getStartTime();
	 * endTime = dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * Date date1 = format.parse(startTime); Date date3 = new Date();
	 * 
	 * SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd"); String
	 * strtDateStr = formatDate.format(date1); String logDate =
	 * formatDate.format(date3);
	 * 
	 * SimpleDateFormat formatDate1 = new SimpleDateFormat("hh:mm:ss"); String
	 * logTime = formatDate1.format(date3);
	 * 
	 * XMLGregorianCalendar xmlGregCal = DatatypeFactory.newInstance()
	 * .newXMLGregorianCalendar(studentCourseAttd.getStartTime());
	 * 
	 * XMLGregorianCalendar xmlGregCal1 = DatatypeFactory.newInstance()
	 * .newXMLGregorianCalendar(studentCourseAttd.getEndTime());
	 * 
	 * XMLGregorianCalendar xmlGregCal2 = DatatypeFactory.newInstance()
	 * .newXMLGregorianCalendar(logTime);
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * 
	 * acadYear = u.getEnrollmentYear(); sca.setAcadYear(u.getEnrollmentYear());
	 * sca.setOrganization(app);
	 * 
	 * sca.setStartTime(startTime); sca.setEndTime(endTime); String studentObjId =
	 * "null"; logger.info("----->" + studentCourseAttd.getListofAbsStud()); if
	 * (null != studentCourseAttd.getListofAbsStud()) { List<String> absentUsers =
	 * Arrays.asList(studentCourseAttd .getListofAbsStud().split("\\s*,\\s*"));
	 * 
	 * logger.info("----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) { sca.setStatus("Absent");
	 * WebTarget webTarget12 = client .target(URIUtil
	 * .encodeQuery(userRoleMgmtCrudUrl + "/getStudentObjIdByUsername?username=" +
	 * u.getUsername())); Invocation.Builder invocationBuilder12 = webTarget12
	 * .request(MediaType.APPLICATION_JSON);
	 * 
	 * studentObjId = invocationBuilder12.get(String.class);
	 * sca.setStudentObjId(studentObjId); logger.info("stoID---> " + studentObjId);
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * if (!studentObjId.equals("null")) { ZattStPortalLt lst = new
	 * ZattStPortalLt(); lst.setOrgunit(app); lst.setProgId(programId);
	 * lst.setAyear(acadYear); lst.setAsession(acadSession);
	 * lst.setEventId(eventId); lst.setEventDate(strtDateStr);
	 * lst.setStartTime(xmlGregCal); lst.setEndTime(xmlGregCal1);
	 * lst.setFacultyId(studentCourseAttd.getFacultyId());
	 * lst.setStObjid(studentObjId); lst.setStNumber(u.getUsername());
	 * lst.setFlag(studentCourseAttd.getFlag()); lst.setLogDate(logDate);
	 * lst.setLogTime(xmlGregCal2);
	 * 
	 * lstLT.getItem().add(lst);
	 * 
	 * logger.info("list------>" + lst); } }
	 * 
	 * studentCourseAttendanceService.insertBatch(studentCourseAttdList);
	 * 
	 * if (null != lstLT) {
	 * 
	 * logger.info("list TT------>" + lstLT);
	 * 
	 * try {
	 * 
	 * String response = ws.getZATTSTPORTALUPDATEBIN() .zattStPortalUpdate(lstLT);
	 * 
	 * logger.info("response:----> " + response); } catch (Exception e) {
	 * 
	 * logger.error("Exception while calling a webservice", e);
	 * 
	 * } } return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * }
	 * 
	 * }
	 * 
	 * @RequestMapping(value = { "/updateStudentAttendanceForApp" }, method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * updateStudentAttendanceForApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { try {
	 * 
	 * List<Course> StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(studentCourseAttd.getCourseId())); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId =
	 * studentCourseAttd.getCourseId(); String eventId = courseId.substring(0, 8);
	 * String programId = courseId.substring(8); String username = "", acadSession =
	 * "", acadYear = "", startTime = "", endTime = "";
	 * List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * startTime = dateFormat.format(date) + " " + studentCourseAttd.getStartTime();
	 * endTime = dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId); username = u.getUsername();
	 * sca.setUsername(u.getUsername()); sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag()); acadSession = u.getAcadSession();
	 * sca.setAcadSession(u.getAcadSession()); acadYear = u.getEnrollmentYear();
	 * sca.setAcadYear(u.getEnrollmentYear()); sca.setOrganization(app);
	 * 
	 * sca.setStartTime(startTime); sca.setEndTime(endTime); logger.info("----->" +
	 * studentCourseAttd.getListofAbsStud()); if (null !=
	 * studentCourseAttd.getListofAbsStud()) { List<String> absentUsers =
	 * Arrays.asList(studentCourseAttd .getListofAbsStud().split("\\s*,\\s*"));
	 * 
	 * logger.info("----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) { sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.updateBatch(studentCourseAttdList);
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * }
	 * 
	 * }
	 */

	@RequestMapping(value = { "/showStudentAttendanceForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showStudentAttendanceForApp(@RequestBody StudentCourseAttendance studentCourseAttd,
			HttpServletResponse resp,@RequestHeader Map<String, String> headers) {
		try {
			studentCourseAttd = (StudentCourseAttendance) decryptRequestBody(
					studentCourseAttd.getEncrypted_key(), "StudentCourseAttendance");
			boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttd.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");
			// Date d = new Date(studentCourseAttd.getAttdDate());]
			Date d = dateFormatApp.parse(studentCourseAttd.getAttdDate());

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startTime = dateFormat.format(d) + " " + studentCourseAttd.getStartTime();
			String endTime = dateFormat.format(d) + " " + studentCourseAttd.getEndTime();

			List<StudentCourseAttendance> SCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
					studentCourseAttd.getCourseId(), startTime, endTime, studentCourseAttd.getFacultyId());

			String json = new Gson().toJson(SCAList);
			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			String json = "{\"Status\":\"Fail\"}";
			json = encryptResponseBody(json);
			return json;
		}

	}

	/*
	 * @RequestMapping(value = { "/getTimetableByCourseForApp" }, method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * getTimetableByCourseForApp(
	 * 
	 * @RequestBody Course course, HttpServletResponse resp) { try {
	 * 
	 * // logger.info("Course----->"+course);
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "timetable_metadata");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String cid = course.getId().toString();
	 * 
	 * long eventId = Long.parseLong(cid.substring(0, 8)); long pgrmId =
	 * Long.parseLong(cid.substring(8)); long username =
	 * Long.parseLong(course.getUsername());
	 * 
	 * Date dt = new Date(); SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("dd-MM-yyyy"); String curDate = "%" + dateFormat.format(dt)
	 * + "%";
	 * 
	 * logger.info("current Date ------->" + curDate);
	 * logger.info("course Id ------->" + cid); logger.info("event Id ------->" +
	 * eventId); logger.info("program Id ------->" + pgrmId);
	 * logger.info("username ------->" + username);
	 * 
	 * List<Timetable> tt = timetableDAO.getTimetableByCourse(pgrmId, eventId,
	 * username, curDate);
	 * 
	 * for (Timetable tmtl : tt) {
	 * 
	 * tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
	 * tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace( ".", ":"));
	 * tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
	 * tmtl.setCourseId(cid);
	 * 
	 * }
	 * 
	 * String json = new Gson().toJson(tt);
	 * 
	 * // logger.info("timtable List ------->" + json);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * return json; // return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 */

	/*
	 * @RequestMapping(value = { "/getTimetableByCourseForApp" }, method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * getTimetableByCourseForApp(
	 * 
	 * @RequestBody Course course, HttpServletResponse resp) { try {
	 * 
	 * // logger.info("Course----->"+course);
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "timetable_metadata");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable); String cid = ""; long eventId = 0;
	 * long pgrmId = 0; if (null != course.getId()) { cid =
	 * course.getId().toString(); eventId = Long.parseLong(cid.substring(0, 8));
	 * pgrmId = Long.parseLong(cid.substring(8)); } // String cid =
	 * course.getId().toString();
	 * 
	 * // long eventId = Long.parseLong(cid.substring(0, 8)); // long pgrmId =
	 * Long.parseLong(cid); long username = Long.parseLong(course.getUsername());
	 * String curDate = "";
	 * 
	 * if (null == course.getClassDate()) { Date dt = new Date(); SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("dd-MM-yyyy"); curDate = "%" +
	 * dateFormat.format(dt) + "%"; } else { curDate = "%" + course.getClassDate() +
	 * "%"; } logger.info("current Date ------->" + curDate);
	 * logger.info("course Id ------->" + cid); logger.info("event Id ------->" +
	 * eventId); logger.info("program Id ------->" + pgrmId);
	 * logger.info("username ------->" + username); List<Timetable> tt = new
	 * ArrayList<>(); if (null != course.getId()) { tt =
	 * timetableDAO.getTimetableByCourse(pgrmId, eventId, username, curDate); } else
	 * { tt = timetableDAO.getTimetableByCourseForApp(username, curDate); }
	 * 
	 * long pgrmId1; String cid1; List<Timetable> ttFinal = new ArrayList<>();
	 * 
	 * for (Timetable tmtl : tt) {
	 * 
	 * if (tmtl.getProgramId().contains(" , ")) {
	 * 
	 * String[] programidsStrings = tmtl.getProgramId().split( " , ");
	 * 
	 * for (int i = 0; i < programidsStrings.length; i++) { cid1 = "" +
	 * tmtl.getEventId() + programidsStrings[i]; Course c1 = courseService
	 * .findByID(Long.parseLong(cid1)); pgrmId1 =
	 * Long.parseLong(programidsStrings[i]); Program p1 =
	 * programService.findByID(pgrmId1);
	 * 
	 * if (null != c1 && null != p1) { Timetable tmtlSub = new Timetable();
	 * 
	 * tmtlSub.setClass_date(tmtl.getClass_date().split( " ")[0]);
	 * tmtlSub.setStart_time(tmtl.getStart_time().split( " ")[1].replace(".", ":"));
	 * tmtlSub.setEnd_time(tmtl.getEnd_time().split(" ")[1] .replace(".", ":"));
	 * tmtlSub.setEventId(tmtl.getEventId());
	 * tmtlSub.setFacultyId(tmtl.getFacultyId()); tmtlSub.setFlag(tmtl.getFlag());
	 * 
	 * // logger.info("inside for------>" + // programidsStrings[i]); cid1 = "" +
	 * tmtl.getEventId() + programidsStrings[i];
	 * 
	 * // logger.info("inside for------>" + cid1); // Course c1 = //
	 * courseService.findByID(Long.parseLong(cid1)); tmtlSub.setCourseId(cid1);
	 * tmtlSub.setCourseName(c1.getCourseName());
	 * tmtlSub.setProgramId(programidsStrings[i]);
	 * tmtlSub.setProgramName(p1.getProgramName()); //
	 * logger.info(p1.getProgramName()); ttFinal.add(tmtlSub); }
	 * 
	 * }
	 * 
	 * } else { cid1 = "" + tmtl.getEventId() + tmtl.getProgramId(); Course c1 =
	 * courseService.findByID(Long.parseLong(cid1)); pgrmId1 =
	 * Long.parseLong(tmtl.getProgramId()); Program p1 =
	 * programService.findByID(pgrmId1);
	 * 
	 * if (null != c1 && null != p1) {
	 * tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
	 * tmtl.setStart_time(tmtl.getStart_time().split(" ")[1] .replace(".", ":"));
	 * tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1] .replace(".", ":"));
	 * 
	 * // logger.info(cid);
	 * 
	 * tmtl.setCourseId(cid1); tmtl.setCourseName(c1.getCourseName());
	 * tmtl.setProgramName(p1.getProgramName()); //
	 * logger.info(p1.getProgramName()); ttFinal.add(tmtl); } } }
	 * 
	 * String json = new Gson().toJson(ttFinal);
	 * 
	 * logger.info("timtable List ------->" + json);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * return json; // return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 */
	@RequestMapping(value = { "/getTimetableByCourseForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTimetableByCourseForApp(@RequestBody Course course, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		try {
			course = (Course) decryptRequestBody(course.getEncrypted_key(), "Course");
			boolean auth = isUserAuthorized(headers.get("token"), course.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}

			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

					.createDefaultConnectionByDS(defaultUrl, defaultUsername,

							defaultPassword);

			DriverManagerDataSource dataSourceTimetable = multipleDBConnection

					.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
							"timetable_metadata");

			timetableDAO.setDS(dataSourceTimetable);
			String cid = "";
			long eventId = 0;
			long pgrmId = 0;
			if (null != course.getId()) {
				cid = course.getId().toString();
				eventId = Long.parseLong(cid.substring(0, 8));
				pgrmId = Long.parseLong(cid.substring(8));
			}
			// String cid = course.getId().toString();

			// long eventId = Long.parseLong(cid.substring(0, 8));
			// long pgrmId = Long.parseLong(cid);
			String username = course.getUsername();
			if (username.contains("_")) {
				username = username.substring(0, username.indexOf("_"));
			}
			String curDate = "";

			if (null == course.getClassDate()) {
				Date dt = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				curDate = "%" + dateFormat.format(dt) + "%";
			} else {
				curDate = "%" + course.getClassDate() + "%";
			}
			logger.info("current Date ------->" + curDate);
			logger.info("course Id ------->" + cid);
			logger.info("event Id ------->" + eventId);
			logger.info("program Id ------->" + pgrmId);
			logger.info("username ------->" + username);
			List<Timetable> tt = new ArrayList<>();
			if (null != course.getId()) {
				tt = timetableDAO.getTimetableByCourse(pgrmId, eventId, username, curDate);
				// logger.info("prett size"+ tt.size());
				// logger.info("TT LIST----------->" + tt);
			} else {
				tt = timetableDAO.getTimetableByCourseForApp(username, curDate);
			}

			long pgrmId1;
			String cid1;
			List<Timetable> ttFinal = new ArrayList<>();
			logger.info("tt size" + tt.size());
			for (Timetable tmtl : tt) {

				logger.info("ProgramId" + tmtl.getProgramId());
				if (tmtl.getProgramId().contains(" , ")) {

					String[] programidsStrings = tmtl.getProgramId().split(" , ");

					for (int i = 0; i < programidsStrings.length; i++) {
						cid1 = "" + tmtl.getEventId() + programidsStrings[i];
						Course c1 = courseService.findByID(Long.parseLong(cid1));
						pgrmId1 = Long.parseLong(programidsStrings[i]);
						Program p1 = programService.findByID(pgrmId1);

						if (null != c1 && null != p1) {
							Timetable tmtlSub = new Timetable();

							tmtlSub.setClass_date(tmtl.getClass_date().split(" ")[0]);
							tmtlSub.setStart_time(tmtl.getStart_time().split(" ")[1].replace(".", ":"));
							tmtlSub.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
							tmtlSub.setEventId(tmtl.getEventId());
							tmtlSub.setFacultyId(tmtl.getFacultyId());
							tmtlSub.setFlag(tmtl.getFlag());

							// logger.info("inside for------>" +
							// programidsStrings[i]);
							cid1 = "" + tmtl.getEventId() + programidsStrings[i];

							// logger.info("inside for------>" + cid1);
							// Course c1 =
							// courseService.findByID(Long.parseLong(cid1));
							tmtlSub.setCourseId(cid1);
							tmtlSub.setCourseName(c1.getCourseName());
							tmtlSub.setProgramId(programidsStrings[i]);
							tmtlSub.setProgramName(p1.getProgramName());
							logger.info(p1.getProgramName());
							if (null != course.getId()) {
								logger.info("courseId------->" + course.getId());
								logger.info("tmtlSub courseId------->" + tmtlSub.getCourseId());
								if (cid.equals(tmtlSub.getCourseId())) {
									ttFinal.add(tmtlSub);
								}
							} else {
								ttFinal.add(tmtlSub);
							}
						}

					}

				} else {
					cid1 = "" + tmtl.getEventId() + tmtl.getProgramId();
					Course c1 = courseService.findByID(Long.parseLong(cid1));
					pgrmId1 = Long.parseLong(tmtl.getProgramId());
					Program p1 = programService.findByID(pgrmId1);

					if (null != c1 && null != p1) {
						tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
						tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace(".", ":"));
						tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));

						// logger.info(cid);

						tmtl.setCourseId(cid1);
						tmtl.setCourseName(c1.getCourseName());
						tmtl.setProgramName(p1.getProgramName());
						// logger.info(p1.getProgramName());
						ttFinal.add(tmtl);
					}
				}
			}

			String json = new Gson().toJson(ttFinal);

			logger.info("timtable List ------->" + json);

			timetableDAO.setDS(dataSourceDefaultLms);

			json = encryptResponseBody(json);
			return json;
			// return "{\"Status\":\"Success\"}";

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"Status\":\"Fail\"}";

		}
	}

	/*
	 * @RequestMapping(value = { "/sendTimetableNotificationForApp" }, method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * sendTimetableNotificationForApp() {
	 * 
	 * String jsonResponse = "";
	 * 
	 * Client clientWS = null; ClientConfig clientConfig = null; WebTarget webTarget
	 * = null; Invocation.Builder invocationBuilder = null; Response response =
	 * null;
	 * 
	 * try {
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword,
	 * 
	 * "timetable_metadata");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * List<Timetable> timetableList = timetableDAO.getTimetableByTime();
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * for (Timetable tt : timetableList) {
	 * 
	 * User u = userService.findActivePlayerIdByUserName(tt .getFacultyId());
	 * 
	 * logger.info("user - - - >" + u);
	 * 
	 * if (u != null) {
	 * 
	 * String msg = "You have lecture at " + tt.getStart_time() + "";
	 * 
	 * String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\"," +
	 * "\"include_player_ids\": [\"" + u.getPlayerId() + "\"]," +
	 * "\"data\": {\"foo\": \"bar\"}," + "\"contents\": {\"en\": \"" + msg + "\"}" +
	 * "}";
	 * 
	 * int responseCode; clientConfig = new ClientConfig();
	 * 
	 * clientWS = ClientBuilder.newClient(clientConfig); webTarget = clientWS
	 * .target("https://onesignal.com/api/v1/notifications");
	 * 
	 * invocationBuilder = webTarget.request().header( HttpHeaders.AUTHORIZATION,
	 * "MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");
	 * 
	 * response = invocationBuilder.post(Entity.entity( json.toString(),
	 * MediaType.APPLICATION_JSON));
	 * 
	 * // get response code responseCode = response.getStatus();
	 * 
	 * logger.info("responseCode: " + responseCode);
	 * 
	 * jsonResponse += response.readEntity(String.class);
	 * 
	 * // logger.info("resp" + jsonResponse);
	 * 
	 * logger.info("jsonResponse:\n" + jsonResponse); } }
	 * 
	 * } catch (Throwable t) { t.printStackTrace(); }
	 * 
	 * return jsonResponse; }
	 */

	@RequestMapping(value = { "/sendTimetableNotificationForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String sendTimetableNotificationForApp(@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), headers.get("username"));
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String jsonResponse = "";

		Client clientWS = null;
		ClientConfig clientConfig = null;
		WebTarget webTarget = null;
		Invocation.Builder invocationBuilder = null;
		Response response = null;

		try {

			File file = new File(workDir + File.separator + "timetable.json");
			List<Timetable> timetableList = mapper.readValue(file, new TypeReference<List<Timetable>>() {
			});

			logger.info("timetableList - - - >" + timetableList);

			timetableList.removeIf(x -> {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				if (null != x.getStart_time() && !x.getStart_time().equals("")) {
					try {

						return !((dateFormat.parse(x.getStart_time()).compareTo(Utils.getInIST()) > 0
								|| dateFormat.parse(x.getStart_time()).compareTo(Utils.getInIST()) == 0)
								&& (dateFormat.parse(x.getStart_time())
										.compareTo(DateUtils.addMinutes(Utils.getInIST(), 5)) < 0
										|| dateFormat.parse(x.getStart_time())
												.compareTo(DateUtils.addMinutes(Utils.getInIST(), 5)) == 0));
					} catch (Exception e) {
						// e.printStackTrace();
						return false;
					}
				} else {
					return false;
				}

			});

			logger.info("timetableList - - - >" + timetableList);

			for (Timetable tt : timetableList) {

				User u = userService.findActivePlayerIdByUserName(tt.getFacultyId());

				logger.info("user - - - >" + u);

				if (u != null) {

					String msg = "You have lecture at " + tt.getStart_time() + "";

					String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\","
							+ "\"include_player_ids\": [\"" + u.getPlayerId() + "\"]," + "\"data\": {\"foo\": \"bar\"},"
							+ "\"contents\": {\"en\": \"" + msg + "\"}" + "}";

					int responseCode;

					logger.info("json - - - >" + json);
					clientConfig = new ClientConfig();

					clientWS = ClientBuilder.newClient(clientConfig);
					webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");

					invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
							"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");

					response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

					// get response code
					responseCode = response.getStatus();

					logger.info("responseCode: " + responseCode);

					jsonResponse += response.readEntity(String.class);

					// logger.info("resp" + jsonResponse);

					logger.info("jsonResponse:\n" + jsonResponse);
				}
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}

		return jsonResponse;
	}

	@RequestMapping(value = { "/insertUserPlayerIdForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String insertUserPlayerIdForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {

		try {
			user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
			boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);
				json = encryptResponseBody(json);
				return json;
			}
			String username = user.getUsername();
			String playerId = user.getPlayerId();
			User u = userService.findPlayerIdByUserName(username);
			if (u != null && !StringUtils.isEmpty(username) && !StringUtils.isEmpty(playerId)) {
				userService.updateUserPlayerId(username, playerId);
			} else {
				userService.insertUserPlayerId(username, playerId);
			}
			String json = "{\"Status\":\"Success\"}";
			json = encryptResponseBody(json);
			return json;
		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";
			json = encryptResponseBody(json);
			return json;
		}
	}

	@RequestMapping(value = { "/deleteUserPlayerIdForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String deleteUserPlayerIdForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {

		try {
			user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
			boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			String username = user.getUsername();

			userService.deleteUserPlayerId(username);
			service.deleteKey(headers.get("token"));
			String json = "{\"Status\":\"Success\"}";

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";

			json = encryptResponseBody(json);
			return json;
		}
	}

	@RequestMapping(value = { "/getAnnouncementListForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAnnouncementListForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {

		try {
			user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
			boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);
				json = encryptResponseBody(json);
				return json;
			}
			String username = user.getUsername();

			List<Announcement> announcementListForStudent = announcementService
					.getAnnouncementByUsernameForApp(username);

			String json = new Gson().toJson(announcementListForStudent);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";
			return json;

		}
	}

	@RequestMapping(value = { "/getNewsListForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getNewsListForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {

		try {
			user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
			boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			String username = user.getUsername();

			List<NewsEvents> newsEventsList = newsEventsService.getAllActiveNews();

			String json = new Gson().toJson(newsEventsList);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";
			return json;

		}
	}

	@RequestMapping(value = { "/getEventsListForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getEventsListForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {

		try {
			user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
			boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			String username = user.getUsername();

			List<NewsEvents> newsEventsList = newsEventsService.getAllActiveEvents();

			String json = new Gson().toJson(newsEventsList);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";

			json = encryptResponseBody(json);
			return json;

		}
	}

	/*
	 * @RequestMapping(value = "/downloadFileForApp", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public ModelAndView downloadFile(@RequestParam(required
	 * = false, name = "id", defaultValue = "") String id,
	 * 
	 * @RequestParam(required = false, name = "saId", defaultValue = "") String
	 * saId,
	 * 
	 * @RequestParam(required = false, name = "libraryId", defaultValue = "") String
	 * libraryId,
	 * 
	 * @RequestParam(required = false, name = "filePath", defaultValue = "") String
	 * filePath, HttpServletRequest request, HttpServletResponse response) {
	 * 
	 * OutputStream outStream = null; FileInputStream inputStream = null; String
	 * projectUrl = ""; logger.info("filePath" + filePath); try {
	 * 
	 * if (StringUtils.isEmpty(filePath)) { if (!StringUtils.isEmpty(id)) {
	 * Assignment assignment = assignmentService.findByID(Long.valueOf(id)); if
	 * (assignment != null) filePath = assignment.getFilePath(); } else {
	 * 
	 * if (!StringUtils.isEmpty(saId)) { StudentAssignment sa =
	 * studentAssignmentService.findByID(Long.valueOf(saId)); if (sa != null)
	 * filePath = sa.getStudentFilePath(); }
	 * 
	 * } if (!StringUtils.isEmpty(libraryId)) { Library library =
	 * libraryService.findByID(Long.valueOf(libraryId)); if (library != null)
	 * filePath = library.getFilePath(); } } logger.info("Full Path for download= "
	 * + filePath); ServletOutputStream out = response.getOutputStream();
	 * response.setContentType("Content-type: text/zip"); if (filePath.contains(",")
	 * && StringUtils.isEmpty(libraryId)) {
	 * 
	 * File folderPath = new File(workDir + File.separator + "allFiles");
	 * List<String> files = Arrays.asList(filePath.split(",")); if
	 * (!folderPath.exists()) { folderPath.mkdir(); }
	 * 
	 * for (String file : files) { File fileNew = new File(file); //
	 * files.add(file);
	 * 
	 * File dest = new File(folderPath.getAbsolutePath() + File.separator +
	 * fileNew.getName()); FileUtils.copyFile(fileNew, dest);
	 * 
	 * } String filename = "assignmentFiles.zip";
	 * response.setHeader("Content-Disposition", "attachment; filename=" + filename
	 * + ""); projectUrl = "/" + "workDir" + "/" + folderPath.getName() + ".zip";
	 * pack(folderPath.getAbsolutePath(), out);
	 * FileUtils.deleteDirectory(folderPath); return null;
	 * 
	 * } else {
	 * 
	 * if (StringUtils.isEmpty(filePath)) { request.setAttribute("error", "true");
	 * request.setAttribute("errorMessage", "Error in downloading file."); }
	 * 
	 * // get absolute path of the application ServletContext context =
	 * request.getSession().getServletContext(); File downloadFile = new
	 * File(filePath); inputStream = new FileInputStream(downloadFile);
	 * 
	 * // get MIME type of the file String mimeType = context.getMimeType(filePath);
	 * if (mimeType == null) { // set to binary type if MIME mapping not found
	 * mimeType = "application/octet-stream"; }
	 * 
	 * // set content attributes for the response response.setContentType(mimeType);
	 * response.setContentLength((int) downloadFile.length());
	 * 
	 * // set headers for the response String headerKey = "Content-Disposition";
	 * String headerValue = String.format("attachment; filename=\"%s\"",
	 * downloadFile.getName()); response.setHeader(headerKey, headerValue);
	 * 
	 * // get output stream of the response outStream = response.getOutputStream();
	 * IOUtils.copy(inputStream, outStream); }
	 * 
	 * } catch (Exception e) { logger.error("Exception", e);
	 * request.setAttribute("error", "true"); request.setAttribute("errorMessage",
	 * "Error in downloading file."); } finally { if (inputStream != null)
	 * IOUtils.closeQuietly(inputStream); if (outStream != null)
	 * IOUtils.closeQuietly(outStream);
	 * 
	 * } return null; }
	 */

	// 05/05/2020 shubham

	@RequestMapping(value = "/downloadFileForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadFile(
			@RequestParam(required = false, name = "id", defaultValue = "") String id,
			@RequestParam(required = false, name = "saId", defaultValue = "") String saId,
			@RequestParam(required = false, name = "libraryId", defaultValue = "") String libraryId,
			@RequestParam(required = false, name = "filePath", defaultValue = "") String filePath,
			@RequestParam(required = false, name = "username", defaultValue = "") String username,
			HttpServletRequest request, HttpServletResponse response, @RequestHeader Map<String, String> headers) {
		logger.info("HttpHeaders ==> " + headers);
		logger.info("username ==> " + username);
		id = decryptRequestParam(id);
		saId = decryptRequestParam(saId);
		libraryId = decryptRequestParam(libraryId);
		filePath = decryptRequestParam(filePath);
		username = decryptRequestParam(username);
		logger.info("id===> " + id);
		logger.info("saId===> " + saId);
		logger.info("libraryId===> " + libraryId);
		logger.info("username===> " + username);
		boolean auth = isUserAuthorized(headers.get("token"), username);
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			return null;
		}

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		String projectUrl = "";
		logger.info("filePath" + filePath);
		try {

			if (StringUtils.isEmpty(filePath)) {
				if (!StringUtils.isEmpty(id)) {
					Assignment assignment = assignmentService.findByID(Long.valueOf(id));
					Date date = Utils.getInIST();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentTime = sdf.format(date);
					logger.info("currentTime===> " + currentTime);
					String assignmentStartDateTime = assignment.getStartDate().replace("T", " ");
					logger.info("assignmentStartDateTime --> " + assignmentStartDateTime);

					long diff = timeDifferenceFormat(assignmentStartDateTime, currentTime);
					logger.info("diff --> " + diff);
					if (assignment != null && diff > 0) {
						filePath = assignment.getFilePath();
					}
				} else {

					if (!StringUtils.isEmpty(saId)) {
						StudentAssignment sa = studentAssignmentService.findByID(Long.valueOf(saId));
						if (sa != null)
							filePath = sa.getStudentFilePath();
					}

				}
				if (!StringUtils.isEmpty(libraryId)) {
					Library library = libraryService.findByID(Long.valueOf(libraryId));
					if (library != null)
						filePath = library.getFilePath();
				}
			}
			if (filePath.isEmpty()) {
				return null;
			}

			logger.info("Full Path for download= " + filePath);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			if (null != filePath && filePath.contains(",") && StringUtils.isEmpty(libraryId)) {

				File folderPath = new File(
						downloadAllFolderTemp + File.separator + RandomStringUtils.randomAlphanumeric(10));
				List<String> files = Arrays.asList(filePath.split(","));
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}

				for (String file : files) {
					File fileNew = new File(file);
					// files.add(file);

					InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
					String ext1 = FilenameUtils.getExtension(fileNew.getName());
					File dest = new File(
							folderPath.getAbsolutePath() + File.separator + fileNew.getName() + "." + ext1);
					FileUtils.copyInputStreamToFile(inpStream, dest);

					/*
					 * File dest = new File(folderPath.getAbsolutePath() + "/" + fileNew.getName());
					 * FileUtils.copyFile(fileNew, dest);
					 */

				}
				String filename = "assignmentFiles" + RandomStringUtils.random(10) + ".zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
				projectUrl = "/" + downloadAllFolderTemp + "/" + filename + ".zip";
				pack(folderPath.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				if (StringUtils.isEmpty(filePath)) {
					request.setAttribute("error", "true");
					request.setAttribute("errorMessage", "Error in downloading file.");
				}

				File downloadFile = new File(filePath);

				final byte[] data = amazonS3ClientService.getFile(filePath);
				final ByteArrayResource resource = new ByteArrayResource(data);
				return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
						.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
						.body(resource);

			}

		} catch (Exception e) {
			logger.error("Exception", e);
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage", "Error in downloading file.");
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

	@RequestMapping(value = { "/getAttendanceStatForApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAttendanceStatForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {

		try {
			user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
			boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
			logger.info("HttpHeadersToken ==> " + headers.get("token"));
			if (!auth) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("status", "failed");
				map.put("access", "unauthorised access");
				String json = new Gson().toJson(map);

				json = encryptResponseBody(json);
				return json;
			}
			String username = user.getUsername();
			String courseId = user.getCourseId();
			SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");
			Date sd = dateFormatApp.parse(user.getStartDate());
			Date ed = dateFormatApp.parse(user.getEndDate());
			logger.info("sDate---------> " + sd);
			logger.info("eDate---------> " + ed);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String startDate = dateFormat.format(sd) + " " + "00:00:00";
			logger.info(startDate);
			String endDate = dateFormat.format(ed) + " " + "23:59:59";
			logger.info(endDate);
			List<StudentCourseAttendance> AttendanceStatisticsList = studentCourseAttendanceService
					.getAttendanceStatByUsernameAndCourseId(username, startDate, endDate);

			String json = new Gson().toJson(AttendanceStatisticsList);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";
			return json;

		}
	}

	/*
	 * @RequestMapping(value = "/submitAssignmentForApp", method = {
	 * 
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * submitAssignmentForApp(
	 * 
	 * @RequestPart(value = "file") MultipartFile file,
	 * 
	 * @RequestParam(value = "assignmentSubmission") String
	 * assignmentSubmissionJson, HttpServletResponse resp) {
	 * 
	 * logger.info("Inside submitAssignmentForApp");
	 * 
	 * logger.info("file--->" + file.getOriginalFilename());
	 * 
	 * StudentAssignment assignmentSubmission = new Gson().fromJson(
	 * assignmentSubmissionJson, StudentAssignment.class); ;
	 * 
	 * logger.info("id--->" + assignmentSubmission.getId());
	 * 
	 * Assignment assignment =
	 * 
	 * assignmentService.findByID(assignmentSubmission.getId());
	 * 
	 * assignmentSubmission.setCourseId(assignment.getCourseId());
	 * 
	 * String username = assignmentSubmission.getUsername();
	 * 
	 * assignmentSubmission.setAssignmentId(assignmentSubmission.getId());
	 * 
	 * assignmentSubmission.setUsername(username);
	 * logger.info(assignmentSubmission.getUsername());
	 * 
	 * assignmentSubmission.setSubmissionDate(Utils.getInIST());
	 * 
	 * assignmentSubmission.setLastModifiedBy(username);
	 * 
	 * String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentSubmission.getSubmissionDate());
	 * assignmentSubmission.setActive("Y");
	 * 
	 * int chkStartDate = studentAssignmentService
	 * .chkStartandEndDateOfAssignment(username, assignment.getId());
	 * 
	 * int chkStartAndEndDate = studentAssignmentService
	 * .chkStartandEndDtOfAssignment(username, assignment.getId());
	 * 
	 * logger.info("chkStartAndEndDate " + chkStartAndEndDate);
	 * 
	 * if (chkStartAndEndDate > 0) {
	 * 
	 * assignmentSubmission.setSubmissionStatus("Y");
	 * 
	 * } else {
	 * 
	 * // assignmentSubmission.setAssignmentError(assignmentError);
	 * 
	 * assignmentSubmission.setSubmissionStatus("N"); }
	 * 
	 * assignmentSubmission.setStartDate(assignment.getStartDate());
	 * 
	 * assignmentSubmission.setEndDate(assignment.getEndDate());
	 * 
	 * if (chkStartDate == 0) { // setNote(m, //
	 * "Assignment Submission has not started yet or deadline is missed!!");
	 * 
	 * } else {
	 * 
	 * if (!file.isEmpty()) { Date date = new Date();
	 * 
	 * String errorMessage = studentAssignmentController
	 * .uploadAssignmentSubmissionFile(assignmentSubmission, file);
	 * logger.info("ErrorMessgae " + errorMessage);
	 * 
	 * Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
	 * 
	 * if (errorMessage == null) {
	 * 
	 * CopyleaksAudit cplAudit = copyleaksAuditService
	 * .getRecordByUsername(username, assignment.getId());
	 * 
	 * boolean isPresent = false; boolean copyleakscheck = false; if (cplAudit ==
	 * null) { isPresent = false; copyleakscheck = true; } else {
	 * 
	 * isPresent = true; if (cplAudit.getCount() >= 2) { copyleakscheck = false;
	 * 
	 * } else { copyleakscheck = true; }
	 * 
	 * }
	 * 
	 * if (assignment.getPlagscanRequired().equals("Yes") &&
	 * assignment.getRunPlagiarism().equals( "Submission") && copyleakscheck ==
	 * true) {
	 * 
	 * if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))
	 * 
	 * { // CopyLeaks copyLeaks = new CopyLeaks();
	 * 
	 * try { File storedFile = submit .multipartToFile(assignmentSubmission
	 * .getStudentFilePath()); plagValueMap = copyLeaks.scan(copyleaksId,
	 * copyleaskKey, storedFile); String url = ""; Integer plagValue = 0; Integer
	 * creditUsed = 0;
	 * 
	 * if (plagValueMap.isEmpty()) { assignmentSubmission.setThreshold(0);
	 * assignmentSubmission.setUrl(" "); } else { for (String key :
	 * plagValueMap.keySet()) { url = key;
	 * 
	 * if (url != null) {
	 * 
	 * } else { url = "Copyleaks internal database"; } plagValue =
	 * plagValueMap.get(key);
	 * 
	 * if (url == "creditUsed") {
	 * 
	 * creditUsed = plagValue;
	 * 
	 * } else { assignmentSubmission.setUrl(url); assignmentSubmission
	 * .setThreshold(plagValue); }
	 * 
	 * 
	 * assignmentSubmission.setUrl(url); assignmentSubmission
	 * .setThreshold(plagValue);
	 * 
	 * 
	 * } }
	 * 
	 * if (isPresent == false) {
	 * 
	 * CopyleaksAudit cpl = new CopyleaksAudit();
	 * cpl.setAssignmentId(assignment.getId()); cpl.setUsername(username);
	 * cpl.setCount(1); cpl.setCreditUsed(creditUsed); cpl.setCreatedBy(username);
	 * cpl.setLastModifiedBy(username);
	 * 
	 * copyleaksAuditService.insert(cpl);
	 * 
	 * } else {
	 * 
	 * cplAudit.setCount(cplAudit.getCount() + 1); cplAudit.setCreditUsed(cplAudit
	 * .getCreditUsed() + creditUsed);
	 * 
	 * copyleaksAuditService.update(cplAudit);
	 * 
	 * }
	 * 
	 * // logger.info("plagValue--" + plagValueMap); if (assignment.getThreshold()
	 * == null) { assignment.setThreshold(50); } else {
	 * 
	 * } if (plagValue >= assignment.getThreshold()) {
	 * 
	 * assignmentSubmission
	 * .setAssignmentError("Uploaded File does not fall under plag threshold " +
	 * plagValue + "% copied from " + url);
	 * 
	 * setError(m, "Uploaded File does not fall under plag threshold " + plagValue +
	 * "% copied from " + url);
	 * 
	 * 
	 * } else { studentAssignmentService .update(assignmentSubmission);
	 * studentAssignmentAuditService .insert(assignmentSubmission); //
	 * assignmentSubmission
	 * .setAssignmentSuccess("Assignment Answer file uploaded successfully. ");
	 * logger.info(assignmentSubmission .getAssignmentSuccess());
	 * 
	 * setSuccess(m, "Assigment Answer file uploaded successfully" );
	 * 
	 * 
	 * String sdf1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentSubmission .getSubmissionDate());
	 * 
	 * if (chkStartAndEndDate == 0) { assignmentSubmission
	 * .setAssignmentSuccess("Your assignment will get submitted only if it's approved by respective faculty. "
	 * ); logger.info(assignmentSubmission .getAssignmentSuccess());
	 * 
	 * setNote(m,
	 * 
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
	 * 
	 * );
	 * 
	 * } else { assignmentSubmission
	 * .setAssignmentSuccess("Assignment Answer file uploaded successfully. ");
	 * logger.info(assignmentSubmission .getAssignmentSuccess());
	 * 
	 * setSuccess(m, "Assigment Answer file uploaded successfully" );
	 * 
	 * } } } catch (Exception e) { logger.error("Exception", e); } }
	 * 
	 * } else { try {
	 * 
	 * studentAssignmentService .update(assignmentSubmission);
	 * studentAssignmentAuditService .insert(assignmentSubmission); //
	 * assignmentSubmission
	 * .setAssignmentSuccess("Assignment Answer file uploaded successfully. ");
	 * logger.info("assignmentStatus--->" + assignmentSubmission
	 * .getAssignmentSuccess());
	 * 
	 * setSuccess(m, "Assigment Answer file uploaded successfully");
	 * 
	 * 
	 * String sdf1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentSubmission.getSubmissionDate());
	 * 
	 * if (chkStartAndEndDate == 0) { assignmentSubmission
	 * .setAssignmentNote("Your assignment will get submitted only if it's approved by respective faculty. "
	 * ); logger.info(assignmentSubmission .getAssignmentNote());
	 * 
	 * setNote(m,
	 * 
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
	 * 
	 * );
	 * 
	 * } else { assignmentSubmission
	 * .setAssignmentSuccess("Assignment Answer file uploaded successfully. ");
	 * logger.info(assignmentSubmission .getAssignmentSuccess());
	 * 
	 * setSuccess(m, "Assigment Answer file uploaded successfully" );
	 * 
	 * } } catch (Exception e) { logger.error("Exception", e); }
	 * 
	 * }
	 * 
	 * } else { assignmentSubmission.setAssignmentError(errorMessage);
	 * logger.info(assignmentSubmission.getAssignmentError()); setError(m,
	 * errorMessage); }
	 * 
	 * } else { assignmentSubmission
	 * .setAssignmentError("File is empty, please upload correct file.");
	 * logger.info(assignmentSubmission.getAssignmentError()); setError(m,
	 * "File is empty, please upload correct file."); }
	 * 
	 * }
	 * 
	 * if (assignment.getRunPlagiarism() != null) { if
	 * (assignment.getRunPlagiarism().equals("Manual")) {
	 * m.addAttribute("showCheckForPlagiarism", true); } }
	 * 
	 * m.addAttribute("assignment", assignment);
	 * 
	 * assignmentSubmission =
	 * 
	 * studentAssignmentService.findAssignmentSubmission(username,
	 * 
	 * assignmentSubmission.getId());
	 * 
	 * 
	 * logger.info("getAssignmentError: " +
	 * assignmentSubmission.getAssignmentError()); logger.info("getAssignmentNote: "
	 * + assignmentSubmission.getAssignmentNote());
	 * logger.info("getAssignmentSuccess: " +
	 * assignmentSubmission.getAssignmentSuccess()); String json = new
	 * Gson().toJson(assignmentSubmission);
	 * 
	 * logger.info("json----->" + json); return json;
	 * m.addAttribute("assignmentSubmission", assignmentSubmission);
	 * 
	 * // return "assignment/submitAssignment"; }
	 */

	@RequestMapping(value = "/submitAssignmentForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<String> submitAssignmentForApp(@RequestPart(value = "file") MultipartFile file,
			@RequestParam(value = "assignmentSubmission") String assignmentSubmissionJson,
			@RequestParam(value = "username") String usernamee, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		logger.info("assignmentSubmissionJson ==> " + assignmentSubmissionJson);
		assignmentSubmissionJson = decryptRequestParam(assignmentSubmissionJson);
		usernamee = decryptRequestParam(usernamee);
		boolean auth = isUserAuthorized(headers.get("token"), usernamee);
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			return ResponseEntity.ok(new Gson().toJson(map));
		}

		logger.info("file--->" + file.getOriginalFilename());

		StudentAssignment assignmentSubmission = new Gson().fromJson(assignmentSubmissionJson, StudentAssignment.class);

		logger.info("id--->" + assignmentSubmission.getId());

		Assignment assignment =

				assignmentService.findByID(assignmentSubmission.getId());

		assignmentSubmission.setCourseId(assignment.getCourseId());

		String username = assignmentSubmission.getUsername();

		assignmentSubmission.setAssignmentId(assignmentSubmission.getId());

		assignmentSubmission.setUsername(username);
		logger.info(assignmentSubmission.getUsername());

		assignmentSubmission.setSubmissionDate(Utils.getInIST());

		assignmentSubmission.setLastModifiedBy(username);

		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

				.format(assignmentSubmission.getSubmissionDate());
		assignmentSubmission.setActive("Y");

		int checkEvaluationStatus = studentAssignmentService.checkAssignmentEvaluationStatus(username,
				assignment.getId());
		logger.info("checkEvaluationStatus " + checkEvaluationStatus);

		String approvalStatus = studentAssignmentService.getApprovalStatusForAssignment(username, assignment.getId());
		logger.info("approvalStatus " + approvalStatus);

		if (checkEvaluationStatus > 0) {
			assignmentSubmission.setAssignmentError("Assignment is already evaluated by faculty.");
			assignmentSubmission.setAssignmentNote("You cannot resubmit assignment now.");
			assignmentSubmission.setEvaluationStatus("Y");
			assignmentSubmission.setAssignmentCompleted("Y");
			assignmentSubmission.setAssignmentStatus("Completed");

			String json = new Gson().toJson(assignmentSubmission);
			json = encryptResponseBody(json);
			return ResponseEntity.ok(json);
		} else if (null != approvalStatus && approvalStatus.contains("Reject")) {
			assignmentSubmission.setAssignmentError("Faculty has already rejected your assignment");
			assignmentSubmission.setAssignmentNote("You cannot resubmit assignment now.");
			assignmentSubmission.setEvaluationStatus("Y");
			assignmentSubmission.setAssignmentCompleted("Y");
			assignmentSubmission.setAssignmentStatus("Completed___Evaluated___Rejected");

			String json = new Gson().toJson(assignmentSubmission);
			json = encryptResponseBody(json);
			return ResponseEntity.ok(json);
		} else {
			int chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId());

			int chkStartAndEndDate = studentAssignmentService.chkStartandEndDtOfAssignment(username,
					assignment.getId());

			logger.info("chkStartAndEndDate first ---> " + chkStartAndEndDate);

			try {
				String bufferTime = lmsVariablesService.getLmsVariableBykeyword("assignmentEndBuferTime").getValue();
				Date actualEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(assignment.getEndDate().replace("T", " "));
				Date bufferEndDate = DateUtils.addMinutes(actualEndDate, Integer.parseInt(bufferTime));
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				chkStartAndEndDate = studentAssignmentService
						.chkStartandEndDtOfAssignment(dateFormat.format(bufferEndDate), username, assignment.getId());

			} catch (Exception e) {
				// e.printStackTrace();
			}

			if (chkStartAndEndDate > 0) {

				assignmentSubmission.setSubmissionStatus("Y");

			} else {
				assignmentSubmission.setSubmissionStatus("N");
			}

			assignmentSubmission.setStartDate(assignment.getStartDate());

			assignmentSubmission.setEndDate(assignment.getEndDate());

			if (chkStartDate == 0) {

			} else {

				if (!file.isEmpty()) {

					logger.info("file_Orig_name " + file.getOriginalFilename());
					String fileNameCheck = file.getOriginalFilename();
					String fileNameCheckArr[] = fileNameCheck.split("\\.");
					logger.info("fileNameCheckArr Size " + fileNameCheckArr.length);
					String whiteList = "png,jpg,jpeg,doc,docx,xlsx,csv,pdf,ppt,txt";
					if (fileNameCheckArr.length == 2 && whiteList.contains(fileNameCheckArr[1].trim().toLowerCase())) {
						Date date = new Date();
						String errorMessage = studentAssignmentController
								.uploadAssignmentSubmissionFile(assignmentSubmission, file);
						logger.info("ErrorMessgae " + errorMessage);
						Map<String, Integer> plagValueMap = new HashMap<String, Integer>();

						if (errorMessage == null) {

							CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username,
									assignment.getId());

							boolean isPresent = false;
							boolean copyleakscheck = false;
							if (cplAudit == null) {
								isPresent = false;
								copyleakscheck = true;
							} else {

								isPresent = true;
								if (cplAudit.getCount() >= 2) {
									copyleakscheck = false;

								} else {
									copyleakscheck = true;
								}

							}

							boolean plagGreaterthanThreshold = false;

							if (assignment.getPlagscanRequired().equals("Yes")
									&& assignment.getRunPlagiarism().equals("Submission") && copyleakscheck == true) {

								if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

								{

									try {

										File storedFile = submit
												.multipartToFileForS3(assignmentSubmission.getStudentFilePath());
										String scanId = app.replaceAll("-", "").toLowerCase() + "-" + assignment.getId()
												+ "-" + username + "-" + RandomStringUtils.randomNumeric(2, 2);

										Map<String, String> scanStatus = copyLeaks.scanFileForCopyleaks(copyleaksId,
												copyleaskKey, storedFile, scanId);
										if (scanStatus.containsKey("Error")) {
											;
											logger.info("Error Occurred");
										} else {
											StudentAssignment auditAssignmentSubmission = studentAssignmentAuditService
													.findAssignmentSubmission(username, assignment.getId());
											if (null == auditAssignmentSubmission) {
												studentAssignmentAuditService.insert(assignmentSubmission);
												auditAssignmentSubmission = studentAssignmentAuditService
														.findAssignmentSubmission(username, assignment.getId());
												logger.info("Waiting...");
												long waitingTime = 0;
												while (null == auditAssignmentSubmission.getThreshold()) {
													Thread.sleep(5000);
													waitingTime = waitingTime + 5000;
													logger.info(
															"Waiting..." + auditAssignmentSubmission.getThreshold());
													// wait for 4.30 mins only
													if (waitingTime > 258000) {
														break;
													}
													auditAssignmentSubmission = studentAssignmentAuditService
															.findAssignmentSubmission(username, assignment.getId());
													logger.info(
															"Checking..." + auditAssignmentSubmission.getThreshold());
												}

												if (null != auditAssignmentSubmission.getThreshold()) {
													logger.info(
															"Threshold--->" + auditAssignmentSubmission.getThreshold());
													if (assignment.getThreshold() <= auditAssignmentSubmission
															.getThreshold()) {
//														setError(rd, "Uploaded File does not fall under plagiarism threshold " + auditAssignmentSubmission.getThreshold()
//																+ "% copied from " + auditAssignmentSubmission.getUrl());
														plagGreaterthanThreshold = true;
														assignmentSubmission.setAssignmentError(
																"Uploaded File does not fall under plag threshold "
																		+ auditAssignmentSubmission.getThreshold()
																		+ "% copied from "
																		+ auditAssignmentSubmission.getUrl());
														assignmentSubmission.setAssignmentStatus("Pending");
													} else {
														assignmentSubmission
																.setThreshold(auditAssignmentSubmission.getThreshold());
														assignmentSubmission.setUrl(auditAssignmentSubmission.getUrl());
														studentAssignmentService.update(assignmentSubmission);
														/*
														 * if (chkStartAndEndDate == 0) { setNote(
														 * rd,"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
														 * ); } else { setSuccess(rd,
														 * "Assigment Answer file uploaded successfully"); }
														 */

														studentAssignmentService.update(assignmentSubmission);
														// studentAssignmentAuditService.insert(assignmentSubmission);
														// //
														assignmentSubmission.setAssignmentSuccess(
																"Assignment Answer file uploaded successfully. ");
														logger.info(assignmentSubmission.getAssignmentSuccess());

														String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

																.format(assignmentSubmission.getSubmissionDate());

														// set submissionDate
														Date submissionDate = Utils.getInIST();
														SimpleDateFormat simpledateformat = new SimpleDateFormat(
																"yyyy-MM-dd HH:mm:ss");
														String submissionDateString = simpledateformat
																.format(submissionDate);
														logger.info(
																"submissionDateString----> " + submissionDateString);
														studentAssignmentService.setSubmissionDate(submissionDateString,
																assignment.getId(), username);

														if (chkStartAndEndDate == 0) {
															assignmentSubmission.setAssignmentSuccess(
																	"Your assignment will get submitted only if it's approved by respective faculty. ");
															logger.info(assignmentSubmission.getAssignmentSuccess());
															assignmentSubmission.setAssignmentStatus("Not Evaluated");

														} else {
															assignmentSubmission.setAssignmentSuccess(
																	"Assignment Answer file uploaded successfully. ");
															logger.info(assignmentSubmission.getAssignmentSuccess());

														}

													}
												} else {
													assignmentSubmission.setAssignmentError(
															"File takes too long time for plagiarism.");
													assignmentSubmission.setAssignmentStatus("Pending");
													logger.info("File takes too long time for plagiarism.");
												}
											}

										}
									} catch (Exception e) {
										logger.error("Exception", e);
									}
								}

							} else {
								try {

									studentAssignmentService.update(assignmentSubmission);
									studentAssignmentAuditService.insert(assignmentSubmission); //
									assignmentSubmission
											.setAssignmentSuccess("Assignment Answer file uploaded successfully. ");
									logger.info("assignmentStatus--->" + assignmentSubmission.getAssignmentSuccess());
									// set submissionDate
									Date submissionDate = Utils.getInIST();
									SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									String submissionDateString = simpledateformat.format(submissionDate);
									logger.info("submissionDateString----> " + submissionDateString);
									studentAssignmentService.setSubmissionDate(submissionDateString, assignment.getId(),
											username);

									String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

											.format(assignmentSubmission.getSubmissionDate());

									if (chkStartAndEndDate == 0) {
										assignmentSubmission.setAssignmentNote(
												"Your assignment will get submitted only if it's approved by respective faculty. ");
										logger.info(assignmentSubmission.getAssignmentNote());
										assignmentSubmission.setAssignmentStatus("Not Evaluated");

									} else {
										assignmentSubmission
												.setAssignmentSuccess("Assignment Answer file uploaded successfully. ");
										logger.info(assignmentSubmission.getAssignmentSuccess());
										assignmentSubmission.setAssignmentStatus("Not Evaluated");

									}
								} catch (Exception e) {
									logger.error("Exception", e);
								}

							}

						} else {
							assignmentSubmission.setAssignmentError(errorMessage);
							logger.info(assignmentSubmission.getAssignmentError());
							assignmentSubmission.setAssignmentStatus("Pending");
						}
					} else {
						assignmentSubmission.setAssignmentError("improper file found, please upload correct file.");
						logger.info(assignmentSubmission.getAssignmentError());
						assignmentSubmission.setAssignmentStatus("Pending");
					}
				}

				else {
					assignmentSubmission.setAssignmentError("File is empty, please upload correct file.");
					logger.info(assignmentSubmission.getAssignmentError());
					assignmentSubmission.setAssignmentStatus("Pending");
				}

			}

			if (assignment.getRunPlagiarism() != null) {
				if (assignment.getRunPlagiarism().equals("Manual")) {
				}
			}

			logger.info("getAssignmentError: " + assignmentSubmission.getAssignmentError());
			logger.info("getAssignmentNote: " + assignmentSubmission.getAssignmentNote());
			logger.info("getAssignmentSuccess: " + assignmentSubmission.getAssignmentSuccess());
			String json = new Gson().toJson(assignmentSubmission);

			json = encryptResponseBody(json);
			logger.info("json----->" + json);
			return ResponseEntity.ok(json);
		}
	}

	/*
	 * @RequestMapping(value = "/getAssignmentListForApp", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * assignmentList(@RequestBody Course course, Model m, Principal principal) {
	 * 
	 * String username = course.getUsername(); Long courseId = course.getId();
	 * 
	 * List<Assignment> assignments = Collections.emptyList();
	 * 
	 * if (courseId != null) { assignments =
	 * assignmentService.findByUserAndCourse(username, courseId); } else {
	 * 
	 * assignments = assignmentService.findByUser(username); }
	 * 
	 * for (Assignment a : assignments) { Course c =
	 * courseService.findByID(a.getCourseId()); a.setCourseName(c.getCourseName());
	 * assignments.set(assignments.indexOf(a), a);
	 * 
	 * }
	 * 
	 * if (assignments.size() == 0 || assignments.isEmpty()) { return
	 * "{\"Status\":\"No Assignment\"}"; }
	 * 
	 * String json = new Gson().toJson(assignments); logger.info("json----->" +
	 * json); return json;
	 * 
	 * }
	 */

	@RequestMapping(value = "/getAssignmentListForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String assignmentList(@RequestBody Course course, Model m, Principal principal,
			@RequestHeader Map<String, String> headers) {
		course = (Course) decryptRequestBody(course.getEncrypted_key(), "Course");
		boolean auth = isUserAuthorized(headers.get("token"), course.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String username = course.getUsername();
		Long courseId = course.getId();

		List<Assignment> assignments = Collections.emptyList();

		if (courseId != null) {
			assignments = assignmentService.findByUserAndCourse(username, courseId);
		} else {

			String schoolNotPermitted = "SBM-NM-M";
			logger.info("schoolName --> " + app);

			if (schoolNotPermitted.contains(app)) {
				logger.info("findByUserType --> " + "findByUserNew");
				assignments = assignmentService.findByUserNew(username);
			} else {
				logger.info("findByUserType --> " + "findByUser");
				assignments = assignmentService.findByUser(username);
			}
			for (Assignment assignment : assignments) {

				if (null != assignment.getStudentFilePath()) {
					logger.info("Submission_Status----->" + "submitted___" + assignment.getSubmissionStatus());
					String endDate = assignment.getEndDate().replace("T", " ");
					logger.info("endDate----->" + endDate);
					String submissionDate = assignment.getSubmissionDate().toString();
					if (submissionDate.endsWith(".0")) {
						submissionDate = submissionDate.substring(0, submissionDate.length() - 2);
					}
					logger.info("submissionDate----->" + submissionDate);
					long diff = timeDifferenceFormat(submissionDate, endDate);
					logger.info("diff----->" + diff);

					if (diff > 0) {
						// student has submitted assignment before endDate
						if (assignment.getEvaluationStatus().equals("Y")) {
							assignment.setAssignmentStatus("Completed");
						} else {
							assignment.setAssignmentStatus("Not Evaluated");
						}
					} else {
						// student has submitted assignment after endDate

						if (null != assignment.getApprovalStatus()
								&& assignment.getApprovalStatus().contains("Reject")) {
							if (assignment.getEvaluationStatus().equals("Y")) {
								assignment.setAssignmentStatus("Completed___Evaluated___Rejected");
							} else {
								assignment.setAssignmentStatus("Completed___Not Evaluated___Rejected");
							}
						} else {
							if (assignment.getEvaluationStatus().equals("Y")) {
								assignment.setAssignmentStatus("Pending___Evaluated___Approved");
							} else {
								assignment.setAssignmentStatus("Pending___Not Evaluated___Approved");
							}
						}
					}
				} else {
					logger.info("Submission_Status----->" + "pending___" + assignment.getSubmissionStatus());
					assignment.setAssignmentStatus("Pending");
				}
			}
		}

		for (Assignment a : assignments) {
			Course c = courseService.findByID(a.getCourseId());
			a.setCourseName(c.getCourseName());
			assignments.set(assignments.indexOf(a), a);

		}
		String json = "";
		if (assignments.size() == 0 || assignments.isEmpty()) {
			json = "{\"Status\":\"No Assignment\"}";
			logger.info("assignmentList ====> " + json);

			json = encryptResponseBody(json);
			return json;
		}
		json = new Gson().toJson(assignments);
		logger.info("assignmentList ====> " + json);

		json = encryptResponseBody(json);
		return json;

	}

	public long timeDifferenceFormat(String time1, String time2) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		long diff = 0;
		try {
			d1 = format.parse(time1);
			d2 = format.parse(time2);
			diff = d2.getTime() - d1.getTime();
			logger.info("diff----->" + String.valueOf(diff));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}

	/*
	 * @RequestMapping(value = "/submitAssignmentByOneInGroupForApp", method = {
	 * 
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * submitAssignmentByOneInGroupForApp(@RequestPart(value = "file") MultipartFile
	 * file,
	 * 
	 * @RequestParam(value = "assignmentSubmission") String
	 * assignmentSubmissionJson, HttpServletResponse resp, Principal principal) {
	 * String json = "";
	 * logger.info("-------------->submitAssignmentByOneInGroupForApp");
	 * 
	 * StudentAssignment assignmentSubmissionJ = new
	 * Gson().fromJson(assignmentSubmissionJson, StudentAssignment.class); //
	 * m.addAttribute("webPage", new //
	 * WebPage("assignment","Submit Assignment",false, false, true, true, //
	 * false)); Assignment assignment =
	 * assignmentService.findByID(assignmentSubmissionJ.getId()); String username =
	 * assignmentSubmissionJ.getUsername();
	 * 
	 * StudentAssignment assignmentSubmission =
	 * studentAssignmentService.findAssignmentSubmission(username,
	 * assignmentSubmissionJ.getId());
	 * 
	 * int chkStartDate =
	 * studentAssignmentService.chkStartandEndDateOfAssignment(username,
	 * assignment.getId());
	 * 
	 * List<String> getAllSapIdsOfStudentFromAssignment = studentAssignmentService
	 * .getAllSapIdsOfStudentFromAssignment(assignmentSubmissionJ.getId(),
	 * assignmentSubmission.getGroupId());
	 * 
	 * String extension = FilenameUtils.getExtension(file.getOriginalFilename());
	 * 
	 * // redirectAttrs.addAttribute("id", assignment.getId()); try { if
	 * (extension.equals("zip")) {
	 * logger.info("zip--------->submitAssignmentByOneInGroupForApp"); File
	 * normalFile = studentAssignmentController.convert(file);
	 * 
	 * Map<String, String> mapOfFiles =
	 * studentAssignmentController.unzip(normalFile.getAbsolutePath(),
	 * submissionFolder, getAllSapIdsOfStudentFromAssignment,
	 * assignment.getAssignmentName() + '-' + assignment.getId());
	 * 
	 * if (mapOfFiles.containsKey("Error")) { // setError(redirectAttrs,
	 * mapOfFiles.get("Error")); // return "redirect:/submitAssignmentForm";
	 * assignmentSubmissionJ.setAssignmentError(mapOfFiles.get("Error")); json = new
	 * Gson().toJson(assignmentSubmissionJ);
	 * 
	 * logger.info("json----->" + json);
	 * 
	 * return json; } else if (mapOfFiles.containsKey("ErrorInUploading")) { //
	 * setError(redirectAttrs, // mapOfFiles.get("ErrorInUploading")); // return
	 * "redirect:/submitAssignmentForm";
	 * assignmentSubmissionJ.setAssignmentError(mapOfFiles.get("ErrorInUploading"));
	 * json = new Gson().toJson(assignmentSubmissionJ);
	 * 
	 * logger.info("json----->" + json);
	 * 
	 * return json;
	 * 
	 * } else {
	 * 
	 * if (mapOfFiles.size() == getAllSapIdsOfStudentFromAssignment.size()) {
	 * 
	 * if (chkStartDate == 0) { assignmentSubmissionJ.setAssignmentNote(
	 * "YOUR GROUP ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY. "
	 * ); // setNote(redirectAttrs,"YOUR GROUP ASSIGNMENT WILL GET SUBMITTED ONLY IF
	 * IT'S // APPROVED BY RESPECTIVE FACULTY"); } for (String key :
	 * mapOfFiles.keySet()) {
	 * 
	 * Map<String, String> submissionStatus =
	 * submitAssignmentByIdForApp(assignment.getId(), key, mapOfFiles.get(key),
	 * principal);
	 * 
	 * if (submissionStatus.containsKey("Plag" + key)) {
	 * assignmentSubmissionJ.setAssignmentError(submissionStatus.get("Plag" + key));
	 * // setError(redirectAttrs, // submissionStatus.get("Plag"+key)); } else {
	 * assignmentSubmissionJ.
	 * setAssignmentSuccess("Assignment Submitted Successfully"); } } } else { //
	 * setError(redirectAttrs,"No. of files doesn't match with No. Of Students in //
	 * the Group"); // return "redirect:/submitAssignmentForm";
	 * assignmentSubmissionJ
	 * .setAssignmentError("No. of files doesn't match with No. Of Students in the Group. "
	 * ); json = new Gson().toJson(assignmentSubmissionJ);
	 * 
	 * logger.info("json----->" + json);
	 * 
	 * return json; } }
	 * 
	 * } else { // setError(redirectAttrs,"Submitted File is not a zip file");
	 * assignmentSubmissionJ.setAssignmentError("Submitted File is not a zip file. "
	 * ); // return "redirect:/submitAssignmentForm"; json = new
	 * Gson().toJson(assignmentSubmissionJ);
	 * 
	 * logger.info("json----->" + json);
	 * 
	 * return json; } } catch (Exception ex) { logger.error("Exception", ex); }
	 * 
	 * // setSuccess(redirectAttrs, "Assignment Submitted Successfully"); //
	 * assignmentSubmissionJ.setAssignmentSuccess("Assignment Submitted //
	 * Successfully"); // m.addAttribute("assignment", assignment); // return
	 * "redirect:/submitAssignmentForm"; logger.info("getAssignmentError: " +
	 * assignmentSubmissionJ.getAssignmentError());
	 * logger.info("getAssignmentNote: " +
	 * assignmentSubmissionJ.getAssignmentNote());
	 * logger.info("getAssignmentSuccess: " +
	 * assignmentSubmissionJ.getAssignmentSuccess()); json = new
	 * Gson().toJson(assignmentSubmissionJ);
	 * 
	 * logger.info("json----->" + json);
	 * 
	 * return json; // return null;
	 * 
	 * }
	 */

	@RequestMapping(value = "/submitAssignmentByOneInGroupForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResponseEntity<String> submitAssignmentByOneInGroupForApp(
			@RequestPart(value = "file") MultipartFile file,
			@RequestParam(value = "assignmentSubmission") String assignmentSubmissionJson,
			@RequestParam(value = "username") String usernamee, HttpServletResponse resp, Principal principal,
			@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), usernamee);
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			return ResponseEntity.ok(new Gson().toJson(map));
		}
		assignmentSubmissionJson = decryptRequestParam(assignmentSubmissionJson);
		usernamee = decryptRequestParam(usernamee);
		String json = "";
		logger.info("-------------->submitAssignmentByOneInGroupForApp");

		StudentAssignment assignmentSubmissionJ = new Gson().fromJson(assignmentSubmissionJson,
				StudentAssignment.class);

		Assignment assignment = assignmentService.findByID(assignmentSubmissionJ.getId());
		String username = assignmentSubmissionJ.getUsername();

		StudentAssignment assignmentSubmission = studentAssignmentService.findAssignmentSubmission(username,
				assignmentSubmissionJ.getId());

		int chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId());

		List<String> getAllSapIdsOfStudentFromAssignment = studentAssignmentService
				.getAllSapIdsOfStudentFromAssignment(assignmentSubmissionJ.getId(), assignmentSubmission.getGroupId());

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		int checkEvaluationStatus = studentAssignmentService.checkAssignmentEvaluationStatus(username,
				assignment.getId());
		logger.info("checkEvaluationStatus " + checkEvaluationStatus);

		String approvalStatus = studentAssignmentService.getApprovalStatusForAssignment(username, assignment.getId());
		logger.info("approvalStatus " + approvalStatus);

		if (checkEvaluationStatus > 0) {
			logger.info("yahn1----------------------------------------------------------------------------->");
			assignmentSubmissionJ.setAssignmentError("Assignment is already evaluated by faculty.");
			assignmentSubmissionJ.setAssignmentNote("You cannot resubmit assignment now.");
			assignmentSubmissionJ.setEvaluationStatus("Y");
			assignmentSubmissionJ.setAssignmentCompleted("Y");
			assignmentSubmissionJ.setAssignmentStatus("Completed");

			json = new Gson().toJson(assignmentSubmissionJ);
			return ResponseEntity.ok(json);
		} else if (null != approvalStatus && approvalStatus.contains("Reject")) {
			logger.info("yahn2----------------------------------------------------------------------------->");
			assignmentSubmissionJ.setAssignmentError("Faculty has already rejected your assignment");
			assignmentSubmissionJ.setAssignmentNote("You cannot resubmit assignment now.");
			assignmentSubmissionJ.setEvaluationStatus("Y");
			assignmentSubmissionJ.setAssignmentCompleted("Y");
			assignmentSubmissionJ.setAssignmentStatus("Completed___Evaluated___Rejected");

			json = new Gson().toJson(assignmentSubmissionJ);
			return ResponseEntity.ok(json);
		} else {
			logger.info("yahn3----------------------------------------------------------------------------->");
			try {
				if (extension.equals("zip")) {
					logger.info("zip--------->submitAssignmentByOneInGroupForApp");
					File normalFile = studentAssignmentController.convert(file);
					System.out.println("normalPath_aws-----> " + normalFile.getPath());
					System.out.println("normalPath_aws_Rep-----> " + normalFile.getPath().replace("\\", "/"));
					Map<String, String> mapOfFiles = studentAssignmentController.unzip(
							normalFile.getPath().replace("\\", "/"), submissionFolder,
							getAllSapIdsOfStudentFromAssignment,
							assignment.getAssignmentName() + '-' + assignment.getId());

					if (mapOfFiles.containsKey("Error")) {
						logger.info("Error_From1----> " + mapOfFiles.get("Error"));
						assignmentSubmissionJ.setAssignmentError(mapOfFiles.get("Error"));
						assignmentSubmissionJ.setAssignmentStatus("Pending");
						json = new Gson().toJson(assignmentSubmissionJ);

						logger.info("json----->" + json);

						return ResponseEntity.ok(json);
					} else if (mapOfFiles.containsKey("ErrorInUploading")) {
						logger.info("Error_From2----> " + mapOfFiles.get("ErrorInUploading"));
						assignmentSubmissionJ.setAssignmentError(mapOfFiles.get("ErrorInUploading"));
						json = new Gson().toJson(assignmentSubmissionJ);

						logger.info("json----->" + json);

						return ResponseEntity.ok(json);

					} else {

						if (mapOfFiles.size() == getAllSapIdsOfStudentFromAssignment.size()) {

							if (chkStartDate == 0) {

								studentAssignmentService.setSubmissionStatusForGroupAssignment(assignment.getId());
								assignmentSubmissionJ.setAssignmentNote(
										"YOUR GROUP ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY. ");

							}
							for (String key : mapOfFiles.keySet()) {

								Map<String, String> submissionStatus = submitAssignmentByIdForApp(assignment.getId(),
										key, mapOfFiles.get(key), principal);

								if (submissionStatus.containsKey("Plag" + key)) {
									assignmentSubmissionJ.setAssignmentError(submissionStatus.get("Plag" + key));
								} else {

									studentAssignmentService.setSubmissionStatusForGroupAssignment(assignment.getId());
									assignmentSubmissionJ.setAssignmentSuccess("Assignment Submitted Successfully");
								}
							}

							assignmentSubmissionJ.setAssignmentStatus("Not Evaluated");
						} else {
							assignmentSubmissionJ.setAssignmentStatus("Pending");
							assignmentSubmissionJ.setAssignmentError(
									"No. of files doesn't match with No. Of Students in the Group. ");
							json = new Gson().toJson(assignmentSubmissionJ);

							logger.info("json----->" + json);

							return ResponseEntity.ok(json);
						}
					}

				} else {
					logger.info("yahn5----------------------------------------------------------------------------->");
					assignmentSubmissionJ.setAssignmentStatus("Pending");
					assignmentSubmissionJ.setAssignmentError("Submitted File is not a zip file. ");
					json = new Gson().toJson(assignmentSubmissionJ);

					logger.info("json----->" + json);

					return ResponseEntity.ok(json);
				}
			} catch (Exception ex) {
				logger.info("yahn6----------------------------------------------------------------------------->");
				logger.error("Exception", ex);
			}

			logger.info("getAssignmentError: " + assignmentSubmissionJ.getAssignmentError());
			logger.info("getAssignmentNote: " + assignmentSubmissionJ.getAssignmentNote());
			logger.info("getAssignmentSuccess: " + assignmentSubmissionJ.getAssignmentSuccess());
			json = new Gson().toJson(assignmentSubmissionJ);

			json = encryptResponseBody(json);
			logger.info("json----->" + json);

			return ResponseEntity.ok(json);
		}

		
	}

	/*
	 * @RequestMapping(value = "/submitAssignmentByIdForApp", method = {
	 * 
	 * RequestMethod.GET, RequestMethod.POST }) public Map<String, String>
	 * submitAssignmentByIdForApp(
	 * 
	 * @RequestParam("assignmentId") Long assignmentId, @RequestParam("username")
	 * String username,
	 * 
	 * @RequestParam("filePath") String filePath, Principal principal) {
	 * 
	 * // m.addAttribute("webPage", new //
	 * WebPage("assignment","Submit Assignment",false, false, true, true, //
	 * false));
	 * 
	 * StudentAssignment assignmentSubmission = studentAssignmentService
	 * .findByID(studentAssignmentId);
	 * 
	 * Map<String, String> copyLeaksMsg = new HashMap<>(); StudentAssignment
	 * assignmentByStudent =
	 * studentAssignmentService.findAssignmentSubmission(username, assignmentId);
	 * 
	 * logger.info("is Submitter------>" +
	 * assignmentByStudent.getIsSubmitterInGroup());
	 * 
	 * String submittingUser = username;
	 * 
	 * assignmentByStudent.setSubmissionDate(Utils.getInIST());
	 * 
	 * assignmentByStudent.setIsSubmitterInGroup("Y");
	 * 
	 * assignmentByStudent.setLastModifiedBy(submittingUser);
	 * assignmentByStudent.setStudentFilePath(filePath);
	 * assignmentByStudent.setFilePreviewPath(filePath);
	 * 
	 * Assignment assignment =
	 * assignmentService.findByID(assignmentByStudent.getAssignmentId());
	 * 
	 * String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentByStudent.getSubmissionDate());
	 * assignmentByStudent.setActive("Y");
	 * 
	 * int chkStartDate =
	 * studentAssignmentService.chkStartandEndDateOfAssignment(username,
	 * assignment.getId());
	 * 
	 * int chkStartAndEndDate =
	 * studentAssignmentService.chkStartandEndDtOfAssignment(username,
	 * assignment.getId());
	 * 
	 * 
	 * if (sdf.compareTo(assignment.getEndDate()) > 0) {
	 * 
	 * assignmentSubmission.setSubmissionStatus("Y");
	 * 
	 * logger.info("submssn status ----------------->  Y"); } else {
	 * 
	 * logger.info("submssn status ----------------->  N");
	 * 
	 * assignmentSubmission.setSubmissionStatus("N"); }
	 * 
	 * 
	 * if (chkStartAndEndDate > 0) {
	 * 
	 * assignmentByStudent.setSubmissionStatus("Y");
	 * 
	 * } else {
	 * 
	 * assignmentByStudent.setSubmissionStatus("N"); }
	 * 
	 * assignmentByStudent.setStartDate(assignment.getStartDate());
	 * 
	 * assignmentByStudent.setEndDate(assignment.getEndDate());
	 * 
	 * if (chkStartDate == 0) { // setNote(m,"Assignment Submission has not started
	 * yet or deadline is // missed!!"); assignmentByStudent.
	 * setAssignmentNote("Assignment Submission has not started yet or deadline is missed!! "
	 * );
	 * 
	 * } else {
	 * 
	 * if (!filePath.isEmpty()) { Date date = new Date();
	 * 
	 * String errorMessage = null;
	 * 
	 * Map<String, Integer> plagValueMap = new HashMap<String, Integer>();
	 * 
	 * if (errorMessage == null) {
	 * 
	 * CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username,
	 * assignment.getId());
	 * 
	 * boolean isPresent = false; boolean copyleakscheck = false; if (cplAudit ==
	 * null) { isPresent = false; copyleakscheck = true; } else {
	 * 
	 * isPresent = true; if (cplAudit.getCount() >= 2) { copyleakscheck = false;
	 * 
	 * } else { copyleakscheck = true; }
	 * 
	 * }
	 * 
	 * if (assignment.getPlagscanRequired().equals("Yes") &&
	 * assignment.getRunPlagiarism().equals("Submission") && copyleakscheck == true)
	 * {
	 * 
	 * if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))
	 * 
	 * { // CopyLeaks copyLeaks = new CopyLeaks();
	 * 
	 * try { File storedFile =
	 * submit.multipartToFile(assignmentByStudent.getStudentFilePath());
	 * plagValueMap = copyLeaks.scan(copyleaksId, copyleaskKey, storedFile); String
	 * url = ""; Integer plagValue = 0; Integer creditUsed = 0;
	 * 
	 * if (plagValueMap.isEmpty()) { assignmentByStudent.setThreshold(0);
	 * assignmentByStudent.setUrl(" "); } else { for (String key :
	 * plagValueMap.keySet()) { url = key;
	 * 
	 * if (url != null) {
	 * 
	 * } else { url = "Copyleaks internal database"; } plagValue =
	 * plagValueMap.get(key);
	 * 
	 * if (url == "creditUsed") {
	 * 
	 * creditUsed = plagValue;
	 * 
	 * } else { assignmentByStudent.setUrl(url);
	 * assignmentByStudent.setThreshold(plagValue); }
	 * 
	 * 
	 * assignmentSubmission.setUrl(url); assignmentSubmission
	 * .setThreshold(plagValue);
	 * 
	 * 
	 * } }
	 * 
	 * if (isPresent == false) {
	 * 
	 * CopyleaksAudit cpl = new CopyleaksAudit();
	 * cpl.setAssignmentId(assignment.getId()); cpl.setUsername(username);
	 * cpl.setCount(1); cpl.setCreditUsed(creditUsed); cpl.setCreatedBy(username);
	 * cpl.setLastModifiedBy(username);
	 * 
	 * copyleaksAuditService.insert(cpl);
	 * 
	 * } else {
	 * 
	 * cplAudit.setCount(cplAudit.getCount() + 1);
	 * cplAudit.setCreditUsed(cplAudit.getCreditUsed() + creditUsed);
	 * 
	 * copyleaksAuditService.update(cplAudit);
	 * 
	 * }
	 * 
	 * // logger.info("plagValue--" + plagValueMap); if (assignment.getThreshold()
	 * == null) { assignment.setThreshold(50); } else {
	 * 
	 * } if (plagValue >= assignment.getThreshold()) { String key = "Plag" +
	 * username;
	 * 
	 * setError(m, "Uploaded File does not fall under plag threshold " + plagValue +
	 * "% copied from " + url); copyLeaksMsg.put(key, username +
	 * " has a file which fall under plag threshold " +plagValue + "% copied from "
	 * + url);
	 * 
	 * assignmentByStudent
	 * .setAssignmentError("Uploaded File does not fall under plag threshold " +
	 * plagValue + "% copied from " + url); copyLeaksMsg.put(key, username +
	 * " has a file which fall under plag threshold " + plagValue + "% copied from "
	 * + url);
	 * 
	 * } else { String key = "NoPlag" + username;
	 * studentAssignmentService.update(assignmentByStudent);
	 * studentAssignmentAuditService.insert(assignmentByStudent); //
	 * 
	 * setSuccess(m, "Assigment Answer file uploaded successfully" );
	 * 
	 * assignmentByStudent
	 * .setAssignmentSuccess("Assigment Answer file uploaded successfully. ");
	 * String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentByStudent.getSubmissionDate());
	 * 
	 * if (chkStartAndEndDate == 0) { assignmentByStudent.setAssignmentNote(
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY. "
	 * ); // setNote(m,"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY
	 * // RESPECTIVE FACULTY"); } else { assignmentByStudent
	 * .setAssignmentSuccess("Assigment Answer file uploaded successfully. "); //
	 * setSuccess(m,"Assigment Answer file uploaded successfully"); }
	 * 
	 * copyLeaksMsg.put(key, "No Plagiarism"); } } catch (Exception e) {
	 * logger.error(e.getMessage()); } }
	 * 
	 * } else { try {
	 * 
	 * studentAssignmentService.update(assignmentByStudent);
	 * studentAssignmentAuditService.insert(assignmentByStudent); // //
	 * setSuccess(m,"Assigment Answer file uploaded successfully");
	 * assignmentByStudent.
	 * setAssignmentSuccess("Assigment Answer file uploaded successfully. ");
	 * 
	 * String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	 * 
	 * .format(assignmentByStudent.getSubmissionDate());
	 * 
	 * if (chkStartAndEndDate == 0) { // setNote(m,"YOUR ASSIGNMENT WILL GET
	 * SUBMITTED ONLY IF IT'S APPROVED BY // RESPECTIVE FACULTY");
	 * assignmentByStudent.setAssignmentNote(
	 * "YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY. "
	 * ); } else { // setSuccess(m,"Assigment Answer file uploaded successfully");
	 * assignmentByStudent
	 * .setAssignmentSuccess("Assigment Answer file uploaded successfully. "); } }
	 * catch (Exception e) { logger.error(e.getMessage()); }
	 * 
	 * }
	 * 
	 * } else { // setError(m, errorMessage);
	 * assignmentByStudent.setAssignmentError(errorMessage); }
	 * 
	 * } else { // setError(m, "File is empty, please upload correct file.");
	 * assignmentByStudent.
	 * setAssignmentError("File is empty, please upload correct file. "); }
	 * 
	 * }
	 * 
	 * if (assignment.getRunPlagiarism() != null) { if
	 * (assignment.getRunPlagiarism().equals("Manual")) { //
	 * m.addAttribute("showCheckForPlagiarism", true); } }
	 * 
	 * m.addAttribute("assignment", assignment); assignmentByStudent =
	 * 
	 * studentAssignmentService.findAssignmentSubmission(username,
	 * 
	 * assignmentByStudent.getId());
	 * 
	 * // m.addAttribute("assignmentSubmission", assignmentByStudent);
	 * 
	 * return copyLeaksMsg; }
	 */

	@RequestMapping(value = "/submitAssignmentByIdForApp", method = {
			RequestMethod.GET, RequestMethod.POST })
	public Map<String, String> submitAssignmentByIdForApp(

			@RequestParam("assignmentId") Long assignmentId, @RequestParam("username") String username,

			@RequestParam("filePath") String filePath, Principal principal) {

		Map<String, String> copyLeaksMsg = new HashMap<>();
		StudentAssignment assignmentByStudent = studentAssignmentService.findAssignmentSubmission(username,
				assignmentId);

		logger.info("is Submitter------>" + assignmentByStudent.getIsSubmitterInGroup());

		String submittingUser = username;

		assignmentByStudent.setSubmissionDate(Utils.getInIST());

		assignmentByStudent.setLastModifiedBy(submittingUser);
		assignmentByStudent.setStudentFilePath(filePath.replace("\\", "/"));
		assignmentByStudent.setFilePreviewPath(filePath.replace("\\", "/"));

		Assignment assignment = assignmentService.findByID(assignmentByStudent.getAssignmentId());

		String sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

				.format(assignmentByStudent.getSubmissionDate());
		assignmentByStudent.setActive("Y");

		int chkStartDate = studentAssignmentService.chkStartandEndDateOfAssignment(username, assignment.getId());

		int chkStartAndEndDate = studentAssignmentService.chkStartandEndDtOfAssignment(username, assignment.getId());

		if (chkStartAndEndDate > 0) {
			assignmentByStudent.setSubmissionStatus("Y");
		} else {
			assignmentByStudent.setSubmissionStatus("N");
		}

		assignmentByStudent.setStartDate(assignment.getStartDate());

		assignmentByStudent.setEndDate(assignment.getEndDate());

		if (chkStartDate == 0) {
			assignmentByStudent.setAssignmentNote("Assignment Submission has not started yet or deadline is missed!! ");
		}

		else {

			if (!filePath.isEmpty()) {
				Date date = new Date();

				String errorMessage = null;

				Map<String, Integer> plagValueMap = new HashMap<String, Integer>();

				if (errorMessage == null) {

					CopyleaksAudit cplAudit = copyleaksAuditService.getRecordByUsername(username, assignment.getId());

					boolean isPresent = false;
					boolean copyleakscheck = false;
					if (cplAudit == null) {
						isPresent = false;
						copyleakscheck = true;
					} else {

						isPresent = true;
						if (cplAudit.getCount() >= 2) {
							copyleakscheck = false;

						} else {
							copyleakscheck = true;
						}

					}

					if (assignment.getPlagscanRequired().equals("Yes")
							&& assignment.getRunPlagiarism().equals("Submission") && copyleakscheck == true) {

						if (plagiarismCheck.equalsIgnoreCase("copyLeaks"))

						{ // CopyLeaks copyLeaks = new CopyLeaks();

							try {

								//

								File storedFile = submit.multipartToFileForS3(assignmentByStudent.getStudentFilePath());
								String scanId = app.replaceAll("-", "").toLowerCase() + "-" + assignment.getId() + "-"
										+ username + "-" + RandomStringUtils.randomNumeric(2, 2);

								Map<String, String> scanStatus = copyLeaks.scanFileForCopyleaks(copyleaksId,
										copyleaskKey, storedFile, scanId);
								if (scanStatus.containsKey("Error")) {
									// setError(rd,scanStatus.get("Error"));
									logger.info("Error Occurred");
								} else {
									StudentAssignment auditAssignmentSubmission = studentAssignmentAuditService
											.findAssignmentSubmission(username, assignment.getId());
									if (null == auditAssignmentSubmission) {
										studentAssignmentAuditService.insert(assignmentByStudent);
										auditAssignmentSubmission = studentAssignmentAuditService
												.findAssignmentSubmission(username, assignment.getId());
										logger.info("Waiting...");
										long waitingTime = 0;
										while (null == auditAssignmentSubmission.getThreshold()) {
											Thread.sleep(5000);
											waitingTime = waitingTime + 5000;
											logger.info("Waiting..." + auditAssignmentSubmission.getThreshold());
											// wait for 4.30 mins only
											if (waitingTime > 258000) {
												break;
											}
											auditAssignmentSubmission = studentAssignmentAuditService
													.findAssignmentSubmission(username, assignment.getId());
											logger.info("Checking..." + auditAssignmentSubmission.getThreshold());
										}

										if (null != auditAssignmentSubmission.getThreshold()) {
											logger.info("Threshold--->" + auditAssignmentSubmission.getThreshold());
											if (assignment.getThreshold() <= auditAssignmentSubmission.getThreshold()) {
												// setError(rd, "Uploaded File does not fall under plagiarism threshold
												// " + auditAssignmentSubmission.getThreshold()
												// + "% copied from " + auditAssignmentSubmission.getUrl());
												String key = "Plag" + username;
												assignmentByStudent.setAssignmentError(
														"Uploaded File does not fall under plag threshold "
																+ auditAssignmentSubmission.getThreshold()
																+ "% copied from "
																+ auditAssignmentSubmission.getUrl());
												assignmentByStudent.setAssignmentStatus("Pending");

												copyLeaksMsg.put(key,
														username + " has a file which fall under plag threshold "
																+ auditAssignmentSubmission.getThreshold()
																+ "% copied from "
																+ auditAssignmentSubmission.getUrl());

												//

											} else {
												assignmentByStudent
														.setThreshold(auditAssignmentSubmission.getThreshold());
												assignmentByStudent.setUrl(auditAssignmentSubmission.getUrl());
												studentAssignmentService.update(assignmentByStudent);
												/*
												 * if (chkStartAndEndDate == 0) { setNote(
												 * rd,"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY"
												 * ); } else { setSuccess(rd,
												 * "Assigment Answer file uploaded successfully"); }
												 */

												studentAssignmentService.update(assignmentByStudent);
												// studentAssignmentAuditService.insert(assignmentSubmission); //
												assignmentByStudent.setAssignmentSuccess(
														"Assignment Answer file uploaded successfully. ");
												logger.info(assignmentByStudent.getAssignmentSuccess());

												String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

														.format(assignmentByStudent.getSubmissionDate());

												// set submissionDate
												Date submissionDate = Utils.getInIST();
												SimpleDateFormat simpledateformat = new SimpleDateFormat(
														"yyyy-MM-dd HH:mm:ss");
												String submissionDateString = simpledateformat.format(submissionDate);
												logger.info("submissionDateString----> " + submissionDateString);
												studentAssignmentService.setSubmissionDate(submissionDateString,
														assignment.getId(), username);

												if (chkStartAndEndDate == 0) {
													assignmentByStudent.setAssignmentSuccess(
															"Your assignment will get submitted only if it's approved by respective faculty. ");
													logger.info(assignmentByStudent.getAssignmentSuccess());
													assignmentByStudent.setAssignmentStatus("Not Evaluated");

												} else {
													assignmentByStudent.setAssignmentSuccess(
															"Assignment Answer file uploaded successfully. ");
													logger.info(assignmentByStudent.getAssignmentSuccess());
													assignmentByStudent.setAssignmentStatus("Not Evaluated");

												}
												String key = "NoPlag" + assignmentByStudent.getUsername();

												copyLeaksMsg.put(key, "No Plagiarism");

											}
										} else {
											assignmentByStudent
													.setAssignmentError("File takes too long time for plagiarism.");
											assignmentByStudent.setAssignmentStatus("Pending");
											logger.info("File takes too long time for plagiarism.");
										}
									}

									//

								}
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}

					} else {
						try {

							studentAssignmentService.update(assignmentByStudent);
							studentAssignmentAuditService.insert(assignmentByStudent); //
							// setSuccess(m,"Assigment Answer file uploaded successfully");
							assignmentByStudent.setAssignmentSuccess("Assigment Answer file uploaded successfully. ");

							String sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

									.format(assignmentByStudent.getSubmissionDate());

							if (chkStartAndEndDate == 0) {
								// setNote(m,"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY
								// RESPECTIVE FACULTY");
								assignmentByStudent.setAssignmentNote(
										"YOUR ASSIGNMENT WILL GET SUBMITTED ONLY IF IT'S APPROVED BY RESPECTIVE FACULTY. ");
							} else {
								// setSuccess(m,"Assigment Answer file uploaded successfully");
								assignmentByStudent
										.setAssignmentSuccess("Assigment Answer file uploaded successfully. ");
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}

					}

				} else {
					// setError(m, errorMessage);
					assignmentByStudent.setAssignmentError(errorMessage);
				}

			} else {
				// setError(m, "File is empty, please upload correct file.");
				assignmentByStudent.setAssignmentError("File is empty, please upload correct file. ");
			}

		}

		if (assignment.getRunPlagiarism() != null) {
			if (assignment.getRunPlagiarism().equals("Manual")) {
				// m.addAttribute("showCheckForPlagiarism", true);
			}
		}

		/* m.addAttribute("assignment", assignment); */
		assignmentByStudent =

				studentAssignmentService.findAssignmentSubmission(username,

						assignmentByStudent.getId());

		// m.addAttribute("assignmentSubmission", assignmentByStudent);

		return copyLeaksMsg;
	}

	/*
	 * @RequestMapping(value = "/sendAnnouncementFileForApp", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public ModelAndView
	 * sendAnnouncementFileForApp(@RequestParam(name = "id") String id,
	 * HttpServletRequest request, HttpServletResponse response) {
	 * logger.info("id-------->" + id); String projectUrl = ""; OutputStream
	 * outStream = null; FileInputStream inputStream = null; try {
	 * ServletOutputStream out = response.getOutputStream();
	 * response.setContentType("Content-type: text/zip"); Announcement sa =
	 * announcementService.findByID(Long.valueOf(id)); File src = new
	 * File(sa.getFilePath());
	 * 
	 * String filePath = sa.getFilePath();
	 * 
	 * logger.info("filePath------> " + filePath);
	 * 
	 * List<File> fileList = new ArrayList<>(); if (filePath.contains(",")) {
	 * 
	 * File folderPath = new File(workDir + File.separator + "allFiles");
	 * List<String> files = Arrays.asList(filePath.split(","));
	 * 
	 * if (!folderPath.exists()) { folderPath.mkdir(); }
	 * 
	 * for (String file : files) { File fileNew = new File(file); //
	 * files.add(file);
	 * 
	 * File dest = new File(folderPath.getAbsolutePath() + File.separator +
	 * fileNew.getName()); FileUtils.copyFile(fileNew, dest);
	 * 
	 * } String filename = "announcementFiles.zip";
	 * response.setHeader("Content-Disposition", "attachment; filename=" + filename
	 * + ""); projectUrl = "/" + "workDir" + "/" + folderPath.getName() + ".zip";
	 * pack(folderPath.getAbsolutePath(), out);
	 * FileUtils.deleteDirectory(folderPath); return null;
	 * 
	 * } else {
	 * 
	 * ServletContext context = request.getSession().getServletContext();
	 * 
	 * File dest = new File(workDir + File.separator + src.getName());
	 * 
	 * logger.info("file path----------->" + dest.getAbsolutePath());
	 * 
	 * if (dest.exists()) dest.delete(); projectUrl = "/" + "workDir" + "/" +
	 * dest.getName(); FileUtils.copyFile(src, dest);
	 * 
	 * inputStream = new FileInputStream(dest);
	 * 
	 * // get MIME type of the file String mimeType = context.getMimeType(filePath);
	 * if (mimeType == null) { // set to binary type if MIME mapping not found
	 * mimeType = "application/octet-stream"; }
	 * 
	 * // set content attributes for the response response.setContentType(mimeType);
	 * response.setContentLength((int) dest.length());
	 * 
	 * // set headers for the response String headerKey = "Content-Disposition";
	 * String headerValue = String.format("attachment; filename=\"%s\"",
	 * dest.getName()); response.setHeader(headerKey, headerValue);
	 * 
	 * // get output stream of the response outStream = response.getOutputStream();
	 * IOUtils.copy(inputStream, outStream); }
	 * 
	 * } catch (Exception e) { logger.error("Error", e); }
	 * 
	 * return null; }
	 */

	@RequestMapping(value = "/sendAnnouncementFileForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> sendAnnouncementFileForApp(@RequestParam(name = "id") String id,
			@RequestParam(name = "username") String username, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), username);
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			return null;
		}
		id = decryptRequestParam(id);
		username = decryptRequestParam(username);
		logger.info("id-------->" + id);
		String projectUrl = "";
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			Announcement sa = announcementService.findByID(Long.valueOf(id));
			File src = new File(sa.getFilePath());

			String filePath = sa.getFilePath();

			logger.info("filePath------> " + filePath);

			List<File> fileList = new ArrayList<>();
			if (filePath.contains(",")) {

				File folderPath = new File(
						downloadAllFolderTemp + File.separator + RandomStringUtils.randomAlphanumeric(10));
				List<String> files = Arrays.asList(filePath.split(","));

				if (!folderPath.exists()) {
					folderPath.mkdir();
				}

				for (String file : files) {
					File fileNew = new File(file);
					// files.add(file);

					System.out.println("filePath_ann_aws----> " + file);
					InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
					String ext1 = FilenameUtils.getExtension(fileNew.getName());
					File dest = new File(
							folderPath.getAbsolutePath() + File.separator + fileNew.getName() + "." + ext1);
					FileUtils.copyInputStreamToFile(inpStream, dest);

				}
				String filename = "announcementFiles.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
				projectUrl = "/" + downloadAllFolderTemp + "/" + filename + ".zip";
				pack(folderPath.getAbsolutePath(), out);
				// FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				ServletContext context = request.getSession().getServletContext();
				File downloadFile = new File(filePath);
				filePath = filePath.replace("\\", "/");
				System.out.println("Announcement_download_filePath_aws=====> " + filePath);
				final byte[] data = amazonS3ClientService.getFile(filePath);
				final ByteArrayResource resource = new ByteArrayResource(data);
				return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
						.header("Content-disposition", "attachment; filename=\"" + downloadFile + "\"").body(resource);

			}

		} catch (Exception e) {
			logger.error("Error", e);
		}

		return null;
	}

	/*
	 * @RequestMapping(value = "/getAssignmentListForApp", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * assignmentList(@RequestBody Course course, Model m, Principal principal) {
	 * 
	 * String username = course.getUsername(); Long courseId = course.getId();
	 * 
	 * List<Assignment> assignments = Collections.emptyList();
	 * 
	 * if (courseId != null) { assignments =
	 * assignmentService.findByUserAndCourse(username, courseId); } else {
	 * 
	 * assignments = assignmentService.findByUser(username); }
	 * 
	 * for (Assignment a : assignments) { Course c =
	 * courseService.findByID(a.getCourseId()); a.setCourseName(c.getCourseName());
	 * assignments.set(assignments.indexOf(a), a);
	 * 
	 * }
	 * 
	 * if (assignments.size() == 0 || assignments.isEmpty()) { return
	 * "{\"Status\":\"No Assignment\"}"; }
	 * 
	 * String json = new Gson().toJson(assignments); logger.info("json----->" +
	 * json); return json;
	 * 
	 * }
	 */

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/getTestStats", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTestStats(Principal principal) {

		logger.info("inside test stats");
		String username;
		username = principal.getName();
		Token userDetails = (Token) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_PARENT)) {
			List<StudentParent> studentParent = studentParentService.findStudentsByParentUname(username);
			username = studentParent.get(0).getStud_username();
		}

		try {
			StudentTest testStats = studentTestService.getTestSummaryById(username);

			if (testStats != null) {
				String json = new Gson().toJson(testStats);
				logger.info("json----->" + json);
				return json;
			} else {
				return "[]";
			}

		} catch (Exception e) {
			logger.error("Error", e);
			return "[]";
		}

	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/getAssignmentStats", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssignmentStats(Principal principal) {

		logger.info("inside test stats");
		String username;
		username = principal.getName();
		Token userDetails = (Token) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_PARENT)) {
			List<StudentParent> studentParent = studentParentService.findStudentsByParentUname(username);
			username = studentParent.get(0).getStud_username();
		}

		try {
			Assignment assignStats = assignmentService.getAssignmentSummaryById(username);

			if (assignStats != null) {
				String json = new Gson().toJson(assignStats);
				logger.info("json----->" + json);
				return json;
			} else {
				return "[]";
			}

		} catch (Exception e) {
			logger.error("Error", e);
			return "[]";
		}

	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/getTestStatsBySem", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTestStatsBySem(@RequestParam(name = "acadSess") String acadSession,
			@RequestParam(name = "courseId", required = false) String courseId, Principal principal) {

		logger.info("AcadSession---->" + acadSession);
		String username;
		username = principal.getName();
		Token userDetails = (Token) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_PARENT)) {
			List<StudentParent> studentParent = studentParentService.findStudentsByParentUname(username);
			username = studentParent.get(0).getStud_username();
		}

		try {
			StudentTest testStats = null;
			if (courseId == null || courseId.equals("")) {
				testStats = studentTestService.getTestSummaryByIdAndSem(username, acadSession);
			} else {
				testStats = studentTestService.getTestSummaryByIdAndSemAndCourse(username, acadSession, courseId);
			}
			if (testStats != null) {
				String json = new Gson().toJson(testStats);
				logger.info("json----->" + json);
				return json;
			} else {
				return "[]";
			}

		} catch (Exception e) {
			logger.error("Error", e);
			return "[]";
		}

	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/getAssignmentStatsBySem", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssignmentStatsBySem(@RequestParam(name = "acadSess") String acadSession,
			@RequestParam(name = "courseId", required = false) String courseId, Principal principal) {

		logger.info("AcadSession---->" + acadSession);
		logger.info("CourseId---->" + courseId);
		String username;
		username = principal.getName();
		Token userDetails = (Token) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_PARENT)) {
			List<StudentParent> studentParent = studentParentService.findStudentsByParentUname(username);
			username = studentParent.get(0).getStud_username();
		}

		try {
			Assignment assignStats = null;
			if (courseId == null || courseId.equals("")) {
				assignStats = assignmentService.getAssignmentSummaryByIdAndSem(username, acadSession);
			} else {
				assignStats = assignmentService.getAssignmentSummaryByIdAndSemAndCourse(username, acadSession,
						courseId);
			}
			if (assignStats != null) {
				String json = new Gson().toJson(assignStats);
				logger.info("json----->" + json);
				return json;
			} else {
				return "[]";
			}

		} catch (Exception e) {
			logger.error("Error", e);
			return "[]";
		}

	}

	
	@RequestMapping(value = "/downloadAttendanceReport", method = RequestMethod.GET)
	public String downloadAttendanceReport(@RequestParam String ofDate, Model m, Principal p,
			HttpServletResponse response, HttpServletRequest request) {

		logger.info("Inside mapping------>");

		List<String> facultyList = userService.findFacultyUsernamesForAttendanceReport();

		MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

		DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

				.createDefaultConnectionByDS(defaultUrl, defaultUsername,

						defaultPassword);

		DriverManagerDataSource dataSourceTimetable = multipleDBConnection

				.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
						"timetable_metadata");

		timetableDAO.setDS(dataSourceTimetable);

		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = Utils.getInIST();
		try {
			dt = dateFormat1.parse(ofDate);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String curDate = "%" + dateFormat.format(dt) + "%";

		List<Timetable> timetableList = timetableDAO.getTimetableDataByFaculties(curDate, facultyList);

		timetableDAO.setDS(dataSourceDefaultLms);

		List<Timetable> newTimetableLst = new ArrayList<>();

		for (Timetable t : timetableList) {

			logger.info("Inside first for------>");

			if (t.getProgramId().equals("")) {

			} else if (t.getProgramId().contains(",")) {

				String[] programidsStrings = t.getProgramId().split(" , ");

				for (int i = 0; i < programidsStrings.length; i++) {
					Program prg = programService.findByID(Long.parseLong(programidsStrings[i]));
					Timetable tSplit = new Timetable();
					tSplit.setProgramId(programidsStrings[i]);
					tSplit.setEventId(t.getEventId());
					tSplit.setFacultyId(t.getFacultyId());
					tSplit.setInput_json(t.getInput_json());
					tSplit.setCreated_by(t.getCreated_by());
					tSplit.setCreated_on(t.getCreated_on());
					tSplit.setLast_updated_by(t.getLast_updated_by());
					tSplit.setLast_updated_on(t.getLast_updated_on());

					newTimetableLst.add(tSplit);
				}
			} else {
				Program prg = programService.findByID(Long.parseLong(t.getProgramId()));

				logger.info("Inside first for prg------>" + prg);
				if (prg != null) {
					if (null != prg.getId()) {

						newTimetableLst.add(t);

					}
				}
			}

		}

		logger.info("TimetableList------>" + newTimetableLst);

		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
		String curDate1 = "%" + dateFormat2.format(dt) + "%";

		List<StudentCourseAttendance> scaList = studentCourseAttendanceService
				.getStudentCourseAttendanceForReport(curDate1, facultyList);

		List<StudentCourseAttendance> reportListFacultyWise = new ArrayList<>();

		for (String facultyId : facultyList) {

			logger.info("Inside second for------>");

			Long totalCount = newTimetableLst.stream().filter(o -> o.getFacultyId().equals(facultyId)).count();
			String atdTakenCount = "0";
			if (totalCount > 0) {
				if (scaList.stream().filter(o -> o.getFacultyId().equals(facultyId)).findFirst().isPresent()) {
					atdTakenCount = scaList.stream().filter(o -> o.getFacultyId().equals(facultyId)).findFirst().get()
							.getAttendanceTakenCount();
				}
			}

			StudentCourseAttendance sca = new StudentCourseAttendance();
			User facultyDetails = userService.findUserWithCampusByUserName(facultyId);

			sca.setFacultyId(facultyId);
			sca.setFirstname(facultyDetails.getFirstname());
			sca.setLastname(facultyDetails.getLastname());
			sca.setTotal_count(totalCount.toString());
			sca.setAttendanceTakenCount(atdTakenCount);
			sca.setCampusId(facultyDetails.getCampusId() == null ? null : facultyDetails.getCampusId().toString());
			sca.setCampusName(facultyDetails.getCampusName() == null ? null : facultyDetails.getCampusName());

			reportListFacultyWise.add(sca);
		}

		logger.info("Final TimetableReportList------>" + reportListFacultyWise);

		String filePath = "";
		filePath = studentCourseAttendanceService.getXlsxforMarkAttendanceReport(reportListFacultyWise);

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		ServletContext context = request.getSession().getServletContext();
		File downloadFile = new File(filePath);
		try {
			inputStream = new FileInputStream(downloadFile);
			outStream = response.getOutputStream();

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}

			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	/* ADDED 23rd may */
	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/facultyTestDashboard", method = RequestMethod.GET)
	public String facultyTestDashboard(@ModelAttribute("user") User user, Model m, Principal principal) {
		List<DashBoard> listOfDashBoardElements = new ArrayList<DashBoard>();

		String username = principal.getName();

		User u = userService.findByUserName(username);
		Token userDetails = (Token) principal;

		logger.info("courseList---->" + userDetails.getCourseList());

		HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();

		m.addAttribute("webPage", new WebPage("facultyHomePage", "Faculty HomePage", false, false));
		m.addAttribute("courses", courseService.findByUserActive(username, userDetails.getProgramName()));

		List<Course> courseDetails = courseService.findByUserActive(username, userDetails.getProgramName());
		Map<String, List<DashBoard>> sessionWiseCourseListMap = new HashMap<>();
		Set<String> acadSessionList = new HashSet<>();

		for (Course c : courseDetails) {
			acadSessionList.add(c.getAcadSession() + "-" + c.getAcadYear());

		}

		m.addAttribute("assignments", assignmentService.findByFaculty(username));
		m.addAttribute("tests", testService.findByFaculty(username));
		m.addAttribute("announcements", announcementService.findAnnouncementsByUser(username));
		listOfDashBoardElements = dashBoardService.listOfDashBoardElements(username, userDetails.getProgramName(),
				userDetails.getCourseList());

		for (String acadSession : acadSessionList) {
			List<DashBoard> dashboardList = new ArrayList<>();
			for (DashBoard c : listOfDashBoardElements) {

				if (c.getCourse().getAcadSession() != null && c.getCourse().getAcadYear() != null) {
					String key = c.getCourse().getAcadSession() + "-" + c.getCourse().getAcadYear();
					logger.info("key---------->" + key);
					if (key.equals(acadSession)) {

						dashboardList.add(c);

					}

				}
			}
			sessionWiseCourseListMap.put(acadSession, dashboardList);
		}
		logger.info("sessionWiseCourseListMap--->" + sessionWiseCourseListMap);
		m.addAttribute("receivedMessage", dashBoardService.getReceivedMessage(username));
		m.addAttribute("sessionWiseCourseListMap", sessionWiseCourseListMap);

		for (Course c : userDetails.getCourseList()) {

			if (!sessionWiseCourseList.containsKey(c.getAcadSession())) {
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			} else {
				List<Course> courseLst = sessionWiseCourseList.get(c.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}

		}

		logger.info("sessionWiseCourseList------------>" + sessionWiseCourseList);
		logger.info("u acadSession" + u.getAcadSession());
		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBean", u);

		return "test/testDashboard";
	}

	/* For faculty side on assignment dashboard page */
	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/facultyAssignmentDashboard", method = RequestMethod.GET)
	public String facultyAssignmentDashboard(@ModelAttribute("user") User user, Model m, Principal principal) {
		List<DashBoard> listOfDashBoardElements = new ArrayList<DashBoard>();

		String username = principal.getName();

		User u = userService.findByUserName(username);
		Token userDetails = (Token) principal;

		logger.info("courseList---->" + userDetails.getCourseList());

		HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();

		m.addAttribute("webPage", new WebPage("facultyHomePage", "Faculty HomePage", false, false));
		m.addAttribute("courses", courseService.findByUserActive(username, userDetails.getProgramName()));

		List<Course> courseDetails = courseService.findByUserActive(username, userDetails.getProgramName());
		Map<String, List<DashBoard>> sessionWiseCourseListMap = new HashMap<>();
		Set<String> acadSessionList = new HashSet<>();

		for (Course c : courseDetails) {
			acadSessionList.add(c.getAcadSession() + "-" + c.getAcadYear());

		}

		m.addAttribute("assignments", assignmentService.findByFaculty(username));
		m.addAttribute("tests", testService.findByFaculty(username));
		m.addAttribute("announcements", announcementService.findAnnouncementsByUser(username));
		listOfDashBoardElements = dashBoardService.listOfDashBoardElements(username, userDetails.getProgramName(),
				userDetails.getCourseList());

		for (String acadSession : acadSessionList) {
			List<DashBoard> dashboardList = new ArrayList<>();
			for (DashBoard c : listOfDashBoardElements) {

				if (c.getCourse().getAcadSession() != null && c.getCourse().getAcadYear() != null) {
					String key = c.getCourse().getAcadSession() + "-" + c.getCourse().getAcadYear();
					logger.info("key---------->" + key);
					if (key.equals(acadSession)) {

						dashboardList.add(c);

					}

				}
			}
			sessionWiseCourseListMap.put(acadSession, dashboardList);
		}
		logger.info("sessionWiseCourseListMap--->" + sessionWiseCourseListMap);
		m.addAttribute("receivedMessage", dashBoardService.getReceivedMessage(username));
		m.addAttribute("sessionWiseCourseListMap", sessionWiseCourseListMap);
		httpSession.setAttribute("sessionWiseCourseListMapper", sessionWiseCourseListMap);

		for (Course c : userDetails.getCourseList()) {

			if (!sessionWiseCourseList.containsKey(c.getAcadSession())) {
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			} else {
				List<Course> courseLst = sessionWiseCourseList.get(c.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}

		}

		logger.info("sessionWiseCourseList------------>" + sessionWiseCourseList);

		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBean", u);

		return "assignment/assignmentDashboard";
	}

	// ------------------------------TCS----------------------------------//
	@RequestMapping(value = { "/samlRequestTCS" }, method = {
			RequestMethod.GET, RequestMethod.POST })
	public String samlRequestTCS(@RequestParam(name = "SAMLRequest") String SAMLRequest, HttpServletResponse resp,
			Principal p, RedirectAttributes r, Model m) {

		logger.info("SAML Request TCS ------>" + SAMLRequest);

		if (SAMLRequest.isEmpty()) {

			setError(r, "Request not found!!");

			return "redirect:/homepage";

		}

		// Base64 decoder

		byte[] decoded = Base64.decodeBase64(SAMLRequest);

		logger.info("SAML Request decoded TCS ------>" + decoded);

		String xmlToTcs = "";

		// Inflater

		try {

			Inflater decompresser = new Inflater(true);

			decompresser.setInput(decoded);

			byte[] result = new byte[1024];

			String inflated = "";

			long limit = 0;

			while (!decompresser.finished() && limit < 150) {

				int resultLength = decompresser.inflate(result);

				limit += 1;

				inflated += new String(result, 0, resultLength, "UTF-8");

			}

			decompresser.end();

			logger.info("SAML Request inflated TCS------>" + inflated);

			String msg = inflated;

			DocumentBuilder newDocumentBuilder = DocumentBuilderFactory

					.newInstance().newDocumentBuilder();

			Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(

					msg.getBytes()));

			logger.info("Destination TCS-->"

					+ parse.getChildNodes().item(0).getAttributes()

							.getNamedItem("Destination").getTextContent());

			logger.info("EntityId TCS-->"

					+ parse.getFirstChild().getChildNodes().item(0)

							.getTextContent());

			String destination = parse.getChildNodes().item(0).getAttributes()

					.getNamedItem("Destination").getTextContent();

			String entity = parse.getFirstChild().getChildNodes().item(0)

					.getTextContent();

			User u = new User();

			if (entity.equals(tcsEntity)) {

				if (p != null) {

					u = userService.findByUserName(p.getName());

					logger.info("firstname-->" + u.getFirstname());

					logger.info("lastname-->" + u.getLastname());

					logger.info("emailId-->" + u.getEmail());

					logger.info("Username-->" + u.getUsername());

				} else {

					logger.info("logoutcalled");

					setError(r, "Session Time out!!");

					return "redirect:/loggedout";

				}

				try {

					String xmlRes = buildResponseXMLString(u, entity, destination);

					Response response = generateSamlResponseObject(xmlRes);

					String strFinalResponse = canonicalizeSamlResponse(xmlRes);

					/* signSamlResponseObject2(xmlRes,strFinalResponse,res_Id); */

					ClassLoader cl = getClass().getClassLoader();

					File f = new File(cl.getResource("./static/resources/templates/keystore.jks").getFile());

					InputStream fis = new FileInputStream(f);

					KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

					ks.load(fis, "javacaps".toCharArray());

					fis.close();

					// Get Private Key Entry From keystore

					KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks

							.getEntry("keystore", new KeyStore.PasswordProtection(

									"javacaps".toCharArray()));

					PrivateKey privKey = pkEntry.getPrivateKey();

					PublicKey pubKey = ks.getCertificate("keystore").getPublicKey();

					X509Certificate cert = (X509Certificate) ks

							.getCertificate("keystore");

					Document doc = convertStringToDocument(strFinalResponse);

					String signAlgorithm = "";

					String digestAlgorithm = "";

					xmlToTcs = addSign(doc, privKey, cert, signAlgorithm, digestAlgorithm);

					/* ObjectMapper mapper = new ObjectMapper(); */

					String encodedXmlTcs = encodeSamlResponse(xmlToTcs);

					/* String json = mapper.writeValueAsString(encodedXmlTcs); */

					logger.info("xmlToTcs---->" + encodedXmlTcs);

					/*
					 * Client clientWS = null;
					 * 
					 * ClientConfig clientConfig = null;
					 * 
					 * WebTarget webTarget = null;
					 * 
					 * Invocation.Builder invocationBuilder = null;
					 * 
					 * javax.ws.rs.core.Response response1 = null;
					 * 
					 * int responseCode;
					 * 
					 * clientConfig = new ClientConfig();
					 * 
					 * 
					 * 
					 * clientWS = ClientBuilder.newClient(clientConfig);
					 * 
					 * webTarget = clientWS.target(
					 * "https://qahf-g01.tcsion.com/LX/INDEXES/ConsumeSAMLToken" );
					 * 
					 * // .target(LMSURL+"/MPSTME-NM-M/createAssignmentFromJson/");
					 * 
					 * 
					 * 
					 * // set file upload values
					 * 
					 * invocationBuilder = webTarget.request();
					 * 
					 * 
					 * 
					 * response1 = invocationBuilder.post(Entity.entity(
					 * 
					 * encodedXmlTcs, MediaType.TEXT_PLAIN));
					 * 
					 * responseCode = response1.getStatus();
					 * 
					 * logger.info("responseCode---->"+responseCode);
					 */

					/* String html = postSamlResponse(resp, encodedXmlTcs); */

					m.addAttribute("encodedXml", encodedXmlTcs);
					m.addAttribute("URL", tcsURL);

				} catch (Exception e) {

					logger.error("Exception while filling and sending SAML Response ...", e);

				}

				logger.info("xmlToTcsOut---->" + xmlToTcs);

				/* return "redirect:/homepage"; */

			} else {

				setError(m, "Tcs Request didn't match!!");
			}

			return "samlResponse";

		} catch (Exception e) {

			logger.error("Exception", e);

			setError(r, "Error in connecting...");

			return "redirect:/homepage";

		}

	}

	public String buildResponseXMLString(User u, String entity, String destination) {
		logger.info("entity--->" + entity);
		try {
			ClassLoader cl = getClass().getClassLoader();
			File f = new File(cl.getResource("./static/resources/templates/sample_response.xml").getFile());
			InputStream assertionTemplateFile = new FileInputStream(f);

			// templateXmlString contains the raw SAML Response templates with
			// field handle to be replaced with appropriate parameters

			strResponseXML = new Scanner(assertionTemplateFile, "UTF-8").useDelimiter("\\A").next().trim();

			assertionTemplateFile.close();

			/*
			 * System.out.println("\n\nThe assertion template is : \n" + strResponseXML);
			 */

			// Id generation
			SecureRandomIdentifierGenerator generator = new

			SecureRandomIdentifierGenerator();

			res_Id = generator.generateIdentifier().trim();
			res_assertionId = generator.generateIdentifier().trim();

			// Other important identifying parameters
			res_issuer = "https://portal.svkm.ac.in";
			res_nameId = u.getUsername().trim();
			String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'+05:30'";
			res_issueInstant = new DateTime().toString(dateTimeFormat).trim();
			res_notbefore = new DateTime().toString(dateTimeFormat).trim();
			res_notonorafter = new DateTime().plusMinutes(5).toString(dateTimeFormat).trim();

			// Filling the parameters into the template...
			strResponseXML = strResponseXML.replaceAll("_ASSERTION_ID", res_assertionId);
			strResponseXML = strResponseXML.replaceAll("_REQUEST_ID", entity);
			strResponseXML = strResponseXML.replaceAll("_DESTINATION",
					"https://g21.tcsion.com/LX/INDEXES/ConsumeSAMLToken");
			strResponseXML = strResponseXML.replaceAll("_RESPONSE_ID", res_Id);
			strResponseXML = strResponseXML.replaceAll("_ISSUE_INSTANT", res_issueInstant);
			strResponseXML = strResponseXML.replaceAll("_FIRST_NAME", u.getFirstname().trim());
			strResponseXML = strResponseXML.replaceAll("_LAST_NAME", u.getLastname().trim());
			strResponseXML = strResponseXML.replaceAll("_EMAIL", u.getEmail().trim());
			strResponseXML = strResponseXML.replaceAll("_ISSUER", res_issuer);
			strResponseXML = strResponseXML.replaceAll("_NAMEID", res_nameId);
			strResponseXML = strResponseXML.replaceAll("_NOTBEFORE", res_notbefore);
			strResponseXML = strResponseXML.replaceAll("_NOTONORAFTER", res_notonorafter);
			/*
			 * strResponseXML = strResponseXML.replaceAll("_ACS_URL", acs); strResponseXML =
			 * strResponseXML.replaceAll("_DOMAIN", domain);
			 */
			/*
			 * System.out.println("\n\nThe complete SAML Response is : \n" +
			 * strResponseXML);
			 */

		} catch (Exception e) {
			logger.error("Exception while filling SAML Response ...", e);
		}
		return strResponseXML;
	}

	public Response generateSamlResponseObject(String strResponseXML) {
		try {

			InputStream xmlResponseAsStream = new ByteArrayInputStream(strResponseXML.getBytes());
			// Load initial configurations
			DefaultBootstrap.bootstrap();

			// Get parser pool manager
			BasicParserPool ppMgr = new BasicParserPool();
			ppMgr.setNamespaceAware(true);

			Document parsedDocumentObject = ppMgr.parse(xmlResponseAsStream);
			Element parsedElementObject = parsedDocumentObject.getDocumentElement();

			// Get apropriate unmarshaller
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(parsedElementObject);

			// Unmarshall using the document root element, an EntitiesDescriptor
			// in this case
			Response responseObject = (Response) unmarshaller.unmarshall(parsedElementObject);
			// responseObject.
			/* System.out.println("\nThe unmarshalled saml response is : "); */

			// System.out.println(XMLHelper.nodeToString(parsedElementObject));
			/* System.out.println(XMLHelper.nodeToString(parsedElementObject)); */

			xmlResponseAsStream.close();

			samlResponseObject = responseObject;

			/*
			 * System.out.println("\nThe SAML signature is : " +
			 * samlResponseObject.getSignature());
			 */

		} catch (Exception e) {
			logger.error("Exception while trying to parse samlResponseObject :", e);

		}
		return samlResponseObject;

	}

	public String canonicalizeSamlResponse(String strResponseXML) {

		try {

			// Initializing the Apache XML security library
			org.apache.xml.security.Init.init();

			// Canonicalizing the XML String.
			Canonicalizer canonicalizer = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

			byte canonicalizedResponseXML[] = canonicalizer.canonicalize(strResponseXML.getBytes("UTF-8"));

			strFinalResponse = new String(canonicalizedResponseXML);

			logger.info("The Response after canonicalization is : " + strResponseXML);

		} catch (Exception e) {
			logger.error("Exception while canonicalizing the SAML Response.", e);
		}
		return strFinalResponse;

	}

	public String addSign(Document document, PrivateKey key, X509Certificate certificate, String signAlgorithm,
			String digestAlgorithm) throws XMLSecurityException, XPathExpressionException {
		// Check arguments.
		if (document == null) {
			throw new IllegalArgumentException("Provided document was null");
		}

		if (document.getDocumentElement() == null) {
			throw new IllegalArgumentException("The Xml Document has no root element.");
		}

		if (key == null) {
			throw new IllegalArgumentException("Provided key was null");
		}

		if (certificate == null) {
			throw new IllegalArgumentException("Provided certificate was null");
		}

		if (signAlgorithm == null || signAlgorithm.isEmpty()) {
			signAlgorithm = Constants.RSA_SHA256;
		}
		if (digestAlgorithm == null || digestAlgorithm.isEmpty()) {
			digestAlgorithm = Constants.SHA256;
		}

		document.normalizeDocument();

		String c14nMethod = Constants.C14NEXC;

		// Signature object
		XMLSignature sig = new XMLSignature(document, null, signAlgorithm, c14nMethod);
		XMLSignature sig1 = new XMLSignature(document, null, signAlgorithm, c14nMethod);
		// Including the signature into the document before sign, because
		// this is an envelop signature
		Element root = document.getDocumentElement();

		document.setXmlStandalone(false);

		// If Issuer, locate Signature after Issuer, Otherwise as first child.
		NodeList issuerNodes = query(document, "//ns1:Issuer", null);
		logger.info("issuerNodes--->" + issuerNodes.item(0));
		logger.info("issuerNodes1--->" + issuerNodes.item(1));

		Element elemToSign = null;
		Element elemToSign1 = null;
		if (issuerNodes.getLength() > 0) {
			Node issuer = issuerNodes.item(0);
			Node issuer1 = issuerNodes.item(1);

			root.insertBefore(sig.getElement(), issuer.getNextSibling());
			logger.info("element11--->" + sig1.getElement());
			logger.info("elementNext--->" + issuer1.getNextSibling().getNextSibling());
			issuer1.getParentNode().replaceChild(sig1.getElement(), issuer1.getNextSibling().getNextSibling());
			elemToSign1 = (Element) issuer1.getParentNode();
			elemToSign = (Element) issuer.getParentNode();
			logger.info("elemToSign00--->" + elemToSign);
		} else {
			NodeList entitiesDescriptorNodes = query(document, "//ds:Signature", null);
			logger.info("entitiesDescriptorNodes--->" + entitiesDescriptorNodes.getLength());
			if (entitiesDescriptorNodes.getLength() > 0) {
				elemToSign = (Element) entitiesDescriptorNodes.item(0);
			} else {
				NodeList entityDescriptorNodes = query(document, "//md:EntityDescriptor", null);
				if (entityDescriptorNodes.getLength() > 0) {
					elemToSign = (Element) entityDescriptorNodes.item(0);
				} else {
					elemToSign = root;
				}
			}
			root.insertBefore(sig.getElement(), elemToSign.getFirstChild());
		}

		String id = elemToSign.getAttribute("ID");

		String reference = id;
		if (!id.isEmpty()) {
			elemToSign.setIdAttributeNS(null, "ID", true);
			reference = "#" + id;
		}
		String id1 = elemToSign1.getAttribute("ID");
		String reference1 = id1;
		if (!id.isEmpty()) {
			elemToSign1.setIdAttributeNS(null, "ID", true);
			reference1 = "#" + id1;
		}
		// Create the transform for the document
		Transforms transforms = new Transforms(document);
		transforms.addTransform(Constants.ENVSIG);
		transforms.addTransform(c14nMethod);
		sig.addDocument(reference, transforms, digestAlgorithm);
		sig1.addDocument(reference1, transforms, digestAlgorithm);
		// Add the certification info
		sig.addKeyInfo(certificate);
		sig1.addKeyInfo(certificate);
		// Sign the document
		sig.sign(key);
		sig1.sign(key);
		return convertDocumentToString(document, true);
	}

	public String convertDocumentToString(Document doc, Boolean c14n) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (c14n) {
			XMLUtils.outputDOMc14nWithComments(doc, baos);
		} else {
			XMLUtils.outputDOM(doc, baos);
		}

		return toStringUtf8(baos.toByteArray());
	}

	private String toStringUtf8(byte[] bytes) {
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public Document convertStringToDocument(String xmlStr)
			throws ParserConfigurationException, SAXException, IOException {
		return parseXML(new InputSource(new StringReader(xmlStr)));
	}

	public Document parseXML(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docfactory = DocumentBuilderFactory.newInstance();
		docfactory.setNamespaceAware(true);

		// do not expand entity reference nodes
		docfactory.setExpandEntityReferences(false);

		/*
		 * docfactory.setAttribute(
		 * "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
		 * XMLConstants.W3C_XML_SCHEMA_NS_URI);
		 */

		// Add various options explicitly to prevent XXE attacks.
		// (adding try/catch around every setAttribute just in case a specific
		// parser does not support it.
		try {
			// do not include external general entities
			docfactory.setAttribute("http://xml.org/sax/features/external-general-entities", Boolean.FALSE);
		} catch (Throwable e) {
		}
		try {
			// do not include external parameter entities or the external DTD
			// subset
			docfactory.setAttribute("http://xml.org/sax/features/external-parameter-entities", Boolean.FALSE);
		} catch (Throwable e) {
		}
		try {
			docfactory.setAttribute("http://apache.org/xml/features/disallow-doctype-decl", Boolean.TRUE);
		} catch (Throwable e) {
		}
		try {
			docfactory.setAttribute("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE);
		} catch (Throwable e) {
		}
		try {
			// ignore the external DTD completely
			docfactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
		} catch (Throwable e) {
		}
		try {
			// build the grammar but do not use the default attributes and
			// attribute types information it contains
			docfactory.setAttribute("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", Boolean.FALSE);
		} catch (Throwable e) {
		}
		try {
			// docfactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,
			// true);
		} catch (Throwable e) {
		}

		DocumentBuilder builder = docfactory.newDocumentBuilder();
		Document doc = builder.parse(inputSource);

		// Loop through the doc and tag every element with an ID attribute
		// as an XML ID node.
		XPath xpath = getXPathFactory().newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile("//*[@ID]");

			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element elem = (Element) nodeList.item(i);
				Attr attr = (Attr) elem.getAttributes().getNamedItem("ID");
				elem.setIdAttributeNode(attr, true);
			}
		} catch (XPathExpressionException e) {
			return null;
		}

		return doc;
	}

	private XPathFactory getXPathFactory() {
		try {

			return XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI,
					"com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl",
					java.lang.ClassLoader.getSystemClassLoader());
		} catch (XPathFactoryConfigurationException e) {
			logger.error("Error generating XPathFactory instance: ", e);
		}
		return XPathFactory.newInstance();
	}

	public NodeList query(Document dom, String query, Node context) throws XPathExpressionException {
		NodeList nodeList;
		XPath xpath = getXPathFactory().newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {

			@Override
			public String getNamespaceURI(String prefix) {
				logger.info("prefix--->" + prefix);
				String result = null;
				if (prefix.equals("samlp") || prefix.equals("samlp2")) {
					result = Constants.NS_SAMLP;
				} else if (prefix.equals("ns1") || prefix.equals("ns2")) {
					result = Constants.NS_SAML;
				} else if (prefix.equals("ds")) {
					result = Constants.NS_DS;
				} else if (prefix.equals("xenc")) {
					result = Constants.NS_XENC;
				} else if (prefix.equals("md")) {
					result = Constants.NS_MD;
				}
				return result;
			}

			@Override
			public String getPrefix(String namespaceURI) {
				return null;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Iterator getPrefixes(String namespaceURI) {
				return null;
			}
		});

		if (context == null) {
			nodeList = (NodeList) xpath.evaluate(query, dom, XPathConstants.NODESET);
		} else {
			nodeList = (NodeList) xpath.evaluate(query, context, XPathConstants.NODESET);
		}
		return nodeList;
	}

	public String encodeSamlResponse(String strFinalResponse) {
		try {

			logger.info("Attempting to b64 encode the response ...");

			// strFinalResponse =
			// strFinalResponse.replaceAll("<?xml version=\"1.0\", encoding=\"UTF-8\"?>",
			// "");

			strFinalResponse = new String(Base64.encodeBase64(strFinalResponse.getBytes("UTF-8")), "UTF-8");

			// strFinalResponse.replaceAll("(\\r|\\n|\\r\\n)+", "")

			strFinalResponse = strFinalResponse.trim();

			StringBuilder formattedResponse = new StringBuilder();

			// Splitting the final response text to 60 char lines
			for (int i = 0; i < strFinalResponse.length(); i++) {
				if (((i % 60) == 0) && i != 0)
					formattedResponse.append("\n");

				formattedResponse.append(strFinalResponse.charAt(i));
			}

			strFinalResponse = formattedResponse.toString();

			logger.info("The base64 encoded saml response is : " + strFinalResponse);

		} catch (Exception e) {
			logger.error("Exception while trying to access session data .The error is :", e);

		}
		return strFinalResponse;

	}

	/*
	 * @RequestMapping(value = "/redirect", method = RequestMethod.GET) public
	 * ModelAndView method() { logger.info("redirectURL--->" + tcsURL); return new
	 * ModelAndView("redirect:" + tcsURL); }
	 */

	/*
	 * public void postSamlResponse(HttpServletResponse responseObject, String
	 * strFinalResponse) throws IOException {
	 * 
	 * String relState = relayStateIsb64 ? relayStateb64 : relayState; String html =
	 * "<HTML>" + "<BODY onload='document.forms[\"saml-form\"].submit()'>" +
	 * "<FORM name='saml-form' action='" + tcsURL + "' method='POST'>"
	 * 
	 * + "<INPUT type='hidden' name='SAMLResponse' value='" + strFinalResponse +
	 * "' />" + "<INPUT type='hidden' value='submit' />" +"</BODY></HTML>";
	 * 
	 * PrintWriter out = responseObject.getWriter(); out.write(html);
	 * logger.info("out--->"+out); out.close();
	 * 
	 * }
	 */
	// ------------------------------MICL----------------------------------//

	@RequestMapping(value = { "/samlRequestMICL" }, method = {

			RequestMethod.GET, RequestMethod.POST })
	public String samlRequestMICL(@RequestParam(name = "SAMLRequest") String SAMLRequest, HttpServletResponse resp,
			Principal p, RedirectAttributes r, Model m) {

		logger.info("SAML Request MICL------>" + SAMLRequest);

		if (SAMLRequest.isEmpty()) {

			setError(r, "MICL Request not found!!");

			return "redirect:/homepage";

		}

		// Base64 decoder

		byte[] decoded = Base64.decodeBase64(SAMLRequest);

		logger.info("SAML Request decoded MICL------>" + decoded);

		String xmlToMICL = "";

		// Inflater

		try {

			Inflater decompresser = new Inflater(true);

			decompresser.setInput(decoded);

			byte[] result = new byte[1024];

			String inflated = "";

			long limit = 0;

			while (!decompresser.finished() && limit < 150) {

				int resultLength = decompresser.inflate(result);

				limit += 1;

				inflated += new String(result, 0, resultLength, "UTF-8");

			}

			decompresser.end();

			logger.info("SAML Request inflated MICL------>" + inflated);

			String msg = inflated;

			DocumentBuilder newDocumentBuilder = DocumentBuilderFactory

					.newInstance().newDocumentBuilder();

			Document parse = newDocumentBuilder.parse(new ByteArrayInputStream(

					msg.getBytes()));

			logger.info("Destination MICL-->"

					+ parse.getChildNodes().item(0).getAttributes()

							.getNamedItem("Destination").getTextContent());

			logger.info("EntityId MICL-->"

					+ parse.getFirstChild().getChildNodes().item(0)

							.getTextContent());

			String destination = parse.getChildNodes().item(0).getAttributes()

					.getNamedItem("Destination").getTextContent();

			String entity = parse.getFirstChild().getChildNodes().item(0)

					.getTextContent();

			User u = new User();

			if (entity.equals(miclEntity)) {

				if (p != null) {

					u = userService.findByUserName(p.getName());

					logger.info("firstname MICL-->" + u.getFirstname());

					logger.info("lastname MICL-->" + u.getLastname());

					logger.info("emailId MICL-->" + u.getEmail());

					logger.info("Username MICL-->" + u.getUsername());

				} else {

					logger.info("logoutcalled MICL");

					setError(r, "Session Time out!!");

					return "redirect:/loggedout";

				}

				try {

					String xmlRes = buildResponseXMLStringMICL(u, entity, destination);

					Response response = generateSamlResponseObject(xmlRes);

					String strFinalResponse = canonicalizeSamlResponse(xmlRes);

					/* signSamlResponseObject2(xmlRes,strFinalResponse,res_Id); */

					ClassLoader cl = getClass().getClassLoader();

					File f = new File(cl.getResource("./static/resources/templates/keystore.jks").getFile());

					InputStream fis = new FileInputStream(f);

					KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

					ks.load(fis, "javacaps".toCharArray());

					fis.close();

					// Get Private Key Entry From keystore

					KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks

							.getEntry("keystore", new KeyStore.PasswordProtection(

									"javacaps".toCharArray()));

					PrivateKey privKey = pkEntry.getPrivateKey();

					PublicKey pubKey = ks.getCertificate("keystore").getPublicKey();

					X509Certificate cert = (X509Certificate) ks

							.getCertificate("keystore");

					Document doc = convertStringToDocument(strFinalResponse);

					String signAlgorithm = "";

					String digestAlgorithm = "";

					xmlToMICL = addSign(doc, privKey, cert, signAlgorithm, digestAlgorithm);

					/* ObjectMapper mapper = new ObjectMapper(); */

					String encodedXmlMICL = encodeSamlResponse(xmlToMICL);

					/* String json = mapper.writeValueAsString(encodedXmlTcs); */

					logger.info("xmlToMICL---->" + encodedXmlMICL);

					/* String html = postSamlResponse(resp, encodedXmlTcs); */

					m.addAttribute("encodedXml", encodedXmlMICL);
					m.addAttribute("URL", miclURL);

				} catch (Exception e) {

					logger.error("Exception while filling and sending SAML Response ...", e);

				}

				logger.info("xmlToMICLOut---->" + xmlToMICL);

				/* return "redirect:/homepage"; */

			} else {

				setError(m, "MICL Request didn't match!!");
			}

			return "samlResponse";

		} catch (Exception e) {

			logger.error("Exception", e);

			setError(r, "Error in connecting...");

			return "redirect:/homepage";

		}

	}

	public String buildResponseXMLStringMICL(User u, String entity, String destination) {
		logger.info("entity MICL--->" + entity);
		try {
			ClassLoader cl = getClass().getClassLoader();
			File f = new File(cl.getResource("./static/resources/templates/sample_response.xml").getFile());
			InputStream assertionTemplateFile = new FileInputStream(f);

			// templateXmlString contains the raw SAML Response templates with
			// field handle to be replaced with appropriate parameters

			strResponseXML = new Scanner(assertionTemplateFile, "UTF-8").useDelimiter("\\A").next().trim();

			assertionTemplateFile.close();

			/*
			 * System.out.println("\n\nThe assertion template is : \n" + strResponseXML);
			 */

			// Id generation
			SecureRandomIdentifierGenerator generator = new

			SecureRandomIdentifierGenerator();

			res_Id = generator.generateIdentifier().trim();
			res_assertionId = generator.generateIdentifier().trim();

			// Other important identifying parameters
			res_issuer = "https://portal.svkm.ac.in";
			res_nameId = u.getUsername().trim();
			String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss'+05:30'";
			res_issueInstant = new DateTime().toString(dateTimeFormat).trim();
			res_notbefore = new DateTime().toString(dateTimeFormat).trim();
			res_notonorafter = new DateTime().plusMinutes(5).toString(dateTimeFormat).trim();

			// Filling the parameters into the template...
			strResponseXML = strResponseXML.replaceAll("_ASSERTION_ID", res_assertionId);
			strResponseXML = strResponseXML.replaceAll("_REQUEST_ID", entity);
			strResponseXML = strResponseXML.replaceAll("_DESTINATION", miclURL);
			strResponseXML = strResponseXML.replaceAll("_RESPONSE_ID", res_Id);
			strResponseXML = strResponseXML.replaceAll("_ISSUE_INSTANT", res_issueInstant);
			strResponseXML = strResponseXML.replaceAll("_FIRST_NAME", u.getFirstname().trim());
			strResponseXML = strResponseXML.replaceAll("_LAST_NAME", u.getLastname().trim());
			strResponseXML = strResponseXML.replaceAll("_EMAIL", u.getEmail().trim());
			strResponseXML = strResponseXML.replaceAll("_ISSUER", res_issuer);
			strResponseXML = strResponseXML.replaceAll("_NAMEID", res_nameId);
			strResponseXML = strResponseXML.replaceAll("_NOTBEFORE", res_notbefore);
			strResponseXML = strResponseXML.replaceAll("_NOTONORAFTER", res_notonorafter);
			/*
			 * strResponseXML = strResponseXML.replaceAll("_ACS_URL", acs); strResponseXML =
			 * strResponseXML.replaceAll("_DOMAIN", domain);
			 */
			/*
			 * System.out.println("\n\nThe complete SAML Response is : \n" +
			 * strResponseXML);
			 */

		} catch (Exception e) {
			logger.error("Exception while filling SAML Response MICL...", e);
		}
		return strResponseXML;
	}

	@RequestMapping(value = "/updateProfileForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateProfileForApp(@RequestBody User user, HttpServletResponse hresp,
			@RequestHeader Map<String, String> headers) {
		user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String successMsg = "", errorMsg = "";
		try {

			String username = user.getUsername();
			String email = user.getEmail();
			String mobile = user.getMobile();
			userService.changeEmailMobileByApp(email, mobile, username);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("username", username);
			userMap.put("email", email);
			userMap.put("mobile", mobile);
			String userJson = new Gson().toJson(userMap);
			try {

				WebTarget webTarget = client
						.target(URIUtil.encodeQuery(userMgmtUrlApp + "changeEmailMobileForApp?userJson=" + userJson));
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);
				String json = "";
				logger.info("updateProfileForApp resp==> " + resp);
				if (resp.contains("Success")) {
					json = "{\"Status\":\"Success\"}";
				} else {
					json = "{\"Status\":\"Fail\"}";
				}

				json = encryptResponseBody(json);
				return json;

			} catch (Exception ex) {
				logger.error("Exception ", ex);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			errorMsg = "Error in updating Profile";
			String json = "{\"Status\":\"Fail\"}";

			json = encryptResponseBody(json);
			return json;

		}
	}

	@RequestMapping(value = "/changePasswordForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String changePasswordForApp(@RequestBody User user, HttpServletResponse hresp,
			@RequestHeader Map<String, String> headers) {
		user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String successMsg = "", errorMsg = "";
		try {

			String username = user.getUsername();
			String password = user.getNewPasswordMob();
			String oldPassword = user.getOldPasswordMob();

			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("username", username);
			userMap.put("password", password);
			userMap.put("oldPassword", oldPassword);

			logger.info("oldPasswordLMS => " + oldPassword);
			String userJson = new Gson().toJson(userMap);
			try {

				WebTarget webTarget = client
						.target(URIUtil.encodeQuery(userMgmtUrlApp + "changePasswordForApp?userJson=" + userJson));
				logger.info("urlToHit ==> " + userMgmtUrlApp + "changePasswordForApp?userJson=" + userJson);
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);
				logger.info("resp ==> " + resp);
				String json = "";
				if (resp.contains("Success")) {
					json = "{\"Status\":\"Success\"}";
				} else if (resp.contains("Invalid Old")) {
					json = "{\"Status\":\"Invalid Old Password\"}";
				} else {
					json = "{\"Status\":\"Fail\"}";
				}

				json = encryptResponseBody(json);
				return json;

			} catch (Exception ex) {
				logger.error("Exception ", ex);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			errorMsg = "Error in updating Profile";
			String json = "{\"Status\":\"Fail\"}";

			json = encryptResponseBody(json);
			return json;

		}
	}
	
	@RequestMapping(value = "/updateProfileForAppUatDev", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateProfileForAppUatDev(@RequestBody User user, HttpServletResponse hresp,
			@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String successMsg = "", errorMsg = "";
		try {

			String username = user.getUsername();
			String email = user.getEmail();
			String mobile = user.getMobile();
			userService.changeEmailMobileByApp(email, mobile, username);
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("username", username);
			userMap.put("email", email);
			userMap.put("mobile", mobile);
			String userJson = new Gson().toJson(userMap);
			try {

				WebTarget webTarget = client
						.target(URIUtil.encodeQuery(userMgmtUrlApp + "changeEmailMobileForApp?userJson=" + userJson));
				logger.info("urlToHit ==> " + userMgmtUrlApp + "changeEmailMobileForApp?userJson=" + userJson);
				Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);
				if (resp.contains("Success")) {
					return "{\"Status\":\"Success\", \"successMsg\":\"" + successMsg + "\"}";
				} else {
					return "{\"Status\":\"Success\", \"successMsg\":\"" + successMsg + "\"}";
				}

			} catch (Exception ex) {
				logger.error("Exception ", ex);
				return "{\"Status\":\"Fail\", \"errorMsg\":\"" + errorMsg + "\"}";
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			errorMsg = "Error in updating Profile";
			return "{\"Status\":\"Fail\", \"errorMsg\":\"" + errorMsg + "\"}";

		}
	}

	@RequestMapping(value = "/showTrainingSession", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showTrainingSession(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String username = user.getUsername();

		List<TrainingProgram> trainingProgramList = new ArrayList<>();

		trainingProgramList = trainingProgramService.getOnGoingTraining();
		logger.info("trainingProgramList---->" + trainingProgramList);
		String json = "";
		TrainingProgram forYou = new TrainingProgram();
		if (!trainingProgramList.isEmpty()) {
			for (TrainingProgram tpl : trainingProgramList) {

				TrainingProgram check = new TrainingProgram();
				check = trainingProgramService.getMarkedTrainingAttendance(username, tpl.getId());
				if (null == check) {
					if (tpl.getUserType().equals("ROLE_FACULTY") || tpl.getUserType().equals("ROLE_STUDENT")) {
						logger.info("getProgramId---->" + tpl.getProgramId());
						if (tpl.getProgramId() == null) {
							if (tpl.getCampusId() != null) {
								List<String> pc = new ArrayList<>();
								pc = programCampusService.getProgramsByCampusId(tpl.getCampusId());
								List<String> userList = new ArrayList<>();
								userList = userService.getUserByProgramsAndRole(pc, tpl.getUserType());

								logger.info("user count---->" + userList.size());
								for (String s : userList) {
									if (s.contains(username)) {
										logger.info("Present---->" + username);
										forYou = tpl;
									}
								}
							}
						} else {
							List<String> userList = new ArrayList<>();
							userList = userService.getUserByProgramAndRole(tpl.getProgramId(), tpl.getUserType());

							logger.info("user count---->" + userList.size());
							for (String s : userList) {
								if (s.contains(username)) {
									logger.info("Present---->" + username);
									forYou = tpl;
								}
							}
						}
					}
				} else {
					json = "{\"Status\":\"Attendance has already been marked...\"}";

					json = encryptResponseBody(json);
					return json;
				}
			}
		} else {
			json = "{\"Status\":\"Currently, No training session is going on for you...\"}";

			json = encryptResponseBody(json);
			return json;
		}
		String _json = new Gson().toJson(forYou);
		json = encryptResponseBody(_json);
		return json;
	}

	@RequestMapping(value = "/insertTrainingAttendance", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String insertTrainingAttendance(@RequestBody TrainingProgram trainingProgram,
			HttpServletResponse resp, @RequestHeader Map<String, String> headers) {
		trainingProgram = (TrainingProgram) decryptRequestBody(trainingProgram.getEncrypted_key(), "TrainingProgram");
		boolean auth = isUserAuthorized(headers.get("token"), trainingProgram.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String json = "";
		try {
			trainingProgramService.insertTrainingAttendance(trainingProgram.getUsername(),
					trainingProgram.getTrainingProgramId());
			json = "{\"Status\":\"Success\"}";
		} catch (Exception e) {
			// e.printStackTrace();
			json = "{\"Status\":\"Failed\"}";
		}

		json = encryptResponseBody(json);
		return json;
	}

	@RequestMapping(value = "/showInternalTotalMarksForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showInternalTotalMarksForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		// Token userdetails1 = (Token) principal;
		String username = user.getUsername();

		List<IcaComponentMarks> componentMarksForStuent = icaComponentMarksService.getIcaComponentMarksByUser(username);
		logger.info("componentMarksForStuent------>" + componentMarksForStuent);
		Map<String, String> componentsMap = new HashMap<>();
		for (IcaComponentMarks icm : componentMarksForStuent) {
			if (!componentsMap.containsKey(icm.getComponentId())) {
				componentsMap.put(icm.getComponentId(), icm.getComponentName());
			}
		}
		logger.info("componentsMap------>" + componentsMap);
		List<IcaTotalMarks> icaTotalMarksForStudent = icaTotalMarksService.getIcaTotalMarksByUser(username);
		List<IcaTotalMarks> icaTotalMarksForStudentForNonEvent = icaTotalMarksService
				.getIcaTotalMarksByUserForNonEvent(username);
		icaTotalMarksForStudent.addAll(icaTotalMarksForStudentForNonEvent);
		List<String> iceIds = icaTotalMarksForStudent.stream().map(map -> map.getIcaId()).collect(Collectors.toList());

		Map<String, Map<String, String>> mapOfComponentsMarksByIcaId = new HashMap<>();
		Map<String, String> dateSpanMap = new HashMap<>();
		for (String i : iceIds) {
			Map<String, String> componentsMapIca = componentMarksForStuent.stream().filter(o -> o.getIcaId().equals(i))
					.collect(Collectors.toMap(IcaComponentMarks::getComponentId, IcaComponentMarks::getMarks));
			mapOfComponentsMarksByIcaId.put(i, componentsMapIca);

		}
		logger.info("mapComponent------>" + mapOfComponentsMarksByIcaId);
		for (IcaTotalMarks itm : icaTotalMarksForStudent) {
			String publishedDate = itm.getPublishedDate();
			String currentDate = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());
			itm.setDueDate(Utils.addDaysToDate(publishedDate, 3));

			String raiseButton = itm.getDueDate().compareTo(currentDate) >= 0 ? "showButton" : "disableButton";
			dateSpanMap.put(itm.getIcaId(), raiseButton);
		}

		logger.info("totalMarks------>" + icaTotalMarksForStudent);

		logger.info("dateSpanMap------>" + userService.findByUserName(username));
		String json = new Gson().toJson(icaTotalMarksForStudent);

		json = encryptResponseBody(json);
		return json;
	}

	@RequestMapping(value = "/showInternalComponentMarksForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showInternalComponentMarksForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String username = user.getUsername();
		String icaId = user.getIcaId();
		List<IcaComponentMarks> componentMarksForStuent = icaComponentMarksService
				.getIcaComponentMarksByUserById(username, icaId);

		logger.info("componentMarksForStuent------>" + componentMarksForStuent);
		String json = new Gson().toJson(componentMarksForStuent);

		json = encryptResponseBody(json);
		return json;
	}

	@RequestMapping(value = "/showExamTimetableForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showExamTimetableForApp(@RequestBody User user, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		user = (User) decryptRequestBody(user.getEncrypted_key(), "User");
		boolean auth = isUserAuthorized(headers.get("token"), user.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String username = user.getUsername();
		List<Announcement> examTimeTableList = announcementService.getExamTimetableForApp(username);
		String json = new Gson().toJson(examTimeTableList);

		json = encryptResponseBody(json);
		return json;

	}

	/*
	 * @RequestMapping(value = { "/getTimetableByCourseForAndroidApp" }, method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * getTimetableByCourseForAndroidApp(
	 * 
	 * @RequestBody Course course, HttpServletResponse resp) { try {
	 * 
	 * // logger.info("Course----->"+course);
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "timetable_metadata");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable); String cid = ""; long eventId = 0;
	 * long pgrmId = 0; if (null != course.getId()) { cid =
	 * course.getId().toString(); eventId = Long.parseLong(cid.substring(0, 8));
	 * pgrmId = Long.parseLong(cid.substring(8)); } String username =
	 * course.getUsername(); if (username.contains("_")) { username =
	 * username.substring(0, username.indexOf("_")); } String curDate = "";
	 * 
	 * if (null == course.getClassDate()) { Date dt = new Date(); SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("dd-MM-yyyy"); curDate = "%" +
	 * dateFormat.format(dt) + "%"; } else { curDate = "%" + course.getClassDate() +
	 * "%"; } logger.info("current Date ------->" + curDate);
	 * logger.info("course Id ------->" + cid); logger.info("event Id ------->" +
	 * eventId); logger.info("program Id ------->" + pgrmId);
	 * logger.info("username ------->" + username); List<Timetable> tt = new
	 * ArrayList<>(); if (null != course.getId()) { tt =
	 * timetableDAO.getTimetableByCourse(pgrmId, eventId, username, curDate); } else
	 * { tt = timetableDAO.getTimetableByCourseForApp(username, curDate); }
	 * 
	 * long pgrmId1; String cid1; List<Timetable> ttFinal = new ArrayList<>();
	 * logger.info("tt size" + tt.size());
	 * 
	 * Map<String, String> courseIdEndTimeMap = new HashMap<String, String>();
	 * 
	 * for (Timetable tmtl : tt) { logger.info("EndTime========>>>" +
	 * tmtl.getEnd_time()); String[] programidsStrings =
	 * tmtl.getProgramId().split(" , "); String programNameToSend = "",
	 * courseIdToSend = "", courseNameToSend = "";
	 * 
	 * tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
	 * tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace( ".", ":"));
	 * tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
	 * tmtl.setMaxEndTimeForCourse(tmtl.getEnd_time());
	 * 
	 * if (tmtl.getProgramId().contains(" , ")) {
	 * 
	 * Program p1 = new Program();
	 * 
	 * for (int i = 0; i < programidsStrings.length; i++) {
	 * 
	 * cid1 = "" + tmtl.getEventId() + programidsStrings[i]; Course c1 =
	 * courseService .findByID(Long.parseLong(cid1));
	 * 
	 * pgrmId1 = Long.parseLong(programidsStrings[i]); p1 =
	 * programService.findByID(pgrmId1); logger.info("c1======> "+ c1);
	 * logger.info("p1======> "+ p1); if (null != c1 && null != p1) {
	 * courseNameToSend = c1.getCourseName(); if (i < programidsStrings.length - 1)
	 * { programNameToSend = programNameToSend + p1.getProgramName() + " , ";
	 * courseIdToSend = courseIdToSend + cid1 + " , "; } else { programNameToSend =
	 * programNameToSend + p1.getProgramName(); courseIdToSend = courseIdToSend +
	 * cid1; } tmtl.setCourseId(courseIdToSend); //
	 * tmtl.setProgramId(tmtl.getProgramId()); tmtl.setCourseName(courseNameToSend);
	 * tmtl.setProgramName(programNameToSend);
	 * 
	 * 
	 * }
	 * 
	 * } if (null != course.getId()) { if (cid.equals(tmtl.getCourseId())) {
	 * ttFinal.add(tmtl); courseIdEndTimeMap.put(courseIdToSend,
	 * tmtl.getEnd_time()); } } else { if(null != tmtl.getCourseId()){
	 * ttFinal.add(tmtl); courseIdEndTimeMap.put(courseIdToSend,
	 * tmtl.getEnd_time()); } } courseIdEndTimeMap.put(courseIdToSend,
	 * tmtl.getEnd_time()); ttFinal.add(tmtl);
	 * 
	 * 
	 * } else { cid1 = "" + tmtl.getEventId() + tmtl.getProgramId(); Course c1 =
	 * courseService.findByID(Long.parseLong(cid1)); pgrmId1 =
	 * Long.parseLong(tmtl.getProgramId()); Program p1 =
	 * programService.findByID(pgrmId1); logger.info("c1======> "+ c1);
	 * logger.info("p1======> "+ p1); logger.info("MaxEndTime======> " +
	 * tmtl.getMaxEndTimeForCourse()); tmtl.setCourseId(cid1); if (null != c1 &&
	 * null != p1) {
	 * 
	 * tmtl.setCourseName(c1.getCourseName());
	 * tmtl.setProgramName(p1.getProgramName());
	 * courseIdEndTimeMap.put(tmtl.getCourseId(), tmtl.getEnd_time());
	 * //ttFinal.add(tmtl); if (null != course.getId()) { if
	 * (cid.equals(tmtl.getCourseId())) { ttFinal.add(tmtl);
	 * courseIdEndTimeMap.put(courseIdToSend, tmtl.getEnd_time()); } } else {
	 * ttFinal.add(tmtl); courseIdEndTimeMap.put(courseIdToSend,
	 * tmtl.getEnd_time()); } }
	 * 
	 * }
	 * 
	 * 
	 * logger.info("tmtl list " + tmtl); }
	 * 
	 * 
	 * for (Timetable tmtl : tt) { logger.info("ProgramId" + tmtl.getProgramId());
	 * if (tmtl.getProgramId().contains(" , ")) {
	 * 
	 * String[] programidsStrings = tmtl.getProgramId().split(" , "); String
	 * programIdToSend ="", programNameToSend="", courseIdToSend=""; Timetable
	 * tmtlSub = new Timetable(); Program p1 = new Program();
	 * 
	 * for (int i = 0; i < programidsStrings.length; i++) { cid1 = "" +
	 * tmtl.getEventId() + programidsStrings[i]; Course c1 =
	 * courseService.findByID(Long.parseLong(cid1)); pgrmId1 =
	 * Long.parseLong(programidsStrings[i]); p1 = programService.findByID(pgrmId1);
	 * 
	 * if (null != c1 && null != p1) { // Timetable tmtlSub = new Timetable();
	 * 
	 * tmtlSub.setClass_date(tmtl.getClass_date().split(" ")[0]); tmtlSub
	 * .setStart_time(tmtl.getStart_time().split(" ")[1].replace(".", ":"));
	 * tmtlSub.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
	 * tmtlSub.setEventId(tmtl.getEventId());
	 * tmtlSub.setFacultyId(tmtl.getFacultyId()); tmtlSub.setFlag(tmtl.getFlag());
	 * 
	 * // logger.info("inside for------>" + // programidsStrings[i]); cid1 = "" +
	 * tmtl.getEventId()+ programidsStrings[i];
	 * 
	 * // logger.info("inside for------>" + cid1); // Course c1 = //
	 * courseService.findByID(Long.parseLong(cid1)); // tmtlSub.setCourseId(cid1);
	 * tmtlSub.setCourseName(c1.getCourseName());
	 * 
	 * 
	 * if(i < programidsStrings.length - 1) { programIdToSend = programIdToSend +
	 * programidsStrings[i]+" , "; programNameToSend = programNameToSend +
	 * p1.getProgramName()+" , "; courseIdToSend = courseIdToSend + cid1+" , "; }
	 * else { programIdToSend = programIdToSend + programidsStrings[i];
	 * programNameToSend = programNameToSend + p1.getProgramName(); courseIdToSend =
	 * courseIdToSend + cid1; } } }
	 * 
	 * //tmtlSub.setProgramId(programidsStrings[i]);
	 * tmtlSub.setCourseId(courseIdToSend); tmtlSub.setProgramId(programIdToSend);
	 * tmtlSub.setProgramName(programNameToSend);
	 * tmtlSub.setMaxEndTimeForCourse(tmtl.getEnd_time());
	 * logger.info("MaxEndTime======> "+tmtl.getEnd_time());
	 * 
	 * 
	 * logger.info(programNameToSend);
	 * 
	 * 
	 * if (null != course.getId()) { logger.info("courseId------->" +
	 * course.getId()); logger.info("tmtlSub courseId------->"+
	 * tmtlSub.getCourseId()); if (cid.equals(tmtlSub.getCourseId())) {
	 * ttFinal.add(tmtlSub); } } else { ttFinal.add(tmtlSub); }
	 * 
	 * logger.info("ttFinalSize-------> " + String.valueOf(ttFinal.size()));
	 * 
	 * logger.info("programIdToSend------->" + programIdToSend);
	 * logger.info("programNameToSend------->"+ programNameToSend);
	 * 
	 * logger.info("getEnd_time------->" + tmtlSub.getEnd_time());
	 * courseIdEndTimeMap.put(courseIdToSend,tmtlSub.getEnd_time()); } else { cid1 =
	 * "" + tmtl.getEventId() + tmtl.getProgramId(); Course c1 =
	 * courseService.findByID(Long.parseLong(cid1)); pgrmId1 =
	 * Long.parseLong(tmtl.getProgramId()); Program p1 =
	 * programService.findByID(pgrmId1);
	 * 
	 * if (null != c1 && null != p1) {
	 * tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]); tmtl.setStart_time
	 * (tmtl.getStart_time().split(" ")[1].replace(".", ":"));
	 * tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
	 * tmtl.setMaxEndTimeForCourse(tmtl.getEnd_time());
	 * logger.info("MaxEndTime======> "+tmtl.getMaxEndTimeForCourse());
	 * tmtl.setCourseId(cid1); tmtl.setCourseName(c1.getCourseName());
	 * tmtl.setProgramName(p1.getProgramName()); ttFinal.add(tmtl); }
	 * 
	 * logger.info("getEnd_time------->" + tmtl.getEnd_time());
	 * courseIdEndTimeMap.put(tmtl.getCourseId(),tmtl.getEnd_time()); }
	 * 
	 * }
	 * 
	 * 
	 * for (Object name : courseIdEndTimeMap.keySet()) { String key =
	 * name.toString(); String value = courseIdEndTimeMap.get(name);
	 * logger.info("Map:---->"+key + " " + value); } logger.info("MapListSize " +
	 * String.valueOf(courseIdEndTimeMap.size()));
	 * 
	 * 
	 * 
	 * // //////////////////////////////////////////////////////////////////
	 * 
	 * for (String key : courseIdEndTimeMap.keySet()) {
	 * 
	 * // List<Timetable> courseTimetable = new ArrayList<>(); List<Timetable>
	 * courseTimetable = new ArrayList<>(); int i = 0; while (i < ttFinal.size()) {
	 * logger.info("KEY---->" + key + "\n"); logger.info("ttFinal---->" +
	 * ttFinal.get(i).getCourseId() + "\n"); if
	 * (key.equals(ttFinal.get(i).getCourseId())) { //
	 * logger.info("key--------->"+key); //
	 * logger.info("ttFinal.get(i).getCourseId()--------->"
	 * +ttFinal.get(i).getCourseId()); courseTimetable.add(ttFinal.get(i)); } i++; }
	 * i = 0; for (Timetable ct : courseTimetable) { if (i < courseTimetable.size()
	 * - 1) { String lecEndTime = ct.getClass_date() + " " +
	 * courseTimetable.get(i).getEnd_time(); String nxtLecStartTime =
	 * ct.getClass_date() + " " + courseTimetable.get(i + 1).getStart_time();
	 * logger.info("lecEndTime >" + lecEndTime + " nextLecStartTime >" +
	 * nxtLecStartTime); long diff = timeDifference(lecEndTime, nxtLecStartTime); if
	 * (diff < 60000) {
	 * logger.info("MaxEnd-------------->"+courseIdEndTimeMap.get(key));
	 * courseTimetable .get(i).setMaxEndTimeForCourse(courseIdEndTimeMap.get(key));
	 * } else { for (int j = i; j > 0; j--) { String lecStartTime =
	 * ct.getClass_date() + " " + courseTimetable.get(j) .getStart_time(); String
	 * prevLecEndTime = ct.getClass_date() + " " + courseTimetable.get(j - 1)
	 * .getEnd_time(); logger.info("lecStartTime >" + lecStartTime +
	 * " prevLecEndTime >" + prevLecEndTime); long diff1 =
	 * timeDifference(prevLecEndTime, lecStartTime); logger.info("diff1--------->" +
	 * diff1); if (diff1 < 60000) { courseTimetable.get(j -
	 * 1).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time()); }
	 * 
	 * }
	 * 
	 * courseTimetable.get(i).setMaxEndTimeForCourse(courseTimetable.get(i).
	 * getEnd_time()); logger.info("MaxEnd-------------->"+courseTimetable.get(i)
	 * .getEnd_time()); }
	 * 
	 * 
	 * } i++; } }
	 * 
	 * // ////////////////////////////////////////////////////////////////////////
	 * 
	 * String json = new Gson().toJson(ttFinal);
	 * 
	 * logger.info("timtable List ------->" + json);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * return json; // return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}"; } }
	 */

	private String[] fetchWorkload(String data) {
		String[] workLoad = new String[3];
		try {
			logger.info("fetchWorkload ===> "+data);
			if(!StringUtils.isEmpty(data)) {
				JSONObject jsonObject = new JSONObject(data);
				String allottedLecture = "", conductedLecture = "", remainingLecture = "";
				if (jsonObject.has("allottedLecture")) {
					allottedLecture = String.valueOf(jsonObject.get("allottedLecture"));
				}
				if (jsonObject.has("conductedLecture")) {
					conductedLecture = String.valueOf(jsonObject.get("conductedLecture"));
				}

				if (!allottedLecture.equals("") && !conductedLecture.equals("")) {
					remainingLecture = String
							.valueOf(Double.parseDouble(allottedLecture) - Double.parseDouble(conductedLecture));
				}

				workLoad[0] = allottedLecture;
				workLoad[1] = conductedLecture;
				workLoad[2] = remainingLecture;
			}
			

			return workLoad;
		} catch (Exception e) {
			e.printStackTrace();
			return workLoad;
		}
	}

	@RequestMapping(value = { "/getTimetableByCourseForAndroidApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getTimetableByCourseForAndroidApp(@RequestBody Course course, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		course = (Course) decryptRequestBody(course.getEncrypted_key(), "Course");
		boolean auth = isUserAuthorized(headers.get("token"), course.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		try {

			// logger.info("Course----->"+course);

			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

					.createDefaultConnectionByDS(defaultUrl, defaultUsername,

							defaultPassword);

			DriverManagerDataSource dataSourceTimetable = multipleDBConnection

					.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
							"timetable_metadata");

			timetableDAO.setDS(dataSourceTimetable);
			String cid = "";
			long eventId = 0;
			long pgrmId = 0;
			if (null != course.getId()) {
				cid = course.getId().toString();
				eventId = Long.parseLong(cid.substring(0, 8));
				pgrmId = Long.parseLong(cid.substring(8));
			}
			String username = course.getUsername();
			if (username.contains("_")) {
				username = username.substring(0, username.indexOf("_"));
			}
			String curDate = "";

			if (null == course.getClassDate()) {
				Date dt = Utils.getInIST();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				curDate = "%" + dateFormat.format(dt) + "%";
			} else {
				curDate = "%" + course.getClassDate() + "%";
			}
			logger.info("current Date ------->" + curDate);
			logger.info("course Id ------->" + cid);
			logger.info("event Id ------->" + eventId);
			logger.info("program Id ------->" + pgrmId);
			logger.info("username ------->" + username);
			List<Timetable> tt = new ArrayList<>();
			if (null != course.getId()) {
				tt = timetableDAO.getTimetableByCourse(pgrmId, eventId, username, curDate);
			} else {
				tt = timetableDAO.getTimetableByCourseForApp(username, curDate);
			}

			long pgrmId1;
			String cid1;
			List<Timetable> ttFinal = new ArrayList<>();
			logger.info("tt size" + tt.size());

			Map<String, String> courseIdEndTimeMap = new HashMap<String, String>();

			for (Timetable tmtl : tt) {
				logger.info("EndTime========>>>" + tmtl.getEnd_time());
				String[] programidsStrings = tmtl.getProgramId().split(" , ");
				String programNameToSend = "", courseIdToSend = "", courseNameToSend = "";

				tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
				tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace(".", ":"));
				tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
				tmtl.setMaxEndTimeForCourse(tmtl.getEnd_time());

				logger.info("eventIdWL=======>" + String.valueOf(tmtl.getEventId()));
				logger.info("FacultyIdWL=======>" + username);
				/* String workLoadResponse = attendanceService.pullFacultyWorkload(String.valueOf(tmtl.getEventId()),username); */  String workLoadResponse = null;
				if (null != workLoadResponse) {
					String workload[] = fetchWorkload(workLoadResponse);
					tmtl.setAllottedLecture(workload[0]);
					tmtl.setConductedLecture(workload[1]);
					tmtl.setRemainingLecture(workload[2]);
				} else {
					logger.info("workLoadResponse --> is null");
				}

				if (tmtl.getProgramId().contains(" , ")) {

					Program p1 = new Program();
					Course c1 = new Course();
					boolean flag = false;

					for (int i = 0; i < programidsStrings.length; i++) {

						cid1 = "" + tmtl.getEventId() + programidsStrings[i];
						c1 = courseService.findByID(Long.parseLong(cid1));

						pgrmId1 = Long.parseLong(programidsStrings[i]);
						p1 = programService.findByID(pgrmId1);

						if (null != c1 && null != p1) {

							if (i < programidsStrings.length - 1) {
								programNameToSend = programNameToSend + p1.getProgramName() + " , ";
								courseIdToSend = courseIdToSend + cid1 + " , ";
								courseNameToSend = c1.getCourseName();
							} else {
								programNameToSend = programNameToSend + p1.getProgramName();
								courseIdToSend = courseIdToSend + cid1;
								courseNameToSend = c1.getCourseName();
							}
						}

						else {
							flag = true;
							for (int j = 0; j < programidsStrings.length; j++) {
								String cid11 = "" + tmtl.getEventId() + programidsStrings[j];
								Course c11 = courseService.findByID(Long.parseLong(cid11));
								pgrmId1 = Long.parseLong(programidsStrings[j]);
								Program p11 = programService.findByID(pgrmId1);

								if (null != c11 && null != p11) {
									Timetable tmtlSub = new Timetable();

									tmtlSub.setClass_date(tmtl.getClass_date());
									tmtlSub.setStart_time(tmtl.getStart_time());
									tmtlSub.setEnd_time(tmtl.getEnd_time());
									tmtlSub.setEventId(tmtl.getEventId());
									tmtlSub.setFacultyId(tmtl.getFacultyId());
									tmtlSub.setFlag(tmtl.getFlag());
									cid11 = "" + tmtl.getEventId() + programidsStrings[j];
									tmtlSub.setMaxEndTimeForCourse(tmtl.getEnd_time());
									tmtlSub.setCourseId(cid11);
									tmtlSub.setCourseName(c11.getCourseName());
									tmtlSub.setProgramId(programidsStrings[j]);
									tmtlSub.setProgramName(p11.getProgramName());

									tmtlSub.setAllottedLecture(tmtl.getAllottedLecture());
									tmtlSub.setConductedLecture(tmtl.getConductedLecture());
									tmtlSub.setRemainingLecture(tmtl.getRemainingLecture());
									logger.info(p11.getProgramName());
									if (null != course.getId()) {
										logger.info("courseId------->" + course.getId());
										logger.info("tmtlSub courseId------->" + tmtlSub.getCourseId());
										if (cid11.equals(tmtlSub.getCourseId())) {
											logger.info("addCOurseId------->" + tmtlSub.getCourseId());
											ttFinal.add(tmtlSub);
										}
									} else {
										courseIdEndTimeMap.put(cid11, tmtlSub.getEnd_time());
										logger.info("addCOurseId------->" + tmtlSub.getCourseId());
										ttFinal.add(tmtlSub);
									}
								}

							}
						}

					}

					/*
					 * if (courseNameToSend.substring(courseNameToSend.length() - 3).equals(" , "))
					 * { courseNameToSend = courseNameToSend.substring(0, courseNameToSend.length()
					 * - 3); }
					 */

					if (!flag) {
						if (null != c1 && null != p1) {
							if (programNameToSend.substring(programNameToSend.length() - 3).equals(" , ")) {
								programNameToSend = programNameToSend.substring(0, programNameToSend.length() - 3);
							}
							if (courseIdToSend.substring(courseIdToSend.length() - 3).equals(" , ")) {
								courseIdToSend = courseIdToSend.substring(0, courseIdToSend.length() - 3);
							}

							logger.info("DATA FOR COMMA SEPERATED PROGRAMS------>" + programNameToSend + "==="
									+ courseNameToSend + "===" + courseIdToSend);

							tmtl.setCourseId(courseIdToSend);
							// tmtl.setProgramId(tmtl.getProgramId());
							tmtl.setCourseName(courseNameToSend);
							tmtl.setProgramName(programNameToSend);
							courseIdEndTimeMap.put(courseIdToSend, tmtl.getEnd_time());
							ttFinal.add(tmtl);
						} else {
							logger.info("c1 or p1 ======multiple_courseId_case=======>" + "is NULL");
						}
					}

				} else {
					cid1 = "" + tmtl.getEventId() + tmtl.getProgramId();
					Course c1 = courseService.findByID(Long.parseLong(cid1));
					pgrmId1 = Long.parseLong(tmtl.getProgramId());
					Program p1 = programService.findByID(pgrmId1);

					if (null != c1 && null != p1) {
						logger.info("MaxEndTime======> " + tmtl.getMaxEndTimeForCourse());
						tmtl.setCourseId(cid1);
						tmtl.setCourseName(c1.getCourseName());
						tmtl.setProgramName(p1.getProgramName());
						courseIdEndTimeMap.put(tmtl.getCourseId(), tmtl.getEnd_time());
						ttFinal.add(tmtl);
					} else {
						logger.info("c1 or p1 =======single_courseId_case======>" + "is NULL");
					}
				}

				logger.info("tmtl list " + tmtl);
			}

			for (Object name : courseIdEndTimeMap.keySet()) {
				String key = name.toString();
				String value = courseIdEndTimeMap.get(name);
				logger.info("Map:---->" + key + " " + value);
			}
			logger.info("MapListSize " + String.valueOf(courseIdEndTimeMap.size()));

			// //////////////////////////////////////////////////////////////////

			for (String key : courseIdEndTimeMap.keySet()) {

				// List<Timetable> courseTimetable = new ArrayList<>();
				List<Timetable> courseTimetable = new ArrayList<>();
				int i = 0;
				while (i < ttFinal.size()) {
					logger.info("KEY---->" + key + "\n");
					logger.info("ttFinal---->" + ttFinal.get(i).getCourseId() + "\n");
					if (key.equals(ttFinal.get(i).getCourseId())) {
						// logger.info("key--------->"+key);
						// logger.info("ttFinal.get(i).getCourseId()--------->"+ttFinal.get(i).getCourseId());
						courseTimetable.add(ttFinal.get(i));
					}
					i++;
				}
				i = 0;
				for (Timetable ct : courseTimetable) {
					if (i < courseTimetable.size() - 1) {
						String lecEndTime = ct.getClass_date() + " " + courseTimetable.get(i).getEnd_time();
						String nxtLecStartTime = ct.getClass_date() + " " + courseTimetable.get(i + 1).getStart_time();
						logger.info("lecEndTime >" + lecEndTime + " nextLecStartTime >" + nxtLecStartTime);
						long diff = timeDifference(lecEndTime, nxtLecStartTime);
						if (diff < 60000) {
							logger.info("MaxEnd-------------->" + courseIdEndTimeMap.get(key));
							courseTimetable.get(i).setMaxEndTimeForCourse(courseIdEndTimeMap.get(key));
						} else {
							for (int j = i; j > 0; j--) {
								String lecStartTime = ct.getClass_date() + " " + courseTimetable.get(j).getStart_time();
								String prevLecEndTime = ct.getClass_date() + " "
										+ courseTimetable.get(j - 1).getEnd_time();
								logger.info("lecStartTime >" + lecStartTime + " prevLecEndTime >" + prevLecEndTime);
								long diff1 = timeDifference(prevLecEndTime, lecStartTime);
								logger.info("diff1--------->" + diff1);
								if (diff1 < 60000) {
									courseTimetable.get(j - 1)
											.setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
								}

							}

							courseTimetable.get(i).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
							logger.info("MaxEnd-------------->" + courseTimetable.get(i).getEnd_time());
						}

					}
					i++;
				}
			}

			// ////////////////////////////////////////////////////////////////////////

			String json = new Gson().toJson(ttFinal);

			logger.info("timtable List ------->" + json);

			timetableDAO.setDS(dataSourceDefaultLms);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";

			json = encryptResponseBody(json);
			return json;
		}
	}

	// shubham_ 2020-02-11(offline student storage changes)
	@RequestMapping(value = { "/getStudentsByCourseForAndroidAppNew" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getStudentsByCourseForAndroidAppNew(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		studentCourseAttendance = (StudentCourseAttendance) decryptRequestBody(
				studentCourseAttendance.getEncrypted_key(), "StudentCourseAttendance");
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		try {
			String coursesId = studentCourseAttendance.getCids();
			String isAttendanceAllowed = "true";
			logger.info("coursesId===> " + coursesId);
			logger.info("actualEndTimeD===> " + studentCourseAttendance.getActualEndTime());
			logger.info("actualStartTime===> " + studentCourseAttendance.getStartTime());
			Date date = Utils.getInIST();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date today = new Date();
			String todayDate = df.format(today);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String currentTime = sdf.format(date);
			logger.info("currentTime===> " + currentTime);

			String actualEndTime = todayDate + " " + studentCourseAttendance.getActualEndTime();
			logger.info("actualEndTimeBefore===> " + actualEndTime);
			Date actualEndDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(actualEndTime); // conversion of
																									// string time to
																									// date
			Date newActualEndDate = DateUtils.addHours(actualEndDate, 2); // Adding
																			// 2
																			// hours
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // changing
																					// date
																					// to
																					// time
																					// format
			actualEndTime = dateFormat.format(newActualEndDate); // getting time
																	// again
			logger.info("actualEndTimeAFTER===> " + actualEndTime);

			String startTime = todayDate + " " + studentCourseAttendance.getStartTime();

			logger.info(
					"TimeLimitValueStart===> " + String.valueOf(timeDifference(startTime, currentTime)) + " Seconds");
			logger.info(
					"TimeLimitValueEnd===> " + String.valueOf(timeDifference(currentTime, actualEndTime)) + " Seconds");

			if (timeDifference(currentTime, startTime) > 0 || timeDifference(actualEndTime, currentTime) > 0) {
				if (timeDifference(currentTime, startTime) > 0) {
					isAttendanceAllowed = "Lecture has not started yet...";
					logger.info("isAttendanceAllowed===> Insert " + "false");
				} else {
					isAttendanceAllowed = "Lecture time ended...";
					logger.info("isAttendanceAllowed===> Insert " + "false");

				}
			}

			logger.info("isAttendanceAllowed===> Insert " + "true");

			String courseIdCheck = "";
			List<Course> msCourseList = new ArrayList<Course>();
			if (coursesId.contains(" , ")) {
				logger.info("courseIdType============> " + "Multiple");

				Course courseDetails = new Course();
				courseIdCheck = String.valueOf(coursesId);
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");
				logger.info("courseIdCheckStrings============> " + courseIdCheck);

				for (int i = 0; i < courseIdCheckStrings.length; i++) {
					long courseId = Long.valueOf(courseIdCheckStrings[i]);
					courseDetails = courseService.findByID(courseId);
					if (courseDetails != null) {
						List<Course> courseList = courseService.findStudentsByCourseIdForAndroidApp(courseId);
						msCourseList.addAll(courseList);
					}
				}

			} else {
				logger.info("courseIdType============> " + "Single");
				long courseId = Long.valueOf(coursesId);
				Course courseDetails = courseService.findByID(courseId);

				if (courseDetails != null) {
					msCourseList = courseService.findStudentsByCourseIdForAndroidApp(courseId);
				}

			}

			for (Course course : msCourseList) {
				course.setIsAttendanceAllowed(isAttendanceAllowed);
			}

			String json = new Gson().toJson(msCourseList);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{}";

			json = encryptResponseBody(json);
			return json;
		}
	}

	@RequestMapping(value = { "/getStudentsByCourseForAndroidApp" }, method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getStudentsByCourseForAndroidApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		try {
			String coursesId = studentCourseAttendance.getCids();

			logger.info("coursesId===> " + coursesId);
			logger.info("actualEndTimeD===> " + studentCourseAttendance.getActualEndTime());
			logger.info("actualStartTime===> " + studentCourseAttendance.getStartTime());
			Date date = Utils.getInIST();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date today = new Date();
			String todayDate = df.format(today);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String currentTime = sdf.format(date);
			logger.info("currentTime===> " + currentTime);

			String actualEndTime = todayDate + " " + studentCourseAttendance.getActualEndTime();
			logger.info("actualEndTimeBefore===> " + actualEndTime);
			Date actualEndDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(actualEndTime); // conversion of
																									// string time to
																									// date
			Date newActualEndDate = DateUtils.addHours(actualEndDate, 2); // Adding
																			// 2
																			// hours
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // changing
																					// date
																					// to
																					// time
																					// format
			actualEndTime = dateFormat.format(newActualEndDate); // getting time
																	// again
			logger.info("actualEndTimeAFTER===> " + actualEndTime);

			String startTime = todayDate + " " + studentCourseAttendance.getStartTime();

			logger.info(
					"TimeLimitValueStart===> " + String.valueOf(timeDifference(startTime, currentTime)) + " Seconds");
			logger.info(
					"TimeLimitValueEnd===> " + String.valueOf(timeDifference(currentTime, actualEndTime)) + " Seconds");

			if (timeDifference(currentTime, startTime) > 0 || timeDifference(actualEndTime, currentTime) > 0) {
				if (timeDifference(currentTime, startTime) > 0) {
					logger.info("isAttendanceAllowed===> Insert " + "false");
					String json = "[{\"isAttendanceAllowed\":\"Lecture has not started yet...\"}]";
					return json;
				} else {
					logger.info("isAttendanceAllowed===> Insert " + "false");
					String json = "[{\"isAttendanceAllowed\":\"Lecture time ended...\"}]";
					return json;
				}
			} else {
				logger.info("isAttendanceAllowed===> Insert " + "true");

				String courseIdCheck = "";
				if (coursesId.contains(" , "))

				{
					logger.info("courseIdType============> " + "Multiple");
					List<Course> msCourseList = new ArrayList<Course>();

					Course courseDetails = new Course();
					courseIdCheck = String.valueOf(coursesId);
					String[] courseIdCheckStrings = courseIdCheck.split(" , ");
					logger.info("courseIdCheckStrings============> " + courseIdCheck);

					for (int i = 0; i < courseIdCheckStrings.length; i++) {
						long courseId = Long.valueOf(courseIdCheckStrings[i]);
						courseDetails = courseService.findByID(courseId);
						if (courseDetails != null) {
							List<Course> courseList = courseService.findStudentsByCourseIdForAndroidApp(courseId);
							msCourseList.addAll(courseList);
						}
					}

					String json = new Gson().toJson(msCourseList);
					return json;
				} else {
					logger.info("courseIdType============> " + "Single");
					long courseId = Long.valueOf(coursesId);
					Course courseDetails = courseService.findByID(courseId);
					List<Course> courseList = new ArrayList<Course>();
					if (courseDetails != null) {
						courseList = courseService.findStudentsByCourseIdForAndroidApp(courseId);
					}
					String json = new Gson().toJson(courseList);

					return json;
				}
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			return "{}";
		}
	}

	// shubham_ 2020-02-11(offline student storage changes)
	@RequestMapping(value = { "/showStudentAttendanceForAndroidAppNew" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String showStudentAttendanceForAndroidAppNew(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		studentCourseAttendance = (StudentCourseAttendance) decryptRequestBody(
				studentCourseAttendance.getEncrypted_key(), "StudentCourseAttendance");
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String isAttendanceAllowed = "true";
		String courseIdCheck = studentCourseAttendance.getCourseId();
		logger.info("coursesId===> " + courseIdCheck);
		logger.info("actualEndTimeD===> " + studentCourseAttendance.getActualEndTime());
		logger.info("actualStartTime===> " + studentCourseAttendance.getStartTime());
		Date date = Utils.getInIST();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date today = new Date();
		String todayDate = df.format(today);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String currentTime = sdf.format(date);
		logger.info("currentTime===> " + currentTime);

		String actualEndTime = todayDate + " " + studentCourseAttendance.getActualEndTime();
		logger.info("actualEndTimeBefore===> " + actualEndTime);
		Date actualEndDate = null;
		try {
			actualEndDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(actualEndTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // conversion of string time to date
		Date newActualEndDate = DateUtils.addHours(actualEndDate, 2); // Adding
																		// 2
																		// hours
		DateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // changing
																				// date
																				// to
																				// time
																				// format
		actualEndTime = dateFormate.format(newActualEndDate); // getting time
																// again
		logger.info("actualEndTimeAFTER===> " + actualEndTime);

		String start_Time = todayDate + " " + studentCourseAttendance.getStartTime();

		logger.info("TimeLimitValueStart===> " + String.valueOf(timeDifference(start_Time, currentTime)) + " Seconds");
		logger.info("TimeLimitValueEnd===> " + String.valueOf(timeDifference(currentTime, actualEndTime)) + " Seconds");
		List<StudentCourseAttendance> MasterSCAList = new ArrayList<StudentCourseAttendance>();
		if (timeDifference(currentTime, start_Time) > 0 || timeDifference(actualEndTime, currentTime) > 0) {
			if (timeDifference(currentTime, start_Time) > 0) {
				isAttendanceAllowed = "Lecture has not started yet...";
				logger.info("isAttendanceAllowed===> Insert " + "false");
			} else {
				isAttendanceAllowed = "Lecture time ended...";
				logger.info("isAttendanceAllowed===> Insert " + "false");
			}
		}

		logger.info("isAttendanceAllowed===> Update " + "true");
		try {
			if (courseIdCheck.contains(" , ")) {

				logger.info("courseIdType ===========>" + "MULTILE");
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");

				SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");

				String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				logger.info("attdDate ===========>" + attdDate);
				Date d = dateFormatApp.parse(attdDate);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = dateFormat.format(d) + " " + studentCourseAttendance.getStartTime();
				String endTime = dateFormat.format(d) + " " + studentCourseAttendance.getEndTime();

				for (int i = 0; i < courseIdCheckStrings.length; i++) {
					String spilittedCourseId = courseIdCheckStrings[i];
					List<StudentCourseAttendance> SCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
							spilittedCourseId, startTime, endTime, studentCourseAttendance.getFacultyId());

					MasterSCAList.addAll(SCAList);
				}

			} else {

				logger.info("courseIdType ===========>" + "SINGLE");
				SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");
				// Date d = new Date(studentCourseAttendance.getAttdDate());]

				String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				logger.info("attdDate ===========>" + attdDate);

				Date d = dateFormatApp.parse(attdDate);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = dateFormat.format(d) + " " + studentCourseAttendance.getStartTime();
				String endTime = dateFormat.format(d) + " " + studentCourseAttendance.getEndTime();

				MasterSCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
						studentCourseAttendance.getCourseId(), startTime, endTime,
						studentCourseAttendance.getFacultyId());

			}
			for (StudentCourseAttendance sca : MasterSCAList) {
				sca.setIsAttendanceAllowed(isAttendanceAllowed);
			}
			String json = new Gson().toJson(MasterSCAList);

			logger.info("MasterSCAList ===========>" + MasterSCAList);

			json = encryptResponseBody(json);
			return json;

		} catch (Exception e) {
			logger.error("Exception", e);
			String json = "{\"Status\":\"Fail\"}";

			json = encryptResponseBody(json);
			return json;
		}

	}

	@RequestMapping(value = { "/showStudentAttendanceForAndroidApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String showStudentAttendanceForAndroidApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String courseIdCheck = studentCourseAttendance.getCourseId();
		logger.info("coursesId===> " + courseIdCheck);
		logger.info("actualEndTimeD===> " + studentCourseAttendance.getActualEndTime());
		logger.info("actualStartTime===> " + studentCourseAttendance.getStartTime());
		Date date = Utils.getInIST();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date today = new Date();
		String todayDate = df.format(today);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String currentTime = sdf.format(date);
		logger.info("currentTime===> " + currentTime);

		String actualEndTime = todayDate + " " + studentCourseAttendance.getActualEndTime();
		logger.info("actualEndTimeBefore===> " + actualEndTime);
		Date actualEndDate = null;
		try {
			actualEndDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(actualEndTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // conversion of string time to date
		Date newActualEndDate = DateUtils.addHours(actualEndDate, 2); // Adding
																		// 2
																		// hours
		DateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // changing
																				// date
																				// to
																				// time
																				// format
		actualEndTime = dateFormate.format(newActualEndDate); // getting time
																// again
		logger.info("actualEndTimeAFTER===> " + actualEndTime);

		String start_Time = todayDate + " " + studentCourseAttendance.getStartTime();

		logger.info("TimeLimitValueStart===> " + String.valueOf(timeDifference(start_Time, currentTime)) + " Seconds");
		logger.info("TimeLimitValueEnd===> " + String.valueOf(timeDifference(currentTime, actualEndTime)) + " Seconds");

		if (timeDifference(currentTime, start_Time) > 0 || timeDifference(actualEndTime, currentTime) > 0) {
			if (timeDifference(currentTime, start_Time) > 0) {
				logger.info("isAttendanceAllowed===> Insert " + "false");
				String json = "[{\"isAttendanceAllowed\":\"Lecture has not started yet...\"}]";
				return json;
			} else {
				logger.info("isAttendanceAllowed===> Insert " + "false");
				String json = "[{\"isAttendanceAllowed\":\"Lecture time ended...\"}]";
				return json;
			}
		} else {
			logger.info("isAttendanceAllowed===> Update " + "true");
			if (courseIdCheck.contains(" , ")) {
				try {
					logger.info("courseIdType ===========>" + "MULTILE");
					String[] courseIdCheckStrings = courseIdCheck.split(" , ");
					List<StudentCourseAttendance> MasterSCAList = new ArrayList<StudentCourseAttendance>();
					SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");

					String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
					logger.info("attdDate ===========>" + attdDate);
					Date d = dateFormatApp.parse(attdDate);
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String startTime = dateFormat.format(d) + " " + studentCourseAttendance.getStartTime();
					String endTime = dateFormat.format(d) + " " + studentCourseAttendance.getEndTime();

					for (int i = 0; i < courseIdCheckStrings.length; i++) {
						String spilittedCourseId = courseIdCheckStrings[i];
						List<StudentCourseAttendance> SCAList = studentCourseAttendanceService
								.findByCourseIdAndDateTime(spilittedCourseId, startTime, endTime,
										studentCourseAttendance.getFacultyId());
						MasterSCAList.addAll(SCAList);
					}

					String json = new Gson().toJson(MasterSCAList);
					return json;

				} catch (Exception e) {
					logger.error("Exception", e);
					return "{\"Status\":\"Fail\"}";
				}
			} else {
				try {
					logger.info("courseIdType ===========>" + "SINGLE");
					SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");
					// Date d = new Date(studentCourseAttendance.getAttdDate());]

					String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
					logger.info("attdDate ===========>" + attdDate);

					Date d = dateFormatApp.parse(attdDate);

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String startTime = dateFormat.format(d) + " " + studentCourseAttendance.getStartTime();
					String endTime = dateFormat.format(d) + " " + studentCourseAttendance.getEndTime();

					List<StudentCourseAttendance> SCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
							studentCourseAttendance.getCourseId(), startTime, endTime,
							studentCourseAttendance.getFacultyId());

					String json = new Gson().toJson(SCAList);

					// logger.info("Course List ------->" + json);

					return json;
					// return "{\"Status\":\"Success\"}";

				} catch (Exception e) {
					logger.error("Exception", e);
					return "{\"Status\":\"Fail\"}";

				}
			}
		}
	}

	@RequestMapping(value = { "/showStudentAttendanceStatusForAndroidApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String showStudentAttendanceStatusForAndroidApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		studentCourseAttendance = (StudentCourseAttendance) decryptRequestBody(
				studentCourseAttendance.getEncrypted_key(), "StudentCourseAttendance");
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String courseIdCheck = studentCourseAttendance.getCourseId();

		if (courseIdCheck.contains(" , ")) {
			try {
				logger.info("courseIdType ===========>" + "MULTILE");
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");
				List<StudentCourseAttendance> MasterSCAList = new ArrayList<StudentCourseAttendance>();
				SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");

				String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				logger.info("attdDate ===========>" + attdDate);

				Date d = dateFormatApp.parse(attdDate);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = dateFormat.format(d) + " " + studentCourseAttendance.getStartTime();
				String endTime = dateFormat.format(d) + " " + studentCourseAttendance.getEndTime();

				for (int i = 0; i < courseIdCheckStrings.length; i++) {
					String spilittedCourseId = courseIdCheckStrings[i];
					List<StudentCourseAttendance> SCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
							spilittedCourseId, startTime, endTime, studentCourseAttendance.getFacultyId());
					MasterSCAList.addAll(SCAList);
				}

				String json = new Gson().toJson(MasterSCAList);
				return json;

			} catch (Exception e) {
				logger.error("Exception", e);
				return "{\"Status\":\"Fail\"}";
			}
		} else {
			try {
				logger.info("courseIdType ===========>" + "SINGLE");
				SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");
				// Date d = new Date(studentCourseAttendance.getAttdDate());]

				String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				logger.info("attdDate ===========>" + attdDate);

				Date d = dateFormatApp.parse(attdDate);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = dateFormat.format(d) + " " + studentCourseAttendance.getStartTime();
				String endTime = dateFormat.format(d) + " " + studentCourseAttendance.getEndTime();

				List<StudentCourseAttendance> SCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
						studentCourseAttendance.getCourseId(), startTime, endTime,
						studentCourseAttendance.getFacultyId());

				String json = new Gson().toJson(SCAList);

				json = encryptResponseBody(json);
				return json;

			} catch (Exception e) {
				logger.error("Exception", e);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;
			}
		}
	}

	/*
	 * @RequestMapping(value = { "/insertStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * insertStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = ""; courseIdCheck =
	 * studentCourseAttd.getCourseId();
	 * 
	 * if (studentCourseAttd.getCourseId().contains(" , ")) { try { String[]
	 * courseIdCheckStrings = courseIdCheck.split(" , "); for (int i = 0; i <
	 * courseIdCheckStrings.length; i++) { Map<String, String> absentMap = new
	 * HashMap<>(); String spilittedCourseId = courseIdCheckStrings[i]; List<Course>
	 * StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(spilittedCourseId)); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId = spilittedCourseId; String
	 * eventId = courseId.substring(0, 8); String programId = courseId.substring(8);
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = ""; List<StudentCourseAttendance> studentCourseAttdList = new
	 * ArrayList<>();
	 * 
	 * String absentStudentMapString = studentCourseAttd .getCourseStudentListMap();
	 * absentMap = splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap .get(spilittedCourseId);
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd"); Date date
	 * = new Date();
	 * 
	 * startTime = dateFormat.format(date) + " " + studentCourseAttd.getStartTime();
	 * endTime = dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); acadYear = u.getEnrollmentYear();
	 * sca.setAcadYear(u.getEnrollmentYear()); sca.setOrganization(app);
	 * 
	 * sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * logger.info("----->" + ListofAbsentStudent);
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays .asList(ListofAbsentStudent .substring( 1,
	 * ListofAbsentStudent .length() - 1) .split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent .split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (null == ListofAbsentStudent || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService .insertBatch(studentCourseAttdList);
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { String
	 * punchedStudentId=""; String currentDate = dateFormat.format(date);
	 * logger.info("currentDate" + currentDate); punchedStudentId =
	 * fetchPunchedStudentId(currentDate); logger.info("punchedStudentId--------->"
	 * + punchedStudentId); String punchedStudentId1 =
	 * punchedStudentId.replace("\"", "").replaceAll("\\[",
	 * "").replaceAll("\\]",""); logger.info("punchedStudentId--------->" +
	 * punchedStudentId1); List<String> listPunchedStudentId = Arrays
	 * .asList(studentCourseAttd.getListofAbsStud().split(",")); List<String>
	 * absentUsers = Arrays.asList(studentCourseAttd.getListofAbsStud
	 * ().split("\\s*,\\s*")); //List<String> notificationUser = new ArrayList<>();
	 * for(String abu : absentUsers){ if(listPunchedStudentId.contains(abu)){
	 * //present in college and absent in class
	 * logger.info("Absent User------>"+abu); String parentUsername =
	 * abu.concat("_P"); //sendPushNotificationForApp(parentUsername
	 * ,"Your child is absent in Lecture."); }else{ //absent in college and class
	 * logger.info("Absent User------>"+abu); String parentUsername =
	 * abu.concat("_P"); //sendPushNotificationForApp(parentUsername
	 * ,"Your child is absent in College."); } } }
	 * 
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}"; } } else { try
	 * {
	 * 
	 * List<Course> StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(studentCourseAttd.getCourseId())); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId =
	 * studentCourseAttd.getCourseId(); String eventId = courseId.substring(0, 8);
	 * String programId = courseId.substring(8); String username = "", acadSession =
	 * "", acadYear = "", startTime = "", endTime = "";
	 * List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * startTime = dateFormat.format(date) + " " + studentCourseAttd.getStartTime();
	 * endTime = dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); acadYear = u.getEnrollmentYear();
	 * sca.setAcadYear(u.getEnrollmentYear()); sca.setOrganization(app);
	 * 
	 * sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { List<String> absentUsers
	 * = new ArrayList<String>(); if (studentCourseAttd.getListofAbsStud()
	 * .startsWith("[") && studentCourseAttd.getListofAbsStud() .endsWith("]")) {
	 * absentUsers = Arrays.asList(studentCourseAttd .getListofAbsStud() .substring(
	 * 1, studentCourseAttd .getListofAbsStud() .length() - 1) .split(",\\s*")); }
	 * else { absentUsers = Arrays.asList(studentCourseAttd
	 * .getListofAbsStud().split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService .insertBatch(studentCourseAttdList);
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { String
	 * punchedStudentId=""; String currentDate = dateFormat.format(date);
	 * logger.info("currentDate" + currentDate); punchedStudentId =
	 * fetchPunchedStudentId(currentDate); logger.info("punchedStudentId--------->"
	 * + punchedStudentId); String punchedStudentId1 =
	 * punchedStudentId.replace("\"", "").replaceAll("\\[",
	 * "").replaceAll("\\]",""); logger.info("punchedStudentId--------->" +
	 * punchedStudentId1); List<String> listPunchedStudentId = Arrays
	 * .asList(studentCourseAttd.getListofAbsStud().split(",")); List<String>
	 * absentUsers = Arrays.asList(studentCourseAttd.getListofAbsStud
	 * ().split("\\s*,\\s*")); //List<String> notificationUser = new ArrayList<>();
	 * for(String abu : absentUsers){ if(listPunchedStudentId.contains(abu)){
	 * //present in college and absent in class
	 * logger.info("Absent User------>"+abu); String parentUsername =
	 * abu.concat("_P"); //sendPushNotificationForApp(parentUsername
	 * ,"Your child is absent in Lecture."); }else{ //absent in college and class
	 * logger.info("Absent User------>"+abu); String parentUsername =
	 * abu.concat("_P"); //sendPushNotificationForApp(parentUsername
	 * ,"Your child is absent in College."); } } }
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	/*
	 * @RequestMapping(value = { "/insertStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * insertStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = ""; courseIdCheck =
	 * studentCourseAttd.getCourseId();
	 * 
	 * if (studentCourseAttd.getCourseId().contains(" , ")) { try { String[]
	 * courseIdCheckStrings = courseIdCheck.split(" , "); for (int i = 0; i <
	 * courseIdCheckStrings.length; i++) { Map<String, String> absentMap = new
	 * HashMap<>(); String spilittedCourseId = courseIdCheckStrings[i]; List<Course>
	 * StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(spilittedCourseId)); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId = spilittedCourseId; String
	 * eventId = courseId.substring(0, 8); String programId = courseId.substring(8);
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate=""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); String absentStudentMapString =
	 * studentCourseAttd .getCourseStudentListMap(); absentMap =
	 * splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap .get(spilittedCourseId);
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd"); Date date
	 * = new Date();
	 * 
	 * if(null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->"+
	 * "class date Found (insert multiple courseId)"); startTime = classDate + " "+
	 * studentCourseAttd.getStartTime(); endTime = classDate + " "+
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->"+ startTime); logger.info("endTime----->"+
	 * endTime); logger.info("classDate----->"+ classDate); } else {
	 * logger.info("classDateInfo----->"+
	 * "No class date Found (insert multiple courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->"+ startTime); logger.info("endTime----->"+
	 * endTime); logger.info("classDate----->"+ classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId);
	 * 
	 * sca.setEventId(eventId); sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); Course c =
	 * courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadYear(c.getAcadYear()); acadYear = c.getAcadYear(); //acadYear =
	 * u.getEnrollmentYear(); //sca.setAcadYear(u.getEnrollmentYear());
	 * sca.setOrganization(app);
	 * 
	 * sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * logger.info("----->" + ListofAbsentStudent);
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays .asList(ListofAbsentStudent .substring( 1,
	 * ListofAbsentStudent .length() - 1) .split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent .split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (null == ListofAbsentStudent || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.upsertBatch(studentCourseAttdList);
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}"; } } else { try
	 * {
	 * 
	 * List<Course> StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(studentCourseAttd.getCourseId())); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId =
	 * studentCourseAttd.getCourseId(); String eventId = courseId.substring(0, 8);
	 * String programId = courseId.substring(8); String username = "", acadSession =
	 * "", acadYear = "", startTime = "", endTime = "", classDate="";
	 * List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * classDate = studentCourseAttd.getClassDate(); SimpleDateFormat dateFormat =
	 * new SimpleDateFormat("yyyy-MM-dd"); Date date = new Date();
	 * 
	 * if(null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->"+
	 * "class date Found (insert single courseId)"); startTime = classDate + " "+
	 * studentCourseAttd.getStartTime(); endTime = classDate + " "+
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->"+ startTime); logger.info("endTime----->"+
	 * endTime); logger.info("classDate----->"+ classDate); } else {
	 * logger.info("classDateInfo----->"+
	 * "No class date Found (insert single courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->"+ startTime); logger.info("endTime----->"+
	 * endTime); logger.info("classDate----->"+ classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); Course c =
	 * courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadYear(c.getAcadYear()); acadYear = c.getAcadYear(); //acadYear =
	 * u.getEnrollmentYear(); //sca.setAcadYear(u.getEnrollmentYear());
	 * sca.setOrganization(app);
	 * 
	 * sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { List<String> absentUsers
	 * = new ArrayList<String>(); if (studentCourseAttd.getListofAbsStud()
	 * .startsWith("[") && studentCourseAttd.getListofAbsStud() .endsWith("]")) {
	 * absentUsers = Arrays.asList(studentCourseAttd .getListofAbsStud() .substring(
	 * 1, studentCourseAttd .getListofAbsStud() .length() - 1) .split(",\\s*")); }
	 * else { absentUsers = Arrays.asList(studentCourseAttd
	 * .getListofAbsStud().split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.upsertBatch(studentCourseAttdList);
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	/*
	 * @RequestMapping(value = { "/insertStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * insertStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = ""; courseIdCheck =
	 * studentCourseAttd.getCourseId(); Map<String, String> courseAcadYearMap = new
	 * HashMap<String, String>();
	 * 
	 * if (studentCourseAttd.getCourseId().contains(" , ")) { try { String[]
	 * courseIdCheckStrings = courseIdCheck.split(" , "); for (int i = 0; i <
	 * courseIdCheckStrings.length; i++) { Map<String, String> absentMap = new
	 * HashMap<>(); String spilittedCourseId = courseIdCheckStrings[i]; List<Course>
	 * StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(spilittedCourseId));
	 * logger.info("flag--------->" + studentCourseAttd.getFlag()); String courseId
	 * = spilittedCourseId; String eventId = courseId.substring(0, 8); String
	 * programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); String absentStudentMapString =
	 * studentCourseAttd.getCourseStudentListMap(); absentMap =
	 * splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap.get(spilittedCourseId);
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (insert multiple courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (insert multiple courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId);
	 * 
	 * sca.setEventId(eventId); sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); sca.setOrganization(app); sca.setStartTime(startTime);
	 * sca.setEndTime(endTime); // Course c = //
	 * courseService.findByID(Long.valueOf(sca.getCourseId())); //
	 * sca.setAcadYear(c.getAcadYear()); // acadYear = c.getAcadYear(); // acadYear
	 * = u.getEnrollmentYear(); // sca.setAcadYear(u.getEnrollmentYear()); Course c
	 * = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear); logger.info("----->" + ListofAbsentStudent);
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays.asList(ListofAbsentStudent .substring(1,
	 * ListofAbsentStudent.length() - 1).split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent.split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (null == ListofAbsentStudent || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.upsertBatch(studentCourseAttdList);
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}"; } } else { try
	 * {
	 * 
	 * List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttd.getCourseId())
	 * ); logger.info("flag--------->" + studentCourseAttd.getFlag()); String
	 * courseId = studentCourseAttd.getCourseId(); String eventId =
	 * courseId.substring(0, 8); String programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); // String sapAcadYear = //
	 * timetableDAO.getAcadYearFromSapMaster("51709397", // "50474742");
	 * courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * classDate = studentCourseAttd.getClassDate(); SimpleDateFormat dateFormat =
	 * new SimpleDateFormat("yyyy-MM-dd"); Date date = new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (insert single courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (insert single courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); sca.setOrganization(app); sca.setStartTime(startTime);
	 * sca.setEndTime(endTime);
	 * 
	 * // Course c = // courseService.findByID(Long.valueOf(sca.getCourseId())); //
	 * sca.setAcadYear(c.getAcadYear()); // acadYear = c.getAcadYear(); //
	 * //acadYear = u.getEnrollmentYear(); //
	 * //sca.setAcadYear(u.getEnrollmentYear()); Course c =
	 * courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { List<String> absentUsers
	 * = new ArrayList<String>(); if
	 * (studentCourseAttd.getListofAbsStud().startsWith("[") &&
	 * studentCourseAttd.getListofAbsStud().endsWith("]")) { absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud() .substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*")); } else {
	 * absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.upsertBatch(studentCourseAttdList);
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	// 08-05-2020 shubham

	/*
	 * @RequestMapping(value = { "/insertStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * insertStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = ""; courseIdCheck =
	 * studentCourseAttd.getCourseId(); Map<String, String> courseAcadYearMap = new
	 * HashMap<String, String>();
	 * 
	 * if (studentCourseAttd.getCourseId().contains(" , ")) { try { String[]
	 * courseIdCheckStrings = courseIdCheck.split(" , "); for (int i = 0; i <
	 * courseIdCheckStrings.length; i++) {
	 * 
	 * Map<String, String> absentMap = new HashMap<>(); String spilittedCourseId =
	 * courseIdCheckStrings[i]; List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(spilittedCourseId));
	 * logger.info("flag--------->" + studentCourseAttd.getFlag()); String courseId
	 * = spilittedCourseId; String eventId = courseId.substring(0, 8); String
	 * programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); String absentStudentMapString =
	 * studentCourseAttd.getCourseStudentListMap(); absentMap =
	 * splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap.get(spilittedCourseId);
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (insert multiple courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (insert multiple courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId);
	 * 
	 * sca.setEventId(eventId); sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); sca.setOrganization(app); sca.setStartTime(startTime);
	 * sca.setEndTime(endTime);
	 * 
	 * Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear); logger.info("----->" + ListofAbsentStudent);
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays.asList(ListofAbsentStudent .substring(1,
	 * ListofAbsentStudent.length() - 1).split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent.split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (null == ListofAbsentStudent || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present"); studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(
	 * spilittedCourseId, startTime, endTime, studentCourseAttd.getFacultyId());
	 * logger.info("sca_count--------->" + sca_count); if (sca_count == 0) {
	 * studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList); }
	 * else { String courseNameToSend = studentCourseAttendanceService
	 * .getCourseNameFromCourseId(spilittedCourseId); String insertFailedFlag =
	 * "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
	 * + startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend +
	 * ") has already been sent to SAP"; String playerid =
	 * studentCourseAttendanceService
	 * .getPlayerIdForFaculty(studentCourseAttd.getFacultyId());
	 * logger.info("playerid--------->" + playerid); String response =
	 * notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
	 * logger.info("responseNotifyFaculty--------->" + response);
	 * 
	 * } }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}"; } } else { try
	 * {
	 * 
	 * List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttd.getCourseId())
	 * ); logger.info("flag--------->" + studentCourseAttd.getFlag()); String
	 * courseId = studentCourseAttd.getCourseId(); String eventId =
	 * courseId.substring(0, 8); String programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); // String sapAcadYear = //
	 * timetableDAO.getAcadYearFromSapMaster("51709397", // "50474742");
	 * courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("getListofAbsStud----->" + studentCourseAttd.getListofAbsStud());
	 * classDate = studentCourseAttd.getClassDate(); SimpleDateFormat dateFormat =
	 * new SimpleDateFormat("yyyy-MM-dd"); Date date = new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (insert single courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (insert single courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * sca.setActive("Y"); sca.setOrganization(app); sca.setStartTime(startTime);
	 * sca.setEndTime(endTime);
	 * 
	 * Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { List<String> absentUsers
	 * = new ArrayList<String>(); if
	 * (studentCourseAttd.getListofAbsStud().startsWith("[") &&
	 * studentCourseAttd.getListofAbsStud().endsWith("]")) { absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud() .substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*")); } else {
	 * absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*")); }
	 * 
	 * logger.info("absentUsers----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * logger.info("presentAll---->"); StudentCourseAttendance sca1 = new
	 * StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttdList.add(sca1);
	 * 
	 * }
	 * 
	 * int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(
	 * studentCourseAttd.getCourseId(), startTime, endTime,
	 * studentCourseAttd.getFacultyId()); logger.info("sca_count--------->" +
	 * sca_count); if (sca_count == 0) {
	 * studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList); }
	 * else { String courseNameToSend = studentCourseAttendanceService
	 * .getCourseNameFromCourseId(studentCourseAttd.getCourseId()); String
	 * insertFailedFlag =
	 * "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
	 * + startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend +
	 * ") has already been sent to SAP"; String playerid =
	 * studentCourseAttendanceService
	 * .getPlayerIdForFaculty(studentCourseAttd.getFacultyId());
	 * logger.info("playerid--------->" + playerid); String response =
	 * notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
	 * logger.info("responseNotifyFaculty--------->" + response);
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	@RequestMapping(value = { "/insertStudentAttendanceForAndroidApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String insertStudentAttendanceForAndroidApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		studentCourseAttendance = (StudentCourseAttendance) decryptRequestBody(
				studentCourseAttendance.getEncrypted_key(), "StudentCourseAttendance");
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String courseIdCheck = "";
		String presentFacultyId = studentCourseAttendance.getPresentFacultyId();
		logger.info("presentFacultyId_Before----->" + presentFacultyId);
		if (presentFacultyId.contains("(")) {
			presentFacultyId = getSapIdOnly(presentFacultyId);
			logger.info("presentFacultyId_After----->" + presentFacultyId);
		}
		boolean multipleFacCheck = true;
		if (presentFacultyId.equalsIgnoreCase("NA")) {
			presentFacultyId = null;
			multipleFacCheck = false;
		}

		String commaSeparatedFacultyId = studentCourseAttendance.getAllFacultyId();
		logger.info("commaSeparatedFacultyId_Before----->" + commaSeparatedFacultyId);
		if (commaSeparatedFacultyId.contains("(")) {
			commaSeparatedFacultyId = getSapIdOnly(commaSeparatedFacultyId);
			logger.info("commaSeparatedFacultyId_After----->" + commaSeparatedFacultyId);
		}
		logger.info("allFacultyIdBefore----->" + commaSeparatedFacultyId);

		List<Long> allFacultyId = new ArrayList<Long>();
		if (commaSeparatedFacultyId.contains(",")) {
			String[] multipleAllFacultyId = commaSeparatedFacultyId.replace(" ", "").split(",");
			for (int m = 0; m < multipleAllFacultyId.length; m++) {
				allFacultyId.add(Long.parseLong(multipleAllFacultyId[m]));
			}
		} else {
			allFacultyId.add(Long.parseLong(commaSeparatedFacultyId));
		}

		logger.info("presentFacultyId----->" + presentFacultyId);
		logger.info("allFacultyId----->" + allFacultyId);
		courseIdCheck = studentCourseAttendance.getCourseId();
		Map<String, String> courseAcadYearMap = new HashMap<String, String>();

		if (studentCourseAttendance.getCourseId().contains(" , ")) {
			try {
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");
				for (int i = 0; i < courseIdCheckStrings.length; i++) {

					Map<String, String> absentMap = new HashMap<>();
					String spilittedCourseId = courseIdCheckStrings[i];
					List<Course> StudentList = courseService
							.findStudentsByCourseIdForApp(Long.parseLong(spilittedCourseId));
					logger.info("flag--------->" + studentCourseAttendance.getFlag());
					String courseId = spilittedCourseId;
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);

					List<Long> mycourseIdList = new ArrayList<Long>();
					for (String s : courseIdCheckStrings) {
						mycourseIdList.add(Long.valueOf(s));
					}

					// ///////////////////get acadyear from sapmAster

					MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

					DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

							.createDefaultConnectionByDS(defaultUrl, defaultUsername,

									defaultPassword);

					DriverManagerDataSource dataSourceTimetable = multipleDBConnection

							.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
									"sap_master_inc");

					timetableDAO.setDS(dataSourceTimetable);

					String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId, programId);
					courseAcadYearMap.put(courseId, sapAcadYear);

					timetableDAO.setDS(dataSourceDefaultLms);

					// ///////////////////

					String username = "", acadSession = "", acadYear = "", startTime = "", endTime = "", classDate = "";
					List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
					classDate = studentCourseAttendance.getClassDate();
					String absentStudentMapString = studentCourseAttendance.getCourseStudentListMap();
					absentMap = splitToMap(absentStudentMapString);

					String ListofAbsentStudent = absentMap.get(spilittedCourseId);

					logger.info("getListofAbsStud----->" + studentCourseAttendance.getListofAbsStud());

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();

					if (null != classDate && !classDate.equals("")) {
						logger.info("classDateInfo----->" + "class date Found (insert multiple courseId)");
						startTime = classDate + " " + studentCourseAttendance.getStartTime();
						endTime = classDate + " " + studentCourseAttendance.getEndTime();

						logger.info("startTime----->" + startTime);
						logger.info("endTime----->" + endTime);
						logger.info("classDate----->" + classDate);
					} else {
						logger.info("classDateInfo----->" + "No class date Found (insert multiple courseId)");
						startTime = dateFormat.format(date) + " " + studentCourseAttendance.getStartTime();
						endTime = dateFormat.format(date) + " " + studentCourseAttendance.getEndTime();

						logger.info("startTime----->" + startTime);
						logger.info("endTime----->" + endTime);
						logger.info("classDate----->" + classDate);
					}

					for (Course cr : StudentList) {
						User u = userService.findByUserName(cr.getUsername());

						StudentCourseAttendance sca = new StudentCourseAttendance();

						sca.setCourseId(spilittedCourseId);

						sca.setEventId(eventId);
						sca.setProgramId(programId);

						username = u.getUsername();
						sca.setUsername(u.getUsername());
						sca.setRollNo(u.getRollNo());
						sca.setFacultyId(studentCourseAttendance.getFacultyId());
						sca.setCreatedBy(studentCourseAttendance.getFacultyId());
						sca.setLastModifiedBy(studentCourseAttendance.getFacultyId());
						sca.setNoOfLec(studentCourseAttendance.getNoOfLec());
						sca.setFlag(studentCourseAttendance.getFlag());
						sca.setPresentFacultyId(presentFacultyId);

						acadSession = u.getAcadSession();
						sca.setAcadSession(u.getAcadSession());
						sca.setActive("Y");
						sca.setOrganization(app);
						sca.setStartTime(startTime);
						sca.setEndTime(endTime);

						Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
						sca.setAcadSession(c.getAcadSession());
						acadSession = c.getAcadSession();
						acadYear = courseAcadYearMap.get(sca.getCourseId());
						sca.setAcadYear(acadYear);
						logger.info("----->" + ListofAbsentStudent);

						if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
							List<String> absentUsers = new ArrayList<String>();
							if (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
								absentUsers = Arrays.asList(ListofAbsentStudent
										.substring(1, ListofAbsentStudent.length() - 1).split(",\\s*"));
							} else {
								absentUsers = Arrays.asList(ListofAbsentStudent.split("\\s*,\\s*"));
							}

							logger.info("absentUsers----->" + absentUsers);

							if (absentUsers.contains(u.getUsername())) {

								sca.setStatus("Absent");

							} else {
								sca.setStatus("Present");
							}
						} else {
							sca.setStatus("Present");
						}
						studentCourseAttdList.add(sca);

					}

					if (null == ListofAbsentStudent || ListofAbsentStudent.equals("")
							|| ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
						logger.info("presentAll---->");
						StudentCourseAttendance sca1 = new StudentCourseAttendance();

						sca1.setCourseId(spilittedCourseId);
						sca1.setEventId(eventId);
						sca1.setProgramId(programId);

						sca1.setFacultyId(studentCourseAttendance.getFacultyId());
						sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
						sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
						sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
						sca1.setFlag(studentCourseAttendance.getFlag());
						sca1.setPresentFacultyId(presentFacultyId);
						sca1.setAcadSession(acadSession);
						sca1.setAcadYear(acadYear);
						sca1.setOrganization(app);
						sca1.setStartTime(startTime);
						sca1.setEndTime(endTime);
						sca1.setActive("Y");
						sca1.setDelFlag("N");
						sca1.setStatus("Present");
						studentCourseAttdList.add(sca1);

					}

					int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(spilittedCourseId,
							startTime, endTime, studentCourseAttendance.getFacultyId());
					logger.info("sca_count--------->" + sca_count);

					if (multipleFacCheck && allFacultyId.toString().contains(",")) {
						// checking if any faculty(among all faculty) has marked attendance or not

						int att_sca_count = studentCourseAttendanceService.getDataCountByAllFacultyId(spilittedCourseId,
								startTime, endTime, allFacultyId);

						// showStudentAttendanceForAndroidAppFromStudentCourseAttendance

						List<StudentCourseAttendance> previousMarkedList = new ArrayList<StudentCourseAttendance>();

						if (att_sca_count > 0) {

							List<StudentCourseAttendance> previousMarkedListFaculty = studentCourseAttendanceService
									.getDataForAllFacultyId(mycourseIdList, startTime, endTime, allFacultyId);

							StudentCourseAttendance sca_all_marked = new StudentCourseAttendance();
							sca_all_marked.setCids(courseIdCheck);
							sca_all_marked.setCourseId(courseIdCheck);
							sca_all_marked.setStartTime(studentCourseAttendance.getStartTime());
							sca_all_marked.setEndTime(studentCourseAttendance.getEndTime());
							sca_all_marked.setActualEndTime(studentCourseAttendance.getEndTime());
							sca_all_marked.setFacultyId(previousMarkedListFaculty.get(0).getAllFacultyId());

							logger.info("FACULTY_ID===>" + studentCourseAttendance.getFacultyId());

							previousMarkedList = showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
									sca_all_marked);

							// checking if this faculty who is marking now has only marked earlier or not
							int fac_sca_count = studentCourseAttendanceService.getDataCountByOneFacultyId(
									spilittedCourseId, startTime, endTime, studentCourseAttendance.getFacultyId());
							if (fac_sca_count > 0) {
								// normal flow
								if (sca_count == 0) {
									studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList);
								} else {
									String courseNameToSend = studentCourseAttendanceService
											.getCourseNameFromCourseId(spilittedCourseId);
									String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
											+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
											+ ") has already been sent to SAP";
									String playerid = studentCourseAttendanceService
											.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
									logger.info("playerid--------->" + playerid);
									if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
										String response = notifyFacultyForFailedAttendanceEntry(playerid,
												insertFailedFlag);
										logger.info("responseNotifyFaculty--------->" + response);
									}

								}
							} else {
								// this faculty cannot mark attendance because some other faculty has already
								// marked attendance before
								// reject this attendance
								// notify user

								String courseNameToSend = studentCourseAttendanceService
										.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
								String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
										+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
										+ ") has been rejected because some other faculty has already marked attendance";
								String playerid = studentCourseAttendanceService
										.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
								logger.info("playerid--------->" + playerid);
								if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
									String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
									logger.info("responseNotifyFaculty--------->" + response);
								}

								// get name of Faculty Name for multiple faculty
								for (int t = 0; t < previousMarkedList.size(); t++) {
									String allFaculty_Id = previousMarkedList.get(t).getPresentFacultyId();
									logger.info("allFaculty_Id----------> " + allFaculty_Id);
									if (allFaculty_Id.contains(",")) {
										allFaculty_Id = allFaculty_Id.replace(",", " , ");
										String[] multipleFacultyArr = allFaculty_Id.split(" , ");
										List<String> multipleFacultyList = Arrays.asList(multipleFacultyArr);
										logger.info("multipleFacultyList----------> " + multipleFacultyList.toString());
										List<User> facultyListWithName = userService
												.findNameOfFaculty(multipleFacultyList);
										logger.info("facultyListWithName----------> " + facultyListWithName.toString());
										allFaculty_Id = "";
										for (User user : facultyListWithName) {
											allFaculty_Id += user.getUsername() + " , ";
										}
										allFaculty_Id = allFaculty_Id.substring(0, allFaculty_Id.length() - 3);
										logger.info("allFaculty_Id_After----------> " + allFaculty_Id);
										previousMarkedList.get(t).setPresentFacultyId(allFaculty_Id);
									}
								}
								StudentCourseAttendance sca_failed = new StudentCourseAttendance();
								sca_failed.setFailStatus("Fail_MF_AM");
								previousMarkedList.add(sca_failed);
								String myResp = new Gson().toJson(previousMarkedList);
								logger.info("myResp--------->" + myResp);
								myResp = encryptResponseBody(myResp);
								return myResp;

							}
						} else {
							// no has marked this lecture till now so any faculty can mark attendance
							// normal flow
							if (sca_count == 0) {
								studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList);
							} else {
								String courseNameToSend = studentCourseAttendanceService
										.getCourseNameFromCourseId(spilittedCourseId);
								String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
										+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
										+ ") has already been sent to SAP";
								String playerid = studentCourseAttendanceService
										.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
								logger.info("playerid--------->" + playerid);
								if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
									String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
									logger.info("responseNotifyFaculty--------->" + response);
								}

							}

						}

					} else {
						// there is single faculty available to mark attendance, so no need to verify
						// previous marked attendance
						// normal flow
						if (sca_count == 0) {
							studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList);
						} else {
							String courseNameToSend = studentCourseAttendanceService
									.getCourseNameFromCourseId(spilittedCourseId);
							String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
									+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
									+ ") has already been sent to SAP";
							String playerid = studentCourseAttendanceService
									.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
							logger.info("playerid--------->" + playerid);
							if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
								String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
								logger.info("responseNotifyFaculty--------->" + response);
							}

						}
					}

				}
				String json = "{\"Status\":\"Success\"}";

				json = encryptResponseBody(json);
				return json;
			} catch (Exception e) {
				logger.error("Exception", e);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;
			}
		} else {
			try {

				List<Course> StudentList = courseService
						.findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttendance.getCourseId()));
				logger.info("flag--------->" + studentCourseAttendance.getFlag());
				String courseId = studentCourseAttendance.getCourseId();
				String eventId = courseId.substring(0, 8);
				String programId = courseId.substring(8);

				List<Long> mycourseIdList = new ArrayList<Long>();
				mycourseIdList.add(Long.valueOf(courseId));
				// ///////////////////get acadyear from sapmAster

				MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

				DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

						.createDefaultConnectionByDS(defaultUrl, defaultUsername,

								defaultPassword);

				DriverManagerDataSource dataSourceTimetable = multipleDBConnection

						.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
								"sap_master_inc");

				timetableDAO.setDS(dataSourceTimetable);

				String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId, programId);

				courseAcadYearMap.put(courseId, sapAcadYear);

				timetableDAO.setDS(dataSourceDefaultLms);

				// ///////////////////

				String username = "", acadSession = "", acadYear = "", startTime = "", endTime = "", classDate = "";
				List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();

				logger.info("getListofAbsStud----->" + studentCourseAttendance.getListofAbsStud());
				classDate = studentCourseAttendance.getClassDate();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();

				if (null != classDate && !classDate.equals("")) {
					logger.info("classDateInfo----->" + "class date Found (insert single courseId)");
					startTime = classDate + " " + studentCourseAttendance.getStartTime();
					endTime = classDate + " " + studentCourseAttendance.getEndTime();

					logger.info("startTime----->" + startTime);
					logger.info("endTime----->" + endTime);
					logger.info("classDate----->" + classDate);
				} else {
					logger.info("classDateInfo----->" + "No class date Found (insert single courseId)");
					startTime = dateFormat.format(date) + " " + studentCourseAttendance.getStartTime();
					endTime = dateFormat.format(date) + " " + studentCourseAttendance.getEndTime();

					logger.info("startTime----->" + startTime);
					logger.info("endTime----->" + endTime);
					logger.info("classDate----->" + classDate);
				}

				for (Course cr : StudentList) {
					User u = userService.findByUserName(cr.getUsername());

					StudentCourseAttendance sca = new StudentCourseAttendance();

					sca.setCourseId(studentCourseAttendance.getCourseId());
					sca.setEventId(eventId);
					sca.setProgramId(programId);

					username = u.getUsername();
					sca.setUsername(u.getUsername());
					sca.setRollNo(u.getRollNo());
					sca.setFacultyId(studentCourseAttendance.getFacultyId());
					sca.setCreatedBy(studentCourseAttendance.getFacultyId());
					sca.setLastModifiedBy(studentCourseAttendance.getFacultyId());
					sca.setNoOfLec(studentCourseAttendance.getNoOfLec());
					sca.setFlag(studentCourseAttendance.getFlag());
					sca.setPresentFacultyId(presentFacultyId);

					acadSession = u.getAcadSession();
					sca.setAcadSession(u.getAcadSession());
					sca.setActive("Y");
					sca.setOrganization(app);
					sca.setStartTime(startTime);
					sca.setEndTime(endTime);

					Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
					sca.setAcadSession(c.getAcadSession());
					acadSession = c.getAcadSession();
					acadYear = courseAcadYearMap.get(sca.getCourseId());
					sca.setAcadYear(acadYear);

					logger.info("----->" + studentCourseAttendance.getListofAbsStud());

					if (null != studentCourseAttendance.getListofAbsStud()
							|| !studentCourseAttendance.getListofAbsStud().equals("")) {
						List<String> absentUsers = new ArrayList<String>();
						if (studentCourseAttendance.getListofAbsStud().startsWith("[")
								&& studentCourseAttendance.getListofAbsStud().endsWith("]")) {
							absentUsers = Arrays.asList(studentCourseAttendance.getListofAbsStud()
									.substring(1, studentCourseAttendance.getListofAbsStud().length() - 1)
									.split(",\\s*"));
						} else {
							absentUsers = Arrays.asList(studentCourseAttendance.getListofAbsStud().split("\\s*,\\s*"));
						}

						logger.info("absentUsers----->" + absentUsers);

						if (absentUsers.contains(u.getUsername())) {

							sca.setStatus("Absent");

						} else {
							sca.setStatus("Present");
						}
					} else {
						sca.setStatus("Present");
					}
					studentCourseAttdList.add(sca);

				}

				if (studentCourseAttendance.getListofAbsStud().equals(null)
						|| studentCourseAttendance.getListofAbsStud().equals("")
						|| studentCourseAttendance.getListofAbsStud().equals("[]")) {
					logger.info("presentAll---->");
					StudentCourseAttendance sca1 = new StudentCourseAttendance();

					sca1.setCourseId(studentCourseAttendance.getCourseId());
					sca1.setEventId(eventId);
					sca1.setProgramId(programId);

					sca1.setFacultyId(studentCourseAttendance.getFacultyId());
					sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
					sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
					sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
					sca1.setFlag(studentCourseAttendance.getFlag());
					sca1.setPresentFacultyId(presentFacultyId);

					sca1.setAcadSession(acadSession);
					sca1.setAcadYear(acadYear);
					sca1.setOrganization(app);
					sca1.setStartTime(startTime);
					sca1.setEndTime(endTime);
					sca1.setActive("Y");
					sca1.setDelFlag("N");
					sca1.setStatus("Present");

					studentCourseAttdList.add(sca1);

				}

				int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(
						studentCourseAttendance.getCourseId(), startTime, endTime,
						studentCourseAttendance.getFacultyId());
				logger.info("sca_count--------->" + sca_count);

				if (multipleFacCheck && allFacultyId.toString().contains(",")) {
					logger.info("allFacultyId1--------->" + allFacultyId.toString());
					// checking if any faculty(among all faculty) has marked attendance or not
					int att_sca_count = studentCourseAttendanceService.getDataCountByAllFacultyId(
							studentCourseAttendance.getCourseId(), startTime, endTime, allFacultyId);
					List<StudentCourseAttendance> previousMarkedList = new ArrayList<StudentCourseAttendance>();

					logger.info("att_sca_count--------->" + att_sca_count);
					if (att_sca_count > 0) {
						List<StudentCourseAttendance> previousMarkedListFaculty = studentCourseAttendanceService
								.getDataForAllFacultyId(mycourseIdList, startTime, endTime, allFacultyId);

						StudentCourseAttendance sca_all_marked = new StudentCourseAttendance();
						sca_all_marked.setCids(courseIdCheck);
						sca_all_marked.setCourseId(courseIdCheck);
						sca_all_marked.setStartTime(studentCourseAttendance.getStartTime());
						sca_all_marked.setEndTime(studentCourseAttendance.getEndTime());
						sca_all_marked.setActualEndTime(studentCourseAttendance.getEndTime());
						sca_all_marked.setFacultyId(previousMarkedListFaculty.get(0).getFacultyId());

						previousMarkedList = showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
								sca_all_marked);

						// checking if this faculty who is marking now has only marked earlier or not
						int fac_sca_count = studentCourseAttendanceService.getDataCountByOneFacultyId(
								studentCourseAttendance.getCourseId(), startTime, endTime,
								studentCourseAttendance.getFacultyId());
						logger.info("fac_sca_count--------->" + fac_sca_count);
						if (fac_sca_count > 0) {

							// normal flow
							if (sca_count == 0) {
								studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList);
							} else {
								String courseNameToSend = studentCourseAttendanceService
										.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
								String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
										+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
										+ ") has already been sent to SAP";
								String playerid = studentCourseAttendanceService
										.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
								logger.info("playerid--------->" + playerid);
								if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
									String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
									logger.info("responseNotifyFaculty--------->" + response);
								}

							}
						} else {
							// this faculty cannot mark attendance because some other faculty has already
							// marked attendance before
							// reject this attendance
							// notify user
							String courseNameToSend = studentCourseAttendanceService
									.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
							String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
									+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
									+ ") has been rejected because some other faculty has already marked attendance";
							String playerid = studentCourseAttendanceService
									.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
							logger.info("playerid--------->" + playerid);

							if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
								String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
								logger.info("responseNotifyFaculty--------->" + response);
							}

							// get name of Faculty Name for multiple faculty
							for (int t = 0; t < previousMarkedList.size(); t++) {
								String allFaculty_Id = previousMarkedList.get(t).getPresentFacultyId();
								logger.info("allFaculty_Id----------> " + allFaculty_Id);
								if (allFaculty_Id.contains(",")) {
									allFaculty_Id = allFaculty_Id.replace(",", " , ");
									String[] multipleFacultyArr = allFaculty_Id.split(" , ");
									List<String> multipleFacultyList = Arrays.asList(multipleFacultyArr);
									logger.info("multipleFacultyList----------> " + multipleFacultyList.toString());
									List<User> facultyListWithName = userService.findNameOfFaculty(multipleFacultyList);
									logger.info("multipleFacultyList----------> " + multipleFacultyList.toString());
									allFaculty_Id = "";
									for (User user : facultyListWithName) {
										allFaculty_Id += user.getUsername() + " , ";
									}
									allFaculty_Id = allFaculty_Id.substring(0, allFaculty_Id.length() - 3);
									logger.info("allFaculty_Id_After----------> " + allFaculty_Id);
									previousMarkedList.get(t).setPresentFacultyId(allFaculty_Id);
								}
							}

							StudentCourseAttendance sca_failed = new StudentCourseAttendance();
							sca_failed.setFailStatus("Fail_MF_AM");
							;
							previousMarkedList.add(sca_failed);
							String myResp = new Gson().toJson(previousMarkedList);
							logger.info("myResp--------->" + myResp);
							myResp = encryptResponseBody(myResp);
							return myResp;

						}
					} else {
						// no has marked this lecture till now so any faculty can mark attendance
						// normal flow
						if (sca_count == 0) {
							studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList);
						} else {
							String courseNameToSend = studentCourseAttendanceService
									.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
							String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
									+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
									+ ") has already been sent to SAP";
							String playerid = studentCourseAttendanceService
									.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
							logger.info("playerid--------->" + playerid);
							if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
								String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
								logger.info("responseNotifyFaculty--------->" + response);
							}

						}

					}

				} else {
					logger.info("allFacultyId2--------->" + allFacultyId.toString());
					// there is single faculty available to mark attendance, so no need to verify
					// previous marked attendance
					// normal flow
					if (sca_count == 0) {
						studentCourseAttendanceService.upsertBatchByApp(studentCourseAttdList);
					} else {
						String courseNameToSend = studentCourseAttendanceService
								.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
						String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
								+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
								+ ") has already been sent to SAP";
						String playerid = studentCourseAttendanceService
								.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
						logger.info("playerid--------->" + playerid);
						if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
							String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
							logger.info("responseNotifyFaculty--------->" + response);
						}

					}
				}

				String json = "{\"Status\":\"Success\"}";

				json = encryptResponseBody(json);
				return json;

			} catch (Exception e) {
				logger.error("Exception", e);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;
			}
		}

	}

	/*
	 * @RequestMapping(value = { "/updateStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * updateStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = studentCourseAttd.getCourseId();
	 * 
	 * if (courseIdCheck.contains(" , ")) { try { String[] courseIdCheckStrings =
	 * courseIdCheck.split(" , ");
	 * 
	 * for (int i = 0; i < courseIdCheckStrings.length; i++) { String
	 * spilittedCourseId = courseIdCheckStrings[i]; Map<String, String> absentMap =
	 * new HashMap<String, String>(); List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long .parseLong(spilittedCourseId));
	 * logger.info("flag--------->" + studentCourseAttd.getFlag()); String courseId
	 * = spilittedCourseId; String eventId = courseId.substring(0, 8); String
	 * programId = courseId.substring(8); String username = "", acadSession = "",
	 * acadYear = "", startTime = "", endTime = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>();
	 * 
	 * String absentStudentMapString = studentCourseAttd .getCourseStudentListMap();
	 * absentMap = splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap .get(spilittedCourseId);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd"); Date date
	 * = new Date();
	 * 
	 * startTime = dateFormat.format(date) + " " + studentCourseAttd.getStartTime();
	 * endTime = dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * 
	 * acadYear = u.getEnrollmentYear(); sca.setAcadYear(u.getEnrollmentYear());
	 * sca.setOrganization(app); sca.setActive("Y"); sca.setStartTime(startTime);
	 * sca.setEndTime(endTime);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) { // old
	 * // List<String> absentUsers = //
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*"));
	 * 
	 * List<String> absentUsers = Arrays.asList(studentCourseAttd
	 * .getListofAbsStud().substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*"));
	 * 
	 * 
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays .asList(ListofAbsentStudent .substring( 1,
	 * ListofAbsentStudent .length() - 1) .split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent .split("\\s*,\\s*")); }
	 * logger.info("----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService .updateBatch(studentCourseAttdList);
	 * 
	 * StudentCourseAttendance stca = studentCourseAttendanceService
	 * .getAllPresentRecord(courseId, startTime, endTime,
	 * studentCourseAttd.getFacultyId());
	 * 
	 * if (ListofAbsentStudent.equals(null) || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("N", stca.getId());
	 * 
	 * } else {
	 * 
	 * // insert
	 * 
	 * StudentCourseAttendance sca1 = new StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd .getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttendanceService.insert(sca1);
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}";
	 * 
	 * }
	 * 
	 * } else { try {
	 * 
	 * List<Course> StudentList = courseService .findStudentsByCourseIdForApp(Long
	 * .parseLong(studentCourseAttd.getCourseId())); logger.info("flag--------->" +
	 * studentCourseAttd.getFlag()); String courseId =
	 * studentCourseAttd.getCourseId(); String eventId = courseId.substring(0, 8);
	 * String programId = courseId.substring(8); String username = "", acadSession =
	 * "", acadYear = "", startTime = "", endTime = "";
	 * List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * startTime = dateFormat.format(date) + " " + studentCourseAttd.getStartTime();
	 * endTime = dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag());
	 * 
	 * acadSession = u.getAcadSession(); sca.setAcadSession(u.getAcadSession());
	 * 
	 * acadYear = u.getEnrollmentYear(); sca.setAcadYear(u.getEnrollmentYear());
	 * sca.setOrganization(app); sca.setActive("Y"); sca.setStartTime(startTime);
	 * sca.setEndTime(endTime);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { // old // List<String>
	 * absentUsers = //
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*"));
	 * 
	 * List<String> absentUsers = Arrays.asList(studentCourseAttd
	 * .getListofAbsStud().substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*"));
	 * 
	 * 
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (studentCourseAttd.getListofAbsStud() .startsWith("[") &&
	 * studentCourseAttd.getListofAbsStud() .endsWith("]")) { absentUsers =
	 * Arrays.asList(studentCourseAttd .getListofAbsStud() .substring( 1,
	 * studentCourseAttd .getListofAbsStud() .length() - 1) .split(",\\s*")); } else
	 * { absentUsers = Arrays.asList(studentCourseAttd
	 * .getListofAbsStud().split("\\s*,\\s*")); } logger.info("----->" +
	 * absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService .updateBatch(studentCourseAttdList);
	 * 
	 * StudentCourseAttendance stca = studentCourseAttendanceService
	 * .getAllPresentRecord(courseId, startTime, endTime,
	 * studentCourseAttd.getFacultyId());
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("N", stca.getId());
	 * 
	 * } else {
	 * 
	 * // insert
	 * 
	 * StudentCourseAttendance sca1 = new StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttendanceService.insert(sca1);
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	/*
	 * @RequestMapping(value = { "/updateStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * updateStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = studentCourseAttd.getCourseId(); Map<String,
	 * String> courseAcadYearMap = new HashMap<String, String>();
	 * 
	 * if (courseIdCheck.contains(" , ")) { try { String[] courseIdCheckStrings =
	 * courseIdCheck.split(" , ");
	 * 
	 * for (int i = 0; i < courseIdCheckStrings.length; i++) { String
	 * spilittedCourseId = courseIdCheckStrings[i]; Map<String, String> absentMap =
	 * new HashMap<String, String>(); List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(spilittedCourseId));
	 * logger.info("flag--------->" + studentCourseAttd.getFlag()); String courseId
	 * = spilittedCourseId; String eventId = courseId.substring(0, 8); String
	 * programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); // String sapAcadYear = //
	 * timetableDAO.getAcadYearFromSapMaster("51709397", // "50474742");
	 * courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); String absentStudentMapString =
	 * studentCourseAttd.getCourseStudentListMap(); absentMap =
	 * splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap.get(spilittedCourseId);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (update multiple courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (update multiple courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId()); //
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag()); sca.setOrganization(app);
	 * sca.setActive("Y"); sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear);
	 * 
	 * // acadSession = u.getAcadSession(); //
	 * sca.setAcadSession(u.getAcadSession()); // acadYear = u.getEnrollmentYear();
	 * // sca.setAcadYear(u.getEnrollmentYear());
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
	 * 
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays.asList(ListofAbsentStudent .substring(1,
	 * ListofAbsentStudent.length() - 1).split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent.split("\\s*,\\s*")); } logger.info("----->"
	 * + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.updateBatch(studentCourseAttdList);
	 * 
	 * StudentCourseAttendance stca =
	 * studentCourseAttendanceService.getAllPresentRecord(courseId, startTime,
	 * endTime, studentCourseAttd.getFacultyId());
	 * 
	 * if (ListofAbsentStudent.equals(null) || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("N", stca.getId());
	 * 
	 * } else {
	 * 
	 * // insert
	 * 
	 * StudentCourseAttendance sca1 = new StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttendanceService.insert(sca1);
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}";
	 * 
	 * }
	 * 
	 * } else { try {
	 * 
	 * List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttd.getCourseId())
	 * ); logger.info("flag--------->" + studentCourseAttd.getFlag()); String
	 * courseId = studentCourseAttd.getCourseId(); String eventId =
	 * courseId.substring(0, 8); String programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); // String sapAcadYear = //
	 * timetableDAO.getAcadYearFromSapMaster("51709397", // "50474742");
	 * courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); logger.info("----->" +
	 * studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (update single courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (update single courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag()); sca.setOrganization(app);
	 * sca.setActive("Y"); sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * // acadSession = u.getAcadSession(); //
	 * sca.setAcadSession(u.getAcadSession()); // // acadYear =
	 * u.getEnrollmentYear(); // sca.setAcadYear(u.getEnrollmentYear());
	 * 
	 * Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) { // old // List<String>
	 * absentUsers = //
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*"));
	 * 
	 * List<String> absentUsers = Arrays.asList(studentCourseAttd
	 * .getListofAbsStud().substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*"));
	 * 
	 * 
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (studentCourseAttd.getListofAbsStud().startsWith("[") &&
	 * studentCourseAttd.getListofAbsStud().endsWith("]")) { absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud() .substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*")); } else {
	 * absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*")); }
	 * logger.info("----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * studentCourseAttendanceService.updateBatch(studentCourseAttdList);
	 * 
	 * StudentCourseAttendance stca =
	 * studentCourseAttendanceService.getAllPresentRecord(courseId, startTime,
	 * endTime, studentCourseAttd.getFacultyId());
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("N", stca.getId());
	 * 
	 * } else {
	 * 
	 * // insert
	 * 
	 * StudentCourseAttendance sca1 = new StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * studentCourseAttendanceService.insert(sca1);
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * if (stca != null) {
	 * 
	 * // update studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	/*
	 * // 08-05-2020 shubham
	 * 
	 * @RequestMapping(value = { "/updateStudentAttendanceForAndroidApp" }, method =
	 * { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * updateStudentAttendanceForAndroidApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttd, HttpServletResponse
	 * resp) { String courseIdCheck = studentCourseAttd.getCourseId(); Map<String,
	 * String> courseAcadYearMap = new HashMap<String, String>();
	 * 
	 * if (courseIdCheck.contains(" , ")) { try { String[] courseIdCheckStrings =
	 * courseIdCheck.split(" , ");
	 * 
	 * for (int i = 0; i < courseIdCheckStrings.length; i++) { boolean insertFlag =
	 * false; String spilittedCourseId = courseIdCheckStrings[i]; Map<String,
	 * String> absentMap = new HashMap<String, String>(); List<Course> StudentList =
	 * courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(spilittedCourseId));
	 * logger.info("flag--------->" + studentCourseAttd.getFlag()); String courseId
	 * = spilittedCourseId; String eventId = courseId.substring(0, 8); String
	 * programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId); // String sapAcadYear = //
	 * timetableDAO.getAcadYearFromSapMaster("51709397", // "50474742");
	 * courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); String absentStudentMapString =
	 * studentCourseAttd.getCourseStudentListMap(); absentMap =
	 * splitToMap(absentStudentMapString);
	 * 
	 * String ListofAbsentStudent = absentMap.get(spilittedCourseId);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (update multiple courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (update multiple courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(spilittedCourseId); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag()); sca.setOrganization(app);
	 * sca.setActive("Y"); sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {
	 * 
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
	 * absentUsers = Arrays.asList(ListofAbsentStudent .substring(1,
	 * ListofAbsentStudent.length() - 1).split(",\\s*")); } else { absentUsers =
	 * Arrays.asList(ListofAbsentStudent.split("\\s*,\\s*")); } logger.info("----->"
	 * + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * }
	 * 
	 * int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(
	 * spilittedCourseId, startTime, endTime, studentCourseAttd.getFacultyId());
	 * logger.info("sca_count--------->" + sca_count); if (sca_count == 0) {
	 * logger.info("studentCourseAttdList_update--------->" +
	 * studentCourseAttdList.toString());
	 * logger.info("studentCourseAttdList_update_json--------->" + new
	 * Gson().toJson(studentCourseAttdList));
	 * studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList); }
	 * else { insertFlag = true; String courseNameToSend =
	 * studentCourseAttendanceService .getCourseNameFromCourseId(spilittedCourseId);
	 * String insertFailedFlag =
	 * "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
	 * + startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend +
	 * ") has already been sent to SAP"; String playerid =
	 * studentCourseAttendanceService
	 * .getPlayerIdForFaculty(studentCourseAttd.getFacultyId());
	 * logger.info("playerid--------->" + playerid); String response =
	 * notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
	 * logger.info("responseNotifyFaculty--------->" + response);
	 * 
	 * }
	 * 
	 * StudentCourseAttendance stca =
	 * studentCourseAttendanceService.getAllPresentRecord(courseId, startTime,
	 * endTime, studentCourseAttd.getFacultyId());
	 * 
	 * if (ListofAbsentStudent.equals(null) || ListofAbsentStudent.equals("") ||
	 * ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {
	 * 
	 * if (stca != null) {
	 * 
	 * // update if (!insertFlag) {
	 * studentCourseAttendanceService.updateDelFlag("N", stca.getId()); }
	 * 
	 * } else {
	 * 
	 * // insert
	 * 
	 * StudentCourseAttendance sca1 = new StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(spilittedCourseId); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * if (!insertFlag) { studentCourseAttendanceService.insertApp(sca1); }
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * if (stca != null) {
	 * 
	 * // update if (!insertFlag) {
	 * studentCourseAttendanceService.updateDelFlag("Y", stca.getId()); }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return "{\"Status\":\"Success\"}"; } catch (Exception e) {
	 * logger.error("Exception", e); return "{\"Status\":\"Fail\"}";
	 * 
	 * }
	 * 
	 * } else { try {
	 * 
	 * List<Course> StudentList = courseService
	 * .findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttd.getCourseId())
	 * ); logger.info("flag--------->" + studentCourseAttd.getFlag()); String
	 * courseId = studentCourseAttd.getCourseId(); String eventId =
	 * courseId.substring(0, 8); String programId = courseId.substring(8);
	 * 
	 * // ///////////////////get acadyear from sapmAster
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "sap_master_inc");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable);
	 * 
	 * String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId,
	 * programId);
	 * 
	 * courseAcadYearMap.put(courseId, sapAcadYear);
	 * 
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * // ///////////////////
	 * 
	 * String username = "", acadSession = "", acadYear = "", startTime = "",
	 * endTime = "", classDate = ""; List<StudentCourseAttendance>
	 * studentCourseAttdList = new ArrayList<>(); classDate =
	 * studentCourseAttd.getClassDate(); logger.info("----->" +
	 * studentCourseAttd.getListofAbsStud()); boolean insertFlag = false;
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Date date =
	 * new Date();
	 * 
	 * if (null != classDate && !classDate.equals("")) {
	 * logger.info("classDateInfo----->" +
	 * "class date Found (update single courseId)"); startTime = classDate + " " +
	 * studentCourseAttd.getStartTime(); endTime = classDate + " " +
	 * studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); } else {
	 * logger.info("classDateInfo----->" +
	 * "No class date Found (update single courseId)"); startTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getStartTime(); endTime =
	 * dateFormat.format(date) + " " + studentCourseAttd.getEndTime();
	 * 
	 * logger.info("startTime----->" + startTime); logger.info("endTime----->" +
	 * endTime); logger.info("classDate----->" + classDate); }
	 * 
	 * for (Course cr : StudentList) { User u =
	 * userService.findByUserName(cr.getUsername());
	 * 
	 * StudentCourseAttendance sca = new StudentCourseAttendance();
	 * 
	 * sca.setCourseId(studentCourseAttd.getCourseId()); sca.setEventId(eventId);
	 * sca.setProgramId(programId);
	 * 
	 * username = u.getUsername(); sca.setUsername(u.getUsername());
	 * sca.setRollNo(u.getRollNo());
	 * sca.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca.setFlag(studentCourseAttd.getFlag()); sca.setOrganization(app);
	 * sca.setActive("Y"); sca.setStartTime(startTime); sca.setEndTime(endTime);
	 * 
	 * Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
	 * sca.setAcadSession(c.getAcadSession()); acadSession = c.getAcadSession();
	 * acadYear = courseAcadYearMap.get(sca.getCourseId());
	 * sca.setAcadYear(acadYear);
	 * 
	 * logger.info("----->" + studentCourseAttd.getListofAbsStud());
	 * 
	 * if (null != studentCourseAttd.getListofAbsStud() ||
	 * !studentCourseAttd.getListofAbsStud().equals("")) {
	 * 
	 * List<String> absentUsers = new ArrayList<String>(); if
	 * (studentCourseAttd.getListofAbsStud().startsWith("[") &&
	 * studentCourseAttd.getListofAbsStud().endsWith("]")) { absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud() .substring(1,
	 * studentCourseAttd.getListofAbsStud().length() - 1).split(",\\s*")); } else {
	 * absentUsers =
	 * Arrays.asList(studentCourseAttd.getListofAbsStud().split("\\s*,\\s*")); }
	 * logger.info("----->" + absentUsers);
	 * 
	 * if (absentUsers.contains(u.getUsername())) {
	 * 
	 * sca.setStatus("Absent");
	 * 
	 * } else { sca.setStatus("Present"); } } else { sca.setStatus("Present"); }
	 * studentCourseAttdList.add(sca);
	 * 
	 * } int sca_count =
	 * studentCourseAttendanceService.getStudentDataCountSentToSap(
	 * studentCourseAttd.getCourseId(), startTime, endTime,
	 * studentCourseAttd.getFacultyId()); logger.info("sca_count--------->" +
	 * sca_count); if (sca_count == 0) {
	 * logger.info("studentCourseAttdList_update--------->" +
	 * studentCourseAttdList.toString()); logger.info(
	 * "studentCourseAttdList_update_json--------->" + new
	 * Gson().toJson(studentCourseAttdList));
	 * studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList); }
	 * else { insertFlag = true; String courseNameToSend =
	 * studentCourseAttendanceService
	 * .getCourseNameFromCourseId(studentCourseAttd.getCourseId()); String
	 * insertFailedFlag =
	 * "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
	 * + startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend +
	 * ") has already been sent to SAP"; String playerid =
	 * studentCourseAttendanceService
	 * .getPlayerIdForFaculty(studentCourseAttd.getFacultyId());
	 * logger.info("playerid--------->" + playerid); String response =
	 * notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
	 * logger.info("responseNotifyFaculty--------->" + response);
	 * 
	 * }
	 * 
	 * StudentCourseAttendance stca =
	 * studentCourseAttendanceService.getAllPresentRecord(courseId, startTime,
	 * endTime, studentCourseAttd.getFacultyId());
	 * 
	 * if (studentCourseAttd.getListofAbsStud().equals(null) ||
	 * studentCourseAttd.getListofAbsStud().equals("") ||
	 * studentCourseAttd.getListofAbsStud().equals("[]")) {
	 * 
	 * if (stca != null) {
	 * 
	 * // update if (!insertFlag) {
	 * studentCourseAttendanceService.updateDelFlag("N", stca.getId()); }
	 * 
	 * } else {
	 * 
	 * // insert
	 * 
	 * StudentCourseAttendance sca1 = new StudentCourseAttendance();
	 * 
	 * sca1.setCourseId(studentCourseAttd.getCourseId()); sca1.setEventId(eventId);
	 * sca1.setProgramId(programId);
	 * 
	 * sca1.setFacultyId(studentCourseAttd.getFacultyId());
	 * sca1.setCreatedBy(studentCourseAttd.getFacultyId());
	 * sca1.setLastModifiedBy(studentCourseAttd.getFacultyId());
	 * sca1.setNoOfLec(studentCourseAttd.getNoOfLec());
	 * sca1.setFlag(studentCourseAttd.getFlag());
	 * 
	 * sca1.setAcadSession(acadSession); sca1.setAcadYear(acadYear);
	 * sca1.setOrganization(app); sca1.setStartTime(startTime);
	 * sca1.setEndTime(endTime); sca1.setActive("Y"); sca1.setDelFlag("N");
	 * sca1.setStatus("Present");
	 * 
	 * if (!insertFlag) { studentCourseAttendanceService.insertApp(sca1); }
	 * 
	 * }
	 * 
	 * } else {
	 * 
	 * if (stca != null) {
	 * 
	 * // update if (!insertFlag) {
	 * studentCourseAttendanceService.updateDelFlag("Y", stca.getId()); }
	 * 
	 * }
	 * 
	 * } logger.info("Status_Reason---------> success");
	 * 
	 * return "{\"Status\":\"Success\"}";
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); return
	 * "{\"Status\":\"Fail\"}";
	 * 
	 * } }
	 * 
	 * }
	 */

	@RequestMapping(value = { "/updateStudentAttendanceForAndroidApp" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateStudentAttendanceForAndroidApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		studentCourseAttendance = (StudentCourseAttendance) decryptRequestBody(
				studentCourseAttendance.getEncrypted_key(), "StudentCourseAttendance");
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String courseIdCheck = studentCourseAttendance.getCourseId();
		String presentFacultyId = studentCourseAttendance.getPresentFacultyId();
		logger.info("presentFacultyId_Before----->" + presentFacultyId);
		if (presentFacultyId.contains("(")) {
			presentFacultyId = getSapIdOnly(presentFacultyId);
			logger.info("presentFacultyId_After----->" + presentFacultyId);
		}

		boolean multipleFacCheck = true;
		if (presentFacultyId.equalsIgnoreCase("NA")) {
			multipleFacCheck = false;
			presentFacultyId = null;
		}
		String commaSeparatedFacultyId = studentCourseAttendance.getAllFacultyId();
		logger.info("commaSeparatedFacultyId_Before----->" + commaSeparatedFacultyId);
		if (commaSeparatedFacultyId.contains("(")) {
			commaSeparatedFacultyId = getSapIdOnly(commaSeparatedFacultyId);
			logger.info("commaSeparatedFacultyId_After----->" + commaSeparatedFacultyId);
		}
		List<Long> allFacultyId = new ArrayList<Long>();
		if (commaSeparatedFacultyId.contains(",")) {
			String[] multipleAllFacultyId = commaSeparatedFacultyId.replace(" ", "").split(",");
			for (int m = 0; m < multipleAllFacultyId.length; m++) {
				allFacultyId.add(Long.parseLong(multipleAllFacultyId[m]));
			}
		} else {
			allFacultyId.add(Long.parseLong(commaSeparatedFacultyId));
		}

		Map<String, String> courseAcadYearMap = new HashMap<String, String>();

		if (courseIdCheck.contains(" , ")) {
			try {
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");

				for (int i = 0; i < courseIdCheckStrings.length; i++) {
					boolean insertFlag = false;
					String spilittedCourseId = courseIdCheckStrings[i];
					Map<String, String> absentMap = new HashMap<String, String>();
					List<Course> StudentList = courseService
							.findStudentsByCourseIdForApp(Long.parseLong(spilittedCourseId));
					logger.info("flag--------->" + studentCourseAttendance.getFlag());
					String courseId = spilittedCourseId;
					String eventId = courseId.substring(0, 8);
					String programId = courseId.substring(8);

					// ///////////////////get acadyear from sapmAster

					MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

					DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

							.createDefaultConnectionByDS(defaultUrl, defaultUsername,

									defaultPassword);

					DriverManagerDataSource dataSourceTimetable = multipleDBConnection

							.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
									"sap_master_inc");

					timetableDAO.setDS(dataSourceTimetable);

					String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId, programId);
					// String sapAcadYear =
					// timetableDAO.getAcadYearFromSapMaster("51709397",
					// "50474742");
					courseAcadYearMap.put(courseId, sapAcadYear);

					timetableDAO.setDS(dataSourceDefaultLms);

					// ///////////////////

					String username = "", acadSession = "", acadYear = "", startTime = "", endTime = "", classDate = "";
					List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
					classDate = studentCourseAttendance.getClassDate();
					String absentStudentMapString = studentCourseAttendance.getCourseStudentListMap();
					absentMap = splitToMap(absentStudentMapString);

					String ListofAbsentStudent = absentMap.get(spilittedCourseId);

					logger.info("----->" + studentCourseAttendance.getListofAbsStud());

					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date();

					if (null != classDate && !classDate.equals("")) {
						logger.info("classDateInfo----->" + "class date Found (update multiple courseId)");
						startTime = classDate + " " + studentCourseAttendance.getStartTime();
						endTime = classDate + " " + studentCourseAttendance.getEndTime();

						logger.info("startTime----->" + startTime);
						logger.info("endTime----->" + endTime);
						logger.info("classDate----->" + classDate);
					} else {
						logger.info("classDateInfo----->" + "No class date Found (update multiple courseId)");
						startTime = dateFormat.format(date) + " " + studentCourseAttendance.getStartTime();
						endTime = dateFormat.format(date) + " " + studentCourseAttendance.getEndTime();

						logger.info("startTime----->" + startTime);
						logger.info("endTime----->" + endTime);
						logger.info("classDate----->" + classDate);
					}

					for (Course cr : StudentList) {
						User u = userService.findByUserName(cr.getUsername());

						StudentCourseAttendance sca = new StudentCourseAttendance();

						sca.setCourseId(spilittedCourseId);
						sca.setEventId(eventId);
						sca.setProgramId(programId);
						sca.setPresentFacultyId(presentFacultyId);
						username = u.getUsername();
						sca.setUsername(u.getUsername());
						sca.setRollNo(u.getRollNo());
						sca.setFacultyId(studentCourseAttendance.getFacultyId());
						sca.setLastModifiedBy(studentCourseAttendance.getFacultyId());
						sca.setPresentFacultyId(presentFacultyId);
						sca.setNoOfLec(studentCourseAttendance.getNoOfLec());
						sca.setFlag(studentCourseAttendance.getFlag());
						sca.setOrganization(app);
						sca.setActive("Y");
						sca.setStartTime(startTime);
						sca.setEndTime(endTime);

						Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
						sca.setAcadSession(c.getAcadSession());
						acadSession = c.getAcadSession();
						acadYear = courseAcadYearMap.get(sca.getCourseId());
						sca.setAcadYear(acadYear);

						logger.info("----->" + studentCourseAttendance.getListofAbsStud());

						if (null != ListofAbsentStudent || !ListofAbsentStudent.equals("")) {

							List<String> absentUsers = new ArrayList<String>();
							if (ListofAbsentStudent.startsWith("[") && ListofAbsentStudent.endsWith("]")) {
								absentUsers = Arrays.asList(ListofAbsentStudent
										.substring(1, ListofAbsentStudent.length() - 1).split(",\\s*"));
							} else {
								absentUsers = Arrays.asList(ListofAbsentStudent.split("\\s*,\\s*"));
							}
							logger.info("----->" + absentUsers);

							if (absentUsers.contains(u.getUsername())) {

								sca.setStatus("Absent");

							} else {
								sca.setStatus("Present");
							}
						} else {
							sca.setStatus("Present");
						}
						studentCourseAttdList.add(sca);

					}

					int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(spilittedCourseId,
							startTime, endTime, studentCourseAttendance.getFacultyId());
					logger.info("sca_count--------->" + sca_count);

					if (multipleFacCheck && allFacultyId.toString().contains(",")) {
						// checking if any faculty(among all faculty) has marked attendance or not
						int att_sca_count = studentCourseAttendanceService.getDataCountByAllFacultyId(spilittedCourseId,
								startTime, endTime, allFacultyId);
						if (att_sca_count > 0) {
							// checking if this faculty who is marking now has only marked earlier or not
							int fac_sca_count = studentCourseAttendanceService.getDataCountByOneFacultyId(
									spilittedCourseId, startTime, endTime, studentCourseAttendance.getFacultyId());
							if (fac_sca_count > 0) {
								// normal flow

								if (sca_count == 0) {

									logger.info("studentCourseAttdList_update--------->"
											+ studentCourseAttdList.toString());
									logger.info("studentCourseAttdList_update_json--------->"
											+ new Gson().toJson(studentCourseAttdList));
									studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList);

									StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(
											courseId, startTime, endTime, studentCourseAttendance.getFacultyId());

									if (ListofAbsentStudent.equals(null) || ListofAbsentStudent.equals("")
											|| ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {

										if (stca != null) {

											// update
											if (!insertFlag) {
												studentCourseAttendanceService.updateDelFlag("N", stca.getId());
											}

										} else {

											// insert

											StudentCourseAttendance sca1 = new StudentCourseAttendance();

											sca1.setCourseId(spilittedCourseId);
											sca1.setEventId(eventId);
											sca1.setProgramId(programId);

											sca1.setFacultyId(studentCourseAttendance.getFacultyId());
											sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
											sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
											sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
											sca1.setFlag(studentCourseAttendance.getFlag());
											sca1.setPresentFacultyId(presentFacultyId);

											sca1.setAcadSession(acadSession);
											sca1.setAcadYear(acadYear);
											sca1.setOrganization(app);
											sca1.setStartTime(startTime);
											sca1.setEndTime(endTime);
											sca1.setActive("Y");
											sca1.setDelFlag("N");
											sca1.setStatus("Present");

											if (!insertFlag) {
												studentCourseAttendanceService.insertApp(sca1);
											}

										}

									} else {

										if (stca != null) {

											// update
											if (!insertFlag) {
												studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
											}

										}

									}
								}

							} else {
								// this faculty cannot mark attendance because some other faculty has already
								// marked attendance before
								// reject this attendance
								// notify user

								String courseNameToSend = studentCourseAttendanceService
										.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
								String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
										+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
										+ ") has been rejected because some other faculty has already marked attendance";
								String playerid = studentCourseAttendanceService
										.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
								logger.info("playerid--------->" + playerid);
								if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
									String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
									logger.info("responseNotifyFaculty--------->" + response);
								}
								String myResp = "{\"Status\":\"Fail_MF_AM\"}";
								myResp = encryptResponseBody(myResp);
								return myResp;

							}
						} else {
							// no has marked this lecture till now so any faculty can mark attendance
							// normal flow

							if (sca_count == 0) {

								logger.info(
										"studentCourseAttdList_update--------->" + studentCourseAttdList.toString());
								logger.info("studentCourseAttdList_update_json--------->"
										+ new Gson().toJson(studentCourseAttdList));
								studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList);

								StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(
										courseId, startTime, endTime, studentCourseAttendance.getFacultyId());

								if (ListofAbsentStudent.equals(null) || ListofAbsentStudent.equals("")
										|| ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {

									if (stca != null) {

										// update
										if (!insertFlag) {
											studentCourseAttendanceService.updateDelFlag("N", stca.getId());
										}

									} else {

										// insert

										StudentCourseAttendance sca1 = new StudentCourseAttendance();

										sca1.setCourseId(spilittedCourseId);
										sca1.setEventId(eventId);
										sca1.setProgramId(programId);

										sca1.setFacultyId(studentCourseAttendance.getFacultyId());
										sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
										sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
										sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
										sca1.setFlag(studentCourseAttendance.getFlag());
										sca1.setPresentFacultyId(presentFacultyId);

										sca1.setAcadSession(acadSession);
										sca1.setAcadYear(acadYear);
										sca1.setOrganization(app);
										sca1.setStartTime(startTime);
										sca1.setEndTime(endTime);
										sca1.setActive("Y");
										sca1.setDelFlag("N");
										sca1.setStatus("Present");

										if (!insertFlag) {
											studentCourseAttendanceService.insertApp(sca1);
										}

									}

								} else {

									if (stca != null) {

										// update
										if (!insertFlag) {
											studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
										}

									}

								}
							}

						}

					} else {
						// there is single faculty available to mark attendance, so no need to verify
						// previous marked attendance
						// normal flow

						if (sca_count == 0) {

							logger.info("studentCourseAttdList_update--------->" + studentCourseAttdList.toString());
							logger.info("studentCourseAttdList_update_json--------->"
									+ new Gson().toJson(studentCourseAttdList));
							studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList);

							StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(courseId,
									startTime, endTime, studentCourseAttendance.getFacultyId());

							if (ListofAbsentStudent.equals(null) || ListofAbsentStudent.equals("")
									|| ListofAbsentStudent.equals("[]") || ListofAbsentStudent.equals("[]]")) {

								if (stca != null) {

									// update
									if (!insertFlag) {
										studentCourseAttendanceService.updateDelFlag("N", stca.getId());
									}

								} else {

									// insert

									StudentCourseAttendance sca1 = new StudentCourseAttendance();

									sca1.setCourseId(spilittedCourseId);
									sca1.setEventId(eventId);
									sca1.setProgramId(programId);

									sca1.setFacultyId(studentCourseAttendance.getFacultyId());
									sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
									sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
									sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
									sca1.setFlag(studentCourseAttendance.getFlag());
									sca1.setPresentFacultyId(presentFacultyId);

									sca1.setAcadSession(acadSession);
									sca1.setAcadYear(acadYear);
									sca1.setOrganization(app);
									sca1.setStartTime(startTime);
									sca1.setEndTime(endTime);
									sca1.setActive("Y");
									sca1.setDelFlag("N");
									sca1.setStatus("Present");

									if (!insertFlag) {
										studentCourseAttendanceService.insertApp(sca1);
									}

								}

							} else {

								if (stca != null) {

									// update
									if (!insertFlag) {
										studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
									}

								}

							}
						}
					}

				}

				String json = "{\"Status\":\"Success\"}";

				json = encryptResponseBody(json);
				return json;
			} catch (Exception e) {
				logger.error("Exception", e);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;

			}

		} else {
			try {

				List<Course> StudentList = courseService
						.findStudentsByCourseIdForApp(Long.parseLong(studentCourseAttendance.getCourseId()));
				logger.info("flag--------->" + studentCourseAttendance.getFlag());
				String courseId = studentCourseAttendance.getCourseId();
				String eventId = courseId.substring(0, 8);
				String programId = courseId.substring(8);

				// ///////////////////get acadyear from sapmAster

				MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

				DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

						.createDefaultConnectionByDS(defaultUrl, defaultUsername,

								defaultPassword);

				DriverManagerDataSource dataSourceTimetable = multipleDBConnection

						.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
								"sap_master_inc");

				timetableDAO.setDS(dataSourceTimetable);

				String sapAcadYear = timetableDAO.getAcadYearFromSapMaster(eventId, programId);

				courseAcadYearMap.put(courseId, sapAcadYear);

				timetableDAO.setDS(dataSourceDefaultLms);

				// ///////////////////

				String username = "", acadSession = "", acadYear = "", startTime = "", endTime = "", classDate = "";
				List<StudentCourseAttendance> studentCourseAttdList = new ArrayList<>();
				classDate = studentCourseAttendance.getClassDate();
				logger.info("----->" + studentCourseAttendance.getListofAbsStud());
				boolean insertFlag = false;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();

				if (null != classDate && !classDate.equals("")) {
					logger.info("classDateInfo----->" + "class date Found (update single courseId)");
					startTime = classDate + " " + studentCourseAttendance.getStartTime();
					endTime = classDate + " " + studentCourseAttendance.getEndTime();

					logger.info("startTime----->" + startTime);
					logger.info("endTime----->" + endTime);
					logger.info("classDate----->" + classDate);
				} else {
					logger.info("classDateInfo----->" + "No class date Found (update single courseId)");
					startTime = dateFormat.format(date) + " " + studentCourseAttendance.getStartTime();
					endTime = dateFormat.format(date) + " " + studentCourseAttendance.getEndTime();

					logger.info("startTime----->" + startTime);
					logger.info("endTime----->" + endTime);
					logger.info("classDate----->" + classDate);
				}

				for (Course cr : StudentList) {
					User u = userService.findByUserName(cr.getUsername());

					StudentCourseAttendance sca = new StudentCourseAttendance();

					sca.setCourseId(studentCourseAttendance.getCourseId());
					sca.setEventId(eventId);
					sca.setProgramId(programId);

					username = u.getUsername();
					sca.setUsername(u.getUsername());
					sca.setRollNo(u.getRollNo());
					sca.setFacultyId(studentCourseAttendance.getFacultyId());
					sca.setCreatedBy(studentCourseAttendance.getFacultyId());
					sca.setLastModifiedBy(studentCourseAttendance.getFacultyId());
					sca.setNoOfLec(studentCourseAttendance.getNoOfLec());
					sca.setFlag(studentCourseAttendance.getFlag());
					sca.setPresentFacultyId(presentFacultyId);
					sca.setOrganization(app);
					sca.setPresentFacultyId(presentFacultyId);
					sca.setPresentFacultyId(presentFacultyId);
					sca.setActive("Y");
					sca.setStartTime(startTime);
					sca.setEndTime(endTime);

					Course c = courseService.findByID(Long.valueOf(sca.getCourseId()));
					sca.setAcadSession(c.getAcadSession());
					acadSession = c.getAcadSession();
					acadYear = courseAcadYearMap.get(sca.getCourseId());
					sca.setAcadYear(acadYear);

					logger.info("----->" + studentCourseAttendance.getListofAbsStud());

					if (null != studentCourseAttendance.getListofAbsStud()
							|| !studentCourseAttendance.getListofAbsStud().equals("")) {

						List<String> absentUsers = new ArrayList<String>();
						if (studentCourseAttendance.getListofAbsStud().startsWith("[")
								&& studentCourseAttendance.getListofAbsStud().endsWith("]")) {
							absentUsers = Arrays.asList(studentCourseAttendance.getListofAbsStud()
									.substring(1, studentCourseAttendance.getListofAbsStud().length() - 1)
									.split(",\\s*"));
						} else {
							absentUsers = Arrays.asList(studentCourseAttendance.getListofAbsStud().split("\\s*,\\s*"));
						}
						logger.info("----->" + absentUsers);

						if (absentUsers.contains(u.getUsername())) {

							sca.setStatus("Absent");

						} else {
							sca.setStatus("Present");
						}
					} else {
						sca.setStatus("Present");
					}
					studentCourseAttdList.add(sca);

				}
				int sca_count = studentCourseAttendanceService.getStudentDataCountSentToSap(
						studentCourseAttendance.getCourseId(), startTime, endTime,
						studentCourseAttendance.getFacultyId());
				logger.info("sca_count--------->" + sca_count);

				if (multipleFacCheck && allFacultyId.toString().contains(",")) {
					logger.info("allFacultyId1--------->" + allFacultyId.toString());
					// checking if any faculty(among all faculty) has marked attendance or not
					int att_sca_count = studentCourseAttendanceService.getDataCountByAllFacultyId(
							studentCourseAttendance.getCourseId(), startTime, endTime, allFacultyId);
					logger.info("att_sca_count--------->" + att_sca_count);
					if (att_sca_count > 0) {

						// checking if this faculty who is marking now has only marked earlier or not
						int fac_sca_count = studentCourseAttendanceService.getDataCountByOneFacultyId(
								studentCourseAttendance.getCourseId(), startTime, endTime,
								studentCourseAttendance.getFacultyId());
						logger.info("fac_sca_count--------->" + fac_sca_count);
						if (fac_sca_count > 0) {

							// normal flow

							if (sca_count == 0) {
								logger.info(
										"studentCourseAttdList_update--------->" + studentCourseAttdList.toString());
								logger.info("studentCourseAttdList_update_json--------->"
										+ new Gson().toJson(studentCourseAttdList));
								studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList);

								StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(
										courseId, startTime, endTime, studentCourseAttendance.getFacultyId());

								if (studentCourseAttendance.getListofAbsStud().equals(null)
										|| studentCourseAttendance.getListofAbsStud().equals("")
										|| studentCourseAttendance.getListofAbsStud().equals("[]")) {

									if (stca != null) {

										// update
										if (!insertFlag) {
											studentCourseAttendanceService.updateDelFlag("N", stca.getId());
										}

									} else {

										// insert

										StudentCourseAttendance sca1 = new StudentCourseAttendance();

										sca1.setCourseId(studentCourseAttendance.getCourseId());
										sca1.setEventId(eventId);
										sca1.setProgramId(programId);

										sca1.setFacultyId(studentCourseAttendance.getFacultyId());
										sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
										sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
										sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
										sca1.setFlag(studentCourseAttendance.getFlag());
										sca1.setPresentFacultyId(presentFacultyId);

										sca1.setAcadSession(acadSession);
										sca1.setAcadYear(acadYear);
										sca1.setOrganization(app);
										sca1.setStartTime(startTime);
										sca1.setEndTime(endTime);
										sca1.setActive("Y");
										sca1.setDelFlag("N");
										sca1.setStatus("Present");

										if (!insertFlag) {
											studentCourseAttendanceService.insertApp(sca1);
										}

									}

								} else {

									if (stca != null) {

										// update
										if (!insertFlag) {
											studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
										}

									}

								}

							} else {
								insertFlag = true;
								String courseNameToSend = studentCourseAttendanceService
										.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
								String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
										+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
										+ ") has already been sent to SAP";
								String playerid = studentCourseAttendanceService
										.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
								logger.info("playerid--------->" + playerid);
								if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
									String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
									logger.info("responseNotifyFaculty--------->" + response);
								}

							}

						} else {
							// this faculty cannot mark attendance because some other faculty has already
							// marked attendance before
							// reject this attendance
							// notify user
							String courseNameToSend = studentCourseAttendanceService
									.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
							String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
									+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
									+ ") has been rejected because some other faculty has already marked attendance";
							String playerid = studentCourseAttendanceService
									.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
							logger.info("playerid--------->" + playerid);
							if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
								String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
								logger.info("responseNotifyFaculty--------->" + response);
							}
							String myResp = "{\"Status\":\"Fail_MF_AM\"}";
							myResp = encryptResponseBody(myResp);
							return myResp;

						}
					} else {
						// no has marked this lecture till now so any faculty can mark attendance
						// normal flow

						if (sca_count == 0) {
							logger.info("studentCourseAttdList_update--------->" + studentCourseAttdList.toString());
							logger.info("studentCourseAttdList_update_json--------->"
									+ new Gson().toJson(studentCourseAttdList));
							studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList);

							StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(courseId,
									startTime, endTime, studentCourseAttendance.getFacultyId());

							if (studentCourseAttendance.getListofAbsStud().equals(null)
									|| studentCourseAttendance.getListofAbsStud().equals("")
									|| studentCourseAttendance.getListofAbsStud().equals("[]")) {

								if (stca != null) {

									// update
									if (!insertFlag) {
										studentCourseAttendanceService.updateDelFlag("N", stca.getId());
									}

								} else {

									// insert

									StudentCourseAttendance sca1 = new StudentCourseAttendance();

									sca1.setCourseId(studentCourseAttendance.getCourseId());
									sca1.setEventId(eventId);
									sca1.setProgramId(programId);

									sca1.setFacultyId(studentCourseAttendance.getFacultyId());
									sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
									sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
									sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
									sca1.setFlag(studentCourseAttendance.getFlag());
									sca1.setPresentFacultyId(presentFacultyId);

									sca1.setAcadSession(acadSession);
									sca1.setAcadYear(acadYear);
									sca1.setOrganization(app);
									sca1.setStartTime(startTime);
									sca1.setEndTime(endTime);
									sca1.setActive("Y");
									sca1.setDelFlag("N");
									sca1.setStatus("Present");

									if (!insertFlag) {
										studentCourseAttendanceService.insertApp(sca1);
									}

								}

							} else {

								if (stca != null) {

									// update
									if (!insertFlag) {
										studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
									}

								}

							}

						} else {
							insertFlag = true;
							String courseNameToSend = studentCourseAttendanceService
									.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
							String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
									+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
									+ ") has already been sent to SAP";
							String playerid = studentCourseAttendanceService
									.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
							logger.info("playerid--------->" + playerid);
							if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
								String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
								logger.info("responseNotifyFaculty--------->" + response);
							}

						}

					}

				} else {
					logger.info("allFacultyId2--------->" + allFacultyId.toString());
					// there is single faculty available to mark attendance, so no need to verify
					// previous marked attendance
					// normal flow

					if (sca_count == 0) {
						logger.info("studentCourseAttdList_update--------->" + studentCourseAttdList.toString());
						logger.info("studentCourseAttdList_update_json--------->"
								+ new Gson().toJson(studentCourseAttdList));
						studentCourseAttendanceService.updateBatchByApp(studentCourseAttdList);

						StudentCourseAttendance stca = studentCourseAttendanceService.getAllPresentRecord(courseId,
								startTime, endTime, studentCourseAttendance.getFacultyId());

						if (studentCourseAttendance.getListofAbsStud().equals(null)
								|| studentCourseAttendance.getListofAbsStud().equals("")
								|| studentCourseAttendance.getListofAbsStud().equals("[]")) {

							if (stca != null) {

								// update
								if (!insertFlag) {
									studentCourseAttendanceService.updateDelFlag("N", stca.getId());
								}

							} else {

								// insert

								StudentCourseAttendance sca1 = new StudentCourseAttendance();

								sca1.setCourseId(studentCourseAttendance.getCourseId());
								sca1.setEventId(eventId);
								sca1.setProgramId(programId);

								sca1.setFacultyId(studentCourseAttendance.getFacultyId());
								sca1.setCreatedBy(studentCourseAttendance.getFacultyId());
								sca1.setLastModifiedBy(studentCourseAttendance.getFacultyId());
								sca1.setNoOfLec(studentCourseAttendance.getNoOfLec());
								sca1.setFlag(studentCourseAttendance.getFlag());
								sca1.setPresentFacultyId(presentFacultyId);

								sca1.setAcadSession(acadSession);
								sca1.setAcadYear(acadYear);
								sca1.setOrganization(app);
								sca1.setStartTime(startTime);
								sca1.setEndTime(endTime);
								sca1.setActive("Y");
								sca1.setDelFlag("N");
								sca1.setStatus("Present");

								if (!insertFlag) {
									studentCourseAttendanceService.insertApp(sca1);
								}

							}

						} else {

							if (stca != null) {

								// update
								if (!insertFlag) {
									studentCourseAttendanceService.updateDelFlag("Y", stca.getId());
								}

							}

						}

					} else {
						insertFlag = true;
						String courseNameToSend = studentCourseAttendanceService
								.getCourseNameFromCourseId(studentCourseAttendance.getCourseId());
						String insertFailedFlag = "Attendance Submission Rejected : \n\n Attendance data for the lecture (StartTime = "
								+ startTime + ", Endtime =" + endTime + ", CourseName =" + courseNameToSend
								+ ") has already been sent to SAP";
						String playerid = studentCourseAttendanceService
								.getPlayerIdForFaculty(studentCourseAttendance.getFacultyId());
						logger.info("playerid--------->" + playerid);
						if (playerid.length() > 1 && !playerid.isEmpty() && null != playerid) {
							String response = notifyFacultyForFailedAttendanceEntry(playerid, insertFailedFlag);
							logger.info("responseNotifyFaculty--------->" + response);
						}

					}

				}

				logger.info("Status_Reason---------> success");

				String json = "{\"Status\":\"Success\"}";

				json = encryptResponseBody(json);
				return json;

			} catch (Exception e) {
				logger.error("Exception", e);
				String json = "{\"Status\":\"Fail\"}";

				json = encryptResponseBody(json);
				return json;

			}
		}

	}

	// 08-05-2020

	public String notifyFacultyForFailedAttendanceEntry(String playerId, String message) {

		String jsonResponse = "";

		if (null != playerId && null != message) {
			logger.info("playerId==============> " + playerId);
			logger.info("message==============> " + message);
			Client clientWS = null;
			ClientConfig clientConfig = null;
			WebTarget webTarget = null;
			Invocation.Builder invocationBuilder = null;
			Response response = null;

			String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\"," + "\"include_player_ids\": [\""
					+ playerId + "\"]," + "\"data\": {\"foo\": \"bar\"}," + "\"contents\": {\"en\": \"" + message
					+ "\"}" + "}";

			int responseCode;
			clientConfig = new ClientConfig();

			clientWS = ClientBuilder.newClient(clientConfig);
			webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");

			invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
					"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");

			response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

			responseCode = response.getStatus();

			logger.info("responseCode: " + responseCode);

			jsonResponse += response.readEntity(String.class);

			logger.info("jsonResponse:\n" + jsonResponse);

			return jsonResponse;
		} else {
			return "{\"jsonResponse\":\"Playerid is null\"}";
		}
	}

	@RequestMapping(value = { "/sendNotificationForAttendanceSync" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String sendNotificationForAttendanceSync(@RequestBody String playerId,
			@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), headers.get("username"));
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String jsonResponse = "";
		String playerID = "";
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(playerId);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (jsonObj.has("playerId")) {
			try {
				playerID = jsonObj.getString("playerId");
				logger.info("playerID==============> " + playerID);
				Client clientWS = null;
				ClientConfig clientConfig = null;
				WebTarget webTarget = null;
				Invocation.Builder invocationBuilder = null;
				Response response = null;

				String msg = "Data is being synced with server";

				String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\","
						+ "\"include_player_ids\": [\"" + playerID + "\"]," + "\"data\": {\"foo\": \"bar\"},"
						+ "\"contents\": {\"en\": \"" + msg + "\"}" + "}";

				int responseCode;
				clientConfig = new ClientConfig();

				clientWS = ClientBuilder.newClient(clientConfig);
				webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");

				invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
						"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");

				response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

				// get response code
				responseCode = response.getStatus();

				logger.info("responseCode: " + responseCode);

				jsonResponse += response.readEntity(String.class);

				// logger.info("resp" + jsonResponse);

				logger.info("jsonResponse:\n" + jsonResponse);

				return jsonResponse;

			} catch (JSONException e) {
				// e.printStackTrace();
				return "{\"jsonResponse\":\"Failed with Exception\"}";
			}
		} else {
			return "{\"jsonResponse\":\"Playerid is null\"}";
		}
	}

	@RequestMapping(value = { "/sendNotificationForAttendanceSyncService" }, method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String sendNotificationForAttendanceSyncService(@RequestHeader Map<String, String> headers) {
		boolean auth = isUserAuthorized(headers.get("token"), headers.get("username"));
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		List<String> facultyPlayerIdList = new ArrayList<>();

		// ////////Fetch facultyPlayerIdList /////////////

		facultyPlayerIdList = userService.findActivePlayerIdForActiveFaculties();

		// /////////////////////////////////////////////

		if (facultyPlayerIdList.size() > 0) {
			logger.info("facultyPlayerIdList.size =============> " + facultyPlayerIdList.size());
			for (int i = 0; i < facultyPlayerIdList.size(); i++) {
				try {
					String playerID = facultyPlayerIdList.get(i);
					logger.info("playerID==============> " + playerID);
					Client clientWS = null;
					ClientConfig clientConfig = null;
					WebTarget webTarget = null;
					Invocation.Builder invocationBuilder = null;
					Response response = null;

					String msg = "Data is being synced with server";

					String json = "{" + "\"app_id\": \"2d46e24e-fb33-43aa-ad6e-fde6940c28ff\","
							+ "\"include_player_ids\": [\"" + playerID + "\"]," + "\"data\": {\"foo\": \"bar\"},"
							+ "\"contents\": {\"en\": \"" + msg + "\"}" + "}";

					int responseCode;
					clientConfig = new ClientConfig();

					clientWS = ClientBuilder.newClient(clientConfig);
					webTarget = clientWS.target("https://onesignal.com/api/v1/notifications");

					invocationBuilder = webTarget.request().header(HttpHeaders.AUTHORIZATION,
							"MDI5OTI2ZTUtYzQ4Ni00MGU5LTk2YjQtMjI4NzA1OGM0ZjBl");

					response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

					// get response code
					responseCode = response.getStatus();

					logger.info("responseCode: " + responseCode);
				} catch (Exception e) {
					// e.printStackTrace();
					logger.info(
							"Attendance_Service_Status_FacultyWise_cause =============>" + facultyPlayerIdList.get(i));
					logger.info("Attendance_Service_Status_FacultyWise_Date =============>" + Utils.getInIST());
				}
			}
		} else {
			logger.info("Attendance_Service_Status_cause =============>" + "No faculty found in list");
			logger.info("Attendance_Service_Status_Date =============>" + Utils.getInIST());
		}
		return null;

	}

	public long timeDifference(String time1, String time2) {

		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		long diff = 0;
		try {
			d1 = format.parse(time1);
			d2 = format.parse(time2);
			diff = d2.getTime() - d1.getTime();
			logger.info("diff----->" + String.valueOf(diff));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}

	private static Map<String, String> splitToMap(String in) {
		logger.info("in_before=======>" + in);
		if (in.startsWith("{")) {
			in = in.replace("{", "");
		}
		if (in.endsWith("}")) {
			in = in.replace("}", "");
		}
		logger.info("in_after=======>" + in);

		String[] value = in.split("], ");

		for (int i = 0; i < value.length; i++) {
			if (i < value.length - 1) {
				value[i] = value[i] + "]";
				logger.info("value=======>" + value[i] + "]");
			} else {
				value[i] = value[i];
				logger.info("value=======>" + value[i]);
			}
		}

		Map<String, String> map = new HashMap<>();

		for (String pair : value) {
			String[] entry = pair.split("=");
			map.put(entry[0].trim().replaceAll("[\"]", ""), entry[1].trim().replaceAll("[\"]", ""));
		}
		return map;
	}

	// SupportAdmin
	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/changePasswordBySupportAdminForm", method = RequestMethod.GET)
	public String changePasswordBySupportAdminForm(@ModelAttribute("user") User user, Model m) {
		m.addAttribute("webPage", new WebPage("changePassword", "Change Password", true, false));
		return "user/changePasswordBySupportAdmin";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/changePasswordBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String changePasswordBySupportAdmin(@ModelAttribute User user, @RequestParam String username,
			RedirectAttributes redirectAttrs, Model m, Principal p) {
		try {
			User userDB = userService.findByUserName(user.getUsername());

			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl + "/changePasswordForUserBySupportAdmin?username=" + username));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			logger.info("checking response" + resp);
			if (resp.equals("Success")) {
				logger.info("Notification of Success");
				setSuccess(redirectAttrs, "Password Changed Successfully, New Password is pass@123");
			} else {
				setNote(redirectAttrs, "No User Found");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in Updating");
		}

		return "redirect:/changePasswordBySupportAdminForm";
	}
	  /* New Audit changes start */
	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/searchUserBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchUserBySupportAdmin(Principal principal, @ModelAttribute("user") User user, Model m,
			RedirectAttributes redirectAttrs, @RequestParam(required = false) String username) {

		try {
			User userDB = userService.findByUserName(user.getUsername());
			ObjectMapper mapper = new ObjectMapper();
			WebTarget webTarget = client.target(
					URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/searchUserBySupportAdmin?username=" + username));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			ObjectMapper objMapper = new ObjectMapper();
			User userList = objMapper.readValue(resp,User.class);

			if (null == userList) {
				m.addAttribute("note", "No User Found");
			}
			logger.info("userList-->"+userList);
			//String json = mapper.writeValueAsString(username);
			m.addAttribute("userList", userList);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			setError(redirectAttrs, "");
		}
		return "user/changePasswordBySupportAdmin";
	}
	  /* New Audit changes end */
//	@RequestMapping(value = "/searchUserBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
//	public String searchUserBySupportAdmin(Principal principal, @ModelAttribute("user") User user, Model m,
//			RedirectAttributes redirectAttrs, @RequestParam(required = false) String username) {
//
//		try {
//			User userDB = userService.findByUserName(user.getUsername());
//			ObjectMapper mapper = new ObjectMapper();
//			WebTarget webTarget = client.target(
//					URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/searchUserBySupportAdmin?username=" + username));
//			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
//			String resp = invocationBuilder.get(String.class);
//			ObjectMapper objMapper = new ObjectMapper();
//			List<User> userList = objMapper.readValue(resp, new TypeReference<List<User>>() {
//			});
//
//			if (userList.isEmpty()) {
//				m.addAttribute("note", "No User Found");
//			}
//			String json = mapper.writeValueAsString(username);
//			m.addAttribute("userList", userList);
//		} catch (Exception ex) {
//			logger.error(ex.getMessage(), ex);
//			setError(redirectAttrs, "");
//		}
//		return "user/changePasswordBySupportAdmin";
//	}

	/*
	 * @RequestMapping(value = "/getAttendanceDataSentToSapForApp", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * getAttendanceDataSentToSapForApp(
	 * 
	 * @RequestBody StudentCourseAttendance studentCourseAttendance,
	 * HttpServletResponse resp) { List<StudentCourseAttendance>
	 * studentCourseAttendanceList = new ArrayList<>(); String json = ""; try {
	 * String startTime = studentCourseAttendance.getStartTime(); String status =
	 * ""; status = studentCourseAttendance.getStatus();
	 * logger.info("startTime==========> " + startTime);
	 * logger.info("status==========> " + status);
	 * 
	 * studentCourseAttendanceList = studentCourseAttendanceService
	 * .getDistinctEndTime(startTime, status);
	 * 
	 * logger.info("studentCourseAttendanceList==========> " +
	 * studentCourseAttendanceList); json = new
	 * Gson().toJson(studentCourseAttendanceList); return json; } catch (Exception
	 * e) { e.printStackTrace();
	 * logger.info("studentCourseAttendanceList==========> Null");
	 * logger.info("getAttendance_Exception=============> " + e.getMessage()); json
	 * = new Gson().toJson(studentCourseAttendanceList); return json; }
	 * 
	 * 
	 * @RequestMapping(value = "/sendAttendanceDataToSapDemo", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * sendAttendanceDataToSapDemo(
	 * 
	 * @RequestBody Attendance attendance, HttpServletResponse resp) { String json
	 * ="", response ="", result=""; try { String startDate =
	 * attendance.getStartDate().replace("T"," ");
	 * logger.info("startDate==========> "+startDate); response =
	 * studentCourseAttendanceService .sendAttendanceToSAPByDateForApp(startDate);
	 * logger.info("ResponseFromSAP========>"+response);
	 * if(response.endsWith("_ErrorOccurred400")) { return
	 * "{\"Status\":\"Failed\"}"; } else { return "{\"Status\":\"Success\"}"; } }
	 * catch (Exception e) { e.printStackTrace();
	 * logger.info("sendAttendanceDataToSapDemoException=============> "
	 * +e.getMessage()); return "{\"Status\":\"Exception Occurred\"}"; } } catch
	 * (Exception e) { e.printStackTrace();
	 * logger.info("sendAttendanceDataToSapDemoException=============> " +
	 * e.getMessage()); return "{\"Status\":\"Exception Occurred\"}"; } }
	 */
	@RequestMapping(value = "/getAttendanceDataSentToSapForApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAttendanceDataSentToSapForApp(
			@RequestBody StudentCourseAttendance studentCourseAttendance, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		studentCourseAttendance = (StudentCourseAttendance) decryptRequestBody(
				studentCourseAttendance.getEncrypted_key(), "StudentCourseAttendance");
		boolean auth = isUserAuthorized(headers.get("token"), studentCourseAttendance.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		List<StudentCourseAttendance> studentCourseAttendanceList = new ArrayList<>();
		String json = "";
		try {
			String startTime = studentCourseAttendance.getStartTime();
			String status = "";
			status = studentCourseAttendance.getStatus();
			logger.info("startTime==========> " + startTime);
			logger.info("status==========> " + status);

			studentCourseAttendanceList = studentCourseAttendanceService.getDistinctEndTime(startTime, status);

			logger.info("studentCourseAttendanceList==========> " + studentCourseAttendanceList);
			json = new Gson().toJson(studentCourseAttendanceList);

			json = encryptResponseBody(json);
			return json;
		} catch (Exception e) {
			// e.printStackTrace();
			logger.info("studentCourseAttendanceList==========> Null");
			logger.info("getAttendance_Exception=============> " + e.getMessage());
			json = new Gson().toJson(studentCourseAttendanceList);

			json = encryptResponseBody(json);
			return json;
		}
	}

	@RequestMapping(value = "/sendAttendanceDataToSapDemo", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String sendAttendanceDataToSapDemo(@RequestBody Attendance attendance,
			@RequestHeader Map<String, String> headers) {
		String json = "", response = "", result = "";
		try {
			String token = headers.get("token");
			if (token != null && service.getValue(token) != null) {
				String value = service.getValue(token).toString();
				logger.info("value==========> " + value);
				if (null != value && value.length() > 0) {
					value = StringEscapeUtils.unescapeJava(value);
					String startDate = attendance.getStartDate().replace("T", " ");
					logger.info("startDate==========> " + startDate);
					List<StudentCourseAttendance> absentList = studentCourseAttendanceService
							.getAbsentRecordsByDate(startDate);
					List<StudentCourseAttendance> presentList = studentCourseAttendanceService
							.getPresentRecordsByDate(startDate);
					if (absentList.size() > 0 || presentList.size() > 0) {
						response = studentCourseAttendanceService.sendAttendanceToSAPByDateForApp(startDate);
						logger.info("ResponseFromSAP========>" + response);
						if (response.endsWith("_ErrorOccurred400")) {
							json = "{\"Status\":\"Failed\"}";
						} else {
							json = "{\"Status\":\"Success\"}";
						}
					} else {
						json = "{\"Status\":\"Failed. Data has been already sent or no data available to send\"}";
					}
					return json;
				} else {
					return "{\"Status\":\"Failed. Invalid Access Token\"}";
				}

			} else {
				return "{\"Status\":\"Failed. Unauthorised Access\"}";
			}

		} catch (Exception e) {
//			e.printStackTrace();
			logger.info("sendAttendanceDataToSapDemoException=============> " + e.getLocalizedMessage());
			json = "{\"Status\":\"Exception Occurred\"}";
//			json = encryptResponseBody(json);
			return json;
		}
	}

	@RequestMapping(value = "/sendAttendanceDataToSapByApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String sendAttendanceDataToSapByApp(@RequestBody Attendance attendance,
			HttpServletResponse resp) {
		String json = "", response = "", result = "";
		try {
			String startDate = attendance.getStartDate().replace("T", " ");
			String startDateLike = startDate.split(" ")[0] + "%";
			logger.info("startDate==========> " + startDate);
			logger.info("startDateLike==========> " + startDateLike);
			response = studentCourseAttendanceService.sendAttendanceToSAPByDateForApp(startDate, startDateLike);
			logger.info("ResponseFromSAP========>" + response);
			if (response.endsWith("_ErrorOccurred400")) {
				return "{\"Status\":\"Failed\"}";
			} else {
				return "{\"Status\":\"Success\"}";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("sendAttendanceDataToSapDemoException=============> " + e.getMessage());
			return "{\"Status\":\"Exception Occurred\"}";
		}
	}

	@RequestMapping(value = "/getTestDetailsForAndroidApp", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTestDetailsForAndroidApp(@RequestBody Test test, HttpServletResponse resp,
			@RequestHeader Map<String, String> headers) {
		test = (Test) decryptRequestBody(test.getEncrypted_key(), "Test");
		boolean auth = isUserAuthorized(headers.get("token"), test.getUsername());
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}
		String todayDate = "", idForTest = "";
		List<Test> testDetailsList = new ArrayList<>();

		try {
			idForTest = test.getIdForTest();
			logger.info("idForTest============> " + idForTest);
			if (idForTest.equals("find_test_details")) {
				logger.info("id available============> " + "false");
				Date date = Utils.getInIST();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date today = new Date();
				todayDate = df.format(today) + "%";
				logger.info("todayDate============> " + todayDate);

				testDetailsList = testService.findtestByStartDate(todayDate);
				logger.info("testDetailsList============> " + testDetailsList);
				String json = new Gson().toJson(testDetailsList);
				return json;
			} else {
				logger.info("id available============> " + "true");
				int studentCount = 0;
				studentCount = testService.getStudentCountByTestId(Long.parseLong(idForTest));
				logger.info("studentCount============> " + String.valueOf(studentCount));
				if (studentCount > 0) {
					return "{\"StudentCount\":\"" + studentCount + "\"}";
				} else {
					return "{\"StudentCount\":\"0\"}";
				}
			}
		} catch (Exception e) {
			String json = new Gson().toJson(testDetailsList);
			return json;
		}
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/createAnnouncementBySupportAdminForm", method = RequestMethod.GET)
	public String createAnnouncementBySupportAdminForm(@ModelAttribute("user") User user, Model m) {
		m.addAttribute("webPage", new WebPage("announcement", "Create Announcement", true, false));
		m.addAttribute("announcement", new Announcement());
		return "user/addAnnouncementBySupportAdmin";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/createAnnouncementBySupportAdmin", method = RequestMethod.POST)
	public String createAnnouncementBySupportAdmin(@ModelAttribute("user") User user, Announcement announcement,
			RedirectAttributes redirectAttrs, Principal principal) {
		String username = principal.getName();

		User userFromUsermgmt = new User();
		user.setUsername(username);

		try {

			announcemnetService.addNotification(announcement);
		} catch (ValidationException ex) {

			setError(redirectAttrs, "Error Creating Notification");
			return "redirect:/homepage";
		}

		String json = new Gson().toJson(announcement);

		try {
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addNotification?json=" + json));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "Notification Added Successfully");
			Boolean sucessStatus = false;

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		return "redirect:/createAnnouncementBySupportAdminForm";
	}

	/*
	 * // shubham (fetching all lecture and studentlist at once) 12Feb2020
	 * 
	 * @RequestMapping(value = "/getCompleteLectureAndStudentListCourseWise", method
	 * = { RequestMethod.GET, RequestMethod.POST }) public @ResponseBody String
	 * getCompleteLectureAndStudentListCourseWise(@RequestBody Course course,
	 * HttpServletResponse resp) { List<Timetable> masterLectureStudentList = new
	 * ArrayList<>(); try { /////////////////////////////////
	 * 
	 * MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
	 * 
	 * DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
	 * 
	 * .createDefaultConnectionByDS(defaultUrl, defaultUsername,
	 * 
	 * defaultPassword);
	 * 
	 * DriverManagerDataSource dataSourceTimetable = multipleDBConnection
	 * 
	 * .createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername,
	 * defaultPassword, "timetable_metadata");
	 * 
	 * timetableDAO.setDS(dataSourceTimetable); String cid = ""; long eventId = 0;
	 * long pgrmId = 0; if (null != course.getId()) { cid =
	 * course.getId().toString(); eventId = Long.parseLong(cid.substring(0, 8));
	 * pgrmId = Long.parseLong(cid.substring(8)); } String username =
	 * course.getUsername(); if (username.contains("_")) { username =
	 * username.substring(0, username.indexOf("_")); } String curDate = "";
	 * 
	 * if (null == course.getClassDate()) { Date dt = new Date(); SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("dd-MM-yyyy"); curDate = "%" +
	 * dateFormat.format(dt) + "%"; } else { curDate = "%" + course.getClassDate() +
	 * "%"; }
	 * 
	 * List<Timetable> tt = new ArrayList<>(); if (null != course.getId()) { tt =
	 * timetableDAO.getTimetableByCourse(pgrmId, eventId, username, curDate); } else
	 * { tt = timetableDAO.getTimetableByCourseForApp(username, curDate); }
	 * 
	 * long pgrmId1; String cid1; List<Timetable> ttFinal = new ArrayList<>();
	 * 
	 * Map<String, String> courseIdEndTimeMap = new HashMap<String, String>();
	 * 
	 * for (Timetable tmtl : tt) {
	 * 
	 * String[] programidsStrings = tmtl.getProgramId().split(" , "); String
	 * programNameToSend = "", courseIdToSend = "", courseNameToSend = "";
	 * 
	 * tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
	 * tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace(".", ":"));
	 * tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
	 * tmtl.setMaxEndTimeForCourse(tmtl.getEnd_time());
	 * 
	 * String allocatedLectures = "", conductedLecture = "", remainingLecture = "",
	 * workLoadResponse = "";
	 * 
	 * workLoadResponse =
	 * attendanceService.pullFacultyWorkload(String.valueOf(tmtl.getEventId()),
	 * username); if (null != workLoadResponse) { Timetable ttWorkLoadResponse = new
	 * Gson().fromJson(workLoadResponse, Timetable.class); if (null !=
	 * ttWorkLoadResponse) { allocatedLectures =
	 * ttWorkLoadResponse.getAllottedLecture(); conductedLecture =
	 * ttWorkLoadResponse.getConductedLecture();
	 * 
	 * if (Double.parseDouble(allocatedLectures) > 0 &&
	 * Double.parseDouble(allocatedLectures) >=
	 * Double.parseDouble(conductedLecture)) {
	 * 
	 * if (!allocatedLectures.equals("") || conductedLecture.equals("")) {
	 * remainingLecture = String.valueOf( Double.parseDouble(allocatedLectures) -
	 * Double.parseDouble(conductedLecture));
	 * 
	 * tmtl.setAllottedLecture(allocatedLectures);
	 * tmtl.setConductedLecture(conductedLecture);
	 * tmtl.setRemainingLecture(remainingLecture); } } } }
	 * 
	 * if (tmtl.getProgramId().contains(" , ")) {
	 * 
	 * Program p1 = new Program(); Course c1 = new Course(); boolean flag = false;
	 * 
	 * for (int i = 0; i < programidsStrings.length; i++) {
	 * 
	 * cid1 = "" + tmtl.getEventId() + programidsStrings[i]; c1 =
	 * courseService.findByID(Long.parseLong(cid1));
	 * 
	 * pgrmId1 = Long.parseLong(programidsStrings[i]); p1 =
	 * programService.findByID(pgrmId1);
	 * 
	 * if (null != c1 && null != p1) {
	 * 
	 * if (i < programidsStrings.length - 1) { programNameToSend = programNameToSend
	 * + p1.getProgramName() + " , "; courseIdToSend = courseIdToSend + cid1 +
	 * " , "; courseNameToSend = c1.getCourseName(); } else { programNameToSend =
	 * programNameToSend + p1.getProgramName(); courseIdToSend = courseIdToSend +
	 * cid1; courseNameToSend = c1.getCourseName(); } }
	 * 
	 * else { flag = true; for (int j = 0; j < programidsStrings.length; j++) {
	 * String cid11 = "" + tmtl.getEventId() + programidsStrings[j]; Course c11 =
	 * courseService.findByID(Long.parseLong(cid11)); pgrmId1 =
	 * Long.parseLong(programidsStrings[j]); Program p11 =
	 * programService.findByID(pgrmId1);
	 * 
	 * if (null != c11 && null != p11) { Timetable tmtlSub = new Timetable();
	 * 
	 * tmtlSub.setClass_date(tmtl.getClass_date());
	 * tmtlSub.setStart_time(tmtl.getStart_time());
	 * tmtlSub.setEnd_time(tmtl.getEnd_time());
	 * tmtlSub.setEventId(tmtl.getEventId());
	 * tmtlSub.setFacultyId(tmtl.getFacultyId()); tmtlSub.setFlag(tmtl.getFlag());
	 * cid11 = "" + tmtl.getEventId() + programidsStrings[j];
	 * tmtlSub.setMaxEndTimeForCourse(tmtl.getEnd_time());
	 * tmtlSub.setCourseId(cid11); tmtlSub.setCourseName(c11.getCourseName());
	 * tmtlSub.setProgramId(programidsStrings[j]);
	 * tmtlSub.setProgramName(p11.getProgramName());
	 * logger.info(p11.getProgramName()); if (null != course.getId()) {
	 * 
	 * if (cid11.equals(tmtlSub.getCourseId())) {
	 * 
	 * ttFinal.add(tmtlSub); } } else { courseIdEndTimeMap.put(cid11,
	 * tmtlSub.getEnd_time());
	 * 
	 * ttFinal.add(tmtlSub); } }
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * if (!flag) { if (null != c1 && null != p1) { if
	 * (programNameToSend.substring(programNameToSend.length() - 3).equals(" , ")) {
	 * programNameToSend = programNameToSend.substring(0, programNameToSend.length()
	 * - 3); } if (courseIdToSend.substring(courseIdToSend.length() -
	 * 3).equals(" , ")) { courseIdToSend = courseIdToSend.substring(0,
	 * courseIdToSend.length() - 3); }
	 * 
	 * tmtl.setCourseId(courseIdToSend); // tmtl.setProgramId(tmtl.getProgramId());
	 * tmtl.setCourseName(courseNameToSend); tmtl.setProgramName(programNameToSend);
	 * courseIdEndTimeMap.put(courseIdToSend, tmtl.getEnd_time());
	 * ttFinal.add(tmtl); } }
	 * 
	 * } else { cid1 = "" + tmtl.getEventId() + tmtl.getProgramId(); Course c1 =
	 * courseService.findByID(Long.parseLong(cid1)); pgrmId1 =
	 * Long.parseLong(tmtl.getProgramId()); Program p1 =
	 * programService.findByID(pgrmId1);
	 * 
	 * if (null != c1 && null != p1) {
	 * 
	 * tmtl.setCourseId(cid1); tmtl.setCourseName(c1.getCourseName());
	 * tmtl.setProgramName(p1.getProgramName());
	 * courseIdEndTimeMap.put(tmtl.getCourseId(), tmtl.getEnd_time());
	 * ttFinal.add(tmtl); } }
	 * 
	 * }
	 * 
	 * for (Object name : courseIdEndTimeMap.keySet()) { String key =
	 * name.toString(); String value = courseIdEndTimeMap.get(name);
	 * 
	 * }
	 * 
	 * // //////////////////////////////////////////////////////////////////
	 * 
	 * for (String key : courseIdEndTimeMap.keySet()) {
	 * 
	 * // List<Timetable> courseTimetable = new ArrayList<>(); List<Timetable>
	 * courseTimetable = new ArrayList<>(); int i = 0; while (i < ttFinal.size()) {
	 * 
	 * if (key.equals(ttFinal.get(i).getCourseId())) { //
	 * logger.info("key--------->"+key); //
	 * logger.info("ttFinal.get(i).getCourseId()--------->"+ttFinal.get(i).
	 * getCourseId()); courseTimetable.add(ttFinal.get(i)); } i++; } i = 0; for
	 * (Timetable ct : courseTimetable) { if (i < courseTimetable.size() - 1) {
	 * String lecEndTime = ct.getClass_date() + " " +
	 * courseTimetable.get(i).getEnd_time(); String nxtLecStartTime =
	 * ct.getClass_date() + " " + courseTimetable.get(i + 1).getStart_time();
	 * 
	 * long diff = timeDifference(lecEndTime, nxtLecStartTime); if (diff < 60000) {
	 * 
	 * courseTimetable.get(i).setMaxEndTimeForCourse(courseIdEndTimeMap.get(key)); }
	 * else { for (int j = i; j > 0; j--) { String lecStartTime = ct.getClass_date()
	 * + " " + courseTimetable.get(j).getStart_time(); String prevLecEndTime =
	 * ct.getClass_date() + " " + courseTimetable.get(j - 1).getEnd_time();
	 * 
	 * long diff1 = timeDifference(prevLecEndTime, lecStartTime);
	 * 
	 * if (diff1 < 60000) { courseTimetable.get(j - 1)
	 * .setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time()); }
	 * 
	 * }
	 * 
	 * courseTimetable.get(i).setMaxEndTimeForCourse(courseTimetable.get(i).
	 * getEnd_time());
	 * 
	 * }
	 * 
	 * } i++; } }
	 * 
	 * // ////////////////////////////////////////////////////////////////////////
	 * 
	 * 
	 * String json = new Gson().toJson(ttFinal);
	 * logger.info("timtable List ------->" + json);
	 * 
	 * logger.info("ttFinal-Size---------> " + ttFinal.size());
	 * masterLectureStudentList.addAll(ttFinal);
	 * timetableDAO.setDS(dataSourceDefaultLms);
	 * 
	 * Date dt = new Date(); SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("yyyy-MM-dd"); String dateToAppend = dateFormat.format(dt);
	 * 
	 * logger.info("dateToAppend---------> " + dateToAppend); for (Timetable ttf :
	 * ttFinal) { if (ttf.getCourseId().contains(" , ")) { List<Long> courseIdList =
	 * new ArrayList<Long>(); String[] multipleCourseArr =
	 * ttf.getCourseId().split(" , "); for (int m = 0; m < multipleCourseArr.length;
	 * m++) { courseIdList.add(Long.parseLong(multipleCourseArr[m])); }
	 * logger.info("courseIdList-Multiple---------> " + courseIdList); int sca_count
	 * = studentCourseAttendanceService.getStudentCountByCourseIdStartEndTime(
	 * courseIdList, dateToAppend + " " + ttf.getStart_time(), dateToAppend + " " +
	 * ttf.getEnd_time()); logger.info("sca_count---------> " + sca_count);
	 * 
	 * StudentCourseAttendance studentCourseAttendance = new
	 * StudentCourseAttendance();
	 * studentCourseAttendance.setCids(ttf.getCourseId());
	 * studentCourseAttendance.setCourseId(ttf.getCourseId());
	 * studentCourseAttendance.setStartTime(ttf.getStart_time());
	 * studentCourseAttendance.setEndTime(ttf.getEnd_time());
	 * studentCourseAttendance.setActualEndTime(ttf.getMaxEndTimeForCourse());
	 * studentCourseAttendance.setFacultyId(ttf.getFacultyId());
	 * 
	 * if (sca_count < 1) {
	 * 
	 * List<Course> studentListByCourse =
	 * getStudentsByCourseForAndroidApp(studentCourseAttendance); for (Timetable
	 * timetable : masterLectureStudentList) { if
	 * (ttf.getCourseId().equals(timetable.getCourseId()) &&
	 * ttf.getStart_time().equals(timetable.getStart_time()) &&
	 * ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
	 * timetable.setCourseList(studentListByCourse); } } } else {
	 * List<StudentCourseAttendance> studentListfromStudentCourseAttendance =
	 * showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
	 * studentCourseAttendance);
	 * logger.info("studentListfromStudentCourseAttendance--Multiple--------> " +
	 * studentListfromStudentCourseAttendance); for (Timetable timetable :
	 * masterLectureStudentList) { if
	 * (ttf.getCourseId().equals(timetable.getCourseId()) &&
	 * ttf.getStart_time().equals(timetable.getStart_time()) &&
	 * ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
	 * timetable.setStudentCourseAttendanceList(
	 * studentListfromStudentCourseAttendance); } }
	 * logger.info("alreadyMarked--Multiple--------> " + courseIdList + " " +
	 * dateToAppend + " " + ttf.getStart_time() + " " + dateToAppend + " " +
	 * ttf.getEnd_time()); } } else { List<Long> courseIdList = new
	 * ArrayList<Long>(); courseIdList.add(Long.parseLong(ttf.getCourseId()));
	 * logger.info("courseIdList-Single---------> " + courseIdList);
	 * 
	 * int sca_count =
	 * studentCourseAttendanceService.getStudentCountByCourseIdStartEndTime(
	 * courseIdList, dateToAppend + " " + ttf.getStart_time(), dateToAppend + " " +
	 * ttf.getEnd_time());
	 * 
	 * StudentCourseAttendance studentCourseAttendance = new
	 * StudentCourseAttendance();
	 * studentCourseAttendance.setCids(ttf.getCourseId());
	 * studentCourseAttendance.setCourseId(ttf.getCourseId());
	 * studentCourseAttendance.setStartTime(ttf.getStart_time());
	 * studentCourseAttendance.setEndTime(ttf.getEnd_time());
	 * studentCourseAttendance.setActualEndTime(ttf.getMaxEndTimeForCourse());
	 * studentCourseAttendance.setFacultyId(ttf.getFacultyId());
	 * 
	 * if (sca_count < 1) {
	 * 
	 * List<Course> studentListByCourse =
	 * getStudentsByCourseForAndroidApp(studentCourseAttendance); for (Timetable
	 * timetable : masterLectureStudentList) { if
	 * (ttf.getCourseId().equals(timetable.getCourseId()) &&
	 * ttf.getStart_time().equals(timetable.getStart_time()) &&
	 * ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
	 * timetable.setCourseList(studentListByCourse); } } } else {
	 * List<StudentCourseAttendance> studentListfromStudentCourseAttendance =
	 * showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
	 * studentCourseAttendance);
	 * logger.info("studentListfromStudentCourseAttendance--Single--------> " +
	 * studentListfromStudentCourseAttendance); for (Timetable timetable :
	 * masterLectureStudentList) { if
	 * (ttf.getCourseId().equals(timetable.getCourseId()) &&
	 * ttf.getStart_time().equals(timetable.getStart_time()) &&
	 * ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
	 * timetable.setStudentCourseAttendanceList(
	 * studentListfromStudentCourseAttendance); } }
	 * logger.info("alreadyMarked--Single--------> " + courseIdList + " " +
	 * dateToAppend + " " + ttf.getStart_time() + " " + dateToAppend + " " +
	 * ttf.getEnd_time()); } } }
	 * 
	 * //////////////////////////////////
	 * 
	 * String json = new Gson().toJson(masterLectureStudentList);
	 * logger.info("masterLectureStudentList----------> " +
	 * masterLectureStudentList); return json; } catch (Exception e) { logger.error(
	 * "getCompleteLectureAndStudentListCourseWise_Exception------------->" + e);
	 * 
	 * return "{\"Status\":\"Exception Occurred\"}"; } }
	 */

	@RequestMapping(value = "/getCompleteLectureAndStudentListCourseWise", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String getCompleteLectureAndStudentListCourseWise(@RequestBody Course course,
			HttpServletResponse resp, @RequestHeader Map<String, String> headers) {
		course = (Course) decryptRequestBody(course.getEncrypted_key(), "Course");
		logger.info("HttpHeadersToken ==> " + headers.get("token"));
		boolean auth = isUserAuthorized(headers.get("token"), course.getUsername());

		if (!auth) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", "failed");
			map.put("access", "unauthorised access");
			String json = new Gson().toJson(map);

			json = encryptResponseBody(json);
			return json;
		}

		List<Timetable> masterLectureStudentList = new ArrayList<>();
		try {
			/////////////////////////////////

			MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

			DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

					.createDefaultConnectionByDS(defaultUrl, defaultUsername,

							defaultPassword);

			DriverManagerDataSource dataSourceTimetable = multipleDBConnection

					.createConnectionByDS("jdbc:mysql://localhost:3306/", defaultUsername, defaultPassword,
							"timetable_metadata");

			timetableDAO.setDS(dataSourceTimetable);
			String cid = "";
			long eventId = 0;
			long pgrmId = 0;
			if (null != course.getId()) {
				cid = course.getId().toString();
				eventId = Long.parseLong(cid.substring(0, 8));
				pgrmId = Long.parseLong(cid.substring(8));
			}
			String username = course.getUsername();
			if (username.contains("_")) {
				username = username.substring(0, username.indexOf("_"));
			}
			String curDate = "";

			if (null == course.getClassDate()) {
				Date dt = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				curDate = "%" + dateFormat.format(dt) + "%";
			} else {
				curDate = "%" + course.getClassDate() + "%";
			}

			List<Timetable> tt = new ArrayList<>();
			if (null != course.getId()) {
				tt = timetableDAO.getTimetableByCourse(pgrmId, eventId, username, curDate);
			} else {
				tt = timetableDAO.getTimetableByCourseForApp(username, curDate);
			}

			long pgrmId1;
			String cid1;
			List<Timetable> ttFinal = new ArrayList<>();

			Map<String, String> courseIdEndTimeMap = new HashMap<String, String>();

			for (Timetable tmtl : tt) {

				String[] programidsStrings = tmtl.getProgramId().split(" , ");
				String programNameToSend = "", courseIdToSend = "", courseNameToSend = "";

				tmtl.setClass_date(tmtl.getClass_date().split(" ")[0]);
				tmtl.setStart_time(tmtl.getStart_time().split(" ")[1].replace(".", ":"));
				tmtl.setEnd_time(tmtl.getEnd_time().split(" ")[1].replace(".", ":"));
				tmtl.setMaxEndTimeForCourse(tmtl.getEnd_time());

				/* String workLoadResponse = attendanceService.pullFacultyWorkload(String.valueOf(tmtl.getEventId()),username); */  String workLoadResponse = null;

				if (null != workLoadResponse) {
					String workload[] = fetchWorkload(workLoadResponse);

					tmtl.setAllottedLecture(workload[0]);
					tmtl.setConductedLecture(workload[1]);
					tmtl.setRemainingLecture(workload[2]);
				} else {
					logger.info("workLoadResponse  --> is null");
				}

				if (tmtl.getProgramId().contains(" , ")) {

					Program p1 = new Program();
					Course c1 = new Course();
					boolean flag = false;

					for (int i = 0; i < programidsStrings.length; i++) {

						cid1 = "" + tmtl.getEventId() + programidsStrings[i];
						c1 = courseService.findByID(Long.parseLong(cid1));

						pgrmId1 = Long.parseLong(programidsStrings[i]);
						p1 = programService.findByID(pgrmId1);

						if (null != c1 && null != p1) {

							if (i < programidsStrings.length - 1) {
								programNameToSend = programNameToSend + p1.getProgramName() + " , ";
								courseIdToSend = courseIdToSend + cid1 + " , ";
								courseNameToSend = c1.getCourseName();
							} else {
								programNameToSend = programNameToSend + p1.getProgramName();
								courseIdToSend = courseIdToSend + cid1;
								courseNameToSend = c1.getCourseName();
							}
						}

						else {
							flag = true;
							for (int j = 0; j < programidsStrings.length; j++) {
								String cid11 = "" + tmtl.getEventId() + programidsStrings[j];
								Course c11 = courseService.findByID(Long.parseLong(cid11));
								pgrmId1 = Long.parseLong(programidsStrings[j]);
								Program p11 = programService.findByID(pgrmId1);

								if (null != c11 && null != p11) {
									Timetable tmtlSub = new Timetable();

									tmtlSub.setClass_date(tmtl.getClass_date());
									tmtlSub.setStart_time(tmtl.getStart_time());
									tmtlSub.setEnd_time(tmtl.getEnd_time());
									tmtlSub.setEventId(tmtl.getEventId());
									tmtlSub.setFacultyId(tmtl.getFacultyId());
									tmtlSub.setFlag(tmtl.getFlag());
									cid11 = "" + tmtl.getEventId() + programidsStrings[j];
									tmtlSub.setMaxEndTimeForCourse(tmtl.getEnd_time());
									tmtlSub.setCourseId(cid11);
									tmtlSub.setCourseName(c11.getCourseName());
									tmtlSub.setProgramId(programidsStrings[j]);
									tmtlSub.setProgramName(p11.getProgramName());
									tmtlSub.setAllottedLecture(tmtl.getAllottedLecture());
									tmtlSub.setConductedLecture(tmtl.getConductedLecture());
									tmtlSub.setRemainingLecture(tmtl.getRemainingLecture());

									logger.info(p11.getProgramName());
									if (null != course.getId()) {

										if (cid11.equals(tmtlSub.getCourseId())) {

											ttFinal.add(tmtlSub);
										}
									} else {
										courseIdEndTimeMap.put(cid11, tmtlSub.getEnd_time());

										ttFinal.add(tmtlSub);
									}
								}

							}
						}

					}

					if (!flag) {
						if (null != c1 && null != p1) {
							if (programNameToSend.substring(programNameToSend.length() - 3).equals(" , ")) {
								programNameToSend = programNameToSend.substring(0, programNameToSend.length() - 3);
							}
							if (courseIdToSend.substring(courseIdToSend.length() - 3).equals(" , ")) {
								courseIdToSend = courseIdToSend.substring(0, courseIdToSend.length() - 3);
							}

							tmtl.setCourseId(courseIdToSend);
							// tmtl.setProgramId(tmtl.getProgramId());
							tmtl.setCourseName(courseNameToSend);
							tmtl.setProgramName(programNameToSend);
							courseIdEndTimeMap.put(courseIdToSend, tmtl.getEnd_time());
							ttFinal.add(tmtl);
						}
					}

				} else {
					cid1 = "" + tmtl.getEventId() + tmtl.getProgramId();
					Course c1 = courseService.findByID(Long.parseLong(cid1));
					pgrmId1 = Long.parseLong(tmtl.getProgramId());
					Program p1 = programService.findByID(pgrmId1);

					if (null != c1 && null != p1) {

						tmtl.setCourseId(cid1);
						tmtl.setCourseName(c1.getCourseName());
						tmtl.setProgramName(p1.getProgramName());
						courseIdEndTimeMap.put(tmtl.getCourseId(), tmtl.getEnd_time());
						ttFinal.add(tmtl);
					}
				}

			}

			for (Object name : courseIdEndTimeMap.keySet()) {
				String key = name.toString();
				String value = courseIdEndTimeMap.get(name);

			}

			// //////////////////////////////////////////////////////////////////

			for (String key : courseIdEndTimeMap.keySet()) {

				// List<Timetable> courseTimetable = new ArrayList<>();
				List<Timetable> courseTimetable = new ArrayList<>();
				int i = 0;
				while (i < ttFinal.size()) {

					if (key.equals(ttFinal.get(i).getCourseId())) {
						courseTimetable.add(ttFinal.get(i));
					}
					i++;
				}
				i = 0;
				for (Timetable ct : courseTimetable) {
					if (i < courseTimetable.size() - 1) {
						String lecEndTime = ct.getClass_date() + " " + courseTimetable.get(i).getEnd_time();
						String nxtLecStartTime = ct.getClass_date() + " " + courseTimetable.get(i + 1).getStart_time();

						long diff = timeDifference(lecEndTime, nxtLecStartTime);
						if (diff < 60000) {

							courseTimetable.get(i).setMaxEndTimeForCourse(courseIdEndTimeMap.get(key));
						} else {
							for (int j = i; j > 0; j--) {
								String lecStartTime = ct.getClass_date() + " " + courseTimetable.get(j).getStart_time();
								String prevLecEndTime = ct.getClass_date() + " "
										+ courseTimetable.get(j - 1).getEnd_time();

								long diff1 = timeDifference(prevLecEndTime, lecStartTime);

								if (diff1 < 60000) {
									courseTimetable.get(j - 1)
											.setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());
								}

							}

							courseTimetable.get(i).setMaxEndTimeForCourse(courseTimetable.get(i).getEnd_time());

						}

					}
					i++;
				}
			}

			// ////////////////////////////////////////////////////////////////////////

			logger.info("ttFinal_Size-_before--------> " + ttFinal.size());
			List<Timetable> removeDuplicateTTF = new ArrayList<Timetable>(new LinkedHashSet<Timetable>(ttFinal));
			logger.info("removeDuplicateTTF_size---------> " + removeDuplicateTTF.size());
			ttFinal.clear();
			ttFinal.addAll(removeDuplicateTTF);
			logger.info("ttFinal_Size-_after--------> " + ttFinal.size());

			masterLectureStudentList.addAll(ttFinal);
			timetableDAO.setDS(dataSourceDefaultLms);

			Date dt = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dateToAppend = dateFormat.format(dt);

			logger.info("dateToAppend---------> " + dateToAppend);
			for (Timetable ttf : ttFinal) {
				if (ttf.getCourseId().contains(" , ")) {
					List<Long> courseIdList = new ArrayList<Long>();
					String[] multipleCourseArr = ttf.getCourseId().split(" , ");
					for (int m = 0; m < multipleCourseArr.length; m++) {
						courseIdList.add(Long.parseLong(multipleCourseArr[m]));
					}
					logger.info("courseIdList-Multiple---------> " + courseIdList);
					int sca_count = studentCourseAttendanceService.getStudentCountByCourseIdStartEndTime(courseIdList,
							dateToAppend + " " + ttf.getStart_time(), dateToAppend + " " + ttf.getEnd_time(), username);
					logger.info("sca_count---------> " + sca_count);

					StudentCourseAttendance studentCourseAttendance = new StudentCourseAttendance();
					studentCourseAttendance.setCids(ttf.getCourseId());
					studentCourseAttendance.setCourseId(ttf.getCourseId());
					studentCourseAttendance.setStartTime(ttf.getStart_time());
					studentCourseAttendance.setEndTime(ttf.getEnd_time());
					studentCourseAttendance.setActualEndTime(ttf.getMaxEndTimeForCourse());
					studentCourseAttendance.setFacultyId(username);

					if (sca_count < 1) {

						List<Course> studentListByCourse = getStudentsByCourseForAndroidApp(studentCourseAttendance);
						for (Timetable timetable : masterLectureStudentList) {
							if (ttf.getCourseId().equals(timetable.getCourseId())
									&& ttf.getStart_time().equals(timetable.getStart_time())
									&& ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
								timetable.setCourseList(studentListByCourse);
							}
						}
					} else {
						List<StudentCourseAttendance> studentListfromStudentCourseAttendance = showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
								studentCourseAttendance);
						logger.info("studentListfromStudentCourseAttendance--Multiple--------> "
								+ studentListfromStudentCourseAttendance);
						for (Timetable timetable : masterLectureStudentList) {
							if (ttf.getCourseId().equals(timetable.getCourseId())
									&& ttf.getStart_time().equals(timetable.getStart_time())
									&& ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
								timetable.setStudentCourseAttendanceList(studentListfromStudentCourseAttendance);
							}
						}
						logger.info("alreadyMarked--Multiple--------> " + courseIdList + " " + dateToAppend + " "
								+ ttf.getStart_time() + " " + dateToAppend + " " + ttf.getEnd_time());
					}
				} else {
					List<Long> courseIdList = new ArrayList<Long>();
					courseIdList.add(Long.parseLong(ttf.getCourseId()));
					logger.info("courseIdList-Single---------> " + courseIdList);

					int sca_count = studentCourseAttendanceService.getStudentCountByCourseIdStartEndTime(courseIdList,
							dateToAppend + " " + ttf.getStart_time(), dateToAppend + " " + ttf.getEnd_time(), username);

					StudentCourseAttendance studentCourseAttendance = new StudentCourseAttendance();
					studentCourseAttendance.setCids(ttf.getCourseId());
					studentCourseAttendance.setCourseId(ttf.getCourseId());
					studentCourseAttendance.setStartTime(ttf.getStart_time());
					studentCourseAttendance.setEndTime(ttf.getEnd_time());
					studentCourseAttendance.setActualEndTime(ttf.getMaxEndTimeForCourse());
					studentCourseAttendance.setFacultyId(username);

					if (sca_count < 1) {

						List<Course> studentListByCourse = getStudentsByCourseForAndroidApp(studentCourseAttendance);
						for (Timetable timetable : masterLectureStudentList) {
							if (ttf.getCourseId().equals(timetable.getCourseId())
									&& ttf.getStart_time().equals(timetable.getStart_time())
									&& ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
								timetable.setCourseList(studentListByCourse);
							}
						}
					} else {
						List<StudentCourseAttendance> studentListfromStudentCourseAttendance = showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
								studentCourseAttendance);
						logger.info("studentListfromStudentCourseAttendance--Single--------> "
								+ studentListfromStudentCourseAttendance);
						for (Timetable timetable : masterLectureStudentList) {
							if (ttf.getCourseId().equals(timetable.getCourseId())
									&& ttf.getStart_time().equals(timetable.getStart_time())
									&& ttf.getMaxEndTimeForCourse().equals(timetable.getMaxEndTimeForCourse())) {
								timetable.setStudentCourseAttendanceList(studentListfromStudentCourseAttendance);
							}
						}
						logger.info("alreadyMarked--Single--------> " + courseIdList + " " + dateToAppend + " "
								+ ttf.getStart_time() + " " + dateToAppend + " " + ttf.getEnd_time());
					}
				}
			}

			// ////////////////////////////////

			// get name of Faculty Name for multiple faculty
			for (int t = 0; t < masterLectureStudentList.size(); t++) {
				String allFacultyId = masterLectureStudentList.get(t).getFacultyId();
				logger.info("allFacultyId----------> " + allFacultyId);
				if (allFacultyId.contains(",")) {
					String[] multipleFacultyArr = allFacultyId.split(" , ");
					List<String> multipleFacultyList = Arrays.asList(multipleFacultyArr);
					logger.info("multipleFacultyList----------> " + multipleFacultyList.toString());
					List<User> facultyListWithName = userService.findNameOfFaculty(multipleFacultyList);
					logger.info("multipleFacultyList----------> " + multipleFacultyList.toString());
					allFacultyId = "";
					for (User user : facultyListWithName) {
						allFacultyId += user.getUsername() + " , ";
					}
					allFacultyId = allFacultyId.substring(0, allFacultyId.length() - 3);
					logger.info("allFacultyId_After----------> " + allFacultyId);
					masterLectureStudentList.get(t).setFacultyId(allFacultyId);
				}
			}

			String json = new Gson().toJson(masterLectureStudentList);
			logger.info("masterLectureStudentList----------> " + json);
			json = encryptResponseBody(json);
			return json;
		} catch (Exception e) {
			logger.error("getCompleteLectureAndStudentListCourseWise_Exception------------->" + e);
			logger.error("getCompleteLectureAndStudentListCourseWise_Cause------------->" + e.getMessage());
			String json = "{\"Status\":\"Exception Occurred\"}";
			json = encryptResponseBody(json);
			return json;
		}
	}

	public List<Course> getStudentsByCourseForAndroidApp(StudentCourseAttendance studentCourseAttendance) {
		List<Course> msCourseList = new ArrayList<Course>();
		try {
			String coursesId = studentCourseAttendance.getCids();
			String isAttendanceAllowed = "true";
			logger.info("coursesId===> " + coursesId);
			logger.info("actualEndTimeD===> " + studentCourseAttendance.getActualEndTime());
			logger.info("actualStartTime===> " + studentCourseAttendance.getStartTime());
			Date date = Utils.getInIST();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Date today = new Date();
			String todayDate = df.format(today);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String currentTime = sdf.format(date);
			logger.info("currentTime===> " + currentTime);

			String actualEndTime = todayDate + " " + studentCourseAttendance.getActualEndTime();
			logger.info("actualEndTimeBefore===> " + actualEndTime);
			Date actualEndDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(actualEndTime); // conversion of
																									// string time to
																									// date
			Date newActualEndDate = DateUtils.addHours(actualEndDate, 2); // Adding
																			// 2
																			// hours
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // changing
																					// date
																					// to
																					// time
																					// format
			actualEndTime = dateFormat.format(newActualEndDate); // getting time
																	// again
			logger.info("actualEndTimeAFTER===> " + actualEndTime);
			String startTime = todayDate + " " + studentCourseAttendance.getStartTime();
			logger.info(
					"TimeLimitValueStart===> " + String.valueOf(timeDifference(startTime, currentTime)) + " Seconds");
			logger.info(
					"TimeLimitValueEnd===> " + String.valueOf(timeDifference(currentTime, actualEndTime)) + " Seconds");

			if (timeDifference(currentTime, startTime) > 0 || timeDifference(actualEndTime, currentTime) > 0) {
				if (timeDifference(currentTime, startTime) > 0) {
					isAttendanceAllowed = "Lecture has not started yet...";
					logger.info("isAttendanceAllowed===> Insert " + "false");
				} else {
					isAttendanceAllowed = "Lecture time ended...";
					logger.info("isAttendanceAllowed===> Insert " + "false");

				}
			}

			logger.info("isAttendanceAllowed===> Insert " + "true");

			String courseIdCheck = "";

			if (coursesId.contains(" , ")) {
				logger.info("courseIdType============> " + "Multiple");
				Course courseDetails = new Course();
				courseIdCheck = String.valueOf(coursesId);
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");
				logger.info("courseIdCheckStrings============> " + courseIdCheck);

				for (int i = 0; i < courseIdCheckStrings.length; i++) {
					long courseId = Long.valueOf(courseIdCheckStrings[i]);
					courseDetails = courseService.findByID(courseId);
					if (courseDetails != null) {
						List<Course> courseList = courseService.findStudentsByCourseIdForAndroidApp(courseId);
						msCourseList.addAll(courseList);
					}
				}

			} else {
				logger.info("courseIdType============> " + "Single");
				long courseId = Long.valueOf(coursesId);
				Course courseDetails = courseService.findByID(courseId);

				if (courseDetails != null) {
					msCourseList = courseService.findStudentsByCourseIdForAndroidApp(courseId);
				}

			}

			for (Course course : msCourseList) {
				course.setIsAttendanceAllowed(isAttendanceAllowed);
			}

			// String json = new Gson().toJson(msCourseList);
			return msCourseList;

		} catch (Exception e) {
			logger.error("Exception", e);
			return msCourseList;
		}
	}

	public List<StudentCourseAttendance> showStudentAttendanceForAndroidAppFromStudentCourseAttendance(
			StudentCourseAttendance studentCourseAttd) {

		List<StudentCourseAttendance> MasterSCAList = new ArrayList<StudentCourseAttendance>();
		String isAttendanceAllowed = "true";
		String courseIdCheck = studentCourseAttd.getCourseId();
		logger.info("coursesId===> " + courseIdCheck);
		logger.info("actualEndTimeD===> " + studentCourseAttd.getActualEndTime());
		logger.info("actualStartTime===> " + studentCourseAttd.getStartTime());
		Date date = Utils.getInIST();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date today = new Date();
		String todayDate = df.format(today);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String currentTime = sdf.format(date);
		logger.info("currentTime===> " + currentTime);

		String actualEndTime = todayDate + " " + studentCourseAttd.getActualEndTime();
		logger.info("actualEndTimeBefore===> " + actualEndTime);
		Date actualEndDate = null;
		try {
			actualEndDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(actualEndTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // conversion of string time to date
		Date newActualEndDate = DateUtils.addHours(actualEndDate, 2); // Adding
																		// 2
																		// hours
		DateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); // changing
																				// date
																				// to
																				// time
																				// format
		actualEndTime = dateFormate.format(newActualEndDate); // getting time
																// again
		logger.info("actualEndTimeAFTER===> " + actualEndTime);

		String start_Time = todayDate + " " + studentCourseAttd.getStartTime();

		logger.info("TimeLimitValueStart===> " + String.valueOf(timeDifference(start_Time, currentTime)) + " Seconds");
		logger.info("TimeLimitValueEnd===> " + String.valueOf(timeDifference(currentTime, actualEndTime)) + " Seconds");

		if (timeDifference(currentTime, start_Time) > 0 || timeDifference(actualEndTime, currentTime) > 0) {
			if (timeDifference(currentTime, start_Time) > 0) {
				isAttendanceAllowed = "Lecture has not started yet...";
				logger.info("isAttendanceAllowed===> Insert " + "false");
			} else {
				isAttendanceAllowed = "Lecture time ended...";
				logger.info("isAttendanceAllowed===> Insert " + "false");
			}
		}

		logger.info("isAttendanceAllowed===> Update " + "true");
		try {
			if (courseIdCheck.contains(" , ")) {

				logger.info("courseIdType ===========>" + "MULTILE");
				String[] courseIdCheckStrings = courseIdCheck.split(" , ");

				SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");

				String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				logger.info("attdDate ===========>" + attdDate);
				Date d = dateFormatApp.parse(attdDate);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = dateFormat.format(d) + " " + studentCourseAttd.getStartTime();
				String endTime = dateFormat.format(d) + " " + studentCourseAttd.getEndTime();

				for (int i = 0; i < courseIdCheckStrings.length; i++) {
					String spilittedCourseId = courseIdCheckStrings[i];
					List<StudentCourseAttendance> SCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
							spilittedCourseId, startTime, endTime, studentCourseAttd.getFacultyId());

					MasterSCAList.addAll(SCAList);
				}

			} else {

				SimpleDateFormat dateFormatApp = new SimpleDateFormat("dd-MM-yyyy");
				// Date d = new Date(studentCourseAttd.getAttdDate());]

				String attdDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

				Date d = dateFormatApp.parse(attdDate);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = dateFormat.format(d) + " " + studentCourseAttd.getStartTime();
				String endTime = dateFormat.format(d) + " " + studentCourseAttd.getEndTime();

				MasterSCAList = studentCourseAttendanceService.findByCourseIdAndDateTime(
						studentCourseAttd.getCourseId(), startTime, endTime, studentCourseAttd.getFacultyId());

			}
			for (StudentCourseAttendance sca : MasterSCAList) {
				sca.setIsAttendanceAllowed(isAttendanceAllowed);
			}
			// String json = new Gson().toJson(MasterSCAList);

			return MasterSCAList;

		} catch (Exception e) {
			logger.error("Exception", e);
			return MasterSCAList;
		}

	}
	// Change Password by Support Admin
//	@Secured({ "ROLE_SUPPORT_ADMIN"})
//	@RequestMapping(value = "/changeTemporaryPasswordBySupportAdmin", method = { RequestMethod.GET,
//			RequestMethod.POST })
//	public String changeTemporaryPasswordBySupportAdmin(@ModelAttribute User user, @RequestParam String username,
//			RedirectAttributes redirectAttrs, Model m, Principal p) {
//		System.out.println("changeTemporaryPasswordBySupportAdminForm");
//		try {
//			User userDB = userService.findByUserName(user.getUsername());
//			logger.info("changeTemporaryPasswordBySupportAdmin userDB : " + userDB);
//			WebTarget webTarget = client.target(URIUtil
//					.encodeQuery(userRoleMgmtCrudUrl + "/changeTemporaryPasswordBySupportAdmin?username=" + username));
//			logger.info("Return from UserRoleMamtCrud");
//			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
//			logger.info("invocationBuilder " + invocationBuilder);
//			String resp = invocationBuilder.get(String.class);
//
//			logger.info("checking response " + resp);
//			if (resp.equals("Success")) {
//				logger.info("Notification of Success");
//				setSuccess(redirectAttrs, "Password Changed Successfully, New Password is pass@123");
//			} else if (resp.equals("userExisted")) {
//				setNote(redirectAttrs, "User Already Existed");
//			} else if (resp.equals("defaultPassword")) {
//				setNote(redirectAttrs, "Password is already pass@123");
//			} else {
//				setError(redirectAttrs, "Error");
//			}
//
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			setError(redirectAttrs, "Error in Updating");
//		}
//		// return null;
//		return "redirect:/changePasswordBySupportAdminForm";
//	}
	/* New Audit changes start */
	@RequestMapping(value = "/changeTemporaryPasswordBySupportAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String changeTemporaryPasswordBySupportAdmin(@ModelAttribute User user, @RequestParam String username,
			RedirectAttributes redirectAttrs, Model m, Principal p) {
		System.out.println("changeTemporaryPasswordBySupportAdminForm");
		try {
			WebTarget webTarget;
			Invocation.Builder invocationBuilder;
			String resp;
			User userDB = userService.findByUserName(user.getUsername());
			if(null != user.getIsUserBlocked()) {
				webTarget = client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/unlockUserBySupportAdmin?username=" + username));
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				resp = invocationBuilder.get(String.class);
				if (resp.equals("Success")) {
					setSuccess(redirectAttrs, "User Unlock");
				} else {
					setError(redirectAttrs, "Error while unlock user.");
				}
			}else {
				webTarget = client.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/changeTemporaryPasswordBySupportAdmin?username=" + username));
				invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
				resp = invocationBuilder.get(String.class);

				if (resp.equals("Success")) {
					setSuccess(redirectAttrs, "Password Changed Successfully, New Password is pass@123");
				} else if (resp.equals("userExisted")) {
					setNote(redirectAttrs, "User Already Existed");
				} else if (resp.equals("defaultPassword")) {
					setNote(redirectAttrs, "Password is already pass@123");
				} else {
					setError(redirectAttrs, "Error");
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in Updating");
		}
		// return null;
		return "redirect:/changePasswordBySupportAdminForm";
	}
	/* New Audit changes end */

	@Secured({ "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/deleteTemporaryPasswordBySupportAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteTemporaryPasswordBySupportAdmin(@ModelAttribute User user, @RequestParam String username,
			RedirectAttributes redirectAttrs, Model m, Principal p) {
		System.out.println("deleteTemporaryPasswordBySupportAdminForm");
		try {
			User userDB = userService.findByUserName(user.getUsername());
			logger.info("deleteTemporaryPasswordBySupportAdmin userDB : " + userDB);
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl + "/deleteTemporaryPasswordBySupportAdmin?username=" + username));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			logger.info("invocationBuilder " + invocationBuilder);
			String resp = invocationBuilder.get(String.class);
			logger.info("checking response " + resp);
			if (resp.equals("Success")) {
				logger.info("Notification of Success");
				setSuccess(redirectAttrs, "Old Password Restore Successfully");
			} else if (resp.equals("Failed")) {
				setNote(redirectAttrs, "No Record Found");
			} else {
				setError(redirectAttrs, "Error");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in Updating");
		}
		return "redirect:/changePasswordBySupportAdminForm";
	}

	// HostelStudentDetailsBySupportAdmin
	// /lms-bootify/src/main/java/com/spts/lms/web/controllers/LoginController.java
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/hostleStudentDetailsBySupportAdminForm", method = RequestMethod.GET)
	public String hSDetailsBySupportAdminForm(@ModelAttribute("hostelUser") HostelUser hostelUser, Model m) {
		m.addAttribute("webPage", new WebPage("hostleStudentDetails", "Hostel Student Details", true, false));
		return "user/hostleStudentDetailsBySupportAdmin";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/searchHostelStudentDetailBySupportAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String searchHostelStudentDetailBySupportAdmin(Principal principal,
			@ModelAttribute("hostelUser") HostelUser hostelUser, Model m, RedirectAttributes redirectAttrs,
			@RequestParam(required = false) String appId) {
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl + "/searchHostelStudentDetailBySupportAdmin?Sapid=" + appId));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			logger.info("resp : " + resp);
			System.out.println("try Sapid---/// : " + appId);
			ObjectMapper objMapper = new ObjectMapper();
			logger.info("objMapper : " + objMapper);
			List<HostelUser> userList = objMapper.readValue(resp, new TypeReference<List<HostelUser>>() {
			});
			// List<User> userList = objMapper.readValue(resp, new
			// TypeReference<List<User>>() {});

			System.out.println("userList---/// : " + userList);

			if (userList.isEmpty()) {
				System.out.println(" userList No User Found ");
				m.addAttribute("note", "No User Found");
				setNote(redirectAttrs, "No User Found");
			}
			String json = mapper.writeValueAsString(appId);
			System.out.println("json--- " + json);
			setSuccess(redirectAttrs, "User Found");
			m.addAttribute("userList", userList);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			setError(redirectAttrs, "Error");
		}
		return "user/hostleStudentDetailsBySupportAdmin";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/updateHostelStudentDetailBySupportAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateHostelStudentDetailBySupportAdmin(Principal principal,
			@ModelAttribute("hostelUser") HostelUser hostelUser, Model m, RedirectAttributes redirectAttrs,
			@RequestParam(required = false) String appId) throws JsonProcessingException {
		System.out.println("updateHostelStudentDetailBySupportAdmin");
		System.out.println("update Appid---/// " + appId);
		try {
			System.out.println("webTarget start");

			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl + "/updateHostelStudentDetailBySupportAdmin?Sapid=" + appId));
			System.out.println("webTarget : " + webTarget);

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			System.out.println("invocationBuilder : " + invocationBuilder);

			String resp = invocationBuilder.get(String.class);
			System.out.println("resp : " + resp);
			logger.info("resp : " + resp);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			setError(redirectAttrs, "");
			return "error";
		}
		return "success";
	}
	
	private String getSapIdOnly(String all)
    {
        all = all.replaceAll(" ","");
        System.out.println("Input FacultyId with Name --> "+all);
        String[] arr = all.split(",");
        String modifiedFacultyId = "";

        for(String s : arr)
        {
            String[] perFac = s.toString().split("\\(");
            modifiedFacultyId+= perFac[0]+" , ";
        }
        modifiedFacultyId = modifiedFacultyId.substring(0,modifiedFacultyId.length()-2);
        // System.out.println("Output FacultyId with SapId only--> "+modifiedFacultyId);
        modifiedFacultyId = modifiedFacultyId.replaceAll(" ", "");
        return modifiedFacultyId;
    }
	
	private boolean isUserAuthorized(String key, String username) {
		try {
			logger.info("isUserAuthorized key==> " + key);
			logger.info("isUserAuthorized username==> " + username);
			String value = service.getValue(key).toString();
			value = StringEscapeUtils.unescapeJava(value);
			value = value.substring(1, value.length() - 1);
			if (null != value && value.length() > 0) {
				JSONObject json = new JSONObject(value);
				boolean check = username.trim().equals(json.getString("username").trim());
				logger.info("isUserAuthorized_check ==> " + check);
				return check;
			} else {
				return false;
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	private Object decryptRequestBody(String encryptedStr, String cast) {
		Object object = null;
		AESEncryption aes = new AESEncryption(secretKey, salt);
		logger.info("encryptedStr ==> " + encryptedStr);
		encryptedStr = encryptedStr.replace("\n", "").replace("\\", "");
		String decryptedStr = aes.decrypt(encryptedStr);
		logger.info("decryptRequestBody_decryptedStr ==> " + decryptedStr);
		ObjectMapper objMap = new ObjectMapper();
		Reader reader = new StringReader(decryptedStr);
		try {
			if (cast.trim().toLowerCase().equals("studentcourseattendance")) {
				object = objMap.readValue(reader, StudentCourseAttendance.class);
			}
			if (cast.trim().toLowerCase().equals("course")) {
				object = objMap.readValue(reader, Course.class);
			}
			if (cast.trim().toLowerCase().equals("user")) {
				object = objMap.readValue(reader, User.class);
			}
			if (cast.trim().toLowerCase().equals("trainingprogram")) {
				object = objMap.readValue(reader, TrainingProgram.class);
			}
			if (cast.trim().toLowerCase().equals("attendance")) {
				object = objMap.readValue(reader, Attendance.class);
			}
			if (cast.trim().toLowerCase().equals("test")) {
				object = objMap.readValue(reader, Test.class);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		} finally {
			logger.info("decryptRequestBody_object ==> " + object);
		}
		return object;
	}

	private String decryptRequestParam(String encryptedStr) {
		AESEncryption aes = new AESEncryption(secretKey, salt);
		encryptedStr = encryptedStr.replace("\n", "").replace("\\", "");
		String decryptedStr = aes.decrypt(encryptedStr);
		logger.info("decryptRequestBody_decryptedStr ==> " + decryptedStr);
		return decryptedStr;
	}

	private String encryptResponseBody(String json) {
		String encryptedStr = "";
		try {
			AESEncryption aes = new AESEncryption(secretKey, salt);
			encryptedStr = aes.encrypt(json);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return encryptedStr;
	}
	
	private void isMasterDataValidationEnabled(String username,Model m) {
		try {
			logger.info("Inside--->isMasterDataValidationEnabled"); 
			User user = userService.findByUserName(username);
			studentDetailConfirmation dd = new studentDetailConfirmation();
			m.addAttribute("studentdetails",dd);
			//Check MSDV enabled for student
			studentDetailConfirmationPeriod programforStudent = 
					studentDetailConfirmationPeriodService.findbyProgramId(String.valueOf(user.getProgramId()),
							user.getAcadSession(), String.valueOf(user.getCampusId()));
			String ds = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());
			if (programforStudent != null && programforStudent.getEndDate() != null) {
				if (programforStudent.getEndDate().compareTo(ds) >= 0) {
					m.addAttribute("confirmperiod", "true");
					studentDetailConfirmation uservalid = studentDetailConfirmationService.findByUserNamevalidate(username);
					// Firstname Lastname Address Validation (Step 2)
					if (uservalid != null) {
						m.addAttribute("uservalid", "Y");
					} else {
						m.addAttribute("uservalid", "N");
					}
					WebTarget webTarget = client.target(URIUtil.encodeQuery(
							userRoleMgmtCrudUrl + "/getMasterDatavalidationChecks?username=" + username));
					Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
					String resp = invocationBuilder.get(String.class);
					ObjectMapper objMapper = new ObjectMapper();

					studentDetailConfirmation msdvChecks = objMapper.readValue(resp, studentDetailConfirmation.class);

					if (msdvChecks.getUsername() != null) {
						m.addAttribute("firstvalidationcheck", "true");
					} else {
						m.addAttribute("firstvalidationcheck", "false");
					}
					m.addAttribute("secquestionList", msdvChecks.getSecQuestionList());
					m.addAttribute("schoolListMaster", msdvChecks.getSchoolListForMaster());
				} else {
					m.addAttribute("confirmperiod", "false");
				}
			} else {
				m.addAttribute("confirmperiod", "false");
			}
			m.addAttribute("userprofile", user);
		} catch (Exception e) {
			logger.error("Exception in MSDV---->"+e.getMessage());
		}
	}

}
