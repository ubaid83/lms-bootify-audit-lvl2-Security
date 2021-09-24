package com.spts.lms.web.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.annotation.Secured;
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
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.CourseOverview;
import com.spts.lms.beans.course.CourseOverview.InstituteSessionType;
import com.spts.lms.beans.course.CourseSyllabus;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.user.User;
import com.spts.lms.beans.user.UserRole;
import com.spts.lms.daos.announcement.AnnouncementDAO;
import com.spts.lms.daos.assignment.AssignmentDAO;
import com.spts.lms.daos.course.CourseDAO;
import com.spts.lms.daos.test.TestDAO;
import com.spts.lms.daos.user.UserCourseDAO;
import com.spts.lms.helpers.PaginationHelper.Page;
import com.spts.lms.helpers.excel.CourseExcelHelper;
import com.spts.lms.helpers.excel.ExcelReader;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.course.CourseOverviewService;
import com.spts.lms.services.course.CourseService;
import com.spts.lms.services.course.CourseSyllabusService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserRoleService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.utils.LMSHelper;
import com.spts.lms.utils.MultipleDBConnection;
import com.spts.lms.web.helper.WebPage;


@Controller
public class CourseController extends BaseController {

	@Autowired
	ApplicationContext act;

	@Autowired
	CourseService courseService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	TestService testService;

	@Autowired
	ProgramService programService;

	@Autowired
	CourseOverviewService courseOverviewService;

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	UserCourseDAO userCourseDAO;

	@Autowired
	CourseSyllabusService courseSyllabusService;

	@Autowired
	CourseDAO courseDao;

	@Autowired
	AssignmentDAO assignmentDAO;

	@Autowired
	TestDAO testDAO;

	@Autowired
	AnnouncementDAO announcementDAO;

	@Autowired
	UserRoleService userRoleService;

	@Value("${acad.years}")
	private String[] acadYears;
	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;
	@Value("#{'${acadSessionList}'.split(',')}")
	private List<String> acadSessionList;

	@Value("${spring.datasource.url}")
	String defaultUrl;

	@Value("${database.username}")
	String defaultUsername;

	@Value("${database.password}")
	String defaultPassword;

	/*
	 * @Value("#{'${userRoleMgmtCrudUrl:http://13.76.166.129:8080/usermgmtcrud}'}" )
	 * String userRoleMgmtCrudUrl;
	 */
	@ModelAttribute("acadYearList")
	public String[] getAcadYearList() {
		return acadYears;
	}

	Client client = ClientBuilder.newClient();

	private static final Logger logger = Logger.getLogger(CourseController.class);

