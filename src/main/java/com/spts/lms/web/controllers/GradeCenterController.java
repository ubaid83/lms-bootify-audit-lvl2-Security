package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.classParticipation.ClassParticipation;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.grade.Grade;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.classParticipation.ClassParticipationService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Secured("ROLE_FACULTY")
@Controller
public class GradeCenterController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	UserService userService;

	@Autowired
	CourseService courseService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	TestService testService;

	@Autowired
	StudentAssignmentService studentAssigmentService;

	@Autowired
	StudentParentService studentParentService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	StudentTestService studentTestService;
	
	@Autowired
	ClassParticipationService classParticipationService;

	private static final Logger logger = Logger
			.getLogger(GradeCenterController.class);

	/*
	 * This is the non concurrent verion to compare with the concurrent version.
	 * Good amount of data required for testing well
	 * 
	 * @RequestMapping(value = "/gradeCenterSimple", method = RequestMethod.GET)
	 * public String gradeCenterSimple(@RequestParam Long courseId, Model m) {
	 * logger.info("Inside Grade Center"); long start =
	 * System.currentTimeMillis(); m.addAttribute("webPage",new
	 * WebPage("gradeList", "Grade Center", false, false));
	 * m.addAttribute("assignments", assignmentService.findByCourse(courseId));
	 * m.addAttribute("tests", testService.findByCourse(courseId));
	 * 
	 * List<User> studentTestScore =
	 * userService.findAllTestScoreByCourse(courseId); List<User>
	 * studentAssignmentScore =
	 * userService.findAllAssignmentScoreByCourse(courseId);
	 * 
	 * for (Iterator<User> studentAssignmentIterator =
	 * studentAssignmentScore.iterator(), studentTestIterator =
	 * studentTestScore.iterator(); studentAssignmentIterator.hasNext();) { User
	 * studentAssignment = studentAssignmentIterator.next(); User studentTest =
	 * studentTestIterator.next();
	 * studentTest.setStudentAssignmentScores(studentAssignment
	 * .getStudentAssignmentScores()); }
	 * 
	 * m.addAttribute("students", studentTestScore);
	 * System.out.println("Elapsed "+(System.currentTimeMillis() - start));
	 * return "grade/gradeList"; }
	 */

	@RequestMapping(value = "/gradeCenterForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String gradeCenterForm(Model m, Principal p) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		List<Course> listOfCoursesAssigned = getCourseBasedOnUser(userDetails,
				p);
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("listOfCoursesAssigned", listOfCoursesAssigned);
		m.addAttribute("showGradeCenterList", false);
		return "grade/gradeList";
	}

	@RequestMapping(value = "/getCoursesByUsername", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCoursesByUsername(
			@RequestParam(name = "username") String uname,

			Principal principal, Model m) {

		String json = "";
		String username = principal.getName();

		List<UserCourse> findCoursesByUname = userCourseService
				.findByUsername(uname);
		List<Course> c = new ArrayList<Course>();

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (UserCourse uc : findCoursesByUname) {
			Map<String, String> returnMap = new HashMap();
			Course course = courseService.findByID(uc.getCourseId());
			c.add(course);
			returnMap.put(String.valueOf(uc.getCourseId()),
					course.getCourseName());

			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block

		}
		return json;

	}

	/*
	 * @RequestMapping(value = "/gradeCenterFormForParents", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * gradeCenterFormForParents(Model m, Principal p) {
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; String username = p.getName();
	 * System.out.println("username------->" + username);
	 * m.addAttribute("webPage", new WebPage("gradeList", "Grade Center", false,
	 * false)); List<StudentParent> listOfStudents = studentParentService
	 * .findStudentsByParentUname(username);
	 * 
	 * Course course = new Course(); System.out.println("list of students:" +
	 * listOfStudents); m.addAttribute("listOfStudents", listOfStudents);
	 * m.addAttribute("course", course); m.addAttribute("showGradeCenterList",
	 * false); return "grade/gradeListForParents"; }
	 */

	/*@RequestMapping(value = "/gradeCenterFormForParents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String gradeCenterFormForParents(Model m, Principal p) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(username);
		for (StudentParent sc : listOfStudents) {
			List<Course> courseListForStudent = courseService.findByUserActive(
					sc.getStud_username(), userdetails1.getProgramName());
			m.addAttribute("courseListForStudent", courseListForStudent);
			m.addAttribute("studentUname", sc.getStud_username());

		}

		Course course = new Course();

		m.addAttribute("listOfStudents", listOfStudents);
		m.addAttribute("course", course);
		m.addAttribute("showGradeCenterList", false);
		return "grade/gradeListForParents";
	}*/
	
	@RequestMapping(value = "/gradeCenterFormForParents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String gradeCenterFormForParents(Model m, Principal p) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(username);
		
		logger.info("programId---->" + userdetails1.getProgramId());
		List<Course> courseList = courseService.findByUser(listOfStudents.get(0).getStud_username());
		
		User up = userService.findByUserName(listOfStudents.get(0).getStud_username());
		
		HashMap<String,List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();
		
		for(Course c : courseList){
			
			if(!sessionWiseCourseList.containsKey(c.getAcadSession())){
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			}else{
				List<Course> courseLst = sessionWiseCourseList.get(c.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}
			
		}
		
		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBean", u);
		m.addAttribute("userBeanParent", up);
		m.addAttribute("acadSessionParent", up.getAcadSession()); 
		
			
		
		return "grade/gradeListForParents";
	}

	@RequestMapping(value = "/gradeCenter", method = RequestMethod.GET)
	public String gradeCenter(@RequestParam Long courseId, Model m, Principal p) throws InterruptedException, ExecutionException {

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		Course course = courseService.findByID(courseId);
		
		Token userDetails1 = (Token)p;
		
		

		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		Future<List<Map<String, Object>>> result = userService
				.findAllTestScorAndAssigmenteByCourseAsyncList(courseId);
		if(userDetails1.getAuthorities().contains(Role.ROLE_FACULTY) && courseId!=null) {
			
			List<String> courseIdsForFaculty = courseService.
					courseListByParams(course.getModuleId(), course.getAcadYear(), String.valueOf(course.getProgramId()),p.getName());
			
			Future<List<Map<String, Object>>> resultAdminTest = userService
					.findAllTestScoreAndAssigmentByCourseListCreatedByAdminForFaculty(courseIdsForFaculty);
			
			result.get().addAll(resultAdminTest.get());
		}
		Map<String, Grade> gradeObj = new LinkedHashMap<String, Grade>();
		Set<String> assigmentList = new HashSet<String>();
		Set<String> testList = new HashSet<String>();
		Set<String> cpList = new HashSet<String>();
		Set<String> offlineList = new HashSet<>();
		Map<String, String> usernameList = new HashMap<>();
		Map<String, String> rollNoList = new HashMap<>();
		try {
			result.get().sort(
					Comparator.comparing(o -> String.valueOf(o.get("rollNo"))));
			for (Map<String, Object> temp : result.get()) {
				String name = (String) temp.get("name");
				String sapid = (String) temp.get("sapid");
				String rollNo = (String) temp.get("rollNo");
				Grade g = null;
				if (gradeObj.containsKey(sapid)) {
					g = gradeObj.get(sapid);

				} else {
					g = new Grade();
					g.setName(name);
				}

				usernameList.put(sapid, name);
				rollNoList.put(sapid, rollNo);
				String type = (String) temp.get("typ");
				switch (type) {
				case "ASSIGNEMENT":
					assigmentList.add(Utils.getBlankIfNull(temp
							.get("nameToShow")));
					g.getAssigmentToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				case "TEST":
					testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getTestToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					g.getStatusMap().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("status")));

					break;
				case "CP":
					cpList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getCpToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;

				case "Offline-Test":
					offlineList
							.add(Utils.getBlankIfNull(temp.get("nameToShow")));

					g.getOfflineToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				}

				gradeObj.put(sapid, g);

			}

			/*
			 * logger.info("resultAssignments.get()" + resultAssignments.get());
			 * m.addAttribute("students", studentTestScore);
			 * m.addAttribute("assignments", resultAssignments.get());
			 * m.addAttribute("tests", resultTests.get());
			 */

			m.addAttribute("students", gradeObj);
			m.addAttribute("tests", testList);
			m.addAttribute("cpList", cpList);
			m.addAttribute("usernameList", usernameList);
			m.addAttribute("offlineList", offlineList);
			m.addAttribute("rollNoList", rollNoList);
			m.addAttribute("assignments", assigmentList);
			m.addAttribute("showGradeCenterList", true);
			m.addAttribute("listOfCoursesAssigned",
					getCourseBasedOnUser(userDetails, p));
			m.addAttribute("cId", courseId);
			m.addAttribute("course", course);

		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "grade/gradeList";
	}

	/*
	 * @RequestMapping(value = "/gradeCenterForParents", method =
	 * RequestMethod.GET) public String gradeCenterForParents(@RequestParam Long
	 * courseId,
	 * 
	 * @RequestParam String username, Model m, Principal p) {
	 * logger.info("Inside Grade Center" + courseId); m.addAttribute("webPage",
	 * new WebPage("gradeList", "Grade Center", false, false)); String uname =
	 * p.getName(); Course course = courseService.findByID(courseId);
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) p; List<StudentParent>
	 * listOfStudents = studentParentService .findStudentsByParentUname(uname);
	 * 
	 * Future<List<Map<String, Object>>> result = userService
	 * .findAllTestScorAndAssigmenteByCourseAsyncList(courseId, username);
	 * 
	 * Map<String, Grade> gradeObj = new HashMap<>(); Set<String> assigmentList
	 * = new HashSet<String>(); Set<String> testList = new HashSet<String>();
	 * for (StudentParent sc : listOfStudents) { List<Course>
	 * courseListForStudent = courseService
	 * .findByUserActive(sc.getStud_username());
	 * m.addAttribute("courseListForStudent", courseListForStudent);
	 * 
	 * } try {
	 * 
	 * for (Map<String, Object> temp : result.get()) { String name = (String)
	 * temp.get("name"); Grade g = null; if (gradeObj.containsKey(name)) {
	 * System.out.println("name :" + temp.get("name")); g = gradeObj.get(name);
	 * 
	 * } else { System.out.println("----------name :-----------" +
	 * temp.get("name")); g = new Grade(); g.setName(name); }
	 * 
	 * String type = (String) temp.get("typ"); switch (type) { case
	 * "ASSIGNEMENT": assigmentList.add(Utils.getBlankIfNull(temp
	 * .get("nameToShow"))); g.getAssigmentToScore().put(
	 * Utils.getBlankIfNull(temp.get("nameToShow")),
	 * Utils.getBlankIfNull(temp.get("score")));
	 * 
	 * break;
	 * 
	 * case "TEST": testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
	 * g.getTestToScore().put( Utils.getBlankIfNull(temp.get("nameToShow")),
	 * Utils.getBlankIfNull(temp.get("score"))); break;
	 * 
	 * }
	 * 
	 * gradeObj.put(name, g);
	 * 
	 * } System.out.println("grade size-------->" + gradeObj.size());
	 * System.out.println("assignment size:" + assigmentList.size());
	 * System.out.println("test size:" + testList.size());
	 * 
	 * 
	 * logger.info("resultAssignments.get()" + resultAssignments.get());
	 * m.addAttribute("students", studentTestScore);
	 * m.addAttribute("assignments", resultAssignments.get());
	 * m.addAttribute("tests", resultTests.get());
	 * 
	 * m.addAttribute("username", username); m.addAttribute("students",
	 * gradeObj); m.addAttribute("listOfStudents", listOfStudents);
	 * m.addAttribute("tests", testList); m.addAttribute("assignments",
	 * assigmentList); m.addAttribute("showGradeCenterList", true);
	 * 
	 * m.addAttribute("courseId", courseId);
	 * 
	 * m.addAttribute("course", course);
	 * 
	 * } catch (Exception e) { logger.error("Exception", e); } return
	 * "grade/gradeListForParents"; }
	 */

	/*@RequestMapping(value = "/gradeCenterForParents", method = RequestMethod.GET)
	public String gradeCenterForParents(@RequestParam Long courseId,
			@RequestParam String username, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		String uname = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Course course = courseService.findByID(courseId);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(uname);

		Future<List<Map<String, Object>>> result = userService
				.findAllTestScorAndAssigmenteByCourseAsyncList(courseId,
						username);

		Map<String, Grade> gradeObj = new HashMap<>();
		Set<String> assigmentList = new HashSet<String>();
		Set<String> testList = new HashSet<String>();
		Set<String> cpList = new HashSet<String>();
		for (StudentParent sc : listOfStudents) {
			List<Course> courseListForStudent = courseService.findByUserActive(
					sc.getStud_username(), userdetails1.getProgramName());
			m.addAttribute("courseListForStudent", courseListForStudent);
			m.addAttribute("studentUname", sc.getStud_username());

		}
		try {

			for (Map<String, Object> temp : result.get()) {
				String name = (String) temp.get("name");
				Grade g = null;
				if (gradeObj.containsKey(name)) {

					g = gradeObj.get(name);

				} else {

					g = new Grade();
					g.setName(name);
				}

				String type = (String) temp.get("typ");
				switch (type) {
				case "ASSIGNEMENT":
					assigmentList.add(Utils.getBlankIfNull(temp
							.get("nameToShow")));
					g.getAssigmentToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				case "TEST":
					testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getTestToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;
				case "CP":
					cpList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getCpToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;

				}

				gradeObj.put(name, g);

			}

			
			 * logger.info("resultAssignments.get()" + resultAssignments.get());
			 * m.addAttribute("students", studentTestScore);
			 * m.addAttribute("assignments", resultAssignments.get());
			 * m.addAttribute("tests", resultTests.get());
			 
			m.addAttribute("cpList", cpList);
			m.addAttribute("username", username);
			m.addAttribute("students", gradeObj);
			m.addAttribute("listOfStudents", listOfStudents);
			m.addAttribute("tests", testList);
			m.addAttribute("assignments", assigmentList);
			m.addAttribute("showGradeCenterList", true);

			m.addAttribute("courseId", courseId);

			m.addAttribute("course", course);

		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "grade/gradeListForParents";
	}*/
	
	@RequestMapping(value = "/gradeCenterForParents", method = RequestMethod.GET)
	public String gradeCenterForParents(@RequestParam Long courseId,
			@RequestParam String acadSessionParent, Model m, Principal p) {
		logger.info("Inside Grade Center" + courseId);
		logger.info("Inside Grade Centeracad" + acadSessionParent);
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		String uname = p.getName();
		uname = StringUtils.substring(uname, 0,
				StringUtils.lastIndexOf(uname, "_P"));
		logger.info("parStud--->"+uname);
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(uname);
		logger.info("ACAD SESSION------------------------->"
				+ u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		/*Course course = courseService.findByID(courseId);*/
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		List<Course> courseList = courseService.findByUser(uname);
		HashMap<String,List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();
		
		for(Course c : courseList){
			
			if(!sessionWiseCourseList.containsKey(c.getAcadSession())){
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			}else{
				List<Course> courseLst = sessionWiseCourseList.get(c.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}
			
		}
		
		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBeanParent", u);
		
		List<StudentAssignment> assignments = studentAssigmentService.getAssignmentsForGradeCenter(uname, courseId);
		logger.info("assignments---->"+assignments);
		m.addAttribute("assignments", assignments);
		
		List<StudentTest> tests = studentTestService.getTestsForGradeCenter(uname, courseId);
		logger.info("tests---->"+tests);
		m.addAttribute("tests", tests);
		
		List<ClassParticipation> cpList = classParticipationService.getCPForGradeCenter(uname, courseId.toString());
		logger.info("cpList---->" + cpList);
		m.addAttribute("cpList", cpList);
		
		m.addAttribute("courseId", courseId);
		m.addAttribute("acadSessionParent", acadSessionParent);
		
		return "grade/gradeListForParents";
	}

	@RequestMapping(value = "/gradeCenterForStudent", method = RequestMethod.GET)
	public String gradeCenterForStudent(@RequestParam Long courseId, Model m,
			Principal p) {

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		String uname = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(uname);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Course course = courseService.findByID(courseId);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		/*
		 * List<StudentParent> listOfStudents = studentParentService
		 * .findStudentsByParentUname(uname);
		 */

		Future<List<Map<String, Object>>> result = userService
				.findAllTestScorAndAssigmenteByCourseAsyncList(courseId, uname);

		Map<String, Grade> gradeObj = new HashMap<>();
		Set<String> assigmentList = new HashSet<String>();
		Set<String> testList = new HashSet<String>();
		Set<String> cpList = new HashSet<String>();
		Set<String> offlineList = new HashSet<>();

		/*
		 * for (StudentParent sc : listOfStudents) { List<Course>
		 * courseListForStudent = courseService
		 * .findByUserActive(sc.getStud_username());
		 * m.addAttribute("courseListForStudent", courseListForStudent);
		 * 
		 * 
		 * }
		 */
		try {

			for (Map<String, Object> temp : result.get()) {
				String name = (String) temp.get("name");
				Grade g = null;
				if (gradeObj.containsKey(name)) {

					g = gradeObj.get(name);

				} else {

					g = new Grade();
					g.setName(name);
				}

				String type = (String) temp.get("typ");
				switch (type) {
				case "ASSIGNEMENT":
					assigmentList.add(Utils.getBlankIfNull(temp
							.get("nameToShow")));
					g.getAssigmentToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				case "TEST":
					testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getTestToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					g.getStatusMap().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("status")));
					break;
				case "CP":
					cpList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getCpToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;

				case "Offline-Test":
					offlineList
							.add(Utils.getBlankIfNull(temp.get("nameToShow")));

					g.getOfflineToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				}

				gradeObj.put(name, g);

			}
			/* logger.info("grade size-------->" + gradeObj.size()); */

			if (assigmentList.size() == 0 && testList.size() == 0) {
				setNote(m, "No Grades for course");
			}
			m.addAttribute("students", gradeObj);
			m.addAttribute("cpList", cpList);
			m.addAttribute("tests", testList);
			m.addAttribute("assignments", assigmentList);
			m.addAttribute("showGradeCenterList", true);
			m.addAttribute("offlineList", offlineList);

			m.addAttribute("courseId", courseId);

			m.addAttribute("course", course);

		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "grade/gradeListForParents";
	}

	@RequestMapping(value = "/getAllAssignments", method = RequestMethod.GET)
	public String getAllAssignments(
			@RequestParam(required = false) Long courseId, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			Course course = courseService.findByID(courseId);
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
			List<Assignment> ListOfAssignments = assignmentService
					.getAllAssignments(courseId, username);
			List<Test> listOfTests = testService
					.getAllTests(courseId, username);

			m.addAttribute("ListOfAssignments", ListOfAssignments);
			m.addAttribute("listOfTests", listOfTests);

			if (ListOfAssignments == null && listOfTests == null) {
				setSuccess(m, "No Results Round");
			}
		} catch (Exception e) {
			setNote(m, "Please Try Again");
		}
		return "grade/gradeWieghtage";
	}

	@RequestMapping(value = "/gradeWieghtage", method = RequestMethod.GET)
	public String gradeWieghtage(@ModelAttribute Grade grade, Principal p,
			Model m) {
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute(
				"courseForWieghtage",
				courseService.findByUser(username,
						Long.parseLong(userdetails1.getProgramId())));
		m.addAttribute("garde", grade);
		return "grade/gradeWieghtage";
	}

	@RequestMapping(value = "/supportAdminGradeCenterForStudent", method = RequestMethod.GET)
	public String supportAdminGradeCenterForStudent(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String username, Model m,
			Principal p) {

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		/* String uname = p.getName(); */

		/*
		 * Token userdetails1 = (Token) p; String ProgramName =
		 * userdetails1.getProgramName();
		 */
		User u1 = userService.findByUserName(username);

		/*
		 * String acadSession = u1.getAcadSession();
		 * 
		 * m.addAttribute("Program_Name", ProgramName);
		 * m.addAttribute("AcadSession", acadSession);
		 */

		Course course = courseService.findByID(courseId);

		/*
		 * UsernamePasswordAuthenticationToken userDetails =
		 * (UsernamePasswordAuthenticationToken) p;
		 */
		/*
		 * List<StudentParent> listOfStudents = studentParentService
		 * .findStudentsByParentUname(uname);
		 */

		Future<List<Map<String, Object>>> result = userService
				.findAllTestScorAndAssigmenteByCourseAsyncList(courseId,
						username);

		Map<String, Grade> gradeObj = new HashMap<>();
		Set<String> assigmentList = new HashSet<String>();
		Set<String> testList = new HashSet<String>();
		Set<String> cpList = new HashSet<String>();

		/*
		 * for (StudentParent sc : listOfStudents) { List<Course>
		 * courseListForStudent = courseService
		 * .findByUserActive(sc.getStud_username());
		 * m.addAttribute("courseListForStudent", courseListForStudent);
		 * 
		 * 
		 * }
		 */
		try {

			for (Map<String, Object> temp : result.get()) {
				String name = (String) temp.get("name");
				Grade g = null;
				if (gradeObj.containsKey(name)) {

					g = gradeObj.get(name);

				} else {

					g = new Grade();
					g.setName(name);
				}

				String type = (String) temp.get("typ");
				switch (type) {
				case "ASSIGNEMENT":
					assigmentList.add(Utils.getBlankIfNull(temp
							.get("nameToShow")));
					g.getAssigmentToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				case "TEST":
					testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getTestToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					g.getStatusMap().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("status")));
					break;
				case "CP":
					cpList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getCpToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;

				}

				gradeObj.put(name, g);

			}
			/* logger.info("grade size-------->" + gradeObj.size()); */

			if (assigmentList.size() == 0 && testList.size() == 0) {
				setNote(m, "No Grades for course");
			}
			m.addAttribute("students", gradeObj);
			m.addAttribute("cpList", cpList);
			m.addAttribute("tests", testList);
			m.addAttribute("assignments", assigmentList);
			m.addAttribute("showGradeCenterList", true);

			m.addAttribute("courseId", courseId);

			m.addAttribute("course", course);
			m.addAttribute("username", username);

		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "grade/supportAdminGradeListForParents";
	}

	@RequestMapping(value = "/supportAdminGradeCenterForFaculty", method = RequestMethod.GET)
	public String supportAdminGradeCenterForFaculty(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String username, Model m,
			Principal p) {
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		Course course = courseService.findByID(courseId);

		/*
		 * String username = p.getName();
		 * 
		 * Token userdetails1 = (Token) p; String ProgramName =
		 * userdetails1.getProgramName();
		 */
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		/*
		 * m.addAttribute("Program_Name", ProgramName);
		 */
		m.addAttribute("AcadSession", acadSession);

		/*
		 * UsernamePasswordAuthenticationToken userDetails =
		 * (UsernamePasswordAuthenticationToken) p;
		 */
		Future<List<Map<String, Object>>> result = userService
				.findAllTestScorAndAssigmenteByCourseAsyncList(courseId);
		Map<String, Grade> gradeObj = new LinkedHashMap<String, Grade>();
		Set<String> assigmentList = new HashSet<String>();
		Set<String> testList = new HashSet<String>();
		Set<String> cpList = new HashSet<String>();
		Map<String, String> usernameList = new HashMap<>();
		Map<String, String> rollNoList = new HashMap<>();
		try {
			result.get().sort(
					Comparator.comparing(o -> String.valueOf(o.get("rollNo"))));
			for (Map<String, Object> temp : result.get()) {
				String name = (String) temp.get("name");
				String sapid = (String) temp.get("sapid");
				String rollNo = (String) temp.get("rollNo");
				Grade g = null;
				if (gradeObj.containsKey(name)) {
					g = gradeObj.get(name);

				} else {
					g = new Grade();
					g.setName(name);
				}

				usernameList.put(name, sapid);
				rollNoList.put(name, rollNo);
				String type = (String) temp.get("typ");
				switch (type) {
				case "ASSIGNEMENT":
					assigmentList.add(Utils.getBlankIfNull(temp
							.get("nameToShow")));
					g.getAssigmentToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				case "TEST":
					testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getTestToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					g.getStatusMap().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("status")));

					break;
				case "CP":
					cpList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getCpToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;

				}

				gradeObj.put(name, g);

			}

			/*
			 * logger.info("resultAssignments.get()" + resultAssignments.get());
			 * m.addAttribute("students", studentTestScore);
			 * m.addAttribute("assignments", resultAssignments.get());
			 * m.addAttribute("tests", resultTests.get());
			 */

			m.addAttribute("students", gradeObj);
			m.addAttribute("tests", testList);
			m.addAttribute("cpList", cpList);
			m.addAttribute("usernameList", usernameList);
			m.addAttribute("rollNoList", rollNoList);
			m.addAttribute("assignments", assigmentList);
			m.addAttribute("showGradeCenterList", true);
			m.addAttribute("username", username);
			/*
			 * m.addAttribute("listOfCoursesAssigned",
			 * getCourseBasedOnUser(userDetails, p));
			 */
			m.addAttribute("cId", courseId);
			m.addAttribute("course", course);

		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "grade/supportAdminGradeList";
	}

	@RequestMapping(value = "/gradeCenterFormForStudent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String gradeCenterFormForStudent(Model m, Principal p) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		/*
		 * m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
		 * false, false));
		 */
		logger.info("programId---->" + userdetails1.getProgramId());
		List<Course> courseList = courseService.findByUser(username);
		HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();
		for (Course c : courseList) {
			if (!sessionWiseCourseList.containsKey(c.getAcadSession())) {
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			} else {
				List<Course> courseLst = sessionWiseCourseList.get(c
						.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}
		}
		logger.info("acadSession" + u.getAcadSession());
		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBean", u);
		m.addAttribute("acadSessionStudent", u.getAcadSession());
		return "grade/gradeListForStudent";
	}

	@RequestMapping(value = "/gradeCenterForStudents", method = RequestMethod.GET)
	public String gradeCenterForStudents(@RequestParam Long courseId,
			@RequestParam String acadSessionStudent, Model m, Principal p) {
		logger.info("Inside Grade Center" + courseId);
		logger.info("Inside Grade Centeracad" + acadSessionStudent);
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		String uname = p.getName();
		Course courseDB = courseService.findByID(courseId);
		/*
		 * uname = StringUtils.substring(uname, 0,
		 * StringUtils.lastIndexOf(uname, "_P"));
		 */
		logger.info("parStud--->" + uname);
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(uname);
		logger.info("ACAD SESSION------------------------->"
				+ u.getAcadSession());

		String acadSession = u.getAcadSession();
		logger.info("Program NAme--------------------> " + ProgramName);
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		/* Course course = courseService.findByID(courseId); */
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		List<Course> courseList = courseService.findByUser(uname);
		HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();
		for (Course c : courseList) {
			if (!sessionWiseCourseList.containsKey(c.getAcadSession())) {
				List<Course> courseLst = new ArrayList();
				courseLst.add(c);
				sessionWiseCourseList.put(c.getAcadSession(), courseLst);
			} else {
				List<Course> courseLst = sessionWiseCourseList.get(c
						.getAcadSession());
				courseLst.add(c);
				sessionWiseCourseList.replace(c.getAcadSession(), courseLst);
			}
		}
		m.addAttribute("sessionWiseCourseList", sessionWiseCourseList);
		m.addAttribute("userBeanParent", u);
		List<StudentAssignment> assignments = studentAssigmentService
				.getAssignmentsForGradeCenter(uname, courseId);
		m.addAttribute("assignments", assignments);
		List<StudentTest> tests = studentTestService.getTestsForGradeCenter(
				uname, courseId);
		
		tests.addAll(studentTestService.getTestsForGradeCenterCreatedByAdminForTest(p.getName(),courseDB.getModuleId()));
		m.addAttribute("tests", tests);
		
		List<ClassParticipation> cpList = classParticipationService.getCPForGradeCenter(uname, courseId.toString());
		logger.info("cpList---->" + cpList);
		m.addAttribute("cpList", cpList);
		
		m.addAttribute("courseId", courseId);
		m.addAttribute("acadSessionStudent", acadSessionStudent);

		return "grade/gradeListForStudent";
	}
	
	
	
	@RequestMapping(value = "/gradeCenterFormForAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String gradeCenterFormForAdmin(Model m, Principal p) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		List<Course> listOfCoursesAssigned = getCourseBasedOnUser(userDetails,
				p);
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("listOfCoursesAssigned", listOfCoursesAssigned);
		m.addAttribute("showGradeCenterList", false);
		return "grade/gradeListAdmin";
	}

	
	
	@RequestMapping(value = "/gradeCenterAdmin", method = RequestMethod.GET)
	public String gradeCenterAdmin(@RequestParam Long courseId, Model m, Principal p) throws InterruptedException, ExecutionException {

		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		Course course = courseService.findByID(courseId);

		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		Future<List<Map<String, Object>>> result = userService
				.findAllTestScoreAndAssigmentByCourseListAdmin(String.valueOf(courseId));
		
		
		Map<String, Grade> gradeObj = new LinkedHashMap<String, Grade>();
		Set<String> assigmentList = new HashSet<String>();
		Set<String> testList = new HashSet<String>();
		Set<String> cpList = new HashSet<String>();
		Set<String> offlineList = new HashSet<>();
		Map<String, String> usernameList = new HashMap<>();
		Map<String, String> rollNoList = new HashMap<>();
		try {
			result.get().sort(
					Comparator.comparing(o -> String.valueOf(o.get("rollNo"))));
			for (Map<String, Object> temp : result.get()) {
				String name = (String) temp.get("name");
				String sapid = (String) temp.get("sapid");
				String rollNo = (String) temp.get("rollNo");
				Grade g = null;
				if (gradeObj.containsKey(sapid)) {
					g = gradeObj.get(sapid);

				} else {
					g = new Grade();
					g.setName(name);
				}

				usernameList.put(sapid, name);
				rollNoList.put(sapid, rollNo);
				String type = (String) temp.get("typ");
				switch (type) {
				case "ASSIGNEMENT":
					assigmentList.add(Utils.getBlankIfNull(temp
							.get("nameToShow")));
					g.getAssigmentToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				case "TEST":
					testList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getTestToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					g.getStatusMap().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("status")));

					break;
				case "CP":
					cpList.add(Utils.getBlankIfNull(temp.get("nameToShow")));
					g.getCpToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));
					break;

				case "Offline-Test":
					offlineList
							.add(Utils.getBlankIfNull(temp.get("nameToShow")));

					g.getOfflineToScore().put(
							Utils.getBlankIfNull(temp.get("nameToShow")),
							Utils.getBlankIfNull(temp.get("score")));

					break;

				}

				gradeObj.put(sapid, g);

			}

			/*
			 * logger.info("resultAssignments.get()" + resultAssignments.get());
			 * m.addAttribute("students", studentTestScore);
			 * m.addAttribute("assignments", resultAssignments.get());
			 * m.addAttribute("tests", resultTests.get());
			 */

			m.addAttribute("students", gradeObj);
			m.addAttribute("tests", testList);
			m.addAttribute("cpList", cpList);
			m.addAttribute("usernameList", usernameList);
			m.addAttribute("offlineList", offlineList);
			m.addAttribute("rollNoList", rollNoList);
			m.addAttribute("assignments", assigmentList);
			m.addAttribute("showGradeCenterList", true);
			m.addAttribute("listOfCoursesAssigned",
					getCourseBasedOnUser(userDetails, p));
			m.addAttribute("cId", courseId);
			m.addAttribute("course", course);

		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "grade/gradeListAdmin";
	}
}