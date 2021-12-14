package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.AssignmentConfiguration;
import com.spts.lms.beans.assignment.AssignmentQuestion;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.assignment.StudentAssignmentQuestion;
import com.spts.lms.beans.assignment.StudentAssignmentQuestionwiseMarks;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.group.Groups;
import com.spts.lms.beans.group.StudentGroup;
import com.spts.lms.beans.library.Library;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.test.TestPool;
import com.spts.lms.beans.test.TestQuestionPools;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.variables.LmsVariables;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.services.assignment.AssignmentConfigurationService;
import com.spts.lms.services.assignment.AssignmentQuestionService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentQuestionService;
import com.spts.lms.services.assignment.StudentAssignmentQuestionwiseMarksService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.group.GroupService;
import com.spts.lms.services.group.StudentGroupService;
import com.spts.lms.services.library.LibraryService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestPoolService;
import com.spts.lms.services.test.TestQuestionPoolsService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.services.variables.LmsVariablesService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;


@Controller
@SessionAttributes("userId")
public class AssignmentController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	ContentService contentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	StudentGroupService studentGroupService;

	@Autowired
	TestService testService;

	@Autowired
	GroupService groupService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	CourseService courseService;

	@Autowired
	AssignmentDAO assignmentDao;

	@Autowired
	LibraryService libraryService;

	@Autowired
	StudentParentService studentParentService;

	@Value("${lms.assignment.questionFolderS3}")
	private String assignmentFolder;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;
	
	@Value("${lms.assignment.submissionFolderS3}")
	private String submissionFolder;

	@Value("${sendAlertsToParents}")
	private String sendAlertsToParents;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Autowired
	Notifier notifier;
	
	@Autowired
	ProgramService programService;
	
	@Autowired
	AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	LmsVariablesService lmsVariablesService;
	
	@Autowired
	AssignmentConfigurationService assignmentConfigurationService;
	
	@Autowired
	StudentAssignmentQuestionwiseMarksService studentAssignmentQuestionwiseMarksService;
	
	@Autowired
	TestPoolService testPoolService;

	@Autowired
	AssignmentQuestionService assignmentQuestionService;

	@Autowired
	StudentAssignmentQuestionService studentAssignmentQuestionService;
	
	@Autowired
	TestQuestionPoolsService testQuestionPoolsService;
	

	protected static final int BUFFER_SIZE = 4096;

	private static final Logger logger = Logger
			.getLogger(AssignmentController.class);

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createAssignmentForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createAssignmentForm(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long id, Model m,
			Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		Assignment assignment = new Assignment();

		if (id != null) {
			assignment = assignmentService.findByID(id);
			m.addAttribute("edit", "true");

		}
		if (courseId != null) {
			assignment.setCourseId(courseId);
			Course c = courseService.findByID(assignment.getCourseId());
			assignment.setCourse(c);
			assignment.setAcadMonth(c.getAcadMonth());
			assignment.setAcadYear(Integer.valueOf(c.getAcadYear()));
		}
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		m.addAttribute("assignment", assignment);
		return "assignment/createAssignment";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createAssignmentFromMenu", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createAssignmentFromMenu(
			@RequestParam(required = false) Long id, Principal principal,
			@RequestParam(required = false) Long courseId, Model m,
			HttpServletRequest request) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();
		try {
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Assignment assignment = new Assignment();
			if (courseId != null) {
				assignment.setCourse(courseService.findByID(courseId));
				m.addAttribute("courseId", courseId);
				m.addAttribute("showCourseHeader", true);
			} else {
				m.addAttribute("showCourseHeader", false);
			}

			if (request.getSession().getAttribute("courseRecord") == null
					|| "".equals(request.getSession().getAttribute(
							"courseRecord"))) {

			} else {
				assignment.setCourse(courseService.findByID(courseId));
				assignment.setIdForCourse(String.valueOf(courseId));
				m.addAttribute("idForCourse", assignment.getIdForCourse());
				request.getSession().removeAttribute("courseRecord");
			}

			// String username = principal.getName();
			if (id != null) {

				assignment = assignmentService.findByID(id);
				assignment.setCourse(courseService.findByID(assignment
						.getCourseId()));
				m.addAttribute("edit", "true");
			}
			if (!"INTDR".equals(userdetails1.getProgramName())) {
				m.addAttribute(
						"allCourses",
						courseService.findByUserActive(username,
								userdetails1.getProgramName()));
			} else {
				m.addAttribute("allCourses",
						courseService.findByUserActive(username));
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				m.addAttribute("sendAlertsToParents", true);
			} else {
				m.addAttribute("sendAlertsToParents", false);
			}

			m.addAttribute("assignment", assignment);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			return "assignment/createAssignment";
		}

		return "assignment/createAssignment";
	}

	public void validateAssignmentType(String s) throws ValidationException{
		if (s == null || s.trim().isEmpty()) {
			 throw new ValidationException("Assignment Type field cannot be empty");
		 }
		if(!s.equals("Presentation") && !s.equals("WrittenAssignment") && !s.equals("Viva") && !s.equals("ReportWriting")) {
			throw new ValidationException("Invalid Assignment Type.");
		}
	}
	
	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createAssignment", method = { RequestMethod.POST })
	public String createAssignment(@ModelAttribute Assignment assignment,
			@RequestParam(required = false) Long myId,
			@RequestParam("file") List<MultipartFile> files, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate())) {
//				setError(redirectAttributes, "Invalid Start date and End date");
//				return "redirect:/createAssignmentFromMenu";
//			}
			HtmlValidation.validateHtml(assignment, Arrays.asList("assignmentText"));
			Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
			BusinessBypassRule.validateNumeric(assignment.getMaxScore());
			BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
			validateAssignmentType(assignment.getAssignmentType());
			Course course = courseService.findByID(assignment.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			if(!assignment.getPlagscanRequired().equals("Yes") && !assignment.getPlagscanRequired().equals("No")) {
				throw new ValidationException("Invalid Input.");
			}
//			BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
			BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
			BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
			BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
			BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
			BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());
//			if(Double.valueOf(assignment.getMaxScore()) < 0.0) {
//				setError(redirectAttributes, "Invalid total score");
//				return "redirect:/createAssignmentFromMenu";
//			}
			/* New Audit changes end */
			if (assignment.getId() != null) {
				assignmentService.update(assignment);
			} else {
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						//Audit change start
						Tika tika = new Tika();
						  String detectedType = tika.detect(file.getBytes());
						if (file.getOriginalFilename().contains(".")) {
							Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
							logger.info("length--->"+count);
							if (count > 1 || count == 0) {
								setError(redirectAttributes, "File uploaded is invalid!");
								redirectAttributes.addAttribute("courseId", assignment.getCourseId());
								return "redirect:/createAssignmentFromMenu";
							}else {
								String extension = FilenameUtils.getExtension(file.getOriginalFilename());
								logger.info("extension--->"+extension);
								if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
										|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
									setError(redirectAttributes, "File uploaded is invalid!");
									redirectAttributes.addAttribute("courseId", assignment.getCourseId());
									return "redirect:/createAssignmentFromMenu";
								}else {
									byte [] byteArr=file.getBytes();
									if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																		("text/plain").equals(detectedType)) {
									String errorMessage = uploadAssignmentFileForS3(assignment, file);
									} else {
										setError(redirectAttributes, "File uploaded is invalid!");
										redirectAttributes.addAttribute("courseId", assignment.getCourseId());
										return "redirect:/createAssignmentFromMenu";
									}
								}
							}
						}else {
							setError(redirectAttributes, "File uploaded is invalid!");
							redirectAttributes.addAttribute("courseId", assignment.getCourseId());
							return "redirect:/createAssignmentFromMenu";
						}
						//Audit change end
					}
				}
				// if (errorMessage == null) {
				assignment.setCreatedBy(username);
				assignment.setLastModifiedBy(username);
				assignment.setFacultyId(username);// Assignment can be
													// created by
													// Faculty
													// only.
				Long courseId = null;
				if (assignment.getIdForCourse() == null) {
					courseId = assignment.getCourseId();
				} else {
					courseId = Long.valueOf(assignment.getIdForCourse());
					assignment.setCourseId(courseId);
				}
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					assignment.setSendEmailAlertToParents("Y");
					assignment.setSendSmsAlertToParents("Y");
				}
				
			
				assignment.setAcadMonth(course.getAcadMonth());
				assignment.setAcadYear(Integer.valueOf(course.getAcadYear()));

				assignment.setFacultyId(username);
				assignmentService.insertWithIdReturn(assignment);
				assignment.setCourse(courseService.findByID(courseId));

				setSuccess(redirectAttributes,
						"Assignment uploaded successfully");

				List<StudentAssignment> students = studentAssignmentService
						.getStudentsForAssignment(assignment.getId(),
								assignment.getCourseId());
				m.addAttribute("students", students);
				m.addAttribute("assignment", assignment);
				myId = assignment.getAssignedId();
				assignment.setAssignedId(assignment.getId());

				redirectAttributes.addAttribute("id",
						assignment.getAssignedId());
				m.addAttribute("id", assignment.getAssignedId());
				/*
				 * } else { setError(m, errorMessage); } }
				 */
			}
		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttributes, ve.getMessage());
			redirectAttributes.addAttribute("courseId", assignment.getCourseId());
			return "redirect:/createAssignmentFromMenu";
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "assignment/createAssignment";
		}
		return "redirect:/viewAssignment";
	}

	
	// added with notification service
	//updated by Amey on 10 Apr 2020
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/updateAssignment", method = { RequestMethod.GET,

			RequestMethod.POST })
	public String updateAssignment(@ModelAttribute Assignment assignment,

			@RequestParam("file") List<MultipartFile> files, Model m,

			Principal principal) {

		m.addAttribute("webPage", new WebPage("assignment",

				"Create Assignment", true, false));

		String errorMessage = null;
		
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		m.addAttribute("assignment", assignment);

		m.addAttribute("edit", "true");
		try {

			Assignment assignDB = assignmentService.findByID(assignment.getId());
			

			String ProgramName = userdetails1.getProgramName();

			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);

			m.addAttribute("AcadSession", acadSession);
			
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				m.addAttribute("allCourses",courseService.findByAdminActive(userdetails1.getProgramName()));
				assignment.setCreatedByAdmin("Y");
			}else {
				m.addAttribute("allCourses",courseService.findByUserActive(username,userdetails1.getProgramName()));
			}
			/* New Audit changes start */
			HtmlValidation.validateHtml(assignment, Arrays.asList("assignmentText"));
			Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
			BusinessBypassRule.validateNumeric(assignment.getMaxScore());
			BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
			validateAssignmentType(assignment.getAssignmentType());
			Course course = courseService.findByID(assignment.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			if(!assignment.getPlagscanRequired().equals("Yes") && !assignment.getPlagscanRequired().equals("No")) {
				throw new ValidationException("Invalid Input.");
			}
//			BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
			BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
			BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
			BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
			BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
			BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());
			/* New Audit changes end */
			// Upload new assignment file, if user selected one.
			Assignment retrived = assignmentService

					.findByID(assignment.getId());
			for (MultipartFile file : files) {
				if (file != null && !file.isEmpty()) {
					//Audit change start
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(m, "File uploaded is invalid!");
							if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
								return "assignment/createAssignmentForAdmin";
							}
							return "assignment/createAssignment";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
									|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(m, "File uploaded is invalid!");
								if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
									return "assignment/createAssignmentForAdmin";
								}
								return "assignment/createAssignment";
							}else {
								byte [] byteArr=file.getBytes();
								if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																	("text/plain").equals(detectedType)) {
								errorMessage = uploadAssignmentFileForS3(assignment, file);
								} else {
									setError(m, "File uploaded is invalid!");
									if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
										return "assignment/createAssignmentForAdmin";
									}
									return "assignment/createAssignment";
								}
							}
						}
					}else {
						setError(m, "File uploaded is invalid!");
						if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
							return "assignment/createAssignmentForAdmin";
						}
						return "assignment/createAssignment";
					}
					//Audit change end
				} else {
					assignment.setFilePath(retrived.getFilePath());
					assignment.setFilePreviewPath(retrived

							.getFilePreviewPath());
				}
			} // If User has not added a new file then take the path from

			// existing assignment to use generic update query

//			Course course = retrived.getCourseId() != null ? courseService.findByID(Long.valueOf(retrived.getCourseId())) : null;
			String subject = " Assigment with name  ";

			StringBuffer buff = new StringBuffer(subject);
			buff.append(retrived.getAssignmentName());
			if (course != null) {
				buff.append(" for Course ");
				buff.append(course.getCourseName());
			}
			buff.append(" is updated on" + retrived.getLastModifiedDate());
			subject = buff.toString();

			assignment.setLastModifiedBy(username);
			
			/* For Assignment Pool Start */
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				if (!assignDB.getIsQuesConfigFromPool().equals(assignment.getIsQuesConfigFromPool())
						|| !assignDB.getMaxQuestnToShow().equals(assignment.getMaxQuestnToShow())
						|| !assignDB.getRandomQuestion().equals(assignment.getRandomQuestion())) {
					logger.info("assignDB.getRandomQuestion---->"+assignDB.getRandomQuestion());
					logger.info("assignment.getRandomQuestion---->"+assignment.getRandomQuestion());
					List<AssignmentConfiguration> apcList = assignmentConfigurationService
							.getAllPoolConfigByAssignmentId(assignment.getId());
					if (null != apcList && apcList.size() > 0) {
						setNote(m, "You need to configure question pool configuration.");
						assignmentConfigurationService.deleteByAssignmentPoolConfigurationAssignmentId(String.valueOf(assignment.getId()));
						List<StudentAssignment> allocatedStudentList = studentAssignmentService.getStudentsForAssignentById(assignment.getId());
						for(StudentAssignment sa:allocatedStudentList) {
							studentAssignmentQuestionService.deleteAssignQuesByAssignment(sa.getId());
						}
						studentAssignmentService.deleteStudentByAssignment(assignment.getId());	
					}
					if(!assignDB.getRandomQuestion().equals(assignment.getRandomQuestion())) {
						assignmentQuestionService.deleteAlocatedQuestion(assignment.getId());
					}
				}
			}
			/* For Assignment Pool End */

			assignmentService.update(assignment);

			retrived = assignmentService.findByID(assignment.getId());

			assignment.setCourse(courseService.findByID(assignment.getCourseId()));

			setSuccess(m, "Assigment updated successfully");

			m.addAttribute("noOfStudentAllocated",

					studentAssignmentService.getNoOfStudentsAllocated(assignment.getId()));

			List<StudentAssignment> students = studentAssignmentService

					.getStudentsForAssignment(assignment.getId(),

							assignment.getCourseId());

			List<String> studentList = new ArrayList<>();
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				studentList.add(retrived.getFacultyId());
			}
			studentList.add(username);

			for (StudentAssignment sa : students) {

				studentList.add(sa.getUsername());

			}

			m.addAttribute("students", students);

			try {

				if (students != null

						&& students.size() > 0) {

					try {

						Map<String, Map<String, String>> result = null;

						if (!students.isEmpty()) {

							if ("Y".equals(retrived.getSendEmailAlert())) {

								for (String s : studentList) {

									List<String> singleStudList = new ArrayList<>();
									singleStudList.add(s);
									result = userService.findEmailByUserName(singleStudList);
									Map<String, String> email = result.get("emails");
									Map<String, String> mobiles = new HashMap();
									notifier.sendEmail(email, mobiles, subject, subject);
								}

							}

							if ("Y".equals(retrived.getSendSmsAlert())) {

								result = userService

										.findEmailByUserName(studentList);

								Map<String, String> email = new HashMap();

								Map<String, String> mobiles = result.get("mobiles");

								notifier.sendEmail(email, mobiles, subject, subject);

							}

						}

					} catch (Exception e) {

						logger.error("Exception", e);

					}


					return viewAssignment(assignment.getId(), m, null, principal);

				}

			} catch (Exception e) {

				logger.error(e);

			}

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(m, ve.getMessage());
			m.addAttribute("courseId", assignment.getCourseId());
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "assignment/createAssignmentForAdmin";
			}
			return "assignment/createAssignment";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			setError(m, "Error in updating assignment");

			m.addAttribute("webPage", new WebPage("assignment",

					"Create Assignment", false, false));
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "assignment/createAssignmentForAdmin";
			}
			return "assignment/createAssignment";
			

		}
		
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "assignment/assignmentByAdmin";
		}
		return "assignment/assignment";

	}

	
	
	//updated by Amey on 10 Apr 2020
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/saveStudentAssignmentAllocationForAllStudents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentAssignmentAllocationForAllStudents(
			@ModelAttribute Assignment assignment, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		String subject = " Assigment with name  ";
		Assignment retrived = assignmentService.findByID(assignment.getId());
		Course course = retrived.getCourseId() != null ? courseService
				.findByID(Long.valueOf(retrived.getCourseId())) : null;
		StringBuffer buff = new StringBuffer(subject);
		buff.append(retrived.getAssignmentName());
		if (course != null) {
			buff.append("for Course ");
			buff.append(course.getCourseName());
		}
		buff.append(" allocated to you");
		subject = buff.toString();
		List<UserCourse> totalStudentsList = userCourseService
				.findStudentsForFaculty(assignment.getCourseId());

		List<String> studentUsernames = new ArrayList<String>();
		for (UserCourse uc : totalStudentsList) {
			studentUsernames.add(uc.getUsername());

		}
		assignment.setStudents(studentUsernames);
		ArrayList<StudentAssignment> studentAssignmentMappingList = new ArrayList<StudentAssignment>();
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		try {
			if (assignment.getStudents() != null
					&& assignment.getStudents().size() > 0) {
				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					studentList.add(retrived.getFacultyId());
				}
				studentList.add(username);
				for (String studentUsername : assignment.getStudents()) {
					StudentAssignment bean = new StudentAssignment();
					bean.setAcadMonth(retrived.getAcadMonth());
					bean.setAcadYear(retrived.getAcadYear());
					bean.setAssignmentId(assignment.getId());
					bean.setCourseId(assignment.getCourseId());
					bean.setUsername(studentUsername);
					bean.setEvaluatedBy(assignment.getFacultyId());
					if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
						bean.setCreatedBy(retrived.getFacultyId());
					}else {
						bean.setCreatedBy(username);
					}
					bean.setLastModifiedBy(username);
					bean.setStartDate(retrived.getStartDate());
					/* bean.setDueDate(retrived.getDueDate()); */
					bean.setEndDate(retrived.getEndDate());
					bean.setActive("Y");

					studentList.add(studentUsername);
					List<StudentAssignment> studentAssList = studentAssignmentService
							.getStudentUsername(bean.getAssignmentId(),
									bean.getCourseId());

					if (studentAssList.isEmpty()) {
						studentAssignmentMappingList.add(bean);
					} else {
						List<String> names = new ArrayList<String>();

						for (StudentAssignment ass : studentAssList) {
							names.add(ass.getUsername());
							ass.setActive("Y");
						}

						if (!names.contains(bean.getUsername())) {
							studentAssignmentMappingList.add(bean);
						}
					}

				}

				studentAssignmentService
						.insertBatch(studentAssignmentMappingList);
				try {
					Map<String, Map<String, String>> result = null;
					if (!studentList.isEmpty()) {
						if ("Y".equals(retrived.getSendEmailAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}

						if ("Y".equals(retrived.getSendSmsAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}
						if ("Y".equals(retrived.getSendEmailAlert())) {

							for(String s: studentList) {
								
								List<String> singleStudList = new ArrayList<>();
								singleStudList.add(s);
								result = userService
										.findEmailByUserName(singleStudList);
								Map<String, String> email = result.get("emails");
								Map<String, String> mobiles = new HashMap();
								notifier.sendEmail(email, mobiles, subject, subject);
							}
						}

						if ("Y".equals(retrived.getSendSmsAlert())) {

							if (result != null)
								result = userService
										.findEmailByUserName(studentList);
							Map<String, String> email = new HashMap();
							Map<String, String> mobiles = result.get("mobiles");
							notifier.sendEmail(email, mobiles, subject, subject);
						}
					}
				} catch (Exception e) {
					logger.error("Exception", e);
				}

				/*
				 * setSuccess(m, "Assignment allocated to All students successfully");
				 */
				if ("Y".equals(retrived.getIsQuesConfigFromPool())) {
					String message = assignQuestionsToStudents(retrived, username);
					if ("Error".equals(message)) {
						setError(m, "Error While Assigning Quesions To Students");
					} else if ("Empty".equals(message)) {
						setError(m,
								"No Students Found To Assign Question Or Students Have already been assign the questions.");
					} else {
						setSuccess(m, "Assignment allocated to " + assignment.getStudents().size()
								+ " students successfully");
					}

				} else {
					setSuccess(m,
							"Assignment allocated to " + assignment.getStudents().size() + " students successfully");
				}

				return viewAssignment(assignment.getId(), m, null, principal);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "assignment/createAssignmentForAdmin";
			}
			return "assignment/createAssignment";
		}
		m.addAttribute("assignment", assignment);
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "assignment/createAssignmentForAdmin";
		}
		return "assignment/createAssignment";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/saveStudentAssignmentAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentAssignmentAllocation(
			@ModelAttribute Assignment assignment, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		String subject = " Assigment with name  ";
		Assignment retrived = assignmentService.findByID(assignment.getId());
		Course course = retrived.getCourseId() != null ? courseService
				.findByID(Long.valueOf(retrived.getCourseId())) : null;
		StringBuffer buff = new StringBuffer(subject);
		buff.append(retrived.getAssignmentName());
		if (course != null) {
			buff.append("for Course ");
			buff.append(course.getCourseName());
		}
		buff.append(" allocated to you");
		subject = buff.toString();
		ArrayList<StudentAssignment> studentAssignmentMappingList = new ArrayList<StudentAssignment>();
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		try {
			if (assignment.getStudents() != null
					&& assignment.getStudents().size() > 0) {
				
				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					studentList.add(retrived.getFacultyId());
				}
				studentList.add(username);
				for (String studentUsername : assignment.getStudents()) {
					StudentAssignment bean = new StudentAssignment();
					bean.setAcadMonth(retrived.getAcadMonth());
					bean.setAcadYear(retrived.getAcadYear());
					bean.setAssignmentId(assignment.getId());
					bean.setCourseId(assignment.getCourseId());
					bean.setUsername(studentUsername);
					bean.setEvaluatedBy(assignment.getFacultyId());
					if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
						bean.setCreatedBy(retrived.getFacultyId());
					}else {
						bean.setCreatedBy(username);
					}
					bean.setLastModifiedBy(username);
					bean.setStartDate(retrived.getStartDate());
					/* bean.setDueDate(retrived.getDueDate()); */
					bean.setEndDate(retrived.getEndDate());
					bean.setActive("Y");

					studentList.add(studentUsername);

					if (bean.getUsername().equals(studentUsername)) {

					} else {

					}
					studentAssignmentMappingList.add(bean);

				}

				studentAssignmentService
						.insertBatch(studentAssignmentMappingList);
				try {
					Map<String, Map<String, String>> result = null;
					if (!studentList.isEmpty()) {
						if ("Y".equals(retrived.getSendEmailAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}

						if ("Y".equals(retrived.getSendSmsAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}
						if ("Y".equals(retrived.getSendEmailAlert())) {
							for(String s: studentList) {
								
								List<String> singleStudList = new ArrayList<>();
								singleStudList.add(s);
								result = userService
										.findEmailByUserName(singleStudList);
								Map<String, String> email = result.get("emails");
								Map<String, String> mobiles = new HashMap();
								notifier.sendEmail(email, mobiles, subject, subject);
							}
						}

						if ("Y".equals(retrived.getSendSmsAlert())) {

							if (result != null)
								result = userService
										.findEmailByUserName(studentList);
							Map<String, String> email = new HashMap();
							Map<String, String> mobiles = result.get("mobiles");
							notifier.sendEmail(email, mobiles, subject, subject);
						}
					}
				} catch (Exception e) {
					logger.error("Exception", e);
				}

//				setSuccess(m, "Assignment allocated to " + assignment.getStudents().size()	+ " students successfully");
				if ("Y".equals(retrived.getIsQuesConfigFromPool())) {
					String message = assignQuestionsToStudents(retrived, username);
					if ("Error".equals(message)) {
						setError(m, "Error While Assigning Quesions To Students");
					} else if ("Empty".equals(message)) {
						setError(m,
								"No Students Found To Assign Question Or Students Have already been assign the questions.");
					} else {
						setSuccess(m, "Assignment allocated to " + assignment.getStudents().size()
								+ " students successfully");
					}

				} else {
					setSuccess(m,
							"Assignment allocated to " + assignment.getStudents().size() + " students successfully");
				}	

				return viewAssignment(assignment.getId(), m, null, principal);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "redirect:/viewAssignment?id=" + assignment.getId();
		}
		m.addAttribute("assignment", assignment);
		return "redirect:/viewAssignment?id=" + assignment.getId();
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/saveGroupAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveGroupAssignment(@ModelAttribute Assignment assignment,
			@RequestParam("file") List<MultipartFile> files, Model m,
			Principal principal,RedirectAttributes redirectAttributes, String multipleAssignmentErrorMsg) throws ValidationException{
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));

		String username = principal.getName();
		if(null == multipleAssignmentErrorMsg) {
			multipleAssignmentErrorMsg = "";
		}
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		ArrayList<StudentAssignment> groupAssignmentMappingList = new ArrayList<StudentAssignment>();
		try {
		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		String subject = " Assigment with name  ";
		List<String> studentList = new ArrayList<String>();
		studentList.add(username);
		Long courseId = null;
		if (assignment.getIdForCourse() == null) {
			courseId = assignment.getCourseId();
		} else {
			courseId = Long.valueOf(assignment.getIdForCourse());
			assignment.setCourseId(courseId);
		}
		/* New Audit changes start */
		HtmlValidation.validateHtml(assignment, Arrays.asList("assignmentText"));
		Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
		BusinessBypassRule.validateNumeric(assignment.getMaxScore());
		BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
		validateAssignmentType(assignment.getAssignmentType());
		Course course = courseService.findByID(assignment.getCourseId());
		if(null == course) {
			throw new ValidationException("Invalid Course selected.");
		}
		if(!assignment.getPlagscanRequired().equals("Yes") && !assignment.getPlagscanRequired().equals("No")) {
			throw new ValidationException("Invalid Input.");
		}
//		BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
		BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
		BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
		if(!multipleAssignmentErrorMsg.equals("Success")) {
			BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
		}
		BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
		BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());

//		if(!Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate())) {
//			setError(redirectAttributes, "Invalid Start date and End date");
//			return "redirect:/createAssignmentFromGroup";
//		}
//		if(Double.valueOf(assignment.getMaxScore()) < 0.0) {
//			setError(redirectAttributes, "Invalid total score");
//			return "redirect:/createAssignmentFromGroup";
//		}
		/* New Audit changes start */
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				//Audit change start
				try {
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttributes, "File uploaded is invalid!");
						//redirectAttrs.addAttribute("courseId", assignment.getCourseId());
						return "redirect:/createAssignmentFromGroup";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
								|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttributes, "File uploaded is invalid!");
							//redirectAttrs.addAttribute("courseId", assignment.getCourseId());
							return "redirect:/createAssignmentFromGroup";
						}else {
							byte [] byteArr=file.getBytes();
							if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																("text/plain").equals(detectedType)) {
							String errorMessage = uploadAssignmentFileForS3(assignment, file);
							} else {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/createAssignmentFromGroup";
							}
						}
					}
				}else {
					setError(redirectAttributes, "File uploaded is invalid!");
					//redirectAttrs.addAttribute("courseId", assignment.getCourseId());
					return "redirect:/createAssignmentFromGroup";
				}
				//Audit change end
				} catch (Exception e) {
					logger.error("Exception while uploading file assign",e);
					setError(redirectAttributes, "Error occurred  while uploading file!");
					return "redirect:/createAssignmentFromGroup";
				}
			}
		}
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			assignment.setSendEmailAlertToParents("Y");
			assignment.setSendSmsAlertToParents("Y");
		}
		assignment.setFacultyId(username);
		assignment.setCreatedBy(username);
		assignment.setLastModifiedBy(username);