	@RequestMapping(value = "/sessionCourseList", method = RequestMethod.GET)
	public @ResponseBody List<Course> getSessionCourseList(@RequestParam Integer programId,
			@RequestParam Integer sessionId) {
		return courseService.findByProgramIdSessionId(programId, sessionId);
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addCourseForm", method = RequestMethod.GET)
	public String addCourseForm(@RequestParam(required = false) String courseId, Model m, Principal p) {

		m.addAttribute("webPage", new WebPage("addCourse", "Add a new Course", false, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		Course course = new Course();
		if (courseId != null && !courseId.equals("")) {
			course = courseService.findByID(Long.parseLong(courseId));
			m.addAttribute("edit", "true");
		}
		m.addAttribute("course", course);
		m.addAttribute("acadSessionList", acadSessionList);

		return "course/addCourse";
	}

	/*
	 * @RequestMapping(value = "/addCourse", method = RequestMethod.POST) public
	 * String addCourse(@ModelAttribute Course course, RedirectAttributes
	 * redirectAttrs,Principal principal){ String username=principal.getName(); try{
	 * 
	 * course.setCreatedBy(username); course.setLastModifiedBy(username);
	 * 
	 * courseService.insertWithIdReturn(course);
	 * 
	 * setSuccess(redirectAttrs, "Course added successfully");
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(), e);
	 * setError(redirectAttrs,"Error in adding Course"); return
	 * "redirect:/addCourseForm"; } return "redirect:/searchCourse"; }
	 */

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/addCourse", method = RequestMethod.POST)
	public String addCourse(@ModelAttribute Course course, RedirectAttributes redirectAttrs, Principal principal,
			Model m) {
		String username = principal.getName();
		try {

			Token userdetails1 = (Token) principal;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Token userDetails = (Token) principal;
			course.setProgramId(Long.valueOf(userDetails.getProgramId()));
			course.setCreatedBy(username);
			course.setLastModifiedBy(username);
			Program p = programService.findByID(Long.valueOf(userDetails.getProgramId()));
			course.setProperty1(p.getProgramName());
			course.setProperty2(p.getAbbr());
			course.setAbbr(p.getAbbr());
			String courseId = String.valueOf(course.getId());

			course.setId(Long.valueOf(courseId));

			course.setOperation("INSERT");
			courseService.insert(course);
			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(course);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateCourse?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "Course added successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in adding Course");
			return "redirect:/addCourseForm";
		}
		return "redirect:/searchCourse";
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/updateCourse", method = RequestMethod.POST)
	public String updateCourse(@ModelAttribute Course course, RedirectAttributes redirectAttrs, Principal principal,
			Model m) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		try {
			Token userDetails = (Token) principal;

			Course courseFromDb = courseService.findByID(course.getId());
			course.setProperty1(courseFromDb.getProperty1());
			course.setProperty2(courseFromDb.getProperty2());
			course.setAbbr(courseFromDb.getAbbr());

			courseFromDb = LMSHelper.copyNonNullFields(courseFromDb, course);
			courseFromDb.setLastModifiedBy(username);
			courseFromDb.setOperation("UPDATE");
			courseFromDb.setProgramId(Long.valueOf(userDetails.getProgramId()));

			courseService.update(courseFromDb);

			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(courseFromDb);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/addOrUpdateCourse?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			courseFromDb.setLastModifiedBy(username);

			courseService.update(courseFromDb);

			setSuccess(redirectAttrs, "Course saved successfully");

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in saving Course");
			return "redirect:/addCourseForm";
		}
		return "redirect:/searchCourse";
	}

	/*
	 * Reused in searchCourse
	 * 
	 * @RequestMapping(value = "/viewAllCourseOld", method = RequestMethod.GET)
	 * public String viewAllCourse(@RequestParam Integer pageNo, Model m){
	 * 
	 * m.addAttribute("webPage", new WebPage("courseList", "View Courses", false,
	 * false, true, true));
	 * 
	 * try{ Page<Course> page = courseService.findAllActive(pageNo == null || pageNo
	 * == 0 ? 1 : pageNo, pageSize);
	 * 
	 * m.addAttribute("page", page);
	 * 
	 * if(page.getPageItems() == null || page.getPageItems().isEmpty()){ setError(m,
	 * "No Courses found"); }
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(), e); setError(m,
	 * "Error in getting Course List"); } return "courseList"; }
	 */

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/searchCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchCourse(@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false, defaultValue = " ") String courseName, Model m,
			@ModelAttribute Course course, Principal p) {

		m.addAttribute("webPage", new WebPage("searchCourse", "View Courses", true, false));
		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<Course> courseList = new ArrayList<Course>();
		Page<Course> page = new Page<Course>(1);

		// Program Prog = programService.findProgramIdUsingProgramName((ProgramName));

		List<UserCourse> listFaculty = userCourseService.findFacultyByUsingProgramId(userdetails1.getProgramId());
		List<UserCourse> listStudents = userCourseService.findStudentsByUsingProgramId(userdetails1.getProgramId());

		Map<Long, Integer> facultyCountMap = new HashMap<>();
		for (UserCourse uc : listFaculty) {

			facultyCountMap.put(uc.getCourseId(), uc.getNoOfFacultyInCourse());
		}

		Map<Long, Integer> StudentCountMap = new HashMap<>();
		for (UserCourse uc : listStudents) {

			StudentCountMap.put(uc.getCourseId(), uc.getNoOfStudentInCourse());
		}

		m.addAttribute("facultyCountMap", facultyCountMap);
		m.addAttribute("StudentCountMap", StudentCountMap);

		try {
			course.setProgramId(Long.parseLong(userdetails1.getProgramId()));

			if (courseName.equals(" ")) {

				/*
				 * page = courseService.searchActiveByExactMatch(course, pageNo == null ||
				 * pageNo == 0 ? 1 : pageNo, pageSize); courseList = page.getPageItems();
				 */
				courseList = courseService.findByCoursesBasedOnProgramName(ProgramName);

			}

			else {

				courseList = courseService.findByCourseName(courseName);

			}

			m.addAttribute("page", page);
			m.addAttribute("courseList", courseList);
			m.addAttribute("q", getQueryString(course));

			if (courseList == null || courseList.size() == 0) {
				setNote(m, "No Courses found");
			}

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			setError(m, "Error in getting Course List");
		}
		return "course/searchCourse";
	}

	/*
	 * @RequestMapping(value = "/deleteCourse", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String deleteCourse(@RequestParam Integer
	 * courseId, RedirectAttributes redirectAttrs) { try {
	 * courseService.deleteSoftById(String.valueOf(courseId));
	 * setSuccess(redirectAttrs, "Course deleted successfully");
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in deleting Exam Center."); }
	 * 
	 * return "redirect:/searchCourse"; }
	 */

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/deleteCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteCourse(@RequestParam Integer courseId, RedirectAttributes redirectAttrs, Model m, Principal p) {
		try {

			String username = p.getName();

			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Course course = courseService.findByID(Long.valueOf(courseId));
			courseService.deleteSoftById(String.valueOf(courseId));
			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(course);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/deleteCourse?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "Course deleted successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Exam Center.");
		}

		return "redirect:/searchCourse";
	}

