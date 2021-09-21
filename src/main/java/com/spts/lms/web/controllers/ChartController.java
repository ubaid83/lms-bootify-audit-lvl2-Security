package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.charts.Charts;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

@Controller
public class ChartController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	UserService userService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	TestService testService;

	@Autowired
	UserCourseService userCourseService;

	@Value("${lms.enrollment.years}")
	private String[] enrollmentYears;

	@ModelAttribute("yearList")
	public String[] getYearList() {
		return enrollmentYears;
	}

	private static final Logger logger = Logger
			.getLogger(ChartController.class);

	@RequestMapping(value = "/createReportsForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String createReportsForm(Model m,
			@RequestParam(required = false) Long courseId, Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("viewMyMessage", "Reports", true,
				true, true, true, false));
		m.addAttribute("allReports", new ArrayList<String>());
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Charts charts = new Charts();
		if (courseId != null) {
			m.addAttribute("showTable", true);
			// m.addAttribute("studentUsername", "70061015074");
			m.addAttribute("courseId", courseId);
			// m.addAttribute("assignmentId", "208");
			// m.addAttribute("testId", "150");
			m.addAttribute("assignmentList", assignmentService
					.findByFacultyAndCourseActive(username, courseId));
			m.addAttribute("testList",
					testService.findByFacultyAndCourse(username, courseId));
			charts.setCourseId(courseId);
		} else {
			m.addAttribute("showTable", false);
		}

		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		m.addAttribute("charts", charts);
		if (userdetails1.getAuthorities().contains(Role.ROLE_STUDENT)) {

			return "reportUtil/createReportsForStudent";
		}

		return "reportUtil/createReport";

	}

	@RequestMapping(value = "/generateReportsForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String generateReportsForm(Model m,
			@RequestParam(required = false) Long courseId, Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("viewMyMessage", "Reports", true,
				true, true, true, false));
		m.addAttribute("allReports", new ArrayList<String>());
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Charts charts = new Charts();
		m.addAttribute("showTable", true);
		// m.addAttribute("studentUsername", "70061015074");
		m.addAttribute("courseId", courseId);
		// m.addAttribute("assignmentId", "208");
		// m.addAttribute("testId", "150");
		m.addAttribute("assignmentList", assignmentService
				.findByFacultyAndCourseActive(username, courseId));
		m.addAttribute("testList",
				testService.findByFacultyAndCourse(username, courseId));
		charts.setCourseId(courseId);

		List<String> chartList = new ArrayList<String>();
		chartList.add("Assignment");
		chartList.add("Test");
		chartList.add("Content");
		chartList.add("Feedback");
		chartList.add("All");
		m.addAttribute("chartList", chartList);

		m.addAttribute("preUsersList", new ArrayList<Charts>());
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			m.addAttribute("allCourses",
					courseService.findByCoursesBasedOnProgramName(ProgramName));
			charts.setAllCourse(courseService
					.findByCoursesBasedOnProgramName(ProgramName));
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userdetails1.getAuthorities()
							.contains(Role.ROLE_STUDENT)) {
				m.addAttribute("allCourses", courseService.findByUserActive(
						principal.getName(), userdetails1.getProgramName()));
				charts.setAllCourse(courseService.findByUserActive(
						principal.getName(), userdetails1.getProgramName()));
			}

		}
		m.addAttribute("course", new Course());

		m.addAttribute("charts", charts);
		return "reportUtil/generateReport";

	}

	@RequestMapping(value = "/getUsersListByProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssignmentBasedOnCourse(
			@RequestParam(name = "role") Role role, Principal principal) {
	
		String json = "";
		Token userdetails1 = (Token) principal;

		List<User> usersList = userService.findAllUsersByProgramId(
				role.getAuthority(), Long.valueOf(userdetails1.getProgramId()));
		
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (User assg : usersList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(assg.getUsername(), assg.getUsername());
			res.add(returnMap);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			logger.error("Exception" + e);
		}
		return json;

	}

}
