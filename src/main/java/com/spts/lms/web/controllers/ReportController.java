package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DecimalFormat;
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
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.UsermgmtEvent;
import com.spts.lms.beans.announcement.Announcement;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.MyCourseStudentBean;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.feedback.Feedback;
import com.spts.lms.beans.feedback.FeedbackQuestion;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.beans.forum.Forum;
import com.spts.lms.beans.program.Program;
import com.spts.lms.beans.programCampus.ProgramCampus;
import com.spts.lms.beans.query.Query;
import com.spts.lms.beans.report.DownloadReportLinks;
import com.spts.lms.beans.report.UtilityReport;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.helpers.excel.ExcelCreater;
import com.spts.lms.services.announcement.AnnouncementService;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.content.StudentContentService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.feedback.FeedbackQuestionService;
import com.spts.lms.services.feedback.FeedbackService;
import com.spts.lms.services.feedback.StudentFeedbackResponseService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.forum.ForumService;
import com.spts.lms.services.program.ProgramService;
import com.spts.lms.services.programCampus.ProgramCampusService;
import com.spts.lms.services.query.QueryService;
import com.spts.lms.services.report.DownloadReportLinkService;
import com.spts.lms.services.report.UtilityReportService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.helper.WebPage;
import com.spts.lms.web.utils.Utils;

@Controller
public class ReportController extends BaseController {

	@Autowired
	UserService userService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Autowired
	FeedbackQuestionService feedbackQuestionService;

	@Autowired
	StudentFeedbackResponseService studentFeedbackResponseService;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	FeedbackService feedbackService;

	@Autowired
	ProgramService programService;

	@Autowired
	DownloadReportLinkService downloadReportLinkService;

	@Autowired
	ProgramCampusService programCampusService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	StudentContentService studentContentService;

	@Autowired
	AnnouncementService announcementService;

	@Autowired
	ForumService forumService;

	@Autowired
	QueryService queryService;

	@Autowired
	UtilityReportService utilityReportService;

	@Value("${userMgmtCrudUrl}")
	private String userRoleMgmtCrudUrl;
	private static final Logger logger = Logger
			.getLogger(ReportController.class);

	Client client = ClientBuilder.newClient();

	@Value("${lms.assignment.downloadAllFolder}")
	private String downloadAllFolder;

	@Value("${lms.enrollment.years}")
	private String[] enrollmentYears;

	@Value("${app}")
	private String app;
	
	@Value("${file.base.directory}")
	private String downloadResultPath;

