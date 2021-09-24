package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.beans.feedback.FeedbackQuestion;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.feedback.StudentFeedbackRecord;
import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.dashboard.DashboardService;
import com.spts.lms.services.feedback.FeedbackQuestionService;
import com.spts.lms.services.feedback.FeedbackService;
import com.spts.lms.services.feedback.StudentFeedbackResponseService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;


@Controller
public class FeedbackController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	FeedbackService feedbackService;

	@Autowired
	UserService userService;

	@Autowired
	ProgramService programService;

	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	FeedbackQuestionService feedbackQuestionService;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Autowired
	StudentFeedbackResponseService studentFeedbackResponseService;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	DashboardService dashBoardService;

	@Autowired
	HttpSession session;

	@Autowired
	LmsVariablesService lmsVariablesService;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${file.base.directory}")
	private String baseDir;

	@Value("${app}")
	private String app;

	private static final Logger logger = Logger.getLogger(FeedbackController.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addFeedbackForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addFeedbackForm(@ModelAttribute Feedback feedback, Model m, Principal principal) {

		m.addAttribute("webPage", new WebPage("feedback", "Create Feedback", false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		LmsVariables makeLive = lmsVariablesService.getLmsVariableBykeyword("feedbackTypeMakeLive");
		m.addAttribute("feedbackTypeMakeLive", makeLive.getValue());

		if (feedback.getId() != null) {
			Feedback feedbackDB = feedbackService.findByID(feedback.getId());
			if (feedbackDB == null) {
				setError(m, "Feedback " + feedback.getId() + " does not exist");
				feedback.setId(null);
			} else {
				LMSHelper.copyNonNullFields(feedback, feedbackDB);
				Feedback dateFeedback = feedbackService.getDatesForFeedback(feedbackDB.getId());
				if (dateFeedback != null) {
					String startDate = dateFeedback.getStartDate();
					String endDate = dateFeedback.getEndDate();
					feedbackDB.setStartDate(startDate);
					feedbackDB.setEndDate(endDate);
				}
				if (studentFeedbackService.getAllocatedStudentFeedback(String.valueOf(feedback.getId())).size() > 0) {
					m.addAttribute("showDates", true);
				}
				m.addAttribute("feedback", feedbackDB);
				m.addAttribute("edit", "true");
			}
		}
		return "feedback/addFeedback";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addFeedback", method = RequestMethod.POST)
	public String addFeedback(@ModelAttribute Feedback feedback, RedirectAttributes redirectAttrs, Principal principal,
			Model m) {
		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			feedback.setCreatedBy(username);
			feedback.setLastModifiedBy(username);

			feedbackService.insertWithIdReturn(feedback);
			redirectAttrs.addAttribute("feedbackId", feedback.getId());

			if ((app.trim().equals("SBM-NM-M")) || (app.trim().equals("PDSEFBM"))
					|| ("mid-term".equals(feedback.getFeedbackType()))) {
				setNote(redirectAttrs, "Please Configure Questions.");
			} else {
				List<FeedbackQuestion> questionList = feedbackQuestionService.getAllActiveFeedbackTemplateQuestions();
				for (FeedbackQuestion fq : questionList) {
					fq.setFeedbackId(feedback.getId());
				}
				feedbackQuestionService.insertBatch(questionList);
				setNote(redirectAttrs, "Template Questions Added Successfully");
			}

			setSuccess(redirectAttrs, "Feedback added successfully");

			/*
			 * ClassLoader cl = getClass().getClassLoader(); File feedbackQuestionFile = new
			 * File(cl.getResource("./static/resources/templates/finalFeedbackQuestion.xlsx"
			 * ).getFile());
			 */

			/*
			 * setNote(redirectAttrs,
			 * "Feedback Questions Configured Automatically after successful creation of Feedback"
			 * );
			 */

			/*
			 * uploadFinalFeedbackQuestion(feedback, feedbackQuestionFile, m, redirectAttrs,
			 * principal);
			 */
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Feedback");
			return "redirect:/addFeedbackForm";
		}
		return "redirect:/viewFeedbackDetails";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/uploadFinalFeedbackQuestion", method = { RequestMethod.POST })
	public String uploadFinalFeedbackQuestion(@ModelAttribute Feedback feedback, @RequestParam("file") File file,
			Model m, RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("uploadFeedbackQuestion", "Upload Feedback Question", false, false));
		List<String> validateHeaders = null;

		Feedback feedbackDb = feedbackService.findByID(feedback.getId());
		validateHeaders = new ArrayList<String>(Arrays.asList("description"));

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
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnNum(file.getAbsolutePath());

			List<FeedbackQuestion> feedbackQuestionPrevious = feedbackQuestionService
					.findByFeedbackId(feedback.getId());

			if (feedbackQuestionPrevious.size() > 0) {

				feedbackQuestionService.deleteBatch(feedbackQuestionPrevious);

			}

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {

						setNote(m, "Error--->" + mapper.get("Error"));
					} else {
						FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
						feedbackQuestion.setFeedbackId(feedback.getId());
						feedbackQuestion.setOption1("1");
						feedbackQuestion.setOption2("2");
						feedbackQuestion.setOption3("3");
						feedbackQuestion.setOption4("4");
						feedbackQuestion.setOption5("5");
						feedbackQuestion.setOption6("6");
						feedbackQuestion.setOption7("7");

						feedbackQuestion.setCreatedBy(username);
						feedbackQuestion.setLastModifiedBy(username);
						feedbackQuestion.setCreatedBy(username);
						feedbackQuestion.setActive("Y");
						feedbackQuestion.setType("SINGLESELECT");

						mapper.put("feedbackId", feedback.getId());
						mapper.put("createdBy", feedbackQuestion.getCreatedBy());
						mapper.put("lastModifiedBy", feedbackQuestion.getLastModifiedBy());

						mapper.put("active", feedbackQuestion.getActive());
						mapper.put("option1", feedbackQuestion.getOption1());
						mapper.put("option2", feedbackQuestion.getOption2());
						mapper.put("option3", feedbackQuestion.getOption3());
						mapper.put("option4", feedbackQuestion.getOption4());
						mapper.put("option5", feedbackQuestion.getOption5());
						mapper.put("option6", feedbackQuestion.getOption6());
						mapper.put("option7", feedbackQuestion.getOption7());
						mapper.put("option8", null);
						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());
						mapper.put("type", feedbackQuestion.getType());
						mapper.put("description", mapper.get("0").toString().trim());

						if (!mapper.get("description").toString().trim().isEmpty()) {
							feedbackQuestionService.insertUsingMap(mapper);
						}
						setSuccess(m, "feedback question file uploaded successfully");
						m.addAttribute("showProceed", true);
					}
				}
			}

			m.addAttribute("feedback", feedback);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "redirect:/viewFeedbackDetails";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/updateFeedback", method = RequestMethod.POST)
	public String updateFeedback(@ModelAttribute Feedback feedback, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		redirectAttrs.addAttribute("feedbackId", feedback.getId());
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {

			Feedback feedbackFromDb = feedbackService.findByID(feedback.getId());
			String oldFeedType = feedbackFromDb.getFeedbackType();
			feedbackFromDb.setFeedbackName(feedback.getFeedbackName());
			feedbackFromDb.setFeedbackType(feedback.getFeedbackType());
			feedbackFromDb.setLastModifiedBy(username);
			if (feedback.getStartDate() != null || feedback.getEndDate() != null) {
				Feedback getDateFb = feedbackService.getDatesForFeedback(feedback.getId());
				StudentFeedback ff = new StudentFeedback();
				ff.setFeedbackId(feedback.getId());
				ff.setStartDate(feedback.getStartDate());
				ff.setEndDate(feedback.getEndDate());
				
				Date startDate = Utils.converFormatsDate(feedback.getStartDate());
				Date endDate = Utils.converFormatsDate(feedback.getEndDate());
				Date currDate = Utils.getInIST();
				
				System.out.println("end date--->"+feedbackFromDb.getEndDate());
				Date oldEndDt = Utils.converFormatsDate(getDateFb.getEndDate());
				
				System.out.println("s Date"+startDate);
				System.out.println("e Date"+endDate);
				System.out.println("c Date"+currDate);
				System.out.println("ec Date"+oldEndDt);
				
				if(startDate.after(endDate)) {
					setError(redirectAttrs, "Date Range is Not Valid");
					return "redirect:/addFeedbackForm";
				}
				if(currDate.before(oldEndDt)) {
					setError(redirectAttrs, "Feedback Should Be Extended After Deadline is Over!!");
					return "redirect:/addFeedbackForm";
				}
				
				ff.setLastModifiedBy(username);
				int updatecount = 0;
				if (!"Y".equals(feedbackFromDb.getIsPublished())) {
					updatecount = studentFeedbackService.updateDates(ff);
				}

				if (updatecount > 0) {

					setSuccess(redirectAttrs, "Feedback Date updated successfully");
				} else {

					if ("Y".equals(feedbackFromDb.getIsPublished())) {
						setError(redirectAttrs, "Feedback Date cannot be updated,It is already published");
					} else {
						setError(redirectAttrs, "Feedback Date cannot be updated");
					}
				}
			} else {

				if (!oldFeedType.equals(feedbackFromDb.getFeedbackType())) {
					List<FeedbackQuestion> feedbackQuestionPrevious = feedbackQuestionService
							.findByFeedbackId(feedback.getId());

					if (feedbackQuestionPrevious.size() > 0) {

						feedbackQuestionService.deleteBatch(feedbackQuestionPrevious);

					}
					
					
					if("end-term".equals(feedbackFromDb.getFeedbackType()) 
							&& !app.trim().equals("SBM-NM-M") && !app.trim().equals("PDSEFBM")) {
								
						List<FeedbackQuestion> questionList = feedbackQuestionService
								.getAllActiveFeedbackTemplateQuestions();
						for (FeedbackQuestion fq : questionList) {
							fq.setFeedbackId(feedback.getId());
						}
						feedbackQuestionService.insertBatch(questionList);
					}
					
				}
				
				int updateFeedback = feedbackService.update(feedbackFromDb);

				if (updateFeedback > 0)
					setSuccess(redirectAttrs, "Feedback updated successfully");
				else
					setError(redirectAttrs, "Feedback cannot be updated");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Feedback");
			return "redirect:/addFeedbackForm";
		}
		return "redirect:/viewFeedbackDetails";
	}

	/*
	 * @RequestMapping(value = "/viewFeedbackDetails", method = RequestMethod.GET)
	 * public String viewFeedbackDetails(
	 * 
	 * @RequestParam(required = false) Long feedbackId,@RequestParam(required =
	 * false) Long courseId,
	 * 
	 * @ModelAttribute StudentFeedback studentFeedback, Model m, Principal
	 * principal,
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo) {
	 * m.addAttribute("webPage", new WebPage("viewFeedback", "Feedback Details",
	 * true, true)); try { UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) principal; Feedback feedbackFromDb =
	 * feedbackService.findByID(feedbackId); m.addAttribute("feedback",
	 * feedbackFromDb);
	 * 
	 * // ----------------------------------
	 * 
	 * String username = principal.getName();
	 * 
	 * Page<StudentFeedback> page = studentFeedbackService
	 * .getStudentFeedbackResponse(String.valueOf(feedbackId), pageNo, pageSize);
	 * 
	 * Page<StudentFeedback> page = null;
	 * 
	 * if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) { page=
	 * studentFeedbackService
	 * .getStudentFeedbackResponse(String.valueOf(feedbackId), pageNo, pageSize);
	 * }else if(userDetails.getAuthorities().contains(Role.ROLE_STUDENT)){ page =
	 * studentFeedbackService .getStudentFeedbackResponseByUsername(username,
	 * pageNo, pageSize);
	 * 
	 * }
	 * 
	 * List<StudentFeedback> programList = page.getPageItems();
	 * 
	 * StudentFeedback studentFeedbackStatus = studentFeedbackService
	 * .getStudentFeedbackStatus(String.valueOf(feedbackId), username);
	 * m.addAttribute("studentFeedbackStatus", studentFeedbackStatus);
	 * System.out.println("page size:-----------is sis sisis si sis si-->" +
	 * programList.get(0).getFeedbackCompleted() + "---------" + feedbackId +
	 * "--------" + username + "-statuss is s---" +
	 * studentFeedbackStatus.getFeedbackCompleted()); System.out.println("data is "
	 * + toString()); UserCourse userCourse = new UserCourse();
	 * 
	 * // System.out.println(" faculty is :" + feedback.getFacultyId());
	 * 
	 * m.addAttribute("feedbackResponse", studentFeedback); m.addAttribute("page",
	 * page); m.addAttribute("q", getQueryString(studentFeedback));
	 * 
	 * m.addAttribute("announcmentList",
	 * dashBoardService.listOfAnnouncementsForCourseList(username,courseId));
	 * m.addAttribute("toDoList", dashBoardService.getToDoList(username));
	 * 
	 * // ---------------------------------
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Error in updating Feedback"); //return "feedback/feedbackDetails"; return
	 * "feedback/feedbackDetails"; } //return "feedback/feedbackDetails"; return
	 * "feedback/feedbackDetails"; }
	 */

	/*
	 * @RequestMapping(value = "/searchStudentFeedbackResponse", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * searchStudentFeedbackResponse(@RequestParam(required=false) Long
	 * feedbackId,Principal p, Model m,@RequestParam(required = false, defaultValue
	 * = "1") int pageNo) { String username=p.getName();
	 * logger.info("Add New Feedback Page"); m.addAttribute("webPage", new
	 * WebPage("searchStudentResponse", "Student Feedback Resonse", true, false));
	 * Long pId=null; Feedback feedback = feedbackService.findByID(feedbackId);
	 * m.addAttribute("feedback", feedback); List<StudentFeedback>
	 * studentFeedbackByFaculty =
	 * studentFeedbackService.findStudentFeedbackByFaculty(username);
	 * List<StudentFeedbackRecord> sfRecords = new ArrayList<>(); User user =
	 * userService.findByUserName(username); String facType =
	 * userService.getFacultyType(username); logger.info("user---------"+ user);
	 * m.addAttribute("user", user); int totalRate =0; int average =0;
	 * logger.info(""); Map<String,Integer> mapOfCourseIdAndStudents = new
	 * HashMap<>(); for(StudentFeedback sfFaculty : studentFeedbackByFaculty){
	 * StudentFeedbackRecord sfrecord = new StudentFeedbackRecord();
	 * List<StudentFeedback> studentsGaveFeedback =
	 * studentFeedbackService.getStudentGaveFeedback
	 * (String.valueOf(sfFaculty.getCourseId())); List<StudentFeedback>
	 * studentAllocatedFeedback =
	 * studentFeedbackService.getNoOfStudentAllocatedFeedback
	 * (String.valueOf(sfFaculty.getCourseId())); Course c =
	 * courseService.findByID(sfFaculty.getCourseId()); String term =
	 * courseService.getTerms(String.valueOf(sfFaculty.getCourseId()));
	 * sfrecord.setCourseName(c.getCourseName());
	 * sfrecord.setCourse(c.getCourseName()); StudentFeedback studfeedback =
	 * studentFeedbackService
	 * .findByfeedbackIDAndUsername(sfFaculty.getFeedbackId(),
	 * sfFaculty.getUsername());
	 * sfrecord.setProgramId(String.valueOf(c.getProgramId()));
	 * sfrecord.setFacultyName(user.getFirstname()+" "+user.getLastname());
	 * List<FeedbackQuestion> fq =
	 * feedbackQuestionService.findByFeedbackId(sfFaculty.getFeedbackId());
	 * List<StudentFeedbackResponse> sfr =
	 * studentFeedbackResponseService.findStudentFeedbackResponseByUsername
	 * (studfeedback.getId(),sfFaculty.getUsername()); for(StudentFeedbackResponse
	 * sr : sfr){ totalRate = totalRate + Integer.parseInt(sr.getAnswer());
	 * logger.info("totalRate"+ totalRate); average = totalRate/fq.size(); }
	 * sfrecord.setAverage(String.valueOf(average)); sfrecord.setArea("NA");
	 * sfrecord.setCoreVisitingFaculty(facType);
	 * sfrecord.setTerm(c.getAcadMonth()+"/"+c.getAcadYear());
	 * sfrecord.setTrimester(term); sfrecord.setTotalNoOfStudentGaveFeedback(String
	 * .valueOf(studentsGaveFeedback.size())); sfrecord.setTotalNoOfStudents(String
	 * .valueOf(studentAllocatedFeedback.size()));;
	 * sfrecord.setGrandAverage(String.valueOf(average)); totalRate=0;
	 * sfRecords.add(sfrecord); }
	 * 
	 * m.addAttribute("sfrecord", sfRecords);
	 * m.addAttribute("sfrecordSize",sfRecords.size()); return
	 * "feedback/feedbackRates";
	 * 
	 * }
	 */

	@Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
	@RequestMapping(value = "/searchStudentFeedbackResponseForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchStudentFeedbackResponseForm(Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("sfResponseList", "Feedback Record", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		String progName = userDetails.getProgramName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<String> programsIds = programService.findProgramIdByProgramName(progName);
		List<String> facultyList = new ArrayList<String>();
		List<User> userList = new ArrayList<User>();
		for (String programId : programsIds) {
			facultyList = courseService.findFacultiesByProgram(programId);

			for (String s : facultyList) {
				User user = new User();
				user.setUsername(s);
				userList.add(user);
			}
		}
		m.addAttribute("listOfFaculty", facultyList);
		m.addAttribute("userList", userList);
		m.addAttribute("showFeedbackRecords", false);
		return "feedback/feedbackRates";
	}

	@Secured({"ROLE_ADMIN","ROLE_LIBRARIAN"})
	@RequestMapping(value = "/searchStudentFeedbackResponse", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchStudentFeedbackResponse(@RequestParam String facultyId,
			@RequestParam(required = false) Long feedbackId, Principal p, Model m,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {
		String username = facultyId;

		m.addAttribute("webPage", new WebPage("searchStudentResponse", "Student Feedback Resonse", false, false));
		// String programId = "50500647";
		Token userDetails = (Token) p;
		String progName = userDetails.getProgramName();
		List<String> programsIds = programService.findProgramIdByProgramName(progName);
		List<String> facultyList = new ArrayList<String>();
		List<User> userList = new ArrayList<User>();
		for (String programId : programsIds) {
			facultyList = courseService.findFacultiesByProgram(programId);

			for (String s : facultyList) {
				User user = new User();
				user.setUsername(s);
				userList.add(user);
			}
		}
		Feedback feedback = feedbackService.findByID(feedbackId);
		m.addAttribute("feedback", feedback);
		List<StudentFeedback> studentFeedbackByFaculty = studentFeedbackService.findStudentFeedbackByFaculty(username);
		List<StudentFeedbackRecord> sfRecords = new ArrayList<>();
		User user = userService.findByUserName(username);
		String facType = userService.getFacultyType(username);

		m.addAttribute("facultyId", facultyId);
		m.addAttribute("userList", userList);
		m.addAttribute("user", user);

		int totalRate = 0;
		int average = 0;
		Map<String, Integer> mapOfCourseIdAndStudents = new HashMap<>();
		for (StudentFeedback sfFaculty : studentFeedbackByFaculty) {
			StudentFeedbackRecord sfrecord = new StudentFeedbackRecord();
			List<StudentFeedback> studentsGaveFeedback = studentFeedbackService.getStudentGaveFeedback(
					String.valueOf(sfFaculty.getCourseId()), String.valueOf(sfFaculty.getFeedbackId()));
			List<StudentFeedback> studentAllocatedFeedback = studentFeedbackService.getNoOfStudentAllocatedFeedback(
					String.valueOf(sfFaculty.getCourseId()), String.valueOf(sfFaculty.getFeedbackId()));
			Course c = courseService.findByID(sfFaculty.getCourseId());
			String term = courseService.getTerms(String.valueOf(sfFaculty.getCourseId()));
			sfrecord.setCourseName(c.getCourseName());
			sfrecord.setCourse(c.getCourseName());
			/*
			 * List<StudentFeedback> studfeedback = studentFeedbackService
			 * .findByfeedbackIDAndUsername(sfFaculty.getFeedbackId(),
			 * sfFaculty.getUsername());
			 */
			sfrecord.setProgramId(String.valueOf(c.getProgramId()));
			sfrecord.setFacultyName(user.getFirstname() + " " + user.getLastname());
			List<FeedbackQuestion> fq = feedbackQuestionService.findByFeedbackId(sfFaculty.getFeedbackId());
			List<StudentFeedbackResponse> sfr = new ArrayList<>();
			for (StudentFeedback sf : studentsGaveFeedback) {
				sfr = studentFeedbackResponseService.findStudentFeedbackResponseByUsername(sf.getId(),
						sf.getUsername());
			}
			for (StudentFeedbackResponse sr : sfr) {
				if (!sr.getAnswer().isEmpty()) {
					totalRate = totalRate + Integer.parseInt(sr.getAnswer());
				}

				average = totalRate / fq.size();
			}
			sfrecord.setAverage(String.valueOf(average));
			sfrecord.setArea("NA");
			sfrecord.setCoreVisitingFaculty(facType);
			sfrecord.setTerm(c.getAcadMonth() + "/" + c.getAcadYear());
			sfrecord.setTrimester(term);
			sfrecord.setTotalNoOfStudentGaveFeedback(String.valueOf(studentsGaveFeedback.size()));
			sfrecord.setTotalNoOfStudents(String.valueOf(studentAllocatedFeedback.size()));

			sfrecord.setGrandAverage(String.valueOf(average));
			totalRate = 0;
			sfRecords.add(sfrecord);
		}

		m.addAttribute("sfrecord", sfRecords);
		m.addAttribute("sfrecordSize", sfRecords.size());
		m.addAttribute("showFeedbackRecords", true);
		return "feedback/feedbackRates";

	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentFeedbackResponseForCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody StudentFeedbackResponse addStudentFeedbackResponseForCourse(@RequestParam String feedbackId,
			Model m, @ModelAttribute Feedback feedback, Principal principal, RedirectAttributes redirectAttrs) {
		try {
			String username = principal.getName();
			m.addAttribute("webPage", new WebPage("feedbackListForMultipleCourses", "Take Feedback", false, false));
			// StudentFeedback sf =
			// studentFeedbackService.getStudentFeedbackIdByCourseAndFeedbackId(courseId,feedbackId,username);

			List<FeedbackQuestion> feedbackQuestion = feedback.getFeedbackQuestions();

			List<StudentFeedbackResponse> sfrList = new ArrayList<>();
			feedback.setId(Long.valueOf(feedbackId));
			logger.info("feedback questions size---" + feedbackQuestion.size());
			StudentFeedbackResponse studentFeedbackResponse = new StudentFeedbackResponse();
			for (FeedbackQuestion fq : feedbackQuestion) {

				studentFeedbackResponse = fq.getStudentFeedbackResponse();
				studentFeedbackResponse.setUsername(username);

				studentFeedbackResponse.setCreatedBy(username);
				studentFeedbackResponse.setLastModifiedBy(username);
				// studentFeedbackResponse.setStudentFeedbackId(sf.getId());

				studentFeedbackResponseService.insert(studentFeedbackResponse);

				StudentFeedback st = studentFeedbackService.findByCourseIdAndFeedback(feedback.getId().toString(),
						username, studentFeedbackResponse.getCourseId(),
						studentFeedbackResponse.getStudentFeedbackId());

				int studentFeedbackCompletionCheck = studentFeedbackResponseService
						.checkFeedbackCompletionStatusByCourse(feedback.getId(),
								studentFeedbackResponse.getStudentFeedbackId(), username);
				if (studentFeedbackCompletionCheck == 1) {

					st.setFeedbackCompleted("Y");

					studentFeedbackService.upsert(st);
				}

			}

			setSuccess(redirectAttrs, "feedback saved successfully");
			//
			// return "redirect:/giveStudentFeedback?feedbackId=" +
			// feedback.getId() + " ";

			return studentFeedbackResponse;

		} catch (Exception e) {
			logger.info("error" + e);
			setError(m, "Error in saving question");
			return null;
		}
	}

	/*
	 * @RequestMapping(value = "/viewFeedbackDetails", method = RequestMethod.GET)
	 * public String viewFeedbackDetails(
	 * 
	 * @RequestParam(required = false) Long feedbackId,
	 * 
	 * @ModelAttribute Feedback feedback, Model m, Principal principal,
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo) {
	 * m.addAttribute("webPage", new WebPage("viewFeedback", "Feedback Details",
	 * true, true)); try { UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) principal; Feedback feedbackFromDb =
	 * feedbackService.findByID(feedbackId); m.addAttribute("feedback",
	 * feedbackFromDb);
	 * 
	 * // ----------------------------------
	 * 
	 * String username = principal.getName();
	 * 
	 * Page<StudentFeedback> page = null; Map<String, List<FeedbackQuestion>>
	 * mapOfFeedIdAndResponses = new HashMap<>(); if
	 * (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) { page =
	 * studentFeedbackService.getStudentFeedbackResponse(
	 * String.valueOf(feedbackId), pageNo, pageSize); } else if
	 * (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) { page =
	 * studentFeedbackService .getStudentFeedbackResponseByUsername(username,
	 * pageNo, pageSize);
	 * 
	 * List<StudentFeedback> programList = page.getPageItems();
	 * logger.info("feedback list of students----" + programList); for
	 * (StudentFeedback sf : programList) { StudentFeedback studentFeedbackStatus =
	 * studentFeedbackService .getStudentFeedbackStatus(
	 * String.valueOf(sf.getFeedbackId()), username); if
	 * (sf.getFeedbackCompleted().equalsIgnoreCase("Y")) { Page<FeedbackQuestion>
	 * pageResp = feedbackQuestionService .getStudentFeedbackResponsePage(
	 * sf.getFeedbackId(), username, pageNo, pageSize); String feedId =
	 * String.valueOf(sf.getFeedbackId()); List<FeedbackQuestion> programListResp =
	 * pageResp .getPageItems();
	 * 
	 * mapOfFeedIdAndResponses.put( String.valueOf(sf.getFeedbackId()),
	 * programListResp); logger.info("feedId---" + feedId); logger.info("list for" +
	 * feedId + programListResp); logger.info("map of id and list" +
	 * mapOfFeedIdAndResponses);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * logger.info("data passed to the ---->" + page.toString()); UserCourse
	 * userCourse = new UserCourse(); m.addAttribute("feedResponseMap",
	 * mapOfFeedIdAndResponses); m.addAttribute("feedbackResponse",
	 * studentFeedback); m.addAttribute("page", page); m.addAttribute("q",
	 * getQueryString(studentFeedback));
	 * 
	 * // ---------------------------------
	 * 
	 * if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * 
	 * return "feedback/feedbackDetailsForStudents"; }
	 * 
	 * }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Error in updating Feedback"); return "feedback/feedbackDetails"; } return
	 * "feedback/feedbackDetails"; }
	 */

	@Secured({ "ROLE_STUDENT", "ROLE_ADMIN" })
	@RequestMapping(value = "/viewFeedbackDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewFeedbackDetails(@RequestParam(required = false) Long feedbackId,
			@ModelAttribute StudentFeedback feedback, Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {
		m.addAttribute("webPage", new WebPage("viewFeedback", "Feedback Details", true, false));
		try {
			logger.info("viewFeedbackDetails--->" + feedbackId);
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
			Feedback feedbackFromDb = feedbackService.findByID(feedbackId);
			m.addAttribute("feedback", feedbackFromDb);

			// ----------------------------------

			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			Page<StudentFeedback> page = null;
			Map<String, List<FeedbackQuestion>> mapOfFeedIdAndResponses = new HashMap<>();

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				/*
				 * page = studentFeedbackService.getStudentFeedbackResponse(
				 * String.valueOf(feedbackId), pageNo, pageSize);
				 * 
				 * List<StudentFeedback> programList = page.getPageItems();
				 */
				Set<String> studentList = new HashSet<>();
				List<StudentFeedback> studentFeedbackList = studentFeedbackService
						.getStudentFeedbackResponseList(String.valueOf(feedbackId));

				for (StudentFeedback sf : studentFeedbackList) {
					studentList.add(sf.getUsername());
				}

				Map<String, List<String>> mapper = new HashMap<>();
				for (String s : studentList) {

					List<String> statusList = new ArrayList<>();

					for (StudentFeedback sf : studentFeedbackList) {

						if (sf.getUsername().equals(s)) {

							if (!statusList.contains(sf.getFeedbackCompleted())) {
								statusList.add(sf.getFeedbackCompleted());
							}
						}
					}

					mapper.put(s, statusList);
				}

				for (StudentFeedback sf : studentFeedbackList) {

					List<String> studentFeedbackcompleteStatus = mapper.get(sf.getUsername());

					if (1 == studentFeedbackcompleteStatus.size()) {

						if ("Y".equals(studentFeedbackcompleteStatus.get(0))) {
							sf.setEachfeedbackCompletionStatus("Y");
						} else {
							sf.setEachfeedbackCompletionStatus("N");
						}

					} else {
						sf.setEachfeedbackCompletionStatus("N");
					}
				}

				m.addAttribute("feedResponseMap", mapOfFeedIdAndResponses);
				m.addAttribute("studentFeedbackList", studentFeedbackList);
				// m.addAttribute("feedbackResponse", feedback);
				// m.addAttribute("page", page);
				// m.addAttribute("q", getQueryString(feedback));

				m.addAttribute("appName", app.trim());
				if ((app.trim().equals("SBM-NM-M")) || (app.trim().equals("PDSEFBM"))) {
					m.addAttribute("showProceed", false);
				} else {
					List<FeedbackQuestion> questionList = feedbackQuestionService.findByFeedbackId(feedbackId);
					if (questionList.size() > 0) {
						m.addAttribute("showProceed", true);
					} else {
						m.addAttribute("showProceed", false);
					}

				}

				return "feedback/feedbackDetails";
			}

			else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				page = studentFeedbackService.getStudentFeedbackResponseByUsername(username, pageNo, pageSize);

				List<StudentFeedback> programList = page.getPageItems();

				for (StudentFeedback sf : programList) {

					List<StudentFeedback> studentFeedbackcompleteStatus = studentFeedbackService
							.findFeedbackCompletedStatus(username, sf.getFeedbackId().toString());

					if (1 == studentFeedbackcompleteStatus.size()) {

						if ("Y".equals(studentFeedbackcompleteStatus.get(0).getFeedbackCompleted())) {
							sf.setEachfeedbackCompletionStatus("Y");
						} else {
							sf.setEachfeedbackCompletionStatus("N");
						}

					} else {
						sf.setEachfeedbackCompletionStatus("N");
					}

					if (sf.getFeedbackCompleted().equalsIgnoreCase("Y")) {
						Page<FeedbackQuestion> pageResp = feedbackQuestionService
								.getStudentFeedbackResponsePage(sf.getFeedbackId(), username, pageNo, pageSize);
						String feedId = String.valueOf(sf.getFeedbackId());
						List<FeedbackQuestion> programListResp = pageResp.getPageItems();

						mapOfFeedIdAndResponses.put(String.valueOf(sf.getFeedbackId()), programListResp);

					}

				}

				LmsVariables lmsVar = lmsVariablesService.getLmsVariableBykeyword("compulsoryFeedback");

				if (null != lmsVar) {
					if ("Y".equals(lmsVar.getValue())) {
						List<StudentFeedback> sfList = studentFeedbackService.findStartedFeedbackByUsername(username);
						if (sfList.size() > 0) {
							session.setAttribute("studentFeedbackActive", "Y");
						} else {
							session.setAttribute("studentFeedbackActive", "N");
						}
					} else {
						session.setAttribute("studentFeedbackActive", "N");
					}
				} else {
					session.setAttribute("studentFeedbackActive", "N");
				}

				/*
				 * if ("ASMSOC".equals(app)) { List<StudentFeedback> sfList =
				 * studentFeedbackService.findStartedFeedbackByUsername(username);
				 * if(sfList.size() > 0){ session.setAttribute("studentFeedbackActive", "Y");
				 * //return "redirect:/home"; }else{
				 * session.setAttribute("studentFeedbackActive", "N"); } }else{
				 * session.setAttribute("studentFeedbackActive", "N"); }
				 */
				UserCourse userCourse = new UserCourse();
				m.addAttribute("feedResponseMap", mapOfFeedIdAndResponses);
				m.addAttribute("feedbackResponse", feedback);
				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(feedback));
				return "feedback/feedbackDetailsForStudents";
			} // ---------------------------------

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Feedback");
			return "feedback/feedbackDetails";
		}

		return "feedback/feedbackDetails";
	}

	@RequestMapping(value = "/removeStudentFeedback", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String removeStudentFeedback(Model m, RedirectAttributes redirectAttrs, Principal p,

			@RequestParam(required = false) String ids[], @RequestParam(required = false) String feedbackId,

			HttpServletResponse response, HttpServletRequest request) {

		try {
			List<String> updatedList = Arrays.asList(ids);

			// StudentFeedback sf = studentFeedbackService.findByID(Long.valueOf(ids[0]));
			List<String> newList = new ArrayList<String>();
			newList.addAll(updatedList);

			for (String id : newList) {
				studentFeedbackService.removeStudent_Feedback(id);

			}

			redirectAttrs.addAttribute("feedbackId", feedbackId);

			setSuccess(redirectAttrs, "Entries deleted successfully");
			return "redirect:/viewFeedbackDetails";

		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			e.printStackTrace();
			return "ERROR";
		}

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/downloadPendingStudentFeedbackList", method = { RequestMethod.GET, RequestMethod.POST })
	public void downloadPendingStudentFeedbackList(Model m, Principal p,
			@RequestParam(required = false) Long feedbackId, HttpServletResponse response) {

		m.addAttribute("webPage", new WebPage("downlaodReport", "Download Report", false, false));
		String username = p.getName();

		List<String> headers = new ArrayList<String>(Arrays.asList("SAP ID", "Student Name",
				"Feedback Completion Status", "Course Name", "Acad Year", "Acad Session", "Email Id", "Mobile"));

		List<Map<String, Object>> map = new ArrayList<>();

		// ----------------- -------------------------------

		List<StudentFeedback> studentFeedbackList = studentFeedbackService
				.getStudentFeedbackResponseList(String.valueOf(feedbackId));

		for (StudentFeedback sfeed : studentFeedbackList) {

			List<StudentFeedback> studentFeedbackcompleteStatus = studentFeedbackService
					.findFeedbackCompletedStatus(sfeed.getUsername(), sfeed.getFeedbackId().toString());

			if (1 == studentFeedbackcompleteStatus.size()) {

				if ("Y".equals(studentFeedbackcompleteStatus.get(0).getFeedbackCompleted())) {
					sfeed.setEachfeedbackCompletionStatus("Completed");
				} else {
					sfeed.setEachfeedbackCompletionStatus("Pending");
				}

			} else {
				sfeed.setEachfeedbackCompletionStatus("Pending");
			}
		}

		// m.addAttribute("studentFeedbackList", studentFeedbackList);

		// -------------------- -------------------------

		for (StudentFeedback stuFeed : studentFeedbackList) {
			Map<String, Object> mapper = new HashMap<>();
			mapper.put("SAP ID", stuFeed.getUsername());
			mapper.put("Student Name", stuFeed.getStudentName());
			mapper.put("Feedback Completion Status", stuFeed.getEachfeedbackCompletionStatus());
			mapper.put("Course Name", stuFeed.getCourseName());
			mapper.put("Acad Year", stuFeed.getAcadYear());
			mapper.put("Acad Session", stuFeed.getAcadSession());
			mapper.put("Email Id", stuFeed.getEmail());
			mapper.put("Mobile", stuFeed.getMobile());

			map.add(mapper);
		}

		List<Map<String, Object>> ReportacadSessionwise = new ArrayList<Map<String, Object>>();

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "FeedbackPendingStudents" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator + "FeedbackPendingStudents"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
			excelCreater.CreateExcelFile(map, headers, filePath);
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

		// return "report/downLoadReportLink";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/UploadFeedbackQuestionForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String UploadFeedbackQuestionForm(Model m, Principal principal, @ModelAttribute Feedback feedback,
			RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("uploadFeedbackQuestion", "Upload Feedback Question", false, false));
		String username = principal.getName();
		feedback = feedbackService.findByID(feedback.getId());

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		List<FeedbackQuestion> feedbackQuestnList = feedbackQuestionService.findByFeedbackId(feedback.getId());

		if (feedbackQuestnList.size() > 0) {
			m.addAttribute("showProceed", true);
		} else {
			m.addAttribute("showProceed", false);
		}

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("feedback", feedback);
		return "feedback/uploadFeedbackQuestions";

	}

	// new mapping 03-02-2021
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadItFeedbackQuestions", method = { RequestMethod.POST })
	public String uploadItFeedbackQuestions(@ModelAttribute Feedback feedback, @RequestParam("file") MultipartFile file,
			Model m, RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("uploadFeedbackQuestion", "Upload Feedback Question", false, false));
		List<String> validateHeaders = null;

		Feedback feedbackDb = feedbackService.findByID(feedback.getId());
		validateHeaders = new ArrayList<String>(Arrays.asList("description", "type", "option1", "option2", "option3",
				"option4", "option5", "option6", "option7", "option8"));

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

			List<FeedbackQuestion> feedbackQuestionPrevious = feedbackQuestionService
					.findByFeedbackId(feedback.getId());

			if (feedbackQuestionPrevious.size() > 0) {

				feedbackQuestionService.deleteBatch(feedbackQuestionPrevious);

			}

			if (maps.size() == 0) {
				setNote(redirectAttributes, "Excel File is empty");
				redirectAttributes.addAttribute("id", feedback.getId());
				return "redirect:/UploadFeedbackQuestionForm";
			} else {
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {

						setNote(redirectAttributes, "Error--->" + mapper.get("Error"));
						redirectAttributes.addAttribute("id", feedback.getId());
						return "redirect:/UploadFeedbackQuestionForm";
					} else {
						/*
						 * FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
						 * feedbackQuestion.setFeedbackId(feedback.getId());
						 * feedbackQuestion.setOption1("1"); feedbackQuestion.setOption2("2");
						 * feedbackQuestion.setOption3("3"); feedbackQuestion.setOption4("4");
						 * feedbackQuestion.setOption5("5"); feedbackQuestion.setOption6("6");
						 * feedbackQuestion.setOption7("7");
						 * 
						 * feedbackQuestion.setCreatedBy(username);
						 * feedbackQuestion.setLastModifiedBy(username);
						 * feedbackQuestion.setCreatedBy(username); feedbackQuestion.setActive("Y");
						 * feedbackQuestion.setType("SINGLESELECT");
						 */
						mapper.put("feedbackId", feedback.getId());
						mapper.put("createdBy", username);
						mapper.put("lastModifiedBy", username);

						mapper.put("active", "Y");

						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());

						if (!mapper.get("description").toString().trim().isEmpty()
								&& !mapper.get("option1").toString().trim().isEmpty()
								&& !mapper.get("option2").toString().trim().isEmpty()
								&& !mapper.get("type").toString().isEmpty()) {
							feedbackQuestionService.insertUsingMap(mapper);
						} else {
							List<FeedbackQuestion> feedByFeedbackIdList = feedbackQuestionService
									.findByFeedbackId(feedback.getId());

							if (feedByFeedbackIdList.size() > 0) {

								feedbackQuestionService.deleteBatch(feedByFeedbackIdList);

							}
							setError(redirectAttributes, "Mandatory Columns Cannot be Blank");
							redirectAttributes.addAttribute("id", feedback.getId());
							return "redirect:/UploadFeedbackQuestionForm";
						}
						setSuccess(redirectAttributes, "feedback question file uploaded successfully");
						// redirectAttributes.addAttribute("showProceed", true);
					}
				}
			}

			m.addAttribute("feedback", feedback);
		} catch (Exception ex) {
			setError(redirectAttributes, "Error in uploading file");
			redirectAttributes.addAttribute("id", feedback.getId());
			return "redirect:/UploadFeedbackQuestionForm";
		}

		redirectAttributes.addAttribute("id", feedback.getId());
		return "redirect:/UploadFeedbackQuestionForm";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadFeedbackQuestion", method = { RequestMethod.POST })
	public String uploadFeedbackQuestion(@ModelAttribute Feedback feedback, @RequestParam("file") MultipartFile file,
			Model m, RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("uploadFeedbackQuestion", "Upload Feedback Question", false, false));
		List<String> validateHeaders = null;

		Feedback feedbackDb = feedbackService.findByID(feedback.getId());
		validateHeaders = new ArrayList<String>(Arrays.asList("description"));

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

			List<FeedbackQuestion> feedbackQuestionPrevious = feedbackQuestionService
					.findByFeedbackId(feedback.getId());

			if (feedbackQuestionPrevious.size() > 0) {

				feedbackQuestionService.deleteBatch(feedbackQuestionPrevious);

			}

			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {

						setNote(m, "Error--->" + mapper.get("Error"));
					} else {
						FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
						feedbackQuestion.setFeedbackId(feedback.getId());
						feedbackQuestion.setOption1("1");
						feedbackQuestion.setOption2("2");
						feedbackQuestion.setOption3("3");
						feedbackQuestion.setOption4("4");
						feedbackQuestion.setOption5("5");
						feedbackQuestion.setOption6("6");
						feedbackQuestion.setOption7("7");

						feedbackQuestion.setCreatedBy(username);
						feedbackQuestion.setLastModifiedBy(username);
						feedbackQuestion.setCreatedBy(username);
						feedbackQuestion.setActive("Y");
						feedbackQuestion.setType("SINGLESELECT");

						mapper.put("feedbackId", feedback.getId());
						mapper.put("createdBy", feedbackQuestion.getCreatedBy());
						mapper.put("lastModifiedBy", feedbackQuestion.getLastModifiedBy());

						mapper.put("active", feedbackQuestion.getActive());
						mapper.put("option1", feedbackQuestion.getOption1());
						mapper.put("option2", feedbackQuestion.getOption2());
						mapper.put("option3", feedbackQuestion.getOption3());
						mapper.put("option4", feedbackQuestion.getOption4());
						mapper.put("option5", feedbackQuestion.getOption5());
						mapper.put("option6", feedbackQuestion.getOption6());
						mapper.put("option7", feedbackQuestion.getOption7());
						mapper.put("option8", null);
						mapper.put("createdDate", Utils.getInIST());
						mapper.put("lastModifiedDate", Utils.getInIST());
						mapper.put("type", feedbackQuestion.getType());
						if (!mapper.get("description").toString().trim().isEmpty()) {
							feedbackQuestionService.insertUsingMap(mapper);
						}
						setSuccess(m, "feedback question file uploaded successfully");
						m.addAttribute("showProceed", true);
					}
				}
			}

			m.addAttribute("feedback", feedback);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "feedback/uploadFeedbackQuestions";
	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/giveStudentFeedback", method = { RequestMethod.GET, RequestMethod.POST })
	public String giveStudentFeedback(Model m, Principal principal, RedirectAttributes redirectAttrs,
			@RequestParam String feedbackId, @ModelAttribute Feedback feedback) {
		m.addAttribute("webPage", new WebPage("feedbackListForMultipleCourses", "View Feedback", false, false));
		feedback.setId(Long.valueOf(feedbackId));

		List<String> descriptionList = new ArrayList<String>();
		String username = principal.getName();

		/*-----------------------------------
		 */
		feedback = feedbackService.findByID(feedback.getId());
		
		List<StudentFeedback> feedbackValidity = studentFeedbackService.checkFeedbackValidity(Long.valueOf(feedbackId),
				username);

		if (feedbackValidity.size() == 0) {

			setNote(redirectAttrs, "feedback has not  started yet or deadline is missed!!! .");

		} else {

			List<StudentFeedback> allocatedCourseList = studentFeedbackService
					.findByfeedbackAllocatedWithCourseName(Long.valueOf(feedbackId), username);
			// .findByfeedbackIDAndUsername(Long.valueOf(feedbackId), username);
			List<FeedbackQuestion> feedbackQuestionstoIterate = new ArrayList<FeedbackQuestion>();
			List<FeedbackQuestion> feedbackQuestions = new ArrayList<FeedbackQuestion>();
			FeedbackQuestion singleQuestion = new FeedbackQuestion();

			feedbackQuestionstoIterate = feedbackQuestionService.findByFeedbackId(Long.parseLong(feedbackId));

			Map<Long, Object> map = new HashMap<>();

			Map<Long, List<FeedbackQuestion>> questionResponsemap = new HashMap<>();
			Map<Long, List<StudentFeedback>> studentFeedbackMap = new HashMap<>();
			Map<Long, Object> mapofCourseName = new HashMap<>();
			Map<Long, Object> mapofFacultyName = new HashMap<>();
			/* ------------------------- */
			String courseName = null;

			for (FeedbackQuestion quest : feedbackQuestionstoIterate) {
				List<FeedbackQuestion> courseWiseQuestion = new ArrayList<FeedbackQuestion>();
				List<StudentFeedback> studFeedback = new ArrayList<>();
				for (StudentFeedback a : allocatedCourseList) {
					courseName = a.getCourseName();
					Long courseId = a.getCourseId();
					String facultyId = a.getFacultyId();
					singleQuestion = feedbackQuestionService.getQuestionForReferenceTemplate(feedbackId, username,
							courseId.toString(), String.valueOf(quest.getId()), facultyId);

					StudentFeedback sf = studentFeedbackService.getStudentFeedbackResponseByQuestionId(feedbackId,
							username, courseId.toString(), String.valueOf(quest.getId()), facultyId);

					if (singleQuestion != null) {

						// map.put(courseId, feedbackQuestions);
						singleQuestion.getStudentFeedbackResponse().setComments(a.getComments());
						feedbackQuestions.add(singleQuestion);
						courseWiseQuestion.add(singleQuestion);
						
						
						List<String> mulAnswers = new ArrayList<>();
						if (sf.getAnswer() != null) {
							if (sf.getAnswer().contains(",")) {
								sf.setMultipleAnswers(Arrays.asList(sf.getAnswer().split(",")));
							} else {
								mulAnswers.add(sf.getAnswer());
								sf.setMultipleAnswers(mulAnswers);
							}
						}

						studFeedback.add(sf);

					}

					/*
					 * logger.info("data inside feedback question is " +
					 * feedbackQuestions.toString());
					 */
				}

				questionResponsemap.put(quest.getId(), courseWiseQuestion);

				studentFeedbackMap.put(quest.getId(), studFeedback);

			}

			feedback.setFeedbackQuestions(feedbackQuestions);
			feedback.setStudentFeedbacks(allocatedCourseList);
			m.addAttribute("map", map);
			m.addAttribute("questionResponsemap", questionResponsemap);
			m.addAttribute("studentFeedbackMap", studentFeedbackMap);
			m.addAttribute("allocatedCourseList", allocatedCourseList);
			m.addAttribute("feedbackQuestions", feedbackQuestions);
			m.addAttribute("feedbackQuestionstoIterate", feedbackQuestionstoIterate);
			m.addAttribute("feedbackId", feedbackId);
			m.addAttribute("feedback", feedback);
			m.addAttribute("mapofFacultyName", mapofFacultyName);
			m.addAttribute("mapofCourseName", mapofCourseName);
			// m.addAttribute("studentFeedbackList", studentFeedbackList);

			setNote(m, "Until you submit feedback of all courses your status wont be changed to  Submitted .");
			return "feedback/feedbackListForMultipleCoursesChanged";
		}

		return "redirect:/viewFeedbackDetails";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/searchFeedback", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchFeedback(@RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
			@ModelAttribute Feedback feedback, Principal p) {

		m.addAttribute("webPage", new WebPage("feedbackList", "View Feedbacks", false, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			/*
			 * Page<Feedback> page = feedbackService. searchActiveByExactMatch( feedback,
			 * pageNo, pageSize);
			 */

			List<Feedback> FeedbackList = feedbackService
					.findfeedbackByProgramId(Long.parseLong(userdetails1.getProgramId()), username);

			m.addAttribute("pageList", FeedbackList);

			List<Feedback> programList = FeedbackList;
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			for (Feedback f : programList) {
				if (f.getCreatedDate() != null) {
					String createdOn = formatter.format(f.getCreatedDate());
					f.setCreatedOn(createdOn);
				}
				Feedback dateFeedback = feedbackService.getDatesForFeedback(f.getId());

				Feedback dateFeedbackCompleted = feedbackService.getDatesForFeedbackForCompleted(f.getId());

				if (dateFeedback != null) {
					String startDate = dateFeedback.getStartDate();
					String endDate = dateFeedback.getEndDate();
					f.setStartDate(startDate);
					f.setEndDate(endDate);
				} else {

					if (dateFeedbackCompleted != null) {
						String startDate = dateFeedbackCompleted.getStartDate();
						String endDate = dateFeedbackCompleted.getEndDate();
						f.setStartDate(startDate);
						f.setEndDate(endDate);
					}
				}

				programList.set(programList.indexOf(f), f);

			}

			m.addAttribute("pageList", FeedbackList);
			m.addAttribute("q", getQueryString(feedback));

			if (programList == null || programList.size() == 0) {
				setNote(m, "No Feedbacks found");
			}

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(m, "Error in getting Feedback List");
		}
		return "feedback/feedbackList";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewStudentFeedbackResponse", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewStudentFeedbackResponse(@RequestParam(required = false) Long feedbackId, Model m,
			Principal principal) {

		m.addAttribute("webPage", new WebPage("viewStudentFeedback", "Feedback Details", true, false));
		try {

			String username = principal.getName();
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			List<StudentFeedbackResponse> studentFeedbackResponse = studentFeedbackResponseService
					.findStudentFeedbackResponse(feedbackId);
			m.addAttribute("studentFeedbackResponse", studentFeedbackResponse);

			if (studentFeedbackResponse == null || studentFeedbackResponse.size() == 0) {
				setNote(m, "No Student Feedbacks found");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Feedback");
			return "feedback/feedbackDetails";
		}
		return "feedback/viewStudentFeedbackResponse";

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/searchStudentFeedback", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchStudentFeedback(@RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
			@ModelAttribute Feedback feedback, Principal p, @RequestParam(required = false) Long courseId) {
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("feedbackList", "View Feedbacks", false, false));
		m.addAttribute("courseId", courseId);
		try {

			Page<Feedback> page = feedbackService.searchStudentFeedback(username, pageNo, pageSize);

			List<Feedback> feedbackList = page.getPageItems();

			m.addAttribute("page", page);
			// m.addAttribute("q", getQueryString(feedback));

			if (feedbackList == null || feedbackList.size() == 0) {
				setNote(m, "No Feedbacks found");
			}

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(m, "Error in getting Feedback List");
		}
		return "feedback/feedbackList";
	}

	/*
	 * @RequestMapping(value = "/searchStudentFeedback", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * searchStudentFeedback(Model m,Principal principal) {
	 * m.addAttribute("webPage", new WebPage("assignment", "View Feedback", true,
	 * false));
	 * 
	 * m.addAttribute("feedback", new Feedback()); String
	 * username=principal.getName(); List<Feedback> listOfFeedback =
	 * feedbackService.findByUser(username);
	 * 
	 * logger.info("list Of Feedback----- "+listOfFeedback);
	 * m.addAttribute("listOfFeedback", listOfFeedback);
	 * 
	 * return "feedback/feedbackListForMultipleCourses"; }
	 */
	/**
	 * Questions setup
	 */
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addFeedbackQuestionForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addFeedbackQuestionForm(@RequestParam Long feedbackId, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("addFeedbackQuestion", "Add Questions To Feedback", true, false));
		Feedback feedback = feedbackService.findByID(feedbackId);
		List<FeedbackQuestion> feedbackQuestionList = feedbackQuestionService.findByFeedbackId(feedbackId);
		FeedbackQuestion feedbackQuestion = new FeedbackQuestion();
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		if (feedbackQuestionList.size() > 0) {
			m.addAttribute("showProceed", true);
		}

		if (null != feedback) {
			feedback.setFeedbackQuestions(feedbackQuestionService.findByFeedbackId(feedbackId));
			feedbackQuestion.setFeedbackId(feedbackId);
		} else {
			setError(m, "Feedback not found");
		}
		m.addAttribute("feedback", feedback);
		feedbackQuestion.setType("SINGLESELECT");
		m.addAttribute("feedbackQuestion", feedbackQuestion);

		return "feedback/addFeedbackQuestion";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addFeedbackQuestion", method = RequestMethod.POST)
	public String addFeedbackQuestion(@ModelAttribute FeedbackQuestion feedbackQuestion,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		redirectAttrs.addAttribute("feedbackId", feedbackQuestion.getFeedbackId());
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {

			feedbackQuestion.setCreatedBy(username);
			feedbackQuestion.setLastModifiedBy(username);

			feedbackQuestion.setOption1("1");
			feedbackQuestion.setOption2("2");
			feedbackQuestion.setOption3("3");
			feedbackQuestion.setOption4("4");
			feedbackQuestion.setOption5("5");
			feedbackQuestion.setOption6("6");
			feedbackQuestion.setOption7("7");

			feedbackQuestionService.insertWithIdReturn(feedbackQuestion);

			setSuccess(redirectAttrs, "Question added successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Question");
			return "redirect:/addFeedbackQuestionForm";
		}
		return "redirect:/addFeedbackQuestionForm";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/updateFeedbackQuestion", method = RequestMethod.POST)
	public String updateFeedbackQuestion(@ModelAttribute Feedback feedback, RedirectAttributes redirectAttrs,
			Principal principal, Model m) {
		FeedbackQuestion feedbackQuestion = feedback.getFeedbackQuestions()
				.get(feedback.getFeedbackQuestions().size() - 1);
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {
			FeedbackQuestion feedbackQuestionFromDb = feedbackQuestionService.findByID(feedbackQuestion.getId());
			redirectAttrs.addAttribute("feedbackId", feedbackQuestionFromDb.getFeedbackId());

			feedbackQuestionFromDb = LMSHelper.copyNonNullFields(feedbackQuestionFromDb, feedbackQuestion);
			feedbackQuestionFromDb.setLastModifiedBy(username);
			feedbackQuestionFromDb.setOption1("1");
			feedbackQuestionFromDb.setOption2("2");
			feedbackQuestionFromDb.setOption3("3");
			feedbackQuestionFromDb.setOption4("4");
			feedbackQuestionFromDb.setOption5("5");
			feedbackQuestionFromDb.setOption6("6");
			feedbackQuestionFromDb.setOption7("7");

			if (feedbackQuestionService.update(feedbackQuestionFromDb) > 0)
				setSuccess(redirectAttrs, "Feedback Question updated successfully");
			else
				setError(redirectAttrs, "Feedback Question cannot be updated");

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Feedback Question");
			return "redirect:/addFeedbackQuestionForm";
		}
		return "redirect:/addFeedbackQuestionForm";
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentFeedbackCommentsForCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody StudentFeedbackResponse addStudentFeedbackCommentsForCourse(@RequestParam String feedbackId,
			Model m, @ModelAttribute Feedback feedback, Principal principal, RedirectAttributes redirectAttrs) {
		try {
			String username = principal.getName();
			m.addAttribute("webPage", new WebPage("feedbackListForMultipleCourses", "Take Feedback", false, false));
			List<FeedbackQuestion> feedbackQuestion = feedback.getFeedbackQuestions();

			StudentFeedbackResponse studentFeedbackResponse = new StudentFeedbackResponse();

			for (FeedbackQuestion fq : feedbackQuestion) {

				studentFeedbackResponse = fq.getStudentFeedbackResponse();

				StudentFeedback studFeedback = studentFeedbackService.findByID(
						Long.valueOf(Long.parseLong(fq.getStudentFeedbackResponse().getStudentFeedbackId().trim())));

				studFeedback.setComments(fq.getStudentFeedbackResponse().getComments());
				studFeedback.setLastModifiedBy(username);
				studFeedback.setLastModifiedDate(Utils.getInIST());
				studentFeedbackService.upsert(studFeedback);

			}

			setSuccess(redirectAttrs, "feedback saved successfully");

			return studentFeedbackResponse;

		} catch (Exception e) {
			logger.info("error" + e);
			setError(m, "Error in saving question");
			return null;
		}
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/deleteFeedback", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteCourse(@RequestParam Integer programId, RedirectAttributes redirectAttrs) {
		try {
			feedbackService.deleteSoftById(String.valueOf(programId));
			setSuccess(redirectAttrs, "Feedback deleted successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Exam Center.");
		}

		return "redirect:/searchFeedback";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/publishFeedbackReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishFeedbackReport(@RequestParam Long id, Model m, RedirectAttributes redirectAttrs,
			Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("downlaodReport", "Download Report", false, false));
		try {
			Feedback feedback = feedbackService.findByID(id);
			feedbackService.publishFeedback(id);
			setSuccess(redirectAttrs, feedback.getFeedbackName() + " published successfully!");
		} catch (Exception e) {
			setError(redirectAttrs, "Error in publishing feedback!");
		}

		return "redirect:/searchFeedback";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/searchAllFacultiesForFeedback", method = RequestMethod.GET)
	public String searchAllFacultiesForFeedback(@RequestParam(required = false) String feedbackId, Model m,
			Principal principal) {

		m.addAttribute("webPage", new WebPage("viewFeedback", "Faculties Assigned For Feedback", true, false));

		Feedback feedback = feedbackService.findByID(Long.valueOf(feedbackId));
		List<Feedback> getAllAllocatedFacultiesByFeedbackId = feedbackService
				.getAllAllocatedFacultiesByFeedbackId(feedbackId);

		m.addAttribute("faculties", getAllAllocatedFacultiesByFeedbackId);
		m.addAttribute("feedback", feedback);

		return "feedback/searchFacultiesForFeedback";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/UploadStudentsToDeallocateForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String UploadStudentsToDeallocateForm(Model m, Principal principal, @ModelAttribute Feedback feedback,
			RedirectAttributes redirectAttributes) {
		// m.addAttribute("webPage", new WebPage("UploadStudentsToDeallocate","Upload",
		// false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("feedback", feedback);
		return "feedback/uploadStudentsToDeallocate";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadStudentsToDeallocate", method = { RequestMethod.POST })
	public String uploadStudentsToDeallocate(@ModelAttribute Feedback feedback,
			@RequestParam("file") MultipartFile file, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {
		// m.addAttribute("webPage", new WebPage("fecultyDetails","Upload Feedback
		// Question", false, false));
		List<String> validateHeaders = null;

		Feedback feedbackDb = feedbackService.findByID(feedback.getId());
		validateHeaders = new ArrayList<String>(Arrays.asList("SAPID"));

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
			// List<Map<String, Object>> maps =
			// excelReader.readExcelFileUsingColumnNum(file,validateHeaders);

			List<String> studentList = new ArrayList<String>();
			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {

						setNote(m, "Error--->" + mapper.get("Error"));
					} else {
						studentList.add(mapper.get("SAPID").toString().trim());

					}
				}
				for (String id : studentList) {
					logger.info("Username---->" + id);
					studentFeedbackService.removeStudent_FeedbackUsingExcel(String.valueOf(feedback.getId()), id);
				}
				setSuccess(m, "Feedback De-allocated successfully.");
			}

			m.addAttribute("feedback", feedback);
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "feedback/uploadStudentsToDeallocate";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/downloadDeallocateFeedbackTemplate", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadDeallocateFeedbackTemplate(

			@RequestParam(name = "feedbackId") String feedbackId, RedirectAttributes redirectAttrs,
			HttpServletRequest request, HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		logger.info("feedbackId--->" + feedbackId);
		List<String> studentList = studentFeedbackService.getStudentFeedbackByFeedbackId(feedbackId);
		try {

			/* GENERATE */
			/*
			 * DataValidation dataValidation = null; DataValidationConstraint constraint =
			 * null; DataValidationHelper validationHelper = null;
			 */
			// List<String> headers = new ArrayList<String>(Arrays.asList("SAPID"));

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Deallocate Feedback");
			/*
			 * validationHelper = new XSSFDataValidationHelper(sheet);
			 * 
			 * 
			 * 
			 * constraint = validationHelper .createExplicitListConstraint(new String[] {
			 * "Y", "N" }); dataValidation = validationHelper.createValidation(constraint,
			 * addressList); dataValidation.setSuppressDropDownArrow(true);
			 * sheet.addValidationData(dataValidation);
			 */

			// create header row
			Row r = sheet.createRow(0);
			// set header value
			r.createCell(0).setCellValue("SAPID");

			int i = 1;
			for (String uc : studentList) {
				Row row = sheet.createRow(i);
				row.createCell(0).setCellValue(String.valueOf(uc));
				i++;
			}
			String folderPath = downloadAllFolder + "/" + app + "/" + "feedbackDealloacateTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
			}
			String filePath = folderP.getAbsolutePath() + File.separator + "feedbackDealloacteTemp" + feedbackId
					+ ".xlsx";
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

			System.out.println("file written successfully");

			/* DOWNLOAD */
			/*
			 * String folderPath = baseDir + "/" + app + "/" + "icaUploadMarkTemp"; File
			 * folderP = new File(folderPath); if (!folderP.exists()) { folderP.mkdir(); }
			 * String filePath = folderP.getAbsolutePath() + "/" + "deallocateFeedbackTemp"
			 * + feedbackId + ".xls"; logger.info("filePath " + filePath);
			 */
			// String filePath =
			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);

			if (!downloadFile.exists()) {

				redirectAttrs.addAttribute("feedbackId", feedbackId);
				setError(redirectAttrs, "Error in download template");
				return "redirect:/UploadStudentsToDeallocateForm";
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

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/removeFacultyFeedbackForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String removeFacultyFeedbackForm(Model m, Principal principal, @ModelAttribute Feedback feedback,
			@RequestParam(required = false) String feedbackId, RedirectAttributes redirectAttributes) {
		// m.addAttribute("webPage", new WebPage("UploadStudentsToDeallocate","Upload",
		// false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("removeFacultyFeedbackForm--->" + feedback.getId());

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("feedback", feedback);
		List<StudentFeedback> facultyList = studentFeedbackService.getFacultyByFeedbackId(feedback.getId());
		m.addAttribute("facultyList", facultyList);

		return "feedback/deallocateFacultyForFeedback";

	}

	@RequestMapping(value = "/removeFacultyFeedback", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String removeFacultyFeedback(Model m, RedirectAttributes redirectAttrs, Principal p,
			@RequestParam(required = false) String ids[], @RequestParam(required = false) String feedbackId,
			HttpServletResponse response, HttpServletRequest request) {

		try {
			List<String> updatedList = Arrays.asList(ids);

			// StudentFeedback sf = studentFeedbackService.findByID(Long.valueOf(ids[0]));
			List<String> newList = new ArrayList<String>();
			newList.addAll(updatedList);

			for (String id : newList) {
				String facultyId = "", courseId = "";
				if (id.contains("_")) {
					facultyId = id.split("_")[0];
					courseId = id.split("_")[1];
					logger.info("FeedbackId : " + feedbackId + " FacultyId : " + facultyId + " courseId : " + courseId);
					studentFeedbackService.removeFacultyFromFeedback(feedbackId, facultyId, courseId);
				}
				// studentFeedbackService.removeStudent_Feedback(id);
			}
			redirectAttrs.addAttribute("feedbackId", feedbackId);

			setSuccess(redirectAttrs, "Entries deleted successfully");
			return "{\"status\":\"success\"}";
			// return "redirect:/viewFeedbackDetails";
			// return "SUCCESS";

		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			e.printStackTrace();
			/* return "{\"status\":\"error\"}"; */
			return "ERROR";
		}

	}

	// Feedback update for supportAdmin
	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/gotoupdateFeedbackQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public String gotoupdateFeedbackQuestion(Model m, Principal principal, HttpServletRequest request,
			@ModelAttribute("FeedbackQuestion") FeedbackQuestion feedbackquestion,
			RedirectAttributes redirectAttributes) {

		logger.info("poem------------------>" + feedbackquestion);
		List<String> SessionList = courseService.getAcadSessionForActiveFeedback();
		List<String> acadYearList = courseService.getAllAcadYear();
		List<Program> programList = programService.findAllProgramForFeedbackSupportAdmin();
		List<ProgramCampus> campusList = programCampusService.getCampusNameSupportAdmin();
		List<String> usernameList = studentFeedbackService.getCreatedbyListForSupportAdmin();

		logger.info("SessionList-----------" + SessionList);
		logger.info("programList-------->>" + programList);
		logger.info("campusList-------->>" + campusList);

		m.addAttribute("SessionList", SessionList);
		m.addAttribute("acadYearList", acadYearList);
		m.addAttribute("programList", programList);
		m.addAttribute("campusList", campusList);
		m.addAttribute("usernameList", usernameList);

		m.addAttribute("FeedbackQuestion", new FeedbackQuestion());
		logger.info("feedbaack acad session" + feedbackquestion.getAcadSession());
		if (feedbackquestion != null) {
			logger.info("feedback question is not null");
			m.addAttribute("FeedbackQuestion", feedbackquestion);
			// m.addAttribute("FeedbackQuestion", feedbackquestion);

		}

		m.addAttribute("webPage", new WebPage("searchCourse", "Update Feedback Question", true, false));
		return "feedback/supportfeedbackist";

	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/checkFeedbackSupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String checkFeedbackSupportAdmin(@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m, @Valid @ModelAttribute("FeedbackQuestion") FeedbackQuestion fdQuestion,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		logger.info("fdQuestion------>" + fdQuestion.getAcadSession() + "-----" + fdQuestion.getAcadYear() + "------"
				+ fdQuestion.getProgramId() + "-- CreatedBy" + fdQuestion.getCreatedBy() + "---"
				+ fdQuestion.getCampusId());

		List<String> programIdList = new ArrayList<String>();
		// programid bhi daal query me aur set kar code me
		programIdList = Arrays.asList(fdQuestion.getProgramId().split("\\s*,\\s*"));
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();

		if (fdQuestion.getCampusId() != null) {
			studentFeedbackListCourseWise = studentFeedbackService
					.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(fdQuestion.getAcadSession(), programIdList,
							fdQuestion.getAcadYear(), String.valueOf(fdQuestion.getCampusId()),
							fdQuestion.getCreatedBy());
		} else {
			studentFeedbackListCourseWise = studentFeedbackService.getstudentFeedbackListCourseWiseAndYearProgram(
					fdQuestion.getAcadSession(), programIdList, fdQuestion.getAcadYear(), fdQuestion.getCreatedBy());
		}

		logger.info("studentFeedbackListCourseWise-------->>" + studentFeedbackListCourseWise);

		Set<String> courseIdList = new HashSet<>();

		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));
			break;
		}

		Token userDetails = (Token) principal;
		Map<String, List<String>> getListOfMapForFeedbackReport = returnMapOfReport(courseIdList,
				fdQuestion.getCreatedBy(), userDetails);

		Map<String, List<String>> Listvaluesgetting = new HashMap<>();

		logger.info("courseIdList-------->>" + courseIdList);

		logger.info("getListOfMapForFeedbackReport-------->>" + getListOfMapForFeedbackReport);

		logger.info("findFeedbackQuestionBySemester-------->>" + findFeedbackQuestionBySemester);
		if (getListOfMapForFeedbackReport.isEmpty()) {
			m.addAttribute("note", "No User Found");
		} else {

			m.addAttribute("getListOfMapForFeedbackReport", getListOfMapForFeedbackReport);
		}

		List<String> SessionList = courseService.getAcadSessionForActiveFeedback();
		List<String> acadYearList = courseService.getAllAcadYear();
		List<Program> programList = programService.findAllProgramForFeedbackSupportAdmin();
		List<ProgramCampus> campusList = programCampusService.getCampusNameSupportAdmin();
		List<String> usernameList = studentFeedbackService.getCreatedbyListForSupportAdmin();

		logger.info("SessionList-----------" + SessionList);
		logger.info("programList-------->>" + programList);
		logger.info("campusList-------->>" + campusList);

		m.addAttribute("SessionList", SessionList);
		m.addAttribute("acadYearList", acadYearList);
		m.addAttribute("programList", programList);
		m.addAttribute("campusList", campusList);
		m.addAttribute("usernameList", usernameList);

		return "feedback/supportfeedbackist";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/updateFeedBackQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateFeedBackQuestion(Model m, Principal principal,
			@Valid @ModelAttribute("FeedbackQuestion") FeedbackQuestion fdQuestion, RedirectAttributes redirectAttrs) {
		FeedbackQuestion feedbackqDB = feedbackQuestionService.findByID(fdQuestion.getId());
		try {
			// String newId =fdQuestion.getId().replace("[", "".trim()).replace("]",
			// "".trim());

			logger.info("feedbackdiscription newId---------------->" + fdQuestion);
			logger.info(
					"feedbackdiscription---------------->" + fdQuestion.getId() + "===" + fdQuestion.getDescription());

			if (fdQuestion.getId() != null) {

				int success = feedbackQuestionService.updateFeedbackQuestion(fdQuestion.getDescription(),
						String.valueOf(fdQuestion.getId()));
				logger.info("success---------" + success);
			}
			setSuccess(redirectAttrs, "SuccessFully Updated");
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting");
			logger.error("Exception", ex);
		}

		/*
		 * if (feedbackqDB != null) { List<FeedbackQuestion> dropdownList =
		 * feedbackQuestionService
		 * .getFeefbackForSupportDrop(String.valueOf(feedbackqDB.getFeedbackId()));
		 * fdQuestion.setAcadSession(dropdownList.get(0).getAcadSession());
		 * fdQuestion.setAcadYear(dropdownList.get(0).getAcadYear());
		 * fdQuestion.setCampusId(dropdownList.get(0).getCampusId());
		 * fdQuestion.setProgramId(dropdownList.get(0).getProgramId());
		 * fdQuestion.setCreatedBy(dropdownList.get(0).getCreatedBy());
		 * 
		 * }
		 */
		if (feedbackqDB != null) {
			FeedbackQuestion dropdownList = feedbackQuestionService
					.getFeefbackForSupportDropBean(String.valueOf(feedbackqDB.getFeedbackId()));
			fdQuestion.setAcadSession(dropdownList.getAcadSession());
			fdQuestion.setAcadYear(dropdownList.getAcadYear());
			fdQuestion.setCampusId(dropdownList.getCampusId());
			fdQuestion.setProgramId(dropdownList.getProgramId());
			fdQuestion.setCreatedBy(dropdownList.getCreatedBy());

		}

		redirectAttrs.addFlashAttribute("FeedbackQuestion", fdQuestion);
		// query kar ke dekh empty aa rha hai kya
		// m.addAttribute("FeedbackQuestion", fdQuestion);
		return "redirect:/checkFeedbackSupportAdmin";
		// return "feedback/supportfeedbackist";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/viewupdateFeedBackQuestionSpAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String viewupdateFeedBackQuestionSpAdmin(Model m, Principal principal,
			@ModelAttribute("FeedbackQuestion") FeedbackQuestion fdQuestion, @RequestParam(name = "id") String id,
			Map<String, String> requestParams, RedirectAttributes redirectAttrs) {
		try {
			// sod jaisa karna padega isko
			logger.info("id---------------->" + id);
			String newId = id.replace("[", "".trim()).replace("]", "".trim());
			long num2 = Long.parseLong(newId);
			logger.info("feedbackdiscription num2---------------->" + fdQuestion.getAcadSession());
			logger.info("feedbackdiscription---------------->" + requestParams);
			FeedbackQuestion questionList = null;
			if (id != null) {
				questionList = feedbackQuestionService.findByID(num2);
				logger.info("feedbackdiscription questionList---------------->" + questionList.getDescription());

			}
			m.addAttribute("FeedbackQuestion", questionList);
			setSuccess(redirectAttrs, "SuccessFully Updated");
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting");
			logger.error("Exception", ex);
		}

		return "feedback/viewupdateFeedBackQuestionSpAdmin";
	}

	public Map<String, List<String>> returnMapOfReport(Set<String> courseIdList, String username, Token userDetails) {
		logger.info("Entering in Map of Report------------------" + courseIdList + " " + username + " "
				+ userDetails.getAuthorities());

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		logger.info("listOfMapOfFeedbackReport---->>>" + listOfMapOfFeedbackReport);
		Map<String, List<String>> mapOfFeedbackQuestionAndListOfFeedbackQuestnIds = new HashMap<>();
		for (String courseIds : courseIdList) {
			List<FeedbackQuestion> feedbackQuestionListByCourse = new ArrayList<>();
			if (userDetails.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)) {
				feedbackQuestionListByCourse = feedbackQuestionService.getFeedbackQuestionListByCourse(courseIds,
						username);
			} else {
				feedbackQuestionListByCourse = feedbackQuestionService
						.getFeedbackQuestionListByCourseForfaculty(courseIds);
			}
			Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
			Set<String> feedbackIds = new HashSet<>();
			for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
				feedbackIds.add(String.valueOf(fq.getFeedbackId()));

			}
			Set<String> feedbackQuestions = new HashSet<>();
			// Map<String, List<String>> mapOfFeedbackQuestionAndListOfFeedbackQuestnIds =
			// new HashMap<>();
			for (String feedbackId : feedbackIds) {
				List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					feedbackQuestions.add(fq.getDescription());
					if (feedbackId.equals(String.valueOf(fq.getFeedbackId()))) {
						feedbackQuestionListByFeedbackId.add(fq);
					}
				}
				mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId, feedbackQuestionListByFeedbackId);
			}
			// Main loopp
			for (String feedbackQ : feedbackQuestions) {
				List<String> feedbackQIds = new ArrayList<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					if (fq.getDescription().equals(feedbackQ)) {

						feedbackQIds.add(String.valueOf(fq.getId()));
					}
				}
				mapOfFeedbackQuestionAndListOfFeedbackQuestnIds.put(feedbackQ, feedbackQIds);
				logger.info("mapOfFeedbackQuestionAndListOfFeedbackQuestnIds---------->"
						+ mapOfFeedbackQuestionAndListOfFeedbackQuestnIds);
			}
			// break;
		}
		return mapOfFeedbackQuestionAndListOfFeedbackQuestnIds;
	}

}