	/*
	 * @RequestMapping(value = "/uploadCourseForm", method = { RequestMethod.GET,
	 * RequestMethod.POST }) public String uploadCourseForm(Model m) {
	 * m.addAttribute("webPage", new WebPage("uploadCourse", "Upload Courses",
	 * false, false, true, true, false)); m.addAttribute("course", new Course());
	 * return "course/uploadCourse"; }
	 */

	/*
	 * @RequestMapping(value = "/uploadCourse", method = RequestMethod.POST) public
	 * String uploadCourse(@ModelAttribute Course course,Principal
	 * principal, @RequestParam("file") MultipartFile file, RedirectAttributes
	 * redirectAttrs){
	 * 
	 * CourseExcelHelper courseExcelHelper = getCourseExcelHelper(); String
	 * username=principal.getName(); try{ if(!file.isEmpty()){
	 * course.setCreatedBy(username); courseExcelHelper.initHelper(course);
	 * courseExcelHelper.readExcel((CommonsMultipartFile)file); }
	 * 
	 * }catch(Exception e){ logger.error(e.getMessage(), e);
	 * setError(redirectAttrs,"Error in uploading File: "+e.getMessage()); return
	 * "course/uploadCourseForm"; }
	 * 
	 * if(courseExcelHelper.getErrorList().isEmpty()){ setSuccess(redirectAttrs,
	 * "File uploaded successfully"); return "redirect:/searchCourse"; }else{
	 * setErrorList(redirectAttrs,
	 * "Errors encountered uploading file: No Records Added"
	 * ,courseExcelHelper.getErrorList()); return "redirect:/uploadCourseForm"; }
	 * 
	 * }
	 */

	private CourseExcelHelper getCourseExcelHelper() {
		return (CourseExcelHelper) act.getBean("courseExcelHelper");
	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadCourseForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String uploadCourseForm(Model m, Principal p) {

		String username = p.getName();
		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		logger.info("ACAD SESSION------------------------->" + u.getAcadSession());

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("programList", programService.getPrograms());
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("uploadCourse", "Upload Courses", false, false, true, true, false));
		Course course = new Course();
		m.addAttribute("course", course);
		return "course/uploadCourse";
	}

