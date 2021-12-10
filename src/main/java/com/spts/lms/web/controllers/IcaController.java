package com.spts.lms.web.controllers;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.apache.tika.Tika;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.ica.IcaBean;
import com.spts.lms.beans.ica.IcaComponent;
import com.spts.lms.beans.ica.IcaComponentMarks;
import com.spts.lms.beans.ica.IcaQueries;
import com.spts.lms.beans.ica.IcaStudentBatchwise;
import com.spts.lms.beans.ica.IcaTotalMarks;
import com.spts.lms.beans.ica.NonCreditIcaModule;
import com.spts.lms.beans.ica.NsBean;
import com.spts.lms.beans.ica.PredefinedIcaComponent;
import com.spts.lms.beans.ica.StudentNcIca;
import com.spts.lms.beans.ica.SubjectSapEnroll;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.ica.IcaBeanService;
import com.spts.lms.services.ica.IcaComponentMarksService;
import com.spts.lms.services.ica.IcaComponentService;
import com.spts.lms.services.ica.IcaQueriesService;
import com.spts.lms.services.ica.IcaStudentBatchwiseService;
import com.spts.lms.services.ica.IcaTotalMarksService;
import com.spts.lms.services.ica.NonCreditIcaModuleService;
import com.spts.lms.services.ica.NsService;
import com.spts.lms.services.ica.PredefinedIcaComponentService;
import com.spts.lms.services.ica.StudentNcIcaService;
import com.spts.lms.services.ica.SubjectSapEnrollService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.tee.TeeBeanService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.Utils;
import com.spts.lms.web.utils.ValidationException;

@Controller
public class IcaController extends BaseController {

	private static final Logger logger = Logger.getLogger(IcaController.class);

	@Autowired
	IcaBeanService icaBeanService;

	@Autowired
	TeeBeanService teeBeanService;

	@Autowired
	CourseService courseService;

	@Autowired
	IcaComponentService icaComponentService;

	@Autowired
	IcaComponentMarksService icaComponentMarksService;

	@Autowired
	PredefinedIcaComponentService predefinedIcaComponentService;

	@Autowired
	IcaTotalMarksService icaTotalMarksService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	UserService userService;

	@Autowired
	ProgramService programService;

	@Autowired
	IcaQueriesService icaQueriesService;

	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	NsService nsService;

	@Autowired
	SubjectSapEnrollService subjectSapEnrollService;

	@Autowired
	NonCreditIcaModuleService nonCreditIcaModuleService;

	@Autowired
	StudentNcIcaService studentNcIcaService;

	@Autowired
	Notifier notifier;

	@Autowired
	IcaStudentBatchwiseService icaStudentBatchwiseService;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Autowired
	TestService testService;

	@Autowired
	StudentTestService studentTestService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAutoGrowCollectionLimit(1000);
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

	@Value("${file.base.directory}")
	private String baseDir;

	@Value("${file.base.directory.s3}")
	private String baseDirS3;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${app}")
	private String app;

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/addIcaForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addIcaForm(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Add ICA", true, false));

		IcaBean ica = new IcaBean();
		// ica.setProgramId(userdetails1.getProgramId());
		m.addAttribute("multipleFaculty", "true");
		if (id != null) {

			ica = icaBeanService.findByID(Long.valueOf(id));
			// IcaTotalMarks icaMarks = icaTotalMarksService.ge
			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			logger.info("ica received" + ica);
			if ("Y".equals(ica.getIsIcaDivisionWise()) && ica.getParentIcaId() == null) {

				m.addAttribute("isParentIca", "true");
				m.addAttribute("isGradingStart",
						icaComponentMarksService.checkWhetherGradingStartOrNotP(String.valueOf(ica.getId())));

			} else {
				m.addAttribute("isParentIca", "false");
				m.addAttribute("isGradingStart",
						icaComponentMarksService.checkWhetherGradingStartOrNot(String.valueOf(ica.getId())));
			}

			if (currentDate.compareTo(ica.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot update, ICA Deadline is over");
				return "redirect:/searchIcaList";
			} else if ("Y".equals(ica.getIsSubmitted())) {
				setNote(redirectAttrs, "Cannot update, ICA is Already Evaluated");
				return "redirect:/searchIcaList";
			} else {
				if (currentDate.compareTo(ica.getStartDate()) > 0) {

					m.addAttribute("icaStarted", "true");
				} else {
					m.addAttribute("icaStarted", "false");
				}
			}

			m.addAttribute("edit", "true");
			m.addAttribute("moduleName", courseService.getModuleName(ica.getModuleId()));
			m.addAttribute("programList", programService.getProgramListByIds(ica.getProgramId()));
			m.addAttribute("facultyList",
					userCourseService.findAllFacultyWithModuleIdICA(ica.getModuleId(), ica.getAcadYear()));
			if (null != ica.getAssignedFaculty()) {
				if (ica.getAssignedFaculty().contains(",")) {
					m.addAttribute("selectedFacultyList",
							userCourseService.findAllFacultyByFacultyIds(ica.getAssignedFaculty()));
					logger.info("facultyListSize2--->"
							+ userCourseService.findAllFacultyByFacultyIds(ica.getAssignedFaculty()));
					m.addAttribute("multipleFaculty", "true");
				} else {
					if ("Y".equals(ica.getIsIcaDivisionWise()) && null == ica.getParentIcaId()) {
						m.addAttribute("multipleFaculty", "false");
					} else if ("Y".equals(ica.getIsIcaDivisionWise()) && null != ica.getParentIcaId()) {
						m.addAttribute("multipleFaculty", "false");
					} else {
						m.addAttribute("multipleFaculty", "true");
						m.addAttribute("selectedFacultyList",
								userCourseService.findAllFacultyByFacultyIds(ica.getAssignedFaculty()));
					}
				}
			}

			m.addAttribute("icaBean", ica);
			if ("Y".equals(ica.getIsIcaDivisionWise()) && ica.getParentIcaId() == null) {
				setNote(m, "This is Division-Wise ICA,Any Update in this, will reflect to all ICA Divisions");
				return "ica/createIcaForDivision";
			}
		}

		List<String> acadYearCodeList = courseService.findAcadYearCode();

		logger.info("acadYearCodeList------------>" + acadYearCodeList);
		m.addAttribute("acadYearCodeList", acadYearCodeList);
		m.addAttribute("icaBean", ica);

		return "ica/createIca";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addIca", method = RequestMethod.POST)
	public String addIca(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {
		
		try {
			logger.info("Validating /addIca...");
			HtmlValidation.validateHtml(icaBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", icaBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year Selected");
			}
			Course acadSession = courseService.checkIfExistsInDB("acadSession", icaBean.getAcadSession());
			if(acadSession == null) {
				throw new ValidationException("Invalid Acad Session Selected");
			}
			if(icaBean.getCampusId() != null && !icaBean.getCampusId().isEmpty()){
				Course campusId = courseService.checkIfExistsInDB("campusId", icaBean.getCampusId());
				if(campusId == null) {
					throw new ValidationException("Invalid Campus Selected");
				}
			}
			Course moduleId = courseService.checkIfExistsInDB("moduleId", icaBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module Selected");
			}
			logger.info("create ica selected programs are " + icaBean.getProgramId());
			List<String> progIds = null;
			if(icaBean.getProgramId().contains(",")) {
				progIds = Arrays.asList(icaBean.getProgramId().split(","));
				for(String programId : progIds) {
					Course progId = courseService.checkIfExistsInDB("programId", programId);			
					if(progId == null) {
						throw new ValidationException("Invalid Program Selected");
					}
				}
			} else {
				Course progId = courseService.checkIfExistsInDB("programId", icaBean.getProgramId());
				if(progId == null) {
					throw new ValidationException("Invalid Program Selected");
				}
			}
			List<String> assignedFaculties = null;
			if(icaBean.getAssignedFaculty().contains(",")) {
				assignedFaculties = Arrays.asList(icaBean.getAssignedFaculty().split(","));
				for(String assignedFaculty : assignedFaculties) {
					UserCourse courseId = userCourseService.getFacultyCourseId(assignedFaculty,icaBean.getModuleId());
					if(null == courseId) {
		                  throw new ValidationException("Invalid faculty selected.");
		            }
					logger.info("courseID is " + courseId);
					UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(assignedFaculty, String.valueOf(courseId.getCourseId()));
		            if(null == userccourse) {
		                  throw new ValidationException("Invalid Faculty Selected.");
		            }
				}
			} else {
				UserCourse courseId = userCourseService.getFacultyCourseId(icaBean.getAssignedFaculty(),icaBean.getModuleId()); 
				if(null == courseId) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
				UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(icaBean.getAssignedFaculty(), String.valueOf(courseId.getCourseId()));
	            if(null == userccourse) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
			}
//			icaBean.setTotalMarks("");
//			icaBean.setInternalMarks("10");
//			icaBean.setInternalPassMarks("-1");
//			icaBean.setExternalMarks("");
//			icaBean.setExternalPassMarks("");
			if(icaBean.getTotalMarks() != null && !icaBean.getTotalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks(),icaBean.getExternalMarks(),icaBean.getExternalPassMarks(),icaBean.getTotalMarks());
			} else
			if(icaBean.getInternalMarks() != null && !icaBean.getInternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks());
			} else
			if(icaBean.getExternalMarks() != null && !icaBean.getExternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getExternalMarks(), icaBean.getExternalPassMarks());
			} else {
				throw new ValidationException("Invalid Marks");
			}
			Utils.validateStartAndEndDates(icaBean.getStartDate(), icaBean.getEndDate());			
			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
				icaBean.setScaledReq("N");
				icaBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumericNotAZero(icaBean.getScaledMarks());
			}
			if(icaBean.getIcaDesc() != null && !icaBean.getIcaDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaDesc());
			}
			String username = principal.getName();
			icaBean.setCreatedBy(username);
			icaBean.setLastModifiedBy(username);
			icaBean.setActive("Y");
			icaBean.setIsNonEventModule("N");

			
			
			/* New Audit changes end */
			List<IcaBean> icaDBList = icaBeanService.checkAlreadyExistICAList(icaBean.getModuleId(),
					icaBean.getAcadYear(), icaBean.getCampusId(), icaBean.getAcadSession());

			for (IcaBean icaDB : icaDBList) {
				if (icaDB != null) {
					if (icaBean.getProgramId().contains(",")) {
						List<String> programIds = Arrays.asList(icaBean.getProgramId().split(","));
						if (icaDB.getProgramId().contains(",")) {
							for (String db : Arrays.asList(icaDB.getProgramId().split(","))) {
								if (programIds.contains(db)) {
									setError(redirectAttrs, "ICA Already Exist");
									return "redirect:/addIcaForm";
								}
							}
						} else {
							if (programIds.contains(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaForm";
							}
						}
					} else {
						if (icaDB.getProgramId().contains(",")) {
							if (Arrays.asList(icaDB.getProgramId().split(",")).contains(icaBean.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaForm";
							}
						} else {
							if (icaBean.getProgramId().equals(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaForm";
							}
						}
					}
				}
			}
			/*
			 * if (totalMarks != icaInternalMarks + icaExternalMarks) {
			 * 
			 * setError(redirectAttrs,
			 * "Total Marks & Sum of Internal,External Marks Should Match"); return
			 * "redirect:/addIcaForm"; logger.info("validation"); } else
			 */

			icaBeanService.insertWithIdReturn(icaBean);
			List<String> userList = new ArrayList<String>();
			if (icaBean.getAssignedFaculty().contains(",")) {
				List<String> faculties = new ArrayList<String>();
				faculties = Arrays.asList(icaBean.getAssignedFaculty().split(","));
				userList.addAll(faculties);
			} else {
				userList.add(icaBean.getAssignedFaculty());
			}

			User u = userService.findByUserName(username);
			String subject = " New ICA: " + icaBean.getIcaName();
			Map<String, Map<String, String>> result = null;
			List<String> acadSessionList = new ArrayList<>();
			String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaBean.getIcaName() + " <br>"
					+ "<b>ICA Description: </b>" + icaBean.getIcaDesc() + "<br><br>"
					+ "<b>Note: </b>This ICA is created by : ?? <br>"
					+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			for (String s : userList) {
				if (!userList.isEmpty()) {
					List<String> userList1 = new ArrayList<String>();
					userList1.add(s);
					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList1);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}

			setSuccess(redirectAttrs, "ICA Added Successfully");
			// redirectAttrs.addAttribute("icaId", icaBean.getId());
			redirectAttrs.addFlashAttribute("icaBean", icaBean);
			if (icaBean.getAssignedFaculty().contains(",")) {
				return "redirect:/createStudentGroupForm";
			} else {
				return "redirect:/addIcaComponentsForm";
			}

		} catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addIcaForm";
		}
		catch (Exception ex) {
			logger.info("INSIDE Exception");
			logger.error("Excption while creating ICA", ex);
			setError(redirectAttrs, "Error While Creating ICA,ICA May have already created");
			return "redirect:/addIcaForm";
		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/updateIca", method = RequestMethod.POST)
	public String updateIca(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {
		
		IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());
		try {
			logger.info("Validating /updateIca...");
			HtmlValidation.validateHtml(icaBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", icaBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year Selected");
			}
			if(icaBean.getCampusId() != null && !icaBean.getCampusId().isEmpty()){
				Course campusId = courseService.checkIfExistsInDB("campusId", icaBean.getCampusId());
				if(campusId == null) {
					throw new ValidationException("Invalid Campus Selected");
				}
			}
			Course moduleId = courseService.checkIfExistsInDB("moduleId", icaBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module Selected");
			}
			List<String> acadSess = null;
			if(icaBean.getAcadSession().contains(",")) {
				acadSess = Arrays.asList(icaBean.getAcadSession().split(","));
				for(String acadS : acadSess) {
					Course acadSession = courseService.checkIfExistsInDB("acadSession", acadS);			
					if(acadSession == null) {
						throw new ValidationException("Invalid Acad Session Selected");
					}
				}
			} else {
				Course acadSession = courseService.checkIfExistsInDB("acadSession", icaBean.getAcadSession());
				if(acadSession == null) {
					throw new ValidationException("Invalid Acad Session Selected");
				}
			}
			List<String> progIds = null;
			if(icaBean.getProgramId().contains(",")) {
				logger.info("inside multiple programs");
				progIds = Arrays.asList(icaBean.getProgramId().split(","));
				for(String programId : progIds) {
					Course progId = courseService.checkIfExistsInDB("programId", programId);			
					if(progId == null) {
						throw new ValidationException("Invalid Program Selected");
					}
				}
			} else {
				logger.info("inside single program");
				Course progId = courseService.checkIfExistsInDB("programId", icaBean.getProgramId());
				if(progId == null) {
					throw new ValidationException("Invalid Program Selected");
				}
			}
			List<String> assignedFaculties = null;
			if(icaBean.getAssignedFaculty().contains(",")) {
				assignedFaculties = Arrays.asList(icaBean.getAssignedFaculty().split(","));
				for(String assignedFaculty : assignedFaculties) {
					UserCourse courseId = userCourseService.getFacultyCourseId(assignedFaculty,icaBean.getModuleId());
					logger.info("courseID is " + courseId);
					if(null == courseId) {
		                  throw new ValidationException("Invalid Faculty Selected.");
		            }
					UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(assignedFaculty, String.valueOf(courseId.getCourseId()));
		            if(null == userccourse) {
		                  throw new ValidationException("Invalid Faculty Selected.");
		            }
				}
			} else {
				UserCourse courseId = userCourseService.getFacultyCourseId(icaBean.getAssignedFaculty(),icaBean.getModuleId());
				if(null == courseId) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
				UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(icaBean.getAssignedFaculty(), String.valueOf(courseId.getCourseId()));
	            if(null == userccourse) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
			}
			if(icaBean.getTotalMarks() != null && !icaBean.getTotalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks(),icaBean.getExternalMarks(),icaBean.getExternalPassMarks(),icaBean.getTotalMarks());
			} else
			if(icaBean.getInternalMarks() != null && !icaBean.getInternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks());
			} else
			if(icaBean.getExternalMarks() != null && !icaBean.getExternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getExternalMarks(), icaBean.getExternalPassMarks());
			} else {
				throw new ValidationException("Invalid Marks");
			}
			BusinessBypassRule.validateStartAndEndDatesToUpdate(icaBean.getStartDate(), icaBean.getEndDate());
			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
				icaBean.setScaledReq("N");
				icaBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumericNotAZero(icaBean.getScaledMarks());
			}
			if(icaBean.getIcaDesc() != null && !icaBean.getIcaDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaDesc());
			}

			logger.info("Acad Year New--------------->" + icaBean.getAcadYear());

			redirectAttrs.addAttribute("id", icaBean.getId());

			icaBean.setActive("Y");
			icaBean.setLastModifiedBy(principal.getName());
			if (icaBean.getScaledReq() == null) {
				icaBean.setScaledReq(icaBeanDAO.getScaledReq());
				icaBean.setScaledMarks(icaBeanDAO.getScaledMarks());
			}
			if (icaBean.getInternalMarks() == null) {
				icaBean.setInternalMarks(icaBeanDAO.getInternalMarks());
			}
			if (icaBean.getInternalPassMarks() == null) {
				icaBean.setInternalPassMarks(icaBeanDAO.getInternalPassMarks());
			}
			if (icaBean.getExternalMarks() == null) {
				icaBean.setExternalMarks(icaBeanDAO.getExternalMarks());
			}
			if (icaBean.getExternalPassMarks() == null) {
				icaBean.setExternalPassMarks(icaBeanDAO.getExternalPassMarks());
			}
			if (icaBean.getTotalMarks() == null) {
				icaBean.setTotalMarks(icaBeanDAO.getTotalMarks());
			}
			if (null != icaBeanDAO.getIsNonEventModule() && ("Y").equals(icaBeanDAO.getIsNonEventModule())) {
				icaBean.setIsNonEventModule(icaBeanDAO.getIsNonEventModule());
			}

			if ("Y".equals(icaBeanDAO.getIsIcaDivisionWise()) && icaBeanDAO.getParentIcaId() == null) {
				List<IcaBean> updatedIcaBeanList = new ArrayList<>();
				List<IcaBean> icaListByParentIcaId = icaBeanService.getIcaIdsByParentIcaIds(icaBean.getId());
				List<IcaBean> submittedIcaListByParentIcaId = icaBeanService
						.getSubmittedIcaIdsByParentIcaIds(icaBean.getId());
				if (icaListByParentIcaId.size() == submittedIcaListByParentIcaId.size()) {
					setError(redirectAttrs, "ICA Cannot be updated since it is already submitted");
				} else {
					icaBeanService.update(icaBean);

					for (IcaBean ib : icaListByParentIcaId) {
						IcaBean ibNew = new IcaBean();
						Long ibId = ib.getId();

						String ibParentId = ib.getParentIcaId();
						String ibFacultyId = ib.getAssignedFaculty();
						String eventId = ib.getEventId();
						ibNew = icaBean;
						ibNew.setId(ibId);
						ibNew.setParentIcaId(ibParentId);
						ibNew.setAssignedFaculty(ibFacultyId);
						ibNew.setEventId(eventId);
						icaBeanService.update(ibNew);

					}
					logger.info("icaBean" + updatedIcaBeanList);
					// icaBeanService.updateBatch(updatedIcaBeanList);
					setSuccess(redirectAttrs, "ICA Updated Successfully");

				}

			} else {
				int updated = 0;// icaBeanService.update(icaBean);
				if (!("Y").equals(icaBeanDAO.getIsIcaDivisionWise())) {
					List<IcaStudentBatchwise> studentListForCheck = icaStudentBatchwiseService
							.getAllByActiveIcaId(String.valueOf(icaBean.getId()));
					int count = 0;
					List<String> dbFacultyList = Arrays.asList(icaBeanDAO.getAssignedFaculty());
					List<String> facultyList = Arrays.asList(icaBean.getAssignedFaculty());
					for (String faculty : facultyList) {
						if (!dbFacultyList.contains(faculty)) {
							count++;
						}
					}
					if (count > 0) {
						if (studentListForCheck.size() > 0) {
							icaStudentBatchwiseService.deleteAllActiveByIcaId(String.valueOf(icaBean.getId()));
						}
					}

					if (null != icaBean.getAssignedFaculty() && icaBean.getAssignedFaculty().contains(",")) {
						updated = icaBeanService.update(icaBean);
						return "redirect:/createStudentGroupForm";
					}

					updated = icaBeanService.update(icaBean);
				} else {
					updated = icaBeanService.update(icaBean);
				}

				if (updated == 0) {

					if (icaBeanDAO.getIsSubmitted().equals("Y")) {
						setError(redirectAttrs, "ICA Cannot be updated since it is already submitted");
					}
					// setNote(redirectAttrs,"Only ICA End-Date,Faculty-Assigned,Name ");
				} else {

					setSuccess(redirectAttrs, "ICA Updated Successfully");
				}

			}

			if (icaBeanDAO.getParentIcaId() == null) {
				logger.info(
						"icabeandao marks" + icaBeanDAO.getInternalMarks() + "ica bean" + icaBean.getInternalMarks());
				if (Integer.parseInt(icaBeanDAO.getInternalMarks()) != Integer.parseInt(icaBean.getInternalMarks())) {
					setNote(redirectAttrs, "You have change the total Internal Marks,Kindly Reassign Components");
					redirectAttrs.addFlashAttribute("icaBean", icaBean);
					return "redirect:/addIcaComponentsForm";
				}
			}
		} catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addIcaForm";

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Updating ICA");
			logger.error("Exception", ex);
		}
		if (null != icaBean.getIsNonEventModule() && ("Y").equals(icaBean.getIsNonEventModule())) {
			return "redirect:/addIcaFormForNonEventModules";
		} else {
			if ("Y".equals(icaBeanDAO.getIsCourseraIca())) {
				return "redirect:/addIcaFormForCoursera";
			}
			return "redirect:/addIcaForm";
		}
	}

	/**
	 * @param m
	 * @param principal
	 * @param id
	 * @param icaBean
	 * @param redirectAttrs
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/createStudentGroupForm", method = RequestMethod.GET)
	public String createStudentGroupForm(Model m, Principal principal, @RequestParam(required = false) Long id,
			@ModelAttribute IcaBean icaBean, RedirectAttributes redirectAttrs) {
		Token userdetails1 = (Token) principal;
		logger.info("ica Bean received" + icaBean);

		if (id != null) {
			icaBean = icaBeanService.findByID(id);
		}

		/*
		 * List<IcaComponentMarks> icaComponentMarksByIcaId = icaComponentMarksService
		 * .icaComponentMarksByIcaId(icaBean.getId()); if
		 * (icaComponentMarksByIcaId.size() > 0 && icaComponentsByIcaId.size() > 0) {
		 * setNote(redirectAttrs, "Cannot Add Components Ica "); return
		 * "redirect:/searchIcaList"; }
		 */

		if (icaBean.getAssignedFaculty().contains(",")) {
			List<IcaStudentBatchwise> studentListForCheck = icaStudentBatchwiseService
					.getAllByActiveIcaId(String.valueOf(icaBean.getId()));
			if (studentListForCheck.size() > 0) {
				List<IcaComponentMarks> icaComponentMarksByIcaId = icaComponentMarksService
						.icaComponentMarksByIcaId(icaBean.getId());

				if (icaComponentMarksByIcaId.size() > 0) {
					setNote(redirectAttrs,
							"Cannot Assign Students to Faculty ,Faculty Evaluation has begun or completed ");
					return "redirect:/searchIcaList";
				} else {
					setNote(redirectAttrs, "You have already assigned students to faculty!");
					return "redirect:/searchIcaList";
				}
			}
		}

		String p = programService.getProgramNamesForIca(icaBean.getProgramId());

		m.addAttribute("programName", p);
		if (null != icaBean.getIsNonEventModule() && ("Y").equals(icaBean.getIsNonEventModule())) {
			m.addAttribute("moduleName", courseService.getModuleNameForNonEvent(icaBean.getModuleId()));
		} else {
			m.addAttribute("moduleName", courseService.getModuleName(icaBean.getModuleId()));
		}
		// m.addAttribute("componentList", getIcaComponenets);
		if (icaBean.getAssignedFaculty().contains(",")) {

			List<UserCourse> facultyList = new ArrayList<>();
			List<UserCourse> studentList = new ArrayList<>();
			List<String> facultyIds = Arrays.asList(icaBean.getAssignedFaculty().split(","));
			facultyList = userCourseService.findFacultyNamesByIdsForBatch(facultyIds);
			if ("Y".equals(icaBean.getIsCourseraIca())) {
				studentList = userCourseService.findStudentByModuleIdAndAcadYearForBatchCE(icaBean.getModuleId(),
						icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(), icaBean.getCampusId());

				logger.info("studentList Coursera ---------" + icaBean.getModuleId() + " " + icaBean.getAcadYear() + " "
						+ icaBean.getAcadSession() + " " + icaBean.getProgramId() + " " + icaBean.getCampusId());
				logger.info("studentList Coursera ---------" + studentList);
			} else {

				studentList = userCourseService.findStudentByModuleIdAndAcadYearForBatch(icaBean.getModuleId(),
						icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(), icaBean.getCampusId());
			}

			logger.info("studentListSize----->" + studentList.size());
			m.addAttribute("facultyList", facultyList);
			m.addAttribute("studentList", studentList);
		}
		m.addAttribute("ica", icaBean);

		return "ica/assignStudentsFacultyWise";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/saveStudentsBatchWiseForIca", method = RequestMethod.POST)
	public @ResponseBody String saveStudentsBatchWiseForIca(@RequestParam String json, HttpServletResponse resp, RedirectAttributes redirectAttr) {
		logger.info("json--->" + json);
		IcaBean icaBean = new IcaBean();
		String ica = "";
		ObjectMapper mapper = new ObjectMapper();

		try {

			List<IcaStudentBatchwise> jsonList = mapper.readValue(json, new TypeReference<List<IcaStudentBatchwise>>() {});
			logger.info("jsonList is " + jsonList.size());
			logger.info("jsonList--->" + jsonList.size());
//			for (IcaStudentBatchwise isb : jsonList) {
//				
//				IcaBean icaId = icaBeanService.checkIfExistsInDB("id",isb.getIcaId());
//				icaId=null;
//				if(icaId == null) {
//					throw new ValidationException("Invalid ICA Selected");
//				}
//				IcaBean assignedFaculty = icaBeanService.checkIfExistsInDB("assignedFaculty",isb.getFacultyId());
//				if(assignedFaculty == null) {
//					throw new ValidationException("Invalid Faculty Selected");
//				}
//				User user = userService.checkIfExistsInDB(isb.getUsername());
//				if(user == null) {
//					throw new ValidationException("Invalid User Selected");
//				}
//			}
			if (jsonList.size() > 0) {

				icaBean = icaBeanService.findByID(Long.valueOf(jsonList.get(0).getIcaId()));
				for (IcaStudentBatchwise isb : jsonList) {
					isb.setCreatedBy(icaBean.getCreatedBy());
					isb.setLastModifiedBy(icaBean.getLastModifiedBy());
					isb.setActive("Y");
				}
				icaStudentBatchwiseService.upsertBatch(jsonList);
				icaBean.setStatus("success");
				ica = mapper.writeValueAsString(icaBean);
				return ica;
			} else {
				icaBean.setStatus("blank");
				ica = mapper.writeValueAsString(icaBean);
			}
		}
//			catch (ValidationException ve) {
//				logger.info("INSIDE Validation Exception");
//				logger.error(ve.getMessage(), ve);
//				setError(redirectAttr, ve.getMessage());
//				icaBean.setStatus("validationError");
//			
//		} 
		catch (Exception e) {
			logger.error("Exception while saving students", e);
			icaBean.setStatus("falied");
		}
		return ica;

	}

	/*
	 * @Secured("ROLE_ADMIN")
	 * 
	 * @RequestMapping(value = "/deleteIca", method = RequestMethod.GET) public
	 * String deleteIca(Model m, Principal principal,
	 * 
	 * @ModelAttribute IcaBean icaBean, RedirectAttributes redirectAttrs) {
	 * 
	 * try {
	 * 
	 * 
	 * IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());
	 * 
	 * redirectAttrs.addAttribute("id",icaBean.getId());
	 * 
	 * icaBean.setActive("N"); icaBean.setLastModifiedBy(principal.getName());
	 * if(icaBean.getScaledReq()==null){ icaBean.setScaledReq("N");
	 * icaBean.setScaledMarks(null); }
	 * 
	 * 
	 * IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());
	 * if(icaBeanDAO.getParentIcaId()!=null){ redirectAttrs.addAttribute("icaId",
	 * icaBeanDAO.getParentIcaId()); }
	 * 
	 * if ("Y".equals(icaBeanDAO.getIsSubmitted())) {
	 * 
	 * setNote(redirectAttrs,
	 * "ICA Cannot be Deleted Since It is Submitted By Faculty"); }
	 * 
	 * else { icaBeanService.deleteSoftById(String.valueOf(icaBean.getId()));
	 * 
	 * setSuccess(redirectAttrs, "ICA Delete Successfully"); }
	 * 
	 * } catch (Exception ex) { setError(redirectAttrs, "Error in Deleting ICA");
	 * logger.error("Exception", ex); }
	 * 
	 * return "redirect:/searchIcaList"; }
	 */

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deleteIca", method = RequestMethod.GET)
	public String deleteIca(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {

		try {

			/*
			 * IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());
			 * 
			 * redirectAttrs.addAttribute("id",icaBean.getId());
			 * 
			 * icaBean.setActive("N"); icaBean.setLastModifiedBy(principal.getName());
			 * if(icaBean.getScaledReq()==null){ icaBean.setScaledReq("N");
			 * icaBean.setScaledMarks(null); }
			 */

			IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());
			if (icaBeanDAO.getParentIcaId() != null) {
				redirectAttrs.addAttribute("icaId", icaBeanDAO.getParentIcaId());
				// changes starts
				List<IcaBean> getSubmittedIca = icaBeanService
						.icaSubmittedListByParent(String.valueOf(icaBeanDAO.getParentIcaId()));
				
				logger.info("getSubmittedIca---" + getSubmittedIca.size());
				if (getSubmittedIca.size() > 0) {

					setNote(redirectAttrs, " ICA Cannot be Deleted ");
					return "redirect:/searchIcaList";
				}

				redirectAttrs.addAttribute("icaId", icaBeanDAO.getParentIcaId());
			}

			if ("Y".equals(icaBeanDAO.getIsIcaDivisionWise()) && icaBeanDAO.getParentIcaId() == null) {

				List<IcaBean> divisionWiseIca = icaBeanService.icaListByParent(String.valueOf(icaBean.getId()));

				if (divisionWiseIca.size() > 0) {
					setNote(redirectAttrs,
							"ICA Cannot be Deleted Since It is for Division,First Delete All Division-Wise ICA's");
					return "redirect:/searchIcaList";
				}

			}
			
			if("Y".equals(icaBeanDAO.getIsPublishCompWise())) {
				List<IcaComponent> submCompIcaList = icaComponentService.icaSubmittedListByIcaId(String.valueOf(icaBean.getId()));
				if(submCompIcaList.size()>0) {
					setNote(redirectAttrs, " ICA Cannot be Deleted ");
					return "redirect:/searchIcaList";
				}
			}

			if ("Y".equals(icaBeanDAO.getIsSubmitted())) {

				setNote(redirectAttrs, "ICA Cannot be Deleted Since It is Submitted By Faculty");
			}

			else {
				icaBeanService.deleteSoftById(String.valueOf(icaBean.getId()));
				if (icaBeanDAO.getAssignedFaculty().contains(",")) {
					List<IcaStudentBatchwise> studentListForCheck = icaStudentBatchwiseService
							.getAllByActiveIcaId(String.valueOf(icaBean.getId()));
					if (studentListForCheck.size() > 0) {
						icaStudentBatchwiseService.deleteSoftByIcaId(String.valueOf(icaBean.getId()));
					}
				}
				setSuccess(redirectAttrs, "ICA Delete Successfully");
			}

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting ICA");
			logger.error("Exception", ex);
		}

		return "redirect:/searchIcaList";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addIcaComponentsForm", method = RequestMethod.GET)
	public String addIcaComponentsForm(Model m, Principal principal, @RequestParam(required = false) Long id,
			@ModelAttribute IcaBean icaBean, RedirectAttributes redirectAttrs) {
		Token userdetails1 = (Token) principal;
		logger.info("ica Bean received" + icaBean);

		if (id != null) {
			icaBean = icaBeanService.findByID(id);
		}

		if (null != icaBean.getAssignedFaculty()) {
			if (icaBean.getAssignedFaculty().contains(",")) {
				List<IcaStudentBatchwise> studentListForCheck = icaStudentBatchwiseService
						.getAllByActiveIcaId(String.valueOf(icaBean.getId()));
				if (studentListForCheck.size() == 0) {
					setNote(redirectAttrs, "Assign Students FacultyWise First!");
					return "redirect:/searchIcaList";
				}
			}
		}

		long icaId = 0l;

		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			icaId = icaBean.getId();
		} else {

			if ("Y".equals(icaBean.getIsIcaDivisionWise())) {
				List<IcaBean> icaBeanCList = icaBeanService.getIcaIdsByParentIcaIds(icaBean.getId());
				icaId = icaBeanCList.get(0).getId();
			} else {
				icaId = icaBean.getId();
			}
		}
		List<IcaBean> getIcaComponenets = icaBeanService.getIcaComponents(icaId);
		List<IcaComponent> icaComponentsByIcaId = icaComponentService.icaComponentListByIcaId(icaId);
		/*
		 * List<IcaComponentMarks> icaComponentMarksByIcaId = icaComponentMarksService
		 * .icaComponentMarksByIcaId(icaBean.getId()); if
		 * (icaComponentMarksByIcaId.size() > 0 && icaComponentsByIcaId.size() > 0) {
		 * setNote(redirectAttrs, "Cannot Add Components Ica "); return
		 * "redirect:/searchIcaList"; }
		 */

		List<IcaComponentMarks> icaComponentMarksByIcaId = new ArrayList<>();

		if ("Y".equals(icaBean.getIsIcaDivisionWise()) && icaBean.getParentIcaId() == null) {
			icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByParentIcaId(icaBean.getId());
		} else {
			icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByIcaId(icaBean.getId());
		}

		if (icaComponentMarksByIcaId.size() > 0 && icaComponentsByIcaId.size() > 0) {
			setNote(redirectAttrs, "Cannot Add Components Ica,Faculty Evaluation has begun or completed ");
			return "redirect:/searchIcaList";
		}

		String p = programService.getProgramNamesForIca(icaBean.getProgramId());

		m.addAttribute("programName", p);
		if (null != icaBean.getIsNonEventModule() && ("Y").equals(icaBean.getIsNonEventModule())) {
			m.addAttribute("moduleName", courseService.getModuleNameForNonEvent(icaBean.getModuleId()));
		} else {
			m.addAttribute("moduleName", courseService.getModuleName(icaBean.getModuleId()));
		}
		m.addAttribute("componentList", getIcaComponenets);
		m.addAttribute("ica", icaBean);

		return "ica/addIcaComponents";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/getModuleByParam", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getModuleByParam(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId, @RequestParam(name = "acadSession") String acadSession,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService.moduleListByAcadYearAndCampus(acadSession,
				acadYear, campusId);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getModuleId(), module.getModuleName() + "(" + module.getModuleAbbr() + ")");
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/getSessionByParam", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getSessionByParam(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService.acadSessionListByAcadYearAndCampus(acadYear,
				campusId);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getAcadSession(), module.getAcadSession());
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/getSessionByParamForIcaReport", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getSessionByParamForIcaReport(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService
				.acadSessionListByAcadYearAndCampusForIca(acadYear, campusId);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getAcadSession(), module.getAcadSession());
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/getFacultyByModuleICA", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getFacultyByModuleICA(@RequestParam(name = "moduleId") String moduleId,
			@RequestParam(name = "acadYear") String acadYear, Principal principal) {

		String json = "";
		// String username = principal.getName();
		Token userdetails1 = (Token) principal;
		List<UserCourse> faculty = userCourseService.findAllFacultyWithModuleIdICA(moduleId, acadYear);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (UserCourse ass : faculty) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(ass.getUsername(), ass.getFacultyName() + "(" + ass.getUsername() + ")");
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

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/saveComponentsForIca", method = RequestMethod.POST)
	public String saveComponentsForIca(

			@ModelAttribute IcaBean ica, RedirectAttributes redirectAttrs, Principal principal, Model m) {
		String username = principal.getName();
		Gson g = new Gson();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();

		List<String> seqList = new ArrayList<>();
		
//		logger.info("component name/s - >" + ica.getComponentName());
//		logger.info("component id/s - >" + ica.getComponentId());
//		logger.info("getComponentList id/s - >" + ica.getComponentList());
//		logger.info("component id/s - >" + ica.get);
	try {//Peter 25/10/2021
		for (IcaComponent icaComp : ica.getComponentList()) {
			
			
			if(icaComp.getComponentId() != null && !icaComp.getComponentId().isEmpty()) {
				IcaBean checkIfComponentIdExists = icaBeanService.checkIfComponentIdExists(icaComp.getComponentId());
				if(checkIfComponentIdExists==null) {
					throw new ValidationException("Invalid Component");
				}
			}
			if(icaComp.getSequenceNo() != null && !icaComp.getSequenceNo().isEmpty()) {
				BusinessBypassRule.validateNumeric(icaComp.getSequenceNo());
			}
			if(icaComp.getMarks()!=null) {
				BusinessBypassRule.validateNumeric(icaComp.getMarks());
			}
		}

		for (IcaComponent icm : ica.getComponentList()) {
			if (icm.getSequenceNo() != null && !icm.getSequenceNo().isEmpty()) {
				seqList.add(icm.getSequenceNo());

			}

		}

		logger.info("seqList is "+seqList);
		Collections.sort(seqList);
		logger.info("seqList after sorting is "+seqList);

		if (seqList.size() > 0) {

			for (int i = 0; i < seqList.size() - 1; i++) {

				if (seqList.get(i).equals(seqList.get(i + 1))) {
					redirectAttrs.addAttribute("id", ica.getId());

					setError(redirectAttrs, "Kindly provide the propper sequence No duplicates are allowed ");
					return "redirect:/addIcaComponentsForm";
				}

			}
			for (int i = 0; i < seqList.size(); i++) {
				int data = Integer.parseInt(seqList.get(i));
				if (data > seqList.size()) {
					redirectAttrs.addAttribute("id", ica.getId());

					logger.info("s duplicatae  valuessssssw");
					setError(redirectAttrs, "Sequence Number should be same as components");
					return "redirect:/addIcaComponentsForm";
				}
			}

		}

		m.addAttribute("Program_Name", ProgramName);

		IcaBean icaDAO = icaBeanService.findByID(ica.getId());
		List<IcaComponent> icaComponentListByIcaId = new ArrayList<>();

		if (!"Y".equals(icaDAO.getIsPublishCompWise())) {
			icaComponentListByIcaId = icaComponentService.icaComponentListByIcaId(ica.getId());

		} else {

			if ("Y".equals(icaDAO.getIsIcaDivisionWise())) {
				icaComponentListByIcaId = icaComponentService.icaComponentListByParentIcaId(ica.getId());
			} else {

				icaComponentListByIcaId = icaComponentService.icaComponentListByIcaId(ica.getId());
			}
		}

		for (IcaComponent ic : icaComponentListByIcaId) {

			ic.setActive("N");

		}

		if (icaComponentListByIcaId.size() > 0) {
			icaComponentService.updateBatch(icaComponentListByIcaId);
		}

//here try
			
			
			
			List<IcaComponent> icaComponentList = new ArrayList<>();
			if (icaComponentListByIcaId.size() > 0) {
				icaComponentList.addAll(icaComponentListByIcaId);
			}
			logger.info("ica compoe list" + icaComponentList);
			double totalMarksComp = 0.0;
			if (!"Y".equals(icaDAO.getIsPublishCompWise())) {
				int count = 1;
				for (IcaComponent ic : ica.getComponentList()) {

					if (ic.getComponentId() != null) {
						ic.setCreatedBy(username);
						ic.setLastModifiedBy(username);
						ic.setActive("Y");
						if (ic.getSequenceNo() == null || ic.getSequenceNo().isEmpty()) {
							ic.setSequenceNo(String.valueOf(count));
						}
						icaComponentList.add(ic);
						totalMarksComp = totalMarksComp + Double.parseDouble(ic.getMarks());
						count++;
					}
				}
			} else {

				if ("Y".equals(icaDAO.getIsIcaDivisionWise())) {
					List<IcaBean> icaChildList = icaBeanService.getIcaIdsByParentIcaIds(icaDAO.getId());
					logger.info("ica child size:" + icaChildList.size());
					int counter = 0;
					for (IcaBean ib : icaChildList) {

						int seqCount = 1;
						for (IcaComponent ic : ica.getComponentList()) {

							if (ic.getComponentId() != null) {

								logger.info("ica id--->" + ib.getId());
								IcaComponent icBean = new IcaComponent();
								if (ic.getSequenceNo() == null || ic.getSequenceNo().isEmpty()) {
									icBean = getIcaCompBean(ic, String.valueOf(ib.getId()), username,
											String.valueOf(seqCount));
								} else {
									icBean = getIcaCompBean(ic, String.valueOf(ib.getId()), username,
											ic.getSequenceNo());
								}

								icaComponentList.add(icBean);

								if (counter == 0) {

									totalMarksComp = totalMarksComp + Double.parseDouble(ic.getMarks());
								}
								seqCount++;
							}
						}
						logger.info("total comp marks:" + totalMarksComp);
						counter++;
					}
				} else {
					int seqCount = 1;
					for (IcaComponent ic : ica.getComponentList()) {

						if (ic.getComponentId() != null) {
							ic.setCreatedBy(username);
							ic.setLastModifiedBy(username);
							ic.setActive("Y");
							if (ic.getSequenceNo() == null || ic.getSequenceNo().isEmpty()) {
								ic.setSequenceNo(String.valueOf(seqCount));

							}
							icaComponentList.add(ic);
							totalMarksComp = totalMarksComp + Double.parseDouble(ic.getMarks());
							seqCount++;
						}
					}
				}
			}

			redirectAttrs.addAttribute("id", ica.getId());

			if (Double.parseDouble(icaDAO.getInternalMarks()) != totalMarksComp) {
				setError(redirectAttrs, "Total Marks For Component Does not match with ica total Internal Marks:"
						+ icaDAO.getInternalMarks());
				return "redirect:/addIcaComponentsForm";
			}

			icaComponentService.upsertBatch(icaComponentList);

			setSuccess(redirectAttrs, "Components Added Successfully");
			return "redirect:/addIcaComponentsForm";
		} catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			redirectAttrs.addAttribute("id", ica.getId());
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addIcaComponentsForm";
		}
		catch (Exception ex) {

			logger.error("Exception", ex);
			setError(redirectAttrs, "Error in Adding Components");
			return "redirect:/addIcaComponentsForm";
		}
	}

	public IcaComponent getIcaCompBean(IcaComponent icaCompDB, String icaId, String username, String seqNo) {

		IcaComponent ic = new IcaComponent();

		ic.setComponentId(icaCompDB.getComponentId());
		ic.setIcaId(icaId);
		ic.setMarks(icaCompDB.getMarks());
		ic.setCreatedBy(username);
		ic.setLastModifiedBy(username);
		ic.setSequenceNo(seqNo);

		ic.setActive("Y");
		return ic;

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/searchIcaList", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchIcaList(Model m, Principal principal, @RequestParam(required = false) String icaId) {

		List<IcaBean> icaList = new ArrayList<>();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Search ICA", true, false));

		String role = "";
		boolean isQueryRaised = false;
		if (icaId != null && userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			icaList = icaBeanService.findDivisionWiseIcaListByParentIca(icaId);

			m.addAttribute("icaId", icaId);

		} else {
			/*
			 * if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) { role =
			 * Role.ROLE_FACULTY.name(); icaList = icaBeanService.findIcaListByProgram(
			 * userdetails1.getProgramId(), role, principal.getName());
			 * 
			 * } else { role = Role.ROLE_ADMIN.name(); icaList =
			 * icaBeanService.findIcaListByProgram( userdetails1.getProgramId(), role,
			 * principal.getName());
			 */
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				role = Role.ROLE_FACULTY.name();
				icaList = icaBeanService.findIcaListByProgramForBatchWise(userdetails1.getProgramId(), role,
						principal.getName());
				
				//Peter 25/11/2021 - SaveAsDraft Highlight Task
				for(IcaBean i : icaList){
					int checkIfSavedAsDraft = icaTotalMarksService.checkIfSavedAsDraft(i.getId());
					logger.info("got saveAsDraft");
					if(checkIfSavedAsDraft>0){
						logger.info("setting saveAsDraft Y");
						i.setSaveAsDraft("Y");
					}
				}

			} else {
				role = Role.ROLE_ADMIN.name();
				icaList = icaBeanService.findIcaListByProgramForBatchWise(userdetails1.getProgramId(), role,
						principal.getName());
				// add Non-Event Module ica list

				for (IcaBean i : icaList) {
					if ("Y".equals(i.getIsIcaDivisionWise()) && i.getParentIcaId() == null) {
						List<String> getStatusForIca = icaBeanService.getIcaStatusesForDivisionIca(i.getId());
						if (getStatusForIca.size() == 1) {
							String status = getStatusForIca.get(0);
							if ("Y".equals(status)) {
								i.setIsSubmitted("Y");
							}
						}
					}
				}

				List<IcaBean> nonEventModuleIcaList = icaBeanService
						.findIcaListByProgramForNonEventModule(userdetails1.getProgramId(), role, principal.getName());
				if (nonEventModuleIcaList.size() > 0) {
					for (IcaBean ica : nonEventModuleIcaList) {
						if (!icaList.stream()
								.filter(o -> o.getModuleId().equals(ica.getModuleId())
										&& o.getProgramId().equals(ica.getProgramId())
										&& o.getAcadYear().equals(ica.getAcadYear()))
								.findFirst().isPresent()) {
							icaList.add(ica);
						}
					}
				}
			}
			logger.info("role--->" + role);
		}

		// New Changes to check tcs flag
		List<IcaBean> icaFinalList = new ArrayList<>();
		for (IcaBean ica : icaList) {

			boolean checkTcsFlagForIca = isIcaMarksSentToTcs(ica);
			if (checkTcsFlagForIca == false) {
				icaFinalList.add(ica);
			}
		}

		icaList.clear();
		icaList.addAll(icaFinalList);
		//

		for (IcaBean ica : icaList) {

			if (role.equals(Role.ROLE_FACULTY.name())) {
				PredefinedIcaComponent pred = predefinedIcaComponentService.findByComponentName("Portal Test");
				IcaComponent icaCompBean = new IcaComponent();
				if ("Y".equals(ica.getIsIcaDivisionWise())) {
					icaCompBean = icaComponentService.getCompBean(Long.valueOf(ica.getParentIcaId()), pred.getId());
				} else {
					icaCompBean = icaComponentService.getCompBean(ica.getId(), pred.getId());
				}
				if (icaCompBean != null) {
					ica.setShowTestMarksIcon("true");
				} else {
					ica.setShowTestMarksIcon("false");
				}
			} else {
				ica.setShowTestMarksIcon("false");
			}

			if ("Y".equals(ica.getIsPublishCompWise())) {
				
				IcaComponent icm = icaComponentService.getSubmittedIcaComponent(ica.getId());
				ica.setIsApproved("N");
				isQueryRaised=false;
				if(icm!=null) {
					IcaQueries iqBean = icaQueriesService.findByIcaIdAndCompId(String.valueOf(ica.getId()), icm.getComponentId());
					if(iqBean!=null) {
						if("Y".equals(iqBean.getIsApproved())) {
							isQueryRaised=true;
							ica.setIsApproved("Y");
						}
					}
					
				}
			} else {
				if (ica.getIcaQueryId() != null) {
					isQueryRaised = true;
				}
			}
			if (ica.getProgramId().contains(",")) {
				ica.setProgramName(programService.getProgramNamesForIca(ica.getProgramId()));
			}

			if (ica.getCampusId() != null) {
				IcaBean addCampusName = icaBeanService.findByCampusid(ica.getCampusId());
				ica.setCampusName(addCampusName.getCampusName());

				logger.info("Campus Name And id campusName=============>" + addCampusName.getCampusName());
			}

		}
		m.addAttribute("Program_Name", userdetails1.getProgramName());
		m.addAttribute("icaList", icaList);
		m.addAttribute("isQueryRaised", isQueryRaised);
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			return "ica/icaListFaculty";
		} else {
			return "ica/icaList";
		}
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/evaluateIca", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateIca(Model m, Principal principal, @RequestParam Long icaId, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		if (icaId != null) {
			icaBean.setId(icaId);
		}

		Map<String, String> studentIcaMarksMap = new HashMap<>();

		// Map<String,String> studentIcaTotalMarks = new HashMap<>();

		icaBean = icaBeanService.findByID(icaBean.getId());
		
		List<IcaComponent> icCompList =new ArrayList<>();
//		if(icaBean.getIsIcaDivisionWise().equals("Y")) 
//		{
//			icCompList= icaComponentService.icaComponentListByIcaId(Long.parseLong(icaBean.getParentIcaId()));
//		
//		}else {
//		
//			icCompList = icaComponentService.icaComponentListByIcaId(icaId);
//		}
		
		String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
		
		
		if (currentDate.compareTo(icaBean.getEndDate()) > 0) {
			setNote(redirectAttrs, "Cannot Evaluate, ICA Deadline is over");
			return "redirect:/searchIcaList";
		}

		if (currentDate.compareTo(icaBean.getStartDate()) < 0) {
			setNote(redirectAttrs, "Cannot Evaluate, ICA has not started yet.");
			return "redirect:/searchIcaList";
		}
//		if (icCompList.size() == 0) {
//			setNote(redirectAttrs,
//					"Cannot Evaluate!! Ica components have not been added.Kindly Contact Your Co-ordinator");
//			return "redirect:/searchIcaList";
//
//		}

		String raiseQComp = "";
		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			if ("Y".equals(icaBean.getIsSubmitted())) {

				IcaQueries icaQueryByIcaId = icaQueriesService.findByIcaId(icaBean.getId());

				if (icaQueryByIcaId != null) {
					if ("Y".equals(icaQueryByIcaId.getIsApproved())) {
						m.addAttribute("icaQuery", "true");
					} else {

						setNote(redirectAttrs, "ICA Reevaluate Not Approved By Co-ordinator");
						return "redirect:/searchIcaList";
					}
				} else {
					m.addAttribute("icaQuery", "false");
					setNote(redirectAttrs, "ICA Already Evaluated");
					return "redirect:/searchIcaList";
				}
			}
		} else {
			long idIca = icaBean.getId();

			IcaComponent submittedIc = icaComponentService.getSubmittedIcaComponent(idIca);
			int maxSubmittedSeqNo = icaComponentService.getSubmittedSeqNo(icaId);
			int maxSeqNo = icaComponentService.getMaxSeqNo(idIca);
			if(maxSeqNo==0) {
				setNote(redirectAttrs,	"Cannot Evaluate!! Ica components have not been added.Kindly Contact Your Co-ordinator");
			return "redirect:/searchIcaList";
			}

			if (submittedIc != null) {
				if ("Y".equals(submittedIc.getIsPublished())) {
					String dueDate = Utils.addDaysToDate(submittedIc.getPublishedDate(), 4);
					IcaQueries icQ = icaQueriesService.findByIcaIdAndCompId(String.valueOf(idIca),
							submittedIc.getComponentId());
					if (icQ != null) {
						if ("Y".equals(icQ.getIsApproved())) {
							if (!"Y".equals(icQ.getIsReEvaluated())) {

								m.addAttribute("icaQuery", "true");
								raiseQComp = submittedIc.getComponentId();

							} else {

								if (maxSeqNo == maxSubmittedSeqNo) {
									m.addAttribute("icaQuery", "false");
									setNote(redirectAttrs, "ICA Already Evaluated");
									return "redirect:/searchIcaList";
								}
								if (currentDate.compareTo(dueDate) < 0) {
									setNote(redirectAttrs,
											"You cannot evaluate next component during the raise query process,"
													+ " You can only re-evaluate the current component");
									return "redirect:/searchIcaList";
								}
							}
						} else {
							setNote(redirectAttrs, "ICA Component Reevaluation is not approved by co-ordinator");
							return "redirect:/searchIcaList";
						}
					} else {

						if (maxSeqNo == maxSubmittedSeqNo) {
							m.addAttribute("icaQuery", "false");
							setNote(redirectAttrs, "ICA Already Evaluated");
							return "redirect:/searchIcaList";
						}
						if (currentDate.compareTo(dueDate) < 0) {
							setNote(redirectAttrs, "You cannot evaluate next component during the raise query process");
							return "redirect:/searchIcaList";
						}
					}
				} else {
					setNote(redirectAttrs,
							"You cannot evalute next component unless your current component is published"
									+ " and re-evaluation process completes");
					return "redirect:/searchIcaList";
				}
			}
		}
		List<IcaComponent> icaComponentsByIcaId = new ArrayList<>();
		IcaComponent icaComp = new IcaComponent();
		int icaCompMarks = 0;

		if (!"Y".equalsIgnoreCase(icaBean.getIsPublishCompWise())) {
			if (icaBean.getParentIcaId() != null) {
				icaComponentsByIcaId = icaComponentService
						.icaComponentListByIcaId(Long.valueOf(icaBean.getParentIcaId()));
				if (icaComponentsByIcaId.size() > 0) {
					icaCompMarks = icaComponentService.icaTotalComponentMarks(Long.valueOf(icaBean.getParentIcaId()));
				}
			} else {
				icaComponentsByIcaId = icaComponentService.icaComponentListByIcaId(icaBean.getId());

				if (icaComponentsByIcaId.size() > 0) {
					icaCompMarks = icaComponentService.icaTotalComponentMarks(icaBean.getId());
				}
			}
		} else {

			icaComp = icaComponentService.icaComponentByIcaId(Long.valueOf(icaBean.getId()), raiseQComp);
			icaComponentsByIcaId.add(icaComp);
			if (icaComponentsByIcaId.size() > 0) {
				icaCompMarks = icaComponentService.icaTotalComponentMarks(Long.valueOf(icaBean.getId()));
			}

		}
		logger.info("icaBean IM" + icaBean.getInternalMarks() + "icaComp Marks" + icaCompMarks);

		if (icaComponentsByIcaId.size() == 0) {
			setNote(redirectAttrs,
					"Cannot Evaluate!! Ica components have not been added.Kindly Contact Your Co-ordinator");
			return "redirect:/searchIcaList";
		}

		if (Integer.parseInt(icaBean.getInternalMarks()) != icaCompMarks) {

			setError(redirectAttrs,
					"Cannot Evaluate!! Total Component Marks does not match with ICA Total Marks,Kindly Contact Your Co-ordinator");
			return "redirect:/searchIcaList";
		}

		/*
		 * int getIcaTestCompleted =
		 * icaBeanService.getIcaTestCompleted(icaBean.getId()); for (IcaComponent
		 * icaComp : icaComponentsByIcaId) { if
		 * ("Portal Test".equalsIgnoreCase(icaComp.getComponentName()) &&
		 * getIcaTestCompleted == 0) { redirectAttrs.addAttribute("icaId",
		 * icaBean.getId()); if ("Y".equals(icaBean.getIsIcaDivisionWise())) {
		 * redirectAttrs.addAttribute("courseId", icaBean.getEventId()); } return
		 * "redirect:/assignTestMarksToIcaForm"; } }
		 */
		List<IcaComponentMarks> icaComponentMarksByIcaId = new ArrayList<>();
		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByIcaId(icaBean.getId());
		} else {
			icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByIcaId(icaBean.getId(),
					icaComp.getComponentId());

		}
		List<IcaTotalMarks> icaTotalMarksByIcaId = icaTotalMarksService.icaTotalMarksByIcaId(icaBean.getId());

		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			if (icaTotalMarksByIcaId.size() > 0) {
				for (IcaTotalMarks i : icaTotalMarksByIcaId) {
					// Map<String, String> map = new HashMap<>();

					studentIcaMarksMap.put(i.getUsername() + "totalM", i.getIcaTotalMarks());
					studentIcaMarksMap.put(i.getUsername() + "scaleM", i.getIcaScaledMarks());
					studentIcaMarksMap.put(i.getUsername() + "remark", i.getRemarks());
					studentIcaMarksMap.put(i.getUsername() + "query", i.getQuery());

					// studentIcaMarksMap.put(i.getUsername(), map);

				}

			}
		} else {
			if (icaComponentMarksByIcaId.size() > 0) {
				for (IcaComponentMarks icm : icaComponentMarksByIcaId) {
					studentIcaMarksMap.put(icm.getUsername() + "remark", icm.getRemarks());
					studentIcaMarksMap.put(icm.getUsername() + "query", icm.getQuery());
				}
			}
		}

		if (icaComponentMarksByIcaId.size() > 0) {
			for (IcaComponentMarks ic : icaComponentMarksByIcaId) {

				studentIcaMarksMap.put(ic.getUsername() + "-" + ic.getComponentId(), ic.getMarks());
			}
		}
		String moduleName = courseService.getModuleName(icaBean.getModuleId());
		String moduleAbbr = courseService.getModuleAbbr(icaBean.getModuleId());
		List<UserCourse> studentsListForIca = new ArrayList<>();
		List<String> studentsBatchWise = new ArrayList<>();
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			studentsBatchWise = icaStudentBatchwiseService.getDistinctUsernamesByActiveIcaIdAndFaculty(icaBean.getId(),
					username);
		}
		if (studentsBatchWise.size() == 0) {
			if (icaBean.getEventId() != null) {
				studentsListForIca = userCourseService.findStudentByEventIdAndAcadYear(icaBean.getEventId(),
						icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(), icaBean.getId(),
						icaBean.getCampusId(), icaBean.getIsPublishCompWise(), icaComp.getComponentId());
			} else {

				if ("Y".equals(icaBean.getIsCourseraIca())) {
					studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYearCE(icaBean.getModuleId(),
							icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(), icaBean.getId(),
							icaBean.getCampusId(), icaBean.getIsPublishCompWise(), icaComp.getComponentId());
					logger.info("studentsListForIca-------" + studentsListForIca);

				} else {
					studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYear(icaBean.getModuleId(),
							icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(), icaBean.getId(),
							icaBean.getCampusId(), icaBean.getIsPublishCompWise(), icaComp.getComponentId());
				}

			}

		} else if ("Y".equals(icaBean.getIsCourseraIca())) {
			studentsListForIca = userCourseService.findStudentByStudentListAndIcaIdCE(studentsBatchWise, icaId,
					icaBean.getAcadSession(), icaBean.getIsPublishCompWise(), icaComp.getComponentId());
		} else {
			studentsListForIca = userCourseService.findStudentByStudentListAndIcaId(studentsBatchWise, icaBean.getId(),
					icaBean.getIsPublishCompWise(), icaComp.getComponentId());
			logger.info("studentsListForIcaBeforeSort--->" + studentsListForIca.size());
		}

		studentsListForIca.sort(Comparator.comparing(UserCourse::getRollNo));
		/*
		 * List<IcaComponent> icaComponents = icaComponentService
		 * .icaComponentListByIcaId(icaBean.getId());
		 */

		String filePathTemplate = generateTemplate(studentsListForIca, icaComponentsByIcaId, moduleAbbr, icaId,
				studentIcaMarksMap);

		if ("error".equals(filePathTemplate)) {
			setError(m, "Error in downloading template");
		}
		m.addAttribute("filePath", filePathTemplate);

		m.addAttribute("acadSession", studentsListForIca.get(0).getAcadSession());
		m.addAttribute("moduleName", moduleName);
		// icaBean.setUserCourseList(studentsListForIca);
		//
		String p = programService.getProgramNamesForIca(icaBean.getProgramId());
		m.addAttribute("programName", p);
		m.addAttribute("ica", icaBean);
		m.addAttribute("icaComponents", icaComponentsByIcaId);
		m.addAttribute("studentsListForIca", studentsListForIca);
		logger.info("studentmarks map" + studentIcaMarksMap);
		m.addAttribute("studentMarksMap", studentIcaMarksMap);

		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			return "ica/evaluateIca";
		} else {
			m.addAttribute("compId", icaComp.getComponentId());
			return "ica/evaluateIcaCompWise";
		}
	}

	public Map<String, IcaBean> getIcaComponents(Long icaId) {
		Map<String, IcaBean> mapper = new HashMap<>();
		List<IcaBean> icaCompList = icaBeanService.getComponentsForIca(icaId);
		for (IcaBean i : icaCompList) {
			mapper.put(i.getComponentName(), i);
		}

		return mapper;
	}

	public String generateTemplate(List<UserCourse> studentsListForIca, List<IcaComponent> icaComponents,
			String moduleName, Long icaId, Map<String, String> studentIcaCompMarks) {

		try {
			// String[] headers = new String[] { "Roll No", "Name", "SAPID" };
			PredefinedIcaComponent predIcaComp = predefinedIcaComponentService.findByComponentName("Portal Test");
			DataValidation dataValidation = null;
			DataValidationConstraint constraint = null;
			DataValidationHelper validationHelper = null;
			List<String> headers = new ArrayList<String>(Arrays.asList("Roll No", "Name", "SAPID", "Is Absent?"));
			// int count=3;
			for (IcaComponent ic : icaComponents) {
				headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
				// count++;
			}

			headers.add("REMARKS");
			XSSFWorkbook workbook = new XSSFWorkbook();
			/*
			 * Sheet sheet = workbook.createSheet("Upload Student ICA Marks - " +
			 * moduleName);
			 */

			XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Upload Student ICA Marks - " + moduleName);
			validationHelper = new XSSFDataValidationHelper(sheet);

			CellRangeAddressList addressList = new CellRangeAddressList(1, studentsListForIca.size(), 3, 3);

			constraint = validationHelper.createExplicitListConstraint(new String[] { "Y", "N" });
			dataValidation = validationHelper.createValidation(constraint, addressList);
			dataValidation.setSuppressDropDownArrow(true);
			sheet.addValidationData(dataValidation);
			Row r = sheet.createRow(0);

			int cellNoTest = 0;
			for (int rn = 0; rn < headers.size(); rn++) {

				r.createCell(rn).setCellValue(headers.get(rn));
				if (headers.get(rn).startsWith("Portal Test")) {
					logger.info("Portal Test header Found at" + rn);
					cellNoTest = rn;
				}
			}
			int i = 1;
			for (UserCourse uc : studentsListForIca) {
				Row row = sheet.createRow(i);

				row.createCell(0).setCellValue(String.valueOf(uc.getRollNo()));
				row.createCell(1).setCellValue(uc.getStudentName());
				row.createCell(2).setCellValue(uc.getUsername());
				row.createCell(3).setCellValue("N");

				if (cellNoTest != 0) {
					String key = uc.getUsername() + "-" + predIcaComp.getId();
					logger.info("student ica comp markss" + studentIcaCompMarks);
					row.createCell(cellNoTest).setCellValue(studentIcaCompMarks.get(key));
				}

				i++;
			}
			String folderPathS3 = baseDirS3 + "/" + app + "/" + "icaUploadMarkTemp";
			String folderPath = downloadAllFolder + "/" + "icaUploadMarkTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
			}
			String filePath = folderP.getAbsolutePath() + File.separator + "uploadMarksTemp" + icaId + ".xls";
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
			// 28-04-2020 Start
			amazonS3ClientService.uploadFile(f, folderPathS3);
			FileUtils.deleteQuietly(f);
			// 28-04-2020 End
			return filePath;
		} catch (Exception ex) {
			logger.error("Exception", ex);
			return "error";
		}
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveIcaAsDraft", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String saveIcaAsDraft(@RequestParam Map<String, String> allRequestParams, Principal p) {
		logger.info("allRequestParams----------------" + allRequestParams);
		try {
			logger.info("allRequestParams.get(\"icaIdValue\") is " + allRequestParams.get("icaIdValue"));
		BusinessBypassRule.validateNumericNotAZero(allRequestParams.get("icaIdValue"));
		
//		for (Entry<String, String> m : allRequestParams.entrySet()) {
//			
//			logger.info("key is " + m.getKey());
//			logger.info("value is " + m.getValue());
//			if(m.getValue() != null) {
//				
//				BusinessBypassRule.validateNumericNotAZero(m.getValue());
//			}
//		}
		
		List<IcaComponentMarks> icaCompMarksList = new ArrayList<>();
		List<IcaTotalMarks> icaTotalMarksList = new ArrayList<>();

		List<String> icaCompStudList = new ArrayList<>();
		List<String> icaTotalStudList = new ArrayList<>();

		
			for (String sapId : allRequestParams.keySet()) {

				if (sapId.contains("-")) {
					String[] splitKey = sapId.split("-");
					IcaComponentMarks icaCompMarks = createIcaComponent(splitKey, allRequestParams, "DRAFT", sapId,
							p.getName());
					logger.info("icaCompMarks is " + icaCompMarks.getMarks());
					BusinessBypassRule.validateNumeric(icaCompMarks.getMarks());
					IcaTotalMarks icaTotalMarks = createIcaTotalMark(splitKey, allRequestParams, "DRAFT", sapId,
							p.getName());
					logger.info("icaTotalMarks is " + icaTotalMarks);

					if (!allRequestParams.get(sapId).isEmpty() && allRequestParams.get(sapId) != null) {
						icaCompMarksList.add(icaCompMarks);
						icaTotalMarksList.add(icaTotalMarks);
						if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
							icaCompStudList.add(icaCompMarks.getUsername());
						}
						if (!icaTotalStudList.contains(icaTotalMarks.getUsername())) {
							icaTotalStudList.add(icaTotalMarks.getUsername());
						}
					}

				}

			}

			icaComponentMarksService.upsertBatch(icaCompMarksList);
			icaTotalMarksService.upsertBatch(icaTotalMarksList);

			if (!allRequestParams.get("icaIdValue").isEmpty() && allRequestParams.get("icaIdValue") != null) {
				List<String> studList = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
				if (studList.size() > 0) {
					List<String> compUsersCopy = new ArrayList();
					List<String> compUsers = icaComponentMarksService
							.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
					compUsers.addAll(icaCompStudList);
					compUsersCopy = compUsers.stream().distinct().collect(Collectors.toList());

					if (studList.size() != compUsersCopy.size()) {
						icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT",
								allRequestParams.get("icaIdValue"), "N");
						/*
						 * for(IcaComponentMarks icm : icaCompMarksList){ icm.setSaveAsDraft("N");
						 * icm.setFinalSubmit("N"); }
						 */
					} else {
						icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT",
								allRequestParams.get("icaIdValue"), "Y");
					}
					List<String> totalMarksUsersCopy = new ArrayList();
					List<String> totalMarksUsers = icaTotalMarksService
							.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
					totalMarksUsers.addAll(icaTotalStudList);
					totalMarksUsersCopy = totalMarksUsers.stream().distinct().collect(Collectors.toList());
					if (studList.size() != totalMarksUsersCopy.size()) {
						icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", allRequestParams.get("icaIdValue"),
								"N");
						/*
						 * for(IcaTotalMarks itm : icaTotalMarksList){ itm.setSaveAsDraft("N");
						 * itm.setFinalSubmit("N"); }
						 */
					} else {
						icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", allRequestParams.get("icaIdValue"),
								"Y");
					}
				}
			}

			return "saved";
			
		} catch (ValidationException ex) {
			logger.error("ValidationException", ex);
			return "validationError";
		} 
		catch (Exception ex) {
			logger.error("Exception", ex);
			return "error";
		}

	}

	public IcaComponentMarks createIcaComponent(String[] splitKey, Map<String, String> allRequestParams,
			String draftOrFinalSubmit, String sapId, String loggedInUser) throws ValidationException {
		IcaComponentMarks icaCompMarks = new IcaComponentMarks();
		logger.info("splitKey[0] is " + splitKey[0]);//Username
		logger.info("splitKey[1] is " + splitKey[1]);//ComponentId
		logger.info("splitKey[2] is " + splitKey[2]);//TotalMarks
		icaCompMarks.setUsername(splitKey[0]);
		
		icaCompMarks.setIcaId(allRequestParams.get("icaIdValue"));
		if(splitKey[1] != null) {
			IcaBean checkIfComponentIdExists = icaBeanService.checkIfComponentIdExists(splitKey[1]);
			if(checkIfComponentIdExists==null) {
				throw new ValidationException("Invalid Component");
			}
		}
		icaCompMarks.setComponentId(splitKey[1]);
		logger.info("createIcaComponent() - sapId is " + sapId);//username-componentId-totalMarks
		logger.info("createIcaComponent() - allRequestParams.get(sapId) is " + allRequestParams.get(sapId)); //Marks given
		BusinessBypassRule.validateNumeric(allRequestParams.get(sapId));
		icaCompMarks.setMarks(allRequestParams.get(sapId));
		if ("DRAFT".equals(draftOrFinalSubmit)) {
			icaCompMarks.setSaveAsDraft("Y");
		} else {
			icaCompMarks.setFinalSubmit("Y");
		}
		icaCompMarks.setActive("Y");
		icaCompMarks.setCreatedBy(loggedInUser);
		icaCompMarks.setLastModifiedBy(loggedInUser);
		if (allRequestParams.containsKey(splitKey[0] + "isAbsent")) {
			String isAbsent = allRequestParams.get(splitKey[0] + "isAbsent");
			icaCompMarks.setIsAbsent(isAbsent);

		}
		if (allRequestParams.containsKey(splitKey[0] + "isApproved")) {
			String isApproved = allRequestParams.get(splitKey[0] + "isApproved");
			icaCompMarks.setIsQueryApproved(isApproved);
		}
		return icaCompMarks;
	}

	public IcaTotalMarks createIcaTotalMark(String[] splitKey, Map<String, String> allRequestParams,
			String draftOrFinalSubmit, String sapId, String loggedInUser) throws ValidationException {
		String totalKey = "total" + splitKey[0];
		String scaleKey = "scale" + splitKey[0];
		String remarkKey = "remark" + splitKey[0];
		String isAbsent = allRequestParams.get(splitKey[0] + "isAbsent");
		String isQueryApproved = allRequestParams.get(splitKey[0] + "isApproved");
		IcaTotalMarks icaTotalMarks = new IcaTotalMarks();

		icaTotalMarks.setUsername(splitKey[0]);
		BusinessBypassRule.validateNumeric(allRequestParams.get(totalKey));

		icaTotalMarks.setIcaId(allRequestParams.get("icaIdValue"));
		logger.info("allRequestParams.get(totalKey) is " + allRequestParams.get(totalKey));
		if (allRequestParams.containsKey(totalKey)) {
			icaTotalMarks.setIcaTotalMarks(allRequestParams.get(totalKey));
		}
		if (allRequestParams.containsKey(scaleKey)) {
			icaTotalMarks.setIcaScaledMarks(allRequestParams.get(scaleKey));
		}
		if (allRequestParams.containsKey(remarkKey) && !allRequestParams.get(remarkKey).isEmpty()) {
			BusinessBypassRule.validateAlphaNumeric(allRequestParams.get(remarkKey));
			icaTotalMarks.setRemarks(allRequestParams.get(remarkKey));
		}
		if (!isAbsent.isEmpty()) {
			BusinessBypassRule.validateYesOrNo(isAbsent);
			icaTotalMarks.setIsAbsent(isAbsent);
		}
		icaTotalMarks.setActive("Y");
		icaTotalMarks.setIsQueryApproved(isQueryApproved);
		if ("DRAFT".equals(draftOrFinalSubmit)) {
			icaTotalMarks.setSaveAsDraft("Y");
		} else {
			icaTotalMarks.setFinalSubmit("Y");
		}
		icaTotalMarks.setCreatedBy(loggedInUser);
		icaTotalMarks.setLastModifiedBy(loggedInUser);

		return icaTotalMarks;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/submitIca", method = { RequestMethod.POST, RequestMethod.GET })
	public String submitIca(@RequestParam Map<String, String> allRequestParams, Principal p,
			RedirectAttributes redirectAttributes) {
		logger.info("allRequestParams----------------" + allRequestParams);
		String teeIdVal = allRequestParams.get("teeIdValue");
		try {
			BusinessBypassRule.validateNumeric(teeIdVal);

		String username = p.getName();
		List<IcaComponentMarks> icaCompMarksList = new ArrayList<>();
		List<IcaTotalMarks> icaTotalMarksList = new ArrayList<>();

		List<String> icaCompStudList = new ArrayList<>();
		List<String> icaTotalStudList = new ArrayList<>();
		boolean isAllSubmitted = true;
		redirectAttributes.addAttribute("icaId", allRequestParams.get("icaIdValue"));

		IcaBean icaDB = icaBeanService.findByID(Long.valueOf(allRequestParams.get("icaIdValue")));
		
			for (String sapId : allRequestParams.keySet()) {
				logger.info("sapId is " + sapId);
				if (sapId.contains("-")) {
					String[] splitKey = sapId.split("-");
					logger.info("splitKey is " + splitKey);
					IcaComponentMarks icaCompMarks = createIcaComponent(splitKey, allRequestParams, "SUBMIT", sapId,
							p.getName());
					BusinessBypassRule.validateNumeric(icaCompMarks.getMarks());

					IcaTotalMarks icaTotalMarks = createIcaTotalMark(splitKey, allRequestParams, "SUBMIT", sapId,
							p.getName());

					if (!allRequestParams.get(sapId).isEmpty() && allRequestParams.get(sapId) != null) {
						icaCompMarksList.add(icaCompMarks);
						icaTotalMarksList.add(icaTotalMarks);
						if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
							icaCompStudList.add(icaCompMarks.getUsername());
						}
						if (!icaTotalStudList.contains(icaTotalMarks.getUsername())) {
							icaTotalStudList.add(icaTotalMarks.getUsername());
						}
					}

				}

			}

			icaComponentMarksService.upsertBatch(icaCompMarksList);
			icaTotalMarksService.upsertBatch(icaTotalMarksList);

			if (!allRequestParams.get("icaIdValue").isEmpty() && allRequestParams.get("icaIdValue") != null) {
				List<String> studList = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
				List<String> remainingStudList = icaStudentBatchwiseService
						.getDistinctActiveUsernamesOfRemainingFaculty(Long.valueOf(allRequestParams.get("icaIdValue")),
								p.getName());
				if (studList.size() > 0) {
					List<String> compUsersCopy = new ArrayList();
					List<String> compUsers = icaComponentMarksService
							.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
					compUsers.addAll(icaCompStudList);
					compUsersCopy = compUsers.stream().distinct().collect(Collectors.toList());

					if (studList.size() != compUsersCopy.size()) {
						isAllSubmitted = false;
						/*
						 * icaComponentMarksService .updateSaveAsDraftOrFinalSubmit("SUBMIT",
						 * allRequestParams.get("icaIdValue"), "N");
						 */

						icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList,
								allRequestParams.get("icaIdValue"));

						/*
						 * for(IcaComponentMarks icm : icaCompMarksList){ icm.setSaveAsDraft("N");
						 * icm.setFinalSubmit("N"); }
						 */
					} else {
						List<String> finalSubmitCompoMarksList = icaComponentMarksService
								.getFinalSubmitByIcaIdAndUserList(allRequestParams.get("icaIdValue"),
										remainingStudList);
						if (finalSubmitCompoMarksList.size() == 1) {
							if (("Y").equalsIgnoreCase(finalSubmitCompoMarksList.get(0))) {
								isAllSubmitted = true;
								icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT",
										allRequestParams.get("icaIdValue"), "Y");
							} else {
								isAllSubmitted = false;
								icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList,
										allRequestParams.get("icaIdValue"));
							}
						} else {
							isAllSubmitted = false;
							icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList,
									allRequestParams.get("icaIdValue"));
						}

					}
					List<String> totalMarksUsersCopy = new ArrayList();
					List<String> totalMarksUsers = icaTotalMarksService
							.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
					totalMarksUsers.addAll(icaTotalStudList);
					totalMarksUsersCopy = totalMarksUsers.stream().distinct().collect(Collectors.toList());
					if (studList.size() != totalMarksUsersCopy.size()) {
						isAllSubmitted = false;
						/*
						 * icaTotalMarksService.updateSaveAsDraftOrFinalSubmit( "SUBMIT",
						 * allRequestParams.get("icaIdValue"), "N");
						 */
						icaTotalMarksService.updateFinalSubmitByUserList(icaTotalStudList,
								allRequestParams.get("icaIdValue"));
						/*
						 * for(IcaTotalMarks itm : icaTotalMarksList){ itm.setSaveAsDraft("N");
						 * itm.setFinalSubmit("N"); }
						 */
					} else {
						List<String> finalSubmitTotalMarksList = icaTotalMarksService.getFinalSubmitByIcaIdAndUserList(
								allRequestParams.get("icaIdValue"), remainingStudList);
						if (finalSubmitTotalMarksList.size() == 1) {
							if (("Y").equalsIgnoreCase(finalSubmitTotalMarksList.get(0))) {
								isAllSubmitted = true;
								icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT",
										allRequestParams.get("icaIdValue"), "Y");
							} else {
								isAllSubmitted = false;
								icaTotalMarksService.updateFinalSubmitByUserList(icaTotalStudList,
										allRequestParams.get("icaIdValue"));
							}
						} else {
							isAllSubmitted = false;
							icaTotalMarksService.updateFinalSubmitByUserList(icaTotalStudList,
									allRequestParams.get("icaIdValue"));
						}
					}
				}
			}

			if (isAllSubmitted) {
				icaBeanService.updateIcaToSubmitted(allRequestParams.get("icaIdValue"), Utils.getInIST());
			}
			setSuccess(redirectAttributes, "ICA Marks Submitted Successfully");
			try {
				List<IcaTotalMarks> reevalStudent = icaTotalMarksService
						.getIsReevalIcaUsername(String.valueOf(icaDB.getId()));
				if (reevalStudent.size() > 0) {

					icaQueriesService.updateReEvaluated(icaDB.getId());

					User u = userService.findByUserName(username);
					String moduleName = "";
					if (icaDB.getIsNonEventModule().equals("Y")) {
						moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
					} else {
						moduleName = courseService.getModuleName(icaDB.getModuleId());
					}
					String subject = " ICA Marks Re-evaluated status changed for Subject " + moduleName;
					Map<String, Map<String, String>> result = null;
					List<String> userList = new ArrayList<String>();
					List<String> acadSessionList = new ArrayList<>();
					// userList.addAll(reevalStudent);
					String notificationEmailMessage = "";
					for (IcaTotalMarks uc : reevalStudent) {
						userList.clear();
						userList.add(uc.getUsername());
						if (!userList.isEmpty()) {
							notificationEmailMessage = "<html><body>" + "<b>Query Status: </b> ? <br>"
									+ "<b>Remakrs: </b>" + uc.getRemarks() + "<br><br>"
									// + "<b>Note: </b>This ICA is created by : ?? <br>"
									+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
									+ "This is auto-generated email, do not reply on this.</body></html>";
							if (uc.getIsQueryApproved().equals("Y")) {
								notificationEmailMessage = notificationEmailMessage.toString().replace("?",
										" Approved and Re-evaluated ");
							} else if (uc.getIsQueryApproved().equals("N")) {
								notificationEmailMessage = notificationEmailMessage.toString().replace("?",
										" Rejected ");
							}
							result = userService.findEmailByUserName(userList);
							Map<String, String> email = result.get("emails");
							Map<String, String> mobiles = new HashMap();
							logger.info("email -----> " + email);
							logger.info("mobile -----> " + mobiles);
							logger.info("subject -----> " + subject);
							logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
							//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
						}
					}

				} else {
					User u = userService.findByUserName(username);
					String subject = " ICA Marks Submitted: " + icaDB.getIcaName();
					Map<String, Map<String, String>> result = null;
					List<String> userList = new ArrayList<String>();
					userList.add(icaDB.getCreatedBy());
					String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName()
							+ " <br>"
							// + "<b>ICA Description: </b>"+ icaBeanDB.getIcaDesc() +"<br><br>"
							+ "<b>Note: </b>This ICA marks is submitted by : ?? <br>"
							+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
							+ "This is auto-generated email, do not reply on this.</body></html>";

					if (!userList.isEmpty()) {
						notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
								+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Error in Sending Email to Student--->" + e);
				setError(redirectAttributes, "Error in Sending Email to Student");
			}
			return "redirect:/showEvaluatedInternalMarks";
		} 
		catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			setError(redirectAttributes, ve.getMessage());
			return "redirect:/evaluateIca";
		}
		catch (Exception ex) {
			setError(redirectAttributes, "Error in Submitting ICA Marks");
			logger.error("Exception", ex);

			return "redirect:/evaluateIca";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/publishIca", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishIca(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		/*
		 * List<IcaBean> submittedIcaList = icaBeanService .getSubmittedIca(username);
		 */

		List<IcaBean> submittedIcaList = icaBeanService.getSubmittedIcaForBatchWise(username);

		List<IcaBean> submittedIcaListForNonEvent = icaBeanService.getSubmittedIcaForNonEvent(username);

		if (submittedIcaListForNonEvent.size() > 0) {
			for (IcaBean ica : submittedIcaListForNonEvent) {
				if (!submittedIcaList.stream()
						.filter(o -> o.getModuleId().equals(ica.getModuleId())
								&& o.getProgramId().equals(ica.getProgramId())
								&& o.getAcadYear().equals(ica.getAcadYear()))
						.findFirst().isPresent()) {
					submittedIcaList.add(ica);
				}
			}
		}

		//
		List<IcaBean> finalIcaBeanList = new ArrayList<>();
		for (IcaBean i : submittedIcaList) {

			boolean isIcaSentToTcs = isIcaMarksSentToTcs(i);

			if (isIcaSentToTcs == false) {
				finalIcaBeanList.add(i);
			}
		}
		submittedIcaList.clear();
		submittedIcaList.addAll(finalIcaBeanList);
		//
		for (IcaBean tb : submittedIcaList) {
			tb.setFacultyName(teeBeanService.getFacultyNameByUsername(tb.getAssignedFaculty()));
		}
		m.addAttribute("submittedIcaList", submittedIcaList);

		String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
		m.addAttribute("currentDate", Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
		return "ica/publishIca";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/publishAllIca", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishAllIca(Model m, Principal principal, RedirectAttributes redirectAttrs) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		try {
			List<String> submittedIcaList = icaBeanService.getSubmittedIcaIds(username);
			String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			icaBeanService.updateMultipleIcaToPublished(submittedIcaList, Utils.getInIST(),
					Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
			User u = userService.findByUserName(username);
			for (String s : submittedIcaList) {
				IcaBean icaDB = icaBeanService.findByID(Long.valueOf(s));
				/********* StudentList *********/
				List<String> userList = new ArrayList<String>();
				List<UserCourse> studentsListForIca = new ArrayList<>();
				if (icaDB.getEventId() != null) {
					studentsListForIca = userCourseService.findStudentByEventIdAndAcadYear(icaDB.getEventId(),
							icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
							icaDB.getCampusId(), "N", null);
				} else {
					studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYear(icaDB.getModuleId(),
							icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
							icaDB.getCampusId(), "N", null);
				}
				// for(UserCourse uc: studentsListForIca){
				// userList.add(uc.getUsername());
				// }

				// Email for student
				String moduleName = "";
				if (icaDB.getIsNonEventModule().equals("Y")) {
					moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
				} else {
					moduleName = courseService.getModuleName(icaDB.getModuleId());
				}
				String subject = " ICA marks is published for Subject : " + moduleName;
				Map<String, Map<String, String>> result = null;
				String notificationEmailMessage = "<html><body>"
						// + "<b>ICA Name: </b>"+icaDB.getIcaName()+" <br>"
						// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						// + "ICA Marks is published.<br>"
						+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";
				for (UserCourse uc : studentsListForIca) {
					userList.clear();
					userList.add(uc.getUsername());
					if (!userList.isEmpty()) {
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}

				// Email For faculty
				subject = " ICA marks is published for Subject : " + moduleName;
				result = null;
				userList.clear();
				if (null != icaDB.getAssignedFaculty()) {
					if (icaDB.getAssignedFaculty().contains(",")) {
						List<String> faculties = new ArrayList<String>();
						faculties = Arrays.asList(icaDB.getAssignedFaculty().split(","));
						userList.addAll(faculties);
					} else {
						userList.add(icaDB.getAssignedFaculty());
					}
				}
				// userList.add(icaDB.getAssignedFaculty());
				notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName() + " <br>"
				// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This ICA is published by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				for (String s1 : userList) {
					if (!userList.isEmpty()) {
						List<String> userList1 = new ArrayList<String>();
						userList1.add(s1);

						notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
								+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
						result = userService.findEmailByUserName(userList1);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}
			}

			setSuccess(redirectAttrs, "All Ica's Published");

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in publishing all Ica");
			logger.error("Exception", ex);
		}

		return "redirect:/publishIca";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/publishOneIca", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String publishOneIca(Model m, Principal principal, @RequestParam String id) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		try {
			String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			int updated = icaBeanService.updateIcaToPublished(id, Utils.getInIST(),
					Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));

			User u = userService.findByUserName(username);
			IcaBean icaDB = icaBeanService.findByID(Long.valueOf(id));
			/********* StudentList *********/
			List<String> userList = new ArrayList<String>();
			List<UserCourse> studentsListForIca = new ArrayList<>();
			if (icaDB.getEventId() != null) {
				studentsListForIca = userCourseService.findStudentByEventIdAndAcadYear(icaDB.getEventId(),
						icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
						icaDB.getCampusId(), "N", null);
			} else {
				studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYear(icaDB.getModuleId(),
						icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
						icaDB.getCampusId(), "N", null);
			}
			// Email for student
			String moduleName = "";
			if (icaDB.getIsNonEventModule().equals("Y")) {
				moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
			} else {
				moduleName = courseService.getModuleName(icaDB.getModuleId());
			}
			String subject = " ICA marks is published for Subject : " + moduleName;
			Map<String, Map<String, String>> result = null;
			String notificationEmailMessage = "<html><body>"
					// + "<b>ICA Name: </b>"+icaDB.getIcaName()+" <br>"
					// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					// + "ICA Marks is published.<br>"
					+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			for (UserCourse uc : studentsListForIca) {
				userList.clear();
				userList.add(uc.getUsername());
				if (!userList.isEmpty()) {
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}

			// Email For faculty
			subject = " ICA marks is published for Subject : " + moduleName;
			result = null;
			userList.clear();
			if (null != icaDB.getAssignedFaculty()) {
				if (icaDB.getAssignedFaculty().contains(",")) {
					List<String> faculties = new ArrayList<String>();
					faculties = Arrays.asList(icaDB.getAssignedFaculty().split(","));
					userList.addAll(faculties);
				} else {
					userList.add(icaDB.getAssignedFaculty());
				}
			}
			// userList.add(icaDB.getAssignedFaculty());
			notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName() + " <br>"
			// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					+ "<b>Note: </b>This ICA is published by : ?? <br>"
					+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";
			for (String s : userList) {
				if (!userList.isEmpty()) {
					List<String> userList1 = new ArrayList<String>();
					userList1.add(s);

					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList1);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}
			return "success";
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "error";
		}

	}

	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/showInternalMarks", method = { RequestMethod.GET, RequestMethod.POST })
	public String showInternalMarks(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		List<IcaComponentMarks> componentMarksForStuent = icaComponentMarksService.getIcaComponentMarksByUser(username);
		List<IcaComponentMarks> publishedCompMarksForStudent = icaComponentMarksService
				.getIcaPublishedCompMarks(username);
		logger.info("componentMarksForStuent---------------------->" + componentMarksForStuent);
		List<IcaComponentMarks> componentMarksForStuentForNonEvent = icaComponentMarksService
				.getIcaComponentMarksByUserForNonEvent(username);

		List<IcaComponentMarks> publishedCompMarksForNE = icaComponentMarksService
				.getIcaPublishedComponentMarksByUserForNonEvent(username);

		componentMarksForStuent.addAll(publishedCompMarksForStudent);
		componentMarksForStuent.addAll(publishedCompMarksForNE);
		componentMarksForStuent.addAll(componentMarksForStuentForNonEvent);

		List<IcaComponentMarks> listWithoutDuplicates = componentMarksForStuent.stream().distinct()
				.collect(Collectors.toList());

		logger.info("componentMarksForStuent---------------------->1" + listWithoutDuplicates);
		logger.info("componentMarksForStuent---------------------->1" + componentMarksForStuent);

		Map<String, String> componentsMap = new HashMap<>();
		for (IcaComponentMarks icm : componentMarksForStuent) {
			if (!componentsMap.containsKey(icm.getComponentId())) {
				componentsMap.put(icm.getComponentId(), icm.getComponentName());
			}
		}

		String acadSession = userService.findByUserName(username).getAcadSession();

		/*
		 * List<IcaTotalMarks> icaTotalMarksForStudent = icaTotalMarksService
		 * .getIcaTotalMarksByUserCoursera(acadSession,username);
		 */

		List<IcaTotalMarks> icaTotalMarksForStudent = icaTotalMarksService.getIcaTotalMarksByUserCoursera(username);

		/*
		 * List<IcaTotalMarks> icaTotalMarksForStudent = icaTotalMarksService
		 * .getIcaTotalMarksByUser(username);
		 */
		logger.info("icaTotalMarksForStudent---------->" + icaTotalMarksForStudent);
		List<IcaTotalMarks> icaTotalMarksForStudentForNonEvent = icaTotalMarksService
				.getIcaTotalMarksByUserForNonEvent(username);
		logger.info("icaTotalMarksForStudentForNonEvent---------->" + icaTotalMarksForStudentForNonEvent);

		icaTotalMarksForStudent.addAll(icaTotalMarksForStudentForNonEvent);

		List<String> iceIds = icaTotalMarksForStudent.stream().distinct().map(map -> map.getIcaId())
				.collect(Collectors.toList());
		List<String> iceIdsComp = componentMarksForStuent.stream().distinct().map(map -> map.getIcaId())
				.collect(Collectors.toList());

		List<String> remainingIcaIds = new ArrayList<>();

		if (iceIds.size() != iceIdsComp.size()) {

			remainingIcaIds = iceIdsComp.stream().distinct().filter(e -> !iceIds.contains(e))
					.collect(Collectors.toList());
		}

		logger.info("iceIds----------------22" + iceIds);
		logger.info("componentMarksForStuent----------------22" + componentMarksForStuent.size());

		/*
		 * Map<String, String> componentsMapIca = componentMarksForStuent.stream()
		 * .collect( Collectors.toMap(IcaComponentMarks::getComponentId,
		 * IcaComponentMarks::getComponentName));
		 */

		Map<String, Map<String, String>> mapOfComponentsMarksByIcaId = new HashMap<>();
		Map<String, String> dateSpanMap = new HashMap<>();
		for (String i : iceIdsComp) {
			logger.info("iceIds----------------loop" + iceIds + " " + i);

			Map<String, String> componentsMapIca = componentMarksForStuent.stream().filter(o -> o.getIcaId().equals(i))
					// New Chnages for avoiding dublicate
					// .filter( Utils.distinctByKey(p -> p.getIcaId().equals(i)) )
					.collect(Collectors.toMap(IcaComponentMarks::getComponentId, IcaComponentMarks::getMarks));

			logger.info("componentsMapIca111----------------loop" + componentsMapIca);
			logger.info("mapOfComponentsMarksByIcaId222----------------loop" + mapOfComponentsMarksByIcaId);
			/*
			 * Map<String, String> componentsMapIca = componentMarksForStuent.stream()
			 * .collect( Collectors.toMap(IcaComponentMarks::getComponentId,
			 * IcaComponentMarks::getComponentName));
			 */
			mapOfComponentsMarksByIcaId.put(i, componentsMapIca);

		}

		String currentDate = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());

		Map<String, IcaQueries> raiseQueryStatus = new HashMap<>();

		for (IcaTotalMarks itm : icaTotalMarksForStudent) {
			String publishedDate = itm.getPublishedDate();

			itm.setDueDate(Utils.addDaysToDate(publishedDate, 3));

			String raiseButton = itm.getDueDate().compareTo(currentDate) >= 0 ? "showButton" : "disableButton";
			dateSpanMap.put(itm.getIcaId(), raiseButton);
			IcaBean icaBeanDB = icaBeanService.findByID(Long.valueOf(itm.getIcaId()));
			IcaQueries raiseQryStatus = new IcaQueries();
			if ("Y".equals(icaBeanDB.getIsPublishCompWise())) {
				IcaComponent icaComponent = icaComponentService.getSubmittedIcaComponent(Long.valueOf(itm.getIcaId()));

				raiseQryStatus = icaQueriesService.findByIcaIdAndCompId(itm.getIcaId(), icaComponent.getComponentId());
				if (icaComponent != null) {
					IcaComponentMarks icmMarks = icaComponentMarksService.getIcaCompMarksByUsername(itm.getIcaId(),
							icaComponent.getComponentId(), username);
					itm.setIsQueryRaise(icmMarks.getIsQueryRaise());
					itm.setIsQueryApproved(icmMarks.getIsQueryApproved());
					itm.setQuery(icmMarks.getQuery());

				}
			} else {
				
				//IcaQueries raiseQryIsApproveStatus = new IcaQueries();
				
				raiseQryStatus=icaQueriesService.findByIcaIdIsApproved(itm.getIcaId());
				
				if(null==raiseQryStatus )
				{
				raiseQryStatus = icaQueriesService.findByIcaId(Long.valueOf(itm.getIcaId()));
				}
				
			}
			if (null != raiseQryStatus) {

				raiseQueryStatus.put(itm.getIcaId(), raiseQryStatus);
			}
		}

		if (remainingIcaIds.size() > 0) {

			logger.info("only comp evaluation exists:" + remainingIcaIds);
			for (String id : remainingIcaIds) {
				IcaBean icaBeanDB = icaBeanService.findByID(Long.valueOf(id));
				String courseName = "";
				Course courseDB = courseService.findByModuleIdAndAcadYear(icaBeanDB.getModuleId(),
						icaBeanDB.getAcadYear());
				if (courseDB == null) {
					courseDB = courseService.findByModuleIdAndAcadYearCode(icaBeanDB.getModuleId(),
							icaBeanDB.getAcadYear());
					if (courseDB != null) {
						courseName = courseDB.getModuleName();
					}
				}
				if (courseDB != null) {
					courseName = courseDB.getModuleName();
				} else {
					courseName = courseService.getModuleNameForNonEvent(icaBeanDB.getModuleId());
				}
				Map<String, String> getCompMap = mapOfComponentsMarksByIcaId.get(id);
				// for (String mulId : getCompMap.keySet()) {
				IcaTotalMarks itmIds = new IcaTotalMarks();
				itmIds.setIcaId(id);
				if (!icaBeanDB.getAcadSession().contains(",")) {
					itmIds.setAcadSession(icaBeanDB.getAcadSession());
				} else {
					UserCourse uc = userCourseService.getMappingByUsernameAndModule(username, icaBeanDB.getModuleId());
					itmIds.setAcadSession(uc.getAcadSession());
				}

				itmIds.setAcadYear(icaBeanDB.getAcadYear());
				itmIds.setModuleName(courseName);

				IcaComponent icomp = icaComponentService.getSubmittedIcaComponent(Long.valueOf(id));
				String pageKey = id + "-" + icomp.getComponentId();
				IcaComponentMarks icm = icaComponentMarksService.getIcaCompMarksByUsername(id, icomp.getComponentId(),
						username);
				// IcaComponent icomp = icaComponentService.getCompBean(Long.valueOf(id),
				// Long.valueOf(mulId));
				String publishedDate = icomp.getPublishedDate();
				itmIds.setPublishedDate(publishedDate);
				itmIds.setDueDate(Utils.addDaysToDate(publishedDate, 3));
				itmIds.setIsComponentMark("Y");
				itmIds.setCompId(icomp.getComponentId());
				itmIds.setIsQueryRaise(icm.getIsQueryRaise());
				itmIds.setPageKey(pageKey);
				itmIds.setRemarks(icm.getRemarks());
				itmIds.setQuery(icm.getQuery());
				String raiseButton = itmIds.getDueDate().compareTo(currentDate) >= 0 ? "showButton" : "disableButton";
				dateSpanMap.put(pageKey, raiseButton);
				icaTotalMarksForStudent.add(itmIds);
				IcaQueries raiseQryStatus = icaQueriesService.findByIcaIdAndCompId(id, icomp.getComponentId());
				if (null != raiseQryStatus) {

					raiseQueryStatus.put(pageKey, raiseQryStatus);
				}
				// }

			}
		}
		m.addAttribute("raiseQueryStatus", raiseQueryStatus);

		logger.info("Check 1----------------loop" + mapOfComponentsMarksByIcaId);
		logger.info("Check 2----------------loop" + componentsMap);
		m.addAttribute("dateSpanMap", dateSpanMap);
		m.addAttribute("mapComponent", mapOfComponentsMarksByIcaId);
		m.addAttribute("totalMarks", icaTotalMarksForStudent);
		m.addAttribute("componentsMap", componentsMap);
		m.addAttribute("user", userService.findByUserName(username));
		logger.info(" userService----------------Datataa" + userService.findByUserName(username));
		// m.addAttribute("publishDate",publishedDate);

		return "ica/showInternalMarks";
	}

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/showIcaQueries", method = { RequestMethod.GET, RequestMethod.POST })
	public String showIcaQueries(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		List<IcaBean> facultyName = icaBeanService.getfacultyNameMap(username);
		Map<String, String> map = new HashMap<String, String>();
		for (IcaBean ib : facultyName) {
			map.put(ib.getAssignedFaculty(), ib.getFacultyName());
		}

		List<IcaBean> icaQueries = icaBeanService.getIcaQueries(username);
		icaQueries.forEach(i -> i.setFacultyName(map.get(i.getAssignedFaculty())));

		List<IcaBean> icaQueriesNonEvent = icaBeanService.getIcaQueriesForNonEvent(username);

		if (icaQueriesNonEvent.size() > 0) {
			icaQueries.addAll(icaQueriesNonEvent);
		}

		//

		List<IcaBean> finalIcaBeanList = new ArrayList<>();
		for (IcaBean i : icaQueries) {

			boolean isIcaSentToTcs = isIcaMarksSentToTcs(i);

			if (isIcaSentToTcs == false) {
				if (i.getComponentId() != null) {
					i.setComponentName(predefinedIcaComponentService.findByID(Long.valueOf(i.getComponentId()))
							.getComponentName());
				} else {
					i.setComponentName("NA");
				}
				finalIcaBeanList.add(i);
			}
		}
		icaQueries.clear();
		icaQueries.addAll(finalIcaBeanList);

		//

		m.addAttribute("icaQueries", icaQueries);
		String isAllApproved = "true";

		for (IcaBean ic : icaQueries) {
			if (!"Y".equals(ic.getIsApproved())) {
				isAllApproved = "false";
			}
		}

		String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
		m.addAttribute("currentDate", Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
		m.addAttribute("isAllApproved", isAllApproved);
		return "ica/icaQueries";
	}

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/approveIcaForReeval", method = { RequestMethod.POST, RequestMethod.GET })
	public String approveIcaForReeval(@RequestParam(name = "file", required = true) List<MultipartFile> input,
			@RequestParam(name = "icaId") String icaId, @RequestParam(name = "compId", required = false) String compId,
			Principal p, RedirectAttributes redirectAttributes) {
		logger.info("allRequestParams----------------" + icaId + "file multipart" + input);
		String username = p.getName();
		try {
			String multipleFilePath = "";
			int errCount = 0;
			int i = 0;
			for (MultipartFile file : input) {
				if (!file.isEmpty()) {
					// 28-04-2020 Start
					//Audit change start
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttributes, "File uploaded is invalid!");
							return "redirect:/showIcaQueries";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/showIcaQueries";
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
								String filePath = baseDirS3 + "/" + "ICAUploads";
					Map<String, String> returnMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file,
							filePath);
					if (!returnMap.containsKey("ERROR")) {
						if (input.size() == 1) {
							multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
						} else {
							if (i == 0) {
								multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
								i++;
							} else {
								multipleFilePath = multipleFilePath + "," + filePath + "/" + returnMap.get("SUCCESS");
							}
						}
					} else {
						errCount++;
					}
								} else {
									setError(redirectAttributes, "File uploaded is invalid!");
									return "redirect:/showIcaQueries";
								}
							}
						}
					}else {
						setError(redirectAttributes, "File uploaded is invalid!");
						return "redirect:/showIcaQueries";
					}
					//Audit change end
//					String filePath = uploadIcaApprovalFile(file);
//
//					if (!filePath.equals("Error")) {
//						if (input.size() == 1) {
//							multipleFilePath = filePath;
//						} else {
//							if (i == 0) {
//								multipleFilePath = filePath;
//								i++;
//							} else {
//								multipleFilePath = multipleFilePath + ","
//										+ filePath;
//							}
//						}
//					} else {
//						errCount++;
//					}
					// 28-04-2020 End
				}

			}

			if (errCount > 0) {
				// logger.error("Exception",ex);
				setError(redirectAttributes, "Error in Approving ICA File");
				return "redirect:/showIcaQueries";
			} else {

				icaQueriesService.updateIsApproved(icaId, p.getName(), multipleFilePath, compId);

				IcaBean icaDB = icaBeanService.findByID(Long.valueOf(icaId));
				List<String> userList = new ArrayList<String>();
				// Student
				/*
				 * List<IcaQueries> studentList =
				 * icaQueriesService.findAllQueriesByIcaId(Long.valueOf(icaId)); for(IcaQueries
				 * iq: studentList){ userList.add(iq.getCreatedBy()); }
				 */
				// Faculty
				User u = userService.findByUserName(username);
				String moduleName = "";
				if ("Y".equals(icaDB.getIsNonEventModule())) {
					moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
				} else {
					moduleName = courseService.getModuleName(icaDB.getModuleId());
				}
				String subject = " ICA raised query approved for subject: " + moduleName;
				Map<String, Map<String, String>> result = null;
				if (icaDB.getAssignedFaculty().contains(",")) {
					List<String> faculties = new ArrayList<String>();
					faculties = Arrays.asList(icaDB.getAssignedFaculty().split(","));
					userList.addAll(faculties);
				} else {
					userList.add(icaDB.getAssignedFaculty());
				}
				// userList.add(icaDB.getAssignedFaculty());
				String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName() + " <br>"
				// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This ICA query approved by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				for (String s : userList) {
					if (!userList.isEmpty()) {
						List<String> userList1 = new ArrayList<String>();
						userList1.add(s);
						notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
								+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
						result = userService.findEmailByUserName(userList1);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}

				setSuccess(redirectAttributes, "ICA Approved Successfully");
			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
			setError(redirectAttributes, "Error in Approving ICA");
		}

		return "redirect:/showIcaQueries";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/approveAllIcaForReeval", method = { RequestMethod.POST, RequestMethod.GET })
	public String approveAllIcaForReeval(@RequestParam(name = "file", required = true) List<MultipartFile> input,
			Principal p, RedirectAttributes redirectAttributes) {
		String username = p.getName();
		try {
			String multipleFilePath = "";
			int errCount = 0;
			int i = 0;
			for (MultipartFile file : input) {
				if (!file.isEmpty()) {
					// 28-04-2020 Start
					//Audit change start
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							errCount++;
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								errCount++;
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
								String filePath = baseDirS3 + "/" + "ICAUploads";
								Map<String, String> returnMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file,
										filePath);
								if (!returnMap.containsKey("ERROR")) {
									if (input.size() == 1) {
										multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
									} else {
										if (i == 0) {
											multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
											i++;
										} else {
											multipleFilePath = multipleFilePath + "," + filePath + "/" + returnMap.get("SUCCESS");
										}
									}
								} else {
									errCount++;
								}
								} else {
									errCount++;
								}
							}
						}
					}else {
						errCount++;
					}
					//Audit change end
//					String filePath = uploadIcaApprovalFile(file);
//
//					if (!filePath.equals("Error")) {
//						if (input.size() == 1) {
//							multipleFilePath = filePath;
//						} else {
//							if (i == 0) {
//								multipleFilePath = filePath;
//								i++;
//							} else {
//								multipleFilePath = multipleFilePath + ","
//										+ filePath;
//							}
//						}
//					} else {
//						errCount++;
//					}
					// 28-04-2020 End
				}

			}

			if (errCount > 0) {
				// logger.error("Exception",ex);
				setError(redirectAttributes, "Error in Approving ICA File");
				return "redirect:/showIcaQueries";
			} else {

				// List<IcaBean> icaQueries = icaBeanService.getIcaQueries(p.getName());
				List<IcaBean> icaQueries = icaBeanService.getIcaQueriesForApproveAll(p.getName());

				for (IcaBean ic : icaQueries) {

					icaQueriesService.updateIsApproved(String.valueOf(ic.getId()), p.getName(), multipleFilePath,
							ic.getComponentId());
					// icaQueryList.add(icaQuery);
				}

				// icaQueriesService.upsertBatch(icaQueryList);
				for (IcaBean ib : icaQueries) {
					IcaBean icaDB = icaBeanService.findByID(ib.getId());
					List<String> userList = new ArrayList<String>();
					// Student
					/*
					 * List<IcaQueries> studentList =
					 * icaQueriesService.findAllQueriesByIcaId(Long.valueOf(icaId)); for(IcaQueries
					 * iq: studentList){ userList.add(iq.getCreatedBy()); }
					 */
					// Faculty
					User u = userService.findByUserName(username);
					String moduleName = "";
					if (icaDB.getIsNonEventModule().equals("Y")) {
						moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
					} else {
						moduleName = courseService.getModuleName(icaDB.getModuleId());
					}
					String subject = " ICA query approved for subject: " + moduleName;
					Map<String, Map<String, String>> result = null;
					if (icaDB.getAssignedFaculty().contains(",")) {
						List<String> faculties = new ArrayList<String>();
						faculties = Arrays.asList(icaDB.getAssignedFaculty().split(","));
						userList.addAll(faculties);
					} else {
						userList.add(icaDB.getAssignedFaculty());
					}
					// userList.add(icaDB.getAssignedFaculty());
					// String defaultMsg = "\\r\\n\\r\\nNote: This ICA query is approved for reeval
					// by : ?? \\r\\nTo view any attached files to this mail kindly login to
					// \\r\\nUrl: https://portal.svkm.ac.in/usermgmt/login ";

					String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName()
							+ " <br>"
							// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
							+ "<b>Note: </b>This ICA query approved by : ?? <br>"
							+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
							+ "This is auto-generated email, do not reply on this.</body></html>";
					for (String s : userList) {
						if (!userList.isEmpty()) {
							List<String> userList1 = new ArrayList<String>();
							userList1.add(s);
							// String notificationEmailMessage =
							// Jsoup.parse(teeDB.getTeeDesc().concat(defaultMsg)).text();

							// notificationEmailMessage = notificationEmailMessage.toString().replace("??",
							// " \\r\\nName :"+ u.getFirstname() + " " + u.getLastname() + "\\r\\nEmail-Id:
							// " + u.getEmail());
							notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
									+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
							result = userService.findEmailByUserName(userList1);
							Map<String, String> email = result.get("emails");

							Map<String, String> mobiles = new HashMap();
							logger.info("email -----> " + email);
							logger.info("mobile -----> " + mobiles);
							logger.info("subject -----> " + subject);
							logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
							//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
						}
					}
				}

				setSuccess(redirectAttributes, "ICA Approved Successfully");
			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
			setError(redirectAttributes, "Error in Approving ICA");
		}

		return "redirect:/showIcaQueries";
	}

	private String uploadIcaApprovalFile(MultipartFile file) {

		String errorMessage = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		String filePath = null;
		File folderPath = null;

		// CommonsMultipartFile file = bean.getFileData();
		String fileName = file.getOriginalFilename();

		// Replace special characters in file
		fileName = fileName.replaceAll("'", "_");
		fileName = fileName.replaceAll(",", "_");
		fileName = fileName.replaceAll("&", "and");
		fileName = fileName.replaceAll(" ", "_");

		fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."), fileName.length());

		try {
			inputStream = file.getInputStream();

			filePath = baseDir + File.separator + "ICAUploads" + File.separator + fileName;
			folderPath = new File(baseDir + File.separator + "ICAUploads");

			// bean.setFilePath(filePath);
			// bean.setFilePreviewPath(filePath);

			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			File newFile = new File(filePath);
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);

		} catch (IOException e) {
			filePath = "Error";
			logger.error("Exception while uploading" + errorMessage, e);
		} finally {

			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);

			if (outputStream != null)
				IOUtils.closeQuietly(outputStream);

		}

		return filePath;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaFile", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadIcaFile(
			@RequestParam(required = false, name = "id", defaultValue = "") String id, HttpServletRequest request,
			HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		String projectUrl = "";

		IcaQueries icaQ = icaQueriesService.findByID(Long.valueOf(id));
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			String filePath = icaQ.getFilePath();
			// 28-04-2020 Start
			String folderPathStr = "data/ICAUploads";
			// 28-04-2020 End
			if (filePath.contains(",")) {

				File folderPath = new File(baseDir + File.separator + "approvalFiles");
				List<String> files = Arrays.asList(filePath.split(","));
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}

				for (String file : files) {
					File fileNew = new File(file);
					// files.add(file);
					// 28-04-2020 Start
					InputStream inpStream = amazonS3ClientService.getFileForDownloadS3Object(fileNew.getName(),
							folderPathStr);
					File dest = new File(folderPath.getAbsolutePath() + "/" + fileNew.getName());
					logger.info("dest--->" + dest.getPath());
					FileUtils.copyInputStreamToFile(inpStream, dest);

//					File dest = new File(folderPath.getAbsolutePath()
//							+ File.separator + fileNew.getName());
//					FileUtils.copyFile(fileNew, dest);
					// 28-04-2020 End

				}
				String filename = "approvalFiles.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
				projectUrl = "/" + "data" + "/" + folderPath.getName() + ".zip";
				pack(folderPath.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				if (StringUtils.isEmpty(filePath)) {
					request.setAttribute("error", "true");
					request.setAttribute("errorMessage", "Error in downloading file.");
				}
				// 28-04-2020 Start
				if (filePath.startsWith("/")) {
					filePath = filePath.replaceFirst("/", "");
				}
				File downloadFile = new File(filePath);
				byte[] data = amazonS3ClientService.getFile(filePath);
				ByteArrayResource resource = new ByteArrayResource(data);
				return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
						.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
						.body(resource);
				// get absolute path of the application
//				ServletContext context = request.getSession()
//						.getServletContext();
//				File downloadFile = new File(filePath);
//				inputStream = new FileInputStream(downloadFile);
//
//				// get MIME type of the file
//				String mimeType = context.getMimeType(filePath);
//				if (mimeType == null) {
//					// set to binary type if MIME mapping not found
//					mimeType = "application/octet-stream";
//				}
//
//				// set content attributes for the response
//				/* response.setContentType(mimeType); */
//				response.setContentLength((int) downloadFile.length());
//
//				// set headers for the response
//				String headerKey = "Content-Disposition";
//				String headerValue = String.format(
//						"attachment; filename=\"%s\"", downloadFile.getName());
//				response.setHeader(headerKey, headerValue);
//
//				// get output stream of the response
//				outStream = response.getOutputStream();
//				IOUtils.copy(inputStream, outStream);
				// 28-04-2020 End
			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
			request.setAttribute("error", "true");
			request.setAttribute("errorMessage", "Error in downloading file.");
		}

		return null;
	}

	public static void pack(String sourceDirPath, ServletOutputStream out) throws IOException {

		try (ZipOutputStream zs = new ZipOutputStream(new BufferedOutputStream(out))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
				ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
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

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaMarksUploadTemplate", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadIcaMarksUploadTemplate(

			@RequestParam(name = "icaId") String icaId, RedirectAttributes redirectAttrs, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			String folderPath = baseDirS3 + "/" + app + "/" + "icaUploadMarkTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			String filePath = baseDirS3 + "/" + app + "/" + "icaUploadMarkTemp" + "/" + "uploadMarksTemp" + icaId
					+ ".xls";
			logger.info("filePath " + filePath);
			// String filePath =
			// 28-04-2020 Start
			File downloadFile = new File(filePath);
			byte[] data = amazonS3ClientService.getFile(filePath);
			ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
					.body(resource);
//			ServletContext context = request.getSession().getServletContext();
//			File downloadFile = new File(filePath);
//
//			if (!downloadFile.exists()) {
//
//				redirectAttrs.addAttribute("icaId", icaId);
//				setError(redirectAttrs, "Error in download template");
//				return "redirect:/evaluateIca";
//			}
//			inputStream = new FileInputStream(downloadFile);
//
//			// get MIME type of the file
//			String mimeType = context.getMimeType(filePath);
//			if (mimeType == null) {
//				// set to binary type if MIME mapping not found
//				mimeType = "application/octet-stream";
//			}
//			logger.info("MIME type: " + mimeType);
//
//			// set content attributes for the response
//			/* response.setContentType(mimeType); */
//			response.setContentLength((int) downloadFile.length());
//
//			// set headers for the response
//			String headerKey = "Content-Disposition";
//			String headerValue = String.format("attachment; filename=\"%s\"",
//					downloadFile.getName());
//			response.setHeader(headerKey, headerValue);
//
//			// get output stream of the response
//			outStream = response.getOutputStream();
//
//			IOUtils.copy(inputStream, outStream);
			// 28-04-2020 End
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

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/uploadStudentMarksExcel", method = { RequestMethod.POST })
	public String uploadStudentMarksExcel(@ModelAttribute Test test, @RequestParam("file") MultipartFile file,
			@RequestParam String saveAs, @RequestParam String icaId, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {

		redirectAttributes.addAttribute("icaId", icaId);

		IcaBean icaBeanDB = icaBeanService.findByID(Long.valueOf(icaId));

		try {
			String username = principal.getName();

			List<String> icaCompStudList = new ArrayList<>();
			List<String> icaTotalStudList = new ArrayList<>();
			boolean isAllSubmitted = true;
			List<String> headers = new ArrayList<String>(Arrays.asList("Roll No", "Name", "SAPID", "Is Absent?"));
			// int count=3;
			Map<String, IcaBean> icaBeanMap = new HashMap<>();

			if (icaBeanDB.getParentIcaId() != null) {
				icaBeanMap = getIcaComponents(Long.valueOf(icaBeanDB.getParentIcaId()));
			} else {
				icaBeanMap = getIcaComponents(Long.valueOf(icaId));
			}

			for (String ic : icaBeanMap.keySet()) {
				headers.add(ic + "(" + icaBeanMap.get(ic).getIcaCompMarks() + ")");
				// count++;
			}
			headers.add("REMARKS");
			logger.info("headers are--->" + headers);
			ExcelReader excelReader = new ExcelReader();

			List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);

			logger.info("maps received" + maps);

			List<String> excelUserList = new ArrayList<>();

			List<Map<String, Object>> copy = new ArrayList<>();
			if (icaBeanDB.getEventId() == null && !("Y").equals(icaBeanDB.getIsIcaDivisionWise())
					&& !("Y").equals(icaBeanDB.getIsNonEventModule())) {

				List<String> ucListForModuleBatch = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaIdAndFaculty(icaBeanDB.getId(), username);

				List<String> ucListForModule = userCourseService.findDistinctStudentByModuleIdAndAcadYear(
						icaBeanDB.getModuleId(), icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(),
						icaBeanDB.getProgramId(), icaBeanDB.getId(), icaBeanDB.getCampusId());
				if (ucListForModuleBatch.size() > 0) {
					ucListForModule.clear();
					ucListForModule.addAll(ucListForModuleBatch);
				}
				logger.info("ucListSize-->" + ucListForModule.size());

				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					if (ucListForModule.contains(user)) {
						return true;
					} else {
						return false;
					}
				}).collect(Collectors.toList());

				if (ucListForModule.size() > copy.size() || maps.size() > ucListForModule.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");

					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}

				logger.info("moduleWise");
			} else if (icaBeanDB.getEventId() == null && !("Y").equals(icaBeanDB.getIsIcaDivisionWise())
					&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {

				List<String> ucListForModule = userCourseService
						.findDistinctStudentByModuleIdAndAcadYearForNonEventModule(icaBeanDB.getModuleId(),
								icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(), icaBeanDB.getProgramId(),
								icaBeanDB.getId(), icaBeanDB.getCampusId());

				logger.info("ucListSize-->" + ucListForModule.size());

				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					if (ucListForModule.contains(user)) {
						if (!excelUserList.contains(user)) {
							excelUserList.add(user);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}).collect(Collectors.toList());

				if (ucListForModule.size() > copy.size() || maps.size() > ucListForModule.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}

				logger.info("moduleWise");
			} else if (icaBeanDB.getEventId() != null) {
				List<String> ucListForEvent = userCourseService.findDistinctStudentByEventIdAndAcadYear(
						icaBeanDB.getEventId(), icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(),
						icaBeanDB.getProgramId(), icaBeanDB.getId(), icaBeanDB.getCampusId());
				logger.info("ucListSize-->" + ucListForEvent.size());
				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					UserCourse uc = userCourseService.getMappingByUsernameAndCourse(user, icaBeanDB.getEventId());
					if (uc == null) {
						return false;
					} else {
						return true;
					}
				}).collect(Collectors.toList());

				if (ucListForEvent.size() > copy.size() || maps.size() > ucListForEvent.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}
				logger.info("courseWise");

			}

			logger.info("maps received after deletion" + copy);

			List<IcaComponentMarks> icaCompMarksList = new ArrayList<>();

			List<IcaTotalMarks> icaTotalMarksList = new ArrayList<>();
			for (Map<String, Object> mapper : copy) {
				if (mapper.get("Error") != null) {
					setError(redirectAttributes, (String) mapper.get("Error"));
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}

				double total = 0;
				int componentCount = 0;
				String isAbsent = mapper.get("Is Absent?").toString();
				if ("Y".equals(isAbsent)) {
					for (String ic : icaBeanMap.keySet()) {
						IcaComponentMarks icaCompMarks = new IcaComponentMarks();
						icaCompMarks.setUsername((String) mapper.get("SAPID"));
						icaCompMarks.setIcaId(icaId);
						icaCompMarks.setComponentId(icaBeanMap.get(ic).getComponentId());
						icaCompMarks.setMarks("0");
						if ("draft".equals(saveAs)) {
							icaCompMarks.setSaveAsDraft("Y");
						} else {
							icaCompMarks.setFinalSubmit("Y");
						}
						icaCompMarks.setActive("Y");
						icaCompMarks.setCreatedBy(principal.getName());
						icaCompMarks.setLastModifiedBy(principal.getName());
						if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
							icaCompStudList.add(icaCompMarks.getUsername());
						}
						// total = total + componentMarks;
						icaCompMarksList.add(icaCompMarks);
					}

					IcaTotalMarks itm = new IcaTotalMarks();

					itm.setIcaId(icaId);
					itm.setUsername((String) mapper.get("SAPID"));
					itm.setIcaTotalMarks("0");
					itm.setIsAbsent("Y");

					if (icaBeanDB.getScaledMarks() != null) {

						// round(multiplyVal/Integer.parseInt(icaBeanDB.getInternalMarks()),2);

						itm.setIcaScaledMarks("0");
					}
					if ("draft".equals(saveAs)) {
						itm.setSaveAsDraft("Y");
					} else {
						itm.setFinalSubmit("Y");
					}
					itm.setRemarks((String) mapper.get("REMARKS"));
					itm.setActive("Y");
					itm.setCreatedBy(principal.getName());
					itm.setLastModifiedBy(principal.getName());
					if (!icaTotalStudList.contains(itm.getUsername())) {
						icaTotalStudList.add(itm.getUsername());
					}
					icaTotalMarksList.add(itm);

				} else {
					for (String ic : icaBeanMap.keySet()) {

						IcaComponentMarks icaCompMarks = new IcaComponentMarks();
						icaCompMarks.setUsername((String) mapper.get("SAPID"));

						String compMark = mapper.get(ic + "(" + icaBeanMap.get(ic).getIcaCompMarks() + ")").toString();
						if (!compMark.trim().isEmpty()) {
							componentCount++;
							if (!excelReader.ISVALIDINPUT(compMark)) {
								setError(redirectAttributes, "Input Mark is not valid for student:"
										+ (String) mapper.get("SAPID") + "-" + compMark);
								if (null != icaBeanDB.getIsNonEventModule()
										&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {
									return "redirect:/evaluateIcaForNonEventModule";
								} else {
									return "redirect:/evaluateIca";
								}
							} else {
								double componentMarks = Double.parseDouble(
										(String) mapper.get(ic + "(" + icaBeanMap.get(ic).getIcaCompMarks() + ")"));
								if (Double.parseDouble(icaBeanMap.get(ic).getIcaCompMarks()) < componentMarks) {
									setError(redirectAttributes, "Invalid Component" + ic + ": Marks For Student"
											+ (String) mapper.get("SAPID"));
									if (null != icaBeanDB.getIsNonEventModule()
											&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {
										return "redirect:/evaluateIcaForNonEventModule";
									} else {
										return "redirect:/evaluateIca";
									}
								} else {
									icaCompMarks.setIcaId(icaId);
									icaCompMarks.setComponentId(icaBeanMap.get(ic).getComponentId());
									icaCompMarks.setMarks(String.valueOf(componentMarks));
									if ("draft".equals(saveAs)) {
										icaCompMarks.setSaveAsDraft("Y");
									} else {
										icaCompMarks.setFinalSubmit("Y");
									}
									icaCompMarks.setActive("Y");
									icaCompMarks.setCreatedBy(principal.getName());
									icaCompMarks.setLastModifiedBy(principal.getName());
									total = total + componentMarks;
									if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
										icaCompStudList.add(icaCompMarks.getUsername());
									}
									icaCompMarksList.add(icaCompMarks);
								}
							}
						} else {
							if ("draft".equals(saveAs)) {
								continue;
							} else {
								setError(redirectAttributes,
										"Error! Please fill in all component marks for all students"
												+ (String) mapper.get("SAPID"));
								if (null != icaBeanDB.getIsNonEventModule()
										&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {
									return "redirect:/evaluateIcaForNonEventModule";
								} else {
									return "redirect:/evaluateIca";
								}
							}
						}

					}

					if (componentCount != icaBeanMap.size() && componentCount != 0) {
						setError(redirectAttributes,
								"Error! Please fill in all component marks for student" + (String) mapper.get("SAPID"));
						if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
							return "redirect:/evaluateIcaForNonEventModule";
						} else {
							return "redirect:/evaluateIca";
						}
					} else {
						if (total != 0) {
							IcaTotalMarks itm = new IcaTotalMarks();

							itm.setIcaId(icaId);
							itm.setUsername((String) mapper.get("SAPID"));
							itm.setIcaTotalMarks(String.valueOf(round(total, 2)));
							itm.setIsAbsent("N");
							if (icaBeanDB.getScaledMarks() != null) {

								double multiplyVal = Double.parseDouble(icaBeanDB.getScaledMarks()) * total;
								// double scaledValue =
								// round(multiplyVal/Integer.parseInt(icaBeanDB.getInternalMarks()),2);
								double scaledValue = 0.0;
								scaledValue = multiplyVal / Double.parseDouble(icaBeanDB.getInternalMarks());

								itm.setIcaScaledMarks(String.valueOf(round(scaledValue, 2)));
							}
							if ("draft".equals(saveAs)) {
								itm.setSaveAsDraft("Y");
							} else {
								itm.setFinalSubmit("Y");
							}
							itm.setRemarks((String) mapper.get("REMARKS"));
							itm.setActive("Y");
							itm.setCreatedBy(principal.getName());
							itm.setLastModifiedBy(principal.getName());
							if (!icaTotalStudList.contains(itm.getUsername())) {
								icaTotalStudList.add(itm.getUsername());
							}
							icaTotalMarksList.add(itm);
						} else {
							IcaTotalMarks itm = new IcaTotalMarks();

							itm.setIcaId(icaId);
							itm.setUsername((String) mapper.get("SAPID"));
							itm.setIcaTotalMarks("0");
							itm.setIsAbsent("N");
							if (icaBeanDB.getScaledMarks() != null) {

								itm.setIcaScaledMarks("0");
							}
							if ("draft".equals(saveAs)) {
								itm.setSaveAsDraft("Y");
							} else {
								itm.setFinalSubmit("Y");
							}
							itm.setRemarks((String) mapper.get("REMARKS"));
							itm.setActive("Y");
							itm.setCreatedBy(principal.getName());
							itm.setLastModifiedBy(principal.getName());
							if (!icaTotalStudList.contains(itm.getUsername())) {
								icaTotalStudList.add(itm.getUsername());
							}
							icaTotalMarksList.add(itm);
						}

					}
				}

			}

			icaComponentMarksService.upsertBatch(icaCompMarksList);
			icaTotalMarksService.upsertBatch(icaTotalMarksList);
			if (!icaId.isEmpty() && icaId != null) {
				List<String> studList = icaStudentBatchwiseService.getDistinctUsernamesByActiveIcaId(icaId);
				List<String> remainingStudList = icaStudentBatchwiseService
						.getDistinctActiveUsernamesOfRemainingFaculty(Long.valueOf(icaId), username);
				if (studList.size() > 0) {
					List<String> compUsersCopy = new ArrayList();
					List<String> compUsers = icaComponentMarksService.getDistinctUsernamesByActiveIcaId(icaId);
					compUsers.addAll(icaCompStudList);
					compUsersCopy = compUsers.stream().distinct().collect(Collectors.toList());
					if ("draft".equals(saveAs)) {
						if (studList.size() != compUsersCopy.size()) {
							isAllSubmitted = false;
							icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", icaId, "N");
						} else {

							isAllSubmitted = false;
							icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", icaId, "Y");
						}
					} else if ("submit".equals(saveAs)) {
						if (studList.size() != compUsersCopy.size()) {
							isAllSubmitted = false;
							/*
							 * icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT", icaId,
							 * "N");
							 */
							icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList, icaId);
						} else {
							List<String> finalSubmitCompoMarksList = icaComponentMarksService
									.getFinalSubmitByIcaIdAndUserList(icaId, remainingStudList);
							if (finalSubmitCompoMarksList.size() == 1) {
								if (("Y").equalsIgnoreCase(finalSubmitCompoMarksList.get(0))) {
									isAllSubmitted = true;
									icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT", icaId, "Y");
								} else {
									isAllSubmitted = false;
									icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList, icaId);
								}
							} else {
								isAllSubmitted = false;
								icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList, icaId);
							}

						}
					}
					List<String> totalMarksUsersCopy = new ArrayList();
					List<String> totalMarksUsers = icaTotalMarksService.getDistinctUsernamesByActiveIcaId(icaId);
					totalMarksUsers.addAll(icaTotalStudList);
					totalMarksUsersCopy = totalMarksUsers.stream().distinct().collect(Collectors.toList());
					if ("draft".equals(saveAs)) {
						if (studList.size() != totalMarksUsersCopy.size()) {
							isAllSubmitted = false;
							icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", icaId, "N");
						} else {
							isAllSubmitted = false;
							icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", icaId, "Y");
						}
					} else if ("submit".equals(saveAs)) {
						if (studList.size() != totalMarksUsersCopy.size()) {
							isAllSubmitted = false;
							/* icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT", icaId, "N"); */
							icaTotalMarksService.updateFinalSubmitByUserList(icaTotalStudList, icaId);
						} else {
							List<String> finalSubmitTotalMarksList = icaTotalMarksService
									.getFinalSubmitByIcaIdAndUserList(icaId, remainingStudList);
							if (finalSubmitTotalMarksList.size() == 1) {
								if (("Y").equalsIgnoreCase(finalSubmitTotalMarksList.get(0))) {
									isAllSubmitted = true;
									icaTotalMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT", icaId, "Y");
								} else {
									isAllSubmitted = false;
									icaTotalMarksService.updateFinalSubmitByUserList(icaTotalStudList, icaId);
								}
							} else {
								isAllSubmitted = false;
								icaTotalMarksService.updateFinalSubmitByUserList(icaTotalStudList, icaId);
							}
						}
					}
				}
			}

			if ("submit".equals(saveAs)) {
				if (isAllSubmitted) {
					icaBeanService.updateIcaToSubmitted(icaId, Utils.getInIST());
				}
			}
			setSuccess(redirectAttributes, "File saved successfully");

			// sdf
			if ("submit".equals(saveAs)) {
				User u = userService.findByUserName(username);
				String subject = " ICA Marks Submitted : " + icaBeanDB.getIcaName();
				Map<String, Map<String, String>> result = null;
				List<String> userList = new ArrayList<String>();
				// List<String> acadSessionList = new ArrayList<>();
				userList.add(icaBeanDB.getCreatedBy());
				String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaBeanDB.getIcaName()
						+ " <br>"
						// + "<b>ICA Description: </b>"+ icaBeanDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This ICA marks is submitted by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				if (!userList.isEmpty()) {
					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}
			if ("submit".equals(saveAs)) {
				if (isAllSubmitted) {
					return "redirect:/showEvaluatedInternalMarks";
				} else {
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}
			} else {
				if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
					return "redirect:/evaluateIcaForNonEventModule";
				} else {
					return "redirect:/evaluateIca";
				}
			}

		} catch (Exception ex) {

			setError(redirectAttributes, "Error in uploading marks");

			logger.error("Exception", ex);
		}
		if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
			return "redirect:/evaluateIcaForNonEventModule";
		} else {
			return "redirect:/evaluateIca";
		}

	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaReportForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadIcaReportForm(Model m, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<Program> programList = programService.findAllActive();
		m.addAttribute("programList", programList);

		List<String> acadYearCodeList = courseService.findAcadYearCode();
		logger.info("acadYearCodeList Ica Report Form------------>" + acadYearCodeList);

		m.addAttribute("acadYearCodeList", acadYearCodeList);

		return "ica/icaReport";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadIcaReport(Model m, HttpServletResponse response, Principal principal,
			@RequestParam String acadYear, @RequestParam String acadSession, @RequestParam String programId,
			@RequestParam(required = false) String campusId, @RequestParam String reportType,
			RedirectAttributes redirectAttribute) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<IcaTotalMarks> datasize = icaTotalMarksService.getIcaTotalMarksByParam(acadYear, acadSession, programId,
				campusId);

		List<IcaTotalMarks> datasizelisttwo = icaTotalMarksService.getIcaTotalMarksByParamForNonEvent(acadYear,
				acadSession, programId, campusId);
		datasize.addAll(datasizelisttwo);

		InputStream is = null;
		String filePath = "";
		try {

			if ("subjWise".equals(reportType)
					|| "subjWiseComp".equals(reportType) /* || "subjWiseCompSaveAsDraft".equals(reportType) */) {
				ServletOutputStream out = response.getOutputStream();
				if (programId.contains(",")) {

					List<String> programs = Arrays.asList(programId.split(","));
					File folderPath = new File(downloadAllFolder + File.separator + "compiledReport");

					for (String pid : programs) {
						String multiProgFilePath = "";
						if ("subjWise".equals(reportType)) {

							if (datasize.size() == 0) {
								setNote(redirectAttribute, "No Data Found");
								return "redirect:/downloadIcaReportForm";

							} else {
								multiProgFilePath = getFilePathOfSubjectWiseReport(pid, acadYear, acadSession, campusId,
										userdetails1.getCollegeName());
							}

						}
						/*
						 * else if("subjWiseCompSaveAsDraft".equals(reportType)){ multiProgFilePath =
						 * getFilePathOfSubjectAndComponentWiseReportDraft( pid, acadYear, acadSession,
						 * campusId, userdetails1.getCollegeName()); }
						 */
						else {

							if (datasize.size() == 0) {
								setNote(redirectAttribute, "No Data Found");
								return "redirect:/downloadIcaReportForm";
							} else {
								multiProgFilePath = getFilePathOfSubjectAndComponentWiseReport(pid, acadYear,
										acadSession, campusId, userdetails1.getCollegeName(), programId);
							}
						}
						File fileP = new File(multiProgFilePath);

						if (!folderPath.exists()) {
							folderPath.mkdir();
						}
						String newFilePath = folderPath.getAbsolutePath() + File.separator + fileP.getName();

						String filename = "Student review ICA Marks MID Term-End Term" + pid + ".xlsx";
						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-Disposition", "attachment; filename=" + filename);

						is = new FileInputStream(multiProgFilePath);

						FileUtils.copyFile(fileP, new File(newFilePath));

					}
					String zipFile = "Consolidated-ICA-Report.zip";
					response.setContentType("Content-type: text/zip");
					response.setHeader("Content-Disposition", "attachment; filename=" + zipFile + "");
					pack(folderPath.getAbsolutePath(), out);
					FileUtils.deleteDirectory(folderPath);
				} else {
					if ("subjWise".equals(reportType)) {

						if (datasize.size() == 0) {
							setNote(redirectAttribute, "No Data Found");
							return "redirect:/downloadIcaReportForm";
						} else {

							filePath = getFilePathOfSubjectWiseReport(programId, acadYear, acadSession, campusId,
									userdetails1.getCollegeName());
						}

					} /*
						 * else if("subjWiseCompSaveAsDraft".equals(reportType)){ filePath =
						 * getFilePathOfSubjectAndComponentWiseReportDraft( programId, acadYear,
						 * acadSession, campusId, userdetails1.getCollegeName());
						 * 
						 * }
						 */else {

						if (datasize.size() == 0) {
							setNote(redirectAttribute, "No Data Found");
							return "redirect:/downloadIcaReportForm";
						} else {

							filePath = getFilePathOfSubjectAndComponentWiseReport(programId, acadYear, acadSession,
									campusId, userdetails1.getCollegeName(), programId);
						}
					}
					// is = new FileInputStream(filePath);
					String filename = "Student review ICA Marks MID Term-End Term.xlsx";
					response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
					response.setHeader("Content-Disposition", "attachment; filename=" + filename);
					// copy it to response's OutputStream
					is = new FileInputStream(filePath);

					org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
					response.flushBuffer();
					response.getOutputStream().flush();
					response.getOutputStream().close();

				}
			} else {
				filePath = getFilePathOfReport(programId, acadYear, acadSession, campusId,
						userdetails1.getCollegeName());
				// is = new FileInputStream(filePath);
				String filename = "Student review ICA Marks MID Term-End Term.xlsx";
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("Content-Disposition", "attachment; filename=" + filename);
				// copy it to response's OutputStream
				is = new FileInputStream(filePath);
				org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
				File fileP = new File(filePath);
				response.flushBuffer();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				FileUtils.deleteQuietly(fileP);
			}

			/*
			 * response.setContentType(
			 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" );
			 * 
			 * response.setHeader("Content-Disposition", "attachment; filename=" + filename
			 * + "");
			 * 
			 * org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			 * response.flushBuffer();
			 * 
			 * response.flushBuffer();
			 */
		} catch (Exception ex) {
			logger.info("Error writing file to output stream. Filename was '{}'", ex);
			throw new RuntimeException("IOError writing file to output stream" + ex);
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			// FileUtils.deleteQuietly(new File(filePath));
		}

		return "ica/icaReport";
	}

	/*
	 * @RequestMapping(value = "/downloadIcaReport", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String downloadIcaReport(Model m,
	 * HttpServletResponse response, Principal principal, @RequestParam String
	 * acadYear,
	 * 
	 * @RequestParam String acadSession, @RequestParam String programId,
	 * 
	 * @RequestParam(required = false) String campusId) {
	 * 
	 * String username = principal.getName(); Token userdetails1 = (Token)
	 * principal; InputStream is = null; String filePath = ""; try { filePath =
	 * getFilePathOfReport(programId, acadYear, acadSession, campusId,
	 * userdetails1.getCollegeName()); // is = new FileInputStream(filePath); String
	 * filename = "Student review ICA Marks MID Term-End Term.xlsx";
	 * response.setContentType(
	 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	 * response.setHeader("Content-Disposition", "attachment; filename=" +
	 * filename); // copy it to response's OutputStream is = new
	 * FileInputStream(filePath); org.apache.commons.io.IOUtils.copy(is,
	 * response.getOutputStream()); response.flushBuffer();
	 * response.getOutputStream().flush(); response.getOutputStream().close();
	 * 
	 * 
	 * } catch (Exception ex) { logger.info(
	 * "Error writing file to output stream. Filename was '{}'", ex); throw new
	 * RuntimeException("IOError writing file to output stream" + ex); } finally {
	 * if (is != null) { org.apache.commons.io.IOUtils.closeQuietly(is); }
	 * 
	 * 
	 * }
	 * 
	 * return "ica/icaReport"; }
	 */

	public String getFilePathOfReport(String programId, String acadYear, String acadSession, String campusId,
			String collegeName) {
		List<String> headers = new ArrayList();
		String h[] = { "SAPID", "Name", "Roll.No", "Modules", "Obtained Marks", "Total ICA Marks", "Status",
				"Remarks" };
		headers = Arrays.asList(h);

		String fileName = "ICA-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "ICA-Report"
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFCellStyle totalStyle = workbook.createCellStyle();

		XSSFCellStyle totalRedStyle = workbook.createCellStyle();

		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(true);
		defaultFont.setItalic(false);
		totalStyle.setFont(defaultFont);
		totalRedStyle.setFont(defaultFont);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		// style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFont(defaultFont);
		headerStyle.setWrapText(true);

		XSSFColor grayColor = new XSSFColor(new Color(211, 211, 211));
		totalStyle.setFillBackgroundColor(grayColor);

		XSSFColor redColor = new XSSFColor(new Color(255, 0, 0));
		totalRedStyle.setFillBackgroundColor(redColor);

		XSSFCellStyle borderStyle = workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 0));
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalStyle.setFillPattern(FillPatternType.LESS_DOTS);

		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalRedStyle.setFillPattern(FillPatternType.LESS_DOTS);

		style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		/*
		 * if (programId.contains(",")) {
		 * 
		 * List<String> programIdList = Arrays.asList(programId.split(",")); int
		 * count=0; for (String p : programIdList) { count++; String programName =
		 * programService.findByID( Long.valueOf(p)).getProgramName(); XSSFSheet sheet =
		 * workbook.createSheet(programName+"-"+count);
		 * logger.info("count--->"+workbook.getSheetName(0));
		 * logger.info("count--->"+workbook.getSheetName(1));; } }
		 */

		if (programId.contains(",")) {

			List<String> programIdList = Arrays.asList(programId.split(","));

			for (String p : programIdList) {

				String programName = programService.findByID(Long.valueOf(p)).getProgramName();
				int rowNum = 0;
				logger.info("program name" + programName);
				XSSFSheet sheet = workbook.createSheet(p);
				// XSSFSheet sheet = workbook.getSheet(programName);

				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

				sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

				sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));

				Row collegeNameRow = sheet.createRow(rowNum++);
				Row programRow = sheet.createRow(rowNum++);
				Row reviewRow = sheet.createRow(rowNum++);

				Cell collegeCell = collegeNameRow.createCell(0);
				if (campusId == null) {
					if (collegeName != null) {
						collegeCell.setCellValue(collegeName);
					} else {
						collegeCell.setCellValue("NMIMS/SVKM");
					}
				} else {
					collegeName = programService.getCollegeName(programId, campusId);
					if (collegeName != null) {
						collegeCell.setCellValue(collegeName);
					} else {
						collegeCell.setCellValue("NMIMS/SVKM");
					}
				}
				// String year = acadYear;
				String batch = acadYear;

				Cell programCell = programRow.createCell(0);
				programCell.setCellValue(programName + "-" + acadSession + "-" + batch);

				Cell reviewCell = reviewRow.createCell(0);
				/*
				 * LocalDateTime now = LocalDateTime.now();
				 * 
				 * DateTimeFormatter formatter = DateTimeFormatter
				 * .ofPattern("yyyy-MM-dd HH:mm:ss");
				 */

				// String formatDateTime = now.format(formatter);
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

				formatter = new SimpleDateFormat("dd MMMM yyyy");
				String strDate = formatter.format(Utils.getInIST());
				/* reviewCell.setCellValue("Mid Term Review report "); */
				reviewCell.setCellValue("Student review ICA Marks");

				collegeCell.setCellStyle(centerStyle);
				reviewCell.setCellStyle(centerStyle);
				programCell.setCellStyle(centerStyle);

				Row dateRow = sheet.createRow(rowNum++);

				dateRow.createCell(7).setCellValue(strDate);
				CellStyle border = workbook.createCellStyle();
				/*
				 * border.setBorderBottom(BorderStyle.MEDIUM);
				 * border.setBorderLeft(BorderStyle.MEDIUM);
				 * border.setBorderRight(BorderStyle.MEDIUM);
				 * border.setBorderTop(BorderStyle.MEDIUM);
				 */
				// dateRow.getCell(8).setCellStyle(border);
				Row emptyRow = sheet.createRow(rowNum++);

				Row headerRow = sheet.createRow(rowNum++);

				Row row = sheet.createRow(rowNum++);
				row.setRowStyle(borderStyle);
				for (int colNum = 0; colNum < headers.size(); colNum++) {

					Cell cell = headerRow.createCell(colNum);
					cell.setCellValue(headers.get(colNum));
					cell.setCellStyle(headerStyle);

				}

				List<IcaTotalMarks> getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParam(acadYear,
						acadSession, p, campusId);

				List<IcaTotalMarks> getIcaTotalMarksByParamNonEvent = icaTotalMarksService
						.getIcaTotalMarksByParamForNonEvent(acadYear, acadSession, programId, campusId);

				getIcaTotalMarksByParam.addAll(getIcaTotalMarksByParamNonEvent);

				HashSet<String> sapIds = new LinkedHashSet<>();
				Map<String, String> mapSapRoll = new HashMap<>();
				Map<String, String> mapSapName = new HashMap<>();
				for (IcaTotalMarks itm : getIcaTotalMarksByParam) {
					sapIds.add(itm.getUsername());
					mapSapRoll.put(itm.getUsername(), itm.getRollNo());
					mapSapName.put(itm.getUsername(), itm.getStudentName());
				}

				Map<String, List<IcaTotalMarks>> componentsMapIca = new HashMap<>();

				for (String s : sapIds) {
					List<IcaTotalMarks> icaList = getIcaTotalMarksByParam.stream()
							.filter(o -> o.getUsername().equals(s)).collect(Collectors.toList());
					componentsMapIca.put(s, icaList);
				}

				List<String> numbersList = new ArrayList<String>(sapIds); // set
																			// ->
																			// list

				// Sort the list
				Collections.sort(numbersList);

				sapIds = new LinkedHashSet<>(numbersList);
				logger.info("sapIds" + sapIds);

				for (String s : sapIds) {

					Row rowd = sheet.createRow(++rowNum);
					int colNum = 0;

					CellStyle wrapstyle = workbook.createCellStyle();
					rowd.createCell(colNum++).setCellValue(s);

					rowd.createCell(colNum++).setCellValue(mapSapName.get(s));
					rowd.createCell(colNum++).setCellValue(mapSapRoll.get(s));
					int count = 0;
					for (IcaTotalMarks itm : componentsMapIca.get(s)) {
						if (count == 0) {
							rowd.createCell(colNum++).setCellValue(itm.getModuleName());
							rowd.createCell(colNum++).setCellValue(itm.getIcaTotalMarks());
							rowd.createCell(colNum++).setCellValue(itm.getInternalMarks());

							rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
							rowd.createCell(colNum++).setCellValue(itm.getRemarks());

						} else {
							/*
							 * Row newRow = sheet.createRow(++rowNum); int colNum1 = 0;
							 * 
							 * CellStyle wrapstyle1 = workbook.createCellStyle();
							 * newRow.createCell(colNum1++).setCellValue("");
							 * 
							 * newRow.createCell(colNum1++).setCellValue( mapSapName.get(""));
							 * newRow.createCell(colNum1++).setCellValue( mapSapRoll.get(""));
							 * rowd.createCell(colNum1++).setCellValue( itm.getModuleName());
							 * rowd.createCell(colNum1++).setCellValue( itm.getIcaTotalMarks());
							 * rowd.createCell(colNum1++).setCellValue( itm.getInternalMarks());
							 * 
							 * rowd.createCell(colNum1++).setCellValue( itm.getPassFailStatus());
							 * rowd.createCell(colNum1++).setCellValue( itm.getRemarks());
							 */

							Row newRow = sheet.createRow(++rowNum);
							int colNum1 = 0;

							CellStyle wrapstyle1 = workbook.createCellStyle();
							newRow.createCell(colNum1++).setCellValue("");

							newRow.createCell(colNum1++).setCellValue(mapSapName.get(""));
							newRow.createCell(colNum1++).setCellValue(mapSapRoll.get(""));
							newRow.createCell(colNum1++).setCellValue(itm.getModuleName());
							newRow.createCell(colNum1++).setCellValue(itm.getIcaTotalMarks());
							newRow.createCell(colNum1++).setCellValue(itm.getInternalMarks());

							newRow.createCell(colNum1++).setCellValue(itm.getPassFailStatus());
							newRow.createCell(colNum1++).setCellValue(itm.getRemarks());

						}
						count++;
					}
					Row rowd1 = sheet.createRow(++rowNum);
				}
				sheet.setColumnWidth(0, 3500);
				sheet.autoSizeColumn(1);
				// log.info("Width Col ------- " + sheet.getColumnWidth(2));
				sheet.setColumnWidth(2, 5500);
				sheet.setColumnWidth(3, 6500);
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);
				sheet.autoSizeColumn(8);
			}

			try {
				FileOutputStream outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
				workbook.close();
			} catch (Exception e) {
				logger.error("Exception ", e);
			}
		} else {

			int rowNum = 0;
			XSSFSheet sheet = workbook.createSheet(programService.findByID(Long.valueOf(programId)).getProgramName());

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 8));

			Row collegeNameRow = sheet.createRow(rowNum++);
			Row programRow = sheet.createRow(rowNum++);
			Row reviewRow = sheet.createRow(rowNum++);

			Cell collegeCell = collegeNameRow.createCell(0);

			// Cell collegeCell = collegeNameRow.createCell(0);
			if (campusId == null) {
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			} else {
				String campusName = programService.getCollegeName(programId, campusId);
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName + "/" + campusName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			}

			// String year = acadYear;
			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(programService.findByID(Long.valueOf(programId)).getProgramName() + " "
					+ acadSession + " " + batch);

			Cell reviewCell = reviewRow.createCell(0);
			/*
			 * LocalDateTime now = LocalDateTime.now();
			 * 
			 * DateTimeFormatter formatter = DateTimeFormatter
			 * .ofPattern("yyyy-MM-dd HH:mm:ss");
			 */

			// String formatDateTime = now.format(formatter);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			/* reviewCell.setCellValue("Mid Term Review report "); */
			reviewCell.setCellValue("Student review ICA Marks");

			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(8).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			/*
			 * border.setBorderBottom(BorderStyle.MEDIUM);
			 * border.setBorderLeft(BorderStyle.MEDIUM);
			 * border.setBorderRight(BorderStyle.MEDIUM);
			 * border.setBorderTop(BorderStyle.MEDIUM);
			 */
			dateRow.getCell(8).setCellStyle(border);
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			List<IcaTotalMarks> getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParam(acadYear,
					acadSession, programId, campusId);

			List<IcaTotalMarks> getIcaTotalMarksByParamNonEvent = icaTotalMarksService
					.getIcaTotalMarksByParamForNonEvent(acadYear, acadSession, programId, campusId);

			getIcaTotalMarksByParam.addAll(getIcaTotalMarksByParamNonEvent);

			HashSet<String> sapIds = new LinkedHashSet<>();
			Map<String, String> mapSapRoll = new HashMap<>();
			Map<String, String> mapSapName = new HashMap<>();
			for (IcaTotalMarks itm : getIcaTotalMarksByParam) {
				sapIds.add(itm.getUsername());
				mapSapRoll.put(itm.getUsername(), itm.getRollNo());
				mapSapName.put(itm.getUsername(), itm.getStudentName());
			}
			Map<String, List<IcaTotalMarks>> componentsMapIca = new HashMap<>();

			for (String s : sapIds) {
				List<IcaTotalMarks> icaList = getIcaTotalMarksByParam.stream().filter(o -> o.getUsername().equals(s))
						.collect(Collectors.toList());
				componentsMapIca.put(s, icaList);
			}

			List<String> numbersList = new ArrayList<String>(sapIds); // set ->
																		// list

			// Sort the list
			Collections.sort(numbersList);

			sapIds = new LinkedHashSet<>(numbersList);
			logger.info("sapIds" + sapIds);

			for (String s : sapIds) {

				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(s);

				rowd.createCell(colNum++).setCellValue(mapSapName.get(s));
				rowd.createCell(colNum++).setCellValue(mapSapRoll.get(s));
				int count = 0;
				for (IcaTotalMarks itm : componentsMapIca.get(s)) {
					if (count == 0) {
						rowd.createCell(colNum++).setCellValue(itm.getModuleName());
						rowd.createCell(colNum++).setCellValue(itm.getIcaTotalMarks());
						rowd.createCell(colNum++).setCellValue(itm.getInternalMarks());

						rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
						rowd.createCell(colNum++).setCellValue(itm.getRemarks());

					} else {
						Row newRow = sheet.createRow(++rowNum);
						int colNum1 = 0;

						CellStyle wrapstyle1 = workbook.createCellStyle();
						newRow.createCell(colNum1++).setCellValue("");

						newRow.createCell(colNum1++).setCellValue(mapSapName.get(""));
						newRow.createCell(colNum1++).setCellValue(mapSapRoll.get(""));
						newRow.createCell(colNum1++).setCellValue(itm.getModuleName());
						newRow.createCell(colNum1++).setCellValue(itm.getIcaTotalMarks());
						newRow.createCell(colNum1++).setCellValue(itm.getInternalMarks());

						newRow.createCell(colNum1++).setCellValue(itm.getPassFailStatus());
						newRow.createCell(colNum1++).setCellValue(itm.getRemarks());

					}
					count++;
				}

				Row rowd1 = sheet.createRow(++rowNum);

			}

			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
			// log.info("Width Col ------- " + sheet.getColumnWidth(2));
			sheet.setColumnWidth(2, 5500);
			sheet.setColumnWidth(3, 6500);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(8);

			try {
				FileOutputStream outputStream = new FileOutputStream(filePath);
				workbook.write(outputStream);
				workbook.close();
			} catch (Exception e) {
				logger.error("Exception ", e);
			}

		}

		return filePath;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/showEvaluatedInternalMarks", method = { RequestMethod.GET, RequestMethod.POST })
	public String showEvaluatedInternalMarks(Model m, Principal principal, @RequestParam String icaId) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		IcaBean icaBeanDB = icaBeanService.findByID(Long.valueOf(icaId));
		String tempIcaId= "";
		String seqFlag="";
		if("Y".equals(icaBeanDB.getIsPublishCompWise())) {
			seqFlag="I";
			tempIcaId = String.valueOf(icaBeanDB.getId());
			
		}else {
			 
			if("Y".equals(icaBeanDB.getIsIcaDivisionWise())) {
				seqFlag="P";
				tempIcaId = icaBeanDB.getParentIcaId();
			}else {
				seqFlag="I";
				tempIcaId = String.valueOf(icaBeanDB.getId());
			}
		}		
		
		List<IcaComponentMarks> componentMarksForIca = new ArrayList<>();
		if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
			
			componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaIdForNonEvent(icaId);
			
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				//			componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaId(tempIcaId);
				componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaIdWithSeqNo(tempIcaId,seqFlag,icaId);
				
				
			} else if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				
		
				List<IcaStudentBatchwise> studentListForCheck = icaStudentBatchwiseService.getAllByActiveIcaId(icaId);
				if (studentListForCheck.size() > 0) {
					
					//componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaIdBatchWise(tempIcaId,	username);
					componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaIdBatchWiseforSeq(tempIcaId,seqFlag,username,icaId);
				} else {
		//			componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaId(tempIcaId);
					componentMarksForIca = icaComponentMarksService.getIcaComponentMarksByIcaIdWithSeqNo(tempIcaId,seqFlag,icaId);
				}
				
				
				
				
				
			}

		}

		
		try {
			 List<IcaComponentMarks> sortedList = componentMarksForIca.stream().sorted(Comparator.comparing(IcaComponentMarks::getSequenceNo)).collect(Collectors.toList());
			 //logger.info("sortedList---->"+sortedList);
			 componentMarksForIca.clear();
			 componentMarksForIca.addAll(sortedList);
			} catch (Exception e) {
				logger.error("error in sorting...");
			}
		
		Map<String, String> componentsMap = new LinkedHashMap<>();
		for (IcaComponentMarks icm : componentMarksForIca) {
			if (!componentsMap.containsKey(icm.getComponentId())) {
				componentsMap.put(icm.getComponentId(), icm.getComponentName());
			}
		}

		List<IcaTotalMarks> icaTotalMarksForIca = new ArrayList<>();
		if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
			icaTotalMarksForIca = icaTotalMarksService.getIcaTotalMarksByIcaForNonEvent(icaId);
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				icaTotalMarksForIca = icaTotalMarksService.getIcaTotalMarksByIca(icaId);
			} else if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				List<IcaStudentBatchwise> studentListForCheck = icaStudentBatchwiseService.getAllByActiveIcaId(icaId);
				if (studentListForCheck.size() > 0) {
					icaTotalMarksForIca = icaTotalMarksService.getIcaTotalMarksByIcaBatchWise(icaId, username);
				} else {
					icaTotalMarksForIca = icaTotalMarksService.getIcaTotalMarksByIca(icaId);
				}
			}

		}

		List<String> usernameList = new ArrayList<>();

		usernameList = icaTotalMarksForIca.stream().map(map -> map.getUsername()).collect(Collectors.toList());
		Map<String, User> userMap = new HashMap<>();
		logger.info("debug1:" + icaBeanDB.getIsPublishCompWise() + " username size" + usernameList.size());
		if ("Y".equals(icaBeanDB.getIsPublishCompWise())) {
			if (usernameList.size() == 0) {
				usernameList = componentMarksForIca.stream().map(map -> map.getUsername()).collect(Collectors.toList());
				for (String s : usernameList) {
					User user = userService.findByUserName(s);
					userMap.put(s, user);
				}
			}
		}
		/*
		 * Map<String, String> componentsMap = componentMarksForStuent.stream()
		 * .collect( Collectors.toMap(IcaComponentMarks::getComponentId,
		 * IcaComponentMarks::getComponentName));
		 */
		Map<String, Map<String, String>> mapOfComponentsMarksByUsername = new HashMap<>();
		Map<String, String> dateSpanMap = new HashMap<>();
		for (String i : usernameList) {

			Map<String, String> componentsMapIca = componentMarksForIca.stream().filter(o -> o.getUsername().equals(i))
					.collect(Collectors.toMap(IcaComponentMarks::getComponentId, IcaComponentMarks::getMarks));

			/*
			 * Map<String, String> componentsMapIca = componentMarksForStuent.stream()
			 * .collect( Collectors.toMap(IcaComponentMarks::getComponentId,
			 * IcaComponentMarks::getComponentName));
			 */
			mapOfComponentsMarksByUsername.put(i, componentsMapIca);

		}

		String p = programService.getProgramNamesForIca(icaBeanDB.getProgramId());

		m.addAttribute("programName", p);
		m.addAttribute("mapComponent", mapOfComponentsMarksByUsername);
		m.addAttribute("totalMarks", icaTotalMarksForIca);
		m.addAttribute("componentsMap", componentsMap);
		m.addAttribute("user", userService.findByUserName(username));
		m.addAttribute("ica", icaBeanDB);
		m.addAttribute("uMap", userMap);
		logger.info("uMap" + userMap.size());

		if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
			m.addAttribute("moduleName", courseService.getModuleNameForNonEvent(icaBeanDB.getModuleId()));
		} else {
			m.addAttribute("moduleName", courseService.getModuleName(icaBeanDB.getModuleId()));
		}

		// m.addAttribute("publishDate",publishedDate);

		return "ica/showEvaluatedMarks";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/GetProgramsFromAcadSessionYearModule", method = { RequestMethod.GET })
	public @ResponseBody String GetProgramsFromAcadSessionYearModule(@RequestParam String acadSession,
			@RequestParam String moduleId, @RequestParam String year, Principal principal) {

		List<Program> programList = new ArrayList<Program>();

		programList = programService.findAllProgramsWithAcadSessionYearModule(acadSession, year, moduleId);

		JSONArray jsonarray = new JSONArray();
		try {
			for (Program p : programList) {
				JSONObject obj = new JSONObject();
				obj.put("value", p.getId().toString());
				obj.put("text", p.getProgramName());
				jsonarray.put(obj);
			}

			// logger.info(jsonarray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
		return jsonarray.toString();
	}

	@Secured({ "ROLE_ADMIN"})
	@RequestMapping(value = "/addIcaFormForDivision", method = { RequestMethod.GET, RequestMethod.POST })
	public String addIcaFormForDivision(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Add ICA For Divisions", true, false));

		IcaBean ica = new IcaBean();
		// ica.setProgramId(userdetails1.getProgramId());

		if (id != null) {

			ica = icaBeanService.findByID(Long.valueOf(id));

			// IcaTotalMarks icaMarks = icaTotalMarksService.ge
			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());

			if (currentDate.compareTo(ica.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot update, ICA Deadline is over");
				return "redirect:/searchIcaList";
			} else if ("Y".equals(ica.getIsSubmitted())) {
				setNote(redirectAttrs, "Cannot update, ICA is Already Evaluated");
				return "redirect:/searchIcaList";
			}

			else {
				if (currentDate.compareTo(ica.getStartDate()) > 0) {

					m.addAttribute("icaStarted", "true");
				} else {
					m.addAttribute("icaStarted", "false");
				}
			}

			m.addAttribute("edit", "true");
			m.addAttribute("moduleName", courseService.getModuleName(ica.getModuleId()));
			m.addAttribute("programList", programService.getProgramListByIds(ica.getProgramId()));
			m.addAttribute("facultyList",
					userCourseService.findAllFacultyWithModuleIdICA(ica.getModuleId(), ica.getAcadYear()));
		}
		List<String> acadYearCodeList = courseService.findAcadYearCode();
		logger.info("acadYearCodeList------------>" + acadYearCodeList);
		m.addAttribute("acadYearCodeList", acadYearCodeList);
		m.addAttribute("icaBean", ica);

		return "ica/createIcaForDivision";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addIcaForDivision", method = RequestMethod.POST)
	public String addIcaForDivision(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {

		try {
			/* New Audit changes start */
			logger.info("Validating /addIcaForDivision...");
			HtmlValidation.validateHtml(icaBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", icaBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year Selected");
			}
			Course acadSession = courseService.checkIfExistsInDB("acadSession", icaBean.getAcadSession());
			if(acadSession == null) {
				throw new ValidationException("Invalid Acad Session Selected");
			}
			if(icaBean.getCampusId() != null && !icaBean.getCampusId().isEmpty()) {
				Course campusId = courseService.checkIfExistsInDB("campusId", icaBean.getCampusId());
				if(campusId == null) {
					throw new ValidationException("Invalid Campus Selected");
				}
			}
			Course moduleId = courseService.checkIfExistsInDB("moduleId", icaBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module Selected");
			}
			Course progId = courseService.checkIfExistsInDB("programId", icaBean.getProgramId());			
			if(progId == null) {
				throw new ValidationException("Invalid Program Selected");
			} 
			if(icaBean.getTotalMarks() != null && !icaBean.getTotalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks(),icaBean.getExternalMarks(),icaBean.getExternalPassMarks(),icaBean.getTotalMarks());
			} else
			if(icaBean.getInternalMarks() != null && !icaBean.getInternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks());
			} else
			if(icaBean.getExternalMarks() != null && !icaBean.getExternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getExternalMarks(), icaBean.getExternalPassMarks());
			} else {
				throw new ValidationException("Invalid Marks");
			}
			Utils.validateStartAndEndDates(icaBean.getStartDate(), icaBean.getEndDate());
			
			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
				icaBean.setScaledReq("N");
				icaBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumeric(icaBean.getScaledMarks());
			}
			if(!icaBean.getIcaDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaDesc());
			}
			logger.info("Validation Done");
			
			String username = principal.getName();

			icaBean.setCreatedBy(username);
			icaBean.setLastModifiedBy(username);
			icaBean.setActive("Y");
			icaBean.setIsIcaDivisionWise("Y");
			icaBean.setIsNonEventModule("N");

//			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
//				icaBean.setScaledReq("N");
//				icaBean.setScaledMarks(null);
//			}

			icaBean.setAssignedFaculty(null);
			
//			if(!Utils.validateStartAndEndDates(icaBean.getStartDate(), icaBean.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addIcaFormForDivision";
//			}
			/* New Audit changes end */
			List<IcaBean> icaDBList = icaBeanService.checkAlreadyExistICAList(icaBean.getModuleId(),
					icaBean.getAcadYear(), icaBean.getCampusId(), icaBean.getAcadSession());

			for (IcaBean icaDB : icaDBList) {
				if (icaDB != null) {
					if (icaBean.getProgramId().contains(",")) {
						List<String> programIds = Arrays.asList(icaBean.getProgramId().split(","));
						if (icaDB.getProgramId().contains(",")) {
							for (String db : Arrays.asList(icaDB.getProgramId().split(","))) {
								if (programIds.contains(db)) {
									setError(redirectAttrs, "ICA Already Exist");
									return "redirect:/addIcaFormForDivision";
								}
							}
						} else {
							if (programIds.contains(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForDivision";
							}
						}
					} else {
						if (icaDB.getProgramId().contains(",")) {
							if (Arrays.asList(icaDB.getProgramId().split(",")).contains(icaBean.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForDivision";
							}
						} else {
							if (icaBean.getProgramId().equals(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForDivision";
							}
						}
					}
				}
			}
			/*
			 * if (totalMarks != icaInternalMarks + icaExternalMarks) {
			 * 
			 * setError(redirectAttrs,
			 * "Total Marks & Sum of Internal,External Marks Should Match"); return
			 * "redirect:/addIcaForm"; logger.info("validation"); } else
			 */

			List<UserCourse> facultyListDivisionWise = userCourseService.getAllFacultiesDivisionWise(
					icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getModuleId(), icaBean.getProgramId(),
					icaBean.getCampusId());
			if (facultyListDivisionWise.size() > 0) {
				icaBeanService.insertWithIdReturn(icaBean);
				String parentIcaId = String.valueOf(icaBean.getId());

				List<String> distinctCourseId = new ArrayList<>();
				List<String> userList = new ArrayList<String>();
				for (UserCourse uc : facultyListDivisionWise) {

					IcaBean icaDiv = icaBean;

					icaDiv.setAssignedFaculty(uc.getUsername());
					icaDiv.setEventId(uc.getEventId());
					icaDiv.setParentIcaId(parentIcaId);

					if (!distinctCourseId.contains(uc.getEventId())) {
						userList.add(icaDiv.getAssignedFaculty());
						icaBeanService.insertWithIdReturn(icaDiv);
						distinctCourseId.add(uc.getEventId());
					}

				}
				// Hiren
				User u = userService.findByUserName(username);
				String subject = " New ICA: " + icaBean.getIcaName();
				Map<String, Map<String, String>> result = null;

				String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaBean.getIcaName() + " <br>"
						+ "<b>ICA Description: </b>" + icaBean.getIcaDesc() + "<br><br>"
						+ "<b>Note: </b>This ICA is created by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				if (!userList.isEmpty()) {
					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}

				setSuccess(redirectAttrs, "ICA Added For " + distinctCourseId.size() + " Divisions successfully ");
				redirectAttrs.addAttribute("id", parentIcaId);
				// redirectAttrs.addFlashAttribute("icaBean", icaBean);
				return "redirect:/addIcaComponentsForm";
			} else {
				setError(redirectAttrs, "There is no event for selected module to create division ICA.");
				return "redirect:/addIcaFormForDivision";
			}
		} catch (ValidationException ve) {
		logger.info("INSIDE Validation Exception");
		logger.error(ve.getMessage(), ve);
		setError(redirectAttrs, ve.getMessage());
		return "redirect:/addIcaFormForDivision";
	}

		catch (Exception ex) {

			logger.error("Excption", ex);

			setError(redirectAttrs, "Error While Creating ICA");
			return "redirect:/addIcaFormForDivision";
		}

	}

	public String getFilePathOfSubjectWiseReport(String programId, String acadYear, String acadSession, String campusId,
			String collegeName) {
		List<String> headers = new ArrayList();
		String h[] = { "SAPID", "Name", "Roll.No", "Obtained Marks", "Total ICA Marks", "Status", "Remarks" };
		headers = Arrays.asList(h);

		String fileName = "ICA-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "ICA-Report" + programId
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFCellStyle totalStyle = workbook.createCellStyle();

		XSSFCellStyle totalRedStyle = workbook.createCellStyle();

		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(true);
		defaultFont.setItalic(false);
		totalStyle.setFont(defaultFont);
		totalRedStyle.setFont(defaultFont);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		// style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFont(defaultFont);
		headerStyle.setWrapText(true);

		XSSFColor grayColor = new XSSFColor(new Color(211, 211, 211));
		totalStyle.setFillBackgroundColor(grayColor);

		XSSFColor redColor = new XSSFColor(new Color(255, 0, 0));
		totalRedStyle.setFillBackgroundColor(redColor);

		XSSFCellStyle borderStyle = workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 0));
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalStyle.setFillPattern(FillPatternType.LESS_DOTS);

		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalRedStyle.setFillPattern(FillPatternType.LESS_DOTS);

		style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		/*
		 * if (programId.contains(",")) {
		 * 
		 * List<String> programIdList = Arrays.asList(programId.split(",")); int
		 * count=0; for (String p : programIdList) { count++; String programName =
		 * programService.findByID( Long.valueOf(p)).getProgramName(); XSSFSheet sheet =
		 * workbook.createSheet(programName+"-"+count);
		 * logger.info("count--->"+workbook.getSheetName(0));
		 * logger.info("count--->"+workbook.getSheetName(1));; } }
		 */

//		List<IcaTotalMarks> getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParam(acadYear,
//				acadSession, programId, campusId);
		// 26-04-2021
		List<IcaTotalMarks> getIcaTotalMarksByParam = new ArrayList<IcaTotalMarks>();
		if (acadSession.contains(",")) {
			logger.info("acadSession comma--->");
			if (programId.contains(",")) {
				logger.info("program comma--->");
				List<String> programidsList = Arrays.asList(programId.split(","));
				for (String str : programidsList) {
					List<IcaTotalMarks> itmByParamNew = icaTotalMarksService.getIcaTotalMarksByParamCoursera(acadYear,
							acadSession, str, campusId);
					getIcaTotalMarksByParam.addAll(itmByParamNew);
				}
			} else {
				logger.info("program--->");
				getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParamCoursera(acadYear, acadSession,
						programId, campusId);
			}
		} else {
			logger.info("acadSession--->");
			getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParam(acadYear, acadSession, programId,
					campusId);
		}
		List<IcaTotalMarks> getIcaTotalMarksByParamNonEvent = icaTotalMarksService
				.getIcaTotalMarksByParamForNonEvent(acadYear, acadSession, programId, campusId);

		getIcaTotalMarksByParam.addAll(getIcaTotalMarksByParamNonEvent);

		Map<String, Course> courseMapper = new HashMap<>();
		Map<String, List<IcaTotalMarks>> icaTotalMarksModuleWise = new HashMap<>();
		for (IcaTotalMarks itm : getIcaTotalMarksByParam) {

			if (!courseMapper.containsKey(itm.getModuleId())) {
				Course c = new Course();
				c.setModuleId(itm.getModuleId());
				c.setModuleName(itm.getModuleName());
				c.setAcadYear(itm.getAcadYear());
				c.setAcadSession(itm.getAcadSession());
				c.setModuleAbbr(itm.getModuleAbbr());

				// courseMapper.put(itm.getModuleId(), c);
				if (!acadSession.contains(",")) {
					if (acadSession.equals(itm.getAcadSession())) {
						courseMapper.put(itm.getModuleId(), c);
					}
				} else {
					courseMapper.put(itm.getModuleId(), c);
				}
			}
		}
		for (String moduleId : courseMapper.keySet()) {
			List<IcaTotalMarks> itmList = getIcaTotalMarksByParam.stream().filter(o -> o.getModuleId().equals(moduleId))
					.collect(Collectors.toList());
			icaTotalMarksModuleWise.put(moduleId, itmList);
		}

		for (String moduleId : courseMapper.keySet()) {

			// Title Of Reports
			String acdYear = courseMapper.get(moduleId).getAcadYear();
			String session = courseMapper.get(moduleId).getAcadSession();

			String faculties = userCourseService.getFacultiesByParam(acdYear, session, moduleId, programId);

			String programName = programService.findByID(Long.valueOf(programId)).getProgramName();
			int rowNum = 0;
			logger.info("program name" + programName);
			XSSFSheet sheet = workbook.createSheet(courseMapper.get(moduleId).getModuleAbbr());
			// XSSFSheet sheet = workbook.getSheet(programName);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));

			Row collegeNameRow = sheet.createRow(rowNum++);
			Row programRow = sheet.createRow(rowNum++);
			Row reviewRow = sheet.createRow(rowNum++);
			Row moduleNameAbbrRow = sheet.createRow(rowNum++);
			Row facultiesRow = sheet.createRow(rowNum++);

			Cell collegeCell = collegeNameRow.createCell(0);
			if (campusId == null) {
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			} else {
				collegeName = programService.getCollgeName(programId, campusId);
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			}
			// String year = acadYear;
			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(programName + "-" + acadSession + "-" + batch);
			Cell subjNameAbbrCell = moduleNameAbbrRow.createCell(0);
			Cell facultiesCell = facultiesRow.createCell(0);
			Cell reviewCell = reviewRow.createCell(0);
			/*
			 * LocalDateTime now = LocalDateTime.now();
			 * 
			 * DateTimeFormatter formatter = DateTimeFormatter
			 * .ofPattern("yyyy-MM-dd HH:mm:ss");
			 */

			// String formatDateTime = now.format(formatter);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			/* reviewCell.setCellValue("Mid Term Review report "); */
			String subjNameVal = courseMapper.get(moduleId).getModuleName() + "("
					+ courseMapper.get(moduleId).getModuleAbbr() + ")";
			subjNameAbbrCell.setCellValue(subjNameVal);
			reviewCell.setCellValue("Student review ICA Marks");
			facultiesCell.setCellValue(faculties);
			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);
			subjNameAbbrCell.setCellStyle(centerStyle);
			facultiesCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(7).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			/*
			 * border.setBorderBottom(BorderStyle.MEDIUM);
			 * border.setBorderLeft(BorderStyle.MEDIUM);
			 * border.setBorderRight(BorderStyle.MEDIUM);
			 * border.setBorderTop(BorderStyle.MEDIUM);
			 */
			// dateRow.getCell(8).setCellStyle(border);
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			for (IcaTotalMarks itm : icaTotalMarksModuleWise.get(moduleId)) {
				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(itm.getUsername());

				rowd.createCell(colNum++).setCellValue(itm.getStudentName());
				rowd.createCell(colNum++).setCellValue(itm.getRollNo());

				rowd.createCell(colNum++).setCellValue(itm.getIcaTotalMarks());
				rowd.createCell(colNum++).setCellValue(itm.getInternalMarks());

				rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
				rowd.createCell(colNum++).setCellValue(itm.getRemarks());

			}
			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
			// log.info("Width Col ------- " + sheet.getColumnWidth(2));
			sheet.setColumnWidth(2, 5500);
			sheet.setColumnWidth(3, 6500);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(8);
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			// FileUtils.deleteQuietly(new File(filePath));
			workbook.close();
		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return filePath;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaRaiseQueryReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadIcaRaiseQueryReport(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String acadYear) throws URIException {
		Token userdetails1 = (Token) p;
		m.addAttribute("webPage", new WebPage("viewAssignment", "Search Assignments", true, true));

		List<IcaTotalMarks> itmRaiseQueryList = new ArrayList<>();
		List<IcaTotalMarks> itmCompQueryList = new ArrayList<>();

		itmRaiseQueryList = icaTotalMarksService.getRaiseQueries(acadYear, userdetails1.getAuthorities(), p.getName());
		itmCompQueryList = icaTotalMarksService.getCompQueries(acadYear, userdetails1.getAuthorities(), p.getName());
		List<IcaTotalMarks> itmNERaiseQueryList = icaTotalMarksService.getNonEventRaiseQueries(acadYear,
				userdetails1.getAuthorities(), p.getName());
		List<IcaTotalMarks> compQueryListNE = icaTotalMarksService.getCompQueriesNE(acadYear,
				userdetails1.getAuthorities(), p.getName());
		if (itmNERaiseQueryList.size() > 0) {
			itmRaiseQueryList.addAll(itmNERaiseQueryList);
		}
		if (compQueryListNE.size() > 0 || itmCompQueryList.size() > 0) {
			itmRaiseQueryList.addAll(compQueryListNE);
			itmRaiseQueryList.addAll(itmCompQueryList);
		}
		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("ICA Name", "AcadYear", "Session", "Roll No",

				"Component", "Component Marks", "Student SAPID", "Student-Name", "Student-EmailId", "Program",
				"Subject", "Total Marks Obtained", "Query Raised Date", "Query", "Division", "Assigned Faculty"));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		File file = null;
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfRaisedQueries = new ArrayList<>();
			/*
			 * fileName = "TestQuestionForTest-" + " " + testId + "-" +
			 * Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
			 */
			fileName = "studentIcaQueries.xlsx";
			filePath = downloadAllFolder + File.separator + fileName;
			Map<String, String> getCompMap = new HashMap<>();
			List<PredefinedIcaComponent> compList = predefinedIcaComponentService.findAllActive();

			for (PredefinedIcaComponent pd : compList) {
				getCompMap.put(String.valueOf(pd.getId()), pd.getComponentName());
			}
			 Calendar c = Calendar.getInstance();
	        c.setTime(Utils.getInIST());
	        int month = c.get(Calendar.MONTH) + 1;
	        int year = c.get(Calendar.YEAR) - 1;
	        int currentYear = c.get(Calendar.YEAR);
			for (IcaTotalMarks itm : itmRaiseQueryList) {
				Map<String, Object> mapOfQueries = new HashMap<>();
				if(month > 6 && itm.getAcadYear().equals(String.valueOf(currentYear))) {
					mapOfQueries.put("ICA Name", itm.getIcaName());
					mapOfQueries.put("AcadYear", itm.getAcadYear());
					mapOfQueries.put("Session", itm.getAcadSession());
					mapOfQueries.put("Student SAPID", itm.getUsername());
					mapOfQueries.put("Student-Name", itm.getStudentName());
					mapOfQueries.put("Program", itm.getProgramName());
					mapOfQueries.put("Subject", itm.getModuleName());
					mapOfQueries.put("Total Marks Obtained", itm.getIcaTotalMarks());
					mapOfQueries.put("Query", itm.getQuery());
					mapOfQueries.put("Assigned Faculty", itm.getAssignedFaculty());
					mapOfQueries.put("Roll No", itm.getRollNo());
					mapOfQueries.put("Student-EmailId", itm.getEmail());
					mapOfQueries.put("Query Raised Date", itm.getRaiseQDate());
					// ,"Component Marks"
					String component = itm.getCompId() != null ? getCompMap.get(itm.getCompId()) : "NA";
					String compMarks = itm.getComponentMarks() != null ? itm.getComponentMarks() : "NA";
					mapOfQueries.put("Component", component);

					mapOfQueries.put("Component Marks", compMarks);

					listOfMapOfRaisedQueries.add(mapOfQueries);
				}else if(itm.getAcadYear().equals(String.valueOf(year)) || itm.getAcadYear().equals(String.valueOf(currentYear))){
					mapOfQueries.put("ICA Name", itm.getIcaName());
					mapOfQueries.put("AcadYear", itm.getAcadYear());
					mapOfQueries.put("Session", itm.getAcadSession());
					mapOfQueries.put("Student SAPID", itm.getUsername());
					mapOfQueries.put("Student-Name", itm.getStudentName());
					mapOfQueries.put("Program", itm.getProgramName());
					mapOfQueries.put("Subject", itm.getModuleName());
					mapOfQueries.put("Total Marks Obtained", itm.getIcaTotalMarks());
					mapOfQueries.put("Query", itm.getQuery());
					mapOfQueries.put("Assigned Faculty", itm.getAssignedFaculty());
					mapOfQueries.put("Roll No", itm.getRollNo());
					mapOfQueries.put("Student-EmailId", itm.getEmail());
					mapOfQueries.put("Query Raised Date", itm.getRaiseQDate());
					// ,"Component Marks"
					String component = itm.getCompId() != null ? getCompMap.get(itm.getCompId()) : "NA";
					String compMarks = itm.getComponentMarks() != null ? itm.getComponentMarks() : "NA";
					mapOfQueries.put("Component", component);

					mapOfQueries.put("Component Marks", compMarks);

					listOfMapOfRaisedQueries.add(mapOfQueries);
				}
				
			}
			excelCreater.CreateExcelFile(listOfMapOfRaisedQueries, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// copy it to response's OutputStream
			file = new File(filePath);
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
				FileUtils.deleteQuietly(file);
			}

		}
		return null;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadIcaStatus(Model m, Principal p, HttpServletResponse response,
			@RequestParam(required = false) String acadYear, @RequestParam(required = false) String forAll)
			throws URIException {

		Token userdetails1 = (Token) p;

		m.addAttribute("webPage", new WebPage("searchCourse", "Search ICA", true, false));
		List<IcaBean> icaList = new ArrayList<>();

		String role = "";
		if (forAll != null) {
			icaList = icaBeanService.findIcaStatusListForAll();
		} else {

			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				role = Role.ROLE_FACULTY.name();
				icaList = icaBeanService.findIcaStatusListByProgram(userdetails1.getProgramId(), role, p.getName());

			} else {
				role = Role.ROLE_ADMIN.name();
				icaList = icaBeanService.findIcaStatusListByProgram(userdetails1.getProgramId(), role, p.getName());
			}
		}

		boolean isQueryRaised = false;
		for (IcaBean ica : icaList) {
			if (ica.getIcaQueryId() != null) {
				isQueryRaised = true;
			}
			if (ica.getProgramId().contains(",")) {
				ica.setProgramName(programService.getProgramNamesForIca(ica.getProgramId()));
			}
		}

		List<String> validateHeaders = new ArrayList<String>(
				Arrays.asList("ICA Name", "Program", "AcadYear", "Session", "Subject", "Division", "Campus",
						"Assigned Faculty", "Scaled Marks", "Start Date", "End Date", "Total Internal Marks", "Status",
						"Published?", "Published Date", "Query Raised??", "Re-evaluate Approved"));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		File file = null;
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfRaisedQueries = new ArrayList<>();
			/*
			 * fileName = "TestQuestionForTest-" + " " + testId + "-" +
			 * Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
			 */
			fileName = "studentIcaQueries.xlsx";
			filePath = downloadAllFolder + File.separator + fileName;

			for (IcaBean itm : icaList) {
				if (itm.getParentIcaId() == null && "Y".equals(itm.getIsIcaDivisionWise())) {
					continue;
				}
				Map<String, Object> mapOfQueries = new HashMap<>();

				mapOfQueries.put("ICA Name", itm.getIcaName());
				mapOfQueries.put("Program", itm.getProgramName());
				mapOfQueries.put("AcadYear", itm.getAcadYear());
				mapOfQueries.put("Session", itm.getAcadSession());
				mapOfQueries.put("Subject", itm.getModuleName());
				if (itm.getCourseName() != null) {

					mapOfQueries.put("Division", itm.getCourseName());
				} else {
					mapOfQueries.put("Division", "NA");
				}

				if (itm.getCampusName() != null) {
					mapOfQueries.put("Campus", itm.getAcadSession());
				} else {
					mapOfQueries.put("Campus", "NA");
				}

				mapOfQueries.put("Assigned Faculty", itm.getFacultyName());
				if (itm.getScaledMarks() != null) {
					mapOfQueries.put("Scaled Marks", itm.getScaledMarks());
				} else {
					mapOfQueries.put("Scaled Marks", "NA");
				}
				mapOfQueries.put("Start Date", itm.getStartDate());
				mapOfQueries.put("End Date", itm.getEndDate());
				mapOfQueries.put("Total Internal Marks", itm.getTotalMarks());
				if ("Y".equals(itm.getIsSubmitted())) {
					mapOfQueries.put("Status", "Evaluated");

				} else {
					mapOfQueries.put("Status", "Pending");
				}

				if ("Y".equals(itm.getIsPublished())) {
					mapOfQueries.put("Published?", "Y");
					mapOfQueries.put("Published Date", itm.getPublishedDate());
				} else {
					mapOfQueries.put("Published?", "N");
					mapOfQueries.put("Published Date", "NA");
				}

				if (itm.getIcaQueryId() != null) {
					mapOfQueries.put("Query Raised??", "Y");
					if ("Y".equals(itm.getIsApproved())) {
						mapOfQueries.put("Re-evaluate Approved", "Y");
					} else {
						mapOfQueries.put("Re-evaluate Approved", "N");
					}

				} else {
					mapOfQueries.put("Query Raised??", "NA");
					mapOfQueries.put("Re-evaluate Approved", "NA");
				}
				listOfMapOfRaisedQueries.add(mapOfQueries);
			}
			excelCreater.CreateExcelFile(listOfMapOfRaisedQueries, validateHeaders, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// copy it to response's OutputStream
			file = new File(filePath);
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
				FileUtils.deleteQuietly(file);
			}

		}
		return null;

	}

	// Start Non-Event Module ICA

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/addIcaFormForNonEventModules", method = { RequestMethod.GET, RequestMethod.POST })
	public String addIcaFormForNonEventModules(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Add ICA", true, false));

		IcaBean ica = new IcaBean();
		// ica.setProgramId(userdetails1.getProgramId());

		if (id != null) {

			ica = icaBeanService.findByID(Long.valueOf(id));
			// IcaTotalMarks icaMarks = icaTotalMarksService.ge
			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			logger.info("ica received" + ica);
			if ("Y".equals(ica.getIsIcaDivisionWise()) && ica.getParentIcaId() == null) {

				m.addAttribute("isParentIca", "true");
				m.addAttribute("isGradingStart",
						icaComponentMarksService.checkWhetherGradingStartOrNotP(String.valueOf(ica.getId())));

			} else {
				m.addAttribute("isParentIca", "false");
				m.addAttribute("isGradingStart",
						icaComponentMarksService.checkWhetherGradingStartOrNot(String.valueOf(ica.getId())));
			}

			if (currentDate.compareTo(ica.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot update, ICA Deadline is over");
				return "redirect:/searchIcaList";
			} else if ("Y".equals(ica.getIsSubmitted())) {
				setNote(redirectAttrs, "Cannot update, ICA is Already Evaluated");
				return "redirect:/searchIcaList";
			} else {
				if (currentDate.compareTo(ica.getStartDate()) > 0) {

					m.addAttribute("icaStarted", "true");
				} else {
					m.addAttribute("icaStarted", "false");
				}
			}

			m.addAttribute("edit", "true");
			if (null != ica.getIsNonEventModule() && ("Y").equals(ica.getIsNonEventModule())) {
				m.addAttribute("moduleName", courseService.getModuleNameForNonEvent(ica.getModuleId()));
				m.addAttribute("facultyList", new ArrayList<UserCourse>());
			} else {
				m.addAttribute("moduleName", courseService.getModuleName(ica.getModuleId()));
				m.addAttribute("facultyList",
						userCourseService.findAllFacultyWithModuleIdICA(ica.getModuleId(), ica.getAcadYear()));
			}
			m.addAttribute("programList", programService.getProgramListByIds(ica.getProgramId()));
			m.addAttribute("icaBean", ica);
			if ("Y".equals(ica.getIsIcaDivisionWise()) && ica.getParentIcaId() == null) {
				setNote(m, "This is Division-Wise ICA,Any Update in this, will reflect to all ICA Divisions");
				return "ica/createIcaForDivision";
			}
		}
		// Acad Year for creating ica

		List<String> acadYearCodeListNS = courseService.findAcadYearCodeForNS();
		logger.info("acadYearCodeListNS------------>" + acadYearCodeListNS.size() + " " + acadYearCodeListNS);
		m.addAttribute("acadYearCodeListNS", acadYearCodeListNS);

		/*
		 * List<String> acadYearCodeList = courseService.findAcadYearCode();
		 * logger.info("acadYearCodeList------------>"+acadYearCodeList);
		 * m.addAttribute("acadYearCodeList",acadYearCodeList);
		 */

		m.addAttribute("icaBean", ica);

		return "ica/createIcaForNonEventModule";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/getSessionByParamForModule", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getSessionByParamForModule(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService
				.acadSessionListByAcadYearAndCampusForModule(acadYear, campusId);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getAcadSession(), module.getAcadSession());
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/getModuleByParamForModule", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getModuleByParamForModule(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId, @RequestParam(name = "acadSession") String acadSession,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService
				.moduleListByAcadYearAndCampusForModule(acadSession, acadYear, campusId);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getModuleId(), module.getModuleName() + "(" + module.getModuleAbbr() + ")");
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/GetProgramsFromAcadSessionYearModuleForModule", method = { RequestMethod.GET })
	public @ResponseBody String GetProgramsFromAcadSessionYearModuleForModule(@RequestParam String acadSession,
			@RequestParam String moduleId, @RequestParam String year, Principal principal) {

		List<Program> programList = new ArrayList<Program>();

		programList = programService.findAllProgramsWithAcadSessionYearModuleForModule(acadSession, year, moduleId);

		JSONArray jsonarray = new JSONArray();
		try {
			for (Program p : programList) {
				JSONObject obj = new JSONObject();
				obj.put("value", p.getId().toString());
				obj.put("text", p.getProgramName());
				jsonarray.put(obj);
			}

			// logger.info(jsonarray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
		return jsonarray.toString();
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addIcaForNonEventModules", method = RequestMethod.POST)
	public String addIcaForNonEventModules(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {

		try {
			logger.info("Validating /addIcaForNonEventModules...");
			HtmlValidation.validateHtml(icaBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", icaBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year Selected");
			}
			Course acadSession = courseService.checkIfAcadSessionExists(icaBean.getAcadSession());
			if(acadSession == null) {
				throw new ValidationException("Invalid Acad Session Selected");
			}
			if(icaBean.getCampusId() != null && !icaBean.getCampusId().isEmpty()) {
				Course campusId = courseService.checkIfExistsInDB("campusId", icaBean.getCampusId());
				if(campusId == null) {
					throw new ValidationException("Invalid Campus Selected");
				}
			}
			Course moduleId = courseService.checkIfModuleExists(icaBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module Selected");
			}
			List<String> progIds = null;
			if(icaBean.getProgramId().contains(",")) {
				progIds = Arrays.asList(icaBean.getProgramId().split(","));
				for(String programId : progIds) {
					Course progId = courseService.checkIfExistsInDB("programId", programId);			
					if(progId == null) {
						throw new ValidationException("Invalid Program Selected");
					}
				}
			} else {
				Course progId = courseService.checkIfExistsInDB("programId", icaBean.getProgramId());
				if(progId == null) {
					throw new ValidationException("Invalid Program Selected");
				}
			}
			if(icaBean.getTotalMarks() != null && !icaBean.getTotalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks(),icaBean.getExternalMarks(),icaBean.getExternalPassMarks(),icaBean.getTotalMarks());
			} else
			if(icaBean.getInternalMarks() != null && !icaBean.getInternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks());
			} else
			if(icaBean.getExternalMarks() != null && !icaBean.getExternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getExternalMarks(), icaBean.getExternalPassMarks());
			} else {
				throw new ValidationException("Invalid Marks");
			}
			Utils.validateStartAndEndDates(icaBean.getStartDate(), icaBean.getEndDate());

			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
				icaBean.setScaledReq("N");
				icaBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumeric(icaBean.getScaledMarks());
			}
			if(icaBean.getIcaDesc() != null && !icaBean.getIcaDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaDesc());
			}
			logger.info("Validation Done");
			
			String username = principal.getName();

			icaBean.setCreatedBy(username);
			icaBean.setLastModifiedBy(username);
			icaBean.setActive("Y");
			icaBean.setIsNonEventModule("Y");
			
//			to be removed if everything works fine
//			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
//				icaBean.setScaledReq("N");
//				icaBean.setScaledMarks(null);
//			}

			/*
			 * IcaBean icaDB = icaBeanService.checkAlreadyExistICA( icaBean.getModuleId(),
			 * icaBean.getAcadYear(), icaBean.getCampusId(), icaBean.getAcadSession());
			 */

			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(icaBean.getStartDate(), icaBean.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addIcaFormForNonEventModules";
//			}
			/* New Audit changes end */
			List<IcaBean> icaDBList = icaBeanService.checkAlreadyExistICAList(icaBean.getModuleId(),
					icaBean.getAcadYear(), icaBean.getCampusId(), icaBean.getAcadSession());

			// logger.info("icaDBList---------->" + icaDBList);
			for (IcaBean icaDB : icaDBList) {
				if (icaDB != null) {

					// logger.info("icaDB---------->" + icaDB);
					if (icaBean.getProgramId().contains(",")) {
						List<String> programIds = Arrays.asList(icaBean.getProgramId().split(","));
						if (icaDB.getProgramId().contains(",")) {
							for (String db : Arrays.asList(icaDB.getProgramId().split(","))) {
								if (programIds.contains(db)) {
									setError(redirectAttrs, "ICA Already Exist");
									return "redirect:/addIcaFormForNonEventModules";
								}
							}
						} else {
							if (programIds.contains(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForNonEventModules";
							}
						}
					} else {
						if (icaDB.getProgramId().contains(",")) {
							if (Arrays.asList(icaDB.getProgramId().split(",")).contains(icaBean.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForNonEventModules";
							}
						} else {
							if (icaBean.getProgramId().equals(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForNonEventModules";
							}
						}
					}
				}
			}
			/*
			 * if (totalMarks != icaInternalMarks + icaExternalMarks) {
			 * 
			 * setError(redirectAttrs,
			 * "Total Marks & Sum of Internal,External Marks Should Match"); return
			 * "redirect:/addIcaForm"; logger.info("validation"); } else
			 */

			icaBeanService.insertWithIdReturn(icaBean);

//			User u = userService.findByUserName(username);
//			String subject = " New ICA: " + icaBean.getIcaName();
//			Map<String, Map<String, String>> result = null;
//			List<String> userList = new ArrayList<String>();
//			List<String> acadSessionList = new ArrayList<>();
//			userList.add(icaBean.getAssignedFaculty());
//			String notificationEmailMessage = "<html><body>"
//					+ "<b>ICA Name: </b>"+icaBean.getIcaName()+" <br>"
//					+ "<b>ICA Description: </b>"+ icaBean.getIcaDesc() +"<br><br>"
//					+ "<b>Note: </b>This ICA is created by : ?? <br>"
//					+"To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
//					+ "This is auto-generated email, do not reply on this.</body></html>";
//
//			if (!userList.isEmpty()) {
//				notificationEmailMessage = notificationEmailMessage
//						.toString().replace(
//								"??",
//								" Name : " + u.getFirstname() + " "
//										+ u.getLastname()
//										+ " ( Email-Id: "
//										+ u.getEmail() + ")");
//				result = userService.findEmailByUserName(userList);
//				Map<String, String> email = result.get("emails");
//				Map<String, String> mobiles = new HashMap();
//				logger.info("email -----> "+email);
//				logger.info("mobile -----> "+mobiles);
//				logger.info("subject -----> "+subject);
//				logger.info("notificationEmailMessage -----> "+notificationEmailMessage);
//				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
//			}

			setSuccess(redirectAttrs, "ICA Added Successfully");
			// redirectAttrs.addAttribute("icaId", icaBean.getId());
			redirectAttrs.addFlashAttribute("icaBean", icaBean);
			return "redirect:/addIcaComponentsForm";

		}
		
		catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addIcaForm";
		}

		catch (Exception ex) {

			logger.error("Excption while creating ICA", ex);

			setError(redirectAttrs, "Error While Creating ICA,ICA May have already created");
			return "redirect:/addIcaFormForNonEventModules";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/evaluateIcaForNonEventModule", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateIcaForNonEventModule(Model m, Principal principal, @RequestParam Long icaId,
			@ModelAttribute IcaBean icaBean, RedirectAttributes redirectAttrs) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		if (icaId != null) {
			icaBean.setId(icaId);
		}

		Map<String, String> studentIcaMarksMap = new HashMap<>();

		// Map<String,String> studentIcaTotalMarks = new HashMap<>();

		icaBean = icaBeanService.findByID(icaBean.getId());

		String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());

		if (currentDate.compareTo(icaBean.getEndDate()) > 0) {
			setNote(redirectAttrs, "Cannot Evaluate, ICA Deadline is over");
			return "redirect:/searchIcaList";
		}

		if (currentDate.compareTo(icaBean.getStartDate()) < 0) {
			setNote(redirectAttrs, "Cannot Evaluate, ICA has not started yet.");
			return "redirect:/searchIcaList";
		}
		List<IcaComponent> icaComponentsByIcaId = new ArrayList<>();
		int icaCompMarks = 0;

		String raiseQComp = "";
		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			if ("Y".equals(icaBean.getIsSubmitted())) {

				IcaQueries icaQueryByIcaId = icaQueriesService.findByIcaId(icaBean.getId());

				if (icaQueryByIcaId != null) {
					if ("Y".equals(icaQueryByIcaId.getIsApproved())) {
						m.addAttribute("icaQuery", "true");
					} else {

						setNote(redirectAttrs, "ICA Reevaluate Not Approved By Co-ordinator");
						return "redirect:/searchIcaList";
					}
				} else {
					m.addAttribute("icaQuery", "false");
					setNote(redirectAttrs, "ICA Already Evaluated");
					return "redirect:/searchIcaList";
				}
			}
		} else {
			long idIca = icaBean.getId();

			IcaComponent submittedIc = icaComponentService.getSubmittedIcaComponent(idIca);
			int maxSubmittedSeqNo = icaComponentService.getSubmittedSeqNo(icaId);
			int maxSeqNo = icaComponentService.getMaxSeqNo(idIca);

			if (submittedIc != null) {
				if ("Y".equals(submittedIc.getIsPublished())) {
					String dueDate = Utils.addDaysToDate(submittedIc.getPublishedDate(), 4);
					IcaQueries icQ = icaQueriesService.findByIcaIdAndCompId(String.valueOf(idIca),
							submittedIc.getComponentId());
					if (icQ != null) {
						if ("Y".equals(icQ.getIsApproved())) {
							if (!"Y".equals(icQ.getIsReEvaluated())) {

								m.addAttribute("icaQuery", "true");
								raiseQComp = submittedIc.getComponentId();

							} else {

								if (maxSeqNo == maxSubmittedSeqNo) {
									m.addAttribute("icaQuery", "false");
									setNote(redirectAttrs, "ICA Already Evaluated");
									return "redirect:/searchIcaList";
								}
								if (currentDate.compareTo(dueDate) < 0) {
									setNote(redirectAttrs,
											"You cannot evaluate next component during the raise query process,"
													+ " You can only re-evaluate the current component");
									return "redirect:/searchIcaList";
								}
							}
						} else {
							setNote(redirectAttrs, "ICA Component Reevaluation is not approved by co-ordinator");
							return "redirect:/searchIcaList";
						}
					} else {

						if (maxSeqNo == maxSubmittedSeqNo) {
							m.addAttribute("icaQuery", "false");
							setNote(redirectAttrs, "ICA Already Evaluated");
							return "redirect:/searchIcaList";
						}
						if (currentDate.compareTo(dueDate) < 0) {
							setNote(redirectAttrs, "You cannot evaluate next component during the raise query process");
							return "redirect:/searchIcaList";
						}
					}
				} else {
					setNote(redirectAttrs,
							"You cannot evalute next component unless your current component is published"
									+ " and re-evaluation process completes");
					return "redirect:/searchIcaList";
				}
			}
		}

		IcaComponent icaComp = new IcaComponent();
		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			icaComponentsByIcaId = icaComponentService.icaComponentListByIcaId(icaBean.getId());

			if (icaComponentsByIcaId.size() > 0) {
				icaCompMarks = icaComponentService.icaTotalComponentMarks(icaBean.getId());
			}
		} else {
			icaComp = icaComponentService.icaComponentByIcaId(Long.valueOf(icaBean.getId()), raiseQComp);
			icaComponentsByIcaId.add(icaComp);
			if (icaComponentsByIcaId.size() > 0) {
				icaCompMarks = icaComponentService.icaTotalComponentMarks(Long.valueOf(icaBean.getId()));
			}
		}

		logger.info("icaBean IM" + icaBean.getInternalMarks() + "icaComp Marks" + icaCompMarks);

		if (icaComponentsByIcaId.size() == 0) {
			setNote(redirectAttrs,
					"Cannot Evaluate!! Ica components have not been added.Kindly Contact Your Co-ordinator");
			return "redirect:/searchIcaList";
		}

		if (Integer.parseInt(icaBean.getInternalMarks()) != icaCompMarks) {

			setError(redirectAttrs,
					"Cannot Evaluate!! Total Component Marks does not match with ICA Total Marks,Kindly Contact Your Co-ordinator");
			return "redirect:/searchIcaList";
		}

		List<IcaComponentMarks> icaComponentMarksByIcaId = new ArrayList<>();
		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByIcaId(icaBean.getId());
		} else {
			if (raiseQComp.isEmpty()) {
				icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByIcaId(icaBean.getId(),
						icaComp.getComponentId());
			} else {
				icaComponentMarksByIcaId = icaComponentMarksService.icaComponentMarksByIcaId(icaBean.getId(),
						raiseQComp);
			}
		}
		List<IcaTotalMarks> icaTotalMarksByIcaId = icaTotalMarksService.icaTotalMarksByIcaId(icaBean.getId());

		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			if (icaTotalMarksByIcaId.size() > 0 && icaComponentMarksByIcaId.size() > 0) {
				for (IcaTotalMarks i : icaTotalMarksByIcaId) {
					// Map<String, String> map = new HashMap<>();

					studentIcaMarksMap.put(i.getUsername() + "totalM", i.getIcaTotalMarks());
					studentIcaMarksMap.put(i.getUsername() + "scaleM", i.getIcaScaledMarks());
					studentIcaMarksMap.put(i.getUsername() + "remark", i.getRemarks());
					studentIcaMarksMap.put(i.getUsername() + "query", i.getQuery());

					// studentIcaMarksMap.put(i.getUsername(), map);

				}

				for (IcaComponentMarks ic : icaComponentMarksByIcaId) {

					studentIcaMarksMap.put(ic.getUsername() + "-" + ic.getComponentId(), ic.getMarks());
				}

			}
		} else {
			if (icaComponentMarksByIcaId.size() > 0) {
				for (IcaComponentMarks icm : icaComponentMarksByIcaId) {
					studentIcaMarksMap.put(icm.getUsername() + "-" + icm.getComponentId(), icm.getMarks());
					studentIcaMarksMap.put(icm.getUsername() + "remark", icm.getRemarks());
					studentIcaMarksMap.put(icm.getUsername() + "query", icm.getQuery());
				}
			}
		}
		String moduleName = courseService.getModuleNameForNonEvent(icaBean.getModuleId());
		List<UserCourse> studentsListForIca = new ArrayList<>();

		if (!"Y".equals(icaBean.getIsPublishCompWise())) {
			studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYearForNonEventModule(
					icaBean.getModuleId(), icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(),
					icaBean.getId(), icaBean.getCampusId(), null);
		} else {
			studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYearForNonEventModule(
					icaBean.getModuleId(), icaBean.getAcadYear(), icaBean.getAcadSession(), icaBean.getProgramId(),
					icaBean.getId(), icaBean.getCampusId(), icaComp.getComponentId());
		}

		studentsListForIca.sort(Comparator.comparing(UserCourse::getRollNo));
		/*
		 * List<IcaComponent> icaComponents = icaComponentService
		 * .icaComponentListByIcaId(icaBean.getId());
		 */
		if (studentsListForIca.isEmpty()) {
			setNote(redirectAttrs, "No Students Found");
			return "redirect:/searchIcaList";
		} else {
			logger.info("studentsListForIca-------------1>" + studentsListForIca);

			String filePathTemplate = generateTemplate(studentsListForIca, icaComponentsByIcaId, moduleName, icaId,
					studentIcaMarksMap);

			if ("error".equals(filePathTemplate)) {
				setError(m, "Error in downloading template");
			}

			m.addAttribute("filePath", filePathTemplate);

			m.addAttribute("acadSession", studentsListForIca.get(0).getAcadSession());

			m.addAttribute("moduleName", moduleName);
			// icaBean.setUserCourseList(studentsListForIca);
			//
			String p = programService.getProgramNamesForIca(icaBean.getProgramId());
			m.addAttribute("programName", p);
			m.addAttribute("ica", icaBean);
			m.addAttribute("icaComponents", icaComponentsByIcaId);
			m.addAttribute("studentsListForIca", studentsListForIca);
			logger.info("studentmarks map" + studentIcaMarksMap);
			m.addAttribute("studentMarksMap", studentIcaMarksMap);
			if (!"Y".equals(icaBean.getIsPublishCompWise())) {
				return "ica/evaluateIca";
			} else {
				m.addAttribute("compId", icaComp.getComponentId());
				return "ica/evaluateIcaCompWise";
			}
		}
	}

	// end Non-Event ICA Changes

	// NS changes
	// ----------------NonCreditIca----------------//

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addNonCreditIcaForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addNonCreditIcaForm(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();

		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();
		m.addAttribute("webPage", new WebPage("searchCourse", "Add NS", true, false));

		m.addAttribute("Program_Name", ProgramName);
		List<Program> programList = programService.findAllActive();

		m.addAttribute("programList", programList);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCampuses", userService.findCampus());

		List<String> programsId = new ArrayList<>();

		for (Program p : programList) {
			programsId.add(p.getId().toString());
		}

		List<UserCourse> acadSessionList = userCourseService.findAllAcadSessionsWithProgramIds(programsId);

		List<String> semesterList = new ArrayList<>();
		for (UserCourse uc : acadSessionList) {
			semesterList.add(uc.getAcadSession());
		}
		m.addAttribute("acadSessionList", semesterList);

		NsBean ns = new NsBean();

		if (id != null) {

			ns = nsService.findByID(Long.valueOf(id));
			m.addAttribute("edit", "true");
			m.addAttribute("moduleName", courseService.getModuleName(ns.getModuleId()));
			m.addAttribute("programList", programService.getProgramListByIds(ns.getProgramId()));

			m.addAttribute("nsBean", ns);

		}
		List<String> acadYearCodeListNS = courseService.findAcadYearCodeForNS();
		logger.info("acadYearCodeListNS------------>" + acadYearCodeListNS.size() + " " + acadYearCodeListNS);
		m.addAttribute("acadYearCodeListNS", acadYearCodeListNS);
		m.addAttribute("nsBean", ns);

		return "ica/addNonCreditIca";

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addNonCreditIca", method = RequestMethod.POST)
	public String addNonCreditIca(Model m, Principal principal, @ModelAttribute NsBean nsBean,
			RedirectAttributes redirectAttrs) {

		try {
			logger.info("Validating /addNonCreditIca...");
			HtmlValidation.validateHtml(nsBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(nsBean.getIcaName());
			Course acadYear = courseService.checkIfAcadYearExists(nsBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year Selected");
			}
			Course acadSession = courseService.checkIfAcadSessionExists(nsBean.getAcadSession());
			if(acadSession == null) {
				throw new ValidationException("Invalid Acad Session Selected");
			}
			Course moduleId = courseService.checkIfModuleExists(nsBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module Selected");
			}
			nsBean.setProgramId("0000000");
			List<String> progIds = null;
			if(nsBean.getProgramId().contains(",")) {
				progIds = Arrays.asList(nsBean.getProgramId().split(","));
				for(String programId : progIds) {
					Course progId = courseService.checkIfExistsInDB("programId", nsBean.getProgramId());		
					if(progId == null) {
						throw new ValidationException("Invalid Program Selected");
					}
				}
			} else {
				Course progId = courseService.checkIfExistsInDB("programId", nsBean.getProgramId());
				if(progId == null) {
					throw new ValidationException("Invalid Program Selected");
				}
			}
			if(nsBean.getCampusId() != null && !nsBean.getCampusId().isEmpty()){
				Course campusId = courseService.checkIfCampusExists(nsBean.getCampusId());
				if(campusId == null) {
					throw new ValidationException("Invalid Campus Selected");
				}
			}
			logger.info("above show to students");
			BusinessBypassRule.validateYesOrNo(nsBean.getShowToStudents());

			logger.info("Validation Done");
			
			String username = principal.getName();

			nsBean.setActive("Y");
			nsBean.setCreatedBy(username);
			nsBean.setLastModifiedBy(username);

			if (nsBean.getCampusId().isEmpty()) {
				nsBean.setCampusId(null);
				logger.info("campusId is empty");
			} else {
				logger.info("campusId is not empty" + nsBean.getCampusId());
			}
			NsBean nsDB = nsService.checkAlreadyExistNS(nsBean.getModuleId(), nsBean.getAcadYear(),
					nsBean.getProgramId(), nsBean.getCampusId(), nsBean.getAcadSession());

			if (nsDB != null) {

				setError(redirectAttrs, "NS Already Exist");
				return "redirect:/addNonCreditIcaForm";
			} else {
				nsService.insertWithIdReturn(nsBean);
				setSuccess(redirectAttrs, "NC Added Successfully");
			}

			// redirectAttrs.addFlashAttribute("nsBean", nsBean);
			redirectAttrs.addAttribute("ncIcaId", nsBean.getId());
			logger.info("redirect uploadNS");
			return "redirect:/uploadNS";

		} catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addNonCreditIcaForm";
		}

		catch (Exception ex) {

			logger.error("Excption", ex);

			setError(redirectAttrs, "Error While Creating NC");
			return "redirect:/addNonCreditIcaForm";
		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/uploadNS", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadNS(@ModelAttribute NsBean nsBean, Model m, Principal principal, @RequestParam String ncIcaId,
			RedirectAttributes redirectAttrs) {

		try {

			NsBean nsDB = nsService.actionNS(nsBean.getId());

			if (nsDB != null) {
				setNote(redirectAttrs, "Cannot Upload File, ICA Already Submitted & Published");
				return "redirect:/searchNsList";
			}

			m.addAttribute("ncIcaId", ncIcaId);
			return "ica/uploadNS";

		}

		catch (Exception ex) {

			logger.error("Excption", ex);

			setError(redirectAttrs, "Error");
			return "redirect:/addNonCreditIcaForm";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/getModuleIdByProgramId", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAcadYearByModuleId(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "programId") String programId) {
		String json = "";

		List<NonCreditIcaModule> NonCreditIcaModuleList = nonCreditIcaModuleService.getModuleIdByProgramId(programId,
				acadYear);

		logger.info("sized" + NonCreditIcaModuleList.size());

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		for (NonCreditIcaModule nc : NonCreditIcaModuleList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(nc.getModule_id(), nc.getModule_description());
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

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadNSTemplate", method = RequestMethod.GET)
	public void downloadNSTemplate(HttpServletResponse response, @ModelAttribute NsBean nsBean, Principal principal) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		InputStream is = null;
		String filePath = "";

		try {

			NsBean ns = nsService.findByID(nsBean.getId());

			List<SubjectSapEnroll> sList = new ArrayList<>();
			List<UserCourse> userCourseList = new ArrayList<>();
			sList = subjectSapEnrollService.getNSStudent(ns.getModuleId(), ns.getAcadYear(), ns.getProgramId(),
					ns.getAcadSession(), ns.getCampusId());

			if (sList.size() == 0) {
				userCourseList = userCourseService.findStudentByModuleIdAndAcadYearForNCAEvents(ns.getModuleId(),
						ns.getAcadYear(), ns.getAcadSession(), ns.getProgramId(), ns.getCampusId());

				userCourseList.sort(Comparator.comparing(UserCourse::getRollNo));
				filePath = getNSTemplateForEvent(userCourseList);
			} else {

				filePath = getNSTemplate(sList);

			}
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info("Error writing file to output stream. Filename was '{}'", ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	public String getNSTemplate(List<SubjectSapEnroll> sList) {
		String filePath = downloadAllFolder + File.separator + "NSTemplate.xlsx";
		String h[] = { "Roll No", "Student Name", "SAPID", "N or S" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Student Report Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;

		for (SubjectSapEnroll s : sList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(s.getRollno());
			row.createCell(1).setCellValue(s.getStudentName());
			row.createCell(2).setCellValue(s.getStudentNumber());
			row.createCell(3).setCellValue("S");
			rowNum++;
		}
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		String[] actions = new String[] { "N, S" };
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(actions);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 3, 3);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;

	}

	public String getNSTemplateForEvent(List<UserCourse> sList) {
		String filePath = downloadAllFolder + File.separator + "NSTemplate.xlsx";
		String h[] = { "Roll No", "Student Name", "SAPID", "N or S" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Student Report Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;

		for (UserCourse s : sList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(s.getRollNo());
			row.createCell(1).setCellValue(s.getStudentName());
			row.createCell(2).setCellValue(s.getUsername());
			row.createCell(3).setCellValue("S");
			rowNum++;
		}
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		String[] actions = new String[] { "N, S" };
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(actions);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 3, 3);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/uploadStudentNS", method = { RequestMethod.POST })
	public String uploadStudentNS(@ModelAttribute StudentNcIca studentNcIca, @RequestParam("file") MultipartFile file,
			@RequestParam String saveAs, @RequestParam String icaId, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {

		redirectAttributes.addAttribute("ncIcaId", icaId);

		try {
			logger.info("ICAId-->" + icaId);
			NsBean icaBeanDB = nsService.findByID(Long.valueOf(icaId));
			List<String> headers = new ArrayList<String>(Arrays.asList("Roll No", "Student Name", "SAPID", "N or S"));

			Map<String, IcaBean> icaBeanMap = new HashMap<>();

			ExcelReader excelReader = new ExcelReader();

			List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);
			logger.info("valid headers" + headers);
			logger.info("maps" + maps);
			List<String> excelUserList = new ArrayList<>();
			// ---------------------------------------------//
			List<Map<String, Object>> copy = new ArrayList<>();
			List<String> ucListForModule = subjectSapEnrollService.getDistinctNSStudentList(icaBeanDB.getModuleId(),
					icaBeanDB.getAcadYear(), icaBeanDB.getProgramId(), icaBeanDB.getAcadSession(),
					icaBeanDB.getCampusId());
			logger.info("ucListSize-->" + ucListForModule.size());
			if (ucListForModule.size() == 0) {
				List<String> ucListForModuleEvent = userCourseService.findStudentNoByModuleIdAndAcadYearForNCAEvents(
						icaBeanDB.getModuleId(), icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(),
						icaBeanDB.getProgramId(), icaBeanDB.getCampusId());

				ucListForModule.addAll(ucListForModuleEvent);
			}
			copy = maps.stream().filter(s -> {

				String user = (String) s.get("SAPID");
				if (ucListForModule.contains(user)) {
					if (!excelUserList.contains(user)) {
						excelUserList.add(user);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			}).collect(Collectors.toList());

			if (ucListForModule.size() > copy.size() || maps.size() > ucListForModule.size()) {

				logger.info("error---->Module");
				setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");
				return "redirect:/uploadNS";
			}
			/*
			 * if(maps.size()>ucListForModule.size()){ logger.info("error---->Module");
			 * setError(redirectAttributes,
			 * "You have tampered the SAP IDs given in the template!"); return
			 * "redirect:/uploadNS"; }
			 */
			logger.info("maps received after deletion" + copy);

			// ---------------------------------------------//

			List<StudentNcIca> studentNcIcaList = new ArrayList<>();
			for (Map<String, Object> mapper : copy) {
				if (mapper.get("Error") != null) {
					setError(redirectAttributes, (String) mapper.get("Error"));
					return "redirect:/uploadNS";
				}
				StudentNcIca snc = new StudentNcIca();

				snc.setIcaId(icaId);
				snc.setUsername((String) mapper.get("SAPID"));
				snc.setGrade((String) mapper.get("N or S"));

				if ("draft".equals(saveAs)) {
					snc.setSaveAsDraft("Y");
				} else {
					snc.setFinalSubmit("Y");
				}

				snc.setActive("Y");
				snc.setCreatedBy(principal.getName());
				snc.setLastModifiedBy(principal.getName());
				studentNcIcaList.add(snc);

			}
			logger.info("studentNcIcaList" + studentNcIcaList);
			studentNcIcaService.upsertBatch(studentNcIcaList);
			if ("submit".equals(saveAs)) {
				studentNcIcaService.insertWithIdReturn(studentNcIca);
			}
			setSuccess(redirectAttributes, "File saved successfully");
			icaBeanDB.setIsSubmitted("Y");
			icaBeanDB.setLastModifiedBy(principal.getName());

			nsService.update(icaBeanDB);

		} catch (Exception ex) {

			setError(redirectAttributes, "Error in uploading");

			logger.error("Exception", ex);
		}
		return "redirect:/uploadNS";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/searchNsList", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchNsList(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Search NS", true, false));
		List<NsBean> nsList = nsService.findNsListByProgram(principal.getName());

		logger.info("nsList-->" + nsList);
		for (NsBean ns : nsList) {

			if (ns.getProgramId().contains(",")) {
				ns.setProgramName(programService.getProgramNamesForIca(ns.getProgramId()));
			}
		}

		m.addAttribute("Program_Name", userdetails1.getProgramName());
		m.addAttribute("nsList", nsList);
		return "ica/nsList";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/publishOneNCIca", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String publishOneNCIca(Model m, Principal principal, @RequestParam String id) {

		try {
			String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			nsService.updateNCIcaToPublished(id, Utils.getInIST());
			return "success";
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "error";
		}

	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/deleteNs", method = RequestMethod.GET)
	public String deleteNs(Model m, Principal principal, @ModelAttribute NsBean nsBean,
			RedirectAttributes redirectAttrs) {

		try {

			NsBean nsBeanDAO = nsService.findByID(nsBean.getId());
			nsService.deleteSoftById(String.valueOf(nsBean.getId()));
			setSuccess(redirectAttrs, "NS Delete Successfully");

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting NS");
			logger.error("Exception", ex);
		}

		return "redirect:/searchNsList";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/showNCMarks", method = { RequestMethod.GET, RequestMethod.POST })
	public String showNCMarks(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		List<NsBean> ncGradeForStudent = nsService.getNcGradedByUser(username);

		List<Long> iceIds = ncGradeForStudent.stream().map(map -> map.getId()).collect(Collectors.toList());

		m.addAttribute("ncGrade", ncGradeForStudent);

		m.addAttribute("user", userService.findByUserName(username));

		return "ica/showNCMarks";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadUpdatedGrade", method = RequestMethod.GET)
	public void downloadUpdatedGrade(@ModelAttribute StudentNcIca sni, HttpServletResponse response,
			Principal principal, @RequestParam("icaId") String icaId) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		InputStream is = null;
		String filePath = "";

		try {

			List<StudentNcIca> nList = new ArrayList<StudentNcIca>();

			nList = studentNcIcaService.getUpdatedGrade(icaId);
			filePath = getStudentGrades(nList);
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info("Error writing file to output stream. Filename was '{}'", ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	public String getStudentGrades(List<StudentNcIca> sList) {
		String filePath = downloadAllFolder + File.separator + "NSTemplate.xlsx";
		String h[] = { "Roll No", "Student Name", "SAPID", "N or S" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Student Report Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;

		for (StudentNcIca s : sList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(s.getRollno());
			row.createCell(1).setCellValue(s.getStudentName());
			row.createCell(2).setCellValue(s.getUsername());
			row.createCell(3).setCellValue(s.getGrade());
			rowNum++;
		}
		XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
		String[] actions = new String[] { "N, S" };
		XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
				.createExplicitListConstraint(actions);
		CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 3, 3);
		XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);
		validation.setShowErrorBox(true);
		sheet.addValidationData(validation);
		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;

	}

	// end of NS

	// subject-wise report with bifurcation

	public String getFilePathOfSubjectAndComponentWiseReport(String programId, String acadYear, String acadSession,
			String campusId, String collegeName, String splittedProgramIds) {
		// List<String> headers = new ArrayList();
		List<IcaComponent> icaComponentsByParam = new ArrayList<IcaComponent>();

		if (acadSession.contains(",")) {
//			logger.info(acadSession + "comma contains");
//			icaComponentsByParam = icaComponentService.getComponentsByParamMultiSession(acadYear, acadSession,
//					programId, campusId);
			// 26-04-2021
			if (programId.contains(",")) {
				List<String> programidsList = Arrays.asList(programId.split(","));
				for (String str : programidsList) {
					List<IcaComponent> icaComponentsByParamTemp = icaComponentService
							.getComponentsByParamMultiSessionCoursera(acadYear, acadSession, str, campusId);
					icaComponentsByParam.addAll(icaComponentsByParamTemp);
				}
			} else {
				icaComponentsByParam = icaComponentService.getComponentsByParamMultiSessionCoursera(acadYear,
						acadSession, programId, campusId);
			}
		} else {
			icaComponentsByParam = icaComponentService.getComponentsByParam(acadYear, acadSession, programId, campusId);
		}

		List<IcaComponentMarks> icaComponentMarks = new ArrayList<IcaComponentMarks>();
		if (acadSession.contains(",")) {
//			logger.info(acadSession + "comma contains2");
//			icaComponentMarks = icaComponentMarksService.getIcaComponentMarksByParamMultiSession(acadYear, acadSession,
//					programId, campusId);
			// 26-04-2021
			if (programId.contains(",")) {
				List<String> programidsList = Arrays.asList(programId.split(","));
				for (String str : programidsList) {
					List<IcaComponentMarks> icaComponentMarksTemp = icaComponentMarksService
							.getIcaComponentMarksByParamMultiSessionCoursera(acadYear, acadSession, str, campusId);
					icaComponentMarks.addAll(icaComponentMarksTemp);
				}
			} else {
				icaComponentMarks = icaComponentMarksService.getIcaComponentMarksByParamMultiSessionCoursera(acadYear,
						acadSession, programId, campusId);
			}
		} else {
			icaComponentMarks = icaComponentMarksService.getIcaComponentMarksByParam(acadYear, acadSession, programId,
					campusId);
		}
		List<String> moduleIds = icaComponentsByParam.stream().map(map -> map.getModuleId())
				.collect(Collectors.toList());

		Map<String, List<IcaComponent>> icaComponentListMap = new HashMap<>();
		Map<String, List<IcaComponentMarks>> icaComponentMarksListMap = new HashMap<>();
		for (String m : moduleIds) {

			List<IcaComponent> icmList = icaComponentsByParam.stream().filter(o -> o.getModuleId().equals(m))
					.collect(Collectors.toList());

			List<IcaComponentMarks> icmarksList = icaComponentMarks.stream().filter(o -> o.getModuleId().equals(m))
					.collect(Collectors.toList());
			icaComponentListMap.put(m, icmList);
			icaComponentMarksListMap.put(m, icmarksList);

		}

		// headers = Arrays.asList(h);

		String fileName = "ICA-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "ICA-Report" + programId
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFCellStyle totalStyle = workbook.createCellStyle();

		XSSFCellStyle totalRedStyle = workbook.createCellStyle();

		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(true);
		defaultFont.setItalic(false);
		totalStyle.setFont(defaultFont);
		totalRedStyle.setFont(defaultFont);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		// style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFont(defaultFont);
		headerStyle.setWrapText(true);

		XSSFColor grayColor = new XSSFColor(new Color(211, 211, 211));
		totalStyle.setFillBackgroundColor(grayColor);

		XSSFColor redColor = new XSSFColor(new Color(255, 0, 0));
		totalRedStyle.setFillBackgroundColor(redColor);

		XSSFCellStyle borderStyle = workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 0));
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalStyle.setFillPattern(FillPatternType.LESS_DOTS);

		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalRedStyle.setFillPattern(FillPatternType.LESS_DOTS);

		style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		/*
		 * if (programId.contains(",")) {
		 * 
		 * List<String> programIdList = Arrays.asList(programId.split(",")); int
		 * count=0; for (String p : programIdList) { count++; String programName =
		 * programService.findByID( Long.valueOf(p)).getProgramName(); XSSFSheet sheet =
		 * workbook.createSheet(programName+"-"+count);
		 * logger.info("count--->"+workbook.getSheetName(0));
		 * logger.info("count--->"+workbook.getSheetName(1));; } }
		 */

//		List<IcaTotalMarks> getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParam(acadYear,
//				acadSession, programId, campusId);
		// 26-04-2021
		List<IcaTotalMarks> getIcaTotalMarksByParam = new ArrayList<IcaTotalMarks>();
		if (acadSession.contains(",")) {
			if (programId.contains(",")) {
				List<String> programidsList = Arrays.asList(programId.split(","));
				for (String str : programidsList) {
					List<IcaTotalMarks> itmByParamNew = icaTotalMarksService.getIcaTotalMarksByParamCoursera(acadYear,
							acadSession, str, campusId);
					getIcaTotalMarksByParam.addAll(itmByParamNew);
				}
			} else {
				getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParamCoursera(acadYear, acadSession,
						programId, campusId);
			}
		} else {
			getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParam(acadYear, acadSession, programId,
					campusId);
		}

		List<IcaTotalMarks> getIcaTotalMarksByParamNonEvent = icaTotalMarksService
				.getIcaTotalMarksByParamForNonEvent(acadYear, acadSession, programId, campusId);

		getIcaTotalMarksByParam.addAll(getIcaTotalMarksByParamNonEvent);

		Map<String, Course> courseMapper = new HashMap<>();
		Map<String, List<IcaTotalMarks>> icaTotalMarksModuleWise = new HashMap<>();
		for (IcaTotalMarks itm : getIcaTotalMarksByParam) {

			if (!courseMapper.containsKey(itm.getModuleId())) {
				Course c = new Course();
				c.setModuleId(itm.getModuleId());
				c.setModuleName(itm.getModuleName());
				c.setAcadYear(itm.getAcadYear());
				c.setAcadSession(itm.getAcadSession());
				c.setModuleAbbr(itm.getModuleAbbr());

				courseMapper.put(itm.getModuleId(), c);
			}
		}
		for (String moduleId : courseMapper.keySet()) {

			List<IcaTotalMarks> itmList = new ArrayList<>();

			if (!splittedProgramIds.contains(",")) {
				itmList = getIcaTotalMarksByParam.stream()

						.filter(o -> o.getModuleId().equals(moduleId)).collect(Collectors.toList());

				itmList = itmList.stream()

						.filter(o -> o.getProgramId().equals(programId)).collect(Collectors.toList());

			} else {

				itmList = getIcaTotalMarksByParam.stream()

						.filter(o -> o.getModuleId().equals(moduleId))

						.collect(Collectors.toList());

				itmList = itmList.stream()

						.filter(o -> Arrays.asList(splittedProgramIds.split(",")).contains(o.getProgramId()))
						.collect(Collectors.toList());

			}

			icaTotalMarksModuleWise.put(moduleId, itmList);

			/*
			 * List<IcaTotalMarks> itmList = getIcaTotalMarksByParam.stream() .filter(o ->
			 * o.getModuleId().equals(moduleId)) .collect(Collectors.toList());
			 * icaTotalMarksModuleWise.put(moduleId, itmList);
			 */
		}

		for (String moduleId : courseMapper.keySet()) {

			// Title Of Reports

			String acdYear = courseMapper.get(moduleId).getAcadYear();
			String session = courseMapper.get(moduleId).getAcadSession();

			String faculties = userCourseService.getFacultiesByParam(acdYear, session, moduleId, programId);

			String h[] = { "SAPID", "Name", "Roll.No" };

			List<String> headers = new ArrayList<String>(Arrays.asList(h));

			List<IcaComponent> getIcaComponentsByModule = icaComponentListMap.get(moduleId);

			if (!icaComponentMarksListMap.containsKey(moduleId)) {
				continue;
			}
			List<IcaComponentMarks> getIcaComponentMarksByModule = icaComponentMarksListMap.get(moduleId);

			List<IcaComponentMarks> icaComponentMarksTemp = getIcaComponentMarksByModule.stream()
					.filter(Utils.distinctByKeys(IcaComponentMarks::getUsername, IcaComponentMarks::getComponentId))
					.collect(Collectors.toList());
			getIcaComponentMarksByModule.clear();
			getIcaComponentMarksByModule.addAll(icaComponentMarksTemp);

			List<String> studentsSapIds = getIcaComponentMarksByModule.stream().map(map -> map.getUsername())
					.collect(Collectors.toList());

			/*
			 * List<String> icaIds = getIcaComponentMarksByModule.stream() .map(map ->
			 * map.getIcaId()).collect(Collectors.toList());
			 */
			List<String> icaIds = new ArrayList<>();
			for (IcaComponentMarks s : getIcaComponentMarksByModule) {
				if (!icaIds.contains(s.getIcaId())) {
					icaIds.add(s.getIcaId());
				}
			}

			Map<String, Map<String, String>> mapOfStudentComponentWiseMarks = new HashMap<>();

			logger.info("getIcaComponents-->" + getIcaComponentsByModule.size());
			logger.info("icaIds list--->" + icaIds);
			for (IcaComponent ic : getIcaComponentsByModule) {
				logger.info("component name is" + ic.getComponentName() + "(" + ic.getMarks() + ")");

				if (!headers.contains(ic.getComponentName() + "(" + ic.getMarks() + ")")) {

					if (icaIds.size() > 1) {
						logger.info("multiple ica Ids");
						if (!icaIds.contains(ic.getIcaId())) {
							headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
						}
					} else {
						logger.info("single ica Id");
						headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
					}

				}

			}

			headers.add("Obtained Marks");
			headers.add("Total ICA Marks");
			headers.add("Status");
			headers.add("Remarks");

			// headers = Arrays.asList(h);

			for (String s : studentsSapIds) {

				Map<String, String> componentsMapIca = getIcaComponentMarksByModule.stream()
						.filter(o -> o.getUsername().equals(s))
						.collect(Collectors.toMap(IcaComponentMarks::getComponentId, IcaComponentMarks::getMarks));

				/*
				 * Map<String, String> componentsMapIca = componentMarksForStuent.stream()
				 * .collect( Collectors.toMap(IcaComponentMarks::getComponentId,
				 * IcaComponentMarks::getComponentName));
				 */
				mapOfStudentComponentWiseMarks.put(s, componentsMapIca);

			}

			String programName = programService.findByID(Long.valueOf(programId)).getProgramName();
			int rowNum = 0;
			logger.info("program name" + programName);
			XSSFSheet sheet = workbook.createSheet(courseMapper.get(moduleId).getModuleAbbr());
			// XSSFSheet sheet = workbook.getSheet(programName);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));

			Row collegeNameRow = sheet.createRow(rowNum++);
			Row programRow = sheet.createRow(rowNum++);
			Row reviewRow = sheet.createRow(rowNum++);
			Row moduleNameAbbrRow = sheet.createRow(rowNum++);

			Row facultiesRow = sheet.createRow(rowNum++);

			Cell collegeCell = collegeNameRow.createCell(0);
			if (campusId == null) {
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			} else {
				collegeName = programService.getCollgeName(programId, campusId);
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			}
			// String year = acadYear;
			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(programName + "-" + acadSession + "-" + batch);
			Cell subjNameAbbrCell = moduleNameAbbrRow.createCell(0);
			Cell facultiesCell = facultiesRow.createCell(0);
			Cell reviewCell = reviewRow.createCell(0);

			/*
			 * LocalDateTime now = LocalDateTime.now();
			 * 
			 * DateTimeFormatter formatter = DateTimeFormatter
			 * .ofPattern("yyyy-MM-dd HH:mm:ss");
			 */

			// String formatDateTime = now.format(formatter);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			/* reviewCell.setCellValue("Mid Term Review report "); */
			String subjNameVal = courseMapper.get(moduleId).getModuleName() + "("
					+ courseMapper.get(moduleId).getModuleAbbr() + ")";
			subjNameAbbrCell.setCellValue(subjNameVal);
			reviewCell.setCellValue("Student review ICA Marks");
			facultiesCell.setCellValue(faculties);
			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);
			subjNameAbbrCell.setCellStyle(centerStyle);
			facultiesCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(7).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			/*
			 * border.setBorderBottom(BorderStyle.MEDIUM);
			 * border.setBorderLeft(BorderStyle.MEDIUM);
			 * border.setBorderRight(BorderStyle.MEDIUM);
			 * border.setBorderTop(BorderStyle.MEDIUM);
			 */
			// dateRow.getCell(8).setCellStyle(border);
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			for (IcaTotalMarks itm : icaTotalMarksModuleWise.get(moduleId)) {
				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(itm.getUsername());

				rowd.createCell(colNum++).setCellValue(itm.getStudentName());
				rowd.createCell(colNum++).setCellValue(itm.getRollNo());

				// add component-wise marks

				List<String> componentIds = new ArrayList<>();
				for (IcaComponent ic : getIcaComponentsByModule) {
					if (!mapOfStudentComponentWiseMarks.containsKey(itm.getUsername()))
						continue;
					Map<String, String> compMarks = mapOfStudentComponentWiseMarks.get(itm.getUsername());

					if (!componentIds.contains(ic.getComponentId())) {

						if (compMarks.get(ic.getComponentId()) != null) {
							rowd.createCell(colNum++).setCellValue(compMarks.get(ic.getComponentId()));
							componentIds.add(ic.getComponentId());
						} else {
							rowd.createCell(colNum++).setCellValue("");
							componentIds.add(ic.getComponentId());
						}
					} /*
						 * else{
						 * 
						 * }
						 */
				}

				rowd.createCell(colNum++).setCellValue(itm.getIcaTotalMarks());
				rowd.createCell(colNum++).setCellValue(itm.getInternalMarks());

				rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
				rowd.createCell(colNum++).setCellValue(itm.getRemarks());

			}
			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
			// log.info("Width Col ------- " + sheet.getColumnWidth(2));
			sheet.setColumnWidth(2, 5500);
			sheet.setColumnWidth(3, 6500);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(8);

			sheet.createFreezePane(0, 7);
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			// FileUtils.deleteQuietly(new File(filePath));
			workbook.close();
		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return filePath;
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/AddIcaComponentNew", method = { RequestMethod.GET, RequestMethod.POST })
	public String ad(Principal principal, Model m) {
		m.addAttribute("webPage",
				new WebPage("createTrainingProgram", "Add new Librarian", true, true, true, true, false));
		// m.addAttribute("AddIcaComponent", new TrainingProgram());
		return "homepage/AddIcaComponentNew";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/createComponentForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String createTraingProgramForm(Principal principal, Model m,
			@RequestParam(value = "componentName") String ComponentName, RedirectAttributes redirectAttrs)
			throws IOException {

		logger.info("IcaBean--->" + ComponentName);

		PredefinedIcaComponent preBean = new PredefinedIcaComponent();

		String comName = ComponentName.trim();
		String lowercase = ComponentName.toLowerCase();
		String uppercase = ComponentName.toUpperCase();
		String firstCap = Character.toUpperCase(ComponentName.charAt(0)) + ComponentName.substring(1);
		logger.info("firstCap------------------>" + firstCap);
		logger.info("comName-------->" + comName);
		// listBean.add(icabean);
		m.addAttribute("ComponentName", comName);

		PredefinedIcaComponent compdata = predefinedIcaComponentService.findByComponentName(comName);

		logger.info("List Of Data-------->" + compdata);

		// String dbdata = list.getComponentName();

		// logger.info("dbdata --------------------> "+dbdata);

		/*
		 * preBean.setComponentName(comName); logger.info("Get Component Name ------> "
		 * +preBean.getComponentName()); predefinedIcaComponentService.insert(preBean);
		 * setSuccess(redirectAttrs, "Component added successfully");
		 */

		if (compdata == null) {
			preBean.setComponentName(comName);
			logger.info("Get Component Name ------> " + preBean.getComponentName());
			predefinedIcaComponentService.insert(preBean);
			setSuccess(redirectAttrs, "Component added successfully");
		} else {
			setError(redirectAttrs, "Component Already Exist");
			logger.info("ERRORRR------>" + comName);
		}

		/*
		 * if(compdata.getComponentName().equals(comName) ||
		 * compdata.getComponentName().equals(lowercase) ||
		 * compdata.getComponentName().equals(uppercase)||
		 * compdata.getComponentName().equals(firstCap)) { setError(redirectAttrs,
		 * "Component Already Exist"); logger.info("ERRORRR------>"+comName); } else {
		 * preBean.setComponentName(comName); logger.info("Get Component Name ------> "
		 * +preBean.getComponentName()); predefinedIcaComponentService.insert(preBean);
		 * setSuccess(redirectAttrs, "Component added successfully"); }
		 */

		// return "homepage/uploadTimeLimitSession";
		return "redirect:/AddIcaComponentNew";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/icaListBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String icaListBySupportAdmin(Model m, Principal principal, @RequestParam(required = false) String icaId) {

		List<IcaBean> icaList = new ArrayList<>();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Search ICA", true, false));

		// 06-04-2021
		if ((userdetails1.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)
				|| userdetails1.getAuthorities().contains(Role.ROLE_EXAM)
				|| userdetails1.getAuthorities().contains(Role.ROLE_ADMIN))) {

			if (icaId != null) {
				icaList = icaBeanService.findDivisionWiseIcaListByParentIcaForSupportAdmin(icaId);
				m.addAttribute("icaId", icaId);

			} else {

				// role = Role.ROLE_SUPPORT_ADMIN.name();
				icaList = icaBeanService.findIcaListByProgramForSupportAdmin();

				// add Non-Event Module ica list

				List<IcaBean> nonEventModuleIcaList = icaBeanService
						.findIcaListByProgramForNonEventModuleForSupportAdmin();
				if (nonEventModuleIcaList.size() > 0) {
					for (IcaBean ica : nonEventModuleIcaList) {
						if (!icaList.stream()
								.filter(o -> o.getModuleId().equals(ica.getModuleId())
										&& o.getProgramId().equals(ica.getProgramId())
										&& o.getAcadYear().equals(ica.getAcadYear())
										&& o.getCreatedBy().equals(ica.getCreatedBy()))
								.findFirst().isPresent()) {
							icaList.add(ica);
						}
					}
				}

			}

			for (IcaBean ica : icaList) {

				if (ica.getProgramId().contains(",")) {
					ica.setProgramName(programService.getProgramNamesForIca(ica.getProgramId()));
				}
			}
//			// New Changes on 09-04-2021 to check tcs flag
//			for (IcaBean ica : icaList) {
//
//				boolean checkTcsFlagForIca = isIcaMarksSentToTcs(ica);
//				if (checkTcsFlagForIca == false) {
//					ica.setFlagTcs("F");
//				} else {
//					ica.setFlagTcs("S");
//				}
//			}
//			//
			// New Changes to check tcs flag
			List<IcaBean> icaFinalList = new ArrayList<>();
			for (IcaBean ica : icaList) {
				boolean checkTcsFlagForIca = isIcaMarksSentToTcs(ica);
				if (checkTcsFlagForIca == false) {
					icaFinalList.add(ica);
				}
			}

			icaList.clear();
			icaList.addAll(icaFinalList);
			//
		}
		m.addAttribute("Program_Name", userdetails1.getProgramName());
		m.addAttribute("icaList", icaList);

		return "ica/icaListSupportAdmin";

	}

	@Secured({ "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/updateIcaDateBySupportAdminForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateIcaDateBySupportAdminForm(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs, @RequestParam String icaId) {

		icaBean.setActive("Y");
		icaBean.setLastModifiedBy(principal.getName());
		m.addAttribute("icaId", icaId);
		m.addAttribute("icaBeanS", new IcaBean());

		return "ica/updateDateBySupportAdmin";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/updateIcaDateBySupportAdmin", method = RequestMethod.POST)
	public String updateIcaDateBySupportAdmin(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());

		logger.info("IcaId" + icaBean.getId());
		logger.info("endDate--->" + icaBean.getEndDate());
		logger.info("pubDate---->" + icaBean.getPublishedDate());

		icaBeanDAO.setEndDate(icaBean.getEndDate());
		icaBeanDAO.setLastModifiedBy(username);
		icaBeanDAO.setPublishedDate(icaBean.getPublishedDate());

		List<Long> icaIds = new ArrayList<>();
		try {
			if ((null == icaBean.getEndDate() || icaBean.getEndDate().equals(""))
					&& (null == icaBean.getPublishedDate() || icaBean.getPublishedDate().equals(""))) {
				redirectAttrs.addAttribute("icaId", icaBean.getId());
				setError(redirectAttrs, "Select EndDate or PublishDate");
				return "redirect:/updateIcaDateBySupportAdminForm";
			} else {
				icaBeanService.updateIcaDate(icaBeanDAO.getEndDate(), icaBeanDAO.getPublishedDate(),
						icaBeanDAO.getId());
				logger.info("iiiiiiooo");

				if ("Y".equals(icaBeanDAO.getIsIcaDivisionWise()) && icaBeanDAO.getParentIcaId() == null) {
					List<IcaBean> updatedIcaBeanList = new ArrayList<>();
					List<IcaBean> icaListByParentIcaId = icaBeanService.getIcaIdsByParentIcaIds(icaBean.getId());

					// icaBeanService.update(icaBean);

					for (IcaBean ib : icaListByParentIcaId) {

						String ibParentId = ib.getParentIcaId();

						String endDate = icaBean.getEndDate();
						String publishedDate = icaBean.getPublishedDate();

						logger.info("ibParentId========>" + ibParentId);

						logger.info("endDate========>" + endDate);
						logger.info("publishedDate========>" + publishedDate);

						icaIds.add(ib.getId());
					}
					logger.info("icaIdsList" + icaIds);
					icaBeanService.updateIcaDateForDivision(icaBean.getEndDate(), icaBean.getPublishedDate(), icaIds);

					setSuccess(redirectAttrs, "ICA Updated Successfully");

				}

				setSuccess(redirectAttrs, "Ica Date Updated Successfully");
				return "redirect:/icaListBySupportAdmin";
			}
		} catch (Exception ex) {
			setError(redirectAttrs, "Error While Updating Date");
			return "redirect:/icaListBySupportAdmin";

		}

	}

	@Secured({ "ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/updateIcaDateBySupportAdminWithoutSubmit", method = RequestMethod.POST)
	public String updateIcaDateBySupportAdminWithoutSubmit(Model m, Principal principal,
			@ModelAttribute("icaBeanS") IcaBean icaBeanS, RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		IcaBean icaBeanDAOS = icaBeanService.findByID(icaBeanS.getId());
		icaBeanDAOS.setEndDate(icaBeanS.getEndDate());
		icaBeanDAOS.setLastModifiedBy(username);
		icaBeanDAOS.setPublishedDate(icaBeanS.getPublishedDate());

		List<Long> icaIds = new ArrayList<>();
		try {
			if ((null == icaBeanS.getEndDate() || icaBeanS.getEndDate().equals(""))
					&& (null == icaBeanS.getPublishedDate() || icaBeanS.getPublishedDate().equals(""))) {
				redirectAttrs.addAttribute("icaId", icaBeanS.getId());
				setError(redirectAttrs, "Select EndDate or PublishDate");
				return "redirect:/updateIcaDateBySupportAdminForm";
			} else {
				icaBeanService.updateIcaDateWithoutSubmit(icaBeanDAOS.getEndDate(), icaBeanDAOS.getPublishedDate(),
						icaBeanDAOS.getId());

				if ("Y".equals(icaBeanDAOS.getIsIcaDivisionWise()) && icaBeanDAOS.getParentIcaId() == null) {

					logger.info("------------New MApping------->");
					List<IcaBean> updatedIcaBeanList = new ArrayList<>();
					List<IcaBean> icaListByParentIcaId1 = icaBeanService.getIcaIdsByParentIcaIds(icaBeanS.getId());

					for (IcaBean ib : icaListByParentIcaId1) {
						String ibParentId = ib.getParentIcaId();

						String endDate = icaBeanS.getEndDate();
						String publishedDate = icaBeanS.getPublishedDate();
						icaIds.add(ib.getId());
					}
					logger.info("icaIdsList" + icaIds);
					icaBeanService.updateIcaDateForDivisionWithoutSubmit(icaBeanS.getEndDate(),
							icaBeanS.getPublishedDate(), icaIds);

					setSuccess(redirectAttrs, "ICA Updated Successfully");

				}

				setSuccess(redirectAttrs, "Ica Date Updated Successfully");
				return "redirect:/icaListBySupportAdmin";
			}
		} catch (Exception ex) {
			setError(redirectAttrs, "Error While Updating Date");
			return "redirect:/icaListBySupportAdmin";

		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/downloadIcaReportFacultyForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadIcaReportFacultyForm(Model m, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<String> acadYearCodeList = courseService.findAcadYearCode();
		logger.info("acadYearCodeList------------>" + acadYearCodeList);
		m.addAttribute("acadYearCodeList", acadYearCodeList);

		return "ica/icaReportFaculty";
	}

	@RequestMapping(value = "/downloadIcaFacultyReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadIcaFacultyReport(Model m, HttpServletResponse response, Principal principal,
			@RequestParam String acadYear, @RequestParam String reportType, RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<IcaTotalMarks> data = icaTotalMarksService.getIcaTotalMarksByParamForFaculty(acadYear, username);

		logger.info("main data" + data);
		InputStream is = null;
		String filePath = "";

		try {

			ServletOutputStream out = response.getOutputStream();
			logger.info("in____>" + reportType);
			if ("subjWise".equals(reportType) || "subjWiseComp".equals(reportType)) {

				if (data.size() == 0) {
					setError(redirectAttributes, "No Data Found");
					return "redirect:/downloadIcaReportFacultyForm";
				} else {
					filePath = getFilePathOfSubjectAndComponentWiseReportForFaculty(acadYear,
							userdetails1.getCollegeName(), principal);
				}

			}
			// is = new FileInputStream(filePath);
			String filename = "Student review ICA Marks MID Term-End Term.xlsx";
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			// copy it to response's OutputStream
			is = new FileInputStream(filePath);
			logger.info("---->is" + is);
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			response.getOutputStream().close();
			response.getOutputStream().flush();

		} catch (Exception ex) {
			logger.info("Error writing file to output stream. Filename was '{}'", ex);
			throw new RuntimeException("IOError writing file to output stream" + ex);
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}
		}

		return "ica/icaReportFaculty";

	}

	public String getFilePathOfSubjectAndComponentWiseReportForFaculty(String acadYear, String collegeName,
			Principal principal) {

		// List<String> headers = new ArrayList();

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<IcaComponent> icaComponentsByParam = icaComponentService.getComponentsByParamFaculty(acadYear, username);

		logger.info(" Data" + icaComponentsByParam);

		List<IcaComponentMarks> icaComponentMarks = icaComponentMarksService
				.getIcaComponentMarksByParamFaculty(acadYear, username);

		List<String> moduleIds = icaComponentsByParam.stream().map(map -> map.getModuleId())
				.collect(Collectors.toList());

		Map<String, List<IcaComponent>> icaComponentListMap = new HashMap<>();
		Map<String, List<IcaComponentMarks>> icaComponentMarksListMap = new HashMap<>();
		for (String m : moduleIds) {

			List<IcaComponent> icmList = icaComponentsByParam.stream().filter(o -> o.getModuleId().equals(m))
					.collect(Collectors.toList());

			List<IcaComponentMarks> icmarksList = icaComponentMarks.stream().filter(o -> o.getModuleId().equals(m))
					.collect(Collectors.toList());
			icaComponentListMap.put(m, icmList);
			icaComponentMarksListMap.put(m, icmarksList);

		}

		// headers = Arrays.asList(h);

		String fileName = "ICA-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "ICA-Report" + acadYear
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFCellStyle totalStyle = workbook.createCellStyle();

		XSSFCellStyle totalRedStyle = workbook.createCellStyle();

		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(true);
		defaultFont.setItalic(false);
		totalStyle.setFont(defaultFont);
		totalRedStyle.setFont(defaultFont);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		// style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFont(defaultFont);
		headerStyle.setWrapText(true);

		XSSFColor grayColor = new XSSFColor(new Color(211, 211, 211));
		totalStyle.setFillBackgroundColor(grayColor);

		XSSFColor redColor = new XSSFColor(new Color(255, 0, 0));
		totalRedStyle.setFillBackgroundColor(redColor);

		XSSFCellStyle borderStyle = workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 0));
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalStyle.setFillPattern(FillPatternType.LESS_DOTS);

		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalRedStyle.setFillPattern(FillPatternType.LESS_DOTS);

		style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		/*
		 * if (programId.contains(",")) {
		 * 
		 * List<String> programIdList = Arrays.asList(programId.split(",")); int
		 * count=0; for (String p : programIdList) { count++; String programName =
		 * programService.findByID( Long.valueOf(p)).getProgramName(); XSSFSheet sheet =
		 * workbook.createSheet(programName+"-"+count);
		 * logger.info("count--->"+workbook.getSheetName(0));
		 * logger.info("count--->"+workbook.getSheetName(1));; } }
		 */

		List<IcaTotalMarks> getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParamForFaculty(acadYear,
				username);

		/*
		 * List<IcaTotalMarks> getIcaTotalMarksByParamNonEvent = icaTotalMarksService
		 * .getIcaTotalMarksByParamForNonEvent(acadYear, acadSession, programId,
		 * campusId);
		 * 
		 * getIcaTotalMarksByParam.addAll(getIcaTotalMarksByParamNonEvent);
		 */

		Map<String, Course> courseMapper = new HashMap<>();
		Map<String, List<IcaTotalMarks>> icaTotalMarksModuleWise = new HashMap<>();
		for (IcaTotalMarks itm : getIcaTotalMarksByParam) {

			if (!courseMapper.containsKey(itm.getModuleId())) {
				Course c = new Course();
				c.setModuleId(itm.getModuleId());
				c.setModuleName(itm.getModuleName());
				c.setAcadYear(itm.getAcadYear());
				c.setAcadSession(itm.getAcadSession());
				c.setModuleAbbr(itm.getModuleAbbr());

				courseMapper.put(itm.getModuleId(), c);
			}
		}
		for (String moduleId : courseMapper.keySet()) {
			List<IcaTotalMarks> itmList = getIcaTotalMarksByParam.stream().filter(o -> o.getModuleId().equals(moduleId))
					.collect(Collectors.toList());
			icaTotalMarksModuleWise.put(moduleId, itmList);
		}

		for (String moduleId : courseMapper.keySet()) {

			// Title Of Reports

			String acdYear = courseMapper.get(moduleId).getAcadYear();
			String session = courseMapper.get(moduleId).getAcadSession();

			String faculties = userCourseService.getFacultiesByParamForReport(acdYear);

			String h[] = { "SAPID", "Name", "Roll.No" };

			List<String> headers = new ArrayList<String>(Arrays.asList(h));

			List<IcaComponent> getIcaComponentsByModule = icaComponentListMap.get(moduleId);
			List<IcaComponentMarks> getIcaComponentMarksByModule = icaComponentMarksListMap.get(moduleId);

			logger.info("--->CheckM" + moduleId);
			logger.info("--->Check" + icaComponentMarksListMap.get(moduleId));

			List<String> studentsSapIds = getIcaComponentMarksByModule.stream().map(map -> map.getUsername())
					.collect(Collectors.toList());

			/*
			 * List<String> icaIds = getIcaComponentMarksByModule.stream() .map(map ->
			 * map.getIcaId()).collect(Collectors.toList());
			 */
			List<String> icaIds = new ArrayList<>();
			for (IcaComponentMarks s : getIcaComponentMarksByModule) {
				if (!icaIds.contains(s.getIcaId())) {
					icaIds.add(s.getIcaId());
				}
			}

			Map<String, Map<String, String>> mapOfStudentComponentWiseMarks = new HashMap<>();

			logger.info("getIcaComponents-->" + getIcaComponentsByModule.size());
			logger.info("icaIds list--->" + icaIds);
			for (IcaComponent ic : getIcaComponentsByModule) {
				logger.info("component name is" + ic.getComponentName() + "(" + ic.getMarks() + ")");

				if (!headers.contains(ic.getComponentName() + "(" + ic.getMarks() + ")")) {

					if (icaIds.size() > 1) {
						logger.info("multiple ica Ids");
						if (!icaIds.contains(ic.getIcaId())) {
							headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
						}
					} else {
						logger.info("single ica Id");
						headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
					}

				}

			}

			headers.add("Obtained Marks");
			headers.add("Total ICA Marks");
			headers.add("Status");
			headers.add("Remarks");

			// headers = Arrays.asList(h);

			for (String s : studentsSapIds) {

				Map<String, String> componentsMapIca = getIcaComponentMarksByModule.stream()
						.filter(o -> o.getUsername().equals(s))
						.collect(Collectors.toMap(IcaComponentMarks::getComponentId, IcaComponentMarks::getMarks));

				/*
				 * Map<String, String> componentsMapIca = componentMarksForStuent.stream()
				 * .collect( Collectors.toMap(IcaComponentMarks::getComponentId,
				 * IcaComponentMarks::getComponentName));
				 */
				mapOfStudentComponentWiseMarks.put(s, componentsMapIca);

			}

			/*
			 * String programName = programService.findByID(
			 * Long.valueOf(programId)).getProgramName();
			 */
			int rowNum = 0;

			XSSFSheet sheet = workbook.createSheet(courseMapper.get(moduleId).getModuleAbbr());
			// XSSFSheet sheet = workbook.getSheet(programName);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));

			Row collegeNameRow = sheet.createRow(rowNum++);
			Row programRow = sheet.createRow(rowNum++);
			Row reviewRow = sheet.createRow(rowNum++);
			Row moduleNameAbbrRow = sheet.createRow(rowNum++);

			Row facultiesRow = sheet.createRow(rowNum++);

			Cell collegeCell = collegeNameRow.createCell(0);
			/*
			 * if (campusId == null) { if (collegeName != null) {
			 * collegeCell.setCellValue(collegeName); } else {
			 * collegeCell.setCellValue("NMIMS/SVKM"); } } else { collegeName =
			 * programService.getCollgeName(programId, campusId); if (collegeName != null) {
			 * collegeCell.setCellValue(collegeName); } else {
			 * collegeCell.setCellValue("NMIMS/SVKM"); } }
			 */
			// String year = acadYear;
			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(acadYear + "-" + batch);
			Cell subjNameAbbrCell = moduleNameAbbrRow.createCell(0);
			Cell facultiesCell = facultiesRow.createCell(0);
			Cell reviewCell = reviewRow.createCell(0);

			/*
			 * LocalDateTime now = LocalDateTime.now();
			 * 
			 * DateTimeFormatter formatter = DateTimeFormatter
			 * .ofPattern("yyyy-MM-dd HH:mm:ss");
			 */

			// String formatDateTime = now.format(formatter);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			/* reviewCell.setCellValue("Mid Term Review report "); */
			String subjNameVal = courseMapper.get(moduleId).getModuleName() + "("
					+ courseMapper.get(moduleId).getModuleAbbr() + ")";
			subjNameAbbrCell.setCellValue(subjNameVal);
			reviewCell.setCellValue("Student review ICA Marks");
			facultiesCell.setCellValue(faculties);
			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);
			subjNameAbbrCell.setCellStyle(centerStyle);
			facultiesCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(7).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			/*
			 * border.setBorderBottom(BorderStyle.MEDIUM);
			 * border.setBorderLeft(BorderStyle.MEDIUM);
			 * border.setBorderRight(BorderStyle.MEDIUM);
			 * border.setBorderTop(BorderStyle.MEDIUM);
			 */
			// dateRow.getCell(8).setCellStyle(border);
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			for (IcaTotalMarks itm : icaTotalMarksModuleWise.get(moduleId)) {
				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(itm.getUsername());

				rowd.createCell(colNum++).setCellValue(itm.getStudentName());
				rowd.createCell(colNum++).setCellValue(itm.getRollNo());

				// add component-wise marks

				List<String> componentIds = new ArrayList<>();
				for (IcaComponent ic : getIcaComponentsByModule) {

					Map<String, String> compMarks = mapOfStudentComponentWiseMarks.get(itm.getUsername());

					if (!componentIds.contains(ic.getComponentId())) {

						if (null != compMarks.get(ic.getComponentId())) {
							rowd.createCell(colNum++).setCellValue(compMarks.get(ic.getComponentId()));
							componentIds.add(ic.getComponentId());
						} else {
							rowd.createCell(colNum++).setCellValue("");
							componentIds.add(ic.getComponentId());
						}
					} /*
						 * else{
						 * 
						 * }
						 */
				}

				rowd.createCell(colNum++).setCellValue(itm.getIcaTotalMarks());
				rowd.createCell(colNum++).setCellValue(itm.getInternalMarks());

				rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
				rowd.createCell(colNum++).setCellValue(itm.getRemarks());

			}
			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
			// log.info("Width Col ------- " + sheet.getColumnWidth(2));
			sheet.setColumnWidth(2, 5500);
			sheet.setColumnWidth(3, 6500);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(8);

			sheet.createFreezePane(0, 7);
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			// FileUtils.deleteQuietly(new File(filePath));
			workbook.close();
		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return filePath;
	}

	public String getFilePathOfSubjectAndComponentWiseReportDraft(String programId, String acadYear, String acadSession,
			String campusId, String collegeName) {
		// List<String> headers = new ArrayList();

		List<IcaComponent> icaComponentsByParam = icaComponentService.getComponentsByParamDraft(acadYear, acadSession,
				programId, campusId);

		List<IcaComponentMarks> icaComponentMarks = icaComponentMarksService.getIcaComponentMarksByParamDraft(acadYear,
				acadSession, programId, campusId);
		List<String> moduleIds = icaComponentsByParam.stream().map(map -> map.getModuleId())
				.collect(Collectors.toList());

		Map<String, List<IcaComponent>> icaComponentListMap = new HashMap<>();
		Map<String, List<IcaComponentMarks>> icaComponentMarksListMap = new HashMap<>();
		for (String m : moduleIds) {

			List<IcaComponent> icmList = icaComponentsByParam.stream().filter(o -> o.getModuleId().equals(m))
					.collect(Collectors.toList());

			List<IcaComponentMarks> icmarksList = icaComponentMarks.stream().filter(o -> o.getModuleId().equals(m))
					.collect(Collectors.toList());
			logger.info("icmarksList---------------<><>" + icmarksList);
			icaComponentListMap.put(m, icmList);
			icaComponentMarksListMap.put(m, icmarksList);
			logger.info("icmarksList---------------<><>1" + icmarksList);
		}

		// headers = Arrays.asList(h);

		String fileName = "ICA-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "ICA-Report" + programId
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFCellStyle totalStyle = workbook.createCellStyle();

		XSSFCellStyle totalRedStyle = workbook.createCellStyle();

		XSSFFont defaultFont = workbook.createFont();
		defaultFont.setFontHeightInPoints((short) 10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(true);
		defaultFont.setItalic(false);
		totalStyle.setFont(defaultFont);
		totalRedStyle.setFont(defaultFont);

		XSSFCellStyle style = workbook.createCellStyle();

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		// style.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());

		XSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
		headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFont(defaultFont);
		headerStyle.setWrapText(true);

		XSSFColor grayColor = new XSSFColor(new Color(211, 211, 211));
		totalStyle.setFillBackgroundColor(grayColor);

		XSSFColor redColor = new XSSFColor(new Color(255, 0, 0));
		totalRedStyle.setFillBackgroundColor(redColor);

		XSSFCellStyle borderStyle = workbook.createCellStyle();
		XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 0));
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		borderStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalStyle.setFillPattern(FillPatternType.LESS_DOTS);

		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		totalRedStyle.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
		totalRedStyle.setFillPattern(FillPatternType.LESS_DOTS);

		style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
		style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);

		CellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

		/*
		 * if (programId.contains(",")) {
		 * 
		 * List<String> programIdList = Arrays.asList(programId.split(",")); int
		 * count=0; for (String p : programIdList) { count++; String programName =
		 * programService.findByID( Long.valueOf(p)).getProgramName(); XSSFSheet sheet =
		 * workbook.createSheet(programName+"-"+count);
		 * logger.info("count--->"+workbook.getSheetName(0));
		 * logger.info("count--->"+workbook.getSheetName(1));; } }
		 */

		List<IcaTotalMarks> getIcaTotalMarksByParam = icaTotalMarksService.getIcaTotalMarksByParamDraft(acadYear,
				acadSession, programId, campusId);

		List<IcaTotalMarks> getIcaTotalMarksByParamNonEvent = icaTotalMarksService
				.getIcaTotalMarksByParamForNonEventDraft(acadYear, acadSession, programId, campusId);

		getIcaTotalMarksByParam.addAll(getIcaTotalMarksByParamNonEvent);

		Map<String, Course> courseMapper = new HashMap<>();
		Map<String, List<IcaTotalMarks>> icaTotalMarksModuleWise = new HashMap<>();
		for (IcaTotalMarks itm : getIcaTotalMarksByParam) {

			if (!courseMapper.containsKey(itm.getModuleId())) {
				Course c = new Course();
				c.setModuleId(itm.getModuleId());
				c.setModuleName(itm.getModuleName());
				c.setAcadYear(itm.getAcadYear());
				c.setAcadSession(itm.getAcadSession());
				c.setModuleAbbr(itm.getModuleAbbr());

				courseMapper.put(itm.getModuleId(), c);
			}
		}
		for (String moduleId : courseMapper.keySet()) {
			List<IcaTotalMarks> itmList = getIcaTotalMarksByParam.stream().filter(o -> o.getModuleId().equals(moduleId))
					.collect(Collectors.toList());
			icaTotalMarksModuleWise.put(moduleId, itmList);
		}

		for (String moduleId : courseMapper.keySet()) {

			// Title Of Reports

			String acdYear = courseMapper.get(moduleId).getAcadYear();
			String session = courseMapper.get(moduleId).getAcadSession();

			String faculties = userCourseService.getFacultiesByParam(acdYear, session, moduleId, programId);

			String h[] = { "SAPID", "Name", "Roll.No" };

			List<String> headers = new ArrayList<String>(Arrays.asList(h));

			List<IcaComponent> getIcaComponentsByModule = icaComponentListMap.get(moduleId);
			List<IcaComponentMarks> getIcaComponentMarksByModule = icaComponentMarksListMap.get(moduleId);

			List<String> studentsSapIds = getIcaComponentMarksByModule.stream().map(map -> map.getUsername())
					.collect(Collectors.toList());

			List<String> icaIds = new ArrayList<>();
			for (IcaComponentMarks s : getIcaComponentMarksByModule) {
				if (!icaIds.contains(s.getIcaId())) {
					icaIds.add(s.getIcaId());
				}
			}

			Map<String, Map<String, String>> mapOfStudentComponentWiseMarks = new HashMap<>();

			logger.info("getIcaComponents-->" + getIcaComponentsByModule.size());
			logger.info("icaIds list--->" + icaIds);
			for (IcaComponent ic : getIcaComponentsByModule) {
				logger.info("component name is" + ic.getComponentName() + "(" + ic.getMarks() + ")");

				if (!headers.contains(ic.getComponentName() + "(" + ic.getMarks() + ")")) {

					if (icaIds.size() > 1) {
						logger.info("multiple ica Ids");
						if (!icaIds.contains(ic.getIcaId())) {
							headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
						}
					} else {
						logger.info("single ica Id");
						headers.add(ic.getComponentName() + "(" + ic.getMarks() + ")");
					}

				}

			}

			headers.add("Obtained Marks");
			headers.add("Total ICA Marks");
			headers.add("Status");
			headers.add("Remarks");

			// headers = Arrays.asList(h);

			for (String s : studentsSapIds) {

				Map<String, String> componentsMapIca = getIcaComponentMarksByModule.stream()
						.filter(o -> o.getUsername().equals(s))
						.collect(Collectors.toMap(IcaComponentMarks::getComponentId, IcaComponentMarks::getMarks));

				mapOfStudentComponentWiseMarks.put(s, componentsMapIca);

			}

			String programName = programService.findByID(Long.valueOf(programId)).getProgramName();
			int rowNum = 0;
			logger.info("program name" + programName);
			XSSFSheet sheet = workbook.createSheet(courseMapper.get(moduleId).getModuleAbbr());
			// XSSFSheet sheet = workbook.getSheet(programName);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 7));
			sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 7));

			Row collegeNameRow = sheet.createRow(rowNum++);
			Row programRow = sheet.createRow(rowNum++);
			Row reviewRow = sheet.createRow(rowNum++);
			Row moduleNameAbbrRow = sheet.createRow(rowNum++);

			Row facultiesRow = sheet.createRow(rowNum++);

			Cell collegeCell = collegeNameRow.createCell(0);
			if (campusId == null) {
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			} else {
				collegeName = programService.getCollgeName(programId, campusId);
				if (collegeName != null) {
					collegeCell.setCellValue(collegeName);
				} else {
					collegeCell.setCellValue("NMIMS/SVKM");
				}
			}
			// String year = acadYear;
			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(programName + "-" + acadSession + "-" + batch);
			Cell subjNameAbbrCell = moduleNameAbbrRow.createCell(0);
			Cell facultiesCell = facultiesRow.createCell(0);
			Cell reviewCell = reviewRow.createCell(0);

			/*
			 * LocalDateTime now = LocalDateTime.now();
			 * 
			 * DateTimeFormatter formatter = DateTimeFormatter
			 * .ofPattern("yyyy-MM-dd HH:mm:ss");
			 */

			// String formatDateTime = now.format(formatter);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			/* reviewCell.setCellValue("Mid Term Review report "); */
			String subjNameVal = courseMapper.get(moduleId).getModuleName() + "("
					+ courseMapper.get(moduleId).getModuleAbbr() + ")";
			subjNameAbbrCell.setCellValue(subjNameVal);
			reviewCell.setCellValue("Student review ICA Marks");
			facultiesCell.setCellValue(faculties);
			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);
			subjNameAbbrCell.setCellStyle(centerStyle);
			facultiesCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(7).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			/*
			 * border.setBorderBottom(BorderStyle.MEDIUM);
			 * border.setBorderLeft(BorderStyle.MEDIUM);
			 * border.setBorderRight(BorderStyle.MEDIUM);
			 * border.setBorderTop(BorderStyle.MEDIUM);
			 */
			// dateRow.getCell(8).setCellStyle(border);
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			for (IcaTotalMarks itm : icaTotalMarksModuleWise.get(moduleId)) {
				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(itm.getUsername());

				rowd.createCell(colNum++).setCellValue(itm.getStudentName());
				rowd.createCell(colNum++).setCellValue(itm.getRollNo());

				// add component-wise marks

				List<String> componentIds = new ArrayList<>();
				for (IcaComponent ic : getIcaComponentsByModule) {
					logger.info("ica---->" + ic);
					logger.info("----------->" + itm.getUsername());
					Map<String, String> compMarks = mapOfStudentComponentWiseMarks.get(itm.getUsername());
					logger.info("marks===>" + compMarks);
					if (!componentIds.contains(ic.getComponentId())) {

						if (compMarks.get(ic.getComponentId()) != null) {
							rowd.createCell(colNum++).setCellValue(compMarks.get(ic.getComponentId()));
							componentIds.add(ic.getComponentId());
						} else {
							rowd.createCell(colNum++).setCellValue("");
							componentIds.add(ic.getComponentId());
						}
					} /*
						 * else{
						 * 
						 * }
						 */
				}

				rowd.createCell(colNum++).setCellValue(itm.getIcaTotalMarks());
				rowd.createCell(colNum++).setCellValue(itm.getInternalMarks());

				rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
				rowd.createCell(colNum++).setCellValue(itm.getRemarks());

			}
			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
			// log.info("Width Col ------- " + sheet.getColumnWidth(2));
			sheet.setColumnWidth(2, 5500);
			sheet.setColumnWidth(3, 6500);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(8);

			sheet.createFreezePane(0, 7);
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			// FileUtils.deleteQuietly(new File(filePath));
			workbook.close();
		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return filePath;
	}

	// Coursera chnages

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addIcaCoursera", method = RequestMethod.POST)
	public String addIcaCoursera(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {

		try {
			/* New Audit changes start */
			logger.info("Validating /addIcaCoursera...");
			HtmlValidation.validateHtml(icaBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", icaBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year Selected");
			}
			
			List<String> acadSess = null;
			if(icaBean.getAcadSession().contains(",")) {
				acadSess = Arrays.asList(icaBean.getAcadSession().split(","));
				for(String acadS : acadSess) {
					Course acadSession = courseService.checkIfExistsInDB("acadSession", acadS);			
					if(acadSession == null) {
						throw new ValidationException("Invalid Acad Session Selected");
					}
				}
			} else {
				Course acadSession = courseService.checkIfExistsInDB("acadSession", icaBean.getAcadSession());
				if(acadSession == null) {
					throw new ValidationException("Invalid Acad Session Selected");
				}
			}
			if(icaBean.getCampusId() != null && !icaBean.getCampusId().isEmpty()) {
				Course campusId = courseService.checkIfExistsInDB("campusId", icaBean.getCampusId());
				if(campusId == null) {
					throw new ValidationException("Invalid Campus Selected");
				}
			}
			Course moduleId = courseService.checkIfExistsInDB("moduleId", icaBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module Selected");
			}
			logger.info("create ica selected programs are " + icaBean.getProgramId());
			List<String> progIds = null;
			if(icaBean.getProgramId().contains(",")) {
				progIds = Arrays.asList(icaBean.getProgramId().split(","));
				for(String programId : progIds) {
					Course progId = courseService.checkIfExistsInDB("programId", programId);			
					if(progId == null) {
						throw new ValidationException("Invalid Program Selected");
					}
				}
			} else {
				logger.info("addIcaCoursera - Inside single program");
				Course progId = courseService.checkIfExistsInDB("programId", icaBean.getProgramId());
				logger.info("addIcaCoursera - Inside single program");
				if(progId == null) {
					throw new ValidationException("Invalid Program Selected");
				}
			}
			List<String> assignedFaculties = null;
			if(icaBean.getAssignedFaculty().contains(",")) {
				assignedFaculties = Arrays.asList(icaBean.getAssignedFaculty().split(","));
				for(String assignedFaculty : assignedFaculties) {
					UserCourse courseId = userCourseService.getFacultyCourseId(assignedFaculty,icaBean.getModuleId());
					logger.info("courseID is " + courseId);
					if(null == courseId) {
		                  throw new ValidationException("Invalid faculty selected.");
		            }
					UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(assignedFaculty, String.valueOf(courseId.getCourseId()));
		            if(null == userccourse) {
		                  throw new ValidationException("Invalid faculty selected.");
		            }
				}
			} else {
				UserCourse courseId = userCourseService.getFacultyCourseId(icaBean.getAssignedFaculty(),icaBean.getModuleId());
				if(null == courseId) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
				UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(icaBean.getAssignedFaculty(), String.valueOf(courseId.getCourseId()));
	            if(null == userccourse) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
			}
			
			
			logger.info("checking internal marks");
			if(icaBean.getTotalMarks() != null && !icaBean.getTotalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks(),icaBean.getExternalMarks(),icaBean.getExternalPassMarks(),icaBean.getTotalMarks());
			} else
			if(icaBean.getInternalMarks() != null && !icaBean.getInternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getInternalMarks(), icaBean.getInternalPassMarks());
			} else
			if(icaBean.getExternalMarks() != null && !icaBean.getExternalMarks().isEmpty()){
				BusinessBypassRule.validateMarks(icaBean.getExternalMarks(), icaBean.getExternalPassMarks());
			} else {
				throw new ValidationException("Invalid Marks");
			}
			
			Utils.validateStartAndEndDates(icaBean.getStartDate(), icaBean.getEndDate());
			logger.info("Scaled Req is " + icaBean.getScaledReq());
			
			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
				icaBean.setScaledReq("N");
				icaBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumeric(icaBean.getScaledMarks());
			}
			if(icaBean.getIcaDesc() != null && !icaBean.getIcaDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(icaBean.getIcaDesc());
			}
			logger.info("Validation Done");			
			
			String username = principal.getName();

			icaBean.setCreatedBy(username);
			icaBean.setLastModifiedBy(username);
			icaBean.setActive("Y");
			icaBean.setIsNonEventModule("N");
			icaBean.setIsCourseraIca("Y");

//			if (icaBean.getScaledReq() == null || icaBean.getScaledReq().equals("N")) {
//				icaBean.setScaledReq("N");
//				icaBean.setScaledMarks(null);
//			}
			
			/* New Audit changes end */
			/*
			 * IcaBean icaDB = icaBeanService.checkAlreadyExistICA( icaBean.getModuleId(),
			 * icaBean.getAcadYear(), icaBean.getCampusId(), icaBean.getAcadSession());
			 */
			List<IcaBean> icaDBList = new ArrayList<IcaBean>();
			if (icaBean.getAcadSession().contains(",")) {
				List<String> acadSession = Arrays.asList(icaBean.getAcadSession().split(","));
				icaDBList = icaBeanService.checkAlreadyExistICAListCE(icaBean.getModuleId(), icaBean.getAcadYear(),
						icaBean.getCampusId(), acadSession);

			} else {
				icaDBList = icaBeanService.checkAlreadyExistICAList(icaBean.getModuleId(), icaBean.getAcadYear(),
						icaBean.getCampusId(), icaBean.getAcadSession());

			}

			for (IcaBean icaDB : icaDBList) {
				if (icaDB != null) {

					if (icaBean.getProgramId().contains(",")) {
						List<String> programIds = Arrays.asList(icaBean.getProgramId().split(","));

						if (icaDB.getProgramId().contains(",")) {
							for (String db : Arrays.asList(icaDB.getProgramId().split(","))) {
								if (programIds.contains(db)) {
									setError(redirectAttrs, "ICA Already Exist");
									return "redirect:/addIcaFormForCoursera";
								}
							}
						} else {
							if (programIds.contains(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForCoursera";
							}
						}
					} else {
						if (icaDB.getProgramId().contains(",")) {
							if (Arrays.asList(icaDB.getProgramId().split(",")).contains(icaBean.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForCoursera";
							}
						} else {
							if (icaBean.getProgramId().equals(icaDB.getProgramId())) {
								setError(redirectAttrs, "ICA Already Exist");
								return "redirect:/addIcaFormForCoursera";
							}
						}
					}

				}
			}
			/*
			 * if (totalMarks != icaInternalMarks + icaExternalMarks) {
			 * 
			 * setError(redirectAttrs,
			 * "Total Marks & Sum of Internal,External Marks Should Match"); return
			 * "redirect:/addIcaForm"; logger.info("validation"); } else
			 */

			icaBeanService.insertWithIdReturn(icaBean);
			List<String> userList = new ArrayList<String>();
			if (icaBean.getAssignedFaculty().contains(",")) {
				List<String> faculties = new ArrayList<String>();
				faculties = Arrays.asList(icaBean.getAssignedFaculty().split(","));
				userList.addAll(faculties);
			} else {
				userList.add(icaBean.getAssignedFaculty());
			}

			User u = userService.findByUserName(username);
			String subject = " New ICA: " + icaBean.getIcaName();
			Map<String, Map<String, String>> result = null;
			List<String> acadSessionList = new ArrayList<>();
			String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaBean.getIcaName() + " <br>"
					+ "<b>ICA Description: </b>" + icaBean.getIcaDesc() + "<br><br>"
					+ "<b>Note: </b>This ICA is created by : ?? <br>"
					+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			// COMMENTED TEMPORARILY

			if (!userList.isEmpty()) {
				notificationEmailMessage = notificationEmailMessage.toString().replace("??",
						" Name : " + u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
				result = userService.findEmailByUserName(userList);
				Map<String, String> email = result.get("emails");
				Map<String, String> mobiles = new HashMap();
				logger.info("email -----> " + email);
				logger.info("mobile -----> " + mobiles);
				logger.info("subject -----> " + subject);
				logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
				//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
			}

			setSuccess(redirectAttrs, "Coursera ICA Added Successfully");
			// redirectAttrs.addAttribute("icaId", icaBean.getId());
			redirectAttrs.addFlashAttribute("icaBean", icaBean);
			if (icaBean.getAssignedFaculty().contains(",")) {
				return "redirect:/createStudentGroupForm";
			} else {
				return "redirect:/addIcaComponentsForm";
			}

		}
		
		catch (ValidationException ve) {
			logger.info("INSIDE Validation Exception");
			logger.error(ve.getMessage(), ve);
			setError(redirectAttrs, ve.getMessage());
			return "redirect:/addIcaForm";
		}

		catch (Exception ex) {

			logger.error("Excption while creating ICA", ex);

			setError(redirectAttrs, "Error While Creating ICA,ICA May have already created");
			return "redirect:/addIcaFormForCoursera";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/GetProgramsFromAcadSessionYearModuleCE", method = { RequestMethod.GET })
	public @ResponseBody String GetProgramsFromAcadSessionYearModuleCE(@RequestParam String acadYear,
			@RequestParam String moduleId, Principal principal) {

		List<Program> programList = new ArrayList<Program>();

		programList = programService.findAllProgramsWithAcadSessionYearModuleCE(acadYear, moduleId);

		JSONArray jsonarray = new JSONArray();
		try {
			for (Program p : programList) {
				JSONObject obj = new JSONObject();
				obj.put("value", p.getId().toString());
				obj.put("text", p.getProgramName());
				jsonarray.put(obj);
			}

			// logger.info(jsonarray.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error";
		}
		return jsonarray.toString();
	}

	// add Ica For Coursera

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/getSessionByParamCE", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getSessionByParamCE(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId, @RequestParam(name = "moduleId") String moduleId,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService.acadSessionListByAcadYearAndCampusCE(acadYear,
				campusId, moduleId);

		try {
			JSONArray array = new JSONArray();
			for (Course module : moduleComponentListByYearAndCampus) {
				JSONObject obj = new JSONObject();
				obj.put("value", module.getAcadSession());
				obj.put("text", module.getAcadSession());
				array.put(obj);
			}
			json = array.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addIcaFormForCoursera", method = { RequestMethod.GET, RequestMethod.POST })
	public String addIcaFormForCoursera(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Add ICA Coursera", true, false));

		IcaBean ica = new IcaBean();
		// ica.setProgramId(userdetails1.getProgramId());
		m.addAttribute("multipleFaculty", "true");
		if (id != null) {

			ica = icaBeanService.findByID(Long.valueOf(id));
			// IcaTotalMarks icaMarks = icaTotalMarksService.ge
			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			logger.info("ica received" + ica);
			if ("Y".equals(ica.getIsIcaDivisionWise()) && ica.getParentIcaId() == null) {

				m.addAttribute("isParentIca", "true");
				m.addAttribute("isGradingStart",
						icaComponentMarksService.checkWhetherGradingStartOrNotP(String.valueOf(ica.getId())));

			} else {
				m.addAttribute("isParentIca", "false");
				m.addAttribute("isGradingStart",
						icaComponentMarksService.checkWhetherGradingStartOrNot(String.valueOf(ica.getId())));
			}

			if (currentDate.compareTo(ica.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot update, ICA Deadline is over");
				return "redirect:/searchIcaList";
			} else if ("Y".equals(ica.getIsSubmitted())) {
				setNote(redirectAttrs, "Cannot update, ICA is Already Evaluated");
				return "redirect:/searchIcaList";
			} else {
				if (currentDate.compareTo(ica.getStartDate()) > 0) {

					m.addAttribute("icaStarted", "true");
				} else {
					m.addAttribute("icaStarted", "false");
				}
			}

			m.addAttribute("edit", "true");
			m.addAttribute("moduleName", courseService.getModuleName(ica.getModuleId()));
			m.addAttribute("programList", programService.getProgramListByIds(ica.getProgramId()));
			m.addAttribute("facultyList",
					userCourseService.findAllFacultyWithModuleIdICA(ica.getModuleId(), ica.getAcadYear()));
			if (null != ica.getAssignedFaculty()) {
				if (ica.getAssignedFaculty().contains(",")) {
					m.addAttribute("selectedFacultyList",
							userCourseService.findAllFacultyByFacultyIds(ica.getAssignedFaculty()));
					logger.info("facultyListSize2--->"
							+ userCourseService.findAllFacultyByFacultyIds(ica.getAssignedFaculty()));
					m.addAttribute("multipleFaculty", "true");
				} else {
					if ("Y".equals(ica.getIsIcaDivisionWise()) && null == ica.getParentIcaId()) {
						m.addAttribute("multipleFaculty", "false");
					} else if ("Y".equals(ica.getIsIcaDivisionWise()) && null != ica.getParentIcaId()) {
						m.addAttribute("multipleFaculty", "false");
					} else {
						m.addAttribute("multipleFaculty", "true");
						m.addAttribute("selectedFacultyList",
								userCourseService.findAllFacultyByFacultyIds(ica.getAssignedFaculty()));
					}
				}
			}

			List<String> acadSessionList = Arrays.asList(ica.getAcadSession().split("\\s*,\\s*"));
			m.addAttribute("acadSessionList", acadSessionList);
			logger.info("Ica AcadSession------------" + ica.getAcadSession());
			m.addAttribute("icaBean", ica);
			if ("Y".equals(ica.getIsIcaDivisionWise()) && ica.getParentIcaId() == null) {
				setNote(m, "This is Division-Wise ICA,Any Update in this, will reflect to all ICA Divisions");
				return "ica/createIcaForDivision";
			}
		}

		List<String> acadYearCodeList = courseService.findAcadYearCode();

		logger.info("acadYearCodeList------------>" + acadYearCodeList);
		m.addAttribute("acadYearCodeList", acadYearCodeList);
		m.addAttribute("icaBean", ica);

		return "ica/createIcaCoursera";

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/getModuleByParamCE", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getModuleByParamCE(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId,

			Principal principal) {
		logger.info("called--------->");
		String json = "";
		String username = principal.getName();
		logger.info("campus id is" + campusId);
		List<Course> moduleComponentListByYearAndCampus = courseService.moduleListByAcadYearAndCampusCE(acadYear,
				campusId);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course module : moduleComponentListByYearAndCampus) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(module.getModuleId(), module.getModuleName() + "(" + module.getModuleAbbr() + ")");
			res.add(returnMap);
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		logger.info("json:" + json);
		return json;

	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/updateIcaInternalPassMarkBySupportAdminForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateIcaInternalPassMarkBySupportAdminForm(Model m, Principal principal,
			@ModelAttribute IcaBean icaBean, RedirectAttributes redirectAttrs, @RequestParam String icaId) {

		icaBean.setActive("Y");
		icaBean.setLastModifiedBy(principal.getName());
		m.addAttribute("icaId", icaId);
		m.addAttribute("icaBeanS", new IcaBean());
		IcaBean persentInternalPassmarks = icaBeanService.presentIcaInternalPassMarks(icaId);
		m.addAttribute("persentInternalPassmarks", persentInternalPassmarks.getInternalPassMarks());
		return "ica/updateInternalPassMarkBySupportAdmin";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN" })
	@RequestMapping(value = "/updateIcaInternalPassMarkBySupportAdmin", method = RequestMethod.POST)
	public String updateIcaInternalPassMarkBySupportAdmin(Model m, Principal principal, @ModelAttribute IcaBean icaBean,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		IcaBean icaBeanDAO = icaBeanService.findByID(icaBean.getId());
		icaBeanDAO.setLastModifiedBy(username);

		if (((icaBeanDAO.getParentIcaId()) == (null)) && ((icaBeanDAO.getIsIcaDivisionWise()).equals("Y"))) {
			icaBeanService.updateParentIcaIdInternalPassMarks(icaBean.getInternalPassMarks(), icaBeanDAO.getId());
			icaBeanService.updateIcaInternalPassMarks(icaBean.getInternalPassMarks(), icaBeanDAO.getId());
			setSuccess(redirectAttrs, "Ica Internal Passing Marks Updated Successfully");
			return "redirect:/icaListBySupportAdmin";

		} else if ((!("Y").equals(icaBeanDAO.getIsIcaDivisionWise()))) {
			icaBeanService.updateIcaInternalPassMarks(icaBean.getInternalPassMarks(), icaBeanDAO.getId());
			setSuccess(redirectAttrs, "Ica Internal Passing Marks Updated Successfully");
			return "redirect:/icaListBySupportAdmin";

		} else {
			icaBeanService.updateIcaInternalPassMarks(icaBean.getInternalPassMarks(), icaBeanDAO.getId());
			setSuccess(redirectAttrs, "Ica Internal Passing Marks Updated Successfully");
			return "redirect:/icaListBySupportAdmin";

		}
	}

	// auto assign ica portal test marks 19-09-2020

	// New Mapping

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/assignTestMarksToIcaForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String assignTestMarksToIcaForm(@RequestParam Long icaId, @RequestParam(required = false) String courseId,
			Principal p, @RequestParam(required = false) String showEvalBtn, Model m,
			RedirectAttributes redirectAttrs) {

		m.addAttribute("webPage", new WebPage("addTestMarksToIcaForm", "Add Test Marks To ICA", true, false));
		String username = p.getName();
		try {

			IcaBean icaBean = icaBeanService.findByID(icaId);

			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());

			if (currentDate.compareTo(icaBean.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot Evaluate, ICA Deadline is over");
				return "redirect:/searchIcaList";
			}

			else if (currentDate.compareTo(icaBean.getStartDate()) < 0) {
				setNote(redirectAttrs, "Cannot Evaluate, ICA has not started yet.");
				return "redirect:/searchIcaList";
			} else {
				if ("Y".equals(icaBean.getIsSubmitted())) {
					setNote(redirectAttrs, "ICA Already Evaluated");
					return "redirect:/searchIcaList";
				}
			}

			List<Test> testListForIca = testService.getTestForIcaModue(icaBean.getModuleId(), icaBean.getAcadYear(),
					username, courseId);

			String courseIdForDivWis = null;
			List<Test> coursesForTest = new ArrayList<>();
			if ("Y".equals(icaBean.getIsIcaDivisionWise())) {
				courseIdForDivWis = icaBean.getEventId();
				coursesForTest = testService.getCoursesForTestIca(icaBean.getModuleId(), icaBean.getAcadYear(),
						username, courseIdForDivWis);
			} else {
				coursesForTest = testService.getCoursesForTestIca(icaBean.getModuleId(), icaBean.getAcadYear(),
						username, courseIdForDivWis);
			}

			Map<Long, List<String>> testIdsForIcaMap = new HashMap<>();
			for (Test t : coursesForTest) {
				logger.info("course1" + t.getCourseId());
				IcaBean icaTestDB = icaBeanService.getIcaTests(icaId, t.getCourseId());
				logger.info("ica null");
				if (icaTestDB == null) {
					IcaBean icaTest = new IcaBean();
					icaTest.setId(icaId);
					icaTest.setCourseId(String.valueOf(t.getCourseId()));
					icaBeanService.insertInIcaTests(icaTest);
					m.addAttribute("testIdsForIca", null);
					logger.info("idsssss if null");
				} else {
					logger.info("icaTest DB" + icaTestDB);
					if (icaTestDB.getTestIdsForIca() != null) {
						logger.info("idsssss111" + Arrays.asList(icaTestDB.getTestIdsForIca().split(",")));
						testIdsForIcaMap.put(t.getCourseId(), Arrays.asList(icaTestDB.getTestIdsForIca().split(",")));
						m.addAttribute("testIdsForIca", testIdsForIcaMap);
					}
				}
			}

			Map<Long, List<Test>> mapper = new HashMap<>();
			Map<Long, String> courseMap = new HashMap<>();
			Set<Long> courses = new HashSet<>();

			testListForIca.forEach(i -> courses.add(i.getCourseId()));

			for (Long c : courses) {

				List<Test> testList = new ArrayList<>();
				for (Test t : testListForIca) {
					if (!courseMap.containsKey(c)) {
						Course courseDB = courseService.findByID(c);
						courseMap.put(c, courseDB.getCourseName());
					}

					if (c.equals(t.getCourseId())) {

						testList.add(t);
					}
				}
				mapper.put(c, testList);
			}
			logger.info("mapper is:" + mapper);
			logger.info("courseMap is:" + courseMap);
			m.addAttribute("testList", testListForIca);
			m.addAttribute("mapper", mapper);
			m.addAttribute("courseMap", courseMap);
			m.addAttribute("coursesForTest", coursesForTest);

			if (courseId != null) {
				IcaBean icaTestDB = icaBeanService.getIcaTests(icaId, Long.valueOf(courseId));

				// IcaBean ica = new IcaBean();
				if (icaTestDB != null) {
					if (icaTestDB.getTestIdsForIca() != null) {
						icaTestDB = icaBeanService.getAllIcaTests(icaId, Long.valueOf(courseId));

					}
					icaTestDB.setId(icaId);
					logger.info("ica bean when course id is not null" + icaTestDB);
					m.addAttribute("ica", icaTestDB);
				} else {
					IcaBean ica = new IcaBean();
					ica.setId(icaId);
					ica.setCourseId(courseId);
					m.addAttribute("ica", ica);
				}
			} else {
				IcaBean ica = new IcaBean();
				ica.setId(icaId);
				m.addAttribute("ica", ica);
			}

			m.addAttribute("icaId", icaId);
			m.addAttribute("courseId", courseId);
			if (courseId != null) {
				m.addAttribute("showTests", "true");
			} else {
				m.addAttribute("showTests", "false");
			}

			int showEval = icaBeanService.getIcaTestNC(icaId);

			if (showEval == 0) {
				m.addAttribute("showEvalBtn", "true");
			} else {
				m.addAttribute("showEvalBtn", "false");
			}

			return "ica/addTestMarksToIca";

		} catch (Exception ex) {

			logger.error("Exception", ex);
			setError(redirectAttrs, "Error in add displaying form");
			return "redirect:/searchIcaList";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveTestsMarksForIca", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveTestsMarksForIca(@ModelAttribute IcaBean ica, Model m, RedirectAttributes redirectAttrs,
			Principal p) {

		m.addAttribute("webPage", new WebPage("addTestMarksToIcaForm", "Add Test Marks To ICA", true, false));
		String username = p.getName();
		try {
			logger.info("ica bean is:" + ica.getId());
			logger.info("ica bean is" + ica);
			redirectAttrs.addAttribute("icaId", ica.getId());
			redirectAttrs.addAttribute("courseId", ica.getCourseId());
			if (ica.getModeOfAddingTestMarks().equals("bestOf")) {
				if (ica.getTestIds().size() <= ica.getBestOf()) {
					setError(redirectAttrs, "No Of Test Selected is less than best of number");
					return "redirect:/assignTestMarksToIcaForm";
				} else {
					List<Test> tests = testService.getTestsByIds(ica.getTestIds());
					if (tests.size() != 1) {
						setError(redirectAttrs, "Tests Which are selected should carry same marks.");
						return "redirect:/assignTestMarksToIcaForm";
					}
				}
			}

			IcaBean icaTestDB = icaBeanService.getIcaTests(ica.getId(), Long.valueOf(ica.getCourseId()));
			if (icaTestDB.getTestIdsForIca() != null) {
				List<String> updTestIds = new ArrayList<>();
				List<String> testIdsFromDb = Arrays.asList(icaTestDB.getTestIdsForIca().split(","));
				List<String> selectedTestIds = ica.getTestIds();
				updTestIds.addAll(testIdsFromDb);
				updTestIds.addAll(selectedTestIds);
				logger.info("updated testIds" + updTestIds);
				ica.setTestIds(updTestIds);
				logger.info("updated ica ids" + ica.getTestIds());
			}

			IcaBean icaBean = icaBeanService.findByID(ica.getId());
			PredefinedIcaComponent pred = predefinedIcaComponentService.findByComponentName("Portal Test");
			Map<String, Double> responseMsg = addComponentMarksToIca(ica, pred, username);
			logger.info("Response is:" + responseMsg);

			if (responseMsg.isEmpty() || ica.getTestIds().size() == 0) {
				setError(redirectAttrs, "Test Data Not Found!!!" + ica.getTestIds());
				return "redirect:/assignTestMarksToIcaForm";
			}
			List<IcaComponentMarks> icmMarksList = new ArrayList<>();
			for (String s : responseMsg.keySet()) {
				IcaComponentMarks icm = new IcaComponentMarks();
				icm.setUsername(s);
				icm.setComponentId(String.valueOf(pred.getId()));
				icm.setMarks(String.valueOf(responseMsg.get(s)));
				icm.setIcaId(String.valueOf(ica.getId()));
				icm.setSaveAsDraft("Y");
				icm.setActive("Y");
				icm.setCreatedBy(username);
				icm.setLastModifiedBy(username);

				icmMarksList.add(icm);
			}
			logger.info("Test ids:----" + ica.getTestIds());
			icaComponentMarksService.upsertBatch(icmMarksList);
			String testIds = (StringUtils.join(ica.getTestIds(), ","));
			logger.info("Test ids:----" + testIds);
			IcaBean icaUpd = new IcaBean();
			icaUpd.setId(ica.getId());
			icaUpd.setCourseId(ica.getCourseId());
			icaUpd.setTestIdsForIca(testIds);
			icaUpd.setModeOfAddingTestMarks(ica.getModeOfAddingTestMarks());
			if ("bestOf".equals(ica.getModeOfAddingTestMarks())) {
				icaUpd.setBestOf(ica.getBestOf());
			}
			icaBeanService.updateInIcaTests(icaUpd);

			setSuccess(redirectAttrs, "Marks have been updated in ICA component successfully!!");

			int icaNC = icaBeanService.getIcaTestNC(ica.getId());
			if (icaNC == 0) {
				redirectAttrs.addAttribute("showEvalBtn", "true");
			} else {
				redirectAttrs.addAttribute("showEvalBtn", "false");
			}
			return "redirect:/assignTestMarksToIcaForm";

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in updating Marks in ICA");
			logger.error("Exception", ex);
			setError(redirectAttrs, "Error in add displaying form");
			return "redirect:/searchIcaList";
		}

	}

	public Map<String, Double> addComponentMarksToIca(IcaBean icaBean, PredefinedIcaComponent pred, String username) {
		logger.info("method entered");
		logger.info("mode is" + icaBean.getModeOfAddingTestMarks());
		Map<String, List<Double>> usernameScoresMap = new HashMap<>();
		IcaBean icaBeanDB = icaBeanService.findByID(icaBean.getId());
		Map<String, Double> returnMap = new HashMap<>();
		double compMark = 0.0;
		if ("Y".equals(icaBeanDB.getIsIcaDivisionWise())) {
			compMark = icaComponentService.getCompMarks(Long.valueOf(icaBeanDB.getParentIcaId()), pred.getId());
		} else {
			compMark = icaComponentService.getCompMarks(icaBean.getId(), pred.getId());
		}
		List<Test> tests = testService.getTestsByIds(icaBean.getTestIds());
		IcaBean icaDB = icaBeanService.findByID(icaBean.getId());
		try {
			if ("bestOf".equals(icaBean.getModeOfAddingTestMarks())) {
				int bestOf = icaBean.getBestOf();
				List<StudentTest> studentTestsScores = new ArrayList<>();
				if (icaDB.getAssignedFaculty().contains(",")) {
					List<String> usernameList = icaStudentBatchwiseService
							.getDistinctUsernamesByActiveIcaIdAndFaculty(icaDB.getId(), username);
					studentTestsScores = studentTestService.getStudetTestMarksByTestList(icaBean.getTestIds(),
							usernameList);
				} else {
					studentTestsScores = studentTestService.getStudetTestMarksByTestList(icaBean.getTestIds());
				}

				List<String> usernames = new ArrayList<>();

				studentTestsScores.forEach(i -> usernames.add(i.getUsername()));
				logger.info("student test scores:" + studentTestsScores.size());
				for (String u : usernames) {

					List<Double> scores = new ArrayList<>();

					for (StudentTest st : studentTestsScores) {

						if (u.equals(st.getUsername())) {

							scores.add(st.getScore());
						}
					}

					if (scores.size() > bestOf) {
						double[] intArray = new double[scores.size()];
						int i = 0;
						for (Double d : intArray) {
							intArray[i] = scores.get(i);
							i++;
						}
						List<Object> bestScores = Arrays.asList(DoubleStream.of(intArray).sorted()
								.skip(scores.size() - bestOf).limit(bestOf).boxed().toArray());
						List<Double> topScores = new ArrayList<>();
						for (Object o : bestScores) {
							topScores.add((Double) o);
						}
						usernameScoresMap.put(u, topScores);

						for (String s : usernameScoresMap.keySet()) {
							List<Double> getScores = usernameScoresMap.get(s);
							double totalScore = 0.0;
							for (Double d : getScores) {
								totalScore = totalScore + d;
							}

							double avgOfTotalScores = totalScore / getScores.size();

							if (tests.get(0).getMaxScore() != compMark) {
								double divRes = avgOfTotalScores / tests.get(0).getMaxScore();
								double finalScore = divRes * compMark;
								returnMap.put(s, round(finalScore, 2));
							} else {
								returnMap.put(s, round(avgOfTotalScores, 2));
							}
						}
						logger.info("return Map:" + returnMap);
					} else {
						continue;
					}
				}

			} else {

				List<StudentTest> studentTestsScores = new ArrayList<>();
				if (icaDB.getAssignedFaculty().contains(",")) {
					List<String> usernameList = icaStudentBatchwiseService
							.getDistinctUsernamesByActiveIcaIdAndFaculty(icaDB.getId(), username);
					studentTestsScores = studentTestService.getStudetTestMarksByTestList(icaBean.getTestIds(),
							usernameList);
				} else {
					studentTestsScores = studentTestService.getStudetTestMarksByTestList(icaBean.getTestIds());
				}

				List<String> usernames = new ArrayList<>();
				Map<String, List<Double>> mapper = new HashMap<>();
				studentTestsScores.forEach(i -> usernames.add(i.getUsername()));

				for (String u : usernames) {

					List<Double> scores = new ArrayList<>();

					for (StudentTest st : studentTestsScores) {

						if (u.equals(st.getUsername())) {

							scores.add(st.getScore());
						}
					}
					mapper.put(u, scores);
				}

				if (tests.size() == 1) {
					double maxScore = tests.get(0).getMaxScore();

					logger.info("studList map" + mapper);
					for (String s : mapper.keySet()) {
						double sumOfScore = 0.0;
						double avgOfScore = 0.0;
						List<Double> studList = mapper.get(s);
						logger.info("size is:1");

						for (Double d : studList) {
							sumOfScore = sumOfScore + d;
						}

						avgOfScore = sumOfScore / studList.size();

						if (maxScore != compMark) {
							double divRes = avgOfScore / maxScore;
							double finalMarks = divRes * compMark;

							returnMap.put(s, round(finalMarks, 2));
						} else {
							returnMap.put(s, round(avgOfScore, 2));
						}

					}

				} else {

					double totalOutOf = 0.0;

					for (Test t : tests) {
						totalOutOf = totalOutOf + t.getMaxScore();
					}

					for (String s : mapper.keySet()) {
						double sumOfScore = 0.0;
						List<Double> studList = mapper.get(s);

						for (Double d : studList) {
							sumOfScore = sumOfScore + d;
						}

						double divRes = sumOfScore / totalOutOf;
						double finalMarks = divRes * compMark;
						returnMap.put(s, round(finalMarks, 2));
					}
				}

			}
			logger.info("Mapper--->" + usernameScoresMap);
			return returnMap;
		} catch (Exception ex) {
			logger.error("Exception", ex);
			return returnMap;
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = { "/clearIcaTestMarks" }, method = { RequestMethod.GET })
	public String deleteMeeting(Model m, @RequestParam String icaId, @RequestParam String courseId,
			HttpServletResponse resp, Principal p, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		String username = p.getName();
		redirectAttrs.addAttribute("icaId", icaId);
		redirectAttrs.addAttribute("courseId", courseId);
		try {
			Long icaIdVal = Long.valueOf(icaId);
			Long courseIdVal = Long.valueOf(courseId);
			IcaBean ica = new IcaBean();
			ica.setId(icaIdVal);
			ica.setCourseId(courseId);

			IcaBean icaDB = icaBeanService.findByID(icaIdVal);
			IcaBean icaTests = icaBeanService.getIcaTests(icaIdVal, courseIdVal);

			if (icaTests.getTestIdsForIca().contains(",")) {
				icaTests.setTestIds(Arrays.asList(icaTests.getTestIdsForIca().split(",")));
			} else {
				icaTests.setTestIds(Arrays.asList(icaTests.getTestIdsForIca()));
			}

			List<StudentTest> studentTestsScores = new ArrayList<>();
			if (icaDB.getAssignedFaculty().contains(",")) {
				List<String> usernameList = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaIdAndFaculty(icaDB.getId(), username);
				studentTestsScores = studentTestService.getStudetTestMarksByTestList(icaTests.getTestIds(),
						usernameList);
			} else {
				studentTestsScores = studentTestService.getStudetTestMarksByTestList(icaTests.getTestIds());
			}

			List<String> usernames = new ArrayList<>();
			studentTestsScores.forEach(i -> usernames.add(i.getUsername()));

			icaComponentMarksService.deleteIcaTotalMarksByStudents(icaId, usernames);
			icaTotalMarksService.deleteIcaTotalMarksByStudents(icaId, usernames);
			int effRows = icaBeanService.clearIcaTests(ica);
			if (effRows > 0) {
				setSuccess(redirectAttrs, "Test Selections Cleared Successfully");
			} else {
				setNote(redirectAttrs, "Tets Data Not Found!!");
			}
			return "redirect:/assignTestMarksToIcaForm";
		} catch (Exception ex) {
			logger.error("Exception", ex);
			setSuccess(redirectAttrs, "Error Occured While Clearing Test Selection");
			return "redirect:/assignTestMarksToIcaForm";
		}
	}

// ICA CHanges on 08-10-2020 to check tcs flag

	public boolean isIcaMarksSentToTcs(IcaBean i) {

		if ("Y".equals(i.getIsIcaDivisionWise()) && i.getParentIcaId() == null) {
			List<IcaBean> divWiseIca = icaBeanService.findDivisionWiseIcaListByParentIca(String.valueOf(i.getId()));
			if (divWiseIca.size() == 0) {
				return false;
			}
			for (IcaBean divIca : divWiseIca) {
				if (null != divIca.getIsPublished() && divIca.getIsPublished().equals("Y")) {
					long divId = divIca.getId();
					int noOfStudentsInIca = icaTotalMarksService.getNoOfStudentsForIca(divId);
					int noOfStudentsMarksSent = icaTotalMarksService.getCountOfTcsFlagSentForIca(divId);
					if (noOfStudentsInIca > 0) {
						if (noOfStudentsInIca != noOfStudentsMarksSent) {
							return false;
						}
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		} else {
			if (null != i.getIsPublished() && i.getIsPublished().equals("Y")) {
				long divId = i.getId();
				int noOfStudentsInIca = icaTotalMarksService.getNoOfStudentsForIca(divId);
				int noOfStudentsMarksSent = icaTotalMarksService.getCountOfTcsFlagSentForIca(divId);
				logger.info("no of students in ica" + noOfStudentsInIca);
				if (noOfStudentsInIca > 0) {
					if (noOfStudentsInIca != noOfStudentsMarksSent) {
						return false;
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	// new mappings

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/saveIcaCompMarksAsDraft", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String saveIcaCompMarksAsDraft(@RequestParam Map<String, String> allRequestParams,
			Principal p) {
		logger.info("allRequestParams----------------" + allRequestParams);
		List<IcaComponentMarks> icaCompMarksList = new ArrayList<>();
		List<IcaTotalMarks> icaTotalMarksList = new ArrayList<>();

		List<String> icaCompStudList = new ArrayList<>();
		List<String> icaTotalStudList = new ArrayList<>();
		String compId = "";
		try {
			for (String sapId : allRequestParams.keySet()) {

				if (sapId.contains("-")) {
					String[] splitKey = sapId.split("-");
					IcaComponentMarks icaCompMarks = createIcaComponent(splitKey, allRequestParams, "DRAFT", sapId,
							p.getName());
					if (compId.isEmpty()) {
						compId = icaCompMarks.getComponentId();
					}
					icaCompMarks.setRemarks(allRequestParams.get("remark" + splitKey[0]));

					if (!allRequestParams.get(sapId).isEmpty() && allRequestParams.get(sapId) != null) {
						icaCompMarksList.add(icaCompMarks);

						if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
							icaCompStudList.add(icaCompMarks.getUsername());
						}

					}

				}

			}

			icaComponentMarksService.upsertBatch(icaCompMarksList);

			if (!allRequestParams.get("icaIdValue").isEmpty() && allRequestParams.get("icaIdValue") != null) {
				List<String> studList = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
				if (studList.size() > 0) {
					List<String> compUsersCopy = new ArrayList();
					List<String> compUsers = icaComponentMarksService
							.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"), compId);
					compUsers.addAll(icaCompStudList);
					compUsersCopy = compUsers.stream().distinct().collect(Collectors.toList());

					if (studList.size() != compUsersCopy.size()) {
						icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT",
								allRequestParams.get("icaIdValue"), "N", compId);
						/*
						 * for(IcaComponentMarks icm : icaCompMarksList){ icm.setSaveAsDraft("N");
						 * icm.setFinalSubmit("N"); }
						 */
					} else {
						icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT",
								allRequestParams.get("icaIdValue"), "Y", compId);
					}

				}
			}

			return "saved";
		} catch (Exception ex) {

			logger.error("Exception", ex);

			return "error";
		}

	}
	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/submitIcaCompWise", method = { RequestMethod.POST, RequestMethod.GET })
	public String submitIcaCompWise(@RequestParam Map<String, String> allRequestParams, Principal p,
			RedirectAttributes redirectAttributes) {
		logger.info("allRequestParams----------------" + allRequestParams);

		String username = p.getName();
		List<IcaComponentMarks> icaCompMarksList = new ArrayList<>();
		List<IcaTotalMarks> icaTotalMarksList = new ArrayList<>();

		List<String> icaCompStudList = new ArrayList<>();
		List<String> icaTotalStudList = new ArrayList<>();
		boolean isAllSubmitted = false;
		redirectAttributes.addAttribute("icaId", allRequestParams.get("icaIdValue"));

		IcaBean icaDB = icaBeanService.findByID(Long.valueOf(allRequestParams.get("icaIdValue")));
		String compId = "";
		try {
			for (String sapId : allRequestParams.keySet()) {

				if (sapId.contains("-")) {
					String[] splitKey = sapId.split("-");
					IcaComponentMarks icaCompMarks = createIcaComponent(splitKey, allRequestParams, "SUBMIT", sapId,
							p.getName());
					if (compId.isEmpty()) {
						compId = icaCompMarks.getComponentId();
					}

					icaCompMarks.setRemarks(allRequestParams.get("remark" + splitKey[0]));

					if (!allRequestParams.get(sapId).isEmpty() && allRequestParams.get(sapId) != null) {
						icaCompMarksList.add(icaCompMarks);

						if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
							icaCompStudList.add(icaCompMarks.getUsername());
						}

					}

				}

			}

			icaComponentMarksService.upsertBatch(icaCompMarksList);

			if (!allRequestParams.get("icaIdValue").isEmpty() && allRequestParams.get("icaIdValue") != null) {
				List<String> studList = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"));
				List<String> remainingStudList = icaStudentBatchwiseService
						.getDistinctActiveUsernamesOfRemainingFaculty(Long.valueOf(allRequestParams.get("icaIdValue")),
								p.getName());
				if (studList.size() > 0) {
					List<String> compUsersCopy = new ArrayList();
					List<String> compUsers = icaComponentMarksService
							.getDistinctUsernamesByActiveIcaId(allRequestParams.get("icaIdValue"), compId);
					compUsers.addAll(icaCompStudList);
					compUsersCopy = compUsers.stream().distinct().collect(Collectors.toList());

					if (studList.size() != compUsersCopy.size()) {
						isAllSubmitted = false;
						/*
						 * icaComponentMarksService .updateSaveAsDraftOrFinalSubmit("SUBMIT",
						 * allRequestParams.get("icaIdValue"), "N");
						 */

						icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList,
								allRequestParams.get("icaIdValue"));

						/*
						 * for(IcaComponentMarks icm : icaCompMarksList){ icm.setSaveAsDraft("N");
						 * icm.setFinalSubmit("N"); }
						 */
					} else {
						List<String> finalSubmitCompoMarksList = icaComponentMarksService
								.getFinalSubmitByIcaIdAndUserList(allRequestParams.get("icaIdValue"), remainingStudList,
										compId);
						if (finalSubmitCompoMarksList.size() == 1) {
							if (("Y").equalsIgnoreCase(finalSubmitCompoMarksList.get(0))) {
								isAllSubmitted = true;
								icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT",
										allRequestParams.get("icaIdValue"), "Y");
							} else {
								isAllSubmitted = false;
								icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList,
										allRequestParams.get("icaIdValue"));
							}
						} else {
							isAllSubmitted = false;
							icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList,
									allRequestParams.get("icaIdValue"));
						}

					}

				} else {
					isAllSubmitted = true;
				}
			}

			logger.info("is All Submitted:" + isAllSubmitted);
			if (isAllSubmitted) {
				String icaId = allRequestParams.get("icaIdValue");
				int getTotalComps = 0;
				int getSubmittedComps = 0;

				icaComponentService.updateIcaCompToSubmitted(icaId, compId);
				getTotalComps = icaComponentService.getTotalComponents(icaId);
				getSubmittedComps = icaComponentService.getSubmittedComponents(icaId);

				IcaBean icaBeanDB = icaBeanService.findByID(Long.valueOf(icaId));

				if (getTotalComps == getSubmittedComps) {

					List<IcaComponentMarks> icaMarks = icaComponentMarksService.getIcaMarks(icaId);
					List<IcaTotalMarks> itmList = new ArrayList<>();
					for (IcaComponentMarks ica : icaMarks) {

						IcaTotalMarks itm = new IcaTotalMarks();
						itm.setUsername(ica.getUsername());
						itm.setIcaTotalMarks(ica.getMarks());
						itm.setIcaId(icaId);
						if ("Y".equals(icaBeanDB.getScaledReq())) {
							double totalMarks = Double.valueOf(icaBeanDB.getTotalMarks());
							double totalSMarks = Double.valueOf(icaBeanDB.getScaledMarks());
							double marksObt = Double.valueOf(ica.getMarks());
							double mulVal = marksObt * totalSMarks;
							double scaleMarks = mulVal / totalMarks;

							itm.setIcaScaledMarks(String.valueOf(scaleMarks));

						}

						itm.setActive("Y");
						itm.setFinalSubmit("Y");
						itm.setCreatedBy(username);
						itm.setLastModifiedBy(username);
						itmList.add(itm);
					}
					icaTotalMarksService.upsertBatch(itmList);

					icaBeanService.updateIcaToSubmitted(icaId, Utils.getInIST());
				}
			}
			setSuccess(redirectAttributes, "ICA Marks Submitted Successfully");
			try {
				List<IcaComponentMarks> reevalStudent = icaComponentMarksService
						.getIsReevalIcaUsername(String.valueOf(icaDB.getId()), compId);
				if (reevalStudent.size() > 0) {

					icaQueriesService.updateReEvaluated(icaDB.getId(), compId);

					User u = userService.findByUserName(username);
					String moduleName = "";
					if (icaDB.getIsNonEventModule().equals("Y")) {
						moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
					} else {
						moduleName = courseService.getModuleName(icaDB.getModuleId());
					}
					String subject = " ICA Marks Re-evaluated status changed for Subject " + moduleName;
					Map<String, Map<String, String>> result = null;
					List<String> userList = new ArrayList<String>();
					List<String> acadSessionList = new ArrayList<>();
					// userList.addAll(reevalStudent);
					String notificationEmailMessage = "";
					for (IcaComponentMarks uc : reevalStudent) {
						userList.clear();
						userList.add(uc.getUsername());
						if (!userList.isEmpty()) {
							notificationEmailMessage = "<html><body>" + "<b>Query Status: </b> ? <br>"
									+ "<b>Remakrs: </b>" + uc.getRemarks() + "<br><br>"
									// + "<b>Note: </b>This ICA is created by : ?? <br>"
									+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
									+ "This is auto-generated email, do not reply on this.</body></html>";
							if (uc.getIsQueryApproved().equals("Y")) {
								notificationEmailMessage = notificationEmailMessage.toString().replace("?",
										" Approved and Re-evaluated ");
							} else if (uc.getIsQueryApproved().equals("N")) {
								notificationEmailMessage = notificationEmailMessage.toString().replace("?",
										" Rejected ");
							}
							result = userService.findEmailByUserName(userList);
							Map<String, String> email = result.get("emails");
							Map<String, String> mobiles = new HashMap();
							logger.info("email -----> " + email);
							logger.info("mobile -----> " + mobiles);
							logger.info("subject -----> " + subject);
							logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
							//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
						}
					}

				} else {
					User u = userService.findByUserName(username);
					String subject = " ICA Marks Submitted: " + icaDB.getIcaName();
					Map<String, Map<String, String>> result = null;
					List<String> userList = new ArrayList<String>();
					userList.add(icaDB.getCreatedBy());
					String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName()
							+ " <br>"
							// + "<b>ICA Description: </b>"+ icaBeanDB.getIcaDesc() +"<br><br>"
							+ "<b>Note: </b>This ICA marks is submitted by : ?? <br>"
							+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
							+ "This is auto-generated email, do not reply on this.</body></html>";

					if (!userList.isEmpty()) {
						notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
								+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Error in Sending Email to Student--->" + e);
				setError(redirectAttributes, "Error in Sending Email to Student");
			}
			return "redirect:/showEvaluatedInternalMarks";
		} catch (Exception ex) {
			setError(redirectAttributes, "Error in Submitting ICA Marks");
			logger.error("Exception", ex);

			return "redirect:/evaluateIca";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/uploadStudentCompMarksExcel", method = { RequestMethod.POST })
	public String uploadStudentCompMarksExcel(@ModelAttribute Test test, @RequestParam("file") MultipartFile file,
			@RequestParam String saveAs, @RequestParam String icaId, @RequestParam String compId, Model m,
			RedirectAttributes redirectAttributes, Principal principal) {

		redirectAttributes.addAttribute("icaId", icaId);

		IcaBean icaBeanDB = icaBeanService.findByID(Long.valueOf(icaId));

		try {
			String username = principal.getName();

			List<String> icaCompStudList = new ArrayList<>();
			List<String> icaTotalStudList = new ArrayList<>();
			boolean isAllSubmitted = false;
			List<String> headers = new ArrayList<String>(Arrays.asList("Roll No", "Name", "SAPID", "Is Absent?"));
			// int count=3;

			IcaBean icaComp = icaBeanService.getComponentsByCompIdForIca(icaId, compId);

			headers.add(icaComp.getComponentName() + "(" + icaComp.getIcaCompMarks() + ")");
			// count++;

			headers.add("REMARKS");
			logger.info("headers are--->" + headers);
			ExcelReader excelReader = new ExcelReader();

			List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);

			logger.info("maps received" + maps);

			List<String> excelUserList = new ArrayList<>();

			List<Map<String, Object>> copy = new ArrayList<>();
			if (icaBeanDB.getEventId() == null && !("Y").equals(icaBeanDB.getIsIcaDivisionWise())
					&& !("Y").equals(icaBeanDB.getIsNonEventModule())) {

				List<String> ucListForModuleBatch = icaStudentBatchwiseService
						.getDistinctUsernamesByActiveIcaIdAndFaculty(icaBeanDB.getId(), username);

				List<String> ucListForModule = userCourseService.findDistinctStudentByModuleIdAndAcadYear(
						icaBeanDB.getModuleId(), icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(),
						icaBeanDB.getProgramId(), icaBeanDB.getId(), icaBeanDB.getCampusId());
				if (ucListForModuleBatch.size() > 0) {
					ucListForModule.clear();
					ucListForModule.addAll(ucListForModuleBatch);
				}
				logger.info("ucListSize-->" + ucListForModule.size());

				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					if (ucListForModule.contains(user)) {
						return true;
					} else {
						return false;
					}
				}).collect(Collectors.toList());

				if (ucListForModule.size() > copy.size() || maps.size() > ucListForModule.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");

					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}

				logger.info("moduleWise");
			} else if (icaBeanDB.getEventId() == null && !("Y").equals(icaBeanDB.getIsIcaDivisionWise())
					&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {

				List<String> ucListForModule = userCourseService
						.findDistinctStudentByModuleIdAndAcadYearForNonEventModule(icaBeanDB.getModuleId(),
								icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(), icaBeanDB.getProgramId(),
								icaBeanDB.getId(), icaBeanDB.getCampusId());

				logger.info("ucListSize-->" + ucListForModule.size());

				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					if (ucListForModule.contains(user)) {
						if (!excelUserList.contains(user)) {
							excelUserList.add(user);
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}).collect(Collectors.toList());

				if (ucListForModule.size() > copy.size() || maps.size() > ucListForModule.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}

				logger.info("moduleWise");
			} else if (icaBeanDB.getEventId() != null) {
				List<String> ucListForEvent = userCourseService.findDistinctStudentByEventIdAndAcadYear(
						icaBeanDB.getEventId(), icaBeanDB.getAcadYear(), icaBeanDB.getAcadSession(),
						icaBeanDB.getProgramId(), icaBeanDB.getId(), icaBeanDB.getCampusId());
				logger.info("ucListSize-->" + ucListForEvent.size());
				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					UserCourse uc = userCourseService.getMappingByUsernameAndCourse(user, icaBeanDB.getEventId());
					if (uc == null) {
						return false;
					} else {
						return true;
					}
				}).collect(Collectors.toList());

				if (ucListForEvent.size() > copy.size() || maps.size() > ucListForEvent.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}
				logger.info("courseWise");

			}

			logger.info("maps received after deletion" + copy);

			List<IcaComponentMarks> icaCompMarksList = new ArrayList<>();

			List<IcaTotalMarks> icaTotalMarksList = new ArrayList<>();
			for (Map<String, Object> mapper : copy) {
				if (mapper.get("Error") != null) {
					setError(redirectAttributes, (String) mapper.get("Error"));
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}

				double total = 0;
				int componentCount = 0;
				String isAbsent = mapper.get("Is Absent?").toString();
				String remarks = (String) mapper.get("REMARKS");
				if ("Y".equals(isAbsent)) {

					IcaComponentMarks icaCompMarks = new IcaComponentMarks();
					icaCompMarks.setUsername((String) mapper.get("SAPID"));
					icaCompMarks.setIcaId(icaId);
					icaCompMarks.setComponentId(compId);
					icaCompMarks.setMarks("0");
					if (remarks.isEmpty()) {
						icaCompMarks.setRemarks("Absent");
					} else {
						icaCompMarks.setRemarks(remarks);
					}
					icaCompMarks.setIsAbsent(isAbsent);
					if ("draft".equals(saveAs)) {
						icaCompMarks.setSaveAsDraft("Y");
					} else {
						icaCompMarks.setFinalSubmit("Y");
					}
					icaCompMarks.setActive("Y");
					icaCompMarks.setCreatedBy(principal.getName());
					icaCompMarks.setLastModifiedBy(principal.getName());
					if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
						icaCompStudList.add(icaCompMarks.getUsername());
					}
					// total = total + componentMarks;
					icaCompMarksList.add(icaCompMarks);

				} else {

					IcaComponentMarks icaCompMarks = new IcaComponentMarks();
					icaCompMarks.setUsername((String) mapper.get("SAPID"));

					String compMark = mapper.get(icaComp.getComponentName() + "(" + icaComp.getIcaCompMarks() + ")")
							.toString();
					if (!compMark.trim().isEmpty()) {
						componentCount++;
						if (!excelReader.ISVALIDINPUT(compMark)) {
							setError(redirectAttributes, "Input Mark is not valid for student:"
									+ (String) mapper.get("SAPID") + "-" + compMark);
							if (null != icaBeanDB.getIsNonEventModule()
									&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {
								return "redirect:/evaluateIcaForNonEventModule";
							} else {
								return "redirect:/evaluateIca";
							}
						} else {
							double componentMarks = Double.parseDouble((String) mapper
									.get(icaComp.getComponentName() + "(" + icaComp.getIcaCompMarks() + ")"));
							if (Double.parseDouble(icaComp.getIcaCompMarks()) < componentMarks) {
								setError(redirectAttributes, "Invalid Component" + icaComp.getComponentName()
										+ ": Marks For Student" + (String) mapper.get("SAPID"));
								if (null != icaBeanDB.getIsNonEventModule()
										&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {
									return "redirect:/evaluateIcaForNonEventModule";
								} else {
									return "redirect:/evaluateIca";
								}
							} else {
								icaCompMarks.setIcaId(icaId);
								icaCompMarks.setComponentId(compId);
								icaCompMarks.setMarks(String.valueOf(componentMarks));
								icaCompMarks.setRemarks(remarks);
								if ("draft".equals(saveAs)) {
									icaCompMarks.setSaveAsDraft("Y");
								} else {
									icaCompMarks.setFinalSubmit("Y");
								}
								icaCompMarks.setActive("Y");
								icaCompMarks.setCreatedBy(principal.getName());
								icaCompMarks.setLastModifiedBy(principal.getName());
								total = total + componentMarks;
								if (!icaCompStudList.contains(icaCompMarks.getUsername())) {
									icaCompStudList.add(icaCompMarks.getUsername());
								}
								icaCompMarksList.add(icaCompMarks);
							}
						}
					} else {
						if ("draft".equals(saveAs)) {
							continue;
						} else {
							setError(redirectAttributes, "Error! Please fill in all component marks for all students"
									+ (String) mapper.get("SAPID"));
							if (null != icaBeanDB.getIsNonEventModule()
									&& ("Y").equals(icaBeanDB.getIsNonEventModule())) {
								return "redirect:/evaluateIcaForNonEventModule";
							} else {
								return "redirect:/evaluateIca";
							}
						}
					}

				}

			}

			icaComponentMarksService.upsertBatch(icaCompMarksList);

			if (!icaId.isEmpty() && icaId != null) {
				List<String> studList = icaStudentBatchwiseService.getDistinctUsernamesByActiveIcaId(icaId);
				List<String> remainingStudList = icaStudentBatchwiseService
						.getDistinctActiveUsernamesOfRemainingFaculty(Long.valueOf(icaId), username);
				if (studList.size() > 0) {
					List<String> compUsersCopy = new ArrayList();
					List<String> compUsers = icaComponentMarksService.getDistinctUsernamesByActiveIcaId(icaId, compId);
					compUsers.addAll(icaCompStudList);
					compUsersCopy = compUsers.stream().distinct().collect(Collectors.toList());
					if ("draft".equals(saveAs)) {
						if (studList.size() != compUsersCopy.size()) {
							isAllSubmitted = false;
							icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", icaId, "N", compId);
						} else {

							isAllSubmitted = false;
							icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("DRAFT", icaId, "Y", compId);
						}
					} else if ("submit".equals(saveAs)) {
						if (studList.size() != compUsersCopy.size()) {
							isAllSubmitted = false;
							/*
							 * icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT", icaId,
							 * "N");
							 */
							icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList, icaId, compId);
						} else {
							List<String> finalSubmitCompoMarksList = icaComponentMarksService
									.getFinalSubmitByIcaIdAndUserList(icaId, remainingStudList, compId);
							if (finalSubmitCompoMarksList.size() == 1) {
								if (("Y").equalsIgnoreCase(finalSubmitCompoMarksList.get(0))) {
									isAllSubmitted = true;
									icaComponentMarksService.updateSaveAsDraftOrFinalSubmit("SUBMIT", icaId, "Y",
											compId);
								} else {
									isAllSubmitted = false;
									icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList, icaId,
											compId);
								}
							} else {
								isAllSubmitted = false;
								icaComponentMarksService.updateFinalSubmitByUserList(icaCompStudList, icaId, compId);
							}

						}
					}
					logger.info("is all submitted to true" + isAllSubmitted);

				} else {
					if ("submit".equals(saveAs)) {
						isAllSubmitted = true;
					}
				}

				if (isAllSubmitted) {

					icaComponentService.updateIcaCompToSubmitted(icaId, compId);

					int getTotalComps = icaComponentService.getTotalComponents(icaId);
					int getSubmittedComps = icaComponentService.getSubmittedComponents(icaId);
					logger.info("get Total Comps" + getTotalComps + " get Submitted Comps" + getSubmittedComps);
					if (getTotalComps == getSubmittedComps) {

						List<IcaComponentMarks> icaMarks = icaComponentMarksService.getIcaMarks(icaId);
						List<IcaTotalMarks> itmList = new ArrayList<>();
						for (IcaComponentMarks ica : icaMarks) {

							IcaTotalMarks itm = new IcaTotalMarks();
							itm.setUsername(ica.getUsername());
							itm.setIcaTotalMarks(ica.getMarks());
							itm.setIcaId(icaId);
							if ("Y".equals(icaBeanDB.getScaledReq())) {
								double totalMarks = Double.valueOf(icaBeanDB.getTotalMarks());
								double totalSMarks = Double.valueOf(icaBeanDB.getScaledMarks());
								double marksObt = Double.valueOf(ica.getMarks());
								double mulVal = marksObt * totalSMarks;
								double scaleMarks = mulVal / totalMarks;

								itm.setIcaScaledMarks(String.valueOf(scaleMarks));

							}
							itm.setActive("Y");
							itm.setFinalSubmit("Y");
							itm.setCreatedBy(username);
							itm.setLastModifiedBy(username);
							itmList.add(itm);
						}
						icaTotalMarksService.upsertBatch(itmList);
						icaBeanService.updateIcaToSubmitted(icaId, Utils.getInIST());

					}

				}
			}

			setSuccess(redirectAttributes, "File saved successfully");

			// sdf

			if ("submit".equals(saveAs)) {
				User u = userService.findByUserName(username);
				String subject = " ICA Marks Submitted : " + icaBeanDB.getIcaName();
				Map<String, Map<String, String>> result = null;
				List<String> userList = new ArrayList<String>();
				// List<String> acadSessionList = new ArrayList<>();
				userList.add(icaBeanDB.getCreatedBy());
				String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaBeanDB.getIcaName()
						+ " <br>"
						// + "<b>ICA Description: </b>"+ icaBeanDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This ICA marks is submitted by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				if (!userList.isEmpty()) {
					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}
			if ("submit".equals(saveAs)) {
				if (isAllSubmitted) {
					return "redirect:/showEvaluatedInternalMarks";
				} else {
					if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
						return "redirect:/evaluateIcaForNonEventModule";
					} else {
						return "redirect:/evaluateIca";
					}
				}
			} else {
				if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
					return "redirect:/evaluateIcaForNonEventModule";
				} else {
					return "redirect:/evaluateIca";
				}
			}

		} catch (Exception ex) {

			setError(redirectAttributes, "Error in uploading marks");

			logger.error("Exception", ex);
		}
		if (null != icaBeanDB.getIsNonEventModule() && ("Y").equals(icaBeanDB.getIsNonEventModule())) {
			return "redirect:/evaluateIcaForNonEventModule";
		} else {
			return "redirect:/evaluateIca";
		}

	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/publishIcaComponents", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishIcaComponents(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		/*
		 * List<IcaBean> submittedIcaList = icaBeanService .getSubmittedIca(username);
		 */

		List<IcaBean> submittedIcaList = icaBeanService.getSubmittedIcaComps(username);

		List<IcaBean> submittedIcaListForNonEvent = icaBeanService.getSubmittedIcaCompsNE(username);

		if (submittedIcaListForNonEvent.size() > 0) {
			for (IcaBean ica : submittedIcaListForNonEvent) {
				if (!submittedIcaList.stream()
						.filter(o -> o.getModuleId().equals(ica.getModuleId())
								&& o.getProgramId().equals(ica.getProgramId())
								&& o.getAcadYear().equals(ica.getAcadYear()))
						.findFirst().isPresent()) {
					submittedIcaList.add(ica);
				}
			}
		}

		//

		List<IcaBean> finalIcaBeanList = new ArrayList<>();
		logger.info("submitted size:" + submittedIcaList.size());
		for (IcaBean i : submittedIcaList) {

			boolean isIcaSentToTcs = isIcaMarksSentToTcs(i);

			if (isIcaSentToTcs == false) {
				List<IcaComponent> iComp = icaComponentService.icaComponentListByIcaId(i.getId());
				List<String> publishedIcaComps = new ArrayList<>();
				for (IcaComponent ic : iComp) {
					if ("Y".equals(ic.getIsSubmitted()) && !"Y".equals(ic.getIsPublished())) {
						i.setSubmittedComps(ic.getComponentName());
						i.setComponentId(ic.getComponentId());
					}
					if ("Y".equals(ic.getIsPublished())) {
						publishedIcaComps.add(ic.getComponentName());
					}
				}
				if (publishedIcaComps.size() > 0) {
					i.setPublishedComps(StringUtils.join(publishedIcaComps, ","));
				} else {
					i.setPublishedComps("NA");
				}
				finalIcaBeanList.add(i);
			}
		}
		submittedIcaList.clear();
		submittedIcaList.addAll(finalIcaBeanList);
		//
		for (IcaBean tb : submittedIcaList) {
			Course cBean = courseService.findByModuleIdAndAcadYear(tb.getModuleId(), tb.getAcadYear());
			if (cBean == null) {
				cBean = courseService.findByModuleIdAndAcadYearCode(tb.getAcadYear(), tb.getAcadYear());
			}
			if (cBean == null) {
				String moduleName = courseService.getModuleNameForNonEvent(tb.getModuleId());
				if (moduleName != null) {
					tb.setModuleName(moduleName);
				} else {
					tb.setModuleName("NA");
				}
			} else {
				tb.setModuleName(cBean.getModuleName() + "(" + cBean.getModuleAbbr() + ")");
			}
			tb.setFacultyName(teeBeanService.getFacultyNameByUsername(tb.getAssignedFaculty()));
		}
		if (submittedIcaList.size() == 0) {
			setNote(m, "Ica Not Found!!");
		}
		m.addAttribute("submittedIcaList", submittedIcaList);

		String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
		m.addAttribute("currentDate", Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
		return "ica/publishIcaComps";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/publishAllIcaComponents", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishAllIcaComponents(Model m, Principal principal, RedirectAttributes redirectAttrs) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		try {

			User u = userService.findByUserName(username);
			List<IcaBean> submittedIcaList = icaBeanService.getSubmittedIcaComps(username);

			List<IcaBean> submittedIcaListForNonEvent = icaBeanService.getSubmittedIcaCompsNE(username);

			if (submittedIcaListForNonEvent.size() > 0) {
				for (IcaBean ica : submittedIcaListForNonEvent) {
					if (!submittedIcaList.stream()
							.filter(o -> o.getModuleId().equals(ica.getModuleId())
									&& o.getProgramId().equals(ica.getProgramId())
									&& o.getAcadYear().equals(ica.getAcadYear()))
							.findFirst().isPresent()) {
						submittedIcaList.add(ica);
					}
				}
			}

			//

			List<IcaBean> finalIcaBeanList = new ArrayList<>();
			logger.info("submitted size:" + submittedIcaList.size());
			for (IcaBean i : submittedIcaList) {

				boolean isIcaSentToTcs = isIcaMarksSentToTcs(i);

				if (isIcaSentToTcs == false) {
					List<IcaComponent> iComp = icaComponentService.icaComponentListByIcaId(i.getId());
					List<String> publishedIcaComps = new ArrayList<>();
					for (IcaComponent ic : iComp) {
						if ("Y".equals(ic.getIsSubmitted()) && !"Y".equals(ic.getIsPublished())) {
							i.setComponentId(ic.getComponentId());
							finalIcaBeanList.add(i);
						}

					}

				}
			}
			submittedIcaList.clear();
			submittedIcaList.addAll(finalIcaBeanList);

			for (IcaBean i : submittedIcaList) {

				String formatDate = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());
				icaComponentService.updateIcaCompsToPublished(String.valueOf(i.getId()), i.getComponentId(),
						Utils.getInIST(), formatDate);

				int getMaxSeqNo = icaComponentService.getMaxSeqNo(Long.valueOf(i.getId()));
				IcaComponent getCompBean = icaComponentService.getCompBean(i.getId(), Long.valueOf(i.getComponentId()));
				if (getMaxSeqNo == Integer.parseInt(getCompBean.getSequenceNo())) {

					icaBeanService.updateIcaToPublished(String.valueOf(i.getId()), Utils.getInIST(), formatDate);
				}

				IcaBean icaDB = icaBeanService.findByID(Long.valueOf(i.getId()));
				/********* StudentList *********/
				List<String> userList = new ArrayList<String>();
				List<UserCourse> studentsListForIca = new ArrayList<>();
				if (icaDB.getEventId() != null) {
					studentsListForIca = userCourseService.findStudentByEventIdAndAcadYear(icaDB.getEventId(),
							icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
							icaDB.getCampusId(), "Y", i.getComponentId());
				} else {
					studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYear(icaDB.getModuleId(),
							icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
							icaDB.getCampusId(), "Y", i.getComponentId());
				}
				// for(UserCourse uc: studentsListForIca){
				// userList.add(uc.getUsername());
				// }

				// Email for student
				String moduleName = "";
				if (icaDB.getIsNonEventModule().equals("Y")) {
					moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
				} else {
					moduleName = courseService.getModuleName(icaDB.getModuleId());
				}
				PredefinedIcaComponent pdBean = predefinedIcaComponentService
						.findByID(Long.valueOf(i.getComponentId()));
				String subject = " ICA Component marks is published for Subject : " + moduleName + " & for component:"
						+ pdBean.getComponentName();
				Map<String, Map<String, String>> result = null;
				String notificationEmailMessage = "<html><body>"
						// + "<b>ICA Name: </b>"+icaDB.getIcaName()+" <br>"
						// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						// + "ICA Marks is published.<br>"
						+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";
				for (UserCourse uc : studentsListForIca) {
					userList.clear();
					userList.add(uc.getUsername());
					if (!userList.isEmpty()) {
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}

				// Email For faculty
				subject = " ICA Component marks is published for Subject : " + moduleName + " & for component:"
						+ pdBean.getComponentName();
				;
				result = null;
				userList.clear();
				if (null != icaDB.getAssignedFaculty()) {
					if (icaDB.getAssignedFaculty().contains(",")) {
						List<String> faculties = new ArrayList<String>();
						faculties = Arrays.asList(icaDB.getAssignedFaculty().split(","));
						userList.addAll(faculties);
					} else {
						userList.add(icaDB.getAssignedFaculty());
					}
				}
				// userList.add(icaDB.getAssignedFaculty());
				notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName() + " <br>"
				// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This ICA is published by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				for (String s1 : userList) {
					if (!userList.isEmpty()) {
						List<String> userList1 = new ArrayList<String>();
						userList1.add(s1);

						notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
								+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
						result = userService.findEmailByUserName(userList1);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						logger.info("email -----> " + email);
						logger.info("mobile -----> " + mobiles);
						logger.info("subject -----> " + subject);
						logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
						//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}

			}

			setSuccess(redirectAttrs, "All Ica's Published");

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in publishing all Ica");
			logger.error("Exception", ex);
		}

		return "redirect:/publishIcaComponents";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_FACULTY" })
	@RequestMapping(value = "/publishOneIcaComponents", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String publishOneIcaComponents(Model m, Principal principal, @RequestParam String id,
			@RequestParam String compId) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		try {

			Date currDate = Utils.getInIST();
			String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", currDate);
			String publishedDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate);

			int updated = icaComponentService.updateIcaCompsToPublished(id, compId, currDate, publishedDate);

			int getMaxSeqNo = icaComponentService.getMaxSeqNo(Long.valueOf(id));
			IcaComponent getCompBean = icaComponentService.getCompBean(Long.valueOf(id), Long.valueOf(compId));
			if (getMaxSeqNo == Integer.parseInt(getCompBean.getSequenceNo())) {

				icaBeanService.updateIcaToPublished(id, currDate, publishedDate);
			}

			User u = userService.findByUserName(username);
			IcaBean icaDB = icaBeanService.findByID(Long.valueOf(id));
			/********* StudentList *********/
			List<String> userList = new ArrayList<String>();
			List<UserCourse> studentsListForIca = new ArrayList<>();
			if (icaDB.getEventId() != null) {
				studentsListForIca = userCourseService.findStudentByEventIdAndAcadYear(icaDB.getEventId(),
						icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
						icaDB.getCampusId(), "Y", compId);
			} else {
				studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYear(icaDB.getModuleId(),
						icaDB.getAcadYear(), icaDB.getAcadSession(), icaDB.getProgramId(), icaDB.getId(),
						icaDB.getCampusId(), "Y", compId);
			}
			// Email for student
			String moduleName = "";
			if (icaDB.getIsNonEventModule().equals("Y")) {
				moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
			} else {
				moduleName = courseService.getModuleName(icaDB.getModuleId());
			}
			PredefinedIcaComponent pdBean = predefinedIcaComponentService.findByID(Long.valueOf(compId));
			String subject = " ICA Componenent marks is published for Subject : " + moduleName + " and for component:"
					+ pdBean.getComponentName();
			logger.info("subject:" + subject);
			Map<String, Map<String, String>> result = null;
			String notificationEmailMessage = "<html><body>"
					// + "<b>ICA Name: </b>"+icaDB.getIcaName()+" <br>"
					// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					// + "ICA Marks is published.<br>"
					+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			for (UserCourse uc : studentsListForIca) {
				userList.clear();
				userList.add(uc.getUsername());
				if (!userList.isEmpty()) {
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}

			// Email For faculty
			subject = " ICA Component marks is published for Subject : " + moduleName + " and for component:"
					+ pdBean.getComponentName();
			;
			result = null;
			userList.clear();
			if (null != icaDB.getAssignedFaculty()) {
				if (icaDB.getAssignedFaculty().contains(",")) {
					List<String> faculties = new ArrayList<String>();
					faculties = Arrays.asList(icaDB.getAssignedFaculty().split(","));
					userList.addAll(faculties);
				} else {
					userList.add(icaDB.getAssignedFaculty());
				}
			}
			// userList.add(icaDB.getAssignedFaculty());
			notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName() + " <br>"
			// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					+ "<b>Note: </b>This ICA Component is published by : ?? <br>"
					+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";
			for (String s : userList) {
				if (!userList.isEmpty()) {
					List<String> userList1 = new ArrayList<String>();
					userList1.add(s);

					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList1);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}
			return "success";
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "error";
		}

	}
	
	@Secured({ "ROLE_STUDENT" })
	@RequestMapping(value = "/raiseQueryForStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String raiseQueryForStudent(Model m, Principal principal, @RequestParam String id,
			@RequestParam(required = false) String compId, @RequestParam String query, RedirectAttributes redirectAttrs) {
		logger.info("INSIDE /raiseQueryForStudent");
		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		if (!query.isEmpty() && query != null) {
			query = query.trim();
		}
		try {
			logger.info("Raising Query...");
			HtmlValidation.checkHtmlCode(id);
			HtmlValidation.checkHtmlCode(compId);
			HtmlValidation.checkHtmlCode(query);
			
			
			BusinessBypassRule.validateAlphaNumeric(query);
			
			IcaBean icaBean = icaBeanService.findByID(Long.valueOf(id));
			if ("Y".equals(icaBean.getIsPublishCompWise())) {
				if (compId != null) {
					icaComponentMarksService.updateRaiseQuery(id, username, query, compId);
				} else {
					compId = icaComponentService.getSubmittedIcaComponent(Long.valueOf(id)).getComponentId();
					icaComponentMarksService.updateRaiseQuery(id, username, query, compId);
				}
			} else {
				icaTotalMarksService.updateRaiseQuery(id, username, query);
			}

			IcaQueries icaQDB = new IcaQueries();
			if (compId == null) {

				icaQDB = icaQueriesService.findByIcaId(Long.valueOf(id));
			} else {
				icaQDB = icaQueriesService.findByIcaIdAndCompId(id, compId);
			}

			if (icaQDB == null) {
				IcaQueries icaQ = new IcaQueries();
				icaQ.setIcaId(id);
				icaQ.setCreatedBy(username);
				icaQ.setLastModifiedBy(username);
				icaQ.setComponentId(compId);
				icaQ.setIsReEvaluated("N");
				icaQueriesService.upsert(icaQ);
			} else {
				icaQDB.setComponentId(compId);
				icaQDB.setIsReEvaluated("N");
				icaQueriesService.upsert(icaQDB);
			}

			List<String> userList = new ArrayList<String>();
			IcaBean icaDB = icaBeanService.findByID(Long.valueOf(id));
			User u = userService.findByUserName(username);
			String moduleName = "";
			if (icaDB.getIsNonEventModule().equals("Y")) {
				moduleName = courseService.getModuleNameForNonEvent(icaDB.getModuleId());
			} else {
				moduleName = courseService.getModuleName(icaDB.getModuleId());
			}
			String subject = " ICA Query raised for subject " + moduleName;
			Map<String, Map<String, String>> result = null;

			userList.add(icaDB.getCreatedBy());
			if (null != icaDB.getAssignedFaculty()) {
				if (icaDB.getAssignedFaculty().contains(",")) {
					String facultyId = icaStudentBatchwiseService.getFacultyIdByusernameAndIcaId(icaDB.getId(),
							username);
					userList.add(facultyId);
				} else {
					userList.add(icaDB.getAssignedFaculty());
				}
			}
			String notificationEmailMessage = "<html><body>" + "<b>ICA Name: </b>" + icaDB.getIcaName() + " <br>"
					+ "<b>Query: </b> " + query + "<br>" + "<b>Query Raised By: </b>Name: " + u.getFirstname() + " "
					+ u.getLastname() + " ( " + username + " )<br><br>"
					+ "<b>Note: </b> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			for (String s : userList) {
				if (!userList.isEmpty()) {
					List<String> userList1 = new ArrayList<String>();
					userList1.add(s);

					result = userService.findEmailByUserName(userList1);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					logger.info("email -----> " + email);
					logger.info("mobile -----> " + mobiles);
					logger.info("subject -----> " + subject);
					logger.info("notificationEmailMessage -----> " + notificationEmailMessage);
					//notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}
			return "success";
		} 
		catch (ValidationException ve) {
			logger.error("ValidationException", ve);
			return "validationError";
		} 
		catch (Exception ex) {
			logger.error("Exception", ex);
			return "error";
		}

	}
	
	
	@RequestMapping(value = "/getEvaluationStatusOfICAByFaculty", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getEvaluationStatusOfICAByFaculty(@RequestParam(name = "id") String id, Principal principal) {

		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			Token userdetails1 = (Token) principal;
			List<IcaTotalMarks> facultyStatus =  icaTotalMarksService.getFacultyEvaluationStatus(id);
			json = mapper.writeValueAsString(facultyStatus);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return json;
	}
}
