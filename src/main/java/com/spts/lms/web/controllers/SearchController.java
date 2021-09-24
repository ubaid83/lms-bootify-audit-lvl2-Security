package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.search.Search;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;

@Controller
public class SearchController extends BaseController {

	private static final Logger logger = Logger
			.getLogger(SearchController.class);

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	TestService testService;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Autowired
	ContentService contentService;
	@Autowired
	UserService userService;

	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT"})
	@RequestMapping(value = "/addSearchForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addSearchForm(@ModelAttribute Search search, Model m,
			HttpServletRequest request, Principal principal) {

		List<String> typeList = new ArrayList();
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		// String types[] = { "Assignment", "Test", "Feedback", "Resource" };
		String types[] = { "Assignment", "Test", "Resource" };
		typeList = Arrays.asList(types);
		m.addAttribute("typeList", typeList);
		m.addAttribute("statusList",
				Arrays.asList(new String[] { "Completed", "Not Completed" }));
		Token userDetails = (Token) principal;

		String progName = userDetails.getProgramName();

		// m.addAttribute("courses",courseService.findByCoursesBasedOnProgramName(progName));
		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			m.addAttribute("allCourses",
					courseService.findByCoursesBasedOnProgramName(progName));
		} else {
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				m.addAttribute("allCourses", courseService.findByUserActive(
						principal.getName(), userDetails.getProgramName()));
			}

		}

		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "search/genericSearchAdmin";
		} else {

		return "search/genericSearch";
		}
	}
	@Secured({ "ROLE_ADMIN" ,"ROLE_FACULTY","ROLE_STUDENT"})
	@RequestMapping(value = "/searchItem", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String searchItem(
			@ModelAttribute Search search,
			Model m,
			Principal principal,
			@RequestParam(name = "status", required = false, defaultValue = " ") String status,
			@RequestParam(name = "searchType", required = false, defaultValue = " ") String searchType,
			@RequestParam("courseId") Long courseId) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Search Assignment Test", true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		List<String> typeList = new ArrayList();

		String types[] = { "Assignment", "Test", "Resource" };
		typeList = Arrays.asList(types);
		m.addAttribute("typeList", typeList);
		m.addAttribute("statusList",
				Arrays.asList(new String[] { "Completed", "Not Completed" }));
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			m.addAttribute("allCourses",
					courseService.findByCoursesBasedOnProgramName(ProgramName));
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userdetails1.getAuthorities()
							.contains(Role.ROLE_STUDENT)) {
				m.addAttribute("allCourses", courseService.findByUserActive(
						principal.getName(), userdetails1.getProgramName()));
			}

		}

		if (status.equalsIgnoreCase("Completed")) {
			status = "Y";
		}
		if (status.equalsIgnoreCase("Not Completed")) {
			status = "N";
		}

		if (!(searchType.equals(" ")) && !(status.equals(" "))
				&& (courseId != null)) {
			

			switch (searchType) {
			case "Assignment":
				List<StudentAssignment> allAssignments = studentAssignmentService
						.serachAllAssignments(courseId, status);

				for (StudentAssignment sa : allAssignments) {

					Assignment a = assignmentService.findByID(sa
							.getAssignmentId());
					sa.setAssignmentName(a.getAssignmentName());
					sa.setEndDate(a.getEndDate());
					String filePath = StringUtils.trimToNull(a.getFilePath());
					if (filePath != null) {
						sa.setShowFileDownload("true");
					} else {
						sa.setShowFileDownload("false");
					}
					String studentFilePath = StringUtils.trimToNull(sa
							.getStudentFilePath());
					if (studentFilePath != null) {
						sa.setShowStudentFileDownload("true");
					} else {
						sa.setShowStudentFileDownload("false");
					}
					allAssignments.set(allAssignments.indexOf(sa), sa);
				}
				if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
					setNote(m, "No Assignments Found!");
				}
				m.addAttribute("allAssignments", allAssignments);
				break;

			case "Test":
				List<StudentTest> allTests = studentTestService.searchAllTest(
						courseId, status);
				for (StudentTest st : allTests) {
					Test t = testService.findByID(st.getTestId());
					st.setTestName(t.getTestName());
					allTests.set(allTests.indexOf(st), st);
				}
				if (allTests.size() == 0 || allTests.isEmpty()) {
					setNote(m, "No Tests Found!");
				}
				m.addAttribute("allTests", allTests);
				break;

			case "Resource":
				List<Content> allContent = contentService
						.findByCourse(courseId);
				if (allContent.size() == 0 || allContent.isEmpty()) {
					setNote(m, "No Resources Found!");
				}
				m.addAttribute("allContent", allContent);

			default:
				break;
			}

		} else {

		}
		if ((searchType.equals(" ")) && (status.equals(" "))
				&& (courseId == null)) {
			
			List<StudentAssignment> allAssignments = studentAssignmentService
					.findAllActive();
			for (StudentAssignment sa : allAssignments) {
				Assignment a = assignmentService.findByID(sa.getAssignmentId());
				sa.setAssignmentName(a.getAssignmentName());
				sa.setEndDate(a.getEndDate());
				String filePath = StringUtils.trimToNull(a.getFilePath());
				if (filePath != null) {
					sa.setShowFileDownload("true");
				} else {
					sa.setShowFileDownload("false");
				}
				String studentFilePath = StringUtils.trimToNull(sa
						.getStudentFilePath());
				if (studentFilePath != null) {
					sa.setShowStudentFileDownload("true");
				} else {
					sa.setShowStudentFileDownload("false");
				}

				allAssignments.set(allAssignments.indexOf(sa), sa);
			}
			List<StudentTest> allTests = studentTestService.findAllActive();
			for (StudentTest st : allTests) {
				Test t = testService.findByID(st.getTestId());
				st.setTestName(t.getTestName());
				allTests.set(allTests.indexOf(st), st);
			}
			List<Content> allContent = contentService.findAll();

			m.addAttribute("allAssignments", allAssignments);
			m.addAttribute("allTests", allTests);

			m.addAttribute("allContent", allContent);

		} else {

		}

		if (!(searchType.equals(" "))
				&& (status.equals(" ") && (courseId == null))) {
			

			switch (searchType) {
			case "Assignment":
				List<StudentAssignment> allAssignments = studentAssignmentService
						.findAllActive();
				for (StudentAssignment sa : allAssignments) {
					Assignment a = assignmentService.findByID(sa
							.getAssignmentId());
					sa.setAssignmentName(a.getAssignmentName());
					sa.setEndDate(a.getEndDate());
					String filePath = StringUtils.trimToNull(a.getFilePath());
					if (filePath != null) {
						sa.setShowFileDownload("true");
					} else {
						sa.setShowFileDownload("false");
					}
					String studentFilePath = StringUtils.trimToNull(sa
							.getStudentFilePath());
					if (studentFilePath != null) {
						sa.setShowStudentFileDownload("true");
					} else {
						sa.setShowStudentFileDownload("false");
					}
					allAssignments.set(allAssignments.indexOf(sa), sa);
				}
				if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
					setNote(m, "No Assignments Found!");
				}
				m.addAttribute("allAssignments", allAssignments);
				break;

			case "Test":
				List<StudentTest> allTests = studentTestService.findAllActive();
				for (StudentTest st : allTests) {
					Test t = testService.findByID(st.getTestId());
					st.setTestName(t.getTestName());
					allTests.set(allTests.indexOf(st), st);
				}
				if (allTests.size() == 0 || allTests.isEmpty()) {
					setNote(m, "No Tests Found!");
				}
				m.addAttribute("allTests", allTests);
				break;

			case "Resource":
				List<Content> allContent = contentService.findAllActive();
				if (allContent.size() == 0 || allContent.isEmpty()) {
					setNote(m, "No Resources Found!");
				}
				m.addAttribute("allContent", allContent);

			default:
				break;
			}
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "search/genericSearchAdmin";
			} else {

			return "search/genericSearch";
			}

		}

		if ((status.contains("Y") || status.contains("N"))
				&& (searchType.equals(" ")) && (courseId == null)) {
			
			List<StudentAssignment> allAssignments = studentAssignmentService
					.findAssignmentsByStatus(status);
			for (StudentAssignment sa : allAssignments) {
				Assignment a = assignmentService.findByID(sa.getAssignmentId());
				sa.setAssignmentName(a.getAssignmentName());
				sa.setEndDate(a.getEndDate());
				String filePath = StringUtils.trimToNull(a.getFilePath());
				if (filePath != null) {
					sa.setShowFileDownload("true");
				} else {
					sa.setShowFileDownload("false");
				}
				String studentFilePath = StringUtils.trimToNull(sa
						.getStudentFilePath());
				if (studentFilePath != null) {
					sa.setShowStudentFileDownload("true");
				} else {
					sa.setShowStudentFileDownload("false");
				}

				allAssignments.set(allAssignments.indexOf(sa), sa);

			}
			if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
				setNote(m, "No Assignments Found!");
			}
			m.addAttribute("allAssignments", allAssignments);

			List<StudentTest> allTests = studentTestService
					.searchAllTestByStatus(status);
			for (StudentTest st : allTests) {
				Test t = testService.findByID(st.getTestId());
				st.setTestName(t.getTestName());
				allTests.set(allTests.indexOf(st), st);
			}
			if (allTests.size() == 0 || allTests.isEmpty()) {
				setNote(m, "No Tests Found!");
			}
			m.addAttribute("allTests", allTests);
			List<Content> allContent = contentService.findAllActive();
			if (allContent.size() == 0 || allContent.isEmpty()) {
				setNote(m, "No Resources Found!");
			}
			m.addAttribute("allContent", allContent);
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "search/genericSearchAdmin";
			} else {

			return "search/genericSearch";
			}

		}
		if (courseId != null && searchType.equals(" ") && status.equals(" ")) {
			

			List<StudentAssignment> allAssignments = studentAssignmentService
					.findAssignmentsByCourseId(courseId);
			for (StudentAssignment sa : allAssignments) {
				Assignment a = assignmentService.findByID(sa.getAssignmentId());
				sa.setAssignmentName(a.getAssignmentName());
				sa.setEndDate(a.getEndDate());
				String filePath = StringUtils.trimToNull(a.getFilePath());
				if (filePath != null) {
					sa.setShowFileDownload("true");
				} else {
					sa.setShowFileDownload("false");
				}
				String studentFilePath = StringUtils.trimToNull(sa
						.getStudentFilePath());
				if (studentFilePath != null) {
					sa.setShowStudentFileDownload("true");
				} else {
					sa.setShowStudentFileDownload("false");
				}

				allAssignments.set(allAssignments.indexOf(sa), sa);

			}
			if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
				setNote(m, "No Assignments Found!");
			}
			m.addAttribute("allAssignments", allAssignments);

			List<StudentTest> allTests = studentTestService
					.searchAllTestByCourseId(courseId);
			for (StudentTest st : allTests) {
				Test t = testService.findByID(st.getTestId());
				st.setTestName(t.getTestName());
				allTests.set(allTests.indexOf(st), st);
			}
			if (allTests.size() == 0 || allTests.isEmpty()) {
				setNote(m, "No Tests Found!");
			}
			m.addAttribute("allTests", allTests);

			List<Content> allContent = contentService.findByCourse(courseId);
			if (allContent.size() == 0 || allContent.isEmpty()) {
				setNote(m, "No Resources Found!");
			}
			m.addAttribute("allContent", allContent);
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "search/genericSearchAdmin";
			} else {

			return "search/genericSearch";
			}

		}

		if (!searchType.equals(" ") && !status.equals(" ") && courseId == null) {
			
			switch (searchType) {
			case "Assignment":
				List<StudentAssignment> allAssignments = studentAssignmentService
						.findAssignmentsByStatus(status);
				for (StudentAssignment sa : allAssignments) {
					Assignment a = assignmentService.findByID(sa
							.getAssignmentId());
					sa.setAssignmentName(a.getAssignmentName());
					sa.setEndDate(a.getEndDate());
					String filePath = StringUtils.trimToNull(a.getFilePath());
					if (filePath != null) {
						sa.setShowFileDownload("true");
					} else {
						sa.setShowFileDownload("false");
					}
					String studentFilePath = StringUtils.trimToNull(sa
							.getStudentFilePath());
					if (studentFilePath != null) {
						sa.setShowStudentFileDownload("true");
					} else {
						sa.setShowStudentFileDownload("false");
					}

					allAssignments.set(allAssignments.indexOf(sa), sa);

				}
				if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
					setNote(m, "No Assignments Found!");
				}
				m.addAttribute("allAssignments", allAssignments);
				break;

			case "Test":
				List<StudentTest> allTests = studentTestService
						.searchAllTestByStatus(status);
				for (StudentTest st : allTests) {
					Test t = testService.findByID(st.getTestId());
					st.setTestName(t.getTestName());
					allTests.set(allTests.indexOf(st), st);
				}
				if (allTests.size() == 0 || allTests.isEmpty()) {
					setNote(m, "No Tests Found!");
				}
				m.addAttribute("allTests", allTests);
				break;

			case "Resource":
				List<Content> allContent = contentService.findAllActive();
				if (allContent.size() == 0 || allContent.isEmpty()) {
					setNote(m, "No Resources Found!");
				}
				m.addAttribute("allContent", allContent);

			default:
				break;
			}
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "search/genericSearchAdmin";
			} else {

			return "search/genericSearch";
			}

		}

		if (courseId != null && !searchType.equals(" ") && (status.equals(" "))) {
			
			switch (searchType) {

			case "Assignment":
				List<StudentAssignment> allAssignments = studentAssignmentService
						.findAssignmentsByCourseId(courseId);
				for (StudentAssignment sa : allAssignments) {
					Assignment a = assignmentService.findByID(sa
							.getAssignmentId());
					sa.setAssignmentName(a.getAssignmentName());
					sa.setEndDate(a.getEndDate());
					String filePath = StringUtils.trimToNull(a.getFilePath());
					if (filePath != null) {
						sa.setShowFileDownload("true");
					} else {
						sa.setShowFileDownload("false");
					}
					String studentFilePath = StringUtils.trimToNull(sa
							.getStudentFilePath());
					if (studentFilePath != null) {
						sa.setShowStudentFileDownload("true");
					} else {
						sa.setShowStudentFileDownload("false");
					}
					allAssignments.set(allAssignments.indexOf(sa), sa);

				}
				if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
					setNote(m, "No Assignments Found!");
				}
				m.addAttribute("allAssignments", allAssignments);
				break;

			case "Test":
				List<StudentTest> allTests = studentTestService
						.searchAllTestByCourseId(courseId);
				for (StudentTest st : allTests) {
					Test t = testService.findByID(st.getTestId());
					st.setTestName(t.getTestName());
					allTests.set(allTests.indexOf(st), st);
				}
				if (allTests.size() == 0 || allTests.isEmpty()) {
					setNote(m, "No Tests Found!");
				}
				m.addAttribute("allTests", allTests);
				break;

			case "Resource":
				List<Content> allContent = contentService
						.findByCourse(courseId);
				if (allContent.size() == 0 || allContent.isEmpty()) {
					setNote(m, "No Resources Found!");
				}
				m.addAttribute("allContent", allContent);

			default:
				break;
			}
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "search/genericSearchAdmin";
			} else {

			return "search/genericSearch";
			}

		}

		if (courseId != null && searchType.equals(" ") && !status.equals(" ")) {
			

			List<StudentAssignment> allAssignments = studentAssignmentService
					.serachAllAssignments(courseId, status);
			for (StudentAssignment sa : allAssignments) {
				Assignment a = assignmentService.findByID(sa.getAssignmentId());
				sa.setAssignmentName(a.getAssignmentName());
				sa.setEndDate(a.getEndDate());
				String filePath = StringUtils.trimToNull(a.getFilePath());
				if (filePath != null) {
					sa.setShowFileDownload("true");
				} else {
					sa.setShowFileDownload("false");
				}
				String studentFilePath = StringUtils.trimToNull(sa
						.getStudentFilePath());
				if (studentFilePath != null) {
					sa.setShowStudentFileDownload("true");
				} else {
					sa.setShowStudentFileDownload("false");
				}

				allAssignments.set(allAssignments.indexOf(sa), sa);

			}
			if (allAssignments.size() == 0 || allAssignments.isEmpty()) {
				setNote(m, "No Assignments Found!");
			}
			m.addAttribute("allAssignments", allAssignments);

			List<StudentTest> allTests = studentTestService.searchAllTest(
					courseId, status);
			for (StudentTest st : allTests) {
				Test t = testService.findByID(st.getTestId());
				st.setTestName(t.getTestName());
				allTests.set(allTests.indexOf(st), st);
			}
			if (allTests.size() == 0 || allTests.isEmpty()) {
				setNote(m, "No Tests Found!");
			}
			m.addAttribute("allTests", allTests);

			List<Content> allContent = contentService.findByCourse(courseId);
			if (allContent.size() == 0 || allContent.isEmpty()) {
				setNote(m, "No Resources Found!");
			}
			m.addAttribute("allContent", allContent);
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "search/genericSearchAdmin";
			} else {

			return "search/genericSearch";
			}

		}

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "search/genericSearchAdmin";
		} else {

		return "search/genericSearch";
		}
	}
}