	/*
	 * @RequestMapping(value = "/uploadCourse", method = RequestMethod.POST) public
	 * String uploadCourse(@ModelAttribute Course course, Principal
	 * principal, @RequestParam("file") MultipartFile file, RedirectAttributes
	 * redirectAttrs) {
	 * 
	 * CourseExcelHelper courseExcelHelper = getCourseExcelHelper(); String username
	 * = principal.getName(); try { if (!file.isEmpty()) {
	 * course.setCreatedBy(username); courseExcelHelper.initHelper(course);
	 * courseExcelHelper.readExcel(file); }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage(), e);
	 * setError(redirectAttrs, "Error in uploading File: " + e.getMessage()); return
	 * "course/uploadCourseForm"; }
	 * 
	 * if (courseExcelHelper.getErrorList().isEmpty()) { setSuccess(redirectAttrs,
	 * "File uploaded successfully"); return "redirect:/searchCourse"; } else {
	 * setErrorList(redirectAttrs,
	 * "Errors encountered uploading file: No Records Added",
	 * courseExcelHelper.getErrorList()); return "redirect:/uploadCourseForm"; }
	 * 
	 * }
	 */

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/uploadCourse", method = RequestMethod.POST)
	public String uploadCourse(@ModelAttribute Course course, Principal principal,
			@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttrs, Model m) {

		Client clientWS = null;
		ClientConfig clientConfig = null;
		WebTarget webTarget = null;
		Invocation.Builder invocationBuilder = null;
		Response response = null;

		CourseExcelHelper courseExcelHelper = getCourseExcelHelper();
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {
			if (!file.isEmpty()) {
				String json = "";
				ObjectMapper mapper = new ObjectMapper();
				ExcelReader excelReader = new ExcelReader();
				if (course.getProgramIds() != null) {
					String progIds = course.getProgramIds();
					List<String> validateHeaders = null;
					validateHeaders = new ArrayList<String>(Arrays.asList("Course Name", "Area/Department",
							"College Abbreviation", "Acad Month", "Acad Year", "Acad Session", "Module Id",
							"Module Name", "Campus Id", "Module Code/abbr", "Department Code", "Department",
							"Module Catogory Code", "Module Catogory Name", "Acad Year Code"));

					List<Map<String, Object>> maps = excelReader.readExcelFileUsingColumnHeader(file, validateHeaders);

					List<Course> courseList = getCourseBeanList(maps, progIds, username);

					logger.info("course List:" + courseList);
					courseService.insertBatch(courseList);
					json = mapper.writeValueAsString(courseList);
				} else {
					Token userDetails = (Token) principal;
					course.setProgramId(Long.valueOf(userDetails.getProgramId()));
					course.setCreatedBy(username);
					courseExcelHelper.initHelper(course);
					courseExcelHelper.readExcel(file);

					if (courseExcelHelper.getErrorList().isEmpty()) {
						json = mapper.writeValueAsString(courseExcelHelper.getSuccessList());

						/*
						 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
						 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
						 * logger.info(" json--->" + (json));
						 */
						/*
						 * WebTarget webTarget = client.target(URIUtil .encodeQuery(userRoleMgmtCrudUrl
						 * + "/addOrUpdateCourseBulk?json=" + json)); Invocation.Builder
						 * invocationBuilder = webTarget .request(MediaType.APPLICATION_JSON); String
						 * resp = invocationBuilder.get(String.class);
						 */

					}
				}
				int responseCode;

				clientConfig = new ClientConfig();

				clientWS = ClientBuilder.newClient(clientConfig);
				webTarget = clientWS.target(userRoleMgmtCrudUrl + "/addOrUpdateCourseBulk");
				// .target(LMSURL+"/MPSTME-NM-M/createAssignmentFromJson/");

				// set file upload values
				invocationBuilder = webTarget.request();

				response = invocationBuilder.post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

				/*
				 * response = webTarget.request(MediaType.APPLICATION_JSON).post
				 * (Entity.json(json), Response.class);
				 */

				// get response code
				responseCode = response.getStatus();

				String resp = response.readEntity(String.class);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in uploading File: " + e.getMessage());
			return "redirect:/uploadCourseForm";
		}

		if (courseExcelHelper.getErrorList().isEmpty()) {
			setSuccess(redirectAttrs, "File uploaded successfully");
			return "redirect:/searchCourse";
		} else {
			setErrorList(redirectAttrs, "Errors encountered uploading file: No Records Added",
					courseExcelHelper.getErrorList());
			return "redirect:/uploadCourseForm";
		}

	}

	public List<Course> getCourseBeanList(List<Map<String, Object>> maps, String progIds, String username) {
		logger.info("maps:" + maps);
		List<Course> courseList = new ArrayList<>();
		if (progIds.contains(",")) {
			List<String> progList = Arrays.asList(progIds.split(","));

			for (Map<String, Object> mapper : maps) {
				String eventId = generateCourseId();
				for (String p : progList) {
					long cId = Long.valueOf(eventId+p);
					Course courseDB = courseService.findByID(cId);
					
					while(courseDB!=null) {
						eventId = generateCourseId();
						cId = Long.valueOf(eventId+p);
						courseDB = courseService.findByID(cId);
					}
					Course courseBean = getCourse(eventId, p, mapper, username, progIds);
					courseList.add(courseBean);

				}
			}

		} else {

			for (Map<String, Object> mapper : maps) {
				String eventId = generateCourseId();
				long cId = Long.valueOf(eventId+progIds);
				Course courseDB = courseService.findByID(cId);
				while(courseDB!=null) {
					eventId = generateCourseId();
					cId = Long.valueOf(eventId+progIds);
					courseDB = courseService.findByID(cId);
				}
				
				Course courseBean = getCourse(eventId, progIds, mapper, username, progIds);
				courseList.add(courseBean);
			}
		}
		return courseList;
	}

	public Course getCourse(String eventId, String p, Map<String, Object> mapper, String username, String progIdStr) {

		Course course = new Course();
		course.setId(Long.valueOf(eventId + p));
		course.setProgramId(Long.valueOf(p));

		String courseName = String.valueOf(mapper.get("Course Name"));
		String acadMonth = String.valueOf(mapper.get("Acad Month"));
		String acadYear = String.valueOf(mapper.get("Acad Year"));
		String acdSession = String.valueOf(mapper.get("Acad Session"));
		String moduleId = String.valueOf(mapper.get("Module Id"));
		String moduleName = String.valueOf(mapper.get("Module Name"));
		String campusId = String.valueOf(mapper.get("Campus Id"));
		String moduleAbbr = String.valueOf(mapper.get("Module Code/abbr"));
		String deptCode = String.valueOf(mapper.get("Department Code"));
		String dept = String.valueOf(mapper.get("Department"));
		String moduleCatCode = String.valueOf(mapper.get("Module Catogory Code"));
		String moduleCatName = String.valueOf(mapper.get("Module Catogory Name"));
		String acadYearCode = String.valueOf(mapper.get("Acad Year Code"));

		if (progIdStr.contains(",")) {
			Program pBean = programService.findByID(Long.valueOf(p));

			int indx = pBean.getProgramName().lastIndexOf("-");
			logger.info("indx:" + indx);
			String pCode = pBean.getProgramName().substring(indx + 1, pBean.getProgramName().length());
			course.setCourseName(courseName + "-" + pCode);
		} else {
			course.setCourseName(courseName);
		}
		course.setAcadYear(acadYear);
		course.setAcadMonth(acadMonth);
		course.setAcadSession(acdSession);
		course.setModuleId(moduleId);
		course.setModuleName(moduleName);
		course.setCreatedBy(username);
		course.setLastModifiedBy(username);

		if (!campusId.isEmpty()) {
			course.setCampusId(Long.valueOf(campusId));
		}
		course.setModuleAbbr(moduleAbbr);
		course.setDeptCode(deptCode);
		course.setDept(dept);
		course.setModuleCategoryCode(moduleCatCode);
		course.setModuleCategoryName(moduleCatName);
		course.setAcadYearCode(acadYearCode);

		return course;
	}

	public String generateCourseId() {
		long timeSeed = System.nanoTime(); // to get the current date
		// time value

		double randSeed = Math.random() * 1000;

		// number
		// generation

		long midSeed = (long) (timeSeed * randSeed); // mixing up the
		// time and
		// rand number.

		// variable timeSeed
		// will be unique

		// variable rand will
		// ensure no relation
		// between the numbers

		String s = midSeed + "";
		String subStr = s.substring(0, 10);

		return subStr;
	}

	@Secured({ "ROLE_ADMIN", "ROLE_LIBRARIAN" })
	@RequestMapping(value = "/addCourseOverviewForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addCourseOverviewForm(CourseOverview courseOverview, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("addCourseOverview", "Add Course Overview", false, false));
		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<InstituteSessionType> instituteSessionType = new ArrayList<InstituteSessionType>(
				Arrays.asList(InstituteSessionType.values()));
		m.addAttribute("allCourses", courseService.findAll());
		m.addAttribute("allPrograms", programService.findAll());
		m.addAttribute("courseOverview", courseOverview);
		m.addAttribute("instituteSessionType", instituteSessionType);
		return "course/addCourseOverview";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_LIBRARIAN" })
	@RequestMapping(value = "/addCourseOverview", method = { RequestMethod.GET, RequestMethod.POST })
	public String addCourseOverview(@ModelAttribute CourseOverview courseOverview, Model m, Principal p) {
		try {
			courseOverview.setId(courseOverview.getCourseId());
			courseOverview.setCreatedBy(p.getName());
			courseOverview.setLastModifiedBy(p.getName());
			courseOverviewService.insert(courseOverview);
			setSuccess(m, "Successfully created Overview for course");
		} catch (Exception e) {
			logger.error("Error addCourseOverview", e);
			setError(m, "Error in  creating Overview for course");
		}
		return addCourseOverviewForm(courseOverview, m, p);
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addSyllabusForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String addSyllabusForm(@RequestParam(required = false) Long courseId, Model m, Principal principal) {
		String username = principal.getName();
		m.addAttribute("webPage", new WebPage("addCourseOverview", "Add Course syllabus", false, false));

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		CourseSyllabus courseSyllabus = new CourseSyllabus();
		courseSyllabus.setCourseId(courseId);

		m.addAttribute("courseName", courseService.findByID(courseId).getCourseName());
		m.addAttribute("courseSyllabus", courseSyllabus);
		return "course/addSyllabus";
	}

	@Secured({ "ROLE_USER" })
	@RequestMapping(value = "/addSyllabus", method = { RequestMethod.GET, RequestMethod.POST })
	public String addSyllabus(@ModelAttribute CourseSyllabus courseSyllabus,
			@RequestParam(required = false) Long courseId, Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("addCourseOverview", "Add Course syllabus", false, false));
		courseSyllabusService.insert(courseSyllabus);
		return "course/addSyllabus";
	}

	
	@RequestMapping(value = "/addDetails", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String addDetails(@RequestParam(required = false) Long id, Model m, Principal principal) {
		m.addAttribute("webPage", new WebPage("addCourseOverview", "Add Course syllabus", false, false));

		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		String json = "";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("details", json);

		return "course/addSyllabus";

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/makeCourseInactive", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView makeCourseInactive(HttpServletRequest request, @ModelAttribute Course course, Model m,
			Principal principal, RedirectAttributes redirectAttrs) {
		String username = principal.getName();

		long courseId = Long.parseLong(StringUtils.trim(request.getParameter("id")));

		courseDao.updateCourseToMakeInactive(courseId);
		userCourseDAO.updateUserCourseToMakeInactive(courseId);

		assignmentDAO.updateInactiveAssignmentByCourseId(courseId);
		testDAO.updateInactiveTestByCourseId(courseId);
		announcementDAO.updateInactiveAnnouncementByCourseId(courseId);

		setSuccess(redirectAttrs, "Course made inactive successfully");
		return new ModelAndView("redirect:/searchCourse");

	}

	@RequestMapping(value = "/getCourseByProgramId", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByProgramId(@RequestParam(name = "programId") String programId,
			Principal principal) {
		String json = "";
		String username = principal.getName();

		List<Course> allCourses = courseService.findByUser(username, Long.parseLong(programId));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course c : allCourses) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(c.getId()), c.getCourseName());
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		/* logger.info("json" + json); */
		return json;

	}

	@Secured({ "ROLE_ADMIN", "ROLE_LIBRARIAN" })
	@RequestMapping(value = "/enterCourseList", method = { RequestMethod.GET, RequestMethod.POST })
	public String enterCourseList(HttpServletRequest request, Model m, Principal principal) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("assignmentList", "Search Courses", false, false));
		m.addAttribute("userCourse", new UserCourse());

		List<UserRole> users = userRoleService.findRoles();
		m.addAttribute("allRoles", users);

		m.addAttribute("preAssigment", new ArrayList());

		/*
		 * Assignment assignments = assignmentService.findByID(id); assignment.setCourse
		 * (courseService.findByID(assignments.getCourseId()));
		 */
		/* m.addAttribute("allAssignments", assignmentService.findAll()); */

		return "course/checkCourseVisibility";
	}

