package com.spts.lms.web.controllers;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spts.lms.web.utils.ValidationException;

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
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.tee.TeeBean;
import com.spts.lms.beans.tee.TeeQueries;
import com.spts.lms.beans.tee.TeeStudentBatchwise;
import com.spts.lms.beans.tee.TeeTotalMarks;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.tee.TeeBeanService;
import com.spts.lms.services.tee.TeeQueriesService;
import com.spts.lms.services.tee.TeeStudentBatchwiseService;
import com.spts.lms.services.tee.TeeTotalMarksService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.BusinessBypassRule;
import com.spts.lms.web.utils.HtmlValidation;
import com.spts.lms.web.utils.Utils;

@Controller
public class TeeController extends BaseController {

	@Autowired
	TeeBeanService teeBeanService;

	@Autowired
	ProgramService programService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	TeeTotalMarksService teeTotalMarksService;

	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	TeeStudentBatchwiseService teeStudentBatchwiseService;

	@Autowired
	TeeQueriesService teeQueriesService;

	@Autowired
	UserService userService;

	@Autowired
	Notifier notifier;

	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Value("${file.base.directory}")
	private String baseDir;

	@Value("${file.base.directory.s3}")
	private String baseDirS3;

	@Value("${app}")
	private String app;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@ModelAttribute("campusList")
	public List<ProgramCampus> getCampusList() {

		return programCampusService.getCampusList();
	}

	@ModelAttribute("campusListByProgram")
	public List<ProgramCampus> getCampusListByProgram(Principal p) {
		Token t = (Token) p;
		return programCampusService.getCampusListByProgram(t.getProgramId());
	}

	private static final Logger logger = Logger.getLogger(TeeController.class);

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTeeForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTeeForm(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Add TEE", false, false));
		m.addAttribute("multipleFaculty", "true");
		TeeBean tee = new TeeBean();
		if (id != null) {
			tee = teeBeanService.findByID(Long.valueOf(id));
			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			if ("Y".equals(tee.getIsTeeDivisionWise()) && tee.getParentTeeId() == null) {
				m.addAttribute("isParentTee", "true");
				m.addAttribute("isGradingStart",
						teeTotalMarksService.checkWhetherGradingStartOrNotP(String.valueOf(tee.getId())));

			} else {
				m.addAttribute("isParentTee", "false");
				m.addAttribute("isGradingStart",
						teeTotalMarksService.checkWhetherGradingStartOrNot(String.valueOf(tee.getId())));

			}
			if (currentDate.compareTo(tee.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot update, TEE Deadline is over");
				return "redirect:/searchTeeList";
			} else if ("Y".equals(tee.getIsSubmitted())) {
				setNote(redirectAttrs, "Cannot update, TEE is Already Evaluated");
				return "redirect:/searchTeeList";
			}
			m.addAttribute("edit", "true");
			m.addAttribute("moduleName", courseService.getModuleName(tee.getModuleId()));
			m.addAttribute("programList", programService.getProgramListByIds(tee.getProgramId()));
			m.addAttribute("facultyList",
					userCourseService.findAllFacultyWithModuleIdICA(tee.getModuleId(), tee.getAcadYear()));
			m.addAttribute("webPage", new WebPage("searchCourse", "Update TEE", false, false));

			if (null != tee.getAssignedFaculty()) {
				if (tee.getAssignedFaculty().contains(",")) {
					m.addAttribute("selectedFacultyList",
							userCourseService.findAllFacultyByFacultyIds(tee.getAssignedFaculty()));
					m.addAttribute("multipleFaculty", "true");
				} else {
					if ("Y".equals(tee.getIsTeeDivisionWise()) && null == tee.getParentTeeId()) {
						m.addAttribute("multipleFaculty", "false");
					} else if ("Y".equals(tee.getIsTeeDivisionWise()) && null != tee.getParentTeeId()) {
						m.addAttribute("multipleFaculty", "false");
					} else {
						m.addAttribute("multipleFaculty", "true");
						m.addAttribute("selectedFacultyList",
								userCourseService.findAllFacultyByFacultyIds(tee.getAssignedFaculty()));
					}
				}
			}

			if ("Y".equals(tee.getIsTeeDivisionWise()) && tee.getParentTeeId() == null) {
				setNote(m, "This is Division-Wise TEE,Any Update in this, will reflect to all TEE Divisions");
				m.addAttribute("teeBean", tee);
				return "tee/createTeeForDivision";
			}
		}
		List<String> acadYearCodeList = courseService.findAcadYearCode();

		m.addAttribute("acadYearCodeList", acadYearCodeList);

		m.addAttribute("teeBean", tee);

		return "tee/createTee";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTee", method = RequestMethod.POST)
	public String addTee(Model m, Principal principal, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs) {

		try {
			
			logger.info("Validating /addTee...");
			HtmlValidation.validateHtml(teeBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(teeBean.getTeeName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", teeBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year");
			}
			Course acadSession = courseService.checkIfExistsInDB("acadSession", teeBean.getAcadSession());
			if(acadSession == null) {
				throw new ValidationException("Invalid Acad Session");
			}
			Course campusId = courseService.checkIfExistsInDB("campusId", teeBean.getCampusId());
			if(campusId == null) {
				throw new ValidationException("Invalid Campus");
			}
			Course moduleId = courseService.checkIfExistsInDB("moduleId", teeBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module");
			}
			logger.info("create Tee selected programs are " + teeBean.getProgramId());
			List<String> progIds = null;
			if(teeBean.getProgramId().contains(",")) {
				progIds = Arrays.asList(teeBean.getProgramId().split(","));
				for(String programId : progIds) {
					Course progId = courseService.checkIfExistsInDB("programId", programId);			
					if(progId == null) {
						throw new ValidationException("Invalid Program");
					}
				}
			} else {
				Course progId = courseService.checkIfExistsInDB("programId", teeBean.getProgramId());
				if(progId == null) {
					throw new ValidationException("Invalid Program");
				}
			}
			
			List<String> assignedFaculties = null;
			if(teeBean.getAssignedFaculty().contains(",")) {
				assignedFaculties = Arrays.asList(teeBean.getAssignedFaculty().split(","));
				for(String assignedFaculty : assignedFaculties) {
					UserCourse courseId = userCourseService.getFacultyCourseId(assignedFaculty,teeBean.getModuleId());
					logger.info("courseID is " + courseId);
					UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(assignedFaculty, String.valueOf(courseId.getCourseId()));
		            if(null == userccourse) {
		                  throw new ValidationException("Invalid faculty selected.");
		            }
				}
			} else {
				UserCourse courseId = userCourseService.getFacultyCourseId(teeBean.getAssignedFaculty(),teeBean.getModuleId());
				UserCourse userccourse = userCourseService.getMappingByUsernameAndCourse(teeBean.getAssignedFaculty(), String.valueOf(courseId.getCourseId()));
	            if(null == userccourse) {
	                  throw new ValidationException("Invalid faculty selected.");
	            }
			}
			
			
            
			BusinessBypassRule.validateNumeric(teeBean.getExternalMarks());
			BusinessBypassRule.validateNumeric(teeBean.getExternalPassMarks());
			if(!teeBean.getInternalMarks().isEmpty()) {
				BusinessBypassRule.validateNumeric(teeBean.getInternalMarks());
			}
			if(!teeBean.getInternalPassMarks().isEmpty()) {
				BusinessBypassRule.validateNumeric(teeBean.getInternalPassMarks());
			}
			if(!teeBean.getTotalMarks().isEmpty()) {
				BusinessBypassRule.validateNumeric(teeBean.getTotalMarks());
			}
			
			Utils.validateStartAndEndDates(teeBean.getStartDate(), teeBean.getEndDate());
			logger.info("Scaled Req is " + teeBean.getScaledReq());
			
			BusinessBypassRule.validateYesOrNo(teeBean.getAutoAssignMarks());
			
			if (teeBean.getScaledReq() == null || teeBean.getScaledReq().equals("N")) {
				teeBean.setScaledReq("N");
				teeBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumeric(teeBean.getScaledMarks());
			}
			if(!teeBean.getTeeDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(teeBean.getTeeDesc());
			}
			logger.info("Validation Done");
			
			String username = principal.getName();

			teeBean.setCreatedBy(username);
			teeBean.setLastModifiedBy(username);
			teeBean.setActive("Y");
			teeBean.setIsNonEventModule("N");

			if (teeBean.getScaledReq() == null || teeBean.getScaledReq().equals("N")) {
				teeBean.setScaledReq("N");
				teeBean.setScaledMarks(null);
			}

			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(teeBean.getStartDate(), teeBean.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addTeeForm";
//			}
			/* New Audit changes end */
			List<TeeBean> teeDBList = teeBeanService.checkAlreadyExistICAList(teeBean.getModuleId(),
					teeBean.getAcadYear(), teeBean.getCampusId(), teeBean.getAcadSession());

			for (TeeBean teeDB : teeDBList) {
				if (teeDB != null) {
					if (teeBean.getProgramId().contains(",")) {
						List<String> programIds = Arrays.asList(teeBean.getProgramId().split(","));
						if (teeDB.getProgramId().contains(",")) {
							for (String db : Arrays.asList(teeDB.getProgramId().split(","))) {
								if (programIds.contains(db)) {
									setError(redirectAttrs, "TEE Already Exist");
									return "redirect:/addTeeForm";
								}
							}
						} else {
							if (programIds.contains(teeDB.getProgramId())) {
								setError(redirectAttrs, "TEE Already Exist");
								return "redirect:/addTeeForm";
							}
						}
					} else {
						if (teeDB.getProgramId().contains(",")) {
							if (Arrays.asList(teeDB.getProgramId().split(",")).contains(teeBean.getProgramId())) {
								setError(redirectAttrs, "TEE Already Exist");
								return "redirect:/addTeeForm";
							}
						} else {
							if (teeBean.getProgramId().equals(teeDB.getProgramId())) {
								setError(redirectAttrs, "TEE Already Exist");
								return "redirect:/addTeeForm";
							}
						}
					}
				}
			}
			teeBeanService.insertWithIdReturn(teeBean);

			List<String> userList = new ArrayList<String>();
			if (teeBean.getAssignedFaculty().contains(",")) {
				List<String> faculties = new ArrayList<String>();
				faculties = Arrays.asList(teeBean.getAssignedFaculty().split(","));
				userList.addAll(faculties);
			} else {
				userList.add(teeBean.getAssignedFaculty());
			}

			User u = userService.findByUserName(username);
			String subject = " New TEE: " + teeBean.getTeeName();
			Map<String, Map<String, String>> result = null;
			List<String> acadSessionList = new ArrayList<>();
			String notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeBean.getTeeName() + " <br>"
					+ "<b>TEE Description: </b>" + teeBean.getTeeDesc() + "<br><br>"
					+ "<b>Note: </b>This TEE is created by : ?? <br>"
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
					notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}

			setSuccess(redirectAttrs, "TEE Added Successfully");
			redirectAttrs.addFlashAttribute("teeBean", teeBean);
			if (teeBean.getAssignedFaculty().contains(",")) {
				return "redirect:/createStudentGroupFormForTee";
			} else {
				return "redirect:/searchTeeList";
			}

		}

		catch (Exception ex) {

			logger.error("Excption while creating ICA", ex);

			setError(redirectAttrs, "Error While Creating TEE,TEE May have already created");
			return "redirect:/addTeeForm";
		}

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/searchTeeList", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchTeeList(Model m, Principal principal, @RequestParam(required = false) String teeId) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("searchCourse", "Search TEE", true, false));
		List<TeeBean> teeList = new ArrayList<>();
		String role = "";
		boolean isQueryRaised = false;
		if (teeId != null && userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			teeList = teeBeanService.findDivisionWiseTeeListByParentTee(teeId);
			m.addAttribute("teeId", teeId);

		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				role = Role.ROLE_FACULTY.name();
				teeList = teeBeanService.findTeeListByProgramAndUsername(userdetails1.getProgramId(), role,
						principal.getName());

			} else {
				role = Role.ROLE_ADMIN.name();
				teeList = teeBeanService.findTeeListByProgramAndUsername(userdetails1.getProgramId(), role,
						principal.getName());

				for (TeeBean t : teeList) {
					if ("Y".equals(t.getIsTeeDivisionWise()) && t.getParentTeeId() == null) {
						List<String> getStatusForTee = teeBeanService.getTeeStatusesForDivisionTee(t.getId());
						if (getStatusForTee.size() == 1) {
							String status = getStatusForTee.get(0);
							if ("Y".equals(status)) {
								t.setIsSubmitted("Y");
							}
						}
					}
				}
			}
		}

		// New Changes on 08-10-2020 to check tcs flag

		List<TeeBean> teeFinalList = new ArrayList<>();
		logger.info("tee final list---" + teeFinalList);
		for (TeeBean tee : teeList) {

			boolean checkTcsFlagForIca = isTeeMarksSentToTcs(tee);
			if (checkTcsFlagForIca == false) {
				teeFinalList.add(tee);
			}
		}

		teeList.clear();
		teeList.addAll(teeFinalList);

		//

		for (TeeBean tb : teeList) {
			if (tb.getTeeQueryId() != null) {
				isQueryRaised = true;
			}
			if (tb.getProgramId().contains(",")) {
				if (null != tb.getCampusId()) {
					tb.setCampusName(programCampusService.getCampusByCampusId(tb.getCampusId()));
				}
				tb.setProgramName(programService.getProgramNamesForIca(tb.getProgramId()));
			}
		}
		m.addAttribute("isQueryRaised", isQueryRaised);
		m.addAttribute("teeList", teeList);
		m.addAttribute("Program_Name", userdetails1.getProgramName());
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			return "tee/searchTeeListForFaculty";
		} else {
			return "tee/searchTeeList";
		}

	}