	@ModelAttribute("yearList")
	public String[] getYearList() {
		return enrollmentYears;
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

	private static DecimalFormat decimalFormatter2Digit = new DecimalFormat(
			".##");

	private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
			Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
			Font.BOLD);
	private static Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.NORMAL);
	private static Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
			Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
			Font.BOLD);

	public Map<String, String> getFacultyDepartments() throws URIException {
		WebTarget webTarget = client.target(URIUtil
				.encodeQuery(userRoleMgmtCrudUrl + "/getFacultyDetails"));

		// logger.info("webTarget" + webTarget);
		Invocation.Builder invocationBuilder = webTarget
				.request(MediaType.APPLICATION_JSON);

		String resp = invocationBuilder.get(String.class);
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> mapOfFacultyIdToDepartment = gson.fromJson(resp,
				type);

		return mapOfFacultyIdToDepartment;

	}

	// Newly Added top5 Faculty Feedback with some query changes.
	@Secured({ "ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/downloadTop5FacultyFeedbackNew", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadTop5FacultyFeedbackNew(Model m, Principal p,
			HttpServletResponse response, @RequestParam String acadSession,
			@RequestParam String acadYear,
			@RequestParam(required = false) String campusId, RedirectAttributes r)
			throws URIException {
		String username = p.getName();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Name of Faculty", "SAP ID", "Feedback Score",
				"No.Of Students Given Feedback"));

		List<String> acadSessionList = Arrays.asList(acadSession
				.split("\\s*,\\s*"));
		/*
		 * List<String> term2acadList =
		 * Arrays.asList(term2AcadSession.split("\\s*,\\s*"));
		 */
		Map<String, List<String>> mapOfFacultyIdAndListOfCourseId = new HashMap<>();
		List<Course> term1CourseList = new ArrayList<>();
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){

		if (campusId != null) {
			term1CourseList = courseService
					.findByAcadSessionAndYearAndCampusList(acadSessionList,
							acadYear, campusId);
		} else {
			term1CourseList = courseService.findByAcadSessionAndYear(
					acadSessionList, acadYear);
		}
		List<String> courseT1 = new ArrayList<String>();
		List<UserCourse> facultyT1 = new ArrayList<UserCourse>();

		for (Course co1 : term1CourseList) {
			courseT1.add(co1.getId().toString());

		}

		if (campusId != null) {
			facultyT1 = userCourseService
					.getUserbasedByAcadSessionAndYearAndCampus("ROLE_FACULTY",
							acadSessionList, acadYear, campusId);
		} else {
			facultyT1 = userCourseService.getUserbasedByAcadSessionAndYear(
					"ROLE_FACULTY", acadSessionList, acadYear);
		}
		for (UserCourse uc : facultyT1) {

			List<String> courseIdList = new ArrayList<>();
			if (mapOfFacultyIdAndListOfCourseId.containsKey(uc.getUsername())) {
				courseIdList = mapOfFacultyIdAndListOfCourseId.get(uc
						.getUsername());
			}
			for (String c : courseT1) {

				if (c.equals(String.valueOf(uc.getCourseId()))) {
					courseIdList.add(c);

				}
			}

			mapOfFacultyIdAndListOfCourseId.put(uc.getUsername(), courseIdList);
		}
		/*List<Feedback> ValidFeedbacks = feedbackService
				.findAllFeedbacksByAcadSesionAndYear(acadSessionList, acadYear);*/
		
		List<Feedback> ValidFeedbacks = feedbackService
				.findAllFeedbacksByAcadSesionAndYear(acadSessionList, acadYear, username);

		List<FeedbackQuestion> questionList = new ArrayList<FeedbackQuestion>();
		List<String> questionsIdList = new ArrayList<String>();

		for (Feedback feed : ValidFeedbacks) {
			questionList = feedbackQuestionService.findByFeedbackId(feed
					.getId());

			for (FeedbackQuestion fq1 : questionList) {
				questionsIdList.add(fq1.getId().toString());
			}

			questionList.clear();
		}

		List<String> usernameList = new ArrayList<>();

		for (UserCourse uc1 : facultyT1) {

			if (!usernameList.contains(uc1.getUsername())) {
				usernameList.add(uc1.getUsername());
			}
		}

		/*List<StudentFeedbackResponse> getAvgAnswersByFacultyId = studentFeedbackResponseService
				.getAvgAnswersByFacultyId(courseT1, usernameList,
						questionsIdList);*/
		
		List<StudentFeedbackResponse> getAvgAnswersByFacultyId = studentFeedbackResponseService
				.getAvgAnswersByFacultyId(courseT1, usernameList,
						questionsIdList, username);

		// List<StudentFeedbackResponse> sortedListByTwoValues =
		// sfr.returnSortedList(getAvgAnswersByFacultyId);

		// logger.info("firstValue---->"+sortedListByTwoValues.get(0).getFacultyName()+"last Value-->"+
		// sortedListByTwoValues.get(sortedListByTwoValues.size()-1).getFacultyName());

		List<Map<String, Object>> listOfMap = new ArrayList<>();
		int flag = 0;

		for (int i = 0; i < getAvgAnswersByFacultyId.size(); i++) {

			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			// logger.info("valuuuuuu================="+avgfacrangeT1.get(i));
			flag++;
			mapOfAvg.put("Sr.No", flag);
			mapOfAvg.put("Name of Faculty", getAvgAnswersByFacultyId.get(i)
					.getFacultyName());
			mapOfAvg.put("SAP ID", getAvgAnswersByFacultyId.get(i)
					.getFacultyId());
			mapOfAvg.put(
					"Feedback Score",
					round(Double.valueOf(getAvgAnswersByFacultyId.get(i)
							.getAverage()), 2));
			mapOfAvg.put("No.Of Students Given Feedback",
					getAvgAnswersByFacultyId.get(i).getNoOfStudentCompleted());

			listOfMap.add(mapOfAvg);

		}
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReportTop5Faculty-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReportTop5Faculty"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}

	// ------------------------------------------

	// ====================================================
	@Secured({ "ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/downloadTop5FacultyFeedback", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadTop5FacultyFeedback(Model m, Principal p,
			HttpServletResponse response, @RequestParam String acadSession)
			throws URIException {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Name of Faculty", "Feedback Score"));

		List<String> acadSessionList = Arrays.asList(acadSession
				.split("\\s*,\\s*"));
		/*
		 * List<String> term2acadList =
		 * Arrays.asList(term2AcadSession.split("\\s*,\\s*"));
		 */

		List<Course> term1CourseList = courseService
				.findByAcadSession(acadSessionList);

		List<String> courseT1 = new ArrayList<String>();
		List<UserCourse> facultyT1 = new ArrayList<UserCourse>();

		for (Course co1 : term1CourseList) {
			courseT1.add(co1.getId().toString());

		}

		facultyT1 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT1);

		List<Feedback> ValidFeedbacks = feedbackService.findAllActive();

		List<FeedbackQuestion> questionList = new ArrayList<FeedbackQuestion>();
		List<String> questionsIdList = new ArrayList<String>();

		Map<String, String> mapOfFacultyAvg = new HashMap<String, String>();
		List<String> ListOfAvg = new ArrayList<String>();
		List<String> ListOfFacultyName = new ArrayList<String>();

		for (Feedback feed : ValidFeedbacks) {
			questionList = feedbackQuestionService.findByFeedbackId(feed
					.getId());

			for (FeedbackQuestion fq1 : questionList) {
				questionsIdList.add(fq1.getId().toString());
			}

			questionList.clear();
		}

		for (UserCourse uc1 : facultyT1) {

			String individualfacultyAvg = studentFeedbackResponseService
					.getAvgAnswer(courseT1, uc1.getUsername(), questionsIdList);
			// avgFacultyT1.add(studentFeedbackResponseService.getAvgAnswer(courseT1,
			// uc1.getUsername(), questionsIdList));
			if (individualfacultyAvg != null && !individualfacultyAvg.isEmpty()) {
				mapOfFacultyAvg.put(uc1.getUsername(), individualfacultyAvg);
			}

		}

		Map sortedMap = sortByValues(mapOfFacultyAvg);

		int mapsize = sortedMap.size() - 5;
		int count = 1;
		for (Object entry : sortedMap.entrySet()) {
			// loop codeent
			entry.toString();
			if (count > mapsize) {

				String part[] = entry.toString().split("=");
				if (part[0] != null && part[1] != null && !part[0].isEmpty()
						&& !part[1].isEmpty()) {

					User u = userService.findByUserName(part[0]);

					ListOfFacultyName.add(u.getFirstname() + " "
							+ u.getLastname());

					ListOfAvg.add(String.format("%.2f",
							Double.parseDouble(part[1])));
				}

			}

			count++;

		}

		int flag = 4;
		List<Map<String, Object>> listOfMap = new ArrayList<>();
		for (int i = 0; i < ListOfAvg.size(); i++) {
			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			// logger.info("valuuuuuu================="+avgfacrangeT1.get(i));

			mapOfAvg.put("Sr.No", i + 1);
			mapOfAvg.put("Name of Faculty", ListOfFacultyName.get(flag));
			mapOfAvg.put("Feedback Score", ListOfAvg.get(flag));

			listOfMap.add(mapOfAvg);
			flag--;
		}

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(
			final Map<K, V> map) {
		Comparator<K> valueComparator = new Comparator<K>() {
			public int compare(K k1, K k2) {
				int compare = map.get(k1).compareTo(map.get(k2));
				if (compare == 0)
					return 1;
				else
					return compare;
			}
		};
		Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
		sortedByValues.putAll(map);
		return sortedByValues;
	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/downloadFeedbackReportRangeWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportRangeWise(Model m, Principal p,
			HttpServletResponse response,
			@RequestParam String term1AcadSession,
			@RequestParam String term2AcadSession,
			@RequestParam long courseQuestnCount) throws URIException {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Range", "Term1_faculty", "Term1_course", "Term2_faculty",
				"Term2_course"));

		List<String> term1acadList = Arrays.asList(term1AcadSession
				.split("\\s*,\\s*"));
		List<String> term2acadList = Arrays.asList(term2AcadSession
				.split("\\s*,\\s*"));

		List<Course> term1CourseList = courseService
				.findByAcadSession(term1acadList);
		List<Course> term2CourseList = courseService
				.findByAcadSession(term2acadList);
		List<String> courseT1 = new ArrayList<String>();
		List<String> courseT2 = new ArrayList<String>();
		List<UserCourse> facultyT1 = new ArrayList<UserCourse>();
		List<UserCourse> facultyT2 = new ArrayList<UserCourse>();
		/*
		 * List<String> facultyIdT1 = new ArrayList<String>(); List<String>
		 * facultyIdT2 = new ArrayList<String>();
		 */
		for (Course co1 : term1CourseList) {
			courseT1.add(co1.getId().toString());

		}

		for (Course co2 : term2CourseList) {
			courseT2.add(co2.getId().toString());
		}

		facultyT1 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT1);
		facultyT2 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT2);

		/*
		 * for(UserCourse uc1:userCourseT1){
		 * facultyIdT1.add(uc1.getFacultyId().toString()); }
		 * 
		 * for(UserCourse uc2:userCourseT2){
		 * facultyIdT2.add(uc2.getFacultyId().toString()); }
		 */

		List<Feedback> ValidFeedbacks = feedbackService.findAllActive();

		List<FeedbackQuestion> questionList = new ArrayList<FeedbackQuestion>();
		Map<String, List<FeedbackQuestion>> mapfeedQuestions = new HashMap<String, List<FeedbackQuestion>>();
		List<FeedbackQuestion> facultyquestions = new ArrayList<FeedbackQuestion>();
		List<FeedbackQuestion> coursequestions = new ArrayList<FeedbackQuestion>();
		List<String> facultyquestionsId = new ArrayList<String>();
		List<String> coursequestionsId = new ArrayList<String>();
		List<String> avgFacultyT1 = new ArrayList<String>();
		List<String> avgCourseT1 = new ArrayList<String>();
		List<String> avgFacultyT2 = new ArrayList<String>();
		List<String> avgCourseT2 = new ArrayList<String>();
		for (Feedback feed : ValidFeedbacks) {
			questionList = feedbackQuestionService.findByFeedbackId(feed
					.getId());
			if (courseQuestnCount < questionList.size()) {
				facultyquestions = questionList.subList(0,
						(int) courseQuestnCount);
				for (FeedbackQuestion fq1 : facultyquestions) {
					facultyquestionsId.add(fq1.getId().toString());
				}
				coursequestions = questionList.subList((int) courseQuestnCount,
						questionList.size());
				for (FeedbackQuestion fq2 : coursequestions) {
					coursequestionsId.add(fq2.getId().toString());
				}
				facultyquestions.remove(feed);
				coursequestions.clear();
				questionList.clear();
			}
		}

		if (facultyquestionsId.size() == 0) {
			facultyquestionsId.add("");
		}
		if (coursequestionsId.size() == 0) {
			coursequestionsId.add("");
		}

		for (UserCourse uc1 : facultyT1) {

			avgFacultyT1.add(studentFeedbackResponseService.getAvgAnswer(
					courseT1, uc1.getUsername(), facultyquestionsId));
			avgCourseT1.add(studentFeedbackResponseService.getAvgAnswer(
					courseT1, uc1.getUsername(), coursequestionsId));

		}

		for (UserCourse uc2 : facultyT2) {
			avgFacultyT2.add(studentFeedbackResponseService.getAvgAnswer(
					courseT2, uc2.getUsername(), facultyquestionsId));
			avgCourseT2.add(studentFeedbackResponseService.getAvgAnswer(
					courseT2, uc2.getUsername(), coursequestionsId));
		}

		avgFacultyT1.removeAll(Collections.singleton(null));
		avgCourseT1.removeAll(Collections.singleton(null));
		avgFacultyT2.removeAll(Collections.singleton(null));
		avgCourseT2.removeAll(Collections.singleton(null));

		// logger.info(avgFacultyT1.size()+"facc"+avgCourseT1.size());
		// logger.info(avgFacultyT2.size()+"facc"+avgCourseT2.size());
		List<Integer> avgfacrangeT1 = calculateRange(avgFacultyT1);
		List<Integer> avgcourangeT1 = calculateRange(avgCourseT1);
		List<Integer> avgfacrangeT2 = calculateRange(avgFacultyT2);
		List<Integer> avgcourangeT2 = calculateRange(avgCourseT2);
		List<String> rangeList = new ArrayList<String>();

		rangeList.add("0 to 1");
		rangeList.add("1 to 1.99");
		rangeList.add("2 to 2.99");
		rangeList.add("3 to 3.99");
		rangeList.add("4 to 4.99");
		rangeList.add("5 to 5.99");
		rangeList.add("6 to 7");

		List<Map<String, Object>> listOfMap = new ArrayList<>();
		for (int i = 0; i < avgfacrangeT1.size(); i++) {
			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			// logger.info("valuuuuuu================="+avgfacrangeT1.get(i));
			mapOfAvg.put("Sr.No", i + 1);
			mapOfAvg.put("Range", rangeList.get(i));
			mapOfAvg.put("Term1_faculty", avgfacrangeT1.get(i));
			mapOfAvg.put("Term1_course", avgcourangeT1.get(i));
			mapOfAvg.put("Term2_faculty", avgfacrangeT2.get(i));
			mapOfAvg.put("Term2_course", avgcourangeT2.get(i));
			listOfMap.add(mapOfAvg);

		}

		Map<String, Object> mapOfAvg2 = new HashMap<String, Object>();
		mapOfAvg2.put("Sr.No", "");
		mapOfAvg2.put("Range", "Total");
		mapOfAvg2.put("Term1_faculty",
				avgfacrangeT1.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term1_course",
				avgcourangeT1.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term2_faculty",
				avgfacrangeT2.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term2_course",
				avgcourangeT2.stream().mapToInt(Integer::intValue).sum());
		listOfMap.add(mapOfAvg2);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	public List<Integer> calculateRange(List<String> averageList) {
		int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0, r6 = 0, r7 = 0;
		for (String average : averageList) {
			double avg = Double.parseDouble(average);
			if (avg > 0 && avg < 1.01) {
				r1++;
			} else if (avg > 1 && avg < 2) {
				r2++;
			} else if (avg > 1.99 && avg < 3) {
				r3++;
			} else if (avg > 2.99 && avg < 4) {
				r4++;
			} else if (avg > 3.99 && avg < 5) {
				r5++;
			} else if (avg > 4.99 && avg < 6) {
				r6++;
			} else if (avg > 5.99 && avg < 7.1) {
				r7++;
			}
		}

		List<Integer> result = new ArrayList<Integer>();
		result.add(r1);
		result.add(r2);
		result.add(r3);
		result.add(r4);
		result.add(r5);
		result.add(r6);
		result.add(r7);

		return result;
	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_ADMIN","ROLE_FACULTY"})
	@RequestMapping(value = "/downloadDepartmentFacultyTermWiseFeedback", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadDepartmentFacultyTermWiseFeedback(Model m,
			Principal p, HttpServletResponse response,
			@RequestParam String term1AcadSession,
			@RequestParam String term2AcadSession, RedirectAttributes r) throws URIException {

		String username = p.getName();	
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Name", "Faculty Type", "Department"));

		List<String> term1acadList = Arrays.asList(term1AcadSession
				.split("\\s*,\\s*"));
		List<String> term2acadList = Arrays.asList(term2AcadSession
				.split("\\s*,\\s*"));
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){

		List<Program> programList = programService.findAllActive();

		for (Program prog : programList) {
			headers.add(prog.getProgramName() + ": Term 1");
		}
		headers.add("Term 1 Average");
		for (Program prog : programList) {
			headers.add(prog.getProgramName() + ": Term 2");
		}
		headers.add("Term 2 Average");
		headers.add("Term 1 & Term 2 Average");

		List<String> departmentListTerm1 = courseService
				.findDistinctDepartment(term1acadList);

		// Map<String, Map<String, Map<String, List<String>>>> departmentWiseMap
		// = new HashMap<String, Map<String,Map<String,List<String>>>>();
		List<Map<String, Object>> listOfMap = new ArrayList<>();

		for (String dept : departmentListTerm1) {

			List<User> facultyList = userService.findAllFacultiesByDept(dept);
			// Map<String, Map<String, List<String>>> facultyAvgMap = new
			// HashMap<String, Map<String,List<String>>>();

			int count = 1;
			for (User faculty : facultyList) {
				// Map<String, List<String>> term = new HashMap<String,
				// List<String>>();
				// List<String> programAverage = new ArrayList<String>();
				Map<String, Object> mapOfAvg = new HashMap<String, Object>();
				mapOfAvg.put("Sr.No", count);
				mapOfAvg.put("Name",
						faculty.getFirstname() + " " + faculty.getLastname());
				mapOfAvg.put("Faculty Type",
						"P".equals(faculty.getType()) ? "Full Time"
								: "Visiting");
				mapOfAvg.put("Department", dept);
				Double sum1 = 0.0;
				Double sum2 = 0.0;
				Double term1Avg = 0.0;
				Double term2Avg = 0.0;
				int term1Count = 0;
				int term2Count = 0;
				for (Program prog : programList) {

					List<Course> coursebeanList = courseService
							.findByProgramIdAcadSession(
									prog.getId().toString(), term1acadList);
					List<String> courseList = new ArrayList<String>();
					for (Course c : coursebeanList) {
						courseList.add(c.getId().toString());
					}

					/*
					 * programAverage.add(studentFeedbackResponseService
					 * .getAvgAnswerforActiveFeedbackByFaculty
					 * (courseList,faculty.getUsername()));
					 */
					String facultyFeedbackAvg1 = "";
					if (courseList.size() != 0) {
						/*facultyFeedbackAvg1 = studentFeedbackResponseService
								.getAvgAnswerforActiveFeedbackByFaculty(
										courseList, faculty.getUsername());*/
						
						facultyFeedbackAvg1 = studentFeedbackResponseService
								.getAvgAnswerforActiveFeedbackByFaculty(
										courseList, faculty.getUsername(), username);
					}

					if (facultyFeedbackAvg1 != null
							&& !facultyFeedbackAvg1.isEmpty()) {
						facultyFeedbackAvg1 = decimalFormatter2Digit.format(
								Double.parseDouble(facultyFeedbackAvg1))
								.toString();
						sum1 = sum1 + Double.parseDouble(facultyFeedbackAvg1);
						term1Count++;
					} else {
						facultyFeedbackAvg1 = "-";
					}

					mapOfAvg.put(prog.getProgramName() + ": Term 1",
							facultyFeedbackAvg1);

				}
				if (sum1 != 0 && sum1 != null) {
					term1Avg = sum1 / term1Count;
					mapOfAvg.put("Term 1 Average",
							decimalFormatter2Digit.format(term1Avg));
				} else {
					mapOfAvg.put("Term 1 Average", "-");
				}

				// term.put("Term1", programAverage);
				// programAverage.clear();

				for (Program prog : programList) {

					List<Course> coursebeanList = courseService
							.findByProgramIdAcadSession(
									prog.getId().toString(), term2acadList);
					List<String> courseList = new ArrayList<String>();
					for (Course c : coursebeanList) {
						courseList.add(c.getId().toString());
					}

					/*
					 * programAverage.add(studentFeedbackResponseService
					 * .getAvgAnswerforActiveFeedbackByFaculty
					 * (courseList,faculty.getUsername()));
					 */
					String facultyFeedbackAvg2 = "";
					if (courseList.size() != 0) {
						facultyFeedbackAvg2 = studentFeedbackResponseService
								.getAvgAnswerforActiveFeedbackByFaculty(
										courseList, faculty.getUsername(), username);
					}

					if (facultyFeedbackAvg2 != null
							&& !facultyFeedbackAvg2.isEmpty()) {
						facultyFeedbackAvg2 = decimalFormatter2Digit.format(
								Double.parseDouble(facultyFeedbackAvg2))
								.toString();
						sum2 = sum2 + Double.parseDouble(facultyFeedbackAvg2);
						term2Count++;
					} else {
						facultyFeedbackAvg2 = "-";
					}

					mapOfAvg.put(prog.getProgramName() + ": Term 2",
							facultyFeedbackAvg2);

				}

				if (sum2 != 0 && sum2 != null) {
					term2Avg = sum2 / term2Count;
					mapOfAvg.put("Term 2 Average",
							decimalFormatter2Digit.format(term2Avg));
				} else {
					mapOfAvg.put("Term 2 Average", "-");
				}

				if (term1Avg > 0 && term2Avg > 0) {

					mapOfAvg.put("Term 1 & Term 2 Average",
							decimalFormatter2Digit
									.format(((term1Avg + term2Avg) / 2)));
				} else if (term1Avg > 0) {
					mapOfAvg.put("Term 1 & Term 2 Average", term1Avg);
				} else if (term2Avg > 0) {
					mapOfAvg.put("Term 1 & Term 2 Average", term2Avg);
				} else {
					mapOfAvg.put("Term 1 & Term 2 Average", "-");
				}

				// term.put("Term2", programAverage);
				// programAverage.clear();
				// facultyAvgMap.put(faculty.getUsername(), term);
				listOfMap.add(mapOfAvg);
				count++;
			}

			// departmentWiseMap.put(dept,facultyAvgMap);
		}

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}

	// ----------------------------------
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadDepartmentWiseFeedback", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadDepartmentWiseFeedback(Model m, Principal p,
			HttpServletResponse response, @RequestParam String acadSession, RedirectAttributes r)
			throws URIException {
		String username = p.getName();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Program / Area"));

		List<String> acadSessionList = Arrays.asList(acadSession
				.split("\\s*,\\s*"));
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){

		
		List<String> departmentList = courseService
				.findDistinctDepartment(acadSessionList);

		List<Program> programList = programService.findAllActive();
		List<Course> CourseList = courseService
				.findByAcadSession(acadSessionList);

		Map<String, Map<String, List<String>>> programWiseCourseIdMap = new HashMap<String, Map<String, List<String>>>();
		for (String dept : departmentList) {
			headers.add(dept);
		}
		for (Program pr : programList) {
			Map<String, List<String>> deptWiseCourseIdMap = new HashMap<String, List<String>>();
			for (String dept : departmentList) {
				List<String> courseT1 = new ArrayList<String>();
				if (dept != null && !dept.isEmpty()) {

					for (Course cou : CourseList) {

						if (dept.equals(cou.getDept())
								&& pr.getId().equals(cou.getProgramId())) {
							courseT1.add(cou.getId().toString());

						}

					}
				}

				deptWiseCourseIdMap.put(dept, courseT1);

			}

			if (deptWiseCourseIdMap.size() != 0) {
				programWiseCourseIdMap.put(pr.getId().toString(),
						deptWiseCourseIdMap);
			}

		}

		List<Map<String, Object>> listOfMap = new ArrayList<>();
		int count = 0;
		for (Program pr : programList) {
			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			mapOfAvg.put("Sr.No", ++count);
			mapOfAvg.put("Program / Area", pr.getProgramName());

			if (programWiseCourseIdMap.containsKey(pr.getId().toString())) {

			}

			Map<String, List<String>> deptWiseMap = new HashMap<String, List<String>>();
			deptWiseMap = programWiseCourseIdMap.get(pr.getId().toString());
			for (String dept : departmentList) {
				String individualdepartAvg = "";

				// logger.info("value is ====="+deptWiseMap.toString());
				if (deptWiseMap != null && !deptWiseMap.isEmpty()
						&& deptWiseMap.get(dept).size() > 0) {

					/*individualdepartAvg = studentFeedbackResponseService
							.getAvgAnswerforActiveFeedback(deptWiseMap
									.get(dept));*/
					
					individualdepartAvg = studentFeedbackResponseService
							.getAvgAnswerforActiveFeedback(deptWiseMap
									.get(dept), username);

				}

				if (individualdepartAvg != null
						&& !individualdepartAvg.isEmpty()) {

					mapOfAvg.put(dept, decimalFormatter2Digit.format(Double
							.parseDouble(individualdepartAvg)));

				} else {

					mapOfAvg.put(dept, "-");
				}

			}

			listOfMap.add(mapOfAvg);

		}

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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

	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}

	// ====================================================

	/*@RequestMapping(value = "/downloadFeedbackReportProgramWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportProgramWise(Model m, Principal p,
			HttpServletResponse response,
			@RequestParam(required = false) long courseQuestnCount,
			@RequestParam String acadSession, @RequestParam String programId,
			RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		int count = 0;
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultyType", "CourseName",
				"AcadSession", "Area/DepartmentName", "Year", "Stream",
				"NoOfStudentInTheCourse", "NoOfStudentsAttempted",
				"FacultyScore", "CourseScore", "AverageScore",
				"CommentsByStudents", "Remarks", "ModuleName"));
		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));
		acadSessionList = Arrays.asList(acadSession.split("\\s*,\\s*"));
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){


		//List<StudentFeedback> studentFeedbackListCourseWise = studentFeedbackService
		//		.getstudentFeedbackListCourseWise(programIdList,
		//				acadSessionList);
		List<StudentFeedback> studentFeedbackListCourseWise = studentFeedbackService
				.getstudentFeedbackListCourseWiseNew(programIdList,
						acadSessionList,username);
		Set<String> courseIdList = new HashSet<>();
		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();

		if (mapOfFacultyIdToDepartment.containsKey("32100185")) {

		}
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		
		List<Course> courseListForModule = new ArrayList<>();
        Map<Long, String> mapOfCourseIdAndModule = new HashMap<Long, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(c.getId(), c.getModuleName());
        }
        
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		//List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		//Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		//try {
		//	WebTarget webTarget = client.target(URIUtil
		//			.encodeQuery(userRoleMgmtCrudUrl
		//					+ "/getModuleDetailsOfAllCourse"));
		//	Invocation.Builder invocationBuilder = webTarget
		//			.request(MediaType.APPLICATION_JSON);
//
			//String resp = invocationBuilder.get(String.class);

			//Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			//}.getType();
			//listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
			//		listType);
			//for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
			//	for (String key : map.keySet()) {
			//		mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
			//	}
			//}

		//} catch (Exception ex) {
		//	logger.error("Exception ", ex);
		//}

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}

		for (String courseIds : courseIdList) {

			List<StudentFeedback> studentFeedbackByCourse = mapOfCourseIdAndStudentFeedbackList
					.get(courseIds);

			Set<String> facultyList = new HashSet<>();
			Set<String> feedbackIdList = new HashSet<>();
			int noOfStudentsAttemptedFeedback = 0;
			if (studentFeedbackByCourse.size() > 0) {
				List<FeedbackQuestion> courseFeedbackQuestions = new ArrayList<>();
				List<FeedbackQuestion> facultyFeedbackQuestions = new ArrayList<>();
				List<String> feedbackQuestionIdForCourse = new ArrayList<>();
				List<String> feedbackQuestionIdForFaculty = new ArrayList<>();
				/*List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
				.getFeedbackQuestionListByCourse(courseIds);/
				List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
				.getFeedbackQuestionListByCourseNew(courseIds,username);
				Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
				Set<String> feedbackIds = new HashSet<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					feedbackIds.add(String.valueOf(fq.getFeedbackId()));

				}
				for (String feedbackId : feedbackIds) {
					List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {

						if (feedbackId
								.equals(String.valueOf(fq.getFeedbackId()))) {
							feedbackQuestionListByFeedbackId.add(fq);

						}
					}
					mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId,
							feedbackQuestionListByFeedbackId);
				}
				for (String feedbackId : feedbackIds) {

					List<FeedbackQuestion> feedQuestnList = mapOfFeedbackIdAndFeedbackQuestionList
							.get(feedbackId);

					double courseQuestn = 0;
					double facultyQuestn = 0;

					// long courseQuestnCount = Math.round(courseQuestn);

					long facultyQuestnCount = Math.round(facultyQuestn);
					for (int i = 0; i < feedQuestnList.size(); i++) {
						if (i < courseQuestnCount) {
							courseFeedbackQuestions.add(feedQuestnList.get(i));
							feedbackQuestionIdForCourse.add(String
									.valueOf(feedQuestnList.get(i).getId()));

						} else {
							facultyFeedbackQuestions.add(feedQuestnList.get(i));
							feedbackQuestionIdForFaculty.add(String
									.valueOf(feedQuestnList.get(i).getId()));
						}

					}
				}

				List<String> studentCompleted = new ArrayList<>();
				Map<String, String> mapOfFacultyAndType = new HashMap<>();
				for (StudentFeedback sf : studentFeedbackByCourse) {
					if (!mapOfFacultyAndType.containsKey(sf.getFacultyId())) {
						mapOfFacultyAndType.put(sf.getFacultyId(),
								sf.getTypeOfFaculty());
					}
					if ("Y".equals(sf.getFeedbackCompleted())
							&& !studentCompleted.contains(sf.getUsername())) {
						studentCompleted.add(sf.getUsername());
						noOfStudentsAttemptedFeedback++;

					}
					feedbackIdList.add(String.valueOf(sf.getFeedbackId()));
					facultyList.add(sf.getFacultyId());
				}
				if (facultyList.size() == 1) {
					double facultyScore = 0;
					double courseScore = 0;
					double averageScore = 0;
					Map<String, Object> mapOfFeedbackReport = new HashMap<>();

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (StudentFeedback sf : studentFeedbackByCourse) {

						if (commentMap.get(courseIds) != null) {
							comments.append(commentMap.get(courseIds));

						}
						if (sf.getComments() != null
								&& !"".equals(sf.getComments())) {
							comments.append("//");
							comments.append(sf.getComments());
						}

						commentMap.put(courseIds, comments + "");
						comments.setLength(0);

					}

					if (studentFeedbackByCourse.size() > 0) {
						mapOfFeedbackReport.put("ProgramName",
								studentFeedbackByCourse.get(0)
										.getProgramNameForFeedback());
						mapOfFeedbackReport
								.put("FacultyName", studentFeedbackByCourse
										.get(0).getFacultyName());

						mapOfFeedbackReport.put("FacultyType",
								mapOfFacultyAndType.get(studentFeedbackByCourse
										.get(0).getFacultyId()));
						mapOfFeedbackReport.put("CourseName",
								studentFeedbackByCourse.get(0)
										.getCourseNameforFeedback());
						mapOfFeedbackReport
								.put("AcadSession", studentFeedbackByCourse
										.get(0).getAcadSession());
						mapOfFeedbackReport.put("Area/DepartmentName",
								mapOfFacultyIdToDepartment
										.get(studentFeedbackByCourse.get(0)
												.getFacultyId()));
						mapOfFeedbackReport.put("Year", studentFeedbackByCourse
								.get(0).getAcadYear());
						mapOfFeedbackReport.put("Stream", "NA");
						mapOfFeedbackReport.put("NoOfStudentInTheCourse",
								mapOfCourseIdAndUserCourseList.get(courseIds)
										.size());
						mapOfFeedbackReport.put("NoOfStudentsAttempted",
								noOfStudentsAttemptedFeedback);
						List<Map<String, Object>> courseAverageScore = studentFeedbackResponseService
								.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
										feedbackQuestionIdForCourse, courseIds,
										studentFeedbackByCourse.get(0)
												.getFacultyId());
						List<Map<String, Object>> facultyAverageScore = studentFeedbackResponseService
								.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
										feedbackQuestionIdForFaculty,
										courseIds,
										studentFeedbackByCourse.get(0)
												.getFacultyId());
						if (courseAverageScore.size() > 0) {

							for (Map<String, Object> mapCourse : courseAverageScore) {
								double average = (Double) mapCourse
										.get("average");
								courseScore = courseScore + average;
							}

							for (Map<String, Object> mapCourse : facultyAverageScore) {
								double average = (Double) mapCourse
										.get("average");
								facultyScore = facultyScore + average;
							}

							courseScore = courseScore
									/ courseAverageScore.size();
							facultyScore = facultyScore
									/ facultyAverageScore.size();
							averageScore = ((courseScore + facultyScore) / 2);

						}
						mapOfFeedbackReport.put("FacultyScore", facultyScore);
						mapOfFeedbackReport.put("CourseScore", courseScore);
						mapOfFeedbackReport.put("AverageScore", averageScore);
						mapOfFeedbackReport.put("CommentsByStudents",
								commentMap.get(courseIds));
						mapOfFeedbackReport.put("Remarks", "");
						if (mapOfCourseIdAndModule
								.containsKey(courseIds)) {

							mapOfFeedbackReport.put("ModuleName",
									mapOfCourseIdAndModule
											.get(courseIds));
						} else {

							mapOfFeedbackReport.put("ModuleName", "NA");
						}
						listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
					}
				} else {

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (String facultyId : facultyList) {

						int noOfStudentCompletedFeedback = 0;
						/*List<StudentFeedback> studentFeedabckList = studentFeedbackService
						.findStudentFeedbacksByCourseAndFaculty(
								courseIds, facultyId);/
						List<StudentFeedback> studentFeedabckList = studentFeedbackService
						.findStudentFeedbacksByCourseAndFacultyNew(
								courseIds, facultyId, username);
						for (StudentFeedback sf : studentFeedabckList) {

							if (commentMap.get(courseIds + "" + facultyId) != null) {
								comments.append(commentMap.get(courseIds + ""
										+ facultyId));

							}
							if (sf.getComments() != null
									&& !"".equals(sf.getComments())) {
								comments.append("//");
								comments.append(sf.getComments());
							}

							commentMap.put(courseIds + "" + facultyId, comments
									+ "");
							comments.setLength(0);

							if ("Y".equals(sf.getFeedbackCompleted())) {
								noOfStudentCompletedFeedback++;
							}
						}

						if (studentFeedabckList.size() > 0) {
							double facultyScore = 0;
							double courseScore = 0;
							double averageScore = 0;
							Map<String, Object> mapOfFeedbackReport = new HashMap<>();
							mapOfFeedbackReport.put("ProgramName",
									studentFeedabckList.get(0)
											.getProgramNameForFeedback());
							mapOfFeedbackReport
									.put("FacultyName", studentFeedabckList
											.get(0).getFacultyName());
							mapOfFeedbackReport.put("FacultyType",
									mapOfFacultyAndType.get(facultyId));
							mapOfFeedbackReport.put("CourseName",
									studentFeedbackByCourse.get(0)
											.getCourseNameforFeedback());
							mapOfFeedbackReport.put("AcadSession",
									studentFeedbackByCourse.get(0)
											.getAcadSession());
							mapOfFeedbackReport.put("Area/DepartmentName",
									mapOfFacultyIdToDepartment.get(facultyId));
							mapOfFeedbackReport.put("Year",
									studentFeedbackByCourse.get(0)
											.getAcadYear());
							mapOfFeedbackReport.put("Stream", "NA");
							mapOfFeedbackReport.put(
									"NoOfStudentInTheCourse",
									mapOfCourseIdAndUserCourseList.get(
											courseIds).size());
							mapOfFeedbackReport.put("NoOfStudentsAttempted",
									noOfStudentCompletedFeedback);
							List<Map<String, Object>> courseAverageScore = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
											feedbackQuestionIdForCourse,
											courseIds, facultyId);
							List<Map<String, Object>> facultyAverageScore = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
											feedbackQuestionIdForFaculty,
											courseIds, facultyId);
							if (courseAverageScore.size() > 0) {

								for (Map<String, Object> mapCourse : courseAverageScore) {
									double average = (Double) mapCourse
											.get("average");
									courseScore = courseScore + average;
								}
							}
							if (facultyAverageScore.size() > 0) {
								for (Map<String, Object> mapCourse : facultyAverageScore) {
									double average = (Double) mapCourse
											.get("average");
									facultyScore = facultyScore + average;
								}
							}

							courseScore = courseScore
									/ courseAverageScore.size();
							facultyScore = facultyScore
									/ facultyAverageScore.size();
							averageScore = ((courseScore + facultyScore) / 2);

							mapOfFeedbackReport.put("FacultyScore",
									facultyScore);
							mapOfFeedbackReport.put("CourseScore", courseScore);
							mapOfFeedbackReport.put("AverageScore",
									averageScore);
							mapOfFeedbackReport.put("CommentsByStudents",
									commentMap.get(courseIds + "" + facultyId));
							mapOfFeedbackReport.put("Remarks", "");
							if (mapOfCourseIdAndModule
									.containsKey(courseIds)) {

								mapOfFeedbackReport.put(
										"ModuleName",
										mapOfCourseIdAndModule.get(
												courseIds));
							} else {

								mapOfFeedbackReport.put("ModuleName", "NA");
							}
							listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
						}
					}

				}
			}

		}
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReportProgramWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReportProgramWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMapOfFeedbackReport, headers,
					filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}*/
	
	//amey 14-10-2020
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFeedbackReportProgramWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportProgramWise(Model m, Principal p,
			HttpServletResponse response,
			@RequestParam(required = false) long courseQuestnCount,
			@RequestParam String acadSession, @RequestParam String programId,
			@RequestParam String feedbackType, RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		int count = 0;
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultyType", "CourseName",
				"AcadSession", "Area/DepartmentName", "Year", "Stream",
				"NoOfStudentInTheCourse", "NoOfStudentsAttempted",
				"FacultyScore", "CourseScore", "AverageScore",
				"CommentsByStudents", "Remarks", "ModuleName"));
		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		List<String> acadSessionList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));
		acadSessionList = Arrays.asList(acadSession.split("\\s*,\\s*"));
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedByAndType(username, feedbackType);
        if(feedList.size() > 0){


		/*List<StudentFeedback> studentFeedbackListCourseWise = studentFeedbackService
				.getstudentFeedbackListCourseWise(programIdList,
						acadSessionList);*/
		List<StudentFeedback> studentFeedbackListCourseWise = studentFeedbackService
				.getstudentFeedbackListCourseWiseByFeedbackType(programIdList,
						acadSessionList, username, feedbackType);
		
		if(studentFeedbackListCourseWise.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}
		
		Set<String> courseIdList = new HashSet<>();
		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();

		if (mapOfFacultyIdToDepartment.containsKey("32100185")) {

		}
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}
		if(courseIdList.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		
		List<Course> courseListForModule = new ArrayList<>();
        Map<Long, String> mapOfCourseIdAndModule = new HashMap<Long, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(c.getId(), c.getModuleName());
        }
        
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}*/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}

		for (String courseIds : courseIdList) {

			List<StudentFeedback> studentFeedbackByCourse = mapOfCourseIdAndStudentFeedbackList
					.get(courseIds);

			Set<String> facultyList = new HashSet<>();
			Set<String> feedbackIdList = new HashSet<>();
			int noOfStudentsAttemptedFeedback = 0;
			if (studentFeedbackByCourse.size() > 0) {
				List<FeedbackQuestion> courseFeedbackQuestions = new ArrayList<>();
				List<FeedbackQuestion> facultyFeedbackQuestions = new ArrayList<>();
				List<String> feedbackQuestionIdForCourse = new ArrayList<>();
				List<String> feedbackQuestionIdForFaculty = new ArrayList<>();
				/*List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
				.getFeedbackQuestionListByCourse(courseIds);*/
				List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
				.getFeedbackQuestionListByCourseAndFeedbackType(courseIds,username,feedbackType);
				Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
				Set<String> feedbackIds = new HashSet<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					feedbackIds.add(String.valueOf(fq.getFeedbackId()));

				}
				for (String feedbackId : feedbackIds) {
					List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {

						if (feedbackId
								.equals(String.valueOf(fq.getFeedbackId()))) {
							feedbackQuestionListByFeedbackId.add(fq);

						}
					}
					mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId,
							feedbackQuestionListByFeedbackId);
				}
				for (String feedbackId : feedbackIds) {

					List<FeedbackQuestion> feedQuestnList = mapOfFeedbackIdAndFeedbackQuestionList
							.get(feedbackId);

					double courseQuestn = 0;
					double facultyQuestn = 0;

					// long courseQuestnCount = Math.round(courseQuestn);

					long facultyQuestnCount = Math.round(facultyQuestn);
					for (int i = 0; i < feedQuestnList.size(); i++) {
						if (i < courseQuestnCount) {
							courseFeedbackQuestions.add(feedQuestnList.get(i));
							feedbackQuestionIdForCourse.add(String
									.valueOf(feedQuestnList.get(i).getId()));

						} else {
							facultyFeedbackQuestions.add(feedQuestnList.get(i));
							feedbackQuestionIdForFaculty.add(String
									.valueOf(feedQuestnList.get(i).getId()));
						}

					}
				}

				List<String> studentCompleted = new ArrayList<>();
				Map<String, String> mapOfFacultyAndType = new HashMap<>();
				for (StudentFeedback sf : studentFeedbackByCourse) {
					if (!mapOfFacultyAndType.containsKey(sf.getFacultyId())) {
						mapOfFacultyAndType.put(sf.getFacultyId(),
								sf.getTypeOfFaculty());
					}
					if ("Y".equals(sf.getFeedbackCompleted())
							&& !studentCompleted.contains(sf.getUsername())) {
						studentCompleted.add(sf.getUsername());
						noOfStudentsAttemptedFeedback++;

					}
					feedbackIdList.add(String.valueOf(sf.getFeedbackId()));
					facultyList.add(sf.getFacultyId());
				}
				if (facultyList.size() == 1) {
					double facultyScore = 0;
					double courseScore = 0;
					double averageScore = 0;
					Map<String, Object> mapOfFeedbackReport = new HashMap<>();

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (StudentFeedback sf : studentFeedbackByCourse) {

						if (commentMap.get(courseIds) != null) {
							comments.append(commentMap.get(courseIds));

						}
						if (sf.getComments() != null
								&& !"".equals(sf.getComments())) {
							comments.append("//");
							comments.append(sf.getComments());
						}

						commentMap.put(courseIds, comments + "");
						comments.setLength(0);

					}

					if (studentFeedbackByCourse.size() > 0) {
						mapOfFeedbackReport.put("ProgramName",
								studentFeedbackByCourse.get(0)
										.getProgramNameForFeedback());
						mapOfFeedbackReport
								.put("FacultyName", studentFeedbackByCourse
										.get(0).getFacultyName());

						mapOfFeedbackReport.put("FacultyType",
								mapOfFacultyAndType.get(studentFeedbackByCourse
										.get(0).getFacultyId()));
						mapOfFeedbackReport.put("CourseName",
								studentFeedbackByCourse.get(0)
										.getCourseNameforFeedback());
						mapOfFeedbackReport
								.put("AcadSession", studentFeedbackByCourse
										.get(0).getAcadSession());
						mapOfFeedbackReport.put("Area/DepartmentName",
								mapOfFacultyIdToDepartment
										.get(studentFeedbackByCourse.get(0)
												.getFacultyId()));
						mapOfFeedbackReport.put("Year", studentFeedbackByCourse
								.get(0).getAcadYear());
						mapOfFeedbackReport.put("Stream", "NA");
						mapOfFeedbackReport.put("NoOfStudentInTheCourse",
								mapOfCourseIdAndUserCourseList.get(courseIds)
										.size());
						mapOfFeedbackReport.put("NoOfStudentsAttempted",
								noOfStudentsAttemptedFeedback);
						List<Map<String, Object>> courseAverageScore = studentFeedbackResponseService
								.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
										feedbackQuestionIdForCourse, courseIds,
										studentFeedbackByCourse.get(0)
												.getFacultyId());
						List<Map<String, Object>> facultyAverageScore = studentFeedbackResponseService
								.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
										feedbackQuestionIdForFaculty,
										courseIds,
										studentFeedbackByCourse.get(0)
												.getFacultyId());
						if (courseAverageScore.size() > 0) {

							for (Map<String, Object> mapCourse : courseAverageScore) {
								double average = (Double) mapCourse
										.get("average");
								courseScore = courseScore + average;
							}

							for (Map<String, Object> mapCourse : facultyAverageScore) {
								double average = (Double) mapCourse
										.get("average");
								facultyScore = facultyScore + average;
							}

							courseScore = courseScore
									/ courseAverageScore.size();
							facultyScore = facultyScore
									/ facultyAverageScore.size();
							averageScore = ((courseScore + facultyScore) / 2);

						}
						mapOfFeedbackReport.put("FacultyScore", facultyScore);
						mapOfFeedbackReport.put("CourseScore", courseScore);
						mapOfFeedbackReport.put("AverageScore", averageScore);
						mapOfFeedbackReport.put("CommentsByStudents",
								commentMap.get(courseIds));
						mapOfFeedbackReport.put("Remarks", "");
						if (mapOfCourseIdAndModule
								.containsKey(courseIds)) {

							mapOfFeedbackReport.put("ModuleName",
									mapOfCourseIdAndModule
											.get(courseIds));
						} else {

							mapOfFeedbackReport.put("ModuleName", "NA");
						}
						listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
					}
				} else {

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (String facultyId : facultyList) {

						int noOfStudentCompletedFeedback = 0;
						/*List<StudentFeedback> studentFeedabckList = studentFeedbackService
						.findStudentFeedbacksByCourseAndFaculty(
								courseIds, facultyId);*/
						List<StudentFeedback> studentFeedabckList = studentFeedbackService
						.findStudentFeedbacksByCourseAndFacultyAndFeedbackType(
								courseIds, facultyId, username, feedbackType);
						for (StudentFeedback sf : studentFeedabckList) {

							if (commentMap.get(courseIds + "" + facultyId) != null) {
								comments.append(commentMap.get(courseIds + ""
										+ facultyId));

							}
							if (sf.getComments() != null
									&& !"".equals(sf.getComments())) {
								comments.append("//");
								comments.append(sf.getComments());
							}

							commentMap.put(courseIds + "" + facultyId, comments
									+ "");
							comments.setLength(0);

							if ("Y".equals(sf.getFeedbackCompleted())) {
								noOfStudentCompletedFeedback++;
							}
						}

						if (studentFeedabckList.size() > 0) {
							double facultyScore = 0;
							double courseScore = 0;
							double averageScore = 0;
							Map<String, Object> mapOfFeedbackReport = new HashMap<>();
							mapOfFeedbackReport.put("ProgramName",
									studentFeedabckList.get(0)
											.getProgramNameForFeedback());
							mapOfFeedbackReport
									.put("FacultyName", studentFeedabckList
											.get(0).getFacultyName());
							mapOfFeedbackReport.put("FacultyType",
									mapOfFacultyAndType.get(facultyId));
							mapOfFeedbackReport.put("CourseName",
									studentFeedbackByCourse.get(0)
											.getCourseNameforFeedback());
							mapOfFeedbackReport.put("AcadSession",
									studentFeedbackByCourse.get(0)
											.getAcadSession());
							mapOfFeedbackReport.put("Area/DepartmentName",
									mapOfFacultyIdToDepartment.get(facultyId));
							mapOfFeedbackReport.put("Year",
									studentFeedbackByCourse.get(0)
											.getAcadYear());
							mapOfFeedbackReport.put("Stream", "NA");
							mapOfFeedbackReport.put(
									"NoOfStudentInTheCourse",
									mapOfCourseIdAndUserCourseList.get(
											courseIds).size());
							mapOfFeedbackReport.put("NoOfStudentsAttempted",
									noOfStudentCompletedFeedback);
							List<Map<String, Object>> courseAverageScore = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
											feedbackQuestionIdForCourse,
											courseIds, facultyId);
							List<Map<String, Object>> facultyAverageScore = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
											feedbackQuestionIdForFaculty,
											courseIds, facultyId);
							if (courseAverageScore.size() > 0) {

								for (Map<String, Object> mapCourse : courseAverageScore) {
									double average = (Double) mapCourse
											.get("average");
									courseScore = courseScore + average;
								}
							}
							if (facultyAverageScore.size() > 0) {
								for (Map<String, Object> mapCourse : facultyAverageScore) {
									double average = (Double) mapCourse
											.get("average");
									facultyScore = facultyScore + average;
								}
							}

							courseScore = courseScore
									/ courseAverageScore.size();
							facultyScore = facultyScore
									/ facultyAverageScore.size();
							averageScore = ((courseScore + facultyScore) / 2);

							mapOfFeedbackReport.put("FacultyScore",
									facultyScore);
							mapOfFeedbackReport.put("CourseScore", courseScore);
							mapOfFeedbackReport.put("AverageScore",
									averageScore);
							mapOfFeedbackReport.put("CommentsByStudents",
									commentMap.get(courseIds + "" + facultyId));
							mapOfFeedbackReport.put("Remarks", "");
							if (mapOfCourseIdAndModule
									.containsKey(courseIds)) {

								mapOfFeedbackReport.put(
										"ModuleName",
										mapOfCourseIdAndModule.get(
												courseIds));
							} else {

								mapOfFeedbackReport.put("ModuleName", "NA");
							}
							listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
						}
					}

				}
			}

		}
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReportProgramWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReportProgramWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMapOfFeedbackReport, headers,
					filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}

	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFeedbackReportQWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportQWise(Model m, Principal p,
			HttpServletResponse response, @RequestParam String acadSession,
			@RequestParam long courseQuestnCount, @RequestParam String programId)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 */

		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultyType", "CourseName",
				"AcadSession", "Area/DepartmentName", "Year", "Stream",
				"NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = studentFeedbackService
				.getstudentFeedbackListCourseWiseAndYearProgram(acadSession,
						programIdList);
		List<FeedbackQuestion> findFeedbackQuestionBySemester = feedbackQuestionService
				.findFeedbackQuestionBySemester(acadSession);

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 */

		for (int i = 0; i < findFeedbackQuestionBySemester.size(); i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				headers.add("Faculty:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());
			}

		}
		Long facultyQCount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");

		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnListOfMap(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndUsermgmtEvent, findFeedbackQuestionBySemester,
				courseQuestnCount, username);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQCount);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	public List<Map<String, Object>> returnListOfMap(
			Set<String> courseIdList,
			Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList,
			Map<String, String> mapOfFacultyIdToDepartment,
			Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList,
			Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent,
			List<FeedbackQuestion> findFeedbackQuestionBySemester,
			long countOfCourseQ, String username) {
		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackByCourse = mapOfCourseIdAndStudentFeedbackList
					.get(courseIds);

			Set<String> facultyList = new HashSet<>();
			Set<String> feedbackIdList = new HashSet<>();
			int noOfStudentsAttemptedFeedback = 0;
			if (studentFeedbackByCourse.size() > 0) {
				List<FeedbackQuestion> courseFeedbackQuestions = new ArrayList<>();
				List<FeedbackQuestion> facultyFeedbackQuestions = new ArrayList<>();
				List<String> feedbackQuestionIdForCourse = new ArrayList<>();
				List<String> feedbackQuestionIdForFaculty = new ArrayList<>();
				List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
						.getFeedbackQuestionListByCourse(courseIds, username);
				Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
				Set<String> feedbackIds = new HashSet<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					feedbackIds.add(String.valueOf(fq.getFeedbackId()));

				}
				for (String feedbackId : feedbackIds) {
					List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {

						if (feedbackId
								.equals(String.valueOf(fq.getFeedbackId()))) {
							feedbackQuestionListByFeedbackId.add(fq);

						}
					}
					mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId,
							feedbackQuestionListByFeedbackId);
				}
				for (String feedbackId : feedbackIds) {

					List<FeedbackQuestion> feedQuestnList = mapOfFeedbackIdAndFeedbackQuestionList
							.get(feedbackId);

					double courseQuestn = 0;
					double facultyQuestn = 0;
					courseQuestn = feedQuestnList.size() * 60;
					courseQuestn = courseQuestn / 100;
					// long courseQuestnCount = Math.round(courseQuestn);
					// facultyQuestn = feedQuestnList.size() * 40;
					// facultyQuestn = facultyQuestn / 100;
					// long facultyQuestnCount = Math.round(facultyQuestn);
					for (int i = 0; i < feedQuestnList.size(); i++) {
						if (i < countOfCourseQ) {
							courseFeedbackQuestions.add(feedQuestnList.get(i));
							feedbackQuestionIdForCourse.add(String
									.valueOf(feedQuestnList.get(i).getId()));

						} else {
							facultyFeedbackQuestions.add(feedQuestnList.get(i));
							feedbackQuestionIdForFaculty.add(String
									.valueOf(feedQuestnList.get(i).getId()));
						}

					}
				}

				List<String> studentCompleted = new ArrayList<>();
				Map<String, String> mapOfFacultyAndType = new HashMap<>();
				for (StudentFeedback sf : studentFeedbackByCourse) {
					if (!mapOfFacultyAndType.containsKey(sf.getFacultyId())) {
						mapOfFacultyAndType.put(sf.getFacultyId(),
								sf.getTypeOfFaculty());
					}
					if ("Y".equals(sf.getFeedbackCompleted())
							&& !studentCompleted.contains(sf.getUsername())) {
						studentCompleted.add(sf.getUsername());
						noOfStudentsAttemptedFeedback++;

					}
					feedbackIdList.add(String.valueOf(sf.getFeedbackId()));
					facultyList.add(sf.getFacultyId());
				}
				if (facultyList.size() == 1) {
					double facultyScore = 0;
					double courseScore = 0;
					double averageScore = 0;
					Map<String, Object> mapOfFeedbackReport = new HashMap<>();

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (StudentFeedback sf : studentFeedbackByCourse) {

						if (commentMap.get(courseIds) != null) {
							comments.append(commentMap.get(courseIds));

						}
						if (sf.getComments() != null
								&& !"".equals(sf.getComments())) {
							comments.append("//");
							comments.append(sf.getComments());
						}

						commentMap.put(courseIds, comments + "");
						comments.setLength(0);

					}

					mapOfFeedbackReport.put("ProgramName",
							studentFeedbackByCourse.get(0)
									.getProgramNameForFeedback());
					mapOfFeedbackReport.put("FacultyName",
							studentFeedbackByCourse.get(0).getFacultyName());

					mapOfFeedbackReport
							.put("FacultyType", mapOfFacultyAndType
									.get(studentFeedbackByCourse.get(0)
											.getFacultyId()));
					mapOfFeedbackReport.put("CourseName",
							studentFeedbackByCourse.get(0)
									.getCourseNameforFeedback());
					mapOfFeedbackReport.put("AcadSession",
							studentFeedbackByCourse.get(0).getAcadSession());
					mapOfFeedbackReport.put("Area/DepartmentName",
							mapOfFacultyIdToDepartment
									.get(studentFeedbackByCourse.get(0)
											.getFacultyId()));
					mapOfFeedbackReport.put("Year", studentFeedbackByCourse
							.get(0).getAcadYear());
					mapOfFeedbackReport.put("Stream", "NA");
					mapOfFeedbackReport.put("NoOfStudentInTheCourse",
							mapOfCourseIdAndUserCourseList.get(courseIds)
									.size());
					mapOfFeedbackReport.put("NoOfStudentsAttempted",
							noOfStudentsAttemptedFeedback);
					/*
					 * List<Map<String, Object>> courseAverageScore =
					 * studentFeedbackResponseService
					 * .getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId
					 * ( feedbackQuestionIdForCourse, courseIds);
					 * List<Map<String, Object>> facultyAverageScore =
					 * studentFeedbackResponseService
					 * .getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
					 * feedbackQuestionIdForFaculty, courseIds,
					 * studentFeedbackByCourse.get(0) .getFacultyId()); if
					 * (courseAverageScore.size() > 0) {
					 * 
					 * for (Map<String, Object> mapCourse : courseAverageScore)
					 * { double average = (Double) mapCourse.get("average");
					 * courseScore = courseScore + average; }
					 * 
					 * for (Map<String, Object> mapCourse : facultyAverageScore)
					 * { double average = (Double) mapCourse.get("average");
					 * facultyScore = facultyScore + average; }
					 * 
					 * courseScore = courseScore / courseAverageScore.size();
					 * facultyScore = facultyScore / facultyAverageScore.size();
					 * averageScore = ((courseScore + facultyScore) / 2);
					 * 
					 * } mapOfFeedbackReport.put("FacultyScore", facultyScore);
					 * mapOfFeedbackReport.put("CourseScore", courseScore);
					 * mapOfFeedbackReport.put("AverageScore", averageScore);
					 */
					/*
					 * mapOfFeedbackReport.put("CommentsByStudents",
					 * commentMap.get(courseIds));
					 */
					mapOfFeedbackReport.put("Remarks", "");
					if (mapOfCourseIdAndUsermgmtEvent.containsKey(courseIds)) {

						mapOfFeedbackReport.put("ModuleName",
								mapOfCourseIdAndUsermgmtEvent.get(courseIds)
										.getCourse_name());
					} else {

						mapOfFeedbackReport.put("ModuleName", "NA");
					}
					double courseAverage = 0;
					double facultyAverage = 0;
					double grandAverage = 0;

					for (int i = 0; i < feedbackQuestionListByCourse.size(); i++) {
						if (i < countOfCourseQ) {
							Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
											feedbackQuestionListByCourse.get(i)
													.getId(), courseIds,
											studentFeedbackByCourse.get(0)
													.getFacultyId());
							if (getAverageQuestionWise != null) {
								mapOfFeedbackReport.put("Course:"
										+ feedbackQuestionListByCourse.get(i)
												.getDescription(),
										getAverageQuestionWise.get("average"));
								courseAverage = courseAverage
										+ (Double) getAverageQuestionWise
												.get("average");
							}

						}

					}
					courseAverage = courseAverage / countOfCourseQ;

					mapOfFeedbackReport.put("CourseAverage", courseAverage);

					for (int i = (int) countOfCourseQ; i < feedbackQuestionListByCourse
							.size(); i++) {
						Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
								.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
										feedbackQuestionListByCourse.get(i)
												.getId(), courseIds,
										studentFeedbackByCourse.get(0)
												.getFacultyId());
						if (getAverageQuestionWise != null) {
							mapOfFeedbackReport.put("Faculty:"
									+ feedbackQuestionListByCourse.get(i)
											.getDescription(),
									getAverageQuestionWise.get("average"));
							facultyAverage = facultyAverage
									+ (Double) getAverageQuestionWise
											.get("average");
						}

					}
					facultyAverage = facultyAverage
							/ (findFeedbackQuestionBySemester.size() - (int) countOfCourseQ);
					grandAverage = (courseAverage + facultyAverage) / 2;
					mapOfFeedbackReport.put("FacultyAverage", facultyAverage);
					mapOfFeedbackReport.put("GrandAverage", grandAverage);
					mapOfFeedbackReport.put("CommentsByStudents",
							commentMap.get(courseIds));

					listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
				} else {

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (String facultyId : facultyList) {

						int noOfStudentCompletedFeedback = 0;
						List<StudentFeedback> studentFeedabckList = studentFeedbackService
								.findStudentFeedbacksByCourseAndFaculty(
										courseIds, facultyId);
						for (StudentFeedback sf : studentFeedabckList) {

							if (commentMap.get(courseIds + "" + facultyId) != null) {
								comments.append(commentMap.get(courseIds + ""
										+ facultyId));

							}
							if (sf.getComments() != null
									&& !"".equals(sf.getComments())) {
								comments.append("//");
								comments.append(sf.getComments());
							}

							commentMap.put(courseIds + "" + facultyId, comments
									+ "");
							comments.setLength(0);

							if ("Y".equals(sf.getFeedbackCompleted())) {
								noOfStudentCompletedFeedback++;
							}
						}

						double facultyScore = 0;
						double courseScore = 0;
						double averageScore = 0;
						Map<String, Object> mapOfFeedbackReport = new HashMap<>();
						mapOfFeedbackReport.put("ProgramName",
								studentFeedabckList.get(0)
										.getProgramNameForFeedback());
						mapOfFeedbackReport.put("FacultyName",
								studentFeedabckList.get(0).getFacultyName());
						mapOfFeedbackReport.put("FacultyType",
								mapOfFacultyAndType.get(facultyId));
						mapOfFeedbackReport.put("CourseName",
								studentFeedbackByCourse.get(0)
										.getCourseNameforFeedback());
						mapOfFeedbackReport
								.put("AcadSession", studentFeedbackByCourse
										.get(0).getAcadSession());
						mapOfFeedbackReport.put("Area/DepartmentName",
								mapOfFacultyIdToDepartment.get(facultyId));
						mapOfFeedbackReport.put("Year", studentFeedbackByCourse
								.get(0).getAcadYear());
						mapOfFeedbackReport.put("Stream", "NA");
						mapOfFeedbackReport.put("NoOfStudentInTheCourse",
								mapOfCourseIdAndUserCourseList.get(courseIds)
										.size());
						mapOfFeedbackReport.put("NoOfStudentsAttempted",
								noOfStudentCompletedFeedback);
						/*
						 * List<Map<String, Object>> courseAverageScore =
						 * studentFeedbackResponseService .
						 * getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId
						 * ( feedbackQuestionIdForCourse, courseIds);
						 * List<Map<String, Object>> facultyAverageScore =
						 * studentFeedbackResponseService
						 * .getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
						 * ( feedbackQuestionIdForFaculty, courseIds,
						 * facultyId); if (courseAverageScore.size() > 0) {
						 * 
						 * for (Map<String, Object> mapCourse :
						 * courseAverageScore) { double average = (Double)
						 * mapCourse .get("average"); courseScore = courseScore
						 * + average; } } if (facultyAverageScore.size() > 0) {
						 * for (Map<String, Object> mapCourse :
						 * facultyAverageScore) { double average = (Double)
						 * mapCourse .get("average"); facultyScore =
						 * facultyScore + average; } }
						 * 
						 * courseScore = courseScore /
						 * courseAverageScore.size(); facultyScore =
						 * facultyScore / facultyAverageScore.size();
						 * averageScore = ((courseScore + facultyScore) / 2);
						 * 
						 * mapOfFeedbackReport.put("FacultyScore",
						 * facultyScore); mapOfFeedbackReport.put("CourseScore",
						 * courseScore); mapOfFeedbackReport.put("AverageScore",
						 * averageScore);
						 */
						/*
						 * mapOfFeedbackReport.put("CommentsByStudents",
						 * commentMap.get(courseIds + "" + facultyId));
						 */
						mapOfFeedbackReport.put("Remarks", "");
						if (mapOfCourseIdAndUsermgmtEvent
								.containsKey(courseIds)) {

							mapOfFeedbackReport.put("ModuleName",
									mapOfCourseIdAndUsermgmtEvent
											.get(courseIds).getCourse_name());
						} else {

							mapOfFeedbackReport.put("ModuleName", "NA");
						}

						double courseAverage = 0;
						double facultyAverage = 0;
						double grandAverage = 0;

						for (int i = 0; i < feedbackQuestionListByCourse.size(); i++) {
							if (i < countOfCourseQ) {
								Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
										.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
												feedbackQuestionListByCourse
														.get(i).getId(),
												courseIds, facultyId);
								if (getAverageQuestionWise != null) {
									mapOfFeedbackReport
											.put("Course:"
													+ feedbackQuestionListByCourse
															.get(i)
															.getDescription(),
													getAverageQuestionWise
															.get("average"));
									courseAverage = courseAverage
											+ (Double) getAverageQuestionWise
													.get("average");
								}

							}

						}
						courseAverage = courseAverage / countOfCourseQ;

						mapOfFeedbackReport.put("CourseAverage", courseAverage);

						for (int i = (int) countOfCourseQ; i < feedbackQuestionListByCourse
								.size(); i++) {
							Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionIdAndCourseId(
											feedbackQuestionListByCourse.get(i)
													.getId(), courseIds,
											facultyId);
							if (getAverageQuestionWise != null) {
								mapOfFeedbackReport.put("Faculty:"
										+ feedbackQuestionListByCourse.get(i)
												.getDescription(),
										getAverageQuestionWise.get("average"));
								facultyAverage = facultyAverage
										+ (Double) getAverageQuestionWise
												.get("average");
							}
						}
						facultyAverage = facultyAverage
								/ (findFeedbackQuestionBySemester.size() - (int) countOfCourseQ);
						grandAverage = (courseAverage + facultyAverage) / 2;

						mapOfFeedbackReport.put("FacultyAverage",
								facultyAverage);
						mapOfFeedbackReport.put("GrandAverage", grandAverage);
						mapOfFeedbackReport.put("CommentsByStudents",
								commentMap.get(courseIds + "" + facultyId));

						listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
					}

				}
			}
		}
		return listOfMapOfFeedbackReport;
	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/GetProgramsFromAcadSession", method = { RequestMethod.GET })
	public @ResponseBody String GetProgramsFromAcadSession(
			@RequestParam String acadSession, Principal principal) {
		String username = principal.getName();
		Token userdetails1 = (Token) principal;

		List<String> acadSessionList = Arrays.asList(acadSession
				.split("\\s*,\\s*"));
		List<Program> programList = new ArrayList<Program>();

		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			programList = programService
					.findAllProgramsWithAcadSessionForFaculty(acadSessionList,
							username);

		} else {
			programList = programService
					.findAllProgramsWithAcadSession(acadSessionList);
		}

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
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadReportMyCourseStudentForm", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadReportMyCourseStudentForm(Model m, Principal principal) {

		String username = principal.getName();
		Token userdetails1 = (Token) principal;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		List<DownloadReportLinks> downloadReportLinkList = new ArrayList<>();

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			downloadReportLinkList = downloadReportLinkService
					.findAllForAdmin();
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			downloadReportLinkList = downloadReportLinkService
					.findAllForFaculty();
		}
		m.addAttribute("downloadReportLinkList", downloadReportLinkList);
		List<String> acadSessionList = new ArrayList<String>();

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			downloadReportLinkList = downloadReportLinkService
					.findAllForAdmin();
			acadSessionList = courseService.getAcadSessionForActiveFeedback();
		}
		if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
			downloadReportLinkList = downloadReportLinkService
					.findAllForFaculty();
			acadSessionList = courseService
					.getAcadSessionForActiveFeedbackForFaculty(username);
		}
		m.addAttribute("acadSessionList", acadSessionList);

		if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
			m.addAttribute("allCourses",
					courseService.findByAdminActive());
		}
		
		m.addAttribute("app", app);
		m.addAttribute("username", username);
        if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN) || userdetails1.getAuthorities().contains(Role.ROLE_EXAM)) {
        	return "report/downLoadReportLinkAdmin";
        }
		return "report/downLoadReportLink";
	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadReportMyCourseStudent", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void downloadReportMyCourseStudent(Model m, Principal p,
			HttpServletResponse response) {
		Token userDetails = (Token) p;
		ObjectMapper mapper = new ObjectMapper();
		String programId = userDetails.getProgramId();
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		try {
			String json = mapper.writeValueAsString(programId);
			String jsonUname = mapper.writeValueAsString(username);
			List<Map<String, Object>> listOfMaps = new ArrayList<>();
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getMyCourseStudentByFacultyCourse?json="
							+ jsonUname));

			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);
			/*
			 * logger.info("webTarget" + webTarget); logger.info("resp -----" +
			 * resp);
			 */

			List<MyCourseStudentBean> myCourseStudentBeanList = new ArrayList<MyCourseStudentBean>();
			myCourseStudentBeanList = mapper.readValue(resp,
					new TypeReference<List<MyCourseStudentBean>>() {
					});

			/*
			 * List<String> headers = new
			 * ArrayList<String>(Arrays.asList("sapId", "fullName",
			 * "courseName", "acadYear", "acadMonth", "acadSession"));
			 */
			List<String> headers = new ArrayList<String>(Arrays.asList("sapId",
					"rollNo", "mobile", "email", "fullName", "courseName",
					"acadYear", "acadMonth", "acadSession"));

			for (MyCourseStudentBean myCourseStudentBean : myCourseStudentBeanList) {
				Map<String, Object> mapOfStudents = new HashMap<>();
				mapOfStudents.put("sapId", myCourseStudentBean.getSapId());
				mapOfStudents
						.put("fullName", myCourseStudentBean.getFullName());
				mapOfStudents.put("courseName",
						myCourseStudentBean.getCourseName());
				mapOfStudents
						.put("acadYear", myCourseStudentBean.getAcadYear());
				mapOfStudents.put("acadMonth",
						myCourseStudentBean.getAcadMonth());
				mapOfStudents.put("acadSession",
						myCourseStudentBean.getAcadSession());
				mapOfStudents.put("rollNo", myCourseStudentBean.getRollNo());
				mapOfStudents.put("mobile", myCourseStudentBean.getMobile());
				mapOfStudents.put("email", myCourseStudentBean.getEmail());
				listOfMaps.add(mapOfStudents);
			}
			ExcelCreater excelCreater = new ExcelCreater();
			InputStream is = null;
			try {
				String fileName = "MyCourseStudent"
						+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss",
								Utils.getInIST()) + ".xlsx";
				String filePath = downloadAllFolder
						+ File.separator
						+ "MyCourseStudent"
						+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss",
								Utils.getInIST()) + ".xlsx";
				excelCreater.CreateExcelFile(listOfMaps, headers, filePath);
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + fileName);
				// copy it to response's OutputStream
				is = new FileInputStream(filePath);
				org.apache.commons.io.IOUtils.copy(is,
						response.getOutputStream());
				response.flushBuffer();

			} catch (Exception ex) {
				logger.error("Exception", ex);
			} finally {
				if (is != null) {
					IOUtils.closeQuietly(is);
				}

			}

		} catch (Exception ex) {
			logger.error("Exception", ex);
		}

	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFeedbackReportAcadSessionWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void downloadFeedbackReportAcadSessionWise(Model m, Principal p,
			HttpServletResponse response) {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();

		List<String> headers = new ArrayList<String>(Arrays.asList(
				"Acad Session", "Faculty Name", "Course Name",
				"Area / Department Name", "Year", "Stream",
				"No.of students in the course", "No.of students attempted",
				"Course Score", "Faculty Score", "Comments by students",
				"Remarks by HOD/Dean if any"));

		List<UserCourse> acadSessionCourseList = userCourseService
				.findAllAcadSessionCourse();
		List<Long> CourseIdList = new ArrayList<Long>();
		List<Map<String, Object>> map = new ArrayList<>();
		Map<Long, Object> acadSessionListMap = new HashMap<Long, Object>();
		Map<Long, String> courseAverageMap = new HashMap<Long, String>();
		Map<Long, String> facultyAverageMap = new HashMap<Long, String>();
		Map<String, Integer> noOfStudentFeedbackMap = new HashMap<String, Integer>();
		Map<String, Integer> noOfStudentInCourseMap = new HashMap<String, Integer>();
		int QuestionPartition = 0;

		List<StudentFeedback> noOfFeedbacksAllocated = new ArrayList<StudentFeedback>();
		Map<Long, List<StudentFeedback>> mapOfFeedbacksincourseId = new HashMap<Long, List<StudentFeedback>>();

		List<Long> feedbackQuestionsList = new ArrayList<Long>();

		List<Long> feedbackQuestionsListForCourse = new ArrayList<Long>();

		List<Long> feedbackQuestionsListForFaculty = new ArrayList<Long>();

		List<StudentFeedbackResponse> feedbackAverageListForCourse = new ArrayList<StudentFeedbackResponse>();
		List<StudentFeedbackResponse> feedbackAverageListForFaculty = new ArrayList<StudentFeedbackResponse>();

		Map<Long, List<FeedbackQuestion>> mapOfCourseQuestion = new HashMap<Long, List<FeedbackQuestion>>();

		List<StudentFeedback> noofStudentsWhoTookFeedbackList = new ArrayList<StudentFeedback>();

		for (UserCourse u : acadSessionCourseList) {

			CourseIdList.add(u.getCourseId());

			noOfFeedbacksAllocated = studentFeedbackService
					.findFeedbackAllocatedToCourse(u.getCourseId());
			mapOfFeedbacksincourseId.put(u.getCourseId(),
					noOfFeedbacksAllocated);

			if (noOfFeedbacksAllocated.size() > 0) {

				for (StudentFeedback feedAllocated : noOfFeedbacksAllocated) {

					feedbackQuestionsList = feedbackQuestionService
							.findQuestionByFeedbackId(feedAllocated
									.getFeedbackId());
					QuestionPartition = (int) Math.round(feedbackQuestionsList
							.size() * 0.6);
					feedbackQuestionsListForCourse.addAll(feedbackQuestionsList
							.subList(0, QuestionPartition));
					feedbackQuestionsListForFaculty
							.addAll(feedbackQuestionsList.subList(
									QuestionPartition,
									feedbackQuestionsList.size()));
					feedbackQuestionsList.clear();

				}

				feedbackAverageListForCourse = studentFeedbackResponseService
						.findAverageForFeedback(u.getCourseId(),
								feedbackQuestionsListForCourse);
				feedbackAverageListForFaculty = studentFeedbackResponseService
						.findAverageForFeedback(u.getCourseId(),
								feedbackQuestionsListForFaculty);
			}

			if (feedbackAverageListForCourse.size() > 0) {
				if (feedbackAverageListForCourse.get(0).getCourseAverage() != null) {
					courseAverageMap.put(u.getCourseId(), String
							.valueOf(feedbackAverageListForCourse.get(0)
									.getCourseAverage()));
				} else {
					courseAverageMap.put(u.getCourseId(), "0");
				}
			}

			if (feedbackAverageListForFaculty.size() > 0) {
				if (feedbackAverageListForFaculty.get(0).getCourseAverage() != null) {
					facultyAverageMap.put(u.getCourseId(), String
							.valueOf(feedbackAverageListForFaculty.get(0)
									.getCourseAverage()));
				} else {
					facultyAverageMap.put(u.getCourseId(), "0");
				}

			}

			noofStudentsWhoTookFeedbackList.add(studentFeedbackService
					.findnoofStudentsWhoTookFeedbackList(u.getCourseId()));

			feedbackQuestionsList.clear();
			feedbackQuestionsListForCourse.clear();
			feedbackQuestionsListForFaculty.clear();
			noOfFeedbacksAllocated.clear();

		}

		List<UserCourse> noofStudentsInCourseList = userCourseService
				.noofStudentsInCourseList(CourseIdList);

		for (StudentFeedback sf : noofStudentsWhoTookFeedbackList) {
			noOfStudentFeedbackMap.put(String.valueOf(sf.getCourseId()),
					sf.getNoOfStudentsFeedback());

		}

		for (UserCourse uc : noofStudentsInCourseList) {
			noOfStudentInCourseMap.put(String.valueOf(uc.getCourseId()),
					uc.getNoOfStudentInCourse());

		}

		for (UserCourse u : acadSessionCourseList) {
			Map<String, Object> mapper = new HashMap<>();
			mapper.put("Acad Session", u.getAcadSession());
			mapper.put("Faculty Name", u.getFacultyName());
			mapper.put("Course Name", u.getCourseName());
			mapper.put("Area / Department Name", "NA");

			mapper.put("Year", u.getAcadYear());
			mapper.put("Stream", "NA");

			mapper.put("No.of students in the course",
					noOfStudentInCourseMap.get(String.valueOf(u.getCourseId())));
			mapper.put("No.of students attempted",
					noOfStudentFeedbackMap.get(String.valueOf(u.getCourseId())));
			mapper.put("Course Score", courseAverageMap.get(u.getCourseId()));
			mapper.put("Faculty Score", facultyAverageMap.get(u.getCourseId()));
			mapper.put("Comments by students", "NA");
			mapper.put("Remarks by HOD/Dean if any", "NA");

			map.add(mapper);
		}

		List<Map<String, Object>> ReportacadSessionwise = new ArrayList<Map<String, Object>>();

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "FeedbackReportAcadSessionWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "FeedbackReportAcadSessionWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(map, headers, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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

		// return "report/downLoadReportLink";
	}

	@RequestMapping(value = "/downloadFeedbackReportStudentWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportStudentWise(Model m, Principal p,
			HttpServletResponse response) {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		int count = 0;
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultyType", "CourseName",
				"AcadSession", "Area/DepartmentName", "Year", "Stream",
				"NoOfStudentInTheCourse", "NoOfStudentsAttempted",
				"FacultyScore", "CourseScore", "AverageScore",
				"CommentsByStudents", "Remarks", "ModuleName"));
		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		try {
			File file = new File("E:/feedbackList.txt");

			String parameterJson = FileUtils.readFileToString(file);

			List<StudentFeedback> usernamejson = objMapper.readValue(
					parameterJson, new TypeReference<List<StudentFeedback>>() {
					});

			String feedId = usernamejson.get(0).getFeedbackId().toString();
			String allUsername = usernamejson.get(0).getUsername().toString();
			String courseQuestionSize = usernamejson.get(0).getCourseQuestion()
					.toString();

			List<String> usernameList = new ArrayList<String>(
					Arrays.asList(allUsername.split("\\s*,\\s*")));

			List<StudentFeedback> studentFeedbackListCourseWise = studentFeedbackService
					.getstudentFeedbackListUserWise(feedId, usernameList);
			Set<String> courseIdList = new HashSet<>();

			for (StudentFeedback sf : studentFeedbackListCourseWise) {

				courseIdList.add(String.valueOf(sf.getCourseId()));

			}
			/*
			 * List<Map<String, String>> listOfMapOfEventAndProgramId = new
			 * ArrayList<>(); for (String courseId : courseIdList) { Map<String,
			 * String> mapOfEventAndProgramId = new HashMap<>();
			 * mapOfEventAndProgramId.put(courseId, courseId);
			 * listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId); }
			 * String json = new Gson().toJson(listOfMapOfEventAndProgramId);
			 */
			List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
			Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();

			/*
			 * WebTarget webTarget = client.target(URIUtil
			 * .encodeQuery(userRoleMgmtCrudUrl +
			 * "/getModuleDetailsOfAllCourse")); Invocation.Builder
			 * invocationBuilder = webTarget
			 * .request(MediaType.APPLICATION_JSON);
			 * 
			 * String resp = invocationBuilder.get(String.class); Type listType
			 * = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			 * }.getType(); listOfMapOfCourseIdAndUsermgmtEvent = new
			 * Gson().fromJson(resp, listType); for (Map<String, UsermgmtEvent>
			 * map : listOfMapOfCourseIdAndUsermgmtEvent) { for (String key :
			 * map.keySet()) { mapOfCourseIdAndUsermgmtEvent.put(key,
			 * map.get(key)); } }
			 */

			Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
			Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
			Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
			Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
			List<UserCourse> studentCourseList = userCourseService
					.getStudentCourseList();

			for (String courseIds : courseIdList) {
				List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
				for (StudentFeedback sf : studentFeedbackListCourseWise) {
					if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
						studentFeedbackCourseWise.add(sf);
					}
				}
				mapOfCourseIdAndStudentFeedbackList.put(courseIds,
						studentFeedbackCourseWise);
			}
			for (String courseIds : courseIdList) {
				List<UserCourse> userCourseList = new ArrayList<>();
				for (UserCourse uc : studentCourseList) {
					if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
						userCourseList.add(uc);
					}
				}
				mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
			}
			for (String courseIds : courseIdList) {

				List<StudentFeedback> studentFeedbackByCourse = mapOfCourseIdAndStudentFeedbackList
						.get(courseIds);

				Set<String> facultyList = new HashSet<>();
				Set<String> feedbackIdList = new HashSet<>();
				int noOfStudentsAttemptedFeedback = 0;
				if (studentFeedbackByCourse.size() > 0) {
					List<FeedbackQuestion> courseFeedbackQuestions = new ArrayList<>();
					List<FeedbackQuestion> facultyFeedbackQuestions = new ArrayList<>();
					List<String> feedbackQuestionIdForCourse = new ArrayList<>();
					List<String> feedbackQuestionIdForFaculty = new ArrayList<>();
					List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
							.getFeedbackQuestionListByCourse(courseIds, username);
					Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
					Set<String> feedbackIds = new HashSet<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
						feedbackIds.add(String.valueOf(fq.getFeedbackId()));

					}
					for (String feedbackId : feedbackIds) {
						List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
						for (FeedbackQuestion fq : feedbackQuestionListByCourse) {

							if (feedbackId.equals(String.valueOf(fq
									.getFeedbackId()))) {
								feedbackQuestionListByFeedbackId.add(fq);

							}
						}
						mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId,
								feedbackQuestionListByFeedbackId);
					}
					for (String feedbackId : feedbackIds) {

						List<FeedbackQuestion> feedQuestnList = mapOfFeedbackIdAndFeedbackQuestionList
								.get(feedbackId);

						List<FeedbackQuestion> sublist = feedQuestnList
								.subList(0, 6);

						double courseQuestn = 0;
						double facultyQuestn = 0;
						/*
						 * courseQuestn = feedQuestnList.size() * 60;
						 * courseQuestn = courseQuestn / 100;
						 */
						courseQuestn = Double.parseDouble(courseQuestionSize);
						long courseQuestnCount = Math.round(courseQuestn);
						facultyQuestn = feedQuestnList.size() * 40;
						facultyQuestn = facultyQuestn / 100;
						long facultyQuestnCount = Math.round(facultyQuestn);
						for (int i = 0; i < feedQuestnList.size(); i++) {
							if (i < courseQuestn) {
								courseFeedbackQuestions.add(feedQuestnList
										.get(i));
								feedbackQuestionIdForCourse
										.add(String.valueOf(feedQuestnList.get(
												i).getId()));

							} else {
								facultyFeedbackQuestions.add(feedQuestnList
										.get(i));
								feedbackQuestionIdForFaculty
										.add(String.valueOf(feedQuestnList.get(
												i).getId()));

							}

						}
					}

					List<String> studentCompleted = new ArrayList<>();
					Map<String, String> mapOfFacultyAndType = new HashMap<>();
					for (StudentFeedback sf : studentFeedbackByCourse) {

						if (!mapOfFacultyAndType.containsKey(sf.getFacultyId())) {
							mapOfFacultyAndType.put(sf.getFacultyId(),
									sf.getTypeOfFaculty());
						}

						if ("Y".equals(sf.getFeedbackCompleted())
								&& !studentCompleted.contains(sf.getUsername())) {

							studentCompleted.add(sf.getUsername());
							noOfStudentsAttemptedFeedback++;

						}
						feedbackIdList.add(String.valueOf(sf.getFeedbackId()));
						facultyList.add(sf.getFacultyId());
					}
					if (facultyList.size() == 1) {
						double facultyScore = 0;
						double courseScore = 0;
						double averageScore = 0;
						Map<String, Object> mapOfFeedbackReport = new HashMap<>();
						Map<String, String> commentMap = new HashMap<String, String>();
						StringBuffer comments = new StringBuffer();

						for (StudentFeedback sf : studentFeedbackByCourse) {

							if (commentMap.get(courseIds) != null) {
								comments.append(commentMap.get(courseIds));

							}
							if (sf.getComments() != null
									&& !"".equals(sf.getComments())) {
								comments.append("//");
								comments.append(sf.getComments());
							}

							commentMap.put(courseIds, comments + "");
							comments.setLength(0);

						}

						mapOfFeedbackReport.put("ProgramName",
								studentFeedbackByCourse.get(0)
										.getProgramNameForFeedback());
						mapOfFeedbackReport
								.put("FacultyName", studentFeedbackByCourse
										.get(0).getFacultyName());
						mapOfFeedbackReport.put("FacultyType",
								mapOfFacultyAndType.get(studentFeedbackByCourse
										.get(0).getFacultyId()));

						mapOfFeedbackReport.put("CourseName",
								studentFeedbackByCourse.get(0)
										.getCourseNameforFeedback());
						mapOfFeedbackReport
								.put("AcadSession", studentFeedbackByCourse
										.get(0).getAcadSession());
						mapOfFeedbackReport.put("Area/DepartmentName", "NA");
						mapOfFeedbackReport.put("Year", studentFeedbackByCourse
								.get(0).getAcadYear());
						mapOfFeedbackReport.put("Stream", "NA");
						/*
						 * mapOfFeedbackReport.put("NoOfStudentInTheCourse",
						 * mapOfCourseIdAndUserCourseList.get(courseIds)
						 * .size());
						 */

						mapOfFeedbackReport.put("NoOfStudentInTheCourse",
								usernameList.size());
						mapOfFeedbackReport.put("NoOfStudentsAttempted",
								noOfStudentsAttemptedFeedback);
						List<Map<String, Object>> courseAverageScore = studentFeedbackResponseService
								.getCourseAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
										feedbackQuestionIdForCourse, courseIds,
										usernameList);
						List<Map<String, Object>> facultyAverageScore = studentFeedbackResponseService
								.getFacultyAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
										feedbackQuestionIdForFaculty,
										courseIds,
										studentFeedbackByCourse.get(0)
												.getFacultyId(), usernameList);
						if (courseAverageScore.size() > 0) {

							for (Map<String, Object> mapCourse : courseAverageScore) {
								double average = (Double) mapCourse
										.get("average");
								courseScore = courseScore + average;
							}

							for (Map<String, Object> mapCourse : facultyAverageScore) {
								double average = (Double) mapCourse
										.get("average");
								facultyScore = facultyScore + average;
							}

							courseScore = courseScore
									/ courseAverageScore.size();
							facultyScore = facultyScore
									/ facultyAverageScore.size();
							averageScore = ((courseScore + facultyScore) / 2);

						}
						mapOfFeedbackReport.put("FacultyScore", facultyScore);
						mapOfFeedbackReport.put("CourseScore", courseScore);
						mapOfFeedbackReport.put("AverageScore", averageScore);
						mapOfFeedbackReport.put("CommentsByStudents",
								commentMap.get(courseIds));
						mapOfFeedbackReport.put("Remarks", "");
						if (mapOfCourseIdAndUsermgmtEvent
								.containsKey(courseIds)) {

							mapOfFeedbackReport.put("ModuleName",
									mapOfCourseIdAndUsermgmtEvent
											.get(courseIds).getCourse_name());
						} else {

							mapOfFeedbackReport.put("ModuleName", "NA");
						}
						listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
					} else {

						Map<String, String> commentMap = new HashMap<String, String>();
						StringBuffer comments = new StringBuffer();

						for (String facultyId : facultyList) {

							int noOfStudentCompletedFeedback = 0;
							List<StudentFeedback> studentFeedabckList = studentFeedbackService
									.findStudentFeedbacksByCourseAndFacultyAndStudentList(
											courseIds, facultyId, usernameList);
							for (StudentFeedback sf : studentFeedabckList) {

								if (commentMap.get(courseIds + "" + facultyId) != null) {
									comments.append(commentMap.get(courseIds
											+ "" + facultyId));

								}
								if (sf.getComments() != null
										&& !"".equals(sf.getComments())) {
									comments.append("//");
									comments.append(sf.getComments());
								}

								commentMap.put(courseIds + "" + facultyId,
										comments + "");
								comments.setLength(0);

								if ("Y".equals(sf.getFeedbackCompleted())) {
									noOfStudentCompletedFeedback++;
								}
							}

							double facultyScore = 0;
							double courseScore = 0;
							double averageScore = 0;
							Map<String, Object> mapOfFeedbackReport = new HashMap<>();
							mapOfFeedbackReport.put("ProgramName",
									studentFeedabckList.get(0)
											.getProgramNameForFeedback());
							mapOfFeedbackReport
									.put("FacultyName", studentFeedabckList
											.get(0).getFacultyName());
							mapOfFeedbackReport.put("FacultyType",
									mapOfFacultyAndType.get(facultyId));
							mapOfFeedbackReport.put("CourseName",
									studentFeedbackByCourse.get(0)
											.getCourseNameforFeedback());
							mapOfFeedbackReport.put("AcadSession",
									studentFeedbackByCourse.get(0)
											.getAcadSession());
							mapOfFeedbackReport
									.put("Area/DepartmentName", "NA");
							mapOfFeedbackReport.put("Year",
									studentFeedbackByCourse.get(0)
											.getAcadYear());
							mapOfFeedbackReport.put("Stream", "NA");
							/*
							 * mapOfFeedbackReport.put("NoOfStudentInTheCourse",
							 * mapOfCourseIdAndUserCourseList.get(courseIds)
							 * .size());
							 */
							mapOfFeedbackReport.put("NoOfStudentInTheCourse",
									usernameList.size());
							mapOfFeedbackReport.put("NoOfStudentsAttempted",
									noOfStudentCompletedFeedback);
							List<Map<String, Object>> courseAverageScore = studentFeedbackResponseService
									.getCourseAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
											feedbackQuestionIdForCourse,
											courseIds, usernameList);
							List<Map<String, Object>> facultyAverageScore = studentFeedbackResponseService
									.getFacultyAverageScoreByUsernameFeedbackQuestionIdAndCourseId(
											feedbackQuestionIdForFaculty,
											courseIds, facultyId, usernameList);
							if (courseAverageScore.size() > 0) {

								for (Map<String, Object> mapCourse : courseAverageScore) {
									double average = (Double) mapCourse
											.get("average");
									courseScore = courseScore + average;
								}
							}
							if (facultyAverageScore.size() > 0) {
								for (Map<String, Object> mapCourse : facultyAverageScore) {
									double average = (Double) mapCourse
											.get("average");
									facultyScore = facultyScore + average;
								}
							}

							courseScore = courseScore
									/ courseAverageScore.size();
							facultyScore = facultyScore
									/ facultyAverageScore.size();
							averageScore = ((courseScore + facultyScore) / 2);

							mapOfFeedbackReport.put("FacultyScore",
									facultyScore);
							mapOfFeedbackReport.put("CourseScore", courseScore);
							mapOfFeedbackReport.put("AverageScore",
									averageScore);
							mapOfFeedbackReport.put("CommentsByStudents",
									commentMap.get(courseIds + "" + facultyId));
							mapOfFeedbackReport.put("Remarks", "");
							if (mapOfCourseIdAndUsermgmtEvent
									.containsKey(courseIds)) {

								mapOfFeedbackReport.put(
										"ModuleName",
										mapOfCourseIdAndUsermgmtEvent.get(
												courseIds).getCourse_name());
							} else {

								mapOfFeedbackReport.put("ModuleName", "NA");
							}
							listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
						}

					}
				}

			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReportProgramWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReportProgramWise"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMapOfFeedbackReport, headers,
					filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	@RequestMapping(value = "/downloadAssignmentEvaluationAdvanceSearch", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadAssignmentEvaluationAdvanceSearch(Model m,
			Principal p, HttpServletResponse response) {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);

		String acadSession = u.getAcadSession();

		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		List<String> headers = new ArrayList<String>(Arrays.asList("sapId",
				"facultyId", "courseName", "assignmentName",
				"evaluationStatus", "submissionStatus", "score", "remarks",
				"lowScoreReason"));
		List<Map<String, Object>> getAllAssignmentsForAdvanceSearchByFaculty = assignmentService
				.getAssignmentsWithAdvanceSearchByFaculty(username);
		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "AssignmentEvaluationAdvanceSearch"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "AssignmentEvaluationAdvanceSearch"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(
					getAllAssignmentsForAdvanceSearchByFaculty, headers,
					filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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

		return "report/downLoadReportLink";
	}

	@RequestMapping(value = "/downloadFeedbackReportQWiseForAdminAndFaculty", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportQWiseForAdminAndFaculty(Model m,
			Principal p, HttpServletResponse response,
			@RequestParam(required = false) String acadSession,
			@RequestParam long courseQuestnCount) throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		User user = userService.findByUserName(username);
		int count = 0;
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultyType", "CourseName",
				"AcadSession", "Area/DepartmentName", "Year", "Stream",
				"NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				"CommentsByStudents", "Remarks", "ModuleName"));
		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();

		if (acadSession != null) {
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearForFaculty(
								acadSession, username);
			}
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYear(acadSession);
			}
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemester(acadSession);
		} else {
			if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListForFacultyAndForAllPrograms(username);
			}
			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListForAllPrograms();
			}
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestions();
		}

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 */

		for (int i = 0; i < findFeedbackQuestionBySemester.size(); i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				headers.add("Faculty:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());
			}

		}
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnListOfMap(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndUsermgmtEvent, findFeedbackQuestionBySemester,
				courseQuestnCount, username);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = null;
			String filePath = null;
			if (acadSession != null) {
				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					fileName = "StudentFeedbackReport-"
							+ " "
							+ acadSession
							+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss",
									Utils.getInIST()) + ".xlsx";
					filePath = downloadAllFolder + File.separator + fileName;
				}
				if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
					fileName = "StudentFeedbackReport-"
							+ " "
							+ username
							+ "-"
							+ acadSession
							+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss",
									Utils.getInIST()) + ".xlsx";
					filePath = downloadAllFolder + File.separator + fileName;
				}

			} else {
				if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
					fileName = "StudentFeedbackReport-Consolidated-"

					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
							+ ".xlsx";
					filePath = downloadAllFolder + File.separator + fileName;
				}
				if (userdetails1.getAuthorities().contains(Role.ROLE_FACULTY)) {
					fileName = "StudentFeedbackReport-Consolidated-"
							+ username
							+ "-"
							+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss",
									Utils.getInIST()) + ".xlsx";
					filePath = downloadAllFolder + File.separator + fileName;
				}

			}
			excelCreater.CreateExcelFile(getListOfMapForFeedbackReport,
					headers, filePath);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	/*@RequestMapping(value = "/downloadFacultyFeedbackReportQWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackReportQWise(Model m, Principal p,
			HttpServletResponse response, @RequestParam String acadSession,
			@RequestParam String acadYear,
			@RequestParam long courseQuestnCount,
			@RequestParam String programId,
			@RequestParam(required = false) String campusId,RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 /

		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultySAPId", "FacultyType",
				"CourseName", "AcadSession", "Area/DepartmentName", "Year",
				"Stream", "NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){


		if (!("null").equals(campusId.trim()) && campusId != null) {
			studentFeedbackListCourseWise = studentFeedbackService
					.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
							acadSession, programIdList, acadYear, campusId, username);
		} else {
			studentFeedbackListCourseWise = studentFeedbackService
					.getstudentFeedbackListCourseWiseAndYearProgram(
							acadSession, programIdList, acadYear, username);
		}

		/*
		 * if (campusId != null) { findFeedbackQuestionBySemester =
		 * feedbackQuestionService
		 * .findFeedbackQuestionBySemesterAndCampus(acadSession, acadYear,
		 * campusId); } else { findFeedbackQuestionBySemester =
		 * feedbackQuestionService .findFeedbackQuestionBySemester(acadSession,
		 * acadYear); }
		 /

		if (!("null").equals(campusId.trim()) && campusId != null) {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgramAndCampus(
							acadSession, acadYear, campusId, programIdList, username);
		} else {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgram(acadSession,
							acadYear, programIdList, username);
		}

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 /

		for (int i = 0; i < findFeedbackQuestionBySemester.size(); i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				headers.add("Faculty:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());
			}

		}
		Long facultyQCount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");
		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		List<Course> courseListForModule = new ArrayList<>();
        Map<String, String> mapOfCourseIdAndModule = new HashMap<String, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(String.valueOf(c.getId()), c.getModuleName());
        }
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnMapOfReport(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndModule, findFeedbackQuestionBySemester,
				courseQuestnCount,username, userDetails);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQCount);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}*/
	
	//amey 14-10-2020
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFacultyFeedbackReportQWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackReportQWise(Model m, Principal p,
			HttpServletResponse response, @RequestParam String acadSession,
			@RequestParam String acadYear,
			@RequestParam long courseQuestnCount,
			@RequestParam String programId,
			@RequestParam(required = false) String campusId,
			@RequestParam String feedbackType,RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 */

		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultySAPId", "FacultyType",
				"CourseName", "AcadSession", "Area/DepartmentName", "Year",
				"Stream", "NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedByAndType(username, feedbackType);
        if(feedList.size() > 0){


		if (!("null").equals(campusId.trim()) && campusId != null) {
			studentFeedbackListCourseWise = studentFeedbackService
					.getstudentFeedbackListCourseWiseAndYearProgramAndCampusandType(
							acadSession, programIdList, acadYear, campusId, username, feedbackType);
		} else {
			studentFeedbackListCourseWise = studentFeedbackService
					.getstudentFeedbackListCourseWiseAndYearProgramAndType(
							acadSession, programIdList, acadYear, username, feedbackType);
		}

		/*
		 * if (campusId != null) { findFeedbackQuestionBySemester =
		 * feedbackQuestionService
		 * .findFeedbackQuestionBySemesterAndCampus(acadSession, acadYear,
		 * campusId); } else { findFeedbackQuestionBySemester =
		 * feedbackQuestionService .findFeedbackQuestionBySemester(acadSession,
		 * acadYear); }
		 */

		if (!("null").equals(campusId.trim()) && campusId != null) {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgramAndCampusAndType(
							acadSession, acadYear, campusId, programIdList, username, feedbackType);
		} else {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgramAndType(acadSession,
							acadYear, programIdList, username, feedbackType);
		}

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 */

		for (int i = 0; i < findFeedbackQuestionBySemester.size(); i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				headers.add("Faculty:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());
			}

		}
		Long facultyQCount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");
		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}
		if(courseIdList.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		List<Course> courseListForModule = new ArrayList<>();
        Map<String, String> mapOfCourseIdAndModule = new HashMap<String, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(String.valueOf(c.getId()), c.getModuleName());
        }
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}*/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnMapOfReportNew(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndModule, findFeedbackQuestionBySemester,
				courseQuestnCount,username, userDetails, feedbackType);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			if(courseQuestnCount==0){
			excelCreater.CreateExcelFileOnlyFaculty(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQCount);
			}else{
				excelCreater.CreateExcelFile(getListOfMapForFeedbackReport,
						headers, filePath, courseQuestnCount, facultyQCount);
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}
	
	//amey 14-10-2020
	
	public List<Map<String, Object>> returnMapOfReportNew(
			Set<String> courseIdList,
			Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList,
			Map<String, String> mapOfFacultyIdToDepartment,
			Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList,
			Map<String, String> mapOfCourseIdAndModule,
			List<FeedbackQuestion> findFeedbackQuestionBySemester,
			long countOfCourseQ, String username, Token userDetails, String feedbackType) {

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackByCourse = mapOfCourseIdAndStudentFeedbackList
					.get(courseIds);

			Set<String> facultyList = new HashSet<>();
			Set<String> feedbackIdList = new HashSet<>();
			int noOfStudentsAttemptedFeedback = 0;
			if (studentFeedbackByCourse.size() > 0) {
				List<FeedbackQuestion> courseFeedbackQuestions = new ArrayList<>();
				List<FeedbackQuestion> facultyFeedbackQuestions = new ArrayList<>();
				List<String> feedbackQuestionIdForCourse = new ArrayList<>();
				List<String> feedbackQuestionIdForFaculty = new ArrayList<>();
				/*List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
						.getFeedbackQuestionListByCourse(courseIds);*/
				List<FeedbackQuestion> feedbackQuestionListByCourse = new ArrayList<>();
				if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					feedbackQuestionListByCourse = feedbackQuestionService
						.getFeedbackQuestionListByCourseAndType(courseIds, username, feedbackType);
				}else{
					feedbackQuestionListByCourse = feedbackQuestionService
							.getFeedbackQuestionListByCourseAndTypeForfaculty(courseIds, feedbackType);
				}
				
//				if(feedbackQuestionListByCourse.size()==0) {
//					continue;
//				}
				Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
				Set<String> feedbackIds = new HashSet<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					feedbackIds.add(String.valueOf(fq.getFeedbackId()));

				}
				Set<String> feedbackQuestions = new HashSet<>();
				Map<String, List<String>> mapOfFeedbackQuestionAndListOfFeedbackQuestnIds = new HashMap<>();
				for (String feedbackId : feedbackIds) {
					List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
						feedbackQuestions.add(fq.getDescription());
						if (feedbackId
								.equals(String.valueOf(fq.getFeedbackId()))) {
							feedbackQuestionListByFeedbackId.add(fq);

						}
					}
					mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId,
							feedbackQuestionListByFeedbackId);
				}

				for (String feedbackQ : feedbackQuestions) {
					List<String> feedbackQIds = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {

						if (fq.getDescription().equals(feedbackQ)) {

							feedbackQIds.add(String.valueOf(fq.getId()));

						}
					}
					mapOfFeedbackQuestionAndListOfFeedbackQuestnIds.put(
							feedbackQ, feedbackQIds);
				}

				List<String> studentCompleted = new ArrayList<>();
				Map<String, String> mapOfFacultyAndType = new HashMap<>();
				for (StudentFeedback sf : studentFeedbackByCourse) {
					if (!mapOfFacultyAndType.containsKey(sf.getFacultyId())) {
						mapOfFacultyAndType.put(sf.getFacultyId(),
								sf.getTypeOfFaculty());
					}
					if ("Y".equals(sf.getFeedbackCompleted())
							&& !studentCompleted.contains(sf.getUsername())) {
						studentCompleted.add(sf.getUsername());
						noOfStudentsAttemptedFeedback++;

					}
					feedbackIdList.add(String.valueOf(sf.getFeedbackId()));
					facultyList.add(sf.getFacultyId());
				}
				if (facultyList.size() == 1) {
					double facultyScore = 0;
					double courseScore = 0;
					double averageScore = 0;
					Map<String, Object> mapOfFeedbackReport = new HashMap<>();

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (StudentFeedback sf : studentFeedbackByCourse) {

						if (commentMap.get(courseIds) != null) {
							comments.append(commentMap.get(courseIds));

						}
						if (sf.getComments() != null
								&& !"".equals(sf.getComments())) {
							comments.append("//");
							comments.append(sf.getComments());
						}

						commentMap.put(courseIds, comments + "");
						comments.setLength(0);

					}

					if (studentFeedbackByCourse.size() > 0) {
						mapOfFeedbackReport.put("ProgramName",
								studentFeedbackByCourse.get(0)
										.getProgramNameForFeedback());
						mapOfFeedbackReport
								.put("FacultyName", studentFeedbackByCourse
										.get(0).getFacultyName());
						mapOfFeedbackReport.put("FacultySAPId",
								studentFeedbackByCourse.get(0).getFacultyId());

						mapOfFeedbackReport.put("FacultyType",
								mapOfFacultyAndType.get(studentFeedbackByCourse
										.get(0).getFacultyId()));
						mapOfFeedbackReport.put("CourseName",
								studentFeedbackByCourse.get(0)
										.getCourseNameforFeedback());
						mapOfFeedbackReport
								.put("AcadSession", studentFeedbackByCourse
										.get(0).getAcadSession());
						mapOfFeedbackReport.put("Area/DepartmentName",
								mapOfFacultyIdToDepartment
										.get(studentFeedbackByCourse.get(0)
												.getFacultyId()));
						mapOfFeedbackReport.put("Year", studentFeedbackByCourse
								.get(0).getAcadYear());
						mapOfFeedbackReport.put("Stream", "NA");
						mapOfFeedbackReport.put("NoOfStudentInTheCourse",
								mapOfCourseIdAndUserCourseList.get(courseIds)
										.size());
						mapOfFeedbackReport.put("NoOfStudentsAttempted",
								noOfStudentsAttemptedFeedback);
						/*
						 * List<Map<String, Object>> courseAverageScore =
						 * studentFeedbackResponseService .
						 * getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId
						 * ( feedbackQuestionIdForCourse, courseIds);
						 * List<Map<String, Object>> facultyAverageScore =
						 * studentFeedbackResponseService
						 * .getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
						 * ( feedbackQuestionIdForFaculty, courseIds,
						 * studentFeedbackByCourse.get(0) .getFacultyId()); if
						 * (courseAverageScore.size() > 0) {
						 * 
						 * for (Map<String, Object> mapCourse :
						 * courseAverageScore) { double average = (Double)
						 * mapCourse.get("average"); courseScore = courseScore +
						 * average; }
						 * 
						 * for (Map<String, Object> mapCourse :
						 * facultyAverageScore) { double average = (Double)
						 * mapCourse.get("average"); facultyScore = facultyScore
						 * + average; }
						 * 
						 * courseScore = courseScore /
						 * courseAverageScore.size(); facultyScore =
						 * facultyScore / facultyAverageScore.size();
						 * averageScore = ((courseScore + facultyScore) / 2);
						 * 
						 * } mapOfFeedbackReport.put("FacultyScore",
						 * facultyScore); mapOfFeedbackReport.put("CourseScore",
						 * courseScore); mapOfFeedbackReport.put("AverageScore",
						 * averageScore);
						 */
						/*
						 * mapOfFeedbackReport.put("CommentsByStudents",
						 * commentMap.get(courseIds));
						 */
						mapOfFeedbackReport.put("Remarks", "");
						if (mapOfCourseIdAndModule
								.containsKey(courseIds)) {

							mapOfFeedbackReport.put("ModuleName",
									mapOfCourseIdAndModule
											.get(courseIds));
						} else {

							mapOfFeedbackReport.put("ModuleName", "NA");
						}
						double courseAverage = 0;
						double facultyAverage = 0;
						double grandAverage = 0;
						logger.info("mapOfFeedbackQuestionAndListOfFeedbackQuestnIdsSize--->"+mapOfFeedbackQuestionAndListOfFeedbackQuestnIds.size());
						logger.info("mapOfFeedbackQuestionAndListOfFeedbackQuestnIds--->"+mapOfFeedbackQuestionAndListOfFeedbackQuestnIds);
						
						
						int courseCntAvg=0;
						int facultyCntAvg=0;
						for (int i = 0; i < findFeedbackQuestionBySemester
								.size(); i++) {
							if (i < countOfCourseQ) {
								List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
										.get(findFeedbackQuestionBySemester
												.get(i).getDescription());
								/*
								 * Map<String, Object> getAverageQuestionWise =
								 * studentFeedbackResponseService .
								 * getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
								 * ( feedbackQuestionListByCourse.get(i)
								 * .getId(), courseIds,
								 * studentFeedbackByCourse.get(0)
								 * .getFacultyId());
								 */

								Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
										.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
												feedbackQuestnIds, courseIds,
												studentFeedbackByCourse.get(0)
														.getFacultyId());

								Double averageByQuestion = 0.0;

								if (noOfStudentsAttemptedFeedback > 0) {
									if (getAverageQuestionWise.get("average") != null) {
										courseCntAvg++;
										averageByQuestion = round(
												(Double) getAverageQuestionWise
														.get("average"),
												2);
										mapOfFeedbackReport
												.put("Course:"
														+ findFeedbackQuestionBySemester
																.get(i)
																.getDescription(),
														averageByQuestion);
										courseAverage = courseAverage
												+ averageByQuestion;
									}
								}

							}

						}
						
						if(countOfCourseQ!=0){
							
						courseAverage = courseAverage / courseCntAvg;

						courseAverage = round(courseAverage, 2);

						mapOfFeedbackReport.put("CourseAverage", courseAverage);
						
						}
						for (int i = (int) countOfCourseQ; i < findFeedbackQuestionBySemester
								.size(); i++) {

							List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
									.get(findFeedbackQuestionBySemester.get(i)
											.getDescription());

							Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
											feedbackQuestnIds, courseIds,
											studentFeedbackByCourse.get(0)
													.getFacultyId());

							Double averageByQuestion = 0.0;
							if (noOfStudentsAttemptedFeedback > 0) {
								if (getAverageQuestionWise.get("average") != null) {
									facultyCntAvg++;
									averageByQuestion = round(
											(Double) getAverageQuestionWise
													.get("average"),
											2);

									mapOfFeedbackReport.put("Faculty:"
											+ findFeedbackQuestionBySemester
													.get(i).getDescription(),
											averageByQuestion);

									facultyAverage = facultyAverage
											+ averageByQuestion;
								}
							}

						}