	@Secured({ "ROLE_ADMIN", "ROLE_LIBRARIAN" })
	@RequestMapping(value = "/checkUserCourse", method = { RequestMethod.GET, RequestMethod.POST })
	public String checkUserCourse(@RequestParam(required = false, defaultValue = "1") int pageNo, Principal principal,
			Model m, @ModelAttribute UserCourse userCourse) {
		String username = principal.getName();

		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);

		List<Course> uc = courseService.getUserCourseByUsername(userCourse.getUsername());

		m.addAttribute("userName", userCourse.getUsername());
		if (uc.isEmpty()) {
			m.addAttribute("note", "No User Found");
		} else {
			UserRole ur = userRoleService.findRoleByUsername(userCourse.getUsername());
			m.addAttribute("userRole", ur.getRole());
			m.addAttribute("courseList", uc);
		}
		return "course/checkCourseVisibility";
	}

	@RequestMapping(value = "/getCourseByProgramIdByParam", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getCourseByProgramIdByParam(@RequestParam(name = "programId") String programId,
			@RequestParam(name = "abbr") String abbr, @RequestParam(name = "username") String usernameSql,
			@RequestParam(name = "url") String url, @RequestParam(name = "password", required = false) String password,
			@RequestParam(name = "dbName") String dbName, Principal principal) {
		String json = "";
		String username = principal.getName();
		List<Program> programList = programService.findAll();
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		MultipleDBConnection multipleDBConnection = new MultipleDBConnection();
		DriverManagerDataSource dataSourceDefaultLms = multipleDBConnection.createDefaultConnectionByDS(defaultUrl,
				defaultUsername, defaultPassword);
		try {

			if (!programList.get(0).getAbbr().equals(abbr)) {

				DriverManagerDataSource dataSourceLms = multipleDBConnection.createConnectionByDS(url, usernameSql,
						defaultPassword, dbName);

				courseDao.setDS(dataSourceLms);
				userCourseDAO.setDS(dataSourceLms);

			}
			List<Course> allCourses = courseDao.findByUserByOtherDB(username, Long.parseLong(programId));

			for (Course c : allCourses) {
				Map<String, String> returnMap = new HashMap();
				returnMap.put(String.valueOf(c.getId()), c.getCourseName());
				res.add(returnMap);

			}

			if (!programList.get(0).getAbbr().equals(abbr)) {
				courseDao.setDS(dataSourceDefaultLms);
				userCourseDAO.setDS(dataSourceDefaultLms);
			}
		} catch (Exception ex) {
			courseDao.setDS(dataSourceDefaultLms);
			userCourseDAO.setDS(dataSourceDefaultLms);
			logger.error("Exception", ex);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			courseDao.setDS(dataSourceDefaultLms);
			userCourseDAO.setDS(dataSourceDefaultLms);
			logger.error("Exception", e);
		}

		/* logger.info("json" + json); */
		return json;

	}

	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/deleteCourseAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String deleteCourseAdmin(@RequestParam String courseId, RedirectAttributes redirectAttrs, Model m,
			Principal p) {
		try {

			String username = p.getName();

			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);

			String acadSession = u.getAcadSession();

			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);

			Course course = courseService.findByID(Long.valueOf(courseId));

			courseService.deleteSoftById(courseId);

			userCourseService.makeCourseActive(courseId);

			ObjectMapper mapper = new ObjectMapper();

			String json = mapper.writeValueAsString(course);
			/*
			 * logger.info("encoded json--->" + URIUtil.encodeQuery(json));
			 * logger.info("userRoleMgmtCrudUrl" + userRoleMgmtCrudUrl);
			 * logger.info(" json--->" + (json));
			 */
			WebTarget webTarget = client
					.target(URIUtil.encodeQuery(userRoleMgmtCrudUrl + "/deleteCourseAdmin?json=" + json));

			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			setSuccess(redirectAttrs, "Course deleted successfully");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			setError(redirectAttrs, "Error in deleting Course.");
		}

		return "redirect:/searchCourse";
	}

	@RequestMapping(value = "/getAllCoursesByProgramId", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAllCoursesByProgramId(@RequestParam(name = "programId") String programId,
			Principal principal) {
		String json = "";
		String username = principal.getName();

		List<Course> allCourses = courseService.findCoursesByProgramId(Long.parseLong(programId));

		List<Map<String, String>> res = new ArrayList<Map<String, String>>();

		for (Course c : allCourses) {
			Map<String, String> returnMap = new HashMap();
			returnMap.put(String.valueOf(c.getId()),
					c.getCourseName() + "-" + c.getAcadYear() + "-" + c.getAcadSession());
			res.add(returnMap);
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(res);

		} catch (JsonProcessingException e) {
			logger.error("Exception", e);
		}

		/* logger.info("json" + json); */
		return json;

	}

}
