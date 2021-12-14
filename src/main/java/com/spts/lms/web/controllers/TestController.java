package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.MultipleDBReference;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.OfflineTest;
import com.spts.lms.beans.test.StudentOfflineTest;
import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.beans.test.StudentQuestionResponseAudit;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.beans.test.TestPool;
import com.spts.lms.beans.test.TestPoolConfiguration;
import com.spts.lms.beans.test.TestQuestion;
import com.spts.lms.beans.test.TestQuestionPools;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.course.CourseDAO;
import com.spts.lms.daos.test.StudentTestDAO;
import com.spts.lms.daos.test.TestDAO;
import com.spts.lms.daos.test.TestPoolDAO;
import com.spts.lms.daos.test.TestQuestionDAO;
import com.spts.lms.daos.test.TestQuestionPoolsDAO;
import com.spts.lms.daos.user.UserCourseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.OfflineTestService;
import com.spts.lms.services.test.StudentOfflineTestService;
import com.spts.lms.services.test.StudentQuestionResponseAuditService;
import com.spts.lms.services.test.StudentQuestionResponseService;
import com.spts.lms.services.test.StudentTestAuditService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestConfigurationService;
import com.spts.lms.services.test.TestPoolConfigurationService;
import com.spts.lms.services.test.TestPoolService;
import com.spts.lms.services.test.TestQuestionPoolsService;
import com.spts.lms.services.test.TestQuestionService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

//@Secured({ "ROLE_FACULTY", "ROLE_CORD", "ROLE_AR" })
@Controller
public class TestController extends BaseController {

	HttpSession session;
	@Autowired
	ApplicationContext act;

	@Autowired
	TestService testService;

	@Autowired
	UserService userService;
	@Autowired
	ProgramService programService;

	@Autowired
	TestQuestionService testQuestionService;

	@Autowired
	OfflineTestService offlineTestService;

	@Autowired
	StudentOfflineTestService studentOfflineTestService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	StudentParentService studentParentService;

	@Autowired
	Notifier notifier;

	@Autowired
	StudentQuestionResponseService studentQuestionResponseService;

	@Autowired
	StudentTestAuditService studentTestAuditService;

	@Autowired
	TestPoolService testPoolService;

	@Autowired
	TestQuestionPoolsService testQuestionPoolsService;

	@Autowired
	StudentQuestionResponseAuditService studentQuestionResponseAuditService;

	@Autowired
	TestConfigurationService testConfigurationService;

	@Autowired
	CourseDAO courseDAO;

	@Autowired
	TestDAO testDAO;

	@Autowired
	TestQuestionDAO testQuestionDAO;

	@Autowired
	UserCourseDAO userCourseDAO;

	@Autowired
	StudentTestDAO studentTestDAO;

	@Autowired
	TestPoolDAO testPoolDAO;

	@Autowired
	TestQuestionPoolsDAO testQuestionPoolsDAO;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${sendAlertsToParents}")
	private String sendAlertsToParents;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Value("${spring.datasource.url}")
	String defaultUrl;

	@Value("${database.username}")
	String defaultUsername;

	@Value("${database.password}")
	String defaultPassword;

	@Value("${app}")
	private String app;

	@Value("${file.base.directory}")
	private String baseDir;

	@Value("${file.base.directory.test}")
	private String testBaseDir;

	@Autowired
	LmsVariablesService lmsVariablesService;

	@Autowired
	TestPoolConfigurationService testPoolConfigurationService;
	

	private static final Logger logger = Logger.getLogger(TestController.class);

	Client client = ClientBuilder.newClient();

	@ModelAttribute("courseList")
	public List<Course> getCourseList(Principal principal) {
		String username = principal.getName();
		Token userDetails = (Token) principal;
		return courseService.findByUserActive(username, userDetails.getProgramName());
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTestForm(@ModelAttribute Test test, Model m, Principal principal,
			@RequestParam(required = false) String courseId, RedirectAttributes redirectAttrs) {

		try {
			String username = principal.getName();
			Token userDetails = (Token) principal;

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			m.addAttribute("username", username);

			m.addAttribute("webPage", new WebPage("test", "Create Test", false, false));

			if (StringUtils.isEmpty(courseId) && test.getId() == null) {
				return "redirect:/addTestFromMenu";
			}
			if (!StringUtils.isEmpty(courseId) && test.getId() == null) {
				m.addAttribute("courseId", courseId);
				Course course = courseService.findByID(Long.valueOf(courseId));
				m.addAttribute("courseIdVal", courseId);
				m.addAttribute("course", course);

			}

			if (test.getId() != null) {

				Test testFromDb = testService.findByID(test.getId());
				int chkStartDateForUpdate = testService.chkStartDateForUpdate(test.getId());

				String role = "";

				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					role = "ROLE_ADMIN";
				} else {
					role = "ROLE_FACULTY";
				}

				Test testDB = testService.findByIDAndFaculty(test.getId(), username, role);
				if (testDB == null) {
					setError(redirectAttrs, "Test " + testFromDb.getTestName() + " Can't be updated by faculty "
							+ username + "(Access Denied)");
					test.setId(null);
					return "redirect:/testList";
				} else if (chkStartDateForUpdate == 0) {
					List<TestQuestion> qList = testQuestionService.findByTestId(test.getId());
					if (qList.isEmpty() || qList.size() == 0) {
						LMSHelper.copyNonNullFields(test, testDB);
						m.addAttribute("edit", "true");
					} else {
						setError(redirectAttrs, "cannot update test: " + testFromDb.getTestName() + " already started");
						test.setId(null);
						return "redirect:/testList";
					}
				} else {
					LMSHelper.copyNonNullFields(test, testDB);
					m.addAttribute("edit", "true");
				}
				List<UserCourse> faculty = userCourseService.findAllFacultyWithCourseId(testFromDb.getCourseId(),
						testFromDb.getAcadMonth(), testFromDb.getAcadYear());
				m.addAttribute("faculties", faculty);
				Course course = courseService.findByID(testFromDb.getCourseId());

				m.addAttribute("course", course);
				m.addAttribute("testFromDb", testFromDb);

			}

			m.addAttribute("test", test);

			test.setIdOfCourse(String.valueOf(test.getCourseId()));
			m.addAttribute("idOfCourse", test.getIdOfCourse());

			m.addAttribute("allCourses", courseService.findByUserActive(username, userDetails.getProgramName()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttrs.addFlashAttribute("test", test);
			setError(redirectAttrs, "Error in adding Test");

			return "test/addTest";
		}
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		return "test/addTest";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestFromMenu", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTestFromMenu(@ModelAttribute Test test, Model m, Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (request.getSession().getAttribute("courseRecord") == null
				|| request.getSession().getAttribute("courseRecord").equals("")) {

		} else {
			request.getSession().removeAttribute("courseRecord");
		}

		m.addAttribute("webPage", new WebPage("test", "Create Test", false, false));

		if (test.getId() != null) {
			String role = "";

			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				role = "ROLE_ADMIN";
			} else {
				role = "ROLE_FACULTY";
			}
			Test testDB = testService.findByIDAndFaculty(test.getId(), username, role);
			if (testDB == null) {
				setError(m, "Test " + test.getId() + " does not exist");
				test.setId(null);
			} else {
				LMSHelper.copyNonNullFields(test, testDB);
				m.addAttribute("edit", "true");
			}
		}
		m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		return "test/addTest";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTest", method = RequestMethod.POST)
	public String addTest(@ModelAttribute Test test, RedirectAttributes redirectAttrs, Principal principal,
			@RequestParam(required = false) Long courseId, Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("username", username);
		try {
			if (courseId != null) {
				test.setCourseId(courseId);
			}
			if (test.getIdOfCourse() != null) {
				courseId = Long.valueOf(test.getIdOfCourse());
				test.setCourseId(courseId);
				test.setCourse(courseService.findByID(Long.valueOf(test.getIdOfCourse())));
			}
			/* New Audit changes start */
			HtmlValidation.validateHtml(test, Arrays.asList("testDescription"));
			BusinessBypassRule.validateAlphaNumeric(test.getTestName());
			Course course = courseService.findByID(test.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(test.getFacultyId(), String.valueOf(test.getCourseId()));
			if(null == userccourse) {
				throw new ValidationException("Invalid faculty selected.");
			}
			Utils.validateStartAndEndDates(test.getStartDate(), test.getEndDate());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxScore());
			BusinessBypassRule.validateNumericNotAZero(test.getDuration());
			BusinessBypassRule.validateNumericNotAZero(test.getPassScore());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxAttempt());
			BusinessBypassRule.validateYesOrNo(test.getRandomQuestion());
			BusinessBypassRule.validateYesOrNo(test.getSameMarksQue());
			if ("Y".equals(test.getRandomQuestion())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMaxQuestnToShow());
			}
			if ("Y".equals(test.getSameMarksQue())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMarksPerQue());
			}
			
			if("Mix".equals(test.getTestType())) {
				BusinessBypassRule.validateNumeric(test.getMaxQuestnToShow());
				BusinessBypassRule.validateNumeric(test.getMaxDesQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxImgQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxMcqQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxRngQueToShow());
			}
			if(test.getMaxScore() < test.getPassScore()) {
				redirectAttrs.addAttribute("testId", test.getId());
				redirectAttrs.addAttribute("courseId", test.getCourseId());
				setError(redirectAttrs, "Passing score should not be greater than total score.");
				return "redirect:/createTestForm";
			}
			/* New Audit changes end */
			if ("Y".equals(test.getRandomQuestion()) && "Y".equals(test.getSameMarksQue())) {
				double total = test.getMarksPerQue() * Double.valueOf(test.getMaxQuestnToShow());
				if (total != test.getMaxScore()) {
					redirectAttrs.addAttribute("testId", test.getId());
					redirectAttrs.addAttribute("courseId", test.getCourseId());
					setError(redirectAttrs, "Error in adding Test (Misconfigured values).");
					return "redirect:/createTestForm";
				}
			}
			test.setCreatedBy(username);
			test.setLastModifiedBy(username);

