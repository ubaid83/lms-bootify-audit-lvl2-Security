package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.group.Groups;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.group.GroupService;
import com.spts.lms.services.group.StudentGroupService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

//@Secured("ROLE_USER")
@Controller
@SessionAttributes("userId")
public class StudentGroupController extends BaseController {
	@Autowired
	ApplicationContext act;

	@Autowired
	StudentGroupService studentGroupService;

	@Autowired
	GroupService groupService;

	@Autowired
	ProgramService programService;

	@Autowired
	CourseService courseService;
	
	@Autowired
	UserService userService;

	private static final Logger logger = Logger
			.getLogger(StudentGroupController.class);

	@ModelAttribute("programs")
	public List<Program> getPrograms() {
		return programService.findAllActive();
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT" })
	@RequestMapping(value = "/groupList", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String groupList(@RequestParam(required = false) Long courseId,
			Model m, Principal principal) {
		String username = principal.getName();
		
		Token userdetails1=(Token)principal;
		String ProgramName=userdetails1.getProgramName();
		User u=userService.findByUserName(username);
		
		
		String acadSession=u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("groupList", "Groups", true,
				false));
		List<Groups> groups = Collections.emptyList();
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			if (courseId != null) {
				groups = groupService
						.findByFacultyAndCourse(username, courseId);
			} else {
				groups = groupService.findAllGroupsByFaculty(username,Long.parseLong(userdetails1.getProgramId())	);
			}
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			if (courseId != null) {
				groups = groupService.findByUserAndCourse(username, courseId);
				for (Groups g : groups) {
					Long cId = g.getCourseId();

					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();
					g.setCourseName(courseName);
					groups.set(groups.indexOf(g), g);
				}
			} else {

				groups = groupService.findByUser(username);
			}
		}

		m.addAttribute("groups", groups);
		m.addAttribute("rowCount", groups.size());
		m.addAttribute("courseId", courseId);
		return "group/groupList";
	}

}
