package com.spts.lms.web.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.pending.PendingTask;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.PendingTest;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

@Secured("ROLE_USER")
@Controller
@SessionAttributes("userId")
public class PendingTaskController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(PendingTaskController.class);

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	StudentParentService studentParentService;
	
	@Autowired
	UserService userService;

	@RequestMapping("/pending")
	public @ResponseBody String getPendingList(Principal principal) {
		String json = "";
		String username = principal.getName();
		boolean isFaculty = false;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		Token userdetails1 = (Token) principal;
		isFaculty = (userDetails.getAuthorities().contains(Role.ROLE_FACULTY));
		List<Assignment> assignments = assignmentService.findByUserPending(
				username, isFaculty ,Long.parseLong(userdetails1.getProgramId()));

		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();

		for (Assignment ass : assignments) {
			PendingTask task = new PendingTask();
			task.setTaskName(ass.getAssignmentName());
			task.setType("Assigment");
			task.setDueDate(ass.getEndDate());
			task.setId("" + ass.getId());
			pendingTasks.add(task);
		}

		if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			List<PendingTest> pendingTest = studentTestService
					.getPendingTest(username);

			for (PendingTest pt : pendingTest) {
				PendingTask task = new PendingTask();
				task.setTaskName(pt.getTestName());
				task.setType("Test");
				task.setTestType(pt.getTestName());
				task.setEndDate(pt.getEndDate());
				task.setId("" + pt.getTestId());
				pendingTasks.add(task);
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(pendingTasks);
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		return json;

	}

	@RequestMapping("/pendingTask")
	public String getPendingTask(Principal principal, Model modal) {

		String username = principal.getName();
		
		Token userdetails1=(Token)principal;
		String ProgramName=userdetails1.getProgramName();
		if(userdetails1.getAuthorities().contains(Role.ROLE_PARENT)){
			username = StringUtils.substring(username, 0,
					StringUtils.lastIndexOf(username, "_P"));
			
		}
		User u=userService.findByUserName(username);
		
		
		String acadSession=u.getAcadSession();
		
		modal.addAttribute("Program_Name", ProgramName);
		modal.addAttribute("AcadSession", acadSession);

		
		try {
		boolean isFaculty = false;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		isFaculty = (userDetails.getAuthorities().contains(Role.ROLE_FACULTY));
		List<Assignment> assignments = assignmentService.findByUserPending(
				username, isFaculty,Long.parseLong(userdetails1.getProgramId()));

		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();
		List<String> ids = new ArrayList<String>();

		for (Assignment ass : assignments) {
			if (isFaculty && ids.contains("" + ass.getId()))
				continue;
			PendingTask task = new PendingTask();
			task.setTaskName(ass.getAssignmentName());
			task.setType("Assigment");
			task.setDueDate(ass.getEndDate());
			task.setCourseId(ass.getCourseId().toString());

			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			
			/*Instant instant = Instant.parse(ass.getStartDate() + "Z");
			Date startDate = java.util.Date.from(instant);

			instant = Instant.parse(ass.getEndDate() + "Z");
			Date endDate = java.util.Date.from(instant);*/
			Date startDate = format1.parse(ass.getStartDate());
			Date endDate = format1.parse(ass.getEndDate());
		
			
			task.setStartDate(startDate);
			task.setEndDate(endDate);
			task.setId("" + ass.getId());
			pendingTasks.add(task);
			ids.add("" + ass.getId());
		}
		ids.clear();

		if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			List<PendingTest> pendingTest = studentTestService
					.getPendingTest(username);

			for (PendingTest pt : pendingTest) {
				PendingTask task = new PendingTask();
				task.setTaskName(pt.getTestName());
				task.setType("Test");
				task.setTestType(pt.getTestType());
				task.setStartDate(pt.getStartDate());
				task.setEndDate(pt.getEndDate());
				task.setId("" + pt.getTestId());
				task.setIsPasswordForTest(pt.getIsPasswordForTest());
				task.setCourseId(String.valueOf(pt.getCourseId()));
				pendingTasks.add(task);
			}
		}
		
		modal.addAttribute("task", pendingTasks);
		modal.addAttribute("webPage", new WebPage("pendingTask",
				"Pending task", true, false));
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		
		return "course/newPending";

	}

	@RequestMapping(value = "/pendingTaskFormForParents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String gradeCenterForm(Model m, Principal p) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) p;
		m.addAttribute("webPage", new WebPage("pendingTaskForParents",
				"Pending Task", false, false));
		String username = p.getName();
		
		Token userdetails1=(Token)p;
		String ProgramName=userdetails1.getProgramName();
		User u=userService.findByUserName(username);
		
		
		String acadSession=u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(username);

		m.addAttribute("listOfStudents", listOfStudents);
		m.addAttribute("showPendingTaskList", false);
		return "course/pendingTaskForParents";
	}

	@RequestMapping("/pendingTaskForParents")
	public String pendingTaskForParents(Principal principal,
			@RequestParam String uname, Model modal) {
		try{
		String username = principal.getName();

		boolean isFaculty = false;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(username);
		List<Assignment> assignments = assignmentService
				.findByUserPendingForParents(uname);

		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();

		for (Assignment ass : assignments) {
			PendingTask task = new PendingTask();
			task.setTaskName(ass.getAssignmentName());
			task.setType("Assigment");
			task.setDueDate(ass.getEndDate());
			task.setId("" + ass.getId());
			/*Instant instant = Instant.parse(ass.getStartDate() + "Z");
			Date startDate = java.util.Date.from(instant);

			instant = Instant.parse(ass.getEndDate() + "Z");
			Date endDate = java.util.Date.from(instant);*/
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			
			/*Instant instant = Instant.parse(ass.getStartDate() + "Z");
			Date startDate = java.util.Date.from(instant);

			instant = Instant.parse(ass.getEndDate() + "Z");
			Date endDate = java.util.Date.from(instant);*/
			Date startDate = format1.parse(ass.getStartDate());
			Date endDate = format1.parse(ass.getEndDate());
			
			task.setStartDate(startDate);
			task.setEndDate(endDate);
			pendingTasks.add(task);

		}

		List<PendingTest> pendingTest = studentTestService
				.getPendingTest(uname);

		for (PendingTest pt : pendingTest) {
			PendingTask task = new PendingTask();
			task.setTaskName(pt.getTestName());
			task.setType("Test");
			task.setTestType(pt.getTestName());
			task.setId("" + pt.getTestId());
			task.setStartDate(pt.getStartDate());
			task.setEndDate(pt.getEndDate());
			pendingTasks.add(task);
		}

		
		modal.addAttribute("uname", uname);
		modal.addAttribute("listOfStudents", listOfStudents);
		modal.addAttribute("showPendingTaskList", true);
		modal.addAttribute("task", pendingTasks);
		modal.addAttribute("webPage", new WebPage("pendingTask",
				"Pending task", true, false));
		}
		catch(Exception e){
			logger.error("Exception", e);
		}
		
		return "course/pendingTaskForParents";

	}

	@RequestMapping("/pendingTaskByCourse")
	public String getPendingTask(Principal principal, Model modal,
			String courseId) {

		String username = principal.getName();
		
		Token userdetails1=(Token)principal;
		String ProgramName=userdetails1.getProgramName();
		User u=userService.findByUserName(username);
		
		
		String acadSession=u.getAcadSession();
		
		modal.addAttribute("Program_Name", ProgramName);
		modal.addAttribute("AcadSession", acadSession);

		boolean isFaculty = false;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		isFaculty = (userDetails.getAuthorities().contains(Role.ROLE_FACULTY));
		List<Assignment> assignments = assignmentService
				.findByUserPendingByCourse(username, isFaculty, courseId);

		List<PendingTask> pendingTasks = new ArrayList<PendingTask>();

		for (Assignment ass : assignments) {
			PendingTask task = new PendingTask();
			task.setTaskName(ass.getAssignmentName());
			task.setType("Assigment");
			task.setDueDate(ass.getEndDate());
			task.setId("" + ass.getId());
			pendingTasks.add(task);
		}

		if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			List<PendingTest> pendingTest = studentTestService
					.getPendingTestByCourse(username, courseId);

			for (PendingTest pt : pendingTest) {
				PendingTask task = new PendingTask();
				task.setTaskName(pt.getTestName());
				task.setType("Test");
				task.setTestType(pt.getTestName());
				task.setEndDate(pt.getEndDate());
				task.setId("" + pt.getTestId());
				pendingTasks.add(task);
			}
		}
		
		modal.addAttribute("task", pendingTasks);
		modal.addAttribute("webPage", new WebPage("pendingTask",
				"Pending task", true, false));
		return "course/pending";

	}
}
