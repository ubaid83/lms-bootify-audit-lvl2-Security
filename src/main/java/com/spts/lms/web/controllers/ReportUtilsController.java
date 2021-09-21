package com.spts.lms.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jfree.chart.plot.PlotOrientation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.spts.lms.auth.Token;
import com.spts.lms.beans.assignment.Assignment;
import com.spts.lms.beans.assignment.StudentAssignment;
import com.spts.lms.beans.content.Content;
import com.spts.lms.beans.content.StudentContent;
import com.spts.lms.beans.course.Course;
import com.spts.lms.beans.course.UserCourse;
import com.spts.lms.beans.feedback.StudentFeedback;
import com.spts.lms.beans.feedback.StudentFeedbackResponse;
import com.spts.lms.beans.test.StudentTest;
import com.spts.lms.beans.test.Test;
import com.spts.lms.beans.user.Role;
import com.spts.lms.beans.user.User;
import com.spts.lms.reportUtils.BarChartUtil;
import com.spts.lms.reportUtils.PieChartUtil;
import com.spts.lms.reportUtils.ReportUtils;
import com.spts.lms.reportUtils.StackedBarChartUtil;
import com.spts.lms.services.assignment.AssignmentService;
import com.spts.lms.services.assignment.StudentAssignmentService;
import com.spts.lms.services.content.ContentService;
import com.spts.lms.services.content.StudentContentService;
import com.spts.lms.services.course.UserCourseService;
import com.spts.lms.services.feedback.StudentFeedbackResponseService;
import com.spts.lms.services.feedback.StudentFeedbackService;
import com.spts.lms.services.test.StudentTestService;
import com.spts.lms.services.test.TestService;
import com.spts.lms.services.user.UserService;
import com.spts.lms.web.controllers.admit.RequestParams;
import com.spts.lms.web.helper.WebPage;

@RestController
public class ReportUtilsController extends BaseController {
	@Autowired
	ApplicationContext act;
	@Autowired
	PieChartUtil pieChartUtil;

	@Autowired
	BarChartUtil barChartUtil;

	@Autowired
	ReportUtils reportUtils;

	@Autowired
	StackedBarChartUtil stackedBarChartUtil;

	@Autowired
	AssignmentService assignmentService;

	@Autowired
	StudentAssignmentService studentAssignmentService;

	@Autowired
	TestService testService;

	@Autowired
	StudentTestService studentTestService;

	@Autowired
	UserCourseService userCourseService;

	@Autowired
	StudentFeedbackService studentFeedbackService;

	@Autowired
	ContentService contentService;

	@Autowired
	StudentContentService studentContentService;

	@Autowired
	UserService userService;

	@Autowired
	StudentFeedbackResponseService studentFeedbackResponseService;

	@Value("${assignmentReportFolder}")
	private String assignmentReportFolder;

	@Value("${testReportFolder}")
	private String testReportFolder;

	@Value("${workStoreDir:''}")
	private String workDir;
	
	@Value("${app}")
	private String app;

	private static final Logger logger = Logger
			.getLogger(ReportController.class);