//		Course c = courseService.findByID(courseId);

		assignment.setAcadMonth(course.getAcadMonth());
		assignment.setAcadYear(Integer.valueOf(course.getAcadYear()));

		assignment.setCourse(course);
		assignmentService.insertWithIdReturn(assignment);

		long assignmentId = assignment.getId();

		List<String> parentList = new ArrayList<String>();

		

		

//			Course course = courseService.findByID(courseId);

			StringBuffer buff = new StringBuffer(subject);
			buff.append(assignment.getAssignmentName());
			if (course != null) {
				buff.append("for Course ");
				buff.append(course.getCourseName());
			}
			buff.append(" allocated to you");
			subject = buff.toString();
			if (assignment.getGrps() != null && assignment.getGrps().size() > 0) {

				for (String g : assignment.getGrps()) {

					Groups group = groupService.findByID(Long.valueOf(g));

					List<StudentGroup> studentGroup = studentGroupService
							.getStudentsBasedOnGroups(group.getId());
					for (StudentGroup sg : studentGroup) {
						StudentAssignment studentAssignment = new StudentAssignment();
						studentAssignment.setAcadMonth(assignment
								.getAcadMonth());
						studentAssignment.setAcadYear(assignment.getAcadYear());
						studentAssignment
								.setAssignmentId(new Long(assignmentId));
						studentAssignment.setCourseId(assignment.getCourseId());
						studentAssignment.setUsername(sg.getUsername());
						studentAssignment.setCourseId(assignment.getCourseId());
						studentAssignment.setGroupId(group.getId());
						studentAssignment.setEvaluatedBy(assignment
								.getFacultyId());
						studentAssignment.setCreatedBy(username);
						studentAssignment.setLastModifiedBy(username);
						studentAssignment.setStartDate(assignment
								.getStartDate());
						
						studentAssignment.setEndDate(assignment.getEndDate());
						groupAssignmentMappingList.add(studentAssignment);
						studentList.add(sg.getUsername());

					}

				}

				studentAssignmentService
						.insertBatch(groupAssignmentMappingList);

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(assignment.getSendEmailAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}

					if ("Y".equals(assignment.getSendSmsAlertToParents())) {
						for (String s : studentList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							studentList.addAll(parentList);
						}

					}
					if ("Y".equals(assignment.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(assignment.getSendSmsAlert())) {

						if (result != null)
							result = userService
									.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}
				setSuccess(m, "Assignment allocated to "
						+ assignment.getGrps().size() + " groups successfully");

				if(multipleAssignmentErrorMsg.equals("Success")) {
					viewByGroupAssignment(assignment.getId(), m, principal);
					return "Success";
				}else {
					return viewByGroupAssignment(assignment.getId(), m, principal);
				}
			}
		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			logger.info("Error----->"+ve.getMessage());
			setError(redirectAttributes, ve.getMessage());
			if(multipleAssignmentErrorMsg.equals("Success")) {
				return ve.getMessage();
			}else {
				return "redirect:/createAssignmentFromGroup";
			}
			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "assignment/createAssignmentFromGroupFinal";
		}
		m.addAttribute("assignment", assignment);
		m.addAttribute("groupAssignmentMappingList", groupAssignmentMappingList);
		return "assignment/viewByGroup";

	}

	//Updated by Amey on 10 Apr 2020
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/viewAssignment", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewAssignment(@RequestParam Long id, Model m,
			@RequestParam(required = false) Long campusId, Principal principal) {
		m.addAttribute("webPage", new WebPage("viewAssignment", "Assignment",
				true, false));
		Assignment assignment = new Assignment();
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("id", id);
		List<StudentAssignment> students = new ArrayList<StudentAssignment>();

		if (id != null) {
			assignment = assignmentService.findByID(id);
			assignment.setCourse(courseService.findByID(assignment
					.getCourseId()));
			String filePath = StringUtils.trimToNull(assignment.getFilePath());
			if (filePath != null) {
				assignment.setShowFileDownload("true");
			} else {
				assignment.setShowFileDownload("false");
			}
			
			

			if (campusId != null) {
				assignment.setCampusId(campusId);
				students = studentAssignmentService
						.getStudentsForAssignmentOnCampusId(assignment.getId(),
								assignment.getCourseId(), campusId);

			} else {
				students = studentAssignmentService.getStudentsForAssignment(
						assignment.getId(), assignment.getCourseId());
			}

			m.addAttribute("allCampuses", userService.findCampus());
			for (StudentAssignment uc : students) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				students.set(students.indexOf(uc), uc);
			}

			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated",
					studentAssignmentService.getNoOfStudentsAllocated(id));

		}
		
		
		//
		if ("Y".equals(assignment.getIsQuesConfigFromPool())) {
			List<AssignmentQuestion> assgnQuestionList = assignmentQuestionService
					.getAssgnQuestionToValidate(assignment.getId(), "Descriptive");
			logger.info("assgnQuestionList---->"+assgnQuestionList);
			List<AssignmentQuestion> testPoolConfig = assignmentQuestionService
					.getTestPoolsByAssignmentId(assignment.getId());
			String showStudentsToAlloc = "true";
			if (testPoolConfig.size() == 0 && "Y".equals(assignment.getRandomQuestion())) {

				showStudentsToAlloc = "false";

			} else if (assgnQuestionList.size() == 0) {
				showStudentsToAlloc = "false";

			} else {

				Map<String, Integer> mapAssgnQuestn = new HashMap<>();
				Map<String, Double> mapAssgnQuestnAndMarks = new HashMap<>();
				Map<String, Integer> mapTestPoolConfig = new HashMap<>();
				
				assgnQuestionList
				.forEach(i -> mapAssgnQuestnAndMarks.put(i.getTestPoolId(), Double.parseDouble(i.getMarks())));
	
				assgnQuestionList
						.forEach(i -> mapAssgnQuestn.put(i.getTestPoolId(), Integer.parseInt(i.getNoOfQuestion())));
				testPoolConfig
						.forEach(i -> mapTestPoolConfig.put(i.getTestPoolId(), Integer.parseInt(i.getNoOfQuestion())));

				if ("Y".equals(assignment.getRandomQuestion())) {
					for (String s : mapTestPoolConfig.keySet()) {
						logger.info("s--->"+s);
						int noOfTestPoolQuestn = mapTestPoolConfig.get(s);
						if (mapAssgnQuestn.containsKey(s)) {
							int noOfAssgnQuestn = mapAssgnQuestn.get(s);
							if (noOfAssgnQuestn < noOfTestPoolQuestn) {
								showStudentsToAlloc = "false";
								logger.info("noOfAssgnQuestn < noOfTestPoolQuestn");
							}
						} else {
							showStudentsToAlloc = "false";
							logger.info("noOfTestPoolQuestn--->"+noOfTestPoolQuestn);
						}
						if (showStudentsToAlloc.equals("false")) {
							break;
						}
					}
				} else {

					double maxScore = Integer.parseInt(assignment.getMaxScore());
					double totalScoreConfig = 0;
					
					for (int i=0;i<assgnQuestionList.size();i++) {
						double mulVal=  Double.parseDouble(assgnQuestionList.get(i).getMarks()) * Double.parseDouble(assgnQuestionList.get(i).getNoOfQuestion());
						totalScoreConfig = totalScoreConfig + mulVal;
					}
					if (maxScore != totalScoreConfig) {
						showStudentsToAlloc = "false";
					}
				}

			}
			if ("false".equals(showStudentsToAlloc)) {
				setNote(m, "Student will not appear to allocate till Assignment Question is Configured properly or total marks of questions equal to total score of assignment.");
			}
			m.addAttribute("showStudentsToAlloc", showStudentsToAlloc);
			logger.info("show student alloca is" + showStudentsToAlloc);
		} else {
			m.addAttribute("showStudentsToAlloc", "true");
			logger.info("showStudentsToAllo is set to true");

		}
		m.addAttribute("assignment", assignment);
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "assignment/assignmentByAdmin";
		}
		return "assignment/assignment";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STUDENT" })
	@RequestMapping(value = "/viewAssignmentFinal", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewAssignmentFinal(
			@RequestParam(required = false) Long courseId, Model m,
			Principal principal) {

		logger.info("courseId------>" + courseId);
		Course cr = courseService.findByID(courseId);

		String username = principal.getName();

		User u = userService.findByUserName(username);
		Token userDetails = (Token) principal;

		logger.info("courseList---->" + userDetails.getCourseList());
		
		HashMap<String, List<Course>> sessionWiseCourseList = new HashMap<String, List<Course>>();

		for (Course c : userDetails.getCourseList()) {

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
		m.addAttribute("userBean", u);
		m.addAttribute("course", cr);

		List<Assignment> assignments = Collections.emptyList();

		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			if (courseId != null) {
				assignments = assignmentService.findByFacultyAndCourseActive(
						username, courseId);
			} else {
				assignments = assignmentService
						.findAllAssignmentsByFaculty(username);
			}
		} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			if (courseId != null) {
				assignments = assignmentService.findByUserAndCourse(username,
						courseId);
			} else {
			}
		}
		try {
		Date currDate = Utils.getInIST();
		for (Assignment a : assignments) {
			Course c = courseService.findByID(a.getCourseId());
			a.setCourseName(c.getCourseName());
			
			a.setCourseName(c.getCourseName());
			Date endDate = Utils.converFormatsDateAlt(a.getEndDate());
			
			if(currDate.after(endDate)) {
				logger.info("N Shown");
				a.setShowGenHashKey("N");	
			}else {
				logger.info("Y Shown");
				a.setShowGenHashKey("Y");
			}
			StudentAssignment sa = studentAssignmentService.getStudentAssignmentHashKey(username, a.getId());
			if(sa!=null) {
				a.setStudentHashKey(sa.getHashKey());
			}else {
				a.setStudentHashKey("Hash Key Not Generated");
			}
			
				boolean remarkFileExist = false;
				String remarkFilePath = amazonS3ClientService.existsFile(submissionFolder+"/"+a.getAssignmentName()+"-"+a.getId()+"/Remarks", username);
				//logger.info("check for file--->"+remarkFileExist);
				if(remarkFilePath.equals("")) {
					if(null != a.getParentModuleId()) {
						Assignment parentAssignment = assignmentService.findByID(Long.valueOf(a.getParentModuleId()));
						remarkFilePath = amazonS3ClientService.existsFile(submissionFolder+"/"+parentAssignment.getAssignmentName()+"-"+parentAssignment.getId()+"/Remarks", username);
						if(remarkFilePath.equals("")) {
							remarkFileExist = false;
						}else {
							remarkFileExist = true;
						}
					}else {
						remarkFileExist = false;
					}
				}else {
					remarkFileExist = true;
				}
				m.addAttribute("remarkFileExist",remarkFileExist);
				logger.info("check for filePath--->"+remarkFilePath);
				logger.info("remarkFileExist--->"+remarkFileExist);
				a.setAssignmentRemarkFile(remarkFileExist);
				assignments.set(assignments.indexOf(a), a);
			
		}
		
		LmsVariables showDisclaimerInAssignment = lmsVariablesService.getLmsVariableBykeyword("showDisclaimerInAssignment");
		m.addAttribute("showDisclaimerInAssignment", showDisclaimerInAssignment.getValue());
		
		if ((assignments.size() == 0 || assignments.isEmpty()) && courseId != null) {
			setNote(m, "No Assignments Allocated to you!");
		}
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting assignment.");
		}
		m.addAttribute("courseId", courseId);
		m.addAttribute("assignments", assignments);
		m.addAttribute("assignment", new Assignment());
		m.addAttribute("rowCount", assignments.size());
		logger.info("assignments---->"+assignments.size());

		return "assignment/viewAssignmentFinal";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_STUDENT" })
	@RequestMapping(value = "/viewAssignmentFinalAjax", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String viewAssignmentFinalAjax(
			@RequestParam(name = "courseId") Long courseId, Model m,
			Principal principal) {

		logger.info("courseId Ajax------>" + courseId);

		try {

			String username = principal.getName();
			Token userDetails = (Token) principal;

			logger.info("courseList Ajax---->" + userDetails.getCourseList());

			List<Assignment> assignments = Collections.emptyList();

			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				if (courseId != null) {
					assignments = assignmentService
							.findByFacultyAndCourseActive(username, courseId);
				} else {
					assignments = assignmentService
							.findAllAssignmentsByFaculty(username);
				}
			} else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				if (courseId != null) {
					assignments = assignmentService.findByUserAndCourse(
							username, courseId);
				} else {
					// assignments =
					// assignmentService.findAllAssignments(username);
					assignments = assignmentService.findByUser(username);
				}
			}
			for (Assignment a : assignments) {
				Course c = courseService.findByID(a.getCourseId());
				a.setCourseName(c.getCourseName());
				assignments.set(assignments.indexOf(a), a);
			}

			if (assignments.size() == 0 || assignments.isEmpty()) {
				logger.info("-----empty-----");
				return "[]";
			} else {
				String json = new Gson().toJson(assignments);
				logger.info("json----->" + json);
				return json;
			}
		} catch (Exception e) {
			logger.error("Error" + e);
			return "[]";
		}
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/viewByGroupAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewByGroupAssignment(@RequestParam Long id, Model m,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("viewAssignment", "Assignment",
				true, false));
		Assignment assignment = new Assignment();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		String username = principal.getName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (id != null) {
			assignment = assignmentService.findByID(id);
			assignment.setCourse(courseService.findByID(assignment
					.getCourseId()));
			if (assignment.getFilePath() != null) {
				assignment.setShowFileDownload("true");
			} else {
				assignment.setShowFileDownload("false");
			}
			List<StudentAssignment> groups = studentAssignmentService
					.getGroupsForAssignment(id, assignment.getCourseId());

			Set<Long> groupIdList = new HashSet<>();

			List<StudentAssignment> getStudentsByGroup = studentAssignmentService
					.getStudentsByGroup(assignment.getId());
			Map<Long, String> mapper = new HashMap<>();
			Map<Long, List<StudentAssignment>> mapperGroup = new HashMap<>();

			for (StudentAssignment sa : groups) {

				groupIdList.add(sa.getGroupId());
			}
			for (Long g : groupIdList) {
				List<StudentAssignment> saForGroup = new ArrayList<>();
				for (StudentAssignment sa : getStudentsByGroup) {
					if (g.equals(sa.getGroupId())) {

						saForGroup.add(sa);

					}

				}
				mapperGroup.put(g, saForGroup);
			}
			Map<Long, String> mapGroupToSubmitter = new HashMap<>();

			for (Long map : mapperGroup.keySet()) {
				String editableParam = "[";
				String otherParam = "";
				int count = 0;
				for (StudentAssignment sa : mapperGroup.get(map)) {
					count++;
					if (mapperGroup.get(map).size() == count) {
						otherParam = otherParam + "{value:'" + sa.getId()
								+ "',text:'" + sa.getStudentName() + "'}";
					} else {
						otherParam = otherParam + "{value:'" + sa.getId()
								+ "',text:'" + sa.getStudentName() + "'},";
					}
					if ("Y".equals(sa.getIsSubmitterInGroup())) {

						mapGroupToSubmitter.put(map, sa.getStudentName());
					}
				}
				editableParam = editableParam + otherParam + "]";
				mapper.put(map, editableParam);
			}

			m.addAttribute("noOfStudentAllocated",
					studentAssignmentService.getNoOfGroupsAllocated(id));
			m.addAttribute("mapper", mapper);
			m.addAttribute("mapperGroup", mapperGroup);
			m.addAttribute("mapGroupToSubmitter", mapGroupToSubmitter);

			assignment.setGroupAssigment(groups);

			m.addAttribute("groups", groups);
		}
		m.addAttribute("assignment", assignment);
		m.addAttribute("id", id);
		return "assignment/viewByGroup";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/updateSubmitter", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateSubmitter(@RequestParam String value,
			@RequestParam Long pk, Principal principal) {

		try {

			StudentAssignment submission = studentAssignmentService
					.findByID(Long.valueOf(value));

			studentAssignmentService.makeAllInactiveFirst(
					submission.getAssignmentId(), submission.getGroupId());
			studentAssignmentService.updateSubmitter(value);
			return "{\"status\": \"success\", \"msg\": \"Submitter is set!\"}";

		} catch (NumberFormatException e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Please enter valid entry!\"}";
		} catch (Exception e) {
			logger.error("Exception", e);
			return "{\"status\": \"error\", \"msg\": \"Error in setting submitter!\"}";
		}

	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/saveGroupAssignmentAllocationSelectAll", method = { RequestMethod.POST })
	public String saveGroupAssignmentAllocationSelectAll(
			@ModelAttribute Assignment assignment, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));

		List<String> grps = new ArrayList<String>();
		String username = principal.getName();
		List<StudentAssignment> allocatedStudents = studentAssignmentService
				.getStudentsForAssignment(assignment.getId(),
						assignment.getCourseId());
		List<UserCourse> studentCourse = userCourseService
				.findStudentsForFaculty(assignment.getCourseId());

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Assignment retrived = assignmentService.findByID(assignment.getId());
		List<Groups> groups = groupService.findByFacultyAndCourse(username,
				assignment.getCourseId());
		for (Groups g : groups) {
			grps.add(String.valueOf(g.getId()));
			assignment.setGrps(grps);
		}

		try {

			if (grps != null && !grps.isEmpty()) {
				if (allocatedStudents.size() == studentCourse.size()) {
					setNote(m,
							"Assignment is already allocated to all students!");
				} else {

					for (String g : grps) {

						ArrayList<StudentAssignment> groupAssignmentMappingList = new ArrayList<StudentAssignment>();
						Groups group = groupService.findByID(Long.valueOf(g));
						
						List<StudentGroup> studentGroup = studentGroupService
								.getStudentsBasedOnGroups(group.getId());

						for (StudentGroup sg : studentGroup) {

							StudentAssignment bean = new StudentAssignment();

							bean.setAcadMonth(assignment.getAcadMonth());
							bean.setAcadYear(assignment.getAcadYear());
							bean.setAssignmentId(assignment.getId());
							bean.setCourseId(retrived.getCourseId());
							bean.setUsername(sg.getUsername());
							bean.setActive("Y");
							bean.setGroupId(group.getId());
							bean.setEvaluatedBy(assignment.getFacultyId());
							bean.setCreatedBy(username);
							bean.setLastModifiedBy(username);
							bean.setStartDate(retrived.getStartDate());
							/* bean.setDueDate(retrived.getDueDate()); */
							bean.setEndDate(retrived.getEndDate());

							List<StudentAssignment> studentAssList = studentAssignmentService
									.getStudentUsername(bean.getAssignmentId(),
											bean.getCourseId());

							if (studentAssList.isEmpty()) {
								groupAssignmentMappingList.add(bean);
							} else {
								List<String> names = new ArrayList<String>();

								for (StudentAssignment ass : studentAssList) {
									names.add(ass.getUsername());
									ass.setActive("Y");
								}

								if (!names.contains(bean.getUsername())) {
									groupAssignmentMappingList.add(bean);
								}
							}

						}

						studentAssignmentService
								.insertBatch(groupAssignmentMappingList);
						setSuccess(m, "Assignment allocated to "
								+ assignment.getGrps().size()
								+ " groups successfully");

					}
				}
				m.addAttribute("showNote", true);
				return viewByGroupAssignment(assignment.getId(), m, principal);
			}
		}

		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "assignment/createAssignment";
		}
		m.addAttribute("assignment", assignment);
		return "assignment/viewByGroup";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/getAssignmentDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssignmentDetails(@RequestParam Long id,
			Model m) {
		Assignment assignment = new Assignment();

		if (id != null) {
			assignment = assignmentService.findByID(id);
			return assignment.getAssignmentText();

		} else {
			return "Error in retrieving assignment: No Id in request";
		}
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/getLateAssignmentDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getLateAssignmentDetails(@RequestParam Long id,
			Model m) {
		Assignment assignment = new Assignment();
		StudentAssignment stud_assignment = studentAssignmentService
				.findByID(id);
		Long assgn_id = stud_assignment.getAssignmentId();
		if (assgn_id != null) {
			assignment = assignmentService.findByID(assgn_id);

			return assignment.getAssignmentText();

		} else {
			return "Error in retrieving assignment: No Id in request";
		}
	}

	private String uploadAssignmentFile(Assignment bean, MultipartFile file) {

		String errorMessage = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		String fileName = file.getOriginalFilename();

		// Replace special characters in file
		fileName = fileName.replaceAll("'", "_");
		fileName = fileName.replaceAll(",", "_");
		fileName = fileName.replaceAll("&", "and");
		fileName = fileName.replaceAll(" ", "_");

		fileName = fileName.substring(0, fileName.lastIndexOf("."))
				+ "_"
				+ RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());

		try {
			inputStream = file.getInputStream();
			String filePath = assignmentFolder + File.separator + fileName;

			if (bean.getFilePath() != null) {

				bean.setFilePath(bean.getFilePath() + "," + filePath);
				bean.setFilePreviewPath(bean.getFilePreviewPath() + ","
						+ filePath);
			} else {

				bean.setFilePath(filePath);
				bean.setFilePreviewPath(filePath);
			}

			File folderPath = new File(assignmentFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			File newFile = new File(filePath);
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);

		} catch (IOException e) {
			errorMessage = "Error in uploading Assignment file : "
					+ e.getMessage();
			logger.error("Exception" + errorMessage, e);
		} finally {

			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);

			if (outputStream != null)
				IOUtils.closeQuietly(outputStream);

		}

		return errorMessage;
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchAssignmentForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAssignmentForm(Model m, Principal principal,
			@ModelAttribute Assignment assignment) {
		m.addAttribute("webPage", new WebPage("assignmentList",
				"Search Assignments", false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		courseService.findByUserActive(principal.getName(),
				userdetails1.getProgramName());
		m.addAttribute("allCourses", courseService.findByUserActive(
				principal.getName(), userdetails1.getProgramName()));
		return "assignment/searchAssignment";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchAssignment", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String searchAssignment(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, @ModelAttribute Assignment assignment, Principal principal) {

		m.addAttribute("webPage", new WebPage("assignmentList",
				"Search Assignments", true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {
			Page<Assignment> page = assignmentService
					.searchActiveByExactMatchReplacement(
							Long.parseLong(userdetails1.getProgramId()),
							pageNo, pageSize);

			List<Assignment> assignmentList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(assignment));

			if (assignmentList == null || assignmentList.size() == 0) {
				setNote(m, "No Assignments found");
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(m, "Error in getting Assignments List");
		}
		return "assignment/searchAssignment";
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchFacultyAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchFacultyAssignment(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m,
			RedirectAttributes redirectAttributes,
			@ModelAttribute Assignment assignment,
			Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("searchCourse",
				"Search Assignments", false, false));
		Page<Assignment> page = new Page<Assignment>(pageNo);
		List<Assignment> assignmentList = new ArrayList<Assignment>();

		try {

			if (courseId == null || courseId.isEmpty()) {
				if (request.getSession().getAttribute("courseRecord") == null
						|| request.getSession().getAttribute("courseRecord")
								.equals("")) {

				} else {
					request.getSession().removeAttribute("courseRecord");
				}

			}

			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				assignment.setFacultyId(principal.getName());
			}
			if (courseId == null || courseId.isEmpty()) {

				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {

					assignmentList = assignmentService
							.searchAssignmentsForFaculty(username,
									Long.parseLong(userdetails1.getProgramId()));

					for (Assignment a : assignmentList) {
						if (a.getFilePath() != null) {
							a.setShowFileDownload("true");
						} else {
							a.setShowFileDownload("false");
						}
					}
					

				} else if (userDetails.getAuthorities().contains(Role.ROLE_HOD)) {

					

					assignmentList = assignmentService
							.searchAssignmentForHOD(Long.parseLong(userdetails1
									.getProgramId()));

					for (Assignment a : assignmentList) {
						if (a.getFilePath() != null) {
							a.setShowFileDownload("true");
						} else {
							a.setShowFileDownload("false");
						}
					}

				} else {

				}

			} else {
				assignmentList = assignmentService.searchAssignmentsForFaculty(
						username, Long.parseLong(userdetails1.getProgramId()),
						Long.valueOf(courseId));
				for (Assignment a : assignmentList) {
					if (a.getFilePath() != null) {
						a.setShowFileDownload("true");
					} else {
						a.setShowFileDownload("false");
					}
				}
				
			}

			
			for (Assignment a : assignmentList) {
				Long cId = a.getCourseId();
				Course course = courseService.findByID(cId);
				String courseName = course.getCourseName();

				a.setCourseName(courseName);
				assignmentList.set(assignmentList.indexOf(a), a);
				m.addAttribute("courseName", courseName);
			}
			m.addAttribute("allCourses", courseService.findByUserActive(
					principal.getName(), userdetails1.getProgramName()));
			m.addAttribute("page", page);
			m.addAttribute("assignmentList", assignmentList);
			m.addAttribute("q", getQueryString(assignment));

			if (assignmentList == null || assignmentList.size() == 0) {
				setNote(m, "No Assignments found");
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(m, "Error in getting Assignments List");
		}
		return "assignment/facultyAssignmentList";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value = "/downloadFile", method = { RequestMethod.GET,
		RequestMethod.POST })
		public ResponseEntity<ByteArrayResource> downloadFile(
		@RequestParam(required = false, name = "id", defaultValue = "") String id,
		@RequestParam(required = false, name = "saId", defaultValue = "") String saId,
		@RequestParam(required = false, name = "saGId", defaultValue = "") String saGId,
		@RequestParam(required = false, name = "libraryId", defaultValue = "") String libraryId,
		@RequestParam(required = false, name = "filePath", defaultValue = "") String filePath,
		HttpServletRequest request, HttpServletResponse response,Principal p) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		String projectUrl = "";
		String username = p.getName();
		Token userDetails = (Token) p;
		try {

			LmsVariables showDisclaimerInAssignment = lmsVariablesService.getLmsVariableBykeyword("showDisclaimerInAssignment");
			if (StringUtils.isEmpty(filePath)) {
				if (!StringUtils.isEmpty(id)) {
					Assignment assignment = assignmentService.findByID(Long.valueOf(id));
					if (assignment != null) {
						filePath = assignment.getFilePath();
						if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
							Date currDate = Utils.getInIST();
							Date startDate = Utils.converFormatsDateAlt(assignment.getStartDate());
							if(startDate.after(currDate)) {
								return null;
							}
						}
					}
					StudentAssignment sa = studentAssignmentService.findAssignmentSubmission(username, assignment.getId());
					if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT) && showDisclaimerInAssignment.getValue().equals("Yes")) {
						if(null == sa.getIsAcceptDisclaimer() && null == sa.getAcceptDisclaimerDate()) {
							studentAssignmentService.updateDisclaimer(String.valueOf(assignment.getId()), username);
						}
					}
					if(null == filePath || "".equals(filePath)) {
						return null;
					}
					
				}else if(!StringUtils.isEmpty(saGId)){
					StudentAssignment agId = studentAssignmentService.getAllGroupAssignemnt(saGId);
					List<String> saFiles = studentAssignmentService.getGroupAssignmentStudentFiles(agId.getGroupId(),agId.getAssignmentId());
					ServletOutputStream out = response.getOutputStream();
					response.setContentType("Content-type: text/zip");
					File folderPath = new File(workDir + File.separator
					+ "allFiles");
					if (!folderPath.exists()) {
						folderPath.mkdir();
					}
		
					for (String file : saFiles) {
		
						if(file.startsWith("/")) {
							file = StringUtils.substring(file, 1);
						}
						file = file.replace("/\\", "/");
						file = file.replace("\\\\","/");
						file = file.replace("\\","/");
						file = file.replace("//","/");
						File fileNew = new File(file);
						logger.info(folderPath.getAbsolutePath());
						logger.info("asignQuesFile---->"+file);
						InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
						File dest = new File(folderPath.getAbsolutePath() + File.separator + fileNew.getName());
						FileUtils.copyInputStreamToFile(inpStream, dest);
		
					}
					String filename = "assignmentFiles.zip";
					response.setHeader("Content-Disposition",
					"attachment; filename=" + filename + "");
					projectUrl = "/" + "workDir" + "/" + folderPath.getName()
					+ ".zip";
					pack(folderPath.getAbsolutePath(), out);
					FileUtils.deleteDirectory(folderPath);
					return null;
		
				} else {
					if (!StringUtils.isEmpty(saId)) {
						StudentAssignment sa = studentAssignmentService.findByID(Long.valueOf(saId));
					if (sa != null)
						filePath = sa.getStudentFilePath();
					}
				}
				if (!StringUtils.isEmpty(libraryId)) {
					Library library = libraryService.findByID(Long.valueOf(libraryId));
				if (library != null)
					filePath = library.getFilePath();
				}
			}
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			if (filePath.contains(",") && StringUtils.isEmpty(libraryId)) {

				File folderPath = new File(workDir + File.separator
				+ "allFiles");
				List<String> files = Arrays.asList(filePath.split(","));
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}
	
				for (String file : files) {
					if(file.startsWith("/")) {
						file = StringUtils.substring(file, 1);
					}
					file = file.replace("/\\", "/");
					file = file.replace("\\\\","/");
					file = file.replace("\\","/");
					file = file.replace("//","/");
					File fileNew = new File(file);
					logger.info(folderPath.getAbsolutePath());
					logger.info("asignQuesFile---->"+file);
					InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
					File dest = new File(folderPath.getAbsolutePath() + File.separator + fileNew.getName());
					FileUtils.copyInputStreamToFile(inpStream, dest);
		
				}
				String filename = "assignmentFiles.zip";
				response.setHeader("Content-Disposition",
				"attachment; filename=" + filename + "");
				projectUrl = "/" + "workDir" + "/" + folderPath.getName()
				+ ".zip";
				pack(folderPath.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				if (StringUtils.isEmpty(filePath)) {
					request.setAttribute("error", "true");
					request.setAttribute("errorMessage",
					"Error in downloading file.");
				}
				File downloadFile = new File(filePath);
				String path = "";
				if(filePath.startsWith("/")) {
					path = StringUtils.substring(filePath, 1);
				}else {
					path = filePath;
				}
				path = path.replace("/\\", "/");
				path = path.replace("\\\\","/");
				path = path.replace("\\","/");
				path = path.replace("//","/");
				byte[] data = amazonS3ClientService.getFile(path);
				logger.info("data"+data.length);
				ByteArrayResource resource = new ByteArrayResource(data);
	
				return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
				.body(resource);
			
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage", "Error in downloading file.");
		} finally {
			if (inputStream != null)
			IOUtils.closeQuietly(inputStream);
			if (outStream != null)
			IOUtils.closeQuietly(outStream);
		}
			return null;
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value = "/downloadFileForCourseUpload", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView downloadFileForCourseUpload(
			@RequestParam(required = false, name = "filePath", defaultValue = "") String filePath,
			HttpServletRequest request, HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		String projectUrl = "";
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			if (filePath.contains(",")) {

				File folderPath = new File(workDir + File.separator
						+ "allFiles");
				List<String> files = Arrays.asList(filePath.split(","));
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}

				for (String file : files) {
					File fileNew = new File(file);
					// files.add(file);

					File dest = new File(folderPath.getAbsolutePath()
							+ File.separator + fileNew.getName());
					FileUtils.copyFile(fileNew, dest);

				}
				String filename = "assignmentFiles.zip";
				response.setHeader("Content-Disposition",
						"attachment; filename=" + filename + "");
				projectUrl = "/" + "workDir" + "/" + folderPath.getName()
						+ ".zip";
				pack(folderPath.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				if (StringUtils.isEmpty(filePath)) {
					request.setAttribute("error", "true");
					request.setAttribute("errorMessage",
							"Error in downloading file.");
				}

				// get absolute path of the application
				ServletContext context = request.getSession()
						.getServletContext();
				File downloadFile = new File(filePath);
				inputStream = new FileInputStream(downloadFile);

				// get MIME type of the file
				String mimeType = context.getMimeType(filePath);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}

				// set content attributes for the response
				/* response.setContentType(mimeType); */
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format(
						"attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				outStream = response.getOutputStream();
				IOUtils.copy(inputStream, outStream);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage", "Error in downloading file.");
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);

		}
		return null;
	}

	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/createAssignmentFromGroup", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createAssignmentFromGroup(
			@RequestParam(required = false) Long id, Principal principal,
			@RequestParam(required = false) Long courseId, Model m) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));

		Groups group = new Groups();
		Assignment assignment = new Assignment();
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		if (courseId != null) {
			assignment.setCourse(courseService.findByID(courseId));
			m.addAttribute("courseId", courseId);
			m.addAttribute("showCourseHeader", true);
		} else {
			m.addAttribute("showCourseHeader", false);
		}

		if (id != null) {
			group = groupService.findByID(id);
			assignment = assignmentService.findByID(id);
			m.addAttribute("edit", "true");
		}
		List<UserCourse> totalStudentsList = userCourseService
				.findStudentsForFaculty(courseId);

		m.addAttribute("totalStudentsList", totalStudentsList.size());
		assignment.setCourseId(courseId);
		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		
		m.addAttribute("allGroups", groupService
				.findByFacultyAndCourseWithGroupCourse(username,
						String.valueOf(courseId)));
		m.addAttribute("assignment", assignment);
		m.addAttribute("courseId", courseId);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		return "assignment/createAssignmentFromGroupFinal";
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/saveGroupAssignmentAllocation", method = { RequestMethod.POST })
	public String saveGroupAssignmentAllocation(
			@ModelAttribute Assignment assignment, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String selectedGrps ="";
		if(null != assignment.getSelectedGrps())
		{
			selectedGrps = assignment.getSelectedGrps();
		}
		List<String> grps = assignment.getGrps();
		if (grps.isEmpty()) {
			grps = Arrays.asList(StringUtils.split(selectedGrps, ","));
		}

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Assignment retrived = assignmentService.findByID(assignment.getId());

		try {
			if (grps != null && !grps.isEmpty()) {

				for (String g : grps) {

					ArrayList<StudentAssignment> groupAssignmentMappingList = new ArrayList<StudentAssignment>();
					Groups group = groupService.findByID(Long.valueOf(g));
					
					List<StudentGroup> studentGroup = studentGroupService
							.getStudentsBasedOnGroups(group.getId());

					for (StudentGroup sg : studentGroup) {

						StudentAssignment bean = new StudentAssignment();

						bean.setAcadMonth(assignment.getAcadMonth());
						bean.setAcadYear(assignment.getAcadYear());
						bean.setAssignmentId(assignment.getId());
						bean.setCourseId(retrived.getCourseId());
						bean.setUsername(sg.getUsername());
						bean.setActive("Y");
						bean.setGroupId(group.getId());
						bean.setEvaluatedBy(assignment.getFacultyId());
						bean.setCreatedBy(username);
						bean.setLastModifiedBy(username);
						bean.setStartDate(retrived.getStartDate());
						bean.setEndDate(retrived.getEndDate());

						List<StudentAssignment> studentAssList = studentAssignmentService
								.getStudentUsername(bean.getAssignmentId(),
										bean.getCourseId());

						if (studentAssList.isEmpty()) {
							groupAssignmentMappingList.add(bean);
						} else {
							List<String> names = new ArrayList<String>();

							for (StudentAssignment ass : studentAssList) {
								names.add(ass.getUsername());
								ass.setActive("Y");
							}

							if (!names.contains(bean.getUsername())) {
								groupAssignmentMappingList.add(bean);
							}
						}

					}

					studentAssignmentService
							.insertBatch(groupAssignmentMappingList);
					setSuccess(m, "Assignment allocated to "
							+ assignment.getGrps().size()
							+ " groups successfully");

				}
				return viewByGroupAssignment(assignment.getId(), m, principal);
			}
		}

		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "assignment/createAssignment";
		}
		m.addAttribute("assignment", assignment);
		return "assignment/viewByGroup";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/facultyAllocation", method = {
			RequestMethod.POST, RequestMethod.GET })
	public String facultyAllocation(@ModelAttribute Assignment assignment,
			Model m, Principal principal,
			@RequestParam(required = false) Long assignedId) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("courseIdList", courseService.findAllActive());
		m.addAttribute("assignmentOnCourse", assignmentService.findAllActive());
		m.addAttribute("facultyIdList",
				assignmentService.findAllFacultyByAssignment(assignedId));
		m.addAttribute("assignment", new Assignment());
		return "assignment/facultyLeave";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/getAssignmentBasedOnCourse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssignmentBasedOnCourse(
			@RequestParam(name = "courseId") String courseId,
			Principal principal) {

		String json = "";
		String username = principal.getName();

		List<Assignment> assignedCourse = assignmentService
				.getAssignmentBasedOnCourse((Long.valueOf(courseId)));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Assignment assg : assignedCourse) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(assg.getAssignmentName(), assg.getAssignmentName());
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

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/searchFacultyAllocationForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchFacultyAllocationForm(
			RedirectAttributes redirectAttributes,
			Model m,
			Long id,
			@ModelAttribute Assignment assignment,
			Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignmentList",
				"Search Assignments", false, false));
		m.addAttribute("assignment", assignment);

		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord")
							.equals("")) {

			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(username,
						userdetails1.getProgramName()));
		m.addAttribute("preAssigment", new ArrayList());

		m.addAttribute("allAssignments", assignmentService.findAll());
		return "assignment/facultyAllocation";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/searchFacultyAllocation", method = { RequestMethod.POST })
	public String searchFacultyAllocationForm(Model m, Principal principal,
			@RequestParam(required = false) Long courseId,
			@ModelAttribute Assignment assignment, @RequestParam Long id) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Faculty Allocation", true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

	
		assignment = assignmentService.findByID(id);
		m.addAttribute("allFaculty", userService.findAllFaculty());
		List<Assignment> list1 = new ArrayList<Assignment>();
		list1.add(assignment);

		if (list1 != null && list1.size() > 0) {
			m.addAttribute("list1", list1);
			m.addAttribute("rowCount", list1.size());
			m.addAttribute("assignment", assignment);
		} else {
			setNote(m, "No Students found for this course");
		}
		return "assignment/facultyAssigned";

	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/saveFacultyAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveFacultyAllocation(@ModelAttribute Assignment assignment,
			Model m, Principal principal,
			@RequestParam(required = false) String facultyId,
			RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("assignment", "Faculty Allocate",
				true, true, true, true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<User> listOfFaculty = new ArrayList<User>();
		listOfFaculty = userService.findAllFaculty();
		m.addAttribute("listOfFaculty", listOfFaculty);
		assignmentService.updateFacultyAssigned(facultyId, assignment.getId());

		if (assignment.getFacultyId().equalsIgnoreCase(facultyId)) {
			setSuccess(redirectAttributes, "Faculty Changed successfully!");
		} else {
			setError(m, "Error!");
		}

		m.addAttribute("assignment", assignment);
		return "redirect:/searchFacultyAllocationForm";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/getAssigmentByCourseOnly", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAssigmentByCourseOnly(
			@RequestParam(name = "courseId") String courseId,
			Principal principal) {
		String json = "";
		String username = principal.getName();

		List<Assignment> assigments = assignmentService.findByCourse(Long
				.valueOf(courseId));
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Assignment ass : assigments) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(ass.getId()), ass.getAssignmentName());
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

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/searchFacultyForAssignment", method = { RequestMethod.POST })
	public String searchFacultyForAssignment(Model m, Principal principal,
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String assignmentId) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Faculty Allocation", true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<Assignment> list1 = assignmentService
				.getAssignmentBasedOnCourse(courseId);
		m.addAttribute("allFaculty", userService.findAllFaculty());

		if (list1 != null && list1.size() > 0) {
			m.addAttribute("list1", list1);
			m.addAttribute("rowCount", list1.size());
			m.addAttribute("assignment", new Assignment());
		} else {
			setNote(m, "No Students found for this course");
		}
		return "assignment/facultyAssigned";

	}

	@Secured({ "ROLE_FACULTY", "ROLE_STUDENT", "ROLE_ADMIN" })
	@RequestMapping(value = "/downloadAllFile", method = { RequestMethod.GET,
		RequestMethod.POST })
		public void downloadAllFile(HttpServletRequest request,
		HttpServletResponse response, Long assignmentId)
		throws ServletException, IOException {

		// Set the content type based to zip
		response.setContentType("Content-type: text/zip");
		Assignment assignment = assignmentService.findByID(assignmentId);
		String filename = assignment.getAssignmentName().replace(",", " ") + ".zip";
		response.setHeader("Content-Disposition", "attachment; filename="
		+ filename + "");

		// List of files to be downloaded
		List<File> files = new ArrayList();
		List<String> username = new ArrayList<String>();

		// Getting students' usernames list
		List<StudentAssignment> assignmentList = studentAssignmentService.getStduentFiles(assignmentId);
		File folderPath = new File(downloadAllFolder + File.separator+ RandomStringUtils.randomAlphanumeric(10));
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		for (StudentAssignment sa : assignmentList) {
			String file = sa.getStudentFilePath();
			if(file.startsWith("/")) {
				file = StringUtils.substring(file, 1);
			}
			file = file.replace("/\\", "/");
			file = file.replace("\\\\","/");
			file = file.replace("\\","/");
			file = file.replace("//","/");
			File fileNew = new File(file);
			logger.info("asignQuesFile---->"+file);
			InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
			String ext1 = FilenameUtils.getExtension(fileNew.getName());
			File dest = new File(folderPath.getAbsolutePath() + File.separator + sa.getUsername() + "." + ext1);
			FileUtils.copyInputStreamToFile(inpStream, dest);
			

		}

		ServletOutputStream out = response.getOutputStream();

		pack(folderPath.getAbsolutePath(), out);
		FileUtils.deleteDirectory(folderPath);
		}
	
	public static void pack(String sourceDirPath, ServletOutputStream out)
			throws IOException {

		try (ZipOutputStream zs = new ZipOutputStream(new BufferedOutputStream(
				out))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp)
					.filter(path -> !Files.isDirectory(path))
					.forEach(
							path -> {
								ZipEntry zipEntry = new ZipEntry(pp.relativize(
										path).toString());
								try {
									zs.putNextEntry(zipEntry);
									zs.write(Files.readAllBytes(path));
									zs.closeEntry();
								} catch (Exception e) {
									logger.error("Ëxception", e);
								}
							});
		}
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/searchAssignmentTestForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAssignmentTestForm(Model m, Principal principal,
			@ModelAttribute Assignment assignment) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Search Assignment Test", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<String> acadMonths = courseService.getAllAcadMonth();
		List<String> acadYears = courseService.getAllAcadYear();
		List<String> list1 = new ArrayList<String>();
		list1.add("Assignment");
		list1.add("Test");
		list1.add("Learning Resource");

		m.addAttribute("list1", list1);
		m.addAttribute("acadMonths", acadMonths);
		m.addAttribute("acadYears", acadYears);
		
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "assignment/searchAllAdmin";
		} else {
		return "assignment/searchAll";
		}



	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(value = "/searchAssignmentTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAssignmentTest(Model m, Principal principal,
			@ModelAttribute Assignment assignment,
			@RequestParam("dropdwonId") String dropdwonId,
			@RequestParam String acadMonth, @RequestParam String acadYear) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Search Assignment Test", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		Token userDetails = (Token) principal;

		List<String> acadMonths = courseService.getAllAcadMonth();
		List<String> acadYears = courseService.getAllAcadYear();
		List<String> list1 = new ArrayList<String>();
		list1.add("Assignment");
		list1.add("Test");
		list1.add("Learning Resource");

		m.addAttribute("list1", list1);
		m.addAttribute("acadMonths", acadMonths);
		m.addAttribute("acadYears", acadYears);
		if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
			if (dropdwonId.equalsIgnoreCase("Assignment")) {

				List<Assignment> assignmentList = assignmentService
						.findAllAssignmentForFaculty(acadMonth, acadYear,
								username);
				for (Assignment a : assignmentList) {
					Long cId = a.getCourseId();
					if(null != cId){
					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();
					logger.info("CourseName---->"+ courseName);
					a.setCourseName(courseName);
					assignmentList.set(assignmentList.indexOf(a), a);
					m.addAttribute("courseName", courseName);
					}
				}

				m.addAttribute("assignmentList", assignmentList);
				if (assignmentList == null || assignmentList.size() == 0) {
					setNote(m, "No Assignments Found");
				}
			} else {
				if (dropdwonId.equalsIgnoreCase("Test")) {
					List<Test> testList = testService.findAllTestForFaculty(
							acadMonth, acadYear, username);
					for (Test a : testList) {
						Long cId = a.getCourseId();
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();

						a.setCourseName(courseName);
						testList.set(testList.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}
					m.addAttribute("testList", testList);
					if (testList == null || testList.size() == 0) {
						setNote(m, "No Tests Found");
					}
				} else if (dropdwonId.equalsIgnoreCase("Learning Resource")) {
					List<Content> allContent = contentService
							.findAllContentForFaculty(acadMonth, acadYear,
									username);
					for (Content a : allContent) {
						Long cId = a.getCourseId();
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();

						a.setCourseName(courseName);
						allContent.set(allContent.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}
					m.addAttribute("allContent", allContent);
					if (allContent == null || allContent.size() == 0) {
						setNote(m, "No content Found");
					}
				}
			}
			m.addAttribute("dropdownId",dropdwonId);
			return "assignment/searchAllFaculty";
		}
		if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			if (dropdwonId.equalsIgnoreCase("Assignment")) {

				List<StudentAssignment> assignmentList = studentAssignmentService
						.findAllAssignment(acadMonth, acadYear, username);
				for (StudentAssignment a : assignmentList) {
					Long cId = a.getCourseId();
					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();

					a.setCourseName(courseName);
					assignmentList.set(assignmentList.indexOf(a), a);
					m.addAttribute("courseName", courseName);
				}

				m.addAttribute("assignmentList", assignmentList);
				if (assignmentList == null || assignmentList.size() == 0) {
					setNote(m, "No Assignments Found");
				}
			} else {
				if (dropdwonId.equalsIgnoreCase("Test")) {
					List<StudentTest> testList = studentTestService
							.findAllTest(acadMonth, acadYear, username);
					for (StudentTest a : testList) {
						Long cId = Long.valueOf(a.getCourseId());
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();

						a.setCourseName(courseName);
						testList.set(testList.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}
					m.addAttribute("testList", testList);
					if (testList == null || testList.size() == 0) {
						setNote(m, "No Tests Found");
					}
				} else if (dropdwonId.equalsIgnoreCase("Learning Resource")) {
					List<Content> allContent = contentService
							.findAllStudentContent(acadMonth, acadYear,
									username);
					for (Content a : allContent) {
						Long cId = a.getCourseId();
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();

						a.setCourseName(courseName);
						allContent.set(allContent.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}
					m.addAttribute("allContent", allContent);
					if (allContent == null || allContent.size() == 0) {
						setNote(m, "No content Found");
					}
				}
			}
			m.addAttribute("dropdownId",dropdwonId);
			return "assignment/searchAllStudent";
		}

		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			if (dropdwonId.equalsIgnoreCase("Assignment")) {

				List<Assignment> assignmentList = assignmentService
						.findAllAssignment(acadMonth, acadYear);
				for (Assignment a : assignmentList) {
					Long cId = a.getCourseId();
					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();

					a.setCourseName(courseName);
					assignmentList.set(assignmentList.indexOf(a), a);
					m.addAttribute("courseName", courseName);
				}

				m.addAttribute("assignmentList", assignmentList);
				if (assignmentList == null || assignmentList.size() == 0) {
					setNote(m, "No Assignments Found");
				}
			} else {
				if (dropdwonId.equalsIgnoreCase("Test")) {
					List<Test> testList = testService.findAllTest(acadMonth,
							acadYear);
					for (Test a : testList) {
						Long cId = a.getCourseId();
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();

						a.setCourseName(courseName);
						testList.set(testList.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}
					m.addAttribute("testList", testList);
					if (testList == null || testList.size() == 0) {
						setNote(m, "No Tests Found");
					}
				} else if (dropdwonId.equalsIgnoreCase("Learning Resource")) {
					List<Content> allContent = contentService.findAllContent(
							acadMonth, acadYear);

					for (Content a : allContent) {
						Long cId = a.getCourseId();
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();

						a.setCourseName(courseName);
						allContent.set(allContent.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}
					m.addAttribute("allContent", allContent);
					if (allContent == null || allContent.size() == 0) {
						setNote(m, "No content Found");
					}
				}
			}
			m.addAttribute("dropdownId",dropdwonId);
		}
		
		return "assignment/searchAllForAdmin";

	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(value = "/viewThisAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewThisAssignment(Model m,
			@ModelAttribute StudentAssignment assignment, Principal principal,
			@RequestParam Long assignmentId) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Search Assignment Test", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<StudentAssignment> assignmentlist = studentAssignmentService
				.findOneAssignment(assignmentId);
		for (StudentAssignment sa : assignmentlist) {
			Course c = courseService.findByID(sa.getCourseId());
			sa.setCourseName(c.getCourseName());
			String studentFilePath = StringUtils.trimToNull(sa
					.getStudentFilePath());
			if (studentFilePath != null) {
				sa.setShowStudentFileDownload("true");
			} else {
				sa.setShowStudentFileDownload("false");
			}
			Assignment a = assignmentService.findByID(sa.getAssignmentId());
			String filePath = StringUtils.trimToNull(a.getFilePath());
			if (filePath != null) {
				sa.setShowFileDownload("true");
			} else {
				sa.setShowFileDownload("fasle");
			}
			assignmentlist.set(assignmentlist.indexOf(sa), sa);
		}

		m.addAttribute("assignmentlist", assignmentlist);
		return "assignment/viewAssignment";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/makeAssignmentInactive", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView makeAssignmentInactive(HttpServletRequest request,
			@ModelAttribute Assignment assignment, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {
		long AssignmentId = Long.parseLong(StringUtils.trim(request
				.getParameter("id")));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		assignmentDao.updateInactiveAssignment(AssignmentId);
		setSuccess(redirectAttrs, "Assignment made inactive successfully");
		return new ModelAndView("redirect:/searchAssignment");
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/createGroupAssignmentsForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createGroupAssignmentsForm(Model m,
			@ModelAttribute Assignment assignment,
			@RequestParam(required = true) Long courseId, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<UserCourse> totalStudentsList = userCourseService
				.findStudentsForFaculty(courseId);

		m.addAttribute("totalStudentsList", totalStudentsList.size());
		assignment.setCourseId(courseId);
		m.addAttribute("assignment", assignment);
		assignment.setCourse(courseService.findByID(courseId));
		
		m.addAttribute("allGroups", groupService
				.findByFacultyAndCourseWithGroupCourse(username,
						String.valueOf(courseId)));
		m.addAttribute("allCourses", courseService.findByUserActive(
				principal.getName(), userdetails1.getProgramName()));
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		return "assignment/createGroupAssignments";

	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/createGroupAssignments", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createGroupAssignments(
			Model m,
			@ModelAttribute Assignment assignment,
			@RequestParam(name = "assignmentName") List<String> assignmentName,
			RedirectAttributes redirectAttrs,
			// @RequestParam ("file") ArrayList<String> file,
			@RequestParam(name = "filePaths1", required = false) List<MultipartFile> filePaths1,
			@RequestParam(name = "filePaths2", required = false) List<MultipartFile> filePaths2,
			@RequestParam(name = "filePaths3", required = false) List<MultipartFile> filePaths3,
			@RequestParam(name = "filePaths4", required = false) List<MultipartFile> filePaths4,
			@RequestParam(name = "filePaths5", required = false) List<MultipartFile> filePaths5,
			// @RequestParam(name = "filePaths") ArrayList<String> filePaths,
			@RequestParam(name = "grps1") List<String> grps1,
			@RequestParam(name = "grps2") List<String> grps2,
			@RequestParam(name = "grps3") List<String> grps3,
			@RequestParam(name = "grps4") List<String> grps4,
			@RequestParam(name = "grps5") List<String> grps5,
			@RequestParam Long courseId, Principal p) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));

		Course c = courseService.findByID(courseId);
		String acadMonth = c.getAcadMonth();
		String acadYear = c.getAcadYear();
		assignment.setCourseId(courseId);


		Map<Integer, List<MultipartFile>> mapper = new HashMap<>();

		mapper.put(1, filePaths1);
		mapper.put(2, filePaths2);
		mapper.put(3, filePaths3);
		mapper.put(4, filePaths4);
		mapper.put(5, filePaths5);

		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		map.put(1, grps1);
		map.put(2, grps2);
		map.put(3, grps3);
		map.put(4, grps4);
		map.put(5, grps5);
		try {
		/* New Audit changes start */
			
//		Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
//		BusinessBypassRule.validateNumeric(assignment.getMaxScore());
//		BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
//		validateAssignmentType(assignment.getAssignmentType());
//		Course course = courseService.findByID(assignment.getCourseId());
//		if(null == course) {
//			throw new ValidationException("Invalid Course selected.");
//		}
//		BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
//		BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
//		BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
//		BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
//		BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
//		BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());
//		if(!Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate())) {
//			setError(redirectAttrs, "Invalid Start date and End date");
//			return "redirect:/createGroupAssignmentsForm?courseId=" + assignment.getCourseId();
//		}
//		if(Double.valueOf(assignment.getMaxScore()) < 0.0) {
//			setError(redirectAttrs, "Invalid total score");
//			return "redirect:/createGroupAssignmentsForm?courseId=" + assignment.getCourseId();
//		}
		/* New Audit changes end */
		
		if (grps1.isEmpty() && grps2.isEmpty() && grps3.isEmpty()
				&& grps4.isEmpty() && grps5.isEmpty()) {
			setNote(redirectAttrs, "No Groups Selected!");
			return "redirect:/createGroupAssignmentsForm?courseId="
					+ assignment.getCourseId();
		} else {
			
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					assignment.setSendEmailAlertToParents("Y");
					assignment.setSendSmsAlertToParents("Y");
				}

				for (int i = 1; i <= assignmentName.size(); i++) {
					if (!assignmentName.get(i - 1).equals("")) {

						assignment.setAcadMonth(acadMonth);
						assignment.setAcadYear(Integer.valueOf(acadYear));
						assignment.setCreatedBy(p.getName());
						assignment.setLastModifiedBy(p.getName());
						assignment.setFacultyId(p.getName());
						assignment.setCourse(courseService.findByID(courseId));
						assignment.setAssignmentName(assignmentName.get(i - 1));
						assignment.setGrps(map.get(i));
						if (mapper.get(i) != null) {
							assignment.setFilePath(null);
							assignment.setFilePreviewPath(null);
							//Audit change start
							for(MultipartFile file : mapper.get(i)) {
								if(!file.isEmpty()) {
									Tika tika = new Tika();
									  String detectedType = tika.detect(file.getBytes());
									if (file.getOriginalFilename().contains(".")) {
										Long count = file.getOriginalFilename().chars().filter(o -> o == ('.')).count();
										logger.info("length--->"+count);
										if (count > 1 || count == 0) {
											setError(redirectAttrs, "File uploaded is invalid!");
											redirectAttrs.addAttribute("courseId", assignment.getCourseId());
											return "redirect:/createGroupAssignmentsForm";
										}else {
											String extension = FilenameUtils.getExtension(file.getOriginalFilename());
											logger.info("extension--->"+extension);
											if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
													|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
												setError(redirectAttrs, "File uploaded is invalid!");
												redirectAttrs.addAttribute("courseId", assignment.getCourseId());
												return "redirect:/createGroupAssignmentsForm";
											} else {
												byte [] byteArr=file.getBytes();
												if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																					(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																					("text/plain").equals(detectedType)) {
													logger.info("file is valid--->");
												}else {
													setError(redirectAttrs, "File uploaded is invalid!");
													redirectAttrs.addAttribute("courseId", assignment.getCourseId());
													return "redirect:/createGroupAssignmentsForm";
												}
											}
										}
									}else {
										setError(redirectAttrs, "File uploaded is invalid!");
										redirectAttrs.addAttribute("courseId", assignment.getCourseId());
										return "redirect:/createGroupAssignmentsForm";
									}
								}
							}
							String multipleAssignmentErrorMsg = "Success";
							HtmlValidation.validateHtml(assignment, Arrays.asList("assignmentText"));
							multipleAssignmentErrorMsg = saveGroupAssignment(assignment, mapper.get(i), m, p,redirectAttrs,multipleAssignmentErrorMsg);
							logger.info("multipleAssignmentErrorMsg---->"+multipleAssignmentErrorMsg);
							if(!multipleAssignmentErrorMsg.equals("Success")) {
								throw new ValidationException(multipleAssignmentErrorMsg);
							}
							//Audit change end
						} else {
							/* logger.info("Multipart Files is null"); */
						}
					}

				}
				setSuccess(redirectAttrs, "Assignments created successfully!");
			
			}
		}catch(ValidationException ve) {
			setError(redirectAttrs, ve.getMessage());
			redirectAttrs.addAttribute("courseId", assignment.getCourseId());
			return "redirect:/createGroupAssignmentsForm";
		} catch (Exception e) {
			logger.error("Ëxception", e);
			setError(redirectAttrs, e.getMessage());
			redirectAttrs.addAttribute("courseId", assignment.getCourseId());
			return "redirect:/createGroupAssignmentsForm";
		}
		m.addAttribute("assignment", assignment);

		return "redirect:/searchFacultyAssignment?courseId="
				+ assignment.getCourseId();
	}


	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/viewGroupStudentsForAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewGroupStudentsForAssignment(@ModelAttribute Groups groups,
			Long courseId, @RequestParam(required = false) Long id, Model m,
			@RequestParam(required = false) Long assignmentId,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("viewGroup", "Group Details",
				true, false));
		String username = principal.getName();
		Map<String, String> map = new HashMap<String, String>();
		List<StudentGroup> students = studentGroupService
				.getStudentsBasedOnGroups(id);
		List<String> allocatedStudents = studentAssignmentService
				.findStudentUsernames(assignmentId);
		Groups g = groupService.findByID(id);
		groups.setGroupName(g.getGroupName());
		for (StudentGroup sg : students) {
			if (allocatedStudents.contains(sg.getUsername())) {
				sg.setAllocated("Allocated");
			} else {
				sg.setAllocated("Not Allocated");
			}

			User u1 = userService.findByUserName(sg.getUsername());
			sg.setRollNo(u1.getRollNo());

			students.set(students.indexOf(sg), sg);
		}
		m.addAttribute("assignmentId", assignmentId);
		m.addAttribute("students", students);
		m.addAttribute("groups", groups);
		return "group/viewGroupMemebersForAssignment";

	}

	
	
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/createAssignmentModuleForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createAssignmentModuleForm(
			@RequestParam(required = false) Long id, Model m,
			Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();

		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);

		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		Assignment assignment = new Assignment();

		if (id != null) {
			assignment = assignmentService.findByID(id);
			m.addAttribute("edit", "true");
		}


		logger.info("Program Name--->" + ProgramName);
		List<Course> moduleList = courseService.findModulesByUsername(username,
				Long.parseLong(userdetails1.getProgramId()));
		m.addAttribute("moduleList", moduleList);
		m.addAttribute("allCampuses", userService.findCampus());
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		m.addAttribute("assignment", assignment);
		return "assignment/createAssignmentForModule";
	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/createAssignmentForModule", method = { RequestMethod.POST })
	public String createAssignmentForModule(@ModelAttribute Assignment assignment,
			@RequestParam(required = false) Long myId,
			@RequestParam("file") List<MultipartFile> files, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			if (assignment.getId() != null) {
				assignmentService.update(assignment);
			} else {
				/* New Audit changes start */
				HtmlValidation.validateHtml(assignment,Arrays.asList("assignmentText"));
				Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
				BusinessBypassRule.validateNumeric(assignment.getMaxScore());
				BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
				validateAssignmentType(assignment.getAssignmentType());
				Course course = courseService.checkIfExistsInDB("moduleId",String.valueOf(assignment.getModuleId()));
				if(null == course) {
					throw new ValidationException("Invalid Module selected.");
				}
				Course acadYear = courseService.checkIfExistsInDB("acadYear",String.valueOf(assignment.getAcadYear()));
				if(null == acadYear) {
					throw new ValidationException("Invalid acadYear selected.");
				}
				if(!assignment.getPlagscanRequired().equals("Yes") && !assignment.getPlagscanRequired().equals("No")) {
					throw new ValidationException("Invalid Input.");
				}
//				BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
				BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
				BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
				BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
				BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
				BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());

//				if(!Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate())) {
//					setError(redirectAttributes, "Invalid Start date and End date");
//					return "redirect:/createAssignmentModuleForm";
//				}
//				if(Double.valueOf(assignment.getMaxScore()) < 0.0) {
//					setError(redirectAttributes, "Invalid total score");
//					return "redirect:/createAssignmentModuleForm";
//				}
				/* New Audit changes end */
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						//Audit change start
						Tika tika = new Tika();
						  String detectedType = tika.detect(file.getBytes());
						if (file.getOriginalFilename().contains(".")) {
							Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
							logger.info("length--->"+count);
							if (count > 1 || count == 0) {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/createAssignmentModuleForm";
							}else {
								String extension = FilenameUtils.getExtension(file.getOriginalFilename());
								logger.info("extension--->"+extension);
								if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
										|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
									setError(redirectAttributes, "File uploaded is invalid!");
									return "redirect:/createAssignmentModuleForm";
								} else {
									byte [] byteArr=file.getBytes();
									if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																		("text/plain").equals(detectedType)) {
									String errorMessage = uploadAssignmentFileForS3(assignment, file);
									} else {
										setError(redirectAttributes, "File uploaded is invalid!");
										return "redirect:/createAssignmentModuleForm";
									}
								}
							}
						}else {
							setError(redirectAttributes, "File uploaded is invalid!");
							return "redirect:/createAssignmentModuleForm";
						}
						//Audit change end
					}
				}
				// if (errorMessage == null) {
				assignment.setCreatedBy(username);
				assignment.setLastModifiedBy(username);
				assignment.setFacultyId(username);// Assignment can be
													// created by
													// Faculty
													// only.
				
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					assignment.setSendEmailAlertToParents("Y");
					assignment.setSendSmsAlertToParents("Y");
				}
				String acadMonth = courseService.getAcadMonthByModuleIdAndAcadYear(String.valueOf(assignment.getModuleId()),String.valueOf(assignment.getAcadYear()));
				

				assignment.setAcadMonth(acadMonth);
				assignment.setAcadYear(assignment.getAcadYear());

				assignment.setFacultyId(username);
				
				
				
				
				assignmentService.insertWithIdReturn(assignment);
				Long parentModuleId  =  assignment.getId();
				logger.info("parentModuleId----->"+parentModuleId);
				List<Course> courseList = new ArrayList<Course>();
				courseList = courseService.findCoursesByModuleId(
							assignment.getModuleId(), username,
							Integer.valueOf(assignment.getAcadYear()));
				for(Course c1: courseList){
					Assignment childAssignment = new Assignment();
					childAssignment.setAcadYear(assignment.getAcadYear());
					childAssignment.setAcadMonth(assignment.getAcadMonth());
					childAssignment.setCourseId(c1.getId());
					childAssignment.setModuleId(assignment.getModuleId());
					childAssignment.setStartDate(assignment.getStartDate());
					childAssignment.setEndDate(assignment.getEndDate());
					childAssignment.setCreatedBy(assignment.getCreatedBy());
					childAssignment.setLastModifiedBy(assignment.getLastModifiedBy());
					childAssignment.setFilePath(assignment.getFilePath());
					childAssignment.setFilePreviewPath(assignment.getFilePreviewPath());
					childAssignment.setAssignmentType(assignment.getAssignmentType());
					childAssignment.setShowResultsToStudents(assignment.getShowResultsToStudents());
					childAssignment.setGroupId(assignment.getGroupId());
					childAssignment.setAllocateToStudents(assignment.getAllocateToStudents());
					childAssignment.setFacultyId(assignment.getFacultyId());
					childAssignment.setAllowAfterEndDate(assignment.getAllowAfterEndDate());
					childAssignment.setSendEmailAlert(assignment.getSendEmailAlert());
					childAssignment.setSendSmsAlert(assignment.getSendSmsAlert());
					if (sendAlertsToParents.equalsIgnoreCase("Y")) {
						childAssignment.setSendEmailAlertToParents("Y");
						childAssignment.setSendSmsAlertToParents("Y");
					}
					childAssignment.setRightGrant(assignment.getRightGrant());
					childAssignment.setAssignmentName(assignment.getAssignmentName());
					childAssignment.setMaxScore(assignment.getMaxScore());
					childAssignment.setAssignmentText(assignment.getAssignmentText());
					childAssignment.setThreshold(assignment.getThreshold());
					childAssignment.setPlagscanRequired(assignment.getPlagscanRequired());
					childAssignment.setRunPlagiarism(assignment.getRunPlagiarism());
					childAssignment.setSubmitByOneInGroup(assignment.getSubmitByOneInGroup());
					childAssignment.setParentModuleId(String.valueOf(parentModuleId));
					
					
					assignmentService.insertWithIdReturn(childAssignment);
					
					
					
				}
				if ("Y".equals(assignment.getAllocateToStudents())) {

					allocationToAllStudentsForModule(principal, assignment);
				}
				//assignment.setCourse(courseService.findByID(courseId));

				setSuccess(redirectAttributes,
						"Assignment uploaded successfully");

				m.addAttribute("assignment", assignment);
				myId = assignment.getAssignedId();
				assignment.setAssignedId(assignment.getId());

				redirectAttributes.addAttribute("id",
						parentModuleId);
				m.addAttribute("id", parentModuleId);
				
			}
		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttributes, ve.getMessage());
			return "redirect:/createAssignmentModuleForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttributes, "Error in creating assignment");
			redirectAttributes.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "redirect:/createAssignmentModuleForm";
		}
		return "redirect:/viewAssignmentForModule";
	}
	
	public String allocationToAllStudentsForModule(Principal principal, Assignment assignment) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

	
		String subject = " Assigment with name  ";
		Assignment retrived = assignmentService.findByID(assignment.getId());
		logger.info("Assignment Id----->"+assignment.getId());
		Course course = retrived.getModuleId() != null ? courseService
				.findByModuleIdAndAcadYear(String.valueOf(retrived.getModuleId()),String.valueOf(retrived.getAcadYear())) : null;
		StringBuffer buff = new StringBuffer(subject);
		buff.append(retrived.getAssignmentName());
		if (course != null) {
			buff.append("for Module ");
			buff.append(course.getModuleName());
		}
		buff.append(" allocated to you");
		subject = buff.toString();
		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
		List<String> courseIdList = new ArrayList<String>();
		for(Long l:childAssignmentId)
		{
			Assignment a = assignmentService.findByID(l);
			courseIdList.add(String.valueOf(a.getCourseId()));
		}
		List<StudentAssignment> students = new ArrayList<StudentAssignment>();
		students = studentAssignmentService
				.getStudentsForAssignmentModule(childAssignmentId,
						courseIdList,String.valueOf(assignment.getAcadYear()));
		
		ArrayList<StudentAssignment> studentAssignmentMappingList = new ArrayList<StudentAssignment>();
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		try {
			if (students != null
					&& students.size() > 0) {
				studentList.add(username);
				for (StudentAssignment studentUsername : students) {
					
					if(null == studentUsername.getAssignmentId()){
						StudentAssignment bean = new StudentAssignment();
						logger.info("studentUsername------->"+studentUsername);
						
						bean.setUsername(studentUsername.getUsername());
						bean.setCourseId(Long.valueOf(studentUsername.getCourseId()));
						bean.setAcadMonth(retrived.getAcadMonth());
						bean.setAcadYear(retrived.getAcadYear());
						Assignment childAssignmentId1 = new Assignment();
						childAssignmentId1 = assignmentService.findIdByCourseIdAndParentModuleId(String.valueOf(studentUsername.getCourseId()),String.valueOf(assignment.getId()));
						bean.setAssignmentId(childAssignmentId1.getId());
						bean.setEvaluatedBy(assignment.getFacultyId());
						bean.setCreatedBy(username);
						bean.setLastModifiedBy(username);
						bean.setStartDate(retrived.getStartDate());
						bean.setEndDate(retrived.getEndDate());
						bean.setActive("Y");

						studentList.add(studentUsername.getUsername());

						
						studentAssignmentMappingList.add(bean);
					}
					

				}

				studentAssignmentService
						.insertBatch(studentAssignmentMappingList);
	
				try {
					Map<String, Map<String, String>> result = null;
					if (!studentList.isEmpty()) {
						if ("Y".equals(retrived.getSendEmailAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}

						if ("Y".equals(retrived.getSendSmsAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}
						if ("Y".equals(retrived.getSendEmailAlert())) {

							for(String s: studentList) {
								
								List<String> singleStudList = new ArrayList<>();
								singleStudList.add(s);
								result = userService
										.findEmailByUserName(singleStudList);
								Map<String, String> email = result.get("emails");
								Map<String, String> mobiles = new HashMap();
								notifier.sendEmail(email, mobiles, subject, subject);
							}
						}

						if ("Y".equals(retrived.getSendSmsAlert())) {

							if (result != null)
								result = userService
										.findEmailByUserName(studentList);
							Map<String, String> email = new HashMap();
							Map<String, String> mobiles = result.get("mobiles");
							notifier.sendEmail(email, mobiles, subject, subject);
						}
					}
				} catch (Exception e) {
					logger.error("Exception", e);
				}

				return "Success";
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "Error";
		}
		return "Success";
	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/viewAssignmentForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewAssignmentForModule(@RequestParam(required = false) Long id, Model m,
			@RequestParam(required = false) Long campusId, Principal principal) {
		m.addAttribute("webPage", new WebPage("viewAssignment", "Assignment",
				true, false));
		Assignment assignment = new Assignment();
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("id", id);
		List<StudentAssignment> students = new ArrayList<StudentAssignment>();

		if (id != null) {
			assignment = assignmentService.findByID(id);
			assignment.setCourse(courseService.findByModuleIdAndAcadYear(String.valueOf(assignment.getModuleId()), String.valueOf(assignment.getAcadYear())));
			String filePath = StringUtils.trimToNull(assignment.getFilePath());
			if (filePath != null) {
				assignment.setShowFileDownload("true");
			} else {
				assignment.setShowFileDownload("false");
			}
			List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
			List<String> courseIdList = new ArrayList<String>();
			for(Long l:childAssignmentId)
			{
				Assignment a = assignmentService.findByID(l);
				courseIdList.add(String.valueOf(a.getCourseId()));
			}
			if (campusId != null) {
				assignment.setCampusId(campusId);
				students = studentAssignmentService
						.getStudentsForAssignmentForModuleOnCampusId(childAssignmentId,
								courseIdList,String.valueOf(assignment.getAcadYear()), campusId);
			} else {
				students = studentAssignmentService
						.getStudentsForAssignmentModule(childAssignmentId,
								courseIdList,String.valueOf(assignment.getAcadYear()));
			}
			m.addAttribute("allCampuses", userService.findCampus());
			for (StudentAssignment uc : students) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				students.set(students.indexOf(uc), uc);
				
			}
			StudentAssignment saForCount = studentAssignmentService.getNoOfStudentsAllocated(childAssignmentId);
			logger.info("noOfStudentAllocated-------->" + saForCount.getCount());
			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated",saForCount.getCount());

		}
		m.addAttribute("assignment", assignment);
		return "assignment/viewAssignmentForModule";
	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/createAssignmentFromMenuForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createAssignmentFromMenuForModule(
			@ModelAttribute Assignment assignment,
			@RequestParam(required = false) Long id, Principal principal,
			@RequestParam(required = false) Long courseId, Model m,
			HttpServletRequest request) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		List<Course> moduleList = courseService.findModulesByUsername(username,
				Long.parseLong(userdetails1.getProgramId()));
		m.addAttribute("moduleList", moduleList);
		m.addAttribute("allCampuses", userService.findCampus());
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		List<Course> courseList = new ArrayList<Course>();
		if (assignment.getCampusId() == null) {
			courseList = courseService.findCoursesByModuleId(
					assignment.getModuleId(), username,
					assignment.getAcadYear());
		} else {
			courseList = courseService.findCoursesByModuleIdAndCampusId(
					assignment.getModuleId(), username,
					assignment.getCampusId());
		}
		
		try {
			//Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			if (courseId != null) {
				assignment.setCourse(courseService.findByID(courseId));
				m.addAttribute("courseId", courseId);
				m.addAttribute("showCourseHeader", true);
			} else {
				m.addAttribute("showCourseHeader", false);
			}

			if (request.getSession().getAttribute("courseRecord") == null
					|| "".equals(request.getSession().getAttribute(
							"courseRecord"))) {

			} else {
				assignment.setCourse(courseService.findByID(courseId));
				assignment.setIdForCourse(String.valueOf(courseId));
				m.addAttribute("idForCourse", assignment.getIdForCourse());
				request.getSession().removeAttribute("courseRecord");
			}

			if (id != null) {

				assignment = assignmentService.findByID(id);
				assignment.setCourse(courseService.findByModuleIdAndAcadYear(String.valueOf(assignment.getModuleId()), String.valueOf(assignment.getAcadYear())));
				m.addAttribute("edit", "true");
			}
			if (!"INTDR".equals(userdetails1.getProgramName())) {
				m.addAttribute(
						"allCourses",
						courseService.findByUserActive(username,
								userdetails1.getProgramName()));
			} else {
				m.addAttribute("allCourses",
						courseService.findByUserActive(username));
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				m.addAttribute("sendAlertsToParents", true);
			} else {
				m.addAttribute("sendAlertsToParents", false);
			}

			m.addAttribute("assignment", assignment);
			logger.info("createAssignmentFromMenuForModule--->"+assignment);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			return "assignment/createAssignmentForModule";
		}

		return "assignment/createAssignmentForModule";
	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(value = "/getAcadYearByModuleId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAcadYearByModuleId(
			@RequestParam(name = "moduleId") String moduleId,Principal p) {
		String json = "";
		String username = p.getName();
		Course course = new Course();
		logger.info("moduleId-->" + moduleId);

		List<Course> courseList = courseService.getAcadYearByModuleId(moduleId,username);

		logger.info("courseList--->" + courseList);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		for (Course c : courseList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(c.getAcadYear()), c.getAcadYear());
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		logger.info("json" + json);
		return json;

	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/deleteAssignment", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView deleteAssignmentForm(HttpServletRequest request,
			@ModelAttribute Assignment assignment, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {

		int AssignmentId = Integer.parseInt(request.getParameter("id"));
		assignmentService.softDeleteAssignment(AssignmentId);
		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(Long.valueOf(AssignmentId));
		if(null != childAssignmentId){
			for(Long c: childAssignmentId){
				assignmentService.softDeleteAssignmentForModule(c);
			}
		}
		setSuccess(redirectAttrs, "Assignement deleted successfully!");
		if(null != childAssignmentId){
			return new ModelAndView("redirect:/searchFacultyAssignmentForModule");
		}
		return new ModelAndView("redirect:/searchFacultyAssignment");
	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/showResultsToStudentsAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String showResultsToStudentsAssignment(
			@RequestParam("id") Long id, Model m, Principal principal) {

		String username = principal.getName();
		try {
			Assignment test = assignmentService.findByID(id);
			List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(id);
			if(null != childAssignmentId){
				for(Long c: childAssignmentId){
					Assignment test1 = assignmentService.findByID(c);
					test1.setShowResultsToStudents("Y");
					
					assignmentService.showResults(test1.getId());
					
				}
			}
			
			test.setShowResultsToStudents("Y");
			
			assignmentService.showResults(test.getId());
			setSuccess(m, "Results will be shown to students!");
			return "Success";
		} catch (Exception e) {

			logger.error("Error, " + e);
			return "Error";
		}

	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/saveStudentAssignmentAllocationForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentAssignmentAllocationForModule(
			@ModelAttribute Assignment assignment, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		String subject = " Assigment with name  ";
		Assignment retrived = assignmentService.findByID(assignment.getId());
		logger.info("Assignment Id----->"+assignment.getId());
		Course course = retrived.getModuleId() != null ? courseService
				.findByModuleIdAndAcadYear(String.valueOf(retrived.getModuleId()),String.valueOf(retrived.getAcadYear())) : null;
		StringBuffer buff = new StringBuffer(subject);
		buff.append(retrived.getAssignmentName());
		if (course != null) {
			buff.append("for Module ");
			buff.append(course.getModuleName());
		}
		buff.append(" allocated to you");
		subject = buff.toString();
		ArrayList<StudentAssignment> studentAssignmentMappingList = new ArrayList<StudentAssignment>();
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		try {
			if (assignment.getStudents() != null
					&& assignment.getStudents().size() > 0) {
				studentList.add(username);
				for (String studentUsername : assignment.getStudents()) {
					StudentAssignment bean = new StudentAssignment();
					logger.info("studentUsername------->"+studentUsername);
					String[] sUsername = null;
					if(studentUsername.contains("_")){
						sUsername = studentUsername.split("_");
						bean.setUsername(sUsername[0]);
						bean.setCourseId(Long.valueOf(sUsername[1]));
					}
					bean.setAcadMonth(retrived.getAcadMonth());
					bean.setAcadYear(retrived.getAcadYear());
					Assignment childAssignmentId = new Assignment();
					childAssignmentId = assignmentService.findIdByCourseIdAndParentModuleId(sUsername[1],String.valueOf(assignment.getId()));
					bean.setAssignmentId(childAssignmentId.getId());
					
					
					bean.setEvaluatedBy(assignment.getFacultyId());
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					bean.setStartDate(retrived.getStartDate());
					/* bean.setDueDate(retrived.getDueDate()); */
					bean.setEndDate(retrived.getEndDate());
					bean.setActive("Y");

					studentList.add(studentUsername);

					if (bean.getUsername().equals(studentUsername)) {

					} else {

					}
					studentAssignmentMappingList.add(bean);

				}

				studentAssignmentService
						.insertBatch(studentAssignmentMappingList);
				try {
					Map<String, Map<String, String>> result = null;
					if (!studentList.isEmpty()) {
						if ("Y".equals(retrived.getSendEmailAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}

						if ("Y".equals(retrived.getSendSmsAlertToParents())) {
							for (String s : studentList) {

								StudentParent sp = studentParentService
										.findParentByStudent(s);
								if (sp != null) {
									parentList.add(sp.getParent_username());
								}
							}
							if (parentList.size() != 0 || !parentList.isEmpty()) {
								studentList.addAll(parentList);
							}

						}
						if ("Y".equals(retrived.getSendEmailAlert())) {

							for(String s: studentList) {
								
								List<String> singleStudList = new ArrayList<>();
								singleStudList.add(s);
								result = userService
										.findEmailByUserName(singleStudList);
								Map<String, String> email = result.get("emails");
								Map<String, String> mobiles = new HashMap();
								notifier.sendEmail(email, mobiles, subject, subject);
							}
						}

						if ("Y".equals(retrived.getSendSmsAlert())) {

							if (result != null)
								result = userService
										.findEmailByUserName(studentList);
							Map<String, String> email = new HashMap();
							Map<String, String> mobiles = result.get("mobiles");
							notifier.sendEmail(email, mobiles, subject, subject);
						}
					}
				} catch (Exception e) {
					logger.error("Exception", e);
				}

				setSuccess(m, "Assignment allocated to "
						+ assignment.getStudents().size()
						+ " students successfully");

				return viewAssignmentForModule(assignment.getId(), m, null, principal);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "redirect:/viewAssignmentForModule?id=" + assignment.getId();
		}
		m.addAttribute("assignment", assignment);
		return "redirect:/viewAssignmentForModule?id=" + assignment.getId();
	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
	@RequestMapping(value = "/updateAssignmentForModule", method = {
			RequestMethod.GET,

			RequestMethod.POST })
	public String updateAssignmentForModule(
			@ModelAttribute Assignment assignment,

			@RequestParam("file") List<MultipartFile> files, Model m,

			Principal principal,RedirectAttributes redirectAttributes) {

		m.addAttribute("webPage", new WebPage("assignment",

		"Create Assignment", true, false));

		logger.info("Assignment------------>"+ assignment.getModuleId() +"\n---->"+ assignment);
		String errorMessage = null;

		try {

			String username = principal.getName();

			Token userdetails1 = (Token) principal;

			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();
			String acadYear= u.getAcadYear();
		

			m.addAttribute("Program_Name", ProgramName);

			m.addAttribute("AcadSession", acadSession);
			
			m.addAttribute(

			"allCourses",

			courseService.findByUserActive(username,

			userdetails1.getProgramName()));
			
			/* New Audit changes start */
			HtmlValidation.validateHtml(assignment,Arrays.asList("assignmentText"));
			Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
			BusinessBypassRule.validateNumeric(assignment.getMaxScore());
			BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
			validateAssignmentType(assignment.getAssignmentType());
			Course course = courseService.findByID(assignment.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			if(!assignment.getPlagscanRequired().equals("Yes") && !assignment.getPlagscanRequired().equals("No")) {
				throw new ValidationException("Invalid Input.");
			}
//			BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
			BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
			BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
			BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
			BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
			BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());
//			if(Double.valueOf(assignment.getMaxScore()) < 0.0) {
//				setError(m, "Invalid total score");
//				return "assignment/createAssignmentForModule";
//			}
			/* New Audit changes end */
			// Upload new assignment file, if user selected one.
			Assignment retrived = assignmentService.findByID(assignment.getId());
			logger.info("Old File---->"+retrived.getFilePath());
			String oldFile = retrived.getFilePath();
			for (MultipartFile file : files) {
				if (file != null && !file.isEmpty()) {
					//Audit change start
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(m, "File uploaded is invalid!");
							return "assignment/createAssignmentForModule";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
									|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(m, "File uploaded is invalid!");
								return "assignment/createAssignmentForModule";
							}else {
								byte [] byteArr=file.getBytes();
								if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																	(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																	("text/plain").equals(detectedType)) {
								errorMessage = uploadAssignmentFileForS3(assignment, file);
								logger.info("New File---->" + assignment.getFilePath());
								} else {
									setError(m, "File uploaded is invalid!");
									return "assignment/createAssignmentForModule";
								}
							}
						}
					}else {
						setError(m, "File uploaded is invalid!");
						return "assignment/createAssignmentForModule";
					}
					//Audit change end
				} else {
					assignment.setFilePath(retrived.getFilePath());
					assignment.setFilePreviewPath(retrived.getFilePreviewPath());
				}
			} // If User has not added a new file then take the path from

			// existing assignment to use generic update query

//			Course course = retrived.getCourseId() != null ? courseService.findByID(Long.valueOf(retrived.getCourseId())) : null;
			String subject = " Assigment with name  ";

			StringBuffer buff = new StringBuffer(subject);
			buff.append(retrived.getAssignmentName());
			if (course != null) {
				buff.append(" for Course ");
				buff.append(course.getCourseName());
			}
			buff.append(" is updated on" + retrived.getLastModifiedDate());
			subject = buff.toString();

			assignment.setLastModifiedBy(username);
			logger.info("Allocate---->"+assignment.getAllocateToStudents());

			assignmentService.update(assignment);
			List<Assignment> childList = new ArrayList<>();
			childList = assignmentService.getChildListByParentModuleId(assignment.getId());
			logger.info("ChildList----->"+childList);
			for(Assignment a:childList){
				logger.info("ChildList ID----->"+a.getId());
				Assignment childAssignment = new Assignment();
				childAssignment.setAcadYear(assignment.getAcadYear());
				childAssignment.setAcadMonth(assignment.getAcadMonth());
				childAssignment.setCourseId(a.getCourseId());
				childAssignment.setModuleId(a.getModuleId());
				childAssignment.setId(a.getId());
				childAssignment.setStartDate(assignment.getStartDate());
				childAssignment.setEndDate(assignment.getEndDate());
				//childAssignment.setCreatedBy(assignment.getCreatedBy());
				childAssignment.setLastModifiedBy(assignment.getLastModifiedBy());
				childAssignment.setFilePath(assignment.getFilePath());
				childAssignment.setFilePreviewPath(assignment.getFilePreviewPath());
				childAssignment.setAssignmentType(assignment.getAssignmentType());
				childAssignment.setShowResultsToStudents(assignment.getShowResultsToStudents());
				childAssignment.setGroupId(assignment.getGroupId());
				childAssignment.setAllocateToStudents(assignment.getAllocateToStudents());
				//childAssignment.setFacultyId(assignment.getFacultyId());
				childAssignment.setRightGrant(assignment.getRightGrant());
				childAssignment.setAllowAfterEndDate(assignment.getAllowAfterEndDate());
				childAssignment.setSendEmailAlert(assignment.getSendEmailAlert());
				childAssignment.setSendSmsAlert(assignment.getSendSmsAlert());
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					childAssignment.setSendEmailAlertToParents("Y");
					childAssignment.setSendSmsAlertToParents("Y");
				}
				childAssignment.setAssignmentName(assignment.getAssignmentName());
				childAssignment.setMaxScore(assignment.getMaxScore());
				childAssignment.setAssignmentText(assignment.getAssignmentText());
				childAssignment.setThreshold(assignment.getThreshold());
				childAssignment.setPlagscanRequired(assignment.getPlagscanRequired());
				childAssignment.setRunPlagiarism(assignment.getRunPlagiarism());
				childAssignment.setSubmitByOneInGroup(assignment.getSubmitByOneInGroup());
				//childAssignment.setParentModuleId(String.valueOf(parentModuleId));
				
				assignmentService.update(childAssignment);
			}
			if ("Y".equals(assignment.getAllocateToStudents())) {
				logger.info("Allocate1---->"+assignment.getAllocateToStudents());
				allocationToAllStudentsForModule(principal, assignment);
			}
			

			retrived = assignmentService

			.findByID(assignment.getId());

			assignment.setCourse(courseService.findByID(assignment

			.getCourseId()));

			setSuccess(m, "Assigment updated successfully");
			
			

		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignment.getId());
		List<String> courseIdList = new ArrayList<String>();
		for(Long l:childAssignmentId)
		{
			Assignment a = assignmentService.findByID(l);
			courseIdList.add(String.valueOf(a.getCourseId()));
		}

			
			List<StudentAssignment> students = studentAssignmentService
					.getStudentsForAssignmentModule(childAssignmentId,
							courseIdList,String.valueOf(assignment.getAcadYear()));

			

			List<String> studentList = new ArrayList<>();
			studentList.add(username);

			for (StudentAssignment sa : students) {

				studentList.add(sa.getUsername());

			}

			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated",studentAssignmentService.getNoOfStudentsAllocated(childAssignmentId).getCount());
			if(null != oldFile) {
				File f = new File(oldFile);
				logger.info("Old File---->"+oldFile);
				logger.info("New File---->"+assignment.getFilePath());
				if(!assignment.getFilePath().equals(oldFile)){
				if(f.exists())
					f.delete();
				}
			}
			
			try {

				if (students != null

				&& students.size() > 0) {

					try {

						Map<String, Map<String, String>> result = null;

						if (!students.isEmpty()) {

							if ("Y".equals(retrived.getSendEmailAlert())) {

								for(String s: studentList) {
									
									List<String> singleStudList = new ArrayList<>();
									singleStudList.add(s);
									result = userService
											.findEmailByUserName(singleStudList);
									Map<String, String> email = result.get("emails");
									Map<String, String> mobiles = new HashMap();
									notifier.sendEmail(email, mobiles, subject, subject);
								}

							}

							if ("Y".equals(retrived.getSendSmsAlert())) {

								result = userService

								.findEmailByUserName(studentList);

								Map<String, String> email = new HashMap();

								Map<String, String> mobiles = result
										.get("mobiles");

								notifier.sendEmail(email, mobiles, subject,
										subject);

							}

						}

					} catch (Exception e) {

						logger.error("Exception", e);

					}

					setSuccess(m, "Assignment allocated to "

					+ studentAssignmentService.getNoOfStudentsAllocated(childAssignmentId).getCount()

					+ " students successfully");

					return viewAssignmentForModule(assignment.getId(), m, null,
							principal);

				}

			} catch (Exception e) {

				logger.error(e);

			}

		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttributes, ve.getMessage());
			return "redirect:/createAssignmentForModule";
		} catch (Exception e) {

			logger.error(e.getMessage(), e);

			setError(m, "Error in updating assignment");

			m.addAttribute("webPage", new WebPage("assignment","Create Assignment", false, false));

			return "assignment/createAssignmentForModule";

		}

		//return "assignment/viewAssignmentForModule";
		redirectAttributes.addAttribute("id", assignment.getId());
		return "redirect:/viewAssignmentForModule";

	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(value = "/downloadAllFileForModule", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void downloadAllFileForModule(HttpServletRequest request,
			HttpServletResponse response, Long assignmentId)
			throws ServletException, IOException {

		// Set the content type based to zip
		response.setContentType("Content-type: text/zip");
		Assignment assignment = assignmentService.findByID(assignmentId);
		String filename = assignment.getAssignmentName().replace(",", " ") + ".zip";
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename + "");

		// List of files to be downloaded
		List<File> files = new ArrayList();
		List<String> username = new ArrayList<String>();

		// Getting students' usernames list
		
		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignmentId);
		List<StudentAssignment> assignmentList = studentAssignmentService
				.getStudentFilesForModule(childAssignmentId);
//		for (StudentAssignment sa : assignmentList) {
//
//			files.add(new File(sa.getStudentFilePath()));
//			username.add(sa.getUsername());
//
//		}

		File folderPath = new File(downloadAllFolder + File.separator+ RandomStringUtils.randomAlphanumeric(10));
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		for (StudentAssignment sa : assignmentList) {
			String file = sa.getStudentFilePath();
			if(file.startsWith("/")) {
				file = StringUtils.substring(file, 1);
			}
			file = file.replace("/\\", "/");
			file = file.replace("\\\\","/");
			file = file.replace("\\","/");
			file = file.replace("//","/");
			File fileNew = new File(file);
			logger.info("asignQuesFile---->"+file);
			InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
			String ext1 = FilenameUtils.getExtension(fileNew.getName());
			File dest = new File(folderPath.getAbsolutePath() + File.separator + sa.getUsername() + "." + ext1);
			FileUtils.copyInputStreamToFile(inpStream, dest);

		}
		ServletOutputStream out = response.getOutputStream();

		pack(folderPath.getAbsolutePath(), out);
		FileUtils.deleteDirectory(folderPath);

	}

	@Secured({ "ROLE_FACULTY", "ROLE_ADMIN", "ROLE_STUDENT" })
	@RequestMapping(value = "/viewThisAssignmentForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewThisAssignmentForModule(Model m,
			@ModelAttribute StudentAssignment assignment, Principal principal,
			@RequestParam Long assignmentId) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Search Assignment Test", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<Long> childAssignmentId = assignmentService.getIdByParentModuleId(assignmentId);
		List<StudentAssignment> assignmentlist = studentAssignmentService
				.findOneAssignment(childAssignmentId);
		for (StudentAssignment sa : assignmentlist) {
			Course c = courseService.findByID(sa.getCourseId());
			sa.setCourseName(c.getCourseName());
			String studentFilePath = StringUtils.trimToNull(sa
					.getStudentFilePath());
			if (studentFilePath != null) {
				sa.setShowStudentFileDownload("true");
			} else {
				sa.setShowStudentFileDownload("false");
			}
			Assignment a = assignmentService.findByID(sa.getAssignmentId());
			String filePath = StringUtils.trimToNull(a.getFilePath());
			if (filePath != null) {
				sa.setShowFileDownload("true");
			} else {
				sa.setShowFileDownload("fasle");
			}
			assignmentlist.set(assignmentlist.indexOf(sa), sa);
		}

		m.addAttribute("assignmentlist", assignmentlist);
		return "assignment/viewAssignment";
	}
	
	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/searchFacultyAssignmentForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchFacultyAssignmentForModule(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m,
			RedirectAttributes redirectAttributes,
			@ModelAttribute Assignment assignment,
			Principal principal,
			@RequestParam(name = "moduleId", required = false, defaultValue = "") String moduleId,
			HttpServletRequest request) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		m.addAttribute("moduleId",assignment.getModuleId());
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("searchCourse",
				"Search Assignments", false, false));
		Page<Assignment> page = new Page<Assignment>(pageNo);
		List<Assignment> assignmentList = new ArrayList<Assignment>();

		try {

			if (moduleId == null || moduleId.isEmpty()) {
				if (request.getSession().getAttribute("courseRecord") == null
						|| request.getSession().getAttribute("courseRecord")
								.equals("")) {

				} else {
					request.getSession().removeAttribute("courseRecord");
				}

			}

			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				assignment.setFacultyId(principal.getName());
			}
			if (moduleId == null || moduleId.isEmpty()) {

				if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {

					assignmentList = assignmentService
							.searchAssignmentsForFacultyByModule(username,
									Long.parseLong(userdetails1.getProgramId()));

					for (Assignment a : assignmentList) {
						if (a.getFilePath() != null) {
							a.setShowFileDownload("true");
						} else {
							a.setShowFileDownload("false");
						}
					}

				} else if (userDetails.getAuthorities().contains(Role.ROLE_HOD)) {

					assignmentList = assignmentService
							.searchAssignmentForHOD(Long.parseLong(userdetails1
									.getProgramId()));

					for (Assignment a : assignmentList) {
						if (a.getFilePath() != null) {
							a.setShowFileDownload("true");
						} else {
							a.setShowFileDownload("false");
						}
					}

				} else {

				}

			} else {
				assignmentList = assignmentService.searchAssignmentsForFacultyByModule(
						username, Long.parseLong(userdetails1.getProgramId()),
						Long.valueOf(moduleId));
				for (Assignment a : assignmentList) {
					if (a.getFilePath() != null) {
						a.setShowFileDownload("true");
					} else {
						a.setShowFileDownload("false");
					}
				}
			}
			for (Assignment a : assignmentList) {
				String moduleId1 = String.valueOf(a.getModuleId());
				Course course = courseService.findByModuleIdAndAcadYear(moduleId1,String.valueOf(a.getAcadYear()));
				String moduleName = course.getModuleName();

				a.setCourseName(moduleName);
				assignmentList.set(assignmentList.indexOf(a), a);
				m.addAttribute("courseName", moduleName);
			}
			List<Course> moduleList = courseService.findModulesByUsername(username,
					Long.parseLong(userdetails1.getProgramId()));
			m.addAttribute("allModules", moduleList);
			//m.addAttribute("allModules", moduleList);
			m.addAttribute("page", page);
			m.addAttribute("assignmentList", assignmentList);
			m.addAttribute("q", getQueryString(assignment));

			if (assignmentList == null || assignmentList.size() == 0) {
				setNote(m, "No Assignments found");
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(m, "Error in getting Assignments List");
		}
		return "assignment/facultyAssignmentListForModule";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/createAssignmentByAdmin", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String createAssignmentByAdmin(
			@RequestParam(required = false) Long id, Principal principal,
			@RequestParam(required = false) Long courseId, Model m,
			HttpServletRequest request) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();
		try {
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			
			logger.info("ProgramName----->" + ProgramName);
			logger.info("Username----->" + username);
			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Assignment assignment = new Assignment();
			if (courseId != null) {
				assignment.setCourse(courseService.findByID(courseId));
				m.addAttribute("courseId", courseId);
				m.addAttribute("showCourseHeader", true);
			} else {
				m.addAttribute("showCourseHeader", false);
			}

			if (request.getSession().getAttribute("courseRecord") == null
					|| "".equals(request.getSession().getAttribute(
							"courseRecord"))) {

			} else {
				assignment.setCourse(courseService.findByID(courseId));
				assignment.setIdForCourse(String.valueOf(courseId));
				m.addAttribute("idForCourse", assignment.getIdForCourse());
				request.getSession().removeAttribute("courseRecord");
			}
			
			/* For Assignment Pool Start */
			m.addAttribute("assignmentStarted", false);
			/* For Assignment Pool End */
			
			// String username = principal.getName();
			if (id != null) {

				assignment = assignmentService.findByID(id);
				assignment.setCourse(courseService.findByID(assignment
						.getCourseId()));
				m.addAttribute("edit", "true");
				/* For Assignment Pool Start */
				Date currDate = Utils.getInIST();
				Date startDate = Utils.converFormatsDateAlt(assignment.getStartDate());
				if (currDate.after(startDate)) {
					m.addAttribute("assignmentStarted", true);
				} else {
					m.addAttribute("assignmentStarted", false);
				}
				/* For Assignment Pool End */
			}
			if (!"INTDR".equals(userdetails1.getProgramName())) {
				m.addAttribute(
						"allCourses",
						courseService.findByAdminActive(
								userdetails1.getProgramName()));
			} else {
				m.addAttribute("allCourses",
						courseService.findByAdminActive());
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				m.addAttribute("sendAlertsToParents", true);
			} else {
				m.addAttribute("sendAlertsToParents", false);
			}

			m.addAttribute("assignment", assignment);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			return "assignment/createAssignmentForAdmin";
		}

		return "assignment/createAssignmentForAdmin";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addAssignmentByAdmin", method = { RequestMethod.POST })
	public String addAssignmentByAdmin(@ModelAttribute Assignment assignment,
			@RequestParam(required = false) Long myId,
			@RequestParam("file") List<MultipartFile> files, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Create Assignment", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate())) {
//				setError(redirectAttributes, "Invalid Start date and End date");
//				return "redirect:/createAssignmentByAdmin";
//			}
//			if(Double.valueOf(assignment.getMaxScore()) < 0.0) {
//				setError(redirectAttributes, "Invalid total score");
//				return "redirect:/createAssignmentByAdmin";
//			}
			HtmlValidation.validateHtml(assignment,Arrays.asList("assignmentText"));
			Utils.validateStartAndEndDates(assignment.getStartDate(), assignment.getEndDate());
			BusinessBypassRule.validateNumeric(assignment.getMaxScore());
			BusinessBypassRule.validateAlphaNumeric(assignment.getAssignmentName());
			validateAssignmentType(assignment.getAssignmentType());
			Course course = courseService.findByID(assignment.getCourseId());
			if(null == course) {
				throw new ValidationException("Invalid Course selected.");
			}
			if(!assignment.getPlagscanRequired().equals("Yes") && !assignment.getPlagscanRequired().equals("No")) {
				throw new ValidationException("Invalid Input.");
			}
//			BusinessBypassRule.validateYesOrNo(assignment.getPlagscanRequired());
			BusinessBypassRule.validateYesOrNo(assignment.getAllowAfterEndDate());
			BusinessBypassRule.validateYesOrNo(assignment.getShowResultsToStudents());
			BusinessBypassRule.validateYesOrNo(assignment.getRightGrant());
			BusinessBypassRule.validateYesOrNo(assignment.getSendEmailAlert());
			BusinessBypassRule.validateYesOrNo(assignment.getSendSmsAlert());
			/* New Audit changes end */
			if (assignment.getId() != null) {
				assignmentService.update(assignment);
			} else {
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						//Audit change start
						Tika tika = new Tika();
						  String detectedType = tika.detect(file.getBytes());
						if (file.getOriginalFilename().contains(".")) {
							Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
							logger.info("length--->"+count);
							if (count > 1 || count == 0) {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/createAssignmentByAdmin";
							}else {
								String extension = FilenameUtils.getExtension(file.getOriginalFilename());
								logger.info("extension--->"+extension);
								if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
										|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
									setError(redirectAttributes, "File uploaded is invalid!");
									return "redirect:/createAssignmentByAdmin";
								}else {
									byte [] byteArr=file.getBytes();
									if((Byte.toUnsignedInt(byteArr[0]) == 0xFF && Byte.toUnsignedInt(byteArr[1]) == 0xD8) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x89 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x25 && Byte.toUnsignedInt(byteArr[1]) == 0x50) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x42 && Byte.toUnsignedInt(byteArr[1]) == 0x4D) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x47 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x49 && Byte.toUnsignedInt(byteArr[1]) == 0x49) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x38 && Byte.toUnsignedInt(byteArr[1]) == 0x42) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x1F && Byte.toUnsignedInt(byteArr[1]) == 0x8B) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x75 && Byte.toUnsignedInt(byteArr[1]) == 0x73) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x52 && Byte.toUnsignedInt(byteArr[1]) == 0x61) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0xD0 && Byte.toUnsignedInt(byteArr[1]) == 0xCF) || 
																		(Byte.toUnsignedInt(byteArr[0]) == 0x50 && Byte.toUnsignedInt(byteArr[1]) == 0x4B) || 
																		("text/plain").equals(detectedType)) {
									String errorMessage = uploadAssignmentFileForS3(assignment, file);
									} else {
										setError(redirectAttributes, "File uploaded is invalid!");
										return "redirect:/createAssignmentByAdmin";
									}
								}
							}
						}else {
							setError(redirectAttributes, "File uploaded is invalid!");
							return "redirect:/createAssignmentByAdmin";
						}
						//Audit change end
					}
				}
				// if (errorMessage == null) {
				
				
				Long courseId = null;
				
				logger.info("idOfCourse------->" + assignment.getIdForCourse());
				logger.info("CourseId------->" + assignment.getCourseId());
				if (assignment.getIdForCourse() == null) {
					courseId = assignment.getCourseId();
				} else {
					courseId = Long.valueOf(assignment.getIdForCourse());
					assignment.setCourseId(courseId);
				}
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					assignment.setSendEmailAlertToParents("Y");
					assignment.setSendSmsAlertToParents("Y");
				}
				Course c = courseService.findByID(courseId);

				assignment.setAcadMonth(c.getAcadMonth());
				assignment.setAcadYear(Integer.valueOf(c.getAcadYear()));

				String facultyId = userCourseService.getFacultyByCourseIdForAssignment(courseId);
				
				assignment.setCreatedByAdmin("Y");
				assignment.setCreatedBy(facultyId);
				assignment.setLastModifiedBy(username);
				assignment.setFacultyId(facultyId);// Assignment can be
													// created by
													// Faculty
													// only.
				assignmentService.insertWithIdReturn(assignment);
				assignment.setCourse(courseService.findByID(courseId));

				setSuccess(redirectAttributes,
						"Assignment uploaded successfully");

				List<StudentAssignment> students = studentAssignmentService
						.getStudentsForAssignment(assignment.getId(),
								assignment.getCourseId());
				m.addAttribute("students", students);
				m.addAttribute("assignment", assignment);
				myId = assignment.getAssignedId();
				assignment.setAssignedId(assignment.getId());

				redirectAttributes.addAttribute("id",
						assignment.getAssignedId());
				m.addAttribute("id", assignment.getAssignedId());
				/*
				 * } else { setError(m, errorMessage); } }
				 */
			}
		}catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttributes, ve.getMessage());
			return "redirect:/createAssignmentByAdmin";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in creating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "redirect:/createAssignmentByAdmin";
		}
		return "redirect:/viewAssignment";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/searchAdminAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAdminAssignment(
			Model m,
			RedirectAttributes redirectAttributes,
			@ModelAttribute Assignment assignment,
			Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("searchCourse",
				"Search Assignments", false, false));
		List<Assignment> assignmentList = new ArrayList<Assignment>();

		try {

			if (courseId == null || courseId.isEmpty()) {
				if (request.getSession().getAttribute("courseRecord") == null
						|| request.getSession().getAttribute("courseRecord")
								.equals("")) {

				} else {
					request.getSession().removeAttribute("courseRecord");
				}

			}

			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
			
			if (courseId == null || courseId.isEmpty()) {

				if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

					assignmentList = assignmentService
							.searchAssignmentsForAdmin(
									Long.parseLong(userdetails1.getProgramId()));

					for (Assignment a : assignmentList) {
						if (a.getFilePath() != null) {
							a.setShowFileDownload("true");
						} else {
							a.setShowFileDownload("false");
						}
					}

				}

			} else {
				assignmentList = assignmentService.searchAssignmentsForAdmin(
						Long.parseLong(userdetails1.getProgramId()),
						Long.valueOf(courseId));
				for (Assignment a : assignmentList) {
					if (a.getFilePath() != null) {
						a.setShowFileDownload("true");
					} else {
						a.setShowFileDownload("false");
					}
				}
			}

			for (Assignment a : assignmentList) {
				Long cId = a.getCourseId();
				Course course = courseService.findByID(cId);
				String courseName = course.getCourseName();

				a.setCourseName(courseName);
				assignmentList.set(assignmentList.indexOf(a), a);
				m.addAttribute("courseName", courseName);
			}
			m.addAttribute("allCourses", courseService.findByAdminActive(
					userdetails1.getProgramName()));
			m.addAttribute("assignmentList", assignmentList);

			if (assignmentList == null || assignmentList.size() == 0) {
				setNote(m, "No Assignments found");
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(m, "Error in getting Assignments List");
		}
		return "assignment/adminAssignmentList";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/exportAssignmentFormForAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String exportAssignmentFormForAdmin(@RequestParam Long id, Model m,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("viewAssignment", "Assignment",
				true, false));
		Assignment assignment = new Assignment();
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		List<Program> programList = programService.findAllActive();
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("id", id);

		if (id != null) {
			assignment = assignmentService.findByID(id);
			assignment.setCourse(courseService.findByID(assignment
					.getCourseId()));
			String filePath = StringUtils.trimToNull(assignment.getFilePath());
			if (filePath != null) {
				assignment.setShowFileDownload("true");
			} else {
				assignment.setShowFileDownload("false");
			}

		}
		m.addAttribute("assignment", assignment);
		m.addAttribute("programList", programList);
		return "assignment/assignmentForAdmin";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/exportAssignmentForAdmin", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String exportAssignmentForAdmin(@ModelAttribute Assignment assignment, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		String username = principal.getName();
		List<String> courseList = new ArrayList<>();
		if (assignment.getCourseIdToExport().contains(",")) {
			courseList = Arrays.asList(assignment.getCourseIdToExport().split(","));
		}else {
			courseList.add(assignment.getCourseIdToExport());
		}
		logger.info("courseListSize---->"+courseList.size());
		assignment = assignmentService.findByID(assignment.getId());
		
		try {
			int count = 0;
			for(String course : courseList){
				Course c = courseService.findByID(Long.valueOf(course));
				List<UserCourse> ucStudentList = userCourseService.findStudentsByYearMonthCourseNew(c.getAcadYear(),
						c.getAcadMonth(), String.valueOf(c.getId()));
				List<UserCourse> ucFacultyList = userCourseService.findFacultiesByYearMonthCourseNew(c.getAcadYear(),
						c.getAcadMonth(), String.valueOf(c.getId()));
				Assignment a = new Assignment();
				a.setAcadYear(Integer.valueOf(c.getAcadYear()));
				a.setAcadMonth(c.getAcadMonth());
				a.setCourseId(c.getId());
				a.setStartDate(assignment.getStartDate());
				a.setEndDate(assignment.getEndDate());
				a.setIsCheckSumReq(assignment.getIsCheckSumReq());
				if(ucFacultyList.size() > 0) {
				a.setCreatedBy(ucFacultyList.get(0).getUsername());
				}
				a.setLastModifiedBy(username);
				a.setCreatedByAdmin("Y");
				a.setFilePath(assignment.getFilePath());
				a.setFilePreviewPath(assignment.getFilePreviewPath());
				a.setAssignmentType(assignment.getAssignmentType());
				//a.setAllocateToStudents("Y");
				a.setShowResultsToStudents(assignment.getShowResultsToStudents());
				a.setGroupId(assignment.getGroupId());
				a.setRightGrant(assignment.getRightGrant());
				a.setActive("Y");
				if(ucFacultyList.size() > 0) {
					a.setFacultyId(ucFacultyList.get(0).getUsername());
				}
				a.setAllowAfterEndDate(assignment.getAllowAfterEndDate());
				a.setSendEmailAlert(assignment.getSendEmailAlert());
				a.setSendEmailAlertToParents(assignment.getSendEmailAlertToParents());
				a.setSendSmsAlertToParents(assignment.getSendSmsAlertToParents());
				a.setSendSmsAlert(assignment.getSendSmsAlert());
				a.setAssignmentName(assignment.getAssignmentName());
				a.setMaxScore(assignment.getMaxScore());
				a.setAssignmentText(assignment.getAssignmentText());
				a.setThreshold(assignment.getThreshold());
				a.setPlagscanRequired(assignment.getPlagscanRequired());
				a.setRunPlagiarism(assignment.getRunPlagiarism());
				a.setSubmitByOneInGroup("N");
				if(ucFacultyList.size() > 0) {
				long assignId = assignmentService.insertWithIdReturn(a);
				logger.info("assignIdLast--->"+a.getId());
				List<String> studentList = new ArrayList<>();	
				List<String> parentList = new ArrayList<String>();
				List<StudentAssignment> saList = new ArrayList<>();
				for(UserCourse uc : ucStudentList) {
					StudentAssignment sa = new StudentAssignment();
					sa.setAcadYear(uc.getAcadYear());
					sa.setAcadMonth(uc.getAcadMonth());
					sa.setActive("Y");
					sa.setUsername(uc.getUsername());
					sa.setCourseId(c.getId());
					sa.setStartDate(a.getStartDate());
					sa.setEndDate(a.getEndDate());
					sa.setCreatedBy(a.getCreatedBy());
					sa.setLastModifiedBy(a.getLastModifiedBy());
					sa.setAssignmentId(a.getId());
					saList.add(sa);
					studentList.add(uc.getUsername());
				}
				studentAssignmentService.insertBatch(saList);
				
				List<AssignmentConfiguration> ac = assignmentConfigurationService.findAllByAssignmentId(assignment.getId());
                List<AssignmentConfiguration> newAc = new ArrayList<>();
                if (ac.size() > 0) {
                    for (AssignmentConfiguration newAcc : ac) {
                        //AssignmentConfiguration acon = newAcc;
                        newAcc.setAssignmentId(a.getId());
                        newAc.add(newAcc);
                    }
                    assignmentConfigurationService.insertBatch(newAc);
                }
				
				String subject = " Assigment with name  ";
				StringBuffer buff = new StringBuffer(subject);
				buff.append(a.getAssignmentName());
				if (c != null) {
					buff.append("for Course ");
					buff.append(c.getCourseName());
				}
				buff.append(" allocated to you");
				subject = buff.toString();
				
				try {
							Map<String, Map<String, String>> result = null;
							if (!studentList.isEmpty()) {
								if ("Y".equals(a.getSendEmailAlertToParents())) {
									for (String s : studentList) {

										StudentParent sp = studentParentService
												.findParentByStudent(s);
										if (sp != null) {
											parentList.add(sp.getParent_username());
										}
									}
									if (parentList.size() != 0 || !parentList.isEmpty()) {
										studentList.addAll(parentList);
									}

								}

								if ("Y".equals(a.getSendSmsAlertToParents())) {
									for (String s : studentList) {

										StudentParent sp = studentParentService
												.findParentByStudent(s);
										if (sp != null) {
											parentList.add(sp.getParent_username());
										}
									}
									if (parentList.size() != 0 || !parentList.isEmpty()) {
										studentList.addAll(parentList);
									}

								}
								if ("Y".equals(a.getSendEmailAlert())) {

									result = userService
											.findEmailByUserName(studentList);
									Map<String, String> email = result.get("emails");
									Map<String, String> mobiles = new HashMap();
									notifier.sendEmail(email, mobiles, subject, subject);
								}

								if ("Y".equals(a.getSendSmsAlert())) {

									if (result != null)
										result = userService
												.findEmailByUserName(studentList);
									Map<String, String> email = new HashMap();
									Map<String, String> mobiles = result.get("mobiles");
									notifier.sendEmail(email, mobiles, subject, subject);
								}
							}
						} catch (Exception e) {
							logger.error("Exception while sending email or sms", e);
						}
				
				}else {
					count ++;
				}
			}

		 if(count > 0){
			 setNote(redirectAttributes, "Assignment for "+count+" courses not created");
		 }
		 setSuccess(redirectAttributes, "Successfully Created Assignment!");
		} catch (Exception e) {
			logger.error("Exception while exporting assignment", e);

		}

		return "redirect:/exportAssignmentFormForAdmin?id=" + assignment.getId();
	}
	
	
	//Hiren 11-04-2020
	@Secured("ROLE_FACULTY")
	@RequestMapping(value = "/downloadAllFileForLateSubmitted", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void downloadAllFileForLateSubmitted(HttpServletRequest request,
			HttpServletResponse response, Long assignmentId)
			throws ServletException, IOException {

		// Set the content type based to zip
		response.setContentType("Content-type: text/zip");
		Assignment assignment = assignmentService.findByID(assignmentId);
		String filename = assignment.getAssignmentName().replace(",", " ") + ".zip";
		response.setHeader("Content-Disposition", "attachment; filename="
				+ filename + "");

		// List of files to be downloaded
		List<File> files = new ArrayList();
		List<String> username = new ArrayList<String>();

		// Getting students' usernames list
		List<StudentAssignment> assignmentList = studentAssignmentService
				.getStudentFilesForlateSubmitted(assignmentId);
		File folderPath = new File(downloadAllFolder + File.separator+ RandomStringUtils.randomAlphanumeric(10));
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		for (StudentAssignment sa : assignmentList) {
			String file = sa.getStudentFilePath();
			if(file.startsWith("/")) {
				file = StringUtils.substring(file, 1);
			}
			file = file.replace("/\\", "/");
			file = file.replace("\\\\","/");
			file = file.replace("\\","/");
			file = file.replace("//","/");
			File fileNew = new File(file);
			logger.info("asignQuesFile---->"+file);
			InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
			String ext1 = FilenameUtils.getExtension(fileNew.getName());
			File dest = new File(folderPath.getAbsolutePath() + File.separator + sa.getUsername() + "." + ext1);
			FileUtils.copyInputStreamToFile(inpStream, dest);

		}
		ServletOutputStream out = response.getOutputStream();


		pack(folderPath.getAbsolutePath(), out);
		FileUtils.deleteDirectory(folderPath);

	}
	
	private String uploadAssignmentFileForS3(Assignment bean, MultipartFile file) {

		String errorMessage = null;
		String filePath = null;

		try {
			filePath = assignmentFolder;
	
			Map<String,String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file, filePath);
			logger.info("map--->"+s3FileNameMap);
			if(s3FileNameMap.containsKey("SUCCESS")) {
			String s3FileName = s3FileNameMap.get("SUCCESS");
			filePath = filePath + "/" + s3FileName;
			if (bean.getFilePath() != null) {
				bean.setFilePath(bean.getFilePath() + "," + filePath);
				bean.setFilePreviewPath(bean.getFilePreviewPath() + ","
				+ filePath);
			} else {
				bean.setFilePath(filePath);
				bean.setFilePreviewPath(filePath);
			}
			}else {
				throw new Exception("Error in uploading Assignment file");
			}
		} catch (Exception e) {
			errorMessage = "Error in uploading Assignment file : "
			+ e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}
	
	@Secured({ "ROLE_FACULTY", "ROLE_STUDENT", "ROLE_ADMIN" })
	@RequestMapping(value = "/getCourseByCourseId", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByCourseId(
			@RequestParam(name = "idForCourse") String courseId,
			Principal principal) {
		String json = "";
		String username = principal.getName();
		
		logger.info("courseId------------>>>"+courseId);

		//List<Assignment> courseList =  courseService.getCoursesAssignedToFacultyByCourseId(courseId);
		List<Groups> groupList =  groupService.findByFacultyAndCourseWithGroupCourseNew(username,courseId);
		
		
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		if(groupList.size()>0){
		for (Groups ass : groupList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(ass.getId()),
					ass.getGroupName());
			res.add(returnMap);
		}
		}else {
			Map<String, String> returnMap = new HashMap();
			returnMap.put("NA","No Group Available");
			res.add(returnMap);
		}
		
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		logger.info("json" + json);
		return json;

	}
	
	//Hiren 29-08-2020
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addQuestionConfigurationForm", method = {RequestMethod.POST,RequestMethod.GET})
	public String addTestConfigurationForm(
			@RequestParam Long assignmentId,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		String username = principal.getName();
		Gson g = new Gson();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Assignment assignment = assignmentService.findByID(assignmentId);
		
		m.addAttribute("assignmentConfiguration", new AssignmentConfiguration());
		m.addAttribute("assignmentId", assignment.getId());
		m.addAttribute("edit", "Y");
		m.addAttribute("TotalScore", assignment.getMaxScore());
		
		
		
		List<AssignmentConfiguration> assignConfigList = assignmentConfigurationService.findAllByAssignmentId(assignment.getId());
		if(null != assignConfigList && assignConfigList.size() > 0){
			m.addAttribute("assignConfigList", assignConfigList);
		}
		return "assignment/configureQuestionMarksForAssignment";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addAssignmentConfiguration", method = RequestMethod.POST)
	public @ResponseBody String addAssignmentConfiguration(
			@RequestParam(required = false) Long assignmentId,
			@RequestParam(required = false) String questionConfiguration,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		String username = principal.getName();
		Gson g = new Gson();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		logger.info("assignmentId--->"+assignmentId);
		Assignment assignment = assignmentService.findByID(assignmentId);
		List<StudentAssignmentQuestionwiseMarks> saqm = studentAssignmentQuestionwiseMarksService.getStudentsMarksQuestionwise(assignment.getId());
		if(saqm.size() > 0) {
			//return "{\"msg\":\"failed\"}";
			return "{\"msg\":\"Already evaluated\"}";
		}
		try {
			
			List<AssignmentConfiguration> assignmentCongfigList = new ArrayList<>();
			assignmentConfigurationService.deleteByAssignmentId(String.valueOf(assignmentId));
			ObjectMapper mapper = new ObjectMapper();
			assignmentCongfigList = mapper.readValue(questionConfiguration, new TypeReference<List<AssignmentConfiguration>>(){});
            logger.info("assignmentCongfigList--->"+assignmentCongfigList);
			for (AssignmentConfiguration  tpc: assignmentCongfigList) {
				tpc.setCreatedBy(username);
				tpc.setLastModifiedBy(username);
				tpc.setActive("Y");
			}
			assignmentConfigurationService.insertBatch(assignmentCongfigList);
			
			return "{\"msg\":\"success\"}";

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "{\"msg\":\"failed\"}";
		}

	}
	
	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/downloadStudentAssignmentHashKeys", method = { RequestMethod.GET})
	public String downloadStudentAssignmentHashKeys(
			@RequestParam(required = false) Long assignmentId,HttpServletResponse response) throws URIException {

		
		Assignment assignmentDB = assignmentService.findByID(assignmentId);
		List<StudentAssignment> studAssgnHashKeys = studentAssignmentService.studentAssignmentHashKeys(assignmentId);

		
		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("SAPID", "Roll No.","First Name", "Last Name",
				"Hash Key", "Hash Key Generation Time","Late Submission Remarks."));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfHashKeys = new ArrayList<>();
			fileName =assignmentDB.getAssignmentName().replace(",", " ") + ".xlsx";
			fileName = fileName.replace("/", "_");

			filePath = downloadAllFolder+File.separator+ fileName;
			
			for (StudentAssignment sa : studAssgnHashKeys) {
				Map<String, Object> hashKeyMap = new HashMap<>();
				String dateOfKeyGen = Utils.formatDate("yyyy-MM-dd HH:mm:ss", sa.getKeyGenerationTime());
				logger.info(dateOfKeyGen);

				hashKeyMap.put("SAPID", sa.getUsername());
				hashKeyMap.put("Roll No.", sa.getRollNo());
				hashKeyMap.put("First Name", sa.getFirstname());
				hashKeyMap.put("Last Name", sa.getLastname());
				hashKeyMap.put("Hash Key", sa.getHashKey());
				hashKeyMap.put("Hash Key Generation Time",dateOfKeyGen);
				hashKeyMap.put("Late Submission Remarks.", sa.getLateSubmRemark()!=null && !sa.getLateSubmRemark().isEmpty()?sa.getLateSubmRemark():"NA");
				listOfHashKeys.add(hashKeyMap);
			
				
				
				
			}
			excelCreater.CreateExcelFile(listOfHashKeys, validateHeaders, filePath);
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
	
		// For Assignment Pool Start
		@Secured("ROLE_ADMIN")
		@RequestMapping(value = "/addAssignmentQuestionPoolConfigurationForm", method = { RequestMethod.POST,
				RequestMethod.GET })
		public String addAssignmentQuestionPoolConfigurationForm(@RequestParam Long assignmentId,
				RedirectAttributes redirectAttrs, Principal principal, Model m) {
			String username = principal.getName();
			Gson g = new Gson();
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Assignment assignment = assignmentService.findByID(assignmentId);

			m.addAttribute("assignmentConfiguration", new AssignmentConfiguration());
			m.addAttribute("assignmentId", assignment.getId());

			m.addAttribute("TotalScore", assignment.getMaxScore());
			m.addAttribute("maxQuestion", assignment.getMaxQuestnToShow());

			List<TestPool> testPoolsList = testPoolService.findAllTestPoolsByUserAdminForAssignment(assignment.getId(),
					username);
			if (null != testPoolsList && testPoolsList.size() > 0) {
				m.addAttribute("testPoolsList", testPoolsList);
			}
			m.addAttribute("edit", "N");
			List<AssignmentConfiguration> apcList = assignmentConfigurationService
					.getAllPoolConfigByAssignmentId(assignment.getId());
			if (null != apcList && apcList.size() > 0) {
				m.addAttribute("edit", "Y");
				m.addAttribute("apcList", apcList);
			}
			return "assignment/questionPoolConfiguration";
		}

		@Secured("ROLE_ADMIN")
		@RequestMapping(value = "/addAssignmentQuestionPoolConfiguration", method = RequestMethod.POST)
		public @ResponseBody String addAssignmentQuestionPoolConfiguration(
				@RequestParam(required = false) Long assignmentId,
				@RequestParam(required = false) String questionConfiguration, RedirectAttributes redirectAttrs,
				Principal principal, Model m) {
			String username = principal.getName();
			Gson g = new Gson();
			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			logger.info("assignmentId--->" + assignmentId);
			Assignment assignment = assignmentService.findByID(assignmentId);

			try {

				List<AssignmentConfiguration> assignmentPoolCongfigList = new ArrayList<>();
				assignmentConfigurationService.deleteByAssignmentPoolConfigurationAssignmentId(String.valueOf(assignmentId));
				//assignmentPoolCongfigList = assignmentConfigurationService.getAllPoolConfigByAssignmentId(assignmentId);
				
				List<StudentAssignment> allocatedStudentList = studentAssignmentService.getStudentsForAssignentById(assignmentId);
				for(StudentAssignment sa:allocatedStudentList) {
					studentAssignmentQuestionService.deleteAssignQuesByAssignment(sa.getId());
				}
				studentAssignmentService.deleteStudentByAssignment(assignmentId);
							
				ObjectMapper mapper = new ObjectMapper();
				assignmentPoolCongfigList = mapper.readValue(questionConfiguration,
						new TypeReference<List<AssignmentConfiguration>>() {
						});
				logger.info("assignmentCongfigList--->" + assignmentPoolCongfigList);
				Date currDate = Utils.getInIST();
				for (AssignmentConfiguration tpc : assignmentPoolCongfigList) {
					tpc.setCreatedBy(username);
					tpc.setCreatedDate(currDate);
					tpc.setLastModifiedBy(username);
					tpc.setLastModifiedDate(currDate);
					tpc.setActive("Y");
					assignmentConfigurationService.insertAssignmentPoolConfiguration(tpc);
				}
				return "{\"msg\":\"success\"}";

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return "{\"msg\":\"failed\"}";
			}

		}

		public String assignQuestionsToStudents(Assignment assignment, String createdBy) {
			try {

				long assgnId = assignment.getId();
				List<StudentAssignment> studAssgnList = studentAssignmentService
						.getStudentsForAssignent(assignment.getId());
				Map<String, List<Long>> mapperForStudentAssgnQuestn = new HashMap<>();
				// if ("Y".equals(assignment.getRandomQuestion())) {

				List<AssignmentQuestion> assgnQuestnForAssgnId = assignmentQuestionService
						.getAssgnQuestionsByAssignmentId(assgnId, "Descriptive");

				Map<String, List<AssignmentQuestion>> mapOfAssgnQuestnsPoolWise = new HashMap<>();
				List<AssignmentQuestion> testPoolIds = new ArrayList<>();
				if ("Y".equals(assignment.getRandomQuestion())) {
					testPoolIds = assignmentQuestionService.getTestPoolsByAssignmentId(assgnId);
				} else {
					testPoolIds = assignmentQuestionService.getAssgnQuestionToValidate(assgnId, "Descriptive");
				}
				Map<String, String> testPoolIdsAndNoOfQuestions = new HashMap<>();
				for (AssignmentQuestion aq : testPoolIds) {
					String testPoolId = aq.getTestPoolId();
					testPoolIdsAndNoOfQuestions.put(testPoolId, aq.getNoOfQuestion());
					List<AssignmentQuestion> assgnQuestns = assgnQuestnForAssgnId.stream()
							.filter(o -> o.getTestPoolId().equals(testPoolId)).collect(Collectors.toList());

					mapOfAssgnQuestnsPoolWise.put(testPoolId, assgnQuestns);

				}

				for (StudentAssignment sa : studAssgnList) {
					if (sa.getStudentAssignmentId() != null) {
						continue;
					}
					List<Long> questnIds = new ArrayList<>();
					for (String s : mapOfAssgnQuestnsPoolWise.keySet()) {

						List<Long> ids = new ArrayList<>();
						List<AssignmentQuestion> assgnQuestion = mapOfAssgnQuestnsPoolWise.get(s);
						logger.info("assgn question size:" + assgnQuestion.size());
						int noOfQuestn = 0;
						logger.info("random question is:" + assignment.getRandomQuestion());
						if ("Y".equals(assignment.getRandomQuestion())) {
							noOfQuestn = Integer.parseInt(testPoolIdsAndNoOfQuestions.get(s));
						} else {
							noOfQuestn = assgnQuestion.size();
						}
						logger.info("noOfQuestion is:" + noOfQuestn);

						for (int i = 0; i < noOfQuestn; i++) {
							Long singleId = 0l;
							if ("Y".equals(assignment.getRandomQuestion())) {
								Random rand = new Random();
								singleId = assgnQuestion.get(rand.nextInt(assgnQuestion.size())).getId();
							} else {
								singleId = assgnQuestion.get(i).getId();
							}

							if (ids.contains(singleId)) {
								i--;
							} else {
								questnIds.add(singleId);
								ids.add(singleId);
							}
						}
					}

					mapperForStudentAssgnQuestn.put(sa.getUsername(), questnIds);
				}
				if (mapperForStudentAssgnQuestn.isEmpty()) {
					return "Empty";
				}
				List<StudentAssignmentQuestion> studAssgnQuestnList = studAssgnQuestnList(mapperForStudentAssgnQuestn,
						studAssgnList, createdBy);

				studentAssignmentQuestionService.insertBatch(studAssgnQuestnList);

				return "Success";

			} catch (Exception ex) {
				logger.error("Exception", ex);
				return "Error";
			}
		}

		public List<StudentAssignmentQuestion> studAssgnQuestnList(Map<String, List<Long>> mapper,
				List<StudentAssignment> studentAssgnList, String createdBy) {
			List<StudentAssignmentQuestion> studAssgnQuestnList = new ArrayList<>();
			for (StudentAssignment sa : studentAssgnList) {
				String username = sa.getUsername();
				if (!mapper.containsKey(username)) {
					continue;
				}
				List<Long> questionIds = mapper.get(username);
				for (Long s : questionIds) {
					StudentAssignmentQuestion saq = new StudentAssignmentQuestion();
					saq.setUsername(username);
					saq.setStudentAssignmentId(String.valueOf(sa.getId()));
					saq.setQuestionId(String.valueOf(s));
					saq.setCreatedBy(createdBy);
					saq.setLastModifiedBy(createdBy);
					studAssgnQuestnList.add(saq);
				}
			}
			return studAssgnQuestnList;
		}
		
		@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
		@RequestMapping(value = "/viewAssignmentQuestionsByTestPoolForm", method = { RequestMethod.GET, RequestMethod.POST })
		public String viewAssignmentQuestionsByTestPoolForm(

				Model m, @ModelAttribute TestPool testPool, @RequestParam(required = false) Long assignmentId,
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
					if (assignmentId != null) {
						m.addAttribute("isAssignmentIdPresent", true);
						m.addAttribute("assignmentId", assignmentId);

						Assignment assignment = assignmentService.findByID(assignmentId);
						if ("Y".equals(assignment.getRandomQuestion())) {
							testPoolsList = testPoolService.findAllTestPoolsByAssignmentId(assignmentId.toString(), username);
						}
					}
					m.addAttribute("testPoolsList", testPoolsList);

					m.addAttribute("showQuestionByPool", false);
				}
			} catch (Exception ex) {
				logger.error("Exception", ex);
			}
			
			return "assignment/assignmentQuestionPoolList";
		}
		
		@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
		@RequestMapping(value = "/viewAssignmentQuestionsByTestPool", method = { RequestMethod.GET, RequestMethod.POST })
		public String viewAssignmentQuestionsByTestPool(

				Model m, @ModelAttribute TestPool testPool, @RequestParam(required = false) Long assignmentId,
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

					if (assignmentId != null) {
						
						Assignment assignmentDB = assignmentService.findByID(assignmentId);
						
						m.addAttribute("isAssignmentIdPresent", true);
						m.addAttribute("assignmentId", assignmentId);

						if ("Y".equals(assignmentDB.getRandomQuestion())) {
							
							testPoolsList = testPoolService.findAllTestPoolsByAssignmentId(assignmentId.toString(), username);

							m.addAttribute("randomQuestion", assignmentDB.getRandomQuestion());
							
						}

						int chkStartDateToConfigQuestn = assignmentService.chkStartDateForUpdate(assignmentId);

						if (chkStartDateToConfigQuestn == 0) {
							redirectAttrs.addAttribute("id", assignmentId);
							setError(redirectAttrs, "Cannot configure Assignment questions, Assignment has already started");
							return "redirect:/viewAssignment";
						}

						
						testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndTestTypeForAssignment(
								String.valueOf(assignmentId), testPoolId, "Descriptive");
						

						List<AssignmentQuestion> assignmentQuestionDBList = assignmentQuestionService
								.findByAssignmentId(testPool.getAssignmentId());

						Map<String, AssignmentQuestion> mapOfTestQIdAndBean = new HashMap<>();
						for (AssignmentQuestion tq : assignmentQuestionDBList) {
							if (null != tq.getTestQuestionPoolId()) {
								mapOfTestQIdAndBean.put(String.valueOf(tq.getTestQuestionPoolId()), tq);
							}
						}

						for (TestQuestionPools tqp : testQuestionPoolList) {

							if (tqp.getAssignmentQuestionId() != null) {

								tqp.setDescription(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getDescription());
								tqp.setMarks(Double.parseDouble(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getMarks()));
								tqp.setTestType("Descriptive");
								tqp.setQuestionType(mapOfTestQIdAndBean.get(String.valueOf(tqp.getId())).getQuestionType());

							}
						}
					} else {
						testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolId(testPoolId);

					}
					m.addAttribute("testPoolsList", testPoolsList);

					if (testQuestionPoolList.size() == 0) {
						setNote(redirectAttrs, "No Questions in this pool");

						if (assignmentId != null) {
							redirectAttrs.addAttribute("assignmentId", assignmentId);

							return "redirect:/viewAssignmentQuestionsByTestPoolForm";
						}
//						else {
//							return "redirect:/viewTestPools";
//						}
					}
					testQuestionPoolList = removeSpecialCharactersOfPoolFromBean(testQuestionPoolList);
					testPool.setTestQuestionPools(testQuestionPoolList);
					m.addAttribute("testQuestionPoolList", testQuestionPoolList);
					m.addAttribute("testPool", testPool);
					m.addAttribute("testPoolId", testPoolId);
					m.addAttribute("showQuestionByPool", true);

					if (assignmentId != null) {
						Assignment assignmentDB = assignmentService.findByID(assignmentId);
						List<AssignmentQuestion> assignmentQuestionDBList = assignmentQuestionService
								.findByAssignmentId(testPool.getAssignmentId());
						double TotalMarks = 0.0;

						for (AssignmentQuestion aq : assignmentQuestionDBList) {

							TotalMarks = TotalMarks + Double.parseDouble(aq.getMarks());

						}
						
						if (!"Y".equals(assignmentDB.getRandomQuestion())) {

							if (TotalMarks == Double.parseDouble(assignmentDB.getMaxScore())) {
								
								m.addAttribute("showProceed", true);
								
							} else {
								m.addAttribute("showProceed", false);
								m.addAttribute("showStudents", false);
							}
						} else {
							if (TotalMarks >= Double.parseDouble(assignmentDB.getMaxScore())) {
								m.addAttribute("showProceed", true);
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

			return "assignment/assignmentQuestionPoolList";
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
		
		@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
		@RequestMapping(value = "/saveAssignmentQuestionsByTestPool", method = { RequestMethod.GET, RequestMethod.POST })
		public String saveAssignmentQuestionsByTestPool(

				Model m, @ModelAttribute TestPool testPool, Principal principal, HttpServletRequest request,
				RedirectAttributes redirectAttr) {
			String username = principal.getName();

			List<Map<String, Object>> testQuestionPoolsByIds = testQuestionPoolsService
					.findByListOfIds(testPool.getTestQuestionsPoolIds());

			List<AssignmentQuestion> assignmentQuestionList = conversionFromQuestionPoolToAssignmentQuestion(testQuestionPoolsByIds,
					Long.toString(testPool.getAssignmentId()), username);

			Assignment assignmentDB = assignmentService.findByID(testPool.getAssignmentId());

			List<AssignmentQuestion> assignmentQuestionDBList = assignmentQuestionService.findByAssignmentId(testPool.getAssignmentId());

			double TotalMarks = 0.0;
			for (AssignmentQuestion tq : assignmentQuestionList) {

				TotalMarks = TotalMarks + Double.parseDouble(tq.getMarks());

			}
			for (AssignmentQuestion tq : assignmentQuestionDBList) {

				TotalMarks = TotalMarks + Double.parseDouble(tq.getMarks());

			}

			if ("N".equals(assignmentDB.getRandomQuestion())) {

				if (TotalMarks < Double.parseDouble(assignmentDB.getMaxScore())) {

					assignmentQuestionService.insertBatch(assignmentQuestionList);
					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
					setNote(redirectAttr,
							"Please add more question as total marks of questions should be match with total marks of assignment.");
				} else if (TotalMarks == Double.parseDouble(assignmentDB.getMaxScore())) {
					assignmentQuestionService.insertBatch(assignmentQuestionList);

					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
				} else {

					setNote(redirectAttr, "Sum of score of added questions is more than the total score of assignment.");
				}

			} else {
				
				if (TotalMarks < Double.parseDouble(assignmentDB.getMaxScore())) {

					assignmentQuestionService.insertBatch(assignmentQuestionList);
					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
					setNote(redirectAttr,
							"Please add more question as total marks of questions are lesser than total marks of assignment.");
				}else {
					assignmentQuestionService.insertBatch(assignmentQuestionList);
					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
				}
			}

			redirectAttr.addAttribute("testPoolId", testPool.getId());
			redirectAttr.addAttribute("assignmentId", testPool.getAssignmentId());

			return "redirect:/viewAssignmentQuestionsByTestPool";

		}
		
		public List<AssignmentQuestion> conversionFromQuestionPoolToAssignmentQuestion(List<Map<String, Object>> mapper, String assignmentId,
				String username) {

			List<AssignmentQuestion> assignmentQuestionList = new ArrayList<>();

			List<AssignmentQuestion> assignmentQuestionDB = assignmentQuestionService.findByAssignmentId(Long.valueOf(assignmentId));

			Assignment assignmentDB = assignmentService.findByID(Long.valueOf(assignmentId));

			double totalMarks = 0.0;

			if (assignmentQuestionDB.size() > 0) {
				for (AssignmentQuestion tq : assignmentQuestionDB) {
					totalMarks = totalMarks + Double.parseDouble(tq.getMarks());
				}
			} else {
				totalMarks = 0.0;
			}
			for (Map<String, Object> map : mapper) {
				AssignmentQuestion tq = new AssignmentQuestion();

				tq.setAssignmentId(assignmentId);
				BigDecimal marks = (BigDecimal) map.get("marks");
				tq.setDescription((String) map.get("description"));
				tq.setMarks(Double.toString(marks.doubleValue()));
				tq.setTestQuestionPoolId(String.valueOf((Integer) map.get("id")));
				tq.setQuestionType((String) map.get("questionType"));
				tq.setCreatedBy(username);
				tq.setLastModifiedBy(username);

				totalMarks = totalMarks + marks.doubleValue();
				assignmentQuestionList.add(tq);

			}
			return assignmentQuestionList;
		}
		
		
		@Secured({ "ROLE_FACULTY", "ROLE_ADMIN" })
		@RequestMapping(value = "/saveAllAssignmentQuestionsByTestPool", method = { RequestMethod.GET, RequestMethod.POST })
		public String saveAllAssignmentQuestionsByTestPool(

				Model m, @ModelAttribute TestPool testPool, Principal principal, HttpServletRequest request,
				RedirectAttributes redirectAttr) {
			String username = principal.getName();

			List<TestQuestionPools> testQuestionPoolList = new ArrayList<>();

			Assignment assignmentDB = assignmentService.findByID(testPool.getAssignmentId());

			testQuestionPoolList = testQuestionPoolsService.findAllTestQuestionsByTestPoolIdAndTestTypeForAssignment(
					Long.toString(testPool.getAssignmentId()), String.valueOf(testPool.getId()), "Descriptive");


			for (TestQuestionPools tqp : testQuestionPoolList) {

				testPool.getTestQuestionsPoolIds().add(tqp.getId().toString());
			}

			List<Map<String, Object>> testQuestionPoolsByIds = testQuestionPoolsService
					.findByListOfIds(testPool.getTestQuestionsPoolIds());
			logger.info("testQuestionPoolsByIds---->"+testQuestionPoolsByIds);
			logger.info("testId---->"+testPool.getTestId());
			List<AssignmentQuestion> assignmentQuestionList = conversionFromQuestionPoolToAssignmentQuestion(testQuestionPoolsByIds,
					Long.toString(testPool.getAssignmentId()), username);

			List<AssignmentQuestion> assignmentQuestionDBList = assignmentQuestionService.findByAssignmentId(testPool.getAssignmentId());

			double TotalMarks = 0.0;
			for (AssignmentQuestion tq : assignmentQuestionList) {

				TotalMarks = TotalMarks + Double.parseDouble(tq.getMarks());

			}
			for (AssignmentQuestion tq : assignmentQuestionDBList) {

				TotalMarks = TotalMarks + Double.parseDouble(tq.getMarks());

			}

			if ("N".equals(assignmentDB.getRandomQuestion())) {

				if (TotalMarks < Double.parseDouble(assignmentDB.getMaxScore())) {

					assignmentQuestionService.insertBatch(assignmentQuestionList);
					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
					setNote(redirectAttr,
							"Please add more question as total marks of questions should be match with total marks of assignment.");
				} else if (TotalMarks == Double.parseDouble(assignmentDB.getMaxScore())) {
					assignmentQuestionService.insertBatch(assignmentQuestionList);

					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
				} else {

					setNote(redirectAttr, "Sum Of score of added questions is more than the total score of assignment.");
				}

			} else {
				
				if (TotalMarks < Double.parseDouble(assignmentDB.getMaxScore())) {

					assignmentQuestionService.insertBatch(assignmentQuestionList);
					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
					setNote(redirectAttr,
							"Please add more question as total marks of questions are lesser than total marks of assignment.");
				}else {
					assignmentQuestionService.insertBatch(assignmentQuestionList);
					setSuccess(redirectAttr, "Assignment Question Added successfully from the pool");
				}
			}

			redirectAttr.addAttribute("testPoolId", testPool.getId());
			redirectAttr.addAttribute("assignmentId", testPool.getAssignmentId());

			return "redirect:/viewAssignmentQuestionsByTestPool";

		}
		
		@SuppressWarnings("unused")
		@Secured("ROLE_SUPPORT_ADMIN")
		@RequestMapping(value="/editAssignment" , method = { RequestMethod.GET,RequestMethod.POST})
		public String editAssignment(@ModelAttribute Assignment assignment, Model m,RedirectAttributes redirect) 
		{
			
			Assignment assignmenta = assignmentService.findByID(assignment.getId());
			//assignmentService.update(assignment);
			Course c =	courseService.findByID(assignmenta.getCourseId());
			logger.info("course service"+c);
			
			
			assignmenta.setCourseName(c.getCourseName());
		logger.info("assignment"+assignment.getId());
		
		
		List<AssignmentConfiguration> assignmentconfig=assignmentConfigurationService.findAllByAssignmentId(assignment.getId());
		List<AssignmentConfiguration> assignmentPoolConfig=assignmentConfigurationService.findPoolByAssignmentId(assignment.getId());
		logger.info("assignmentconfig"+assignmentconfig.size());
		logger.info("assignmentconfig"+assignmentPoolConfig.size());
		
		if(assignmentconfig.isEmpty() && assignmentPoolConfig.isEmpty())
		{
			
			
			m.addAttribute("editmarks", "Y");
		}
		else {
			m.addAttribute("editmarks", "N");
		}
		
		
		m.addAttribute("assignment", assignmenta);
			return "assignment/editAssignmentBySupportAdmin";
		}
		
		@Secured("ROLE_SUPPORT_ADMIN")
		@RequestMapping(value="updateAssignmentBySupportAdmin",method= {RequestMethod.GET,RequestMethod.POST})
		public String updateAssignmentBySupportAdmin (@ModelAttribute Assignment assignment, Model m,RedirectAttributes redirect)
		{
			logger.info("assignment"+assignment.getId());
			logger.info("checksum"+assignment.getIsCheckSumReq());
			logger.info("plagrism"+assignment.getPlagscanRequired());
			
			Assignment assignmentDb=assignmentService.findByID(assignment.getId());
			logger.info("1"+assignment.getIsCheckSumReq());
			logger.info("2"+assignmentDb.getIsCheckSumReq());
			
			
			logger.info("assignment.getIsCheckSumReq() != assignmentDb.getIsCheckSumReq()"+!assignment.getIsCheckSumReq().equals(assignmentDb.getIsCheckSumReq()));
			if (!assignment.getIsCheckSumReq().equals(assignmentDb.getIsCheckSumReq()))
			{
				logger.info("1");
				assignmentDb.setIsCheckSumReq(assignment.getIsCheckSumReq());
			}
			if(!assignment.getAssignmentName().equals(assignmentDb.getAssignmentName())) 
			{
				logger.info("2");
				assignmentDb.setAssignmentName(assignment.getAssignmentName());
			}
			if(!assignment.getMaxScore().equals(assignmentDb.getMaxScore()))
			{
				logger.info("3");
				assignmentDb.setMaxScore(assignment.getMaxScore());
			}
			if(!assignment.getPlagscanRequired().equals(assignmentDb.getPlagscanRequired())) 
			{
				logger.info("4");
				assignmentDb.setPlagscanRequired(assignment.getPlagscanRequired());
			}
			if(assignment.getPlagscanRequired().equals("Yes"))
			{
				
			if(!assignment.getRunPlagiarism().equals(assignmentDb.getRunPlagiarism()))
			{
				assignmentDb.setRunPlagiarism(assignment.getRunPlagiarism());
			}
			
			if(!assignmentDb.getThreshold().equals(assignment.getThreshold()))
			{
				assignmentDb.setThreshold(assignment.getThreshold());
			}
			
			
			}
			assignmentService.update(assignmentDb);
			setSuccess(redirect, "Assignment updated successfully");
			redirect.addFlashAttribute(assignment);

			return "redirect:/getAssignmentStatus";
			
		}
	
}