			if ("N".equals(test.getRandomQuestion())) {
				test.setMaxQuestnToShow("1");
				test.setMaxDesQueToShow("1");
				test.setMaxImgQueToShow("1");
				test.setMaxMcqQueToShow("1");
				test.setMaxRngQueToShow("1");
			} else if ("Y".equals(test.getRandomQuestion()) && !("Mix".equals(test.getTestType()))) {
				test.setMaxDesQueToShow("1");
				test.setMaxImgQueToShow("1");
				test.setMaxMcqQueToShow("1");
				test.setMaxRngQueToShow("1");
			}

			
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				test.setSendEmailAlertToParents("Y");
				test.setSendSmsAlertToParents("Y");
			}
			Course c = courseService.findByID(courseId);
			test.setAcadMonth(c.getAcadMonth());
			test.setAcadYear(Integer.valueOf(c.getAcadYear()));
			testService.insertWithIdReturn(test);
			redirectAttrs.addAttribute("testId", test.getId());
			redirectAttrs.addAttribute("courseId", test.getCourseId());
			redirectAttrs.addAttribute("groupId", test.getGroupId());

			// Create a folder with testId for storing student-test response in
			// file (changes on 11-10-2019) by akshay

			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getEndDate());

			String testDate = testSDate + "-" + testEDate;
			logger.info("testDate--->" + testDate);

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + test.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (!subFolderP.exists()) {
				subFolderP.mkdir();
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			setSuccess(redirectAttrs, "Test added successfully");

//			StudentTest facultyTest = new StudentTest();
//			facultyTest.setUsername(username);
//			facultyTest.setTestId(test.getId());
//			facultyTest.setCreatedBy(username);
//			facultyTest.setLastModifiedBy(username);
//			facultyTest.setGroupId(null);
//			facultyTest.setCourseId(test.getCourseId().toString());
//
//			studentTestService.insert(facultyTest);
			// 10-07-2020
			LmsVariables takeDemoTest = lmsVariablesService.getLmsVariableBykeyword("peerFaculty_takeDemoTest");
			String allocateFaculty = takeDemoTest.getValue();

			if (allocateFaculty.equals("Yes")) {
				logger.info("test.getIsPeerFacultyForDemo()---->" + test.getIsPeerFacultyForDemo());
				// List<UserCourse> facultyList =
				// userCourseService.findAllFacultyWithCourseId(test.getCourseId());
				if (test.getIsPeerFacultyForDemo().equals("Y") && !test.getPeerFacultiesForDemo().equals("")) {
					logger.info("if--->");
					List<String> facultyList = Arrays.asList(test.getPeerFacultiesForDemo().split(","));
					if (facultyList.contains(username)) {
						for (String uc : facultyList) {
							StudentTest facultyTest = new StudentTest();
							facultyTest.setUsername(uc);
							facultyTest.setTestId(test.getId());
							facultyTest.setCreatedBy(username);
							facultyTest.setLastModifiedBy(username);
							facultyTest.setGroupId(null);
							facultyTest.setCourseId(test.getCourseId().toString());

							studentTestService.insert(facultyTest);
						}
					} else {
						for (String uc : facultyList) {
							StudentTest facultyTest = new StudentTest();
							facultyTest.setUsername(uc);
							facultyTest.setTestId(test.getId());
							facultyTest.setCreatedBy(username);
							facultyTest.setLastModifiedBy(username);
							facultyTest.setGroupId(null);
							facultyTest.setCourseId(test.getCourseId().toString());

							studentTestService.insert(facultyTest);
						}

						StudentTest facultyTest = new StudentTest();
						facultyTest.setUsername(username);
						facultyTest.setTestId(test.getId());
						facultyTest.setCreatedBy(username);
						facultyTest.setLastModifiedBy(username);
						facultyTest.setGroupId(null);
						facultyTest.setCourseId(test.getCourseId().toString());

						studentTestService.insert(facultyTest);
					}
				} else {
					logger.info("else--->");
					StudentTest facultyTest = new StudentTest();
					facultyTest.setUsername(username);
					facultyTest.setTestId(test.getId());
					facultyTest.setCreatedBy(username);
					facultyTest.setLastModifiedBy(username);
					facultyTest.setGroupId(null);
					facultyTest.setCourseId(test.getCourseId().toString());

					studentTestService.insert(facultyTest);
				}
			} else {
				logger.info("else--->");
				StudentTest facultyTest = new StudentTest();
				facultyTest.setUsername(username);
				facultyTest.setTestId(test.getId());
				facultyTest.setCreatedBy(username);
				facultyTest.setLastModifiedBy(username);
				facultyTest.setGroupId(null);
				facultyTest.setCourseId(test.getCourseId().toString());

				studentTestService.insert(facultyTest);
			}

			if ("Y".equals(test.getRandomQuestion())) {
				if ("N".equals(test.getSameMarksQue())) {
					m.addAttribute("testConfiguration", new TestConfiguration());
					m.addAttribute("testId", test.getId());
					m.addAttribute("TotalScore", test.getMaxScore());
					m.addAttribute("maxQuestion", test.getMaxQuestnToShow());
					// New Pool Changes
					List<TestConfiguration> tcList = testConfigurationService.findAllByTestId(test.getId());
					List<TestPool> testPoolsList = testPoolService.findAllTestPoolsByUserAndCourse(username,
							String.valueOf(test.getCourseId()));
					m.addAttribute("testPoolsList", testPoolsList);
					List<TestPoolConfiguration> tpcList = testPoolConfigurationService.findAllByTestId(test.getId());
					if (null != tcList && tcList.size() > 0) {
						logger.info("weightageTypes--->questions");
						m.addAttribute("weightageTypes", "questions");
						m.addAttribute("TcList", tcList);
					} else if (null != tpcList && tpcList.size() > 0) {
						logger.info("weightageTypes--->poolQuestions");
						m.addAttribute("weightageTypes", "poolQuestions");
						m.addAttribute("TpcList", tpcList);
					} else {
						m.addAttribute("weightageTypes", "");
					}
					// New Pool Changes
					return "test/configureQuestionMarksForTest";
				}
			}

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			redirectAttrs.addAttribute("courseId", test.getCourseId());
			return "redirect:/createTestForm";
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			redirectAttrs.addAttribute("courseId", test.getCourseId());
			setError(redirectAttrs, "Error in adding Test");

			return "redirect:/createTestForm";
		}
		return "redirect:/viewTestDetails";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestConfiguration", method = RequestMethod.POST)
	public @ResponseBody String addTestConfiguration(@RequestParam(required = false) Long testId,
			@RequestBody(required = false) Map<String, String> hash, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		String username = principal.getName();
		Gson g = new Gson();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Test test = testService.findByID(Long.parseLong(hash.get("testId")));

		redirectAttrs.addAttribute("testId", test.getId());
		redirectAttrs.addAttribute("courseId", test.getCourseId());
		redirectAttrs.addAttribute("groupId", test.getGroupId());

		try {
			// New pool changes
			List<TestQuestion> testQuestionList = testQuestionService.findByTestId(test.getId());
			logger.info("testQuestionList---->" + testQuestionList.size());
			List<StudentTest> studentTestByTestId = studentTestService.findStudentTestForDelete(test.getId());
			if (testQuestionList.size() > 0) {

				for (TestQuestion tq : testQuestionList) {
					studentQuestionResponseService.deleteFacultyTestResponse(String.valueOf(test.getId()),
							principal.getName());
					studentQuestionResponseAuditService.deleteFacultyTestResponseAudit(String.valueOf(test.getId()),
							principal.getName());
					studentTestService.removeStudentQRespFile(test.getId());
					studentTestService.removeStudentQueResp(test.getId());
					testQuestionService.delete(tq);
				}
				String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getStartDate());
				String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getEndDate());
				String testDate = testSDate + "-" + testEDate;

				String rootFolder = testBaseDir + "/" + app;
				File folderR = new File(rootFolder);
				folderR.setExecutable(true, false);
				folderR.setWritable(true, false);
				folderR.setReadable(true, false);

				String folderPath = testBaseDir + "/" + app + "/" + "Tests";
				File folderP = new File(folderPath);
				if (!folderP.exists()) {
					folderP.mkdirs();

					logger.info("folder created");
				}
				folderP.setExecutable(true, false);
				folderP.setWritable(true, false);
				folderP.setReadable(true, false);

				String subFolderPath = folderPath + "/" + test.getId() + "-" + testDate;
				logger.info("Folder------->" + subFolderPath);
				File subFolderP = new File(subFolderPath);
				if (subFolderP.exists()) {
					try {
						logger.info("Delete Folder------->");
						FileUtils.deleteDirectory(subFolderP);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("Error--->", e);
					}
				}
				studentTestService.deleteBatch(studentTestByTestId);
			}
			// New pool changes

			List<TestConfiguration> testCongfigList = new ArrayList<>();

			testConfigurationService.deleteByTestId(hash.get("testId"));
			// New pool changes
			testPoolConfigurationService.deleteByTestId(String.valueOf(test.getId()));
			// New pool changes

			for (Map.Entry<String, String> entry : hash.entrySet()) {

				if (!"testId".equals(entry.getKey())) {

					TestConfiguration testConfig = new TestConfiguration();

					testConfig.setTestId(Long.parseLong(hash.get("testId")));
					testConfig.setMarks(Double.parseDouble(entry.getKey()));
					testConfig.setNoOfQuestion(Integer.parseInt(entry.getValue()));
					testConfig.setCreatedBy(username);
					testConfig.setLastModifiedBy(username);
					testCongfigList.add(testConfig);

				}
			}

			testConfigurationService.insertBatch(testCongfigList);

			return "viewTestDetails";

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test");
			redirectAttrs.addFlashAttribute("test", test);
			return "redirect:/createTestFrom";
		}

	}

	public List<StudentTest> createStudentTestList(List<UserCourse> userCourseListByCourseId, Test testDB,
			String createdBy) {

		List<StudentTest> studentTestList = new ArrayList<>();

		for (UserCourse uc : userCourseListByCourseId) {

			StudentTest st = new StudentTest();

			st.setUsername(uc.getUsername());
			st.setTestId(testDB.getId());
			st.setCreatedBy(createdBy);
			st.setLastModifiedBy(createdBy);
			st.setCourseId(String.valueOf(uc.getCourseId()));

			studentTestList.add(st);
		}

		return studentTestList;
	}

	public StudentTest createFacultyTestList(Test testDB, String createdBy) {

		StudentTest st = new StudentTest();

		st.setUsername(createdBy);
		st.setTestId(testDB.getId());
		st.setCreatedBy(createdBy);
		st.setLastModifiedBy(createdBy);
		st.setCourseId(String.valueOf(testDB.getCourseId()));

		return st;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/updateTest", method = RequestMethod.POST)
	public String updateTest(@ModelAttribute Test test, RedirectAttributes redirectAttrs, Principal principal,
			Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttrs.addAttribute("testId", test.getId());
		Test oldTest = testService.findByID(test.getId());
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(test, Arrays.asList("testDescription"));
			BusinessBypassRule.validateAlphaNumeric(test.getTestName());
			Course course = courseService.findByID(test.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(test.getFacultyId(), String.valueOf(test.getCourseId()));
			if(null == userccourse) {
				throw new ValidationException("Invalid faculty selected.");
			}
			Utils.validateStartAndEndDates(test.getStartDate(), test.getEndDate());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxScore());
			BusinessBypassRule.validateNumericNotAZero(test.getDuration());
			BusinessBypassRule.validateNumericNotAZero(test.getPassScore());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxAttempt());
			BusinessBypassRule.validateYesOrNo(test.getRandomQuestion());
			BusinessBypassRule.validateYesOrNo(test.getSameMarksQue());
			if ("Y".equals(test.getRandomQuestion())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMaxQuestnToShow());
			}
			if ("Y".equals(test.getSameMarksQue())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMarksPerQue());
			}

			if("Mix".equals(test.getTestType())) {
				BusinessBypassRule.validateNumeric(test.getMaxQuestnToShow());
				BusinessBypassRule.validateNumeric(test.getMaxDesQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxImgQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxMcqQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxRngQueToShow());
			}
			if(test.getMaxScore() < test.getPassScore()) {
				redirectAttrs.addAttribute("testId", test.getId());
				redirectAttrs.addAttribute("courseId", test.getCourseId());
				setError(redirectAttrs, "Passing score should not be greater than total score.");
				return "redirect:/createTestForm";
			}
			/* New Audit changes end */
			if ("Y".equals(test.getRandomQuestion()) && "Y".equals(test.getSameMarksQue())) {
				double total = test.getMarksPerQue() * Double.valueOf(test.getMaxQuestnToShow());
				if (total != test.getMaxScore()) {
					redirectAttrs.addAttribute("testId", test.getId());
					redirectAttrs.addAttribute("courseId", test.getCourseId());
					setError(redirectAttrs, "Error in updating Test (Misconfigured values).");
					return "redirect:/addTestForm";
				}
			}
			System.out.println("testId------------>" + test.getId());
			Test testFromDb = testService.findByID(test.getId());
			System.out.println("courseId t------------>" + test.getCourseId());
			test.setCreatedBy(testFromDb.getCreatedBy());
			test.setCreatedDate(testFromDb.getCreatedDate());
			test.setLastModifiedBy(username);
			System.out.println("courseId------------>" + test.getCourseId());
			Course c = courseService.findByID(test.getCourseId());

			test.setAcadMonth(c.getAcadMonth());
			test.setAcadYear(Integer.valueOf(c.getAcadYear()));

			List<StudentTest> studentTestDB = studentTestService.findOneTest(test.getId());
			List<TestQuestion> questionLIst = testQuestionService.findByTestId(test.getId());
			double sumOfTestQuestionMarks = 0.0;
			if (!"Y".equals(test.getRandomQuestion())) {
				if (questionLIst.size() > 0) {
					sumOfTestQuestionMarks = testQuestionService.getSumOfTestQuestionMarksByTestId(test.getId());
				}
				if (test.getMaxScore() == sumOfTestQuestionMarks) {

					if (!"Y".equals(test.getAutoAllocateToStudents())) {
						m.addAttribute("showProceed", true);
					} else {
						if (studentTestDB.size() == 0) {
							autoAllocateStudent(test, username);
						}
						m.addAttribute("showStudents", true);
					}
				}
			} else {
				if (questionLIst.size() > Integer.parseInt(test.getMaxQuestnToShow())) {
					if (!"Y".equals(test.getAutoAllocateToStudents())) {
						m.addAttribute("showProceed", true);
					} else {
						if (studentTestDB.size() == 0) {
							autoAllocateStudent(test, username);
						}
						m.addAttribute("showStudents", true);
					}

				}
			}

			if ("N".equals(test.getIsPasswordForTest())) {
				test.setPasswordForTest(null);
			}

			logger.info("test--->" + test);
			logger.info("oldTest--->" + oldTest);
			if (test.getMaxScore() != oldTest.getMaxScore()
					|| !test.getMaxQuestnToShow().equals(oldTest.getMaxQuestnToShow())
					|| !test.getSameMarksQue().equals(oldTest.getSameMarksQue())
					|| !test.getRandomQuestion().equals(oldTest.getRandomQuestion())
					|| test.getMarksPerQue() != oldTest.getMarksPerQue()
					|| !test.getEndDate().equals(testFromDb.getEndDate())
					|| !test.getStartDate().equals(testFromDb.getStartDate())) {
				List<StudentTest> studentTestList = studentTestService.findByTestId(test.getId());
				testPoolConfigurationService.deleteByTestId(String.valueOf(test.getId()));
				testConfigurationService.deleteByTestId(String.valueOf(test.getId()));
				if (studentTestList.size() > 0) {
					if (!test.getEndDate().equals(testFromDb.getEndDate())
							|| !test.getStartDate().equals(testFromDb.getStartDate())) {
						String changeTestFolderPath = changeTestFolder(test, testFromDb);
						if ("ERROR".equals(changeTestFolderPath)) {
							setError(redirectAttrs, "Error in updating test, updated folder cannot be created");
							return "redirect:/addTestForm";
						}
					} else {
						if (!"Y".equals(test.getRandomQuestion())) {
							if (questionLIst.size() > 0) {
								sumOfTestQuestionMarks = testQuestionService
										.getSumOfTestQuestionMarksByTestId(test.getId());
							}
							if (test.getMaxScore() == sumOfTestQuestionMarks) {
								logger.info("reAllocateStudent from update Test--->");
								reAllocateStudent(test);
							} else if (test.getMaxScore() <= sumOfTestQuestionMarks) {
								test.setRandomQuestion(oldTest.getRandomQuestion());
								setNote(redirectAttrs,
										"More than required questions are alerady configured, So changes will not be reflect.");
							}
						} else {
							if (questionLIst.size() > Integer.parseInt(test.getMaxQuestnToShow())) {
								reAllocateStudent(test);
							}
						}
					}
				}
			}

			if (testService.update(test) > 0)
				setSuccess(redirectAttrs, "Test updated successfully");
			else
				setError(redirectAttrs, "Test cannot be updated");

			if ("Y".equals(test.getRandomQuestion())) {
				if ("N".equals(test.getSameMarksQue())) {
					m.addAttribute("testConfiguration", new TestConfiguration());
					m.addAttribute("testId", test.getId());
					m.addAttribute("edit", "Y");
					m.addAttribute("TotalScore", test.getMaxScore());
					m.addAttribute("maxQuestion", test.getMaxQuestnToShow());

//					List<TestConfiguration> tcList = testConfigurationService.findAllByTestId(test.getId());
//					m.addAttribute("TcList", tcList);
					// New Pool Changes
					List<TestConfiguration> tcList = testConfigurationService.findAllByTestId(test.getId());
					List<TestPool> testPoolsList = testPoolService.findAllTestPoolsByUserAndCourse(username,
							String.valueOf(test.getCourseId()));
					m.addAttribute("testPoolsList", testPoolsList);
					List<TestPoolConfiguration> tpcList = testPoolConfigurationService.findAllByTestId(test.getId());
					if (null != tcList && tcList.size() > 0) {
						logger.info("weightageTypes--->questions");
						m.addAttribute("weightageTypes", "questions");
						m.addAttribute("TcList", tcList);
					} else if (null != tpcList && tpcList.size() > 0) {
						logger.info("weightageTypes--->poolQuestions");
						m.addAttribute("weightageTypes", "poolQuestions");
						m.addAttribute("TpcList", tpcList);
					} else {
						m.addAttribute("weightageTypes", "");
					}
					// New Pool Changes
					return "test/configureQuestionMarksForTest";
				}
			}

		}catch(ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			redirectAttrs.addFlashAttribute("test", test);
			return "redirect:/createTestForm";
		}  catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test");
			redirectAttrs.addFlashAttribute("test", test);
			return "redirect:/createTestForm";
		}
		return "redirect:/viewTestDetails";
	}

	public String changeTestFolder(Test testBean, Test testFromDB) {
		// Create a folder with testId

		try {
			String testOldSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDB.getStartDate());
			String testOldEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDB.getEndDate());

			String testNewSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testBean.getStartDate());
			String testNewEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testBean.getEndDate());

			String oldTestDate = testOldSDate + "-" + testOldEDate;
			String updatedTestDate = testNewSDate + "-" + testNewEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String oldSubFolderPath = folderPath + "/" + testFromDB.getId() + "-" + oldTestDate;
			String updatedSubFolderPath = folderPath + "/" + testFromDB.getId() + "-" + updatedTestDate;

			File oldSFolderP = new File(oldSubFolderPath);
			if (oldSFolderP.exists()) {
				FileUtils.deleteDirectory(oldSFolderP);
			}
			File updatedSFolderP = new File(updatedSubFolderPath);
			if (!updatedSFolderP.exists()) {
				updatedSFolderP.mkdir();
			}
			updatedSFolderP.setExecutable(true, false);
			updatedSFolderP.setWritable(true, false);
			updatedSFolderP.setReadable(true, false);

			List<StudentTest> studentTestList = studentTestService.findByTestId(testBean.getId());

			if (null != studentTestList && studentTestList.size() > 0) {

				studentTestService.updateStudentTestResponseFilePathByTestId(testBean.getId());
				studentTestService.deleteStudentTestResponseByTestId(testBean.getId());
				studentTestService.allocateTestQuestionsForAllStudent(testBean.getId(), updatedSubFolderPath);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			return "ERROR";
		}
		return "SUCCESS";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/uploadTestQuestionForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadTestQuestionForm(Model m, Principal principal, @ModelAttribute Test test,
			RedirectAttributes redirectAttributes, @RequestParam(required = false) String courseId,
			@RequestParam(required = false) String type) {
		m.addAttribute("webPage", new WebPage("uploadTestQuestion", "Upload Test Question", false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (test.getId() != null) {
			int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(test.getId());

			if (chkStartDateToConfigQuestn == 0) {
				redirectAttributes.addAttribute("testId", test.getId());
				setError(redirectAttributes, "Cannot upload test questions, test has already started");
				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/viewTestDetailsByAdmin";
				} else {
					return "redirect:/viewTestDetails";
				}
			}
		}

		if (courseId == null) {

		} else {
			test.setCourseId(Long.valueOf(courseId));

		}
		if (type == null) {

		} else {
			test.setTestType(type);
		}
		if (test.getId() == null) {
			List<Test> testListOfFaculty = testService.findtestByFacultyAndCourseAndType(username, courseId, type);
			if (testListOfFaculty.size() == 0 || testListOfFaculty.isEmpty()) {
				setNote(m, " no test available ");

			}
			m.addAttribute("testListOfFaculty", testListOfFaculty);
		}

		if (test.getId() != null) {

			Test testDB = testService.findByID(test.getId());
			test.setTestType(testDB.getTestType());
		} else {

		}
		m.addAttribute("test", test);
		m.addAttribute("testId", test.getId());
		m.addAttribute("showProceed", false);
		m.addAttribute("showStudents", false);
		return "test/uploadTestQuestion";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/uploadTestQuestion", method = { RequestMethod.POST })
	public String uploadTestQuestion(@ModelAttribute Test test, @RequestParam("file") MultipartFile file, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		m.addAttribute("webPage", new WebPage("test", "Upload Test Question", true, false));
		List<String> validateHeaders = null;
		Test testDb = testService.findByID(test.getId());
		test.setTestType(testDb.getTestType());

		List<TestQuestion> testQuestionList = testQuestionService.findByTestId(test.getId());
//		List<StudentTest> studentTestByTestId = studentTestService.findOneTest(test.getId());
//		if (testQuestionList.size() > 0) {
//
//			for (TestQuestion tq : testQuestionList) {
//				studentQuestionResponseService.deleteFacultyTestResponse(String.valueOf(test.getId()),
//						principal.getName());
//				studentQuestionResponseAuditService.deleteFacultyTestResponseAudit(String.valueOf(test.getId()),
//						principal.getName());
//				testQuestionService.delete(tq);
//			}
//			studentTestService.deleteBatch(studentTestByTestId);
//		}
		List<StudentTest> studentTestByTestId = studentTestService.findStudentTestForDelete(test.getId());
		if (testQuestionList.size() > 0) {

			for (TestQuestion tq : testQuestionList) {
				studentQuestionResponseService.deleteFacultyTestResponse(String.valueOf(test.getId()),
						principal.getName());
				studentQuestionResponseAuditService.deleteFacultyTestResponseAudit(String.valueOf(test.getId()),
						principal.getName());
				studentTestService.removeStudentQRespFile(testDb.getId());
				studentTestService.removeStudentQueResp(testDb.getId());
				testQuestionService.delete(tq);
			}
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();

				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testDb.getId() + "-" + testDate;
			logger.info("Folder------->" + subFolderPath);
			File subFolderP = new File(subFolderPath);
			if (subFolderP.exists()) {
				try {
					logger.info("Delete Folder------->");
					FileUtils.deleteDirectory(subFolderP);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Error--->", e);
				}
			}
			studentTestService.deleteBatch(studentTestByTestId);
		}
		if ("Objective".equals(testDb.getTestType())) {
			validateHeaders = new ArrayList<String>(Arrays.asList("description", "marks", "type", "questionType",
					"Options Shuffle Required", "option1", "option2", "option3", "option4", "option5", "option6",
					"option7", "option8", "answerRangeFrom", "answerRangeTo", "correctOption"));
		} else {
			validateHeaders = new ArrayList<String>(Arrays.asList("description", "marks"));
		}

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		ExcelReader excelReader = new ExcelReader();

		List<Test> testListOfFaculty = testService.findtestByFacultyAndCourseAndType(username,
				String.valueOf(test.getCourseId()), test.getTestType());
		m.addAttribute("testListOfFaculty", testListOfFaculty);

		try {
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);

			if ("Objective".equals(testDb.getTestType())) {
				if (maps.size() == 0) {
					setNote(m, "Excel File is empty");
				} else if (maps.size() < Integer.parseInt(testDb.getMaxQuestnToShow())
						&& "Y".equals(testDb.getRandomQuestion())) {
					setNote(m, "Number of questions must be greater than the maximum questions to show in test");

				} else {

					Map<String, Object> map = maps.get(0);
					if (map.get("Error") != null) {

						setNote(m, "Error--->" + map.get("Error"));
						return "test/uploadTestQuestion";
					}
					if (org.apache.commons.lang.StringUtils.isBlank(map.get("marks").toString())
							|| "0".equals(map.get("marks").toString())) {
						setNote(m, "Error: Marks field is blank/Zero");
						return "test/uploadTestQuestion";
					}
					String marks = (String) map.get("marks");
					double firstQuestionmarks = Double.parseDouble(marks);
					double totalMarks = 0.0;
					double totalMarksForNonRandomTest = 0.0;

					for (Map<String, Object> mapper : maps) {

						if (org.apache.commons.lang.StringUtils.isBlank(mapper.get("marks").toString())
								|| "0".equals(map.get("marks").toString())) {

							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
							setNote(m, "Error: Marks field is blank/Zero");
							return "test/uploadTestQuestion";
						} else 
//							if (firstQuestionmarks != Double.valueOf((mapper.get("marks").toString()))
//								&& "Y".equals(testDb.getRandomQuestion())) {
//
//							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
//							setNote(m, "Error: Please allocate same marks to all questions");
//							return "test/uploadTestQuestion";
//
//						} else
							if (("Y".equals(testDb.getRandomQuestion()) || "N".equals(testDb.getRandomQuestion()))
								&& "Y".equals(testDb.getSameMarksQue())
								&& testDb.getMarksPerQue() != Double.valueOf((mapper.get("marks").toString()))) {

							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
							setNote(m,
									"Error: Please allocate same marks to all questions Which is mention while creating test.");
							return "test/uploadTestQuestion";

						} else if (mapper.get("Error") != null) {
							logger.info("Error exist" + mapper.get("Error"));
							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
							setNote(m, "Error ");
							return "test/uploadTestQuestion";

						} else {

							if ("Numeric".equals((String) mapper.get("questionType"))) {

								String answerRF = (String) mapper.get("answerRangeFrom");
								String answerRT = (String) mapper.get("answerRangeTo");
								String answerCO = (String) mapper.get("correctOption");
								double answerRangeFrom = Double.valueOf(answerRF);
								double answerRangeTo = Double.valueOf(answerRT);
								double correctOption = Double.valueOf(answerCO);

								if (answerRangeFrom > correctOption || answerRangeTo < correctOption
										|| answerRangeFrom > answerRangeTo) {
									testQuestionService.deleteByTestId(String.valueOf(test.getId()));
									setError(m,

											"Answer Range Given in Numeric Type is not valid  "

													+ ", Correct Option has to be between "
													+ " answer-range-from and answer-range-to & answer-range-from field has to be less "
													+ " than answer-range-to field ");

									return "test/uploadTestQuestion";

								}
							}

							if ("Y".equals(testDb.getRandomQuestion()) && "N".equals(testDb.getSameMarksQue())) {

								List<TestConfiguration> testConfigList = testConfigurationService
										.findAllByTestId(testDb.getId());

								boolean check = testConfigList.stream()
										.filter(o -> o.getMarks() == Double.parseDouble(mapper.get("marks").toString()))
										.findFirst().isPresent();

								if (!check) {

									testQuestionService.deleteByTestId(String.valueOf(test.getId()));
									setNote(m,
											"Error: Please allocate same set of marks to all questions Which is mention while creating test.");
									return "test/uploadTestQuestion";
								}
							}

							for (int i = 1; i <= 8; i++) {

								String opt = "option" + i;

								if ("true".equalsIgnoreCase(String.valueOf(mapper.get(opt)))) {

									mapper.put(opt, "True".toString());
								}
								if ("false".equalsIgnoreCase(String.valueOf(mapper.get(opt)))) {

									mapper.put(opt, "False".toString());
								}
							}

							String correctOption = (String) mapper.get("correctOption");
							String mark = (String) mapper.get("marks");
							totalMarksForNonRandomTest = totalMarksForNonRandomTest + Double.parseDouble(mark);
							if(correctOption.isEmpty()) {
								testQuestionService.deleteByTestId(String.valueOf(test.getId()));
								setError(m, "Please fill all Correct Option field.");
								return "test/uploadTestQuestion";
							}
							if (correctOption.contains(" ")) {

								correctOption = correctOption.replaceAll("\\s", "");

								mapper.put("correctOption", correctOption);
							}
							TestQuestion testQuestion = new TestQuestion();
							testQuestion.setTestId(test.getId());
							testQuestion.setCreatedBy(username);
							testQuestion.setLastModifiedBy(username);
							testQuestion.setActive("Y");

							// Added By Akshay for optionShuffle

							testQuestion.setOptionShuffle((String) mapper.get("Options Shuffle Required"));

							mapper.put("optionShuffle", testQuestion.getOptionShuffle());

							// End

							mapper.put("testId", testQuestion.getTestId());
							mapper.put("createdBy", testQuestion.getCreatedBy());
							mapper.put("lastModifiedBy", testQuestion.getLastModifiedBy());
							mapper.put("testPoolId", null);

							mapper.put("active", testQuestion.getActive());

							mapper.put("createdDate", Utils.getInIST());
							mapper.put("lastModifiedDate", Utils.getInIST());
							testQuestionService.insertUsingMap(mapper);

						}
					}

					if (!"Y".equals(testDb.getRandomQuestion())) {
						if (testDb.getMaxScore() != totalMarksForNonRandomTest) {
							setError(m,
									"Sum of marks entered in excel does not match with totalScore entered while creating test."
											+ "total score of test is :" + testDb.getMaxScore());

							testQuestionService.deleteByTestId(String.valueOf(test.getId()));

							return "test/uploadTestQuestion";
						}
					}

					setSuccess(m,
							"test question file uploaded successfully!!! (Total Marks :" + testDb.getMaxScore() + ") ");
					if (!"Y".equals(testDb.getAutoAllocateToStudents())) {
						reAllocateStudent(testDb);
						m.addAttribute("showProceed", true);
					} else {
						autoAllocateStudent(testDb, username);
						m.addAttribute("showStudents", true);
					}

				}
			} else {
				if (maps.size() == 0) {
					setNote(m, "Excel File is empty");
				} else if (maps.size() < Integer.parseInt(testDb.getMaxQuestnToShow())) {
					setNote(m, "Number of questions must be greater than the maximum questions to show in test");

				} else {

					Map<String, Object> map = maps.get(0);
					if (map.get("Error") != null) {

						setNote(m, "Error--->" + map.get("Error"));
						return "test/uploadTestQuestion";
					}
					if (org.apache.commons.lang.StringUtils.isBlank(map.get("marks").toString())
							|| "0".equals(map.get("marks").toString())) {
						setNote(m, "Error: Marks field is blank/Zero");
						return "test/uploadTestQuestion";
					}
					double firstQuestionmarks = Double.parseDouble((String) map.get("marks"));

					double totalMarks = 0.0;
					double totalMarksForNonRandomTest = 0.0;

					for (Map<String, Object> mapper : maps) {
						if (org.apache.commons.lang.StringUtils.isBlank(mapper.get("marks").toString())
								|| "0".equals(map.get("marks").toString())) {

							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
							setNote(m, "Error: Marks field is blank/Zero");
							return "test/uploadTestQuestion";
						} else 
//							if (firstQuestionmarks != Double.valueOf((mapper.get("marks").toString()))
//								&& "Y".equals(testDb.getRandomQuestion())) {
//
//							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
//							setNote(m, "Error: Please allocate same marks to all questions");
//							return "test/uploadTestQuestion";
//
//						} else 
							if (("Y".equals(testDb.getRandomQuestion()) || "N".equals(testDb.getRandomQuestion()))
								&& "Y".equals(testDb.getSameMarksQue())
								&& testDb.getMarksPerQue() != Double.valueOf((mapper.get("marks").toString()))) {

							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
							setNote(m,
									"Error: Please allocate same marks to all questions Which is mention while creating test.");
							return "test/uploadTestQuestion";

						} else if (mapper.get("Error") != null) {
							logger.info("Error exist" + mapper.get("Error"));
							testQuestionService.deleteByTestId(String.valueOf(test.getId()));
							setNote(m, "Error ");
							return "test/uploadTestQuestion";

						} else {

							if ("Y".equals(testDb.getRandomQuestion()) && "N".equals(testDb.getSameMarksQue())) {

								List<TestConfiguration> testConfigList = testConfigurationService
										.findAllByTestId(testDb.getId());

								boolean check = testConfigList.stream()
										.filter(o -> o.getMarks() == Double.parseDouble(mapper.get("marks").toString()))
										.findFirst().isPresent();

								if (!check) {

									testQuestionService.deleteByTestId(String.valueOf(test.getId()));
									setNote(m,
											"Error: Please allocate same set of marks to all questions Which is mention while creating test.");
									return "test/uploadTestQuestion";
								}
							}

							String mark = (String) mapper.get("marks");
							totalMarksForNonRandomTest = totalMarksForNonRandomTest + Double.parseDouble(mark);

							TestQuestion testQuestion = new TestQuestion();
							testQuestion.setTestId(test.getId());
							testQuestion.setCreatedBy(username);
							testQuestion.setLastModifiedBy(username);
							testQuestion.setActive("Y");
							testQuestion.setType("null");

							mapper.put("testId", testQuestion.getTestId());
							mapper.put("createdBy", testQuestion.getCreatedBy());
							mapper.put("lastModifiedBy", testQuestion.getLastModifiedBy());
							com.spts.lms.web.utils.Utils.getInIST();
							mapper.put("active", testQuestion.getActive());
							mapper.put("createdDate", Utils.getInIST());
							mapper.put("lastModifiedDate", Utils.getInIST());
							mapper.put("type", null);
							mapper.put("option1", null);
							mapper.put("option2", null);
							mapper.put("option3", null);
							mapper.put("option4", null);
							mapper.put("option5", null);
							mapper.put("option6", null);
							mapper.put("option7", null);
							mapper.put("option8", null);
							mapper.put("optionShuffle", null);
							mapper.put("correctOption", null);
							mapper.put("questionType", "Descriptive");
							mapper.put("answerRangeFrom", null);
							mapper.put("answerRangeTo", null);
							mapper.put("testPoolId", null);
							testQuestionService.insertUsingMap(mapper);

						}
					}
					if (!"Y".equals(testDb.getRandomQuestion())) {
						if (testDb.getMaxScore() != totalMarksForNonRandomTest) {
							setError(m,
									"Sum of marks entered in excel does not match with totalScore entered while creating test."
											+ "total score of test is :" + testDb.getMaxScore());

							testQuestionService.deleteByTestId(String.valueOf(test.getId()));

							return "test/uploadTestQuestion";
						}
					}
					setSuccess(m,
							"test question file uploaded successfully !!! (Total Marks :" + testDb.getMaxScore() + ")");
					if (!"Y".equals(testDb.getAutoAllocateToStudents())) {
						reAllocateStudent(testDb);
						m.addAttribute("showProceed", true);
					} else {
						m.addAttribute("showStudents", true);
						autoAllocateStudent(testDb, username);
					}

				}
			}
			m.addAttribute("test", test);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}
		m.addAttribute("testId", test.getId());
		return "test/uploadTestQuestion";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestDetails", method = RequestMethod.GET)
	public String viewTestDetails(@RequestParam(required = false) Long testId, Model m,
			@RequestParam(required = false) String groupId, @RequestParam(required = false) Long campusId,
			@RequestParam(required = false) String courseId, Principal p, RedirectAttributes redirectAttrs) {
		m.addAttribute("webPage", new WebPage("viewTest", "Test Details", true, false));
		try {
			Token userdetails1 = (Token) p;
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {

				redirectAttrs.addAttribute("testId", testId);
				return "redirect:/viewTestDetailsByAdmin";
			}
			String username = p.getName();

			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
			m.addAttribute("allCampuses", userService.findCampus());
			Test testFromDb = testService.findByID(testId);
			m.addAttribute("test", testFromDb);
			// New Pool Changes
			m.addAttribute("testPoolConfig", false);
			List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService.findAllByTestId(testId);
			if (testPoolConfigList.size() > 0) {
				m.addAttribute("testPoolConfig", true);
			}
			// New Pool Changes
			StudentTest studentTest = studentTestService.findBytestIDAndUsername(testId, p.getName());

			int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(testId);
			List<TestQuestion> testQuestionsForTestId = new ArrayList<>();
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				testQuestionsForTestId = testQuestionService.findByTestId(testId);
				if (testQuestionsForTestId.size() == 0) {
					m.addAttribute("isTestQConfigured", false);
				}
				if (testQuestionsForTestId.size() > 0) {

					m.addAttribute("isTestQConfigured", true);
				}

				// commented By Akshay As Faculty will allocate student even
				// after test starts.

				/*
				 * if (chkStartDateToConfigQuestn == 0) { List<StudentTest> students = new
				 * ArrayList<>(); setNote(m, "Test has already started :" +
				 * testFromDb.getTestName());
				 * 
				 * if ((testQuestionsForTestId.size() > Integer
				 * 
				 * .parseInt(testFromDb.getMaxQuestnToShow()) && "Y"
				 * .equals(testFromDb.getRandomQuestion())) ||
				 * "N".equals(testFromDb.getRandomQuestion())) {
				 * 
				 * if (campusId != null) { testFromDb.setCampusId(campusId); students =
				 * studentTestService .getStudentForTestAndCampusId( testFromDb.getId(),
				 * testFromDb.getCourseId(), campusId);
				 * 
				 * } else {
				 * 
				 * students = studentTestService.getStudentForTest( testFromDb.getId(),
				 * testFromDb.getCourseId()); }
				 * 
				 * } m.addAttribute("id", testId); m.addAttribute("students", students);
				 * 
				 * return "test/testDetails"; }
				 */

				// End

				if (testQuestionsForTestId.isEmpty() && chkStartDateToConfigQuestn == 1) {

					setNote(m, "please configure question for test :" + testFromDb.getTestName());

					return "test/testDetails";
				}

				if (testQuestionsForTestId.size() > 0 && chkStartDateToConfigQuestn == 1) {
					if (testQuestionsForTestId.size() <= Integer.parseInt(testFromDb.getMaxQuestnToShow())
							&& "Y".equals(testFromDb.getRandomQuestion())) {
						
						setNote(m,
								"number of questions configured is not sufficient to allocate students, please configure more questions:"
										+ testFromDb.getTestName());

						return "test/testDetails";
					//New Pool Changes	
					}else {
						if ("Y".equals(testFromDb.getRandomQuestion()) && testPoolConfigList.size() > 0) {
							for(TestPoolConfiguration tpc :  testPoolConfigList) {
								List<TestQuestion> allocatedQuestion = testQuestionService.getQuestionsByPoolConfiguration(tpc.getTestId(), tpc.getTestPoolId(), tpc.getMarks());
								if(tpc.getNoOfQuestion() > allocatedQuestion.size()) {
									setNote(m,
											"number of questions configured is not sufficient to allocate students, please configure more questions:"
													+ testFromDb.getTestName());

									return "test/testDetails";
								}
							}
						}
					}
					//New Pool Changes
				}

			}

			List<StudentTest> students = new ArrayList<>();
			if ((testQuestionsForTestId.size() > Integer.parseInt(testFromDb.getMaxQuestnToShow())
					&& "Y".equals(testFromDb.getRandomQuestion()))
					|| "N".equals(testFromDb.getRandomQuestion()) && testQuestionsForTestId.size() > 0) {

				if ("Mix".equals(testFromDb.getTestType()) && "Y".equals(testFromDb.getRandomQuestion())) {

					List<TestQuestion> testQuestionDes = new ArrayList<>();
					List<TestQuestion> testQuestionMcq = new ArrayList<>();
					List<TestQuestion> testQuestionRng = new ArrayList<>();
					List<TestQuestion> testQuestionImg = new ArrayList<>();

					for (TestQuestion tq : testQuestionsForTestId) {

						if ("MCQ".equals(tq.getQuestionType())) {
							testQuestionMcq.add(tq);
						} else if ("Numeric".equals(tq.getQuestionType())) {
							testQuestionRng.add(tq);
						} else if ("Descriptive".equals(tq.getQuestionType())) {
							testQuestionDes.add(tq);
						} else if ("Image".equals(tq.getQuestionType())) {
							testQuestionImg.add(tq);
						}
					}

					if ((testQuestionDes.size() >= Integer.parseInt(testFromDb.getMaxDesQueToShow())
							&& testQuestionMcq.size() >= Integer.parseInt(testFromDb.getMaxMcqQueToShow())
							&& testQuestionRng.size() >= Integer.parseInt(testFromDb.getMaxRngQueToShow())
							&& testQuestionImg.size() >= Integer.parseInt(testFromDb.getMaxImgQueToShow())
							&& "Y".equals(testFromDb.getRandomQuestion()))) {

						students = studentTestService.getStudentForTest(testFromDb.getId(), testFromDb.getCourseId());

					} else {
						setNote(m, "Please Add Questions More Than Max Question Shown To Student for each type");
					}

				} else {

					students = studentTestService.getStudentForTest(testFromDb.getId(), testFromDb.getCourseId());
				}
				// }

			}
			m.addAttribute("noOfStudentAllocated", studentTestService.getNoOfStudentsAllocated(testId));

			m.addAttribute("students", students);
			m.addAttribute("groupId", groupId);
			m.addAttribute("courseId", courseId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Test");
			return "test/testDetails";
		}
		return "test/testDetails";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestDetailsToEvaluate", method = RequestMethod.GET)
	public String viewTestDetailsToEvaluate(@RequestParam(required = false) Long testId, Model m,
			@RequestParam(required = false) String groupId, @RequestParam(required = false) String courseId,
			Principal p) {
		m.addAttribute("webPage", new WebPage("viewTest", "Test Details", true, false));
		try {
			String username = p.getName();
			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;

			Test testFromDb = testService.findByID(testId);
			m.addAttribute("test", testFromDb);

			/*
			 * List<StudentTest> students =
			 * studentTestService.getStudentToEvaluate(testFromDb.getId(),
			 * testFromDb.getCourseId());
			 */

			List<StudentTest> students = new ArrayList<>();
			if ("Y".equals(testFromDb.getIsCreatedByAdmin())) {

				List<String> courseIds = courseService.courseListByParams(testFromDb.getModuleId(),
						String.valueOf(testFromDb.getAcadYear()), userdetails1.getProgramId(), p.getName());
				for (String s : courseIds) {
					students.addAll(studentTestService.getStudentToEvaluate(testFromDb.getId(), Long.valueOf(s)));
				}

			} else {
				students = studentTestService.getStudentToEvaluate(testFromDb.getId(), testFromDb.getCourseId());
			}

			m.addAttribute("testType", testFromDb.getTestType());
			m.addAttribute("students", students);
			m.addAttribute("groupId", groupId);
			m.addAttribute("courseId", courseId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Test");
			return "test/testDetails";
		}
		return "test/studentTestlistToEvaluate";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/saveStudentTest", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveStudentTest(@ModelAttribute Test test, Model m, RedirectAttributes redirectAttr,
			Principal principal) {
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		String username = principal.getName();
		studentList.add(username);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttr.addFlashAttribute("test", test);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		Test testFromDb = testService.findByID(test.getId());
		int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(test.getId());
		m.addAttribute("test", testFromDb);
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			List<TestQuestion> testQuestionsForTestId = testQuestionService.findByTestId(test.getId());

			if (testQuestionsForTestId.isEmpty()) {

				setNote(m, "please configure question for test :" + testFromDb.getTestName());

				return "test/testDetails";

			}

			// commented By Akshay as test can be allocated to students event
			// after end date of a test.

			/*
			 * else if (chkStartDateToConfigQuestn < 1) {
			 * 
			 * setNote(m, "Test has already started :" + testFromDb.getTestName());
			 * 
			 * return "test/testDetails"; }
			 */

			// End

			else if (testQuestionsForTestId.size() < Integer.parseInt(testFromDb.getMaxQuestnToShow())
					&& "Y".equals(testFromDb.getRandomQuestion())) {
				setNote(m,
						"number of questions configured is not sufficient to allocate students, please configure more questions:"
								+ testFromDb.getTestName());

				return "test/testDetails";
			}
		}

		try {
			for (StudentTest studentTest : test.getStudentTests()) {

				studentTest.setCreatedBy(username);
				studentTest.setLastModifiedBy(username);
				studentTest.setGroupId(test.getGroupId() == null ? "" : test.getGroupId());
				studentTest.setCourseId(test.getCourseId() == null ? "" : test.getCourseId() + "");

				studentList.add(studentTest.getUsername());
			}
			studentTestService.insertBatch(test.getStudentTests());

			// Code Here For Allocation of Test Questions...

			logger.info("file creation entered");
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDb.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDb.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testFromDb.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (!subFolderP.exists()) {
				subFolderP.mkdir();
				logger.info("subfolder created");
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			logger.info("subfolder created" + subFolderPath);

			studentTestService.allocateTestQuestionsForAllStudent(testFromDb.getId(), subFolderPath);

			try {
				Test retrive = testService.findByID((test.getId()));
				Course course = test.getCourseId() != null ? courseService.findByID(Long.valueOf(test.getCourseId()))
						: null;

				String subject = " Test with name " + testFromDb.getTestName();

				StringBuffer buff = new StringBuffer(subject);

				if (course != null) {
					buff.append(" for Course ");
					buff.append(course.getCourseName());
					buff.append(" is scheduled on " + retrive.getStartDate());
				}
				buff.append(" allocated to you ");
				subject = buff.toString();

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(retrive.getSendEmailAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}

					if ("Y".equals(retrive.getSendSmsAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}
					if ("Y".equals(retrive.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(retrive.getSendSmsAlert())) {

						if (result != null)
							result = userService.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}

			} catch (Exception e) {
				logger.error("Exception e", e);
			}
			setSuccess(redirectAttr, "Students Allocated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating test");
			redirectAttr.addAttribute("testId", test.getId());
			return "redirect:/viewTestDetails";
		}
		redirectAttr.addAttribute("testId", test.getId());
		return "redirect:/viewTestDetails";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/saveStudentTestAllStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveStudentTestAllStudents(@ModelAttribute Test test, Model m, RedirectAttributes redirectAttr,
			Principal principal) {
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		String username = principal.getName();
		studentList.add(username);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		redirectAttr.addFlashAttribute("test", test);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		Test testFromDb = testService.findByID(test.getId());
		m.addAttribute("test", testFromDb);
		int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(test.getId());

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			List<TestQuestion> testQuestionsForTestId = testQuestionService.findByTestId(test.getId());

			if (testQuestionsForTestId.isEmpty()) {

				setNote(m, "please configure question for test :" + testFromDb.getTestName());

				return "test/testDetails";
			} else if (chkStartDateToConfigQuestn < 1) {

				setNote(m, "Test has already started :" + testFromDb.getTestName());

				return "test/testDetails";
			} else if (testQuestionsForTestId.size() <= Integer.parseInt(testFromDb.getMaxQuestnToShow())
					&& "Y".equals(testFromDb.getRandomQuestion())) {
				setNote(m,
						"number of questions configured is not sufficient to allocate students, please configure more questions:"
								+ testFromDb.getTestName());

				return "test/testDetails";
			}

		}

		List<StudentTest> students = studentTestService.getStudentForTest(test.getId(), test.getCourseId());

		try {
			List<StudentTest> studentTestMappingList = new ArrayList<StudentTest>();
			for (StudentTest studentTest : students) {

				studentTest.setCreatedBy(username);
				studentTest.setLastModifiedBy(username);
				studentTest.setGroupId(test.getGroupId() == null ? "" : test.getGroupId());
				studentTest.setCourseId(test.getCourseId() == null ? "" : test.getCourseId() + "");
				studentTest.setTestId(test.getId());
				studentTest.setAttempt(0);
				studentList.add(studentTest.getUsername());
				List<StudentTest> studentAssList = studentTestService.getStudentUsername(studentTest.getTestId(),
						Long.valueOf(studentTest.getCourseId()));

				if (studentAssList.isEmpty()) {
					studentTestMappingList.add(studentTest);
				} else {
					List<String> names = new ArrayList<String>();

					for (StudentTest ass : studentAssList) {
						names.add(ass.getUsername());
					}

					if (!names.contains(studentTest.getUsername())) {
						studentTestMappingList.add(studentTest);
					}
				}

			}
			studentTestService.insertBatch(studentTestMappingList);

			// Code Here For Allocation of Test Questions...

			logger.info("file creation entered");
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDb.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDb.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testFromDb.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (!subFolderP.exists()) {
				subFolderP.mkdir();
				logger.info("subfolder created");
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			logger.info("subfolder created" + subFolderPath);

			studentTestService.allocateTestQuestionsForAllStudent(testFromDb.getId(), subFolderPath);

			try {
				Test retrive = testService.findByID((test.getId()));
				Course course = test.getCourseId() != null ? courseService.findByID(Long.valueOf(test.getCourseId()))
						: null;

				String subject = " Test with name " + retrive.getTestName();

				StringBuffer buff = new StringBuffer(subject);

				if (course != null) {
					buff.append(" for Course ");
					buff.append(course.getCourseName());
					buff.append(" is scheduled on " + retrive.getStartDate());

				}
				buff.append(" allocated to you ");
				subject = buff.toString();

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(retrive.getSendEmailAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}

					if ("Y".equals(retrive.getSendSmsAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}
					if ("Y".equals(retrive.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(retrive.getSendSmsAlert())) {

						if (result != null)
							result = userService.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}

			} catch (Exception e) {
				logger.error("Exception e", e);
			}
			setSuccess(redirectAttr, "Students Allocated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating test");
			redirectAttr.addAttribute("testId", test.getId());
			return "redirect:/viewTestDetails";
		}
		redirectAttr.addAttribute("testId", test.getId());
		return "redirect:/viewTestDetails";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_STUDENT"})
	@RequestMapping(value = "/getPasswordForTest", method = RequestMethod.POST)
	public @ResponseBody String getPasswordForTest(Principal p, @RequestParam String testId,
			@RequestParam String password, Model m) {

		try {
			Test testDB = testService.findByID(Long.valueOf(testId));

			if (testDB != null) {

				if (testDB.getPasswordForTest() != null) {
					if (testDB.getPasswordForTest().equalsIgnoreCase(password)) {
						return "SUCCESS";
					}
				}

				return "WARNING";
			} else {
				return "NOTE";
			}
		} catch (Exception e) {
			setError(m, "Error in matching password");
			return "ERROR";
		}
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getFacultyByCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyByCourse(@RequestParam(name = "courseId") String courseId,
			Principal principal) {

		String json = "";
		String username = principal.getName();

		List<UserCourse> faculty = userCourseService.findAllFacultyWithCourseId(Long.valueOf(courseId));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (UserCourse ass : faculty) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(ass.getUsername(), ass.getUsername());
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}
		return json;

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_HOD"})
	@RequestMapping(value = "/searchTest", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchTest(@RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
			@ModelAttribute Test test, Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("viewTest", "View Tests", true, false));

		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			test.setFacultyId(username);
			m.addAttribute("courselist", courseService.findByUserActive(username, userdetails1.getProgramName()));
		}
		try {

			Page<Test> page;
			Page<Test> pageAdmin;

			if (!courseId.isEmpty()) {
				Course courseDB = courseService.findByID(Long.valueOf(courseId));
				page = testService.findByFacultyAndCourse(username, Long.valueOf(courseId), pageNo, pageSize);
				pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
						Long.valueOf(userdetails1.getProgramId()), courseDB.getModuleId(), courseDB.getAcadYear(),
						pageNo, pageSize);
			} else {
				test.setMaxAttempt(null);
				page = testService.searchActiveByExactMatchReplacement(username,
						Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
				pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
						Long.valueOf(userdetails1.getProgramId()), null, null, pageNo, pageSize);
			}
			List<Test> programList = page.getPageItems();
			if (pageAdmin.getPageItems().size() > 0) {
				logger.info(pageAdmin + " found");
				programList.addAll(pageAdmin.getPageItems());
				page.setPageItems(pageAdmin.getPageItems());
				page.setRowCount(page.getPageItems().size());
			}
			for (Test t : programList) {
				Course c = courseService.findByID(t.getCourseId());
				t.setCourseName(c.getCourseName());
				programList.set(programList.indexOf(t), t);

			}

			m.addAttribute("page", page);
			m.addAttribute("programList", programList);
			m.addAttribute("q", getQueryString(test));

			if (programList == null || programList.size() == 0) {
				setNote(m, "No Tests found");
			}

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Test List");
		}
		m.addAttribute("courselist", courseService.findByUserActive(username, userdetails1.getProgramName()));
		return "test/searchTest";
	}

	/**
	 * Questions setup
	 */
	
	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestQuestionForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTestQuestionForm(@RequestParam Long id, Model m, Principal principal,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("addTestQuestion", "Add Questions To Test", true, false));

		String role = "";

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			role = "ROLE_ADMIN";
		} else {
			role = "ROLE_FACULTY";
		}

		Test test = testService.findByIDAndFaculty(id, username, role);
		Date currDate = Utils.getInIST();
		Date endDate = Utils.converFormatsDateAlt(test.getStartDate());
		   logger.info(currDate+"Now time");	
		   logger.info("endDate"+endDate);
		   
		   if(currDate.after(endDate)) {
			   logger.info("insode");
			   m.addAttribute("allocation", "N");   
		   }else 
		   {
			   m.addAttribute("allocation", "Y");
			   logger.info("else");
		   }
		StudentTest stList = studentTestService.findBytestIDAndUsername(id, username);
		if (test == null && stList != null) {
			test = testService.findByID(id);
		}
		int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(id);
		int checkbothDates = testService.checkDatesForUpdate(id);

		TestQuestion testQuestion = new TestQuestion();
		if (null != test) {
			if (checkbothDates == 1) {
				setNote(m, "test has already started");
				List<TestQuestion> testQuestionList = testQuestionService.findByTestId(id);
				List<TestQuestion> testQuestionListSub = new ArrayList<TestQuestion>();
				if (testQuestionList.size() == 0) {
					setError(redirectAttributes, "cannot configure question,test has already started");
					redirectAttributes.addAttribute("testId", id);
					if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
						return "redirect:/viewTestDetailsByAdmin";
					} else {
						return "redirect:/viewTestDetails";
					}
				} else {
					testQuestionListSub.addAll(testQuestionList);

					if (test.getTestType().equalsIgnoreCase("Subjective")) {
						m.addAttribute("testQuestionListSub", testQuestionListSub);
					} else {
						m.addAttribute("testQuestionList", testQuestionList);
					}
					m.addAttribute("testId", test.getId());
					return "test/viewTestQuestion";
				}
			} else if (chkStartDateToConfigQuestn == 1) {
				test.setTestQuestions(testQuestionService.findByTestId(id));
				testQuestion.setTestId(id);
			} else if (test.getShowResultsToStudents().equalsIgnoreCase("N")) {
				test.setTestQuestions(testQuestionService.findByTestId(id));
				testQuestion.setTestId(id);
				setNote(m, "test has already ended");
				m.addAttribute("disabled", true);
				 m.addAttribute("allocation", "N");
			} else if (test.getShowResultsToStudents().equalsIgnoreCase("Y")) {
				setNote(m, "test has already ended");
				List<TestQuestion> testQuestionList = testQuestionService.findByTestId(id);
				List<TestQuestion> testQuestionListSub = new ArrayList<TestQuestion>();
				if (testQuestionList.size() == 0) {
					setError(redirectAttributes, "cannot configure question,test has already started");
					redirectAttributes.addAttribute("testId", id);
					if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
						return "redirect:/viewTestDetailsByAdmin";
					} else {
						return "redirect:/viewTestDetails";
					}
				} else {
					testQuestionListSub.addAll(testQuestionList);

					if (test.getTestType().equalsIgnoreCase("Subjective")) {
						m.addAttribute("testQuestionListSub", testQuestionListSub);
					} else {
						m.addAttribute("testQuestionList", testQuestionList);
					}
					m.addAttribute("testId", test.getId());
					return "test/viewTestQuestion";
				}
			}

		} else {
			setError(m, "Test not found");
		}
		m.addAttribute("test", test);

		if ("Y".equals(test.getIsCreatedByAdmin())) {

			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				m.addAttribute("showButton", "false");
			} else {
				m.addAttribute("showButton", "true");
			}
		} else {
			m.addAttribute("showButton", "true");
		}

		testQuestion.setType("SINGLESELECT");
		m.addAttribute("testQuestion", testQuestion);

		double sumOfTestQuestionMarks = 0.0;

		if (test.getTestQuestions().size() > 0) {
			sumOfTestQuestionMarks = testQuestionService.getSumOfTestQuestionMarksByTestId(id);
		}
		List<StudentTest> studentTestDB = studentTestService.findOneTest(id);
		if (!"Y".equals(test.getRandomQuestion())) {
			if (test.getMaxScore() == sumOfTestQuestionMarks) {

				if (!"Y".equals(test.getAutoAllocateToStudents())) {
					m.addAttribute("showProceed", true);
				} else {
					if (studentTestDB.size() == 0) {
						autoAllocateStudent(test, username);
					}
					m.addAttribute("showStudents", true);
				}
			} else {
				m.addAttribute("showProceed", false);
				m.addAttribute("showStudents", false);
			}
		} else {
			if (test.getTestQuestions().size() > Integer.parseInt(test.getMaxQuestnToShow())) {
				if (!"Y".equals(test.getAutoAllocateToStudents())) {
					m.addAttribute("showProceed", true);
				} else {
					if (studentTestDB.size() == 0) {
						autoAllocateStudent(test, username);
					}
					m.addAttribute("showStudents", true);
				}
				;

			} else {
				m.addAttribute("showProceed", false);
				m.addAttribute("showStudents", false);
			}
		}

		m.addAttribute("sameMarksQue", test.getSameMarksQue());

		m.addAttribute("marksPerQue", test.getMarksPerQue());

		m.addAttribute("randomQuestionStatus", test.getRandomQuestion());

		if ("Y".equals(test.getRandomQuestion()) && "N".equals(test.getSameMarksQue())) {
			List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(test.getId());
			//List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService.findAllByTestId(test.getId());
			//m.addAttribute("testConfigList", testConfigList);
			//m.addAttribute("testPoolConfigList", testPoolConfigList);
			List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService.findAllByTestId(test.getId());
			if(testConfigList.size() > 0) {
				m.addAttribute("testConfigList", testConfigList);
			}else {
				m.addAttribute("testConfigList", testPoolConfigList);
			}
		}

		m.addAttribute("testId", test.getId());

		if (test != null) {
			if (test.getTestType() != null) {
				if ("Subjective".equals(test.getTestType())) {

					return "test/addSubjectiveTestQuestion";
				}
			}
		}

		return "test/addTestQuestion";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/evaluateTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateTestForm(@RequestParam Long id, @RequestParam String studusername, Model m,
			Principal principal, RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("addTestQuestion", "Evaluate Student Test", true, false));

		String role = "";

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			role = "ROLE_ADMIN";
		} else {
			role = "ROLE_FACULTY";
		}

		Test test = testService.findByIDAndFaculty(id, username, role);
		StudentTest stList = studentTestService.findBytestIDAndUsername(id, username);
		if (test == null && stList != null) {
			test = testService.findByID(id);
		}

		int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(id);

		TestQuestion testQuestion = new TestQuestion();

		StudentTest studentTest = studentTestService.findBytestIDAndUsername(id, studusername);

		List<TestQuestion> testQuestionResponse = new ArrayList<>();
		if ("Subjective".equals(test.getTestType())) {
			testQuestionResponse = testQuestionService.findResponseByTestIdAndUsernameToEvaluate(id, studusername);
		} else {
			testQuestionResponse = testQuestionService.findResponseByTestIdAndUsernameToEvaluateForMix(id,
					studusername);
		}

		test.setTestQuestions(testQuestionResponse);
		testQuestion.setTestId(id);
		m.addAttribute("TestId", id);
		m.addAttribute("test", test);
		m.addAttribute("studusername", studusername);

		m.addAttribute("testQuestion", testQuestion);

		return "test/evaluateStudentTest";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_DEAN","ROLE_CORD","ROLE_AREA_INCHARGE","ROLE_AR"})
	@RequestMapping(value = "/viewTestGroupForm", method = { RequestMethod.POST, RequestMethod.GET })
	public String viewTestGroupForm(Principal principal, Model m) {

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("viewTestGroup", "View Test Groups", true, false));
		Token userdetails1 = (Token) principal;

		List<Course> courselist = null;
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			courselist = courseService.findByUserActive(principal.getName(), userdetails1.getProgramName());
		} else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			courselist = courseService.findAll();
		} else if (userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
			courselist = courseService.findAll();
		} else if (userDetails.getAuthorities().contains(Role.ROLE_CORD)) {
			courselist = courseService.findAll();
		} else if (userDetails.getAuthorities().contains(Role.ROLE_AREA_INCHARGE)) {
			courselist = courseService.findAll();
		} else if (userDetails.getAuthorities().contains(Role.ROLE_AR)) {
			courselist = courseService.findAll();
		}
		m.addAttribute("courselist", courselist);
		m.addAttribute("testGroup", new Test());

		return "test/viewTestGroup";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestGroup", method = RequestMethod.POST)
	public String viewTestGroupForm(@ModelAttribute Test test, Principal principal, Model m) {
		List<Test> groupTestList = testService.getAllStudentsAssociatedWithGroup(test.getGroupId());
		m.addAttribute("groupTestList", groupTestList);
		m.addAttribute("rowCount", groupTestList.size());
		return viewTestGroupForm(principal, m);
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/updateTestDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String updateTestDetails(@RequestParam String value, @RequestParam Long pk,
			Principal principal, @RequestParam("attr") String attribute) {
		String username = principal.getName();
		try {
			testService.updateTestDetails(pk, value, attribute, username);
			return "{\"status\": \"success\", \"msg\": \"Score saved successfully!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);

			return "{\"status\": \"error\", \"msg\": \"Error.\"}";
		}

	}
	public void validateTestType(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Input field cannot be empty");
		 }
		if(!s.equals("Objective") && !s.equals("Subjective") && !s.equals("Mix")) {
			throw new ValidationException("Invalid Test Question Type.");
		}
	}
	public void validateTestQuestionType(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Input field cannot be empty");
		 }
		if(!s.equals("MCQ") && !s.equals("Numeric") && !s.equals("Image")) {
			throw new ValidationException("Invalid Test Question Type.");
		}
	}
	public void validateTestQuestionSubType(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Input field cannot be empty");
		 }
		if(!s.equals("SINGLESELECT") && !s.equals("MULTISELECT")) {
			throw new ValidationException("Invalid Test Question Sub Type.");
		}
	}
	
	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestQuestion", method = RequestMethod.POST)
	public String addTestQuestion(@ModelAttribute TestQuestion testQuestion, Model m, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();
		redirectAttrs.addAttribute("id", testQuestion.getTestId());
		List<TestQuestion> testQuestions = testQuestionService.findByTestId(testQuestion.getTestId());
		Test test = testService.findByID(testQuestion.getTestId());
		double sumOfQuestionMarks = 0.0;
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(testQuestion, Arrays.asList("description","option1","option2","option3","option4","option5","option6","option7","option8"));
			
			if(testQuestion.getDescription() == null || testQuestion.getDescription().isEmpty()) {
				throw new ValidationException("Input field cannot be empty");
			}
			BusinessBypassRule.validateNumericNotAZero(testQuestion.getMarks());
			validateTestQuestionType(testQuestion.getQuestionType());
			if(testQuestion.getQuestionType().equals("MCQ")) {
//				BusinessBypassRule.validateAlphaNumeric(testQuestion.getOption1());
//				BusinessBypassRule.validateAlphaNumeric(testQuestion.getOption2());
				if(testQuestion.getOption1() == null || testQuestion.getOption1().isEmpty()) {
					throw new ValidationException("Minimum 2 Option required.");
				}
				if(testQuestion.getOption2() == null || testQuestion.getOption2().isEmpty()) {
					throw new ValidationException("Minimum 2 Option required.");
				}
				validateTestQuestionSubType(testQuestion.getType());
				BusinessBypassRule.validateYesOrNo(testQuestion.getOptionShuffle());
				if(testQuestion.getCorrectOption() == null || testQuestion.getCorrectOption().isEmpty()) {
					throw new ValidationException("Please select correct Answer");
				}
				
				if (testQuestion.getCorrectOption().contains(",")) {
					List<String> splittedCorrectOption = new ArrayList<>();
					splittedCorrectOption = Arrays.asList(testQuestion.getCorrectOption().split(","));
					for(String s:splittedCorrectOption) {
						switch(s) {
						case "1": 
							if(testQuestion.getOption1() == null || testQuestion.getOption1().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "2":
							if(testQuestion.getOption2() == null || testQuestion.getOption2().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "3":
							if(testQuestion.getOption3() == null || testQuestion.getOption3().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "4":
							if(testQuestion.getOption4() == null || testQuestion.getOption4().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "5":
							if(testQuestion.getOption5() == null || testQuestion.getOption5().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "6":
							if(testQuestion.getOption6() == null || testQuestion.getOption6().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "7":
							if(testQuestion.getOption7() == null || testQuestion.getOption7().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "8":
							if(testQuestion.getOption8() == null || testQuestion.getOption8().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						default:
							throw new ValidationException("Please select correct Answer");
						}
					}
				}else {
					switch(testQuestion.getCorrectOption()) {
						case "1": 
							if(testQuestion.getOption1() == null || testQuestion.getOption1().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "2":
							if(testQuestion.getOption2() == null || testQuestion.getOption2().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "3":
							if(testQuestion.getOption3() == null || testQuestion.getOption3().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "4":
							if(testQuestion.getOption4() == null || testQuestion.getOption4().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "5":
							if(testQuestion.getOption5() == null || testQuestion.getOption5().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "6":
							if(testQuestion.getOption6() == null || testQuestion.getOption6().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "7":
							if(testQuestion.getOption7() == null || testQuestion.getOption7().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "8":
							if(testQuestion.getOption8() == null || testQuestion.getOption8().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						default:
							throw new ValidationException("Please select correct Answer");
					}
				}
				
			}
			if(testQuestion.getQuestionType().equals("Numeric")) {
				if(testQuestion.getCorrectAnswerNum() == null || testQuestion.getCorrectAnswerNum().isEmpty()) {
					throw new ValidationException("Correct Answer Input field cannot be empty");
				}
			}
			if("Y".equals(test.getSameMarksQue())) {
				if(!testQuestion.getMarks().equals(test.getMarksPerQue())) {
					throw new ValidationException("Question Marks should be " + test.getMarksPerQue() + ".");
				}
			}
			/* New Audit changes end */
			if (testQuestions.isEmpty()) {
				sumOfQuestionMarks = testQuestion.getMarks();

				if (!"Y".equals(test.getRandomQuestion())) {
					if (test.getMaxScore() < sumOfQuestionMarks) {
						setNote(redirectAttrs, "Marks Configured are more than total No. Of Marks for Test");
						return "redirect:/addTestQuestionForm";
					}
				}

				if (testQuestion.getQuestionType().equals("Numeric")) {

					double answerRangeFrom = Double.valueOf(testQuestion.getAnswerRangeFrom());
					double answerRangeTo = Double.valueOf(testQuestion.getAnswerRangeTo());
					double correctOption = Double.valueOf(testQuestion.getCorrectAnswerNum());
					if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
							&& answerRangeFrom < answerRangeTo) {
						testQuestion.setCorrectOption(testQuestion.getCorrectAnswerNum());
					} else {
						setNote(redirectAttrs, "Answer Range Given are not valid,Correct Option has to be between "
								+ " answer range from and to & answer range from field has to be less than answer range to field");
						return "redirect:/addTestQuestionForm";
					}
				}
				testQuestion.setCreatedBy(username);
				testQuestion.setLastModifiedBy(username);
				testQuestionService.insertWithIdReturn(testQuestion);

				setSuccess(redirectAttrs, "Question added successfully");

			} else {
				sumOfQuestionMarks = testQuestionService.getSumOfTestQuestionMarksByTestId(testQuestion.getTestId())
						+ testQuestion.getMarks();

				if (testQuestion.getQuestionType().equals("Numeric")) {

					double answerRangeFrom = Double.valueOf(testQuestion.getAnswerRangeFrom());
					double answerRangeTo = Double.valueOf(testQuestion.getAnswerRangeTo());
					double correctOption = Double.valueOf(testQuestion.getCorrectAnswerNum());
					if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
							&& answerRangeFrom < answerRangeTo) {
						testQuestion.setCorrectOption(testQuestion.getCorrectAnswerNum());
					} else {
						setNote(redirectAttrs, "Answer Range Given are not valid,Correct Option has to be between "
								+ " answer range from and to & answer range from field has to be less than answer range to field");
						return "redirect:/addTestQuestionForm";
					}
				}
				testQuestion.setCreatedBy(username);
				testQuestion.setLastModifiedBy(username);

				if (!"Y".equals(test.getRandomQuestion())) {
					if (test.getMaxScore() < sumOfQuestionMarks) {
						setNote(redirectAttrs, "Marks Configured are more than total No. Of Marks for Test");
						return "redirect:/addTestQuestionForm";
					}
				}
				testQuestionService.insertWithIdReturn(testQuestion);
				double sumOfQuestnMarks = testQuestionService.getSumOfTestQuestnMarksByTestId(testQuestion.getTestId());

				List<TestQuestion> testQuestn = testQuestionService.findByTestId(testQuestion.getTestId());

				List<StudentTest> studentTestList = studentTestService.findByTestId(testQuestion.getTestId());
				if (!"Y".equals(test.getRandomQuestion())) {
					if (test.getMaxScore() == sumOfQuestionMarks) {
						if (studentTestList.size() > 0) {
							reAllocateStudent(test);
						}
					}
				} else {
					if (testQuestn.size() > Integer.parseInt(test.getMaxQuestnToShow())) {
						reAllocateStudent(test);
					}
				}

				setSuccess(redirectAttrs, "Question added successfully");

			}

		}catch(ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addTestQuestionForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding question.");
			return "redirect:/addTestQuestionForm";
		}
		return "redirect:/addTestQuestionForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addSubjectiveTestQuestion", method = RequestMethod.POST)
	public String addSubjectiveTestQuestion(@ModelAttribute TestQuestion testQuestion, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		redirectAttrs.addAttribute("id", testQuestion.getTestId());
		List<TestQuestion> testQuestions = testQuestionService.findByTestId(testQuestion.getTestId());
		Test test = testService.findByID(testQuestion.getTestId());
		double sumOfQuestionMarks = 0.0;
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(testQuestion, Arrays.asList("description","option1","option2","option3","option4","option5","option6","option7","option8"));
			if(testQuestion.getDescription() == null || testQuestion.getDescription().isEmpty()) {
				throw new ValidationException("Input field cannot be empty");
			}
			BusinessBypassRule.validateNumeric(testQuestion.getMarks());
			if(testQuestion.getQuestionType() == null || testQuestion.getQuestionType().isEmpty() || !testQuestion.getQuestionType().equals("Descriptive")) {
				throw new ValidationException("Input field cannot be empty");
			}
			if("Y".equals(test.getSameMarksQue())) {
				if(!testQuestion.getMarks().equals(test.getMarksPerQue())) {
					throw new ValidationException("Question Marks should be " + test.getMarksPerQue() + ".");
				}
			}
			/* New Audit changes end */
			if (testQuestions.isEmpty()) {

				sumOfQuestionMarks = testQuestion.getMarks();

				if (!"Y".equals(test.getRandomQuestion())) {
					if (test.getMaxScore() < sumOfQuestionMarks) {
						setNote(redirectAttrs, "Marks Configured are more than total No. Of Marks for Test");
						return "redirect:/addTestQuestionForm";
					}
				}

				testQuestion.setCreatedBy(username);
				testQuestion.setLastModifiedBy(username);
				testQuestion.setTestTypeInTestQuestion(test.getTestType());

				testQuestionService.insert(testQuestion);

				setSuccess(redirectAttrs, "Question added successfully");

			} else {

				sumOfQuestionMarks = testQuestionService.getSumOfTestQuestionMarksByTestId(testQuestion.getTestId())
						+ testQuestion.getMarks();

				testQuestion.setCreatedBy(username);
				testQuestion.setLastModifiedBy(username);

				if (!"Y".equals(test.getRandomQuestion())) {
					if (test.getMaxScore() < sumOfQuestionMarks) {
						setNote(redirectAttrs, "Marks Configured are more than total No. Of Marks for Test");
						return "redirect:/addTestQuestionForm";
					}
				}

				testQuestionService.insert(testQuestion);
				double sumOfQuestnMarks = testQuestionService.getSumOfTestQuestnMarksByTestId(testQuestion.getTestId());

				List<TestQuestion> testQuestn = testQuestionService.findByTestId(testQuestion.getTestId());

				setSuccess(redirectAttrs, "Question added successfully");

			}

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addTestQuestionForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setNote(redirectAttrs, "Error in adding Question.");
			return "redirect:/addTestQuestionForm";
		}
		return "redirect:/addTestQuestionForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/showResultsToStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showResultsToStudents(@RequestParam("id") Long id, Model m, Principal principal) {

		String username = principal.getName();
		try {
			Test test = testService.findByID(id);
			test.setShowResultsToStudents("Y");
			testService.showResults(test.getId());
			setSuccess(m, "Results will be shown to students!");
			return "Success";
		} catch (Exception e) {

			logger.error("Error, " + e);
			return "Error";
		}

	}

	@ModelAttribute("TestList")
	public List<Test> getTestList() {
		return testService.findAllValidTest();
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/allocateStudentTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String allocateStudentTestForm(@ModelAttribute Test test, Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {

		m.addAttribute("webPage", new WebPage("allocateStudentTest", "Allocate Test ", true, false));

		// ----------------
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<StudentTest> studentList = studentTestService.getStudentForTestList(test.getId(), test.getFacultyId(),
				test.getCourseId());
		m.addAttribute("noOfStudentAllocated", studentTestService.getNoOfStudentsAllocated(test.getId()));

		List<StudentTest> programList = studentList;

		UserCourse userCourse = new UserCourse();

		m.addAttribute("studentList", studentList);
		m.addAttribute("test", test);
		m.addAttribute("q", getQueryString(test));
		return "test/allocateStudentTest";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/updateTestQuestion", method = RequestMethod.POST)
	public String updateTestQuestion(@ModelAttribute Test test, RedirectAttributes redirectAttrs, Principal principal) {
		String username = principal.getName();

		TestQuestion testQuestion = test.getTestQuestions().get(test.getTestQuestions().size() - 1);
		double sumOfQuestionMarks = 0.0;
		try {
			
			TestQuestion testQuestionFromDb = testQuestionService.findByID(testQuestion.getId());
			test = testService.findByID(testQuestionFromDb.getTestId());
			int id = testQuestionFromDb.getTestId().intValue();
			redirectAttrs.addAttribute("id", testQuestionFromDb.getTestId());
			/* New Audit changes start */
			HtmlValidation.validateHtml(testQuestion, Arrays.asList("description","option1","option2","option3","option4","option5","option6","option7","option8"));
	
			if(testQuestion.getDescription() == null || testQuestion.getDescription().isEmpty()) {
				throw new ValidationException("Input field cannot be empty");
			}
			BusinessBypassRule.validateNumericNotAZero(testQuestion.getMarks());
			logger.info(testQuestion.getQuestionType());
//			validateTestQuestionType(testQuestion.getQuestionType());
			if(testQuestion.getQuestionType().equals("MCQ")) {
//				BusinessBypassRule.validateAlphaNumeric(testQuestion.getOption1());
//				BusinessBypassRule.validateAlphaNumeric(testQuestion.getOption2());
				if(testQuestion.getOption1() == null || testQuestion.getOption1().isEmpty()) {
					throw new ValidationException("Minimum 2 Option required.");
				}
				if(testQuestion.getOption2() == null || testQuestion.getOption2().isEmpty()) {
					throw new ValidationException("Minimum 2 Option required.");
				}
				validateTestQuestionSubType(testQuestion.getType());
				BusinessBypassRule.validateYesOrNo(testQuestion.getOptionShuffle());
				if(testQuestion.getCorrectOption() == null && testQuestion.getCorrectOption().isEmpty()) {
					throw new ValidationException("Please select correct Answer");
				}
				if (testQuestion.getCorrectOption().contains(",")) {
					List<String> splittedCorrectOption = new ArrayList<>();
					splittedCorrectOption = Arrays.asList(testQuestion.getCorrectOption().split(","));
					for(String s:splittedCorrectOption) {
						switch(s) {
						case "1": 
							if(testQuestion.getOption1() == null || testQuestion.getOption1().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "2":
							if(testQuestion.getOption2() == null || testQuestion.getOption2().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "3":
							if(testQuestion.getOption3() == null || testQuestion.getOption3().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "4":
							if(testQuestion.getOption4() == null || testQuestion.getOption4().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "5":
							if(testQuestion.getOption5() == null || testQuestion.getOption5().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "6":
							if(testQuestion.getOption6() == null || testQuestion.getOption6().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "7":
							if(testQuestion.getOption7() == null || testQuestion.getOption7().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "8":
							if(testQuestion.getOption8() == null || testQuestion.getOption8().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						default:
							throw new ValidationException("Please select correct Answer");
						}
					}
				}else {
					switch(testQuestion.getCorrectOption()) {
						case "1": 
							if(testQuestion.getOption1() == null || testQuestion.getOption1().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "2":
							if(testQuestion.getOption2() == null || testQuestion.getOption2().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "3":
							if(testQuestion.getOption3() == null || testQuestion.getOption3().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "4":
							if(testQuestion.getOption4() == null || testQuestion.getOption4().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "5":
							if(testQuestion.getOption5() == null || testQuestion.getOption5().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "6":
							if(testQuestion.getOption6() == null || testQuestion.getOption6().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "7":
							if(testQuestion.getOption7() == null || testQuestion.getOption7().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "8":
							if(testQuestion.getOption8() == null || testQuestion.getOption8().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						default:
							throw new ValidationException("Please select correct Answer");
					}
				}
			}
			if(testQuestion.getQuestionType().equals("Numeric")) {
				if(testQuestion.getCorrectAnswerNum() == null && testQuestion.getCorrectAnswerNum().isEmpty()) {
					throw new ValidationException("Correct Answer Input field cannot be empty");
				}
			}
			if("Y".equals(test.getSameMarksQue())) {
				if(!testQuestion.getMarks().equals(test.getMarksPerQue())) {
					throw new ValidationException("Question Marks should be " + test.getMarksPerQue() + ".");
				}
			}
			/* New Audit changes end */
			sumOfQuestionMarks = (testQuestionService.getSumOfTestQuestionMarksByTestId(testQuestionFromDb.getTestId())
					- testQuestionFromDb.getMarks()) + testQuestion.getMarks();
			if (!"Y".equals(test.getRandomQuestion())) {
				if (test.getMaxScore() < sumOfQuestionMarks) {
					setNote(redirectAttrs, "Marks Configured are more than total No. Of Marks for Test");
					return "redirect:/addTestQuestionForm";
				}
			}

			testQuestionFromDb = LMSHelper.copyNonNullFields(testQuestionFromDb, testQuestion);
			testQuestionFromDb.setLastModifiedBy(username);

			Test testDB = testService.authenticateUserPrivilages(id, username);
			if (testDB != null) {
				if (testQuestion.getQuestionType().equals("Numeric")) {

					double answerRangeFrom = Double.valueOf(testQuestion.getAnswerRangeFrom());
					double answerRangeTo = Double.valueOf(testQuestion.getAnswerRangeTo());
					double correctOption = Double.valueOf(testQuestion.getCorrectOption());
					if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
							&& answerRangeFrom < answerRangeTo) {

					} else {
						setNote(redirectAttrs, "Answer Range Given are not valid,Correct Option has to be between "
								+ " answer range from and to & answer range from field has to be less than answer range to field");
						return "redirect:/addTestQuestionForm";
					}
				}

				if (testQuestionService.updateTestQuestionsAfterTest(testQuestionFromDb) > 0) {
					int noOfStudentsAllocated = studentTestService.getNoOfStudentsAllocated(testDB.getId());

					if (noOfStudentsAllocated > 0) {
						Long qId = testQuestion.getId();
						testQuestionFromDb = testQuestionService.findByID(qId);
						// List<StudentQuestionResponse> responseList =
						// studentQuestionResponseService.findByQId(String.valueOf(qId));
						List<StudentQuestionResponse> responseList = studentQuestionResponseService
								.findByQIdNew(String.valueOf(qId));
						// 17-09-2020 Hiren
						List<StudentQuestionResponseAudit> responseAuditList = studentQuestionResponseAuditService
								.findByQuesId(qId);
						if ("Numeric".equals(testQuestionFromDb.getQuestionType())) {
							testQuestionService.updateStudentMarks(testQuestionFromDb);

							testQuestionService.updateStudentMarksAudit(testQuestionFromDb);

							testQuestionService.updateStudentMarksToZero(testQuestionFromDb);
							testQuestionService.updateStudentMarksToZeroAudit(testQuestionFromDb);

							List<StudentTest> findStudentTest = studentTestService
									.findStudentTest(testQuestionFromDb.getTestId());

							for (StudentTest st : findStudentTest) {

								st.setScore(st.getUpdatedScore());
							}

							studentTestService.insertBatch(findStudentTest);
							studentTestAuditService.updateBatch(findStudentTest);

						} else {
							for (StudentQuestionResponse s : responseList) {

								updateStudentResponse(s, s.getUsername());
								updateStudentTest(s.getStudentTestId(), s.getUsername());
							}
							// 17-09-2020 Hiren
							for (StudentQuestionResponseAudit sa : responseAuditList) {
								double marks = 0.0;
								boolean isCorrectAudit = checkStudentsQuestionAnswerForMCQ(
										testQuestionFromDb.getCorrectOption(), sa.getAnswer());
								if (isCorrectAudit) {
									marks = testQuestionFromDb.getMarks();
								}
								studentQuestionResponseAuditService.updateMarksForQuestionResponseAudit(marks,
										sa.getId());
							}
						}
					}

					setSuccess(redirectAttrs, "Test Question updated successfully");
				}

				else {
					setError(redirectAttrs, "Test Question cannot be updated");
				}

			} else {
				setError(redirectAttrs, "Dont Have sufficeint Right to update");
				return "redirect:/addTestQuestionForm";
			}

		} catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addTestQuestionForm";
		}catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test Question");
			return "redirect:/addTestQuestionForm";
		}
		return "redirect:/addTestQuestionForm";
	}

	public StudentQuestionResponse updateStudentResponse(StudentQuestionResponse studentQuestionResponse,
			String Username) {
		try {
			;
			TestQuestion tq = testQuestionService.findByID(studentQuestionResponse.getQuestionId());

			String cOption = tq.getCorrectOption();
			String studentOption = studentQuestionResponse.getAnswer();

//			List<StudentTest> studentTestAuditList = studentTestAuditService.findByTestIdAndUsername(tq.getTestId(),
//					Username);
//			int latestAttempt = studentTestAuditList.size();
//			if (cOption.equals(studentOption)) {
//				studentQuestionResponse.setMarks(tq.getMarks());
//
//			} else {
//				studentQuestionResponse.setMarks(0.0);
//			}

			// 17-09-2020 Hiren
			boolean isCorrect = checkStudentsQuestionAnswerForMCQ(cOption, studentOption);
			if (isCorrect) {
				studentQuestionResponse.setMarks(tq.getMarks());
			} else {
				studentQuestionResponse.setMarks(0.0);
			}

			studentQuestionResponse.setUsername(Username);
			studentQuestionResponse.setCreatedBy(Username);
			studentQuestionResponse.setLastModifiedBy(Username);

//			for (int i = 1; i <= latestAttempt; i++) {
//
//				StudentQuestionResponseAudit studentQuestionResponseAudit = responseBeanToAuditBeanForUpdate(
//						studentQuestionResponse, tq.getTestId(), i);
//				studentQuestionResponseAudit.setAnswer(studentQuestionResponse.getAnswer());
//				studentQuestionResponseAudit.setMarks(studentQuestionResponse.getMarks());
//
//				studentQuestionResponseAuditService.insert(studentQuestionResponseAudit);
//			}

			studentQuestionResponseService.insert(studentQuestionResponse);

			return studentQuestionResponse;
		} catch (Exception e) {
			return null;
		}
	}

	public StudentQuestionResponseAudit responseBeanToAuditBean(StudentQuestionResponse sqr, Long testId) {

		StudentQuestionResponseAudit sqra = new StudentQuestionResponseAudit();
		StudentTest st = studentTestService.findByID(sqr.getStudentTestId());

		testId = st.getTestId();
		List<StudentTest> studentTestAuditList = studentTestAuditService.findByTestIdAndUsername(st.getTestId(),
				sqr.getUsername());
		int latestAttempt = 0;

		if (studentTestAuditList.size() > 0) {
			latestAttempt = studentTestAuditList.get(studentTestAuditList.size() - 1).getAttempt() + 1;
		} else {
			latestAttempt = 1;
		}

		sqra.setUsername(sqr.getUsername());
		sqra.setStudentTestId(sqr.getStudentTestId());
		sqra.setQuestionId(sqr.getQuestionId());
		sqra.setAnswer(sqra.getAnswer());
		sqra.setCreatedBy(sqr.getUsername());
		sqra.setLastModifiedBy(sqr.getUsername());
		sqra.setAttempts(String.valueOf(latestAttempt));

		return sqra;
	}

	public StudentQuestionResponseAudit responseBeanToAuditBeanForUpdate(StudentQuestionResponse sqr, Long testId,
			int latestAttempt) {

		StudentQuestionResponseAudit sqra = new StudentQuestionResponseAudit();
		StudentTest st = studentTestService.findByID(sqr.getStudentTestId());

		testId = st.getTestId();

		sqra.setUsername(sqr.getUsername());
		sqra.setStudentTestId(sqr.getStudentTestId());
		sqra.setQuestionId(sqr.getQuestionId());
		sqra.setAnswer(sqra.getAnswer());
		sqra.setCreatedBy(sqr.getUsername());
		sqra.setLastModifiedBy(sqr.getUsername());
		sqra.setAttempts(String.valueOf(latestAttempt));

		return sqra;
	}

	public void updateStudentTest(Long studentTestId, String username) {
		StudentTest studentTestDB = studentTestService.findByID(Long.valueOf(studentTestId));
		Test testDetails = testService.findByID(studentTestDB.getTestId());

		try {
			double totalMarks = 0.0;
			List<StudentQuestionResponse> sqrList = studentQuestionResponseService
					.findByStudentTestId(String.valueOf(studentTestId));
			for (StudentQuestionResponse sqr : sqrList) {
				if (sqr.getMarks() != null) {
					totalMarks = totalMarks + sqr.getMarks();
				}
			}
			studentTestDB.setUsername(username);
			Date dt = Utils.getInIST();
			studentTestDB.setTestEndTime(dt);
			studentTestDB.setTestCompleted("Y");
			studentTestDB.setScore(totalMarks);
			if (totalMarks >= testDetails.getPassScore()) {
				studentTestDB.setStatus(StudentTest.PASS);
			} else {
				studentTestDB.setStatus(StudentTest.FAIL);
			}
			studentTestService.upsert(studentTestDB);
			studentTestAuditService.update(studentTestDB);

		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/deleteTest", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deleteTestForm(HttpServletRequest request, @ModelAttribute Test test, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {

		int id = Integer.parseInt(request.getParameter("id"));
		String username = principal.getName();
		Test t = testService.findByID(Long.valueOf(id));
		Test testDB = testService.authenticateUserPrivilages(id, username);
		if (testDB != null) {
			int testId = Integer.parseInt(request.getParameter("id"));
			testService.deleteSoftById(String.valueOf(testId));
			setSuccess(redirectAttrs, "test deleted successfully");
			return new ModelAndView("redirect:/testList");
		} else {
			if (t.getCreatedBy().equalsIgnoreCase(username) || t.getFacultyId().equalsIgnoreCase(username)) {
				int testId = Integer.parseInt(request.getParameter("id"));
				List<TestQuestion> qList = testQuestionService.findByTestId(Long.valueOf(testId));
				if (qList.isEmpty() || qList.size() == 0) {
					testService.deleteSoftById(String.valueOf(testId));
					setSuccess(redirectAttrs, "test deleted successfully");
					return new ModelAndView("redirect:/testList");
				} else {
					setError(redirectAttrs, "Dont Have sufficeint Right to delete OR test has started");
					return new ModelAndView("redirect:/testList");
				}
			}

		}
		return new ModelAndView("redirect:/testList");

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/searchFacultyTestAllocationForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchFacultyTestAllocationForm(RedirectAttributes redirectAttributes, Model m, Long id,
			@ModelAttribute Test testObj, Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignmentList", "Search tests", false, false));
		m.addAttribute("testObj", testObj);

		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		m.addAttribute("allCourses", courseService.findByUserActive(username, userdetails1.getProgramName()));
		m.addAttribute("preAssigment", new ArrayList());

		m.addAttribute("allTests", testService.findAll());
		return "test/facultyTestAllocation";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/searchFacultyTestAllocation", method = { RequestMethod.POST })
	public String searchFacultyTestAllocation(Model m, Principal principal,
			@RequestParam(required = false) Long courseId,

			@RequestParam Long id) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Faculty Allocation", true, false));

		Test testObj = testService.findByID(id);
		m.addAttribute("allFaculty", userService.findAllFaculty());
		List<Test> list1 = new ArrayList<Test>();
		list1.add(testObj);

		if (list1 != null && list1.size() > 0) {
			m.addAttribute("list1", list1);
			m.addAttribute("rowCount", list1.size());
			m.addAttribute("testObj", testObj);
		} else {
			setNote(m, "No Students found for this course");
		}
		return "test/facultyTestAssigned";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/getTestsByCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTestsByCourse(@RequestParam(name = "courseId") String courseId,

			Principal principal, Model m) {

		String json = "";
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Test> testList = testService.findByCourse(Long.valueOf(courseId));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Test t : testList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(t.getId()), t.getTestName());
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}
		return json;

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/saveFacultyTestAllocation", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveFacultyTestAllocation(@ModelAttribute Test testObj, Model m, Principal principal,
			@RequestParam(required = false) String facultyId, RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("groups", "Faculty Allocate", true, true, true, true, false));

		List<User> listOfFaculty = new ArrayList<User>();
		listOfFaculty = userService.findAllFaculty();
		m.addAttribute("listOfFaculty", listOfFaculty);
		testService.updateFacultyAssigned(facultyId, testObj.getId());

		if (testObj.getFacultyId().equalsIgnoreCase(facultyId)) {
			setSuccess(redirectAttributes, "Faculty Changed successfully!");
		} else {
			setError(m, "Error!");
		}

		m.addAttribute("testObj", testObj);
		return "redirect:/searchFacultyTestAllocationForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTestQuestionByTestId", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTestQuestionByTestId(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String testId) throws URIException {

		m.addAttribute("webPage", new WebPage("viewAssignment", "Search Assignments", true, true));

		List<TestQuestion> testQuestionsByTestId = testQuestionService.findByTestId(Long.valueOf(testId));

		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("description", "marks", "type",
				"questionType", "Options Shuffle Required", "option1", "option2", "option3", "option4", "option5",
				"option6", "option7", "option8", "answerRangeFrom", "answerRangeTo", "correctOption"));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfTestQuestion = new ArrayList<>();
			fileName = testService.findByID(Long.valueOf(testId)).getTestName() + ".xlsx";
			fileName = fileName.replace("/", "_");

			filePath = downloadAllFolder + File.separator + fileName;

			for (TestQuestion tq : testQuestionsByTestId) {
				Map<String, Object> mapOfTestQuestion = new HashMap<>();

				String answerRangeFrom = tq.getAnswerRangeFrom() != null ? tq.getAnswerRangeFrom() : "";
				String answerRangeTo = tq.getAnswerRangeTo() != null ? tq.getAnswerRangeTo() : "";
				String questionType = tq.getQuestionType() != null ? tq.getQuestionType() : "MCQ";
				String optionShuffleReq = tq.getOptionShuffle() != null ? tq.getOptionShuffle() : "N";

				mapOfTestQuestion.put("answerRangeFrom", answerRangeFrom);
				mapOfTestQuestion.put("answerRangeTo", answerRangeTo);
				mapOfTestQuestion.put("Options Shuffle Required", optionShuffleReq);
				mapOfTestQuestion.put("questionType", questionType);
				mapOfTestQuestion.put("description", tq.getDescription());
				mapOfTestQuestion.put("marks", tq.getMarks());
				mapOfTestQuestion.put("type", tq.getType());
				mapOfTestQuestion.put("option1", tq.getOption1());
				mapOfTestQuestion.put("option2", tq.getOption2());
				mapOfTestQuestion.put("option3", tq.getOption3());
				mapOfTestQuestion.put("option4", tq.getOption4());
				mapOfTestQuestion.put("option5", tq.getOption5());
				mapOfTestQuestion.put("option6", tq.getOption6());
				mapOfTestQuestion.put("option7", tq.getOption7());
				mapOfTestQuestion.put("option8", tq.getOption8());
				mapOfTestQuestion.put("correctOption", tq.getCorrectOption());
				listOfMapOfTestQuestion.add(mapOfTestQuestion);
			}
			excelCreater.CreateExcelFile(listOfMapOfTestQuestion, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// copy it to response's OutputStream
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
			}

		}
		return null;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTestReportByTestId", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTestReportByTestId(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String testId) throws URIException {

		Token userDetails = (Token) p;
		Test testDB = testService.findByID(Long.valueOf(testId));
		List<TestQuestion> testDetailsByTestId = new ArrayList<>();
		if ("Y".equals(testDB.getIsCreatedByAdmin()) && userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			List<String> courseIds = userCourseDAO.findCoursesByParam(testDB.getModuleId(), userDetails.getProgramId(),
					String.valueOf(testDB.getAcadYear()), p.getName());
			testDetailsByTestId = testQuestionService.findTestCreatedByAdminQuestionWise(testId, courseIds);
		} else {
			testDetailsByTestId = testQuestionService.findTestDetailsQuestionWise(Long.valueOf(testId));
		}

		// List<TestQuestion> testDetailsByTestId =
		// testQuestionService.findTestDetailsQuestionWise(Long.valueOf(testId));

		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("username", "Student Answer",
				"Student Marks", "Description", "Correct Option", "Option1", "option2", "option3", "option4", "option5",
				"option6", "option7", "option8", "answerRangeFrom", "answerRangeTo"));
		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfTestDetails = new ArrayList<>();
			fileName = testDB.getTestName() + ".xlsx";
			fileName = fileName.replace("/", "_");
			filePath = downloadAllFolder + File.separator + fileName;

			for (TestQuestion tq : testDetailsByTestId) {
				Map<String, Object> mapOfTestDetails = new HashMap<>();

				String studentAnswer = tq.getAnswer() != null ? String.valueOf(tq.getAnswer()) : "";
				String studentMarks = tq.getStudentMarks() != null ? String.valueOf(tq.getStudentMarks()) : "";
				String answerRangeFrom = tq.getAnswerRangeFrom() != null ? tq.getAnswerRangeFrom() : "";
				String answerRangeTo = tq.getAnswerRangeTo() != null ? tq.getAnswerRangeTo() : "";

				mapOfTestDetails.put("username", tq.getUsername());
				mapOfTestDetails.put("Student Answer", studentAnswer);
				mapOfTestDetails.put("Student Marks", studentMarks);
				//mapOfTestDetails.put("Description", tq.getDescription());
				if(tq.getDescription().length() > 20000) {	
					String des = tq.getDescription().substring(0, 20000);
					mapOfTestDetails.put("Description", des);
				}else {
					mapOfTestDetails.put("Description", tq.getDescription());
				}
				mapOfTestDetails.put("Correct Option", tq.getCorrectOption());
				mapOfTestDetails.put("Option1", tq.getOption1());
				mapOfTestDetails.put("option2", tq.getOption2());
				mapOfTestDetails.put("option3", tq.getOption3());
				mapOfTestDetails.put("option4", tq.getOption4());
				mapOfTestDetails.put("option5", tq.getOption5());
				mapOfTestDetails.put("option6", tq.getOption6());
				mapOfTestDetails.put("option7", tq.getOption7());
				mapOfTestDetails.put("option8", tq.getOption8());
				mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
				mapOfTestDetails.put("answerRangeTo", answerRangeTo);

				if (tq.getQuestionType() != null) {
					if (tq.getQuestionType().equals("Numeric")) {
						mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
						mapOfTestDetails.put("answerRangeTo", answerRangeTo);

					}
				}

				listOfMapOfTestDetails.add(mapOfTestDetails);
			}
			excelCreater.CreateExcelFile(listOfMapOfTestDetails, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
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
			}

		}
		return null;
	}

	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/downloadTestReportByTestIdAndUsername", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String downloadTestReportByTestIdAndUsername(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String testId) throws URIException {

		String username = p.getName();

		List<TestQuestion> testDetailsByTestId = testQuestionService
				.findTestDetailsQuestionWiseByUsername(Long.valueOf(testId), username);

		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("username", "Student Answer",
				"Student Marks", "Description", "Correct Option", "Option1", "option2", "option3", "option4", "option5",
				"option6", "option7", "option8", "answerRangeFrom", "answerRangeTo"));
		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfTestDetails = new ArrayList<>();
			fileName = testService.findByID(Long.valueOf(testId)).getTestName() + ".xlsx";
			fileName = fileName.replace("/", "_");
			fileName = fileName.replace(",", "_");
			filePath = downloadAllFolder + File.separator + fileName;

			for (TestQuestion tq : testDetailsByTestId) {
				Map<String, Object> mapOfTestDetails = new HashMap<>();

				String studentAnswer = tq.getAnswer() != null ? String.valueOf(tq.getAnswer()) : "";
				String studentMarks = tq.getStudentMarks() != null ? String.valueOf(tq.getStudentMarks()) : "";
				String answerRangeFrom = tq.getAnswerRangeFrom() != null ? tq.getAnswerRangeFrom() : "";
				String answerRangeTo = tq.getAnswerRangeTo() != null ? tq.getAnswerRangeTo() : "";

				mapOfTestDetails.put("username", tq.getUsername());
				mapOfTestDetails.put("Student Answer", studentAnswer);
				mapOfTestDetails.put("Student Marks", studentMarks);
				//mapOfTestDetails.put("Description", tq.getDescription());
				if(tq.getDescription().length() > 20000) {	
					String des = tq.getDescription().substring(0, 20000);
					mapOfTestDetails.put("Description", des);
				}else {
					mapOfTestDetails.put("Description", tq.getDescription());
				}
				mapOfTestDetails.put("Correct Option", tq.getCorrectOption());
				mapOfTestDetails.put("Option1", tq.getOption1());
				mapOfTestDetails.put("option2", tq.getOption2());
				mapOfTestDetails.put("option3", tq.getOption3());
				mapOfTestDetails.put("option4", tq.getOption4());
				mapOfTestDetails.put("option5", tq.getOption5());
				mapOfTestDetails.put("option6", tq.getOption6());
				mapOfTestDetails.put("option7", tq.getOption7());
				mapOfTestDetails.put("option8", tq.getOption8());

				mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
				mapOfTestDetails.put("answerRangeTo", answerRangeTo);

				if (tq.getQuestionType() != null) {
					if (tq.getQuestionType().equals("Numeric")) {
						mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
						mapOfTestDetails.put("answerRangeTo", answerRangeTo);

					}
				}

				listOfMapOfTestDetails.add(mapOfTestDetails);
			}
			excelCreater.CreateExcelFile(listOfMapOfTestDetails, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
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
			}

		}
		return null;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestPoolForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTestPoolForm(@ModelAttribute TestPool testPool, Model m, Principal principal,
			@RequestParam(required = false) String courseId, RedirectAttributes redirectAttrs) {

		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			if (courseId != null) {
				testPool.setCourseId(courseId);
			}

			m.addAttribute("webPage", new WebPage("test", "Create Test Pool", false, false));

			if (testPool.getId() != null) {
				TestPool testPoolsDB = testPoolService.findByID(testPool.getId());
				if (testPoolsDB == null) {
					setError(m, "TEST " + testPool.getTestPoolName() + " does not exist");
					testPool.setId(null);
				} else {
					LMSHelper.copyNonNullFields(testPool, testPoolsDB);
					m.addAttribute("edit", "true");
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttrs.addFlashAttribute("testPools", testPool);
			setError(redirectAttrs, "Error in adding Test");
			return "test/addOfflineTest";
		}
		redirectAttrs.addFlashAttribute("testPools", testPool);

		return "test/addTestPool";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestPool", method = RequestMethod.POST)
	public String addTestPool(@ModelAttribute TestPool testPool, RedirectAttributes redirectAttrs, Principal principal,
			Model m) {
		try {
			String username = principal.getName();
			redirectAttrs.addFlashAttribute("testPool", testPool);
			/* New Audit changes start */
			HtmlValidation.validateHtml(testPool, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(testPool.getTestPoolName());
			/* New Audit changes end */
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			testPool.setCreatedBy(username);
			testPool.setLastModifiedBy(username);

			testPoolService.insertWithIdReturn(testPool);
			
			setSuccess(redirectAttrs, " Test Pool added successfully");

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addTestPoolForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Test Pool");
			return "redirect:/addTestPoolForm";
		}
		return "redirect:/uploadTestQuestionPoolForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/uploadTestQuestionPoolForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadTestQuestionPoolForm(Model m, Principal principal, @ModelAttribute TestPool testPool,
			RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage",
				new WebPage("uploadStudentOfflineTestScoreForm", "Upload Student Offline Test Score", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("testPool", testPool);
		return "test/uploadTestQuestionPool";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/uploadTestQuestionPool", method = { RequestMethod.POST })
	public String uploadTestQuestionPool(@ModelAttribute TestPool testPool, @RequestParam("file") MultipartFile file,
			Model m, RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage",
				new WebPage("uploadStudentOfflineTest", "Upload Student Offline Test Score ", false, false));
		List<String> validateHeaders = null;
		List<String> mcqReqField = null;
		List<String> numericReqField = null;
		List<String> subjectiveReqField = null;

		TestPool offlineTestDb = testPoolService.findByID(testPool.getId());
		validateHeaders = new ArrayList<String>(Arrays.asList("description", "marks", "testType", "questionType",
				"type", "answerRangeFrom", "answerRangeTo", "Options Shuffle Required", "option1", "option2", "option3",
				"option4", "option5", "option6", "option7", "option8", "correctOption"));

		mcqReqField = new ArrayList<String>(Arrays.asList("description", "marks", "testType", "questionType", "type",
				"Options Shuffle Required", "option1", "option2","correctOption"));

		numericReqField = new ArrayList<String>(Arrays.asList("description", "marks", "testType", "questionType",
				"answerRangeFrom", "answerRangeTo", "correctOption"));

		subjectiveReqField = new ArrayList<String>(Arrays.asList("description", "marks", "testType", "questionType"));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		ExcelReader excelReader = new ExcelReader();

		try {
			String correctOpt = "";
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);

			List<TestQuestionPools> testQuestionPools = testQuestionPoolsService
					.findByTestPoolId(String.valueOf(testPool.getId()));
			List<Map<String, Object>> listOfMapper = new ArrayList<>();

			if (testQuestionPools.size() > 0) {

				// testQuestionPoolsService.deleteBatch(testQuestionPools);

			}

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				int rowNum = 0;
				int errorCount = 0;
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
						errorCount++;
						setNote(m, "Error--->" + mapper.get("Error"));

					} else {
						rowNum++;
						TestQuestionPools testQuestionPool = new TestQuestionPools();

						testQuestionPool.setTestPoolId(testPool.getId());
						String questionType = (String) mapper.get("questionType");

						if (!questionType.isEmpty()) {

							if ("Numeric".equals(questionType)) {

								String answerRF = (String) mapper.get("answerRangeFrom");
								String answerRT = (String) mapper.get("answerRangeTo");
								String answerCO = (String) mapper.get("correctOption");
								double answerRangeFrom = Double.valueOf(answerRF);
								double answerRangeTo = Double.valueOf(answerRT);
								double correctOption = Double.valueOf(answerCO);

								if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
										&& answerRangeFrom < answerRangeTo) {
									for (String numercFields : numericReqField) {

										String field = (String) mapper.get(numercFields);

										if (field.trim().isEmpty()) {

											setError(m, numercFields + "Cannot be blank in Numeric at Row - " + rowNum);
											return "test/uploadTestQuestionPool";
										}

									}
								} else {
									setError(m,

											"Answer Range Given are not valid at row " + rowNum
													+ ", Correct Option has to be between "
													+ " answer-range-from and answer-range-to & answer-range-from field has to be less "
													+ " than answer-range-to field ");
									return "test/uploadTestQuestionPool";
								}

							} else if ("MCQ".equals(questionType)) {

								for (String numercFields : mcqReqField) {

									String field = String.valueOf(mapper.get(numercFields));

									if (field.trim().isEmpty()) {

										setError(m, numercFields + "Cannot be blank in MCQ at Row - " + rowNum);
										return "test/uploadTestQuestionPool";
									}
								}

							} else if ("Descriptive".equals(questionType)) {

								for (String numercFields : subjectiveReqField) {

									String field = (String) mapper.get(numercFields);

									if (field.trim().isEmpty()) {

										setError(m, numercFields + "Cannot be blank in Subjective/Descriptive at Row - "
												+ rowNum);
										return "test/uploadTestQuestionPool";
									}
								}

							} else {

							}

						}

						if (mapper.get("type").toString().isEmpty()) {

							mapper.put("type", null);
						}
						testQuestionPool.setCreatedBy(username);
						testQuestionPool.setLastModifiedBy(username);
						mapper.get("Options Shuffle Required");
						mapper.put("testPoolId", testPool.getId());
						mapper.put("createdBy", testQuestionPool.getCreatedBy());
						mapper.put("lastModifiedBy", testQuestionPool.getLastModifiedBy());
						mapper.put("optionShuffle", (String) mapper.get("Options Shuffle Required"));
						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());
						mapper.put("option1", mapper.get("option1").toString());
						mapper.put("option2", mapper.get("option2").toString());
						mapper.put("option3", mapper.get("option3").toString());
						mapper.put("option4", mapper.get("option4").toString());
						mapper.put("option5", mapper.get("option5").toString());
						mapper.put("option6", mapper.get("option6").toString());
						mapper.put("option7", mapper.get("option7").toString());
						mapper.put("option8", mapper.get("option8").toString());
						listOfMapper.add(mapper);
					}
				}

				if (errorCount == 0) {
					testQuestionPoolsService.insertUsingMapBulk(listOfMapper);
					setSuccess(m, "test question pool file uploaded successfully");
				}
			}

			m.addAttribute("testPool", testPool);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "test/uploadTestQuestionPool";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/configureImageTestQuestionPoolForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String configureImageTestQuestionPoolForm(@RequestParam Long testPoolId, Model m, Principal principal,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		m.addAttribute("Program_Name", ProgramName);

		m.addAttribute("webPage", new WebPage("addTestQuestion", "Add Image Questions For Pool", true, false));

		TestPool testPool = testPoolService.findByID(testPoolId);

		TestQuestionPools testQuestionPools = new TestQuestionPools();

		List<TestQuestionPools> testQuestionPoolList = testQuestionPoolsService
				.findByTestPoolId(String.valueOf(testPoolId));

		testPool.setTestQuestionPools(testQuestionPoolList);
		if (null != testPool) {

		} else {
			setError(m, "Test Pool not found");
		}
		m.addAttribute("testPool", testPool);
		m.addAttribute("testPoolId", testPoolId);
		m.addAttribute("testQuestionPools", testQuestionPools);

		return "test/configureImageTestQuestionPool";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/updateTestImageQuestionPool", method = RequestMethod.POST)
	public String updateTestImageQuestionPool(@ModelAttribute TestPool test, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();

		TestQuestionPools testQuestionPool = test.getTestQuestionPools().get(test.getTestQuestionPools().size() - 1);
		TestQuestionPools testQuestionPoolsFromDb = testQuestionPoolsService.findByID(testQuestionPool.getId());
		int id = testQuestionPoolsFromDb.getTestPoolId().intValue();
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(test, Arrays.asList("description","option1","option2","option3","option4","option5","option6","option7","option8"));
			if(testQuestionPool.getDescription() == null || testQuestionPool.getDescription().isEmpty()) {
				throw new ValidationException("Input field cannot be empty");
			}
			BusinessBypassRule.validateNumericNotAZero(testQuestionPool.getMarks());
			validateTestQuestionType(testQuestionPool.getQuestionType());
			if(testQuestionPool.getQuestionType().equals("MCQ")) {
				validateTestQuestionSubType(testQuestionPool.getType());
				BusinessBypassRule.validateYesOrNo(testQuestionPool.getOptionShuffle());
				if(testQuestionPool.getCorrectOption() == null && testQuestionPool.getCorrectOption().isEmpty()) {
					throw new ValidationException("Please select correct Answer");
				}
			}
			if(testQuestionPool.getQuestionType().equals("Numeric")) {
				if(testQuestionPool.getCorrectAnswerNum() == null && testQuestionPool.getCorrectAnswerNum().isEmpty()) {
					throw new ValidationException("Please select correct Answer");
				}
			}
			/* New Audit changes end */
			


			testQuestionPoolsFromDb = LMSHelper.copyNonNullFields(testQuestionPoolsFromDb, testQuestionPool);
			testQuestionPoolsFromDb.setLastModifiedBy(username);
			testQuestionPoolsFromDb.setTestType(testQuestionPoolsFromDb.getTestType());
			redirectAttrs.addAttribute("testPoolId", id);
			if (testQuestionPool.getQuestionType().equals("Numeric")) {

				double answerRangeFrom = Double.valueOf(testQuestionPool.getAnswerRangeFrom());
				double answerRangeTo = Double.valueOf(testQuestionPool.getAnswerRangeTo());
				double correctOption = Double.valueOf(testQuestionPool.getCorrectOption());
				if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
						&& answerRangeFrom < answerRangeTo) {

				} else {
					setNote(redirectAttrs, "Answer Range Given are not valid,Correct Option has to be between "
							+ " answer range from and to & answer range from field has to be less than answer range to field");
					return "redirect:/configureImageTestQuestionPoolForm";
				}
			}

			if (testQuestionPoolsService.update(testQuestionPoolsFromDb) > 0) {
				setSuccess(redirectAttrs, "Test Question Pool updated successfully");
			} else {
				setError(redirectAttrs, "Test Question cannot be updated");
			}

		} catch (ValidationException ve) {
			redirectAttrs.addAttribute("testPoolId", id);
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/configureImageTestQuestionPoolForm";
		}  catch (Exception e) {
			redirectAttrs.addAttribute("testPoolId", id);
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test Question");
			return "redirect:/configureImageTestQuestionPoolForm";
		}
		return "redirect:/configureImageTestQuestionPoolForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestQuestionPool", method = RequestMethod.POST)
	public String addTestQuestionPool(@ModelAttribute TestQuestionPools testQuestionPools, Model m,
			RedirectAttributes redirectAttrs, Principal principal, @RequestParam String testType,
			@RequestParam(required = false) String optionShuffle) {
		String username = principal.getName();
		redirectAttrs.addAttribute("testPoolId", testQuestionPools.getTestPoolId());
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(testQuestionPools, Arrays.asList("description","option1","option2","option3","option4","option5","option6","option7","option8"));
			if(testQuestionPools.getDescription() == null || testQuestionPools.getDescription().isEmpty()) {
				throw new ValidationException("Input field cannot be empty");
			}
			BusinessBypassRule.validateNumericNotAZero(testQuestionPools.getMarks());
			validateTestType(testQuestionPools.getTestType());
			if(testQuestionPools.getTestType().equals("Objective")) {
				validateTestQuestionType(testQuestionPools.getQuestionType());
				if(testQuestionPools.getQuestionType().equals("MCQ")) {
//					BusinessBypassRule.validateAlphaNumeric(testQuestionPools.getOption1());
//					BusinessBypassRule.validateAlphaNumeric(testQuestionPools.getOption2());
					if(testQuestionPools.getOption1() == null || testQuestionPools.getOption1().isEmpty()) {
						throw new ValidationException("Minimum 2 Option required.");
					}
					if(testQuestionPools.getOption2() == null || testQuestionPools.getOption2().isEmpty()) {
						throw new ValidationException("Minimum 2 Option required.");
					}
					validateTestQuestionSubType(testQuestionPools.getType());
					BusinessBypassRule.validateYesOrNo(testQuestionPools.getOptionShuffle());
					if(testQuestionPools.getCorrectOption() == null && testQuestionPools.getCorrectOption().isEmpty()) {
						throw new ValidationException("Please select correct Answer");
					}
					if (testQuestionPools.getCorrectOption().contains(",")) {
						List<String> splittedCorrectOption = new ArrayList<>();
						splittedCorrectOption = Arrays.asList(testQuestionPools.getCorrectOption().split(","));
						for(String s:splittedCorrectOption) {
							switch(s) {
							case "1": 
								if(testQuestionPools.getOption1() == null || testQuestionPools.getOption1().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "2":
								if(testQuestionPools.getOption2() == null || testQuestionPools.getOption2().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "3":
								if(testQuestionPools.getOption3() == null || testQuestionPools.getOption3().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "4":
								if(testQuestionPools.getOption4() == null || testQuestionPools.getOption4().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "5":
								if(testQuestionPools.getOption5() == null || testQuestionPools.getOption5().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "6":
								if(testQuestionPools.getOption6() == null || testQuestionPools.getOption6().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "7":
								if(testQuestionPools.getOption7() == null || testQuestionPools.getOption7().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "8":
								if(testQuestionPools.getOption8() == null || testQuestionPools.getOption8().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							default:
								throw new ValidationException("Please select correct Answer");
							}
						}
					}else {
						switch(testQuestionPools.getCorrectOption()) {
							case "1": 
								if(testQuestionPools.getOption1() == null || testQuestionPools.getOption1().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "2":
								if(testQuestionPools.getOption2() == null || testQuestionPools.getOption2().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "3":
								if(testQuestionPools.getOption3() == null || testQuestionPools.getOption3().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "4":
								if(testQuestionPools.getOption4() == null || testQuestionPools.getOption4().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "5":
								if(testQuestionPools.getOption5() == null || testQuestionPools.getOption5().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "6":
								if(testQuestionPools.getOption6() == null || testQuestionPools.getOption6().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "7":
								if(testQuestionPools.getOption7() == null || testQuestionPools.getOption7().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							case "8":
								if(testQuestionPools.getOption8() == null || testQuestionPools.getOption8().isEmpty()) {
									throw new ValidationException("Selected correct option cannot be empty.");
								}
								break;
							default:
								throw new ValidationException("Please select correct Answer");
						}
					}
				}
				if(testQuestionPools.getQuestionType().equals("Numeric")) {
					if(testQuestionPools.getCorrectAnswerNum() == null && testQuestionPools.getCorrectAnswerNum().isEmpty()) {
						throw new ValidationException("Correct Answer Input field cannot be empty");
					}
				}
			}
			
			/* New Audit changes end */
			if (testQuestionPools.getQuestionType().equals("Numeric")) {

				double answerRangeFrom = Double.valueOf(testQuestionPools.getAnswerRangeFrom());
				double answerRangeTo = Double.valueOf(testQuestionPools.getAnswerRangeTo());
				double correctOption = Double.valueOf(testQuestionPools.getCorrectAnswerNum());
				if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
						&& answerRangeFrom < answerRangeTo) {
					testQuestionPools.setCorrectOption(testQuestionPools.getCorrectAnswerNum());
				} else {
					setNote(redirectAttrs, "Answer Range Given are not valid,Correct Option has to be between "
							+ " answer range from and to & answer range from field has to be less than answer range to field");
					return "redirect:/configureImageTestQuestionPoolForm";
				}
			}

			if (testQuestionPools.getTestType().equals("Subjective")) {
				testQuestionPools.setQuestionType("Descriptive");
			}

			testQuestionPools.setCreatedBy(username);
			testQuestionPools.setLastModifiedBy(username);

			if (testQuestionPools.getQuestionType().equals("MCQ")) {
				if (testQuestionPools.getCorrectOption() == null || testQuestionPools.getType() == null) {
					setNote(redirectAttrs, "Please fill all the fields");
					return "redirect:/configureImageTestQuestionPoolForm";
				}
			}

			testQuestionPoolsService.insert(testQuestionPools);

			setSuccess(redirectAttrs, "Test Question Added Successfully To The Pool");
		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/configureImageTestQuestionPoolForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setNote(redirectAttrs, "Error in Adding Question");
			return "redirect:/configureImageTestQuestionPoolForm";
		}
		return "redirect:/configureImageTestQuestionPoolForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestPools", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewTestPools(

			Model m, @ModelAttribute TestPool testPool, @RequestParam(required = false) String courseId,
			@RequestParam(required = false) Long testId, Principal principal, HttpServletRequest request) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("viewTestPoolList", "View Test Pools", true, false));

		try {
			List<TestPool> testPoolsList = new ArrayList<>();

			String role = "";

			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				role = "ROLE_ADMIN";
			} else {
				role = "ROLE_FACULTY";
			}

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {

				if (courseId != null) {
					testPoolsList = testPoolService.findAllTestPoolsByUserAndCourse(username, courseId);
				} else {
					testPoolsList = testPoolService.findAllTestPoolsByUser(username, role);
				}

				if (testId != null) {
					m.addAttribute("isTestIdPresent", true);
					m.addAttribute("testId", testId);
				}
				m.addAttribute("testPoolsList", testPoolsList);

			}

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				testPoolsList = testPoolService.findAllTestPoolsByUserForAdmin(username);

				if (testId != null) {
					m.addAttribute("isTestIdPresent", true);
					m.addAttribute("testId", testId);
				}
				m.addAttribute("testPoolsList", testPoolsList);
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "test/testPoolList";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/deleteTestPool", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deleteTestPool(HttpServletRequest request, @ModelAttribute Test testPool, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {

		int id = Integer.parseInt(request.getParameter("id"));
		String username = principal.getName();

		TestPool testPoolDB = testPoolService.findByID(Long.valueOf(id));
		if (testPoolDB != null) {
			int testId = Integer.parseInt(request.getParameter("id"));

			testPoolService.deleteSoftById(String.valueOf(testId));
			setSuccess(redirectAttrs, "test-pool deleted successfully");
			return new ModelAndView("redirect:/viewTestPools");
		} else {
			setError(redirectAttrs, "Test Pool Not Found");
			return new ModelAndView("redirect:/viewTestPools");
		}

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestQuestionsByTestPool", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewTestQuestionsByTestPool(

			Model m, @ModelAttribute TestPool testPool, @RequestParam(required = false) Long testId,
			@RequestParam(required = false) String testPoolId, Principal principal, HttpServletRequest request,
			RedirectAttributes redirectAttrs) {

		String username = principal.getName();
		testPool.setId(Long.valueOf(testPoolId));

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("addTestQuestion", "View Test Pools", true, false));

		try {
			List<TestQuestionPools> testQuestionPoolList = new ArrayList<>();

			List<TestPool> testPoolsList = new ArrayList<>();

			String role = "";
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				role = "ROLE_FACULTY";
			} else {
				role = "ROLE_ADMIN";
			}
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

				testPoolsList = testPoolService.findAllTestPoolsByUser(username, role);

				if (testId != null) {
					Test testDB = testService.findByID(Long.valueOf(testId));
					m.addAttribute("isTestIdPresent", true);
					m.addAttribute("testId", testId);

					if ("Y".equals(testDB.getRandomQuestion())) {
						if ("Y".equals(testDB.getSameMarksQue())) {
							testPoolsList = testPoolService.findAllTestPoolsByUserAndSameMarks(username,
									testDB.getMarksPerQue(), role);

							m.addAttribute("sameMarksQue", testDB.getSameMarksQue());
							m.addAttribute("randomQuestion", testDB.getRandomQuestion());
						} else if ("N".equals(testDB.getSameMarksQue())) {

							List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(testId);

//							String marksStr = "";
//							for (TestConfiguration tc : testConfigList) {
//								marksStr = marksStr + ", " + tc.getMarks();
//							}
//							marksStr = marksStr.substring(2);
//
//							testPoolsList = testPoolService.findAllTestPoolsByUserAndDiffMarks(username, marksStr,
//									role);
							// New Pool Changes
							List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService
									.findAllByTestId(testId);
							String marksStr = "";
							if (testConfigList.size() > 0) {
								for (TestConfiguration tc : testConfigList) {
									marksStr = marksStr + ", " + tc.getMarks();
								}
							}
							if (marksStr.startsWith(", ")) {
								marksStr = marksStr.substring(2);
							}
							if (testPoolConfigList.size() > 0) {
								logger.info("testPoolsList--->" + testPoolsList);
								testPoolsList = testPoolService.findAllTestPoolsByUserAndTestPoolConfig(username);
								List<TestPool> testPLT = new ArrayList<TestPool>();
								for (TestPool tpl : testPoolsList) {
									for (TestPoolConfiguration tpcl : testPoolConfigList) {
										if (tpcl.getTestPoolId() == tpl.getId()) {
											if (!testPLT.stream().filter(o -> o.getId().equals(tpl.getId())).findFirst()
													.isPresent()) {
												logger.info("contentFolderId--->" + tpl.getId());
												testPLT.add(tpl);
											}
										}
									}
								}
								logger.info("testPLT--->" + testPLT);
								testPoolsList.clear();
								testPoolsList.addAll(testPLT);
							} else {
								logger.info("testPoolsList--->testConfigList");
								testPoolsList = testPoolService.findAllTestPoolsByUserAndDiffMarks(username, marksStr,
										role);
							}
							// New Pool Changes

							m.addAttribute("sameMarksQue", testDB.getSameMarksQue());
							m.addAttribute("randomQuestion", testDB.getRandomQuestion());
						}
					}

					int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(testId);

					if (chkStartDateToConfigQuestn == 0) {
						redirectAttrs.addAttribute("testId", testId);
						setError(redirectAttrs, "Cannot configure test questions, test has already started");
						return "redirect:/viewTestPools";
					}

					if ("Mix".equals(testDB.getTestType())) {
						testQuestionPoolList = testQuestionPoolsService
								.findAllTestQuestionsByTestPoolId(String.valueOf(testId), testPoolId);

						if ("Y".equals(testDB.getRandomQuestion())) {
							if ("Y".equals(testDB.getSameMarksQue())) {

								testQuestionPoolList = testQuestionPoolsService
										.findAllTestQuestionsByTestPoolIdAndMarks(testId.toString(), testPoolId,
												testDB.getMarksPerQue());

							} else if ("N".equals(testDB.getSameMarksQue())) {

								List<TestConfiguration> testConfigList = testConfigurationService
										.findAllByTestId(testId);

//								String marksStr = "";
//								for (TestConfiguration tc : testConfigList) {
//									marksStr = marksStr + ", " + tc.getMarks();
//								}
//								marksStr = marksStr.substring(2);
								// New Pool Changes
								List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService
										.findAllByTestId(testId);
								String marksStr = "";
								if (testConfigList.size() > 0) {
									for (TestConfiguration tc : testConfigList) {
										marksStr = marksStr + ", " + tc.getMarks();
									}
								} else if (testPoolConfigList.size() > 0) {
									for (TestPool tpl : testPoolsList) {
										marksStr = marksStr + ", " + tpl.getMarks();
									}
								}
								if (marksStr.startsWith(", ")) {
									marksStr = marksStr.substring(2);
								}
								// New Pool Changes
								testQuestionPoolList = testQuestionPoolsService
										.findAllTestQuestionsByTestPoolIdAndMarksList(testId.toString(), testPoolId,
												marksStr);

							}
						}

					} else {
						testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndTestType(
								String.valueOf(testId), testPoolId, testDB.getTestType());

						if ("Y".equals(testDB.getRandomQuestion())) {
							if ("Y".equals(testDB.getSameMarksQue())) {

								testQuestionPoolList = testQuestionPoolsService
										.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarks(String.valueOf(testId),
												testPoolId, testDB.getTestType(), testDB.getMarksPerQue());

							} else if ("N".equals(testDB.getSameMarksQue())) {

								List<TestConfiguration> testConfigList = testConfigurationService
										.findAllByTestId(testId);

//								String marksStr = "";
//								for (TestConfiguration tc : testConfigList) {
//									marksStr = marksStr + ", " + tc.getMarks();
//								}
//								marksStr = marksStr.substring(2);
//
//								testQuestionPoolList = testQuestionPoolsService
//										.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarksList(String.valueOf(testId),
//												testPoolId, testDB.getTestType(), marksStr);

								// New Pool Changes
								List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService
										.findAllByTestId(testId);
								String marksStr = "";
								if (testConfigList.size() > 0) {
									for (TestConfiguration tc : testConfigList) {
										marksStr = marksStr + ", " + tc.getMarks();
									}
								}
								if (marksStr.startsWith(", ")) {
									marksStr = marksStr.substring(2);
								}
								if (testPoolConfigList.size() > 0) {
									testQuestionPoolList = testQuestionPoolsService
											.findAllTestQuestionsByTestPoolIdAndTestPoolConfigurationAndTestType(
													String.valueOf(testId), testPoolId, testDB.getTestType());

								} else {
									testQuestionPoolList = testQuestionPoolsService
											.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarksList(
													String.valueOf(testId), testPoolId, testDB.getTestType(), marksStr);
								}
								// New Pool Changes

							}
						}
					}

					List<TestQuestion> testQuestionDBList = testQuestionService
							.findByTestId(Long.parseLong(testPool.getTestId()));

					Map<String, TestQuestion> mapOfTestQIdAndBean = new HashMap<>();
					for (TestQuestion tq : testQuestionDBList) {
						if (tq.getTestPoolId() != null) {
							mapOfTestQIdAndBean.put(String.valueOf(tq.getTestPoolId()), tq);
						}
					}

					for (TestQuestionPools tqp : testQuestionPoolList) {

						if (tqp.getTestQuestionId() != null) {

							tqp.setDescription(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getDescription());
							tqp.setMarks(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getMarks());
							tqp.setTestType(testDB.getTestType());
							tqp.setQuestionType(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getQuestionType());

							tqp.setType(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getType());

							if (tqp.getQuestionType().equalsIgnoreCase("MCQ")) {
								tqp.setOptionShuffle(
										mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOptionShuffle());

								tqp.setType(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getType());
								tqp.setOption1(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption1());
								tqp.setOption2(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption2());
								tqp.setOption3(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption3());
								tqp.setOption4(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption4());
								tqp.setOption5(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption5());
								tqp.setOption6(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption6());
								tqp.setOption7(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption7());
								tqp.setOption8(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getOption8());
							}
							tqp.setCorrectOption(
									mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getCorrectOption());

							if (tqp.getQuestionType().equalsIgnoreCase("Numeric")) {
								tqp.setAnswerRangeFrom(
										mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getAnswerRangeFrom());
								tqp.setAnswerRangeTo(
										mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getAnswerRangeTo());
							}
						}
					}
				} else {
					testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolId(testPoolId);

				}
				m.addAttribute("testPoolsList", testPoolsList);

				if (testQuestionPoolList.size() == 0) {
					setNote(redirectAttrs, "No Questions in this pool");

					if (testId != null) {
						redirectAttrs.addAttribute("testId", testId);

						return "redirect:/viewTestQuestionsByTestPoolForm";
					} else {
						return "redirect:/viewTestPools";
					}
				}
				testQuestionPoolList = removeSpecialCharactersOfPoolFromBean(testQuestionPoolList);
				testPool.setTestQuestionPools(testQuestionPoolList);
				m.addAttribute("testQuestionPoolList", testQuestionPoolList);
				m.addAttribute("testPool", testPool);
				m.addAttribute("testPoolId", testPoolId);
				m.addAttribute("showQuestionByPool", true);

				if (testId != null) {
					Test testDB = testService.findByID(Long.valueOf(testId));
					List<TestQuestion> testQuestionDBList = testQuestionService
							.findByTestId(Long.parseLong(testPool.getTestId()));
					double TotalMarks = 0.0;

					for (TestQuestion tq : testQuestionDBList) {

						TotalMarks = TotalMarks + tq.getMarks();

					}
					List<StudentTest> studentTestByTestId = studentTestService.findOneTest(testDB.getId());

					if (!"Y".equals(testDB.getRandomQuestion())) {

						if (TotalMarks == testDB.getMaxScore()) {
							if (!"Y".equals(testDB.getAutoAllocateToStudents())) {
								m.addAttribute("showProceed", true);
							} else {
								if (studentTestByTestId.size() == 0) {
									autoAllocateStudent(testDB, username);
								}else if (studentTestByTestId.size() > 0) {
									reAllocateStudent(testDB);
								}
								m.addAttribute("showStudents", true);
							}
						} else {
							m.addAttribute("showProceed", false);
							m.addAttribute("showStudents", false);
						}
					} else {
						if (TotalMarks >= testDB.getMaxScore()) {
							//m.addAttribute("showProceed", true);

							if (!"Y".equals(testDB.getAutoAllocateToStudents())) {
								m.addAttribute("showProceed", true);
							} else {
								if (studentTestByTestId.size() == 0) {
									autoAllocateStudent(testDB, username);
								}else if (studentTestByTestId.size() > 0) {
									reAllocateStudent(testDB);
								}
								m.addAttribute("showStudents", true);
							}

						} else {
							m.addAttribute("showProceed", false);
							m.addAttribute("showStudents", false);
						}
					}

				}
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "test/testQuestionPoolList";
	}

	public List<TestQuestionPools> removeSpecialCharactersOfPoolFromBean(List<TestQuestionPools> testQuestions) {

		for (TestQuestionPools tq : testQuestions) {

			if (tq.getOption1() != null) {
				if (tq.getOption1().contains("<")) {
					logger.info("opt entered");
					tq.setOption1(tq.getOption1().replace("<", " < "));
				}
			}
			if (tq.getOption2() != null) {
				if (tq.getOption2().contains("<")) {
					logger.info("opt entered");
					tq.setOption2(tq.getOption2().replace("<", " < "));
				}
			}
			if (tq.getOption3() != null) {
				if (tq.getOption3().contains("<")) {
					logger.info("opt entered");
					tq.setOption3(tq.getOption3().replace("<", " < "));
				}
			}
			if (tq.getOption4() != null) {
				if (tq.getOption4().contains("<")) {
					logger.info("opt entered");
					tq.setOption4(tq.getOption4().replace("<", " < "));
				}
			}
			if (tq.getOption5() != null) {
				if (tq.getOption5().contains("<")) {
					logger.info("opt entered");
					tq.setOption5(tq.getOption5().replace("<", " < "));
				}
			}
			if (tq.getOption6() != null) {
				if (tq.getOption6().contains("<")) {
					logger.info("opt entered");
					tq.setOption6(tq.getOption6().replace("<", " < "));
				}
			}
			if (tq.getOption7() != null) {
				if (tq.getOption7().contains("<")) {
					logger.info("opt entered");
					tq.setOption7(tq.getOption7().replace("<", " < "));
				}
			}
			if (tq.getOption8() != null) {
				if (tq.getOption8().contains("<")) {
					logger.info("opt entered");
					tq.setOption8(tq.getOption8().replace("<", " < "));
				}
			}
		}

		return testQuestions;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestQuestionsByTestPoolForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewTestQuestionsByTestPoolForm(

			Model m, @ModelAttribute TestPool testPool, @RequestParam(required = false) Long testId,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("viewTestPoolList", "View Test Pools", true, false));

		try {
			List<TestPool> testPoolsList = new ArrayList<>();

			String role = "";

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				role = "ROLE_FACULTY";
			} else {
				role = "ROLE_ADMIN";
			}

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

				testPoolsList = testPoolService.findAllTestPoolsByUser(username, role);
				if (testId != null) {
					m.addAttribute("isTestIdPresent", true);
					m.addAttribute("testId", testId);

					Test test = testService.findByID(testId);

					if ("Y".equals(test.getRandomQuestion())) {
						if ("Y".equals(test.getSameMarksQue())) {
							testPoolsList = testPoolService.findAllTestPoolsByUserAndSameMarks(username,
									test.getMarksPerQue(), role);

							m.addAttribute("sameMarksQue", test.getSameMarksQue());
						} else if ("N".equals(test.getSameMarksQue())) {
//							List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(testId);
//							String marksStr = "";
//							for (TestConfiguration tc : testConfigList) {
//								marksStr = marksStr + ", " + tc.getMarks();
//							}
//							marksStr = marksStr.substring(2);
//
//							testPoolsList = testPoolService.findAllTestPoolsByUserAndDiffMarks(username, marksStr,
//									role);

							// New Pool Changes
							List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(testId);

							List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService
									.findAllByTestId(testId);
							String marksStr = "";
							if (testConfigList.size() > 0) {
								for (TestConfiguration tc : testConfigList) {
									marksStr = marksStr + ", " + tc.getMarks();
								}
							}
							if (marksStr.startsWith(", ")) {
								marksStr = marksStr.substring(2);
							}
							if (testPoolConfigList.size() > 0) {
								logger.info("testPoolsList--->" + testPoolsList);
								testPoolsList = testPoolService.findAllTestPoolsByUserAndTestPoolConfig(username);
								List<TestPool> testPLT = new ArrayList<TestPool>();
								for (TestPool tpl : testPoolsList) {
									for (TestPoolConfiguration tpcl : testPoolConfigList) {
										if (tpcl.getTestPoolId() == tpl.getId()) {
											if (!testPLT.stream().filter(o -> o.getId().equals(tpl.getId())).findFirst()
													.isPresent()) {
												logger.info("contentFolderId--->" + tpl.getId());
												testPLT.add(tpl);
											}
										}
									}
								}
								logger.info("testPLT--->" + testPLT);
								testPoolsList.clear();
								testPoolsList.addAll(testPLT);
							} else {
								logger.info("testPoolsList--->testConfigList");
								logger.info("marksStr--->" + marksStr);
								testPoolsList = testPoolService.findAllTestPoolsByUserAndDiffMarks(username, marksStr,
										role);
							}
							// New Pool Changes
							m.addAttribute("sameMarksQue", test.getSameMarksQue());
						}
					} else {
						if ("Y".equals(test.getSameMarksQue())) {
							testPoolsList = testPoolService.findAllTestPoolsByUserAndSameMarks(username,
									test.getMarksPerQue(), role);

							m.addAttribute("sameMarksQue", test.getSameMarksQue());
						}
					}

				}
				m.addAttribute("testPoolsList", testPoolsList);

				m.addAttribute("showQuestionByPool", false);
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "test/testQuestionPoolList";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/saveAllTestQuestionsByTestPool", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveAllTestQuestionsByTestPool(

			Model m, @ModelAttribute TestPool testPool, Principal principal, HttpServletRequest request,
			RedirectAttributes redirectAttr) {
		String username = principal.getName();

		List<TestQuestionPools> testQuestionPoolList = new ArrayList<>();

		Test testDB = testService.findByID(Long.parseLong(testPool.getTestId()));

		if ("Mix".equals(testDB.getTestType())) {
			testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolId(testPool.getTestId(),
					String.valueOf(testPool.getId()));

			if ("Y".equals(testDB.getRandomQuestion())) {
				if ("Y".equals(testDB.getSameMarksQue())) {

					testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndMarks(
							testPool.getTestId(), String.valueOf(testPool.getId()), testDB.getMarksPerQue());

				} else if ("N".equals(testDB.getSameMarksQue())) {
					List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(testDB.getId());
//					String marksStr = "";
//					for (TestConfiguration tc : testConfigList) {
//						marksStr = marksStr + ", " + tc.getMarks();
//					}
//					marksStr = marksStr.substring(2);
					// New Pool Changes
					List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService
							.findAllByTestId(testDB.getId());
					String marksStr = "";
					if (testConfigList.size() > 0) {
						for (TestConfiguration tc : testConfigList) {
							marksStr = marksStr + ", " + tc.getMarks();
						}
					} else if (testPoolConfigList.size() > 0) {
						for (TestPoolConfiguration tpl : testPoolConfigList) {
							marksStr = marksStr + ", " + tpl.getMarks();
						}
					}
					if (marksStr.startsWith(", ")) {
						marksStr = marksStr.substring(2);
					}
					// New Pool Changes
					testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndMarksList(
							testPool.getTestId(), String.valueOf(testPool.getId()), marksStr);
				}

			}

		} else {
			testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndTestType(
					testPool.getTestId(), String.valueOf(testPool.getId()), testDB.getTestType());

			if ("Y".equals(testDB.getRandomQuestion())) {
				if ("Y".equals(testDB.getSameMarksQue())) {

					testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarks(
							testPool.getTestId(), String.valueOf(testPool.getId()), testDB.getTestType(),
							testDB.getMarksPerQue());

				} else if ("N".equals(testDB.getSameMarksQue())) {
					List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(testDB.getId());
//					String marksStr = "";
//					for (TestConfiguration tc : testConfigList) {
//						marksStr = marksStr + ", " + tc.getMarks();
//					}
//					marksStr = marksStr.substring(2);
					// New Pool Changes
					List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService
							.findAllByTestId(testDB.getId());
					String marksStr = "";
					if (testConfigList.size() > 0) {
						for (TestConfiguration tc : testConfigList) {
							marksStr = marksStr + ", " + tc.getMarks();
						}
					} else if (testPoolConfigList.size() > 0) {
						for (TestPoolConfiguration tpl : testPoolConfigList) {
							marksStr = marksStr + ", " + tpl.getMarks();
						}
					}
					if (marksStr.startsWith(", ")) {
						marksStr = marksStr.substring(2);
					}
					// New Pool Changes
					testQuestionPoolList = testQuestionPoolsService
							.findAllTestQuestionsByTestPoolIdAndTestTypeAndMarksList(String.valueOf(testDB.getId()),
									String.valueOf(testPool.getId()), testDB.getTestType(), marksStr);
				}

			}

		}

		for (TestQuestionPools tqp : testQuestionPoolList) {

			testPool.getTestQuestionsPoolIds().add(tqp.getId().toString());
		}

		List<Map<String, Object>> testQuestionPoolsByIds = testQuestionPoolsService
				.findByListOfIds(testPool.getTestQuestionsPoolIds());

		List<TestQuestion> testQuestionList = conversionFromQuestionPoolToTestQuestion(testQuestionPoolsByIds,
				testPool.getTestId(), username);

		List<TestQuestion> testQuestionDBList = testQuestionService.findByTestId(Long.parseLong(testPool.getTestId()));

		double TotalMarks = 0.0;
		for (TestQuestion tq : testQuestionList) {

			TotalMarks = TotalMarks + tq.getMarks();

		}
		for (TestQuestion tq : testQuestionDBList) {

			TotalMarks = TotalMarks + tq.getMarks();

		}

		if ("N".equals(testDB.getRandomQuestion())) {

			if (TotalMarks < testDB.getMaxScore()) {

				testQuestionService.insertBatch(testQuestionList);
				setSuccess(redirectAttr, "Test Question Added successfully from the pool");
				setNote(redirectAttr,
						"Please add more question as total marks of questions should be match with total marks of test.");
			} else if (TotalMarks == testDB.getMaxScore()) {
				testQuestionService.insertBatch(testQuestionList);

				setSuccess(redirectAttr, "Test Question Added successfully from the pool");
			} else {

				setNote(redirectAttr, "Sum Of score of added questions is more than the total score of test.");
			}

		} else {

			testQuestionService.insertBatch(testQuestionList);
			setSuccess(redirectAttr, "Test Question Added successfully from the pool");
		}

		redirectAttr.addAttribute("testPoolId", testPool.getId());
		redirectAttr.addAttribute("testId", testPool.getTestId());

		return "redirect:/viewTestQuestionsByTestPool";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/saveTestQuestionsByTestPool", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveTestQuestionsByTestPool(

			Model m, @ModelAttribute TestPool testPool, Principal principal, HttpServletRequest request,
			RedirectAttributes redirectAttr) {
		String username = principal.getName();

		List<Map<String, Object>> testQuestionPoolsByIds = testQuestionPoolsService
				.findByListOfIds(testPool.getTestQuestionsPoolIds());

		List<TestQuestion> testQuestionList = conversionFromQuestionPoolToTestQuestion(testQuestionPoolsByIds,
				testPool.getTestId(), username);

		Test testDB = testService.findByID(Long.parseLong(testPool.getTestId()));

		List<TestQuestion> testQuestionDBList = testQuestionService.findByTestId(Long.parseLong(testPool.getTestId()));

		double TotalMarks = 0.0;
		for (TestQuestion tq : testQuestionList) {

			TotalMarks = TotalMarks + tq.getMarks();

		}
		for (TestQuestion tq : testQuestionDBList) {

			TotalMarks = TotalMarks + tq.getMarks();

		}

		if ("N".equals(testDB.getRandomQuestion())) {

			if (TotalMarks < testDB.getMaxScore()) {

				testQuestionService.insertBatch(testQuestionList);
				setSuccess(redirectAttr, "Test Question Added successfully from the pool");
				setNote(redirectAttr,
						"Please add more question as total marks of questions should be match with total marks of test.");
			} else if (TotalMarks == testDB.getMaxScore()) {
				testQuestionService.insertBatch(testQuestionList);

				setSuccess(redirectAttr, "Test Question Added successfully from the pool");
			} else {

				setNote(redirectAttr, "Sum Of score of added questions is more than the total score of test.");
			}

		} else {

			testQuestionService.insertBatch(testQuestionList);
			setSuccess(redirectAttr, "Test Question Added successfully from the pool");
		}

		redirectAttr.addAttribute("testPoolId", testPool.getId());
		redirectAttr.addAttribute("testId", testPool.getTestId());

		return "redirect:/viewTestQuestionsByTestPool";

	}

	public List<TestQuestion> conversionFromQuestionPoolToTestQuestion(List<Map<String, Object>> mapper, String testId,
			String username) {

		List<TestQuestion> testQuestionList = new ArrayList<>();

		List<TestQuestion> testQuestionDB = testQuestionService.findByTestId(Long.valueOf(testId));

		Test testDB = testService.findByID(Long.valueOf(testId));

		double totalMarks = 0.0;

		if (testQuestionDB.size() > 0) {
			for (TestQuestion tq : testQuestionDB) {
				totalMarks = totalMarks + tq.getMarks();
			}
		} else {
			totalMarks = 0.0;
		}
		for (Map<String, Object> map : mapper) {
			TestQuestion tq = new TestQuestion();

			tq.setTestId(Long.valueOf(testId));
			BigDecimal marks = (BigDecimal) map.get("marks");
			// String marksStr = "123";

			tq.setDescription((String) map.get("description"));
			tq.setMarks(marks.doubleValue());
			tq.setType((String) map.get("type"));
			tq.setOption1((String) map.get("option1"));
			tq.setOption2((String) map.get("option2"));
			tq.setOption3((String) map.get("option3"));
			tq.setOption4((String) map.get("option4"));
			tq.setOption5((String) map.get("option5"));
			tq.setOption6((String) map.get("option6"));
			tq.setOption7((String) map.get("option7"));
			tq.setOption8((String) map.get("option8"));
			tq.setOptionShuffle((String) map.get("optionShuffle"));
			tq.setCorrectOption((String) map.get("correctOption"));
			tq.setTestPoolId(String.valueOf((Integer) map.get("id")));
			tq.setQuestionType((String) map.get("questionType"));
			tq.setAnswerRangeFrom((String) map.get("answerRangeFrom"));
			tq.setAnswerRangeTo((String) map.get("answerRangeTo"));
			tq.setCreatedBy(username);
			tq.setLastModifiedBy(username);

			totalMarks = totalMarks + marks.doubleValue();
			testQuestionList.add(tq);

		}
		return testQuestionList;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTestReportByTestIdAttemptWise", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String downloadTestReportByTestIdAttemptWise(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String testId) throws URIException {
		Token userDetails = (Token) p;
		List<TestQuestion> testDetailsByTestId = new ArrayList<>();
		Test testDB = testService.findByID(Long.valueOf(testId));

		if ("Y".equals(testDB.getIsCreatedByAdmin()) && userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			List<String> courseIds = userCourseDAO.findCoursesByParam(testDB.getModuleId(), userDetails.getProgramId(),
					String.valueOf(testDB.getAcadYear()), p.getName());
			testDetailsByTestId = testQuestionService.findTestByAdminDetailsQuestionWiseAndAttemptWise(testId,
					courseIds);
		} else {
			testDetailsByTestId = testQuestionService.findTestDetailsQuestionWiseAndAttemptWise(Long.valueOf(testId));
		}

		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("username", "AttemptNo", "Student Answer",
				"Student Marks", "Description", "Correct Option", "Option1", "option2", "option3", "option4", "option5",
				"option6", "option7", "option8", "answerRangeFrom", "answerRangeTo"));
		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfTestDetails = new ArrayList<>();
			fileName = testDB.getTestName() + "-AttemptWise.xlsx";
			fileName = fileName.replace("/", "_");
			fileName = fileName.replaceAll(",", "_");
			filePath = downloadAllFolder + File.separator + fileName;

			for (TestQuestion tq : testDetailsByTestId) {
				Map<String, Object> mapOfTestDetails = new HashMap<>();

				String studentAnswer = tq.getAnswer() != null ? String.valueOf(tq.getAnswer()) : "";
				String studentMarks = tq.getStudentMarks() != null ? String.valueOf(tq.getStudentMarks()) : "";
				String answerRangeFrom = tq.getAnswerRangeFrom() != null ? tq.getAnswerRangeFrom() : "";
				String answerRangeTo = tq.getAnswerRangeTo() != null ? tq.getAnswerRangeTo() : "";

				mapOfTestDetails.put("username", tq.getUsername());
				mapOfTestDetails.put("AttemptNo", tq.getAttempts());
				mapOfTestDetails.put("Student Answer", studentAnswer);
				mapOfTestDetails.put("Student Marks", studentMarks);
				//mapOfTestDetails.put("Description", tq.getDescription());
				if(tq.getDescription().length() > 20000) {	
					String des = tq.getDescription().substring(0, 20000);
					mapOfTestDetails.put("Description", des);
				}else {
					mapOfTestDetails.put("Description", tq.getDescription());
				}
				mapOfTestDetails.put("Correct Option", tq.getCorrectOption());
				mapOfTestDetails.put("Option1", tq.getOption1());
				mapOfTestDetails.put("option2", tq.getOption2());
				mapOfTestDetails.put("option3", tq.getOption3());
				mapOfTestDetails.put("option4", tq.getOption4());
				mapOfTestDetails.put("option5", tq.getOption5());
				mapOfTestDetails.put("option6", tq.getOption6());
				mapOfTestDetails.put("option7", tq.getOption7());
				mapOfTestDetails.put("option8", tq.getOption8());
				mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
				mapOfTestDetails.put("answerRangeTo", answerRangeTo);

				if (tq.getQuestionType() != null) {
					if (tq.getQuestionType().equals("Numeric")) {
						mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
						mapOfTestDetails.put("answerRangeTo", answerRangeTo);
					}
				}

				listOfMapOfTestDetails.add(mapOfTestDetails);
			}
			excelCreater.CreateExcelFile(listOfMapOfTestDetails, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// copy it to response's OutputStream
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
			}

		}
		return null;
	}

	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/downloadTestReportByTestIdAndUsernameAttemptWise", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String downloadTestReportByTestIdAndUsernameAttemptWise(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String testId) throws URIException {

		String username = p.getName();

		List<TestQuestion> testDetailsByTestId = testQuestionService
				.findTestDetailsQuestionWiseByUsernameAttemptWise(Long.valueOf(testId), username);

		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("username", "AttemptNo", "Student Answer",
				"Student Marks", "Description", "Correct Option", "Option1", "option2", "option3", "option4", "option5",
				"option6", "option7", "option8", "answerRangeFrom", "answerRangeTo"));
		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfTestDetails = new ArrayList<>();
			fileName = testService.findByID(Long.valueOf(testId)).getTestName() + "-AttemptWise.xlsx";
			fileName = fileName.replace("/", "_");

			filePath = downloadAllFolder + File.separator + fileName;

			for (TestQuestion tq : testDetailsByTestId) {

				Map<String, Object> mapOfTestDetails = new HashMap<>();

				String studentAnswer = tq.getAnswer() != null ? String.valueOf(tq.getAnswer()) : "";
				String studentMarks = tq.getStudentMarks() != null ? String.valueOf(tq.getStudentMarks()) : "";
				String answerRangeFrom = tq.getAnswerRangeFrom() != null ? tq.getAnswerRangeFrom() : "";
				String answerRangeTo = tq.getAnswerRangeTo() != null ? tq.getAnswerRangeTo() : "";

				mapOfTestDetails.put("AttemptNo", tq.getAttempts());
				mapOfTestDetails.put("username", tq.getUsername());
				mapOfTestDetails.put("Student Answer", studentAnswer);
				mapOfTestDetails.put("Student Marks", studentMarks);
				//mapOfTestDetails.put("Description", tq.getDescription());
				if(tq.getDescription().length() > 20000) {	
					String des = tq.getDescription().substring(0, 20000);
					mapOfTestDetails.put("Description", des);
				}else {
					mapOfTestDetails.put("Description", tq.getDescription());
				}
				mapOfTestDetails.put("Correct Option", tq.getCorrectOption());
				mapOfTestDetails.put("Option1", tq.getOption1());
				mapOfTestDetails.put("option2", tq.getOption2());
				mapOfTestDetails.put("option3", tq.getOption3());
				mapOfTestDetails.put("option4", tq.getOption4());
				mapOfTestDetails.put("option5", tq.getOption5());
				mapOfTestDetails.put("option6", tq.getOption6());
				mapOfTestDetails.put("option7", tq.getOption7());
				mapOfTestDetails.put("option8", tq.getOption8());
				mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
				mapOfTestDetails.put("answerRangeTo", answerRangeTo);

				if (tq.getQuestionType() != null) {
					if (tq.getQuestionType().equals("Numeric")) {
						mapOfTestDetails.put("answerRangeFrom", answerRangeFrom);
						mapOfTestDetails.put("answerRangeTo", answerRangeTo);

					}
				}

				listOfMapOfTestDetails.add(mapOfTestDetails);
			}
			excelCreater.CreateExcelFile(listOfMapOfTestDetails, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// copy it to response's OutputStream
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
			}

		}
		return null;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/updateTestQuestionPool", method = RequestMethod.POST)
	public String updateTestQuestionPool(@ModelAttribute TestPool test, RedirectAttributes redirectAttrs,
			Principal principal) {
		String username = principal.getName();

		TestQuestionPools testQuestionPool = test.getTestQuestionPools().get(test.getTestQuestionPools().size() - 1);
		logger.info("testquestionpool" + testQuestionPool);
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(testQuestionPool, Arrays.asList("description","option1","option2","option3","option4","option5","option6","option7","option8"));
			if(testQuestionPool.getDescription() == null || testQuestionPool.getDescription().isEmpty()) {
				throw new ValidationException("Input field cannot be empty");
			}
			BusinessBypassRule.validateNumericNotAZero(testQuestionPool.getMarks());
			validateTestQuestionType(testQuestionPool.getQuestionType());
			if(testQuestionPool.getQuestionType().equals("MCQ")) {
//				BusinessBypassRule.validateAlphaNumeric(testQuestionPool.getOption1());
//				BusinessBypassRule.validateAlphaNumeric(testQuestionPool.getOption2());
				if(testQuestionPool.getOption1() == null || testQuestionPool.getOption1().isEmpty()) {
					throw new ValidationException("Minimum 2 Option required.");
				}
				if(testQuestionPool.getOption2() == null || testQuestionPool.getOption2().isEmpty()) {
					throw new ValidationException("Minimum 2 Option required.");
				}
				validateTestQuestionSubType(testQuestionPool.getType());
				BusinessBypassRule.validateYesOrNo(testQuestionPool.getOptionShuffle());
				if(testQuestionPool.getCorrectOption() == null && testQuestionPool.getCorrectOption().isEmpty()) {
					throw new ValidationException("Please select correct Answer");
				}
				if (testQuestionPool.getCorrectOption().contains(",")) {
					List<String> splittedCorrectOption = new ArrayList<>();
					splittedCorrectOption = Arrays.asList(testQuestionPool.getCorrectOption().split(","));
					for(String s:splittedCorrectOption) {
						switch(s) {
						case "1": 
							if(testQuestionPool.getOption1() == null || testQuestionPool.getOption1().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "2":
							if(testQuestionPool.getOption2() == null || testQuestionPool.getOption2().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "3":
							if(testQuestionPool.getOption3() == null || testQuestionPool.getOption3().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "4":
							if(testQuestionPool.getOption4() == null || testQuestionPool.getOption4().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "5":
							if(testQuestionPool.getOption5() == null || testQuestionPool.getOption5().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "6":
							if(testQuestionPool.getOption6() == null || testQuestionPool.getOption6().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "7":
							if(testQuestionPool.getOption7() == null || testQuestionPool.getOption7().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "8":
							if(testQuestionPool.getOption8() == null || testQuestionPool.getOption8().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						default:
							throw new ValidationException("Please select correct Answer");
						}
					}
				}else {
					switch(testQuestionPool.getCorrectOption()) {
						case "1": 
							if(testQuestionPool.getOption1() == null || testQuestionPool.getOption1().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "2":
							if(testQuestionPool.getOption2() == null || testQuestionPool.getOption2().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "3":
							if(testQuestionPool.getOption3() == null || testQuestionPool.getOption3().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "4":
							if(testQuestionPool.getOption4() == null || testQuestionPool.getOption4().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "5":
							if(testQuestionPool.getOption5() == null || testQuestionPool.getOption5().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "6":
							if(testQuestionPool.getOption6() == null || testQuestionPool.getOption6().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "7":
							if(testQuestionPool.getOption7() == null || testQuestionPool.getOption7().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						case "8":
							if(testQuestionPool.getOption8() == null || testQuestionPool.getOption8().isEmpty()) {
								throw new ValidationException("Selected correct option cannot be empty.");
							}
							break;
						default:
							throw new ValidationException("Please select correct Answer");
					}
				}
			}
			if(testQuestionPool.getQuestionType().equals("Numeric")) {
				if(testQuestionPool.getCorrectAnswerNum() == null && testQuestionPool.getCorrectAnswerNum().isEmpty()) {
					throw new ValidationException("Correct Answer Input field cannot be empty");
				}
			}
			/* New Audit changes end */
			TestQuestionPools testQuestionPoolsFromDb = testQuestionPoolsService.findByID(testQuestionPool.getId());

			int id = testQuestionPoolsFromDb.getTestPoolId().intValue();

			testQuestionPoolsFromDb = LMSHelper.copyNonNullFields(testQuestionPoolsFromDb, testQuestionPool);
			testQuestionPoolsFromDb.setLastModifiedBy(username);
			testQuestionPoolsFromDb.setTestType(testQuestionPoolsFromDb.getTestType());
			redirectAttrs.addAttribute("testPoolId", id);
			if (testQuestionPool.getQuestionType().equals("Numeric")) {

				double answerRangeFrom = Double.valueOf(testQuestionPool.getAnswerRangeFrom());
				double answerRangeTo = Double.valueOf(testQuestionPool.getAnswerRangeTo());
				double correctOption = 0.0;
				logger.info("correct optin" + testQuestionPool.getCorrectOption());
				if (testQuestionPool.getCorrectOption() != null) {
					correctOption = Double.valueOf(testQuestionPool.getCorrectOption());
				} else {
					correctOption = Double.valueOf(testQuestionPool.getCorrectAnswerNum());
				}
				if (answerRangeFrom <= correctOption && answerRangeTo >= correctOption
						&& answerRangeFrom < answerRangeTo) {
					testQuestionPool.setCorrectOption(testQuestionPool.getCorrectAnswerNum());
				} else {
					setNote(redirectAttrs, "Answer Range Given are not valid,Correct Option has to be between "
							+ " answer range from and to & answer range from field has to be less than answer range to field");
					return "redirect:/viewTestQuestionsByTestPool";
				}
			}

			if (testQuestionPoolsService.update(testQuestionPoolsFromDb) > 0) {
				setSuccess(redirectAttrs, "Test Question Pool updated successfully");
			} else {
				setError(redirectAttrs, "Test Question cannot be updated");
			}

		} catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/viewTestQuestionsByTestPool";
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test Question");
			return "redirect:/viewTestQuestionsByTestPool";
		}
		return "redirect:/viewTestQuestionsByTestPool";
	}
	
	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/removeStudentTest", method = { RequestMethod.GET, RequestMethod.POST })
	public String removeStudentTest(Model m, RedirectAttributes redirectAttrs, Principal p, @ModelAttribute Test test,
			@RequestParam(required = false) String name[], @RequestParam(required = false) String id,
			HttpServletResponse response, HttpServletRequest request) {

		try {

			List<String> updatedList = Arrays.asList(name);

			List<String> newList = new ArrayList<String>();
			newList.addAll(updatedList);

			for (String username : newList) {
				studentTestService.removeStudent_Test(username, id);
			}

			setSuccess(redirectAttrs, "Entries deleted successfully");

		} catch (Exception e) {

			logger.error("Exception", e);

		}

		redirectAttrs.addAttribute("testId", id);
		return "redirect:/viewTestDetails";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/hideResultsToStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String hideResultsToStudents(@RequestParam("id") Long id, Model m, Principal principal) {

		String username = principal.getName();
		try {
			Test test = testService.findByID(id);
			test.setShowResultsToStudents("N");
			testService.hideResults(test.getId());
			setSuccess(m, "Results will be hidden from students!");
			return "Success";
		} catch (Exception e) {

			logger.error("Error, " + e);
			return "Error";
		}

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/updateTestPool", method = RequestMethod.POST)
	public String updateTestPool(@ModelAttribute TestPool testPool, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		

		try {

			/* New Audit changes start */
			HtmlValidation.validateHtml(testPool, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(testPool.getTestPoolName());
			/* New Audit changes end */
			TestPool testPoolFromDb = testPoolService.findByID(testPool.getId());
			testPoolFromDb = LMSHelper.copyNonNullFields(testPoolFromDb, testPool);
			testPoolFromDb.setLastModifiedBy(username);

			if (testPoolService.update(testPoolFromDb) > 0)
				setSuccess(redirectAttrs, "Test-Pool updated successfully");
			else
				setError(redirectAttrs, "Test-Pool cannot be updated");

		} catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			redirectAttrs.addFlashAttribute("testPool", testPool);
			redirectAttrs.addAttribute("id", testPool.getId());
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/addTestPoolFormByAdmin";
			} else {
				return "redirect:/addTestPoolForm";
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test Pool");
			redirectAttrs.addFlashAttribute("testPool", testPool);
			redirectAttrs.addAttribute("id", testPool.getId());
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/addTestPoolFormByAdmin";
			} else {
				return "redirect:/addTestPoolForm";
			}
		}
		redirectAttrs.addAttribute("courseId", testPool.getCourseId());
		return "redirect:/viewTestPools";
	}

	public void autoAllocateStudent(Test testDB, String username) {

		List<StudentTest> stListForAllocated = studentTestService.findByTestId(testDB.getId());
		if (stListForAllocated.size() > 0) {
			studentTestService.removeStudentQRespFile(testDB.getId());
			studentTestService.removeStudentQueResp(testDB.getId());
		}

		List<UserCourse> userCourseListByCourseId = new ArrayList<>();

		if ("Y".equals(testDB.getIsCreatedByAdmin())) {
			userCourseListByCourseId = userCourseService.getStudentsByModuleId(String.valueOf(testDB.getModuleId()),
					String.valueOf(testDB.getAcadYear()));
		} else {
			userCourseListByCourseId = userCourseService.getStudentsByCourseId(String.valueOf(testDB.getCourseId()));
		}

		List<StudentTest> studentTestList = createStudentTestList(userCourseListByCourseId, testDB, username);

		if (studentTestList.size() > 0) {

			studentTestService.insertBatch(studentTestList);

			// Code Here For Allocation of Test Questions...

			logger.info("file creation entered");
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDB.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDB.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();

				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testDB.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (subFolderP.exists()) {
				try {
					logger.info("Delete Old Folder---->");
					FileUtils.deleteDirectory(subFolderP);
				} catch (IOException e) {
					logger.error("Error--->", e);
				}
				subFolderP.mkdir();
				logger.info("New Folder created---->");
				logger.info("subfolder created");
			} else {
				subFolderP.mkdir();
				logger.info("New Folder created else---->");
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			logger.info("subfolder created" + subFolderPath);

			for (StudentTest st : stListForAllocated) {
				File jsonFilePath = new File(subFolderPath + "/" + st.getUsername() + ".json");
				if (jsonFilePath.exists()) {
					jsonFilePath.delete();
					logger.info("file Deleted----> " + st.getUsername() + ".json");
				}
			}

			studentTestService.allocateTestQuestionsForAllStudent(testDB.getId(), subFolderPath);
		}
	}

	public void reAllocateStudent(Test testDB) {

		List<StudentTest> studentTestList = studentTestService.findByTestId(testDB.getId());
		// .getStudentsByCourseId(String.valueOf(testDB.getCourseId()));

		// List<StudentTest> studentTestList =
		// createStudentTestList(userCourseListByCourseId, testDB, username);

		if (studentTestList.size() > 0) {

			// studentTestService.insertBatch(studentTestList);

			// Code Here For Allocation of Test Questions...

			logger.info("file creation entered");
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDB.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDB.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();

				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testDB.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (subFolderP.exists()) {
				try {
					logger.info("Delete Old Folder---->");
					FileUtils.deleteDirectory(subFolderP);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Error--->", e);
				}
				subFolderP.mkdir();
				logger.info("New Folder created---->");
				logger.info("subfolder created");
			}else {
				subFolderP.mkdirs();
				logger.info("New Folder created---->");
				logger.info("subfolder created");
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			logger.info("subfolder created" + subFolderPath);

//			for(StudentTest st:studentTestList){
//				File jsonFilePath = new File(subFolderPath +"/"+ st.getUsername()+".json");
//				if(jsonFilePath.exists()){
//					jsonFilePath.delete();
//				}
//			}

			studentTestService.removeStudentQRespFile(testDB.getId());
			studentTestService.removeStudentQueResp(testDB.getId());

			studentTestService.allocateTestQuestionsForAllStudent(testDB.getId(), subFolderPath);
		}
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/exportTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String exportTestForm(@ModelAttribute Test test, Model m, @RequestParam(required = false) Long campusId,
			Principal p) {
		m.addAttribute("webPage", new WebPage("content", "Export Test", true, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		test = testService.findByID(test.getId());
		Course c = courseService.findByID(test.getCourseId());

		test.setCourseName(c.getCourseName());
		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("test", test);

		try {
			WebTarget webTarget = client.target(
					URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getAllProgramDetailsByUsername?username=" + username));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			List<Program> programList = new Gson().fromJson(resp, new TypeToken<List<Program>>() {
			}.getType());

			m.addAttribute("programList", programList);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "test/exportTest";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/exportTest", method = { RequestMethod.GET, RequestMethod.POST })
	public String exportTest(@ModelAttribute Test test, Model m, @RequestParam(name = "programId") String programId,
			@RequestParam(name = "testId") String testId, @RequestParam(name = "abbr") String abbr,
			@RequestParam(name = "courseIds") String courseIds, @RequestParam(name = "username") String usernameSql,
			@RequestParam(name = "url") String url,

			@RequestParam(name = "dbName") String dbName, RedirectAttributes redirectAttributes, Principal principal) {

		MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
		DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection.createDefaultConnectionByDS(defaultUrl,
				defaultUsername, defaultPassword);

		List<String> courseIdList = Arrays.asList(courseIds.split(","));
		String username = principal.getName();

		test.setId(Long.valueOf(testId));
		List<Program> programList = programService.findAll();
		test = testService.findByID(test.getId());
		redirectAttributes.addAttribute("id", test.getId());
		List<TestQuestion> testQuestionListByTestId = testQuestionService.findByTestId(test.getId());

		List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(test.getId());

		try {

			if (programList.get(0).getAbbr().equals(abbr)) {

				for (int i = 0; i < courseIdList.size(); i++) {
					Course course = courseService.findByID(Long.valueOf(courseIdList.get(i)));

					List<UserCourse> userCourseListByCourseId = userCourseService
							.getStudentsByCourseId(String.valueOf(course.getId()));

					if (test.getCourseId().equals(Long.valueOf(courseIdList.get(i)))) {

					} else {

						Test newTest = test;
						newTest.setCourseId(course.getId());
						newTest.setLastModifiedBy(username);
						newTest.setAcadYear(Integer.parseInt(course.getAcadYear()));
						newTest.setAcadMonth(course.getAcadMonth());
						newTest.setFacultyId(username);
						testService.insertWithIdReturn(newTest);

						List<TestQuestion> newTestQuestions = testQuestionListByTestId;

						for (TestQuestion tq : newTestQuestions) {
							tq.setTestId(newTest.getId());
						}
						List<StudentTest> studentTestList = createStudentTestList(userCourseListByCourseId, newTest,
								username);

						studentTestList.add(createFacultyTestList(newTest, username));
						testQuestionService.insertBatch(newTestQuestions);

						if (testConfigList.size() > 0) {
							for (TestConfiguration tc : testConfigList) {

								tc.setTestId(newTest.getId());
							}

							testConfigurationService.insertBatch(testConfigList);
						}

						studentTestService.insertBatch(studentTestList);

						logger.info("file creation entered");
						String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
								newTest.getStartDate());
						String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
								newTest.getEndDate());
						String testDate = testSDate + "-" + testEDate;

						String rootFolder = testBaseDir + "/" + app;
						File folderR = new File(rootFolder);
						folderR.setExecutable(true, false);
						folderR.setWritable(true, false);
						folderR.setReadable(true, false);

						String folderPath = testBaseDir + "/" + app + "/" + "Tests";
						File folderP = new File(folderPath);
						if (!folderP.exists()) {
							folderP.mkdirs();
							logger.info("folder created");
						}
						folderP.setExecutable(true, false);
						folderP.setWritable(true, false);
						folderP.setReadable(true, false);

						String subFolderPath = folderPath + "/" + newTest.getId() + "-" + testDate;

						File subFolderP = new File(subFolderPath);
						if (!subFolderP.exists()) {
							subFolderP.mkdir();
							logger.info("subfolder created");
						}
						subFolderP.setExecutable(true, false);
						subFolderP.setWritable(true, false);
						subFolderP.setReadable(true, false);

						logger.info("subfolder created" + subFolderPath);

						studentTestService.allocateTestQuestionsForAllStudent(newTest.getId(), subFolderPath);

					}

				}
			} else {

				DriverManagerDataSource dataSourceLms = multipleDBConnection.createConnectionByDS(url, usernameSql,
						defaultPassword, dbName);

				for (int i = 0; i < courseIdList.size(); i++) {

					courseDAO.setDS(dataSourceLms);
					userCourseDAO.setDS(dataSourceLms);

					testDAO.setDS(dataSourceLms);
					testQuestionDAO.setDS(dataSourceLms);
					studentTestDAO.setDS(dataSourceLms);
					Course course = courseDAO.findOne(Long.valueOf(courseIdList.get(i)));

					List<UserCourse> userCourseListByCourseId = userCourseDAO
							.getStudentsByCourseId(String.valueOf(course.getId()));

					if (test.getCourseId().equals(Long.valueOf(courseIdList.get(i)))) {

					} else {

						Test newTest = test;
						newTest.setCourseId(course.getId());
						newTest.setLastModifiedBy(username);
						newTest.setAcadYear(Integer.parseInt(course.getAcadYear()));
						newTest.setAcadMonth(course.getAcadMonth());
						newTest.setFacultyId(username);

						testDAO.insertWithIdReturn(newTest);

						List<TestQuestion> newTestQuestions = testQuestionListByTestId;

						for (TestQuestion tq : newTestQuestions) {
							tq.setTestId(newTest.getId());
						}
						List<StudentTest> studentTestList = createStudentTestList(userCourseListByCourseId, newTest,
								username);

						studentTestList.add(createFacultyTestList(newTest, username));

						testQuestionDAO.insertBatch(newTestQuestions);

						if (testConfigList.size() > 0) {
							for (TestConfiguration tc : testConfigList) {

								tc.setTestId(newTest.getId());
							}

							testConfigurationService.insertBatch(testConfigList);
						}

						studentTestDAO.insertBatch(studentTestList);

						logger.info("file creation entered");
						String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
								newTest.getStartDate());
						String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
								newTest.getEndDate());
						String testDate = testSDate + "-" + testEDate;

						String rootFolder = testBaseDir + "/" + app;
						File folderR = new File(rootFolder);
						folderR.setExecutable(true, false);
						folderR.setWritable(true, false);
						folderR.setReadable(true, false);

						String folderPath = testBaseDir + "/" + app + "/" + "Tests";
						File folderP = new File(folderPath);
						if (!folderP.exists()) {
							folderP.mkdirs();
							logger.info("folder created");
						}
						folderP.setExecutable(true, false);
						folderP.setWritable(true, false);
						folderP.setReadable(true, false);

						String subFolderPath = folderPath + "/" + newTest.getId() + "-" + testDate;

						File subFolderP = new File(subFolderPath);
						if (!subFolderP.exists()) {
							subFolderP.mkdir();
							logger.info("subfolder created");
						}
						subFolderP.setExecutable(true, false);
						subFolderP.setWritable(true, false);
						subFolderP.setReadable(true, false);

						logger.info("subfolder created" + subFolderPath);

						studentTestService.allocateTestQuestionsForAllStudent(newTest.getId(), subFolderPath);

					}

				}

			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttributes, "Error in Exporting Test");
			return "redirect:/exportTestForm";

		}

		courseDAO.setDS(dataSourceDefaultLms);
		userCourseDAO.setDS(dataSourceDefaultLms);

		testDAO.setDS(dataSourceDefaultLms);
		testQuestionDAO.setDS(dataSourceDefaultLms);
		studentTestDAO.setDS(dataSourceDefaultLms);

		setSuccess(redirectAttributes, "Test Exported Successfully");
		return "redirect:/exportTestForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTestQuestionPoolByTestPoolId", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTestQuestionPoolByTestPoolId(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String testPoolId, RedirectAttributes redirectAttrs) throws URIException {

		List<TestQuestionPools> testQuestionsByTestPoolId = testQuestionPoolsService
				.findAllTestQuestionsByTestPoolId(testPoolId);

		if (testQuestionsByTestPoolId.size() == 0) {

			setNote(redirectAttrs, "No Questions in this pool.");
			return "redirect:/viewTestPools";
		}
		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("description", "marks", "testType",
				"questionType", "type", "answerRangeFrom", "answerRangeTo", "Options Shuffle Required", "option1",
				"option2", "option3", "option4", "option5", "option6", "option7", "option8", "correctOption"));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfTestQuestion = new ArrayList<>();
			fileName = testPoolService.findByID(Long.valueOf(testPoolId)).getTestPoolName() + ".xlsx";
			fileName = fileName.replace("/", "_");

			filePath = downloadAllFolder + File.separator + fileName;

			for (TestQuestionPools tq : testQuestionsByTestPoolId) {
				Map<String, Object> mapOfTestQuestion = new HashMap<>();

				if (!tq.getDescription().contains("<img")) {
					mapOfTestQuestion.put("testType", tq.getTestType());
					mapOfTestQuestion.put("questionType", tq.getQuestionType());
					mapOfTestQuestion.put("description", tq.getDescription());
					mapOfTestQuestion.put("marks", tq.getMarks());
					mapOfTestQuestion.put("type", tq.getType());
					mapOfTestQuestion.put("option1", tq.getOption1());
					mapOfTestQuestion.put("option2", tq.getOption2());
					mapOfTestQuestion.put("option3", tq.getOption3());
					mapOfTestQuestion.put("option4", tq.getOption4());
					mapOfTestQuestion.put("option5", tq.getOption5());
					mapOfTestQuestion.put("option6", tq.getOption6());
					mapOfTestQuestion.put("option7", tq.getOption7());
					mapOfTestQuestion.put("option8", tq.getOption8());
					mapOfTestQuestion.put("correctOption", tq.getCorrectOption());
					mapOfTestQuestion.put("answerRangeFrom", tq.getAnswerRangeFrom());
					mapOfTestQuestion.put("answerRangeTo", tq.getAnswerRangeTo());
					mapOfTestQuestion.put("Options Shuffle Required", tq.getOptionShuffle());

					listOfMapOfTestQuestion.add(mapOfTestQuestion);
				}
			}
			excelCreater.CreateExcelFile(listOfMapOfTestQuestion, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// copy it to response's OutputStream
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
			}

		}
		return null;
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/deleteTestQuestionPool", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deleteTestQuestionPool(HttpServletRequest request,
			@RequestParam(required = false) String testQuestionPoolId,
			@RequestParam(required = false) String testPoolId, @RequestParam(required = false) String mapping, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {

		redirectAttrs.addAttribute("testPoolId", testPoolId);
		if (testQuestionPoolId != null) {
			testQuestionPoolsService.deleteSoftById(testQuestionPoolId);
			setSuccess(redirectAttrs, "test-question deleted from pool");
			return new ModelAndView("redirect:/" + mapping);
		} else {
			setError(redirectAttrs, "Test Pool Not Found");
			return new ModelAndView("redirect:/" + mapping);
		}

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/exportTestPoolForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String exportTestPoolForm(@ModelAttribute TestPool testPool, Model m,
			@RequestParam(required = false) Long campusId, Principal p) {
		m.addAttribute("webPage", new WebPage("content", "Export Test", true, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		testPool = testPoolService.findByID(testPool.getId());
		Course c = courseService.findByID(Long.valueOf(testPool.getCourseId()));

		testPool.setCourseName(c.getCourseName());
		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("testPool", testPool);

		try {
			WebTarget webTarget = client.target(
					URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getAllProgramDetailsByUsername?username=" + username));
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			List<Program> programList = new Gson().fromJson(resp, new TypeToken<List<Program>>() {
			}.getType());

			m.addAttribute("programList", programList);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "test/exportTestPool";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/exportTestPool", method = { RequestMethod.GET, RequestMethod.POST })
	public String exportTestPool(@ModelAttribute TestPool testPool, Model m,
			@RequestParam(name = "programId") String programId, @RequestParam(name = "testPoolId") String testPoolId,
			@RequestParam(name = "abbr") String abbr, @RequestParam(name = "courseIds") String courseIds,
			@RequestParam(name = "username") String usernameSql, @RequestParam(name = "url") String url,

			@RequestParam(name = "dbName") String dbName, RedirectAttributes redirectAttributes, Principal principal) {

		MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
		DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection.createDefaultConnectionByDS(defaultUrl,
				defaultUsername, defaultPassword);

		List<String> courseIdList = Arrays.asList(courseIds.split(","));
		String username = principal.getName();

		testPool.setId(Long.valueOf(testPoolId));
		List<Program> programList = programService.findAll();
		testPool = testPoolService.findByID(testPool.getId());
		redirectAttributes.addAttribute("id", testPool.getId());
		List<TestQuestionPools> testQuestionPoolListByTestPoolId = testQuestionPoolsService
				.findAllTestQuestionsByTestPoolId(testPoolId);

		try {

			if (programList.get(0).getAbbr().equals(abbr)) {

				for (int i = 0; i < courseIdList.size(); i++) {
					Course course = courseService.findByID(Long.valueOf(courseIdList.get(i)));

					List<UserCourse> userCourseListByCourseId = userCourseService
							.getStudentsByCourseId(String.valueOf(course.getId()));

					if (testPool.getCourseId().equals(Long.valueOf(courseIdList.get(i)))) {

					} else {

						TestPool newTestPool = testPool;
						newTestPool.setCourseId(String.valueOf(course.getId()));
						newTestPool.setLastModifiedBy(username);

						testPoolService.insertWithIdReturn(newTestPool);

						List<TestQuestionPools> newTestQuestionPools = testQuestionPoolListByTestPoolId;

						for (TestQuestionPools tq : newTestQuestionPools) {
							tq.setTestPoolId(newTestPool.getId());
						}

						testQuestionPoolsService.insertBatch(newTestQuestionPools);

					}

				}
			} else {

				DriverManagerDataSource dataSourceLms = multipleDBConnection.createConnectionByDS(url, usernameSql,
						defaultPassword, dbName);

				for (int i = 0; i < courseIdList.size(); i++) {

					courseDAO.setDS(dataSourceLms);
					userCourseDAO.setDS(dataSourceLms);

					testPoolDAO.setDS(dataSourceLms);
					testQuestionPoolsDAO.setDS(dataSourceLms);

					Course course = courseDAO.findOne(Long.valueOf(courseIdList.get(i)));

					if (testPool.getCourseId().equals(Long.valueOf(courseIdList.get(i)))) {

					} else {

						TestPool newTestPool = testPool;
						newTestPool.setCourseId(String.valueOf(course.getId()));
						newTestPool.setLastModifiedBy(username);

						testPoolDAO.insertWithIdReturn(newTestPool);

						List<TestQuestionPools> newTestQuestionPoolss = testQuestionPoolListByTestPoolId;

						for (TestQuestionPools tq : testQuestionPoolListByTestPoolId) {
							tq.setTestPoolId(newTestPool.getId());
						}

						testQuestionPoolsDAO.insertBatch(testQuestionPoolListByTestPoolId);

					}

				}

			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttributes, "Error in Exporting Test");
			return "redirect:/exportTestForm";

		}

		courseDAO.setDS(dataSourceDefaultLms);
		userCourseDAO.setDS(dataSourceDefaultLms);

		testDAO.setDS(dataSourceDefaultLms);
		testQuestionDAO.setDS(dataSourceDefaultLms);
		studentTestDAO.setDS(dataSourceDefaultLms);

		setSuccess(redirectAttributes, "Test Pool Exported Successfully");
		return "redirect:/exportTestPoolForm";
	}

	// Code For Offline Test Creation & Uploading Marks In Excel

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addOfflineTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addOfflineTestForm(@ModelAttribute OfflineTest offlineTest, Model m, Principal principal,
			@RequestParam(required = false) String courseId, RedirectAttributes redirectAttrs) {

		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

			String acadSession = u.getAcadSession();
			logger.info("Program NAme--------------------> " + ProgramName);
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			if (courseId != null) {
				offlineTest.setCourseId(courseId);
			}
			logger.info("Add New Test Page");
			logger.info("courseId------------" + courseId);
			m.addAttribute("webPage", new WebPage("test", "Create Offline Test", false, false));

			if (offlineTest.getId() != null) {
				OfflineTest offlineTestDB = offlineTestService.findByID(offlineTest.getId());
				if (offlineTestDB == null) {
					setError(m, "TEST " + offlineTest.getTestName() + " does not exist");
					offlineTest.setId(null);
				} else {
					LMSHelper.copyNonNullFields(offlineTest, offlineTestDB);
					m.addAttribute("edit", "true");
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttrs.addFlashAttribute("offlineTest", offlineTest);
			setError(redirectAttrs, "Error in adding Test");
			logger.info("test exception----" + e.getMessage());
			return "test/addOfflineTest";
		}
		redirectAttrs.addFlashAttribute("offlineTest", offlineTest);

		return "test/addOfflineTest";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/uploadStudentOfflineTestScoreForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadStudentOfflineTestScoreForm(Model m, Principal principal,
			@ModelAttribute OfflineTest offlineTest, RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage",
				new WebPage("uploadStudentOfflineTestScoreForm", "Upload Student Offline Test Score", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		logger.info("feedback id --" + offlineTest.getId());
		m.addAttribute("offlineTest", offlineTest);
		return "test/uploadStudentOfflineTest";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/deleteOfflineTest", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deleteOfflineTest(HttpServletRequest request, @ModelAttribute OfflineTest offlineTest, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {

		int id = Integer.parseInt(request.getParameter("id"));
		String username = principal.getName();

		OfflineTest offlineTestFromDB = offlineTestService.findByID(Long.valueOf(id));
		if (offlineTestFromDB != null) {
			int testId = Integer.parseInt(request.getParameter("id"));
			logger.info("id===>" + testId);
			offlineTestService.deleteSoftById(String.valueOf(testId));
			setSuccess(redirectAttrs, " offline test deleted successfully");
			return new ModelAndView("redirect:/viewOfflineTests");
		} else {
			setError(redirectAttrs, "Test Pool Not Found");
			return new ModelAndView("redirect:/viewOfflineTests");
		}

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/updateOfflineTest", method = RequestMethod.POST)
	public String updateOfflineTest(@ModelAttribute OfflineTest offlineTest, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttrs.addAttribute("id", offlineTest.getId());
		logger.info("updated test called");

		try {

			OfflineTest offlineTestFromDB = offlineTestService.findByID(offlineTest.getId());
			offlineTestFromDB = LMSHelper.copyNonNullFields(offlineTestFromDB, offlineTest);
			offlineTestFromDB.setLastModifiedBy(username);

			if (offlineTestService.update(offlineTestFromDB) > 0)
				setSuccess(redirectAttrs, "Offline-Test updated successfully");
			else
				setError(redirectAttrs, "Offline-Test cannot be updated");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test Pool");
			redirectAttrs.addFlashAttribute("testPool", offlineTest);
			return "redirect:/addOfflineTestForm";
		}
		return "redirect:/addOfflineTestForm";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/uploadStudentOfflineTest", method = { RequestMethod.POST })
	public String uploadStudentOfflineTest(@ModelAttribute OfflineTest offlineTest,
			@RequestParam("file") MultipartFile file, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {
		m.addAttribute("webPage",
				new WebPage("uploadStudentOfflineTest", "Upload Student Offline Test Score ", false, false));
		List<String> validateHeaders = null;
		logger.info(" id----" + offlineTest.getId());
		OfflineTest offlineTestDb = offlineTestService.findByID(offlineTest.getId());
		validateHeaders = new ArrayList<String>(Arrays.asList("username", "score"));

		logger.info("validateHeaders" + validateHeaders);
		logger.info("validate header size" + validateHeaders.size());

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		ExcelReader excelReader = new ExcelReader();

		try {
			String correctOpt = "";
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);
			logger.info("map size---->" + maps.size());

			List<StudentOfflineTest> studentOfflineTestById = studentOfflineTestService
					.findByOfflineTestId(String.valueOf(offlineTest.getId()));

			if (studentOfflineTestById.size() > 0) {
				studentOfflineTestService.deleteBatch(studentOfflineTestById);

			}

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
						logger.info("Error exist");
						setNote(m, "Error--->" + mapper.get("Error"));
					} else {
						StudentOfflineTest student = new StudentOfflineTest();

						student.setOfflineTestId(String.valueOf(offlineTest.getId()));

						student.setCreatedBy(username);
						student.setLastModifiedBy(username);

						mapper.put("offlineTestId", offlineTest.getId());
						mapper.put("createdBy", student.getCreatedBy());
						mapper.put("lastModifiedBy", student.getLastModifiedBy());

						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());

						studentOfflineTestService.insertUsingMap(mapper);
						setSuccess(m, "student offline-test score file uploaded successfully");
						m.addAttribute("showProceed", true);
					}
				}
			}

			m.addAttribute("offlineTest", offlineTest);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "test/uploadStudentOfflineTest";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addOfflineTest", method = RequestMethod.POST)
	public String addOfflineTest(@ModelAttribute OfflineTest offlineTest, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		try {
			String username = principal.getName();
			logger.info("offline test--->" + offlineTest);
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

			String acadSession = u.getAcadSession();
			logger.info("Program NAme--------------------> " + ProgramName);
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			offlineTest.setCreatedBy(username);
			offlineTest.setLastModifiedBy(username);

			offlineTestService.insertWithIdReturn(offlineTest);

			redirectAttrs.addAttribute("id", offlineTest.getId());
			setSuccess(redirectAttrs, "Offline Test added successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Feedback");
			return "redirect:/uploadStudentOfflineTestScoreForm";
		}
		return "redirect:/uploadStudentOfflineTestScoreForm";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/viewOfflineTests", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewOfflineTests(

			Model m, @ModelAttribute OfflineTest offlineTest, @RequestParam(required = false) String courseId,
			@RequestParam(required = false) Long offlineTestId, Principal principal, HttpServletRequest request) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("viewTestPoolList", "View Test Pools", true, false));

		try {
			List<OfflineTest> offlineTestList = new ArrayList<>();

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {

				if (courseId != null) {
					offlineTestList = offlineTestService.findAllOfflineTestsByUsernameAndCourseId(username, courseId);
				} else {
					offlineTestList = offlineTestService.findAllOfflineTestsByUsername(username);
				}

				if (offlineTestId != null) {
					m.addAttribute("isTestIdPresent", true);
					m.addAttribute("testId", offlineTestId);
				}
				m.addAttribute("offlineTestList", offlineTestList);

			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "test/offlineTestList";
	}

	/* Added on 23rd May */

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/createTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String createTestForm(@RequestParam(required = false) String courseId,
			@RequestParam(required = false) String acadSessionParam, @RequestParam(required = false) String acadYear,
			Model m, Principal principal, @ModelAttribute Test test, RedirectAttributes redirectAttrs,
			HttpServletRequest request) {

		logger.info("CourseId------>" + courseId);
		try {
			String username = principal.getName();
			Token userDetails = (Token) principal;

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			m.addAttribute("username", username);

			HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();

			for (Course c : userDetails.getCourseList()) {

				if (!sessionWiseCourseList.containsKey(c.getAcadSession() + c.getAcadYear())) {
					List<Course> courseLst = new ArrayList();
					courseLst.add(c);
					sessionWiseCourseList.put(c.getAcadSession() + c.getAcadYear(), courseLst);
				} else {
					List<Course> courseLst = sessionWiseCourseList.get(c.getAcadSession() + c.getAcadYear());
					courseLst.add(c);
					sessionWiseCourseList.replace(c.getAcadSession() + c.getAcadYear(), courseLst);
				}

			}

			m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
			logger.info("sessionWiseCourseListTest--------->" + sessionWiseCourseList);

			m.addAttribute("webPage", new WebPage("test", "Create Test", false, false));

			if (StringUtils.isEmpty(courseId) && test.getId() == null) {
				return "redirect:/addTestFromMenu";
			}
			if (!StringUtils.isEmpty(courseId) && test.getId() == null) {
				m.addAttribute("courseId", courseId);
				Course course = courseService.findByID(Long.valueOf(courseId));
				course.setAcadSession(course.getAcadSession() + course.getAcadYear());
				m.addAttribute("courseIdVal", courseId);
				m.addAttribute("course", course);

			}

			if (test.getId() != null) {

				Test testFromDb = testService.findByID(test.getId());
				int chkStartDateForUpdate = testService.chkStartDateForUpdate(test.getId());

				String role = "";

				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					role = "ROLE_ADMIN";
				} else {
					role = "ROLE_FACULTY";
				}

				Test testDB = testService.findByIDAndFaculty(test.getId(), username, role);
				if (testDB == null) {
					setError(redirectAttrs, "Test " + testFromDb.getTestName() + " Can't be updated by faculty "
							+ username + "(Access Denied)");
					test.setId(null);
					return "redirect:/testList";
				} else if (chkStartDateForUpdate == 0) {
					List<TestQuestion> qList = testQuestionService.findByTestId(test.getId());
					if (qList.isEmpty() || qList.size() == 0) {
						LMSHelper.copyNonNullFields(test, testDB);
						m.addAttribute("edit", "true");
					} else {
						setError(redirectAttrs, "cannot update test: " + testFromDb.getTestName() + " already started");
						test.setId(null);
						return "redirect:/testList";
					}
				} else {
					LMSHelper.copyNonNullFields(test, testDB);
					m.addAttribute("edit", "true");
				}
				List<UserCourse> faculty = userCourseService.findAllFacultyWithCourseId(testFromDb.getCourseId(),
						testFromDb.getAcadMonth(), testFromDb.getAcadYear());
				m.addAttribute("faculties", faculty);
				Course course = courseService.findByID(testFromDb.getCourseId());
				course.setAcadSession(course.getAcadSession() + course.getAcadYear());
				m.addAttribute("course", course);
				// logger.info("course name-------------"+);
				m.addAttribute("testFromDb", testFromDb);

			}

			m.addAttribute("test", test);

			test.setIdOfCourse(String.valueOf(test.getCourseId()));
			m.addAttribute("idOfCourse", test.getIdOfCourse());

			m.addAttribute("allCourses", courseService.findByUserActive(username, userDetails.getProgramName()));

			LmsVariables takeDemoTest = lmsVariablesService.getLmsVariableBykeyword("peerFaculty_takeDemoTest");
			String allocateFaculty = takeDemoTest.getValue();

			if (allocateFaculty.equals("Yes")) {
				m.addAttribute("allocateFaculty", true);
			} else {
				m.addAttribute("allocateFaculty", false);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttrs.addFlashAttribute("test", test);
			setError(redirectAttrs, "Error in adding Test");

			return "test/addTest";
		}
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		return "test/createTestForm";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_STUDENT"})
	@RequestMapping(value = "/viewTestFinal", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewTestFinal(@RequestParam(required = false) Long courseId,
			@RequestParam(required = false, defaultValue = "1") int pageNo, Model m, Principal principal) {
		Token userDetails = (Token) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "redirect:/testList";
		}

		logger.info("courseId------>" + courseId);
		Course cr = courseService.findByID(courseId);

		String username = principal.getName();

		User u = userService.findByUserName(username);

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
		m.addAttribute("course", cr);

		Page<Test> page;
		Page<Test> pageAdmin;

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			if (courseId != null) {
				Course courseDB = courseService.findByID(courseId);
				page = testService.findByFacultyAndCourse(username, courseId, pageNo, pageSize);
				pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
						Long.parseLong(userDetails.getProgramId()), courseDB.getModuleId(), courseDB.getAcadYear(),
						pageNo, pageSize);
			} else {
				page = testService.searchActiveByExactMatchReplacement(username,
						Long.parseLong(userDetails.getProgramId()), pageNo, pageSize);
				pageAdmin = page = testService.searchTestCreatedByAdminForFaculty(username,
						Long.parseLong(userDetails.getProgramId()), null, null, pageNo, pageSize);
			}
			List<Test> programList = page.getPageItems();

			if (pageAdmin.getPageItems().size() > 0) {
				logger.info(pageAdmin + " found");
				programList.addAll(pageAdmin.getPageItems());
				page.setPageItems(pageAdmin.getPageItems());
				page.setRowCount(page.getPageItems().size());
			}
			for (Test t : programList) {
				Course c = courseService.findByID(t.getCourseId());
				t.setCourseName(c.getCourseName());
				programList.set(programList.indexOf(t), t);

			}
			m.addAttribute("page", page);

			if (programList == null || programList.size() == 0) {
				setNote(m, "No Tests found");
			}
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

			if ("ASMSOC".equals(app)) {

				User user = userService.findByUserName(username);

				String outTimeText = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());

				List<StudentTest> studentTestOfCurrentDate = studentTestService
						.findAllTestOfCurrentDateByStudent(username, outTimeText);

				for (StudentTest st : studentTestOfCurrentDate) {

					setNote(m, user.getFirstname() + " " + user.getLastname() + ": Test With Name " + st.getTestName()
							+ " For Subject - " + st.getCourseName() + " is Assigned To You For Today");
				}

			}
			if (courseId != null) {
				page = testService.findTestAllocatedbyCourseId(username, courseId, pageNo, pageSize);

			} else {

				page = testService.findTestAllocated(username, pageNo, pageSize);
			}

			List<Test> programList = page.getPageItems();
			for (Test t : programList) {
				Course c = courseService.findByID(t.getCourseId());
				StudentTest st = studentTestService.findBytestIDAndUsername(t.getId(), username);
				t.setStudentTest(st);
				t.setCourseName(c.getCourseName());
				programList.set(programList.indexOf(t), t);

			}
			m.addAttribute("page", page);

			if (programList == null || programList.size() == 0) {
				setNote(m, "No Tests found");
			}

		}

		m.addAttribute("courseId", courseId);

		return "test/viewTestFinal";
	}
	
	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_STUDENT"})
	@RequestMapping(value = "/viewTestFinalAjax", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String viewTestFinalAjax(@RequestParam Long courseId,
			@RequestParam(required = false, defaultValue = "1") int pageNo, Model m, Principal principal) {

		logger.info("courseId------>" + courseId);
		Course cr = courseService.findByID(courseId);

		String username = principal.getName();

		User u = userService.findByUserName(username);
		Token userDetails = (Token) principal;

		Page<Test> page = null;

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			if (courseId != null) {
				page = testService.findByFacultyAndCourse(username, courseId, pageNo, pageSize);
			} else {
				page = testService.searchActiveByExactMatchReplacement(username,
						Long.parseLong(userDetails.getProgramId()), pageNo, pageSize);
			}
			List<Test> programList = page.getPageItems();
			for (Test t : programList) {
				Course c = courseService.findByID(t.getCourseId());
				t.setCourseName(c.getCourseName());
				programList.set(programList.indexOf(t), t);

			}

			if (programList == null || programList.size() == 0) {
				/* setNote(m, "No Tests found"); */
			}
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

			if ("ASMSOC".equals(app)) {

				User user = userService.findByUserName(username);

				String outTimeText = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());

				List<StudentTest> studentTestOfCurrentDate = studentTestService
						.findAllTestOfCurrentDateByStudent(username, outTimeText);

				for (StudentTest st : studentTestOfCurrentDate) {

					setNote(m, user.getFirstname() + " " + user.getLastname() + ": Test With Name " + st.getTestName()
							+ " For Subject - " + st.getCourseName() + " is Assigned To You For Today");
				}

			}
			if (courseId != null) {
				page = testService.findTestAllocatedbyCourseId(username, courseId, pageNo, pageSize);

			} else {

				page = testService.findTestAllocated(username, pageNo, pageSize);
			}

			List<Test> programList = page.getPageItems();
			for (Test t : programList) {
				Course c = courseService.findByID(t.getCourseId());
				t.setCourseName(c.getCourseName());
				programList.set(programList.indexOf(t), t);

			}

			if (programList == null || programList.size() == 0) {

			}
		}

		List<Test> programList = page.getPageItems();

		if (programList == null || programList.size() == 0) {
			logger.info("-----empty-----");
			return "[]";
		} else {
			String json = new Gson().toJson(page.getPageItems());
			logger.info("json----->" + json);
			return json;
		}
	}

	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/searchTestListBySupportAdminForm", method = RequestMethod.GET)
	public String searchTestListBySupportAdmin(@ModelAttribute("test") Test test, Model m) {
		m.addAttribute("webPage", new WebPage("test", "Search Test", true, false));
		List<Test> testList = new ArrayList<Test>();
		testList = testDAO.findTestDetails("", "");
		for (Test t : testList) {
			List<TestQuestion> tqlist = testQuestionDAO.getQuestionCount(t.getId());
			t.setNoOfQuestion(String.valueOf(tqlist.size()));
			t.setSchoolName(app);
		}
		m.addAttribute("finalTestList", testList);
		m.addAttribute("test", new Test());
		return "test/searchTestListBySupportAdminForm";
	}

	// new
	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/searchTestListBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchTestListBySupportAdmin(Model m, Principal principal, RedirectAttributes redirectAttributes,
			@ModelAttribute("test") Test test, HttpServletRequest request) {
		List<Test> testList = new ArrayList<Test>();
		try {
			String submit = request.getParameter("submit");
			if ((!test.getStartDate().isEmpty() || !test.getEndDate().isEmpty())) {
				if (null != test.getSchoolName() && null != test.getId()) {
					logger.info("------------->" + test.getId());

					m.addAttribute("finalTestList", test);
					return "test/testDetailsBySupportAdmin";
				}
				if ("SearchOne".equals(submit)) {
					testList = testDAO.findTestDetails(test.getStartDate().replace("T", " "),
							test.getEndDate().replace("T", " "));
					for (Test t : testList) {
						List<TestQuestion> tqlist = testQuestionDAO.getQuestionCount(t.getId());
						t.setNoOfQuestion(String.valueOf(tqlist.size()));
						t.setSchoolName(app);
					}
				} else {
					logger.info("url---->" + userRoleMgmtCrudUrl + "/getSchoolListForTest");
					WebTarget webTarget = client
							.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/getSchoolListForTest"));
					Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
					String resp = invocationBuilder.get(String.class);
					ObjectMapper objMapper = new ObjectMapper();
					List<MultipleDBReference> schoolList = objMapper.readValue(resp,
							new TypeReference<List<MultipleDBReference>>() {
							});
					logger.info("schoolList---->" + new Gson().toJson(schoolList));

					List<Test> testListTemp = new ArrayList<>();

					MultipleDBConnection multipleDBConnection = new MultipleDBConnection();

					DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection

							.createDefaultConnectionByDS(defaultUrl, defaultUsername, defaultPassword);

					logger.info("schoolList_size----" + schoolList.size());

					for (MultipleDBReference s : schoolList) {
						String dbPort = s.getDbPort();
						String dbName = s.getDbName();
						String schoolName = s.getSchoolName();
						logger.info("dbPort----" + dbPort);
						logger.info("dbName----" + dbName);
						logger.info("schoolName--->" + schoolName);
						if (null != dbPort) {
							DriverManagerDataSource dataSourceTest = multipleDBConnection.createConnectionByDS(
									"jdbc:mysql://10.25.10.50:" + dbPort + "/", defaultUsername, defaultPassword,
									dbName);

							logger.info("dataSourceTest---->" + dataSourceTest);

							testDAO.setDS(dataSourceTest);
							testQuestionDAO.setDS(dataSourceTest);
							testListTemp = testDAO.findTestDetails(test.getStartDate().replace("T", " "),
									test.getEndDate().replace("T", " "));

							for (Test t : testListTemp) {
								List<TestQuestion> tqlist = testQuestionDAO.getQuestionCount(t.getId());
								t.setNoOfQuestion(String.valueOf(tqlist.size()));
								t.setSchoolName(schoolName);
								t.setDbName(dbName);
								t.setDbPort(dbPort);
							}
							testList.addAll(testListTemp);
						}
					}
					testQuestionDAO.setDS(dataSourceDefaultLms);
					testDAO.setDS(dataSourceDefaultLms);
				}

			} else {
				testList = testDAO.findTestDetails("", "");
				for (Test t : testList) {
					List<TestQuestion> tqlist = testQuestionDAO.getQuestionCount(t.getId());
					t.setNoOfQuestion(String.valueOf(tqlist.size()));
					t.setSchoolName(app);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting Test List");
		}
		m.addAttribute("finalTestList", testList);
		return "test/searchTestListBySupportAdminForm";
	}

	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/studetTestListBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String studetTestListBySupportAdmin(Model m, Principal principal, RedirectAttributes redirectAttributes,
			@RequestParam(name = "id") String id, @RequestParam(name = "dbName") String dbName,
			@RequestParam(name = "dbPort") String dbPort, StudentTest test) {
		try {

			logger.info("dbPort------->" + dbPort);
			logger.info("dbName---->" + dbName);
			if (!dbPort.equals("") && !dbName.equals("")) {
				MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
				DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection
						.createDefaultConnectionByDS(defaultUrl, defaultUsername, defaultPassword);

				DriverManagerDataSource dataSourceTest = multipleDBConnection

						.createConnectionByDS("jdbc:mysql://10.25.10.50:" + dbPort + "/", defaultUsername,
								defaultPassword, dbName);

				studentTestDAO.setDS(dataSourceTest);

				List<StudentTest> studentTList = studentTestDAO.studentTestDetails(Long.valueOf(id));

				studentTestDAO.setDS(dataSourceDefaultLms);
				m.addAttribute("studentTList", studentTList);
				logger.info("studentTList--------->" + studentTList);
				logger.info("studentTListJSonnn--------->" + new Gson().toJson(studentTList));
			} else {
				List<StudentTest> studentTList = studentTestDAO.studentTestDetails(Long.valueOf(id));
				m.addAttribute("studentTList", studentTList);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			setError(redirectAttributes, "No Data ");
		}
		return "test/studentTestDetailsBySupportAdmin";
	}

	// 27-07-2020
	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/showReportsToStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showReportsToStudents(@RequestParam("id") Long id, Model m, Principal principal) {

		String username = principal.getName();
		try {
			Test test = testService.findByID(id);
			testService.updateHideOrShowReports(test.getId(), "Y");
			setSuccess(m, "Reports will be shown to students!");
			return "Success";
		} catch (Exception e) {
			logger.error("Error, " + e);
			return "Error";
		}
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/hideReportsToStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String hideReportsToStudents(@RequestParam("id") Long id, Model m, Principal principal) {

		String username = principal.getName();
		try {
			Test test = testService.findByID(id);
			testService.updateHideOrShowReports(test.getId(), "N");
			setSuccess(m, "Reports will be hidden from students!");
			return "Success";
		} catch (Exception e) {
			logger.error("Error, " + e);
			return "Error";
		}

	}

	// Added Admin Test New Mapping on 29-07-2020

	// Test Creation Admin End On 18-07-2020
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTestFormByAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTestFormByAdmin(@ModelAttribute Test test, Model m, Principal principal,

			RedirectAttributes redirectAttrs) {

		try {
			String username = principal.getName();
			Token userDetails = (Token) principal;

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			m.addAttribute("username", username);

			m.addAttribute("webPage", new WebPage("test", "Create Test", false, false));

			if (test.getId() != null) {

				Test testFromDb = testService.findByID(test.getId());
				int chkStartDateForUpdate = testService.chkStartDateForUpdate(test.getId());

				if (chkStartDateForUpdate == 0) {
					List<TestQuestion> qList = testQuestionService.findByTestId(test.getId());
					if (qList.isEmpty() || qList.size() == 0) {
						LMSHelper.copyNonNullFields(test, testFromDb);
						m.addAttribute("edit", "true");
					} else {
						setError(redirectAttrs, "cannot update test: " + testFromDb.getTestName() + " already started");
						test.setId(null);
						return "redirect:/testList";
					}
				} else {
					LMSHelper.copyNonNullFields(test, testFromDb);
					m.addAttribute("edit", "true");
				}

				Course course = courseService.findByID(testFromDb.getCourseId());
				String campusId = "";
				if (testFromDb.getCampusId() != null) {
					ProgramCampus pc = programCampusService
							.getCampusNameByCampusId(String.valueOf(testFromDb.getCampusId()));
					m.addAttribute("pcBean", pc);
					campusId = String.valueOf(testFromDb.getCampusId());
				} else {
					campusId = "null";
				}
				List<UserCourse> faculty = userCourseService.findAllFacultyWithModuleIdTest(testFromDb.getModuleId(),
						String.valueOf(testFromDb.getAcadYear()), campusId, userdetails1.getProgramId());
				m.addAttribute("facultyList", faculty);
				m.addAttribute("course", course);
				m.addAttribute("testFromDb", testFromDb);

			}

			m.addAttribute("test", test);
			//acadYear change by Hiren 02-02-2021
			List<String> acadYearCodeList = courseService.getAllAcadYear();

			m.addAttribute("acadYearCodeList", acadYearCodeList);

			test.setIdOfCourse(String.valueOf(test.getCourseId()));
			m.addAttribute("idOfCourse", test.getIdOfCourse());

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttrs.addFlashAttribute("test", test);
			setError(redirectAttrs, "Error in adding Test");

			return "test/createTestFormForAdmin";
		}
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		return "test/createTestFormForAdmin";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getModuleByParamForTest", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getModuleByParamForTest(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId", required = false) String campusId,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		Token userdetails = (Token) principal;
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		if (campusId == null) {
			campusId = "null";
		}
		List<Course> moduleComponentListByYearAndCampus = courseService.moduleListByAcadYearAndCampusForTest(acadYear,
				campusId, userdetails.getProgramId());

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getModuleId(), module.getModuleName() + "(" + module.getModuleAbbr() + ")");
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTestByAdmin", method = RequestMethod.POST)
	public String addTestByAdmin(@ModelAttribute Test test, RedirectAttributes redirectAttrs, Principal principal,
			@RequestParam(required = false) Long courseId, Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("username", username);
		try {
			/* New Audit changes start */
			HtmlValidation.validateHtml(test, Arrays.asList("testDescription"));
			BusinessBypassRule.validateAlphaNumeric(test.getTestName());
			Course course = courseService.findByID(test.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(test.getFacultyId(), String.valueOf(test.getCourseId()));
			if(null == userccourse) {
				throw new ValidationException("Invalid faculty selected.");
			}
			Utils.validateStartAndEndDates(test.getStartDate(), test.getEndDate());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxScore());
			BusinessBypassRule.validateNumericNotAZero(test.getDuration());
			BusinessBypassRule.validateNumericNotAZero(test.getPassScore());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxAttempt());
			BusinessBypassRule.validateYesOrNo(test.getRandomQuestion());
			BusinessBypassRule.validateYesOrNo(test.getSameMarksQue());
			if ("Y".equals(test.getRandomQuestion())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMaxQuestnToShow());
			}
			if ("Y".equals(test.getSameMarksQue())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMarksPerQue());
			}

			if("Mix".equals(test.getTestType())) {
				BusinessBypassRule.validateNumeric(test.getMaxQuestnToShow());
				BusinessBypassRule.validateNumeric(test.getMaxDesQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxImgQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxMcqQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxRngQueToShow());
			}
			/* New Audit changes end */
			if ("Y".equals(test.getRandomQuestion()) && "Y".equals(test.getSameMarksQue())) {
				double total = test.getMarksPerQue() * Double.valueOf(test.getMaxQuestnToShow());
				if (total != test.getMaxScore()) {
					redirectAttrs.addAttribute("testId", test.getId());

					setError(redirectAttrs, "Error in adding Test (Misconfigured values).");
					return "redirect:/addTestFormByAdmin";
				}
			}
			test.setCreatedBy(username);
			test.setLastModifiedBy(username);

			if ("N".equals(test.getRandomQuestion())) {
				test.setMaxQuestnToShow("1");
				test.setMaxDesQueToShow("1");
				test.setMaxImgQueToShow("1");
				test.setMaxMcqQueToShow("1");
				test.setMaxRngQueToShow("1");
			} else if ("Y".equals(test.getRandomQuestion()) && !("Mix".equals(test.getTestType()))) {
				test.setMaxDesQueToShow("1");
				test.setMaxImgQueToShow("1");
				test.setMaxMcqQueToShow("1");
				test.setMaxRngQueToShow("1");
			}

			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				test.setSendEmailAlertToParents("Y");
				test.setSendSmsAlertToParents("Y");
			}
			List<Course> courseListByModuleId = courseService.findCoursesByModuleIdAcadYearAndProgram(
					Long.valueOf(test.getModuleId()), String.valueOf(test.getAcadYear()), userdetails1.getProgramId());
			String cMonth = courseService.getAcadMonthByModuleIdAndAcadYearAndProgram(test.getModuleId(),
					String.valueOf(test.getAcadYear()), userdetails1.getProgramId());

			test.setCourseId(courseListByModuleId.get(0).getId());
			test.setAcadMonth(cMonth);
			test.setAcadYear(Integer.valueOf(test.getAcadYear()));
			//test.setAutoAllocateToStudents("Y");
			test.setIsCreatedByAdmin("Y");
			test.setFacultyId(username);
			testService.insertWithIdReturn(test);
			redirectAttrs.addAttribute("testId", test.getId());

			// Create a folder with testId for storing student-test response in
			// file (changes on 11-10-2019) by akshay

			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getEndDate());

			String testDate = testSDate + "-" + testEDate;
			logger.info("testDate--->" + testDate);

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + test.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (!subFolderP.exists()) {
				subFolderP.mkdir();
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			setSuccess(redirectAttrs, "Test added successfully");

			StudentTest facultyTest = new StudentTest();
			facultyTest.setUsername(username);
			facultyTest.setTestId(test.getId());
			facultyTest.setCreatedBy(username);
			facultyTest.setLastModifiedBy(username);
			facultyTest.setGroupId(null);
			facultyTest.setCourseId(test.getCourseId().toString());
			studentTestService.insert(facultyTest);

			List<UserCourse> faculty = userCourseService.findAllFacultyWithModuleAndProgram(test.getModuleId(),
					String.valueOf(test.getAcadYear()), userdetails1.getProgramId(),
					String.valueOf(test.getCampusId()));
			logger.info("fauclty size : is" + faculty.size());
			for (UserCourse st : faculty) {
				StudentTest fTest = new StudentTest();
				fTest.setUsername(st.getUsername());
				fTest.setTestId(test.getId());
				fTest.setCreatedBy(username);
				fTest.setLastModifiedBy(username);
				fTest.setGroupId(null);
				fTest.setCourseId(test.getCourseId().toString());
				studentTestService.insert(fTest);
			}

			if ("Y".equals(test.getRandomQuestion())) {
				if ("N".equals(test.getSameMarksQue())) {
					m.addAttribute("testConfiguration", new TestConfiguration());
					m.addAttribute("testId", test.getId());
					m.addAttribute("TotalScore", test.getMaxScore());
					m.addAttribute("maxQuestion", test.getMaxQuestnToShow());

					return "test/configureQuestionMarksForTest";
				}
			}

		} catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addTestFormByAdmin";
		}catch (Exception e) {
			logger.error(e.getMessage(), e);

			// redirectAttrs.addAttribute("courseId", test.getCourseId());
			setError(redirectAttrs, "Error in adding Test");

			return "redirect:/addTestFormByAdmin";
		}
		// viewTestDetailsByAdmin
		redirectAttrs.addAttribute("testId", test.getId());
		redirectAttrs.addAttribute("moduleId", test.getModuleId());
		return "redirect:/viewTestDetailsByAdmin";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/viewTestDetailsByAdmin", method = RequestMethod.GET)
	public String viewTestDetailsByAdmin(@RequestParam(required = false) Long testId, Model m,
			@RequestParam(required = false) String groupId, @RequestParam(required = false) Long campusId,
			@RequestParam(required = false) String moduleId, Principal p) {
		m.addAttribute("webPage", new WebPage("viewTest", "Test Details", true, false));
		try {

			String username = p.getName();
			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
			m.addAttribute("allCampuses", userService.findCampus());
			Test testFromDb = testService.findByID(testId);
			m.addAttribute("test", testFromDb);

			StudentTest studentTest = studentTestService.findBytestIDAndUsername(testId, p.getName());

			int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(testId);
			List<TestQuestion> testQuestionsForTestId = new ArrayList<>();
			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				testQuestionsForTestId = testQuestionService.findByTestId(testId);
				if (testQuestionsForTestId.size() == 0) {
					m.addAttribute("isTestQConfigured", false);
				}
				if (testQuestionsForTestId.size() > 0) {

					m.addAttribute("isTestQConfigured", true);
				}

				// commented By Akshay As Faculty will allocate student even
				// after test starts.

				/*
				 * if (chkStartDateToConfigQuestn == 0) { List<StudentTest> students = new
				 * ArrayList<>(); setNote(m, "Test has already started :" +
				 * testFromDb.getTestName());
				 * 
				 * if ((testQuestionsForTestId.size() > Integer
				 * 
				 * .parseInt(testFromDb.getMaxQuestnToShow()) && "Y"
				 * .equals(testFromDb.getRandomQuestion())) ||
				 * "N".equals(testFromDb.getRandomQuestion())) {
				 * 
				 * if (campusId != null) { testFromDb.setCampusId(campusId); students =
				 * studentTestService .getStudentForTestAndCampusId( testFromDb.getId(),
				 * testFromDb.getCourseId(), campusId);
				 * 
				 * } else {
				 * 
				 * students = studentTestService.getStudentForTest( testFromDb.getId(),
				 * testFromDb.getCourseId()); }
				 * 
				 * } m.addAttribute("id", testId); m.addAttribute("students", students);
				 * 
				 * return "test/testDetails"; }
				 */

				// End

				if (testQuestionsForTestId.isEmpty() && chkStartDateToConfigQuestn == 1) {

					setNote(m, "please configure question for test :" + testFromDb.getTestName());

					return "test/testDetailsForAdmin";
				}

				if (testQuestionsForTestId.size() > 0 && chkStartDateToConfigQuestn == 1) {
					if (testQuestionsForTestId.size() <= Integer.parseInt(testFromDb.getMaxQuestnToShow())
							&& "Y".equals(testFromDb.getRandomQuestion())) {
						setNote(m,
								"number of questions configured is not sufficient to allocate students, please configure more questions:"
										+ testFromDb.getTestName());

						return "test/testDetailsForAdmin";
					}
				}

			}

			List<StudentTest> students = new ArrayList<>();
			if ((testQuestionsForTestId.size() > Integer.parseInt(testFromDb.getMaxQuestnToShow())
					&& "Y".equals(testFromDb.getRandomQuestion()))
					|| "N".equals(testFromDb.getRandomQuestion()) && testQuestionsForTestId.size() > 0) {

				if ("Mix".equals(testFromDb.getTestType()) && "Y".equals(testFromDb.getRandomQuestion())) {

					List<TestQuestion> testQuestionDes = new ArrayList<>();
					List<TestQuestion> testQuestionMcq = new ArrayList<>();
					List<TestQuestion> testQuestionRng = new ArrayList<>();
					List<TestQuestion> testQuestionImg = new ArrayList<>();

					for (TestQuestion tq : testQuestionsForTestId) {

						if ("MCQ".equals(tq.getQuestionType())) {
							testQuestionMcq.add(tq);
						} else if ("Numeric".equals(tq.getQuestionType())) {
							testQuestionRng.add(tq);
						} else if ("Descriptive".equals(tq.getQuestionType())) {
							testQuestionDes.add(tq);
						} else if ("Image".equals(tq.getQuestionType())) {
							testQuestionImg.add(tq);
						}
					}

					if ((testQuestionDes.size() >= Integer.parseInt(testFromDb.getMaxDesQueToShow())
							&& testQuestionMcq.size() >= Integer.parseInt(testFromDb.getMaxMcqQueToShow())
							&& testQuestionRng.size() >= Integer.parseInt(testFromDb.getMaxRngQueToShow())
							&& testQuestionImg.size() >= Integer.parseInt(testFromDb.getMaxImgQueToShow())
							&& "Y".equals(testFromDb.getRandomQuestion()))) {

						List<String> courseIds = courseService.courseListByParams(testFromDb.getModuleId(),
								String.valueOf(testFromDb.getAcadYear()), userdetails1.getProgramId());
						students = studentTestService.getStudentForTestByAdmin(testFromDb.getId(), courseIds);

					} else {
						setNote(m, "Please Add Questions More Than Max Question Shown To Student for each type");
					}

				} else {

					List<String> courseIds = courseService.courseListByParams(testFromDb.getModuleId(),
							String.valueOf(testFromDb.getAcadYear()), userdetails1.getProgramId());
					students = studentTestService.getStudentForTestByAdmin(testFromDb.getId(), courseIds);
				}
				// }

			}
			m.addAttribute("noOfStudentAllocated", studentTestService.getNoOfStudentsAllocated(testId));

			m.addAttribute("students", students);
			m.addAttribute("groupId", groupId);
			m.addAttribute("moduleId", moduleId);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in displaying test");
			return "test/testDetailsForAdmin";
		}
		return "test/testDetailsForAdmin";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTestPoolFormByAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTestPoolFormByAdmin(@ModelAttribute TestPool testPool, Model m, Principal principal,
			@RequestParam(required = false) String courseId, RedirectAttributes redirectAttrs) {

		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			if (courseId != null) {
				testPool.setCourseId(courseId);
			}

			m.addAttribute("webPage", new WebPage("test", "Create Test Pool", false, false));

			if (testPool.getId() != null) {
				TestPool testPoolsDB = testPoolService.findByID(testPool.getId());
				if (testPoolsDB == null) {
					setError(m, "TEST " + testPool.getTestPoolName() + " does not exist");
					testPool.setId(null);
				} else {
					LMSHelper.copyNonNullFields(testPool, testPoolsDB);
					m.addAttribute("edit", "true");

				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			redirectAttrs.addFlashAttribute("testPools", testPool);
			setError(redirectAttrs, "Error in adding Test Pool");
			return "test/addTestPoolByAdmin";
		}
		redirectAttrs.addFlashAttribute("testPools", testPool);
		//acadYear change by Hiren 02-02-2021
		List<String> acadYearCodeList = courseService.getAllAcadYear();

		m.addAttribute("acadYearCodeList", acadYearCodeList);

		return "test/addTestPoolByAdmin";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTestPoolByAdmin", method = RequestMethod.POST)
	public String addTestPoolByAdmin(@ModelAttribute TestPool testPool, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		try {
			String username = principal.getName();
			/* New Audit changes start */
			HtmlValidation.validateHtml(testPool, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(testPool.getTestPoolName());
			/* New Audit changes end */
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			testPool.setCreatedBy(username);
			testPool.setLastModifiedBy(username);
			testPool.setIsCreatedByAdmin("Y");
			logger.info("test pool courseid" + testPool.getCourseId());

			logger.info("testPool---------->" + testPool);
			testPoolService.insertWithIdReturn(testPool);
			redirectAttrs.addFlashAttribute("testPool", testPool);
			setSuccess(redirectAttrs, " Test Pool added successfully");

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addTestPoolFormByAdmin";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Test Pool");
			return "redirect:/addTestPoolFormByAdmin";
		}
		return "redirect:/uploadTestQuestionPoolForm";
	}
	//change method name by hiren 02-02-2021
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getFacultyByModuleTest", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyByModuleTest(@RequestParam(name = "moduleId") String moduleId,
			@RequestParam(name = "acadYear") String acadYear, @RequestParam(name = "campusId",required = false, defaultValue = "null") String campusId,
			Principal principal) {

		String json = "";
		// String username = principal.getName();
		Token userdetails1 = (Token) principal;
		List<UserCourse> faculty = userCourseService.findAllFacultyWithModuleIdTest(moduleId, acadYear, campusId,
				userdetails1.getProgramId());

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (UserCourse ass : faculty) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(ass.getUsername(), ass.getFacultyName() + "(" + ass.getUsername() + ")");

			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return json;

	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/updateTestByAdmin", method = RequestMethod.POST)
	public String updateTestByAdmin(@ModelAttribute Test test, RedirectAttributes redirectAttrs, Principal principal,
			Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttrs.addAttribute("testId", test.getId());
		Test oldTest = testService.findByID(test.getId());
		try {
			/* New Audit changes start */
			
			HtmlValidation.validateHtml(test, Arrays.asList("testDescription"));
			BusinessBypassRule.validateAlphaNumeric(test.getTestName());
			Course course = courseService.findByID(test.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(test.getFacultyId(), String.valueOf(test.getCourseId()));
			if(null == userccourse) {
				throw new ValidationException("Invalid faculty selected.");
			}
			Utils.validateStartAndEndDates(test.getStartDate(), test.getEndDate());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxScore());
			BusinessBypassRule.validateNumericNotAZero(test.getDuration());
			BusinessBypassRule.validateNumericNotAZero(test.getPassScore());
			BusinessBypassRule.validateNumericNotAZero(test.getMaxAttempt());
			BusinessBypassRule.validateYesOrNo(test.getRandomQuestion());
			BusinessBypassRule.validateYesOrNo(test.getSameMarksQue());
			if ("Y".equals(test.getRandomQuestion())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMaxQuestnToShow());
			}
			if ("Y".equals(test.getSameMarksQue())) {
				BusinessBypassRule.validateNumericNotAZero(test.getMarksPerQue());
			}

			if("Mix".equals(test.getTestType())) {
				BusinessBypassRule.validateNumeric(test.getMaxQuestnToShow());
				BusinessBypassRule.validateNumeric(test.getMaxDesQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxImgQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxMcqQueToShow());
				BusinessBypassRule.validateNumeric(test.getMaxRngQueToShow());
			}
			/* New Audit changes end */
			if ("Y".equals(test.getRandomQuestion()) && "Y".equals(test.getSameMarksQue())) {
				double total = test.getMarksPerQue() * Double.valueOf(test.getMaxQuestnToShow());
				if (total != test.getMaxScore()) {
					redirectAttrs.addAttribute("id", test.getId());

					setError(redirectAttrs, "Error in updating Test (Misconfigured values).");
					return "redirect:/addTestFormByAdmin";
				}
			}
			System.out.println("testId------------>" + test.getId());
			Test testFromDb = testService.findByID(test.getId());
			System.out.println("courseId t------------>" + test.getCourseId());
			test.setCreatedBy(testFromDb.getCreatedBy());
			test.setCreatedDate(testFromDb.getCreatedDate());
			test.setLastModifiedBy(username);
			System.out.println("courseId------------>" + test.getCourseId());
			Course c = courseService.findByID(test.getCourseId());

			test.setAcadMonth(c.getAcadMonth());
			test.setAcadYear(Integer.valueOf(c.getAcadYear()));
			//test.setAutoAllocateToStudents("Y");
			test.setFacultyId(oldTest.getFacultyId());

			List<StudentTest> studentTestDB = studentTestService.findOneTest(test.getId());
			List<TestQuestion> questionLIst = testQuestionService.findByTestId(test.getId());
			double sumOfTestQuestionMarks = 0.0;
			if (!"Y".equals(test.getRandomQuestion())) {
				if (questionLIst.size() > 0) {
					sumOfTestQuestionMarks = testQuestionService.getSumOfTestQuestionMarksByTestId(test.getId());
				}
				if (test.getMaxScore() == sumOfTestQuestionMarks) {

					if (!"Y".equals(test.getAutoAllocateToStudents())) {
						m.addAttribute("showProceed", true);
					} else {
						if (studentTestDB.size() == 0) {
							autoAllocateStudent(test, username);
						}
						m.addAttribute("showStudents", true);
					}
				}
			} else {
				if (questionLIst.size() > Integer.parseInt(test.getMaxQuestnToShow())) {
					if (!"Y".equals(test.getAutoAllocateToStudents())) {
						m.addAttribute("showProceed", true);
					} else {
						if (studentTestDB.size() == 0) {
							autoAllocateStudent(test, username);
						}
						m.addAttribute("showStudents", true);
					}

				}
			}
			if(studentTestDB.size() > 0) {
				studentTestService.removeFaultyDemoTestTime(test.getId());
			}
			if ("N".equals(test.getIsPasswordForTest())) {
				test.setPasswordForTest(null);
			}

			logger.info("test--->" + test);
			logger.info("oldTest--->" + oldTest);
			if (test.getMaxScore() != oldTest.getMaxScore()
					|| !test.getMaxQuestnToShow().equals(oldTest.getMaxQuestnToShow())
					|| !test.getSameMarksQue().equals(oldTest.getSameMarksQue())
					|| !test.getRandomQuestion().equals(oldTest.getRandomQuestion())
					|| test.getMarksPerQue() != oldTest.getMarksPerQue()
					|| !test.getEndDate().equals(testFromDb.getEndDate())
					|| !test.getStartDate().equals(testFromDb.getStartDate())) {
				List<StudentTest> studentTestList = studentTestService.findByTestId(test.getId());
				if (studentTestList.size() > 0) {
					if (!test.getEndDate().equals(testFromDb.getEndDate())
							|| !test.getStartDate().equals(testFromDb.getStartDate())) {
						String changeTestFolderPath = changeTestFolder(test, testFromDb);
						if ("ERROR".equals(changeTestFolderPath)) {
							setError(redirectAttrs, "Error in updating test, updated folder cannot be created");
							return "redirect:/addTestFormByAdmin";
						}
					} else {
						if (!"Y".equals(test.getRandomQuestion())) {
							if (questionLIst.size() > 0) {
								sumOfTestQuestionMarks = testQuestionService
										.getSumOfTestQuestionMarksByTestId(test.getId());
							}
							if (test.getMaxScore() == sumOfTestQuestionMarks) {
								logger.info("reAllocateStudent from update Test--->");
								reAllocateStudent(test);
							} else if (test.getMaxScore() <= sumOfTestQuestionMarks) {
								test.setRandomQuestion(oldTest.getRandomQuestion());
								setNote(redirectAttrs,
										"More than required questions are alerady configured, So changes will not be reflect.");
							}
						} else {
							if (questionLIst.size() > Integer.parseInt(test.getMaxQuestnToShow())) {
								reAllocateStudent(test);
							}
						}
					}
				}
			}

			if (testService.update(test) > 0)
				setSuccess(redirectAttrs, "Test updated successfully");
			else
				setError(redirectAttrs, "Test cannot be updated");

			if ("Y".equals(test.getRandomQuestion())) {
				if ("N".equals(test.getSameMarksQue())) {
					m.addAttribute("testConfiguration", new TestConfiguration());
					m.addAttribute("testId", test.getId());
					m.addAttribute("edit", "Y");
					m.addAttribute("TotalScore", test.getMaxScore());
					m.addAttribute("maxQuestion", test.getMaxQuestnToShow());

					List<TestConfiguration> tcList = testConfigurationService.findAllByTestId(test.getId());
					m.addAttribute("TcList", tcList);
					return "test/configureQuestionMarksForTest";
				}
			}

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			redirectAttrs.addFlashAttribute("test", test);
			return "redirect:/addTestFormByAdmin";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test");
			redirectAttrs.addFlashAttribute("test", test);
			return "redirect:/addTestFormByAdmin";
		}
		return "redirect:/viewTestDetailsByAdmin";
	}

	// 17-09-2020 Hiren

	public boolean checkStudentsQuestionAnswerForMCQ(String correctOption, String studentOption) {
		// Multiselect answer update
		List<String> splittedCorrectOption = new ArrayList<>();
		List<String> splittedAnswers = new ArrayList<>();
		if (correctOption.contains(",")) {
			splittedCorrectOption = Arrays.asList(correctOption.split(","));
		}

		if (studentOption.contains(",")) {
			splittedAnswers = Arrays.asList(studentOption.split(","));
			int count = 0;
			if (splittedCorrectOption.size() > 0) {
				if (splittedCorrectOption.size() > splittedAnswers.size()) {
					for (String s : splittedCorrectOption) {
						if (splittedAnswers.contains(s)) {

						} else {
							count++;
						}
					}
				} else {
					for (String s : splittedAnswers) {
						if (splittedCorrectOption.contains(s)) {

						} else {
							count++;
						}
					}
				}
			} else {
				count++;
			}
			if (count > 0) {
				return false;
				// studentQuestionResponse.setMarks(0.0);
			} else {
				return true;
				// studentQuestionResponse.setMarks(tq.getMarks());
			}
		} else {
			if (correctOption.equals(studentOption)) {
				return true;
				// studentQuestionResponse.setMarks(tq.getMarks());
			} else {
				return false;
				// studentQuestionResponse.setMarks(0.0);
			}
		}
	}

	// New Pool Changes
	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addTestConfigurationForm", method = RequestMethod.POST)
	public String addTestConfigurationForm(@RequestParam(required = false) Long testId,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		String username = principal.getName();
		Gson g = new Gson();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Test test = testService.findByID(testId);

		int chkStartDateForUpdate = testService.chkStartDateForUpdate(test.getId());
		if (chkStartDateForUpdate == 0) {
			setError(redirectAttrs, "cannot configure weightage test already started");
			redirectAttrs.addAttribute("testId", test.getId());
			return "redirect:/viewTestDetails";
		}

		m.addAttribute("testConfiguration", new TestConfiguration());
		m.addAttribute("testId", test.getId());
		m.addAttribute("edit", "N");
		m.addAttribute("TotalScore", test.getMaxScore());
		m.addAttribute("maxQuestion", test.getMaxQuestnToShow());
		logger.info("TotalScore--->" + test.getMaxScore());
		logger.info("maxQuestion--->" + test.getMaxQuestnToShow());

		List<TestConfiguration> tcList = testConfigurationService.findAllByTestId(test.getId());
		List<TestPool> testPoolsList = testPoolService.findAllTestPoolsByUserAndCourse(username,
				String.valueOf(test.getCourseId()));
		m.addAttribute("testPoolsList", testPoolsList);
		List<TestPoolConfiguration> tpcList = testPoolConfigurationService.findAllByTestId(test.getId());
		if (null != tcList && tcList.size() > 0) {
			logger.info("weightageTypes--->questions");
			m.addAttribute("weightageTypes", "questions");
			m.addAttribute("edit", "Y");
			m.addAttribute("TcList", tcList);
		} else if (null != tpcList && tpcList.size() > 0) {
			logger.info("weightageTypes--->poolQuestions");
			m.addAttribute("weightageTypes", "poolQuestions");
			m.addAttribute("edit", "Y");
			m.addAttribute("TpcList", tpcList);
		} else {
			m.addAttribute("weightageTypes", "");
		}
		return "test/configureQuestionMarksForTest";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/addPoolTestConfiguration", method = RequestMethod.POST)
	public @ResponseBody String addPoolTestConfiguration(@RequestParam(required = false) Long testId,
			@RequestParam(required = false) String testPoolConfiguration, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		String username = principal.getName();
		Gson g = new Gson();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Test test = testService.findByID(testId);

		redirectAttrs.addAttribute("testId", test.getId());
		redirectAttrs.addAttribute("courseId", test.getCourseId());
		redirectAttrs.addAttribute("groupId", test.getGroupId());

		try {

			List<TestQuestion> testQuestionList = testQuestionService.findByTestId(test.getId());
			logger.info("testQuestionList---->" + testQuestionList.size());
			List<StudentTest> studentTestByTestId = studentTestService.findStudentTestForDelete(test.getId());
			if (testQuestionList.size() > 0) {

				for (TestQuestion tq : testQuestionList) {
					studentQuestionResponseService.deleteFacultyTestResponse(String.valueOf(test.getId()),
							principal.getName());
					studentQuestionResponseAuditService.deleteFacultyTestResponseAudit(String.valueOf(test.getId()),
							principal.getName());
					studentTestService.removeStudentQRespFile(test.getId());
					studentTestService.removeStudentQueResp(test.getId());
					testQuestionService.delete(tq);
				}
				String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getStartDate());
				String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", test.getEndDate());
				String testDate = testSDate + "-" + testEDate;

				String rootFolder = testBaseDir + "/" + app;
				File folderR = new File(rootFolder);
				folderR.setExecutable(true, false);
				folderR.setWritable(true, false);
				folderR.setReadable(true, false);

				String folderPath = testBaseDir + "/" + app + "/" + "Tests";
				File folderP = new File(folderPath);
				if (!folderP.exists()) {
					folderP.mkdirs();

					logger.info("folder created");
				}
				folderP.setExecutable(true, false);
				folderP.setWritable(true, false);
				folderP.setReadable(true, false);

				String subFolderPath = folderPath + "/" + test.getId() + "-" + testDate;
				logger.info("Folder------->" + subFolderPath);
				File subFolderP = new File(subFolderPath);
				if (subFolderP.exists()) {
					try {
						logger.info("Delete Folder------->");
						FileUtils.deleteDirectory(subFolderP);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("Error--->", e);
					}
				}
				studentTestService.deleteBatch(studentTestByTestId);
			}

			List<TestPoolConfiguration> testPoolCongfigList = new ArrayList<>();
			testConfigurationService.deleteByTestId(String.valueOf(testId));
			testPoolConfigurationService.deleteByTestId(String.valueOf(testId));
			ObjectMapper mapper = new ObjectMapper();
			testPoolCongfigList = mapper.readValue(testPoolConfiguration,
					new TypeReference<List<TestPoolConfiguration>>() {
					});
			logger.info("testPoolCongfigList--->" + testPoolCongfigList);
			for (TestPoolConfiguration tpc : testPoolCongfigList) {
				tpc.setCreatedBy(username);
			}
			testPoolConfigurationService.insertBatch(testPoolCongfigList);

			return "{\"status\":\"success\"}";

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Test");
			redirectAttrs.addFlashAttribute("test", test);
			return "{\"status\":\"failed\"}";
		}

	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/saveStudentTestForAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveStudentTestForAdmin(@ModelAttribute Test test, Model m, RedirectAttributes redirectAttr,
			Principal principal) {
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		String username = principal.getName();
		studentList.add(username);
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttr.addFlashAttribute("test", test);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		Test testFromDb = testService.findByID(test.getId());
		int chkStartDateToConfigQuestn = testService.chkStartDateForUpdate(test.getId());
		m.addAttribute("test", testFromDb);
		//01-03-2021
		test.setCourseId(testFromDb.getCourseId());
		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			List<TestQuestion> testQuestionsForTestId = testQuestionService.findByTestId(test.getId());

			if (testQuestionsForTestId.isEmpty()) {

				setNote(m, "please configure question for test :" + testFromDb.getTestName());

				return "test/testDetails";

			}

			// commented By Akshay as test can be allocated to students event
			// after end date of a test.

			/*
			 * else if (chkStartDateToConfigQuestn < 1) {
			 * 
			 * setNote(m, "Test has already started :" + testFromDb.getTestName());
			 * 
			 * return "test/testDetails"; }
			 */

			// End

			else if (testQuestionsForTestId.size() < Integer.parseInt(testFromDb.getMaxQuestnToShow())
					&& "Y".equals(testFromDb.getRandomQuestion())) {
				setNote(m,
						"number of questions configured is not sufficient to allocate students, please configure more questions:"
								+ testFromDb.getTestName());

				return "test/testDetails";
			}
		}

		try {
			logger.info("Students------->"+test.getStudents());
			logger.info("StudentTests------->"+test.getStudentTests());
			logger.info("Module------->"+test.getModuleId());
//			List<UserCourse> userCourseListByCourseId = new ArrayList<>();
//			userCourseListByCourseId = userCourseService.getStudentsByModuleId(String.valueOf(testFromDb.getModuleId()),
//					String.valueOf(testFromDb.getAcadYear()));
			for (StudentTest studentTest : test.getStudentTests()) {

				studentTest.setCreatedBy(username);
				studentTest.setLastModifiedBy(username);
				studentTest.setGroupId(test.getGroupId() == null ? "" : test.getGroupId());
				//studentTest.setCourseId(test.getCourseId() == null ? "" : test.getCourseId() + "");
				
				String courseIdForStudent = userCourseService.getCourseIdByModuleAndAcadYear(studentTest.getUsername(),testFromDb.getModuleId(),String.valueOf(testFromDb.getAcadYear()));
				
				studentTest.setCourseId(courseIdForStudent);
				studentList.add(studentTest.getUsername());
			}
			studentTestService.insertBatch(test.getStudentTests());

			// Code Here For Allocation of Test Questions...

			logger.info("file creation entered");
			String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDb.getStartDate());
			String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testFromDb.getEndDate());
			String testDate = testSDate + "-" + testEDate;

			String rootFolder = testBaseDir + "/" + app;
			File folderR = new File(rootFolder);
			folderR.setExecutable(true, false);
			folderR.setWritable(true, false);
			folderR.setReadable(true, false);

			String folderPath = testBaseDir + "/" + app + "/" + "Tests";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
				logger.info("folder created");
			}
			folderP.setExecutable(true, false);
			folderP.setWritable(true, false);
			folderP.setReadable(true, false);

			String subFolderPath = folderPath + "/" + testFromDb.getId() + "-" + testDate;

			File subFolderP = new File(subFolderPath);
			if (!subFolderP.exists()) {
				subFolderP.mkdir();
				logger.info("subfolder created");
			}
			subFolderP.setExecutable(true, false);
			subFolderP.setWritable(true, false);
			subFolderP.setReadable(true, false);

			logger.info("subfolder created" + subFolderPath);

			studentTestService.allocateTestQuestionsForAllStudent(testFromDb.getId(), subFolderPath);

			try {
				Test retrive = testService.findByID((test.getId()));
				Course course = test.getCourseId() != null ? courseService.findByID(Long.valueOf(test.getCourseId()))
						: null;

				String subject = " Test with name " + testFromDb.getTestName();

				StringBuffer buff = new StringBuffer(subject);

				if (course != null) {
					buff.append(" for Course ");
					buff.append(course.getCourseName());
					buff.append(" is scheduled on " + retrive.getStartDate());
				}
				buff.append(" allocated to you ");
				subject = buff.toString();

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(retrive.getSendEmailAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}

					if ("Y".equals(retrive.getSendSmsAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}
					if ("Y".equals(retrive.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(retrive.getSendSmsAlert())) {

						if (result != null)
							result = userService.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}

			} catch (Exception e) {
				logger.error("Exception e", e);
			}
			setSuccess(redirectAttr, "Students Allocated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating test");
			redirectAttr.addAttribute("testId", test.getId());
			return "redirect:/viewTestDetails";
		}
		redirectAttr.addAttribute("testId", test.getId());
		return "redirect:/viewTestDetails";
	}
}
