package com.spts.lms.web.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.forum.ForumReply;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.FacultyDetails;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.daos.user.UserDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.helpers.excel.UserCourseExcelHelper;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.calender.CalenderService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.dashboard.DashboardService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.forum.ForumReplyService;
import com.spts.lms.services.forum.ForumService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.FacultyDetailsService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.web.helper.WebPage;

@Secured("ROLE_ADMIN")
@Controller
public class UserCourseController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	UserService userService;

	@Autowired
	UserRoleService userRoleService;

	

	@Autowired
	CalenderService calenderService;

	@Autowired
	CourseService courseService;

	@Autowired
	ProgramService programService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	TestService testService;

	@Autowired
	private AnnouncementService announcementService;

	@Autowired
	private DashboardService dashBoardService;

	@Autowired
	FacultyDetailsService facultyDetailsService;
	@Autowired
	ForumService forumService;

	@Autowired
	ForumReplyService forumReplyService;

	@Autowired
	ContentService contentService;
	
	@Autowired
	UserCourseService userCourseService;

	@Autowired
	UserDAO userDao;

	@Autowired
	HttpSession session;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;

	@Value("${workStoreDir:''}")
	private String workDir;
	
	@Autowired
	StudentFeedbackService studentFeedbackService;

	/*
	 * @Value("#{'${userRoleMgmtCrudUrl:http://13.76.166.129:8080/usermgmtcrud}'}"
	 * ) String userRoleMgmtCrudUrl;
	 */

	Client client = ClientBuilder.newClient();

	private UserCourseExcelHelper getUserCourseExcelHelper() {
		return (UserCourseExcelHelper) act.getBean("userCourseExcelHelper");
	}

	private static final Logger logger = Logger
			.getLogger(UserCourseController.class);

	@ModelAttribute("programs")
	public List<Program> getPrograms() {
		return programService.findAllActive();
	}

	@ModelAttribute("courseList")
	public List<Course> getCourseList() {
		return courseService.findAllActive();
	}

	@ModelAttribute("courseNameMap")
	public Map<Long, String> getCourseNames() {
		List<Course> courseNameList = courseService.findAllActive();
		HashMap<Long, String> courseNameMap = new HashMap<Long, String>();
		for (Course course : courseNameList) {
			courseNameMap.put(course.getId(), course.getCourseName());
		}

		return courseNameMap;
	}

	/*
	 * @RequestMapping(value = "/showMyCourseStudents", method = {
	 * RequestMethod.GET, RequestMethod.POST }) public String
	 * showMyCourseStudents(@ModelAttribute UserCourse userCourse, Model m,
	 * 
	 * @RequestParam Long courseId, Principal principal) {
	 * m.addAttribute("webPage", new WebPage("myCourseStudents", "My Students",
	 * true, true)); List<UserCourse> students = userCourseService
	 * .findStudentsForFaculty(courseId); logger.info("students" + students);
	 * m.addAttribute("students", students); m.addAttribute("allCourses",
	 * courseService.findByUserActive(principal.getName())); Course course =
	 * courseService.findByID(courseId);
	 * userCourse.setCourseName(course.getCourseName()); return
	 * "course/showMyStudents";
	 * 
	 * }
	 */

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/showMyProgramStudentsForAdmin", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String showMyProgramStudentsForAdmin(@ModelAttribute User user,
			Model m, @RequestParam(required = false) String programId,
			Principal principal) {
		m.addAttribute("webPage", new WebPage("myCourseStudents",
				"My Students", true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		if (programId != null) {

			List<UserCourse> students = userCourseService
					.getStudentsByProgram(programId);

			for (UserCourse uc : students) {
				User users = userService.findByUserName(uc.getUsername());
				uc.setRollNo(users.getRollNo());
				uc.setEmail(users.getEmail());
				uc.setMobile(users.getMobile());
				uc.setAcadSession(users.getAcadSession());
				students.set(students.indexOf(uc), uc);
			}
			
			m.addAttribute("students", students);

		} else {

			Token userDetails = (Token) principal;

			List<User> students = userService.findAllStudents(Long
					.valueOf(userDetails.getProgramId()));
			
			m.addAttribute("students", students);
		}

		return "course/showMyStudents_ADMIN";

	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN"})
	@RequestMapping(value = "/showMyCourseStudents", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String showMyCourseStudents(@ModelAttribute UserCourse userCourse,
			Model m, @RequestParam(required = false) Long courseId,
			@RequestParam(required = false) Long campusId, Principal principal) {
		m.addAttribute("webPage", new WebPage("myCourseStudents",
				"My Students", true, false));

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCampuses", userService.findCampus());
		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			m.addAttribute("allCourses",
					courseService.findByCoursesBasedOnProgramName(ProgramName));
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				m.addAttribute("allCourses", courseService.findByUserActive(
						principal.getName(), userdetails1.getProgramName()));
			}

		}

		if (courseId != null) {
			List<UserCourse> students = new ArrayList<UserCourse>();
			if (campusId != null) {
				students = userCourseService
						.findStudentsForFacultyWithCampusId(courseId, campusId);
			} else {
				students = userCourseService.findStudentsForFaculty(courseId);
			}

			for (UserCourse uc : students) {
				User users = userService.findByUserName(uc.getUsername());
				uc.setRollNo(users.getRollNo());
				uc.setEmail(users.getEmail());
				uc.setMobile(users.getMobile());
				uc.setAcadSession(users.getAcadSession());
				students.set(students.indexOf(uc), uc);
			}
			
			m.addAttribute("students", students);
			Course course = courseService.findByID(courseId);
			userCourse.setCourseName(course.getCourseName());
		} else {

			Token userDetails = (Token) principal;

			// Long programId = Long.valueOf(userDetails.getProgramId());
			List<User> students = userService.findAllStudents(Long
					.valueOf(userDetails.getProgramId()));
			m.addAttribute("programName", userdetails1.getProgramName());
			
			m.addAttribute("students", students);
			return "course/showMyStudentsProgram";
		}
		
		if(userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			return "course/showMyStudentsAdmin";
		}

		return "course/showMyStudents";

	}

	@RequestMapping(value = "/addFacultyCourseForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String addFacultyCourseForm(@ModelAttribute UserCourse userCourse,
			Model m) {
		m.addAttribute("webPage", new WebPage("addFacultyCourse",
				"Add Faculty Courses", true, false));
		m.addAttribute("facultyList", userService.findAllFaculty());
		// m.addAttribute("userCourses",
		// userCourseService.findFacultyByAcadMonthAndAcadYear(userCourse.getAcadMonth(),
		// userCourse.getAcadYear()));
		return "course/addFacultyCourse";
	}

	/*
	 * @RequestMapping(value = "/addFacultyCourse", method = RequestMethod.POST)
	 * public String addFacultyCourse(@ModelAttribute UserCourse userCourse,
	 * RedirectAttributes redirectAttr, Principal principal) { String username =
	 * principal.getName(); try { userCourse.setCreatedBy(username);
	 * userCourse.setLastModifiedBy(username);
	 * 
	 * userCourseService.insert(userCourse);
	 * 
	 * setSuccess(redirectAttr, "Faculty Added"); } catch (Exception e) {
	 * logger.error("Exception", e); setError(redirectAttr,
	 * "Error in adding Faculty"); }
	 * 
	 * redirectAttr.addFlashAttribute("userCourse", userCourse); return
	 * "redirect:/addFacultyCourseForm"; }
	 */

	@RequestMapping(value = "/addFacultyCourse", method = RequestMethod.POST)
	public String addFacultyCourse(@ModelAttribute UserCourse userCourse,
			RedirectAttributes redirectAttr, Principal principal) {
		String username = principal.getName();
		try {
			List<UserCourse> uc = new ArrayList<>();
			userCourse.setCreatedBy(username);
			userCourse.setLastModifiedBy(username);
			userCourse.setRole("ROLE_FACULTY");
			userCourse.setOperation("INSERT");
			uc.add(userCourse);
			Course c = courseService.findByID(userCourse.getCourseId());
			userCourseService.insert(userCourse);
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(uc);
			
			// logger.info(" json--->" + (json));
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/addOrUpdateUserCourse?json=" + json));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			// logger.info("resp" + resp);

			setSuccess(redirectAttr, "Faculty Added");
		} catch (Exception e) {
			logger.error("Exception", e);
			setError(redirectAttr, "Error in adding Faculty");
		}

		redirectAttr.addFlashAttribute("userCourse", userCourse);
		return "redirect:/addFacultyCourseForm";
	}

	@RequestMapping(value = "/deleteFacultyCourse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String deleteProgram(@ModelAttribute UserCourse userCourse,
			RedirectAttributes redirectAttrs) {
		redirectAttrs.addFlashAttribute("userCourse", userCourse);
		try {
			userCourseService.delete(userCourse);
			setSuccess(redirectAttrs, "Removed Faculty from the course.");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Faculty Course.");
		}

		return "redirect:/addFacultyCourseForm";
	}

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/uploadUserCourseForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadUserCourseForm(Model m) {
		m.addAttribute("webPage", new WebPage("uploadUserCourse",
				"Upload Student/Faculty Course Mapping", true, true, true,
				true, false));
		m.addAttribute("userCourse", new UserCourse());
		return "course/uploadUserCourse";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/uploadUserCourseEnrolForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String uploadUserCourseEnrolForm(Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("uploadUserCourse",
				"Upload Student/Faculty Course Mapping", true, true, true,
				true, false));
		Token userDetails = (Token) principal;

		String progName = userDetails.getProgramName();
		m.addAttribute("userCourse", new UserCourse());
		List<String> acadYearCodeList= courseService.findAcadYearCode();
		List<String> acadYearList = courseService.getAllAcadYear();
		for (String s : acadYearList) {
            if(!acadYearCodeList.contains(s)) {
            	acadYearCodeList.add(s);
            }
		}
		if(acadYearCodeList.contains("")) {
			acadYearCodeList.remove("");
		}
		 Collections.sort(acadYearCodeList);
		m.addAttribute("acadYearCodeList",acadYearCodeList);
		//m.addAttribute("allCourses",courseService.findByCoursesBasedOnProgramName(progName));

		return "course/uploadUserCourseEnrol";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/generateUserCourseEnrolTemplate", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String generateUserCourseEnrolTemplate(Model m,
			@ModelAttribute UserCourse userCourse, Principal principal) {
		m.addAttribute("webPage", new WebPage("uploadUserCourse",
				"Upload Student/Faculty Course Mapping", true, true, true,
				true, false));
		
		List<Long> courseIds = userCourse.getCourseIds();
		Token userDetails = (Token) principal;
		String progName = userDetails.getProgramName();
		String username = principal.getName();
		List<String> acadYearCodeList= courseService.findAcadYearCode();
		List<String> acadYearList = courseService.getAllAcadYear();
		for (String s : acadYearList) {
            if(!acadYearCodeList.contains(s)) {
            	acadYearCodeList.add(s);
            }
		}
		if(acadYearCodeList.contains("")) {
			acadYearCodeList.remove("");
		}
		 Collections.sort(acadYearCodeList);
		m.addAttribute("acadYearCodeList",acadYearCodeList);
		List<Course> allCourses = courseService.findByCoursesBasedOnYearAndcreatedBy(userCourse.getAcadYear(),username);
		for(Course c: allCourses) {
			Program progDB = programService.findByID(c.getProgramId());
			String programName = progDB.getProgramName();
			int indx = programName.lastIndexOf("-");
			String progCode = programName.substring(indx+1, programName.length());
			String newCourseName = c.getCourseName()+"("+progCode+")";
			c.setCourseName(newCourseName);
			c.setCourseId(String.valueOf(c.getId()));
		}
		m.addAttribute("allCourses",allCourses);
		//m.addAttribute("allCourses",courseService.findByCoursesBasedOnYearAndcreatedBy(userCourse.getAcadYear(),username));
		//m.addAttribute("allCourses",courseService.findByCoursesBasedOnProgramName(progName));
		// String sname = "TestSheet";

		XSSFWorkbook workbook;
		// File file = new File(type);
		FileOutputStream fos = null;

		try {
			File dest = new File(workDir + File.separator
					+ "userCourseEnrollmentTemplate.xlsx");
			String filePath = dest.getAbsolutePath();
			filePath = filePath.replaceAll("\\\\", "/");
			m.addAttribute("filePath", filePath);

			fos = new FileOutputStream(dest);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			logger.error("Exception", e1);
		}
		
		workbook = new XSSFWorkbook();

		for (Long c : courseIds) {

			
			Course course = courseService.findByID(c);
			
				Course acadYearCode = courseService.findByID(course.getId());
			
				logger.info("Course Li-------"+course.getProgramId()+" " +course.getAcadSession());
				
			
			List<String> studentList = userService.findstudentForCoursera(course.getProgramId(),course.getAcadSession());
			
			logger.info("studentList -------------->"+studentList);
			
			logger.info("courseId -------------->"+acadYearCode.getAcadYearCode());
			// XSSFSheet sheet = workbook.createSheet(course.getCourseName());
			XSSFSheet sheet = workbook.createSheet(String.valueOf(course
					.getId()));
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setLocked(true);

			// sheet.createFreezePane(0, 1);
			// sheet.createFreezePane(1, 1, 1, 1);
			// sheet.createFreezePane(0, 0);
			Row headerRow = sheet.createRow(0);
			Cell c1 = headerRow.createCell(0);
			c1.setCellValue("Course ID");
			Cell c2 = headerRow.createCell(1);
			c2.setCellValue("Course Name");
			Cell c3 = headerRow.createCell(2);
			c3.setCellValue("SAPID");
			Cell c4 = headerRow.createCell(3);
			c4.setCellValue("User Role");
			
			Cell c5 = headerRow.createCell(4);
			c5.setCellValue("acadYearCode");
			
			for (int i =0; i <studentList.size(); i++) {
				Row row = sheet.createRow(i+1);
				Cell cell1 = row.createCell(0);
				cell1.setCellValue(c.toString());//courseid
				Cell cell2 = row.createCell(1);
				cell2.setCellValue(course.getCourseName());//coursename
				Cell cell2student = row.createCell(2);
				cell2student.setCellValue(studentList.get(i));
				
				Cell cell3 = row.createCell(3);
				cell3.setCellValue("ROLE_STUDENT");
				Cell cell4 = row.createCell(4);
				if(acadYearCode != null) {
					cell4.setCellValue(acadYearCode.getAcadYearCode());
				}
				else
				{
					cell4.setCellValue("");
				}
				XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(
						sheet);
				String[] actions = new String[] { "ROLE_FACULTY,ROLE_STUDENT" };
				XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper
						.createExplicitListConstraint(actions);
				CellRangeAddressList addressList = new CellRangeAddressList(1,
						100, 3, 3);
				XSSFDataValidation validation = (XSSFDataValidation) dvHelper
						.createValidation(constraint, addressList);
				validation.setShowErrorBox(true);
				sheet.addValidationData(validation);

			}

		}

		try {

			workbook.write(fos);
			fos.flush();
			fos.close();

			
		} catch (Exception e) {
			logger.error("Exception while writing excel", e);
		}

		return "course/uploadUserCourseEnrol";
	}
	
	/*
	 * @RequestMapping(value = "/uploadUserCourse", method = RequestMethod.POST)
	 * public String uploadUserCourse(@ModelAttribute UserCourse userCourse,
	 * 
	 * @RequestParam("file") MultipartFile file, RedirectAttributes
	 * redirectAttrs, Principal principal) {
	 * 
	 * UserCourseExcelHelper userCourseExcelHelper = getUserCourseExcelHelper();
	 * String username = principal.getName(); try { if (!file.isEmpty()) {
	 * userCourse.setCreatedBy(username);
	 * userCourseExcelHelper.initHelper(userCourse);
	 * userCourseExcelHelper.readExcel((MultipartFile) file); }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in uploading File"); return
	 * "course/uploadUserCourse"; } if
	 * (userCourseExcelHelper.getErrorList().isEmpty())
	 * setSuccess(redirectAttrs, "File uploaded successfully"); else
	 * setErrorList(redirectAttrs,
	 * "Errors encountered uploading file: No Records Added",
	 * userCourseExcelHelper.getErrorList()); return
	 * "redirect:/uploadUserCourseForm"; }
	 */

	@Secured({"ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/uploadUserCourse", method = RequestMethod.POST)
	public String uploadUserCourse(@ModelAttribute UserCourse userCourse,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs, Principal principal, Model m) {
		Token userDetails = (Token) principal;
		String progName = userDetails.getProgramName();
		m.addAttribute("allCourses",
				courseService.findByCoursesBasedOnProgramName(progName));
		UserCourseExcelHelper userCourseExcelHelper = getUserCourseExcelHelper();
		String username = principal.getName();
		try {
			if (!file.isEmpty()) {
				userCourse.setCreatedBy(username);
				userCourse.setLastModifiedBy(username);
				userCourse.setOperation("INSERT");
				userCourseExcelHelper.initHelper(userCourse);
				userCourseExcelHelper.readExcel((MultipartFile) file);
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(userCourseExcelHelper
						.getSuccessList());
				
				// logger.info(" json--->" + (json));
				WebTarget webTarget = client.target(URIUtil
						.encodeQuery(userRoleMgmtCrudUrl
								+ "/addOrUpdateUserCourse?json=" + json));
				Invocation.Builder invocationBuilder = webTarget
						.request(MediaType.APPLICATION_JSON);
				String resp = invocationBuilder.get(String.class);
				logger.info("resp" + resp);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in uploading File");
			return "course/uploadUserCourse";
		}
		if (userCourseExcelHelper.getErrorList().isEmpty())
			setSuccess(redirectAttrs, "File uploaded successfully");
		else
			setErrorList(redirectAttrs,
					"Errors encountered uploading file: No Records Added",
					userCourseExcelHelper.getErrorList());
		return "redirect:/uploadUserCourseForm";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/uploadUserCourseEnrol", method = RequestMethod.POST)
	public String uploadUserCourseEnrol(@ModelAttribute UserCourse userCourse,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttrs, Principal principal) {

		Client clientWS = null;
		ClientConfig clientConfig = null;
		WebTarget webTarget = null;
		Invocation.Builder invocationBuilder = null;
		Response response = null;

		UserCourseExcelHelper userCourseExcelHelper = getUserCourseExcelHelper();
		ExcelReader excelReader = new ExcelReader();
		List<String> validateHeader = new ArrayList<String>();
		validateHeader.add("Course ID");
		validateHeader.add("Course Name");
		validateHeader.add("SAPID");
		validateHeader.add("User Role");
		validateHeader.add("acadYearCode");
		String username = principal.getName();
		
		try {

			if (!file.isEmpty()) {
				userCourse.setCreatedBy(username);
				userCourse.setLastModifiedBy(username);
				userCourse.setOperation("INSERT");
				userCourseExcelHelper.initHelper(userCourse);
				List<Map<String, Object>> list = excelReader
						.readExcelFileUsingColumnHeaderForMultipleSheets(file,
								validateHeader);
				
				List<UserCourse> userCourseList = new ArrayList<UserCourse>();

				String resp = null;
				ObjectMapper mapper = new ObjectMapper();
				for (Map<String, Object> map : list) {
					
					UserCourse bean = new UserCourse();
					Boolean isUserPresent = null;
					for (Map.Entry<String, Object> entry : map.entrySet()) {

						Long courseId;
						String uname = null;
						String role;
						Course c;
						if (entry.getKey().equalsIgnoreCase("Course ID")) {
							courseId = Long
									.parseLong((String) entry.getValue());
							bean.setCourseId(courseId);
							c = courseService.findByID(courseId);
							bean.setAcadMonth(userCourse.getAcadMonth());
							bean.setAcadMonth(c.getAcadMonth());
							bean.setAcadYear(Integer.valueOf(c.getAcadYear()));
							bean.setAcadSession(c.getAcadSession());
							bean.setAcadYearCode(c.getAcadYearCode());

						} else if (entry.getKey().equalsIgnoreCase("SAPID")) {
							uname = (String) entry.getValue();
							
							if (userService.findByUserName(uname) == null) {
								
								isUserPresent = false;

								webTarget = client
										.target(URIUtil
												.encodeQuery(userRoleMgmtCrudUrl
														+ "/getUserBeanFromUsermgmt?username="
														+ uname));
								invocationBuilder = webTarget
										.request(MediaType.APPLICATION_JSON);
								resp = invocationBuilder.get(String.class);
								// logger.info("resp from uname" + resp);
								if (resp == null) {
									setError(redirectAttrs, "User " + uname
											+ "is not present!");
								} else {
									User userTo = mapper.readValue(resp,
											User.class);
									UserRole ur = new UserRole();
									userService.insert(userTo);
									for (String s : userTo.getRolesForUser()) {

										ur.setUsername(uname);
										ur.setRole(s);
										
										
										ur.setUser(userTo);
										userRoleService.insert(ur);

									}
									bean.setUsername(uname);

								}

							} else {
								isUserPresent = true;
								bean.setUsername(uname);
							}

						} else if (entry.getKey().equalsIgnoreCase("User Role")) {
							role = (String) entry.getValue();

							bean.setRole(role);
						}
						
						bean.setCreatedBy(username);
						bean.setLastModifiedBy(username);

					}
					
					userCourseList.add(bean);
				}
				userCourseService.insertBatch(userCourseList);
				setSuccess(redirectAttrs, "Successfully added");

				String json = mapper.writeValueAsString(userCourseList);
				
				// logger.info(" json--->" + (json));

				int responseCode;
				clientConfig = new ClientConfig();

				clientWS = ClientBuilder.newClient(clientConfig);
				webTarget = clientWS.target(userRoleMgmtCrudUrl
						+ "/addOrUpdateUserCourse");
				// .target(LMSURL+"/MPSTME-NM-M/createAssignmentFromJson/");

				// set file upload values
				invocationBuilder = webTarget.request();

				response = invocationBuilder.post(Entity.entity(
						json.toString(), MediaType.APPLICATION_JSON));

				/*
				 * response = webTarget.request(MediaType.APPLICATION_JSON).post
				 * (Entity.json(json), Response.class);
				 */

				// get response code
				responseCode = response.getStatus();

				

				// String respInsert = response.readEntity(String

				// resp = invocationBuilder.get(String.class);
				// logger.info("resp" + resp);

			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in uploading File");
			return "redirect:/uploadUserCourseEnrolForm";
		}
		if (userCourseExcelHelper.getErrorList().isEmpty())
			setSuccess(redirectAttrs, "File uploaded successfully");
		else
			setErrorList(redirectAttrs,
					"Errors encountered uploading file: No Records Added",
					userCourseExcelHelper.getErrorList());
		return "redirect:/uploadUserCourseEnrolForm";
	}

	@Secured({"ROLE_USER"})
	@RequestMapping(value = "/viewCourse", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewCourse(@ModelAttribute Course course, Model m,
			Principal principal, HttpSession session) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("courseView", "View Course",
				false, false, true, true, true));
		session.setAttribute("courseId", course.getId());
		Course courseDB = courseService.findByID(course.getId());
		LMSHelper.copyNonNullFields(course, courseDB);
		
		UsernamePasswordAuthenticationToken userDetails = (UsernamePasswordAuthenticationToken) principal;
		try {
			if (userDetails.getAuthorities().contains(Role.ROLE_FACULTY)
					|| userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
				m.addAttribute("receivedMessage",
						dashBoardService.getReceivedMessage(username));
				m.addAttribute("assignments", assignmentService
						.findByFacultyAndCourseActive(username,
								courseDB.getId()));
				if (userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
					m.addAttribute("assignments",
							assignmentService.findByCourse(courseDB.getId()));
					m.addAttribute("tests",
							testService.findByCourse(courseDB.getId()));
					
					m.addAttribute("announcements",
							announcementService.findByCourse(courseDB.getId()));
					m.addAttribute("courseId", courseDB.getId());
					m.addAttribute("courseRecord", courseDB);
					session.setAttribute("courseRecord", courseDB);
					session.setAttribute("courseId", courseDB.getId());
					m.addAttribute("events",
							dashBoardService.getToDoList(username));
					m.addAttribute(
							"mapOfForumIdAndMostRecentActivityDate",
							forumReplyService
									.mapOfForumIdAndMostRecentActivityDate(course
											.getId()));

					List<Forum> forumList = forumService.findByCourse(course
							.getId());
					List<Integer> replyCount = new ArrayList<Integer>();

					for (Forum f : forumList) {
						List<ForumReply> replies = forumReplyService
								.getRepliesFromQuestion(f.getId());
						replyCount.add(replies.size());
						f.setReplyCount(replies.size());
						forumList.set(forumList.indexOf(f), f);

						m.addAttribute("replyCount", replyCount);
						m.addAttribute("allForums", forumList);

						// m.addAttribute("replyCount", replies.size());
					}
					// m.addAttribute("replyCount", replyCount);
					// --------------------

					List<Content> allContent = new ArrayList<Content>();
					List<Content> sharedFolder = null;

					if (course.getId() != null) {
						sharedFolder = contentService
								.getFoldersByCourseForFaculty(
										principal.getName(), course.getId());
					} else {
						sharedFolder = contentService
								.getFoldersForFaculty(principal.getName());
					}

					if (sharedFolder == null || sharedFolder.size() == 0) {
						m.addAttribute("size", 0);
					} else {
						// Prepare Content Table Tree
						for (Content contentFolder : sharedFolder) {
							allContent.add(contentFolder);// Add parent folder
															// in
															// table

						}
					}

					session.setAttribute("announcmentList", dashBoardService
							.listOfAnnouncementsForCourseList(username, null,
									acadSession,
									Long.valueOf(userdetails1.getProgramId())));
					m.addAttribute("allContent", allContent);
					m.addAttribute("toDoDaily", dashBoardService
							.getToDoEveryday(username, Role.ROLE_DEAN.name(),
									courseDB.getId().toString()));
					m.addAttribute("toDoList",
							dashBoardService.getToDoList(username));

					session.setAttribute("courseRecord", courseDB);
					session.setAttribute("courseId", courseDB.getId());
				}
				m.addAttribute(
						"tests",
						testService.findByFacultyAndCourse(username,
								courseDB.getId()));
				m.addAttribute("announcements", announcementService
						.findByUserAndCourse(username, courseDB.getId()));
				m.addAttribute("courseId", courseDB.getId());
				m.addAttribute("courseRecord", courseDB);
				m.addAttribute("events", dashBoardService.getToDoList(username));
				/*
				 * m.addAttribute("allForums",
				 * forumService.findByCourse(course.getId()));
				 */

				m.addAttribute("mapOfForumIdAndMostRecentActivityDate",
						forumReplyService
								.mapOfForumIdAndMostRecentActivityDate(course
										.getId()));

				List<Forum> forumList = forumService.findByCourse(course
						.getId());
				List<Integer> replyCount = new ArrayList<Integer>();

				for (Forum f : forumList) {
					List<ForumReply> replies = forumReplyService
							.getRepliesFromQuestion(f.getId());
					replyCount.add(replies.size());
					f.setReplyCount(replies.size());
					forumList.set(forumList.indexOf(f), f);

					m.addAttribute("replyCount", replyCount);
					m.addAttribute("allForums", forumList);

					// m.addAttribute("replyCount", replies.size());
				}
				// m.addAttribute("replyCount", replyCount);
				// --------------------

				List<Content> allContent = new ArrayList<Content>();
				List<Content> sharedFolder = null;

				if (course.getId() != null) {
					sharedFolder = contentService.getFoldersByCourseForFaculty(
							principal.getName(), course.getId());
				} else {
					sharedFolder = contentService
							.getFoldersForFaculty(principal.getName());
				}

				if (sharedFolder == null || sharedFolder.size() == 0) {
					m.addAttribute("size", 0);
				} else {
					// Prepare Content Table Tree
					for (Content contentFolder : sharedFolder) {
						allContent.add(contentFolder);// Add parent folder in
														// table

					}
				}

				session.setAttribute("announcmentList", dashBoardService
						.listOfAnnouncementsForCourseList(username, null,
								acadSession,
								Long.valueOf(userdetails1.getProgramId())));
				m.addAttribute("allContent", allContent);
				m.addAttribute("toDoDaily", dashBoardService.getToDoEveryday(
						username, Role.ROLE_FACULTY.name(), courseDB.getId()
								.toString()));
				m.addAttribute("toDoList",
						dashBoardService.getToDoList(username));

				session.setAttribute("courseRecord", courseDB);
				session.setAttribute("courseId", courseDB.getId());

				return "course/myCourse/viewStudentCourseMain";

			} else if (userDetails.getAuthorities().contains(Role.ROLE_CORD)) {
				
				m.addAttribute("assignments",
						assignmentService.findByCourse(courseDB.getId()));
				m.addAttribute("tests",
						testService.findByCourse(courseDB.getId()));
				m.addAttribute("announcements",
						announcementService.findByCourse(courseDB.getId()));
				m.addAttribute("courseId", courseDB.getId());
				m.addAttribute("courseRecord", courseDB);
				session.setAttribute("courseRecord", courseDB);
				session.setAttribute("courseId", courseDB.getId());
				return "course/myCourse/viewStudentCourseMain";
			}

			/*
			 * else if (userDetails.getAuthorities().contains(Role.ROLE_DEAN)) {
			 * logger.info("UNDER ROLE CORD "); m.addAttribute("assignments",
			 * assignmentService.findByCourse(courseDB.getId()));
			 * m.addAttribute("tests",
			 * testService.findByCourse(courseDB.getId()));
			 * m.addAttribute("announcements",
			 * announcementService.findByCourse(courseDB.getId()));
			 * m.addAttribute("courseId", courseDB.getId());
			 * m.addAttribute("courseRecord", courseDB);
			 * session.setAttribute("courseRecord", courseDB);
			 * session.setAttribute("courseId", courseDB.getId()); return
			 * "course/myCourse/deanMain"; }
			 */
			else if (userDetails.getAuthorities().contains(Role.ROLE_AR)) {
				
				m.addAttribute("assignments",
						assignmentService.findByCourse(courseDB.getId()));
				m.addAttribute("tests",
						testService.findByCourse(courseDB.getId()));
				m.addAttribute("announcements",
						announcementService.findByCourse(courseDB.getId()));
				m.addAttribute("courseId", courseDB.getId());
				m.addAttribute("courseRecord", courseDB);
				session.setAttribute("courseRecord", courseDB);
				session.setAttribute("courseId", courseDB.getId());

				return "course/myCourse/arMain";
			}

			else if (userDetails.getAuthorities().contains(
					Role.ROLE_AREA_INCHARGE)) {
				
				String courseId = String.valueOf(course.getId());
				m.addAttribute("facultyDropdown",
						userDao.findFacultyByCourse(courseId));
				m.addAttribute("assignments",
						assignmentService.findByCourse(courseDB.getId()));
				m.addAttribute("tests",
						testService.findByCourse(courseDB.getId()));
				m.addAttribute("announcements",
						announcementService.findByCourse(courseDB.getId()));
				m.addAttribute("courseId", courseDB.getId());
				m.addAttribute("courseRecord", courseDB);
				session.setAttribute("courseRecord", courseDB);
				session.setAttribute("courseId", courseDB.getId());
				return "course/myCourse/area_inchargeMain";
			}

			else if (userDetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
				m.addAttribute("receivedMessage",
						dashBoardService.getReceivedMessage(username));
				m.addAttribute("assignments", assignmentService
						.findByUserAndCourse(username, courseDB.getId()));
				m.addAttribute(
						"tests",
						testService.findByUserAndCourse(username,
								courseDB.getId()));
				m.addAttribute("announcements", announcementService
						.findByUserAndCourse(username, courseDB.getId()));
				m.addAttribute("courseId", courseDB.getId());

				m.addAttribute("events", dashBoardService.getToDoList(username));

				/*
				 * m.addAttribute("allForums",
				 * forumService.findByCourse(course.getId()));
				 */

				m.addAttribute("mapOfForumIdAndMostRecentActivityDate",
						forumReplyService
								.mapOfForumIdAndMostRecentActivityDate(course
										.getId()));
				m.addAttribute("toDoDaily", dashBoardService.getToDoEveryday(
						username, Role.ROLE_STUDENT.name(), courseDB.getId()
								.toString()));

				List<Forum> forumList = forumService.findByCourse(course
						.getId());
				List<Integer> replyCount = new ArrayList<Integer>();

				for (Forum f : forumList) {
					List<ForumReply> replies = forumReplyService
							.getRepliesFromQuestion(f.getId());
					replyCount.add(replies.size());
					f.setReplyCount(replies.size());
					forumList.set(forumList.indexOf(f), f);
					// forumList.add(f);

					m.addAttribute("replyCount", replyCount);
					m.addAttribute("allForums", forumList);

					// m.addAttribute("replyCount", replies.size());
				}

				// --------------------

				List<Content> allContent = new ArrayList<Content>();
				List<Content> sharedFolder = null;

				if (course.getId() != null) {
					sharedFolder = contentService.getSharedFoldersByCourse(
							principal.getName(), course.getId());
				} else {
					sharedFolder = contentService.getSharedFolders(principal
							.getName());
				}

				if (sharedFolder == null || sharedFolder.size() == 0) {
					m.addAttribute("size", 0);
				} else {
					// Prepare Content Table Tree
					for (Content contentFolder : sharedFolder) {
						allContent.add(contentFolder);// Add parent folder in
														// table

					}
				}

				session.setAttribute("announcmentList", dashBoardService
						.listOfAnnouncementsForCourseList(username, null,
								acadSession,
								Long.valueOf(userdetails1.getProgramId())));
				m.addAttribute("allContent", allContent);

				m.addAttribute(
						"toDoList",
						calenderService.getAllEventsCourseWise(username,
								String.valueOf(courseDB.getId())));
				// dashBoardService.getToDoList(username));
				session.setAttribute("courseRecord", courseDB);
				session.setAttribute("courseId", courseDB.getId());

				// ----------------------
				// return "course/viewStudentCourse";

				return "course/myCourse/viewStudentCourseMain";
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}

		return "course/myCourse/viewStudentCourseMain";
	}

	@Secured({"ROLE_FACULTY","ROLE_ADMIN","ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/searchUserCourse", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String searchUserCourse(
			@RequestParam(required = false) Integer pageNo, Model m,
			@ModelAttribute UserCourse userCourse, Principal principal) {
		
		m.addAttribute("webPage", new WebPage("courseList",
				"View Course Enrollments", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCourses",
							courseService.findByCoursesBasedOnProgramName(ProgramName));
		try {
			/*Page<UserCourse> page = userCourseService.searchByExactMatch(
					userCourse, pageNo == null || pageNo == 0 ? 1 : pageNo,
					pageSize);*/
			List<String> acadSessionList = userCourseService.findAcadSessionList(userdetails1.getProgramId());
			Page<UserCourse> page = userCourseService.searchUserCourse(userdetails1.getProgramId(),

                    userCourse, pageNo == null || pageNo == 0 ? 1 : pageNo,

                    pageSize);
			
			List<UserCourse> userCourseList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(userCourse));
			m.addAttribute("acadSessionList",acadSessionList);

			if (userCourseList == null || userCourseList.size() == 0) {
				setNote(m, "No Enrollments found");
			}

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(m, "Error in getting userCourseList List");
		}
		return "course/searchUserCourse";
	}

	/**
	 * Supports JSON
	 */
	@RequestMapping(value = "/courseFacultyList", method = RequestMethod.GET)
	public @ResponseBody List<UserCourse> courseFacultyList(
			@ModelAttribute UserCourse userCourse) {

		return userCourseService.findFacultyByCourseID(
				userCourse.getCourseId(), userCourse.getAcadMonth(),
				userCourse.getAcadYear());
	}

	@RequestMapping(value = "/searchCourseFaculty", method = RequestMethod.GET)
	public @ResponseBody List<UserCourse> facultyForAcad(
			@ModelAttribute UserCourse userCourse) {

		return userCourseService.searchByExactMatch(userCourse, 1, 0)
				.getPageItems();
	}

	@RequestMapping(value = "/addKtStudentsForm", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addKtStudentsForm(Model m, Principal principal,
			@ModelAttribute UserCourse userCourse,
			RedirectAttributes redirectAttributes) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignment", "Add KT Students",
				false, false));
		m.addAttribute("allCourses", courseService.findAllActive());
		// m.addAttribute("preFaculties", new ArrayList());
		m.addAttribute("faculties", userService.findAllFaculty());
		m.addAttribute("userCourse", userCourse);

		return "course/viewKtStudents";

	}

	@RequestMapping(value = "/addKtStudents", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String addKtStudents(@RequestParam Long courseId,
			@ModelAttribute UserCourse userCourse, Principal principal, Model m) {
		m.addAttribute("webPage", new WebPage("assignment", "Add KT Students",
				true, false, true, true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u1 = userService.findByUserName(username);
		

		String acadSession = u1.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			Course c = courseService.findByID(courseId);

			List<UserCourse> students = userCourseService
					.getUsersBasedOnCourse(courseId, "ROLE_STUDENT");
			
			for (UserCourse sa : students) {

				Long cId = sa.getCourseId();
				Course course = courseService.findByID(cId);
				String courseName = course.getCourseName();
				
				sa.setCourseName(courseName);
				// students.set(students.indexOf(sa), sa);
				User u = userService.findByUserName(sa.getUsername());
				String firstName = u.getFirstname();
				
				sa.setFirstname(firstName);
				// students.set(students.indexOf(sa), sa);

				String lastName = u.getLastname();
				
				sa.setLastname(lastName);
				students.set(students.indexOf(sa), sa);
				m.addAttribute("courseName", courseName);
				m.addAttribute("firstName", firstName);
				m.addAttribute("lastName", lastName);

			}
			m.addAttribute("students", students);
			m.addAttribute("courseId", courseId);

		} catch (Exception e) {

			logger.error("Exception", e);
			setError(m, "Error in getting Student List");
		}

		m.addAttribute("userCourse", userCourse);
		return "course/addKTStudents";
	}

	@RequestMapping(value = "/saveKtStudentsAllocation", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String saveKtStudentsAllocation(
			@ModelAttribute UserCourse userCourse, Model m,
			Principal principal, RedirectAttributes redirectAttributes) {
		m.addAttribute("webPage", new WebPage("assignment",
				"Allocate KT students", true, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		ArrayList<UserCourse> ktStudentMappingList = new ArrayList<UserCourse>();
		try {
			if (userCourse.getStudents() != null
					&& userCourse.getStudents().size() > 0) {
				Course c = courseService.findByID(userCourse.getCourseId());
				
				
				courseService.makeInActive(c.getId());
				Course newCourse = new Course();
				newCourse.setCourseName(c.getCourseName());
				newCourse.setCreatedBy(username);
				newCourse.setProgramId(c.getProgramId());
				newCourse.setAbbr(c.getAbbr());
				String new_acadMonth = userCourse.getAcadMonth().replace(",",
						"");
				newCourse.setAcadMonth(new_acadMonth);
				newCourse.setAcadYear(String.valueOf(userCourse.getAcadYear()));
				newCourse.setEventName(c.getEventName());
				newCourse.setLastModifiedBy(username);
				courseService.insertWithIdReturn(newCourse);
				
				for (String studentUsername : userCourse.getStudents()) {

					userCourseService.makeInActive(userCourse.getCourseId(),
							studentUsername);

					UserCourse bean = new UserCourse();
					
					bean.setAcadMonth(new_acadMonth);
					bean.setAcadYear(userCourse.getAcadYear());
					bean.setRole(Role.ROLE_STUDENT);

					// bean.setCourseId(userCourse.getCourseId());
					bean.setCourseId(newCourse.getId());
					bean.setUsername(studentUsername);

					bean.setCreatedBy(username);
					bean.setLastModifiedBy(username);

					if (bean.getUsername().equals(studentUsername)) {

						

					}
					ktStudentMappingList.add(bean);

				}

				

				userCourseService.insertBatch(ktStudentMappingList);

				setSuccess(redirectAttributes, "Made "
						+ userCourse.getStudents().size()
						+ " students active successfully");

				

				return "redirect:/addKtStudentsForm";
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in allocating assignment");
			m.addAttribute("webPage", new WebPage("assignment",
					"Create Assignment", false, false));
			return "course/addKTStudents";
		}
		m.addAttribute("userCourse", userCourse);
		return "course/addKTStudents";
	}

	@Secured({ "ROLE_PARENT" })
	@RequestMapping(value = "/knowChildFacultyForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String knowChildFacultyForm(
			Model m,
			@ModelAttribute FacultyDetails facultyDetails,
			Principal principal,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			HttpServletRequest request, RedirectAttributes redirectAttrs) {

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		m.addAttribute("webPage", new WebPage("facultys",
				"View Faculty Details", true, true, true, true, false));

		

		String studentName = userService.findMyChild(username);

		

		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(studentName,
						userdetails1.getProgramName()));
		if (courseId == null || courseId.isEmpty()) {
			if (request.getSession().getAttribute("courseRecord") == null
					|| request.getSession().getAttribute("courseRecord")
							.equals("")) {
				
			} else {
				request.getSession().removeAttribute("courseRecord");
			}

		}

		return "user/knowChildFaculty";
	}

	@Secured({ "ROLE_PARENT" })
	@RequestMapping(value = "/knowChildFaculty", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String knowChildFaculty(
			Model m,
			@ModelAttribute FacultyDetails facultyDetails,
			@RequestParam(name = "courseId", required = false, defaultValue = "") String courseId,
			Principal principal, RedirectAttributes redirectAttrs)

	{

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		String studentName = userService.findMyChild(username);
		m.addAttribute("webPage", new WebPage("facultys",
				"View Faculty Details", true, true, true, true, false));

		

		Long CourseId = Long.valueOf(courseId);
		List<FacultyDetails> facultyList = facultyDetailsService
				.findMyFacultyByCourse(CourseId);
		

		if (facultyList.size() == 0) {
			
			setError(m, "No Faculty profile Information Present ");
		}

		for (FacultyDetails fd : facultyList) {
			facultyDetails.setImagePath(fd.getImagePath());
			facultyDetails.setCourseId(CourseId);
			facultyDetails.setFirstName(fd.getFirstName());
			facultyDetails.setLastName(fd.getLastName());
			facultyDetails.setEmail(fd.getEmail());
			facultyDetails.setDesignation(fd.getDesignation());
			facultyDetails.setDob(fd.getDob());
			facultyDetails.setAge(fd.getAge());
			facultyDetails.setMobile(fd.getMobile());
			facultyDetails.setOverview(fd.getOverview());
			Course course = courseService.findByID(CourseId);
			String courseName = course.getCourseName();
			facultyDetails.setCourseName(courseName);
			String path = fd.getImagePath();

			Integer pathx = path.lastIndexOf("resources");
			String imagePath = path.substring(pathx);
			
			m.addAttribute("imagePath", imagePath);
		}

		m.addAttribute(
				"allCourses",
				courseService.findByUserActive(studentName,
						userdetails1.getProgramName()));
		m.addAttribute("facultyDetails", facultyDetails);
		m.addAttribute("courseId", CourseId);
		m.addAttribute("facultyList", facultyList);
		return "user/knowChildFaculty";
	}

	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/makeInactive", method = { RequestMethod.POST,
			RequestMethod.GET })
	String searchToMakeInactive(@ModelAttribute Test test,
			@ModelAttribute Assignment assignment,
			@ModelAttribute Announcement announcement,
			@ModelAttribute Course course, Model m, Principal principal,
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			@RequestParam(required = false) Long courseId) {
		m.addAttribute("webPage", new WebPage("assignment", "Mark Inactive",
				true, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {
			Page<Course> page;
			Page<Test> page1;
			Page<Assignment> page2;
			Page<Announcement> page3;
			course.setProgramId(Long.parseLong(userdetails1.getProgramId()));

			page = courseService.searchActiveByExactMatch(course, pageNo,
					pageSize);
			page1 = testService
					.searchActiveByExactMatch(test, pageNo, pageSize);
			page2 = assignmentService.searchActiveByExactMatchReplacement(
					Long.parseLong(userdetails1.getProgramId()), pageNo,
					pageSize);
			// searchActiveByExactMatch(assignment,
			// pageNo, pageSize);
			page3 = announcementService.searchActiveByExactMatch(announcement,
					pageNo, pageSize);

			List<Course> courseList = page.getPageItems();
			List<Test> programList = page1.getPageItems();
			List<Assignment> assignmentList = page2.getPageItems();
			List<Announcement> announcementList = page3.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("page1", page1);
			m.addAttribute("page2", page2);
			m.addAttribute("page3", page3);

			m.addAttribute("q", getQueryString(course));
			m.addAttribute("q", getQueryString(test));
			m.addAttribute("q", getQueryString(assignment));
			m.addAttribute("q", getQueryString(announcement));

			if (courseList == null || courseList.size() == 0) {
				setSuccess(m, "No Course found");
			}
			if (programList == null || programList.size() == 0) {
				setSuccess(m, "No Tests found");
			}

			if (assignmentList == null || assignmentList.size() == 0) {
				setSuccess(m, "No Assignment found");
			}

			if (announcementList == null || announcementList.size() == 0) {
				setSuccess(m, "No Announcement found");
			}

		} catch (Exception e) {

		}
		return "user/markInactive";

	}
	
	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/viewUserLoginDetails", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String viewUserLoginDetails(HttpServletRequest request, Model m, Principal principal) {
		
		m.addAttribute("user", new User());
		return "user/viewUserLoginDetails";
	}
	
	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/viewUserDetails", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String viewUserDetails(
			@RequestParam(required = false, defaultValue = "1") int pageNo,
			Principal principal, Model m,
			@ModelAttribute User user){
		
		List<User> u = userService.getUserByUsername(user.getUsername());
		
		if(u.isEmpty()){
			
			m.addAttribute("note", "No User Found");
		}else{
		m.addAttribute("userList", u);
		}
		return "user/viewUserLoginDetails";
	}
	
	
	
	@RequestMapping(value = "/deleteUserCourse", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String deleteUserCourse(RedirectAttributes redirectAttrs, Model m,
			@RequestParam String courseId, @RequestParam String username,
			@RequestParam String role,
			@RequestParam(required = false) String redirectToFeedback,
			@RequestParam(required = false) String feedbackId) {
		m.addAttribute("webPage", new WebPage("deleteUserCourse",
				"Delete UserCourse", false, false, false, true, false));
		ObjectMapper mapper = new ObjectMapper();
		try {
			UserCourse uc = new UserCourse();
			uc.setUsername(username);
			uc.setCourseId(Long.valueOf(courseId));

			String json = mapper.writeValueAsString(uc);

			
			// logger.info(" json--->" + (json));
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/deleteUserCourse?courseId=" + courseId
							+ "&username=" + username));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			// logger.info("resp" + resp);

			StudentFeedback sf = new StudentFeedback();
			if ("ROLE_FACULTY".equals(role)) {
				sf.setFacultyId(username);
			} else {
				sf.setUsername(username);
			}
			sf.setCourseId(uc.getCourseId());
			sf.setActive("N");
			userCourseService.makeInActive(uc.getCourseId(), username);
			studentFeedbackService.makeFacultyCourseInactiveForFeedback(sf,
					role);

			setSuccess(redirectAttrs, "User Disbled for the course");
		} catch (Exception ex) {
			setError(redirectAttrs, "Error in Disabling the course");
			logger.error("Exception", ex);
		}
		if ("Y".equals(redirectToFeedback)) {

			redirectAttrs.addAttribute("feedbackId", feedbackId);
			return "redirect:/searchAllFacultiesForFeedback";
		} else {
			return "redirect:/searchUserCourse";
		}
	}
	
	//29-05-2020
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/getCourseByAcadYearAndProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByAcadYearAndProgram(Principal principal,@RequestParam String acadYear) {
		
		Token userDetails = (Token) principal;
		String progName = userDetails.getProgramName();
		logger.info(principal.getName());
		String username = principal.getName();
		//m.addAttribute("allCourses",courseService.findByCoursesBasedOnProgramName(progName));
		List<Course> allCourses = new ArrayList<>();
		if(!acadYear.isEmpty()) {
			allCourses = courseService.findByCoursesBasedOnYearAndcreatedBy(Integer.valueOf(acadYear),username);
		}
		for(Course c: allCourses) {
			//Changed By akshay 23-06-2021
			Program progDB = programService.findByID(c.getProgramId());
			String programName = progDB.getProgramName();
			int indx = programName.lastIndexOf("-");
			String progCode = programName.substring(indx+1, programName.length());
			String newCourseName = c.getCourseName()+"("+progCode+")";
			c.setCourseName(newCourseName);
			c.setCourseId(String.valueOf(c.getId()));
		}
		String json = new Gson().toJson(allCourses);
		logger.info("json---->"+json);
		return json;
	}
	
	
	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/SerchdeleteUserCourseEnrollmentSupport", method = {RequestMethod.GET, RequestMethod.POST })
	public String SerchdeleteUserCourseEnrollmentSupport(
			@RequestParam(required = false) Integer pageNo, Model m,
			@ModelAttribute UserCourse userCourse, Principal principal) {
		m.addAttribute("webPage", new WebPage("courseList",
				"View Course Enrollments", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ProgramName----------------------------->"+ProgramName);
		String acadSession = u.getAcadSession();
		List<Course> courseList =courseService.findByCoursesBasedOnProgramNameSupportAdmin();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCourses", courseList);
		
		try {

			List<String> acadSessionList = userCourseService.findAcadSessionListSupportAdmin();
			logger.info("acadSessionList----------------------------->"+acadSessionList);

			List<UserCourse> userCourseList = null;
			m.addAttribute("acadSessionList",acadSessionList);
			if (userCourseList == null || userCourseList.size() == 0) {
				setNote(m, "No Enrollments found");
			
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting userCourseList List");
		}
		  return "homepage/deleteUserCourseEnrollmentSupport";
	}
	
	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/deleteUserCourseEnrollmentSupport", method = {RequestMethod.GET, RequestMethod.POST })
	public String deleteUserCourseEnrollmentSupport(
			@RequestParam(required = false) Integer pageNo, Model m,
			@ModelAttribute UserCourse userCourse, Principal principal) {
		m.addAttribute("webPage", new WebPage("courseList",
				"View Course Enrollments", false, false));
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ProgramName----------------------------->"+ProgramName);
		String acadSession = u.getAcadSession();
		List<Course> courseList = courseService.findByCoursesBasedOnProgramNameSupportAdmin();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("allCourses", courseList);
		
		try {

			List<String> acadSessionList = userCourseService.findAcadSessionListSupportAdmin();
			logger.info("acadSessionList----------------------------->"+acadSessionList);
			
			Page<UserCourse> page = userCourseService.searchUserCourse(userdetails1.getProgramId(),

           userCourse, pageNo == null || pageNo == 0 ? 1 : pageNo,

                    pageSize);
			
			List<UserCourse> userCourseList = page.getPageItems();

			m.addAttribute("page", page);
			m.addAttribute("q", getQueryString(userCourse));
			m.addAttribute("acadSessionList",acadSessionList);
		
			if (userCourseList == null || userCourseList.size() == 0) {
				setNote(m, "No Enrollments found");
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(m, "Error in getting userCourseList List");
		}
		  return "homepage/deleteUserCourseEnrollmentSupport";
	}
	
	
	
	
	@Secured({"ROLE_SUPPORT_ADMIN"})
	@RequestMapping(value = "/deleteuserEnrolmentSupportAdmin", method = { RequestMethod.POST })
	public @ResponseBody Object deleteuserEnrolmentSupport(Model m, 
			@RequestParam(name="username")String username, 
			@RequestParam(name="role")String role,
			@RequestParam(name="courseId")String courseId) throws JsonParseException, JsonMappingException, IOException  {
			

		List<String> usernameList = Arrays.asList(username.split(","));
		List<String> roleList = Arrays.asList(role.split(","));
		List<String> courseIdList = Arrays.asList(courseId.split(","));

		logger.info("usernameList------------->"+usernameList);
		logger.info("roleList------------->"+roleList);
		
		

			ObjectMapper mapper = new ObjectMapper();
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/deleteUserCourseSupportAdmin?courseId=" + courseId
							+ "&username=" + username));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);
			String resp = invocationBuilder.get(String.class);
			logger.info("RESPONSE------------->"+resp);

	
			

		int result =	userCourseService.makeInActiveBySupportAdmin(courseIdList, usernameList);
			
		logger.info("result------------->"+result);
		int studentfeedbackInactive = studentFeedbackService.makeMultipleUserCourseInactiveForFeedbackSupportAdmin(usernameList,roleList,courseIdList);
		logger.info("studentfeedbackInactive------------->"+studentfeedbackInactive);

			return resp;
		}
	
	
	
}