	public boolean isTeeMarksSentToTcs(TeeBean i) {
		logger.info("Method ccaclled tee");
		if ("Y".equals(i.getIsTeeDivisionWise()) && i.getParentTeeId() == null) {
			List<TeeBean> divWiseTee = teeBeanService.findDivisionWiseTeeListByParentTee(String.valueOf(i.getId()));

			if (divWiseTee.size() == 0) {
				return false;
			}
			for (TeeBean divIca : divWiseTee) {
				long divId = divIca.getId();
				int noOfStudentsInIca = teeTotalMarksService.getNoOfStudentsForTee(divId);
				int noOfStudentsMarksSent = teeTotalMarksService.getCountOfTcsFlagSentForTee(divId);
				if (noOfStudentsInIca > 0) {
					if (noOfStudentsInIca != noOfStudentsMarksSent) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		} else {
			long divId = i.getId();
			int noOfStudentsInIca = teeTotalMarksService.getNoOfStudentsForTee(divId);
			int noOfStudentsMarksSent = teeTotalMarksService.getCountOfTcsFlagSentForTee(divId);
			if (noOfStudentsInIca > 0) {
				if (noOfStudentsInIca != noOfStudentsMarksSent) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/updateTee", method = RequestMethod.POST)
	public String updateTee(Model m, Principal principal, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs) {

		try {

			TeeBean teeBeanDAO = teeBeanService.findByID(teeBean.getId());

			redirectAttrs.addAttribute("id", teeBean.getId());

			teeBean.setActive("Y");
			teeBean.setLastModifiedBy(principal.getName());
			if (teeBean.getScaledReq() == null) {
				teeBean.setScaledMarks(null);
			}
			if (teeBean.getInternalMarks() == null) {
				teeBean.setInternalMarks(teeBeanDAO.getInternalMarks());
			}
			if (teeBean.getInternalPassMarks() == null) {
				teeBean.setInternalPassMarks(teeBeanDAO.getInternalPassMarks());
			}
			if (teeBean.getExternalMarks() == null) {
				teeBean.setExternalMarks(teeBeanDAO.getExternalMarks());
			}
			if (teeBean.getExternalPassMarks() == null) {
				teeBean.setExternalPassMarks(teeBeanDAO.getExternalPassMarks());
			}
			if (teeBean.getTotalMarks() == null) {
				teeBean.setTotalMarks(teeBeanDAO.getTotalMarks());
			}
			if (teeBean.getAssignedFaculty() == null) {
				teeBean.setAssignedFaculty(teeBeanDAO.getAssignedFaculty());
			}
			if (null != teeBeanDAO.getIsNonEventModule() && ("Y").equals(teeBeanDAO.getIsNonEventModule())) {
				teeBean.setIsNonEventModule(teeBeanDAO.getIsNonEventModule());
			}

			if ("Y".equals(teeBeanDAO.getIsTeeDivisionWise()) && teeBeanDAO.getParentTeeId() == null) {
				List<TeeBean> updatedTeeBeanList = new ArrayList<>();
				List<TeeBean> teeListByParentTeeId = teeBeanService.getTeeIdsByParentTeeIds(teeBean.getId());
				List<TeeBean> submittedTeeListByParentTeeId = teeBeanService
						.getSubmittedTeeIdsByParentTeeIds(teeBean.getId());
				if (teeListByParentTeeId.size() == submittedTeeListByParentTeeId.size()) {
					setError(redirectAttrs, "TEE Cannot be updated since it is already submitted");
				} else {
					teeBeanService.update(teeBean);

					for (TeeBean ib : teeListByParentTeeId) {
						TeeBean ibNew = new TeeBean();
						Long ibId = ib.getId();

						String ibParentId = ib.getParentTeeId();
						String ibFacultyId = ib.getAssignedFaculty();
						String eventId = ib.getEventId();
						ibNew = teeBean;
						ibNew.setId(ibId);
						ibNew.setParentTeeId(ibParentId);
						ibNew.setAssignedFaculty(ibFacultyId);
						ibNew.setEventId(eventId);
						teeBeanService.update(ibNew);

					}
					setSuccess(redirectAttrs, "TEE Updated Successfully");

				}

			} else {
				int updated = 0;
				if (!("Y").equals(teeBeanDAO.getIsTeeDivisionWise())) {
					List<TeeStudentBatchwise> studentListForCheck = teeStudentBatchwiseService
							.getAllByActiveTeeId(String.valueOf(teeBean.getId()));
					int count = 0;
					List<String> dbFacultyList = Arrays.asList(teeBeanDAO.getAssignedFaculty());
					List<String> facultyList = Arrays.asList(teeBean.getAssignedFaculty());
					for (String faculty : facultyList) {
						if (!dbFacultyList.contains(faculty)) {
							count++;
						}
					}
					if (count > 0) {
						if (studentListForCheck.size() > 0) {
							teeStudentBatchwiseService.deleteAllActiveByTeeId(String.valueOf(teeBean.getId()));
						}
					}

					if (teeBean.getAssignedFaculty().contains(",")) {
						updated = teeBeanService.update(teeBean);
						String checkEvaluation = teeTotalMarksService
								.checkWhetherGradingStartOrNot(String.valueOf(teeBean.getId()));
						if (checkEvaluation.equals("t")) {
							return "redirect:/searchTeeList";
						}
						return "redirect:/createStudentGroupFormForTee";
					}

					updated = teeBeanService.update(teeBean);
				} else {
					updated = teeBeanService.update(teeBean);
				}

				if (updated == 0) {

					if (teeBeanDAO.getIsSubmitted().equals("Y")) {
						setError(redirectAttrs, "TEE Cannot be updated since it is already submitted");
					}
				} else {

					setSuccess(redirectAttrs, "TEE Updated Successfully");
				}

			}

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Updating ICA");
			logger.error("Exception", ex);
		}

		return "redirect:/searchTeeList";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/publishTeeForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishTeeForm(Model m, Principal principal) {
		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		List<TeeBean> submittedTeeList = new ArrayList<>();
		submittedTeeList = teeBeanService.getAllSubmittedTee(username);
		for (TeeBean tb : submittedTeeList) {
			if (tb.getProgramId().contains(",")) {
				tb.setProgramName(programService.getProgramNamesForIca(tb.getProgramId()));
			}
			tb.setFacultyName(teeBeanService.getFacultyNameByUsername(tb.getAssignedFaculty()));
		}

		//

		List<TeeBean> finalTeeBeanList = new ArrayList<>();
		for (TeeBean t : submittedTeeList) {

			boolean isTeeSentToTcs = isTeeMarksSentToTcs(t);

			if (isTeeSentToTcs == false) {
				finalTeeBeanList.add(t);
			}
		}
		submittedTeeList.clear();
		submittedTeeList.addAll(finalTeeBeanList);

		//

		m.addAttribute("submittedTeeList", submittedTeeList);
		m.addAttribute("Program_Name", userdetails1.getProgramName());
		String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
		m.addAttribute("currentDate", Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
		return "tee/publishTee";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/publishOneTee", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String publishOneTee(Model m, Principal principal, @RequestParam String id) {
		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		try {
			String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			int updated = teeBeanService.updateTeeToPublished(id, Utils.getInIST(),
					Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));

			User u = userService.findByUserName(username);
			TeeBean teeDB = teeBeanService.findByID(Long.valueOf(id));
			/********* StudentList *********/
			List<String> userList = new ArrayList<String>();
			List<UserCourse> studentsListForTee = new ArrayList<>();
			if (teeDB.getEventId() != null) {
				studentsListForTee = userCourseService.findStudentByEventIdAndAcadYear(teeDB.getEventId(),
						teeDB.getAcadYear(), teeDB.getAcadSession(), teeDB.getProgramId(), teeDB.getId(),
						teeDB.getCampusId(), "N", null);
			} else {
				studentsListForTee = userCourseService.findStudentByModuleIdAndAcadYear(teeDB.getModuleId(),
						teeDB.getAcadYear(), teeDB.getAcadSession(), teeDB.getProgramId(), teeDB.getId(),
						teeDB.getCampusId(), "N", null);
			}
			// Email for student
			String moduleName = courseService.getModuleName(teeDB.getModuleId());
			String subject = " TEE marks is published for Subject : " + moduleName;
			Map<String, Map<String, String>> result = null;
			String notificationEmailMessage = "<html><body>"
					// + "<b>ICA Name: </b>"+icaDB.getIcaName()+" <br>"
					// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					// + "ICA Marks is published.<br>"
					+ "<b>Note: </b>To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			for (UserCourse uc : studentsListForTee) {
				userList.clear();
				userList.add(uc.getUsername());
				if (!userList.isEmpty()) {
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}

			// Email For faculty
			subject = " TEE marks is published for Subject : " + moduleName;
			result = null;
			userList.clear();
			if (teeDB.getAssignedFaculty().contains(",")) {
				List<String> faculties = new ArrayList<String>();
				faculties = Arrays.asList(teeDB.getAssignedFaculty().split(","));
				userList.addAll(faculties);
			} else {
				userList.add(teeDB.getAssignedFaculty());
			}
			notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeDB.getTeeName() + " <br>"
			// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					+ "<b>Note: </b>This TEE is published by : ?? <br>"
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
					notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}
			}

			return "success";
		} catch (Exception ex) {

			logger.error("Exception", ex);
			return "error";
		}
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/publishAllTee", method = { RequestMethod.GET, RequestMethod.POST })
	public String publishAllTee(Model m, Principal principal, RedirectAttributes redirectAttrs) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		try {
			List<String> submittedTeeList = teeBeanService.getSubmittedTeeIds(username);
			String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
			teeBeanService.updateMultipleTeeToPublished(submittedTeeList, Utils.getInIST(),
					Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
			User u = userService.findByUserName(username);
			for (String s : submittedTeeList) {
				TeeBean teeDB = teeBeanService.findByID(Long.valueOf(s));
				/********* StudentList *********/
				List<String> userList = new ArrayList<String>();
				List<UserCourse> studentsListForIca = new ArrayList<>();
				if (teeDB.getEventId() != null) {
					studentsListForIca = userCourseService.findStudentByEventIdAndAcadYear(teeDB.getEventId(),
							teeDB.getAcadYear(), teeDB.getAcadSession(), teeDB.getProgramId(), teeDB.getId(),
							teeDB.getCampusId(), "N", null);
				} else {
					studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYear(teeDB.getModuleId(),
							teeDB.getAcadYear(), teeDB.getAcadSession(), teeDB.getProgramId(), teeDB.getId(),
							teeDB.getCampusId(), "N", null);
				}
				// Email for student
				String moduleName = courseService.getModuleName(teeDB.getModuleId());
				String subject = " TEE marks is published for Subject : " + moduleName;
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
						notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}

				// Email For faculty
				subject = " TEE marks is published for Subject : " + moduleName;
				result = null;
				userList.clear();
				if (teeDB.getAssignedFaculty().contains(",")) {
					List<String> faculties = new ArrayList<String>();
					faculties = Arrays.asList(teeDB.getAssignedFaculty().split(","));
					userList.addAll(faculties);
				} else {
					userList.add(teeDB.getAssignedFaculty());
				}
				notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeDB.getTeeName() + " <br>"
				// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This TEE is published by : ?? <br>"
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
						notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}
			}

			setSuccess(redirectAttrs, "All TEE's Published");

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in publishing all TEE");
			logger.error("Exception", ex);
		}

		return "redirect:/publishTeeForm";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/deleteTee", method = RequestMethod.GET)
	public String deleteTee(Model m, Principal principal, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs) {

		try {
			TeeBean teeBeanDB = teeBeanService.findByID(teeBean.getId());

			if (teeBeanDB.getParentTeeId() != null) {
				redirectAttrs.addAttribute("teeId", teeBeanDB.getParentTeeId());

				// changes starts
				List<TeeBean> getSubmittedTee = teeBeanService
						.teeSubmittedListByParent(String.valueOf(teeBeanDB.getParentTeeId()));
				logger.info("getSubmittedTee---" + getSubmittedTee.size());
				if (getSubmittedTee.size() > 0) {

					setNote(redirectAttrs, " TEE Cannot be Deleted ");
					return "redirect:/searchTeeList";
				}
			}

			if ("Y".equals(teeBeanDB.getIsTeeDivisionWise()) && teeBeanDB.getParentTeeId() == null) {

				List<TeeBean> divisionWiseTee = teeBeanService.teeListByParent(String.valueOf(teeBean.getId()));

				if (divisionWiseTee.size() > 0) {
					setNote(redirectAttrs,
							"TEE Cannot be Deleted Since It is for Division,First Delete All Division-Wise TEE's");
					return "redirect:/searchTeeList";
				}

			}
			if ("Y".equals(teeBeanDB.getIsSubmitted())) {

				setNote(redirectAttrs, "TEE Cannot be Deleted Since It is Submitted By Faculty");
			} else {
				teeBeanService.deleteSoftById(String.valueOf(teeBean.getId()));
				if (teeBeanDB.getAssignedFaculty().contains(",")) {
					List<TeeStudentBatchwise> studentListForCheck = teeStudentBatchwiseService
							.getAllByActiveTeeId(String.valueOf(teeBean.getId()));
					if (studentListForCheck.size() > 0) {
						teeStudentBatchwiseService.deleteSoftByTeeId(String.valueOf(teeBean.getId()));
					}
				}
				setSuccess(redirectAttrs, "TEE Delete Successfully");
			}
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Deleting TEE");
			logger.error("Exception", ex);
		}

		return "redirect:/searchTeeList";
	}

	// New Changes For TEE

	// generate tee template

	public String generateTeeTemplate(List<UserCourse> studentsListForTee, String moduleName, Long icaId) {

		try {
			DataValidation dataValidation = null;
			DataValidationConstraint constraint = null;
			DataValidationHelper validationHelper = null;
			List<String> headers = new ArrayList<String>(
					Arrays.asList("Roll No", "Name", "SAPID", "Is Absent?", "Total Marks", "REMARKS"));

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Upload Student Tee Marks - " + moduleName);
			validationHelper = new XSSFDataValidationHelper(sheet);

			CellRangeAddressList addressList = new CellRangeAddressList(1, studentsListForTee.size(), 3, 3);

			constraint = validationHelper.createExplicitListConstraint(new String[] { "Y", "N" });
			dataValidation = validationHelper.createValidation(constraint, addressList);
			dataValidation.setSuppressDropDownArrow(true);
			sheet.addValidationData(dataValidation);
			Row r = sheet.createRow(0);

			for (int rn = 0; rn < headers.size(); rn++) {

				r.createCell(rn).setCellValue(headers.get(rn));
			}
			int i = 1;
			for (UserCourse uc : studentsListForTee) {
				Row row = sheet.createRow(i);

				row.createCell(0).setCellValue(String.valueOf(uc.getRollNo()));
				row.createCell(1).setCellValue(uc.getStudentName());
				row.createCell(2).setCellValue(uc.getUsername());
				row.createCell(3).setCellValue("N");
				if (uc.getTeeTotalMarks() != null) {
					String val = uc.getTeeTotalMarks();
					row.createCell(4).setCellValue(val);
				}
				i++;
			}
			String folderPathS3 = baseDirS3 + "/" + app + "/" + "teeUploadMarkTemp";
			String folderPath = downloadAllFolder + "/" + "teeUploadMarkTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdirs();
			}
			String filePath = folderP.getAbsolutePath() + "/" + "teeUploadMarksTemp" + icaId + ".xls";
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

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/evaluateTee", method = { RequestMethod.GET, RequestMethod.POST })
	public String evaluateTee(Model m, Principal principal, @RequestParam Long teeId, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		if (teeId != null) {
			teeBean.setId(teeId);
		}

		Map<String, String> studentTeeMarksMap = new HashMap<>();

		logger.info("teeBean" + teeBean);
		logger.info("teeId" + teeId);
		teeBean = teeBeanService.findByID(teeId);

		String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());

		if (currentDate.compareTo(teeBean.getEndDate()) > 0) {
			setNote(redirectAttrs, "Cannot Evaluate, TEE Deadline is over");
			return "redirect:/searchTeeList";
		}

		if (currentDate.compareTo(teeBean.getStartDate()) < 0) {
			setNote(redirectAttrs, "Cannot Evaluate, TEE has not started yet.");
			return "redirect:/searchTeeList";
		}

		if (null != teeBean.getAssignedFaculty()) {
			if (teeBean.getAssignedFaculty().contains(",")) {
				List<TeeStudentBatchwise> studentListForCheck = teeStudentBatchwiseService
						.getAllByActiveTeeId(String.valueOf(teeBean.getId()));
				if (studentListForCheck.size() == 0) {
					setNote(redirectAttrs, "Students are not Assigned to you. Kindly Contact Your Co-ordinator");
					return "redirect:/searchTeeList";
				}
			}
		}

		if ("Y".equals(teeBean.getIsSubmitted())) {
			TeeQueries teeQueryByTeeId = teeQueriesService.findByTeeId(teeBean.getId());
			if (teeQueryByTeeId != null) {
				if ("Y".equals(teeQueryByTeeId.getIsApproved())) {
					m.addAttribute("teeQuery", "true");
				} else {
					setNote(redirectAttrs, "TEE Reevaluate Not Approved By Co-ordinator");
					return "redirect:/searchTeeList";
				}
			} else {
				setNote(redirectAttrs, "TEE Already Evaluated");
				return "redirect:/searchTeeList";
			}
		}

		/*
		 * int getTeeAssgnCompleted =
		 * teeBeanService.getTeeAssignmentCompleted(teeBean.getId());
		 * logger.info("tee auto assignm"+teeBean.getAutoAssignMarks()); if
		 * ("Y".equalsIgnoreCase(teeBean.getAutoAssignMarks()) && getTeeAssgnCompleted
		 * == 0) { redirectAttrs.addAttribute("teeId", teeBean.getId());
		 * if("Y".equals(teeBean.getIsTeeDivisionWise())) {
		 * redirectAttrs.addAttribute("courseId", teeBean.getEventId()); } return
		 * "redirect:/assignAssignmentMarksToTeeForm"; }
		 */

		List<TeeTotalMarks> teeTotalMarksByTeeId = teeTotalMarksService.getAllTeeTotalMarksByTeeId(teeId);
		if (teeTotalMarksByTeeId.size() > 0) {
			for (TeeTotalMarks ttm : teeTotalMarksByTeeId) {
				studentTeeMarksMap.put(ttm.getUsername() + "totalM", ttm.getTeeTotalMarks());
				studentTeeMarksMap.put(ttm.getUsername() + "scaleM", ttm.getTeeScaledMarks());
				studentTeeMarksMap.put(ttm.getUsername() + "remark", ttm.getRemarks());
				studentTeeMarksMap.put(ttm.getUsername() + "query", ttm.getQuery());
			}
		}

		String moduleName = courseService.getModuleName(teeBean.getModuleId());
		String moduleAbbr = courseService.getModuleAbbr(teeBean.getModuleId());
		List<UserCourse> studentsListForIca = new ArrayList<>();
		List<String> studentsBatchWise = new ArrayList<>();
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			studentsBatchWise = teeStudentBatchwiseService.getDistinctUsernamesByActiveTeeIdAndFaculty(teeBean.getId(),
					username);
		}
		if (studentsBatchWise.size() == 0) {
			if (teeBean.getEventId() != null) {
				studentsListForIca = userCourseService.findStudentByEventIdAndAcadYearForTEE(teeBean.getEventId(),
						teeBean.getAcadYear(), teeBean.getAcadSession(), teeBean.getProgramId(), teeBean.getId(),
						teeBean.getCampusId());
			} else {
				studentsListForIca = userCourseService.findStudentByModuleIdAndAcadYearForTEE(teeBean.getModuleId(),
						teeBean.getAcadYear(), teeBean.getAcadSession(), teeBean.getProgramId(), teeBean.getId(),
						teeBean.getCampusId());
				for (UserCourse uc : studentsListForIca) {
				}
			}
		} else {
			studentsListForIca = userCourseService.findStudentByStudentListAndTeeId(studentsBatchWise, teeBean.getId());
		}

		studentsListForIca.sort(Comparator.comparing(UserCourse::getRollNo));

		String filePathTemplate = generateTeeTemplate(studentsListForIca, moduleAbbr, teeId);

		if ("error".equals(filePathTemplate)) {
			setError(m, "Error in downloading template");
		}
		m.addAttribute("filePath", filePathTemplate);

		m.addAttribute("acadSession", studentsListForIca.get(0).getAcadSession());
		m.addAttribute("moduleName", moduleName);
		String p = programService.getProgramNamesForIca(teeBean.getProgramId());
		m.addAttribute("programName", p);
		m.addAttribute("tee", teeBean);

		m.addAttribute("studentsListForIca", studentsListForIca);
		m.addAttribute("studentMarksMap", studentTeeMarksMap);
		return "tee/evaluateTee";
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/downloadTeeMarksUploadTemplate", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadTeeMarksUploadTemplate(

			@RequestParam(name = "teeId") String teeId, RedirectAttributes redirectAttrs, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;
		try {
			String folderPath = baseDirS3 + "/" + app + "/" + "teeUploadMarkTemp";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			String filePath = baseDirS3 + "/" + app + "/" + "teeUploadMarkTemp" + "/" + "teeUploadMarksTemp" + teeId
					+ ".xls";
			// String filePath =
			// 28-04-2020 Start
			File downloadFile = new File(filePath);
			byte[] data = amazonS3ClientService.getFile(filePath);
			ByteArrayResource resource = new ByteArrayResource(data);
			return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + downloadFile.getName() + "\"")
					.body(resource);

			// 28-04-2020 End
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}
		return null;

	}

	public TeeTotalMarks createTeeTotalMarks(String[] splitKey, Map<String, String> allRequestParams,
			String draftOrFinalSubmit, String sapId, String loggedInUser) throws ValidationException {
		logger.info("allRequestParams is " + allRequestParams);
		String totalKey = "total" + splitKey[0];
		String scaleKey = "scale" + splitKey[0];

		String remarkKey = "remark" + splitKey[0];
		String isAbsent = allRequestParams.get(splitKey[0] + "isAbsent");

		String isQueryApproved = splitKey[0] + "isApproved";

		TeeTotalMarks teeTotalMarks = new TeeTotalMarks();

		teeTotalMarks.setUsername(splitKey[0]);
		teeTotalMarks.setTeeId(Long.valueOf(allRequestParams.get("teeIdValue")));
		if (allRequestParams.containsKey(isQueryApproved)) {
			teeTotalMarks.setIsQueryApproved(allRequestParams.get(isQueryApproved));

		}
		if (allRequestParams.containsKey(totalKey)) {
			BusinessBypassRule.validateNumeric(allRequestParams.get(totalKey));
			teeTotalMarks.setTeeTotalMarks(allRequestParams.get(totalKey));
		}
		if (allRequestParams.containsKey(scaleKey)) {
			logger.info("checking scaleKey --->" + allRequestParams.get(scaleKey));
			BusinessBypassRule.validateNumeric(allRequestParams.get(scaleKey));
			teeTotalMarks.setTeeScaledMarks(allRequestParams.get(scaleKey));
		}
		if (allRequestParams.containsKey(remarkKey) && !allRequestParams.get(remarkKey).isEmpty()) {
			BusinessBypassRule.validateAlphaNumeric(allRequestParams.get(remarkKey));
			teeTotalMarks.setRemarks(allRequestParams.get(remarkKey));
		}
		if (!isAbsent.isEmpty()) {
			BusinessBypassRule.validateYesOrNo(isAbsent);
			teeTotalMarks.setIsAbsent(isAbsent);
		}
		
		teeTotalMarks.setActive("Y");

		if ("DRAFT".equals(draftOrFinalSubmit)) {
			teeTotalMarks.setSaveAsDraft("Y");
		} else {
			teeTotalMarks.setFinalSubmit("Y");
		}
		teeTotalMarks.setCreatedBy(loggedInUser);
		teeTotalMarks.setLastModifiedBy(loggedInUser);

		return teeTotalMarks;
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/showEvaluatedTeeMarks", method = { RequestMethod.GET, RequestMethod.POST })
	public String showEvaluatedTeeMarks(Model m, Principal principal, @RequestParam String teeId) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		TeeBean teeBeanDB = teeBeanService.findByID(Long.valueOf(teeId));
		List<TeeTotalMarks> studentsListForTee = new ArrayList<>();
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			studentsListForTee = teeTotalMarksService.getAllTeeTotalMarksByTeeId(teeBeanDB.getId());
		} else if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			List<TeeStudentBatchwise> studentListForCheck = teeStudentBatchwiseService.getAllByActiveTeeId(teeId);
			if (studentListForCheck.size() > 0) {
				studentsListForTee = teeTotalMarksService.getTeeTotalMarksByTeeIdBatchwise(teeId, username);
			} else {
				studentsListForTee = teeTotalMarksService.getAllTeeTotalMarksByTeeId(teeBeanDB.getId());
			}
		}

		String p = programService.getProgramNamesForIca(teeBeanDB.getProgramId());

		m.addAttribute("programName", p);
		m.addAttribute("moduleName", courseService.getModuleName(teeBeanDB.getModuleId()));
		m.addAttribute("Program_Name", userdetails1.getProgramName());
		m.addAttribute("studentsListForTee", studentsListForTee);

		m.addAttribute("tee", teeBeanDB);
		return "tee/showEvaluatedTeeMarks";
	}

	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/showExternalMarks", method = { RequestMethod.GET, RequestMethod.POST })
	public String showExternalMarks(Model m, Principal principal) {
		String username = principal.getName();

		List<TeeTotalMarks> totalMarks = new ArrayList<TeeTotalMarks>();
		totalMarks = teeTotalMarksService.getTeeTotalMarksByUsername(username);
		Map<String, TeeQueries> raiseQueryStatus = new HashMap<>();
		Map<String, String> dateSpanMap = new HashMap<>();
		for (TeeTotalMarks itm : totalMarks) {
			String publishedDate = itm.getPublishedDate();
			String currentDate = Utils.formatDate("yyyy-MM-dd", Utils.getInIST());
			itm.setDueDate(Utils.addDaysToDate(publishedDate, 3));
			String raiseButton = itm.getDueDate().compareTo(currentDate) >= 0 ? "showButton" : "disableButton";
			dateSpanMap.put(String.valueOf(itm.getTeeId()), raiseButton);

			TeeQueries raiseQryStatus = teeQueriesService.findByTeeId(Long.valueOf(itm.getTeeId()));
			if (null != raiseQryStatus) {

				raiseQueryStatus.put(String.valueOf(itm.getTeeId()), raiseQryStatus);
			}
		}

		m.addAttribute("dateSpanMap", dateSpanMap);
		m.addAttribute("raiseQueryStatus", raiseQueryStatus);
		m.addAttribute("totalMarks", totalMarks);
		m.addAttribute("user", userService.findByUserName(username));
		return "tee/showExternalMarks";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/createStudentGroupFormForTee", method = RequestMethod.GET)
	public String createStudentGroupFormForTee(Model m, Principal principal, @RequestParam(required = false) Long id,
			@ModelAttribute TeeBean teeBean, RedirectAttributes redirectAttrs) {
		Token userdetails1 = (Token) principal;

		if (id != null) {
			teeBean = teeBeanService.findByID(id);
		}

		if (teeBean.getAssignedFaculty().contains(",")) {
			List<TeeStudentBatchwise> studentListForCheck = teeStudentBatchwiseService
					.getAllByActiveTeeId(String.valueOf(teeBean.getId()));
			if (studentListForCheck.size() > 0) {
				List<TeeTotalMarks> teeTotalMarksByTeeId = teeTotalMarksService
						.getAllTeeTotalMarksByTeeId(teeBean.getId());

				if (teeTotalMarksByTeeId.size() > 0) {
					setNote(redirectAttrs,
							"Cannot Assign Students to Faculty ,Faculty Evaluation has begun or completed ");
					return "redirect:/searchTeeList";
				} else {
					setNote(redirectAttrs, "You have already assigned students to faculty!");
					return "redirect:/searchTeeList";
				}
			}
		}

		String p = programService.getProgramNamesForIca(teeBean.getProgramId());

		m.addAttribute("programName", p);

		m.addAttribute("moduleName", courseService.getModuleName(teeBean.getModuleId()));

		if (teeBean.getAssignedFaculty().contains(",")) {
			List<String> facultyIds = Arrays.asList(teeBean.getAssignedFaculty().split(","));
			List<UserCourse> facultyList = userCourseService.findFacultyNamesByIdsForBatch(facultyIds);
			List<UserCourse> studentList = userCourseService.findStudentByModuleIdAndAcadYearForBatch(
					teeBean.getModuleId(), teeBean.getAcadYear(), teeBean.getAcadSession(), teeBean.getProgramId(),
					teeBean.getCampusId());
			m.addAttribute("facultyList", facultyList);
			m.addAttribute("studentList", studentList);
		}
		m.addAttribute("tee", teeBean);

		return "tee/assignStudentsFacultyWiseTee";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/saveStudentsBatchWiseForTee", method = RequestMethod.POST)
	public @ResponseBody String saveStudentsBatchWiseForTee(@RequestParam String json, HttpServletResponse resp) {
		TeeBean teeBean = new TeeBean();
		String tee = "";
		ObjectMapper mapper = new ObjectMapper();

		try {

			List<TeeStudentBatchwise> jsonList = mapper.readValue(json, new TypeReference<List<TeeStudentBatchwise>>() {
			});
			if (jsonList.size() > 0) {

				teeBean = teeBeanService.findByID(Long.valueOf(jsonList.get(0).getTeeId()));
				for (TeeStudentBatchwise tsb : jsonList) {
					tsb.setCreatedBy(teeBean.getCreatedBy());
					tsb.setLastModifiedBy(teeBean.getLastModifiedBy());
					tsb.setActive("Y");
				}
				teeStudentBatchwiseService.upsertBatch(jsonList);
				teeBean.setStatus("success");
				tee = mapper.writeValueAsString(teeBean);
				return tee;
			} else {
				teeBean.setStatus("blank");
				tee = mapper.writeValueAsString(teeBean);
			}
		} catch (Exception e) {
			logger.error("Exception while saving students", e);
			teeBean.setStatus("falied");
		}
		return tee;

	}

	@Secured({"ROLE_STUDENT"})
	@RequestMapping(value = "/raiseQueryTeeForStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String raiseQueryTeeForStudent(Model m, Principal principal, @RequestParam String id,
			@RequestParam String query) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();
		try {
			HtmlValidation.checkHtmlCode(id);
			HtmlValidation.checkHtmlCode(query);
			
			BusinessBypassRule.validateAlphaNumeric(query);
			int updated = teeTotalMarksService.updateRaiseQuery(id, username, query);

			TeeQueries teeQ = new TeeQueries();

			teeQ.setTeeId(id);
			teeQ.setCreatedBy(username);
			teeQ.setLastModifiedBy(username);

			teeQueriesService.upsert(teeQ);

			List<String> userList = new ArrayList<String>();
			TeeBean teeDB = teeBeanService.findByID(Long.valueOf(id));
			User u = userService.findByUserName(username);
			String moduleName = courseService.getModuleName(teeDB.getModuleId());
			String subject = " TEE Query raised for subject " + moduleName;
			Map<String, Map<String, String>> result = null;

			userList.add(teeDB.getCreatedBy());
			String notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeDB.getTeeName() + " <br>"
					+ "<b>Query: </b> " + query + "<br>" + "<b>Query Raised By: </b>Name: " + u.getFirstname() + " "
					+ u.getLastname() + " ( " + username + " )<br><br>"

					// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
					// + "This ICA is created by : ?? <br>"
					+ "<b>Note: </b> To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
					+ "This is auto-generated email, do not reply on this.</body></html>";

			if (!userList.isEmpty()) {
				result = userService.findEmailByUserName(userList);
				Map<String, String> email = result.get("emails");
				Map<String, String> mobiles = new HashMap();
				notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
			}
			return "success";
		} catch (ValidationException ve) {
//			logger.error(ve.getMessage(), ve);
//			setError(redirectAttrs, ve.getMessage());
//			return "redirect:/showInternalMarks";
			logger.info("INSIDE ValidationException");
			logger.error("ValidationException", ve);
			return "validationError";
		} 
		catch (Exception ex) {

			logger.error("Exception", ex);
			return "error";
		}

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTeeFile", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadTeeFile(
			@RequestParam(required = false, name = "id", defaultValue = "") String id, HttpServletRequest request,
			HttpServletResponse response) {

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		String projectUrl = "";

		TeeQueries icaQ = teeQueriesService.findByID(Long.valueOf(id));
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			String filePath = icaQ.getFilePath();
			// 28-04-2020 Start
			String folderPathStr = "data/TEEUploads";
			// 28-04-2020 End
			if (filePath.contains(",")) {

				File folderPath = new File(baseDir + "/" + "approvalFiles");
				List<String> files = Arrays.asList(filePath.split(","));
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}
				logger.info("files--->" + files.size());
				for (String file : files) {
					File fileNew = new File(file);
					// files.add(file);
					// 28-04-2020 Start
					InputStream inpStream = amazonS3ClientService.getFileForDownloadS3Object(fileNew.getName(),
							folderPathStr);
					File dest = new File(folderPath.getAbsolutePath() + "/" + fileNew.getName());
					logger.info("dest--->" + dest.getPath());
					FileUtils.copyInputStreamToFile(inpStream, dest);
					// 28-04-2020 End
				}
				String filename = "approvalFiles.zip";
				response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
				projectUrl = "/" + "data" + "/" + folderPath.getName() + ".zip";
				logger.info("folderPath--->" + folderPath.getAbsolutePath());
				pack(folderPath.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folderPath);
				return null;

			} else {

				if (StringUtils.isEmpty(filePath)) {
					request.setAttribute("error", "true");
					request.setAttribute("errorMessage", "Error in downloading file.");
				}

				// get absolute path of the application
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

	@Secured({"ROLE_FACULTY"})
	@RequestMapping(value = "/downloadTeeReportFacultyForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTeeReportFacultyForm(Model m, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<String> acadYearCodeList = courseService.findAcadYearCode();
		m.addAttribute("acadYearCodeList", acadYearCodeList);
		m.addAttribute("Program_Name", userdetails1.getProgramName());

		return "tee/teeReportFaculty";
	}

	@Secured({"ROLE_FACULTY"})
	@RequestMapping(value = "/downloadTeeFacultyReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTeeFacultyReport(Model m, HttpServletResponse response, Principal principal,
			@RequestParam String acadYear, @RequestParam String reportType, RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<TeeTotalMarks> data = teeTotalMarksService.getTeeTotalMarksByParamForFaculty(acadYear, username);

		InputStream is = null;
		String filePath = "";

		try {

			ServletOutputStream out = response.getOutputStream();
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
			String filename = "Student review TEE Marks.xlsx";
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			// copy it to response's OutputStream
			is = new FileInputStream(filePath);
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

		return null;

	}

	public String getFilePathOfSubjectAndComponentWiseReportForFaculty(String acadYear, String collegeName,
			Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		String fileName = "TEE-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "TEE-Report" + acadYear
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
		List<TeeTotalMarks> getTeeTotalMarksByParam = teeTotalMarksService.getTeeTotalMarksByParamForFaculty(acadYear,
				username);

		Map<String, Course> courseMapper = new HashMap<>();
		Map<String, List<TeeTotalMarks>> teeTotalMarksModuleWise = new HashMap<>();
		for (TeeTotalMarks itm : getTeeTotalMarksByParam) {

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
			List<TeeTotalMarks> itmList = getTeeTotalMarksByParam.stream().filter(o -> o.getModuleId().equals(moduleId))
					.collect(Collectors.toList());
			teeTotalMarksModuleWise.put(moduleId, itmList);
		}

		for (String moduleId : courseMapper.keySet()) {

			String acdYear = courseMapper.get(moduleId).getAcadYear();
			String session = courseMapper.get(moduleId).getAcadSession();

			String faculties = userCourseService.getFacultiesByParamForReport(acdYear);

			String h[] = { "SAPID", "Name", "Roll.No" };

			List<String> headers = new ArrayList<String>(Arrays.asList(h));

			List<String> studentsSapIds = getTeeTotalMarksByParam.stream().map(map -> map.getUsername())
					.collect(Collectors.toList());

			headers.add("Obtained Marks");
			headers.add("Total Tee Marks");
			headers.add("Status");
			headers.add("Remarks");

			int rowNum = 0;

			XSSFSheet sheet = workbook.createSheet(courseMapper.get(moduleId).getModuleAbbr());

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

			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(acadYear + "-" + batch);
			Cell subjNameAbbrCell = moduleNameAbbrRow.createCell(0);
			Cell facultiesCell = facultiesRow.createCell(0);
			Cell reviewCell = reviewRow.createCell(0);

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			String subjNameVal = courseMapper.get(moduleId).getModuleName() + "("
					+ courseMapper.get(moduleId).getModuleAbbr() + ")";
			subjNameAbbrCell.setCellValue(subjNameVal);
			reviewCell.setCellValue("Student review TEE Marks");
			facultiesCell.setCellValue(faculties);
			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);
			subjNameAbbrCell.setCellStyle(centerStyle);
			facultiesCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(7).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			for (TeeTotalMarks itm : teeTotalMarksModuleWise.get(moduleId)) {
				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(itm.getUsername());
				rowd.createCell(colNum++).setCellValue(itm.getStudentName());
				rowd.createCell(colNum++).setCellValue(itm.getRollNo());

				rowd.createCell(colNum++).setCellValue(itm.getTeeTotalMarks());
				rowd.createCell(colNum++).setCellValue(itm.getExternalMarks());

				rowd.createCell(colNum++).setCellValue(itm.getPassFailStatus());
				rowd.createCell(colNum++).setCellValue(itm.getRemarks());

			}
			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
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
			workbook.close();
		} catch (Exception e) {
			logger.error("Exception ", e);
		}

		return filePath;
	}

	// 10-04-2020 Merged Code

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTeeFormForDivision", method = { RequestMethod.GET, RequestMethod.POST })
	public String addTeeFormForDivision(Model m, Principal principal, @RequestParam(required = false) String id,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Add TEE For Divisions", true, false));

		TeeBean tee = new TeeBean();

		if (id != null) {

			tee = teeBeanService.findByID(Long.valueOf(id));

			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());

			if (currentDate.compareTo(tee.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot update, TEE Deadline is over");
				return "redirect:/searchTeeList";
			} else if ("Y".equals(tee.getIsSubmitted())) {
				setNote(redirectAttrs, "Cannot update, TEE is Already Evaluated");
				return "redirect:/searchTeeList";
			}

			else {
				if (currentDate.compareTo(tee.getStartDate()) > 0) {

					m.addAttribute("icaStarted", "true");
				} else {
					m.addAttribute("icaStarted", "false");
				}
			}

			m.addAttribute("edit", "true");
			m.addAttribute("moduleName", courseService.getModuleName(tee.getModuleId()));
			m.addAttribute("programList", programService.getProgramListByIds(tee.getProgramId()));
			m.addAttribute("facultyList",
					userCourseService.findAllFacultyWithModuleIdICA(tee.getModuleId(), tee.getAcadYear()));
		}
		List<String> acadYearCodeList = courseService.findAcadYearCode();
		m.addAttribute("acadYearCodeList", acadYearCodeList);
		m.addAttribute("teeBean", tee);

		return "tee/createTeeForDivision";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/addTeeForDivision", method = RequestMethod.POST)
	public String addTeeForDivision(Model m, Principal principal, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs) {

		try {

			logger.info("Validating /addTeeForDivision...");
			HtmlValidation.validateHtml(teeBean, new ArrayList<>());
			BusinessBypassRule.validateAlphaNumeric(teeBean.getTeeName());
			Course acadYear = courseService.checkIfExistsInDB("acadYear", teeBean.getAcadYear());
			if(acadYear == null) {
				throw new ValidationException("Invalid Acad Year");
			}
			Course acadSession = courseService.checkIfExistsInDB("acadSession", teeBean.getAcadSession());
			if(acadSession == null) {
				throw new ValidationException("Invalid Acad Session");
			}
			Course campusId = courseService.checkIfExistsInDB("campusId", teeBean.getCampusId());
			if(campusId == null) {
				throw new ValidationException("Invalid Campus");
			}
			Course moduleId = courseService.checkIfExistsInDB("moduleId", teeBean.getModuleId());
			if(moduleId == null) {
				throw new ValidationException("Invalid Module");
			}
			logger.info("create Tee selected programs are " + teeBean.getProgramId());
			Course progId = courseService.checkIfExistsInDB("programId", teeBean.getProgramId());			
			if(progId == null) {
					throw new ValidationException("Invalid Program");
				}
			            
			BusinessBypassRule.validateNumeric(teeBean.getExternalMarks());
			BusinessBypassRule.validateNumeric(teeBean.getExternalPassMarks());
			if(!teeBean.getInternalMarks().isEmpty()) {
				BusinessBypassRule.validateNumeric(teeBean.getInternalMarks());
			}
			if(!teeBean.getInternalPassMarks().isEmpty()) {
				BusinessBypassRule.validateNumeric(teeBean.getInternalPassMarks());
			}
			if(!teeBean.getTotalMarks().isEmpty()) {
				BusinessBypassRule.validateNumeric(teeBean.getTotalMarks());
			}
			
			Utils.validateStartAndEndDates(teeBean.getStartDate(), teeBean.getEndDate());
			logger.info("Scaled Req is " + teeBean.getScaledReq());
			
			BusinessBypassRule.validateYesOrNo(teeBean.getAutoAssignMarks());
			
			if (teeBean.getScaledReq() == null || teeBean.getScaledReq().equals("N")) {
				teeBean.setScaledReq("N");
				teeBean.setScaledMarks(null);
			} else {
				BusinessBypassRule.validateNumeric(teeBean.getScaledMarks());
			}
			if(!teeBean.getTeeDesc().isEmpty()) {
				BusinessBypassRule.validateAlphaNumeric(teeBean.getTeeDesc());
			}
			logger.info("Validation Done");
			
			String username = principal.getName();

			teeBean.setCreatedBy(username);
			teeBean.setLastModifiedBy(username);
			teeBean.setActive("Y");
			teeBean.setIsTeeDivisionWise("Y");
			teeBean.setIsNonEventModule("N");

			if (teeBean.getScaledReq() == null || teeBean.getScaledReq().equals("N")) {
				teeBean.setScaledReq("N");
				teeBean.setScaledMarks(null);
			}
			/* New Audit changes start */
//			if(!Utils.validateStartAndEndDates(teeBean.getStartDate(), teeBean.getEndDate())) {
//				setError(redirectAttrs, "Invalid Start date and End date");
//				return "redirect:/addTeeForm";
//			}
			/* New Audit changes end */
			teeBean.setAssignedFaculty(null);
			List<TeeBean> teeDBList = teeBeanService.checkAlreadyExistTEEAList(teeBean.getModuleId(),
					teeBean.getAcadYear(), teeBean.getCampusId(), teeBean.getAcadSession());

			for (TeeBean teeDB : teeDBList) {
				if (teeDB != null) {
					if (teeDB.getProgramId().contains(",")) {
						List<String> programIds = Arrays.asList(teeDB.getProgramId().split(","));
						if (teeDB.getProgramId().contains(",")) {
							for (String db : Arrays.asList(teeDB.getProgramId().split(","))) {
								if (programIds.contains(db)) {
									setError(redirectAttrs, "TEE Already Exist");
									return "redirect:/addTeeFormForDivision";
								}
							}
						} else {
							if (programIds.contains(teeDB.getProgramId())) {
								setError(redirectAttrs, "TEE Already Exist");
								return "redirect:/addTeeFormForDivision";
							}
						}
					} else {
						if (teeDB.getProgramId().contains(",")) {
							if (Arrays.asList(teeDB.getProgramId().split(",")).contains(teeDB.getProgramId())) {
								setError(redirectAttrs, "TEE Already Exist");
								return "redirect:/addTeeFormForDivision";
							}
						} else {
							if (teeDB.getProgramId().equals(teeDB.getProgramId())) {
								setError(redirectAttrs, "TEE Already Exist");
								return "redirect:/addTeeFormForDivision";
							}
						}
					}
				}
			}
			// teeBeanService.insertWithIdReturn(teeBean);

			List<UserCourse> facultyListDivisionWise = userCourseService.getAllFacultiesDivisionWise(
					teeBean.getAcadYear(), teeBean.getAcadSession(), teeBean.getModuleId(), teeBean.getProgramId(),
					teeBean.getCampusId());
			if (facultyListDivisionWise.size() > 0) {
				teeBeanService.insertWithIdReturn(teeBean);
				String parentTeeId = String.valueOf(teeBean.getId());

				List<String> distinctCourseId = new ArrayList<>();
				List<String> userList = new ArrayList<String>();
				for (UserCourse uc : facultyListDivisionWise) {

					TeeBean icaDiv = teeBean;

					icaDiv.setAssignedFaculty(uc.getUsername());
					icaDiv.setEventId(uc.getEventId());
					icaDiv.setParentTeeId(parentTeeId);

					if (!distinctCourseId.contains(uc.getEventId())) {
						userList.add(icaDiv.getAssignedFaculty());
						teeBeanService.insertWithIdReturn(icaDiv);
						distinctCourseId.add(uc.getEventId());
					}

				}
				// Hiren
				User u = userService.findByUserName(username);
				String subject = " New TEE: " + teeBean.getTeeName();
				Map<String, Map<String, String>> result = null;

				String notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeBean.getTeeName() + " <br>"
						+ "<b>TEE Description: </b>" + teeBean.getTeeDesc() + "<br><br>"
						+ "<b>Note: </b>This TEE is created by : ?? <br>"
						+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
						+ "This is auto-generated email, do not reply on this.</body></html>";

				if (!userList.isEmpty()) {
					notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
							+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
					result = userService.findEmailByUserName(userList);
					Map<String, String> email = result.get("emails");
					Map<String, String> mobiles = new HashMap();
					notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
				}

				setSuccess(redirectAttrs, "TEE Added For " + distinctCourseId.size() + " Divisions successfully ");
				redirectAttrs.addAttribute("id", parentTeeId);
				return "redirect:/searchTeeList";
			} else {
				setError(redirectAttrs, "There is no event for selected module to create division TEE.");
				return "redirect:/addTeeFormForDivision";
			}
		}

		catch (Exception ex) {

			logger.error("Excption", ex);

			setError(redirectAttrs, "Error While Creating TEE");
			return "redirect:/addTeeFormForDivision";
		}

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/showTeeQueries", method = { RequestMethod.GET, RequestMethod.POST })
	public String showTeeQueries(Model m, Principal principal) {

		Token userdetails1 = (Token) principal;
		String username = principal.getName();

		List<TeeBean> teeQueries = teeBeanService.getTeeQueries(username);

		m.addAttribute("teeQueries", teeQueries);
		String isAllApproved = "true";

		//
		List<TeeBean> finalTeeBeanList = new ArrayList<>();
		for (TeeBean t : teeQueries) {

			boolean isTeeSentToTcs = isTeeMarksSentToTcs(t);

			if (isTeeSentToTcs == false) {
				finalTeeBeanList.add(t);
			}
		}
		teeQueries.clear();
		teeQueries.addAll(finalTeeBeanList);
		//

		for (TeeBean ic : teeQueries) {
			if (!"Y".equals(ic.getIsApproved())) {
				isAllApproved = "false";
			}
		}

		String formatDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());
		m.addAttribute("currentDate", Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", formatDate));
		m.addAttribute("isAllApproved", isAllApproved);
		return "tee/teeQueries";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/approveTeeForReeval", method = { RequestMethod.POST, RequestMethod.GET })
	public String approveTeeForReeval(@RequestParam(name = "file", required = true) List<MultipartFile> input,
			@RequestParam(name = "teeId") String teeId, Principal p, RedirectAttributes redirectAttributes) {
		String username = p.getName();
		try {
			String multipleFilePath = "";
			int errCount = 0;
			int i = 0;
			for (MultipartFile file : input) {
				if (!file.isEmpty()) {
					// Audit change start
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->" + count);
						if (count > 1 || count == 0) {
							setError(redirectAttributes, "File uploaded is invalid!");
							return "redirect:/showTeeQueries";
						} else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->" + extension);
							if (extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								setError(redirectAttributes, "File uploaded is invalid!");
								return "redirect:/showTeeQueries";
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
								String filePath = baseDirS3 + "/" + "TEEUploads";
								Map<String, String> returnMap = amazonS3ClientService
										.uploadFileToS3BucketWithRandomString(file, filePath);
								if (!returnMap.containsKey("ERROR")) {
									if (input.size() == 1) {
										multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
									} else {
										if (i == 0) {
											multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
											i++;
										} else {
											multipleFilePath = multipleFilePath + "," + filePath + "/"
													+ returnMap.get("SUCCESS");
										}
									}
								} else {
									errCount++;
								}
								} else {
									setError(redirectAttributes, "File uploaded is invalid!");
									return "redirect:/showTeeQueries";
								}
							}
						}
					} else {
						setError(redirectAttributes, "File uploaded is invalid!");
						return "redirect:/showTeeQueries";
					}
					// Audit change end
					// 28-04-2020 Start

					// 28-04-2020 End
				}

			}

			if (errCount > 0) {
				setError(redirectAttributes, "Error in Approving TEE File");
				return "redirect:/showTeeQueries";
			} else {
				TeeQueries teeQuery = new TeeQueries();
				teeQuery.setTeeId(teeId);
				teeQuery.setFilePath(multipleFilePath);
				teeQuery.setIsApproved("Y");
				teeQuery.setLastModifiedBy(p.getName());

				teeQueriesService.upsert(teeQuery);

				TeeBean teeDB = teeBeanService.findByID(Long.valueOf(teeId));
				List<String> userList = new ArrayList<String>();
				// Faculty
				User u = userService.findByUserName(username);
				String moduleName = courseService.getModuleName(teeDB.getModuleId());
				String subject = " TEE raised query approved for subject: " + moduleName;
				Map<String, Map<String, String>> result = null;
				if (teeDB.getAssignedFaculty().contains(",")) {
					List<String> faculties = new ArrayList<String>();
					faculties = Arrays.asList(teeDB.getAssignedFaculty().split(","));
					userList.addAll(faculties);
				} else {
					userList.add(teeDB.getAssignedFaculty());
				}
				String notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeDB.getTeeName() + " <br>"
				// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
						+ "<b>Note: </b>This TEE query approved by : ?? <br>"
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
						notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}

				setSuccess(redirectAttributes, "TEE Approved Successfully");
			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
			setError(redirectAttributes, "Error in Approving TEE");
		}

		return "redirect:/showTeeQueries";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/approveAllTeeForReeval", method = { RequestMethod.POST, RequestMethod.GET })
	public String approveAllTeeForReeval(@RequestParam(name = "file", required = true) List<MultipartFile> input,
			Principal p, RedirectAttributes redirectAttributes) {
		String username = p.getName();
		try {
			String multipleFilePath = "";
			int errCount = 0;
			int i = 0;
			for (MultipartFile file : input) {
				if (!file.isEmpty()) {
					// 28-04-2020 Start
					// Audit change start
					Tika tika = new Tika();
					  String detectedType = tika.detect(file.getBytes());
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->" + count);
						if (count > 1 || count == 0) {
							errCount++;
						} else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->" + extension);
							if (extension.equalsIgnoreCase("exe") || ("application/x-msdownload").equals(detectedType) || ("application/x-sh").equals(detectedType)) {
								errCount++;
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
								String filePath = baseDirS3 + "/" + "TEEUploads";
								Map<String, String> returnMap = amazonS3ClientService
										.uploadFileToS3BucketWithRandomString(file, filePath);
								if (!returnMap.containsKey("ERROR")) {
									if (input.size() == 1) {
										multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
									} else {
										if (i == 0) {
											multipleFilePath = filePath + "/" + returnMap.get("SUCCESS");
											i++;
										} else {
											multipleFilePath = multipleFilePath + "," + filePath + "/"
													+ returnMap.get("SUCCESS");
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
					} else {
						errCount++;
					}
					// Audit change end
					// 28-04-2020 End
				}

			}

			if (errCount > 0) {
				setError(redirectAttributes, "Error in Approving TEE File");
				return "redirect:/showTeeQueries";
			} else {

				List<TeeBean> teeQueries = teeBeanService.getTeeQueriesForApproveAll(p.getName());

				List<TeeQueries> teeQueryList = new ArrayList<>();

				for (TeeBean ic : teeQueries) {
					TeeQueries teeQuery = new TeeQueries();
					teeQuery.setTeeId(String.valueOf(ic.getId()));
					teeQuery.setFilePath(multipleFilePath);
					teeQuery.setIsApproved("Y");
					teeQuery.setLastModifiedBy(p.getName());
					teeQueryList.add(teeQuery);
				}

				teeQueriesService.upsertBatch(teeQueryList);
				for (TeeBean ib : teeQueries) {
					TeeBean teeDB = teeBeanService.findByID(ib.getId());
					List<String> userList = new ArrayList<String>();
					// Faculty
					User u = userService.findByUserName(username);
					String moduleName = courseService.getModuleName(teeDB.getModuleId());
					String subject = " TEE raised query approved for subject: " + moduleName;
					Map<String, Map<String, String>> result = null;
					if (teeDB.getAssignedFaculty().contains(",")) {
						List<String> faculties = new ArrayList<String>();
						faculties = Arrays.asList(teeDB.getAssignedFaculty().split(","));
						userList.addAll(faculties);
					} else {
						userList.add(teeDB.getAssignedFaculty());
					}
					String notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeDB.getTeeName()
							+ " <br>"
							// + "<b>ICA Description: </b>"+ icaDB.getIcaDesc() +"<br><br>"
							+ "<b>Note: </b>This TEE query approved by : ?? <br>"
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
							notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
						}
					}
				}

				setSuccess(redirectAttributes, "TEE Approved Successfully");
			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
			setError(redirectAttributes, "Error in Approving TEE");
		}

		return "redirect:/showTeeQueries";
	}

	private String uploadTeeApprovalFile(MultipartFile file) {

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

			filePath = baseDir + File.separator + "TEEUploads" + File.separator + fileName;
			folderPath = new File(baseDir + File.separator + "TEEUploads");

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

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/submitTee", method = { RequestMethod.POST, RequestMethod.GET })
	public String submitTee(@RequestParam Map<String, String> allRequestParams, Principal p,
			RedirectAttributes redirectAttributes) {

		String username = p.getName();

		List<TeeTotalMarks> teeTotalMarksList = new ArrayList<>();

		List<String> teeCompStudList = new ArrayList<>();
		List<String> teeTotalStudList = new ArrayList<>();
		boolean isAllSubmitted = true;
		redirectAttributes.addAttribute("teeId", allRequestParams.get("teeIdValue"));
		ExcelReader excReader = new ExcelReader();
		TeeBean teeDB = teeBeanService.findByID(Long.valueOf(allRequestParams.get("teeIdValue")));
		logger.info("allRequestParams---->" + allRequestParams);
		try {
			for (String sapId : allRequestParams.keySet()) {
				if (sapId.contains("-")) {

					String[] splitKey = sapId.split("-");
					String totalKey = "total" + splitKey[0];
					String marksGotStr = allRequestParams.get(totalKey);
					if (!excReader.ISVALIDINPUT(marksGotStr)) {
						setError(redirectAttributes, "Input should be valid number");
						return "redirect:/evaluateTee";
					}
					double marksGot = Double.parseDouble((allRequestParams.get(totalKey)));
					if (marksGot > Double.parseDouble(teeDB.getExternalMarks())) {
						setError(redirectAttributes, "Marks entered is greater than total TEE Marks For");

						return "redirect:/evaluateTee";
					}

					TeeTotalMarks teeTotalMarks = createTeeTotalMarks(splitKey, allRequestParams, "SUBMIT", sapId,
							p.getName());
					teeTotalMarksList.add(teeTotalMarks);

					if (!teeTotalStudList.contains(teeTotalMarks.getUsername())) {
						teeTotalStudList.add(teeTotalMarks.getUsername());
					}

				}

			}

			teeTotalMarksService.upsertBatch(teeTotalMarksList);

			if (teeDB.getAssignedFaculty().contains(",")) {
				String teeIdParam = allRequestParams.get("teeIdValue");
				int totalSubmitted = teeTotalMarksService.getDistinctSubmittedUsernamesByActiveTeeId(teeIdParam).size();
				int totalStudentsByTeeId = teeStudentBatchwiseService.getAllByActiveTeeId(teeIdParam).size();
				if (totalSubmitted == totalStudentsByTeeId) {

					teeBeanService.updateTeeToSubmitted(allRequestParams.get("teeIdValue"), Utils.getInIST());
				}

			} else {
				teeBeanService.updateTeeToSubmitted(allRequestParams.get("teeIdValue"), Utils.getInIST());
			}
			setSuccess(redirectAttributes, "TEE Marks Submitted Successfully");
			try {
				List<TeeTotalMarks> reevalStudent = teeTotalMarksService
						.getIsReevalTeeUsername(String.valueOf(teeDB.getId()));
				if (reevalStudent.size() > 0) {

					teeQueriesService.updateReEvaluated(teeDB.getId());

					User u = userService.findByUserName(username);
					String moduleName = courseService.getModuleName(teeDB.getModuleId());
					String subject = " TEE Marks Re-evaluated status changed for Subject " + moduleName;
					Map<String, Map<String, String>> result = null;
					List<String> userList = new ArrayList<String>();
					List<String> acadSessionList = new ArrayList<>();
					String notificationEmailMessage = "";
					for (TeeTotalMarks uc : reevalStudent) {
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
							notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
						}
					}

				} else {
					User u = userService.findByUserName(username);
					String subject = " TEE Marks Submitted: " + teeDB.getTeeName();
					Map<String, Map<String, String>> result = null;
					List<String> userList = new ArrayList<String>();
					userList.add(teeDB.getCreatedBy());
					String notificationEmailMessage = "<html><body>" + "<b>TEE Name: </b>" + teeDB.getTeeName()
							+ " <br>"
							// + "<b>ICA Description: </b>"+ icaBeanDB.getIcaDesc() +"<br><br>"
							+ "<b>Note: </b>This TEE marks is submitted by : ?? <br>"
							+ "To view kindly login to Url: https://portal.svkm.ac.in/usermgmt/login <br>"
							+ "This is auto-generated email, do not reply on this.</body></html>";

					if (!userList.isEmpty()) {
						notificationEmailMessage = notificationEmailMessage.toString().replace("??", " Name : "
								+ u.getFirstname() + " " + u.getLastname() + " ( Email-Id: " + u.getEmail() + ")");
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject, notificationEmailMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Email Error---->" + e);
				setError(redirectAttributes, "Error in Sending Email to Student");
			}
			return "redirect:/showEvaluatedTeeMarks";
		} catch (ValidationException ve) {
			logger.error(ve.getMessage(), ve);
			setError(redirectAttributes, ve.getMessage());
			return "redirect:/evaluateTee";
			
		} 
		catch (Exception ex) {
			setError(redirectAttributes, "Error in Submitting TEE Marks");
			logger.error("Exception", ex);

			return "redirect:/evaluateTee";
		}

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/saveTeeAsDraft", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody String saveTeeAsDraft(@RequestParam Map<String, String> allRequestParams, Principal p) {

		List<TeeTotalMarks> teeTotalMarksList = new ArrayList<>();
		ExcelReader excReader = new ExcelReader();
		List<String> teeTotalStudentList = new ArrayList<>();
		TeeBean teeBeanDAO = teeBeanService.findByID(Long.valueOf(allRequestParams.get("teeIdValue")));
		int totalMarks = Integer.parseInt(teeBeanDAO.getExternalMarks());
		try {
			for (String sapId : allRequestParams.keySet()) {
				if (sapId.contains("-")) {
					String[] splitKey = sapId.split("-");
					String totalKey = "total" + splitKey[0];

					String marksGotStr = allRequestParams.get(totalKey);
					if (!excReader.ISVALIDINPUT(marksGotStr)) {

						return "Input should be valid number";
					}
					if (!allRequestParams.get(totalKey).isEmpty()) {
						double marksGot = Double.parseDouble(allRequestParams.get(totalKey));
						if (marksGot > totalMarks) {
							return "Marks entered is greater than total TEE Marks For " + splitKey[0];
						}
					}
					TeeTotalMarks teeTotalMarks = createTeeTotalMarks(splitKey, allRequestParams, "DRAFT", sapId,
							p.getName());

					teeTotalMarksList.add(teeTotalMarks);

					if (!teeTotalStudentList.contains(teeTotalMarks.getUsername())) {
						teeTotalStudentList.add(teeTotalMarks.getUsername());
					}

				}

			}

			teeTotalMarksService.upsertBatch(teeTotalMarksList);

			return "saved";
		} catch (Exception ex) {

			logger.error("Exception", ex);

			return "error";
		}

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/uploadStudentMarksExcelTee", method = { RequestMethod.POST })
	public String uploadStudentMarksExcelTee(@ModelAttribute Test test, @RequestParam("file") MultipartFile file,
			@RequestParam String saveAs, @RequestParam String teeId, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {

		// redirectAttributes.addAttribute("icaId", teeId);
		redirectAttributes.addAttribute("teeId", teeId);
		try {
			String username = principal.getName();
			TeeBean teeBeanDB = teeBeanService.findByID(Long.valueOf(teeId));

			List<String> teeTotalStudList = new ArrayList<>();
			boolean isAllSubmitted = true;
			List<String> headers = new ArrayList<String>(
					Arrays.asList("Roll No", "Name", "SAPID", "Is Absent?", "Total Marks", "REMARKS"));

			ExcelReader excelReader = new ExcelReader();

			List<Map<String, Object>> maps = excelReader.readIcaExcelFileUsingColumnHeader(file, headers);

			List<String> excelUserList = new ArrayList<>();

			List<Map<String, Object>> copy = new ArrayList<>();

			if (teeBeanDB.getEventId() == null && !("Y").equals(teeBeanDB.getIsTeeDivisionWise())
					&& !("Y").equals(teeBeanDB.getIsNonEventModule())) {

				List<String> ucListForModuleBatch = teeStudentBatchwiseService
						.getDistinctUsernamesByActiveTeeIdAndFaculty(teeBeanDB.getId(), username);

				List<String> ucListForModule = userCourseService.findDistinctStudentByModuleIdAndAcadYearForTEE(
						teeBeanDB.getModuleId(), teeBeanDB.getAcadYear(), teeBeanDB.getAcadSession(),
						teeBeanDB.getProgramId(), teeBeanDB.getId(), teeBeanDB.getCampusId());

				if (ucListForModuleBatch.size() > 0) {
					ucListForModule.clear();
					ucListForModule.addAll(ucListForModuleBatch);
				}

				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					if (ucListForModule.contains(user)) {
						return true;
					} else {
						return false;
					}
				}).collect(Collectors.toList());
				if (ucListForModule.size() > copy.size() || copy.size() > ucListForModule.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");

					return "redirect:/evaluateTee";
				}
			} // 11-04-2020
			else if (teeBeanDB.getEventId() != null && ("Y").equals(teeBeanDB.getIsTeeDivisionWise())) {
				List<UserCourse> ucListForEvent = userCourseService.findStudentByEventIdAndAcadYearForTEE(
						teeBeanDB.getEventId(), teeBeanDB.getAcadYear(), teeBeanDB.getAcadSession(),
						teeBeanDB.getProgramId(), teeBeanDB.getId(), teeBeanDB.getCampusId());
				logger.info("ucListSize-->" + ucListForEvent.size());
				copy = maps.stream().filter(s -> {

					String user = (String) s.get("SAPID");
					UserCourse uc = userCourseService.getMappingByUsernameAndCourse(user, teeBeanDB.getEventId());
					if (uc == null) {
						return false;
					} else {
						return true;
					}
				}).collect(Collectors.toList());

				if (ucListForEvent.size() > copy.size() || maps.size() > ucListForEvent.size()) {
					setError(redirectAttributes, "You have tampered the SAP IDs given in the template!");
					return "redirect:/evaluateTee";
				}
				logger.info("courseWise");

			}
			List<TeeTotalMarks> teeTotalMarksList = new ArrayList<>();
			for (Map<String, Object> mapper : copy) {
				if (mapper.get("Error") != null) {
					setError(redirectAttributes, (String) mapper.get("Error"));

					return "redirect:/evaluateTee";

				}

				double total = 0;
				int componentCount = 0;
				String isAbsent = mapper.get("Is Absent?").toString();
				if ("Y".equals(isAbsent)) {

					TeeTotalMarks ttm = new TeeTotalMarks();

					ttm.setTeeId(Long.valueOf(teeId));
					ttm.setUsername((String) mapper.get("SAPID"));
					ttm.setTeeTotalMarks("0");
					if ("Y".equals(teeBeanDB.getScaledReq())) {
						ttm.setTeeScaledMarks("0");
					}
					ttm.setIsAbsent("Y");

					if ("draft".equals(saveAs)) {
						ttm.setSaveAsDraft("Y");
					} else {
						ttm.setFinalSubmit("Y");
					}
					ttm.setRemarks((String) mapper.get("REMARKS"));
					ttm.setActive("Y");
					ttm.setCreatedBy(principal.getName());
					ttm.setLastModifiedBy(principal.getName());
					if (!teeTotalStudList.contains(ttm.getUsername())) {
						teeTotalStudList.add(ttm.getUsername());
					}
					teeTotalMarksList.add(ttm);

				} else {

					TeeTotalMarks ttm = new TeeTotalMarks();
					if (!excelReader.ISVALIDINPUT(String.valueOf(mapper.get("Total Marks")))) {
						setError(redirectAttributes, "Entered marks for student is not valid");
						return "redirect:/evaluateTee";
					}
					double totalMarks = Double.valueOf((String) mapper.get("Total Marks"));
					if (Integer.parseInt(teeBeanDB.getExternalMarks()) < totalMarks) {
						setError(redirectAttributes, "Entered marks for student is not valid");
						return "redirect:/evaluateTee";
					}

					ttm.setTeeId(Long.valueOf(teeId));
					ttm.setUsername((String) mapper.get("SAPID"));
					ttm.setTeeTotalMarks(String.valueOf(round(totalMarks, 2)));
					ttm.setIsAbsent("N");
					if ("Y".equals(teeBeanDB.getScaledReq())) {

						double multiplyVal = Double.parseDouble(teeBeanDB.getScaledMarks()) * totalMarks;
						double scaledValue = 0.0;
						scaledValue = multiplyVal / Double.parseDouble(teeBeanDB.getExternalMarks());

						ttm.setTeeScaledMarks(String.valueOf(round(scaledValue, 2)));
					}

					if ("draft".equals(saveAs)) {
						ttm.setSaveAsDraft("Y");
					} else {
						ttm.setFinalSubmit("Y");
					}
					ttm.setRemarks((String) mapper.get("REMARKS"));
					ttm.setActive("Y");
					ttm.setCreatedBy(principal.getName());
					ttm.setLastModifiedBy(principal.getName());
					if (!teeTotalStudList.contains(ttm.getUsername())) {
						teeTotalStudList.add(ttm.getUsername());
					}
					teeTotalMarksList.add(ttm);
				}

			}

			teeTotalMarksService.upsertBatch(teeTotalMarksList);

			if ("submit".equals(saveAs)) {
				if (teeBeanDB.getAssignedFaculty().contains(",")) {
					int totalSubmitted = teeTotalMarksService.getDistinctSubmittedUsernamesByActiveTeeId(teeId).size();
					int totalAllocated = teeStudentBatchwiseService.getAllByActiveTeeId(teeId).size();
					if (totalSubmitted == totalAllocated) {
						teeBeanService.updateTeeToSubmitted(teeId, Utils.getInIST());
					}
				} else {
					teeBeanService.updateTeeToSubmitted(teeId, Utils.getInIST());
				}

			}

			setSuccess(redirectAttributes, "File uploaded successfully");

			if (isAllSubmitted) {
				return "redirect:/showEvaluatedTeeMarks";
			} else {
				return "redirect:/evaluateTee";
			}

		} catch (

		Exception ex) {

			setError(redirectAttributes, "Error in uploading marks");

			logger.error("Exception", ex);
		}
		return "redirect:/evaluateTee";

	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTeeReportForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTeeReportForm(Model m, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<Program> programList = programService.findAllActive();
		m.addAttribute("programList", programList);

		List<String> acadYearCodeList = courseService.findAcadYearCode();

		m.addAttribute("acadYearCodeList", acadYearCodeList);

		return "tee/teeReport";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getSessionByParamForTeeReport", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getSessionByParamForTeeReport(@RequestParam(name = "acadYear") String acadYear,
			@RequestParam(name = "campusId") String campusId,

			Principal principal) {
		String json = "";
		String username = principal.getName();
		List<Course> moduleComponentListByYearAndCampus = courseService
				.acadSessionListByAcadYearAndCampusForTee(acadYear, campusId);

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
			logger.error("Exception", e);
		}
		return json;
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTeeReport", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTeeReport(Model m, HttpServletResponse response, Principal principal,
			@RequestParam String acadYear, @RequestParam String acadSession, @RequestParam String programId,
			@RequestParam(required = false) String campusId, @RequestParam String reportType) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		InputStream is = null;
		String filePath = "";
		try {

			if ("subjWise".equals(reportType)) {
				ServletOutputStream out = response.getOutputStream();
				if (programId.contains(",")) {

					List<String> programs = Arrays.asList(programId.split(","));
					File folderPath = new File(downloadAllFolder + "/" + "compiledReport");

					for (String pid : programs) {
						String multiProgFilePath = "";
						if ("subjWise".equals(reportType)) {
							multiProgFilePath = getFilePathOfSubjectWiseReportForTee(pid, acadYear, acadSession,
									campusId, userdetails1.getCollegeName());
						}
						File fileP = new File(multiProgFilePath);

						if (!folderPath.exists()) {
							folderPath.mkdir();
						}
						String newFilePath = folderPath.getAbsolutePath() + File.separator + fileP.getName();

						String filename = "Student review TEE Marks" + pid + ".xlsx";
						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-Disposition", "attachment; filename=" + filename);

						is = new FileInputStream(multiProgFilePath);

						FileUtils.copyFile(fileP, new File(newFilePath));

					}
					String zipFile = "Consolidated-TEE-Report.zip";
					response.setContentType("Content-type: text/zip");
					response.setHeader("Content-Disposition", "attachment; filename=" + zipFile + "");
					pack(folderPath.getAbsolutePath(), out);

					FileUtils.deleteDirectory(folderPath);
				} else {
					if ("subjWise".equals(reportType)) {
						filePath = getFilePathOfSubjectWiseReportForTee(programId, acadYear, acadSession, campusId,
								userdetails1.getCollegeName());
					}
					String filename = "Student review TEE Marks.xlsx";
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
				String filename = "Student-review-TEE-Marks-" + acadYear + "-" + acadSession + ".xlsx";
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
		} catch (Exception ex) {
			logger.info("Error writing file to output stream. Filename was '{}'", ex);
			throw new RuntimeException("IOError writing file to output stream" + ex);
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}
		}

		return null;
	}

	public String getFilePathOfSubjectWiseReportForTee(String programId, String acadYear, String acadSession,
			String campusId, String collegeName) {
		List<String> headers = new ArrayList();
		String h[] = { "SAPID", "Name", "Roll.No", "Obtained Marks", "Total TEE Marks", "Status", "Remarks" };
		headers = Arrays.asList(h);

		String fileName = "TEE-Report" + Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "TEE-Report" + programId
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

		List<TeeTotalMarks> getTeeTotalMarksByParam = teeTotalMarksService.getTeeTotalMarksByParam(acadYear,
				acadSession, programId, campusId);

		Map<String, Course> courseMapper = new HashMap<>();
		Map<String, List<TeeTotalMarks>> teeTotalMarksModuleWise = new HashMap<>();
		for (TeeTotalMarks ttm : getTeeTotalMarksByParam) {

			if (!courseMapper.containsKey(ttm.getModuleId())) {
				Course c = new Course();
				c.setModuleId(ttm.getModuleId());
				c.setModuleName(ttm.getModuleName());
				c.setAcadYear(ttm.getAcadYear());
				c.setAcadSession(ttm.getAcadSession());
				c.setModuleAbbr(ttm.getModuleAbbr());

				courseMapper.put(ttm.getModuleId(), c);
			}
		}
		for (String moduleId : courseMapper.keySet()) {
			List<TeeTotalMarks> itmList = getTeeTotalMarksByParam.stream().filter(o -> o.getModuleId().equals(moduleId))
					.collect(Collectors.toList());
			teeTotalMarksModuleWise.put(moduleId, itmList);
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
			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(programName + "-" + acadSession + "-" + batch);
			Cell subjNameAbbrCell = moduleNameAbbrRow.createCell(0);
			Cell facultiesCell = facultiesRow.createCell(0);
			Cell reviewCell = reviewRow.createCell(0);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			String subjNameVal = courseMapper.get(moduleId).getModuleName() + "("
					+ courseMapper.get(moduleId).getModuleAbbr() + ")";
			subjNameAbbrCell.setCellValue(subjNameVal);
			reviewCell.setCellValue("Student review TEE Marks");
			facultiesCell.setCellValue(faculties);
			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);
			subjNameAbbrCell.setCellStyle(centerStyle);
			facultiesCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(7).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
			Row emptyRow = sheet.createRow(rowNum++);

			Row headerRow = sheet.createRow(rowNum++);

			Row row = sheet.createRow(rowNum++);
			row.setRowStyle(borderStyle);
			for (int colNum = 0; colNum < headers.size(); colNum++) {

				Cell cell = headerRow.createCell(colNum);
				cell.setCellValue(headers.get(colNum));
				cell.setCellStyle(headerStyle);

			}

			for (TeeTotalMarks ttm : teeTotalMarksModuleWise.get(moduleId)) {
				Row rowd = sheet.createRow(++rowNum);
				int colNum = 0;

				CellStyle wrapstyle = workbook.createCellStyle();
				rowd.createCell(colNum++).setCellValue(ttm.getUsername());

				rowd.createCell(colNum++).setCellValue(ttm.getStudentName());
				rowd.createCell(colNum++).setCellValue(ttm.getRollNo());

				rowd.createCell(colNum++).setCellValue(ttm.getTeeTotalMarks());
				rowd.createCell(colNum++).setCellValue(ttm.getExternalMarks());

				rowd.createCell(colNum++).setCellValue(ttm.getPassFailStatus());
				rowd.createCell(colNum++).setCellValue(ttm.getRemarks());

			}
			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
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

		return filePath;
	}

	public String getFilePathOfReport(String programId, String acadYear, String acadSession, String campusId,
			String collegeName) {
		List<String> headers = new ArrayList();
		String h[] = { "SAPID", "Name", "Roll.No", "Modules", "Obtained Marks", "Total TEE Marks", "Status",
				"Remarks" };
		headers = Arrays.asList(h);

		String fileName = "TEE-Report-" + programId.replaceAll(",", "-") + "-"
				+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST()) + ".xlsx";
		String filePath = downloadAllFolder + File.separator + "TEE-Report-" + programId.replaceAll(",", "-") + "-"
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

		if (programId.contains(",")) {

			List<String> programIdList = Arrays.asList(programId.split(","));

			for (String p : programIdList) {

				String programName = programService.findByID(Long.valueOf(p)).getProgramName();
				int rowNum = 0;
				logger.info("program name" + programName);
				XSSFSheet sheet = workbook.createSheet(p);

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
				String batch = acadYear;

				Cell programCell = programRow.createCell(0);
				programCell.setCellValue(programName + "-" + acadSession + "-" + batch);

				Cell reviewCell = reviewRow.createCell(0);
				SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

				formatter = new SimpleDateFormat("dd MMMM yyyy");
				String strDate = formatter.format(Utils.getInIST());
				reviewCell.setCellValue("Student review TEE Marks");

				collegeCell.setCellStyle(centerStyle);
				reviewCell.setCellStyle(centerStyle);
				programCell.setCellStyle(centerStyle);

				Row dateRow = sheet.createRow(rowNum++);

				dateRow.createCell(7).setCellValue(strDate);
				CellStyle border = workbook.createCellStyle();
				Row emptyRow = sheet.createRow(rowNum++);

				Row headerRow = sheet.createRow(rowNum++);

				Row row = sheet.createRow(rowNum++);
				row.setRowStyle(borderStyle);
				for (int colNum = 0; colNum < headers.size(); colNum++) {

					Cell cell = headerRow.createCell(colNum);
					cell.setCellValue(headers.get(colNum));
					cell.setCellStyle(headerStyle);

				}

				List<TeeTotalMarks> getTeeTotalMarksByParam = teeTotalMarksService.getTeeTotalMarksByParam(acadYear,
						acadSession, p, campusId);

				HashSet<String> sapIds = new LinkedHashSet<>();
				Map<String, String> mapSapRoll = new HashMap<>();
				Map<String, String> mapSapName = new HashMap<>();
				for (TeeTotalMarks ttm : getTeeTotalMarksByParam) {
					sapIds.add(ttm.getUsername());
					mapSapRoll.put(ttm.getUsername(), ttm.getRollNo());
					mapSapName.put(ttm.getUsername(), ttm.getStudentName());
				}

				Map<String, List<TeeTotalMarks>> componentsMapTee = new HashMap<>();

				for (String s : sapIds) {
					List<TeeTotalMarks> teeaList = getTeeTotalMarksByParam.stream()
							.filter(o -> o.getUsername().equals(s)).collect(Collectors.toList());
					componentsMapTee.put(s, teeaList);
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
					for (TeeTotalMarks ttm : componentsMapTee.get(s)) {
						if (count == 0) {
							rowd.createCell(colNum++).setCellValue(ttm.getModuleName());
							rowd.createCell(colNum++).setCellValue(ttm.getTeeTotalMarks());
							rowd.createCell(colNum++).setCellValue(ttm.getExternalMarks());

							rowd.createCell(colNum++).setCellValue(ttm.getPassFailStatus());
							rowd.createCell(colNum++).setCellValue(ttm.getRemarks());

						} else {

							Row newRow = sheet.createRow(++rowNum);
							int colNum1 = 0;

							CellStyle wrapstyle1 = workbook.createCellStyle();
							newRow.createCell(colNum1++).setCellValue("");

							newRow.createCell(colNum1++).setCellValue(mapSapName.get(""));
							newRow.createCell(colNum1++).setCellValue(mapSapRoll.get(""));
							newRow.createCell(colNum1++).setCellValue(ttm.getModuleName());
							newRow.createCell(colNum1++).setCellValue(ttm.getTeeTotalMarks());
							newRow.createCell(colNum1++).setCellValue(ttm.getExternalMarks());

							newRow.createCell(colNum1++).setCellValue(ttm.getPassFailStatus());
							newRow.createCell(colNum1++).setCellValue(ttm.getRemarks());

						}
						count++;
					}
					Row rowd1 = sheet.createRow(++rowNum);
				}
				sheet.setColumnWidth(0, 3500);
				sheet.autoSizeColumn(1);
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

			String batch = acadYear;

			Cell programCell = programRow.createCell(0);
			programCell.setCellValue(programService.findByID(Long.valueOf(programId)).getProgramName() + " "
					+ acadSession + " " + batch);

			Cell reviewCell = reviewRow.createCell(0);
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			formatter = new SimpleDateFormat("dd MMMM yyyy");
			String strDate = formatter.format(Utils.getInIST());
			reviewCell.setCellValue("Student review TEE Marks");

			collegeCell.setCellStyle(centerStyle);
			reviewCell.setCellStyle(centerStyle);
			programCell.setCellStyle(centerStyle);

			Row dateRow = sheet.createRow(rowNum++);

			dateRow.createCell(8).setCellValue(strDate);
			CellStyle border = workbook.createCellStyle();
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

			List<TeeTotalMarks> getTeeTotalMarksByParam = teeTotalMarksService.getTeeTotalMarksByParam(acadYear,
					acadSession, programId, campusId);

			HashSet<String> sapIds = new LinkedHashSet<>();
			Map<String, String> mapSapRoll = new HashMap<>();
			Map<String, String> mapSapName = new HashMap<>();
			for (TeeTotalMarks ttm : getTeeTotalMarksByParam) {
				sapIds.add(ttm.getUsername());
				mapSapRoll.put(ttm.getUsername(), ttm.getRollNo());
				mapSapName.put(ttm.getUsername(), ttm.getStudentName());
			}
			Map<String, List<TeeTotalMarks>> componentsMapTee = new HashMap<>();

			for (String s : sapIds) {
				List<TeeTotalMarks> teeList = getTeeTotalMarksByParam.stream().filter(o -> o.getUsername().equals(s))
						.collect(Collectors.toList());
				componentsMapTee.put(s, teeList);
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
				for (TeeTotalMarks ttm : componentsMapTee.get(s)) {
					if (count == 0) {
						rowd.createCell(colNum++).setCellValue(ttm.getModuleName());
						rowd.createCell(colNum++).setCellValue(ttm.getTeeTotalMarks());
						rowd.createCell(colNum++).setCellValue(ttm.getExternalMarks());

						rowd.createCell(colNum++).setCellValue(ttm.getPassFailStatus());
						rowd.createCell(colNum++).setCellValue(ttm.getRemarks());

					} else {
						Row newRow = sheet.createRow(++rowNum);
						int colNum1 = 0;

						CellStyle wrapstyle1 = workbook.createCellStyle();
						newRow.createCell(colNum1++).setCellValue("");

						newRow.createCell(colNum1++).setCellValue(mapSapName.get(""));
						newRow.createCell(colNum1++).setCellValue(mapSapRoll.get(""));
						newRow.createCell(colNum1++).setCellValue(ttm.getModuleName());
						newRow.createCell(colNum1++).setCellValue(ttm.getTeeTotalMarks());
						newRow.createCell(colNum1++).setCellValue(ttm.getExternalMarks());

						newRow.createCell(colNum1++).setCellValue(ttm.getPassFailStatus());
						newRow.createCell(colNum1++).setCellValue(ttm.getRemarks());

					}
					count++;
				}

				Row rowd1 = sheet.createRow(++rowNum);

			}

			sheet.setColumnWidth(0, 3500);
			sheet.autoSizeColumn(1);
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

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/downloadTeeRaiseQueryReport", method = { RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> downloadTeeRaiseQueryReport(Model m, Principal p,
			HttpServletResponse response, @RequestParam(required = false) String acadYear) throws URIException {
		Token userdetails1 = (Token) p;
		m.addAttribute("webPage", new WebPage("viewAssignment", "Search Assignments", true, true));

		List<TeeTotalMarks> ttmRaiseQueryList = new ArrayList<>();

		ttmRaiseQueryList = teeTotalMarksService.getRaiseQueriesForTee(acadYear, userdetails1.getAuthorities(),
				p.getName());

		List<String> validateHeaders = new ArrayList<String>(Arrays.asList("Tee Name", "AcadYear", "Session", "Roll No",
				"Student SAPID", "Student-Name", "Student-EmailId", "Program", "Subject", "Total Marks Obtained",
				"Query Raised Date", "Query", "Assigned Faculty"));

		String fileName = null;

		String filePath = null;
		ExcelCreater excelCreater = new ExcelCreater();
		File file = null;
		InputStream is = null;
		try {

			List<Map<String, Object>> listOfMapOfRaisedQueries = new ArrayList<>();
			fileName = "studentTeeQueries.xlsx";
			String folderPath = downloadAllFolder;
			filePath = downloadAllFolder + "/" + fileName;

			for (TeeTotalMarks ttm : ttmRaiseQueryList) {
				Map<String, Object> mapOfQueries = new HashMap<>();
				mapOfQueries.put("Tee Name", ttm.getTeeName());
				mapOfQueries.put("AcadYear", ttm.getAcadYear());
				mapOfQueries.put("Session", ttm.getAcadSession());
				mapOfQueries.put("Student SAPID", ttm.getUsername());
				mapOfQueries.put("Student-Name", ttm.getStudentName());
				mapOfQueries.put("Program", ttm.getProgramName());
				mapOfQueries.put("Subject", ttm.getModuleName());
				mapOfQueries.put("Total Marks Obtained", ttm.getTeeTotalMarks());
				mapOfQueries.put("Query", ttm.getQuery());
				mapOfQueries.put("Assigned Faculty", ttm.getAssignedFaculty());
				mapOfQueries.put("Roll No", ttm.getRollNo());
				mapOfQueries.put("Student-EmailId", ttm.getEmail());
				mapOfQueries.put("Query Raised Date", ttm.getRaiseQDate());

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

	// Tee Support Admin Mappings
	@Secured({"ROLE_SUPPORT_ADMIN","ROLE_ADMIN","ROLE_EXAM"})
	@RequestMapping(value = "/teeListBySupportAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String teeListBySupportAdmin(Model m, Principal principal, @RequestParam(required = false) String teeId) {

		List<TeeBean> teeList = new ArrayList<>();

		Token userdetails1 = (Token) principal;

		m.addAttribute("webPage", new WebPage("searchCourse", "Search TEE", true, false));
		// 06-04-2021
		if (userdetails1.getAuthorities().contains(Role.ROLE_SUPPORT_ADMIN)
				|| userdetails1.getAuthorities().contains(Role.ROLE_EXAM)
				|| userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			if (teeId != null) {
				teeList = teeBeanService.findDivisionWiseTeeListByParentTeeForSupportAdmin(teeId);
			} else {
				teeList = teeBeanService.findTeeListByProgramForSupportAdmin();
			}

			// New Changes on 09-04-2021 to check tcs flag
			for (TeeBean tee : teeList) {
				boolean checkTcsFlagForIca = isTeeMarksSentToTcs(tee);
				if (checkTcsFlagForIca == false) {
					tee.setFlagTcs("F");
				} else {
					tee.setFlagTcs("S");
				}
			}
			//

		}
		m.addAttribute("Program_Name", userdetails1.getProgramName());
		m.addAttribute("teeList", teeList);
		return "tee/teeListSupportAdmin";

	}

	@Secured({"ROLE_SUPPORT_ADMIN","ROLE_ADMIN","ROLE_EXAM"})
	@RequestMapping(value = "/updateTeeDateBySupportAdminForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String updateIcaDateBySupportAdminForm(Model m, Principal principal, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs, @RequestParam String teeId) {

		teeBean.setActive("Y");
		teeBean.setLastModifiedBy(principal.getName());
		m.addAttribute("teeId", teeId);
		m.addAttribute("teeBeanS", new TeeBean());

		m.addAttribute("webPage", new WebPage("searchCourse", "Update Tee", true, false));

		return "tee/updateDateBySupportAdmin";
	}

	@Secured({"ROLE_SUPPORT_ADMIN","ROLE_ADMIN","ROLE_EXAM"})
	@RequestMapping(value = "/updateTeeDateBySupportAdmin", method = RequestMethod.POST)
	public String updateTeeDateBySupportAdmin(Model m, Principal principal, @ModelAttribute TeeBean teeBean,
			RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		TeeBean teeBeanDAO = teeBeanService.findByID(teeBean.getId());

		teeBeanDAO.setEndDate(teeBean.getEndDate());
		teeBeanDAO.setLastModifiedBy(username);
		teeBeanDAO.setPublishedDate(teeBean.getPublishedDate());

		List<Long> teeIds = new ArrayList<>();
		try {
			if ((null == teeBean.getEndDate() || teeBean.getEndDate().equals(""))
					&& (null == teeBean.getPublishedDate() || teeBean.getPublishedDate().equals(""))) {
				redirectAttrs.addAttribute("icaId", teeBean.getId());
				setError(redirectAttrs, "Select EndDate or PublishDate");
				return "redirect:/updateIcaDateBySupportAdminForm";
			} else {
				teeBeanService.updateTeeDate(teeBeanDAO.getEndDate(), teeBeanDAO.getPublishedDate(),
						teeBeanDAO.getId());

				if ("Y".equals(teeBeanDAO.getIsTeeDivisionWise()) && teeBeanDAO.getParentTeeId() == null) {
					List<TeeBean> updatedTeeBeanList = new ArrayList<>();
					List<TeeBean> TeeListByParentTeeId = teeBeanService.getTeeIdsByParentTeeIds(teeBean.getId());

					// teeBeanService.update(icaBean);

					for (TeeBean ib : TeeListByParentTeeId) {

						String ibParentId = ib.getParentTeeId();

						String endDate = teeBean.getEndDate();
						String publishedDate = teeBean.getPublishedDate();

						teeIds.add(ib.getId());
					}
					teeBeanService.updateTeeDateForDivision(teeBean.getEndDate(), teeBean.getPublishedDate(), teeIds);

					setSuccess(redirectAttrs, "Tee Date Updated Successfully");

				}

				setSuccess(redirectAttrs, "Tee Date Updated Successfully");
				return "redirect:/teeListBySupportAdmin";
			}
		} catch (Exception ex) {
			setError(redirectAttrs, "Error While Updating Tee Date");
			return "redirect:/teeListBySupportAdmin";

		}

	}

	@Secured({"ROLE_SUPPORT_ADMIN","ROLE_ADMIN","ROLE_EXAM"})
	@RequestMapping(value = "/updateTeeDateBySupportAdminWithoutSubmit", method = RequestMethod.POST)
	public String updateTeeDateBySupportAdminWithoutSubmit(Model m, Principal principal,
			@ModelAttribute("teeBeanS") TeeBean teeBeanS, RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		TeeBean teeBeanDAOS = teeBeanService.findByID(teeBeanS.getId());
		teeBeanDAOS.setEndDate(teeBeanS.getEndDate());
		teeBeanDAOS.setLastModifiedBy(username);
		teeBeanDAOS.setPublishedDate(teeBeanS.getPublishedDate());

		List<Long> teeIds = new ArrayList<>();
		try {
			if ((null == teeBeanS.getEndDate() || teeBeanS.getEndDate().equals(""))
					&& (null == teeBeanS.getPublishedDate() || teeBeanS.getPublishedDate().equals(""))) {
				redirectAttrs.addAttribute("icaId", teeBeanS.getId());
				setError(redirectAttrs, "Select EndDate or PublishDate");
				return "redirect:/updateTeeDateBySupportAdminForm";
			} else {
				teeBeanService.updateTeeDateWithoutSubmit(teeBeanDAOS.getEndDate(), teeBeanDAOS.getPublishedDate(),
						teeBeanDAOS.getId());

				if ("Y".equals(teeBeanDAOS.getIsTeeDivisionWise()) && teeBeanDAOS.getParentTeeId() == null) {
					List<TeeBean> updatedIcaBeanList = new ArrayList<>();
					List<TeeBean> icaListByParentIcaId1 = teeBeanService.getTeeIdsByParentTeeIds(teeBeanS.getId());

					for (TeeBean ib : icaListByParentIcaId1) {
						String ibParentId = ib.getParentTeeId();

						String endDate = teeBeanS.getEndDate();
						String publishedDate = teeBeanS.getPublishedDate();
						teeIds.add(ib.getId());
					}
					teeBeanService.updateTeeDateForDivisionWithoutSubmit(teeBeanS.getEndDate(),
							teeBeanS.getPublishedDate(), teeIds);

					setSuccess(redirectAttrs, "Tee Date Updated Successfully");

				}

				setSuccess(redirectAttrs, "Tee Date Updated Successfully");
				return "redirect:/teeListBySupportAdmin";
			}
		} catch (Exception ex) {
			setError(redirectAttrs, "Error While Updating Tee Date");
			return "redirect:/teeListBySupportAdmin";

		}

	}

	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/uploadTeeMarksChangeForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadTeeMarksChangeForm(Model m, Principal principal, RedirectAttributes redirectAttributes) {
		// m.addAttribute("webPage", new WebPage("UploadStudentsToDeallocate","Upload",
		// false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		return "tee/updateTeeMarksBySupportAdmin";

	}

	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/downloadTeeMarksChangeTemplate", method = { RequestMethod.GET, RequestMethod.POST })
	public String downloadTeeMarksChangeTemplate(RedirectAttributes redirectAttrs, HttpServletRequest request,
			HttpServletResponse response) {
		OutputStream outStream = null;
		FileInputStream inputStream = null;

		try {
			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = (XSSFSheet) workbook.createSheet("TEE Template");

			// create header row
			Row r = sheet.createRow(0);
			// set header value
			r.createCell(0).setCellValue("TEE ID");
			r.createCell(1).setCellValue("SAPID");
			r.createCell(2).setCellValue("Total Marks");
			r.createCell(3).setCellValue("REMARKS");

			String folderPath = baseDir + "/" + app + "/" + "TeeMarksChangeTemplate";
			File folderP = new File(folderPath);
			if (!folderP.exists()) {
				folderP.mkdir();
			}
			String filePath = folderP.getAbsolutePath() + File.separator + "TeeMarksChangeTemplate.xlsx";
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
			}
			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);

			if (!downloadFile.exists()) {
				setError(redirectAttrs, "Error in download template");
				return "redirect:/uploadTeeMarksChangeForm";
			}
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
			String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			outStream = response.getOutputStream();

			IOUtils.copy(inputStream, outStream);
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

	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/uploadTeeMarksChangeTemplate", method = { RequestMethod.POST })
	public String uploadTeeMarksChangeTemplate(@ModelAttribute TeeBean teeBean,
			@RequestParam("file") MultipartFile file, Model m, RedirectAttributes redirectAttributes,
			Principal principal) {

		List<String> headers = new ArrayList<String>(Arrays.asList("TEE ID", "SAPID", "Total Marks", "REMARKS"));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		ExcelReader excelReader = new ExcelReader();

		try {
			String correctOpt = "";
			List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, headers);
			// List<Map<String, Object>> maps =
			// excelReader.readExcelFileUsingColumnNum(file,validateHeaders);

			List<String> studentList = new ArrayList<String>();
			List<TeeTotalMarks> updateMarksList = new ArrayList<>();
			if (maps.size() == 0) {
				setNote(m, "Excel File is empty");
			} else {
				int count = 0;
				for (Map<String, Object> mapper : maps) {
					if (mapper.get("Error") != null) {
						setNote(m, "Error--->" + mapper.get("Error"));
					} else {
						TeeTotalMarks ttm = new TeeTotalMarks();
						Long teeId = Long.valueOf(mapper.get("TEE ID").toString().trim());
						TeeBean teeBeanDB = teeBeanService.findByID(teeId);
						if (!excelReader.ISVALIDINPUT(String.valueOf(mapper.get("TEE ID")))) {
							setError(redirectAttributes, "Entered TEE ID is not valid " + teeId);
							return "redirect:/uploadTeeMarksChangeForm";
						}
						ttm.setTeeId(Long.valueOf(mapper.get("TEE ID").toString().trim()));
						if (!excelReader.ISVALIDINPUT(String.valueOf(mapper.get("SAPID")))) {
							setError(redirectAttributes,
									"Entered student's SAPID is not valid " + String.valueOf(mapper.get("SAPID")));
							return "redirect:/uploadTeeMarksChangeForm";
						}
						ttm.setUsername(mapper.get("SAPID").toString().trim());
						if (!excelReader.ISVALIDINPUT(String.valueOf(mapper.get("Total Marks")))) {
							setError(redirectAttributes, "Entered marks for student is not valid "
									+ String.valueOf(mapper.get("Total Marks")));
							return "redirect:/uploadTeeMarksChangeForm";
						}
						double totalMarks = Double.valueOf((String) mapper.get("Total Marks"));
						ttm.setTeeTotalMarks(String.valueOf(round(totalMarks, 2)));
						if (null != teeBeanDB.getScaledReq() && "Y".equals(teeBeanDB.getScaledReq())) {

							double multiplyVal = Double.parseDouble(teeBeanDB.getScaledMarks()) * totalMarks;
							double scaledValue = 0.0;
							scaledValue = multiplyVal / Double.parseDouble(teeBeanDB.getExternalMarks());

							ttm.setTeeScaledMarks(String.valueOf(round(scaledValue, 2)));
						}
						ttm.setRemarks(mapper.get("REMARKS").toString().trim());

						updateMarksList.add(ttm);
					}
				}
				for (TeeTotalMarks ttm : updateMarksList) {
					int updated = teeTotalMarksService.updateMarksFromSupportAdmin(ttm);
					count = count + updated;
				}
				setSuccess(m, "Tee Marks updated successfully for " + count + " number of Student.");
			}
		} catch (Exception ex) {
			setError(m, "Error in uploading file");
			ex.printStackTrace();
		}

		return "tee/updateTeeMarksBySupportAdmin";
	}

	/// new tee changes for assignment marks to auto assign in tee

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/assignAssignmentMarksToTeeForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String assignAssignmentMarksToTeeForm(@RequestParam Long teeId,
			@RequestParam(required = false) String courseId, Principal p,
			@RequestParam(required = false) String showEvalBtn, Model m, RedirectAttributes redirectAttrs) {

		m.addAttribute("webPage", new WebPage("addTestMarksToIcaForm", "Add Test Marks To ICA", true, false));
		String username = p.getName();
		try {

			TeeBean teeBean = teeBeanService.findByID(teeId);

			String currentDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", Utils.getInIST());

			if (currentDate.compareTo(teeBean.getEndDate()) > 0) {
				setNote(redirectAttrs, "Cannot Evaluate, ICA Deadline is over");
				return "redirect:/searchTeeList";
			}

			else if (currentDate.compareTo(teeBean.getStartDate()) < 0) {
				setNote(redirectAttrs, "Cannot Evaluate, ICA has not started yet.");
				return "redirect:/searchTeeList";
			} else {
				if ("Y".equals(teeBean.getIsSubmitted())) {
					setNote(redirectAttrs, "TEE Already Evaluated");
					return "redirect:/searchTeeList";
				}
			}

			List<Assignment> assignmentListForTee = assignmentService.getAssignmentsForTeeModule(username,
					teeBean.getModuleId(), teeBean.getAcadYear(), courseId);

			String courseIdForDivWis = null;
			List<Assignment> coursesForTee = new ArrayList<>();
			if ("Y".equals(teeBean.getIsTeeDivisionWise())) {
				courseIdForDivWis = teeBean.getEventId();
				coursesForTee = assignmentService.getCoursesForAssignmentTee(teeBean.getModuleId(),
						teeBean.getAcadYear(), username, courseIdForDivWis);
			} else {
				coursesForTee = assignmentService.getCoursesForAssignmentTee(teeBean.getModuleId(),
						teeBean.getAcadYear(), username, courseIdForDivWis);
			}

			Map<Long, List<String>> testIdsForIcaMap = new HashMap<>();
			for (Assignment t : coursesForTee) {
				logger.info("course1" + t.getCourseId());
				TeeBean teeAssignDB = teeBeanService.getTeeAssignments(teeId, t.getCourseId());
				logger.info("ica null");
				if (teeAssignDB == null) {
					TeeBean teeAssgn = new TeeBean();
					teeAssgn.setId(teeId);
					teeAssgn.setCourseId(String.valueOf(t.getCourseId()));
					teeBeanService.insertInTeeAssignments(teeAssgn);
					m.addAttribute("assignmentIdsForTee", null);
					logger.info("idsssss if null");
				} else {
					logger.info("icaTest DB" + teeAssignDB);
					if (teeAssignDB.getAssignmentIdsForTee() != null) {
						logger.info("idsssss111" + Arrays.asList(teeAssignDB.getAssignmentIdsForTee().split(",")));
						testIdsForIcaMap.put(t.getCourseId(),
								Arrays.asList(teeAssignDB.getAssignmentIdsForTee().split(",")));
						m.addAttribute("assignmentIdsForTee", testIdsForIcaMap);
					}
				}
			}

			Map<Long, List<Assignment>> mapper = new HashMap<>();
			Map<Long, String> courseMap = new HashMap<>();
			Set<Long> courses = new HashSet<>();

			assignmentListForTee.forEach(i -> courses.add(i.getCourseId()));

			for (Long c : courses) {

				List<Assignment> assignmentList = new ArrayList<>();
				for (Assignment t : assignmentListForTee) {
					if (!courseMap.containsKey(c)) {
						Course courseDB = courseService.findByID(c);
						courseMap.put(c, courseDB.getCourseName());
					}

					if (c.equals(t.getCourseId())) {

						assignmentList.add(t);
					}
				}
				mapper.put(c, assignmentList);
			}
			logger.info("mapper is:" + mapper);
			logger.info("courseMap is:" + courseMap);
			m.addAttribute("assgnList", assignmentListForTee);
			m.addAttribute("mapper", mapper);
			m.addAttribute("courseMap", courseMap);
			m.addAttribute("coursesForTee", coursesForTee);

			if (courseId != null) {

				TeeBean teeAssgnDB = teeBeanService.getTeeAssignments(teeId, Long.valueOf(courseId));

				// IcaBean ica = new IcaBean();
				if (teeAssgnDB != null) {
					if (teeAssgnDB.getAssignmentIdsForTee() != null) {
						teeAssgnDB = teeBeanService.getAllTeeAssignments(teeId, Long.valueOf(courseId));

					}
					teeAssgnDB.setId(teeId);
					logger.info("ica bean when course id is not null" + teeAssgnDB);
					m.addAttribute("tee", teeAssgnDB);
				} else {
					TeeBean tee = new TeeBean();
					tee.setId(teeId);
					tee.setCourseId(courseId);
					m.addAttribute("tee", tee);
				}
			} else {
				TeeBean tee = new TeeBean();
				tee.setId(teeId);
				m.addAttribute("tee", tee);
			}

			m.addAttribute("teeId", teeId);
			m.addAttribute("courseId", courseId);
			if (courseId != null) {
				m.addAttribute("showTests", "true");
			} else {
				m.addAttribute("showTests", "false");
			}

			int showEval = teeBeanService.getTeeAssignmentNC(teeId);

			if (showEval == 0) {
				m.addAttribute("showEvalBtn", "true");
			} else {
				m.addAttribute("showEvalBtn", "false");
			}

			return "tee/addAssignmentMarksToTee";

		} catch (Exception ex) {

			logger.error("Exception", ex);
			setError(redirectAttrs, "Error in add displaying form");
			return "redirect:/searchTeeList";
		}

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/saveAssignmentMarksToTee", method = { RequestMethod.GET, RequestMethod.POST })
	public String saveAssignmentMarksToTee(@ModelAttribute TeeBean tee, Model m, RedirectAttributes redirectAttrs,
			Principal p) {

		m.addAttribute("webPage", new WebPage("addTestMarksToIcaForm", "Add Test Marks To ICA", true, false));
		String username = p.getName();
		try {
			logger.info("tee bean is:" + tee.getId());
			logger.info("tee bean is" + tee);
			redirectAttrs.addAttribute("teeId", tee.getId());
			redirectAttrs.addAttribute("courseId", tee.getCourseId());
			if (tee.getModeOfAddingAssignmentMarks().equals("bestOf")) {
				if (tee.getAssignmentIds().size() <= tee.getBestOf()) {
					setError(redirectAttrs, "No Of Assignments Selected is less than best of number");
					return "redirect:/assignAssignmentMarksToTeeForm";
				} else {
					List<Assignment> assignments = assignmentService.getAssignmentsByIds(tee.getAssignmentIds());
					if (assignments.size() != 1) {
						setError(redirectAttrs, "Assignments Which are selected should carry same marks.");
						return "redirect:/assignAssignmentMarksToTeeForm";
					}
				}
			}

			TeeBean teeAssgnDB = teeBeanService.getTeeAssignments(tee.getId(), Long.valueOf(tee.getCourseId()));
			/*
			 * if (teeAssgnDB.getAssignmentIdsForTee() != null) { List<String> updAssgnIds =
			 * new ArrayList<>(); List<String> assgnIdsFromDb =
			 * Arrays.asList(teeAssgnDB.getAssignmentIdsForTee().split(",")); List<String>
			 * selectedAssgnIds = tee.getAssignmentIds();
			 * updAssgnIds.addAll(assgnIdsFromDb); updAssgnIds.addAll(selectedAssgnIds);
			 * logger.info("updated testIds" + updAssgnIds);
			 * tee.setAssignmentIds(updAssgnIds); logger.info("updated ica ids" +
			 * tee.getAssignmentIds()); }
			 */

			TeeBean teeBean = teeBeanService.findByID(tee.getId());

			Map<String, Double> responseMsg = addAssignmentMarksToTee(tee, username);
			logger.info("Response is:" + responseMsg);

			if (responseMsg.isEmpty() || tee.getAssignmentIds().size() == 0) {
				setError(redirectAttrs, "Test Data Not Found");
				return "redirect:/assignAssignmentMarksToTeeForm";
			}
			List<TeeTotalMarks> teeTotalMarksList = new ArrayList<>();
			for (String s : responseMsg.keySet()) {
				double studMarks = responseMsg.get(s);
				double totalScore = Double.valueOf(teeBean.getExternalMarks());
				TeeTotalMarks teeTM = new TeeTotalMarks();

				teeTM.setTeeId(tee.getId());

				teeTM.setUsername(s);
				teeTM.setTeeTotalMarks(String.valueOf(studMarks));
				teeTM.setActive("Y");
				teeTM.setCreatedBy(username);
				teeTM.setLastModifiedBy(username);
				if ("Y".equals(teeBean.getScaledReq())) {
					double scaleMarks = Double.valueOf(teeBean.getScaledMarks());
					double divRes = studMarks / totalScore;
					double scaledScoreObt = divRes * scaleMarks;

					teeTM.setTeeScaledMarks(String.valueOf(round(scaledScoreObt, 2)));

				}
				teeTM.setSaveAsDraft("Y");
				teeTotalMarksList.add(teeTM);

			}

			teeTotalMarksService.upsertBatch(teeTotalMarksList);
			String assignmentIds = (StringUtils.join(tee.getAssignmentIds(), ","));

			TeeBean teeUpd = new TeeBean();
			teeUpd.setId(tee.getId());
			teeUpd.setCourseId(tee.getCourseId());
			teeUpd.setAssignmentIdsForTee(assignmentIds);
			teeUpd.setModeOfAddingAssignmentMarks(tee.getModeOfAddingAssignmentMarks());
			if ("bestOf".equals(tee.getModeOfAddingAssignmentMarks())) {
				teeUpd.setBestOf(tee.getBestOf());
			}
			teeBeanService.updateInTeeAssignments(teeUpd);

			setSuccess(redirectAttrs, "Marks have been updated in TEE Successfully!!");

			int teeNC = teeBeanService.getTeeAssignmentNC(tee.getId());
			if (teeNC == 0) {
				redirectAttrs.addAttribute("showEvalBtn", "true");
			} else {
				redirectAttrs.addAttribute("showEvalBtn", "false");
			}
			return "redirect:/assignAssignmentMarksToTeeForm";

		} catch (Exception ex) {
			setError(redirectAttrs, "Error in updating Marks in TEE");
			logger.error("Exception", ex);

			return "redirect:/searchTeeList";
		}

	}

	public Map<String, Double> addAssignmentMarksToTee(TeeBean teeBean, String username) {
		logger.info("method entered");
		logger.info("mode is" + teeBean.getModeOfAddingAssignmentMarks());
		Map<String, List<Double>> usernameScoresMap = new HashMap<>();
		TeeBean teeBeanDB = teeBeanService.findByID(teeBean.getId());
		Map<String, Double> returnMap = new HashMap<>();
		double totalMarks = Double.valueOf(teeBeanDB.getExternalMarks());

		List<Assignment> assignments = assignmentService.getAssignmentsByIds(teeBean.getAssignmentIds());
		TeeBean teeDB = teeBeanService.findByID(teeBean.getId());
		try {
			if ("bestOf".equals(teeBean.getModeOfAddingAssignmentMarks())) {
				int bestOf = teeBean.getBestOf();
				List<StudentAssignment> studentAssignmentScores = new ArrayList<>();
				if (teeDB.getAssignedFaculty().contains(",")) {
					List<String> usernameList = teeStudentBatchwiseService
							.getDistinctUsernamesByActiveTeeIdAndFaculty(teeDB.getId(), username);
					studentAssignmentScores = studentAssignmentService
							.getStudentAssignmentMarksByAssignmentIds(teeBean.getAssignmentIds(), usernameList);
				} else {
					studentAssignmentScores = studentAssignmentService
							.getStudentAssignmentMarksByAssignmentIds(teeBean.getAssignmentIds());
				}

				List<String> usernames = new ArrayList<>();

				studentAssignmentScores.forEach(i -> usernames.add(i.getUsername()));
				logger.info("student test scores:" + studentAssignmentScores.size());
				for (String u : usernames) {

					List<Double> scores = new ArrayList<>();

					for (StudentAssignment st : studentAssignmentScores) {

						if (u.equals(st.getUsername())) {

							scores.add(Double.valueOf(st.getScore()));
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
							double assignmentScore = Double.valueOf(assignments.get(0).getMaxScore());
							if (assignmentScore != totalMarks) {
								double divRes = avgOfTotalScores / assignmentScore;
								double finalScore = divRes * totalMarks;
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

				List<StudentAssignment> studentAssignmentScores = new ArrayList<>();
				if (teeDB.getAssignedFaculty().contains(",")) {
					List<String> usernameList = teeStudentBatchwiseService
							.getDistinctUsernamesByActiveTeeIdAndFaculty(teeDB.getId(), username);
					studentAssignmentScores = studentAssignmentService
							.getStudentAssignmentMarksByAssignmentIds(teeBean.getAssignmentIds(), usernameList);
				} else {
					studentAssignmentScores = studentAssignmentService
							.getStudentAssignmentMarksByAssignmentIds(teeBean.getAssignmentIds());
				}

				List<String> usernames = new ArrayList<>();
				Map<String, List<Double>> mapper = new HashMap<>();
				studentAssignmentScores.forEach(i -> usernames.add(i.getUsername()));

				for (String u : usernames) {

					List<Double> scores = new ArrayList<>();

					for (StudentAssignment st : studentAssignmentScores) {

						if (u.equals(st.getUsername())) {

							scores.add(Double.valueOf(st.getScore()));
						}
					}
					mapper.put(u, scores);
				}

				if (assignments.size() == 1) {
					double maxScore = Double.valueOf(assignments.get(0).getMaxScore());

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

						if (maxScore != totalMarks) {
							double divRes = avgOfScore / maxScore;
							double finalMarks = divRes * totalMarks;

							returnMap.put(s, round(finalMarks, 2));
						} else {
							returnMap.put(s, round(avgOfScore, 2));
						}

					}

				} else {

					double totalOutOf = 0.0;

					for (Assignment t : assignments) {
						totalOutOf = totalOutOf + Double.valueOf(t.getMaxScore());
					}

					for (String s : mapper.keySet()) {
						double sumOfScore = 0.0;
						List<Double> studList = mapper.get(s);

						for (Double d : studList) {
							sumOfScore = sumOfScore + d;
						}

						double divRes = sumOfScore / totalOutOf;
						double finalMarks = divRes * totalMarks;
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

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = { "/clearTeeAsignmentMarks" }, method = { RequestMethod.GET })
	public String clearTeeAsignmentMarks(Model m, @RequestParam String teeId, @RequestParam String courseId,
			HttpServletResponse resp, Principal p, RedirectAttributes redirectAttrs, HttpServletRequest request) {

		String username = p.getName();
		redirectAttrs.addAttribute("teeId", teeId);
		redirectAttrs.addAttribute("courseId", courseId);
		try {
			TeeBean tee = new TeeBean();
			tee.setId(Long.valueOf(teeId));
			tee.setCourseId(courseId);

			TeeBean teeBeanDB = teeBeanService.findByID(Long.valueOf(teeId));
			TeeBean teeBeanAssgn = teeBeanService.getTeeAssignments(Long.valueOf(teeId), Long.valueOf(courseId));

			if (teeBeanAssgn.getAssignmentIdsForTee().contains(",")) {
				teeBeanAssgn.setAssignmentIds(Arrays.asList(teeBeanAssgn.getAssignmentIdsForTee().split(",")));
			} else {
				teeBeanAssgn.setAssignmentIds(Arrays.asList(teeBeanAssgn.getAssignmentIdsForTee()));
			}
			logger.info("assignmenttids are:" + teeBeanAssgn.getAssignmentIds());
			List<StudentAssignment> studentAssignmentScores = new ArrayList<>();
			if (teeBeanDB.getAssignedFaculty().contains(",")) {
				List<String> usernameList = teeStudentBatchwiseService
						.getDistinctUsernamesByActiveTeeIdAndFaculty(teeBeanDB.getId(), username);
				studentAssignmentScores = studentAssignmentService
						.getStudentAssignmentMarksByAssignmentIds(teeBeanAssgn.getAssignmentIds(), usernameList);
			} else {
				studentAssignmentScores = studentAssignmentService
						.getStudentAssignmentMarksByAssignmentIds(teeBeanAssgn.getAssignmentIds());
			}
			List<String> usernames = new ArrayList<>();
			studentAssignmentScores.forEach(i -> usernames.add(i.getUsername()));

			logger.info("usernames are:" + usernames);
			teeTotalMarksService.deleteTeeTotalMarksByStudents(teeId, usernames);

			int effRows = teeBeanService.clearTeeAssignments(tee);
			if (effRows > 0) {
				setSuccess(redirectAttrs, "Assignments Selections Cleared Successfully");
			} else {
				setNote(redirectAttrs, "Assignment Data Not Found!!");
			}

			return "redirect:/assignAssignmentMarksToTeeForm";
		} catch (Exception ex) {
			logger.error("Exception", ex);
			setError(redirectAttrs, "Error Occured While Clearing Assignment Selection");
			return "redirect:/assignAssignmentMarksToTeeForm";
		}
	}

}