//						facultyAverage = facultyAverage
//								/ (findFeedbackQuestionBySemester.size() - (int) countOfCourseQ);
						facultyAverage = facultyAverage	/ facultyCntAvg;
						facultyAverage = round(facultyAverage, 2);
						if(countOfCourseQ==0){
							grandAverage = facultyAverage;
						}else{
						grandAverage = (courseAverage + facultyAverage) / 2;
						}
						grandAverage = round(grandAverage, 2);
						mapOfFeedbackReport.put("FacultyAverage",
								facultyAverage);
						mapOfFeedbackReport.put("GrandAverage", grandAverage);
						mapOfFeedbackReport.put("CommentsByStudents",
								commentMap.get(courseIds));
						listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
					}
				} else {

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (String facultyId : facultyList) {

						int noOfStudentCompletedFeedback = 0;
						List<StudentFeedback> studentFeedabckList = studentFeedbackService
								.findStudentFeedbacksByCourseAndFacultyAndType(
										courseIds, facultyId, feedbackType);
						List<String> studentCompletedForFaculty = new ArrayList<>();
						for (StudentFeedback sf : studentFeedabckList) {

							if (commentMap.get(courseIds + "" + facultyId) != null) {
								comments.append(commentMap.get(courseIds + ""
										+ facultyId));

							}
							if (sf.getComments() != null
									&& !"".equals(sf.getComments())) {
								comments.append("//");
								comments.append(sf.getComments());
							}

							commentMap.put(courseIds + "" + facultyId, comments
									+ "");
							comments.setLength(0);

							if ("Y".equals(sf.getFeedbackCompleted())
									&& !studentCompletedForFaculty.contains(sf
											.getUsername())) {
								studentCompletedForFaculty
										.add(sf.getUsername());
								noOfStudentCompletedFeedback++;
							}
						}

						double facultyScore = 0;
						double courseScore = 0;
						double averageScore = 0;
						Map<String, Object> mapOfFeedbackReport = new HashMap<>();

						if (studentFeedabckList.size() > 0) {
							mapOfFeedbackReport.put("ProgramName",
									studentFeedabckList.get(0)
											.getProgramNameForFeedback());
							mapOfFeedbackReport
									.put("FacultyName", studentFeedabckList
											.get(0).getFacultyName());
							mapOfFeedbackReport.put("FacultySAPId", facultyId);

							mapOfFeedbackReport.put("FacultyType",
									mapOfFacultyAndType.get(facultyId));
							mapOfFeedbackReport.put("CourseName",
									studentFeedbackByCourse.get(0)
											.getCourseNameforFeedback());
							mapOfFeedbackReport.put("AcadSession",
									studentFeedbackByCourse.get(0)
											.getAcadSession());
							mapOfFeedbackReport.put("Area/DepartmentName",
									mapOfFacultyIdToDepartment.get(facultyId));
							mapOfFeedbackReport.put("Year",
									studentFeedbackByCourse.get(0)
											.getAcadYear());
							mapOfFeedbackReport.put("Stream", "NA");
							mapOfFeedbackReport.put(
									"NoOfStudentInTheCourse",
									mapOfCourseIdAndUserCourseList.get(
											courseIds).size());
							mapOfFeedbackReport.put("NoOfStudentsAttempted",
									noOfStudentCompletedFeedback);
							/*
							 * List<Map<String, Object>> courseAverageScore =
							 * studentFeedbackResponseService .
							 * getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId
							 * ( feedbackQuestionIdForCourse, courseIds);
							 * List<Map<String, Object>> facultyAverageScore =
							 * studentFeedbackResponseService .
							 * getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
							 * ( feedbackQuestionIdForFaculty, courseIds,
							 * facultyId); if (courseAverageScore.size() > 0) {
							 * 
							 * for (Map<String, Object> mapCourse :
							 * courseAverageScore) { double average = (Double)
							 * mapCourse .get("average"); courseScore =
							 * courseScore + average; } } if
							 * (facultyAverageScore.size() > 0) { for
							 * (Map<String, Object> mapCourse :
							 * facultyAverageScore) { double average = (Double)
							 * mapCourse .get("average"); facultyScore =
							 * facultyScore + average; } }
							 * 
							 * courseScore = courseScore /
							 * courseAverageScore.size(); facultyScore =
							 * facultyScore / facultyAverageScore.size();
							 * averageScore = ((courseScore + facultyScore) /
							 * 2);
							 * 
							 * mapOfFeedbackReport.put("FacultyScore",
							 * facultyScore);
							 * mapOfFeedbackReport.put("CourseScore",
							 * courseScore);
							 * mapOfFeedbackReport.put("AverageScore",
							 * averageScore);
							 */
							/*
							 * mapOfFeedbackReport.put("CommentsByStudents",
							 * commentMap.get(courseIds + "" + facultyId));
							 */
							mapOfFeedbackReport.put("Remarks", "");
							if (mapOfCourseIdAndModule
									.containsKey(courseIds)) {

								mapOfFeedbackReport.put(
										"ModuleName",
										mapOfCourseIdAndModule.get(
												courseIds));
							} else {

								mapOfFeedbackReport.put("ModuleName", "NA");
							}

							double courseAverage = 0;
							double facultyAverage = 0;
							double grandAverage = 0;
									
							int courseCntAvg=0;
							int facultyCntAvg=0;
							
							for (int i = 0; i < findFeedbackQuestionBySemester
									.size(); i++) {
								if (i < countOfCourseQ) {
									List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
											.get(findFeedbackQuestionBySemester
													.get(i).getDescription());

									Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
											.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
													feedbackQuestnIds,
													courseIds, facultyId);

									Double averageByQuestion = 0.0;

									if (noOfStudentCompletedFeedback > 0) {
										if (getAverageQuestionWise
												.get("average") != null) {
											courseCntAvg++;
											mapOfFeedbackReport
													.put("Course:"
															+ findFeedbackQuestionBySemester
																	.get(i)
																	.getDescription(),
															getAverageQuestionWise
																	.get("average"));
											averageByQuestion = round(
													(Double) getAverageQuestionWise
															.get("average"), 2);

											courseAverage = courseAverage
													+ averageByQuestion;
										}
									}

								}

							}
							if(countOfCourseQ!=0){
							courseAverage = courseAverage / courseCntAvg;
							courseAverage = round(courseAverage, 2);
							mapOfFeedbackReport.put("CourseAverage",
									courseAverage);
							}

							for (int i = (int) countOfCourseQ; i < findFeedbackQuestionBySemester
									.size(); i++) {
								List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
										.get(findFeedbackQuestionBySemester
												.get(i).getDescription());

								Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
										.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
												feedbackQuestnIds, courseIds,
												facultyId);
								Double averageByQuestion = 0.0;

								if (noOfStudentCompletedFeedback > 0) {
									if (getAverageQuestionWise.get("average") != null) {
										facultyCntAvg++;
										averageByQuestion = round(
												(Double) getAverageQuestionWise
														.get("average"),
												2);

										mapOfFeedbackReport.put("Faculty:"
												+ findFeedbackQuestionBySemester
														.get(i)
														.getDescription(),
												getAverageQuestionWise
														.get("average"));
										facultyAverage = facultyAverage
												+ averageByQuestion;
									}
								}
							}
//							facultyAverage = facultyAverage
//									/ (findFeedbackQuestionBySemester.size() - (int) countOfCourseQ);
							facultyAverage = facultyAverage / facultyCntAvg;
							facultyAverage = round(facultyAverage, 2);
							if(countOfCourseQ==0){
								grandAverage=facultyAverage;
							}else{
							grandAverage = (courseAverage + facultyAverage) / 2;
							}

							grandAverage = round(grandAverage, 2);

							mapOfFeedbackReport.put("FacultyAverage",
									facultyAverage);
							mapOfFeedbackReport.put("GrandAverage",
									grandAverage);
							mapOfFeedbackReport.put("CommentsByStudents",
									commentMap.get(courseIds + "" + facultyId));
							listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
						}

					}

				}
			}
		}
		return listOfMapOfFeedbackReport;

	}

	public List<Map<String, Object>> returnMapOfReport(
			Set<String> courseIdList,
			Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList,
			Map<String, String> mapOfFacultyIdToDepartment,
			Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList,
			Map<String, String> mapOfCourseIdAndModule,
			List<FeedbackQuestion> findFeedbackQuestionBySemester,
			long countOfCourseQ, String username, Token userDetails) {

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackByCourse = mapOfCourseIdAndStudentFeedbackList
					.get(courseIds);

			Set<String> facultyList = new HashSet<>();
			Set<String> feedbackIdList = new HashSet<>();
			int noOfStudentsAttemptedFeedback = 0;
			if (studentFeedbackByCourse.size() > 0) {
				List<FeedbackQuestion> courseFeedbackQuestions = new ArrayList<>();
				List<FeedbackQuestion> facultyFeedbackQuestions = new ArrayList<>();
				List<String> feedbackQuestionIdForCourse = new ArrayList<>();
				List<String> feedbackQuestionIdForFaculty = new ArrayList<>();
				/*List<FeedbackQuestion> feedbackQuestionListByCourse = feedbackQuestionService
						.getFeedbackQuestionListByCourse(courseIds);*/
				List<FeedbackQuestion> feedbackQuestionListByCourse = new ArrayList<>();
				if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
					feedbackQuestionListByCourse = feedbackQuestionService
						.getFeedbackQuestionListByCourse(courseIds, username);
				}else{
					feedbackQuestionListByCourse = feedbackQuestionService
							.getFeedbackQuestionListByCourseForfaculty(courseIds);
				}
				
//				if(feedbackQuestionListByCourse.size()==0) {
//					continue;
//				}
				Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndFeedbackQuestionList = new HashMap<>();
				Set<String> feedbackIds = new HashSet<>();
				for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
					feedbackIds.add(String.valueOf(fq.getFeedbackId()));

				}
				Set<String> feedbackQuestions = new HashSet<>();
				Map<String, List<String>> mapOfFeedbackQuestionAndListOfFeedbackQuestnIds = new HashMap<>();
				for (String feedbackId : feedbackIds) {
					List<FeedbackQuestion> feedbackQuestionListByFeedbackId = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {
						feedbackQuestions.add(fq.getDescription());
						if (feedbackId
								.equals(String.valueOf(fq.getFeedbackId()))) {
							feedbackQuestionListByFeedbackId.add(fq);

						}
					}
					mapOfFeedbackIdAndFeedbackQuestionList.put(feedbackId,
							feedbackQuestionListByFeedbackId);
				}

				for (String feedbackQ : feedbackQuestions) {
					List<String> feedbackQIds = new ArrayList<>();
					for (FeedbackQuestion fq : feedbackQuestionListByCourse) {

						if (fq.getDescription().equals(feedbackQ)) {

							feedbackQIds.add(String.valueOf(fq.getId()));

						}
					}
					mapOfFeedbackQuestionAndListOfFeedbackQuestnIds.put(
							feedbackQ, feedbackQIds);
				}

				List<String> studentCompleted = new ArrayList<>();
				Map<String, String> mapOfFacultyAndType = new HashMap<>();
				for (StudentFeedback sf : studentFeedbackByCourse) {
					if (!mapOfFacultyAndType.containsKey(sf.getFacultyId())) {
						mapOfFacultyAndType.put(sf.getFacultyId(),
								sf.getTypeOfFaculty());
					}
					if ("Y".equals(sf.getFeedbackCompleted())
							&& !studentCompleted.contains(sf.getUsername())) {
						studentCompleted.add(sf.getUsername());
						noOfStudentsAttemptedFeedback++;

					}
					feedbackIdList.add(String.valueOf(sf.getFeedbackId()));
					facultyList.add(sf.getFacultyId());
				}
				if (facultyList.size() == 1) {
					double facultyScore = 0;
					double courseScore = 0;
					double averageScore = 0;
					Map<String, Object> mapOfFeedbackReport = new HashMap<>();

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (StudentFeedback sf : studentFeedbackByCourse) {

						if (commentMap.get(courseIds) != null) {
							comments.append(commentMap.get(courseIds));

						}
						if (sf.getComments() != null
								&& !"".equals(sf.getComments())) {
							comments.append("//");
							comments.append(sf.getComments());
						}

						commentMap.put(courseIds, comments + "");
						comments.setLength(0);

					}

					if (studentFeedbackByCourse.size() > 0) {
						mapOfFeedbackReport.put("ProgramName",
								studentFeedbackByCourse.get(0)
										.getProgramNameForFeedback());
						mapOfFeedbackReport
								.put("FacultyName", studentFeedbackByCourse
										.get(0).getFacultyName());
						mapOfFeedbackReport.put("FacultySAPId",
								studentFeedbackByCourse.get(0).getFacultyId());

						mapOfFeedbackReport.put("FacultyType",
								mapOfFacultyAndType.get(studentFeedbackByCourse
										.get(0).getFacultyId()));
						mapOfFeedbackReport.put("CourseName",
								studentFeedbackByCourse.get(0)
										.getCourseNameforFeedback());
						mapOfFeedbackReport
								.put("AcadSession", studentFeedbackByCourse
										.get(0).getAcadSession());
						mapOfFeedbackReport.put("Area/DepartmentName",
								mapOfFacultyIdToDepartment
										.get(studentFeedbackByCourse.get(0)
												.getFacultyId()));
						mapOfFeedbackReport.put("Year", studentFeedbackByCourse
								.get(0).getAcadYear());
						mapOfFeedbackReport.put("Stream", "NA");
						mapOfFeedbackReport.put("NoOfStudentInTheCourse",
								mapOfCourseIdAndUserCourseList.get(courseIds)
										.size());
						mapOfFeedbackReport.put("NoOfStudentsAttempted",
								noOfStudentsAttemptedFeedback);
						/*
						 * List<Map<String, Object>> courseAverageScore =
						 * studentFeedbackResponseService .
						 * getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId
						 * ( feedbackQuestionIdForCourse, courseIds);
						 * List<Map<String, Object>> facultyAverageScore =
						 * studentFeedbackResponseService
						 * .getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
						 * ( feedbackQuestionIdForFaculty, courseIds,
						 * studentFeedbackByCourse.get(0) .getFacultyId()); if
						 * (courseAverageScore.size() > 0) {
						 * 
						 * for (Map<String, Object> mapCourse :
						 * courseAverageScore) { double average = (Double)
						 * mapCourse.get("average"); courseScore = courseScore +
						 * average; }
						 * 
						 * for (Map<String, Object> mapCourse :
						 * facultyAverageScore) { double average = (Double)
						 * mapCourse.get("average"); facultyScore = facultyScore
						 * + average; }
						 * 
						 * courseScore = courseScore /
						 * courseAverageScore.size(); facultyScore =
						 * facultyScore / facultyAverageScore.size();
						 * averageScore = ((courseScore + facultyScore) / 2);
						 * 
						 * } mapOfFeedbackReport.put("FacultyScore",
						 * facultyScore); mapOfFeedbackReport.put("CourseScore",
						 * courseScore); mapOfFeedbackReport.put("AverageScore",
						 * averageScore);
						 */
						/*
						 * mapOfFeedbackReport.put("CommentsByStudents",
						 * commentMap.get(courseIds));
						 */
						mapOfFeedbackReport.put("Remarks", "");
						if (mapOfCourseIdAndModule
								.containsKey(courseIds)) {

							mapOfFeedbackReport.put("ModuleName",
									mapOfCourseIdAndModule
											.get(courseIds));
						} else {

							mapOfFeedbackReport.put("ModuleName", "NA");
						}
						double courseAverage = 0;
						double facultyAverage = 0;
						double grandAverage = 0;
						logger.info("mapOfFeedbackQuestionAndListOfFeedbackQuestnIdsSize--->"+mapOfFeedbackQuestionAndListOfFeedbackQuestnIds.size());
						logger.info("mapOfFeedbackQuestionAndListOfFeedbackQuestnIds--->"+mapOfFeedbackQuestionAndListOfFeedbackQuestnIds);
						
						
						int courseCntAvg=0;
						int facultyCntAvg=0;
						for (int i = 0; i < findFeedbackQuestionBySemester
								.size(); i++) {
							if (i < countOfCourseQ) {
								List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
										.get(findFeedbackQuestionBySemester
												.get(i).getDescription());
								/*
								 * Map<String, Object> getAverageQuestionWise =
								 * studentFeedbackResponseService .
								 * getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
								 * ( feedbackQuestionListByCourse.get(i)
								 * .getId(), courseIds,
								 * studentFeedbackByCourse.get(0)
								 * .getFacultyId());
								 */

								Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
										.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
												feedbackQuestnIds, courseIds,
												studentFeedbackByCourse.get(0)
														.getFacultyId());

								Double averageByQuestion = 0.0;

								if (noOfStudentsAttemptedFeedback > 0) {
									if (getAverageQuestionWise.get("average") != null) {
										courseCntAvg++;
										averageByQuestion = round(
												(Double) getAverageQuestionWise
														.get("average"),
												2);
										mapOfFeedbackReport
												.put("Course:"
														+ findFeedbackQuestionBySemester
																.get(i)
																.getDescription(),
														averageByQuestion);
										courseAverage = courseAverage
												+ averageByQuestion;
									}
								}

							}

						}
						courseAverage = courseAverage / courseCntAvg;

						courseAverage = round(courseAverage, 2);

						mapOfFeedbackReport.put("CourseAverage", courseAverage);

						for (int i = (int) countOfCourseQ; i < findFeedbackQuestionBySemester
								.size(); i++) {

							List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
									.get(findFeedbackQuestionBySemester.get(i)
											.getDescription());

							Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
									.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
											feedbackQuestnIds, courseIds,
											studentFeedbackByCourse.get(0)
													.getFacultyId());

							Double averageByQuestion = 0.0;
							if (noOfStudentsAttemptedFeedback > 0) {
								if (getAverageQuestionWise.get("average") != null) {
									facultyCntAvg++;
									averageByQuestion = round(
											(Double) getAverageQuestionWise
													.get("average"),
											2);

									mapOfFeedbackReport.put("Faculty:"
											+ findFeedbackQuestionBySemester
													.get(i).getDescription(),
											averageByQuestion);

									facultyAverage = facultyAverage
											+ averageByQuestion;
								}
							}

						}
