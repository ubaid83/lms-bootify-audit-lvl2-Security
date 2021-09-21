package com.spts.lms.web.controllers;

import java.io.File;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.test.StudentQuestionResponse;
import com.spts.lms.beans.test.StudentQuestionResponseAudit;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.test.TestConfiguration;
import com.spts.lms.beans.test.TestPoolConfiguration;
import com.spts.lms.beans.test.TestQuestion;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.dashboard.DashboardService;
import com.spts.lms.services.test.StudentQuestionResponseAuditService;
import com.spts.lms.services.test.StudentQuestionResponseService;
import com.spts.lms.services.test.StudentTestAuditService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestConfigurationService;
import com.spts.lms.services.test.TestPoolConfigurationService;
import com.spts.lms.services.test.TestQuestionService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Secured({ "ROLE_FACULTY", "ROLE_CORD", "ROLE_AREA_INCHARGE", "ROLE_AR" })
@Controller
public class StudentTestController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	UserService userService;
	@Autowired
	StudentTestAuditService studentTestAuditService;

	@Autowired
	StudentQuestionResponseService studentQuestionResponseService;

	@Autowired
	TestQuestionService testQuestionService;

	@Autowired
	TestService testService;

	@Autowired
	DashboardService dashBoardService;

	@Autowired
	StudentQuestionResponseAuditService studentQuestionResponseAuditService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	TestConfigurationService testConfigurationService;

	@Autowired
	LmsVariablesService lmsVariablesService;
	
	@Autowired
	TestPoolConfigurationService testPoolConfigurationService;

	@Value("${app}")
	private String app;

	@Value("${file.base.directory}")
	private String baseDir;

	@Value("${file.base.directory.test}")
	private String testBaseDir;

	private static final Logger logger = Logger.getLogger(StudentTestController.class);

	@RequestMapping(value = "/addStudentTestForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addStudentTestForm(@RequestParam Long testId, Model m, Principal principal) {

		m.addAttribute("webPage", new WebPage("test", "Take Test", false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		StudentTest studentTest = new StudentTest();
		studentTest.setTestId(testId);
		if (testId != null && !testId.equals("")) {
			studentTest = studentTestService.findByID(testId);
			m.addAttribute("edit", "true");
		}
		m.addAttribute("test", studentTest);

		return "test/addTest";
	}

	@RequestMapping(value = "/addStudentTest", method = RequestMethod.POST)
	public String addStudentTest(@ModelAttribute StudentTest studentTest, Model m, Principal principal) {
		try {
			String username = principal.getName();
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			studentTest.setCreatedBy(username);
			studentTest.setLastModifiedBy(username);

			studentTestService.insertWithIdReturn(studentTest);

			setSuccess(m, "Test added successfully");

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in adding Test");
			return "test/addTest";
		}
		return "test/testDetails";
	}

	public StudentQuestionResponseAudit responseBeanToAuditBeanForSubjectiveEvaluation(StudentQuestionResponse sqr,
			Long testId) {

		StudentQuestionResponseAudit sqra = new StudentQuestionResponseAudit();
		StudentTest st = studentTestService.findByID(sqr.getStudentTestId());

		testId = st.getTestId();

		StudentTest studentTestDB = studentTestService.findByID(sqr.getStudentTestId());
		/*
		 * List<StudentTest> studentTestAuditList = studentTestAuditService
		 * .findByTestIdAndUsername(st.getTestId(), sqr.getUsername()); int
		 * latestAttempt = 0;
		 */

		sqra.setUsername(sqr.getUsername());
		sqra.setStudentTestId(sqr.getStudentTestId());
		sqra.setQuestionId(sqr.getQuestionId());
		sqra.setAnswer(sqr.getAnswer());
		sqra.setCreatedBy(sqr.getUsername());
		sqra.setLastModifiedBy(sqr.getUsername());
		sqra.setAttempts(String.valueOf(studentTestDB.getAttempt()));

		return sqra;
	}

	@RequestMapping(value = "/addStudentMarks", method = RequestMethod.POST)
	public String addStudentMarks(@ModelAttribute Test test, @RequestParam String testQuestionId,
			@RequestParam String studusername, Principal p, Model m, RedirectAttributes redirectAttributes) {
		String username1 = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username1);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("addStudentMarks", "Add Student Marks", false, false));
		TestQuestion testQuestion = test.getTestQuestions().get(test.getTestQuestions().size() - 1);
		TestQuestion testQuestionDB = testQuestionService.findByID(testQuestion.getId());
		int testId = testQuestionService.findTestIdByTestQuestionId(testQuestionId);

		StudentQuestionResponse studQuestionResponse = studentQuestionResponseService
				.findByStudentUsernameAndTestQuestnId(studusername, testQuestionId);
		String username = p.getName();

		if (testQuestionDB.getMarks() < testQuestion.getMarks()) {
			redirectAttributes.addAttribute("id", testId);
			redirectAttributes.addAttribute("studusername", studusername);
			setNote(redirectAttributes, "evaluated marks must not be greater than testQuestion marks");
			return "redirect:/evaluateTestForm";
		} else {
			double totalMarks = 0.0;

			studQuestionResponse.setMarks(testQuestion.getMarks());
			studQuestionResponse.setLastModifiedBy(username);
			studQuestionResponse.setLastModifiedDate(Utils.getInIST());

			StudentQuestionResponseAudit studentQuestionResponseAudit = responseBeanToAuditBeanForSubjectiveEvaluation(
					studQuestionResponse, testQuestion.getTestId());
			studentQuestionResponseAudit.setAnswer(studQuestionResponse.getAnswer());
			studentQuestionResponseAudit.setMarks(studQuestionResponse.getMarks());

			m.addAttribute("testQuestId", testQuestionId);
			try {
				studentQuestionResponseService.insert(studQuestionResponse);
				studentQuestionResponseAuditService.insert(studentQuestionResponseAudit);

				List<StudentQuestionResponse> scoreList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studQuestionResponse.getStudentTestId()));

				for (StudentQuestionResponse s : scoreList) {
					if (s.getMarks() != null) {
						totalMarks = totalMarks + s.getMarks();
					}

				}
				StudentTest studenttest = studentTestService
						.findStudentTestByStudentTestID(studQuestionResponse.getStudentTestId());

				studenttest.setScore(totalMarks);
				studentTestService.insert(studenttest);
				studenttest.setLastModifiedBy(p.getName());
				studenttest.setLastModifiedDate(Utils.getInIST());
				studentTestAuditService.update(studenttest);
				redirectAttributes.addAttribute("id", testId);
				redirectAttributes.addAttribute("studusername", studusername);
				setSuccess(redirectAttributes, "student marks saved");

				return "redirect:/evaluateTestForm";

			} catch (Exception ex) {
				logger.error("Exception", ex);
				redirectAttributes.addAttribute("id", testId);
				redirectAttributes.addAttribute("studusername", studusername);
				setError(redirectAttributes, "Error in saving marks");
				return "redirect:/evaluateTestForm";
			}
		}

	}

	@RequestMapping(value = "/updateStudentTest", method = RequestMethod.POST)
	public String updateStudentTest(@ModelAttribute StudentTest studentTest, Model m, Principal p) {
		m.addAttribute("webPage", new WebPage("test", "Update Test", false, false));
		try {
			String username = p.getName();

			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			StudentTest studentTestFromDb = studentTestService.findByID(studentTest.getId());
			studentTestFromDb = LMSHelper.copyNonNullFields(studentTestFromDb, studentTest);
			studentTestFromDb.setLastModifiedBy(username);

			studentTestService.update(studentTestFromDb);

			setSuccess(m, "Test updated successfully");

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in updating Test");
			return "test/addTest";
		}
		return "test/testDetails";
	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTest", method = RequestMethod.GET)
	 * public String startStudentTest(Principal p, @ModelAttribute Test test, Model
	 * m, @RequestParam("id") String testId) throws ParseException {
	 * m.addAttribute("webPage", new WebPage("studentTest", "Test", true, true));
	 * Test testDb = testService.findByID(Long.valueOf(testId)); StudentTest
	 * studentTestDB = studentTestService.findBytestIDAndUsername(
	 * Long.valueOf(testId), p.getName()); if (null == studentTestDB) { setError(m,
	 * "You have not been assigned the test"); return "test/studentTest"; } if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * int chkStartandEndDateOfTest = studentTestService
	 * .chkStartandEndDateOfTests(p.getName(), Long.valueOf(testId)); if
	 * (chkStartandEndDateOfTest == 0) { setError(m, "test can't be shown"); return
	 * "test/studentTest"; } else { studentTestDB.setTestStartTime(new Date());
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB);
	 * 
	 * if (studentTestDB.isCompleted()) setSuccess(m, "Test completed. You scored "
	 * + (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); else
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted())
	 * setSuccess(m, "Test completed. You scored " + (studentTestDB.getScore() ==
	 * null ? 0 : studentTestDB.getScore()) + " marks out of " +
	 * test.getMaxScore());
	 * 
	 * else setSuccess(m, "Test has started");
	 * 
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; } } return
	 * "test/studentTest"; }
	 */

	@RequestMapping(value = "/viewStudentTestResponse", method = RequestMethod.GET)
	public String viewStudentTestResponse(Principal p, RedirectAttributes redirectAttrs, @ModelAttribute Test test,
			Model m, @RequestParam("id") String testId) throws ParseException {
		Test testDb = testService.findByID(Long.valueOf(testId));
		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<TestQuestion> testQuestions = testQuestionService
				.findStudentResponseByTestIdAndUsernameOnlyAttemptedQuestions(testDb.getId(), p.getName());
		test.setTestQuestions(testQuestions);
		m.addAttribute("action", "view");

		test.setStudentTest(studentTestDB);
		m.addAttribute("test", test);
		m.addAttribute("testDb", testDb);

		return "test/studentTest";
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/deleteStudentTestResponse", method = RequestMethod.POST)
	public @ResponseBody String deleteStudentTestResponse(Principal p, @RequestParam String testId, Model m) {

		try {
			StudentTest studentTestDB = studentTestService.findBytestIDAndUsernameAndTestCompleted(Long.valueOf(testId),
					p.getName());

			if (studentTestDB != null) {
				List<StudentQuestionResponse> sqrList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));
				if (sqrList.size() > 0) {
					for (StudentQuestionResponse sqr : sqrList) {
						sqr.setMarks(0.0);
						sqr.setAnswer(" ");
						studentQuestionResponseService.delete(sqr);
					}
				}

				studentTestDB.setTestCompleted(null);

				studentTestService.upsert(studentTestDB);

				return "Y";
			} else {
				return "N";
			}
		} catch (Exception e) {
			setError(m, "Error in saving question");
			return null;
		}
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/startStudentTest", method = RequestMethod.GET)
	public String startStudentTest(Principal p, RedirectAttributes redirectAttrs, @ModelAttribute Test test, Model m,
			@RequestParam("id") String testId) throws ParseException {
		m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
		Test testDb = testService.findByID(Long.valueOf(testId));
		Token userdetails1 = (Token) p;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		if (null == studentTestDB) {
			setNote(redirectAttrs, "You have not been assigned the test");
			return "redirect:/testList";
		}
		// if (!studentTestDB.isCompleted()) {

		if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted())
				&& (testDb.getMaxAttempt() > studentTestDB.getAttempt()))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			String allowAfterEndDate = testDb.getAllowAfterEndDate();

			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
				return "redirect:/testList";
			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				// Date startDateTime = Utils.getInIST();
				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				// test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration());
				String completionTime = "";
				int durationCompletedByStudent = 0;
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
				} else {

					if (studentTestDB.getDurationCompleted() != null) {
						durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
						int durationForTest = testDb.getDuration();
						int remainingDurationForStudent = durationForTest - durationCompletedByStudent;

						completionTime = Utils.getCompletionTime(startDateTime, remainingDurationForStudent);
					} else {
						// durationCompletedByStudent = 0;
						completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
					}

				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					List<StudentQuestionResponse> sqrList = studentQuestionResponseService
							.findByStudentTestId(String.valueOf(studentTestDB.getId()));
					if (sqrList.size() > 0) {
						for (StudentQuestionResponse sqr : sqrList) {
							sqr.setMarks(0.0);
							sqr.setAnswer(" ");
							studentQuestionResponseService.delete(sqr);
						}
					}

					studentTestDB.setTestCompleted(null);
					studentTestService.upsert(studentTestDB);
				} else {
					studentTestService.upsert(studentTestDB);
				}

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				/*
				 * List<TestQuestion> testQuestions = testQuestionService
				 * .findByTestId(testDB.getId());
				 */
				List<TestQuestion> testQuestions = new ArrayList<>();
				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				if ("Y".equals(studentTestDB.getTestCompleted())) {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							username);
				} else {
					if (studentQuestnResponseList.size() > 0) {
						testQuestions = testQuestionService
								.findStudentResponseByTestIdAndUsernameForIncompleteTest(testDB.getId(), username);
					} else {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
								username);
					}
				}
				// .findStudentResponseByTestIdAndUsername(testDB.getId(),
				// p.getName());

				testQuestions = removeSpecialCharactersFromBean(testQuestions);

				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				if ("Y".equals(testDB.getRandomQuestion())) {
					/*
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
					 * .get(rand.nextInt(testQuestions.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 */
					if ("Y".equals(testDB.getSameMarksQue())) {

						for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
							Random rand = new Random();
							TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
							if (randomTestQuestnList.contains(testQuestionRandom)) {
								i--;
							} else {
								randomTestQuestnList.add(testQuestionRandom);
							}
						}

					} else if ("N".equals(testDB.getSameMarksQue())) {

						List<TestConfiguration> testConfigList = testConfigurationService
								.findAllByTestId(Long.parseLong(testId));

						for (TestConfiguration tc : testConfigList) {

							List<TestQuestion> testQuestionRandomList = testQuestions.stream()
									.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());

							testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
							for (int i = 0; i < tc.getNoOfQuestion(); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestionRandomList
										.get(rand.nextInt(testQuestionRandomList.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}
						}

					}
				} else {
					randomTestQuestnList.addAll(testQuestions);
				}
				test.setTestQuestions(randomTestQuestnList);

				if (studentQuestnResponseList.size() == 0) {
					List<StudentQuestionResponse> sqrList = new ArrayList<>();
					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (TestQuestion tq : randomTestQuestnList) {

						StudentQuestionResponse sqr = new StudentQuestionResponse();

						sqr.setUsername(username);
						sqr.setStudentTestId(studentTestDB.getId());
						sqr.setQuestionId(tq.getId());
						sqr.setLastModifiedBy(username);
						sqr.setCreatedBy(username);

						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
						sqrList.add(sqr);
						sqrAuditList.add(sqrAudit);

					}
					studentQuestionResponseService.insertBatch(sqrList);
					studentQuestionResponseAuditService.insertBatch(sqrAuditList);
				}

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");
			}

		} else {

			try {
				studentTestDB.setLastModifiedBy(p.getName());

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = testQuestionService
						.findStudentResponseByTestIdAndUsername(testDB.getId(), p.getName());
				test.setTestQuestions(testQuestions);
				test.setStudentTest(studentTestDB);
				// if (studentTestDB.isCompleted())
				if ("Y".equals(testDB.getShowResultsToStudents())) {

					setNote(redirectAttrs,
							"Test completed. You scored "
									+ (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore())
									+ " marks out of " + test.getMaxScore());
				} else {

					setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");

				}

			} catch (Exception e) {
				logger.error("Error", e);
				setError(m, "Test could not be started");
				return "test/studentTest";
			}
			setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!");
			return "redirect:/testList";
		}
		return "test/studentTest";
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/startStudentTestForSubjective", method = RequestMethod.GET)
	public String startStudentTestForSubjective(Principal p, RedirectAttributes redirectAttrs,
			@ModelAttribute Test test, Model m, @RequestParam("id") String testId) throws ParseException {
		m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		Test testDb = testService.findByID(Long.valueOf(testId));
		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		if (null == studentTestDB) {
			setNote(redirectAttrs, "You have not been assigned the test");
			return "redirect:/testList";
		}
		// if (!studentTestDB.isCompleted()) {

		if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted())
				&& (testDb.getMaxAttempt() > studentTestDB.getAttempt()))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			String allowAfterEndDate = testDb.getAllowAfterEndDate();
			// he is valid user to take test remain no of attempt
			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
				return "redirect:/testList";
			} else {

				m.addAttribute("action", "add");

				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				// Date startDateTime = Utils.getInIST();

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				// test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration());
				String completionTime = "";
				int durationCompletedByStudent = 0;
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
				} else {

					if (studentTestDB.getDurationCompleted() != null) {
						durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
						int durationForTest = testDb.getDuration();
						int remainingDurationForStudent = durationForTest - durationCompletedByStudent;

						completionTime = Utils.getCompletionTime(startDateTime, remainingDurationForStudent);
					} else {
						// durationCompletedByStudent = 0;
						completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
					}

				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);

				m.addAttribute("dateTime", startTestTimeFormat);
				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}

				List<StudentQuestionResponse> sqrList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));
				if (sqrList.size() > 0) {
					for (StudentQuestionResponse sqr : sqrList) {
						sqr.setMarks(0.0);
						sqr.setAnswer(" ");
						studentQuestionResponseService.delete(sqr);
					}
				}

				studentTestDB.setTestCompleted(null);

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = new ArrayList<>();
				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				if ("Y".equals(studentTestDB.getTestCompleted())) {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							p.getName());
				} else {
					if (studentQuestnResponseList.size() > 0) {
						testQuestions = testQuestionService
								.findStudentResponseByTestIdAndUsernameForIncompleteTest(testDB.getId(), p.getName());
					} else {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
								p.getName());
					}
				}
				// .findStudentResponseByTestIdAndUsername(testDB.getId(),
				// p.getName());
				testQuestions = removeSpecialCharactersFromBean(testQuestions);
				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				if ("Y".equals(testDB.getRandomQuestion())) {
					/*
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
					 * .get(rand.nextInt(testQuestions.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 */

					if ("Y".equals(testDB.getSameMarksQue())) {

						for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
							Random rand = new Random();
							TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
							if (randomTestQuestnList.contains(testQuestionRandom)) {
								i--;
							} else {
								randomTestQuestnList.add(testQuestionRandom);
							}
						}

					} else if ("N".equals(testDB.getSameMarksQue())) {

						List<TestConfiguration> testConfigList = testConfigurationService
								.findAllByTestId(Long.parseLong(testId));

						for (TestConfiguration tc : testConfigList) {

							List<TestQuestion> testQuestionRandomList = testQuestions.stream()
									.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());
							testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
							for (int i = 0; i < tc.getNoOfQuestion(); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestionRandomList
										.get(rand.nextInt(testQuestionRandomList.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}
						}

					}
				} else {
					randomTestQuestnList.addAll(testQuestions);
				}
				test.setTestQuestions(randomTestQuestnList);

				if (studentQuestnResponseList.size() == 0) {
					List<StudentQuestionResponse> squestnResponseList = new ArrayList<>();
					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (TestQuestion tq : randomTestQuestnList) {

						StudentQuestionResponse sqr = new StudentQuestionResponse();

						sqr.setUsername(p.getName());
						sqr.setStudentTestId(studentTestDB.getId());
						sqr.setQuestionId(tq.getId());
						sqr.setLastModifiedBy(p.getName());
						sqr.setCreatedBy(p.getName());

						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
						squestnResponseList.add(sqr);

						sqrAuditList.add(sqrAudit);

					}
					studentQuestionResponseService.insertBatch(squestnResponseList);

					studentQuestionResponseAuditService.insertBatch(sqrAuditList);
				}

				// test.setTestQuestions(testQuestions);

				test.setStudentTest(studentTestDB);
				/*
				 * if (studentTestDB.isCompleted()) setSuccess(m, "Test completed. You scored "
				 * + (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
				 * " marks out of " + test.getMaxScore()); else
				 */
				setSuccess(m, "Test has started");
			}

		} else {

			try {
				studentTestDB.setLastModifiedBy(p.getName());

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = testQuestionService
						.findStudentResponseByTestIdAndUsername(testDB.getId(), p.getName());
				test.setTestQuestions(testQuestions);
				test.setStudentTest(studentTestDB);
				// if (studentTestDB.isCompleted())
				setSuccess(m,
						"Test completed. You scored "
								+ (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) + " marks out of "
								+ test.getMaxScore());
				/*
				 * else setSuccess(m, "Test has started");
				 */

			} catch (Exception e) {
				logger.error("Error", e);
				setError(m, "Test could not be started");
				return "test/studentTest";
			}

			setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!");
			return "redirect:/testList";
		}
		return "test/studentTestForSubjective";
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/startStudentTestForMix", method = RequestMethod.GET)
	public String startStudentTestForMix(Principal p, RedirectAttributes redirectAttrs, @ModelAttribute Test test,
			Model m, @RequestParam("id") String testId) throws ParseException {
		m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
		Test testDb = testService.findByID(Long.valueOf(testId));
		Token userdetails1 = (Token) p;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		if (null == studentTestDB) {
			setNote(redirectAttrs, "You have not been assigned the test");
			return "redirect:/testList";
		}
		// if (!studentTestDB.isCompleted()) {

		if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted())
				&& (testDb.getMaxAttempt() > studentTestDB.getAttempt()))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			String allowAfterEndDate = testDb.getAllowAfterEndDate();

			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
				return "redirect:/testList";
			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				// Date startDateTime = Utils.getInIST();
				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				// test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration());
				String completionTime = "";
				int durationCompletedByStudent = 0;
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
				} else {

					if (studentTestDB.getDurationCompleted() != null) {
						durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
						int durationForTest = testDb.getDuration();
						int remainingDurationForStudent = durationForTest - durationCompletedByStudent;

						completionTime = Utils.getCompletionTime(startDateTime, remainingDurationForStudent);
					} else {
						// durationCompletedByStudent = 0;
						completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
					}

				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					List<StudentQuestionResponse> sqrList = studentQuestionResponseService
							.findByStudentTestId(String.valueOf(studentTestDB.getId()));
					if (sqrList.size() > 0) {
						for (StudentQuestionResponse sqr : sqrList) {
							sqr.setMarks(0.0);
							sqr.setAnswer(" ");
							studentQuestionResponseService.delete(sqr);
						}
					}

					studentTestDB.setTestCompleted(null);
					studentTestService.upsert(studentTestDB);
				} else {
					studentTestService.upsert(studentTestDB);
				}

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				/*
				 * List<TestQuestion> testQuestions = testQuestionService
				 * .findByTestId(testDB.getId());
				 */
				List<TestQuestion> testQuestions = new ArrayList<>();
				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				if ("Y".equals(studentTestDB.getTestCompleted())) {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							username);
				} else {
					if (studentQuestnResponseList.size() > 0) {
						testQuestions = testQuestionService
								.findStudentResponseByTestIdAndUsernameForIncompleteTest(testDB.getId(), username);
					} else {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
								username);
					}
				}

				testQuestions = removeSpecialCharactersFromBean(testQuestions);
				// .findStudentResponseByTestIdAndUsername(testDB.getId(),
				// p.getName());
				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				if ("Y".equals(testDB.getRandomQuestion())) {
					/*
					 * List<TestQuestion> testQuestionDes = new ArrayList<>(); List<TestQuestion>
					 * testQuestionMcq = new ArrayList<>(); List<TestQuestion> testQuestionRng = new
					 * ArrayList<>(); List<TestQuestion> testQuestionImg = new ArrayList<>();
					 * 
					 * for(TestQuestion tq : testQuestions){
					 * 
					 * if("MCQ".equals(tq.getQuestionType())){ testQuestionMcq.add(tq); }else
					 * if("Numeric".equals(tq.getQuestionType())){ testQuestionRng.add(tq); }else
					 * if("Descriptive".equals(tq.getQuestionType())){ testQuestionDes.add(tq);
					 * }else if("Image".equals(tq.getQuestionType())){ testQuestionImg.add(tq); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxDesQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionDes
					 * .get(rand.nextInt(testQuestionDes.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxMcqQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionMcq
					 * .get(rand.nextInt(testQuestionMcq.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxRngQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionRng
					 * .get(rand.nextInt(testQuestionRng.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxImgQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionImg
					 * .get(rand.nextInt(testQuestionImg.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 */

					if ("Y".equals(testDB.getSameMarksQue())) {

						for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
							Random rand = new Random();
							TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
							if (randomTestQuestnList.contains(testQuestionRandom)) {
								i--;
							} else {
								randomTestQuestnList.add(testQuestionRandom);
							}
						}

					} else if ("N".equals(testDB.getSameMarksQue())) {

						List<TestConfiguration> testConfigList = testConfigurationService
								.findAllByTestId(Long.parseLong(testId));

						for (TestConfiguration tc : testConfigList) {

							List<TestQuestion> testQuestionRandomList = testQuestions.stream()
									.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());
							testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
							for (int i = 0; i < tc.getNoOfQuestion(); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestionRandomList
										.get(rand.nextInt(testQuestionRandomList.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}
						}

					}

				} else {
					randomTestQuestnList.addAll(testQuestions);
				}
				test.setTestQuestions(randomTestQuestnList);

				if (studentQuestnResponseList.size() == 0) {
					List<StudentQuestionResponse> sqrList = new ArrayList<>();
					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (TestQuestion tq : randomTestQuestnList) {

						StudentQuestionResponse sqr = new StudentQuestionResponse();

						sqr.setUsername(username);
						sqr.setStudentTestId(studentTestDB.getId());
						sqr.setQuestionId(tq.getId());
						sqr.setLastModifiedBy(username);
						sqr.setCreatedBy(username);

						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
						sqrList.add(sqr);
						sqrAuditList.add(sqrAudit);

					}
					studentQuestionResponseService.insertBatch(sqrList);
					studentQuestionResponseAuditService.insertBatch(sqrAuditList);
				}

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");
			}

		} else {

			try {
				studentTestDB.setLastModifiedBy(p.getName());

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = testQuestionService
						.findStudentResponseByTestIdAndUsername(testDB.getId(), p.getName());
				test.setTestQuestions(testQuestions);
				test.setStudentTest(studentTestDB);
				// if (studentTestDB.isCompleted())
				if ("Y".equals(testDB.getShowResultsToStudents())) {

					setNote(redirectAttrs,
							"Test completed. You scored "
									+ (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore())
									+ " marks out of " + test.getMaxScore());
				} else {

					setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");

				}

			} catch (Exception e) {
				logger.error("Error", e);
				setError(m, "Test could not be started");
				return "test/studentTest";
			}
			setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!");
			return "redirect:/testList";
		}
		return "test/studentTestForMix";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/testList", method = { RequestMethod.GET, RequestMethod.POST })
	public String testList(@RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
			@ModelAttribute Test test, @RequestParam(required = false) Long courseId, Principal principal,
			HttpServletRequest request) {

		if (test != null) {
			test.setMaxAttempt(null);
		}

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		if (courseId == null) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord").equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("testList", "View Tests", true, false));
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			test.setFacultyId(username);
		}
		try {
			Page<Test> page;
			Page<Test> pageAdmin;

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				// 10-07-2020
				LmsVariables takeDemoTest = lmsVariablesService.getLmsVariableBykeyword("peerFaculty_takeDemoTest");
				String allocateFaculty = takeDemoTest.getValue();

				if (allocateFaculty.equals("Yes")) {
					if (courseId != null) {
						Course courseDB = courseService.findByID(courseId);
						page = testService.findByFacultyAndCourseForDemo(username, courseId, pageNo, pageSize);
						pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
								Long.valueOf(userdetails1.getProgramId()), courseDB.getModuleId(),
								courseDB.getAcadYear(), pageNo, pageSize);
					} else {
						page = testService.searchActiveByExactMatchReplacementForDemo(username,
								Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);

						pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
								Long.valueOf(userdetails1.getProgramId()), null, null, pageNo, pageSize);
					}
				} else {
					if (courseId != null) {
						Course courseDB = courseService.findByID(courseId);
						page = testService.findByFacultyAndCourse(username, courseId, pageNo, pageSize);
						pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
								Long.valueOf(userdetails1.getProgramId()), courseDB.getModuleId(),
								courseDB.getAcadYear(), pageNo, pageSize);

					} else {
						page = testService.searchActiveByExactMatchReplacement(username,
								Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
						pageAdmin = testService.searchTestCreatedByAdminForFaculty(username,
								Long.valueOf(userdetails1.getProgramId()), null, null, pageNo, pageSize);
					}
				}

