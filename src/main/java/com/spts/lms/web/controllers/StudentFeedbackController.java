package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.beans.feedback.FeedbackQuestion;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.feedback.FeedbackQuestionService;
import com.spts.lms.services.feedback.FeedbackService;
import com.spts.lms.services.feedback.StudentFeedbackResponseService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Secured("ROLE_ADMIN")
@Controller
public class StudentFeedbackController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	CourseService courseService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Autowired
	StudentFeedbackResponseService studentFeedbackResponseService;

	@Autowired
	FeedbackQuestionService feedbackQuestionService;

	@Autowired
	FeedbackService feedbackService;

	@Autowired
	UserService userService;

	@Autowired
	ProgramCampusService programCampusService;
	
	@Autowired
	ProgramService programService;

	@Value("${lms.enrollment.years}")
	private String[] enrollmentYears;

	private static final Logger logger = Logger
			.getLogger(StudentFeedbackController.class);

	@ModelAttribute("courseList")
	public List<Course> getCourseList() {

		return courseService.findAllActive();
	}

	@ModelAttribute("courseListByprogramName")
	public List<Course> getCourseListByProgram(Principal p) {
		Token t = (Token) p;
		return courseService
				.findByCoursesBasedOnProgramName(t.getProgramName());
	}

	@ModelAttribute("yearList")
	public String[] getYearList() {
		return enrollmentYears;
	}

	@ModelAttribute("campusListByProgram")
	public List<ProgramCampus> getCampusListByProgram(Principal p) {
		Token t = (Token) p;
		return programCampusService.getCampusListByProgram(t.getProgramId());
	}

	@ModelAttribute("campusList")
	public List<ProgramCampus> getCampusList() {

		return programCampusService.getCampusList();
	}

	/*
	 * @ModelAttribute("feedbackList") public List<Feedback> getFeedbackList() {
	 * return feedbackService.findAllActive(); }
	 */
	@ModelAttribute("feedbackList")
	public List<Feedback> getFeedbackList() {
		return feedbackService.findAllValidFeedback();
	}

	@RequestMapping(value = "/addStudentFeedbackForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addStudentFeedbackForm(@ModelAttribute Feedback feedback,
			Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {
		
		m.addAttribute("webPage", new WebPage("addStudentFeedback",
				"Create Feedback", true, false));

		// ----------------

		if (feedback.getId() != null) {

			
			List<StudentFeedback> studentFeedbackDuplicates = studentFeedbackService
					.checkStudentFeedbackDuplicates(String.valueOf(feedback
							.getId()));
			
			if (studentFeedbackDuplicates.size() > 0) {
				

				studentFeedbackService.deleteDuplicateStudentFeedback(String
						.valueOf(feedback.getId()));

				

			}
		}

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Page<StudentFeedback> page = studentFeedbackService
				.getStudentForFeedback(String.valueOf(feedback.getId()),
						feedback.getFacultyId(), feedback.getCourseId(),
						pageNo, pageSize);

		List<StudentFeedback> programList = page.getPageItems();
		
		UserCourse userCourse = new UserCourse();

		
		List<String> acadMonths = courseService.getAllAcadMonth();

		m.addAttribute("acadMonths", acadMonths);

		m.addAttribute("feedback", feedback);
		m.addAttribute("page", page);
		m.addAttribute("q", getQueryString(feedback));
		// ---------------
		return "feedback/addStudentFeedback";
	}

	@RequestMapping(value = "/viewEachStudentFeedbackForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewEachStudentFeedbackForm(
			@ModelAttribute Feedback feedback, Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {
		
		m.addAttribute("webPage", new WebPage("addStudentFeedback",
				"Create Feedback", true, false));

		// ----------------
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Page<StudentFeedback> page = studentFeedbackService
				.viewStudentFeedback(String.valueOf(feedback.getId()),
						feedback.getAcadMonth(), feedback.getAcadYear(),
						pageNo, pageSize);

		List<StudentFeedback> programList = page.getPageItems();
		
		UserCourse userCourse = new UserCourse();

		

		m.addAttribute("feedback", feedback);
		m.addAttribute("page", page);
		m.addAttribute("q", getQueryString(feedback));
		// ---------------
		return "feedback/viewEachStudentFeedback";
	}

	@RequestMapping(value = "/StudentFeedbackAllocationCount", method = { RequestMethod.GET })
	public @ResponseBody int StudentFeedbackAllocationCount(
			@RequestParam(required = false, defaultValue = "1") String feedbackId) {
		

		List<StudentFeedback> count = studentFeedbackService
				.getStudentFeedbackAllocationStatus(feedbackId);

		return count.size();
	}

	@RequestMapping(value = "/saveStudentFeedback", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentFeedback(@ModelAttribute Feedback feedback,
			RedirectAttributes redirectAttr, Principal principal, Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttr.addFlashAttribute("feedback", feedback);
		
		try {
			for (StudentFeedback studentFeedback : feedback
					.getStudentFeedbacks()) {
				
				studentFeedback.setCreatedBy(username);
				studentFeedback.setLastModifiedBy(username);
				Course c = courseService.findByID(feedback.getCourseId());
				studentFeedback.setAcadMonth(c.getAcadMonth());
				studentFeedback.setAcadYear(Integer.valueOf(c.getAcadYear()));
			}
			studentFeedbackService.insertBatch(feedback.getStudentFeedbacks());
			setSuccess(redirectAttr, "Students Allocated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating feedback");
			return "redirect:/addStudentFeedbackForm";
		}
		return "redirect:/addStudentFeedbackForm";
	}

	@RequestMapping(value = "/saveStudentFeedbackForProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentFeedbackForProgram(
			@ModelAttribute Feedback feedback, RedirectAttributes redirectAttr,
			Principal principal, Model m) {
		String username = principal.getName();
		Course c;
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttr.addFlashAttribute("feedback", feedback);
		
		try {

			Token t = (Token) principal;
			

			List<StudentFeedback> allocationStatus = studentFeedbackService
					.getAllocatedStudentFeedback(String.valueOf(feedback
							.getId()));
			Date startDate = Utils.converFormatsDate(feedback.getStartDate());
			Date endDate = Utils.converFormatsDate(feedback.getEndDate());
			System.out.println("s Date"+startDate);
			System.out.println("e Date"+endDate);
			if(startDate.after(endDate)) {
				setError(redirectAttr, "Date Range is Not Valid");
			}

			
			if (allocationStatus.size() > 0) {
				setSuccess(redirectAttr, "Feedback already allocated ");
			} else {

				if (feedback.getCampusId() != null) {
					feedback.setStudentFeedbacks(studentFeedbackService
							.getStudentsByProgramAndCampus(
									userdetails1.getProgramId(),

									String.valueOf(feedback.getCampusId()),
									feedback.getAcadYear(),feedback.getAcadMonth()));
				} else {
					feedback.setStudentFeedbacks(studentFeedbackService
							.getStudentsByProgram(t.getProgramId(),
									feedback.getAcadYear(),feedback.getAcadMonth()));
				}
			}

			// "50332686"
			

			List<Course> courseList = courseService
					.findByCoursesBasedOnProgramNameAndYear(ProgramName,
							feedback.getAcadYear());

			Map<Long, Course> mapofCourseId = new HashMap<>();

			for (Course course : courseList) {

				mapofCourseId.put(course.getId(), course);
			}

			for (StudentFeedback studentFeedback : feedback
					.getStudentFeedbacks()) {
				
				studentFeedback.setCreatedBy(username);
				studentFeedback.setLastModifiedBy(username);
				studentFeedback.setFeedbackId(feedback.getId());
				studentFeedback.setStartDate(feedback.getStartDate());
				studentFeedback.setEndDate(feedback.getEndDate());
				// studentFeedback.setFeedbackId((long) 4);
				c = mapofCourseId.get(studentFeedback.getCourseId());

				/*
				 * Course c = courseService
				 * .findByID(studentFeedback.getCourseId());
				 */

				/*
				 * if(c.getAcadMonth()!= null){
				 * studentFeedback.setAcadMonth(c.getAcadMonth() +" "); }
				 * 
				 * if(c.getAcadYear()!= null){
				 * studentFeedback.setAcadYear(Integer
				 * .valueOf(c.getAcadYear())); }
				 */
			}
			
			studentFeedbackService.insertBatch(feedback.getStudentFeedbacks());

			if (feedback.getStudentFeedbacks().size() > 0) {
				setSuccess(redirectAttr, "Students Allocated successfully");
			} else {
				setNote(redirectAttr,
						"No Students Found To Allocate For Semester--->"
								+ feedback.getAcadSession());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating feedback");
			return "redirect:/addStudentFeedbackForm";
		}
		redirectAttr.addAttribute("feedbackId", feedback.getId());
		return "redirect:/searchAllFacultiesForFeedback";
	}

	@RequestMapping(value = "/saveStudentFeedbackForAcadSession", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentFeedbackForAcadSession(
			@ModelAttribute Feedback feedback, RedirectAttributes redirectAttr,
			Principal principal, Model m) {
		Course c;
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		redirectAttr.addFlashAttribute("feedback", feedback);
		
		try {

			List<StudentFeedback> allocationStatus = studentFeedbackService
					.getAllocatedStudentFeedback(String.valueOf(feedback
							.getId()));

			Date startDate = Utils.converFormatsDate(feedback.getStartDate());
			Date endDate = Utils.converFormatsDate(feedback.getEndDate());
			System.out.println("s Date"+startDate);
			System.out.println("e Date"+endDate);
			if(startDate.after(endDate)) {
				setError(redirectAttr, "Date Range is Not Valid");
			}

			if (allocationStatus.size() > 0) {
				setSuccess(redirectAttr, "Feedback already allocated ");
			} else {
				if (feedback.getCampusId() != null) {
					feedback.setStudentFeedbacks(studentFeedbackService
							.getStudentsByAcadSessionAndCampus(
									userdetails1.getProgramId(),
									feedback.getAcadSession(),
									feedback.getAcadYear(),feedback.getAcadMonth(),
									String.valueOf(feedback.getCampusId())));
				} else {

					feedback.setStudentFeedbacks(studentFeedbackService
							.getStudentsByAcadSession(
									userdetails1.getProgramId(),
									feedback.getAcadSession(),
									feedback.getAcadYear(),feedback.getAcadMonth()));
				}
			}

			// "50332686"
			

			/*
			 * List<Course> courseList =
			 * courseService.findByCoursesBasedOnProgramName(ProgramName);
			 * 
			 * 
			 * Map<Long, Course> mapofCourseId = new HashMap<>();
			 * 
			 * for (Course course : courseList){
			 * 
			 * mapofCourseId.put(course.getId(), course); }
			 */

			for (StudentFeedback studentFeedback : feedback
					.getStudentFeedbacks()) {
				
				studentFeedback.setCreatedBy(username);
				studentFeedback.setLastModifiedBy(username);
				studentFeedback.setFeedbackId(feedback.getId());
				studentFeedback.setStartDate(feedback.getStartDate());
				studentFeedback.setEndDate(feedback.getEndDate());

			}
			studentFeedbackService.insertBatch(feedback.getStudentFeedbacks());
			

			if (feedback.getStudentFeedbacks().size() > 0) {
				setSuccess(redirectAttr, "Students Allocated successfully");
			} else {
				setNote(redirectAttr,
						"No Students Found To Allocate For Semester--->"
								+ feedback.getAcadSession());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating feedback");
			return "redirect:/addStudentFeedbackForm";
		}
		redirectAttr.addAttribute("feedbackId", feedback.getId());
		return "redirect:/searchAllFacultiesForFeedback";
	}

	@RequestMapping(value = "/updateStudentFeedback", method = RequestMethod.POST)
	public String updateStudentFeedback(
			@ModelAttribute StudentFeedback studentFeedback, Model m,
			Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("feedback", "Update Feedback",
				false, false));
		try {

			StudentFeedback studentFeedbackFromDb = studentFeedbackService
					.findByID(studentFeedback.getId());
			studentFeedbackFromDb = LMSHelper.copyNonNullFields(
					studentFeedbackFromDb, studentFeedback);
			studentFeedbackFromDb.setLastModifiedBy(username);

			studentFeedbackService.update(studentFeedbackFromDb);

			setSuccess(m, "Feedback updated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Feedback");
			return "feedback/addFeedback";
		}
		return "feedback/feedbackDetails";
	}

	/*
	 * @Secured("ROLE_STUDENT")
	 * 
	 * @RequestMapping(value = "/startStudentFeedback", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * startStudentFeedback(@RequestParam Long feedbackId,
	 * 
	 * @ModelAttribute Feedback feedback, Model m, Principal principal) { String
	 * username = principal.getName(); m.addAttribute("webPage", new
	 * WebPage("studentFeedback", "Feedback", true, true));
	 * logger.info("fededback id:" + feedback.getId()); StudentFeedback
	 * studentFeedbackDB = studentFeedbackService
	 * .findByfeedbackIDAndUsername(feedbackId, username); if (null ==
	 * studentFeedbackDB) { setNote(m,
	 * "You have not been assigned the feedback"); return
	 * "feedback/studentFeedback"; } try {
	 * studentFeedbackDB.setLastModifiedBy(username);
	 * 
	 * studentFeedbackService.upsert(studentFeedbackDB);
	 * 
	 * Feedback feedbackDB = feedbackService.findByID(feedbackId);
	 * LMSHelper.copyNonNullFields(feedback, feedbackDB); List<FeedbackQuestion>
	 * feedbackQuestions = feedbackQuestionService
	 * .findStudentResponseByFeedbackIdAndUsername(feedbackId, username);
	 * 
	 * logger.info("feedbackQuestions" + feedbackQuestions);
	 * m.addAttribute("feedbackName",feedbackDB.getFeedbackName());
	 * 
	 * feedback.setFeedbackQuestions(feedbackQuestions);
	 * feedback.setStudentFeedback(studentFeedbackDB);
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e); setError(m,
	 * "Feedback could not be started"); // return "feedback/studentFeedback";
	 * return "feedback/studentFeedbackNew"; } // return
	 * "feedback/studentFeedback"; return "feedback/studentFeedbackNew"; }
	 */

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/viewStudentResponseFeedback", method = RequestMethod.GET)
	public String viewStudentResponseFeedback(
			@RequestParam(required = false) Long feedbackId,
			@RequestParam String username, @ModelAttribute Feedback feedback,
			Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {
		m.addAttribute("webPage", new WebPage("viewFeedback",
				"Feedback Details", true, false));
		try {

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Feedback feedbackFromDb = feedbackService.findByID(feedbackId);
			m.addAttribute("feedback", feedbackFromDb);

			// ----------------------------------
			// FeedbackQuestion feedq = new FeedbackQuestion();

			List<FeedbackQuestion> StudentResponseList = feedbackQuestionService
					.getStudentFeedbackResponseList(feedbackId, username);
			/*
			 * .getStudentFeedbackResponsePage(feedbackId, username, pageNo,
			 * pageSize);
			 */

			for (FeedbackQuestion fq : StudentResponseList) {
				
				m.addAttribute("feedbackquestion", fq);
			}

			UserCourse userCourse = new UserCourse();

			// System.out.println(" faculty is :" + feedback.getFacultyId());

			// m.addAttribute("feedbackResponse",studentFeedback);
			m.addAttribute("page", StudentResponseList);
			m.addAttribute("q", getQueryString(feedback));

			// ---------------------------------

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in updating Feedback");
			
			return "feedback/viewStudentFeedbackResponse";

		}
		return "feedback/viewStudentFeedbackResponse";
	}

	/**
	 * Questions response
	 */
	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentFeedbackResponseForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addStudentFeedbackResponseForm(
			@RequestParam Long studentFeedbackId, Model m, Principal principal) {
		String username = principal.getName();

		
		m.addAttribute("webPage", new WebPage("addStudentFeedbackResponse",
				"Take Feedback", false, false));
		// Update the start time and attempts
		StudentFeedback studentFeedback = studentFeedbackService
				.findByID(studentFeedbackId);
		studentFeedback.setLastModifiedBy(username);
		studentFeedbackService.update(studentFeedback);

		List<FeedbackQuestion> feedbackQuestions = feedbackQuestionService
				.findByFeedbackId(studentFeedback.getFeedbackId());
		m.addAttribute("feedbackQuestions", feedbackQuestions);
		return "addStudentFeedbackResponse";
	}

	/**
	 * Supports JSON
	 */
	@RequestMapping(value = "/getStudentsForFeedback", method = RequestMethod.GET)
	public @ResponseBody List<StudentFeedback> getStudentsForFeedback(
			@ModelAttribute StudentFeedback studentFeedback) {
		

		return studentFeedbackService.getStudentsForFeedback(
				studentFeedback.getFacultyId(), studentFeedback.getCourseId(),
				studentFeedback.getAcadMonth(), studentFeedback.getAcadYear());
	}

	@RequestMapping(value = "/getFacultyByCourseForFeedback", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyByCourse(
			@RequestParam(name = "courseId") String courseId,

			Principal principal) {
		
		String json = "";
		String username = principal.getName();

		List<UserCourse> faculty = userCourseService
				.findAllFacultyWithCourseId(Long.valueOf(courseId));

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
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return json;

	}

	@RequestMapping(value = "/getSemesterByAcadYearForFeedback", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getSemesterByAcadYearForFeedback(
			@RequestParam(name = "acadYear") String acadYear,
			Principal principal) {
		
		String json = "";
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		/*List<UserCourse> acadSession = userCourseService
				.findAllAcadSessionsWithAcadYear(Long.valueOf(acadYear),
						userdetails1.getProgramId());*/
		List<UserCourse> acadSession = userCourseService
				.findAllAcadSessionsWithAcadYear(Long.valueOf(acadYear));
		
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (UserCourse acad : acadSession) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(acad.getAcadSession(), acad.getAcadSession());
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

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/addStudentFeedbackResponse", method = RequestMethod.POST)
	public @ResponseBody StudentFeedbackResponse addStudentFeedbackResponse(
			@ModelAttribute Feedback feedback, Principal principal) {
		String username = principal.getName();
		FeedbackQuestion feedbackQuestion = feedback.getFeedbackQuestions()
				.get(feedback.getFeedbackQuestions().size() - 1);
		StudentFeedbackResponse studentFeedbackResponse = feedbackQuestion
				.getStudentFeedbackResponse();

		studentFeedbackResponse.setUsername(username);
		studentFeedbackResponse.setCreatedBy(username);
		studentFeedbackResponse.setLastModifiedBy(username);
		studentFeedbackResponseService.insert(studentFeedbackResponse);
		return studentFeedbackResponse;
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/completeStudentFeedback", method = RequestMethod.POST)
	public @ResponseBody StudentFeedback completeStudentFeedback(
			@ModelAttribute StudentFeedback studentFeedback, Principal principal) {
		String username = principal.getName();
		
		StudentFeedback studentFeedbackDB = studentFeedbackService
				.findByID(studentFeedback.getId());
		
		studentFeedbackDB.setUsername(username);
		studentFeedbackDB.setFeedbackCompleted("Y");
		studentFeedbackService.upsert(studentFeedbackDB);
		return studentFeedbackDB;
	}

	@RequestMapping(value = "/addStudentFeedbackFormForCourses", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addStudentFeedbackFormForCourses(
			@ModelAttribute Feedback feedback, Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {
		
		m.addAttribute("webPage", new WebPage(
				"addStudentFeedbackFormForCourses", "Create Feedback", true,
				false));

		// ----------------

		if (feedback.getId() != null) {

			
			List<StudentFeedback> studentFeedbackDuplicates = studentFeedbackService
					.checkStudentFeedbackDuplicates(String.valueOf(feedback
							.getId()));
			
			if (studentFeedbackDuplicates.size() > 0) {
				

				studentFeedbackService.deleteDuplicateStudentFeedback(String
						.valueOf(feedback.getId()));

				
			}
		}

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		String programId = userdetails1.getProgramId();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("showCourses", false);
		if (!feedback.getStudentFeedbacks().isEmpty()) {
			
			m.addAttribute("showCourses", true);
			// m.addAttribute("courses", feedback.getCourses());

		}
		
		if (feedback.getId() != null && feedback.getAcadYear() != null
				&& feedback.getAcadSession() != null && feedback.getProgramId() != null && feedback.getAcadMonth()!=null) {
			
			List<StudentFeedback> studentFeedbacksByCourse = studentFeedbackService
					.getStudentFeedbacksByCourse(feedback.getProgramId(),
							String.valueOf(feedback.getId()),
							String.valueOf(feedback.getAcadYear()),
							feedback.getAcadSession(),feedback.getAcadMonth());
			// logger.info("no.of courses found--->" + courses.size());
			// feedback.setCourses(courses);
			// redirectAttribute.addAttribute("courses", courses);
			feedback.setProgramName(programService.getProgramName(feedback.getProgramId()));
			feedback.setStudentFeedbacks(studentFeedbacksByCourse);
		}
		
		List<String> acadMonths = courseService.getAllAcadMonth();

		m.addAttribute("acadMonths", acadMonths);
		m.addAttribute("feedback", feedback);
		// ---------------
		return "feedback/addStudentFeedbackByCourses";
	}

	@RequestMapping(value = "/searchCoursesByInputParams", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchCoursesByInputParams(@ModelAttribute Feedback feedback,
			Model m, Principal principal, RedirectAttributes redirectAttribute) {
		
		m.addAttribute("webPage", new WebPage("addStudentFeedback",
				"Create Feedback", true, false));
		try {
			// ----------------
			String username = principal.getName();
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			String programId = userdetails1.getProgramId();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			

			/*
			 * List<Course> courses = courseService
			 * .findCoursesByAcadYearMonthSession(
			 * String.valueOf(feedback.getAcadYear()),
			 * feedback.getAcadSession(), userdetails1.getProgramId());
			 */

			List<StudentFeedback> studentFeedbacksByCourse = studentFeedbackService
					.getStudentFeedbacksByCourse(feedback.getProgramId(),
							String.valueOf(feedback.getId()),
							String.valueOf(feedback.getAcadYear()),
							feedback.getAcadSession(),feedback.getAcadMonth());
			// logger.info("no.of courses found--->" + courses.size());
			// feedback.setCourses(courses);
			// redirectAttribute.addAttribute("courses", courses);
			feedback.setProgramName(programService.getProgramName(feedback.getProgramId()));
			feedback.setStudentFeedbacks(studentFeedbacksByCourse);
			
			redirectAttribute.addFlashAttribute("feedback", feedback);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		// ---------------
		return "redirect:/addStudentFeedbackFormForCourses";
	}

	@RequestMapping(value = "/saveStudentFeedbackByCourse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentFeedbackByCourse(
			@ModelAttribute Feedback feedback, RedirectAttributes redirectAttr,
			Principal principal, Model m) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		redirectAttr.addFlashAttribute("feedback", feedback);
		Date startDate = Utils.converFormatsDate(feedback.getStartDate());
		Date endDate = Utils.converFormatsDate(feedback.getEndDate());
		System.out.println("s Date"+startDate);
		System.out.println("e Date"+endDate);
		if(startDate.after(endDate)) {
		setError(redirectAttr, "Date Range is Not Valid");
			return "redirect:/addStudentFeedbackFormForCourses";
		}

		Map<String, List<String>> mapOfCourseIdAndUsernameList = new HashMap<>();
		Map<String, List<String>> mapOfCourseIdAndFacultyList = new HashMap<>();
		List<UserCourse> listOfCourseIdsAndUsername = userCourseService
				.getStudentsByCourseIds(feedback.getCourseIds());
		List<UserCourse> listOfCourseIdsAndFaculties = userCourseService
				.getFacultiesByCourseIds(feedback.getCourseIds());
		for (String id : feedback.getCourseIds()) {
			List<String> usernameList = new ArrayList<>();
			for (UserCourse uc : listOfCourseIdsAndUsername) {
				if (id.equals(String.valueOf(uc.getCourseId()))) {
					usernameList.add(uc.getUsername());
				}
			}
			mapOfCourseIdAndUsernameList.put(id, usernameList);
		}
		for (String id : feedback.getCourseIds()) {
			List<String> usernameList = new ArrayList<>();
			for (UserCourse uc : listOfCourseIdsAndFaculties) {
				if (id.equals(String.valueOf(uc.getCourseId()))) {
					usernameList.add(uc.getUsername());
				}
			}
			mapOfCourseIdAndFacultyList.put(id, usernameList);
		}
		
		try {
			/*
			 * for (StudentFeedback studentFeedback : feedback
			 * .getStudentFeedbacks()) { logger.info("courseId is--->" +
			 * studentFeedback.getCourseId());
			 * feedback.setStudents(mapOfCourseIdAndUsernameList
			 * .get(String.valueOf(studentFeedback.getCourseId())));
			 * logger.info("facultyId got--->"); for (String facultyId :
			 * mapOfCourseIdAndFacultyList.get(String
			 * .valueOf(studentFeedback.getCourseId()))) {
			 * 
			 * studentFeedback.setFacultyId(facultyId);
			 * studentFeedback.setCourseId(studentFeedback.getCourseId());
			 * 
			 * studentFeedback.setCreatedBy(username);
			 * studentFeedback.setLastModifiedBy(username); Course c =
			 * courseService.findByID(studentFeedback .getCourseId());
			 * studentFeedback.setAcadMonth(c.getAcadMonth()); studentFeedback
			 * .setAcadYear(Integer.valueOf(c.getAcadYear())); }
			 */
			List<StudentFeedback> studentFeedbackList = new ArrayList<>();
			for (String courseId : feedback.getCourseIds()) {
				for (String facultyId : mapOfCourseIdAndFacultyList
						.get(courseId)) {

					for (String studentUsername : mapOfCourseIdAndUsernameList
							.get(courseId)) {
						Course c = courseService.findByID(Long
								.valueOf(courseId));
						StudentFeedback studentFeedback = new StudentFeedback();
						studentFeedback.setAcadMonth(c.getAcadMonth());
						studentFeedback.setAcadYear(Integer.parseInt(c
								.getAcadYear()));
						studentFeedback.setCourseId(Long.valueOf(courseId));
						;
						studentFeedback.setUsername(studentUsername);
						studentFeedback.setFacultyId(facultyId);
						studentFeedback.setMandatory("N");
						studentFeedback.setAllowAfterEndDate("N");
						studentFeedback.setFeedbackId(feedback.getId());
						studentFeedback.setStartDate(feedback.getStartDate());
						studentFeedback.setEndDate(feedback.getEndDate());
						studentFeedback.setCreatedBy(username);
						studentFeedback.setLastModifiedBy(username);
						studentFeedbackList.add(studentFeedback);
					}

				}
			}
			List<StudentFeedback> studentFeedbacksByCourse = studentFeedbackService
					.getStudentFeedbacksByCourse(feedback.getProgramId(),
							String.valueOf(feedback.getId()),
							String.valueOf(feedback.getAcadYear()),
							feedback.getAcadSession(),feedback.getAcadMonth());
			// logger.info("no.of courses found--->" + courses.size());
			// feedback.setCourses(courses);
			// redirectAttribute.addAttribute("courses", courses);
			feedback.setProgramName(programService.getProgramName(feedback.getProgramId()));
			feedback.setStudentFeedbacks(studentFeedbacksByCourse);
			
			redirectAttr.addFlashAttribute("feedback", feedback);
			studentFeedbackService.insertBatch(studentFeedbackList);
			setSuccess(redirectAttr, "Students Allocated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttr, "Error in allocating feedback");
			return "redirect:/addStudentFeedbackFormForCourses";
		}
		return "redirect:/addStudentFeedbackFormForCourses";
	}

}
