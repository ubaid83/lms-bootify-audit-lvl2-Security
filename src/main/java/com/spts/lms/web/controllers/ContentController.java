package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.content.StudentContentService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.SortByCreatedDate;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.UnzipUtil;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;


@Controller
@SessionAttributes("userId")
public class ContentController extends BaseController {

	@Autowired
	ApplicationContext act;
	
	
	@Autowired
	ContentService contentService;

	@Autowired
	StudentContentService studentContentService;

	@Autowired
	UserService userService;

	@Autowired
	Notifier notifier;
	
	@Autowired
	Utils utils;
	@Autowired
	UserCourseService userCourseService;

	@Value("${lms.content.courseRootFolder}")
	private String courseRootFolder;
	
	@Value("${lms.content.courseRootFolderS3}")
	private String courseRootFolderS3;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Value("${sendAlertsToParents}")
	private String sendAlertsToParents;

	@Autowired
	StudentParentService studentParentService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	TestService testService;

	@Autowired
	ProgramService programService;
	
	@Autowired
	BusinessBypassRule businessBypassRule;
	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;
	
	@Autowired
	private static final Logger logger = Logger
			.getLogger(ContentController.class);
	private static final String FILE_SEPARATOR = "/";
	
	
	@Value("${app}")
	private String appName;
	
	Client client = ClientBuilder.newClient();
	ObjectMapper mapper = new ObjectMapper();

	private static final String serverURL = "http://192.168.2.116:8443/"; // "http://localhost:8085/" "http://192.168.2.116:8443/" "http://192.168.2.139:8443/"
	private static final String serverCrudURL = "http://192.168.2.139:8443/usermgmtcrud/"; // "http://192.168.2.139:8443/usermgmtcrud/"

	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addContentForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addContentForm(@ModelAttribute Content content, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {
		m.addAttribute("webPage", new WebPage("assignment", "Create Content",
				false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		if(content.getParentContentId()!=null){
		
			Content parentContentDB = contentService.findByID(content.getParentContentId());
			content.setModuleId(parentContentDB.getModuleId());
		}
		
		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		if (content.getIdForModule() != null
				&& (content.getModuleId() == null || content.getModuleId() == "")) {
			content.setModuleId(content.getIdForModule());
		}
		String contentType = content.getContentType();
		if (content.getAcadYear() != null && content.getModuleId() != null
				&& content.getCourseId() == null) {
			List<Course> moduleList = courseService.findModulesByUsername(
					username, String.valueOf(content.getAcadYear()),
					Long.parseLong(userdetails1.getProgramId()));
			m.addAttribute("moduleName",
					courseService.getModuleName(content.getModuleId()));
			m.addAttribute("modules", moduleList);

		} else if (content.getAcadYear() != null
				&& content.getModuleId() != null
				&& content.getCourseId() != null
				&& !content.getModuleId().isEmpty()) {
			m.addAttribute(
					"courses",
					courseService.findCoursesByModuleId(
							Long.valueOf(content.getModuleId()),
							principal.getName(),
							String.valueOf(content.getAcadYear()),userdetails1.getProgramId()));
		} else if (content.getAcadYear() == null
				&& content.getModuleId() == null
				&& content.getCourseId() == null) {
			List<String> academicYear = courseService.getAllAcadYear();
			m.addAttribute("acadYear", academicYear);
		}
		if (content != null && content.getId() != null) {
			content = contentService.findByID(content.getId());
			if (content.getModuleId() != null && content.getCourseId() == null) {
				List<Course> moduleList = courseService.findModulesByUsername(
						username, String.valueOf(content.getAcadYear()),
						Long.parseLong(userdetails1.getProgramId()));
				m.addAttribute("moduleName",
						courseService.getModuleName(content.getModuleId()));
				m.addAttribute("modules", moduleList);
			} else if (content.getCourseId() != null) {
				Course c = courseService.findByID(content.getCourseId());
				m.addAttribute("moduleName", c.getCourseName());
			}
			
			m.addAttribute("edit", "true");

		}

		if (content != null && content.getParentContentId() != null) {
			Content parentContent = contentService.findByID(content
					.getParentContentId());
			content.setAccessType(parentContent.getAccessType());
		}

		m.addAttribute("content", content);
		Token userDetails = (Token) principal;
		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			String progName = userDetails.getProgramName();
			m.addAttribute("courses",
					courseService.findByCoursesBasedOnProgramName(progName));
		} else {
			List<String> academicYear = courseService.getAllAcadYear();
			m.addAttribute("acadYear", academicYear);
			if (null != content.getModuleId()) {
				if (!content.getModuleId().isEmpty()) {
					m.addAttribute("courses", courseService
							.findCoursesByModuleId(
									Long.valueOf(content.getModuleId()),
									principal.getName(),
									String.valueOf(content.getAcadYear()),userdetails1.getProgramId()));

				} else {
					// moduleId empty
					m.addAttribute(
							"courses",
							courseService.findCoursesByUsernameAndProgramId(
									principal.getName(),
									Long.parseLong(userdetails1.getProgramId())));

				}
			} else {// moduleId null
				if (null != content.getContentFor()) {
					if (content.getContentFor().equals("course")) {
						m.addAttribute("courses", courseService
								.findCoursesByUsernameAndProgramId(principal
										.getName(), Long.parseLong(userdetails1
										.getProgramId())));
					}
				}
			}

		}
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		if (Content.FOLDER.equalsIgnoreCase(contentType)) {
			if (null != content.getContentFor()) {
				if (content.getContentFor().equals("module")) {
					return "content/addContentFolderForModule";
				} else if (content.getContentFor().equals("course")) {
					return "content/addContentFolder";
				}

				return "content/addContentFolderForModule";
			} else {
				if (content.getCourseId() != null
						&& content.getModuleId() != null) {

					return "content/addContentFolder";
				} else if (content.getCourseId() != null
						&& content.getModuleId() == null) {

					return "content/addContentFolder";
				}

				else {

					return "content/addContentFolderForModule";
				}
			}


		} else if (Content.FILE.equalsIgnoreCase(contentType)) {
			if (null != content.getContentFor()) {
				if (content.getContentFor().equals("module")) {
					return "content/addContentFileForModule";
				} else if (content.getContentFor().equals("course")) {
					return "content/addContentFile";
				}
				return "content/addContentFileForModule";
			} else {
				if (content.getCourseId() != null
						&& content.getModuleId() != null) {

					return "content/addContentFile";
				} else if (content.getCourseId() != null
						&& content.getModuleId() == null) {
					return "content/addContentFile";
				} else {
					return "content/addContentFileForModule";
				}
			}
		} else if (Content.MULTIPLE_FILE.equalsIgnoreCase(contentType)) {
			if (null != content.getContentFor()) {
				if (content.getContentFor().equals("module")) {
					return "content/addContentMultipleFileForModule";
				} else if (content.getContentFor().equals("course")) {
					return "content/addContentMultipleFile";
				}
				return "content/addContentMultipleFileForModule";
			} else {
				if (content.getCourseId() != null
						&& content.getModuleId() != null) {
					return "content/addContentMultipleFile";
				} else if (content.getCourseId() != null
						&& content.getModuleId() == null) {
					return "content/addContentMultipleFile";
				} else {
					return "content/addContentMultipleFileForModule";
				}
			}
		} else if (Content.LINK.equalsIgnoreCase(contentType)) {
			if (null != content.getContentFor()) {
				if (content.getContentFor().equals("module")) {
					return "content/addContentLinkForModule";
				} else if (content.getContentFor().equals("course")) {
					return "content/addContentLink";
				}
				return "content/addContentLinkForModule";
			}

			else {
				if (content.getCourseId() != null
						&& content.getModuleId() != null) {
					return "content/addContentLink";
				} else if (content.getCourseId() != null
						&& content.getModuleId() == null) {
					return "content/addContentLink";
				} else {
					return "content/addContentLinkForModule";
				}
			}
		} else if (Content.YOUTUBE_VIDEO.equalsIgnoreCase(contentType)) {
			return "content/addContentYoutubeVideo";
		} else {
			return "content/addContentFolder";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addMultipleFile", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addMultipleFile(
			@ModelAttribute Content content,
			@RequestParam("file") List<MultipartFile> files,
			RedirectAttributes redirectAttrs,
			Principal p,
			Model m,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);

		try {
			Course c = new Course();
			String username = p.getName();


			HtmlValidation.validateHtml(content, new ArrayList<>());
			
			Course course = new Course();
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.valueOf(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			
			
			Content contentd = new Content();
			if (content.getCourseId() == null) {
				content.setCourseId(Long.valueOf(idForCourse));
			}
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			performFolderPathCheck(content);
			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			String acadSession = u.getAcadSession();
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			for (MultipartFile file : files) {
				if (file == null || file.isEmpty()) {
					setError(redirectAttrs,
							"No file selected. Please select a file to upload.");
					return "redirect:/addContentForm";
				}
				//Audit change start
				String errorMessage = "";
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(o -> o == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/addContentForm";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
								|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/addContentForm";
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
							errorMessage = uploadContentFileForS3(content, content.getFolderPath(), file);
							} else {
								setError(redirectAttrs, "File uploaded is invalid!");
								return "redirect:/addContentForm";
							}
						}
					}
				}else {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/addContentForm";
				}
				//Audit change end

				if (!errorMessage.contains("Error")) {
					logger.info("errorMessage ---------- " + errorMessage);
					if (sendAlertsToParents.equalsIgnoreCase("Y")) {
						content.setSendEmailAlertToParents("Y");
						content.setSendSmsAlertToParents("Y");
					}

					if (StringUtils.isEmpty(idForCourse)) {
						idForCourse = content.getIdForCourse();
					}
					if (!StringUtils.isEmpty(idForCourse)) {
						c = courseService.findByID(Long.valueOf(idForCourse));
						content.setCourseId(Long.valueOf(idForCourse));
					} else
						c = courseService.findByID(content.getCourseId());
					if (sendAlertsToParents.equalsIgnoreCase("Y")) {
						content.setSendEmailAlertToParents("Y");
						content.setSendSmsAlertToParents("Y");
					}

					content.setContentName(errorMessage);
					content.setAcadMonth(c.getAcadMonth());
					content.setAcadYear(Integer.valueOf(c.getAcadYear()));

					content.setContentFileName(errorMessage);
					content.setCreatedBy(p.getName());
					content.setLastModifiedBy(p.getName());
					content.setFacultyId(p.getName());
					content.setContentType("File");

					contentService.insertWithIdReturn(content);
				

					setSuccess(redirectAttrs, "File Created Successfully");
				} else {
					setError(redirectAttrs, "Error in uploading file "
							+ errorMessage);
					return "redirect:/addContentForm";
				}
			}

		}catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			return "redirect:/addContentForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addMultipleFileForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addMultipleFileForModule(
			@ModelAttribute Content content,
			@RequestParam("file") List<MultipartFile> files,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse,
			@RequestParam(name = "idForModule", required = false, defaultValue = "") String idForModule,
			@RequestParam(name = "acadYear", required = false, defaultValue = "") String acadYear) {
		redirectAttrs.addFlashAttribute("content", content);
		try {

			
			HtmlValidation.validateHtml(content, new ArrayList<>());
			
			businessBypassRule.validateNumeric(acadYear);
		
			Course course = new Course();
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.valueOf(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			if (null != idForModule && !idForModule.isEmpty()) {
				businessBypassRule.validateNumeric(idForModule);
				course = courseService.checkIfExistsInDB("moduleId", idForModule);
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}

			
				
			businessBypassRule.validateaccesstype(content.getAccessType());
			
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
		
			String username = p.getName();
			List<Course> courseIdList = new ArrayList<>();
			courseIdList = courseService.findCoursesByModuleId(
					Long.valueOf(idForModule), username, acadYear);
			
			Content cont= contentService.findByID(content.getId());
			

			if (idForModule.isEmpty()) {
				idForModule = content.getModuleId();
			}
			boolean create = true;
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			performFolderPathCheckForModule(content);
			for (MultipartFile file : files) {
				if (file == null || file.isEmpty()) {
					setError(redirectAttrs,
							"No file selected. Please select a file to upload.");
					return "redirect:/addContentForm";
				}

				Content contentForMultiFileModule = new Content();

				String acadMonth = courseService
						.getAcadMonthByModuleIdAndAcadYear(idForModule,
								String.valueOf(content.getAcadYear()));

				contentForMultiFileModule.setContentName(content
						.getContentName());

				contentForMultiFileModule.setContentDescription(content
						.getContentDescription());
				contentForMultiFileModule
						.setAcadYear(Integer.valueOf(acadYear));
				contentForMultiFileModule.setContentType(content
						.getContentType());
				contentForMultiFileModule
						.setAccessType(content.getAccessType());
				contentForMultiFileModule.setStartDate(content.getStartDate());
				contentForMultiFileModule.setEndDate(content.getEndDate());
				contentForMultiFileModule.setIsCourseRootFolder(content
						.getIsCourseRootFolder());
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					contentForMultiFileModule.setSendEmailAlertToParents("Y");
					contentForMultiFileModule.setSendSmsAlertToParents("Y");
				}
				//For Adding the parent Contentid
				if (content.getParentContentId() != null) {
					contentForMultiFileModule.setParentContentId(content
							.getParentContentId());
				}
				
				contentForMultiFileModule.setSendSmsAlert(content
						.getSendSmsAlert());
				contentForMultiFileModule.setSendEmailAlert(content
						.getSendEmailAlert());
				contentForMultiFileModule.setExamViewType(content
						.getExamViewType());
				contentForMultiFileModule.setModuleId(idForModule);
				contentForMultiFileModule.setCreatedBy(p.getName());
				contentForMultiFileModule.setLastModifiedBy(p.getName());
				contentForMultiFileModule.setFacultyId(p.getName());

				contentForMultiFileModule.setFolderPath(courseRootFolder
						+ idForModule + FILE_SEPARATOR);
				contentForMultiFileModule.setFilePath(content.getFolderPath()
						+ contentForMultiFileModule.getContentName()
						+ FILE_SEPARATOR);
			

				boolean created = false;
				//Audit change start
				String errorMessage = "";
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/addContentForm";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
								|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/addContentForm";
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
							errorMessage = uploadContentFileForS3(content,
										content.getFolderPath(), file);
							} else {
								setError(redirectAttrs, "File uploaded is invalid!");
								return "redirect:/addContentForm";
							}
						}
					}
				}else {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/addContentForm";
				}
				//Audit change end

				contentForMultiFileModule.setAcadMonth(acadMonth);
				logger.info("ParentContent  -----------> "+content.getParentContentId());
				
				contentForMultiFileModule.setFilePath(content.getFilePath());
				contentForMultiFileModule.setFolderPath(content.getFolderPath());
				if (!errorMessage.contains("Error")) {
					contentForMultiFileModule.setContentName(errorMessage);
					logger.info("errorMessage ---------- " + errorMessage);
					contentService
							.insertWithIdReturn(contentForMultiFileModule);
					Long moduleEntryId;
					moduleEntryId = contentForMultiFileModule.getId();
					if (StringUtils.isEmpty(idForCourse)) {
						idForCourse = content.getIdForCourse();
					}
					Course c = new Course();
					for (Course c1 : courseIdList) {
						String idForCourse1 = String.valueOf(c1.getId());
						content.setCourseId(Long.valueOf(idForCourse1));
						Token userdetails1 = (Token) p;
						String ProgramName = userdetails1.getProgramName();
						User u = userService.findByUserName(username);
						if (!StringUtils.isEmpty(idForCourse1)) {

							c = courseService.findByID(Long
									.valueOf(idForCourse1));
							content.setCourseId(Long.valueOf(idForCourse1));
						} else
							c = courseService.findByID(Long
									.valueOf(idForCourse1));
						if (sendAlertsToParents.equalsIgnoreCase("Y")) {
							content.setSendEmailAlertToParents("Y");
							content.setSendSmsAlertToParents("Y");
						}
						content.setAcadMonth(c.getAcadMonth());
						String acadSession = u.getAcadSession();
						content.setAcadMonth(c.getAcadMonth());
						content.setContentName(contentForMultiFileModule
								.getContentName());
						content.setParentModuleId(String.valueOf(moduleEntryId));
						if(idForModule != null)
						{
							content.setModuleId(idForModule);
						}else
						{
							content.setModuleId(content.getIdForModule());
						}
						content.setAcadYear(Integer.valueOf(c.getAcadYear()));
						content.setCreatedBy(p.getName());
						content.setLastModifiedBy(p.getName());

						content.setFolderPath(contentForMultiFileModule
								.getFolderPath());
						content.setFilePath(contentForMultiFileModule
								.getFilePath());

						content.setModuleId(idForModule);

						content.setFacultyId(p.getName());

						contentService.insertWithIdReturn(content);
					}
					setSuccess(redirectAttrs, "File Created Successfully");

				} else {
					setError(redirectAttrs, "Error in uploading file "
							+ errorMessage);
					return "redirect:/addContentForm";
				}
			}
			
			
		}
		catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addContentForm";
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addContentForm";
		}
		content.setCourseId(null);
		return "redirect:/getContentUnderAPathForFacultyForModule";
		
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addFolder", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addFolder(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			Model m,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);
		File file = null;
		try {
			
			HtmlValidation.validateHtml(content, new ArrayList<>());
			
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			
			
			Course course = new Course();
			
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.parseLong(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			String username = p.getName();
			if (content.getCourseId() == null) {
				content.setCourseId(Long.valueOf(idForCourse));
			}
			performFolderPathCheck(content);
			
			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			String completFolderPath = content.getFolderPath()
					+ content.getContentName();
			file = new File(completFolderPath);

			boolean created = false;
			String folderPathForS3 = content.getFolderPath();
			if(folderPathForS3.startsWith("/")) {
				folderPathForS3 = StringUtils.substring(folderPathForS3, 1);
			}
			if(folderPathForS3.endsWith("/")) {
				folderPathForS3 = folderPathForS3.substring(0, folderPathForS3.length()-1);
			}
			if(amazonS3ClientService.exists(completFolderPath)) {
				setError(redirectAttrs, "Folder Already Exists with name "+content.getContentName());
				return "redirect:/addContentForm";
			}
			created = amazonS3ClientService.createFolder(content.getContentName(),folderPathForS3);
			content.setFolderPath(folderPathForS3+"/");
				Course c = null;

				if (StringUtils.isEmpty(idForCourse)) {
					idForCourse = content.getIdForCourse();
				}
				if (!StringUtils.isEmpty(idForCourse)) {
					c = courseService.findByID(Long.valueOf(idForCourse));
					content.setCourseId(Long.valueOf(idForCourse));
				} else
					c = courseService.findByID(content.getCourseId());
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					content.setSendEmailAlertToParents("Y");
					content.setSendSmsAlertToParents("Y");
				}
				content.setAcadMonth(c.getAcadMonth());
				content.setAcadYear(Integer.valueOf(c.getAcadYear()));
				content.setCreatedBy(p.getName());
				content.setLastModifiedBy(p.getName());
				content.setFilePath(folderPathForS3+"/"+content.getContentName()+"/");
				content.setFacultyId(p.getName());
				// Create entry in DB
				contentService.insertWithIdReturn(content);

				if (created) {
					setSuccess(redirectAttrs, "Folder Created Successfully");
					if (Content.ACCESS_TYPE_EVERYONE.equals(content.getAccessType())){
                        return "redirect:/saveStudentContentAllocationForAllStudents";
					}
				} else {
					setError(redirectAttrs, "Error in creating folder");
					return "redirect:/addContentForm";
				}

		}catch(ValidationException e) {
			

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			if (file != null && file.list().length == 0) {
				file.delete();
			}
			return "redirect:/addContentForm";
		
		}
		
		
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			if (file != null && file.list().length == 0) {
				file.delete();
			}
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	
	
	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addFolderForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addFolderForModule(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			Model m,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse,
			@RequestParam(name = "idForModule", required = false, defaultValue = "") String idForModule,
			@RequestParam(name = "acadYear", required = false, defaultValue = "") String acadYear) {
		redirectAttrs.addFlashAttribute("content", content);
		File file = null;
		try {
			

				
				HtmlValidation.validateHtml(content, new ArrayList<>());


			//	businessBypassRule.validateNumeric(acadYear);
				Course acadyear=courseService.checkIfExistsInDB("acadYear",acadYear);
				
				if(null==acadyear || acadyear.equals(" ") ) {
					
					
					if (file != null && file.list().length == 0) {
						file.delete();
					}
					
					throw new ValidationException("Error in creating Folder Invalid Acad Year Selected");
				}
				

				businessBypassRule.validateAlphaNumeric(content.getContentName());
				Course course=new Course();
				if (null != idForCourse && !idForCourse.isEmpty()) {
					businessBypassRule.validateNumeric(idForCourse);
					course = courseService.findByID(Long.valueOf(idForCourse));
					if (null == course) {
						if (file != null && file.list().length == 0) {
							file.delete();
						}
						throw new ValidationException("Invalid course");
					}
				}
				if (null != idForModule && !idForModule.isEmpty()) {
					businessBypassRule.validateNumeric(idForModule);
					course = courseService.checkIfExistsInDB("moduleId", idForModule);
					if (null == course) {
						if (file != null && file.list().length == 0) {
							file.delete();
						}
						throw new ValidationException("Invalid course");
					}
				}
				
				
				businessBypassRule.validateaccesstype(content.getAccessType());
			
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());

		
			
			String username = p.getName();
			if (idForModule != null && !idForModule.isEmpty()) {
				content.setModuleId(idForModule);
			}
			if (!acadYear.isEmpty() && acadYear != null) {
				content.setAcadYear(Integer.valueOf(acadYear));
			}
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			String acadMonth = courseService.getAcadMonthByModuleIdAndAcadYear(
					content.getModuleId(),
					String.valueOf(content.getAcadYear()));

			performFolderPathCheckForModule(content);

			if (idForModule.isEmpty()) {

				idForModule = content.getModuleId();
			}
			List<Course> courseIdList = new ArrayList<>();

			courseIdList = courseService.findCoursesByModuleId(
					Long.valueOf(idForModule), username, acadYear);
			boolean created = false;

			Content contentForModule = new Content();
			contentForModule.setContentName(content.getContentName());
			contentForModule.setContentDescription(content
					.getContentDescription());
			contentForModule.setAcadMonth(acadMonth);
			contentForModule.setAcadYear(Integer.valueOf(acadYear));
			contentForModule.setContentType(content.getContentType());
			contentForModule.setAccessType(content.getAccessType());
			contentForModule.setStartDate(content.getStartDate());
			contentForModule.setEndDate(content.getEndDate());
			contentForModule.setIsCourseRootFolder(content
					.getIsCourseRootFolder());
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				contentForModule.setSendEmailAlertToParents("Y");
				contentForModule.setSendSmsAlertToParents("Y");
			}

			contentForModule.setSendSmsAlert(content.getSendSmsAlert());
			contentForModule.setSendEmailAlert(content.getSendEmailAlert());
			contentForModule.setExamViewType(content.getExamViewType());
			contentForModule.setModuleId(idForModule);
			contentForModule.setCreatedBy(p.getName());
			contentForModule.setLastModifiedBy(p.getName());
			contentForModule.setFacultyId(p.getName());
			if (content.getParentContentId() != null) {
				contentForModule.setParentContentId(content
						.getParentContentId());
			}
			String completFolderPath = content.getFolderPath()+ content.getContentName() + "/";
			String folderPathForS3 = content.getFolderPath();
			if(folderPathForS3.startsWith("/")) {
				folderPathForS3 = StringUtils.substring(folderPathForS3, 1);
			}
			if(folderPathForS3.endsWith("/")) {
				folderPathForS3 = folderPathForS3.substring(0, folderPathForS3.length()-1);
			}
			if(completFolderPath.startsWith("/")) {
				completFolderPath = StringUtils.substring(completFolderPath, 1);
			}
			if(amazonS3ClientService.exists(completFolderPath)) {
				setError(redirectAttrs, "Folder Already Exists with name "+content.getContentName());
				return "redirect:/addContentForm";
			}
			created = amazonS3ClientService.createFolder(content.getContentName(),folderPathForS3);
			contentForModule.setFolderPath(folderPathForS3+"/");
			contentForModule.setFilePath(completFolderPath);
			if (created) {
				contentService.insertWithIdReturn(contentForModule);
				long moduleEntryId = contentForModule.getId();
				for (Course c1 : courseIdList) {
					String idForCourse1 = String.valueOf(c1.getId());
					content.setCourseId(Long.valueOf(idForCourse1));

					Token userdetails1 = (Token) p;
					String ProgramName = userdetails1.getProgramName();
					User u = userService.findByUserName(username);

					String acadSession = u.getAcadSession();

					m.addAttribute("Program_Name", ProgramName);
					m.addAttribute("AcadSession", acadSession);

					Course c = null;

					if (!StringUtils.isEmpty(idForCourse1)) {

						c = courseService.findByID(Long.valueOf(idForCourse1));
						content.setCourseId(Long.valueOf(idForCourse1));
					} else
						c = courseService.findByID(Long.valueOf(idForCourse1));

					content.setFolderPath(contentForModule.getFolderPath());
					content.setFilePath(contentForModule.getFilePath());
					content.setParentModuleId(String.valueOf(moduleEntryId));

					content.setModuleId(idForModule);

					content.setAcadMonth(c.getAcadMonth());
					content.setAcadYear(Integer.valueOf(c.getAcadYear()));
					content.setCreatedBy(p.getName());
					content.setLastModifiedBy(p.getName());
					content.setFacultyId(p.getName());
					contentService.insertWithIdReturn(content);

				}

				setSuccess(redirectAttrs, "Folder Created Successfully");
				if (Content.ACCESS_TYPE_EVERYONE.equals(content
                        .getAccessType())){
					redirectAttrs.addFlashAttribute("content",contentForModule);
                    return "redirect:/saveStudentContentAllocationForAllStudentsForModule";
				}

			} else {
				setError(redirectAttrs, "Error in creating folder");
				return "redirect:/addContentForm";
			}

		}
		catch (ValidationException e) {

			
			setError(redirectAttrs, e.getMessage());
			
			return "redirect:/addContentForm";
		
		}
		
		
		
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			if (file != null && file.list().length == 0) {
				file.delete();
			}
			return "redirect:/addContentForm";
		}
		content.setCourseId(null);
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewContentForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewContentForModule(@ModelAttribute Content content,
			Model m, @RequestParam(required = false) Long campusId,
			Principal p, @RequestParam Long id, @RequestParam Long moduleId,
			@RequestParam String acadYear) {
		m.addAttribute("webPage", new WebPage("content", "Create Content",
				true, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("id", content.getId());

		if (content != null && content.getId() != null) {
			logger.info("content is not null or id is not null");
			content = contentService.findByID(content.getId());
		}
		String contentType = content.getContentType();
		m.addAttribute("allCampuses", userService.findCampus());

		m.addAttribute("content", content);
		List<Course> courseIdList = new ArrayList<>();
		courseIdList = courseService.findCoursesByModuleId(moduleId, username,
				acadYear);
		logger.info("Id------------------->" + id);
		List<Long> perentidList = contentService.getIdByParentModuleId(id);

		logger.info("perentidList------------------->" + perentidList + "--->"
				+ contentType);
		if (Content.FOLDER.equalsIgnoreCase(contentType)) {
			List<StudentContent> students = new ArrayList<StudentContent>();
			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusIdbyModuleId(
								perentidList, content.getModuleId(), campusId,
								content.getAcadYear());
			} else {
				students = studentContentService.getStudentsContentForModule(
						perentidList, content.getModuleId(),
						content.getAcadYear(),content.getFacultyId());
			}
			

			m.addAttribute("students", students);

			StudentContent studentCount = studentContentService
					.getNoOfStudentsAllocatedForModule(perentidList);
			m.addAttribute("noOfStudentAllocated", studentCount.getCount());
			return "content/contentFolderForModule";
		} else if (Content.FILE.equalsIgnoreCase(contentType)) {

			List<StudentContent> students = new ArrayList<StudentContent>();

			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusIdbyModuleId(
								perentidList, content.getModuleId(), campusId,
								content.getAcadYear());
			} else {
				students = studentContentService.getStudentsContentForModule(
						perentidList, content.getModuleId(),
						content.getAcadYear(),content.getFacultyId());
			}
			m.addAttribute("students", students);

			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocatedForModule(perentidList).getCount());

			return "content/contentFileForModule";

		} else if (Content.MULTIPLE_FILE.equalsIgnoreCase(contentType)) {
			List<StudentContent> students = new ArrayList<StudentContent>();

			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusIdbyModuleId(
								perentidList, content.getModuleId(), campusId,
								content.getAcadYear());
			} else {
				students = studentContentService.getStudentsContentForModule(
						perentidList, content.getModuleId(),
						content.getAcadYear(),content.getFacultyId());

			}

			m.addAttribute("students", students);

			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocatedForModule(perentidList).getCount());
			return "content/contentFileForModule";
		} else if (Content.LINK.equalsIgnoreCase(contentType)) {
			List<StudentContent> students = new ArrayList<StudentContent>();

			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusIdbyModuleId(
								perentidList, content.getModuleId(), campusId,
								content.getAcadYear());
			} else {
				students = studentContentService.getStudentsContentForModule(
						perentidList, content.getModuleId(),
						content.getAcadYear(),content.getFacultyId());
			}

			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocatedForModule(perentidList).getCount());
			return "content/contentLinkForModule";
		} else if (Content.YOUTUBE_VIDEO.equalsIgnoreCase(contentType)) {

			return "content/contentYoutubeVideo";
		} else {
			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocatedForModule(perentidList).getCount());
			return "content/contentFolderForModule";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateFolder", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateFolder(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			Model m,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			
			HtmlValidation.validateHtml(content, new ArrayList<>());
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			businessBypassRule.validateNumeric(idForCourse);
			Course course=courseService.findByID(Long.valueOf(idForCourse));
			if(course.toString().isEmpty() || null==course)
			{
				setError(redirectAttrs, "Invalid Course While creating folder");
				redirectAttrs.addFlashAttribute("edit", "true");
				return "redirect:/addContentForm";
			}
			businessBypassRule.validateaccesstype(content.getAccessType());
		
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			performFolderPathCheck(content);
			Course c = null;
			String completFolderPath = content.getFolderPath()+ content.getContentName()+"/";
			if(completFolderPath.startsWith("/")) {
				completFolderPath = StringUtils.substring(completFolderPath, 1);
			}
			content.setLastModifiedBy(p.getName());
			content.setFacultyId(p.getName());
			
			if (StringUtils.isEmpty(idForCourse)) {
				idForCourse = content.getIdForCourse();
			}

			if ((content.getCourseId() == null)) {
				if (!StringUtils.isEmpty(idForCourse)) {
					content.setCourseId(Long.valueOf(idForCourse));
				}

			}
			if (!StringUtils.isEmpty(idForCourse)) {
				c = courseService.findByID(Long.valueOf(idForCourse));
				content.setCourseId(Long.valueOf(idForCourse));
			} else
				c = courseService.findByID(content.getCourseId());
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				content.setSendEmailAlertToParents("Y");
				content.setSendSmsAlertToParents("Y");
			}

			content.setAcadMonth(c.getAcadMonth());
			content.setAcadYear(Integer.valueOf(c.getAcadYear()));

			Content oldContent = contentService.findByID(content.getId());
			if (!amazonS3ClientService.exists(completFolderPath)) {
				File dest = new File(downloadAllFolder+"/localCopyContent");
				String sourcePathLocal = downloadAllFolder+"/localCopyContent";
				if(!dest.exists()) {
					dest.mkdirs();
					logger.info("create--->");
				}
				// Means Folder Name changed, Rename folder
				String newFolderName = content.getContentName();
				
				File existingFolder = new File(oldContent.getFilePath());
				File newFolder = new File(existingFolder.getParent()
						+ FILE_SEPARATOR + newFolderName);
				
				logger.info("old--->"+oldContent.getFilePath());
				logger.info("new--->"+completFolderPath);
				String tempFolderPath = content.getFolderPath();
				if(tempFolderPath.startsWith("/")) {
					tempFolderPath = StringUtils.substring(tempFolderPath, 1);
				}
				String oldFilePath = oldContent.getFilePath();
				if(oldFilePath.startsWith("/")) {
					oldFilePath = oldFilePath.substring(1, oldFilePath.length());
				}
				boolean renamed = amazonS3ClientService.createFolder(content.getContentName(),tempFolderPath);
				boolean download = amazonS3ClientService.downloadDir(oldContent.getFilePath(), sourcePathLocal);
				boolean upload = false;
				if(download) {
					if(renamed) {
						if (dest.listFiles().length > 0) {
							upload = amazonS3ClientService.uploadDir(sourcePathLocal+"/"+oldFilePath, completFolderPath, true);
						}else {
							upload = true;
						}
					}
				}
				if (!upload) {
					setError(redirectAttrs, "Error in Renaming folder");
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				} else {
					amazonS3ClientService.deleteDir(oldFilePath);
					content.setFilePath(completFolderPath);
					setSuccess(redirectAttrs, "Updated SuccessFully");
					contentService.updateChildrenDetails(content.getFilePath(), oldFilePath);
				}
				FileUtils.deleteDirectory(dest);
			}else {
				content.setFilePath(completFolderPath);
				if(!content.getContentName().equals(oldContent.getContentName())) {
					setError(redirectAttrs, "Folder Already Exists with name "+content.getContentName());
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				}
			}
			contentService.updateFolder(content);
			contentService.updateAccessTypeById(content.getAccessType(), String.valueOf(content.getId()));