//				if (courseId != null) {
//					page = testService.findByFacultyAndCourse(username,
//							courseId, pageNo, pageSize);
//				} else {
//					page = testService.searchActiveByExactMatchReplacement(
//							username,
//							Long.parseLong(userdetails1.getProgramId()),
//							pageNo, pageSize);
//					// searchActiveByExactMatch(test, pageNo,
//					// pageSize);
//				}
				List<Test> allTest = new ArrayList<>();
				allTest.addAll(page.getPageItems());
				List<Test> programList = page.getPageItems();
				if (pageAdmin.getPageItems().size() > 0) {
					logger.info(pageAdmin + " found");
					programList.addAll(pageAdmin.getPageItems());
					allTest.addAll(pageAdmin.getPageItems());
					page.setPageItems(allTest);
					//page.setPageItems(pageAdmin.getPageItems());
				}
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}
				return "test/testList";
			} 
			else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

				page = testService.searchActiveByExactMatchReplacementForAdmin(username,
						Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
				// searchActiveByExactMatch(test, pageNo,
				// pageSize);

				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}
				return "test/testList";
			}
			
			else if (userDetails.getAuthorities().contains(Role.ROLE_HOD)) {
				if (courseId != null) {
					page = testService.findByCourse(courseId, pageNo, pageSize);
				} else {
					page = testService.searchActiveByExactMatchReplacementForHOD(
							Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
					// searchActiveByExactMatch(test, pageNo,
					// pageSize);
				}
				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}
				return "test/testList";
			} else if (userDetails.getAuthorities().contains(Role.ROLE_CORD)) {
				page = testService.searchActiveByExactMatchReplacement(username,
						Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
				// searchActiveByExactMatch(test, pageNo,
				// pageSize);
				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}

			} else if (userDetails.getAuthorities().contains(Role.ROLE_AREA_INCHARGE)) {
				page = testService.searchActiveByExactMatchReplacement(username,
						Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
				// searchActiveByExactMatch(test, pageNo,
				// pageSize);
				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}

			} else if (userDetails.getAuthorities().contains(Role.ROLE_AR)) {
				page = testService.searchActiveByExactMatchReplacement(username,
						Long.parseLong(userdetails1.getProgramId()), pageNo, pageSize);
				// searchActiveByExactMatch(test, pageNo,
				// pageSize);
				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}

			}

			else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

				if ("ASMSOC".equals(app)) {

					User user = userService.findByUserName(username);

					String outTimeText = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());

					List<StudentTest> studentTestOfCurrentDate = studentTestService
							.findAllTestOfCurrentDateByStudent(username, outTimeText);

					for (StudentTest st : studentTestOfCurrentDate) {

						setNote(m,
								user.getFirstname() + " " + user.getLastname() + ": Test With Name " + st.getTestName()
										+ " For Subject - " + st.getCourseName() + " is Assigned To You For Today");
					}

				}
				if (courseId != null) {
					page = testService.findTestAllocatedbyCourseId(username, courseId, pageNo, pageSize);

				} else {

					page = testService.findTestAllocated(username, pageNo, pageSize);
				}
				List<StudentTest> sTestList = studentTestService.findByUsername(username);

				m.addAttribute("sTestList", sTestList);
				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page.getPageItems());
				m.addAttribute("q", getQueryString(test));

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}

				/*
				 * m.addAttribute("announcmentList", dashBoardService
				 * .listOfAnnouncementsForCourseList(principal.getName(), courseId));
				 * m.addAttribute("toDoList",
				 * dashBoardService.getToDoList(principal.getName()));
				 */

				return "test/testListNew";
			}

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in getting Test List");
		}
		return "test/testList";
	}

	/*
	 * @Secured("ROLE_USER")
	 * 
	 * @RequestMapping(value = "/testList", method = RequestMethod.GET) public
	 * String viewStudentTestList(@RequestParam(required = false) Long courseId,
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo, Model
	 * m,Principal p){ m.addAttribute("webPage",new WebPage("studentTestList",
	 * "Test List", false, false)); String username=p.getName(); List<Test> tests =
	 * Collections.emptyList(); if(hasRole(Role.ROLE_FACULTY)) { if(null !=
	 * courseId) { tests = testService.findByFacultyAndCourse(username, courseId); }
	 * else { tests = testService.findByFaculty(getUserName()); } } else
	 * if(hasRole(Role.ROLE_STUDENT)) { if(null != courseId) { tests =
	 * testService.findByUserAndCourse(username, courseId); } else { tests =
	 * testService.findByUser(username); } }
	 * 
	 * m.addAttribute("tests", tests); m.addAttribute("rowCount", tests.size());
	 * return "test/testList"; }
	 */

	/**
	 * Questions response
	 */
	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentQuestionResponseForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addStudentQuestionResponseForm(@RequestParam Long studentTestId, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("addStudentQuestionResponse", "Take Test", false, false));
		String username = p.getName();

		// Update the start time and attempts
		StudentTest studentTest = studentTestService.findByID(studentTestId);
		Date dt = Utils.getInIST();
		studentTest.setTestStartTime(dt);
		studentTest.setAttempt(studentTest.getAttempt() + 1);
		studentTest.setLastModifiedBy(username);
		studentTestService.update(studentTest);

		List<TestQuestion> testQuestions = testQuestionService.findByTestId(studentTest.getTestId());
		m.addAttribute("testQuestions", testQuestions);
		return "addStudentQuestionResponse";
	}

	/**
	 * Supports JSON
	 */
	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/addStudentQuestionResponse", method =
	 * RequestMethod.POST) public @ResponseBody StudentQuestionResponse
	 * addStudentQuestionResponse( Principal p, @ModelAttribute Test test, Model m)
	 * { TestQuestion testQuestion = test.getTestQuestions().get(
	 * test.getTestQuestions().size() - 1); logger.info("testQuestion------------->"
	 * + testQuestion);
	 * 
	 * try { StudentQuestionResponse studentQuestionResponse = testQuestion
	 * .getStudentQuestionResponse();
	 * logger.info("student test questions-------------->" +
	 * studentQuestionResponse); studentQuestionResponse.setUsername(p.getName());
	 * studentQuestionResponse.setCreatedBy(p.getName());
	 * studentQuestionResponse.setLastModifiedBy(p.getName());
	 * studentQuestionResponseService.insert(studentQuestionResponse); return
	 * studentQuestionResponse; } catch (Exception e) { setError(m,
	 * "Error in saving question"); return null; }
	 * 
	 * }
	 */

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/addStudentQuestionResponse", method =
	 * RequestMethod.POST) public @ResponseBody StudentQuestionResponse
	 * addStudentQuestionResponse( Principal p, @ModelAttribute Test test, Model m)
	 * { TestQuestion testQuestion = test.getTestQuestions().get(
	 * test.getTestQuestions().size() - 1);
	 * 
	 * try { StudentQuestionResponse studentQuestionResponse = testQuestion
	 * .getStudentQuestionResponse(); TestQuestion tq = testQuestionService
	 * .findByID(studentQuestionResponse.getQuestionId());
	 * 
	 * String cOption = tq.getCorrectOption(); String studentOption =
	 * studentQuestionResponse.getAnswer();
	 * 
	 * if ("MCQ".equals(tq.getQuestionType()) ||
	 * "Image".equals(tq.getQuestionType())) {
	 * 
	 * List<String> splittedCorrectOption = new ArrayList<>(); List<String>
	 * splittedAnswers = new ArrayList<>(); if (tq.getCorrectOption().contains(","))
	 * { splittedCorrectOption = Arrays.asList(cOption.split(","));
	 * 
	 * }
	 * 
	 * if (studentOption.contains(",")) { splittedAnswers =
	 * Arrays.asList(studentOption.split(","));
	 * 
	 * int count = 0; if (splittedCorrectOption.size() > 0) {
	 * 
	 * if (splittedCorrectOption.size() > splittedAnswers .size()) { for (String s :
	 * splittedCorrectOption) {
	 * 
	 * if (splittedAnswers.contains(s)) {
	 * 
	 * } else { count++; } } } else { for (String s : splittedAnswers) {
	 * 
	 * if (splittedCorrectOption.contains(s)) {
	 * 
	 * } else { count++; } } } } else { count++; }
	 * 
	 * if (count > 0) { studentQuestionResponse.setMarks(0.0); } else {
	 * studentQuestionResponse.setMarks(tq.getMarks()); }
	 * 
	 * } else { if (cOption.equals(studentOption)) {
	 * studentQuestionResponse.setMarks(tq.getMarks());
	 * 
	 * } else { studentQuestionResponse.setMarks(0.0); } }
	 * 
	 * } if ("Numeric".equals(tq.getQuestionType())) {
	 * 
	 * // double correctAnswer = Double.valueOf(cOption);
	 * 
	 * if (studentOption.trim().isEmpty()) {
	 * studentQuestionResponse.setAnswer(studentOption);
	 * studentQuestionResponse.setMarks(0.0); } else if
	 * (!ISVALIDINPUT(studentOption)) {
	 * studentQuestionResponse.setAnswer(studentOption);
	 * studentQuestionResponse.setMarks(0.0);
	 * 
	 * } else {
	 * 
	 * double studentAnswer = Double.valueOf(studentOption); double rangeFrom =
	 * Double.valueOf(tq.getAnswerRangeFrom()); double rangeTo =
	 * Double.valueOf(tq.getAnswerRangeTo());
	 * 
	 * if (studentAnswer >= rangeFrom && studentAnswer <= rangeTo) {
	 * studentQuestionResponse.setMarks(tq.getMarks());
	 * 
	 * } else { studentQuestionResponse.setMarks(0.0); } }
	 * 
	 * } studentQuestionResponse.setUsername(p.getName());
	 * studentQuestionResponse.setCreatedBy(p.getName());
	 * studentQuestionResponse.setLastModifiedBy(p.getName());
	 * 
	 * StudentQuestionResponseAudit studentQuestionResponseAudit =
	 * responseBeanToAuditBean( studentQuestionResponse, testQuestion.getTestId());
	 * studentQuestionResponseAudit.setAnswer(studentQuestionResponse .getAnswer());
	 * studentQuestionResponseAudit.setMarks(studentQuestionResponse .getMarks());
	 * 
	 * // studentQuestionResponseService.insert(studentQuestionResponse);
	 * 
	 * studentQuestionResponseService
	 * .insertIntoResponseTemp(studentQuestionResponse);
	 * studentQuestionResponseAuditService .insert(studentQuestionResponseAudit);
	 * return studentQuestionResponse; } catch (Exception e) { setError(m,
	 * "Error in saving question"); return null; }
	 * 
	 * }
	 */

	// New mapping added on 11-10-2019

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentQuestionResponse", method = RequestMethod.POST)
	public @ResponseBody StudentQuestionResponse addStudentQuestionResponse(Principal p, @ModelAttribute Test test,
			Model m) {
		TestQuestion testQuestion = test.getTestQuestions().get(test.getTestQuestions().size() - 1);

		try {
			StudentQuestionResponse studentQuestionResponse = testQuestion.getStudentQuestionResponse();
			TestQuestion tq = testQuestionService.findByID(studentQuestionResponse.getQuestionId());

			String cOption = tq.getCorrectOption();
			String studentOption = studentQuestionResponse.getAnswer();

			if ("MCQ".equals(tq.getQuestionType()) || "Image".equals(tq.getQuestionType())) {

				List<String> splittedCorrectOption = new ArrayList<>();
				List<String> splittedAnswers = new ArrayList<>();
				if (tq.getCorrectOption().contains(",")) {
					splittedCorrectOption = Arrays.asList(cOption.split(","));

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
						studentQuestionResponse.setMarks(0.0);
					} else {
						studentQuestionResponse.setMarks(tq.getMarks());
					}

				} else {
					if (cOption.equals(studentOption)) {
						studentQuestionResponse.setMarks(tq.getMarks());

					} else {
						studentQuestionResponse.setMarks(0.0);
					}
				}

			}
			if ("Numeric".equals(tq.getQuestionType())) {

				// double correctAnswer = Double.valueOf(cOption);
				studentQuestionResponse.setAnswer(studentQuestionResponse.getAnswer().trim()); 
				studentOption = studentQuestionResponse.getAnswer();
				if (studentOption.trim().isEmpty()) {
					studentQuestionResponse.setAnswer(studentOption);
					studentQuestionResponse.setMarks(0.0);
				} else if (!ISVALIDINPUT(studentOption)) {
					studentQuestionResponse.setAnswer(studentOption);
					studentQuestionResponse.setMarks(0.0);

				} else {

					double studentAnswer = Double.valueOf(studentOption);
					double rangeFrom = Double.valueOf(tq.getAnswerRangeFrom());
					double rangeTo = Double.valueOf(tq.getAnswerRangeTo());

					if (studentAnswer >= rangeFrom && studentAnswer <= rangeTo) {
						studentQuestionResponse.setMarks(tq.getMarks());

					} else {
						studentQuestionResponse.setMarks(0.0);
					}
				}

			}
			studentQuestionResponse.setUsername(p.getName());
			studentQuestionResponse.setCreatedBy(p.getName());
			studentQuestionResponse.setLastModifiedBy(p.getName());

			/*
			 * StudentQuestionResponseAudit studentQuestionResponseAudit =
			 * responseBeanToAuditBean( studentQuestionResponse, testQuestion.getTestId());
			 * studentQuestionResponseAudit.setAnswer(studentQuestionResponse .getAnswer());
			 * studentQuestionResponseAudit.setMarks(studentQuestionResponse .getMarks());
			 */

			// studentQuestionResponseService.insert(studentQuestionResponse);

			/*
			 * studentQuestionResponseService
			 * .insertIntoResponseTemp(studentQuestionResponse);
			 * studentQuestionResponseAuditService .insert(studentQuestionResponseAudit);
			 */

			ObjectMapper mapper = new ObjectMapper();
			String studentQResponse = FileUtils
					.readFileToString(new File(studentQuestionResponse.getStudentFilePath()));

			List<StudentQuestionResponse> studentQuestionResponseList = mapper.readValue(studentQResponse,
					new TypeReference<List<StudentQuestionResponse>>() {
					});

			logger.info("array length" + studentQuestionResponseList.size());

			for (StudentQuestionResponse sqr : studentQuestionResponseList) {
				if (sqr.getQuestionId().equals(studentQuestionResponse.getQuestionId())) {

					sqr.setAnswer(studentQuestionResponse.getAnswer());
					sqr.setMarks(studentQuestionResponse.getMarks());
					sqr.setAnswers(studentQuestionResponse.getAnswers());
				}
			}

			mapper.writeValue(new File(studentQuestionResponse.getStudentFilePath()), studentQuestionResponseList);
			return studentQuestionResponse;
		} catch (Exception e) {
			setError(m, "Error in saving question");
			return null;
		}

	}

	public static boolean ISVALIDINPUT(String input) {
		if (input != null) {
			if (input.matches("[0-9.]") || NumberUtils.isNumber(input)

			) {
				return true;
			}
		}
		return false;
	}

	public StudentQuestionResponseAudit responseBeanToAuditBean(StudentQuestionResponse sqr, Long testId) {

		StudentQuestionResponseAudit sqra = new StudentQuestionResponseAudit();
		// StudentTest st = studentTestService.findByID(sqr.getStudentTestId());

		// testId = st.getTestId();
		/*
		 * List<StudentTest> studentTestAuditList = studentTestAuditService
		 * .findByTestIdAndUsername(st.getTestId(), sqr.getUsername());
		 */
		int latestAttempt = sqr.getAttemptNo() + 1;

		sqra.setUsername(sqr.getUsername());
		sqra.setStudentTestId(sqr.getStudentTestId());
		sqra.setQuestionId(sqr.getQuestionId());
		sqra.setAnswer(sqr.getAnswer());
		sqra.setCreatedBy(sqr.getUsername());
		sqra.setLastModifiedBy(sqr.getUsername());
		sqra.setAttempts(String.valueOf(latestAttempt));

		return sqra;
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentQuestionResponseForSubjective", method = RequestMethod.POST)
	public @ResponseBody StudentQuestionResponse addStudentQuestionResponseForSubjective(Principal p,
			@ModelAttribute Test test, Model m) {
		TestQuestion testQuestion = test.getTestQuestions().get(test.getTestQuestions().size() - 1);

		try {
			StudentQuestionResponse studentQuestionResponse = testQuestion.getStudentQuestionResponse();
			TestQuestion tq = testQuestionService.findByID(studentQuestionResponse.getQuestionId());

			// String cOption = tq.getCorrectOption();
			// String studentOption = studentQuestionResponse.getAnswer();
			// logger.info("tq correct option0--" + cOption);
			// logger.info("tq correct option0--" + studentOption);
			/*
			 * if (cOption.equals(studentOption)) {
			 * studentQuestionResponse.setMarks(tq.getMarks());
			 * logger.info("mark for question--" + tq.getMarks()); } else {
			 * studentQuestionResponse.setMarks(0); }
			 */
			studentQuestionResponse.setUsername(p.getName());
			studentQuestionResponse.setCreatedBy(p.getName());
			studentQuestionResponse.setLastModifiedBy(p.getName());

			StudentQuestionResponseAudit studentQuestionResponseAudit = responseBeanToAuditBean(studentQuestionResponse,
					testQuestion.getTestId());
			studentQuestionResponseAudit.setAnswer(studentQuestionResponse.getAnswer());
			studentQuestionResponseAudit.setMarks(studentQuestionResponse.getMarks());

			// studentQuestionResponseService.insert(studentQuestionResponse);
			studentQuestionResponseService.insertIntoResponseTemp(studentQuestionResponse);
			studentQuestionResponseAuditService.insert(studentQuestionResponseAudit);
			return studentQuestionResponse;
		} catch (Exception e) {
			setError(m, "Error in saving question");
			return null;
		}

	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTest", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public String
	 * completeStudentTest(Principal p,
	 * 
	 * @RequestParam String studentTestId, Model m) { ModelAndView mav = new
	 * ModelAndView(); StudentTest postResultTestRecord = null;
	 * m.addAttribute("webPage", new WebPage("studentTest", "Result", false,
	 * false)); StudentTest studentTestDB = studentTestService.findByID(Long
	 * .valueOf(studentTestId)); Test testDetails =
	 * testService.findByID(studentTestDB.getTestId());
	 * 
	 * logger.info("Student Test Id**" + "<-->" + studentTestDB.getTestId() + "<-->"
	 * + testDetails); try { setSuccess(m, "Exam Completed successfully");
	 * studentTestDB.setUsername(p.getName()); studentTestDB.setTestEndTime(new
	 * Date()); studentTestDB.setTestCompleted("Y");
	 * studentTestService.upsert(studentTestDB); logger.info(
	 * "to show the result status is ---------------->>>>>>>>------------>>>>>>---------->>>>>>"
	 * + studentTestService.calculateMarks(studentTestDB));
	 * studentTestService.calculateMarks(studentTestDB); logger.info(
	 * "to show the result status is ---------------->>>>>>>>------------>>>>>>---------->>>>>>"
	 * + testDetails.getShowResultsToStudents()); if
	 * ("Y".equals(testDetails.getShowResultsToStudents())) { postResultTestRecord =
	 * studentTestService .findBytestIDAndUsername(studentTestDB.getTestId(),
	 * p.getName());
	 * 
	 * m.addAttribute("postResultTestRecord", postResultTestRecord);
	 * logger.info("postResultTestRecord" + postResultTestRecord); //
	 * mav.setViewName("test/viewTestResults"); return "test/viewTestResults"; }
	 * else { // mav.setViewName("test/testDetails");
	 * logger.info("Hello---------------------"); return
	 * "test/viewTestCompletionStatus";
	 * 
	 * }
	 * 
	 * // return mav; } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 */

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/completeStudentTest", method = { RequestMethod.POST, RequestMethod.GET })
	public String completeStudentTest(Principal p, @RequestParam String studentTestId, Model m) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		ModelAndView mav = new ModelAndView();
		StudentTest postResultTestRecord = null;
		m.addAttribute("webPage", new WebPage("studentTest", "Result", false, false));
		StudentTest studentTestDB = studentTestService.findByID(Long.valueOf(studentTestId));
		Test testDetails = testService.findByID(studentTestDB.getTestId());

		try {
			setSuccess(m, "Exam Completed successfully");
			studentTestDB.setUsername(p.getName());
			Date dt = Utils.getInIST();
			studentTestDB.setTestEndTime(dt);
			studentTestDB.setTestCompleted("Y");
			studentTestService.upsert(studentTestDB);

			studentTestService.calculateMarks(studentTestDB);

			if ("Y".equals(testDetails.getShowResultsToStudents())) {
				postResultTestRecord = studentTestService.findBytestIDAndUsername(studentTestDB.getTestId(),
						p.getName());

				m.addAttribute("postResultTestRecord", postResultTestRecord);

				// mav.setViewName("test/viewTestResults");
				return "test/viewTestResults";
			} else {
				// mav.setViewName("test/testDetails");

				return "test/viewTestCompletionStatus";

			}

			// return mav;
		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in completing test");
			return null;
		}

	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTestAjax", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public @ResponseBody String
	 * completeStudentTestAjax(Principal p,
	 * 
	 * @RequestParam String studentTestId, Model m) {
	 * 
	 * StudentTest postResultTestRecord = null; Gson g = new Gson(); StudentTest
	 * studentTestDB = studentTestService.findByID(Long .valueOf(studentTestId));
	 * double totalMarks = 0.0; List<StudentQuestionResponse> sqrList =
	 * studentQuestionResponseService .findByStudentTestIdTemp(studentTestId); for
	 * (StudentQuestionResponse sqr : sqrList) { if (sqr.getMarks() != null) {
	 * totalMarks = totalMarks + sqr.getMarks(); } } Test testDetails =
	 * testService.findByID(studentTestDB.getTestId());
	 * 
	 * try {
	 * 
	 * UserRole ur = userRoleService.findRoleByUsername(p.getName());
	 * 
	 * setSuccess(m, "Exam Completed successfully");
	 * studentTestDB.setUsername(p.getName()); Date dt = Utils.getInIST();
	 * 
	 * if (Role.ROLE_FACULTY.equals(ur.getRole())) {
	 * 
	 * studentTestDB.setTestStartTime(null); studentTestDB.setTestEndTime(null); }
	 * else { studentTestDB.setTestEndTime(dt); }
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) {
	 * studentTestDB.setAttempt(studentTestDB.getAttempt() + 1); }
	 * studentTestDB.setTestCompleted("Y");
	 * 
	 * studentTestDB.setScore(totalMarks); if (totalMarks >=
	 * testDetails.getPassScore()) { studentTestDB.setStatus(StudentTest.PASS); }
	 * else { studentTestDB.setStatus(StudentTest.FAIL); }
	 * studentTestService.upsert(studentTestDB);
	 * studentTestAuditService.insertStudentTest(studentTestDB);
	 * studentQuestionResponseService.insertBatch(sqrList);
	 * 
	 * studentQuestionResponseService.deleteResponseTempBatch(sqrList);
	 * 
	 * // studentTestService.calculateMarks(studentTestDB);
	 * 
	 * if ("Y".equals(testDetails.getShowResultsToStudents())) {
	 * postResultTestRecord = studentTestService
	 * .findBytestIDAndUsername(studentTestDB.getTestId(), p.getName());
	 * 
	 * m.addAttribute("postResultTestRecord", postResultTestRecord);
	 * 
	 * // mav.setViewName("test/viewTestResults"); return g.toJson(studentTestDB); }
	 * else { // mav.setViewName("test/testDetails");
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * }
	 * 
	 * // return mav; } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 */

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTestAjax", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public @ResponseBody String
	 * completeStudentTestAjax(Principal p,
	 * 
	 * @RequestParam String studentTestId, Model m) {
	 * 
	 * 
	 * Gson g = new Gson();
	 * 
	 * 
	 * try {
	 * 
	 * StudentTest studentTestDB =
	 * studentTestService.callCompleteStudentTest(studentTestId, "Objective");
	 * 
	 * setSuccess(m, "Exam Completed successfully");
	 * 
	 * if (studentTestDB.getScore() >= studentTestDB.getPassScore()) {
	 * studentTestDB.setStatus(StudentTest.PASS); } else {
	 * studentTestDB.setStatus(StudentTest.FAIL); }
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 */

	// Added on 11-10-2019

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/completeStudentTestAjax", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String completeStudentTestAjax(Principal p, @RequestParam String studentTestId,
			@RequestParam String studentFilePath, Model m) {

		Gson g = new Gson();

		try {

			ObjectMapper mapper = new ObjectMapper();
			studentFilePath = studentFilePath.replaceAll(";", "/");

			String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));
			List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
					new TypeReference<List<StudentQuestionResponse>>() {
					});

			studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

			StudentTest studentTestDB = studentTestService.callCompleteStudentTest(studentTestId, "Objective");
			logger.info("studentTestDB---->"+studentTestDB);
			List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
			for (StudentQuestionResponse sqr : studentQRespList) {
				sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
				StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
				sqrAudit.setMarks(sqr.getMarks());
				sqrAuditList.add(sqrAudit);

			}
			studentQuestionResponseAuditService.insertBatch(sqrAuditList);

			setSuccess(m, "Exam Completed successfully");

			if (studentTestDB.getScore() >= studentTestDB.getPassScore()) {
				studentTestDB.setStatus(StudentTest.PASS);
			} else {
				studentTestDB.setStatus(StudentTest.FAIL);
			}

			return g.toJson(studentTestDB);

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in completing test");
			return null;
		}

	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTestAjaxForSubjective", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public @ResponseBody String
	 * completeStudentTestAjaxForSubjective( Principal p, @RequestParam String
	 * studentTestId, Model m) {
	 * 
	 * 
	 * Gson g = new Gson();
	 * 
	 * try {
	 * 
	 * StudentTest studentTestDB =
	 * studentTestService.callCompleteStudentTest(studentTestId, "Subjective");
	 * 
	 * setSuccess(m, "Exam Completed successfully");
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 */

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/completeStudentTestAjaxForSubjective", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String completeStudentTestAjaxForSubjective(Principal p, @RequestParam String studentTestId,
			@RequestParam String studentFilePath, Model m) {

		Gson g = new Gson();

		try {

			ObjectMapper mapper = new ObjectMapper();
			studentFilePath = studentFilePath.replaceAll(";", "/");

			String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));
			List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
					new TypeReference<List<StudentQuestionResponse>>() {
					});

			studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

			StudentTest studentTestDB = studentTestService.callCompleteStudentTest(studentTestId, "Subjective");

			List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
			for (StudentQuestionResponse sqr : studentQRespList) {
				sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
				StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBeanForSubjectiveEvaluation(sqr,
						studentTestDB.getTestId());
				sqrAuditList.add(sqrAudit);

			}
			studentQuestionResponseAuditService.insertBatch(sqrAuditList);

			setSuccess(m, "Exam Completed successfully");

			return g.toJson(studentTestDB);

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in completing test");
			return null;
		}

	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTestForMixAjax", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public @ResponseBody String
	 * completeStudentTestForMixAjax(Principal p,
	 * 
	 * @RequestParam String studentTestId, Model m) {
	 * 
	 * Gson g = new Gson();
	 * 
	 * try {
	 * 
	 * StudentTest studentTestDB = studentTestService
	 * .callCompleteStudentTest(studentTestId, "Mix");
	 * 
	 * setSuccess(m, "Exam Completed successfully");
	 * 
	 * if (studentTestDB.getScore() >= studentTestDB.getPassScore()) {
	 * studentTestDB.setStatus(StudentTest.PASS); } else {
	 * studentTestDB.setStatus(StudentTest.FAIL); }
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 */

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/completeStudentTestForMixAjax", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String completeStudentTestForMixAjax(Principal p, @RequestParam String studentTestId,
			@RequestParam String studentFilePath, Model m) {

		Gson g = new Gson();

		try {

			ObjectMapper mapper = new ObjectMapper();
			studentFilePath = studentFilePath.replaceAll(";", "/");

			String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));
			List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
					new TypeReference<List<StudentQuestionResponse>>() {
					});

			studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

			StudentTest studentTestDB = studentTestService.callCompleteStudentTest(studentTestId, "Mix");

			List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
			for (StudentQuestionResponse sqr : studentQRespList) {
				sqr.setAttemptNo(studentTestDB.getAttempt() - 1);
				StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());

				sqrAudit.setMarks(sqr.getMarks());
				sqrAuditList.add(sqrAudit);

			}
			studentQuestionResponseAuditService.insertBatch(sqrAuditList);

			setSuccess(m, "Exam Completed successfully");

			/*
			 * if (studentTestDB.getScore() >= studentTestDB.getPassScore()) {
			 * studentTestDB.setStatus(StudentTest.PASS); } else {
			 * studentTestDB.setStatus(StudentTest.FAIL); }
			 */

			return g.toJson(studentTestDB);

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in completing test");
			return null;
		}

	}

	/*
	 * @Secured("ROLE_USER")
	 * 
	 * @RequestMapping(value = "/getTestSummeryAjax", method = { RequestMethod.POST,
	 * RequestMethod.GET }) public @ResponseBody String getTestSummeryAjax(Principal
	 * p,
	 * 
	 * @RequestParam String studentTestId, @RequestParam String testId, Model m) {
	 * 
	 * StudentTest postResultTestRecord = null; Gson g = new Gson();
	 * 
	 * StudentTest studentTestDB = studentTestService.findByID(Long
	 * .valueOf(studentTestId));
	 * 
	 * 
	 * StudentTest studentTestDB = new StudentTest();
	 * 
	 * StudentQuestionResponse sqr = studentQuestionResponseService
	 * .findTotalAnsweredTempQueByStudentTestId(studentTestId); studentTestDB
	 * .setTotalQuestionAttempted(sqr.getTotalQuestionAttempted());
	 * 
	 * Test testDetails = testService.findByID(Long.valueOf(testId));
	 * 
	 * if ("N".equals(testDetails.getRandomQuestion())) { List<TestQuestion>
	 * testQuestionList = testQuestionService .findByTestId(testDetails.getId());
	 * studentTestDB.setTotalQuestions(testQuestionList.size()); } else if
	 * ("Y".equals(testDetails.getRandomQuestion())) {
	 * studentTestDB.setTotalQuestions(Integer.parseInt(testDetails
	 * .getMaxQuestnToShow())); }
	 * 
	 * try {
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in getting test summery"); return null; }
	 * 
	 * }
	 */

	// New Mapping added on 11-10-2019

	@Secured("ROLE_USER")
	@RequestMapping(value = "/getTestSummeryAjax", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String getTestSummeryAjax(Principal p, @RequestParam String studentTestId,
			@RequestParam String testId, @RequestParam String studentFilePath, Model m) {

		StudentTest postResultTestRecord = null;
		Gson g = new Gson();
		/*
		 * StudentTest studentTestDB = studentTestService.findByID(Long
		 * .valueOf(studentTestId));
		 */

		StudentTest studentTestDB = new StudentTest();
		studentFilePath = studentFilePath.replaceAll(";", "/");
		try {
			ObjectMapper mapper = new ObjectMapper();

			String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));

			List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
					new TypeReference<List<StudentQuestionResponse>>() {
					});
			int noOfAttemptedQuestns = 0;
			for (StudentQuestionResponse sqr : studentQRespList) {

				if (sqr.getAnswer() != null || sqr.getAnswers() != null) {
					noOfAttemptedQuestns++;
				}
			}
			studentTestDB.setTotalQuestionAttempted(noOfAttemptedQuestns);

			Test testDetails = testService.findByID(Long.valueOf(testId));

			if ("N".equals(testDetails.getRandomQuestion())) {
				List<TestQuestion> testQuestionList = testQuestionService.findByTestId(testDetails.getId());
				studentTestDB.setTotalQuestions(testQuestionList.size());
			} else if ("Y".equals(testDetails.getRandomQuestion())) {
				studentTestDB.setTotalQuestions(Integer.parseInt(testDetails.getMaxQuestnToShow()));
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		try {

			return g.toJson(studentTestDB);

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in getting test summery");
			return null;
		}

	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTestAjaxForSubjective", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public @ResponseBody String
	 * completeStudentTestAjaxForSubjective( Principal p, @RequestParam String
	 * studentTestId, Model m) {
	 * 
	 * StudentTest postResultTestRecord = null; Gson g = new Gson(); StudentTest
	 * studentTestDB = studentTestService.findByID(Long .valueOf(studentTestId));
	 * 
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestIdTemp(studentTestId);
	 * 
	 * // int totalMarks = 0;
	 * 
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(studentTestId); for (StudentQuestionResponse sqr :
	 * sqrList) { if (sqr.getMarks() != null) { totalMarks = totalMarks +
	 * sqr.getMarks(); } }
	 * 
	 * Test testDetails = testService.findByID(studentTestDB.getTestId());
	 * 
	 * try {
	 * 
	 * UserRole ur = userRoleService.findRoleByUsername(p.getName());
	 * 
	 * setSuccess(m, "Exam Completed successfully");
	 * studentTestDB.setUsername(p.getName()); Date dt = Utils.getInIST();
	 * 
	 * if (Role.ROLE_FACULTY.equals(ur.getRole())) {
	 * 
	 * studentTestDB.setTestStartTime(null); studentTestDB.setTestEndTime(null); }
	 * else { studentTestDB.setTestEndTime(dt); }
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) {
	 * studentTestDB.setAttempt(studentTestDB.getAttempt() + 1); }
	 * studentTestDB.setTestCompleted("Y");
	 * 
	 * 
	 * studentTestDB.setScore(totalMarks); if (totalMarks >=
	 * testDetails.getPassScore()) { studentTestDB.setStatus(StudentTest.PASS); }
	 * else { studentTestDB.setStatus(StudentTest.FAIL); }
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * studentTestAuditService.insertStudentTest(studentTestDB);
	 * 
	 * studentQuestionResponseService.insertBatch(sqrList);
	 * studentQuestionResponseService.deleteResponseTempBatch(sqrList); //
	 * studentTestService.calculateMarks(studentTestDB);
	 * 
	 * 
	 * if ("Y".equals(testDetails.getShowResultsToStudents())) {
	 * postResultTestRecord = studentTestService
	 * .findBytestIDAndUsername(studentTestDB.getTestId(), p.getName());
	 * 
	 * m.addAttribute("postResultTestRecord", postResultTestRecord);
	 * logger.info("postResultTestRecord" + postResultTestRecord); //
	 * mav.setViewName("test/viewTestResults"); return g.toJson(studentTestDB); }
	 * else { // mav.setViewName("test/testDetails");
	 * logger.info("N---------------------"); return g.toJson(studentTestDB);
	 * 
	 * }
	 * 
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * // return mav; } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 * 
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/completeStudentTestForMixAjax", method = {
	 * RequestMethod.POST, RequestMethod.GET }) public @ResponseBody String
	 * completeStudentTestForMixAjax(Principal p,
	 * 
	 * @RequestParam String studentTestId, Model m) {
	 * 
	 * StudentTest postResultTestRecord = null; Gson g = new Gson(); StudentTest
	 * studentTestDB = studentTestService.findByID(Long .valueOf(studentTestId));
	 * double totalMarks = 0.0; List<StudentQuestionResponse> sqrList =
	 * studentQuestionResponseService .findByStudentTestIdTemp(studentTestId); for
	 * (StudentQuestionResponse sqr : sqrList) { if (sqr.getMarks() != null) {
	 * totalMarks = totalMarks + sqr.getMarks(); } } Test testDetails =
	 * testService.findByID(studentTestDB.getTestId());
	 * 
	 * try { setSuccess(m, "Exam Completed successfully");
	 * studentTestDB.setUsername(p.getName()); Date dt = Utils.getInIST();
	 * studentTestDB.setTestEndTime(dt);
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) {
	 * studentTestDB.setAttempt(studentTestDB.getAttempt() + 1); }
	 * studentTestDB.setTestCompleted("Y"); studentTestDB.setScore(totalMarks); if
	 * (totalMarks >= testDetails.getPassScore()) {
	 * studentTestDB.setStatus(StudentTest.PASS); } else {
	 * studentTestDB.setStatus(StudentTest.FAIL); }
	 * studentTestService.upsert(studentTestDB);
	 * studentTestAuditService.insertStudentTest(studentTestDB);
	 * 
	 * studentQuestionResponseService.insertBatch(sqrList);
	 * 
	 * studentQuestionResponseService.deleteResponseTempBatch(sqrList); //
	 * studentTestService.calculateMarks(studentTestDB);
	 * 
	 * 
	 * if ("Y".equals(testDetails.getShowResultsToStudents())) {
	 * postResultTestRecord = studentTestService
	 * .findBytestIDAndUsername(studentTestDB.getTestId(), p.getName());
	 * 
	 * m.addAttribute("postResultTestRecord", postResultTestRecord);
	 * logger.info("postResultTestRecord" + postResultTestRecord); //
	 * mav.setViewName("test/viewTestResults"); return g.toJson(studentTestDB); }
	 * else { // mav.setViewName("test/testDetails");
	 * logger.info("N---------------------"); return g.toJson(studentTestDB);
	 * 
	 * }
	 * 
	 * 
	 * return g.toJson(studentTestDB);
	 * 
	 * // return mav; } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Error in completing test"); return null; }
	 * 
	 * }
	 */

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/configureQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public String configureQuestions(@RequestParam(required = false, defaultValue = "1") int pageNo,
			@RequestParam(required = false) Long courseId, Model m, @ModelAttribute Test test, Principal p) {

		m.addAttribute("webPage", new WebPage("testList", "View Tests", false, false));
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		test.setFacultyId(username);
		try {
			Page<Test> page;
			if (courseId != null) {
				page = testService.findByFacultyAndCourse(username, courseId, pageNo, pageSize);
			} else {
				page = testService.searchActiveByExactMatch(test, pageNo, pageSize);
			}

			List<Test> programList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(test));

			if (programList == null || programList.size() == 0) {
				setNote(m, "No Tests found");
			}

		} catch (Exception e) {
			logger.error("Error", e);

			setError(m, "Error in getting Test List");
		}
		return "test/configureQuestions";
	}

	@RequestMapping(value = "/viewThisTest", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewThisTest(Model m, @ModelAttribute StudentTest test, Long testId, Principal principal) {
		m.addAttribute("webPage", new WebPage("testList", "View Tests", true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<StudentTest> testList = studentTestService.findOneTest(testId);
		for (StudentTest st : testList) {
			Course c = courseService.findByID(Long.valueOf(st.getCourseId()));
			st.setCourseName(c.getCourseName());
			testList.set(testList.indexOf(st), st);
		}

		m.addAttribute("testList", testList);
		return "test/viewTest";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/supportAdminTestList", method = { RequestMethod.GET, RequestMethod.POST })
	public String supportAdminTestList(@RequestParam(required = false, defaultValue = "1") int pageNo, Model m,

			@RequestParam(required = false) Long courseId, @RequestParam(required = false) String username,
			Principal principal, HttpServletRequest request) {

		/* String username = principal.getName(); */

		/*
		 * Token userdetails1 = (Token) principal; String ProgramName =
		 * userdetails1.getProgramName();
		 */
		User u = userService.findByUserName(username);

		/*
		 * String acadSession = u.getAcadSession();
		 * 
		 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
		 * acadSession);
		 */
		Test test = new Test();

		m.addAttribute("webPage", new WebPage("testList", "View Tests", true, false));
		/*
		 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
		 * test.setFacultyId(username); }
		 */
		m.addAttribute("username", username);
		UserRole userDetails = userRoleService.findRoleByUsername(username);
		try {
			Page<Test> page;

			if (userDetails.getRole().equals(Role.ROLE_FACULTY)) {
				if (courseId != null) {
					page = testService.findByFacultyAndCourse(username, courseId, pageNo, pageSize);
				} else {
					page = null;
				}
				List<Test> programList = page.getPageItems();

				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(test));
				m.addAttribute("username", username);

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}
				return "test/supportAdminTestList";
			} else if (userDetails.getRole().equals(Role.ROLE_STUDENT)) {
				if (courseId != null) {
					page = testService.findTestAllocatedbyCourseId(username, courseId, pageNo, pageSize);

				} else {

					page = testService.findTestAllocated(username, pageNo, pageSize);
				}
				List<StudentTest> sTestList = studentTestService.findByUsername(username);

				m.addAttribute("sTestList", sTestList);
				List<Test> programList = page.getPageItems();
				for (Test t : programList) {
					Course c = courseService.findByID(t.getCourseId());
					t.setCourseName(c.getCourseName());
					programList.set(programList.indexOf(t), t);

				}
				Course course = courseService.findByID(courseId);
				m.addAttribute("courseRecord", course);
				m.addAttribute("courseId", courseId);

				m.addAttribute("page", page.getPageItems());
				m.addAttribute("q", getQueryString(test));
				m.addAttribute("username", username);

				if (programList == null || programList.size() == 0) {
					setNote(m, "No Tests found");
				}

				/*
				 * m.addAttribute("announcmentList", dashBoardService
				 * .listOfAnnouncementsForCourseList(principal.getName(), courseId));
				 * m.addAttribute("toDoList",
				 * dashBoardService.getToDoList(principal.getName()));
				 */

				return "test/supportAdminTestListNew";
			}

		} catch (Exception e) {
			logger.error("Error", e);
			setError(m, "Error in getting Test List");
		}
		return "test/supportAdminTestList";
	}

	public List<TestQuestion> removeSpecialCharactersFromBean(List<TestQuestion> testQuestions) {

		for (TestQuestion tq : testQuestions) {

			/*
			 * if (!tq.getDescription().contains("<img")) { Document doc =
			 * Jsoup.parse(tq.getDescription()); String text = doc.body().text();
			 * tq.setDescription(text); }
			 * 
			 * if (tq.getDescription() != null) {
			 * 
			 * if (tq.getDescription().contains("<") &&
			 * !tq.getDescription().contains("<img")) {
			 * 
			 * tq.setDescription(tq.getDescription().replace("<", " < ")); } }
			 */
			if (tq.getOption1() != null) {
				if (tq.getOption1().contains("<")) {

					tq.setOption1(tq.getOption1().replace("<", " < "));
				}
			}
			if (tq.getOption2() != null) {
				if (tq.getOption2().contains("<")) {

					tq.setOption2(tq.getOption2().replace("<", " < "));
				}
			}
			if (tq.getOption3() != null) {
				if (tq.getOption3().contains("<")) {

					tq.setOption3(tq.getOption3().replace("<", " < "));
				}
			}
			if (tq.getOption4() != null) {
				if (tq.getOption4().contains("<")) {

					tq.setOption4(tq.getOption4().replace("<", " < "));
				}
			}
			if (tq.getOption5() != null) {
				if (tq.getOption5().contains("<")) {

					tq.setOption5(tq.getOption5().replace("<", " < "));
				}
			}
			if (tq.getOption6() != null) {
				if (tq.getOption6().contains("<")) {

					tq.setOption6(tq.getOption6().replace("<", " < "));
				}
			}
			if (tq.getOption7() != null) {
				if (tq.getOption7().contains("<")) {

					tq.setOption7(tq.getOption7().replace("<", " < "));
				}
			}
			if (tq.getOption8() != null) {
				if (tq.getOption8().contains("<")) {

					tq.setOption8(tq.getOption8().replace("<", " < "));
				}
			}
		}

		return testQuestions;
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/updateStudentsAttemptedDuration", method = { RequestMethod.POST, RequestMethod.GET })
	public String updateStudentsAttemptedDuration(Principal p, @RequestParam String durationInMinute,
			@RequestParam String studentTestId, Model m) {

		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		ModelAndView mav = new ModelAndView();
		try {
			studentTestService.updateStudentTestDuration(studentTestId, durationInMinute);

			return "SUCCESS";
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "ERROR";
		}
	}

	// New student-test Modal Mapping created on 24-05-2019

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTestNew", method = RequestMethod.GET)
	 * public String startStudentTestNew(Principal p, RedirectAttributes
	 * redirectAttrs, @ModelAttribute Test test, Model m,
	 * 
	 * @RequestParam("id") String testId) throws ParseException {
	 * m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
	 * Test testDb = testService.findByID(Long.valueOf(testId)); Token userdetails1
	 * = (Token) p; UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; String username = p.getName();
	 * String ProgramName = userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(
	 * Long.valueOf(testId), p.getName()); if (null == studentTestDB) {
	 * setNote(redirectAttrs, "You have not been assigned the test"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } // if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (testDb
	 * .getMaxAttempt() > studentTestDB.getAttempt())) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { String
	 * allowAfterEndDate = testDb.getAllowAfterEndDate();
	 * 
	 * int chkStartandEndDateOfTest = studentTestService
	 * .chkStartandEndDateOfTests(p.getName(), Long.valueOf(testId)); if
	 * (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate) &&
	 * userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
	 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } else {
	 * 
	 * m.addAttribute("action", "add"); Date startDateTime = null;
	 * 
	 * Date startTestTime = studentTestDB.getTestStartTime(); Date completeAt =
	 * Utils.getCompletionTimeDateType( studentTestDB.getTestStartTime(),
	 * testDb.getDuration());
	 * 
	 * if (startTestTime != null) {
	 * 
	 * if ((completeAt.compareTo(Utils.getInIST())) > 0 &&
	 * studentTestDB.getTestCompleted() == null) { startDateTime =
	 * studentTestDB.getTestStartTime();
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * // Date startDateTime = Utils.getInIST(); String startTestTimeFormat = Utils
	 * .changeTestStartTimeFormat(startDateTime); m.addAttribute("dateTime",
	 * startTestTimeFormat);
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getDurationCompleted() != null) { if
	 * (Integer.parseInt(studentTestDB .getDurationCompleted()) >= testDb
	 * .getDuration()) { testDb.setDuration(1); } else {
	 * testDb.setDuration(testDb.getDuration() - Integer.parseInt(studentTestDB
	 * .getDurationCompleted())); } } } else {
	 * studentTestDB.setDurationCompleted("0"); } m.addAttribute("testTime",
	 * testDb.getDuration() * 60);
	 * 
	 * studentTestDB.setTestStartTime(startDateTime);
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * // test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration
	 * ()); String completionTime = ""; int durationCompletedByStudent = 0;
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * 
	 * durationCompletedByStudent = Integer.parseInt(studentTestDB
	 * .getDurationCompleted()); }
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { completionTime =
	 * Utils.getCompletionTime(startDateTime, testDb.getDuration()); } else {
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * durationCompletedByStudent = Integer
	 * .parseInt(studentTestDB.getDurationCompleted()); int durationForTest =
	 * testDb.getDuration(); int remainingDurationForStudent = durationForTest -
	 * durationCompletedByStudent;
	 * 
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * remainingDurationForStudent); } else { // durationCompletedByStudent = 0;
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * testDb.getDuration()); }
	 * 
	 * }
	 * 
	 * 
	 * m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);
	 * 
	 * m.addAttribute("durationsLeft", completionTime);
	 * 
	 * if (studentTestDB.getScore() != null) { m.addAttribute("score",
	 * studentTestDB.getScore()); if (testDb.getPassScore() <
	 * studentTestDB.getScore()) { String status = "PASS"; m.addAttribute("status",
	 * status); } else { String status = "FAIL"; m.addAttribute("status", status); }
	 * } if ("Y".equals(studentTestDB.getTestCompleted())) {
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId())); if
	 * (sqrList.size() > 0) { for (StudentQuestionResponse sqr : sqrList) {
	 * sqr.setMarks(0.0); sqr.setAnswer(" ");
	 * studentQuestionResponseService.delete(sqr); } }
	 * 
	 * studentTestDB.setTestCompleted(null);
	 * studentTestService.upsert(studentTestDB); } else {
	 * studentTestService.upsert(studentTestDB); }
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB);
	 * 
	 * List<TestQuestion> testQuestions = testQuestionService
	 * .findByTestId(testDB.getId());
	 * 
	 * List<TestQuestion> testQuestions = new ArrayList<>();
	 * List<StudentQuestionResponse> studentQuestnResponseList =
	 * studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId()));
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername( testDB.getId(),
	 * username); } else { if (studentQuestnResponseList.size() > 0) { testQuestions
	 * = testQuestionService
	 * .findStudentResponseByTestIdAndUsernameForIncompleteTestTemp( testDB.getId(),
	 * username); } else { testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsername( testDB.getId(), username); } } //
	 * .findStudentResponseByTestIdAndUsername(testDB.getId(), // p.getName());
	 * 
	 * testQuestions = removeSpecialCharactersFromBean(testQuestions);
	 * 
	 * List<TestQuestion> randomTestQuestnList = new ArrayList<>(); if
	 * ("Y".equals(testDB.getRandomQuestion())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * if ("Y".equals(testDB.getSameMarksQue())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * } else if ("N".equals(testDB.getSameMarksQue())) {
	 * 
	 * List<TestConfiguration> testConfigList = testConfigurationService
	 * .findAllByTestId(Long.parseLong(testId));
	 * 
	 * for (TestConfiguration tc : testConfigList) {
	 * 
	 * List<TestQuestion> testQuestionRandomList = testQuestions .stream() .filter(o
	 * -> o.getMarks().equals( tc.getMarks())) .collect(Collectors.toList());
	 * 
	 * testQuestionRandomList =
	 * removeSpecialCharactersFromBean(testQuestionRandomList); for (int i = 0; i <
	 * tc.getNoOfQuestion(); i++) { Random rand = new Random(); TestQuestion
	 * testQuestionRandom = testQuestionRandomList .get(rand
	 * .nextInt(testQuestionRandomList .size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else { randomTestQuestnList
	 * .add(testQuestionRandom); } } }
	 * 
	 * } } else { randomTestQuestnList.addAll(testQuestions); }
	 * test.setTestQuestions(randomTestQuestnList);
	 * 
	 * if (studentQuestnResponseList.size() == 0) { List<StudentQuestionResponse>
	 * sqrList = new ArrayList<>(); List<StudentQuestionResponseAudit> sqrAuditList
	 * = new ArrayList<>(); for (TestQuestion tq : randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(username); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(username);
	 * sqr.setCreatedBy(username);
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); sqrList.add(sqr); sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseService.insertBatch(sqrList);
	 * studentQuestionResponseService.insertBatchIntoTemp(sqrList);
	 * studentQuestionResponseAuditService .insertBatch(sqrAuditList); }
	 * 
	 * test.setStudentTest(studentTestDB);
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted()) if
	 * ("Y".equals(testDB.getShowResultsToStudents())) {
	 * 
	 * setNote(redirectAttrs, "Test completed. You scored " +
	 * (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); } else {
	 * 
	 * setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");
	 * 
	 * }
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "test/studentTest"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } }
	 * setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } return
	 * "test/studentTestUpdated"; }
	 */

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTestUpdatedForSubjective", method =
	 * RequestMethod.GET) public String
	 * startStudentTestUpdatedForSubjective(Principal p, RedirectAttributes
	 * redirectAttrs, @ModelAttribute Test test, Model m, @RequestParam("id") String
	 * testId) throws ParseException { m.addAttribute("webPage", new
	 * WebPage("studentTest", "Test", true, false));
	 * 
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; Test testDb =
	 * testService.findByID(Long.valueOf(testId)); StudentTest studentTestDB =
	 * studentTestService.findBytestIDAndUsername( Long.valueOf(testId),
	 * p.getName()); if (null == studentTestDB) { setNote(redirectAttrs,
	 * "You have not been assigned the test"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } // if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (testDb
	 * .getMaxAttempt() > studentTestDB.getAttempt())) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { String
	 * allowAfterEndDate = testDb.getAllowAfterEndDate(); // he is valid user to
	 * take test remain no of attempt int chkStartandEndDateOfTest =
	 * studentTestService .chkStartandEndDateOfTests(p.getName(),
	 * Long.valueOf(testId)); if (chkStartandEndDateOfTest == 0 &&
	 * "N".equals(allowAfterEndDate) &&
	 * userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
	 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } else {
	 * 
	 * m.addAttribute("action", "add");
	 * 
	 * Date startDateTime = null;
	 * 
	 * Date startTestTime = studentTestDB.getTestStartTime(); Date completeAt =
	 * Utils.getCompletionTimeDateType( studentTestDB.getTestStartTime(),
	 * testDb.getDuration());
	 * 
	 * if (startTestTime != null) {
	 * 
	 * if ((completeAt.compareTo(Utils.getInIST())) > 0 &&
	 * studentTestDB.getTestCompleted() == null) { startDateTime =
	 * studentTestDB.getTestStartTime();
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * // Date startDateTime = Utils.getInIST();
	 * 
	 * studentTestDB.setTestStartTime(startDateTime);
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * // test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration
	 * ()); String completionTime = ""; if
	 * (!"Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getDurationCompleted() != null) { if
	 * (Integer.parseInt(studentTestDB .getDurationCompleted()) >= testDb
	 * .getDuration()) { testDb.setDuration(1); } else {
	 * testDb.setDuration(testDb.getDuration() - Integer.parseInt(studentTestDB
	 * .getDurationCompleted())); } } } else {
	 * studentTestDB.setDurationCompleted("0"); } m.addAttribute("testTime",
	 * testDb.getDuration() * 60);
	 * 
	 * int durationCompletedByStudent = 0;
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * 
	 * durationCompletedByStudent = Integer.parseInt(studentTestDB
	 * .getDurationCompleted()); }
	 * 
	 * m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);
	 * 
	 * String startTestTimeFormat = Utils .changeTestStartTimeFormat(startDateTime);
	 * 
	 * m.addAttribute("dateTime", startTestTimeFormat);
	 * m.addAttribute("durationsLeft", completionTime);
	 * 
	 * if (studentTestDB.getScore() != null) { m.addAttribute("score",
	 * studentTestDB.getScore()); if (testDb.getPassScore() <
	 * studentTestDB.getScore()) { String status = "PASS"; m.addAttribute("status",
	 * status); } else { String status = "FAIL"; m.addAttribute("status", status); }
	 * }
	 * 
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId())); if
	 * (sqrList.size() > 0) { for (StudentQuestionResponse sqr : sqrList) {
	 * sqr.setMarks(0.0); sqr.setAnswer(" ");
	 * studentQuestionResponseService.delete(sqr); } }
	 * 
	 * studentTestDB.setTestCompleted(null);
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * new ArrayList<>(); List<StudentQuestionResponse> studentQuestnResponseList =
	 * studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId()));
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername( testDB.getId(),
	 * p.getName()); } else { if (studentQuestnResponseList.size() > 0) {
	 * testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsernameForIncompleteTestTemp( testDB.getId(),
	 * p.getName()); } else { testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsername( testDB.getId(), p.getName()); } } //
	 * .findStudentResponseByTestIdAndUsername(testDB.getId(), // p.getName());
	 * testQuestions = removeSpecialCharactersFromBean(testQuestions);
	 * List<TestQuestion> randomTestQuestnList = new ArrayList<>(); if
	 * ("Y".equals(testDB.getRandomQuestion())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * 
	 * if ("Y".equals(testDB.getSameMarksQue())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * } else if ("N".equals(testDB.getSameMarksQue())) {
	 * 
	 * List<TestConfiguration> testConfigList = testConfigurationService
	 * .findAllByTestId(Long.parseLong(testId));
	 * 
	 * for (TestConfiguration tc : testConfigList) {
	 * 
	 * List<TestQuestion> testQuestionRandomList = testQuestions .stream() .filter(o
	 * -> o.getMarks().equals( tc.getMarks())) .collect(Collectors.toList());
	 * testQuestionRandomList =
	 * removeSpecialCharactersFromBean(testQuestionRandomList); for (int i = 0; i <
	 * tc.getNoOfQuestion(); i++) { Random rand = new Random(); TestQuestion
	 * testQuestionRandom = testQuestionRandomList .get(rand
	 * .nextInt(testQuestionRandomList .size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else { randomTestQuestnList
	 * .add(testQuestionRandom); } } }
	 * 
	 * } } else { randomTestQuestnList.addAll(testQuestions); }
	 * test.setTestQuestions(randomTestQuestnList);
	 * 
	 * if (studentQuestnResponseList.size() == 0) { List<StudentQuestionResponse>
	 * squestnResponseList = new ArrayList<>(); List<StudentQuestionResponseAudit>
	 * sqrAuditList = new ArrayList<>(); for (TestQuestion tq :
	 * randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(p.getName()); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(p.getName());
	 * sqr.setCreatedBy(p.getName());
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); squestnResponseList.add(sqr);
	 * 
	 * sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseService .insertBatch(squestnResponseList);
	 * studentQuestionResponseService.insertBatchIntoTemp(sqrList);
	 * studentQuestionResponseAuditService .insertBatch(sqrAuditList); }
	 * 
	 * // test.setTestQuestions(testQuestions);
	 * 
	 * test.setStudentTest(studentTestDB);
	 * 
	 * if (studentTestDB.isCompleted()) setSuccess(m, "Test completed. You scored "
	 * + (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); else
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted())
	 * setSuccess( m, "Test completed. You scored " + (studentTestDB.getScore() ==
	 * null ? 0 : studentTestDB.getScore()) + " marks out of " +
	 * test.getMaxScore());
	 * 
	 * else setSuccess(m, "Test has started");
	 * 
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "test/studentTest"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } }
	 * 
	 * setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } return
	 * "test/studentTestUpdatedForSubjective"; }
	 */

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTestUpdatedForMix", method =
	 * RequestMethod.GET) public String startStudentTestUpdatedForMix(Principal p,
	 * RedirectAttributes redirectAttrs, @ModelAttribute Test test, Model m,
	 * 
	 * @RequestParam("id") String testId) throws ParseException {
	 * m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
	 * Test testDb = testService.findByID(Long.valueOf(testId)); Token userdetails1
	 * = (Token) p; UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; String username = p.getName();
	 * String ProgramName = userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(
	 * Long.valueOf(testId), p.getName()); if (null == studentTestDB) {
	 * setNote(redirectAttrs, "You have not been assigned the test"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } // if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (testDb
	 * .getMaxAttempt() > studentTestDB.getAttempt())) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { String
	 * allowAfterEndDate = testDb.getAllowAfterEndDate();
	 * 
	 * int chkStartandEndDateOfTest = studentTestService
	 * .chkStartandEndDateOfTests(p.getName(), Long.valueOf(testId)); if
	 * (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate) &&
	 * userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
	 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } else {
	 * 
	 * m.addAttribute("action", "add"); Date startDateTime = null;
	 * 
	 * Date startTestTime = studentTestDB.getTestStartTime(); Date completeAt =
	 * Utils.getCompletionTimeDateType( studentTestDB.getTestStartTime(),
	 * testDb.getDuration());
	 * 
	 * if (startTestTime != null) {
	 * 
	 * if ((completeAt.compareTo(Utils.getInIST())) > 0 &&
	 * studentTestDB.getTestCompleted() == null) { startDateTime =
	 * studentTestDB.getTestStartTime();
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * // Date startDateTime = Utils.getInIST(); String startTestTimeFormat = Utils
	 * .changeTestStartTimeFormat(startDateTime); m.addAttribute("dateTime",
	 * startTestTimeFormat);
	 * 
	 * studentTestDB.setTestStartTime(startDateTime);
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * // test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration
	 * ()); String completionTime = "";
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getDurationCompleted() != null) { if
	 * (Integer.parseInt(studentTestDB .getDurationCompleted()) >= testDb
	 * .getDuration()) { testDb.setDuration(1); } else {
	 * testDb.setDuration(testDb.getDuration() - Integer.parseInt(studentTestDB
	 * .getDurationCompleted())); } } } else {
	 * studentTestDB.setDurationCompleted("0"); }
	 * 
	 * m.addAttribute("testTime", testDb.getDuration() * 60);
	 * 
	 * int durationCompletedByStudent = 0; if
	 * ("Y".equals(studentTestDB.getTestCompleted())) { completionTime =
	 * Utils.getCompletionTime(startDateTime, testDb.getDuration()); } else {
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * durationCompletedByStudent = Integer
	 * .parseInt(studentTestDB.getDurationCompleted()); int durationForTest =
	 * testDb.getDuration(); int remainingDurationForStudent = durationForTest -
	 * durationCompletedByStudent;
	 * 
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * remainingDurationForStudent); } else { // durationCompletedByStudent = 0;
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * testDb.getDuration()); }
	 * 
	 * }
	 * 
	 * m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);
	 * 
	 * m.addAttribute("durationsLeft", completionTime);
	 * 
	 * if (studentTestDB.getScore() != null) { m.addAttribute("score",
	 * studentTestDB.getScore()); if (testDb.getPassScore() <
	 * studentTestDB.getScore()) { String status = "PASS"; m.addAttribute("status",
	 * status); } else { String status = "FAIL"; m.addAttribute("status", status); }
	 * } if ("Y".equals(studentTestDB.getTestCompleted())) {
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId())); if
	 * (sqrList.size() > 0) { for (StudentQuestionResponse sqr : sqrList) {
	 * sqr.setMarks(0.0); sqr.setAnswer(" ");
	 * studentQuestionResponseService.delete(sqr); } }
	 * 
	 * studentTestDB.setTestCompleted(null);
	 * studentTestService.upsert(studentTestDB); } else {
	 * studentTestService.upsert(studentTestDB); }
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB);
	 * 
	 * List<TestQuestion> testQuestions = testQuestionService
	 * .findByTestId(testDB.getId());
	 * 
	 * List<TestQuestion> testQuestions = new ArrayList<>();
	 * List<StudentQuestionResponse> studentQuestnResponseList =
	 * studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId()));
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername( testDB.getId(),
	 * username); } else { if (studentQuestnResponseList.size() > 0) { testQuestions
	 * = testQuestionService
	 * .findStudentResponseByTestIdAndUsernameForIncompleteTestTemp( testDB.getId(),
	 * username); } else { testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsername( testDB.getId(), username); } }
	 * 
	 * testQuestions = removeSpecialCharactersFromBean(testQuestions); //
	 * .findStudentResponseByTestIdAndUsername(testDB.getId(), // p.getName());
	 * List<TestQuestion> randomTestQuestnList = new ArrayList<>(); if
	 * ("Y".equals(testDB.getRandomQuestion())) {
	 * 
	 * List<TestQuestion> testQuestionDes = new ArrayList<>(); List<TestQuestion>
	 * testQuestionMcq = new ArrayList<>(); List<TestQuestion> testQuestionRng = new
	 * ArrayList<>(); List<TestQuestion> testQuestionImg = new ArrayList<>();
	 * 
	 * for(TestQuestion tq : testQuestions){
	 * 
	 * if("MCQ".equals(tq.getQuestionType())){ testQuestionMcq.add(tq); }else
	 * if("Numeric".equals(tq.getQuestionType())){ testQuestionRng.add(tq); }else
	 * if("Descriptive".equals(tq.getQuestionType())){ testQuestionDes.add(tq);
	 * }else if("Image".equals(tq.getQuestionType())){ testQuestionImg.add(tq); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxDesQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionDes
	 * .get(rand.nextInt(testQuestionDes.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxMcqQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionMcq
	 * .get(rand.nextInt(testQuestionMcq.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxRngQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionRng
	 * .get(rand.nextInt(testQuestionRng.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxImgQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionImg
	 * .get(rand.nextInt(testQuestionImg.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * 
	 * if ("Y".equals(testDB.getSameMarksQue())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * } else if ("N".equals(testDB.getSameMarksQue())) {
	 * 
	 * List<TestConfiguration> testConfigList = testConfigurationService
	 * .findAllByTestId(Long.parseLong(testId));
	 * 
	 * for (TestConfiguration tc : testConfigList) {
	 * 
	 * List<TestQuestion> testQuestionRandomList = testQuestions .stream() .filter(o
	 * -> o.getMarks().equals( tc.getMarks())) .collect(Collectors.toList());
	 * testQuestionRandomList =
	 * removeSpecialCharactersFromBean(testQuestionRandomList); for (int i = 0; i <
	 * tc.getNoOfQuestion(); i++) { Random rand = new Random(); TestQuestion
	 * testQuestionRandom = testQuestionRandomList .get(rand
	 * .nextInt(testQuestionRandomList .size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else { randomTestQuestnList
	 * .add(testQuestionRandom); } } }
	 * 
	 * }
	 * 
	 * } else { randomTestQuestnList.addAll(testQuestions); }
	 * test.setTestQuestions(randomTestQuestnList);
	 * 
	 * if (studentQuestnResponseList.size() == 0) { List<StudentQuestionResponse>
	 * sqrList = new ArrayList<>(); List<StudentQuestionResponseAudit> sqrAuditList
	 * = new ArrayList<>(); for (TestQuestion tq : randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(username); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(username);
	 * sqr.setCreatedBy(username);
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); sqrList.add(sqr); sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseService.insertBatch(sqrList);
	 * studentQuestionResponseService.insertBatchIntoTemp(sqrList);
	 * studentQuestionResponseAuditService .insertBatch(sqrAuditList); }
	 * 
	 * test.setStudentTest(studentTestDB);
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted()) if
	 * ("Y".equals(testDB.getShowResultsToStudents())) {
	 * 
	 * setNote(redirectAttrs, "Test completed. You scored " +
	 * (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); } else {
	 * 
	 * setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");
	 * 
	 * }
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "test/studentTest"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } }
	 * setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } return
	 * "test/studentTestUpdatedForMix"; }
	 */

	// New Mapping added by akshay on 11-10-2019

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTestNew", method = RequestMethod.GET)
	 * public String startStudentTestNew(Principal p, RedirectAttributes
	 * redirectAttrs, @ModelAttribute Test test, Model m, @RequestParam("id") String
	 * testId) throws ParseException { m.addAttribute("webPage", new
	 * WebPage("studentTest", "Test", true, false)); Test testDb =
	 * testService.findByID(Long.valueOf(testId)); Token userdetails1 = (Token) p;
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; String username = p.getName();
	 * String ProgramName = userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(
	 * Long.valueOf(testId), p.getName()); if (null == studentTestDB) {
	 * setNote(redirectAttrs, "You have not been assigned the test"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } // if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (testDb
	 * .getMaxAttempt() > studentTestDB.getAttempt())) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { String
	 * allowAfterEndDate = testDb.getAllowAfterEndDate();
	 * 
	 * int chkStartandEndDateOfTest = studentTestService
	 * .chkStartandEndDateOfTests(p.getName(), Long.valueOf(testId)); if
	 * (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate) &&
	 * userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
	 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } else {
	 * 
	 * m.addAttribute("action", "add"); Date startDateTime = null;
	 * 
	 * Date startTestTime = studentTestDB.getTestStartTime(); Date completeAt =
	 * Utils.getCompletionTimeDateType( studentTestDB.getTestStartTime(),
	 * testDb.getDuration());
	 * 
	 * if (startTestTime != null) {
	 * 
	 * if ((completeAt.compareTo(Utils.getInIST())) > 0 &&
	 * studentTestDB.getTestCompleted() == null) { startDateTime =
	 * studentTestDB.getTestStartTime();
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * // Date startDateTime = Utils.getInIST(); String startTestTimeFormat = Utils
	 * .changeTestStartTimeFormat(startDateTime); m.addAttribute("dateTime",
	 * startTestTimeFormat);
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getDurationCompleted() != null) { if
	 * (Integer.parseInt(studentTestDB .getDurationCompleted()) >= testDb
	 * .getDuration()) { testDb.setDuration(1); } else {
	 * testDb.setDuration(testDb.getDuration() - Integer.parseInt(studentTestDB
	 * .getDurationCompleted())); } } } else {
	 * studentTestDB.setDurationCompleted("0"); } m.addAttribute("testTime",
	 * testDb.getDuration() * 60);
	 * 
	 * studentTestDB.setTestStartTime(startDateTime);
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * //
	 * test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration())
	 * ; String completionTime = ""; int durationCompletedByStudent = 0;
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * 
	 * durationCompletedByStudent = Integer.parseInt(studentTestDB
	 * .getDurationCompleted()); }
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { completionTime =
	 * Utils.getCompletionTime(startDateTime, testDb.getDuration()); } else {
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * durationCompletedByStudent = Integer
	 * .parseInt(studentTestDB.getDurationCompleted()); int durationForTest =
	 * testDb.getDuration(); int remainingDurationForStudent = durationForTest -
	 * durationCompletedByStudent;
	 * 
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * remainingDurationForStudent); } else { // durationCompletedByStudent = 0;
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * testDb.getDuration()); }
	 * 
	 * }
	 * 
	 * 
	 * m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);
	 * 
	 * m.addAttribute("durationsLeft", completionTime);
	 * 
	 * if (studentTestDB.getScore() != null) { m.addAttribute("score",
	 * studentTestDB.getScore()); if (testDb.getPassScore() <
	 * studentTestDB.getScore()) { String status = "PASS"; m.addAttribute("status",
	 * status); } else { String status = "FAIL"; m.addAttribute("status", status); }
	 * } if ("Y".equals(studentTestDB.getTestCompleted())) {
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId())); if
	 * (sqrList.size() > 0) { for (StudentQuestionResponse sqr : sqrList) {
	 * sqr.setMarks(0.0); sqr.setAnswer(" ");
	 * studentQuestionResponseService.delete(sqr); } }
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() != null) {
	 * FileUtils.deleteQuietly(new File(studentTestDB .getStudentQRespFilePath()));
	 * }
	 * 
	 * studentTestDB.setTestCompleted(null);
	 * studentTestDB.setStudentQRespFilePath(null);
	 * studentTestService.upsert(studentTestDB); } else {
	 * studentTestService.upsert(studentTestDB); }
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB);
	 * 
	 * List<TestQuestion> testQuestions = testQuestionService
	 * .findByTestId(testDB.getId());
	 * 
	 * List<TestQuestion> testQuestions = new ArrayList<>();
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() != null) { ObjectMapper mapper =
	 * new ObjectMapper(); try { String studentQRespFiles = FileUtils
	 * .readFileToString(new File(studentTestDB .getStudentQRespFilePath()));
	 * List<StudentQuestionResponse> studentQRespList = mapper .readValue(
	 * studentQRespFiles, new TypeReference<List<StudentQuestionResponse>>() { });
	 * 
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (StudentQuestionResponse sqr : studentQRespList) {
	 * sqr.setAttemptNo(studentTestDB.getAttempt()); StudentQuestionResponseAudit
	 * sqrAudit = responseBeanToAuditBean( sqr, studentTestDB.getTestId());
	 * sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseAuditService .insertBatch(sqrAuditList);
	 * 
	 * studentQuestionResponseService .insertBatch(studentQRespList);
	 * studentQuestionResponseService .insertBatchIntoTemp(studentQRespList); }
	 * catch (Exception ex) { setError(redirectAttrs, "Error in displaying test..");
	 * logger.error("Exception", ex); return "redirect:/viewTestFinal"; } }
	 * 
	 * List<StudentQuestionResponse> studentQuestnResponseList =
	 * studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId()));
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername( testDB.getId(),
	 * username); } else { if (studentQuestnResponseList.size() > 0) { testQuestions
	 * = testQuestionService
	 * .findStudentResponseByTestIdAndUsernameForIncompleteTestTemp( testDB.getId(),
	 * username); } else { testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsername( testDB.getId(), username); } } //
	 * .findStudentResponseByTestIdAndUsername(testDB.getId(), // p.getName());
	 * 
	 * testQuestions = removeSpecialCharactersFromBean(testQuestions);
	 * 
	 * List<TestQuestion> randomTestQuestnList = new ArrayList<>(); if
	 * ("Y".equals(testDB.getRandomQuestion())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * if ("Y".equals(testDB.getSameMarksQue())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * } else if ("N".equals(testDB.getSameMarksQue())) {
	 * 
	 * List<TestConfiguration> testConfigList = testConfigurationService
	 * .findAllByTestId(Long.parseLong(testId));
	 * 
	 * for (TestConfiguration tc : testConfigList) {
	 * 
	 * List<TestQuestion> testQuestionRandomList = testQuestions .stream() .filter(o
	 * -> o.getMarks().equals( tc.getMarks())) .collect(Collectors.toList());
	 * 
	 * testQuestionRandomList =
	 * removeSpecialCharactersFromBean(testQuestionRandomList); for (int i = 0; i <
	 * tc.getNoOfQuestion(); i++) { Random rand = new Random(); TestQuestion
	 * testQuestionRandom = testQuestionRandomList .get(rand
	 * .nextInt(testQuestionRandomList .size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else { randomTestQuestnList
	 * .add(testQuestionRandom); } } }
	 * 
	 * } } else { randomTestQuestnList.addAll(testQuestions); }
	 * test.setTestQuestions(randomTestQuestnList);
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() == null) {
	 * 
	 * try {
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() == null) {
	 * List<StudentQuestionResponse> sqrList = new ArrayList<>();
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (TestQuestion tq : randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(username); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(username);
	 * sqr.setCreatedBy(username);
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); sqrList.add(sqr); sqrAuditList.add(sqrAudit);
	 * 
	 * }
	 * 
	 * // JsonObject jsonObject = new // JsonObject(studentTestJsonObject);
	 * logger.info("file creation entered"); String testSDate = Utils.formatDate(
	 * "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getStartDate()); String
	 * testEDate = Utils.formatDate( "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
	 * testDb.getEndDate()); String testDate = testSDate + "-" + testEDate; String
	 * folderPath = baseDir + "/" + app + "/" + "Tests"; File folderP = new
	 * File(folderPath); if (!folderP.exists()) { folderP.mkdir(); }
	 * logger.info("folder created"); String subFolderPath = folderPath + "/" +
	 * testDb.getId() + "-" + testDate; logger.info("subfolder created"); File
	 * subFolderP = new File(subFolderPath); if (!subFolderP.exists()) {
	 * subFolderP.mkdir(); }
	 * 
	 * String studentFilePath = subFolderPath + "/" + username + ".json";
	 * logger.info("studentFilePat subfolder created"); File fileP = new
	 * File(studentFilePath);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * // mapper.writeValueAsString(sqrList); mapper.writeValue(fileP, sqrList);
	 * test.setStudentFilePath(studentFilePath); logger.info("student file path--->"
	 * + studentFilePath); String relacedStudentFilePath = studentFilePath
	 * .replaceAll("/", ";"); m.addAttribute("relacedStudentFilePath",
	 * relacedStudentFilePath); m.addAttribute("studentFileP", studentFilePath);
	 * 
	 * studentTestService .updateStudentTestResponseFilePath( studentFilePath,
	 * studentTestDB.getId());
	 * 
	 * } } catch (Exception ex) {
	 * 
	 * logger.error("Error in creating json object", ex); }
	 * 
	 * List<StudentQuestionResponse> sqrList = new ArrayList<>();
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (TestQuestion tq : randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(username); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(username);
	 * sqr.setCreatedBy(username);
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); sqrList.add(sqr); sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseService.insertBatch(sqrList);
	 * studentQuestionResponseService .insertBatchIntoTemp(sqrList);
	 * studentQuestionResponseAuditService .insertBatch(sqrAuditList);
	 * 
	 * } else {
	 * 
	 * test.setStudentFilePath(studentTestDB .getStudentQRespFilePath());
	 * logger.info("student file path--->" +
	 * studentTestDB.getStudentQRespFilePath()); String relacedStudentFilePath =
	 * studentTestDB .getStudentQRespFilePath().replaceAll("/", ";");
	 * m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
	 * m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath()); }
	 * 
	 * test.setStudentTest(studentTestDB);
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted()) if
	 * ("Y".equals(testDB.getShowResultsToStudents())) {
	 * 
	 * setNote(redirectAttrs, "Test completed. You scored " +
	 * (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); } else {
	 * 
	 * setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");
	 * 
	 * }
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "test/studentTest"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } }
	 * setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } return
	 * "test/studentTestUpdated"; }
	 * 
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTestUpdatedForSubjective", method =
	 * RequestMethod.GET) public String
	 * startStudentTestUpdatedForSubjective(Principal p, RedirectAttributes
	 * redirectAttrs, @ModelAttribute Test test, Model m, @RequestParam("id") String
	 * testId) throws ParseException { m.addAttribute("webPage", new
	 * WebPage("studentTest", "Test", true, false));
	 * 
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; Test testDb =
	 * testService.findByID(Long.valueOf(testId)); StudentTest studentTestDB =
	 * studentTestService.findBytestIDAndUsername( Long.valueOf(testId),
	 * p.getName()); if (null == studentTestDB) { setNote(redirectAttrs,
	 * "You have not been assigned the test"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } // if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (testDb
	 * .getMaxAttempt() > studentTestDB.getAttempt())) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { String
	 * allowAfterEndDate = testDb.getAllowAfterEndDate(); // he is valid user to
	 * take test remain no of attempt int chkStartandEndDateOfTest =
	 * studentTestService .chkStartandEndDateOfTests(p.getName(),
	 * Long.valueOf(testId)); if (chkStartandEndDateOfTest == 0 &&
	 * "N".equals(allowAfterEndDate) &&
	 * userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
	 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } else {
	 * 
	 * m.addAttribute("action", "add");
	 * 
	 * Date startDateTime = null;
	 * 
	 * Date startTestTime = studentTestDB.getTestStartTime(); Date completeAt =
	 * Utils.getCompletionTimeDateType( studentTestDB.getTestStartTime(),
	 * testDb.getDuration());
	 * 
	 * if (startTestTime != null) {
	 * 
	 * if ((completeAt.compareTo(Utils.getInIST())) > 0 &&
	 * studentTestDB.getTestCompleted() == null) { startDateTime =
	 * studentTestDB.getTestStartTime();
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * // Date startDateTime = Utils.getInIST();
	 * 
	 * studentTestDB.setTestStartTime(startDateTime);
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * //
	 * test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration())
	 * ; String completionTime = ""; if
	 * (!"Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getDurationCompleted() != null) { if
	 * (Integer.parseInt(studentTestDB .getDurationCompleted()) >= testDb
	 * .getDuration()) { testDb.setDuration(1); } else {
	 * testDb.setDuration(testDb.getDuration() - Integer.parseInt(studentTestDB
	 * .getDurationCompleted())); } } } else {
	 * studentTestDB.setDurationCompleted("0"); } m.addAttribute("testTime",
	 * testDb.getDuration() * 60);
	 * 
	 * int durationCompletedByStudent = 0;
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * 
	 * durationCompletedByStudent = Integer.parseInt(studentTestDB
	 * .getDurationCompleted()); }
	 * 
	 * m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);
	 * 
	 * String startTestTimeFormat = Utils .changeTestStartTimeFormat(startDateTime);
	 * 
	 * m.addAttribute("dateTime", startTestTimeFormat);
	 * m.addAttribute("durationsLeft", completionTime);
	 * 
	 * if (studentTestDB.getScore() != null) { m.addAttribute("score",
	 * studentTestDB.getScore()); if (testDb.getPassScore() <
	 * studentTestDB.getScore()) { String status = "PASS"; m.addAttribute("status",
	 * status); } else { String status = "FAIL"; m.addAttribute("status", status); }
	 * }
	 * 
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId())); if
	 * (sqrList.size() > 0) { for (StudentQuestionResponse sqr : sqrList) {
	 * sqr.setMarks(0.0); sqr.setAnswer(" ");
	 * studentQuestionResponseService.delete(sqr); } } if
	 * ("Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getStudentQRespFilePath() != null) {
	 * FileUtils.deleteQuietly(new File(studentTestDB .getStudentQRespFilePath()));
	 * } studentTestDB.setTestCompleted(null);
	 * studentTestDB.setStudentQRespFilePath(null); //
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * // studentTestDB.setTestCompleted(null);
	 * 
	 * studentTestService.upsert(studentTestDB); } else {
	 * studentTestService.upsert(studentTestDB); }
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * new ArrayList<>();
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() != null) { ObjectMapper mapper =
	 * new ObjectMapper(); try { String studentQRespFiles = FileUtils
	 * .readFileToString(new File(studentTestDB .getStudentQRespFilePath()));
	 * List<StudentQuestionResponse> studentQRespList = mapper .readValue(
	 * studentQRespFiles, new TypeReference<List<StudentQuestionResponse>>() { });
	 * 
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (StudentQuestionResponse sqr : studentQRespList) {
	 * sqr.setAttemptNo(studentTestDB.getAttempt()); StudentQuestionResponseAudit
	 * sqrAudit = responseBeanToAuditBean( sqr, studentTestDB.getTestId());
	 * sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseAuditService .insertBatch(sqrAuditList);
	 * 
	 * studentQuestionResponseService .insertBatch(studentQRespList);
	 * studentQuestionResponseService .insertBatchIntoTemp(studentQRespList); }
	 * catch (Exception ex) { setError(redirectAttrs, "Error in displaying test..");
	 * logger.error("Exception", ex); return "redirect:/viewTestFinal"; } }
	 * 
	 * List<StudentQuestionResponse> studentQuestnResponseList =
	 * studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId()));
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername( testDB.getId(),
	 * p.getName()); } else { if (studentQuestnResponseList.size() > 0) {
	 * testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsernameForIncompleteTestTemp( testDB.getId(),
	 * p.getName()); } else { testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsername( testDB.getId(), p.getName()); } } //
	 * .findStudentResponseByTestIdAndUsername(testDB.getId(), // p.getName());
	 * testQuestions = removeSpecialCharactersFromBean(testQuestions);
	 * List<TestQuestion> randomTestQuestnList = new ArrayList<>(); if
	 * ("Y".equals(testDB.getRandomQuestion())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * 
	 * if ("Y".equals(testDB.getSameMarksQue())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * } else if ("N".equals(testDB.getSameMarksQue())) {
	 * 
	 * List<TestConfiguration> testConfigList = testConfigurationService
	 * .findAllByTestId(Long.parseLong(testId));
	 * 
	 * for (TestConfiguration tc : testConfigList) {
	 * 
	 * List<TestQuestion> testQuestionRandomList = testQuestions .stream() .filter(o
	 * -> o.getMarks().equals( tc.getMarks())) .collect(Collectors.toList());
	 * testQuestionRandomList =
	 * removeSpecialCharactersFromBean(testQuestionRandomList); for (int i = 0; i <
	 * tc.getNoOfQuestion(); i++) { Random rand = new Random(); TestQuestion
	 * testQuestionRandom = testQuestionRandomList .get(rand
	 * .nextInt(testQuestionRandomList .size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else { randomTestQuestnList
	 * .add(testQuestionRandom); } } }
	 * 
	 * } } else { randomTestQuestnList.addAll(testQuestions); }
	 * test.setTestQuestions(randomTestQuestnList);
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() == null) {
	 * List<StudentQuestionResponse> squestnResponseList = new ArrayList<>();
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (TestQuestion tq : randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(p.getName()); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(p.getName());
	 * sqr.setCreatedBy(p.getName());
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); squestnResponseList.add(sqr);
	 * 
	 * sqrAuditList.add(sqrAudit);
	 * 
	 * }
	 * 
	 * studentQuestionResponseService .insertBatch(squestnResponseList);
	 * studentQuestionResponseService .insertBatchIntoTemp(sqrList);
	 * studentQuestionResponseAuditService .insertBatch(sqrAuditList);
	 * 
	 * try { // JsonObject jsonObject = new // JsonObject(studentTestJsonObject);
	 * logger.info("file creation entered"); String testSDate = Utils.formatDate(
	 * "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getStartDate()); String
	 * testEDate = Utils.formatDate( "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
	 * testDb.getEndDate()); String testDate = testSDate + "-" + testEDate; String
	 * folderPath = baseDir + "/" + app + "/" + "Tests"; File folderP = new
	 * File(folderPath); if (!folderP.exists()) { folderP.mkdir(); }
	 * logger.info("folder created"); String subFolderPath = folderPath + "/" +
	 * testDb.getId() + "-" + testDate; logger.info("subfolder created"); File
	 * subFolderP = new File(subFolderPath); if (!subFolderP.exists()) {
	 * subFolderP.mkdir(); }
	 * 
	 * String studentFilePath = subFolderPath + "/" + p.getName() + ".json";
	 * logger.info("studentFilePat subfolder created"); File fileP = new
	 * File(studentFilePath);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * // mapper.writeValueAsString(sqrList); mapper.writeValue(fileP,
	 * squestnResponseList); test.setStudentFilePath(studentFilePath);
	 * logger.info("student file path--->" + studentFilePath); String
	 * relacedStudentFilePath = studentFilePath .replaceAll("/", ";");
	 * m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
	 * m.addAttribute("studentFileP", studentFilePath);
	 * 
	 * studentTestService.updateStudentTestResponseFilePath( studentFilePath,
	 * studentTestDB.getId());
	 * 
	 * } catch (Exception ex) {
	 * 
	 * logger.error("Error in creating json object", ex); }
	 * 
	 * } else { test.setStudentFilePath(studentTestDB .getStudentQRespFilePath());
	 * logger.info("student file path--->" +
	 * studentTestDB.getStudentQRespFilePath()); String relacedStudentFilePath =
	 * studentTestDB .getStudentQRespFilePath().replaceAll("/", ";");
	 * m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
	 * m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath()); }
	 * 
	 * // test.setTestQuestions(testQuestions);
	 * 
	 * test.setStudentTest(studentTestDB);
	 * 
	 * if (studentTestDB.isCompleted()) setSuccess(m, "Test completed. You scored "
	 * + (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); else
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted())
	 * setSuccess( m, "Test completed. You scored " + (studentTestDB.getScore() ==
	 * null ? 0 : studentTestDB.getScore()) + " marks out of " +
	 * test.getMaxScore());
	 * 
	 * else setSuccess(m, "Test has started");
	 * 
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "test/studentTest"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } }
	 * 
	 * setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } return
	 * "test/studentTestUpdatedForSubjective"; }
	 * 
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentTestUpdatedForMix", method =
	 * RequestMethod.GET) public String startStudentTestUpdatedForMix(Principal p,
	 * RedirectAttributes redirectAttrs, @ModelAttribute Test test, Model
	 * m, @RequestParam("id") String testId) throws ParseException {
	 * m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
	 * Test testDb = testService.findByID(Long.valueOf(testId)); Token userdetails1
	 * = (Token) p; UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; String username = p.getName();
	 * String ProgramName = userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * 
	 * String acadSession = u.getAcadSession();
	 * 
	 * m.addAttribute("Program_Name", ProgramName); m.addAttribute("AcadSession",
	 * acadSession);
	 * 
	 * StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(
	 * Long.valueOf(testId), p.getName()); if (null == studentTestDB) {
	 * setNote(redirectAttrs, "You have not been assigned the test"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } // if
	 * (!studentTestDB.isCompleted()) {
	 * 
	 * if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (testDb
	 * .getMaxAttempt() > studentTestDB.getAttempt())) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { String
	 * allowAfterEndDate = testDb.getAllowAfterEndDate();
	 * 
	 * int chkStartandEndDateOfTest = studentTestService
	 * .chkStartandEndDateOfTests(p.getName(), Long.valueOf(testId)); if
	 * (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate) &&
	 * userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
	 * if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } else {
	 * 
	 * m.addAttribute("action", "add"); Date startDateTime = null;
	 * 
	 * Date startTestTime = studentTestDB.getTestStartTime(); Date completeAt =
	 * Utils.getCompletionTimeDateType( studentTestDB.getTestStartTime(),
	 * testDb.getDuration());
	 * 
	 * if (startTestTime != null) {
	 * 
	 * if ((completeAt.compareTo(Utils.getInIST())) > 0 &&
	 * studentTestDB.getTestCompleted() == null) { startDateTime =
	 * studentTestDB.getTestStartTime();
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * } else { startDateTime = Utils.getInIST(); }
	 * 
	 * // Date startDateTime = Utils.getInIST(); String startTestTimeFormat = Utils
	 * .changeTestStartTimeFormat(startDateTime); m.addAttribute("dateTime",
	 * startTestTimeFormat);
	 * 
	 * studentTestDB.setTestStartTime(startDateTime);
	 * studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * //
	 * test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration())
	 * ; String completionTime = "";
	 * 
	 * if (!"Y".equals(studentTestDB.getTestCompleted())) { if
	 * (studentTestDB.getDurationCompleted() != null) { if
	 * (Integer.parseInt(studentTestDB .getDurationCompleted()) >= testDb
	 * .getDuration()) { testDb.setDuration(1); } else {
	 * testDb.setDuration(testDb.getDuration() - Integer.parseInt(studentTestDB
	 * .getDurationCompleted())); } } } else {
	 * studentTestDB.setDurationCompleted("0"); }
	 * 
	 * m.addAttribute("testTime", testDb.getDuration() * 60);
	 * 
	 * int durationCompletedByStudent = 0; if
	 * ("Y".equals(studentTestDB.getTestCompleted())) { completionTime =
	 * Utils.getCompletionTime(startDateTime, testDb.getDuration()); } else {
	 * 
	 * if (studentTestDB.getDurationCompleted() != null) {
	 * durationCompletedByStudent = Integer
	 * .parseInt(studentTestDB.getDurationCompleted()); int durationForTest =
	 * testDb.getDuration(); int remainingDurationForStudent = durationForTest -
	 * durationCompletedByStudent;
	 * 
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * remainingDurationForStudent); } else { // durationCompletedByStudent = 0;
	 * completionTime = Utils.getCompletionTime(startDateTime,
	 * testDb.getDuration()); }
	 * 
	 * }
	 * 
	 * m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);
	 * 
	 * m.addAttribute("durationsLeft", completionTime);
	 * 
	 * if (studentTestDB.getScore() != null) { m.addAttribute("score",
	 * studentTestDB.getScore()); if (testDb.getPassScore() <
	 * studentTestDB.getScore()) { String status = "PASS"; m.addAttribute("status",
	 * status); } else { String status = "FAIL"; m.addAttribute("status", status); }
	 * } if ("Y".equals(studentTestDB.getTestCompleted())) {
	 * List<StudentQuestionResponse> sqrList = studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId())); if
	 * (sqrList.size() > 0) { for (StudentQuestionResponse sqr : sqrList) {
	 * sqr.setMarks(0.0); sqr.setAnswer(" ");
	 * studentQuestionResponseService.delete(sqr); } } if
	 * (studentTestDB.getStudentQRespFilePath() != null) {
	 * FileUtils.deleteQuietly(new File(studentTestDB .getStudentQRespFilePath()));
	 * } studentTestDB.setStudentQRespFilePath(null);
	 * studentTestDB.setTestCompleted(null);
	 * studentTestService.upsert(studentTestDB); } else {
	 * studentTestService.upsert(studentTestDB); }
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB);
	 * 
	 * List<TestQuestion> testQuestions = testQuestionService
	 * .findByTestId(testDB.getId());
	 * 
	 * List<TestQuestion> testQuestions = new ArrayList<>();
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() != null) { ObjectMapper mapper =
	 * new ObjectMapper(); try { String studentQRespFiles = FileUtils
	 * .readFileToString(new File(studentTestDB .getStudentQRespFilePath()));
	 * List<StudentQuestionResponse> studentQRespList = mapper .readValue(
	 * studentQRespFiles, new TypeReference<List<StudentQuestionResponse>>() { });
	 * 
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (StudentQuestionResponse sqr : studentQRespList) {
	 * sqr.setAttemptNo(studentTestDB.getAttempt()); StudentQuestionResponseAudit
	 * sqrAudit = responseBeanToAuditBean( sqr, studentTestDB.getTestId());
	 * sqrAuditList.add(sqrAudit);
	 * 
	 * } studentQuestionResponseAuditService .insertBatch(sqrAuditList);
	 * 
	 * studentQuestionResponseService .insertBatch(studentQRespList);
	 * studentQuestionResponseService .insertBatchIntoTemp(studentQRespList); }
	 * catch (Exception ex) { setError(redirectAttrs, "Error in displaying test..");
	 * logger.error("Exception", ex); return "redirect:/viewTestFinal"; } }
	 * 
	 * List<StudentQuestionResponse> studentQuestnResponseList =
	 * studentQuestionResponseService
	 * .findByStudentTestId(String.valueOf(studentTestDB .getId()));
	 * 
	 * if ("Y".equals(studentTestDB.getTestCompleted())) { testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername( testDB.getId(),
	 * username); } else { if (studentQuestnResponseList.size() > 0) { testQuestions
	 * = testQuestionService
	 * .findStudentResponseByTestIdAndUsernameForIncompleteTestTemp( testDB.getId(),
	 * username); } else { testQuestions = testQuestionService
	 * .findStudentResponseByTestIdAndUsername( testDB.getId(), username); } }
	 * 
	 * testQuestions = removeSpecialCharactersFromBean(testQuestions); //
	 * .findStudentResponseByTestIdAndUsername(testDB.getId(), // p.getName());
	 * List<TestQuestion> randomTestQuestnList = new ArrayList<>(); if
	 * ("Y".equals(testDB.getRandomQuestion())) {
	 * 
	 * List<TestQuestion> testQuestionDes = new ArrayList<>(); List<TestQuestion>
	 * testQuestionMcq = new ArrayList<>(); List<TestQuestion> testQuestionRng = new
	 * ArrayList<>(); List<TestQuestion> testQuestionImg = new ArrayList<>();
	 * 
	 * for(TestQuestion tq : testQuestions){
	 * 
	 * if("MCQ".equals(tq.getQuestionType())){ testQuestionMcq.add(tq); }else
	 * if("Numeric".equals(tq.getQuestionType())){ testQuestionRng.add(tq); }else
	 * if("Descriptive".equals(tq.getQuestionType())){ testQuestionDes.add(tq);
	 * }else if("Image".equals(tq.getQuestionType())){ testQuestionImg.add(tq); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxDesQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionDes
	 * .get(rand.nextInt(testQuestionDes.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxMcqQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionMcq
	 * .get(rand.nextInt(testQuestionMcq.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxRngQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionRng
	 * .get(rand.nextInt(testQuestionRng.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxImgQueToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionImg
	 * .get(rand.nextInt(testQuestionImg.size())); if
	 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * 
	 * if ("Y".equals(testDB.getSameMarksQue())) {
	 * 
	 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
	 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
	 * .get(rand.nextInt(testQuestions.size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else {
	 * randomTestQuestnList.add(testQuestionRandom); } }
	 * 
	 * } else if ("N".equals(testDB.getSameMarksQue())) {
	 * 
	 * List<TestConfiguration> testConfigList = testConfigurationService
	 * .findAllByTestId(Long.parseLong(testId));
	 * 
	 * for (TestConfiguration tc : testConfigList) {
	 * 
	 * List<TestQuestion> testQuestionRandomList = testQuestions .stream() .filter(o
	 * -> o.getMarks().equals( tc.getMarks())) .collect(Collectors.toList());
	 * testQuestionRandomList =
	 * removeSpecialCharactersFromBean(testQuestionRandomList); for (int i = 0; i <
	 * tc.getNoOfQuestion(); i++) { Random rand = new Random(); TestQuestion
	 * testQuestionRandom = testQuestionRandomList .get(rand
	 * .nextInt(testQuestionRandomList .size())); if (randomTestQuestnList
	 * .contains(testQuestionRandom)) { i--; } else { randomTestQuestnList
	 * .add(testQuestionRandom); } } }
	 * 
	 * }
	 * 
	 * } else { randomTestQuestnList.addAll(testQuestions); }
	 * test.setTestQuestions(randomTestQuestnList);
	 * 
	 * if (studentTestDB.getStudentQRespFilePath() == null) {
	 * List<StudentQuestionResponse> sqrList = new ArrayList<>();
	 * List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>(); for
	 * (TestQuestion tq : randomTestQuestnList) {
	 * 
	 * StudentQuestionResponse sqr = new StudentQuestionResponse();
	 * 
	 * sqr.setUsername(username); sqr.setStudentTestId(studentTestDB.getId());
	 * sqr.setQuestionId(tq.getId()); sqr.setLastModifiedBy(username);
	 * sqr.setCreatedBy(username);
	 * 
	 * StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean( sqr,
	 * studentTestDB.getTestId()); sqrList.add(sqr); sqrAuditList.add(sqrAudit);
	 * 
	 * }
	 * 
	 * studentQuestionResponseService.insertBatch(sqrList);
	 * studentQuestionResponseService .insertBatchIntoTemp(sqrList);
	 * studentQuestionResponseAuditService .insertBatch(sqrAuditList);
	 * 
	 * 
	 * try { // JsonObject jsonObject = new // JsonObject(studentTestJsonObject);
	 * logger.info("file creation entered"); String testSDate = Utils.formatDate(
	 * "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getStartDate()); String
	 * testEDate = Utils.formatDate( "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
	 * testDb.getEndDate()); String testDate = testSDate + "-" + testEDate; String
	 * folderPath = baseDir + "/" + app + "/" + "Tests"; File folderP = new
	 * File(folderPath); if (!folderP.exists()) { folderP.mkdir(); }
	 * logger.info("folder created"); String subFolderPath = folderPath + "/" +
	 * testDb.getId() + "-" + testDate; logger.info("subfolder created"); File
	 * subFolderP = new File(subFolderPath); if (!subFolderP.exists()) {
	 * subFolderP.mkdir(); }
	 * 
	 * String studentFilePath = subFolderPath + "/" + username + ".json";
	 * logger.info("studentFilePat subfolder created"); File fileP = new
	 * File(studentFilePath);
	 * 
	 * ObjectMapper mapper = new ObjectMapper();
	 * 
	 * // mapper.writeValueAsString(sqrList); mapper.writeValue(fileP, sqrList);
	 * test.setStudentFilePath(studentFilePath); logger.info("student file path--->"
	 * + studentFilePath); String relacedStudentFilePath = studentFilePath
	 * .replaceAll("/", ";"); m.addAttribute("relacedStudentFilePath",
	 * relacedStudentFilePath); m.addAttribute("studentFileP", studentFilePath);
	 * 
	 * studentTestService.updateStudentTestResponseFilePath( studentFilePath,
	 * studentTestDB.getId());
	 * 
	 * } catch (Exception ex) {
	 * 
	 * logger.error("Error in creating json object", ex); } } else {
	 * test.setStudentFilePath(studentTestDB .getStudentQRespFilePath());
	 * logger.info("student file path--->" +
	 * studentTestDB.getStudentQRespFilePath()); String relacedStudentFilePath =
	 * studentTestDB .getStudentQRespFilePath().replaceAll("/", ";");
	 * m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
	 * m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath()); }
	 * 
	 * test.setStudentTest(studentTestDB);
	 * 
	 * setSuccess(m, "Test has started"); }
	 * 
	 * } else {
	 * 
	 * try { studentTestDB.setLastModifiedBy(p.getName());
	 * 
	 * studentTestService.upsert(studentTestDB);
	 * 
	 * Test testDB = testService.findByID(test.getId());
	 * LMSHelper.copyNonNullFields(test, testDB); List<TestQuestion> testQuestions =
	 * testQuestionService .findStudentResponseByTestIdAndUsername(testDB.getId(),
	 * p.getName()); test.setTestQuestions(testQuestions);
	 * test.setStudentTest(studentTestDB); // if (studentTestDB.isCompleted()) if
	 * ("Y".equals(testDB.getShowResultsToStudents())) {
	 * 
	 * setNote(redirectAttrs, "Test completed. You scored " +
	 * (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
	 * " marks out of " + test.getMaxScore()); } else {
	 * 
	 * setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");
	 * 
	 * }
	 * 
	 * } catch (Exception e) { logger.error("Error", e); setError(m,
	 * "Test could not be started"); return "test/studentTest"; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "test/studentTest"; } else { redirectAttrs .addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } }
	 * setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!"); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) { return
	 * "redirect:/testList"; } else { redirectAttrs.addAttribute("courseId",
	 * testDb.getCourseId()); return "redirect:/viewTestFinal"; } } return
	 * "test/studentTestUpdatedForMix"; }
	 */

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/startStudentTestNew", method = RequestMethod.GET)
	public String startStudentTestNew(Principal p, RedirectAttributes redirectAttrs, @ModelAttribute Test test, Model m,
			@RequestParam("id") String testId) throws ParseException {
		m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
		Test testDb = testService.findByID(Long.valueOf(testId));
		Token userdetails1 = (Token) p;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (null != testDb) {
			if ("N".equals(testDb.getActive())) {
				setNote(redirectAttrs, "Test has been deactivated");
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
						|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			}
		}

		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		if (null == studentTestDB) {
			setNote(redirectAttrs, "You have not been assigned the test");
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/testList";
			} else {
				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";
			}
		}

		if ((((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (studentTestDB.getAttempt() == 0))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN))
				&& null != studentTestDB.getStudentQRespFilePath() && "".equals(studentTestDB.getStudentQRespFilePath())
				&& null == studentTestDB.getTestStartTime()) {
			logger.info("INSIDE IF-->");
			String allowAfterEndDate = testDb.getAllowAfterEndDate();
			
			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");

				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";

			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				if (!"Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getDurationCompleted() != null) {
						if (Integer.parseInt(studentTestDB.getDurationCompleted()) >= testDb.getDuration()) {
							testDb.setDuration(1);
						} else {
							testDb.setDuration(
									testDb.getDuration() - Integer.parseInt(studentTestDB.getDurationCompleted()));
						}
					}
				} else {
					studentTestDB.setDurationCompleted("0");
				}
				m.addAttribute("testTime", testDb.getDuration() * 60);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				String completionTime = "";
				int durationCompletedByStudent = 0;

				if (studentTestDB.getDurationCompleted() != null) {

					durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}

				studentTestService.upsert(studentTestDB);
				logger.info("testStarted---->faculty");
				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);

				List<TestQuestion> testQuestions = new ArrayList<>();

				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				logger.info("studentQuestnResponseList---->"+studentQuestnResponseList.size());
				if (studentQuestnResponseList.size() > 0) {

					testQuestions = testQuestionService
							.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(testDB.getId(), username);
					testQuestions = removeSpecialCharactersFromBean(testQuestions);

					randomTestQuestnList.addAll(testQuestions);

				} else {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							username);

					testQuestions = removeSpecialCharactersFromBean(testQuestions);

					if ("Y".equals(testDB.getRandomQuestion())) {

						if ("Y".equals(testDB.getSameMarksQue())) {

							for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}

						} else if ("N".equals(testDB.getSameMarksQue())) {

							List<TestConfiguration> testConfigList = testConfigurationService
									.findAllByTestId(Long.parseLong(testId));
							
							for (TestConfiguration tc : testConfigList) {

								List<TestQuestion> testQuestionRandomList = testQuestions.stream()
										.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());

								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								for (int i = 0; i < tc.getNoOfQuestion(); i++) {
									Random rand = new Random();
									TestQuestion testQuestionRandom = testQuestionRandomList
											.get(rand.nextInt(testQuestionRandomList.size()));
									if (randomTestQuestnList.contains(testQuestionRandom)) {
										i--;
									} else {
										randomTestQuestnList.add(testQuestionRandom);
									}
								}
							}

						}
					} else {
						randomTestQuestnList.addAll(testQuestions);
					}

				}
				test.setTestQuestions(randomTestQuestnList);
				logger.info("testQuestions---->"+test.getTestQuestions());
				test.setStudentFilePath(studentTestDB.getStudentQRespFilePath());
				logger.info("student file path--->" + studentTestDB.getStudentQRespFilePath());
				String relacedStudentFilePath = studentTestDB.getStudentQRespFilePath().replaceAll("/", ";");
				m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
				m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath());

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");

			}

		} else if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted())
				&& (testDb.getMaxAttempt() > studentTestDB.getAttempt()))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String allowAfterEndDate = testDb.getAllowAfterEndDate();
			logger.info("MorethanOne--->");
			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
						|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				if (!"Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getDurationCompleted() != null) {
						if (Integer.parseInt(studentTestDB.getDurationCompleted()) >= testDb.getDuration()) {
							testDb.setDuration(1);
						} else {
							testDb.setDuration(
									testDb.getDuration() - Integer.parseInt(studentTestDB.getDurationCompleted()));
						}
					}
				} else {
					studentTestDB.setDurationCompleted("0");
				}
				m.addAttribute("testTime", testDb.getDuration() * 60);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				String completionTime = "";
				int durationCompletedByStudent = 0;

				if (studentTestDB.getDurationCompleted() != null) {

					durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					List<StudentQuestionResponse> sqrList = studentQuestionResponseService
							.findByStudentTestId(String.valueOf(studentTestDB.getId()));
					if (sqrList.size() > 0) {
						for (StudentQuestionResponse sqr : sqrList) {
							sqr.setMarks(0.0);
							sqr.setAnswer(" ");
							studentQuestionResponseService.delete(sqr);
						}
					}

					if (studentTestDB.getStudentQRespFilePath() != null) {
						FileUtils.deleteQuietly(new File(studentTestDB.getStudentQRespFilePath()));
					}

					studentTestDB.setTestCompleted(null);
					//New Pool Changes
					studentTestDB.setTestEndTime(null);
					//New Pool Changes
					studentTestDB.setStudentQRespFilePath(null);
					studentTestService.upsert(studentTestDB);
				} else {
					studentTestService.upsert(studentTestDB);
				}

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);

				List<TestQuestion> testQuestions = new ArrayList<>();

				if (studentTestDB.getStudentQRespFilePath() != null) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String studentQRespFiles = FileUtils
								.readFileToString(new File(studentTestDB.getStudentQRespFilePath()));
						List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
								new TypeReference<List<StudentQuestionResponse>>() {
								});

						List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
						for (StudentQuestionResponse sqr : studentQRespList) {
							sqr.setAttemptNo(studentTestDB.getAttempt());
							StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr,
									studentTestDB.getTestId());
							sqrAuditList.add(sqrAudit);

						}
						studentQuestionResponseAuditService.insertBatch(sqrAuditList);

						studentQuestionResponseService.insertBatch(studentQRespList);
						studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);
					} catch (Exception ex) {
						setError(redirectAttrs, "Error in displaying test..");
						logger.error("Exception", ex);
						if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
							return "redirect:/testList";
						} else {
							return "redirect:/viewTestFinal";

						}
					}
				}

				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				if ("Y".equals(studentTestDB.getTestCompleted())) {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							username);
				} else {
					if (studentQuestnResponseList.size() > 0) {
						testQuestions = testQuestionService
								.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(testDB.getId(), username);
					} else {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
								username);
					}
				}

				testQuestions = removeSpecialCharactersFromBean(testQuestions);
				logger.info("testQuestions---->"+testQuestions);
				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				if ("Y".equals(testDB.getRandomQuestion())) {

					if ("Y".equals(testDB.getSameMarksQue())) {

						for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
							Random rand = new Random();
							TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
							if (randomTestQuestnList.contains(testQuestionRandom)) {
								i--;
							} else {
								randomTestQuestnList.add(testQuestionRandom);
							}
						}

					} else if ("N".equals(testDB.getSameMarksQue())) {

						List<TestConfiguration> testConfigList = testConfigurationService.findAllByTestId(Long.parseLong(testId));
						//New Pool Changes
						List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService.findAllByTestId(Long.parseLong(testId));
						
						if(testConfigList.size()>0){
							for (TestConfiguration tc : testConfigList) {
	
								List<TestQuestion> testQuestionRandomList = testQuestions.stream()
										.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());
	
								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								for (int i = 0; i < tc.getNoOfQuestion(); i++) {
									Random rand = new Random();
									TestQuestion testQuestionRandom = testQuestionRandomList
											.get(rand.nextInt(testQuestionRandomList.size()));
									if (randomTestQuestnList.contains(testQuestionRandom)) {
										i--;
									} else {
										randomTestQuestnList.add(testQuestionRandom);
									}
								}
							}
						}else if(testPoolConfigList.size()>0){
							logger.info("For pool Config--->");
							if("Y".equals(studentTestDB.getTestCompleted()) || studentQuestnResponseList.size() <= 0) {
								for (TestPoolConfiguration tpc : testPoolConfigList) {
	
//									List<TestQuestion> testQuestionRandomList = testQuestions.stream()
//											.filter(o -> o.getMarks().equals(tpc.getMarks()))
//											.collect(Collectors.toList());
									List<TestQuestion> testQuestionRandomList = new ArrayList<TestQuestion>();
									testQuestionRandomList = testQuestionService.getQuestionsByPoolConfiguration(tpc.getTestId(),tpc.getTestPoolId(),tpc.getMarks());
									testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
									for (int i = 0; i < tpc.getNoOfQuestion(); i++) {
										Random rand = new Random();
										TestQuestion testQuestionRandom = testQuestionRandomList
												.get(rand
														.nextInt(testQuestionRandomList
																.size()));
										if (randomTestQuestnList
												.contains(testQuestionRandom)) {
											i--;
										} else {
											randomTestQuestnList
													.add(testQuestionRandom);
										}
									}
								}
							}else {
									
								List<TestQuestion> testQuestionRandomList = new ArrayList<TestQuestion>();
								testQuestionRandomList.addAll(testQuestions);
								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								randomTestQuestnList.addAll(testQuestionRandomList);
							}
						}
						//New Pool Changes
					}
				} else {
					randomTestQuestnList.addAll(testQuestions);
				}
				test.setTestQuestions(randomTestQuestnList);
				logger.info("test.testQues--->"+test.getTestQuestions());
				if (studentTestDB.getStudentQRespFilePath() == null) {

					try {

						if (studentTestDB.getStudentQRespFilePath() == null) {
							List<StudentQuestionResponse> sqrList = new ArrayList<>();
							List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
							for (TestQuestion tq : randomTestQuestnList) {

								StudentQuestionResponse sqr = new StudentQuestionResponse();

								sqr.setUsername(username);
								sqr.setStudentTestId(studentTestDB.getId());
								sqr.setQuestionId(tq.getId());
								sqr.setLastModifiedBy(username);
								sqr.setCreatedBy(username);

								StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr,
										studentTestDB.getTestId());
								sqrList.add(sqr);
								sqrAuditList.add(sqrAudit);

							}

							logger.info("file creation entered");
							String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
									testDb.getStartDate());
							String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
									testDb.getEndDate());
							String testDate = testSDate + "-" + testEDate;
							String folderPath = testBaseDir + "/" + app + "/" + "Tests";
							File folderP = new File(folderPath);
							if (!folderP.exists()) {
								folderP.mkdirs();
							}

							folderP.setExecutable(true, false);
							folderP.setWritable(true, false);
							folderP.setReadable(true, false);

							logger.info("folder created");
							String subFolderPath = folderPath + "/" + testDb.getId() + "-" + testDate;
							logger.info("subfolder created");
							File subFolderP = new File(subFolderPath);
							if (!subFolderP.exists()) {
								subFolderP.mkdir();
							}

							subFolderP.setExecutable(true, false);
							subFolderP.setWritable(true, false);
							subFolderP.setReadable(true, false);

							String studentFilePath = subFolderPath + "/" + username + ".json";
							logger.info("studentFilePat subfolder created");
							File fileP = new File(studentFilePath);

							fileP.setExecutable(true, false);
							fileP.setWritable(true, false);
							fileP.setReadable(true, false);

							ObjectMapper mapper = new ObjectMapper();

							mapper.writeValue(fileP, sqrList);
							test.setStudentFilePath(studentFilePath);
							logger.info("student file path--->" + studentFilePath);
							String relacedStudentFilePath = studentFilePath.replaceAll("/", ";");
							m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
							m.addAttribute("studentFileP", studentFilePath);

							studentTestService.updateStudentTestResponseFilePath(studentFilePath,
									studentTestDB.getId());
							//New Pool Changes
							studentQuestionResponseService.insertBatch(sqrList);
							//New Pool Changes
						}
					} catch (Exception ex) {

						logger.error("Error in creating json object", ex);
					}

				} else {

					test.setStudentFilePath(studentTestDB.getStudentQRespFilePath());
					logger.info("student file path--->" + studentTestDB.getStudentQRespFilePath());
					String relacedStudentFilePath = studentTestDB.getStudentQRespFilePath().replaceAll("/", ";");
					m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
					m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath());
				}

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");
			}

		} else {
			logger.info("else------->");
			try {
				studentTestDB.setLastModifiedBy(p.getName());

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = testQuestionService
						.findStudentResponseByTestIdAndUsername(testDB.getId(), p.getName());
				test.setTestQuestions(testQuestions);
				test.setStudentTest(studentTestDB);

				if ("Y".equals(testDB.getShowResultsToStudents())) {

					setNote(redirectAttrs,
							"Test completed. You scored "
									+ (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore())
									+ " marks out of " + test.getMaxScore());
				} else {

					setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");

				}

			} catch (Exception e) {
				logger.error("Error", e);
				setError(m, "Test could not be started");

				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
					return "test/studentTest";
				} else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			}
			setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!");
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/testList";
			} else {
				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";
			}
		}
		return "test/studentTestUpdated";
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/startStudentTestUpdatedForSubjective", method = RequestMethod.GET)
	public String startStudentTestUpdatedForSubjective(Principal p, RedirectAttributes redirectAttrs,
			@ModelAttribute Test test, Model m, @RequestParam("id") String testId) throws ParseException {
		m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		Test testDb = testService.findByID(Long.valueOf(testId));

		if (null != testDb) {
			if ("N".equals(testDb.getActive())) {
				setNote(redirectAttrs, "Test has been deactivated");
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			}
		}

		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		if (null == studentTestDB) {
			setNote(redirectAttrs, "You have not been assigned the test");
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/testList";
			} else {
				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";
			}
		}
		// if (!studentTestDB.isCompleted()) {

		if ((((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (studentTestDB.getAttempt() == 0))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN))
				&& null != studentTestDB.getStudentQRespFilePath() && "".equals(studentTestDB.getStudentQRespFilePath())
				&& null == studentTestDB.getTestStartTime()) {

			String allowAfterEndDate = testDb.getAllowAfterEndDate();

			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");

				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";

			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				if (!"Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getDurationCompleted() != null) {
						if (Integer.parseInt(studentTestDB.getDurationCompleted()) >= testDb.getDuration()) {
							testDb.setDuration(1);
						} else {
							testDb.setDuration(
									testDb.getDuration() - Integer.parseInt(studentTestDB.getDurationCompleted()));
						}
					}
				} else {
					studentTestDB.setDurationCompleted("0");
				}
				m.addAttribute("testTime", testDb.getDuration() * 60);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				String completionTime = "";
				int durationCompletedByStudent = 0;

				if (studentTestDB.getDurationCompleted() != null) {

					durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);

				List<TestQuestion> testQuestions = new ArrayList<>();

				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				List<TestQuestion> randomTestQuestnList = new ArrayList<>();

				if (studentQuestnResponseList.size() > 0) {

					testQuestions = testQuestionService
							.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(testDB.getId(), p.getName());
					testQuestions = removeSpecialCharactersFromBean(testQuestions);

					randomTestQuestnList.addAll(testQuestions);

				} else {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							p.getName());

					testQuestions = removeSpecialCharactersFromBean(testQuestions);

					if ("Y".equals(testDB.getRandomQuestion())) {

						if ("Y".equals(testDB.getSameMarksQue())) {

							for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}

						} else if ("N".equals(testDB.getSameMarksQue())) {

							List<TestConfiguration> testConfigList = testConfigurationService
									.findAllByTestId(Long.parseLong(testId));

							for (TestConfiguration tc : testConfigList) {

								List<TestQuestion> testQuestionRandomList = testQuestions.stream()
										.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());

								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								for (int i = 0; i < tc.getNoOfQuestion(); i++) {
									Random rand = new Random();
									TestQuestion testQuestionRandom = testQuestionRandomList
											.get(rand.nextInt(testQuestionRandomList.size()));
									if (randomTestQuestnList.contains(testQuestionRandom)) {
										i--;
									} else {
										randomTestQuestnList.add(testQuestionRandom);
									}
								}
							}

						}
					} else {
						randomTestQuestnList.addAll(testQuestions);
					}

				}
				test.setTestQuestions(randomTestQuestnList);

				test.setStudentFilePath(studentTestDB.getStudentQRespFilePath());
				logger.info("student file path--->" + studentTestDB.getStudentQRespFilePath());
				String relacedStudentFilePath = studentTestDB.getStudentQRespFilePath().replaceAll("/", ";");
				m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
				m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath());

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");

			}

		} else if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted())
				&& (testDb.getMaxAttempt() > studentTestDB.getAttempt()))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String allowAfterEndDate = testDb.getAllowAfterEndDate();
			// he is valid user to take test remain no of attempt
			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
						|| userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			} else {

				m.addAttribute("action", "add");

				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				// Date startDateTime = Utils.getInIST();

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				// test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration());
				String completionTime = "";
				if (!"Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getDurationCompleted() != null) {
						if (Integer.parseInt(studentTestDB.getDurationCompleted()) >= testDb.getDuration()) {
							testDb.setDuration(1);
						} else {
							testDb.setDuration(
									testDb.getDuration() - Integer.parseInt(studentTestDB.getDurationCompleted()));
						}
					}
				} else {
					studentTestDB.setDurationCompleted("0");
				}
				m.addAttribute("testTime", testDb.getDuration() * 60);

				int durationCompletedByStudent = 0;

				if (studentTestDB.getDurationCompleted() != null) {

					durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);

				m.addAttribute("dateTime", startTestTimeFormat);
				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}

				List<StudentQuestionResponse> sqrList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));
				if (sqrList.size() > 0) {
					for (StudentQuestionResponse sqr : sqrList) {
						sqr.setMarks(0.0);
						sqr.setAnswer(" ");
						studentQuestionResponseService.delete(sqr);
					}
				}
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getStudentQRespFilePath() != null) {
						FileUtils.deleteQuietly(new File(studentTestDB.getStudentQRespFilePath()));
					}
					studentTestDB.setTestCompleted(null);
					//New Pool Changes
					studentTestDB.setTestEndTime(null);
					//New Pool Changes
					studentTestDB.setStudentQRespFilePath(null);
					// studentTestService.upsert(studentTestDB);

					// studentTestDB.setTestCompleted(null);

					studentTestService.upsert(studentTestDB);
				} else {
					studentTestService.upsert(studentTestDB);
				}

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = new ArrayList<>();

				if (studentTestDB.getStudentQRespFilePath() != null) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String studentQRespFiles = FileUtils
								.readFileToString(new File(studentTestDB.getStudentQRespFilePath()));
						List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
								new TypeReference<List<StudentQuestionResponse>>() {
								});

						List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
						for (StudentQuestionResponse sqr : studentQRespList) {
							sqr.setAttemptNo(studentTestDB.getAttempt());
							StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr,
									studentTestDB.getTestId());
							sqrAuditList.add(sqrAudit);

						}
						studentQuestionResponseAuditService.insertBatch(sqrAuditList);

						studentQuestionResponseService.insertBatch(studentQRespList);
						studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);
					} catch (Exception ex) {
						setError(redirectAttrs, "Error in displaying test..");
						logger.error("Exception", ex);
						if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
							return "redirect:/testList";
						} else {
							return "redirect:/viewTestFinal";

						}
					}
				}

				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				if ("Y".equals(studentTestDB.getTestCompleted())) {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							p.getName());
				} else {
					if (studentQuestnResponseList.size() > 0) {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(
								testDB.getId(), p.getName());
					} else {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
								p.getName());
					}
				}
				// .findStudentResponseByTestIdAndUsername(testDB.getId(),
				// p.getName());
				testQuestions = removeSpecialCharactersFromBean(testQuestions);
				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				if ("Y".equals(testDB.getRandomQuestion())) {
					/*
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxQuestnToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestions
					 * .get(rand.nextInt(testQuestions.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 */

					if ("Y".equals(testDB.getSameMarksQue())) {

						for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
							Random rand = new Random();
							TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
							if (randomTestQuestnList.contains(testQuestionRandom)) {
								i--;
							} else {
								randomTestQuestnList.add(testQuestionRandom);
							}
						}

					} else if ("N".equals(testDB.getSameMarksQue())) {

						List<TestConfiguration> testConfigList = testConfigurationService
								.findAllByTestId(Long.parseLong(testId));

//						for (TestConfiguration tc : testConfigList) {
//
//							List<TestQuestion> testQuestionRandomList = testQuestions.stream()
//									.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());
//							testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
//							for (int i = 0; i < tc.getNoOfQuestion(); i++) {
//								Random rand = new Random();
//								TestQuestion testQuestionRandom = testQuestionRandomList
//										.get(rand.nextInt(testQuestionRandomList.size()));
//								if (randomTestQuestnList.contains(testQuestionRandom)) {
//									i--;
//								} else {
//									randomTestQuestnList.add(testQuestionRandom);
//								}
//							}
//						}
						//New Pool Changes
						List<TestPoolConfiguration> testPoolConfigList = testPoolConfigurationService.findAllByTestId(Long.parseLong(testId));
						
						if(testConfigList.size()>0){
							for (TestConfiguration tc : testConfigList) {
	
								List<TestQuestion> testQuestionRandomList = testQuestions.stream()
										.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());
	
								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								for (int i = 0; i < tc.getNoOfQuestion(); i++) {
									Random rand = new Random();
									TestQuestion testQuestionRandom = testQuestionRandomList
											.get(rand.nextInt(testQuestionRandomList.size()));
									if (randomTestQuestnList.contains(testQuestionRandom)) {
										i--;
									} else {
										randomTestQuestnList.add(testQuestionRandom);
									}
								}
							}
						}else if(testPoolConfigList.size()>0){
							logger.info("For pool Config--->");
							if("Y".equals(studentTestDB.getTestCompleted()) || studentQuestnResponseList.size() <= 0) {
								for (TestPoolConfiguration tpc : testPoolConfigList) {
	
//									List<TestQuestion> testQuestionRandomList = testQuestions.stream()
//											.filter(o -> o.getMarks().equals(tpc.getMarks()))
//											.collect(Collectors.toList());
									List<TestQuestion> testQuestionRandomList = new ArrayList<TestQuestion>();
									testQuestionRandomList = testQuestionService.getQuestionsByPoolConfiguration(tpc.getTestId(),tpc.getTestPoolId(),tpc.getMarks());
									testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
									for (int i = 0; i < tpc.getNoOfQuestion(); i++) {
										Random rand = new Random();
										TestQuestion testQuestionRandom = testQuestionRandomList
												.get(rand
														.nextInt(testQuestionRandomList
																.size()));
										if (randomTestQuestnList
												.contains(testQuestionRandom)) {
											i--;
										} else {
											randomTestQuestnList
													.add(testQuestionRandom);
										}
									}
								}
							}else {
									
								List<TestQuestion> testQuestionRandomList = new ArrayList<TestQuestion>();
								testQuestionRandomList.addAll(testQuestions);
								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								randomTestQuestnList.addAll(testQuestionRandomList);
							}
						}
						//New Pool Changes

					}
				} else {
					randomTestQuestnList.addAll(testQuestions);
				}
				test.setTestQuestions(randomTestQuestnList);

				if (studentTestDB.getStudentQRespFilePath() == null) {
					List<StudentQuestionResponse> squestnResponseList = new ArrayList<>();
					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (TestQuestion tq : randomTestQuestnList) {

						StudentQuestionResponse sqr = new StudentQuestionResponse();

						sqr.setUsername(p.getName());
						sqr.setStudentTestId(studentTestDB.getId());
						sqr.setQuestionId(tq.getId());
						sqr.setLastModifiedBy(p.getName());
						sqr.setCreatedBy(p.getName());

						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
						squestnResponseList.add(sqr);

						sqrAuditList.add(sqrAudit);

					}
					/*
					 * studentQuestionResponseService .insertBatch(squestnResponseList);
					 * studentQuestionResponseService .insertBatchIntoTemp(sqrList);
					 * studentQuestionResponseAuditService .insertBatch(sqrAuditList);
					 */
					try {
						// JsonObject jsonObject = new
						// JsonObject(studentTestJsonObject);
						logger.info("file creation entered");
						String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
								testDb.getStartDate());
						String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getEndDate());
						String testDate = testSDate + "-" + testEDate;
						String folderPath = testBaseDir + "/" + app + "/" + "Tests";
						File folderP = new File(folderPath);
						if (!folderP.exists()) {
							folderP.mkdirs();
						}
						logger.info("folder created");
						String subFolderPath = folderPath + "/" + testDb.getId() + "-" + testDate;
						logger.info("subfolder created");
						File subFolderP = new File(subFolderPath);
						if (!subFolderP.exists()) {
							subFolderP.mkdir();
						}

						String studentFilePath = subFolderPath + "/" + p.getName() + ".json";
						logger.info("studentFilePat subfolder created");
						File fileP = new File(studentFilePath);

						ObjectMapper mapper = new ObjectMapper();

						// mapper.writeValueAsString(sqrList);
						mapper.writeValue(fileP, squestnResponseList);
						test.setStudentFilePath(studentFilePath);
						logger.info("student file path--->" + studentFilePath);
						String relacedStudentFilePath = studentFilePath.replaceAll("/", ";");
						m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
						m.addAttribute("studentFileP", studentFilePath);

						studentTestService.updateStudentTestResponseFilePath(studentFilePath, studentTestDB.getId());
						//New Pool Changes
						studentQuestionResponseService.insertBatch(squestnResponseList);
						//New Pool Changes
					} catch (Exception ex) {

						logger.error("Error in creating json object", ex);
					}

				} else {
					test.setStudentFilePath(studentTestDB.getStudentQRespFilePath());
					logger.info("student file path--->" + studentTestDB.getStudentQRespFilePath());
					String relacedStudentFilePath = studentTestDB.getStudentQRespFilePath().replaceAll("/", ";");
					m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
					m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath());
				}

				// test.setTestQuestions(testQuestions);

				test.setStudentTest(studentTestDB);
				/*
				 * if (studentTestDB.isCompleted()) setSuccess(m, "Test completed. You scored "
				 * + (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) +
				 * " marks out of " + test.getMaxScore()); else
				 */
				setSuccess(m, "Test has started");
			}

		} else {

			try {
				studentTestDB.setLastModifiedBy(p.getName());

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = testQuestionService
						.findStudentResponseByTestIdAndUsername(testDB.getId(), p.getName());
				test.setTestQuestions(testQuestions);
				test.setStudentTest(studentTestDB);
				// if (studentTestDB.isCompleted())
				setSuccess(m,
						"Test completed. You scored "
								+ (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore()) + " marks out of "
								+ test.getMaxScore());
				/*
				 * else setSuccess(m, "Test has started");
				 */

			} catch (Exception e) {
				logger.error("Error", e);
				setError(m, "Test could not be started");
				/* return "test/studentTest"; */
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
					return "test/studentTest";
				}

				else if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			}

			setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!");
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY) || userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/testList";
			} else {
				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";
			}
		}
		return "test/studentTestUpdatedForSubjective";
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/startStudentTestUpdatedForMix", method = RequestMethod.GET)
	public String startStudentTestUpdatedForMix(Principal p, RedirectAttributes redirectAttrs,
			@ModelAttribute Test test, Model m, @RequestParam("id") String testId) throws ParseException {
		m.addAttribute("webPage", new WebPage("studentTest", "Test", true, false));
		Test testDb = testService.findByID(Long.valueOf(testId));
		Token userdetails1 = (Token) p;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (null != testDb) {
			if ("N".equals(testDb.getActive())) {
				setNote(redirectAttrs, "Test has been deactivated");
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY) || userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			}
		}

		StudentTest studentTestDB = studentTestService.findBytestIDAndUsername(Long.valueOf(testId), p.getName());
		if (null == studentTestDB) {
			setNote(redirectAttrs, "You have not been assigned the test");
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY) || userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/testList";
			} else {
				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";
			}
		}
		// if (!studentTestDB.isCompleted()) {

		if ((((!studentTestDB.isCompleted() || studentTestDB.isCompleted()) && (studentTestDB.getAttempt() == 0))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY) || userDetails.getAuthorities().contains(Role.ROLE_ADMIN))
				&& null != studentTestDB.getStudentQRespFilePath() && "".equals(studentTestDB.getStudentQRespFilePath())
				&& null == studentTestDB.getTestStartTime()) {

			String allowAfterEndDate = testDb.getAllowAfterEndDate();

			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");

				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";

			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				if (!"Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getDurationCompleted() != null) {
						if (Integer.parseInt(studentTestDB.getDurationCompleted()) >= testDb.getDuration()) {
							testDb.setDuration(1);
						} else {
							testDb.setDuration(
									testDb.getDuration() - Integer.parseInt(studentTestDB.getDurationCompleted()));
						}
					}
				} else {
					studentTestDB.setDurationCompleted("0");
				}
				m.addAttribute("testTime", testDb.getDuration() * 60);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				String completionTime = "";
				int durationCompletedByStudent = 0;

				if (studentTestDB.getDurationCompleted() != null) {

					durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);

				List<TestQuestion> testQuestions = new ArrayList<>();

				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				List<TestQuestion> randomTestQuestnList = new ArrayList<>();

				if (studentQuestnResponseList.size() > 0) {

					testQuestions = testQuestionService
							.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(testDB.getId(), username);
					testQuestions = removeSpecialCharactersFromBean(testQuestions);

					randomTestQuestnList.addAll(testQuestions);

				} else {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							username);

					testQuestions = removeSpecialCharactersFromBean(testQuestions);

					if ("Y".equals(testDB.getRandomQuestion())) {

						if ("Y".equals(testDB.getSameMarksQue())) {

							for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}

						} else if ("N".equals(testDB.getSameMarksQue())) {

							List<TestConfiguration> testConfigList = testConfigurationService
									.findAllByTestId(Long.parseLong(testId));

							for (TestConfiguration tc : testConfigList) {

								List<TestQuestion> testQuestionRandomList = testQuestions.stream()
										.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());

								testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
								for (int i = 0; i < tc.getNoOfQuestion(); i++) {
									Random rand = new Random();
									TestQuestion testQuestionRandom = testQuestionRandomList
											.get(rand.nextInt(testQuestionRandomList.size()));
									if (randomTestQuestnList.contains(testQuestionRandom)) {
										i--;
									} else {
										randomTestQuestnList.add(testQuestionRandom);
									}
								}
							}

						}
					} else {
						randomTestQuestnList.addAll(testQuestions);
					}

				}
				test.setTestQuestions(randomTestQuestnList);

				test.setStudentFilePath(studentTestDB.getStudentQRespFilePath());
				logger.info("student file path--->" + studentTestDB.getStudentQRespFilePath());
				String relacedStudentFilePath = studentTestDB.getStudentQRespFilePath().replaceAll("/", ";");
				m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
				m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath());

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");

			}

		} else if (((!studentTestDB.isCompleted() || studentTestDB.isCompleted())
				&& (testDb.getMaxAttempt() > studentTestDB.getAttempt()))
				|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY) || userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String allowAfterEndDate = testDb.getAllowAfterEndDate();

			int chkStartandEndDateOfTest = studentTestService.chkStartandEndDateOfTests(p.getName(),
					Long.valueOf(testId));
			if (chkStartandEndDateOfTest == 0 && "N".equals(allowAfterEndDate)
					&& userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				setNote(redirectAttrs, "test has not started yet or deadline is missed!!");
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY) || userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					return "redirect:/testList";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			} else {

				m.addAttribute("action", "add");
				Date startDateTime = null;

				Date startTestTime = studentTestDB.getTestStartTime();
				Date completeAt = Utils.getCompletionTimeDateType(studentTestDB.getTestStartTime(),
						testDb.getDuration());

				if (startTestTime != null) {

					if ((completeAt.compareTo(Utils.getInIST())) > 0 && studentTestDB.getTestCompleted() == null) {
						startDateTime = studentTestDB.getTestStartTime();

					} else {
						startDateTime = Utils.getInIST();
					}

				} else {
					startDateTime = Utils.getInIST();
				}

				// Date startDateTime = Utils.getInIST();
				String startTestTimeFormat = Utils.changeTestStartTimeFormat(startDateTime);
				m.addAttribute("dateTime", startTestTimeFormat);

				studentTestDB.setTestStartTime(startDateTime);
				studentTestDB.setLastModifiedBy(p.getName());

				// test.setCompletionTime(studentTestDB.getTestStartTime(),testDb.getDuration());
				String completionTime = "";

				if (!"Y".equals(studentTestDB.getTestCompleted())) {
					if (studentTestDB.getDurationCompleted() != null) {
						if (Integer.parseInt(studentTestDB.getDurationCompleted()) >= testDb.getDuration()) {
							testDb.setDuration(1);
						} else {
							testDb.setDuration(
									testDb.getDuration() - Integer.parseInt(studentTestDB.getDurationCompleted()));
						}
					}
				} else {
					studentTestDB.setDurationCompleted("0");
				}

				m.addAttribute("testTime", testDb.getDuration() * 60);

				int durationCompletedByStudent = 0;
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
				} else {

					if (studentTestDB.getDurationCompleted() != null) {
						durationCompletedByStudent = Integer.parseInt(studentTestDB.getDurationCompleted());
						int durationForTest = testDb.getDuration();
						int remainingDurationForStudent = durationForTest - durationCompletedByStudent;

						completionTime = Utils.getCompletionTime(startDateTime, remainingDurationForStudent);
					} else {
						// durationCompletedByStudent = 0;
						completionTime = Utils.getCompletionTime(startDateTime, testDb.getDuration());
					}

				}

				m.addAttribute("durationCompletedByStudent", durationCompletedByStudent);

				m.addAttribute("durationsLeft", completionTime);

				if (studentTestDB.getScore() != null) {
					m.addAttribute("score", studentTestDB.getScore());
					if (testDb.getPassScore() < studentTestDB.getScore()) {
						String status = "PASS";
						m.addAttribute("status", status);
					} else {
						String status = "FAIL";
						m.addAttribute("status", status);
					}
				}
				if ("Y".equals(studentTestDB.getTestCompleted())) {
					List<StudentQuestionResponse> sqrList = studentQuestionResponseService
							.findByStudentTestId(String.valueOf(studentTestDB.getId()));
					if (sqrList.size() > 0) {
						for (StudentQuestionResponse sqr : sqrList) {
							sqr.setMarks(0.0);
							sqr.setAnswer(" ");
							studentQuestionResponseService.delete(sqr);
						}
					}
					if (studentTestDB.getStudentQRespFilePath() != null) {
						FileUtils.deleteQuietly(new File(studentTestDB.getStudentQRespFilePath()));
					}
					studentTestDB.setStudentQRespFilePath(null);
					studentTestDB.setTestCompleted(null);
					studentTestService.upsert(studentTestDB);
				} else {
					studentTestService.upsert(studentTestDB);
				}

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				/*
				 * List<TestQuestion> testQuestions = testQuestionService
				 * .findByTestId(testDB.getId());
				 */
				List<TestQuestion> testQuestions = new ArrayList<>();

				if (studentTestDB.getStudentQRespFilePath() != null) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String studentQRespFiles = FileUtils
								.readFileToString(new File(studentTestDB.getStudentQRespFilePath()));
						List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
								new TypeReference<List<StudentQuestionResponse>>() {
								});

						List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
						for (StudentQuestionResponse sqr : studentQRespList) {
							sqr.setAttemptNo(studentTestDB.getAttempt());
							StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr,
									studentTestDB.getTestId());
							sqrAuditList.add(sqrAudit);

						}
						studentQuestionResponseAuditService.insertBatch(sqrAuditList);

						studentQuestionResponseService.insertBatch(studentQRespList);
						studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);
					} catch (Exception ex) {
						setError(redirectAttrs, "Error in displaying test..");
						logger.error("Exception", ex);
						if(userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
							return "redirect:/testList";
						}else {
						return "redirect:/viewTestFinal";
						
						
						}
					}
				}

				List<StudentQuestionResponse> studentQuestnResponseList = studentQuestionResponseService
						.findByStudentTestId(String.valueOf(studentTestDB.getId()));

				if ("Y".equals(studentTestDB.getTestCompleted())) {
					testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
							username);
				} else {
					if (studentQuestnResponseList.size() > 0) {
						testQuestions = testQuestionService
								.findStudentResponseByTestIdAndUsernameForIncompleteTestTemp(testDB.getId(), username);
					} else {
						testQuestions = testQuestionService.findStudentResponseByTestIdAndUsername(testDB.getId(),
								username);
					}
				}

				testQuestions = removeSpecialCharactersFromBean(testQuestions);
				// .findStudentResponseByTestIdAndUsername(testDB.getId(),
				// p.getName());
				List<TestQuestion> randomTestQuestnList = new ArrayList<>();
				if ("Y".equals(testDB.getRandomQuestion())) {
					/*
					 * List<TestQuestion> testQuestionDes = new ArrayList<>(); List<TestQuestion>
					 * testQuestionMcq = new ArrayList<>(); List<TestQuestion> testQuestionRng = new
					 * ArrayList<>(); List<TestQuestion> testQuestionImg = new ArrayList<>();
					 * 
					 * for(TestQuestion tq : testQuestions){
					 * 
					 * if("MCQ".equals(tq.getQuestionType())){ testQuestionMcq.add(tq); }else
					 * if("Numeric".equals(tq.getQuestionType())){ testQuestionRng.add(tq); }else
					 * if("Descriptive".equals(tq.getQuestionType())){ testQuestionDes.add(tq);
					 * }else if("Image".equals(tq.getQuestionType())){ testQuestionImg.add(tq); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxDesQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionDes
					 * .get(rand.nextInt(testQuestionDes.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxMcqQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionMcq
					 * .get(rand.nextInt(testQuestionMcq.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxRngQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionRng
					 * .get(rand.nextInt(testQuestionRng.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 * 
					 * for (int i = 0; i < Integer.parseInt(testDb .getMaxImgQueToShow()); i++) {
					 * Random rand = new Random(); TestQuestion testQuestionRandom = testQuestionImg
					 * .get(rand.nextInt(testQuestionImg.size())); if
					 * (randomTestQuestnList.contains(testQuestionRandom)) { i--; } else {
					 * randomTestQuestnList.add(testQuestionRandom); } }
					 */

					if ("Y".equals(testDB.getSameMarksQue())) {

						for (int i = 0; i < Integer.parseInt(testDb.getMaxQuestnToShow()); i++) {
							Random rand = new Random();
							TestQuestion testQuestionRandom = testQuestions.get(rand.nextInt(testQuestions.size()));
							if (randomTestQuestnList.contains(testQuestionRandom)) {
								i--;
							} else {
								randomTestQuestnList.add(testQuestionRandom);
							}
						}

					} else if ("N".equals(testDB.getSameMarksQue())) {

						List<TestConfiguration> testConfigList = testConfigurationService
								.findAllByTestId(Long.parseLong(testId));

						for (TestConfiguration tc : testConfigList) {

							List<TestQuestion> testQuestionRandomList = testQuestions.stream()
									.filter(o -> o.getMarks().equals(tc.getMarks())).collect(Collectors.toList());
							testQuestionRandomList = removeSpecialCharactersFromBean(testQuestionRandomList);
							for (int i = 0; i < tc.getNoOfQuestion(); i++) {
								Random rand = new Random();
								TestQuestion testQuestionRandom = testQuestionRandomList
										.get(rand.nextInt(testQuestionRandomList.size()));
								if (randomTestQuestnList.contains(testQuestionRandom)) {
									i--;
								} else {
									randomTestQuestnList.add(testQuestionRandom);
								}
							}
						}

					}

				} else {
					randomTestQuestnList.addAll(testQuestions);
				}
				test.setTestQuestions(randomTestQuestnList);

				if (studentTestDB.getStudentQRespFilePath() == null) {
					List<StudentQuestionResponse> sqrList = new ArrayList<>();
					List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
					for (TestQuestion tq : randomTestQuestnList) {

						StudentQuestionResponse sqr = new StudentQuestionResponse();

						sqr.setUsername(username);
						sqr.setStudentTestId(studentTestDB.getId());
						sqr.setQuestionId(tq.getId());
						sqr.setLastModifiedBy(username);
						sqr.setCreatedBy(username);

						StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
						sqrList.add(sqr);
						sqrAuditList.add(sqrAudit);

					}
					/*
					 * studentQuestionResponseService.insertBatch(sqrList);
					 * studentQuestionResponseService .insertBatchIntoTemp(sqrList);
					 * studentQuestionResponseAuditService .insertBatch(sqrAuditList);
					 */

					try {
						// JsonObject jsonObject = new
						// JsonObject(studentTestJsonObject);
						logger.info("file creation entered");
						String testSDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy",
								testDb.getStartDate());
						String testEDate = Utils.formatDate("yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy", testDb.getEndDate());
						String testDate = testSDate + "-" + testEDate;
						String folderPath = testBaseDir + "/" + app + "/" + "Tests";
						File folderP = new File(folderPath);
						if (!folderP.exists()) {
							folderP.mkdirs();
						}
						logger.info("folder created");
						String subFolderPath = folderPath + "/" + testDb.getId() + "-" + testDate;
						logger.info("subfolder created");
						File subFolderP = new File(subFolderPath);
						if (!subFolderP.exists()) {
							subFolderP.mkdir();
						}

						String studentFilePath = subFolderPath + "/" + username + ".json";
						logger.info("studentFilePat subfolder created");
						File fileP = new File(studentFilePath);

						ObjectMapper mapper = new ObjectMapper();

						// mapper.writeValueAsString(sqrList);
						mapper.writeValue(fileP, sqrList);
						test.setStudentFilePath(studentFilePath);
						logger.info("student file path--->" + studentFilePath);
						String relacedStudentFilePath = studentFilePath.replaceAll("/", ";");
						m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
						m.addAttribute("studentFileP", studentFilePath);

						studentTestService.updateStudentTestResponseFilePath(studentFilePath, studentTestDB.getId());
						//New Pool Changes
						studentQuestionResponseService.insertBatch(sqrList);
						//New Pool Changes
					} catch (Exception ex) {

						logger.error("Error in creating json object", ex);
					}
				} else {
					test.setStudentFilePath(studentTestDB.getStudentQRespFilePath());
					logger.info("student file path--->" + studentTestDB.getStudentQRespFilePath());
					String relacedStudentFilePath = studentTestDB.getStudentQRespFilePath().replaceAll("/", ";");
					m.addAttribute("relacedStudentFilePath", relacedStudentFilePath);
					m.addAttribute("studentFileP", studentTestDB.getStudentQRespFilePath());
				}

				test.setStudentTest(studentTestDB);

				setSuccess(m, "Test has started");
			}

		} else {

			try {
				studentTestDB.setLastModifiedBy(p.getName());

				studentTestService.upsert(studentTestDB);

				Test testDB = testService.findByID(test.getId());
				LMSHelper.copyNonNullFields(test, testDB);
				List<TestQuestion> testQuestions = testQuestionService
						.findStudentResponseByTestIdAndUsername(testDB.getId(), p.getName());
				test.setTestQuestions(testQuestions);
				test.setStudentTest(studentTestDB);
				// if (studentTestDB.isCompleted())
				if ("Y".equals(testDB.getShowResultsToStudents())) {

					setNote(redirectAttrs,
							"Test completed. You scored "
									+ (studentTestDB.getScore() == null ? 0 : studentTestDB.getScore())
									+ " marks out of " + test.getMaxScore());
				} else {

					setNote(redirectAttrs, "Test completed. Test Score will be displayed soon ");

				}

			} catch (Exception e) {
				logger.error("Error", e);
				setError(m, "Test could not be started");
				/* return "test/studentTest"; */
				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
					return "test/studentTest";
				} else {
					redirectAttrs.addAttribute("courseId", testDb.getCourseId());
					return "redirect:/viewTestFinal";
				}
			}
			setNote(redirectAttrs, "You have exceeded maximum permitted attempts!!");
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)  || userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "redirect:/testList";
			} 
			
			else {
				redirectAttrs.addAttribute("courseId", testDb.getCourseId());
				return "redirect:/viewTestFinal";
			}
		}
		return "test/studentTestUpdatedForMix";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/getIncompleteTestAndConfigure", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String getIncompleteTestAndConfigure(@RequestParam String testId) {

		Test testDB = testService.findByID(Long.valueOf(testId));

		if (testDB != null) {
			List<StudentTest> getIncompleteStudentTest = studentTestService.getIncompleteStudentTest(testId);
			if (getIncompleteStudentTest.size() == 0) {
				return "No Students Pending";
			} else {
				List<String> respList = new ArrayList<>();
				for (StudentTest st : getIncompleteStudentTest) {

					String resp = configureStudentTestAndResponse(String.valueOf(st.getId()), testDB.getTestType(),
							st.getStudentQRespFilePath());

					respList.add(resp);
				}
				String respListJson = new Gson().toJson(respList);

				return respListJson;
			}
		} else {
			return "No Test Found";
		}

	}

	public String configureStudentTestAndResponse(@RequestParam String studentTestId, @RequestParam String testType,
			@RequestParam String studentFilePath) {

		Gson g = new Gson();

		try {

			ObjectMapper mapper = new ObjectMapper();
			// studentFilePath = studentFilePath.replaceAll(";", "/");

			String studentQRespFiles = FileUtils.readFileToString(new File(studentFilePath));
			List<StudentQuestionResponse> studentQRespList = mapper.readValue(studentQRespFiles,
					new TypeReference<List<StudentQuestionResponse>>() {
					});

			if (studentQRespList.size() > 0) {
				studentQuestionResponseService.insertBatchIntoTemp(studentQRespList);

				StudentTest studentTestDB = studentTestService.callCompleteStudentTest(studentTestId, testType);

				List<StudentQuestionResponseAudit> sqrAuditList = new ArrayList<>();
				for (StudentQuestionResponse sqr : studentQRespList) {
					sqr.setAttemptNo(studentTestDB.getAttempt() - 1);

					StudentQuestionResponseAudit sqrAudit = responseBeanToAuditBean(sqr, studentTestDB.getTestId());
					sqrAudit.setMarks(sqr.getMarks());
					sqrAuditList.add(sqrAudit);

				}
				studentQuestionResponseAuditService.insertBatch(sqrAuditList);

				return "Successfully Configured-" + studentTestId;
			} else {
				return "No Response-" + studentTestId;
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			return "Error while Configuring";
		}

	}

	// Ended
	
	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/viewTestResultQuestionwise", method = {RequestMethod.GET, RequestMethod.POST})
	public String viewTestResultQuestionwise(@RequestParam(required = false) Long testId,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		String username = principal.getName();

		Test test = testService.findByID(testId);
		m.addAttribute("testDetails",test);
		List<TestQuestion> testQuestionDetailsWithResponse = testQuestionService.findTestDetailsQuestionWiseByUsername(Long.valueOf(testId), username);
		m.addAttribute("testQuestionDetailsWithResponse", testQuestionDetailsWithResponse);
		
		return "test/viewTestResultQuestionwise";
	}
}