//						facultyAverage = facultyAverage
//								/ (findFeedbackQuestionBySemester.size() - (int) countOfCourseQ);
						facultyAverage = facultyAverage	/ facultyCntAvg;
						facultyAverage = round(facultyAverage, 2);
						grandAverage = (courseAverage + facultyAverage) / 2;
						grandAverage = round(grandAverage, 2);
						mapOfFeedbackReport.put("FacultyAverage",
								facultyAverage);
						mapOfFeedbackReport.put("GrandAverage", grandAverage);
						mapOfFeedbackReport.put("CommentsByStudents",
								commentMap.get(courseIds));
						listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
					}
				} else {

					Map<String, String> commentMap = new HashMap<String, String>();
					StringBuffer comments = new StringBuffer();

					for (String facultyId : facultyList) {

						int noOfStudentCompletedFeedback = 0;
						List<StudentFeedback> studentFeedabckList = studentFeedbackService
								.findStudentFeedbacksByCourseAndFaculty(
										courseIds, facultyId);
						List<String> studentCompletedForFaculty = new ArrayList<>();
						for (StudentFeedback sf : studentFeedabckList) {

							if (commentMap.get(courseIds + "" + facultyId) != null) {
								comments.append(commentMap.get(courseIds + ""
										+ facultyId));

							}
							if (sf.getComments() != null
									&& !"".equals(sf.getComments())) {
								comments.append("//");
								comments.append(sf.getComments());
							}

							commentMap.put(courseIds + "" + facultyId, comments
									+ "");
							comments.setLength(0);

							if ("Y".equals(sf.getFeedbackCompleted())
									&& !studentCompletedForFaculty.contains(sf
											.getUsername())) {
								studentCompletedForFaculty
										.add(sf.getUsername());
								noOfStudentCompletedFeedback++;
							}
						}

						double facultyScore = 0;
						double courseScore = 0;
						double averageScore = 0;
						Map<String, Object> mapOfFeedbackReport = new HashMap<>();

						if (studentFeedabckList.size() > 0) {
							mapOfFeedbackReport.put("ProgramName",
									studentFeedabckList.get(0)
											.getProgramNameForFeedback());
							mapOfFeedbackReport
									.put("FacultyName", studentFeedabckList
											.get(0).getFacultyName());
							mapOfFeedbackReport.put("FacultySAPId", facultyId);

							mapOfFeedbackReport.put("FacultyType",
									mapOfFacultyAndType.get(facultyId));
							mapOfFeedbackReport.put("CourseName",
									studentFeedbackByCourse.get(0)
											.getCourseNameforFeedback());
							mapOfFeedbackReport.put("AcadSession",
									studentFeedbackByCourse.get(0)
											.getAcadSession());
							mapOfFeedbackReport.put("Area/DepartmentName",
									mapOfFacultyIdToDepartment.get(facultyId));
							mapOfFeedbackReport.put("Year",
									studentFeedbackByCourse.get(0)
											.getAcadYear());
							mapOfFeedbackReport.put("Stream", "NA");
							mapOfFeedbackReport.put(
									"NoOfStudentInTheCourse",
									mapOfCourseIdAndUserCourseList.get(
											courseIds).size());
							mapOfFeedbackReport.put("NoOfStudentsAttempted",
									noOfStudentCompletedFeedback);
							/*
							 * List<Map<String, Object>> courseAverageScore =
							 * studentFeedbackResponseService .
							 * getCourseFacultyAverageScoreByFeedbackQuestionIdAndCourseId
							 * ( feedbackQuestionIdForCourse, courseIds);
							 * List<Map<String, Object>> facultyAverageScore =
							 * studentFeedbackResponseService .
							 * getFacultyAverageScoreByFeedbackQuestionIdAndCourseId
							 * ( feedbackQuestionIdForFaculty, courseIds,
							 * facultyId); if (courseAverageScore.size() > 0) {
							 * 
							 * for (Map<String, Object> mapCourse :
							 * courseAverageScore) { double average = (Double)
							 * mapCourse .get("average"); courseScore =
							 * courseScore + average; } } if
							 * (facultyAverageScore.size() > 0) { for
							 * (Map<String, Object> mapCourse :
							 * facultyAverageScore) { double average = (Double)
							 * mapCourse .get("average"); facultyScore =
							 * facultyScore + average; } }
							 * 
							 * courseScore = courseScore /
							 * courseAverageScore.size(); facultyScore =
							 * facultyScore / facultyAverageScore.size();
							 * averageScore = ((courseScore + facultyScore) /
							 * 2);
							 * 
							 * mapOfFeedbackReport.put("FacultyScore",
							 * facultyScore);
							 * mapOfFeedbackReport.put("CourseScore",
							 * courseScore);
							 * mapOfFeedbackReport.put("AverageScore",
							 * averageScore);
							 */
							/*
							 * mapOfFeedbackReport.put("CommentsByStudents",
							 * commentMap.get(courseIds + "" + facultyId));
							 */
							mapOfFeedbackReport.put("Remarks", "");
							if (mapOfCourseIdAndModule
									.containsKey(courseIds)) {

								mapOfFeedbackReport.put(
										"ModuleName",
										mapOfCourseIdAndModule.get(
												courseIds));
							} else {

								mapOfFeedbackReport.put("ModuleName", "NA");
							}

							double courseAverage = 0;
							double facultyAverage = 0;
							double grandAverage = 0;
									
							int courseCntAvg=0;
							int facultyCntAvg=0;
							
							for (int i = 0; i < findFeedbackQuestionBySemester
									.size(); i++) {
								if (i < countOfCourseQ) {
									List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
											.get(findFeedbackQuestionBySemester
													.get(i).getDescription());

									Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
											.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
													feedbackQuestnIds,
													courseIds, facultyId);

									Double averageByQuestion = 0.0;

									if (noOfStudentCompletedFeedback > 0) {
										if (getAverageQuestionWise
												.get("average") != null) {
											courseCntAvg++;
											mapOfFeedbackReport
													.put("Course:"
															+ findFeedbackQuestionBySemester
																	.get(i)
																	.getDescription(),
															getAverageQuestionWise
																	.get("average"));
											averageByQuestion = round(
													(Double) getAverageQuestionWise
															.get("average"), 2);

											courseAverage = courseAverage
													+ averageByQuestion;
										}
									}

								}

							}
							courseAverage = courseAverage / courseCntAvg;
							courseAverage = round(courseAverage, 2);
							mapOfFeedbackReport.put("CourseAverage",
									courseAverage);

							for (int i = (int) countOfCourseQ; i < findFeedbackQuestionBySemester
									.size(); i++) {
								List<String> feedbackQuestnIds = mapOfFeedbackQuestionAndListOfFeedbackQuestnIds
										.get(findFeedbackQuestionBySemester
												.get(i).getDescription());

								Map<String, Object> getAverageQuestionWise = studentFeedbackResponseService
										.getFacultyAverageScoreByFeedbackQuestionListIdAndCourseId(
												feedbackQuestnIds, courseIds,
												facultyId);
								Double averageByQuestion = 0.0;

								if (noOfStudentCompletedFeedback > 0) {
									if (getAverageQuestionWise.get("average") != null) {
										facultyCntAvg++;
										averageByQuestion = round(
												(Double) getAverageQuestionWise
														.get("average"),
												2);

										mapOfFeedbackReport.put("Faculty:"
												+ findFeedbackQuestionBySemester
														.get(i)
														.getDescription(),
												getAverageQuestionWise
														.get("average"));
										facultyAverage = facultyAverage
												+ averageByQuestion;
									}
								}
							}
//							facultyAverage = facultyAverage
//									/ (findFeedbackQuestionBySemester.size() - (int) countOfCourseQ);
							facultyAverage = facultyAverage / facultyCntAvg;
							facultyAverage = round(facultyAverage, 2);
							grandAverage = (courseAverage + facultyAverage) / 2;

							grandAverage = round(grandAverage, 2);

							mapOfFeedbackReport.put("FacultyAverage",
									facultyAverage);
							mapOfFeedbackReport.put("GrandAverage",
									grandAverage);
							mapOfFeedbackReport.put("CommentsByStudents",
									commentMap.get(courseIds + "" + facultyId));
							listOfMapOfFeedbackReport.add(mapOfFeedbackReport);
						}

					}

				}
			}
		}
		return listOfMapOfFeedbackReport;

	}

	private static double round(double value, int places) {
		if (Double.isNaN(value)) {
			return 0;
		} else {
			if (places < 0)
				throw new IllegalArgumentException();
	
			BigDecimal bd = new BigDecimal(Double.toString(value));
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
	}

	/*@RequestMapping(value = "/downloadFacultyFeedbackReportQuestionWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackReportQuestionWise(Model m,
			Principal p, HttpServletResponse response,
			@RequestParam String acadSession, @RequestParam String acadYear,
			@RequestParam long courseQuestnCount,
			@RequestParam String programId,
			@RequestParam(required = false) String campusId,RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 /
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultySAPId", "FacultyType",
				"CourseName", "AcadSession", "Area/DepartmentName", "Year",
				"Stream", "NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){


		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

			/*if (campusId != null) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
								acadSession, programIdList, acadYear, campusId);
			} else {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgram(
								acadSession, programIdList, acadYear);
			}/
			if (campusId != null) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
								acadSession, programIdList, acadYear, campusId, username);
			} else {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgram(
								acadSession, programIdList, acadYear, username);
			}
		} else {
			if (campusId != null) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFaculty(
								acadSession, programIdList, acadYear, campusId,
								username);
			} else {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramByFaculty(
								acadSession, programIdList, acadYear, username);
			}
		}
		if(studentFeedbackListCourseWise.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}

		/*
		 * if (campusId != null) { findFeedbackQuestionBySemester =
		 * feedbackQuestionService
		 * .findFeedbackQuestionBySemesterAndCampus(acadSession, acadYear,
		 * campusId); } else { findFeedbackQuestionBySemester =
		 * feedbackQuestionService .findFeedbackQuestionBySemester(acadSession,
		 * acadYear); }
		 /
		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
		if (campusId != null) {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgramAndCampus(
							acadSession, acadYear, campusId, programIdList, username);
		} else {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgram(acadSession,
							acadYear, programIdList, username);
		}
		}else{
			//08-09-2020 Akshay
//			if (campusId != null) {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgramAndCampus(
//								acadSession, acadYear, campusId, programIdList);
//			} else {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgram(acadSession,
//								acadYear, programIdList);
//			}
			Set<Long> courseList=new HashSet<>();
			studentFeedbackListCourseWise.forEach(o->courseList.add(o.getCourseId()));
			findFeedbackQuestionBySemester=feedbackQuestionService.findFeedbackQuestionByCourseList(username, courseList);
		}

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 /

		for (int i = 0; i < findFeedbackQuestionBySemester.size(); i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				headers.add("Faculty:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());
			}

		}
		Long facultyQcount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");

		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}
		if(courseIdList.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}
		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		List<Course> courseListForModule = new ArrayList<>();
        Map<String, String> mapOfCourseIdAndModule = new HashMap<String, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(String.valueOf(c.getId()), c.getModuleName());
        }

		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnMapOfReport(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndModule, findFeedbackQuestionBySemester,
				courseQuestnCount,username,userDetails);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQcount);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}*/
	
	//amey 14-10-2020
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFacultyFeedbackReportQuestionWise", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackReportQuestionWise(Model m,
			Principal p, HttpServletResponse response,
			@RequestParam String acadSession, @RequestParam String acadYear,
			@RequestParam long courseQuestnCount,
			@RequestParam String programId,
			@RequestParam String feedbackType,
			@RequestParam(required = false) String campusId,RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 */
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultySAPId", "FacultyType",
				"CourseName", "AcadSession", "Area/DepartmentName", "Year",
				"Stream", "NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = new ArrayList<String>();
		programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		
		logger.info("feedbackType----------->"+feedbackType);
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedByAndType(username, feedbackType);
        if(feedList.size() > 0){


		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

			/*if (campusId != null) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndCampus(
								acadSession, programIdList, acadYear, campusId);
			} else {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgram(
								acadSession, programIdList, acadYear);
			}*/
			if (campusId != null) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndCampusandType(
								acadSession, programIdList, acadYear, campusId, username,feedbackType);
			} else {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndType(
								acadSession, programIdList, acadYear, username,feedbackType);
			}
		} else {
			if (campusId != null) {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFaculty(
								acadSession, programIdList, acadYear, campusId,
								username);
			} else {
				studentFeedbackListCourseWise = studentFeedbackService
						.getstudentFeedbackListCourseWiseAndYearProgramByFaculty(
								acadSession, programIdList, acadYear, username);
			}
		}
		if(studentFeedbackListCourseWise.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}

		/*
		 * if (campusId != null) { findFeedbackQuestionBySemester =
		 * feedbackQuestionService
		 * .findFeedbackQuestionBySemesterAndCampus(acadSession, acadYear,
		 * campusId); } else { findFeedbackQuestionBySemester =
		 * feedbackQuestionService .findFeedbackQuestionBySemester(acadSession,
		 * acadYear); }
		 */
		if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
		if (campusId != null) {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgramAndCampusAndType(
							acadSession, acadYear, campusId, programIdList, username, feedbackType);
		} else {
			findFeedbackQuestionBySemester = feedbackQuestionService
					.findFeedbackQuestionBySemesterProgramAndType(acadSession,
							acadYear, programIdList, username, feedbackType);
		}
		}else{
			//08-09-2020 Akshay
//			if (campusId != null) {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgramAndCampus(
//								acadSession, acadYear, campusId, programIdList);
//			} else {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgram(acadSession,
//								acadYear, programIdList);
//			}
			Set<Long> courseList=new HashSet<>();
			studentFeedbackListCourseWise.forEach(o->courseList.add(o.getCourseId()));
			findFeedbackQuestionBySemester=feedbackQuestionService.findFeedbackQuestionByCourseList(username, courseList);
		}

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 */

		for (int i = 0; i < findFeedbackQuestionBySemester.size(); i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				headers.add("Faculty:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());
			}

		}
		Long facultyQcount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");

		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}
		if(courseIdList.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}
		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		List<Course> courseListForModule = new ArrayList<>();
        Map<String, String> mapOfCourseIdAndModule = new HashMap<String, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(String.valueOf(c.getId()), c.getModuleName());
        }

		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}*/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnMapOfReportNew(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndModule, findFeedbackQuestionBySemester,
				courseQuestnCount,username,userDetails, feedbackType);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " " + acadSession
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			if(courseQuestnCount==0){
				excelCreater.CreateExcelFileOnlyFaculty(getListOfMapForFeedbackReport,
						headers, filePath, courseQuestnCount, facultyQcount);
			}else{
			excelCreater.CreateExcelFile(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQcount);
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}

	// ---------------------- Utility Report Starts
	// --------------------------------

	// =============================================================================================
	// First Report
	// =============================================================================================
	
	
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY"})
	@RequestMapping(value = "/featureWiseUtilisationReport", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String FeatureWiseUtilisationReport(Model m, Principal p,
			HttpServletResponse response, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String campus)
			throws URIException {

		if ("null".equals(campus)) {
			campus = null;
		}

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(
				Arrays.asList("Sr.No", "Group Name", "Students",
						"Total Student", "Student Percentage", "Admin",
						"Total Admin", "Admin Percentage", "Faculty Visiting",
						"Total Visiting Faculty",
						"Visiting Faculty Percentage", "Faculty Core",
						"Total Core Faculty", "Core Faculty Percentage"));

		List<String> featureName = new ArrayList<String>();
		featureName.add("announcement");
		featureName.add("forum");
		featureName.add("content");
		featureName.add("assessment");
		featureName.add("feedback");
		featureName.add("query");
		List<String> groupNameToShow = new ArrayList<String>();
		groupNameToShow.add("Notification");
		groupNameToShow.add("Discussion");
		groupNameToShow.add("Course Content");
		groupNameToShow.add("Team work Submission");
		groupNameToShow.add("Feedback");
		groupNameToShow.add("Student Queries");

		// ==================== Student Queries start =========================

		List<StudentTest> studentTestSubmission = studentTestService
				.getNoOfStudentSubmissionStats(campus, fromDate, toDate);

		List<StudentAssignment> studentAssignmentSubmission = studentAssignmentService
				.getStudentAssignmentSubmissionStats(campus, fromDate, toDate);

		List<StudentContent> getStudentContentStats = studentContentService
				.getStudentContentStats(campus, fromDate, toDate);

		List<StudentFeedback> getStudentFeedbackStats = studentFeedbackService
				.getStudentFeedbackStats(campus, fromDate, toDate);

		List<Announcement> getNoOfAnnouncementStats = announcementService
				.getNoOfAnnouncementStats(fromDate, toDate);

		List<Forum> getNoOfForumStats = forumService.getNoOfForumStats(
				fromDate, toDate);

		List<Query> getQueryStats = queryService.getQueryStats(campus,
				fromDate, toDate);

		Map<String, List<User>> adminUsageCountMap = userService
				.checkCreatedByRoleStatsMap(campus, Role.ROLE_ADMIN, fromDate,
						toDate, null);

		Map<String, List<User>> vistingFacultyUsageCountMap = userService
				.checkCreatedByRoleStatsMap(campus, Role.ROLE_FACULTY,
						fromDate, toDate, "H");

		Map<String, List<User>> coreFacultyUsageCountMap = userService
				.checkCreatedByRoleStatsMap(campus, Role.ROLE_FACULTY,
						fromDate, toDate, "P");

		List<User> getStudentStats = userService.getTotalUserStats(campus,
				Role.ROLE_STUDENT, null);

		List<User> getVisitingFacultyStats = userService.getTotalUserStats(
				campus, Role.ROLE_FACULTY, "H");

		List<User> getCoreFacultyStats = userService.getTotalUserStats(campus,
				Role.ROLE_FACULTY, "P");

		List<User> getAdminStats = userService.findByRole(Role.ROLE_ADMIN);

		// ==================== Student Queries end =========================

		Set<String> assesCount = new HashSet<String>();
		Set<String> forumCount = new HashSet<String>();
		Set<String> courseContentCount = new HashSet<String>();
		Set<String> feedbackCount = new HashSet<String>();
		Set<String> queryCount = new HashSet<String>();

		// ============== student =======================
		for (StudentTest test : studentTestSubmission) {
			assesCount.add(test.getUsername());
		}

		for (StudentAssignment assign : studentAssignmentSubmission) {
			assesCount.add(assign.getUsername());
		}
		for (StudentContent content : getStudentContentStats) {
			courseContentCount.add(content.getUsername());
		}
		for (StudentFeedback feed : getStudentFeedbackStats) {
			feedbackCount.add(feed.getUsername());
		}
		for (Query query : getQueryStats) {
			queryCount.add(query.getUsername());
		}

		// ============== student =======================

		// ============== admin =======================
		Map<String, Set<String>> adminCountUsageNoDuplicateMap = new HashMap<String, Set<String>>();
		for (String name : featureName) {

			Set<String> removeDuplicate = new HashSet<String>();

			for (User user : adminUsageCountMap.get(name)) {
				removeDuplicate.add(user.getUsername());
			}
			adminCountUsageNoDuplicateMap.put(name, removeDuplicate);
		}

		// ============== admin =======================

		// ============== Visiting Faculty =============

		Map<String, Set<String>> visitingCountUsageNoDuplicateMap = new HashMap<String, Set<String>>();
		for (String name : featureName) {
			// List<User> usage =vistingFacultyUsageCountMap.get(name);
			Set<String> removeDuplicate = new HashSet<String>();
			for (User user : vistingFacultyUsageCountMap.get(name)) {
				removeDuplicate.add(user.getUsername());
			}
			visitingCountUsageNoDuplicateMap.put(name, removeDuplicate);
		}

		// ============== Visiting Faculty =============

		// ============== core Faculty =============

		Map<String, Set<String>> coreCountUsageNoDuplicateMap = new HashMap<String, Set<String>>();
		for (String name : featureName) {
			Set<String> removeDuplicate = new HashSet<String>();
			for (User user : coreFacultyUsageCountMap.get(name)) {
				removeDuplicate.add(user.getUsername());
			}
			coreCountUsageNoDuplicateMap.put(name, removeDuplicate);
		}
		// ============== core Faculty =============
		int instituteAnnouncemntCount = 0;
		List<Long> courseIdAnnouncement = new ArrayList<Long>();
		for (Announcement announce : getNoOfAnnouncementStats) {
			if ("INSTITUTE".equals(announce.getAnnouncementType())) {
				instituteAnnouncemntCount++;
			} else if ("COURSE".equals(announce.getAnnouncementType())) {
				courseIdAnnouncement.add(announce.getCourseId());
			}
		}
		Long totalAnnouncemntUsage = Long.valueOf(instituteAnnouncemntCount
				* getStudentStats.size());
		for (Long courseId : courseIdAnnouncement) {
			List<UserCourse> studentCount = userCourseService
					.noofStudentsInCourseList(campus, courseId);
			totalAnnouncemntUsage = totalAnnouncemntUsage
					+ Long.valueOf(studentCount.get(0).getNoOfStudentInCourse());
		}

		Integer totalForumUsageByStudents = 0;
		for (Forum forum : getNoOfForumStats) {
			List<UserCourse> studentCount = userCourseService
					.noofStudentsInCourseList(campus, forum.getCourseId());
			totalForumUsageByStudents = totalForumUsageByStudents
					+ studentCount.get(0).getNoOfStudentInCourse();

			for (UserCourse user : studentCount) {
				forumCount.add(user.getUsername());
			}

		}

		// ---------------------------------

		Set<StudentTest> d = new LinkedHashSet<StudentTest>(
				studentTestSubmission);

		// ---------------------------------
		Map<String, Long> studentMap = new HashMap<String, Long>();
		Map<String, Long> adminMap = new HashMap<String, Long>();
		Map<String, Long> visitingfacultyMap = new HashMap<String, Long>();
		Map<String, Long> corefacultyMap = new HashMap<String, Long>();
		Map<String, Long> studentPerCentageMap = new HashMap<String, Long>();
		Map<String, String> studentUsageCount = new HashMap<String, String>();

		// ==== student data start ======
		studentMap.put("announcement", totalAnnouncemntUsage);
		studentMap.put("forum", Long.valueOf(totalForumUsageByStudents));
		studentMap.put("content", Long.valueOf(getStudentContentStats.size()));
		studentMap.put(
				"assessment",
				Long.valueOf(studentTestSubmission.size()
						+ studentAssignmentSubmission.size()));
		studentMap
				.put("feedback", Long.valueOf(getStudentFeedbackStats.size()));
		studentMap.put("query", Long.valueOf(getQueryStats.size()));

		studentPerCentageMap.put("announcement",
				Long.valueOf(assesCount.size()));
		studentPerCentageMap.put("forum", Long.valueOf(forumCount.size()));
		studentPerCentageMap.put("content",
				Long.valueOf(courseContentCount.size()));
		studentPerCentageMap.put("assessment", Long.valueOf(assesCount.size()));
		studentPerCentageMap
				.put("feedback", Long.valueOf(feedbackCount.size()));
		studentPerCentageMap.put("query", Long.valueOf(queryCount.size()));
		// ==== student data end =======

		// ===== admin data starts ======
		for (String name : featureName) {
			adminMap.put(name,
					Long.valueOf(adminUsageCountMap.get(name).size()));
		}

		// ====== admin data end =======

		// ===== visiting faculty data starts ======
		for (String name : featureName) {
			visitingfacultyMap.put(name,
					Long.valueOf(vistingFacultyUsageCountMap.get(name).size()));
		}
		// ===== visiting faculty data end ======

		// ===== core faculty data starts ======
		for (String name : featureName) {
			corefacultyMap.put(name,
					Long.valueOf(coreFacultyUsageCountMap.get(name).size()));
		}
		// ===== core faculty data end ======
		List<Map<String, Object>> listOfMap = new ArrayList<>();
		for (int i = 0; i < featureName.size(); i++) {
			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			mapOfAvg.put("Sr.No", i + 1);
			// mapOfAvg.put("Name of School", );
			mapOfAvg.put("Group Name", groupNameToShow.get(i));
			mapOfAvg.put("Students", studentMap.get(featureName.get(i)));
			mapOfAvg.put("Total Student", getStudentStats.size());

			if (studentPerCentageMap.get(featureName.get(i)) != 0) {
				Double percent = (((double) studentPerCentageMap
						.get(featureName.get(i)) / (double) getStudentStats
						.size())) * 100;
				mapOfAvg.put("Student Percentage", percent);
			} else {

				mapOfAvg.put("Student Percentage", "0");
			}
			mapOfAvg.put("Admin", adminMap.get(featureName.get(i)));

			mapOfAvg.put("Total Admin", getAdminStats.size());

			if (adminCountUsageNoDuplicateMap.get(featureName.get(i)).size() != 0) {
				Double percent = ((Double.valueOf(adminCountUsageNoDuplicateMap
						.get(featureName.get(i)).size()) / Double
						.valueOf(getAdminStats.size()))) * 100;
				mapOfAvg.put("Admin Percentage", percent);
			} else {

				mapOfAvg.put("Admin Percentage", "0");
			}
			mapOfAvg.put("Faculty Visiting",
					visitingfacultyMap.get(featureName.get(i)));
			mapOfAvg.put("Total Visiting Faculty",
					getVisitingFacultyStats.size());
			if (visitingCountUsageNoDuplicateMap.get(featureName.get(i)).size() != 0) {
				Double percent = ((Double
						.valueOf(visitingCountUsageNoDuplicateMap.get(
								featureName.get(i)).size()) / Double
						.valueOf(getVisitingFacultyStats.size()))) * 100;
				mapOfAvg.put("Visiting Faculty Percentage", percent);
			} else {

				mapOfAvg.put("Visiting Faculty Percentage", "0");
			}

			mapOfAvg.put("Faculty Core", corefacultyMap.get(featureName.get(i)));

			mapOfAvg.put("Total Core Faculty", getCoreFacultyStats.size());

			if (coreCountUsageNoDuplicateMap.get(featureName.get(i)).size() != 0) {
				Double percent = ((Double.valueOf(coreCountUsageNoDuplicateMap
						.get(featureName.get(i)).size()) / Double
						.valueOf(getCoreFacultyStats.size()))) * 100;
				mapOfAvg.put("Core Faculty Percentage", percent);
			} else {

				mapOfAvg.put("Core Faculty Percentage", "0");
			}
			listOfMap.add(mapOfAvg);
		}

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "UtilityReportSummary-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	// =============================================================================================
	// Second Report
	// =============================================================================================
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/featureWiseDetailedUtilisationReport", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String featureWiseDetailedUtilisationReport(Model m, Principal p,
			HttpServletResponse response, @RequestParam String fromDate,
			@RequestParam String toDate) throws URIException {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<ProgramCampus> campusList = programCampusService.getCampusList();
		Map<String, String> mapOfCampus = new HashMap<String, String>();

		for (ProgramCampus pc : campusList) {
			mapOfCampus.put(pc.getCampusId(), pc.getCampusName());
		}

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Campus", "Dept", "Acad Year", "UserName", "User Type",
				"Faculty Type(Core/Visiting)", "Program", "Acad Session",
				"Course Id", "Course/Event Name", "Features", "Total Used"));

		List<User> adminList = userService
				.findActiveUsersByRole(Role.ROLE_ADMIN);
		List<User> facultyList = userService
				.findActiveUsersByRole(Role.ROLE_FACULTY);
		List<User> studentList = userService
				.findActiveUsersByRole(Role.ROLE_STUDENT);

		Map<String, List<User>> mapOfUser = new HashMap<String, List<User>>();
		mapOfUser.put(Role.ROLE_ADMIN.name(), adminList);
		mapOfUser.put(Role.ROLE_FACULTY.name(), facultyList);
		mapOfUser.put(Role.ROLE_STUDENT.name(), studentList);

		Map<String, String> peopleWhoUsedPortalFeatureMapFaculty = new HashMap<String, String>();

		Map<String, List<User>> visitingFacultyUsageMap = new HashMap<String, List<User>>();
		Map<String, List<User>> coreFacultyUsageMap = new HashMap<String, List<User>>();
		Map<String, List<User>> AdminUsageMap = new HashMap<String, List<User>>();
		Map<String, List<User>> StudentUsageMap = new HashMap<String, List<User>>();

		List<String> facultyFeatures = new ArrayList<String>();
		facultyFeatures.add("Assignment");
		facultyFeatures.add("Test");
		facultyFeatures.add("Announcement");
		facultyFeatures.add("Forum");
		facultyFeatures.add("Content");
		facultyFeatures.add("Evaluation");

		List<String> adminFeatures = new ArrayList<String>();
		adminFeatures.add("Announcement");
		adminFeatures.add("Feedback");
		adminFeatures.add("Query");

		List<String> studentFeatures = new ArrayList<String>();
		studentFeatures.add("Assignment");
		studentFeatures.add("Test");
		studentFeatures.add("Forum");
		studentFeatures.add("Content");
		studentFeatures.add("Feedback");
		studentFeatures.add("Query");

		List<String> facultyFeaturesList = new ArrayList<String>();
		facultyFeaturesList.add("Create Assignment");
		facultyFeaturesList.add("Evaluate Assignment");
		facultyFeaturesList.add("Create Announcement");
		facultyFeaturesList.add("Create Test");
		facultyFeaturesList.add("Create Forum");
		facultyFeaturesList.add("Create Content");
		List<String> adminFeaturesList = new ArrayList<String>();
		adminFeaturesList.add("Create feedback");
		adminFeaturesList.add("Create Announcement");
		adminFeaturesList.add("Raise query Response");
		List<String> studentFeaturesList = new ArrayList<String>();
		studentFeaturesList.add("Submit Assignment");
		studentFeaturesList.add("Take Test");
		studentFeaturesList.add("Forum comment and like");
		studentFeaturesList.add("Give Feedback");
		studentFeaturesList.add("Course content");
		studentFeaturesList.add("Raise Query");

		visitingFacultyUsageMap = userService.checkAllRoleStatsMap(
				Role.ROLE_FACULTY, fromDate, toDate, "H");
		coreFacultyUsageMap = userService.checkAllRoleStatsMap(
				Role.ROLE_FACULTY, fromDate, toDate, "P");
		AdminUsageMap = userService.checkAllRoleStatsMap(Role.ROLE_ADMIN,
				fromDate, toDate, null);
		StudentUsageMap = userService.checkAllRoleStatsMap(Role.ROLE_STUDENT,
				fromDate, toDate, null);

		List<Map<String, Object>> listOfMap = new ArrayList<>();

		Integer counter = 1;

		for (String feature : facultyFeatures) {
			for (User featureUsage : coreFacultyUsageMap.get(feature)) {

				Map<String, Object> mapOfAvg = new HashMap<String, Object>();
				mapOfAvg.put("Sr.No", counter);
				if (featureUsage.getCampusId() != null) {
					mapOfAvg.put("Campus", mapOfCampus.get(featureUsage
							.getCampusId().toString()));
				} else {
					mapOfAvg.put("Campus", "-");
				}
				mapOfAvg.put(
						"Dept",
						(featureUsage.getDept() != null ? featureUsage
								.getDept() : "-"));
				mapOfAvg.put(
						"Acad Year",
						(featureUsage.getAcadYear() != null ? featureUsage
								.getAcadYear() : "-"));
				mapOfAvg.put("UserName", featureUsage.getUsername());
				mapOfAvg.put("User Type", featureUsage.getRole());
				mapOfAvg.put("Faculty Type(Core/Visiting)", (featureUsage
						.getType() != null ? featureUsage.getType() : "-"));
				mapOfAvg.put(
						"Program",
						(featureUsage.getProgramId() != null ? featureUsage
								.getProgramId() : "-"));
				mapOfAvg.put(
						"Acad Session",
						(featureUsage.getAcadSession() != null ? featureUsage
								.getAcadSession() : "-"));
				mapOfAvg.put(
						"Course Id",
						(featureUsage.getCourseId() != null ? featureUsage
								.getCourseId() : "-"));
				mapOfAvg.put(
						"Course/Event Name",
						(featureUsage.getCourseName() != null ? featureUsage
								.getCourseName() : "-"));
				mapOfAvg.put("Features", feature);
				mapOfAvg.put("Total Used", featureUsage.getCount());
				listOfMap.add(mapOfAvg);
				counter = counter + 1;
			}
		}

		for (String feature : facultyFeatures) {

			for (User featureUsage : visitingFacultyUsageMap.get(feature)) {

				Map<String, Object> mapOfAvg = new HashMap<String, Object>();
				mapOfAvg.put("Sr.No", counter);
				if (featureUsage.getCampusId() != null) {
					mapOfAvg.put("Campus", mapOfCampus.get(featureUsage
							.getCampusId().toString()));
				} else {
					mapOfAvg.put("Campus", "-");
				}
				mapOfAvg.put(
						"Dept",
						(featureUsage.getDept() != null ? featureUsage
								.getDept() : "-"));
				mapOfAvg.put(
						"Acad Year",
						(featureUsage.getAcadYear() != null ? featureUsage
								.getAcadYear() : "-"));
				mapOfAvg.put("UserName", featureUsage.getUsername());
				mapOfAvg.put("User Type", featureUsage.getRole());
				mapOfAvg.put("Faculty Type(Core/Visiting)", (featureUsage
						.getType() != null ? featureUsage.getType() : "-"));
				mapOfAvg.put(
						"Program",
						(featureUsage.getProgramId() != null ? featureUsage
								.getProgramId() : "-"));
				mapOfAvg.put(
						"Acad Session",
						(featureUsage.getAcadSession() != null ? featureUsage
								.getAcadSession() : "-"));
				mapOfAvg.put(
						"Course Id",
						(featureUsage.getCourseId() != null ? featureUsage
								.getCourseId() : "-"));
				mapOfAvg.put(
						"Course/Event Name",
						(featureUsage.getCourseName() != null ? featureUsage
								.getCourseName() : "-"));
				mapOfAvg.put("Features", feature);
				mapOfAvg.put("Total Used", featureUsage.getCount());
				listOfMap.add(mapOfAvg);
				counter = counter + 1;
			}

		}

		for (String feature : adminFeatures) {

			for (User featureUsage : AdminUsageMap.get(feature)) {

				Map<String, Object> mapOfAvg = new HashMap<String, Object>();
				mapOfAvg.put("Sr.No", counter);
				if (featureUsage.getCampusId() != null) {
					mapOfAvg.put("Campus", mapOfCampus.get(featureUsage
							.getCampusId().toString()));
				} else {
					mapOfAvg.put("Campus", "-");
				}
				mapOfAvg.put(
						"Dept",
						(featureUsage.getDept() != null ? featureUsage
								.getDept() : "-"));
				mapOfAvg.put(
						"Acad Year",
						(featureUsage.getAcadYear() != null ? featureUsage
								.getAcadYear() : "-"));
				mapOfAvg.put("UserName", featureUsage.getUsername());
				mapOfAvg.put("User Type", featureUsage.getRole());
				mapOfAvg.put("Faculty Type(Core/Visiting)", (featureUsage
						.getType() != null ? featureUsage.getType() : "-"));
				mapOfAvg.put(
						"Program",
						(featureUsage.getProgramId() != null ? featureUsage
								.getProgramId() : "-"));
				mapOfAvg.put(
						"Acad Session",
						(featureUsage.getAcadSession() != null ? featureUsage
								.getAcadSession() : "-"));
				mapOfAvg.put(
						"Course Id",
						(featureUsage.getCourseId() != null ? featureUsage
								.getCourseId() : "-"));
				mapOfAvg.put(
						"Course/Event Name",
						(featureUsage.getCourseName() != null ? featureUsage
								.getCourseName() : "-"));
				mapOfAvg.put("Features", feature);
				mapOfAvg.put("Total Used", featureUsage.getCount());
				listOfMap.add(mapOfAvg);
				counter = counter + 1;
			}

		}

		for (String feature : studentFeatures) {

			for (User featureUsage : StudentUsageMap.get(feature)) {

				Map<String, Object> mapOfAvg = new HashMap<String, Object>();
				mapOfAvg.put("Sr.No", counter);
				if (featureUsage.getCampusId() != null) {
					mapOfAvg.put("Campus", mapOfCampus.get(featureUsage
							.getCampusId().toString()));
				} else {
					mapOfAvg.put("Campus", "-");
				}
				mapOfAvg.put(
						"Dept",
						(featureUsage.getDept() != null ? featureUsage
								.getDept() : "-"));
				mapOfAvg.put(
						"Acad Year",
						(featureUsage.getAcadYear() != null ? featureUsage
								.getAcadYear() : "-"));
				mapOfAvg.put("UserName", featureUsage.getUsername());
				mapOfAvg.put("User Type", featureUsage.getRole());
				mapOfAvg.put("Faculty Type(Core/Visiting)", (featureUsage
						.getType() != null ? featureUsage.getType() : "-"));
				mapOfAvg.put(
						"Program",
						(featureUsage.getProgramId() != null ? featureUsage
								.getProgramId() : "-"));
				mapOfAvg.put(
						"Acad Session",
						(featureUsage.getAcadSession() != null ? featureUsage
								.getAcadSession() : "-"));
				mapOfAvg.put(
						"Course Id",
						(featureUsage.getCourseId() != null ? featureUsage
								.getCourseId() : "-"));
				mapOfAvg.put(
						"Course/Event Name",
						(featureUsage.getCourseName() != null ? featureUsage
								.getCourseName() : "-"));
				mapOfAvg.put("Features", feature);
				mapOfAvg.put("Total Used", featureUsage.getCount());
				listOfMap.add(mapOfAvg);
				counter = counter + 1;
			}

		}

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "DetailedUtilityReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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

		return "report/downLoadReportLink";
	}

	// ---------------------- Utility Report End
	// --------------------------------

	/*@RequestMapping(value = "/downloadFacultyFeedbackReportQuestionWiseForAllPrograms", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackReportQuestionWiseForAllPrograms(
			Model m, Principal p, HttpServletResponse response,
			@RequestParam String acadYear,
			@RequestParam long courseQuestnCount,
			@RequestParam(required = false) String campusId,RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 /
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultySAPId", "FacultyType",
				"CourseName", "AcadSession", "Area/DepartmentName", "Year",
				"Stream", "NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = programService
				.findAllProgramsForFaculty(username);
		// programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		/*
		 * List<String> acadSessionList = studentFeedbackService
		 * .findAllAcadSessionForFacultyFeedback(username);
		 /

		// for (String acadSession : acadSessionList)
		if (programIdList.size() > 0) {

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

				if (campusId != null) {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramAndCampusWithoutSession(
									programIdList, acadYear, campusId);
				} else {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramWithoutSession(
									programIdList, acadYear);
				}
			} else {
				if (campusId != null) {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramAndCampusByFacultyWithoutSession(
									programIdList, acadYear, campusId, username);
				} else {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramByFacultyWithoutSession(
									programIdList, acadYear, username);
				}
			}
				
			if(studentFeedbackListCourseWise.size() == 0){
				setNote(r,"No Feedback  Found For Inputs!");
	            return "redirect:/downloadReportMyCourseStudentForm";
			}
			/*
			 * if (campusId != null) { findFeedbackQuestionBySemester =
			 * feedbackQuestionService
			 * .findFeedbackQuestionBySemesterAndCampus(acadSession, acadYear,
			 * campusId); } else { findFeedbackQuestionBySemester =
			 * feedbackQuestionService
			 * .findFeedbackQuestionBySemester(acadSession, acadYear); }
			 /

//			if (campusId != null) {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(
//								acadYear, campusId, programIdList);
//			} else {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgramWithoutSession(
//								acadYear, programIdList);
//			}
			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				if (campusId != null) {
					findFeedbackQuestionBySemester = feedbackQuestionService.findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(acadYear, campusId,
							programIdList);
				} else {
				findFeedbackQuestionBySemester = feedbackQuestionService
				.findFeedbackQuestionBySemesterProgramWithoutSession(acadYear, programIdList);
				}
				} else {
				Set<Long> courseList = new HashSet<>();
				studentFeedbackListCourseWise.forEach(o -> courseList.add(o.getCourseId()));
				findFeedbackQuestionBySemester = feedbackQuestionService.findFeedbackQuestionByCourseList(username,
				courseList);
				}
		}else{
            
            setNote(r,"No feedback  published ");
            return "redirect:/downloadReportMyCourseStudentForm";
      }

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 /

		for (int i = 0; i < findFeedbackQuestionBySemester.size() + 1; i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				if(i==courseQuestnCount) {
					headers.add("");
				}else {
				headers.add("Faculty:" + findFeedbackQuestionBySemester.get(i-1).getDescription());
				}
			}

		}
		Long facultyQcount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");

		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		List<Course> courseListForModule = new ArrayList<>();
        Map<String, String> mapOfCourseIdAndModule = new HashMap<String, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(String.valueOf(c.getId()), c.getModuleName());
        }
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnMapOfReport(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndModule, findFeedbackQuestionBySemester,
				courseQuestnCount,username, userDetails);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " "
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " "
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFileChanged(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQcount);
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}*/
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFacultyFeedbackReportQuestionWiseForAllPrograms", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackReportQuestionWiseForAllPrograms(
			Model m, Principal p, HttpServletResponse response,
			@RequestParam String acadYear,
			@RequestParam long courseQuestnCount,
			@RequestParam(required = false) String campusId,
			@RequestParam String feedbackType, RedirectAttributes r)
			throws URIException {
		ObjectMapper objMapper = new ObjectMapper();
		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		String username = p.getName();
		Token userDetails = (Token) p;
		int count = 0;
		/*
		 * List<String> headers = new ArrayList<String>(Arrays.asList(
		 * "ProgramName", "FacultyName", "FacultyType", "CourseName",
		 * "AcadSession", "Area/DepartmentName", "Year", "Stream",
		 * "NoOfStudentInTheCourse", "NoOfStudentsAttempted",
		 * 
		 * "CommentsByStudents", "Remarks", "ModuleName"));
		 */
		List<String> headers = new ArrayList<String>(Arrays.asList(
				"ProgramName", "FacultyName", "FacultySAPId", "FacultyType",
				"CourseName", "AcadSession", "Area/DepartmentName", "Year",
				"Stream", "NoOfStudentInTheCourse", "NoOfStudentsAttempted",

				// "CommentsByStudents",
				"Remarks", "ModuleName", ""));

		List<Map<String, Object>> listOfMapOfFeedbackReport = new ArrayList<>();
		Token userdetails1 = (Token) p;

		List<String> programIdList = programService
				.findAllProgramsForFacultyByType(username, feedbackType);
		// programIdList = Arrays.asList(programId.split("\\s*,\\s*"));

		List<StudentFeedback> studentFeedbackListCourseWise = new ArrayList<>();
		List<FeedbackQuestion> findFeedbackQuestionBySemester = new ArrayList<>();
		/*
		 * List<String> acadSessionList = studentFeedbackService
		 * .findAllAcadSessionForFacultyFeedback(username);
		 */

		// for (String acadSession : acadSessionList)
		if (programIdList.size() > 0) {

			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {

				if (campusId != null) {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramAndCampusWithoutSession(
									programIdList, acadYear, campusId);
				} else {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramWithoutSession(
									programIdList, acadYear);
				}
			} else {
				if (campusId != null) {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramAndCampusAndTypeByFacultyWithoutSession(
									programIdList, acadYear, campusId, username, feedbackType);
				} else {
					studentFeedbackListCourseWise = studentFeedbackService
							.getstudentFeedbackListCourseWiseAndYearProgramAndTypeByFacultyWithoutSession(
									programIdList, acadYear, username, feedbackType);
				}
			}
				
			if(studentFeedbackListCourseWise.size() == 0){
				setNote(r,"No Feedback  Found For Inputs!");
	            return "redirect:/downloadReportMyCourseStudentForm";
			}
			/*
			 * if (campusId != null) { findFeedbackQuestionBySemester =
			 * feedbackQuestionService
			 * .findFeedbackQuestionBySemesterAndCampus(acadSession, acadYear,
			 * campusId); } else { findFeedbackQuestionBySemester =
			 * feedbackQuestionService
			 * .findFeedbackQuestionBySemester(acadSession, acadYear); }
			 */

//			if (campusId != null) {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(
//								acadYear, campusId, programIdList);
//			} else {
//				findFeedbackQuestionBySemester = feedbackQuestionService
//						.findFeedbackQuestionBySemesterProgramWithoutSession(
//								acadYear, programIdList);
//			}
			if (userDetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
				if (campusId != null) {
					findFeedbackQuestionBySemester = feedbackQuestionService.findFeedbackQuestionBySemesterProgramAndCampusWithoutSession(acadYear, campusId,
							programIdList);
				} else {
				findFeedbackQuestionBySemester = feedbackQuestionService
				.findFeedbackQuestionBySemesterProgramWithoutSession(acadYear, programIdList);
				}
			} else {
				Set<Long> courseList = new HashSet<>();
				studentFeedbackListCourseWise.forEach(o -> courseList.add(o.getCourseId()));
				findFeedbackQuestionBySemester = feedbackQuestionService.findFeedbackQuestionByCourseListAndType(username,
				courseList, feedbackType);
			}
		}else{
            
            setNote(r,"No feedback  published ");
            return "redirect:/downloadReportMyCourseStudentForm";
      }

		/*
		 * double courseQuestn = 0; double facultyQuestn = 0; courseQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; courseQuestn =
		 * courseQuestn / 100; long courseQuestnCount =
		 * Math.round(courseQuestn); facultyQuestn =
		 * findFeedbackQuestionBySemester.size() * 50; facultyQuestn =
		 * facultyQuestn / 100; long facultyQuestnCount =
		 * Math.round(facultyQuestn);
		 */

		for (int i = 0; i < findFeedbackQuestionBySemester.size() + 1; i++) {
			if (i < courseQuestnCount) {
				headers.add("Course:"
						+ findFeedbackQuestionBySemester.get(i)
								.getDescription());

				if (i == courseQuestnCount - 1) {
					headers.add("CourseAverage");
				}

			} else {
				if(i==courseQuestnCount) {
					headers.add("");
				}else {
				headers.add("Faculty:" + findFeedbackQuestionBySemester.get(i-1).getDescription());
				}
			}

		}
		Long facultyQcount = findFeedbackQuestionBySemester.size()
				- courseQuestnCount;
		headers.add("FacultyAverage");
		headers.add("GrandAverage");
		headers.add("CommentsByStudents");

		Map<String, String> mapOfFacultyIdToDepartment = getFacultyDepartments();
		Set<String> courseIdList = new HashSet<>();
		for (StudentFeedback sf : studentFeedbackListCourseWise) {

			courseIdList.add(String.valueOf(sf.getCourseId()));

		}

		List<Map<String, String>> listOfMapOfEventAndProgramId = new ArrayList<>();
		List<Course> courseListForModule = new ArrayList<>();
        Map<String, String> mapOfCourseIdAndModule = new HashMap<String, String>();
        courseListForModule = courseService.findcourseDetailsByCourseIds(courseIdList);
        
        for(Course c : courseListForModule){
              mapOfCourseIdAndModule.put(String.valueOf(c.getId()), c.getModuleName());
        }
		for (String courseId : courseIdList) {
			Map<String, String> mapOfEventAndProgramId = new HashMap<>();
			mapOfEventAndProgramId.put(courseId, courseId);
			listOfMapOfEventAndProgramId.add(mapOfEventAndProgramId);
		}
		String json = new Gson().toJson(listOfMapOfEventAndProgramId);
		/*List<Map<String, UsermgmtEvent>> listOfMapOfCourseIdAndUsermgmtEvent = new ArrayList<>();
		Map<String, UsermgmtEvent> mapOfCourseIdAndUsermgmtEvent = new HashMap<String, UsermgmtEvent>();
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getModuleDetailsOfAllCourse"));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			String resp = invocationBuilder.get(String.class);

			Type listType = new TypeToken<List<Map<String, UsermgmtEvent>>>() {
			}.getType();
			listOfMapOfCourseIdAndUsermgmtEvent = new Gson().fromJson(resp,
					listType);
			for (Map<String, UsermgmtEvent> map : listOfMapOfCourseIdAndUsermgmtEvent) {
				for (String key : map.keySet()) {
					mapOfCourseIdAndUsermgmtEvent.put(key, map.get(key));
				}
			}

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}*/

		Map<String, List<StudentFeedback>> mapOfCourseIdAndStudentFeedbackList = new HashMap<>();
		Map<String, List<UserCourse>> mapOfCourseIdAndUserCourseList = new HashMap<>();
		Map<String, List<StudentFeedback>> mapOfFeedbackIdAndStudentFeedbackList = new HashMap<>();
		Map<String, Set<String>> mapOfUsernameAndCompletionStatusList = new HashMap<>();
		List<UserCourse> studentCourseList = userCourseService
				.getStudentCourseList();

		for (String courseIds : courseIdList) {
			List<StudentFeedback> studentFeedbackCourseWise = new ArrayList<>();
			for (StudentFeedback sf : studentFeedbackListCourseWise) {
				if (courseIds.equals(String.valueOf(sf.getCourseId()))) {
					studentFeedbackCourseWise.add(sf);
				}
			}
			mapOfCourseIdAndStudentFeedbackList.put(courseIds,
					studentFeedbackCourseWise);
		}

		for (String courseIds : courseIdList) {
			List<UserCourse> userCourseList = new ArrayList<>();
			for (UserCourse uc : studentCourseList) {
				if (courseIds.equals(String.valueOf(uc.getCourseId()))) {
					userCourseList.add(uc);
				}
			}
			mapOfCourseIdAndUserCourseList.put(courseIds, userCourseList);
		}
		List<Map<String, Object>> getListOfMapForFeedbackReport = returnMapOfReportNew(
				courseIdList, mapOfCourseIdAndStudentFeedbackList,
				mapOfFacultyIdToDepartment, mapOfCourseIdAndUserCourseList,
				mapOfCourseIdAndModule, findFeedbackQuestionBySemester,
				courseQuestnCount,username, userDetails, feedbackType);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-" + " "
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport" + " "
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			
			if(courseQuestnCount==0){
				excelCreater.CreateExcelFileOnlyFaculty(getListOfMapForFeedbackReport,
						headers, filePath, courseQuestnCount, facultyQcount);
			}else{
			excelCreater.CreateExcelFileChanged(getListOfMapForFeedbackReport,
					headers, filePath, courseQuestnCount, facultyQcount);
			}
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		return "report/downLoadReportLink";
	}

	// ---------------------- New Utility Report Starts
	// --------------------------------

	// =============================================================================================
	// First Report
	// =============================================================================================
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/featureWiseSummaryUtilityReport", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String featureWiseSummaryUtilityReport(Model m, Principal p,
			HttpServletResponse response, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String campus,
			HttpServletRequest request) throws URIException {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<UtilityReport> summaryUtilityReportList = utilityReportService
				.getSummaryUtilityReport(campus, fromDate, toDate);

		String filePath = "";
		filePath = utilityReportService
				.getXlsxforSummaryUtilityReport(summaryUtilityReportList);

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		ServletContext context = request.getSession().getServletContext();
		File downloadFile = new File(filePath);
		try {
			inputStream = new FileInputStream(downloadFile);
			outStream = response.getOutputStream();

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}

			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	// =============================================================================================
	// Second Report
	// =============================================================================================
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/featureWiseDetailedUtilityReport", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String featureWiseDetailedUtilityReport(Model m, Principal p,
			HttpServletResponse response, @RequestParam String fromDate,
			@RequestParam String toDate, HttpServletRequest request)
			throws URIException {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<UtilityReport> detailedUtilityReportList = utilityReportService
				.getDetailedUtilityReport(null, fromDate, toDate);

		String filePath = "";
		filePath = utilityReportService
				.getXlsxforDetailedUtilityReport(detailedUtilityReportList);

		OutputStream outStream = null;
		FileInputStream inputStream = null;
		ServletContext context = request.getSession().getServletContext();
		File downloadFile = new File(filePath);
		try {
			inputStream = new FileInputStream(downloadFile);
			outStream = response.getOutputStream();

			// get MIME type of the file
			String mimeType = context.getMimeType(filePath);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}

			response.setContentLength((int) downloadFile.length());

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			IOUtils.copy(inputStream, outStream);
		} catch (Exception e) {
			logger.error("Exception", e);
		} finally {
			if (inputStream != null)
				IOUtils.closeQuietly(inputStream);
			if (outStream != null)
				IOUtils.closeQuietly(outStream);
		}
		return null;
	}

	// ---------------------- Utility Report End
	// --------------------------------

	// Feedback Letter Code
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFacultyFeedbackLetterForAllPrograms", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFacultyFeedbackLetterForAllPrograms(Model m,
			Principal p, HttpServletResponse response,
			HttpServletRequest request, @RequestParam String acadYear,
			@RequestParam String acadSessionList, @RequestParam String term,
			@RequestParam String dean,

			@RequestParam(required = false) String username,
			@RequestParam(required = false) String campusId)
			throws URIException {

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));
		m.addAttribute("app", app);
		Token userDetails = (Token) p;

		List<String> acadSessions = Arrays.asList(acadSessionList
				.split("\\s*,\\s*"));

		m.addAttribute("acadSessionList", acadSessions);

		String programId = String.valueOf(userDetails.getProgramId());
		Program program = programService.findByID(Long.valueOf(programId));

		if (username == null
				&& userDetails.getAuthorities().contains(Role.ROLE_FACULTY)) {

			username = p.getName();
		}

		String collegeName = "";
		try {
			WebTarget webTarget = client.target(URIUtil
					.encodeQuery(userRoleMgmtCrudUrl
							+ "/getCollegeNameByProgramId?programId="
							+ programId));
			Invocation.Builder invocationBuilder = webTarget
					.request(MediaType.APPLICATION_JSON);

			collegeName = invocationBuilder.get(String.class);

		} catch (Exception ex) {
			logger.error("Exception ", ex);
		}

		if (username == null) {

			List<String> getAllFacultiesForFeedback = new ArrayList<>();
			if (campusId != null) {
				getAllFacultiesForFeedback = studentFeedbackService
						.getAllFacultiesForFeedback(acadYear, campusId,
								p.getName(), acadSessions);
			} else {
				getAllFacultiesForFeedback = studentFeedbackService
						.getAllFacultiesForFeedback(acadYear, null,
								p.getName(), acadSessions);
			}
			try {
				String zipFileName = "FacultyFeedBackLetters-" + acadYear + "-"
						+ program.getAbbr() + ".zip";
				response.setContentType("Content-type: text/zip");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + zipFileName + "");
				ServletOutputStream out = response.getOutputStream();
				String folderPath = downloadAllFolder + File.separator
						+ "FacultyFeedBackLetters-" + acadYear + "-"
						+ program.getAbbr();

				File folder = new File(folderPath);

				if (!folder.exists()) {

					folder.mkdirs();
				}

				for (String s : getAllFacultiesForFeedback) {
					String returnedFilePath = generatePDFForAllFacultiesInASchool(
							acadYear, userDetails.getProgramId(), s,
							collegeName, term, dean, acadSessions);

					File f = new File(returnedFilePath);
					String copiedFilePath = folderPath + File.separator
							+ f.getName();
					File copiedFile = new File(copiedFilePath);
					FileUtils.copyFile(f, copiedFile);

					f.delete();

				}

				pack(folder.getAbsolutePath(), out);
				FileUtils.deleteDirectory(folder);

			} catch (Exception ex) {

				logger.error("Exception", ex);
			}
			return null;
		}

		else {
			List<Program> programsByFaculty = programService
					.findAllProgramsByFacultyId(acadYear, username,
							acadSessions);

			if (programsByFaculty.size() > 0) {

				String abbr = programsByFaculty.get(0).getAbbr();

				Map<String, List<StudentFeedbackResponse>> mapOfProgramAndAverages = new HashMap<>();
				for (Program prog : programsByFaculty) {

					List<StudentFeedbackResponse> getStudentFeedbackResponseListByProgram = studentFeedbackResponseService
							.findAnswersByFacultyAndProgramAndYear(acadYear,
									username, String.valueOf(prog.getId()),
									acadSessions);

					mapOfProgramAndAverages.put(prog.getProgramName(),
							getStudentFeedbackResponseListByProgram);

				}

				Map<String, Map<String, Double>> mapOfScoresByProgram = new HashMap<>();
				double grandAverage = 0.0;
				for (String prog : mapOfProgramAndAverages.keySet()) {

					List<StudentFeedbackResponse> studentFeedbackResponseList = mapOfProgramAndAverages
							.get(prog);

					double highestScore = 0.0;
					double lowestScore = 0.0;
					double avgScore = 0.0;
					Map<String, Double> mapOfScores = new HashMap<>();
					for (StudentFeedbackResponse sfr : studentFeedbackResponseList) {

						highestScore = highestScore + sfr.getHighestFeedback();
						lowestScore = lowestScore + sfr.getLowestFeedback();
						avgScore = avgScore + sfr.getAverageFeedback();
					}

					highestScore = highestScore
							/ studentFeedbackResponseList.size();
					logger.info("highestScore------------------>"+avgScore);
					lowestScore = lowestScore
							/ studentFeedbackResponseList.size();
					avgScore = avgScore / studentFeedbackResponseList.size();
					grandAverage = grandAverage + avgScore;
					mapOfScores.put("HS", round(highestScore,2));
					mapOfScores.put("LS", round(lowestScore,2));
					mapOfScores.put("AS", round(avgScore,2));

					mapOfScoresByProgram.put(prog, mapOfScores);

				}
				grandAverage = grandAverage / mapOfScoresByProgram.size();
				List<StudentFeedbackResponse> mapOfAveragesDeptWise = studentFeedbackResponseService
						.findAnswersByDept(acadYear, username, acadSessions);

				User u = userService.findByUserName(username);
				String facultyName = u.getFirstname() + " " + u.getLastname();
				String filename = "FacultyFeedbackLetter-"
						+ " "
						+ acadYear
						+ "-"
						+ facultyName
						+ "-"
						+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss",
								Utils.getInIST()) + ".pdf";
				String filePath = generatePDF(collegeName, acadYear,
						mapOfProgramAndAverages, mapOfAveragesDeptWise,
						username, grandAverage, mapOfScoresByProgram, abbr,
						term, dean, acadSessions);

				// String agent = request.getHeader("USER-AGENT");

				response.setContentType("application/x-download");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + filename);

				try {
					InputStream is = null;
					is = new FileInputStream(filePath);

					org.apache.commons.io.IOUtils.copy(is,
							response.getOutputStream());
					response.flushBuffer();
					response.getOutputStream().flush();
					response.getOutputStream().close();

				} catch (Exception ex) {
					logger.error("Exception", ex);
				}
			}
		}

		return "report/downLoadReportLink";
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	public String generatePDFForAllFacultiesInASchool(String acadYear,
			String programId, String username, String collegeName, String term,
			String dean, List<String> acadSessions) {

		List<Program> programsByFaculty = programService
				.findAllProgramsByFacultyId(acadYear, username, acadSessions);

		List<String> filePathList = new ArrayList<>();
		Map<String, String> fileMap = new HashMap<>();
		String filePath = "";
		if (programsByFaculty.size() > 0) {

			String abbr = programsByFaculty.get(0).getAbbr();

			Map<String, List<StudentFeedbackResponse>> mapOfProgramAndAverages = new HashMap<>();
			for (Program prog : programsByFaculty) {

				List<StudentFeedbackResponse> getStudentFeedbackResponseListByProgram = studentFeedbackResponseService
						.findAnswersByFacultyAndProgramAndYear(acadYear,
								username, String.valueOf(prog.getId()),
								acadSessions);

				if (getStudentFeedbackResponseListByProgram.size() > 0) {
					mapOfProgramAndAverages.put(prog.getProgramName(),
							getStudentFeedbackResponseListByProgram);
				}

			}

			Map<String, Map<String, Double>> mapOfScoresByProgram = new HashMap<>();
			double grandAverage = 0.0;
			
			for (String prog : mapOfProgramAndAverages.keySet()) {

				List<StudentFeedbackResponse> studentFeedbackResponseList = mapOfProgramAndAverages
						.get(prog);

				double highestScore = 0.0;
				double lowestScore = 0.0;
				double avgScore = 0.0;
				Map<String, Double> mapOfScores = new HashMap<>();
				for (StudentFeedbackResponse sfr : studentFeedbackResponseList) {

					highestScore = highestScore + sfr.getHighestFeedback();
					lowestScore = lowestScore + sfr.getLowestFeedback();
					avgScore = avgScore + sfr.getAverageFeedback();
				}

				
				
				highestScore = highestScore
						/ studentFeedbackResponseList.size();
				
			
				
				lowestScore = lowestScore / studentFeedbackResponseList.size();
				
				avgScore = avgScore / studentFeedbackResponseList.size();
				grandAverage = grandAverage + avgScore;

				avgScore = round(avgScore, 2);
				grandAverage = round(grandAverage, 2);

				
				mapOfScores.put("HS", round(highestScore,2));
				mapOfScores.put("LS", round(lowestScore,2));
				mapOfScores.put("AS", avgScore);

				mapOfScoresByProgram.put(prog, mapOfScores);

			}
			grandAverage = grandAverage / mapOfScoresByProgram.size();
			
	
			List<StudentFeedbackResponse> mapOfAveragesDeptWise = studentFeedbackResponseService
					.findAnswersByDept(acadYear, username, acadSessions);

			User u = userService.findByUserName(username);
			String facultyName = u.getFirstname() + " " + u.getLastname();
			String filename = "FacultyFeedbackLetter-" + " " + acadYear + "-"
					+ facultyName + "-" + u.getUsername() + "-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".pdf";
			filePath = generatePDF(collegeName, acadYear,
					mapOfProgramAndAverages, mapOfAveragesDeptWise, username,
					grandAverage, mapOfScoresByProgram, abbr, term, dean,
					acadSessions);
			
			// fileMap.put(filename, filePath);

		}
		return filePath;
	}

	public String generatePDF(String collegeName, String acadYear,
			Map<String, List<StudentFeedbackResponse>> mapOfProgramAndAverages,
			List<StudentFeedbackResponse> mapOfAveragesDeptWise,
			String username, Double grandAverage,
			Map<String, Map<String, Double>> mapOfScoresByProgram, String abbr,
			String term, String dean, List<String> acadSessions) {
		Document document = new Document();
		String filePath = "";
		try {
			User u = userService.findByUserName(username);

			String facultyName = u.getFirstname() + " " + u.getLastname();
			filePath = "FacultyFeedbackLetter-" + " " + acadYear + "-"
					+ facultyName + "-" + u.getUsername() + "-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".pdf";
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();
			
			List<Course> getModulesByFaculty = courseService
					.getModulesByFaculty(u.getUsername(), acadYear,
							acadSessions);

			Set<String> moduleName = new HashSet<>();
			for (Course c : getModulesByFaculty) {

				moduleName.add(c.getModuleName());

			}
			List<String> moduleProgramList = new ArrayList<>();
			for (String m : moduleName) {
				String moduleProgram = m + "(";
				for (Course c : getModulesByFaculty) {

					if (m.equals(c.getModuleName())) {

						moduleProgram = moduleProgram + c.getProgramName()
								+ ",";
					}
				}
				moduleProgram = moduleProgram + ")";
				StringBuilder sb = new StringBuilder(moduleProgram);
				sb.deleteCharAt(moduleProgram.length() - 2);

				moduleProgram = sb.toString();
				moduleProgramList.add(moduleProgram);
			}

			String modules = "";

			for (int i = 0; i < moduleProgramList.size(); i++) {

				if (i == moduleProgramList.size() - 1) {
					modules = modules + moduleProgramList.get(i);
				} else {
					modules = modules + moduleProgramList.get(i) + ",";
				}
			}

		
			Paragraph header = new Paragraph();

			
			header.add(new Paragraph("SVKM’S NMIMS (Deemed-to-be-University)",
					catFont));

			addEmptyLine(header, 1);

			header.add(new Paragraph(collegeName, catFont));

			header.setAlignment(Element.ALIGN_CENTER);

			Paragraph date = new Paragraph();

			String currentDate = Utils.formatDate("dd/MM/yyyy",
					Utils.getInIST());

			date.add(new Paragraph("Date: " + currentDate, textFont));
			date.setAlignment(Element.ALIGN_RIGHT);

			Paragraph greetText = new Paragraph();
			greetText.add(new Paragraph("Dear Mr/Ms " + facultyName + ",",
					textFont));
			greetText.setAlignment(Element.ALIGN_LEFT);

			Paragraph content = new Paragraph();

			content.add(new Paragraph(
					"I sincerely appreciate all the efforts taken by you in the academic activities of our Institute.",
					textFont));

			addEmptyLine(content, 1);

			content.add(new Paragraph(
					"Your feedbacks on the subject(s) taught by you in "
							+ term
							+ " for the Academic Year "
							+ acadYear
							+ " have been discussed with you by the Dean. "
							+ " It may provide insight in revising your teaching plan while handling further teaching assignments."
							+ " The highest /lowest /average feedback received in this area & Programme are mentioned below for your reference: 7 point scale. ",
					textFont));

			addEmptyLine(content, 1);

			List<String> programCodes = new ArrayList<>();
			for (String programName : mapOfProgramAndAverages.keySet()) {

				programCodes
						.add(programName.substring(
								programName.lastIndexOf("-") + 1,
								programName.length()));
			}

			PdfPTable tableProgramWise = createTableProgramWise(
					mapOfProgramAndAverages, mapOfScoresByProgram);

			tableProgramWise.setSpacingAfter(10);

			PdfPTable tableDeptWise = createTableDeptWise(mapOfAveragesDeptWise);
			tableDeptWise.setSpacingBefore(10);

			tableDeptWise.setSpacingAfter(20);

			Paragraph otherContent = new Paragraph();

			otherContent.add(new Paragraph("Yourself Feedback –", boldFont));
			otherContent.setSpacingAfter(1);

			if (grandAverage >= 6 && grandAverage <= 7) {

				otherContent.add(new Paragraph(
						"Let me congratulate you for the excellent feedback in Programs: "
								+ programCodes + " And under modules :"
								+ modules, textFont));
			} else if (grandAverage >= 4 && grandAverage < 6) {
				otherContent.add(new Paragraph(
						"You got acceptable feedback in Programs: "
								+ programCodes + " And under modules :"
								+ modules, textFont));
			} else {

				otherContent.add(new Paragraph(
						"Kindly note that your feedback falls below expectations in Programs: "
								+ programCodes + " And under modules :"
								+ modules, textFont));
			}

			addEmptyLine(otherContent, 1);

			otherContent
					.add(new Paragraph(
							"I once again thank you for your efforts in the academic activities of this Institute and look forward to your continued support.",
							textFont));

			addEmptyLine(otherContent, 1);

			otherContent.add(new Paragraph("With regards,", textFont));
			otherContent.setSpacingAfter(1);
			otherContent.add(new Paragraph("Yours sincerely,", textFont));
			otherContent.setSpacingAfter(1);
			otherContent.add(new Paragraph(dean, boldFont));
			otherContent.setSpacingAfter(1);
			otherContent.add(new Paragraph("Dean," + abbr, boldFont));

			document.add(header);
			document.add(date);
			document.add(greetText);
			document.add(content);

			document.add(tableProgramWise);

			if (mapOfAveragesDeptWise.size() > 0) {
				document.add(tableDeptWise);
			}
			document.add(otherContent);

			document.close();

		} catch (Exception ex) {

			logger.error("Exception", ex);
		}
		return filePath;
	}

	//Modulewise report working code for KmpSol by suraj
	/*public String generatePDF(String collegeName, String acadYear,
			Map<String, List<StudentFeedbackResponse>> mapOfProgramAndAverages,
			List<StudentFeedbackResponse> mapOfAveragesDeptWise,
			String username, Double grandAverage,
			Map<String, Map<String, Double>> mapOfScoresByProgram, String abbr,
			String term, String dean, List<String> acadSessions) {
		Document document = new Document();
		String filePath = "";
		try {
			User u = userService.findByUserName(username);

			String facultyName = u.getFirstname() + " " + u.getLastname();
			filePath = "FacultyFeedbackLetter-" + " " + acadYear + "-"
					+ facultyName + "-" + u.getUsername() + "-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".pdf";
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			List<Course> getModulesByFaculty = courseService
					.getModulesByFaculty(u.getUsername(), acadYear,
							acadSessions);

			logger.info("getModulesByFaculty ------------------->"
					+ getModulesByFaculty);

			Set<String> moduleName = new HashSet<>();
			List<StudentFeedbackResponse> getStudentFeedbackResponseListByModule = new ArrayList<StudentFeedbackResponse>();
			List<StudentFeedbackResponseService> modulewiseresponse = new ArrayList<StudentFeedbackResponseService>();

			
			Map<String, String> mapOfAvergaeRangeWithModule = new HashMap<>();
			Map<String, String> mapOfAvergaeRangeWithProgram = new HashMap<>();
			

			StringBuilder sbEF = new StringBuilder();
			StringBuilder sbAF = new StringBuilder();
			StringBuilder sbPF = new StringBuilder();

			StringBuilder sbEFProg = new StringBuilder();
			StringBuilder sbAFProg = new StringBuilder();
			StringBuilder sbPFProg = new StringBuilder();

			for (Course c : getModulesByFaculty) {

				moduleName.add(c.getModuleName());

				logger.info("Course module id-------------------->"
						+ c.getModuleId());

				getStudentFeedbackResponseListByModule = studentFeedbackResponseService
						.findAnswersByFacultyAndModuleAndYear(acadYear,
								u.getUsername(),
								String.valueOf(c.getModuleId()), acadSessions);

				if (getStudentFeedbackResponseListByModule.size() > 0) {
					double moduleaverage = getStudentFeedbackResponseListByModule
							.get(0).getAverageFeedback();
					
					logger.info("moduleaverage------------------->>"+moduleaverage);
					
					String modName = getStudentFeedbackResponseListByModule
							.get(0).getModuleName()
							+ "("
							+ c.getProgramName()
							+ "),";
					String programName = c.getProgramName() + ",";

					logger.info("programName-------------->><" + programName);

					if (moduleaverage >= 6 && moduleaverage <= 7) {
						logger.info("EF Entered with avg"+moduleaverage);
						mapOfAvergaeRangeWithModule.put("EF",
								sbEF.append(modName).toString());
						mapOfAvergaeRangeWithProgram.put("EF",
								sbEFProg.append(programName).toString());

					} else if (moduleaverage >= 4 && moduleaverage < 6) {
						logger.info("AF Entered with avg"+moduleaverage);
						mapOfAvergaeRangeWithModule.put("AF",
								sbAF.append(modName).toString());
						mapOfAvergaeRangeWithProgram.put("AF",
								sbAFProg.append(programName).toString());
					} else{
						mapOfAvergaeRangeWithModule.put("PF",
								sbPF.append(modName).toString());
						logger.info("PF Entered with avg"+moduleaverage);
						mapOfAvergaeRangeWithProgram.put("PF",
								sbPFProg.append(programName).toString());
					}
				}

			}
			
			List<String> strProgEF = Arrays.asList(sbEFProg.toString().split(","));
			List<String> strProgAF = Arrays.asList(sbAFProg.toString().split(","));
			List<String> strProgPF = Arrays.asList(sbPFProg.toString().split(","));

			if (mapOfAvergaeRangeWithProgram.containsKey("EF")) {
				List<String> listEF = strProgEF.stream().distinct()
						.collect(Collectors.toList());

				String newStrEF = String.join(",", listEF);
				mapOfAvergaeRangeWithProgram.put("EF", newStrEF);
			}
			if (mapOfAvergaeRangeWithProgram.containsKey("AF")) {
				List<String> listAF = strProgAF.stream().distinct()
						.collect(Collectors.toList());
				String newStrAF = String.join(",", listAF);
				mapOfAvergaeRangeWithProgram.put("AF", newStrAF);
			}
			if (mapOfAvergaeRangeWithProgram.containsKey("PF")) {
				logger.info("sb prog pf entered"+mapOfAvergaeRangeWithProgram);
				List<String> listPF = strProgPF.stream().distinct()
						.collect(Collectors.toList());
				String newStrPF = String.join(",", listPF);
				mapOfAvergaeRangeWithProgram.put("PF", newStrPF);
			}

			logger.info("mapOfAverg11aeRange------------------->"
					+ mapOfAvergaeRangeWithModule);

		
			logger.info("getStudentFeedbackResponseListByModule------>>>"
					+ getStudentFeedbackResponseListByModule);

			// logger.info("moduleaverage ------------------->"+moduleaverage);
			List<String> moduleProgramList = new ArrayList<>();

			for (String m : moduleName) {
				String moduleProgram = m + "(";
				for (Course c : getModulesByFaculty) {

					if (m.equals(c.getModuleName())) {

						moduleProgram = moduleProgram + c.getProgramName()
								+ ",";
					}

				}

				logger.info("moduleProgram ------------------->"
						+ moduleProgram);

				moduleProgram = moduleProgram + ")";
				StringBuilder sb = new StringBuilder(moduleProgram);
				sb.deleteCharAt(moduleProgram.length() - 2);

				moduleProgram = sb.toString();
				moduleProgramList.add(moduleProgram);
			}

			logger.info("moduleProgramList ------------------->"
					+ moduleProgramList);

			String modules = "";

			for (int i = 0; i < moduleProgramList.size(); i++) {

				if (i == moduleProgramList.size() - 1) {
					modules = modules + moduleProgramList.get(i);
				} else {
					modules = modules + moduleProgramList.get(i) + ",";
				}
			}

			logger.info("modules modules ------------------->" + modules);

		
			Paragraph header = new Paragraph();

			// Lets write a big header
			header.add(new Paragraph("SVKM’S NMIMS (Deemed-to-be-University)",
					catFont));

			addEmptyLine(header, 1);

			header.add(new Paragraph(collegeName, catFont));

			header.setAlignment(Element.ALIGN_CENTER);

			Paragraph date = new Paragraph();

			String currentDate = Utils.formatDate("dd/MM/yyyy",
					Utils.getInIST());

			date.add(new Paragraph("Date: " + currentDate, textFont));
			date.setAlignment(Element.ALIGN_RIGHT);

			Paragraph greetText = new Paragraph();
			greetText.add(new Paragraph("Dear Mr/Ms " + facultyName + ",",
					textFont));
			greetText.setAlignment(Element.ALIGN_LEFT);

			Paragraph content = new Paragraph();

			content.add(new Paragraph(
					"I sincerely appreciate all the efforts taken by you in the academic activities of our Institute.",
					textFont));

			addEmptyLine(content, 1);

			content.add(new Paragraph(
					"Your feedbacks on the subject(s) taught by you in "
							+ term
							+ " for the Academic Year "
							+ acadYear
							+ " have been discussed with you by the Dean. "
							+ " It may provide insight in revising your teaching plan while handling further teaching assignments."
							+ " The highest /lowest /average feedback received in this area & Programme are mentioned below for your reference: 7 point scale. ",
					textFont));

			addEmptyLine(content, 1);

			List<String> programCodes = new ArrayList<>();
			for (String programName : mapOfProgramAndAverages.keySet()) {

				programCodes
						.add(programName.substring(
								programName.lastIndexOf("-") + 1,
								programName.length()));
			}

			PdfPTable tableProgramWise = createTableProgramWise(
					mapOfProgramAndAverages, mapOfScoresByProgram);

			tableProgramWise.setSpacingAfter(10);

			PdfPTable tableDeptWise = createTableDeptWise(mapOfAveragesDeptWise);
			tableDeptWise.setSpacingBefore(10);

			tableDeptWise.setSpacingAfter(20);

			Paragraph otherContent = new Paragraph();
			Font zapfdingbats = new Font();
			Chunk bullet = new Chunk("\u2022", zapfdingbats);
			
			otherContent
					.add(new Paragraph(
							"Feedback Parameters: 6 – 7 (Excellent), 4 – 5.99 (Acceptable), 2 – 3.99 (Below Expectation).",
							boldFont));
			
		
			otherContent.add(new Paragraph("Yourself Feedback –", boldFont));
			otherContent.setSpacingAfter(1);
			logger.info("grandAverage--------->>>>" + grandAverage
					+ "programCodes -->" + programCodes + "Module--> "
					+ modules);
			
			for (String map : mapOfAvergaeRangeWithProgram.keySet()) {
				
			
				if ("EF".equals(map)) {
					otherContent.add(new Paragraph(bullet + 
							" Let me congratulate you for the excellent feedback in Programs: "
									+ mapOfAvergaeRangeWithProgram.get("EF")
									+ " And under modules :"

									+ mapOfAvergaeRangeWithModule.get("EF")
									+ "\n– (range 6-7)", textFont));
				} else if ("AF".equals(map)) {
					otherContent.add(new Paragraph(bullet + 
							" You got acceptable feedback in Programs: "
									+ mapOfAvergaeRangeWithProgram.get("AF")
									+ " And under modules :"
									+ mapOfAvergaeRangeWithModule.get("AF")
									+ "\n– (range 4-5.99)", textFont));
				}else{
					logger.info("pf entered 0-0000");
					otherContent.add(new Paragraph(bullet + 
							" Kindly note that your feedback falls below expectations in Programs: "
									+ mapOfAvergaeRangeWithProgram.get("PF")
									+ " And under modules :"
									+ mapOfAvergaeRangeWithModule.get("PF")
									+ "\n-range(2-3.99)", textFont));
				}
			}

			
			 * if (grandAverage >= 6 && grandAverage <= 7 ) {
			 * 
			 * otherContent.add(new Paragraph(
			 * "Let me congratulate you for the excellent feedback in Programs: "
			 * + programCodes + " And under modules :" +
			 * modules+"\n– (range 6-7)", textFont));
			 * 
			 * 
			 * 
			 * } else if (grandAverage >= 4 && grandAverage <=6) {
			 * otherContent.add(new Paragraph(
			 * "You got acceptable feedback in Programs: " + programCodes +
			 * " And under modules :" + modules+"\n– (range 4-5.99)",
			 * textFont)); } else {
			 * 
			 * otherContent.add(new Paragraph(
			 * "Kindly note that your feedback falls below expectations in Programs: "
			 * + programCodes + " And under modules :" +
			 * modules+"\n-range(2-3.99)", textFont)); }
			 
			addEmptyLine(otherContent, 1);

			otherContent
					.add(new Paragraph(
							"I once again thank you for your efforts in the academic activities of this Institute and look forward to your continued support.",
							textFont));

			addEmptyLine(otherContent, 1);

			otherContent.add(new Paragraph("With regards,", textFont));
			otherContent.setSpacingAfter(1);
			otherContent.add(new Paragraph("Yours sincerely,", textFont));
			otherContent.setSpacingAfter(1);
			otherContent.add(new Paragraph(dean, boldFont));
			otherContent.setSpacingAfter(1);
			otherContent.add(new Paragraph("Dean," + abbr, boldFont));

			document.add(header);
			document.add(date);
			document.add(greetText);
			document.add(content);

			document.add(tableProgramWise);

			if (mapOfAveragesDeptWise.size() > 0) {
				document.add(tableDeptWise);
			}
			document.add(otherContent);

			document.close();

		} catch (Exception ex) {

			logger.error("Exception", ex);
		}
		return filePath;
	}*/
	
	
	
	private static PdfPTable createTableDeptWise(
			List<StudentFeedbackResponse> mapOfAveragesDeptWise)
			throws BadElementException {
		PdfPTable table = new PdfPTable(mapOfAveragesDeptWise.size() + 1);
		try {

			PdfPCell c1 = new PdfPCell(new Phrase("Department-Wise:"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			for (StudentFeedbackResponse sfr : mapOfAveragesDeptWise) {
				c1 = new PdfPCell(new Phrase(sfr.getDept()));
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c1);
			}

			table.setHeaderRows(1);

			PdfPCell c2 = new PdfPCell(new Phrase(
					"Highest Feedback in Programme"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c2);

			for (StudentFeedbackResponse sfr : mapOfAveragesDeptWise) {

				c2 = new PdfPCell(new Phrase(String.valueOf(sfr
						.getHighestFeedback())));
				c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c2);
			}

			PdfPCell c3 = new PdfPCell(new Phrase(
					"Lowest Feedback in Programme"));
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c3);
			for (StudentFeedbackResponse sfr : mapOfAveragesDeptWise) {
				c3 = new PdfPCell(new Phrase(String.valueOf(sfr
						.getLowestFeedback())));
				c3.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c3);
			}

			PdfPCell c4 = new PdfPCell(new Phrase(
					"Average Feedback in Programme"));
			c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c4);

			for (StudentFeedbackResponse sfr : mapOfAveragesDeptWise) {
				
				c4 = new PdfPCell(new Phrase(String.valueOf(sfr
						.getAverageFeedback())));
				c4.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c4);
			}

		} catch (Exception ex) {

		}
		table.setWidthPercentage(100f);
		return table;
	}

	private static PdfPTable createTableProgramWise(
			Map<String, List<StudentFeedbackResponse>> mapOfProgramAndAverages,
			Map<String, Map<String, Double>> mapOfScoresByProgram)
			throws BadElementException {
		PdfPTable table = new PdfPTable(mapOfProgramAndAverages.size() + 1);
		try {

			// t.setBorderColor(BaseColor.GRAY);
			// t.setPadding(4);
			// t.setSpacing(4);
			// t.setBorderWidth(1);

			PdfPCell c1 = new PdfPCell(new Phrase("Programme Wise:"));
			c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c1);

			for (String program : mapOfProgramAndAverages.keySet()) {
				c1 = new PdfPCell(new Phrase(program.substring(
						program.lastIndexOf("-") + 1, program.length())));
			
				c1.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c1);
			}
			
			

			table.setHeaderRows(1);

			PdfPCell c2 = new PdfPCell(new Phrase(
					"Highest Feedback in Programme"));
			c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c2);

			// table.addCell("Highest Feedback in Programme");

			for (String program : mapOfProgramAndAverages.keySet()) {

				c2 = new PdfPCell(new Phrase(
						String.valueOf(mapOfScoresByProgram.get(program).get(
								"HS"))));
				
				c2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c2);
				
			}
			
			PdfPCell c3 = new PdfPCell(new Phrase(
					"Lowest Feedback in Programme"));
			c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c3);
			for (String program : mapOfProgramAndAverages.keySet()) {

				c3 = new PdfPCell(new Phrase(
						String.valueOf(mapOfScoresByProgram.get(program).get(
								"LS"))));
				
				c3.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c3);
			}

			PdfPCell c4 = new PdfPCell(new Phrase(
					"Average Feedback in Programme"));
			c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c4);

			for (String program : mapOfProgramAndAverages.keySet()) {

				c4 = new PdfPCell(new Phrase(
						String.valueOf(mapOfScoresByProgram.get(program).get(
								"AS"))));
			
				c4.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(c4);
			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}

		table.setWidthPercentage(100f);
		return table;

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

	/*@RequestMapping(value = "/downloadFeedbackReportRangeWiseNew", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportRangeWiseNew(Model m, Principal p,
			HttpServletResponse response,
			@RequestParam String term1AcadSession,
			@RequestParam String term2AcadSession,
			@RequestParam long courseQuestnCount,
			@RequestParam String acadYear, @RequestParam String campusId,
			RedirectAttributes r)
			throws URIException {

		String username = p.getName();

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Range", "Term1_faculty", "Term1_course", "Term2_faculty",
				"Term2_course"));

		List<String> term1acadList = Arrays.asList(term1AcadSession
				.split("\\s*,\\s*"));
		List<String> term2acadList = Arrays.asList(term2AcadSession
				.split("\\s*,\\s*"));

		List<String> acadYearSplit = Arrays.asList(acadYear.split("-"));

		String acadYear1 = acadYearSplit.get(0);
		String acadYear2 = acadYearSplit.get(1);
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedBy(username);
        if(feedList.size() > 0){


		List<Course> term1CourseList = courseService.findByAcadSession(
				term1acadList, acadYear1, acadYear2, campusId);
		List<Course> term2CourseList = courseService.findByAcadSession(
				term2acadList, acadYear1, acadYear2, campusId);
		List<String> courseT1 = new ArrayList<String>();
		List<String> courseT2 = new ArrayList<String>();
		List<UserCourse> facultyT1 = new ArrayList<UserCourse>();
		List<UserCourse> facultyT2 = new ArrayList<UserCourse>();
		/*
		 * List<String> facultyIdT1 = new ArrayList<String>(); List<String>
		 * facultyIdT2 = new ArrayList<String>();
		 /
		for (Course co1 : term1CourseList) {
			courseT1.add(co1.getId().toString());

		}

		for (Course co2 : term2CourseList) {
			courseT2.add(co2.getId().toString());
		}

		facultyT1 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT1);
		facultyT2 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT2);

		/*
		 * for(UserCourse uc1:userCourseT1){
		 * facultyIdT1.add(uc1.getFacultyId().toString()); }
		 * 
		 * for(UserCourse uc2:userCourseT2){
		 * facultyIdT2.add(uc2.getFacultyId().toString()); }
		 /

		/*List<Feedback> ValidFeedbacks = feedbackService
				.findAllActiveByUsername(campusId, acadYear1, acadYear2);/
		List<Feedback> ValidFeedbacks = feedbackService
				.findAllActiveByUsername(campusId, acadYear1, acadYear2, username);

		List<String> feedbackIds = new ArrayList<>();
		for (Feedback f : ValidFeedbacks) {
			feedbackIds.add(String.valueOf(f.getId()));

		}

		List<FeedbackQuestion> feedbackQuestionListByFeedbackIds = feedbackQuestionService
				.findAllQuestionsByFeedbackId(feedbackIds);
		Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndListOfQuestions = new HashMap<>();
		for (String id : feedbackIds) {

			List<FeedbackQuestion> fqList = new ArrayList<>();

			for (FeedbackQuestion fq : feedbackQuestionListByFeedbackIds) {

				if (id.equals(String.valueOf(fq.getFeedbackId()))) {

					fqList.add(fq);
				}
			}

			mapOfFeedbackIdAndListOfQuestions.put(id, fqList);
		}

		List<FeedbackQuestion> questionList = new ArrayList<FeedbackQuestion>();
		Map<String, List<FeedbackQuestion>> mapfeedQuestions = new HashMap<String, List<FeedbackQuestion>>();
		List<FeedbackQuestion> facultyquestions = new ArrayList<FeedbackQuestion>();
		List<FeedbackQuestion> coursequestions = new ArrayList<FeedbackQuestion>();
		List<String> facultyquestionsId = new ArrayList<String>();
		List<String> coursequestionsId = new ArrayList<String>();
		List<String> avgFacultyT1 = new ArrayList<String>();
		List<String> avgCourseT1 = new ArrayList<String>();
		List<String> avgFacultyT2 = new ArrayList<String>();
		List<String> avgCourseT2 = new ArrayList<String>();
		for (Feedback feed : ValidFeedbacks) {
			questionList = mapOfFeedbackIdAndListOfQuestions.get(String
					.valueOf(feed.getId()));
			if (courseQuestnCount < questionList.size()) {
				facultyquestions = questionList.subList(0,
						(int) courseQuestnCount);
				for (FeedbackQuestion fq1 : facultyquestions) {
					facultyquestionsId.add(fq1.getId().toString());
				}
				coursequestions = questionList.subList((int) courseQuestnCount,
						questionList.size());
				for (FeedbackQuestion fq2 : coursequestions) {
					coursequestionsId.add(fq2.getId().toString());
				}
				facultyquestions.remove(feed);
				coursequestions.clear();
				questionList.clear();
			}
		}

		if (facultyquestionsId.size() == 0) {
			facultyquestionsId.add("");
		}
		if (coursequestionsId.size() == 0) {
			coursequestionsId.add("");
		}

		List<String> facultyTerm1List = new ArrayList<>();
		List<String> facultyTerm2List = new ArrayList<>();
		for (UserCourse uc1 : facultyT1) {

			if (!facultyTerm1List.contains(uc1.getUsername())) {
				facultyTerm1List.add(uc1.getUsername());
			}

		}

		/*avgFacultyT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, facultyquestionsId);
		avgCourseT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, coursequestionsId);/
		
		avgFacultyT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, facultyquestionsId, username);
		avgCourseT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, coursequestionsId, username);

		for (UserCourse uc2 : facultyT2) {

			if (!facultyTerm2List.contains(uc2.getUsername())) {
				facultyTerm2List.add(uc2.getUsername());
			}
		}

		/*avgFacultyT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, facultyquestionsId);
		avgCourseT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, coursequestionsId);/
		
		avgFacultyT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, facultyquestionsId, username);
		avgCourseT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, coursequestionsId, username);

		avgFacultyT1.removeAll(Collections.singleton(null));
		avgCourseT1.removeAll(Collections.singleton(null));
		avgFacultyT2.removeAll(Collections.singleton(null));
		avgCourseT2.removeAll(Collections.singleton(null));

		// logger.info(avgFacultyT1.size()+"facc"+avgCourseT1.size());
		// logger.info(avgFacultyT2.size()+"facc"+avgCourseT2.size());
		List<Integer> avgfacrangeT1 = calculateRange(avgFacultyT1);
		List<Integer> avgcourangeT1 = calculateRange(avgCourseT1);
		List<Integer> avgfacrangeT2 = calculateRange(avgFacultyT2);
		List<Integer> avgcourangeT2 = calculateRange(avgCourseT2);
		List<String> rangeList = new ArrayList<String>();

		rangeList.add("0 to 1");
		rangeList.add("1 to 1.99");
		rangeList.add("2 to 2.99");
		rangeList.add("3 to 3.99");
		rangeList.add("4 to 4.99");
		rangeList.add("5 to 5.99");
		rangeList.add("6 to 7");

		List<Map<String, Object>> listOfMap = new ArrayList<>();
		for (int i = 0; i < avgfacrangeT1.size(); i++) {
			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			// logger.info("valuuuuuu================="+avgfacrangeT1.get(i));
			mapOfAvg.put("Sr.No", i + 1);
			mapOfAvg.put("Range", rangeList.get(i));
			mapOfAvg.put("Term1_faculty", avgfacrangeT1.get(i));
			mapOfAvg.put("Term1_course", avgcourangeT1.get(i));
			mapOfAvg.put("Term2_faculty", avgfacrangeT2.get(i));
			mapOfAvg.put("Term2_course", avgcourangeT2.get(i));
			listOfMap.add(mapOfAvg);

		}

		Map<String, Object> mapOfAvg2 = new HashMap<String, Object>();
		mapOfAvg2.put("Sr.No", "");
		mapOfAvg2.put("Range", "Total");
		mapOfAvg2.put("Term1_faculty",
				avgfacrangeT1.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term1_course",
				avgcourangeT1.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term2_faculty",
				avgfacrangeT2.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term2_course",
				avgcourangeT2.stream().mapToInt(Integer::intValue).sum());
		listOfMap.add(mapOfAvg2);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}*/
	
	//amey 14-10-2020
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/downloadFeedbackReportRangeWiseNew", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String downloadFeedbackReportRangeWiseNew(Model m, Principal p,
			HttpServletResponse response,
			@RequestParam String term1AcadSession,
			@RequestParam String term2AcadSession,
			@RequestParam long courseQuestnCount,
			@RequestParam String acadYear, @RequestParam String campusId,
			@RequestParam String feedbackType, RedirectAttributes r)
			throws URIException {

		String username = p.getName();

		m.addAttribute("webPage", new WebPage("downlaodReport",
				"Download Report", false, false));

		List<String> headers = new ArrayList<String>(Arrays.asList("Sr.No",
				"Range", "Term1_faculty", "Term1_course", "Term2_faculty",
				"Term2_course"));

		List<String> term1acadList = Arrays.asList(term1AcadSession
				.split("\\s*,\\s*"));
		List<String> term2acadList = Arrays.asList(term2AcadSession
				.split("\\s*,\\s*"));

		List<String> acadYearSplit = Arrays.asList(acadYear.split("-"));

		String acadYear1 = acadYearSplit.get(0);
		String acadYear2 = acadYearSplit.get(1);
		
		List<Feedback> feedList = feedbackService.findAllActiveByCreatedByAndType(username, feedbackType);
        if(feedList.size() > 0){


		List<Course> term1CourseList = courseService.findByAcadSession(
				term1acadList, acadYear1, acadYear2, campusId);
		List<Course> term2CourseList = courseService.findByAcadSession(
				term2acadList, acadYear1, acadYear2, campusId);
		List<String> courseT1 = new ArrayList<String>();
		List<String> courseT2 = new ArrayList<String>();
		List<UserCourse> facultyT1 = new ArrayList<UserCourse>();
		List<UserCourse> facultyT2 = new ArrayList<UserCourse>();
		/*
		 * List<String> facultyIdT1 = new ArrayList<String>(); List<String>
		 * facultyIdT2 = new ArrayList<String>();
		 */
		for (Course co1 : term1CourseList) {
			courseT1.add(co1.getId().toString());

		}

		for (Course co2 : term2CourseList) {
			courseT2.add(co2.getId().toString());
		}
		
		if(courseT1.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}
		if(courseT2.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}

		facultyT1 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT1);
		facultyT2 = userCourseService.getUserbasedOnMultipleCourse(
				"ROLE_FACULTY", courseT2);

		/*
		 * for(UserCourse uc1:userCourseT1){
		 * facultyIdT1.add(uc1.getFacultyId().toString()); }
		 * 
		 * for(UserCourse uc2:userCourseT2){
		 * facultyIdT2.add(uc2.getFacultyId().toString()); }
		 */

		/*List<Feedback> ValidFeedbacks = feedbackService
				.findAllActiveByUsername(campusId, acadYear1, acadYear2);*/
		List<Feedback> ValidFeedbacks = feedbackService
				.findAllActiveByUsernameAndFeedbackType(campusId, acadYear1, acadYear2, username, feedbackType);
		
		if(ValidFeedbacks.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}
		
		List<String> feedbackIds = new ArrayList<>();
		for (Feedback f : ValidFeedbacks) {
			feedbackIds.add(String.valueOf(f.getId()));

		}
		
		if(feedbackIds.size() == 0){
		      setNote(r,"No feedback found");
		      return "redirect:/downloadReportMyCourseStudentForm";
		}

		List<FeedbackQuestion> feedbackQuestionListByFeedbackIds = feedbackQuestionService
				.findAllQuestionsByFeedbackId(feedbackIds);
		Map<String, List<FeedbackQuestion>> mapOfFeedbackIdAndListOfQuestions = new HashMap<>();
		for (String id : feedbackIds) {

			List<FeedbackQuestion> fqList = new ArrayList<>();

			for (FeedbackQuestion fq : feedbackQuestionListByFeedbackIds) {

				if (id.equals(String.valueOf(fq.getFeedbackId()))) {

					fqList.add(fq);
				}
			}

			mapOfFeedbackIdAndListOfQuestions.put(id, fqList);
		}

		List<FeedbackQuestion> questionList = new ArrayList<FeedbackQuestion>();
		Map<String, List<FeedbackQuestion>> mapfeedQuestions = new HashMap<String, List<FeedbackQuestion>>();
		List<FeedbackQuestion> facultyquestions = new ArrayList<FeedbackQuestion>();
		List<FeedbackQuestion> coursequestions = new ArrayList<FeedbackQuestion>();
		List<String> facultyquestionsId = new ArrayList<String>();
		List<String> coursequestionsId = new ArrayList<String>();
		List<String> avgFacultyT1 = new ArrayList<String>();
		List<String> avgCourseT1 = new ArrayList<String>();
		List<String> avgFacultyT2 = new ArrayList<String>();
		List<String> avgCourseT2 = new ArrayList<String>();
		for (Feedback feed : ValidFeedbacks) {
			questionList = mapOfFeedbackIdAndListOfQuestions.get(String
					.valueOf(feed.getId()));
			if (courseQuestnCount < questionList.size()) {
				facultyquestions = questionList.subList(0,
						(int) courseQuestnCount);
				for (FeedbackQuestion fq1 : facultyquestions) {
					facultyquestionsId.add(fq1.getId().toString());
				}
				coursequestions = questionList.subList((int) courseQuestnCount,
						questionList.size());
				for (FeedbackQuestion fq2 : coursequestions) {
					coursequestionsId.add(fq2.getId().toString());
				}
				facultyquestions.remove(feed);
				coursequestions.clear();
				questionList.clear();
			}
		}

		if (facultyquestionsId.size() == 0) {
			facultyquestionsId.add("");
		}
		if (coursequestionsId.size() == 0) {
			coursequestionsId.add("");
		}

		List<String> facultyTerm1List = new ArrayList<>();
		List<String> facultyTerm2List = new ArrayList<>();
		for (UserCourse uc1 : facultyT1) {

			if (!facultyTerm1List.contains(uc1.getUsername())) {
				facultyTerm1List.add(uc1.getUsername());
			}

		}

		/*avgFacultyT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, facultyquestionsId);
		avgCourseT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, coursequestionsId);*/
		
		avgFacultyT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, facultyquestionsId, username);
		avgCourseT1 = studentFeedbackResponseService.getAvgAnswer1(courseT1,
				facultyTerm1List, coursequestionsId, username);

		for (UserCourse uc2 : facultyT2) {

			if (!facultyTerm2List.contains(uc2.getUsername())) {
				facultyTerm2List.add(uc2.getUsername());
			}
		}

		/*avgFacultyT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, facultyquestionsId);
		avgCourseT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, coursequestionsId);*/
		
		avgFacultyT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, facultyquestionsId, username);
		avgCourseT2 = studentFeedbackResponseService.getAvgAnswer1(courseT2,
				facultyTerm2List, coursequestionsId, username);

		avgFacultyT1.removeAll(Collections.singleton(null));
		avgCourseT1.removeAll(Collections.singleton(null));
		avgFacultyT2.removeAll(Collections.singleton(null));
		avgCourseT2.removeAll(Collections.singleton(null));

		// logger.info(avgFacultyT1.size()+"facc"+avgCourseT1.size());
		// logger.info(avgFacultyT2.size()+"facc"+avgCourseT2.size());
		List<Integer> avgfacrangeT1 = calculateRange(avgFacultyT1);
		List<Integer> avgcourangeT1 = calculateRange(avgCourseT1);
		List<Integer> avgfacrangeT2 = calculateRange(avgFacultyT2);
		List<Integer> avgcourangeT2 = calculateRange(avgCourseT2);
		List<String> rangeList = new ArrayList<String>();

		rangeList.add("0 to 1");
		rangeList.add("1 to 1.99");
		rangeList.add("2 to 2.99");
		rangeList.add("3 to 3.99");
		rangeList.add("4 to 4.99");
		rangeList.add("5 to 5.99");
		rangeList.add("6 to 7");

		List<Map<String, Object>> listOfMap = new ArrayList<>();
		for (int i = 0; i < avgfacrangeT1.size(); i++) {
			Map<String, Object> mapOfAvg = new HashMap<String, Object>();
			// logger.info("valuuuuuu================="+avgfacrangeT1.get(i));
			mapOfAvg.put("Sr.No", i + 1);
			mapOfAvg.put("Range", rangeList.get(i));
			mapOfAvg.put("Term1_faculty", avgfacrangeT1.get(i));
			mapOfAvg.put("Term1_course", avgcourangeT1.get(i));
			mapOfAvg.put("Term2_faculty", avgfacrangeT2.get(i));
			mapOfAvg.put("Term2_course", avgcourangeT2.get(i));
			listOfMap.add(mapOfAvg);

		}

		Map<String, Object> mapOfAvg2 = new HashMap<String, Object>();
		mapOfAvg2.put("Sr.No", "");
		mapOfAvg2.put("Range", "Total");
		mapOfAvg2.put("Term1_faculty",
				avgfacrangeT1.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term1_course",
				avgcourangeT1.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term2_faculty",
				avgfacrangeT2.stream().mapToInt(Integer::intValue).sum());
		mapOfAvg2.put("Term2_course",
				avgcourangeT2.stream().mapToInt(Integer::intValue).sum());
		listOfMap.add(mapOfAvg2);

		ExcelCreater excelCreater = new ExcelCreater();
		InputStream is = null;
		try {
			String fileName = "StudentFeedbackReport-"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			String filePath = downloadAllFolder + File.separator
					+ "StudentFeedbackReport"
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";
			excelCreater.CreateExcelFile(listOfMap, headers, filePath);

			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ fileName);
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
		
	}else{
        setError(r, "You haven't created any feedback!!!");
        return "redirect:/downloadReportMyCourseStudentForm";
	}
		return "report/downLoadReportLink";
	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/reportFormForStudents", method = RequestMethod.GET)
	public String reportFormForStudents(Model m, Principal p) {
		
		String username = p.getName();

		Token userdetails1 = (Token) p;
		String ProgramName = userdetails1.getProgramName();
		User u = userService.findByUserName(username);
		

		String acadSession = u.getAcadSession();
		
		m.addAttribute("Program_Name", ProgramName);
		m.addAttribute("AcadSession", acadSession);
		
		m.addAttribute("webPage", new WebPage(null, "Reports",
				false, false));
		
		List<Course> courseList = userdetails1.getCourseList();
		logger.info("courseList---->"+courseList.get(0).getCourseName());
		m.addAttribute("courseList", courseList);
		return "report/reportStudent";
	}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
	@RequestMapping(value = "/getAssignmentStatsByCourseParent", method = {
				RequestMethod.GET, RequestMethod.POST })
		public @ResponseBody String getAssignmentStatsByCourseParent(@RequestParam Long courseId, Principal principal) 
	{

			
			logger.info("courseId---->" + courseId);
			String username;
			username = principal.getName();
			Token userDetails = (Token) principal;
			if(userDetails.getAuthorities().contains(Role.ROLE_PARENT)){
				username = StringUtils.substring(username, 0,
						StringUtils.lastIndexOf(username, "_P"));
			}
			
			try {
				/*StudentTest testStats = studentTestService.getTestSummaryByIdAndSem(username, acadSession);*/
				List<StudentAssignment> assignments = studentAssignmentService.getAssignmentsForParentReport(username, courseId);
				logger.info("assignments--->"+assignments);
				
				if(assignments.size() > 0){
					String json = new Gson().toJson(assignments);
					logger.info("json----->" + json);
					return json;
				}else{
					return "[]";
				}
				
			}catch (Exception e) {
				logger.error("Error", e);
				return "[]";
			}

		}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
		@RequestMapping(value = "/getTestStatsByCourseParent", method = {
				RequestMethod.GET, RequestMethod.POST })
		public @ResponseBody String getTestStatsByCourseParent(@RequestParam Long courseId, Principal principal) {

			
			logger.info("courseId---->" + courseId);
			String username;
			username = principal.getName();
			Token userDetails = (Token) principal;
			if(userDetails.getAuthorities().contains(Role.ROLE_PARENT)){
				username = StringUtils.substring(username, 0,
						StringUtils.lastIndexOf(username, "_P"));
			}
			
			try {
				logger.info("stud--->"+username);
				List<StudentTest> tests = studentTestService.getTestsForParentReport(username, courseId);
				logger.info("tests--->"+tests);
				
				if(tests.size() > 0){
					String json = new Gson().toJson(tests);
					logger.info("json----->" + json);
					return json;
				}else{
					return "[]";
				}
				
			}catch (Exception e) {
				logger.error("Error", e);
				return "[]";
			}

		}
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
		@RequestMapping(value = "/reportFormForParents", method = RequestMethod.GET)
		public String reportFormForParents(Model m, Principal p) {
			
			String username = p.getName();

			Token userdetails1 = (Token) p;
			String ProgramName = userdetails1.getProgramName();
			User u = userService.findByUserName(username);
			

			String acadSession = u.getAcadSession();
			
			m.addAttribute("Program_Name", ProgramName);
			m.addAttribute("AcadSession", acadSession);
			
			m.addAttribute("webPage", new WebPage(null, "Reports",
					false, false));
			
			if(userdetails1.getAuthorities().contains(Role.ROLE_PARENT)){
			username = StringUtils.substring(username, 0,
					StringUtils.lastIndexOf(username, "_P"));
			logger.info("studeNAme---->"+username);
			}
			List<Course> courseList = courseService.findByUser(username);
			logger.info("courseList---->"+courseList.get(0).getCourseName());
			m.addAttribute("courseList", courseList);
			return "reportUtil/reportParent";
		}

		
		
		
		//New LMS Utilty Report Added with New Report Format By Akshay on 31-01-2020
	@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
		@RequestMapping(value = "/lmsUsageReportCombinedWithAllCampus", method = {
				RequestMethod.GET, RequestMethod.POST })
		public String lmsUsageReportCombinedWithAllCampus(Model m, Principal p,
				HttpServletResponse response, @RequestParam String fromDate,
				@RequestParam String toDate,
				@RequestParam(required = false) String campus) throws URIException {
			Token userdetails1 = (Token) p;

			
			
			
			Program prog = new Program();
			if (userdetails1.getProgramId() != null) {
				prog = programService.findByID(Long.valueOf(userdetails1.getProgramId()));
			} else {
				prog = programService.findAllActive().get(0);
			}

			List<ProgramCampus> programCampusList = programCampusService
					.getCampusesForSchool();

			Map<String, String> programCampusMap = new HashMap<>();

			for (ProgramCampus pc : programCampusList) {
				programCampusMap.put(pc.getCampusId(), pc.getCampusAbbr());
			}

			String campusName = "NA";
			if ("null".equals(campus)) {
				if (programCampusList.size() == 0 || "PDSEFBM".equalsIgnoreCase(app)) {
					ProgramCampus pc = new ProgramCampus();
					pc.setCampusId(null);
					pc.setCampusAbbr(prog.getAbbr());
					programCampusList.add(pc);
					programCampusMap.put(pc.getCampusId(), pc.getCampusAbbr());
				}
				campus = null;
			} else {
				campusName = programCampusService.getCampusByCampusId(campus);
				programCampusList.clear();
				ProgramCampus pc = new ProgramCampus();
				pc.setCampusId(campus);
				pc.setCampusAbbr(campusName);
				programCampusList.add(pc);
				programCampusMap.put(pc.getCampusId(), pc.getCampusAbbr());
			}
			InputStream is = null;
			try {

				Map<String, List<UtilityReport>> resultMapper1 = new HashMap<>();
				Map<String, List<UtilityReport>> resultMapper2 = new HashMap<>();
				Map<String, List<UtilityReport>> resultMapper3 = new HashMap<>();
				
//				if("PDSEFBM".equals(app)) {
//					programCampusList.clear();
//					campus=null;
//				}

				if (programCampusList.size() > 0) {

					for (ProgramCampus pc : programCampusList) {
						List<Map<String, Object>> detailedReport1 = utilityReportService
								.getDetailedReport1(pc.getCampusId(), fromDate,
										toDate);

						List<Map<String, Object>> detailedReport2 = utilityReportService
								.getDetailedReport2(pc.getCampusId(), fromDate,
										toDate);

						List<Map<String, Object>> getSummReport = utilityReportService
								.getSummaryUtilityReportMap(pc.getCampusId(),
										fromDate, toDate);

						List<UtilityReport> report1 = getReport1(getSummReport,
								detailedReport1, fromDate, toDate);

						List<UtilityReport> report2 = getReport2(getSummReport);
						List<UtilityReport> report3 = getReport3(detailedReport2);
						
						logger.info("report 2 map----<>--------"+ detailedReport2);

						resultMapper1.put(pc.getCampusId(), report1);
						resultMapper2.put(pc.getCampusId(), report2);
						resultMapper3.put(pc.getCampusId(), report3);
					}
				} else {

					List<Map<String, Object>> detailedReport1 = utilityReportService
							.getDetailedReport1(campus, fromDate, toDate);

					List<Map<String, Object>> detailedReport2 = utilityReportService
							.getDetailedReport2(campus, fromDate, toDate);

					List<Map<String, Object>> getSummReport = utilityReportService
							.getSummaryUtilityReportMap(campus, fromDate, toDate);

					List<UtilityReport> report1 = getReport1(getSummReport,
							detailedReport1, fromDate, toDate);

					List<UtilityReport> report2 = getReport2(getSummReport);
					List<UtilityReport> report3 = getReport3(detailedReport2);
					logger.info("report 2 map----<>--------"+ detailedReport2);
					resultMapper1.put(campus, report1);
					resultMapper2.put(campus, report2);
					resultMapper3.put(campus, report3);

				}

				// logger.info("summ report" + getSummReport);

				String getReportFilePath = generateReportFilePathWithAllCampuses(
						resultMapper1, resultMapper2, resultMapper3,
						prog.getAbbr(), campusName, fromDate, toDate,
						programCampusList, programCampusMap);

				String filename = "LMS-UtilizationReport.xlsx";
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("Content-Disposition", "attachment; filename="
						+ filename);
				// copy it to response's OutputStream
				is = new FileInputStream(getReportFilePath);

				org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				response.getOutputStream().flush();
				response.getOutputStream().close();

			} catch (Exception ex) {

				logger.error("Exception", ex);
				return "report/downLoadReportLink";
			}

			return "report/downLoadReportLink";
		}

		public String generateReportFilePath(List<UtilityReport> report1,
				List<UtilityReport> report2, List<UtilityReport> report3,
				String collegeAbbr, String campusAbbr, String fromDate,
				String toDate) {

			logger.info("Report 1" + report1);
			logger.info("Report 2" + report1);
			logger.info("Report 3" + report1);
			String filePath = downloadAllFolder + File.separator
					+ "LMS-UtilizationReport" + collegeAbbr
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";

			XSSFWorkbook workbook = new XSSFWorkbook();

			XSSFSheet sheet = workbook.createSheet("LMS Utilitzation Report");

			CellStyle centerStyle = workbook.createCellStyle();
			centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

			XSSFCellStyle totalStyle = workbook.createCellStyle();
			XSSFCellStyle totalRedStyle = workbook.createCellStyle();

			XSSFFont defaultFont = workbook.createFont();
			defaultFont.setFontHeightInPoints((short) 15);
			defaultFont.setFontName("Arial");
			defaultFont.setColor(IndexedColors.BLACK.getIndex());
			defaultFont.setBold(true);
			defaultFont.setItalic(false);
			totalStyle.setFont(defaultFont);
			totalRedStyle.setFont(defaultFont);

			XSSFFont reportTitleFont = workbook.createFont();
			reportTitleFont.setFontHeightInPoints((short) 10);
			reportTitleFont.setFontName("Arial");
			reportTitleFont.setColor(IndexedColors.RED.getIndex());
			reportTitleFont.setBold(true);
			reportTitleFont.setItalic(false);
			totalStyle.setFont(reportTitleFont);
			totalRedStyle.setFont(reportTitleFont);

			XSSFCellStyle reportTitleStyle = workbook.createCellStyle();
			reportTitleStyle.setBorderBottom(CellStyle.BORDER_THIN);
			reportTitleStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			reportTitleStyle.setBorderLeft(CellStyle.BORDER_THIN);
			reportTitleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			reportTitleStyle.setBorderRight(CellStyle.BORDER_THIN);
			reportTitleStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			reportTitleStyle.setBorderTop(CellStyle.BORDER_THIN);
			reportTitleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			reportTitleStyle.setFont(reportTitleFont);
			reportTitleStyle.setWrapText(true);
			reportTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);

			XSSFCellStyle reportHeadersStyle = workbook.createCellStyle();
			reportHeadersStyle.setBorderBottom(CellStyle.BORDER_THIN);
			reportHeadersStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			reportHeadersStyle.setAlignment(CellStyle.ALIGN_CENTER);
			reportHeadersStyle.setWrapText(true);

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
			headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

			XSSFCellStyle wrapStyle = workbook.createCellStyle();
			wrapStyle.setWrapText(true);
			wrapStyle.setAlignment(CellStyle.ALIGN_CENTER);

			XSSFCellStyle detailStyle = workbook.createCellStyle();
			detailStyle.setWrapText(true);
			detailStyle.setAlignment(CellStyle.ALIGN_CENTER);
			detailStyle.setFont(reportTitleFont);

			XSSFCellStyle style = workbook.createCellStyle();

			style.setBorderBottom(CellStyle.BORDER_THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(CellStyle.BORDER_THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight(CellStyle.BORDER_THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());

			int rowNum = 0;
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 5));

			Row titleRow = sheet.createRow(rowNum++);
			Cell titleCell = titleRow.createCell(0);

			titleCell.setCellValue("LMS Portal Usage Report");

			titleCell.setCellStyle(headerStyle);

			sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
			sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));

			rowNum = 2;
			Row detailRow = sheet.createRow(rowNum++);

			int detailCell = 0;
			Cell schoolCell = detailRow.createCell(0);
			schoolCell.setCellValue("Name Of School:" + collegeAbbr);
			schoolCell.setCellStyle(detailStyle);

			Cell campusCell = detailRow.createCell(2);
			campusCell.setCellValue("Campus:" + campusAbbr);
			campusCell.setCellStyle(detailStyle);

			Cell durationCell = detailRow.createCell(4);
			durationCell.setCellValue("Duration Of Report: " + fromDate + " To "
					+ toDate);
			durationCell.setCellStyle(detailStyle);

			Row emptyRow = sheet.createRow(rowNum++);

			rowNum = 3;
			Row reportTitle1 = sheet.createRow(rowNum++);

			sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));
			Cell reportCell1 = reportTitle1.createCell(0);
			Cell reportCell11 = reportTitle1.createCell(1);
			Cell reportCell12 = reportTitle1.createCell(2);
			Cell reportCell13 = reportTitle1.createCell(3);
			Cell reportCell14 = reportTitle1.createCell(4);
			// Cell reportCell15 = reportTitle1.createCell(5);

			reportCell1.setCellValue("Total No. of Users By Role");

			reportCell1.setCellStyle(reportTitleStyle);
			reportCell11.setCellStyle(reportTitleStyle);
			reportCell12.setCellStyle(reportTitleStyle);
			reportCell13.setCellStyle(reportTitleStyle);
			reportCell14.setCellStyle(reportTitleStyle);
			// reportCell15.setCellStyle(reportTitleStyle);

			/*
			 * for(int i=0;i<6;i++){ Cell cellStyle = reportTitle1.createCell(i);
			 * 
			 * }
			 */

			String report1h[] = { "Sr. No.", "Portal Role", "Total No. of Users",
					"No. of Users using Portal", "% of Usage" };

			String report2h[] = { "Sr. No.", "Features Provided by Portal",
					"No. of Times Features Used By Core + Adjunct Faculty",
					"No. of Times Features Used By  Visiting Faculty",
					"No. of Times Features Used By Student",
					"No. of Times Features Used By Admin" };

			String report3h[] = { "Sr. No.", "Name of the Faculty", "Faculty Type",
					"No. of Times Features Used By Faculty", "Features Used", };

			Row headerRow1 = sheet.createRow(rowNum++);
			// headerRow1.setCellStyle(style);
			for (int colNum = 0; colNum < report1h.length; colNum++) {

				Cell cell = headerRow1.createCell(colNum);
				cell.setCellValue(report1h[colNum]);
				cell.setCellStyle(reportHeadersStyle);

			}

			int srNo = 1;
			logger.info("size of report1" + report1.size());
			for (UtilityReport ur : report1) {
				Row dataRow1 = sheet.createRow(rowNum++);

				int cellNo = 0;

				dataRow1.createCell(cellNo++).setCellValue(srNo);
				dataRow1.createCell(cellNo++).setCellValue(ur.getUserType());
				dataRow1.createCell(cellNo++).setCellValue(ur.getNoOfUsersByRole());
				dataRow1.createCell(cellNo++).setCellValue(ur.getNoOfUsedByRole());
				dataRow1.createCell(cellNo++).setCellValue(ur.getUsagePercentage());
				// dataRow1.createCell(cellNo++).setCellStyle(style);

				/*
				 * if (srNo == report1.size()) { int count = 0; while (count < 5) {
				 * dataRow1.createCell(count).setCellStyle(reportHeadersStyle);
				 * count++; } }
				 */

				srNo = srNo + 1;

			}

			Row emptyRow1 = sheet.createRow(rowNum++);

			Row reportTitle2 = sheet.createRow(rowNum++);

			sheet.addMergedRegion(new CellRangeAddress(11, 11, 0, 5));
			Cell reportCell2 = reportTitle2.createCell(0);
			Cell reportCell21 = reportTitle2.createCell(1);
			Cell reportCell22 = reportTitle2.createCell(2);
			Cell reportCell23 = reportTitle2.createCell(3);
			Cell reportCell24 = reportTitle2.createCell(4);

			reportCell2.setCellValue("LMS Portal Feature-Wise Usage");
			reportCell2.setCellStyle(reportTitleStyle);
			reportCell21.setCellStyle(reportTitleStyle);
			reportCell22.setCellStyle(reportTitleStyle);
			reportCell23.setCellStyle(reportTitleStyle);
			reportCell24.setCellStyle(reportTitleStyle);

			Row headerRow2 = sheet.createRow(rowNum++);

			for (int colNum = 0; colNum < report2h.length; colNum++) {

				Cell cell = headerRow2.createCell(colNum);
				cell.setCellValue(report2h[colNum]);
				cell.setCellStyle(reportHeadersStyle);

			}

			srNo = 1;

			logger.info("size of report2" + report2.size());
			for (UtilityReport ur : report2) {
				Row dataRow1 = sheet.createRow(rowNum++);

				int cellNo = 0;

				dataRow1.createCell(cellNo++).setCellValue(srNo);
				dataRow1.createCell(cellNo++).setCellValue(ur.getFeatures());
				dataRow1.createCell(cellNo++).setCellValue(
						ur.getNoOfHitByPFaculty());

				dataRow1.createCell(cellNo++).setCellValue(
						ur.getNoOfHitByVFaculty());
				dataRow1.createCell(cellNo++)
						.setCellValue(ur.getNoOfHitByStudent());
				dataRow1.createCell(cellNo++).setCellValue(ur.getNoOfHitByAdmin());

				/*
				 * if (srNo == report2.size()) { int count = 0; while (count < 5) {
				 * dataRow1.createCell(count).setCellStyle(reportHeadersStyle);
				 * count++; } }
				 */

				srNo = srNo + 1;
			}

			Row emptyRow2 = sheet.createRow(rowNum++);

			Row reportTitle3 = sheet.createRow(rowNum++);

			sheet.addMergedRegion(new CellRangeAddress(22, 22, 0, 5));
			Cell reportCell3 = reportTitle3.createCell(0);
			Cell reportCell31 = reportTitle3.createCell(1);
			Cell reportCell32 = reportTitle3.createCell(2);
			Cell reportCell33 = reportTitle3.createCell(3);
			Cell reportCell34 = reportTitle3.createCell(4);

			reportCell3.setCellValue("Top 5 Users For School");

			reportCell3.setCellStyle(reportTitleStyle);
			reportCell31.setCellStyle(reportTitleStyle);
			reportCell32.setCellStyle(reportTitleStyle);
			reportCell33.setCellStyle(reportTitleStyle);
			reportCell34.setCellStyle(reportTitleStyle);

			Row headerRow3 = sheet.createRow(rowNum++);

			for (int colNum = 0; colNum < report3h.length; colNum++) {

				Cell cell = headerRow3.createCell(colNum);
				cell.setCellValue(report3h[colNum]);
				cell.setCellStyle(reportHeadersStyle);
			}

			srNo = 1;

			logger.info("size of report3" + report3.size());
			for (UtilityReport ur : report3) {
				Row dataRow1 = sheet.createRow(rowNum++);

				int cellNo = 0;

				dataRow1.createCell(cellNo++).setCellValue(srNo);
				dataRow1.createCell(cellNo++).setCellValue(ur.getName());
				dataRow1.createCell(cellNo++).setCellValue(ur.getFacultyType());
				dataRow1.createCell(cellNo++)
						.setCellValue(ur.getNoOfHitByFaculty());
				dataRow1.createCell(cellNo++).setCellValue(ur.getFeaturesUsed());

				/*
				 * if (srNo == report3.size()) { int count = 0; while (count < 4) {
				 * dataRow1.createCell(count).setCellStyle(reportHeadersStyle);
				 * count++; } }
				 */
				srNo = srNo + 1;
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
				// FileUtils.deleteQuietly(new File(filePath));
				workbook.close();
			} catch (Exception e) {
				logger.error("Exception ", e);
			}

			return filePath;
		}

		public String generateReportFilePathWithAllCampuses(
				Map<String, List<UtilityReport>> reportMap1,
				Map<String, List<UtilityReport>> reportMap2,
				Map<String, List<UtilityReport>> reportMap3, String collegeAbbr,
				String campusAbbr, String fromDate, String toDate,
				List<ProgramCampus> programCampusList,
				Map<String, String> programCampusMap) {

			String filePath = downloadAllFolder + File.separator
					+ "LMS-UtilizationReport" + collegeAbbr
					+ Utils.formatDate("dd-MM-yyyy-HH-mm-ss", Utils.getInIST())
					+ ".xlsx";

			logger.info("fromDate" + fromDate);
			logger.info("toDate" + toDate);
			fromDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
					fromDate);
			toDate = Utils.formatDate("yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", toDate);

			XSSFWorkbook workbook = new XSSFWorkbook();

			for (ProgramCampus pc : programCampusList) {

				XSSFSheet sheet = workbook.createSheet(programCampusMap.get(pc
						.getCampusId()));

				List<UtilityReport> report1 = reportMap1.get(pc.getCampusId());
				List<UtilityReport> report2 = reportMap2.get(pc.getCampusId());
				List<UtilityReport> report3 = reportMap3.get(pc.getCampusId());

				CellStyle centerStyle = workbook.createCellStyle();
				centerStyle.setAlignment(CellStyle.ALIGN_CENTER);

				XSSFCellStyle totalStyle = workbook.createCellStyle();
				XSSFCellStyle totalRedStyle = workbook.createCellStyle();

				XSSFFont defaultFont = workbook.createFont();
				defaultFont.setFontHeightInPoints((short) 15);
				defaultFont.setFontName("Arial");
				defaultFont.setColor(IndexedColors.BLACK.getIndex());
				defaultFont.setBold(true);
				defaultFont.setItalic(false);
				totalStyle.setFont(defaultFont);
				totalRedStyle.setFont(defaultFont);

				XSSFFont reportTitleFont = workbook.createFont();
				reportTitleFont.setFontHeightInPoints((short) 10);
				reportTitleFont.setFontName("Arial");
				reportTitleFont.setColor(IndexedColors.RED.getIndex());
				reportTitleFont.setBold(true);
				reportTitleFont.setItalic(false);
				totalStyle.setFont(reportTitleFont);
				totalRedStyle.setFont(reportTitleFont);

				XSSFCellStyle reportTitleStyle = workbook.createCellStyle();
				reportTitleStyle.setBorderBottom(CellStyle.BORDER_THIN);
				reportTitleStyle.setBottomBorderColor(IndexedColors.BLACK
						.getIndex());
				reportTitleStyle.setBorderLeft(CellStyle.BORDER_THIN);
				reportTitleStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				reportTitleStyle.setBorderRight(CellStyle.BORDER_THIN);
				reportTitleStyle
						.setRightBorderColor(IndexedColors.BLACK.getIndex());
				reportTitleStyle.setBorderTop(CellStyle.BORDER_THIN);
				reportTitleStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
				reportTitleStyle.setFont(reportTitleFont);
				reportTitleStyle.setWrapText(true);
				reportTitleStyle.setAlignment(CellStyle.ALIGN_CENTER);

				XSSFCellStyle reportHeadersStyle = workbook.createCellStyle();
				reportHeadersStyle.setBorderBottom(CellStyle.BORDER_THIN);
				reportHeadersStyle.setBottomBorderColor(IndexedColors.BLACK
						.getIndex());
				reportHeadersStyle.setAlignment(CellStyle.ALIGN_CENTER);
				reportHeadersStyle.setWrapText(true);

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
				headerStyle.setAlignment(CellStyle.ALIGN_CENTER);

				XSSFCellStyle wrapStyle = workbook.createCellStyle();
				wrapStyle.setWrapText(true);
				wrapStyle.setAlignment(CellStyle.ALIGN_CENTER);

				XSSFCellStyle detailStyle = workbook.createCellStyle();
				detailStyle.setWrapText(true);
				detailStyle.setAlignment(CellStyle.ALIGN_CENTER);
				detailStyle.setFont(reportTitleFont);

				XSSFCellStyle style = workbook.createCellStyle();

				style.setBorderBottom(CellStyle.BORDER_THIN);
				style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderLeft(CellStyle.BORDER_THIN);
				style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderRight(CellStyle.BORDER_THIN);
				style.setRightBorderColor(IndexedColors.BLACK.getIndex());
				style.setBorderTop(CellStyle.BORDER_MEDIUM_DASHED);
				style.setTopBorderColor(IndexedColors.BLACK.getIndex());

				int rowNum = 0;
				sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 5));

				Row titleRow = sheet.createRow(rowNum++);
				Cell titleCell = titleRow.createCell(0);

				titleCell.setCellValue("LMS Portal Usage Report");

				titleCell.setCellStyle(headerStyle);

				sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 2, 3));
				sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));

				rowNum = 2;
				Row detailRow = sheet.createRow(rowNum++);

				int detailCell = 0;
				Cell schoolCell = detailRow.createCell(0);
				schoolCell.setCellValue("Name Of School:" + collegeAbbr);
				schoolCell.setCellStyle(detailStyle);

				Cell campusCell = detailRow.createCell(2);
				if (pc.getCampusId() != null) {
					campusCell.setCellValue("Campus: "
							+ programCampusMap.get(pc.getCampusId()));
				} else {
					campusCell.setCellValue("Campus: " + "NA");
				}
				campusCell.setCellStyle(detailStyle);

				Cell durationCell = detailRow.createCell(4);
				durationCell.setCellValue("Duration Of Report: " + fromDate
						+ " To " + toDate);
				durationCell.setCellStyle(detailStyle);

				Row emptyRow = sheet.createRow(rowNum++);

				rowNum = 3;
				Row reportTitle1 = sheet.createRow(rowNum++);

				sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 5));
				Cell reportCell1 = reportTitle1.createCell(0);
				Cell reportCell11 = reportTitle1.createCell(1);
				Cell reportCell12 = reportTitle1.createCell(2);
				Cell reportCell13 = reportTitle1.createCell(3);
				Cell reportCell14 = reportTitle1.createCell(4);
				Cell reportCell15 = reportTitle1.createCell(5);
				// Cell reportCell15 = reportTitle1.createCell(5);

				reportCell1.setCellValue("Total No. of Users By Role");

				reportCell1.setCellStyle(reportTitleStyle);
				reportCell11.setCellStyle(reportTitleStyle);
				reportCell12.setCellStyle(reportTitleStyle);
				reportCell13.setCellStyle(reportTitleStyle);
				reportCell14.setCellStyle(reportTitleStyle);
				reportCell15.setCellStyle(reportTitleStyle);
				// reportCell15.setCellStyle(reportTitleStyle);

				/*
				 * for(int i=0;i<6;i++){ Cell cellStyle =
				 * reportTitle1.createCell(i);
				 * 
				 * }
				 */

				String report1h[] = { "Sr. No.", "Portal Role",
						"Total No. of Users", "No. of Users using Portal",
						"% of Usage" };

				String report2h[] = { "Sr. No.", "Features Provided by Portal",
						"No. of Times Features Used By Core + Adjunct Faculty",
						"No. of Times Features Used By  Visiting Faculty",
						"No. of Times Features Used By Student",
						"No. of Times Features Used By Admin" };

				String report3h[] = { "Sr. No.", "Name of the Faculty",
						"Faculty Type", "No. of Times Features Used By Faculty",
						"Features Used" };

				Row headerRow1 = sheet.createRow(rowNum++);
				// headerRow1.setCellStyle(style);
				for (int colNum = 0; colNum < report1h.length; colNum++) {

					Cell cell = headerRow1.createCell(colNum);
					cell.setCellValue(report1h[colNum]);
					cell.setCellStyle(reportHeadersStyle);

				}

				int srNo = 1;
				logger.info("size of report1" + report1.size());
				for (UtilityReport ur : report1) {
					Row dataRow1 = sheet.createRow(rowNum++);

					int cellNo = 0;

					Cell cell1 = dataRow1.createCell(cellNo++);
					cell1.setCellValue(srNo);
					//cell1.setCellStyle(wrapStyle);

					Cell cell2 = dataRow1.createCell(cellNo++);
					cell2.setCellValue(ur.getUserType());
					//cell2.setCellStyle(wrapStyle);

					Cell cell3 = dataRow1.createCell(cellNo++);
					cell3.setCellValue(ur.getNoOfUsersByRole());
					cell3.setCellStyle(wrapStyle);

					Cell cell4 = dataRow1.createCell(cellNo++);
					cell4.setCellValue(ur.getNoOfUsedByRole());
					cell4.setCellStyle(wrapStyle);

					Cell cell5 = dataRow1.createCell(cellNo++);
					cell5.setCellValue(ur.getUsagePercentage());
					cell5.setCellStyle(wrapStyle);

					srNo = srNo + 1;

				}

				Row emptyRow1 = sheet.createRow(rowNum++);

				Row reportTitle2 = sheet.createRow(rowNum++);

				sheet.addMergedRegion(new CellRangeAddress(11, 11, 0, 5));
				Cell reportCell2 = reportTitle2.createCell(0);
				Cell reportCell21 = reportTitle2.createCell(1);
				Cell reportCell22 = reportTitle2.createCell(2);
				Cell reportCell23 = reportTitle2.createCell(3);
				Cell reportCell24 = reportTitle2.createCell(4);
				Cell reportCell25 = reportTitle2.createCell(5);

				reportCell2.setCellValue("LMS Portal Feature-Wise Usage");
				reportCell2.setCellStyle(reportTitleStyle);
				reportCell21.setCellStyle(reportTitleStyle);
				reportCell22.setCellStyle(reportTitleStyle);
				reportCell23.setCellStyle(reportTitleStyle);
				reportCell24.setCellStyle(reportTitleStyle);
				reportCell25.setCellStyle(reportTitleStyle);

				Row headerRow2 = sheet.createRow(rowNum++);

				for (int colNum = 0; colNum < report2h.length; colNum++) {

					Cell cell = headerRow2.createCell(colNum);
					cell.setCellValue(report2h[colNum]);
					cell.setCellStyle(reportHeadersStyle);

				}

				srNo = 1;

				logger.info("size of report2" + report2.size());
				for (UtilityReport ur : report2) {
					Row dataRow1 = sheet.createRow(rowNum++);

					int cellNo = 0;
					
					Cell cell1 = dataRow1.createCell(cellNo++);
					cell1.setCellValue(srNo);
					
					Cell cell2 = dataRow1.createCell(cellNo++);
					cell2.setCellValue(ur.getFeatures()); 
					
					Cell cell3 = dataRow1.createCell(cellNo++);
					cell3.setCellValue(ur.getNoOfHitByPFaculty());
					cell3.setCellStyle(wrapStyle);
					
					
					Cell cell4 = dataRow1.createCell(cellNo++);
					cell4.setCellValue(ur.getNoOfHitByVFaculty());
					cell4.setCellStyle(wrapStyle);
					
					
					Cell cell5 = dataRow1.createCell(cellNo++);
					cell5.setCellValue(ur.getNoOfHitByStudent());
					cell5.setCellStyle(wrapStyle);
					
					
					Cell cell6 = dataRow1.createCell(cellNo++);
					cell6.setCellValue(ur.getNoOfHitByAdmin());
					cell6.setCellStyle(wrapStyle);

					

					/*
					 * if (srNo == report2.size()) { int count = 0; while (count <
					 * 5) {
					 * dataRow1.createCell(count).setCellStyle(reportHeadersStyle);
					 * count++; } }
					 */

					srNo = srNo + 1;
				}

				Row emptyRow2 = sheet.createRow(rowNum++);

				Row reportTitle3 = sheet.createRow(rowNum++);

				sheet.addMergedRegion(new CellRangeAddress(22, 22, 0, 5));
				Cell reportCell3 = reportTitle3.createCell(0);
				Cell reportCell31 = reportTitle3.createCell(1);
				Cell reportCell32 = reportTitle3.createCell(2);
				Cell reportCell33 = reportTitle3.createCell(3);
				Cell reportCell34 = reportTitle3.createCell(4);
				Cell reportCell35 = reportTitle3.createCell(5);

				reportCell3.setCellValue("Top 10 Users For School");

				reportCell3.setCellStyle(reportTitleStyle);
				reportCell31.setCellStyle(reportTitleStyle);
				reportCell32.setCellStyle(reportTitleStyle);
				reportCell33.setCellStyle(reportTitleStyle);
				reportCell34.setCellStyle(reportTitleStyle);
				reportCell35.setCellStyle(reportTitleStyle);

				Row headerRow3 = sheet.createRow(rowNum++);

				for (int colNum = 0; colNum < report3h.length; colNum++) {

					Cell cell = headerRow3.createCell(colNum);
					cell.setCellValue(report3h[colNum]);
					cell.setCellStyle(reportHeadersStyle);
				}

				srNo = 1;

				logger.info("size of report3" + report3.size());
				for (UtilityReport ur : report3) {
					Row dataRow1 = sheet.createRow(rowNum++);

					int cellNo = 0;

					Cell cell1 = dataRow1.createCell(cellNo++);
					cell1.setCellValue(srNo);
					
					Cell cell2 = dataRow1.createCell(cellNo++);
					cell2.setCellValue(ur.getName());
					
					Cell cell3 = dataRow1.createCell(cellNo++);
					cell3.setCellValue(ur.getFacultyType());
					
					
					Cell cell4 = dataRow1.createCell(cellNo++);
					cell4.setCellValue(ur.getNoOfHitByFaculty());
					cell4.setCellStyle(wrapStyle);
					
					
					Cell cell5 = dataRow1.createCell(cellNo++);
					cell5.setCellValue(ur.getFeaturesUsed());
					
					
					/*dataRow1.createCell(cellNo++).setCellValue(srNo);
					dataRow1.createCell(cellNo++).setCellValue(ur.getName());
					dataRow1.createCell(cellNo++).setCellValue(ur.getFacultyType());
					dataRow1.createCell(cellNo++).setCellValue(
							ur.getNoOfHitByFaculty());
					dataRow1.createCell(cellNo++)
							.setCellValue(ur.getFeaturesUsed());*/

					/*
					 * if (srNo == report3.size()) { int count = 0; while (count <
					 * 4) {
					 * dataRow1.createCell(count).setCellStyle(reportHeadersStyle);
					 * count++; } }
					 */
					srNo = srNo + 1;
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

		public List<UtilityReport> getReport1(List<Map<String, Object>> summReport,
				List<Map<String, Object>> detailReport1, String startDate,
				String endDate) {

			List<UtilityReport> utilList = new ArrayList<>();
			List<String> roleList = Arrays.asList("Faculty - Core + Adjunct",
					"Faculty - Visiting", "Students", "Admin User", "Total");

			Map<String, Integer> mapOfRoleAndTotalNoUser = new HashMap<>();
			mapOfRoleAndTotalNoUser.put(
					"Faculty - Core + Adjunct",
					Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalCFaculty"))));
			mapOfRoleAndTotalNoUser.put(
					"Faculty - Visiting",
					Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalVFaculty"))));
			mapOfRoleAndTotalNoUser.put(
					"Students",
					Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalStudent"))));
			mapOfRoleAndTotalNoUser.put(
					"Admin User",
					Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalAdmin"))));

			int total = Integer.parseInt(String.valueOf(summReport.get(0).get(
					"totalCFaculty")))
					+ Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalVFaculty")))
					+ Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalStudent")))
					+ Integer.parseInt(String.valueOf(summReport.get(0).get(
							"totalAdmin")));
			mapOfRoleAndTotalNoUser.put("Total", total);

			Map<String, Integer> mapOfRoleAndTotalNoUsed = new HashMap<>();

			int totalUsed = 0;
			for (Map<String, Object> maps : detailReport1) {

				if (maps.get("role").equals("ROLE_FACULTY")) {
					if (maps.get("facultyType").equals("P")) {
						mapOfRoleAndTotalNoUsed.put("Faculty - Core + Adjunct",
								Integer.parseInt(String.valueOf(maps
										.get("noOfUsed"))));
						totalUsed = totalUsed
								+ Integer.parseInt(String.valueOf(maps
										.get("noOfUsed")));
					} else {
						mapOfRoleAndTotalNoUsed.put("Faculty - Visiting", Integer
								.parseInt(String.valueOf(maps.get("noOfUsed"))));
						totalUsed = totalUsed
								+ Integer.parseInt(String.valueOf(maps
										.get("noOfUsed")));
					}
				} else if (maps.get("role").equals("ROLE_STUDENT")) {
					mapOfRoleAndTotalNoUsed.put("Students",
							Integer.parseInt(String.valueOf(maps.get("noOfUsed"))));
					totalUsed = totalUsed
							+ Integer
									.parseInt(String.valueOf(maps.get("noOfUsed")));
				} else {
					mapOfRoleAndTotalNoUsed.put("Admin User",
							Integer.parseInt(String.valueOf(maps.get("noOfUsed"))));
					totalUsed = totalUsed
							+ Integer
									.parseInt(String.valueOf(maps.get("noOfUsed")));
				}
			}
			System.out.println("map of used" + mapOfRoleAndTotalNoUsed);
			System.out.println("map of users" + mapOfRoleAndTotalNoUser);
			mapOfRoleAndTotalNoUsed.put("Total", totalUsed);
			for (int i = 0; i < roleList.size(); i++) {
				if (mapOfRoleAndTotalNoUser.get(roleList.get(i)) == 0) {
					mapOfRoleAndTotalNoUsed.put(roleList.get(i), 0);
				}
				UtilityReport ur = new UtilityReport();

				ur.setUserType(roleList.get(i));
				logger.info(roleList.get(i));

				int noOfUsers = mapOfRoleAndTotalNoUser.get(roleList.get(i));

				int noOfUsed = 0;

				if (mapOfRoleAndTotalNoUsed.containsKey(roleList.get(i))) {
					System.out.println("No key for " + roleList.get(i));
					noOfUsed = mapOfRoleAndTotalNoUsed.get(roleList.get(i));
				}
				ur.setNoOfUsersByRole(noOfUsers);
				ur.setNoOfUsedByRole(noOfUsed);
				System.out.println("noOf Users" + noOfUsers);
				System.out.println("noOf Used" + noOfUsed);
				double percentage = 0.0;

				if (noOfUsers == 0 || noOfUsed == 0) {
					ur.setUsagePercentage(percentage);
				} else {
					percentage = Double.valueOf(noOfUsed)
							/ Double.valueOf(noOfUsers);

					System.out.println("p got" + percentage);
					percentage = percentage * 100;
					System.out.println("p got" + percentage);
					ur.setUsagePercentage(round(percentage, 2));
				}

				utilList.add(ur);
			}

			return utilList;

		}

		public List<UtilityReport> getReport2(List<Map<String, Object>> summReport) {
			List<UtilityReport> utilList = new ArrayList<>();

			Map<String, Map<String, Integer>> resultMap = new HashMap<>();
			for (Map<String, Object> mapper : summReport) {

				switch ((String) mapper.get("features")) {
				case "Notification":
					Map<String, Integer> hitsByRole = new HashMap<>();
					String studentHit = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHit = String
							.valueOf(mapper.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHit = String
							.valueOf(mapper.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHit = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRole.put("studentHit", Integer.parseInt(studentHit));
					hitsByRole.put("pFacultyHit", Integer.parseInt(pFacultyHit));
					hitsByRole.put("vFacultyHit", Integer.parseInt(vFacultyHit));
					hitsByRole.put("adminHit", Integer.parseInt(adminHit));

					resultMap.put("Notification", hitsByRole);
					break;

				case "Discussion":
					Map<String, Integer> hitsByRoleD = new HashMap<>();
					String studentHitD = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitD = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitD = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitD = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleD.put("studentHit", Integer.parseInt(studentHitD));
					hitsByRoleD.put("pFacultyHit", Integer.parseInt(pFacultyHitD));
					hitsByRoleD.put("vFacultyHit", Integer.parseInt(vFacultyHitD));
					hitsByRoleD.put("adminHit", Integer.parseInt(adminHitD));

					resultMap.put("Discussion", hitsByRoleD);
					break;

				case "Course Content":
					Map<String, Integer> hitsByRoleCC = new HashMap<>();
					String studentHitCC = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitCC = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitCC = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitCC = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleCC.put("studentHit", Integer.parseInt(studentHitCC));
					hitsByRoleCC
							.put("pFacultyHit", Integer.parseInt(pFacultyHitCC));
					hitsByRoleCC
							.put("vFacultyHit", Integer.parseInt(vFacultyHitCC));
					hitsByRoleCC.put("adminHit", Integer.parseInt(adminHitCC));

					resultMap.put("Course Content", hitsByRoleCC);
					break;

				case "Team work Submission":
					Map<String, Integer> hitsByRoleTS = new HashMap<>();
					String studentHitTS = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitTS = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitTS = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitTS = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleTS.put("studentHit", Integer.parseInt(studentHitTS));
					hitsByRoleTS
							.put("pFacultyHit", Integer.parseInt(pFacultyHitTS));
					hitsByRoleTS
							.put("vFacultyHit", Integer.parseInt(vFacultyHitTS));
					hitsByRoleTS.put("adminHit", Integer.parseInt(adminHitTS));

					resultMap.put("Term work Submission", hitsByRoleTS);
					break;

				case "Feedback":
					Map<String, Integer> hitsByRoleF = new HashMap<>();
					String studentHitF = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitF = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitF = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitF = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleF.put("studentHit", Integer.parseInt(studentHitF));
					hitsByRoleF.put("pFacultyHit", Integer.parseInt(pFacultyHitF));
					hitsByRoleF.put("vFacultyHit", Integer.parseInt(vFacultyHitF));
					hitsByRoleF.put("adminHit", Integer.parseInt(adminHitF));

					resultMap.put("Faculty Feedback", hitsByRoleF);
					break;

				case "Student Queries":
					Map<String, Integer> hitsByRoleSQ = new HashMap<>();
					String studentHitSQ = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitSQ = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitSQ = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitSQ = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleSQ.put("studentHit", Integer.parseInt(studentHitSQ));
					hitsByRoleSQ
							.put("pFacultyHit", Integer.parseInt(pFacultyHitSQ));
					hitsByRoleSQ
							.put("vFacultyHit", Integer.parseInt(vFacultyHitSQ));
					hitsByRoleSQ.put("adminHit", Integer.parseInt(adminHitSQ));

					resultMap.put("Student Grievance", hitsByRoleSQ);
					break;

				case "Mark Attendance":
					Map<String, Integer> hitsByRoleMA = new HashMap<>();
					String studentHitMA = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitMA = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitMA = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitMA = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleMA.put("studentHit", Integer.parseInt(studentHitMA));
					hitsByRoleMA
							.put("pFacultyHit", Integer.parseInt(pFacultyHitMA));
					hitsByRoleMA
							.put("vFacultyHit", Integer.parseInt(vFacultyHitMA));
					hitsByRoleMA.put("adminHit", Integer.parseInt(adminHitMA));

					resultMap.put("Student Attendance App", hitsByRoleMA);
					break;

				case "ICA":
					Map<String, Integer> hitsByRoleI = new HashMap<>();
					String studentHitI = String.valueOf(mapper.get("studentsHit")) != "null" ? String
							.valueOf(mapper.get("studentsHit")) : "0";
					String vFacultyHitI = String.valueOf(mapper
							.get("vFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("vFacultiesHit")) : "0";
					String pFacultyHitI = String.valueOf(mapper
							.get("cFacultiesHit")) != "null" ? String
							.valueOf(mapper.get("cFacultiesHit")) : "0";
					String adminHitI = String.valueOf(mapper.get("adminsHit")) != "null" ? String
							.valueOf(mapper.get("adminsHit")) : "0";

					hitsByRoleI.put("studentHit", Integer.parseInt(studentHitI));
					hitsByRoleI.put("pFacultyHit", Integer.parseInt(pFacultyHitI));
					hitsByRoleI.put("vFacultyHit", Integer.parseInt(vFacultyHitI));
					hitsByRoleI.put("adminHit", Integer.parseInt(adminHitI));

					resultMap.put("ICA upload", hitsByRoleI);
					break;

				default:
					break;
				}
			}
			int i = 1;
			for (String s : resultMap.keySet()) {
				UtilityReport ur = new UtilityReport();

				ur.setFeatures(s);

				Map<String, Integer> result = resultMap.get(s);

				ur.setNoOfHitByStudent(result.get("studentHit"));

				ur.setNoOfHitByPFaculty(result.get("pFacultyHit"));
				ur.setNoOfHitByVFaculty(result.get("vFacultyHit"));
				ur.setNoOfHitByAdmin(result.get("adminHit"));

				utilList.add(ur);
				i++;
			}
			return utilList;
		}

		public List<UtilityReport> getReport3(
				List<Map<String, Object>> detailedReport) {
			List<UtilityReport> utilList = new ArrayList<>();
			int i = 1;
			for (Map<String, Object> m : detailedReport) {
				UtilityReport ur = new UtilityReport();
				ur.setName((String) m.get("name"));
				ur.setNoOfHitByFaculty(Integer.parseInt(String.valueOf(m
						.get("total"))));
				ur.setFeaturesUsed((String) m.get("featuresUsed"));
				ur.setFacultyType((String) m.get("facultyType"));
				utilList.add(ur);
				i++;
			}

			return utilList;
		}
		
		
		//MarksheetDownload
		@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
		@RequestMapping(value = "/downloadExamResult", method = { RequestMethod.GET, RequestMethod.POST })
		public String downloadExamResult(
				//@RequestParam(required = false, name = "filePath", defaultValue = "/data/FykcResult/40104190360.pdf") String filePath,
				HttpServletRequest request, HttpServletResponse response,Principal p,RedirectAttributes redirect)
		{
			
			
			InputStream inputStream = null;
			OutputStream outStream = null;
			String filePath="";
			try {
				String username = p.getName();
				
						
						filePath = downloadResultPath+"/"+"Fyjc"+"/"+username+".PDF";
						
						logger.info("filePath--------------->"+filePath);
						
						
						/*
						 * if(filePath != null) { session.setAttribute("abletodownload", "yes");
						 * logger.info("Enterin ------------------"); } else {
						 * logger.info("Enterout ------------------");
						 * //session.setAttribute("abletodownload", "no"); }
						 */
						
						
				ServletContext context = request.getSession().getServletContext();
				File downloadFile = new File(filePath);
			
				inputStream = new FileInputStream(downloadFile);
			
				// get MIME type of the file
				String mimeType = context.getMimeType(filePath);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}
				logger.info("MIME type: " + mimeType);

				// set content attributes for the response
				/* response.setContentType(mimeType); */
				response.setContentLength((int) downloadFile.length());

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"",
						downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				// get output stream of the response
				outStream = response.getOutputStream();

				IOUtils.copy(inputStream, outStream);

			} catch (FileNotFoundException e) {
				logger.error("file not found "+e);
				
			
				setError(redirect," Unable to download marksheet , Result may not have been declared yet. ");
				return "redirect:/homepage";
				
			}
			catch(IOException ie) {
				setError(redirect, "Error in downloading marksheet");
				logger.error("Exception ex",ie);
				return "redirect:/homepage";
			}
			finally {
				if (inputStream != null)
					IOUtils.closeQuietly(inputStream);
				if (outStream != null)
					IOUtils.closeQuietly(outStream);

			}
			return null;
		}
		
		@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
		@RequestMapping(value = "/downloadAssignmentReportQuestionWise", method = RequestMethod.GET)
		public String downloadAssignmentReportQuestionWise(@RequestParam String courseId, @RequestParam String assignmentId, Model m, Principal p,
				HttpServletResponse response, HttpServletRequest request) {

			logger.info("Inside mapping------>" + assignmentId + "----" + courseId);

			List<Assignment> assignmentReportList = assignmentService.findAssignmentReportListWithAssignmentIdForAdmin(assignmentId);

			logger.info("Final assignmentReportList------>" + assignmentReportList);

			String filePath = "";
			filePath = assignmentService.getXlsxforAssignmentReport(assignmentReportList);

			OutputStream outStream = null;
			FileInputStream inputStream = null;
			ServletContext context = request.getSession().getServletContext();
			File downloadFile = new File(filePath);
			try {
				inputStream = new FileInputStream(downloadFile);
				outStream = response.getOutputStream();

				// get MIME type of the file
				String mimeType = context.getMimeType(filePath);
				if (mimeType == null) {
					// set to binary type if MIME mapping not found
					mimeType = "application/octet-stream";
				}

				response.setContentLength((int) downloadFile.length());

				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
				response.setHeader(headerKey, headerValue);

				IOUtils.copy(inputStream, outStream);
			} catch (Exception e) {
				logger.error("Exception", e);
			} finally {
				if (inputStream != null)
					IOUtils.closeQuietly(inputStream);
				if (outStream != null)
					IOUtils.closeQuietly(outStream);
			}
			return null;
		}
		
		@Secured({ "ROLE_SUPPORT_ADMIN_REPORT","ROLE_USER","ROLE_ADMIN" ,"ROLE_FACULTY" })
		@RequestMapping(value = "/GetAssignmentFromCourse", method = { RequestMethod.GET })
		public @ResponseBody String GetAssignmentFromCourse(
				@RequestParam String courses, Principal principal) {
			String username = principal.getName();
			Token userdetails1 = (Token) principal;

			List<String> courseList = Arrays.asList(courses
					.split("\\s*,\\s*"));
			List<Assignment> AssignmentList = new ArrayList<Assignment>();

			if (userdetails1.getAuthorities().contains(Role.ROLE_ADMIN)) {
				AssignmentList = assignmentService
						.findAllAssignmentWithCourseIdsForAdmin(courseList);

			}

			JSONArray jsonarray = new JSONArray();
			try {
				for (Assignment a : AssignmentList) {
					JSONObject obj = new JSONObject();
					obj.put("value", a.getId().toString());
					obj.put("text", a.getAssignmentName());
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
		
}