	@RequestMapping(value = "/create3DPieChartForCourseTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForCourseTest(Principal principal,
			@RequestParam Long courseId, @RequestParam Long testId,
			HttpServletResponse resp) {

		Token userdetails = (Token) principal;

		List<StudentTest> testList = new ArrayList<StudentTest>();
		// testList =
		// studentTestService.findTestScoreByCourseListByFaculty(testId);
		testList = studentTestService.getStudentForTestWithScores(testId,
				courseId);
		Integer passed = 0;
		Integer failed = 0;
		Integer NA = 0;
		Integer passScore = 0;
		Course c = courseService.findByID(Long.valueOf(courseId));
		Test t = testService.findByID(testId);
		Map<String, String> dataMap = new HashMap<String, String>();
		for (StudentTest st : testList) {
			passScore = st.getPassScore();
			if (st.getScore() == null || st.getScore() == 0) {
				
				NA++;
			} else {
				if (Double.valueOf(st.getScore()) >= passScore) {
					
					passed++;

				} else {
					
					failed++;

				}
			}

		}
		
		if(testList.size()>0){
		dataMap.put("Passed", String.valueOf((passed * 100) / testList.size()));
		dataMap.put("failed", String.valueOf((failed * 100) / testList.size()));
		dataMap.put("Not Attempted",
				String.valueOf((NA * 100) / testList.size()));
		}
		// dataMap.put("TestName", t.getTestName());
		String projectUrl = "";
		try {
			File ip = new File(reportUtils.create3DPieChart(dataMap,
					t.getTestName(), true, false, false));

			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */

			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/createSimpleBarChart", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void createSimpleBarChart(@RequestParam Long courseId,
			@RequestParam Long assignmentId, HttpServletResponse resp,
			Principal principal) {
		StudentAssignment sa = new StudentAssignment();
		Token userdetails = (Token) principal;
		List<StudentAssignment> assignmentList = new ArrayList<StudentAssignment>();

		assignmentList = studentAssignmentService
				.findOneAssignment(assignmentId);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Assignment a = assignmentService.findByID(assignmentId);

		try {
			File ip = new File(reportUtils.createSimpleBarChart(assignmentList,
					"Assignment Marks", a.getAssignmentName() + " Marks",
					"Students", "Marks", PlotOrientation.VERTICAL, true, true,
					false));
			resp.setHeader("Content-Type", "image/jpeg");
			IOUtils.copy(new FileInputStream(ip), resp.getOutputStream());
			resp.setContentLength((int) ip.getTotalSpace());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	@RequestMapping(value = "/createSimpleLineChartForAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createSimpleLineChartForAssignment(
			@RequestParam Long courseId, @RequestParam Long assignmentId,
			HttpServletResponse resp, Principal principal) {
		Token userdetails = (Token) principal;
		List<StudentAssignment> assignmentList = new ArrayList<StudentAssignment>();

		assignmentList = studentAssignmentService
				.getStudentsForAssignmentWithScores(assignmentId, courseId);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Assignment a = assignmentService.findByID(assignmentId);
		String projectUrl = "";
		try {

			File ip = new File(reportUtils.createSimpleLineChartForAssignment(
					a.getAssignmentName() + " Marks", assignmentList,
					"Assignment", "Students", "Marks",
					PlotOrientation.VERTICAL, true, true, false));

			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */
			// request.getContextPath()+
			
			
			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/createLineChartForStdDeviationAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createLineChartForStdDeviationAssignment(
			@RequestParam Long courseId, @RequestParam Long assignmentId,
			HttpServletResponse resp, Principal principal) {
		Token userdetails = (Token) principal;
		List<StudentAssignment> assignmentList = new ArrayList<StudentAssignment>();

		assignmentList = studentAssignmentService
				.getStudentsForAssignmentWithScores(assignmentId, courseId);

		Integer avg = 0;
		double mean = 0.0;
		double stdDev = 0;
		Integer totalScore = 0;
		Integer squaredTotal = 0;
		List<String> scoresList = new ArrayList<String>();

		for (StudentAssignment sa : assignmentList) {
			if (sa.getScore() == null) {
				sa.setScore("0");
			} else {

			}
			totalScore += Integer.valueOf(sa.getScore());
			scoresList.add(sa.getScore());

		}
		avg = totalScore / assignmentList.size();
		for (String s : scoresList) {

			squaredTotal += (Integer.valueOf(s) - avg)
					* (Integer.valueOf(s) - avg);
		}
		mean = squaredTotal / assignmentList.size();
		stdDev = Math.sqrt(mean);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Assignment a = assignmentService.findByID(assignmentId);
		String projectUrl = "";
		try {

			File ip = new File(
					reportUtils.createLineChartForStdDeviationForAssignment(
							a.getAssignmentName() + " Marks", assignmentList,
							(int) stdDev, "Assignment Marks", "Students",
							"Marks", PlotOrientation.VERTICAL, true, true,
							false));

			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */

			
			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/createLineChartForStdDeviationTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createLineChartForStdDeviationTest(
			@RequestParam Long courseId, @RequestParam Long testId,
			HttpServletResponse resp, Principal principal) {
		Token userdetails = (Token) principal;
		List<StudentTest> testList = new ArrayList<StudentTest>();

		testList = studentTestService.getStudentForTestWithScores(testId,
				courseId);

		double avg = 0.0;
		double mean = 0.0;
		double stdDev = 0;
		double totalScore = 0.0;
		double squaredTotal = 0.0;
		List<Double> scoresList = new ArrayList<Double>();

		for (StudentTest sa : testList) {
			if (sa.getScore() == null) {
				sa.setScore(0.0);
			} else {

			}
			totalScore += Double.valueOf(sa.getScore());
			scoresList.add(sa.getScore());

		}
		avg = totalScore / testList.size();
		for (Double s : scoresList) {

			squaredTotal += (s - avg) * (s - avg);
		}
		mean = squaredTotal / testList.size();
		stdDev = Math.sqrt(mean);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Test t = testService.findByID(testId);
		String projectUrl = "";
		try {

			File ip = new File(
					reportUtils.createLineChartForStdDeviationForTest(
							t.getTestName() + " Marks", testList, (int) stdDev,
							"Test Marks", "Students", "Marks",
							PlotOrientation.VERTICAL, true, true, false));

			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */

			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/createSimpleLineChartForTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView createSimpleLineChartForTest(
			@RequestParam Long courseId, @RequestParam Long testId,
			HttpServletResponse resp, Principal principal) {
		Token userdetails = (Token) principal;
		List<StudentTest> testList = new ArrayList<StudentTest>();

		testList = studentTestService.getStudentForTestWithScores(testId,
				courseId);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Test t = testService.findByID(testId);
		String projectUrl = "";
		try {

			File ip = new File(reportUtils.createSimpleLineChartForTest(
					t.getTestName() + " Marks", testList, "Test", "Students",
					"Marks", PlotOrientation.VERTICAL, true, true, false));

			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */

			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DBarChartForAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DBarChartForAssignment(
			@RequestParam Long courseId, @RequestParam Long assignmentId,
			HttpServletResponse resp, Principal principal) {

		StudentAssignment sa = new StudentAssignment();
		Token userdetails = (Token) principal;
		List<StudentAssignment> assignmentList = new ArrayList<StudentAssignment>();

		assignmentList = studentAssignmentService
				.getStudentsForAssignmentWithScores(assignmentId, courseId);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Assignment a = assignmentService.findByID(assignmentId);
		String projectUrl = "";

		try {

			File ip = new File(reportUtils.create3DBarChart(assignmentList,
					"Assignment Marks", a.getAssignmentName() + " Marks",
					"Students", "Marks", PlotOrientation.VERTICAL, true, true,
					false));
			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */

			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

		return new ModelAndView("redirect:" + projectUrl);

	}

	@RequestMapping(value = "/create3DBarChartForTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DBarChartForTest(@RequestParam Long courseId,
			@RequestParam Long testId, HttpServletResponse resp,
			Principal principal) {

		StudentTest st = new StudentTest();
		Token userdetails = (Token) principal;
		List<StudentTest> testList = new ArrayList<StudentTest>();

		testList = studentTestService.getStudentForTestWithScores(testId,
				courseId);

		Course c = courseService.findByID(Long.valueOf(courseId));
		Test t = testService.findByID(testId);
		String projectUrl = "";
		try {

			File ip = new File(reportUtils.create3DBarChartForTest(testList,
					"Test Marks", t.getTestName() + " Marks", "Students",
					"Marks", PlotOrientation.VERTICAL, true, true, false));

			/*
			 * File src = ip; File dest = new File(workDir + File.separator +
			 * src.getName());
			 * 
			 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src, dest);
			 */

			//For Test-Server
			/*
			 * projectUrl = File.separator+app+File.separator + "workDir" + File.separator +
			 * dest.getName();
			 */
			
			//For Production Server
			projectUrl = File.separator + "workDir" + File.separator
					+ ip.getName();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DStackedBarChartForStudentAssignment", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DStackedBarChartForStudentAssignment(
			Principal principal,
			@RequestParam(required = false) String studentUsername,
			@RequestParam String courseId, HttpServletResponse resp) {
		StudentAssignment sa = new StudentAssignment();
		Token userdetails = (Token) principal;
		List<StudentAssignment> assignmentList = new ArrayList<StudentAssignment>();
		List<UserCourse> students = userCourseService
				.findStudentsForFaculty(Long.valueOf(courseId));

		if (userdetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			assignmentList = studentAssignmentService.getAssignmentsByStudent(
					Long.valueOf(courseId), principal.getName());
		}

		Course c = courseService.findByID(Long.valueOf(courseId));
		String projectUrl = "";

		try {

			File ip = new File(reportUtils.createStackedBarChartForAssignment(
					assignmentList, "", principal.getName()
							+ " Assignment Marks For " + c.getCourseName(),
					"Assignments", "Marks", PlotOrientation.VERTICAL, true,
					true, false));
			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

		return new ModelAndView("redirect:" + projectUrl);

	}

	@RequestMapping(value = "/create3DStackedBarChartForStudentAssignmentForFaculty", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void create3DStackedBarChartForStudentAssignmentForFaculty(
			Principal principal, @RequestParam String courseId,
			HttpServletResponse resp) {
		StudentAssignment sa = new StudentAssignment();
		Token userdetails = (Token) principal;
		List<StudentAssignment> assignmentList = new ArrayList<StudentAssignment>();
		List<UserCourse> students = userCourseService
				.findStudentsForFaculty(Long.valueOf(courseId));

		Course c = courseService.findByID(Long.valueOf(courseId));
		List<File> files = new ArrayList();
		List<String> username = new ArrayList<String>();
		resp.setContentType("Content-type: text/zip");
		String filename = c.getCourseName() + "Assignmnets" + ".zip";
		// String filename = "asmita.zip";
		resp.setHeader("Content-Disposition", "attachment; filename="
				+ filename + "");
		String projectUrl = "";

		try {
			for (UserCourse uc : students) {
				assignmentList = studentAssignmentService
						.getAssignmentsByStudent(Long.valueOf(courseId),
								uc.getUsername());

				File ip = new File(
						reportUtils.createStackedBarChartForAssignment(
								assignmentList, "",
								principal.getName() + " Assignment Marks For "
										+ c.getCourseName(), "Assignments",
								"Marks", PlotOrientation.VERTICAL, true, true,
								false));
				/*
				 * File src = ip;
				 * logger.info("SRC filepath "+src.getAbsolutePath()); File dest
				 * = new File(workDir + File.separator + src.getName());
				 * 
				 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src,
				 * dest);
				 */
				/*
				 * projectUrl = File.separator + "workDir" + File.separator +
				 * dest.getName(); 
				 * 
				 * File newFile = new File(projectUrl);
				 */
				files.add(ip);
				username.add(uc.getUsername());

			}
			ServletOutputStream out = resp.getOutputStream();

			Integer index = 0;
			File folderPath = new File(workDir + "assignmentReportFolder");
			for (File file : files) {
				

				try {
					String ext1 = FilenameUtils.getExtension(file.getName());
					File newfile = new File(workDir + "assignmentReportFolder"
							+ File.separator + username.get(index) + "." + ext1);

					if (!folderPath.exists()) {
						folderPath.mkdirs();
					}
					index++;

					FileUtils.copyFile(file, newfile);

				} catch (Exception e) {
					logger.error("Ëxception", e);
				}

				
			}

			pack(folderPath.getAbsolutePath(), out);
			FileUtils.deleteDirectory(folderPath);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	@RequestMapping(value = "/create3DStackedBarChartForStudentTestForFaculty", method = {
			RequestMethod.GET, RequestMethod.POST })
	public void create3DStackedBarChartForStudentTestForFaculty(
			Principal principal, @RequestParam String courseId,
			HttpServletResponse resp) {

		
		StudentTest st = new StudentTest();
		Token userdetails = (Token) principal;

		List<StudentTest> testList = new ArrayList<StudentTest>();
		List<UserCourse> students = userCourseService
				.findStudentsForFaculty(Long.valueOf(courseId));

		Course c = courseService.findByID(Long.valueOf(courseId));
		List<File> files = new ArrayList();
		List<String> username = new ArrayList<String>();
		resp.setContentType("Content-type: text/zip");
		String filename = c.getCourseName() + "Tests" + ".zip";
		// String filename = "asmita.zip";
		resp.setHeader("Content-Disposition", "attachment; filename="
				+ filename + "");

		String projectUrl = "";

		

		try {
			for (UserCourse uc : students) {

				testList = studentTestService.getTestsByStudent(
						Long.valueOf(courseId), uc.getUsername());

				File ip = new File(reportUtils.createStackedBarChartForTest(
						testList,
						"",
						uc.getUsername() + " Test Marks For "
								+ c.getCourseName(), "Tests", "Marks",
						PlotOrientation.VERTICAL, true, true, false));

				/*
				 * File src = ip; File dest = new File(workDir + File.separator
				 * + src.getName());
				 * 
				 * if (dest.exists()) dest.delete(); FileUtils.copyFile(src,
				 * dest);
				 * 
				 * projectUrl = File.separator + "workDir" + File.separator +
				 * dest.getName(); 
				 * 
				 * File newFile = new File(projectUrl);
				 */
				files.add(ip);
				username.add(uc.getUsername());

			}
			ServletOutputStream out = resp.getOutputStream();

			Integer index = 0;
			File folderPath = new File(testReportFolder);
			if (!folderPath.exists()) {
				folderPath.mkdirs();
			}
			for (File file : files) {
				

				try {
					String ext1 = FilenameUtils.getExtension(file.getName());
					File newfile = new File(folderPath.getAbsolutePath()
							+ File.separator + username.get(index) + "." + ext1);
					index++;

					FileUtils.copyFile(file, newfile);

				} catch (Exception e) {
					logger.error("Ëxception", e);
				}

				
			}

			pack(folderPath.getAbsolutePath(), out);
			FileUtils.deleteDirectory(folderPath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

	}

	/*
	 * public File fileToPDF(String folderPath, String outputFile, String
	 * inputFile) { File root = new File("C:/Users/Asmita.P/Documents/Reports");
	 * String outputFile = "output.pdf"; List<String> files = new
	 * ArrayList<String>(); files.add("create3DPieChartForCourseTest-SOD.jpg");
	 * // files.add("page2.jpg");
	 * 
	 * Document document = new Document(); PdfWriter.getInstance(document, new
	 * FileOutputStream(new File(folderPath, outputFile))); document.open(); for
	 * (String f : files) { document.newPage(); document.setPageSize(new
	 * Rectangle(500, 400)); Image image = Image.getInstance(new File(root,
	 * f).getAbsolutePath()); image.setAbsolutePosition(0, 1000);
	 * image.setBorderWidth(0); ///=image.scaleAbsolute(PageSize.A4);
	 * image.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
	 * //image.scaleToFit(500, 400); //image.scaleAbsolute(500, 400);
	 * document.add(image); } document.close(); return file; }
	 */

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

	@RequestMapping(value = "/create3DStackedBarChartForStudentTest", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DStackedBarChartForStudentTest(
			Principal principal,
			@RequestParam(required = false) String studentUsername,
			@RequestParam String courseId, HttpServletResponse resp) {

		
		StudentTest st = new StudentTest();
		Token userdetails = (Token) principal;

		List<StudentTest> testList = new ArrayList<StudentTest>();

		

		if (userdetails.getAuthorities().contains(Role.ROLE_FACULTY)
				|| userdetails.getAuthorities().contains(Role.ROLE_ADMIN)) {
			testList = studentTestService.getTestsByStudent(
					Long.valueOf(courseId), studentUsername);

		}
		if (userdetails.getAuthorities().contains(Role.ROLE_STUDENT)) {
			testList = studentTestService.getTestsByStudent(
					Long.valueOf(courseId), principal.getName());
		}

		Course c = courseService.findByID(Long.valueOf(courseId));
		String projectUrl = "";

		try {

			File ip = new File(reportUtils.createStackedBarChartForTest(
					testList, "",
					studentUsername + " Test Marks For " + c.getCourseName(),
					"Tests", "Marks", PlotOrientation.VERTICAL, true, true,
					false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}

		return new ModelAndView("redirect:" + projectUrl);

	}

	@RequestMapping(value = "/create3DPieChartForSemesterYearForProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForSemesterYearForProgram(
			Principal principal, @RequestParam String acadSession,
			@RequestParam Integer acadYear, @RequestParam String searchType,
			HttpServletResponse resp) {
		
		searchType = searchType.replaceAll(",", "");

		Token userdetails = (Token) principal;
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;
		if (StringUtils.isEmpty(searchType)) {
			searchType = "All";
		} else {

		}
		switch (searchType) {
		case "Assignment":

			assignmentList = assignmentService
					.findAssignmentsBySessionAndYearForProgram(acadSession,
							acadYear, Long.valueOf(programId));
			total = assignmentList.size();
			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (assignmentList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;
		case "Test":

			testList = testService.findTestsBySessionAndYearForProgram(
					acadSession, acadYear, Long.valueOf(programId));

			total = testList.size();

			dataMap.put("Tests Created", String.valueOf(testList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (testList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;
		case "Content":

			contentList = contentService
					.findContentsBySessionAndYearForProgram(acadSession,
							acadYear, Long.valueOf(programId));

			total = contentList.size();

			dataMap.put("Content Created", String.valueOf(contentList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (contentList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;
		case "Feedback":

			feedbackList = studentFeedbackService
					.findFeedbacksBySessionAndYearForProgram(acadSession,
							acadYear, Long.valueOf(programId));

			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));

			total = feedbackList.size();
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (feedbackList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;

		case "All":

			assignmentList = assignmentService
					.findAssignmentsBySessionAndYearForProgram(acadSession,
							acadYear, Long.valueOf(programId));
			testList = testService.findTestsBySessionAndYearForProgram(
					acadSession, acadYear, Long.valueOf(programId));

			contentList = contentService
					.findContentsBySessionAndYearForProgram(acadSession,
							acadYear, Long.valueOf(programId));

			feedbackList = studentFeedbackService
					.findFeedbacksBySessionAndYearForProgram(acadSession,
							acadYear, Long.valueOf(programId));

			total = assignmentList.size() + testList.size()
					+ contentList.size() + feedbackList.size();

			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			dataMap.put("Tests Created", String.valueOf(testList.size()));
			dataMap.put("Content Created", String.valueOf(contentList.size()));
			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - ((assignmentList.size() / total)
								+ (testList.size() / total)
								+ (contentList.size() / total) + (feedbackList
								.size() / total))));
			} else {
				dataMap.put("None", "100");
			}

			break;

		default:
			break;
		}

		String projectUrl = "";
		try {
			File ip = new File(reportUtils.create3DPieChart(dataMap,
					"Usage Report for " + userdetails.getProgramName() + " "
							+ acadSession + " " + acadYear, true, false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DPieChartForProgram", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForProgram(Principal principal,
			@RequestParam(required = false) String searchType,
			HttpServletResponse resp) {
		
		searchType = searchType.replaceAll(",", "");
		Token userdetails = (Token) principal;
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;
		if (StringUtils.isEmpty(searchType)) {
			searchType = "All";
		} else {

		}
		switch (searchType) {
		case "Assignment":

			assignmentList = assignmentService.findAllAssignmentForHOD(Long
					.valueOf(programId));
			total = assignmentList.size();
			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (assignmentList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;
		case "Test":

			testList = testService.findAllTestForProgram(Long
					.valueOf(programId));

			total = testList.size();

			dataMap.put("Tests Created", String.valueOf(testList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (testList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;
		case "Content":

			contentList = contentService.findAllContentForProgram(Long
					.valueOf(programId));

			total = contentList.size();

			dataMap.put("Content Created", String.valueOf(contentList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (contentList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;
		case "Feedback":

			feedbackList = studentFeedbackService
					.findAllFeedbackForProgram(Long.valueOf(programId));
			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));

			total = feedbackList.size();
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - (feedbackList.size() / total)));
			} else {
				dataMap.put("None", "100");
			}
			break;

		case "All":

			assignmentList = assignmentService.findAllAssignmentForHOD(Long
					.valueOf(programId));
			testList = testService.findAllTestForProgram(Long
					.valueOf(programId));

			contentList = contentService.findAllContentForProgram(Long
					.valueOf(programId));

			feedbackList = studentFeedbackService
					.findAllFeedbackForProgram(Long.valueOf(programId));

			total = assignmentList.size() + testList.size()
					+ contentList.size() + feedbackList.size();

			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			dataMap.put("Tests Created", String.valueOf(testList.size()));
			dataMap.put("Content Created", String.valueOf(contentList.size()));
			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - ((assignmentList.size() / total)
								+ (testList.size() / total)
								+ (contentList.size() / total) + (feedbackList
								.size() / total))));
			} else {
				dataMap.put("None", "100");
			}

			break;

		default:
			break;
		}

		String projectUrl = "";
		try {
			File ip = new File(reportUtils.create3DPieChart(dataMap,
					"Usage Report for " + userdetails.getProgramName(), true,
					false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DPieChartForCourse", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForCourse(Principal principal,
			@RequestParam(required = false) String searchType,
			HttpServletResponse resp, @RequestParam Long courseId) {
		
		searchType = searchType.replaceAll(",", "");
		Token userdetails = (Token) principal;
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;
		
		Course c = courseService.findByID(courseId);
		if (StringUtils.isEmpty(searchType)) {
			searchType = "All";
		} else {

		}
		if (courseId != null) {

			switch (searchType) {
			case "Assignment":

				assignmentList = assignmentService.findByCourse(courseId);
				total = assignmentList.size();
				dataMap.put("Assignments Created",
						String.valueOf(assignmentList.size()));
				if (total != 0) {
					dataMap.put("None", String.valueOf(100 - (assignmentList
							.size() / total)));
				} else {
					dataMap.put("None", "100");
				}
				break;
			case "Test":

				testList = testService.findByCourse(courseId);

				total = testList.size();

				dataMap.put("Tests Created", String.valueOf(testList.size()));
				if (total != 0) {
					dataMap.put("None",
							String.valueOf(100 - (testList.size() / total)));
				} else {
					dataMap.put("None", "100");
				}
				break;
			case "Content":

				contentList = contentService.findByCourse(courseId);

				total = contentList.size();

				dataMap.put("Content Created",
						String.valueOf(contentList.size()));
				if (total != 0) {
					dataMap.put("None",
							String.valueOf(100 - (contentList.size() / total)));
				} else {
					dataMap.put("None", "100");
				}
				break;
			case "Feedback":

				feedbackList = studentFeedbackService
						.findFeedbackAllocatedToCourse(courseId);
				dataMap.put("Feedback created",
						String.valueOf(feedbackList.size()));

				total = feedbackList.size();
				if (total != 0) {
					dataMap.put("None",
							String.valueOf(100 - (feedbackList.size() / total)));
				} else {
					dataMap.put("None", "100");
				}

				break;

			case "All":

				assignmentList = assignmentService.findByCourse(courseId);
				
				testList = testService.findByCourse(courseId);
				
				contentList = contentService.findByCourse(courseId);
				
				feedbackList = studentFeedbackService
						.findFeedbackAllocatedToCourse(courseId);
				
				total = assignmentList.size() + testList.size()
						+ contentList.size() + feedbackList.size();

				dataMap.put("Assignments Created",
						String.valueOf(assignmentList.size()));
				dataMap.put("Tests Created", String.valueOf(testList.size()));
				dataMap.put("Content Created",
						String.valueOf(contentList.size()));
				dataMap.put("Feedback created",
						String.valueOf(feedbackList.size()));
				if (total != 0) {
					dataMap.put("None", String.valueOf(100 - ((assignmentList
							.size() / total)
							+ (testList.size() / total)
							+ (contentList.size() / total) + (feedbackList
							.size() / total))));
				} else {
					dataMap.put("None", "100");
				}

				break;

			default:
				break;
			}
		}

		String projectUrl = "";
		try {
			File ip = new File(
					reportUtils.create3DPieChart(dataMap, "Usage Report for "
							+ c.getCourseName(), true, false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DPieChartForSemesterYearForCollege", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForSemesterYearForCollege(
			Principal principal, @RequestParam String acadSession,
			@RequestParam Integer acadYear, @RequestParam String searchType,
			HttpServletResponse resp) {

		Token userdetails = (Token) principal;
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;

		switch (searchType) {
		case "Assignment":

			assignmentList = assignmentService
					.findAssignmentsBySessionAndYearForCollege(acadSession,
							acadYear);
			total = assignmentList.size();
			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));

			break;
		case "Test":

			testList = testService.findTestsBySessionAndYearForCollege(
					acadSession, acadYear);

			total = testList.size();

			dataMap.put("Tests Created", String.valueOf(testList.size()));

			break;
		case "Content":

			contentList = contentService
					.findContentsBySessionAndYearForCollege(acadSession,
							acadYear);

			total = contentList.size();

			dataMap.put("Content Created", String.valueOf(contentList.size()));

			break;
		case "Feedback":

			feedbackList = studentFeedbackService
					.findFeedbacksBySessionAndYearForCollege(acadSession,
							acadYear);

			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));

			total = feedbackList.size();

			break;

		case "All":

			assignmentList = assignmentService
					.findAssignmentsBySessionAndYearForCollege(acadSession,
							acadYear);
			testList = testService.findTestsBySessionAndYearForCollege(
					acadSession, acadYear);

			contentList = contentService
					.findContentsBySessionAndYearForCollege(acadSession,
							acadYear);

			feedbackList = studentFeedbackService
					.findFeedbacksBySessionAndYearForCollege(acadSession,
							acadYear);

			total = assignmentList.size() + testList.size()
					+ contentList.size() + feedbackList.size();

			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			dataMap.put("Tests Created", String.valueOf(testList.size()));
			dataMap.put("Content Created", String.valueOf(contentList.size()));
			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - ((assignmentList.size() / total)
								+ (testList.size() / total)
								+ (contentList.size() / total) + (feedbackList
								.size() / total))));
			} else {
				dataMap.put("None", "100");
			}

			break;

		default:
			break;
		}

		String projectUrl = "";
		try {
			File ip = new File(reportUtils.create3DPieChart(dataMap,
					"Usage Report for " + acadSession + " " + acadYear, true,
					false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DPieChartForSemesterYearForCollegeAll", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForSemesterYearForCollegeAll(
			Principal principal,
			@RequestParam(required = false) String searchType,
			HttpServletResponse resp) {

		
		searchType = searchType.replaceAll(",", "");
		Token userdetails = (Token) principal;
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;
		if (StringUtils.isEmpty(searchType)) {
			searchType = "All,";
		} else {

		}

		switch (searchType) {
		case "Assignment":

			assignmentList = assignmentService.findAllActive();
			total = assignmentList.size();
			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));

			dataMap.put("None",
					String.valueOf(100 - (assignmentList.size() / total)));

			break;
		case "Test":

			testList = testService.findAllActive();

			total = testList.size();

			dataMap.put("Tests Created", String.valueOf(testList.size()));

			dataMap.put("None", String.valueOf(100 - (testList.size() / total)));

			break;
		case "Content":

			contentList = contentService.findAllActive();
			total = contentList.size();

			dataMap.put("Content Created", String.valueOf(contentList.size()));

			dataMap.put("None",
					String.valueOf(100 - (contentList.size() / total)));

			break;
		case "Feedback":

			feedbackList = studentFeedbackService.findAll();

			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));

			total = feedbackList.size();

			dataMap.put("None",
					String.valueOf(100 - (feedbackList.size() / total)));

			break;

		case "All":
			

			assignmentList = assignmentService.findAllActive();
			testList = testService.findAllActive();

			contentList = contentService.findAllActive();

			feedbackList = studentFeedbackService.findAll();
			total = assignmentList.size() + testList.size()
					+ contentList.size() + feedbackList.size();

			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			dataMap.put("Tests Created", String.valueOf(testList.size()));
			dataMap.put("Content Created", String.valueOf(contentList.size()));
			dataMap.put("Feedback created", String.valueOf(feedbackList.size()));
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - ((assignmentList.size() / total)
								+ (testList.size() / total)
								+ (contentList.size() / total) + (feedbackList
								.size() / total))));
			} else {
				dataMap.put("None", "100");
			}

			break;

		default:
			break;
		}

		String projectUrl = "";
		try {
			File ip = new File(reportUtils.create3DPieChart(dataMap,
					"Usage Report for SOC", true, false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DPieChartForSemesterYearForUser", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForSemesterYearForUser(
			Principal principal, @RequestParam String acadSession,
			@RequestParam Integer acadYear, @RequestParam String searchType,
			HttpServletResponse resp) {

		Token userdetails = (Token) principal;
		String username = principal.getName();
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		List<StudentAssignment> studentAssignmentList = new ArrayList<StudentAssignment>();
		List<StudentTest> studentTestList = new ArrayList<StudentTest>();
		List<StudentContent> studentContentList = new ArrayList<StudentContent>();
		List<StudentFeedbackResponse> studentFeedbackList = new ArrayList<StudentFeedbackResponse>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;

		if (userdetails.getAuthorities().contains(Role.ROLE_STUDENT)) {

			switch (searchType) {
			case "Assignment":

				studentAssignmentList = studentAssignmentService
						.findAssignmentByStudent(username);
				total = studentAssignmentList.size();
				dataMap.put("Assignments Used",
						String.valueOf(studentAssignmentList.size()));

				break;
			case "Test":

				studentTestList = studentTestService
						.findTestByStudent(username);

				total = studentTestList.size();

				dataMap.put("Tests Used",
						String.valueOf(studentTestList.size()));

				break;
			case "Content":

				studentContentList = studentContentService
						.findContentByStudent(username);

				total = studentContentList.size();

				dataMap.put("Content Used",
						String.valueOf(studentContentList.size()));

				break;
			case "Feedback":

				studentFeedbackList = studentFeedbackResponseService
						.findFeedbackResponseByStudent(username);

				total = studentFeedbackList.size();
				dataMap.put("Feedback Given",
						String.valueOf(studentFeedbackList.size()));

				break;

			case "All":

				studentAssignmentList = studentAssignmentService
						.findAssignmentByStudent(username);
				studentTestList = studentTestService
						.findTestByStudent(username);

				studentContentList = studentContentService
						.findContentByStudent(username);

				studentFeedbackList = studentFeedbackResponseService
						.findFeedbackResponseByStudent(username);

				total = studentAssignmentList.size() + studentTestList.size()
						+ studentContentList.size()
						+ studentFeedbackList.size();

				dataMap.put("Assignments Used",
						String.valueOf(studentAssignmentList.size()));
				dataMap.put("Tests Used",
						String.valueOf(studentTestList.size()));
				dataMap.put("Content Used",
						String.valueOf(studentContentList.size()));
				dataMap.put("Feedback given",
						String.valueOf(studentFeedbackList.size()));
				if (total != 0) {
					dataMap.put(
							"None",
							String.valueOf(100 - ((studentAssignmentList.size() / total)
									+ (studentTestList.size() / total)
									+ (studentContentList.size() / total) + (studentFeedbackList
									.size() / total))));
				} else {
					dataMap.put("None", "100");
				}

				break;

			default:
				break;
			}

		}

		if (userdetails.getAuthorities().contains(Role.ROLE_FACULTY)) {

			switch (searchType) {
			case "Assignment":

				assignmentList = assignmentService
						.findAllAssignmentsByFaculty(username);
				total = assignmentList.size();
				dataMap.put("Assignments Created",
						String.valueOf(assignmentList.size()));

				break;
			case "Test":

				testList = testService.findAllTestsByFaculty(username);

				total = testList.size();

				dataMap.put("Tests Created", String.valueOf(testList.size()));

				break;
			case "Content":

				contentList = contentService.findAllConetntsByFaculty(username);

				total = contentList.size();

				dataMap.put("Content Created",
						String.valueOf(contentList.size()));

				break;
			case "Feedback":

				/*
				 * feedbackList = studentFeedbackService
				 * .findFeedbacksBySessionAndYearForCollege(acadSession,
				 * acadYear);
				 * 
				 * dataMap.put("Feedback created",
				 * String.valueOf(feedbackList.size()));
				 * 
				 * total = feedbackList.size();
				 */

				break;

			case "All":

				assignmentList = assignmentService
						.findAllAssignmentsByFaculty(username);
				testList = testService.findAllTestsByFaculty(username);

				contentList = contentService.findAllConetntsByFaculty(username);

				/*
				 * feedbackList = studentFeedbackService
				 * .findFeedbacksBySessionAndYearForCollege(acadSession,
				 * acadYear);
				 */

				total = assignmentList.size() + testList.size()
						+ contentList.size();

				dataMap.put("Assignments Created",
						String.valueOf(assignmentList.size()));
				dataMap.put("Tests Created", String.valueOf(testList.size()));
				dataMap.put("Content Created",
						String.valueOf(contentList.size()));
				/*
				 * dataMap.put("Feedback created",
				 * String.valueOf(feedbackList.size()));
				 */
				if (total != 0) {
					dataMap.put(
							"None",
							String.valueOf(100 - ((assignmentList.size() / total)
									+ (testList.size() / total) + (contentList
									.size() / total))));
				} else {
					dataMap.put("None", "100");
				}

				break;

			default:
				break;
			}

		}
		String projectUrl = "";
		User u = userService.findByUserName(username);
		try {
			File ip = new File(reportUtils.create3DPieChart(
					dataMap,
					"Usage Report for Student " + username + "-"
							+ u.getFirstname() + " " + u.getLastname(), true,
					false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}

	@RequestMapping(value = "/create3DPieChartForUser", method = {
			RequestMethod.GET, RequestMethod.POST })
	public ModelAndView create3DPieChartForUser(Principal principal,
			@RequestParam String username, @RequestParam String role,
			HttpServletResponse resp) {

		Token userdetails = (Token) principal;
		// String username = principal.getName();
		String programId = userdetails.getProgramId();
		List<Assignment> assignmentList = new ArrayList<Assignment>();
		List<Test> testList = new ArrayList<Test>();
		List<Content> contentList = new ArrayList<Content>();
		List<StudentFeedback> feedbackList = new ArrayList<StudentFeedback>();
		List<StudentAssignment> studentAssignmentList = new ArrayList<StudentAssignment>();
		List<StudentTest> studentTestList = new ArrayList<StudentTest>();
		List<StudentContent> studentContentList = new ArrayList<StudentContent>();
		List<StudentFeedbackResponse> studentFeedbackList = new ArrayList<StudentFeedbackResponse>();
		Map<String, String> dataMap = new HashMap<String, String>();
		Integer total = 0;
		

		if (role.equals("ROLE_STUDENT")) {

			studentAssignmentList = studentAssignmentService
					.findAssignmentByStudent(username);
			studentTestList = studentTestService.findTestByStudent(username);

			studentContentList = studentContentService
					.findContentByStudent(username);

			studentFeedbackList = studentFeedbackResponseService
					.findFeedbackResponseByStudent(username);

			total = studentAssignmentList.size() + studentTestList.size()
					+ studentContentList.size() + studentFeedbackList.size();

			dataMap.put("Assignments Used",
					String.valueOf(studentAssignmentList.size()));
			dataMap.put("Tests Used", String.valueOf(studentTestList.size()));
			dataMap.put("Content Used",
					String.valueOf(studentContentList.size()));
			dataMap.put("Feedback given",
					String.valueOf(studentFeedbackList.size()));

			if (total != 0) {
				dataMap.put(
						"None",
						String.valueOf(100 - ((studentAssignmentList.size() / total)
								+ (studentTestList.size() / total)
								+ (studentContentList.size() / total) + (studentFeedbackList
								.size() / total))));
			} else {
				dataMap.put("None", "100");
			}

		}

		if (role.equals("ROLE_FACULTY")) {

			assignmentList = assignmentService
					.findAllAssignmentsByFaculty(username);
			testList = testService.findAllTestsByFaculty(username);

			contentList = contentService.findAllConetntsByFaculty(username);

			/*
			 * feedbackList = studentFeedbackService
			 * .findFeedbacksBySessionAndYearForCollege(acadSession, acadYear);
			 */

			total = assignmentList.size() + testList.size()
					+ contentList.size();

			dataMap.put("Assignments Created",
					String.valueOf(assignmentList.size()));
			dataMap.put("Tests Created", String.valueOf(testList.size()));

			dataMap.put("Content Created", String.valueOf(contentList.size()));
			/*
			 * dataMap.put("Feedback created",
			 * String.valueOf(feedbackList.size()));
			 */
			if (total != 0) {
				dataMap.put("None",
						String.valueOf(100 - ((assignmentList.size() / total)
								+ (testList.size() / total) + (contentList
								.size() / total))));
			} else {
				dataMap.put("None", "100");
			}

		}
		String projectUrl = "";
		User u = userService.findByUserName(username);
		try {
			File ip = new File(reportUtils.create3DPieChart(
					dataMap,
					"Usage Report for Student " + username + "-"
							+ u.getFirstname() + " " + u.getLastname(), true,
					false, false));

			File src = ip;
			File dest = new File(workDir + File.separator + src.getName());

			if (dest.exists())
				dest.delete();
			FileUtils.copyFile(src, dest);

			//For Test-Server
			projectUrl = File.separator+app+File.separator + "workDir" + File.separator
					+ dest.getName();
			
			//For Production Server
			/*projectUrl = File.separator + "workDir" + File.separator
					+ dest.getName();*/
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception", e);
		}
		return new ModelAndView("redirect:" + projectUrl);
	}
}
