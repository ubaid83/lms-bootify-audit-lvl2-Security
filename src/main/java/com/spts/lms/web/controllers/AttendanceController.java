package com.spts.lms.web.controllers;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.attendance.Attendance;
import com.spts.lms.beans.attendance.RtasAttendance;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.StudentDetails;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.attendance.AttendanceService;
import com.spts.lms.services.attendance.RtasAttendanceService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

@Controller
public class AttendanceController extends BaseController {
	private static final Logger logger = Logger
			.getLogger(AttendanceController.class);

	@Autowired
	CourseService courseService;

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	AttendanceService attendanceService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	RtasAttendanceService rtasAttendanceService;

	@Value("${studentAttendanceURL}")
	private String studentAttendanceURL;

	@Value("${app}")
	private String app;

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/addAttendanceForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addAttendanceForm(Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("message", "Attendance", true,
				false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("courses", courseService.findAllActive());

		m.addAttribute("facultyList", userService.findAllFaculty());

		m.addAttribute("attendance", new Attendance());
		return "attendance/addAttendance";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchStudentForAttendance", method = { RequestMethod.POST })
	public String searchStudentForAttendance(
			@ModelAttribute("attendance") Attendance attendance, Model m,
			Principal principal, @RequestParam(required = false) Long id) {
		m.addAttribute("webPage", new WebPage("assignment", "Mark Attendance",
				true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<UserCourse> listOfStudents = userCourseService
				.getUsersBasedOnCourse(Long.valueOf(attendance.getCourseId()),
						Role.ROLE_STUDENT.name());
		
		if (listOfStudents != null && listOfStudents.size() > 0) {
			m.addAttribute("listOfStudents", listOfStudents);
			m.addAttribute("rowCount", listOfStudents.size());
			m.addAttribute("attendance", attendance);
		} else {
			setError(m, "No Students found for this course");
		}
		return "attendance/markAttendance";

	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/saveStudentAttendance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentAttendance(@ModelAttribute Attendance attendance,
			Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("groups", "Create Group", true,
				false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		ArrayList<Attendance> studentMappingList = new ArrayList<Attendance>();
		
		List<String> asg = attendance.getStudents();
		if (asg != null && asg.size() > 0) {
			for (String Username : attendance.getStudents()) {
				Attendance attb = new Attendance();

				attb.setFacultyId(attendance.getFacultyId());
				attb.setCourseId(attendance.getCourseId());
				attb.setUsername(Username);
				attb.setStartDate(attendance.getStartDate());
				attb.setEndDate(attendance.getEndDate());
				studentMappingList.add(attb);
			}
			

			attendanceService.insertBatch(studentMappingList);
			setSuccess(m, "Attendance marked for "
					+ attendance.getStudents().size()
					+ " students successfully");

			return searchStudentForAttendance(attendance, m, principal,
					attendance.getId());

		}
		m.addAttribute("attendance", attendance);
		return "attendance/markAttendance";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/viewDailyAttendance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewDailyAttendance(Model m, Principal principal,
			@ModelAttribute Attendance attendance,
			@RequestParam(required = false) String startDate)
			throws ParseException {
		m.addAttribute("webPage", new WebPage("assignment", "View Attendnace",
				true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Attendance> dailyAttendanceList = new ArrayList<>();
		List<Attendance> courseList = attendanceService.findByUser(username);
		
		for (Attendance a : courseList) {
			String courseId = a.getCourseId();
			
			Course c = courseService.findByID(Long.valueOf(courseId));
			a.setCourseName(c.getCourseName());
			courseList.set(courseList.indexOf(a), a);
			m.addAttribute("courseName", c.getCourseName());
			String s = a.getStartDate();
			
			String newDate = s.substring(0, 10);
			
			if (startDate != null) {
				String sd = startDate.substring(1);
			

				if (newDate.equals(sd)) {
					dailyAttendanceList.add(a);
					

				}
			}
		}

		m.addAttribute("dailyAttendanceList", dailyAttendanceList);
		m.addAttribute("attendance", attendance);
		m.addAttribute("courseList", courseList);
	
		return "attendance/dailyBasisAttendance";
	}

	@Secured({"ROLE_STUDENT","ROLE_PARENT"})
	@RequestMapping(value = "/viewDailyAttendanceByStudent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView viewDailyAttendanceByStudent(Model m,
			Principal principal, RedirectAttributes rm) throws ParseException {
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			m.addAttribute("webPage", new WebPage("assignment",
					"View Attendnace", true, false));

			Token userDetails =(Token) principal;
			String username = principal.getName();
			User user = new User();
			if(userDetails.getAuthorities().contains(Role.ROLE_PARENT)){
				username = StringUtils.substring(username, 0,
						StringUtils.lastIndexOf(username, "_P"));
				user = userService.findDetailsUserName(username);
			}else{
				user = userService.findDetailsUserName(username);
			}
			
			
//			List<Course> userCourseByUsername = courseService.findByUserActive(username);
			List<Course> userCourseByUsername = courseService.findCoursesForAttd(username);
			StudentDetails studentDetails = new StudentDetails();
			Map<String, String> mapOfSAPEventToLMSCourse = new HashMap<>();
			
			for (Course c : userCourseByUsername) {

				String sapEventId = StringUtils.substring(
						String.valueOf(c.getId()), 0, 8);
				
				mapOfSAPEventToLMSCourse.put(sapEventId, c.getCourseName());
			}
			
			studentDetails.setUsername(username);
			studentDetails.setContext(app.trim().toString());
			studentDetails.setProgramName(user.getProgramName());
			studentDetails.setAcadSession(user.getAcadSession());
			studentDetails.setFullName(user.getFirstname()+' '+user.getLastname());
			studentDetails.setRollNo(user.getRollNo());

		
			if (user.getProgramId() != null) {
				
				studentDetails
						.setProgramId(String.valueOf(user.getProgramId()));
			}
			studentDetails
					.setMapOfSAPEventToEventName(mapOfSAPEventToLMSCourse);

			String projectUrl = studentAttendanceURL
					+ "/studentAttendanceHandShake";
			RedirectView view = new RedirectView(projectUrl);

			json = mapper.writeValueAsString(studentDetails);
			/* logger.info("Json" + json); */
			rm.addAttribute("json", json);
			return new ModelAndView(view);
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return new ModelAndView("/");
	}

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/viewRtasAttendance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewRtasAttendance(Model m, Principal principal,
			@ModelAttribute RtasAttendance rtasAttendance,
			@RequestParam(required = false) String date) throws ParseException {
		m.addAttribute("webPage", new WebPage("assignment", "View Attendnace",
				true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<RtasAttendance> rtas = new ArrayList<>();
		List<RtasAttendance> listOfStudent = rtasAttendanceService
				.findByUser(username);
		
		for (RtasAttendance a : listOfStudent) {
			String s = a.getDate();
			
			if (date != null) {
				String sd = date.substring(1);
				

				if (date.equals(sd)) {
					rtas.add(a);
					

				}
			}
		}
		m.addAttribute("rtas", rtas);
		m.addAttribute("listOfStudent", listOfStudent);
		m.addAttribute("rtasAttendance", rtasAttendance);

		return "attendance/rtasAttendance";
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/getFacultyByCourseForAttendance", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyByCourseForAttendance(
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
			logger.error("Exception" + e);
		}
		return json;

	}

}
