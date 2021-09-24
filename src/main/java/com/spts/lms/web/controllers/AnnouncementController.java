package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.amazon.AmazonS3ClientService;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.announcement.Announcement.AnnouncementType;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.studentParent.StudentParent;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.daos.announcement.AnnouncementDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.notification.Notifier;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.studentParent.StudentParentService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;


@Controller
public class AnnouncementController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	CourseService courseService;

	@Autowired
	AnnouncementService announcementService;

	@Autowired
	StudentParentService studentParentService;

	@Autowired
	ProgramService programService;

	@Autowired
	Notifier notifier;

	@Autowired
	AnnouncementDAO annoucementDao;

	@Autowired
	UserRoleService userRoleService;
	
	@Autowired
	AmazonS3ClientService amazonS3ClientService;

	@Value("${lms.announcement.courseFolderS3}")
	private String courseAnnouncementFolder;

	@Value("${sendAlertsToParents}")
	private String sendAlertsToParents;

	@Value("${lms.announcementS3}")
	private String announcementFolder;

	@Value("${workStoreDir:''}")
	private String workDir;

	@Value("${lms.enrollment.years}")
	private String[] enrollmentYears;

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	Client client = ClientBuilder.newClient();
	
	@ModelAttribute("yearList")
	public String[] getYearList() {
		return enrollmentYears;
	}

	private static final Logger logger = Logger
			.getLogger(AnnouncementController.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/addAnnouncementFormProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAnnouncementFormProgram(
			@RequestParam(required = false) Long id,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Add a new Announcement", true, false));
		if (request.getSession().getAttribute("courseRecord") == null
				|| request.getSession().getAttribute("courseRecord").equals("")) {

		} else {
			request.getSession().removeAttribute("courseRecord");
		}

		if (announcement.getId() != null && !announcement.getId().equals("")) {
			announcement = announcementService.findByID(announcement.getId());
			m.addAttribute("edit", "true");
		}

		/*
		 * Temporarily commented by Sanket: Not sure if they will need Program
		 * related announcements. else if(announcement.getProgramId() != null){
		 * announcement.setAnnouncementType(AnnouncementType.PROGRAM); }
		 */

		announcement.setAnnouncementType(AnnouncementType.PROGRAM);
		if (userdetails1.getProgramId() != null)
			// announcement.setAnnouncementType(AnnouncementType.PROGRAM);
			announcement.setSemesters(userCourseService
					.findAllAcadSessionsWithProgramId(userdetails1
							.getProgramId()));

		m.addAttribute("announcement", announcement);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		m.addAttribute("allCampuses", userService.findCampus());
		return "announcement/addAnnouncementProgram";
	}

	/*
	 * @RequestMapping(value = "/searchAnnouncementForLibrarian", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * searchAnnouncementForLibrarian(
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
	 * 
	 * @ModelAttribute Announcement announcement, Principal principal) { String
	 * username = principal.getName(); m.addAttribute("webPage", new
	 * WebPage("announcementList", "View Announcements", true, false)); Token
	 * userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * 
	 * try { Page<Announcement> page = announcementService
	 * .findAnnouncementsForLibrary(announcement, pageNo, pageSize);
	 * List<Announcement> announcementList = page.getPageItems();
	 * 
	 * 
	 * for (Announcement a : announcementList) { Long courseId =
	 * a.getCourseId(); if (courseId != null) { Course course =
	 * courseService.findByID(courseId); String courseName =
	 * course.getCourseName(); a.setCourseName(courseName);
	 * announcementList.set(announcementList.indexOf(a), a);
	 * m.addAttribute("courseName", courseName); } }
	 * 
	 * 
	 * 
	 * for (Announcement a : announcementList) { Long cId = a.getCourseId(); if
	 * (cId != null) { Course course = courseService.findByID(cId); String
	 * courseName = course.getCourseName(); a.setCourseName(courseName);
	 * 
	 * Program program = programService.findByID(Long.valueOf(a
	 * .getProgramId())); a.setProgramName(program.getProgramName()); //
	 * announcementList.set(announcementList.indexOf(a), a);
	 * m.addAttribute("courseName", courseName); } else if (a.getProgramId() !=
	 * null) { logger.info("------------------------------------------");
	 * Program program = programService.findByID(Long.valueOf(a
	 * .getProgramId())); a.setProgramName(program.getProgramName());
	 * logger.info("a.getProgramName() " + a.getProgramName());
	 * 
	 * } announcementList.set(announcementList.indexOf(a), a); }
	 * 
	 * m.addAttribute("page", page); m.addAttribute("q",
	 * getQueryString(announcement));
	 * 
	 * if (announcementList == null || announcementList.size() == 0) {
	 * setNote(m, "No Announcement found"); }
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error("Exception", e); setError(m,
	 * "Error in getting Announcement List"); } return
	 * "announcement/announcementListForLibrary"; }
	 */

	@Secured("ROLE_LIBRARIAN")
	@RequestMapping(value = "/addAnnouncementFormLibrary", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAnnouncementFormLibrary(
			@RequestParam(required = false) Long id,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Add a new Announcement", true, false));

		announcement.setAnnouncementType(AnnouncementType.LIBRARY);
		if (announcement.getId() != null && !announcement.getId().equals("")) {
			announcement = announcementService.findByID(announcement.getId());
			m.addAttribute("edit", "true");
		}
		/*
		 * Temporarily commented by Sanket: Not sure if they will need Program
		 * related announcements. else if(announcement.getProgramId() != null){
		 * announcement.setAnnouncementType(AnnouncementType.PROGRAM); }
		 */
		m.addAttribute("announcement", announcement);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		m.addAttribute("allCampuses", userService.findCampus());
		return "announcement/addAnnouncementLibrary";
	}

	@Secured("ROLE_COUNSELOR")
	@RequestMapping(value = "/addAnnouncementFormCounselor", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAnnouncementFormCounselor(
			@RequestParam(required = false) Long id,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Add a new Announcement", true, false));

		announcement.setAnnouncementType(AnnouncementType.COUNSELOR);
		if (announcement.getId() != null && !announcement.getId().equals("")) {
			announcement = announcementService.findByID(announcement.getId());
			m.addAttribute("edit", "true");
		}
		/*
		 * Temporarily commented by Sanket: Not sure if they will need Program
		 * related announcements. else if(announcement.getProgramId() != null){
		 * announcement.setAnnouncementType(AnnouncementType.PROGRAM); }
		 */
		m.addAttribute("announcement", announcement);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		m.addAttribute("allCampuses", userService.findCampus());
		return "announcement/addAnnouncementCounselor";
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM"})
	@RequestMapping(value = "/addAnnouncementForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAnnouncementForm(@RequestParam(required = false) Long id,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCampuses", userService.findCampus());

		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Add a new Announcement", true, false));
		if (request.getSession().getAttribute("courseRecord") == null
				|| request.getSession().getAttribute("courseRecord").equals("")) {

		} else {
			request.getSession().removeAttribute("courseRecord");
		}

		if (announcement.getId() != null && !announcement.getId().equals("")) {
			announcement = announcementService.findByID(announcement.getId());
			m.addAttribute("edit", "true");
		}
		if (announcement.getCourseId() != null) {
			announcement.setAnnouncementType(AnnouncementType.COURSE);
		}
		/*
		 * Temporarily commented by Sanket: Not sure if they will need Program
		 * related announcements. else if(announcement.getProgramId() != null){
		 * announcement.setAnnouncementType(AnnouncementType.PROGRAM); }
		 */
		else {
			announcement.setAnnouncementType(AnnouncementType.INSTITUTE);
			if (userdetails1.getProgramId() != null)
				announcement.setSemesters(userCourseService
						.findAllAcadSessionsWithProgramId(userdetails1
								.getProgramId()));
		}
		m.addAttribute("announcement", announcement);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "announcement/addAnnouncementAdmin";
		} else {
			return "announcement/addAnnouncement";
		}
	}

	/*
	 * @RequestMapping(value = "/viewUserAnnouncementsSearch", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * viewUserAnnouncementsSearch(
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo,
	 * 
	 * Model m, @ModelAttribute Announcement announcements, Principal principal)
	 * { try { String username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * String acadSession = u.getAcadSession();
	 * logger.info("Program NAme--------------------> " + ProgramName);
	 * m.addAttribute("Program_Name", ProgramName);
	 * m.addAttribute("AcadSession", acadSession); Page<Announcement> page =
	 * null; UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) principal; if
	 * (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) { if
	 * (announcements.getAnnouncementType() != null) {
	 * logger.info("announcement type is not null---"); if
	 * (announcements.getAnnouncementSubType() != null &&
	 * !announcements.getAnnouncementSubType() .isEmpty()) {
	 * logger.info("announcement subtype is not null---"); if ("COURSE"
	 * .equals(announcements.getAnnouncementType())) { List<Course>
	 * courseByUsername = courseService .findByUserActive(username,
	 * userdetails1.getProgramName());
	 * 
	 * List<Long> courseIds = new ArrayList(); for (Course cId :
	 * courseByUsername) {
	 * 
	 * courseIds.add(cId.getId()); } page = announcementService
	 * .getAnnouncementsByStudentCoursesAndSubType( courseIds, pageNo, pageSize,
	 * announcements .getAnnouncementSubType()); } if
	 * ("INSTITUTE".equals(announcements .getAnnouncementType())) { page =
	 * announcementService .getInstitutionalAnnouncementBySubtype( pageNo,
	 * pageSize, announcements .getAnnouncementSubType()); } } else {
	 * logger.info("announcement subtype is null---"); if ("COURSE"
	 * .equals(announcements.getAnnouncementType())) {
	 * logger.info("announcement type is course---"); List<Course>
	 * courseByUsername = courseService .findByUserActive(username,
	 * userdetails1.getProgramName());
	 * 
	 * List<Long> courseIds = new ArrayList(); for (Course cId :
	 * courseByUsername) {
	 * 
	 * courseIds.add(cId.getId()); } page = announcementService
	 * .getAnnouncementsByStudentCourses( courseIds, pageNo, pageSize); } if
	 * ("INSTITUTE".equals(announcements .getAnnouncementType())) {
	 * logger.info("announcement type is institute---"); page =
	 * announcementService .getInstitutionalAnnouncement(pageNo, pageSize); } if
	 * ("LIBRARY".equals(announcements .getAnnouncementType())) {
	 * logger.info("announcement type is LIBRARY---"); page =
	 * announcementService.getLibraryAnnouncement( pageNo, pageSize); }
	 * 
	 * if ("COUNSELOR".equals(announcements .getAnnouncementType())) {
	 * logger.info("announcement type is LIBRARY---"); page =
	 * announcementService .getCounselorAnnouncement(pageNo, pageSize); } } }
	 * else { List<Course> courseByUsername = courseService
	 * .findByUserActive(username, userdetails1.getProgramName());
	 * 
	 * List<Long> courseIds = new ArrayList(); for (Course cId :
	 * courseByUsername) {
	 * 
	 * courseIds.add(cId.getId()); } page =
	 * announcementService.getAnnouncementsByCoursesP( courseIds, pageNo,
	 * pageSize); } List<Announcement> announcementList = page.getPageItems();
	 * for (Announcement a : announcementList) { Long cId = a.getCourseId(); if
	 * (cId != null) { Course course = courseService.findByID(cId); String
	 * courseName = course.getCourseName(); a.setCourseName(courseName);
	 * 
	 * Program program = programService.findByID(Long
	 * .valueOf(a.getProgramId())); a.setProgramName(program.getProgramName());
	 * announcementList.set(announcementList.indexOf(a), a);
	 * m.addAttribute("courseName", courseName); } } m.addAttribute("page",
	 * page); m.addAttribute("q", getQueryString(announcements));
	 * m.addAttribute("pageRowCount", announcementList.size());
	 * 
	 * if (announcementList == null || announcementList.size() == 0) {
	 * setNote(m, "No Announcement found"); }
	 * 
	 * } } catch (Exception ex) { logger.error("Exception", ex); }
	 * logger.info("announcement bean--->" + announcements); return
	 * "announcement/userAnnouncementList"; }
	 */

	@Secured("ROLE_STUDENT")
	@RequestMapping(value = "/viewUserAnnouncementsSearch", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewUserAnnouncementsSearch(

	@RequestParam(required = false, defaultValue = "1") int pageNo,

	Model m, @ModelAttribute Announcement announcements, Principal principal) {
		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			Page<Announcement> page = null;
			UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
			if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				if (announcements.getAnnouncementType() != null) {

					if (announcements.getAnnouncementSubType() != null
							&& !announcements.getAnnouncementSubType()
									.isEmpty()) {

						if ("COURSE"
								.equals(announcements.getAnnouncementType())) {
							List<Course> courseByUsername = courseService
									.findByUserActive(username,
											userdetails1.getProgramName());

							List<Long> courseIds = new ArrayList();
							for (Course cId : courseByUsername) {

								courseIds.add(cId.getId());
							}
							page = announcementService
									.getAnnouncementsByStudentCoursesAndSubType(
											courseIds, pageNo, pageSize,
											announcements
													.getAnnouncementSubType());
						}
						if ("INSTITUTE".equals(announcements
								.getAnnouncementType())) {
							page = announcementService
									.getInstitutionalAnnouncementBySubtype(
											pageNo, pageSize, announcements
													.getAnnouncementSubType());
						}
					} else {

						if ("COURSE"
								.equals(announcements.getAnnouncementType())) {

							List<Course> courseByUsername = courseService
									.findByUserActive(username,
											userdetails1.getProgramName());

							List<Long> courseIds = new ArrayList();
							for (Course cId : courseByUsername) {

								courseIds.add(cId.getId());
							}
							page = announcementService
									.getAnnouncementsByStudentCourses(
											courseIds, pageNo, pageSize);
						}
						if ("INSTITUTE".equals(announcements
								.getAnnouncementType())) {

							page = announcementService
									.getInstitutionalAnnouncement(pageNo,
											pageSize);
						}
						if ("LIBRARY".equals(announcements
								.getAnnouncementType())) {

							page = announcementService.getLibraryAnnouncement(
									pageNo, pageSize);
						}

						if ("COUNSELOR".equals(announcements
								.getAnnouncementType())) {

							page = announcementService
									.getCounselorAnnouncement(pageNo, pageSize);
						}
					}
				} else {
					List<Course> courseByUsername = courseService
							.findByUserActive(username,
									userdetails1.getProgramName());

					List<Long> courseIds = new ArrayList();
					for (Course cId : courseByUsername) {

						courseIds.add(cId.getId());
					}
					page = announcementService.getAnnouncementsByCoursesP(
							courseIds, pageNo, pageSize);
				}
				List<Announcement> announcementList = page.getPageItems();
				for (Announcement a : announcementList) {

					Long cId = a.getCourseId();
					if (cId != null) {
						Course course = courseService.findByID(cId);
						String courseName = course.getCourseName();
						a.setCourseName(courseName);

						Program program = programService.findByID(Long
								.valueOf(a.getProgramId()));
						a.setProgramName(program.getProgramName());
						// announcementList.set(announcementList.indexOf(a), a);
						m.addAttribute("courseName", courseName);
					}

					if (u.getCampusId() != null) {
						if (a.getCampusId() != null) {

							if (!u.getCampusId().equals(a.getCampusId())) {

								Integer index = announcementList.indexOf(a);

								announcementList.remove(index);
								announcementList.set(index, null);
							} else {
								announcementList.set(
										announcementList.indexOf(a), a);
							}
						} else {

							announcementList
									.set(announcementList.indexOf(a), a);
						}
					} else {
						announcementList.set(announcementList.indexOf(a), a);
					}

				}
				announcementList.removeAll(Collections.singleton(null));
				m.addAttribute("page", page);
				m.addAttribute("q", getQueryString(announcements));
				m.addAttribute("pageRowCount", announcementList.size());

				if (announcementList == null || announcementList.size() == 0) {
					setNote(m, "No Announcement found");
				}

			}
		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

		return "announcement/userAnnouncementList";
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/addAnnouncement", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addAnnouncement(@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam("file") List<MultipartFile> files,
			@RequestParam(required = false) String typeOfAnn) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		String subject = " New Announcement: " + announcement.getSubject()
				+ " send ";
		Map<String, Map<String, String>> result = null;
		List<String> userList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<>();
		userList.add(username);
		String defaultMsg = "\\r\\n\\r\\nNote: This Announcement is created by : ?? \\r\\nTo view any attached files to this mail kindly login to \\r\\nUrl: https://portal.svkm.ac.in/usermgmt/login ";

		List<String> parentList = new ArrayList<String>();
		try {
			/* New Audit changes start */
			if(!Utils.validateStartAndEndDates(announcement.getStartDate(), announcement.getEndDate())) {
				setError(redirectAttrs, "Invalid Start date and End date");
				if (typeOfAnn != null) {
					if ("PROGRAM".equals(typeOfAnn)) {
						return "redirect:/addAnnouncementFormProgram";
					}
				}
				return "redirect:/addAnnouncementForm";
			} 
			/* New Audit changes end */
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttrs, "File uploaded is invalid!");
							if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {
								return "redirect:/addAnnouncementFormLibrary";
							}
								if (typeOfAnn != null) {
									if ("PROGRAM".equals(typeOfAnn)) {
										return "redirect:/addAnnouncementFormProgram";
									}
								}

								return "redirect:/addAnnouncementForm";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe")) {
								setError(redirectAttrs, "File uploaded is invalid!");
								if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {
									return "redirect:/addAnnouncementFormLibrary";
								}
								if (typeOfAnn != null) {
									if ("PROGRAM".equals(typeOfAnn)) {
										return "redirect:/addAnnouncementFormProgram";
									}
								}

								return "redirect:/addAnnouncementForm";
							}else {
								uploadAnnouncementFileForS3(announcement, file);
							}
						}
					}else {
						setError(redirectAttrs, "File uploaded is invalid!");
						if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {
							return "redirect:/addAnnouncementFormLibrary";
						}
								if (typeOfAnn != null) {
									if ("PROGRAM".equals(typeOfAnn)) {
										return "redirect:/addAnnouncementFormProgram";
									}
								}

								return "redirect:/addAnnouncementForm";
					}
				}
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				announcement.setSendEmailAlertToParents("Y");
				announcement.setSendSmsAlertToParents("Y");
			}
			if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {

				announcement.setCreatedBy(username);
				announcement.setLastModifiedBy(username);
				announcementService.insertWithIdReturn(announcement);

				setSuccess(redirectAttrs, "Announcement created successfully");

			} else {
				if (announcement.getCourseId() != null) {
					Course c = courseService.findByID(announcement
							.getCourseId());

					List<User> users = userService.findAllByCourse(announcement
							.getCourseId());

					for (User u : users) {
						userList.add(u.getUsername());
					}
					subject = subject + " for course " + c.getCourseName();
					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);
					announcement.setProgramId(c.getProgramId());

					announcementService.insertWithIdReturn(announcement);

					setSuccess(redirectAttrs,
							"Announcement created successfully");
				} else {

					/*
					 * List<User> users = new ArrayList<User>(); String acad =
					 * announcement.getAcadSession(); if (acad == null ||
					 * "NONE".equals(acad)) users = userService.findAllActive();
					 * else { users = userService
					 * .findByProgramIdAndSemester(Long
					 * .valueOf(userdetails1.getProgramId()), acad); }
					 */
					List<User> users = new ArrayList<User>();
					if (announcement.getAnnouncementType().equals("INSTITUTE")) {

						users = userService.findAllActiveUsers(String
								.valueOf(announcement.getCampusId()));
						/*
						 * else { users =
						 * userService.findByProgramIdAndSemester(
						 * Long.valueOf(userdetails1.getProgramId()),
						 * announcement.getAcadSession()); }
						 */
					}

					if (announcement.getAnnouncementType().equals("PROGRAM")) {
						announcement.setProgramId(Long.valueOf(userdetails1
								.getProgramId()));

						if (announcement.getAcadSession() == null
								|| "NONE".equals(announcement.getAcadSession())
								|| announcement.getAcadSession().isEmpty())
							users = userService.findByProgramIdAndYear(
									Long.valueOf(userdetails1.getProgramId()),
									String.valueOf(announcement.getAcadYear()),
									String.valueOf(announcement.getCampusId()));
						else {
							if (announcement.getAcadSession().contains(",")) {
								acadSessionList = Arrays.asList(announcement
										.getAcadSession().split(","));
								for (String a : acadSessionList) {
									List<User> userByAcadSession = userService
											.findByProgramIdAndSemesterAndYear(
													Long.valueOf(userdetails1
															.getProgramId()),
													a,
													String.valueOf(announcement
															.getAcadYear()),
													String.valueOf(announcement
															.getCampusId()));
									users.addAll(userByAcadSession);
								}

							} else {
								users = userService
										.findByProgramIdAndSemesterAndYear(Long
												.valueOf(userdetails1
														.getProgramId()),
												announcement.getAcadSession(),
												String.valueOf(announcement
														.getAcadYear()), String
														.valueOf(announcement
																.getCampusId()));
							}
						}
					}

					for (User u : users) {
						userList.add(u.getUsername());
					}

					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);

					if (announcement.getAcadSession() != null) {
						if (announcement.getAcadSession().contains(",")) {
							acadSessionList = Arrays.asList(announcement
									.getAcadSession().split(","));
							for (String a : acadSessionList) {
								announcement.setAcadSession(a);
								announcementService
										.insertWithIdReturn(announcement);
							}
						} else {
							announcementService
									.insertWithIdReturn(announcement);
						}
					} else {
						announcementService.insertWithIdReturn(announcement);
					}
					setSuccess(redirectAttrs,
							"Announcement created successfully");
				}
			}
			if (announcement.getCampusId() != null) {
				Long campusId = announcement.getCampusId();
				announcement
						.setCampusName(userService.findCampusName(campusId));
			}
			try {

				if (!userList.isEmpty()) {

					String notificationEmailMessage = Jsoup.parse(
							announcement.getDescription().concat(defaultMsg))
							.text();

					// announcement
					// .getDescription().concat(defaultMsg);
					String notificationMobileMessage = announcement
							.getSubject().concat(defaultMsg);
					notificationEmailMessage = notificationEmailMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());
					notificationMobileMessage = notificationMobileMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());

					// ----
					/*
					 * List<String> singleUserList = new ArrayList<String>();
					 * singleUserList.add(userList.get(0)); logger.info(
					 * "user List ------------==========================================================3443====================="
					 * +userList.get(0));
					 */
					// ---------
					if ("Y".equals(announcement.getSendEmailAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendEmailAlert())) {

						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");

						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject
								+ " for course ", notificationEmailMessage);
					}
					if ("Y".equals(announcement.getSendSmsAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendSmsAlert())) {

						if (result != null)
							result = userService.findEmailByUserName(userList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject,
								notificationMobileMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in creating Announcement");

			if (typeOfAnn != null) {
				if ("PROGRAM".equals(typeOfAnn)) {
					return "redirect:/addAnnouncementFormProgram";
				}
			}

			return "redirect:/addAnnouncementForm";
		}

		if (acadSessionList.size() > 0) {
			return "redirect:/searchAnnouncement";
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "announcement/announcementAdmin";
			} else if (userdetails1.getAuthorities().contains(
					Role.ROLE_LIBRARIAN)) {
				return "announcement/announcementLibrarian";
			}

			else {
				return "announcement/announcement";
			}
		}
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR","ROLE_STUDENT"})
	@RequestMapping(value = "/sendAnnouncementFile", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ResponseEntity<ByteArrayResource> sendAnnouncementFile(
			@RequestParam(name = "id") String id, HttpServletRequest request,
			HttpServletResponse response) {

		String projectUrl = "";
		try {
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("Content-type: text/zip");
			Announcement sa = announcementService.findByID(Long.valueOf(id));
			File src = new File(sa.getFilePath());

			String filePath = sa.getFilePath();
			List<File> fileList = new ArrayList<>();
			if (filePath.contains(",")) {
				
				File folderPath = new File(workDir + File.separator
						+ "allFiles");
				if (!folderPath.exists()) {
					folderPath.mkdir();
				}
				
				List<String> files = Arrays.asList(filePath.split(","));
				for (String file : files) {
					if(file.startsWith("/")) {
						file = StringUtils.substring(file, 1);
					}
					File fileNew = new File(file);
					logger.info(folderPath.getAbsolutePath());
					InputStream inpStream = amazonS3ClientService.getFileByFullPath(file);
					File dest = new File(folderPath.getAbsolutePath() + File.separator + fileNew.getName());
					FileUtils.copyInputStreamToFile(inpStream, dest);
					
				}
				String filename = "announcementFiles.zip";
				response.setHeader("Content-Disposition",
						"attachment; filename=" + filename + "");
				projectUrl = "/" + "workDir" + "/" + folderPath.getName()
						+ ".zip";
				pack(folderPath.getAbsolutePath(), out);
				
				FileUtils.deleteDirectory(folderPath);
				 return null;

			} else {
					String path = "";
					if(sa.getFilePath().startsWith("/")) {
						path = StringUtils.substring(sa.getFilePath(), 1);
					}else {
						path = sa.getFilePath();
					}
					
					byte[] data = amazonS3ClientService.getFile(path);
					logger.info("data"+data.length);
					ByteArrayResource resource = new ByteArrayResource(data);

					return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
							.header("Content-disposition", "attachment; filename=\"" + src.getName() + "\"")
							.body(resource);


				}
				

			

		} catch (Exception e) {
			logger.error("Error", e);
		}
		return null;
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

	private String uploadAnnouncementFile(Announcement bean, MultipartFile file) {

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

		fileName = fileName.substring(0, fileName.lastIndexOf("."))
				+ "_"
				+ RandomStringUtils.randomAlphanumeric(10)
				+ fileName.substring(fileName.lastIndexOf("."),
						fileName.length());

		try {
			inputStream = file.getInputStream();

			if (bean.getCourseId() != null) {
				filePath = courseAnnouncementFolder + File.separator + fileName;
				folderPath = new File(courseAnnouncementFolder);
			} else {
				filePath = announcementFolder + File.separator + fileName;
				folderPath = new File(announcementFolder);
			}
			if (bean.getFilePath() != null) {

				bean.setFilePath(bean.getFilePath() + "," + filePath);
				bean.setFilePreviewPath(bean.getFilePreviewPath() + ","
						+ filePath);
			} else {

				bean.setFilePath(filePath);
				bean.setFilePreviewPath(filePath);
			}

			// bean.setFilePath(filePath);
			// bean.setFilePreviewPath(filePath);

			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			File newFile = new File(filePath);
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);

		} catch (IOException e) {
			errorMessage = "Error in uploading Announcement file : "
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

	private String uploadAnnouncementFileForS3(Announcement bean, MultipartFile file) {

		String errorMessage = null;
		String filePath = null;


		try {

			if (bean.getCourseId() != null) {
				filePath = courseAnnouncementFolder;
			} else {
				filePath = announcementFolder;
			}
			
			
			Map<String,String> s3FileNameMap = amazonS3ClientService.uploadFileToS3BucketWithRandomString(file, filePath);
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
				throw new Exception("Error in uploading Announcement file");
			}
			


		} catch (Exception e) {
			errorMessage = "Error in uploading Announcement file : "
					+ e.getMessage();
			logger.error("Exception" + errorMessage, e);
		}

		return errorMessage;
	}
	
	@Secured("ROLE_PARENT")
	@RequestMapping(value = "/announcementFormForParents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String gradeCenterForm(Model m, Principal principal) {
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		m.addAttribute("webPage", new WebPage("gradeList", "Grade Center",
				false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(username);

		m.addAttribute("listOfStudents", listOfStudents);
		m.addAttribute("showAnnouncementList", false);

		return "announcement/announcementListForParents";
	}

	@Secured("ROLE_PARENT")
	@RequestMapping("/announcementListForParents")
	public String pendingTaskForParents(Principal principal,
			@RequestParam String uname, Model modal,
			@RequestParam(required = false, defaultValue = "1") int pageNo) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		modal.addAttribute("Program_Name", ProgramName);
		modal.addAttribute("AcadSession", acadSession);
		boolean isFaculty = false;
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		List<StudentParent> listOfStudents = studentParentService
				.findStudentsByParentUname(username);
		Announcement announcement = new Announcement();

		List<Course> courseByUsername = courseService.findByUserActive(uname,
				userdetails1.getProgramName());

		String acadYear, acadMonth;
		List<Long> courseIds = new ArrayList();
		for (Course cId : courseByUsername) {

			courseIds.add(cId.getId());
			acadYear = cId.getAcadYear();
			acadMonth = cId.getAcadMonth();
		}

		Page<Announcement> page = announcementService
				.getAnnouncementsByCoursesP(courseIds, pageNo, pageSize);

		modal.addAttribute("page", page);
		modal.addAttribute("q", uname);

		modal.addAttribute("uname", uname);
		modal.addAttribute("listOfStudents", listOfStudents);
		modal.addAttribute("showAnnouncementList", true);

		modal.addAttribute("webPage", new WebPage("announcementListForParents",
				"Announcement List", true, false));
		return "announcement/announcementListForParents";

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/updateAnnouncement", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String updateAnnouncement(@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam("file") List<MultipartFile> files,
			@RequestParam(required = false) String typeOfAnn) {
		
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		
		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		
		String subject = " Updated Announcement: " + announcement.getSubject() + " send ";
		Map<String, Map<String, String>> result = null;
		List<String> userList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<>();
		userList.add(username);
		String defaultMsg = "\\r\\n\\r\\nNote: This Announcement is updated by : ?? \\r\\nTo view any attached files to this mail kindly login to \\r\\nUrl: https://portal.svkm.ac.in/usermgmt/login ";

		List<String> parentList = new ArrayList<String>();
		try {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					//Audit change start
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttrs, "File uploaded is invalid!");
							if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY") || userdetails1.getAuthorities().contains(
									Role.ROLE_LIBRARIAN)) {
								redirectAttrs.addAttribute("id", announcement.getId());
								return "redirect:/addAnnouncementFormLibrary";
							}
							if (typeOfAnn != null) {
								if ("PROGRAM".equals(typeOfAnn)) {
									return "redirect:/addAnnouncementFormProgram";
								}
							}
							return "redirect:/addAnnouncementForm";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe")) {
								setError(redirectAttrs, "File uploaded is invalid!");
								if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY") || userdetails1.getAuthorities().contains(
										Role.ROLE_LIBRARIAN)) {
									redirectAttrs.addAttribute("id", announcement.getId());
									return "redirect:/addAnnouncementFormLibrary";
								}
								if (typeOfAnn != null) {
									if ("PROGRAM".equals(typeOfAnn)) {
										return "redirect:/addAnnouncementFormProgram";
									}
								}
								return "redirect:/addAnnouncementForm";
							}else {
								uploadAnnouncementFileForS3(announcement, file);
							}
						}
					}else {
						setError(redirectAttrs, "File uploaded is invalid!");
						if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY") || userdetails1.getAuthorities().contains(
								Role.ROLE_LIBRARIAN)) {
							redirectAttrs.addAttribute("id", announcement.getId());
							return "redirect:/addAnnouncementFormLibrary";
						}
						if (typeOfAnn != null) {
							if ("PROGRAM".equals(typeOfAnn)) {
								return "redirect:/addAnnouncementFormProgram";
							}
						}
						return "redirect:/addAnnouncementForm";
					}
					//Audit change end
				}
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				announcement.setSendEmailAlertToParents("Y");
				announcement.setSendSmsAlertToParents("Y");
			}
			if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {

				announcement.setCreatedBy(username);
				announcement.setLastModifiedBy(username);
				announcementService.update(announcement);

				setSuccess(redirectAttrs, "Announcement updated successfully");

			} else {
				if (announcement.getCourseId() != null) {
					Course c = courseService.findByID(announcement
							.getCourseId());

					List<User> users = userService.findAllByCourse(announcement
							.getCourseId());

					for (User u : users) {
						userList.add(u.getUsername());
					}
					subject = subject + " for course " + c.getCourseName();
					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);
					announcement.setProgramId(c.getProgramId());

					announcementService.update(announcement);

					setSuccess(redirectAttrs,
							"Announcement updated successfully");
				} else {

					/*
					 * List<User> users = new ArrayList<User>(); String acad =
					 * announcement.getAcadSession(); if (acad == null ||
					 * "NONE".equals(acad)) users = userService.findAllActive();
					 * else { users = userService
					 * .findByProgramIdAndSemester(Long
					 * .valueOf(userdetails1.getProgramId()), acad); }
					 */
					List<User> users = new ArrayList<User>();
					if (announcement.getAnnouncementType().equals("INSTITUTE")) {

						users = userService.findAllActiveUsers(String
								.valueOf(announcement.getCampusId()));
						/*
						 * else { users =
						 * userService.findByProgramIdAndSemester(
						 * Long.valueOf(userdetails1.getProgramId()),
						 * announcement.getAcadSession()); }
						 */
					}

					if (announcement.getAnnouncementType().equals("PROGRAM")) {

						if (announcement.getAcadSession() == null
								|| "NONE".equals(announcement.getAcadSession())
								|| announcement.getAcadSession().isEmpty())
							users = userService.findByProgramIdAndYear(
									announcement.getProgramId(),
									String.valueOf(announcement.getAcadYear()),
									String.valueOf(announcement.getCampusId()));
						else {
								users = userService
										.findByProgramIdAndSemesterAndYear(announcement.getProgramId(),
												announcement.getAcadSession(),
												String.valueOf(announcement
														.getAcadYear()), String
														.valueOf(announcement
																.getCampusId()));
						}
					}

					for (User u : users) {
						userList.add(u.getUsername());
					}

					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);

					announcementService.update(announcement);
					setSuccess(redirectAttrs,
							"Announcement updated successfully");
				}
			}
			if (announcement.getCampusId() != null) {
				Long campusId = announcement.getCampusId();
				announcement
						.setCampusName(userService.findCampusName(campusId));
			}
			try {

				if (!userList.isEmpty()) {

					String notificationEmailMessage = Jsoup.parse(
							announcement.getDescription().concat(defaultMsg))
							.text();

					// announcement
					// .getDescription().concat(defaultMsg);
					String notificationMobileMessage = announcement
							.getSubject().concat(defaultMsg);
					notificationEmailMessage = notificationEmailMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());
					notificationMobileMessage = notificationMobileMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());

					// ----
					/*
					 * List<String> singleUserList = new ArrayList<String>();
					 * singleUserList.add(userList.get(0)); logger.info(
					 * "user List ------------==========================================================3443====================="
					 * +userList.get(0));
					 */
					// ---------
					if ("Y".equals(announcement.getSendEmailAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendEmailAlert())) {

						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");

						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject
								+ " for course ", notificationEmailMessage);
					}
					if ("Y".equals(announcement.getSendSmsAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendSmsAlert())) {

						if (result != null)
							result = userService.findEmailByUserName(userList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject,
								notificationMobileMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Exception while sending email/ editing Announcement", e);
			}
			m.addAttribute("announcement", announcement);
			redirectAttrs.addFlashAttribute("announcement", announcement);
		} catch (Exception e) {

			logger.error("Exception", e);
			setError(redirectAttrs, "Error in updating Announcement");
			if (typeOfAnn != null) {
				if ("PROGRAM".equals(typeOfAnn)) {
					return "redirect:/addAnnouncementFormProgram";
				}
			}
			return "redirect:/addAnnouncementForm";
		}
		
		if (acadSessionList.size() > 0) {
			return "redirect:/searchAnnouncement";
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "announcement/announcementAdmin";
			} else if (userdetails1.getAuthorities().contains(
					Role.ROLE_LIBRARIAN)) {
				return "announcement/announcementLibrarian";
			}

			else {
				return "announcement/announcement";
			}
		}
		
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR","ROLE_STUDENT"})
	@RequestMapping(value = "/viewAnnouncement", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewAnnouncement(@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		try {
			Announcement announcementDb = announcementService
					.findByID(announcement.getId());
			if (announcementDb.getCampusId() != null) {
				Long campusId = announcementDb.getCampusId();
				announcementDb.setCampusName(userService
						.findCampusName(campusId));
			}
			redirectAttrs.addFlashAttribute("announcement", announcementDb);
			m.addAttribute("announcement", announcementDb);

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(redirectAttrs, "Error in fetching Announcement");
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				return "announcement/announcementAdmin";
			} else {
				return "announcement/announcement";
			}
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "announcement/announcementAdmin";
		} else {
			return "announcement/announcement";
		}
	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/getAnnouncementDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAnnouncementDetails(
			@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m) {
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		try {
			Announcement announcementDb = announcementService
					.findByID(announcement.getId());
			redirectAttrs.addFlashAttribute("announcement", announcementDb);
			m.addAttribute("announcement", announcementDb);
			return announcementDb.getDescription();

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(redirectAttrs, "Error in fetching Announcement");
			return "Error in fetching Announcement Details";
		}

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/searchAnnouncement", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAnnouncement(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, @ModelAttribute Announcement announcement,
			Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("announcementList",
				"View Announcements", true, false));
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			/*
			 * announcement.setProgramId(Long.parseLong(userdetails1
			 * .getProgramId()));
			 */
			Page<Announcement> page;

			if (announcement.getAnnouncementType() == null) {
				if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
					page = announcementService
							.getAnnouncementsByCoursesAndAcadSessionAndInstitute(
									userCourseService
											.findUserCourseList(username),
									username, userdetails1.getProgramId(),
									pageNo, pageSize);
				} else {

					page = announcementService.findAnnouncements(announcement,
							pageNo, pageSize);
				}
			} else {
				if (announcement.getAnnouncementType().equalsIgnoreCase(
						"LIBRARY")
						|| announcement.getAnnouncementType().equalsIgnoreCase(
								"COUNSELOR")) {
					page = announcementService
							.findLibraryCounselorAnnouncements(announcement,
									pageNo, pageSize);
				} else {
					page = announcementService.findAnnouncements(announcement,
							pageNo, pageSize);
				}
			}
			List<Announcement> announcementList = page.getPageItems();

			/*
			 * for (Announcement a : announcementList) { Long courseId =
			 * a.getCourseId(); if (courseId != null) { Course course =
			 * courseService.findByID(courseId); String courseName =
			 * course.getCourseName(); a.setCourseName(courseName);
			 * announcementList.set(announcementList.indexOf(a), a);
			 * m.addAttribute("courseName", courseName); } }
			 */

			for (Announcement a : announcementList) {

				Long cId = a.getCourseId();
				if (cId != null) {
					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();
					a.setCourseName(courseName);

//					Program program = programService.findByID(Long.valueOf(a
//							.getProgramId()));
//					a.setProgramName(program.getProgramName());
					
					Program program = new Program();
					if(a.getProgramId() != null) {
							program	= programService.findByID(Long.valueOf(a.getProgramId()));
							a.setProgramName(program.getProgramName());
					}
					else
					{
						program	= programService.findByID(course.getProgramId());
						a.setProgramName(program.getProgramName());
					}
					
					
					// announcementList.set(announcementList.indexOf(a), a);
					m.addAttribute("courseName", courseName);
				} else if (a.getProgramId() != null) {

					Program program = programService.findByID(Long.valueOf(a
							.getProgramId()));
					a.setProgramName(program.getProgramName());

				}
				/*
				 * if (u.getCampusId() != null) { if (a.getCampusId() != null) {
				 * logger.info("Announcement campus id is not null");
				 * logger.info("users campus id--------------- " +
				 * u.getCampusId());
				 * logger.info("announcement campus id--------------- " +
				 * a.getCampusId());
				 * 
				 * if (!u.getCampusId().equals(a.getCampusId())) {
				 * logger.info("Campus ids are not same"); //
				 * announcementList.remove(announcementList.indexOf(a));
				 * announcementList.set(announcementList.indexOf(a), null); }
				 * else { announcementList .set(announcementList.indexOf(a), a);
				 * } } else { announcementList.set(announcementList.indexOf(a),
				 * a); } } else {
				 */
				announcementList.set(announcementList.indexOf(a), a);
				// }
			}
			announcementList.removeAll(Collections.singleton(null));
			m.addAttribute("page", page);
			m.addAttribute("announcementList", announcementList);
			m.addAttribute("q", getQueryString(announcement));
			m.addAttribute("username", username);

			logger.info("data count--------------->"
					+ page.getPageItems().size());
			logger.info("data data--------------->" + page.getPageItems());
			logger.info("announcement count--------------->"
					+ announcementList.size());

			if (announcementList == null || announcementList.size() == 0) {
				setNote(m, "No Announcement found");
			}

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Announcement List");
		}

		return "announcement/announcementList";
	}

	@Secured("ROLE_LIBRARIAN")
	@RequestMapping(value = "/searchAnnouncementForLibrarian", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAnnouncementForLibrarian(
			@RequestParam(required = false) String announcementType,
			@RequestParam(required = false) String announcementSubType,
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, @ModelAttribute Announcement announcement,
			Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("announcementList",
				"View Announcements", true, false));
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		try {
			if (announcementType != null) {
				announcement.setAnnouncementType(announcementType);
			}
			if (announcementSubType != null) {
				announcement.setAnnouncementSubType(announcementSubType);
			}

			Page<Announcement> page = announcementService
					.findAnnouncementsForLibrary(announcement, pageNo, pageSize);
			List<Announcement> announcementList = page.getPageItems();

			/*
			 * for (Announcement a : announcementList) { Long courseId =
			 * a.getCourseId(); if (courseId != null) { Course course =
			 * courseService.findByID(courseId); String courseName =
			 * course.getCourseName(); a.setCourseName(courseName);
			 * announcementList.set(announcementList.indexOf(a), a);
			 * m.addAttribute("courseName", courseName); } }
			 */

			/*
			 * for (Announcement a : announcementList) { Long cId =
			 * a.getCourseId(); if (cId != null) { Course course =
			 * courseService.findByID(cId); String courseName =
			 * course.getCourseName(); a.setCourseName(courseName);
			 * 
			 * Program program = programService.findByID(Long.valueOf(a
			 * .getProgramId())); a.setProgramName(program.getProgramName()); //
			 * announcementList.set(announcementList.indexOf(a), a);
			 * m.addAttribute("courseName", courseName); } else if
			 * (a.getProgramId() != null) {
			 * logger.info("------------------------------------------");
			 * Program program = programService.findByID(Long.valueOf(a
			 * .getProgramId())); a.setProgramName(program.getProgramName());
			 * logger.info("a.getProgramName() " + a.getProgramName());
			 * 
			 * } announcementList.set(announcementList.indexOf(a), a); }
			 */
			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(announcement));

			if (announcementList == null || announcementList.size() == 0) {
				setNote(m, "No Announcement found");
			}

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Announcement List");
		}
		return "announcement/announcementListForLibrary";
	}

	@Secured("ROLE_COUNSELOR")
	@RequestMapping(value = "/searchAnnouncementForCounselor", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String searchAnnouncementForCounselor(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, @ModelAttribute Announcement announcement,
			Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("announcementList",
				"View Announcements", true, false));
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		try {
			Page<Announcement> page = announcementService
					.findAnnouncementsForCounselor(announcement, pageNo,
							pageSize);
			List<Announcement> announcementList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(announcement));

			if (announcementList == null || announcementList.size() == 0) {
				setNote(m, "No Announcement found");
			}

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Announcement List");
		}
		return "announcement/announcementListForCounselor";
	}

	/*
	 * @RequestMapping(value = "/viewUserAnnouncements", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * viewUserAnnouncements(
	 * 
	 * @RequestParam(required = false) Long courseId,
	 * 
	 * @RequestParam(required = false, defaultValue = "1") int pageNo, Model m,
	 * 
	 * @ModelAttribute Announcement announcements, Principal principal) { String
	 * username = principal.getName();
	 * 
	 * Token userdetails1 = (Token) principal; String ProgramName =
	 * userdetails1.getProgramName(); User u =
	 * userService.findByUserName(username);
	 * logger.info("ACAD SESSION------------------------->" +
	 * u.getAcadSession());
	 * 
	 * String acadSession = u.getAcadSession();
	 * logger.info("Program NAme--------------------> " + ProgramName);
	 * m.addAttribute("Program_Name", ProgramName);
	 * m.addAttribute("AcadSession", acadSession);
	 * UsernamePasswordAuthenticationToken userDetails =
	 * (UsernamePasswordAuthenticationToken) principal;
	 * 
	 * m.addAttribute("webPage", new WebPage("announcementList",
	 * "View Announcements", false, false));
	 * 
	 * try {
	 * 
	 * Page<Announcement> page = null;
	 * 
	 * if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_DEAN) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_FACULTY) ||
	 * userDetails.getAuthorities().contains(Role.ROLE_HOD)) {
	 * logger.info("ädmin entered");
	 * 
	 * if (announcements.getCourseId() != null) { m.addAttribute("courseId",
	 * announcements.getCourseId()); page =
	 * announcementService.findAnnouncementsReplacement(
	 * Long.parseLong(userdetails1.getProgramId()), Long.valueOf(courseId),
	 * pageNo, pageSize); }
	 * 
	 * logger.info("courseId param is--->" + courseId); if (courseId != null) {
	 * logger.info("courseId is not null"); m.addAttribute("courseId",
	 * courseId); page = announcementService.findAnnouncementsReplacement(
	 * Long.parseLong(userdetails1.getProgramId()), Long.valueOf(courseId),
	 * pageNo, pageSize); } else {
	 * 
	 * if ("LIBRARY".equals(announcements.getAnnouncementType())) {
	 * logger.info("announcement type is LIBRARY---"); page =
	 * announcementService.getLibraryAnnouncement( pageNo, pageSize); }
	 * 
	 * else if ("COUNSELOR".equals(announcements .getAnnouncementType())) {
	 * logger.info("announcement type is LIBRARY---"); page =
	 * announcementService.getCounselorAnnouncement( pageNo, pageSize); } else {
	 * logger.info("course Id  is  null"); page = announcementService
	 * .findAnnouncementsReplacement( Long.parseLong(userdetails1
	 * .getProgramId()), pageNo, pageSize); } }
	 * 
	 * page = announcementService.findAnnouncements(announcements, pageNo,
	 * pageSize);
	 * 
	 * }
	 * 
	 * if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
	 * 
	 * if (announcements.getCourseId() != null) {
	 * announcements.setAnnouncementType("COURSE"); page =
	 * announcementService.findByUserAndCourseP(username,
	 * announcements.getCourseId(), pageNo, pageSize);
	 * 
	 * } else { List<Course> courseByUsername = courseService
	 * .findByUserActive(username, userdetails1.getProgramName());
	 * 
	 * List<Long> courseIds = new ArrayList(); for (Course cId :
	 * courseByUsername) {
	 * 
	 * courseIds.add(cId.getId()); } if
	 * ("LIBRARY".equals(announcements.getAnnouncementType())) {
	 * logger.info("announcement type is LIBRARY---"); page =
	 * announcementService.getLibraryAnnouncement( pageNo, pageSize); }
	 * 
	 * else if ("COUNSELOR".equals(announcements .getAnnouncementType())) {
	 * logger.info("announcement type is LIBRARY---"); page =
	 * announcementService.getCounselorAnnouncement( pageNo, pageSize); } else {
	 * 
	 * page = announcementService.getAnnouncementsByCoursesP( courseIds, pageNo,
	 * pageSize); } } } List<Announcement> acadSessionAnnounmentList = new
	 * ArrayList<Announcement>(); acadSessionAnnounmentList =
	 * announcementService .getAnnouncementsByAcadSessionYear(
	 * Long.valueOf(userdetails1.getProgramId()), acadSession);
	 * logger.info("acadSessionAnnounmentList " + acadSessionAnnounmentList);
	 * 
	 * m.addAttribute("acadSessionAnnounmentList", acadSessionAnnounmentList);
	 * List<Announcement> announcementList = page.getPageItems();
	 * announcementList.addAll(acadSessionAnnounmentList);
	 * 
	 * for (Announcement a : announcementList) { Long cId = a.getCourseId(); if
	 * (cId != null) { Course course = courseService.findByID(cId); String
	 * courseName = course.getCourseName(); a.setCourseName(courseName);
	 * 
	 * Program program = programService.findByID(Long.valueOf(a
	 * .getProgramId())); a.setProgramName(program.getProgramName()); //
	 * announcementList.set(announcementList.indexOf(a), a);
	 * m.addAttribute("courseName", courseName); } else if (a.getProgramId() !=
	 * null) { logger.info("------------------------------------------");
	 * Program program = programService.findByID(Long.valueOf(a
	 * .getProgramId())); a.setProgramName(program.getProgramName());
	 * logger.info("a.getProgramName() " + a.getProgramName());
	 * 
	 * } announcementList.set(announcementList.indexOf(a), a); }
	 * logger.info("announcement list size : " + announcementList.size());
	 * m.addAttribute("page", page); m.addAttribute("q",
	 * getQueryString(announcements)); m.addAttribute("pageRowCount",
	 * announcementList.size());
	 * 
	 * m.addAttribute("username", username);
	 * 
	 * if ((announcementList == null || announcementList.size() == 0) &&
	 * (acadSessionAnnounmentList == null || acadSessionAnnounmentList .size()
	 * == 0)) { setNote(m, "No Announcement found"); }
	 * 
	 * } catch (Exception e) {
	 * 
	 * logger.error("Exception", e); setError(m,
	 * "Error in getting Announcement List"); } return
	 * "announcement/userAnnouncementList";
	 * 
	 * }
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value = "/viewUserAnnouncements", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewUserAnnouncements(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, @ModelAttribute Announcement announcements,
			Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;

		m.addAttribute("webPage", new WebPage("announcementList",
				"View Announcements", false, false));

		try {

			Page<Announcement> page = null;

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)
					|| userDetails.getAuthorities().contains(Role.ROLE_DEAN)
					|| userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_HOD)) {

				/*
				 * if (announcements.getCourseId() != null) {
				 * m.addAttribute("courseId", announcements.getCourseId()); page
				 * = announcementService.findAnnouncementsReplacement(
				 * Long.parseLong(userdetails1.getProgramId()),
				 * Long.valueOf(courseId), pageNo, pageSize); }
				 */

				if (courseId != null) {

					m.addAttribute("courseId", courseId);
					page = announcementService.findAnnouncementsReplacement(
							Long.parseLong(userdetails1.getProgramId()),
							Long.valueOf(courseId), pageNo, pageSize);
				} else {

					if ("LIBRARY".equals(announcements.getAnnouncementType())) {

						page = announcementService.getLibraryAnnouncement(
								pageNo, pageSize);
					}

					else if ("COUNSELOR".equals(announcements
							.getAnnouncementType())) {

						page = announcementService.getCounselorAnnouncement(
								pageNo, pageSize);
					} else {

						/*
						 * page = announcementService
						 * .findAnnouncementsReplacement(
						 * Long.parseLong(userdetails1 .getProgramId()), pageNo,
						 * pageSize);
						 */
						page = announcementService
								.getAnnouncementsByCoursesAndAcadSessionAndInstitute(
										userCourseService
												.findUserCourseList(username),
										username, userdetails1.getProgramId(),
										pageNo, pageSize);

					}
				}
				/*
				 * page = announcementService.findAnnouncements(announcements,
				 * pageNo, pageSize);
				 */
			}

			if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

				if (announcements.getCourseId() != null) {
					announcements.setAnnouncementType("COURSE");
					page = announcementService.findByUserAndCourseP(username,
							announcements.getCourseId(), pageNo, pageSize);

				} else {
					List<Course> courseByUsername = courseService
							.findByUserActive(username,
									userdetails1.getProgramName());

					List<Long> courseIds = new ArrayList();
					for (Course cId : courseByUsername) {

						courseIds.add(cId.getId());
					}
					if ("LIBRARY".equals(announcements.getAnnouncementType())) {

						page = announcementService.getLibraryAnnouncement(
								pageNo, pageSize);
					}

					else if ("COUNSELOR".equals(announcements
							.getAnnouncementType())) {

						page = announcementService.getCounselorAnnouncement(
								pageNo, pageSize);
					} else {

						page = announcementService.getAnnouncementsByCoursesP(
								courseIds, pageNo, pageSize);
					}
				}
			}
			List<Announcement> acadSessionAnnounmentList = new ArrayList<Announcement>();
			acadSessionAnnounmentList = announcementService
					.getAnnouncementsByAcadSessionYear(
							Long.valueOf(userdetails1.getProgramId()),
							acadSession);

			m.addAttribute("acadSessionAnnounmentList",
					acadSessionAnnounmentList);
			List<Announcement> announcementList = page.getPageItems();
			announcementList.addAll(acadSessionAnnounmentList);

			for (Announcement a : announcementList) {
				Long cId = a.getCourseId();
				if (cId != null) {
					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();
					a.setCourseName(courseName);

					Program program = programService.findByID(Long.valueOf(a
							.getProgramId()));
					a.setProgramName(program.getProgramName());
					// announcementList.set(announcementList.indexOf(a), a);
					m.addAttribute("courseName", courseName);
				} else if (a.getProgramId() != null) {

					Program program = programService.findByID(Long.valueOf(a
							.getProgramId()));
					a.setProgramName(program.getProgramName());

				}

				if (u.getCampusId() != null) {
					if (a.getCampusId() != null) {

						if (!u.getCampusId().equals(a.getCampusId())) {

							Integer index = announcementList.indexOf(a);

							announcementList.remove(index);
							announcementList.set(index, null);
						} else {
							announcementList
									.set(announcementList.indexOf(a), a);
						}
					} else {

						announcementList.set(announcementList.indexOf(a), a);
					}
				} else {
					announcementList.set(announcementList.indexOf(a), a);
				}
			}

			announcementList.removeAll(Collections.singleton(null));

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(announcements));
			m.addAttribute("pageRowCount", announcementList.size());

			m.addAttribute("username", username);

			if ((announcementList == null || announcementList.size() == 0)
					&& (acadSessionAnnounmentList == null || acadSessionAnnounmentList
							.size() == 0)) {
				setNote(m, "No Announcement found");
			}

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Announcement List");
		}
		return "announcement/userAnnouncementList";

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR","ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/deleteAnnouncement", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteCourse(@RequestParam Integer id,
			RedirectAttributes redirectAttrs) {
		try {
			announcementService.deleteSoftById(String.valueOf(id));
			setSuccess(redirectAttrs, "Announcement deleted successfully");

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in deleting Exam Center.");
		}

		return "redirect:/searchAnnouncement";
	}
	
	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR","ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/deleteFacultyAnnouncement", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteFacultyAnnouncement(@RequestParam Integer id,
			RedirectAttributes redirectAttrs) {
		try {
	
			announcementService.deleteSoftById(String.valueOf(id));
			setSuccess(redirectAttrs, "Announcement deleted successfully");
			
		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in Announcement.");
		}
		return "redirect:/viewUserAnnouncementsSearchNew";
	}
	
	
	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR","ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/makeAnnouncementInactive", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView makeAnnouncementInactive(HttpServletRequest request,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {
		int id = Integer.parseInt(request.getParameter("id"));
		String username = principal.getName();
		int announcementId = Integer.parseInt(request.getParameter("id"));

		annoucementDao.updateAnnouncementToMakeInactive(announcementId);
		setSuccess(redirectAttrs, "Announcement made inactive successfully");
		return new ModelAndView("redirect:/viewUserAnnouncements");
	}

	@Secured("ROLE_SUPPORT_ADMIN")
	@RequestMapping(value = "/supportAdminViewUserAnnouncements", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String supportAdminViewUserAnnouncements(
			@RequestParam(required = false) Long courseId,
			@RequestParam(required = false) String username,
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Model m, Principal principal) {
		/*
		 * Token userdetails1 = (Token) principal; String ProgramName =
		 * userdetails1.getProgramName();
		 */
		User u = userService.findByUserName(username);

		/*
		 * String acadSession = u.getAcadSession();
		 * logger.info("Program NAme--------------------> " + ProgramName);
		 * m.addAttribute("Program_Name", ProgramName);
		 * m.addAttribute("AcadSession", acadSession);
		 */
		Announcement announcements = new Announcement();

		m.addAttribute("webPage", new WebPage("announcementList",
				"View Announcements", false, false));
		UserRole userDetails = userRoleService.findRoleByUsername(username);

		try {

			Page<Announcement> page = null;
			List<Announcement> acadSessionAnnouncementList = new ArrayList<Announcement>();

			if (courseId != null) {

				m.addAttribute("courseId", courseId);
				page = announcementService.findByUserAndCourseP(username,
						courseId, pageNo, pageSize);

			}
			acadSessionAnnouncementList = announcementService
					.getAnnouncementsByAcadSessionYear(u.getProgramId(),
							u.getAcadSession());

			List<Announcement> announcementList = page.getPageItems();
			announcementList.addAll(acadSessionAnnouncementList);
			for (Announcement a : announcementList) {
				Long cId = a.getCourseId();
				if (cId != null) {
					Course course = courseService.findByID(cId);
					String courseName = course.getCourseName();
					a.setCourseName(courseName);

					Program program = programService.findByID(Long.valueOf(a
							.getProgramId()));
					a.setProgramName(program.getProgramName());
					// announcementList.set(announcementList.indexOf(a), a);
					m.addAttribute("courseName", courseName);
				} else if (a.getProgramId() != null) {

					Program program = programService.findByID(Long.valueOf(a
							.getProgramId()));
					a.setProgramName(program.getProgramName());

				}
			}

			m.addAttribute("announcementList", announcementList);
			m.addAttribute("username", username);
			m.addAttribute("pageRowCount", announcementList.size());

			if (announcementList == null || announcementList.size() == 0) {
				setNote(m, "No Announcement found");
			}
		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Announcement List");
		}

		return "announcement/supportAdminUserAnnouncementList";

	}

	@Secured("ROLE_USER")
	@RequestMapping(value = "/viewUserAnnouncementsSearchNew", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewUserAnnouncementsSearchNew(Model m, Principal principal,RedirectAttributes redirectAttrs) {
		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			String programId = userdetails1.getProgramId();
			logger.info("programId----->" + programId);
			if (userdetails1.getAuthorities().contains(Role.ROLE_PARENT)) {
				username = StringUtils.substring(username, 0,
						StringUtils.lastIndexOf(username, "_P"));

			}
			logger.info("userstud----->" + username);
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();
			logger.info("acadSession----->" + acadSession);

			List<Course> courseByUsername = courseService.findByUserActive(
					username, userdetails1.getProgramName());

			List<Long> courseIds = new ArrayList();
			for (Course cId : courseByUsername) {

				courseIds.add(cId.getId());
			}
			logger.info("courseIds----->" + courseIds);
			List<Announcement> courseAnn = announcementService
					.getCourseAnnouncementByCourseIds(courseIds);
			List<String> courseSubType = new ArrayList<String>();
			if (courseAnn.size() > 0) {
				for (Announcement anncourse : courseAnn) {
					if (null != anncourse.getAnnouncementSubType()
							|| !("").equals(anncourse.getAnnouncementSubType())) {
						if (!courseSubType.contains(anncourse
								.getAnnouncementSubType())) {
							courseSubType.add(anncourse.getAnnouncementSubType());
						}
					}
				}
			}
			m.addAttribute("courseSubType", courseSubType);

			List<Announcement> programAnn = announcementService
					.getProgramAnnouncementByProgramId(programId);

			List<String> programSubType = new ArrayList<String>();
			if (programAnn.size() > 0) {
				for (Announcement annprogram : programAnn) {
					if (null != annprogram.getAnnouncementSubType()
							|| !("").equals(annprogram.getAnnouncementSubType())) {
						if (!programSubType.contains(annprogram
								.getAnnouncementSubType())) {
							programSubType.add(annprogram.getAnnouncementSubType());
						}
					}
				}
			}
			m.addAttribute("programSubType", programSubType);

			List<Announcement> timetableAnn = announcementService
					.getTimeTableByProgramId(acadSession, programId);
			logger.info("List Of TimeTable------>" + timetableAnn);
			logger.info("List Of ProgramId------>" + programId);
			logger.info("List Of AcadSession------>" + acadSession);
			List<String> timetableSubType = new ArrayList<String>();
			if (timetableAnn.size() > 0) {
				for (Announcement anntimetable : timetableAnn) {
					if (null != anntimetable.getAnnouncementSubType()
							|| !("").equals(anntimetable.getAnnouncementSubType())) {
						if (!timetableSubType.contains(anntimetable
								.getAnnouncementSubType())) {
							timetableSubType.add(anntimetable
									.getAnnouncementSubType());
						}
					}
				}
			}
			m.addAttribute("timetableSubType", timetableSubType);

			List<Announcement> libraryAnn = announcementService
					.getLibraryAnnouncement();

			List<String> librarySubType = new ArrayList<String>();
			if (libraryAnn.size() > 0) {
				for (Announcement annlibrary : libraryAnn) {
					if (null != annlibrary.getAnnouncementSubType()
							|| !("").equals(annlibrary.getAnnouncementSubType())) {
						if (!librarySubType.contains(annlibrary
								.getAnnouncementSubType())) {
							librarySubType.add(annlibrary.getAnnouncementSubType());
						}
					}
				}
			}

			m.addAttribute("librarySubType", librarySubType);

			List<Announcement> instituteAnn = announcementService
					.getInstituteAnnouncement();

			List<String> instituteSubType = new ArrayList<String>();
			if (instituteAnn.size() > 0) {
				for (Announcement anninstitute : instituteAnn) {
					if (null != anninstitute.getAnnouncementSubType()
							|| !("").equals(anninstitute.getAnnouncementSubType())) {
						if (!instituteSubType.contains(anninstitute
								.getAnnouncementSubType())) {
							instituteSubType.add(anninstitute
									.getAnnouncementSubType());
						}
					}
				}
			}
			m.addAttribute("instituteSubType", instituteSubType);

			List<Announcement> counselorAnn = announcementService
					.getCounselorAnnouncement();

			List<String> counselorSubType = new ArrayList<String>();
			if (counselorAnn.size() > 0) {
				for (Announcement anncounselor : counselorAnn) {
					if (null != anncounselor.getAnnouncementSubType()
							|| !("").equals(anncounselor.getAnnouncementSubType())) {
						if (!counselorSubType.contains(anncounselor
								.getAnnouncementSubType())) {
							counselorSubType.add(anncounselor
									.getAnnouncementSubType());
						}
					}
				}
			}
			m.addAttribute("counselorSubType", counselorSubType);

			Map<String, List<Announcement>> announcementTypeMap = new HashMap();
			announcementTypeMap.put("COURSE", courseAnn);
			announcementTypeMap.put("INSTITUTE", instituteAnn);
			announcementTypeMap.put("LIBRARY", libraryAnn);
			announcementTypeMap.put("PROGRAM", programAnn);
			announcementTypeMap.put("COUNSELOR", counselorAnn);
			announcementTypeMap.put("TIMETABLE", timetableAnn);

			m.addAttribute("announcementTypeMap", announcementTypeMap);

			return "announcement/newUserAnnouncementList";
			
		}catch(Exception e) {
			
			logger.info("Exception"+e.getMessage());
			setNote(m, "No Announcement Found");
			return "announcement/newUserAnnouncementList";
		}
		
		
	}

	// Nafeesa
	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/addAnnouncementFormMultiProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAnnouncementFormMultiProgram(
			@RequestParam(required = false) Long id,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Program> programList = programService.findAllActive();

		m.addAttribute("programList", programList);

		List<String> programsIds = new ArrayList<>();

		for (Program p : programList) {
			programsIds.add(p.getId().toString());
		}

		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Add a new Announcement", true, false));
		if (request.getSession().getAttribute("courseRecord") == null
				|| request.getSession().getAttribute("courseRecord").equals("")) {

		} else {
			request.getSession().removeAttribute("courseRecord");
		}

		if (announcement.getId() != null && !announcement.getId().equals("")) {
			announcement = announcementService.findByID(announcement.getId());
			announcement.setStartDate(Utils.formatDate("yyyy-MM-dd",
					"yyyy-MM-dd HH:mm:ss", announcement.getStartDate()));
			announcement.setEndDate(Utils.formatDate("yyyy-MM-dd",
					"yyyy-MM-dd HH:mm:ss", announcement.getEndDate()));
			m.addAttribute("edit", "true");
		}
		announcement.setAnnouncementType(AnnouncementType.PROGRAM);
		if (userdetails1.getProgramId() != null) {
			// announcement.setAnnouncementType(AnnouncementType.PROGRAM);
			List<UserCourse> acadSessionList = userCourseService
					.findAllAcadSessionsWithProgramIds(programsIds);

			List<String> semesterList = new ArrayList<>();
			for (UserCourse uc : acadSessionList) {
				semesterList.add(uc.getAcadSession());
			}
			// logger.info("semester list -----> " + semesterList);
			announcement.setSemesters(semesterList);
		}
		// logger.info("semester list -----> " + announcement.getSemesters());

		m.addAttribute("announcement", announcement);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		m.addAttribute("allCampuses", userService.findCampus());
		return "announcement/addAnnouncementMultiProgram";
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/addAnnouncementMultiProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addAnnouncementMultiProgram(
			@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam("file") List<MultipartFile> files,
			@RequestParam(name="admincourseId",required = false) String admincourseId,
			@RequestParam(required = false) String typeOfAnn) {

		logger.info("Announcemnet---" + announcement.getProgramIds());
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		String subject = " New Announcement: " + announcement.getSubject()
				+ " send ";
		Map<String, Map<String, String>> result = null;
		List<String> userList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<>();

		userList.add(username);
		String defaultMsg = "\\r\\n\\r\\nNote: This Announcement is created by : ?? \\r\\nTo view any attached files to this mail kindly login to \\r\\nUrl: https://portal.svkm.ac.in/usermgmt/login ";

		List<String> parentList = new ArrayList<String>();
		try {
			/* New Audit changes start */
			if(!Utils.validateStartAndEndDates(announcement.getStartDate(), announcement.getEndDate())) {
				setError(redirectAttrs, "Invalid Start date and End date");
				if (typeOfAnn != null) {
					if ("PROGRAM".equals(typeOfAnn)) {
						return "redirect:/addAnnouncementFormMultiProgram";
					}
				}
				return "redirect:/addAnnouncementForm";
			} /* New Audit changes end */
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					//Audit change start
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttrs, "File Uploaded is not valid");
							if (typeOfAnn != null) {
								if ("PROGRAM".equals(typeOfAnn)) {
									return "redirect:/addAnnouncementFormMultiProgram";
								}
							}
							return "redirect:/addAnnouncementForm";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe")) {
								setError(redirectAttrs, "File Uploaded is not valid");
								if (typeOfAnn != null) {
									if ("PROGRAM".equals(typeOfAnn)) {
										return "redirect:/addAnnouncementFormMultiProgram";
									}
								}
								return "redirect:/addAnnouncementForm";
							}else {
								uploadAnnouncementFileForS3(announcement, file);
							}
						}
					}else {
						setError(redirectAttrs, "File Uploaded is not valid");
						if (typeOfAnn != null) {
							if ("PROGRAM".equals(typeOfAnn)) {
								return "redirect:/addAnnouncementFormMultiProgram";
							}
						}
						return "redirect:/addAnnouncementForm";
					}
					//Audit change end
				}
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				announcement.setSendEmailAlertToParents("Y");
				announcement.setSendSmsAlertToParents("Y");
			}
			logger.info("---------New check-"
					+ announcement.getAnnouncementType());
			if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {

				announcement.setCreatedBy(username);
				announcement.setLastModifiedBy(username);
				announcementService.insertWithIdReturn(announcement);

				logger.info("-New Libraray-----" + announcement);

				setSuccess(redirectAttrs, "Announcement created successfully");

			} else {
				if (announcement.getCourseId() != null) {
					Course c = courseService.findByID(announcement
							.getCourseId());

					List<User> users = userService.findAllByCourse(announcement
							.getCourseId());

					for (User u : users) {
						userList.add(u.getUsername());
					}
					subject = subject + " for course " + c.getCourseName();
					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);
					announcement.setProgramId(c.getProgramId());

					announcementService.insertWithIdReturn(announcement);

					setSuccess(redirectAttrs,
							"Announcement created successfully");
				} else if(admincourseId != null){
					
					 String[] courseId = admincourseId.split("\\s*[,]\\s*");
					 List<String>  semMailList = Arrays.asList(courseId);
					 
					 logger.info("PROGRAMID---------------->"+announcement.getProgramId()+" "+announcement.getProgramIds());
					 //announcement.setProgramIds(announcement.getProgramIds());
					 List<String> programList =  announcement.getProgramIds();
					
//					 for(String p: programList) {
//						 announcement.setProgramId(Long.valueOf(p));
//					 }
					 
					 for(String s : semMailList) {
						 announcement.setCreatedBy(username);
						 announcement.setLastModifiedBy(username);
						 announcement.setCourseId(Long.parseLong(s));
						 announcement.setAnnouncementType(AnnouncementType.COURSE);
						 //announcement.setProgramId(programList);
						 
						 announcementService.insertWithIdReturn(announcement);
							setSuccess(redirectAttrs,
									"Announcement created successfully");
					 }
					
				}
				
				else {
					List<User> users = new ArrayList<User>();
					if (announcement.getAnnouncementType().equals("INSTITUTE")) {

						users = userService.findAllActiveUsers(String
								.valueOf(announcement.getCampusId()));

					}

					/*------------------ Multiple check Program------------- */
					logger.info("New programName---->"
							+ announcement.getProgramName());
					if (announcement.getProgramName() == null) {
						if (announcement.getAnnouncementType()
								.equals("PROGRAM")) {
							if (announcement.getProgramIds().size() > 0) {
								for (String p : announcement.getProgramIds()) {
									List<User> programUsers = new ArrayList<>();
									announcement.setProgramId(Long.valueOf(p));
									logger.info("TimeTable program---->" + p);
									logger.info("TT session---->"
											+ announcement.getAcadSession());
									logger.info("TT year---->"
											+ announcement.getAcadYear());
									logger.info("TT campus---->"
											+ announcement.getCampusId());

									programUsers = userService
											.findByProgramIdAndYear(Long
													.valueOf(p), String
													.valueOf(announcement
															.getAcadYear()),
													String.valueOf(announcement
															.getCampusId()));
									logger.info("New programUsers2---->"
											+ programUsers);

									users.addAll(programUsers);
								}
								logger.info("New users---->" + users);
							}
						}

						for (User u : users) {
							userList.add(u.getUsername());
						}

						announcement.setCreatedBy(username);
						announcement.setLastModifiedBy(username);

						List<Announcement> announcementList = new ArrayList<>();
						if (announcement.getAnnouncementType()
								.equalsIgnoreCase("PROGRAM")) {
							if (announcement.getProgramIds().size() > 0) {
								for (String p1 : announcement.getProgramIds()) {
									Announcement a = new Announcement();
									announcement.setProgramId(Long.valueOf(p1));
									logger.info("Add TT For Loop---->"
											+ announcement.getProgramId());
									a.setProgramId(announcement.getProgramId());
									a.setAnnouncementType(announcement
											.getAnnouncementType());
									a.setSubject(announcement.getSubject());
									a.setDescription(announcement
											.getDescription());
									a.setStartDate(announcement.getStartDate());
									a.setEndDate(announcement.getEndDate());
									a.setCreatedBy(announcement.getCreatedBy());

									a.setLastModifiedBy(announcement
											.getLastModifiedBy());
									a.setAcadSession(announcement
											.getAcadSession());
									a.setFilePath(announcement.getFilePath());
									a.setFilePreviewPath(announcement
											.getFilePreviewPath());
									a.setSendEmailAlert(announcement
											.getSendEmailAlert());
									a.setSendSmsAlert(announcement
											.getSendSmsAlert());
									a.setAnnouncementSubType(announcement
											.getAnnouncementSubType());
									a.setAcadYear(announcement.getAcadYear());
									a.setCampusId(announcement.getCampusId());

									/* a=announcement; */
									logger.info("check a--->" + a);
									announcementList.add(a);
									logger.info("InsideCheck List------>"
											+ announcementList);
								}
								logger.info("Check List------>"
										+ announcementList);
								announcementService
										.insertBatch(announcementList);
							}
						} else {
							announcementService
									.insertWithIdReturn(announcement);
						}
						setSuccess(redirectAttrs,
								"Announcement created successfully");
					}

				}
			}
			if (announcement.getCampusId() != null) {
				Long campusId = announcement.getCampusId();
				announcement
						.setCampusName(userService.findCampusName(campusId));
			}
			try {

				if (!userList.isEmpty()) {

					String notificationEmailMessage = Jsoup.parse(
							announcement.getDescription().concat(defaultMsg))
							.text();

					// announcement
					// .getDescription().concat(defaultMsg);
					String notificationMobileMessage = announcement
							.getSubject().concat(defaultMsg);
					notificationEmailMessage = notificationEmailMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());
					notificationMobileMessage = notificationMobileMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());

					// ----
					/*
					 * List<String> singleUserList = new ArrayList<String>();
					 * singleUserList.add(userList.get(0)); logger.info(
					 * "user List ------------==========================================================3443====================="
					 * +userList.get(0));
					 */
					// ---------
					if ("Y".equals(announcement.getSendEmailAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendEmailAlert())) {

						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");

						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject
								+ " for course ", notificationEmailMessage);
					}
					if ("Y".equals(announcement.getSendSmsAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendSmsAlert())) {

						if (result != null)
							result = userService.findEmailByUserName(userList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject,
								notificationMobileMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in creating Announcement");

			if (typeOfAnn != null) {
				if ("PROGRAM".equals(typeOfAnn)) {
					return "redirect:/addAnnouncementFormMultiProgram";
				}
			}

			return "redirect:/addAnnouncementForm";
		}

		if (acadSessionList.size() > 0) {
			return "redirect:/searchAnnouncement";
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				redirectAttrs.addFlashAttribute("announcement",
						new Announcement());
				return "redirect:/searchAnnouncement";
				// return "redirect:/viewAnnouncement";
			} else if (userdetails1.getAuthorities().contains(
					Role.ROLE_LIBRARIAN)) {
				return "announcement/announcementLibrarian";
			}

			else {
				return "announcement/announcement";
			}
		}

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/newUpdateAnnouncement", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String newUpdateAnnouncement(
			@ModelAttribute Announcement announcement,
			@RequestParam("file") List<MultipartFile> files,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam(required = false) String typeOfAnn) {

		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			String errorMessage = null;

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Announcement announcementDb = announcementService
					.findByID(announcement.getId());

			// If User has not added a new file then take the path from

			// existing announcement to use generic update query

			for (MultipartFile file : files) {
				if (file != null && !file.isEmpty()) {
					//Audit change start
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttrs, "File Uploaded is not valid");
							if (typeOfAnn != null) {
								if ("PROGRAM".equals(typeOfAnn)) {
									return "redirect:/addAnnouncementFormMultiProgram";
								}
							}
							return "redirect:/addAnnouncementForm";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe")) {
								setError(redirectAttrs, "File Uploaded is not valid");
								if (typeOfAnn != null) {
									if ("PROGRAM".equals(typeOfAnn)) {
										return "redirect:/addAnnouncementFormMultiProgram";
									}
								}
								return "redirect:/addAnnouncementForm";
							}else {
								errorMessage = uploadAnnouncementFileForS3(announcement, file);
							}
						}
					}else {
						setError(redirectAttrs, "File Uploaded is not valid");
						if (typeOfAnn != null) {
							if ("PROGRAM".equals(typeOfAnn)) {
								return "redirect:/addAnnouncementFormMultiProgram";
							}
						}
						return "redirect:/addAnnouncementForm";
					}
					//Audit change end
				} else {
					announcement.setFilePath(announcementDb.getFilePath());
					announcement.setFilePreviewPath(announcementDb
							.getFilePreviewPath());
				}
			}

			logger.info("announcementDb----->" + announcementDb);

			Announcement announcementDb1 = LMSHelper.copyNonNullFields(
					announcementDb, announcement);

			logger.info("announcementDb1----->" + announcementDb1);
			announcementDb1.setLastModifiedBy(username);

			announcementService.update(announcementDb1);

			setSuccess(m, "Announcement updated successfully");
			m.addAttribute("announcement", announcementDb1);

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(redirectAttrs, "Error in updating Announcement");
			if (typeOfAnn != null) {
				if ("PROGRAM".equals(typeOfAnn)) {
					return "redirect:/addAnnouncementFormMultiProgram";
				}
			}
			return "redirect:/addAnnouncementForm";
		}
		return "announcement/announcementAdmin";

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/addTimeTableForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addTimeTableForm(@RequestParam(required = false) Long id,
			@ModelAttribute Announcement announcement, Model m,
			Principal principal, HttpServletRequest request) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Program> programList = programService.findAllActive();

		m.addAttribute("programList", programList);

		List<String> programsIds = new ArrayList<>();

		for (Program p : programList) {
			programsIds.add(p.getId().toString());
		}

		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Add a new Announcement", true, false));
		if (request.getSession().getAttribute("courseRecord") == null
				|| request.getSession().getAttribute("courseRecord").equals("")) {

		} else {
			request.getSession().removeAttribute("courseRecord");
		}

		if (announcement.getId() != null && !announcement.getId().equals("")) {
			announcement = announcementService.findByID(announcement.getId());
			announcement.setStartDate(Utils.formatDate("yyyy-MM-dd",
					"yyyy-MM-dd HH:mm:ss", announcement.getStartDate()));
			announcement.setEndDate(Utils.formatDate("yyyy-MM-dd",
					"yyyy-MM-dd HH:mm:ss", announcement.getEndDate()));
			m.addAttribute("edit", "true");
		}
		announcement.setAnnouncementType(AnnouncementType.TIMETABLE);
		/* if (userdetails1.getProgramId() != null){ */
		// announcement.setAnnouncementType(AnnouncementType.PROGRAM);
		List<UserCourse> acadSessionList = userCourseService
				.findAllAcadSessionsWithProgramIds(programsIds);

		logger.info("acadSessionList--------->" + acadSessionList);

		List<String> semesterList = new ArrayList<>();
		for (UserCourse uc : acadSessionList) {
			semesterList.add(uc.getAcadSession());
		}

		logger.info("semesterList--------->" + semesterList);
		// logger.info("semester list -----> " + semesterList);
		announcement.setSemesters(semesterList);
		/* } */
		// logger.info("semester list -----> " + announcement.getSemesters());

		m.addAttribute("announcement", announcement);
		if (sendAlertsToParents.equalsIgnoreCase("Y")) {
			m.addAttribute("sendAlertsToParents", true);
		} else {
			m.addAttribute("sendAlertsToParents", false);
		}
		m.addAttribute("allCampuses", userService.findCampus());
		return "announcement/addTimeTable";
	}

	/*@RequestMapping(value = "/addTimeTable", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addTimeTable(@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam("file") List<MultipartFile> files,@RequestParam(required=false) String typeOfAnn) {

		logger.info("Announcemnet---"+announcement.getProgramIds());
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		String subject = " New Announcement: " + announcement.getSubject()
				+ " send ";
		Map<String, Map<String, String>> result = null;
		List<String> userList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<>();
		
		userList.add(username);
		String defaultMsg = "\\r\\n\\r\\nNote: This Announcement is created by : ?? \\r\\nTo view any attached files to this mail kindly login to \\r\\nUrl: https://portal.svkm.ac.in/usermgmt/login ";

		List<String> parentList = new ArrayList<String>();
		try {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					uploadAnnouncementFile(announcement, file);
				}
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				announcement.setSendEmailAlertToParents("Y");
				announcement.setSendSmsAlertToParents("Y");
			}
			logger.info("---------New check-"+announcement.getAnnouncementType());
			if (announcement.getAnnouncementType().equalsIgnoreCase("LIBRARY")) {

				announcement.setCreatedBy(username);
				announcement.setLastModifiedBy(username);
				announcementService.insertWithIdReturn(announcement);
				
				logger.info("-New Libraray-----"+announcement);

				setSuccess(redirectAttrs, "Announcement created successfully");

			} else {
				if (announcement.getCourseId() != null) {
					Course c = courseService.findByID(announcement
							.getCourseId());

					List<User> users = userService.findAllByCourse(announcement
							.getCourseId());

					for (User u : users) {
						userList.add(u.getUsername());
					}
					subject = subject + " for course " + c.getCourseName();
					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);
					announcement.setProgramId(c.getProgramId());

					announcementService.insertWithIdReturn(announcement);

					setSuccess(redirectAttrs,
							"Announcement created successfully");
				} else {
					List<User> users = new ArrayList<User>();
					if (announcement.getAnnouncementType().equals("INSTITUTE")) {

						users = userService.findAllActiveUsers(String
								.valueOf(announcement.getCampusId()));
						 
					}
					
					
                       ------------------ Multiple check Program------------- 
					logger.info("New programName---->"+announcement.getProgramName());
					if(announcement.getProgramName()==null){
					if (announcement.getAnnouncementType().equals("PROGRAM")) {	
						if(announcement.getProgramIds().size() > 0)
						{
						 for( String p: announcement.getProgramIds()) {
							 List<User>  programUsers = new ArrayList<>();
						announcement.setProgramId(Long.valueOf(p));
						logger.info("TimeTable program---->"+p);
						logger.info("TT session---->"+announcement.getAcadSession());
						logger.info("TT year---->"+announcement.getAcadYear());
						logger.info("TT campus---->"+announcement.getCampusId());
						
						
								programUsers = userService
										.findByProgramIdAndYear(Long
												.valueOf(p),
												String.valueOf(announcement
														.getAcadYear()), String
														.valueOf(announcement
																.getCampusId()));
								logger.info("New programUsers2---->"+programUsers);
							
						
						users.addAll(programUsers);
						}
						 logger.info("New users---->"+users);
						}
						}
					
					for (User u : users) {
						userList.add(u.getUsername());
					}

					announcement.setCreatedBy(username);
					announcement.setLastModifiedBy(username);
					
					List<Announcement> announcementList= new ArrayList<>();
					if(announcement.getAnnouncementType().equalsIgnoreCase("TIMETABLE"))
					{
						if(announcement.getProgramIds().size()>0){
						for(String p1:announcement.getProgramIds())
						{ 
							Announcement a = new Announcement();
							announcement.setProgramId(Long.valueOf(p1));
							logger.info("Add TT For Loop---->"+announcement.getProgramId());
							a.setProgramId(announcement.getProgramId());
							a.setAnnouncementType(announcement.getAnnouncementType());
							a.setSubject(announcement.getSubject());
							a.setDescription(announcement.getDescription());
							a.setStartDate(announcement.getStartDate());
							a.setEndDate(announcement.getEndDate());
							a.setCreatedBy(announcement.getCreatedBy());
							
							a.setLastModifiedBy(announcement.getLastModifiedBy());
							a.setAcadSession(announcement.getAcadSession());
							a.setFilePath(announcement.getFilePath());
							a.setFilePreviewPath(announcement.getFilePreviewPath());
							a.setSendEmailAlert(announcement.getSendEmailAlert());
							a.setSendSmsAlert(announcement.getSendSmsAlert());
							a.setAnnouncementSubType(announcement.getAnnouncementSubType());
							a.setAcadYear(announcement.getAcadYear());
							a.setCampusId(announcement.getCampusId());

						    a=announcement;
							logger.info("check a--->"+a);
							announcementList.add(a);
							logger.info("InsideCheck List------>"+announcementList);
						 }
						logger.info("Check List------>"+announcementList);
						announcementService.insertBatch(announcementList);
						}
					}
					else{
						announcementService.insertWithIdReturn(announcement);
					}
					setSuccess(redirectAttrs,
							"Announcement created successfully");
					}
					
				}
			}
			if (announcement.getCampusId() != null) {
				Long campusId = announcement.getCampusId();
				announcement
						.setCampusName(userService.findCampusName(campusId));
			}
			try {

				if (!userList.isEmpty()) {

					String notificationEmailMessage = Jsoup.parse(
							announcement.getDescription().concat(defaultMsg))
							.text();

					// announcement
					// .getDescription().concat(defaultMsg);
					String notificationMobileMessage = announcement
							.getSubject().concat(defaultMsg);
					notificationEmailMessage = notificationEmailMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());
					notificationMobileMessage = notificationMobileMessage
							.toString().replace(
									"??",
									" \\r\\nName :" + u1.getFirstname() + " "
											+ u1.getLastname()
											+ "\\r\\nEmail-Id: "
											+ u1.getEmail());
					

					// ----
					
					 * List<String> singleUserList = new ArrayList<String>();
					 * singleUserList.add(userList.get(0)); logger.info(
					 * "user List ------------==========================================================3443====================="
					 * +userList.get(0));
					 
					// ---------
					if ("Y".equals(announcement.getSendEmailAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendEmailAlert())) {
						
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");

						
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, subject
								+ " for course ", notificationEmailMessage);
					}
					if ("Y".equals(announcement.getSendSmsAlertToParents())) {
						for (String s : userList) {

							StudentParent sp = studentParentService
									.findParentByStudent(s);
							if (sp != null) {
								parentList.add(sp.getParent_username());
							}
						}
						if (parentList.size() != 0 || !parentList.isEmpty()) {
							userList.addAll(parentList);
						}

					}

					if ("Y".equals(announcement.getSendSmsAlert())) {
						
						if (result != null)
							result = userService.findEmailByUserName(userList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						notifier.sendEmail(email, mobiles, subject,
								notificationMobileMessage);
					}
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in creating Announcement");
			
			if(typeOfAnn!=null){
				if("PROGRAM".equals(typeOfAnn)){
					return "redirect:/addTimeTableForm";
				}
			}
			
			return "redirect:/addAnnouncementForm";
		}
		
		if (acadSessionList.size() > 0) {
			return "redirect:/searchAnnouncement";
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				redirectAttrs.addFlashAttribute("announcement", new Announcement());
				return "redirect:/searchAnnouncement";
				//return "redirect:/viewAnnouncement";
			} else if(userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {
				return "announcement/announcementLibrarian";
			} else if(userdetails1.getAuthorities().contains(Role.ROLE_EXAM)) {
				logger.info("ROLE_EXAM-------->");
				redirectAttrs.addFlashAttribute("announcement", new Announcement());
				redirectAttrs.addAttribute("announcementType", "TIMETABLE");
				redirectAttrs.addAttribute("announcementSubType", "EXAM");
				return "redirect:/searchAnnouncementForLibrarian";
			}
			
			else {
			return "announcement/announcement";
			}
		}
	
	
	
	}*/
	
	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/addTimeTable", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addTimeTable(@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam("file") List<MultipartFile> files,@RequestParam(required=false) String typeOfAnn) {

		logger.info("Announcemnet---"+announcement.getProgramIds());
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		
		String programNames = "";
		Map<String, Map<String, String>> result = null;
		List<String> userList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<>();
		userList.add(username);
		try {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					//Audit change start
					if (file.getOriginalFilename().contains(".")) {
						Long count = file.getOriginalFilename().chars().filter(c -> c == ('.')).count();
						logger.info("length--->"+count);
						if (count > 1 || count == 0) {
							setError(redirectAttrs, "File uploaded is invalid!");
							if(typeOfAnn!=null){
								if("PROGRAM".equals(typeOfAnn)){
									return "redirect:/addTimeTableForm";
								}
							}
							return "redirect:/addAnnouncementForm";
						}else {
							String extension = FilenameUtils.getExtension(file.getOriginalFilename());
							logger.info("extension--->"+extension);
							if(extension.equalsIgnoreCase("exe")) {
								setError(redirectAttrs, "File uploaded is invalid!");
								if(typeOfAnn!=null){
									if("PROGRAM".equals(typeOfAnn)){
										return "redirect:/addTimeTableForm";
									}
								}
								return "redirect:/addAnnouncementForm";
							}else {
								uploadAnnouncementFileForS3(announcement, file);
							}
						}
					}else {
						setError(redirectAttrs, "File uploaded is invalid!");
						if(typeOfAnn!=null){
							if("PROGRAM".equals(typeOfAnn)){
								return "redirect:/addTimeTableForm";
							}
						}
						return "redirect:/addAnnouncementForm";
					}
					//Audit change end
				}
			}
			if (sendAlertsToParents.equalsIgnoreCase("Y")) {
				announcement.setSendEmailAlertToParents("Y");
				announcement.setSendSmsAlertToParents("Y");
			}
			logger.info("---------New check-"+announcement.getAnnouncementType());
			
				
			List<User> users = new ArrayList<User>();
			   /*------------------ Multiple check Program------------- */
			logger.info("New programName---->"+announcement.getProgramName());
			if(announcement.getProgramName()==null){
				if (announcement.getAnnouncementType().equals("TIMETABLE")) {	
					if(announcement.getProgramIds().size() > 0)
					{
					 for( String p: announcement.getProgramIds()) {
						 List<User>  programUsers = new ArrayList<>();
					announcement.setProgramId(Long.valueOf(p));
					logger.info("TimeTable program---->"+p);
					logger.info("TT session---->"+announcement.getAcadSession());
					logger.info("TT year---->"+announcement.getAcadYear());
					
					
					programUsers = userService
							.findStudentByProgramIdAndSemesterAndYear(Long
									.valueOf(p),
									announcement.getAcadSession(),
									String.valueOf(announcement
											.getAcadYear()));
					logger.info("New programUsers2---->"+programUsers);
						
					
					users.addAll(programUsers);
					}
					 logger.info("New users---->"+users);
					}
				}
			
				for (User u : users) {
					userList.add(u.getUsername());
				}
				
				logger.info("User List-------->"+userList);
	
				announcement.setCreatedBy(username);
				announcement.setLastModifiedBy(username);
				
				List<Announcement> announcementList= new ArrayList<>();
				if(announcement.getAnnouncementType().equalsIgnoreCase("TIMETABLE"))
				{
					if(announcement.getProgramIds().size()>0){
					for(String p1:announcement.getProgramIds())
					{ 
						
						Program prg = programService.findByID(Long.valueOf(p1));
						programNames += prg.getProgramName() + " / " ;
						Announcement a = new Announcement();
						announcement.setProgramId(Long.valueOf(p1));
						logger.info("Add TT For Loop---->"+announcement.getProgramId());
						a.setProgramId(announcement.getProgramId());
						a.setAnnouncementType(announcement.getAnnouncementType());
						a.setSubject(announcement.getSubject());
						a.setDescription(announcement.getDescription());
						a.setStartDate(announcement.getStartDate());
						a.setEndDate(announcement.getEndDate());
						a.setCreatedBy(announcement.getCreatedBy());
						
						a.setLastModifiedBy(announcement.getLastModifiedBy());
						a.setAcadSession(announcement.getAcadSession());
						a.setFilePath(announcement.getFilePath());
						a.setFilePreviewPath(announcement.getFilePreviewPath());
						a.setSendEmailAlert(announcement.getSendEmailAlert());
						a.setSendSmsAlert(announcement.getSendSmsAlert());
						a.setAnnouncementSubType(announcement.getAnnouncementSubType());
						a.setAcadYear(announcement.getAcadYear());
						a.setCampusId(announcement.getCampusId());
						a.setExamMonth(announcement.getExamMonth());
						a.setExamYear(announcement.getExamYear());
	
					    /*a=announcement;*/
						logger.info("check a--->"+a);
						announcementList.add(a);
						logger.info("InsideCheck List------>"+announcementList);
					 }
					logger.info("Check List------>"+announcementList);
					announcementService.insertBatch(announcementList);
					}
				}
				else{
					announcementService.insertWithIdReturn(announcement);
				}
				setSuccess(redirectAttrs,
						"Announcement created successfully");
			}
			
			if (announcement.getCampusId() != null) {
				Long campusId = announcement.getCampusId();
				announcement
						.setCampusName(userService.findCampusName(campusId));
			}
			try {
				String notificationEmailMessage ="";
				String notificationMobileMessage="";
				if (!userList.isEmpty()) {
					
					if(announcement.getExamType().equals("finalExam")){
						 notificationEmailMessage = "Dear Student, <br/><br/>The Final examination Time table of "
								+ announcement.getExamMonth() 
								+ " " 
								+ announcement.getExamYear() 
								+ ", "
								+ announcement.getAcadSession()
								+" of "
								+ programNames.substring(0, programNames.length() - 3)
								+" , has been uploaded on student Portal. <br/>Please visit Student Portal For Details.( https://portal.svkm.ac.in/usermgmt/login )<br/><br/> Regards, <br/>Controller of Examinations";
								
						 notificationMobileMessage = "Final  examination Time table of "
								+ announcement.getExamMonth() 
								+ " " 
								+ announcement.getExamYear() 
								+ " "
								+ "has been declared.%0aPlease visit Student Portal For Details.( https://portal.svkm.ac.in/usermgmt/login )";
						}
						else if(announcement.getExamType().equals("reExam")){
							 notificationEmailMessage = "Dear Student, <br/><br/>The Re-Examination Time table of "
									+ announcement.getExamMonth() 
									+ " " 
									+ announcement.getExamYear() 
									+ ", "
									+ announcement.getAcadSession()
									+" of "
									+ programNames.substring(0, programNames.length() - 3)
									+" , has been uploaded on student Portal. <br/>Please visit Student Portal For Details.( https://portal.svkm.ac.in/usermgmt/login )<br/><br/> Regards, <br/>Controller of Examinations";
									
							 notificationMobileMessage = "Re-Examination Time table of "
									+ announcement.getExamMonth() 
									+ " " 
									+ announcement.getExamYear() 
									+ " "
									+ "has been declared.%0aPlease visit Student Portal For Details.( https://portal.svkm.ac.in/usermgmt/login )";
						}
					if ("Y".equals(announcement.getSendEmailAlert())) {
						
						result = userService.findEmailByUserName(userList);
						Map<String, String> email = result.get("emails");

						
						Map<String, String> mobiles = new HashMap();
						notifier.sendEmail(email, mobiles, " Exam Timetable ", notificationEmailMessage);
					}

					if ("Y".equals(announcement.getSendSmsAlert())) {
						
						if (result != null)
							result = userService.findEmailByUserName(userList);
						Map<String, String> email = new HashMap();
						Map<String, String> mobiles = result.get("mobiles");
						/*notifier.sendEmail(email, mobiles, " Exam Timetable ",
								notificationMobileMessage);*/
						
						try {
							for (Map.Entry<String,String> entry : mobiles.entrySet()){
	                            WebTarget webTarget = client.target(URIUtil
	                                                            .encodeQuery("https://api-alerts.kaleyra.com/v4/?api_key=A2ca055a3f744079004c80b9d2f9a783e&method=sms&message="
	                            + notificationMobileMessage
	                            +"&to="
	                            + entry.getValue()
	                            +"&sender=NMUEXM"));
	                            Invocation.Builder invocationBuilder = webTarget
	                                                            .request(MediaType.APPLICATION_JSON);
	                            String resp = invocationBuilder.get(String.class);
	                            logger.info("resp" + resp);
							}
			            } catch (Exception ex) {
			            	logger.error("Exception", ex);
			            }
					}
				}
			} catch (Exception e) {
				logger.error("Exception", e);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttrs, "Error in creating Announcement");
			
			if(typeOfAnn!=null){
				if("PROGRAM".equals(typeOfAnn)){
					return "redirect:/addTimeTableForm";
				}
			}
			
			return "redirect:/addAnnouncementForm";
		}
		
		if (acadSessionList.size() > 0) {
			return "redirect:/searchAnnouncement";
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				redirectAttrs.addFlashAttribute("announcement", new Announcement());
				return "redirect:/searchAnnouncement";
				//return "redirect:/viewAnnouncement";
			} else if(userdetails1.getAuthorities().contains(Role.ROLE_LIBRARIAN)) {
				return "announcement/announcementLibrarian";
			} else if(userdetails1.getAuthorities().contains(Role.ROLE_EXAM)) {
				logger.info("ROLE_EXAM-------->");
				redirectAttrs.addFlashAttribute("announcement", new Announcement());
				redirectAttrs.addAttribute("announcementType", "TIMETABLE");
				redirectAttrs.addAttribute("announcementSubType", "EXAM");
				return "redirect:/searchAnnouncementForLibrarian";
			}
			
			else {
			return "announcement/announcement";
			}
		}
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/updateTimeTable", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String updateTimeTable(@ModelAttribute Announcement announcement,
			RedirectAttributes redirectAttrs, Model m, Principal principal,
			@RequestParam(required = false) String typeOfAnn) {

		redirectAttrs.addFlashAttribute("announcement", announcement);
		m.addAttribute("webPage", new WebPage("addAnnouncement",
				"Announcement Details", false, false));
		try {
			String username = principal.getName();

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Announcement announcementDb = announcementService
					.findByID(announcement.getId());
			announcementDb = LMSHelper.copyNonNullFields(announcementDb,
					announcement);
			announcementDb.setLastModifiedBy(username);

			announcementService.update(announcementDb);

			setSuccess(m, "Announcement updated successfully");
			m.addAttribute("announcement", announcementDb);

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(redirectAttrs, "Error in updating Announcement");
			if (typeOfAnn != null) {
				if ("PROGRAM".equals(typeOfAnn)) {
					return "redirect:/addTimeTableForm";
				}
			}
			return "redirect:/addAnnouncementForm";
		}
		return "announcement/announcementAdmin";

	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/timetableResult", method = { RequestMethod.GET,
			RequestMethod.POST })
	public @ResponseBody String timetableResult(
			@ModelAttribute Announcement announcement,
			@RequestParam(required = false) String username,
			@RequestParam("announcementId") String announcementId, Model m,
			Principal principal) {

		try {
			logger.info("mapping called");
			logger.info("announcement14--->" + announcement.getUsername());

			Announcement ann = announcementService.getTimeTableStudent(
					principal.getName(), announcementId);
			if (ann == null) {
				announcementService.insertTimeTableStudent(principal.getName(),
						announcementId);
			}
			/*
			 * else{
			 * announcementService.getTimeTableStudent(principal.getName(),
			 * announcementId); }
			 */

			return "Success";
		} catch (Exception e) {

			logger.error("Error, " + e);
			return "Error";
		}
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/downloadStudentsReport", method = RequestMethod.GET)
	public void downloadStudentsRCForms(HttpServletResponse response,
			Principal principal, @RequestParam("id") Long id) {

		logger.info("Id is ------" + id);

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		InputStream is = null;
		String filePath = "";
		// Announcement Bean = announcementService.findByID(id);
		try {

			List<Announcement> aList = new ArrayList<Announcement>();

			aList = announcementService.getStudentReport(id);

			logger.info("aList---------->" + aList);

			filePath = getRCApplications(aList);
			is = new FileInputStream(filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();

			response.flushBuffer();
		} catch (Exception ex) {
			logger.info(
					"Error writing file to output stream. Filename was '{}'",
					ex);
			throw new RuntimeException("IOError writing file to output stream");
		} finally {
			if (is != null) {
				org.apache.commons.io.IOUtils.closeQuietly(is);
			}

			FileUtils.deleteQuietly(new File(filePath));
		}

	}

	public String getRCApplications(List<Announcement> aList) {
		String filePath = downloadAllFolder + File.separator
				+ "StudentsReport.xlsx";
		String h[] = { "Announcement Name", "SAP Id" };
		List<String> header = Arrays.asList(h);
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Student Report Sheet");
		Row headerRow = sheet.createRow(0);
		for (int colNum = 0; colNum < header.size(); colNum++) {
			Cell cell = headerRow.createCell(colNum);
			cell.setCellValue(header.get(colNum));
		}
		int rowNum = 1;

		for (Announcement a : aList) {
			Row row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(a.getSubject());
			row.createCell(1).setCellValue(a.getUsername());
			rowNum++;
		}

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
	
	@Secured({"ROLE_ADMIN","ROLE_FACULTY","ROLE_EXAM","ROLE_LIBRARIAN","ROLE_COUNSELOR"})
	@RequestMapping(value = "/getCourseByProgramIdAdmin", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseBySemester(
			@RequestParam(name = "programIds") String programIds,
			Principal principal) {
		String json = "";
		
		List<Course>  courseList = courseService.getCourseByProgramId(programIds);
			
		logger.info("courseList------------ " + courseList);

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		logger.info("courseListCombine------------ " + courseList);
		
		if(courseList.size()>0){
		for (Course ass : courseList) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(ass.getId()),ass.getCourseName() + " ("+ass.getAcadYearCode()+")");
			res.add(returnMap);
		}
		}
		else{
			Map<String, String> returnMap = new HashMap();
			returnMap.put("NA","No Course Available");
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
	
	
	
}