//			List<Content> childContentList = contentService.findByContentId(String.valueOf(content.getId()));
//			for(Content con: childContentList){
//				con.setAccessType(content.getAccessType());
//				contentService.update(con);
//			}
			
			if ("Private".equals(content.getAccessType())) {

				studentContentService.deleteByContentId(content.getId());
			}else{
				studentContentService.setActiveByContentId(content.getId());
			}
			int studentContentCount = studentContentService.getNoOfStudentsAllocated(content.getId());
			if(studentContentCount == 0){
				if (Content.ACCESS_TYPE_EVERYONE.equals(content
	                    .getAccessType())){
	              return "redirect:/saveStudentContentAllocationForAllStudents";
	            }
			}
			setSuccess(redirectAttrs, "Folder Details updated successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateFolderForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updateFolderForModule(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			Model m,
			@RequestParam(name = "idForModule", required = false, defaultValue = "") String idForModule,
			@RequestParam(name = "acadYear", required = false, defaultValue = "") String acadYear) {
		redirectAttrs.addFlashAttribute("content", content);
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (content.getIdForModule() == null) {
			content.setIdForModule(content.getModuleId());
		}
		logger.info("ModuleID---->" + content.getModuleId());

		try {
			HtmlValidation.validateHtml(content, new ArrayList<>());
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course cr = new Course();
			if (null != idForModule && !idForModule.isEmpty()) {
				businessBypassRule.validateNumeric(idForModule);
				cr = courseService.checkIfExistsInDB("moduleId", idForModule);
				if (null == cr) {
					throw new ValidationException("Invalid course");
				}
			}
			
			
			businessBypassRule.validateaccesstype(content.getAccessType());
		
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
	
			
			
			
			performFolderPathCheckForModule(content);
			String completFolderPath = content.getFolderPath()+ content.getContentName()+"/";
			if(completFolderPath.startsWith("/")) {
				completFolderPath = StringUtils.substring(completFolderPath, 1);
			}
			content.setLastModifiedBy(p.getName());
			content.setFacultyId(p.getName());
			Content oldContent = contentService.findByID(content.getId());
			if (!amazonS3ClientService.exists(completFolderPath)) {
				File dest = new File(downloadAllFolder+"/localCopyContent");
				String sourcePathLocal = downloadAllFolder+"/localCopyContent";
				if(!dest.exists()) {
					dest.mkdirs();
					logger.info("create--->");
				}
				logger.info("old--->"+oldContent.getFilePath());
				logger.info("new--->"+completFolderPath);
				String tempFolderPath = content.getFolderPath();
				if(tempFolderPath.startsWith("/")) {
					tempFolderPath = StringUtils.substring(tempFolderPath, 1);
				}
				String oldFilePath = oldContent.getFilePath();
				if(oldFilePath.startsWith("/")) {
					oldFilePath = oldFilePath.substring(1, oldFilePath.length());
				}
				boolean renamed = amazonS3ClientService.createFolder(content.getContentName(),tempFolderPath);
				boolean download = amazonS3ClientService.downloadDir(oldContent.getFilePath(), sourcePathLocal);
				boolean upload = false;
				if(download) {
					if(renamed) {
						if (dest.listFiles().length > 0) {
							upload = amazonS3ClientService.uploadDir(sourcePathLocal+"/"+oldFilePath, completFolderPath, true);
						}else {
							upload = true;
						}
					}
				}
				if (!upload) {
					setError(redirectAttrs, "Error in Renaming folder");
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				} else {
					amazonS3ClientService.deleteDir(oldFilePath);
					content.setFilePath(completFolderPath);
					setSuccess(redirectAttrs, "Updated SuccessFully");
					contentService.updateChildrenDetails(content.getFilePath(),
							oldFilePath);
				}
				FileUtils.deleteDirectory(dest);
			}else {
				content.setFilePath(completFolderPath);
				if(!content.getContentName().equals(oldContent.getContentName())) {
					setError(redirectAttrs, "Folder Already Exists with name "+content.getContentName());
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				}
			}

			contentService.updateFolder(content);
			
			//List<Content> childContentList = contentService.findByContentId(String.valueOf(content.getId()));
		//	for(Content con: childContentList){
				//con.setAccessType(content.getAccessType());
				contentService.updateAccessTypeById(content.getAccessType(), String.valueOf(content.getId()));
			//}
			
			List<Content> childListForUpdate = new ArrayList<>();
			childListForUpdate = contentService.findByParentModuleId(content
					.getId());
			for (Content c : childListForUpdate) {
				c.setContentName(content.getContentName());
				c.setContentDescription(content.getContentDescription());
				c.setAccessType(content.getAccessType());
				c.setStartDate(content.getStartDate());
				c.setEndDate(content.getEndDate());
				c.setFolderPath(content.getFolderPath());
				c.setLastModifiedBy(content.getLastModifiedBy());
				c.setFilePath(content.getFilePath());
				c.setFacultyId(content.getFacultyId());
				contentService.updateFolder(c);
				if ("Private".equals(c.getAccessType())) {
					studentContentService.deleteByContentId(c.getId());
				}else{
					studentContentService.setActiveByContentId(c.getId());
				}
				int studentContentCount = studentContentService.getNoOfStudentsAllocated(c.getId());
				if(studentContentCount == 0){
					logger.info("id---->"+c.getId());
					if (Content.ACCESS_TYPE_EVERYONE.equals(content
		                    .getAccessType())){
						redirectAttrs.addFlashAttribute("content",c);
		              return "redirect:/saveStudentContentAllocationForAllStudentsForModule";
		            }
				}
			}
			setSuccess(redirectAttrs, "Folder Details updated successfully");
		}catch (ValidationException e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		
		} 
		
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
		
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addFile", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addFile(
			@ModelAttribute Content content,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);
		try {
			

			HtmlValidation.validateHtml(content, new ArrayList<>());

			businessBypassRule.validateAlphaNumeric(content.getContentName());
			
			Course course = new Course();
			
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.parseLong(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			
			
			
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			HtmlValidation.validateHtml(content, new ArrayList<>());
			if (content.getCourseId() == null) {
				content.setCourseId(Long.valueOf(idForCourse));
			}

			performFolderPathCheck(content);

			if (file == null || file.isEmpty()) {
				setError(redirectAttrs,
						"No file selected. Please select a file to upload.");
				return "redirect:/addContentForm";
			}
			//Audit change start
			String errorMessage = "";
			Tika tika = new Tika();
			  String detectedType = tika.detect(file.getBytes());
			if (file.getOriginalFilename().contains(".")) {
				Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
				logger.info("length--->"+count);
				if (count > 1 || count == 0) {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/addContentForm";
				}else {
					String extension = FilenameUtils.getExtension(file.getOriginalFilename());
					logger.info("extension--->"+extension);
					if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
							|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/addContentForm";
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
						errorMessage = uploadContentFileForS3(content, content.getFolderPath(), file);
						} else {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/addContentForm";
						}
					}
				}
			}else {
				setError(redirectAttrs, "File uploaded is invalid!");
				return "redirect:/addContentForm";
			}
			//Audit change end
			
			Course c = null;
			if (!errorMessage.contains("Error")) {
				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					content.setSendEmailAlertToParents("Y");
					content.setSendSmsAlertToParents("Y");
				}
				content.setCreatedBy(p.getName());
				content.setLastModifiedBy(p.getName());
				content.setFacultyId(p.getName());

				if (StringUtils.isEmpty(idForCourse)) {
					idForCourse = content.getIdForCourse();
				}

				if ((content.getCourseId() == null)) {
					if (!StringUtils.isEmpty(idForCourse)) {
						content.setCourseId(Long.valueOf(idForCourse));
					}

				}

				if (!StringUtils.isEmpty(idForCourse)) {
					c = courseService.findByID(Long.valueOf(idForCourse));
					content.setCourseId(Long.valueOf(idForCourse));
				} else
					c = courseService.findByID(content.getCourseId());

				content.setAcadMonth(c.getAcadMonth());
				content.setAcadYear(Integer.valueOf(c.getAcadYear()));

				contentService.insertWithIdReturn(content);
				
			if (Content.ACCESS_TYPE_EVERYONE.equals(content
                    .getAccessType())){
              return "redirect:/saveStudentContentAllocationForAllStudents";
            }

				setSuccess(redirectAttrs, "File Created Successfully");
			} else {
				setError(redirectAttrs, "Error in uploading file "
						+ errorMessage);
				return "redirect:/addContentForm";
			}

		}
		catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs,e.getMessage());
			return "redirect:/addContentForm";
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addFileForModule", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addFileForModule(
			@ModelAttribute Content content,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse,
			@RequestParam(name = "idForModule", required = false, defaultValue = "") String idForModule,
			@RequestParam(name = "acadYear", required = false, defaultValue = "") String acadYear) {
		redirectAttrs.addFlashAttribute("content", content);
		File file1 = null;
		try {
			HtmlValidation.validateHtml(content, new ArrayList<>());
			String username = p.getName();
			performFolderPathCheckForModule(content);
			
			
			

			businessBypassRule.validateNumeric(acadYear);
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course course=new Course();
			//logger.info("--->"+idForCourse);
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.valueOf(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			if (null != idForModule && !idForModule.isEmpty()) {
				businessBypassRule.validateNumeric(idForModule);
				course = courseService.checkIfExistsInDB("moduleId", idForModule);
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			if (idForModule != null) {
				content.setModuleId(idForModule);
			}

			if (acadYear != null) {
				content.setAcadYear(Integer.valueOf(acadYear));
			}

			List<Course> courseIdList = new ArrayList<>();
			if (content.getIdForCourse() == null
					|| content.getIdForModule() == null) {
				content.setAcadMonth(content.getAcadMonth());
				content.setIdForModule(content.getIdForModule());
			}
			courseIdList = courseService.findCoursesByModuleId(
					Long.valueOf(content.getIdForModule()), username, acadYear);

			boolean created = false;

			String acadMonth = courseService.getAcadMonthByModuleIdAndAcadYear(
					idForModule, String.valueOf(content.getAcadYear()));

			Content contentForModule = new Content();
			contentForModule.setContentName(content.getContentName());
			contentForModule.setContentDescription(content
					.getContentDescription());
			contentForModule.setAcadYear(Integer.valueOf(acadYear));
			contentForModule.setContentType(content.getContentType());
			contentForModule.setAccessType(content.getAccessType());
			contentForModule.setStartDate(content.getStartDate());
			contentForModule.setEndDate(content.getEndDate());
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				contentForModule.setSendEmailAlertToParents("Y");
				contentForModule.setSendSmsAlertToParents("Y");
			}
			if (content.getParentContentId() != null) {
				contentForModule.setParentContentId(content
						.getParentContentId());
			}
			contentForModule.setSendSmsAlert(content.getSendSmsAlert());
			contentForModule.setSendEmailAlert(content.getSendEmailAlert());
			contentForModule.setExamViewType(content.getExamViewType());
			contentForModule.setModuleId(content.getIdForModule());
			contentForModule.setAcadMonth(acadMonth);
			contentForModule.setCreatedBy(p.getName());
			contentForModule.setLastModifiedBy(p.getName());
			contentForModule.setFacultyId(p.getName());
			
			String completFolderPath1 = contentForModule.getFolderPath()
					+ contentForModule.getContentName() + FILE_SEPARATOR;
			if (file == null || file.isEmpty()) {
				setError(redirectAttrs,
						"No file selected. Please select a file to upload.");
				return "redirect:/addContentForm";
			}
			//Audit change start
			String errorMessage = "";
			Tika tika = new Tika();
			  String detectedType = tika.detect(file.getBytes());
			if (file.getOriginalFilename().contains(".")) {
				Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
				logger.info("length--->"+count);
				if (count > 1 || count == 0) {
					setError(redirectAttrs, "File uploaded is invalid!");
					return "redirect:/addContentForm";
				}else {
					String extension = FilenameUtils.getExtension(file.getOriginalFilename());
					logger.info("extension--->"+extension);
					if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
							|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
						setError(redirectAttrs, "File uploaded is invalid!");
						return "redirect:/addContentForm";
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
						errorMessage = uploadContentFileForS3(content, content.getFolderPath(), file);
						} else {
							setError(redirectAttrs, "File uploaded is invalid!");
							return "redirect:/addContentForm";
						}
					}
				}
			}else {
				setError(redirectAttrs, "File uploaded is invalid!");
				return "redirect:/addContentForm";
			}
			//Audit change end

			if (!errorMessage.contains("Error")) {
				contentForModule.setFolderPath(content.getFolderPath());
				contentForModule.setFilePath(content.getFilePath());
				contentService.insertWithIdReturn(contentForModule);
				long moduleEntryId = contentForModule.getId();
				for (Course c1 : courseIdList) {

					String idForCourse1 = String.valueOf(c1.getId());
					content.setCourseId(Long.valueOf(idForCourse1));

					Course c = null;

					if (!StringUtils.isEmpty(idForCourse1)) {

						c = courseService.findByID(Long.valueOf(idForCourse1));
						content.setCourseId(Long.valueOf(idForCourse1));
					} else
						c = courseService.findByID(Long.valueOf(idForCourse1));

					if (sendAlertsToParents.equalsIgnoreCase("Y")) {
						content.setSendEmailAlertToParents("Y");
						content.setSendSmsAlertToParents("Y");
					}
					content.setFolderPath(contentForModule.getFolderPath());
					content.setFilePath(contentForModule.getFilePath());
					content.setParentModuleId(String.valueOf(moduleEntryId));
					content.setAcadMonth(c.getAcadMonth());

					content.setModuleId(content.getIdForModule());
					content.setCreatedBy(p.getName());
					content.setLastModifiedBy(p.getName());
					content.setFacultyId(p.getName());

					contentService.insertWithIdReturn(content);
				}
				setSuccess(redirectAttrs, "File Created Successfully");
				if (Content.ACCESS_TYPE_EVERYONE.equals(content
                        .getAccessType())){
					redirectAttrs.addFlashAttribute("content", contentForModule);
                    return "redirect:/saveStudentContentAllocationForAllStudentsForModule";
    }


			} else {
				setError(redirectAttrs, "Error in uploading file "
						+ errorMessage);
				return "redirect:/addContentForm";
			}

		}catch (ValidationException e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			return "redirect:/addContentForm";
		
		} 
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating file");
			return "redirect:/addContentForm";
		}
		content.setCourseId(null);
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateFile", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateFile(
			@ModelAttribute Content content,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);
		try {
			HtmlValidation.validateHtml(content, new ArrayList<>());
			performFolderPathCheck(content);
			
			//businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course course=new Course();
			//logger.info("--->"+idForCourse);
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.valueOf(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			

			if (file != null && !file.isEmpty()) {
				Content c = contentService.findByID(content.getId());
				String oldFilePath = c.getFilePath();
				//Audit change start
				String errorMessage = "";
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(o -> o == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						redirectAttrs.addFlashAttribute("edit", "true");
						return "redirect:/addContentForm";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
								|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							redirectAttrs.addFlashAttribute("edit", "true");
							return "redirect:/addContentForm";
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
							errorMessage = uploadContentFileForS3(content,
								content.getFolderPath(), file);
							} else {
								setError(redirectAttrs, "File uploaded is invalid!");
								redirectAttrs.addFlashAttribute("edit", "true");
								return "redirect:/addContentForm";
							}
						}
					}
				}else {
					setError(redirectAttrs, "File uploaded is invalid!");
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				}
				//Audit change end
				String newFilePath = content.getFilePath();
					
					if(oldFilePath.startsWith("/")) {
						oldFilePath = StringUtils.substring(oldFilePath, 1);
					}
					amazonS3ClientService.deleteFileFromS3Bucket(oldFilePath);
				if (errorMessage.contains("Error")) {
					setError(redirectAttrs, "Error in uploading file "
							+ errorMessage);
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				}
			}
			content.setLastModifiedBy(p.getName());

			Course c = null;
			if (StringUtils.isEmpty(idForCourse)) {
				idForCourse = content.getIdForCourse();
			}

			if ((content.getCourseId() == null)) {
				if (!StringUtils.isEmpty(idForCourse)) {
					content.setCourseId(Long.valueOf(idForCourse));
				}

			}
			if (!StringUtils.isEmpty(idForCourse)) {
				c = courseService.findByID(Long.valueOf(idForCourse));
				content.setCourseId(Long.valueOf(idForCourse));
			} else
				c = courseService.findByID(content.getCourseId());

			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				content.setSendEmailAlertToParents("Y");
				content.setSendSmsAlertToParents("Y");
			}

			logger.info("=======================:?" + c.getAcadMonth());
			logger.info("=======================:?" + c.getAcadYear());
			content.setAcadMonth(c.getAcadMonth());
			content.setAcadYear(Integer.valueOf(c.getAcadYear()));

			contentService.updateFile(content);

			if ("Private".equals(content.getAccessType())) {
				studentContentService.deleteByContentId(content.getId());
			}else{
				studentContentService.setActiveByContentId(content.getId());
			}
			int studentContentCount = studentContentService.getNoOfStudentsAllocated(content.getId());
			if(studentContentCount == 0){
				if (Content.ACCESS_TYPE_EVERYONE.equals(content
	                    .getAccessType())){
	              return "redirect:/saveStudentContentAllocationForAllStudents";
	            }
			}
			setSuccess(redirectAttrs, "File Updated Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating file");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		}
		//return "redirect:/getContentUnderAPathForFaculty";
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateFileForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updateFileForModule(
			@ModelAttribute Content content,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);
		try {
			HtmlValidation.validateHtml(content, new ArrayList<>());
			performFolderPathCheck(content);
			
			//businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course course=new Course();
			//logger.info("--->"+idForCourse);
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.valueOf(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			Content contentDB = contentService.findByID(content.getId());
			if (file != null && !file.isEmpty()) {
				
				String oldFilePath = contentDB.getFilePath();
				//Audit change start
				String errorMessage = "";
				Tika tika = new Tika();
				  String detectedType = tika.detect(file.getBytes());
				if (file.getOriginalFilename().contains(".")) {
					Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
					logger.info("length--->"+count);
					if (count > 1 || count == 0) {
						setError(redirectAttrs, "File uploaded is invalid!");
						redirectAttrs.addFlashAttribute("edit", "true");
						return "redirect:/addContentForm";
					}else {
						String extension = FilenameUtils.getExtension(file.getOriginalFilename());
						logger.info("extension--->"+extension);
						if(extension.equalsIgnoreCase("exe") || extension.equalsIgnoreCase("php") || extension.equalsIgnoreCase("java") 
								|| ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
							setError(redirectAttrs, "File uploaded is invalid!");
							redirectAttrs.addFlashAttribute("edit", "true");
							return "redirect:/addContentForm";
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
							errorMessage = uploadContentFileForS3(content,
									content.getFolderPath(), file);
							} else {
								setError(redirectAttrs, "File uploaded is invalid!");
								redirectAttrs.addFlashAttribute("edit", "true");
								return "redirect:/addContentForm";
							}
						}
					}
				}else {
					setError(redirectAttrs, "File uploaded is invalid!");
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				}
				//Audit change end
				
				String newFilePath = content.getFilePath();
					if(oldFilePath.startsWith("/")) {
						oldFilePath = StringUtils.substring(oldFilePath, 1);
					}
					amazonS3ClientService.deleteFileFromS3Bucket(oldFilePath);
				if (errorMessage.contains("Error")) {
					setError(redirectAttrs, "Error in uploading file "
							+ errorMessage);
					redirectAttrs.addFlashAttribute("edit", "true");
					return "redirect:/addContentForm";
				}
				if(contentDB.getContentType().equals("Multiple_File")) {
					content.setContentName(errorMessage);
				}else {
					content.setContentName(contentDB.getContentName());
				}
			}else{
                content.setContentName(contentDB.getContentName());
			}

			content.setLastModifiedBy(p.getName());

			if (StringUtils.isEmpty(idForCourse)) {
				idForCourse = content.getIdForCourse();
			}

			if ((content.getCourseId() == null)) {
				if (!StringUtils.isEmpty(idForCourse)) {
					content.setCourseId(Long.valueOf(idForCourse));
				}

			}
			if (content.getParentContentId() != null) {
				content.setParentContentId(content.getParentContentId());
			}
			contentService.updateFile(content);
			
			List<Content> childListForUpdate = new ArrayList<>();
			childListForUpdate = contentService.findByParentModuleId(content
					.getId());

			for (Content c : childListForUpdate) {
				c.setAcadYear(content.getAcadYear());
				c.setContentName(content.getContentName());
				c.setContentDescription(content.getContentDescription());
				c.setAccessType(content.getAccessType());
				c.setStartDate(content.getStartDate());
				c.setEndDate(content.getEndDate());
				c.setFolderPath(content.getFolderPath());
				c.setLastModifiedBy(content.getLastModifiedBy());
				c.setFilePath(content.getFilePath());
				c.setFacultyId(content.getFacultyId());
				contentService.updateFile(c);
				if ("Private".equals(c.getAccessType())) {
					studentContentService.deleteByContentId(c.getId());
				}else{
					studentContentService.setActiveByContentId(c.getId());
				}
				int studentContentCount = studentContentService.getNoOfStudentsAllocated(c.getId());
				if(studentContentCount == 0){
					if (Content.ACCESS_TYPE_EVERYONE.equals(content
		                    .getAccessType())){
						redirectAttrs.addFlashAttribute("content",c);
		              return "redirect:/saveStudentContentAllocationForAllStudentsForModule";
		            }
				}
			}
			setSuccess(redirectAttrs, "File Updated Successfully");

		} catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			return "redirect:/addContentForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating file");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLink", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addLink(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {
		redirectAttrs.addFlashAttribute("content", content);
		try {

			HtmlValidation.validateHtml(content, new ArrayList<>());

			
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			
			Course course = new Course();
			
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.parseLong(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}
			
			businessBypassRule.validateaccesstype(content.getAccessType());
			utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			

			logger.info("Contnet idForCourse--------------><" + idForCourse);
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				content.setSendEmailAlertToParents("Y");
				content.setSendSmsAlertToParents("Y");
			}
			if (content.getCourseId() == null) {
				logger.info("Contnet Add Folder--------------><"
						+ content.getCourseId());
				content.setCourseId(Long.valueOf(idForCourse));
			}
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			performFolderPathCheck(content);
			Course c = null;
			if (StringUtils.isEmpty(idForCourse)) {
				idForCourse = content.getIdForCourse();
			}
			if (!StringUtils.isEmpty(idForCourse)) {
				c = courseService.findByID(Long.valueOf(idForCourse));
				content.setCourseId(Long.valueOf(idForCourse));
			} else
				c = courseService.findByID(content.getCourseId());

			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				content.setSendEmailAlertToParents("Y");
				content.setSendSmsAlertToParents("Y");
			}

			content.setAcadMonth(c.getAcadMonth());
			content.setAcadYear(Integer.valueOf(c.getAcadYear()));
			content.setCreatedBy(p.getName());
			content.setLastModifiedBy(p.getName());
			content.setFacultyId(p.getName());

			contentService.insertWithIdReturn(content);
			
		if (Content.ACCESS_TYPE_EVERYONE.equals(content
                .getAccessType())){
                return "redirect:/saveStudentContentAllocationForAllStudents";
        }

			setSuccess(redirectAttrs, "Link added Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating Link");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addLinkForModule", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addLinkForModule(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForModule", required = false, defaultValue = "") String idForModule,
			@RequestParam(name = "acadYear", required = false, defaultValue = "") String acadYear) {
		redirectAttrs.addFlashAttribute("content", content);
		try {

			HtmlValidation.validateHtml(content, new ArrayList<>());

			businessBypassRule.validateNumeric(acadYear);
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course course = new Course();
			
			if (null != idForModule && !idForModule.isEmpty()) {
				businessBypassRule.validateNumeric(idForModule);
				course = courseService.checkIfExistsInDB("moduleId", idForModule);
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}

			businessBypassRule.validateaccesstype(content.getAccessType());

			utils.validateStartAndEndDates(content.getStartDate(),
					content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
		

			String username = p.getName();
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(content.getStartDate(), content.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addContentForm";
//			}
			/* New Audit changes end */
			List<Course> courseIdList = new ArrayList<>();
			courseIdList = courseService.findCoursesByModuleId(
					Long.valueOf(idForModule), username, acadYear);

			String acadMonth = courseService.getAcadMonthByModuleIdAndAcadYear(
					idForModule, String.valueOf(content.getAcadYear()));

			Content contentForModule = new Content();
			contentForModule.setContentName(content.getContentName());
			contentForModule.setContentDescription(content
					.getContentDescription());
			contentForModule.setAcadYear(Integer.valueOf(acadYear));
			contentForModule.setContentType(content.getContentType());
			contentForModule.setAccessType(content.getAccessType());
			contentForModule.setStartDate(content.getStartDate());
			contentForModule.setEndDate(content.getEndDate());
			contentForModule.setIsCourseRootFolder(content
					.getIsCourseRootFolder());
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				contentForModule.setSendEmailAlertToParents("Y");
				contentForModule.setSendSmsAlertToParents("Y");
			}
			contentForModule.setSendSmsAlert(content.getSendSmsAlert());
			contentForModule.setSendEmailAlert(content.getSendEmailAlert());
			contentForModule.setExamViewType(content.getExamViewType());
			contentForModule.setModuleId(idForModule);

			contentForModule.setCreatedBy(p.getName());
			contentForModule.setLastModifiedBy(p.getName());
			contentForModule.setFacultyId(p.getName());
			contentForModule.setFolderPath(courseRootFolder + idForModule
					+ FILE_SEPARATOR);
			contentForModule.setLinkUrl(content.getLinkUrl());
			contentForModule.setAcadMonth(acadMonth);
			contentService.insertWithIdReturn(contentForModule);
			long moduleEntryId = contentForModule.getId();

			for (Course c1 : courseIdList) {

				String idForCourse1 = String.valueOf(c1.getId());
				content.setCourseId(Long.valueOf(idForCourse1));
				performFolderPathCheckForModule(content);

				Course c = null;

				if (!StringUtils.isEmpty(idForCourse1)) {

					c = courseService.findByID(Long.valueOf(idForCourse1));
					content.setCourseId(Long.valueOf(idForCourse1));
				} else
					c = courseService.findByID(Long.valueOf(idForCourse1));

				if (sendAlertsToParents.equalsIgnoreCase("Y")) {
					content.setSendEmailAlertToParents("Y");
					content.setSendSmsAlertToParents("Y");
				}

				content.setFolderPath(contentForModule.getFolderPath());
				content.setParentModuleId(String.valueOf(moduleEntryId));
				content.setAcadMonth(c.getAcadMonth());
				content.setAcadYear(Integer.valueOf(c.getAcadYear()));
				content.setModuleId(idForModule);
				content.setCreatedBy(p.getName());
				content.setLastModifiedBy(p.getName());
				content.setFacultyId(p.getName());

				contentService.insertWithIdReturn(content);
			}
			setSuccess(redirectAttrs, "Link added Successfully");

			if (Content.ACCESS_TYPE_EVERYONE.equals(content
                    .getAccessType())){
				redirectAttrs.addFlashAttribute("content", contentForModule);	
                return "redirect:/saveStudentContentAllocationForAllStudentsForModule";
			}


		} catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			return "redirect:/addContentForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating Link");
			return "redirect:/addContentForm";
		}
		content.setCourseId(null);
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateLink", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateLink(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse) {

		redirectAttrs.addFlashAttribute("content", content);
		try {
			HtmlValidation.validateHtml(content, new ArrayList<>());
			
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course course = new Course();
			
			if (null != idForCourse && !idForCourse.isEmpty()) {
				businessBypassRule.validateNumeric(idForCourse);
				course = courseService.findByID(Long.parseLong(idForCourse));
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}

			businessBypassRule.validateaccesstype(content.getAccessType());

			utils.validateStartAndEndDates(content.getStartDate(),
					content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			performFolderPathCheck(content);
			Course c = null;
			content.setLastModifiedBy(p.getName());
			if (StringUtils.isEmpty(idForCourse)) {
				idForCourse = content.getIdForCourse();
			}

			if ((content.getCourseId() == null)) {
				if (!StringUtils.isEmpty(idForCourse)) {
					content.setCourseId(Long.valueOf(idForCourse));
				}

			}
			c = courseService.findByID(content.getCourseId());
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				content.setSendEmailAlertToParents("Y");
				content.setSendSmsAlertToParents("Y");
			}

			content.setAcadMonth(c.getAcadMonth());
			content.setAcadYear(Integer.valueOf(c.getAcadYear()));
			contentService.updateLink(content);
			if ("Private".equals(content.getAccessType())) {
				studentContentService.deleteByContentId(content.getId());
			}else{
				studentContentService.setActiveByContentId(content.getId());
			}
			int studentContentCount = studentContentService.getNoOfStudentsAllocated(content.getId());
			if(studentContentCount == 0){
				if (Content.ACCESS_TYPE_EVERYONE.equals(content
	                    .getAccessType())){
	              return "redirect:/saveStudentContentAllocationForAllStudents";
	            }
			}
			setSuccess(redirectAttrs, "Link updated Successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Link");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/updateLinkForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updateLinkForModule(
			@ModelAttribute Content content,
			RedirectAttributes redirectAttrs,
			Principal p,
			@RequestParam(name = "idForCourse", required = false, defaultValue = "") String idForCourse,
			@RequestParam(name = "idForModule", required = false, defaultValue = "") String idForModule,
			@RequestParam(name = "acadYear", required = false, defaultValue = "") String acadYear) {

		redirectAttrs.addFlashAttribute("content", content);

		try {
			HtmlValidation.validateHtml(content, new ArrayList<>());
			
			businessBypassRule.validateAlphaNumeric(content.getContentName());
			Course course = new Course();
			
			if (null != idForModule && !idForModule.isEmpty()) {
				businessBypassRule.validateNumeric(idForModule);
				course = courseService.checkIfExistsInDB("moduleId", idForModule);
				if (null == course) {
					throw new ValidationException("Invalid course");
				}
			}

			businessBypassRule.validateaccesstype(content.getAccessType());

			logger.info("startDate---->" + content.getStartDate());
			logger.info("endDate---->" + content.getEndDate());
			
			utils.validateStartAndEndDates(content.getStartDate(),
					content.getEndDate());
			businessBypassRule.validateYesOrNo(content.getSendEmailAlert());
			businessBypassRule.validateYesOrNo(content.getSendSmsAlert());
			businessBypassRule.validateYesOrNo(content.getExamViewType());
			
			
			String username = p.getName();
			Content contentForModule = new Content();

			if (content.getIdForModule() == null) {
				content.setIdForModule(content.getModuleId());

			}

			List<Content> parentList = new ArrayList<>();
			parentList = contentService.findparentBychildId(content.getId());

			contentForModule.setModuleId(content.getModuleId());
			contentForModule.setLastModifiedBy(content.getLastModifiedBy());

			performFolderPathCheck(content);
			content.setLastModifiedBy(p.getName());
			if (StringUtils.isEmpty(idForCourse)) {
				idForCourse = content.getIdForCourse();
			}

			if (StringUtils.isEmpty(idForCourse)) {
				idForModule = content.getIdForModule();
			}

			if ((content.getCourseId() == null)) {
				if (!StringUtils.isEmpty(idForCourse)) {
					content.setCourseId(Long.valueOf(idForCourse));
				}

			}

			if ((content.getModuleId() == null)) {
				if (!StringUtils.isEmpty(idForModule)) {
					content.setModuleId(idForModule);
				}

			}
			Long moduleEntryId = contentForModule.getId();
			Course c = null;
			for (Content ct : parentList) {
				content.setAcadMonth(c.getAcadMonth());
				content.setModuleId(idForModule);
				content.setCreatedBy(p.getName());
				content.setLastModifiedBy(p.getName());
				content.setFacultyId(p.getName());
				contentService.updateLink(content);
				
			}

			contentService.updateLink(content);
			if ("Private".equals(content.getAccessType())) {
				studentContentService.deleteByContentId(content.getId());
			}else{
				studentContentService.setActiveByContentId(content.getId());
			}
			int studentContentCount = studentContentService.getNoOfStudentsAllocated(content.getId());
			if(studentContentCount == 0){
				if (Content.ACCESS_TYPE_EVERYONE.equals(content
	                    .getAccessType())){
					redirectAttrs.addFlashAttribute("content",content);
	              return "redirect:/saveStudentContentAllocationForAllStudentsForModule";
	            }
			}
			setSuccess(redirectAttrs, "Link updated Successfully");

		} catch (ValidationException e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, e.getMessage());
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in updating Link");
			redirectAttrs.addFlashAttribute("edit", "true");
			return "redirect:/addContentForm";
		}
		return "redirect:/getContentUnderAPathForFacultyForModule";
	}

	private String uploadContentFile(Content content, String folderPath,

	MultipartFile file) {

		String errorMessage = null;

		InputStream inputStream = null;

		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();

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

			String filePath = folderPath + fileName;

			// Check if Folder exists. If not then create

			File directory = new File(folderPath);

			if (!directory.exists()) {

				directory.mkdirs();

			}

			File newFile = new File(filePath);

			outputStream = new FileOutputStream(newFile);

			IOUtils.copy(inputStream, outputStream);

			content.setFilePath(filePath);
			errorMessage = fileName;

		} catch (IOException e) {

			errorMessage = "Error in uploading Content file : "

			+ e.getMessage();

			logger.error("Exception", e);

		} finally {

			if (outputStream != null)

				IOUtils.closeQuietly(outputStream);

			if (inputStream != null)

				IOUtils.closeQuietly(inputStream);

		}

		return errorMessage;

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/getContentUnderAPath", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getContentUnderAPath(@ModelAttribute Content content,
			Model m, Long courseId, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("contentList", "View Content",
				true, false));
		m.addAttribute("content", content);
		Token userDetails = (Token) principal;
		String progName = userDetails.getProgramName();
		m.addAttribute("allCourses",
				courseService.findByCoursesBasedOnProgramName(progName));

		if (content.getCourseId() == null) {
			// If Admin wants to see content for all courses
			content.setFolderPath(courseRootFolder);
		} else if (content.getFolderPath() == null) {
			// If path is not present
			content.setFolderPath(courseRootFolder + "" + content.getCourseId()
					+ FILE_SEPARATOR);
		}
		try {

			String am = content.getAcadMonth();
			Integer ay = content.getAcadYear();

			if (am == null && ay == null) {
				if (courseId != null) {
					Course c = courseService.findByID(courseId);
					am = c.getAcadMonth();
					ay = Integer.valueOf(c.getAcadYear());

					content.setAcadMonth(am);
					content.setAcadYear(ay);
				}
			}
			content.setCreatedBy(username);
			List<Content> allContent = contentService
					.getContentUnderAPath(content);
			List<Content> everyoneContentList = contentService
					.findEveryoneContentByCourseId(courseId);
			if (everyoneContentList != null || everyoneContentList.size() != 0) {
				allContent.addAll(everyoneContentList);
				Collections.sort(allContent, new SortByCreatedDate());
			}
			if (allContent == null || allContent.size() == 0) {
				m.addAttribute("size", 0);
			} else {
				m.addAttribute("courseId", courseId);
				m.addAttribute("allContent", allContent);
				m.addAttribute("size", allContent.size());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "content/contentListAdmin";

		} else {
			return "content/contentList";
		}
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/getContentUnderAPathForFaculty", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getContentUnderAPathForFaculty(Principal principal,
			@ModelAttribute Content content, Model m) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("contentList", "View Content",
				true, false));
		String acadMonth = "";
		String acadYear = "";
		m.addAttribute("content", content);
		m.addAttribute(
				"allCourses",
				courseService.findByUser(principal.getName(),
						Long.parseLong(userdetails1.getProgramId())));

		if (content.getCourseId() == null) {
			// If Admin wants to see content for all courses
			content.setFolderPath(courseRootFolder);
		} else if (content.getFolderPath() == null) {
			// If path is not present
			content.setFolderPath(courseRootFolder + "" + content.getCourseId()
					+ FILE_SEPARATOR);
		}
		try {

			content.setCreatedBy(username);
			List<Content> allContent = contentService
					.getContentUnderAPath(content);
			if (allContent == null || allContent.size() == 0) {
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allContent);
				m.addAttribute("size", allContent.size());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		return "content/facultyContentList";
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/getContentUnderAPathForFacultyForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getContentUnderAPathForFacultyForModule(Principal principal,
			@ModelAttribute Content content, Model m) {
		Calendar c = Calendar.getInstance();
        c.setTime(Utils.getInIST());
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR) - 1;
        int currentYear = c.get(Calendar.YEAR);
        List<Integer> currYear = new ArrayList<Integer>();
        if(month>6){
        	currYear.add(currentYear);
        	m.addAttribute("currentYear",currYear);
        }else{
        	currYear.add(currentYear);
        	currYear.add(year);
        	m.addAttribute("currentYear",currYear);
        }
        
        
		if (content.getIdForModule() != null) {
			content.setModuleId(content.getIdForModule());
		}
		if (content.getCourseIdForSearch() != null) {
			content.setCourseId(Long.valueOf(content.getCourseIdForSearch()));
		}
		if (content.getAcadYear() == null) {
			content.setModuleId(null);
			content.setCourseId(null);
		}
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("contentList", "View Content",
				true, false));
		String acadMonth = "";
		String acadYear = "";
		List<String> academicYear = courseService.getAllAcadYear();
		m.addAttribute("acadYear", academicYear);
		m.addAttribute("content", content);

		if (content.getAcadYear() != null) {
			List<Course> moduleList = courseService.findModulesByUsername(
					username, String.valueOf(content.getAcadYear()),
					Long.parseLong(userdetails1.getProgramId()));
			m.addAttribute("modules", moduleList);
			if (content.getModuleId() != null
					&& !content.getModuleId().isEmpty()) {
				m.addAttribute(
						"allCourses",
						courseService.findCoursesByModuleId(
								Long.valueOf(content.getModuleId()),
								principal.getName(),
								String.valueOf(content.getAcadYear()),userdetails1.getProgramId()));
			                                                
			}else{
                m.addAttribute(
                        "allCourses",
                        courseService.findCoursesByAcadYear(principal.getName(),
                                                        Long.parseLong(userdetails1.getProgramId()),String.valueOf(content.getAcadYear())));
}

		}
		if (content.getFolderPath() == null)
		{
		if (content.getCourseId() == null) {
			// If Admin wants to see content for all
			if (content.getModuleId() == null) {
				content.setFolderPath(courseRootFolderS3);
			} else {
				content.setFolderPath(courseRootFolderS3 + ""
						+ content.getModuleId() + FILE_SEPARATOR);
			}
		} else if (content.getCourseId() != null) {
			content.setFolderPath(courseRootFolderS3 + "" + content.getCourseId()
					+ FILE_SEPARATOR);
		} else {
			// If path is not present
			content.setFolderPath(courseRootFolderS3);
		}
		}

		try {
			content.setCreatedBy(username);
			
			List<Content> allContent = contentService
					.getContentUnderAPathForModule(content);
			if (allContent == null || allContent.size() == 0) {
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allContent);
				m.addAttribute("size", allContent.size());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content");
		}
		return "content/facultyContentListForModule";
	}

	private void performFolderPathCheck(Content content) {

		if (content.getParentContentId() == null) {
			// If path is not present, start with Course Root Folder

			content.setFolderPath(courseRootFolder + "" + content.getCourseId()
					+ FILE_SEPARATOR);

		}

		if (!content.getFolderPath().endsWith(FILE_SEPARATOR)) {
			content.setFolderPath(content.getFolderPath() + FILE_SEPARATOR);
		}

	}

	private void performFolderPathCheckForModule(Content content) {
		if (content.getParentContentId() == null) {
			content.setFolderPath(courseRootFolder + content.getIdForModule()
					+ FILE_SEPARATOR);
		}
		if (!content.getFolderPath().endsWith(FILE_SEPARATOR)) {
			content.setFolderPath(content.getFolderPath() + FILE_SEPARATOR);
		}

	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/viewContent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewContent(@ModelAttribute Content content, Model m,
			@RequestParam(required = false) Long campusId, Principal p) {
		m.addAttribute("webPage", new WebPage("content", "Create Content",
				true, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("id", content.getId());
		String contentType = content.getContentType();
		if (content != null && content.getId() != null) {
			content = contentService.findByID(content.getId());
		}
		m.addAttribute("allCampuses", userService.findCampus());

		m.addAttribute("content", content);

		if (Content.FOLDER.equalsIgnoreCase(contentType)) {
			List<StudentContent> students = new ArrayList<StudentContent>();

			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusId(content.getId(),
								content.getCourseId(), campusId);
			} else {
				students = studentContentService.getStudentsForContent(
						content.getId(), content.getCourseId());
			}

			for (StudentContent uc : students) {
				User u1 = userService.findByUserName(uc.getUsername());
				uc.setRollNo(u1.getRollNo());
				students.set(students.indexOf(uc), uc);
			}

			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocated(content.getId()));

			return "content/contentFolder";
		} else if (Content.FILE.equalsIgnoreCase(contentType)) {
			List<StudentContent> students = new ArrayList<StudentContent>();

			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusId(content.getId(),
								content.getCourseId(), campusId);
			} else {

				students = studentContentService.getStudentsForContent(
						content.getId(), content.getCourseId());
			}

			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocated(content.getId()));
			return "content/contentFile";
		} else if (Content.LINK.equalsIgnoreCase(contentType)) {
			List<StudentContent> students = new ArrayList<StudentContent>();

			if (campusId != null) {
				content.setCampusId(campusId);
				students = studentContentService
						.getStudentsForContentAndCampusId(content.getId(),
								content.getCourseId(), campusId);
			} else {

				students = studentContentService.getStudentsForContent(
						content.getId(), content.getCourseId());
			}
			m.addAttribute("students", students);
			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocated(content.getId()));
			return "content/contentLink";
		} else if (Content.YOUTUBE_VIDEO.equalsIgnoreCase(contentType)) {

			return "content/contentYoutubeVideo";
		} else {
			m.addAttribute("noOfStudentAllocated", studentContentService
					.getNoOfStudentsAllocated(content.getId()));
			return "content/contentFolder";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveStudentContentAllocationForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentContentAllocationForModule(
			@ModelAttribute Content content, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("content", "Share Folder", true,
				false));
		Content contentDb = contentService.findByID(content.getId());
		ArrayList<StudentContent> studentContentMappingList = new ArrayList<StudentContent>();
		String subject = "Content with name  " + contentDb.getContentName();
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();

		// m.addAttribute("allCampuses", userService.findCampus());
		try {
			/*
			 * Course course = content.getCourseId() != null ? courseService
			 * .findByID(Long.valueOf(content.getCourseId())) : null;
			 * StringBuffer buff = new StringBuffer(subject);
			 * 
			 * logger.info("Content Check--------->"+content.getCourseId());
			 * 
			 * 
			 * if (course != null) { buff.append("for Course ");
			 * buff.append(course.getCourseName()); }
			 */
			Course course = content.getModuleId() != null ? courseService
					.findByModuleIdAndAcadYear(
							String.valueOf(content.getModuleId()),
							String.valueOf(content.getAcadYear())) : null;
			StringBuffer buff = new StringBuffer(subject);


			if (course != null) {
				buff.append("for Module ");
				buff.append(course.getModuleName());
			}

			buff.append(" shared with you");
			subject = buff.toString();
			if (content.getStudents() != null
					&& content.getStudents().size() > 0) {
				businessBypassRule.validateStudentAllocationListForModule(content.getStudents());
				for (String studentUsername : content.getStudents()) {
					StudentContent bean = new StudentContent();

					String[] sUsername = null;

					if (studentUsername.contains("_")) {
						sUsername = studentUsername.split("_");
						bean.setUsername(sUsername[0]);
						bean.setCourseId(Long.valueOf(sUsername[1]));
					}

					bean.setAcadMonth(content.getAcadMonth());
					bean.setAcadYear(content.getAcadYear());

					Content ChildContentId = new Content();
					ChildContentId = contentService
							.findIdByCourseIdAndParentModuleId(sUsername[1],
									String.valueOf(content.getId()));
					bean.setContentId(ChildContentId.getId());
					bean.setCreatedBy(p.getName());
					bean.setLastModifiedBy(p.getName());
					studentList.add(studentUsername);

					if (bean.getUsername().equals(studentUsername)) {

					} 
					studentContentMappingList.add(bean);

				}

				studentContentService.insertBatch(studentContentMappingList);

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(contentDb.getSendEmailAlertToParents())) {
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

					if ("Y".equals(contentDb.getSendSmsAlertToParents())) {
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
					if ("Y".equals(contentDb.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(contentDb.getSendSmsAlert())) {
						if (result != null)

							result = userService
									.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}


				if (Content.FILE.equalsIgnoreCase(content.getContentType())) {
					setSuccess(m, "File shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else if (Content.FOLDER.equalsIgnoreCase(content
						.getContentType())) {
					setSuccess(m, "Folder shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else if (Content.LINK.equalsIgnoreCase(content
						.getContentType())) {
					setSuccess(m, "Link shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else if (Content.MULTIPLE_FILE.equalsIgnoreCase(content
						.getContentType())) {
					setSuccess(m, "MultiFile shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else {
					setSuccess(m, "shared with " + content.getStudents().size()
							+ " students successfully");
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in sharing folder");
		}
		m.addAttribute("content", content);
		return viewContentForModule(content, m, null, p, content.getId(),
				Long.valueOf(content.getModuleId()),
				String.valueOf(content.getAcadYear()));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveStudentContentAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentContentAllocation(@ModelAttribute Content content,
			Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("content", "Share Folder", true,
				false));
		Content contentDb = contentService.findByID(content.getId());
		ArrayList<StudentContent> studentContentMappingList = new ArrayList<StudentContent>();
		String subject = "Content with name  " + contentDb.getContentName();
		List<String> studentList = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		try {
			Course course = content.getCourseId() != null ? courseService
					.findByID(Long.valueOf(content.getCourseId())) : null;
			StringBuffer buff = new StringBuffer(subject);

			if (course != null) {
				buff.append("for Course ");
				buff.append(course.getCourseName());
			}
			buff.append(" shared with you");
			subject = buff.toString();
			if (content.getStudents() != null
					&& content.getStudents().size() > 0) {
				
				businessBypassRule.validateStudentAllocationList(content.getStudents(), content.getCourseId().toString());
				
				for (String studentUsername : content.getStudents()) {
					StudentContent bean = new StudentContent();
					bean.setAcadMonth(content.getAcadMonth());
					bean.setAcadYear(content.getAcadYear());
					bean.setContentId(content.getId());
					bean.setCourseId(content.getCourseId());
					bean.setUsername(studentUsername);
					bean.setCreatedBy(p.getName());
					bean.setLastModifiedBy(p.getName());
					studentContentMappingList.add(bean);
					studentList.add(studentUsername);
				}

				studentContentService.insertBatch(studentContentMappingList);

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(contentDb.getSendEmailAlertToParents())) {
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

					if ("Y".equals(contentDb.getSendSmsAlertToParents())) {
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
					if ("Y".equals(contentDb.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(contentDb.getSendSmsAlert())) {
						if (result != null)

							result = userService
									.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}


				if (Content.FILE.equalsIgnoreCase(content.getContentType())) {
					setSuccess(m, "File shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else if (Content.FOLDER.equalsIgnoreCase(content
						.getContentType())) {
					setSuccess(m, "Folder shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else if (Content.LINK.equalsIgnoreCase(content
						.getContentType())) {
					setSuccess(m, "Link shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else if (Content.MULTIPLE_FILE.equalsIgnoreCase(content
						.getContentType())) {
					setSuccess(m, "MultiFile shared with "
							+ content.getStudents().size()
							+ " students successfully");
				} else {
					setSuccess(m, "shared with " + content.getStudents().size()
							+ " students successfully");
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in sharing folder");
		}
		m.addAttribute("content", content);
		return viewContent(content, m, null, p);
	}

	@Secured({ "ROLE_FACULTY" })
	@RequestMapping(value = "/deleteContent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteContent(@ModelAttribute Content content, Principal p,
			RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("content", content);
		String username = p.getName();
		Token userdetails1 = (Token) p;
		try {
			Content oldContent = contentService.findByID(content.getId());
			content = contentService.findByID(content.getId());
			List<Content> childList = new ArrayList<>();
			childList = contentService.findByParentModuleId(content.getId());
			if (Content.FOLDER.equalsIgnoreCase(content.getContentType())) {
				// Mark Folder and its Children Inactive in DB
				//contentService.softDeleteFolder(content);
				List<Content> filesOfFolder = contentService.findAllByParentContentId(content);
				contentService.softDeleteFolderForModule(content);
				for(Content c:filesOfFolder) {
					if(c.getContentType().equals("File") || c.getContentType().equals("Multiple_File")) {
						String oldPath =c.getFilePath();
						int index = oldPath.lastIndexOf(".");
						String newPath = oldPath.substring(0,index) + "_Deleted"+oldPath.substring(index);
						boolean copy = amazonS3ClientService.copyObject(oldPath, newPath);
						if(copy)
							amazonS3ClientService.deleteFileFromS3Bucket(oldPath);
						c.setFilePath(newPath);
						
						contentService.updateFile(c);
						contentService.deleteSoftById(String.valueOf(c.getId()));
						contentService.deleteSoftByParentModuleId(String.valueOf(c.getId()));
					}
				}
				// Rename folder with _Deleted in the name
				
				//delete from shared school
				if(null != content.getSchoolToExport()) {
					if(!content.getSchoolToExport().isEmpty()) {
						String schoolName[] = content.getSchoolToExport().split(",");
						for(int i=0;i<schoolName.length; i++){
							String apiUrl = serverURL + schoolName[i].replace(" ","")+"/api/deleteShareContent";
							
							try {
								ObjectMapper Objmapper = new ObjectMapper();
								String contentJson = Objmapper.writeValueAsString(oldContent);
								WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
								Invocation.Builder invocationBuilder = webTarget.request();
								Response response = invocationBuilder.post(Entity.entity(
										contentJson.toString(), MediaType.APPLICATION_JSON));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								logger.error("Exception", e);
							}
						}
					}
				}
			} else if (Content.FILE.equalsIgnoreCase(content.getContentType())) {
				
				
				String oldPath =content.getFilePath();
				int index = oldPath.lastIndexOf(".");
				String newPath = oldPath.substring(0,index) + "_Deleted"+oldPath.substring(index);
				boolean copy = amazonS3ClientService.copyObject(oldPath, newPath);
				if(copy)
					amazonS3ClientService.deleteFileFromS3Bucket(oldPath);
				content.setFilePath(newPath);
				
				contentService.updateFile(content);
				for(Content c:childList){
					c.setFilePath(newPath);
					
					contentService.updateFile(c);
				}
				contentService.deleteSoftById(String.valueOf(content.getId()));
				contentService.deleteSoftByParentModuleId(String.valueOf(content.getId()));
				
				//delete from shared school
				if(null != content.getSchoolToExport()) {
					if(!content.getSchoolToExport().isEmpty()) {
						String schoolName[] = content.getSchoolToExport().split(",");
						for(int i=0;i<schoolName.length; i++){
							String apiUrl = serverURL + schoolName[i].replace(" ","")+"/api/deleteShareContent";
							
							try {
								ObjectMapper Objmapper = new ObjectMapper();
								String contentJson = Objmapper.writeValueAsString(oldContent);
								WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
								Invocation.Builder invocationBuilder = webTarget.request();
								Response response = invocationBuilder.post(Entity.entity(
										contentJson.toString(), MediaType.APPLICATION_JSON));
							} catch (Exception e) {
								logger.error("Exception", e);
							}
						}
					}
				}
			} else if (Content.LINK.equalsIgnoreCase(content.getContentType())) {

				contentService.deleteSoftById(String.valueOf(content.getId()));
		
					contentService.deleteSoftByParentModuleId(String.valueOf(content.getId()));
					//delete from shared school
					if(null != content.getSchoolToExport()) {
						if(!content.getSchoolToExport().isEmpty()) {
							String schoolName[] = content.getSchoolToExport().split(",");
							for(int i=0;i<schoolName.length; i++){
								String apiUrl = serverURL + schoolName[i].replace(" ","")+"/api/deleteShareContent";
								
								try {
									ObjectMapper Objmapper = new ObjectMapper();
									String contentJson = Objmapper.writeValueAsString(oldContent);
									WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
									Invocation.Builder invocationBuilder = webTarget.request();
									Response response = invocationBuilder.post(Entity.entity(
											contentJson.toString(), MediaType.APPLICATION_JSON));
								} catch (Exception e) {
									logger.error("Exception", e);
								}
							}
						}
					}
			} else if (Content.MULTIPLE_FILE.equalsIgnoreCase(content
					.getContentType())) {
			
			String oldPath =content.getFilePath();
			int index = oldPath.lastIndexOf(".");
			String newPath = oldPath.substring(0,index) + "_Deleted"+oldPath.substring(index);
			boolean copy = amazonS3ClientService.copyObject(oldPath, newPath);
			if(copy)
				amazonS3ClientService.deleteFileFromS3Bucket(oldPath);
			content.setFilePath(newPath);
			
			contentService.updateFile(content);
			for(Content c:childList){
				c.setFilePath(newPath);
				
				contentService.updateFile(c);
			}
				contentService.deleteSoftById(String.valueOf(content.getId()));
				contentService.deleteSoftByParentModuleId(String.valueOf(content.getId()));
				//delete from shared school
				if(null != content.getSchoolToExport()) {
					if(!content.getSchoolToExport().isEmpty()) {
						String schoolName[] = content.getSchoolToExport().split(",");
						for(int i=0;i<schoolName.length; i++){
							String apiUrl = serverURL + schoolName[i].replace(" ","")+"/api/deleteShareContent";
							
							try {
								ObjectMapper Objmapper = new ObjectMapper();
								String contentJson = Objmapper.writeValueAsString(oldContent);
								WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
								Invocation.Builder invocationBuilder = webTarget.request();
								Response response = invocationBuilder.post(Entity.entity(
										contentJson.toString(), MediaType.APPLICATION_JSON));
							} catch (Exception e) {
								logger.error("Exception", e);
							}
						}
					}
				}
			}
			setSuccess(redirectAttrs, "Content Deleted Successfully");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Content");
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			return "redirect:/getContentUnderAPathForFacultyForModule";
		}

		return "redirect:/getContentUnderAPathForFacultyForModule";

	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/studentContentList", method = {
            RequestMethod.GET, RequestMethod.POST })
public String studentContentList(@ModelAttribute Content content, Model m,
            Long courseId, Principal p) {

      String username = p.getName();
      Token userdetails1 = (Token) p;
      String ProgramName = userdetails1.getProgramName();
      User u = userService.findByUserName(username);

      String acadSession = u.getAcadSession();
  	if(content.getAcadYear() != null){
		List<Course> courseList = courseService.findCoursesByAcadYear(
				p.getName(), Long.parseLong(userdetails1.getProgramId()),
				String.valueOf(content.getAcadYear()));
		
		m.addAttribute("courseList", courseList);
	}
      m.addAttribute("courseId", content.getCourseId());
      m.addAttribute("Program_Name", ProgramName);
      m.addAttribute("AcadSession", acadSession);

      m.addAttribute("webPage", new WebPage("contentList", "View Content",
                  true, false));
      m.addAttribute("content", content);
      m.addAttribute("courseId", courseId);
      List<StudentTest> sTestList = studentTestService
                  .findByUsername(username);
      
      List<String> academicYear = courseService.getAllAcadYear();
      m.addAttribute("acadYearList", academicYear);

      m.addAttribute("sTestList", sTestList);

      if (courseId != null) {
            Course c = courseService.findByID(courseId);

            content.setAcadMonth(c.getAcadMonth());
            content.setAcadYear(Integer.valueOf(c.getAcadYear()));
      }
      List<Test> testList = testService.findTestAllocatedForContent(username);
      List<Content> sharedFolder = null;
      try {

            if (content != null && content.getCourseId() != null) {
                  sharedFolder = contentService.getSharedFoldersByCourse(
                              p.getName(), content.getCourseId());
            } else {
                  sharedFolder = contentService.getSharedFolders(p.getName());
            }

            List<Content> allContent = new ArrayList<Content>();
            List<Content> everyoneContentList = new ArrayList<Content>();
            if (courseId != null) {
                  everyoneContentList = contentService
                              .findEveryoneContentByCourseId(courseId);
            } else {
                  everyoneContentList = contentService
                              .findEveryoneContentByUC(username);
            }

            if (everyoneContentList != null || everyoneContentList.size() != 0) {

                  allContent.addAll(everyoneContentList);
                  Collections.sort(allContent, new SortByCreatedDate());
            }

            if (sharedFolder == null || sharedFolder.size() == 0) {
                  m.addAttribute("size", 0);
            } else {
                  // Prepare Content Table Tree
                  for (Content contentFolder : sharedFolder) {
                        if(!allContent.stream().filter(o -> o.getId().equals(contentFolder.getId())).findFirst().isPresent()){
                        	allContent.add(contentFolder);
                        }
                  }

            }
            List<Content> newList = new ArrayList<Content>();

            if (testList.size() != 0) {
                  for (Content c : allContent) {
                        if (c.getExamViewType() != null
                                    && c.getExamViewType().equalsIgnoreCase("Y")) {
                              newList.add(c);

                        }
                  }
                  m.addAttribute("allContent", newList);
                  m.addAttribute("size", newList.size());
            } else {
                  m.addAttribute("allContent", allContent);
                  m.addAttribute("size", allContent.size());
            }

      } catch (Exception e) {

            logger.error(e.getMessage(), e);
            if (sharedFolder == null) {
                  m.addAttribute("size", 0);
                  setNote(m, "No Active Content Found");
            } else {
                  setError(m, "Error in getting content: " + e.getMessage());
            }

      }
      return "content/studentContentList";
}


	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/contentFormForStudents", method = RequestMethod.GET)
	public String contentFormForStudents(Model m, Principal p) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage(null, "Reports", false, false));
		List<Course> courseList = userdetails1.getCourseList();
		m.addAttribute("courseList", courseList);
		List<String> academicYear = courseService.getAllAcadYear();
        m.addAttribute("acadYearList", academicYear);
		
		return "content/studentContentList";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/getContentUnderAPathForStudent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getContentUnderAPathForStudent(
			@ModelAttribute Content content, Model m, Principal p) {
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		List<Course> courseList = new ArrayList<>();

		courseList = userdetails1.getCourseList();

		m.addAttribute("courseList", courseList);
		m.addAttribute("courseId", content.getCourseId());
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		List<String> academicYear = courseService.getAllAcadYear();
        m.addAttribute("acadYearList", academicYear);
		
		m.addAttribute("webPage", new WebPage("contentList", "View Content",
				true, false));
		m.addAttribute("content", content);
		try {
			// New Start Chnages
			if (content.getCourseId() == null) {
				// If Admin wants to see content for all
				if (content.getModuleId() == null && content.getModuleId().isEmpty()) {
					content.setFolderPath(courseRootFolder);
				} else {
					content.setFolderPath(courseRootFolder + ""
							+ content.getModuleId() + FILE_SEPARATOR);
				}
			} else if (content.getCourseId() != null && (content.getModuleId() == null || content.getModuleId().isEmpty())) {
				content.setFolderPath(courseRootFolder + ""
						+ content.getCourseId() + FILE_SEPARATOR);
			} else if(content.getCourseId() != null && content.getModuleId() != null ){
				content.setFolderPath(courseRootFolder + ""
						+ content.getModuleId() + FILE_SEPARATOR);
			}
			else{
				// If path is not present
				content.setFolderPath(courseRootFolder);
			}

			
			List<Content> allContent = contentService
					.getContentUnderAPathForStudent(content);// getContentUnderAPath
																// have Chnages
			
			if (allContent == null || allContent.size() == 0) {
				m.addAttribute("No Content available under this Course Folder");
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allContent);
				m.addAttribute("size", allContent.size());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content: " + e.getMessage());
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "content/adminContentList";
		}
		return "content/studentContentList";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/getContentUnderAPathForStudentForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getContentUnderAPathForStudentForModule(
			@ModelAttribute Content content, Model m, Principal p) {
		String username = p.getName();

		if (content.getIdForModule() != null) {
			content.setModuleId(content.getIdForModule());
		}
		if (content.getCourseIdForSearch() != null) {
			content.setCourseId(Long.valueOf(content.getCourseIdForSearch()));
		}

		if (content.getAcadYear() == null) {
			content.setModuleId(null);
			content.setCourseId(null);
		}

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		List<Course> courseList = new ArrayList<>();

		courseList = userdetails1.getCourseList();

		m.addAttribute("courseList", courseList);
		m.addAttribute("courseId", content.getCourseId());
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("contentList", "View Content",
				true, false));
		String acadMonth = "";
		String acadYear = "";
		List<String> academicYear = courseService.getAllAcadYear();
		m.addAttribute("acadYear", academicYear);
		m.addAttribute("content", content);

		if (content.getAcadYear() != null) {
			List<Course> moduleList = courseService.findModulesByUsername(
					username, String.valueOf(content.getAcadYear()),
					Long.parseLong(userdetails1.getProgramId()));
			m.addAttribute("modules", moduleList);
			if (content.getModuleId() != null
					&& !content.getModuleId().isEmpty()) {
				m.addAttribute(
						"allCourses",
						courseService.findCoursesByModuleId(
								Long.valueOf(content.getModuleId()),
								p.getName(),
								String.valueOf(content.getAcadYear())));
			}
		}
		try {
			List<Content> allContent = contentService
					.getContentUnderAPathForModule(content);

			if (allContent == null || allContent.size() == 0) {
				m.addAttribute("No Content available under this Course Folder");
				m.addAttribute("size", 0);
			} else {

				m.addAttribute("allContent", allContent);
				m.addAttribute("size", allContent.size());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting content: " + e.getMessage());
		}

		return "content/studentContentListForModule";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addQFolderFile", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addQFolderFile(
			@ModelAttribute Content content,
			@RequestParam(name = "contentFileName") List<String> contentFileName,
			Model m, @RequestParam Long courseId,
			RedirectAttributes redirectAttrs, Principal p,
			@RequestParam("filePaths1") MultipartFile filePaths1,
			@RequestParam("filePaths2") MultipartFile filePaths2,
			@RequestParam("filePaths3") MultipartFile filePaths3,
			@RequestParam("filePaths4") MultipartFile filePaths4,
			@RequestParam("filePaths5") MultipartFile filePaths5) {
		redirectAttrs.addFlashAttribute("content", content);

		List<Integer> list = new ArrayList<Integer>();
		Integer n = 0;
		for (String s : contentFileName) {
			if (s != null && s.length() > 0) {
				n++;
				list.add(contentFileName.indexOf(s));
			}
		}

		List<MultipartFile> multiPartFiles = new ArrayList<MultipartFile>();
		multiPartFiles.add(filePaths1);
		multiPartFiles.add(filePaths2);
		multiPartFiles.add(filePaths3);
		multiPartFiles.add(filePaths4);
		multiPartFiles.add(filePaths5);
		File file = null;
		try {

			HtmlValidation.validateHtml(content, new ArrayList<>());
			String courseName = (courseService.findByID(content.getCourseId()))
					.getCourseName();
			content.setContentName(courseName + " "
					+ RandomStringUtils.randomAlphabetic(2) + " Question Bank");
			performFolderPathCheck(content);

			String completFolderPath = content.getFolderPath()
					+ content.getContentName();
			file = new File(completFolderPath);
			boolean created = false;
			if (!file.exists()) {
				// Create Folder if it does not exists
				created = file.mkdirs();

				Course c = courseService.findByID(courseId);
				content.setCourseId(content.getCourseId());
				content.setAcadMonth(content.getAcadMonth());
				content.setAcadYear(content.getAcadYear());
				content.setCreatedBy(p.getName());
				content.setLastModifiedBy(p.getName());
				content.setFilePath(completFolderPath);
				content.setFacultyId(p.getName());
				content.setAccessType("Public");
				content.setContentType("Folder");

				// Create entry in DB
				contentService.insertWithIdReturn(content);
				Long folderId = content.getId();
				String createdFolderPath = content.getFolderPath();
				String folderName = content.getContentName();

				if (created) {
					setSuccess(redirectAttrs, "Folder Created Successfully");
					if (Content.FOLDER.equalsIgnoreCase(content
							.getContentType())
							&& Content.ACCESS_TYPE_PUBLIC.equals(content
									.getAccessType())) {
						// If it is a Public Folder, redirect to page used to
						// shared Folder with students

						// return "redirect:/viewContent";

					}
					try {
						for (int i = 1; i <= n; i++) {

							// if(contentFileName.get(i-1).equals(null)){
							String filePathForFiles = createdFolderPath
									+ folderName + FILE_SEPARATOR
									+ contentFileName.get(list.get(i - 1));
							String folderPathForFiles = createdFolderPath
									+ folderName + FILE_SEPARATOR;
							content.setFolderPath(folderPathForFiles);
							content.setFilePath(filePathForFiles);
							content.setContentType("File");
							content.setAcadMonth(content.getAcadMonth());
							content.setAcadYear(content.getAcadYear());
							content.setAccessType("Public");
							content.setContentName(contentFileName.get(list
									.get(i - 1)));
							content.setParentContentId(folderId);
							addFile(content,
									multiPartFiles.get(list.get(i - 1)),
									redirectAttrs, p,
									String.valueOf(content.getCourseId()));
							// }
							// else
							// continue;
						}

						List<UserCourse> totalStudentsList = userCourseService
								.findStudentsForFaculty(courseId);

						m.addAttribute("totalStudentsList",
								totalStudentsList.size());
						List<String> students = new ArrayList<String>();
						for (UserCourse sg : totalStudentsList) {
							students.add(sg.getUsername());

						}
						Content con = contentService.findByID(folderId);
						con.setStudents(students);
						saveStudentContentAllocation(con, m, p);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						setError(redirectAttrs, "Error in creating folder");
						// Delete folder if it was created successfully, but DB
						// operation
						// failed.
						if (file != null && file.list().length == 0) {
							file.delete();
						}
						return "redirect:/addContentForm";
					}
				}

				else {
					setError(redirectAttrs, "Error in creating folder");
					return "redirect:/addContentForm";
				}
			} else {
				setError(redirectAttrs, "Folder already exists with name "
						+ content.getContentName());
				return "redirect:/addContentForm";
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in creating folder");
			// Delete folder if it was created successfully, but DB operation
			// failed.
			if (file != null && file.list().length == 0) {
				file.delete();
			}
			return "redirect:/addContentForm";
		}

		return "redirect:/getContentUnderAPath";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addQBank", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addQBank(@ModelAttribute Content content, Model m, Principal p) {
		m.addAttribute("webPage", new WebPage("assignment", "Create Content",
				false, false));

		String contentType = content.getContentType();
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (content != null && content.getId() != null) {
			content = contentService.findByID(content.getId());
			m.addAttribute("edit", "true");

		}

		if (content != null && content.getParentContentId() != null) {
			Content parentContent = contentService.findByID(content
					.getParentContentId());
			content.setAccessType(parentContent.getAccessType());
		}
		// m.addAttribute("allCourses",courseService.findByUserActive(p.getName()));//
		// for admin do
		Token userDetails = (Token) p;

		String progName = userDetails.getProgramName(); // findbycoursebased
		// on program
		m.addAttribute("allCourses",
				courseService.findByCoursesBasedOnProgramName(progName));
		m.addAttribute("content", content);

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "content/addQFolderFileAdmin";
		} else {
			return "content/addQFolderFile";
		}

	}

	@RequestMapping(value = "/updateCount", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String updateCount(@RequestParam("id") Long id,
			Model m, Principal principal) {

		String username = principal.getName();
		try {
			List<Long> childList = new ArrayList<Long>();
			studentContentService.keepCount(id, username);
			childList.add(id);
			StudentContent countForChild = studentContentService.getStudentViewCount(childList);
			contentService.keepCount(id,countForChild.getCount());
			
			String parentModuleId = contentService.getParentModuleIdById(id);
			if(null != parentModuleId){
			List<Long> childListForParent =  contentService.getIdByParentModuleId(Long.valueOf(parentModuleId));
			Content countForParent = contentService.getStudentViewCountForParent(childListForParent);
			contentService.keepCount(Long.valueOf(parentModuleId),countForParent.getCount());
			}
			return "Success";
		} catch (Exception e) {

			logger.error("Error " + e.getMessage());
			return "Error";
		}
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/saveStudentContentAllocationForAllStudentsForModule", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentContentAllocationForAllStudentsForModule(
			@ModelAttribute Content content, Model m, Principal p,
			RedirectAttributes redirectAttrs) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("content", "Share Folder", true,
				false));

		try {
			logger.info("id---->"+content.getId());
			Content contentDb = contentService.findByID(content.getId());
			logger.info("ParentModule--->"+contentDb.getParentModuleId());
			List<StudentContent> students = new ArrayList<>();
			//04-03-2021
			List<Content> parentContentList = new ArrayList<>();
			if(null != contentDb.getParentModuleId()){
				parentContentList.add(contentDb);
			}else{
				parentContentList = contentService.getContentByParentModuleId(contentDb.getId());
			}
			
			List<Long> perentidList = new ArrayList<>();

			Map<Long, Long> mapper = new HashMap<>();

			for (Content c : parentContentList) {
				mapper.put(c.getCourseId(), c.getId());
				perentidList.add(c.getId());

			}
			if (content.getCampusId() != null) {
				students = studentContentService
						.getStudentsForContentAndCampusIdbyModuleId(
								perentidList, content.getModuleId(),
								content.getCampusId(), content.getAcadYear());
			} else {
				students = studentContentService.getStudentsContentForModule(
						perentidList, content.getModuleId(),
						content.getAcadYear(),contentDb.getFacultyId());
			}

			List<StudentContent> scList = new ArrayList<>();
			for (StudentContent sc : students) {
				if (sc.getContentId() == null) {

					StudentContent bean = new StudentContent();

					bean.setAcadMonth(contentDb.getAcadMonth());
					bean.setAcadYear(contentDb.getAcadYear());
					bean.setUsername(sc.getUsername());
					bean.setCourseId(sc.getCourseId());
					bean.setContentId(mapper.get(sc.getCourseId()));

					bean.setCount(0);
					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);
					scList.add(bean);
				}
			}

			studentContentService.insertBatch(scList);
			setSuccess(redirectAttrs, "All students allocated successfully");
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Allocation of all the students");
			logger.error("Exception ", ex);
		}
		redirectAttrs.addAttribute("id", content.getId());
		redirectAttrs.addAttribute("moduleId", content.getModuleId());
		redirectAttrs.addAttribute("acadYear", content.getAcadYear());
		if(!content.getAccessType().equals("Everyone")){
		return "redirect:/viewContentForModule";
		}else{
			return "redirect:/getContentUnderAPathForFacultyForModule";
		}
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/saveStudentContentAllocationForAllStudents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveStudentContentAllocationForAllStudents(
			@ModelAttribute Content content, Model m, Principal p) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		Content contentDb = contentService.findByID(content.getId());

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("content", "Share Folder", true,
				false));
		List<String> allocateContent = new ArrayList<String>();
		List<String> parentList = new ArrayList<String>();
		String subject = "Content with name  " + contentDb.getContentName();

		try {
			List<StudentContent> studentContent = studentContentService
					.getStudentsForContentCoursewise(content.getCourseId());


			for (StudentContent sg : studentContent) {
				allocateContent.add(sg.getUsername());

			}
			content.setStudents(allocateContent);

			Course course = content.getCourseId() != null ? courseService
					.findByID(Long.valueOf(content.getCourseId())) : null;
			StringBuffer buff = new StringBuffer(subject);

			if (course != null) {
				buff.append(" for Course ");
				buff.append(course.getCourseName());
			}
			buff.append(" shared with you ");
			subject = buff.toString();
			ArrayList<StudentContent> studentContentMappingList = new ArrayList<StudentContent>();
			List<String> studentList = new ArrayList<String>();
			if (content.getStudents() != null
					&& content.getStudents().size() > 0) {

				for (String studentUsername : content.getStudents()) {
					StudentContent bean = new StudentContent();
					bean.setAcadMonth(content.getAcadMonth());
					bean.setAcadYear(content.getAcadYear());
					bean.setContentId(content.getId());
					bean.setCourseId(content.getCourseId());
					bean.setUsername(studentUsername);
					bean.setCreatedBy(p.getName());
					bean.setLastModifiedBy(p.getName());
					studentList.add(studentUsername);
					List<StudentContent> studentAssList = studentContentService
							.getStudentUsername(bean.getContentId(),
									bean.getCourseId());

					if (studentAssList.isEmpty()) {
						studentContentMappingList.add(bean);
					} else {
						List<String> names = new ArrayList<String>();

						for (StudentContent ass : studentAssList) {
							names.add(ass.getUsername());
						}

						if (!names.contains(bean.getUsername())) {
							studentContentMappingList.add(bean);
						}
					}
				}

				studentContentService.insertBatch(studentContentMappingList);

				Map<String, Map<String, String>> result = null;
				if (!studentList.isEmpty()) {
					if ("Y".equals(contentDb.getSendEmailAlertToParents())) {
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

					if ("Y".equals(contentDb.getSendSmsAlertToParents())) {
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
					if ("Y".equals(contentDb.getSendEmailAlert())) {

						result = userService.findEmailByUserName(studentList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, subject);
					}

					if ("Y".equals(contentDb.getSendSmsAlert())) {
						if (result != null)

							result = userService
									.findEmailByUserName(studentList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject, subject);
					}
				}

				setSuccess(m, "Folder shared with "
						+ content.getStudents().size()
						+ " students successfully");

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in sharing folder");
		}
		m.addAttribute("content", content);
		if(!content.getAccessType().equals("Everyone")){
			return viewContent(content, m, null, p);	
		}
		else{
			return "redirect:/getContentUnderAPathForFacultyForModule";
		}
	}
	
	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/downloadFolder", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView downloadFolder(Principal principal,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String id) {
		try {

			Token userDetails = (Token) principal;
			response.setContentType("Content-type: text/zip");
			
			
			if (StringUtils.isEmpty(id)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage",
						"Error in downloading file.");
			}
			
			Content content = new Content();
			if (id != null) {
				content = contentService.findByID(Long.valueOf(id));
			}

			File folderPath = new File(downloadAllFolder + File.separator+ "allContent");
			if(!folderPath.exists()) {
				folderPath.mkdirs();
			}
			ServletOutputStream out = response.getOutputStream();
			String s3FilePath = content.getFilePath();
			if(s3FilePath.startsWith("/")) {
				s3FilePath = StringUtils.substring(s3FilePath, 1);
			}
			boolean status = amazonS3ClientService.downloadDir(s3FilePath,downloadAllFolder + "/allContent");
			if(status) {
				String fp = downloadAllFolder + "/allContent/" + s3FilePath;
				File file = new File(fp);
				String filename = content.getContentName() + ".zip";
				response.setHeader("Content-Disposition", "attachment; filename="+ filename + "");
				checkDirectoryOrNot(file, folderPath);
				pack(file.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return null;
	}
	
	
	//02-06-2020
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY", "ROLE_STUDENT" })
	@RequestMapping(value = "/downloadAllContent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadAllContent(Principal principal,
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String courseId) {
		try {

			Token userDetails = (Token) principal;
			response.setContentType("Content-type: text/zip");
			String filename = null;

			List<Content> contents = new ArrayList<>();
			String username = principal.getName();
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {
				if (courseId != null) {
					contents = contentService
							.findAllContentByUsernameAndCourseId(username,
									courseId);
					Course c = courseService.findByID(Long.valueOf(courseId));
					filename = c.getCourseName() + "-content.zip";
				} else {
					contents = contentService
							.findAllContentByUsername(username);
					filename = "allContent_" + username + ".zip";
				}
			}
			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

				if (courseId != null) {
					contents = contentService
							.findAllContentByCourseId(courseId);
					Course c = courseService.findByID(Long.valueOf(courseId));
					filename = c.getCourseName() + "-content.zip";
				} else {
					contents = contentService.findAllContent();
					filename = "allContent_" + username + ".zip";
				}
			}

			if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				if (courseId != null) {
					contents = contentService.getSharedFoldersAndFilesByCourse(
							username, Long.valueOf(courseId));
					List<Content> everyoneList = contentService
							.findEveryoneContentByCourseId(Long
									.valueOf(courseId));
					if (everyoneList != null || everyoneList.size() != 0)
						contents.addAll(everyoneList);

					Course c = courseService.findByID(Long.valueOf(courseId));
					filename = c.getCourseName() + "-content.zip";
				} else {
					contents = contentService.getSharedFoldersAndFile(username);
					List<Content> everyoneList = contentService
							.findAllEveryoneContentForStudent(username);
					if (everyoneList != null || everyoneList.size() != 0)
						contents.addAll(everyoneList);
					filename = "allContent_" + username + ".zip";
				}
			}
			response.setHeader("Content-Disposition", "attachment; filename="
					+ filename + "");
			File folderPath = new File(downloadAllFolder + File.separator
					+ "allContentDownload");

			ServletOutputStream out = response.getOutputStream();
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			List<File> files = new ArrayList();

			Map<String, String> mapOfFilePathAndCourseName = new HashMap<>();
			Map<String, File> mapOfFileAndFolderPath = new HashMap<>();
			for (Content c : contents) {
				if (c.getParentContentId() == null) {
					String s3FilePath = c.getFilePath();
					if (s3FilePath.startsWith("/")) {
						s3FilePath = StringUtils.substring(s3FilePath, 1);
					}
					if (userDetails.getAuthorities()
							.contains(Role.ROLE_STUDENT)) {
						if (!c.getFilePath().contains(".pptx")
								&& !c.getFilePath().contains(".zip")) {
							if(Content.FOLDER.equals(c.getContentType())) {
								boolean status = amazonS3ClientService.downloadDir(s3FilePath,
										downloadAllFolder + "/allContentDownload");

							}else if(Content.FILE.equals(c.getContentType())) {
								
								String fp = downloadAllFolder + "/allContentDownload/" + s3FilePath;
								File file = new File(fp);
								InputStream inpStream = amazonS3ClientService.getFileByFullPath(s3FilePath);
								FileUtils.copyInputStreamToFile(inpStream,file);
								
							}
							files.add(new File(downloadAllFolder + "/allContentDownload/" + s3FilePath));
							mapOfFilePathAndCourseName
									.put(new File(downloadAllFolder + "/allContentDownload/" + s3FilePath)
											.getAbsolutePath(),
											c.getCourseName().replaceAll(":", "_").replaceAll("&", " and ") 
													+ "-"
													+ String.valueOf(c
															.getCourseId()));

						}

					} else {
						if(Content.FOLDER.equals(c.getContentType())) {
							boolean status = amazonS3ClientService.downloadDir(s3FilePath,
									downloadAllFolder + "/allContentDownload");

						}else if(Content.FILE.equals(c.getContentType())) {
							
							String fp = downloadAllFolder + "/allContentDownload/" + s3FilePath;
							File file = new File(fp);
							InputStream inpStream = amazonS3ClientService.getFileByFullPath(s3FilePath);
							FileUtils.copyInputStreamToFile(inpStream,file);
							
						}
						files.add(new File(downloadAllFolder + "/allContentDownload/" + s3FilePath));
						mapOfFilePathAndCourseName.put(
								new File(downloadAllFolder + "/allContentDownload/" + s3FilePath).getAbsolutePath(),
								c.getCourseName().replaceAll(":", "_").replaceAll("&", " and ")  + "-"
										+ String.valueOf(c.getCourseId()));
					}

				}

			}
			for (File file : files) {
				File folderPathOfFile = new File(
						downloadAllFolder
								+ File.separator
								+ "allContent"
								+ File.separator
								+ mapOfFilePathAndCourseName.get(file
										.getAbsolutePath()));
				mapOfFileAndFolderPath.put(file.getAbsolutePath(),
						folderPathOfFile);
			}
			for (File file : files) {

				try {
					checkDirectoryOrNot(file,
							mapOfFileAndFolderPath.get(file.getAbsolutePath()));

				} catch (Exception e) {
					logger.error("Ëxception", e);
				}
			}

			pack(downloadAllFolder+ File.separator+ "allContent", out);
			FileUtils.deleteDirectory(folderPath);
			File folder= new File(downloadAllFolder+ File.separator+ "allContent");
			FileUtils.deleteDirectory(folder);

		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return null;

	}

	public void checkDirectoryOrNot(File file, File folderPath) {
		File newfile = new File(folderPath.getAbsolutePath() + File.separator
				+ file.getName());
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		try {
			if (file.isDirectory()) {
				if (file.listFiles().length == 0) {
					if (!newfile.exists()) {
						newfile.mkdir();
					}

				} else {
					for (File f : file.listFiles()) {

						File newFolder = new File(folderPath.getAbsolutePath() + File.separator + file.getName() + File.separator + f.getName());
						File newFolder1 = new File(folderPath.getAbsolutePath() + File.separator + file.getName());
						
						if(!f.getName().contains("_Deleted")){
							List<Content> checkForDown = contentService.findContentByFileName(f.getName());
								if(checkForDown.size() > 0) {
									if (!f.isDirectory()) {
										FileUtils.copyFile(f, newFolder);
									} else {
										checkDirectoryOrNot(f, newFolder1);
									}
								}
						}else {
							if(f.exists()) {
								f.delete();
							}
						}
					}
				}
			} else {
				FileUtils.copyFile(file, newfile);
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
	}
	
	public void checkFileAssignedOrNot(File file, File folderPath, List<Content> contents) {
		File newfile = new File(folderPath.getAbsolutePath() + File.separator + file.getName());
		if (!folderPath.exists()) {
			folderPath.mkdirs();
		}
		try {
			if (file.isDirectory()) {
				if (file.listFiles().length == 0) {

					if (!newfile.exists()) {
						newfile.mkdir();
					}

				} else {
					for (File f : file.listFiles()) {

						File newFolder = new File(folderPath.getAbsolutePath() + File.separator + file.getName()
								+ File.separator + f.getName());
						File newFolder1 = new File(folderPath.getAbsolutePath() + File.separator + file.getName());

							
						if (!contents
								.stream()
								.filter(o -> f.getAbsolutePath()
										.contains(o.getFilePath())).findFirst()
								.isPresent()) {
							if (!f.isDirectory()) {
								FileUtils.copyFile(f, newFolder);
							} else {
								checkFileAssignedOrNot(f, newFolder1, contents);
							}
						} else {
							f.delete();
						}
						
					}
				}
			} else {
				FileUtils.copyFile(file, newfile);
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
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
	
	@RequestMapping(value = "/downloadContentFile", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadFile(
			@RequestParam(required = false, name = "id", defaultValue = "") String id,
			HttpServletRequest request, HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;

		try {

			if (StringUtils.isEmpty(id)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage",
						"Error in downloading file.");
			}
			
			Content content = new Content();
			if (id != null) {
				content = contentService.findByID(Long.valueOf(id));
			}

			// get absolute path of the application
			ServletContext context = request.getSession().getServletContext();
			String projectUrl = "";
			File downloadFile = new File(content.getFilePath());

			String fileName = downloadFile.getName();
			

			String path = "";
			if(content.getFilePath().startsWith("/")) {
			path = StringUtils.substring(content.getFilePath(), 1);
			}else {
			path = content.getFilePath();
			}
			path = path.replace("/\\", "/");
			path = path.replace("\\\\","/");
			path = path.replace("\\","/");
			
			byte[] data = amazonS3ClientService.getFile(path);
			ByteArrayResource resource = new ByteArrayResource(data);

			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
			.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
			.body(resource);

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
	
	@RequestMapping(value = "/downloadScormContentFile", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView downloadScormFile(
			@RequestParam(required = false, name = "id", defaultValue = "") String id,
			HttpServletRequest request, HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;

		try {

			if (StringUtils.isEmpty(id)) {
				request.setAttribute("error", "true");
				request.setAttribute("errorMessage",
						"Error in downloading file.");
			}
			
			Content content = new Content();
			if (id != null) {
				content = contentService.findByID(Long.valueOf(id));
			}

			// get absolute path of the application
			ServletContext context = request.getSession().getServletContext();
			String projectUrl = "";
			File downloadFile = new File(content.getFilePath());
			File downloadFileTemp = new File(downloadAllFolder + "/" + downloadFile.getName());
			InputStream inpStream = amazonS3ClientService.getFileByFullPath(content.getFilePath());
			FileUtils.copyInputStreamToFile(inpStream,downloadFileTemp);
			String fileName = downloadFile.getName();
			
				
			response.setContentType("Content-type: text/html");

			File scormFile = viewFile(downloadFileTemp.getAbsolutePath(), content);

			projectUrl = scormFile.getAbsolutePath();

			String[] filePathSplit;
			filePathSplit = projectUrl.split("scromFiles");

			projectUrl = "/" + "workDir" + "/" + "scromFiles"
					+ filePathSplit[1];
			return new ModelAndView("redirect:" + projectUrl);

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
	
	
	@RequestMapping(value = "/viewFile", method = { RequestMethod.GET,
			RequestMethod.POST })
	public File viewFile(String filePath, Content content) {
		File scormFilePath = null;
		try {
			File sourceFile = new File(filePath);

			List<String> filePathSplit = Arrays.asList(sourceFile
					.getAbsolutePath().split(sourceFile.getName()));

			String destinationZip = workDir + File.separator + "scromFiles"
					+ File.separator + content.getContentName() + "-"
					+ content.getId();
			UnzipUtil.unzip(sourceFile.getAbsolutePath(), destinationZip);

			File unzippedFile = new File(destinationZip);
			String returnedFile = "";

			returnedFile = findFile("index.html", unzippedFile);

			if (returnedFile != null) {

				scormFilePath = new File(returnedFile);

			} else {

			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return scormFilePath;

	}

	String message = null;

	public String findFile(String name, File file) {

		File[] list = file.listFiles();

		if (list != null)
			for (File fil : list) {

				if (fil.isDirectory()) {
					findFile(name, fil);
				} else if (name.equalsIgnoreCase(fil.getName())) {

					message = fil.getAbsolutePath();

				}

			}

		return message;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/exportContentForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String exportContentForm(@ModelAttribute Content content, Model m,
			@RequestParam(required = false) Long campusId, Principal p) {
		m.addAttribute("webPage", new WebPage("content", "Create Content",
				true, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		String contentType = content.getContentType();
		if (content != null && content.getId() != null) {
			content = contentService.findByID(content.getId());
		}

		m.addAttribute("content", content);
		List<String> academicYear = courseService.getAllAcadYear();
		m.addAttribute("acadYear", academicYear);
		List<StudentContent> students = new ArrayList<StudentContent>();
		m.addAttribute("programList",
				programService.findAppsByUsername(username));
		m.addAttribute("preAssigments", new ArrayList<String>());

		return "content/exportContent";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/exportContent", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String exportContent(@ModelAttribute Content content, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		String[] str = content.getCourseIdToExport().split(",");
		content = contentService.findByID(content.getId());
		List<Content> allItems = contentService
				.getContentUnderAPathContentName(content);
		List<Content> contentList = new ArrayList<Content>();
		try {

			for (int i = 0; i < str.length; i++) {

				Course course = courseService.findByID(Long.valueOf(str[i]));
				for (Content c : allItems) {
					Content checkContent = contentService
							.findOneContent(c.getContentName(),
									courseRootFolder + str[i] + "/");

					if (checkContent != null) {
						setNote(redirectAttributes,
								course.getCourseName()
										+ " already contains a folder with name "
										+ c.getContentName());
					} else {

						String folderPath = c.getFolderPath();
						String filePath = c.getFilePath();
						String courseId = String.valueOf(c.getCourseId());
						String newfolderPath = folderPath.replaceFirst(
								courseId, str[i]);
						String newfilePath = "";
						if (c.getContentType().equalsIgnoreCase("Link")) {

						} else {
							newfilePath = filePath.replaceFirst(courseId,
									str[i]);
						}

						c.setCourseId(Long.valueOf(str[i]));
						c.setFilePath(newfilePath);
						c.setFolderPath(newfolderPath);
						File directory = new File(newfolderPath);

						if (!directory.exists()) {

							directory.mkdirs();

						}
						contentService.insertWithIdReturn(c);

						setSuccess(redirectAttributes,
								"Content Exported successfully");

						Content con = contentService.findOneContent(
								c.getContentName(), newfolderPath);
						//11-01-2021
						boolean copy = amazonS3ClientService.copyObject(content.getFilePath(), c.getFilePath());
//						if (c.getContentType().equalsIgnoreCase("File") || c.getContentType().equalsIgnoreCase("Multiple_File")) {
//							File file = new File(filePath);
//							String errorMessage = copyContentFile(
//									newfolderPath, newfilePath, file);
//						}
//						if (c.getContentType().equalsIgnoreCase("Folder")) {
//							File Folder = new File(newfilePath);
//							if (!Folder.exists()) {
//
//								Folder.mkdirs();
//
//							}
//						}
						if (c.getChildrenList().size() != 0) {
							Long parentId = con.getId();
							// for (Content cn : c.getChildrenList()) {
							exportContent(c.getChildrenList(), str, parentId);
							// }

						}
					}
					break;
				}
			}

		} catch (Exception e) {
			logger.error("Exception", e);

		}

		return "redirect:/exportContentForm?id=" + content.getId();
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/exportContentForModule", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String exportContentForModule(@ModelAttribute Content content,@RequestParam(name = "acadYearToExport", required = false, defaultValue = "") String acadYearToExport, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		String moduleId = content.getModuleId();
		String[] str = content.getModuleId().split(",");
		content = contentService.findByID(content.getId());
		List<Content> allItems = contentService
				.getContentUnderAPathContentNameForModule(content);

		List<Content> contentList = new ArrayList<Content>();
		
		List<Course> courseIdList = new ArrayList<>();
		

		courseIdList = courseService.findCoursesByModuleId(
				Long.valueOf(content.getModuleId()), principal.getName(), acadYearToExport);
		
		try {
			for (Content c : allItems) {
		
			Content checkContent = contentService.findOneContentForModule(c.getContentName(),courseRootFolder + moduleId + "/");
			if (checkContent != null) {
				String moduleName = courseService.getModuleName(moduleId);
				setNote(redirectAttributes,
						moduleName
								+ " already contains a folder with name "
								+ c.getContentName());
			} else {

				String folderPath = c.getFolderPath();
				String filePath = c.getFilePath();
				String moduleId1 = String.valueOf(c.getModuleId());
				String newfolderPath = folderPath.replaceFirst(
						moduleId1, moduleId);
				String newfilePath = "";
				if (c.getContentType().equalsIgnoreCase("Link")) {

				} else {
					newfilePath = filePath.replaceFirst(moduleId1,
							moduleId);
				}
				c.setAcadYear(Integer.valueOf(acadYearToExport));
				c.setModuleId(moduleId);
				c.setFilePath(newfilePath);
				c.setFolderPath(newfolderPath);
				File directory = new File(newfolderPath);

				if (!directory.exists()) {

					directory.mkdirs();

				}

				contentService.insertWithIdReturn(c);

				setSuccess(redirectAttributes,
						"Content Exported successfully");

				Content con = contentService.findOneContentForModule(
						c.getContentName(), newfolderPath);
				
				 //11-01-2021
				boolean copy = amazonS3ClientService.copyObject(content.getFilePath(), c.getFilePath());
//				if (c.getContentType().equalsIgnoreCase("File") || c.getContentType().equalsIgnoreCase("Multiple_File")) {
//					File file = new File(filePath);
//					String errorMessage = copyContentFile(
//							newfolderPath, newfilePath, file);
//				}
//				if (c.getContentType().equalsIgnoreCase("Folder")) {
//					File Folder = new File(newfilePath);
//					if (!Folder.exists()) {
//
//						Folder.mkdirs();
//
//					}
//				}
				courseIdList = courseService.findCoursesByModuleId(
						Long.valueOf(moduleId), principal.getName(), acadYearToExport);
				for(Course cc: courseIdList)
				{
					Content courseC = c ;
					courseC.setCourseId(cc.getId());
					courseC.setParentModuleId(String.valueOf(con.getId()));
					contentService.insertWithIdReturn(courseC);
				}
				if (c.getChildrenList().size() != 0) {
					exportContentForModule(c.getChildrenList(), str, con.getId(),acadYearToExport);
				}
			
			}
			break;
			}
			
		} catch (Exception e) {
			logger.error("Exception", e);

		}

		return "redirect:/exportContentForm?id=" + content.getId();
	}
	
	
	public void exportContent(List<Content> allItems, String[] str,
			Long parentId) {

		for (int i = 0; i < str.length; i++) {
			for (Content c : allItems) {

				String folderPath = c.getFolderPath();
				String filePath = c.getFilePath();
				String courseId = String.valueOf(c.getCourseId());
				String newfolderPath = folderPath
						.replaceFirst(courseId, str[i]);
				String newfilePath = "";
				if (c.getContentType().equalsIgnoreCase("Link")) {

				} else {
					newfilePath = filePath.replaceFirst(courseId, str[i]);
				}

				c.setCourseId(Long.valueOf(str[i]));
				c.setFilePath(newfilePath);
				c.setFolderPath(newfolderPath);
				File directory = new File(newfolderPath);

				if (!directory.exists()) {

					directory.mkdirs();

				}
				c.setParentContentId(parentId);
				contentService.insertWithIdReturn(c);
				 //11-01-2021
				boolean copy = amazonS3ClientService.copyObject(filePath, c.getFilePath());
//				if (c.getContentType().equalsIgnoreCase("File") || c.getContentType().equalsIgnoreCase("Multiple_File")) {
//					File file = new File(filePath);
//					String errorMessage = copyContentFile(newfolderPath,
//							newfilePath, file);
//				}
//				if (c.getContentType().equalsIgnoreCase("Folder")) {
//					File Folder = new File(newfilePath);
//					if (!Folder.exists()) {
//						Folder.mkdirs();
//					}
//				}
				
				Content con = contentService.findOneContent(c.getContentName(),
						c.getFolderPath());
				if (c.getChildrenList().size() != 0) {
					exportContent(c.getChildrenList(), str, con.getId());
				}
			}
		}

	}

	public void exportContentForModule(List<Content> allItems, String[] str,
			Long parentId, String acadYearToExport) {

		for (int i = 0; i < str.length; i++) {
			for (Content c : allItems) {

				String folderPath = c.getFolderPath();
				String filePath = c.getFilePath();
				String moduleId = String.valueOf(c.getModuleId());
				String newfolderPath = folderPath
						.replaceFirst(moduleId, str[i]);
				String newfilePath = "";
				if (c.getContentType().equalsIgnoreCase("Link")) {

				} else {
					newfilePath = filePath.replaceFirst(moduleId, str[i]);
				}
				c.setAcadYear(Integer.valueOf(acadYearToExport));
				c.setModuleId(str[i]);
				c.setFilePath(newfilePath);
				c.setFolderPath(newfolderPath);
				File directory = new File(newfolderPath);

				if (!directory.exists()) {

					directory.mkdirs();

				}
				c.setParentContentId(parentId);
				contentService.insertWithIdReturn(c);
				//11-01-2021
				boolean copy = amazonS3ClientService.copyObject(filePath, c.getFilePath());
//				if (c.getContentType().equalsIgnoreCase("File") || c.getContentType().equalsIgnoreCase("Multiple_File")) {
//					File file = new File(filePath);
//					String errorMessage = copyContentFile(newfolderPath,
//							newfilePath, file);
//				}
//				if (c.getContentType().equalsIgnoreCase("Folder")) {
//					File Folder = new File(newfilePath);
//					if (!Folder.exists()) {
//						Folder.mkdirs();
//					}
//				}
				
				Content con = contentService.findOneContentForModule(c.getContentName(),
						c.getFolderPath());
				List<Course> courseIdList = courseService.findCoursesByModuleId(
						Long.valueOf(c.getModuleId()), c.getCreatedBy(), acadYearToExport);
				
				for(Course cc: courseIdList)
				{
					Content courseC = c ;
					courseC.setCourseId(cc.getId());
					courseC.setParentModuleId(String.valueOf(con.getId()));
					contentService.insertWithIdReturn(courseC);
				}
				if (c.getChildrenList().size() != 0) {
					exportContentForModule(c.getChildrenList(), str, con.getId(),acadYearToExport);
				}
			}
		}

	}
	
	
	
	
	public String copyContentFile(String newFolderPath, String newFilePath,
			File file) {
		String errorMessage = null;

		InputStream inputStream = null;

		OutputStream outputStream = null;

		// CommonsMultipartFile file = bean.getFileData();

		String fileName = file.getName();

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

			inputStream = new FileInputStream(file);

			// Check if Folder exists. If not then create

			File directory = new File(newFolderPath);

			if (!directory.exists()) {

				directory.mkdirs();

			}

			File newFile = new File(newFilePath);

			outputStream = new FileOutputStream(newFile);

			IOUtils.copy(inputStream, outputStream);

		} catch (IOException e) {

			errorMessage = "Error in uploading Content file : "

			+ e.getMessage();

			logger.error("Exception", e);

		} finally {

			if (outputStream != null)

				IOUtils.closeQuietly(outputStream);

			if (inputStream != null)

				IOUtils.closeQuietly(inputStream);

		}

		return errorMessage;

	}

	@RequestMapping(value = "/supportAdminStudentContentList", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String supportAdminStudentContentList(Model m,
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String username, Principal p) {

		User u = userService.findByUserName(username);

		m.addAttribute("webPage", new WebPage("contentList", "View Content",
				true, false));
		Content content = new Content();
		m.addAttribute("content", content);
		m.addAttribute("courseId", courseId);

		List<Content> sharedFolder = null;
		List<Content> everyoneContentList = new ArrayList<Content>();
		List<Content> allContent = new ArrayList<Content>();
		try {

			if (courseId != null) {

				sharedFolder = contentService.getSharedFoldersByCourse(
						username, courseId);
				everyoneContentList = contentService
						.findEveryoneContentByCourseId(courseId);
			}

			if (everyoneContentList != null || everyoneContentList.size() != 0) {

				allContent.addAll(everyoneContentList);
				Collections.sort(allContent, new SortByCreatedDate());
			}

			if (sharedFolder != null || sharedFolder.size() != 0) {

				allContent.addAll(sharedFolder);
				Collections.sort(allContent, new SortByCreatedDate());
			}

			m.addAttribute("allContent", allContent);
			m.addAttribute("size", allContent.size());
			m.addAttribute("username", username);


		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			if (sharedFolder == null) {
				m.addAttribute("size", 0);
				setNote(m, "No Active Content Found");
			} else {
				setError(m, "Error in getting content: " + e.getMessage());
			}

		}
		return "content/supportAdminStudentContentList";
	}

	@RequestMapping(value = "/findModulesByUsernameAndAcadYear", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String findModulesByUsernameAndAcadYear(
			@ModelAttribute Content content, Model m,
			@RequestParam(name = "acadYear") String acadYear, Principal p) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		try {
			List<Course> moduleList = courseService.findModulesByUsername(
					p.getName(), acadYear,
					Long.parseLong(userdetails1.getProgramId()));
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			for (Course c : moduleList) {
				Map<String, String> returnMap = new HashMap();
				returnMap.put(c.getModuleId(), c.getModuleName());
				res.add(returnMap);
			}
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(res);
			return result;
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "";
		}

	}

	@RequestMapping(value = "/findCoursesByModuleIdAndUsernameAndAcadYear", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String findCoursesByModuleIdAndUsernameAndAcadYear(
			@ModelAttribute Content content, Model m,
			@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "moduleId") String moduleId, Principal p) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		try {

			List<Course> courseList = courseService.findCoursesByModuleId(
					Long.valueOf(moduleId), p.getName(), acadYear,userdetails1.getProgramId());
			List<Map<String, String>> res = new ArrayList<Map<String, String>>();

			for (Course c : courseList) {
				Map<String, String> returnMap = new HashMap();
				returnMap.put(String.valueOf(c.getId()), c.getCourseName());
				res.add(returnMap);
			}
			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(res);
			return result;
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "";
		}

	}
	@RequestMapping(value = "/findCoursesByUsernameAndAcadYear", method = {
            RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String findCourseByUsernameAndAcadYear(
            @ModelAttribute Content content, Model m,
            @RequestParam(name = "acadYear") String acadYear, Principal p) {
		String username = p.getName();
		Token userdetails1 = (Token) p;
		try {
            List<Course> allCourses = courseService.findCoursesByAcadYear(p.getName(),
                                            Long.parseLong(userdetails1.getProgramId()),acadYear);
            List<Map<String, String>> res = new ArrayList<Map<String, String>>();

            for (Course c : allCourses) {
                            Map<String, String> returnMap = new HashMap();
                            returnMap.put(String.valueOf(c.getId()), c.getCourseName());
                            res.add(returnMap);
            }
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(res);
            return result;
		} catch (Exception ex) {

            logger.error("Exception", ex);
            return "";
		}

}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/exportContentToOtherSchoolForm", method = {
            RequestMethod.GET, RequestMethod.POST })
	public String exportContentToOtherSchoolForm(
            @ModelAttribute Content content, Model m,Principal p) {
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
	
		try{
			List<String> academicYear = courseService.getAllAcadYear();
			m.addAttribute("acadYear", academicYear);
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(serverCrudURL + "getSchoolsListForLibrary"));
		
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);
		
			String resp = invocationBuilder.get(String.class);
			List<Map<String, Object>> schoolMap = mapper.readValue(resp, new TypeReference<List<Map<String, Object>>>(){});
			m.addAttribute("schoolListMap",schoolMap);
			m.addAttribute("appName",appName);
			m.addAttribute("allCampuses", userService.findCampus());
		}catch(Exception e){
			logger.error("Exception", e);
		}
	
		
		m.addAttribute("content", content);
		return "content/exportContentForSchool";
		
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/exportContentToOtherSchool", method = {
            RequestMethod.GET, RequestMethod.POST })
	public String exportContentToOtherSchool(
            @ModelAttribute Content content, Model m,Principal p,RedirectAttributes redirectAttrs) {
		
		String schoolName = content.getSchoolToExport();
		
		Content c = contentService.findByID(content.getId());
		
		List<Content> itemsUnderPath = contentService.getItemsToShare(c,String.valueOf(c.getCourseId()));
		List<Content> courseIds = new ArrayList<Content>();
		courseIds.add(content);
		String[] str = content.getCourseIdToExport().split(",");
		
		Map<String,List<Content>> objJson = new HashMap<String, List<Content>>();
		objJson.put("courseIds",courseIds);
		objJson.put("items", itemsUnderPath);
		
		
		String apiUrl = serverURL + schoolName.replace(" ", "")+"/api/insertShareContent";
		
		try {
			ObjectMapper Objmapper = new ObjectMapper();
	        String contentJson = Objmapper.writeValueAsString(objJson);
			WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
			Invocation.Builder invocationBuilder = webTarget.request();
			
			Response response = invocationBuilder.post(Entity.entity(contentJson, MediaType.APPLICATION_JSON));
			String resJson = response.readEntity(String.class);
			if(resJson.contains("Status") && resJson.contains("Success")){
				contentService.updateShareToSchool(schoolName, c.getFilePath());
				setSuccess(redirectAttrs,"Content Exported Successfully.");
			}else{
				setError(redirectAttrs, "Error in exporting content.");
			}
			
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		content.setAcadYearToExport(null);
		redirectAttrs.addFlashAttribute("content", content);
		return "redirect:/exportContentForm";
	
	}
	
	@RequestMapping(value = "/getProgramsOfSchool", method = {
            RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getProgramsOfSchool(
            @RequestParam(name = "schoolName") String schoolName, Model m,Principal p) {
		
		String apiUrl = serverURL +schoolName.replace(" ", "")+"/api/getProgramsOfSchool";
		
		try {
			ObjectMapper Objmapper = new ObjectMapper();
			WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
			Invocation.Builder invocationBuilder = webTarget.request();
			String response = invocationBuilder.get(String.class);
		
			return response;	
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "";
	}
	@RequestMapping(value = "/getCourseByProgramIdOfSchool", method = {
            RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByProgramIdOfSchool(
			@RequestParam(name = "schoolName") String schoolName,
            @RequestParam(name = "programId") Long programId,
            @RequestParam(name = "acadYear") Integer acadYear,
            @RequestParam(name = "campusId", required =false) Long campusId, Model m,Principal p) {
		
		String apiUrl = serverURL +schoolName.replace(" ", "")+"/api/getCourseByProgramIdOfSchool";
		Content content = new Content();
		content.setAcadYear(acadYear);
		content.setProgramId(programId);
		content.setCampusId(campusId);
		try {
			ObjectMapper Objmapper = new ObjectMapper();
			
	        String contentJson = Objmapper.writeValueAsString(content);
			WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
			Invocation.Builder invocationBuilder = webTarget.request();
			Response response = invocationBuilder.post(Entity.entity(
					contentJson.toString(), MediaType.APPLICATION_JSON));
			String respJson = response.readEntity(String.class);
			return respJson;	
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "";
	}

	private String uploadContentFileForS3(Content content,String folderPath, MultipartFile file) {

		String errorMessage = null;
		try {
			if(folderPath.startsWith("/")) {
				folderPath = StringUtils.substring(folderPath, 1);
			}
			if(folderPath.endsWith("/")) {
				folderPath = folderPath.substring(0, folderPath.length()-1);
			}
			
			Map<String,String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file, folderPath);
			if(s3FileNameMap.containsKey("SUCCESS")) {
				String s3FileName = s3FileNameMap.get("SUCCESS");
				String filePath = folderPath + "/" + s3FileName;
				content.setFolderPath(folderPath+"/");
				content.setFilePath(filePath);
				errorMessage = s3FileName;
			}else {
				throw new Exception("Error in uploading file");
			}
		} catch (Exception e) {
			errorMessage = "Error in uploading file : "
			+ e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}
	
	@ModelAttribute("campusList")
	public List<ProgramCampus> getCampusList() {

		return programCampusService.getCampusList();
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadContentReportForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadContentReportForm(Model m, Principal principal) {

		List<String> acadYearCodeList = courseService.findAcadYearCode();

		logger.info("acadYearCodeList------------>" + acadYearCodeList);
		m.addAttribute("acadYearCodeList", acadYearCodeList);
		return "report/contentReport";
	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadContentReport", method = { RequestMethod.GET })
	public String downloadContentReport(@RequestParam String acadYear, @RequestParam(required = false) String campusId,
			HttpServletResponse response, Model m, Principal p) throws URIException {

		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		String programId = userdetails1.getProgramId();
		List<Content> contentList = contentService.findAllContentsByParams(programId, acadYear, campusId);

		List<String> validateHeaders = new ArrayList<String>(
				Arrays.asList("Faculty Id", "Faculty Name", "Course Name", "Content Name", "Content Creation Date"));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfContents = new ArrayList<>();
			fileName = "contentDetails-" + acadYear + "-" + programId + ".xlsx";
			fileName = fileName.replace("/", "_");

			filePath = "/data/temp/" + fileName;

			for (Content c : contentList) {
				Map<String, Object> contentMap = new HashMap<>();

				contentMap.put("Faculty Id", c.getFacultyId());
				contentMap.put("Faculty Name", c.getFacultyName());
				contentMap.put("Course Name", c.getCourseName());
				contentMap.put("Content Name", c.getContentName());
				contentMap.put("Content Creation Date", c.getCreatedDate());

				listOfMapOfContents.add(contentMap);

			}
			excelCreater.CreateExcelFile(listOfMapOfContents, validateHeaders, filePath);
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
	@RequestMapping(value = "/getFacultyOfCourseFromOtherSchool", method = {
            RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyOfCourseFromOtherSchool(
			@RequestParam(name = "schoolName") String schoolName,
            @RequestParam(name = "programId") Long programId,
            @RequestParam(name = "acadYear") Integer acadYear,
            @RequestParam(name = "courseId") Long courseId,
            @RequestParam(name = "campusId", required =false) Long campusId,
            Model m,Principal p) {
		
		String apiUrl = serverURL +schoolName.replace(" ", "")+"/api/getFacultyOfCourseFromOtherSchool";
		Content content = new Content();
		content.setAcadYear(acadYear);
		content.setProgramId(programId);
		content.setCampusId(campusId);
		content.setCourseId(courseId);
		try {
			ObjectMapper Objmapper = new ObjectMapper();
			
	        String contentJson = Objmapper.writeValueAsString(content);
			WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
			Invocation.Builder invocationBuilder = webTarget.request();
			Response response = invocationBuilder.post(Entity.entity(
					contentJson.toString(), MediaType.APPLICATION_JSON));
			String respJson = response.readEntity(String.class);
			return respJson;	
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "";
	}
	
	@RequestMapping(value = "/getProgramCampusOfSchool", method = {
            RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getProgramCampusOfSchool(
            @RequestParam(name = "schoolName") String schoolName, Model m,Principal p) {
		
		String apiUrl = serverURL +schoolName.replace(" ", "")+"/api/getProgramCampusOfSchool";
		
		try {
			ObjectMapper Objmapper = new ObjectMapper();
			WebTarget webTarget = client.target(URIUtil.encodeQuery(apiUrl));
			Invocation.Builder invocationBuilder = webTarget.request();
			String response = invocationBuilder.get(String.class);
		
			return response;	
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return "";
	}
	
}
